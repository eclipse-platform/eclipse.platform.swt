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
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

public class Bug209977_GCEraseItemEventBackground {

	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setBounds(10,10,200,200);
		final Table table = new Table(shell, SWT.NONE);
		table.setBounds(10,10,100,100);
		new TableItem(table, SWT.NONE).setText("selected item");
		Text text = new Text(shell, SWT.SINGLE);
		text.setBounds(10,140,100,30);
		table.addListener(SWT.EraseItem, event -> {
			if ((event.detail & SWT.SELECTED) == 0) return;
			if (table.isFocusControl()) {
				System.out.print("selection background while focused: ");
			} else {
				System.out.print("selection background while not focused: ");
			}
			System.out.println(event.gc.getBackground());
		});
		table.select(0);
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) display.sleep();
		}
		display.dispose();
	}

}
