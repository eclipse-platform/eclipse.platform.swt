package org.eclipse.swt.custom;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

/**
*
* A TableEditor is a manager for a Control that appears above a cell in a Table and tracks with the
* moving and resizing of that cell.  It can be used to display a text widget above a cell
* in a Table so that the user can edit the contents of that cell.  It can also be used to display
* a button that can launch a dialog for modifying the contents of the associated cell.
*
* <p> Here is an example of using a TableEditor:
* <code><pre>
* Table table = new Table(parent, SWT.FULL_SELECTION);
* TableEditor editor = new TableEditor (table);
* table.addSelectionListener (new SelectionAdapter() {
*	public void widgetSelected(SelectionEvent e) {
*
*		// Clean up any previous editor control
*		Control oldEditor = editor.getEditor();
*		if (oldEditor != null)
*			oldEditor.dispose();	
*
*		// Identify the selected row
*		int index = table.getSelectionIndex ();
*		if (index == -1) return;
*		TableItem item = table.getItem (index);
*
*		// The control that will be the editor must be a child of the Table
*		Text text = new Text(table, SWT.NONE);
*
*		//The text editor must have the same size as the cell and must
*		//not be any smaller than 50 pixels.
*		editor.horizontalAlignment = SWT.LEFT;
*		editor.grabHorizontal = true;
*		editor.minimumWidth = 50;
*
*		// Open the text editor in the second column of the selected row.
*		editor.setEditor (text, item, 1);
*
*		// Assign focus to the text control
*		text.setFocus ();
*	}
* });
* </pre></code>
*/
public class TableEditor extends ControlEditor {

	Table table;
	TableItem item;
	int column = -1;
	Listener columnListener;
/**
* Creates a TableEditor for the specified Table.
*
* @param table the Table Control above which this editor will be displayed
*
*/
public TableEditor (Table table) {
	super(table);
	this.table = table;
	
	columnListener = new Listener() {
		public void handleEvent(Event e) {
			resize ();
		}
	};

}
Rectangle computeBounds () {
	if (item == null || column == -1 || item.isDisposed()) return new Rectangle(0, 0, 0, 0);
	
	Rectangle cell = item.getBounds(column);
	Rectangle editorRect = new Rectangle(cell.x, cell.y, minimumWidth, cell.height);
	Rectangle area = table.getClientArea();
	if (cell.x < area.x + area.width) {
		if (cell.x + cell.width > area.x + area.width) {
			cell.width = area.width - cell.x;
		}
	}
	
	if (grabHorizontal){
		editorRect.width = Math.max(cell.width, minimumWidth);
	}
	
	if (horizontalAlignment == SWT.RIGHT) {
		editorRect.x += cell.width - editorRect.width;
	} else if (horizontalAlignment == SWT.LEFT) {
		// do nothing - cell.x is the right answer
	} else { // default is CENTER
		editorRect.x += (cell.width - editorRect.width)/2;
	}
	
	return editorRect;
}
/**
 * Removes all associations between the TableEditor and the cell in the table.  The
 * Table and the editor Control are <b>not</b> disposed.
 */
public void dispose () {
	
	if (this.column > -1 && this.column < table.getColumnCount()){
		TableColumn tableColumn = table.getColumn(this.column);
		tableColumn.removeListener(SWT.Resize, columnListener);
		tableColumn.removeListener(SWT.Move, columnListener);
	}

	table = null;
	item = null;
	column = -1;
	
	super.dispose();
}
/**
* Returns the zero based index of the column of the cell being tracked by this editor.
*
* @return the zero based index of the column of the cell being tracked by this editor
*/
public int getColumn () {
	return column;
}
/**
* Returns the TableItem for the row of the cell being tracked by this editor.
*
* @return the TableItem for the row of the cell being tracked by this editor
*/
public TableItem getItem () {
	return item;
}
public void setColumn(int column) {
		
	if (this.column > -1 && this.column < table.getColumnCount()){
		TableColumn tableColumn = table.getColumn(this.column);
		tableColumn.removeListener(SWT.Resize, columnListener);
		tableColumn.removeListener(SWT.Move, columnListener);
		this.column = -1;
	}

	if (column < 0  || column >= table.getColumnCount()) return;	
		
	this.column = column;
	TableColumn tableColumn = table.getColumn(this.column);
	tableColumn.addListener(SWT.Resize, columnListener);
	tableColumn.addListener(SWT.Move, columnListener);
	resize();
}
public void setItem (TableItem item) {	
	this.item = item;
	resize();
}

/**
* Specify the Control that is to be displayed and the cell in the table that it is to be positioned above.
*
* <p>Note: The Control provided as the editor <b>must</b> be created with its parent being the Table control
* specified in the TableEditor constructor.
* 
* @param editor the Control that is displayed above the cell being edited
* @param item the TableItem for the row of the cell being tracked by this editor
* @param column the zero based index of the column of the cell being tracked by this editor
*/
public void setEditor (Control editor, TableItem item, int column) {
	setItem(item);
	setColumn(column);
	setEditor(editor);
}
void resize () {
	if (table.isDisposed()) return;
	if (item == null || item.isDisposed()) return;
	if (column < 0 || column >= table.getItemCount()) return;
	super.resize();
}
}
