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
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class Bug565089_TableCtrlA {
	public static int counter = 0;

	public static void main(String[] args)  {
		final Display display = new Display();

		final Shell shell = new Shell(display);
		shell.setLayout(new FillLayout(SWT.VERTICAL));

		final Table table = new Table(shell, SWT.BORDER | SWT.MULTI);
		table.setHeaderVisible(true);
		final TableColumn column = new TableColumn(table, SWT.LEFT);
		column.setText("Column");
		column.setWidth(200);

		for (int i = 0; i < 3; i++) {
			final TableItem item = new TableItem(table, 0);
			item.setText(String.valueOf(i + 1));
		}

		table.select(0);

		final Text text = new Text(shell, SWT.MULTI | SWT.BORDER);
		text.setText("- select the table row 3\n"
		             + "- click into this field\n"
		             + "- quickly click at table row 3 again and press Ctrl+A\n\n"
		             + "=> for a short time all rows look selected, but then row 3 gets selected again");

		table.addListener(SWT.KeyDown, event -> {
			if (event.keyCode == 'a' && (event.stateMask & SWT.MODIFIER_MASK) == SWT.MOD1) {
				System.out.println("Select all");
				counter = 0;
				table.selectAll();
			}
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
