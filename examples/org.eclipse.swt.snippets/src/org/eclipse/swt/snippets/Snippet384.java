/*******************************************************************************
 * Copyright (c) 2025 Yatta Solutions
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
import org.eclipse.swt.widgets.*;

/**
 * Snippet to show how to draw on a image loaded from the file system which has
 * multiple variant for different zoom levels. Mainly motivated by custom
 * drawing behavior in the win32 implementation
 * <p>
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 * </p>
 */
public class Snippet384 {

	public static void main(String[] args) {
		System.setProperty("swt.autoScale.updateOnRuntime", "true");
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setText("Watermark over Image using GC");
		shell.setSize(1200, 900);

		final ImageFileNameProvider filenameProvider = zoom -> {
			String path = null;
			switch (zoom) {
			case 150:
				path = "bin/org/eclipse/swt/snippets/red.jpeg";
				break;
			case 100:
				path = "bin/org/eclipse/swt/snippets/black.jpg";
				break;
			default:
				path = "bin/org/eclipse/swt/snippets/black.jpg";
			}
			return path;
		};
		Image loadedImage = new Image(display, filenameProvider);

		int width = loadedImage.getBounds().width;
		int height = loadedImage.getBounds().height;

		Image composedImage = new Image(display, (gc, iX, iY) -> {
			gc.drawImage(loadedImage, 0, 0);

			gc.setForeground(display.getSystemColor(SWT.COLOR_WHITE));
			Font font = new Font(display, "Arial", 24, SWT.BOLD);
			gc.setFont(font);
			gc.setAlpha(128); // semi-transparent
			String watermark = "WATERMARK";
			Point extent = gc.textExtent(watermark);
			int x = (width - extent.x) / 2;
			int y = (height - extent.y) / 2;
			gc.drawText(watermark, x, y, SWT.DRAW_TRANSPARENT);
			font.dispose();
		}, width, height);

		shell.addPaintListener(e -> {
			e.gc.drawImage(composedImage, 0, 0);
		});

		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}

		loadedImage.dispose();
		composedImage.dispose();
		display.dispose();
	}
}
