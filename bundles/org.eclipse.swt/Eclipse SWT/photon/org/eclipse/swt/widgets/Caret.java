package org.eclipse.swt.widgets;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import org.eclipse.swt.internal.photon.*;
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;

public /*final*/ class Caret extends Widget {
	Canvas parent;
	Image image;
	int x, y, width, height;
	boolean moved, resized;
	boolean isVisible, isShowing;
//	int blinkRate = 500;

public Caret (Canvas parent, int style) {
	super (parent, style);
	this.parent = parent;
	createWidget (0);
}

//boolean blinkCaret () {
//	if (!isVisible) return true;
//	if (!isShowing) return showCaret ();
//	if (blinkRate == 0) return true;
//	return hideCaret ();
//}

void createWidget (int index) {
	super.createWidget (index);
	isVisible = true;
	if (parent.getCaret () == null) {
		parent.setCaret (this);
	}
}

boolean drawCaret () {
	if (parent == null) return false;
	if (parent.isDisposed ()) return false;
	int handle = parent.handle;
	if (!OS.PtWidgetIsRealized (handle)) return false;
	int phGC = OS.PgCreateGC (0); // NOTE: PgCreateGC ignores the parameter
	if (phGC == 0) return false;
	int [] args = {OS.Pt_ARG_COLOR, 0, 0, OS.Pt_ARG_FILL_COLOR, 0, 0};
	OS.PtGetResources (handle, args.length / 3, args);
	int foreground = args [1];
	int background = args [4];
	int color = foreground ^ ~background;
	int prevContext = OS.PgSetGC (phGC);
	OS.PgSetRegion (OS.PtWidgetRid (handle));
	OS.PgSetDrawMode (OS.Pg_DRAWMODE_XOR);
	OS.PgSetFillColor (color);
	int nWidth = width, nHeight = height;
	if (image != null) {
		Rectangle rect = image.getBounds ();
		nWidth = rect.width;
		nHeight = rect.height;
	}
	if (nWidth <= 0) nWidth = 2;
	OS.PgDrawIRect (x, y, x + nWidth - 1, y + nHeight - 1, OS.Pg_DRAW_FILL);
	OS.PgSetGC (prevContext);	
	OS.PgDestroyGC (phGC);
	return true;
}	

public Rectangle getBounds () {
	checkWidget();
	if (image != null) {
		Rectangle rect = image.getBounds ();
		return new Rectangle (x, y, rect.width, rect.height);
	}
	return new Rectangle (x, y, width, height);
}

public Display getDisplay () {
	Composite parent = this.parent;
	if (parent == null) error (SWT.ERROR_WIDGET_DISPOSED);
	return parent.getDisplay ();
}

public Font getFont () {
	checkWidget();
	return parent.getFont ();
}

public Image getImage () {
	checkWidget();
	return image;
}

public Point getLocation () {
	checkWidget();
	return new Point (x, y);
}

public Canvas getParent () {
	checkWidget();
	return parent;
}

public Point getSize () {
	checkWidget();
	if (image != null) {
		Rectangle rect = image.getBounds ();
		return new Point (rect.width, rect.height);
	}
	return new Point (width, height);
}

public boolean getVisible () {
	checkWidget();
	return isVisible;
}

boolean hideCaret () {
//	Display display = getDisplay ();
//	if (display.currentCaret != this) return false;
	if (!isShowing) return true;
	isShowing = false;
	return drawCaret ();
}

public boolean isVisible () {
	checkWidget();
	return isVisible && parent.isVisible () && parent.hasFocus ();
}

void killFocus () {
//	Display display = getDisplay ();
//	if (display.currentCaret != this) return;
	if (isVisible) hideCaret ();
//	display.setCurrentCaret (null);
}

void releaseChild () {
	super.releaseChild ();
	if (this == parent.getCaret ()) parent.setCaret (null);
}

void releaseWidget () {
	super.releaseWidget ();
//	Display display = getDisplay ();
//	if (display.currentCaret == this) {
//		if (isVisible) hideCaret ();
//		display.setCurrentCaret (null);
//	}
	parent = null;
	image = null;
}

public void setBounds (int x, int y, int width, int height) {
	checkWidget();
	boolean samePosition, sameExtent, showing;
	samePosition = (this.x == x) && (this.y == y);
	sameExtent = (this.width == width) && (this.height == height);
	if ((samePosition) && (sameExtent)) return;
	if (showing = isShowing) hideCaret ();
	this.x = x; this.y = y;
	this.width = width; this.height = height;
	if (sameExtent) {
			moved = true;
			if (isVisible ()) {
				moved = false;
			}
	} else {
			resized = true;
			if (isVisible ()) {
				moved = false;
				resized = false;
			}
	}
	if (showing) showCaret ();
}

void setFocus () {
//	Display display = getDisplay ();
//	if (display.currentCaret == this) return;
//	display.setCurrentCaret (this);
	if (isVisible) showCaret ();
}

public void setFont (Font font) {
	checkWidget();
	if (font != null && font.isDisposed ()) {
		error (SWT.ERROR_INVALID_ARGUMENT);
	}
}

public void setImage (Image image) {
	checkWidget();
	if (image != null && image.isDisposed ()) {
		error (SWT.ERROR_INVALID_ARGUMENT);
	}
	if (isShowing) hideCaret ();
	this.image = image;
	if (isShowing) showCaret ();
}

public void setLocation (int x, int y) {
	checkWidget();
	setBounds (x, y, width, height);
}

public void setLocation (Point location) {
	checkWidget();
	if (location == null) error (SWT.ERROR_NULL_ARGUMENT);
	setLocation (location.x, location.y);
}

public void setSize (int width, int height) {
	checkWidget();
	setBounds (x, y, width, height);
}

public void setSize (Point size) {
	checkWidget();
	if (size == null) error (SWT.ERROR_NULL_ARGUMENT);
	setSize (size.x, size.y);
}

public void setVisible (boolean visible) {
	checkWidget();
	if (visible == isVisible) return;
	if (isVisible = visible) {
		showCaret ();
	} else {
		hideCaret ();
	}
}

boolean showCaret () {
//	if (getDisplay ().currentCaret != this) return false;
	if (isShowing) return true;
	isShowing = true;
	return drawCaret ();
}
}
