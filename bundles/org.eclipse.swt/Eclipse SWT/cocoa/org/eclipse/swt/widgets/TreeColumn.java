/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.widgets;


import org.eclipse.swt.internal.cocoa.*;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.events.*;

/**
 * Instances of this class represent a column in a tree widget.
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
 * @see <a href="http://www.eclipse.org/swt/snippets/#tree">Tree, TreeItem, TreeColumn snippets</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 * 
 * @since 3.1
 */
public class TreeColumn extends Item {
	NSTableColumn nsColumn;
	Tree parent;
	String toolTipText, displayText;

	static final int MARGIN = 2;

/**
 * Constructs a new instance of this class given its parent
 * (which must be a <code>Tree</code>) and a style value
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
public TreeColumn (Tree parent, int style) {
	super (parent, checkStyle (style));
	this.parent = parent;
	parent.createItem (this, parent.columnCount);
}

/**
 * Constructs a new instance of this class given its parent
 * (which must be a <code>Tree</code>), a style value
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
public TreeColumn (Tree parent, int style, int index) {
	super (parent, checkStyle (style));
	this.parent = parent;
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
public void addControlListener(ControlListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.Resize,typedListener);
	addListener (SWT.Move,typedListener);
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
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.Selection,typedListener);
	addListener (SWT.DefaultSelection,typedListener);
}

static int checkStyle (int style) {
	return checkBits (style, SWT.LEFT, SWT.CENTER, SWT.RIGHT, 0, 0, 0);
}

protected void checkSubclass () {
	if (!isValidSubclass ()) error (SWT.ERROR_INVALID_SUBCLASS);
}

void deregister () {
	super.deregister ();
	display.removeWidget (nsColumn.headerCell());
}

void destroyWidget () {
	parent.destroyItem (this);
	releaseHandle ();
}

void drawInteriorWithFrame_inView (int /*long*/ id, int /*long*/ sel, int /*long*/ cellFrame, int /*long*/ view) {
	NSRect cellRect = new NSRect ();
	OS.memmove (cellRect, cellFrame, NSRect.sizeof);

	/*
	 * Feature in Cocoa.  When the last column in a tree does not reach the
	 * rightmost edge of the tree view, the cell that draws the rightmost-
	 * column's header is also invoked to draw the header space between its
	 * right edge and the tree's right edge.  If this case is detected then
	 * nothing should be drawn.
	 */
	NSOutlineView outlineView = (NSOutlineView)parent.view;
	int columnIndex = (int)/*64*/outlineView.columnWithIdentifier (nsColumn);
	NSRect headerRect = parent.headerView.headerRectOfColumn (columnIndex);
	if (headerRect.x != cellRect.x || headerRect.width != cellRect.width) return;

	NSGraphicsContext context = NSGraphicsContext.currentContext ();
	context.saveGraphicsState ();

	int contentWidth = 0;
	NSSize stringSize = null, imageSize = null;
	NSAttributedString attrString = null;
	NSTableHeaderCell headerCell = nsColumn.headerCell ();
	if (displayText != null) {
		NSMutableDictionary dict = NSMutableDictionary.dictionaryWithCapacity(4);
		dict.setObject (headerCell.font (), OS.NSFontAttributeName);
		NSMutableParagraphStyle paragraphStyle = (NSMutableParagraphStyle)new NSMutableParagraphStyle ().alloc ().init ();
		paragraphStyle.autorelease ();
		paragraphStyle.setLineBreakMode (OS.NSLineBreakByClipping);
		dict.setObject (paragraphStyle, OS.NSParagraphStyleAttributeName);
		if ((parent.state & DISABLED) != 0) {
			dict.setObject (NSColor.disabledControlTextColor (), OS.NSForegroundColorAttributeName);
		}
		NSString string = NSString.stringWith (displayText);
		attrString = ((NSAttributedString)new NSAttributedString ().alloc ()).initWithString (string, dict);
		stringSize = attrString.size ();
		contentWidth += Math.ceil (stringSize.width);
		if (image != null) contentWidth += MARGIN; /* space between image and text */
	}
	if (image != null) {
		imageSize = image.handle.size ();
		contentWidth += Math.ceil (imageSize.width);
	}

	if (parent.sortColumn == this && parent.sortDirection != SWT.NONE) {
		boolean ascending = parent.sortDirection == SWT.UP;
		headerCell.drawSortIndicatorWithFrame (cellRect, new NSView(view), ascending, 0);
		/* remove the arrow's space from the available drawing width */
		NSRect sortRect = headerCell.sortIndicatorRectForBounds (cellRect);
		cellRect.width = Math.max (0, sortRect.x - cellRect.x);
	}

	int drawX = 0;
	if ((style & SWT.CENTER) != 0) {
		drawX = (int)(cellRect.x + Math.max (MARGIN, ((cellRect.width - contentWidth) / 2)));
	} else if ((style & SWT.RIGHT) != 0) {
		drawX = (int)(cellRect.x + Math.max (MARGIN, cellRect.width - contentWidth - MARGIN));
	} else {
		drawX = (int)cellRect.x + MARGIN;
	}

	if (image != null) {
		NSRect destRect = new NSRect ();
		destRect.x = drawX;
		destRect.y = cellRect.y;
		destRect.width = Math.min (imageSize.width, cellRect.width - 2 * MARGIN);
		destRect.height = Math.min (imageSize.height, cellRect.height);
		boolean isFlipped = new NSView (view).isFlipped(); 
		if (isFlipped) {
			context.saveGraphicsState ();
			NSAffineTransform transform = NSAffineTransform.transform ();
		 	transform.scaleXBy (1, -1);
		 	transform.translateXBy (0, -(destRect.height + 2 * destRect.y));
		 	transform.concat ();
		}
		NSRect sourceRect = new NSRect ();
		sourceRect.width = destRect.width;
		sourceRect.height = destRect.height;
		image.handle.drawInRect (destRect, sourceRect, OS.NSCompositeSourceOver, 1f);
		if (isFlipped) context.restoreGraphicsState ();
		drawX += destRect.width;
	}

	if (displayText != null && displayText.length () > 0) {
		if (image != null) drawX += MARGIN; /* space between image and text */
		NSRect destRect = new NSRect ();
		destRect.x = drawX;
		destRect.y = cellRect.y;
		destRect.width = Math.min (stringSize.width, cellRect.x + cellRect.width - MARGIN - drawX);
		destRect.height = Math.min (stringSize.height, cellRect.height);
		attrString.drawInRect (destRect);
		attrString.release ();
	}

	context.restoreGraphicsState ();
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
	if ((style & SWT.LEFT) != 0) return SWT.LEFT;
	if ((style & SWT.CENTER) != 0) return SWT.CENTER;
	if ((style & SWT.RIGHT) != 0) return SWT.RIGHT;
	return SWT.LEFT;
}

