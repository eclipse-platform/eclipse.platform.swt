/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.widgets;
 
import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
 
public class TreeColumn extends Item {
	Tree parent;
	int width;
	boolean resizable = true;
	
public TreeColumn (Tree parent, int style) {
	this (parent, style, checkNull (parent).getColumnCount ());
}
public TreeColumn (Tree parent, int style, int index) {
	super (parent, checkStyle (style), index);
	if (!(0 <= index && index <= parent.getColumnCount ())) error (SWT.ERROR_INVALID_RANGE);
	this.parent = parent;
	parent.addColumn (this, index);
}
public void addControlListener (ControlListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.Resize, typedListener);
	addListener (SWT.Move, typedListener);
}
public void addSelectionListener (SelectionListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.Selection, typedListener);
	addListener (SWT.DefaultSelection, typedListener);
}
static Tree checkNull (Tree tree) {
	if (tree == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
	return tree;
}
static int checkStyle (int style) {
	return checkBits (style, SWT.LEFT, SWT.CENTER, SWT.RIGHT, 0, 0, 0);
}
protected void checkSubclass () {
	if (!isValidSubclass ()) error (SWT.ERROR_INVALID_SUBCLASS);
}
public void dispose () {
	if (isDisposed ()) return;
	Rectangle parentBounds = parent.getClientArea ();
	int x = getX ();
	Tree parent = this.parent;
	dispose (true);
	int width = parentBounds.width - x;
	parent.redraw (x, 0, width, parentBounds.height, false);
	if (parent.getHeaderVisible ()) {
		parent.header.redraw (x, 0, width, parent.getHeaderHeight (), false);
	}
}
void dispose (boolean notifyParent) {
	super.dispose ();	/* the use of super is intentional here */
	if (notifyParent) parent.removeColumn (this);
	parent = null;
}
public int getAlignment () {
	checkWidget ();
	if ((style & SWT.CENTER) != 0) return SWT.CENTER;
	if ((style & SWT.RIGHT) != 0) return SWT.RIGHT;
	return SWT.LEFT;
}
int getIndex () {
	TreeColumn[] columns = parent.getColumns ();
	for (int i = 0; i < columns.length; i++) {
		if (columns[i] == this) return i;
	}
	return -1;
}
public Tree getParent () {
	checkWidget ();
	return parent;
}
int getPreferredWidth () {
	if (!parent.getHeaderVisible ()) return 0;
	return 0;	// TODO
}
public boolean getResizable () {
	checkWidget ();
	return resizable;
}
public int getWidth () {
	checkWidget ();
	return width;
}
int getX () {
	int index = getIndex ();
	if (index == 0) return -parent.horizontalOffset;
	TreeColumn previousColumn = parent.getColumns ()[index - 1];
	return previousColumn.getX () + previousColumn.width;
}
void paint (GC gc) {
	int padding = parent.getHeaderPadding ();
	
	int x = getX ();
	int startX = x + padding;
	if ((style & SWT.LEFT) == 0) {
		int contentWidth = gc.textExtent (text, SWT.DRAW_MNEMONIC).x;
		if (image != null) {
			contentWidth += image.getBounds ().width + Tree.MARGIN_IMAGE;
		}
		if ((style & SWT.RIGHT) != 0) {
			startX = Math.max (startX, x + width - padding - contentWidth);	
		} else {	/* SWT.CENTER */
			startX = Math.max (startX, x + (width - contentWidth) / 2);	
		}
	}
	int headerHeight = parent.getHeaderHeight ();

	/* restrict the clipping region to the header cell */
	gc.setClipping (
		x + padding,
		padding,
		width - 2 * padding,
		headerHeight - 2 * padding);
	
	if (image != null) {
		Rectangle imageBounds = image.getBounds ();
		int drawHeight = Math.min (imageBounds.height, headerHeight - 2 * padding);
		gc.drawImage (
			image,
			0, 0,
			imageBounds.width, imageBounds.height,
			startX, (headerHeight - drawHeight) / 2,
			imageBounds.width, drawHeight); 
		startX += imageBounds.width + Tree.MARGIN_IMAGE; 
	}
	if (text.length () > 0) {
		int fontHeight = parent.fontHeight;
		gc.drawText (text, startX, (headerHeight - fontHeight) / 2, SWT.DRAW_MNEMONIC);
	}
}
public void pack () {
	checkWidget ();
	TreeItem[] availableItems = parent.availableItems;
	if (availableItems.length == 0) return;
	int index = getIndex ();
	int width = getPreferredWidth ();
	for (int i = 0; i < availableItems.length; i++) {
		width = Math.max (width, availableItems [i].getPreferredWidth (index));
	}
	parent.updateColumnWidth (this, width);
}
public void removeControlListener (ControlListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Move, listener);
	eventTable.unhook (SWT.Resize, listener);
}
public void removeSelectionListener (SelectionListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	removeListener (SWT.Selection, listener);
	removeListener (SWT.DefaultSelection, listener);
}
public void setAlignment (int alignment) {
	checkWidget ();
	if (getIndex () == 0) return; 	/* column 0 can only have left-alignment */
	if ((style & alignment) != 0) return;				/* same value */
	style &= ~(SWT.LEFT | SWT.CENTER | SWT.RIGHT);
	style |= alignment;
	int x = getX ();
	parent.redraw (x, 0, width, parent.getClientArea ().height, false);
	if (parent.getHeaderVisible ()) {
		parent.header.redraw (x, 0, width, parent.getHeaderHeight (), false);		
	}
}
public void setImage (Image value) {
	checkWidget ();
	if (value == image) return;
	if (value != null && value.equals (image)) return;	/* same value */
	super.setImage (value);
	
	/*
	 * If this is the first image being put into the header then the header
	 * height may be adjusted, in which case a full redraw is needed.
	 */
	if (parent.headerImageHeight == 0) {
		int oldHeaderHeight = parent.getHeaderHeight ();
		parent.setHeaderImageHeight (value.getBounds ().height);
		if (oldHeaderHeight != parent.getHeaderHeight ()) {
			parent.redrawHeader ();
			parent.redraw ();
			return;
		}
	}
	
	parent.header.redraw (getX (), 0, width, parent.getHeaderHeight (), false);
}
public void setResizable (boolean value) {
	checkWidget ();
	resizable = value;
}
public void setText (String value) {
	checkWidget ();
	if (value == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (value.equals (text)) return;						/* same value */
	super.setText (value);
	parent.header.redraw (getX (), 0, width, parent.getHeaderHeight (), false);
}
public void setWidth (int value) {
	checkWidget ();
	if (width == value) return;							/* same value */
	parent.updateColumnWidth (this, value);
}
}
