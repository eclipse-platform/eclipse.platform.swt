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

package org.eclipse.swt.tests.manual;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

import java.io.InputStream;

public final class Bug577879_JvmCrash_16bppGrayscalePng {
	public static void main(String[] args) {
		final Display display = new Display();
		final Shell shell = new Shell(display);
		shell.setLayout (new GridLayout (1, true));

		Label hint = new Label (shell, 0);
		hint.setText (
			"1) Run on Windows\n" +
			"2) Click the button\n" +
			"3) Bug 577879: JVM will crash"
		);

		Label imageLabel = new Label(shell, 0);

		Button button = new Button(shell, 0);
		button.setText ("Test");
		button.addListener (SWT.Selection, e -> {
			InputStream fileStream = Bug577879_JvmCrash_16bppGrayscalePng.class.getResourceAsStream("Bug577879_JvmCrash_16bppGrayscalePng.png");
			Image image = new Image (display, fileStream);
			imageLabel.setImage (image);
			shell.pack();
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
