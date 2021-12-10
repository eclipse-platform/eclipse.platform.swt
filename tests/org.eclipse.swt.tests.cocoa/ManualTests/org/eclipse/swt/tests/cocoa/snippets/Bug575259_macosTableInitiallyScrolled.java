/*******************************************************************************
 * Copyright (c) 2021 Syntevo and others.
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

package org.eclipse.swt.tests.cocoa.snippets;

import org.eclipse.swt.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public final class Bug575259_macosTableInitiallyScrolled {
	public static void main(String[] args) {
		final Display display = new Display();

		final Shell shell = new Shell(display);
		shell.setLayout(new GridLayout (1, true));

		final Label hint = new Label(shell, 0);
		hint.setLayoutData (new GridData ());
		hint.setText(
			"1) Use macOS 11 or macOS 12\n" +
			"2) Bug 575259: Table/Tree are incorrectly scrolled to some row before being shown\n"
		);

		final Composite composite = new Composite (shell, 0);
		composite.setLayout (new GridLayout (2, true));
		composite.setLayoutData (new GridData (SWT.FILL, SWT.FILL, true, true));

		final Table table = new Table (composite, SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
		{
			table.setLayoutData (new GridData (SWT.FILL, SWT.FILL, true, true));

			// Not needed to reproduce, but makes it easier to notice the problem
			table.setLinesVisible (true);

			// This is what triggers the problem
			table.setHeaderVisible (true);

			final TableColumn column = new TableColumn (table, SWT.LEFT);
			column.setText ("Column");
			column.setWidth (200);

			// Need enough items to make scrollbar show
			for (int i = 0; i < 100; i++) {
				new TableItem (table, SWT.LEFT).setText (String.valueOf (i));
			}
		}

		final Tree tree = new Tree (composite, SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
		{
			tree.setLayoutData (new GridData (SWT.FILL, SWT.FILL, true, true));

			// Not needed to reproduce, but makes it easier to notice the problem
			tree.setLinesVisible (true);

			// This is what triggers the problem
			tree.setHeaderVisible (true);

			final TreeColumn column = new TreeColumn (tree, SWT.LEFT);
			column.setText ("Column");
			column.setWidth (200);

			// Need enough items to make scrollbar show
			for (int i = 0; i < 100; i++) {
				new TreeItem (tree, SWT.LEFT).setText (String.valueOf (i));
			}
		}

		Button button = new Button(shell, SWT.PUSH);
		button.setText ("Enable/disable headers");
		button.addListener (SWT.Selection, e -> {
			table.setHeaderVisible(!table.getHeaderVisible ());
			tree.setHeaderVisible(!tree.getHeaderVisible ());
		});

		shell.setSize(600, 300);
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}

		display.dispose();
	}
}
