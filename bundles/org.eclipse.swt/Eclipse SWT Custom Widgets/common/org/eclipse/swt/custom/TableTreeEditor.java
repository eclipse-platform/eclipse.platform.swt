package org.eclipse.swt.custom;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;

/**
*
* A TableTreeEditor is a manager for a Control that appears above a cell in a TableTree and tracks with the
* moving and resizing of that cell.  It can be used to display a text widget above a cell
* in a TableTree so that the user can edit the contents of that cell.  It can also be used to display
* a button that can launch a dialog for modifying the contents of the associated cell.
*
* <p>The TableTreeEditor performs the same functions as the TableEditor except that the cell is in
* a TableTree.  Refer to TableEditor for additional information.  The significant differnce is that
* the Control provided as the editor <b>must</b> be created with its parent being the <b>Table</b>
* control that is underneath the TableTree specified in the TableTreeEditor constructor.
* The Table can be obtained from the TableTree using tableTree.getTable().
* 
*/
public class TableTreeEditor {

	public int horizontalAlignment = SWT.CENTER;
	public boolean grabHorizontal = false;
	public int minimumWidth = 0;
	
	TableTreeItem item;
	TableEditor editor;
/**
* Creates a TableTreeEditor for the specified TableTree.
*
* @param tableTree the TableTree Control above which this editor will be displayed
*
*/
public TableTreeEditor (TableTree tableTree) {
	editor = new TableEditor (tableTree.getTable ());
}
/**
 * Removes all associations between the TableTreeEditor and the cell in the TableTree.  The
 * TableTree and the editor Control are <b>not</b> disposed.
 */
public void dispose () {

	editor.dispose();
}
/**
* Returns the zero based index of the column of the cell being tracked by this editor.
*
* @return the zero based index of the column of the cell being tracked by this editor
*/
public int getColumn () {
	return editor.getColumn ();
}
/**
* Returns the Control that is displayed above the cell being edited.
*
* @return the Control that is displayed above the cell being edited
*/
public Control getEditor () {
	return editor.getEditor ();
}
/**
* Returns the TableTreeItem for the row of the cell being tracked by this editor.
*
* @return the TableTreeItem for the row of the cell being tracked by this editor
*/
public TableTreeItem getItem () {
	return item;
}
/**
* Specify the Control that is to be displayed and the cell in the TableTree that it is to be 
* positioned above.
*
* <p>Note: The Control provided as the editor <b>must</b> be created with its parent being the <b>Table</b>
* control that is underneath the TableTree specified in the TableTreeEditor constructor.
* The Table can be obtained from the TableTree using tableTree.getTable().
* 
* @param editor the Control that is displayed above the cell being edited
* @param item the TableTreeItem for the row of the cell being tracked by this editor
* @param column the zero based index of the column of the cell being tracked by this editor
*/
public void setEditor (Control control, TableTreeItem item, int column) {
	if (control == null) {
		editor.setEditor (null, null, -1);
		return;
	}
		
	this.item = item;
	TableItem tableItem = item.tableItem;
	editor.horizontalAlignment = horizontalAlignment;
	editor.grabHorizontal = grabHorizontal;
	editor.minimumWidth = minimumWidth;

	editor.setEditor (control, tableItem, column);
}
}
