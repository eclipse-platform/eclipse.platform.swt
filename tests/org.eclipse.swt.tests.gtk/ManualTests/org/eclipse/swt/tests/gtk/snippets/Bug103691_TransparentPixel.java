/*******************************************************************************
 * Copyright (c) 2018 Red Hat and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Red Hat - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tests.gtk.snippets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

public class Bug103691_TransparentPixel {

	private static byte LO = 0;
	private static byte MID = 1;
	private static byte HI = 2;

	// Change to path pointing to image.
	private static String IMG_PATH = "/home/xiyan/Downloads/smallfish.gif";

	private static float markOrigColor(byte orig[]) {
		int foo[] = new int[3];
		for (int i = 0; i < 3; i++)
			foo[i] = orig[i] < 0 ? orig[i] + 256 : orig[i];

		int hi, lo, mid;
		hi = lo = mid = foo[0];
		for (int i = 1; i < 3; i++)
			if (foo[i] > hi) {
				mid = hi;
				hi = foo[i];
			} else if (foo[i] < lo) {
				mid = lo;
				lo = foo[i];
			} else
				mid = foo[i];

		for (int i = 0; i < 3; i++)
			if (foo[i] == hi)
				orig[i] = HI;
			else if (foo[i] == lo)
				orig[i] = LO;
			else
				orig[i] = MID;

		return (mid - lo) / (float) (hi - lo);
	}

	private static void colorize(ImageData data, byte orig[], float midratio) {
		for (int y = 0; y < data.height; y++)
			for (int x = 0; x < data.width; x++) {
				int pixel = data.getPixel(x, y);
				if (pixel == data.transparentPixel) {
					data.setPixel(x, y, data.palette.getPixel(new RGB(0, 0, 255)));
					continue;
				}

				RGB pixelValue = data.palette.getRGB(data.getPixel(x, y));
				int foo[] = new int[] { pixelValue.red, pixelValue.green, pixelValue.blue };
				int hi, lo, mid;
				hi = lo = mid = foo[0];
				for (int i = 1; i < 3; i++)
					if (foo[i] > hi) {
						mid = hi;
						hi = foo[i];
					} else if (foo[i] < lo) {
						mid = lo;
						lo = foo[i];
					} else
						mid = foo[i];

				float mul = ((hi + mid + lo) / 765.0f * 255 - hi) / hi;
				hi += mul * hi;
				lo += mul * lo;

				int sat = 100 * (255 - lo) / (hi + 1);
				float dif = (255 - (hi - lo)) / (float) 255;
				hi *= 1 + dif;
				if (hi > 255)
					hi = 255;
				lo -= (sat + 0 * dif);
				if (lo < 0)
					lo = 0;

				mid = (int) (midratio * (hi - lo) + lo);

				for (int i = 0; i < 3; i++)
					if (orig[i] == HI)
						foo[i] = hi;
					else if (orig[i] == LO)
						foo[i] = lo;
					else
						foo[i] = mid;
				data.setPixel(x, y, data.palette.getPixel(new RGB(foo[0], foo[1], foo[2])));
			}
	}

	public static void printArray(byte bites[]) {
		System.out.print("[");
		for (int i = 0; i < 2; i++)
			System.out.print((bites[i] < 0 ? bites[i] + 256 : bites[i]) + ", ");
		System.out.println((bites[2] < 0 ? bites[2] + 256 : bites[2]) + "]");
	}

	public static void main(String[] args) {
		byte orig[] = { (byte) 255, (byte) 64, (byte) 0 };
		float midratio = markOrigColor(orig);

		Display display = new Display();
		final Shell shell = new Shell(display);

		final Label label = new Label(shell, SWT.BORDER);
		Image image = new Image(display, IMG_PATH);
		ImageData data = image.getImageData();

		colorize(data, orig, midratio);

		image = new Image(display, data);

		label.setBounds(image.getBounds());
		shell.pack();

		label.setImage(image);

		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}

}
