package org.eclipse.swt.widgets;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */

import org.eclipse.swt.internal.carbon.*;

import org.eclipse.swt.*;
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

int kEventControlDraw (int nextHandler, int theEvent, int userData) {
	boolean isFocus = caret != null && caret.isFocusCaret ();
	if (isFocus) caret.killFocus ();
	int result = super.kEventControlDraw (nextHandler, theEvent, userData);
	if (isFocus) caret.setFocus ();
	return result;
}

int kEventControlSetFocusPart (int nextHandler, int theEvent, int userData) {
	int result = super.kEventControlSetFocusPart (nextHandler, theEvent, userData);
	if (caret != null) {
		short [] part = new short [1];
		OS.GetEventParameter (theEvent, OS.kEventParamControlPart, OS.typeControlPartCode, null, 2, null, part);
		if (part [0] != 0) {
			caret.setFocus ();
		} else {
			caret.killFocus ();
		}
	}
	return result;
}

void releaseWidget () {
	if (caret != null) caret.releaseResources ();
	caret = null;
	super.releaseWidget ();
}

public void scroll (int destX, int destY, int x, int y, int width, int height, boolean all) {
	checkWidget();
	if (width <= 0 || height <= 0) return;
	int deltaX = destX - x, deltaY = destY - y;
	if (deltaX == 0 && deltaY == 0) return;
	if (!isVisible ()) return;
	boolean isFocus = caret != null && caret.isFocusCaret ();
	if (isFocus) caret.killFocus ();
	update ();
    GC gc = new GC (this);
    gc.copyArea (x, y, width, height, destX, destY);
    gc.dispose ();
	if (isFocus) caret.setFocus ();
}

public void setCaret (Caret caret) {
	checkWidget();
	Caret newCaret = caret;
	Caret oldCaret = this.caret;
	this.caret = newCaret;
	if (hasFocus ()) {
		if (oldCaret != null) oldCaret.killFocus ();
		if (newCaret != null) {
			if (newCaret.isDisposed()) error(SWT.ERROR_INVALID_ARGUMENT);
			newCaret.setFocus ();
		}
	}
}

}