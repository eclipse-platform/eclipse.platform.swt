/*******************************************************************************
 * Copyright (c) 2019 Thomas Singer and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Thomas Singer - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tests.gtk.snippets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

public class Bug552096_TreeShowItemUnreliable {

	public static void main(String[] args) {
		final Display display = new Display();
		final Shell shell = new Shell(display);
		shell.setLayout(new GridLayout());

		final Button button = new Button(shell, SWT.PUSH);
		button.setText("Add");
		button.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false));

		final Tree tree = new Tree(shell, SWT.BORDER);
		tree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		button.addListener(SWT.Selection, event -> {
			final TreeItem rootNode = new TreeItem(tree, SWT.NONE, 0);
			// Code below appends to the bottom of the Tree
//			final TreeItem rootNode = new TreeItem(tree, SWT.NONE, tree.getItemCount());
			rootNode.setText("Root " + tree.getItemCount());

			final TreeItem childItem = new TreeItem(rootNode, SWT.NONE);
			childItem.setText("Child");
			rootNode.setExpanded(true);

			tree.showItem(rootNode);
		});

		shell.setSize(400, 300);
		shell.open();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}

		display.dispose();
	}
}
