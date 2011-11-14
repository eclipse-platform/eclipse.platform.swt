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
package org.eclipse.swt.snippets;

/*
 * TreeCursor example snippet: navigate a Tree's cells with the arrow keys.
 * Edit when user hits Return key.  Exit edit mode by hitting Escape (cancels edit)
 * or Return (applies edit to tree).  Expand/Collapse with Ctrl+Arrow keys.
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 * 
 * @since 3.8
 */
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class Snippet360 {

public static void main(String[] args) {
	Display display = new Display();
	Shell shell = new Shell(display);
	shell.setLayout(new GridLayout());

	// create a a tree with 3 columns and fill with data
	final Tree tree = new Tree(shell, SWT.BORDER | SWT.MULTI | SWT.FULL_SELECTION);
	tree.setLayoutData(new GridData(GridData.FILL_BOTH));
	tree.setHeaderVisible(true);
	TreeColumn column1 = new TreeColumn(tree, SWT.NONE);
	TreeColumn column2 = new TreeColumn(tree, SWT.NONE);
	TreeColumn column3 = new TreeColumn(tree, SWT.NONE);
	for (int i = 0; i < 9; i++) {
		TreeItem item = new TreeItem(tree, SWT.NONE);
		item.setText(new String[] { "root " + i + " 0", "root " + i + " 1", "root " + i + " 2" });
		for (int j = 0; j < 9; j++) {
			TreeItem item2 = new TreeItem(item, SWT.NONE);
			item2.setText(new String[] { "child " + j + " 0", "child " + j + " 1", "child " + j + " 2" });
			TreeItem current = item2;
			for (int k = 0; k < 5; k++) {
				TreeItem item3 = new TreeItem(current, SWT.NONE);
				item3.setText(new String[] { "descendent " + k + " 0", "descendent " + k + " 1", "descendent " + k + " 2" });
				current = item3;
			}
		}
	}
	column1.setWidth(200);
	column2.setWidth(100);
	column3.setWidth(100);

	// create a TreeCursor to navigate around the tree
	final TreeCursor cursor = new TreeCursor(tree, SWT.NONE);
	// create an editor to edit the cell when the user hits "ENTER"
	// while over a cell in the tree
	final ControlEditor editor = new ControlEditor(cursor);
	editor.grabHorizontal = true;
	editor.grabVertical = true;

	cursor.addSelectionListener(new SelectionAdapter() {
		// when the TreeEditor is over a cell, select the corresponding row
		// in the tree
		public void widgetSelected(SelectionEvent e) {
			tree.setSelection(new TreeItem[] { cursor.getRow() });
		}

		// when the user hits "ENTER" in the TreeCursor, pop up a text
		// editor so that they can change the text of the cell
		public void widgetDefaultSelected(SelectionEvent e) {
			final Text text = new Text(cursor, SWT.NONE);
			TreeItem row = cursor.getRow();
			int column = cursor.getColumn();
			text.setText(row.getText(column));
			text.addKeyListener(new KeyAdapter() {
				public void keyPressed(KeyEvent e) {
					// close the text editor and copy the data over
					// when the user hits "ENTER"
					if (e.character == SWT.CR) {
						TreeItem row = cursor.getRow();
						int column = cursor.getColumn();
						row.setText(column, text.getText());
						text.dispose();
					}
					// close the text editor when the user hits "ESC"
					if (e.character == SWT.ESC) {
						text.dispose();
					}
				}
			});
			editor.setEditor(text);
			text.setFocus();
		}
	});
	// Hide the TreeCursor when the user hits the "MOD1" or "MOD2" key.
	// This allows the user to select multiple items in the tree.
	cursor.addKeyListener(new KeyAdapter() {
		public void keyPressed(KeyEvent e) {
			if (e.keyCode == SWT.MOD1 || e.keyCode == SWT.MOD2
					|| (e.stateMask & SWT.MOD1) != 0
					|| (e.stateMask & SWT.MOD2) != 0) {
				cursor.setVisible(false);
			}
		}
	});
	// Show the TreeCursor when the user releases the "MOD2" or "MOD1" key.
	// This signals the end of the multiple selection task.
	tree.addKeyListener(new KeyListener() {
		public void keyReleased(KeyEvent e) {
			if (e.keyCode == SWT.MOD1 && (e.stateMask & SWT.MOD2) != 0) return;
			if (e.keyCode == SWT.MOD2 && (e.stateMask & SWT.MOD1) != 0) return;
			if (e.keyCode != SWT.MOD1 && (e.stateMask & SWT.MOD1) != 0) return;
			if (e.keyCode != SWT.MOD2 && (e.stateMask & SWT.MOD2) != 0) return;

			TreeItem[] selection = tree.getSelection();
			TreeItem row = (selection.length == 0) ? tree.getItem(tree.indexOf(tree.getTopItem())) : selection[0];
			tree.showItem(row);
			cursor.setSelection(row, cursor.getColumn());
			cursor.setVisible(true);
			cursor.setFocus();
		}
		public void keyPressed(KeyEvent e) {
			switch (e.keyCode) {
				case SWT.ARROW_LEFT:
				case SWT.ARROW_RIGHT: {
					if ((e.stateMask & SWT.MOD1) != 0) {
						TreeItem[] selection = tree.getSelection();
						if (selection.length > 0) {
							selection[0].setExpanded(e.keyCode == SWT.ARROW_RIGHT);
						}
						break;
					}
				}
			}
		}
	});

	shell.pack();
	shell.open();
	while (!shell.isDisposed()) {
		if (!display.readAndDispatch())
			display.sleep();
	}
	display.dispose();
}

}
