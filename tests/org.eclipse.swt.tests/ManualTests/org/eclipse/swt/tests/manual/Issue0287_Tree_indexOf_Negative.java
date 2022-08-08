/*******************************************************************************
 * Copyright (c) 2022 Syntevo and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Syntevo - initial API and implementation
 *******************************************************************************/

package org.eclipse.swt.tests.manual;

import org.eclipse.swt.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public final class Issue0287_Tree_indexOf_Negative {
	public static void main(String[] args) {
		final Display display = new Display();
		final Shell shell = new Shell(display);
		shell.setLayout (new GridLayout (1, true));

		Label hint = new Label (shell, 0);
		hint.setText (
			"1) Run on Windows\n" +
			"2) Click the button\n" +
			"3) Issue 287: 'Tree.indexOf(TreeItem)' returns wrong index\n"
		);

		Tree tree = new Tree(shell, SWT.VIRTUAL);
		tree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		tree.addListener(SWT.SetData, e -> {
			StringBuilder sb = new StringBuilder();
			TreeItem item = (TreeItem)e.item;
			sb.insert(0, "(" + e.index + ")");

			for (;;) {
				TreeItem itemParent = item.getParentItem();
				if (itemParent != null) {
					sb.insert(0, ":" + itemParent.indexOf(item));
				} else {
					sb.insert(0, ":" + item.getParent().indexOf(item));
					break;
				}
			}

			sb.insert(0, "Item");
			item.setText(sb.toString());
		});

		tree.setItemCount(10);

		Button btnTest = new Button(shell, SWT.PUSH);
		btnTest.setText("Test");
		btnTest.addListener(SWT.Activate, e -> {
			tree.setItemCount(5);
			TreeItem treeItem2 = tree.getItem(2);
			tree.setItemCount(10);

			int index = tree.indexOf(treeItem2);
			if (2 != index) {
				throw new AssertionError("Item index is " + index);
			}
		});

		shell.pack();
		shell.open();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}

		display.dispose();
	}
}
