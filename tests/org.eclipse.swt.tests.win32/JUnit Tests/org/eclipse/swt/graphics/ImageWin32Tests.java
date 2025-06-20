/*******************************************************************************
 * Copyright (c) 2024 Yatta Solutions
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Yatta Solutions - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.graphics;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.eclipse.swt.internal.DPIUtil;
import org.eclipse.swt.widgets.Display;
import org.junit.Before;
import org.junit.Test;

/**
 * Automated Tests for class org.eclipse.swt.graphics.Image
 * for Windows specific behavior
 *
 * @see org.eclipse.swt.graphics.Image
 */
@SuppressWarnings("restriction")
public class ImageWin32Tests {
	private Display display;

	@Before
	public void setUp() {
		display = Display.getDefault();
	}

	@Test
	public void testImageDataForDifferentFractionalZoomsShouldBeDifferent() {
		ImageGcDrawer noOpGcDrawer = (gc, height, width) -> {};
		Image image = new Image(display, noOpGcDrawer, 10, 10);
		int zoom1 = 125;
		int zoom2 = 150;
		ImageData imageDataAtZoom1 = image.getImageData(zoom1);
		ImageData imageDataAtZoom2 = image.getImageData(zoom2);
		assertNotEquals("ImageData::height should not be the same for imageData at different zoom levels",
				imageDataAtZoom1.height, imageDataAtZoom2.height);
		assertNotEquals("ImageData::width should not be the same for imageData at different zoom levels",
				imageDataAtZoom1.width, imageDataAtZoom2.width);
	}

	@Test
	public void testImageShouldHaveDimesionAsPerZoomLevel() {
		ImageGcDrawer noOpGcDrawer = (gc, height, width) -> {};
		int zoom = DPIUtil.getDeviceZoom();
		int scalingFactor = 2;
		Image image = new Image(display, noOpGcDrawer, 10, 10);
		try {
			ImageData baseImageData = image.getImageData(zoom);
			assertEquals("Width should equal the initial width on the same zoom", 10, baseImageData.width);
			ImageData scaledImageData = image.getImageData(zoom * scalingFactor);
			assertEquals("Width should be doubled on doubled zoom", 10*2, scaledImageData.width);
		} finally {
			image.dispose();
		}
}
}
