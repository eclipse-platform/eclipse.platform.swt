/*******************************************************************************
 * Copyright (c) 2022 IBM Corporation and others.
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
package org.eclipse.swt.tests.win32.snippets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

public class SWTIssue223 {

	public static void main(String[] args) {
		final Display display = new Display();

		final Shell shell = new Shell(display);
		shell.setLayout(new FillLayout());

		final Table table = new Table(shell, SWT.BORDER | SWT.VIRTUAL);
		table.setHeaderVisible(true);
		table.setVisible(false);

		table.addListener(SWT.SetData, event -> {
			((TableItem) event.item).setText("Row " + event.index);
		});
		table.addListener(SWT.PaintItem, event -> {
		});

		final TableColumn tableColumn = new TableColumn(table, SWT.LEFT);
		tableColumn.setText("Column");
		tableColumn.setWidth(100);

		System.out.println("Before Table#setItemCount: " + table.getVisible());
		table.setItemCount(5);
		System.out.println("After  Table#setItemCount: " + table.getVisible());

		shell.setSize(400, 300);
		shell.open();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}

		display.dispose();
	}
}
