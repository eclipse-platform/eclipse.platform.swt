/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.snippets;

/*
 * example snippet: convert between SWT Image and AWT BufferedImage
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.DirectColorModel;
import java.awt.image.IndexColorModel;
import java.awt.image.WritableRaster;

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.graphics.*;

public class Snippet156 {
	
static BufferedImage convertToAWT(ImageData data) {
	ColorModel colorModel = null;
	PaletteData palette = data.palette;
	if (palette.isDirect) {
		colorModel = new DirectColorModel(data.depth, palette.redMask, palette.greenMask, palette.blueMask);
		BufferedImage bufferedImage = new BufferedImage(colorModel, colorModel.createCompatibleWritableRaster(data.width, data.height), false, null);
		for (int y = 0; y < data.height; y++) {
			for (int x = 0; x < data.width; x++) {
				int pixel = data.getPixel(x, y);
				RGB rgb = palette.getRGB(pixel);
				bufferedImage.setRGB(x, y,  rgb.red << 16 | rgb.green << 8 | rgb.blue);
			}
		}
		return bufferedImage;
	} else {
		RGB[] rgbs = palette.getRGBs();
		byte[] red = new byte[rgbs.length];
		byte[] green = new byte[rgbs.length];
		byte[] blue = new byte[rgbs.length];
		for (int i = 0; i < rgbs.length; i++) {
			RGB rgb = rgbs[i];
			red[i] = (byte)rgb.red;
			green[i] = (byte)rgb.green;
			blue[i] = (byte)rgb.blue;
		}
		if (data.transparentPixel != -1) {
			colorModel = new IndexColorModel(data.depth, rgbs.length, red, green, blue, data.transparentPixel);
		} else {
			colorModel = new IndexColorModel(data.depth, rgbs.length, red, green, blue);
		}		
		BufferedImage bufferedImage = new BufferedImage(colorModel, colorModel.createCompatibleWritableRaster(data.width, data.height), false, null);
		WritableRaster raster = bufferedImage.getRaster();
		int[] pixelArray = new int[1];
		for (int y = 0; y < data.height; y++) {
			for (int x = 0; x < data.width; x++) {
				int pixel = data.getPixel(x, y);
				pixelArray[0] = pixel;
				raster.setPixel(x, y, pixelArray);
			}
		}
		return bufferedImage;
	}
}

static ImageData convertToSWT(BufferedImage bufferedImage) {
	if (bufferedImage.getColorModel() instanceof DirectColorModel) {
		DirectColorModel colorModel = (DirectColorModel)bufferedImage.getColorModel();
		PaletteData palette = new PaletteData(colorModel.getRedMask(), colorModel.getGreenMask(), colorModel.getBlueMask());
		ImageData data = new ImageData(bufferedImage.getWidth(), bufferedImage.getHeight(), colorModel.getPixelSize(), palette);
		for (int y = 0; y < data.height; y++) {
			for (int x = 0; x < data.width; x++) {
				int rgb = bufferedImage.getRGB(x, y);
				int pixel = palette.getPixel(new RGB((rgb >> 16) & 0xFF, (rgb >> 8) & 0xFF, rgb & 0xFF)); 
				data.setPixel(x, y, pixel);
				if (colorModel.hasAlpha()) {
					data.setAlpha(x, y, (rgb >> 24) & 0xFF);
				}
			}
		}
		return data;		
	} else if (bufferedImage.getColorModel() instanceof IndexColorModel) {
		IndexColorModel colorModel = (IndexColorModel)bufferedImage.getColorModel();
		int size = colorModel.getMapSize();
		byte[] reds = new byte[size];
		byte[] greens = new byte[size];
		byte[] blues = new byte[size];
		colorModel.getReds(reds);
		colorModel.getGreens(greens);
		colorModel.getBlues(blues);
		RGB[] rgbs = new RGB[size];
		for (int i = 0; i < rgbs.length; i++) {
			rgbs[i] = new RGB(reds[i] & 0xFF, greens[i] & 0xFF, blues[i] & 0xFF);
		}
		PaletteData palette = new PaletteData(rgbs);
		ImageData data = new ImageData(bufferedImage.getWidth(), bufferedImage.getHeight(), colorModel.getPixelSize(), palette);
		data.transparentPixel = colorModel.getTransparentPixel();
		WritableRaster raster = bufferedImage.getRaster();
		int[] pixelArray = new int[1];
		for (int y = 0; y < data.height; y++) {
			for (int x = 0; x < data.width; x++) {
				raster.getPixel(x, y, pixelArray);
				data.setPixel(x, y, pixelArray[0]);
			}
		}
		return data;
	}
	return null;
}

static ImageData createSampleImage(Display display) {
	Image image = new Image(display, 100, 100);
	Rectangle bounds = image.getBounds();
	GC gc = new GC(image);
	gc.setBackground(display.getSystemColor(SWT.COLOR_BLUE));
	gc.fillRectangle(bounds);
	gc.setBackground(display.getSystemColor(SWT.COLOR_GREEN));
	gc.fillOval(0, 0, bounds.width, bounds.height);
	gc.setForeground(display.getSystemColor(SWT.COLOR_RED));
	gc.drawLine(0, 0, bounds.width, bounds.height);
	gc.drawLine(bounds.width, 0, 0, bounds.height);
	gc.dispose();
	ImageData data = image.getImageData();
	image.dispose();
	return data;
}

public static void main(String[] args) {
	Display display = new Display();
	Shell shell = new Shell(display);
	shell.setText("SWT Image");
	ImageData data;
	if (args.length > 0) {
		String fileName = args[0];
		data = new ImageData(fileName);
	} else {
		data = createSampleImage(display);
	}
	final Image swtImage = new Image(display, data);
	final BufferedImage awtImage = convertToAWT(data);
	final Image swtImage2 = new Image(display, convertToSWT(awtImage));
	shell.addListener(SWT.Paint, new Listener() {
		public void handleEvent(Event e) {
			int y = 10;
			if (swtImage != null) {
				e.gc.drawImage(swtImage, 10, y);
				y += swtImage.getBounds().height + 10;
			}
			if (swtImage2 != null) {
				e.gc.drawImage(swtImage2, 10, y);
			}
		}
	});
	Frame frame = new Frame() {
		public void paint(Graphics g) {
			Insets insets = getInsets();
			if (awtImage != null) {
				g.drawImage(awtImage, 10 + insets.left, 10 + insets.top, null);
			}
		}
	};
	frame.setTitle("AWT Image");
	shell.setLocation(50, 50);
	Rectangle bounds = swtImage.getBounds();
	shell.setSize(bounds.width + 50, bounds.height * 2 + 100);
	Point size = shell.getSize();
	Point location = shell.getLocation();
	Insets insets = frame.getInsets();
	frame.setLocation(location.x + size.x + 10, location.y);
	frame.setSize(size.x - (insets.left + insets.right), size.y - (insets.top + insets.bottom));
	frame.setVisible(true);	
	shell.open();
	while (!shell.isDisposed()) {
		if (!display.readAndDispatch()) display.sleep();
	}
	if (swtImage != null) swtImage.dispose();
	if (swtImage2 != null) swtImage.dispose();
	frame.dispose();
	display.dispose();
	/* Note: If you are using JDK 1.3.x, you need to use System.exit(0) at the end of your program to exit AWT.
	 * This is because in 1.3.x, AWT does not exit when the frame is disposed, because the AWT thread is not a daemon.
	 * This was fixed in JDK 1.4.x with the addition of the AWT Shutdown thread.
	 */
}
}
