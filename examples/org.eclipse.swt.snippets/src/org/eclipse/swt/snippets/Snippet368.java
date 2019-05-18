/**
 *  Copyright (c) 2017 Angelo ZERR.
 *
 *  This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License 2.0
 *  which accompanies this distribution, and is available at
 *  https://www.eclipse.org/legal/epl-2.0/
 *
 *  SPDX-License-Identifier: EPL-2.0
 *
 *  Contributors:
 *  Angelo Zerr <angelo.zerr@gmail.com> - Customize different line spacing of StyledText - Bug 522020
 */
package org.eclipse.swt.snippets;

/*
 * example snippet: customize line spacing provider
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 *
 * @since 3.107
 */
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class Snippet368 {

	public static void main(String[] args) throws Exception {
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setLayout(new FillLayout());
		shell.setText("Customize line spacing provider");

		StyledText text = new StyledText(shell, SWT.BORDER | SWT.V_SCROLL);
		text.setText("a\nb\nc\nd");
		text.setLineSpacingProvider(lineIndex -> {
			if (lineIndex == 0) {
				return 10;
			} else if (lineIndex == 1) {
				return 30;
			}
			return null;
		});

		shell.pack();
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}
}
