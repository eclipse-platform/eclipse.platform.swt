package org.eclipse.swt.widgets;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */
import org.eclipse.swt.internal.carbon.OS;
import org.eclipse.swt.internal.carbon.Rect;
import org.eclipse.swt.internal.carbon.MacUtil;
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;

/**
 * Instances of the receiver represent is an unselectable
 * user interface object that is used to display progress,
 * typically in the form of a bar.
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>SMOOTH, HORIZONTAL, VERTICAL</dd>
 * <dt><b>Events:</b></dt>
 * <dd>(none)</dd>
 * </dl>
 * <p>
 * Note: Only one of the styles HORIZONTAL and VERTICAL may be specified.
 * </p><p>
 * IMPORTANT: This class is intended to be subclassed <em>only</em>
 * within the SWT implementation.
 * </p>
 */
public /*final*/ class ProgressBar extends Control {
	
	// AW
	private static final int SIZE= 16;
	private int fTopMargin;
	private int fBottomMargin;
	// AW

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
 * @see SWT#SMOOTH
 * @see SWT#HORIZONTAL
 * @see SWT#VERTICAL
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public ProgressBar (Composite parent, int style) {
	super (parent, checkStyle (style));
}
static int checkStyle (int style) {
	return checkBits (style, SWT.HORIZONTAL, SWT.VERTICAL, 0, 0, 0, 0);
}
public Point computeSize (int wHint, int hHint, boolean changed) {
	checkWidget();
	int border = getBorderWidth ();
	int width = border * 2, height = border * 2;
	if ((style & SWT.HORIZONTAL) != 0) {
		width += SIZE * 10;
		height += SIZE;
	} else {
		width += SIZE;
		height += SIZE * 10;
	}
	if (wHint != SWT.DEFAULT) width = wHint + (border * 2);
	if (hHint != SWT.DEFAULT) height = hHint + (border * 2);
	return new Point (width, height);
}
void createHandle (int index) {
	state |= HANDLE;
	int parentHandle = parent.handle;
	handle = MacUtil.newControl(parentHandle, (short)0, (short)0, (short)100, OS.kControlProgressBarProc);
	if (handle == 0) error (SWT.ERROR_NO_HANDLES);
	if ((style & SWT.INDETERMINATE) != 0)
		OS.SetControlData(handle, (short)0, OS.kControlProgressBarIndeterminateTag, 4, new int[]{-1});
}
/* AW
void disableButtonPress () {
	int xWindow = OS.XtWindow (handle);
	if (xWindow == 0) return;
	int xDisplay = OS.XtDisplay (handle);
	if (xDisplay == 0) return;
	int event_mask = OS.XtBuildEventMask (handle);
	XSetWindowAttributes attributes = new XSetWindowAttributes ();
	attributes.event_mask = event_mask & ~OS.ButtonPressMask;
	OS.XChangeWindowAttributes (xDisplay, xWindow, OS.CWEventMask, attributes);
}
void disableTraversal () {
	int [] argList = {OS.XmNtraversalOn, 0};
	OS.XtSetValues (handle, argList, argList.length / 2);
}
*/
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
	checkWidget();
    return OS.GetControl32BitMaximum(handle);
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
	checkWidget();
    return OS.GetControl32BitMinimum(handle);
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
	checkWidget();
    return OS.GetControl32BitValue(handle);
}
/* AW
void propagateWidget (boolean enabled) {
	super.propagateWidget (enabled);
	/*
	* ProgressBars never participate in focus traversal when
	* either enabled or disabled.
	*/
	/* AW
	if (enabled) {
		disableTraversal ();
		disableButtonPress ();
	}
}
void realizeChildren () {
	super.realizeChildren ();
	disableButtonPress ();
}
*/
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
	checkWidget();
	if (value < 0) return;
    OS.SetControl32BitMaximum(handle, value);
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
	checkWidget();
	if (value < 0) return;
    OS.SetControl32BitMinimum(handle, value);
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
	checkWidget();
	if (value < 0) return;
    OS.SetControl32BitValue(handle, value);
}

////////////////////////////////////////////////////////
// Mac stuff
////////////////////////////////////////////////////////

/**
 * Overridden from Control since we want to center the bar within its area. 
 */
void handleResize(int hndl, Rect bounds) {	
	if ((style & SWT.HORIZONTAL) != 0) { 	// horizontal
		int diff= bounds.bottom-bounds.top-SIZE;
		fTopMargin= diff/2;
		fBottomMargin= diff-fTopMargin;
		bounds.top+= fTopMargin;
		bounds.bottom-= fBottomMargin;
	} else {	// vertical
		int diff= bounds.right-bounds.left-SIZE;
		fTopMargin= diff/2;
		fBottomMargin= diff-fTopMargin;
		bounds.left+= fTopMargin;
		bounds.right-= fBottomMargin;
	}
	super.handleResize(hndl, bounds);
}

void internalGetControlBounds(int hndl, Rect bounds) {
	super.internalGetControlBounds(hndl, bounds);
	if ((style & SWT.HORIZONTAL) != 0) { 	// horizontal
		bounds.top+= -fTopMargin;
		bounds.bottom-= -fBottomMargin;
	} else {	// vertical
		bounds.left+= -fTopMargin;
		bounds.right-= -fBottomMargin;
	}
}

}
