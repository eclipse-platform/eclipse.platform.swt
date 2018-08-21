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
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

public class Bug449000_TableDrawingColumnMoved {

	public static void main(String[] args) {
		final Display display = new Display();
		Shell shell = new Shell(display);
		GridLayout layout = new GridLayout();
		shell.setLayout(layout);

		final Table table = new Table(shell, SWT.NONE);
		table.setLayoutData(new GridData(GridData.FILL_BOTH));
		table.setLinesVisible(true);
		table.setHeaderVisible(true);

		TableColumn column1 = new TableColumn(table, SWT.NONE);
		column1.setText("Column 1");
		column1.setMoveable(true);

		TableColumn column2 = new TableColumn(table, SWT.NONE);
		column2.setText("Column 2");
		column2.setMoveable(true);

		TableColumn column3 = new TableColumn(table, SWT.NONE);
		column3.setText("Column 3");
		column3.setMoveable(true);

		column3.addControlListener(new ControlAdapter() {
			@Override
			public void controlResized(ControlEvent e) {
				table.getClientArea();
			}
		});

		TableItem item = new TableItem(table, SWT.NONE);
		item.setText(new String[] { "column 1 widget.text", "column 2 widget.text",
				"column 3 widget.text" });

		for (TableColumn column : table.getColumns()) {
			column.pack();
		}

		shell.setMinimumSize(200, 200);
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}

}
