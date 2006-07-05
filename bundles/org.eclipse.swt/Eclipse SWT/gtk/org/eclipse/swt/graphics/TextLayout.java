/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
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
import org.eclipse.swt.internal.cairo.*;
import org.eclipse.swt.internal.gtk.*;
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
 *  @since 3.0
 */
public final class TextLayout extends Resource {
	
	static class StyleItem {
		TextStyle style;
		int start;

		public String toString () {
			return "StyleItem {" + start + ", " + style + "}";
		}
	}

	Font font;
	String text;
	int ascent, descent;
	int[] segments;
	int[] tabs;
	StyleItem[] styles;
	int /*long*/ layout, context, attrList;
	int[] invalidOffsets;
	static final char LTR_MARK = '\u200E', RTL_MARK = '\u200F', ZWS = '\u200B', ZWNBS = '\uFEFF';

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
	context = OS.gdk_pango_context_get();
	if (context == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	OS.pango_context_set_language(context, OS.gtk_get_default_language());
	OS.pango_context_set_base_dir(context, OS.PANGO_DIRECTION_LTR);
	OS.gdk_pango_context_set_colormap(context, OS.gdk_colormap_get_system());
	layout = OS.pango_layout_new(context);
	if (layout == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	OS.pango_layout_set_wrap(layout, OS.PANGO_WRAP_WORD_CHAR);
	OS.pango_layout_set_tabs(layout, device.emptyTab);
	if (OS.GTK_VERSION >= OS.VERSION(2, 4, 0)) {
		OS.pango_layout_set_auto_dir(layout, false);
	}
	text = "";
	ascent = descent = -1;
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
	String segmentsText = getSegmentsText();
	byte[] buffer = Converter.wcsToMbcs(null, segmentsText, false);
	OS.pango_layout_set_text (layout, buffer, buffer.length);
	if (styles.length == 2 && styles[0].style == null && ascent == -1 && descent == -1 && segments == null) return;
	int /*long*/ ptr = OS.pango_layout_get_text(layout);
	attrList = OS.pango_attr_list_new();	
	PangoAttribute attribute = new PangoAttribute();
	char[] chars = null;
	int segementsLength = segmentsText.length();
	if ((ascent != -1  || descent != -1) && segementsLength > 0) {
		int /*long*/ iter = OS.pango_layout_get_iter(layout);
		if (iter == 0) SWT.error(SWT.ERROR_NO_HANDLES);
		PangoRectangle rect = new PangoRectangle();
		if (ascent != -1) rect.y =  -(ascent  * OS.PANGO_SCALE);
		rect.height = (Math.max(0, ascent) + Math.max(0, descent)) * OS.PANGO_SCALE;
		int lineCount = OS.pango_layout_get_line_count(layout);
		chars = new char[segementsLength + lineCount * 2];
		int oldPos = 0, count = 0;
		do {
			int bytePos = OS.pango_layout_iter_get_index(iter);
			/* Note: The length in bytes of ZWS and ZWNBS are both equals to 3 */
			int offset = count * 6;
			int /*long*/ attr = OS.pango_attr_shape_new (rect, rect);
			OS.memmove (attribute, attr, PangoAttribute.sizeof);
			attribute.start_index = bytePos + offset;
			attribute.end_index = bytePos + offset + 3;
			OS.memmove (attr, attribute, PangoAttribute.sizeof);
			OS.pango_attr_list_insert(attrList, attr);
			attr = OS.pango_attr_shape_new (rect, rect);
			OS.memmove (attribute, attr, PangoAttribute.sizeof);
			attribute.start_index = bytePos + offset + 3;
			attribute.end_index = bytePos + offset + 6;
			OS.memmove (attr, attribute, PangoAttribute.sizeof);
			OS.pango_attr_list_insert(attrList, attr);			
			int pos = (int)/*64*/OS.g_utf8_pointer_to_offset(ptr, ptr + bytePos);
			chars[pos + count * 2] = ZWS;
			chars[pos + count * 2 + 1] = ZWNBS;
			segmentsText.getChars(oldPos, pos, chars,  oldPos + count * 2);
			oldPos = pos;
			count++;
		} while (OS.pango_layout_iter_next_line(iter));
		OS.pango_layout_iter_free (iter);
		segmentsText.getChars(oldPos, segementsLength, chars,  oldPos + count * 2);
		buffer = Converter.wcsToMbcs(null, chars, false);
		OS.pango_layout_set_text (layout, buffer, buffer.length);
		ptr = OS.pango_layout_get_text(layout);
	} else {
		chars = new char[segementsLength];
		segmentsText.getChars(0, segementsLength, chars, 0);
	}
	int offsetCount = 0;
	for (int i = 0; i < chars.length; i++) {
		char c = chars[i];
		if (c == LTR_MARK || c == RTL_MARK || c == ZWNBS || c == ZWS) {
			offsetCount++;
		}
	}
	invalidOffsets = new int[offsetCount];
	offsetCount = 0;
	for (int i = 0; i < chars.length; i++) {
		char c = chars[i];
		if (c == LTR_MARK || c == RTL_MARK || c == ZWNBS || c == ZWS) {
			invalidOffsets[offsetCount++] = i;
		}
	}
	int strlen = OS.strlen(ptr);
	for (int i = 0; i < styles.length - 1; i++) {
		StyleItem styleItem = styles[i];
		TextStyle style = styleItem.style; 
		if (style == null) continue;
		int start = translateOffset(styleItem.start);
		int end = translateOffset(styles[i+1].start - 1);
		int byteStart = (int)/*64*/(OS.g_utf8_offset_to_pointer(ptr, start) - ptr);
		int byteEnd = (int)/*64*/(OS.g_utf8_offset_to_pointer(ptr, end + 1) - ptr);
		byteStart = Math.min(byteStart, strlen);
		byteEnd = Math.min(byteEnd, strlen);
		Font font = style.font;
		if (font != null && !font.isDisposed()) {
			int /*long*/ attr = OS.pango_attr_font_desc_new (font.handle);
			OS.memmove (attribute, attr, PangoAttribute.sizeof);
			attribute.start_index = byteStart;
			attribute.end_index = byteEnd;
			OS.memmove (attr, attribute, PangoAttribute.sizeof);
			OS.pango_attr_list_insert(attrList, attr);
		}
		if (style.underline) {
			int /*long*/ attr = OS.pango_attr_underline_new(OS.PANGO_UNDERLINE_SINGLE);
			OS.memmove(attribute, attr, PangoAttribute.sizeof);
			attribute.start_index = byteStart;
			attribute.end_index = byteEnd;
			OS.memmove(attr, attribute, PangoAttribute.sizeof);
			OS.pango_attr_list_insert(attrList, attr);
		}
		if (style.strikeout) {
			int /*long*/ attr = OS.pango_attr_strikethrough_new(true);
			OS.memmove(attribute, attr, PangoAttribute.sizeof);
			attribute.start_index = byteStart;
			attribute.end_index = byteEnd;
			OS.memmove(attr, attribute, PangoAttribute.sizeof);
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
		GlyphMetrics metrics = style.metrics;
		if (metrics != null) {
			PangoRectangle rect = new PangoRectangle();
			rect.y =  -(metrics.ascent * OS.PANGO_SCALE);
			rect.height = (metrics.ascent + metrics.descent) * OS.PANGO_SCALE;
			rect.width = metrics.width * OS.PANGO_SCALE;
			int /*long*/ attr = OS.pango_attr_shape_new (rect, rect);
			OS.memmove (attribute, attr, PangoAttribute.sizeof);
			attribute.start_index = byteStart;
			attribute.end_index = byteEnd;
			OS.memmove (attr, attribute, PangoAttribute.sizeof);
			OS.pango_attr_list_insert(attrList, attr);
		}
		int rise = style.rise;
		if (rise != 0) {
			int /*long*/ attr = OS.pango_attr_rise_new (rise * OS.PANGO_SCALE);
			OS.memmove (attribute, attr, PangoAttribute.sizeof);
			attribute.start_index = byteStart;
			attribute.end_index = byteEnd;
			OS.memmove (attr, attribute, PangoAttribute.sizeof);
			OS.pango_attr_list_insert(attrList, attr);
		}
	}
	OS.pango_layout_set_attributes(layout, attrList);
}

/**
 * Disposes of the operating system resources associated with
 * the text layout. Applications must dispose of all allocated text layouts.
 */
public void dispose() {
	if (layout == 0) return;
	font = null;
	text = null;
	styles = null;
	freeRuns();
	if (layout != 0) OS.g_object_unref(layout);
	layout = 0;
	if (context != 0) OS.g_object_unref(context);
	context = 0;
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
public void draw(GC gc, int x, int y) {
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
public void draw(GC gc, int x, int y, int selectionStart, int selectionEnd, Color selectionForeground, Color selectionBackground) {
	checkLayout ();
	computeRuns();
	if (gc == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (gc.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	if (selectionForeground != null && selectionForeground.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	if (selectionBackground != null && selectionBackground.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	gc.checkGC(GC.FOREGROUND);
	int length = text.length();
	if (length == 0) return;
	boolean hasSelection = selectionStart <= selectionEnd && selectionStart != -1 && selectionEnd != -1;
	GCData data = gc.data;
	int /*long*/ cairo = data.cairo;
	if (!hasSelection) {
		if (cairo != 0 && OS.GTK_VERSION >= OS.VERSION(2, 8, 0)) {
			Cairo.cairo_move_to(cairo, x, y);
			OS.pango_cairo_show_layout(cairo, layout);
		} else {
			OS.gdk_draw_layout(data.drawable, gc.handle, x, y, layout);
		}
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
			if (cairo != 0 && OS.GTK_VERSION >= OS.VERSION(2, 8, 0)) {
				int /*long*/ ptr = OS.pango_layout_get_text(layout);
				drawWithCairo(cairo, x, y, 0, OS.strlen(ptr), fullSelection, selectionBackground.handle, selectionForeground.handle);
			} else {
				OS.gdk_draw_layout_with_colors(data.drawable, gc.handle, x, y, layout, selectionForeground.handle, selectionBackground.handle);
			}
		} else {
			int /*long*/ ptr = OS.pango_layout_get_text(layout);
			int byteSelStart = (int)/*64*/(OS.g_utf8_offset_to_pointer(ptr, selectionStart) - ptr);
			int byteSelEnd = (int)/*64*/(OS.g_utf8_offset_to_pointer(ptr, selectionEnd + 1) - ptr);
			int strlen = OS.strlen(ptr);
			byteSelStart = Math.min(byteSelStart, strlen);
			byteSelEnd = Math.min(byteSelEnd, strlen);
			if (cairo != 0 && OS.GTK_VERSION >= OS.VERSION(2, 8, 0)) {
				drawWithCairo(cairo, x, y, byteSelStart, byteSelEnd, fullSelection, selectionBackground.handle, selectionForeground.handle);
			} else {
				Region clipping = new Region();
				gc.getClipping(clipping);
				OS.gdk_draw_layout(data.drawable, gc.handle, x, y, layout);
				int[] ranges = new int[]{byteSelStart, byteSelEnd};
				int /*long*/ rgn = OS.gdk_pango_layout_get_clip_region(layout, x, y, ranges, ranges.length / 2);
				if (rgn != 0) {
					OS.gdk_gc_set_clip_region(gc.handle, rgn);
					OS.gdk_region_destroy(rgn);
				}
				OS.gdk_draw_layout_with_colors(data.drawable, gc.handle, x, y, layout, selectionForeground.handle, selectionBackground.handle);
				gc.setClipping(clipping);
				clipping.dispose();
			}
		}
	}
}

void drawWithCairo(int /*long*/ cairo, int x, int y, int byteSelStart, int byteSelEnd, boolean fullSelection, GdkColor selectionBackground, GdkColor selectionForeground) {
	Cairo.cairo_save(cairo);
	if (!fullSelection) {
		Cairo.cairo_move_to(cairo, x, y);
		OS.pango_cairo_show_layout(cairo, layout);
	}
	int[] ranges = new int[]{byteSelStart, byteSelEnd};
	int /*long*/ rgn = OS.gdk_pango_layout_get_clip_region(layout, x, y, ranges, ranges.length / 2);
	if (rgn != 0) {
		OS.gdk_cairo_region(cairo, rgn);
		Cairo.cairo_clip(cairo);
		OS.gdk_cairo_set_source_color(cairo, selectionBackground);
		Cairo.cairo_paint(cairo);
		OS.gdk_region_destroy(rgn);
	}
	OS.gdk_cairo_set_source_color(cairo, selectionForeground);
	Cairo.cairo_move_to(cairo, x, y);
	OS.pango_cairo_show_layout(cairo, layout);
	Cairo.cairo_restore(cairo);
}

void freeRuns() {
	if (attrList == 0) return;
	OS.pango_layout_set_attributes(layout, 0);
	OS.pango_attr_list_unref(attrList);
	attrList = 0;
	invalidOffsets = null;
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
public int getAlignment() {
	checkLayout();
	int align = OS.pango_layout_get_alignment(layout);
	switch (align) {
		case OS.PANGO_ALIGN_CENTER: return SWT.CENTER;
		case OS.PANGO_ALIGN_RIGHT: return SWT.RIGHT;
	}
	return SWT.LEFT;
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
public Rectangle getBounds() {
	checkLayout();
	computeRuns();
	int[] w = new int[1], h = new int[1];
	OS.pango_layout_get_size(layout, w, h);
	int wrapWidth = OS.pango_layout_get_width(layout);
	int width = OS.PANGO_PIXELS(wrapWidth != -1 ? wrapWidth : w[0]);
	int height = OS.PANGO_PIXELS(h[0]);
	if (ascent != -1 && descent != -1) {
		height = Math.max (height, ascent + descent);
	}
	return new Rectangle(0, 0, width, height);
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
	int strlen = OS.strlen(ptr);
	byteStart = Math.min(byteStart, strlen);
	byteEnd = Math.min(byteEnd, strlen);
	int[] ranges = new int[]{byteStart, byteEnd};
	int /*long*/ clipRegion = OS.gdk_pango_layout_get_clip_region(layout, 0, 0, ranges, 1);
	if (clipRegion == 0) return new Rectangle(0, 0, 0, 0);
	GdkRectangle rect = new GdkRectangle();
	
	/* 
	* Bug in Pango. The region returned by gdk_pango_layout_get_clip_region()
	* includes areas from lines outside of the requested range.  The fix
	* is to subtract these areas from the clip region.
	*/
	PangoRectangle pangoRect = new PangoRectangle();
	int /*long*/ iter = OS.pango_layout_get_iter(layout);
	if (iter == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	int /*long*/ linesRegion = OS.gdk_region_new();
	if (linesRegion == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	int lineEnd = 0;
	do {
		OS.pango_layout_iter_get_line_extents(iter, null, pangoRect);
		if (OS.pango_layout_iter_next_line(iter)) {
			lineEnd = OS.pango_layout_iter_get_index(iter) - 1;
		} else {
			lineEnd = strlen;
		}
		if (byteStart > lineEnd) continue;
		rect.x = OS.PANGO_PIXELS(pangoRect.x);
		rect.y = OS.PANGO_PIXELS(pangoRect.y);
		rect.width = OS.PANGO_PIXELS(pangoRect.width);
		rect.height = OS.PANGO_PIXELS(pangoRect.height);
		OS.gdk_region_union_with_rect(linesRegion, rect);
	} while (lineEnd + 1 <= byteEnd);
	OS.gdk_region_intersect(clipRegion, linesRegion);
	OS.gdk_region_destroy(linesRegion);
	OS.pango_layout_iter_free(iter);
	
	OS.gdk_region_get_clipbox(clipRegion, rect);
	OS.gdk_region_destroy(clipRegion);
	return new Rectangle(rect.x, rect.y, rect.width, rect.height);
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
	return OS.PANGO_PIXELS(OS.pango_layout_get_indent(layout));
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
	return OS.pango_layout_get_justify(layout);
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
	int strlen = OS.strlen(ptr);
	byteOffset = Math.min(byteOffset, strlen);
	do {
		int /*long*/ runPtr = OS.pango_layout_iter_get_run(iter);
		if (runPtr != 0) {
			OS.memmove(run, runPtr, PangoLayoutRun.sizeof);
			OS.memmove(item, run.item, PangoItem.sizeof);
			if (item.offset <= byteOffset && byteOffset < item.offset + item.length) {
				level = item.analysis_level;
				break;
			}
		}
	} while (OS.pango_layout_iter_next_run(iter));
	OS.pango_layout_iter_free(iter);
	return level;
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
	if (ascent != -1 && descent != -1) {
		height = Math.max (height, ascent + descent);
	}
	return new Rectangle(x, y, width, height);
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
public int getLineCount() {
	checkLayout ();
	computeRuns();
	return OS.pango_layout_get_line_count(layout);
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
public int getLineIndex(int offset) {
	checkLayout ();
	computeRuns();
	int length = text.length();
	if (!(0 <= offset && offset <= length)) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	offset = translateOffset(offset);
	int line = 0;
	int /*long*/ ptr = OS.pango_layout_get_text(layout);
	int /*long*/ byteOffset = OS.g_utf8_offset_to_pointer(ptr,offset) - ptr;
	int strlen = OS.strlen(ptr);
	byteOffset = Math.min(byteOffset, strlen);
	int /*long*/ iter = OS.pango_layout_get_iter(layout);
	if (iter == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	while (OS.pango_layout_iter_next_line(iter)) {
		if (OS.pango_layout_iter_get_index(iter) > byteOffset) break;
		line++;
	}
	OS.pango_layout_iter_free(iter);
	return line;
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
	checkLayout ();
	computeRuns();
	int lineCount = OS.pango_layout_get_line_count(layout);
	if (!(0 <= lineIndex && lineIndex < lineCount)) SWT.error(SWT.ERROR_INVALID_RANGE);
	int ascent = 0, descent = 0;
	PangoLayoutLine line = new PangoLayoutLine();
	OS.memmove(line, OS.pango_layout_get_line(layout, lineIndex), PangoLayoutLine.sizeof);
	if (line.runs == 0) {
		int /*long*/ font = this.font != null ? this.font.handle : device.systemFont.handle;
		int /*long*/ lang = OS.pango_context_get_language(context);
		int /*long*/ metrics = OS.pango_context_get_metrics(context, font, lang);
		ascent = OS.pango_font_metrics_get_ascent(metrics);
		descent = OS.pango_font_metrics_get_descent(metrics);
		OS.pango_font_metrics_unref(metrics);
	} else {
		PangoRectangle rect = new PangoRectangle();
		OS.pango_layout_line_get_extents(OS.pango_layout_get_line(layout, lineIndex), null, rect);
		ascent = -rect.y;
		descent = rect.height - ascent;
	}
	ascent = Math.max(this.ascent, OS.PANGO_PIXELS(ascent));
	descent = Math.max(this.descent, OS.PANGO_PIXELS(descent));
	return FontMetrics.gtk_new(ascent, descent, 0, 0, ascent + descent);
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
public int[] getLineOffsets() {
	checkLayout();
	computeRuns();
	int lineCount = OS.pango_layout_get_line_count(layout);
	int[] offsets = new int [lineCount + 1];
	int /*long*/ ptr = OS.pango_layout_get_text(layout);
	int /*long*/ iter = OS.pango_layout_get_iter(layout);
	if (iter == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	int i = 0;
	do {
		int bytePos = OS.pango_layout_iter_get_index(iter);
		int pos = (int)/*64*/OS.g_utf8_pointer_to_offset(ptr, ptr + bytePos);
		offsets[i++] = untranslateOffset(pos);
	} while (OS.pango_layout_iter_next_line(iter));
	OS.pango_layout_iter_free(iter);
	offsets[lineCount] = text.length();						 
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
public Point getLocation(int offset, boolean trailing) {
	checkLayout();
	computeRuns();
	int length = text.length();
	if (!(0 <= offset && offset <= length)) SWT.error(SWT.ERROR_INVALID_RANGE);
	offset = translateOffset(offset);
	int /*long*/ ptr = OS.pango_layout_get_text(layout);
	int byteOffset = (int)/*64*/(OS.g_utf8_offset_to_pointer(ptr, offset) - ptr);
	int strlen = OS.strlen(ptr);
	byteOffset = Math.min(byteOffset, strlen);
	PangoRectangle pos = new PangoRectangle();
	OS.pango_layout_index_to_pos(layout, byteOffset, pos);
	int x = trailing ? pos.x + pos.width : pos.x;
	int y = pos.y;
	return new Point(OS.PANGO_PIXELS(x), OS.PANGO_PIXELS(y));
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
	return _getOffset(offset, movement, true);
}

int _getOffset (int offset, int movement, boolean forward) {
	checkLayout();
	computeRuns();
	int length = text.length();
	if (!(0 <= offset && offset <= length)) SWT.error(SWT.ERROR_INVALID_RANGE);
	if (forward) {
		if (offset == length) return length;
	} else {
		if (offset == 0) return 0;
	}
	int step = forward ? 1 : -1;
	if ((movement & SWT.MOVEMENT_CHAR) != 0) return offset + step;
	int /*long*/[] attrs = new int /*long*/[1];
	int[] nAttrs = new int[1];
	OS.pango_layout_get_log_attrs(layout, attrs, nAttrs);
	if (attrs[0] == 0) return offset + step;
	length = (int)/*64*/OS.g_utf8_strlen(OS.pango_layout_get_text(layout), -1);
	offset = translateOffset(offset);
	PangoLogAttr logAttr = new PangoLogAttr();
	offset = validateOffset(offset, step);
	while (0 < offset && offset < length) {
		OS.memmove(logAttr, attrs[0] + offset * PangoLogAttr.sizeof, PangoLogAttr.sizeof);
		if (((movement & SWT.MOVEMENT_CLUSTER) != 0) && logAttr.is_cursor_position) break; 
		if (((movement & SWT.MOVEMENT_WORD) != 0) && (logAttr.is_word_start || logAttr.is_sentence_end)) break;
		offset = validateOffset(offset, step);
	}
	OS.g_free(attrs[0]);
	return Math.min(Math.max(0, untranslateOffset(offset)), text.length());
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
public int getOffset(Point point, int[] trailing) {
	checkLayout();
	if (point == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	return getOffset(point.x, point.y, trailing);
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
public int getOffset(int x, int y, int[] trailing) {
	checkLayout();
	computeRuns();
	if (trailing != null && trailing.length < 1) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	
	/*
	* Feature in GTK.  pango_layout_xy_to_index() returns the 
	* logical end/start offset of a line when the coordinates are outside 
	* the line bounds. In SWT the correct behavior is to return the closest 
	* visual offset. The fix is to clamp the coordinates inside the  
	* line bounds.
	*/
	int /*long*/ iter = OS.pango_layout_get_iter(layout);
	if (iter == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	PangoRectangle rect = new PangoRectangle();
	do {
		OS.pango_layout_iter_get_line_extents(iter, null, rect);
		rect.y = OS.PANGO_PIXELS(rect.y);
		rect.height = OS.PANGO_PIXELS(rect.height);
		if (rect.y <= y && y < rect.y + rect.height) {
			rect.x = OS.PANGO_PIXELS(rect.x);
			rect.width = OS.PANGO_PIXELS(rect.width);
			if (x >= rect.x + rect.width) x = rect.x + rect.width - 1;
			if (x < rect.x) x = rect.x;
			break;
		}
	} while (OS.pango_layout_iter_next_line(iter));
	OS.pango_layout_iter_free(iter);
	
	int[] index = new int[1];
	int[] piTrailing = new int[1];
	OS.pango_layout_xy_to_index(layout, x * OS.PANGO_SCALE, y * OS.PANGO_SCALE, index, piTrailing);
	int /*long*/ ptr = OS.pango_layout_get_text(layout);
	int offset = (int)/*64*/OS.g_utf8_pointer_to_offset(ptr, ptr + index[0]);
	if (trailing != null) trailing[0] = piTrailing[0];
	return untranslateOffset(offset);
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
public int getOrientation() {
	checkLayout();
	int baseDir = OS.pango_context_get_base_dir(context);
	return baseDir == OS.PANGO_DIRECTION_RTL ? SWT.RIGHT_TO_LEFT : SWT.LEFT_TO_RIGHT;
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
public int getPreviousOffset (int index, int movement) {
	return _getOffset(index, movement, false);
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
	int[] result = new int[styles.length * 2];
	int count = 0;
	for (int i=0; i<styles.length - 1; i++) {
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
	return OS.PANGO_PIXELS(OS.pango_layout_get_spacing(layout));
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
		StyleItem item = styles[i];
		if (item.start > offset) {
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
	TextStyle[] result = new TextStyle[styles.length];
	int count = 0;
	for (int i=0; i<styles.length; i++) {
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
public int[] getTabs() {
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
	checkLayout ();
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
	checkLayout ();
	int width = OS.pango_layout_get_width(layout);
	return width != -1 ? OS.PANGO_PIXELS(width) : -1;
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
	return layout == 0;
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
	int align = OS.PANGO_ALIGN_LEFT;
	switch (alignment) {
		case SWT.CENTER: align = OS.PANGO_ALIGN_CENTER; break;
		case SWT.RIGHT: align = OS.PANGO_ALIGN_RIGHT; break;
	}
	OS.pango_layout_set_alignment(layout, align);
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
public void setAscent (int ascent) {
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
public void setDescent (int descent) {
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
	checkLayout ();
	if (font != null && font.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	if (this.font == font) return;
	if (font != null && font.equals(this.font)) return;
	this.font = font;
	OS.pango_layout_set_font_description(layout, font != null ? font.handle : 0);
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
	OS.pango_layout_set_indent(layout, indent * OS.PANGO_SCALE);
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
	OS.pango_layout_set_justify(layout, justify);
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
	OS.pango_layout_set_spacing(layout, spacing * OS.PANGO_SCALE);
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
		if (styles[index + 1].start > start) {
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
	int modifyStart = high;
	int modifyEnd = modifyStart;
	while (modifyEnd < styles.length) {
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
			StyleItem[] newStyles = new StyleItem[styles.length + 2];
			System.arraycopy(styles, 0, newStyles, 0, modifyStart + 1);
			StyleItem item = new StyleItem();
			item.start = start;
			item.style = style;
			newStyles[modifyStart + 1] = item;	
			item = new StyleItem();
			item.start = end + 1;
			item.style = styles[modifyStart].style;
			newStyles[modifyStart + 2] = item;
			System.arraycopy(styles, modifyEnd + 1, newStyles, modifyEnd + 3, styles.length - modifyEnd - 1);
			styles = newStyles;
			return;
		}
	}
	if (start == styles[modifyStart].start) modifyStart--;
	if (end == styles[modifyEnd + 1].start - 1) modifyEnd++;
	int newLength = styles.length + 1 - (modifyEnd - modifyStart - 1);
	StyleItem[] newStyles = new StyleItem[newLength];
	System.arraycopy(styles, 0, newStyles, 0, modifyStart + 1);	
	StyleItem item = new StyleItem();
	item.start = start;
	item.style = style;
	newStyles[modifyStart + 1] = item;
	styles[modifyEnd].start = end + 1;
	System.arraycopy(styles, modifyEnd, newStyles, modifyStart + 2, styles.length - modifyEnd);
	styles = newStyles;
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
public void setTabs(int[] tabs) {
	checkLayout();
	if (this.tabs == null && tabs == null) return;
	if (this.tabs!= null && tabs != null) {
		if (this.tabs.length == tabs.length) {
			int i;
			for (i = 0; i <tabs.length; i++) {
				if (this.tabs[i] != tabs[i]) break;
			}
			if (i == tabs.length) return;
		}
	}
	this.tabs = tabs;
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
	/*
	* Bug in Pango. A change in the tab stop array is not automatically reflected in the
	* pango layout object because the call pango_layout_set_tabs() does not free the 
	* lines cache. The fix to use pango_layout_context_changed() to free the lines cache.
	*/
	OS.pango_layout_context_changed(layout);
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
	checkLayout ();
	if (width < -1 || width == 0) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	freeRuns();
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

/**
 * Returns a string containing a concise, human-readable
 * description of the receiver.
 *
 * @return a string representation of the receiver
 */
public String toString () {
	if (isDisposed()) return "TextLayout {*DISPOSED*}";
	return "TextLayout {" + layout + "}";
}

/*
 *  Translate a client offset to an internal offset
 */
int translateOffset(int offset) {
	int length = text.length();
	if (length == 0) return offset;
	if (invalidOffsets == null) return offset;
	for (int i = 0; i < invalidOffsets.length; i++) {
		if (offset < invalidOffsets[i]) break; 
		offset++;
	}
	return offset;
}

/*
 *  Translate an internal offset to a client offset
 */
int untranslateOffset(int offset) {
	int length = text.length();
	if (length == 0) return offset;
	if (invalidOffsets == null) return offset;
	for (int i = 0; i < invalidOffsets.length; i++) {
		if (offset == invalidOffsets[i]) {
			offset++;
			continue;
		}
		if (offset < invalidOffsets[i]) {
			return offset - i;
		}
	}
	return offset - invalidOffsets.length;
}

int validateOffset(int offset, int step) {
	if (invalidOffsets == null) return offset + step;
	int i = step > 0 ? 0 : invalidOffsets.length - 1;
	do {
		offset += step;
		while (0 <= i && i < invalidOffsets.length) {
			if (invalidOffsets[i] == offset) break;
			i += step;
		}
	} while (0 <= i && i < invalidOffsets.length);
	return offset;
}

} 
