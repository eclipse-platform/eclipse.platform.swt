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
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class Bug568501_JniWarningDisposingAccessible {
	public static void main(String[] args) {
		// The parent Shell has to be modal to reproduce
		final Display display = new Display();
		final Shell shell = new Shell(display);
		shell.setLayout(new GridLayout());

		final Label hint = new Label(shell, 0);
		hint.setText(
			"1) Run this snippet on Linux with -Xcheck:jni\n" +
			"2) Click button below\n" +
			"3) Observe JNI warnings on console"
		);

		final Button btnTest = new Button(shell, SWT.PUSH);
		btnTest.setText("Show me the bug");
		btnTest.addListener(SWT.Selection, event1 -> {
			Shell shell2 = new Shell(shell, SWT.SHELL_TRIM);

			// Somehow 2 is not enough, let's have more to be sure.
			for (int i = 0; i < 16; i++) {
				StyledText styledText = new StyledText(shell2, 0);
				styledText.setText("I'm a StyledText");
			}

			shell2.dispose();
		});

		shell.pack();
		shell.open();

		while (!shell.isDisposed()) {
			if (display.readAndDispatch()) {
				display.sleep();
			}
		}

		display.dispose();
	}
}
