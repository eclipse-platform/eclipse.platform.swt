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


import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.layout.*;
public class Bug236863_VirtualPerformance {
	static final int ROWS = 3000000;
	public static void main (String [] args) {
		final Display display = new Display ();
		Shell shell = new Shell (display);
		shell.setLayout (new FillLayout ());
		final Table table = new Table (shell, SWT.VIRTUAL);
		table.setHeaderVisible (true);
		table.addListener (SWT.SetData, new Listener () {
			@Override
			public void handleEvent (Event e) {
				TableItem item = (TableItem) e.item;
				item.setText ("test");
			}
		});
		TableColumn column = new TableColumn (table, SWT.NONE);
		column.setText ("Column 1");
		column.setWidth (80);

		long start = System.currentTimeMillis();
		table.setItemCount (ROWS);
		System.out.println("" + (System.currentTimeMillis() - start) + "ms");

		shell.open ();
		while (!shell.isDisposed ()) {
			if (!display.readAndDispatch ()) display.sleep ();
		}
		display.dispose ();
	}
}