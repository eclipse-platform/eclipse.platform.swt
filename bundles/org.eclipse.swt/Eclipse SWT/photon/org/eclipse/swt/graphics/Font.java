package org.eclipse.swt.graphics;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.photon.*;
import org.eclipse.swt.*;

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
	public byte[] handle;
	
	/**
	 * the device where this font was created
	 */
	Device device;
	
	static final byte[] DefaultFontName = {(byte)'h', (byte)'e', (byte)'l', (byte)'v'};
	static final byte[] DefaultFont = {(byte)'h', (byte)'e', (byte)'l', (byte)'v', (byte)'1', (byte)'2'};

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
public Font(Device device, FontData fd) {
	if (device == null) device = Device.getDevice();
	if (device == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (fd == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	init(device, fd.getName(), fd.getHeight(), fd.getStyle(), fd.stem);
	if (device.tracking) device.new_Object(this);	
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
public Font(Device device, String name, int height, int style) {
	if (device == null) device = Device.getDevice();
	if (device == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (name == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	init(device, name, height, style, null);
	if (device.tracking) device.new_Object(this);	
}

/**
 * Disposes of the operating system resources associated with
 * the font. Applications must dispose of all fonts which
 * they allocate.
 */
public void dispose() {
	if (handle == null) return;
		
	handle = null;
	if (device.tracking) device.dispose_Object(this);
	device = null;
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
	byte[] h = ((Font)object).handle;
	if (h == handle) return true;
	if (h == null || handle == null) return false;
	if (h.length != handle.length) return false;
	for (int i = 0; i < handle.length; i++) {
		if (handle[i] != h[i]) return false;
	}
	return true;
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
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	return new FontData[]{new FontData(handle)};
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
public int hashCode () {
	if (handle == null) return 0;
	return handle.hashCode();
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
	return handle == null;
}

void init(Device device, String name, int height, int style, byte[] stem) {
	if (height < 0) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	this.device = device;
	if (stem != null) {
		handle = stem;
	} else {
		byte[] description = (name == null) ? null : Converter.wcsToMbcs(null, name, true);
		int osStyle = 0;
		if ((style & SWT.BOLD) != 0) osStyle |= OS.PF_STYLE_BOLD;
		if ((style & SWT.ITALIC) != 0) osStyle |= OS.PF_STYLE_ITALIC;
		byte[] buffer = new byte[OS.MAX_FONT_TAG];
		handle = OS.PfGenerateFontName(description, osStyle, height, buffer);
		if (handle == null) handle = OS.PfGenerateFontName(DefaultFontName, osStyle, height, buffer);
	}
	if (handle == null) handle = DefaultFont;
	FontQueryInfo info = new FontQueryInfo();
	if (OS.PfQueryFontInfo(handle, info) == 0) handle = info.font;
}

public static Font photon_new(Device device, byte[] stem) {
	if (device == null) device = Device.getDevice();
	Font font = new Font();
	font.init(device, null, 0, 0, stem);
	return font;
}

/**
 * Returns a string containing a concise, human-readable
 * description of the receiver.
 *
 * @return a string representation of the receiver
 */
public String toString () {
	if (isDisposed()) return "Font {*DISPOSED*}";
	return "Font {" + new String(handle).trim() + "}";
}

}
