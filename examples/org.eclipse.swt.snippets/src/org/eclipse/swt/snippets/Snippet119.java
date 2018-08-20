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
 * Cursor example snippet: create a color cursor from a source and a mask
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 *
 * @since 3.0
 */
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

public class Snippet119 {

static byte[] srcData = {
	(byte)0x11, (byte)0x11, (byte)0x11, (byte)0x00, (byte)0x00, (byte)0x11, (byte)0x11, (byte)0x11,
	(byte)0x11, (byte)0x10, (byte)0x00, (byte)0x01, (byte)0x10, (byte)0x00, (byte)0x01, (byte)0x11,
	(byte)0x11, (byte)0x00, (byte)0x22, (byte)0x01, (byte)0x10, (byte)0x33, (byte)0x00, (byte)0x11,
	(byte)0x10, (byte)0x02, (byte)0x22, (byte)0x01, (byte)0x10, (byte)0x33, (byte)0x30, (byte)0x01,
	(byte)0x10, (byte)0x22, (byte)0x22, (byte)0x01, (byte)0x10, (byte)0x33, (byte)0x33, (byte)0x01,
	(byte)0x10, (byte)0x22, (byte)0x22, (byte)0x01, (byte)0x10, (byte)0x33, (byte)0x33, (byte)0x01,
	(byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
	(byte)0x01, (byte)0x11, (byte)0x11, (byte)0x01, (byte)0x10, (byte)0x11, (byte)0x11, (byte)0x10,
	(byte)0x01, (byte)0x11, (byte)0x11, (byte)0x01, (byte)0x10, (byte)0x11, (byte)0x11, (byte)0x10,
	(byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
	(byte)0x10, (byte)0x44, (byte)0x44, (byte)0x01, (byte)0x10, (byte)0x55, (byte)0x55, (byte)0x01,
	(byte)0x10, (byte)0x44, (byte)0x44, (byte)0x01, (byte)0x10, (byte)0x55, (byte)0x55, (byte)0x01,
	(byte)0x10, (byte)0x04, (byte)0x44, (byte)0x01, (byte)0x10, (byte)0x55, (byte)0x50, (byte)0x01,
	(byte)0x11, (byte)0x00, (byte)0x44, (byte)0x01, (byte)0x10, (byte)0x55, (byte)0x00, (byte)0x11,
	(byte)0x11, (byte)0x10, (byte)0x00, (byte)0x01, (byte)0x10, (byte)0x00, (byte)0x01, (byte)0x11,
	(byte)0x11, (byte)0x11, (byte)0x11, (byte)0x00, (byte)0x00, (byte)0x11, (byte)0x11, (byte)0x11,
};

static byte[] mskData = {
	(byte)0x03, (byte)0xc0,
	(byte)0x1f, (byte)0xf8,
	(byte)0x3f, (byte)0xfc,
	(byte)0x7f, (byte)0xfe,
	(byte)0x7f, (byte)0xfe,
	(byte)0x7f, (byte)0xfe,
	(byte)0xff, (byte)0xff,
	(byte)0xfe, (byte)0x7f,
	(byte)0xfe, (byte)0x7f,
	(byte)0xff, (byte)0xff,
	(byte)0x7f, (byte)0xfe,
	(byte)0x7f, (byte)0xfe,
	(byte)0x7f, (byte)0xfe,
	(byte)0x3f, (byte)0xfc,
	(byte)0x1f, (byte)0xf8,
	(byte)0x03, (byte)0xc0
};

public static void main (String [] args) {
	Display display = new Display();
	Color white = display.getSystemColor (SWT.COLOR_WHITE);
	Color black = display.getSystemColor (SWT.COLOR_BLACK);
	Color yellow = display.getSystemColor (SWT.COLOR_YELLOW);
	Color red = display.getSystemColor (SWT.COLOR_RED);
	Color green = display.getSystemColor (SWT.COLOR_GREEN);
	Color blue = display.getSystemColor (SWT.COLOR_BLUE);

	//Create a source ImageData of depth 4
	PaletteData palette = new PaletteData (black.getRGB(), white.getRGB(), yellow.getRGB(),
		red.getRGB(), blue.getRGB(), green.getRGB());
	ImageData sourceData = new ImageData (16, 16, 4, palette, 1, srcData);

	//Create a mask ImageData of depth 1 (monochrome)
	palette = new PaletteData (black.getRGB(), white.getRGB());
	ImageData maskData = new ImageData (16, 16, 1, palette, 1, mskData);

	//Set mask
	sourceData.maskData = maskData.data;
	sourceData.maskPad = maskData.scanlinePad;

	//Create cursor
	Cursor cursor = new Cursor(display, sourceData, 10, 10);

	//Remove mask to draw them separately just to show what they look like
	sourceData.maskData = null;
	sourceData.maskPad = -1;

	Shell shell = new Shell(display);
	final Image source = new Image (display,sourceData);
	final Image mask = new Image (display, maskData);
	shell.addPaintListener(e -> {
		GC gc = e.gc;
		int x = 10, y = 10;
		String stringSource = "source: ";
		String stringMask = "mask: ";
		gc.drawString(stringSource, x, y);
		gc.drawString(stringMask, x, y + 30);
		x += Math.max(gc.stringExtent(stringSource).x, gc.stringExtent(stringMask).x);
		gc.drawImage(source, x, y);
		gc.drawImage(mask, x, y + 30);
	});
	shell.setSize(150, 150);
	shell.open();
	shell.setCursor(cursor);

	while (!shell.isDisposed()) {
		if (!display.readAndDispatch())
			display.sleep();
	}
	cursor.dispose();
	source.dispose();
	mask.dispose();
	display.dispose();
}
}
