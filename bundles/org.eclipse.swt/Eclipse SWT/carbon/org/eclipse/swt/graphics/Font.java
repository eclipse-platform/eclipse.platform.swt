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


import org.eclipse.swt.internal.carbon.*;
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
public final class Font extends Resource {

	/**
	 * the handle to the OS font resource
	 * (Warning: This field is platform dependent)
	 * <p>
	 * <b>IMPORTANT:</b> This field is <em>not</em> part of the SWT
	 * public API. It is marked public only so that it can be shared
	 * within the packages provided by SWT. It is not available on all
	 * platforms and should never be accessed from application code.
	 * </p>
	 */
	public int handle;
	
	/**
	 * the id to the OS font (a FMFontFamily)
	 * (Warning: This field is platform dependent)
	 * <p>
	 * <b>IMPORTANT:</b> This field is <em>not</em> part of the SWT
	 * public API. It is marked public only so that it can be shared
	 * within the packages provided by SWT. It is not available on all
	 * platforms and should never be accessed from application code.
	 * </p>
	 */
	public short id;
	
	/**
	 * the style to the OS font (a FMFontStyle)
	 * (Warning: This field is platform dependent)
	 * <p>
	 * <b>IMPORTANT:</b> This field is <em>not</em> part of the SWT
	 * public API. It is marked public only so that it can be shared
	 * within the packages provided by SWT. It is not available on all
	 * platforms and should never be accessed from application code.
	 * </p>
	 */
	public short style;

	/**
	 * the size to the OS font
	 * (Warning: This field is platform dependent)
	 * <p>
	 * <b>IMPORTANT:</b> This field is <em>not</em> part of the SWT
	 * public API. It is marked public only so that it can be shared
	 * within the packages provided by SWT. It is not available on all
	 * platforms and should never be accessed from application code.
	 * </p>
	 */
	public short size;
	
	/**
	 * The ATSUI style for the font.
	 */
	int atsuiStyle;
	
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
 *    <li>ERROR_NULL_ARGUMENT - if device is null and there is no current device</li>
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
	init(device, fd.getName(), fd.getHeight(), fd.getStyle());
}

/**	 
 * Constructs a new font given a device and an array
 * of font data which describes the desired font's
 * appearance.
 * <p>
 * You must dispose the font when it is no longer required. 
 * </p>
 *
 * @param device the device to create the font on
 * @param fds the array of FontData that describes the desired font (must not be null)
 * 
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if device is null and there is no current device</li>
 *    <li>ERROR_NULL_ARGUMENT - if the fds argument is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if the length of fds is zero</li>
 *    <li>ERROR_NULL_ARGUMENT - if any fd in the array is null</li>
 * </ul>
 * @exception SWTError <ul>
 *    <li>ERROR_NO_HANDLES - if a font could not be created from the given font data</li>
 * </ul>
 * 
 * @since 2.1
 */
