package org.eclipse.swt.custom;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.events.*;

public class TableCursor extends Canvas {
	Table table;
	int row, column;
	Listener tableListener, resizeListener;

public TableCursor(Table parent, int style) {
	super(parent, style);
	table = parent;
	Listener listener = new Listener() {
		public void handleEvent(Event event) {
			switch (event.type) {
				case SWT.Dispose :
					dispose(event);
					break;
				case SWT.KeyDown :
					keyDown(event);
					break;
				case SWT.Paint :
					paint(event);
					break;
				case SWT.Traverse :
					traverse(event);
					break;
			}
		}
	};
	addListener(SWT.Dispose, listener);
	addListener(SWT.KeyDown, listener);
	addListener(SWT.Paint, listener);
	addListener(SWT.Traverse, listener);

	tableListener = new Listener() {
		public void handleEvent(Event event) {
			switch (event.type) {
				case SWT.MouseDown :
					tableMouseDown(event);
					break;
				case SWT.FocusIn :
					tableFocusIn(event);
					break;
			}
		}
	};
	table.addListener(SWT.FocusIn, tableListener);
	table.addListener(SWT.MouseDown, tableListener);

	resizeListener = new Listener() {
		public void handleEvent(Event event) {
			resize();
		}
	};
	int columns = table.getColumnCount();
	for (int i = 0; i < columns; i++) {
		TableColumn column = table.getColumn(i);
		column.addListener(SWT.Resize, resizeListener);
	}
	ScrollBar hBar = table.getHorizontalBar();
	if (hBar != null) {
		hBar.addListener(SWT.Selection, resizeListener);
	}
	ScrollBar vBar = table.getVerticalBar();
	if (vBar != null) {
		vBar.addListener(SWT.Selection, resizeListener);
	}
}

/**
 * Adds the listener to the collection of listeners who will
 * be notified when the receiver's selection changes, by sending
 * it one of the messages defined in the <code>SelectionListener</code>
 * interface.
 * <p>
 * When <code>widgetSelected</code> is called, the item field of the event object is valid.
 * If the reciever has <code>SWT.CHECK</code> style set and the check selection changes,
 * the event object detail field contains the value <code>SWT.CHECK</code>.
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
 * @see #removeSelectionListener
 * @see SelectionEvent
 */
public void addSelectionListener(SelectionListener listener) {
	checkWidget();
	if (listener == null)
		SWT.error(SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener(listener);
	addListener(SWT.Selection, typedListener);
	addListener(SWT.DefaultSelection, typedListener);
}

void dispose(Event event) {
	Display display = getDisplay();
	display.asyncExec(new Runnable() {
		public void run() {
			if (table.isDisposed())
				return;
			table.removeListener(SWT.FocusIn, tableListener);
			table.removeListener(SWT.MouseDown, tableListener);
			int columns = table.getColumnCount();
			for (int i = 0; i < columns; i++) {
				TableColumn column = table.getColumn(i);
				column.removeListener(SWT.Resize, resizeListener);
			}
			ScrollBar hBar = table.getHorizontalBar();
			if (hBar != null) {
				hBar.removeListener(SWT.Selection, resizeListener);
			}
			ScrollBar vBar = table.getVerticalBar();
			if (vBar != null) {
				vBar.removeListener(SWT.Selection, resizeListener);
			}
		}
	});
}

void keyDown(Event event) {
	switch (event.character) {
		case SWT.CR :
			notifyListeners(SWT.DefaultSelection, new Event());
			return;
	}
	switch (event.keyCode) {
		case SWT.ARROW_UP :
			setRowColumn(row - 1, column, true);
			break;
		case SWT.ARROW_DOWN :
			setRowColumn(row + 1, column, true);
			break;
		case SWT.ARROW_LEFT :
			setRowColumn(row, column - 1, true);
			break;
		case SWT.ARROW_RIGHT :
			setRowColumn(row, column + 1, true);
			break;
		case SWT.HOME :
			setRowColumn(0, column, true);
			break;
		case SWT.END :
			{
				int row = table.getItemCount() - 1;
				setRowColumn(row, column, true);
				break;
			}
		case SWT.PAGE_UP :
			{
				int index = table.getTopIndex();
				if (index == row) {
					Rectangle rect = table.getClientArea();
					TableItem item = table.getItem(index);
					Rectangle itemRect = item.getBounds(0);
					rect.height -= itemRect.y;
					int height = table.getItemHeight();
					int page = Math.max(1, rect.height / height);
					index = Math.max(0, index - page + 1);
				}
				setRowColumn(index, column, true);
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
				if (index == row) {
					index = Math.min(end, index + page - 1);
				}
				setRowColumn(index, column, true);
				break;
			}
	}
}

void paint(Event event) {
	GC gc = event.gc;
	Display display = getDisplay();
	gc.setBackground(display.getSystemColor(SWT.COLOR_LIST_SELECTION_TEXT));
	gc.setForeground(display.getSystemColor(SWT.COLOR_LIST_SELECTION));
	gc.fillRectangle(event.x, event.y, event.width, event.height);
	TableItem item = table.getItem(row);
	int x = 0, y = 0;
	Point size = getSize();
	Image image = item.getImage(column);
	if (image != null) {
		Rectangle imageSize = image.getBounds();
		int imageY = y + (int) (((float) size.y - (float) imageSize.height) / 2.0);
		gc.drawImage(image, x, imageY);
		x += imageSize.width;
	}
	x += (column == 0) ? 2 : 6;

	int textY =
		y + (int) (((float) size.y - (float) gc.getFontMetrics().getHeight()) / 2.0);
	gc.drawString(item.getText(column), x, textY);
	if (isFocusControl()) {
		gc.setBackground(display.getSystemColor(SWT.COLOR_BLACK));
		gc.setForeground(display.getSystemColor(SWT.COLOR_WHITE));
		gc.drawFocus(0, 0, size.x, size.y);
	}

}

void tableFocusIn(Event event) {
	if (isDisposed())
		return;
	if (isVisible())
		setFocus();
}

void tableMouseDown(Event event) {
	if (isDisposed() || !isVisible())
		return;
	Point pt = new Point(event.x, event.y);
	Rectangle clientRect = table.getClientArea();
	int columns = table.getColumnCount();
	int start = table.getTopIndex();
	int end = table.getItemCount();
	for (int row = start; row < end; row++) {
		TableItem item = table.getItem(row);
		for (int column = 0; column < columns; column++) {
			Rectangle rect = item.getBounds(column);
			if (rect.y > clientRect.y + clientRect.height)
				return;
			if (rect.contains(pt)) {
				setRowColumn(row, column, true);
				setFocus();
				return;
			}
		}
	}
}

void traverse(Event event) {
	switch (event.detail) {
		case SWT.TRAVERSE_ARROW_NEXT :
		case SWT.TRAVERSE_ARROW_PREVIOUS :
		case SWT.TRAVERSE_RETURN :
			event.doit = false;
			return;
	}
	event.doit = true;
}

void setRowColumn(int row, int column, boolean notify) {
	if (0 <= row && row < table.getItemCount()) {
		if (0 <= column && column < table.getColumnCount()) {
			this.row = row;
			this.column = column;
			TableItem item = table.getItem(row);
			table.showItem(item);
			setBounds(item.getBounds(column));
			redraw();
			if (notify) {
				notifyListeners(SWT.Selection, new Event());
			}
		}
	}
}

public void setVisible(boolean visible) {
	if (visible)
		resize();
	super.setVisible(visible);
}

void resize() {
	TableItem item = table.getItem(row);
	setBounds(item.getBounds(column));
}

public int getColumn() {
	return column;
}
public TableItem getRow() {
	return table.getItem(row);
}
public void setSelection(TableItem row, int column) {
	if (row == null
		|| row.isDisposed()
		|| column < 0
		|| column >= table.getColumnCount())
		SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	setRowColumn(table.indexOf(row), column, false);

}
}