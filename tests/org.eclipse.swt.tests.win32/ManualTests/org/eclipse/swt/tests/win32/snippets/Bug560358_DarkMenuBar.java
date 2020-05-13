/*******************************************************************************
 * Copyright (c) 2020 Syntevo and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Syntevo - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tests.win32.snippets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.*;

public class Bug560358_DarkMenuBar {
	static Image createMenuImage(Device a_Device) {
		Image result = new Image(a_Device, 16, 16);
		GC gc = new GC(result);

		gc.setBackground(a_Device.getSystemColor(SWT.COLOR_BLUE));
		gc.fillRectangle(0, 0, 16, 16);

		gc.setForeground(a_Device.getSystemColor(SWT.COLOR_RED));
		gc.drawOval(4, 4, 8, 8);

		gc.dispose();
		return result;
	}

	static void setMenuItemName(MenuItem item) {
		StringBuilder itemText = new StringBuilder();

		if      ((item.getStyle() & SWT.SEPARATOR) != 0)
			return;
		else if ((item.getStyle() & SWT.CASCADE) != 0)
			itemText.append("SWT.CASCADE");
		else if ((item.getStyle() & SWT.PUSH) != 0)
			itemText.append("SWT.PUSH");
		else if ((item.getStyle() & SWT.CHECK) != 0)
			itemText.append("SWT.CHECK");
		else if ((item.getStyle() & SWT.RADIO) != 0)
			itemText.append("SWT.RADIO");
		else
			throw new RuntimeException();

		if (!item.getEnabled())
			itemText.append(" + disabled");

		if (item.getImage() != null)
			itemText.append(" + image");

		if (item.getSelection())
			itemText.append(" + selection");

		itemText.append("\tCtrl+A");

		item.setText(itemText.toString());
	}

	static void createMenus(Menu a_Parent, Image menuImage, int a_Level) {
		// SWT.CASCADE
		if (a_Level < 1) {
			for (int isEnabled = 0; isEnabled < 2; isEnabled++) {
				for (int isImage = 0; isImage < 2; isImage++) {
					MenuItem item = new MenuItem(a_Parent, SWT.CASCADE);
					item.setEnabled(isEnabled != 0);
					item.setImage((isImage != 0) ? menuImage : null);
					setMenuItemName(item);

					Menu subMenu = new Menu(a_Parent.getShell(), SWT.DROP_DOWN);
					item.setMenu(subMenu);

					createMenus(subMenu, menuImage, a_Level + 1);
				}
			}

			new MenuItem(a_Parent, SWT.SEPARATOR);
		}

		// SWT.PUSH
		{
			for (int isEnabled = 0; isEnabled < 2; isEnabled++) {
				for (int isImage = 0; isImage < 2; isImage++) {
					MenuItem item = new MenuItem(a_Parent, SWT.PUSH);
					item.setEnabled(isEnabled != 0);
					item.setImage((isImage != 0) ? menuImage : null);
					setMenuItemName(item);
				}
			}

			new MenuItem(a_Parent, SWT.SEPARATOR);
		}

		// SWT.CHECK
		{
			for (int isEnabled = 0; isEnabled < 2; isEnabled++) {
				for (int isChecked = 0; isChecked < 2; isChecked++) {
					for (int isImage = 0; isImage < 2; isImage++) {
						MenuItem item = new MenuItem(a_Parent, SWT.CHECK);
						item.setEnabled(isEnabled != 0);
						item.setImage((isImage != 0) ? menuImage : null);
						item.setSelection(isChecked != 0);
						setMenuItemName(item);
					}
				}
			}

			new MenuItem(a_Parent, SWT.SEPARATOR);
		}

		// SWT.RADIO
		{
			for (int isEnabled = 0; isEnabled < 2; isEnabled++) {
				for (int isImage = 0; isImage < 2; isImage++) {
					for (int isChecked = 0; isChecked < 2; isChecked++) {
						MenuItem item = new MenuItem(a_Parent, SWT.RADIO);
						item.setEnabled(isEnabled != 0);
						item.setImage((isImage != 0) ? menuImage : null);
						item.setSelection(isChecked != 0);
						setMenuItemName(item);
					}
				}
			}
		}
	}

	public static void main (String [] args) {
		Display display = new Display ();
		Shell shell = new Shell (display);

		RowLayout layout = new RowLayout(SWT.VERTICAL);
		layout.marginHeight = 10;
		layout.marginWidth = 10;
		layout.spacing = 10;
		shell.setLayout(layout);

		Color backColor   = new Color(display, 0x30, 0x30, 0x30);
		Color foreColor   = new Color(display, 0xD0, 0xD0, 0xD0);
		Color borderColor = new Color(display, 0x50, 0x50, 0x50);
		display.setData("org.eclipse.swt.internal.win32.menuBarBackgroundColor", backColor);
		display.setData("org.eclipse.swt.internal.win32.menuBarForegroundColor", foreColor);
		display.setData("org.eclipse.swt.internal.win32.menuBarBorderColor", borderColor);

		final Text labelInfo = new Text(shell, SWT.READ_ONLY | SWT.MULTI);
		labelInfo.setText("This snippet is for testing menubar coloring on Windows.");

		Image menuImage = createMenuImage(display);

		// Create menus
		Menu rootMenu = new Menu(shell, SWT.BAR);
		shell.setMenuBar(rootMenu);
		for (int i = 0; i < 3; i++)
		{
			StringBuilder menuName = new StringBuilder("SWT.CASCADE");
			menuName.insert(i, '&');

			MenuItem item = new MenuItem(rootMenu, SWT.CASCADE);
			item.setText(menuName.toString());

			Menu subMenu = new Menu(shell, SWT.DROP_DOWN);
			item.setMenu(subMenu);

			createMenus(subMenu, menuImage, 0);
		}

		// Set shell colors
		shell.setBackground(backColor);
		shell.setForeground(foreColor);
		labelInfo.setBackground(backColor);
		labelInfo.setForeground(foreColor);

		// Pack and show shell
		shell.pack();
		shell.open();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch ()) display.sleep ();
		}

		display.dispose ();
	}
}
