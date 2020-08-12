/*******************************************************************************
 * Copyright (c) 2020 Syntevo and others.
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
package org.eclipse.swt.tests.win32.snippets;

import org.eclipse.swt.*;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class Bug565679_WS_BORDER {
	public static void main(String[] args) {
		final Display display = new Display();
		final Shell shell = new Shell(display);
		GridLayout layout = new GridLayout(1, true);
		layout.horizontalSpacing = 10;
		shell.setLayout(layout);

		new Label(shell, 0).setText(
			"Border of these Composite controls is affected by the patch.\n" +
			"They should have thin border as before."
		);

		{
			final Composite compControls = new Composite(shell, 0);
			compControls.setLayout(new GridLayout(2, true));

			new Label(compControls, 0).setText("Canvas");
			new Canvas(compControls, SWT.BORDER);

			new Label(compControls, 0).setText("Composite");
			new Composite(compControls, SWT.BORDER);

			new Label(compControls, 0).setText("StyledText");
			new StyledText(compControls, SWT.BORDER).setText("Some text");

			new Label(compControls, 0).setText("Spinner (with border)");
			new Spinner(compControls, SWT.BORDER);

			new Label(compControls, 0).setText("Spinner (no border)");
			new Spinner(compControls, 0);

			new Label(compControls, 0).setText("Tree (with header)");
			{
				Tree tree = new Tree(compControls, SWT.BORDER);
				tree.setHeaderVisible(true);
				TreeColumn column = new TreeColumn(tree, 0);
				column.setText("Column");
				column.setWidth(100);
				TreeItem treeItem1 = new TreeItem(tree, 0);
				treeItem1.setText("Item");
				TreeItem treeItem2 = new TreeItem(treeItem1, 0);
				treeItem2.setText("Item");
				treeItem1.setExpanded(true);
			}

			new Label(compControls, 0).setText("Tree (no header)");
			{
				Tree tree = new Tree(compControls, SWT.BORDER);
				TreeItem treeItem1 = new TreeItem(tree, 0);
				treeItem1.setText("Item");
				TreeItem treeItem2 = new TreeItem(treeItem1, 0);
				treeItem2.setText("Item");
				treeItem1.setExpanded(true);
			}
		}

		new Label(shell, 0).setText(
			"Border of these Composite controls is unchanged."
		);

		{
			final Composite compControls = new Composite(shell, 0);
			compControls.setLayout(new GridLayout(2, true));

			new Label(compControls, 0).setText("Combo");
			new Combo(compControls, SWT.BORDER);

			new Label(compControls, 0).setText("CoolBar");
			CoolBar coolbar = new CoolBar(compControls, SWT.BORDER);
			coolbar.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

			new Label(compControls, 0).setText("DateTime");
			new DateTime(compControls, SWT.BORDER);

			new Label(compControls, 0).setText("Decorations");
			new Decorations(compControls, SWT.BORDER);

			new Label(compControls, 0).setText("ExpandBar");
			new ExpandBar(compControls, SWT.BORDER);

			new Label(compControls, 0).setText("Table");
			new Table(compControls, SWT.BORDER);

			new Label(compControls, 0).setText("Text");
			new Text(compControls, SWT.BORDER);

			new Label(compControls, 0).setText("ToolBar");
			ToolBar toolbar = new ToolBar(compControls, SWT.BORDER);
			toolbar.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		}

		new Label(shell, 0).setText(
			"Border of these Composite controls is unchanged.\n" +
			"But using SWT.BORDER with them is very weird anyway..."
		);

		{
			final Composite compControls = new Composite(shell, 0);
			compControls.setLayout(new GridLayout(2, true));

			new Label(compControls, 0).setText("Group");
			Group group = new Group(compControls, SWT.BORDER);
			group.setText("Group");

			new Label(compControls, 0).setText("TabFolder");
			TabFolder tabFolder = new TabFolder(compControls, SWT.BORDER);
			new TabItem(tabFolder, 0).setText("Item");
			new TabItem(tabFolder, 0).setText("Item");
		}

		shell.setSize(400, 900);
		shell.open();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}

		display.dispose();
	}
}
