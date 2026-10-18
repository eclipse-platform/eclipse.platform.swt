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

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class Bug568590_SpinnerText {
	public static void main(String[] args) {
		final Display display = new Display();

		// Uncomment to test Dark Theme control sizes
		// display.setData("org.eclipse.swt.internal.win32.Text.use_WS_BORDER", true);

		FontData fontData = display.getSystemFont().getFontData()[0];
		// fontData.height *= 2;
		Font font = new Font(display, fontData);

		final Shell shell = new Shell(display);
		shell.setLayout(new GridLayout(4, true));

		Text textB = new Text(shell, SWT.BORDER);
		textB.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		textB.setFont(font);
		textB.setText("23");

		Spinner spinnerB = new Spinner(shell, SWT.BORDER);
		spinnerB.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		spinnerB.setFont(font);
		spinnerB.setSelection(23);

		Text textF = new Text(shell, 0);
		textF.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		textF.setFont(font);
		textF.setText("23");

		Spinner spinnerF = new Spinner(shell, 0);
		spinnerF.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		spinnerF.setFont(font);
		spinnerF.setSelection(23);

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