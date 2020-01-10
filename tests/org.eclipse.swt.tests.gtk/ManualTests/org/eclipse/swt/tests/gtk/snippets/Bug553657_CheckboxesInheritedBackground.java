/*******************************************************************************
 * Copyright (c) 2019, 2020 Thomas Singer and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Thomas Singer - initial implementation to test bug 546552
 *     Paul Pazderski - adaption to test bug 553657
 *******************************************************************************/
package org.eclipse.swt.tests.gtk.snippets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * An inverted toggle must not inherit background colors.
 * <p>
 * Result before fix: toggle state (check mark, dot, ...) is hard to read
 * because it's color is equal or very similar to the toggles background.
 * <p>
 * Expected result after fix: toggle state is readable and toggles background is
 * either as someone would expect for the used theme or equal to the shell's
 * background.
 */
public class Bug553657_CheckboxesInheritedBackground {

	public static void main(String[] args) {
		final Display display = new Display();

		final Shell shell = new Shell(display);
		shell.setText("Test bug 553657");
		shell.setBackground(display.getSystemColor(SWT.COLOR_WHITE));
		shell.setBackgroundMode(SWT.INHERIT_DEFAULT);
		shell.setLayout(new GridLayout(1, false));

		for (int type : new int[] { SWT.CHECK, SWT.RADIO }) {
			for (int i = 0; i < 4; i++) {
				createToggle("Toggle type " + type + " variant " + i, i % 2 == 0, i / 2 == 0, shell, type);
			}
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

	private static void createToggle(String text, boolean selection, boolean enabled, Composite parent, int type) {
		final Button checkbox = new Button(parent, type);
		checkbox.setText(text);
		checkbox.setSelection(selection);
		checkbox.setEnabled(enabled);
	}
}
