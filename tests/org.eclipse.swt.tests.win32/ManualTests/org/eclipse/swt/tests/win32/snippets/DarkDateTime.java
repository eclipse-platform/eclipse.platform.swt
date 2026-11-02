/*******************************************************************************
 * Copyright (c) 2026 vogella GmbH and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package org.eclipse.swt.tests.win32.snippets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

/**
 * Shows every DateTime style with the dark theme. Check the field colors, focus and selected
 * field, keyboard editing, the drop-down calendar and the disabled state.
 */
public class DarkDateTime {
	private static Color labelBackground;
	private static Color labelForeground;

	public static void main(String[] args) {
		Display display = new Display();
		display.setData("org.eclipse.swt.internal.win32.useDarkModeExplorerTheme", Boolean.TRUE);
		labelBackground = new Color(0x2B, 0x2B, 0x2B);
		labelForeground = new Color(0xDC, 0xDC, 0xDC);

		Shell shell = new Shell(display);
		shell.setText("DateTime in dark mode");
		shell.setBackground(labelBackground);
		shell.setLayout(new GridLayout(4, false));

		add(shell, "DATE | DROP_DOWN", SWT.DATE | SWT.DROP_DOWN | SWT.BORDER);
		add(shell, "DATE | SHORT", SWT.DATE | SWT.SHORT);
		add(shell, "DATE | MEDIUM", SWT.DATE | SWT.MEDIUM | SWT.BORDER);
		add(shell, "DATE | LONG", SWT.DATE | SWT.LONG);
		add(shell, "TIME", SWT.TIME | SWT.BORDER);
		add(shell, "disabled", SWT.DATE | SWT.DROP_DOWN).setEnabled(false);
		DateTime custom = add(shell, "custom colors", SWT.DATE | SWT.DROP_DOWN);
		custom.setBackground(new Color(0, 60, 90));
		custom.setForeground(new Color(255, 220, 120));
		add(shell, "TIME without border", SWT.TIME);
		add(shell, "CALENDAR", SWT.CALENDAR | SWT.BORDER);
		add(shell, "CALENDAR | WEEKNUMBERS", SWT.CALENDAR | SWT.CALENDAR_WEEKNUMBERS);

		shell.pack();
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) display.sleep();
		}
		display.dispose();
	}

	private static DateTime add(Shell shell, String text, int style) {
		Label label = new Label(shell, SWT.NONE);
		label.setText(text);
		label.setBackground(labelBackground);
		label.setForeground(labelForeground);
		return new DateTime(shell, style);
	}
}
