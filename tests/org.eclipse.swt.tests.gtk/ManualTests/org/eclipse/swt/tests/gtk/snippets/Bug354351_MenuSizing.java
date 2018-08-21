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
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;

public class Bug354351_MenuSizing {

		private Display display;
		private Shell shell;

		private void buildUI() {

			shell = new Shell(display,SWT.CLOSE);
			shell.setText("SWT Test");
			shell.setLayout(new GridLayout());

			Menu menuBar = new Menu(shell, SWT.BAR);
			shell.setMenuBar(menuBar);
			MenuItem fileMenu = new MenuItem(menuBar,SWT.CASCADE);
			Menu fileSubmenu = new Menu(shell, SWT.DROP_DOWN);
			fileMenu.setMenu(fileSubmenu);
			fileMenu.setText("File");
			MenuItem menuItem = new MenuItem(fileSubmenu,SWT.NONE);
			menuItem.setText("Menu Item");

			Label l = new Label(shell,SWT.NONE);
			l.setText("This is some random text.");

			Button button = new Button(shell, SWT.NONE);
			button.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			button.setText("Button");

			shell.addListener(SWT.Close, event -> shell.dispose());

			shell.pack();

		}

		public Bug354351_MenuSizing() {

			display = new Display();

			buildUI();

			if (!shell.isDisposed())
				shell.open ();

			while (!shell.isDisposed ()) {
				if (!display.readAndDispatch ()) display.sleep ();
			}

	}


	public static void main(String[] args) {

		new Bug354351_MenuSizing();

	}

}
