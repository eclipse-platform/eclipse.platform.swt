/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
 * Portion Copyright (c) 2009-2010 compeople AG (http://www.compeople.de).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Compeople AG	- QtJambi/Qt based implementation for Windows/Mac OS X/Linux
 *******************************************************************************/
package org.eclipse.swt.graphics;

import com.trolltech.qt.gui.QFont;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.internal.qt.QtSupplementaryFontData;
import org.eclipse.swt.internal.qt.SWQT;

/**
 * Instances of this class manage operating system resources that define how
 * text looks when it is displayed. Fonts may be constructed by providing a
 * device and either name, size and style information or a <code>FontData</code>
 * object which encapsulates this data.
 * <p>
 * Application code must explicitly invoke the <code>Font.dispose()</code>
 * method to release the operating system resources managed by each instance
 * when those instances are no longer required.
 * </p>
 * 
 * @see FontData
 * @see <a href="http://www.eclipse.org/swt/snippets/#font">Font snippets</a>
 * @see <a href="http://www.eclipse.org/swt/examples.php">SWT Examples:
 *      GraphicsExample, PaintExample</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further
 *      information</a>
 */

public final class Font extends Resource {

	/**
	 * the handle to the OS font resource (Warning: This field is platform
	 * dependent)
	 * <p>
	 * <b>IMPORTANT:</b> This field is <em>not</em> part of the SWT public API.
	 * It is marked public only so that it can be shared within the packages
	 * provided by SWT. It is not available on all platforms and should never be
	 * accessed from application code.
	 * </p>
	 */
	private QFont font;

	boolean extraFontStyle;
	String xlfd;

	/**
	 * Prevents uninitialized instances from being created outside the package.
	 */
	Font() {
	}

	/**
	 * Prevents uninitialized instances from being created outside the package.
	 */
	Font(Device device) {
		super(device);
	}

	/**
	 * Constructs a new font given a device and font data which describes the
	 * desired font's appearance.
	 * <p>
	 * You must dispose the font when it is no longer required.
	 * </p>
	 * 
	 * @param device
	 *            the device to create the font on
	 * @param fd
	 *            the FontData that describes the desired font (must not be
	 *            null)
	 * 
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if device is null and there is
	 *                no current device</li>
	 *                <li>ERROR_NULL_ARGUMENT - if the fd argument is null</li>
	 *                </ul>
	 * @exception SWTError
	 *                <ul>
	 *                <li>ERROR_NO_HANDLES - if a font could not be created from
	 *                the given font data</li>
	 *                </ul>
	 */
	public Font(Device device, FontData fd) {
		super(device);
		init(fd);
		init();
	}

	/**
	 * Constructs a new font given a device and an array of font data which
	 * describes the desired font's appearance.
	 * <p>
	 * You must dispose the font when it is no longer required.
	 * </p>
	 * 
	 * @param device
	 *            the device to create the font on
	 * @param fds
	 *            the array of FontData that describes the desired font (must
	 *            not be null)
	 * 
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if device is null and there is
	 *                no current device</li>
	 *                <li>ERROR_NULL_ARGUMENT - if the fds argument is null</li>
	 *                <li>ERROR_INVALID_ARGUMENT - if the length of fds is zero</li>
	 *                <li>ERROR_NULL_ARGUMENT - if any fd in the array is null</li>
	 *                </ul>
	 * @exception SWTError
	 *                <ul>
	 *                <li>ERROR_NO_HANDLES - if a font could not be created from
	 *                the given font data</li>
	 *                </ul>
	 * 
	 * @since 2.1
	 */
	public Font(Device device, FontData[] fds) {
		super(device);
		if (fds == null) {
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		}
		if (fds.length == 0) {
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		}
		for (int i = 0; i < fds.length; i++) {
			if (fds[i] == null) {
				SWT.error(SWT.ERROR_INVALID_ARGUMENT);
			}
		}
		init(fds[0]);
		init();
	}

	/**
	 * Constructs a new font given a device, a font name, the height of the
	 * desired font in points, and a font style.
	 * <p>
	 * You must dispose the font when it is no longer required.
	 * </p>
	 * 
	 * @param device
	 *            the device to create the font on
	 * @param name
	 *            the name of the font (must not be null)
	 * @param height
	 *            the font height in points
	 * @param style
	 *            a bit or combination of NORMAL, BOLD, ITALIC
	 * 
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if device is null and there is
	 *                no current device</li>
	 *                <li>ERROR_NULL_ARGUMENT - if the name argument is null</li>
	 *                <li>ERROR_INVALID_ARGUMENT - if the height is negative</li>
	 *                </ul>
	 * @exception SWTError
	 *                <ul>
	 *                <li>ERROR_NO_HANDLES - if a font could not be created from
	 *                the given arguments</li>
	 *                </ul>
	 */
	public Font(Device device, String name, int height, int style) {
		super(device);
		if (name == null) {
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		}
		init(new FontData(name, height, style));
		init();
	}

	public QFont getQFont() {
		return font;
	}

	// /*public*/ Font(Device device, String name, float height, int style) {
	// super(device);
	// if (name == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	// init(new FontData (name, height, style));
	// init();
	// }
	@Override
	void destroy() {
		font.dispose(); // TODO_DISPOSE
		font = null;

	}

	/**
	 * Compares the argument to the receiver, and returns true if they represent
	 * the <em>same</em> object using a class specific comparison.
	 * 
	 * @param object
	 *            the object to compare with this object
	 * @return <code>true</code> if the object is the same as this object and
	 *         <code>false</code> otherwise
	 * 
	 * @see #hashCode
	 */
	@Override
	public boolean equals(Object object) {
		if (object == this) {
			return true;
		}
		if (!(object instanceof Font)) {
			return false;
		}
		Font font = (Font) object;
		return this.getQFont().equals(font.getQFont());
	}

	/**
	 * Returns an array of <code>FontData</code>s representing the receiver. On
	 * Windows, only one FontData will be returned per font. On X however, a
	 * <code>Font</code> object <em>may</em> be composed of multiple X fonts. To
	 * support this case, we return an array of font data objects.
	 * 
	 * @return an array of font data objects describing the receiver
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                </ul>
	 */
	public FontData[] getFontData() {
		if (isDisposed()) {
			SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
		}

		String family = font.family();
		int pointSize = font.pointSize();
		int weight = font.weight();
		boolean italic = font.italic();

		int style = SWT.NORMAL;
		if (weight > SWQT.QT_FONTNORMAL) {
			style |= SWT.BOLD;
		}
		if (italic) {
			style |= SWT.ITALIC;
		}
		FontData data = new FontData(family, pointSize, style);
		if (xlfd != null) {
			data.xlfd = xlfd;
		} else if (extraFontStyle) {
			data.extraFontData = new QtSupplementaryFontData();
			QtSupplementaryFontData extraData = data.extraFontData;
			extraData.underline = font.underline() ? 1 : 0;
			extraData.overline = font.overline() ? 1 : 0;
			extraData.strikeOut = font.strikeOut() ? 1 : 0;
			extraData.stretch = font.stretch();
			extraData.fixedPitch = font.fixedPitch() ? 1 : 0;
			extraData.style = font.style();
			extraData.weight = font.weight();
			extraData.styleStrategy = font.styleStrategy();
		}
		return new FontData[] { data };
	}

	/**
	 * Returns an integer hash code for the receiver. Any two objects that
	 * return <code>true</code> when passed to <code>equals</code> must return
	 * the same value for this method.
	 * 
	 * @return the receiver's hash
	 * 
	 * @see #equals
	 */
	@Override
	public int hashCode() {
		if (font == null) {
			return 0;
		}
		return font.toString().hashCode(); // TODO
	}

	void init(FontData fd) {
		if (fd == null) {
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		}
		int weight = QFont.Weight.Normal.value();
		boolean italic = false;
		if (fd.extraFontData != null) {
			weight = fd.extraFontData.weight;
			italic = fd.extraFontData.style == QFont.Style.StyleItalic;
		}

		font = new QFont(fd.name, fd.getHeight(), weight, italic);
		// TODO font can be BOLD AND ITALIC
		if (fd.style == SWT.BOLD) {
			font.setBold(true);
		}
		if (fd.style == SWT.ITALIC) {
			font.setItalic(true);
		}
		// int lfHeight = fontData.lfHeight;
		// fontData.lfHeight = device.computePixels(fd.height);
		// handle = OS.CreateFontIndirect(logFont);
		// fontData.lfHeight = lfHeight;
		// if (handle == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	}

	/**
	 * Returns <code>true</code> if the font has been disposed, and
	 * <code>false</code> otherwise.
	 * <p>
	 * This method gets the dispose state for the font. When a font has been
	 * disposed, it is an error to invoke any other method using the font.
	 * 
	 * @return <code>true</code> when the font is disposed and
	 *         <code>false</code> otherwise
	 */
	@Override
	public boolean isDisposed() {
		return font == null;
	}

	/**
	 * Returns a string containing a concise, human-readable description of the
	 * receiver.
	 * 
	 * @return a string representation of the receiver
	 */
	@Override
	public String toString() {
		if (isDisposed()) {
			return "Font {*DISPOSED*}"; //$NON-NLS-1$
		}
		return "Font {" + font.family() + "}"; //$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * Invokes platform specific functionality to allocate a new font.
	 * <p>
	 * <b>IMPORTANT:</b> This method is <em>not</em> part of the public API for
	 * <code>Font</code>. It is marked public only so that it can be shared
	 * within the packages provided by SWT. It is not available on all
	 * platforms, and should never be called from application code.
	 * </p>
	 * 
	 * @param device
	 *            the device on which to allocate the color
	 * @param handle
	 *            the handle for the font
	 * @return a new font object containing the specified device and handle
	 */
	public static Font qt_new(Device device, QFont fontParm) {
		if (fontParm == null) {
			return null;
		}
		if (device == null) {
			device = Device.getDevice();
		}
		if (device == null) {
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		}
		Font font = new Font();
		font.font = fontParm;
		font.device = device;
		return font;
	}

}
