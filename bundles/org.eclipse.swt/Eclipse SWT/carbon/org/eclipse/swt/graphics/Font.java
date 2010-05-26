/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
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
 * @see <a href="http://www.eclipse.org/swt/snippets/#font">Font snippets</a>
 * @see <a href="http://www.eclipse.org/swt/examples.php">SWT Examples: GraphicsExample, PaintExample</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
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
	 * 
	 * @noreference This field is not intended to be referenced by clients.
	 */
	public int handle;
	
	/**
	 * the style to the OS font (a FMFontStyle)
	 * (Warning: This field is platform dependent)
	 * <p>
	 * <b>IMPORTANT:</b> This field is <em>not</em> part of the SWT
	 * public API. It is marked public only so that it can be shared
	 * within the packages provided by SWT. It is not available on all
	 * platforms and should never be accessed from application code.
	 * </p>
	 * 
	 * @noreference This field is not intended to be referenced by clients.
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
	 * 
	 * @noreference This field is not intended to be referenced by clients.
	 */
	public float size;
	
	/**
	 * the ATSUI style for the OS font
	 * (Warning: This field is platform dependent)
	 * <p>
	 * <b>IMPORTANT:</b> This field is <em>not</em> part of the SWT
	 * public API. It is marked public only so that it can be shared
	 * within the packages provided by SWT. It is not available on all
	 * platforms and should never be accessed from application code.
	 * </p>
	 * 
	 * @noreference This field is not intended to be referenced by clients.
	 */
	public int atsuiStyle;
	
