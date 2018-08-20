/*******************************************************************************
 * Copyright (c) 2000, 2017 IBM Corporation and others.
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
 * Table example snippet: resize columns as table resizes
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */
import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class Snippet77 {

public static void main(String[] args) {
	Display display = new Display();
	Shell shell = new Shell(display);
	shell.setLayout(new FillLayout());

	final Composite comp = new Composite(shell, SWT.NONE);
	final Table table = new Table(comp, SWT.BORDER | SWT.V_SCROLL);
	table.setHeaderVisible(true);
	table.setLinesVisible(true);
	final TableColumn column1 = new TableColumn(table, SWT.NONE);
	column1.setText("Column 1");
	final TableColumn column2 = new TableColumn(table, SWT.NONE);
	column2.setText("Column 2");
	for (int i = 0; i < 10; i++) {
		TableItem item = new TableItem(table, SWT.NONE);
		item.setText(new String[] {"item 0" + i, "item 1"+i});
	}
	comp.addControlListener(ControlListener.controlResizedAdapter(e -> {
		Rectangle area = comp.getClientArea();
		Point size = table.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		ScrollBar vBar = table.getVerticalBar();
		int width = area.width - table.computeTrim(0, 0, 0, 0).width - vBar.getSize().x;
		if (size.y > area.height + table.getHeaderHeight()) {
			// Subtract the scrollbar width from the total column width
			// if a vertical scrollbar will be required
			Point vBarSize = vBar.getSize();
			width -= vBarSize.x;
		}
		Point oldSize = table.getSize();
		if (oldSize.x > area.width) {
			// table is getting smaller so make the columns
			// smaller first and then resize the table to
			// match the client area width
			column1.setWidth(width / 3);
			column2.setWidth(width - column1.getWidth());
			table.setSize(area.width, area.height);
		} else {
			// table is getting bigger so make the table
			// bigger first and then make the columns wider
			// to match the client area width
			table.setSize(area.width, area.height);
			column1.setWidth(width / 3);
			column2.setWidth(width - column1.getWidth());
		}
	}));

	shell.open();
	while (!shell.isDisposed()) {
		if (!display.readAndDispatch())
			display.sleep();
	}
	display.dispose();
}
}
