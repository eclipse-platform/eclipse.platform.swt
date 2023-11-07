/*******************************************************************************
 * Copyright (c) 2000, 2015 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.examples.accessibility;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.accessibility.ACC;
import org.eclipse.swt.accessibility.Accessible;
import org.eclipse.swt.accessibility.AccessibleAdapter;
import org.eclipse.swt.accessibility.AccessibleControlAdapter;
import org.eclipse.swt.accessibility.AccessibleControlEvent;
import org.eclipse.swt.accessibility.AccessibleEvent;
import org.eclipse.swt.accessibility.AccessibleTableCellEvent;
import org.eclipse.swt.accessibility.AccessibleTableCellListener;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.TypedListener;
import org.eclipse.swt.widgets.Widget;

/**
 * Instances of this class represent a column in a table widget.
 * <p><dl>
 * <dt><b>Styles:</b></dt>
 * <dd>LEFT, RIGHT, CENTER</dd>
 * <dt><b>Events:</b></dt>
 * <dd> Move, Resize, Selection</dd>
 * </dl>
 * </p><p>
 * Note: Only one of the styles LEFT, RIGHT and CENTER may be specified.
 * </p><p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 *
 * @see <a href="http://www.eclipse.org/swt/snippets/#table">Table, TableItem, TableColumn snippets</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 * @noextend This class is not intended to be subclassed by clients.
 */
