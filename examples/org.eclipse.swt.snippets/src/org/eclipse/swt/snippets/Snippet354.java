/*******************************************************************************
 * Copyright (c) 2000, 2022 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.snippets;

/*
 * Display snippet: use the application system menu when available.
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 *
 * @since 3.7
 */
import static org.eclipse.swt.events.SelectionListener.*;

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class Snippet354 {

	static MenuItem getItem(Menu menu, int id) {
		MenuItem[] items = menu.getItems();
		for (MenuItem item : items) {
			if (item.getID() == id) return item;
		}
		return null;
	}

	public static void main(String[] args) {
		final Display display = new Display();
		Shell shell = new Shell(display);
		shell.setText("Snippet 354");
		shell.setLayout(new GridLayout(1, false));

		Menu appMenuBar = display.getMenuBar();
		if (appMenuBar == null) {
			appMenuBar = new Menu(shell, SWT.BAR);
			shell.setMenuBar(appMenuBar);
		}
		MenuItem file = new MenuItem(appMenuBar, SWT.CASCADE);
		file.setText("File");
		Menu dropdown = new Menu(appMenuBar);
		file.setMenu(dropdown);
		MenuItem exit = new MenuItem(dropdown, SWT.PUSH);
		exit.setText("Exit");
		exit.addSelectionListener(widgetSelectedAdapter(e -> display.dispose()));

		Listener keyDownFilter = event -> System.out.println("Key event!");
		display.addFilter(SWT.KeyDown, keyDownFilter);
		display.addFilter(SWT.KeyUp, keyDownFilter);

		ArmListener armListener = System.out::println;

		Menu systemMenu = display.getSystemMenu();
		if (systemMenu != null) {
			systemMenu.addMenuListener(new MenuListener() {
				@Override
				public void menuHidden(MenuEvent e) {
					System.out.println("App menu closed");
				}

				@Override
				public void menuShown(MenuEvent e) {
					System.out.println("App menu opened");
				}
			});

			MenuItem sysItem = getItem(systemMenu, SWT.ID_QUIT);
			sysItem.addArmListener(armListener);
			sysItem.addSelectionListener(widgetSelectedAdapter(e -> System.out.println("Quit selected")));
			sysItem = getItem(systemMenu, SWT.ID_HIDE_OTHERS);
			sysItem.addArmListener(armListener);
			sysItem.addSelectionListener(widgetSelectedAdapter(e -> {
				System.out.println("Hide others selected -- and blocked!");
				e.doit = false;
			}));
			sysItem = getItem(systemMenu, SWT.ID_HIDE);
			sysItem.addArmListener(armListener);
			sysItem.addSelectionListener(widgetSelectedAdapter(e -> {
				System.out.println("Hide selected -- and blocked!");
				e.doit = false;
			}));
			sysItem = getItem(systemMenu, SWT.ID_PREFERENCES);
			sysItem.addArmListener(armListener);
			sysItem.addSelectionListener(widgetSelectedAdapter(e -> System.out.println("Preferences selected")));
			sysItem = getItem(systemMenu, SWT.ID_ABOUT);
			sysItem.addArmListener(armListener);
			sysItem.addSelectionListener(widgetSelectedAdapter(e -> System.out.println("About selected")));

			int prefsIndex = systemMenu.indexOf(getItem(systemMenu, SWT.ID_PREFERENCES));
			MenuItem newAppMenuItem = new MenuItem(systemMenu, SWT.CASCADE, prefsIndex + 1);
			newAppMenuItem.setText("SWT-added item");
			newAppMenuItem.setAccelerator(SWT.MOD1 | 'i');
			newAppMenuItem.addArmListener(armListener);
			newAppMenuItem.addSelectionListener(widgetSelectedAdapter(e -> System.out.println("SWT-added item selected")));
			Menu subMenu = new Menu(systemMenu);

			for (int i = 0; i < 4; i++) {
				MenuItem subItem = new MenuItem(subMenu, SWT.PUSH);
				subItem.setText("Item " + i);
			}
			newAppMenuItem.setMenu(subMenu);
		}
		Button b = new Button(shell, SWT.PUSH);
		b.setText("Test");
		shell.pack();
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}
}