package org.eclipse.swt.widgets;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */
 
import org.eclipse.swt.internal.carbon.OS;
 
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;

public class ToolBar extends Composite {
	int itemCount;
	ToolItem [] items;


public ToolBar (Composite parent, int style) {
	super (parent, checkStyle (style));
	
	/*
	* Ensure that either of HORIZONTAL or VERTICAL is set.
	* NOTE: HORIZONTAL and VERTICAL have the same values
	* as H_SCROLL and V_SCROLL so it is necessary to first
	* clear these bits to avoid scroll bars and then reset
	* the bits using the original style supplied by the
	* programmer.
	*/
	if ((style & SWT.VERTICAL) != 0) {
		this.style |= SWT.VERTICAL;
	} else {
		this.style |= SWT.HORIZONTAL;
	}
}

static int checkStyle (int style) {
	/*
	* Even though it is legal to create this widget
	* with scroll bars, they serve no useful purpose
	* because they do not automatically scroll the
	* widget's client area.  The fix is to clear
	* the SWT style.
	*/
	return style & ~(SWT.H_SCROLL | SWT.V_SCROLL);
}

protected void checkSubclass () {
	if (!isValidSubclass ()) error (SWT.ERROR_INVALID_SUBCLASS);
}

public Point computeSize (int wHint, int hHint, boolean changed) {
	checkWidget();
	int width = wHint, height = hHint;
	if (wHint == SWT.DEFAULT) width = 0x7FFFFFFF;
	if (hHint == SWT.DEFAULT) height = 0x7FFFFFFF;
	int [] result = layout (width, height, false);
	Point extent = new Point (result [1], result [2]);
	if (wHint != SWT.DEFAULT) extent.x = wHint;
	if (hHint != SWT.DEFAULT) extent.y = hHint;
	return extent;
}

void createHandle () {
	state |= GRAB;
	super.createHandle (parent.handle);
}

void createItem (ToolItem item, int index) {
	if (!(0 <= index && index <= itemCount)) error (SWT.ERROR_INVALID_RANGE);
	if (itemCount == items.length) {
		ToolItem [] newItems = new ToolItem [itemCount + 4];
		System.arraycopy (items, 0, newItems, 0, items.length);
		items = newItems;
	}
	item.createWidget ();
	System.arraycopy (items, index, items, index + 1, itemCount++ - index);
	items [index] = item;
	if (parent.font != null) item.setFontStyle (parent.font);
}

void createWidget () {
	super.createWidget ();
	items = new ToolItem [4];
	itemCount = 0;
}

int defaultThemeFont () {	
	return OS.kThemeToolbarFont;
}

void destroyItem (ToolItem item) {
	int index = 0;
	while (index < itemCount) {
		if (items [index] == item) break;
		index++;
	}
	if (index == itemCount) return;
	System.arraycopy (items, index + 1, items, index, --itemCount - index);
	items [itemCount] = null;
}

void drawBackground (int control) {
	drawBackground (control, background);
}

void enableWidget (boolean enabled) {
	/* Do nothing - A tool bar does not disable items when it is disabled */
}

public ToolItem getItem (int index) {
	checkWidget();
	if (0 <= index && index < itemCount) return items [index];
	error (SWT.ERROR_INVALID_RANGE);
	return null;
}

public ToolItem getItem (Point pt) {
	checkWidget();
	if (pt == null) error (SWT.ERROR_NULL_ARGUMENT);
	for (int i=0; i<itemCount; i++) {
		Rectangle rect = items [i].getBounds ();
		if (rect.contains (pt)) return items [i];
	}
	return null;
}

public int getItemCount () {
	checkWidget();
	return itemCount;
}

public ToolItem [] getItems () {
	checkWidget();
	ToolItem [] result = new ToolItem [itemCount];
	System.arraycopy (items, 0, result, 0, itemCount);
	return result;
}

public int getRowCount () {
	checkWidget();
	Rectangle rect = getClientArea ();
	return layout (rect.width, rect.height, false) [0];
}

public int indexOf (ToolItem item) {
	checkWidget();
	if (item == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (item.isDisposed()) error(SWT.ERROR_INVALID_ARGUMENT);
	for (int i=0; i<itemCount; i++) {
		if (items [i] == item) return i;
	}
	return -1;
}

int [] layoutHorizontal (int width, int height, boolean resize) {
	int xSpacing = 0, ySpacing = 2;
	int marginWidth = 0, marginHeight = 0;
	int x = marginWidth, y = marginHeight;
	int maxHeight = 0, maxX = 0, rows = 1;
	boolean wrap = (style & SWT.WRAP) != 0;
	int itemHeight = 0;
	for (int i=0; i<itemCount; i++) {
		Rectangle rect = items [i].getBounds ();
		itemHeight = Math.max (itemHeight, rect.height);
	}
	for (int i=0; i<itemCount; i++) {
		ToolItem item = items [i];
		Rectangle rect = item.getBounds ();
		if (wrap && i != 0 && x + rect.width > width) {
			rows++;
			x = marginWidth;
			y += ySpacing + maxHeight;
			maxHeight = 0;
		}
		maxHeight = Math.max (maxHeight, rect.height);
		if (resize) {
			item.setBounds (x, y, rect.width, itemHeight);
		}
		x += xSpacing + rect.width;
		maxX = Math.max (maxX, x);
	}
	return new int [] {rows, maxX, y + maxHeight};
}

int [] layoutVertical (int width, int height, boolean resize) {
	int xSpacing = 2, ySpacing = 0;
	int marginWidth = 0, marginHeight = 0;
	int x = marginWidth, y = marginHeight;
	int maxWidth = 0, maxY = 0, cols = 1;
	boolean wrap = (style & SWT.WRAP) != 0;
	int itemWidth = 0;
	for (int i=0; i<itemCount; i++) {
		Rectangle rect = items [i].getBounds ();
		itemWidth = Math.max (itemWidth, rect.width);
	}
	for (int i=0; i<itemCount; i++) {
		ToolItem item = items [i];
		Rectangle rect = item.getBounds ();
		if (wrap && i != 0 && y + rect.height > height) {
			cols++;
			x += xSpacing + maxWidth;
			y = marginHeight;
			maxWidth = 0;
		}
		maxWidth = Math.max (maxWidth, rect.width);
		if (resize) {
			item.setBounds (x, y, itemWidth, rect.height);
		}
		y += ySpacing + rect.height;
		maxY = Math.max (maxY, y);
	}
	return new int [] {cols, x + maxWidth, maxY};
}

int [] layout (int nWidth, int nHeight, boolean resize) {
	if ((style & SWT.VERTICAL) != 0) {
		return layoutVertical (nWidth, nHeight, resize);
	} else {
		return layoutHorizontal (nWidth, nHeight, resize);
	}
}

void relayout () {
	if (drawCount > 0) return;
	Rectangle rect = getClientArea ();
	layout (rect.width, rect.height, true);
}

void relayout (int width, int height) {
	if (drawCount > 0) return;
	layout (width, height, true);
}

void releaseWidget () {
	for (int i=0; i<itemCount; i++) {
		ToolItem item = items [i];
		if (!item.isDisposed ()) item.releaseResources ();
	}
	items = null;
	super.releaseWidget ();
}

int setBounds (int control, int x, int y, int width, int height, boolean move, boolean resize, boolean events) {
	int result = super.setBounds (control, x, y, width, height, move, resize, events);
	if ((result & RESIZED) != 0) {
		Rectangle rect = getClientArea ();
		relayout (rect.width, rect.height);
	}
	return result;
}

void setFontStyle (Font font) {
	super.setFontStyle (font);
	for (int i=0; i<itemCount; i++) {
		ToolItem item = items [i];
		item.setFontStyle (font);
		Point size = item.computeSize ();
		item.setSize (size.x, size.y, false);
	}
	relayout ();
}

public void setRedraw (boolean redraw) {
	checkWidget();
	super.setRedraw (redraw);
	if (redraw && drawCount == 0) relayout();
}

}
