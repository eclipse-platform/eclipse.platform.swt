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

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.internal.win32.OS;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;

@SuppressWarnings("restriction")
public class Bug563473_DarkProgressBar {
	static void setColors(Control control, Color backColor, Color foreColor) {
		control.setBackground(backColor);
		control.setForeground(foreColor);

		if (control instanceof Composite) {
			for (Control child : ((Composite)control).getChildren()) {
				setColors(child, backColor, foreColor);
			}
		}
	}

	public static void main(String[] args) {
		final Display display = new Display();
		OS.setTheme(true);

		final Shell shell = new Shell(display);
		GridLayout layout = new GridLayout(1, true);
		layout.horizontalSpacing = 10;
		shell.setLayout(layout);

		Label hint = new Label(shell, 0);
		hint.setText("The ProgressBar's background shall look good in Dark Theme");

		ProgressBar progress = new ProgressBar(shell, 0);
		progress.setSelection(75);

		Color backColor = new Color(display, 0x30, 0x30, 0x30);
		Color foreColor = new Color(display, 0xD0, 0xD0, 0xD0);
		setColors(shell, backColor, foreColor);

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
