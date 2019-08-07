/*******************************************************************************
 * Copyright (c) 2019 Patrick Tasse and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Patrick Tasse - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tests.gtk.snippets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

public class Bug546940_TableSizingIssues {

	private static final int NUM_COL = 5;
	private static final int NUM_ROW = 5;

	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display);

		shell.setLayout(new RowLayout(SWT.VERTICAL));

		Label label1 = new Label(shell, SWT.NONE);
		label1.setText("Table #1 with no items, 5 columns packed");
		Table table1 = new Table(shell, SWT.NO_SCROLL);
		table1.setBackground(display.getSystemColor(SWT.COLOR_YELLOW));
		table1.setHeaderVisible(true);
		table1.setLinesVisible(true);
		for (int col = 0; col < NUM_COL; col++) {
			TableColumn column = new TableColumn(table1, SWT.CENTER);
			column.setText("Column " + col);
			column.pack();
		}

		Label label2 = new Label(shell, SWT.NONE);
		label2.setText("Table #2 with no items, 5 columns width=0");
		Table table2 = new Table(shell, SWT.NO_SCROLL);
		table2.setBackground(display.getSystemColor(SWT.COLOR_YELLOW));
		table2.setHeaderVisible(true);
		table2.setLinesVisible(true);
		for (int col = 0; col < NUM_COL; col++) {
			TableColumn column = new TableColumn(table2, SWT.CENTER);
			column.setText("Column " + col);
			column.setWidth(0);
		}

		Label label3 = new Label(shell, SWT.NONE);
		label3.setText("Table #3 with no items, 5 columns width=100");
		Table table3 = new Table(shell, SWT.NO_SCROLL);
		table3.setBackground(display.getSystemColor(SWT.COLOR_YELLOW));
		table3.setHeaderVisible(true);
		table3.setLinesVisible(true);
		for (int col = 0; col < NUM_COL; col++) {
			TableColumn column = new TableColumn(table3, SWT.CENTER);
			column.setText("Column " + col);
			column.setWidth(100);
		}

		Label label4 = new Label(shell, SWT.NONE);
		label4.setText("Table #4 with 5 items, 5 columns width=100");
		Table table4 = new Table(shell, SWT.NO_SCROLL);
		table4.setBackground(display.getSystemColor(SWT.COLOR_YELLOW));
		table4.setHeaderVisible(true);
		table4.setLinesVisible(true);
		for (int col = 0; col < NUM_COL; col++) {
			TableColumn column = new TableColumn(table4, SWT.CENTER);
			column.setText("Column " + col);
			column.setWidth(100);
		}
		for (int row = 1; row <= NUM_ROW; row++) {
			TableItem item = new TableItem(table4, SWT.NONE);
			if (row % 2 == 0) {
				item.setBackground(display.getSystemColor(SWT.COLOR_CYAN));
			} else {
				item.setBackground(display.getSystemColor(SWT.COLOR_GREEN));
			}
			for (int col = 0; col < NUM_COL; col++) {
				item.setText(col, "R" + row + "C" + col);
			}
		}

		Label label5 = new Label(shell, SWT.NONE);
		label5.setText("Table #5 with 5 items, 5 columns packed");
		Table table5 = new Table(shell, SWT.NO_SCROLL);
		table5.setBackground(display.getSystemColor(SWT.COLOR_YELLOW));
		table5.setHeaderVisible(true);
		table5.setLinesVisible(true);
		for (int col = 0; col < NUM_COL; col++) {
			TableColumn column = new TableColumn(table5, SWT.CENTER);
			column.setText("Column " + col);
		}
		for (int row = 1; row <= NUM_ROW; row++) {
			TableItem item = new TableItem(table5, SWT.NONE);
			if (row % 2 == 0) {
				item.setBackground(display.getSystemColor(SWT.COLOR_CYAN));
			} else {
				item.setBackground(display.getSystemColor(SWT.COLOR_GREEN));
			}
			for (int col = 0; col < NUM_COL; col++) {
				item.setText(col, "R" + row + "C" + col);
			}
		}
		for (TableColumn column : table5.getColumns()) {
			column.pack();
		}

		Label label = new Label(shell, SWT.NONE);
		label.setText("Resize shell to see table sizes change!");

		shell.setText("Table, never resized");
		shell.pack();
		shell.open();
		shell.addControlListener(new ControlAdapter() {
			@Override
			public void controlResized(ControlEvent e) {
				shell.setText("Table, has been resized");
			}
		});
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();
	}
}