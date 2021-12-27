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

package org.eclipse.swt.tests.manual;

import org.eclipse.swt.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class Bug546935_TableFlickeringResizingColumns {
	public static void main(String[] args) {
		final Display display = new Display();
		final Shell shell = new Shell(display);
		shell.setLayout(new GridLayout(1, true));

		final Label hint = new Label(shell, 0);
		hint.setText(
			"1) Run on Windows\n" +
			"2) Choose a column where header is wider than items\n" +
			"3) Double-click the header separator to auto-adjust column size\n" +
			"4) Bug 546935: there is flickering even though column size doesn't change"
		);

		final Table table = new Table(shell, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		table.setHeaderVisible(true);

		String[] columns = {
			"column",
			"header is longer"
		};

		String[] items = {
			"item is longer",
			"item"
		};

		for (int iColumn = 0; iColumn < columns.length * 3; iColumn++) {
			final TableColumn tableColumn = new TableColumn(table, 0);
			tableColumn.setText(columns[iColumn % columns.length]);
			tableColumn.setWidth(100);
		}

		for (int iItem = 0; iItem < 100; iItem++) {
			TableItem item = new TableItem(table, 0);
			for (int iColumn = 0; iColumn < table.getColumnCount(); iColumn++) {
				item.setText(iColumn, items[iColumn % columns.length]);
			}
		}

		// 'MeasureItem' is required to reproduce.
		table.addListener(SWT.MeasureItem, event -> {
			event.width = 35;
		});

		// Debugging code, not needed to reproduce
		{
			final int[] itemsMeasured = new int[1];
			final int[] itemsPainted = new int[1];
			table.addListener(SWT.MeasureItem, event -> {
				itemsMeasured[0]++;
			});
			table.addListener(SWT.PaintItem, event -> {
				itemsPainted[0]++;
			});
			table.addListener(SWT.Paint, event -> {
				System.out.format("Measured=%d Painted=%d%n", itemsMeasured[0], itemsPainted[0]);
				itemsMeasured[0] = 0;
				itemsPainted[0] = 0;
			});
		}

		Button btnPackColumn = new Button(shell, SWT.PUSH);
		btnPackColumn.setText("Pack columns");
		btnPackColumn.addListener(SWT.Selection, e -> {
			for (int iColumn = 0; iColumn < table.getColumnCount(); iColumn++) {
				table.getColumn(iColumn).pack();
			}
		});

		shell.setSize(800, 340);
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();
	}
}
