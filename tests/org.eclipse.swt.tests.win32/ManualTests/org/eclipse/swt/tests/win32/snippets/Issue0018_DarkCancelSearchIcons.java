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

package org.eclipse.swt.tests.win32.snippets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.internal.win32.OS;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

@SuppressWarnings("restriction")
public class Issue0018_DarkCancelSearchIcons {
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
		shell.setLayout(new GridLayout(1, true));

		new Label(shell, 0).setText(
			"1) Use Windows\n" +
			"2) Issue #18: Icons in search field are hard to see"
		);

		Text text = new Text(shell, SWT.SEARCH | SWT.ICON_SEARCH | SWT.ICON_CANCEL);
		text.setText("I'm a search box");
		text.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

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