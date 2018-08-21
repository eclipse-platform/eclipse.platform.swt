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
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

public class Bug83833_SetTopIndexSelectionEvent {
	public static void main(String [] args) {
		Display display = new Display();
		Shell shell = new Shell(display);
		Table table = new Table(shell, SWT.NONE);
		ScrollBar scrollBar = table.getVerticalBar();
		scrollBar.setEnabled(false);
		scrollBar.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {}
			@Override
			public void widgetSelected(SelectionEvent e) {
				System.out.println("Selection event");
			}
		});
		for(int i = 0; i < 500; i++) {
			TableItem item = new TableItem(table, SWT.NONE);
			item.setText(0, "This is item " + i);
		}
		table.setTopIndex(20);
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) display.sleep();
		}
		display.dispose();
	}
}
