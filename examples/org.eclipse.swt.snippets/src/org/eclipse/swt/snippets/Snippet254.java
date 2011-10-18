/*******************************************************************************
 * Copyright (c) 2000, 2007 IBM Corporation and others.
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
 * Tree example snippet: compute the number of visible rows in a tree
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.layout.*;

public class Snippet254 {

	static TreeItem nextItem(Tree tree, TreeItem item) {
		if (item == null) return null;
		if (item.getExpanded() && item.getItemCount() > 0) return item.getItem(0);
		TreeItem childItem = item;
		TreeItem parentItem = childItem.getParentItem();
		int index = parentItem == null ? tree.indexOf(childItem) : parentItem.indexOf(childItem);
		int count = parentItem == null ? tree.getItemCount() : parentItem.getItemCount();
		while (true) {
			if (index + 1 < count) return parentItem == null ? tree.getItem(index + 1) : parentItem.getItem(index + 1);
			if (parentItem == null) return null;
			childItem = parentItem;
			parentItem = childItem.getParentItem();
			index = parentItem == null ? tree.indexOf(childItem) : parentItem.indexOf(childItem);
			count = parentItem == null ? tree.getItemCount() : parentItem.getItemCount();
		}
	}
	
	public static void main(String[] args) {
		final Display display = new Display();
		final Shell shell = new Shell(display);
		RowLayout layout = new RowLayout (SWT.VERTICAL);
		layout.fill = true;
		layout.wrap = false;
		shell.setLayout (layout);
		final Tree tree = new Tree (shell, SWT.NONE);
		for (int i=0; i<32; i++) {
			TreeItem item0 = new TreeItem (tree, SWT.NONE);
			item0.setText ("Item " + i + " is quite long");
			for (int j=0; j<3; j++) {
				TreeItem item1 = new TreeItem (item0, SWT.NONE);
				item1.setText ("Item " + i + " " + j + " is quite long");
				for (int k=0; k<3; k++) {
					TreeItem item2 = new TreeItem (item1, SWT.NONE);
					item2.setText ("Item " + i + " " + j + " " + k + " is quite long");	
					for (int l=0; l<3; l++) {
						TreeItem item3 = new TreeItem (item2, SWT.NONE);
						item3.setText ("Item " + i + " " + j + " " + k + " " + l + " is quite long");
					}
				}
			}
		}
		tree.setLayoutData(new RowData (200, 200));
		final Button button = new Button (shell, SWT.PUSH);
		button.setText ("Visible Items []");
		button.addListener (SWT.Selection, new Listener () {
			public void handleEvent (Event e) {
				int visibleCount = 0;
				Rectangle rect = tree.getClientArea ();
				TreeItem item = tree.getTopItem ();
				while (item != null) {
					visibleCount++;
					Rectangle itemRect = item.getBounds();
					if (itemRect.y + itemRect.height > rect.y + rect.height) {
						break;
					}
					item = nextItem (tree, item);
				}				
				button.setText ("Visible Items [" + visibleCount + "]");
			}
		});
		shell.pack();
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) display.sleep();
		}
		display.dispose();
	}
} 