public Font(Device device, FontData[] fds) {
	if (device == null) device = Device.getDevice();
	if (device == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (fds == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (fds.length == 0) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	for (int i=0; i<fds.length; i++) {
		if (fds[i] == null) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	}
	FontData fd = fds[0];
	init(device,fd.getName(), fd.getHeight(), fd.getStyle());
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
 *    <li>ERROR_NULL_ARGUMENT - if device is null and there is no current device</li>
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
	init(device, name, height, style);
}

int createStyle () {
	int[] buffer = new int[1];
	OS.ATSUCreateStyle(buffer);
	if (buffer[0] == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	int atsuStyle = buffer[0];

	short[] realStyle = new short[1];
	OS.FMGetFontFromFontFamilyInstance(id, style, buffer, realStyle);
	boolean synthesize = style != realStyle[0];
	int ptr = OS.NewPtr(8 + (synthesize ? 8 : 0));
	OS.memcpy(ptr, new int[]{handle}, 4); 
	OS.memcpy(ptr + 4, new int[]{OS.X2Fix(size)}, 4);
	int[] tags, sizes, values;
	if (synthesize) {
		OS.memcpy(ptr + 8, new byte[]{(style & OS.bold) != 0 ? (byte)1 : 0}, 1); 
		OS.memcpy(ptr + 9, new byte[]{(style & OS.italic) != 0 ? (byte)1 : 0}, 1);
		tags = new int[]{OS.kATSUFontTag, OS.kATSUSizeTag, OS.kATSUQDBoldfaceTag, OS.kATSUQDItalicTag};
		sizes = new int[]{4, 4, 1, 1};
		values = new int[]{ptr, ptr + 4, ptr + 8, ptr + 9};
	} else {
		tags = new int[]{OS.kATSUFontTag, OS.kATSUSizeTag};
		sizes = new int[]{4, 4};
		values = new int[]{ptr, ptr + 4};
	}
	OS.ATSUSetAttributes(atsuStyle, tags.length, tags, sizes, values);
	OS.DisposePtr(ptr);
	
	short[] types = {
		(short)OS.kLigaturesType,
		(short)OS.kLigaturesType,
		(short)OS.kLigaturesType,
		(short)OS.kLigaturesType,
		(short)OS.kLigaturesType,
		(short)OS.kLigaturesType,
		(short)OS.kLigaturesType,
		(short)OS.kLigaturesType,
	};
	short[] selectors = {
		(short)OS.kRequiredLigaturesOffSelector,
		(short)OS.kCommonLigaturesOffSelector,
		(short)OS.kRareLigaturesOffSelector,
		(short)OS.kLogosOffSelector,
		(short)OS.kRebusPicturesOffSelector,
		(short)OS.kDiphthongLigaturesOffSelector,
		(short)OS.kSquaredLigaturesOffSelector,
		(short)OS.kAbbrevSquaredLigaturesOffSelector,
		(short)OS.kSymbolLigaturesOffSelector,
	};
	OS.ATSUSetFontFeatures(atsuStyle, types.length, types, selectors);
	return atsuStyle;
}

/**
 * Disposes of the operating system resources associated with
 * the font. Applications must dispose of all fonts which
 * they allocate.
 */
public void dispose() {
	if (handle == 0) return;
	handle = 0;
	id = -1;
	if (atsuiStyle != 0) OS.ATSUDisposeStyle(atsuiStyle);
	atsuiStyle = 0;
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
	Font font = (Font)object;
	return handle == font.handle && size == font.size;
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
	int [] actualLength = new int [1];
	int platformCode = OS.kFontUnicodePlatform, encoding = OS.kCFStringEncodingUnicode;
	if (OS.ATSUFindFontName(handle, OS.kFontFamilyName, platformCode, OS.kFontNoScriptCode, OS.kFontNoLanguageCode, 0, null, actualLength, null) != OS.noErr) {
		platformCode = OS.kFontNoPlatformCode;
		encoding = OS.kCFStringEncodingMacRoman;
		OS.ATSUFindFontName (handle, OS.kFontFamilyName, platformCode, OS.kFontNoScriptCode, OS.kFontNoLanguageCode, 0, null, actualLength, null);
	}	
	byte[] buffer = new byte[actualLength[0]];
	OS.ATSUFindFontName(handle, OS.kFontFamilyName, platformCode, OS.kFontNoScriptCode, OS.kFontNoLanguageCode, buffer.length, buffer, actualLength, null);
	String name = "";
	int ptr = OS.CFStringCreateWithBytes(0, buffer, buffer.length, encoding, false);
	if (ptr != 0) {
		int length = OS.CFStringGetLength(ptr);
		if (length != 0) {
			char[] chars = new char[length];
			CFRange range = new CFRange();
			range.length = length;
			OS.CFStringGetCharacters(ptr, range, chars);
			name = new String(chars);
		}
		OS.CFRelease(ptr);
	}
	int style = SWT.NORMAL;
	if ((this.style & OS.italic) != 0) style |= SWT.ITALIC;
	if ((this.style & OS.bold) != 0) style |= SWT.BOLD;
	FontData data = new FontData(name, size, style);
	return new FontData[]{data};
}

/**	 
 * Invokes platform specific functionality to allocate a new font.
 * <p>
 * <b>IMPORTANT:</b> This method is <em>not</em> part of the public
 * API for <code>Font</code>. It is marked public only so that it
 * can be shared within the packages provided by SWT. It is not
 * available on all platforms, and should never be called from
 * application code.
 * </p>
 *
 * @param device the device on which to allocate the color
 * @param handle the handle for the font
 * @param size the size for the font
 * 
 * @private
 */
public static Font carbon_new(Device device, int handle, short id, short style, short size) {
	if (device == null) device = Device.getDevice();
	Font font = new Font();
	font.handle = handle;
	font.id = id;
	font.style = style;
	font.size = size;
	font.device = device;
	return font;
}

/**
 * Returns an integer hash code for the receiver. Any two 
 * objects that return <code>true</code> when passed to 
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

void init(Device device, String name, int height, int style) {
	if (name == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (height < 0) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	this.device = device;
	int[] font = new int[1];
	byte[] buffer = name.getBytes();
	this.id = OS.kInvalidFontFamily;
	if (OS.ATSUFindFontFromName(buffer, buffer.length, OS.kFontFamilyName, OS.kFontNoPlatformCode, OS.kFontNoScriptCode, OS.kFontNoLanguageCode, font) == OS.noErr) {
		short[] family = new short[1];
		OS.FMGetFontFamilyInstanceFromFont(font[0], family, new short[1]);
		this.id = family[0];
	}
	if (this.id == OS.kInvalidFontFamily) this.id = OS.GetAppFont();
	if ((style & SWT.ITALIC) != 0) this.style |= OS.italic;
	if ((style & SWT.BOLD) != 0) this.style |= OS.bold;
	this.size = (short)height;
	OS.FMGetFontFromFontFamilyInstance(id, this.style, font, null);
	if (font[0] == 0) {
		Font systemFont = device.systemFont;
		this.handle = systemFont.handle;
	} else {
		this.handle = font[0];
	}
	this.atsuiStyle = createStyle();
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