Font(Device device) {
	super(device);
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
	super(device);
	if (fd == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	init(fd.getName(), fd.getHeightF(), fd.getStyle(), fd.atsName);
	init();
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
	super(device);
	if (fds == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (fds.length == 0) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	for (int i=0; i<fds.length; i++) {
		if (fds[i] == null) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	}
	FontData fd = fds[0];
	init(fd.getName(), fd.getHeightF(), fd.getStyle(), fd.atsName);
	init();
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
	super(device);
	init(name, height, style, null);
	init();
}

/*public*/ Font(Device device, String name, float height, int style) {
	super(device);
	init(name, height, style, null);
	init();
}

int createStyle () {
	int[] buffer = new int[1];
	OS.ATSUCreateStyle(buffer);
	if (buffer[0] == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	int atsuStyle = buffer[0];

	boolean synthesize = style != 0;
	int ptr = OS.NewPtr(8 + (synthesize ? 8 : 0));
	OS.memmove(ptr, new int[]{OS.FMGetFontFromATSFontRef(handle)}, 4); 
	OS.memmove(ptr + 4, new int[]{OS.X2Fix(size)}, 4);
	int[] tags, sizes, values;
	if (synthesize) {
		OS.memmove(ptr + 8, new byte[]{(style & OS.bold) != 0 ? (byte)1 : 0}, 1); 
		OS.memmove(ptr + 9, new byte[]{(style & OS.italic) != 0 ? (byte)1 : 0}, 1);
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

void destroy() {
	if (handle == 0) return;
	handle = 0;
	if (atsuiStyle != 0) OS.ATSUDisposeStyle(atsuiStyle);
	atsuiStyle = 0;
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
	int[] buffer = new int[1];
	OS.ATSFontGetName(handle, 0, buffer);
	CFRange range = new CFRange();
	range.length = OS.CFStringGetLength(buffer[0]);
	char [] chars = new char[range.length];
	OS.CFStringGetCharacters(buffer[0], range, chars);
	OS.CFRelease(buffer[0]);
	String atsName = new String(chars);
	int platformCode = OS.kFontUnicodePlatform, encoding = OS.kCFStringEncodingUnicode;
	if (OS.ATSUFindFontName(handle, OS.kFontFamilyName, platformCode, OS.kFontNoScriptCode, OS.kFontNoLanguageCode, 0, null, buffer, null) != OS.noErr) {
		platformCode = OS.kFontNoPlatformCode;
		encoding = OS.kCFStringEncodingMacRoman;
		OS.ATSUFindFontName (handle, OS.kFontFamilyName, platformCode, OS.kFontNoScriptCode, OS.kFontNoLanguageCode, 0, null, buffer, null);
	}	
	byte[] bytes = new byte[buffer[0]];
	OS.ATSUFindFontName(handle, OS.kFontFamilyName, platformCode, OS.kFontNoScriptCode, OS.kFontNoLanguageCode, bytes.length, bytes, buffer, null);
	String name = "";
	int ptr = OS.CFStringCreateWithBytes(0, bytes, bytes.length, encoding, false);
	if (ptr != 0) {
		range.length = OS.CFStringGetLength(ptr);
		if (range.length != 0) {
			chars = new char[range.length];
			OS.CFStringGetCharacters(ptr, range, chars);
			name = new String(chars);
		}
		OS.CFRelease(ptr);
	}
	int style = 0;
	if ((this.style & OS.italic) != 0) style |= SWT.ITALIC;
	if ((this.style & OS.bold) != 0) style |= SWT.BOLD;
	if (atsName.indexOf("Italic") != -1) style |= SWT.ITALIC;
	if (atsName.indexOf("Bold") != -1) style |= SWT.BOLD;
	Point dpi = device.dpi, screenDPI = device.getScreenDPI();
	FontData data = new FontData(name, size * screenDPI.y / dpi.y, style);
	data.atsName = atsName;
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
 * @param style the style for the font
 * @param size the size for the font
 * 
 * @noreference This method is not intended to be referenced by clients.
 */
public static Font carbon_new(Device device, int handle, short style, float size) {
	Font font = new Font(device);
	font.handle = handle;
	font.style = style;
	font.size = size;
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

void init(String name, float height, int style, String atsName) {
	if (name == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (height < 0) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	int font = 0;
	if (atsName != null) {		
		int ptr = createCFString(atsName);
		if (ptr != 0) {
			font = OS.ATSFontFindFromName(ptr, OS.kATSOptionFlagsDefault);
			OS.CFRelease(ptr);
		}
	} else {
		atsName = name;
		if ((style & SWT.BOLD) != 0) atsName += " Bold";
		if ((style & SWT.ITALIC) != 0) atsName += " Italic";
		int ptr = createCFString(atsName);
		if (ptr != 0) {
			font = OS.ATSFontFindFromName(ptr, OS.kATSOptionFlagsDefault);
			OS.CFRelease(ptr);
		}
		if (font == 0 && (style & SWT.ITALIC) != 0) {
			this.style |= OS.italic;
			atsName = name;
			if ((style & SWT.BOLD) != 0) atsName += " Bold";
			ptr = createCFString(atsName);
			if (ptr != 0) {
				font = OS.ATSFontFindFromName(ptr, OS.kATSOptionFlagsDefault);
				OS.CFRelease(ptr);
			}
		}
		if (font == 0 && (style & SWT.BOLD) != 0) {
			this.style |= OS.bold;
			atsName = name;
			ptr = createCFString(atsName);
			if (ptr != 0) {
				font = OS.ATSFontFindFromName(ptr, OS.kATSOptionFlagsDefault);
				OS.CFRelease(ptr);
			}
		}
	}
	Point dpi = device.dpi, screenDPI = device.getScreenDPI();
	this.size = height * dpi.y / screenDPI.y;
	if (font == 0) {
		Font systemFont = device.systemFont;
		this.handle = systemFont.handle;
	} else {
		this.handle = font;
	}
	this.atsuiStyle = createStyle();
}

int createCFString(String str) {
	char[] chars = new char[str.length()];
	str.getChars(0, chars.length, chars, 0);
	return OS.CFStringCreateWithCharacters(OS.kCFAllocatorDefault, chars, chars.length);
}

/**
 * Returns <code>true</code> if the font has been disposed,
 * and <code>false</code> otherwise.
 * <p>
 * This method gets the dispose state for the font.
 * When a font has been disposed, it is an error to
 * invoke any other method (except {@link #dispose()}) using the font.
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
