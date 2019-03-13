/*******************************************************************************
 * Copyright (c) 2018 Red Hat and others. All rights reserved.
 * The contents of this file are made available under the terms
 * of the GNU Lesser General Public License (LGPL) Version 2.1 that
 * accompanies this distribution (lgpl-v21.txt).  The LGPL is also
 * available at http://www.gnu.org/licenses/lgpl.html.  If the version
 * of the LGPL at http://www.gnu.org is different to the version of
 * the LGPL accompanying this distribution and there is any conflict
 * between the two license versions, the terms of the LGPL accompanying
 * this distribution shall govern.
 *
 * Contributors:
 *     Red Hat - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tests.gtk.snippets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

public class Bug545139_TableColumnEraseString {

	private static boolean COLUMN_BEFORE = false;
	private static int NUM_ITEMS = 5;
	private static int NUM_COLS = 5;

	public static void main(String args[]) {
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setLayout(new GridLayout(2, false));
		shell.setSize(500, 200);

		Table table = new Table(shell, SWT.NONE);
		table.setHeaderVisible(true);
		TableColumn [] columns = new TableColumn [NUM_COLS];
		if (COLUMN_BEFORE) {
			for (int i = 0; i < NUM_COLS; i++) {
				TableColumn col = new TableColumn(table, SWT.NONE);
				col.setText("Column " + i);
				columns [i] = col;
			}
		}

		TableItem [] items = new TableItem [5];
		for (int i = 0; i < NUM_ITEMS; i++) {
			TableItem item = new TableItem(table, SWT.NONE);
			String [] textArray = new String [NUM_COLS];
			for (int col = 0; col < NUM_COLS; col++) {
				textArray [col] = "Col " + col + " Item " + i;
			}
			item.setText(textArray);
			items [i] = item;
		}

		if (!COLUMN_BEFORE) {
			for (int i = 0; i < NUM_COLS; i++) {
				TableColumn col = new TableColumn(table, SWT.NONE);
				col.setText("Column " + i);
				columns [i] = col;
			}
		}

		for (int i = 0; i < NUM_ITEMS; i++) {
			System.out.println(i + " --> " + items[i].getText());
		}

		for (TableColumn col : columns) {
			col.pack();
		}

		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
	}
}