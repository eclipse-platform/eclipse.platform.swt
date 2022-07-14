/*******************************************************************************
 * Copyright (c) 2000, 2016 IBM Corporation and others.
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
package org.eclipse.swt.custom;

import org.eclipse.swt.*;
import org.eclipse.swt.accessibility.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

/**
 * A TableCursor provides a way for the user to navigate around a Table
 * using the keyboard.  It also provides a mechanism for selecting an
 * individual cell in a table.
 * <p>
 * For a detailed example of using a TableCursor to navigate to a cell and then edit it see
 * https://github.com/eclipse-platform/eclipse.platform.swt/tree/master/examples/org.eclipse.swt.snippets/src/org/eclipse/swt/snippets/Snippet96.java .
 *
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>BORDER</dd>
 * <dt><b>Events:</b></dt>
 * <dd>Selection, DefaultSelection</dd>
 * </dl>
 *
 * @since 2.0
 *
 * @see <a href="http://www.eclipse.org/swt/snippets/#tablecursor">TableCursor snippets</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 */
public class TableCursor extends Canvas {
	Table table;
	TableItem row = null;
	TableColumn column = null;
	Listener listener, tableListener, resizeListener, disposeItemListener, disposeColumnListener;

	Color background = null;
	Color foreground = null;

	/* By default, invert the list selection colors */
	static final int BACKGROUND = SWT.COLOR_LIST_SELECTION_TEXT;
	static final int FOREGROUND = SWT.COLOR_LIST_SELECTION;

/**
 * Constructs a new instance of this class given its parent
 * table and a style value describing its behavior and appearance.
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
 * @param parent a Table control which will be the parent of the new instance (cannot be null)
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
 * @see SWT#BORDER
 * @see Widget#checkSubclass()
 * @see Widget#getStyle()
 */
public TableCursor(Table parent, int style) {
	super(parent, style);
	table = parent;
	setBackground(null);
	setForeground(null);

	listener = event -> {
		switch (event.type) {
			case SWT.Dispose :
				onDispose(event);
				break;
			case SWT.FocusIn :
			case SWT.FocusOut :
				redraw();
				break;
			case SWT.KeyDown :
				keyDown(event);
				break;
			case SWT.Paint :
				paint(event);
				break;
			case SWT.Traverse : {
				event.doit = true;
				switch (event.detail) {
					case SWT.TRAVERSE_ARROW_NEXT :
					case SWT.TRAVERSE_ARROW_PREVIOUS :
					case SWT.TRAVERSE_RETURN :
						event.doit = false;
						break;
				}
				break;
			}
		}
	};
	int[] events = new int[] {SWT.Dispose, SWT.FocusIn, SWT.FocusOut, SWT.KeyDown, SWT.Paint, SWT.Traverse};
	for (int event : events) {
		addListener(event, listener);
	}

	tableListener = event -> {
		switch (event.type) {
			case SWT.MouseDown :
				tableMouseDown(event);
				break;
			case SWT.FocusIn :
				tableFocusIn(event);
				break;
		}
	};
	table.addListener(SWT.FocusIn, tableListener);
	table.addListener(SWT.MouseDown, tableListener);

	disposeItemListener = event -> {
		unhookRowColumnListeners();
		row = null;
		column = null;
		_resize();
	};
	disposeColumnListener = event -> {
		unhookRowColumnListeners();
		row = null;
		column = null;
		_resize();
	};
	resizeListener = event -> _resize();
	ScrollBar hBar = table.getHorizontalBar();
	if (hBar != null) {
		hBar.addListener(SWT.Selection, resizeListener);
	}
	ScrollBar vBar = table.getVerticalBar();
	if (vBar != null) {
		vBar.addListener(SWT.Selection, resizeListener);
	}

	getAccessible().addAccessibleControlListener(new AccessibleControlAdapter() {
		@Override
		public void getRole(AccessibleControlEvent e) {
			e.detail = ACC.ROLE_TABLECELL;
		}
	});
	getAccessible().addAccessibleListener(new AccessibleAdapter() {
		@Override
		public void getName(AccessibleEvent e) {
			if (row == null) return;
			int columnIndex = column == null ? 0 : table.indexOf(column);
			e.result = row.getText(columnIndex);
		}
	});
}

/**
 * Adds the listener to the collection of listeners who will
 * be notified when the user changes the receiver's selection, by sending
 * it one of the messages defined in the <code>SelectionListener</code>
 * interface.
 * <p>
 * When <code>widgetSelected</code> is called, the item field of the event object is valid.
 * If the receiver has <code>SWT.CHECK</code> style set and the check selection changes,
 * the event object detail field contains the value <code>SWT.CHECK</code>.
 * <code>widgetDefaultSelected</code> is typically called when an item is double-clicked.
 * </p>
 *
 * @param listener the listener which should be notified when the user changes the receiver's selection
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
 * @see SelectionEvent
 * @see #removeSelectionListener(SelectionListener)
 *
 */
public void addSelectionListener(SelectionListener listener) {
	checkWidget();
	if (listener == null)
		SWT.error(SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener(listener);
	addListener(SWT.Selection, typedListener);
	addListener(SWT.DefaultSelection, typedListener);
}

void onDispose(Event event) {
	removeListener(SWT.Dispose, listener);
	notifyListeners(SWT.Dispose, event);
	event.type = SWT.None;

	table.removeListener(SWT.FocusIn, tableListener);
	table.removeListener(SWT.MouseDown, tableListener);
	unhookRowColumnListeners();
	ScrollBar hBar = table.getHorizontalBar();
	if (hBar != null) {
		hBar.removeListener(SWT.Selection, resizeListener);
	}
	ScrollBar vBar = table.getVerticalBar();
	if (vBar != null) {
		vBar.removeListener(SWT.Selection, resizeListener);
	}
}

void keyDown(Event event) {
	if (row == null) return;
	switch (event.character) {
		case SWT.CR :
			notifyListeners(SWT.DefaultSelection, new Event());
			return;
	}
	int rowIndex = table.indexOf(row);
	int columnIndex = column == null ? 0 : table.indexOf(column);
	switch (event.keyCode) {
		case SWT.ARROW_UP :
			setRowColumn(Math.max(0, rowIndex - 1), columnIndex, true);
			break;
		case SWT.ARROW_DOWN :
			setRowColumn(Math.min(rowIndex + 1, table.getItemCount() - 1), columnIndex, true);
			break;
		case SWT.ARROW_LEFT :
		case SWT.ARROW_RIGHT :
			{
				int columnCount = table.getColumnCount();
				if (columnCount == 0) break;
				int[] order = table.getColumnOrder();
				int index = 0;
				while (index < order.length) {
					if (order[index] == columnIndex) break;
					index++;
				}
				if (index == order.length) index = 0;
				int leadKey = (getStyle() & SWT.RIGHT_TO_LEFT) != 0 ? SWT.ARROW_RIGHT : SWT.ARROW_LEFT;
				if (event.keyCode == leadKey) {
					setRowColumn(rowIndex, order[Math.max(0, index - 1)], true);
				} else {
					setRowColumn(rowIndex, order[Math.min(columnCount - 1, index + 1)], true);
				}
				break;
			}
		case SWT.HOME :
			setRowColumn(0, columnIndex, true);
			break;
		case SWT.END :
			{
				int i = table.getItemCount() - 1;
				setRowColumn(i, columnIndex, true);
				break;
			}
		case SWT.PAGE_UP :
			{
				int index = table.getTopIndex();
				if (index == rowIndex) {
					Rectangle rect = table.getClientArea();
					TableItem item = table.getItem(index);
					Rectangle itemRect = item.getBounds(0);
					rect.height -= itemRect.y;
					int height = table.getItemHeight();
					int page = Math.max(1, rect.height / height);
					index = Math.max(0, index - page + 1);
				}
				setRowColumn(index, columnIndex, true);
				break;
			}
		case SWT.PAGE_DOWN :
			{
				int index = table.getTopIndex();
				Rectangle rect = table.getClientArea();
				TableItem item = table.getItem(index);
				Rectangle itemRect = item.getBounds(0);
				rect.height -= itemRect.y;
				int height = table.getItemHeight();
				int page = Math.max(1, rect.height / height);
				int end = table.getItemCount() - 1;
				index = Math.min(end, index + page - 1);
				if (index == rowIndex) {
					index = Math.min(end, index + page - 1);
				}
				setRowColumn(index, columnIndex, true);
				break;
			}
	}
}

void paint(Event event) {
	if (row == null) return;
	int columnIndex = column == null ? 0 : table.indexOf(column);
	GC gc = event.gc;
	gc.setBackground(getBackground());
	gc.setForeground(getForeground());
	gc.fillRectangle(event.x, event.y, event.width, event.height);
	int x = 0;
	Point size = getSize();
	Image image = row.getImage(columnIndex);
	if (image != null) {
		Rectangle imageSize = image.getBounds();
		int imageY = (size.y - imageSize.height) / 2;
		gc.drawImage(image, x, imageY);
		x += imageSize.width;
	}
	String text = row.getText(columnIndex);
	if (text.length() > 0) {
		Rectangle bounds = row.getBounds(columnIndex);
		Point extent = gc.stringExtent(text);
		// Temporary code - need a better way to determine table trim
		String platform = SWT.getPlatform();
		if ("win32".equals(platform)) { //$NON-NLS-1$
			if (table.getColumnCount() == 0 || columnIndex == 0) {
				x += 2;
			} else {
				int alignmnent = column.getAlignment();
				switch (alignmnent) {
					case SWT.LEFT:
						x += 6;
						break;
					case SWT.RIGHT:
						x = bounds.width - extent.x - 6;
						break;
					case SWT.CENTER:
						x += (bounds.width - x - extent.x) / 2;
						break;
				}
			}
		}  else {
			if (table.getColumnCount() == 0) {
				x += 5;
			} else {
				int alignmnent = column.getAlignment();
				switch (alignmnent) {
					case SWT.LEFT:
						x += 5;
						break;
					case SWT.RIGHT:
						x = bounds.width- extent.x - 2;
						break;
					case SWT.CENTER:
						x += (bounds.width - x - extent.x) / 2 + 2;
						break;
				}
			}
		}
		int textY = (size.y - extent.y) / 2;
		gc.drawString(text, x, textY);
	}
	if (isFocusControl()) {
		Display display = getDisplay();
		gc.setBackground(display.getSystemColor(SWT.COLOR_BLACK));
		gc.setForeground(display.getSystemColor(SWT.COLOR_WHITE));
		gc.drawFocus(0, 0, size.x, size.y);
	}
}

void tableFocusIn(Event event) {
	if (isDisposed()) return;
	if (isVisible()) {
		if (row == null && column == null) return;
		setFocus();
	}
}

void tableMouseDown(Event event) {
	if (isDisposed() || !isVisible()) return;
	Point pt = new Point(event.x, event.y);
	int lineWidth = table.getLinesVisible() ? table.getGridLineWidth() : 0;
	TableItem item = table.getItem(pt);
	if ((table.getStyle() & SWT.FULL_SELECTION) != 0) {
		if (item == null) return;
	} else {
		int start = item != null ? table.indexOf(item) : table.getTopIndex();
		int end = table.getItemCount();
		Rectangle clientRect = table.getClientArea();
		for (int i = start; i < end; i++) {
			TableItem nextItem = table.getItem(i);
			Rectangle rect = nextItem.getBounds(0);
			if (pt.y >= rect.y && pt.y < rect.y + rect.height + lineWidth) {
				item = nextItem;
				break;
			}
			if (rect.y > clientRect.y + clientRect.height) 	return;
		}
		if (item == null) return;
	}
	TableColumn newColumn = null;
	int columnCount = table.getColumnCount();
	if (columnCount == 0) {
		if ((table.getStyle() & SWT.FULL_SELECTION) == 0) {
			Rectangle rect = item.getBounds(0);
			rect.width += lineWidth;
			rect.height += lineWidth;
			if (!rect.contains(pt)) return;
		}
	} else {
		for (int i = 0; i < columnCount; i++) {
			Rectangle rect = item.getBounds(i);
			rect.width += lineWidth;
			rect.height += lineWidth;
			if (rect.contains(pt)) {
				newColumn = table.getColumn(i);
				break;
			}
		}
		if (newColumn == null) {
			if ((table.getStyle() & SWT.FULL_SELECTION) == 0) return;
			newColumn = table.getColumn(0);
		}
	}
	setRowColumn(item, newColumn, true);
	setFocus();
	return;
}
void setRowColumn(int row, int column, boolean notify) {
	TableItem item = row == -1 ? null : table.getItem(row);
	TableColumn col = column == -1 || table.getColumnCount() == 0 ? null : table.getColumn(column);
	setRowColumn(item, col, notify);
}
void setRowColumn(TableItem row, TableColumn column, boolean notify) {
	if (this.row == row && this.column == column) {
		return;
	}
	if (this.row != null && this.row != row) {
		this.row.removeListener(SWT.Dispose, disposeItemListener);
		this.row = null;
	}
	if (this.column != null && this.column != column) {
		this.column.removeListener(SWT.Dispose, disposeColumnListener);
		this.column.removeListener(SWT.Move, resizeListener);
		this.column.removeListener(SWT.Resize, resizeListener);
		this.column = null;
	}
	if (row != null) {
		if (this.row != row) {
			this.row = row;
			row.addListener(SWT.Dispose, disposeItemListener);
			table.showItem(row);
		}
		if (this.column != column && column != null) {
			this.column = column;
			column.addListener(SWT.Dispose, disposeColumnListener);
			column.addListener(SWT.Move, resizeListener);
			column.addListener(SWT.Resize, resizeListener);
			table.showColumn(column);
		}
		int columnIndex = column == null ? 0 : table.indexOf(column);
		setBounds(row.getBounds(columnIndex));
		redraw();
		if (notify) {
			notifyListeners(SWT.Selection, new Event());
		}
	}
	getAccessible().setFocus(ACC.CHILDID_SELF);
}

@Override
public void setVisible(boolean visible) {
	checkWidget();
	if (visible) _resize();
	super.setVisible(visible);
}

/**
 * Removes the listener from the collection of listeners who will
 * be notified when the user changes the receiver's selection.
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
 * @see #addSelectionListener(SelectionListener)
 *
 * @since 3.0
 */
public void removeSelectionListener(SelectionListener listener) {
	checkWidget();
	if (listener == null) {
		SWT.error(SWT.ERROR_NULL_ARGUMENT);
	}
	removeListener(SWT.Selection, listener);
	removeListener(SWT.DefaultSelection, listener);
}

void _resize() {
	if (row == null) {
		setBounds(-200, -200, 0, 0);
	} else {
		int columnIndex = column == null ? 0 : table.indexOf(column);
		setBounds(row.getBounds(columnIndex));
	}
}
/**
 * Returns the index of the column over which the TableCursor is positioned.
 *
 * @return the column index for the current position
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getColumn() {
	checkWidget();
	return column == null ? 0 : table.indexOf(column);
}
/**
 * Returns the background color that the receiver will use to draw.
 *
 * @return the receiver's background color
 */
@Override
public Color getBackground() {
	checkWidget();
	if (background == null) {
		return getDisplay().getSystemColor(BACKGROUND);
	}
	return background;
}
/**
 * Returns the foreground color that the receiver will use to draw.
 *
 * @return the receiver's foreground color
 */
@Override
public Color getForeground() {
	checkWidget();
	if (foreground == null) {
		return getDisplay().getSystemColor(FOREGROUND);
	}
	return foreground;
}
/**
 * Returns the row over which the TableCursor is positioned.
 *
 * @return the item for the current position, or <code>null</code> if none
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public TableItem getRow() {
	checkWidget();
	return row;
}
/**
 * Sets the receiver's background color to the color specified
 * by the argument, or to the default system color for the control
 * if the argument is null.
 * <p>
 * Note: This operation is a hint and may be overridden by the platform.
 * For example, on Windows the background of a Button cannot be changed.
 * </p>
 * @param color the new color (or null)
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the argument has been disposed</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
@Override
public void setBackground (Color color) {
	background = color;
	super.setBackground(getBackground());
	redraw();
}
/**
 * Sets the receiver's foreground color to the color specified
 * by the argument, or to the default system color for the control
 * if the argument is null.
 * <p>
 * Note: This operation is a hint and may be overridden by the platform.
 * </p>
 * @param color the new color (or null)
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the argument has been disposed</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
@Override
public void setForeground (Color color) {
	foreground = color;
	super.setForeground(getForeground());
	redraw();
}
/**
 * Positions the TableCursor over the cell at the given row and column in the parent table.
 *
 * @param row the index of the row for the cell to select
 * @param column the index of column for the cell to select
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 */
public void setSelection(int row, int column) {
	checkWidget();
	int columnCount = table.getColumnCount();
	int maxColumnIndex =  columnCount == 0 ? 0 : columnCount - 1;
	if (row < 0
		|| row >= table.getItemCount()
		|| column < 0
		|| column > maxColumnIndex)
		SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	setRowColumn(row, column, false);
}
/**
 * Positions the TableCursor over the cell at the given row and column in the parent table.
 *
 * @param row the TableItem of the row for the cell to select
 * @param column the index of column for the cell to select
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 */
public void setSelection(TableItem row, int column) {
	checkWidget();
	int columnCount = table.getColumnCount();
	int maxColumnIndex =  columnCount == 0 ? 0 : columnCount - 1;
	if (row == null
		|| row.isDisposed()
		|| column < 0
		|| column > maxColumnIndex)
		SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	setRowColumn(table.indexOf(row), column, false);
}
void unhookRowColumnListeners() {
	if (column != null) {
		column.removeListener(SWT.Dispose, disposeColumnListener);
		column.removeListener(SWT.Move, resizeListener);
		column.removeListener(SWT.Resize, resizeListener);
		column = null;
	}
	if (row != null) {
		row.removeListener(SWT.Dispose, disposeItemListener);
		row = null;
	}
}
}
