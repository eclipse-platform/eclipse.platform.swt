/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
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
import org.eclipse.swt.internal.win32.*;
import org.eclipse.swt.*;

/**
 * <code>TextLayout</code> is a graphic object that represents
 * styled text.
 *<p>
 * Instances of this class provide support for drawing, cursor
 * navigation, hit testing, text wrapping, alignment, tab expansion
 * line breaking, etc.  These are aspects required for rendering internationalized text.
 * </p>
 * 
 * <p>
 * Application code must explicitly invoke the <code>TextLayout#dispose()</code> 
 * method to release the operating system resources managed by each instance
 * when those instances are no longer required.
 * </p>
 * 
 *  @since 3.0
 */
public final class TextLayout extends Resource {
	Font font;
	String text, segmentsText;
	int lineSpacing;
	int ascent, descent;
	int alignment;
	int wrapWidth;
	int orientation;
	int[] tabs;
	int[] segments;
	StyleItem[] styles;

	StyleItem[] allRuns;
	StyleItem[][] runs;
	int[] lineOffset, lineY, lineWidth;
	int mLangFontLink2;
	
	static final char LTR_MARK = '\u200E', RTL_MARK = '\u200F';	
	static final int SCRIPT_VISATTR_SIZEOF = 2;
	static final int GOFFSET_SIZEOF = 8;
	static final byte[] CLSID_CMultiLanguage = new byte[16];
	static final byte[] IID_IMLangFontLink2 = new byte[16];
	static {
		OS.IIDFromString("{275c23e2-3747-11d0-9fea-00aa003f8646}\0".toCharArray(), CLSID_CMultiLanguage);
		OS.IIDFromString("{DCCFC162-2B38-11d2-B7EC-00C04F8F5D9A}\0".toCharArray(), IID_IMLangFontLink2);
	}
	
	class StyleItem {
		TextStyle style;
		int start, length;
		boolean lineBreak, softBreak, tab;	
		
		/*Script cache and analysis */
		SCRIPT_ANALYSIS analysis;
		int psc = 0;
		
		/*Shape info (malloc when the run is shaped) */
		int glyphs;
		int glyphCount;
		int clusters;
		int visAttrs;
		
		/*Place info (malloc when the run is placed) */
		int advances;
		int goffsets;
		int width;
		int ascent;
		int descent;
		int leading;
		int x;

		/* ScriptBreak */
		int psla;

		int fallbackFont;
	
