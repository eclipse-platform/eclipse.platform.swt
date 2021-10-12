/*******************************************************************************
 * Copyright (c) 2020 Paul Pazderski and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Paul Pazderski - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tests.win32.snippets;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.function.Function;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class Bug566545_GrayScaleImage {

	/**
	 * Example to demonstrate bug 566545 on windows. The Snippet shows six times
	 * the same image. Image is a smooth(!) diagonal gradient from black to white.
	 * If everything works as expected all six images look equal. With bug the
	 * top right and bottom right image have clear steps instead of a smooth gradient.
	 *
	 * The four images are:
	 *   top left: 8-bit grayscale using indexed palette
	 *   top right: 8-bit grayscale using direct color values
	 *   middle left: same as top left but after ImageData was saved and loaded
	 *   middle right: same as top right but after ImageData was saved and loaded
	 *   bottom left: same as top left but after ImageData returned from Image was saved and loaded
	 *   bottom right: same as top right but after ImageData returned from Image was saved and loaded
	 */
	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setText("Bug 566545");

		RGB[] grayscale = new RGB[256];
		for (int i = 0; i < grayscale.length; i++)
			grayscale[i] = new RGB(i, i, i);
		int margin = 10;
		int width = 128;
		int height = 128;
		ImageData imageDataIndexed = new ImageData(width, height, 8, new PaletteData(grayscale));
		ImageData imageDataDirect = new ImageData(width, height, 8, new PaletteData(0xFF, 0xFF, 0xFF));

		fillImage(imageDataIndexed);
		fillImage(imageDataDirect);

		Image imageIndexed = new Image(display, imageDataIndexed);
		Image imageDirect = new Image(display, imageDataDirect);
		shell.addListener(SWT.Paint, e -> {
			GC gc = e.gc;
			gc.drawImage(imageIndexed, margin, margin);
			gc.drawImage(imageDirect, margin + width + margin, margin);

			Function<ImageData, Image> simulateSave = imageData -> {
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				ImageLoader im = new ImageLoader();
				im.data = new ImageData[] { imageData };
				im.save(out, SWT.IMAGE_PNG);
				ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
				ImageData[] data = im.load(in);
				Image img = new Image(display, data[0]);
				return img;
			};
			gc.drawImage(simulateSave.apply(imageDataIndexed), margin, margin + height + margin);
			gc.drawImage(simulateSave.apply(imageDataDirect), margin + width + margin, margin + height + margin);
			gc.drawImage(simulateSave.apply(imageIndexed.getImageData()), margin, margin * 2 + height * 2 + margin);
			gc.drawImage(simulateSave.apply(imageDirect.getImageData()), margin + width + margin, margin * 2 + height * 2 + margin);
		});
		Rectangle size = shell.computeTrim(0, 0, width * 2 + margin * 3, height * 3 + margin * 4);
		shell.setSize(size.width, size.height);
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}

	private static void fillImage(ImageData imageData) {
		for (int x = 0; x < imageData.width; x++)
			for (int y = 0; y < imageData.height; y++)
				imageData.setPixel(x, y, (x + y) % 256);
	}
}