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
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;

public class Bug565526_SpaceSizes {
	public static void main(String[] args) {
		final Display display = new Display();

		final Shell shell = new Shell(display);
		shell.setLayout(new GridLayout(1, true));

		final Label hint = new Label(shell, SWT.WRAP);
		hint.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL));
		hint.setText("Problem: spaces in row (3) after the \u203b are too narrow");

		StyledText text = new StyledText(shell, 0);

		FontData fontData = text.getFont().getFontData()[0];
		fontData.setName("Courier new");
		text.setFont(new Font(display, fontData));

		text.setText(
			"(1) ...... 012345 ......\n" +
			"(2) 012345 0\u203b2345 012345\n" +
			"(3) 012345 0\u203b     012345\n" +
			"(4) ...... 012345 ......"
		);

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
