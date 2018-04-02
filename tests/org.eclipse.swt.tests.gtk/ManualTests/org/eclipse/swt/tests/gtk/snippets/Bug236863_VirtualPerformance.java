/*******************************************************************************
 * Copyright (c) 2018 Red Hat and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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