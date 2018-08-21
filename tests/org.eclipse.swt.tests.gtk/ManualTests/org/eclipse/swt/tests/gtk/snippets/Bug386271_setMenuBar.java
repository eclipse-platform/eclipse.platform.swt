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
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;

public class Bug386271_setMenuBar {

	public static void main(String[] args) {

		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setText("Test Bug?");
		shell.setSize(1000,550);
		shell.setLayout(new FormLayout());

		Label testLabel = new Label(shell, SWT.BORDER);
		testLabel.setText("Some text");

	    FormData fdata = new FormData();
	    fdata.left = new FormAttachment(0);
	    fdata.right = new FormAttachment(100);
	    fdata.bottom = new FormAttachment(100);
	    fdata.height = 15;
	    testLabel.setLayoutData(fdata);

	    /*When you uncomment the following line, the menu gets created
	     * but the label is pushed back to the bottom and disappears from view.
	     * To me, it seems that the 'bottom' is pushed further down outside the
	     * bottom border shell.
	     */
	    shell.setMenuBar(returnMenu(shell));

	    shell.open();
	    while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}

	    display.dispose();

	}

	private static Menu returnMenu(Shell shell) {
	    Menu menu = new Menu(shell, SWT.BAR);

	    MenuItem fileMenuHeader = new MenuItem(menu, SWT.CASCADE);
	    fileMenuHeader.setText("&File");

	    Menu fileMenu = new Menu(shell, SWT.DROP_DOWN);
	    fileMenuHeader.setMenu(fileMenu);

	    MenuItem fileNewItem = new MenuItem(fileMenu, SWT.PUSH);
	    fileNewItem.setText("&New\tCtrl+N");
	    fileNewItem.setAccelerator(SWT.CTRL + 'N');

	    return menu;
	}

}