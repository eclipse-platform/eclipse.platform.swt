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
 * Allow user to reorder columns by dragging or programmatically.
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 *
 * @since 3.1
 */
import org.eclipse.swt.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class Snippet181 {

	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setLayout(new RowLayout(SWT.HORIZONTAL));
		final Table table = new Table(shell, SWT.BORDER | SWT.CHECK);
		table.setLayoutData(new RowData(-1, 300));
		table.setHeaderVisible(true);
		TableColumn column = new TableColumn(table, SWT.LEFT);
		column.setText("Column 0");
		column = new TableColumn(table, SWT.CENTER);
		column.setText("Column 1");
		column = new TableColumn(table, SWT.CENTER);
		column.setText("Column 2");
		column = new TableColumn(table, SWT.CENTER);
		column.setText("Column 3");
		column = new TableColumn(table, SWT.CENTER);
		column.setText("Column 4");
		for (int i = 0; i < 100; i++) {
			TableItem item = new TableItem(table, SWT.NONE);
			String[] text = new String[]{i+" 0", i+" 1", i+" 2", i+" 3", i+" 4"};
			item.setText(text);
		}
		Listener listener = e -> System.out.println("Move "+e.widget);
		TableColumn[] columns = table.getColumns();
		for (int i = 0; i < columns.length; i++) {
			columns[i].pack();
			columns[i].setMoveable(true);
			columns[i].addListener(SWT.Move, listener);
		}
		Button b = new Button(shell, SWT.PUSH);
		b.setText("invert column order");
		b.addListener(SWT.Selection, e -> {
			int[] order = table.getColumnOrder();
			for (int i = 0; i < order.length / 2; i++) {
				int temp = order[i];
				order[i] = order[order.length - i - 1];
				order[order.length - i - 1] = temp;
			}
			table.setColumnOrder(order);
		});
		shell.pack();
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}
}
