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


import org.eclipse.swt.internal.cocoa.*;
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
	 */
	public NSFont handle;
	
	/**
	 * the traits not supported to the OS font resource
	 * (Warning: This field is platform dependent)
	 * <p>
	 * <b>IMPORTANT:</b> This field is <em>not</em> part of the SWT
	 * public API. It is marked public only so that it can be shared
	 * within the packages provided by SWT. It is not available on all
	 * platforms and should never be accessed from application code.
	 * </p>
	 */
	public int extraTraits;
	
	static final double SYNTHETIC_BOLD = -2.5;
	static final double SYNTHETIC_ITALIC = 0.2;

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
	NSAutoreleasePool pool = null;
	if (!NSThread.isMainThread()) pool = (NSAutoreleasePool) new NSAutoreleasePool().alloc().init();
	try {
		init(fd.getName(), fd.getHeightF(), fd.getStyle(), fd.nsName);
		init();
	} finally {
		if (pool != null) pool.release();
	}
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
	NSAutoreleasePool pool = null;
	if (!NSThread.isMainThread()) pool = (NSAutoreleasePool) new NSAutoreleasePool().alloc().init();
	try {
		FontData fd = fds[0];
		init(fd.getName(), fd.getHeightF(), fd.getStyle(), fd.nsName);
		init();
	} finally {
		if (pool != null) pool.release();
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
	NSAutoreleasePool pool = null;
	if (!NSThread.isMainThread()) pool = (NSAutoreleasePool) new NSAutoreleasePool().alloc().init();
	try {
		init(name, height, style, null);
		init();
	} finally {
		if (pool != null) pool.release();
	}
}

/*public*/ Font(Device device, String name, float height, int style) {
	super(device);
	init(name, height, style, null);
	init();
}

void addTraits(NSMutableAttributedString attrStr, NSRange range) {
	if ((extraTraits & OS.NSBoldFontMask) != 0) {
		attrStr.addAttribute(OS.NSStrokeWidthAttributeName, NSNumber.numberWithDouble(SYNTHETIC_BOLD), range);
	}
	if ((extraTraits & OS.NSItalicFontMask) != 0) {
		attrStr.addAttribute(OS.NSObliquenessAttributeName, NSNumber.numberWithDouble(SYNTHETIC_ITALIC), range);
	}
}

void addTraits(NSMutableDictionary dict) {
	if ((extraTraits & OS.NSBoldFontMask) != 0) {
		dict.setObject(NSNumber.numberWithDouble(SYNTHETIC_BOLD), OS.NSStrokeWidthAttributeName);
	}
	if ((extraTraits & OS.NSItalicFontMask) != 0) {
		dict.setObject(NSNumber.numberWithDouble(SYNTHETIC_ITALIC), OS.NSObliquenessAttributeName);
	}
}

void destroy() {
	handle.release();
	handle = null;
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
	return handle == font.handle;
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
	NSAutoreleasePool pool = null;
	if (!NSThread.isMainThread()) pool = (NSAutoreleasePool) new NSAutoreleasePool().alloc().init();
	try {
		NSString family = handle.familyName();
		String name = family.getString();
		NSString str = handle.fontName();
		String nsName = str.getString();
		NSFontManager manager = NSFontManager.sharedFontManager();
		int /*long*/ traits = manager.traitsOfFont(handle);
		int /*long*/ weight = manager.weightOfFont(handle);
		int style = SWT.NORMAL;
		if ((traits & OS.NSItalicFontMask) != 0) style |= SWT.ITALIC;
		if (weight == 9) style |= SWT.BOLD;
		if ((extraTraits & OS.NSItalicFontMask) != 0) style |= SWT.ITALIC;
		if ((extraTraits & OS.NSBoldFontMask) != 0) style |= SWT.BOLD;
		Point dpi = device.dpi, screenDPI = device.getScreenDPI();
		FontData data = new FontData(name, (float)/*64*/handle.pointSize() * screenDPI.y / dpi.y, style);
		data.nsName = nsName;
		return new FontData[]{data};
	} finally {
		if (pool != null) pool.release();
	}
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
 * @private
 */
public static Font cocoa_new(Device device, NSFont handle) {
	Font font = new Font(device);
	font.handle = handle;
	font.handle.retain();
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
	return handle != null ? (int)/*64*/handle.id : 0;
}

void init(String name, float height, int style, String nsName) {
	if (name == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (height < 0) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	Point dpi = device.dpi, screenDPI = device.getScreenDPI();
	float size = height * dpi.y / screenDPI.y;
	if (nsName != null) {
		handle = NSFont.fontWithName(NSString.stringWith(nsName), size);
	} else {
		int traits = 0;
		if ((style & SWT.ITALIC) != 0) traits |= OS.NSItalicFontMask;
		int weight = 5;
		if ((style & SWT.BOLD) != 0) weight = 9;
		NSString family = NSString.stringWith(name);
		NSFontManager manager = NSFontManager.sharedFontManager();
		handle = manager.fontWithFamily(family, traits, weight, size);
		if (handle == null && (style & SWT.ITALIC) != 0) {
			traits &= ~OS.NSItalicFontMask;
			handle = manager.fontWithFamily(family, traits, weight, size);
		}
		if (handle == null && (style & SWT.BOLD) != 0) {
			weight = 5;
			handle = manager.fontWithFamily(family, traits, weight, size);
		}
		if (handle == null) {
			handle = NSFont.systemFontOfSize(size);
		}
		if ((style & SWT.ITALIC) != 0 && (manager.traitsOfFont(handle) & OS.NSItalicFontMask) == 0) {
			extraTraits |= OS.NSItalicFontMask;
		}
		if ((style & SWT.BOLD) != 0 && (manager.traitsOfFont(handle) & OS.NSBoldFontMask) == 0) {
			extraTraits |= OS.NSBoldFontMask;
		}
	}
	if (handle == null) {
		handle = device.systemFont.handle;
	}
	handle.retain();
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
