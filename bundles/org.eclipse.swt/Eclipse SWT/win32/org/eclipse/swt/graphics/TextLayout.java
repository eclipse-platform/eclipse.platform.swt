/*******************************************************************************
 * Copyright (c) 2000, 2022 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.graphics;

import java.util.*;

import org.eclipse.swt.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.gdip.*;
import org.eclipse.swt.internal.ole.win32.*;
import org.eclipse.swt.internal.win32.*;

/**
 * <code>TextLayout</code> is a graphic object that represents
 * styled text.
 * <p>
 * Instances of this class provide support for drawing, cursor
 * navigation, hit testing, text wrapping, alignment, tab expansion
 * line breaking, etc.  These are aspects required for rendering internationalized text.
 * </p><p>
 * Application code must explicitly invoke the <code>TextLayout#dispose()</code>
 * method to release the operating system resources managed by each instance
 * when those instances are no longer required.
 * </p>
 *
 * @see <a href="http://www.eclipse.org/swt/snippets/#textlayout">TextLayout, TextStyle snippets</a>
 * @see <a href="http://www.eclipse.org/swt/examples.php">SWT Example: CustomControlExample, StyledText tab</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 *
 * @since 3.0
 */
public final class TextLayout extends Resource {
	Font font;
	String text, segmentsText;
	int lineSpacingInPoints;
	int ascentInPixels, descentInPixels;
	int alignment;
	int wrapWidth;
	int orientation;
	int textDirection;
	int indent;
	int wrapIndent;
	boolean justify;
	int[] tabs;
	int[] segments;
	char[] segmentsChars;
	StyleItem[] styles;
	int stylesCount;

	StyleItem[] allRuns;
	StyleItem[][] runs;
	int[] lineOffset, lineY, lineWidth;
	IMLangFontLink2 mLangFontLink2;
	int verticalIndentInPoints;

	static final char LTR_MARK = '\u200E', RTL_MARK = '\u200F';
	static final int SCRIPT_VISATTR_SIZEOF = 2;
	static final int GOFFSET_SIZEOF = 8;
	static final int MERGE_MAX = 512;
	static final int TOO_MANY_RUNS = 1024;
	/**
	 * Runs over a certain length (32000 characters / 65536 Glyphs / 32768 pixels -
	 * these numbers come from WinAPI docs + analysis done in Bug 23406 Comment 31)
	 * will fail to render in ScriptTextOut, ScriptShape, ScriptPlace so such
	 * long runs need to be split into shorter runs. Because it is expensive to
	 * keep testing (with Script*) to maximize the length we use this heuristic
	 * to minimize the length. However splitting the runs into too short pieces
	 * affects performance, so this is a balance.
	 */
	static final int MAX_RUN_LENGTH = 32000;
	/**
	 * When splitting a run (see {@link #MAX_RUN_LENGTH}) the run needs to split
	 * in a way that does not affect the display of the glyphs, so it is important
	 * to not split the run in the middle of a glyph. We use the same info to find
	 * where we can wrap text to find where we can break the runs (ScriptBreak's info).
	 * This setting limits how far back from {@link #MAX_RUN_LENGTH} the code
	 * will search for a break before forcing a break at {@link #MAX_RUN_LENGTH}.
	 */
	static final int MAX_SEARCH_RUN_BREAK = 1000;
	{
		// While developing the splitting long runs it can be useful to
		// make these constants smaller, but these invariants must
		// preserved even in such cases.
		assert MAX_RUN_LENGTH > 1;
		assert MAX_SEARCH_RUN_BREAK < MAX_RUN_LENGTH;
	}

	/* IME has a copy of these constants */
	static final int UNDERLINE_IME_DOT = 1 << 16;
	static final int UNDERLINE_IME_DASH = 2 << 16;
	static final int UNDERLINE_IME_THICK = 3 << 16;

	class StyleItem {
		TextStyle style;
		int start, length;
		boolean lineBreak, softBreak, tab;

		/*Script cache and analysis */
		SCRIPT_ANALYSIS analysis;
		long psc = 0;

		/*Shape info (malloc when the run is shaped) */
		long glyphs;
		int glyphCount;
		long clusters;
		long visAttrs;

		/*Place info (malloc when the run is placed) */
		long advances;
		long goffsets;
		int width;
		int ascentInPoints;
		int descentInPoints;
		int leadingInPoints;
		int x;
		int underlinePos, underlineThickness;
		int strikeoutPos, strikeoutThickness;

		/* Justify info (malloc during computeRuns) */
		long justify;

		/* ScriptBreak */
		int pslaAllocSize;
		long psla;

		long fallbackFont;

