package org.eclipse.swt.widgets;

/*
* Licensed Materials - Property of IBM,
* SWT - The Simple Widget Toolkit,
* (c) Copyright IBM Corp 1998, 1999.
*/

/**
*	The tool bar class supports the layout of
* selectable tool bar items.
*
* Styles
*
*	WRAP, FLAT
*
* Events
*
**/

/* Imports */
import org.eclipse.swt.internal.motif.*;
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;

/* Class Definition */
public /*final*/ class ToolBar extends Composite {
	int drawCount, itemCount;
	ToolItem [] items;
/**
* Creates a new instance of the widget.
*/
public ToolBar (Composite parent, int style) {
	super (parent, checkStyle (style));
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
/**
* Computes the preferred size.
*/
public Point computeSize (int wHint, int hHint, boolean changed) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	int width = wHint, height = hHint;
	if (wHint == SWT.DEFAULT) width = 0x7FFFFFFF;
	if (hHint == SWT.DEFAULT) height = 0x7FFFFFFF;
	int [] result = layout (width, height, false);
	int border = getBorderWidth () * 2;
	Point extent = new Point (result [1], result [2]);
	if (wHint != SWT.DEFAULT) extent.x = wHint;
	if (hHint != SWT.DEFAULT) extent.y = hHint;
	extent.x += border;
	extent.y += border;
	return extent;
}
void createItem (ToolItem item, int index) {
	if (!(0 <= index && index <= itemCount)) error (SWT.ERROR_INVALID_RANGE);
	if (itemCount == items.length) {
		ToolItem [] newItems = new ToolItem [itemCount + 4];
		System.arraycopy (items, 0, newItems, 0, items.length);
		items = newItems;
	}
	item.createWidget (index);
	System.arraycopy (items, index, items, index + 1, itemCount++ - index);
	items [index] = item;
}

void createHandle (int index) {
	super.createHandle (index);
	state &= ~CANVAS;
}

void createWidget (int index) {
	super.createWidget (index);
	items = new ToolItem [4];
	itemCount = 0;
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
public ToolItem getItem (int index) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	ToolItem [] items = getItems ();
	if (0 <= index && index < items.length) return items [index];
	error (SWT.ERROR_INVALID_RANGE);
	return null;
}

public ToolItem getItem (Point pt) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	ToolItem [] items = getItems ();
	for (int i=0; i<items.length; i++) {
		Rectangle rect = items [i].getBounds ();
		if (rect.contains (pt)) return items [i];
	}
	return null;
}

public int getItemCount () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	return itemCount;
}
public ToolItem [] getItems () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	ToolItem [] result = new ToolItem [itemCount];
	System.arraycopy (items, 0, result, 0, itemCount);
	return result;
}
public int getRowCount () {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	Rectangle rect = getClientArea ();
	return layout (rect.width, rect.height, false) [0];
}
public int indexOf (ToolItem item) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (item == null) error (SWT.ERROR_NULL_ARGUMENT);
	ToolItem [] items = getItems ();
	for (int i=0; i<items.length; i++) {
		if (items [i] == item) return i;
	}
	return -1;
}
int [] layout (int nWidth, int nHeight, boolean resize) {
	int xSpacing = 0, ySpacing = 4;
	int marginWidth = 0, marginHeight = 0;
	ToolItem [] children = getItems ();
	int length = children.length;
	int x = marginWidth, y = marginHeight;
	int maxHeight = 0, maxX = 0, rows = 1;
	boolean wrap = (style & SWT.WRAP) != 0;
	for (int i=0; i<length; i++) {
		ToolItem child = children [i];
		Rectangle rect = child.getBounds ();
		if (wrap && i != 0 && x + rect.width > nWidth) {
			rows++;
			x = marginWidth;  y += ySpacing + maxHeight;
			maxHeight = 0;
		}
		maxHeight = Math.max (maxHeight, rect.height);
		if (resize) {
			child.setBounds (x, y, rect.width, rect.height);
		}
		x += xSpacing + rect.width;
		maxX = Math.max (maxX, x);
	}
	return new int [] {rows, maxX, y + maxHeight};
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
		if (!item.isDisposed ()) {
			item.releaseWidget ();
			item.releaseHandle ();
		}
	}
	items = null;
	super.releaseWidget ();
}
public void setBounds (int x, int y, int width, int height) {
	super.setBounds (x, y, width, height);
	Rectangle rect = getClientArea ();
	relayout (rect.width, rect.height);
}
public void setRedraw (boolean redraw) {
	if (!isValidThread ()) error (SWT.ERROR_THREAD_INVALID_ACCESS);
	if (!isValidWidget ()) error (SWT.ERROR_WIDGET_DISPOSED);
	if (redraw) {
		if (--drawCount == 0) relayout();
	} else {
		drawCount++;
	}
}
public void setSize (int width, int height) {
	super.setSize (width, height);
	Rectangle rect = getClientArea ();
	relayout (rect.width, rect.height);
}
}
