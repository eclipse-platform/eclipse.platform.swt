package org.eclipse.swt.graphics;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */

import org.eclipse.swt.*;

/**
 * Instances of this class manage operating system resources that
 * specify the appearance of the on-screen pointer. To create a
 * cursor you specify the device and either a simple cursor style
 * describing one of the standard operating system provided cursors
 * or the image and mask data for the desired appearance.
 * <p>
 * Application code must explicitly invoke the <code>Cursor.dispose()</code> 
 * method to release the operating system resources managed by each instance
 * when those instances are no longer required.
 * </p>
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>
 *   CURSOR_ARROW, CURSOR_WAIT, CURSOR_CROSS, CURSOR_APPSTARTING, CURSOR_HELP,
 *   CURSOR_SIZEALL, CURSOR_SIZENESW, CURSOR_SIZENS, CURSOR_SIZENWSE, CURSOR_SIZEWE,
 *   CURSOR_SIZEN, CURSOR_SIZES, CURSOR_SIZEE, CURSOR_SIZEW, CURSOR_SIZENE, CURSOR_SIZESE,
 *   CURSOR_SIZESW, CURSOR_SIZENW, CURSOR_UPARROW, CURSOR_IBEAM, CURSOR_NO, CURSOR_HAND
 * </dd>
 * </dl>
 * <p>
 * Note: Only one of the above styles may be specified.
 * </p>
 */
public final class Cursor {
	/**
	 * the handle to the OS cursor resource
	 * (Warning: This field is platform dependent)
	 */
	public int handle;

	/**
	 * The device where this image was created.
	 */
	Device device;

Cursor () {
}
/**	 
 * Constructs a new cursor given a device and a style
 * constant describing the desired cursor appearance.
 * <p>
 * You must dispose the cursor when it is no longer required. 
 * </p>
 *
 * @param device the device on which to allocate the cursor
 * @param style the style of cursor to allocate
 * 
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if device is null and there is no current device</li>
 *    <li>ERROR_INVALID_ARGUMENT - when an unknown style is specified</li>
 * </ul>
 * @exception SWTError <ul>
 *    <li>ERROR_NO_HANDLES - if a handle could not be obtained for cursor creation</li>
 * </ul>
 *
 * @see SWT#CURSOR_ARROW
 * @see SWT#CURSOR_WAIT
 * @see SWT#CURSOR_CROSS
 * @see SWT#CURSOR_APPSTARTING
 * @see SWT#CURSOR_HELP
 * @see SWT#CURSOR_SIZEALL
 * @see SWT#CURSOR_SIZENESW
 * @see SWT#CURSOR_SIZENS
 * @see SWT#CURSOR_SIZENWSE
 * @see SWT#CURSOR_SIZEWE
 * @see SWT#CURSOR_SIZEN
 * @see SWT#CURSOR_SIZES
 * @see SWT#CURSOR_SIZEE
 * @see SWT#CURSOR_SIZEW
 * @see SWT#CURSOR_SIZENE
 * @see SWT#CURSOR_SIZESE
 * @see SWT#CURSOR_SIZESW
 * @see SWT#CURSOR_SIZENW
 * @see SWT#CURSOR_UPARROW
 * @see SWT#CURSOR_IBEAM
 * @see SWT#CURSOR_NO
 * @see SWT#CURSOR_HAND
 */
public Cursor (Device device, int style) {
	if (device == null) device = Device.getDevice();
	if (device == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	this.device = device;
       /* AW
	int shape = 0;
	switch (style) {
		case SWT.CURSOR_ARROW: shape = OS.XC_left_ptr; break;
		case SWT.CURSOR_WAIT: shape = OS.XC_watch; break;
		case SWT.CURSOR_HAND: shape = OS.XC_hand2; break;
		case SWT.CURSOR_CROSS: shape = OS.XC_cross; break;
		case SWT.CURSOR_APPSTARTING: shape = OS.XC_watch; break;
		case SWT.CURSOR_HELP: shape = OS.XC_question_arrow; break;
		case SWT.CURSOR_SIZEALL: shape = OS.XC_diamond_cross; break;
		case SWT.CURSOR_SIZENESW: shape = OS.XC_sizing; break;
		case SWT.CURSOR_SIZENS: shape = OS.XC_double_arrow; break;
		case SWT.CURSOR_SIZENWSE: shape = OS.XC_sizing; break;
		case SWT.CURSOR_SIZEWE: shape = OS.XC_sb_h_double_arrow; break;
		case SWT.CURSOR_SIZEN: shape = OS.XC_top_side; break;
		case SWT.CURSOR_SIZES: shape = OS.XC_bottom_side; break;
		case SWT.CURSOR_SIZEE: shape = OS.XC_right_side; break;
		case SWT.CURSOR_SIZEW: shape = OS.XC_left_side; break;
		case SWT.CURSOR_SIZENE: shape = OS.XC_top_right_corner; break;
		case SWT.CURSOR_SIZESE: shape = OS.XC_bottom_right_corner; break;
		case SWT.CURSOR_SIZESW: shape = OS.XC_bottom_left_corner; break;
		case SWT.CURSOR_SIZENW: shape = OS.XC_top_left_corner; break;
		case SWT.CURSOR_UPARROW: shape = OS.XC_sb_up_arrow; break;
		case SWT.CURSOR_IBEAM: shape = OS.XC_xterm; break;
		case SWT.CURSOR_NO: shape = OS.XC_X_cursor; break;
		default:
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	}
	this.handle = OS.XCreateFontCursor(device.xDisplay, shape);
        */
}
/**
 * Returns <code>true</code> if the cursor has been disposed,
 * and <code>false</code> otherwise.
 * <p>
 * This method gets the dispose state for the cursor.
 * When a cursor has been disposed, it is an error to
 * invoke any other method using the cursor.
 *
 * @return <code>true</code> when the cursor is disposed and <code>false</code> otherwise
 */
public boolean isDisposed() {
	return handle == 0;
}

/**
 * Disposes of the operating system resources associated with
 * the cursor. Applications must dispose of all cursors which
 * they allocate.
 */
    public void dispose()  {
    }


/**
 * Returns a string containing a concise, human-readable
 * description of the receiver.
 *
 * @return a string representation of the receiver
 */
public String toString () {
	if (isDisposed()) return "Cursor {*DISPOSED*}";
	return "Cursor {" + handle + "}";
}
}
