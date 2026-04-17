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
 *     Christian Hahn (ch@medianetz.de) - Original Version
 *     Laurent Caron (laurent.caron at gmail dot com) - Conversion to SWT
 *     IBM Corporation - adaptation to GraphicsExample
 *******************************************************************************/

package org.eclipse.swt.examples.graphics;

import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;

/**
 * This tab displays an animated voxel terrain rendered by raycasting against a
 * heightmap and colormap. The camera automatically flies over the landscape.
 */
public class VoxelTab extends AnimatedGraphicsTab {

	private static final int RENDER_WIDTH = 640;
	private static final int RENDER_HEIGHT = 400;

	private static final int FIXP_SHIFT = 12;
	private static final int FIXP_MUL = 4096;
	private static final int HFIELD_WIDTH = 512;
	private static final int HFIELD_HEIGHT = 512;
	private static final int HFIELD_BIT_SHIFT = 9;
	private static final int TERRAIN_SCALE_X2 = 2;
	private static final int ANGLE_360 = 1920;
	private static final int ANGLE_30 = 160;
	private static final int MAX_STEPS = 200;

	private ImageData imageData;
	private Image outputImage;
	private int[] heightMap;
	private int[] colorMap;
	private int[] virtualScreen;
	private int[] cosLook;
	private int[] sinLook;

	private int viewAngY = 960;
	private int viewPosX = 256;
	private int viewPosY = 256;
	private int viewPosZ = 700;
	private final int viewAngX = 80;
	private int speed = 4;
	private final int dslope = (int) (1.0 / (RENDER_WIDTH / 64.0) * FIXP_MUL);

	public VoxelTab(GraphicsExample example) {
		super(example);
	}

	@Override
	public String getCategory() {
		return GraphicsExample.getResourceString("Misc"); //$NON-NLS-1$
	}

	@Override
	public String getText() {
		return GraphicsExample.getResourceString("Voxel"); //$NON-NLS-1$
	}

	@Override
	public String getDescription() {
		return GraphicsExample.getResourceString("VoxelDescription"); //$NON-NLS-1$
	}

	@Override
	public int getInitialAnimationTime() {
		return 30;
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
		if (virtualScreen == null) return;

		viewAngY = (viewAngY + 2) % ANGLE_360;
		viewPosX = ((viewPosX + (speed * cosLook(viewAngY) >> FIXP_SHIFT)) % HFIELD_WIDTH + HFIELD_WIDTH) % HFIELD_WIDTH;
		viewPosY = ((viewPosY + (speed * sinLook(viewAngY) >> FIXP_SHIFT)) % HFIELD_HEIGHT + HFIELD_HEIGHT) % HFIELD_HEIGHT;

		java.util.Arrays.fill(virtualScreen, 0);

		int destBuffer = 0;
		int vpX = viewPosX << FIXP_SHIFT;
		int vpY = viewPosY << FIXP_SHIFT;
		int vpZ = viewPosZ << FIXP_SHIFT;
		destBuffer += RENDER_WIDTH * (RENDER_HEIGHT - 1);
		int raycastAng = viewAngY + ANGLE_30;

		for (int currColumn = 0; currColumn < RENDER_WIDTH - 1; currColumn++) {
			int xRay = vpX;
			int yRay = vpY;
			int zRay = vpZ;
			int dx = cosLook(raycastAng) << 1;
			int dy = sinLook(raycastAng) << 1;
			int dz = dslope * (viewAngX - RENDER_HEIGHT);
			int destColumnPtr = destBuffer;
			int currVoxelScale = 0;
			int currRow = 0;

			for (int currStep = 0; currStep < MAX_STEPS; currStep++) {
				int xr = (xRay >> FIXP_SHIFT) & (HFIELD_WIDTH - 1);
				int yr = (yRay >> FIXP_SHIFT) & (HFIELD_HEIGHT - 1);
				int mapAddr = xr + (yr << HFIELD_BIT_SHIFT);
				int columnHeight = heightMap[mapAddr] << (FIXP_SHIFT + TERRAIN_SCALE_X2);
				if (columnHeight > zRay) {
					while (true) {
						virtualScreen[destColumnPtr] = colorMap[mapAddr];
						dz += dslope;
						zRay += currVoxelScale;
						destColumnPtr -= RENDER_WIDTH;
						if (++currRow >= RENDER_HEIGHT) {
							currStep = MAX_STEPS;
							break;
						}
						if (zRay > columnHeight) {
							break;
						}
					}
				}
				xRay += dx;
				yRay += dy;
				zRay += dz;
				currVoxelScale += dslope;
			}
			destBuffer++;
			raycastAng--;
		}

		imageData.setPixels(0, 0, RENDER_WIDTH * RENDER_HEIGHT, virtualScreen, 0);
	}

	@Override
	public void paint(GC gc, int width, int height) {
		if (!example.checkAdvancedGraphics()) return;

		if (virtualScreen == null) {
			Image heightLoaded = example.loadImage(gc.getDevice(), "heightmap1.gif"); //$NON-NLS-1$
			Image colorLoaded = example.loadImage(gc.getDevice(), "colormap.jpg"); //$NON-NLS-1$
			if (heightLoaded == null || colorLoaded == null) return;
			ImageData heightImage = heightLoaded.getImageData();
			ImageData colorImage = colorLoaded.getImageData();

			heightMap = new int[HFIELD_WIDTH * HFIELD_HEIGHT];
			heightImage.getPixels(0, 0, HFIELD_WIDTH * HFIELD_HEIGHT, heightMap, 0);
			for (int i = 0; i < heightMap.length; i++) {
				heightMap[i] &= 0xff;
			}

			colorMap = new int[HFIELD_WIDTH * HFIELD_HEIGHT];
			colorImage.getPixels(0, 0, HFIELD_WIDTH * HFIELD_HEIGHT, colorMap, 0);

			imageData = new ImageData(RENDER_WIDTH, RENDER_HEIGHT, colorImage.depth, colorImage.palette);
			virtualScreen = new int[RENDER_WIDTH * RENDER_HEIGHT];

			cosLook = new int[ANGLE_360];
			sinLook = new int[ANGLE_360];
			for (int a = 0; a < ANGLE_360; a++) {
				double rad = 2 * Math.PI * a / ANGLE_360;
				cosLook[a] = (int) (Math.cos(rad) * FIXP_MUL);
				sinLook[a] = (int) (Math.sin(rad) * FIXP_MUL);
			}
		}

		if (imageData == null) return;

		if (outputImage != null) {
			outputImage.dispose();
		}
		outputImage = new Image(gc.getDevice(), imageData);

		int x = (width - RENDER_WIDTH) / 2;
		int y = (height - RENDER_HEIGHT) / 2;
		gc.drawImage(outputImage, x, y);
	}

	private int cosLook(int theta) {
		if (theta < 0) return cosLook[theta + ANGLE_360];
		if (theta >= ANGLE_360) return cosLook[theta - ANGLE_360];
		return cosLook[theta];
	}

	private int sinLook(int theta) {
		if (theta < 0) return sinLook[theta + ANGLE_360];
		if (theta >= ANGLE_360) return sinLook[theta - ANGLE_360];
		return sinLook[theta];
	}
}
