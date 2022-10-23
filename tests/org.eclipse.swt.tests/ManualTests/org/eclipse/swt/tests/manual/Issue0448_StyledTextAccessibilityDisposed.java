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

package org.eclipse.swt.tests.manual;

import org.eclipse.swt.*;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public final class Issue0448_StyledTextAccessibilityDisposed {
	private static int AUTO_CLOSE_DELAY = 0;

	public static void main(String[] args) {
		final Display display = new Display();
		final Shell shell = new Shell(display);
		shell.setLayout (new GridLayout (1, true));

		Label hint = new Label(shell, 0);
		hint.setText(
			"With a native snippet from SWT:\n" +
			"1) Run on Win10 v1903+ (not Win11)\n" +
			"2) Click 'Test' button, a Shell with a StyledText will open\n" +
			"3) Run native snippet using 'Issue0448_StyledTextAccessibilityDisposed.bat'\n" +
			"4) Move mouse over StyledText and press Win+F2 so that native snippet starts\n" +
			"   querying IAccessible2 in StyledText\n" +
			"5) Close test Shell\n" +
			"6) SWT will begin to throw 'Widget is disposed' error in 'StyledText.getCharCount'\n" +
			"\n" +
			"Alternatively, with Grammarly 3rd party application:\n" +
			"1) Run on Win10 v1903+ (not Win11)\n" +
			"2) Run Grammarly\n" +
			"3) Edit AUTO_CLOSE_DELAY in this snippet to a value around 300\n" +
			"   If not reproducible, try other values. You need to guess within ~50ms\n" +
			"4) Click 'Test' button\n" +
			"5) SWT will sometimes throw 'Widget is disposed' error in 'StyledText.getCharCount'\n"
		);

		Button button = new Button(shell, 0);
		button.setText("Test");
		button.addListener(SWT.Selection, e -> {
			int numErrors = 0;
			for (int iIteration = 1; ; iIteration++) {
				try {
					if (shell.isDisposed())
						break;

					Shell shell2 = new Shell(shell, SWT.SHELL_TRIM);
					shell2.setLayout(new FillLayout());

					StyledText styledText = new StyledText(shell2, SWT.V_SCROLL | SWT.H_SCROLL);
					styledText.setText(
						"00 Test text\n" +
						"01 Test text\n" +
						"02 Test text\n" +
						"03 Test text\n" +
						"04 Test text\n" +
						"05 Test text\n"
					);

					shell2.setSize(200, 200);
					shell2.open();

					if (AUTO_CLOSE_DELAY != 0) {
						long time0 = System.currentTimeMillis();
						while (time0 + AUTO_CLOSE_DELAY > System.currentTimeMillis()) {
							if (!display.readAndDispatch()) {
								display.sleep();
							}
						}

						shell2.close();

						while (display.readAndDispatch()) {}
					} else {
						while (!shell2.isDisposed()) {
							if (!display.readAndDispatch()) {
								display.sleep();
							}
						}
					}
				} catch (Throwable t) {
					t.printStackTrace();
					numErrors++;
				}

				System.out.format("Iterations=%d errors=%s%n", iIteration, numErrors);
			}
		});

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
