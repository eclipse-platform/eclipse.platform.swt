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
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

public class Bug271032_CustomTablePainting {

	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setLayout(new RowLayout(SWT.VERTICAL));
		shell.setSize(300, 400);
		
		Button button = new Button(shell, SWT.PUSH);
		button.setText("Unselect All");
		button.pack();

		final Table table = new Table(shell, SWT.SINGLE | SWT.FULL_SELECTION);
		table.setHeaderVisible(true);
		
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				table.deselectAll();
			}
		});

		table.addListener(SWT.EraseItem, new Listener() {
			@Override
			public void handleEvent(Event event) {
//				event.detail &= ~SWT.SELECTED;
				event.detail &= ~SWT.FOCUSED;
			}
		});
		
		TableColumn column0 = new TableColumn(table, SWT.CENTER);
		TableColumn column1 = new TableColumn(table, SWT.CENTER);
		TableColumn column2 = new TableColumn(table, SWT.CENTER);
		
		for(int row = 0; row < 10; row++) {
			TableItem item = new TableItem(table, SWT.NONE);
			item.setText(new String[] {"garbage", "garbage", "garbage"});
		}
		
		column0.pack();
		column1.pack();
		column2.pack();
		
		shell.open();
		while(!shell.isDisposed()) {
			if(!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();
	}

}