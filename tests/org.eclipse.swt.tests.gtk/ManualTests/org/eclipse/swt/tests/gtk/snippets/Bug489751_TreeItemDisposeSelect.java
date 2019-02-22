/*******************************************************************************
 * Copyright (c) 2019 Red Hat and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Red Hat - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tests.gtk.snippets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

public class Bug489751_TreeItemDisposeSelect {

	private static final boolean DISPOSE_DIRECTLY = true;

	public static void main(String[] args) {
		final Display display = new Display ();
		final Shell shell = new Shell (display);
		shell.setLayout(new FillLayout());
		final Tree tree = new Tree (shell, SWT.BORDER);
		for (int i=0; i<4; i++) {
			TreeItem iItem = new TreeItem (tree, 0);
			iItem.setText ("TreeItem (0) -" + i);
			for (int j=0; j<4; j++) {
				TreeItem jItem = new TreeItem (iItem, 0);
				jItem.setText ("TreeItem (1) -" + j);
				for (int k=0; k<4; k++) {
					TreeItem kItem = new TreeItem (jItem, 0);
					kItem.setText ("TreeItem (2) -" + k);
					for (int l=0; l<4; l++) {
						TreeItem lItem = new TreeItem(kItem, 0);
						lItem.setText ("TreeItem (3) -" + l);
					}
				}
			}
		}

		final TreeItem firstNode = tree.getItem(0);
		firstNode.setExpanded(true);
		tree.setSelection(firstNode.getItem(3));

		shell.setSize(200, 200);
		shell.open();

		display.timerExec(1000, () -> {
			if (shell.isDisposed()) {
				return;
			}

			// replace selected node
			final TreeItem[] selection = tree.getSelection();
			if (selection.length != 1) {
				return;
			}

			final TreeItem item = selection[0];
			final TreeItem parentItem = item.getParentItem();
			if (parentItem == null) {
				return;
			}

			tree.deselectAll();

			if (DISPOSE_DIRECTLY) {
				item.dispose();
			}
			else {
				display.asyncExec(() -> {
					if (!item.isDisposed()) {
						return;
					}

					item.dispose();
				});
			}
		});

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch ()) display.sleep ();
		}
		display.dispose();
	}
}