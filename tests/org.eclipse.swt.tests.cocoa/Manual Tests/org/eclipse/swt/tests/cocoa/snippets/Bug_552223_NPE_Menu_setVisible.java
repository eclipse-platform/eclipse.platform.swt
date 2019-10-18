/*******************************************************************************
 * Copyright (c) 2019 Lablicate GmbH and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 * Christoph Läubrich - Bug 552223 - [cocoa] NPE in Menu._setVisible when clicking a menuitem of a disposed menu
 *******************************************************************************/
package org.eclipse.swt.tests.cocoa.snippets;

import java.util.concurrent.TimeUnit;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MenuEvent;
import org.eclipse.swt.events.MenuListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;

public class Bug_552223_NPE_Menu_setVisible {

	public static void main(String[] args) {

		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setText("Bug 552223");
		shell.open();
		Menu menu = new Menu(shell);
		MenuItem menuItem = new MenuItem(menu, SWT.NONE);
		menuItem.setText("If you click me within three seconds everything will be fine");
		menuItem.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				System.out.println("Item was clicked");
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {

			}
		});
		menu.addMenuListener(new MenuListener() {

			@Override
			public void menuShown(MenuEvent e) {

				Display.getDefault().timerExec((int)TimeUnit.SECONDS.toMillis(3), () -> {

					menu.dispose();
					System.out.println("The menu is now disposed!");
				});
			}

			@Override
			public void menuHidden(MenuEvent e) {

			}
		});
		shell.setMenu(menu);
		while(!shell.isDisposed()) {
			if(!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();
	}
}