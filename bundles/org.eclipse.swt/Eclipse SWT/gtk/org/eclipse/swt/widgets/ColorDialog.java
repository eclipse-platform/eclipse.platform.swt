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
 * @param parent a composite control which will be the parent of the new instance (cannot be null)
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
	this (parent, SWT.NULL);
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
 * for all SWT widget classes should include a comment which
 * describes the style constants which are applicable to the class.
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
	int handle = OS.gtk_color_selection_dialog_new (buffer);
	if (parent!=null) {
		OS.gtk_window_set_transient_for(handle, parent.topHandle());
	}
	if (rgb != null) {
		double [] color = new double [4];
		color [0] = (double)rgb.red / 256;
		color [1] = (double)rgb.green / 256;
		color [2] = (double)rgb.blue / 256;
		OS.gtk_color_selection_set_color (OS.GTK_COLOR_SELECTION_DIALOG_COLORSEL(handle), color);
	}
	rgb = null;
	int response = OS.gtk_dialog_run(handle);
	if (response == OS.GTK_RESPONSE_OK) {
		double [] color = new double [4];
		OS.gtk_color_selection_get_color (OS.GTK_COLOR_SELECTION_DIALOG_COLORSEL(handle), color);
		rgb = new RGB ((int)(color [0] * 256), (int)(color [1] * 256), (int)(color [2] * 256));
	}
	OS.gtk_widget_destroy(handle);
	return rgb;
}
/**
 * Returns the receiver's selected color to be the argument.
 *
 * @param rgb the new RGB value for the selected color, may be
 *        null to let the platform to select a default when
 *        open() is called
 *
 * @see PaletteData#getRGBs
 */
public void setRGB (RGB rgb) {
	this.rgb = rgb;
}
}
