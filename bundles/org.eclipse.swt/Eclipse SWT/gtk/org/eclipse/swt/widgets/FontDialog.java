package org.eclipse.swt.widgets;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import org.eclipse.swt.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.gtk.*;
import org.eclipse.swt.graphics.*;

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
 * for all SWT dialog classes should include a comment which
 * describes the style constants which are applicable to the class.
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
 */
public FontData getFontData() {
	return fontData;
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
	int handle;
	byte [] titleBytes;
	titleBytes = Converter.wcsToMbcs (null, title, true);
	handle = OS.gtk_font_selection_dialog_new (titleBytes);
	if (parent!=null) {
		OS.gtk_window_set_transient_for(handle, parent.topHandle());
	}
	if (fontData != null) {
		Display display = parent != null ? parent.getDisplay () : Display.getCurrent ();
		Font font = new Font (display, fontData);
		int fontName = OS.pango_font_description_to_string (font.handle);
		int length = OS.strlen (fontName);
		byte [] buffer = new byte [length + 1];
		OS.memmove (buffer, fontName, length);
		font.dispose();
		OS.g_free (fontName);
		OS.gtk_font_selection_dialog_set_font_name (handle, buffer);
	}
	fontData = null;
	int response = OS.gtk_dialog_run(handle);
	if (response == OS.GTK_RESPONSE_OK) {
		int fontName = OS.gtk_font_selection_dialog_get_font_name (handle);
		int length = OS.strlen (fontName);
		byte [] buffer = new byte [length + 1];
		OS.memmove (buffer, fontName, length);
		int fontDesc = OS.pango_font_description_from_string (buffer);
		Display display = parent != null ? parent.getDisplay () : Display.getCurrent ();
		Font font = Font.gtk_new (display, fontDesc);
		fontData = font.getFontData () [0];
		OS.pango_font_description_free (fontDesc);		
	}
	OS.gtk_widget_destroy(handle);
	return fontData;
}
/**
 * Sets a FontData object describing the font to be
 * selected by default in the dialog, or null to let
 * the platform choose one.
 * 
 * @param fontData the FontData to use initially, or null
 */
public void setFontData (FontData fontData) {
	this.fontData = fontData;
}
}
