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

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;

public class Bug570238_FontDescriptionLeak {
	public static void main(String[] args) {
		final Display display = new Display();

		final Shell shell = new Shell(display);
		shell.setLayout(new GridLayout(1, true));

		Text hint = new Text(shell, SWT.MULTI | SWT.READ_ONLY);
		hint.setText(
			"1) Run this snippet with -Xmx8m -Xms8m to avoid waiting for Java heap size to stabilize\n" +
			"2) Run `top` and observe 'RES'\n" +
			"3) Every click on 'Test leak' will increase 'RES' by ~1mb"
		);

		Label label = new Label(shell, 0);
		label.setText("This is a label");

		Button button = new Button(shell, 0);
		button.setText("Test leak");
		button.addListener(SWT.Selection, e -> {
			for (int i = 0; i < 10*1024; i++) {
				// What actually leaks here is the result of Control.getFontDescription()
				label.computeSize(SWT.DEFAULT, SWT.DEFAULT);
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