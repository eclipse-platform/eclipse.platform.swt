/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.graphics;

import org.eclipse.swt.internal.win32.*;
import org.eclipse.swt.*;

/**
 * <code>TextLayout</code> is a graphic object to work with text.
 * 
 * <p>
 * Application code must explicitly invoke the <code>TextLayout#dispose()</code> 
 * method to release the operating system resources managed by each instance
 * when those instances are no longer required.
 * </p>
 * 
 *  @since 3.0
 */

public class TextLayout {

	final public static int MOVEMENT_CHAR = 0;
	final public static int MOVEMENT_CLUSTER = 1;
	final public static int MOVEMENT_WORD = 2;
	
	Device device;
	Font font;
	String text;
	int lineSpacing;
	int alignment;
	int wrapWidth;
	int orientation;
	int[] tabs;
	StyleItem[] styles;

	StyleItem[] allRuns;
	StyleItem[][] runs;
	int[] lineOffset, lineY, lineWidth;
	
	static final int SCRIPT_VISATTR_SIZEOF = 2;
	static final int GOFFSET_SIZEOF = 8;
	
	static class StyleItem {
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
		int height;

		/* ScriptBreak */
		int psla;
	
	void free() {
		int hHeap = OS.GetProcessHeap();
		/* This code is intentionaly commented. */
//		if (psc != 0) {
//			OS.ScriptFreeCache (psc);
//			OS.HeapFree(hHeap, 0, psc);
//			psc = 0;
//		}
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
		width = 0;
		height = 0;
		lineBreak = softBreak = false;		
	}
	}

/**
 * Prevents uninitialized instances from being created outside the package.
 */
TextLayout() {
}

/**	 
 * Constructs a new instance of this class on the given device
 * <p>
 * You must dispose the text layout when it is no longer required. 
 * </p>
 * @param device 
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if device is null and there is no current device</li>
 * </ul>
 */
public TextLayout (Device device) {
	if (device == null) device = Device.getDevice();
	if (device == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	this.device = device;
	wrapWidth = -1;
	lineSpacing = 0;
	orientation = SWT.LEFT_TO_RIGHT;
	styles = new StyleItem[2];
	styles[0] = new StyleItem();
	styles[1] = new StyleItem();
	text = "";
	if (device.tracking) device.new_Object(this);
}

void breakRun(StyleItem run) {
	if (run.psla != 0) return;
	char[] chars = new char[run.length];
	text.getChars(run.start, run.start + run.length, chars, 0);
	int hHeap = OS.GetProcessHeap();
	run.psla = OS.HeapAlloc(hHeap, OS.HEAP_ZERO_MEMORY, SCRIPT_LOGATTR.sizeof * chars.length); 
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
	int hDC = gc != null ? gc.handle : device.internal_new_GC(null);
	int srcHdc = OS.CreateCompatibleDC(hDC);
	allRuns = itemize();
	for (int i=0; i<allRuns.length - 1; i++) {
		StyleItem run = allRuns[i];
		Font font = null;
		if (run.style != null) font = run.style.font;
		if (font == null) font = this.font;
		if (font == null) font = device.getSystemFont();
		OS.SelectObject(srcHdc, font.handle);
		shape(srcHdc, run);
	}
	SCRIPT_LOGATTR logAttr = new SCRIPT_LOGATTR();
	int lineWidth = 0, lineStart = 0, lineCount = 1;
	for (int i=0; i<allRuns.length - 1; i++) {
		StyleItem run = allRuns[i];
		if (run.length == 1) {
			char ch = text.charAt(run.start);
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
						while (tabX <= lineWidth) tabX += lastTabWidth;
						run.width = tabX - lineWidth;
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
					if (next.length != 0 && text.charAt(next.start) == '\n') {
						run.length += 1;
						next.free();
						i++;
					}
					break;
				}
			}
		} else {
			if (wrapWidth != -1 && lineWidth + run.width > wrapWidth) {
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
					if (start >= 0 || i == lineStart) break;
					run = allRuns[--i];
					start = run.length - 1;
				}
				if (start == 0 && i != lineStart) {
					run = allRuns[--i];
				}
				if (start <= 0 && i == lineStart) {
					i = firstIndice;
					run = allRuns[i];
					start = Math.max(1, firstStart);
					breakRun(run);
					while (start < run.length) {
						OS.MoveMemory(logAttr, run.psla + (start * SCRIPT_LOGATTR.sizeof), SCRIPT_LOGATTR.sizeof); 
						if (!logAttr.fWhiteSpace) break;
						start++;
					}
				}
				if (0 < start && start < run.length) {
					StyleItem newRun = new StyleItem();
					newRun.start = run.start + start;
					newRun.length = run.length - start;
					newRun.style = run.style;
					newRun.analysis = run.analysis;
					run.free();
					run.length = start;
					Font font = null;
					if (run.style != null) font = run.style.font;
					if (font == null) font = this.font;
					if (font == null) font = device.getSystemFont();
					OS.SelectObject(srcHdc, font.handle);
					shape (srcHdc, run);
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
		}
		lineWidth += run.width;
		if (run.lineBreak) {
			lineStart = i + 1;
			lineWidth = 0;
			lineCount++;
		}
	}
	if (srcHdc != 0) OS.DeleteDC(srcHdc);
	if (gc == null) device.internal_dispose_GC(hDC, null);
	lineWidth = 0;
	runs = new StyleItem[lineCount][];
	lineOffset = new int[lineCount + 1];
	lineY = new int[lineCount + 1];
	this.lineWidth = new int[lineCount];
	int lineHeight = 0, lineRunCount = 0, line = 0;
	StyleItem[] lineRuns = new StyleItem[allRuns.length];
	for (int i=0; i<allRuns.length; i++) {
		StyleItem run = allRuns[i];
		lineRuns[lineRunCount++] = run;
		lineWidth += run.width;
		lineHeight = Math.max(run.height + lineSpacing, lineHeight);
		if (run.lineBreak || i == allRuns.length - 1) {
			runs[line] = new StyleItem[lineRunCount];
			System.arraycopy(lineRuns, 0, runs[line], 0, lineRunCount);
			StyleItem lastRun = runs[line][lineRunCount - 1];
			runs[line] = reorder(runs[line]);
			this.lineWidth[line] = lineWidth;
			line++;
			lineY[line] = lineY[line - 1] + lineHeight;
			lineOffset[line] = lastRun.start + lastRun.length;
			lineRunCount = lineWidth = lineHeight = 0;
		}
	}
}

/**
 * Disposes of the operating system resources associated with
 * the text layout.
 */
public void dispose () {
	if (device == null) return;
	freeRuns();
	font = null;	
	text = null;
	tabs = null;
	styles = null;
	runs = null;
	lineOffset = null;
	lineY = null;
	lineWidth = null;
	if (device.tracking) device.dispose_Object(this);
	device = null;
}

/**
 * Draws the current text layout
 * 
 * @param gc teh gc to be used to draw
 * @param x the x coordinate of where to draw
 * @param y the y coordinate of where to draw
 */
public void draw (GC gc, int x, int y) {
	draw(gc, x, y, -1, -1, null, null);
}

/**
 * Draws the current text layout with selection at given x and y coordinates using the given gc
 * x and y are inclusive.
 * 
 * The selection offsets are inclusive, out of range values are clamp. If no selection is desire 
 * you use draw() , selectionStart > selectionEnd, selectionStart = selection = -1.
 * 
 * @param gc
 * @param x the x coordinate of where to draw
 * @param y the y coordinate of where to draw
 * @param selectionStart offset where the selections starts, or -1 indicating no selection 
 * @param selectionEnd offset where the selections ends, or -1 indicating no selection 
 * @param selectionForeground selection foreground, or NULL to use system default 
 * @param selectionBackground selection background, or NULL to use system default 
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
	}
	int foreground = OS.GetTextColor(hdc);
	int state = OS.SaveDC(hdc);
	RECT rect = new RECT();
	TEXTMETRIC lptm = OS.IsUnicode ? (TEXTMETRIC)new TEXTMETRICW() : new TEXTMETRICA();
	int selBrush = 0;
	if (hasSelection) selBrush = OS.CreateSolidBrush (selectionBackground.handle);
	int rop2 = 0;
	if (OS.IsWinCE) {
		rop2 = OS.SetROP2(hdc, OS.R2_COPYPEN);
		OS.SetROP2(hdc, rop2);
	} else {
		rop2 = OS.GetROP2(hdc);
	}
	int dwRop = rop2 == OS.R2_XORPEN ? OS.PATINVERT : OS.PATCOPY;
	OS.SetBkMode(hdc, OS.TRANSPARENT);
	for (int line=0; line<runs.length; line++) {
		int drawX = x, drawY = y + lineY[line];
		StyleItem[] lineRuns = runs[line];
		FontMetrics metrics = getLineMetrics(line);
		int baseline = metrics.getAscent() + metrics.getLeading();
		int lineHeight = metrics.getHeight();
		if (wrapWidth != -1) {
			switch (alignment) {
				case SWT.CENTER: drawX += (wrapWidth - lineWidth[line]) / 2; break;
				case SWT.RIGHT: drawX += wrapWidth - lineWidth[line]; break;
			}
		}
		int alignmentX = drawX;
		for (int i = 0; i < lineRuns.length; i++) {
			StyleItem run = lineRuns[i];
			if (run.length == 0) continue;
			if (!run.lineBreak || run.softBreak) {
				int end = run.start + run.length - 1;
				boolean fullSelection = hasSelection && selectionStart <= run.start && selectionEnd >= end;
				if (fullSelection) {
					OS.SelectObject(hdc, selBrush);
					OS.PatBlt(hdc, drawX, drawY, run.width, lineHeight, dwRop);
				} else {
					if (run.style != null && run.style.background != null) {
						int bg = run.style.background.handle;
						Font font = null;
						if (run.style != null) font = run.style.font;
						if (font == null) font = this.font;
						if (font == null) font = device.getSystemFont();
						OS.SelectObject(hdc, font.handle);
						OS.GetTextMetrics(hdc, lptm);
						int drawRunY = drawY + (baseline - lptm.tmAscent);
						int hBrush = OS.CreateSolidBrush (bg);
						int oldBrush = OS.SelectObject(hdc, hBrush);
						OS.PatBlt(hdc, drawX, drawRunY, run.width, run.height, dwRop);
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
			drawX += run.width;
		}
		drawX = alignmentX;
		for (int i = 0; i < lineRuns.length; i++) {
			StyleItem run = lineRuns[i];
			if (run.length == 0) continue;
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
				Font font = null;
				if (run.style != null) font = run.style.font;
				if (font == null) font = this.font;
				if (font == null) font = device.getSystemFont();
				OS.SelectObject(hdc, font.handle);
				OS.GetTextMetrics(hdc, lptm);
				int drawRunY = drawY + (baseline - lptm.tmAscent);
				OS.ScriptTextOut(hdc, run.psc, drawX, drawRunY, 0, null, run.analysis , 0, 0, run.glyphs, run.glyphCount, run.advances, null, run.goffsets);
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
				}
			}
			drawX += run.width;
		}
	}
	OS.RestoreDC(hdc, state);
	if (selBrush != 0) OS.DeleteObject (selBrush);
}

void freeRuns () {
	if (allRuns == null) return;
	for (int i=0; i<allRuns.length; i++) {
		StyleItem run = allRuns[i];
		run.free();
	}
	allRuns = null;
	runs = null;
}

/**
 * Returns the current alignment of the receiver, the possible return valus are
 * SWT.LEFT, SWT.CENTER, SWT.RIGHT
 * 
 * @return the alignment 
 */
public int getAlignment () {
	checkLayout();
	return alignment;
}

/**
 * Returns the bounds of the receivers which is equilavent to the sums of the line
 * height and the max line width
 *  
 * @return the boundsof the receiver
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
 *  @return Rectangle the rectangle that conver all characters 
 * (considering potencial bidi reordering) between start and end.
 * start and end are inclusive, out of range values are clamp
 */
public Rectangle getBounds (int start, int end) {
	checkLayout();
	computeRuns(null);
	int length = text.length();
	if (length == 0) return new Rectangle(0, 0, 0, 0);
	if (start > end) return new Rectangle(0, 0, 0, 0);
	start = Math.min(Math.max(0, start), length - 1);
	end = Math.min(Math.max(0, end), length - 1);
	int startLine = getLineIndex(start);
	int endLine = getLineIndex(end);
	if (startLine != endLine) {
		int width = 0;
		int y = lineY[startLine];
		while (startLine <= endLine) {
			width = Math.max (width, lineWidth[startLine++]);
		}
		return new Rectangle (0, y, width, lineY[endLine + 1] - y);
	}
	int x = 0, startRunX = 0, endRunX = 0, i = 0;
	StyleItem startRun = null, endRun = null, lastRun;
	StyleItem[] lineRuns = runs[startLine];
	for (; i < lineRuns.length; i++) {
		StyleItem run = lineRuns[i];
		int runEnd = run.start + run.length;
		if (runEnd == length) runEnd++;
		if (run.start <= start && start < runEnd) {
			startRun = run;
			startRunX = x;
			break;
		}
		x  += run.width;
	}
	boolean reordered = false;	
	lastRun = startRun;
	boolean isRTL = (orientation & SWT.RIGHT_TO_LEFT) != 0 ^ (lastRun.analysis.s.uBidiLevel & 1) != 0;
	for (; i < lineRuns.length; i++) {
		StyleItem run = lineRuns[i];
		if (run != lastRun) {
			if (isRTL) {
				reordered = run.start + run.length != lastRun.start;
			} else {
				reordered = lastRun.start + lastRun.length != run.start;
			}
		}
		if (reordered) break;
		lastRun = run;
		int runEnd = run.start + run.length;	
		if (runEnd == length) runEnd++;
		if ( run.start <= end && end < runEnd) {
			endRun = run;
			endRunX = x;
			break;
		}
		x  += run.width;
	}
	if (reordered || endRun == null) {
		int y = lineY[startLine];
		return new Rectangle (0, y, lineWidth[startLine], lineY[startLine + 1] - y);
	}
	if (((startRun.analysis.s.uBidiLevel & 1) != 0) ^ ((endRun.analysis.s.uBidiLevel & 1) != 0)) {
		int y = lineY[startLine];
		return new Rectangle (startRunX, y, endRunX + endRun.width, lineY[startLine + 1] - y);
	}
	int startX, endX;
	if (startRun.tab) {
		startX = startRunX;
	} else {
		int runOffset = start - startRun.start;
		int cChars = startRun.length;
		int gGlyphs = startRun.glyphCount;
		int[] piX = new int[1];
		OS.ScriptCPtoX(runOffset, false, cChars, gGlyphs, startRun.clusters, startRun.visAttrs, startRun.advances, startRun.analysis, piX);
		if ((orientation & SWT.RIGHT_TO_LEFT) != 0) {
			piX[0] = startRun.width - piX[0];
		}
		startX = startRunX + piX[0];
	}
	if (endRun.tab) {
		endX = endRunX + endRun.width;
	} else {
		int runOffset = end - endRun.start;
		int cChars = endRun.length;
		int gGlyphs = endRun.glyphCount;
		int[] piX = new int[1];
		OS.ScriptCPtoX(runOffset, true, cChars, gGlyphs, endRun.clusters, endRun.visAttrs, endRun.advances, endRun.analysis, piX);
		if ((orientation & SWT.RIGHT_TO_LEFT) != 0) {
			piX[0] = endRun.width - piX[0];
		}
		endX = endRunX + piX[0];
	}
	if (startX > endX) {
		int tmp = startX;
		startX = endX;
		endX = tmp;
	}
	int width = endX - startX;
	if (wrapWidth != -1) {
		switch (alignment) {
			case SWT.CENTER: startX += (wrapWidth - lineWidth[startLine]) / 2; break;
			case SWT.RIGHT: startX += wrapWidth - lineWidth[startLine]; break;
		}
	}
	int y = lineY[startLine];
	return new Rectangle (startX, y, width, lineY[startLine + 1] - y);
}

/**
 * Returns the font of the receiver
 * 
 * @return the font
 */
public Font getFont () {
	checkLayout();
	return font;
}

/**
 * Returns the embedding level at given character offset
 * 
 * @param offset
 * @return level
 */
public int getLevel (int offset) {
	checkLayout();
	computeRuns(null);
	int length = text.length();
	if (!(0 <= offset && offset <= length)) SWT.error(SWT.ERROR_INVALID_RANGE);
	int line;
	for (line=0; line<runs.length; line++) {
		if (lineOffset[line + 1] > offset) break;
	}
	line = Math.min(line, runs.length - 1);
	StyleItem[] lineRuns = runs[line];
	for (int i=0; i<lineRuns.length; i++) {
		StyleItem run = lineRuns[i];
		int end = run.start + run.length;
		if (end == length) end++;
		if (run.start <= offset && offset < end) {
			return run.analysis.s.uBidiLevel;
		}
	}
	return 0;
}

/**
 * Returns the bounds of the line at given line index
 * 
 * @param line index
 * @return line bounds 
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
 * Returns the line count (include hard and soft breaks(wrap lines)).
 *
 * @return line count
 */
public int getLineCount () {
	checkLayout();
	computeRuns(null);
	return runs.length;
}

/**
 * Returns the line index of the line that containing the given character offset
 * 
 * @param character offset
 * @return line index
 */
public int getLineIndex (int offset) {
	checkLayout();
	computeRuns(null);
	int length = text.length();
	if (!(0 <= offset && offset <= length)) SWT.error(SWT.ERROR_INVALID_RANGE);
	for (int line=0; line<runs.length; line++) {
		if (lineOffset[line + 1] > offset) {
			return line;
		}
	}
	return runs.length - 1;
}

/**
 * Returns the font metrics of the line at given line index
 * 
 * @param line index
 * @return line font metrics 
 */
public FontMetrics getLineMetrics (int lineIndex) {
	checkLayout();
	computeRuns(null);
	if (!(0 <= lineIndex && lineIndex < runs.length)) SWT.error(SWT.ERROR_INVALID_RANGE);
	int hdc = device.internal_new_GC(null);
	Font font = this.font != null ? this.font : device.getSystemFont();
	TEXTMETRIC lptm = OS.IsUnicode ? (TEXTMETRIC)new TEXTMETRICW() : new TEXTMETRICA();
	if (text.length() == 0) {
		OS.SelectObject(hdc, font.handle);
		OS.GetTextMetrics(hdc, lptm);
	} else {
		int ascent = 0, descent = 0, leading = 0, aveCharWidth = 0, height = 0;
		StyleItem[] lineRuns = runs[lineIndex];
		for (int i = 0; i<lineRuns.length; i++) {
			StyleItem run = lineRuns[i];
			Font runFont = run.style != null ? run.style.font : null;
			if (runFont == null) runFont = font;
			OS.SelectObject(hdc, runFont.handle);
			OS.GetTextMetrics(hdc, lptm);
			ascent = Math.max (ascent, lptm.tmAscent);
			descent = Math.max (descent, lptm.tmDescent);
			height = Math.max (height, lptm.tmHeight);
			leading = Math.max (leading, lptm.tmInternalLeading);
			aveCharWidth += lptm.tmAveCharWidth;
		}
		lptm.tmAscent = ascent;
		lptm.tmDescent = descent;
		lptm.tmHeight = height;
		lptm.tmInternalLeading = leading;
		lptm.tmAveCharWidth = aveCharWidth / lineRuns.length;
	}
	device.internal_dispose_GC(hdc, null);
	return FontMetrics.win32_new(lptm);
}

/**
 * Given a valid line index returns a point holding start character offset of the line in the x field 
 * and the end character offset of the line in the y field.  
 * 
 * @param line index
 * @return line offsets
 */
public Point getLineOffsets (int lineIndex) {
	checkLayout();
	computeRuns(null);
	if (!(0 <= lineIndex && lineIndex < runs.length)) SWT.error(SWT.ERROR_INVALID_RANGE);
	int start = lineOffset[lineIndex];
	int end = lineOffset[lineIndex + 1]  - 1;
	return new Point (start, Math.max(start, end));
}

/**
 *  Returns the location point given a character offset 
 * 
 * For offset equals to length it returns the trail edge of last line
 * 
 * @param character offset
 * @return Point location
 */
public Point getLocation (int offset, int trailing) {
	checkLayout();
	computeRuns(null);
	int length = text.length();
	if (!(0 <= offset && offset <= length)) SWT.error(SWT.ERROR_INVALID_RANGE);
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
				boolean isTrailing = (trailing & SWT.TRAIL) != 0;
				if (run.tab) {
					if (isTrailing || (offset == length)) width += run.width;
					result = new Point(width, lineY[line]);
				} else {
					int runOffset = offset - run.start;
					int cChars = run.length;
					int gGlyphs = run.glyphCount;
					int[] piX = new int[1];
					OS.ScriptCPtoX(runOffset, isTrailing, cChars, gGlyphs, run.clusters, run.visAttrs, run.advances, run.analysis, piX);
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
 * Given a offset and movement type returns the next character offset 
 * The possible values for movement type are:
 * CHARACTER, WORD, CLUSTER
 * 
 * @param start offset
 * @param movement type 
 * @return end offset
 */
public int getNextOffset (int offset, int movement) {
	checkLayout();
	computeRuns(null);
	int length = text.length();
	if (!(0 <= offset && offset <= length)) SWT.error(SWT.ERROR_INVALID_RANGE);
	if (offset == length) return length;
	if ((movement & MOVEMENT_CHAR) != 0) return offset + 1;
	int[] ppSp = new int[1];
	int[] piNumScripts = new int[1];
	OS.ScriptGetProperties(ppSp, piNumScripts);
	int[] scripts = new int[piNumScripts[0]];
	OS.MoveMemory(scripts, ppSp[0], scripts.length * 4);
	SCRIPT_PROPERTIES properties = new SCRIPT_PROPERTIES();
	SCRIPT_LOGATTR logAttr = new SCRIPT_LOGATTR();
	boolean previousWhitespace = false;
	int i = 0;	
	int lastScript  = -1;
	for (; i < allRuns.length; i++) {
		StyleItem run = allRuns[i];
		if (run.start <= offset && offset < run.start + run.length) {
			if (run.lineBreak && !run.softBreak) return run.start + run.length;
			breakRun(run);
			OS.MoveMemory(logAttr, run.psla + ((offset - run.start) * SCRIPT_LOGATTR.sizeof), SCRIPT_LOGATTR.sizeof);	
			previousWhitespace = logAttr.fWhiteSpace;
			lastScript = run.analysis.eScript;
			break;
		}
	}
	offset++;
	for (; i < allRuns.length && offset < length; i++) {
		StyleItem run = allRuns[i];
		if (run.start <= offset && offset < run.start + run.length) {
			if (run.lineBreak && !run.softBreak) return offset;
			OS.MoveMemory(properties, scripts[run.analysis.eScript], SCRIPT_PROPERTIES.sizeof);
			if (((movement & MOVEMENT_CLUSTER) != 0) && !properties.fNeedsCaretInfo) {
				return offset;
			}
			breakRun(run);
			if ((movement & MOVEMENT_WORD) != 0) {
				if (run.analysis.eScript != lastScript) {
					OS.MoveMemory(logAttr, run.psla + ((offset - run.start) * SCRIPT_LOGATTR.sizeof), SCRIPT_LOGATTR.sizeof);
					if (!logAttr.fWhiteSpace) return offset;
				}				
				lastScript = run.analysis.eScript;
			}
			while (run.start <= offset && offset < run.start + run.length) {
				OS.MoveMemory(logAttr, run.psla + ((offset - run.start) * SCRIPT_LOGATTR.sizeof), SCRIPT_LOGATTR.sizeof);
				if (!logAttr.fInvalid) {
					if ((movement & MOVEMENT_CLUSTER) != 0 && logAttr.fCharStop) return offset;
					if ((movement & MOVEMENT_WORD) != 0) {
						if (properties.fNeedsWordBreaking) {
							if (logAttr.fWordStop) return offset;
						} else {
							if (!logAttr.fWhiteSpace && previousWhitespace) return offset;
						}
					}
					previousWhitespace = logAttr.fWhiteSpace;
				}
				offset++;
			}
		}
	}
	return length;
}

/**
 * Returns the character offset under the given a Point 
 * 
 * @param x
 * @param y
 * @return character offset
 */
public int getOffset (Point point, int[] trailing) {
	checkLayout();
	if (point == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
	return getOffset (point.x, point.y, trailing) ;
}

/**
 * Returns the character offset under the given x and y coordinates 
 * 
 * @param x
 * @param y
 * @return character offset
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
	boolean isRTL = (orientation & SWT.RIGHT_TO_LEFT) != 0; 
	if (x < 0) {
		StyleItem firstRun = lineRuns[0];
		if (firstRun.analysis.fRTL ^ isRTL) {
			if (trailing != null) trailing[0] = SWT.TRAIL;
			return firstRun.start + firstRun.length - 1;
		} else {
			if (trailing != null) trailing[0] = SWT.LEAD; 
			return firstRun.start;
		}
	}
	if (x > lineWidth[line]) {
		int index = lineRuns.length - 1;
		if (line == lineCount - 1 && lineRuns.length > 1) index--;
		StyleItem lastRun = lineRuns[index];
		if (lastRun.analysis.fRTL ^ isRTL) {
			if (trailing != null) trailing[0] = SWT.LEAD; 
			return lastRun.start;
		} else {
			if (trailing != null) trailing[0] = SWT.TRAIL;
			return lastRun.start + lastRun.length - 1;
		}
	}	
	int width = 0;
	for (int i = 0; i < lineRuns.length; i++) {
		StyleItem run = lineRuns[i];
		if (run.lineBreak && !run.softBreak) return run.start;
		if (width + run.width > x) {
			if (run.tab) {
				if (trailing != null) {
					trailing[0] = x < (width + run.width / 2) ? SWT.LEAD : SWT.TRAIL;
				}
				return run.start;
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
			if (trailing != null) {
				trailing[0] = piTrailing[0] == 0 ? SWT.LEAD : SWT.TRAIL;
			}
			return run.start + piCP[0];
		}
		width += run.width;
	}
	if (trailing != null) trailing[0] = SWT.LEAD; 
	return lineOffset[line + 1];
}

/**
 * Returns the receiver orientation
 */
public int getOrientation () {
	checkLayout();
	return orientation;
}

/**
 * Given a offset and movement type returns the previous character offset 
 * The possible values for movement type are:
 * CHARACTER, WORD, CLUSTER
 * 
 * @param start offset
 * @param movement type 
 * @return end offset
 */
public int getPreviousOffset (int offset, int movement) {
	checkLayout();
	computeRuns(null);
	int length = text.length();
	if (!(0 <= offset && offset <= length)) SWT.error(SWT.ERROR_INVALID_RANGE);
	if (offset == 0) return 0;
	if ((movement & MOVEMENT_CHAR) != 0) return offset - 1;
	int[] ppSp = new int[1];
	int[] piNumScripts = new int[1];
	OS.ScriptGetProperties(ppSp, piNumScripts);
	int[] scripts = new int[piNumScripts[0]];
	OS.MoveMemory(scripts, ppSp[0], scripts.length * 4);
	SCRIPT_PROPERTIES properties = new SCRIPT_PROPERTIES();
	SCRIPT_LOGATTR logAttr = new SCRIPT_LOGATTR();
	boolean previousWhitespace = false;
	int i = allRuns.length - 1;
	int lastScript  = -1;
	offset--;
	for (;  i >= 0; i--) {
		StyleItem run = allRuns[i];
		if (run.start <= offset && offset < run.start + run.length) {
			if (run.lineBreak && !run.softBreak) return run.start;
			breakRun(run);
			OS.MoveMemory(logAttr, run.psla + ((offset - run.start) * SCRIPT_LOGATTR.sizeof), SCRIPT_LOGATTR.sizeof);	
			previousWhitespace = logAttr.fWhiteSpace;
			lastScript = run.analysis.eScript;
			break;
		}
	}
	for (; i >= 0 && offset >= 0; i--) {
		StyleItem run = allRuns[i];
		if (run.start <= offset && offset < run.start + run.length) {
			if (run.lineBreak && !run.softBreak) return run.start + run.length;
			OS.MoveMemory(properties, scripts[run.analysis.eScript], SCRIPT_PROPERTIES.sizeof);
			if (((movement & MOVEMENT_CLUSTER) != 0) && !properties.fNeedsCaretInfo) {
				return offset;
			}
			if ((movement & MOVEMENT_WORD) != 0) {
				if (run.analysis.eScript != lastScript) {
					if (!previousWhitespace) return offset + 1;
				}
				lastScript = run.analysis.eScript;
			}
			breakRun(run);
			while (run.start <= offset && offset < run.start + run.length) {
				OS.MoveMemory(logAttr, run.psla + ((offset - run.start) * SCRIPT_LOGATTR.sizeof), SCRIPT_LOGATTR.sizeof);
				if (!logAttr.fInvalid) {
					if ((movement & MOVEMENT_CLUSTER) != 0 && logAttr.fCharStop) return offset;
					if ((movement & MOVEMENT_WORD) != 0) {
						if (properties.fNeedsWordBreaking) {
							if (logAttr.fWordStop) return offset;
						} else {
							if (logAttr.fWhiteSpace && !previousWhitespace) return offset + 1;
						}
					}
					previousWhitespace = logAttr.fWhiteSpace;
				};
				offset--;
			}
		}
	}
	return 0;
}

/**
 * Returns the receiver line spacing
 */
public int getSpacing () {
	checkLayout();	
	return lineSpacing;
}

/**
 *  Returns the text style given a character offset
 * 
 * @param character offset
 * @return text style
 */
public TextStyle getStyle (int offset) {
	checkLayout();
	int length = text.length();
	if (!(0 <= offset && offset < length)) SWT.error(SWT.ERROR_INVALID_RANGE);
	for (int i=1; i<styles.length; i++) {
		StyleItem item = styles[i];
		if (item.start > offset) {
			return styles[i - 1].style;
		}
	}
	return null;
}

/**
 * Returns the tab list of the receiver
 * 
 * @return tab list
 */
public int[] getTabs () {
	checkLayout();
	return tabs;
}

/**
 * Returns the text of the receiver
 * 
 * @return text
 */
public String getText () {
	checkLayout();
	return text;
}

/**
 * Returns the width of the receiver
 * 
 * @return width
 */
public int getWidth () {
	checkLayout();
	return wrapWidth;
}

public boolean isDisposed () {
	return device == null;
}

/*
 *  Itemize the receiver text
 */
StyleItem[] itemize () {
	int length = text.length();
	SCRIPT_CONTROL scriptControl = new SCRIPT_CONTROL();
	SCRIPT_STATE scriptState = new SCRIPT_STATE();
	int MAX_ITEM = length * 2; //if max_item == length ScriptItemize fails for length == 1
	
	//TODO still working this problem out
	if ((orientation & SWT.RIGHT_TO_LEFT) != 0) {
		scriptState.uBidiLevel = 1;
//		scriptControl.uDefaultLanguage = 0x01;
		scriptState.fArabicNumContext = true;
	}
	
	//TODO NeutralOverride is important when breaking up the RTL text into segments
	// bug mostly of the time it just case problems.
//	scriptControl.fNeutralOverride = true;
//	scriptControl.fNumericOverride = true;
	
	int hHeap = OS.GetProcessHeap();
	int pItems = OS.HeapAlloc(hHeap, OS.HEAP_ZERO_MEMORY, MAX_ITEM * SCRIPT_ITEM.sizeof);
	int[] pcItems = new int[1];	
	char[] chars = new char[length];
	text.getChars(0, length, chars, 0); 
	/*int hr = */OS.ScriptItemize(chars, length, MAX_ITEM, scriptControl, scriptState, pItems, pcItems);
//	if (hr == E_OUTOFMEMORY) //TODO handle it
	
	/* Merge */
	StyleItem[] runs = merge(pItems, pcItems[0]);
	OS.HeapFree(hHeap, 0, pItems);
	return runs;
}

/* 
 *  Merge styles ranges and script items 
 */
StyleItem[] merge (int items, int itemCount) {
	int count = 0, start = 0, end = text.length(), itemIndex = 0, styleIndex = 0;
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
		int styleLimit = styles[styleIndex + 1].start;
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
	byte[] bidiLevels = new byte[runs.length];
	for (int i=0; i<runs.length; i++) {
		bidiLevels[i] = (byte)(runs[i].analysis.s.uBidiLevel & 0x1F);
	}
	int[] log2vis = new int[runs.length];
	OS.ScriptLayout(runs.length, bidiLevels, null, log2vis);
	StyleItem[] result = new StyleItem[runs.length];
	for (int i=0; i<runs.length; i++) {
		result[log2vis[i]] = runs[i];
	}	
	if ((orientation & SWT.RIGHT_TO_LEFT) != 0) {
		for (int i = 0; i < (result.length - 1) / 2 ; i++) {
			StyleItem tmp = result[i];
			result[i] = result[result.length - i - 2];
			result[result.length - i - 2] = tmp;
		}
	}
	return result;
}

/**
 * Sets the alignment, the possible values for alignment are
 * SWT.LEFT, SWT.CENTER, SWT.RIGHT
 * or (equivalent but more suitable for bidi)
 * SWT.LEAD, SWT.CENTER, SWT.TRAIL
 * 
 * @param alignment
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
 * Sets the font
 * 
 * @param font
 */
public void setFont (Font font) {
	if (font != null && font.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	freeRuns();
	this.font = font;
}

/**
 *  Sets the receiver default orientation
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
 *  Sets the line spacing (space between lines)
 * 
 * @param spacing
 */
public void setSpacing (int spacing) {
	checkLayout();
	if (spacing < 0) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	freeRuns();
	this.lineSpacing = spacing;
}

/** 
 * set a style, a new style overrides existent styles
 * start and end are inclusive
 */
public void setStyle (TextStyle style, int start, int end) {
	checkLayout();
	int length = text.length();
	if (length == 0) return;
	if (start > end) return;
	start = Math.min(Math.max(0, start), length - 1);
	end = Math.min(Math.max(0, end), length - 1);
	freeRuns();
	int count = 0, i;
	StyleItem[] newStyles = new StyleItem[styles.length + 2];
	StyleItem previousStyle = null;
	for (i = 0; i < styles.length; i++) {
		StyleItem item = styles[i];
		if (item.start >= start) {
			if (item.start == start) previousStyle = item;
			break;
		}
		newStyles[count++] = item;
	}
	StyleItem newItem = new StyleItem();
	newItem.start = start;
	newItem.style = style;
	newStyles[count++] = newItem;
	for (; i<styles.length; i++) {
		StyleItem item = styles[i];
		if (item.start >= end) break;
		previousStyle = null;
	}
	newItem = new StyleItem();
	newItem.style = previousStyle != null ? previousStyle.style : styles[Math.max(0, i - 1)].style;
	newItem.start = end + 1;
	newStyles[count++] = newItem;
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
 *  Sets the tab list, a tab list is a list of spaces in pixels between the previous tab stop and 
 *  the current one.
 * 
 * @param tabs list
 */
public void setTabs (int[] tabs) {
	checkLayout();
	freeRuns();
	this.tabs = tabs;
} 

/**
 *  Sets the text in the receiver 
 * 
 * @param text
 */
public void setText (String text) {
	checkLayout();
	if (text == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	freeRuns();
	this.text = text;
	styles = new StyleItem[2];
	styles[0] = new StyleItem();
	styles[1] = new StyleItem();	
	styles[1].start = text.length();
}

/** 
 *  Sets the width in the receiver, setting the width in a TextLayout automatically causes 
 *  word wrap, to desactivate word wrap the width must the set to -1.
 * 
 * @param width
 */
public void setWidth (int width) {
	checkLayout();
	if (width < -1 || width == 0) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	freeRuns();
	this.wrapWidth = width;
}

/* 
 * Generate glyphs for one Run.
 */
void shape (int hdc, StyleItem run) {
	int[] buffer = new int[1];
	char[] chars = new char[run.length];
	text.getChars(run.start, run.start + run.length, chars, 0);
	int MAX_GLYPHS = (chars.length * 3 / 2) + 16;
	int hHeap = OS.GetProcessHeap();
	int pGlyphs = OS.HeapAlloc(hHeap, OS.HEAP_ZERO_MEMORY, MAX_GLYPHS * 2);
	int pClusters = OS.HeapAlloc(hHeap, OS.HEAP_ZERO_MEMORY, MAX_GLYPHS * 2);
	int psva = OS.HeapAlloc(hHeap, OS.HEAP_ZERO_MEMORY, MAX_GLYPHS * SCRIPT_VISATTR_SIZEOF);
	run.psc = OS.HeapAlloc(hHeap, OS.HEAP_ZERO_MEMORY, 4);
	OS.ScriptShape(hdc, run.psc, chars, chars.length, MAX_GLYPHS, run.analysis, pGlyphs, pClusters, psva, buffer);
	//TODO
//	if (hr == E_OUTOFMEMORY)
//	if (hr == USP_E_SCRIPT_NOT_IN_FONT)
	device.addScriptCache(run.psc);
	run.glyphs = pGlyphs;
	run.glyphCount = buffer[0];
	run.clusters = pClusters;
	run.visAttrs = psva;
	int[] abc = new int[3];
	int pGoffsets = OS.HeapAlloc(hHeap, OS.HEAP_ZERO_MEMORY, run.glyphCount * GOFFSET_SIZEOF);
	int piAdvances = OS.HeapAlloc(hHeap, OS.HEAP_ZERO_MEMORY, run.glyphCount * 4);
	OS.ScriptPlace(hdc, run.psc, run.glyphs, run.glyphCount, run.visAttrs, run.analysis, piAdvances, pGoffsets, abc);
	OS.ScriptCacheGetHeight(hdc, run.psc, buffer);
	run.advances = piAdvances;
	run.goffsets = pGoffsets;
	run.width = abc[0] + abc[1] + abc[2];
	run.height = buffer[0];
}
}
