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
*
* A TableTreeEditor is a manager for a Control that appears above a cell in a TableTree
* and tracks with the moving and resizing of that cell.  It can be used to display a
* text widget above a cell in a TableTree so that the user can edit the contents of 
* that cell.  It can also be used to display a button that can launch a dialog for 
* modifying the contents of the associated cell.
*
* <p> Here is an example of using a TableTreeEditor:
* <code><pre>
* public static void main (String [] args) {
* 	Display display = new Display ();
* 	Shell shell = new Shell (display);
* 	final TableTree tableTree = new TableTree(shell, SWT.FULL_SELECTION);
* 	Table table = tableTree.getTable();
* 	table.setLinesVisible(true);
* 	TableColumn column1 = new TableColumn(table, SWT.NONE);
* 	column1.setText("column 1");
* 	TableColumn column2 = new TableColumn(table, SWT.NONE);
* 	column2.setText("column 2");
* 	for (int i = 0; i < 40; i++) {
* 		TableTreeItem item = new TableTreeItem(tableTree, SWT.NONE);
* 		item.setText(0, "table tree item"+i);
* 		item.setText(1, "value "+i);
* 	}
* 	column1.pack();
* 	column2.pack();
* 	final TableTreeEditor editor = new TableTreeEditor (tableTree);
* 	tableTree.addSelectionListener (new SelectionAdapter() {
* 		public void widgetSelected(SelectionEvent e) {
* 			// Clean up any previous editor control
* 			Control oldEditor = editor.getEditor();
* 			if (oldEditor != null)
* 				oldEditor.dispose();	
* 			// Identify the selected row
* 			TableTreeItem[] selection = tableTree.getSelection();
* 			if (selection.length == 0) return;
* 			TableTreeItem item = selection[0];
* 			// The control that will be the editor must be a child of the Table
* 			// that underlies the TableTree
* 			Text text = new Text(tableTree.getTable(), SWT.NONE);
* 			//text.moveAbove(tableTree);
* 			//The text editor must have the same size as the cell and must
* 			//not be any smaller than 50 pixels.
* 			editor.horizontalAlignment = SWT.LEFT;
* 			editor.grabHorizontal = true;
* 			//editor.minimumWidth = 50;
* 			// Open the text editor in the second column of the selected row.
* 			editor.setEditor (text, item, 1);
* 			// Assign focus to the text control
* 			text.setFocus ();
* 		}
* 	});
* 	tableTree.setBounds(10, 10, 200, 400);
* 	shell.open ();
* 	while (!shell.isDisposed ()) {
* 		if (!display.readAndDispatch ()) display.sleep ();
* 	}
* 	display.dispose ();
* }
* </pre></code>
*/
public class TableTreeEditor extends ControlEditor {

	TableTree tableTree;
	TableTreeItem item;
	int column = -1;
	ControlListener columnListener;
	TreeListener treeListener;
/**
* Creates a TableEditor for the specified Table.
*
* @param table the Table Control above which this editor will be displayed
*
*/
public TableTreeEditor (TableTree tableTree) {
	super(tableTree.getTable());
	this.tableTree = tableTree;

	treeListener = new TreeListener () {
		final Runnable runnable = new Runnable() {
			public void run() {
				if (TableTreeEditor.this.tableTree.isDisposed() || editor == null) return;
				resize();
				if (editor == null || editor.isDisposed()) return;
				editor.setVisible(true);
			}
		};
		public void treeCollapsed(TreeEvent e) {
			if (editor == null || editor.isDisposed ()) return;
			editor.setVisible(false);
			Display display = TableTreeEditor.this.tableTree.getDisplay();
			display.asyncExec(runnable);
		}
		public void treeExpanded(TreeEvent e) {
			if (editor == null || editor.isDisposed ()) return;
			editor.setVisible(false);
			Display display = TableTreeEditor.this.tableTree.getDisplay();
			display.asyncExec(runnable);
		}
	};
	tableTree.addTreeListener(treeListener);
	
	columnListener = new ControlListener() {
		public void controlMoved(ControlEvent e){
			resize ();
		}
		public void controlResized(ControlEvent e){
			resize ();
		}
	};
	
	// To be consistent with older versions of SWT, grabVertical defaults to true
	grabVertical = true;
}
Rectangle computeBounds () {
	if (item == null || column == -1 || item.isDisposed() || item.tableItem == null) return new Rectangle(0, 0, 0, 0);
	Rectangle cell = item.getBounds(column);
	Rectangle area = tableTree.getClientArea();
	if (cell.x < area.x + area.width) {
		if (cell.x + cell.width > area.x + area.width) {
			cell.width = area.x + area.width - cell.x;
		}
	}
	Rectangle editorRect = new Rectangle(cell.x, cell.y, minimumWidth, minimumHeight);

	if (grabHorizontal) {
		editorRect.width = Math.max(cell.width, minimumWidth);
	}
	
	if (grabVertical) {
		editorRect.height = Math.max(cell.height, minimumHeight);
	}
	
	if (horizontalAlignment == SWT.RIGHT) {
		editorRect.x += cell.width - editorRect.width;
	} else if (horizontalAlignment == SWT.LEFT) {
		// do nothing - cell.x is the right answer
	} else { // default is CENTER
		editorRect.x += (cell.width - editorRect.width)/2;
	}
	
	if (verticalAlignment == SWT.BOTTOM) {
		editorRect.y += cell.height - editorRect.height;
	} else if (verticalAlignment == SWT.TOP) {
		// do nothing - cell.y is the right answer
	} else { // default is CENTER
		editorRect.y += (cell.height - editorRect.height)/2;
	}
	return editorRect;
}
/**
 * Removes all associations between the TableTreeEditor and the cell in the table tree.  The
 * TableTree and the editor Control are <b>not</b> disposed.
 */
public void dispose () {
	if (treeListener != null) 
		tableTree.removeTreeListener(treeListener);
	treeListener = null;	
	Table table = tableTree.getTable();
	if (this.column > -1 && this.column < table.getColumnCount()){
		TableColumn tableColumn = table.getColumn(this.column);
		tableColumn.removeControlListener(columnListener);
	}
	tableTree = null;
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
* Returns the TableTreeItem for the row of the cell being tracked by this editor.
*
* @return the TableTreeItem for the row of the cell being tracked by this editor
*/
public TableTreeItem getItem () {
	return item;
}
public void setColumn(int column) {
	Table table = tableTree.getTable();
	int columnCount = table.getColumnCount();
	// Separately handle the case where the table has no TableColumns.
	// In this situation, there is a single default column.
	if (columnCount == 0) {
		this.column = (column == 0) ? 0 : -1;
		resize();
		return;
	}
	if (this.column > -1 && this.column < columnCount){
		TableColumn tableColumn = table.getColumn(this.column);
		tableColumn.removeControlListener(columnListener);
		this.column = -1;
	}

	if (column < 0  || column >= table.getColumnCount()) return;	
		
	this.column = column;
	TableColumn tableColumn = table.getColumn(this.column);
	tableColumn.addControlListener(columnListener);
	resize();
}
public void setItem (TableTreeItem item) {	
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
public void setEditor (Control editor, TableTreeItem item, int column) {
	setItem(item);
	setColumn(column);
	setEditor(editor);
}
void resize () {
	if (tableTree.isDisposed()) return;
	if (item == null || item.isDisposed()) return;
	Table table = tableTree.getTable();
	int columnCount = table.getColumnCount();
	if (columnCount == 0 && column != 0) return;
	if (columnCount > 0 && (column < 0 || column >= columnCount)) return;
	super.resize();
}
}
