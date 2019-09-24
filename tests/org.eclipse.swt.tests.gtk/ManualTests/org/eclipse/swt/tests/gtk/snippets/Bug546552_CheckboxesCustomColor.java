/*******************************************************************************
 * Copyright (c) 2019 Thomas Singer and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Thomas Singer - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tests.gtk.snippets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class Bug546552_CheckboxesCustomColor {

	public static void main(String[] args) {
		final Display display = new Display();

		final Shell shell = new Shell(display);
		shell.setLayout(new GridLayout(1, false));

		for (int i = 0; i < 4; i++) {
			createCheckbox("Checkbox " + i, i % 2 == 0, i / 2 == 0, shell);
		}

		shell.setSize(400, 300);
		shell.open();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}

		display.dispose();
	}

	private static void createCheckbox(String text, boolean selection, boolean enabled, Composite parent) {
		final Button checkbox = new Button(parent, SWT.CHECK);
		checkbox.setText(text);
		checkbox.setSelection(selection);
		checkbox.setEnabled(enabled);

		final Display display = parent.getDisplay();
		checkbox.setBackground(display.getSystemColor(SWT.COLOR_WHITE));
		checkbox.setForeground(display.getSystemColor(SWT.COLOR_BLACK));
	}
}
