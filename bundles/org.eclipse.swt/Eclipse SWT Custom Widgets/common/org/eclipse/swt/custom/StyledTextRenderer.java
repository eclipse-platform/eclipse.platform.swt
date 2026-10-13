/*******************************************************************************
 * Copyright (c) 2000, 2018 IBM Corporation and others.
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
 *     Anton Leherbauer (Wind River Systems) - Bug 439419
 *     Angelo Zerr <angelo.zerr@gmail.com> - Customize different line spacing of StyledText - Bug 522020
 *******************************************************************************/
package org.eclipse.swt.custom;


import java.util.*;
import java.util.List;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.widgets.*;

/**
 * A StyledTextRenderer renders the content of a StyledText widget.
 * This class can be used to render to the display or to a printer.
 */
class StyledTextRenderer {
	Device device;
	StyledText styledText;
	StyledTextContent content;

	/* Custom line spacing */
	StyledTextLineSpacingProvider lineSpacingProvider;
	boolean lineSpacingComputing;

	/* Custom line metrics */
	private FontMetrics fixedLineMetrics;

	/* Font info */
	Font regularFont, boldFont, italicFont, boldItalicFont;
	int tabWidth;
	int ascent, descent;
	int fontAscent, fontDescent;
	int averageCharWidth;
	int tabLength;	//tab length in spaces

	/* Line data */
	int topIndex = -1;
	TextLayout[] layouts;
	int lineCount;
	/**
	 * Per-line state (sizes, attribute masks, runs and patterns, interned ids):
	 * parent-owned arrays, no object per line.
	 */
	StyledTextLineStore store = new StyledTextLineStore();
	/** Height of the line measured last by {@link #calculate(int, int)}, stored or not. */
	int lastHeight;
	int maxWidth;
	int maxWidthLineIndex;
	float averageLineHeight;
	int linesInAverageLineHeight;
	boolean idleRunning;
	/** Number of line layouts requested (observability for viewport-bound tests and benches). */
	long layoutRequests;
	/** Layouts built or rebuilt (cache misses of getTextLayout); hits = layoutRequests - layoutBuilds. */
	long layoutBuilds;
	/**
	 * Idle measurement window [idleStart, idleEnd): visible lines plus one viewport
	 * of overscan, as packed ints owned by the widget's renderer (no window object).
	 */
	int idleStart, idleEnd;

	/* Bullet */
	Bullet[] bullets;
	int[] bulletsIndices;
	int[] redrawLines;

	/* Style data */
	int[] ranges;
	int styleCount;
	StyleRange[] styles;
	StyleRange[] stylesSet;
	int stylesSetCount = 0;
	boolean hasLinks, fixedPitch;
	final static int BULLET_MARGIN = 8;

	final static boolean COMPACT_STYLES = true;
	final static boolean MERGE_STYLES = true;

	final static int GROW = 32;
	/**
	 * Capacity for a growable array that must hold {@code required} elements of
	 * {@code elementBytes} bytes each, sampled once per capacity miss: twice the
	 * current capacity while the heap has headroom (the slack stays under a quarter of
	 * the free headroom), the exact required length under memory pressure.
	 * Single switch point for the shared SWT growth helper (PackedHeapGrowth port).
	 */
	static int grownCapacity(int current, int required, int elementBytes) {
		if (required <= current) {
			return current;
		}
		long doubled = Math.max((long) required, 2L * current);
		Runtime runtime = Runtime.getRuntime();
		long headroom = runtime.maxMemory() - (runtime.totalMemory() - runtime.freeMemory());
		long slackBytes = (doubled - required) * elementBytes;
		if (doubled > Integer.MAX_VALUE - 8 || slackBytes > headroom / 4) {
			return required;
		}
		return (int) doubled;
	}
	final static int IDLE_TIME = 50;
	/**
	 * Documents with more lines than this keep the logical extent estimated and
	 * measure only the visible window plus overscan (viewport mode). Smaller
	 * documents keep the exact whole-document measurement.
	 */
	final static int VIEWPORT_LINE_THRESHOLD = 1 << 16;
	final static int CACHE_SIZE = 300;

	final static int BACKGROUND = StyledTextLineStore.BACKGROUND;
	final static int ALIGNMENT = StyledTextLineStore.ALIGNMENT;
	final static int INDENT = StyledTextLineStore.INDENT;
	final static int JUSTIFY = StyledTextLineStore.JUSTIFY;
	final static int SEGMENTS = StyledTextLineStore.SEGMENTS;
	final static int TABSTOPS = StyledTextLineStore.TABSTOPS;
	final static int WRAP_INDENT = StyledTextLineStore.WRAP_INDENT;
	final static int SEGMENT_CHARS = StyledTextLineStore.SEGMENT_CHARS;
	final static int VERTICAL_INDENT = StyledTextLineStore.VERTICAL_INDENT;
	final static int UNMEASURED = StyledTextLineStore.UNMEASURED;

	private record LineDrawInfo(int index, TextLayout layout, String text, int offset, int height) {

	}

