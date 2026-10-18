/*******************************************************************************
 * Copyright (c) 2019 Thomas Singer and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Thomas Singer - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tests.gtk.snippets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class Bug551624_ShellOpenOverflow {

	public static void main(String[] args) {
		final Display display = new Display();

		final Shell shell = new Shell(display);
		shell.setToolTipText("Top-level window");
		shell.setSize(500, 300);

		shell.addListener(SWT.Show, event -> {
			System.err.println("Got SWT.Show");

			final Shell subShell = new Shell(shell, SWT.PRIMARY_MODAL | SWT.DIALOG_TRIM);
			subShell.setText("Dialog");
			subShell.setSize(300, 200);
			subShell.open();
		});

		shell.open();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}

		display.dispose();
	}
}
