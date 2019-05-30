/*******************************************************************************
 * Copyright (c) 2019 Simeon Andreev and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Simeon Andreev - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tests.gtk.snippets;

import java.io.File;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

/*
 * Title: Bug 545032 - [GTK] Implement native ImageLoader
 * How to run: run snippet and two images side-by-side.
 * Bug description: any malfunction will be on the right side of the shell.
 * Expected results: two RGBA patterns side-by-side, looking identical.
 * Note that the last box may be black, due to the different alpha values.
 * GTK Version(s): all GTK versions.
 */
public class Bug545032_ImageLoaderTesting {

	public static void main(String[] args) throws Exception {
		File file = File.createTempFile("swt", "example");

		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setLayout(new FillLayout());
		shell.setSize(500, 500);
		shell.setText("Bug 545032: Native ImageLoader Testing");

		shell.open();

		int imageWidth = 200;
		int imageHeight = 200;
		int bitDepth = 32;
		ImageData imageData = new ImageData(imageWidth, imageHeight, bitDepth, new PaletteData(0xFF0000, 0x00FF00, 0x0000FF));
		int w = imageWidth / 2;
		int h = imageHeight / 2;
		for (int y = 0; y < imageHeight; ++y) {
			for (int x = 0; x < imageWidth; ++x) {
				// Setting this to 128 will make all colors half-transparent
				int alpha = 255;
				int color = 0x000000;
				if (x < w && y < h) {
					color = 0xFF0000;
				} else if (x < w && y >= h) {
					color = 0x00FF00;
				} else if (x >= w && y < h) {
					color = 0x0000FF;
				} else {
					alpha = 128;
				}
				imageData.setPixel(x, y, color);
				// Comment this line to test without transparency in the last box
				imageData.setAlpha(x, y, alpha);
			}
		}

		Image image = new Image(display, imageData);
		new Label(shell, SWT.BORDER).setImage(image);

		ImageLoader saver = new ImageLoader();
		saver.data = new ImageData[] { image.getImageData() };
		file.delete();
		saver.save(file.getAbsolutePath(), SWT.IMAGE_PNG);

		ImageLoader loader = new ImageLoader();
		ImageData[] loaded = loader.load(file.getAbsolutePath());
		for (ImageData imageLoadData : loaded) {
			Label l = new Label(shell, SWT.BORDER);
			image = new Image(display, imageLoadData);
			l.setImage(image);
		}
		shell.pack();
		file.delete();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();
	}
}
