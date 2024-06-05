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
	public void imageMustBeRescaledOnZoomChange() {
		int zoom = DPIUtil.getDeviceZoom();
		Image image = new Image(display, 10, 10);

		try {
			ImageData baseImageData = image.getImageData(zoom);
			assertEquals("Width should equal the initial width on the same zoom", 10, baseImageData.width);
			Image scaledImage = Image.win32_new(image, zoom);
			ImageData scaledImageData = scaledImage.getImageData(zoom*2);
			assertEquals("Width should be doubled on doubled zoom", 10*2, scaledImageData.width);
			scaledImage = Image.win32_new(image, zoom*2);
			baseImageData = scaledImage.getImageData(zoom);
			assertEquals("Width of ImageData must not be affected by a zoom change", 10, baseImageData.width);
		} finally {
			image.dispose();
		}
}
}
