/*******************************************************************************
 * Copyright (c) 2018 Laurent Caron and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Laurent Caron (laurent.caron at gmail dot com) - Conversion to SWT
 *     IBM Corporation - adaptation to GraphicsExample
 *******************************************************************************/

package org.eclipse.swt.examples.graphics;

import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;

/**
 * This tab displays an animated sine-scroller: a classic amiga-style banner
 * text flying across a starfield and gradient bars, waving along a sine curve.
 */
public class SineScrollTab extends AnimatedGraphicsTab {

	private static final int RENDER_WIDTH = 320;
	private static final int RENDER_HEIGHT = 240;
	private static final int DISPLAY_SCALE = 2;
	private static final int BG_COLOR = 0x000000;
	private static final int NUM_STARS = 250;
	private static final String SCROLL_TEXT = "This is an old-fashion amiga demo !";

	private ImageData imageData;
	private Image outputImage;
	private short[] font;
	private byte[] fontLookup;
	private byte[] scrollText;
	private int[] rasters;
	private int[] sine;
	private int[] background;
	private int[] scrPix;
	private Star[] stars;
	private int scrollPos, scrollOffset, sinePos;

	public SineScrollTab(GraphicsExample example) {
		super(example);
	}

	@Override
	public String getCategory() {
		return GraphicsExample.getResourceString("Misc"); //$NON-NLS-1$
	}

	@Override
	public String getText() {
		return GraphicsExample.getResourceString("SineScroll"); //$NON-NLS-1$
	}

	@Override
	public String getDescription() {
		return GraphicsExample.getResourceString("SineScrollDescription"); //$NON-NLS-1$
	}

	@Override
	public int getInitialAnimationTime() {
		return 10;
	}

	@Override
	public void dispose() {
		if (outputImage != null) {
			outputImage.dispose();
			outputImage = null;
		}
	}

	@Override
	public void next(int width, int height) {
		if (scrPix == null) return;

		int sco = scrollOffset;
		int scp = scrollPos;
		int sp = sinePos;

		if (++scrollOffset >= 16) {
			scrollOffset = 0;
			if (++scrollPos >= scrollText.length) {
				scrollPos = 0;
			}
		}
		sinePos += 4;
		if (sinePos >= sine.length) {
			sinePos -= sine.length;
		}

		System.arraycopy(background, 0, scrPix, 0, scrPix.length);

		for (int i = 0; i < stars.length; i++) {
			Star s = stars[i];
			while (s.x >= RENDER_WIDTH) s.x -= RENDER_WIDTH;
			if ((scrPix[s.y * RENDER_WIDTH + s.x] & 0xFFFFFF) == BG_COLOR) {
				scrPix[s.y * RENDER_WIDTH + s.x] = s.color;
			}
			s.x += s.dx;
		}

		for (int x = 0; x < RENDER_WIDTH; x++) {
			int ypos = sine[sp++];
			if (sp >= sine.length) sp = 0;

			short data = font[(scrollText[scp] & 0xFF) * 16 + sco];
			if (++sco >= 16) {
				sco = 0;
				if (++scp >= scrollText.length) scp = 0;
			}

			for (int y = 0; y < 16; y++) {
				if ((data & 1 << y) != 0) {
					int py = y + ypos;
					if (py < 0 || py >= RENDER_HEIGHT) continue;
					scrPix[py * RENDER_WIDTH + x] = rasters[py];
					if (py + 2 < RENDER_HEIGHT && x + 2 < RENDER_WIDTH) {
						scrPix[(py + 2) * RENDER_WIDTH + x + 2] = 0x000000;
					}
				}
			}
		}

		imageData.setPixels(0, 0, scrPix.length, scrPix, 0);
	}

	@Override
	public void paint(GC gc, int width, int height) {
		if (!example.checkAdvancedGraphics()) return;

		if (scrPix == null) {
			Image fontImg = example.loadImage(gc.getDevice(), "font_classic_16x16.gif"); //$NON-NLS-1$
			if (fontImg == null) return;
			loadFont(fontImg.getImageData());
			initScrollText(SCROLL_TEXT);
			initRasters();
			initSine();
			initBackground();
			initStars();

			scrPix = new int[RENDER_WIDTH * RENDER_HEIGHT];
			scrollOffset = 0;
			scrollPos = 0;
			sinePos = 0;
			imageData = new ImageData(RENDER_WIDTH, RENDER_HEIGHT, 24,
					new PaletteData(0xFF0000, 0xFF00, 0xFF));
		}

		if (imageData == null) return;

		if (outputImage != null) {
			outputImage.dispose();
		}
		outputImage = new Image(gc.getDevice(), imageData);

		int dw = RENDER_WIDTH * DISPLAY_SCALE;
		int dh = RENDER_HEIGHT * DISPLAY_SCALE;
		int x = (width - dw) / 2;
		int y = (height - dh) / 2;
		gc.drawImage(outputImage, 0, 0, RENDER_WIDTH, RENDER_HEIGHT, x, y, dw, dh);
	}

