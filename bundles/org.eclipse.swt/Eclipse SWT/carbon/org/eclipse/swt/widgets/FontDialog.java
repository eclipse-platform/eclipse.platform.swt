package org.eclipse.swt.widgets;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */
 
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.carbon.OS;
import org.eclipse.swt.internal.carbon.FontSelectionQDStyle;
import org.eclipse.swt.internal.Callback;
import org.eclipse.swt.internal.carbon.RGBColor;

public class FontDialog extends Dialog {
	FontData fontData;
	RGB rgb;
	boolean open;

public FontDialog (Shell parent) {
	this (parent, SWT.APPLICATION_MODAL);
}

public FontDialog (Shell parent, int style) {
	super (parent, style);
	checkSubclass ();
}

public FontData getFontData () {
	return fontData;
}

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
	Display display = parent.getDisplay ();
	while (!parent.isDisposed() && open) {
		if (!display.readAndDispatch ()) display.sleep ();
	};
	OS.RemoveEventHandler (outRef [0]);
	fontPanelCallback.dispose ();
	return fontData;
}

public void setFontData (FontData fontData) {
	this.fontData = fontData;
}

public void setRGB (RGB rgb) {
	this.rgb = rgb;
}

}
