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

import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.gtk.*;
import org.eclipse.swt.*;

public final class TextLayout {

	final public static int MOVEMENT_CHAR = 1;
	final public static int MOVEMENT_CLUSTER = 2;
	final public static int MOVEMENT_WORD = 4;
	
	static class StyleItem {
		TextStyle style;
		int start;
	}
	
	Device device;
	Font font;
	String text;
	int[] segments;
	StyleItem[] styles;
	int /*long*/ layout, context, attrList;
	
	static final char LTR_MARK = '\u200E', RTL_MARK = '\u200F';

public TextLayout (Device device) {
	if (device == null) device = Device.getDevice();
	if (device == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	this.device = device;
	context = OS.gdk_pango_context_get();
	if (context == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	layout = OS.pango_layout_new(context);
	if (layout == 0) SWT.error(SWT.ERROR_NO_HANDLES);	
	OS.pango_layout_set_wrap(layout, OS.PANGO_WRAP_WORD_CHAR);
	OS.pango_layout_set_tabs(layout, device.emptyTab);		
	OS.gdk_pango_context_set_colormap(context, OS.gdk_colormap_get_system());
	text = "";
	styles = new StyleItem[2];
	styles[0] = new StyleItem();
	styles[1] = new StyleItem();
	if (device.tracking) device.new_Object(this);
}

void checkLayout() {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
}

void computeRuns () {
	if (attrList != 0) return;
	byte[] buffer = Converter.wcsToMbcs(null, getSegmentsText(), false);
	OS.pango_layout_set_text (layout, buffer, buffer.length);
	if (styles.length == 2 && styles[0].style == null) return;
	int /*long*/ ptr = OS.pango_layout_get_text(layout);
	attrList = OS.pango_attr_list_new();	
	PangoAttribute attribute = new PangoAttribute();	
	for (int i = 0; i < styles.length - 1; i++) {
		StyleItem styleItem = styles[i];
		TextStyle style = styleItem.style; 
		if (style == null) continue;
		int start = translateOffset(styleItem.start);
		int end = translateOffset(styles[i+1].start - 1);
		int byteStart = (int)/*64*/(OS.g_utf8_offset_to_pointer(ptr, start) - ptr);
		int byteEnd = (int)/*64*/(OS.g_utf8_offset_to_pointer(ptr, end + 1) - ptr);
		Font font = style.font;
		if (font != null && !font.isDisposed()) {
			int /*long*/ attr = OS.pango_attr_font_desc_new (font.handle);
			OS.memmove (attribute, attr, PangoAttribute.sizeof);
			attribute.start_index = byteStart;
			attribute.end_index = byteEnd;
			OS.memmove (attr, attribute, PangoAttribute.sizeof);
			OS.pango_attr_list_insert(attrList, attr);
		}
		Color foreground = style.foreground;
		if (foreground != null && !foreground.isDisposed()) {
			GdkColor fg = foreground.handle;
			int /*long*/ attr = OS.pango_attr_foreground_new(fg.red, fg.green, fg.blue);
			OS.memmove (attribute, attr, PangoAttribute.sizeof);
			attribute.start_index = byteStart;
			attribute.end_index = byteEnd;
			OS.memmove (attr, attribute, PangoAttribute.sizeof);
			OS.pango_attr_list_insert(attrList, attr);
		}
		Color background = style.background;
		if (background != null && !background.isDisposed()) {
			GdkColor bg = background.handle;
			int /*long*/ attr = OS.pango_attr_background_new(bg.red, bg.green, bg.blue);
			OS.memmove (attribute, attr, PangoAttribute.sizeof);
			attribute.start_index = byteStart;
			attribute.end_index = byteEnd;
			OS.memmove (attr, attribute, PangoAttribute.sizeof);
			OS.pango_attr_list_insert(attrList, attr);
		}
	}
	OS.pango_layout_set_attributes(layout, attrList);
}

public void dispose() {
	if (layout == 0) return;
	font = null;
	text = null;
	freeRuns();
	if (layout != 0) OS.g_object_unref(layout);
	layout = 0;
	if (context != 0) OS.g_object_unref(context);
	context = 0;
	if (device.tracking) device.dispose_Object(this);
	device = null;
}

public void draw(GC gc, int x, int y) {
	checkLayout ();
	computeRuns();
	if (gc == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (gc.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	OS.gdk_draw_layout(gc.data.drawable, gc.handle, x, y, layout);
}

public void draw(GC gc, int x, int y, int selectionStart, int selectionEnd, Color selectionForeground, Color selectionBackground) {
	checkLayout ();
	computeRuns();
	if (gc == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (gc.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	if (selectionForeground != null && selectionForeground.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	if (selectionBackground != null && selectionBackground.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	int length = text.length();
	if (length == 0) return;
	boolean hasSelection = selectionStart <= selectionEnd && selectionStart != -1 && selectionEnd != -1;
	if (!hasSelection) {
		OS.gdk_draw_layout(gc.data.drawable, gc.handle, x, y, layout);
	} else {
		selectionStart = Math.min(Math.max(0, selectionStart), length - 1);
		selectionEnd = Math.min(Math.max(0, selectionEnd), length - 1);
		length = (int)/*64*/OS.g_utf8_strlen(OS.pango_layout_get_text(layout), -1);
		selectionStart = translateOffset(selectionStart);
		selectionEnd = translateOffset(selectionEnd);
		if (selectionForeground == null) selectionForeground = device.getSystemColor(SWT.COLOR_LIST_SELECTION_TEXT);
		if (selectionBackground == null) selectionBackground = device.getSystemColor(SWT.COLOR_LIST_SELECTION);
		boolean fullSelection = selectionStart == 0 && selectionEnd == length - 1;
		if (fullSelection) {
			OS.gdk_draw_layout_with_colors(gc.data.drawable, gc.handle, x, y, layout, selectionForeground.handle, selectionBackground.handle);
		} else {
			int /*long*/ ptr = OS.pango_layout_get_text(layout);
			Region clipping = new Region();
			gc.getClipping(clipping);
			int byteSelStart = (int)/*64*/(OS.g_utf8_offset_to_pointer(ptr, selectionStart) - ptr);
			int byteSelEnd = (int)/*64*/(OS.g_utf8_offset_to_pointer(ptr, selectionEnd + 1) - ptr);
			OS.gdk_draw_layout(gc.data.drawable, gc.handle, x, y, layout);
			int[] ranges = new int[]{byteSelStart, byteSelEnd};
			int /*long*/ rgn = OS.gdk_pango_layout_get_clip_region(layout, x, y, ranges, ranges.length / 2);
			if (rgn != 0) {
				OS.gdk_gc_set_clip_region(gc.handle, rgn);
				OS.gdk_region_destroy(rgn);
			}
			OS.gdk_draw_layout_with_colors(gc.data.drawable, gc.handle, x, y, layout, selectionForeground.handle, selectionBackground.handle);
			gc.setClipping(clipping);
		}
	}
}

void freeRuns() {
	if (attrList == 0) return;
	OS.pango_layout_set_attributes(layout, 0);
	OS.pango_attr_list_unref(attrList);
	attrList = 0;
}

public int getAlignment() {
	checkLayout();
	int align = OS.pango_layout_get_alignment(layout);
	switch (align) {
		case OS.PANGO_ALIGN_CENTER: return SWT.CENTER;
		case OS.PANGO_ALIGN_RIGHT: return SWT.RIGHT;
	}
	return SWT.LEFT;
}

public Rectangle getBounds() {
	checkLayout();
	computeRuns();
	int[] w = new int[1], h = new int[1];
	OS.pango_layout_get_size(layout, w, h);
	int wrapWidth = OS.pango_layout_get_width(layout);
	int width = OS.PANGO_PIXELS(wrapWidth != -1 ? wrapWidth : w[0]);
	int height = OS.PANGO_PIXELS(h[0]);
	return new Rectangle(0, 0, width, height);
}

public Rectangle getBounds(int start, int end) {
	checkLayout();
	computeRuns();
	int length = text.length();
	if (length == 0) return new Rectangle(0, 0, 0, 0);
	if (start > end) return new Rectangle(0, 0, 0, 0);
	start = Math.min(Math.max(0, start), length - 1);
	end = Math.min(Math.max(0, end), length - 1);
	start = translateOffset(start);
	end = translateOffset(end);
	int /*long*/ ptr = OS.pango_layout_get_text(layout);
	int byteStart = (int)/*64*/(OS.g_utf8_offset_to_pointer (ptr, start) - ptr);
	int byteEnd = (int)/*64*/(OS.g_utf8_offset_to_pointer (ptr, end + 1) - ptr);
	int[] ranges = new int[]{byteStart, byteEnd};
	int /*long*/ clipRegion = OS.gdk_pango_layout_get_clip_region(layout, 0, 0, ranges, 1);
	if (clipRegion == 0) return new Rectangle(0, 0, 0, 0);
	GdkRectangle rect = new GdkRectangle();
	OS.gdk_region_get_clipbox(clipRegion, rect);
	OS.gdk_region_destroy(clipRegion);
	return new Rectangle(rect.x, rect.y, rect.width, rect.height);
}

public Font getFont () {
	checkLayout();
	return font;
}

public int getLevel(int offset) {
	checkLayout();
	computeRuns();
	int length = text.length();
	if (!(0 <= offset && offset <= length)) SWT.error(SWT.ERROR_INVALID_RANGE);
	offset = translateOffset(offset);
	int /*long*/ iter = OS.pango_layout_get_iter(layout);
	if (iter == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	int level = 0;
	PangoItem item = new PangoItem();
	PangoLayoutRun run = new PangoLayoutRun();
	int /*long*/ ptr = OS.pango_layout_get_text(layout);
	int /*long*/ byteOffset = OS.g_utf8_offset_to_pointer(ptr, offset) - ptr;
	while (OS.pango_layout_iter_next_run(iter)) {
		int /*long*/ runPtr = OS.pango_layout_iter_get_run(iter);
		if (runPtr != 0) {
			OS.memmove(run, runPtr, PangoLayoutRun.sizeof);
			OS.memmove(item, run.item, PangoItem.sizeof);
			if (item.offset <= byteOffset && byteOffset < item.offset + item.length) {
				level = item.analysis_level;
				break;
			}
		}
	}
	OS.pango_layout_iter_free(iter);
	return level;
}

public Rectangle getLineBounds(int lineIndex) {
	checkLayout();
	computeRuns();
	int lineCount = OS.pango_layout_get_line_count(layout);
	if (!(0 <= lineIndex && lineIndex < lineCount)) SWT.error(SWT.ERROR_INVALID_RANGE);
	int /*long*/ iter = OS.pango_layout_get_iter(layout);
	if (iter == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	for (int i = 0; i < lineIndex; i++) OS.pango_layout_iter_next_line(iter);
	PangoRectangle rect = new PangoRectangle();
	OS.pango_layout_iter_get_line_extents(iter, null, rect);
	OS.pango_layout_iter_free(iter);
	int x = OS.PANGO_PIXELS(rect.x);
	int y = OS.PANGO_PIXELS(rect.y);
	int width = OS.PANGO_PIXELS(rect.width);
	int height = OS.PANGO_PIXELS(rect.height);
	return new Rectangle(x, y, width, height);
}

public int getLineCount() {
	checkLayout ();
	computeRuns();
	return OS.pango_layout_get_line_count(layout);
}

public int getLineIndex(int offset) {
	checkLayout ();
	computeRuns();
	int length = text.length();
	if (!(0 <= offset && offset <= length)) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	offset = translateOffset(offset);
	int line = 0;
	int /*long*/ ptr = OS.pango_layout_get_text(layout);
	int /*long*/ byteOffset = OS.g_utf8_offset_to_pointer(ptr,offset) - ptr;
	int /*long*/ iter = OS.pango_layout_get_iter(layout);
	if (iter == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	while (OS.pango_layout_iter_next_line(iter)) {
		if (OS.pango_layout_iter_get_index(iter) > byteOffset) break;
		line++;
	}
	OS.pango_layout_iter_free(iter);
	return line;
}

public FontMetrics getLineMetrics (int lineIndex) {
	checkLayout ();
	computeRuns();
	int lineCount = OS.pango_layout_get_line_count(layout);
	if (!(0 <= lineIndex && lineIndex < lineCount)) SWT.error(SWT.ERROR_INVALID_RANGE);
	int /*long*/ font = this.font != null ? this.font.handle : device.getSystemFont().handle;
	int /*long*/ lang = OS.pango_context_get_language(context);
	int ascent = 0, descent = 0, averageCharWidth = 0, height = 0;
	int /*long*/ metrics = OS.pango_context_get_metrics(context, font, lang);
	if (text.length() == 0) {
		ascent = OS.pango_font_metrics_get_ascent(metrics);
		descent = OS.pango_font_metrics_get_descent(metrics);
		averageCharWidth = OS.pango_font_metrics_get_approximate_char_width(metrics);
		height = ascent + descent;
	} else {
		PangoLayoutLine line = new PangoLayoutLine();
		OS.memmove(line, OS.pango_layout_get_line(layout, lineIndex), PangoLayoutLine.sizeof);
		int /*long*/ runs = line.runs;
		PangoItem item = new PangoItem();
		PangoLayoutRun run = new PangoLayoutRun();
		int runCount = 0;
		while (runs != 0) {
			OS.memmove(run, OS.g_slist_data(runs), PangoLayoutRun.sizeof);
			OS.memmove(item, run.item, PangoItem.sizeof);
			int /*long*/ runMetrics = metrics;
			if (item.analysis_font != 0) {
				runMetrics = OS.pango_font_get_metrics(item.analysis_font, item.analysis_language);
			}
			int runAscent = OS.pango_font_metrics_get_ascent(runMetrics);
			int runDescent = OS.pango_font_metrics_get_descent(runMetrics);
			ascent = Math.max(ascent, runAscent);
			descent = Math.max(descent, runDescent);
			averageCharWidth += OS.pango_font_metrics_get_approximate_char_width(runMetrics);
			height = Math.max(height, runAscent + runDescent);
			if (metrics != runMetrics) OS.pango_font_metrics_unref(runMetrics);
			runCount++;
			runs = OS.g_slist_next(runs);
		}
		averageCharWidth = averageCharWidth / runCount;
	}
	OS.pango_font_metrics_unref(metrics);
	ascent = OS.PANGO_PIXELS(ascent);
	descent = OS.PANGO_PIXELS(descent);
	averageCharWidth = OS.PANGO_PIXELS(averageCharWidth);
	height = OS.PANGO_PIXELS(height);
	return FontMetrics.gtk_new(ascent, descent, averageCharWidth, 0, height);
}

public Point getLineOffsets(int lineIndex) {
	checkLayout ();
	computeRuns();
	int lineCount = OS.pango_layout_get_line_count(layout);
	if (!(0 <= lineIndex && lineIndex < lineCount)) SWT.error(SWT.ERROR_INVALID_RANGE);
	PangoLayoutLine line = new PangoLayoutLine();
	OS.memmove(line, OS.pango_layout_get_line(layout, lineIndex), PangoLayoutLine.sizeof);
	int /*long*/ ptr = OS.pango_layout_get_text(layout);
	int start = (int)/*64*/OS.g_utf8_pointer_to_offset(ptr, ptr + line.start_index), end;
	if (lineIndex < lineCount - 1) {
		OS.memmove(line, OS.pango_layout_get_line(layout, lineIndex + 1), PangoLayoutLine.sizeof);
		end = (int)/*64*/OS.g_utf8_pointer_to_offset(ptr, ptr + line.start_index) - 1; 
	} else {
		end = text.length() - 1; 
	}
	return new Point(start, Math.max(start, end));
}

public Point getLocation(int offset, int trailing) {
	checkLayout();
	computeRuns();
	int length = text.length();
	if (!(0 <= offset && offset <= length)) SWT.error(SWT.ERROR_INVALID_RANGE);
	offset = translateOffset(offset);
	int /*long*/ ptr = OS.pango_layout_get_text(layout);
	int byteOffset = (int)/*64*/(OS.g_utf8_offset_to_pointer(ptr, offset) - ptr); 
	PangoRectangle pos = new PangoRectangle();
	OS.pango_layout_index_to_pos(layout, byteOffset, pos);
	int x = (trailing & SWT.TRAIL) != 0 ? pos.x + pos.width : pos.x;
	int y = pos.y;
	return new Point(OS.PANGO_PIXELS(x), OS.PANGO_PIXELS(y));
}

public int getNextOffset (int offset, int movement) {
	return _getOffset(offset, movement, true);
}

int _getOffset (int offset, int movement, boolean forward) {
	checkLayout();
	int length = text.length();
	if (!(0 <= offset && offset <= length)) SWT.error(SWT.ERROR_INVALID_RANGE);
	if (forward) {
		if (offset == length) return length;
	} else {
		if (offset == 0) return 0;
	}
	int step = forward ? 1 : -1;
	if ((movement & MOVEMENT_CHAR) != 0) return offset + step;
	int /*long*/[] attrs = new int /*long*/[1];
	int[] nAttrs = new int[1];
	OS.pango_layout_get_log_attrs(layout, attrs, nAttrs);
	if (attrs[0] == 0) return offset + step;
	length = (int)/*64*/OS.g_utf8_strlen(OS.pango_layout_get_text(layout), -1);
	offset = translateOffset(offset);
	PangoLogAttr logAttr = new PangoLogAttr();
	offset += step;
	if (segments != null && segments.length > 2) {
		for (int j = 0; j < segments.length; j++) {
			if (translateOffset(segments[j]) - 1 == offset) {
				offset += step;
				break;
			}
		}
	}
	while (0 < offset && offset < length) {
		OS.memmove(logAttr, attrs[0] + offset * PangoLogAttr.sizeof, PangoLogAttr.sizeof);
		if (((movement & MOVEMENT_CLUSTER) != 0) && logAttr.is_cursor_position) break; 
		if (((movement & MOVEMENT_WORD) != 0) && (logAttr.is_word_start || logAttr.is_sentence_end)) break;
		offset += step;
		if (segments != null && segments.length > 2) {
			for (int j = 0; j < segments.length; j++) {
				if (translateOffset(segments[j]) - 1 == offset) {
					offset += step;
					break;
				}
			}
		}
	}
	OS.g_free(attrs[0]);
	return Math.min(Math.max(0, untranslateOffset(offset)), text.length());
}

public int getOffset(Point point, int[] trailing) {
	checkLayout();
	if (point == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	return getOffset(point.x, point.y, trailing);
}

public int getOffset(int x, int y, int[] trailing) {
	checkLayout();
	computeRuns();
	if (trailing != null && trailing.length < 1) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	int[] index = new int[1];
	int[] piTrailing = new int[1];
	OS.pango_layout_xy_to_index(layout, x * OS.PANGO_SCALE, y * OS.PANGO_SCALE, index, piTrailing);
	int /*long*/ ptr = OS.pango_layout_get_text(layout);
	int offset = (int)/*64*/OS.g_utf8_pointer_to_offset(ptr, ptr + index[0]);
	if (trailing != null) trailing[0] = piTrailing[0] == 0 ? SWT.LEAD : SWT.TRAIL;
	return untranslateOffset(offset);
}

public int getOrientation() {
	checkLayout();
	int baseDir = OS.pango_context_get_base_dir(context);
	return baseDir == OS.PANGO_DIRECTION_RTL ? SWT.RIGHT_TO_LEFT : SWT.LEFT_TO_RIGHT;
}

public int getPreviousOffset (int index, int movement) {
	return _getOffset(index, movement, false);
}

public int[] getSegments() {
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
	char separator = getOrientation() == SWT.RIGHT_TO_LEFT ? RTL_MARK : LTR_MARK;
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

public int getSpacing () {
	checkLayout();	
	return OS.PANGO_PIXELS(OS.pango_layout_get_spacing(layout));
}

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

public int[] getTabs() {
	checkLayout();
	int /*long*/ tabArray = OS.pango_layout_get_tabs(layout);
	if (tabArray == 0) return null;
	int nTabs = OS.pango_tab_array_get_size(tabArray);
	int[] tabs = new int[nTabs];
	if (nTabs > 0) {
		int /*long*/[] locations = new int /*long*/[1];
		OS.pango_tab_array_get_tabs(tabArray, null, locations);
		if (locations[0] != 0) {
			OS.memmove(tabs, locations[0], nTabs * 4);
			OS.g_free(locations[0]);
		}
	}
	OS.pango_tab_array_free(tabArray);
	return tabs;
}

public String getText () {
	checkLayout ();
	return text;
}

public int getWidth () {
	checkLayout ();
	return OS.PANGO_PIXELS(OS.pango_layout_get_width(layout));
}

public boolean isDisposed () {
	return layout == 0;
}

public void setAlignment (int alignment) {
	checkLayout();
	int mask = SWT.LEFT | SWT.CENTER | SWT.RIGHT;
	alignment &= mask;
	if (alignment == 0) return;
	if ((alignment & SWT.LEFT) != 0) alignment = SWT.LEFT; 
	if ((alignment & SWT.RIGHT) != 0) alignment = SWT.RIGHT; 
	int align = OS.PANGO_ALIGN_LEFT;
	switch (alignment) {
		case SWT.CENTER: align = OS.PANGO_ALIGN_CENTER; break;
		case SWT.RIGHT: align = OS.PANGO_ALIGN_RIGHT; break;
	}
	OS.pango_layout_set_alignment(layout, align);
}

public void setFont (Font font) {
	checkLayout ();
	if (font != null && font.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	if (this.font == font) return;
	if (font != null && font.equals(this.font)) return;
	this.font = font;
	OS.pango_layout_set_font_description(layout, font != null ? font.handle : 0);
}

public void setOrientation(int orientation) {
	checkLayout();
	int mask = SWT.RIGHT_TO_LEFT | SWT.LEFT_TO_RIGHT;
	orientation &= mask;
	if (orientation == 0) return;
	if ((orientation & SWT.LEFT_TO_RIGHT) != 0) orientation = SWT.LEFT_TO_RIGHT;
	int baseDir = orientation == SWT.RIGHT_TO_LEFT ? OS.PANGO_DIRECTION_RTL : OS.PANGO_DIRECTION_LTR;
	if (OS.pango_context_get_base_dir(context) == baseDir) return;
	OS.pango_context_set_base_dir(context, baseDir);
	OS.pango_layout_context_changed(layout);
}

public void setSpacing (int spacing) {
	checkLayout();
	if (spacing < 0) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	OS.pango_layout_set_spacing(layout, spacing * OS.PANGO_SCALE);
}

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

public void setStyle (TextStyle style, int start, int end) {
	checkLayout();
	int length = text.length();
	if (length == 0) return;
	if (start > end) return;
	start = Math.min(Math.max(0, start), length - 1);
	end = Math.min(Math.max(0, end), length - 1);
	
	/*
	* Bug in Pango. Pango 1.2.2 will cause a segmentation fault if a style
	* is not applied for a whole ligature.  The fix is to applied the
	* style for the whole ligature.
	* 
	* NOTE that fix only LamAlef ligatures. 
	*/
	if (start > 0 && isAlef(text.charAt(start)) && isLam(text.charAt(start - 1))) {
		start--;
	}
	if (end < length - 1 && isLam(text.charAt(end)) && isAlef(text.charAt(end + 1))) {
		end++;
	}

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
			if (style == item.style) return;
			if (style != null && style.equals(item.style)) return;
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

public void setTabs(int[] tabs) {
	checkLayout();
	if (tabs == null) {
		OS.pango_layout_set_tabs(layout, device.emptyTab);
	} else {
		int /*long*/ tabArray = OS.pango_tab_array_new(tabs.length, true);
		if (tabArray != 0) {
			for (int i = 0; i < tabs.length; i++) {
				OS.pango_tab_array_set_tab(tabArray, i, OS.PANGO_TAB_LEFT, tabs[i]);
			}
			OS.pango_layout_set_tabs(layout, tabArray);
			OS.pango_tab_array_free(tabArray);
		}		
	}
}

public void setText (String text) {
	checkLayout ();
	if (text == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (text.equals(this.text)) return;
	freeRuns();
	this.text = text;
	styles = new StyleItem[2];
	styles[0] = new StyleItem();
	styles[1] = new StyleItem();
	styles[styles.length - 1].start = text.length();
}

public void setWidth (int width) {
	checkLayout ();
	if (width < -1 || width == 0) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	OS.pango_layout_set_width(layout, width == -1 ? -1 : width * OS.PANGO_SCALE);
}

static final boolean isLam(int ch) {
	return ch == 0x0644;
}

static final boolean isAlef(int ch) {
	switch (ch) {
		case 0x0622:
		case 0x0623:
		case 0x0625:
		case 0x0627:
		case 0x0649:
		case 0x0670:
		case 0x0671:
		case 0x0672:
		case 0x0673:
		case 0x0675:
			return true;
	}
	return false;
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
