package org.eclipse.swt.widgets;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import org.eclipse.swt.*;
import org.eclipse.swt.internal.photon.*;
import org.eclipse.swt.graphics.*;

/**
 * Instances of this class provide a surface for drawing
 * arbitrary graphics.
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>(none)</dd>
 * <dt><b>Events:</b></dt>
 * <dd>(none)</dd>
 * </dl>
 * <p>
 * This class may be subclassed by custom control implementors
 * who are building controls that are <em>not</em> constructed
 * from aggregates of other controls. That is, they are either
 * painted using SWT graphics calls or are handled by native
 * methods.
 * </p>
 *
 * @see Composite
 */
public class Canvas extends Composite {
	Caret caret;
	
Canvas () {
	/* Do nothing */
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
public Canvas (Composite parent, int style) {
	super (parent, style);
}

/**
 * Returns the caret.
 * <p>
 * The caret for the control is automatically hidden
 * and shown when the control is painted or resized,
 * when focus is gained or lost and when an the control
 * is scrolled.  To avoid drawing on top of the caret,
 * the programmer must hide and show the caret when
 * drawing in the window any other time.
 * </p>
 *
 * @return the caret
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Caret getCaret () {
	checkWidget();
	return caret;
}

void hookEvents () {
	super.hookEvents ();
	int windowProc = getDisplay ().windowProc;
	OS.PtAddEventHandler (handle, OS.Ph_EV_DRAG, windowProc, SWT.MouseMove);
}

int processFocusIn (int info) {
	int result = super.processFocusIn (info);
	if (caret != null) caret.setFocus ();
	return result;
}
int processFocusOut (int info) {
	int result = super.processFocusOut (info);
	if (caret != null) caret.killFocus ();
	return result;
}

int processPaint (int callData) {
	boolean isVisible = caret != null && caret.isVisible ();
	if (isVisible) caret.hideCaret ();
	int result = super.processPaint (callData);
	if (isVisible) caret.showCaret ();
	return result;
}

int processMouse (int info) {
	/*
	* Bug in Photon.  Despite the fact that we are returning
	* Pt_END when a button is pressed, for some reason, the
	* single-line text widget does not end the processing and 
	* the callback is sent to the parent.  This causes us to
	* call PhInitDrag () which interferes with drag selection
	* in the text widget.  The fix is to only call PhInitDrag ()
	* when there are no children.
	*/
	if (OS.PtWidgetChildFront (handle) != 0) {
		return super.processMouse (info);
	}
	
	if (info == 0) return OS.Pt_END;
	PtCallbackInfo_t cbinfo = new PtCallbackInfo_t ();
	OS.memmove (cbinfo, info, PtCallbackInfo_t.sizeof);
	if (cbinfo.event == 0) return OS.Pt_END;
	PhEvent_t ev = new PhEvent_t ();
	OS.memmove (ev, cbinfo.event, PhEvent_t.sizeof);
	switch (ev.type) {
		case OS.Ph_EV_BUT_PRESS:
			int data = OS.PhGetData (cbinfo.event);
			if (data == 0) return OS.Pt_END;
			PhPointerEvent_t pe = new PhPointerEvent_t ();
			OS.memmove (pe, data, PhPointerEvent_t.sizeof);
			PhRect_t rect = new PhRect_t ();
			PhPoint_t pos = new PhPoint_t();
			pos.x = pe.pos_x;
			pos.y = pe.pos_y;
			rect.ul_x = rect.lr_x = (short) (pos.x + ev.translation_x);
			rect.ul_y = rect.lr_y = (short) (pos.y + ev.translation_y);
			int rid = OS.PtWidgetRid (handle);
//			int input_group = OS.PhInputGroup (cbinfo.event);
			int input_group = OS.PhInputGroup (0);
			OS.PhInitDrag (rid, OS.Ph_DRAG_KEY_MOTION | OS.Ph_DRAG_TRACK | OS.Ph_TRACK_DRAG, rect, null, input_group, null, null, null, pos, null);
	}
	return super.processMouse (info);
}

public void redraw () {
	checkWidget();
	boolean isVisible = caret != null && caret.isVisible ();
	if (isVisible) caret.hideCaret ();
	super.redraw ();
	if (isVisible) caret.showCaret ();
}

public void redraw (int x, int y, int width, int height, boolean all) {
	checkWidget();
	boolean isVisible = caret != null && caret.isVisible ();
	if (isVisible) caret.hideCaret ();
	super.redraw (x, y, width, height, all);
	if (isVisible) caret.showCaret ();
}

void releaseWidget () {
	if (caret != null) {
		caret.releaseWidget ();
		caret.releaseHandle ();
	}
	caret = null;
	super.releaseWidget();
}

/**
 * Scrolls a rectangular area of the receiver by first copying 
 * the source area to the destination and then causing the area
 * of the source which is not covered by the destination to
 * be repainted. Children that intersect the rectangle are
 * optionally moved during the operation. In addition, outstanding
 * paint events are flushed before the source area is copied to
 * ensure that the contents of the canvas are drawn correctly.
 *
 * @param destX the x coordinate of the destination
 * @param destY the y coordinate of the destination
 * @param x the x coordinate of the source
 * @param y the y coordinate of the source
 * @param width the width of the area
 * @param height the height of the area
 * @param all <code>true</code>if children should be scrolled, and <code>false</code> otherwise
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void scroll (int destX, int destY, int x, int y, int width, int height, boolean all) {
	checkWidget();
	if (width <= 0 || height <= 0) return;
	int deltaX = destX - x, deltaY = destY - y;
	if (deltaX == 0 && deltaY == 0) return;
	if (!isVisible ()) return;
	boolean isVisible = (caret != null) && (caret.isVisible ());
	if (isVisible) caret.hideCaret ();
	GC gc = new GC (this);
	gc.copyArea (x, y, width, height, destX, destY);
	gc.dispose ();
	if (isVisible) caret.showCaret ();
}

void setBounds (int x, int y, int width, int height, boolean move, boolean resize) {
	checkWidget();
	boolean isVisible = (caret != null) && (caret.isVisible ());
	if (isVisible) caret.hideCaret ();
	super.setBounds (x, y, width, height, move, resize);
	if (isVisible) caret.showCaret ();
}

/**
 * Sets the receiver's caret.
 * <p>
 * The caret for the control is automatically hidden
 * and shown when the control is painted or resized,
 * when focus is gained or lost and when an the control
 * is scrolled.  To avoid drawing on top of the caret,
 * the programmer must hide and show the caret when
 * drawing in the window any other time.
 * </p>
 * @param caret the new caret for the receiver, may be null
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the caret has been disposed</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setCaret (Caret caret) {
	checkWidget();
	Caret newCaret = caret;
	Caret oldCaret = this.caret;
	this.caret = newCaret;
	if (isFocusControl ()) {
		if (oldCaret != null) oldCaret.killFocus ();
		if (newCaret != null) {
			if (newCaret.isDisposed()) error(SWT.ERROR_INVALID_ARGUMENT);
			newCaret.setFocus ();
		}
	}
}

}