	static int cap (TextLayout layout, int offset) {
        if (layout == null) {
            return offset;
        }
		return Math.min (layout.getText().length() -1, Math.max (0, offset));
	}

StyledTextRenderer(Device device, StyledText styledText) {
	this.device = device;
	this.styledText = styledText;
}
int addMerge(int[] mergeRanges, StyleRange[] mergeStyles, int mergeCount, int modifyStart, int modifyEnd) {
	int rangeCount = styleCount << 1;
	StyleRange endStyle = null;
	int endStart = 0, endLength = 0;
	if (modifyEnd < rangeCount) {
		endStyle = styles[modifyEnd >> 1];
		endStart = ranges[modifyEnd];
		endLength = ranges[modifyEnd + 1];
	}
	int grow = mergeCount - (modifyEnd - modifyStart);
	if (rangeCount + grow >= ranges.length) {
		int[] tmpRanges = new int[grownCapacity(ranges.length, ranges.length + grow + (GROW << 1), 4)];
		System.arraycopy(ranges, 0, tmpRanges, 0, modifyStart);
		StyleRange[] tmpStyles = new StyleRange[Math.max(styles.length + (grow >> 1) + GROW, tmpRanges.length >> 1)];
		System.arraycopy(styles, 0, tmpStyles, 0, modifyStart >> 1);
		if (rangeCount > modifyEnd) {
			System.arraycopy(ranges, modifyEnd, tmpRanges, modifyStart + mergeCount, rangeCount - modifyEnd);
			System.arraycopy(styles, modifyEnd >> 1, tmpStyles, (modifyStart + mergeCount) >> 1, styleCount - (modifyEnd >> 1));
		}
		ranges = tmpRanges;
		styles = tmpStyles;
	} else {
		if (rangeCount > modifyEnd) {
			System.arraycopy(ranges, modifyEnd, ranges, modifyStart + mergeCount, rangeCount - modifyEnd);
			System.arraycopy(styles, modifyEnd >> 1, styles, (modifyStart + mergeCount) >> 1, styleCount - (modifyEnd >> 1));
		}
	}
	if (MERGE_STYLES) {
		int j = modifyStart;
		for (int i = 0; i < mergeCount; i += 2) {
			if (j > 0 && ranges[j - 2] + ranges[j - 1] == mergeRanges[i] && mergeStyles[i >> 1].similarTo(styles[(j - 2) >> 1])) {
				ranges[j - 1] += mergeRanges[i + 1];
			} else {
				styles[j >> 1] = mergeStyles[i >> 1];
				ranges[j++] = mergeRanges[i];
				ranges[j++] = mergeRanges[i + 1];
			}
		}
		if (endStyle != null && ranges[j - 2] + ranges[j - 1] == endStart && endStyle.similarTo(styles[(j - 2) >> 1])) {
			ranges[j - 1] += endLength;
			modifyEnd += 2;
			mergeCount += 2;
		}
		if (rangeCount > modifyEnd) {
			System.arraycopy(ranges, modifyStart + mergeCount, ranges, j, rangeCount - modifyEnd);
			System.arraycopy(styles, (modifyStart + mergeCount) >> 1, styles, j >> 1, styleCount - (modifyEnd >> 1));
		}
		grow = (j - modifyStart) - (modifyEnd - modifyStart);
	} else {
		System.arraycopy(mergeRanges, 0, ranges, modifyStart, mergeCount);
		System.arraycopy(mergeStyles, 0, styles, modifyStart >> 1, mergeCount >> 1);
	}
	styleCount += grow >> 1;
	return grow;
}
int addMerge(StyleRange[] mergeStyles, int mergeCount, int modifyStart, int modifyEnd) {
	int grow = mergeCount - (modifyEnd - modifyStart);
	StyleRange endStyle = null;
    if (modifyEnd < styleCount) {
        endStyle = styles[modifyEnd];
    }
	if (styleCount + grow >= styles.length) {
		StyleRange[] tmpStyles = new StyleRange[grownCapacity(styles.length, styles.length + grow + GROW, 4)];
		System.arraycopy(styles, 0, tmpStyles, 0, modifyStart);
		if (styleCount > modifyEnd) {
			System.arraycopy(styles, modifyEnd, tmpStyles, modifyStart + mergeCount, styleCount - modifyEnd);
		}
		styles = tmpStyles;
	} else {
		if (styleCount > modifyEnd) {
			System.arraycopy(styles, modifyEnd, styles, modifyStart + mergeCount, styleCount - modifyEnd);
		}
	}
	if (MERGE_STYLES) {
		int j = modifyStart;
		for (int i = 0; i < mergeCount; i++) {
			StyleRange newStyle = mergeStyles[i], style;
			if (j > 0 && (style = styles[j - 1]).start + style.length == newStyle.start && newStyle.similarTo(style)) {
				style.length += newStyle.length;
			} else {
				styles[j++] = newStyle;
			}
		}
		StyleRange style = styles[j - 1];
		if (endStyle != null && style.start + style.length == endStyle.start && endStyle.similarTo(style)) {
			style.length += endStyle.length;
			modifyEnd++;
			mergeCount++;
		}
		if (styleCount > modifyEnd) {
			System.arraycopy(styles, modifyStart + mergeCount, styles, j, styleCount - modifyEnd);
		}
		grow = (j - modifyStart) - (modifyEnd - modifyStart);
	} else {
		System.arraycopy(mergeStyles, 0, styles, modifyStart, mergeCount);
	}
	styleCount += grow;
	return grow;
}
void calculate(int startLine, int lineCount) {
	int endLine = startLine + lineCount;
	if (startLine < 0 || endLine > this.lineCount) {
		return;
	}
	int hTrim = styledText.leftMargin + styledText.rightMargin + styledText.getCaretWidth();
	if (store.windowed) {
		store.follow(Math.max(0, styledText.topIndex));
	}
	for (int i = startLine; i < endLine; i++) {
		int width = store.width(i), height = store.height(i);
		if (width == UNMEASURED || height == UNMEASURED) {
			TextLayout layout = getTextLayout(i);
			Rectangle rect = layout.getBounds();
			width = rect.width + hTrim;
			height = rect.height;
			//FLLDCIM: MASKS - sizes and the wrapped bit are ints in the line store (window)
			store.setSize(i, width, height, layout.getLineCount() > 1 ? StyledTextLineStore.WRAPPED : 0);
			averageLineHeight += (height - Math.round(averageLineHeight)) / ++linesInAverageLineHeight;
			disposeTextLayout(layout);
		}
		lastHeight = height;
		if (width > maxWidth) {
			maxWidth = width;
			maxWidthLineIndex = i;
		}
	}
}
/** True when line i has no current width or height (never allocates). */
boolean needsMeasure(int i) {
	return store.width(i) == UNMEASURED || store.height(i) == UNMEASURED;
}
/**
 * Places the size window: the whole document in exact mode, the visible lines
 * plus overscan around the top line in viewport mode.
 */
void syncSizeWindow() {
	boolean windowed = styledText != null && isViewportMode();
	int top = styledText != null ? Math.max(0, styledText.topIndex) : 0;
	int span = windowed ? Math.max(1, viewportHeight() / Math.max(1, getLineHeight()) + 1) : 0;
	store.sizeWindow(windowed, top, span, lineCount);
}
void calculateClientArea () {
	int index = Math.max (0, styledText.getTopIndex());
	int lineCount = content.getLineCount();
	int height = viewportHeight();
	int y = 0;
	/*
	 * There exists a possibility of ArrayIndexOutOfBounds Exception in
	 * below code, exact scenario not known. To avoid this exception added
	 * check for 'index' value, refer Bug 471192.
	 */
	while (height > y && lineCount > index && this.lineCount > index) {
		calculate(index++, 1);
		y += lastHeight;
	}
}
void calculateIdle () {
    if (idleRunning) {
        return;
    }
	if (styledText != null && styledText.defer(StyledText.DEFER_MEASURE)) {
		//FLLDCIM: VISIBILITY - no idle measurement pass while the widget is not visible
		return;
	}
	if (isViewportMode()) {
		calculateIdleWindow();
		return;
	}
	Runnable runnable = new Runnable() {
		@Override
		public void run() {
            if (styledText == null) {
                return;
            }
			if (styledText.isDisposed() || styledText.defer(StyledText.DEFER_MEASURE)) {
				idleRunning = false;
				return;
			}
			if (isViewportMode()) {
				// the content grew past the threshold after this pass was queued
				// (e.g. setWordWrap followed by setText): measure the window only
				idleRunning = false;
				calculateIdleWindow();
				return;
			}
			int i;
			long start = System.currentTimeMillis();
			for (i = 0; i < lineCount; i++) {
				if (needsMeasure(i)) {
					calculate(i, 1);
                    if (System.currentTimeMillis() - start > IDLE_TIME) {
                        break;
                    }
				}
			}
			if (i < lineCount) {
				Display display = styledText.getDisplay();
				display.asyncExec(this);
			} else {
				idleRunning = false;
				styledText.setScrollBars(true);
				ScrollBar bar = styledText.getVerticalBar();
				if (bar != null) {
					bar.setSelection(styledText.getVerticalScrollOffset());
				}
			}
		}
	};
	Display display = styledText.getDisplay();
	display.asyncExec(runnable);
	idleRunning = true;
}
void clearLineBackground(int startLine, int count) {
	store.clear(startLine, count, BACKGROUND);
}
void clearLineStyle(int startLine, int count) {
	store.clear(startLine, count, StyledTextLineStore.LINE_STYLE);
}
void copyInto(StyledTextRenderer renderer) {
	if (ranges != null) {
		int[] newRanges = renderer.ranges = new int[styleCount << 1];
		System.arraycopy(ranges, 0, newRanges, 0, newRanges.length);
	}
	if (styles != null) {
		StyleRange[] newStyles = renderer.styles = new StyleRange[styleCount];
		for (int i = 0; i < newStyles.length; i++) {
			newStyles[i] = (StyleRange)styles[i].clone();
		}
		renderer.styleCount = styleCount;
	}
	if (store.hasAttributes()) {
		renderer.store = store.copyAttributes();
		renderer.lineCount = lineCount;
	}
}
void dispose() {
    if (boldFont != null) {
        boldFont.dispose();
    }
    if (italicFont != null) {
        italicFont.dispose();
    }
    if (boldItalicFont != null) {
        boldItalicFont.dispose();
    }
	boldFont = italicFont = boldItalicFont = null;
	reset();
	content = null;
	device = null;
	styledText = null;
}
void disposeTextLayout (TextLayout layout) {
	if (layouts != null) {
		for (TextLayout l : layouts) {
            if (l == layout) {
                return;
            }
		}
	}
	layout.dispose();
}
void drawBullet(Bullet bullet, GC gc, int paintX, int paintY, int index, int lineAscent, int lineDescent) {
	StyleRange style = bullet.style;
	GlyphMetrics metrics = style.metrics;
	Color color = style.foreground;
    if (color != null) {
        gc.setForeground(color);
    }
	Font font = style.font;
    if (font != null) {
        gc.setFont(font);
    }
	String string = "";
	int type = bullet.type & (ST.BULLET_DOT|ST.BULLET_NUMBER|ST.BULLET_LETTER_LOWER|ST.BULLET_LETTER_UPPER);
	switch (type) {
		case ST.BULLET_DOT: string = "\u2022"; break;
		case ST.BULLET_NUMBER: string = String.valueOf(index + 1); break;
		case ST.BULLET_LETTER_LOWER: string = String.valueOf((char) (index % 26 + 97)); break;
		case ST.BULLET_LETTER_UPPER: string = String.valueOf((char) (index % 26 + 65)); break;
	}
    if ((bullet.type & ST.BULLET_TEXT) != 0) {
        string += bullet.text;
    }
	Display display = styledText.getDisplay();
	TextLayout layout = new TextLayout(display);
	layout.setText(string);
	layout.setAscent(lineAscent);
	layout.setDescent(lineDescent);
	style = (StyleRange)style.clone();
	style.metrics = null;
    if (style.font == null) {
        style.font = getFont(style.fontStyle);
    }
	layout.setStyle(style, 0, string.length());
	int x = paintX + Math.max(0, metrics.width - layout.getBounds().width - BULLET_MARGIN);
	layout.draw(gc, x, paintY);
	layout.dispose();
}

/**
 * Caches draw-related info that may be expensive to calculate twice when
 * drawing first background and then foreground.
 */
private LineDrawInfo makeLineDrawInfo(int lineIndex) {
	TextLayout layout = getTextLayout(lineIndex);
	String text = content.getLine(lineIndex);
	int offset = content.getOffsetAtLine(lineIndex);
	int height = layout.getBounds().height;
	return new LineDrawInfo(lineIndex, layout, text, offset, height);
}

int drawLines(int startLine, int endLine, int begX, int begY, int endY, GC gc, Color widgetBackground, Color widgetForeground) {
	//FLLDCIM: RETAINED - the selection plane reads the widget's packed {start, end} pairs; no copy per paint
	final int[] selectionRanges = styledText.getBlockSelection() ? null : styledText.selection;
	// When fixed line metrics is in effect, tall unicode characters
	// will not always fit line's height. In this case, they will
	// draw out of line's bounds. To prevent them from being clipped
	// by next line's background, paint entire background before any
	// foreground.
	// I considered to make this mode default, but was worried about
	// potential regressions in various legacy code. For example, it
	// could change something about line heights/colors during
	// painting. While this doesn't sound like a good thing to do, yet
	// still, I'd rather stay safe.
	final boolean drawBackBeforeFore = (fixedLineMetrics != null);

	if (drawBackBeforeFore) {
		// Cache drawing information
		final List<LineDrawInfo> drawInfos = new ArrayList<>();
		int y = begY;
		for (int iLine = startLine; y < endY && iLine < endLine; iLine++) {
			LineDrawInfo lineInfo = makeLineDrawInfo(iLine);
			drawInfos.add(lineInfo);
			y += lineInfo.height;
		}

		// Draw background
		y = begY;
		for (LineDrawInfo lineInfo : drawInfos) {
			drawLineBackground(lineInfo, y, gc, widgetBackground);
			y += lineInfo.height;
		}

		// Draw foreground
		y = begY;
		for (LineDrawInfo lineInfo : drawInfos) {
			drawLineForeground(lineInfo, begX, y, gc, widgetForeground, selectionRanges);
			y += lineInfo.height;
		}

		// cleanup
		for (LineDrawInfo lineInfo : drawInfos) {
			disposeTextLayout(lineInfo.layout);
		}

		return y - begY;
	}

	int y = begY;
	for (int iLine = startLine; y < endY && iLine < endLine; iLine++) {
		LineDrawInfo lineInfo = makeLineDrawInfo(iLine);
		drawLineBackground(lineInfo, y, gc, widgetBackground);
		drawLineForeground(lineInfo, begX, y, gc, widgetForeground, selectionRanges);
		disposeTextLayout(lineInfo.layout);
		y += lineInfo.height;
	}
	return y - begY;
}

private void drawLineBackground(LineDrawInfo lineInfo, int paintY, GC gc, Color widgetBackground) {
	int clientWidth = styledText.clientAreaWidth; // the client area starts at 0; no Rectangle per line
	Color lineBackground = getLineBackground(lineInfo.index, null);
	StyledTextEvent event = styledText.getLineBackgroundData(lineInfo.offset, lineInfo.text);
    if (event != null && event.lineBackground != null) {
        lineBackground = event.lineBackground;
    }
	int verticalIndent = lineInfo.layout.getVerticalIndent();

	if (lineBackground != null) {
		if (verticalIndent > 0) {
			gc.setBackground(widgetBackground);
			gc.fillRectangle(0, paintY, clientWidth, verticalIndent);
		}
		gc.setBackground(lineBackground);
		gc.fillRectangle(0, paintY + verticalIndent, clientWidth, lineInfo.height - verticalIndent);
	} else {
		gc.setBackground(widgetBackground);
		styledText.drawBackground(gc, 0, paintY, clientWidth, lineInfo.height);
	}
}

private void drawLineForeground(LineDrawInfo lineInfo, int paintX, int paintY, GC gc, Color widgetForeground, int[] selectionRanges) {
	int lineLength = lineInfo.text.length();
	gc.setForeground(widgetForeground);
	// selectionRanges are null for block selection; otherwise packed {start, end} pairs, sorted
	if (selectionRanges != null) {
		boolean selected = false;
		Color selectionFg = null, selectionBg = null;
		int baseFlags = 0;
		for (int i = 0; i < selectionRanges.length; i += 2) {
			int x = selectionRanges[i] - lineInfo.offset;
			int y = selectionRanges[i + 1] - lineInfo.offset;
			if (x == y || x > lineLength || y < 0) {
				continue; // empty, or not on this line
			}
			if (!selected) {
				selected = true;
				selectionFg = styledText.getSelectionForeground();
				selectionBg = styledText.getSelectionBackground();
				baseFlags = (styledText.getStyle() & SWT.FULL_SELECTION) != 0 ? SWT.FULL_SELECTION : SWT.DELIMITER_SELECTION;
			}
			int start = Math.max(0, x);
			int end = Math.min(lineLength, y);
			int flags = baseFlags;
			if (x <= lineLength && lineLength < y) {
				flags |= SWT.LAST_LINE_SELECTION;
			}
			// TODO calling draw multiple times here prints line multiple times, overriding some colors
			lineInfo.layout.draw(gc, paintX, paintY, start, end - 1, selectionFg, selectionBg, flags);
		}
		if (!selected) {
			lineInfo.layout.draw(gc, paintX, paintY);
		}
	} else {
		lineInfo.layout.draw(gc, paintX, paintY);
	}

	// draw objects
	Bullet bullet = null;
	int bulletIndex = -1;
	if (bullets != null) {
		if (bulletsIndices != null) {
			int index = lineInfo.index - topIndex;
			if (0 <= index && index < CACHE_SIZE) {
				bullet = bullets[index];
				bulletIndex = bulletsIndices[index];
			}
		} else {
			for (Bullet b : bullets) {
				bullet = b;
				bulletIndex = bullet.indexOf(lineInfo.index);
                if (bulletIndex != -1) {
                    break;
                }
			}
		}
	}
	if (bulletIndex != -1 && bullet != null) {
		FontMetrics metrics = lineInfo.layout.getLineMetrics(0);
		int lineAscent = metrics.getAscent() + metrics.getLeading();
		if (bullet.type == ST.BULLET_CUSTOM) {
			bullet.style.start = lineInfo.offset;
			styledText.paintObject(gc, paintX, paintY, lineAscent, metrics.getDescent(), bullet.style, bullet, bulletIndex);
		} else {
			drawBullet(bullet, gc, paintX, paintY, bulletIndex, lineAscent, metrics.getDescent());
		}
	}
	TextStyle[] styles = lineInfo.layout.getStyles();
	int[] ranges = null;
	for (int i = 0; i < styles.length; i++) {
		if (styles[i].metrics != null) {
            if (ranges == null) {
                ranges = lineInfo.layout.getRanges();
            }
			int start = ranges[i << 1];
			int length = ranges[(i << 1) + 1] - start + 1;
			Point point = lineInfo.layout.getLocation(start, false);
			FontMetrics metrics = lineInfo.layout.getLineMetrics(lineInfo.layout.getLineIndex(start));
			StyleRange style = (StyleRange)((StyleRange)styles[i]).clone();
			style.start = start + lineInfo.offset;
			style.length = length;
			int lineAscent = metrics.getAscent() + metrics.getLeading();
			styledText.paintObject(gc, point.x + paintX, point.y + paintY, lineAscent, metrics.getDescent(), style, null, 0);
		}
	}
}


int getBaseline() {
	return ascent;
}
int getCachedLineHeight(int lineIndex) {
	return getLineHeight(lineIndex, false);
}
Font getFont(int style) {
	switch (style) {
		case SWT.BOLD:
            if (boldFont != null) {
                return boldFont;
            }
			return boldFont = new Font(device, getFontData(style));
		case SWT.ITALIC:
            if (italicFont != null) {
                return italicFont;
            }
			return italicFont = new Font(device, getFontData(style));
		case SWT.BOLD | SWT.ITALIC:
            if (boldItalicFont != null) {
                return boldItalicFont;
            }
			return boldItalicFont = new Font(device, getFontData(style));
		default:
			return regularFont;
	}
}
FontData[] getFontData(int style) {
	FontData[] fontDatas = regularFont.getFontData();
	for (FontData fontData : fontDatas) {
		fontData.setStyle(style);
	}
	return fontDatas;
}
/**
 * Viewport mode: the widget's SWT.VIRTUAL style bit, or a document above
 * VIEWPORT_LINE_THRESHOLD lines. Tested as a mask, no mode object.
 */
boolean isViewportMode() {
	return lineCount > VIEWPORT_LINE_THRESHOLD || (styledText != null && (styledText.getStyle() & SWT.VIRTUAL) != 0);
}
/**
 * Measures only the visible lines plus one viewport of overscan
 * ([idleStart, idleEnd)), in IDLE_TIME slices, then publishes the reconciled
 * scroll extent. Re-targeted when the top index moves.
 */
void calculateIdleWindow () {
	//FLLDCIM: VIEWPORT - visible + bounded overscan work only
	Runnable runnable = new Runnable() {
		@Override
		public void run() {
			if (styledText == null || styledText.isDisposed() || styledText.defer(StyledText.DEFER_MEASURE)) {
				idleRunning = false;
				return;
			}
			// the window follows the current top index, so a scroll re-targets pending work
			syncIdleWindow();
			long start = System.currentTimeMillis();
			int end = Math.min(lineCount, idleEnd);
			int i;
			for (i = idleStart; i < end; i++) {
				if (needsMeasure(i)) {
					calculate(i, 1);
					if (System.currentTimeMillis() - start > IDLE_TIME) {
						break;
					}
				}
			}
			if (i < end) {
				styledText.getDisplay().asyncExec(this);
			} else {
				idleRunning = false;
				styledText.setScrollBars(true);
				ScrollBar bar = styledText.getVerticalBar();
				if (bar != null) {
					bar.setSelection(styledText.getVerticalScrollOffset());
				}
			}
		}
	};
	idleRunning = true;
	styledText.getDisplay().asyncExec(runnable);
}
/**
 * Height of the area whose lines are pre-measured: the client area, capped in
 * viewport mode by the display height (a control sized to its whole preferred
 * height must not pre-measure lines that no screen can show; paint still lays
 * out whatever it paints).
 */
int viewportHeight () {
	//FLLDCIM: VISIBILITY - the client area ints from the last Resize; getClientArea() would force deferred window positions
	int height = styledText.clientAreaHeight;
	if (isViewportMode()) {
		height = Math.min(height, styledText.getDisplay().getClientArea().height);
	}
	return height;
}
/**
 * The idle measurement pass, synchronously (a caller asked for the extent of a
 * widget that deferred it while hidden): the window in viewport mode, every line
 * otherwise; then the scroll bars, as the asynchronous pass ends.
 */
void measureIdleNow () {
	if (isViewportMode()) {
		syncIdleWindow();
		calculate(idleStart, idleEnd - idleStart);
	} else {
		calculate(0, lineCount);
	}
	styledText.setScrollBars(true);
	ScrollBar bar = styledText.getVerticalBar();
	if (bar != null) {
		bar.setSelection(styledText.getVerticalScrollOffset());
	}
}
void syncIdleWindow () {
	// same arithmetic as ViewportRuntime.RowWindow (paintStart/paintEndExclusive), inline on ints
	int visible = Math.max(1, viewportHeight() / Math.max(1, getLineHeight()) + 1);
	int top = Math.max(0, Math.min(styledText.topIndex, lineCount - 1));
	store.follow(top);
	idleStart = Math.max(0, top - visible);
	idleEnd = (int) Math.min(lineCount, (long) top + 2L * visible);
}
int getHeight () {
	int defaultLineHeight = getLineHeight();
	if (styledText.isFixedLineHeight()) {
		return lineCount * defaultLineHeight + styledText.topMargin + styledText.bottomMargin;
	}
	if (isViewportMode()) {
		return getEstimatedHeight(defaultLineHeight) + styledText.topMargin + styledText.bottomMargin;
	}
	int totalHeight = 0;
	int width = styledText.getWrapWidth();
	for (int i = 0; i < lineCount; i++) {
		int height = store.height(i);
		if (height == UNMEASURED) {
			if (width > 0) {
				int length = content.getLine(i).length();
				height = ((length * averageCharWidth / width) + 1) * defaultLineHeight;
			} else {
				height = defaultLineHeight;
			}
		}
		totalHeight += height;
	}
	return totalHeight + styledText.topMargin + styledText.bottomMargin;
}
boolean hasLink(int offset) {
    if (offset == -1) {
        return false;
    }
	int lineIndex = content.getLineAtOffset(offset);
	int lineOffset = content.getOffsetAtLine(lineIndex);
	String line = content.getLine(lineIndex);
	StyledTextEvent event = styledText.getLineStyleData(lineOffset, line);
	if (event != null) {
		StyleRange[] styles = event.styles;
		if (styles != null) {
			int[] ranges = event.ranges;
			if (ranges != null) {
				for (int i = 0; i < ranges.length; i+=2) {
					if (ranges[i] <= offset && offset < ranges[i] + ranges[i+1] && styles[i >> 1].underline && styles[i >> 1].underlineStyle == SWT.UNDERLINE_LINK) {
						return true;
					}
				}
			} else {
				for (StyleRange style : styles) {
					if (style.start <= offset && offset < style.start + style.length && style.underline && style.underlineStyle == SWT.UNDERLINE_LINK) {
						return true;
					}
				}
			}
		}
	}  else {
		if (ranges != null) {
			int rangeCount = styleCount << 1;
			int index = getRangeIndex(offset, -1, rangeCount);
            if (index >= rangeCount) {
                return false;
            }
			int rangeStart = ranges[index];
			int rangeLength = ranges[index + 1];
			StyleRange rangeStyle = styles[index >> 1];
			if (rangeStart <= offset && offset < rangeStart + rangeLength && rangeStyle.underline && rangeStyle.underlineStyle == SWT.UNDERLINE_LINK) {
				return true;
			}
		}
	}
	return false;
}
int getLineAlignment(int index, int defaultAlignment) {
	return store.get(index, ALIGNMENT, defaultAlignment);
}
Color getLineBackground(int index, Color defaultBackground) {
	return (Color) store.getObject(index, BACKGROUND, defaultBackground);
}
Bullet getLineBullet (int index, Bullet defaultBullet) {
    if (bullets == null) {
        return defaultBullet;
    }
    if (bulletsIndices != null) {
        return defaultBullet;
    }
	for (Bullet bullet : bullets) {
        if (bullet.indexOf(index) != -1) {
            return bullet;
        }
	}
	return defaultBullet;
}
int getLineHeight() {
	return ascent + descent;
}
int getLineHeight(int lineIndex) {
	return getLineHeight(lineIndex, true);
}
int getLineHeight(int lineIndex, boolean exact) {
	int height = store.height(lineIndex);
	if (height == UNMEASURED) {
		// here we are in "variable line height", the call of calculate which uses TextLayout is very slow
		// so use the average line height of all calculated lines when many heights are needed e.g. for scrolling.
		if (isVariableHeight(lineIndex)) {
			if (exact) {
				calculate(lineIndex, 1);
				height = lastHeight;
			} else {
				// an estimate stores nothing
				return Math.round(averageLineHeight);
			}
		} else {
			height = getLineHeight() + getLineSpacing(lineIndex) + getLineVerticalIndent(lineIndex);
			store.setHeight(lineIndex, height);
		}
	}
	return height;
}
/**
 * Returns true if the given line can use the default line height and false
 * otherwise.
 *
 * @param lineIndex
 *            line index
 * @return true if the given line can use the default line height and false
 *         otherwise.
 */
private boolean isVariableHeight(int lineIndex) {
	if (styledText.isWordWrap()) {
		// In word wrap mode, the line height must be recomputed with TextLayout
		return true;
	}
	//FLLDCIM: NO-OBJECTS - int scan of the packed ranges; no StyleRange[] or line String per query
	int start = content.getOffsetAtLine(lineIndex);
	int end = lineIndex + 1 < content.getLineCount() ? content.getOffsetAtLine(lineIndex + 1) : content.getCharCount();
	return hasVariableHeightStyle(start, end - start);
}
/**
 * Whether a style range intersecting [start, start + length) changes line
 * height; the same range search as getStyleRanges(start, length, false), on the
 * packed store, without building the array.
 */
boolean hasVariableHeightStyle(int start, int length) {
	if (length == 0 || styles == null || styleCount == 0) {
		return false;
	}
	int end = start + length - 1;
	if (ranges != null) {
		int rangeCount = styleCount << 1;
		int rangeStart = getRangeIndex(start, -1, rangeCount);
		if (rangeStart >= rangeCount || ranges[rangeStart] > end) {
			return false;
		}
		int rangeEnd = Math.min(rangeCount - 2, getRangeIndex(end, rangeStart - 1, rangeCount));
		if (ranges[rangeEnd] > end) {
			rangeEnd = Math.max(rangeStart, rangeEnd - 2);
		}
		for (int i = rangeStart; i <= rangeEnd; i += 2) {
			if (styles[i >> 1].isVariableHeight()) {
				return true;
			}
		}
		return false;
	}
	int rangeStart = getRangeIndex(start, -1, styleCount);
	if (rangeStart >= styleCount || styles[rangeStart].start > end) {
		return false;
	}
	int rangeEnd = Math.min(styleCount - 1, getRangeIndex(end, rangeStart - 1, styleCount));
	if (styles[rangeEnd].start > end) {
		rangeEnd = Math.max(rangeStart, rangeEnd - 1);
	}
	for (int i = rangeStart; i <= rangeEnd; i++) {
		if (styles[i].isVariableHeight()) {
			return true;
		}
	}
	return false;
}
/**
 * returns true if the given line index defines custom line spacing and false
 * otherwise.
 *
 * @param lineIndex
 *            the line index.
 * @return true if the given line index defines custom line spacing and false
 *         otherwise.
 */
private int getLineSpacing(int lineIndex) {
	if (styledText.lineSpacing > 0) {
		return styledText.lineSpacing;
	} else if (lineSpacingProvider != null) {
		Integer lineSpacing = lineSpacingProvider.getLineSpacing(lineIndex);
		if (lineSpacing != null) {
			return lineSpacing;
		}
	}
	return 0;
}
int getLineIndent(int index, int defaultIndent) {
	return store.get(index, INDENT, defaultIndent);
}
int getLineVerticalIndent(int index) {
	return store.get(index, VERTICAL_INDENT, 0);
}
int getLineWrapIndent(int index, int defaultWrapIndent) {
	return store.get(index, WRAP_INDENT, defaultWrapIndent);
}
boolean getLineJustify(int index, boolean defaultJustify) {
	return store.find(index, JUSTIFY) ? store.found != 0 : defaultJustify;
}
int[] getLineTabStops(int index, int[] defaultTabStops) {
	return (int[]) store.getObject(index, TABSTOPS, defaultTabStops);
}
StyledTextLineSpacingProvider getLineSpacingProvider() {
	return lineSpacingProvider;
}
int getRangeIndex(int offset, int low, int high) {
    if (styleCount == 0) {
        return 0;
    }
	if (ranges != null)  {
		while (high - low > 2) {
			int index = ((high + low) / 2) / 2 * 2;
			int end = ranges[index] + ranges[index + 1];
			if (end > offset) {
				high = index;
			} else {
				low = index;
			}
		}
	} else {
		while (high - low > 1) {
			int index = ((high + low) / 2);
			int end = styles[index].start + styles[index].length;
			if (end > offset) {
				high = index;
			} else {
				low = index;
			}
		}
	}
	return high;
}
int[] getRanges(int start, int length) {
    if (length == 0) {
        return null;
    }
	int[] newRanges;
	int end = start + length - 1;
	if (ranges != null) {
		int rangeCount = styleCount << 1;
		int rangeStart = getRangeIndex(start, -1, rangeCount);
        if (rangeStart >= rangeCount) {
            return null;
        }
        if (ranges[rangeStart] > end) {
            return null;
        }
		int rangeEnd = Math.min(rangeCount - 2, getRangeIndex(end, rangeStart - 1, rangeCount));
        if (ranges[rangeEnd] > end) {
            rangeEnd = Math.max(rangeStart, rangeEnd - 2);
        }
		newRanges = new int[rangeEnd - rangeStart + 2];
		System.arraycopy(ranges, rangeStart, newRanges, 0, newRanges.length);
	} else {
		int rangeStart = getRangeIndex(start, -1, styleCount);
        if (rangeStart >= styleCount) {
            return null;
        }
        if (styles[rangeStart].start > end) {
            return null;
        }
		int rangeEnd = Math.min(styleCount - 1, getRangeIndex(end, rangeStart - 1, styleCount));
        if (styles[rangeEnd].start > end) {
            rangeEnd = Math.max(rangeStart, rangeEnd - 1);
        }
		newRanges = new int[(rangeEnd - rangeStart + 1) << 1];
		for (int i = rangeStart, j = 0; i <= rangeEnd; i++, j += 2) {
			StyleRange style = styles[i];
			newRanges[j] = style.start;
			newRanges[j + 1] = style.length;
		}
	}
	if (start > newRanges[0]) {
		newRanges[1] = newRanges[0] + newRanges[1] - start;
		newRanges[0] = start;
	}
	if (end < newRanges[newRanges.length - 2] + newRanges[newRanges.length - 1] - 1) {
		newRanges[newRanges.length - 1] = end - newRanges[newRanges.length - 2] + 1;
	}
	return newRanges;
}
StyleRange[] getStyleRanges(int start, int length, boolean includeRanges) {
    if (length == 0) {
        return null;
    }
	StyleRange[] newStyles;
	int end = start + length - 1;
	if (ranges != null) {
		int rangeCount = styleCount << 1;
		int rangeStart = getRangeIndex(start, -1, rangeCount);
        if (rangeStart >= rangeCount) {
            return null;
        }
        if (ranges[rangeStart] > end) {
            return null;
        }
		int rangeEnd = Math.min(rangeCount - 2, getRangeIndex(end, rangeStart - 1, rangeCount));
        if (ranges[rangeEnd] > end) {
            rangeEnd = Math.max(rangeStart, rangeEnd - 2);
        }
		newStyles = new StyleRange[((rangeEnd - rangeStart) >> 1) + 1];
		if (includeRanges) {
			for (int i = rangeStart, j = 0; i <= rangeEnd; i += 2, j++) {
				newStyles[j] = (StyleRange)styles[i >> 1].clone();
				newStyles[j].start = ranges[i];
				newStyles[j].length = ranges[i + 1];
			}
		} else {
			System.arraycopy(styles, rangeStart >> 1, newStyles, 0, newStyles.length);
		}
	} else {
		int rangeStart = getRangeIndex(start, -1, styleCount);
        if (rangeStart >= styleCount) {
            return null;
        }
        if (styles[rangeStart].start > end) {
            return null;
        }
		int rangeEnd = Math.min(styleCount - 1, getRangeIndex(end, rangeStart - 1, styleCount));
        if (styles[rangeEnd].start > end) {
            rangeEnd = Math.max(rangeStart, rangeEnd - 1);
        }
		newStyles = new StyleRange[rangeEnd - rangeStart + 1];
		System.arraycopy(styles, rangeStart, newStyles, 0, newStyles.length);
	}
	if (includeRanges || ranges == null) {
		StyleRange style = newStyles[0];
		if (start > style.start) {
			newStyles[0] = style = (StyleRange)style.clone();
			style.length = style.start + style.length - start;
			style.start = start;
		}
		style = newStyles[newStyles.length - 1];
		if (end < style.start + style.length - 1) {
			newStyles[newStyles.length - 1] = style = (StyleRange)style.clone();
			style.length = end - style.start + 1;
		}
	}
	return newStyles;
}
StyleRange getStyleRange(StyleRange style) {
    if (style.underline && style.underlineStyle == SWT.UNDERLINE_LINK) {
        hasLinks = true;
    }
    if (style.start == 0 && style.length == 0 && style.fontStyle == SWT.NORMAL) {
        return style;
    }
	StyleRange clone = (StyleRange)style.clone();
	clone.start = clone.length = 0;
	clone.fontStyle = SWT.NORMAL;
    if (clone.font == null) {
        clone.font = getFont(style.fontStyle);
    }
	return clone;
}
TextLayout getTextLayout(int lineIndex) {
	if (lineSpacingProvider == null) {
		return getTextLayout(lineIndex, styledText.getOrientation(), styledText.getWrapWidth(), styledText.lineSpacing);
	}
	// Compute line spacing for the given line index.
	int newLineSpacing = styledText.lineSpacing;
	Integer spacing = lineSpacingProvider.getLineSpacing(lineIndex);
	if (spacing != null && spacing.intValue() >= 0) {
		newLineSpacing = spacing;
	}
	// Check if line spacing has not changed
	if (isSameLineSpacing(lineIndex, newLineSpacing)) {
		return getTextLayout(lineIndex, styledText.getOrientation(), styledText.getWrapWidth(), newLineSpacing);
	}
	// Get text layout with original StyledText line spacing.
	TextLayout layout = getTextLayout(lineIndex, styledText.getOrientation(), styledText.getWrapWidth(),
			styledText.lineSpacing);
	if (layout.getSpacing() != newLineSpacing) {
		layout.setSpacing(newLineSpacing);
		if (lineSpacingComputing) {
			return layout;
		}
		try {
			/* Call of resetCache, setCaretLocation, redraw call getTextLayout method
			 * To avoid having stack overflow, lineSpacingComputing flag is used to call
			 * resetCache, setCaretLocation, redraw methods only at the end of the compute of all lines spacing.
			 */
			lineSpacingComputing = true;
			styledText.resetCache(lineIndex, 1);
			styledText.setCaretLocations();
			styledText.redraw();
		} finally {
			lineSpacingComputing = false;
		}
	}
	return layout;
}
boolean isSameLineSpacing(int lineIndex, int newLineSpacing) {
	if (layouts == null) {
		return false;
	}
	int layoutIndex = lineIndex - topIndex;
	if (0 <= layoutIndex && layoutIndex < layouts.length) {
		TextLayout layout = layouts[layoutIndex];
		return layout != null && !layout.isDisposed() && layout.getSpacing() == newLineSpacing;
	}
	return false;
}

private static final class StyleEntry {
	public final int start;
	public final int end;
	public final TextStyle style;

