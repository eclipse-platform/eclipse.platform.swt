/*******************************************************************************
 * Copyright (c) 2011 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.custom;


import org.eclipse.swt.*;
import org.eclipse.swt.accessibility.*;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

/**
 * A TreeCursor provides a way for the user to navigate around a Tree with columns using the
 * keyboard. It also provides a mechanism for selecting an individual cell in a tree.
 * <p>
 * For a detailed example of using a TreeCursor to navigate to a cell and then edit it see
 * http://git.eclipse.org/c/platform/eclipse.platform.swt.git/tree/examples/org.eclipse.swt.snippets/src/org/eclipse/swt/snippets/Snippet360.java .
 * 
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>BORDER</dd>
 * <dt><b>Events:</b></dt>
 * <dd>Selection, DefaultSelection</dd>
 * </dl>
 * 
 * @since 3.8
 */
public class TreeCursor extends Canvas {
	Tree tree;
	TreeItem row;
	TreeColumn column;
	Listener listener, treeListener, resizeListener, disposeItemListener, disposeColumnListener;

	Color background = null;
	Color foreground = null;
	
	/* By default, invert the list selection colors */
	static final int BACKGROUND = SWT.COLOR_LIST_SELECTION_TEXT;
	static final int FOREGROUND = SWT.COLOR_LIST_SELECTION;

/**
 * Constructs a new instance of this class given its parent tree and a style value describing
 * its behavior and appearance.
 * <p>
 * The style value is either one of the style constants defined in class <code>SWT</code> which
 * is applicable to instances of this class, or must be built by <em>bitwise OR</em>'ing
 * together (that is, using the <code>int</code> "|" operator) two or more of those
 * <code>SWT</code> style constants. The class description lists the style constants that are
 * applicable to the class. Style bits are also inherited from superclasses.
 * </p>
 * 
 * @param parent a Tree control which will be the parent of the new instance (cannot be null)
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
public TreeCursor(Tree parent, int style) {
	super(parent, style);
	tree = parent;
	setBackground(null);
	setForeground(null);

	listener = new Listener() {
		public void handleEvent(Event event) {
			if (row != null) {
				/*
				 * Detect cases where the cursor position has become invalid and fix it.
				 * The typical cause of this is programmatic tree changes, such as
				 * expanding/collapsing and item and creating/disposing items.
				 */
				if (row.isDisposed()) {
					unhookRowColumnListeners();
					_resize();
					tree.setFocus();
					return;
				}
				TreeItem current = row;
				TreeItem parentItem = row.getParentItem();
				while (parentItem != null && !parentItem.getExpanded()) {
					current = parentItem;
					parentItem = current.getParentItem();
				}
				if (current != row) {
					setRowColumn(current, column, false);
				}
			}
			switch (event.type) {
				case SWT.Dispose:
					onDispose(event);
					break;
				case SWT.FocusIn:
				case SWT.FocusOut:
					redraw();
					break;
				case SWT.KeyDown:
					keyDown(event);
					break;
				case SWT.Paint:
					paint(event);
					break;
				case SWT.Traverse:
					event.doit = true;
					switch (event.detail) {
						case SWT.TRAVERSE_ARROW_NEXT:
						case SWT.TRAVERSE_ARROW_PREVIOUS:
						case SWT.TRAVERSE_RETURN:
							event.doit = false;
							break;
					}
					break;
			}
		}
	};
	int[] events = new int[] { SWT.Dispose, SWT.FocusIn, SWT.FocusOut, SWT.KeyDown, SWT.Paint, SWT.Traverse };
	for (int i = 0; i < events.length; i++) {
		addListener(events[i], listener);
	}

	treeListener = new Listener() {
		public void handleEvent(Event event) {
			switch (event.type) {
				case SWT.Collapse:
					treeCollapse(event);
					break;
				case SWT.Expand:
					treeExpand(event);
					break;
				case SWT.FocusIn:
					treeFocusIn(event);
					break;
				case SWT.MouseDown:
					treeMouseDown(event);
					break;
			}
		}
	};
	tree.addListener(SWT.Collapse, treeListener);
	tree.addListener(SWT.Expand, treeListener);
	tree.addListener(SWT.FocusIn, treeListener);
	tree.addListener(SWT.MouseDown, treeListener);
	
	disposeItemListener = new Listener() {
		public void handleEvent(Event event) {
			TreeItem currentItem = row;
			while (currentItem != null) {
				currentItem.removeListener(SWT.Dispose, disposeItemListener);
				currentItem = currentItem.getParentItem();
			}
			TreeItem disposedItem = (TreeItem)event.widget;
			TreeItem parentItem = disposedItem.getParentItem();
			if (parentItem != null) {
				setRowColumn(parentItem, column, true);
			} else {
				if (tree.getItemCount() == 1) {
					unhookRowColumnListeners();
				} else {
					TreeItem newFocus = null;
					int rowIndex = tree.indexOf(disposedItem);
					if (rowIndex != 0) {
						TreeItem previousItem = tree.getItem(rowIndex - 1);
						if (!previousItem.isDisposed()) {
							newFocus = previousItem;
						}
					}
					if (newFocus == null && rowIndex + 1 < tree.getItemCount()) {
						TreeItem nextItem = tree.getItem(rowIndex + 1);
						if (!nextItem.isDisposed()) {
							newFocus = nextItem;
						}
					}
					if (newFocus != null) {
						setRowColumn(newFocus, column, true);
					} else {
						unhookRowColumnListeners();
					}
				}
			}
			_resize();
		}
	};
	disposeColumnListener = new Listener() {
		public void handleEvent(Event event) {
			if (column != null) {
				if (tree.getColumnCount() == 1) {
					column = null;
				} else {
					int columnIndex = tree.indexOf(column);
					int positionIndex = columnIndex;
					int[] columnOrder = tree.getColumnOrder();
					for (int i = 0; i < columnOrder.length; i++) {
						if (columnOrder[i] == columnIndex) {
							positionIndex = i;
							break;
						}
					}
					if (positionIndex == columnOrder.length - 1) {
						setRowColumn(row, tree.getColumn(columnOrder[positionIndex - 1]), true);
					} else {
						setRowColumn(row, tree.getColumn(columnOrder[positionIndex + 1]), true);
					}
				}
			}
			_resize();
		}
	};
	resizeListener = new Listener() {
		public void handleEvent(Event event) {
			_resize();
		}
	};
	ScrollBar hBar = tree.getHorizontalBar();
	if (hBar != null) {
		hBar.addListener(SWT.Selection, resizeListener);
	}
	ScrollBar vBar = tree.getVerticalBar();
	if (vBar != null) {
		vBar.addListener(SWT.Selection, resizeListener);
	}
	
	getAccessible().addAccessibleControlListener(new AccessibleControlAdapter() {
		public void getRole(AccessibleControlEvent e) {
			e.detail = ACC.ROLE_TABLECELL;
		}
	});
	getAccessible().addAccessibleListener(new AccessibleAdapter() {
		public void getName(AccessibleEvent e) {
			if (row == null) return;
			int columnIndex = column == null ? 0 : tree.indexOf(column);
			e.result = row.getText(columnIndex);
		}
	});
}

