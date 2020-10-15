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
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

public class Bug427882_StyledText_IME_IllegalArgument {
	public static void main(String[] args) {
		final Display display = new Display();
		final Shell shell = new Shell(display, SWT.SHELL_TRIM);
		RowLayout layout = new RowLayout(SWT.VERTICAL);
		layout.fill = true;
		shell.setLayout(layout);

		new Label(shell, SWT.NONE).setText(
			"Preparation :\n" +
			"1) Use macOS\n" +
			"2) Install keyboard layout:\n" +
			"   Chinese, Simplified | Pinyin\n" +
			"3) Switch to this keyboard layout\n" +
			"\n" +
			"Bug: \n" +
			"1) Type 'modified' in box below, without quotes\n" +
			"   You should see hieroglyph suggestions as you type.\n" +
			"   This time, word will be typed correctly.\n" +
			"2) Type 'modifi' and press 1 to accept suggestion.\n" +
			"3) Type 'modified', SWT will die with 'IllegalArgumentException: Argument not valid'\n" +
 			"\n" +
			"How to recover original state: \n" +
			"1) Delete keyboard layout\n" +
			"2) Reboot macOS\n" +
			"3) Delete file ~/Library/Dictionaries/DynamicPhraseLexicon_zh_Hans.db\n"
		);

		new StyledText(shell, SWT.BORDER);

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
