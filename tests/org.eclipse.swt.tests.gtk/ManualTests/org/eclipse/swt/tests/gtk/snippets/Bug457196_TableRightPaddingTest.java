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
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

public class Bug457196_TableRightPaddingTest {

	public static void main(String[] a) {
		Shell shell = new Shell(Display.getDefault());
		shell.setSize(200, 180);
		shell.setLayout(new FillLayout());

		Table t = new Table(shell, SWT.BORDER);
		t.setHeaderVisible(true);

		TableColumn tc1 = new TableColumn(t, SWT.NONE);
		TableColumn tc2 = new TableColumn(t, SWT.NONE);
		tc1.setText("First Name");
		tc2.setText("Last Name");
		tc1.setWidth(70);
		tc2.setWidth(70);

		/*t.addListener(SWT.MeasureItem, new Listener() {
			public void handleEvent(Event event) {				
				event.height = 30;
			}
		});*/

		new TableItem(t, SWT.NONE).setText(new String[] { "Tim", "Hatton" });
		new TableItem(t, SWT.NONE).setText(new String[] { "Caitlyn", "Warner" });

		shell.open();
		shell.layout();

		Display display = Display.getDefault();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}

		display.dispose();
	}
}
