package org.eclipse.swt.widgets;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import org.eclipse.swt.*;
import org.eclipse.swt.internal.photon.*;
import org.eclipse.swt.graphics.*;

public class Canvas extends Composite {
	Caret caret;
	
Canvas () {
	/* Do nothing */
}

public Canvas (Composite parent, int style) {
	super (parent, style);
}

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
	boolean isVisible = caret != null && caret.isVisible ();
	if (isVisible) caret.hideCaret ();
	super.redraw ();
	if (isVisible) caret.showCaret ();
}

public void redraw (int x, int y, int width, int height, boolean all) {
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

public void setCaret (Caret caret) {
	checkWidget();
	Caret newCaret = caret;
	Caret oldCaret = this.caret;
	this.caret = newCaret;
	if (isFocusControl ()) {
		if (oldCaret != null) oldCaret.killFocus ();
		if (newCaret != null) newCaret.setFocus ();
	}
}

}
