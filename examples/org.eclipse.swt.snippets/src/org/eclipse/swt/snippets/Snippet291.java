/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
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
 * Give accessible names to a tree and its tree items
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */
import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.accessibility.*;

public class Snippet291 {
	public static void main(String[] args) {
		final Display display = new Display();
		Shell shell = new Shell(display);
		shell.setLayout(new FillLayout());
		final Tree tree = new Tree(shell, SWT.BORDER);
		for (int i = 0; i < 5; i++) {
			TreeItem treeItem = new TreeItem (tree, SWT.NONE);
			treeItem.setText ("item" + i);
			for (int j = 0; j < 3; j++) {
				TreeItem subItem = new TreeItem(treeItem, SWT.NONE);
				subItem.setText("item" + i + j);
			}
		}
		tree.getAccessible().addAccessibleListener(new AccessibleAdapter() {
			public void getName(AccessibleEvent e) {
				if (e.childID == ACC.CHILDID_SELF) {
					e.result = "This is the Accessible Name for the Tree";
				} else {
					TreeItem item = (TreeItem)display.findWidget(tree, e.childID);
					if (item != null) {
						e.result = "This is the Accessible Name for the TreeItem: " + item.getText();
					}
				}
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