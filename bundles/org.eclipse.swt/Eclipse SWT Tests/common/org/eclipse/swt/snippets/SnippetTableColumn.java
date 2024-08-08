
/*******************************************************************************
 * Copyright (c) 2000, 2016 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.snippets;

/*
 * Table example snippet: insert a table column (at an index)
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class SnippetTableColumn {

	final static boolean USE_IMAGE = true;

	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setText("Snippet 106");
		shell.setLayout(new RowLayout(SWT.VERTICAL));
		final Table table = new Table(shell, SWT.BORDER | SWT.MULTI | SWT.CHECK);
		table.setHeaderVisible(true);
		for (int i = 0; i < 5; i++) {
			TableColumn column = new TableColumn(table, SWT.NONE);
			column.setText("Column " + i);
		}
		final TableColumn[] columns = table.getColumns();
		for (int i = 0; i < 5; i++) {
			TableItem item = new TableItem(table, SWT.NONE);
			for (int j = 0; j < columns.length; j++) {
				item.setText(j, "Item " + i + j);
				if (USE_IMAGE)
					item.setImage(j, table.getDisplay().getSystemImage(SWT.ICON_INFORMATION));
			}
		}
		for (TableColumn col : columns)
			col.pack();
		{
			Button button = new Button(shell, SWT.PUSH);
			final int index = 1;
			button.setText("Insert Column " + index + "a");
			button.addListener(SWT.Selection, e -> {
				TableColumn column = new TableColumn(table, SWT.NONE, index);
				column.setText("Column " + index + "a");
				TableItem[] items = table.getItems();
				for (int i = 0; i < items.length; i++) {
					items[i].setText(index, "Item " + i + "a");
				}
				column.pack();
			});
		}
		{
			Button button = new Button(shell, SWT.PUSH);
			final int index = 1;
			button.setText("Insert Item " + index + "a");
			button.addListener(SWT.Selection, e -> {
				var item = new TableItem(table, SWT.NONE);
				for (int c = 0; c < table.getColumnCount(); c++) {
					item.setText(c, "Item: " + c);
				}
			});
		}

		table.setTopIndex(50);

		table.setHeaderVisible(true);

		table.setSize(500, 500);

//		shell.pack();

		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();

//			printTableBounds(table);

		}
		display.dispose();
	}

	static void printTableBounds(Table t) {

		System.out.println("Table: " + t.getBounds());

		System.out.println("\tColumns:");

		for (var c : t.getColumns()) {
			System.out.println("\t\t" + c.getText() + " " + c.getWidth());
		}

		System.out.println("\tItems:");

		for (var i : t.getItems()) {

			System.out.println("\t\tItem: " + i.getText() + " " + i.getBounds());

			for (int r = 0; r < t.getColumnCount(); r++) {

				System.out.println("\t\t\t" + i.getText(r) + " " + i.getBounds(r));
			}

		}

	}

}
