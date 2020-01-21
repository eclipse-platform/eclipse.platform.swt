/*******************************************************************************
 * Copyright (c) 2000, 2012 IBM Corporation and others.
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
import org.eclipse.swt.internal.win32.*;

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
	static final int CUSTOM_COLOR_COUNT = 16; // from the MS spec for CHOOSECOLOR.lpCustColors
	RGB rgb;
	RGB [] rgbs;
	int [] colors = new int [CUSTOM_COLOR_COUNT];

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

long CCHookProc (long hdlg, long uiMsg, long lParam, long lpData) {
	switch ((int)uiMsg) {
		case OS.WM_INITDIALOG: {
			if (title != null && title.length () != 0) {
				TCHAR buffer = new TCHAR (0, title, true);
				OS.SetWindowText (hdlg, buffer);
			}
			break;
		}
	}
	return 0;
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

	/* Get the owner HWND for the dialog */
	long hwndOwner = parent.handle;
	long hwndParent = parent.handle;

	/*
	* Feature in Windows.  There is no API to set the orientation of a
	* color dialog.  It is always inherited from the parent.  The fix is
	* to create a hidden parent and set the orientation in the hidden
	* parent for the dialog to inherit.
	*/
	boolean enabled = false;
	int dialogOrientation = style & (SWT.LEFT_TO_RIGHT | SWT.RIGHT_TO_LEFT);
	int parentOrientation = parent.style & (SWT.LEFT_TO_RIGHT | SWT.RIGHT_TO_LEFT);
	if (dialogOrientation != parentOrientation) {
		int exStyle = OS.WS_EX_NOINHERITLAYOUT;
		if (dialogOrientation == SWT.RIGHT_TO_LEFT) exStyle |= OS.WS_EX_LAYOUTRTL;
		hwndOwner = OS.CreateWindowEx (
			exStyle,
			Shell.DialogClass,
			null,
			0,
			OS.CW_USEDEFAULT, 0, OS.CW_USEDEFAULT, 0,
			hwndParent,
			0,
			OS.GetModuleHandle (null),
			null);
		enabled = OS.IsWindowEnabled (hwndParent);
		if (enabled) OS.EnableWindow (hwndParent, false);
	}

	/* Create the CCHookProc */
	Callback callback = new Callback (this, "CCHookProc", 4); //$NON-NLS-1$
	long lpfnHook = callback.getAddress ();

	/* Allocate the Custom Colors and initialize to white */
	Display display = parent.display;
	if (display.lpCustColors == 0) {
		long hHeap = OS.GetProcessHeap ();
		display.lpCustColors = OS.HeapAlloc (hHeap, OS.HEAP_ZERO_MEMORY, CUSTOM_COLOR_COUNT * 4);
		for (int i=0; i < CUSTOM_COLOR_COUNT; i++) {
			colors[i] = 0x00FFFFFF;
		}
		OS.MoveMemory (display.lpCustColors, colors, CUSTOM_COLOR_COUNT * 4);
	}

	/* Set the Custom Colors (if any) into the dialog */
	if (rgbs != null) {
		int length = rgbs.length > CUSTOM_COLOR_COUNT ? CUSTOM_COLOR_COUNT : rgbs.length;
		for (int i=0; i<length; i++) {
			RGB rgb = rgbs [i];
			int red = rgb.red & 0xFF;
			int green = (rgb.green << 8) & 0xFF00;
			int blue = (rgb.blue << 16) & 0xFF0000;
			colors[i] = red | green | blue;
		}
		for (int i=length; i < CUSTOM_COLOR_COUNT; i++) {
			colors[i] = 0x00FFFFFF;
		}
		OS.MoveMemory (display.lpCustColors, colors, CUSTOM_COLOR_COUNT * 4);
	}

	/* Open the dialog */
	CHOOSECOLOR lpcc = new CHOOSECOLOR ();
	lpcc.lStructSize = CHOOSECOLOR.sizeof;
	lpcc.Flags = OS.CC_ANYCOLOR | OS.CC_ENABLEHOOK;
	lpcc.lpfnHook = lpfnHook;
	lpcc.hwndOwner = hwndOwner;
	lpcc.lpCustColors = display.lpCustColors;
	if (rgb != null) {
		lpcc.Flags |= OS.CC_RGBINIT;
		int red = rgb.red & 0xFF;
		int green = (rgb.green << 8) & 0xFF00;
		int blue = (rgb.blue << 16) & 0xFF0000;
		lpcc.rgbResult = red | green | blue;
	}

	/* Make the parent shell be temporary modal */
	Dialog oldModal = null;
	if ((style & (SWT.APPLICATION_MODAL | SWT.SYSTEM_MODAL)) != 0) {
		oldModal = display.getModalDialog ();
		display.setModalDialog (this);
	}

	display.externalEventLoop = true;
	display.sendPreExternalEventDispatchEvent ();
	/* Open the dialog */
	boolean success = OS.ChooseColor (lpcc);
	display.externalEventLoop = false;
	display.sendPostExternalEventDispatchEvent ();

	/* Clear the temporary dialog modal parent */
	if ((style & (SWT.APPLICATION_MODAL | SWT.SYSTEM_MODAL)) != 0) {
		display.setModalDialog (oldModal);
	}

	/* Get the Custom Colors (if the user defined any) from the dialog */
	boolean customColor = false;
	OS.MoveMemory (colors, display.lpCustColors, colors.length * 4);
	for (int color : colors) {
		if (color != 0x00FFFFFF) {
			customColor = true;
			break;
		}
	}
	if (customColor) {
		rgbs = new RGB [CUSTOM_COLOR_COUNT];
		for (int i=0; i<colors.length; i++) {
			int color = colors[i];
			int red = color & 0xFF;
			int green = (color >> 8) & 0xFF;
			int blue = (color >> 16) & 0xFF;
			rgbs[i] = new RGB (red, green, blue);
		}
	}

	if (success) {
		int red = lpcc.rgbResult & 0xFF;
		int green = (lpcc.rgbResult >> 8) & 0xFF;
		int blue = (lpcc.rgbResult >> 16) & 0xFF;
		rgb = new RGB (red, green, blue);
	}

	/* Free the CCHookProc */
	callback.dispose ();

	/* Destroy the BIDI orientation window */
	if (hwndParent != hwndOwner) {
		if (enabled) OS.EnableWindow (hwndParent, true);
		OS.SetActiveWindow (hwndParent);
		OS.DestroyWindow (hwndOwner);
	}

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
	if (rgbs != null) {
		for (RGB rgb : rgbs) {
			if (rgb == null) error (SWT.ERROR_INVALID_ARGUMENT);
		}
	}
	this.rgbs = rgbs;
}

}