/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.widgets;


import org.eclipse.swt.internal.win32.*;
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.Compatibility;

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
 * IMPORTANT: This class is intended to be subclassed <em>only</em>
 * within the SWT implementation.
 * </p>
 */
public class FontDialog extends Dialog {
	FontData fontData;
	RGB rgb;
	
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
	this (parent, SWT.PRIMARY_MODAL);
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
	super (parent, style);
	checkSubclass ();
}

/**
 * Returns a FontData object describing the font that was
 * selected in the dialog, or null if none is available.
 * 
 * @return the FontData for the selected font, or null
 * @deprecated use #getFontList ()
 */
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
 * Returns the currently selected color in the receiver.
 *
 * @return the RGB value for the selected color, may be null
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
	if (OS.IsWinCE) SWT.error (SWT.ERROR_NOT_IMPLEMENTED);
	
	/* Get the owner HWND for the dialog */
	int hwndOwner = 0;
	if (parent != null) hwndOwner = parent.handle;

	/* Open the dialog */
	int hHeap = OS.GetProcessHeap ();
	CHOOSEFONT lpcf = new CHOOSEFONT ();
	lpcf.lStructSize = CHOOSEFONT.sizeof;
	lpcf.hwndOwner = hwndOwner;
	lpcf.Flags = OS.CF_SCREENFONTS | OS.CF_EFFECTS;
	int lpLogFont = OS.HeapAlloc (hHeap, OS.HEAP_ZERO_MEMORY, LOGFONT.sizeof);
	if (fontData != null && fontData.data != null) {
		LOGFONT logFont = fontData.data;
		int lfHeight = logFont.lfHeight;
		int hDC = OS.GetDC (0);
		int pixels = -Compatibility.round (fontData.height * OS.GetDeviceCaps(hDC, OS.LOGPIXELSY), 72);
		OS.ReleaseDC (0, hDC);
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
	Shell oldModal = null;
	Display display = null;
	if ((style & (SWT.APPLICATION_MODAL | SWT.SYSTEM_MODAL)) != 0) {
		display = parent.getDisplay ();
		oldModal = display.getModalDialogShell ();
		display.setModalDialogShell (parent);
	}

	/* Open the dialog */
	boolean success = OS.ChooseFont (lpcf);
	
	/* Clear the temporary dialog modal parent */
	if ((style & (SWT.APPLICATION_MODAL | SWT.SYSTEM_MODAL)) != 0) {
		display.setModalDialogShell (oldModal);
	}
	
	/* Compute the result */
	if (success) {
		LOGFONT logFont = OS.IsUnicode ? (LOGFONT) new LOGFONTW () : new LOGFONTA ();
		OS.MoveMemory (logFont, lpLogFont, LOGFONT.sizeof);

		/*
		 * This will not work on multiple screens or
		 * for printing. Should use DC for the proper device.
		 */
		int hDC = OS.GetDC(0);
		int logPixelsY = OS.GetDeviceCaps(hDC, OS.LOGPIXELSY);
		int pixels = 0; 
		if (logFont.lfHeight > 0) {
			/*
			 * Feature in Windows. If the lfHeight of the LOGFONT structure
			 * is positive, the lfHeight measures the height of the entire
			 * cell, including internal leading, in logical units. Since the
			 * height of a font in points does not include the internal leading,
			 * we must subtract the internal leading, which requires a TEXTMETRIC,
			 * which in turn requires font creation.
			 */
			int hFont = OS.CreateFontIndirect(logFont);
			int oldFont = OS.SelectObject(hDC, hFont);
			TEXTMETRIC lptm = OS.IsUnicode ? (TEXTMETRIC) new TEXTMETRICW () : new TEXTMETRICA ();
			OS.GetTextMetrics(hDC, lptm);
			OS.SelectObject(hDC, oldFont);
			OS.DeleteObject(hFont);
			pixels = logFont.lfHeight - lptm.tmInternalLeading;
		} else {
			pixels = -logFont.lfHeight;
		}
		OS.ReleaseDC(0, hDC);

		int points = Compatibility.round(pixels * 72, logPixelsY);
		fontData = FontData.win32_new (logFont, points);
		int red = lpcf.rgbColors & 0xFF;
		int green = (lpcf.rgbColors >> 8) & 0xFF;
		int blue = (lpcf.rgbColors >> 16) & 0xFF;
		rgb = new RGB (red, green, blue);
	}
		
	/* Free the OS memory */
	if (lpLogFont != 0) OS.HeapFree (hHeap, 0, lpLogFont);

	/*
	* This code is intentionally commented.  On some
	* platforms, the owner window is repainted right
	* away when a dialog window exits.  This behavior
	* is currently unspecified.
	*/
//	if (hwndOwner != 0) OS.UpdateWindow (hwndOwner);
	
	if (!success) return null;
	return fontData;
}

/**
 * Sets a FontData object describing the font to be
 * selected by default in the dialog, or null to let
 * the platform choose one.
 * 
 * @param fontData the FontData to use initially, or null
 * @deprecated use #setFontList (FontData [])
 */
public void setFontData (FontData fontData) {
	this.fontData = fontData;
}

/**
 * Sets a set of FontData objects describing the font to
 * be selected by default in the dialog, or null to let
 * the platform choose one.
 * 
 * @param fontData the set of FontData objects to use initially, or null
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
 * Sets the receiver's selected color to be the argument.
 *
 * @param rgb the new RGB value for the selected color, may be
 *        null to let the platform to select a default when
 *        open() is called
 *
 * @see PaletteData#getRGBs
 * 
 * @since 2.1
 */
public void setRGB (RGB rgb) {
	this.rgb = rgb;
}

}
