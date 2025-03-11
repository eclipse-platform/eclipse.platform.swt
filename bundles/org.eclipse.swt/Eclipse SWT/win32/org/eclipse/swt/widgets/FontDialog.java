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
package org.eclipse.swt.widgets;


import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.win32.*;

/**
 * Instances of this class allow the user to select a font
 * from all available fonts in the system.
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>(none)</dd>
 * <dt><b>Events:</b></dt>
 * <dd>(none)</dd>
 * </dl>
 * <p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 *
 * @see <a href="http://www.eclipse.org/swt/examples.php">SWT Example: ControlExample, Dialog tab</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 * @noextend This class is not intended to be subclassed by clients.
 */
public class FontDialog extends Dialog {
	FontData fontData;
	RGB rgb;
	boolean effectsVisible = true;

/**
 * Constructs a new instance of this class given only its parent.
 *
 * @param parent a shell which will be the parent of the new instance
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the parent</li>
 *    <li>ERROR_INVALID_SUBCLASS - if this class is not an allowed subclass</li>
 * </ul>
 */
public FontDialog (Shell parent) {
	this (parent, SWT.APPLICATION_MODAL);
}

/**
 * Constructs a new instance of this class given its parent
 * and a style value describing its behavior and appearance.
 * <p>
 * The style value is either one of the style constants defined in
 * class <code>SWT</code> which is applicable to instances of this
 * class, or must be built by <em>bitwise OR</em>'ing together
 * (that is, using the <code>int</code> "|" operator) two or more
 * of those <code>SWT</code> style constants. The class description
 * lists the style constants that are applicable to the class.
 * Style bits are also inherited from superclasses.
 * </p>
 *
 * @param parent a shell which will be the parent of the new instance
 * @param style the style of dialog to construct
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the parent</li>
 *    <li>ERROR_INVALID_SUBCLASS - if this class is not an allowed subclass</li>
 * </ul>
 */
public FontDialog (Shell parent, int style) {
	super (parent, checkStyle (parent, style));
	checkSubclass ();
}

/**
 * Returns <code>true</code> if the dialog's effects selection controls
 * are visible, and <code>false</code> otherwise.
 * <p>
 * If the platform's font dialog does not have any effects selection controls,
 * then this method always returns false.
 * </p>
 *
 * @return <code>true</code> if the dialog's effects selection controls
 * are visible and <code>false</code> otherwise
 *
 * @since 3.8
 */
public boolean getEffectsVisible () {
	return effectsVisible;
}

/**
 * Returns a FontData object describing the font that was
 * selected in the dialog, or null if none is available.
 *
 * @return the FontData for the selected font, or null
 * @deprecated use #getFontList ()
 */
@Deprecated
public FontData getFontData () {
	return fontData;
}

/**
 * Returns a FontData set describing the font that was
 * selected in the dialog, or null if none is available.
 *
 * @return the FontData for the selected font, or null
 * @since 2.1.1
 */
public FontData [] getFontList () {
	if (fontData == null) return null;
	FontData [] result = new FontData [1];
	result [0] = fontData;
	return result;
}

/**
 * Returns an RGB describing the color that was selected
 * in the dialog, or null if none is available.
 *
 * @return the RGB value for the selected color, or null
 *
 * @see PaletteData#getRGBs
 *
 * @since 2.1
 */
public RGB getRGB () {
	return rgb;
}

/**
 * Makes the dialog visible and brings it to the front
 * of the display.
 *
 * @return a FontData object describing the font that was selected,
 *         or null if the dialog was cancelled or an error occurred
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the dialog has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the dialog</li>
 * </ul>
 */
public FontData open () {
	/* Get the owner HWND for the dialog */
	long hwndOwner = parent.handle;
	long hwndParent = parent.handle;

	/*
	* Feature in Windows.  There is no API to set the orientation of a
	* font dialog.  It is always inherited from the parent.  The fix is
	* to create a hidden parent and set the orientation in the hidden
	* parent for the dialog to inherit.
	*/
	boolean enabled = false;
	int dialogOrientation = style & (SWT.LEFT_TO_RIGHT | SWT.RIGHT_TO_LEFT);
	int parentOrientation = parent.style & (SWT.LEFT_TO_RIGHT | SWT.RIGHT_TO_LEFT);
	if (dialogOrientation != parentOrientation) {
		int exStyle = OS.WS_EX_NOINHERITLAYOUT;
		if (dialogOrientation == SWT.RIGHT_TO_LEFT) exStyle |= OS.WS_EX_LAYOUTRTL;
		hwndOwner = OS.CreateWindowEx (
			exStyle,
			Shell.DialogClass,
			null,
			0,
			OS.CW_USEDEFAULT, 0, OS.CW_USEDEFAULT, 0,
			hwndParent,
			0,
			OS.GetModuleHandle (null),
			null);
		enabled = OS.IsWindowEnabled (hwndParent);
		if (enabled) OS.EnableWindow (hwndParent, false);
	}

	/* Open the dialog */
	long hHeap = OS.GetProcessHeap ();
	CHOOSEFONT lpcf = new CHOOSEFONT ();
	lpcf.lStructSize = CHOOSEFONT.sizeof;
	lpcf.hwndOwner = hwndOwner;
	lpcf.Flags = OS.CF_SCREENFONTS;
	if (effectsVisible) {
		lpcf.Flags |= OS.CF_EFFECTS;
	}

	long lpLogFont = OS.HeapAlloc (hHeap, OS.HEAP_ZERO_MEMORY, LOGFONT.sizeof);
	if (fontData != null && fontData.data != null) {
		LOGFONT logFont = fontData.data;
		int lfHeight = logFont.lfHeight;
		int pixels = -(int)(0.5f + (fontData.height * getDPI() / 72));
		logFont.lfHeight = pixels;
		lpcf.Flags |= OS.CF_INITTOLOGFONTSTRUCT;
		OS.MoveMemory (lpLogFont, logFont, LOGFONT.sizeof);
		logFont.lfHeight = lfHeight;
	}
	lpcf.lpLogFont = lpLogFont;
	if (rgb != null) {
		int red = rgb.red & 0xFF;
		int green = (rgb.green << 8) & 0xFF00;
		int blue = (rgb.blue << 16) & 0xFF0000;
		lpcf.rgbColors = red | green | blue;
	}

	/* Make the parent shell be temporary modal */
	Dialog oldModal = null;
	Display display = parent.getDisplay ();
	if ((style & (SWT.APPLICATION_MODAL | SWT.SYSTEM_MODAL)) != 0) {
		oldModal = display.getModalDialog ();
		display.setModalDialog (this);
	}

	display.externalEventLoop = true;
	display.sendPreExternalEventDispatchEvent ();
	long currentDpiAwarenessContext = OS.GetThreadDpiAwarenessContext();
	boolean success = false;
	try {
		/*
		 * Temporarily setting the thread dpi awareness to gdi scaling because window
		 * dialog has weird resize handling
		 */
		if (display.isRescalingAtRuntime()) {
			currentDpiAwarenessContext = OS.SetThreadDpiAwarenessContext(OS.DPI_AWARENESS_CONTEXT_UNAWARE_GDISCALED);
		}

		/* Open the dialog */
		success = OS.ChooseFont(lpcf);
		display.externalEventLoop = false;
		display.sendPostExternalEventDispatchEvent();

		/* Clear the temporary dialog modal parent */
		if ((style & (SWT.APPLICATION_MODAL | SWT.SYSTEM_MODAL)) != 0) {
			display.setModalDialog(oldModal);
		}

		/* Compute the result */
		if (success) {
			LOGFONT logFont = new LOGFONT();
			OS.MoveMemory(logFont, lpLogFont, LOGFONT.sizeof);

			/*
			 * This will not work on multiple screens or for printing. Should use DC for the
			 * proper device.
			 */
			int logPixelsY = getDPI();
			int pixels = 0;
			long hDC = OS.GetDC(0);
			if (logFont.lfHeight > 0) {
				/*
				 * Feature in Windows. If the lfHeight of the LOGFONT structure is positive, the
				 * lfHeight measures the height of the entire cell, including internal leading,
				 * in logical units. Since the height of a font in points does not include the
				 * internal leading, we must subtract the internal leading, which requires a
				 * TEXTMETRIC, which in turn requires font creation.
				 */
				long hFont = OS.CreateFontIndirect(logFont);
				long oldFont = OS.SelectObject(hDC, hFont);
				TEXTMETRIC lptm = new TEXTMETRIC();
				OS.GetTextMetrics(hDC, lptm);
				OS.SelectObject(hDC, oldFont);
				OS.DeleteObject(hFont);
				pixels = logFont.lfHeight - lptm.tmInternalLeading;
			} else {
				pixels = -logFont.lfHeight;
			}
			OS.ReleaseDC(0, hDC);

			float points = pixels * 72f / logPixelsY;
			fontData = FontData.win32_new(logFont, points);
			if (effectsVisible) {
				int red = lpcf.rgbColors & 0xFF;
				int green = (lpcf.rgbColors >> 8) & 0xFF;
				int blue = (lpcf.rgbColors >> 16) & 0xFF;
				rgb = new RGB(red, green, blue);
			}
		}
	} finally {
		/* Reset the dpi awareness context */
		if (display.isRescalingAtRuntime()) {
			OS.SetThreadDpiAwarenessContext(currentDpiAwarenessContext);
		}
		/* Free the OS memory */
		if (lpLogFont != 0) OS.HeapFree (hHeap, 0, lpLogFont);

		/* Destroy the BIDI orientation window */
		if (hwndParent != hwndOwner) {
			if (enabled) OS.EnableWindow (hwndParent, true);
			OS.SetActiveWindow (hwndParent);
			OS.DestroyWindow (hwndOwner);
		}
		if (!success) return null;
	}

	return fontData;
}

private int getDPI() {
	long hDC = OS.GetDC (0);
	// We need to use OS.GetDeviceCaps, which is static throughout application
	// lifecycle (System DPI Aware), because we use
	// DPI_AWARENESS_CONTEXT_UNAWARE_GDISCALED which always depends on the DPI at
	// application startup
	int dpi = OS.GetDeviceCaps(hDC, OS.LOGPIXELSY);
	OS.ReleaseDC (0, hDC);
	return dpi;
}

/**
 * Sets the effects selection controls in the dialog visible if the
 * argument is <code>true</code>, and invisible otherwise.
 * <p>
 * By default the effects selection controls are displayed if the
 * platform font dialog supports effects selection.
 * </p>
 *
 * @param visible whether or not the dialog will show the effects selection controls
 *
 * @since 3.8
 */
public void setEffectsVisible(boolean visible) {
	effectsVisible = visible;
}

/**
 * Sets a FontData object describing the font to be
 * selected by default in the dialog, or null to let
 * the platform choose one.
 *
 * @param fontData the FontData to use initially, or null
 * @deprecated use #setFontList (FontData [])
 */
@Deprecated
public void setFontData (FontData fontData) {
	this.fontData = fontData;
}

/**
 * Sets the set of FontData objects describing the font to
 * be selected by default in the dialog, or null to let
 * the platform choose one.
 *
 * @param fontData the set of FontData objects to use initially, or null
 *        to let the platform select a default when open() is called
 *
 * @see Font#getFontData
 *
 * @since 2.1.1
 */
public void setFontList (FontData [] fontData) {
	if (fontData != null && fontData.length > 0) {
		this.fontData = fontData [0];
	} else {
		this.fontData = null;
	}
}

/**
 * Sets the RGB describing the color to be selected by default
 * in the dialog, or null to let the platform choose one.
 *
 * @param rgb the RGB value to use initially, or null to let
 *        the platform select a default when open() is called
 *
 * @see PaletteData#getRGBs
 *
 * @since 2.1
 */
public void setRGB (RGB rgb) {
	this.rgb = rgb;
}

}
