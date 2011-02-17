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
 * Tree snippet: implement standard tree check box behavior (SWT.CHECK)
 * 
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 * 
 * @since 3.3
 */
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.*;

public class Snippet274 {
	
	static void checkPath(TreeItem item, boolean checked, boolean grayed) {
	    if (item == null) return;
	    if (grayed) {
	        checked = true;
	    } else {
	        int index = 0;
	        TreeItem[] items = item.getItems();
	        while (index < items.length) {
	            TreeItem child = items[index];
	            if (child.getGrayed() || checked != child.getChecked()) {
	                checked = grayed = true;
	                break;
	            }
	            index++;
	        }
	    }
	    item.setChecked(checked);
	    item.setGrayed(grayed);
	    checkPath(item.getParentItem(), checked, grayed);
	}

	static void checkItems(TreeItem item, boolean checked) {
	    item.setGrayed(false);
	    item.setChecked(checked);
	    TreeItem[] items = item.getItems();
	    for (int i = 0; i < items.length; i++) {
	        checkItems(items[i], checked);
	    }
	}

	public static void main(String[] args) {
	    Display display = new Display();
	    Shell shell = new Shell(display);
	    Tree tree = new Tree(shell, SWT.BORDER | SWT.CHECK);
	    tree.addListener(SWT.Selection, new Listener() {
	        public void handleEvent(Event event) {
	            if (event.detail == SWT.CHECK) {
	                TreeItem item = (TreeItem) event.item;
	                boolean checked = item.getChecked();
	                checkItems(item, checked);
	                checkPath(item.getParentItem(), checked, false);
	            }
	        }
	    });
	    for (int i = 0; i < 4; i++) {
	        TreeItem itemI = new TreeItem(tree, SWT.NONE);
	        itemI.setText("Item " + i);
	        for (int j = 0; j < 4; j++) {
	            TreeItem itemJ = new TreeItem(itemI, SWT.NONE);
	            itemJ.setText("Item " + i + " " + j);
	            for (int k = 0; k < 4; k++) {
	                TreeItem itemK = new TreeItem(itemJ, SWT.NONE);
	                itemK.setText("Item " + i + " " + j + " " + k);
	            }
	        }
	    }
		Rectangle clientArea = shell.getClientArea();
	    tree.setBounds(clientArea.x, clientArea.y, 200, 200);
	    shell.pack();
	    shell.open();
	    while (!shell.isDisposed()) {
	        if (!display.readAndDispatch()) display.sleep();
	    }
	    display.dispose();
	}

}
