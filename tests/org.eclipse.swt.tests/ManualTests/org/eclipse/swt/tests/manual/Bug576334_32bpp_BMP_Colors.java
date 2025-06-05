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

import java.io.InputStream;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

public class Bug576334_32bpp_BMP_Colors {
	public static void main (String[] args) {
		final Display display = new Display ();
		final Shell shell = new Shell (display);
		shell.setLayout (new GridLayout ());

		Label hint = new Label (shell, 0);
		hint.setText ("The image below shall be orange; not green");

		InputStream imageStream = Bug576334_32bpp_BMP_Colors.class.getResourceAsStream ("/Bug576334_32bpp_BMP_Colors.bmp");
		ImageData imageData = ImageData.load(imageStream);
		Image image = new Image (display, imageData);

		Label imageLabel = new Label (shell, 0);
		imageLabel.setImage (image);

		shell.pack ();
		shell.open ();
		while (!shell.isDisposed ()) {
			if (!display.readAndDispatch ())
				display.sleep ();
		}
		display.dispose ();
	}
}