/**
 * Adds the listener to the collection of listeners who will be notified when the receiver's
 * selection changes, by sending it one of the messages defined in the
 * <code>SelectionListener</code> interface.
 * <p>
 * When <code>widgetSelected</code> is called, the item field of the event object is valid. If
 * the receiver has <code>SWT.CHECK</code> style set and the check selection changes, the event
 * object detail field contains the value <code>SWT.CHECK</code>.
 * <code>widgetDefaultSelected</code> is typically called when an item is double-clicked.
 * </p>
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
 * @see SelectionListener
 * @see SelectionEvent
 * @see #removeSelectionListener(SelectionListener)
 */
public void addSelectionListener(SelectionListener listener) {
	checkWidget();
	if (listener == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener(listener);
	addListener(SWT.Selection, typedListener);
	addListener(SWT.DefaultSelection, typedListener);
}

int countSubTreePages(TreeItem root) {
	int pages = 1;
	if (root == null) return 0;
	if (root.getItemCount() == 0) return 1;
	if (!root.getExpanded()) return 1;
	TreeItem[] items = root.getItems();
	for (int i = 0; i < items.length; i++) {
		pages += countSubTreePages(items[i]);
	}
	return pages;
}

int findIndex(TreeItem[] items, TreeItem treeItem) {
	if (items == null || treeItem == null) return -1;
	Rectangle rect = treeItem.getBounds();
	int index = 0;
	for (int i = 0; i < items.length; i++) {
		TreeItem previousItem = null;
		TreeItem currentItem = items[i];
		if (i > 0) previousItem = items[i - 1];
		Rectangle rect1 = currentItem.getBounds();
		if (rect.y == rect1.y) return index;
		if (rect.y < rect1.y) {
			return index - 1 + findIndex(previousItem.getItems(), treeItem);
		}
		if (rect.y > rect1.y && i == items.length - 1) {
			return index + findIndex(currentItem.getItems(), treeItem);
		}
		if (rect.y >= rect1.y + (1 + currentItem.getItemCount()) * tree.getItemHeight() && currentItem.getExpanded()) {
			index += countSubTreePages(currentItem);
			continue;
		}
		index++;
	}
	return -1;
}

TreeItem findItem(TreeItem[] items, Point pt) {
	int start = 0, end = items.length - 1;
	int index = end / 2;
	while (end - start > 1) {
		TreeItem currentItem = items[index];
		Rectangle bounds = currentItem.getBounds();
		if (pt.y < bounds.y) {
			end = index;
			index = (end - start) / 2;
		} else {
			start = index;
			index = start + ((end - start) / 2);
		}
	}

	Rectangle endBounds = items[end].getBounds();
	if (endBounds.y < pt.y) {
		if (endBounds.y + endBounds.height < pt.y) {
			if (!items[end].getExpanded()) return null;
			return findItem(items[end].getItems(), pt);
		}
		int[] columnOrder = tree.getColumnOrder();
		Rectangle bounds = null;
		if (columnOrder.length > 0) {
			Rectangle rect1 = items[end].getBounds(columnOrder[0]);
			Rectangle rect2 = items[end].getBounds(columnOrder[columnOrder.length - 1]);
			bounds = rect1.union(rect2);
			bounds.height += tree.getLinesVisible() ? tree.getGridLineWidth() : 0;
		} else {
			bounds = items[end].getBounds();
		}
		return bounds.contains(pt) ? items[end] : null;		
	}
	
	Rectangle startBounds = items[start].getBounds();
	if (startBounds.y + startBounds.height < pt.y) {
		return findItem(items[start].getItems(), pt);
	}
	int[] columnOrder = tree.getColumnOrder();
	Rectangle bounds = null;
	if (columnOrder.length > 0) {
		Rectangle rect1 = items[start].getBounds(columnOrder[0]);
		Rectangle rect2 = items[start].getBounds(columnOrder[columnOrder.length - 1]);
		bounds = rect1.union(rect2);
		bounds.height += tree.getLinesVisible() ? tree.getGridLineWidth() : 0;
	} else {
		bounds = items[start].getBounds();
	}
	return bounds.contains(pt) ? items[start] : null;
}

/**
 * Returns the background color that the receiver will use to draw.
 *
 * @return the receiver's background color
 */
public Color getBackground() {
	checkWidget();
	if (background == null) {
		return getDisplay().getSystemColor(BACKGROUND);
	}
	return background;
}

/**
 * Returns the index of the column over which the TreeCursor is positioned.
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
	return column == null ? 0 : tree.indexOf(column);
}

/**
 * Returns the foreground color that the receiver will use to draw.
 *
 * @return the receiver's foreground color
 */
public Color getForeground() {
	checkWidget();
	if (foreground == null) {
		return getDisplay().getSystemColor(FOREGROUND);
	}
	return foreground;
}

TreeItem getLastVisibleItem(TreeItem[] items) {
	if (items == null) return null;
	TreeItem last = items[items.length - 1];
	if (last.getExpanded() && last.getItemCount() > 0) {
		return getLastVisibleItem(last.getItems());
	}
	return last;
}

TreeItem getNextItem(TreeItem item) {
	if (item == null) return null;
	if (item.getExpanded() && item.getItemCount() > 0) {
		return item.getItem(0);
	}

	TreeItem parentItem = item.getParentItem();
	while (parentItem != null) {
		int index = parentItem.indexOf(item);
		if (index == -1) return null;
		if (index < parentItem.getItemCount() - 1) {
			return parentItem.getItem(index + 1);
		}
		item = parentItem;
		parentItem = item.getParentItem();
	}
	int index = tree.indexOf(item);
	if (index == -1) return null;
	if (index == tree.getItemCount() - 1) return null;
	return tree.getItem(index + 1);	
}

TreeItem getPreviousItem(TreeItem item) {
	if (item == null) return null;
	TreeItem parentItem = item.getParentItem();
	if (parentItem == null) {
		int index = tree.indexOf(item);
		if (index == -1 || index == 0) return null;
		item = tree.getItem(index - 1);
		if (item.getExpanded() && item.getItemCount() > 0) {
			return getLastVisibleItem(item.getItems());
		}
		return item;
	}
	int index = parentItem.indexOf(item);
	if (index == -1) return null;
	if (index == 0) return parentItem;
	item = parentItem.getItem(index - 1);
	if (item.getExpanded() && item.getItemCount() > 0) {
		return getLastVisibleItem(item.getItems());
	}
	return item;
}

/**
 * Returns the row over which the TreeCursor is positioned.
 * 
 * @return the item for the current position, or <code>null</code> if none
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public TreeItem getRow() {
	checkWidget();
	return row;
}

void keyDown(Event event) {
	if (row == null) return;
	switch (event.character) {
		case SWT.CR:
			notifyListeners(SWT.DefaultSelection, new Event());
			return;
	}
	switch (event.keyCode) {
		case SWT.ARROW_UP:
			TreeItem previousItem = getPreviousItem(row);
			if (previousItem != null) {
				setRowColumn(previousItem, column, true);
			}
			break;
		case SWT.ARROW_DOWN:
			TreeItem nextItem = getNextItem(row);
			if (nextItem != null) {
				setRowColumn(nextItem, column, true);
			}
			break;
		case SWT.ARROW_LEFT:
		case SWT.ARROW_RIGHT: {
			if ((event.stateMask & SWT.MOD1) != 0) {
				row.setExpanded (event.keyCode == SWT.ARROW_RIGHT);
				break;
			}
			int columnCount = tree.getColumnCount();
			if (columnCount == 0) break;
			int columnIndex = column == null ? 0 : tree.indexOf(column);
			int[] columnOrder = tree.getColumnOrder();
			int index = 0;
			while (index < columnOrder.length) {
				if (columnOrder[index] == columnIndex) break;
				index++;
			}
			if (index == columnOrder.length) index = 0;
			int leadKey = (getStyle() & SWT.RIGHT_TO_LEFT) != 0 ? SWT.ARROW_RIGHT : SWT.ARROW_LEFT;
			TreeItem parentRow = row.getParentItem();
			int rowIndex = tree.indexOf(row);
			if (event.keyCode == leadKey) {
				if (parentRow != null) {
					setRowColumn(row, tree.getColumn(columnOrder[Math.max(0, index - 1)]), true);
				} else {
					setRowColumn(rowIndex, columnOrder[Math.max(0, index - 1)], true);
				}
			} else {
				if (parentRow != null) {
					setRowColumn(row, tree.getColumn(columnOrder[Math.min(columnCount - 1, index + 1)]), true);
				} else {
					setRowColumn(rowIndex, columnOrder[Math.min(columnCount - 1, index + 1)], true);
				}
			}
			break;
		}
		case SWT.HOME:
			int columnIndex = column == null ? 0 : tree.indexOf(column);
			setRowColumn(0, columnIndex, true);
			break;
		case SWT.END: {
			TreeItem[] items = tree.getItems();
			setRowColumn(getLastVisibleItem(items), column, true);
			break;
		}
		case SWT.PAGE_UP: {
			Rectangle rect = tree.getClientArea();
			Rectangle itemRect = tree.getTopItem().getBounds();
			TreeItem item = row;
			int index = findIndex(tree.getItems(), item);
			int itemHeight = tree.getItemHeight();
			rect.height -= itemRect.y;
			int page = Math.max(1, rect.height / itemHeight);
			if (index - page <= 0) {
				TreeItem first = tree.getItem(0);
				setRowColumn(first, column, true);
				break;
			}
			for (int i = 0; i < page; i++) {
				item = getPreviousItem(item);
			}
			setRowColumn(item, column, true);
			break;
		}
		case SWT.PAGE_DOWN: {
			Rectangle rect = tree.getClientArea();
			Rectangle itemRect = tree.getTopItem().getBounds();
			TreeItem item = row;
			int index = findIndex(tree.getItems(), item);
			int height = tree.getItemHeight();
			rect.height -= itemRect.y;
			TreeItem last = getLastVisibleItem(tree.getItems());
			int page = Math.max(1, rect.height / height);
			int end = findIndex(tree.getItems(), last);
			if (end <= index + page) {
				setRowColumn(last, column, true);
				break;
			}
			for (int i = 0; i < page; i++) {
				item = getNextItem(item);
			}
			setRowColumn(item, column, true);
			break;
		}
	}
}

void onDispose(Event event) {
	removeListener(SWT.Dispose, listener);
	notifyListeners(SWT.Dispose, event);
	event.type = SWT.None;

	tree.removeListener(SWT.Collapse, treeListener);
	tree.removeListener(SWT.Expand, treeListener);
	tree.removeListener(SWT.FocusIn, treeListener);
	tree.removeListener(SWT.MouseDown, treeListener);
	unhookRowColumnListeners();
	ScrollBar hBar = tree.getHorizontalBar();
	if (hBar != null) {
		hBar.removeListener(SWT.Selection, resizeListener);
	}
	ScrollBar vBar = tree.getVerticalBar();
	if (vBar != null) {
		vBar.removeListener(SWT.Selection, resizeListener);
	}
}

void paint(Event event) {
	if (row == null) return;
	int columnIndex = column == null ? 0 : tree.indexOf(column);
	int orderedIndex = columnIndex;
	int[] columnOrder = tree.getColumnOrder();
	for (int i = 0; i < columnOrder.length; i++) {
		if (columnOrder[i] == columnIndex) {
			orderedIndex = i;
			break;
		}
	}
	GC gc = event.gc;
	gc.setBackground(getBackground());
	gc.setForeground(getForeground());
	gc.fillRectangle(event.x, event.y, event.width, event.height);
	Image image = row.getImage(columnIndex);
	int x = 0;
	// Temporary code - need a better way to determine trim
	String platform = SWT.getPlatform();
	if (image != null || (orderedIndex > 0 && "win32".equals(platform))) { //$NON-NLS-1$
		x += 2;
	}
	Point size = getSize();
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
		// Temporary code - need a better way to determine trim
		if ("win32".equals(platform)) { //$NON-NLS-1$
			if (tree.getColumnCount() == 0 || orderedIndex == 0) {
				x += image == null ? 2 : 5;
			} else {
				int alignmnent = column.getAlignment();
				switch (alignmnent) {
					case SWT.LEFT:
						x += image == null ? 5 : 3;
						break;
					case SWT.RIGHT:
						x = bounds.width - extent.x - 2;
						break;
					case SWT.CENTER:
						x += Math.ceil((bounds.width - x - extent.x) / 2.0);
						break;
				}
			}
		} else {
			if (tree.getColumnCount() == 0) {
				x += image == null ? 4 : 3;
			} else {
				int alignmnent = column.getAlignment();
				switch (alignmnent) {
					case SWT.LEFT:
						x += image == null ? 5 : 3;
						break;
					case SWT.RIGHT:
						x = bounds.width - extent.x - 2;
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

/**
 * Removes the listener from the collection of listeners who will be notified when the
 * receiver's selection changes.
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
 */
public void removeSelectionListener(SelectionListener listener) {
	checkWidget();
	if (listener == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	removeListener(SWT.Selection, listener);
	removeListener(SWT.DefaultSelection, listener);
}

void _resize() {
	if (row == null) {
		setBounds(-200, -200, 0, 0);
	} else {
		int columnIndex = column == null ? 0 : tree.indexOf(column);
		setBounds(row.getBounds(columnIndex));
	}
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
public void setForeground (Color color) {
	foreground = color;
	super.setForeground(getForeground());
	redraw();
}

void setRowColumn(int row, int column, boolean notify) {
	TreeItem item = row == -1 ? null : tree.getItem(row);
	TreeColumn col = column == -1 || tree.getColumnCount() == 0 ? null : tree.getColumn(column);
	setRowColumn(item, col, notify);
}

void setRowColumn(TreeItem row, TreeColumn column, boolean notify) {
	if (this.row != null && this.row != row) {
		TreeItem currentItem = this.row;
		while (currentItem != null) {
			currentItem.removeListener(SWT.Dispose, disposeItemListener);
			currentItem = currentItem.getParentItem();
		}
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
			TreeItem currentItem = row;
			while (currentItem != null) {
				currentItem.addListener(SWT.Dispose, disposeItemListener);
				currentItem = currentItem.getParentItem();
			}
			tree.showItem(row);
		}
		if (this.column != column && column != null) {
			this.column = column;
			column.addListener(SWT.Dispose, disposeColumnListener);
			column.addListener(SWT.Move, resizeListener);
			column.addListener(SWT.Resize, resizeListener);
			tree.showColumn(column);
		}
		int columnIndex = column == null ? 0 : tree.indexOf(column);
		setBounds(row.getBounds(columnIndex));
		redraw();
		if (notify) notifyListeners(SWT.Selection, new Event());
	}
}

/**
 * Positions the TreeCursor over the root-level cell at the given row and column in the parent tree.
 * 
 * @param row the index of the root-level row for the cell to select
 * @param column the index of column for the cell to select
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setSelection(int row, int column) {
	checkWidget();
	int columnCount = tree.getColumnCount();
	int maxColumnIndex = columnCount == 0 ? 0 : columnCount - 1;
	if (row < 0 || row >= tree.getItemCount() || column < 0 || column > maxColumnIndex) {
		SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	}
	setRowColumn(row, column, false);
}

/**
 * Positions the TreeCursor over the cell at the given row and column in the parent tree.
 * 
 * @param row the TreeItem of the row for the cell to select
 * @param column the index of column for the cell to select
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setSelection(TreeItem row, int column) {
	checkWidget();
	int columnCount = tree.getColumnCount();
	int maxColumnIndex = columnCount == 0 ? 0 : columnCount - 1;
	if (row == null || row.isDisposed() || column < 0 || column > maxColumnIndex) {
		SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	}
	TreeColumn col = tree.getColumnCount() == 0 ? null : tree.getColumn(column);
	setRowColumn(row, col, false);
}

public void setVisible(boolean visible) {
	checkWidget();
	if (visible) {
		_resize();
	}
	super.setVisible(visible);
}

void treeCollapse(Event event) {
	if (row == null) return;
	TreeItem root = (TreeItem)event.item;
	TreeItem parentItem = row.getParentItem();
	while (parentItem != null) {
		if (parentItem == root) {
			setRowColumn(root, column, true);
			return;
		}
		parentItem = parentItem.getParentItem();
	}
	
	getDisplay().asyncExec(new Runnable() {
		public void run() {
			if (isDisposed()) return;
			setRowColumn(row, column, true);
		}
	});
}

void treeExpand(Event event) {
	getDisplay().asyncExec(new Runnable() {
		public void run() {
			if (isDisposed()) return;
			setRowColumn(row, column, true);
		}
	});
}

void treeFocusIn(Event event) {
	if (isVisible()) {
		if (row == null && column == null) return;
		setFocus();
	}
}

void treeMouseDown(Event event) {
	if (tree.getItemCount() == 0) return;
	Point pt = new Point(event.x, event.y);
	TreeItem item = tree.getItem(pt);
	if (item == null && (tree.getStyle() & SWT.FULL_SELECTION) == 0) {
		TreeItem currentItem = tree.getTopItem();
		TreeItem parentItem = currentItem.getParentItem();
		while (parentItem != null) {
			currentItem = parentItem;
			parentItem = currentItem.getParentItem();
		}
		int start = tree.indexOf(currentItem);
		int viewportItemCount = tree.getClientArea().height / tree.getItemHeight();
		int end = Math.min(start + viewportItemCount, tree.getItemCount() - 1);
		TreeItem[] allItems = tree.getItems();
		TreeItem[] items = new TreeItem[end - start + 1];
		System.arraycopy(allItems, start, items, 0, end - start + 1);
		item = findItem(items, pt);
	}
	if (item == null) return;

	TreeColumn newColumn = null;
	int lineWidth = tree.getLinesVisible() ? tree.getGridLineWidth() : 0;
	int columnCount = tree.getColumnCount();
	if (columnCount > 0) {
		for (int i = 0; i < columnCount; i++) {
			Rectangle rect = item.getBounds(i);
			rect.width += lineWidth;
			rect.height += lineWidth;
			if (rect.contains(pt)) {
				newColumn = tree.getColumn(i);
				break;
			}
		}
		if (newColumn == null) {
			newColumn = tree.getColumn(0);
		}
	}
	setRowColumn(item, newColumn, true);
	setFocus();
}

void unhookRowColumnListeners() {
	if (column != null && !column.isDisposed()) {
		column.removeListener(SWT.Dispose, disposeColumnListener);
		column.removeListener(SWT.Move, resizeListener);
		column.removeListener(SWT.Resize, resizeListener);
	}
	column = null;
	if (row != null && !row.isDisposed()) {
		TreeItem currentItem = row;
		while (currentItem != null) {
			currentItem.removeListener(SWT.Dispose, disposeItemListener);
			currentItem = currentItem.getParentItem();
		}
	}
	row = null;
}

}
