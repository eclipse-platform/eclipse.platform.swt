/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
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
 * Write an animated GIF to a file.
 * 
 * For a list of all SWT example snippets see
 * http://dev.eclipse.org/viewcvs/index.cgi/%7Echeckout%7E/platform-swt-home/dev.html#snippets
 */
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

public class Snippet194 {
	Display display;
	Color white, red, green, blue;
	Font font;

	public static void main(String[] args) {
		new Snippet194().run();
	}
	
	public void run() {
		display = new Display();
		RGB whiteRGB = new RGB(0xff, 0xff, 0xff);
		RGB redRGB = new RGB(0xff, 0, 0);
		RGB greenRGB = new RGB(0, 0xff, 0);
		RGB blueRGB = new RGB(0, 0, 0xff);
		PaletteData palette = new PaletteData(new RGB[] {
				whiteRGB,	// 0
				redRGB,		// 1
				greenRGB,	// 2
				blueRGB });	// 3
		white = new Color(display, whiteRGB);
		red = new Color(display, redRGB);
		green = new Color(display, greenRGB);
		blue = new Color(display, blueRGB);
		font = new Font(display, "Comic Sans MS", 24, SWT.BOLD);
		
		ImageData[] data = new ImageData[4];
		data[0] = newFrame("",  white, palette, false, 0, 0, 101, 55, SWT.DM_FILL_NONE, 40);
		data[1] = newFrame("S", red,   palette, true,  0, 0,  30, 55, SWT.DM_FILL_NONE, 40);
		data[2] = newFrame("W", green, palette, true, 28, 0,  39, 55, SWT.DM_FILL_NONE, 40);
		data[3] = newFrame("T", blue,  palette, true, 69, 0,  32, 55, SWT.DM_FILL_BACKGROUND, 200);
		font.dispose();
		
		ImageLoader loader = new ImageLoader();
		loader.data = data;
		loader.backgroundPixel = 0;
		loader.logicalScreenHeight = data[0].height;
		loader.logicalScreenWidth = data[0].width;
		loader.repeatCount = 0; // run forever
		loader.save("swt.gif", SWT.IMAGE_GIF);
	}

	ImageData newFrame(String letter, Color color, PaletteData palette, boolean transparent, int x, int y, int width, int height, int disposalMethod, int delayTime) {
		ImageData temp = new ImageData(width, height, palette.getRGBs().length, palette);
		Image image = new Image(display, temp);
		GC gc = new GC(image);
		gc.setBackground(white);
		gc.fillRectangle(0, 0, width, height);
		gc.setForeground(color);
		gc.setFont(font);
		gc.drawString(letter, 5, 5);
		gc.dispose();
		ImageData frame = image.getImageData();
		if (transparent) frame.transparentPixel = 0; // white
		image.dispose();
		frame.x = x;
		frame.y = y;
		frame.disposalMethod = disposalMethod;
		frame.delayTime = delayTime;
		return frame;
	}
}
