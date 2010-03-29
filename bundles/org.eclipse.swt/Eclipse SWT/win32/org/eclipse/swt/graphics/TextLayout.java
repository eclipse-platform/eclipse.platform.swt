/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.graphics;

import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.gdip.*;
import org.eclipse.swt.internal.win32.*;
import org.eclipse.swt.*;

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
	int lineSpacing;
	int ascent, descent;
	int alignment;
	int wrapWidth;
	int orientation;
	int indent;
	boolean justify;
	int[] tabs;
	int[] segments;
	StyleItem[] styles;
	int stylesCount;

	StyleItem[] allRuns;
	StyleItem[][] runs;
	int[] lineOffset, lineY, lineWidth;
	int /*long*/ mLangFontLink2;
	
	static final char LTR_MARK = '\u200E', RTL_MARK = '\u200F';	
	static final int SCRIPT_VISATTR_SIZEOF = 2;
	static final int GOFFSET_SIZEOF = 8;
	static final byte[] CLSID_CMultiLanguage = new byte[16];
	static final byte[] IID_IMLangFontLink2 = new byte[16];
	static {
		OS.IIDFromString("{275c23e2-3747-11d0-9fea-00aa003f8646}\0".toCharArray(), CLSID_CMultiLanguage);
		OS.IIDFromString("{DCCFC162-2B38-11d2-B7EC-00C04F8F5D9A}\0".toCharArray(), IID_IMLangFontLink2);
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
		int /*long*/ psc = 0;
		
		/*Shape info (malloc when the run is shaped) */
		int /*long*/ glyphs;
		int glyphCount;
		int /*long*/ clusters;
		int /*long*/ visAttrs;
		
		/*Place info (malloc when the run is placed) */
		int /*long*/ advances;
		int /*long*/ goffsets;
		int width;
		int ascent;
		int descent;
		int leading;
		int x;
		int underlinePos, underlineThickness;
		int strikeoutPos, strikeoutThickness;

		/* Justify info (malloc during computeRuns) */
		int /*long*/ justify;

		/* ScriptBreak */
		int /*long*/ psla;

		int /*long*/ fallbackFont;
	
	void free() {
		int /*long*/ hHeap = OS.GetProcessHeap();
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
		width = ascent = descent = x = 0;
		lineBreak = softBreak = false;		
	}
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
	wrapWidth = ascent = descent = -1;
	lineSpacing = 0;
	orientation = SWT.LEFT_TO_RIGHT;
	styles = new StyleItem[2];
	styles[0] = new StyleItem();
	styles[1] = new StyleItem();
	stylesCount = 2;
	text = ""; //$NON-NLS-1$
	int /*long*/[] ppv = new int /*long*/[1];
	OS.OleInitialize(0);
	if (OS.CoCreateInstance(CLSID_CMultiLanguage, 0, OS.CLSCTX_INPROC_SERVER, IID_IMLangFontLink2, ppv) == OS.S_OK) {
		mLangFontLink2 = ppv[0];
	}
	init();
}