	public StyleEntry(TextStyle style, int start, int end) {
		this.style = style;
		this.start = start;
		this.end = end;
	}
}

TextLayout getTextLayout(int lineIndex, int orientation, int width, int lineSpacing) {
	layoutRequests++;
	TextLayout layout = null;
	if (styledText != null) {
		int topIndex = styledText.topIndex > 0 ? styledText.topIndex - 1 : 0;
		if (layouts == null || topIndex != this.topIndex) {
			TextLayout[] newLayouts = new TextLayout[CACHE_SIZE];
			if (layouts != null) {
				for (int i = 0; i < layouts.length; i++) {
					if (layouts[i] != null) {
						int layoutIndex = (i + this.topIndex) - topIndex;
						if (0 <= layoutIndex && layoutIndex < newLayouts.length) {
							newLayouts[layoutIndex] = layouts[i];
						} else {
							layouts[i].dispose();
						}
					}
				}
			}
			if (bullets != null && bulletsIndices != null && topIndex != this.topIndex) {
				int delta = topIndex - this.topIndex;
				if (delta > 0) {
					if (delta < bullets.length) {
						System.arraycopy(bullets, delta, bullets, 0, bullets.length - delta);
						System.arraycopy(bulletsIndices, delta, bulletsIndices, 0, bulletsIndices.length - delta);
					}
					int startIndex = Math.max(0, bullets.length - delta);
                    for (int i = startIndex; i < bullets.length; i++) {
                        bullets[i] = null;
                    }
				} else {
					if (-delta < bullets.length) {
						System.arraycopy(bullets, 0, bullets, -delta, bullets.length + delta);
						System.arraycopy(bulletsIndices, 0, bulletsIndices, -delta, bulletsIndices.length + delta);
					}
					int endIndex = Math.min(bullets.length, -delta);
                    for (int i = 0; i < endIndex; i++) {
                        bullets[i] = null;
                    }
				}
			}
			this.topIndex = topIndex;
			layouts = newLayouts;
		}
		if (layouts != null) {
			int layoutIndex = lineIndex - topIndex;
			if (0 <= layoutIndex && layoutIndex < layouts.length) {
				layout = layouts[layoutIndex];
				if (layout != null) {
					// Bug 520374: lineIndex can be >= linesSize.length
					if(lineIndex < this.lineCount && store.width(lineIndex) != UNMEASURED) {
						return layout;
					}
				} else {
					layout = layouts[layoutIndex] = new TextLayout(device);
				}
			}
		}
	}
    if (layout == null) {
        layout = new TextLayout(device);
    }
	layoutBuilds++;
	String line = content.getLine(lineIndex);
	int lineOffset = content.getOffsetAtLine(lineIndex);
	int[] segments = null;
	char[] segmentChars = null;
	int indent = 0;
	int wrapIndent = 0;
	int verticalIndent = 0;
	int alignment = SWT.LEFT;
	int textDirection = orientation;
	boolean justify = false;
	int[] tabs = {tabWidth};
	Bullet bullet = null;
	int[] ranges = null;
	StyleRange[] styles = null;
	int rangeStart = 0, styleCount = 0;
	StyledTextEvent event = null;
	if (styledText != null) {
		event = styledText.getBidiSegments(lineOffset, line);
		if (event != null) {
			segments = event.segments;
			segmentChars = event.segmentsChars;
		}
		event = styledText.getLineStyleData(lineOffset, line);
		indent = styledText.indent;
		wrapIndent = styledText.wrapIndent;
		alignment = styledText.alignment;
		if (styledText.isAutoDirection()) {
			textDirection = SWT.AUTO_TEXT_DIRECTION;
		} else if ((styledText.getStyle() & SWT.FLIP_TEXT_DIRECTION) != 0) {
			textDirection = orientation == SWT.RIGHT_TO_LEFT ? SWT.LEFT_TO_RIGHT : SWT.RIGHT_TO_LEFT;
		}
		justify = styledText.justify;
        if (styledText.tabs != null) {
            tabs = styledText.tabs;
        }
	}
	if (event != null) {
		indent = event.indent;
		verticalIndent = event.verticalIndent;
		wrapIndent = event.wrapIndent;
		alignment = event.alignment;
		justify = event.justify;
		bullet = event.bullet;
		ranges = event.ranges;
		styles = event.styles;
        if (event.tabStops != null) {
            tabs = event.tabStops;
        }
		if (styles != null) {
			styleCount = styles.length;
			if (styledText.isFixedLineHeight()) {
				for (int i = 0; i < styleCount; i++) {
					if (styles[i].isVariableHeight()) {
						styledText.hasStyleWithVariableHeight = true;
						styledText.verticalScrollOffset = -1;
						styledText.redraw();
						break;
					}
				}
			}
		}
		if (bullets == null || bulletsIndices == null) {
			bullets = new Bullet[CACHE_SIZE];
			bulletsIndices = new int[CACHE_SIZE];
		}
		int index = lineIndex - topIndex;
		if (0 <= index && index < CACHE_SIZE) {
			bullets[index] = bullet;
			bulletsIndices[index] = event.bulletIndex;
		}
	} else {
		if (store.hasAttributes()) {
			//FLLDCIM: MASKS - line attributes from the mask, patterns and interned ids
			indent = store.get(lineIndex, INDENT, indent);
			verticalIndent = store.get(lineIndex, VERTICAL_INDENT, verticalIndent);
			wrapIndent = store.get(lineIndex, WRAP_INDENT, wrapIndent);
			alignment = store.get(lineIndex, ALIGNMENT, alignment);
			if (store.find(lineIndex, JUSTIFY)) {
				justify = store.found != 0;
			}
			segments = (int[]) store.getObject(lineIndex, SEGMENTS, segments);
			segmentChars = (char[]) store.getObject(lineIndex, SEGMENT_CHARS, segmentChars);
			tabs = (int[]) store.getObject(lineIndex, TABSTOPS, tabs);
		}
		if (bulletsIndices != null) {
			bullets = null;
			bulletsIndices = null;
		}
		if (bullets != null) {
			for (Bullet b : bullets) {
				if (b.indexOf(lineIndex) != -1) {
					bullet = b;
					break;
				}
			}
		}
		ranges = this.ranges;
		styles = this.styles;
		styleCount = this.styleCount;
		if (ranges != null) {
			rangeStart = getRangeIndex(lineOffset, -1, styleCount << 1);
		} else {
			rangeStart = getRangeIndex(lineOffset, -1, styleCount);
		}
	}
	if (bullet != null) {
		StyleRange style = bullet.style;
		GlyphMetrics metrics = style.metrics;
		indent += metrics.width;
	}

	// prepare styles, as it may change the line content, do it before calling layout.setText()
	// This needs to happen early to handle the case of GlyphMetrics on \t.
	// The root cause is that TextLayout doesn't return the right value for the bounds when
	// GlyphMetrics are applied on \t. A better fix could be implemented directly in (all 3)
	// TextLayout classes.
	List<StyleEntry> styleEntries = new ArrayList<>();
	int lastOffset = 0;
	int length = line.length();
	if (styles != null) {
		if (ranges != null) {
			int rangeCount = styleCount << 1;
			for (int i = rangeStart; i < rangeCount; i += 2) {
				int start, end;
				if (lineOffset > ranges[i]) {
					start = 0;
					end = Math.min (length, ranges[i + 1] - lineOffset + ranges[i]);
				} else {
					start = ranges[i] - lineOffset;
					end = Math.min(length, start + ranges[i + 1]);
				}
                if (start >= length) {
                    break;
                }
				if (lastOffset < start) {
					styleEntries.add(new StyleEntry(null, lastOffset, start - 1));
				}
				TextStyle style = getStyleRange(styles[i >> 1]);
				int endIndex = Math.max(start, Math.min(length, end + 1));
				if (style.metrics != null && line.substring(start, endIndex).contains("\t")) {
					line =
						line.substring(0, start) +
						line.substring(start, endIndex).replace('\t', ' ') +
						(end < line.length() ? line.substring(end + 1, line.length()) : "");
				}
				styleEntries.add(new StyleEntry(style, start, end));
				lastOffset = Math.max(lastOffset, end);
			}
		} else {
			for (int i = rangeStart; i < styleCount; i++) {
				int start, end;
				if (lineOffset > styles[i].start) {
					start = 0;
					end = Math.min (length, styles[i].length - lineOffset + styles[i].start);
				} else {
					start = styles[i].start - lineOffset;
					end = Math.min(length, start + styles[i].length);
				}
                if (start >= length) {
                    break;
                }
				if (lastOffset < start) {
					styleEntries.add(new StyleEntry(null, lastOffset, start - 1));
				}
				TextStyle style = getStyleRange(styles[i]);
				int endIndex = Math.max(start, Math.min(length, end + 1));
				if (style.metrics != null && line.substring(start, endIndex).contains("\t")) {
					line =
						line.substring(0, start) +
						line.substring(start, endIndex).replace('\t', ' ') +
						(end < line.length() ? line.substring(end + 1, line.length()) : "");
				}
				styleEntries.add(new StyleEntry(style, start, end));
				lastOffset = Math.max(lastOffset, end);
			}
		}
	}
    if (lastOffset < length) {
        styleEntries.add(new StyleEntry(null, lastOffset, length));
    }

	layout.setFont(regularFont);
	layout.setAscent(ascent);
	layout.setDescent(descent);
	layout.setFixedLineMetrics(fixedLineMetrics);
	layout.setText(line);
	layout.setOrientation(orientation);
	layout.setSegments(segments);
	layout.setSegmentsChars(segmentChars);
	layout.setWidth(width);
	layout.setSpacing(lineSpacing);
	layout.setTabs(tabs);
	layout.setDefaultTabWidth(tabLength);
	layout.setIndent(indent);
	layout.setVerticalIndent(verticalIndent);
	layout.setWrapIndent(wrapIndent);
	layout.setAlignment(alignment);
	layout.setJustify(justify);
	layout.setTextDirection(textDirection);
	// apply styles, must be done after layout.setText()
	for (StyleEntry styleEntry : styleEntries) {
		layout.setStyle(styleEntry.style, styleEntry.start, styleEntry.end);
	}

	if (styledText != null && styledText.ime != null) {
		IME ime = styledText.ime;
		int compositionOffset = ime.getCompositionOffset();
		if (compositionOffset != -1 && compositionOffset <= content.getCharCount()) {
			int commitCount = ime.getCommitCount();
			int compositionLength = ime.getText().length();
			if (compositionLength != commitCount) {
				int compositionLine = content.getLineAtOffset(compositionOffset);
				if (compositionLine == lineIndex) {
					int[] imeRanges = ime.getRanges();
					TextStyle[] imeStyles = ime.getStyles();
					if (imeRanges.length > 0) {
						for (int i = 0; i < imeStyles.length; i++) {
							int start = imeRanges[i*2] - lineOffset;
							int end = imeRanges[i*2+1] - lineOffset;
							TextStyle imeStyle = imeStyles[i], userStyle;
							for (int j = start; j <= end; j++) {
                                if (!(0 <= j && j < length)) {
                                    break;
                                }
								userStyle = layout.getStyle(cap(layout, j));
                                if (userStyle == null && j > 0) {
                                    userStyle = layout.getStyle(cap(layout, j - 1));
                                }
                                if (userStyle == null && j + 1 < length) {
                                    userStyle = layout.getStyle(cap(layout, j + 1));
                                }
								if (userStyle == null) {
									layout.setStyle(imeStyle, j, j);
								} else {
									TextStyle newStyle = new TextStyle(imeStyle);
                                    if (newStyle.font == null) {
                                        newStyle.font = userStyle.font;
                                    }
                                    if (newStyle.foreground == null) {
                                        newStyle.foreground = userStyle.foreground;
                                    }
                                    if (newStyle.background == null) {
                                        newStyle.background = userStyle.background;
                                    }
									layout.setStyle(newStyle, j, j);
								}
							}
						}
					} else {
						int start = compositionOffset - lineOffset;
						int end = start + compositionLength - 1;
						TextStyle userStyle = layout.getStyle(cap(layout, start));
						if (userStyle == null) {
                            if (start > 0) {
                                userStyle = layout.getStyle(cap(layout, start - 1));
                            }
                            if (userStyle == null && end + 1 < length) {
                                userStyle = layout.getStyle(cap(layout, end + 1));
                            }
							if (userStyle != null) {
								TextStyle newStyle = new TextStyle();
								newStyle.font = userStyle.font;
								newStyle.foreground = userStyle.foreground;
								newStyle.background = userStyle.background;
								layout.setStyle(newStyle, start, end);
							}
						}
					}
				}
			}
		}
	}

	if (styledText != null && styledText.isFixedLineHeight()) {
		int index = -1;
		int lineCount = layout.getLineCount();
		int height = getLineHeight();
		for (int i = 0; i < lineCount; i++) {
			int lineHeight = layout.getLineBounds(i).height;
			if (lineHeight > height) {
				height = lineHeight;
				index = i;
			}
		}
		if (index != -1) {
			FontMetrics metrics = layout.getLineMetrics(index);
			ascent = metrics.getAscent() + metrics.getLeading();
			descent = metrics.getDescent();
			if (layouts != null) {
				for (TextLayout l : layouts) {
					if (l != null && l != layout) {
						l.setAscent(ascent);
						l.setDescent(descent);
					}
				}
			}
			styledText.calculateScrollBars();
			if (styledText.verticalScrollOffset != 0) {
				int topIndex = styledText.topIndex;
				int topIndexY = styledText.topIndexY;
				int lineHeight = getLineHeight();
				int newVerticalScrollOffset;
				if (topIndexY >= 0) {
					newVerticalScrollOffset = (topIndex - 1) * lineHeight + lineHeight - topIndexY;
				} else {
					newVerticalScrollOffset = topIndex * lineHeight - topIndexY;
				}
				styledText.scrollVertical(newVerticalScrollOffset - styledText.verticalScrollOffset, true);
			}
            if (styledText.isBidiCaret()) {
                styledText.createCaretBitmaps();
            }
			styledText.caretDirection = SWT.NULL;
			styledText.setCaretLocations();
			styledText.redraw();
		}
	}
	return layout;
}
/**
 * Estimated height of one line in viewport mode: the measured average line
 * height, or the wrap estimate from the average line length while nothing is
 * measured yet. Logical offsets of unmeasured regions are line * estimate.
 */
int getEstimatedLineHeight() {
	//FLLDCIM: VIEWPORT - sparse logical extent; reconciled as lines are measured
	int defaultLineHeight = getLineHeight();
	if (linesInAverageLineHeight > 0) {
		return Math.max(1, Math.round(averageLineHeight));
	}
	int width = styledText.getWrapWidth();
	if (width > 0 && lineCount > 0) {
		long averageChars = content.getCharCount() / lineCount;
		return (int) Math.min(Integer.MAX_VALUE, (averageChars * averageCharWidth / width + 1) * defaultLineHeight);
	}
	return Math.max(1, defaultLineHeight);
}
long getEstimatedOffset(int lineIndex) {
	return (long) lineIndex * getEstimatedLineHeight();
}
/** O(1) logical extent for large variable-height documents. */
int getEstimatedHeight(int defaultLineHeight) {
	return (int) Math.min(Integer.MAX_VALUE, getEstimatedOffset(lineCount));
}
int getWidth() {
	return maxWidth;
}
void reset() {
	if (layouts != null) {
		for (TextLayout layout : layouts) {
            if (layout != null) {
                layout.dispose();
            }
		}
		layouts = null;
	}
	topIndex = -1;
	stylesSetCount = styleCount = lineCount = 0;
	ranges = null;
	styles = null;
	stylesSet = null;
	store.reset(0);
	bullets = null;
	bulletsIndices = null;
	redrawLines = null;
	hasLinks = false;
}
void reset(int startLine, int lineCount) {
	if (startLine < 0 || startLine > this.lineCount) {
		return;
	}
	lineCount = Math.min(lineCount, this.lineCount - startLine);
	int endLine = startLine + lineCount;
	if (lineCount <= 0) {
		return;
	}
	//FLLDCIM: MASKS - a reset clears the size window in place (the window is bounded in viewport mode)
	store.resetSizes(startLine, endLine);
	discountAverage(lineCount);
	if (startLine <= maxWidthLineIndex && maxWidthLineIndex < endLine) {
		maxWidth = 0;
		maxWidthLineIndex = -1;
		if (lineCount != this.lineCount && !isViewportMode()) {
			// viewport mode: the width extent is rebuilt from the lines measured next
			recomputeMaxWidth();
		}
	}
}
private void discountAverage(int resetLineCount) {
	if (linesInAverageLineHeight > resetLineCount) {
		linesInAverageLineHeight -= resetLineCount;
	} else {
		linesInAverageLineHeight = 0;
		averageLineHeight = 0.0f;
	}
}
private void recomputeMaxWidth() {
	int[] widths = store.width;
	for (int i = 0, end = Math.min(widths.length, this.lineCount - store.sizeBase); i < end; i++) {
		if (widths[i] > maxWidth) {
			maxWidth = widths[i];
			maxWidthLineIndex = store.sizeBase + i;
		}
	}
}
void reset(Set<Integer> lines) {
    if (lines == null || lines.isEmpty()) {
        return;
    }
	int resetLineCount = 0;
	for (Integer line : lines) {
		if (line >= 0 || line < lineCount) {
			resetLineCount++;
			store.resetSize(line.intValue());
		}
	}
	discountAverage(resetLineCount);
	if (lines.contains(Integer.valueOf(maxWidthLineIndex))) {
		maxWidth = 0;
		maxWidthLineIndex = -1;
		if (resetLineCount != this.lineCount && !isViewportMode()) {
			recomputeMaxWidth();
		}
	}
}
/** Whether any style range changes line height; scans the store without copying it. */
boolean hasVariableHeightStyle() {
	if (styles == null) {
		return false;
	}
	int count = styleCount;
	for (int i = 0; i < count; i++) {
		if (styles[i] != null && styles[i].isVariableHeight()) {
			return true;
		}
	}
	return false;
}
void setContent(StyledTextContent content) {
	reset();
	// drop the growth caused by fallback fonts of the previous content
	ascent = fontAscent;
	descent = fontDescent;
	this.content = content;
	lineCount = content.getLineCount();
	store.reset(lineCount);
	syncSizeWindow();
	maxWidth = 0;
	maxWidthLineIndex = -1;
	reset(0, lineCount);
}

/**
 * See {@link TextLayout#setFixedLineMetrics}
 *
 * @since 3.125
 */
public void setFixedLineMetrics(FontMetrics metrics) {
	fixedLineMetrics = metrics;
}

void setFont(Font font, int tabs) {
	TextLayout layout = new TextLayout(device);
	layout.setFont(regularFont);
	tabLength = tabs;
	if (font != null) {
        if (boldFont != null) {
            boldFont.dispose();
        }
        if (italicFont != null) {
            italicFont.dispose();
        }
        if (boldItalicFont != null) {
            boldItalicFont.dispose();
        }
		boldFont = italicFont = boldItalicFont = null;
		regularFont = font;
		layout.setText("    ");
		layout.setFont(font);
		layout.setStyle(new TextStyle(getFont(SWT.NORMAL), null, null), 0, 0);
		layout.setStyle(new TextStyle(getFont(SWT.BOLD), null, null), 1, 1);
		layout.setStyle(new TextStyle(getFont(SWT.ITALIC), null, null), 2, 2);
		layout.setStyle(new TextStyle(getFont(SWT.BOLD | SWT.ITALIC), null, null), 3, 3);
		FontMetrics metrics = layout.getLineMetrics(0);
		ascent = fontAscent = metrics.getAscent() + metrics.getLeading();
		descent = fontDescent = metrics.getDescent();
		boldFont.dispose();
		italicFont.dispose();
		boldItalicFont.dispose();
		boldFont = italicFont = boldItalicFont = null;
	}
	layout.dispose();
	layout = new TextLayout(device);
	layout.setFont(regularFont);
	StringBuilder tabBuffer = new StringBuilder(tabs);
	for (int i = 0; i < tabs; i++) {
		tabBuffer.append(' ');
	}
	layout.setText(tabBuffer.toString());
	tabWidth = layout.getBounds().width;
	layout.dispose();
	if (styledText != null) {
		GC gc = new GC(styledText);
		averageCharWidth = (int) gc.getFontMetrics().getAverageCharacterWidth();
		fixedPitch = gc.stringExtent("l").x == gc.stringExtent("W").x; //$NON-NLS-1$ //$NON-NLS-2$
		gc.dispose();
	}
}
void setLineAlignment(int startLine, int count, int alignment) {
	store.set(startLine, count, ALIGNMENT, alignment);
}
void setLineBackground(int startLine, int count, Color background) {
	store.setObject(startLine, count, BACKGROUND, background);
}
void setLineBullet(int startLine, int count, Bullet bullet) {
	if (bulletsIndices != null) {
		bulletsIndices = null;
		bullets = null;
	}
	if (bullets == null) {
        if (bullet == null) {
            return;
        }
		bullets = new Bullet[1];
		bullets[0] = bullet;
	}
	int index = 0;
	while (index < bullets.length) {
        if (bullet == bullets[index]) {
            break;
        }
		index++;
	}
	if (bullet != null) {
		if (index == bullets.length) {
			Bullet[] newBulletsList = new Bullet[bullets.length + 1];
			System.arraycopy(bullets, 0, newBulletsList, 0, bullets.length);
			newBulletsList[index] = bullet;
			bullets = newBulletsList;
		}
		bullet.addIndices(startLine, count);
	} else {
		updateBullets(startLine, count, 0, false);
		styledText.redrawLinesBullet(redrawLines);
		redrawLines = null;
	}
}
void setLineIndent(int startLine, int count, int indent) {
	store.set(startLine, count, INDENT, indent);
}
void setLineVerticalIndent(int lineIndex, int verticalLineIndent) {
	int delta = verticalLineIndent - store.get(lineIndex, VERTICAL_INDENT, 0);
	store.set(lineIndex, 1, VERTICAL_INDENT, verticalLineIndent);
	int height = store.height(lineIndex);
	if (height != UNMEASURED) {
		store.setHeight(lineIndex, height + delta);
	}
}
void setLineWrapIndent(int startLine, int count, int wrapIndent) {
	store.set(startLine, count, WRAP_INDENT, wrapIndent);
}
void setLineJustify(int startLine, int count, boolean justify) {
	store.set(startLine, count, JUSTIFY, justify ? 1 : 0);
}
void setLineSegments(int startLine, int count, int[] segments) {
	store.setObject(startLine, count, SEGMENTS, segments);
}
void setLineSegmentChars(int startLine, int count, char[] segmentChars) {
	store.setObject(startLine, count, SEGMENT_CHARS, segmentChars);
}
void setLineTabStops(int startLine, int count, int[] tabStops) {
	store.setObject(startLine, count, TABSTOPS, tabStops);
}
void setLineSpacingProvider(StyledTextLineSpacingProvider lineSpacingProvider) {
	this.lineSpacingProvider = lineSpacingProvider;
}
void setStyleRanges (int[] newRanges, StyleRange[] newStyles) {
	if (newStyles == null) {
		stylesSetCount = styleCount = 0;
		ranges = null;
		styles = null;
		stylesSet = null;
		hasLinks = false;
		return;
	}
	if (newRanges == null && COMPACT_STYLES) {
		newRanges = new int[newStyles.length << 1];
		StyleRange[] tmpStyles = new StyleRange[newStyles.length];
        if (stylesSet == null) {
            stylesSet = new StyleRange[4];
        }
		for (int i = 0, j = 0; i < newStyles.length; i++) {
			StyleRange newStyle = newStyles[i];
			newRanges[j++] = newStyle.start;
			newRanges[j++] = newStyle.length;
			int index = 0;
			while (index < stylesSetCount) {
                if (stylesSet[index].similarTo(newStyle)) {
                    break;
                }
				index++;
			}
			if (index == stylesSetCount) {
				if (stylesSetCount == stylesSet.length) {
					StyleRange[] tmpStylesSet = new StyleRange[stylesSetCount + 4];
					System.arraycopy(stylesSet, 0, tmpStylesSet, 0, stylesSetCount);
					stylesSet = tmpStylesSet;
				}
				stylesSet[stylesSetCount++] = newStyle;
			}
			tmpStyles[i] = stylesSet[index];
		}
		newStyles = tmpStyles;
	}

	if (styleCount == 0) {
		if (newRanges != null) {
			ranges = new int[newRanges.length];
			System.arraycopy(newRanges, 0, ranges, 0, ranges.length);
		}
		styles = new StyleRange[newStyles.length];
		System.arraycopy(newStyles, 0, styles, 0, styles.length);
		styleCount = newStyles.length;
		return;
	}
	if (newRanges != null && ranges == null) {
		ranges = new int[styles.length << 1];
		for (int i = 0, j = 0; i < styleCount; i++) {
			ranges[j++] = styles[i].start;
			ranges[j++] = styles[i].length;
		}
	}
	if (newRanges == null && ranges != null) {
		newRanges = new int[newStyles.length << 1];
		for (int i = 0, j = 0; i < newStyles.length; i++) {
			newRanges[j++] = newStyles[i].start;
			newRanges[j++] = newStyles[i].length;
		}
	}
	if (ranges != null) {
		int rangeCount = styleCount << 1;
		int start = newRanges[0];
		int modifyStart = getRangeIndex(start, -1, rangeCount), modifyEnd;
		boolean insert = modifyStart == rangeCount;
		if (!insert) {
			int end = newRanges[newRanges.length - 2] + newRanges[newRanges.length - 1];
			modifyEnd = getRangeIndex(end, modifyStart - 1, rangeCount);
			insert = modifyStart == modifyEnd && ranges[modifyStart] >= end;
		}
		if (insert) {
			addMerge(newRanges, newStyles, newRanges.length, modifyStart, modifyStart);
			return;
		}
		modifyEnd = modifyStart;
		int[] mergeRanges = new int[6];
		StyleRange[] mergeStyles = new StyleRange[3];
		for (int i = 0; i < newRanges.length; i += 2) {
			int newStart = newRanges[i];
			int newEnd = newStart + newRanges[i + 1];
            if (newStart == newEnd) {
                continue;
            }
			int modifyLast = 0, mergeCount = 0;
			while (modifyEnd < rangeCount) {
                if (newStart >= ranges[modifyStart] + ranges[modifyStart + 1]) {
                    modifyStart += 2;
                }
                if (ranges[modifyEnd] + ranges[modifyEnd + 1] > newEnd) {
                    break;
                }
				modifyEnd += 2;
			}
			if (ranges[modifyStart] < newStart && newStart < ranges[modifyStart] + ranges[modifyStart + 1]) {
				mergeStyles[mergeCount >> 1] = styles[modifyStart >> 1];
				mergeRanges[mergeCount] = ranges[modifyStart];
				mergeRanges[mergeCount + 1] = newStart - ranges[modifyStart];
				mergeCount += 2;
			}
			mergeStyles[mergeCount >> 1] = newStyles[i >> 1];
			mergeRanges[mergeCount] = newStart;
			mergeRanges[mergeCount + 1] = newRanges[i + 1];
			mergeCount += 2;
			if (modifyEnd < rangeCount && ranges[modifyEnd] < newEnd && newEnd < ranges[modifyEnd] + ranges[modifyEnd + 1]) {
				mergeStyles[mergeCount >> 1] = styles[modifyEnd >> 1];
				mergeRanges[mergeCount] = newEnd;
				mergeRanges[mergeCount + 1] = ranges[modifyEnd] + ranges[modifyEnd + 1] - newEnd;
				mergeCount += 2;
				modifyLast = 2;
			}
			int grow = addMerge(mergeRanges, mergeStyles, mergeCount, modifyStart, modifyEnd + modifyLast);
			rangeCount += grow;
			modifyStart = modifyEnd += grow;
		}
	} else {
		int start = newStyles[0].start;
		int modifyStart = getRangeIndex(start, -1, styleCount), modifyEnd;
		boolean insert = modifyStart == styleCount;
		if (!insert) {
			int end = newStyles[newStyles.length - 1].start + newStyles[newStyles.length - 1].length;
			modifyEnd = getRangeIndex(end, modifyStart - 1, styleCount);
			insert = modifyStart == modifyEnd && styles[modifyStart].start >= end;
		}
		if (insert) {
			addMerge(newStyles, newStyles.length, modifyStart, modifyStart);
			return;
		}
		modifyEnd = modifyStart;
		StyleRange[] mergeStyles = new StyleRange[3];
		for (StyleRange newStyle : newStyles) {
			StyleRange style;
			int newStart = newStyle.start;
			int newEnd = newStart + newStyle.length;
            if (newStart == newEnd) {
                continue;
            }
			int modifyLast = 0, mergeCount = 0;
			while (modifyEnd < styleCount) {
                if (newStart >= styles[modifyStart].start + styles[modifyStart].length) {
                    modifyStart++;
                }
                if (styles[modifyEnd].start + styles[modifyEnd].length > newEnd) {
                    break;
                }
				modifyEnd++;
			}
			style = styles[modifyStart];
			if (style.start < newStart && newStart < style.start + style.length) {
				style = mergeStyles[mergeCount++] = (StyleRange)style.clone();
				style.length = newStart - style.start;
			}
			mergeStyles[mergeCount++] = newStyle;
			if (modifyEnd < styleCount) {
				style = styles[modifyEnd];
				if (style.start < newEnd && newEnd < style.start + style.length) {
					style = mergeStyles[mergeCount++] = (StyleRange)style.clone();
					style.length += style.start - newEnd;
					style.start = newEnd;
					modifyLast = 1;
				}
			}
			int grow = addMerge(mergeStyles, mergeCount, modifyStart, modifyEnd + modifyLast);
			modifyStart = modifyEnd += grow;
		}
	}
}
void textChanging(TextChangingEvent event) {
	int start = event.start;
	int newCharCount = event.newCharCount, replaceCharCount = event.replaceCharCount;
	int newLineCount = event.newLineCount, replaceLineCount = event.replaceLineCount;

	updateRanges(start, replaceCharCount, newCharCount);

	int startLine = content.getLineAtOffset(start);
    if (replaceCharCount == content.getCharCount()) {
        store.clearAttributes();
    }
	if (replaceLineCount == lineCount) {
		lineCount = store.lineCount = newLineCount;
		syncSizeWindow();
		store.resetAllSizes();
		reset(0, lineCount);
	} else {
		int startIndex = startLine + replaceLineCount + 1;
		int endIndex = startLine + newLineCount + 1;
		if(lineCount < startLine) {
			SWT.error(SWT.ERROR_INVALID_RANGE, null, "bug 478020: lineCount < startLine: " + lineCount + ":" + startLine);
		}
		if(lineCount < startIndex) {
			SWT.error(SWT.ERROR_INVALID_RANGE, null, "bug 478020: lineCount < startIndex: " + lineCount + ":" + startIndex);
		}
		int delta = newLineCount - replaceLineCount;
		//FLLDCIM: MASKS - sizes move inside the window; an edit that keeps the line count moves nothing
		store.shiftSizes(startLine, replaceLineCount, newLineCount);
		if (layouts != null) {
			int layoutStartLine = startLine - topIndex;
			int layoutEndLine = layoutStartLine + replaceLineCount + 1;
			for (int i = layoutStartLine; i < layoutEndLine; i++) {
				if (0 <= i && i < layouts.length) {
                    if (layouts[i] != null) {
                        layouts[i].dispose();
                    }
					layouts[i] = null;
                    if (bullets != null && bulletsIndices != null) {
                        bullets[i] = null;
                    }
				}
			}
			if (delta > 0) {
				for (int i = layouts.length - 1; i >= layoutEndLine; i--) {
					if (0 <= i && i < layouts.length) {
						endIndex = i + delta;
						if (0 <= endIndex && endIndex < layouts.length) {
							layouts[endIndex] = layouts[i];
							layouts[i] = null;
							if (bullets != null && bulletsIndices != null) {
								bullets[endIndex] = bullets[i];
								bulletsIndices[endIndex] = bulletsIndices[i];
								bullets[i] = null;
							}
						} else {
                            if (layouts[i] != null) {
                                layouts[i].dispose();
                            }
							layouts[i] = null;
                            if (bullets != null && bulletsIndices != null) {
                                bullets[i] = null;
                            }
						}
					}
				}
			} else if (delta < 0) {
				for (int i = layoutEndLine; i < layouts.length; i++) {
					if (0 <= i && i < layouts.length) {
						endIndex = i + delta;
						if (0 <= endIndex && endIndex < layouts.length) {
							layouts[endIndex] = layouts[i];
							layouts[i] = null;
							if (bullets != null && bulletsIndices != null) {
								bullets[endIndex] = bullets[i];
								bulletsIndices[endIndex] = bulletsIndices[i];
								bullets[i] = null;
							}
						} else {
                            if (layouts[i] != null) {
                                layouts[i].dispose();
                            }
							layouts[i] = null;
                            if (bullets != null && bulletsIndices != null) {
                                bullets[i] = null;
                            }
						}
					}
				}
			}
		}
		if (replaceLineCount != 0 || newLineCount != 0) {
			int startLineOffset = content.getOffsetAtLine(startLine);
            if (startLineOffset != start) {
                startLine++;
            }
			updateBullets(startLine, replaceLineCount, newLineCount, true);
			store.shiftAttributes(startLine, replaceLineCount, newLineCount);
		}
		lineCount += delta;
		store.lineCount = lineCount;
		if (delta != 0 && styledText != null && store.windowed != isViewportMode()) {
			syncSizeWindow();
		}
		if (maxWidthLineIndex != -1 && startLine <= maxWidthLineIndex && maxWidthLineIndex <= startLine + replaceLineCount) {
			maxWidth = 0;
			maxWidthLineIndex = -1;
			//FLLDCIM: VIEWPORT - no LineSizeInfo per line; viewport mode rebuilds the extent from lines measured next
			if (!isViewportMode()) {
				recomputeMaxWidth();
			}
		}
	}
}
void updateBullets(int startLine, int replaceLineCount, int newLineCount, boolean update) {
    if (bullets == null) {
        return;
    }
    if (bulletsIndices != null) {
        return;
    }
	for (Bullet bullet : bullets) {
		int[] lines = bullet.removeIndices(startLine, replaceLineCount, newLineCount, update);
		if (lines != null) {
			if (redrawLines == null) {
				redrawLines = lines;
			} else {
				int[] newRedrawBullets = new int[redrawLines.length + lines.length];
				System.arraycopy(redrawLines, 0, newRedrawBullets, 0, redrawLines.length);
				System.arraycopy(lines, 0, newRedrawBullets, redrawLines.length, lines.length);
				redrawLines = newRedrawBullets;
			}
		}
	}
	int removed = 0;
	for (Bullet bullet : bullets) {
        if (bullet.size() == 0) {
            removed++;
        }
	}
	if (removed > 0) {
		if (removed == bullets.length) {
			bullets = null;
		} else {
			Bullet[] newBulletsList = new Bullet[bullets.length - removed];
			for (int i = 0, j = 0; i < bullets.length; i++) {
				Bullet bullet = bullets[i];
                if (bullet.size() > 0) {
                    newBulletsList[j++] = bullet;
                }
			}
			bullets = newBulletsList;
		}
	}
}
void updateRanges(int start, int replaceCharCount, int newCharCount) {
    if (styleCount == 0 || (replaceCharCount == 0 && newCharCount == 0)) {
        return;
    }
	if (ranges != null) {
		int rangeCount = styleCount << 1;
		int modifyStart = getRangeIndex(start, -1, rangeCount);
        if (modifyStart == rangeCount) {
            return;
        }
		int end = start + replaceCharCount;
		int modifyEnd = getRangeIndex(end, modifyStart - 1, rangeCount);
		int offset = newCharCount - replaceCharCount;
		if (modifyStart == modifyEnd && ranges[modifyStart] < start && end < ranges[modifyEnd] + ranges[modifyEnd + 1]) {
			if (newCharCount == 0) {
				ranges[modifyStart + 1] -= replaceCharCount;
				modifyEnd += 2;
			} else {
				if (rangeCount + 2 > ranges.length) {
					int[] newRanges = new int[grownCapacity(ranges.length, ranges.length + (GROW << 1), 4)];
					System.arraycopy(ranges, 0, newRanges, 0, rangeCount);
					ranges = newRanges;
					StyleRange[] newStyles = new StyleRange[Math.max(styles.length + GROW, ranges.length >> 1)];
					System.arraycopy(styles, 0, newStyles, 0, styleCount);
					styles = newStyles;
				}
				System.arraycopy(ranges, modifyStart + 2, ranges, modifyStart + 4, rangeCount - (modifyStart + 2));
				System.arraycopy(styles, (modifyStart + 2) >> 1, styles, (modifyStart + 4) >> 1, styleCount - ((modifyStart + 2) >> 1));
				ranges[modifyStart + 3] = ranges[modifyStart] + ranges[modifyStart + 1] - end;
				ranges[modifyStart + 2] = start + newCharCount;
				ranges[modifyStart + 1] = start - ranges[modifyStart];
				styles[(modifyStart >> 1) + 1] = styles[modifyStart >> 1];
				rangeCount += 2;
				styleCount++;
				modifyEnd += 4;
			}
			if (offset != 0) {
				for (int i = modifyEnd; i < rangeCount; i += 2) {
					ranges[i] += offset;
				}
			}
		} else {
			if (ranges[modifyStart] < start && start < ranges[modifyStart] + ranges[modifyStart + 1]) {
				ranges[modifyStart + 1] = start - ranges[modifyStart];
				modifyStart += 2;
			}
			if (modifyEnd < rangeCount && ranges[modifyEnd] < end && end < ranges[modifyEnd] + ranges[modifyEnd + 1]) {
				ranges[modifyEnd + 1] = ranges[modifyEnd] + ranges[modifyEnd + 1] - end;
				ranges[modifyEnd] = end;
			}
			if (offset != 0) {
				for (int i = modifyEnd; i < rangeCount; i += 2) {
					ranges[i] += offset;
				}
			}
			System.arraycopy(ranges, modifyEnd, ranges, modifyStart, rangeCount - modifyEnd);
			System.arraycopy(styles, modifyEnd >> 1, styles, modifyStart >> 1, styleCount - (modifyEnd >> 1));
			styleCount -= (modifyEnd - modifyStart) >> 1;
		}
	} else {
		int modifyStart = getRangeIndex(start, -1, styleCount);
        if (modifyStart == styleCount) {
            return;
        }
		int end = start + replaceCharCount;
		int modifyEnd = getRangeIndex(end, modifyStart - 1, styleCount);
		int offset = newCharCount - replaceCharCount;
		if (modifyStart == modifyEnd && styles[modifyStart].start < start && end < styles[modifyEnd].start + styles[modifyEnd].length) {
			if (newCharCount == 0) {
				styles[modifyStart].length -= replaceCharCount;
				modifyEnd++;
			} else {
				if (styleCount + 1 > styles.length) {
					StyleRange[] newStyles = new StyleRange[grownCapacity(styles.length, styles.length + GROW, 4)];
					System.arraycopy(styles, 0, newStyles, 0, styleCount);
					styles = newStyles;
				}
				System.arraycopy(styles, modifyStart + 1, styles, modifyStart + 2, styleCount - (modifyStart + 1));
				styles[modifyStart + 1] = (StyleRange)styles[modifyStart].clone();
				styles[modifyStart + 1].length = styles[modifyStart].start + styles[modifyStart].length - end;
				styles[modifyStart + 1].start = start + newCharCount;
				styles[modifyStart].length = start - styles[modifyStart].start;
				styleCount++;
				modifyEnd += 2;
			}
			if (offset != 0) {
				for (int i = modifyEnd; i < styleCount; i++) {
					styles[i].start += offset;
				}
			}
		} else {
			if (styles[modifyStart].start < start && start < styles[modifyStart].start + styles[modifyStart].length) {
				styles[modifyStart].length = start - styles[modifyStart].start;
				modifyStart++;
			}
			if (modifyEnd < styleCount && styles[modifyEnd].start < end && end < styles[modifyEnd].start + styles[modifyEnd].length) {
				styles[modifyEnd].length = styles[modifyEnd].start + styles[modifyEnd].length - end;
				styles[modifyEnd].start = end;
			}
			if (offset != 0) {
				for (int i = modifyEnd; i < styleCount; i++) {
					styles[i].start += offset;
				}
			}
			System.arraycopy(styles, modifyEnd, styles, modifyStart, styleCount - modifyEnd);
			styleCount -= modifyEnd - modifyStart;
		}
	}
}

public boolean hasVerticalIndent() {
	return store.anyNonZero(VERTICAL_INDENT);
}


}
