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
package org.eclipse.swt.widgets;

 
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.carbon.*;
import org.eclipse.swt.internal.Callback;
import org.eclipse.swt.internal.carbon.RGBColor;

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
	boolean open;

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

int fontProc (int nextHandler, int theEvent, int userData) {
	int kind = OS.GetEventKind (theEvent);
	switch (kind) {
		case OS.kEventFontPanelClosed:
			open = false;
			break;
		case OS.kEventFontSelection:
			if (fontData == null) fontData = new FontData();
			int [] fontID = new int [1];
			if (OS.GetEventParameter (theEvent, OS.kEventParamATSUFontID, OS.typeUInt32, null, 4, null, fontID) == OS.noErr) {
				int [] actualLength = new int [1];
				int platformCode = OS.kFontUnicodePlatform, encoding = OS.kCFStringEncodingUnicode;
				if (OS.ATSUFindFontName (fontID [0], OS.kFontFamilyName, platformCode, OS.kFontNoScriptCode, OS.kFontNoLanguageCode, 0, null, actualLength, null) != OS.noErr) {
					platformCode = OS.kFontNoPlatformCode;
					encoding = OS.kCFStringEncodingMacRoman;
					OS.ATSUFindFontName (fontID [0], OS.kFontFamilyName, platformCode, OS.kFontNoScriptCode, OS.kFontNoLanguageCode, 0, null, actualLength, null);
				}	
				byte[] buffer = new byte[actualLength[0]];
				OS.ATSUFindFontName (fontID [0], OS.kFontFamilyName, platformCode, OS.kFontNoScriptCode, OS.kFontNoLanguageCode, buffer.length, buffer, actualLength, null);
				String name = "";
				int ptr = OS.CFStringCreateWithBytes (0, buffer, buffer.length, encoding, false);
				if (ptr != 0) {
					int length = OS.CFStringGetLength (ptr);
					if (length != 0) {
						char[] chars = new char [length];
						CFRange range = new CFRange ();
						range.length = length;
						OS.CFStringGetCharacters (ptr, range, chars);
						name = new String (chars);
					}
					OS.CFRelease (ptr);
				}
				fontData.setName (name);
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
		int[] font = new int[1];
		byte[] buffer = familyName.getBytes();
		if (OS.ATSUFindFontFromName(buffer, buffer.length, OS.kFontFamilyName, OS.kFontNoPlatformCode, OS.kFontNoScriptCode, OS.kFontNoLanguageCode, font) == OS.noErr) {
			short[] family = new short[1];
			OS.FMGetFontFamilyInstanceFromFont(font[0], family, new short[1]);		
			qdStyle.instance_fontFamily = family[0];
		}
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
	int fontPanelCallbackAddress = fontPanelCallback.getAddress ();
	if (fontPanelCallbackAddress == 0) SWT.error (SWT.ERROR_NO_MORE_CALLBACKS);
	int appTarget = OS.GetApplicationEventTarget ();
	int [] outRef = new int [1];
	OS.InstallEventHandler (appTarget, fontPanelCallbackAddress, mask.length / 2, mask, 0, outRef);
	fontData = null;
	rgb = null;
	open = true;
	/*
	* Feature in the Macintosh.  The Fonts window is not modal and it cannot
	* be accessed through direct API.  The fix is to figure out the Fonts
	* window by checking all available windows and set its modality
	* explicitily.
	*/
	int count = 0;
	int window = OS.GetPreviousWindow (0);
	while (window != 0) {
		count++;
		window = OS.GetPreviousWindow (window);
	}
	int [] windows = new int [count];
	boolean[] visible = new boolean [count];
	count = 0;
	window = OS.GetPreviousWindow (0);
	while (window != 0) {
		windows [count] = window;
		visible [count] = OS.IsWindowVisible (window);
		count++;
		window = OS.GetPreviousWindow (window);
	}
	OS.FPShowHideFontPanel ();
	int fontsWindow = 0;
	window = OS.GetPreviousWindow (0);
	while (window != 0 && fontsWindow == 0) {
		if (OS.IsWindowVisible (window)) {
			boolean found = false;
			for (int i = 0; i < windows.length; i++) {
				if (windows [i] == window) {
					found = true;
					if (!visible [i]) {
						fontsWindow = window;
						break;						
					}
				}
			}
			if (!found) {
				fontsWindow = window;
				break;
			}
		}
		window = OS.GetPreviousWindow (window);
	}
	if (fontsWindow != 0) {
		int inModalKind = OS.kWindowModalityNone;
		if ((style & SWT.PRIMARY_MODAL) != 0) inModalKind = OS.kWindowModalityWindowModal;
		if ((style & SWT.APPLICATION_MODAL) != 0) inModalKind = OS.kWindowModalityAppModal;
		if ((style & SWT.SYSTEM_MODAL) != 0) inModalKind = OS.kWindowModalitySystemModal;
		if (inModalKind != OS.kWindowModalityNone) {
			int inUnavailableWindow = 0;
			if (parent != null) inUnavailableWindow = OS.GetControlOwner (parent.handle);
			OS.SetWindowModality (fontsWindow, inModalKind, inUnavailableWindow);
			OS.SelectWindow (fontsWindow);
		}
	}
	Display display = parent.display;
	while (!parent.isDisposed() && open) {
		if (!display.readAndDispatch ()) display.sleep ();
	}
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
