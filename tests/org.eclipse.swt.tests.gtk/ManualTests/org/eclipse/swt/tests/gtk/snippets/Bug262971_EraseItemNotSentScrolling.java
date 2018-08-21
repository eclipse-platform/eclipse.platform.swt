package org.eclipse.swt.tests.gtk.snippets;
/*******************************************************************************
 * Copyright (c) 2000, 2018 IBM Corporation and others.
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


/*
 * Table example snippet: create a widget.table (columns, headers, lines)
 *
 * For a widget.list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */
import org.eclipse.swt.*;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class Bug262971_EraseItemNotSentScrolling {

	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setLayout(new GridLayout());
		Table table = new Table(shell, SWT.MULTI | SWT.BORDER
				| SWT.FULL_SELECTION);
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
		data.heightHint = 200;
		table.setLayoutData(data);
		String[] titles = { " ", "C", "!", "Description", "Resource",
				"In Folder", "Location" };
		for (int i = 0; i < titles.length; i++) {
			TableColumn column = new TableColumn(table, SWT.NONE);
			column.setText(titles[i]);
		}
		int count = 128;
		for (int i = 0; i < count; i++) {
			TableItem item = new TableItem(table, SWT.NONE);
			item.setText(0, "x");
			item.setText(1, "y");
			item.setText(2, "!");
			item.setText(3, "this stuff behaves the way I expect");
			item.setText(4, "almost everywhere");
			item.setText(5, "some.folder");
			item.setText(6, "line " + i + " in nowhere");
		}
		for (int i = 0; i < titles.length; i++) {
			table.getColumn(i).pack();
		}
		
		//Listeners
		
		table.addListener(SWT.EraseItem, new Listener() {

			@Override
			public void handleEvent(Event event) {
				if (event.index ==1)
				System.out.println("x = " + event.y + "\t" + "y = " + event.y);
			}});
		
		table.getHorizontalBar().addSelectionListener(new SelectionListener() {

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}

			@Override
			public void widgetSelected(SelectionEvent e) {
				System.out.println("scroll.selection");
				
			}});
		
		shell.pack();
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}
}