/*******************************************************************************
 * Copyright (c) 2026 Eclipse Platform Contributors and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Eclipse Platform Contributors - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.snippets;

import java.nio.file.Path;

import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

/**
 * Snippet to draw an SVG image using three Image constructors
 * <p>
 * Useful for comparing rendering on different operating systems.
 * <p>
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 *
 * @see org.eclipse.swt.graphics.Image#Image(org.eclipse.swt.graphics.Device,
 *      java.io.InputStream)
 * @see org.eclipse.swt.graphics.Image#Image(org.eclipse.swt.graphics.Device,
 *      String)
 * @see org.eclipse.swt.graphics.Image#Image(org.eclipse.swt.graphics.Device,
 *      org.eclipse.swt.graphics.ImageFileNameProvider)
 */
public class Snippet390 {

	public static void main(String[] args) throws Exception {
		final Display display = new Display();

		final Shell shell = new Shell(display);
		shell.setText("Snippet 390");
		shell.setLayout(new FillLayout());
		shell.setBounds(250, 50, 1000, 400);
		shell.setBackground(new Color(255, 255, 255));

		String imgPath = "eclipse.svg";
		Path fullPath = Path.of(Snippet390.class.getResource(imgPath).toURI());

		// Image loaded in stream
		Image image1 = new Image(display, Snippet390.class.getResourceAsStream(imgPath));

		// Image loaded in file path
		Image image2 = new Image(display, fullPath.toString());

		// Image loaded in ImageFileNameProvider
		Image image3 = new Image(display, (ImageFileNameProvider) zoom -> {
			return zoom == 100 ? fullPath.toString() : null;
		});

		shell.addPaintListener(e -> {
			e.gc.drawImage(image1, 20, 10, 300, 300);
			e.gc.drawText("Image(Display, InputStream)", 80, 300);

			e.gc.drawImage(image2, 340, 10, 300, 300);
			e.gc.drawText("Image(Display, String)", 420, 300);

			e.gc.drawImage(image3, 660, 10, 300, 300);
			e.gc.drawText("Image(Display, ImageFileNameProvider)", 700, 300);
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
