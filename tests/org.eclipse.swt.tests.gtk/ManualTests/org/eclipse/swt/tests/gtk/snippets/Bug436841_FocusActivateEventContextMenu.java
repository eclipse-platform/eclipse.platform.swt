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
import org.eclipse.swt.events.MenuAdapter;
import org.eclipse.swt.events.MenuEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

public class Bug436841_FocusActivateEventContextMenu {

	public static void main(String[] args) {
		final Display display = new Display();
		Shell shell = new Shell(display);
		shell.setText(Bug436841_FocusActivateEventContextMenu.class.getName());
		shell.setLayout(new GridLayout(1, false));

		Tree tree = new Tree(shell, SWT.BORDER);
		tree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		new TreeItem(tree, SWT.NONE).setText("Open context menu, hover Show In");

		Menu menu = new Menu(tree);
		tree.setMenu(menu);

		new MenuItem(menu, SWT.PUSH).setText("First");

		MenuItem showInItem = new MenuItem(menu, SWT.CASCADE);
		showInItem.setText("Show In");
		Menu showInMenu = new Menu(menu);
		showInItem.setMenu(showInMenu);
		showInMenu.addMenuListener(new MenuAdapter() {
			@Override
			public void menuShown(MenuEvent e) {
				Shell activeShell = display.getActiveShell();
				System.out.println("menuShown: activeShell == " + activeShell);
			}
		});

		display.addFilter(SWT.FocusIn, event -> new Exception("FocusIn: " + event.widget).printStackTrace());

		display.addFilter(SWT.FocusOut, event -> new Exception("FocusOut: " + event.widget).printStackTrace());

		display.addFilter(SWT.Activate, event -> new Exception("Activate: " + event.widget).printStackTrace());

		display.addFilter(SWT.Deactivate, event -> new Exception("Deactivate: " + event.widget).printStackTrace());

//		new Runnable() {
//			@Override
//			public void run() {
//				System.out.print("."); // print a heartbeat to show where the user paused
//				display.timerExec(1000, this);
//			}
//		}.run();

		shell.setSize(400, 100);
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}

}