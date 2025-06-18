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

import org.eclipse.swt.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.widgets.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;

@ExtendWith(PlatformSpecificExecutionExtension.class)
@ExtendWith(WithMonitorSpecificScalingExtension.class)
class ImagesWin32Tests {

	@Test
	public void test_imageScaledFrom150_when125PercentImageUnavailable() {
		Display display = Display.getDefault();
		GC gc = new GC(display);
		GCData gcData = gc.getGCData();
		gcData.nativeZoom = 125;
		ImageDataProvider imageDataProviderReturnsNullAt125 = zoom -> {
			if (zoom == 125) {
				return null;
			}
			float scaleFactor = zoom / 100f;
			int scaledWidth = Math.round(16 * scaleFactor);
			int scaledHeight = Math.round(16 * scaleFactor);
			return new ImageData(scaledWidth, scaledHeight, 1, new PaletteData(new RGB(255, 0, 0)));
		};
		Image image = new Image(display, imageDataProviderReturnsNullAt125);
		gc.drawImage(image, 0, 0, 16, 16, 0, 0, 16, 16);
		gc.dispose();
		image.dispose();
	}

	@Test
	public void test_imageScaledFrom100_when125And150PercentImagesUnavailable() {
		Display display = Display.getDefault();
		GC gc = new GC(display);
		GCData gcData = gc.getGCData();
		gcData.nativeZoom = 125;
		ImageDataProvider imageDataProviderReturnsNullAt125and150 = zoom -> {
			if (zoom == 125) {
				return null;
			}
			if (zoom == 150) {
				return null;
			}
			float scaleFactor = zoom / 100f;
			int scaledWidth = Math.round(16 * scaleFactor);
			int scaledHeight = Math.round(16 * scaleFactor);
			return new ImageData(scaledWidth, scaledHeight, 1, new PaletteData(new RGB(255, 0, 0)));
		};
		Image image = new Image(display, imageDataProviderReturnsNullAt125and150);
		gc.drawImage(image, 0, 0, 16, 16, 0, 0, 16, 16);
		gc.dispose();
		image.dispose();
	}

	@Test
	public void test_drawImage_when130PercentImageUnavailable() {
		Display display = Display.getDefault();
		GC gc = new GC(display);
		GCData gcData = gc.getGCData();
		gcData.nativeZoom = 130;
		ImageDataProvider imageDataProviderReturnsNullAt125 = zoom -> {
			if (zoom == 130){
				return null;
			}
			float scaleFactor = zoom / 100f;
			int scaledWidth = Math.round(16 * scaleFactor);
			int scaledHeight = Math.round(16 * scaleFactor);
			return new ImageData(scaledWidth, scaledHeight, 1, new PaletteData(new RGB(255, 0, 0)));
		};
		Image image = new Image(display, imageDataProviderReturnsNullAt125);
		gc.drawImage(image, 0, 0, 16, 16, 0, 0, 16, 16);
		gc.dispose();
		image.dispose();
	}

	@Test
	public void test_drawImageUsesWronglyScaledImageDataProvider() {
		Display display = Display.getDefault();
		GC gc = new GC(display);
		GCData gcData = gc.getGCData();
		gcData.nativeZoom = 125;
		ImageDataProvider incorrectlyScaledimageDataProvider = zoom -> {
			int scaleFactor = zoom / 100;
			int scaledWidth = Math.round(16 * scaleFactor);
			int scaledHeight = Math.round(16 * scaleFactor);
			return new ImageData(scaledWidth, scaledHeight, 1, new PaletteData(new RGB(255, 0, 0)));
		};
		Image image = new Image(display, incorrectlyScaledimageDataProvider);
		gc.drawImage(image, 0, 0, 16, 16, 0, 0, 16, 16);
		gc.dispose();
		image.dispose();
	}

	@Test
	public void testImageIconTypeShouldNotChangeAfterCallingGetHandleForDifferentZoom() {
		Image icon = Display.getDefault().getSystemImage(SWT.ICON_ERROR);
		try {
			Image.win32_getHandle(icon, 200);
			assertEquals("Image type should stay to SWT.ICON", SWT.ICON, icon.type);
		} finally {
			icon.dispose();
		}
	}
}
