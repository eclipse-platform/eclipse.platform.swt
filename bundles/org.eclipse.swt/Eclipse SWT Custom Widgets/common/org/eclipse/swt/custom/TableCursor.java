/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.custom;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.events.*;

/**
 * A TableCursor provides a way for the user to navigate around a Table
 * using the keyboard.  It also provides a mechanism for selecting an
 * individual cell in a table.
 * 
 * <p> Here is an example of using a TableCursor to navigate to a cell and then edit it.
 * 
 * <code><pre>
 *  public static void main(String[] args) {
 *		Display display = new Display();
 *		Shell shell = new Shell(display);
 *		shell.setLayout(new GridLayout());
 *	
 *		// create a a table with 3 columns and fill with data
 *		final Table table = new Table(shell, SWT.BORDER | SWT.MULTI | SWT.FULL_SELECTION);
 *		table.setLayoutData(new GridData(GridData.FILL_BOTH));
 *		TableColumn column1 = new TableColumn(table, SWT.NONE);
 *		TableColumn column2 = new TableColumn(table, SWT.NONE);
 *		TableColumn column3 = new TableColumn(table, SWT.NONE);
 *		for (int i = 0; i &lt; 100; i++) {
 *			TableItem item = new TableItem(table, SWT.NONE);
 *			item.setText(new String[] { "cell "+i+" 0", "cell "+i+" 1", "cell "+i+" 2"});
 *		}
 *		column1.pack();
 *		column2.pack();
 *		column3.pack();
 *	
 *		// create a TableCursor to navigate around the table
 *		final TableCursor cursor = new TableCursor(table, SWT.NONE);
 *		// create an editor to edit the cell when the user hits "ENTER" 
 *		// while over a cell in the table
 *		final ControlEditor editor = new ControlEditor(cursor);
 *		editor.grabHorizontal = true;
 *		editor.grabVertical = true;
 *	
 *		cursor.addSelectionListener(new SelectionAdapter() {
 *			// when the TableEditor is over a cell, select the corresponding row in 
 *			// the table
 *			public void widgetSelected(SelectionEvent e) {
 *				table.setSelection(new TableItem[] {cursor.getRow()});
 *			}
 *			// when the user hits "ENTER" in the TableCursor, pop up a text editor so that 
 *			// they can change the text of the cell
 *			public void widgetDefaultSelected(SelectionEvent e){
 *				final Text text = new Text(cursor, SWT.NONE);
 *				TableItem row = cursor.getRow();
 *				int column = cursor.getColumn();
 *				text.setText(row.getText(column));
 *				text.addKeyListener(new KeyAdapter() {
 *					public void keyPressed(KeyEvent e) {
 *						// close the text editor and copy the data over 
 *						// when the user hits "ENTER"
 *						if (e.character == SWT.CR) {
 *							TableItem row = cursor.getRow();
 *							int column = cursor.getColumn();
 *							row.setText(column, text.getText());
 *							text.dispose();
 *						}
 *						// close the text editor when the user hits "ESC"
 *						if (e.character == SWT.ESC) {
 *							text.dispose();
 *						}
 *					}
 *				});
 *				editor.setEditor(text);
 *				text.setFocus();
 *			}
 *		});
 *		// Hide the TableCursor when the user hits the "CTRL" or "SHIFT" key.
 *		// This alows the user to select multiple items in the table.
 *		cursor.addKeyListener(new KeyAdapter() {
 *			public void keyPressed(KeyEvent e) {
 *				if (e.keyCode == SWT.CTRL || 
 *				    e.keyCode == SWT.SHIFT || 
 *				    (e.stateMask & SWT.CONTROL) != 0 || 
 *				    (e.stateMask & SWT.SHIFT) != 0) {
 *					cursor.setVisible(false);
 *				}
 *			}
 *		});
 *		// Show the TableCursor when the user releases the "SHIFT" or "CTRL" key.
 *		// This signals the end of the multiple selection task.
 *		table.addKeyListener(new KeyAdapter() {
 *			public void keyReleased(KeyEvent e) {
 *				if (e.keyCode == SWT.CONTROL && (e.stateMask & SWT.SHIFT) != 0) return;
 *				if (e.keyCode == SWT.SHIFT && (e.stateMask & SWT.CONTROL) != 0) return;
 *				if (e.keyCode != SWT.CONTROL && (e.stateMask & SWT.CONTROL) != 0) return;
 *				if (e.keyCode != SWT.SHIFT && (e.stateMask & SWT.SHIFT) != 0) return;
 *			
 *				TableItem[] selection = table.getSelection();
 *				TableItem row = (selection.length == 0) ? table.getItem(table.getTopIndex()) : selection[0];
 *				table.showItem(row);
 *				cursor.setSelection(row, 0);
 *				cursor.setVisible(true);
 *				cursor.setFocus();
 *			}
 *		});
 *	
 *		shell.open();
 *		while (!shell.isDisposed()) {
 *			if (!display.readAndDispatch())
 *				display.sleep();
 *		}
 *		display.dispose();
 *	}
 * </pre></code>
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
 */
