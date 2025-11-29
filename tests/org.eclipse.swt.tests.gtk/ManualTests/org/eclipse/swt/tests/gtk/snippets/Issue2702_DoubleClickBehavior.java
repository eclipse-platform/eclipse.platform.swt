/*******************************************************************************
 * Copyright (c) 2025 Patrick Ziegler and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Patrick Ziegler - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tests.gtk.snippets;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

/**
 * This snippet demonstrates a missed selection event when updating the
 * background color within an SWT.Activate event. To reproduce, alternative
 * between the table and the tree. On each click, the clicked tree/table item
 * should be selected.
 *
 * @see <a href="https://github.com/eclipse-platform/eclipse.platform.swt/issues/2702">Issue 2702</a>
 */
public class Issue2702_DoubleClickBehavior {
	private static Color ACTIVE = new Color(255, 255, 255);
	private static Color INACTIVE = new Color(248, 248, 248);

	public static void main(String[] args) {
		Shell shell = new Shell();
		shell.setLayout(new GridLayout(2, true));
		shell.setSize(400, 200);

		Tree tree = new Tree(shell,  SWT.FULL_SELECTION);
		tree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		TreeItem treeItem1 = new TreeItem(tree, SWT.NONE);
		treeItem1.setText("Tree Item 1");
		TreeItem treeItem2 = new TreeItem(tree, SWT.NONE);
		treeItem2.setText("Tree Item 2");
		tree.addListener(SWT.Activate, event -> tree.setBackground(ACTIVE));
		tree.addListener(SWT.Deactivate, event -> tree.setBackground(INACTIVE));

		Table table = new Table(shell, SWT.FULL_SELECTION);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		TableItem tableItem1 = new TableItem(table, SWT.NONE);
		tableItem1.setText("Table Item 1");
		TableItem tableItem2 = new TableItem(table, SWT.NONE);
		tableItem2.setText("Table Item 2");
		table.addListener(SWT.Activate, event -> table.setBackground(ACTIVE));
		table.addListener(SWT.Deactivate, event -> table.setBackground(INACTIVE));

		shell.open();

		Display display = shell.getDisplay();
		while (!shell.isDisposed()) {
		 	if (!display.readAndDispatch()) {
		 		display.sleep();
	 		}
		}
	}
}