String getNameText () {
	return getText ();
}

/**
 * Returns the receiver's parent, which must be a <code>Tree</code>.
 *
 * @return the receiver's parent
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Tree getParent () {
	checkWidget ();
	return parent;
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
 * @see Tree#getColumnOrder()
 * @see Tree#setColumnOrder(int[])
 * @see TreeColumn#setMoveable(boolean)
 * @see SWT#Move
 * 
 * @since 3.2
 */
public boolean getMoveable () {
	checkWidget ();
	return ((NSTableView)parent.view).allowsColumnReordering();
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
	return nsColumn.resizingMask() != OS.NSTableColumnNoResizing;
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
	return (int)nsColumn.width();
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
 *
 */
public void pack () {
	checkWidget ();

	int width = 0;

	/* compute header width */
	if (displayText != null) {
		NSMutableDictionary dict = NSMutableDictionary.dictionaryWithCapacity (4);
		NSTableHeaderCell headerCell = nsColumn.headerCell ();
		dict.setObject (headerCell.font (), OS.NSFontAttributeName);
		NSString string = NSString.stringWith (displayText);
		NSAttributedString attrString = ((NSAttributedString)new NSAttributedString ().alloc ()).initWithString (string, dict);
		NSSize stringSize = attrString.size ();
		attrString.release ();
		width += Math.ceil (stringSize.width);
		if (image != null) width += MARGIN; /* space between image and text */
	}
	if (image != null) {
		NSSize imageSize = image.handle.size ();
		width += Math.ceil (imageSize.width);
	}
	if (parent.sortColumn == this && parent.sortDirection != SWT.NONE) {
		NSTableHeaderCell headerCell = nsColumn.headerCell ();
		NSRect rect = new NSRect ();
		rect.width = rect.height = Float.MAX_VALUE;
		NSSize cellSize = headerCell.cellSizeForBounds (rect);
		rect.height = cellSize.height;
		NSRect sortRect = headerCell.sortIndicatorRectForBounds (rect);
		width += Math.ceil (sortRect.width);
	}

	/* compute item widths down column */
	GC gc = new GC (parent);
	width = Math.max(width, parent.calculateWidth(parent.items, parent.indexOf (this), gc, true));
	gc.dispose ();
	setWidth (width);
}

void releaseHandle () {
	super.releaseHandle ();
	if (nsColumn != null) {
		nsColumn.headerCell ().release ();
		nsColumn.release ();
	}
	nsColumn = null;
	parent = null;
}

void releaseWidget () {
	super.releaseWidget ();
	if (parent.sortColumn == this) {
		parent.sortColumn = null;
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
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Move, listener);
	eventTable.unhook (SWT.Resize, listener);
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
public void removeSelectionListener(SelectionListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Selection, listener);
	eventTable.unhook (SWT.DefaultSelection,listener);	
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
	int index = parent.indexOf (this);
	if (index == -1 || index == 0) return;
	style &= ~(SWT.LEFT | SWT.RIGHT | SWT.CENTER);
	style |= alignment & (SWT.LEFT | SWT.RIGHT | SWT.CENTER);
	NSOutlineView outlineView = ((NSOutlineView) parent.view);
	NSTableHeaderView headerView = outlineView.headerView ();
	if (headerView == null) return;
	index = (int)/*64*/outlineView.columnWithIdentifier (nsColumn);
	NSRect rect = headerView.headerRectOfColumn (index);
	headerView.setNeedsDisplayInRect (rect);
	rect = outlineView.rectOfColumn (index);
	parent.view.setNeedsDisplayInRect (rect);
}

public void setImage (Image image) {
	checkWidget();
	if (image != null && image.isDisposed ()) {
		error (SWT.ERROR_INVALID_ARGUMENT);
	}
	super.setImage (image);
	NSTableHeaderView headerView = ((NSOutlineView) parent.view).headerView ();
	if (headerView == null) return;
	int /*long*/ index = ((NSOutlineView)parent.view).columnWithIdentifier (nsColumn);
	NSRect rect = headerView.headerRectOfColumn (index);
	headerView.setNeedsDisplayInRect (rect);
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
 * @see Tree#setColumnOrder(int[])
 * @see Tree#getColumnOrder()
 * @see TreeColumn#getMoveable()
 * @see SWT#Move
 * 
 * @since 3.2
 */
public void setMoveable (boolean moveable) {
	checkWidget ();
	// TODO how to make only some columns movable?  And handle moveable == false.
	if (moveable) {
		((NSOutlineView)parent.view).setAllowsColumnReordering (true);
	}
}

/**
 * Sets the resizable attribute.  A column that is
 * not resizable cannot be dragged by the user but
 * may be resized by the programmer.
 *
 * @param resizable the resize attribute
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setResizable (boolean resizable) {
	checkWidget ();
	nsColumn.setResizingMask(resizable ? OS.NSTableColumnUserResizingMask : OS.NSTableColumnNoResizing);
}

public void setText (String string) {
	checkWidget ();
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	super.setText (string);
	char [] buffer = new char [text.length ()];
	text.getChars (0, buffer.length, buffer, 0);
	int length = fixMnemonic (buffer);
	displayText = new String (buffer, 0, length);
	NSString title = NSString.stringWith (displayText);
	nsColumn.headerCell ().setTitle (title);
	NSTableHeaderView headerView = ((NSOutlineView) parent.view).headerView ();
	if (headerView == null) return;
	int /*long*/ index = ((NSOutlineView)parent.view).columnWithIdentifier (nsColumn);
	NSRect rect = headerView.headerRectOfColumn (index);
	headerView.setNeedsDisplayInRect (rect);
}

/**
 * Sets the receiver's tool tip text to the argument, which
 * may be null indicating that no tool tip text should be shown.
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
	checkWidget();
	toolTipText = string;
	parent.checkToolTip (this);
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
public void setWidth (int width) {
	checkWidget ();
	if (width < 0) return;
	nsColumn.setWidth (width);
}

String tooltipText () {
	return toolTipText;
}
}
