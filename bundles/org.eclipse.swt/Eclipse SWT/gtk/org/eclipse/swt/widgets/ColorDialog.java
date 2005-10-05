/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
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
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.gtk.*;
import org.eclipse.swt.graphics.*;

/**
 * Instances of this class allow the user to select a color
 * from a predefined set of available colors.
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
public class ColorDialog extends Dialog {
	RGB rgb;
/**
 * Constructs a new instance of this class given only its parent.
 *
 * @param parent a composite control which will be the parent of the new instance
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the parent</li>
 *    <li>ERROR_INVALID_SUBCLASS - if this class is not an allowed subclass</li>
 * </ul>
 *
 * @see SWT
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public ColorDialog (Shell parent) {
	this (parent, SWT.NONE);
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
 * @param parent a composite control which will be the parent of the new instance (cannot be null)
 * @param style the style of control to construct
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the parent</li>
 *    <li>ERROR_INVALID_SUBCLASS - if this class is not an allowed subclass</li>
 * </ul>
 *
 * @see SWT
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public ColorDialog (Shell parent, int style) {
	super (parent, style);
	checkSubclass ();
}

/**
 * Returns the currently selected color in the receiver.
 *
 * @return the RGB value for the selected color, may be null
 *
 * @see PaletteData#getRGBs
 */
public RGB getRGB () {
	return rgb;
}
/**
 * Makes the receiver visible and brings it to the front
 * of the display.
 *
 * @return the selected color, or null if the dialog was
 *         cancelled, no color was selected, or an error
 *         occurred
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public RGB open () {
	byte [] buffer = Converter.wcsToMbcs (null, title, true);
	int /*long*/ handle = OS.gtk_color_selection_dialog_new (buffer);
	if (parent != null) {
		int /*long*/ shellHandle = parent.topHandle ();
		OS.gtk_window_set_transient_for (handle, shellHandle);
		int /*long*/ pixbufs = OS.gtk_window_get_icon_list (shellHandle);
		if (pixbufs != 0) {
			OS.gtk_window_set_icon_list (handle, pixbufs);
			OS.g_list_free (pixbufs);
		}
	}
	GtkColorSelectionDialog dialog = new GtkColorSelectionDialog ();
	OS.memmove (dialog, handle);
	GdkColor color = new GdkColor();
	if (rgb != null) {
		color.red = (short)((rgb.red & 0xFF) | ((rgb.red & 0xFF) << 8));
		color.green = (short)((rgb.green & 0xFF) | ((rgb.green & 0xFF) << 8));
		color.blue = (short)((rgb.blue & 0xFF) | ((rgb.blue & 0xFF) << 8));
		OS.gtk_color_selection_set_current_color (dialog.colorsel, color);
	}
	OS.gtk_color_selection_set_has_palette (dialog.colorsel, true);
	int response = OS.gtk_dialog_run (handle);
	boolean success = response == OS.GTK_RESPONSE_OK; 
	if (success) {
		OS.gtk_color_selection_get_current_color (dialog.colorsel, color);
		int red = (color.red >> 8) & 0xFF;
		int green = (color.green >> 8) & 0xFF;
		int blue = (color.blue >> 8) & 0xFF;
		rgb = new RGB (red, green, blue);
	}
	OS.gtk_widget_destroy (handle);
	if (!success) return null;
	return rgb;
}
/**
 * Sets the receiver's selected color to be the argument.
 *
 * @param rgb the new RGB value for the selected color, may be
 *        null to let the platform select a default when
 *        open() is called
 * @see PaletteData#getRGBs
 */
public void setRGB (RGB rgb) {
	this.rgb = rgb;
}
}
