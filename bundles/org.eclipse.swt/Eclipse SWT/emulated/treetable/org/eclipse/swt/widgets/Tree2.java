package org.eclipse.swt.widgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.*;

public class Tree2 extends Composite {
	Canvas header;
	TreeColumn[] columns = new TreeColumn [0];
	TreeItem2[] items = new TreeItem2 [0];
	TreeItem2[] availableItems = new TreeItem2 [0];
	TreeItem2[] selectedItems = new TreeItem2 [0];
	TreeItem2 focusItem;
	boolean linesVisible;
	int topIndex = 0, horizontalOffset = 0;
	int fontHeight = 0, imageHeight = 0, itemHeight = 0;
	int col0ImageWidth = 0;
	int headerImageHeight = 0;
	TreeColumn resizeColumn;
	int resizeColumnX = -1;
	boolean inExpand = false;	/* enables item creation within Expand callback */

	static Color LineColor, HighlightShadowColor, NormalShadowColor;
	static Cursor ResizeCursor;
	
	static final int MARGIN_IMAGE = 3;
	static final int MARGIN_CELL = 1;
	static final int TOLLERANCE_COLUMNRESIZE = 2;
	static final int WIDTH_HEADER_SHADOW = 2;
	static final int WIDTH_CELL_HIGHLIGHT = 1;

public Tree2 (Composite parent, int style) {
	super(parent, checkStyle (style | SWT.H_SCROLL | SWT.V_SCROLL | SWT.NO_REDRAW_RESIZE));
	Display display = getDisplay();
	setForeground(display.getSystemColor(SWT.COLOR_LIST_FOREGROUND));
	setBackground(display.getSystemColor(SWT.COLOR_LIST_BACKGROUND));
	GC gc = new GC(this);
	fontHeight = gc.getFontMetrics().getHeight();
	gc.dispose();
	itemHeight = fontHeight + (2 * getCellPadding ());
	if (LineColor == null) {
		LineColor = display.getSystemColor(SWT.COLOR_BLACK);
		HighlightShadowColor = display.getSystemColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW);
		NormalShadowColor = display.getSystemColor(SWT.COLOR_WIDGET_NORMAL_SHADOW);
		ResizeCursor = display.getSystemCursor(SWT.CURSOR_SIZEWE);
	}

	Listener listener = new Listener() {
		public void handleEvent(Event event) {
			handleEvents(event);
		}
	};
	addListener(SWT.Paint, listener);
	addListener(SWT.MouseDown, listener);
	addListener(SWT.MouseDoubleClick, listener);
	addListener(SWT.Dispose, listener);	
	addListener(SWT.Resize, listener);
	addListener(SWT.KeyDown, listener);
	addListener(SWT.FocusOut, listener);
	addListener(SWT.FocusIn, listener);
	addListener(SWT.Traverse, listener);
	header = new Canvas(this, SWT.NO_REDRAW_RESIZE | SWT.NO_FOCUS);
	header.setVisible(false);
	header.setLocation(0,0);
	header.addListener(SWT.Paint, listener);
	header.addListener(SWT.MouseDown, listener);
	header.addListener(SWT.MouseUp, listener);
	header.addListener(SWT.MouseMove, listener);
	header.addListener(SWT.MouseExit, listener);