	void free() {
		long hHeap = OS.GetProcessHeap();
		if (psc != 0) {
			OS.ScriptFreeCache (psc);
			OS.HeapFree(hHeap, 0, psc);
			psc = 0;
		}
		if (glyphs != 0) {
			OS.HeapFree(hHeap, 0, glyphs);
			glyphs = 0;
			glyphCount = 0;
		}
		if (clusters != 0) {
			OS.HeapFree(hHeap, 0, clusters);
			clusters = 0;
		}
		if (visAttrs != 0) {
			OS.HeapFree(hHeap, 0, visAttrs);
			visAttrs = 0;
		}
		if (advances != 0) {
			OS.HeapFree(hHeap, 0, advances);
			advances = 0;
		}
		if (goffsets != 0) {
			OS.HeapFree(hHeap, 0, goffsets);
			goffsets = 0;
		}
		if (justify != 0) {
			OS.HeapFree(hHeap, 0, justify);
			justify = 0;
		}
		if (psla != 0) {
			OS.HeapFree(hHeap, 0, psla);
			psla = 0;
		}
		if (fallbackFont != 0) {
			OS.DeleteObject(fallbackFont);
			fallbackFont = 0;
		}
		width = ascentInPoints = descentInPoints = x = 0;
		lineBreak = softBreak = false;
	}
	@Override
	public String toString () {
		return "StyleItem {" + start + ", " + style + "}";
	}
	}

/**
 * Constructs a new instance of this class on the given device.
 * <p>
 * You must dispose the text layout when it is no longer required.
 * </p>
 *
 * @param device the device on which to allocate the text layout
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if device is null and there is no current device</li>
 * </ul>
 *
 * @see #dispose()
 */
public TextLayout (Device device) {
	super(device);
	wrapWidth = ascentInPixels = descentInPixels = -1;
	lineSpacingInPoints = 0;
	verticalIndentInPoints = 0;
	orientation = SWT.LEFT_TO_RIGHT;
	textDirection = SWT.LEFT_TO_RIGHT;
	styles = new StyleItem[2];
	styles[0] = new StyleItem();
	styles[1] = new StyleItem();
	stylesCount = 2;
	text = ""; //$NON-NLS-1$
	long[] ppv = new long[1];
	OS.OleInitialize(0);
	if (COM.CoCreateInstance(COM.CLSID_CMultiLanguage, 0, COM.CLSCTX_INPROC_SERVER, COM.IID_IMLangFontLink2, ppv) == OS.S_OK) {
		mLangFontLink2 = new IMLangFontLink2(ppv[0]);
	}
	init();
}

RECT addClipRect(StyleItem run, RECT clipRect, RECT rect, int selectionStart, int selectionEnd) {
	if (rect != null) {
		if (clipRect == null) {
			clipRect = new RECT ();
			OS.SetRect(clipRect, -1, rect.top, -1, rect.bottom);
		}
		boolean isRTL = (orientation & SWT.RIGHT_TO_LEFT) != 0;
		if (run.start <= selectionStart && selectionStart <= run.start + run.length) {
			if (run.analysis.fRTL ^ isRTL) {
				clipRect.right = rect.left;
			} else {
				clipRect.left = rect.left;
			}
		}
		if (run.start <= selectionEnd && selectionEnd <= run.start + run.length) {
			if (run.analysis.fRTL ^ isRTL) {
				clipRect.left = rect.right;
			} else {
				clipRect.right = rect.right;
			}
		}
	}
	return clipRect;
}

void breakRun(StyleItem run) {
	if (run.psla != 0) return;
	char[] chars = new char[run.length];
	segmentsText.getChars(run.start, run.start + run.length, chars, 0);
	long hHeap = OS.GetProcessHeap();
	run.pslaAllocSize = SCRIPT_LOGATTR.sizeof * chars.length;
	run.psla = OS.HeapAlloc(hHeap, OS.HEAP_ZERO_MEMORY, SCRIPT_LOGATTR.sizeof * chars.length);
	if (run.psla == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	OS.ScriptBreak(chars, chars.length, run.analysis, run.psla);
}

void checkLayout () {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
}

/*
*  Compute the runs: itemize, shape, place, and reorder the runs.
* 	Break paragraphs into lines, wraps the text, and initialize caches.
*/
void computeRuns (GC gc) {
	if (runs != null) return;
	long hDC = gc != null ? gc.handle : device.internal_new_GC(null);
	long srcHdc = OS.CreateCompatibleDC(hDC);
	allRuns = itemize();
	for (int i=0; i<allRuns.length - 1; i++) {
		StyleItem run = allRuns[i];
		OS.SelectObject(srcHdc, getItemFont(run));
		shape(srcHdc, run);
	}
	SCRIPT_LOGATTR logAttr = new SCRIPT_LOGATTR();
	SCRIPT_PROPERTIES properties = new SCRIPT_PROPERTIES();
	int lineWidth = indent, lineStart = 0, lineCount = 1;
	for (int i=0; i<allRuns.length - 1; i++) {
		StyleItem run = allRuns[i];
		if (tabs != null && run.tab) {
			int tabsLength = tabs.length, j;
			for (j = 0; j < tabsLength; j++) {
				if (tabs[j] > lineWidth) {
					run.width = tabs[j] - lineWidth;
					break;
				}
			}
			if (j == tabsLength) {
				int tabX = tabs[tabsLength-1];
				int lastTabWidth = tabsLength > 1 ? tabs[tabsLength-1] - tabs[tabsLength-2] : tabs[0];
				if (lastTabWidth > 0) {
					while (tabX <= lineWidth) tabX += lastTabWidth;
					run.width = tabX - lineWidth;
				}
			}

			/*
			 * This block adjusts the indentation after merged tabs stops.
			 * The extra tabs are removed in merge.
			 */
			int length = run.length;
			if (length > 1) {
				int stop = j + length - 1;
				if (stop < tabsLength) {
					run.width += tabs[stop] - tabs[j];
				} else {
					if (j < tabsLength) {
						run.width += tabs[tabsLength - 1] - tabs[j];
						length -= (tabsLength - 1) - j;
					}
					int lastTabWidth = tabsLength > 1 ? tabs[tabsLength-1] - tabs[tabsLength-2] : tabs[0];
					run.width += lastTabWidth * (length - 1);
				}
			}
		}
		if (wrapWidth != -1 && lineWidth + run.width > wrapWidth && !run.tab && !run.lineBreak) {
			int start = 0;
			int[] piDx = new int[run.length];
			if (run.style != null && run.style.metrics != null) {
				piDx[0] = run.width;
			} else {
				OS.ScriptGetLogicalWidths(run.analysis, run.length, run.glyphCount, run.advances, run.clusters, run.visAttrs, piDx);
			}
			int width = 0, maxWidth = wrapWidth - lineWidth;
			while (width + piDx[start] < maxWidth) {
				width += piDx[start++];
			}
			int firstStart = start;
			int firstIndice = i;
			while (i >= lineStart) {
				breakRun(run);
				while (start >= 0) {
					OS.MoveMemory(logAttr, run.psla + (start * SCRIPT_LOGATTR.sizeof), SCRIPT_LOGATTR.sizeof);
					if (logAttr.fSoftBreak || logAttr.fWhiteSpace) break;
					start--;
				}

				/*
				*  Bug in Windows. For some reason Uniscribe sets the fSoftBreak flag for the first letter
				*  after a letter with an accent. This cause a break line to be set in the middle of a word.
				*  The fix is to detect the case and ignore fSoftBreak forcing the algorithm keep searching.
				*/
				if (start == 0 && i != lineStart && !run.tab) {
					if (logAttr.fSoftBreak && !logAttr.fWhiteSpace) {
						OS.MoveMemory(properties, device.scripts[run.analysis.eScript], SCRIPT_PROPERTIES.sizeof);
						int langID = properties.langid;
						StyleItem pRun = allRuns[i - 1];
						OS.MoveMemory(properties, device.scripts[pRun.analysis.eScript], SCRIPT_PROPERTIES.sizeof);
						if (properties.langid == langID || langID == OS.LANG_NEUTRAL || properties.langid == OS.LANG_NEUTRAL) {
							breakRun(pRun);
							OS.MoveMemory(logAttr, pRun.psla + ((pRun.length - 1) * SCRIPT_LOGATTR.sizeof), SCRIPT_LOGATTR.sizeof);
							if (!logAttr.fWhiteSpace) start = -1;
						}
					}
				}
				if (start >= 0 || i == lineStart) break;
				run = allRuns[--i];
				start = run.length - 1;
			}
			boolean wrapEntireRun = start == 0 && i != lineStart && !run.tab;
			if (wrapEntireRun) {
				breakRun(run);
				OS.MoveMemory(logAttr, run.psla + (start * SCRIPT_LOGATTR.sizeof), SCRIPT_LOGATTR.sizeof);
				wrapEntireRun = !logAttr.fWhiteSpace;
			}
			if (wrapEntireRun) {
				run = allRuns[--i];
				start = run.length;
			} else if (start <= 0 && i == lineStart) {
				/*
				 * No soft-break or line-feed found. Avoid breaking a run at
				 * the first character (firstStart == 0) unless it's the
				 * only run available (firstIndice == lineStart). See bug 408833.
				 */
				if (firstStart == 0 && firstIndice > lineStart) {
					i = firstIndice - 1;
					run = allRuns[i];
					start = run.length;
				} else {
					i = firstIndice;
					run = allRuns[i];
					start = Math.max(1, firstStart);
				}
			}
			breakRun(run);
			while (start < run.length) {
				OS.MoveMemory(logAttr, run.psla + (start * SCRIPT_LOGATTR.sizeof), SCRIPT_LOGATTR.sizeof);
				if (!logAttr.fWhiteSpace) break;
				start++;
			}
			if (0 < start && start < run.length) {
				StyleItem newRun = new StyleItem();
				newRun.start = run.start + start;
				newRun.length = run.length - start;
				newRun.style = run.style;
				newRun.analysis = cloneScriptAnalysis(run.analysis);
				run.free();
				run.length = start;
				OS.SelectObject(srcHdc, getItemFont(run));
				run.analysis.fNoGlyphIndex = false;
				shape (srcHdc, run);
				OS.SelectObject(srcHdc, getItemFont(newRun));
				newRun.analysis.fNoGlyphIndex = false;
				shape (srcHdc, newRun);
				StyleItem[] newAllRuns = new StyleItem[allRuns.length + 1];
				System.arraycopy(allRuns, 0, newAllRuns, 0, i + 1);
				System.arraycopy(allRuns, i + 1, newAllRuns, i + 2, allRuns.length - i - 1);
				allRuns = newAllRuns;
				allRuns[i + 1] = newRun;
			}
			if (i != allRuns.length - 2) {
				run.softBreak = run.lineBreak = true;
			}
		}
		lineWidth += run.width;
		if (run.lineBreak) {
			lineStart = i + 1;
			lineWidth = run.softBreak ?  wrapIndent : indent;
			lineCount++;
		}
	}
	lineWidth = 0;
	runs = new StyleItem[lineCount][];
	lineOffset = new int[lineCount + 1];
	lineY = new int[lineCount + 1];
	this.lineWidth = new int[lineCount];
	int lineRunCount = 0, line = 0;
	int ascentInPoints = Math.max(0, DPIUtil.autoScaleDown(getDevice(), this.ascentInPixels));
	int descentInPoints = Math.max(0, DPIUtil.autoScaleDown(getDevice(), this.descentInPixels));
	StyleItem[] lineRuns = new StyleItem[allRuns.length];
	for (int i=0; i<allRuns.length; i++) {
		StyleItem run = allRuns[i];
		lineRuns[lineRunCount++] = run;
		lineWidth += run.width;
		ascentInPoints = Math.max(ascentInPoints, run.ascentInPoints);
		descentInPoints = Math.max(descentInPoints, run.descentInPoints);
		if (run.lineBreak || i == allRuns.length - 1) {
			/* Update the run metrics if the last run is a hard break. */
			if (lineRunCount == 1 && (i == allRuns.length - 1 || !run.softBreak)) {
				TEXTMETRIC lptm = new TEXTMETRIC();
				OS.SelectObject(srcHdc, getItemFont(run));
				OS.GetTextMetrics(srcHdc, lptm);
				run.ascentInPoints = DPIUtil.autoScaleDown(getDevice(), lptm.tmAscent);
				run.descentInPoints = DPIUtil.autoScaleDown(getDevice(), lptm.tmDescent);
				ascentInPoints = Math.max(ascentInPoints, run.ascentInPoints);
				descentInPoints = Math.max(descentInPoints, run.descentInPoints);
			}
			runs[line] = new StyleItem[lineRunCount];
			System.arraycopy(lineRuns, 0, runs[line], 0, lineRunCount);

			if (justify && wrapWidth != -1 && run.softBreak && lineWidth > 0) {
				int lineIndent = wrapIndent;
				if (line == 0) {
					lineIndent = indent;
				} else {
					StyleItem[] previousLine = runs[line - 1];
					StyleItem previousRun = previousLine[previousLine.length - 1];
					if (previousRun.lineBreak && !previousRun.softBreak) {
						lineIndent = indent;
					}
				}
				lineWidth += lineIndent;
				long hHeap = OS.GetProcessHeap();
				int newLineWidth = 0;
				for (StyleItem item : runs[line]) {
					int iDx = item.width * wrapWidth / lineWidth;
					if (iDx != item.width) {
						item.justify = OS.HeapAlloc(hHeap, OS.HEAP_ZERO_MEMORY, item.glyphCount * 4);
						if (item.justify == 0) SWT.error(SWT.ERROR_NO_HANDLES);
						OS.ScriptJustify(item.visAttrs, item.advances, item.glyphCount, iDx - item.width, 2, item.justify);
						item.width = iDx;
					}
					newLineWidth += item.width;
				}
				lineWidth = newLineWidth;
			}
			this.lineWidth[line] = lineWidth;

			StyleItem lastRun = runs[line][lineRunCount - 1];
			int lastOffset = lastRun.start + lastRun.length;
			runs[line] = reorder(runs[line], i == allRuns.length - 1);
			lastRun = runs[line][lineRunCount - 1];
			if (run.softBreak && run != lastRun) {
				run.softBreak = run.lineBreak = false;
				lastRun.softBreak = lastRun.lineBreak = true;
			}

			lineWidth = getLineIndent(line);
			for (StyleItem run1 : runs[line]) {
				run1.x = lineWidth;
				lineWidth += run1.width;
			}
			line++;
			lineY[line] = lineY[line - 1] + ascentInPoints + descentInPoints + lineSpacingInPoints;
			lineOffset[line] = lastOffset;
			lineRunCount = lineWidth = 0;
			ascentInPoints = Math.max(0, DPIUtil.autoScaleDown(getDevice(), this.ascentInPixels));
			descentInPoints = Math.max(0, DPIUtil.autoScaleDown(getDevice(), this.descentInPixels));
		}
	}
	if (srcHdc != 0) OS.DeleteDC(srcHdc);
	if (gc == null) device.internal_dispose_GC(hDC, null);
}

@Override
void destroy () {
	freeRuns();
	font = null;
	text = null;
	segmentsText = null;
	tabs = null;
	styles = null;
	runs = null;
	lineOffset = null;
	lineY = null;
	lineWidth = null;
	segments = null;
	segmentsChars = null;
	if (mLangFontLink2 != null) {
		mLangFontLink2.Release();
		mLangFontLink2 = null;
	}
	OS.OleUninitialize();
}

SCRIPT_ANALYSIS cloneScriptAnalysis (SCRIPT_ANALYSIS src) {
	SCRIPT_ANALYSIS dst = new SCRIPT_ANALYSIS();
	dst.eScript = src.eScript;
	dst.fRTL = src.fRTL;
	dst.fLayoutRTL = src.fLayoutRTL;
	dst.fLinkBefore = src.fLinkBefore;
	dst.fLinkAfter = src.fLinkAfter;
	dst.fLogicalOrder = src.fLogicalOrder;
	dst.fNoGlyphIndex = src.fNoGlyphIndex;
	dst.s = new SCRIPT_STATE();
	dst.s.uBidiLevel = src.s.uBidiLevel;
	dst.s.fOverrideDirection = src.s.fOverrideDirection;
	dst.s.fInhibitSymSwap = src.s.fInhibitSymSwap;
	dst.s.fCharShape = src.s.fCharShape;
	dst.s.fDigitSubstitute = src.s.fDigitSubstitute;
	dst.s.fInhibitLigate = src.s.fInhibitLigate;
	dst.s.fDisplayZWG = src.s.fDisplayZWG;
	dst.s.fArabicNumContext = src.s.fArabicNumContext;
	dst.s.fGcpClusters = src.s.fGcpClusters;
	dst.s.fReserved = src.s.fReserved;
	dst.s.fEngineReserved = src.s.fEngineReserved;
	return dst;
}

int[] computePolyline(int left, int top, int right, int bottom) {
	int height = bottom - top; // can be any number
	int width = 2 * height; // must be even
	int peaks = Compatibility.ceil(right - left, width);
	if (peaks == 0 && right - left > 2) {
		peaks = 1;
	}
	int length = ((2 * peaks) + 1) * 2;
	if (length < 0) return new int[0];

	int[] coordinates = new int[length];
	for (int i = 0; i < peaks; i++) {
		int index = 4 * i;
		coordinates[index] = left + (width * i);
		coordinates[index+1] = bottom;
		coordinates[index+2] = coordinates[index] + width / 2;
		coordinates[index+3] = top;
	}
	coordinates[length-2] = left + (width * peaks);
	coordinates[length-1] = bottom;
	return coordinates;
}

long createGdipBrush(int pixel, int alpha) {
	int argb = ((alpha & 0xFF) << 24) | ((pixel >> 16) & 0xFF) | (pixel & 0xFF00) | ((pixel & 0xFF) << 16);
	return Gdip.SolidBrush_new(argb);
}

long createGdipBrush(Color color, int alpha) {
	return createGdipBrush(color.handle, alpha);
}

/**
 * Draws the receiver's text using the specified GC at the specified
 * point.
 *
 * @param gc the GC to draw
 * @param x the x coordinate of the top left corner of the rectangular area where the text is to be drawn
 * @param y the y coordinate of the top left corner of the rectangular area where the text is to be drawn
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the gc is null</li>
 * </ul>
 */
public void draw (GC gc, int x, int y) {
	checkLayout();
	drawInPixels(gc, DPIUtil.autoScaleUp(getDevice(), x), DPIUtil.autoScaleUp(getDevice(), y));
}

void drawInPixels (GC gc, int x, int y) {
	drawInPixels(gc, x, y, -1, -1, null, null);
}

/**
 * Draws the receiver's text using the specified GC at the specified
 * point.
 *
 * @param gc the GC to draw
 * @param x the x coordinate of the top left corner of the rectangular area where the text is to be drawn
 * @param y the y coordinate of the top left corner of the rectangular area where the text is to be drawn
 * @param selectionStart the offset where the selections starts, or -1 indicating no selection
 * @param selectionEnd the offset where the selections ends, or -1 indicating no selection
 * @param selectionForeground selection foreground, or NULL to use the system default color
 * @param selectionBackground selection background, or NULL to use the system default color
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the gc is null</li>
 * </ul>
 */
public void draw (GC gc, int x, int y, int selectionStart, int selectionEnd, Color selectionForeground, Color selectionBackground) {
	checkLayout();
	drawInPixels(gc, DPIUtil.autoScaleUp(getDevice(), x), DPIUtil.autoScaleUp(getDevice(), y), selectionStart, selectionEnd, selectionForeground, selectionBackground);
}

void drawInPixels (GC gc, int x, int y, int selectionStart, int selectionEnd, Color selectionForeground, Color selectionBackground) {
	drawInPixels(gc, x, y, selectionStart, selectionEnd, selectionForeground, selectionBackground, 0);
}

/**
 * Draws the receiver's text using the specified GC at the specified
 * point.
 * <p>
 * The parameter <code>flags</code> can include one of <code>SWT.DELIMITER_SELECTION</code>
 * or <code>SWT.FULL_SELECTION</code> to specify the selection behavior on all lines except
 * for the last line, and can also include <code>SWT.LAST_LINE_SELECTION</code> to extend
 * the specified selection behavior to the last line.
 * </p>
 * @param gc the GC to draw
 * @param x the x coordinate of the top left corner of the rectangular area where the text is to be drawn
 * @param y the y coordinate of the top left corner of the rectangular area where the text is to be drawn
 * @param selectionStart the offset where the selections starts, or -1 indicating no selection
 * @param selectionEnd the offset where the selections ends, or -1 indicating no selection
 * @param selectionForeground selection foreground, or NULL to use the system default color
 * @param selectionBackground selection background, or NULL to use the system default color
 * @param flags drawing options
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the gc is null</li>
 * </ul>
 *
 * @since 3.3
 */
public void draw (GC gc, int x, int y, int selectionStart, int selectionEnd, Color selectionForeground, Color selectionBackground, int flags) {
	checkLayout();
	drawInPixels(gc, DPIUtil.autoScaleUp(getDevice(), x), DPIUtil.autoScaleUp(getDevice(), y), selectionStart, selectionEnd, selectionForeground, selectionBackground, flags);
}

void drawInPixels (GC gc, int x, int y, int selectionStart, int selectionEnd, Color selectionForeground, Color selectionBackground, int flags) {
	computeRuns(gc);
	if (gc == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (gc.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	if (selectionForeground != null && selectionForeground.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	if (selectionBackground != null && selectionBackground.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	int length = text.length();
	if (length == 0 && flags == 0) return;
	y += getScaledVerticalIndent();
	long hdc = gc.handle;
	Rectangle clip = gc.getClippingInPixels();
	GCData data = gc.data;
	long gdipGraphics = data.gdipGraphics;
	int foreground = data.foreground;
	int linkColor = OS.GetSysColor (OS.COLOR_HOTLIGHT);
	int alpha = data.alpha;
	boolean gdip = gdipGraphics != 0;
	long gdipForeground = 0;
	long gdipLinkColor = 0;
	int state = 0;
	if (gdip) {
		gc.checkGC(GC.FOREGROUND);
		gdipForeground = gc.getFgBrush();
	} else {
		state = OS.SaveDC(hdc);
		if ((data.style & SWT.MIRRORED) != 0) {
			OS.SetLayout(hdc, OS.GetLayout(hdc) | OS.LAYOUT_RTL);
		}
	}
	boolean hasSelection = selectionStart <= selectionEnd && selectionStart != -1 && selectionEnd != -1;
	long gdipSelBackground = 0, gdipSelForeground = 0, gdipFont = 0, lastHFont = 0;
	long selBackground = 0;
	int selForeground = 0;
	if (hasSelection || ((flags & SWT.LAST_LINE_SELECTION) != 0 && (flags & (SWT.FULL_SELECTION | SWT.DELIMITER_SELECTION)) != 0)) {
		int fgSel = selectionForeground != null ? selectionForeground.handle : OS.GetSysColor (OS.COLOR_HIGHLIGHTTEXT);
		int bgSel = selectionBackground != null ? selectionBackground.handle : OS.GetSysColor (OS.COLOR_HIGHLIGHT);
		if (gdip) {
			gdipSelBackground = createGdipBrush(bgSel, alpha);
			gdipSelForeground = createGdipBrush(fgSel, alpha);
		} else {
			selBackground = OS.CreateSolidBrush(bgSel);
			selForeground = fgSel;
		}
		if (hasSelection) {
			selectionStart = translateOffset(Math.min(Math.max(0, selectionStart), length - 1));
			selectionEnd = translateOffset(Math.min(Math.max(0, selectionEnd), length - 1));
		}
	}
	RECT rect = new RECT();
	OS.SetBkMode(hdc, OS.TRANSPARENT);
	for (int line=0; line<runs.length; line++) {
		int drawX = x + getLineIndent(line);
		int drawY = y + DPIUtil.autoScaleUp(getDevice(), lineY[line]);
		StyleItem[] lineRuns = runs[line];
		int lineHeight = DPIUtil.autoScaleUp(getDevice(), lineY[line+1] - lineY[line] - lineSpacingInPoints);

		//Draw last line selection
		if ((flags & (SWT.FULL_SELECTION | SWT.DELIMITER_SELECTION)) != 0 && (hasSelection || (flags & SWT.LAST_LINE_SELECTION) != 0)) {
			boolean extents = false;
			if (line == runs.length - 1 && (flags & SWT.LAST_LINE_SELECTION) != 0) {
				extents = true;
			} else {
				StyleItem run = lineRuns[lineRuns.length - 1];
				if (run.lineBreak && !run.softBreak) {
					if (selectionStart <= run.start && run.start <= selectionEnd) extents = true;
				} else {
					int endOffset = run.start + run.length - 1;
					if (selectionStart <= endOffset && endOffset < selectionEnd && (flags & SWT.FULL_SELECTION) != 0) {
						extents = true;
					}
				}
			}
			if (extents) {
				int width;
				if ((flags & SWT.FULL_SELECTION) != 0) {
					width = 0x6FFFFFF;
				} else {
					width = lineHeight / 3;
				}
				if (gdip) {
					Gdip.Graphics_FillRectangle(gdipGraphics, gdipSelBackground, drawX + lineWidth[line], drawY, width, lineHeight);
				} else {
					OS.SelectObject(hdc, selBackground);
					OS.PatBlt(hdc, drawX + lineWidth[line], drawY, width, lineHeight, OS.PATCOPY);
				}
			}
		}
		if (drawX > clip.x + clip.width) continue;
		if (drawX + lineWidth[line] < clip.x) continue;

		//Draw the background of the runs in the line
		int alignmentX = drawX;
		for (StyleItem run : lineRuns) {
			if (run.length == 0) continue;
			if (drawX > clip.x + clip.width) break;
			if (drawX + run.width >= clip.x) {
				if (!run.lineBreak || run.softBreak) {
					OS.SetRect(rect, drawX, drawY, drawX + run.width, drawY + lineHeight);
					if (gdip) {
						drawRunBackgroundGDIP(run, gdipGraphics, rect, selectionStart, selectionEnd, alpha, gdipSelBackground, hasSelection);
					} else {
						drawRunBackground(run, hdc, rect, selectionStart, selectionEnd, selBackground, hasSelection);
					}
				}
			}
			drawX += run.width;
		}

		//Draw the text, underline, strikeout, and border of the runs in the line
		int baselineInPixels = Math.max(0, this.ascentInPixels);
		int lineUnderlinePos = 0;
		for (StyleItem run : lineRuns) {
			baselineInPixels = Math.max(baselineInPixels, DPIUtil.autoScaleUp(getDevice(), run.ascentInPoints));
			lineUnderlinePos = Math.min(lineUnderlinePos, run.underlinePos);
		}
		RECT borderClip = null, underlineClip = null, strikeoutClip = null, pRect = null;
		drawX = alignmentX;
		for (int i = 0; i < lineRuns.length; i++) {
			StyleItem run = lineRuns[i];
			TextStyle style = run.style;
			boolean hasAdorners = style != null && (style.underline || style.strikeout || style.borderStyle != SWT.NONE);
			if (run.length == 0) continue;
			if (drawX > clip.x + clip.width) break;
			if (drawX + run.width >= clip.x) {
				boolean skipTab = run.tab && !hasAdorners;
				if (!skipTab && (!run.lineBreak || run.softBreak) && !(style != null && style.metrics != null)) {
					OS.SetRect(rect, drawX, drawY, drawX + run.width, drawY + lineHeight);
					if (gdip) {
						long hFont = getItemFont(run);
						if (hFont != lastHFont) {
							lastHFont = hFont;
							if (gdipFont != 0) Gdip.Font_delete(gdipFont);
							long oldFont = OS.SelectObject(hdc, hFont);
							gdipFont = Gdip.Font_new(hdc, hFont);
							OS.SelectObject(hdc, oldFont);
							if (gdipFont == 0) SWT.error(SWT.ERROR_NO_HANDLES);
							if (!Gdip.Font_IsAvailable(gdipFont)) {
								Gdip.Font_delete(gdipFont);
								gdipFont = 0;
							}
						}
						long gdipFg = gdipForeground;
						if (style != null && style.underline && style.underlineStyle == SWT.UNDERLINE_LINK) {
							if (gdipLinkColor == 0) gdipLinkColor = createGdipBrush(linkColor, alpha);
							gdipFg = gdipLinkColor;
						}
						if (gdipFont != 0 && !run.analysis.fNoGlyphIndex) {
							pRect = drawRunTextGDIP(gdipGraphics, run, rect, gdipFont, baselineInPixels, gdipFg, gdipSelForeground, selectionStart, selectionEnd, alpha);
						} else {
							int fg = style != null && style.underline && style.underlineStyle == SWT.UNDERLINE_LINK ? linkColor : foreground;
							pRect = drawRunTextGDIPRaster(gdipGraphics, run, rect, baselineInPixels, fg, selForeground, selectionStart, selectionEnd);
						}
						underlineClip = drawUnderlineGDIP(gdipGraphics, x, drawY + baselineInPixels, lineUnderlinePos, drawY + lineHeight, lineRuns, i, gdipFg, gdipSelForeground, underlineClip, pRect, selectionStart, selectionEnd, alpha, clip);
						strikeoutClip = drawStrikeoutGDIP(gdipGraphics, x, drawY + baselineInPixels, lineRuns, i, gdipFg, gdipSelForeground, strikeoutClip, pRect, selectionStart, selectionEnd, alpha, clip);
						borderClip = drawBorderGDIP(gdipGraphics, x, drawY, lineHeight, lineRuns, i, gdipFg, gdipSelForeground, borderClip, pRect, selectionStart, selectionEnd, alpha, clip);
					}  else {
						int fg = style != null && style.underline && style.underlineStyle == SWT.UNDERLINE_LINK ? linkColor : foreground;
						pRect = drawRunText(hdc, run, rect, baselineInPixels, fg, selForeground, selectionStart, selectionEnd);
						underlineClip = drawUnderline(hdc, x, drawY + baselineInPixels, lineUnderlinePos, drawY + lineHeight, lineRuns, i, fg, selForeground, underlineClip, pRect, selectionStart, selectionEnd, clip);
						strikeoutClip = drawStrikeout(hdc, x, drawY + baselineInPixels, lineRuns, i, fg, selForeground, strikeoutClip, pRect, selectionStart, selectionEnd, clip);
						borderClip = drawBorder(hdc, x, drawY, lineHeight, lineRuns, i, fg, selForeground, borderClip, pRect,  selectionStart, selectionEnd, clip);
					}
				}
			}
			drawX += run.width;
		}
	}
	if (gdipSelBackground != 0) Gdip.SolidBrush_delete(gdipSelBackground);
	if (gdipSelForeground != 0) Gdip.SolidBrush_delete(gdipSelForeground);
	if (gdipLinkColor != 0) Gdip.SolidBrush_delete(gdipLinkColor);
	if (gdipFont != 0) Gdip.Font_delete(gdipFont);
	if (state != 0)	OS.RestoreDC(hdc, state);
	if (selBackground != 0) OS.DeleteObject (selBackground);
}

RECT drawBorder(long hdc, int x, int y, int lineHeight, StyleItem[] line, int index, int color, int selectionColor, RECT clipRect, RECT pRect, int selectionStart, int selectionEnd, Rectangle drawClip) {
	StyleItem run = line[index];
	TextStyle style = run.style;
	if (style == null) return null;
	if (style.borderStyle == SWT.NONE) return null;
	clipRect = addClipRect(run, clipRect, pRect, selectionStart, selectionEnd);
	boolean lastRunVisible = drawClip != null && (x + run.x + run.width) > (drawClip.x + drawClip.width);
	if (index + 1 >= line.length || lastRunVisible || line[index + 1].lineBreak || !style.isAdherentBorder(line[index + 1].style)) {
		int left = run.x;
		int start = run.start;
		int end = run.start + run.length - 1;
		for (int i = index; i > 0 && style.isAdherentBorder(line[i - 1].style); i--) {
			left = line[i - 1].x;
			start = Math.min(start, line[i - 1].start);
			end = Math.max(end, line[i - 1].start + line[i - 1].length - 1);
		}
		boolean hasSelection = selectionStart <= selectionEnd && selectionStart != -1 && selectionEnd != -1;
		boolean fullSelection = hasSelection && selectionStart <= start && end <= selectionEnd;
		if (style.borderColor != null) {
			color = style.borderColor.handle;
			clipRect = null;
		} else {
			if (fullSelection) {
				color = selectionColor;
				clipRect = null;
			} else {
				if (style.foreground != null) {
					color = style.foreground.handle;
				}
			}
		}
		int lineWidth = 1;
		int pattern = 1;
		int lineStyle = OS.PS_SOLID;
		switch (style.borderStyle) {
			case SWT.BORDER_SOLID: break;
			case SWT.BORDER_DASH: {
				lineStyle = OS.PS_DASH;
				pattern = 4;
				break;
			}
			case SWT.BORDER_DOT: {
				lineStyle = OS.PS_DOT;
				pattern = 2;
				break;
			}
		}
		long oldBrush = OS.SelectObject(hdc, OS.GetStockObject(OS.NULL_BRUSH));
		LOGBRUSH logBrush = new LOGBRUSH();
		logBrush.lbStyle = OS.BS_SOLID;
		logBrush.lbColor = color;
		long newPen = OS.ExtCreatePen(lineStyle | OS.PS_GEOMETRIC, lineWidth, logBrush, 0, null);
		long oldPen = OS.SelectObject(hdc, newPen);
		RECT drawRect = new RECT();
		OS.SetRect(drawRect, x + left, y, x + run.x + run.width, y + lineHeight);
		if (drawClip != null) {
			if (drawRect.left < drawClip.x) {
				int remainder = drawRect.left % pattern;
				drawRect.left = drawClip.x / pattern * pattern + remainder - pattern;
			}
			if (drawRect.right > drawClip.x + drawClip.width) {
				int remainder = drawRect.right % pattern;
				drawRect.right = (drawClip.x + drawClip.width) / pattern * pattern + remainder + pattern;
			}
		}
		OS.Rectangle(hdc, drawRect.left,drawRect.top, drawRect.right, drawRect.bottom);
		OS.SelectObject(hdc, oldPen);
		OS.DeleteObject(newPen);
		if (clipRect != null) {
			int state = OS.SaveDC(hdc);
			if (clipRect.left == -1) clipRect.left = 0;
			if (clipRect.right == -1) clipRect.right = 0x7ffff;
			OS.IntersectClipRect(hdc, clipRect.left, clipRect.top, clipRect.right, clipRect.bottom);
			logBrush.lbColor = selectionColor;
			long selPen = OS.ExtCreatePen (lineStyle | OS.PS_GEOMETRIC, lineWidth, logBrush, 0, null);
			oldPen = OS.SelectObject(hdc, selPen);
			OS.Rectangle(hdc, drawRect.left, drawRect.top, drawRect.right, drawRect.bottom);
			OS.RestoreDC(hdc, state);
			OS.SelectObject(hdc, oldPen);
			OS.DeleteObject(selPen);
		}
		OS.SelectObject(hdc, oldBrush);
		return null;
	}
	return clipRect;
}

RECT drawBorderGDIP(long graphics, int x, int y, int lineHeight, StyleItem[] line, int index, long color, long selectionColor, RECT clipRect, RECT pRect,  int selectionStart, int selectionEnd, int alpha, Rectangle drawClip) {
	StyleItem run = line[index];
	TextStyle style = run.style;
	if (style == null) return null;
	if (style.borderStyle == SWT.NONE) return null;
	clipRect = addClipRect(run, clipRect, pRect, selectionStart, selectionEnd);
	boolean lastRunVisible = drawClip != null && (x + run.x + run.width) > (drawClip.x + drawClip.width);
	if (index + 1 >= line.length || lastRunVisible || line[index + 1].lineBreak || !style.isAdherentBorder(line[index + 1].style)) {
		int left = run.x;
		int start = run.start;
		int end = run.start + run.length - 1;
		for (int i = index; i > 0 && style.isAdherentBorder(line[i - 1].style); i--) {
			left = line[i - 1].x;
			start = Math.min(start, line[i - 1].start);
			end = Math.max(end, line[i - 1].start + line[i - 1].length - 1);
		}
		boolean hasSelection = selectionStart <= selectionEnd && selectionStart != -1 && selectionEnd != -1;
		boolean fullSelection = hasSelection && selectionStart <= start && end <= selectionEnd;
		long brush = color;
		if (style.borderColor != null) {
			brush = createGdipBrush(style.borderColor, alpha);
			clipRect = null;
		} else {
			if (fullSelection) {
				brush = selectionColor;
				clipRect = null;
			} else {
				if (style.foreground != null) {
					brush = createGdipBrush(style.foreground, alpha);
				}
			}
		}
		int lineWidth = 1;
		int lineStyle = Gdip.DashStyleSolid;
		switch (style.borderStyle) {
			case SWT.BORDER_SOLID: break;
			case SWT.BORDER_DASH: lineStyle = Gdip.DashStyleDash; break;
			case SWT.BORDER_DOT: lineStyle = Gdip.DashStyleDot; break;
		}
		long pen = Gdip.Pen_new(brush, lineWidth);
		Gdip.Pen_SetDashStyle(pen, lineStyle);
		Gdip.Graphics_SetPixelOffsetMode(graphics, Gdip.PixelOffsetModeNone);
		int smoothingMode = Gdip.Graphics_GetSmoothingMode(graphics);
		Gdip.Graphics_SetSmoothingMode(graphics, Gdip.SmoothingModeNone);
		if (clipRect != null) {
			int gstate = Gdip.Graphics_Save(graphics);
			if (clipRect.left == -1) clipRect.left = 0;
			if (clipRect.right == -1) clipRect.right = 0x7ffff;
			Rect gdipRect = new Rect();
			gdipRect.X = clipRect.left;
			gdipRect.Y = clipRect.top;
			gdipRect.Width = clipRect.right - clipRect.left;
			gdipRect.Height = clipRect.bottom - clipRect.top;
			Gdip.Graphics_SetClip(graphics, gdipRect, Gdip.CombineModeExclude);
			Gdip.Graphics_DrawRectangle(graphics, pen, x + left, y, run.x + run.width - left - 1, lineHeight - 1);
			Gdip.Graphics_Restore(graphics, gstate);
			gstate = Gdip.Graphics_Save(graphics);
			Gdip.Graphics_SetClip(graphics, gdipRect, Gdip.CombineModeIntersect);
			long selPen = Gdip.Pen_new(selectionColor, lineWidth);
			Gdip.Pen_SetDashStyle(selPen, lineStyle);
			Gdip.Graphics_DrawRectangle(graphics, selPen, x + left, y, run.x + run.width - left - 1, lineHeight - 1);
			Gdip.Pen_delete(selPen);
			Gdip.Graphics_Restore(graphics, gstate);
		} else {
			Gdip.Graphics_DrawRectangle(graphics, pen, x + left, y, run.x + run.width - left - 1, lineHeight - 1);
		}
		Gdip.Graphics_SetPixelOffsetMode(graphics, Gdip.PixelOffsetModeHalf);
		Gdip.Graphics_SetSmoothingMode(graphics, smoothingMode);
		Gdip.Pen_delete(pen);
		if (brush != selectionColor && brush != color) Gdip.SolidBrush_delete(brush);
		return null;
	}
	return clipRect;
}

void drawRunBackground(StyleItem run, long hdc, RECT rect, int selectionStart, int selectionEnd, long selBrush, boolean hasSelection) {
	int end = run.start + run.length - 1;
	boolean fullSelection = hasSelection && selectionStart <= run.start && selectionEnd >= end;
	if (fullSelection) {
		OS.SelectObject(hdc, selBrush);
		OS.PatBlt(hdc, rect.left, rect.top, rect.right - rect.left, rect.bottom - rect.top, OS.PATCOPY);
	} else {
		if (run.style != null && run.style.background != null) {
			int bg = run.style.background.handle;
			long hBrush = OS.CreateSolidBrush (bg);
			long oldBrush = OS.SelectObject(hdc, hBrush);
			OS.PatBlt(hdc, rect.left, rect.top, rect.right - rect.left, rect.bottom - rect.top, OS.PATCOPY);
			OS.SelectObject(hdc, oldBrush);
			OS.DeleteObject(hBrush);
		}
		boolean partialSelection = hasSelection && !(selectionStart > end || run.start > selectionEnd);
		if (partialSelection) {
			getPartialSelection(run, selectionStart, selectionEnd, rect);
			OS.SelectObject(hdc, selBrush);
			OS.PatBlt(hdc, rect.left, rect.top, rect.right - rect.left, rect.bottom - rect.top, OS.PATCOPY);
		}
	}
}

void drawRunBackgroundGDIP(StyleItem run, long graphics, RECT rect, int selectionStart, int selectionEnd, int alpha, long selBrush, boolean hasSelection) {
	int end = run.start + run.length - 1;
	boolean fullSelection = hasSelection && selectionStart <= run.start && selectionEnd >= end;
	if (fullSelection) {
		Gdip.Graphics_FillRectangle(graphics, selBrush, rect.left, rect.top, rect.right - rect.left, rect.bottom - rect.top);
	} else {
		if (run.style != null && run.style.background != null) {
			long brush = createGdipBrush(run.style.background, alpha);
			Gdip.Graphics_FillRectangle(graphics, brush, rect.left, rect.top, rect.right - rect.left, rect.bottom - rect.top);
			Gdip.SolidBrush_delete(brush);
		}
		boolean partialSelection = hasSelection && !(selectionStart > end || run.start > selectionEnd);
		if (partialSelection) {
			getPartialSelection(run, selectionStart, selectionEnd, rect);
			if (rect.left > rect.right) {
				int tmp = rect.left;
				rect.left = rect.right;
				rect.right = tmp;
			}
			Gdip.Graphics_FillRectangle(graphics, selBrush, rect.left, rect.top, rect.right - rect.left, rect.bottom - rect.top);
		}
	}
}

RECT drawRunText(long hdc, StyleItem run, RECT rect, int baselineInPixels, int color, int selectionColor, int selectionStart, int selectionEnd) {
	int end = run.start + run.length - 1;
	boolean hasSelection = selectionStart <= selectionEnd && selectionStart != -1 && selectionEnd != -1;
	boolean fullSelection = hasSelection && selectionStart <= run.start && selectionEnd >= end;
	boolean partialSelection = hasSelection && !fullSelection && !(selectionStart > end || run.start > selectionEnd);
	int offset = (orientation & SWT.RIGHT_TO_LEFT) != 0 ? -1 : 0;
	int x = rect.left + offset;
	int y = rect.top + (baselineInPixels - DPIUtil.autoScaleUp(getDevice(), run.ascentInPoints));
	long hFont = getItemFont(run);
	OS.SelectObject(hdc, hFont);
	if (fullSelection) {
		color = selectionColor;
	} else {
		if (run.style != null && run.style.foreground != null) {
			color = run.style.foreground.handle;
		}
	}
	OS.SetTextColor(hdc, color);
	OS.ScriptTextOut(hdc, run.psc, x, y, 0, null, run.analysis , 0, 0, run.glyphs, run.glyphCount, run.advances, run.justify, run.goffsets);
	if (partialSelection) {
		getPartialSelection(run, selectionStart, selectionEnd, rect);
		OS.SetTextColor(hdc, selectionColor);
		OS.ScriptTextOut(hdc, run.psc, x, y, OS.ETO_CLIPPED, rect, run.analysis , 0, 0, run.glyphs, run.glyphCount, run.advances, run.justify, run.goffsets);
	}
	return fullSelection || partialSelection ? rect : null;
}

RECT drawRunTextGDIP(long graphics, StyleItem run, RECT rect, long gdipFont, int baselineInPixels, long color, long selectionColor, int selectionStart, int selectionEnd, int alpha) {
	int end = run.start + run.length - 1;
	boolean hasSelection = selectionStart <= selectionEnd && selectionStart != -1 && selectionEnd != -1;
	boolean fullSelection = hasSelection && selectionStart <= run.start && selectionEnd >= end;
	boolean partialSelection = hasSelection && !fullSelection && !(selectionStart > end || run.start > selectionEnd);
	int drawY = rect.top + baselineInPixels;
	if (run.style != null && run.style.rise != 0) drawY -= DPIUtil.autoScaleUp(getDevice(), run.style.rise);
	int drawX = rect.left;
	long brush = color;
	if (fullSelection) {
		brush = selectionColor;
	} else {
		if (run.style != null && run.style.foreground != null) {
			brush = createGdipBrush(run.style.foreground, alpha);
		}
	}
	int gstate = 0;
	Rect gdipRect = null;
	if (partialSelection) {
		gdipRect = new Rect();
		getPartialSelection(run, selectionStart, selectionEnd, rect);
		gdipRect.X = rect.left;
		gdipRect.Y = rect.top;
		gdipRect.Width = rect.right - rect.left;
		gdipRect.Height = rect.bottom - rect.top;
		gstate = Gdip.Graphics_Save(graphics);
		Gdip.Graphics_SetClip(graphics, gdipRect, Gdip.CombineModeExclude);
	}
	int gstateMirrored = 0;
	boolean isMirrored = (orientation & SWT.RIGHT_TO_LEFT) != 0;
	if (isMirrored) {
		switch (Gdip.Brush_GetType(brush)) {
			case Gdip.BrushTypeLinearGradient:
				Gdip.LinearGradientBrush_ScaleTransform(brush, -1, 1, Gdip.MatrixOrderPrepend);
				Gdip.LinearGradientBrush_TranslateTransform(brush, -2 * drawX - run.width, 0, Gdip.MatrixOrderPrepend);
				break;
			case Gdip.BrushTypeTextureFill:
				Gdip.TextureBrush_ScaleTransform(brush, -1, 1, Gdip.MatrixOrderPrepend);
				Gdip.TextureBrush_TranslateTransform(brush, -2 * drawX - run.width, 0, Gdip.MatrixOrderPrepend);
				break;
		}
		gstateMirrored = Gdip.Graphics_Save(graphics);
		Gdip.Graphics_ScaleTransform(graphics, -1, 1, Gdip.MatrixOrderPrepend);
		Gdip.Graphics_TranslateTransform(graphics, -2 * drawX - run.width, 0, Gdip.MatrixOrderPrepend);
	}
	int[] advances = new int[run.glyphCount];
	float[] points = new float[run.glyphCount * 2];
	C.memmove(advances, run.justify != 0 ? run.justify : run.advances, run.glyphCount * 4);
	int glyphX = drawX;
	for (int h = 0, j = 0; h < advances.length; h++) {
		points[j++] = glyphX;
		points[j++] = drawY;
		glyphX += advances[h];
	}
	Gdip.Graphics_DrawDriverString(graphics, run.glyphs, run.glyphCount, gdipFont, brush, points, 0, 0);
	if (partialSelection) {
		if (isMirrored) {
			Gdip.Graphics_Restore(graphics, gstateMirrored);
		}
		Gdip.Graphics_Restore(graphics, gstate);
		gstate = Gdip.Graphics_Save(graphics);
		Gdip.Graphics_SetClip(graphics, gdipRect, Gdip.CombineModeIntersect);
		if (isMirrored) {
			gstateMirrored = Gdip.Graphics_Save(graphics);
			Gdip.Graphics_ScaleTransform(graphics, -1, 1, Gdip.MatrixOrderPrepend);
			Gdip.Graphics_TranslateTransform(graphics, -2 * drawX - run.width, 0, Gdip.MatrixOrderPrepend);
		}
		Gdip.Graphics_DrawDriverString(graphics, run.glyphs, run.glyphCount, gdipFont, selectionColor, points, 0, 0);
		Gdip.Graphics_Restore(graphics, gstate);
	}
	if (isMirrored) {
		switch (Gdip.Brush_GetType(brush)) {
			case Gdip.BrushTypeLinearGradient:
				Gdip.LinearGradientBrush_ResetTransform(brush);
				break;
			case Gdip.BrushTypeTextureFill:
				Gdip.TextureBrush_ResetTransform(brush);
				break;
		}
		Gdip.Graphics_Restore(graphics, gstateMirrored);
	}
	if (brush != selectionColor && brush != color) Gdip.SolidBrush_delete(brush);
	return fullSelection || partialSelection ? rect : null;
}

RECT drawRunTextGDIPRaster(long graphics, StyleItem run, RECT rect, int baselineInPixels, int color, int selectionColor, int selectionStart, int selectionEnd) {
	long clipRgn = 0;
	Gdip.Graphics_SetPixelOffsetMode(graphics, Gdip.PixelOffsetModeNone);
	long rgn = Gdip.Region_new();
	if (rgn == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	Gdip.Graphics_GetClip(graphics, rgn);
	if (!Gdip.Region_IsInfinite(rgn, graphics)) {
		clipRgn = Gdip.Region_GetHRGN(rgn, graphics);
	}
	Gdip.Region_delete(rgn);
	Gdip.Graphics_SetPixelOffsetMode(graphics, Gdip.PixelOffsetModeHalf);
	float[] lpXform = null;
	long matrix = Gdip.Matrix_new(1, 0, 0, 1, 0, 0);
	if (matrix == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	Gdip.Graphics_GetTransform(graphics, matrix);
	if (!Gdip.Matrix_IsIdentity(matrix)) {
		lpXform = new float[6];
		Gdip.Matrix_GetElements(matrix, lpXform);
	}
	Gdip.Matrix_delete(matrix);
	long hdc = Gdip.Graphics_GetHDC(graphics);
	int state = OS.SaveDC(hdc);
	if (lpXform != null) {
		OS.SetGraphicsMode(hdc, OS.GM_ADVANCED);
		OS.SetWorldTransform(hdc, lpXform);
	}
	if (clipRgn != 0) {
		OS.SelectClipRgn(hdc, clipRgn);
		OS.DeleteObject(clipRgn);
	}
	if ((orientation & SWT.RIGHT_TO_LEFT) != 0) {
		OS.SetLayout(hdc, OS.GetLayout(hdc) | OS.LAYOUT_RTL);
	}
	OS.SetBkMode(hdc, OS.TRANSPARENT);
	RECT pRect = drawRunText(hdc, run, rect, baselineInPixels, color, selectionColor, selectionStart, selectionEnd);
	OS.RestoreDC(hdc, state);
	Gdip.Graphics_ReleaseHDC(graphics, hdc);
	return pRect;
}

RECT drawStrikeout(long hdc, int x, int baselineInPixels, StyleItem[] line, int index, int color, int selectionColor, RECT clipRect, RECT pRect, int selectionStart, int selectionEnd, Rectangle drawClip) {
	StyleItem run = line[index];
	TextStyle style = run.style;
	if (style == null) return null;
	if (!style.strikeout) return null;
	clipRect = addClipRect(run, clipRect, pRect, selectionStart, selectionEnd);
	boolean lastRunVisible = drawClip != null && (x + run.x + run.width) > (drawClip.x + drawClip.width);
	if (index + 1 >= line.length || lastRunVisible || line[index + 1].lineBreak || !style.isAdherentStrikeout(line[index + 1].style)) {
		int left = run.x;
		int start = run.start;
		int end = run.start + run.length - 1;
		for (int i = index; i > 0 && style.isAdherentStrikeout(line[i - 1].style); i--) {
			left = line[i - 1].x;
			start = Math.min(start, line[i - 1].start);
			end = Math.max(end, line[i - 1].start + line[i - 1].length - 1);
		}
		boolean hasSelection = selectionStart <= selectionEnd && selectionStart != -1 && selectionEnd != -1;
		boolean fullSelection = hasSelection && selectionStart <= start && end <= selectionEnd;
		if (style.strikeoutColor != null) {
			color = style.strikeoutColor.handle;
			clipRect = null;
		} else {
			if (fullSelection) {
				color = selectionColor;
				clipRect = null;
			} else {
				if (style.foreground != null) {
					color = style.foreground.handle;
				}
			}
		}
		RECT rect = new RECT();
		int riseInPixels = DPIUtil.autoScaleUp(getDevice(), style.rise);
		OS.SetRect(rect, x + left, baselineInPixels - run.strikeoutPos - riseInPixels, x + run.x + run.width, baselineInPixels - run.strikeoutPos + run.strikeoutThickness - riseInPixels);
		long brush = OS.CreateSolidBrush(color);
		OS.FillRect(hdc, rect, brush);
		OS.DeleteObject(brush);
		if (clipRect != null) {
			long selBrush = OS.CreateSolidBrush(selectionColor);
			if (clipRect.left == -1) clipRect.left = 0;
			if (clipRect.right == -1) clipRect.right = 0x7ffff;
			OS.SetRect(clipRect, Math.max(rect.left, clipRect.left), rect.top, Math.min(rect.right, clipRect.right), rect.bottom);
			OS.FillRect(hdc, clipRect, selBrush);
			OS.DeleteObject(selBrush);
		}
		return null;
	}
	return clipRect;
}

RECT drawStrikeoutGDIP(long graphics, int x, int baselineInPixels, StyleItem[] line, int index, long color, long selectionColor, RECT clipRect, RECT pRect, int selectionStart, int selectionEnd, int alpha, Rectangle drawClip) {
	StyleItem run = line[index];
	TextStyle style = run.style;
	if (style == null) return null;
	if (!style.strikeout) return null;
	clipRect = addClipRect(run, clipRect, pRect, selectionStart, selectionEnd);
	boolean lastRunVisible = drawClip != null && (x + run.x + run.width) > (drawClip.x + drawClip.width);
	if (index + 1 >= line.length || lastRunVisible || line[index + 1].lineBreak || !style.isAdherentStrikeout(line[index + 1].style)) {
		int left = run.x;
		int start = run.start;
		int end = run.start + run.length - 1;
		for (int i = index; i > 0 && style.isAdherentStrikeout(line[i - 1].style); i--) {
			left = line[i - 1].x;
			start = Math.min(start, line[i - 1].start);
			end = Math.max(end, line[i - 1].start + line[i - 1].length - 1);
		}
		boolean hasSelection = selectionStart <= selectionEnd && selectionStart != -1 && selectionEnd != -1;
		boolean fullSelection = hasSelection && selectionStart <= start && end <= selectionEnd;
		long brush = color;
		if (style.strikeoutColor != null) {
			brush = createGdipBrush(style.strikeoutColor, alpha);
			clipRect = null;
		} else {
			if (fullSelection) {
				brush = selectionColor;
				clipRect = null;
			} else {
				if (style.foreground != null) {
					brush = createGdipBrush(style.foreground, alpha);
				}
			}
		}
		int riseInPixels = DPIUtil.autoScaleUp(getDevice(), style.rise);
		if (clipRect != null) {
			int gstate = Gdip.Graphics_Save(graphics);
			if (clipRect.left == -1) clipRect.left = 0;
			if (clipRect.right == -1) clipRect.right = 0x7ffff;
			Rect gdipRect = new Rect();
			gdipRect.X = clipRect.left;
			gdipRect.Y = clipRect.top;
			gdipRect.Width = clipRect.right - clipRect.left;
			gdipRect.Height = clipRect.bottom - clipRect.top;
			Gdip.Graphics_SetClip(graphics, gdipRect, Gdip.CombineModeExclude);
			Gdip.Graphics_FillRectangle(graphics, brush, x + left, baselineInPixels - run.strikeoutPos - riseInPixels, run.x + run.width - left, run.strikeoutThickness);
			Gdip.Graphics_Restore(graphics, gstate);
			gstate = Gdip.Graphics_Save(graphics);
			Gdip.Graphics_SetClip(graphics, gdipRect, Gdip.CombineModeIntersect);
			Gdip.Graphics_FillRectangle(graphics, selectionColor, x + left, baselineInPixels - run.strikeoutPos - riseInPixels, run.x + run.width - left, run.strikeoutThickness);
			Gdip.Graphics_Restore(graphics, gstate);
		} else {
			Gdip.Graphics_FillRectangle(graphics, brush, x + left, baselineInPixels - run.strikeoutPos - riseInPixels, run.x + run.width - left, run.strikeoutThickness);
		}
		if (brush != selectionColor && brush != color) Gdip.SolidBrush_delete(brush);
		return null;
	}
	return clipRect;
}

RECT drawUnderline(long hdc, int x, int baselineInPixels, int lineUnderlinePos, int lineBottom, StyleItem[] line, int index, int color, int selectionColor, RECT clipRect, RECT pRect, int selectionStart, int selectionEnd, Rectangle drawClip) {
	StyleItem run = line[index];
	TextStyle style = run.style;
	if (style == null) return null;
	if (!style.underline) return null;
	clipRect = addClipRect(run, clipRect, pRect, selectionStart, selectionEnd);
	boolean lastRunVisible = drawClip != null && (x + run.x + run.width) > (drawClip.x + drawClip.width);
	if (index + 1 >= line.length || lastRunVisible || line[index + 1].lineBreak || !style.isAdherentUnderline(line[index + 1].style)) {
		int left = run.x;
		int start = run.start;
		int end = run.start + run.length - 1;
		for (int i = index; i > 0 && style.isAdherentUnderline(line[i - 1].style); i--) {
			left = line[i - 1].x;
			start = Math.min(start, line[i - 1].start);
			end = Math.max(end, line[i - 1].start + line[i - 1].length - 1);
		}
		boolean hasSelection = selectionStart <= selectionEnd && selectionStart != -1 && selectionEnd != -1;
		boolean fullSelection = hasSelection && selectionStart <= start && end <= selectionEnd;
		if (style.underlineColor != null) {
			color = style.underlineColor.handle;
			clipRect = null;
		} else {
			if (fullSelection) {
				color = selectionColor;
				clipRect = null;
			} else {
				if (style.foreground != null) {
					color = style.foreground.handle;
				}
			}
		}
		RECT rect = new RECT();
		int riseInPixels = DPIUtil.autoScaleUp(getDevice(), style.rise);
		OS.SetRect(rect, x + left, baselineInPixels - lineUnderlinePos - riseInPixels, x + run.x + run.width, baselineInPixels - lineUnderlinePos + run.underlineThickness - riseInPixels);
		if (clipRect != null) {
			if (clipRect.left == -1) clipRect.left = 0;
			if (clipRect.right == -1) clipRect.right = 0x7ffff;
			OS.SetRect(clipRect, Math.max(rect.left, clipRect.left), rect.top, Math.min(rect.right, clipRect.right), rect.bottom);
		}
		switch (style.underlineStyle) {
			case SWT.UNDERLINE_SQUIGGLE:
			case SWT.UNDERLINE_ERROR: {
				int squigglyThickness = 1;
				int squigglyHeight = 2 * squigglyThickness;
				int squigglyY = Math.min(rect.top - squigglyHeight / 2, lineBottom - squigglyHeight - 1);
				int[] points = computePolyline(rect.left, squigglyY, rect.right, squigglyY + squigglyHeight);
				long pen = OS.CreatePen(OS.PS_SOLID, squigglyThickness, color);
				long oldPen = OS.SelectObject(hdc, pen);
				int state = OS.SaveDC(hdc);
				OS.IntersectClipRect(hdc, rect.left, squigglyY, rect.right + 1, squigglyY + squigglyHeight + 1);
				OS.Polyline(hdc, points, points.length / 2);
				int length = points.length;
				if (length >= 2 && squigglyThickness <= 1) {
					OS.SetPixel (hdc, points[length - 2], points[length - 1], color);
				}
				OS.SelectObject(hdc, oldPen);
				OS.DeleteObject(pen);
				OS.RestoreDC(hdc, state);
				if (clipRect != null) {
					pen = OS.CreatePen(OS.PS_SOLID, squigglyThickness, selectionColor);
					oldPen = OS.SelectObject(hdc, pen);
					state = OS.SaveDC(hdc);
					OS.IntersectClipRect(hdc, clipRect.left, squigglyY, clipRect.right + 1, squigglyY + squigglyHeight + 1);
					OS.Polyline(hdc, points, points.length / 2);
					if (length >= 2 && squigglyThickness <= 1) {
						OS.SetPixel (hdc, points[length - 2], points[length - 1], selectionColor);
					}
					OS.SelectObject(hdc, oldPen);
					OS.DeleteObject(pen);
					OS.RestoreDC(hdc, state);
				}
				break;
			}
			case SWT.UNDERLINE_SINGLE:
			case SWT.UNDERLINE_DOUBLE:
			case SWT.UNDERLINE_LINK:
			case UNDERLINE_IME_THICK:
				if (style.underlineStyle == UNDERLINE_IME_THICK) {
					rect.top -= run.underlineThickness;
					if (clipRect != null) clipRect.top -= run.underlineThickness;
				}
				int bottom = style.underlineStyle == SWT.UNDERLINE_DOUBLE ? rect.bottom + run.underlineThickness * 2 : rect.bottom;
				if (bottom > lineBottom) {
					OS.OffsetRect(rect, 0, lineBottom - bottom);
					if (clipRect != null) OS.OffsetRect(clipRect, 0, lineBottom - bottom);
				}
				long brush = OS.CreateSolidBrush(color);
				OS.FillRect(hdc, rect, brush);
				if (style.underlineStyle == SWT.UNDERLINE_DOUBLE) {
					OS.SetRect(rect, rect.left, rect.top + run.underlineThickness * 2, rect.right, rect.bottom + run.underlineThickness * 2);
					OS.FillRect(hdc, rect, brush);
				}
				OS.DeleteObject(brush);
				if (clipRect != null) {
					long selBrush = OS.CreateSolidBrush(selectionColor);
					OS.FillRect(hdc, clipRect, selBrush);
					if (style.underlineStyle == SWT.UNDERLINE_DOUBLE) {
						OS.SetRect(clipRect, clipRect.left, rect.top, clipRect.right, rect.bottom);
						OS.FillRect(hdc, clipRect, selBrush);
					}
					OS.DeleteObject(selBrush);
				}
				break;
			case UNDERLINE_IME_DASH:
			case UNDERLINE_IME_DOT: {
				int penStyle = style.underlineStyle == UNDERLINE_IME_DASH ? OS.PS_DASH : OS.PS_DOT;
				long pen = OS.CreatePen(penStyle, 1, color);
				long oldPen = OS.SelectObject(hdc, pen);
				int descentInPixels = DPIUtil.autoScaleUp(getDevice(), run.descentInPoints);
				OS.SetRect(rect, rect.left, baselineInPixels + descentInPixels, rect.right, baselineInPixels + descentInPixels + run.underlineThickness);
				OS.MoveToEx(hdc, rect.left, rect.top, 0);
				OS.LineTo(hdc, rect.right, rect.top);
				OS.SelectObject(hdc, oldPen);
				OS.DeleteObject(pen);
				if (clipRect != null) {
					pen = OS.CreatePen(penStyle, 1, selectionColor);
					oldPen = OS.SelectObject(hdc, pen);
					OS.SetRect(clipRect, clipRect.left, rect.top, clipRect.right, rect.bottom);
					OS.MoveToEx(hdc, clipRect.left, clipRect.top, 0);
					OS.LineTo(hdc, clipRect.right, clipRect.top);
					OS.SelectObject(hdc, oldPen);
					OS.DeleteObject(pen);
				}
				break;
			}
		}
		return null;
	}
	return clipRect;
}

RECT drawUnderlineGDIP (long graphics, int x, int baselineInPixels, int lineUnderlinePos, int lineBottom, StyleItem[] line, int index, long color, long selectionColor, RECT clipRect, RECT pRect, int selectionStart, int selectionEnd, int alpha, Rectangle drawClip) {
	StyleItem run = line[index];
	TextStyle style = run.style;
	if (style == null) return null;
	if (!style.underline) return null;
	clipRect = addClipRect(run, clipRect, pRect, selectionStart, selectionEnd);
	boolean lastRunVisible = drawClip != null && (x + run.x + run.width) > (drawClip.x + drawClip.width);
	if (index + 1 >= line.length || lastRunVisible || line[index + 1].lineBreak || !style.isAdherentUnderline(line[index + 1].style)) {
		int left = run.x;
		int start = run.start;
		int end = run.start + run.length - 1;
		for (int i = index; i > 0 && style.isAdherentUnderline(line[i - 1].style); i--) {
			left = line[i - 1].x;
			start = Math.min(start, line[i - 1].start);
			end = Math.max(end, line[i - 1].start + line[i - 1].length - 1);
		}
		boolean hasSelection = selectionStart <= selectionEnd && selectionStart != -1 && selectionEnd != -1;
		boolean fullSelection = hasSelection && selectionStart <= start && end <= selectionEnd;
		long brush = color;
		if (style.underlineColor != null) {
			brush = createGdipBrush(style.underlineColor, alpha);
			clipRect = null;
		} else {
			if (fullSelection) {
				brush = selectionColor;
				clipRect = null;
			} else {
				if (style.foreground != null) {
					brush = createGdipBrush(style.foreground, alpha);
				}
			}
		}
		RECT rect = new RECT();
		int riseInPixels = DPIUtil.autoScaleUp(getDevice(), style.rise);
		OS.SetRect(rect, x + left, baselineInPixels - lineUnderlinePos - riseInPixels, x + run.x + run.width, baselineInPixels - lineUnderlinePos + run.underlineThickness - riseInPixels);
		Rect gdipRect = null;
		if (clipRect != null) {
			if (clipRect.left == -1) clipRect.left = 0;
			if (clipRect.right == -1) clipRect.right = 0x7ffff;
			OS.SetRect(clipRect, Math.max(rect.left, clipRect.left), rect.top, Math.min(rect.right, clipRect.right), rect.bottom);
			gdipRect = new Rect();
			gdipRect.X = clipRect.left;
			gdipRect.Y = clipRect.top;
			gdipRect.Width = clipRect.right - clipRect.left;
			gdipRect.Height = clipRect.bottom - clipRect.top;
		}
		int gstate = 0;
		Gdip.Graphics_SetPixelOffsetMode(graphics, Gdip.PixelOffsetModeNone);
		int smoothingMode = Gdip.Graphics_GetSmoothingMode(graphics);
		Gdip.Graphics_SetSmoothingMode(graphics, Gdip.SmoothingModeNone);
		switch (style.underlineStyle) {
			case SWT.UNDERLINE_SQUIGGLE:
			case SWT.UNDERLINE_ERROR: {
				int squigglyThickness = 1;
				int squigglyHeight = 2 * squigglyThickness;
				int squigglyY = Math.min(rect.top - squigglyHeight / 2, lineBottom - squigglyHeight - 1);
				int[] points = computePolyline(rect.left, squigglyY, rect.right, squigglyY + squigglyHeight);
				long pen = Gdip.Pen_new(brush, squigglyThickness);
				gstate = Gdip.Graphics_Save(graphics);
				if (gdipRect != null) {
					Gdip.Graphics_SetClip(graphics, gdipRect, Gdip.CombineModeExclude);
				} else {
					Rect r = new Rect();
					r.X = rect.left;
					r.Y = squigglyY;
					r.Width = rect.right - rect.left;
					r.Height = squigglyHeight + 1;
					Gdip.Graphics_SetClip(graphics, r, Gdip.CombineModeIntersect);
				}
				Gdip.Graphics_DrawLines(graphics, pen, points, points.length / 2);
				if (gdipRect != null) {
					long selPen = Gdip.Pen_new(selectionColor, squigglyThickness);
					Gdip.Graphics_Restore(graphics, gstate);
					gstate = Gdip.Graphics_Save(graphics);
					Gdip.Graphics_SetClip(graphics, gdipRect, Gdip.CombineModeIntersect);
					Gdip.Graphics_DrawLines(graphics, selPen, points, points.length / 2);
					Gdip.Pen_delete(selPen);
				}
				Gdip.Graphics_Restore(graphics, gstate);
				Gdip.Pen_delete(pen);
				if (gstate != 0) Gdip.Graphics_Restore(graphics, gstate);
				break;
			}
			case SWT.UNDERLINE_SINGLE:
			case SWT.UNDERLINE_DOUBLE:
			case SWT.UNDERLINE_LINK:
			case UNDERLINE_IME_THICK:
				if (style.underlineStyle == UNDERLINE_IME_THICK) {
					rect.top -= run.underlineThickness;
				}
				int bottom = style.underlineStyle == SWT.UNDERLINE_DOUBLE ? rect.bottom + run.underlineThickness * 2 : rect.bottom;
				if (bottom > lineBottom) {
					OS.OffsetRect(rect, 0, lineBottom - bottom);
				}
				if (gdipRect != null) {
					gdipRect.Y = rect.top;
					if (style.underlineStyle == UNDERLINE_IME_THICK) {
						gdipRect.Height = run.underlineThickness * 2;
					}
					if (style.underlineStyle == SWT.UNDERLINE_DOUBLE) {
						gdipRect.Height = run.underlineThickness * 3;
					}
					gstate = Gdip.Graphics_Save(graphics);
					Gdip.Graphics_SetClip(graphics, gdipRect, Gdip.CombineModeExclude);
				}
				Gdip.Graphics_FillRectangle(graphics, brush, rect.left, rect.top, rect.right - rect.left, rect.bottom - rect.top);
				if (style.underlineStyle == SWT.UNDERLINE_DOUBLE) {
					Gdip.Graphics_FillRectangle(graphics, brush, rect.left, rect.top + run.underlineThickness * 2, rect.right - rect.left, rect.bottom - rect.top);
				}
				if (gdipRect != null) {
					Gdip.Graphics_Restore(graphics, gstate);
					gstate = Gdip.Graphics_Save(graphics);
					Gdip.Graphics_SetClip(graphics, gdipRect, Gdip.CombineModeIntersect);
					Gdip.Graphics_FillRectangle(graphics, selectionColor, rect.left, rect.top, rect.right - rect.left, rect.bottom - rect.top);
					if (style.underlineStyle == SWT.UNDERLINE_DOUBLE) {
						Gdip.Graphics_FillRectangle(graphics, selectionColor, rect.left, rect.top + run.underlineThickness * 2, rect.right - rect.left, rect.bottom - rect.top);
					}
					Gdip.Graphics_Restore(graphics, gstate);
				}
				break;
			case UNDERLINE_IME_DOT:
			case UNDERLINE_IME_DASH: {
				long pen = Gdip.Pen_new(brush, 1);
				int dashStyle = style.underlineStyle == UNDERLINE_IME_DOT ? Gdip.DashStyleDot : Gdip.DashStyleDash;
				Gdip.Pen_SetDashStyle(pen, dashStyle);
				if (gdipRect != null) {
					gstate = Gdip.Graphics_Save(graphics);
					Gdip.Graphics_SetClip(graphics, gdipRect, Gdip.CombineModeExclude);
				}
				int descentInPixels = DPIUtil.autoScaleUp(getDevice(), run.descentInPoints);
				Gdip.Graphics_DrawLine(graphics, pen, rect.left, baselineInPixels + descentInPixels, run.width - run.length, baselineInPixels + descentInPixels);
				if (gdipRect != null) {
					Gdip.Graphics_Restore(graphics, gstate);
					gstate = Gdip.Graphics_Save(graphics);
					Gdip.Graphics_SetClip(graphics, gdipRect, Gdip.CombineModeIntersect);
					long selPen = Gdip.Pen_new(brush, 1);
					Gdip.Pen_SetDashStyle(selPen, dashStyle);
					Gdip.Graphics_DrawLine(graphics, selPen, rect.left, baselineInPixels + descentInPixels, run.width - run.length, baselineInPixels + descentInPixels);
					Gdip.Graphics_Restore(graphics, gstate);
					Gdip.Pen_delete(selPen);
				}
				Gdip.Pen_delete(pen);
				break;
			}
		}
		if (brush != selectionColor && brush != color) Gdip.SolidBrush_delete(brush);
		Gdip.Graphics_SetPixelOffsetMode(graphics, Gdip.PixelOffsetModeHalf);
		Gdip.Graphics_SetSmoothingMode(graphics, smoothingMode);
		return null;
	}
	return clipRect;
}

void freeRuns () {
	if (allRuns == null) return;
	for (StyleItem run : allRuns) {
		run.free();
	}
	allRuns = null;
	runs = null;
	segmentsText = null;
}

/**
 * Returns the receiver's horizontal text alignment, which will be one
 * of <code>SWT.LEFT</code>, <code>SWT.CENTER</code> or
 * <code>SWT.RIGHT</code>.
 *
 * @return the alignment used to positioned text horizontally
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public int getAlignment () {
	checkLayout();
	return alignment;
}

/**
 * Returns the ascent of the receiver.
 *
 * @return the ascent
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 *
 * @see #getDescent()
 * @see #setDescent(int)
 * @see #setAscent(int)
 * @see #getLineMetrics(int)
 */
public int getAscent () {
	checkLayout();
	return DPIUtil.autoScaleDown(getDevice(), ascentInPixels);
}

/**
 * Returns the bounds of the receiver. The width returned is either the
 * width of the longest line or the width set using {@link TextLayout#setWidth(int)}.
 * To obtain the text bounds of a line use {@link TextLayout#getLineBounds(int)}.
 *
 * @return the bounds of the receiver
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 *
 * @see #setWidth(int)
 * @see #getLineBounds(int)
 */
public Rectangle getBounds () {
	checkLayout();
	computeRuns(null);
	int width = 0;
	if (wrapWidth != -1) {
		width = wrapWidth;
	} else {
		for (int line=0; line<runs.length; line++) {
			width = Math.max(width, lineWidth[line] + getLineIndent(line));
		}
	}
	return new Rectangle (0, 0, DPIUtil.autoScaleDown(getDevice(), width), lineY[lineY.length - 1] + getScaledVerticalIndent());
}

/**
 * Returns the bounds for the specified range of characters. The
 * bounds is the smallest rectangle that encompasses all characters
 * in the range. The start and end offsets are inclusive and will be
 * clamped if out of range.
 *
 * @param start the start offset
 * @param end the end offset
 * @return the bounds of the character range
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public Rectangle getBounds (int start, int end) {
	checkLayout();
	return DPIUtil.autoScaleDown(getDevice(), getBoundsInPixels(start, end));
}

Rectangle getBoundsInPixels (int start, int end) {
	computeRuns(null);
	int length = text.length();
	if (length == 0) return new Rectangle(0, 0, 0, 0);
	if (start > end) return new Rectangle(0, 0, 0, 0);
	start = Math.min(Math.max(0, start), length - 1);
	end = Math.min(Math.max(0, end), length - 1);
	start = translateOffset(start);
	end = translateOffset(end);
	/* use the high surrogate for the start offset and the low surrogate for the end offset */
	length = segmentsText.length();
	char ch = segmentsText.charAt(start);
	if (0xDC00 <= ch && ch <= 0xDFFF) {
		if (start - 1 >= 0) {
			ch = segmentsText.charAt(start - 1);
			if (0xD800 <= ch && ch <= 0xDBFF) {
				start--;
			}
		}
	}
	ch = segmentsText.charAt(end);
	if (0xD800 <= ch && ch <= 0xDBFF) {
		if (end + 1 < length) {
			ch = segmentsText.charAt(end + 1);
			if (0xDC00 <= ch && ch <= 0xDFFF) {
				end++;
			}
		}
	}
	int left = 0x7fffffff, right = 0;
	int top = 0x7fffffff, bottom = 0;
	boolean isRTL = (orientation & SWT.RIGHT_TO_LEFT) != 0;
	for (int i = 0; i < allRuns.length - 1; i++) {
		StyleItem run = allRuns[i];
		int runEnd = run.start + run.length;
		if (runEnd <= start) continue;
		if (run.start > end) break;
		int runLead = run.x;
		int runTrail = run.x + run.width;
		if (run.start <= start && start < runEnd) {
			int cx = 0;
			if (run.style != null && run.style.metrics != null) {
				GlyphMetrics metrics = run.style.metrics;
				cx = metrics.getWidthInPixels() * (start - run.start);
			} else if (!run.tab) {
				int iX = ScriptCPtoX(start - run.start, false, run);
				cx = isRTL ? run.width - iX : iX;
			}
			if (run.analysis.fRTL ^ isRTL) {
				runTrail = run.x + cx;
			} else {
				runLead = run.x + cx;
			}
		}
		if (run.start <= end && end < runEnd) {
			int cx = run.width;
			if (run.style != null && run.style.metrics != null) {
				GlyphMetrics metrics = run.style.metrics;
				cx = metrics.getWidthInPixels() * (end - run.start + 1);
			} else if (!run.tab) {
				int iX = ScriptCPtoX(end - run.start, true, run);
				cx = isRTL ? run.width - iX : iX;
			}
			if (run.analysis.fRTL ^ isRTL) {
				runLead = run.x + cx;
			} else {
				runTrail = run.x + cx;
			}
		}
		int lineIndex = 0;
		while (lineIndex < runs.length && lineOffset[lineIndex + 1] <= run.start) {
			lineIndex++;
		}
		left = Math.min(left, runLead);
		right = Math.max(right, runTrail);
		top = Math.min(top, DPIUtil.autoScaleUp(getDevice(), lineY[lineIndex]));
		bottom = Math.max(bottom, DPIUtil.autoScaleUp(getDevice(), lineY[lineIndex + 1] - lineSpacingInPoints));
	}
	return new Rectangle(left, top, right - left, bottom - top + getScaledVerticalIndent());
}

/**
 * Returns the descent of the receiver.
 *
 * @return the descent
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 *
 * @see #getAscent()
 * @see #setAscent(int)
 * @see #setDescent(int)
 * @see #getLineMetrics(int)
 */
public int getDescent () {
	checkLayout();
	return DPIUtil.autoScaleDown(getDevice(), descentInPixels);
}

/**
 * Returns the default font currently being used by the receiver
 * to draw and measure text.
 *
 * @return the receiver's font
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public Font getFont () {
	checkLayout();
	return font;
}

/**
* Returns the receiver's indent.
*
* @return the receiver's indent
*
* @exception SWTException <ul>
*    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
* </ul>
*
* @since 3.2
*/
public int getIndent () {
	checkLayout();
	return DPIUtil.autoScaleDown(getDevice(), getIndentInPixels());
}

int getIndentInPixels () {
	return indent;
}

/**
* Returns the receiver's justification.
*
* @return the receiver's justification
*
* @exception SWTException <ul>
*    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
* </ul>
*
* @since 3.2
*/
public boolean getJustify () {
	checkLayout();
	return justify;
}

long getItemFont (StyleItem item) {
	if (item.fallbackFont != 0) return item.fallbackFont;
	if (item.style != null && item.style.font != null) {
		return item.style.font.handle;
	}
	if (this.font != null) {
		return this.font.handle;
	}
	return device.systemFont.handle;
}

/**
 * Returns the embedding level for the specified character offset. The
 * embedding level is usually used to determine the directionality of a
 * character in bidirectional text.
 *
 * @param offset the character offset
 * @return the embedding level
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the character offset is out of range</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li></ul>
 */
public int getLevel (int offset) {
	checkLayout();
	computeRuns(null);
	int length = text.length();
	if (!(0 <= offset && offset <= length)) SWT.error(SWT.ERROR_INVALID_RANGE);
	offset = translateOffset(offset);
	for (int i=1; i<allRuns.length; i++) {
		if (allRuns[i].start > offset) {
			return allRuns[i - 1].analysis.s.uBidiLevel;
		}
	}
	return (resolveTextDirection() & SWT.RIGHT_TO_LEFT) != 0 ? 1 : 0;
}

/**
 * Returns the bounds of the line for the specified line index.
 *
 * @param lineIndex the line index
 * @return the line bounds
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the line index is out of range</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public Rectangle getLineBounds (int lineIndex) {
	checkLayout();
	return DPIUtil.autoScaleDown(getDevice(), getLineBoundsInPixels(lineIndex));
}

Rectangle getLineBoundsInPixels(int lineIndex) {
	computeRuns(null);
	if (!(0 <= lineIndex && lineIndex < runs.length)) SWT.error(SWT.ERROR_INVALID_RANGE);
	int x = getLineIndent(lineIndex);
	int y = DPIUtil.autoScaleUp(getDevice(), lineY[lineIndex]);
	int width = lineWidth[lineIndex];
	int height = DPIUtil.autoScaleUp(getDevice(), lineY[lineIndex + 1] - lineY[lineIndex] - lineSpacingInPoints);
	return new Rectangle (x, y, width, height);
}

/**
 * Returns the receiver's line count. This includes lines caused
 * by wrapping.
 *
 * @return the line count
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public int getLineCount () {
	checkLayout();
	computeRuns(null);
	return runs.length;
}

int getLineIndent (int lineIndex) {
	int lineIndent = wrapIndent;
	if (lineIndex == 0) {
		lineIndent = indent;
	} else {
		StyleItem[] previousLine = runs[lineIndex - 1];
		StyleItem previousRun = previousLine[previousLine.length - 1];
		if (previousRun.lineBreak && !previousRun.softBreak) {
			lineIndent = indent;
		}
	}
	if (wrapWidth != -1) {
		boolean partialLine = true;
		if (justify) {
			StyleItem[] lineRun = runs[lineIndex];
			if (lineRun[lineRun.length - 1].softBreak) {
				partialLine = false;
			}
		}
		if (partialLine) {
			int lineWidth = this.lineWidth[lineIndex] + lineIndent;
			switch (alignment) {
				case SWT.CENTER: lineIndent += (wrapWidth - lineWidth) / 2; break;
				case SWT.RIGHT: lineIndent += wrapWidth - lineWidth; break;
			}
		}
	}
	return lineIndent;
}

/**
 * Returns the index of the line that contains the specified
 * character offset.
 *
 * @param offset the character offset
 * @return the line index
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the character offset is out of range</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public int getLineIndex (int offset) {
	checkLayout();
	computeRuns(null);
	int length = text.length();
	if (!(0 <= offset && offset <= length)) SWT.error(SWT.ERROR_INVALID_RANGE);
	offset = translateOffset(offset);
	for (int line=0; line<runs.length; line++) {
		if (lineOffset[line + 1] > offset) {
			return line;
		}
	}
	return runs.length - 1;
}

/**
 * Returns the font metrics for the specified line index.
 *
 * @param lineIndex the line index
 * @return the font metrics
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the line index is out of range</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public FontMetrics getLineMetrics (int lineIndex) {
	checkLayout();
	computeRuns(null);
	if (!(0 <= lineIndex && lineIndex < runs.length)) SWT.error(SWT.ERROR_INVALID_RANGE);
	long hDC = device.internal_new_GC(null);
	long srcHdc = OS.CreateCompatibleDC(hDC);
	TEXTMETRIC lptm = new TEXTMETRIC();
	OS.SelectObject(srcHdc, font != null ? font.handle : device.systemFont.handle);
	OS.GetTextMetrics(srcHdc, lptm);
	OS.DeleteDC(srcHdc);
	device.internal_dispose_GC(hDC, null);

	int ascentInPoints = DPIUtil.autoScaleDown(getDevice(), Math.max(lptm.tmAscent, this.ascentInPixels));
	int descentInPoints = DPIUtil.autoScaleDown(getDevice(), Math.max(lptm.tmDescent, this.descentInPixels));
	int leadingInPoints = DPIUtil.autoScaleDown(getDevice(), lptm.tmInternalLeading);
	if (text.length() != 0) {
		for (StyleItem run : runs[lineIndex]) {
			if (run.ascentInPoints > ascentInPoints) {
				ascentInPoints = run.ascentInPoints;
				leadingInPoints = run.leadingInPoints;
			}
			descentInPoints = Math.max(descentInPoints, run.descentInPoints);
		}
	}
	lptm.tmAscent = DPIUtil.autoScaleUp(getDevice(), ascentInPoints);
	lptm.tmDescent = DPIUtil.autoScaleUp(getDevice(), descentInPoints);
	lptm.tmHeight = DPIUtil.autoScaleUp(getDevice(), ascentInPoints + descentInPoints);
	lptm.tmInternalLeading = DPIUtil.autoScaleUp(getDevice(), leadingInPoints);
	lptm.tmAveCharWidth = 0;
	return FontMetrics.win32_new(lptm);
}

/**
 * Returns the line offsets.  Each value in the array is the
 * offset for the first character in a line except for the last
 * value, which contains the length of the text.
 *
 * @return the line offsets
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public int[] getLineOffsets () {
	checkLayout();
	computeRuns(null);
	int[] offsets = new int[lineOffset.length];
	for (int i = 0; i < offsets.length; i++) {
		offsets[i] = untranslateOffset(lineOffset[i]);
	}
	return offsets;
}

/**
 * Returns the location for the specified character offset. The
 * <code>trailing</code> argument indicates whether the offset
 * corresponds to the leading or trailing edge of the cluster.
 *
 * @param offset the character offset
 * @param trailing the trailing flag
 * @return the location of the character offset
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 *
 * @see #getOffset(Point, int[])
 * @see #getOffset(int, int, int[])
 */
public Point getLocation (int offset, boolean trailing) {
	checkLayout();
	return DPIUtil.autoScaleDown(getDevice(), getLocationInPixels(offset, trailing));
}

Point getLocationInPixels (int offset, boolean trailing) {
	computeRuns(null);
	int length = text.length();
	if (!(0 <= offset && offset <= length)) SWT.error(SWT.ERROR_INVALID_RANGE);
	length = segmentsText.length();
	offset = translateOffset(offset);
	int line;
	for (line=0; line<runs.length; line++) {
		if (lineOffset[line + 1] > offset) break;
	}
	line = Math.min(line, runs.length - 1);
	if (offset == length) {
		return new Point(getLineIndent(line) + lineWidth[line], DPIUtil.autoScaleUp(getDevice(), lineY[line]));
	}
	/* For trailing use the low surrogate and for lead use the high surrogate */
	char ch = segmentsText.charAt(offset);
	if (trailing) {
		if (0xD800 <= ch && ch <= 0xDBFF) {
			if (offset + 1 < length) {
				ch = segmentsText.charAt(offset + 1);
				if (0xDC00 <= ch && ch <= 0xDFFF) {
					offset++;
				}
			}
		}
	} else {
		if (0xDC00 <= ch && ch <= 0xDFFF) {
			if (offset - 1 >= 0) {
				ch = segmentsText.charAt(offset - 1);
				if (0xD800 <= ch && ch <= 0xDBFF) {
					offset--;
				}
			}
		}
	}
	int low = -1;
	int high = allRuns.length;
	while (high - low > 1) {
		int index = ((high + low) / 2);
		StyleItem run = allRuns[index];
		if (run.start > offset) {
			high = index;
		} else if (run.start + run.length <= offset) {
			low = index;
		} else {
			int width;
			if (run.style != null && run.style.metrics != null) {
				GlyphMetrics metrics = run.style.metrics;
				width = metrics.getWidthInPixels() * (offset - run.start + (trailing ? 1 : 0));
			} else if (run.tab) {
				width = (trailing || (offset == length)) ? run.width : 0;
			} else {
				int runOffset = offset - run.start;
				final int iX = ScriptCPtoX(runOffset, trailing, run);
				width = (orientation & SWT.RIGHT_TO_LEFT) != 0 ? run.width - iX : iX;
			}
			return new Point(run.x + width, DPIUtil.autoScaleUp(getDevice(), lineY[line]) + getScaledVerticalIndent());
		}
	}
	return new Point(0, 0);
}

/**
 * Wrapper around
 * {@link OS#ScriptCPtoX(int, boolean, int, int, long, long, long, SCRIPT_ANALYSIS, int[])}
 * to handle common arguments consistently.
 *
 * @param characterPosition the first argument of OS.ScriptCPtoX
 * @param trailing          the first argument of OS.ScriptCPtoX
 * @param run               used to define remaining arguments of OS.ScriptCPtoX
 * @return x position of the caret.
 */
private int ScriptCPtoX(int characterPosition, boolean trailing, StyleItem run) {
	int[] piX = new int[1];
	long advances = run.justify != 0 ? run.justify : run.advances;
	OS.ScriptCPtoX(characterPosition, trailing, run.length, run.glyphCount, run.clusters, run.visAttrs, advances,
			run.analysis, piX);
	return piX[0];
}

/**
 * Returns the next offset for the specified offset and movement
 * type.  The movement is one of <code>SWT.MOVEMENT_CHAR</code>,
 * <code>SWT.MOVEMENT_CLUSTER</code>, <code>SWT.MOVEMENT_WORD</code>,
 * <code>SWT.MOVEMENT_WORD_END</code> or <code>SWT.MOVEMENT_WORD_START</code>.
 *
 * @param offset the start offset
 * @param movement the movement type
 * @return the next offset
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the offset is out of range</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 *
 * @see #getPreviousOffset(int, int)
 */
public int getNextOffset (int offset, int movement) {
	checkLayout();
	return _getOffset (offset, movement, true);
}

int _getOffset(int offset, int movement, boolean forward) {
	computeRuns(null);
	int length = text.length();
	if (!(0 <= offset && offset <= length)) SWT.error(SWT.ERROR_INVALID_RANGE, null, " [offset value: " + offset + "]");//$NON-NLS-1$ $NON-NLS-2$
	if (forward && offset == length) return length;
	if (!forward && offset == 0) return 0;
	int step = forward ? 1 : -1;
	if ((movement & SWT.MOVEMENT_CHAR) != 0) return offset + step;
	length = segmentsText.length();
	offset = translateOffset(offset);
	SCRIPT_LOGATTR logAttr = new SCRIPT_LOGATTR();
	SCRIPT_PROPERTIES properties = new  SCRIPT_PROPERTIES();
	int i = forward ? 0 : allRuns.length - 1;
	offset = validadeOffset(offset, step);
	do {
		StyleItem run = allRuns[i];
		if (run.start <= offset && offset < run.start + run.length) {
			if (run.lineBreak && !run.softBreak) return untranslateOffset(run.start);
			if (run.tab) return untranslateOffset(run.start);
			OS.MoveMemory(properties, device.scripts[run.analysis.eScript], SCRIPT_PROPERTIES.sizeof);
			boolean isComplex = properties.fNeedsCaretInfo || properties.fNeedsWordBreaking;
			if (isComplex) breakRun(run);
			while (run.start <= offset && offset < run.start + run.length) {
				if (isComplex) {
					OS.MoveMemory(logAttr, run.psla + ((offset - run.start) * SCRIPT_LOGATTR.sizeof), SCRIPT_LOGATTR.sizeof);
				}
				switch (movement) {
					case SWT.MOVEMENT_CLUSTER: {
						if (!properties.fNeedsCaretInfo || (!logAttr.fInvalid && logAttr.fCharStop)) {
							char ch = segmentsText.charAt(offset);
							if (0xDC00 <= ch && ch <= 0xDFFF) {
								if (offset > 0) {
									ch = segmentsText.charAt(offset - 1);
									if (0xD800 <= ch && ch <= 0xDBFF) {
										offset += step;
									}
								}
							}
							return untranslateOffset(offset);
						}
						break;
					}
					case SWT.MOVEMENT_WORD_START:
					case SWT.MOVEMENT_WORD: {
						if (properties.fNeedsWordBreaking) {
							if (!logAttr.fInvalid && logAttr.fWordStop) return untranslateOffset(offset);
						} else {
							if (offset > 0) {
								boolean letterOrDigit = Character.isLetterOrDigit(segmentsText.charAt(offset));
								boolean previousLetterOrDigit = Character.isLetterOrDigit(segmentsText.charAt(offset - 1));
								if (letterOrDigit != previousLetterOrDigit || !letterOrDigit) {
									if (!Character.isWhitespace(segmentsText.charAt(offset))) {
										return untranslateOffset(offset);
									}
								}
							}
						}
						break;
					}
					case SWT.MOVEMENT_WORD_END: {
						if (offset > 0) {
							boolean isLetterOrDigit = Character.isLetterOrDigit(segmentsText.charAt(offset));
							boolean previousLetterOrDigit = Character.isLetterOrDigit(segmentsText.charAt(offset - 1));
							if (!isLetterOrDigit && previousLetterOrDigit) {
								return untranslateOffset(offset);
							}
						}
						break;
					}
				}
				offset = validadeOffset(offset, step);
			}
		}
		i += step;
	} while (0 <= i && i < allRuns.length - 1 && 0 <= offset && offset < length);
	return forward ? text.length() : 0;
}

/**
 * Returns the character offset for the specified point.
 * For a typical character, the trailing argument will be filled in to
 * indicate whether the point is closer to the leading edge (0) or
 * the trailing edge (1).  When the point is over a cluster composed
 * of multiple characters, the trailing argument will be filled with the
 * position of the character in the cluster that is closest to
 * the point.
 *
 * @param point the point
 * @param trailing the trailing buffer
 * @return the character offset
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the trailing length is less than <code>1</code></li>
 *    <li>ERROR_NULL_ARGUMENT - if the point is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 *
 * @see #getLocation(int, boolean)
 */
public int getOffset (Point point, int[] trailing) {
	checkLayout();
	if (point == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);	return getOffsetInPixels(DPIUtil.autoScaleUp(getDevice(), point), trailing);
}

int getOffsetInPixels (Point point, int[] trailing) {
	return getOffsetInPixels (point.x, point.y, trailing) ;
}

/**
 * Returns the character offset for the specified point.
 * For a typical character, the trailing argument will be filled in to
 * indicate whether the point is closer to the leading edge (0) or
 * the trailing edge (1).  When the point is over a cluster composed
 * of multiple characters, the trailing argument will be filled with the
 * position of the character in the cluster that is closest to
 * the point.
 *
 * @param x the x coordinate of the point
 * @param y the y coordinate of the point
 * @param trailing the trailing buffer
 * @return the character offset
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the trailing length is less than <code>1</code></li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 *
 * @see #getLocation(int, boolean)
 */
public int getOffset (int x, int y, int[] trailing) {
	checkLayout();
	return getOffsetInPixels(DPIUtil.autoScaleUp(getDevice(), x), DPIUtil.autoScaleUp(getDevice(), y), trailing);
}

int getOffsetInPixels (int x, int y, int[] trailing) {
	computeRuns(null);
	if (trailing != null && trailing.length < 1) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	int line;
	int lineCount = runs.length;
	for (line=0; line<lineCount; line++) {
		if (DPIUtil.autoScaleUp(getDevice(), lineY[line + 1]) > y) break;
	}
	line = Math.min(line, runs.length - 1);
	StyleItem[] lineRuns = runs[line];
	int lineIndent = getLineIndent(line);
	if (x >= lineIndent + lineWidth[line]) x = lineIndent + lineWidth[line] - 1;
	if (x < lineIndent) x = lineIndent;
	int low = -1;
	int high = lineRuns.length;
	while (high - low > 1) {
		int index = ((high + low) / 2);
		StyleItem run = lineRuns[index];
		if (run.x > x) {
			high = index;
		} else if (run.x + run.width <= x) {
			low = index;
		} else {
			if (run.lineBreak && !run.softBreak) return untranslateOffset(run.start);
			int xRun = x - run.x;
			if (run.style != null && run.style.metrics != null) {
				GlyphMetrics metrics = run.style.metrics;
				if (metrics.getWidthInPixels() > 0) {
					if (trailing != null) {
						trailing[0] = (xRun % metrics.getWidthInPixels() < metrics.getWidthInPixels() / 2) ? 0 : 1;
					}
					return untranslateOffset(run.start + xRun / metrics.getWidthInPixels());
				}
			}
			if (run.tab) {
				if (trailing != null) trailing[0] = x < (run.x + run.width / 2) ? 0 : 1;
				return untranslateOffset(run.start);
			}
			int cChars = run.length;
			int cGlyphs = run.glyphCount;
			int[] piCP = new int[1];
			int[] piTrailing = new int[1];
			if ((orientation & SWT.RIGHT_TO_LEFT) != 0) {
				xRun = run.width - xRun;
			}
			long advances = run.justify != 0 ? run.justify : run.advances;
			OS.ScriptXtoCP(xRun, cChars, cGlyphs, run.clusters, run.visAttrs, advances, run.analysis, piCP, piTrailing);
			int offset = run.start + piCP[0];
			int length = segmentsText.length();
			char ch = offset < length ? segmentsText.charAt(offset) : 0;
			if (0xD800 <= ch && ch <= 0xDBFF && piTrailing[0] <= 1) {
				if (offset + 1 < length) {
					ch = segmentsText.charAt(offset + 1);
					if (0xDC00 <= ch && ch <= 0xDFFF) {
						if (trailing != null) trailing[0] = 0;
					}
				}
			} else if (0xDC00 <= ch && ch <= 0xDFFF && piTrailing[0] <= 1) {
				if (offset - 1 >= 0) {
					ch = segmentsText.charAt(offset - 1);
					if (0xD800 <= ch && ch <= 0xDBFF) {
						offset--;
						if (trailing != null) trailing[0] = 2;
					}
				}
			} else {
				if (trailing != null) trailing[0] = piTrailing[0];
			}
			return untranslateOffset(offset);
		}
	}
	if (trailing != null) trailing[0] = 0;
	if (lineRuns.length == 1) {
		StyleItem run = lineRuns[0];
		if (run.lineBreak && !run.softBreak) return untranslateOffset(run.start);
	}
	return untranslateOffset(lineOffset[line + 1]);
}

/**
 * Returns the orientation of the receiver.
 *
 * @return the orientation style
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public int getOrientation () {
	checkLayout();
	return orientation;
}

void getPartialSelection(StyleItem run, int selectionStart, int selectionEnd, RECT rect) {
	int end = run.start + run.length - 1;
	int selStart = Math.max(selectionStart, run.start) - run.start;
	int selEnd = Math.min(selectionEnd, end) - run.start;
	int x = rect.left;
	int iX = ScriptCPtoX(selStart, false, run);
	int runX = (orientation & SWT.RIGHT_TO_LEFT) != 0 ? run.width - iX : iX;
	rect.left = x + runX;
	iX = ScriptCPtoX(selEnd, true, run);
	runX = (orientation & SWT.RIGHT_TO_LEFT) != 0 ? run.width - iX : iX;
	rect.right = x + runX;
}

/**
 * Returns the previous offset for the specified offset and movement
 * type.  The movement is one of <code>SWT.MOVEMENT_CHAR</code>,
 * <code>SWT.MOVEMENT_CLUSTER</code> or <code>SWT.MOVEMENT_WORD</code>,
 * <code>SWT.MOVEMENT_WORD_END</code> or <code>SWT.MOVEMENT_WORD_START</code>.
 *
 * @param offset the start offset
 * @param movement the movement type
 * @return the previous offset
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the offset is out of range</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 *
 * @see #getNextOffset(int, int)
 */
public int getPreviousOffset (int offset, int movement) {
	checkLayout();
	return _getOffset (offset, movement, false);
}

/**
 * Gets the ranges of text that are associated with a <code>TextStyle</code>.
 *
 * @return the ranges, an array of offsets representing the start and end of each
 * text style.
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 *
 * @see #getStyles()
 *
 * @since 3.2
 */
public int[] getRanges () {
	checkLayout();
	int[] result = new int[stylesCount * 2];
	int count = 0;
	for (int i=0; i<stylesCount - 1; i++) {
		if (styles[i].style != null) {
			result[count++] = styles[i].start;
			result[count++] = styles[i + 1].start - 1;
		}
	}
	if (count != result.length) {
		int[] newResult = new int[count];
		System.arraycopy(result, 0, newResult, 0, count);
		result = newResult;
	}
	return result;
}

/**
 * Returns the text segments offsets of the receiver.
 *
 * @return the text segments offsets
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public int[] getSegments () {
	checkLayout();
	return segments;
}

/**
 * Returns the segments characters of the receiver.
 *
 * @return the segments characters
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 *
 * @since 3.6
 */
public char[] getSegmentsChars () {
	checkLayout();
	return segmentsChars;
}

String getSegmentsText() {
	int length = text.length();
	if (length == 0) return text;
	if (segments == null) return text;
	int nSegments = segments.length;
	if (nSegments == 0) return text;
	if (segmentsChars == null) {
		if (nSegments == 1) return text;
		if (nSegments == 2) {
			if (segments[0] == 0 && segments[1] == length) return text;
		}
	}
	char[] oldChars = new char[length];
	text.getChars(0, length, oldChars, 0);
	char[] newChars = new char[length + nSegments];
	int charCount = 0, segmentCount = 0;
	char defaultSeparator = (resolveTextDirection() & SWT.RIGHT_TO_LEFT) != 0 ? RTL_MARK : LTR_MARK;
	while (charCount < length) {
		if (segmentCount < nSegments && charCount == segments[segmentCount]) {
			char separator = segmentsChars != null && segmentsChars.length > segmentCount ? segmentsChars[segmentCount] : defaultSeparator;
			newChars[charCount + segmentCount++] = separator;
		} else {
			newChars[charCount + segmentCount] = oldChars[charCount++];
		}
	}
	while (segmentCount < nSegments) {
		segments[segmentCount] = charCount;
		char separator = segmentsChars != null && segmentsChars.length > segmentCount ? segmentsChars[segmentCount] : defaultSeparator;
		newChars[charCount + segmentCount++] = separator;
	}
	return new String(newChars, 0, newChars.length);
}

/**
 * Returns the line spacing of the receiver.
 *
 * @return the line spacing
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public int getSpacing () {
	checkLayout();
	return lineSpacingInPoints;
}

/**
 * Returns the vertical indent of the receiver.
 *
 * @return the vertical indent
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 * @since 3.109
 */
public int getVerticalIndent () {
	checkLayout();
	return verticalIndentInPoints;
}

/**
 * Returns the scaled vertical indent.
 *
 * @return the scaled vertical indent.
 * @since 3.109
 */
private int getScaledVerticalIndent() {
	if (verticalIndentInPoints == 0) {
		return verticalIndentInPoints;
	}
	return DPIUtil.autoScaleUp(getDevice(), verticalIndentInPoints);
}

/**
 * Gets the style of the receiver at the specified character offset.
 *
 * @param offset the text offset
 * @return the style or <code>null</code> if not set
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the character offset is out of range</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public TextStyle getStyle (int offset) {
	checkLayout();
	int length = text.length();
	if (!(0 <= offset && offset < length)) SWT.error(SWT.ERROR_INVALID_RANGE);
	for (int i=1; i<stylesCount; i++) {
		if (styles[i].start > offset) {
			return styles[i - 1].style;
		}
	}
	return null;
}

/**
 * Gets all styles of the receiver.
 *
 * @return the styles
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 *
 * @see #getRanges()
 *
 * @since 3.2
 */
public TextStyle[] getStyles () {
	checkLayout();
	TextStyle[] result = new TextStyle[stylesCount];
	int count = 0;
	for (int i=0; i<stylesCount; i++) {
		if (styles[i].style != null) {
			result[count++] = styles[i].style;
		}
	}
	if (count != result.length) {
		TextStyle[] newResult = new TextStyle[count];
		System.arraycopy(result, 0, newResult, 0, count);
		result = newResult;
	}
	return result;
}

/**
 * Returns the tab list of the receiver.
 *
 * @return the tab list
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public int[] getTabs () {
	checkLayout();
	return DPIUtil.autoScaleDown (getDevice(), getTabsInPixels ());
}

int[] getTabsInPixels () {
	return tabs;
}

/**
 * Gets the receiver's text, which will be an empty
 * string if it has never been set.
 *
 * @return the receiver's text
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public String getText () {
	checkLayout();
	return text;
}

/**
 * Returns the text direction of the receiver.
 *
 * @return the text direction value
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 * @since 3.103
 */
public int getTextDirection () {
	checkLayout();
	return resolveTextDirection();
}

/**
 * Returns the width of the receiver.
 *
 * @return the width
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public int getWidth () {
	checkLayout();
	return DPIUtil.autoScaleDown(getDevice(), getWidthInPixels());
}

int getWidthInPixels () {
	return wrapWidth;
}

/**
* Returns the receiver's wrap indent.
*
* @return the receiver's wrap indent
*
* @exception SWTException <ul>
*    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
* </ul>
*
* @since 3.6
*/
public int getWrapIndent () {
	checkLayout();
	return DPIUtil.autoScaleDown(getDevice(), getWrapIndentInPixels());
}

int getWrapIndentInPixels () {
	return wrapIndent;
}

/**
 * Returns <code>true</code> if the text layout has been disposed,
 * and <code>false</code> otherwise.
 * <p>
 * This method gets the dispose state for the text layout.
 * When a text layout has been disposed, it is an error to
 * invoke any other method (except {@link #dispose()}) using the text layout.
 * </p>
 *
 * @return <code>true</code> when the text layout is disposed and <code>false</code> otherwise
 */
@Override
public boolean isDisposed () {
	return device == null;
}

/*
 *  Itemize the receiver text
 */
StyleItem[] itemize () {
	segmentsText = getSegmentsText();
	int length = segmentsText.length();
	SCRIPT_CONTROL scriptControl = new SCRIPT_CONTROL();
	SCRIPT_STATE scriptState = new SCRIPT_STATE();
	final int MAX_ITEM = length + 1;

	if ((resolveTextDirection() & SWT.RIGHT_TO_LEFT) != 0) {
		scriptState.uBidiLevel = 1;
		scriptState.fArabicNumContext = true;
	}

	OS.ScriptApplyDigitSubstitution(0, scriptControl, scriptState);

	long hHeap = OS.GetProcessHeap();
	// This buffer needs to be one entry bigger than the cMaxItems param to ScriptItemize
	// see https://docs.microsoft.com/en-us/windows/win32/api/usp10/nf-usp10-scriptitemize
	// and https://bugzilla.mozilla.org/show_bug.cgi?id=366643 which was a similar bug
	// in Mozilla. The MSDN docs have been updated since the Mozilla bug to make this clear
	long pItems = OS.HeapAlloc(hHeap, OS.HEAP_ZERO_MEMORY, (1 + MAX_ITEM) * SCRIPT_ITEM.sizeof);
	if (pItems == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	int[] pcItems = new int[1];
	char[] chars = new char[length];
	segmentsText.getChars(0, length, chars, 0);
	// enable font ligatures
	scriptControl.fMergeNeutralItems = true;
	/*
	 * When RIGHT_TO_LEFT Arabic like characters are used, it's not rendered
	 * properly. For more details refer bug 579626(SWT issue #37)
	 */
	if (BidiUtil.resolveTextDirection(text) != SWT.RIGHT_TO_LEFT) {
		/*
		 * With font ligatures enabled: CJK characters are not rendered properly when
		 * used in Java comments, workaround is to avoid ligatures between ascii and
		 * non-ascii chars. For more details refer bug 565526
		 */
		for (int i = 0, latestNeutralIndex = -2, latestUnicodeIndex = -2; i < length; i++) {
			char c = chars[i];

			if (c >= ' ' && c <= '~' && !Character.isAlphabetic(c)) {
				latestNeutralIndex = i;
			} else if (c > 255) {
				latestUnicodeIndex = i;
			} else {
				continue;
			}

			// If the latest neutral and unicode characters are adjacent
			if (Math.abs(latestNeutralIndex - latestUnicodeIndex) == 1) {
				// Change the neutral into a non-neutral alphabet character
				chars[latestNeutralIndex] = 'A';
			}
		}
	}

	OS.ScriptItemize(chars, length, MAX_ITEM, scriptControl, scriptState, pItems, pcItems);

//	if (hr == E_OUTOFMEMORY) //TODO handle it

	StyleItem[] runs = merge(pItems, pcItems[0]);
	OS.HeapFree(hHeap, 0, pItems);
	return runs;
}

/*
 *  Merge styles ranges and script items
 */
StyleItem[] merge (long items, int itemCount) {
	if (styles.length > stylesCount) {
		StyleItem[] newStyles = new StyleItem[stylesCount];
		System.arraycopy(styles, 0, newStyles, 0, stylesCount);
		styles = newStyles;
	}
	final int end = segmentsText.length();
	int start = 0, itemIndex = 0, styleIndex = 0;
	/*
	 * Maximum size of runs is each itemized item + each style needing its own run +
	 * enough space for splitting runs that are too long.
	 */
	List<StyleItem> runs = new ArrayList<>(itemCount + stylesCount + (end + MAX_RUN_LENGTH - 1) / MAX_RUN_LENGTH);
	SCRIPT_ITEM scriptItem = new SCRIPT_ITEM();
	int itemLimit = -1;
	int nextItemIndex = 0;
	boolean linkBefore = false;
	boolean merge = itemCount > TOO_MANY_RUNS;
	SCRIPT_PROPERTIES sp = new SCRIPT_PROPERTIES();
	while (start < end) {
		StyleItem item = new StyleItem();
		item.start = start;
		item.style = styles[styleIndex].style;
		runs.add(item);
		OS.MoveMemory(scriptItem, items + itemIndex * SCRIPT_ITEM.sizeof, SCRIPT_ITEM.sizeof);
		item.analysis = scriptItem.a;
		scriptItem.a = new SCRIPT_ANALYSIS();
		if (linkBefore) {
			item.analysis.fLinkBefore = true;
			linkBefore = false;
		}
		char ch = segmentsText.charAt(start);
		switch (ch) {
			case '\r':
			case '\n':
				item.lineBreak = true;
				break;
			case '\t':
				item.tab = true;
				break;
		}
		if (itemLimit == -1) {
			nextItemIndex = itemIndex + 1;
			OS.MoveMemory(scriptItem, items + nextItemIndex * SCRIPT_ITEM.sizeof, SCRIPT_ITEM.sizeof);
			itemLimit = scriptItem.iCharPos;
			if (nextItemIndex < itemCount && ch == '\r' && segmentsText.charAt(itemLimit) == '\n') {
				nextItemIndex = itemIndex + 2;
				OS.MoveMemory(scriptItem, items + nextItemIndex * SCRIPT_ITEM.sizeof, SCRIPT_ITEM.sizeof);
				itemLimit = scriptItem.iCharPos;
			}

			/*
			 * This block merges a bunch of tabs or non-complex scripts into a single item
			 * run. This is done so that less item runs are needed and is used when there
			 * could be a performance penalty because of too many runs.
			 * The tabs need to be "restored", see computeRuns
			 */
			if (nextItemIndex < itemCount && merge) {
				if (!item.lineBreak) {
					OS.MoveMemory(sp, device.scripts[item.analysis.eScript], SCRIPT_PROPERTIES.sizeof);
					if (!sp.fComplex || item.tab) {
						for (int i = 0; i < MERGE_MAX; i++) {
							if (nextItemIndex == itemCount) break;
							char c = segmentsText.charAt(itemLimit);
							if (c == '\n' || c == '\r') break;
							if (c == '\t' != item.tab) break;
							OS.MoveMemory(sp, device.scripts[scriptItem.a.eScript], SCRIPT_PROPERTIES.sizeof);
							if (!item.tab && sp.fComplex) break;
							nextItemIndex++;
							OS.MoveMemory(scriptItem, items + nextItemIndex * SCRIPT_ITEM.sizeof, SCRIPT_ITEM.sizeof);
							itemLimit = scriptItem.iCharPos;
						}
					}
				}
			}
		}

		boolean mayNeedSplit = true;
		int styleLimit = translateOffset(styles[styleIndex + 1].start);
		if (styleLimit <= itemLimit) {
			int runLen = styleLimit - start;
			if (runLen < MAX_RUN_LENGTH) {
				mayNeedSplit = false;
				styleIndex++;
				start = styleLimit;
				if (start < itemLimit && 0 < start && start < end) {
					char pChar = segmentsText.charAt(start - 1);
					char tChar = segmentsText.charAt(start);
					if (Character.isLetter(pChar) && Character.isLetter(tChar)) {
						item.analysis.fLinkAfter = true;
						linkBefore = true;
					}
				}
			}
		}
		int runLen = itemLimit - start;
		if (mayNeedSplit && runLen > MAX_RUN_LENGTH) {
			start += splitLongRun(item);
		} else if (itemLimit <= styleLimit) {
			itemIndex = nextItemIndex;
			start = itemLimit;
			itemLimit = -1;
		}
		item.length = start - item.start;
	}
	StyleItem item = new StyleItem();
	item.start = end;
	OS.MoveMemory(scriptItem, items + itemCount * SCRIPT_ITEM.sizeof, SCRIPT_ITEM.sizeof);
	item.analysis = scriptItem.a;
	runs.add(item);
	return runs.toArray(StyleItem[]::new);
}

/**
 * Use OS.ScriptBreak to identify where in the run it is safe to split a character.
 * @param run the run to split
 * @return how many characters into the run is the best place to split
 */
int splitLongRun(StyleItem run) {
	run.length = MAX_RUN_LENGTH;
	breakRun(run);
	SCRIPT_LOGATTR logAttr = new SCRIPT_LOGATTR();
	int best = MAX_RUN_LENGTH;
	for (int i = MAX_RUN_LENGTH - 1; i >= MAX_RUN_LENGTH - MAX_SEARCH_RUN_BREAK; i--) {
		int memoryIndex = i * SCRIPT_LOGATTR.sizeof;
		if (memoryIndex + SCRIPT_LOGATTR.sizeof > run.pslaAllocSize) {
			throw new IndexOutOfBoundsException();
		}
		OS.MoveMemory(logAttr, run.psla + memoryIndex, SCRIPT_LOGATTR.sizeof);
		if (logAttr.fSoftBreak || logAttr.fWhiteSpace || logAttr.fWordStop) {
			best = i;
			break;
		}
	}

	/*
	 * In the improbable case that the entire run has nowhere to split we need to
	 * make sure that at least we don't split a surrogate pair. This can happen
	 * if ScriptBreak above identifies nowhere that can be split, and the last
	 * character is the first part of a surrogate pair.
	 */
	if (Character.isHighSurrogate(segmentsText.charAt(run.start + best - 1))) {
		best--;
	}

	return best;
}

/*
 *  Resolves text direction. If the nominal direction is LTR or RTL, no
 *  resolution is needed; if the nominal direction is "auto", have BidiUtil
 *  resolve it according to the first strong bidi character.
 */
int resolveTextDirection () {
	return textDirection == SWT.AUTO_TEXT_DIRECTION ? BidiUtil.resolveTextDirection (text) : textDirection;
}

/*
 *  Reorder the run
 */
StyleItem[] reorder (StyleItem[] runs, boolean terminate) {
	int length = runs.length;
	if (length <= 1) return runs;
	byte[] bidiLevels = new byte[length];
	for (int i=0; i<length; i++) {
		bidiLevels[i] = (byte)(runs[i].analysis.s.uBidiLevel & 0x1F);
	}
	/*
	* Feature in Windows.  If the orientation is RTL Uniscribe will
	* resolve the level of line breaks to 1, this can cause the line
	* break to be reorder to the middle of the line. The fix is to set
	* the level to zero to prevent it to be reordered.
	*/
	StyleItem lastRun = runs[length - 1];
	if (lastRun.lineBreak && !lastRun.softBreak) {
		bidiLevels[length - 1] = 0;
	}
	int[] log2vis = new int[length];
	OS.ScriptLayout(length, bidiLevels, null, log2vis);
	StyleItem[] result = new StyleItem[length];
	for (int i=0; i<length; i++) {
		result[log2vis[i]] = runs[i];
	}
	if ((orientation & SWT.RIGHT_TO_LEFT) != 0) {
		if (terminate) length--;
		for (int i = 0; i < length / 2 ; i++) {
			StyleItem tmp = result[i];
			result[i] = result[length - i - 1];
			result[length - i - 1] = tmp;
		}
	}
	return result;
}

/**
 * Sets the text alignment for the receiver. The alignment controls
 * how a line of text is positioned horizontally. The argument should
 * be one of <code>SWT.LEFT</code>, <code>SWT.RIGHT</code> or <code>SWT.CENTER</code>.
 * <p>
 * The default alignment is <code>SWT.LEFT</code>.  Note that the receiver's
 * width must be set in order to use <code>SWT.RIGHT</code> or <code>SWT.CENTER</code>
 * alignment.
 * </p>
 *
 * @param alignment the new alignment
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 *
 * @see #setWidth(int)
 */
public void setAlignment (int alignment) {
	checkLayout();
	int mask = SWT.LEFT | SWT.CENTER | SWT.RIGHT;
	alignment &= mask;
	if (alignment == 0) return;
	if ((alignment & SWT.LEFT) != 0) alignment = SWT.LEFT;
	if ((alignment & SWT.RIGHT) != 0) alignment = SWT.RIGHT;
	if (this.alignment == alignment) return;
	freeRuns();
	this.alignment = alignment;
}

/**
 * Sets the ascent of the receiver. The ascent is distance in points
 * from the baseline to the top of the line and it is applied to all
 * lines. The default value is <code>-1</code> which means that the
 * ascent is calculated from the line fonts.
 *
 * @param ascent the new ascent
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the ascent is less than <code>-1</code></li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 *
 * @see #setDescent(int)
 * @see #getLineMetrics(int)
 */
public void setAscent (int ascent) {
	checkLayout();
	if (ascent < -1) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	ascent = DPIUtil.autoScaleUp(getDevice(), ascent);
	if (this.ascentInPixels == ascent) return;
	freeRuns();
	this.ascentInPixels = ascent;
}

/**
 * Sets the descent of the receiver. The descent is distance in points
 * from the baseline to the bottom of the line and it is applied to all
 * lines. The default value is <code>-1</code> which means that the
 * descent is calculated from the line fonts.
 *
 * @param descent the new descent
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the descent is less than <code>-1</code></li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 *
 * @see #setAscent(int)
 * @see #getLineMetrics(int)
 */
public void setDescent (int descent) {
	checkLayout();
	if (descent < -1) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	descent = DPIUtil.autoScaleUp(getDevice(), descent);
	if (this.descentInPixels == descent) return;
	freeRuns();
	this.descentInPixels = descent;
}

/**
 * Sets the default font which will be used by the receiver
 * to draw and measure text. If the
 * argument is null, then a default font appropriate
 * for the platform will be used instead. Note that a text
 * style can override the default font.
 *
 * @param font the new font for the receiver, or null to indicate a default font
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the font has been disposed</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public void setFont (Font font) {
	checkLayout();
	if (font != null && font.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	Font oldFont = this.font;
	if (oldFont == font) return;
	this.font = font;
	if (oldFont != null && oldFont.equals(font)) return;
	freeRuns();
}

/**
 * Sets the indent of the receiver. This indent is applied to the first line of
 * each paragraph.
 *
 * @param indent new indent
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 *
 * @see #setWrapIndent(int)
 *
 * @since 3.2
 */
public void setIndent (int indent) {
	checkLayout();
	setIndentInPixels(DPIUtil.autoScaleUp(getDevice(), indent));
}

void setIndentInPixels (int indent) {
	if (indent < 0) return;
	if (this.indent == indent) return;
	freeRuns();
	this.indent = indent;
}

/**
 * Sets the justification of the receiver. Note that the receiver's
 * width must be set in order to use justification.
 *
 * @param justify new justify
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 *
 * @since 3.2
 */
public void setJustify (boolean justify) {
	checkLayout();
	if (this.justify == justify) return;
	freeRuns();
	this.justify = justify;
}

/**
 * Sets the orientation of the receiver, which must be one
 * of <code>SWT.LEFT_TO_RIGHT</code> or <code>SWT.RIGHT_TO_LEFT</code>.
 *
 * @param orientation new orientation style
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public void setOrientation (int orientation) {
	checkLayout();
	int mask = SWT.LEFT_TO_RIGHT | SWT.RIGHT_TO_LEFT;
	orientation &= mask;
	if (orientation == 0) return;
	if ((orientation & SWT.LEFT_TO_RIGHT) != 0) orientation = SWT.LEFT_TO_RIGHT;
	if (this.orientation == orientation) return;
	textDirection = this.orientation = orientation;
	freeRuns();
}

/**
 * Sets the offsets of the receiver's text segments. Text segments are used to
 * override the default behavior of the bidirectional algorithm.
 * Bidirectional reordering can happen within a text segment but not
 * between two adjacent segments.
 * <p>
 * Each text segment is determined by two consecutive offsets in the
 * <code>segments</code> arrays. The first element of the array should
 * always be zero and the last one should always be equals to length of
 * the text.
 * </p>
 * <p>
 * When segments characters are set, the segments are the offsets where
 * the characters are inserted in the text.
 * </p>
 *
 * @param segments the text segments offset
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 *
 * @see #setSegmentsChars(char[])
 */
public void setSegments(int[] segments) {
	checkLayout();
	if (this.segments == null && segments == null) return;
	if (this.segments != null && segments != null) {
		if (this.segments.length == segments.length) {
			int i;
			for (i = 0; i <segments.length; i++) {
				if (this.segments[i] != segments[i]) break;
			}
			if (i == segments.length) return;
		}
	}
	freeRuns();
	this.segments = segments;
}

/**
 * Sets the characters to be used in the segments boundaries. The segments
 * are set by calling <code>setSegments(int[])</code>. The application can
 * use this API to insert Unicode Control Characters in the text to control
 * the display of the text and bidi reordering. The characters are not
 * accessible by any other API in <code>TextLayout</code>.
 *
 * @param segmentsChars the segments characters
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 *
 * @see #setSegments(int[])
 *
 * @since 3.6
 */
public void setSegmentsChars(char[] segmentsChars) {
	checkLayout();
	if (this.segmentsChars == null && segmentsChars == null) return;
	if (this.segmentsChars != null && segmentsChars != null) {
		if (this.segmentsChars.length == segmentsChars.length) {
			int i;
			for (i = 0; i <segmentsChars.length; i++) {
				if (this.segmentsChars[i] != segmentsChars[i]) break;
			}
			if (i == segmentsChars.length) return;
		}
	}
	freeRuns();
	this.segmentsChars = segmentsChars;
}

/**
 * Sets the line spacing of the receiver.  The line spacing
 * is the space left between lines.
 *
 * @param spacing the new line spacing
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the spacing is negative</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public void setSpacing (int spacing) {
	checkLayout();
	if (spacing < 0) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	if (this.lineSpacingInPoints == spacing) return;
	freeRuns();
	this.lineSpacingInPoints = spacing;
}

/**
 * Sets the vertical indent of the receiver.  The vertical indent
 * is the space left before the first line.
 *
 * @param verticalIndent the new vertical indent
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the vertical indent is negative</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 * @since 3.109
 */
public void setVerticalIndent (int verticalIndent) {
	checkLayout();
	if (verticalIndent < 0) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	if (this.verticalIndentInPoints == verticalIndent) return;
	this.verticalIndentInPoints = verticalIndent;
}

/**
 * Sets the style of the receiver for the specified range.  Styles previously
 * set for that range will be overwritten.  The start and end offsets are
 * inclusive and will be clamped if out of range.
 *
 * @param style the style
 * @param start the start offset
 * @param end the end offset
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public void setStyle (TextStyle style, int start, int end) {
	checkLayout();
	int length = text.length();
	if (length == 0) return;
	if (start > end) return;
	start = Math.min(Math.max(0, start), length - 1);
	end = Math.min(Math.max(0, end), length - 1);
	int low = -1;
	int high = stylesCount;
	while (high - low > 1) {
		int index = (high + low) / 2;
		if (styles[index + 1].start > start) {
			high = index;
		} else {
			low = index;
		}
	}
	if (0 <= high && high < stylesCount) {
		StyleItem item = styles[high];
		if (item.start == start && styles[high + 1].start - 1 == end) {
			if (style == null) {
				if (item.style == null) return;
			} else {
				if (style.equals(item.style)) return;
			}
		}
	}
	freeRuns();
	int modifyStart = high;
	int modifyEnd = modifyStart;
	while (modifyEnd < stylesCount) {
		if (styles[modifyEnd + 1].start > end) break;
		modifyEnd++;
	}
	if (modifyStart == modifyEnd) {
		int styleStart = styles[modifyStart].start;
		int styleEnd = styles[modifyEnd + 1].start - 1;
		if (styleStart == start && styleEnd == end) {
			styles[modifyStart].style = style;
			return;
		}
		if (styleStart != start && styleEnd != end) {
			int newLength = stylesCount + 2;
			if (newLength > styles.length) {
				int newSize = Math.min(newLength + 1024, Math.max(64, newLength * 2));
				StyleItem[] newStyles = new StyleItem[newSize];
				System.arraycopy(styles, 0, newStyles, 0, stylesCount);
				styles = newStyles;
			}
			System.arraycopy(styles, modifyEnd + 1, styles, modifyEnd + 3, stylesCount - modifyEnd - 1);
			StyleItem item = new StyleItem();
			item.start = start;
			item.style = style;
			styles[modifyStart + 1] = item;
			item = new StyleItem();
			item.start = end + 1;
			item.style = styles[modifyStart].style;
			styles[modifyStart + 2] = item;
			stylesCount = newLength;
			return;
		}
	}
	if (start == styles[modifyStart].start) modifyStart--;
	if (end == styles[modifyEnd + 1].start - 1) modifyEnd++;
	int newLength = stylesCount + 1 - (modifyEnd - modifyStart - 1);
	if (newLength > styles.length) {
		int newSize = Math.min(newLength + 1024, Math.max(64, newLength * 2));
		StyleItem[] newStyles = new StyleItem[newSize];
		System.arraycopy(styles, 0, newStyles, 0, stylesCount);
		styles = newStyles;
	}
	System.arraycopy(styles, modifyEnd, styles, modifyStart + 2, stylesCount - modifyEnd);
	StyleItem item = new StyleItem();
	item.start = start;
	item.style = style;
	styles[modifyStart + 1] = item;
	styles[modifyStart + 2].start = end + 1;
	stylesCount = newLength;
}

/**
 * Sets the receiver's tab list. Each value in the tab list specifies
 * the space in points from the origin of the text layout to the respective
 * tab stop.  The last tab stop width is repeated continuously.
 *
 * @param tabs the new tab list
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public void setTabs (int[] tabs) {
	checkLayout();
	if (this.tabs == null && tabs == null) return;
	setTabsInPixels (DPIUtil.autoScaleUp (getDevice(), tabs));
}

void setTabsInPixels (int[] tabs) {
	if (Arrays.equals (this.tabs, tabs)) return;
	freeRuns();
	this.tabs = tabs;
}

/**
 * Sets the receiver's text.
 *<p>
 * Note: Setting the text also clears all the styles. This method
 * returns without doing anything if the new text is the same as
 * the current text.
 * </p>
 *
 * @param text the new text
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the text is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public void setText (String text) {
	checkLayout();
	if (text == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (text.equals(this.text)) return;
	freeRuns();
	this.text = text;
	styles = new StyleItem[2];
	styles[0] = new StyleItem();
	styles[1] = new StyleItem();
	styles[1].start = text.length();
	stylesCount = 2;
}

/**
 * Sets the text direction of the receiver, which must be one
 * of <code>SWT.LEFT_TO_RIGHT</code>, <code>SWT.RIGHT_TO_LEFT</code>
 * or <code>SWT.AUTO_TEXT_DIRECTION</code>.
 *
 * <p>
 * <b>Warning</b>: This API is currently only implemented on Windows.
 * It doesn't set the base text direction on GTK and Cocoa.
 * </p>
 *
 * @param textDirection the new text direction
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 * @since 3.103
 */
public void setTextDirection (int textDirection) {
	checkLayout();
	int mask = SWT.LEFT_TO_RIGHT | SWT.RIGHT_TO_LEFT;
	textDirection &= mask;
	if (textDirection == 0) return;
	if (textDirection != SWT.AUTO_TEXT_DIRECTION) {
		if ((textDirection & SWT.LEFT_TO_RIGHT) != 0) textDirection = SWT.LEFT_TO_RIGHT;
		if (this.textDirection == textDirection) return;
	}
	this.textDirection = textDirection;
	freeRuns();
}

/**
 * Sets the line width of the receiver, which determines how
 * text should be wrapped and aligned. The default value is
 * <code>-1</code> which means wrapping is disabled.
 *
 * @param width the new width
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the width is <code>0</code> or less than <code>-1</code></li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 *
 * @see #setAlignment(int)
 */
public void setWidth (int width) {
	checkLayout();
	setWidthInPixels(width != SWT.DEFAULT ? DPIUtil.autoScaleUp(getDevice(), width) : width);
}

void setWidthInPixels (int width) {
	if (width < -1 || width == 0) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	if (this.wrapWidth == width) return;
	freeRuns();
	this.wrapWidth = width;
}

/**
 * Sets the wrap indent of the receiver. This indent is applied to all lines
 * in the paragraph except the first line.
 *
 * @param wrapIndent new wrap indent
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 *
 * @see #setIndent(int)
 *
 * @since 3.6
 */
public void setWrapIndent (int wrapIndent) {
	checkLayout();
	setWrapIndentInPixels(DPIUtil.autoScaleUp(getDevice(), wrapIndent));
}

void setWrapIndentInPixels (int wrapIndent) {
	if (wrapIndent < 0) return;
	if (this.wrapIndent == wrapIndent) return;
	freeRuns();
	this.wrapIndent = wrapIndent;
}

boolean shape (long hdc, StyleItem run, char[] chars, int[] glyphCount, int maxGlyphs, SCRIPT_PROPERTIES sp) {
	boolean useCMAPcheck = !sp.fComplex && !run.analysis.fNoGlyphIndex;
	if (useCMAPcheck) {
		short[] glyphs = new short[chars.length];
		if (OS.ScriptGetCMap(hdc, run.psc, chars, chars.length, 0, glyphs) != OS.S_OK) {
			if (run.psc != 0) {
				OS.ScriptFreeCache(run.psc);
				glyphCount[0] = 0;
				OS.MoveMemory(run.psc, new long [1], C.PTR_SIZEOF);
			}
			return false;
		}
	}
	int scriptShaprHr = OS.ScriptShape(hdc, run.psc, chars, chars.length, maxGlyphs, run.analysis, run.glyphs,
			run.clusters, run.visAttrs, glyphCount);
	if (scriptShaprHr == OS.S_OK) {
		run.glyphCount = glyphCount[0];
		if (useCMAPcheck) return true;

		/*
		 * scriptShapeHr could have been OS.USP_E_SCRIPT_NOT_IN_FONT which indicates
		 * the whole run doesn't work with the font. The rest of this method verifies that
		 * none of the individual glyphs are missing an entry in the font.
		 * The fallback is to try other fonts (See caller)
		 */
		if (run.analysis.fNoGlyphIndex) return true;
		SCRIPT_FONTPROPERTIES fp = new SCRIPT_FONTPROPERTIES ();
		fp.cBytes = SCRIPT_FONTPROPERTIES.sizeof;
		OS.ScriptGetFontProperties(hdc, run.psc, fp);
		short[] glyphs = new short[glyphCount[0]];
		OS.MoveMemory(glyphs, run.glyphs, glyphs.length * 2);
		int i;
		for (i = 0; i < glyphs.length; i++) {
			if (glyphs[i] == fp.wgDefault) break;
		}
		if (i == glyphs.length) return true;
	}
	if (run.psc != 0) {
		OS.ScriptFreeCache(run.psc);
		glyphCount[0] = 0;
		OS.MoveMemory(run.psc, new long [1], C.PTR_SIZEOF);
	}
	run.glyphCount = 0;
	return false;
}

long createMetafileWithChars(long hdc, long hFont, char[] chars, int charCount) {
	long hHeap = OS.GetProcessHeap();

	/*
	 * The native string must remain unchanged between ScriptStringAnalyse and ScriptStringOut.
	 * According to debugging, ScriptStringAnalyse implicitly saves string to SCRIPT_STRING_ANALYSIS.
	 * Then, ScriptStringOut uses the saved string in internal call to ExtTextOutW.
	 * I believe this is due to OS.SSA_METAFILE, which is documented as follows:
	 *     Write items with ExtTextOutW calls, not with glyphs.
	 * Note: passing Java chars to native function is wrong, because JNI will allocate
	 * temporary native string which will be deallocated upon return from ScriptStringAnalyse.
	 */
	int nativeStringSize = charCount * Character.BYTES;
	long nativeString = OS.HeapAlloc (hHeap, OS.HEAP_ZERO_MEMORY, nativeStringSize);
	OS.MoveMemory (nativeString, chars, nativeStringSize);

	long ssa = OS.HeapAlloc(hHeap, OS.HEAP_ZERO_MEMORY, OS.SCRIPT_STRING_ANALYSIS_sizeof());
	long metaFileDc = OS.CreateEnhMetaFile(hdc, null, null, null);
	long oldMetaFont = OS.SelectObject(metaFileDc, hFont);
	int flags = OS.SSA_METAFILE | OS.SSA_FALLBACK | OS.SSA_GLYPHS | OS.SSA_LINK;
	if (OS.ScriptStringAnalyse(metaFileDc, nativeString, charCount, 0, -1, flags, 0, null, null, 0, 0, 0, ssa) == OS.S_OK) {
		OS.ScriptStringOut(ssa, 0, 0, 0, null, 0, 0, false);
		OS.ScriptStringFree(ssa);
	}
	OS.HeapFree(hHeap, 0, nativeString);
	OS.HeapFree(hHeap, 0, ssa);
	OS.SelectObject(metaFileDc, oldMetaFont);
	return OS.CloseEnhMetaFile(metaFileDc);
}

/*
 * Generate glyphs for one Run.
 */
void shape (final long hdc, final StyleItem run) {
	if (run.lineBreak) return;
	if (run.glyphs != 0) return;
	final int[] buffer = new int[1];
	final char[] chars = new char[run.length];
	segmentsText.getChars(run.start, run.start + run.length, chars, 0);

	final int maxGlyphs = (chars.length * 3 / 2) + 16;
	long hHeap = OS.GetProcessHeap();
	run.glyphs = OS.HeapAlloc(hHeap, OS.HEAP_ZERO_MEMORY, maxGlyphs * 2);
	if (run.glyphs == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	run.clusters = OS.HeapAlloc(hHeap, OS.HEAP_ZERO_MEMORY, maxGlyphs * 2);
	if (run.clusters == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	run.visAttrs = OS.HeapAlloc(hHeap, OS.HEAP_ZERO_MEMORY, maxGlyphs * SCRIPT_VISATTR_SIZEOF);
	if (run.visAttrs == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	run.psc = OS.HeapAlloc(hHeap, OS.HEAP_ZERO_MEMORY, C.PTR_SIZEOF);
	if (run.psc == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	final short script = run.analysis.eScript;
	final SCRIPT_PROPERTIES sp = new SCRIPT_PROPERTIES();
	OS.MoveMemory(sp, device.scripts[script], SCRIPT_PROPERTIES.sizeof);
	boolean shapeSucceed = shape(hdc, run, chars, buffer,  maxGlyphs, sp);
	if (!shapeSucceed) {
		if (sp.fPrivateUseArea) {
			run.analysis.fNoGlyphIndex = true;
			shapeSucceed = shape(hdc, run, chars, buffer,  maxGlyphs, sp);
		}
	}
	if (!shapeSucceed) {
		long hFont = OS.GetCurrentObject(hdc, OS.OBJ_FONT);
		long newFont = 0;
		/*
		* Bug in Uniscribe. In some version of Uniscribe, ScriptStringAnalyse crashes
		* when the character array is too long. The fix is to limit the size of character
		* array to two. Note, limiting the array to only one character would cause surrogate
		* pairs to stop working.
		*/
		char[] sampleChars = new char[Math.min(chars.length, 2)];
		SCRIPT_LOGATTR logAttr = new SCRIPT_LOGATTR();
		breakRun(run);
		int count = 0;
		for (int i = 0; i < chars.length; i++) {
			OS.MoveMemory(logAttr, run.psla + (i * SCRIPT_LOGATTR.sizeof), SCRIPT_LOGATTR.sizeof);
			if (!logAttr.fWhiteSpace) {
				sampleChars[count++] = chars[i];
				if (count == sampleChars.length) break;
			}
		}
		if (count > 0) {
			long metaFile = createMetafileWithChars(hdc, hFont, sampleChars, count);
			final EMREXTCREATEFONTINDIRECTW emr = new EMREXTCREATEFONTINDIRECTW();
			class MetaFileEnumProc {
				long metaFileEnumProc (long hDC, long table, long record, long nObj, long lpData) {
					OS.MoveMemory(emr.emr, record, EMR.sizeof);
					switch (emr.emr.iType) {
						case OS.EMR_EXTCREATEFONTINDIRECTW:
							OS.MoveMemory(emr, record, EMREXTCREATEFONTINDIRECTW.sizeof);
							break;
						case OS.EMR_EXTTEXTOUTW:
							return 0;
					}
					return 1;
				}
			}
			MetaFileEnumProc object = new MetaFileEnumProc();
			/* Avoid compiler warnings */
			boolean compilerWarningWorkaround = false;
			if (compilerWarningWorkaround) object.metaFileEnumProc(0, 0, 0, 0, 0);
			Callback callback = new Callback(object, "metaFileEnumProc", 5);
			OS.EnumEnhMetaFile(0, metaFile, callback.getAddress(), 0, null);
			OS.DeleteEnhMetaFile(metaFile);
			callback.dispose();
			newFont = OS.CreateFontIndirect(emr.elfw.elfLogFont);
		} else {
			/*
			* The run is composed only by white spaces, this happens when a run is split
			* by a visual style. The font fallback for the script can not be determined
			* using only white spaces. The solution is to use the font of the previous
			* or next run of the same script.
			*/
			int index = 0;
			while (index < allRuns.length - 1) {
				if (allRuns[index] == run) {
					if (index > 0) {
						StyleItem pRun = allRuns[index - 1];
						if (pRun.analysis.eScript == run.analysis.eScript) {
							long pFont = getItemFont(pRun);
							LOGFONT logFont = new LOGFONT ();
							OS.GetObject(pFont, LOGFONT.sizeof, logFont);
							newFont = OS.CreateFontIndirect(logFont);
						}
					}
					if (newFont == 0) {
						if (index + 1 < allRuns.length - 1) {
							StyleItem nRun = allRuns[index + 1];
							if (nRun.analysis.eScript == run.analysis.eScript) {
								OS.SelectObject(hdc, getItemFont(nRun));
								shape(hdc, nRun);
								long nFont = getItemFont(nRun);
								LOGFONT logFont = new LOGFONT ();
								OS.GetObject(nFont, LOGFONT.sizeof, logFont);
								newFont = OS.CreateFontIndirect(logFont);
							}
						}
					}
					break;
				}
				index++;
			}
		}
		if (newFont != 0) {
			OS.SelectObject(hdc, newFont);
			if (shapeSucceed = shape(hdc, run, chars, buffer,  maxGlyphs, sp)) {
				run.fallbackFont = newFont;
			}
		}
		if (!shapeSucceed) {
			if (!sp.fComplex) {
				run.analysis.fNoGlyphIndex = true;
				if (shapeSucceed = shape(hdc, run, chars, buffer,  maxGlyphs, sp)) {
					run.fallbackFont = newFont;
				} else {
					run.analysis.fNoGlyphIndex = false;
				}
			}
		}
		if (!shapeSucceed) {
			if (mLangFontLink2 != null) {
				long [] hNewFont = new long [1];
				int[] dwCodePages = new int[1], cchCodePages = new int[1];
				mLangFontLink2.GetStrCodePages(chars, chars.length, 0, dwCodePages, cchCodePages);
				if (mLangFontLink2.MapFont(hdc, dwCodePages[0], chars[0], hNewFont) == OS.S_OK) {
					LOGFONT logFont = new LOGFONT ();
					OS.GetObject(hNewFont[0], LOGFONT.sizeof, logFont);
					mLangFontLink2.ReleaseFont(hNewFont[0]);
					long mLangFont = OS.CreateFontIndirect(logFont);
					long oldFont = OS.SelectObject(hdc, mLangFont);
					if (shapeSucceed = shape(hdc, run, chars, buffer,  maxGlyphs, sp)) {
						run.fallbackFont = mLangFont;
					} else {
						OS.SelectObject(hdc, oldFont);
						OS.DeleteObject(mLangFont);
					}
				}
			}
		}
		if (!shapeSucceed) OS.SelectObject(hdc, hFont);
		if (newFont != 0 && newFont != run.fallbackFont) OS.DeleteObject(newFont);
	}

	if (!shapeSucceed) {
		/*
		* Shape Failed.
		* Give up and shape the run with the default font.
		* Missing glyphs typically will be represent as black boxes in the text.
		*/
		OS.ScriptShape(hdc, run.psc, chars, chars.length, maxGlyphs, run.analysis, run.glyphs, run.clusters, run.visAttrs, buffer);
		run.glyphCount = buffer[0];
	}
	int[] abc = new int[3];
	run.advances = OS.HeapAlloc(hHeap, OS.HEAP_ZERO_MEMORY, run.glyphCount * 4);
	if (run.advances == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	run.goffsets = OS.HeapAlloc(hHeap, OS.HEAP_ZERO_MEMORY, run.glyphCount * GOFFSET_SIZEOF);
	if (run.goffsets == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	OS.ScriptPlace(hdc, run.psc, run.glyphs, run.glyphCount, run.visAttrs, run.analysis, run.advances, run.goffsets, abc);
	run.width = abc[0] + abc[1] + abc[2];
	TextStyle style = run.style;
	if (style != null) {
		OUTLINETEXTMETRIC lotm = null;
		if (style.underline || style.strikeout) {
			lotm = new OUTLINETEXTMETRIC();
			if (OS.GetOutlineTextMetrics(hdc, OUTLINETEXTMETRIC.sizeof, lotm) == 0) {
				lotm = null;
			}
		}
		if (style.metrics != null) {
			GlyphMetrics metrics = style.metrics;
			/*
			 *  Bug in Windows, on a Japanese machine, Uniscribe returns glyphcount
			 *  equals zero for FFFC (possibly other unicode code points), the fix
			 *  is to make sure the glyph is at least one pixel wide.
			 */
			run.width = metrics.getWidthInPixels() * Math.max (1, run.glyphCount);
			run.ascentInPoints = metrics.ascent;
			run.descentInPoints = metrics.descent;
			run.leadingInPoints = 0;
		} else {
			TEXTMETRIC lptm = null;
			if (lotm != null) {
				lptm = lotm.otmTextMetrics;
			} else {
				lptm = new TEXTMETRIC();
				OS.GetTextMetrics(hdc, lptm);
			}
			run.ascentInPoints = DPIUtil.autoScaleDown(getDevice(), lptm.tmAscent);
			run.descentInPoints = DPIUtil.autoScaleDown(getDevice(), lptm.tmDescent);
			run.leadingInPoints = DPIUtil.autoScaleDown(getDevice(), lptm.tmInternalLeading);
		}
		if (lotm != null) {
			run.underlinePos = lotm.otmsUnderscorePosition;
			run.underlineThickness = Math.max(1, lotm.otmsUnderscoreSize);
			run.strikeoutPos = lotm.otmsStrikeoutPosition;
			run.strikeoutThickness = Math.max(1, lotm.otmsStrikeoutSize);
		} else {
			run.underlinePos = 1;
			run.underlineThickness = 1;
			run.strikeoutPos = DPIUtil.autoScaleUp(getDevice(), run.ascentInPoints) / 2;
			run.strikeoutThickness = 1;
		}
		run.ascentInPoints += style.rise;
		run.descentInPoints -= style.rise;
	} else {
		TEXTMETRIC lptm = new TEXTMETRIC();
		OS.GetTextMetrics(hdc, lptm);
		run.ascentInPoints = DPIUtil.autoScaleDown(getDevice(), lptm.tmAscent);
		run.descentInPoints = DPIUtil.autoScaleDown(getDevice(), lptm.tmDescent);
		run.leadingInPoints = DPIUtil.autoScaleDown(getDevice(), lptm.tmInternalLeading);
	}
}

int validadeOffset(int offset, int step) {
	offset = untranslateOffset(offset);
	return translateOffset(offset + step);
}

/**
 * Returns a string containing a concise, human-readable
 * description of the receiver.
 *
 * @return a string representation of the receiver
 */
@Override
public String toString () {
	if (isDisposed()) return "TextLayout {*DISPOSED*}";
	return "TextLayout {}";
}

int translateOffset(int offset) {
	int length = text.length();
	if (length == 0) return offset;
	if (segments == null) return offset;
	int nSegments = segments.length;
	if (nSegments == 0) return offset;
	if (segmentsChars == null) {
		if (nSegments == 1) return offset;
		if (nSegments == 2) {
			if (segments[0] == 0 && segments[1] == length) return offset;
		}
	}
	for (int i = 0; i < nSegments && offset - i >= segments[i]; i++) {
		offset++;
	}
	return offset;
}

int untranslateOffset(int offset) {
	int length = text.length();
	if (length == 0) return offset;
	if (segments == null) return offset;
	int nSegments = segments.length;
	if (nSegments == 0) return offset;
	if (segmentsChars == null) {
		if (nSegments == 1) return offset;
		if (nSegments == 2) {
			if (segments[0] == 0 && segments[1] == length) return offset;
		}
	}
	for (int i = 0; i < nSegments && offset > segments[i]; i++) {
		offset--;
	}
	return offset;
}

/**
 * Sets Default Tab Width in terms if number of space characters.
 *
 * @param tabLength in number of characters
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the tabLength is less than <code>0</code></li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 *
 * @noreference This method is not intended to be referenced by clients.
 *
 * DO NOT USE This might be removed in 4.8
 * @since 3.107
 */
public void setDefaultTabWidth(int tabLength) {
	// unused in win32
}
}
