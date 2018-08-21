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
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

public class Bug106798_TreeCheckBoxTest {

	static boolean[] myModel = new boolean[200];

	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display);
		GridLayout gridLayout = new GridLayout(1, false);
		gridLayout.marginLeft = 10;
		gridLayout.marginTop = 10;
		gridLayout.marginBottom = 10;
		gridLayout.marginRight = 10;
		shell.setLayout(gridLayout);

		final Table table = new Table(shell, SWT.BORDER | SWT.VIRTUAL | SWT.CHECK
				| SWT.FULL_SELECTION);
		table.setHeaderVisible(true);
		table.setLayoutData(new GridData(GridData.FILL_BOTH));
		table.addListener(SWT.SetData, new Listener() {

			@Override
			public void handleEvent(Event event) {
				TableItem item = (TableItem) event.item;
				int index = table.indexOf(item);
				boolean checked = myModel[index];
				item.setChecked(checked);
				item.setText(0, "Row " + index + ": " + checked);
			}
		});

		TableColumn tableColumn = new TableColumn(table, SWT.LEFT);
		tableColumn.setWidth(100);
		tableColumn.setText("BlahBlah");

		table.setItemCount(myModel.length);
		table.clearAll();

		table.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event e) {
				if (e.detail == SWT.CHECK) {
					TableItem item = (TableItem) e.item;
					int index = table.indexOf(item);
					boolean isChecked = item.getChecked();
					myModel[index] = isChecked;
					myModel[index / 2] = isChecked;
					table.clear(new int[] { index, index / 2 });
				}
			}
		});

		shell.setSize(400, 300);
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}
}