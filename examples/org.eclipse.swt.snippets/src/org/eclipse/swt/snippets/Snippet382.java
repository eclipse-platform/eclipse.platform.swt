/*******************************************************************************
 * Copyright (c) 2024 Yatta Solutions
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Yatta Solutions - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.snippets;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

/**
 * Snippet to test the use of the DPI-aware Image constructors with disabled and gray images
 * Work in progress in https://bugs.eclipse.org/399786
 * <p>
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 * </p>
 */
public class Snippet382 {
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
		shell.setText("Snippet382");
		shell.setLayout (new GridLayout (3, false));
		Listener l = new Listener() {
			@Override
			public void handleEvent(Event e)  {
				if (e.type == SWT.Paint) {
					GC mainGC = e.gc;
					GCData gcData = mainGC.getGCData();
					final Image imageWithFileNameProvider = new Image (display, filenameProvider);
					final Image disabledImageWithFileNameProvider = new Image (display,imageWithFileNameProvider, SWT.IMAGE_DISABLE);
					final Image greyImageWithFileNameProvider = new Image (display,imageWithFileNameProvider, SWT.IMAGE_GRAY);

					final Image imageWithDataProvider = new Image (display, imageDataProvider);
					final Image disabledImageWithDataProvider = new Image (display,imageWithDataProvider, SWT.IMAGE_DISABLE);
					final Image greyImageWithDataProvider = new Image (display,imageWithDataProvider, SWT.IMAGE_GRAY);

					final Image imageWithData = new Image (display, IMAGE_PATH_100);
					final Image disabledImageWithData = new Image (display,imageWithData, SWT.IMAGE_DISABLE);
					final Image greyImageWithData = new Image (display,imageWithData, SWT.IMAGE_GRAY);

					try {
						drawImages(mainGC, gcData, "Normal",40, imageWithFileNameProvider);
						drawImages(mainGC, gcData, "Disabled",80, disabledImageWithFileNameProvider);
						drawImages(mainGC, gcData, "Greyed",120, greyImageWithFileNameProvider);

						drawImages(mainGC, gcData, "Normal",160, imageWithDataProvider);
						drawImages(mainGC, gcData, "Disabled",200, disabledImageWithDataProvider);
						drawImages(mainGC, gcData, "Greyed",240, greyImageWithDataProvider);

						drawImages(mainGC, gcData, "Normal",280, imageWithDataProvider);
						drawImages(mainGC, gcData, "Disabled",320, disabledImageWithData);
						drawImages(mainGC, gcData, "Greyed",360, greyImageWithData);
					} finally {
						mainGC.dispose ();
					}
				}
			}

			private void drawImages(GC mainGC, GCData gcData, String text, int y, final Image imageWithFileNameProvider) {
				gcData.nativeZoom = 100;
				mainGC.drawText(text, 0, y);
				mainGC.drawImage(imageWithFileNameProvider, 50, y);
				gcData.nativeZoom = 150;
				mainGC.drawImage(imageWithFileNameProvider, 100, (int) (y/1.5));
				gcData.nativeZoom = 200;
				mainGC.drawImage(imageWithFileNameProvider, 150, y/2);
			}
		};
		shell.addListener(SWT.Paint, l);

		shell.setSize(400, 500);
		shell.open ();
		while (!shell.isDisposed ()) {
			if (!display.readAndDispatch ()) display.sleep ();
		}
		display.dispose ();
	}

}
