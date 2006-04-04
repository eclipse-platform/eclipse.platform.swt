/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.widgets;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;

public class ExpandItem extends Item {
	ExpandBar parent;
	Control control;
	boolean expanded;
	int x, y, width, height;
	int imageHeight, imageWidth;
	static final int TEXT_INSET = 6;
	static final int BORDER = 1;
	static final int CHEVRON_SIZE = 24;

public ExpandItem (ExpandBar parent, int style) {
	this (parent, style, checkNull (parent).getItemCount ());
}

public ExpandItem (ExpandBar parent, int style, int index) {
	super (parent, style);
	this.parent = parent;
	parent.createItem (this, style, index);
}

static ExpandBar checkNull (ExpandBar control) {
	if (control == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
	return control;	
}

public void dispose () {
	if (isDisposed ()) return;
	//if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	parent.destroyItem (this);
	super.dispose();
	parent = null;
	control = null;
}

void drawChevron (GC gc, int x, int y) {
	int [] polyline1, polyline2;
	if (expanded) {
		int px = x + 4 + 5;
		int py = y + 4 + 7;
		polyline1 = new int [] {
				px,py, px+1,py, px+1,py-1, px+2,py-1, px+2,py-2, px+3,py-2, px+3,py-3,
				px+3,py-2, px+4,py-2, px+4,py-1, px+5,py-1, px+5,py, px+6,py};
		py += 4;
		polyline2 = new int [] {
				px,py, px+1,py, px+1,py-1, px+2,py-1, px+2,py-2, px+3,py-2, px+3,py-3,
				px+3,py-2, px+4,py-2, px+4,py-1,  px+5,py-1, px+5,py, px+6,py};
	} else {
		int px = x + 4 + 5;
		int py = y + 4 + 4;
		polyline1 = new int[] {
				px,py, px+1,py, px+1,py+1, px+2,py+1, px+2,py+2, px+3,py+2, px+3,py+3,
				px+3,py+2, px+4,py+2, px+4,py+1,  px+5,py+1, px+5,py, px+6,py};
		py += 4;
		polyline2 = new int [] {
				px,py, px+1,py, px+1,py+1, px+2,py+1, px+2,py+2, px+3,py+2, px+3,py+3,
				px+3,py+2, px+4,py+2, px+4,py+1,  px+5,py+1, px+5,py, px+6,py};
	}
	gc.setForeground (display.getSystemColor (SWT.COLOR_TITLE_FOREGROUND));
	gc.drawPolyline (polyline1);
	gc.drawPolyline (polyline2);
}

void drawItem (GC gc, boolean drawFocus) {
	int headerHeight = parent.getBandHeight ();
	Display display = getDisplay ();
	gc.setForeground (display.getSystemColor (SWT.COLOR_TITLE_BACKGROUND));
	gc.setBackground (display.getSystemColor (SWT.COLOR_TITLE_BACKGROUND_GRADIENT));
	gc.fillGradientRectangle (x, y, width, headerHeight, true);
	if (expanded) {
		gc.setForeground (display.getSystemColor (SWT.COLOR_TITLE_BACKGROUND_GRADIENT));
		gc.drawLine (x, y + headerHeight, x, y + headerHeight + height - 1);
		gc.drawLine (x, y + headerHeight + height - 1, x + width - 1, y + headerHeight + height - 1);
		gc.drawLine (x + width - 1, y + headerHeight + height - 1, x + width - 1, y + headerHeight);
	}
	int drawX = x;
	if (image != null) {
		drawX += ExpandItem.TEXT_INSET;
		if (imageHeight > headerHeight) {
			gc.drawImage (image, drawX, y + headerHeight - imageHeight);
		} else {
			gc.drawImage (image, drawX, y + (headerHeight - imageHeight) / 2);
		}
		drawX += imageWidth;
	}
	if (text.length() > 0) {
		drawX += ExpandItem.TEXT_INSET;
		Point size = gc.stringExtent (text);
		gc.setForeground (parent.getForeground ());
		gc.drawString (text, drawX, y + (headerHeight - size.y) / 2, true);
	}
	int chevronSize = ExpandItem.CHEVRON_SIZE;
	drawChevron (gc, x + width - chevronSize, y + (headerHeight - chevronSize) / 2);
	if (drawFocus) {
		gc.drawFocus (x + 1, y + 1, width - 2, headerHeight - 2);
	}
}

public Control getControl() {
	checkWidget ();
	return control;
}

public boolean getExpanded() {
	checkWidget ();
	return expanded;
}

public int getHeaderHeight () {
	checkWidget ();
	return Math.max (parent.getBandHeight (), imageHeight);
}

public int getHeight () {
	checkWidget ();
	return height;
}

public ExpandBar getParent () {
	checkWidget ();
	return parent;
}

int getPreferredWidth (GC gc) {
	int width = ExpandItem.TEXT_INSET * 2 + ExpandItem.CHEVRON_SIZE;
	if (image != null) {
		width += ExpandItem.TEXT_INSET + imageWidth;
	}
	if (text.length() > 0) {
		width += gc.stringExtent (text).x;
	}
	return width;
}

void redraw () {
	int headerHeight = parent.getBandHeight ();
	if (imageHeight > headerHeight) {
		parent.redraw (x + ExpandItem.TEXT_INSET, y + headerHeight - imageHeight, imageWidth, imageHeight, false);
	}
	parent.redraw (x, y, width, headerHeight + height, false);
}

void setBounds (int x, int y, int width, int height, boolean move, boolean size) {
	redraw ();
	int headerHeight = parent.getBandHeight ();
	if (move) {
		if (imageHeight > headerHeight) {
			y += (imageHeight - headerHeight);
		}
		this.x = x;
		this.y = y;
		redraw ();
	}
	if (size) {
		this.width = width;
		this.height = height;
		redraw ();
	}
	if (control != null && !control.isDisposed ()) {
		if (move) control.setLocation (x + BORDER, y + headerHeight);
		if (size) control.setSize (Math.max (0, width - 2 * BORDER), Math.max (0, height - BORDER));
	}
}

public void setControl(Control control) {
	checkWidget ();
	if (control != null) {
		if (control.isDisposed ()) error (SWT.ERROR_INVALID_ARGUMENT);
		if (control.parent != parent) error (SWT.ERROR_INVALID_PARENT);
	}
	this.control = control;
	if (control != null) {
		control.setVisible (expanded);
		int headerHeight = parent.getBandHeight ();
		control.setBounds (x + BORDER, y + headerHeight, Math.max (0, width - 2 * BORDER), Math.max (0, height - BORDER));
	}
}

public void setExpanded (boolean expanded) {
	checkWidget ();
	this.expanded = expanded;
	parent.showItem (parent.indexOf (this));
}

public void setImage (Image image) {
	super.setImage (image);
	int oldImageHeight = imageHeight;
	if (image != null) {
		Rectangle bounds = image.getBounds ();
		imageHeight = bounds.height;
		imageWidth = bounds.width;
	} else {
		imageHeight = imageWidth = 0;
	}
	if (oldImageHeight != imageHeight) {
		parent.layoutItems (parent.indexOf (this), true);
	} else {
		redraw ();
	}
}

public void setHeight (int height) {
	checkWidget ();
	if (height < 0) return;
	setBounds (0, 0, width, height, false, true);
	if (expanded) parent.layoutItems (parent.indexOf (this) + 1, true);
}

public void setText (String string) {
	super.setText (string);
	redraw ();
}
}
