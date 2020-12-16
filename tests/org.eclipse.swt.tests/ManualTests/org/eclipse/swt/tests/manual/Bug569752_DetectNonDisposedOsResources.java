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
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Resource;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;

public class Bug569752_DetectNonDisposedOsResources {
	public static void main(String[] args) {
		// System.setProperty("org.eclipse.swt.graphics.Resource.reportNonDisposed", "stacks");
		// System.setProperty("org.eclipse.swt.graphics.Resource.reportNonDisposed", "false");

		final Display display = new Display();
		final Shell shell = new Shell(display);
		shell.setLayout(new GridLayout(1, true));

		Label hint = new Label(shell, 0);
		hint.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		hint.setText(
			"1) Click the button below\n" +
			"2) With patch, there's a leak report for Image"
		);

		Button chkDispose = new Button(shell, SWT.CHECK);
		chkDispose.setText("Dispose objects");

		Button btnTest = new Button(shell, SWT.PUSH);
		btnTest.setText("Leak Image");
		btnTest.addListener(SWT.Selection, event -> {
			Image image = new Image(display, 10, 10);

			if (chkDispose.getSelection())
				image.dispose();

			image = null;
			System.gc();
		});

		Resource.trackNonDisposed(true, (object, exception) -> {
			System.err.println("Snippet reporter: Resource was not properly disposed: " + object);
			if (exception != null)
				exception.printStackTrace();
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