public class CTableColumn extends Item {
	CTable parent;
	int style;
	String displayText = "";
	int width;
	boolean moveable, resizable = true;
	int sort = SWT.NONE;
	String toolTipText;
	Accessible accessible;

/**
 * Constructs a new instance of this class given its parent
 * (which must be a <code>Table</code>) and a style value
 * describing its behavior and appearance. The item is added
 * to the end of the items maintained by its parent.
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
 * @see SWT#LEFT
 * @see SWT#RIGHT
 * @see SWT#CENTER
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public CTableColumn (CTable parent, int style) {
	this (parent, style, checkNull (parent).columns.length);
}
/**
 * Constructs a new instance of this class given its parent
 * (which must be a <code>Table</code>), a style value
 * describing its behavior and appearance, and the index
 * at which to place it in the items maintained by its parent.
 * <p>
 * The style value is either one of the style constants defined in
 * class <code>SWT</code> which is applicable to instances of this
 * class, or must be built by <em>bitwise OR</em>'ing together
 * (that is, using the <code>int</code> "|" operator) two or more
 * of those <code>SWT</code> style constants. The class description
 * lists the style constants that are applicable to the class.
 * Style bits are also inherited from superclasses.
 * </p>
 * <p>
 * Note that due to a restriction on some platforms, the first column
 * is always left aligned.
 * </p>
 * @param parent a composite control which will be the parent of the new instance (cannot be null)
 * @param style the style of control to construct
 * @param index the zero-relative index to store the receiver in its parent
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
 *    <li>ERROR_INVALID_RANGE - if the index is not between 0 and the number of elements in the parent (inclusive)</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the parent</li>
 *    <li>ERROR_INVALID_SUBCLASS - if this class is not an allowed subclass</li>
 * </ul>
 *
 * @see SWT#LEFT
 * @see SWT#RIGHT
 * @see SWT#CENTER
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public CTableColumn (CTable parent, int style, int index) {
	super (parent, checkStyle (style), index);
	if (!(0 <= index && index <= parent.columns.length)) SWT.error (SWT.ERROR_INVALID_RANGE);
	this.parent = parent;
	this.style = super.getStyle ();
	parent.createItem (this, index);
}
/**
 * Adds the listener to the collection of listeners who will
 * be notified when the control is moved or resized, by sending
 * it one of the messages defined in the <code>ControlListener</code>
 * interface.
 *
 * @param listener the listener which should be notified
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see ControlListener
 * @see #removeControlListener
 */
public void addControlListener (ControlListener listener) {
	checkWidget ();
	if (listener == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.Resize, typedListener);
	addListener (SWT.Move, typedListener);
}
/**
 * Adds the listener to the collection of listeners who will
 * be notified when the control is selected by the user, by sending
 * it one of the messages defined in the <code>SelectionListener</code>
 * interface.
 * <p>
 * <code>widgetSelected</code> is called when the column header is selected.
 * <code>widgetDefaultSelected</code> is not called.
 * </p>
 *
 * @param listener the listener which should be notified when the control is selected by the user
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see SelectionListener
 * @see #removeSelectionListener
 * @see SelectionEvent
 */
public void addSelectionListener (SelectionListener listener) {
	checkWidget ();
	if (listener == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.Selection, typedListener);
	addListener (SWT.DefaultSelection, typedListener);
}
static CTable checkNull (CTable table) {
	if (table == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
	return table;
}
static int checkStyle (int style) {
	return CTable.checkBits (style, SWT.LEFT, SWT.CENTER, SWT.RIGHT, 0, 0, 0);
}
void computeDisplayText (GC gc) {
	int availableWidth = width - 2 * parent.getHeaderPadding ();
	if (super.getImage () != null) {
		availableWidth -= super.getImage ().getBounds ().width;
		availableWidth -= CTable.MARGIN_IMAGE;
	}
	if (sort != SWT.NONE) {
		availableWidth -= parent.arrowBounds.width;
		availableWidth -= CTable.MARGIN_IMAGE;
	}
	String text = super.getText ();
	int textWidth = gc.textExtent (text, SWT.DRAW_MNEMONIC).x;
	if (textWidth <= availableWidth) {
		displayText = text;
		return;
	}

	/* Ellipsis will be needed, so subtract their width from the available text width */
	int ellipsisWidth = gc.stringExtent (CTable.ELLIPSIS).x;
	availableWidth -= ellipsisWidth;
	if (availableWidth <= 0) {
		displayText = CTable.ELLIPSIS;
		return;
	}

	/* Make initial guess. */
	int index = (int) Math.min (availableWidth / gc.getFontMetrics ().getAverageCharacterWidth (), text.length ());
	textWidth = gc.textExtent (text.substring (0, index), SWT.DRAW_MNEMONIC).x;

	/* Initial guess is correct. */
	if (availableWidth == textWidth) {
		displayText = text.substring (0, index) + CTable.ELLIPSIS;
		return;
	}

	/* Initial guess is too high, so reduce until fit is found. */
	if (availableWidth < textWidth) {
		do {
			index--;
			if (index < 0) {
				displayText = CTable.ELLIPSIS;
				return;
			}
			text = text.substring (0, index);
			textWidth = gc.textExtent (text, SWT.DRAW_MNEMONIC).x;
		} while (availableWidth < textWidth);
		displayText = text + CTable.ELLIPSIS;
		return;
	}

	/* Initial guess is too low, so increase until overrun is found. */
	while (textWidth < availableWidth) {
		index++;
		textWidth = gc.textExtent (text.substring (0, index), SWT.DRAW_MNEMONIC).x;
	}
	displayText = text.substring (0, index - 1) + CTable.ELLIPSIS;
}
@Override
public void dispose () {
	if (isDisposed ()) return;
	Rectangle parentBounds = parent.clientArea;
	int x = getX ();
	int index = getIndex ();
	int orderIndex = getOrderIndex ();
	int nextColumnAlignment = parent.columns.length > 1 ? parent.columns [1].getAlignment () : SWT.LEFT;
	CTable parent = this.parent;
	dispose (true);

	int width = parentBounds.width - x;
	parent.redraw (x, 0, width, parentBounds.height, false);
	/*
	 * If column 0 was disposed then the new column 0 must be redrawn if it appears to the
	 * left of the disposed column in the column order AND one the following are true:
	 * - the parent has style CHECK, since these will now appear in the new column 0
	 * - the new column 0 had non-left alignment before the dispose, since the parent will have
	 * 	changed this to LEFT in the call to dispose(true)
	 */
	if (index == 0 && ((parent.getStyle () & SWT.CHECK) != 0 || nextColumnAlignment != SWT.LEFT)) {
		if (parent.columns.length > 0) {
			CTableColumn newColumn0 = parent.columns [0];
			if (newColumn0.getOrderIndex () < orderIndex) {
				int newColumn0x = newColumn0.getX ();
				parent.redraw (newColumn0x, 0, newColumn0.width, parentBounds.height, false);
				/* if the alignment changed then the header text must be repainted with its new alignment */
				if (nextColumnAlignment != SWT.LEFT && parent.getHeaderVisible () && parent.drawCount <= 0) {
					parent.header.redraw (newColumn0x, 0, newColumn0.width, parent.header.getClientArea ().height, false);
				}
			}
		}
	}
	if (parent.drawCount <= 0 && parent.getHeaderVisible ()) {
		parent.header.redraw (x, 0, width, parent.getHeaderHeight (), false);
	}
}
void dispose (boolean notifyParent) {
	super.dispose ();	/* super is intentional here */
	if (notifyParent) parent.destroyItem (this);
	if (accessible != null) {
		accessible.dispose();
		accessible = null;
	}
	parent = null;
}
/**
 * Returns a value which describes the position of the
 * text or image in the receiver. The value will be one of
 * <code>LEFT</code>, <code>RIGHT</code> or <code>CENTER</code>.
 *
 * @return the alignment
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getAlignment () {
	checkWidget ();
	if ((getStyle () & SWT.CENTER) != 0) return SWT.CENTER;
	if ((getStyle () & SWT.RIGHT) != 0) return SWT.RIGHT;
	return SWT.LEFT;
}
/*
 * Returns the width of the header's content
 * (image + text + sort arrow + internal margins)
 */
int getContentWidth (GC gc, boolean useDisplayText) {
	int contentWidth = 0;
	String text = useDisplayText ? displayText : super.getText ();
	if (text.length () > 0) {
		contentWidth += gc.textExtent (text, SWT.DRAW_MNEMONIC).x;
	}
	if (super.getImage () != null) {
		contentWidth += super.getImage ().getBounds ().width;
		if (text.length () > 0) contentWidth += CTable.MARGIN_IMAGE;
	}
	if (sort != SWT.NONE) {
		contentWidth += parent.arrowBounds.width;
		if (text.length () > 0 || super.getImage () != null) {
			contentWidth += CTable.MARGIN_IMAGE;
		}
	}
	return contentWidth;
}
int getIndex () {
	CTableColumn[] columns = parent.columns;
	for (int i = 0; i < columns.length; i++) {
		if (columns [i] == this) return i;
	}
	return -1;
}
/**
 * Gets the moveable attribute. A column that is
 * not moveable cannot be reordered by the user
 * by dragging the header but may be reordered
 * by the programmer.
 *
 * @return the moveable attribute
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see CTable#getColumnOrder()
 * @see CTable#setColumnOrder(int[])
 * @see CTableColumn#setMoveable(boolean)
 * @see SWT#Move
 *
 * @since 3.1
 */
public boolean getMoveable () {
	checkWidget ();
	return moveable;
}
int getOrderIndex () {
	CTableColumn[] orderedColumns = parent.orderedColumns;
	if (orderedColumns == null) return getIndex ();
	for (int i = 0; i < orderedColumns.length; i++) {
		if (orderedColumns [i] == this) return i;
	}
	return -1;
}
/**
 * Returns the receiver's parent, which must be a <code>Table</code>.
 *
 * @return the receiver's parent
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public CTable getParent () {
	checkWidget ();
	return parent;
}
int getPreferredWidth () {
	if (!parent.getHeaderVisible ()) return 0;
	GC gc = new GC (parent);
	int result = getContentWidth (gc, false);
	gc.dispose ();
	return result + 2 * parent.getHeaderPadding ();
}
/**
 * Gets the resizable attribute. A column that is
 * not resizable cannot be dragged by the user but
 * may be resized by the programmer.
 *
 * @return the resizable attribute
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public boolean getResizable () {
	checkWidget ();
	return resizable;
}
/**
 * Returns the receiver's tool tip text, or null if it has
 * not been set.
 *
 * @return the receiver's tool tip text
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.2
 */
public String getToolTipText () {
	checkWidget ();
	return toolTipText;
}
/**
 * Gets the width of the receiver.
 *
 * @return the width
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getWidth () {
	checkWidget ();
	return width;
}
int getX () {
	CTableColumn[] orderedColumns = parent.getOrderedColumns ();
	int index = getOrderIndex ();
	int result = -parent.horizontalOffset;
	for (int i = 0; i < index; i++) {
		result += orderedColumns [i].width;
	}
	return result;
}
/**
 * Causes the receiver to be resized to its preferred size.
 * For a composite, this involves computing the preferred size
 * from its layout, if there is one.
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void pack () {
	checkWidget ();
	CTableItem[] items = parent.items;
	int index = getIndex ();
	int newWidth = getPreferredWidth ();
	for (int i = 0; i < parent.itemsCount; i++) {
		int width = items [i].getPreferredWidth (index);
		/* ensure that receiver and parent were not disposed in a callback */
		if (parent.isDisposed () || isDisposed ()) return;
		if (!items [i].isDisposed ()) {
			newWidth = Math.max (newWidth, width);
		}
	}
	if (newWidth != width) parent.updateColumnWidth (this, newWidth);
}
void paint (GC gc) {
	int padding = parent.getHeaderPadding ();

	int x = getX ();
	int startX = x + padding;
	if ((getStyle () & SWT.LEFT) == 0) {
		int contentWidth = getContentWidth (gc, true);
		if ((getStyle () & SWT.RIGHT) != 0) {
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

	if (super.getImage () != null) {
		Rectangle imageBounds = super.getImage ().getBounds ();
		int drawHeight = Math.min (imageBounds.height, headerHeight - 2 * padding);
		gc.drawImage (
			super.getImage (),
			0, 0,
			imageBounds.width, imageBounds.height,
			startX, (headerHeight - drawHeight) / 2,
			imageBounds.width, drawHeight);
		startX += imageBounds.width + CTable.MARGIN_IMAGE;
	}
	if (displayText.length () > 0) {
		gc.setForeground (getParent ().display.getSystemColor (SWT.COLOR_BLACK));
		int fontHeight = parent.fontHeight;
		gc.drawText (displayText, startX, (headerHeight - fontHeight) / 2, SWT.DRAW_MNEMONIC);
		startX += gc.textExtent (displayText, SWT.DRAW_MNEMONIC).x + CTable.MARGIN_IMAGE;
	}
	if (sort != SWT.NONE) {
		Image image = sort == SWT.DOWN ? parent.getArrowDownImage () : parent.getArrowUpImage ();
		int y = (headerHeight - parent.arrowBounds.height) / 2;
		gc.drawImage (image, startX, y);
	}
}
/**
 * Removes the listener from the collection of listeners who will
 * be notified when the control is moved or resized.
 *
 * @param listener the listener which should no longer be notified
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see ControlListener
 * @see #addControlListener
 */
public void removeControlListener (ControlListener listener) {
	checkWidget ();
	if (listener == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
	removeListener (SWT.Move, listener);
	removeListener (SWT.Resize, listener);
}
/**
 * Removes the listener from the collection of listeners who will
 * be notified when the control is selected by the user.
 *
 * @param listener the listener which should no longer be notified
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see SelectionListener
 * @see #addSelectionListener
 */
public void removeSelectionListener (SelectionListener listener) {
	checkWidget ();
	if (listener == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
	removeListener (SWT.Selection, listener);
	removeListener (SWT.DefaultSelection, listener);
}
/**
 * Controls how text and images will be displayed in the receiver.
 * The argument should be one of <code>LEFT</code>, <code>RIGHT</code>
 * or <code>CENTER</code>.
 * <p>
 * Note that due to a restriction on some platforms, the first column
 * is always left aligned.
 * </p>
 * @param alignment the new alignment
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setAlignment (int alignment) {
	checkWidget ();
	if ((alignment & (SWT.LEFT | SWT.RIGHT | SWT.CENTER)) == 0) return;
	int index = getIndex ();
	if (index == -1 || index == 0) return;	/* column 0 can only have left-alignment */
	alignment = CTable.checkBits (alignment, SWT.LEFT, SWT.CENTER, SWT.RIGHT, 0, 0, 0);
	int style = getStyle ();
	if ((style & alignment) != 0) return;	/* same value */
	style &= ~(SWT.LEFT | SWT.CENTER | SWT.RIGHT);
	style |= alignment;
	setStyle (style);
	int x = getX ();
	parent.redraw (x, 0, width, parent.clientArea.height, false);
	if (parent.drawCount <= 0 && parent.getHeaderVisible ()) {
		/* don't damage the header's drawn borders */
		parent.header.redraw (x, 1, width - 2, parent.getHeaderHeight () - 3, false);
	}
}
@Override
public int getStyle () {
	int s = super.getStyle ();
	return style | s;
}
void setStyle (int newStyle) {
	int s = super.getStyle ();
	style |= s | newStyle;
}
@Override
public void setImage (Image value) {
	checkWidget ();
	if (value == super.getImage ()) return;
	if (value != null && value.equals (super.getImage ())) return;	/* same value */
	super.setImage (value);

	/* An image width change may affect the space available for the column's displayText. */
	GC gc = new GC (parent);
	computeDisplayText (gc);
	gc.dispose ();

	/*
	 * If this is the first image being put into the header then the header
	 * height may be adjusted, in which case a full redraw is needed.
	 */
	if (parent.headerImageHeight == 0) {
		int oldHeaderHeight = parent.getHeaderHeight ();
		parent.setHeaderImageHeight (value.getBounds ().height);
		if (oldHeaderHeight != parent.getHeaderHeight ()) {
			/* parent header height changed */
			if (parent.drawCount <= 0 && parent.getHeaderVisible ()) {
				parent.header.redraw ();
			}
			parent.redraw ();
			return;
		}
	}

	if (parent.drawCount <= 0 && parent.getHeaderVisible ()) {
		/* don't damage the header's drawn borders */
		parent.header.redraw (getX (), 1, width - 2, parent.getHeaderHeight () - 3, false);
	}
}
/**
 * Sets the moveable attribute.  A column that is
 * moveable can be reordered by the user by dragging
 * the header. A column that is not moveable cannot be
 * dragged by the user but may be reordered
 * by the programmer.
 *
 * @param moveable the moveable attribute
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see CTable#setColumnOrder(int[])
 * @see CTable#getColumnOrder()
 * @see CTableColumn#getMoveable()
 * @see SWT#Move
 *
 * @since 3.1
 */
public void setMoveable (boolean moveable) {
	checkWidget ();
	this.moveable = moveable;
}
/**
 * Sets the resizable attribute.  A column that is
 * resizable can be resized by the user dragging the
 * edge of the header.  A column that is not resizable
 * cannot be dragged by the user but may be resized
 * by the programmer.
 *
 * @param resizable the resize attribute
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setResizable (boolean value) {
	checkWidget ();
	resizable = value;
}
void setSortDirection (int value) {
	if (value == sort) return;
	boolean widthChange = value == SWT.NONE || sort == SWT.NONE;
	sort = value;
	if (widthChange) {
		/*
		 * adding/removing the sort arrow decreases/increases the width that is
		 * available for the column's header text, so recompute the display text
		 */
		GC gc = new GC (parent);
		computeDisplayText (gc);
		gc.dispose ();
	}
	if (parent.drawCount <= 0 && parent.getHeaderVisible ()) {
		/* don't damage the header's drawn borders */
		parent.header.redraw (getX (), 1, width - 2, parent.getHeaderHeight () - 3, false);
	}
}
@Override
public void setText (String value) {
	checkWidget ();
	if (value == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
	if (value.equals (super.getText ())) return;					/* same value */
	super.setText (value);
	GC gc = new GC (parent);
	computeDisplayText (gc);
	gc.dispose ();
	if (parent.drawCount <= 0 && parent.getHeaderVisible ()) {
		/* don't damage the header's drawn borders */
		parent.header.redraw (getX (), 1, width - 2, parent.getHeaderHeight () - 3, false);
	}
}
/**
 * Sets the receiver's tool tip text to the argument, which
 * may be null indicating that the default tool tip for the
 * control will be shown. For a control that has a default
 * tool tip, such as the Tree control on Windows, setting
 * the tool tip text to an empty string replaces the default,
 * causing no tool tip text to be shown.
 * <p>
 * The mnemonic indicator (character '&amp;') is not displayed in a tool tip.
 * To display a single '&amp;' in the tool tip, the character '&amp;' can be
 * escaped by doubling it in the string.
 * </p>
 *
 * @param string the new tool tip text (or null)
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.2
 */
public void setToolTipText (String string) {
	checkWidget ();
	if (toolTipText == string) return;
	if (toolTipText != null && toolTipText.equals (string)) return;
	toolTipText = string;
	if (parent.toolTipShell == null) return; /* tooltip not currently showing */
	if (((Integer) parent.toolTipShell.getData ()).intValue () != getIndex ()) return;	/* tooltip showing for different column */
	parent.headerUpdateToolTip (getX () + (width / 2));	/* update the tooltip text */
}
/**
 * Sets the width of the receiver.
 *
 * @param width the new width
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setWidth (int value) {
	checkWidget ();
	if (value < 0) return;
	if (width == value) return;							/* same value */
	parent.updateColumnWidth (this, value);
}
void updateFont (GC gc) {
	computeDisplayText (gc);
}
/*
 * Perform any internal changes necessary to reflect a changed width.
 */
void updateWidth (GC gc) {
	String oldDisplayText = displayText;
	computeDisplayText (gc);
	/* the header must be damaged if the display text has changed or if the alignment is not LEFT */
	if (parent.getHeaderVisible ()) {
		if ((getStyle () & SWT.LEFT) == 0 || !oldDisplayText.equals (displayText)) {
			int padding = parent.getHeaderPadding ();
			parent.header.redraw (getX () + padding, 0, width - padding, parent.getHeaderHeight (), false);
		}
	}
}
/* Returns the accessible for the column header. */
Accessible getAccessible(final Accessible accessibleTable) {
	if (accessible == null) {
		accessible = new Accessible(accessibleTable);
		accessible.addAccessibleListener(new AccessibleAdapter() {
			@Override
			public void getName(AccessibleEvent e) {
				e.result = getText();
				System.out.println("tableColumn getName = " + e.result);
			}
		});
		accessible.addAccessibleControlListener(new AccessibleControlAdapter() {
			@Override
			public void getChildAtPoint(AccessibleControlEvent e) {
				Point point = parent.toControl(e.x, e.y);
				int x = getX();
				if (x <= point.x && point.x <= x + getWidth()) {
					e.childID = ACC.CHILDID_SELF;
				} else {
					e.childID = ACC.CHILDID_NONE;
				}
			}
			@Override
			public void getChildCount(AccessibleControlEvent e) {
				e.detail = 0;
			}
			@Override
			public void getLocation(AccessibleControlEvent e) {
				Rectangle rect = parent.header.getBounds();
				rect.x = getX();
				Point pt = parent.toDisplay(rect.x, rect.y);
				e.x = pt.x;
				e.y = pt.y;
				e.width = width;
				e.height = rect.height;
			}
			@Override
			public void getRole(AccessibleControlEvent e) {
				e.detail = ACC.ROLE_TABLECELL;
			}
		});
		accessible.addAccessibleTableCellListener(new AccessibleTableCellListener() {
			@Override
			public void getColumnHeaders(AccessibleTableCellEvent e) {
				/* Column header does not have a header. */
			}
			@Override
			public void getColumnIndex(AccessibleTableCellEvent e) {
				e.index = getIndex();
			}
			@Override
			public void getColumnSpan(AccessibleTableCellEvent e) {
				e.count = 1; /* CTable cells only occupy one column. */
			}
			@Override
			public void getRowHeaders(AccessibleTableCellEvent e) {
				/* CTable does not support row headers. */
			}
			@Override
			public void getRowIndex(AccessibleTableCellEvent e) {
				e.index = 0;
			}
			@Override
			public void getRowSpan(AccessibleTableCellEvent e) {
				e.count = 1; /* CTable cells only occupy one row. */
			}
			@Override
			public void getTable(AccessibleTableCellEvent e) {
				e.accessible = accessibleTable;
			}
			@Override
			public void isSelected(AccessibleTableCellEvent e) {
				e.isSelected = false;  /* CTable columns cannot be selected (only rows can be selected). */
			}
		});
	}
	return accessible;
}
}
