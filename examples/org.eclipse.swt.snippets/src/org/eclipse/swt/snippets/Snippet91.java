/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
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
 * Drag and Drop example snippet: drag leaf items in a tree
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */
import org.eclipse.swt.*;
import org.eclipse.swt.dnd.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class Snippet91 {

public static void main (String [] args) {
	
	final Display display = new Display ();
	final Shell shell = new Shell (display);
	shell.setLayout(new FillLayout());
	final Tree tree = new Tree(shell, SWT.BORDER);
	for (int i = 0; i < 3; i++) {
		TreeItem item = new TreeItem(tree, SWT.NONE);
		item.setText("item "+i);
		for (int j = 0; j < 3; j++) {
			TreeItem subItem = new TreeItem(item, SWT.NONE);
			subItem.setText("item "+i+" "+j);
			for (int k = 0; k < 3; k++) {
				TreeItem subsubItem = new TreeItem(subItem, SWT.NONE);
				subsubItem.setText("item "+i+" "+j+" "+k);
			}
		}
	}
	
	Transfer[] types = new Transfer[] {TextTransfer.getInstance()};
	int operations = DND.DROP_MOVE | DND.DROP_COPY | DND.DROP_LINK;
	
	final DragSource source = new DragSource (tree, operations);
	source.setTransfer(types);
	final TreeItem[] dragSourceItem = new TreeItem[1];
	source.addDragListener (new DragSourceListener () {
		public void dragStart(DragSourceEvent event) {
			TreeItem[] selection = tree.getSelection();
			if (selection.length > 0 && selection[0].getItemCount() == 0) {
				event.doit = true;
				dragSourceItem[0] = selection[0];
			} else {
				event.doit = false;
			}
		};
		public void dragSetData (DragSourceEvent event) {
			event.data = dragSourceItem[0].getText();
		}
		public void dragFinished(DragSourceEvent event) {
			if (event.detail == DND.DROP_MOVE)
				dragSourceItem[0].dispose();
				dragSourceItem[0] = null;
		}
	});

	DropTarget target = new DropTarget(tree, operations);
	target.setTransfer(types);
	target.addDropListener (new DropTargetAdapter() {
		public void dragOver(DropTargetEvent event) {
			event.feedback = DND.FEEDBACK_EXPAND | DND.FEEDBACK_SCROLL;
			if (event.item != null) {
				TreeItem item = (TreeItem)event.item;
				Point pt = display.map(null, tree, event.x, event.y);
				Rectangle bounds = item.getBounds();
				if (pt.y < bounds.y + bounds.height/3) {
					event.feedback |= DND.FEEDBACK_INSERT_BEFORE;
				} else if (pt.y > bounds.y + 2*bounds.height/3) {
					event.feedback |= DND.FEEDBACK_INSERT_AFTER;
				} else {
					event.feedback |= DND.FEEDBACK_SELECT;
				}
			}
		}
		public void drop(DropTargetEvent event) {
			if (event.data == null) {
				event.detail = DND.DROP_NONE;
				return;
			}
			String text = (String)event.data;
			if (event.item == null) {
				TreeItem item = new TreeItem(tree, SWT.NONE);
				item.setText(text);
			} else {
				TreeItem item = (TreeItem)event.item;
				Point pt = display.map(null, tree, event.x, event.y);
				Rectangle bounds = item.getBounds();
				TreeItem parent = item.getParentItem();
				if (parent != null) {
					TreeItem[] items = parent.getItems();
					int index = 0;
					for (int i = 0; i < items.length; i++) {
						if (items[i] == item) {
							index = i;
							break;
						}
					}
					if (pt.y < bounds.y + bounds.height/3) {
						TreeItem newItem = new TreeItem(parent, SWT.NONE, index);
						newItem.setText(text);
					} else if (pt.y > bounds.y + 2*bounds.height/3) {
						TreeItem newItem = new TreeItem(parent, SWT.NONE, index+1);
						newItem.setText(text);
					} else {
						TreeItem newItem = new TreeItem(item, SWT.NONE);
						newItem.setText(text);
					}
					
				} else {
					TreeItem[] items = tree.getItems();
					int index = 0;
					for (int i = 0; i < items.length; i++) {
						if (items[i] == item) {
							index = i;
							break;
						}
					}
					if (pt.y < bounds.y + bounds.height/3) {
						TreeItem newItem = new TreeItem(tree, SWT.NONE, index);
						newItem.setText(text);
					} else if (pt.y > bounds.y + 2*bounds.height/3) {
						TreeItem newItem = new TreeItem(tree, SWT.NONE, index+1);
						newItem.setText(text);
					} else {
						TreeItem newItem = new TreeItem(item, SWT.NONE);
						newItem.setText(text);
					}
				}
				
				
			}
		}
	});

	shell.setSize (400, 400);
	shell.open ();
	while (!shell.isDisposed ()) {
		if (!display.readAndDispatch ()) display.sleep ();
	}
	display.dispose ();
}
}
