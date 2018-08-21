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
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

public class Bug151308_TableGetClientAreaColumn {
	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setBounds(10,10,400,300);
		final Table table = new Table(shell, SWT.NONE);
		table.setBounds(10,10,350,250);
		table.setHeaderVisible(true);
		TableColumn col0 = new TableColumn(table, SWT.NONE);
		col0.setText("column 0");
		TableColumn col1 = new TableColumn(table, SWT.NONE);
		col1.setText("column 1");
		col0.pack();
		System.out.println("col0 width: " + col0.getWidth());
		System.out.println("widget.table clientArea: " + table.getClientArea());
		System.out.println("col0 width: " + col0.getWidth()); // <--- !!!
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) display.sleep();
		}
		display.dispose();
	}
}
