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


import java.util.Random;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

public class Bug499850_VirtualTableHang {

	static final int COUNT = 100000;

	public static void main(String[] args) {
		Random random = new Random();
		final int[][] data = new int[COUNT][];
		for (int i = 0; i < data.length; i++) {
			data[i] = new int[] {i, random.nextInt()};
		}

		Display display = new Display ();
		final Shell shell = new Shell (display);
		shell.setLayout (new RowLayout (SWT.VERTICAL));
		final Table table = new Table (shell, SWT.VIRTUAL | SWT.MULTI);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		table.setItemCount(COUNT);
		TableColumn eventColumn = new TableColumn(table, SWT.NONE);
		eventColumn.setText("Event");
		eventColumn.setWidth(200);
		TableColumn dataColumn = new TableColumn(table, SWT.NONE);
		dataColumn.setText("Data");
		dataColumn.setWidth(200);
		table.addListener (SWT.SetData, event -> {
			TableItem item = (TableItem) event.item;
			int index = table.indexOf (item);
			int[] datum = data[index + 1];
			item.setText(new String[] {Integer.toString(datum[0]),
					Integer.toString(datum[1]) });
		});
		table.setLayoutData (new RowData (400, 300));
		Button button = new Button (shell, SWT.PUSH);
		button.setText ("Add Items");
		button.addListener (SWT.Selection, event -> {
			table.setItemCount (COUNT);
			shell.layout ();
		});
		Button removeButton;
		removeButton = new Button(shell, SWT.PUSH);
		removeButton.setText("Remove All");
		final Label label = new Label(shell, SWT.NONE);
		label.setLayoutData(new RowData (400, 30));
		removeButton.addListener(SWT.Selection, e -> {
			long t1 = System.currentTimeMillis ();
			table.removeAll();
			long t2 = System.currentTimeMillis ();
			label.setText ("Items: " + COUNT + ", Time to remove: " + (t2 - t1) + " (ms)");
		});
		shell.pack ();
		shell.open ();
		while (!shell.isDisposed ()) {
			if (!display.readAndDispatch ()) display.sleep ();
		}
		display.dispose ();
	}
}