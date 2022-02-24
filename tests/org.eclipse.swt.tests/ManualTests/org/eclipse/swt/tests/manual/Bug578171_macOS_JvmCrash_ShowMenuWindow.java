/*******************************************************************************
 * Copyright (c) 2022 Syntevo and others.
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
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class Bug578171_macOS_JvmCrash_ShowMenuWindow {
	public static void main(String[] args) {
		final Display display = new Display();
		final Shell shell = new Shell(display);
		shell.setLayout(new GridLayout(1, true));

		new Label(shell, 0).setText(
			"1) Use macOS 12 (macOS < 12 are not affected)\n" +
			"2) Press the button\n" +
			"3) Click menu bar and move mouse left-right to switch between submenus\n" +
			"4) Bug 578171: Popup Shell will be activated; that's a bug already\n" +
			"5) Bug 578171: JVM will crash"
		);

		Menu rootMenu = new Menu(shell, SWT.BAR);
		shell.setMenuBar(rootMenu);
		for (int iMenu = 0; iMenu < 3; iMenu++) {
			MenuItem rootItem = new MenuItem(rootMenu, SWT.CASCADE);
			rootItem.setText("Menu:" + iMenu);

			Menu menu = new Menu(shell, SWT.DROP_DOWN);
			rootItem.setMenu(menu);

			for (int iItem = 0; iItem < 10; iItem++) {
				MenuItem item = new MenuItem(menu, SWT.CASCADE);
				item.setText("MenuItem:" + iMenu + ":" + iItem);
			}
		}

		Button button = new Button(shell, SWT.PUSH);
		button.setText("Show popup after 2000ms");
		button.addListener(SWT.Selection, e -> {
			display.timerExec(2000, () -> {
				Shell popup = new Shell(shell, SWT.DIALOG_TRIM);
				popup.addListener(SWT.Activate, e2 -> {
					System.out.println("Popup Shell SWT.Activate: It's a bug when it happens without clicking the popup");
				});

				popup.setSize(100, 100);
				popup.setVisible(true);
			});
		});

		new Label(shell, 0).setText("A text field so that you can check if Shell is still active:");
		new Text(shell, 0).setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		shell.pack();
		shell.open();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}

		display.dispose();
	}
}