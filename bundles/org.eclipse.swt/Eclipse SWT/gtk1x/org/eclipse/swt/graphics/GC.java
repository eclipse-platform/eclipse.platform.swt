package org.eclipse.swt.graphics;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import org.eclipse.swt.widgets.*;
import org.eclipse.swt.internal.gtk.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.*;


/**
 * Class <code>GC</code> is where all of the drawing capabilities that are 
 * supported by SWT are located. Instances are used to draw on either an 
 * <code>Image</code>, a <code>Control</code>, or directly on a <code>Display</code>.
 * <p>
 * Application code must explicitly invoke the <code>GC.dispose()</code> 
 * method to release the operating system resources managed by each instance
 * when those instances are no longer required. This is <em>particularly</em>
 * important on Windows95 and Windows98 where the operating system has a limited
 * number of device contexts available.
 * </p>
 *
 * @see org.eclipse.swt.events.PaintEvent
 */
public final class GC {
	/**
	 * the handle to the OS device context
	 * (Warning: This field is platform dependent)
	 */
	public int handle;
	Drawable drawable;
	GCData data;


/*
 *   ===  Constructors  ===
 */

GC() {
}

/**	 
 * Constructs a new instance of this class which has been
 * configured to draw on the specified drawable. Sets the
 * foreground and background color in the GC to match those
 * in the drawable.
 * <p>
 * You must dispose the graphics context when it is no longer required. 
 * </p>
 * @param drawable the drawable to draw on
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the drawable is null</li>
 *    <li>ERROR_INVALID_ARGUMENT
 *          - if the drawable is an image that is not a bitmap or an icon
 *          - if the drawable is an image or printer that is already selected
 *            into another graphics context</li>
 * </ul>
 */
public GC(Drawable drawable) {
	if (drawable == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	
	data = new GCData();
	handle = drawable.internal_new_GC(data);
	this.drawable = drawable;
	
	// The colors we get from the widget are not always right.
	// Get the default GTK_STATE_NORMAL colors
	setBackground( DefaultGtkStyle.instance().backgroundColorNORMAL() );
	setForeground( DefaultGtkStyle.instance().foregroundColorNORMAL() );


	// Feature in GDK.
	// Sometimes, gdk_gc_new() doesn't get the font from the control,
	// and also, some controls don't contain a font; so when the GC
	// was created in internal_new_gc(), the font might or might not
	// be set; if the font isn't there, just fall back to default.
	GdkGCValues values = new GdkGCValues();
	OS.gdk_gc_get_values(handle, values);
	if (values.font == 0) {
		OS.gdk_gc_set_font(handle,  DefaultGtkStyle.instance().loadDefaultFont() );
	}
	
	if (data.image != null) {
		data.image.memGC = this;
		/*
		 * The transparent pixel mask might change when drawing on
		 * the image.  Destroy it so that it is regenerated when
		 * necessary.
		 */
		//if (image.transparentPixel != -1) image.destroyMask();
	}
	
}



/** 
 * Returns the background color.
 *
 * @return the receiver's background color
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
/*
 *   ===  Access - Get/Set  ===
 */

public Color getBackground() {
	if (handle == 0) error(SWT.ERROR_WIDGET_DISPOSED);
	GdkColor gdkColor = _getBackgroundGdkColor();
	return Color.gtk_new(gdkColor);	
}
/**
 * Sets the background color. The background color is used
 * for fill operations and as the background color when text
 * is drawn.
 *
 * @param color the new background color for the receiver
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the color is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if the color has been disposed</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public void setBackground(Color color) {	
	if (color == null) error(SWT.ERROR_NULL_ARGUMENT);
	if (color.handle == null) error(SWT.ERROR_NULL_ARGUMENT);
	if (handle == 0) error(SWT.ERROR_WIDGET_DISPOSED);
	OS.gdk_gc_set_background(handle, color.handle);
}

/** 
 * Returns the receiver's foreground color.
 *
 * @return the color used for drawing foreground things
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public Color getForeground() {	
	if (handle == 0) error(SWT.ERROR_WIDGET_DISPOSED);
	GdkColor gdkColor = _getForegroundGdkColor();
	return Color.gtk_new(gdkColor);	
}

/**
 * Sets the foreground color. The foreground color is used
 * for drawing operations including when text is drawn.
 *
 * @param color the new foreground color for the receiver
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the color is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if the color has been disposed</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public void setForeground(Color color) {	
	if (handle == 0) error(SWT.ERROR_WIDGET_DISPOSED);
	if (color == null) error(SWT.ERROR_NULL_ARGUMENT);
	if (color.handle == null) error(SWT.ERROR_NULL_ARGUMENT);
	OS.gdk_gc_set_foreground(handle, color.handle);
}







/**
 * Returns the <em>advance width</em> of the specified character in
 * the font which is currently selected into the receiver.
 * <p>
 * The advance width is defined as the horizontal distance the cursor
 * should move after printing the character in the selected font.
 * </p>
 *
 * @param ch the character to measure
 * @return the distance in the x direction to move past the character before painting the next
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
/*
 *   ===  Access - Get - Calculated  ===
 */

public int getAdvanceWidth(char ch) {	
	byte[] charBuffer = Converter.wcsToMbcs(null, new char[] { ch });
	return OS.gdk_char_width(_getGCFont(), charBuffer[0]);
}
/**
 * Returns the width of the specified character in the font
 * selected into the receiver. 
 * <p>
 * The width is defined as the space taken up by the actual
 * character, not including the leading and tailing whitespace
 * or overhang.
 * </p>
 *
 * @param ch the character to measure
 * @return the width of the character
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public int getCharWidth(char ch) {
	byte[] charBuffer = Converter.wcsToMbcs(null, new char[] { ch });
	int[] lbearing = new int[1];
	int[] rbearing = new int[1];
	int[] unused = new int[1];
	OS.gdk_string_extents(_getGCFont(), charBuffer, lbearing, rbearing, unused, unused, unused);
	return rbearing[0] - lbearing[0];
}
/** 
 * Returns the bounding rectangle of the receiver's clipping
 * region. If no clipping region is set, the return value
 * will be a rectangle which covers the entire bounds of the
 * object the receiver is drawing on.
 *
 * @return the bounding rectangle of the clipping region
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public Rectangle getClipping() {
	if (data.clipRgn == 0) {
		int[] width = new int[1]; int[] height = new int[1];
		int[] unused = new int[1];
		OS.gdk_window_get_geometry(data.drawable, unused, unused, width, height, unused);
		return new Rectangle(0, 0, width[0], height[0]);
	}
	GdkRectangle rect = new GdkRectangle();
	OS.gdk_region_get_clipbox(data.clipRgn, rect);
	return new Rectangle(rect.x, rect.y, rect.width, rect.height);
}
/** 
 * Sets the region managed by the argument to the current
 * clipping region of the receiver.
 *
 * @param region the region to fill with the clipping region
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the region is null</li>
 * </ul>	
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public void getClipping(Region region) {	
	if (region == null) error(SWT.ERROR_NULL_ARGUMENT);
	int hRegion = region.handle;
	if (data.clipRgn == 0) {
		int[] width = new int[1]; int[] height = new int[1];
		int[] unused = new int[1];
		OS.gdk_window_get_geometry(data.drawable, unused, unused, width, height, unused);
		hRegion = OS.gdk_region_new();
		GdkRectangle rect = new GdkRectangle();
		rect.x = 0; rect.y = 0;
		rect.width = (short)width[0]; rect.height = (short)height[0];
		region.handle = OS.gdk_region_union_with_rect(hRegion, rect);
		return;
	}
	hRegion = OS.gdk_region_new();
	region.handle = OS.gdk_regions_union(data.clipRgn, hRegion);
}
/** 
 * Returns the font currently being used by the receiver
 * to draw and measure text.
 *
 * @return the receiver's font
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public Font getFont() {		
	return Font.gtk_new(_getGCFont());
}
/**
 * Returns a FontMetrics which contains information
 * about the font currently being used by the receiver
 * to draw and measure text.
 *
 * @return font metrics for the receiver's font
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
// Not done
public FontMetrics getFontMetrics() {
	int fontHandle = _getGCFont();
	if (fontHandle==0) {
		error(SWT.ERROR_UNSPECIFIED);
	}
	GdkFont gdkFont = new GdkFont();
	OS.memmove(gdkFont, fontHandle, GdkFont.sizeof);
	byte [] w = Converter.wcsToMbcs (null, "w", true);
	return FontMetrics.gtk_new(fontHandle);
}

/** 
 * Returns the receiver's line style, which will be one
 * of the constants <code>SWT.LINE_SOLID</code>, <code>SWT.LINE_DASH</code>,
 * <code>SWT.LINE_DOT</code>, <code>SWT.LINE_DASHDOT</code> or
 * <code>SWT.LINE_DASHDOTDOT</code>.
 *
 * @return the style used for drawing lines
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public int getLineStyle() {
	return data.lineStyle;
}
/** 
 * Returns the width that will be used when drawing lines
 * for all of the figure drawing operations (that is,
 * <code>drawLine</code>, <code>drawRectangle</code>, 
 * <code>drawPolyline</code>, and so forth.
 *
 * @return the receiver's line width 
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public int getLineWidth() {	
	GdkGCValues values = new GdkGCValues();
	OS.gdk_gc_get_values(handle, values);
	return values.line_width;
}
/** 
 * Returns <code>true</code> if this GC is drawing in the mode
 * where the resulting color in the destination is the
 * <em>exclusive or</em> of the color values in the source
 * and the destination, and <code>false</code> if it is
 * drawing in the mode where the destination color is being
 * replaced with the source color value.
 *
 * @return <code>true</code> true if the receiver is in XOR mode, and false otherwise
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public boolean getXORMode() {	
	GdkGCValues values = new GdkGCValues();
	OS.gdk_gc_get_values(handle, values);
	return values.function == OS.GDK_XOR;
}
/**
 * Returns <code>true</code> if the receiver has a clipping
 * region set into it, and <code>false</code> otherwise.
 * If this method returns false, the receiver will draw on all
 * available space in the destination. If it returns true, 
 * it will draw only in the area that is covered by the region
 * that can be accessed with <code>getClipping(region)</code>.
 *
 * @return <code>true</code> if the GC has a clipping region, and <code>false</code> otherwise
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public boolean isClipped() {
	return data.clipRgn != 0;
}


/**
 * Sets the area of the receiver which can be changed
 * by drawing operations to the rectangular area specified
 * by the arguments.
 *
 * @param x the x coordinate of the clipping rectangle
 * @param y the y coordinate of the clipping rectangle
 * @param width the width of the clipping rectangle
 * @param height the height of the clipping rectangle
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public void setClipping(int x, int y, int width, int height) {
	if (data.clipRgn == 0) data.clipRgn = OS.gdk_region_new();
	GdkRectangle rect = new GdkRectangle();
	rect.x = (short)x;  rect.y = (short)y;
	rect.width = (short)width;  rect.height = (short)height;
	OS.gdk_gc_set_clip_rectangle(handle, rect);
	data.clipRgn = OS.gdk_regions_subtract(data.clipRgn, data.clipRgn);
	data.clipRgn = OS.gdk_region_union_with_rect(data.clipRgn, rect);
}
/**
 * Sets the area of the receiver which can be changed
 * by drawing operations to the rectangular area specified
 * by the argument.
 *
 * @param rect the clipping rectangle
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public void setClipping(Rectangle rect) {
	if (rect == null) error(SWT.ERROR_NULL_ARGUMENT);
	setClipping (rect.x, rect.y, rect.width, rect.height);
}
/**
 * Sets the area of the receiver which can be changed
 * by drawing operations to the region specified
 * by the argument.
 *
 * @param rect the clipping region.
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public void setClipping(Region region) {	
	if (data.clipRgn == 0) data.clipRgn = OS.gdk_region_new();
	if (region == null) {
		data.clipRgn = OS.gdk_regions_subtract(data.clipRgn, data.clipRgn);
		OS.gdk_gc_set_clip_mask(handle, OS.GDK_NONE);
	} else {
		data.clipRgn = OS.gdk_regions_subtract(data.clipRgn, data.clipRgn);
		data.clipRgn = OS.gdk_regions_union(region.handle, data.clipRgn);
		OS.gdk_gc_set_clip_region(handle, region.handle);
	}
}
/** 
 * Sets the font which will be used by the receiver
 * to draw and measure text to the argument. If the
 * argument is null, then a default font appropriate
 * for the platform will be used instead.
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
public void setFont(Font font) {	
	int fontHandle = 0;
	if (font == null) {
		GtkStyle gtkStyle = new GtkStyle();
		int style = OS.gtk_widget_get_default_style();
		OS.memmove(gtkStyle, style, GtkStyle.sizeof);
		fontHandle = gtkStyle.font;
	} else {
		fontHandle = font.handle;
	}
	OS.gdk_gc_set_font(handle, fontHandle);
}

/** 
 * Sets the receiver's line style to the argument, which must be one
 * of the constants <code>SWT.LINE_SOLID</code>, <code>SWT.LINE_DASH</code>,
 * <code>SWT.LINE_DOT</code>, <code>SWT.LINE_DASHDOT</code> or
 * <code>SWT.LINE_DASHDOTDOT</code>.
 *
 * @param lineStyle the style to be used for drawing lines
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public void setLineStyle(int lineStyle) {
	switch (lineStyle) {
		case SWT.LINE_SOLID:
			this.data.lineStyle = lineStyle;
			OS.gdk_gc_set_line_attributes(handle, 0, OS.GDK_LINE_SOLID, OS.GDK_CAP_BUTT, OS.GDK_JOIN_MITER);
			return;
		case SWT.LINE_DASH:
			OS.gdk_gc_set_dashes(handle, 0, new byte[] {6, 2}, 2);
			break;
		case SWT.LINE_DOT:
			OS.gdk_gc_set_dashes(handle, 0, new byte[] {3, 1}, 2);
			break;
		case SWT.LINE_DASHDOT:
			OS.gdk_gc_set_dashes(handle, 0, new byte[] {6, 2, 3, 1}, 4);
			break;
		case SWT.LINE_DASHDOTDOT:
			OS.gdk_gc_set_dashes(handle, 0, new byte[] {6, 2, 3, 1, 3, 1}, 6);
			break;
		default:
			error(SWT.ERROR_INVALID_ARGUMENT);
	}
	this.data.lineStyle = lineStyle;
	OS.gdk_gc_set_line_attributes(handle, 0, OS.GDK_LINE_DOUBLE_DASH, OS.GDK_CAP_BUTT, OS.GDK_JOIN_MITER);
}
/** 
 * Sets the width that will be used when drawing lines
 * for all of the figure drawing operations (that is,
 * <code>drawLine</code>, <code>drawRectangle</code>, 
 * <code>drawPolyline</code>, and so forth.
 *
 * @param lineWidth the width of a line
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public void setLineWidth(int width) {	
	if (data.lineStyle == SWT.LINE_SOLID) {
		OS.gdk_gc_set_line_attributes(handle, width, OS.GDK_LINE_SOLID, OS.GDK_CAP_BUTT, OS.GDK_JOIN_MITER);
	} else {
		OS.gdk_gc_set_line_attributes(handle, width, OS.GDK_LINE_DOUBLE_DASH, OS.GDK_CAP_BUTT, OS.GDK_JOIN_MITER);
	}
}
/** 
 * If the argument is <code>true</code>, puts the receiver
 * in a drawing mode where the resulting color in the destination
 * is the <em>exclusive or</em> of the color values in the source
 * and the destination, and if the argument is <code>false</code>,
 * puts the receiver in a drawing mode where the destination color
 * is replaced with the source color value.
 *
 * @param xor if <code>true</code>, then <em>xor</em> mode is used, otherwise <em>source copy</em> mode is used
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public void setXORMode(boolean val) {	
	if (val) {
		OS.gdk_gc_set_function(handle, OS.GDK_XOR);
	} else {
		OS.gdk_gc_set_function(handle, OS.GDK_COPY);
	}
}
/**
 * Returns the extent of the given string. No tab
 * expansion or carriage return processing will be performed.
 * <p>
 * The <em>extent</em> of a string is the width and height of
 * the rectangular area it would cover if drawn in a particular
 * font (in this case, the current font in the receiver).
 * </p>
 *
 * @param string the string to measure
 * @return a point containing the extent of the string
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the string is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public Point stringExtent(String string) {	
	if (string == null) error(SWT.ERROR_NULL_ARGUMENT);
	byte[] buffer = Converter.wcsToMbcs(null, string, true);
	int width = OS.gdk_string_width(_getGCFont(), buffer);
	int height = OS.gdk_string_height(_getGCFont(), buffer);
	return new Point(width, height);
}
/**
 * Returns the extent of the given string. Tab expansion and
 * carriage return processing are performed.
 * <p>
 * The <em>extent</em> of a string is the width and height of
 * the rectangular area it would cover if drawn in a particular
 * font (in this case, the current font in the receiver).
 * </p>
 *
 * @param string the string to measure
 * @return a point containing the extent of the string
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the string is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public Point textExtent(String string) {		
	if (string == null) error(SWT.ERROR_NULL_ARGUMENT);
	byte[] buffer = Converter.wcsToMbcs(null, string, true);
	int width = OS.gdk_string_width(_getGCFont(), buffer);
	int height = OS.gdk_string_height(_getGCFont(), buffer);
	return new Point(width, height);
}



/*
 *   ===  Access - Internal utils  ===
 */
 
private GdkGCValues _getGCValues() {
	GdkGCValues values = new GdkGCValues();
	OS.gdk_gc_get_values(handle, values);
	return values;
}
private GdkColor _getForegroundGdkColor() {
	GdkGCValues values = _getGCValues();
	GdkColor color = new GdkColor();
	color.pixel = values.foreground_pixel;
	color.red   = values.foreground_red;
	color.green = values.foreground_green;
	color.blue  = values.foreground_blue;
	return color;
}
private GdkColor _getBackgroundGdkColor() {
	GdkGCValues values = _getGCValues();
	GdkColor color = new GdkColor();
	color.pixel = values.background_pixel;
	color.red   = values.background_red;
	color.green = values.background_green;
	color.blue  = values.background_blue;
	return color;
}
private int _getGCFont() {
	GdkGCValues values = _getGCValues();
	if (values.font==0) {
		SWT.error(SWT.ERROR_UNSPECIFIED);
	}
	return values.font;
}



/**
 * Copies a rectangular area of the receiver at the source
 * position onto the receiver at the destination position.
 *
 * @param srcX the x coordinate in the receiver of the area to be copied
 * @param srcY the y coordinate in the receiver of the area to be copied
 * @param width the width of the area to copy
 * @param height the height of the area to copy
 * @param destX the x coordinate in the receiver of the area to copy to
 * @param destY the y coordinate in the receiver of the area to copy to
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
/*
 *   ===  Drawing operations proper  ===
 */

public void copyArea(int srcX, int srcY, int width, int height, int destX, int destY) {
	OS.gdk_window_copy_area(data.drawable, handle, destX, destY, data.drawable, srcX, srcY, width, height);
}

/**
 * Draws the outline of a circular or elliptical arc 
 * within the specified rectangular area.
 * <p>
 * The resulting arc begins at <code>startAngle</code> and extends  
 * for <code>arcAngle</code> degrees, using the current color.
 * Angles are interpreted such that 0 degrees is at the 3 o'clock
 * position. A positive value indicates a counter-clockwise rotation
 * while a negative value indicates a clockwise rotation.
 * </p><p>
 * The center of the arc is the center of the rectangle whose origin 
 * is (<code>x</code>, <code>y</code>) and whose size is specified by the 
 * <code>width</code> and <code>height</code> arguments. 
 * </p><p>
 * The resulting arc covers an area <code>width + 1</code> pixels wide
 * by <code>height + 1</code> pixels tall.
 * </p>
 *
 * @param x the x coordinate of the upper-left corner of the arc to be drawn
 * @param y the y coordinate of the upper-left corner of the arc to be drawn
 * @param width the width of the arc to be drawn
 * @param height the height of the arc to be drawn
 * @param startAngle the beginning angle
 * @param arcAngle the angular extent of the arc, relative to the start angle
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if any of the width, height or endAngle is zero.</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public void drawArc(int x, int y, int width, int height, int startAngle, int endAngle) {
	if (width < 0) {
		x = x + width;
		width = -width;
	}
	if (height < 0) {
		y = y + height;
		height = -height;
	}
	if (width == 0 || height == 0 || endAngle == 0) {
		error(SWT.ERROR_INVALID_ARGUMENT);
	}	
	OS.gdk_draw_arc(data.drawable, handle, 0, x, y, width, height, startAngle * 64, endAngle * 64);
}
/** 
 * Draws a rectangle, based on the specified arguments, which has
 * the appearance of the platform's <em>focus rectangle</em> if the
 * platform supports such a notion, and otherwise draws a simple
 * rectangle in the receiver's forground color.
 *
 * @param x the x coordinate of the rectangle
 * @param y the y coordinate of the rectangle
 * @param width the width of the rectangle
 * @param height the height of the rectangle
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 *
 * @see #drawRectangle
 */
public void drawFocus(int x, int y, int width, int height) {
	GtkStyle style = new GtkStyle();
	int hStyle = OS.gtk_widget_get_default_style();
	OS.memmove(style, hStyle, GtkStyle.sizeof);
	GdkColor color = new GdkColor();
	color.pixel = style.fg0_pixel;
	color.red = style.fg0_red;
	color.green = style.fg0_green;
	color.blue = style.fg0_blue;
	GdkGCValues values = new GdkGCValues();
	OS.gdk_gc_get_values(handle, values);
	OS.gdk_gc_set_foreground(handle, color);
	OS.gdk_draw_rectangle(data.drawable, handle, 0, x, y, width, height);
	color.pixel = values.foreground_pixel;
	color.red = values.foreground_red;
	color.green = values.foreground_green;
	color.blue = values.foreground_blue;
	OS.gdk_gc_set_foreground(handle, color);
}
/**
 * Draws the given image in the receiver at the specified
 * coordinates.
 *
 * @param image the image to draw
 * @param x the x coordinate of where to draw
 * @param y the y coordinate of where to draw
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the image is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if the image has been disposed</li>
 *    <li>ERROR_INVALID_ARGUMENT - if the given coordinates are outside the bounds of the image</li>
 * @exception SWTError <ul>
 *    <li>ERROR_NO_HANDLES - if no handles are available to perform the operation</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public void drawImage(Image image, int x, int y) {
	if (image == null) error(SWT.ERROR_NULL_ARGUMENT);
	int pixmap = image.pixmap;
	int [] unused = new int [1];  int [] width = new int [1];  int [] height = new int [1];
 	OS.gdk_window_get_geometry(pixmap, unused, unused, width, height, unused);
 	drawImage(image, 0, 0, width[0], height[0], x, y, width[0], height[0]);
}

/**
 * Copies a rectangular area from the source image into a (potentially
 * different sized) rectangular area in the receiver. If the source
 * and destination areas are of differing sizes, then the source
 * area will be stretched or shrunk to fit the destination area
 * as it is copied. The copy fails if any of the given coordinates
 * are negative or lie outside the bounds of their respective images.
 *
 * @param image the source image
 * @param srcX the x coordinate in the source image to copy from
 * @param srcY the y coordinate in the source image to copy from
 * @param srcWidth the width in pixels to copy from the source
 * @param srcHeight the height in pixels to copy from the source
 * @param destX the x coordinate in the destination to copy to
 * @param destY the y coordinate in the destination to copy to
 * @param destWidth the width in pixels of the destination rectangle
 * @param destHeight the height in pixels of the destination rectangle
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the image is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if the image has been disposed</li>
 *    <li>ERROR_INVALID_ARGUMENT - if the given coordinates are outside the bounds of their respective images</li>
 * </ul>
 * @exception SWTError <ul>
 *    <li>ERROR_NO_HANDLES - if no handles are available to perform the operation</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public void drawImage(Image srcImage, int srcX, int srcY, int srcWidth, int srcHeight, int destX, int destY, int destWidth, int destHeight) {
	/* basic sanity checks */
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (srcWidth == 0 || srcHeight == 0 || destWidth == 0 || destHeight == 0) return;
	if (srcX < 0 || srcY < 0 || srcWidth < 0 || srcHeight < 0 || destWidth < 0 || destHeight < 0) {
		SWT.error (SWT.ERROR_INVALID_ARGUMENT);
	}
	if (srcImage == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (srcImage.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);

	/* source image properties */
	int[] width = new int[1];
	int[] height = new int[1];
	int[] unused = new int[1];
 	OS.gdk_window_get_geometry(srcImage.pixmap, unused, unused, width, height, unused);
	if ((srcY + srcWidth > width[0]) ||
		(srcY + srcHeight > height[0])) {
		error(SWT.ERROR_INVALID_ARGUMENT);
	}

	/* Special case:  If we don't need to scale, and there is no alpha/mask,
	 * then we can just blit the image inside the X server - no net traffic
	 */
	boolean needScaling = (srcWidth != destWidth) || (srcHeight != destHeight);
	boolean simple = !needScaling & (srcImage.mask == 0);
	
	if (simple) {
		OS.gdk_draw_pixmap(data.drawable, handle, srcImage.pixmap,
			srcX, srcY,
			destX, destY,
			width[0], height[0]);
		return;
	}
	
	
	/* Fetch a local GdkPixbuf from server */
	Pixbuffer pixbuf = new Pixbuffer(srcImage);
	
	/* Scale if necessary */
	if ((srcWidth != destWidth) || (srcHeight != destHeight)) {
		double scale_x = (double)destWidth / (double)srcWidth;
		double scale_y = (double)destHeight / (double)srcHeight;
		double offset_x = - srcX * scale_x;
		double offset_y = - srcY * scale_y;

		int destSizePixbuf = GDKPIXBUF.gdk_pixbuf_new (
			GDKPIXBUF.GDK_COLORSPACE_RGB,
			true, 8, destWidth, destHeight);
		GDKPIXBUF.gdk_pixbuf_scale(
			pixbuf.handle, // src,
			destSizePixbuf,
			0,
			0,
			destWidth, destHeight,
			offset_x, offset_y,
			scale_x, scale_y,
			GDKPIXBUF.GDK_INTERP_BILINEAR);
		pixbuf.handle = destSizePixbuf;
	}
	
	/* Paint it */
	GDKPIXBUF.gdk_pixbuf_render_to_drawable_alpha(
			pixbuf.handle,
			data.drawable,
			0, 0,
			destX, destY,
			destWidth, destHeight,
			GDKPIXBUF.GDK_PIXBUF_ALPHA_BILEVEL, 128,
			GDKPIXBUF.GDK_RGB_DITHER_NORMAL,
			0, 0
			);
}

/** 
 * Draws a line, using the foreground color, between the points 
 * (<code>x1</code>, <code>y1</code>) and (<code>x2</code>, <code>y2</code>).
 *
 * @param x1 the first point's x coordinate
 * @param y1 the first point's y coordinate
 * @param x2 the second point's x coordinate
 * @param y2 the second point's y coordinate
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public void drawLine(int x1, int y1, int x2, int y2) {
	OS.gdk_draw_line (data.drawable, handle, x1, y1, x2, y2);
}
/** 
 * Draws the outline of an oval, using the foreground color,
 * within the specified rectangular area.
 * <p>
 * The result is a circle or ellipse that fits within the 
 * rectangle specified by the <code>x</code>, <code>y</code>, 
 * <code>width</code>, and <code>height</code> arguments. 
 * </p><p> 
 * The oval covers an area that is <code>width + 1</code> 
 * pixels wide and <code>height + 1</code> pixels tall.
 * </p>
 *
 * @param x the x coordinate of the upper left corner of the oval to be drawn
 * @param y the y coordinate of the upper left corner of the oval to be drawn
 * @param width the width of the oval to be drawn
 * @param height the height of the oval to be drawn
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public void drawOval(int x, int y, int width, int height) {
	if (width < 0) {
		x = x + width;
		width = -width;
	}
	if (height < 0) {
		y = y + height;
		height = -height;
	}
	OS.gdk_draw_arc(data.drawable, handle, 0, x, y, width, height, 0, 23040);
}
/** 
 * Draws the closed polygon which is defined by the specified array
 * of integer coordinates, using the receiver's foreground color. The array 
 * contains alternating x and y values which are considered to represent
 * points which are the vertices of the polygon. Lines are drawn between
 * each consecutive pair, and between the first pair and last pair in the
 * array.
 *
 * @param pointArray an array of alternating x and y values which are the vertices of the polygon
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT if pointArray is null</li>
 * </ul>	
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public void drawPolygon(int[] pointArray) {
	if (pointArray == null) error(SWT.ERROR_NULL_ARGUMENT);
	short[] points = new short[pointArray.length];
	for (int i = 0; i < pointArray.length; i++) {
		points[i] = (short)pointArray[i];
	}
	OS.gdk_draw_polygon(data.drawable, handle, 0, points, points.length / 2);
}
/** 
 * Draws the polyline which is defined by the specified array
 * of integer coordinates, using the receiver's foreground color. The array 
 * contains alternating x and y values which are considered to represent
 * points which are the corners of the polyline. Lines are drawn between
 * each consecutive pair, but not between the first pair and last pair in
 * the array.
 *
 * @param pointArray an array of alternating x and y values which are the corners of the polyline
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the point array is null</li>
 * </ul>	
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public void drawPolyline(int[] pointArray) {
	if (pointArray == null) error(SWT.ERROR_NULL_ARGUMENT);
	short[] points = new short[pointArray.length];
	for (int i = 0; i < pointArray.length; i++) {
		points[i] = (short)pointArray[i];
	}
	OS.gdk_draw_lines(data.drawable, handle, points, points.length / 2);
}
/** 
 * Draws the outline of the rectangle specified by the arguments,
 * using the receiver's foreground color. The left and right edges
 * of the rectangle are at <code>x</code> and <code>x + width</code>. 
 * The top and bottom edges are at <code>y</code> and <code>y + height</code>. 
 *
 * @param x the x coordinate of the rectangle to be drawn
 * @param y the y coordinate of the rectangle to be drawn
 * @param width the width of the rectangle to be drawn
 * @param height the height of the rectangle to be drawn
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public void drawRectangle(int x, int y, int width, int height) {
	if (width < 0) {
		x = x + width;
		width = -width;
	}
	if (height < 0) {
		y = y + height;
		height = -height;
	}
	OS.gdk_draw_rectangle(data.drawable, handle, 0, x, y, width, height);
}
/** 
 * Draws the outline of the specified rectangle, using the receiver's
 * foreground color. The left and right edges of the rectangle are at
 * <code>rect.x</code> and <code>rect.x + rect.width</code>. The top 
 * and bottom edges are at <code>rect.y</code> and 
 * <code>rect.y + rect.height</code>. 
 *
 * @param rect the rectangle to draw
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the rectangle is null</li>
 * </ul>	
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public void drawRectangle(Rectangle rect) {
	if (rect == null) error(SWT.ERROR_NULL_ARGUMENT);
	drawRectangle (rect.x, rect.y, rect.width, rect.height);
}
/** 
 * Draws the outline of the round-cornered rectangle specified by 
 * the arguments, using the receiver's foreground color. The left and
 * right edges of the rectangle are at <code>x</code> and <code>x + width</code>. 
 * The top and bottom edges are at <code>y</code> and <code>y + height</code>.
 * The <em>roundness</em> of the corners is specified by the 
 * <code>arcWidth</code> and <code>arcHeight</code> arguments. 
 *
 * @param x the x coordinate of the rectangle to be drawn
 * @param y the y coordinate of the rectangle to be drawn
 * @param width the width of the rectangle to be drawn
 * @param height the height of the rectangle to be drawn
 * @param arcWidth the horizontal diameter of the arc at the four corners
 * @param arcHeight the vertical diameter of the arc at the four corners
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public void drawRoundRectangle(int x, int y, int width, int height, int arcWidth, int arcHeight) {
	int nx = x;
	int ny = y;
	int nw = width;
	int nh = height;
	int naw = arcWidth;
	int nah = arcHeight;
	
	if (nw < 0) {
		nw = 0 - nw;
		nx = nx - nw;
	}
	if (nh < 0) {
		nh = 0 - nh;
		ny = ny -nh;
	}
	if (naw < 0) naw = 0 - naw;
	if (nah < 0) nah = 0 - nah;
				
	int naw2 = Compatibility.floor(naw, 2);
	int nah2 = Compatibility.floor(nah, 2);

	OS.gdk_draw_arc(data.drawable, handle, 0, nx, ny, naw, nah, 5760, 5760);
	OS.gdk_draw_arc(data.drawable, handle, 0, nx, ny + nh - nah, naw, nah, 11520, 5760);
	OS.gdk_draw_arc(data.drawable, handle, 0, nx + nw - naw, ny + nh - nah, naw, nah, 17280, 5760);
	OS.gdk_draw_arc(data.drawable, handle, 0, nx + nw - naw, ny, naw, nah, 0, 5760);
	OS.gdk_draw_line(data.drawable, handle, nx + naw2, ny, nx + nw - naw2, ny);
	OS.gdk_draw_line(data.drawable, handle, nx,ny + nah2, nx, ny + nh - nah2);
	OS.gdk_draw_line(data.drawable, handle, nx + naw2, ny + nh, nx + nw - naw2, ny + nh);
	OS.gdk_draw_line(data.drawable, handle, nx + nw, ny + nah2, nx + nw, ny + nh - nah2);
}
/** 
 * Draws the given string, using the receiver's current font and
 * foreground color. No tab expansion or carriage return processing
 * will be performed. The background of the rectangular area where
 * the string is being drawn will be filled with the receiver's
 * background color.
 *
 * @param string the string to be drawn
 * @param x the x coordinate of the top left corner of the rectangular area where the string is to be drawn
 * @param y the y coordinate of the top left corner of the rectangular area where the string is to be drawn
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the string is null</li>
 * </ul>	
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public void drawString (String string, int x, int y) {
	drawString(string, x, y, false);
}
/** 
 * Draws the given string, using the receiver's current font and
 * foreground color. No tab expansion or carriage return processing
 * will be performed. If <code>isTransparent</code> is <code>true</code>,
 * then the background of the rectangular area where the string is being
 * drawn will not be modified, otherwise it will be filled with the
 * receiver's background color.
 *
 * @param string the string to be drawn
 * @param x the x coordinate of the top left corner of the rectangular area where the string is to be drawn
 * @param y the y coordinate of the top left corner of the rectangular area where the string is to be drawn
 * @param isTransparent if <code>true</code> the background will be transparent, otherwise it will be opaque
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the string is null</li>
 * </ul>	
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public void drawString(String string, int x, int y, boolean isTransparent) {
	if (string == null) error(SWT.ERROR_NULL_ARGUMENT);
	byte[] buffer = Converter.wcsToMbcs(null, string, true);
	byte[] buffer1 = Converter.wcsToMbcs(null, "Y", true);
	int[] unused = new int[1];
	int[] width = new int[1];
	int[] ascent = new int[1];
	int[] average_ascent = new int [1];
	int fontHandle = _getGCFont();
	OS.gdk_string_extents(fontHandle, buffer, unused, unused, width, ascent, unused);
	OS.gdk_string_extents(fontHandle, buffer1, unused, unused, unused, average_ascent, unused);
	if (ascent[0]<average_ascent[0]) ascent[0] = average_ascent[0];
	if (!isTransparent) {
		int height = OS.gdk_string_height(fontHandle, buffer);
		GdkGCValues values = new GdkGCValues();
		OS.gdk_gc_get_values(handle, values);
		GdkColor color = new GdkColor();
		color.pixel = values.background_pixel;
		color.red = values.background_red;
		color.green = values.background_green;
		color.blue = values.background_blue;
		OS.gdk_gc_set_foreground(handle, color);
		OS.gdk_draw_rectangle(data.drawable, handle, 1, x, y, width[0], height);
		color.pixel = values.foreground_pixel;
		color.red = values.foreground_red;
		color.green = values.foreground_green;
		color.blue = values.foreground_blue;
		OS.gdk_gc_set_foreground(handle, color);
	}
	OS.gdk_draw_string(data.drawable, fontHandle, handle, x, y + ascent[0], buffer);
}
/** 
 * Draws the given string, using the receiver's current font and
 * foreground color. Tab expansion and carriage return processing
 * are performed. The background of the rectangular area where
 * the text is being drawn will be filled with the receiver's
 * background color.
 *
 * @param string the string to be drawn
 * @param x the x coordinate of the top left corner of the rectangular area where the text is to be drawn
 * @param y the y coordinate of the top left corner of the rectangular area where the text is to be drawn
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the string is null</li>
 * </ul>	
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public void drawText(String string, int x, int y) {
	drawText(string, x, y, false);
}
/** 
 * Draws the given string, using the receiver's current font and
 * foreground color. Tab expansion and carriage return processing
 * are performed. If <code>isTransparent</code> is <code>true</code>,
 * then the background of the rectangular area where the text is being
 * drawn will not be modified, otherwise it will be filled with the
 * receiver's background color.
 *
 * @param string the string to be drawn
 * @param x the x coordinate of the top left corner of the rectangular area where the text is to be drawn
 * @param y the y coordinate of the top left corner of the rectangular area where the text is to be drawn
 * @param isTransparent if <code>true</code> the background will be transparent, otherwise it will be opaque
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the string is null</li>
 * </ul>	
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public void drawText(String string, int x, int y, boolean isTransparent) {
	if (string == null) error(SWT.ERROR_NULL_ARGUMENT);
	byte[] buffer = Converter.wcsToMbcs(null, string, true);
	byte[] buffer1 = Converter.wcsToMbcs(null, "Y", true);
	int fontHandle = _getGCFont();
	int[] unused = new int[1];
	int[] width = new int[1];
	int[] ascent = new int[1];
	int[] average_ascent = new int [1];
	OS.gdk_string_extents(fontHandle, buffer, unused, unused, width, ascent, unused);
	OS.gdk_string_extents(fontHandle, buffer1, unused, unused, unused, average_ascent, unused);
	if (ascent[0]<average_ascent[0]) ascent[0] = average_ascent[0];
	if (!isTransparent) {
		int height = OS.gdk_string_height(fontHandle, buffer);
		GdkGCValues values = new GdkGCValues();
		OS.gdk_gc_get_values(handle, values);
		GdkColor color = new GdkColor();
		color.pixel = values.background_pixel;
		color.red = values.background_red;
		color.green = values.background_green;
		color.blue = values.background_blue;
		OS.gdk_gc_set_foreground(handle, color);
		OS.gdk_draw_rectangle(data.drawable, handle, 1, x, y, width[0], height);
		color.pixel = values.foreground_pixel;
		color.red = values.foreground_red;
		color.green = values.foreground_green;
		color.blue = values.foreground_blue;
		OS.gdk_gc_set_foreground(handle, color);
	}
	OS.gdk_draw_string(data.drawable, fontHandle, handle, x, y + ascent[0], buffer);
}

/** 
 * Draws the given string, using the receiver's current font and
 * foreground color. Tab expansion, line delimiter and mnemonic
 * processing are performed according to the specified flags. If
 * <code>flags</code> includes <code>DRAW_TRANSPARENT</code>,
 * then the background of the rectangular area where the text is being
 * drawn will not be modified, otherwise it will be filled with the
 * receiver's background color.
 * <p>
 * The parameter <code>flags</code> may be a combination of:
 * <dl>
 * <dt><b>DRAW_DELIMITER</b></dt>
 * <dd>draw multiple lines</dd>
 * <dt><b>DRAW_TAB</b></dt>
 * <dd>expand tabs</dd>
 * <dt><b>DRAW_MNEMONIC</b></dt>
 * <dd>underline the mnemonic character</dd>
 * <dt><b>DRAW_TRANSPARENT</b></dt>
 * <dd>transparent background</dd>
 * </dl>
 * </p>
 *
 * @param string the string to be drawn
 * @param x the x coordinate of the top left corner of the rectangular area where the text is to be drawn
 * @param y the y coordinate of the top left corner of the rectangular area where the text is to be drawn
 * @param flags the flags specifing how to process the text
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the string is null</li>
 * </ul>	
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public void drawText (String string, int x, int y, int flags) {
	// NOT IMPLEMENTED
	drawText(string, x, y, (flags & SWT.DRAW_TRANSPARENT) != 0);
}

/**
 * Fills the interior of a circular or elliptical arc within
 * the specified rectangular area, with the receiver's background
 * color.
 * <p>
 * The resulting arc begins at <code>startAngle</code> and extends  
 * for <code>arcAngle</code> degrees, using the current color.
 * Angles are interpreted such that 0 degrees is at the 3 o'clock
 * position. A positive value indicates a counter-clockwise rotation
 * while a negative value indicates a clockwise rotation.
 * </p><p>
 * The center of the arc is the center of the rectangle whose origin 
 * is (<code>x</code>, <code>y</code>) and whose size is specified by the 
 * <code>width</code> and <code>height</code> arguments. 
 * </p><p>
 * The resulting arc covers an area <code>width + 1</code> pixels wide
 * by <code>height + 1</code> pixels tall.
 * </p>
 *
 * @param x the x coordinate of the upper-left corner of the arc to be filled
 * @param y the y coordinate of the upper-left corner of the arc to be filled
 * @param width the width of the arc to be filled
 * @param height the height of the arc to be filled
 * @param startAngle the beginning angle
 * @param arcAngle the angular extent of the arc, relative to the start angle
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if any of the width, height or endAngle is zero.</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 *
 * @see #drawArc
 */
public void fillArc(int x, int y, int width, int height, int startAngle, int endAngle) {
	if (width < 0) {
		x = x + width;
		width = -width;
	}
	if (height < 0) {
		y = y + height;
		height = -height;
	}
	if (width == 0 || height == 0 || endAngle == 0) {
		error(SWT.ERROR_INVALID_ARGUMENT);
	}
	GdkGCValues values = new GdkGCValues();
	OS.gdk_gc_get_values(handle, values);
	GdkColor color = new GdkColor();
	color.pixel = values.background_pixel;
	color.red = values.background_red;
	color.green = values.background_green;
	color.blue = values.background_blue;
	OS.gdk_gc_set_foreground(handle, color);
	OS.gdk_draw_arc(data.drawable, handle, 1, x, y, width, height, startAngle * 64, endAngle * 64);
	color.pixel = values.foreground_pixel;
	color.red = values.foreground_red;
	color.green = values.foreground_green;
	color.blue = values.foreground_blue;
	OS.gdk_gc_set_foreground(handle, color);
}

/**
 * Fills the interior of the specified rectangle with a gradient
 * sweeping from left to right or top to bottom progressing
 * from the receiver's foreground color to its background color.
 *
 * @param x the x coordinate of the rectangle to be filled
 * @param y the y coordinate of the rectangle to be filled
 * @param width the width of the rectangle to be filled, may be negative
 *        (inverts direction of gradient if horizontal)
 * @param height the height of the rectangle to be filled, may be negative
 *        (inverts direction of gradient if vertical)
 * @param vertical if true sweeps from top to bottom, else 
 *        sweeps from left to right
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 *
 * @see #drawRectangle
 */
public void fillGradientRectangle(int x, int y, int width, int height, boolean vertical) {
	if (handle == 0) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if ((width == 0) || (height == 0)) return;

	GdkGCValues values = new GdkGCValues();
	OS.gdk_gc_get_values(handle, values);
	GdkColor foregroundGdkColor = new GdkColor();
	
	RGB backgroundRGB, foregroundRGB;
	backgroundRGB = Color.gtk_getRGBIntensities(_getBackgroundGdkColor());
	foregroundRGB = Color.gtk_getRGBIntensities(_getForegroundGdkColor());

	RGB fromRGB, toRGB;
	fromRGB = foregroundRGB;
	toRGB   = backgroundRGB;
	boolean swapColors = false;
	if (width < 0) {
		x += width; width = -width;
		if (! vertical) swapColors = true;
	}
	if (height < 0) {
		y += height; height = -height;
		if (vertical) swapColors = true;
	}
	if (swapColors) {
		fromRGB = backgroundRGB;
		toRGB   = foregroundRGB;
	}
	if (fromRGB == toRGB) {
		fillRectangle(x, y, width, height);
		return;
	}
	ImageData.fillGradientRectangle(this, Display.getCurrent(),
		x, y, width, height, vertical, fromRGB, toRGB,
		8, 8, 8);
}

/** 
 * Fills the interior of an oval, within the specified
 * rectangular area, with the receiver's background
 * color.
 *
 * @param x the x coordinate of the upper left corner of the oval to be filled
 * @param y the y coordinate of the upper left corner of the oval to be filled
 * @param width the width of the oval to be filled
 * @param height the height of the oval to be filled
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 *
 * @see #drawOval
 */
public void fillOval(int x, int y, int width, int height) {
	if (width < 0) {
		x = x + width;
		width = -width;
	}
	if (height < 0) {
		y = y + height;
		height = -height;
	}
	GdkGCValues values = new GdkGCValues();
	OS.gdk_gc_get_values(handle, values);
	GdkColor color = new GdkColor();
	color.pixel = values.background_pixel;
	color.red = values.background_red;
	color.green = values.background_green;
	color.blue = values.background_blue;
	OS.gdk_gc_set_foreground(handle, color);
	OS.gdk_draw_arc(data.drawable, handle, 1, x, y, width, height, 0, 23040);
	color.pixel = values.foreground_pixel;
	color.red = values.foreground_red;
	color.green = values.foreground_green;
	color.blue = values.foreground_blue;
	OS.gdk_gc_set_foreground(handle, color);
}
/** 
 * Fills the interior of the closed polygon which is defined by the
 * specified array of integer coordinates, using the receiver's
 * background color. The array contains alternating x and y values
 * which are considered to represent points which are the vertices of
 * the polygon. Lines are drawn between each consecutive pair, and
 * between the first pair and last pair in the array.
 *
 * @param pointArray an array of alternating x and y values which are the vertices of the polygon
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT if pointArray is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 *
 * @see #drawPolygon	
 */
public void fillPolygon(int[] pointArray) {
	if (pointArray == null) error(SWT.ERROR_NULL_ARGUMENT);
	short[] points = new short[pointArray.length];
	for (int i = 0; i < pointArray.length; i++) {
		points[i] = (short)pointArray[i];
	}
	GdkGCValues values = new GdkGCValues();
	OS.gdk_gc_get_values(handle, values);
	GdkColor color = new GdkColor();
	color.pixel = values.background_pixel;
	color.red = values.background_red;
	color.green = values.background_green;
	color.blue = values.background_blue;
	OS.gdk_gc_set_foreground(handle, color);
	OS.gdk_draw_polygon(data.drawable, handle, 1, points, points.length / 2);
	color.pixel = values.foreground_pixel;
	color.red = values.foreground_red;
	color.green = values.foreground_green;
	color.blue = values.foreground_blue;
	OS.gdk_gc_set_foreground(handle, color);
}
/** 
 * Fills the interior of the rectangle specified by the arguments,
 * using the receiver's background color. 
 *
 * @param x the x coordinate of the rectangle to be filled
 * @param y the y coordinate of the rectangle to be filled
 * @param width the width of the rectangle to be filled
 * @param height the height of the rectangle to be filled
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 *
 * @see #drawRectangle
 */
public void fillRectangle(int x, int y, int width, int height) {
	if (width < 0) {
		x = x + width;
		width = -width;
	}
	if (height < 0) {
		y = y + height;
		height = -height;
	}

	GdkGCValues values = new GdkGCValues();
	OS.gdk_gc_get_values(handle, values);
	GdkColor color = new GdkColor();
	color.pixel = values.background_pixel;
	color.red = values.background_red;
	color.green = values.background_green;
	color.blue = values.background_blue;
	OS.gdk_gc_set_foreground(handle, color);
	OS.gdk_draw_rectangle(data.drawable, handle, 1, x, y, width, height);
	color.pixel = values.foreground_pixel;
	color.red = values.foreground_red;
	color.green = values.foreground_green;
	color.blue = values.foreground_blue;
	OS.gdk_gc_set_foreground(handle, color);
}
/** 
 * Fills the interior of the specified rectangle, using the receiver's
 * background color. 
 *
 * @param rectangle the rectangle to be filled
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the rectangle is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 *
 * @see #drawRectangle
 */
public void fillRectangle(Rectangle rect) {
	if (rect == null) error(SWT.ERROR_NULL_ARGUMENT);
	fillRectangle(rect.x, rect.y, rect.width, rect.height);
}
/** 
 * Fills the interior of the round-cornered rectangle specified by 
 * the arguments, using the receiver's background color. 
 *
 * @param x the x coordinate of the rectangle to be filled
 * @param y the y coordinate of the rectangle to be filled
 * @param width the width of the rectangle to be filled
 * @param height the height of the rectangle to be filled
 * @param arcWidth the horizontal diameter of the arc at the four corners
 * @param arcHeight the vertical diameter of the arc at the four corners
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 *
 * @see #drawRoundRectangle
 */
public void fillRoundRectangle(int x, int y, int width, int height, int arcWidth, int arcHeight) {
	int nx = x;
	int ny = y;
	int nw = width;
	int nh = height;
	int naw = arcWidth;
	int nah = arcHeight;
	
	if (nw < 0) {
		nw = 0 - nw;
		nx = nx - nw;
	}
	if (nh < 0) {
		nh = 0 - nh;
		ny = ny -nh;
	}
	if (naw < 0) 
		naw = 0 - naw;
	if (nah < 0)
		nah = 0 - nah;

	naw = Math.min(naw,nw);
	nah = Math.min(nah, nh);
		
	int naw2 = Compatibility.round(naw, 2);
	int nah2 = Compatibility.round(nah, 2);

	GdkGCValues values = new GdkGCValues();
	OS.gdk_gc_get_values(handle, values);
	GdkColor color = new GdkColor();
	color.pixel = values.background_pixel;
	color.red = values.background_red;
	color.green = values.background_green;
	color.blue = values.background_blue;
	OS.gdk_gc_set_foreground(handle, color);
	OS.gdk_draw_arc(data.drawable, handle, 1, nx, ny, naw, nah, 5760, 5760);
	OS.gdk_draw_arc(data.drawable, handle, 1, nx, ny + nh - nah, naw, nah, 11520, 5760);
	OS.gdk_draw_arc(data.drawable, handle, 1, nx + nw - naw, ny + nh - nah, naw, nah, 17280, 5760);
	OS.gdk_draw_arc(data.drawable, handle, 1, nx + nw - naw, ny, naw, nah, 0, 5760);
	OS.gdk_draw_rectangle(data.drawable, handle, 1, nx + naw2, ny, nw - naw, nh);
	OS.gdk_draw_rectangle(data.drawable, handle, 1, nx, ny + nah2, naw2, nh - nah);
	OS.gdk_draw_rectangle(data.drawable, handle, 1, nx + nw - (naw / 2), ny + nah2, naw2, nh -nah);
	color.pixel = values.foreground_pixel;
	color.red = values.foreground_red;
	color.green = values.foreground_green;
	color.blue = values.foreground_blue;
	OS.gdk_gc_set_foreground(handle, color);
}

/**
 * Compares the argument to the receiver, and returns true
 * if they represent the <em>same</em> object using a class
 * specific comparison.
 *
 * @param object the object to compare with this object
 * @return <code>true</code> if the object is the same as this object and <code>false</code> otherwise
 *
 * @see #hashCode
 */
/*
 *   ===  As yet unclassified  ===
 */
  
public boolean equals(Object object) {
	return (object == this) || ((object instanceof GC) && (handle == ((GC)object).handle));
}
/**
 * Returns an integer hash code for the receiver. Any two 
 * objects which return <code>true</code> when passed to 
 * <code>equals</code> must return the same value for this
 * method.
 *
 * @return the receiver's hash
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 *
 * @see #equals
 */
public int hashCode() {
	return handle;
}
void error(int code) {
	throw new SWTError(code);
}
/**
 * Returns <code>true</code> if the GC has been disposed,
 * and <code>false</code> otherwise.
 * <p>
 * This method gets the dispose state for the GC.
 * When a GC has been disposed, it is an error to
 * invoke any other method using the GC.
 *
 * @return <code>true</code> when the GC is disposed and <code>false</code> otherwise
 */
public boolean isDisposed() {
	return handle == 0;
}
/**
 * Disposes of the operating system resources associated with
 * the graphics context. Applications must dispose of all GCs
 * which they allocate.
 */
public void dispose() {
	if (handle == 0) return;
	
	/* Free resources */
	int clipRgn = data.clipRgn;
	if (clipRgn != 0) OS.gdk_region_destroy(clipRgn);
	Image image = data.image;
	if (image != null) image.memGC = null;

	/* Dispose the GC */
	if(drawable == null)
		OS.gdk_gc_unref(handle);
	else
		drawable.internal_dispose_GC(handle, data);

	data.drawable = // data.colormap = data.fontList = 
		data.clipRgn = data.renderTable = 0;
	drawable = null;
	data.image = null;
	data = null;
	handle = 0;
	
}

/**
 * Returns the extent of the given string. Tab expansion, line
 * delimiter and mnemonic processing are performed according to
 * the specified flags, which can be a combination of:
 * <dl>
 * <dt><b>DRAW_DELIMITER</b></dt>
 * <dd>draw multiple lines</dd>
 * <dt><b>DRAW_TAB</b></dt>
 * <dd>expand tabs</dd>
 * <dt><b>DRAW_MNEMONIC</b></dt>
 * <dd>underline the mnemonic character</dd>
 * <dt><b>DRAW_TRANSPARENT</b></dt>
 * <dd>transparent background</dd>
 * </dl>
 * <p>
 * The <em>extent</em> of a string is the width and height of
 * the rectangular area it would cover if drawn in a particular
 * font (in this case, the current font in the receiver).
 * </p>
 *
 * @param string the string to measure
 * @param flags the flags specifing how to process the text
 * @return a point containing the extent of the string
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the string is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public Point textExtent(String string, int flags) {
	//NOT IMPLEMENTED
	return textExtent(string);
}

}
