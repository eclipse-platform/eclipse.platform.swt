/*******************************************************************************
 * Copyright (c) 2000, 2021 IBM Corporation and others.
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
 * Write an animated GIF to a file.
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */
import java.nio.file.Path;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

public class Snippet194 {
	Display display;
	Color white, red, green, blue;
	PaletteData palette;
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
		palette = new PaletteData(whiteRGB,	// 0
				redRGB,		// 1
				greenRGB,	// 2
				blueRGB);	// 3
		white = new Color(whiteRGB);
		red = new Color(redRGB);
		green = new Color(greenRGB);
		blue = new Color(blueRGB);
		font = new Font(display, "Comic Sans MS", 24, SWT.BOLD);

		ImageData[] data = new ImageData[4];
		data[0] = newFrame("",  white, false, 0, 0, 101, 55, SWT.DM_FILL_NONE, 40);
		data[1] = newFrame("S", red,   true,  0, 0,  30, 55, SWT.DM_FILL_NONE, 40);
		data[2] = newFrame("W", green, true, 28, 0,  39, 55, SWT.DM_FILL_NONE, 40);
		data[3] = newFrame("T", blue,  true, 69, 0,  32, 55, SWT.DM_FILL_BACKGROUND, 200);

		ImageLoader loader = new ImageLoader();
		loader.data = data;
		loader.backgroundPixel = 0;
		loader.logicalScreenHeight = data[0].height;
		loader.logicalScreenWidth = data[0].width;
		loader.repeatCount = 0; // run forever
		try {
			loader.save(Path.of("swt.gif"), SWT.IMAGE_GIF);
		} catch (SWTException ex) {
			System.out.println("Error saving GIF: " + ex);
		} finally {
			font.dispose();
			display.dispose();
		}
	}

	ImageData newFrame(String letter, Color color, boolean transparent, int x, int y, int width, int height, int disposalMethod, int delayTime) {
		ImageData temp = new ImageData(width, height, 2, palette); // 4-color palette has depth 2
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