void breakRun(StyleItem run) {
	if (run.psla != 0) return;
	char[] chars = new char[run.length];
	segmentsText.getChars(run.start, run.start + run.length, chars, 0);
	int /*long*/ hHeap = OS.GetProcessHeap();
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
	int /*long*/ hDC = gc != null ? gc.handle : device.internal_new_GC(null);
	int /*long*/ srcHdc = OS.CreateCompatibleDC(hDC);
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
		if (run.length == 1) {
			char ch = segmentsText.charAt(run.start);
			switch (ch) {
				case '\t': {
					run.tab = true;
					if (tabs == null) break;
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
					break;
				}
				case '\n': {
					run.lineBreak = true;
					break;
				}
				case '\r': {
					run.lineBreak = true;
					StyleItem next = allRuns[i + 1];
					if (next.length != 0 && segmentsText.charAt(next.start) == '\n') {
						run.length += 1;
						next.free();
						StyleItem[] newAllRuns = new StyleItem[allRuns.length - 1];
						System.arraycopy(allRuns, 0, newAllRuns, 0, i + 1);
						System.arraycopy(allRuns, i + 2, newAllRuns, i + 1, allRuns.length - i - 2);
						allRuns = newAllRuns;
					}
					break;
				}
			}
		} 
		if (wrapWidth != -1 && lineWidth + run.width > wrapWidth && !run.tab) {
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
			if (start == 0 && i != lineStart && !run.tab) {
				run = allRuns[--i];
			} else 	if (start <= 0 && i == lineStart) {
				if (lineWidth == wrapWidth && firstIndice > 0) {
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
			lineWidth = run.softBreak ?  0 : indent;
			lineCount++;
		}
	}
	lineWidth = 0;
	runs = new StyleItem[lineCount][];
	lineOffset = new int[lineCount + 1];
	lineY = new int[lineCount + 1];
	this.lineWidth = new int[lineCount];
	int lineRunCount = 0, line = 0;
	int ascent = Math.max(0, this.ascent);
	int descent = Math.max(0, this.descent);
	StyleItem[] lineRuns = new StyleItem[allRuns.length];
	for (int i=0; i<allRuns.length; i++) {
		StyleItem run = allRuns[i];
		lineRuns[lineRunCount++] = run;
		lineWidth += run.width;
		ascent = Math.max(ascent, run.ascent);
		descent = Math.max(descent, run.descent);
		if (run.lineBreak || i == allRuns.length - 1) {
			/* Update the run metrics if the last run is a hard break. */
			if (lineRunCount == 1 && i == allRuns.length - 1) {
				TEXTMETRIC lptm = OS.IsUnicode ? (TEXTMETRIC)new TEXTMETRICW() : new TEXTMETRICA();
				OS.SelectObject(srcHdc, getItemFont(run));
				OS.GetTextMetrics(srcHdc, lptm);
				run.ascent = lptm.tmAscent;
				run.descent = lptm.tmDescent;
				ascent = Math.max(ascent, run.ascent);
				descent = Math.max(descent, run.descent);
			}
			runs[line] = new StyleItem[lineRunCount];
			System.arraycopy(lineRuns, 0, runs[line], 0, lineRunCount);
			
			if (justify && wrapWidth != -1 && run.softBreak && lineWidth > 0) {
				if (line == 0) {
					lineWidth += indent;
				} else {
					StyleItem[] previousLine = runs[line - 1];
					StyleItem previousRun = previousLine[previousLine.length - 1];
					if (previousRun.lineBreak && !previousRun.softBreak) {
						lineWidth += indent;
					}
				}
				int /*long*/ hHeap = OS.GetProcessHeap();
				int newLineWidth = 0;
				for (int j = 0; j < runs[line].length; j++) {
					StyleItem item = runs[line][j];
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
			for (int j = 0; j < runs[line].length; j++) {
				runs[line][j].x = lineWidth;
				lineWidth += runs[line][j].width;
			}
			line++;
			lineY[line] = lineY[line - 1] + ascent + descent + lineSpacing;
			lineOffset[line] = lastOffset;
			lineRunCount = lineWidth = 0;
			ascent = Math.max(0, this.ascent);
			descent = Math.max(0, this.descent);
		}
	}
	if (srcHdc != 0) OS.DeleteDC(srcHdc);
	if (gc == null) device.internal_dispose_GC(hDC, null);	
}

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
	if (mLangFontLink2 != 0) {
		/* Release() */
		OS.VtblCall(2, mLangFontLink2);
		mLangFontLink2 = 0;
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
	draw(gc, x, y, -1, -1, null, null);
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
	draw(gc, x, y, selectionStart, selectionEnd, selectionForeground, selectionBackground, 0);
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
	computeRuns(gc);
	if (gc == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (gc.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);	
	if (selectionForeground != null && selectionForeground.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	if (selectionBackground != null && selectionBackground.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	int length = text.length();
	if (length == 0 && flags == 0) return;
	int /*long*/ hdc = gc.handle;
	Rectangle clip = gc.getClipping();
	GCData data = gc.data;
	int /*long*/ gdipGraphics = data.gdipGraphics;
	int foreground = data.foreground;
	int alpha = data.alpha;
	boolean gdip = gdipGraphics != 0 && (alpha != 0xFF || data.foregroundPattern != null);
	int /*long*/ clipRgn = 0;
	float[] lpXform = null;
	Rect gdipRect = new Rect();
	if (gdipGraphics != 0 && !gdip) {
		int /*long*/ matrix = Gdip.Matrix_new(1, 0, 0, 1, 0, 0);
		if (matrix == 0) SWT.error(SWT.ERROR_NO_HANDLES);
		Gdip.Graphics_GetTransform(gdipGraphics, matrix);
		int /*long*/ identity = gc.identity();
		Gdip.Matrix_Invert(identity);
		Gdip.Matrix_Multiply(matrix, identity, Gdip.MatrixOrderAppend);
		Gdip.Matrix_delete(identity);
		if (!Gdip.Matrix_IsIdentity(matrix)) {
			lpXform = new float[6];
			Gdip.Matrix_GetElements(matrix, lpXform);
		}
		Gdip.Matrix_delete(matrix);
		if ((data.style & SWT.MIRRORED) != 0 && lpXform != null) {
			gdip = true;
			lpXform = null;
		} else {
			Gdip.Graphics_SetPixelOffsetMode(gdipGraphics, Gdip.PixelOffsetModeNone);
			int /*long*/ rgn = Gdip.Region_new();
			Gdip.Graphics_GetClip(gdipGraphics, rgn);
			if (!Gdip.Region_IsInfinite(rgn, gdipGraphics)) {
				clipRgn = Gdip.Region_GetHRGN(rgn, gdipGraphics);
			}
			Gdip.Region_delete(rgn);
			Gdip.Graphics_SetPixelOffsetMode(gdipGraphics, Gdip.PixelOffsetModeHalf);
			hdc = Gdip.Graphics_GetHDC(gdipGraphics);
		}
	}
	int /*long*/ foregroundBrush = 0;
	int state = 0;
	if (gdip) {
		gc.checkGC(GC.FOREGROUND);
		foregroundBrush = gc.getFgBrush();
	} else {
		state = OS.SaveDC(hdc);
		if ((data.style & SWT.MIRRORED) != 0) {
			OS.SetLayout(hdc, OS.GetLayout(hdc) | OS.LAYOUT_RTL);
		}
		if (lpXform != null) {
			OS.SetGraphicsMode(hdc, OS.GM_ADVANCED);
			OS.SetWorldTransform(hdc, lpXform);
		}
		if (clipRgn != 0) {
			OS.SelectClipRgn(hdc, clipRgn);
			OS.DeleteObject(clipRgn);
		}
	}
	boolean hasSelection = selectionStart <= selectionEnd && selectionStart != -1 && selectionEnd != -1;
	if (hasSelection || (flags & SWT.LAST_LINE_SELECTION) != 0) {
		selectionStart = Math.min(Math.max(0, selectionStart), length - 1);
		selectionEnd = Math.min(Math.max(0, selectionEnd), length - 1);
		if (selectionForeground == null) selectionForeground = device.getSystemColor(SWT.COLOR_LIST_SELECTION_TEXT);
		if (selectionBackground == null) selectionBackground = device.getSystemColor(SWT.COLOR_LIST_SELECTION);
		selectionStart = translateOffset(selectionStart);
		selectionEnd = translateOffset(selectionEnd);
	}
	RECT rect = new RECT();
	int /*long*/ selBrush = 0, selPen = 0, selBrushFg = 0;
	if (hasSelection || (flags & SWT.LAST_LINE_SELECTION) != 0) {
		if (gdip) {
			int bg = selectionBackground.handle;
			int argb = ((alpha & 0xFF) << 24) | ((bg >> 16) & 0xFF) | (bg & 0xFF00) | ((bg & 0xFF) << 16);
			int /*long*/ color = Gdip.Color_new(argb); 
			selBrush = Gdip.SolidBrush_new(color);
			Gdip.Color_delete(color);
			int fg = selectionForeground.handle;
			argb = ((alpha & 0xFF) << 24) | ((fg >> 16) & 0xFF) | (fg & 0xFF00) | ((fg & 0xFF) << 16);
			color = Gdip.Color_new(argb); 
			selBrushFg = Gdip.SolidBrush_new(color);
			selPen = Gdip.Pen_new(selBrushFg, 1);
			Gdip.Color_delete(color);
		} else {
			selBrush = OS.CreateSolidBrush(selectionBackground.handle);
			selPen = OS.CreatePen(OS.PS_SOLID, 1, selectionForeground.handle);
		}
	}
	int offset = (orientation & SWT.RIGHT_TO_LEFT) != 0 ? -1 : 0;
	OS.SetBkMode(hdc, OS.TRANSPARENT);
	for (int line=0; line<runs.length; line++) {
		int drawX = x + getLineIndent(line);
		int drawY = y + lineY[line];
		StyleItem[] lineRuns = runs[line];
		int lineHeight = lineY[line+1] - lineY[line] - lineSpacing;
		if (flags != 0 && (hasSelection || (flags & SWT.LAST_LINE_SELECTION) != 0)) {
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
					width = OS.IsWin95 ? 0x7FFF : 0x6FFFFFF;
				} else {
					width = lineHeight / 3;
				}
				if (gdip) {
					Gdip.Graphics_FillRectangle(gdipGraphics, selBrush, drawX + lineWidth[line], drawY, width, lineHeight);
				} else {
					OS.SelectObject(hdc, selBrush);
					OS.PatBlt(hdc, drawX + lineWidth[line], drawY, width, lineHeight, OS.PATCOPY);
				}
			}
		}
		if (drawX > clip.x + clip.width) continue;
		if (drawX + lineWidth[line] < clip.x) continue;
		int baseline = Math.max(0, this.ascent);
		int lineUnderlinePos = 0;
		for (int i = 0; i < lineRuns.length; i++) {
			baseline = Math.max(baseline, lineRuns[i].ascent);
			lineUnderlinePos = Math.min(lineUnderlinePos, lineRuns[i].underlinePos);
		}
		int alignmentX = drawX;
		for (int i = 0; i < lineRuns.length; i++) {
			StyleItem run = lineRuns[i];
			if (run.length == 0) continue;
			if (drawX > clip.x + clip.width) break;
			if (drawX + run.width >= clip.x) {
				if (!run.lineBreak || run.softBreak) {
					int end = run.start + run.length - 1;
					boolean fullSelection = hasSelection && selectionStart <= run.start && selectionEnd >= end;
					if (fullSelection) {
						if (gdip) {
							Gdip.Graphics_FillRectangle(gdipGraphics, selBrush, drawX, drawY, run.width, lineHeight);
						} else {
							OS.SelectObject(hdc, selBrush);
							OS.PatBlt(hdc, drawX, drawY, run.width, lineHeight, OS.PATCOPY);
						}
					} else {
						if (run.style != null && run.style.background != null) {
							int bg = run.style.background.handle;
							if (gdip) {
								int argb = ((alpha & 0xFF) << 24) | ((bg >> 16) & 0xFF) | (bg & 0xFF00) | ((bg & 0xFF) << 16);
								int /*long*/ color = Gdip.Color_new(argb); 
								int /*long*/ brush = Gdip.SolidBrush_new(color);
								Gdip.Graphics_FillRectangle(gdipGraphics, brush, drawX, drawY, run.width, lineHeight);
								Gdip.Color_delete(color);
								Gdip.SolidBrush_delete(brush);
							} else {
								int /*long*/ hBrush = OS.CreateSolidBrush (bg);
								int /*long*/ oldBrush = OS.SelectObject(hdc, hBrush);
								OS.PatBlt(hdc, drawX, drawY, run.width, lineHeight, OS.PATCOPY);
								OS.SelectObject(hdc, oldBrush);
								OS.DeleteObject(hBrush);
							}
						}
						boolean partialSelection = hasSelection && !(selectionStart > end || run.start > selectionEnd);
						if (partialSelection) {
							int selStart = Math.max(selectionStart, run.start) - run.start;
							int selEnd = Math.min(selectionEnd, end) - run.start;
							int cChars = run.length;
							int gGlyphs = run.glyphCount;
							int[] piX = new int[1];
							int /*long*/ advances = run.justify != 0 ? run.justify : run.advances;
							OS.ScriptCPtoX(selStart, false, cChars, gGlyphs, run.clusters, run.visAttrs, advances, run.analysis, piX);
							int runX = (orientation & SWT.RIGHT_TO_LEFT) != 0 ? run.width - piX[0] : piX[0];
							rect.left = drawX + runX;
							rect.top = drawY;
							OS.ScriptCPtoX(selEnd, true, cChars, gGlyphs, run.clusters, run.visAttrs, advances, run.analysis, piX);
							runX = (orientation & SWT.RIGHT_TO_LEFT) != 0 ? run.width - piX[0] : piX[0];
							rect.right = drawX + runX;
							rect.bottom = drawY + lineHeight;
							if (gdip) {
								if (rect.left > rect.right) {
									int tmp = rect.left;
									rect.left = rect.right;
									rect.right = tmp;
								}
								Gdip.Graphics_FillRectangle(gdipGraphics, selBrush, rect.left, rect.top, rect.right - rect.left, rect.bottom - rect.top);
							} else {
								OS.SelectObject(hdc, selBrush);
								OS.PatBlt(hdc, rect.left, rect.top, rect.right - rect.left, rect.bottom - rect.top, OS.PATCOPY);
							}
						}
					}
				}
			}
			drawX += run.width;
		}
		RECT borderClip = null;
		drawX = alignmentX;
		for (int i = 0; i < lineRuns.length; i++) {
			StyleItem run = lineRuns[i];
			if (run.length == 0) continue;
			if (drawX > clip.x + clip.width) break;
			if (drawX + run.width >= clip.x) {
				if (!run.tab && (!run.lineBreak || run.softBreak) && !(run.style != null && run.style.metrics != null)) {
					int end = run.start + run.length - 1;
					boolean fullSelection = hasSelection && selectionStart <= run.start && selectionEnd >= end;
					boolean partialSelection = hasSelection && !fullSelection && !(selectionStart > end || run.start > selectionEnd);
					OS.SelectObject(hdc, getItemFont(run));
					int drawRunY = drawY + (baseline - run.ascent);
					if (partialSelection) {
						int selStart = Math.max(selectionStart, run.start) - run.start;
						int selEnd = Math.min(selectionEnd, end) - run.start;
						int cChars = run.length;
						int gGlyphs = run.glyphCount;
						int[] piX = new int[1];
						int /*long*/ advances = run.justify != 0 ? run.justify : run.advances;
						OS.ScriptCPtoX(selStart, false, cChars, gGlyphs, run.clusters, run.visAttrs, advances, run.analysis, piX);
						int runX = (orientation & SWT.RIGHT_TO_LEFT) != 0 ? run.width - piX[0] : piX[0];
						rect.left = drawX + runX;
						rect.top = drawY;
						OS.ScriptCPtoX(selEnd, true, cChars, gGlyphs, run.clusters, run.visAttrs, advances, run.analysis, piX);
						runX = (orientation & SWT.RIGHT_TO_LEFT) != 0 ? run.width - piX[0] : piX[0];
						rect.right = drawX + runX;
						rect.bottom = drawY + lineHeight;
					}
					if (gdip) {
						OS.BeginPath(hdc);
						OS.ScriptTextOut(hdc, run.psc, drawX, drawRunY, 0, null, run.analysis , 0, 0, run.glyphs, run.glyphCount, run.advances, run.justify, run.goffsets);
						OS.EndPath(hdc);
						int count = OS.GetPath(hdc, null, null, 0);
						int[] points = new int[count*2];
						byte[] types = new byte[count];
						OS.GetPath(hdc, points, types, count);
						for (int typeIndex = 0; typeIndex < types.length; typeIndex++) {
							int newType = 0;
							int type = types[typeIndex] & 0xFF;
							switch (type & ~OS.PT_CLOSEFIGURE) {
								case OS.PT_MOVETO: newType = Gdip.PathPointTypeStart; break;
								case OS.PT_LINETO: newType = Gdip.PathPointTypeLine; break;
								case OS.PT_BEZIERTO: newType = Gdip.PathPointTypeBezier; break;
							}
							if ((type & OS.PT_CLOSEFIGURE) != 0) newType |= Gdip.PathPointTypeCloseSubpath;
							types[typeIndex] = (byte)newType;
						}
						int /*long*/ path = Gdip.GraphicsPath_new(points, types, count, Gdip.FillModeAlternate);
						if (path == 0) SWT.error(SWT.ERROR_NO_HANDLES);
						int /*long*/ brush = foregroundBrush;
						if (fullSelection) {
							brush = selBrushFg;
						} else {
							if (run.style != null && run.style.foreground != null) {
								int fg = run.style.foreground.handle;
								int argb = ((alpha & 0xFF) << 24) | ((fg >> 16) & 0xFF) | (fg & 0xFF00) | ((fg & 0xFF) << 16);
								int /*long*/ color = Gdip.Color_new(argb); 
								brush = Gdip.SolidBrush_new(color);
								Gdip.Color_delete(color);
							}
						}
						int gstate = 0;
						if (partialSelection) {
							gdipRect.X = rect.left;
							gdipRect.Y = rect.top;
							gdipRect.Width = rect.right - rect.left;
							gdipRect.Height = rect.bottom - rect.top;
							gstate = Gdip.Graphics_Save(gdipGraphics);
							Gdip.Graphics_SetClip(gdipGraphics, gdipRect, Gdip.CombineModeExclude);
						}
						int antialias = Gdip.Graphics_GetSmoothingMode(gdipGraphics), textAntialias = 0;
						int mode = Gdip.Graphics_GetTextRenderingHint(data.gdipGraphics);
						switch (mode) {
							case Gdip.TextRenderingHintSystemDefault: textAntialias = Gdip.SmoothingModeAntiAlias; break;
							case Gdip.TextRenderingHintSingleBitPerPixel:
							case Gdip.TextRenderingHintSingleBitPerPixelGridFit: textAntialias = Gdip.SmoothingModeNone; break;
							case Gdip.TextRenderingHintAntiAlias:
							case Gdip.TextRenderingHintAntiAliasGridFit:
							case Gdip.TextRenderingHintClearTypeGridFit: textAntialias = Gdip.SmoothingModeAntiAlias; break;
						}
						Gdip.Graphics_SetSmoothingMode(gdipGraphics, textAntialias);
						int gstate2 = 0;
						if ((data.style & SWT.MIRRORED) != 0) {
							gstate2 = Gdip.Graphics_Save(gdipGraphics);
							Gdip.Graphics_ScaleTransform(gdipGraphics, -1, 1, Gdip.MatrixOrderPrepend);
							Gdip.Graphics_TranslateTransform(gdipGraphics, -2 * drawX - run.width, 0, Gdip.MatrixOrderPrepend);
						}
						Gdip.Graphics_FillPath(gdipGraphics, brush, path);
						if ((data.style & SWT.MIRRORED) != 0) {
							Gdip.Graphics_Restore(gdipGraphics, gstate2);
						}
						Gdip.Graphics_SetSmoothingMode(gdipGraphics, antialias);
						drawLines(gdip, gdipGraphics, x, drawY + baseline, lineUnderlinePos, drawY + lineHeight, lineRuns, i, brush, null, alpha);
						if (partialSelection) {
							Gdip.Graphics_Restore(gdipGraphics, gstate);
							gstate = Gdip.Graphics_Save(gdipGraphics);
							Gdip.Graphics_SetClip(gdipGraphics, gdipRect, Gdip.CombineModeIntersect);
							Gdip.Graphics_SetSmoothingMode(gdipGraphics, textAntialias);
							if ((data.style & SWT.MIRRORED) != 0) {
								gstate2 = Gdip.Graphics_Save(gdipGraphics);
								Gdip.Graphics_ScaleTransform(gdipGraphics, -1, 1, Gdip.MatrixOrderPrepend);
								Gdip.Graphics_TranslateTransform(gdipGraphics, -2 * drawX - run.width, 0, Gdip.MatrixOrderPrepend);
							}
							Gdip.Graphics_FillPath(gdipGraphics, selBrushFg, path);
							if ((data.style & SWT.MIRRORED) != 0) {
								Gdip.Graphics_Restore(gdipGraphics, gstate2);
							}
							Gdip.Graphics_SetSmoothingMode(gdipGraphics, antialias);
							drawLines(gdip, gdipGraphics, x, drawY + baseline, lineUnderlinePos, drawY + lineHeight, lineRuns, i, selBrushFg, rect, alpha);
							Gdip.Graphics_Restore(gdipGraphics, gstate);
						}
						borderClip = drawBorder(gdip, gdipGraphics, x, drawY, lineHeight, foregroundBrush, selBrushFg, fullSelection, borderClip, partialSelection ? rect : null, alpha, lineRuns, i, selectionStart, selectionEnd);
						Gdip.GraphicsPath_delete(path);
						if (brush != selBrushFg && brush != foregroundBrush) Gdip.SolidBrush_delete(brush);
					}  else {
						int fg = foreground;
						if (fullSelection) {
							fg = selectionForeground.handle;
						} else {
							if (run.style != null && run.style.foreground != null) fg = run.style.foreground.handle;
						}
						OS.SetTextColor(hdc, fg);
						OS.ScriptTextOut(hdc, run.psc, drawX + offset, drawRunY, 0, null, run.analysis , 0, 0, run.glyphs, run.glyphCount, run.advances, run.justify, run.goffsets);
						drawLines(gdip, hdc, x, drawY + baseline, lineUnderlinePos, drawY + lineHeight, lineRuns, i, fg, null, alpha);
						if (partialSelection && fg != selectionForeground.handle) {
							OS.SetTextColor(hdc, selectionForeground.handle);
							OS.ScriptTextOut(hdc, run.psc, drawX + offset, drawRunY, OS.ETO_CLIPPED, rect, run.analysis , 0, 0, run.glyphs, run.glyphCount, run.advances, run.justify, run.goffsets);
							drawLines(gdip, hdc, x, drawY + baseline, lineUnderlinePos, drawY + lineHeight, lineRuns, i, selectionForeground.handle, rect, alpha);
						}
						int selForeground = selectionForeground != null ? selectionForeground.handle : 0;
						borderClip = drawBorder(gdip, hdc, x, drawY, lineHeight, foreground, selForeground, fullSelection, borderClip, partialSelection ? rect : null, alpha, lineRuns, i, selectionStart, selectionEnd);
					}
				}
			}
			drawX += run.width;
		}
	}
	if (gdip) {
		if (selBrush != 0) Gdip.SolidBrush_delete(selBrush);
		if (selBrushFg != 0) Gdip.SolidBrush_delete(selBrushFg);
		if (selPen != 0) Gdip.Pen_delete(selPen);
	} else {
		OS.RestoreDC(hdc, state);
		if (gdipGraphics != 0) Gdip.Graphics_ReleaseHDC(gdipGraphics, hdc);
		if (selBrush != 0) OS.DeleteObject (selBrush);
		if (selPen != 0) OS.DeleteObject (selPen);
	}
}

void drawLines(boolean advance, int /*long*/ graphics, int x, int lineBaseline, int lineUnderlinePos, int lineBottom, StyleItem[] line, int index, int /*long*/ color, RECT clipRect, int alpha) {
	StyleItem run = line[index];
	TextStyle style = run.style;
	if (style == null) return;
	if (!style.underline && !style.strikeout) return;
	int runX = x + run.x;
	int underlineY = lineBaseline - lineUnderlinePos;
	int strikeoutY = lineBaseline - run.strikeoutPos;
	if (advance) {
		Gdip.Graphics_SetPixelOffsetMode(graphics, Gdip.PixelOffsetModeNone);
		int /*long*/ brush = color;
		if (style.underline) {
			if (style.underlineColor != null) {
				int fg = style.underlineColor.handle;
				int argb = ((alpha & 0xFF) << 24) | ((fg >> 16) & 0xFF) | (fg & 0xFF00) | ((fg & 0xFF) << 16);
				int /*long*/ gdiColor = Gdip.Color_new(argb); 
				brush = Gdip.SolidBrush_new(gdiColor);
				Gdip.Color_delete(gdiColor);
			}
			switch (style.underlineStyle) {
				case SWT.UNDERLINE_SQUIGGLE:
				case SWT.UNDERLINE_ERROR: {
					int squigglyThickness = 1;
					int squigglyHeight = 2 * squigglyThickness;
					int squigglyY = Math.min(underlineY - squigglyHeight / 2, lineBottom - squigglyHeight - 1);
					int squigglyX = runX;
					for (int i = index; i > 0 && style.isAdherentUnderline(line[i - 1].style); i--) {
						squigglyX = x + line[i - 1].x;
					}
					int gstate = 0;
					if (clipRect == null) {
						gstate = Gdip.Graphics_Save(graphics);
						Rect gdipRect = new Rect();
						gdipRect.X = runX;
						gdipRect.Y = squigglyY;
						gdipRect.Width = run.width + 1;
						gdipRect.Height = squigglyY + squigglyHeight + 1;
						Gdip.Graphics_SetClip(graphics, gdipRect, Gdip.CombineModeIntersect);
					}
					int[] points = computePolyline(squigglyX, squigglyY, runX + run.width, squigglyY + squigglyHeight);
					int /*long*/ pen = Gdip.Pen_new(brush, squigglyThickness);
					Gdip.Graphics_DrawLines(graphics, pen, points, points.length / 2);
					Gdip.Pen_delete(pen);
					if (gstate != 0) Gdip.Graphics_Restore(graphics, gstate);
					break;
				}
				case SWT.UNDERLINE_SINGLE:
					Gdip.Graphics_FillRectangle(graphics, brush, runX, underlineY, run.width, run.underlineThickness);
					break;
				case SWT.UNDERLINE_DOUBLE:
					Gdip.Graphics_FillRectangle(graphics, brush, runX, underlineY, run.width, run.underlineThickness);
					Gdip.Graphics_FillRectangle(graphics, brush, runX, underlineY + run.underlineThickness * 2, run.width, run.underlineThickness);
					break;
				case UNDERLINE_IME_THICK:
					Gdip.Graphics_FillRectangle(graphics, brush, runX - run.underlineThickness, underlineY, run.width, run.underlineThickness * 2);
					break;
				case UNDERLINE_IME_DOT:
				case UNDERLINE_IME_DASH: {
					int /*long*/ pen = Gdip.Pen_new(brush, 1);
					int dashStyle = style.underlineStyle == UNDERLINE_IME_DOT ? Gdip.DashStyleDot : Gdip.DashStyleDash;
					Gdip.Pen_SetDashStyle(pen, dashStyle);
					Gdip.Graphics_DrawLine(graphics, pen, runX, underlineY, runX + run.width, underlineY);
					Gdip.Pen_delete(pen);
					break;
				}
			}
			if (brush != color) Gdip.SolidBrush_delete(brush);
		}
		if (style.strikeout) {
			if (style.strikeoutColor != null) {
				int fg = style.strikeoutColor.handle;
				int argb = ((alpha & 0xFF) << 24) | ((fg >> 16) & 0xFF) | (fg & 0xFF00) | ((fg & 0xFF) << 16);
				int /*long*/ gdiColor = Gdip.Color_new(argb); 
				brush = Gdip.SolidBrush_new(gdiColor);
				Gdip.Color_delete(gdiColor);
			}
			Gdip.Graphics_FillRectangle(graphics, brush, runX, strikeoutY, run.width, run.strikeoutThickness);
			if (brush != color) Gdip.SolidBrush_delete(brush);
		}
		Gdip.Graphics_SetPixelOffsetMode(graphics, Gdip.PixelOffsetModeHalf);
	} else {
		int colorRefUnderline = (int)/*64*/color;
		int colorRefStrikeout = (int)/*64*/color;
		int /*long*/ brushUnderline = 0;
		int /*long*/ brushStrikeout = 0;
		RECT rect = new RECT();
		if (style.underline) {
			if (style.underlineColor != null) {
				colorRefUnderline = style.underlineColor.handle;
			}
			switch (style.underlineStyle) {
				case SWT.UNDERLINE_SQUIGGLE:
				case SWT.UNDERLINE_ERROR: {
					int squigglyThickness = 1;
					int squigglyHeight = 2 * squigglyThickness;
					int squigglyY = Math.min(underlineY - squigglyHeight / 2, lineBottom - squigglyHeight - 1);
					int squigglyX = runX;
					for (int i = index; i > 0 && style.isAdherentUnderline(line[i - 1].style); i--) {
						squigglyX = x + line[i - 1].x;
					}
					int state = OS.SaveDC(graphics);
					if (clipRect != null) {
						OS.IntersectClipRect(graphics, clipRect.left, clipRect.top, clipRect.right, clipRect.bottom);
					} else {
						OS.IntersectClipRect(graphics, runX, squigglyY, runX + run.width + 1, squigglyY + squigglyHeight + 1);
					}
					int[] points = computePolyline(squigglyX, squigglyY, runX + run.width, squigglyY + squigglyHeight);
					int /*long*/ pen = OS.CreatePen(OS.PS_SOLID, squigglyThickness, colorRefUnderline);
					int /*long*/ oldPen = OS.SelectObject(graphics, pen);
					OS.Polyline(graphics, points, points.length / 2);
					int length = points.length;
					if (length >= 2 && squigglyThickness <= 1) {
						OS.SetPixel (graphics, points[length - 2], points[length - 1], colorRefUnderline);
					}
					OS.RestoreDC(graphics, state);
					OS.SelectObject(graphics, oldPen);
					OS.DeleteObject(pen);
					break;
				}
				case SWT.UNDERLINE_SINGLE:
					brushUnderline = OS.CreateSolidBrush(colorRefUnderline);
					OS.SetRect(rect, runX, underlineY, runX + run.width, underlineY + run.underlineThickness);
					if (clipRect != null) {
						rect.left = Math.max(rect.left, clipRect.left);
						rect.right = Math.min(rect.right, clipRect.right);
					}
					OS.FillRect(graphics, rect, brushUnderline);
					break;
				case SWT.UNDERLINE_DOUBLE:
					brushUnderline = OS.CreateSolidBrush(colorRefUnderline);
					OS.SetRect(rect, runX, underlineY, runX + run.width, underlineY + run.underlineThickness);
					if (clipRect != null) {
						rect.left = Math.max(rect.left, clipRect.left);
						rect.right = Math.min(rect.right, clipRect.right);
					}
					OS.FillRect(graphics, rect, brushUnderline);
					OS.SetRect(rect, runX, underlineY + run.underlineThickness * 2, runX + run.width, underlineY + run.underlineThickness * 3);
					if (clipRect != null) {
						rect.left = Math.max(rect.left, clipRect.left);
						rect.right = Math.min(rect.right, clipRect.right);
					}
					OS.FillRect(graphics, rect, brushUnderline);
					break;
				case UNDERLINE_IME_THICK:
					brushUnderline = OS.CreateSolidBrush(colorRefUnderline);
					OS.SetRect(rect, runX, underlineY - run.underlineThickness, runX + run.width, underlineY + run.underlineThickness);
					if (clipRect != null) {
						rect.left = Math.max(rect.left, clipRect.left);
						rect.right = Math.min(rect.right, clipRect.right);
					}
					OS.FillRect(graphics, rect, brushUnderline);
					break;
				case UNDERLINE_IME_DASH:
				case UNDERLINE_IME_DOT: {
					underlineY = lineBaseline + run.descent;
					int penStyle = style.underlineStyle == UNDERLINE_IME_DASH ? OS.PS_DASH : OS.PS_DOT;
					int /*long*/ pen = OS.CreatePen(penStyle, 1, colorRefUnderline);
					int /*long*/ oldPen = OS.SelectObject(graphics, pen);
					OS.SetRect(rect, runX, underlineY, runX + run.width, underlineY + run.underlineThickness);
					if (clipRect != null) {
						rect.left = Math.max(rect.left, clipRect.left);
						rect.right = Math.min(rect.right, clipRect.right);
					}
					OS.MoveToEx(graphics, rect.left, rect.top, 0);
					OS.LineTo(graphics, rect.right, rect.top);
					OS.SelectObject(graphics, oldPen);
					OS.DeleteObject(pen);
					break;
				}
			}
		}
		if (style.strikeout) {
			if (style.strikeoutColor != null) {
				colorRefStrikeout = style.strikeoutColor.handle;
			}
			if (brushUnderline != 0 && colorRefStrikeout == colorRefUnderline) {
				brushStrikeout = brushUnderline;
			} else {
				brushStrikeout = OS.CreateSolidBrush(colorRefStrikeout);
			}
			OS.SetRect(rect, runX, strikeoutY, runX + run.width, strikeoutY + run.strikeoutThickness);
			if (clipRect != null) {
				rect.left = Math.max(rect.left, clipRect.left);
				rect.right = Math.min(rect.right, clipRect.right);
			}
			OS.FillRect(graphics, rect, brushStrikeout);
		}
		if (brushUnderline != 0) OS.DeleteObject(brushUnderline);
		if (brushStrikeout != 0 && brushStrikeout != brushUnderline) OS.DeleteObject(brushStrikeout);
	}
}

RECT drawBorder(boolean advance, int /*long*/ graphics, int x, int y, int lineHeight, int /*long*/ color, int /*long*/ selectionColor, boolean fullSelection, RECT clipRect, RECT rect, int alpha, StyleItem[] line, int index, int selectionStart, int selectionEnd) {
	StyleItem run = line[index]; 
	TextStyle style = run.style;
	if (style == null) return null;
	if (style.borderStyle == SWT.NONE) return null;
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
	if (index + 1 >= line.length || !style.isAdherentBorder(line[index + 1].style)) {
		int left = run.x;
		for (int i = index; i > 0 && style.isAdherentBorder(line[i - 1].style); i--) {
			left = line[i - 1].x;
		}
		if (advance) {
			int /*long*/ brush = color;
			int customColor = -1;
			if (style.borderColor != null) {
				customColor = style.borderColor.handle;
			} else {
				if (style.foreground != null) {
					customColor = style.foreground.handle;
				}
				if (fullSelection && clipRect == null) {
					customColor = -1;
					brush = selectionColor;
				}
			}
			if (customColor != -1) {
				int argb = ((alpha & 0xFF) << 24) | ((customColor >> 16) & 0xFF) | (customColor & 0xFF00) | ((customColor & 0xFF) << 16);
				int /*long*/ gdiColor = Gdip.Color_new(argb); 
				brush = Gdip.SolidBrush_new(gdiColor);
				Gdip.Color_delete(gdiColor);	
			}
			int lineWidth = 1;
			int lineStyle = Gdip.DashStyleSolid;
			switch (style.borderStyle) {
				case SWT.BORDER_SOLID: break;
				case SWT.BORDER_DASH: lineStyle = Gdip.DashStyleDash; break;
				case SWT.BORDER_DOT: lineStyle = Gdip.DashStyleDot; break;
			}
			int /*long*/ pen = Gdip.Pen_new(brush, lineWidth);
			Gdip.Pen_SetDashStyle(pen, lineStyle);
			float gdipXOffset = 0.5f, gdipYOffset = 0.5f;
			Gdip.Graphics_TranslateTransform(graphics, gdipXOffset, gdipYOffset, Gdip.MatrixOrderPrepend);
			if (style.borderColor == null && clipRect != null) {
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
				int /*long*/ selPen = Gdip.Pen_new(selectionColor, lineWidth);
				Gdip.Pen_SetDashStyle(pen, lineStyle);
				Gdip.Graphics_DrawRectangle(graphics, selPen, x + left, y, run.x + run.width - left - 1, lineHeight - 1);
				Gdip.Pen_delete(selPen);
				Gdip.Graphics_Restore(graphics, gstate);
			} else {
				Gdip.Graphics_DrawRectangle(graphics, pen, x + left, y, run.x + run.width - left - 1, lineHeight - 1);
			}
			Gdip.Graphics_TranslateTransform(graphics, -gdipXOffset, -gdipYOffset, Gdip.MatrixOrderPrepend);
			Gdip.Pen_delete(pen);
			if (customColor != -1) Gdip.SolidBrush_delete(brush);
		} else {
			if (style.borderColor != null) {
				color = style.borderColor.handle;
			} else {
				if (style.foreground != null) {
					color = style.foreground.handle;
				}
				if (fullSelection && clipRect == null) {
					color = selectionColor;
				}
			}
			int lineWidth = 1;
			int lineStyle = OS.PS_SOLID;
			switch (style.borderStyle) {
				case SWT.BORDER_SOLID: break;
				case SWT.BORDER_DASH: lineStyle = OS.PS_DASH; break;
				case SWT.BORDER_DOT: lineStyle = OS.PS_DOT; break;
			}
			LOGBRUSH logBrush = new LOGBRUSH();
			logBrush.lbStyle = OS.BS_SOLID;
			logBrush.lbColor = /*64*/(int)color;
			int /*long*/ newPen = OS.ExtCreatePen(lineStyle | OS.PS_GEOMETRIC, Math.max(1, lineWidth), logBrush, 0, null);
			int /*long*/ oldPen = OS.SelectObject(graphics, newPen);
			int /*long*/ oldBrush = OS.SelectObject(graphics, OS.GetStockObject(OS.NULL_BRUSH));
			OS.Rectangle(graphics, x + left, y, x + run.x + run.width, y + lineHeight);
			if (style.borderColor == null && clipRect != null && color != selectionColor) {
				int state = OS.SaveDC(graphics);
				if (clipRect.left == -1) clipRect.left = 0;
				if (clipRect.right == -1) clipRect.right = 0x7ffff;
				OS.IntersectClipRect(graphics, clipRect.left, clipRect.top, clipRect.right, clipRect.bottom);
				logBrush.lbColor = /*64*/(int)selectionColor;
				int /*long*/ selPen = OS.ExtCreatePen (lineStyle | OS.PS_GEOMETRIC, Math.max(1, lineWidth), logBrush, 0, null);
				OS.SelectObject(graphics, selPen);
				OS.Rectangle(graphics, x + left, y, x + run.x + run.width, y + lineHeight);
				OS.RestoreDC(graphics, state);
				OS.SelectObject(graphics, newPen);
				OS.DeleteObject(selPen);
			}
			OS.SelectObject(graphics, oldBrush);
			OS.SelectObject(graphics, oldPen);
			OS.DeleteObject(newPen);
		}
		return null;
	}
	return clipRect;
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

void freeRuns () {
	if (allRuns == null) return;
	for (int i=0; i<allRuns.length; i++) {
		StyleItem run = allRuns[i];
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
	return ascent;
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
	return new Rectangle (0, 0, width, lineY[lineY.length - 1]);
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
	computeRuns(null);
	int length = text.length();
	if (length == 0) return new Rectangle(0, 0, 0, 0);
	if (start > end) return new Rectangle(0, 0, 0, 0);
	start = Math.min(Math.max(0, start), length - 1);
	end = Math.min(Math.max(0, end), length - 1);
	start = translateOffset(start);
	end = translateOffset(end);
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
				cx = metrics.width * (start - run.start);
			} else if (!run.tab) {
				int[] piX = new int[1];
				int /*long*/ advances = run.justify != 0 ? run.justify : run.advances;
				OS.ScriptCPtoX(start - run.start, false, run.length, run.glyphCount, run.clusters, run.visAttrs, advances, run.analysis, piX);
				cx = isRTL ? run.width - piX[0] : piX[0];
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
				cx = metrics.width * (end - run.start + 1);
			} else if (!run.tab) {
				int[] piX = new int[1];
				int /*long*/ advances = run.justify != 0 ? run.justify : run.advances;
				OS.ScriptCPtoX(end - run.start, true, run.length, run.glyphCount, run.clusters, run.visAttrs, advances, run.analysis, piX);
				cx = isRTL ? run.width - piX[0] : piX[0];
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
		top = Math.min(top, lineY[lineIndex]);
		bottom = Math.max(bottom, lineY[lineIndex + 1] - lineSpacing);
	}
	return new Rectangle(left, top, right - left, bottom - top);
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
	return descent;
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

int /*long*/ getItemFont (StyleItem item) {
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
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
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
	return (orientation & SWT.RIGHT_TO_LEFT) != 0 ? 1 : 0;
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
public Rectangle getLineBounds(int lineIndex) {
	checkLayout();
	computeRuns(null);
	if (!(0 <= lineIndex && lineIndex < runs.length)) SWT.error(SWT.ERROR_INVALID_RANGE);
	int x = getLineIndent(lineIndex);
	int y = lineY[lineIndex];
	int width = lineWidth[lineIndex];
	int height = lineY[lineIndex + 1] - y - lineSpacing;
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
	int lineIndent = 0;
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
	int /*long*/ hDC = device.internal_new_GC(null);
	int /*long*/ srcHdc = OS.CreateCompatibleDC(hDC);
	TEXTMETRIC lptm = OS.IsUnicode ? (TEXTMETRIC)new TEXTMETRICW() : new TEXTMETRICA();
	OS.SelectObject(srcHdc, font != null ? font.handle : device.systemFont.handle);
	OS.GetTextMetrics(srcHdc, lptm);
	OS.DeleteDC(srcHdc);
	device.internal_dispose_GC(hDC, null);
	
	int ascent = Math.max(lptm.tmAscent, this.ascent);
	int descent = Math.max(lptm.tmDescent, this.descent);
	int leading = lptm.tmInternalLeading;
	if (text.length() != 0) {
		StyleItem[] lineRuns = runs[lineIndex];
		for (int i = 0; i<lineRuns.length; i++) {
			StyleItem run = lineRuns[i];
			if (run.ascent > ascent) {
				ascent = run.ascent;
				leading = run.leading;
			}
			descent = Math.max(descent, run.descent);
		}
	}
	lptm.tmAscent = ascent;
	lptm.tmDescent = descent;
	lptm.tmHeight = ascent + descent;
	lptm.tmInternalLeading = leading;
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
		return new Point(getLineIndent(line) + lineWidth[line], lineY[line]);
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
				width = metrics.width * (offset - run.start + (trailing ? 1 : 0));
			} else if (run.tab) {
				width = (trailing || (offset == length)) ? run.width : 0;
			} else {
				int runOffset = offset - run.start;
				int cChars = run.length;
				int gGlyphs = run.glyphCount;
				int[] piX = new int[1];
				int /*long*/ advances = run.justify != 0 ? run.justify : run.advances;
				OS.ScriptCPtoX(runOffset, trailing, cChars, gGlyphs, run.clusters, run.visAttrs, advances, run.analysis, piX);
				width = (orientation & SWT.RIGHT_TO_LEFT) != 0 ? run.width - piX[0] : piX[0];   
			}
			return new Point(run.x + width, lineY[line]);
		}
	}
	return new Point(0, 0);
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
	if (!(0 <= offset && offset <= length)) SWT.error(SWT.ERROR_INVALID_RANGE);
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
						if (properties.fNeedsCaretInfo) {
							if (!logAttr.fInvalid && logAttr.fCharStop) return untranslateOffset(offset);
						} else {
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
								boolean letterOrDigit = Compatibility.isLetterOrDigit(segmentsText.charAt(offset));
								boolean previousLetterOrDigit = Compatibility.isLetterOrDigit(segmentsText.charAt(offset - 1));
								if (letterOrDigit != previousLetterOrDigit || !letterOrDigit) {
									if (!Compatibility.isWhitespace(segmentsText.charAt(offset))) {
										return untranslateOffset(offset);
									}
								}
							}
						}
						break;
					}
					case SWT.MOVEMENT_WORD_END: {
						if (offset > 0) {
							boolean isLetterOrDigit = Compatibility.isLetterOrDigit(segmentsText.charAt(offset));
							boolean previousLetterOrDigit = Compatibility.isLetterOrDigit(segmentsText.charAt(offset - 1));
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
	if (point == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
	return getOffset (point.x, point.y, trailing) ;
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
	computeRuns(null);
	if (trailing != null && trailing.length < 1) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	int line;
	int lineCount = runs.length;
	for (line=0; line<lineCount; line++) {
		if (lineY[line + 1] > y) break;
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
				if (metrics.width > 0) {
					if (trailing != null) {
						trailing[0] = (xRun % metrics.width < metrics.width / 2) ? 0 : 1;
					}
					return untranslateOffset(run.start + xRun / metrics.width);
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
			int /*long*/ advances = run.justify != 0 ? run.justify : run.advances;
			OS.ScriptXtoCP(xRun, cChars, cGlyphs, run.clusters, run.visAttrs, advances, run.analysis, piCP, piTrailing);
			if (trailing != null) trailing[0] = piTrailing[0];
			return untranslateOffset(run.start + piCP[0]);
		}
	}
	if (trailing != null) trailing[0] = 0;
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

String getSegmentsText() {
	if (segments == null) return text;
	int nSegments = segments.length;
	if (nSegments <= 1) return text;
	int length = text.length();
	if (length == 0) return text;
	if (nSegments == 2) {
		if (segments[0] == 0 && segments[1] == length) return text;
	}
	char[] oldChars = new char[length];
	text.getChars(0, length, oldChars, 0);
	char[] newChars = new char[length + nSegments];
	int charCount = 0, segmentCount = 0;
	char separator = orientation == SWT.RIGHT_TO_LEFT ? RTL_MARK : LTR_MARK;
	while (charCount < length) {
		if (segmentCount < nSegments && charCount == segments[segmentCount]) {
			newChars[charCount + segmentCount++] = separator;
		} else {
			newChars[charCount + segmentCount] = oldChars[charCount++];
		}
	}
	if (segmentCount < nSegments) {
		segments[segmentCount] = charCount;
		newChars[charCount + segmentCount++] = separator;
	}
	return new String(newChars, 0, Math.min(charCount + segmentCount, newChars.length));
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
	return lineSpacing;
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
	return wrapWidth;
}

/**
 * Returns <code>true</code> if the text layout has been disposed,
 * and <code>false</code> otherwise.
 * <p>
 * This method gets the dispose state for the text layout.
 * When a text layout has been disposed, it is an error to
 * invoke any other method using the text layout.
 * </p>
 *
 * @return <code>true</code> when the text layout is disposed and <code>false</code> otherwise
 */
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
	
	if ((orientation & SWT.RIGHT_TO_LEFT) != 0) {
		scriptState.uBidiLevel = 1;
		scriptState.fArabicNumContext = true;
		SCRIPT_DIGITSUBSTITUTE psds = new SCRIPT_DIGITSUBSTITUTE();
		OS.ScriptRecordDigitSubstitution(OS.LOCALE_USER_DEFAULT, psds);
		OS.ScriptApplyDigitSubstitution(psds, scriptControl, scriptState);
	}
	
	int /*long*/ hHeap = OS.GetProcessHeap();
	int /*long*/ pItems = OS.HeapAlloc(hHeap, OS.HEAP_ZERO_MEMORY, MAX_ITEM * SCRIPT_ITEM.sizeof);
	if (pItems == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	int[] pcItems = new int[1];	
	char[] chars = new char[length];
	segmentsText.getChars(0, length, chars, 0); 
	OS.ScriptItemize(chars, length, MAX_ITEM, scriptControl, scriptState, pItems, pcItems);
//	if (hr == E_OUTOFMEMORY) //TODO handle it
	
	StyleItem[] runs = merge(pItems, pcItems[0]);
	OS.HeapFree(hHeap, 0, pItems);
	return runs;
}

/* 
 *  Merge styles ranges and script items 
 */
StyleItem[] merge (int /*long*/ items, int itemCount) {
	if (styles.length > stylesCount) {
		StyleItem[] newStyles = new StyleItem[stylesCount];
		System.arraycopy(styles, 0, newStyles, 0, stylesCount);
		styles = newStyles;
	}
	int count = 0, start = 0, end = segmentsText.length(), itemIndex = 0, styleIndex = 0;
	StyleItem[] runs = new StyleItem[itemCount + stylesCount];
	SCRIPT_ITEM scriptItem = new SCRIPT_ITEM();
	boolean linkBefore = false;
	while (start < end) {
		StyleItem item = new StyleItem();
		item.start = start;
		item.style = styles[styleIndex].style;
		runs[count++] = item;
		OS.MoveMemory(scriptItem, items + itemIndex * SCRIPT_ITEM.sizeof, SCRIPT_ITEM.sizeof);
		item.analysis = scriptItem.a;
		if (linkBefore) {
			item.analysis.fLinkBefore = true;
			linkBefore = false;
		}
		scriptItem.a = new SCRIPT_ANALYSIS();
		OS.MoveMemory(scriptItem, items + (itemIndex + 1) * SCRIPT_ITEM.sizeof, SCRIPT_ITEM.sizeof);
		int itemLimit = scriptItem.iCharPos;
		int styleLimit = translateOffset(styles[styleIndex + 1].start);
		if (styleLimit <= itemLimit) {
			styleIndex++;
			start = styleLimit;
			if (start < itemLimit && 0 < start && start < end) {
				char pChar = segmentsText.charAt(start - 1);
				char tChar = segmentsText.charAt(start);
				if (Compatibility.isLetter(pChar) && Compatibility.isLetter(tChar)) {
					item.analysis.fLinkAfter = true;
					linkBefore = true;
				}
			}
		}
		if (itemLimit <= styleLimit) {
			itemIndex++;
			start = itemLimit;
		}
		item.length = start - item.start;
	}
	StyleItem item = new StyleItem();
	item.start = end;
	OS.MoveMemory(scriptItem, items + itemCount * SCRIPT_ITEM.sizeof, SCRIPT_ITEM.sizeof);
	item.analysis = scriptItem.a;
	runs[count++] = item;
	if (runs.length != count) {
		StyleItem[] result = new StyleItem[count];
		System.arraycopy(runs, 0, result, 0, count);
		return result;
	}
	return runs;
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
 * Sets the ascent of the receiver. The ascent is distance in pixels
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
public void setAscent(int ascent) {
	checkLayout();
	if (ascent < -1) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	if (this.ascent == ascent) return;
	freeRuns();
	this.ascent = ascent;
}

/**
 * Sets the descent of the receiver. The descent is distance in pixels
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
public void setDescent(int descent) {
	checkLayout();
	if (descent < -1) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	if (this.descent == descent) return;
	freeRuns();
	this.descent = descent;
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
 * Sets the indent of the receiver. This indent it applied of the first line of 
 * each paragraph.  
 *
 * @param indent new indent
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 * 
 * @since 3.2
 */
public void setIndent (int indent) {
	checkLayout();
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
	this.orientation = orientation;
	freeRuns();
}

/**
 * Sets the offsets of the receiver's text segments. Text segments are used to
 * override the default behaviour of the bidirectional algorithm.
 * Bidirectional reordering can happen within a text segment but not 
 * between two adjacent segments.
 * <p>
 * Each text segment is determined by two consecutive offsets in the 
 * <code>segments</code> arrays. The first element of the array should 
 * always be zero and the last one should always be equals to length of
 * the text.
 * </p>
 * 
 * @param segments the text segments offset
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
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
	if (this.lineSpacing == spacing) return;
	freeRuns();
	this.lineSpacing = spacing;
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
 * the space in pixels from the origin of the text layout to the respective
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
	if (this.tabs != null && tabs !=null) {
		if (this.tabs.length == tabs.length) {
			int i;
			for (i = 0; i <tabs.length; i++) {
				if (this.tabs[i] != tabs[i]) break;
			}
			if (i == tabs.length) return;
		}
	}
	freeRuns();
	this.tabs = tabs;
} 

/**
 * Sets the receiver's text.
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
	if (width < -1 || width == 0) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	if (this.wrapWidth == width) return;
	freeRuns();
	this.wrapWidth = width;
}

boolean shape (int /*long*/ hdc, StyleItem run, char[] chars, int[] glyphCount, int maxGlyphs, SCRIPT_PROPERTIES sp) {
	boolean useCMAPcheck = !sp.fComplex && !run.analysis.fNoGlyphIndex;
	if (useCMAPcheck) {
		short[] glyphs = new short[chars.length];
		if (OS.ScriptGetCMap(hdc, run.psc, chars, chars.length, 0, glyphs) != OS.S_OK) {
			if (run.psc != 0) {
				OS.ScriptFreeCache(run.psc);
				glyphCount[0] = 0;
				OS.MoveMemory(run.psc, new int /*long*/ [1], OS.PTR_SIZEOF);
			}
			return false;
		}
	}
	int hr = OS.ScriptShape(hdc, run.psc, chars, chars.length, maxGlyphs, run.analysis, run.glyphs, run.clusters, run.visAttrs, glyphCount);
	run.glyphCount = glyphCount[0];
	if (useCMAPcheck) return true;
	
	if (hr != OS.USP_E_SCRIPT_NOT_IN_FONT) {
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
		OS.MoveMemory(run.psc, new int /*long*/ [1], OS.PTR_SIZEOF);
	}
	run.glyphCount = 0;
	return false;
}

/* 
 * Generate glyphs for one Run.
 */
void shape (final int /*long*/ hdc, final StyleItem run) {
	final int[] buffer = new int[1];
	final char[] chars = new char[run.length];
	segmentsText.getChars(run.start, run.start + run.length, chars, 0);
	
	final int maxGlyphs = (chars.length * 3 / 2) + 16;
	int /*long*/ hHeap = OS.GetProcessHeap();
	run.glyphs = OS.HeapAlloc(hHeap, OS.HEAP_ZERO_MEMORY, maxGlyphs * 2);
	if (run.glyphs == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	run.clusters = OS.HeapAlloc(hHeap, OS.HEAP_ZERO_MEMORY, maxGlyphs * 2);
	if (run.clusters == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	run.visAttrs = OS.HeapAlloc(hHeap, OS.HEAP_ZERO_MEMORY, maxGlyphs * SCRIPT_VISATTR_SIZEOF);
	if (run.visAttrs == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	run.psc = OS.HeapAlloc(hHeap, OS.HEAP_ZERO_MEMORY, OS.PTR_SIZEOF);
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
		int /*long*/ hFont = OS.GetCurrentObject(hdc, OS.OBJ_FONT);
		int /*long*/ ssa = OS.HeapAlloc(hHeap, OS.HEAP_ZERO_MEMORY, OS.SCRIPT_STRING_ANALYSIS_sizeof());
		int /*long*/ metaFileDc = OS.CreateEnhMetaFile(hdc, null, null, null);
		int /*long*/ oldMetaFont = OS.SelectObject(metaFileDc, hFont);
		int flags = OS.SSA_METAFILE | OS.SSA_FALLBACK | OS.SSA_GLYPHS | OS.SSA_LINK;
		if (OS.ScriptStringAnalyse(metaFileDc, chars, chars.length, 0, -1, flags, 0, null, null, 0, 0, 0, ssa) == OS.S_OK) {
			OS.ScriptStringOut(ssa, 0, 0, 0, null, 0, 0, false);
			OS.ScriptStringFree(ssa);
		}
		OS.HeapFree(hHeap, 0, ssa);
		OS.SelectObject(metaFileDc, oldMetaFont);
		int /*long*/ metaFile = OS.CloseEnhMetaFile(metaFileDc);
		final EMREXTCREATEFONTINDIRECTW emr = new EMREXTCREATEFONTINDIRECTW();
		class MetaFileEnumProc {
			int /*long*/ metaFileEnumProc (int /*long*/ hDC, int /*long*/ table, int /*long*/ record, int /*long*/ nObj, int /*long*/ lpData) {
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
		};
		MetaFileEnumProc object = new MetaFileEnumProc();
		/* Avoid compiler warnings */
		if (false) object.metaFileEnumProc(0, 0, 0, 0, 0);
		Callback callback = new Callback(object, "metaFileEnumProc", 5);
		int /*long*/ address = callback.getAddress();
		if (address == 0) SWT.error(SWT.ERROR_NO_MORE_CALLBACKS);
		OS.EnumEnhMetaFile(0, metaFile, address, 0, null);
		OS.DeleteEnhMetaFile(metaFile);
		callback.dispose();

		int /*long*/ newFont = OS.CreateFontIndirectW(emr.elfw.elfLogFont);
		OS.SelectObject(hdc, newFont);
		if (shapeSucceed = shape(hdc, run, chars, buffer,  maxGlyphs, sp)) {
			run.fallbackFont = newFont;
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
			if (mLangFontLink2 != 0) {
				int /*long*/[] hNewFont = new int /*long*/[1];
				int[] dwCodePages = new int[1], cchCodePages = new int[1];
				/* GetStrCodePages() */
				OS.VtblCall(4, mLangFontLink2, chars, chars.length, 0, dwCodePages, cchCodePages);
				/* MapFont() */
				if (OS.VtblCall(10, mLangFontLink2, hdc, dwCodePages[0], chars[0], hNewFont) == OS.S_OK) {
					LOGFONT logFont = OS.IsUnicode ? (LOGFONT)new LOGFONTW () : new LOGFONTA ();
					OS.GetObject(hNewFont[0], LOGFONT.sizeof, logFont);
					/* ReleaseFont() */
					OS.VtblCall(8, mLangFontLink2, hNewFont[0]);
					int /*long*/ mLangFont = OS.CreateFontIndirect(logFont);
					int /*long*/ oldFont = OS.SelectObject(hdc, mLangFont);
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
		if (newFont != run.fallbackFont) OS.DeleteObject(newFont);
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
			lotm = OS.IsUnicode ? (OUTLINETEXTMETRIC)new OUTLINETEXTMETRICW() : new OUTLINETEXTMETRICA();
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
			run.width = metrics.width * Math.max (1, run.glyphCount);
			run.ascent = metrics.ascent;
			run.descent = metrics.descent;
			run.leading = 0;
		} else {
			TEXTMETRIC lptm = null;
			if (lotm != null) {
				lptm = OS.IsUnicode ? (TEXTMETRIC)((OUTLINETEXTMETRICW)lotm).otmTextMetrics : ((OUTLINETEXTMETRICA)lotm).otmTextMetrics;
			} else {
				lptm = OS.IsUnicode ? (TEXTMETRIC)new TEXTMETRICW() : new TEXTMETRICA();
				OS.GetTextMetrics(hdc, lptm);
			}
			run.ascent = lptm.tmAscent;
			run.descent = lptm.tmDescent;
			run.leading = lptm.tmInternalLeading;
		}
		if (lotm != null) {
			run.underlinePos = lotm.otmsUnderscorePosition;
			run.underlineThickness = Math.max(1, lotm.otmsUnderscoreSize);
			run.strikeoutPos = lotm.otmsStrikeoutPosition;
			run.strikeoutThickness = Math.max(1, lotm.otmsStrikeoutSize);
		} else {
			run.underlinePos = 1;
			run.underlineThickness = 1;
			run.strikeoutPos = run.ascent / 2;
			run.strikeoutThickness = 1;
		}
		run.ascent += style.rise;
		run.descent -= style.rise;
	} else {
		TEXTMETRIC lptm = OS.IsUnicode ? (TEXTMETRIC)new TEXTMETRICW() : new TEXTMETRICA();
		OS.GetTextMetrics(hdc, lptm);
		run.ascent = lptm.tmAscent;
		run.descent = lptm.tmDescent;
		run.leading = lptm.tmInternalLeading;
	}
}

int validadeOffset(int offset, int step) {
	offset += step;
	if (segments != null && segments.length > 2) {
		for (int i = 0; i < segments.length; i++) {
			if (translateOffset(segments[i]) - 1 == offset) {
				offset += step;
				break;
			}
		}
	}	
	return offset;
}

/**
 * Returns a string containing a concise, human-readable
 * description of the receiver.
 *
 * @return a string representation of the receiver
 */
public String toString () {
	if (isDisposed()) return "TextLayout {*DISPOSED*}";
	return "TextLayout {}";
}

int translateOffset(int offset) {
	if (segments == null) return offset;
	int nSegments = segments.length;
	if (nSegments <= 1) return offset;
	int length = text.length();
	if (length == 0) return offset;
	if (nSegments == 2) {
		if (segments[0] == 0 && segments[1] == length) return offset;
	}
	for (int i = 0; i < nSegments && offset - i >= segments[i]; i++) {
		offset++;
	}	
	return offset;
}

int untranslateOffset(int offset) {
	if (segments == null) return offset;
	int nSegments = segments.length;
	if (nSegments <= 1) return offset;
	int length = text.length();
	if (length == 0) return offset;
	if (nSegments == 2) {
		if (segments[0] == 0 && segments[1] == length) return offset;
	}
	for (int i = 0; i < nSegments && offset > segments[i]; i++) {
		offset--;
	}
	return offset;
}
}
