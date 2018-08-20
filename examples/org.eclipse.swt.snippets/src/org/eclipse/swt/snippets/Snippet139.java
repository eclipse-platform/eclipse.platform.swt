/*******************************************************************************
 * Copyright (c) 2000, 2016 IBM Corporation and others.
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

/*
 * This snippet was adapted from org.eclipse.draw2d.ImageUtilities in
 * http://dev.eclipse.org/viewcvs/index.cgi/org.eclipse.draw2d/?cvsroot=Tools_Project
 * by Pratik Shah.
 *
 * example snippet: rotate and flip an image
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

public class Snippet139 {

static ImageData rotate(ImageData srcData, int direction) {
	int bytesPerPixel = srcData.bytesPerLine / srcData.width;
	int destBytesPerLine = (direction == SWT.DOWN)? srcData.width * bytesPerPixel : srcData.height * bytesPerPixel;
	byte[] newData = new byte[(direction == SWT.DOWN)? srcData.height * destBytesPerLine : srcData.width * destBytesPerLine];
	int width = 0, height = 0;
	for (int srcY = 0; srcY < srcData.height; srcY++) {
		for (int srcX = 0; srcX < srcData.width; srcX++) {
			int destX = 0, destY = 0, destIndex = 0, srcIndex = 0;
			switch (direction){
				case SWT.LEFT: // left 90 degrees
					destX = srcY;
					destY = srcData.width - srcX - 1;
					width = srcData.height;
					height = srcData.width;
					break;
				case SWT.RIGHT: // right 90 degrees
					destX = srcData.height - srcY - 1;
					destY = srcX;
					width = srcData.height;
					height = srcData.width;
					break;
				case SWT.DOWN: // 180 degrees
					destX = srcData.width - srcX - 1;
					destY = srcData.height - srcY - 1;
					width = srcData.width;
					height = srcData.height;
					break;
			}
			destIndex = (destY * destBytesPerLine) + (destX * bytesPerPixel);
			srcIndex = (srcY * srcData.bytesPerLine) + (srcX * bytesPerPixel);
			System.arraycopy(srcData.data, srcIndex, newData, destIndex, bytesPerPixel);
		}
	}
	// destBytesPerLine is used as scanlinePad to ensure that no padding is required
	return new ImageData(width, height, srcData.depth, srcData.palette, srcData.scanlinePad, newData);
}
static ImageData flip(ImageData srcData, boolean vertical) {
	int bytesPerPixel = srcData.bytesPerLine / srcData.width;
	int destBytesPerLine = srcData.width * bytesPerPixel;
	byte[] newData = new byte[srcData.data.length];
	for (int srcY = 0; srcY < srcData.height; srcY++) {
		for (int srcX = 0; srcX < srcData.width; srcX++) {
			int destX = 0, destY = 0, destIndex = 0, srcIndex = 0;
			if (vertical){
				destX = srcX;
				destY = srcData.height - srcY - 1;
			} else {
				destX = srcData.width - srcX - 1;
				destY = srcY;
			}
			destIndex = (destY * destBytesPerLine) + (destX * bytesPerPixel);
			srcIndex = (srcY * srcData.bytesPerLine) + (srcX * bytesPerPixel);
			System.arraycopy(srcData.data, srcIndex, newData, destIndex, bytesPerPixel);
		}
	}
	// destBytesPerLine is used as scanlinePad to ensure that no padding is required
	return new ImageData(srcData.width, srcData.height, srcData.depth, srcData.palette, srcData.scanlinePad, newData);
}

public static void main(String[] args) {
	Display display = new Display();

	// create an image with the word "hello" on it
	final Image image0 = new Image(display, 50, 30);
	GC gc = new GC(image0);
	gc.setBackground(display.getSystemColor(SWT.COLOR_RED));
	gc.fillRectangle(image0.getBounds());
	gc.drawString("hello",	5, 5, true);
	gc.dispose();

	ImageData data = image0.getImageData();
	// rotate and flip this image
	final Image image1 = new Image(display, rotate(data, SWT.LEFT));
	final Image image2 = new Image(display, rotate(data, SWT.RIGHT));
	final Image image3 = new Image(display, rotate(data, SWT.DOWN));
	final Image image4 = new Image(display, flip(data, true));
	final Image image5 = new Image(display, flip(data, false));

	Shell shell = new Shell(display);
	// draw the results on the shell
	shell.addPaintListener(e -> {
		e.gc.drawText("Original Image:", 10, 10, true);
		e.gc.drawImage(image0, 10, 40);
		e.gc.drawText("Left, Right, 180:", 10, 80, true);
		e.gc.drawImage(image1, 10, 110);
		e.gc.drawImage(image2, 50, 110);
		e.gc.drawImage(image3, 90, 110);
		e.gc.drawText("Flipped Vertical, Horizontal:", 10, 170, true);
		e.gc.drawImage(image4, 10, 200);
		e.gc.drawImage(image5, 70, 200);
	});
	shell.setSize(300, 300);
	shell.open();
	while (!shell.isDisposed()) {
		if (!display.readAndDispatch())
			display.sleep();
	}
	image0.dispose();
	image1.dispose();
	image2.dispose();
	image3.dispose();
	image4.dispose();
	image5.dispose();
	display.dispose();
}
}
