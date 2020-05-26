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

package org.eclipse.swt.tests.gtk.snippets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.*;

public class Bug563555_IsReadyPopup {
	static void createShells(Shell parent, int x, int y, int cx, int cy, int level, String name) {
		int shellCy = cy / 2;

		FillLayout layout = new FillLayout();
		layout.marginHeight = 10;
		layout.marginWidth = 10;

		for (int i = 0; i < 2; i++) {
			String shellName = name + (char)('0' + i);
			int shellStyle = (level == 0) ? SWT.SHELL_TRIM : SWT.DIALOG_TRIM;

			Shell shell = new Shell(parent, shellStyle);
			shell.setLayout(layout);
			shell.setText(shellName);

			Button button = new Button(shell, SWT.NONE);
			button.setText("Hide");
			button.addListener(SWT.Selection, e -> shell.setVisible(false));

			int shellY = y + i*shellCy;
			shell.setBounds(x, shellY, cx, shellCy);
			shell.open();

			shell.addListener(SWT.Activate, e -> {
				System.out.format("%s was activated\n", shellName);
			});

			if (level < 1)
				createShells(shell, x + cx, shellY, cx, shellCy, level + 1, shellName);
		}
	}

	public static void main (String [] args) {
		Display display = new Display ();

		FillLayout layout = new FillLayout();
		layout.marginHeight = 10;
		layout.marginWidth = 10;

		Shell shell = new Shell(display);
		shell.setLayout(layout);
		shell.setText("Test");

		Label hint = new Label(shell, SWT.NONE);
		hint.setText(
			"How to reproduce Bug 563555:\n" +
			"1. IMPORTANT: Click this Shell's contents\n" +
			"2. IMPORTANT: Move other Shell to obscure this Shell\n" +
			"3. Close other Shell with (x) button\n" +
			"4. There will be a quick GNOME popup on top of screen:\n" +
			"   \"Test\" is ready\n" +
			"\n" +
			"Verifying that `Shell.fixActiveShell()` was not needed:\n" +
			"1. Choose any Shell you like\n" +
			"2. Click that Shell to activate it\n" +
			"3. Close/hide that Shell\n" +
			"4. Shell's parent Shell will be activated\n" +
			"   This happens with or without `Shell.fixActiveShell()`"
		);

		final int x = 100;
		final int y = 100;
		final int cx = 600;
		final int cy = 800;
		shell.setBounds(x, y, cx, cy);
		shell.open();

		createShells(shell, x + cx, y, 200, cy, 0, "Shell#");

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch ()) display.sleep ();
		}

		display.dispose ();
	}
}
