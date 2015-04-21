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
import org.eclipse.swt.events.*;
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
		new Label (shell, SWT.NONE).setImage (new Image (display, IMAGE_PATH_200));

		new Label (shell, SWT.NONE).setText (IMAGE_150 + ":");
		new Label (shell, SWT.NONE).setImage (new Image (display, IMAGE_PATH_150));

		new Label (shell, SWT.NONE).setText (IMAGE_100 + ":");
		new Label (shell, SWT.NONE).setImage (new Image (display, IMAGE_PATH_100));

		new Label (shell, SWT.SEPARATOR | SWT.HORIZONTAL).setLayoutData (new GridData (SWT.FILL, SWT.FILL, true, false, 2, 1));

		new Label (shell, SWT.NONE).setText ("ImageFileNameProvider:");
		new Label (shell, SWT.NONE).setImage (new Image (display, filenameProvider));

		new Label (shell, SWT.NONE).setText ("ImageDataProvider:");
		new Label (shell, SWT.NONE).setImage (new Image (display, imageDataProvider));

		new Label (shell, SWT.SEPARATOR | SWT.HORIZONTAL).setLayoutData (new GridData (SWT.FILL, SWT.FILL, true, false, 2, 1));

		new Label (shell, SWT.NONE).setText ("Canvas\n(PaintListener)");
		final Point size = new Point (550, 35);
		final Canvas canvas = new Canvas (shell, SWT.NONE);
		canvas.addPaintListener (new PaintListener () {
			@Override
			public void paintControl (PaintEvent e) {
				Point size = canvas.getSize ();
				paintImage (e.gc, size);
			}
		});
		canvas.setLayoutData (new GridData (size.x, size.y));

		new Label (shell, SWT.SEPARATOR | SWT.HORIZONTAL).setLayoutData (new GridData (SWT.FILL, SWT.FILL, true, false, 2, 1));

		new Label (shell, SWT.NONE).setText ("Painted image\n (default resolution)");
		Image image = new Image (display, size.x, size.y);
		GC gc = new GC (image);
		try {
			paintImage (gc, size);
		} finally {
			gc.dispose ();
		}
		new Label (shell, SWT.NONE).setImage (image);

		new Label (shell, SWT.SEPARATOR | SWT.HORIZONTAL).setLayoutData (new GridData (SWT.FILL, SWT.FILL, true, false, 2, 1));

		new Label (shell, SWT.NONE).setText ("Painted image\n(multi-res, unzoomed paint)");
		new Label (shell, SWT.NONE).setImage (new Image (display, new ImageDataProvider () {
			@Override
			public ImageData getImageData (int zoom) {
				Image temp = new Image (display, size.x * zoom / 100, size.y * zoom / 100);
				GC gc = new GC (temp);
				try {
					paintImage (gc, size);
					return temp.getImageData ();
				} finally {
					gc.dispose ();
					temp.dispose ();
				}
			}
		}));

		new Label (shell, SWT.SEPARATOR | SWT.HORIZONTAL).setLayoutData (new GridData (SWT.FILL, SWT.FILL, true, false, 2, 1));

		new Label (shell, SWT.NONE).setText ("Painted image\n(multi-res, zoomed paint)");
		new Label (shell, SWT.NONE).setImage (new Image (display, new ImageDataProvider () {
			@Override
			public ImageData getImageData (int zoom) {
				Image temp = new Image (display, size.x * zoom / 100, size.y * zoom / 100);
				GC gc = new GC (temp);
				try {
					paintImage2 (gc, new Point (size.x * zoom / 100, size.y * zoom / 100), zoom / 100);
					return temp.getImageData ();
				} finally {
					gc.dispose ();
					temp.dispose ();
				}
			}
		}));

		shell.pack ();
		shell.open ();
		while (!shell.isDisposed ()) {
			if (!display.readAndDispatch ()) display.sleep ();
		}
		display.dispose ();
	}

	private static void paintImage (GC gc, Point size) {
		gc.setBackground (gc.getDevice ().getSystemColor (SWT.COLOR_WIDGET_BACKGROUND));
		gc.fillRectangle (0, 0, size.x, size.y);

		gc.setBackground (gc.getDevice ().getSystemColor (SWT.COLOR_LIST_SELECTION));
		gc.fillRoundRectangle (0, 0, size.x - 1, size.y - 1, 10, 10);

		gc.setBackground (gc.getDevice ().getSystemColor (SWT.COLOR_WIDGET_BACKGROUND));
		gc.drawRoundRectangle (0, 0, size.x - 1, size.y - 1, 10, 10);
		gc.drawText (gc.getFont ().getFontData ()[0].toString (), 10, 10, true);
	}

	private static void paintImage2 (GC gc, Point size, int f) {
		gc.setBackground (gc.getDevice ().getSystemColor (SWT.COLOR_WIDGET_BACKGROUND));
		gc.fillRectangle (0, 0, size.x, size.y);

		// Scale line width, corner roundness, and font size.
		// Caveat: line width expands in all directions, so the origin also has to move.

		gc.setBackground (gc.getDevice ().getSystemColor (SWT.COLOR_LIST_SELECTION));
		gc.fillRoundRectangle (f/2, f/2, size.x - f, size.y - f, 10*f, 10*f);

		gc.setBackground (gc.getDevice ().getSystemColor (SWT.COLOR_WIDGET_BACKGROUND));
		gc.setLineWidth (f);
		gc.drawRoundRectangle (f/2, f/2, size.x - f, size.y - f, 10*f, 10*f);
		FontData fontData = gc.getFont ().getFontData ()[0];
		fontData.setHeight (fontData.getHeight ()*f);
		Font font = new Font (gc.getDevice (), fontData);
		try {
			gc.setFont (font);
			gc.drawText (fontData.toString (), 10*f, 10*f, true);
		} finally {
			font.dispose ();
		}
	}
}
