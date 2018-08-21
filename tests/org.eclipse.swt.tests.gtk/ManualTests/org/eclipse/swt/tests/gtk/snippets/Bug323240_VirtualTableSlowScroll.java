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
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;


public class Bug323240_VirtualTableSlowScroll {

static final int COUNT = 2000;

public static void main(String[] args) {
	Display display = new Display ();
	final Shell shell = new Shell (display);
	shell.setLayout (new GridLayout());
	final Table table = new Table (shell, SWT.VIRTUAL | SWT.BORDER);
	table.addListener (SWT.SetData, new Listener () {
		@Override
		public void handleEvent (Event event) {
			TableItem item = (TableItem) event.item;
			int index = table.indexOf (item);
			item.setText (0, "Item " + index);

			for (int i = 1; i <= 10; i++) {
				item.setText(i, index + " test " + i);
			}

		}
	});

	GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
	gridData.heightHint = 200;
	table.setLayoutData(gridData);

	table.setHeaderVisible(true);
	table.setLinesVisible(true);

	Button button = new Button (shell, SWT.PUSH);
	button.setText ("Add Items");

	for (int i = 1; i <= 10; i++) {
		TableColumn column = new TableColumn(table, SWT.NONE);
		column.setText("header " + i);
	}


	final Label label = new Label(shell, SWT.NONE);
	button.addListener (SWT.Selection, new Listener () {
		@Override
		public void handleEvent (Event event) {
			long t1 = System.currentTimeMillis ();
			table.setItemCount (COUNT);
			long t2 = System.currentTimeMillis ();
			label.setText ("Items: " + COUNT + ", Time: " + (t2 - t1) + " (ms)");
			shell.layout ();
		}
	});

	for (TableColumn column: table.getColumns()) {
		column.pack();
	}

	shell.pack ();
	shell.open ();
	while (!shell.isDisposed ()) {
		if (!display.readAndDispatch ()) display.sleep ();
	}
	display.dispose ();
}
}