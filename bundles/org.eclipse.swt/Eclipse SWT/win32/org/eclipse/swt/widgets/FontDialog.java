package org.eclipse.swt.widgets;

/*
 * Licensed Materials - Property of IBM,
 * (c) Copyright IBM Corp. 1998, 2001  All Rights Reserved
 */

import org.eclipse.swt.internal.win32.*;
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

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
		lpcf.Flags |= OS.CF_INITTOLOGFONTSTRUCT;
		OS.MoveMemory (lpLogFont, fontData.data, LOGFONT.sizeof);
	}
	lpcf.lpLogFont = lpLogFont;
	fontData = null;
	if (OS.ChooseFont (lpcf)) {
		LOGFONT logFont = new LOGFONT ();
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
			TEXTMETRIC lptm = new TEXTMETRIC();
			OS.GetTextMetrics(hDC, lptm);
			OS.SelectObject(hDC, oldFont);
			OS.DeleteObject(hFont);
			pixels = logFont.lfHeight - lptm.tmInternalLeading;
		} else {
			pixels = -logFont.lfHeight;
		}
		OS.ReleaseDC(0, hDC);

		/* Avoid using Math.round() */
		int points = (int)((pixels * 72.0f / logPixelsY) + 0.5f);
		fontData = FontData.win32_new (logFont, points);
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
	
	return fontData;
}

public void setFontData (FontData fontData) {
	this.fontData = fontData;
}

}
