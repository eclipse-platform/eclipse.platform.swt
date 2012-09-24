/*******************************************************************************
 * Copyright (c) 2000, 2011 IBM Corporation and others.
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
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 * 
 * @see <a href="http://www.eclipse.org/swt/examples.php">SWT Example: ControlExample, Dialog tab</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 * @noextend This class is not intended to be subclassed by clients.
 */
public class ColorDialog extends Dialog {
	RGB rgb;
	RGB [] rgbs;
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
	super (parent, checkStyle (parent, style));
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
 * Returns an array of <code>RGB</code>s which are the list of
 * custom colors selected by the user in the receiver, or null
 * if no custom colors were selected.
 *
 * @return the array of RGBs, which may be null
 * 
 * @since 3.8
 */
public RGB[] getRGBs() {
	return rgbs;
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
	long /*int*/ handle = OS.gtk_color_selection_dialog_new (buffer);
	Display display = parent != null ? parent.getDisplay (): Display.getCurrent ();
	if (parent != null) {
		long /*int*/ shellHandle = parent.topHandle ();
		OS.gtk_window_set_transient_for (handle, shellHandle);
		long /*int*/ pixbufs = OS.gtk_window_get_icon_list (shellHandle);
		if (pixbufs != 0) {
			OS.gtk_window_set_icon_list (handle, pixbufs);
			OS.g_list_free (pixbufs);
		}
	}
	if (OS.GTK_VERSION >= OS.VERSION (2, 10, 0)) {
		long /*int*/ group = OS.gtk_window_get_group(0);
		OS.gtk_window_group_add_window (group, handle);
	}
	OS.gtk_window_set_modal (handle, true);
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
	if (rgbs != null) {
		long /*int*/ colors = OS.g_malloc(GdkColor.sizeof * rgbs.length);
		for (int i=0; i<rgbs.length; i++) {
			RGB rgb = rgbs[i];
			if (rgb != null) {
				color.red = (short)((rgb.red & 0xFF) | ((rgb.red & 0xFF) << 8));
				color.green = (short)((rgb.green & 0xFF) | ((rgb.green & 0xFF) << 8));
				color.blue = (short)((rgb.blue & 0xFF) | ((rgb.blue & 0xFF) << 8));
				OS.memmove (colors + i * GdkColor.sizeof, color, GdkColor.sizeof);
			}
		}
		long /*int*/ strPtr = OS.gtk_color_selection_palette_to_string(colors, rgbs.length);
		int length = OS.strlen (strPtr);
		buffer = new byte [length];
		OS.memmove (buffer, strPtr, length);
		String paletteString = new String (Converter.mbcsToWcs (null, buffer));
		buffer = Converter.wcsToMbcs (null, paletteString, true);
		OS.g_free (colors);
		long /*int*/ settings = OS.gtk_settings_get_default ();
		if (settings != 0) {
			OS.gtk_settings_set_string_property(settings, OS.gtk_color_palette, buffer, Converter.wcsToMbcs (null, "gtk_color_selection_palette_to_string", true));
		}
	}
	display.addIdleProc ();
	Dialog oldModal = null;
	if (OS.gtk_window_get_modal (handle)) {
		oldModal = display.getModalDialog ();
		display.setModalDialog (this);
	}
	int signalId = 0;
	long /*int*/ hookId = 0;
	if ((style & SWT.RIGHT_TO_LEFT) != 0) {
		signalId = OS.g_signal_lookup (OS.map, OS.GTK_TYPE_WIDGET());
		hookId = OS.g_signal_add_emission_hook (signalId, 0, display.emissionProc, handle, 0);
	}	
	int response = OS.gtk_dialog_run (handle);
	/*
	* This call to gdk_threads_leave() is a temporary work around
	* to avoid deadlocks when gdk_threads_init() is called by native
	* code outside of SWT (i.e AWT, etc). It ensures that the current
	* thread leaves the GTK lock acquired by the function above. 
	*/
	OS.gdk_threads_leave();
	if ((style & SWT.RIGHT_TO_LEFT) != 0) {
		OS.g_signal_remove_emission_hook (signalId, hookId);
	}
	if (OS.gtk_window_get_modal (handle)) {
		display.setModalDialog (oldModal);
	}
	boolean success = response == OS.GTK_RESPONSE_OK; 
	if (success) {
		OS.gtk_color_selection_get_current_color (dialog.colorsel, color);
		int red = (color.red >> 8) & 0xFF;
		int green = (color.green >> 8) & 0xFF;
		int blue = (color.blue >> 8) & 0xFF;
		rgb = new RGB (red, green, blue);
	}
	long /*int*/ settings = OS.gtk_settings_get_default ();
	if (settings != 0) {
		long /*int*/ [] ptr = new long /*int*/ [1];
		OS.g_object_get (settings, OS.gtk_color_palette, ptr, 0);
		if (ptr [0] != 0) {
			int length = OS.strlen (ptr [0]);
			buffer = new byte [length];
			OS.memmove (buffer, ptr [0], length);
			OS.g_free (ptr [0]);
			String [] gdkColorStrings = null;
			if (length > 0) {
				String gtk_color_palette = new String(Converter.mbcsToWcs (null, buffer));
				gdkColorStrings = splitString(gtk_color_palette, ':');
				length = gdkColorStrings.length;
			}
			rgbs = new RGB [length];
			for (int i=0; i<length; i++) {
				String colorString = gdkColorStrings[i];
				buffer = Converter.wcsToMbcs (null, colorString, true);
				OS.gdk_color_parse(buffer, color);
				int red = (color.red >> 8) & 0xFF;
				int green = (color.green >> 8) & 0xFF;
				int blue = (color.blue >> 8) & 0xFF;
				rgbs [i] = new RGB (red, green, blue);
			}
		}
	}
	display.removeIdleProc ();
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
/**
 * Sets the receiver's list of custom colors to be the given array
 * of <code>RGB</code>s, which may be null to let the platform select
 * a default when open() is called.
 *
 * @param rgbs the array of RGBs, which may be null
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if an RGB in the rgbs array is null</li>
 * </ul>
 * 
 * @since 3.8
 */
public void setRGBs(RGB[] rgbs) {
	this.rgbs = rgbs;
}
static String[] splitString(String text, char ch) {
    String[] substrings = new String[1];
    int start = 0, pos = 0;
    while (pos != -1) {
        pos = text.indexOf(ch, start);
        if (pos == -1) {
        	substrings[substrings.length - 1] = text.substring(start);
        } else {
            substrings[substrings.length - 1] = text.substring(start, pos);
            start = pos + 1;
            String[] newSubstrings = new String[substrings.length+1];
            System.arraycopy(substrings, 0, newSubstrings, 0, substrings.length);
       		substrings = newSubstrings;
        }
    }
    return substrings;
}
}
