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


import org.eclipse.swt.widgets.*;
import org.eclipse.swt.*;


public class Bug399522_TreeColumnNumber {
public static void main(String[] args) {
	Display display = new Display();
	Shell shell = new Shell(display);

	final Table table = new Table(shell, SWT.BORDER);
	new TableItem(table, SWT.NONE).setText("Item1");
	new TableColumn(table, SWT.NONE);
	
	table.setSize(200, 200);
	
	shell.addListener(SWT.MouseDown, new Listener() {
		@Override
		public void handleEvent(Event event) {
			System.out.println(table.computeSize(-1, -1));
		}
	});

	shell.setSize(300, 300);
	shell.open();
	while (!shell.isDisposed()) {
		if (!display.readAndDispatch())
			display.sleep();
	}
	display.dispose();
}
}