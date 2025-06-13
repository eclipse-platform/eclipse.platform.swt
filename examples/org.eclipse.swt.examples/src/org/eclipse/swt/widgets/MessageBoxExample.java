/*******************************************************************************
 * Copyright (c) 2025 Advantest Europe GmbH and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 * 				Raghunandana Murthappa (Advantest)
 *******************************************************************************/
package org.eclipse.swt.widgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;

/**
 * Styles supported: ICON_ERROR, ICON_INFORMATION, ICON_QUESTION, ICON_WARNING,
 * ICON_WORKING OK, OK | CANCEL YES | NO, YES | NO | CANCEL RETRY | CANCEL ABORT
 * | RETRY | IGNORE
 */
public class MessageBoxExample {

	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setText("MessageBox Test");
		shell.setSize(400, 300);
		shell.setLayout(new GridLayout(1, false));

		Button testButton = new Button(shell, SWT.PUSH);
		testButton.setText("Show Custom MessageBox");
		testButton.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true));
		testButton.addListener(SWT.Selection, e -> {
			// Try different styles here:
			int style = SWT.ICON_INFORMATION | SWT.RETRY | SWT.ABORT | SWT.IGNORE;

			MessageBox box = new MessageBox(shell, style);
			box.setText("Confirmation Required");
			box.setMessage("Do you want to continue with this operation?");
			int result = box.open();

			String resultStr = switch (result) {
			case SWT.YES -> "YES";
			case SWT.NO -> "NO";
			case SWT.CANCEL -> "CANCEL";
			case SWT.OK -> "OK";
			case SWT.ABORT -> "ABORT";
			case SWT.RETRY -> "RETRY";
			case SWT.IGNORE -> "IGNORE";
			default -> "UNKNOWN";
			};
			System.out.println("User selected: " + resultStr);
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
