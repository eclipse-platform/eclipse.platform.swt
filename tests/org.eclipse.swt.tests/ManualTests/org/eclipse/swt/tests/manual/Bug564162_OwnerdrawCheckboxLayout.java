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

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class Bug564162_OwnerdrawCheckboxLayout {
	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setLayout(new RowLayout(SWT.VERTICAL));

		Font biggerFont = new Font(display, "Courier new", 15, SWT.NONE);

		for (int iFont      = 0; iFont < 2;      iFont++)
		for (int iCheckbox  = 0; iCheckbox < 2;  iCheckbox++)
		for (int iBackColor = 0; iBackColor < 2; iBackColor++)
		for (int iDisabled  = 0; iDisabled < 2;  iDisabled++)
		{
			Button button = new Button(shell, (iCheckbox == 0) ? SWT.RADIO : SWT.CHECK);
			button.setText("000 Every control with same font shall have same text position");

			if (iFont != 0)
				button.setFont(biggerFont);

			if (iBackColor != 0)
				button.setForeground(shell.getForeground());

			if (iDisabled != 0)
				button.setEnabled(false);
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
