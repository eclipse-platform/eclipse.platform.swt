/*******************************************************************************
 * Copyright (c) 2018 Red Hat and others.
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
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

public class Bug127132_TableColumnPackVirtual {

	/*
	 * Testcase to demonstrate problem with Table not
	 * being able to pack columns for virtual tables
	 */

		private static TableColumn column1;
		private static TableColumn column2;
		private static TableColumn column3;

		public static void main(String[] args) {
			Display display = new Display();
			Shell shell = new Shell(display);
			shell.setLayout(new GridLayout());

			final Table table = new Table(shell, SWT.BORDER | SWT.MULTI | SWT.VIRTUAL);
			table.setLayoutData(new GridData(GridData.FILL_BOTH));
			column1 = new TableColumn(table, SWT.NONE);
			column1.setText("A");
			column2 = new TableColumn(table, SWT.NONE);
			column2.setText("B");
			column3 = new TableColumn(table, SWT.NONE);
			column3.setText("C");

			table.setLinesVisible (true);
			table.setHeaderVisible (true);

			for (int i = 0; i < 500; i++) {
				TableItem item = new TableItem(table, SWT.NONE);
				if (i < 200) {
					item.setText(new String[] { "cell "+i+" 0", "medium cell "+i+" 1", "cell "+i+" 2"});
				} else if (i < 300) {
					item.setText(new String[] { "medium cell "+i+" 0", "medium cell "+i+" 1", "medium cell "+i+" 2"});
				} else {
					item.setText(new String[] { "this is a long cell "+i+" 0", "this is a long cell "+i+" 1", "this is a long cell "+i+" 2"});
				}
			}

			// These have no effects on Linux GTK?
			column1.pack();
			column2.pack();
			column3.pack();

			Button action = new Button(shell, SWT.PUSH);
			action.setText("Pack Columns");
			action.addListener(SWT.Selection,  e -> {
				column1.pack();
				column2.pack();
				column3.pack();
			});

			shell.open();
			while (!shell.isDisposed()) {
				if (!display.readAndDispatch())
					display.sleep();
			}
			display.dispose();
	}
}