	void free() {
		int hHeap = OS.GetProcessHeap();
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
		if (psla != 0) {
			OS.HeapFree(hHeap, 0, psla);
			psla = 0;
		}
		if (fallbackFont != 0) {
			if (mLangFontLink2 != 0) {
				/* ReleaseFont() */
				OS.VtblCall(8, mLangFontLink2, fallbackFont);
			}
			fallbackFont = 0;
		}
		width = ascent = descent = x = 0;
		lineBreak = softBreak = false;		
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
	if (device == null) device = Device.getDevice();
	if (device == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	this.device = device;
	wrapWidth = ascent = descent = -1;
	lineSpacing = 0;
	orientation = SWT.LEFT_TO_RIGHT;
	styles = new StyleItem[2];
	styles[0] = new StyleItem();
	styles[1] = new StyleItem();
	text = ""; //$NON-NLS-1$
	int[] ppv = new int[1];
	OS.OleInitialize(0);
	if (OS.CoCreateInstance(CLSID_CMultiLanguage, 0, OS.CLSCTX_INPROC_SERVER, IID_IMLangFontLink2, ppv) == OS.S_OK) {
		mLangFontLink2 = ppv[0];
	}
	if (device.tracking) device.new_Object(this);
}

void breakRun(StyleItem run) {
	if (run.psla != 0) return;
	char[] chars = new char[run.length];
	segmentsText.getChars(run.start, run.start + run.length, chars, 0);
	int hHeap = OS.GetProcessHeap();
	run.psla = OS.HeapAlloc(hHeap, OS.HEAP_ZERO_MEMORY, SCRIPT_LOGATTR.sizeof * chars.length); 
	OS.ScriptBreak(chars, chars.length, run.analysis, run.psla);
}

void checkItem (int hDC, StyleItem item) {
	if (item.fallbackFont != 0) {
		/*
		* Feature in Windows. The fallback font returned by the MLang service
		* can be disposed by some other client running in the same thread.
		* For example, disposing a Browser widget internally releases all fonts
		* in the MLang cache. The fix is to use GetObject() to detect if the 
		* font was disposed and reshape the run.
		*/
		LOGFONT logFont = OS.IsUnicode ? (LOGFONT)new LOGFONTW() : new LOGFONTA();
		if (OS.GetObject(item.fallbackFont, LOGFONT.sizeof, logFont) == 0) {
			item.free();
			OS.SelectObject(hDC, getItemFont(item));
			shape(hDC, item);
		}
	}
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
	int hDC = gc != null ? gc.handle : device.internal_new_GC(null);
	int srcHdc = OS.CreateCompatibleDC(hDC);
	allRuns = itemize();
	for (int i=0; i<allRuns.length - 1; i++) {
		StyleItem run = allRuns[i];
		OS.SelectObject(srcHdc, getItemFont(run));
		shape(srcHdc, run);
	}
	SCRIPT_LOGATTR logAttr = new SCRIPT_LOGATTR();
	SCRIPT_PROPERTIES properties = new SCRIPT_PROPERTIES();
	int lineWidth = 0, lineStart = 0, lineCount = 1;
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
						i++;
					}
					break;
				}
			}
		} 
		if (wrapWidth != -1 && lineWidth + run.width > wrapWidth && !run.tab) {
			int start = 0;
			int[] piDx = new int[run.length];
			OS.ScriptGetLogicalWidths(run.analysis, run.length, run.glyphCount, run.advances, run.clusters, run.visAttrs, piDx);
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
				i = firstIndice;
				run = allRuns[i];
				start = Math.max(1, firstStart);
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
				newRun.analysis = run.analysis;
				run.free();
				run.length = start;
				OS.SelectObject(srcHdc, getItemFont(run));
				shape (srcHdc, run);
				OS.SelectObject(srcHdc, getItemFont(newRun));
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
			lineWidth = 0;
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
			StyleItem lastRun = runs[line][lineRunCount - 1];
			runs[line] = reorder(runs[line]);
			this.lineWidth[line] = lineWidth;
			lineWidth = 0;
			if (wrapWidth != -1) {
				switch (alignment) {
					case SWT.CENTER: lineWidth = (wrapWidth - this.lineWidth[line]) / 2; break;
					case SWT.RIGHT: lineWidth = wrapWidth - this.lineWidth[line]; break;
				}
			}
			for (int j = 0; j < runs[line].length; j++) {
				runs[line][j].x = lineWidth;
				lineWidth += runs[line][j].width;
			}
			line++;
			lineY[line] = lineY[line - 1] + ascent + descent + lineSpacing;
			lineOffset[line] = lastRun.start + lastRun.length;
			lineRunCount = lineWidth = 0;
			ascent = Math.max(0, this.ascent);
			descent = Math.max(0, this.descent);
		}
	}
	if (srcHdc != 0) OS.DeleteDC(srcHdc);
	if (gc == null) device.internal_dispose_GC(hDC, null);	
}

/**
 * Disposes of the operating system resources associated with
 * the text layout. Applications must dispose of all allocated text layouts.
 */
