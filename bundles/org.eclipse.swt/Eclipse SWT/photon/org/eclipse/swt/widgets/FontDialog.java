package org.eclipse.swt.widgets;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.photon.*;
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

public /*final*/ class FontDialog extends Dialog {
	FontData fontData;	
public FontDialog (Shell parent) {
	this (parent, SWT.PRIMARY_MODAL);
}
public FontDialog (Shell parent, int style) {
	super (parent, style);
}
public FontData getFontData() {
	return fontData;
}
public FontData open () {
	int parentHandle = 0;
	if (parent != null) parentHandle = parent.shellHandle;
	byte [] title = null;
	if (this.title != null) title = Converter.wcsToMbcs (null, this.title, true);

	byte[] font = null;
	if (fontData != null) {
		if (fontData.stem != null) {
			font = fontData.stem;
		} else {
			byte[] description = Converter.wcsToMbcs(null, fontData.getName(), true);
			int osStyle = 0, style = fontData.getStyle();
			if ((style & SWT.BOLD) != 0) osStyle |= OS.PF_STYLE_BOLD;
			if ((style & SWT.ITALIC) != 0) osStyle |= OS.PF_STYLE_ITALIC;
			int size = fontData.getHeight();		
			font = OS.PfGenerateFontName(description, osStyle, size, new byte[OS.MAX_FONT_TAG]);
		}
		fontData = null;
	}
	
	int fontPtr = OS.PtFontSelection (parentHandle, null, title, font, -1, OS.PHFONT_ALL_FONTS | OS.PFFONT_DONT_SHOW_LEGACY, null);

	if (fontPtr != 0) {
		int length = OS.strlen(fontPtr);
		font = new byte[length];
		OS.memmove(font, fontPtr, length);
		fontData = FontData.photon_new(font);
	}
	return fontData;
}
public void setFontData (FontData fontData) {
	this.fontData = fontData;
}
}
