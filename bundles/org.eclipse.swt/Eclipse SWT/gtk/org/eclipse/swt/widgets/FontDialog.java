/*******************************************************************************
 * Copyright (c) 2000, 2024 IBM Corporation and others.
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
 * Instances of this class allow the user to select a font
 * from all available fonts in the system.
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
 * Returns <code>true</code> if the dialog's effects selection controls
 * are visible, and <code>false</code> otherwise.
 * <p>
 * If the platform's font dialog does not have any effects selection controls,
 * then this method always returns false.
 * </p>
 *
 * @return <code>true</code> if the dialog's effects selection controls
 * are visible and <code>false</code> otherwise
 *
 * @since 3.8
 */
public boolean getEffectsVisible () {
	// The GTK FontDialog does not have any effects selection controls.
	return false;
}

/**
 * Returns a FontData object describing the font that was
 * selected in the dialog, or null if none is available.
 *
 * @return the FontData for the selected font, or null
 * @deprecated use #getFontList ()
 */
@Deprecated
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
	byte[] titleBytes = Converter.javaStringToCString(title);
	Display display = parent != null ? parent.getDisplay(): Display.getCurrent();
	long handle;
	if (GTK.GTK4) {
		handle = GTK4.gtk_font_dialog_new();
	} else {
		handle = GTK3.gtk_font_chooser_dialog_new (titleBytes, 0);
	}
	if (handle == 0) error (SWT.ERROR_NO_HANDLES);

	if (GTK.GTK4) {
		GTK4.gtk_font_dialog_set_modal(handle, true);
		GTK4.gtk_font_dialog_set_title(handle, titleBytes);
	} else {
		if (parent != null) {
			long shellHandle = parent.topHandle();
			GTK.gtk_window_set_transient_for(handle, shellHandle);
		}

		long defaultWindowGroup = GTK.gtk_window_get_group(0);
		GTK.gtk_window_group_add_window(defaultWindowGroup, handle);
		GTK.gtk_window_set_modal(handle, true);
	}

	display.addIdleProc();
	Dialog oldModal = display.getModalDialog();
	display.setModalDialog(this);

	// Set font chooser dialog to current font
	if (fontData != null && !GTK.GTK4) {
		Font font = new Font(display, fontData);

		long fontName = OS.pango_font_description_to_string(font.handle);
		int length = C.strlen(fontName);
		byte[] buffer = new byte[length + 1];
		C.memmove(buffer, fontName, length);
		font.dispose();
		OS.g_free(fontName);

		GTK3.gtk_font_chooser_set_font(handle, buffer);
	}

	int signalId = 0;
	long hookId = 0;
	if ((style & SWT.RIGHT_TO_LEFT) != 0) {
		signalId = OS.g_signal_lookup(OS.map, GTK.GTK_TYPE_WIDGET());
		hookId = OS.g_signal_add_emission_hook(signalId, 0, display.emissionProc, handle, 0);
	}

	int response;
	long fontDesc = 0;
	if (GTK.GTK4) {
		long shellHandle = parent != null ? parent.topHandle() : 0;
		Font font = new Font(display, fontData);
		fontDesc = SyncDialogUtil.run(display, new AsyncReadyCallback() {
			@Override
			public void async(long callback) {
				// The font dialog ignores the given font and simply picks the first installed font
				// See https://gitlab.gnome.org/GNOME/gtk/-/issues/6892
				GTK4.gtk_font_dialog_choose_font(handle, shellHandle, font.handle, 0, callback, 0);
			}

			@Override
			public long await(long result) {
				return GTK4.gtk_font_dialog_choose_font_finish(handle, result, null);
			}

		});
		font.dispose();

		response = fontDesc != 0 ? GTK.GTK_RESPONSE_OK : GTK.GTK_RESPONSE_CANCEL;
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

	display.setModalDialog(oldModal);

	boolean success = response == GTK.GTK_RESPONSE_OK;
	if (success) {
		if (!GTK.GTK4) {
			long fontName = GTK3.gtk_font_chooser_get_font (handle);
			int length = C.strlen (fontName);
			byte [] buffer = new byte [length + 1];
			C.memmove (buffer, fontName, length);
			OS.g_free (fontName);
			fontDesc = OS.pango_font_description_from_string (buffer);
		}
		Font font = Font.gtk_new (display, fontDesc);
		fontData = font.getFontData () [0];
		OS.pango_font_description_free (fontDesc);
	} else {
		fontData = null;
	}

	display.removeIdleProc ();

	if (!GTK.GTK4) {
		GTK3.gtk_widget_destroy(handle);
	}

	return fontData;
}
/**
 * Sets the effects selection controls in the dialog visible if the
 * argument is <code>true</code>, and invisible otherwise.
 * <p>
 * By default the effects selection controls are displayed if the
 * platform font dialog supports effects selection.
 * </p>
 *
 * @param visible whether or not the dialog will show the effects selection controls
 *
 * @since 3.8
 */
public void setEffectsVisible(boolean visible) {
	// The GTK FontDialog does not have any effects selection controls.
}

/**
 * Sets a FontData object describing the font to be
 * selected by default in the dialog, or null to let
 * the platform choose one.
 *
 * @param fontData the FontData to use initially, or null
 * @deprecated use #setFontList (FontData [])
 */
@Deprecated
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
