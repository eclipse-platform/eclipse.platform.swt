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

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;

public class Bug568033_StyledText_CRLF_IAE {
	public static void main(String[] args) {
		final Display display = new Display();

		final Shell shell = new Shell(display);
		shell.setLayout(new GridLayout(1, true));

		final Label hint = new Label(shell, SWT.WRAP);
		hint.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		hint.setText(
			"Problem: IllegalArgumentException occurs in some scenarios\n" +
			"\n" +
			"Reproducing on any platform with keyDown event:\n" +
			"1) Select 'Selectme' in StyledText below\n" +
			"2) Hit Backspace to delete it\n" +
			"3) Press 'A' key\n" +
			"\n" +
			"Reproducing with IME on any platform:\n" +
			"1) Install keyboard layout with IME, such as Chinese Simplified - Pinyin\n" +
			"2) Select 'Selectme' in StyledText below\n" +
			"3) Hit Backspace to delete it\n" +
			"4) Press 'A' key and select any hieroglyph\n" +
			"\n" +
			"Reproducing with 'handleCompositionChanged' event with Emoji on Win10:\n" +
			"1) Select 'Selectme' in StyledText below\n" +
			"2) Press Win+.\n" +
			"3) Select any emoji\n"
		);

		StyledText text = new StyledText(shell, 0);
		text.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		// Problem is caused by \r\n separated with some text
		text.setText("\rSelectme\n");

		final Button button = new Button(shell, 0);
		button.setText("Test that errors are still caught");
		button.addListener(SWT.Selection, event -> {
			text.setText("0123\r\n6789");

			try {
				text.replaceTextRange(5, 0, "x");
				System.out.println("Test1: BAD!");
			} catch (IllegalArgumentException ex) {
				System.out.println("Test1: ok");
			}

			try {
				text.replaceTextRange(0, 5, "x");
				System.out.println("Test2: BAD!");
			} catch (IllegalArgumentException ex) {
				System.out.println("Test2: ok");
			}

			try {
				text.replaceTextRange(5, 5, "x");
				System.out.println("Test3: BAD!");
			} catch (IllegalArgumentException ex) {
				System.out.println("Test3: ok");
			}

			try {
				text.replaceTextRange(4, 2, "|");
				System.out.println("Test4: ok");
			} catch (IllegalArgumentException ex) {
				System.out.println("Test4: BAD!");
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
