/*******************************************************************************
 * Copyright (c) 2020 Conrad Groth and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Conrad Groth - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.snippets;

import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

/**
 * Example snippet: Styled Text Widget with 50000 lines of text and line wrap.
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 *
 * @since 3.114
 */
public class Snippet376 {

	private static final int LINES = 500000;
	private static final int WORDS_PER_LINE = 10;
	private static final String WORD = "123456789 ";

	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setBounds(10, 10, 300, 300);
		shell.setLayout(new FillLayout());
		final StyledText text = new StyledText(shell, SWT.MULTI | SWT.V_SCROLL | SWT.WRAP);

		StringBuilder buffer = new StringBuilder(LINES * WORDS_PER_LINE * WORD.length() * 2);
		for (int i = 0; i < LINES ; i++) {
			buffer.append("Line " + i + ": ");
			for (int j = 0; j < WORDS_PER_LINE; j++) {
				buffer.append(WORD);
			}
			buffer.append("\n");
		}

		shell.open();
		text.setText(buffer.toString());

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}
}