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
package org.eclipse.swt.widgets;


import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.wpf.OS;

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
 * 
 * @see <a href="http://www.eclipse.org/swt/examples.php">SWT Example: ControlExample, Dialog tab</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 * @noextend This class is not intended to be subclassed by clients.
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
	int dialog = OS.gcnew_FontDialog ();
	OS.FontDialog_ShowColor  (dialog, true);
	if (fontData != null) {
		int fontFamily = parent.createDotNetString (fontData.fontFamily, false);
		int fontStyle = 0;
		int fontWeight = OS.FontWeight_ToOpenTypeWeight (OS.FontWeights_Bold);
		if (fontWeight == fontData.weight) fontStyle |= OS.FontStyle_Bold;
		if (fontData.style == OS.FontStyles_Italic) fontStyle |= OS.FontStyle_Italic;
		if (fontData.style == OS.FontStyles_Normal) fontStyle |= OS.FontStyle_Regular;
		//TODO strikethrough/underline
		int font = OS.gcnew_Font (fontFamily, fontData.height, fontStyle);
		OS.FontDialog_Font (dialog, font);
		OS.GCHandle_Free (font);
		OS.GCHandle_Free (fontFamily);
	}
	if (rgb != null) {
		int color = OS.DrawingColor_FromArgb (174, rgb.red, rgb.blue, rgb.green);
		OS.FontDialog_Color (dialog, color);
		OS.GCHandle_Free (color);
	}
	int result = OS.FormsCommonDialog_ShowDialog (dialog);
	boolean success = result == OS.DialogResult_OK; 
	if (success) {
		int font = OS.FontDialog_Font (dialog);
		int fontFamilyPtr = OS.Font_FontFamily (font);
		int fontFamilyName = OS.DrawingFontFamily_Name (fontFamilyPtr);
		String fontFamily = Widget.createJavaString (fontFamilyName);
		int fontStyle = OS.Font_Style (font);
		int fontDataStyle;
		if ((fontStyle & OS.FontStyle_Italic) != 0) {
			fontDataStyle = OS.FontStyles_Italic;
		} else {
			fontDataStyle = OS.FontStyles_Normal;
		}
		int weight; 
		if ((fontStyle & OS.FontStyle_Bold) != 0) {
			weight = OS.FontWeight_ToOpenTypeWeight (OS.FontWeights_Bold);
		} else {
			weight = OS.FontWeight_ToOpenTypeWeight (OS.FontWeights_Normal);
		}
		int height = OS.Font_Size (font);
		OS.GCHandle_Free(fontFamilyName);
		OS.GCHandle_Free(fontFamilyPtr);
		OS.GCHandle_Free(font);
		fontData = FontData.wpf_new (fontFamily, fontDataStyle, weight, 1, height);
				
		int color = OS.FontDialog_Color (dialog);
		int argb = OS.DrawingColor_ToArgb (color);	
		int red = (argb & 0xFF0000) >> 16;
		int green = (argb & 0xFF00) >> 8;
		int blue = argb & 0xFF;
		OS.GCHandle_Free (color);
		rgb = new RGB (red, green, blue);
	}
	OS.GCHandle_Free (dialog);
	return success ? fontData : null;
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
