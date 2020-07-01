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
package org.eclipse.swt.tests.manual;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.*;

public class Bug564595_PopupMenuSwtShowEvent {
	public static void main(String[] args) {
		final Display display = new Display();
		final Shell shell = new Shell(display);

		RowLayout layoutV = new RowLayout(SWT.VERTICAL);
		layoutV.marginHeight = layoutV.marginWidth = 10;
		shell.setLayout(layoutV);

		Label hint = new Label(shell, SWT.NONE);
		hint.setText(
			"This snippet is to test\n" +
			"'SWT.Show' and 'SWT.Hide' in menus.\n" +
			"\n" +
			"Right-click controls below to see menu, watch STDOUT for events\n" +
			"\n" +
			"Reproducing a problem where GTK fails to show menu:\n" +
			"1. Use GNOME on XOrg session\n" +
			"2. In GNOME settings, Region & Language :\n" +
			"   Select 'Allow different sources for each window' from 'Options' button\n" +
			"3. When some other window is active, right-click this snippet\n" +
			"4. SWT shall not report SWT.Show event in this case"
		);

		Composite composite = new Composite(shell, SWT.NONE);
		RowLayout layoutH = new RowLayout(SWT.HORIZONTAL);
		layoutH.marginHeight = layoutH.marginWidth = 20;
		composite.setLayout(layoutH);

		{
			Label label = new Label(composite, SWT.BORDER | SWT.CENTER);
			label.setText("I show\nnormal menu");

			Menu menu = new Menu(label);
			new MenuItem(menu, SWT.NONE).setText("Menu 1");
			new MenuItem(menu, SWT.NONE).setText("Menu 2");
			new MenuItem(menu, SWT.NONE).setText("Menu 3");

			menu.addListener(SWT.Show,    e -> System.out.println("Menu: SWT.Show"));
			menu.addListener(SWT.Hide,    e -> System.out.println("Menu: SWT.Hide"));

			label.setMenu(menu);
		}

		{
			Label label = new Label(composite, SWT.BORDER | SWT.CENTER);
			label.setText("I show\nempty menu");

			Menu menu = new Menu(label);

			menu.addListener(SWT.Show,    e -> System.out.println("Menu: SWT.Show"));
			menu.addListener(SWT.Hide,    e -> System.out.println("Menu: SWT.Hide"));

			label.setMenu(menu);
		}

		{
			Label label = new Label(composite, SWT.BORDER | SWT.CENTER);
			label.setText("I show\nmenu populated in SWT.Show");

			Menu menu = new Menu(label);

			menu.addListener(SWT.Show,    e -> {
				System.out.println("Menu: SWT.Show");

				if (menu.getItemCount() == 0) {
					new MenuItem(menu, SWT.NONE).setText("Menu 1");
					new MenuItem(menu, SWT.NONE).setText("Menu 2");
					new MenuItem(menu, SWT.NONE).setText("Menu 3");
				}
			});

			menu.addListener(SWT.Hide,    e -> System.out.println("Menu: SWT.Hide"));

			label.setMenu(menu);
		}

		shell.pack();
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}
}