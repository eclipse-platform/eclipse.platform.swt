/*******************************************************************************
 * Copyright (c) 2000, 2012 IBM Corporation and others.
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
package org.eclipse.swt.widgets;


import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.cocoa.*;

/**
 * Instances of this class represent a column in a table widget.
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>LEFT, RIGHT, CENTER</dd>
 * <dt><b>Events:</b></dt>
 * <dd> Move, Resize, Selection</dd>
 * </dl>
 * <p>
 * Note: Only one of the styles LEFT, RIGHT and CENTER may be specified.
 * </p><p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 *
 * @see <a href="http://www.eclipse.org/swt/snippets/#table">Table, TableItem, TableColumn snippets</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 * @noextend This class is not intended to be subclassed by clients.
 */
public class TableColumn extends Item {
	Table parent;
	NSTableColumn nsColumn;
	String toolTipText, displayText;
	boolean movable;

	static final int MARGIN = 2;

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
public TableColumn (Table parent, int style) {
	super (parent, checkStyle (style));
	this.parent = parent;
	parent.createItem (this, parent.columnCount);
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
public TableColumn (Table parent, int style, int index) {
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

@Override
protected void checkSubclass () {
	if (!isValidSubclass ()) error (SWT.ERROR_INVALID_SUBCLASS);
}

@Override
void deregister () {
	super.deregister ();
	display.removeWidget (nsColumn.headerCell());
}

@Override
void destroyWidget () {
	parent.destroyItem (this);
	releaseHandle ();
}

@Override
void drawInteriorWithFrame_inView (long id, long sel, NSRect cellRect, long view) {
	/*
	 * Feature in Cocoa.  When the last column in a table does not reach the
	 * rightmost edge of the table view, the cell that draws the rightmost-
	 * column's header is also invoked to draw the header space between its
	 * right edge and the table's right edge.  If this case is detected, then
	 * only the headerBackground should be drawn.
	 */
	int columnIndex = parent.indexOf (nsColumn);
	NSRect headerRect = parent.headerView.headerRectOfColumn (columnIndex);
	double borderWidth = parent.hasBorder() ? 0.5 : 0;
	if (headerRect.x != cellRect.x || headerRect.width != cellRect.width) {
		if (parent.headerBackground != null) {
			NSGraphicsContext context = NSGraphicsContext.currentContext ();
			context.saveGraphicsState ();
			double /* float */ [] colorRGB = parent.headerBackground;
			NSColor color = NSColor.colorWithDeviceRed(colorRGB[0], colorRGB[1], colorRGB[2], 1f);
			color.setFill();
			/*
			 * Before this method is invoked, the header cell's border is already drawn with the system
			 * default color. Use headerRect's height & y values so that we can draw over
			 * the header cell's borders with the header background color.
			 * Don't draw over the top border when the border style is set. Also, adjust rect height
			 * so that separator line between header & first row is drawn.
			 */
			NSRect rect = new NSRect();
			rect.x = cellRect.x;
			rect.y = headerRect.y + borderWidth;
			rect.width = cellRect.width;
			rect.height = headerRect.height - borderWidth - 0.5; /* 0.5 -> header-row separator width */
			NSBezierPath.fillRect(rect);
			context.restoreGraphicsState();
		}
		return;
	}

	NSGraphicsContext context = NSGraphicsContext.currentContext ();
	context.saveGraphicsState ();

	int contentWidth = 0;
	NSSize stringSize = null, imageSize = null;
	NSAttributedString attrString = null;
	NSTableHeaderCell headerCell = nsColumn.headerCell ();
	if (displayText != null) {
		Font font = Font.cocoa_new(display, headerCell.font ());
		attrString = parent.createString(displayText, font, parent.getHeaderForegroundColor().handle, SWT.LEFT, false, (parent.state & DISABLED) == 0, false);
		stringSize = attrString.size ();
		contentWidth += Math.ceil (stringSize.width);
		if (image != null) contentWidth += MARGIN; /* space between image and text */
	}

	if (parent.headerBackground != null) {
		// fill header background
		context.saveGraphicsState();
		double [] colorRGB = parent.headerBackground;
		NSColor color1 = NSColor.colorWithDeviceRed(colorRGB[0], colorRGB[1], colorRGB[2], 1f);
		color1.setFill();
		/*
		 * Before this method is invoked, the header cell's border is already drawn
		 * with the system default color. Use headerRect instead of cellRect so that we
		 * can draw over the header cell's borders with the header background color.
		 * Don't draw over the top border when the border style is set. Also, adjust rect
		 * height so that separator line between header & first row is drawn.
		 */
		NSRect rect = new NSRect();
		rect.x = headerRect.x;
		rect.y = headerRect.y + borderWidth;
		rect.width = headerRect.width;
		rect.height = headerRect.height - borderWidth - 0.5; /* 0.5 -> header-row separator width */
		NSBezierPath.fillRect(rect);

		// draw header column separator
		if (parent.headerForeground != null) {
			colorRGB = parent.headerForeground;
			NSColor color = NSColor.colorWithDeviceRed(colorRGB[0], colorRGB[1], colorRGB[2], 0.6f);
			color.setStroke();
		}
		NSBezierPath path = NSBezierPath.bezierPath();
		NSPoint pt = new NSPoint();
		pt.x = cellRect.x + cellRect.width - 0.5;
		pt.y = cellRect.y + 2;
		path.moveToPoint(pt);
		pt.y += cellRect.y + cellRect.height - 4;
		path.lineToPoint(pt);
		path.stroke();
		context.restoreGraphicsState();
	}

	if (image != null) {
		imageSize = image.handle.size ();
		contentWidth += Math.ceil (imageSize.width);
	}

	if (parent.sortColumn == this && parent.sortDirection != SWT.NONE) {
		boolean ascending = parent.sortDirection == SWT.UP;
		if (parent.headerBackground != null || parent.headerForeground != null) {
			NSRect sortIndicatorRect = headerCell.sortIndicatorRectForBounds(cellRect);
			context.saveGraphicsState();
			if (parent.headerForeground != null) {
				double [] colorRGB = parent.headerForeground;
				NSColor color = NSColor.colorWithDeviceRed(colorRGB[0], colorRGB[1], colorRGB[2], 0.9f);
				color.setStroke();
			}
			NSBezierPath path = NSBezierPath.bezierPath();
			path.setLineWidth(1.5);
			NSPoint pt = new NSPoint();
			final double y1 = sortIndicatorRect.y + sortIndicatorRect.height / 2 - 1.5;
			final double y2 = sortIndicatorRect.y + sortIndicatorRect.height / 2 + 1.5;
			final double x1 = sortIndicatorRect.x;
			final double x2 = sortIndicatorRect.x + 3;
			final double x3 = sortIndicatorRect.x + 6;
			if (ascending) {
				pt.x = x1;
				pt.y = y2;
				path.moveToPoint(pt);
				pt.x = x2;
				pt.y = y1;
				path.lineToPoint(pt);
				pt.x = x3;
				pt.y = y2;
				path.lineToPoint(pt);
			} else {
				pt.x = x1;
				pt.y = y1;
				path.moveToPoint(pt);
				pt.x = x2;
				pt.y = y2;
				path.lineToPoint(pt);
				pt.x = x3;
				pt.y = y1;
				path.lineToPoint(pt);
			}
			path.stroke();
			context.restoreGraphicsState();
		} else {
			headerCell.drawSortIndicatorWithFrame (cellRect, new NSView(view), ascending, 0);
		}
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
		destRect.y = Math.max(cellRect.y, cellRect.y + (cellRect.height - imageSize.height)/2);
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
	}
	if (attrString != null) attrString.release ();

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

@Override
String getNameText () {
	return getText ();
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
public Table getParent () {
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
 * @see Table#getColumnOrder()
 * @see Table#setColumnOrder(int[])
 * @see TableColumn#setMoveable(boolean)
 * @see SWT#Move
 *
 * @since 3.1
 */
public boolean getMoveable () {
	checkWidget ();
	return movable;
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
	int width = (int)nsColumn.width();
	// TODO how to differentiate 0 and 1 cases?
	if (width > 0) width += Table.CELL_GAP;
	return width;
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

	int width = 0;

	/* compute header width */
	NSTableHeaderCell headerCell = nsColumn.headerCell ();
	NSSize size = headerCell.cellSize();
	width += Math.ceil (size.width);
	if (image != null) {
		NSSize imageSize = image.handle.size ();
		width += Math.ceil (imageSize.width) + MARGIN;
	}
	if (parent.sortColumn == this && parent.sortDirection != SWT.NONE) {
		NSRect sortRect = headerCell.sortIndicatorRectForBounds (new NSRect ());
		width += Math.ceil (sortRect.width + 2 * MARGIN);
	}

	/* compute item widths down column */
	GC gc = new GC (parent);
	int index = parent.indexOf (this);
	width = Math.max (width, parent.calculateWidth (parent.items, index, gc));
	gc.dispose ();
	setWidth (width);
}

@Override
void releaseHandle () {
	super.releaseHandle ();
	if (nsColumn != null) {
		nsColumn.headerCell ().release ();
		nsColumn.release ();
	}
	nsColumn = null;
	parent = null;
}

@Override
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
	NSTableView tableView = ((NSTableView) parent.view);
	NSTableHeaderView headerView = tableView.headerView ();
	if (headerView == null) return;
	index = parent.indexOf (nsColumn);
	NSRect rect = headerView.headerRectOfColumn (index);
	headerView.setNeedsDisplayInRect (rect);
	rect = tableView.rectOfColumn (index);
	parent.view.setNeedsDisplayInRect (rect);
}

@Override
public void setImage (Image image) {
	checkWidget();
	if (image != null && image.isDisposed ()) {
		error (SWT.ERROR_INVALID_ARGUMENT);
	}
	super.setImage (image);
	NSTableHeaderView headerView = ((NSTableView) parent.view).headerView ();
	if (headerView == null) return;
	int index = parent.indexOf (nsColumn);
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
 * @see Table#setColumnOrder(int[])
 * @see Table#getColumnOrder()
 * @see TableColumn#getMoveable()
 * @see SWT#Move
 *
 * @since 3.1
 */
public void setMoveable (boolean moveable) {
	checkWidget ();
	this.movable = moveable;
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
public void setResizable (boolean resizable) {
	checkWidget ();
	nsColumn.setResizingMask (resizable ? OS.NSTableColumnUserResizingMask : OS.NSTableColumnNoResizing);
}

@Override
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
	NSTableHeaderView headerView = ((NSTableView) parent.view).headerView ();
	if (headerView == null) return;
	int index = parent.indexOf (nsColumn);
	NSRect rect = headerView.headerRectOfColumn (index);
	headerView.setNeedsDisplayInRect (rect);
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
 * <p>
 * NOTE: This operation is a hint and behavior is platform specific, on Windows
 * for CJK-style mnemonics of the form " (&amp;C)" at the end of the tooltip text
 * are not shown in tooltip.
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
	// TODO how to differentiate 0 and 1 cases?
	width = Math.max (0, width - Table.CELL_GAP);
	nsColumn.setWidth (width);
}

@Override
String tooltipText () {
	return toolTipText;
}

}
