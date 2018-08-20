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
 * Table snippet: specify custom column widths when a column is packed
 *
 * For a detailed explanation of this snippet see
 * http://www.eclipse.org/articles/Article-CustomDrawingTableAndTreeItems/customDraw.htm#_example2
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 *
 * @since 3.2
 */
import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;

public class Snippet272 {

public static void main(String[] args) {
	Display display = new Display();
	Shell shell = new Shell(display);
	shell.setBounds(10,10,400,200);
	Table table = new Table(shell, SWT.NONE);
	table.setBounds(10,10,350,150);
	table.setHeaderVisible(true);
	table.setLinesVisible(true);
	final TableColumn column0 = new TableColumn(table, SWT.NONE);
	column0.setWidth(100);
	final TableColumn column1 = new TableColumn(table, SWT.NONE);
	column1.setWidth(100);
	column0.addListener(SWT.Selection, event -> column0.pack());
	column1.addListener(SWT.Selection, event -> column1.pack());
	for (int i = 0; i < 5; i++) {
		TableItem item = new TableItem(table, SWT.NONE);
		item.setText(0, "item " + i + " col 0");
		item.setText(1, "item " + i + " col 1");
	}

	/*
	 * NOTE: MeasureItem is called repeatedly.  Therefore it is critical
	 * for performance that this method be as efficient as possible.
	 */
	table.addListener(SWT.MeasureItem, event -> event.width *= 2);

	shell.open();
	while (!shell.isDisposed()) {
		if (!display.readAndDispatch()) display.sleep();
	}
	display.dispose();
}
}