	private void loadFont(ImageData imgData) {
		fontLookup = new byte[256];
		String s = " !'(),-.:?0123456789abcdefghijklmnopqrstuvwxyz";
		char[] sc = s.toCharArray();
		for (int i = 0; i < sc.length; i++) {
			fontLookup[sc[i] & 0xFF] = (byte) i;
		}
		for (char c = 'A'; c <= 'Z'; c++) {
			fontLookup[c] = (byte) (c - 'A' + 20);
		}

		int w = imgData.width;
		font = new short[46 * 16];
		int xpos = 0;
		int ypos = 0;
		for (int i = 0; i < font.length; i++) {
			if (xpos >= w) {
				xpos = 0;
				ypos += 16;
			}
			short data = 0;
			for (int y = 0; y < 16; y++) {
				int pixel = imgData.getPixel(xpos, ypos + y);
				if ((pixel & 0xFFFFFF) != 0) {
					data |= 1 << y;
				}
			}
			font[i] = data;
			xpos++;
		}
	}

	private void initScrollText(String text) {
		String padded = "                    " + text;
		char[] chrs = padded.toCharArray();
		scrollText = new byte[chrs.length];
		for (int i = 0; i < chrs.length; i++) {
			scrollText[i] = fontLookup[chrs[i] & 0xFF];
		}
	}

	private void initBackground() {
		background = new int[RENDER_WIDTH * RENDER_HEIGHT];
		for (int i = 0; i < background.length; i++) {
			background[i] = BG_COLOR;
		}
	}

	private void initRasters() {
		int[] cols = { 0xFF0000, 0xFFFF00, 0x00FF00, 0x00FFFF, 0xFF00FF, 0x00FFFF, 0x0000FF };
		int bar = 0;
		int seg = RENDER_HEIGHT / (cols.length - 1);
		rasters = new int[RENDER_HEIGHT];

		int rFrom = (cols[0] >> 16) & 0xFF;
		int gFrom = (cols[0] >> 8) & 0xFF;
		int bFrom = cols[0] & 0xFF;

		for (int x = 1; x <= cols.length - 1; x++) {
			int rTo = (cols[x] >> 16) & 0xFF;
			int gTo = (cols[x] >> 8) & 0xFF;
			int bTo = cols[x] & 0xFF;
			for (int y = 0; y < seg && bar < RENDER_HEIGHT; y++) {
				int r = rFrom + (rTo - rFrom) * (y + 1) / seg;
				int g = gFrom + (gTo - gFrom) * (y + 1) / seg;
				int b = bFrom + (bTo - bFrom) * (y + 1) / seg;
				rasters[bar++] = (r << 16) | (g << 8) | b;
			}
			rFrom = rTo;
			gFrom = gTo;
			bFrom = bTo;
		}
		while (bar < RENDER_HEIGHT) {
			rasters[bar++] = 0x0000FF;
		}
	}

	private void initSine() {
		sine = new int[1024];
		double ampl = (RENDER_HEIGHT - 18) / 2.0;
		for (int i = 0; i < sine.length; i++) {
			sine[i] = (int) (Math.sin(i * 0.50 * Math.PI / 256.0)
					* Math.sin(i * 0.75 * Math.PI / 256.0) * ampl + ampl) + 1;
		}
	}

	private void initStars() {
		stars = new Star[NUM_STARS];
		for (int i = 0; i < stars.length; i++) {
			stars[i] = new Star();
		}
	}

	private static class Star {
		int x, y, dx, color;

		Star() {
			x = (int) (Math.random() * RENDER_WIDTH);
			y = (int) (Math.random() * RENDER_HEIGHT);
			dx = (int) (Math.random() * 4.0) + 1;
			color = ((int) (Math.random() * 11.0) + 4) * 0x111111;
		}
	}
}
