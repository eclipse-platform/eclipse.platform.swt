/*******************************************************************************
 * Copyright (c) 2000, 2019 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.widgets;


import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.gtk.*;
import org.eclipse.swt.internal.gtk3.*;
import org.eclipse.swt.internal.gtk4.*;

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
	byte[] buffer = Converter.javaStringToCString(title);
	long handle = GTK.gtk_color_chooser_dialog_new(buffer, parent.topHandle());
	Display display = parent != null ? parent.getDisplay(): Display.getCurrent();

	GdkRGBA rgba = new GdkRGBA();
	if (rgb != null) {
		rgba.red = (double) rgb.red / 255;
		rgba.green = (double) rgb.green / 255;
		rgba.blue = (double) rgb.blue / 255;
		rgba.alpha = 1;
	}
	GTK.gtk_color_chooser_set_rgba (handle, rgba);
	if (rgbs != null) {
		int colorsPerRow = 9;
		long gdkRGBAS = OS.g_malloc(GdkRGBA.sizeof * rgbs.length);
		rgba = new GdkRGBA ();
		for (int i=0; i<rgbs.length; i++) {
			RGB rgbS = rgbs[i];
			if (rgbS != null) {
				rgba.red = (double) rgbS.red / 255;
				rgba.green = (double) rgbS.green / 255;
				rgba.blue = (double) rgbS.blue / 255;
				OS.memmove (gdkRGBAS + i * GdkRGBA.sizeof, rgba, GdkRGBA.sizeof);
			}
		}
		GTK.gtk_color_chooser_add_palette(handle, GTK.GTK_ORIENTATION_HORIZONTAL, colorsPerRow,
				rgbs.length, gdkRGBAS);
		GTK.gtk_color_chooser_set_rgba (handle, rgba);


		if (GTK.gtk_color_chooser_get_use_alpha(handle)) {
			GTK.gtk_color_chooser_set_use_alpha (handle, false);
		}
		OS.g_free (gdkRGBAS);
	}

	display.addIdleProc();
	Dialog oldModal = null;
	if (GTK.gtk_window_get_modal(handle)) {
		oldModal = display.getModalDialog();
		display.setModalDialog(this);
	}
	int signalId = 0;
	long hookId = 0;
	if ((style & SWT.RIGHT_TO_LEFT) != 0) {
		signalId = OS.g_signal_lookup (OS.map, GTK.GTK_TYPE_WIDGET());
		hookId = OS.g_signal_add_emission_hook (signalId, 0, display.emissionProc, handle, 0);
	}

	int response;
	if (GTK.GTK4) {
		response = SyncDialogUtil.run(display, handle, false);
	} else {
		display.externalEventLoop = true;
		display.sendPreExternalEventDispatchEvent();
		response = GTK3.gtk_dialog_run(handle);
		display.externalEventLoop = false;
		display.sendPostExternalEventDispatchEvent();
	}

	if ((style & SWT.RIGHT_TO_LEFT) != 0) {
		OS.g_signal_remove_emission_hook(signalId, hookId);
	}
	if (GTK.gtk_window_get_modal (handle)) {
		display.setModalDialog(oldModal);
	}
	boolean success = response == GTK.GTK_RESPONSE_OK;
	if (success) {
		int red = 0;
		int green = 0;
		int blue = 0;
		rgba = new GdkRGBA();
		GTK.gtk_color_chooser_get_rgba(handle, rgba);
		red =  (int) (rgba.red * 255);
		green = (int) (rgba.green * 255);
		blue =  (int) (rgba.blue *  255);
		rgb = new RGB(red, green, blue);
	} else {
		rgb = null;
	}

	display.removeIdleProc();
	if (GTK.GTK4) {
		GTK4.gtk_window_destroy(handle);
	} else {
		GTK3.gtk_widget_destroy(handle);
	}

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

