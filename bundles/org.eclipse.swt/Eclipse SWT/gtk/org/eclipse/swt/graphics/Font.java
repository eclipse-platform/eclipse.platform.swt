package org.eclipse.swt.graphics;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import org.eclipse.swt.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.gtk.*;
import org.eclipse.swt.widgets.*;

/**
 * Instances of this class manage operating system resources that
 * define how text looks when it is displayed. Fonts may be constructed
 * by providing a device and either name, size and style information
 * or a <code>FontData</code> object which encapsulates this data.
 * <p>
 * Application code must explicitly invoke the <code>Font.dispose()</code> 
 * method to release the operating system resources managed by each instance
 * when those instances are no longer required.
 * </p>
 *
 * @see FontData
 */
public final class Font {
	/**
	 * the handle to the OS font resource
	 * (Warning: This field is platform dependent)
	 */
	public int handle;
Font() {
}
/**	 
 * Constructs a new font given a device and font data
 * which describes the desired font's appearance.
 * <p>
 * You must dispose the font when it is no longer required. 
 * </p>
 *
 * @param device the device to create the font on
 * @param fd the FontData that describes the desired font (must not be null)
 * 
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the fd argument is null</li>
 * </ul>
 * @exception SWTError <ul>
 *    <li>ERROR_NO_HANDLES - if a font could not be created from the given font data</li>
 * </ul>
 */
public Font(Device display, FontData fd) {
	if (fd == null) error(SWT.ERROR_NULL_ARGUMENT);
	
	String xlfd = fd.getXlfd();
	byte[] buffer = Converter.wcsToMbcs(null, xlfd, true);
	handle = OS.gdk_font_load(buffer);
	if (handle == 0) {
		int hStyle = OS.gtk_widget_get_default_style();
		GtkStyle gtkStyle = new GtkStyle();
		OS.memmove(gtkStyle, hStyle, GtkStyle.sizeof);
		handle = OS.gdk_font_ref(gtkStyle.font);
	}
}
/**	 
 * Constructs a new font given a device, a font name,
 * the height of the desired font in points, and a font
 * style.
 * <p>
 * You must dispose the font when it is no longer required. 
 * </p>
 *
 * @param device the device to create the font on
 * @param name the name of the font (must not be null)
 * @param height the font height in points
 * @param style a bit or combination of NORMAL, BOLD, ITALIC
 * 
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the name argument is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if the height is negative</li>
 * </ul>
 * @exception SWTError <ul>
 *    <li>ERROR_NO_HANDLES - if a font could not be created from the given arguments</li>
 * </ul>
 */
public Font(Device display, String fontFamily, int height, int style) {
	if (fontFamily == null) error(SWT.ERROR_NULL_ARGUMENT);
	FontData fd = new FontData(fontFamily, height, style);
	byte[] buffer = Converter.wcsToMbcs(null, fd.getXlfd(), true);
	handle = OS.gdk_font_load(buffer);
	if (handle == 0) {
		int hStyle = OS.gtk_widget_get_default_style();
		GtkStyle gtkStyle = new GtkStyle();
		OS.memmove(gtkStyle, hStyle, GtkStyle.sizeof);
		handle = OS.gdk_font_ref(gtkStyle.font);
	}
}
/**
 * Disposes of the operating system resources associated with
 * the font. Applications must dispose of all fonts which
 * they allocate.
 */
public void dispose() {
	if (handle != 0) OS.gdk_font_unref(handle);
	handle = 0;
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
public boolean equals(Object object) {
	if (object == this) return true;
	if (!(object instanceof Font)) return false;
	return OS.gdk_font_equal(handle, ((Font)object).handle);
}
void error(int code) {
	throw new SWTError (code);
}

/**
 * Returns an array of <code>FontData</code>s representing the receiver.
 * On Windows, only one FontData will be returned per font. On X however, 
 * a <code>Font</code> object <em>may</em> be composed of multiple X 
 * fonts. To support this case, we return an array of font data objects.
 *
 * @return an array of font data objects describing the receiver
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public FontData[] getFontData() {
	int index=0;
	int fnames = getFontNameList(handle);
	int nfonts = OS.g_slist_length(fnames);
	FontData[] answer = new FontData[nfonts];
	for (int i=0; i<nfonts; i++) {
		FontData data = new FontData();
		
		int name = OS.g_slist_nth_data(fnames, index);
		int length = OS.strlen(name);
		byte [] buffer1 = new byte[length];
		OS.memmove(buffer1, name, length);
		char [] buffer2 = Converter.mbcsToWcs (null, buffer1);
		String fontname =  new String (buffer2, 0, buffer2.length);
		data.setXlfd(fontname);
		
		// Wild guess, 'a' looks average enough
		data.averageWidth = OS.gdk_char_width(handle, (byte)'a');
		
		// Wild guess, a progressive font should probably have A wider than l
		int widthA = OS.gdk_char_width(handle, (byte)'A');
		int widthl = OS.gdk_char_width(handle, (byte)'l');
		if (widthA == widthl) data.spacing = "m";
			else data.spacing = "p";
		
		answer[i] = data;
	}
	return answer;
}
static int getFontNameList(int handle) {
	int[] mem = new int[7];
	OS.memmove(mem, handle, 7*4);
	int type = mem[0];
	int ascent = mem[1];
	int descent = mem[2];
	int xfont =mem [3];
	int xdisplay = mem[4];
	int ref_count = mem[5];
	int names = mem[6];
	return names;
}
public static Font gtk_new(int handle) {
	Font font = new Font();
	font.handle = handle;
	return font;
}
/**
 * Returns an integer hash code for the receiver. Any two 
 * objects which return <code>true</code> when passed to 
 * <code>equals</code> must return the same value for this
 * method.
 *
 * @return the receiver's hash
 *
 * @see #equals
 */
public int hashCode() {
	return handle;
}
/**
 * Returns <code>true</code> if the font has been disposed,
 * and <code>false</code> otherwise.
 * <p>
 * This method gets the dispose state for the font.
 * When a font has been disposed, it is an error to
 * invoke any other method using the font.
 *
 * @return <code>true</code> when the font is disposed and <code>false</code> otherwise
 */
public boolean isDisposed() {
	return handle == 0;
}

/**
 * Returns a string containing a concise, human-readable
 * description of the receiver.
 *
 * @return a string representation of the receiver
 */
public String toString () {
	if (isDisposed()) return "Font {*DISPOSED*}";
	return "Font {" + handle + "}";
}

}
