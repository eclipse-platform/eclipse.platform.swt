/*******************************************************************************
 * Copyright (c) 2000, 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.widgets;

 
import org.eclipse.swt.internal.motif.*;
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;

/**
 * Instances of this class support the layout of selectable
 * tool bar items.
 * <p>
 * The item children that may be added to instances of this class
 * must be of type <code>ToolItem</code>.
 * </p><p>
 * Note that although this class is a subclass of <code>Composite</code>,
 * it does not make sense to add <code>Control</code> children to it,
 * or set a layout on it.
 * </p><p>
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>FLAT, WRAP, RIGHT, HORIZONTAL, VERTICAL, SHADOW_OUT</dd>
 * <dt><b>Events:</b></dt>
 * <dd>(none)</dd>
 * </dl>
 * <p>
 * Note: Only one of the styles HORIZONTAL and VERTICAL may be specified.
 * </p><p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 *
 * @see <a href="http://www.eclipse.org/swt/snippets/#toolbar">ToolBar, ToolItem snippets</a>
 * @see <a href="http://www.eclipse.org/swt/examples.php">SWT Example: ControlExample</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 * @noextend This class is not intended to be subclassed by clients.
 */
public class ToolBar extends Composite {
	int drawCount, itemCount;
	ToolItem [] items;
	ToolItem lastFocus;
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
 * @see SWT#FLAT
 * @see SWT#WRAP
 * @see SWT#RIGHT
 * @see SWT#HORIZONTAL
 * @see SWT#SHADOW_OUT
 * @see SWT#VERTICAL
 * @see Widget#checkSubclass()
 * @see Widget#getStyle()
 */
public ToolBar (Composite parent, int style) {
	super (parent, checkStyle (style));
	
	/*
	* Ensure that either HORIZONTAL or VERTICAL is set.
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
	int border = getBorderWidth () * 2;
	Point extent = new Point (result [1], result [2]);
	if (wHint != SWT.DEFAULT) extent.x = wHint;
	if (hHint != SWT.DEFAULT) extent.y = hHint;
	extent.x += border;
	extent.y += border;
	return extent;
}
void createHandle (int index) {
	super.createHandle (index);
	state &= ~CANVAS;
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
	relayout ();
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
public boolean forceFocus () {
	checkWidget ();
	if (display.focusEvent == SWT.FocusOut) return false;
	Decorations shell = menuShell ();
	shell.setSavedFocus (this);
	shell.bringToTop (false);
	if (lastFocus != null && lastFocus.setFocus ()) return true;
	for (int i = 0; i < itemCount; i++) {
		ToolItem item = items [i];
		if (item.setFocus ()) return true;
	}
	return super.forceFocus ();
}
/**
 * Returns the item at the given, zero-relative index in the
 * receiver. Throws an exception if the index is out of range.
 *
 * @param index the index of the item to return
 * @return the item at the given index
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_RANGE - if the index is not between 0 and the number of elements in the list minus 1 (inclusive)</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public ToolItem getItem (int index) {
	checkWidget();
	if (0 <= index && index < itemCount) return items [index];
	error (SWT.ERROR_INVALID_RANGE);
	return null;
}

/**
 * Returns the item at the given point in the receiver
 * or null if no such item exists. The point is in the
 * coordinate system of the receiver.
 *
 * @param point the point used to locate the item
 * @return the item at the given point
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the point is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public ToolItem getItem (Point pt) {
	checkWidget();
	if (pt == null) error (SWT.ERROR_NULL_ARGUMENT);
	for (int i=0; i<itemCount; i++) {
		Rectangle rect = items [i].getBounds ();
		if (rect.contains (pt)) return items [i];
	}
	return null;
}

/**
 * Returns the number of items contained in the receiver.
 *
 * @return the number of items
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getItemCount () {
	checkWidget();
	return itemCount;
}
/**
 * Returns an array of <code>ToolItem</code>s which are the items
 * in the receiver. 
 * <p>
 * Note: This is not the actual structure used by the receiver
 * to maintain its list of items, so modifying the array will
 * not affect the receiver. 
 * </p>
 *
 * @return the items in the receiver
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public ToolItem [] getItems () {
	checkWidget();
	ToolItem [] result = new ToolItem [itemCount];
	System.arraycopy (items, 0, result, 0, itemCount);
	return result;
}
/**
 * Returns the number of rows in the receiver. When
 * the receiver has the <code>WRAP</code> style, the
 * number of rows can be greater than one.  Otherwise,
 * the number of rows is always one.
 *
 * @return the number of items
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getRowCount () {
	checkWidget();
	Rectangle rect = getClientArea ();
	return layout (rect.width, rect.height, false) [0];
}
/**
 * Searches the receiver's list starting at the first item
 * (index 0) until an item is found that is equal to the 
 * argument, and returns the index of that item. If no item
 * is found, returns -1.
 *
 * @param item the search item
 * @return the index of the item
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the tool item is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if the tool item has been disposed</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
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
	int xSpacing = 0, ySpacing = (style & SWT.NO_FOCUS) != 0 ? 4 : 2;
	int marginWidth = 0, marginHeight = 0;
	int x = marginWidth, y = marginHeight;
	int maxX = 0, rows = 1;
	boolean wrap = (style & SWT.WRAP) != 0;
	int itemHeight = 0;
	Point [] sizes = new Point [itemCount];
	GC gc = new GC (this);
	for (int i=0; i<itemCount; i++) {
		Point size = sizes [i] = items [i].computeSize (gc);
		itemHeight = Math.max (itemHeight, size.y);
	}
	for (int i=0; i<itemCount; i++) {
		ToolItem item = items [i];
		Point size = sizes [i];
		if (wrap && i != 0 && x + size.x > width) {
			rows++;
			x = marginWidth;
			y += ySpacing + itemHeight;
		}
		if (resize) {
			item.setBounds (x, y, size.x, itemHeight);
			boolean visible = x + size.x <= width && y + itemHeight <= height;
			item.setVisible (visible);
			Control control = item.control;
			if (control != null) {
				int controlY = y + (itemHeight - size.y) / 2;
				control.setBounds (x, controlY, size.x, itemHeight - (controlY - y));
			}
		}
		x += xSpacing + size.x;
		maxX = Math.max (maxX, x);
	}
	gc.dispose ();
	return new int [] {rows, maxX, y + itemHeight};
}
int [] layoutVertical (int width, int height, boolean resize) {
	int xSpacing = (style & SWT.NO_FOCUS) != 0 ? 4 : 2, ySpacing = 0;
	int marginWidth = 0, marginHeight = 0;
	int x = marginWidth, y = marginHeight;
	int maxY = 0, cols = 1;
	boolean wrap = (style & SWT.WRAP) != 0;
	int itemWidth = 0;
	Point [] sizes = new Point [itemCount];
	GC gc = new GC (this);
	for (int i=0; i<itemCount; i++) {
		Point size = sizes [i] = items [i].computeSize (gc);
		itemWidth = Math.max (itemWidth, size.x);
	}	
	for (int i=0; i<itemCount; i++) {
		ToolItem item = items [i];
		Point size = sizes [i];
		if (wrap && i != 0 && y + size.y > height) {
			cols++;
			x += xSpacing + itemWidth;
			y = marginHeight;
		}
		if (resize) {
			item.setBounds (x, y, itemWidth, size.y);
			boolean visible = x + itemWidth <= width && y + size.y <= height;
			item.setVisible (visible);
			Control control = item.control;
			if (control != null) {
				int controlX = x + (itemWidth - size.x) / 2;
				control.setBounds (controlX, y, itemWidth - (controlX - x), size.y);
			}
		}
		y += ySpacing + size.y;
		maxY = Math.max (maxY, y);
	}
	return new int [] {cols, x + itemWidth, maxY};
}
int [] layout (int nWidth, int nHeight, boolean resize) {
	if ((style & SWT.VERTICAL) != 0) {
		return layoutVertical (nWidth, nHeight, resize);
	} else {
		return layoutHorizontal (nWidth, nHeight, resize);
	}
}
boolean mnemonicHit (char key) {
	for (int i = 0; i < itemCount; i++) {
		ToolItem item = items [i];
		char mnemonic = findMnemonic (item.getText ());
		if (mnemonic != '\0') {
			if (Character.toUpperCase (key) == Character.toUpperCase (mnemonic)) {
				XmProcessTraversal (item.handle, OS.XmTRAVERSE_CURRENT);
				item.click (false, 0);
				return true;
			}
		}
	}
	return false;
}
boolean mnemonicMatch (char key) {
	for (int i = 0; i < itemCount; i++) {
		ToolItem item = items [i];
		if (item.getEnabled ()) {
			char mnemonic = findMnemonic (item.getText ());
			if (mnemonic != '\0') {
				if (Character.toUpperCase (key) == Character.toUpperCase (mnemonic)) {
					return true;
				}
			}
		}
	}
	return false;
}
void propagateWidget (boolean enabled) {
	super.propagateWidget (enabled);
	for (int i=0; i<itemCount; i++) {
		items [i].propagateWidget (enabled);
	}
}
void realizeChildren () {
	super.realizeChildren ();
	if (items != null) {
		for (int i=0; i<itemCount; i++) {
			ToolItem item = items [i];
			if (item != null && !item.isDisposed ()) {
				item.realizeChildren ();
			}
		}
	}
}
void redrawWidget (int x, int y, int width, int height, boolean redrawAll, boolean allChildren, boolean trim) {
	super.redrawWidget (x, y, width, height, redrawAll, allChildren, trim);
	if (items != null) {
		for (int i=0; i<itemCount; i++) {
			ToolItem item = items [i];
			if (item != null && !item.isDisposed ()) {
				item.redrawWidget (x, y, width, height, redrawAll, allChildren, true);
			}
		}
	}
}
void relayout () {
	if (drawCount > 0) return;
	Rectangle rect = getClientArea ();
	layout (rect.width, rect.height, true);
}
void releaseChildren (boolean destroy) {
	if (items != null) {
		for (int i=0; i<itemCount; i++) {
			ToolItem item = items [i];
			if (item != null && !item.isDisposed ()) {
				item.release (false);
			}
		}
		items = null;
	}
	super.releaseChildren (destroy);
}
void removeControl (Control control) {
	super.removeControl (control);
	for (int i=0; i<itemCount; i++) {
		ToolItem item = items [i];
		if (item.control == control) item.setControl (null);
	}
}
void setBackgroundPixel (int pixel) {
	super.setBackgroundPixel (pixel);
	for (int i=0; i<itemCount; i++) {
		items[i].setBackgroundPixel (pixel);
	}
}
boolean setBounds (int x, int y, int width, int height, boolean move, boolean resize) {
	boolean changed = super.setBounds (x, y, width, height, move, resize);
	if (changed && resize) relayout ();
	return changed;
}
public void setFont (Font font) {
	checkWidget();
	super.setFont (font);
	relayout ();
}
void setForegroundPixel (int pixel) {
	super.setForegroundPixel (pixel);
	for (int i = 0; i < itemCount; i++) {
		items[i].setForegroundPixel (pixel);
	}
}
public void setRedraw (boolean redraw) {
	checkWidget();
	if (redraw) {
		if (--drawCount == 0) relayout();
	} else {
		drawCount++;
	}
}
boolean setTabItemFocus (boolean next) {
	int index = 0;
	while (index < itemCount) {
		ToolItem item = items [index];
		if ((item.style & SWT.SEPARATOR) == 0) {
			if (item.getEnabled ()) break;
		}
		index++;
	}
	if (index == itemCount) return false;
	return super.setTabItemFocus (next);
}
int traversalCode (int key, XKeyEvent xEvent) {
	return super.traversalCode (key, xEvent) | SWT.TRAVERSE_MNEMONIC;
}
int xFocusIn (XFocusChangeEvent xEvent) {
	int newFocus = OS.XmGetFocusWidget (handle); 
	if (newFocus != focusHandle ()) {
		for (int i = 0; i < itemCount; i++) {
			if (items [i].handle == newFocus) {
				lastFocus = items [i];
				break;
			}
		}
	}
	return super.xFocusIn (xEvent);
}
}
