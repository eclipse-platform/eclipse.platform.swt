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
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

import java.io.InputStream;

public final class Issue0232_16bppIndexedImageData {
	static Image loadImage(Display display, String filename) throws Exception {
		try (InputStream fileStream = Issue0232_16bppIndexedImageData.class.getResourceAsStream(filename)) {
			Image image = new Image(display, fileStream);
			return image;
		}
	}

	public static void main(String[] args) {
		final Display display = new Display();
		final Shell shell = new Shell(display);
		shell.setLayout (new GridLayout (1, true));

		Label hint = new Label (shell, 0);
		hint.setText (
			"1) Run on macOS\n" +
			"2) Click the button\n" +
			"3) Issue 232: There will be a 'ERROR_UNSUPPORTED_DEPTH' exception\n" +
			"\n" +
			"4) Run on Windows\n" +
			"5) Click the button\n" +
			"6) Issue 232: Colors are all wrong"
		);

		Composite composite = new Composite(shell, 0);
		composite.setLayout (new GridLayout (2, true));

		new Label(composite, 0).setText("8bpp");
		new Label(composite, 0).setText("16bpp");

		Label imageLabel08 = new Label(composite, 0);
		Label imageLabel16 = new Label(composite, 0);

		Button button = new Button(shell, 0);
		button.setText ("Test");
		button.addListener (SWT.Selection, e -> {
			try {
				imageLabel08.setImage(loadImage(display, "Issue0232_08bppTiff.tif"));
				imageLabel16.setImage(loadImage(display, "Issue0232_16bppTiff.tif"));
				shell.pack();
			} catch (Exception ex) {
				ex.printStackTrace();
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