	ScrollBar vBar = getVerticalBar();
	vBar.setMaximum(1);	// TODO ok?
	vBar.addListener(SWT.Selection, listener);
	ScrollBar hBar = getHorizontalBar();
	hBar.addListener(SWT.Selection, listener);
	hBar.setMaximum(1);	// TODO ok?
}
void addColumn(TreeColumn column, int index) {
	/* insert column into the columns collection */
	TreeColumn[] newColumns = new TreeColumn [columns.length + 1];
	System.arraycopy(columns, 0, newColumns, 0, index);
	newColumns[index] = column;
	System.arraycopy(columns, index, newColumns, index + 1, columns.length - index);
	columns = newColumns;

	/* no visual update needed because column's initial width is 0 */
}
void addItem(TreeItem2 item, int index) {
	/* insert item into the root items collection */
	TreeItem2[] newItems = new TreeItem2 [items.length + 1];
	System.arraycopy(items, 0, newItems, 0, index);
	newItems[index] = item;
	System.arraycopy(items, index, newItems, index + 1, items.length - index);
	items = newItems;

	/* determine the item's availability index */
	int startIndex;
	if (index == items.length - 1) {
		startIndex = availableItems.length;		/* last item */
	} else {
		startIndex = items[index + 1].availableIndex;
	}
	
	/* root items are always available so insert into available items collection */
	TreeItem2[] newAvailableItems = new TreeItem2[availableItems.length + 1];
	System.arraycopy(availableItems, 0, newAvailableItems, 0, startIndex);
	newAvailableItems[startIndex] = item;
	System.arraycopy(availableItems, startIndex, newAvailableItems, startIndex + 1, newAvailableItems.length - startIndex - 1);
	availableItems = newAvailableItems;
	
	/* update the availableIndex for items bumped down by this new item */
	for (int i = startIndex; i < availableItems.length; i++) {
		availableItems[i].availableIndex = i;
	}

	updateVerticalBar();
	updateHorizontalBar();
	int redrawIndex = index;
	if (redrawIndex > 0) redrawIndex--;
	redrawFromItemDownwards(items[redrawIndex].availableIndex);
}
public void addSelectionListener(SelectionListener listener) {
	checkWidget();
	if (listener == null) error(SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener(listener);	
	addListener(SWT.Selection, typedListener);
	addListener(SWT.DefaultSelection, typedListener);
}
public void addTreeListener(TreeListener listener) {
	checkWidget();
	if (listener == null) error(SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener(listener);	
	addListener(SWT.Expand, typedListener);
	addListener(SWT.Collapse, typedListener);
}
static int checkStyle (int style) {
	return checkBits (style, SWT.SINGLE, SWT.MULTI, 0, 0, 0, 0);
}
/*
 * Returns the index of the column that the specified x falls within, or
 * -1 if the x lies to the right of the last column.
 */
int computeColumnIntersect(int x, int startColumn) {
	int numColumns = getColumnCount();
	for (int i = startColumn; i < numColumns; i++) {
		int endX = columns[i].getRightmostX();
		if (x <= endX) return i;
	}
	return -1;
}
public void deselectAll() {
	checkWidget();
	TreeItem2[] oldSelection = selectedItems;
	internalSetSelection(new TreeItem2[0]);
	for (int i = 0; i < oldSelection.length; i++) {
		redrawItem(oldSelection[i].availableIndex);
	}
}
void doArrowDown(int stateMask) {
	if ((stateMask & (SWT.CTRL | SWT.SHIFT)) == 0) {
		int newFocusIndex = focusItem.availableIndex + 1;
		if (newFocusIndex == availableItems.length) return; 	/* at bottom */
		selectItem(availableItems[newFocusIndex], false);
		setFocusItem(availableItems[newFocusIndex], true);
		Event newEvent = new Event();
		newEvent.item = this;
		sendEvent(SWT.Selection, newEvent);
		showItem(availableItems[newFocusIndex]);
		redrawItem(newFocusIndex);
		return;
	}
	if ((style & SWT.SINGLE) != 0) {
		if ((stateMask & SWT.CTRL) != 0) {
			int visibleItemCount = (getClientArea().height - getHeaderHeight()) / itemHeight;
			if (availableItems.length <= topIndex + visibleItemCount) return;	/* at bottom */
			topIndex++;
			getVerticalBar().setSelection(topIndex);
			redraw();
			return;
		} else {
			int newFocusIndex = focusItem.availableIndex + 1;
			if (newFocusIndex == availableItems.length) return; 	/* at bottom */
			selectItem(availableItems[newFocusIndex], false);
			setFocusItem(availableItems[newFocusIndex], true);
			Event newEvent = new Event();
			newEvent.item = this;
			sendEvent(SWT.Selection, newEvent);
			showItem(availableItems[newFocusIndex]);
			redrawItem(newFocusIndex);
			return;
		}
	}
	/* SWT.MULTI */
	if ((stateMask & SWT.CTRL) != 0) {
		if ((stateMask & SWT.SHIFT) != 0) {
			int visibleItemCount = (getClientArea().height - getHeaderHeight()) / itemHeight;
			if (availableItems.length <= topIndex + visibleItemCount) return;	/* at bottom */
			topIndex++;
			getVerticalBar().setSelection(topIndex);
			redraw();
			return;
		} else {
			int focusIndex = focusItem.availableIndex; 
			if (focusIndex == availableItems.length - 1) return;	/* at bottom */
			TreeItem2 newFocusItem = availableItems[focusIndex + 1];
			setFocusItem(newFocusItem, true);
			showItem(newFocusItem);
			redrawItem(newFocusItem.availableIndex);
			return;
		}
	} else {
		// TODO study Shift behaviour
	}
}
void doArrowLeft(int stateMask) {
	if ((stateMask & SWT.CTRL) != 0) {
		// TODO scroll left
		return;
	}
	if (focusItem.getExpanded()) {
		focusItem.setExpanded(false);
		Event newEvent = new Event();
		newEvent.item = focusItem;
		sendEvent(SWT.Collapse, newEvent);
	} else {
		TreeItem2 parentItem = focusItem.getParentItem();
		if (parentItem != null) {
			selectItem(parentItem, false);
			setFocusItem(parentItem, true);
			Event newEvent = new Event();
			newEvent.item = this;
			sendEvent(SWT.Selection, newEvent);
			showItem(parentItem);
			redrawItem(parentItem.availableIndex);
		}
	}
}
void doArrowRight(int stateMask) {
	if ((stateMask & SWT.CTRL) != 0) {
		// TODO scroll right
		return;
	}
	TreeItem2[] children = focusItem.getItems();
	if (children.length == 0) return;
	if (!focusItem.getExpanded()) {
		focusItem.setExpanded(true);
		Event newEvent = new Event();
		newEvent.item = focusItem;
		inExpand = true;
		sendEvent(SWT.Expand, newEvent);
		inExpand = false;
		if (focusItem.getItemCount() == 0) {
			focusItem.expanded = false;
		}
	} else {
		selectItem(children[0], false);
		setFocusItem(children[0], true);
		Event newEvent = new Event();
		newEvent.item = children[0];
		sendEvent(SWT.Selection, newEvent);
		showItem(children[0]);
		redrawItem(children[0].availableIndex);	
	}
}
void doArrowUp(int stateMask) {
	if ((stateMask & (SWT.CTRL | SWT.SHIFT)) == 0) {
		int newFocusIndex = focusItem.availableIndex - 1;
		if (newFocusIndex < 0) return; 		/* at top */
		TreeItem2 item = availableItems[newFocusIndex];
		selectItem(item, false);
		setFocusItem(item, true);
		Event newEvent = new Event();
		newEvent.item = item;
		sendEvent(SWT.Selection, newEvent);
		showItem(item);
		redrawItem(newFocusIndex);
		return;
	}
	if ((style & SWT.SINGLE) != 0) {
		if ((stateMask & SWT.CTRL) != 0) {
			if (topIndex == 0) return;	/* at top */
			topIndex--;
			getVerticalBar().setSelection(topIndex);
			redraw();
			return;
		} else {
			int newFocusIndex = focusItem.availableIndex - 1;
			if (newFocusIndex < 0) return; 	/* at top */
			TreeItem2 item = availableItems[newFocusIndex];
			selectItem(item, false);
			setFocusItem(item, true);
			Event newEvent = new Event();
			newEvent.item = item;
			sendEvent(SWT.Selection, newEvent);
			showItem(item);
			redrawItem(newFocusIndex);
			return;
		}
	}
	/* SWT.MULTI */
	if ((stateMask & SWT.CTRL) != 0) {
		if ((stateMask & SWT.SHIFT) != 0) {
			if (topIndex == 0) return;	/* at top */
			topIndex--;
			getVerticalBar().setSelection(topIndex);
			redraw();
			return;
		} else {
			int focusIndex = focusItem.availableIndex; 
			if (focusIndex == 0) return;	/* at top */
			TreeItem2 newFocusItem = availableItems[focusIndex - 1];
			setFocusItem(newFocusItem, true);
			showItem(newFocusItem);
			redrawItem(newFocusItem.availableIndex);
			return;
		}
	} else {
		// TODO study Shift behaviour
	}
}
void doCR() {
	if (focusItem == null) return;
	Event event = new Event();
	event.item = focusItem;
	sendEvent(SWT.DefaultSelection, event);
}
void doDispose() {
	if (isDisposed()) return;
	for (int i = 0; i < items.length; i++) {
		items[i].dispose(false);
	}
	for (int i = 0; i < columns.length; i++) {
		columns[i].dispose(false);
	}
	availableItems = null;
	columns = null;
	focusItem = null;
	header = null;
	items = null;
	selectedItems = null;		
}
void doEnd(int stateMask) {
	if ((stateMask & (SWT.CTRL | SWT.SHIFT)) == 0) {
		int lastAvailableIndex = availableItems.length - 1;
		if (focusItem.availableIndex == lastAvailableIndex) return; 	/* at bottom */
		TreeItem2 item = availableItems[lastAvailableIndex]; 
		selectItem(item, false);
		setFocusItem(item, true);
		Event newEvent = new Event();
		newEvent.item = item;
		sendEvent(SWT.Selection, newEvent);
		showItem(item);
		redrawItem(lastAvailableIndex);
		return;
	}
	if ((style & SWT.SINGLE) != 0) {
		if ((stateMask & SWT.CTRL) != 0) {
			int visibleItemCount = (getClientArea().height - getHeaderHeight()) / itemHeight;
			setTopItem(availableItems[availableItems.length - visibleItemCount]);
			return;
		} else {
			int lastAvailableIndex = availableItems.length - 1;
			if (focusItem.availableIndex == lastAvailableIndex) return; /* at bottom */
			TreeItem2 item = availableItems[lastAvailableIndex]; 
			selectItem(item, false);
			setFocusItem(item, true);
			Event newEvent = new Event();
			newEvent.item = item;
			sendEvent(SWT.Selection, newEvent);
			showItem(item);
			redrawItem(lastAvailableIndex);
			return;
		}
	}
	/* SWT.MULTI */
	if ((stateMask & SWT.CTRL) != 0) {
		if ((stateMask & SWT.SHIFT) != 0) {
			// TODO scroll to bottom, fire selection (?) but don't change it
		} else {
			int lastAvailableIndex = availableItems.length - 1;
			if (focusItem.availableIndex == lastAvailableIndex) return; /* at bottom */
			TreeItem2 item = availableItems[lastAvailableIndex];
			setFocusItem(item, true);
			showItem(item);
			redrawItem(item.availableIndex);
			return;
		}
	} else {
		// TODO select anchor -> bottom
	}
}
void doFocusIn() {
	if (getItemCount() == 0) return;
	if (focusItem == null) {
		TreeItem2 item = availableItems[0];
		selectItem(item, false);
		setFocusItem(item, false);
		Event newEvent = new Event();
		newEvent.item = item;
		sendEvent(SWT.Selection, newEvent);
		showItem(item);
	}
	redrawItem(focusItem.availableIndex);
}
void doFocusOut() {
	if (focusItem != null) {
		redrawItem(focusItem.availableIndex);
	}
}
void doHome(int stateMask) {
	if ((stateMask & (SWT.CTRL | SWT.SHIFT)) == 0) {
		if (focusItem.availableIndex == 0) return; 		/* at top */
		TreeItem2 item = availableItems[0];
		selectItem(item, false);
		setFocusItem(item, true);
		Event newEvent = new Event();
		newEvent.item = item;
		sendEvent(SWT.Selection, newEvent);
		showItem(item);
		redrawItem(0);
		return;
	}
	if ((style & SWT.SINGLE) != 0) {
		if ((stateMask & SWT.CTRL) != 0) {
			setTopItem(availableItems[0]);
			return;
		} else {
			if (focusItem.availableIndex == 0) return; 		/* at top */
			TreeItem2 item = availableItems[0];
			selectItem(item, false);
			setFocusItem(item, true);
			Event newEvent = new Event();
			newEvent.item = item;
			sendEvent(SWT.Selection, newEvent);
			showItem(item);
			redrawItem(0);
			return;
		}
	}
	/* SWT.MULTI */
	if ((stateMask & SWT.CTRL) != 0) {
		if ((stateMask & SWT.SHIFT) != 0) {
			// TODO scroll to top, fire selection (?) but don't change it
		} else {
			if (focusItem.availableIndex == 0) return; /* at top */
			TreeItem2 item = availableItems[0];
			setFocusItem(item, true);
			showItem(item);
			redrawItem(item.availableIndex);
			return;
		}
	} else {
		// TODO select anchor -> top
	}
}
void doKeyDown(Event event) {
	if (focusItem == null) return;
	switch (event.keyCode) {
		case SWT.ARROW_UP:
			doArrowUp(event.stateMask);
			break;
		case SWT.ARROW_DOWN:
			doArrowDown(event.stateMask);
			break;
		case SWT.ARROW_LEFT:
			doArrowLeft(event.stateMask);
			break;
		case SWT.ARROW_RIGHT:
			doArrowRight(event.stateMask);
			break;			
		case SWT.PAGE_UP:
			doPageUp(event.stateMask);
			break;		
		case SWT.PAGE_DOWN:
			doPageDown(event.stateMask);
			break;
		case SWT.HOME:
			doHome(event.stateMask);
			break;
		case SWT.END:
			doEnd(event.stateMask);
			break;
	}
	if (event.character == ' ') doSpace();
	if (event.character == SWT.CR) doCR();
}
void doMouseDoubleClick(Event event) {
	if (!isFocusControl()) setFocus();
	int index = (event.y - getHeaderHeight()) / itemHeight;
	if (!(0 <= index && index < availableItems.length)) return;	/* not on an available item */
	TreeItem2 selectedItem = availableItems[index];

	/* if click was in expander box then don't fire event */
	if (selectedItem.getItemCount() > 0 && selectedItem.getExpanderBounds().contains(event.x, event.y)) {
		return;
	}
	
	if (!selectedItem.getHitBounds().contains(event.x, event.y)) return;
	
	Event newEvent = new Event();
	newEvent.item = selectedItem;
	sendEvent(SWT.DefaultSelection, newEvent);
}
void doMouseDown(Event event) {
	if (!isFocusControl()) setFocus();
	int index = (event.y - getHeaderHeight()) / itemHeight + topIndex;
	if (!(0 <= index && index < availableItems.length)) return;	/* not on an available item */
	TreeItem2 selectedItem = availableItems[index];
	
	/* if click was in expander box */
	if (selectedItem.getItemCount() > 0 && selectedItem.getExpanderBounds().contains(event.x, event.y)) {
		if (event.button != 1) return;
		boolean expand = !selectedItem.getExpanded();
		selectedItem.setExpanded(expand);
		Event newEvent = new Event();
		newEvent.item = selectedItem;
		if (expand) {
			inExpand = true;
			sendEvent(SWT.Expand, newEvent);
			inExpand = false;
			if (focusItem != null && focusItem.getItemCount() == 0) {
				focusItem.expanded = false;
			}
		} else {
			sendEvent(SWT.Collapse, newEvent);
		}
		return;
	}
	/* if click was in checkbox */
	if ((style & SWT.CHECK) != 0 && selectedItem.getCheckboxBounds().contains(event.x, event.y)) {
		if (event.button != 1) return;
		selectedItem.setChecked(!selectedItem.getChecked());
		Event newEvent = new Event();
		newEvent.item = selectedItem;
		newEvent.detail = SWT.CHECK;
		sendEvent(SWT.Selection, newEvent);
		return;
	}
	
	if (!selectedItem.getHitBounds().contains(event.x, event.y)) return;
	
	if ((style & SWT.SINGLE) != 0) {
		if (!selectedItem.selected) {
			if (event.button == 1) {
				selectItem(selectedItem, false);
				setFocusItem(selectedItem, true);
				Event newEvent = new Event();
				newEvent.item = selectedItem;
				sendEvent(SWT.Selection, newEvent);
				redrawItem(selectedItem.availableIndex);
			} else {
				if ((event.stateMask & (SWT.CTRL | SWT.SHIFT)) == 0) {
					selectItem(selectedItem, false);
					setFocusItem(selectedItem, true);
					Event newEvent = new Event();
					newEvent.item = selectedItem;
					sendEvent(SWT.Selection, newEvent);
					redrawItem(selectedItem.availableIndex);
				}
			}
		} else {	/* item is selected */
			if (event.button == 1) {
				/* fire a selection event, though the selection did not change */
				Event newEvent = new Event();
				newEvent.item = selectedItem;
				sendEvent(SWT.Selection, newEvent);
			}
		}
	} else {	/* SWT.MULTI */
		if (!selectedItem.selected) {
			if (event.button == 1) {
				if ((event.stateMask & (SWT.CTRL | SWT.SHIFT)) == SWT.SHIFT) {
					// TODO do shift-select
				} else {
					selectItem(selectedItem, (event.stateMask & SWT.CTRL) != 0);
					setFocusItem(selectedItem, true);
					Event newEvent = new Event();
					newEvent.item = selectedItem;
					sendEvent(SWT.Selection, newEvent);
					redrawItem(selectedItem.availableIndex);
				}
			} else {	/* button 3 */
				if ((event.stateMask & (SWT.CTRL | SWT.SHIFT)) == 0) {
					selectItem(selectedItem, false);
					setFocusItem(selectedItem, true);
					Event newEvent = new Event();
					newEvent.item = selectedItem;
					sendEvent(SWT.Selection, newEvent);
					redrawItem(selectedItem.availableIndex);
				}
			}
		} else {	/* item is selected */
			if (event.button != 1) return;
			if ((event.stateMask & SWT.CTRL) != 0) {
				removeSelectedItem(getSelectionIndex(selectedItem));
				setFocusItem(selectedItem, true);
				redrawItem(selectedItem.availableIndex);
				Event newEvent = new Event();
				newEvent.item = selectedItem;
				sendEvent(SWT.Selection, newEvent);
			} else {
				if ((event.stateMask & SWT.SHIFT) != 0) {
					// TODO study this
				} else {
					selectItem(selectedItem, false);
					setFocusItem(selectedItem, true);
					Event newEvent = new Event();
					newEvent.item = selectedItem;
					sendEvent(SWT.Selection, newEvent);
					redrawItem(selectedItem.availableIndex);
				}
			}
		}
	}		
}
void doPageDown(int stateMask) {
	if ((stateMask & (SWT.CTRL | SWT.SHIFT)) == 0) {
		int visibleItemCount = (getClientArea().height - getHeaderHeight()) / itemHeight;
		int newFocusIndex = focusItem.availableIndex + visibleItemCount - 1;
		newFocusIndex = Math.min(newFocusIndex, availableItems.length - 1);
		TreeItem2 item = availableItems[newFocusIndex];
		selectItem(item, false);
		setFocusItem(item, true);
		showItem(item);
		redrawItem(item.availableIndex);
		return;
	}
	// TODO handle modifier key cases
}
void doPageUp(int stateMask) {
	if ((stateMask & (SWT.CTRL | SWT.SHIFT)) == 0) {
		int visibleItemCount = (getClientArea().height - getHeaderHeight()) / itemHeight;
		int newFocusIndex = focusItem.availableIndex - visibleItemCount + 1;
		newFocusIndex = Math.max(newFocusIndex, 0);
		TreeItem2 item = availableItems[newFocusIndex];
		selectItem(item, false);
		setFocusItem(item, true);
		showItem(item);
		redrawItem(item.availableIndex);
		return;
	}
	// TODO handle modifier key cases
}
void doPaint (Event event) {
	GC gc = event.gc;
	Rectangle clipping = gc.getClipping ();
	int numColumns = getColumnCount();
	int startColumn = -1, endColumn = -1;
	if (numColumns > 0) {
		startColumn = computeColumnIntersect(clipping.x, 0);
		if (startColumn != -1) {	/* the click fell within a column's bounds */
			endColumn = computeColumnIntersect(clipping.x + clipping.width, startColumn);
			if (endColumn == -1) endColumn = numColumns - 1;
		}
	} else {
		startColumn = endColumn = 0;
	}
	
	/* repaint grid lines if necessary */
	if (linesVisible) {
		Color oldForeground = gc.getForeground();
		if (numColumns > 0 && startColumn != -1) {
			gc.setForeground(LineColor);
			/* vertical column lines */
			for (int i = startColumn; i <= endColumn; i++) {
				int x = columns[i].getRightmostX() - 1;
				gc.drawLine(x, clipping.y, x, clipping.y + clipping.height);
			}
		}
		/* horizontal item lines */
		int bottomY = clipping.y + clipping.height;
		int rightX = clipping.x + clipping.width;
		int headerHeight = getHeaderHeight();
		int y = (clipping.y - headerHeight) / itemHeight * itemHeight + headerHeight;
		while (y <= bottomY) {
			gc.drawLine(clipping.x, y, rightX, y);
			y += itemHeight;
		}
		gc.setForeground(oldForeground);
	}
	
	/* Determine the TreeItems to be painted */
	int startIndex = (clipping.y - getHeaderHeight()) / itemHeight + topIndex;
	if (!(0 <= startIndex && startIndex < availableItems.length)) return;	/* no items to paint */
	/* the -1 below is needed because index ranges are inclusive */
	int endIndex = startIndex + Compatibility.ceil (clipping.height, itemHeight) - 1;
	endIndex = Math.min (endIndex, availableItems.length - 1);
	int current = 0;
	for (int i = startIndex; i <= endIndex; i++) {
		TreeItem2 item = availableItems[i];
		if (startColumn == -1) {
			/* indicates that region to paint is to the right of the last column */
			item.paint(gc, null, false);
		} else {
			if (numColumns == 0) {
				item.paint(gc, null, true);
			} else {
				for (int j = startColumn; j <= endColumn; j++) {
					item.paint(gc, columns[j], true);
				}
			}
		}
		if (isFocusControl() && focusItem == item) {
			int oldStyle = gc.getLineStyle();
			gc.setLineStyle(SWT.LINE_DOT);
			Rectangle focusBounds = item.getFocusBounds();
			gc.drawFocus(focusBounds.x, focusBounds.y, focusBounds.width, focusBounds.height);
			gc.setLineStyle(oldStyle);
		}
	}
}
void doResize (Event event) {
	Rectangle clientArea = getClientArea();
	int value = clientArea.height / itemHeight;
	ScrollBar vBar = getVerticalBar();
	vBar.setThumb(value);
	vBar.setPageIncrement(value);
	ScrollBar hBar = getHorizontalBar();
	hBar.setThumb(clientArea.width);
	hBar.setPageIncrement(clientArea.width);
	int headerHeight = Math.max(fontHeight, headerImageHeight) + 2 * getHeaderPadding();
	header.setSize(clientArea.width, headerHeight);
}
void doScrollHorizontal(Event event) {
	int newSelection = getHorizontalBar().getSelection();
	Rectangle clientArea = getClientArea();
	GC gc = new GC(this);
	gc.copyArea(
		0, 0,
		clientArea.width, clientArea.height,
		horizontalOffset - newSelection, 0);
	gc.dispose();
	if (header.isVisible()) {
		clientArea = header.getClientArea();
		gc = new GC(header);
		gc.copyArea(
			0, 0,
			clientArea.width, clientArea.height,
			horizontalOffset - newSelection, 0);
		gc.dispose();
	}
	horizontalOffset = newSelection;
}
void doScrollVertical(Event event) {
	int newSelection = getVerticalBar().getSelection();
	Rectangle clientArea = getClientArea();
	GC gc = new GC(this);
	gc.copyArea(
		0, 0,
		clientArea.width, clientArea.height,
		0, (topIndex - newSelection) * itemHeight);
	gc.dispose();
	topIndex = newSelection;
}
void doSpace() {
	if (focusItem == null) return;
	if (!focusItem.selected) {
		selectItem(focusItem, (style & SWT.MULTI) != 0);
		showItem(focusItem);
		redrawItem(focusItem.availableIndex);
	}
	Event event = new Event();
	event.item = focusItem;
	sendEvent(SWT.Selection, event);
}
int getCellPadding() {
	return MARGIN_CELL + WIDTH_CELL_HIGHLIGHT; 
}
public Control [] getChildren() {
	checkWidget();
	Control[] controls = _getChildren();
	if (header == null) return controls;
	Control[] result = new Control[controls.length - 1];
	/* remove the Header from the returned set of children */
	int index = 0;
	for (int i = 0; i < controls.length; i++) {
		 if (controls[i] != header) {
		 	result[index++] = controls[i];
		 }
	}
	return result;
}
public TreeColumn getColumn(int index) {
	// TODO public?
	checkWidget();
	if (!(0 <= index && index < columns.length)) error(SWT.ERROR_INVALID_RANGE);
	return columns[index];
}
public int getColumnCount() {
	// TODO public?
	checkWidget();
	return columns.length;
}
public TreeColumn[] getColumns() {
	// TODO public?
	checkWidget();
	TreeColumn[] result = new TreeColumn[columns.length];
	System.arraycopy(columns, 0, result, 0, columns.length);
	return result;
}
int getHeaderHeight() {
	if (!header.getVisible()) return 0;
	return header.getSize().y;
}
int getHeaderPadding() {
	return MARGIN_CELL + WIDTH_HEADER_SHADOW; 
}
public boolean getHeaderVisible () {
	checkWidget();
	return header.getVisible();
}
public TreeItem2 getItem(Point point) {
	checkWidget();
	int index = (point.y - getHeaderHeight()) / itemHeight - topIndex;
	if (availableItems.length < index + 1) return null;		/* below the last item */
	TreeItem2 result = availableItems[index];
	if (!result.getHitBounds().contains(point)) return null;	/* considers the x value */
	return result;
}
public int getItemCount() {
	checkWidget();
	return items.length;
}
public int getItemHeight() {
	checkWidget();
	return itemHeight;
}
public TreeItem2 [] getItems() {
	checkWidget();
	TreeItem2 result[] = new TreeItem2[items.length];
	System.arraycopy(items, 0, result, 0, items.length);
	return result;	
}
/*
 * Returns the current y-coordinate that the specified item should have. 
 */
int getItemY(TreeItem2 item) {
	int index = item.availableIndex;
	if (index == -1) return -1;
	return (index - topIndex) * itemHeight + getHeaderHeight();
}
public boolean getLinesVisible () {
	checkWidget();
	return linesVisible;
}
public TreeItem2 getParentItem() {
	checkWidget();
	return null;
}
public TreeItem2[] getSelection() {
	checkWidget();
	TreeItem2[] result = new TreeItem2[selectedItems.length];
	System.arraycopy(selectedItems, 0, result, 0, selectedItems.length);
	return result;
}
public int getSelectionCount() {
	checkWidget();
	return selectedItems.length;
}
int getSelectionIndex(TreeItem2 item) {
	for (int i = 0; i < selectedItems.length; i++) {
		if (selectedItems[i] == item) return i;
	}
	return -1;
}
public TreeItem2 getTopItem() {
	checkWidget();
	if (availableItems.length == 0) return null;
	return availableItems[topIndex];
}
void handleEvents (Event event) {
	switch (event.type) {
		case SWT.Paint:
			if (event.widget == header) {
				headerDoPaint(event);
			} else {
				doPaint(event);
			}
			break;
		case SWT.MouseDown:
			if (event.widget == header) {
				headerDoMouseDown(event);
			} else {
				doMouseDown(event);
			}
			break;
		case SWT.MouseUp:
			headerDoMouseUp(event); break;
		case SWT.MouseMove:
			headerDoMouseMove(event); break;
		case SWT.MouseDoubleClick:
			doMouseDoubleClick(event); break;
		case SWT.MouseExit:
			headerDoMouseExit(); break;
		case SWT.Dispose:
			doDispose(); break;		
		case SWT.KeyDown:
			doKeyDown(event); break;
		case SWT.Resize:
			doResize(event); break;
		case SWT.Selection:
			if (event.widget == getVerticalBar()) {
				doScrollVertical(event);
			} else {
				doScrollHorizontal(event);
			}
			break;
		case SWT.FocusOut:
			doFocusOut(); break;
		case SWT.FocusIn:
			doFocusIn(); break;	
		case SWT.Traverse:
			switch (event.detail) {
				case SWT.TRAVERSE_ESCAPE:
				case SWT.TRAVERSE_RETURN:
				case SWT.TRAVERSE_TAB_NEXT:
				case SWT.TRAVERSE_TAB_PREVIOUS:
				case SWT.TRAVERSE_PAGE_NEXT:
				case SWT.TRAVERSE_PAGE_PREVIOUS:
					event.doit = true;
					break;
			}
			break;			
	}
}
void headerDoMouseDown(Event event) {
	if (event.button != 1) return;
	for (int i = 0; i < columns.length; i++) {
		TreeColumn column = columns[i]; 
		int x = column.getRightmostX();
		/* if close to a column separator line then prepare for column resize */
		if (Math.abs (x - event.x) <= TOLLERANCE_COLUMNRESIZE) {
			if (!column.getResizable()) return;
			resizeColumn = column;
			resizeColumnX = x;
			return;
		}
		/* if within column but not near separator line then fire column Selection */
		if (event.x < x) {
			Event newEvent = new Event();
			newEvent.widget = column;
			sendEvent(SWT.Selection, newEvent);
			return;
		}
	}
}
void headerDoMouseExit() {
	if (resizeColumn != null) return;
	setCursor(null);	/* ensure that a column resize cursor does not escape */
}
void headerDoMouseMove(Event event) {
	/* not currently resizing a column */
	if (resizeColumn == null) {
		for (int i = 0; i < columns.length; i++) {
			TreeColumn column = columns[i]; 
			int x = column.getRightmostX();
			if (Math.abs (x - event.x) <= TOLLERANCE_COLUMNRESIZE) {
				if (column.getResizable()) {
					setCursor(ResizeCursor);
				} else {
					setCursor(null);
				}
				return;
			}
		}
		setCursor(null);
		return;
	}
	
	/* currently resizing a column */
	
	/* don't allow the resize x to move left of the column's x position */
	if (event.x <= resizeColumn.getX()) return;

	/* redraw the resizing line at its new location */
	GC gc = new GC(this);
	int lineHeight = getClientArea().height;
	redraw(resizeColumnX - 1, 0, 1, lineHeight, false);
	resizeColumnX = event.x;
	gc.drawLine(resizeColumnX - 1, 0, resizeColumnX - 1, lineHeight);
	gc.dispose();
	
}
void headerDoMouseUp(Event event) {
	if (resizeColumn == null) return;
	int newWidth = resizeColumnX - resizeColumn.getX();
	if (newWidth != resizeColumn.getWidth()) {
		setCursor(null);
		updateColumnWidth(resizeColumn, newWidth);
		Event newEvent = new Event();
		event.widget = resizeColumn;
		sendEvent(SWT.Resize, newEvent);
	} else {
		/* remove the resize line */
		GC gc = new GC(this);
		int lineHeight = getClientArea().height;
		redraw(resizeColumnX - 1, 0, 1, lineHeight, false);
		gc.dispose();
	}
	resizeColumnX = -1;
	resizeColumn = null;
}
void headerDoPaint(Event event) {
	int numColumns = getColumnCount();
	GC gc = event.gc;
	Rectangle clipping = gc.getClipping ();
	int startColumn = -1, endColumn = -1;
	if (numColumns > 0) {
		startColumn = computeColumnIntersect(clipping.x, 0);
		if (startColumn != -1) {	/* the click fell within a column's bounds */
			endColumn = computeColumnIntersect(clipping.x + clipping.width, startColumn);
			if (endColumn == -1) endColumn = numColumns - 1;
		}
	} else {
		startColumn = endColumn = 0;
	}
	
	/* paint the column header shadow that spans the full header width */
	Rectangle paintBounds = new Rectangle (clipping.x, 0, clipping.width, getSize().y);
	headerPaintShadow(gc, paintBounds, true, false);
	
	/* if damage occurred to the right of the last column then finished */
	if (startColumn == -1) return;
	
	/* paint each of the column headers */
	int headerHeight = getHeaderHeight ();
	if (numColumns > 0) {
		int padding = getHeaderPadding();
		int twoPaddings = padding + padding; 
		for (int i = startColumn; i <= endColumn; i++) {
			headerPaintShadow(gc, columns[i].getBounds(), false, true);
			columns[i].paint(gc);
		}
	} else {
		// TODO paint left edge only?  Seems good as-is...
	}
}
void headerPaintShadow(GC gc, Rectangle bounds, boolean paintHorizontalLines, boolean paintVerticalLines) {
	Color oldForeground = gc.getForeground();
	
	/* draw highlight shadow */
	gc.setForeground(HighlightShadowColor);
	if (paintHorizontalLines) {
		int endX = bounds.x + bounds.width;
		gc.drawLine(bounds.x, bounds.y, endX, bounds.y);
	}
	if (paintVerticalLines) {
		gc.drawLine(bounds.x, bounds.y, bounds.x, bounds.y + bounds.height - 1);
	}
	
	/* draw lowlight shadow */
	Point bottomShadowStart = new Point(bounds.x + 1, bounds.height - 2);
	Point bottomShadowStop = new Point(bottomShadowStart.x + bounds.width - 2, bottomShadowStart.y);	

	/* light inner shadow */
	gc.setForeground(NormalShadowColor);
	if (paintHorizontalLines) {
		gc.drawLine(
			bottomShadowStart.x, bottomShadowStart.y,
			bottomShadowStop.x, bottomShadowStop.y);
	}
	Point rightShadowStart = new Point(bounds.x + bounds.width - 2, bounds.y + 1);
	Point rightShadowStop = new Point(rightShadowStart.x, bounds.height - 2);
	if (paintVerticalLines) {
		gc.drawLine(
			rightShadowStart.x, rightShadowStart.y,
			rightShadowStop.x, rightShadowStop.y);
	}

	/* dark outer shadow */ 
	gc.setForeground(display.getSystemColor(SWT.COLOR_WIDGET_DARK_SHADOW));
	--bottomShadowStart.x;
	++bottomShadowStart.y;
	++bottomShadowStop.y;
	
	if (paintHorizontalLines) {
		gc.drawLine(
			bottomShadowStart.x, bottomShadowStart.y,
			bottomShadowStop.x, bottomShadowStop.y);
	}
	if (paintVerticalLines) {
		gc.drawLine(
			rightShadowStart.x + 1, rightShadowStart.y - 1,
			rightShadowStop.x + 1, rightShadowStop.y + 1);
	}
	
	gc.setForeground(oldForeground);
}
/*
 * Sets the collection of selected items in the receiver.  Clients should typically
 * change the selection through #selectItem(...) unless they know that the previous
 * selected items do not need to be visually updated right now.
 */
void internalSetSelection(TreeItem2[] items) {
	for (int i = 0; i < selectedItems.length; i++) {
		selectedItems[i].selected = false;
	}
	for (int i = 0; i < items.length; i++) {
		items[i].selected = true;
	}
	selectedItems = items;

}
/*
 * Allows the Tree to update internal structures it has that may contain the
 * item that is about to be disposed.  The argument is not necessarily a root-level
 * item.
 */
void itemDisposing(TreeItem2 item) {
	int availableIndex = item.availableIndex; 
	if (availableIndex != -1) {
		TreeItem2[] newAvailableItems = new TreeItem2[availableItems.length - 1];
		System.arraycopy(availableItems, 0, newAvailableItems, 0, availableIndex);
		System.arraycopy(
			availableItems,
			availableIndex + 1,
			newAvailableItems,
			availableIndex,
			newAvailableItems.length - availableIndex);
		availableItems = newAvailableItems;
		/* update the availableIndex on affected items */
		for (int i = availableIndex; i < availableItems.length; i++) {
			availableItems[i].availableIndex = i;
		}
		item.availableIndex = -1;
		updateVerticalBar();
		updateHorizontalBar();
	}
	if (item.selected) {
		int selectionIndex = getSelectionIndex(item);
		TreeItem2[] newSelectedItems = new TreeItem2[selectedItems.length - 1];
		System.arraycopy(selectedItems, 0, newSelectedItems, 0, selectionIndex);
		System.arraycopy(
			selectedItems,
			selectionIndex + 1,
			newSelectedItems,
			selectionIndex,
			newSelectedItems.length - selectionIndex);
		selectedItems = newSelectedItems;
		item.selected = false;
	}
	if (item.isRoot()) {
		int index = item.getIndex();
		TreeItem2[] newItems = new TreeItem2[items.length - 1];
		System.arraycopy(items, 0, newItems, 0, index);
		System.arraycopy(items, index + 1, newItems, index, newItems.length - index);
		items = newItems;
	}
	if (item == focusItem) reassignFocus();
}
/*
 * Important: Assumes that item just became available (ie.- was either created
 * or the parent item was expanded) and the parent is available.
 */
void makeAvailable(TreeItem2 item) {
	TreeItem2 parentItem = item.getParentItem();
	int parentAvailableIndex = parentItem.availableIndex;
	TreeItem2[] parentAvailableDescendents = parentItem.computeAvailableDescendents();
	TreeItem2[] newAvailableItems = new TreeItem2[availableItems.length + 1];
	
	System.arraycopy(availableItems, 0, newAvailableItems, 0, parentAvailableIndex);
	System.arraycopy(parentAvailableDescendents, 0, newAvailableItems, parentAvailableIndex, parentAvailableDescendents.length);
	System.arraycopy(			// TODO ugly!
			availableItems,
			parentAvailableIndex + parentAvailableDescendents.length - 1,
			newAvailableItems,
			parentAvailableIndex + parentAvailableDescendents.length,
			availableItems.length - (parentAvailableIndex + parentAvailableDescendents.length - 1));
	availableItems = newAvailableItems;
	
	/* update availableIndex as needed */
	for (int i = parentAvailableIndex; i < availableItems.length; i++) {
		availableItems[i].availableIndex = i;
	}
	updateVerticalBar();
	updateHorizontalBar();
}

/*
 * Important: Assumes that item is available and its descendents have just become
 * available (ie.- they were either created or the item was expanded).
 */
void makeDescendentsAvailable(TreeItem2 item) {
	int itemAvailableIndex = item.availableIndex;
	TreeItem2[] availableDescendents = item.computeAvailableDescendents();
	TreeItem2[] newAvailableItems = new TreeItem2[availableItems.length + availableDescendents.length - 1];
	
	System.arraycopy(availableItems, 0, newAvailableItems, 0, itemAvailableIndex);
	System.arraycopy(availableDescendents, 0, newAvailableItems, itemAvailableIndex, availableDescendents.length);
	System.arraycopy(			// TODO ugly!
			availableItems,
			itemAvailableIndex + 1,
			newAvailableItems,
			itemAvailableIndex + availableDescendents.length,
			availableItems.length - (itemAvailableIndex + 1));
	availableItems = newAvailableItems;
	
	/* update availableIndex as needed */
	for (int i = itemAvailableIndex; i < availableItems.length; i++) {
		availableItems[i].availableIndex = i;
	}
	
	updateVerticalBar();
	updateHorizontalBar();
}

/*
 * Important: Assumes that item is available and its descendents have just become
 * unavailable (ie.- they were either disposed or the item was collapsed).
 */
void makeDescendentsUnavailable(TreeItem2 item, TreeItem2[] removedDescendents) {
	int descendentsLength = removedDescendents.length;
	TreeItem2[] newAvailableItems = new TreeItem2[availableItems.length - descendentsLength + 1];
	
	System.arraycopy(availableItems, 0, newAvailableItems, 0, item.availableIndex + 1);
	System.arraycopy(			// TODO ugly!
			availableItems,
			item.availableIndex + descendentsLength,
			newAvailableItems,
			item.availableIndex + 1,
			availableItems.length - (item.availableIndex + descendentsLength));
	availableItems = newAvailableItems;
	
	/* update availableIndex as needed */
	for (int i = item.availableIndex; i < availableItems.length; i++) {
		availableItems[i].availableIndex = i;
	}
	
	/* remove the selection from all descendents */
	for (int i = 0; i < selectedItems.length; i++) {
		if (selectedItems[i] != item && selectedItems[i].hasAncestor(item)) {
			removeSelectedItem(i);
		}
	}
	updateVerticalBar();
	updateHorizontalBar();
}
/*
 * Important: Assumes that item just became unavailable (ie.- was either disposed
 * or the parent item was collapsed) and the parent is available.
 */
void makeUnavailable(TreeItem2 item) {
	// TODO currently no references, can probably go away...
	int oldAvailableLength = availableItems.length;
	TreeItem2 parentItem = item.getParentItem();
	TreeItem2[] parentAvailableDescendents = parentItem.computeAvailableDescendents();
	TreeItem2[] newAvailableItems = new TreeItem2[availableItems.length - 1];
	
	System.arraycopy(availableItems, 0, newAvailableItems, 0, parentItem.availableIndex);
	System.arraycopy(parentAvailableDescendents, 0, newAvailableItems, parentItem.availableIndex, parentAvailableDescendents.length);
	System.arraycopy(			// TODO ugly!
		availableItems,
		parentItem.availableIndex + parentAvailableDescendents.length + 1,
		newAvailableItems,
		parentItem.availableIndex + parentAvailableDescendents.length,
		availableItems.length - (parentItem.availableIndex + parentAvailableDescendents.length + 1));
	availableItems = newAvailableItems;
	
	/* update availableIndex as needed */
	for (int i = parentItem.availableIndex; i < availableItems.length; i++) {
		availableItems[i].availableIndex = i;
	}
	
	// TODO handle selection change if needed?
	
	updateVerticalBar();
	updateHorizontalBar();
	redrawItems(item.getParentItem().availableIndex, oldAvailableLength - 1);
}
/*
 * The current focus item is about to become unavailable, so reassign focus.
 */
void reassignFocus() {
	if (focusItem == null) return;
	
	/* reassign to current focus' parent item if it has one */
	if (!focusItem.isRoot()) {
		TreeItem2 item = focusItem.getParentItem();
		setFocusItem(item, false);
		if ((style & SWT.SINGLE) != 0) {
			setSelection(new TreeItem2[] {item});
			Event event = new Event();
			event.item = item;
			sendEvent(SWT.Selection, event);
		}
		showItem(item);
		return;
	}
	
	/* 
	 * reassign to the previous root-level item if there is one, or the next
	 * root-level item otherwise
	 */
	int index = focusItem.getIndex();
	if (index != 0) {
		index--;
	} else {
		index++;
	}
	if (index < items.length) {
		TreeItem2 item = items[index];
		setFocusItem(item, false);
		if ((style & SWT.SINGLE) != 0) {
			setSelection(new TreeItem2[] {item});
			Event event = new Event();
			event.item = item;
			sendEvent(SWT.Selection, event);
		}
		showItem(item);
	} else {
		setFocusItem(null, false);		/* no items left */
	}
}
/* 
 * Redraws from the specified index down to the last available item inclusive.  Note
 * that the redraw bounds do not extend beyond the current last item, so clients
 * that reduce the number of available items should use #redrawItems(int,int) instead
 * to ensure that redrawing extends down to the previous bottom item boundary.
 */
void redrawFromItemDownwards(int index) {
	redrawItems(index, availableItems.length - 1);
}
void redrawHeader() {
	header.redraw();
}
/*
 * Redraws the table at the specified index.  It is valid for this index to reside
 * beyond the last available item in the receiver.
 */
void redrawItem(int index) {
	redrawItems(index,index);
}
/*
 * Redraws the table between the start and end item indices inclusive.  It is valid
 * for the end index value to extend beyond the last available item in the receiver.
 */
void redrawItems(int start, int end) {
	Rectangle bounds = getClientArea();
	int startY = (start - topIndex) * itemHeight + getHeaderHeight();
	int height = (end - start + 1) * itemHeight;
	redraw(0, startY, bounds.width, height, false);
}
public void removeAll() {
	checkWidget();
	setFocusItem (null, false);
	TreeItem2[] items = this.items;
	this.items = new TreeItem2[0];
	selectedItems = new TreeItem2[0];
	availableItems = new TreeItem2[0];
	for (int i = 0; i < items.length; i++) {
		items[i].dispose(false);
	}
	getVerticalBar().setMaximum(1);
	redraw();
}
void removeColumn(TreeColumn column) {
	int numColumns = getColumnCount();
	int index = column.getIndex();

	TreeColumn[] newColumns = new TreeColumn[columns.length - 1];
	System.arraycopy(columns, 0, newColumns, 0, index);
	System.arraycopy(columns, index + 1, newColumns, index, columns.length - index);
	columns = newColumns;
	
	getHorizontalBar().setMaximum(columns[columns.length - 1].getRightmostX());
}
void removeSelectedItem(int index) {
	selectedItems[index].selected = false;
	TreeItem2[] newSelectedItems = new TreeItem2[selectedItems.length - 1];
	System.arraycopy(selectedItems, 0, newSelectedItems, 0, index);
	System.arraycopy(selectedItems, index + 1, newSelectedItems, index, newSelectedItems.length - index);
	selectedItems = newSelectedItems;
}
public void removeSelectionListener(SelectionListener listener) {
	checkWidget();
	if (listener == null) error(SWT.ERROR_NULL_ARGUMENT);
	removeListener (SWT.Selection, listener);
	removeListener (SWT.DefaultSelection, listener);	
}
public void removeTreeListener(TreeListener listener) {
	checkWidget();
	if (listener == null) error(SWT.ERROR_NULL_ARGUMENT);
	removeListener (SWT.Expand, listener);
	removeListener (SWT.Collapse, listener);
}
public void selectAll() {
	checkWidget();
	for (int i = 0; i < availableItems.length; i++) {
		availableItems[i].selected = true;
	}
	redraw();
}
void selectItem(TreeItem2 item, boolean addToSelection) {
	TreeItem2[] oldSelectedItems = selectedItems;
	if (!addToSelection || (style & SWT.SINGLE) != 0) {
		selectedItems = new TreeItem2[] {item};
		for (int i = 0; i < oldSelectedItems.length; i++) {
			if (oldSelectedItems[i] != item) {
				oldSelectedItems[i].selected = false;
				redrawItem(oldSelectedItems[i].availableIndex);
			}
		}
		item.selected = true;
	} else {
		selectedItems = new TreeItem2[selectedItems.length + 1];
		System.arraycopy(oldSelectedItems, 0, selectedItems, 0, oldSelectedItems.length);
		selectedItems[selectedItems.length - 1] = item;
		item.selected = true;
	}
}
void setFocusItem(TreeItem2 item, boolean redrawOldFocus) {
	if (item == focusItem) return;
	TreeItem2 oldFocusItem = focusItem;
	focusItem = item;
	if (redrawOldFocus && oldFocusItem != null) {
		redrawItem(oldFocusItem.availableIndex);
	}
}
public void setFont (Font value) {
	checkWidget();
	Font font = getFont();
	if (font == value) return;
	if (font != null && value.equals (font)) return;
	super.setFont(value);
	
	GC gc = new GC(this);
	gc.setFont(getFont());
	
	/* recompute the receiver's cached font height and item height values */
	fontHeight = gc.getFontMetrics().getHeight();
	itemHeight = Math.max(fontHeight, imageHeight) + 2 * getCellPadding();
	Point headerSize = header.getSize();
	int newHeaderHeight = Math.max(fontHeight, headerImageHeight) + 2 * getHeaderPadding();
	if (headerSize.y != newHeaderHeight) {
		header.setSize(headerSize.x, newHeaderHeight);
	}

	/* 
	 * Notify all columns of the font change so that they can recompute
	 * their cached string widths.
	 */
	for (int i = 0; i < columns.length; i++) {
		columns[i].updateFont(gc);
	}

	/* 
	 * Notify all items of the font change so that those items that
	 * use the receiver's font can recompute their cached string widths.
	 */
	for (int i = 0; i < items.length; i++) {
		items[i].updateFont(gc);
	}
	
	gc.dispose();
	
	if (header.isVisible()) header.redraw();
	redraw();
}
void setHeaderImageHeight(int value) {
	headerImageHeight = value;
	Point headerSize = header.getSize();
	int newHeaderHeight = Math.max(fontHeight, headerImageHeight) + 2 * getHeaderPadding();
	if (headerSize.y != newHeaderHeight) {
		header.setSize(headerSize.x, newHeaderHeight);
	}
}
public void setHeaderVisible (boolean value) {
	checkWidget();
	if (header.getVisible() == value) return;		/* no change */
	header.setVisible(value);
	redraw();
}
void setImageHeight(int value) {
	imageHeight = value;
	itemHeight = Math.max(fontHeight, imageHeight) + 2 * getCellPadding();
}
public void setInsertMark(TreeItem2 item, boolean before) {
	checkWidget();
	// TODO Tree.setInsertMark()
}
public void setLinesVisible (boolean value) {
	checkWidget();
	if (linesVisible == value) return;		/* no change */
	linesVisible = value;
	redraw();
}
public void setSelection(TreeItem2[] items) {
	checkWidget();
	TreeItem2[] oldSelection = selectedItems;
	internalSetSelection(items);
	for (int i = 0; i < oldSelection.length; i++) {
		if (!oldSelection[i].selected) {
			redrawItem(oldSelection[i].availableIndex);
		}
	}
	for (int i = 0; i < items.length; i++) {
		redrawItem(items[i].availableIndex);
	}
}
public void setTopItem(TreeItem2 item) {
	checkWidget();
	if (item == null) error(SWT.ERROR_NULL_ARGUMENT);
	if (item.isDisposed()) error(SWT.ERROR_INVALID_ARGUMENT);
	if (item.getParent() != this) return;

	if (!item.isAvailable()) item.expandAncestors();
	
	int visibleItemCount = (getClientArea().height - getHeaderHeight()) / itemHeight;
	int index = Math.min (item.availableIndex, availableItems.length - visibleItemCount);
	if (topIndex == index) return;
	topIndex = index;
	getVerticalBar().setSelection(topIndex);
	redraw();
}
public void showItem(TreeItem2 item) {
	checkWidget();
	if (item == null) error(SWT.ERROR_NULL_ARGUMENT);
	if (item.isDisposed()) error(SWT.ERROR_INVALID_ARGUMENT);
	if (item.getParent() != this) return;
	
	if (!item.isAvailable()) item.getParentItem().expandAncestors();
	
	int index = item.availableIndex;
	int visibleItemCount = (getClientArea().height - getHeaderHeight()) / itemHeight;
	/* determine if item is already visible */
	if (topIndex <= index && index < topIndex + visibleItemCount) return;
	
	if (index < topIndex) {
		/* item is above current viewport, so show on top*/
		setTopItem(item);
	} else {
		/* item is below current viewport, so show on bottom */
		setTopItem(availableItems[index - visibleItemCount + 1]);
	}
}
public void showSelection() {
	checkWidget();
	if (selectedItems.length == 0) return;
	showItem(selectedItems[0]);
}
void updateColumnWidth (TreeColumn column, int width) {
	int oldWidth = column.getWidth();
	column.width = width;

	Rectangle bounds = getClientArea();
	ScrollBar hBar = getHorizontalBar();
	hBar.setMaximum(columns[columns.length - 1].getRightmostX());
	hBar.setThumb(bounds.width);
	int x = column.getX();
	redraw(x, 0, bounds.width - x, bounds.height, true);
}
/*
 * This is a naive implementation just to make it work.  The args are not currently used.
 */
void updateHorizontalBar() {
	// TODO revisit
	
	/* the horizontal range is never affected by an item change if there are columns */
	if (getColumnCount() > 0) return;
	
	ScrollBar hBar = getHorizontalBar();
	int maxX = 0;
	for (int i = 0; i < availableItems.length; i++) {
		maxX = Math.max (maxX, availableItems[i].getRightmostX());
	}
	
	hBar.setMaximum(maxX);
	int thumb = Math.min(maxX, getClientArea().width);
	hBar.setThumb(thumb);
	
	/* reclaim any space now left on the right */
	if (maxX < horizontalOffset + thumb) {
		horizontalOffset = maxX - thumb;
		hBar.setSelection(horizontalOffset);
		redraw();
	}
	
	/* 
	 * The following is intentionally commented; it may be useful for writing a
	 * less naive implementation.
	 */
	
//		if (nowAvailable) {
//			if (rightX <= hBar.getMaximum()) return;
//			int maximum = Math.max(1, rightX);
//			hBar.setMaximum(maximum);
//			hBar.setThumb(getClientArea().width);
//			return;
//		}
//		
//		/* item has become unavailable */
//		int barMaximum = hBar.getMaximum();
//		if (rightX < barMaximum) return;
//		
//		/* compute new maximum value */
//		int newMaxX = 1;
//		for (int i = 0; i < availableItems.length; i++) {
//			int maxX = availableItems[i].getRightmostX();
//			if (newMaxX < maxX) newMaxX = maxX;
//		}
//		if (newMaxX == barMaximum) return;
//		hBar.setMaximum(newMaxX);
//		hBar.setThumb(getClientArea().width);
//		
//		/* reclaim any space now left on the right side */
//		horizontalOffset += newMaxX - barMaximum;
//		hBar.setSelection(horizontalOffset);
//		redraw();
}
void updateVerticalBar() {
	ScrollBar vBar = getVerticalBar();
	int maximum = Math.max(1,availableItems.length);
	if (maximum == vBar.getMaximum()) return;
	vBar.setMaximum(maximum);
	int thumb = Math.min(maximum, getClientArea().height / itemHeight);
	vBar.setThumb(thumb);
	/* reclaim any space now left on the bottom */
	if (maximum < topIndex + thumb) {
		topIndex = maximum - thumb;
		vBar.setSelection(topIndex);
		redraw();
	}
}
}
