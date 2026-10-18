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
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public final class Bug577129_Win11_NoButtonArrows {
	public static void main(String[] args) {
		final Display display = new Display();
		final Shell shell = new Shell(display);
		shell.setLayout (new GridLayout (1, true));

		Label hint = new Label (shell, 0);
		hint.setText (
			"1) Run on Win11 (Win10 is not enough)\n" +
			"2) Bug 577129: Enabled buttons don't show arrows"
		);

		Composite composite = new Composite(shell, 0);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		composite.setLayout(new GridLayout (5, true));

		Button button;

		new Label(composite, 0).setText("");
		new Label(composite, 0).setText("UP");
		new Label(composite, 0).setText("DOWN");
		new Label(composite, 0).setText("LEFT");
		new Label(composite, 0).setText("RIGHT");

		for (int isEnabled = 0; isEnabled < 2; isEnabled++) {
			if (isEnabled != 0) {
				new Label(composite, 0).setText("Enabled");
			} else {
				new Label(composite, 0).setText("Disabled");
			}

			button = new Button(composite, SWT.ARROW | SWT.UP);
			if (isEnabled == 0) {
				button.setEnabled(false);
			}

			button = new Button(composite, SWT.ARROW | SWT.DOWN);
			if (isEnabled == 0) {
				button.setEnabled(false);
			}

			button = new Button(composite, SWT.ARROW | SWT.LEFT);
			if (isEnabled == 0) {
				button.setEnabled(false);
			}

			button = new Button(composite, SWT.ARROW | SWT.RIGHT);
			if (isEnabled == 0) {
				button.setEnabled(false);
			}
		}

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
