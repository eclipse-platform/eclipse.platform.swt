/*******************************************************************************
 * Copyright (c) 2000, 2014 IBM Corporation and others.
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
 *******************************************************************************/
package org.eclipse.swt.graphics;


import org.eclipse.swt.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.win32.*;

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
	public long handle;

	/**
	 * the zoom level to pixel height the OS font resource is scaled to
	 * (Warning: This field is platform dependent)
	 */
	int zoomFactor;
/**
 * Prevents uninitialized instances from being created outside the package.
 */
Font(Device device) {
	super(device);
	this.zoomFactor = extractZoomFactor(this.device);
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
 *
 * @see #dispose()
 */
public Font(Device device, FontData fd) {
	super(device);
	this.zoomFactor = extractZoomFactor(this.device);
	init(fd);
	init();
}

private Font(Device device, FontData fd, int zoomFactor) {
	super(device);
	this.zoomFactor = zoomFactor;
	init(fd);
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
 * @see #dispose()
 *
 * @since 2.1
 */
public Font(Device device, FontData[] fds) {
	super(device);
	if (fds == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (fds.length == 0) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	for (FontData fd : fds) {
		if (fd == null) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	}
	this.zoomFactor = extractZoomFactor(this.device);
	init(fds[0]);
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
 *
 * @see #dispose()
 */
public Font(Device device, String name, int height, int style) {
	super(device);
	if (name == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	this.zoomFactor = extractZoomFactor(this.device);
	init(new FontData (name, height, style));
	init();
}

/*public*/ Font(Device device, String name, float height, int style) {
	super(device);
	if (name == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	this.zoomFactor = extractZoomFactor(this.device);
	init(new FontData (name, height, style));
	init();
}
@Override
void destroy() {
	OS.DeleteObject(handle);
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
@Override
public boolean equals(Object object) {
	if (object == this) return true;
	if (!(object instanceof Font)) return false;
	Font font = (Font) object;
	return device == font.device && handle == font.handle;
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
	LOGFONT logFont = new LOGFONT ();
	OS.GetObject(handle, LOGFONT.sizeof, logFont);
	float heightInPoints = device.computePoints(logFont, handle);
	int primaryZoom = DPIUtil.mapDPIToZoom (device._getDPIx());
	float adjustedZoomFactor;
	if (zoomFactor != primaryZoom) {
		// as Device::computePoints will always return point on the basis of the
		// primary monitor zoom, a custom zoomFactor must be calculated if the font
		// is used for a different zoom level
		adjustedZoomFactor = 1.0f * primaryZoom / zoomFactor;
	} else {
		adjustedZoomFactor = 1.0f;
	}
	return new FontData[] {FontData.win32_new(logFont, heightInPoints * adjustedZoomFactor)};
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
@Override
public int hashCode () {
	return (int)handle;
}

void init (FontData fd) {
	if (fd == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	LOGFONT logFont = fd.data;
	int lfHeight = logFont.lfHeight;
	logFont.lfHeight = device.computePixels(fd.height);

	int primaryZoomFactor = DPIUtil.mapDPIToZoom (device._getDPIx());
	if (zoomFactor != primaryZoomFactor) {
		float scaleFactor = 1f * zoomFactor / primaryZoomFactor;
		logFont.lfHeight *= scaleFactor;
	}

	handle = OS.CreateFontIndirect(logFont);
	logFont.lfHeight = lfHeight;
	if (handle == 0) SWT.error(SWT.ERROR_NO_HANDLES);
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
@Override
public boolean isDisposed() {
	return handle == 0;
}

/**
 * Returns a string containing a concise, human-readable
 * description of the receiver.
 *
 * @return a string representation of the receiver
 */
@Override
public String toString () {
	if (isDisposed()) return "Font {*DISPOSED*}";
	return "Font {" + handle + "}";
}

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
public Font scaleFor(int targetZoomFactor) {
	if (targetZoomFactor == zoomFactor) {
		return this;
	}
	return getDevice().getFont(getFontData()[0], targetZoomFactor);
}

private static int extractZoomFactor(Device device) {
	if (device == null) {
		DPIUtil.getNativeDeviceZoom();
	}
	return DPIUtil.mapDPIToZoom (device._getDPIx());
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
 * @return a new font object containing the specified device and handle
 *
 * @noreference This method is not intended to be referenced by clients.
 */
public static Font win32_new(Device device, long handle) {
	Font font = new Font(device);
	font.zoomFactor = extractZoomFactor(font.device);
	font.handle = handle;
	/*
	 * When created this way, Font doesn't own its .handle, and
	 * for this reason it can't be disposed. Tell leak detector
	 * to just ignore it.
	 */
	font.ignoreNonDisposed();
	return font;
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
 * @param device the device on which to allocate the font
 * @param handle the handle for the font
 * @param zoomFactor the zoom factor
 * @return a new font object containing the specified device and handle
 *
 * @noreference This method is not intended to be referenced by clients.
 * @since 3.125
 */
public static Font win32_new(Device device, long handle, int zoomFactor) {
	Font font = win32_new(device, handle);
	font.zoomFactor = zoomFactor;
	return font;
}

/**
 * Invokes platform specific private constructor to allocate a new font.
 * <p>
 * <b>IMPORTANT:</b> This method is <em>not</em> part of the public
 * API for <code>Font</code>. It is marked public only so that it
 * can be shared within the packages provided by SWT. It is not
 * available on all platforms, and should never be called from
 * application code.
 * </p>
 *
 * @param device the device on which to allocate the font
 * @param fontData font data to create the font for
 * @param zoomFactor the zoom factor
 * @return a new font object containing the specified device and handle
 *
 * @noreference This method is not intended to be referenced by clients.
 * @since 3.125
 */
public static Font win32_new(Device device, FontData fontData, int zoomFactor) {
	return new Font(device, fontData, zoomFactor);
}
}