public class TableCursor extends Canvas {
	Table table;
	int row, column;
	Listener tableListener, resizeListener;
	
	// By default, invert the list selection colors
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
				case SWT.FocusIn :
				case SWT.FocusOut :
					redraw();
					break;
			}
		}
	};
	addListener(SWT.Dispose, listener);
	addListener(SWT.KeyDown, listener);
	addListener(SWT.Paint, listener);
	addListener(SWT.Traverse, listener);
	addListener(SWT.FocusIn, listener);
	addListener(SWT.FocusOut, listener);

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
		TableColumn col = table.getColumn(i);
		col.addListener(SWT.Resize, resizeListener);
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
				TableColumn col = table.getColumn(i);
				col.removeListener(SWT.Resize, resizeListener);
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
        case SWT.ARROW_RIGHT :
        	{	
		        int leadKey = (getStyle() & SWT.RIGHT_TO_LEFT) != 0 ? SWT.ARROW_RIGHT : SWT.ARROW_LEFT;
		        if (event.keyCode == leadKey) {
		           setRowColumn(row, column - 1, true);
		        } else {
		           setRowColumn(row, column + 1, true);
		        }
		        break;
        	}
		case SWT.HOME :
			setRowColumn(0, column, true);
			break;
		case SWT.END :
			{
				int i = table.getItemCount() - 1;
				setRowColumn(i, column, true);
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
	gc.setBackground(getBackground());
	gc.setForeground(getForeground());
	gc.fillRectangle(event.x, event.y, event.width, event.height);
	TableItem item = table.getItem(row);
	int x = 0, y = 0;
	Point size = getSize();
	Image image = item.getImage(column);
	if (image != null) {
		Rectangle imageSize = image.getBounds();
		int imageY = y + (size.y - imageSize.height) / 2;
		gc.drawImage(image, x, imageY);
		x += imageSize.width;
	}
	x += column == 0 ? 2 : 6;
	int textY = y + (size.y - gc.getFontMetrics().getHeight()) / 2;
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
	for (int i = start; i < end; i++) {
		TableItem item = table.getItem(i);
		for (int j = 0; j < columns; j++) {
			Rectangle rect = item.getBounds(j);
			if (rect.y > clientRect.y + clientRect.height)
				return;
			if (rect.contains(pt)) {
				setRowColumn(i, j, true);
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
	checkWidget();
	if (visible) resize();
	super.setVisible(visible);
}

void resize() {
	TableItem item = table.getItem(row);
	setBounds(item.getBounds(column));
}
/**
 * Returns the column over which the TableCursor is positioned.
 *
 * @return the column for the current position
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getColumn() {
	checkWidget();
	return column;
}
/**
 * Returns the row over which the TableCursor is positioned.
 *
 * @return the item for the current position
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public TableItem getRow() {
	checkWidget();
	return table.getItem(row);
}
public void setBackground (Color color) {
	if (color == null) color = getDisplay().getSystemColor(BACKGROUND);
	super.setBackground(color);
	redraw();
}
public void setForeground (Color color) {
	if (color == null) color = getDisplay().getSystemColor(FOREGROUND);
	super.setForeground(color);
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
	if (row < 0
	    || row >= table.getItemCount()
		|| column < 0
		|| column >= table.getColumnCount())
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
	if (row == null
		|| row.isDisposed()
		|| column < 0
		|| column >= table.getColumnCount())
		SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	setRowColumn(table.indexOf(row), column, false);
}
}
