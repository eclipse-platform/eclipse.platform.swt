/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.widgets;

 
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.carbon.OS;
import org.eclipse.swt.internal.carbon.FontSelectionQDStyle;
import org.eclipse.swt.internal.Callback;
import org.eclipse.swt.internal.carbon.RGBColor;

/**
 * Instances of this class allow the user to select a font
 * from all available fonts in the system.
 * <p>
 * IMPORTANT: This class is intended to be subclassed <em>only</em>
 * within the SWT implementation.
 * </p>
 */
public class FontDialog extends Dialog {
	FontData fontData;
	RGB rgb;
	boolean open;

/**
 * Constructs a new instance of this class given only its
 * parent.
 * <p>
 * Note: Currently, null can be passed in for the parent.
 * This has the effect of creating the dialog on the currently active
 * display if there is one. If there is no current display, the 
 * dialog is created on a "default" display. <b>Passing in null as
 * the parent is not considered to be good coding style,
 * and may not be supported in a future release of SWT.</b>
 * </p>
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
 * Note: Currently, null can be passed in for the parent.
 * This has the effect of creating the dialog on the currently active
 * display if there is one. If there is no current display, the 
 * dialog is created on a "default" display. <b>Passing in null as
 * the parent is not considered to be good coding style,
 * and may not be supported in a future release of SWT.</b>
 * </p>
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
public FontDialog (Shell parent, int style) {
	super (parent, style);
	checkSubclass ();
}

/**
 * Returns a FontData object describing the font that was
 * selected in the dialog, or null if none is available.
 * 
 * @return the FontData for the selected font, or null
 * @deprecated use #getFontSet ()
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
public FontData [] getFontSet () {
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

int fontProc (int nextHandler, int theEvent, int userData) {
	int kind = OS.GetEventKind (theEvent);
	switch (kind) {
		case OS.kEventFontPanelClosed:
			open = false;
			break;
		case OS.kEventFontSelection:
			if (fontData == null) fontData = new FontData();
			short [] fontFamily = new short [1];
			if (OS.GetEventParameter (theEvent, OS.kEventParamFMFontFamily, OS.typeSInt16, null, 2, null, fontFamily) == OS.noErr) {
				byte[] buffer = new byte[256];
				OS.FMGetFontFamilyName(fontFamily [0], buffer);
				int length = buffer[0] & 0xFF;
				char[] chars = new char[length];
				for (int i=0; i<length; i++) {
					chars[i]= (char)buffer[i+1];
				}
				fontData.setName (new String(chars));
			}
			short [] fontStyle = new short [1];
			if (OS.GetEventParameter (theEvent, OS.kEventParamFMFontStyle, OS.typeSInt16, null, 2, null, fontStyle) == OS.noErr) {
				int style = SWT.NORMAL;
				if ((fontStyle [0] & OS.bold) != 0) style |= SWT.BOLD;
				if ((fontStyle [0] & OS.italic) != 0) style |= SWT.ITALIC;
				fontData.setStyle (style);
			}
			short [] fontSize = new short [1];
			if (OS.GetEventParameter (theEvent, OS.kEventParamFMFontSize, OS.typeSInt16, null, 2, null, fontSize) == OS.noErr) {
				fontData.setHeight (fontSize [0]);
			}
			// NEEDS WORK - color not supported in native dialog for Carbon
			RGBColor color = new RGBColor ();
			int [] actualSize = new int [1];
			if (OS.GetEventParameter (theEvent, OS.kEventParamFontColor, OS.typeRGBColor, null, RGBColor.sizeof, actualSize, color) == OS.noErr) {
				int red = (color.red >> 8) & 0xFF;
				int green = (color.green >> 8) & 0xFF;
				int blue =	(color.blue >> 8) & 0xFF;
				rgb = new RGB(red, green, blue);
			}
			break;
	}
	return OS.noErr;
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
	FontSelectionQDStyle qdStyle = new FontSelectionQDStyle();
	qdStyle.version = OS.kFontSelectionQDStyleVersionZero;
	// NEEDS WORK - color not supported in native dialog for Carbon
	if (rgb != null) {
		qdStyle.hasColor = true;
		qdStyle.color_red = (short)(rgb.red * 257);
		qdStyle.color_green = (short)(rgb.green * 257);
		qdStyle.color_blue = (short)(rgb.blue * 257);
	}
	if (fontData != null) {
		String familyName = fontData.name;
		byte [] buffer = new byte [256];
		int length = familyName.length();
		if (length > 255) length = 255;
		buffer [0] = (byte)length;
		for (int i=0; i<length; i++) {
			buffer [i+1] = (byte) familyName.charAt(i);
		}
		int id = OS.FMGetFontFamilyFromName (buffer);
		if (id == OS.kInvalidFontFamily) id = OS.GetAppFont();
		qdStyle.instance_fontFamily = (short)id;
		int style = fontData.style;
		int fontStyle = OS.normal;
		if ((style & SWT.BOLD) != 0) fontStyle |= OS.bold;
		if ((style & SWT.ITALIC) != 0) fontStyle |= OS.italic;
		qdStyle.instance_fontStyle = (short)fontStyle;
		qdStyle.size = (short)fontData.height;
	}
	int ptr = OS.NewPtr(FontSelectionQDStyle.sizeof);
	OS.memcpy (ptr, qdStyle, FontSelectionQDStyle.sizeof);
	OS.SetFontInfoForSelection(OS.kFontSelectionQDType, 1, ptr, 0);
	OS.DisposePtr (ptr);
	int[] mask = new int[] {
		OS.kEventClassFont, OS.kEventFontSelection,
		OS.kEventClassFont, OS.kEventFontPanelClosed,
	};
	Callback fontPanelCallback = new Callback (this, "fontProc", 3);
	int appTarget = OS.GetApplicationEventTarget ();
	int [] outRef = new int [1];
	OS.InstallEventHandler (appTarget, fontPanelCallback.getAddress(), mask.length / 2, mask, 0, outRef);
	fontData = null;
	rgb = null;
	open = true;
	OS.FPShowHideFontPanel ();	
	Display display = parent.display;
	while (!parent.isDisposed() && open) {
		if (!display.readAndDispatch ()) display.sleep ();
	};
	OS.RemoveEventHandler (outRef [0]);
	fontPanelCallback.dispose ();
	return fontData;
}

/**
 * Sets a FontData object describing the font to be
 * selected by default in the dialog, or null to let
 * the platform choose one.
 * 
 * @param fontData the FontData to use initially, or null
 * @deprecated use #setFontSet (FontData [])
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
public void setFontSet (FontData [] fontData) {
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
