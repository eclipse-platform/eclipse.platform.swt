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
package org.eclipse.swt.tests.gtk.snippets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;

public class Bug569647_GestureMemoryLeaks {
	public static void main(String[] args) {
		final Display display = new Display();

		final Shell shell = new Shell(display);
		shell.setLayout(new GridLayout(1, true));

		Text hint = new Text(shell, SWT.MULTI | SWT.READ_ONLY);
		hint.setText(
			"1) Open in terminal:\n" +
			"     top -p `pgrep -f Bug569647_GestureMemoryLeaks`\n" +
			"2) Click button, wait ~30sec for 'done' on console\n" +
			"3) Repeat a few times.\n" +
			"4) Without patch, 'RES' will keep growing by ~11mb each click\n" +
			"5) With patch, it stabilizes after ~3 clicks"
		);

		Button btnTest = new Button(shell, 0);
		btnTest.setText("Create Shells");
		btnTest.addListener(SWT.Selection, event -> {
			System.out.format("Creating Shells...");
			for (int i = 0; i < 3000; i++) {
				Shell item = new Shell(display);
				item.dispose();
			}
			System.gc();
			System.out.println ("done");
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