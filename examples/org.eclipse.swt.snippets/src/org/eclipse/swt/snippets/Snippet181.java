/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.snippets;

/*
 * Allow user to reorder columns and reorder columns programmatically.
 * 
 * For a list of all SWT example snippets see
 * http://dev.eclipse.org/viewcvs/index.cgi/%7Echeckout%7E/platform-swt-home/dev.html#snippets
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
		table.setLayoutData(new RowData(-1, 400));
		table.setHeaderVisible(true);
		TableColumn column0 = new TableColumn(table, SWT.NONE);
		column0.setText("Column 0");
		column0.setMoveable(true);
		TableColumn column1 = new TableColumn(table, SWT.NONE);
		column1.setText("Column 1");
		column1.setMoveable(true);
		TableColumn column2 = new TableColumn(table, SWT.NONE);
		column2.setText("Column 2");
		column2.setMoveable(true);
		TableColumn column3 = new TableColumn(table, SWT.NONE);
		column3.setText("Column 3");
		column3.setMoveable(true);
		TableColumn column4 = new TableColumn(table, SWT.NONE);
		column4.setText("Column 4");
		column4.setMoveable(true);
		for (int i = 0; i < 20; i++) {
			TableItem item = new TableItem(table, SWT.NONE);
			String[] text = new String[]{"abc"+i, "def"+i, "ghi"+i,	"jkl"+i, "mnop"+i};
			item.setText(text);
		}
		column0.pack();
		column1.pack();
		column2.pack();
		column3.pack();
		column4.pack();
		Button b = new Button(shell, SWT.PUSH);
		b.setText("invert column order");
		b.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				int[] order = table.getColumnOrder();
				for (int i = 0; i < order.length / 2; i++) {
					int temp = order[i];
					order[i] = order[order.length - i - 1];
					order[order.length - i - 1] = temp;
				}
				table.setColumnOrder(order);
			}
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