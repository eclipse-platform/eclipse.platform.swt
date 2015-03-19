/*******************************************************************************
 * Copyright (c) 2015 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.snippets;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

/**
 * Snippet to demonstrate the use of the DPI-aware Image constructors.
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */
public class Snippet367 {
	private static final String IMAGE_100 = "eclipse16.png";
	private static final String IMAGE_150 = "eclipse24.png";
	private static final String IMAGE_200 = "eclipse32.png";
	private static final String IMAGES_ROOT = "bin/org/eclipse/swt/snippets/";

	private static final String IMAGE_PATH_100 = IMAGES_ROOT + IMAGE_100;
	private static final String IMAGE_PATH_150 = IMAGES_ROOT + IMAGE_150;
	private static final String IMAGE_PATH_200 = IMAGES_ROOT + IMAGE_200;

	public static void main (String [] args) {
		final ImageFileNameProvider filenameProvider = new ImageFileNameProvider () {
			@Override
			public String getImagePath (int zoom) {
				switch (zoom) {
				case 150:
					return IMAGE_PATH_150;
				case 200:
					return IMAGE_PATH_200;
				default:
					return IMAGE_PATH_100;
				}
			}
		};
		final ImageDataProvider imageDataProvider = new ImageDataProvider () {
			@Override
			public ImageData getImageData (int zoom) {
				switch (zoom) {
				case 150:
					return new ImageData (IMAGE_PATH_150);
				case 200:
					return new ImageData (IMAGE_PATH_200);
				default:
					return new ImageData (IMAGE_PATH_100);
				}
			}
		};

		final Display display = new Display ();
		final Shell shell = new Shell (display);
		shell.setLayout (new GridLayout (2, false));

		new Label (shell, SWT.NONE).setText (IMAGE_200 + ":");
		new Label (shell, SWT.NONE).setImage (new Image(display, IMAGE_PATH_200));

		new Label (shell, SWT.NONE).setText (IMAGE_150 + ":");
		new Label (shell, SWT.NONE).setImage (new Image(display, IMAGE_PATH_150));

		new Label (shell, SWT.NONE).setText (IMAGE_100 + ":");
		new Label (shell, SWT.NONE).setImage (new Image(display, IMAGE_PATH_100));

		new Label (shell, SWT.SEPARATOR | SWT.HORIZONTAL).setLayoutData (new GridData (SWT.FILL, SWT.FILL, true, false, 2, 1));

		new Label (shell, SWT.NONE).setText ("ImageFileNameProvider:");
		new Label (shell, SWT.NONE).setImage (new Image (display, filenameProvider));

		new Label (shell, SWT.NONE).setText ("ImageDataProvider:");
		new Label (shell, SWT.NONE).setImage (new Image (display, imageDataProvider));

		shell.pack ();
		shell.open ();
		while (!shell.isDisposed ()) {
			if (!display.readAndDispatch ()) display.sleep ();
		}
		display.dispose ();
	}
}
