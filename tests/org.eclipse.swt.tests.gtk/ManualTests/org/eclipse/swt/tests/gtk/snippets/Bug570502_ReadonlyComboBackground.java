/*******************************************************************************
 * Copyright (c) 2021 Syntevo and others.
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

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class Bug570502_ReadonlyComboBackground {
	public static void main(String[] args) {
		final Display display = new Display();
		final Color clrBack = new Color(0x41, 0x41, 0x41);
		final Color clrFore = new Color(0xcf, 0xcf, 0xcf);

		final Shell shell = new Shell(display);
		shell.setLayout(new GridLayout());
		shell.setForeground(clrFore);
		shell.setBackground(clrBack);

		Label hint = new Label(shell, 0);
		hint.setText(
			"There are multiple problems with Combo colors :\n" +
			"1) Fore+Back, READ_ONLY: Background color and button's triangle are wrong\n" +
			"2) Back+Fore, DROP_DOWN: Button's triangle is wrong\n" +
			"3) Back+Fore, SIMPLE: Button's triangle is wrong\n" +
			"4) Back only: fore colors are weird, not fixed in this patch"
		);

		Composite composite = new Composite(shell, 0);
		composite.setForeground(clrFore);
		composite.setBackground(clrBack);
		composite.setLayout(new GridLayout(7, false));

		int styles[] = {SWT.READ_ONLY, SWT.DROP_DOWN, SWT.SIMPLE};
		new Label(composite, 0).setText("");
		new Label(composite, 0).setText("Default");
		new Label(composite, 0).setText("Back+Fore, then default");
		new Label(composite, 0).setText("Fore only");
		new Label(composite, 0).setText("Back only");
		new Label(composite, 0).setText("Fore+Back");
		new Label(composite, 0).setText("Back+Fore");

		for (int style : styles) {
			switch (style) {
				case SWT.READ_ONLY:
					new Label(composite, 0).setText("READ_ONLY");
					break;
				case SWT.DROP_DOWN:
					new Label(composite, 0).setText("DROP_DOWN");
					break;
				case SWT.SIMPLE:
					new Label(composite, 0).setText("SIMPLE");
					break;
			}

			for (int iColoring = 0; iColoring < 6; iColoring++) {
				final Combo combo = new Combo(composite, style);
				combo.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
				combo.setItems("Item 1", "Item 2");
				combo.select(0);

				switch (iColoring) {
					case 0:
						break;
					case 1:
						combo.setBackground(clrBack);
						combo.setForeground(clrFore);
						combo.setBackground(null);
						combo.setForeground(null);
						break;
					case 2:
						combo.setForeground(clrFore);
						break;
					case 3:
						combo.setBackground(clrBack);
						break;
					case 4:
						combo.setForeground(clrFore);
						combo.setBackground(clrBack);
						break;
					case 5:
						combo.setBackground(clrBack);
						combo.setForeground(clrFore);
						break;
				}
			}
		}

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
