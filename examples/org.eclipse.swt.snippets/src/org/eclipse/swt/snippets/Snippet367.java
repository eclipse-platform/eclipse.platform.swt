/*******************************************************************************
 * Copyright (c) 2015, 2018 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
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
 * Snippet to test the use of the DPI-aware Image constructors.
 * Work in progress in https://bugs.eclipse.org/399786
 * <p>
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
		final ImageFileNameProvider filenameProvider = zoom -> {
			switch (zoom) {
			case 100:
				return IMAGE_PATH_100;
			case 150:
				return IMAGE_PATH_150;
			case 200:
				return IMAGE_PATH_200;
			default:
				return null;
			}
		};
		final ImageDataProvider imageDataProvider = zoom -> {
			switch (zoom) {
			case 100:
				return new ImageData (IMAGE_PATH_100);
			case 150:
				return new ImageData (IMAGE_PATH_150);
			case 200:
				return new ImageData (IMAGE_PATH_200);
			default:
				return null;
			}
		};

		final Display display = new Display ();
		final Shell shell = new Shell (display);
		shell.setText("Snippet367");
		shell.setLayout (new GridLayout (3, false));

		Menu menuBar = new Menu(shell, SWT.BAR);
		shell.setMenuBar(menuBar);
		MenuItem fileItem = new MenuItem(menuBar, SWT.CASCADE);
		fileItem.setText("&File");
		Menu fileMenu = new Menu(menuBar);
		fileItem.setMenu(fileMenu);
		MenuItem exitItem = new MenuItem(fileMenu, SWT.PUSH);
		exitItem.setText("&Exit");
		exitItem.addListener(SWT.Selection, e -> shell.close());

		new Label (shell, SWT.NONE).setText (IMAGE_200 + ":");
		new Label (shell, SWT.NONE).setImage (new Image (display, IMAGE_PATH_200));
		new Button(shell, SWT.PUSH).setImage (new Image (display, IMAGE_PATH_200));

		new Label (shell, SWT.NONE).setText (IMAGE_150 + ":");
		new Label (shell, SWT.NONE).setImage (new Image (display, IMAGE_PATH_150));
		new Button(shell, SWT.NONE).setImage (new Image (display, IMAGE_PATH_150));

		new Label (shell, SWT.NONE).setText (IMAGE_100 + ":");
		new Label (shell, SWT.NONE).setImage (new Image (display, IMAGE_PATH_100));
		new Button(shell, SWT.NONE).setImage (new Image (display, IMAGE_PATH_100));

		createSeparator(shell);

		new Label (shell, SWT.NONE).setText ("ImageFileNameProvider:");
		new Label (shell, SWT.NONE).setImage (new Image (display, filenameProvider));
		new Button(shell, SWT.NONE).setImage (new Image (display, filenameProvider));

		new Label (shell, SWT.NONE).setText ("ImageDataProvider:");
		new Label (shell, SWT.NONE).setImage (new Image (display, imageDataProvider));
		new Button(shell, SWT.NONE).setImage (new Image (display, imageDataProvider));

		createSeparator(shell);

		new Label (shell, SWT.NONE).setText ("1. Canvas\n(PaintListener)");
		final Point size = new Point (550, 40);
		final Canvas canvas = new Canvas (shell, SWT.NONE);
		canvas.addPaintListener (e -> {
			Point size1 = canvas.getSize ();
			paintImage (e.gc, size1);
		});
		GridData gridData = new GridData (size.x, size.y);
		gridData.horizontalSpan = 2;
		canvas.setLayoutData (gridData);

		createSeparator(shell);

		new Label (shell, SWT.NONE).setText ("2. Painted image\n (default resolution)");
		Image image = new Image (display, size.x, size.y);
		GC gc = new GC (image);
		try {
			paintImage (gc, size);
		} finally {
			gc.dispose ();
		}
		Label imageLabel = new Label (shell, SWT.NONE);
		imageLabel.setImage (image);
		imageLabel.setLayoutData (new GridData (SWT.BEGINNING, SWT.BEGINNING, false, false, 2, 1));

		createSeparator(shell);

		new Label (shell, SWT.NONE).setText ("3. Painted image\n(multi-res, unzoomed paint)");
		imageLabel = new Label (shell, SWT.NONE);
		imageLabel.setImage (new Image (display, (ImageDataProvider) zoom -> {
			Image temp = new Image (display, size.x * zoom / 100, size.y * zoom / 100);
			GC gc1 = new GC (temp);
			try {
				paintImage (gc1, size);
				return temp.getImageData ();
			} finally {
				gc1.dispose ();
				temp.dispose ();
			}
		}));
		imageLabel.setLayoutData (new GridData (SWT.BEGINNING, SWT.BEGINNING, false, false, 2, 1));

		createSeparator(shell);

		new Label (shell, SWT.NONE).setText ("4. Painted image\n(multi-res, zoomed paint)");
		imageLabel = new Label (shell, SWT.NONE);
		imageLabel.setImage (new Image (display, (ImageDataProvider) zoom -> {
			Image temp = new Image (display, size.x * zoom / 100, size.y * zoom / 100);
			GC gc1 = new GC (temp);
			try {
				paintImage2 (gc1, new Point (size.x * zoom / 100, size.y * zoom / 100), zoom / 100);
				return temp.getImageData ();
			} finally {
				gc1.dispose ();
				temp.dispose ();
			}
		}));
		imageLabel.setLayoutData (new GridData (SWT.BEGINNING, SWT.BEGINNING, false, false, 2, 1));

		createSeparator(shell);

		new Label (shell, SWT.NONE).setText ("5. 50x50 box\n(Display#getDPI(): " + display.getDPI().x + ")");
		Label box= new Label (shell, SWT.NONE);
		box.setBackground(display.getSystemColor(SWT.COLOR_WIDGET_DARK_SHADOW));
		box.setLayoutData (new GridData (50, 50));

		shell.pack ();
		shell.open ();
		while (!shell.isDisposed ()) {
			if (!display.readAndDispatch ()) display.sleep ();
		}
		display.dispose ();
	}

	private static void createSeparator(Composite shell) {
		new Label (shell, SWT.SEPARATOR | SWT.HORIZONTAL).setLayoutData (new GridData (SWT.FILL, SWT.FILL, true, false, 3, 1));
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
