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
* A TreeEditor is a manager for a Control that appears above a cell in a Tree and tracks with the
* moving and resizing of that cell.  It can be used to display a text widget above a cell
* in a Tree so that the user can edit the contents of that cell.  It can also be used to display
* a button that can launch a dialog for modifying the contents of the associated cell.
*
* <p> Here is an example of using a TreeEditor:
* <code><pre>
* final Tree tree = new Tree(parent, SWT.FULL_SELECTION);
* final TreeEditor editor = new TreeEditor (tree);
* tree.addSelectionListener (new SelectionAdapter() {
*	public void widgetSelected(SelectionEvent e) {
*
*		// Clean up any previous editor control
*		Control oldEditor = editor.getEditor();
*		if (oldEditor != null)
*			oldEditor.dispose();	
*
*		// Identify the selected row
*		TreeItem item = (TreeItem)e.item;
*
*		// The control that will be the editor must be a child of the Tree
*		Text text = new Text(tree, SWT.NONE);
*
*		//The text editor must have the same size as the cell and must
*		//not be any smaller than 50 pixels.
*		editor.horizontalAlignment = SWT.LEFT;
*		editor.grabHorizontal = true;
*		editor.minimumWidth = 50;
*
*		// Open the text editor on the selected row.
*		editor.setEditor (text, item);
*
*		// Assign focus to the text control
*		text.setFocus ();
*	}
* });
* </pre></code>
*/
public class TreeEditor extends ControlEditor {	
	Tree tree;
	TreeItem item;
	TreeListener treeListener;
/**
* Creates a TreeEditor for the specified Tree.
*
* @param tree the Tree Control above which this editor will be displayed
*
*/
public TreeEditor (Tree tree) {
	super(tree);
	this.tree = tree;
	treeListener = new TreeAdapter () {
		final Runnable runnable = new Runnable() {
			public void run() {
				if (TreeEditor.this.tree.isDisposed() || editor == null) return;
				resize();
				if (editor == null || editor.isDisposed ()) return;
				editor.setVisible(true);
			}
		};
		public void treeCollapsed(TreeEvent e) {
			if (editor == null || editor.isDisposed ()) return;
			editor.setVisible(false);
			Display display = TreeEditor.this.tree.getDisplay();
			display.asyncExec(runnable);
		}
		public void treeExpanded(TreeEvent e) {
			if (editor == null || editor.isDisposed ()) return;
			editor.setVisible(false);
			Display display = TreeEditor.this.tree.getDisplay();
			display.asyncExec(runnable);
		}
	};
	tree.addTreeListener(treeListener);
}
Rectangle computeBounds () {
	if (item == null || item.isDisposed()) return new Rectangle(0, 0, 0, 0);
	
	Rectangle cell = item.getBounds();
	Rectangle area = tree.getClientArea();
	if (cell.x < area.x + area.width) {
		if (cell.x + cell.width > area.x + area.width) {
			cell.width = area.x + area.width - cell.x;
		}
	}
	Rectangle editorRect = new Rectangle(cell.x, cell.y, minimumWidth, cell.height);
	
	if (grabHorizontal) {
		editorRect.width = Math.max(area.x + area.width - cell.x, minimumWidth);
	}
	
	if (horizontalAlignment == SWT.RIGHT) {
		editorRect.x = Math.max(cell.x, cell.x + cell.width - editorRect.width);
	} else if (horizontalAlignment == SWT.LEFT) {
		// do nothing - cell.x is the right answer
	} else { // default is CENTER
		editorRect.x = Math.max(cell.x, cell.x + (cell.width - editorRect.width)/2);
	}
	
	return editorRect;
}
/**
 * Removes all associations between the TreeEditor and the cell in the tree.  The
 * tree and the editor Control are <b>not</b> disposed.
 */
public void dispose () {
	if (treeListener != null) 
		tree.removeTreeListener(treeListener);
	treeListener = null;
	tree = null;
	item = null;
	super.dispose();
}
/**
* Returns the TreeItem for the row of the cell being tracked by this editor.
*
* @return the TreeItem for the row of the cell being tracked by this editor
*/
public TreeItem getItem () {
	return item;
}
public void setItem (TreeItem item) {
	this.item = item;
	resize();
}

/**
* Specify the Control that is to be displayed and the cell in the tree that it is to be positioned above.
*
* <p>Note: The Control provided as the editor <b>must</b> be created with its parent being the Tree control
* specified in the TreeEditor constructor.
* 
* @param editor the Control that is displayed above the cell being edited
* @param item the TreeItem for the row of the cell being tracked by this editor
* @param column the zero based index of the column of the cell being tracked by this editor
*/
public void setEditor (Control editor, TreeItem item) {
	setItem (item);
	super.setEditor (editor);
}

void resize () {
	if (tree.isDisposed()) return;
	if (item == null || item.isDisposed()) return;	
	super.resize();
}
}
