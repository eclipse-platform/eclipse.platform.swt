package org.eclipse.swt.widgets;

/*
 * Licensed Materials - Property of IBM,
 * (c) Copyright IBM Corp. 1998, 2001  All Rights Reserved
 */

import org.eclipse.swt.internal.win32.*;
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;

/**
 * Instances of the receiver represent is an unselectable
 * user interface object that is used to display progress,
 * typlically in the form of a bar.
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>SMOOTH, HORIZONTAL, VERTICAL</dd>
 * <dt><b>Events:</b></dt>
 * <dd>(none)</dd>
 * </dl>
 * <p>
 * IMPORTANT: This class is intended to be subclassed <em>only</em>
 * within the SWT implementation.
 * </p>
 */
public class ProgressBar extends Control {
	static final int ProgressBarProc;
	static final byte [] ProgressBarClass = OS.PROGRESS_CLASS;
	static {
		WNDCLASSEX lpWndClass = new WNDCLASSEX ();
		lpWndClass.cbSize = WNDCLASSEX.sizeof;
		OS.GetClassInfoEx (0, ProgressBarClass, lpWndClass);
		ProgressBarProc = lpWndClass.lpfnWndProc;
	}

public ProgressBar (Composite parent, int style) {
	super (parent, checkStyle (style));
}

int callWindowProc (int msg, int wParam, int lParam) {
	if (handle == 0) return 0;
	return OS.CallWindowProc (ProgressBarProc, handle, msg, wParam, lParam);
}

static int checkStyle (int style) {
	return checkBits (style, SWT.HORIZONTAL, SWT.VERTICAL, 0, 0, 0, 0);
}

public Point computeSize (int wHint, int hHint, boolean changed) {
	checkWidget ();
	int border = getBorderWidth ();
	int width = border * 2, height = border * 2;
	if ((style & SWT.HORIZONTAL) != 0) {
		width += OS.GetSystemMetrics (OS.SM_CXHSCROLL) * 10;
		height += OS.GetSystemMetrics (OS.SM_CYHSCROLL);
	} else {
		width += OS.GetSystemMetrics (OS.SM_CXVSCROLL);
		height += OS.GetSystemMetrics (OS.SM_CYVSCROLL) * 10;
	}
	if (wHint != SWT.DEFAULT) width = wHint + (border * 2);
	if (hHint != SWT.DEFAULT) height = hHint + (border * 2);
	return new Point (width, height);
}

void createHandle () {
	super.createHandle ();
	OS.SendMessage (handle, OS.TBM_SETRANGEMAX, 0, 100);
	OS.SendMessage (handle, OS.TBM_SETPAGESIZE, 0, 10);
	OS.SendMessage (handle, OS.TBM_SETTICFREQ, 10, 0);
}

int defaultForeground () {
	return OS.GetSysColor (OS.COLOR_HIGHLIGHT);
}

/**
 * Returns the maximum value which the receiver will allow.
 *
 * @return the maximum
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getMaximum () {
	checkWidget ();
	return OS.SendMessage (handle, OS.PBM_GETRANGE, 0, 0);
}

/**
 * Returns the minimum value which the receiver will allow.
 *
 * @return the minimum
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getMinimum () {
	checkWidget ();
	return OS.SendMessage (handle, OS.PBM_GETRANGE, 1, 0);
}

/**
 * Returns the single <em>selection</em> that is the receiver's position.
 *
 * @return the selection
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getSelection () {
	checkWidget ();
	return OS.SendMessage (handle, OS.PBM_GETPOS, 0, 0);
}

void setBackgroundPixel (int pixel) {
	if (background == pixel) return;
	background = pixel;
	/*
	* Feature in Windows.  Setting the color to be
	* the current default is not correct because the
	* widget will not change colors when the colors
	* are changed from the control panel.  There is
	* no fix at this time.
	*/
	if (pixel == -1) pixel = defaultBackground ();
	OS.SendMessage (handle, OS.PBM_SETBKCOLOR, 0, pixel);
}

void setForegroundPixel (int pixel) {
	if (foreground == pixel) return;
	foreground = pixel;
	/*
	* Feature in Windows.  Setting the color to be
	* the current default is not correct because the
	* widget will not change colors when the colors
	* are changed from the control panel.  There is
	* no fix at this time.
	*/
	if (pixel == -1) pixel = defaultForeground ();
	OS.SendMessage (handle, OS.PBM_SETBARCOLOR, 0, pixel);
}

/**
 * Sets the maximum value which the receiver will allow
 * to be the argument which must be greater than or
 * equal to zero.
 *
 * @param value the new maximum (must be zero or greater)
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setMaximum (int value) {
	checkWidget ();
	int minimum = OS.SendMessage (handle, OS.PBM_GETRANGE, 1, 0);
	if (0 <= minimum && minimum < value) {
		OS.SendMessage (handle, OS.PBM_SETRANGE32, minimum, value);
	}
}

/**
 * Sets the minimum value which the receiver will allow
 * to be the argument which must be greater than or
 * equal to zero.
 *
 * @param value the new minimum (must be zero or greater)
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setMinimum (int value) {
	checkWidget ();
	int maximum = OS.SendMessage (handle, OS.PBM_GETRANGE, 0, 0);
	if (0 <= value && value < maximum) {
		OS.SendMessage (handle, OS.PBM_SETRANGE32, value, maximum);
	}
}

/**
 * Sets the single <em>selection</em> that is the receiver's
 * position to the argument which must be greater than or equal
 * to zero.
 *
 * @param value the new selection (must be zero or greater)
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setSelection (int value) {
	checkWidget ();
	if (value < 0) return;
	OS.SendMessage (handle, OS.PBM_SETPOS, value, 0);
}

int widgetStyle () {
	int bits = super.widgetStyle ();
	if ((style & SWT.SMOOTH) != 0) bits |= OS.PBS_SMOOTH;
	if ((style & SWT.VERTICAL) != 0) bits |= OS.PBS_VERTICAL;
	return bits;
}

byte [] windowClass () {
	return ProgressBarClass;
}

int windowProc () {
	return ProgressBarProc;
}

}
