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
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

public class Bug489751_TableItemDisposeSelect {

	private static final boolean DISPOSE_DIRECTLY = true;

	public static void main(String[] args) {
		final Display display = new Display ();
		final Shell shell = new Shell (display);
		shell.setLayout(new FillLayout());
		final Table tree = new Table (shell, SWT.BORDER);
		for (int i=0; i<4; i++) {
			TableItem iItem = new TableItem (tree, 0);
			iItem.setText ("TableItem (0) -" + i);
		}

		tree.setSelection(tree.getItem(3));

		shell.setSize(200, 200);
		shell.open();

		display.timerExec(1000, () -> {
			if (shell.isDisposed()) {
				return;
			}

			// replace selected node
			final TableItem[] selection = tree.getSelection();
			if (selection.length != 1) {
				return;
			}

			final TableItem item = selection[0];

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