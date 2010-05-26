/*******************************************************************************
 * Copyright (c) 2000, 2009 IBM Corporation and others.
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
 * @see <a href="http://www.eclipse.org/swt/snippets/#textlayout">TextLayout, TextStyle snippets</a>
 * @see <a href="http://www.eclipse.org/swt/examples.php">SWT Example: CustomControlExample, StyledText tab</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 * 
 * @since 3.0
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
	int indent, wrapIndent, wrapWidth;
	int[] segments;
	char[] segmentsChars;
	int[] tabs;
	StyleItem[] styles;
	int stylesCount;
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
	super(device);
	device = this.device;
	context = OS.gdk_pango_context_get();
	if (context == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	OS.pango_context_set_language(context, OS.gtk_get_default_language());
	OS.pango_context_set_base_dir(context, OS.PANGO_DIRECTION_LTR);
	OS.gdk_pango_context_set_colormap(context, OS.gdk_colormap_get_system());
	layout = OS.pango_layout_new(context);
	if (layout == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	OS.pango_layout_set_font_description(layout, device.systemFont.handle);
	OS.pango_layout_set_wrap(layout, OS.PANGO_WRAP_WORD_CHAR);
	OS.pango_layout_set_tabs(layout, device.emptyTab);
	if (OS.GTK_VERSION >= OS.VERSION(2, 4, 0)) {
		OS.pango_layout_set_auto_dir(layout, false);
	}
	text = "";
	wrapWidth = ascent = descent = -1;
	styles = new StyleItem[2];
	styles[0] = new StyleItem();
	styles[1] = new StyleItem();
	stylesCount = 2;
	init();
}

void checkLayout() {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
}

void computeRuns () {
	if (attrList != 0) return;
	String segmentsText = getSegmentsText();
	byte[] buffer = Converter.wcsToMbcs(null, segmentsText, false);
	OS.pango_layout_set_text (layout, buffer, buffer.length);
	if (stylesCount == 2 && styles[0].style == null && ascent == -1 && descent == -1 && segments == null) return;
	int /*long*/ ptr = OS.pango_layout_get_text(layout);
	attrList = OS.pango_attr_list_new();	
	PangoAttribute attribute = new PangoAttribute();
	char[] chars = null;
	int segementsLength = segmentsText.length();
	int nSegments = segementsLength - text.length();
	int offsetCount = nSegments;
	int[] lineOffsets = null; 
	if ((ascent != -1  || descent != -1) && segementsLength > 0) {
		PangoRectangle rect = new PangoRectangle();
		if (ascent != -1) rect.y =  -(ascent  * OS.PANGO_SCALE);
		rect.height = (Math.max(0, ascent) + Math.max(0, descent)) * OS.PANGO_SCALE;
		int lineCount = OS.pango_layout_get_line_count(layout);
		chars = new char[segementsLength + lineCount * 2];
		lineOffsets = new int [lineCount];
		int oldPos = 0, lineIndex = 0;
		PangoLayoutLine line = new PangoLayoutLine();
		while (lineIndex < lineCount) {
			int /*long*/ linePtr = OS.pango_layout_get_line(layout, lineIndex);
			OS.memmove(line, linePtr, PangoLayoutLine.sizeof);
			int bytePos = line.start_index;
			/* Note: The length in bytes of ZWS and ZWNBS are both equals to 3 */
			int offset = lineIndex * 6;
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
			chars[pos + lineIndex * 2] = ZWS;
			chars[pos + lineIndex * 2 + 1] = ZWNBS;
			segmentsText.getChars(oldPos, pos, chars,  oldPos + lineIndex * 2);
			lineOffsets[lineIndex] = pos + lineIndex * 2; 
			oldPos = pos;
			lineIndex++;
		}
		segmentsText.getChars(oldPos, segementsLength, chars,  oldPos + lineIndex * 2);
		buffer = Converter.wcsToMbcs(null, chars, false);
		OS.pango_layout_set_text (layout, buffer, buffer.length);
		ptr = OS.pango_layout_get_text(layout);
		offsetCount += 2 * lineCount;
	} else {
		chars = new char[segementsLength];
		segmentsText.getChars(0, segementsLength, chars, 0);
	}
	invalidOffsets = new int[offsetCount];
	if (offsetCount > 0) {
		offsetCount = 0;
		int lineIndex = 0;
		int segmentCount = 0;
		for (int i = 0; i < chars.length; i++) {
			char c = chars[i];
			if (c == ZWS && lineOffsets != null && i == lineOffsets[lineIndex]) {
				invalidOffsets[offsetCount++] = i;		//ZWS
				invalidOffsets[offsetCount++] = ++i;	//ZWNBS
				lineIndex++;
			} else if (segmentCount < nSegments && i - offsetCount == segments[segmentCount]) {
				invalidOffsets[offsetCount++] = i;
				segmentCount++;
			}
		}
	}
	int strlen = OS.strlen(ptr);
	Font defaultFont = font != null ? font : device.systemFont;
	for (int i = 0; i < stylesCount - 1; i++) {
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
		if (font != null && !font.isDisposed() && !defaultFont.equals(font)) {
			int /*long*/ attr = OS.pango_attr_font_desc_new (font.handle);
			OS.memmove (attribute, attr, PangoAttribute.sizeof);
			attribute.start_index = byteStart;
			attribute.end_index = byteEnd;
			OS.memmove (attr, attribute, PangoAttribute.sizeof);
			OS.pango_attr_list_insert(attrList, attr);
		}
		if (style.underline) {
			int underlineStyle = OS.PANGO_UNDERLINE_NONE;
			switch (style.underlineStyle) {
				case SWT.UNDERLINE_SINGLE:
					underlineStyle = OS.PANGO_UNDERLINE_SINGLE; 
					break;
				case SWT.UNDERLINE_DOUBLE:
					underlineStyle = OS.PANGO_UNDERLINE_DOUBLE; 
					break;
				case SWT.UNDERLINE_SQUIGGLE:
				case SWT.UNDERLINE_ERROR:
					if (OS.GTK_VERSION >= OS.VERSION(2, 4, 0)) {
						underlineStyle = OS.PANGO_UNDERLINE_ERROR;
					}
					break;
				case SWT.UNDERLINE_LINK: {
					if (style.foreground == null) {
						int /*long*/ attr = OS.pango_attr_foreground_new((short)0, (short)0x3333, (short)0x9999);
						OS.memmove (attribute, attr, PangoAttribute.sizeof);
						attribute.start_index = byteStart;
						attribute.end_index = byteEnd;
						OS.memmove (attr, attribute, PangoAttribute.sizeof);
						OS.pango_attr_list_insert(attrList, attr);
					} 
					if (style.underlineColor == null) {
						underlineStyle = OS.PANGO_UNDERLINE_SINGLE;
					}
					break;
				}
			}
			if (underlineStyle != OS.PANGO_UNDERLINE_NONE && style.underlineColor == null) {
				int /*long*/ attr = OS.pango_attr_underline_new(underlineStyle);
				OS.memmove(attribute, attr, PangoAttribute.sizeof);
				attribute.start_index = byteStart;
				attribute.end_index = byteEnd;
				OS.memmove(attr, attribute, PangoAttribute.sizeof);
				OS.pango_attr_list_insert(attrList, attr);
			}
		}
		if (style.strikeout && style.strikeoutColor == null) {
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

void destroy() {
	font = null;
	text = null;
	styles = null;
	freeRuns();
	segments = null;
	segmentsChars = null;
	if (layout != 0) OS.g_object_unref(layout);
	layout = 0;
	if (context != 0) OS.g_object_unref(context);
	context = 0;
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
public void draw(GC gc, int x, int y, int selectionStart, int selectionEnd, Color selectionForeground, Color selectionBackground, int flags) {	
	checkLayout ();
	computeRuns();
	if (gc == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (gc.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	if (selectionForeground != null && selectionForeground.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	if (selectionBackground != null && selectionBackground.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	gc.checkGC(GC.FOREGROUND);
	int length = text.length();
	x += Math.min (indent, wrapIndent);
	boolean hasSelection = selectionStart <= selectionEnd && selectionStart != -1 && selectionEnd != -1;
	GCData data = gc.data;
	int /*long*/ cairo = data.cairo;
	if (flags != 0 && (hasSelection || (flags & SWT.LAST_LINE_SELECTION) != 0)) {
		int /*long*/[] attrs = new int /*long*/[1];
		int[] nAttrs = new int[1];
		PangoLogAttr logAttr = new PangoLogAttr();
		PangoRectangle rect = new PangoRectangle();
		int lineCount = OS.pango_layout_get_line_count(layout);
		int /*long*/ ptr = OS.pango_layout_get_text(layout);
		int /*long*/ iter = OS.pango_layout_get_iter(layout);
		if (selectionBackground == null) selectionBackground = device.getSystemColor(SWT.COLOR_LIST_SELECTION);
		if (cairo != 0 && OS.GTK_VERSION >= OS.VERSION(2, 8, 0)) {
			Cairo.cairo_save(cairo);
			GdkColor color = selectionBackground.handle;
			Cairo.cairo_set_source_rgba(cairo, (color.red & 0xFFFF) / (float)0xFFFF, (color.green & 0xFFFF) / (float)0xFFFF, (color.blue & 0xFFFF) / (float)0xFFFF, data.alpha / (float)0xFF);
		} else {
			OS.gdk_gc_set_foreground(gc.handle, selectionBackground.handle);
		}
		int lineIndex = 0;
		do {
			int lineEnd;
			OS.pango_layout_iter_get_line_extents(iter, null, rect);
			if (OS.pango_layout_iter_next_line(iter)) {
				int bytePos = OS.pango_layout_iter_get_index(iter);
				lineEnd = (int)/*64*/OS.g_utf8_pointer_to_offset(ptr, ptr + bytePos);
			} else {
				lineEnd = (int)/*64*/OS.g_utf8_strlen(ptr, -1);
			}
			boolean extent = false;
			if (lineIndex == lineCount - 1 && (flags & SWT.LAST_LINE_SELECTION) != 0) {
				extent = true;
			} else {
				if (attrs[0] == 0) OS.pango_layout_get_log_attrs(layout, attrs, nAttrs);
				OS.memmove(logAttr, attrs[0] + lineEnd * PangoLogAttr.sizeof, PangoLogAttr.sizeof);
				if (!logAttr.is_line_break) {
					if (selectionStart <= lineEnd && lineEnd <= selectionEnd) extent = true;
				} else {
					if (selectionStart <= lineEnd && lineEnd < selectionEnd && (flags & SWT.FULL_SELECTION) != 0) {
						extent = true;
					}
				}
			}
			if (extent) {
				int lineX = x + OS.PANGO_PIXELS(rect.x) + OS.PANGO_PIXELS(rect.width);
				int lineY = y + OS.PANGO_PIXELS(rect.y);
				int height = OS.PANGO_PIXELS(rect.height);
				if (ascent != -1 && descent != -1) {
					height = Math.max (height, ascent + descent);
				}
				int width = (flags & SWT.FULL_SELECTION) != 0 ? 0x7fffffff : height / 3;
				if (cairo != 0 && OS.GTK_VERSION >= OS.VERSION(2, 8, 0)) {
					Cairo.cairo_rectangle(cairo, lineX, lineY, width, height);
					Cairo.cairo_fill(cairo);
				} else {
					OS.gdk_draw_rectangle(data.drawable, gc.handle, 1, lineX, lineY, width, height);
				}
			}
			lineIndex++;
		} while (lineIndex < lineCount);
		OS.pango_layout_iter_free(iter);
		if (attrs[0] != 0) OS.g_free(attrs[0]);
		if (cairo != 0 && OS.GTK_VERSION >= OS.VERSION(2, 8, 0)) {
			Cairo.cairo_restore(cairo);	
		} else {
			OS.gdk_gc_set_foreground(gc.handle, data.foreground);
		}
	}
	if (length == 0) return;
	if (!hasSelection) {
		if (cairo != 0 && OS.GTK_VERSION >= OS.VERSION(2, 8, 0)) {
			if ((data.style & SWT.MIRRORED) != 0) {
				Cairo.cairo_save(cairo);
				Cairo.cairo_scale(cairo, -1,  1);
				Cairo.cairo_translate(cairo, -2 * x - width(), 0);
			}
			Cairo.cairo_move_to(cairo, x, y);
			OS.pango_cairo_show_layout(cairo, layout);
			drawBorder(gc, x, y, null);
			if ((data.style & SWT.MIRRORED) != 0) {
			    Cairo.cairo_restore(cairo);
			}
		} else {
			OS.gdk_draw_layout(data.drawable, gc.handle, x, y, layout);
			drawBorder(gc, x, y, null);
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
				if ((data.style & SWT.MIRRORED) != 0) {
					Cairo.cairo_save(cairo);
					Cairo.cairo_scale(cairo, -1,  1);
					Cairo.cairo_translate(cairo, -2 * x - width(), 0);
				}
				drawWithCairo(gc, x, y, 0, OS.strlen(ptr), fullSelection, selectionForeground.handle, selectionBackground.handle);
				if ((data.style & SWT.MIRRORED) != 0) {
					Cairo.cairo_restore(cairo);
				}
			} else {
				OS.gdk_draw_layout_with_colors(data.drawable, gc.handle, x, y, layout, selectionForeground.handle, selectionBackground.handle);
				drawBorder(gc, x, y, selectionForeground.handle);
			}
		} else {
			int /*long*/ ptr = OS.pango_layout_get_text(layout);
			int byteSelStart = (int)/*64*/(OS.g_utf8_offset_to_pointer(ptr, selectionStart) - ptr);
			int byteSelEnd = (int)/*64*/(OS.g_utf8_offset_to_pointer(ptr, selectionEnd + 1) - ptr);
			int strlen = OS.strlen(ptr);
			byteSelStart = Math.min(byteSelStart, strlen);
			byteSelEnd = Math.min(byteSelEnd, strlen);
			if (cairo != 0 && OS.GTK_VERSION >= OS.VERSION(2, 8, 0)) {
				if ((data.style & SWT.MIRRORED) != 0) {
					Cairo.cairo_save(cairo);
					Cairo.cairo_scale(cairo, -1,  1);
					Cairo.cairo_translate(cairo, -2 * x - width(), 0);
				}
				drawWithCairo(gc, x, y, byteSelStart, byteSelEnd, fullSelection, selectionForeground.handle, selectionBackground.handle);
				if ((data.style & SWT.MIRRORED) != 0) {
					Cairo.cairo_restore(cairo);
				}
			} else {
				Region clipping = new Region();
				gc.getClipping(clipping);
				OS.gdk_draw_layout(data.drawable, gc.handle, x, y, layout);
				drawBorder(gc, x, y, null);
				int[] ranges = new int[]{byteSelStart, byteSelEnd};
				int /*long*/ rgn = OS.gdk_pango_layout_get_clip_region(layout, x, y, ranges, ranges.length / 2);
				if (rgn != 0) {
					OS.gdk_gc_set_clip_region(gc.handle, rgn);
					OS.gdk_region_destroy(rgn);
				}
				OS.gdk_draw_layout_with_colors(data.drawable, gc.handle, x, y, layout, selectionForeground.handle, selectionBackground.handle);
				drawBorder(gc, x, y, selectionForeground.handle);
				gc.setClipping(clipping);
				clipping.dispose();
			}
		}
	}
}

void drawWithCairo(GC gc, int x, int y, int start, int end, boolean fullSelection, GdkColor fg, GdkColor bg) {
	GCData data = gc.data;
	int /*long*/ cairo = data.cairo;
	Cairo.cairo_save(cairo);
	if (!fullSelection) {
		Cairo.cairo_move_to(cairo, x, y);
		OS.pango_cairo_show_layout(cairo, layout);
		drawBorder(gc, x, y, null);
	}
	int[] ranges = new int[]{start, end};
	int /*long*/ rgn = OS.gdk_pango_layout_get_clip_region(layout, x, y, ranges, ranges.length / 2);
	if (rgn != 0) {
		OS.gdk_cairo_region(cairo, rgn);
		Cairo.cairo_clip(cairo);
		Cairo.cairo_set_source_rgba(cairo, (bg.red & 0xFFFF) / (float)0xFFFF, (bg.green & 0xFFFF) / (float)0xFFFF, (bg.blue & 0xFFFF) / (float)0xFFFF, data.alpha / (float)0xFF);
		Cairo.cairo_paint(cairo);
		OS.gdk_region_destroy(rgn);
	}
	Cairo.cairo_set_source_rgba(cairo, (fg.red & 0xFFFF) / (float)0xFFFF, (fg.green & 0xFFFF) / (float)0xFFFF, (fg.blue & 0xFFFF) / (float)0xFFFF, data.alpha / (float)0xFF);
	Cairo.cairo_move_to(cairo, x, y);
	OS.pango_cairo_show_layout(cairo, layout);
	drawBorder(gc, x, y, fg);
	Cairo.cairo_restore(cairo);
}

void drawBorder(GC gc, int x, int y, GdkColor selectionColor) {
	GCData data = gc.data;
	int /*long*/ cairo = data.cairo;
	int /*long*/ gdkGC = gc.handle;
	int /*long*/ ptr = OS.pango_layout_get_text(layout);
	GdkGCValues gcValues = null;
	if (cairo != 0 && OS.GTK_VERSION >= OS.VERSION(2, 8, 0)) {
		Cairo.cairo_save(cairo);
	}
	for (int i = 0; i < stylesCount - 1; i++) {
		TextStyle style = styles[i].style;
		if (style == null) continue;
		
		boolean drawBorder = style.borderStyle != SWT.NONE;
		if (drawBorder && !style.isAdherentBorder(styles[i+1].style)) {
			int start = styles[i].start;
			for (int j = i; j > 0 && style.isAdherentBorder(styles[j-1].style); j--) {
				start = styles[j - 1].start;
			}
			start = translateOffset(start);
			int end = translateOffset(styles[i+1].start - 1);
			int byteStart = (int)/*64*/(OS.g_utf8_offset_to_pointer(ptr, start) - ptr);
			int byteEnd = (int)/*64*/(OS.g_utf8_offset_to_pointer(ptr, end + 1) - ptr);
			int[] ranges = new int[]{byteStart, byteEnd};
			int /*long*/ rgn = OS.gdk_pango_layout_get_clip_region(layout, x, y, ranges, ranges.length / 2);
			if (rgn != 0) {
				int[] nRects = new int[1];
				int /*long*/[] rects = new int /*long*/[1];
				OS.gdk_region_get_rectangles(rgn, rects, nRects);
				GdkRectangle rect = new GdkRectangle();
				GdkColor color = null;
				if (color == null && style.borderColor != null) color = style.borderColor.handle;
				if (color == null && selectionColor != null) color = selectionColor;
				if (color == null && style.foreground != null) color = style.foreground.handle;
				if (color == null) color = data.foreground;
				int width = 1;
				float[] dashes = null;
				switch (style.borderStyle) {
					case SWT.BORDER_SOLID: break;
					case SWT.BORDER_DASH: dashes = width != 0 ? GC.LINE_DASH : GC.LINE_DASH_ZERO; break;
					case SWT.BORDER_DOT: dashes = width != 0 ? GC.LINE_DOT : GC.LINE_DOT_ZERO; break;
				}
				if (cairo != 0 && OS.GTK_VERSION >= OS.VERSION(2, 8, 0)) {
					Cairo.cairo_set_source_rgba(cairo, (color.red & 0xFFFF) / (float)0xFFFF, (color.green & 0xFFFF) / (float)0xFFFF, (color.blue & 0xFFFF) / (float)0xFFFF, data.alpha / (float)0xFF);
					Cairo.cairo_set_line_width(cairo, width);
					if (dashes != null) {
						double[] cairoDashes = new double[dashes.length];
						for (int j = 0; j < cairoDashes.length; j++) {
							cairoDashes[j] = width == 0 || data.lineStyle == SWT.LINE_CUSTOM ? dashes[j] : dashes[j] * width;
						}
						Cairo.cairo_set_dash(cairo, cairoDashes, cairoDashes.length, 0);
					} else {
						Cairo.cairo_set_dash(cairo, null, 0, 0);
					}
					for (int j=0; j<nRects[0]; j++) {
						OS.memmove(rect, rects[0] + (j * GdkRectangle.sizeof), GdkRectangle.sizeof);
						Cairo.cairo_rectangle(cairo, rect.x + 0.5, rect.y + 0.5, rect.width - 1, rect.height - 1);
					}
					Cairo.cairo_stroke(cairo);
				} else {
					if (gcValues == null) {
						gcValues = new GdkGCValues();
						OS.gdk_gc_get_values(gdkGC, gcValues);
					}
					OS.gdk_gc_set_foreground(gdkGC, color);
					int cap_style = OS.GDK_CAP_BUTT;
					int join_style = OS.GDK_JOIN_MITER;
					int line_style = 0;
					if (dashes != null) {
						byte[] dash_list = new byte[dashes.length];
						for (int j = 0; j < dash_list.length; j++) {
							dash_list[j] = (byte)(width == 0 || data.lineStyle == SWT.LINE_CUSTOM ? dashes[j] : dashes[j] * width);
						}
						OS.gdk_gc_set_dashes(gdkGC, 0, dash_list, dash_list.length);
						line_style = OS.GDK_LINE_ON_OFF_DASH;
					} else {
						line_style = OS.GDK_LINE_SOLID;
					}
					OS.gdk_gc_set_line_attributes(gdkGC, width, line_style, cap_style, join_style);
					for (int j=0; j<nRects[0]; j++) {
						OS.memmove(rect, rects[0] + (j * GdkRectangle.sizeof), GdkRectangle.sizeof);
						OS.gdk_draw_rectangle(data.drawable, gdkGC, 0, rect.x, rect.y, rect.width - 1, rect.height - 1);
					}
				}
				if (rects[0] != 0) OS.g_free(rects[0]);
				OS.gdk_region_destroy(rgn);
			}
		}
		
		boolean drawUnderline = false;
		if (style.underline && style.underlineColor != null) drawUnderline = true;
		if (style.underline && (style.underlineStyle == SWT.UNDERLINE_ERROR || style.underlineStyle == SWT.UNDERLINE_SQUIGGLE)&& OS.GTK_VERSION < OS.VERSION(2, 4, 0)) drawUnderline = true;
		if (drawUnderline && !style.isAdherentUnderline(styles[i+1].style)) {
			int start = styles[i].start;
			for (int j = i; j > 0 && style.isAdherentUnderline(styles[j-1].style); j--) {
				start = styles[j - 1].start;
			}
			start = translateOffset(start);
			int end = translateOffset(styles[i+1].start - 1);
			int byteStart = (int)/*64*/(OS.g_utf8_offset_to_pointer(ptr, start) - ptr);
			int byteEnd = (int)/*64*/(OS.g_utf8_offset_to_pointer(ptr, end + 1) - ptr);
			int[] ranges = new int[]{byteStart, byteEnd};
			int /*long*/ rgn = OS.gdk_pango_layout_get_clip_region(layout, x, y, ranges, ranges.length / 2);
			if (rgn != 0) {
				int[] nRects = new int[1];
				int /*long*/[] rects = new int /*long*/[1];
				OS.gdk_region_get_rectangles(rgn, rects, nRects);
				GdkRectangle rect = new GdkRectangle();
				GdkColor color = null;
				if (color == null && style.underlineColor != null) color = style.underlineColor.handle;
				if (color == null && selectionColor != null) color = selectionColor;
				if (color == null && style.foreground != null) color = style.foreground.handle;
				if (color == null) color = data.foreground;
				if (cairo != 0 && OS.GTK_VERSION >= OS.VERSION(2, 8, 0)) {
					Cairo.cairo_set_source_rgba(cairo, (color.red & 0xFFFF) / (float)0xFFFF, (color.green & 0xFFFF) / (float)0xFFFF, (color.blue & 0xFFFF) / (float)0xFFFF, data.alpha / (float)0xFF);
				} else {
					if (gcValues == null) {
						gcValues = new GdkGCValues();
						OS.gdk_gc_get_values(gdkGC, gcValues);
					}
					OS.gdk_gc_set_foreground(gdkGC, color);
				}
				int underlinePosition = -1;
				int underlineThickness = 1;
				if (OS.GTK_VERSION >= OS.VERSION(2, 6, 0)) {
					Font font = style.font;
					if (font == null) font = this.font;
					if (font == null) font = device.systemFont;
					int /*long*/ lang = OS.pango_context_get_language(context);
					int /*long*/ metrics = OS.pango_context_get_metrics(context, font.handle, lang);
					underlinePosition = OS.PANGO_PIXELS(OS.pango_font_metrics_get_underline_position(metrics));
					underlineThickness = OS.PANGO_PIXELS(OS.pango_font_metrics_get_underline_thickness(metrics));
					OS.pango_font_metrics_unref(metrics);
				}
				for (int j=0; j<nRects[0]; j++) {
					OS.memmove(rect, rects[0] + (j * GdkRectangle.sizeof), GdkRectangle.sizeof);
					int offset = getOffset(rect.x - x, rect.y - y, null);
					int lineIndex = getLineIndex(offset);
					FontMetrics metrics = getLineMetrics(lineIndex);
					int underlineY = rect.y + metrics.ascent - underlinePosition - style.rise;
					switch (style.underlineStyle) {
						case SWT.UNDERLINE_SQUIGGLE:
						case SWT.UNDERLINE_ERROR: {
							int squigglyThickness = underlineThickness;
							int squigglyHeight = 2 * squigglyThickness;
							int squigglyY = Math.min(underlineY, rect.y + rect.height - squigglyHeight - 1);
							int[] points = computePolyline(rect.x, squigglyY, rect.x + rect.width, squigglyY + squigglyHeight);
							if (cairo != 0 && OS.GTK_VERSION >= OS.VERSION(2, 8, 0)) {
								Cairo.cairo_set_line_width(cairo, squigglyThickness);
								Cairo.cairo_set_line_cap(cairo, Cairo.CAIRO_LINE_CAP_BUTT);
								Cairo.cairo_set_line_join(cairo, Cairo.CAIRO_LINE_JOIN_MITER);
								if (points.length > 0) {
									double xOffset = 0.5, yOffset = 0.5; 
									Cairo.cairo_move_to(cairo, points[0] + xOffset, points[1] + yOffset);
									for (int k = 2; k < points.length; k += 2) {
										Cairo.cairo_line_to(cairo, points[k] + xOffset, points[k + 1] + yOffset);
									}
									Cairo.cairo_stroke(cairo);
								}
							} else {	
								OS.gdk_gc_set_line_attributes(gdkGC, squigglyThickness, OS.GDK_LINE_SOLID, OS.GDK_CAP_BUTT, OS.GDK_JOIN_MITER);
								OS.gdk_draw_lines(data.drawable, gdkGC, points, points.length / 2);
							}
							break;
						}
						case SWT.UNDERLINE_DOUBLE:
							if (cairo != 0 && OS.GTK_VERSION >= OS.VERSION(2, 8, 0)) {
								Cairo.cairo_rectangle(cairo, rect.x, underlineY + underlineThickness * 2, rect.width, underlineThickness);
								Cairo.cairo_fill(cairo);
							} else {
								OS.gdk_draw_rectangle(data.drawable, gdkGC, 1, rect.x, underlineY + underlineThickness * 2, rect.width, underlineThickness);
							}
							//FALLTHROUGH
						case SWT.UNDERLINE_LINK:
						case SWT.UNDERLINE_SINGLE:
							if (cairo != 0 && OS.GTK_VERSION >= OS.VERSION(2, 8, 0)) {
								Cairo.cairo_rectangle(cairo, rect.x, underlineY, rect.width, underlineThickness);
								Cairo.cairo_fill(cairo);
							} else {
								OS.gdk_draw_rectangle(data.drawable, gdkGC, 1, rect.x, underlineY, rect.width, underlineThickness);
							}
							break;
					}
				}
				if (rects[0] != 0) OS.g_free(rects[0]);
				OS.gdk_region_destroy(rgn);
			}
		}
		
		boolean drawStrikeout = false;
		if (style.strikeout && style.strikeoutColor != null) drawStrikeout = true;
		if (drawStrikeout && !style.isAdherentStrikeout(styles[i+1].style)) {
			int start = styles[i].start;
			for (int j = i; j > 0 && style.isAdherentStrikeout(styles[j-1].style); j--) {
				start = styles[j - 1].start;
			}
			start = translateOffset(start);
			int end = translateOffset(styles[i+1].start - 1);
			int byteStart = (int)/*64*/(OS.g_utf8_offset_to_pointer(ptr, start) - ptr);
			int byteEnd = (int)/*64*/(OS.g_utf8_offset_to_pointer(ptr, end + 1) - ptr);
			int[] ranges = new int[]{byteStart, byteEnd};
			int /*long*/ rgn = OS.gdk_pango_layout_get_clip_region(layout, x, y, ranges, ranges.length / 2);
			if (rgn != 0) {
				int[] nRects = new int[1];
				int /*long*/[] rects = new int /*long*/[1];
				OS.gdk_region_get_rectangles(rgn, rects, nRects);
				GdkRectangle rect = new GdkRectangle();
				GdkColor color = null;
				if (color == null && style.strikeoutColor != null) color = style.strikeoutColor.handle;
				if (color == null && selectionColor != null) color = selectionColor;
				if (color == null && style.foreground != null) color = style.foreground.handle;
				if (color == null) color = data.foreground;
				if (cairo != 0 && OS.GTK_VERSION >= OS.VERSION(2, 8, 0)) {
					Cairo.cairo_set_source_rgba(cairo, (color.red & 0xFFFF) / (float)0xFFFF, (color.green & 0xFFFF) / (float)0xFFFF, (color.blue & 0xFFFF) / (float)0xFFFF, data.alpha / (float)0xFF);
				} else {
					if (gcValues == null) {
						gcValues = new GdkGCValues();
						OS.gdk_gc_get_values(gdkGC, gcValues);
					}
					OS.gdk_gc_set_foreground(gdkGC, color);
				}
				int strikeoutPosition = -1;
				int strikeoutThickness = 1;
				if (OS.GTK_VERSION >= OS.VERSION(2, 6, 0)) {
					Font font = style.font;
					if (font == null) font = this.font;
					if (font == null) font = device.systemFont;
					int /*long*/ lang = OS.pango_context_get_language(context);
					int /*long*/ metrics = OS.pango_context_get_metrics(context, font.handle, lang);
					strikeoutPosition = OS.PANGO_PIXELS(OS.pango_font_metrics_get_strikethrough_position(metrics));
					strikeoutThickness = OS.PANGO_PIXELS(OS.pango_font_metrics_get_strikethrough_thickness(metrics));
					OS.pango_font_metrics_unref(metrics);
				}
				for (int j=0; j<nRects[0]; j++) {
					OS.memmove(rect, rects[0] + (j * GdkRectangle.sizeof), GdkRectangle.sizeof);
					int strikeoutY = rect.y + rect.height / 2 - style.rise;
					if (OS.GTK_VERSION >= OS.VERSION(2, 6, 0)) {
						int offset = getOffset(rect.x - x, rect.y - y, null);
						int lineIndex = getLineIndex(offset);
						FontMetrics metrics = getLineMetrics(lineIndex);
						strikeoutY = rect.y + metrics.ascent - strikeoutPosition - style.rise;
					}
					if (cairo != 0 && OS.GTK_VERSION >= OS.VERSION(2, 8, 0)) {
						Cairo.cairo_rectangle(cairo, rect.x, strikeoutY, rect.width, strikeoutThickness);
						Cairo.cairo_fill(cairo);
					} else {
						OS.gdk_draw_rectangle(data.drawable, gdkGC, 1, rect.x, strikeoutY, rect.width, strikeoutThickness);
					}
				}
				if (rects[0] != 0) OS.g_free(rects[0]);
				OS.gdk_region_destroy(rgn);
			}
		}
	}
	if (gcValues != null) {
		int mask = OS.GDK_GC_FOREGROUND | OS.GDK_GC_LINE_WIDTH | OS.GDK_GC_LINE_STYLE | OS.GDK_GC_CAP_STYLE | OS.GDK_GC_JOIN_STYLE;
		OS.gdk_gc_set_values(gdkGC, gcValues, mask);
		data.state &= ~GC.LINE_STYLE;
	}
	if (cairo != 0 && OS.GTK_VERSION >= OS.VERSION(2, 8, 0)) {
		Cairo.cairo_restore(cairo);
	}
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
	boolean rtl = OS.pango_context_get_base_dir(context) == OS.PANGO_DIRECTION_RTL;
	switch (align) {
		case OS.PANGO_ALIGN_LEFT: return rtl ? SWT.RIGHT : SWT.LEFT;
		case OS.PANGO_ALIGN_RIGHT: return rtl ? SWT.LEFT : SWT.RIGHT;
	}
	return SWT.CENTER;
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
public Rectangle getBounds() {
	checkLayout();
	computeRuns();
	int[] w = new int[1], h = new int[1];
	OS.pango_layout_get_size(layout, w, h);
	int wrapWidth = OS.pango_layout_get_width(layout);
	w[0] = wrapWidth != -1 ? wrapWidth : w[0] + OS.pango_layout_get_indent(layout);
	int width = OS.PANGO_PIXELS(w[0]);
	int height = OS.PANGO_PIXELS(h[0]);
	if (ascent != -1 && descent != -1) {
		height = Math.max (height, ascent + descent);
	}
	height += OS.PANGO_PIXELS(OS.pango_layout_get_spacing(layout));	
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
	if (OS.pango_context_get_base_dir(context) == OS.PANGO_DIRECTION_RTL) {
		rect.x = width() - rect.x - rect.width;
	}
	rect.x += Math.min (indent, wrapIndent);
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
	return OS.pango_layout_get_justify(layout);
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
	if (OS.pango_context_get_base_dir(context) == OS.PANGO_DIRECTION_RTL) {
		x = width() - x - width;
	}
	x += Math.min (indent, wrapIndent);
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
	PangoLayoutLine line = new PangoLayoutLine();
	for (int i = 0; i < lineCount; i++) {
		int /*long*/ linePtr = OS.pango_layout_get_line(layout, i);
		OS.memmove(line, linePtr, PangoLayoutLine.sizeof);
		int pos = (int)/*64*/OS.g_utf8_pointer_to_offset(ptr, ptr + line.start_index);
		offsets[i] = untranslateOffset(pos);
	}
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
	x = OS.PANGO_PIXELS(x);
	if (OS.pango_context_get_base_dir(context) == OS.PANGO_DIRECTION_RTL) {
		x = width() - x;
	}
	x += Math.min (indent, wrapIndent);
	return new Point(x, OS.PANGO_PIXELS(y));
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
	offset += step;
	int internalOffset = translateOffset(offset);
	PangoLogAttr logAttr = new PangoLogAttr();
	while (0 < internalOffset && internalOffset < length) {
		OS.memmove(logAttr, attrs[0] + internalOffset * PangoLogAttr.sizeof, PangoLogAttr.sizeof);
		if (((movement & SWT.MOVEMENT_CLUSTER) != 0) && logAttr.is_cursor_position) break; 
		if ((movement & SWT.MOVEMENT_WORD) != 0) {
			if (forward) {
				if (logAttr.is_word_end) break;
			} else {
				if (logAttr.is_word_start) break;
			}
		}
		if ((movement & SWT.MOVEMENT_WORD_START) != 0) {
			if (logAttr.is_word_start) break;
			if (logAttr.is_sentence_end) break;
		}
		if ((movement & SWT.MOVEMENT_WORD_END) != 0) {
			if (logAttr.is_word_end) break;
		}
		offset += step;
		internalOffset = translateOffset(offset);
	}
	OS.g_free(attrs[0]);
	return Math.min(Math.max(0, offset), text.length());
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
	x -= Math.min (indent, wrapIndent);
	if (OS.pango_context_get_base_dir(context) == OS.PANGO_DIRECTION_RTL) {
		x = width() - x;
	}
	
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
public int[] getSegments() {
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
	char defaultSeparator = getOrientation() == SWT.RIGHT_TO_LEFT ? RTL_MARK : LTR_MARK;
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
	for (int i=1; i<stylesCount; i++) {
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
	checkLayout ();
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
	boolean rtl = OS.pango_context_get_base_dir(context) == OS.PANGO_DIRECTION_RTL;
	int align = OS.PANGO_ALIGN_CENTER;
	switch (alignment) {
		case SWT.LEFT: 
			align = rtl ? OS.PANGO_ALIGN_RIGHT : OS.PANGO_ALIGN_LEFT; 
			break;
		case SWT.RIGHT: 
			align = rtl ? OS.PANGO_ALIGN_LEFT : OS.PANGO_ALIGN_RIGHT; 
			break;
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
	Font oldFont = this.font;
	if (oldFont == font) return;
	freeRuns();
	this.font = font;
	if (oldFont != null && oldFont.equals(font)) return;
	OS.pango_layout_set_font_description(layout, font != null ? font.handle : device.systemFont.handle);
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
	if (indent < 0) return;
	if (this.indent == indent) return;
	this.indent = indent;
	OS.pango_layout_set_indent(layout, (indent - wrapIndent) * OS.PANGO_SCALE);
	if (wrapWidth != -1) setWidth();
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
	int align = OS.pango_layout_get_alignment(layout);
	if (align != OS.PANGO_ALIGN_CENTER) {
		align = align == OS.PANGO_ALIGN_LEFT ? OS.PANGO_ALIGN_RIGHT : OS.PANGO_ALIGN_LEFT;
		OS.pango_layout_set_alignment(layout, align);
	}
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
 * <p> 
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
	checkLayout ();
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
	checkLayout ();
	if (width < -1 || width == 0) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	if (wrapWidth == width) return;
	freeRuns();
	wrapWidth = width;
	setWidth();
}

void setWidth () {
	if (wrapWidth == -1) {
		OS.pango_layout_set_width(layout, -1);
		boolean rtl = OS.pango_context_get_base_dir(context) == OS.PANGO_DIRECTION_RTL;
		OS.pango_layout_set_alignment(layout, rtl ? OS.PANGO_ALIGN_RIGHT : OS.PANGO_ALIGN_LEFT);
	} else {
		int margin = Math.min (indent, wrapIndent);
		OS.pango_layout_set_width(layout, (wrapWidth - margin) * OS.PANGO_SCALE);
	}
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
	if (wrapIndent < 0) return;
	if (this.wrapIndent == wrapIndent) return;
	this.wrapIndent = wrapIndent;
	OS.pango_layout_set_indent(layout, (indent - wrapIndent) * OS.PANGO_SCALE);
	if (wrapWidth != -1) setWidth();
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
	int i = 0;
	while (i < invalidOffsets.length && offset > invalidOffsets[i]) {
		i++;
	}
	return offset - i;
}

int width () {
	int wrapWidth = OS.pango_layout_get_width(layout);
	if (wrapWidth != -1) return OS.PANGO_PIXELS(wrapWidth); 
	int[] w = new int[1], h = new int[1];
	OS.pango_layout_get_size(layout, w, h);
	return OS.PANGO_PIXELS(w[0]);
}

} 
