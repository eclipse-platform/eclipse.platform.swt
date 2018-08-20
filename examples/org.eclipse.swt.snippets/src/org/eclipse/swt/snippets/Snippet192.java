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

import java.util.*;

import org.eclipse.swt.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

/*
 * Show a sort indicator in the column header
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 *
 * @since 3.2
 */

public class Snippet192 {
public static void main(String[] args) {
	// initialize data with keys and random values
	int size = 100;
	Random random = new Random();
	final int[][] data = new int[size][];
	for (int i = 0; i < data.length; i++) {
		data[i] = new int[] {i, random.nextInt()};
	}
	// create a virtual table to display data
	Display display = new Display();
	Shell shell = new Shell(display);
	shell.setLayout(new FillLayout());
	final Table table = new Table(shell, SWT.VIRTUAL);
	table.setHeaderVisible(true);
	table.setLinesVisible(true);
	table.setItemCount(size);
	final TableColumn column1 = new TableColumn(table, SWT.NONE);
	column1.setText("Key");
	column1.setWidth(200);
	final TableColumn column2 = new TableColumn(table, SWT.NONE);
	column2.setText("Value");
	column2.setWidth(200);
	table.addListener(SWT.SetData, e -> {
		TableItem item = (TableItem) e.item;
		int index = table.indexOf(item);
		int[] datum = data[index];
		item.setText(new String[] {Integer.toString(datum[0]),
				Integer.toString(datum[1]) });
	});
	// Add sort indicator and sort data when column selected
	Listener sortListener = e -> {
		// determine new sort column and direction
		TableColumn sortColumn = table.getSortColumn();
		TableColumn currentColumn = (TableColumn) e.widget;
		int dir = table.getSortDirection();
		if (sortColumn == currentColumn) {
			dir = dir == SWT.UP ? SWT.DOWN : SWT.UP;
		} else {
			table.setSortColumn(currentColumn);
			dir = SWT.UP;
		}
		// sort the data based on column and direction
		final int index = currentColumn == column1 ? 0 : 1;
		final int direction = dir;
		Arrays.sort(data, (a, b) -> {
			if (a[index] == b[index]) return 0;
			if (direction == SWT.UP) {
				return a[index] < b[index] ? -1 : 1;
			}
			return a[index] < b[index] ? 1 : -1;
		});
		// update data displayed in table
		table.setSortDirection(dir);
		table.clearAll();
	};
	column1.addListener(SWT.Selection, sortListener);
	column2.addListener(SWT.Selection, sortListener);
	table.setSortColumn(column1);
	table.setSortDirection(SWT.UP);
	shell.setSize(shell.computeSize(SWT.DEFAULT, SWT.DEFAULT).x, 300);
	shell.open();
	while (!shell.isDisposed()) {
		if (!display.readAndDispatch())
			display.sleep();
	}
	display.dispose();
}
}