public void dispose () {
	if (device == null) return;
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
	if (device.tracking) device.dispose_Object(this);
	device = null;
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
	checkLayout();
	computeRuns(gc);
	if (gc == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (gc.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);	
	if (selectionForeground != null && selectionForeground.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	if (selectionBackground != null && selectionBackground.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	int length = text.length();
	if (length == 0) return;
	int hdc = gc.handle;
	boolean hasSelection = selectionStart <= selectionEnd && selectionStart != -1 && selectionEnd != -1;
	if (hasSelection) {
		selectionStart = Math.min(Math.max(0, selectionStart), length - 1);
		selectionEnd = Math.min(Math.max(0, selectionEnd), length - 1);
		if (selectionForeground == null) selectionForeground = device.getSystemColor(SWT.COLOR_LIST_SELECTION_TEXT);
		if (selectionBackground == null) selectionBackground = device.getSystemColor(SWT.COLOR_LIST_SELECTION);
		selectionStart = translateOffset(selectionStart);
		selectionEnd = translateOffset(selectionEnd);
	}
	int foreground = OS.GetTextColor(hdc);
	int state = OS.SaveDC(hdc);
	RECT rect = new RECT();
	int selBrush = 0, selPen = 0;
	if (hasSelection) {
		selBrush = OS.CreateSolidBrush(selectionBackground.handle);
		selPen = OS.CreatePen(OS.BS_SOLID, 1, selectionForeground.handle);
	}
	int rop2 = 0;
	if (OS.IsWinCE) {
		rop2 = OS.SetROP2(hdc, OS.R2_COPYPEN);
		OS.SetROP2(hdc, rop2);
	} else {
		rop2 = OS.GetROP2(hdc);
	}
	int dwRop = rop2 == OS.R2_XORPEN ? OS.PATINVERT : OS.PATCOPY;
	OS.SetBkMode(hdc, OS.TRANSPARENT);
	Rectangle clip = gc.getClipping();
	for (int line=0; line<runs.length; line++) {
		int drawX = x, drawY = y + lineY[line];
		if (wrapWidth != -1) {
			switch (alignment) {
				case SWT.CENTER: drawX += (wrapWidth - lineWidth[line]) / 2; break;
				case SWT.RIGHT: drawX += wrapWidth - lineWidth[line]; break;
			}
		}
		if (drawX > clip.x + clip.width) continue;
		if (drawX + lineWidth[line] < clip.x) continue;
		StyleItem[] lineRuns = runs[line];
		int baseline = Math.max(0, this.ascent);
		for (int i = 0; i < lineRuns.length; i++) {
			baseline = Math.max(baseline, lineRuns[i].ascent);
		}
		int lineHeight = lineY[line+1] - lineY[line];
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
						OS.SelectObject(hdc, selBrush);
						OS.PatBlt(hdc, drawX, drawY, run.width, lineHeight, dwRop);
					} else {
						if (run.style != null && run.style.background != null) {
							int bg = run.style.background.handle;
							int drawRunY = drawY + (baseline - run.ascent);
							int hBrush = OS.CreateSolidBrush (bg);
							int oldBrush = OS.SelectObject(hdc, hBrush);
							OS.PatBlt(hdc, drawX, drawRunY, run.width, run.ascent + run.descent, dwRop);
							OS.SelectObject(hdc, oldBrush);
							OS.DeleteObject(hBrush);
						}
						boolean partialSelection = hasSelection && !(selectionStart > end || run.start > selectionEnd);
						if (partialSelection) {
							OS.SelectObject(hdc, selBrush);
							int selStart = Math.max(selectionStart, run.start) - run.start;
							int selEnd = Math.min(selectionEnd, end) - run.start;
							int cChars = run.length;
							int gGlyphs = run.glyphCount;
							int[] piX = new int[1];
							OS.ScriptCPtoX(selStart, false, cChars, gGlyphs, run.clusters, run.visAttrs, run.advances, run.analysis, piX);
							int runX = (orientation & SWT.RIGHT_TO_LEFT) != 0 ? run.width - piX[0] : piX[0];
							rect.left = drawX + runX;
							rect.top = drawY;
							OS.ScriptCPtoX(selEnd, true, cChars, gGlyphs, run.clusters, run.visAttrs, run.advances, run.analysis, piX);
							runX = (orientation & SWT.RIGHT_TO_LEFT) != 0 ? run.width - piX[0] : piX[0];
							rect.right = drawX + runX;
							rect.bottom = drawY + lineHeight;
							OS.PatBlt(hdc, rect.left, rect.top, rect.right - rect.left, rect.bottom - rect.top, dwRop);
						}
					}
				}
			}
			drawX += run.width;
		}
		drawX = alignmentX;
		for (int i = 0; i < lineRuns.length; i++) {
			StyleItem run = lineRuns[i];
			if (run.length == 0) continue;
			if (drawX > clip.x + clip.width) break;
			if (drawX + run.width >= clip.x) {
				if (!run.tab && (!run.lineBreak || run.softBreak)) {
					int end = run.start + run.length - 1;
					int fg = foreground;
					boolean fullSelection = hasSelection && selectionStart <= run.start && selectionEnd >= end;
					if (fullSelection) {
						fg = selectionForeground.handle;
					} else {
						if (run.style != null && run.style.foreground != null) fg = run.style.foreground.handle;
					}
					OS.SetTextColor(hdc, fg);
					checkItem(hdc, run);
					OS.SelectObject(hdc, getItemFont(run));
					int drawRunY = drawY + (baseline - run.ascent);
					OS.ScriptTextOut(hdc, run.psc, drawX, drawRunY, 0, null, run.analysis , 0, 0, run.glyphs, run.glyphCount, run.advances, null, run.goffsets);
					if ((run.style != null) && (run.style.underline || run.style.strikeout)) {
						int newPen = hasSelection && fg == selectionForeground.handle ? selPen : OS.CreatePen(OS.BS_SOLID, 1, fg);
						int oldPen = OS.SelectObject(hdc, newPen);
						if (run.style.underline) {
							int underlineY = drawY + baseline + 1;
							OS.MoveToEx(hdc, drawX, underlineY, 0);
							OS.LineTo(hdc, drawX + run.width, underlineY);
						}
						if (run.style.strikeout) {
							int strikeoutY = drawRunY + run.leading + run.ascent / 2;
							OS.MoveToEx(hdc, drawX, strikeoutY, 0);
							OS.LineTo(hdc, drawX + run.width, strikeoutY);	
						}
						OS.SelectObject(hdc, oldPen);
						if (!hasSelection || fg != selectionForeground.handle) OS.DeleteObject(newPen);
					}
					boolean partialSelection = hasSelection && !(selectionStart > end || run.start > selectionEnd);
					if (!fullSelection && partialSelection && fg != selectionForeground.handle) {
						OS.SetTextColor(hdc, selectionForeground.handle);
						int selStart = Math.max(selectionStart, run.start) - run.start;
						int selEnd = Math.min(selectionEnd, end) - run.start;
						int cChars = run.length;
						int gGlyphs = run.glyphCount;
						int[] piX = new int[1];
						OS.ScriptCPtoX(selStart, false, cChars, gGlyphs, run.clusters, run.visAttrs, run.advances, run.analysis, piX);
						int runX = (orientation & SWT.RIGHT_TO_LEFT) != 0 ? run.width - piX[0] : piX[0];
						rect.left = drawX + runX;
						rect.top = drawY;
						OS.ScriptCPtoX(selEnd, true, cChars, gGlyphs, run.clusters, run.visAttrs, run.advances, run.analysis, piX);
						runX = (orientation & SWT.RIGHT_TO_LEFT) != 0 ? run.width - piX[0] : piX[0];
						rect.right = drawX + runX;
						rect.bottom = drawY + lineHeight;
						OS.ScriptTextOut(hdc, run.psc, drawX, drawRunY, OS.ETO_CLIPPED, rect, run.analysis , 0, 0, run.glyphs, run.glyphCount, run.advances, null, run.goffsets);
						if ((run.style != null) && (run.style.underline || run.style.strikeout)) {							
							int oldPen = OS.SelectObject(hdc, selPen);
							if (run.style.underline) {
								int underlineY = drawY + baseline + 1;
								OS.MoveToEx(hdc, rect.left, underlineY, 0);
								OS.LineTo(hdc, rect.right, underlineY);
							}
							if (run.style.strikeout) {
								int strikeoutY = drawRunY + run.leading + run.ascent / 2;
								OS.MoveToEx(hdc, rect.left, strikeoutY, 0);
								OS.LineTo(hdc, rect.right, strikeoutY);	
							}
							OS.SelectObject(hdc, oldPen);
						}
					}
				}
			}
			drawX += run.width;
		}
	}
	OS.RestoreDC(hdc, state);
	if (selBrush != 0) OS.DeleteObject (selBrush);
	if (selPen != 0) OS.DeleteObject (selPen);
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
 * Returns the bounds of the receiver.
 * 
 * @return the bounds of the receiver
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public Rectangle getBounds () {
	checkLayout();
	computeRuns(null);
	int width = 0;
	if (wrapWidth != -1) {
		width = wrapWidth;
	} else {
		for (int line=0; line<runs.length; line++) {
			width = Math.max(width, lineWidth[line]);
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
	int lineIndex = 0;
	boolean isRTL = (orientation & SWT.RIGHT_TO_LEFT) != 0;
	for (int i = 0; i < allRuns.length - 1; i++) {
		StyleItem run = allRuns[i];
		int runEnd = run.start + run.length;
		if (run.lineBreak) lineIndex++;
		if (runEnd <= start) continue;
		if (run.start > end) break;
		int runLead = run.x;
		int runTrail = run.x + run.width;
		if (run.start <= start && start < runEnd) {
			int cx = 0;
			if (!run.tab) {
				int[] piX = new int[1];
				OS.ScriptCPtoX(start - run.start, false, run.length, run.glyphCount, run.clusters, run.visAttrs, run.advances, run.analysis, piX);
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
			if (!run.tab) {
				int[] piX = new int[1];
				OS.ScriptCPtoX(end - run.start, true, run.length, run.glyphCount, run.clusters, run.visAttrs, run.advances, run.analysis, piX);
				cx = isRTL ? run.width - piX[0] : piX[0];
			}
			if (run.analysis.fRTL ^ isRTL) {
				runLead = run.x + cx;
			} else {
				runTrail = run.x + cx;
			}
		}
		left = Math.min(left, runLead);
		right = Math.max(right, runTrail);
		top = Math.min(top, lineY[run.lineBreak ? lineIndex - 1 : lineIndex]);
		bottom = Math.max(bottom, lineY[run.lineBreak ? lineIndex : lineIndex + 1]);
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

int getItemFont (StyleItem item) {
	if (item.fallbackFont != 0) return item.fallbackFont;
	if (item.style != null && item.style.font != null) {
		return item.style.font.handle;
	}
	if (this.font != null) {
		return this.font.handle;
	}
	return device.systemFont;
}

/**
 * Returns the embedding level for the specified character offset. The
 * embedding level is usually used to determine the directionality of a
 * character in bidirectional text.
 * 
 * @param offset the charecter offset
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
	int x = 0, y = lineY[lineIndex];	
	int width = lineWidth[lineIndex], height = lineY[lineIndex + 1] - y; 
	if (wrapWidth != -1) {
		switch (alignment) {
			case SWT.CENTER: x = (wrapWidth - width) / 2; break;
			case SWT.RIGHT: x = wrapWidth - width; break;
		}
	}
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
	int hDC = device.internal_new_GC(null);
	int srcHdc = OS.CreateCompatibleDC(hDC);
	TEXTMETRIC lptm = OS.IsUnicode ? (TEXTMETRIC)new TEXTMETRICW() : new TEXTMETRICA();
	if (text.length() == 0) {
		OS.SelectObject(srcHdc, font != null ? font.handle : device.systemFont);
		OS.GetTextMetrics(srcHdc, lptm);
		lptm.tmAscent = Math.max(lptm.tmAscent, this.ascent);
		lptm.tmDescent = Math.max(lptm.tmDescent, this.descent);		
	} else {
		int ascent = this.ascent, descent = this.descent, leading = 0, aveCharWidth = 0, height = 0;
		StyleItem[] lineRuns = runs[lineIndex];
		for (int i = 0; i<lineRuns.length; i++) {
			StyleItem run = lineRuns[i];
			checkItem(srcHdc, run);
			OS.SelectObject(srcHdc, getItemFont(run));
			OS.GetTextMetrics(srcHdc, lptm);
			ascent = Math.max(ascent, lptm.tmAscent);
			descent = Math.max(descent, lptm.tmDescent);
			height = Math.max(height, lptm.tmHeight);
			leading = Math.max(leading, lptm.tmInternalLeading);
			aveCharWidth += lptm.tmAveCharWidth;
		}
		lptm.tmAscent = ascent;
		lptm.tmDescent = descent;
		lptm.tmHeight = height;
		lptm.tmInternalLeading = leading;
		lptm.tmAveCharWidth = aveCharWidth / lineRuns.length;
	}
	if (srcHdc != 0) OS.DeleteDC(srcHdc);
	device.internal_dispose_GC(hDC, null);
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
	StyleItem[] lineRuns = runs[line];
	Point result = null;
	if (offset == length) {
		result = new Point(lineWidth[line], lineY[line]);
	} else {
		int width = 0;
		for (int i=0; i<lineRuns.length; i++) {
			StyleItem run = lineRuns[i];
			int end = run.start + run.length;
			if (run.start <= offset && offset < end) {
				if (run.tab) {
					if (trailing || (offset == length)) width += run.width;
					result = new Point(width, lineY[line]);
				} else {
					int runOffset = offset - run.start;
					int cChars = run.length;
					int gGlyphs = run.glyphCount;
					int[] piX = new int[1];
					OS.ScriptCPtoX(runOffset, trailing, cChars, gGlyphs, run.clusters, run.visAttrs, run.advances, run.analysis, piX);
					if ((orientation & SWT.RIGHT_TO_LEFT) != 0) {
						result = new Point(width + (run.width - piX[0]), lineY[line]);
					} else {
						result = new Point(width + piX[0], lineY[line]);
					}
				}
				break;
			}
			width += run.width;
		}
	}
	if (result == null) result = new Point(0, 0);
	if (wrapWidth != -1) {
		switch (alignment) {
			case SWT.CENTER: result.x += (wrapWidth - lineWidth[line]) / 2; break;
			case SWT.RIGHT: result.x += wrapWidth - lineWidth[line]; break;
		}
	}
	return result;
}

/**
 * Returns the next offset for the specified offset and movement
 * type.  The movement is one of <code>SWT.MOVEMENT_CHAR</code>, 
 * <code>SWT.MOVEMENT_CLUSTER</code> or <code>SWT.MOVEMENT_WORD</code>.
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
	if (wrapWidth != -1) {
		switch (alignment) {
			case SWT.CENTER: x -= (wrapWidth - lineWidth[line]) / 2; break;
			case SWT.RIGHT: x -= wrapWidth - lineWidth[line]; break;
		}
	}
	StyleItem[] lineRuns = runs[line];
	if (x >= lineWidth[line]) x = lineWidth[line] - 1;
	if (x < 0) x = 0;
	int width = 0;
	for (int i = 0; i < lineRuns.length; i++) {
		StyleItem run = lineRuns[i];
		if (run.lineBreak && !run.softBreak) return untranslateOffset(run.start);
		if (width + run.width > x) {
			if (run.tab) {
				if (trailing != null) trailing[0] = x < (width + run.width / 2) ? 0 : 1;
				return untranslateOffset(run.start);
			}
			int cChars = run.length;
			int cGlyphs = run.glyphCount;
			int xRun = x - width;
			int[] piCP = new int[1];
			int[] piTrailing = new int[1];
			if ((orientation & SWT.RIGHT_TO_LEFT) != 0) {
				xRun = run.width - xRun;
			}
			OS.ScriptXtoCP(xRun, cChars, cGlyphs, run.clusters, run.visAttrs, run.advances, run.analysis, piCP, piTrailing);
			if (trailing != null) trailing[0] = piTrailing[0];
			return untranslateOffset(run.start + piCP[0]);
		}
		width += run.width;
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
 * <code>SWT.MOVEMENT_CLUSTER</code> or <code>SWT.MOVEMENT_WORD</code>.
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
	for (int i=1; i<styles.length; i++) {
		if (styles[i].start > offset) {
			return styles[i - 1].style;
		}
	}
	return null;
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
	}
	
	int hHeap = OS.GetProcessHeap();
	int pItems = OS.HeapAlloc(hHeap, OS.HEAP_ZERO_MEMORY, MAX_ITEM * SCRIPT_ITEM.sizeof);
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
StyleItem[] merge (int items, int itemCount) {
	int count = 0, start = 0, end = segmentsText.length(), itemIndex = 0, styleIndex = 0;
	StyleItem[] runs = new StyleItem[itemCount + styles.length];
	SCRIPT_ITEM scriptItem = new SCRIPT_ITEM();
	while (start < end) {
		StyleItem item = new StyleItem();
		item.start = start;
		item.style = styles[styleIndex].style;
		runs[count++] = item;
		OS.MoveMemory(scriptItem, items + itemIndex * SCRIPT_ITEM.sizeof, SCRIPT_ITEM.sizeof);
		item.analysis = scriptItem.a;
		scriptItem.a = new SCRIPT_ANALYSIS();
		OS.MoveMemory(scriptItem, items + (itemIndex + 1) * SCRIPT_ITEM.sizeof, SCRIPT_ITEM.sizeof);
		int itemLimit = scriptItem.iCharPos;
		int styleLimit = translateOffset(styles[styleIndex + 1].start);
		if (styleLimit <= itemLimit) {
			styleIndex++;
			start = styleLimit;
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
StyleItem[] reorder (StyleItem[] runs) {
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
		for (int i = 0; i < (length - 1) / 2 ; i++) {
			StyleItem tmp = result[i];
			result[i] = result[length - i - 2];
			result[length - i - 2] = tmp;
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
 *</p>
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
	if (this.font == font) return;
	if (font != null && font.equals(this.font)) return;
	freeRuns();
	this.font = font;
}

/**
 * Sets the orientation of the receiver, which must be one
 * of <code>SWT.LEFT_TO_RIGHT</code> or <code>SWT.RIGHT_TO_LEFT</code>.
 * <p>
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
 * Each text segment is determined by two consecutive offsets in the 
 * <code>segments</code> arrays. The first element of the array should 
 * always be zero and the last one should always be equals to length of
 * the text.
 * <p>
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
	if (this.segments != null && segments !=null) {
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
	int high = styles.length;
	while (high - low > 1) {
		int index = (high + low) / 2;
		if (start <= styles[index].start) {
			high = index;
		} else {
			low = index;
		}
	}
	if (0 <= high && high < styles.length) {
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
	int count = 0, i;
	StyleItem[] newStyles = new StyleItem[styles.length + 2];
	for (i = 0; i < styles.length; i++) {
		StyleItem item = styles[i];
		if (item.start >= start) break;
		newStyles[count++] = item;
	}
	StyleItem newItem = new StyleItem();
	newItem.start = start;
	newItem.style = style;
	newStyles[count++] = newItem;
	if (styles[i].start > end) {
		newItem = new StyleItem();
		newItem.start = end + 1;
		newItem.style = styles[i -1].style;
		newStyles[count++] = newItem;
	} else {
		for (; i<styles.length; i++) {
			StyleItem item = styles[i];
			if (item.start > end) break;
		}
		if (end != styles[i].start - 1) {
			i--;
			styles[i].start = end + 1;
		}
	}
	for (; i<styles.length; i++) {
		StyleItem item = styles[i];
		if (item.start > end) newStyles[count++] = item;
	}
	if (newStyles.length != count) {
		styles = new StyleItem[count];
		System.arraycopy(newStyles, 0, styles, 0, count);
	} else {
		styles = newStyles;
	}
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

boolean shape (int hdc, StyleItem run, char[] chars, int[] glyphCount, int maxGlyphs) {
	int hr = OS.ScriptShape(hdc, run.psc, chars, chars.length, maxGlyphs, run.analysis, run.glyphs, run.clusters, run.visAttrs, glyphCount);
	run.glyphCount = glyphCount[0];
	if (hr != OS.USP_E_SCRIPT_NOT_IN_FONT) {
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
		OS.MoveMemory(run.psc, glyphCount, 4);
	}
	run.glyphCount = 0;
	return false;
}

/* 
 * Generate glyphs for one Run.
 */
void shape (final int hdc, final StyleItem run) {
	int[] buffer = new int[1];
	char[] chars = new char[run.length];
	segmentsText.getChars(run.start, run.start + run.length, chars, 0);
	int maxGlyphs = (chars.length * 3 / 2) + 16;
	int hHeap = OS.GetProcessHeap();
	run.glyphs = OS.HeapAlloc(hHeap, OS.HEAP_ZERO_MEMORY, maxGlyphs * 2);
	run.clusters = OS.HeapAlloc(hHeap, OS.HEAP_ZERO_MEMORY, maxGlyphs * 2);
	run.visAttrs = OS.HeapAlloc(hHeap, OS.HEAP_ZERO_MEMORY, maxGlyphs * SCRIPT_VISATTR_SIZEOF);
	run.psc = OS.HeapAlloc(hHeap, OS.HEAP_ZERO_MEMORY, 4);
	if (!shape(hdc, run, chars, buffer,  maxGlyphs)) {
		if (mLangFontLink2 != 0) {
			int[] dwCodePages = new int[1];
			int[] cchCodePages = new int[1];
			/* GetStrCodePages() */
			OS.VtblCall(4, mLangFontLink2, chars, chars.length, 0, dwCodePages, cchCodePages);
			int[] hNewFont = new int[1];
			/* MapFont() */
			if (OS.VtblCall(10, mLangFontLink2, hdc, dwCodePages[0], chars[0], hNewFont) == OS.S_OK) {
				int hFont = OS.SelectObject(hdc, hNewFont[0]);
				if (shape(hdc, run, chars, buffer, maxGlyphs)) {
					run.fallbackFont = hNewFont[0];
				} else {
					/* ReleaseFont() */
					OS.VtblCall(8, mLangFontLink2, hNewFont[0]);
					OS.SelectObject(hdc, hFont);
					OS.ScriptShape(hdc, run.psc, chars, chars.length, maxGlyphs, run.analysis, run.glyphs, run.clusters, run.visAttrs, buffer);
					run.glyphCount = buffer[0];
				}
			}
		}
	}
	int[] abc = new int[3];
	run.advances = OS.HeapAlloc(hHeap, OS.HEAP_ZERO_MEMORY, run.glyphCount * 4);
	run.goffsets = OS.HeapAlloc(hHeap, OS.HEAP_ZERO_MEMORY, run.glyphCount * GOFFSET_SIZEOF);
	OS.ScriptPlace(hdc, run.psc, run.glyphs, run.glyphCount, run.visAttrs, run.analysis, run.advances, run.goffsets, abc);
	run.width = abc[0] + abc[1] + abc[2];
	TEXTMETRIC lptm = OS.IsUnicode ? (TEXTMETRIC)new TEXTMETRICW() : new TEXTMETRICA();
	OS.GetTextMetrics(hdc, lptm);
	run.ascent = lptm.tmAscent;
	run.descent = lptm.tmDescent;
	run.leading = lptm.tmInternalLeading;
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
