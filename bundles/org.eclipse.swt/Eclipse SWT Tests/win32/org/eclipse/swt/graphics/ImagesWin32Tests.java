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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.eclipse.swt.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.widgets.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;

@ExtendWith(PlatformSpecificExecutionExtension.class)
@ExtendWith(WithMonitorSpecificScalingExtension.class)
class ImagesWin32Tests {

	@Test
	public void testImageIconTypeShouldNotChangeAfterCallingGetHandleForDifferentZoom() {
		Image icon = Display.getDefault().getSystemImage(SWT.ICON_ERROR);
		try {
			Image.win32_getHandle(icon, 200);
			assertEquals(SWT.ICON, icon.type, "Image type should stay to SWT.ICON");
		} finally {
			icon.dispose();
		}
	}

	/**
	 * Tests that a GC.drawImage() handle is reused across consecutive calls at
	 * different pixel sizes when the image only provides 100% zoom data. Because
	 * every zoom request falls back to the same 100% data, the effective (nearest
	 * available) zoom is always 100%, and a freshly allocated handle should not be
	 * required for each different draw size.
	 * <p>
	 * See https://github.com/eclipse-platform/eclipse.platform.swt/issues/3419
	 */
	@Test
	public void testDrawingHandleIsReusedForSingleZoomImageAtDifferentSizes() {
		PaletteData palette = new PaletteData(0xFF0000, 0xFF00, 0xFF);
		ImageData imageData = new ImageData(10, 10, 24, palette);
		// Provider only has 100% data; returns null for every other zoom
		Image image = new Image(Display.getDefault(), (ImageDataProvider) zoom -> zoom == 100 ? imageData : null);
		long[] firstHandle = {0};
		long[] secondHandle = {0};
		try {
			// 20x20 pixels → 200% zoom equivalent for a 10x10 base image
			image.executeOnImageHandleAtBestFittingSize(h -> firstHandle[0] = h.handle(), 20, 20);
			// 30x30 pixels → 300% zoom equivalent; provider still falls back to 100%,
			// so the nearest available zoom is still 100% and the handle must be reused
			image.executeOnImageHandleAtBestFittingSize(h -> secondHandle[0] = h.handle(), 30, 30);
			assertNotEquals(0L, firstHandle[0], "First handle should be non-zero");
			assertEquals(firstHandle[0], secondHandle[0],
					"Consecutive GC.drawImage() calls at different sizes should reuse the same "
					+ "handle when the nearest available zoom is the same (100% in this case)");
		} finally {
			image.dispose();
		}
	}

	/**
	 * Tests that a GC.drawImage() handle is reused across consecutive calls at
	 * different pixel sizes when the image provides data at 100% and 200% zoom.
	 * Sizes that both map to the 200% nearest available zoom (e.g. 200% and 250%)
	 * should share the same underlying handle without re-allocating it.
	 * <p>
	 * See https://github.com/eclipse-platform/eclipse.platform.swt/issues/3419
	 */
	@Test
	public void testDrawingHandleIsReusedForTwoZoomImageAtSizesWithSameNearestZoom() {
		PaletteData palette = new PaletteData(0xFF0000, 0xFF00, 0xFF);
		ImageData imageData100 = new ImageData(10, 10, 24, palette);
		ImageData imageData200 = new ImageData(20, 20, 24, palette);
		// Provider has explicit data at 100% and 200%; returns null for anything else
		Image image = new Image(Display.getDefault(), (ImageDataProvider) zoom -> zoom == 100 ? imageData100 : zoom == 200 ? imageData200 : null);
		long[] firstHandle = {0};
		long[] secondHandle = {0};
		try {
			// 20x20 pixels → exactly 200% zoom for the 10x10 base image; uses 200% data
			image.executeOnImageHandleAtBestFittingSize(h -> firstHandle[0] = h.handle(), 20, 20);
			// 25x25 pixels → 250% zoom equivalent; nearest available is 200%, so the
			// previously cached 200% handle should be reused
			image.executeOnImageHandleAtBestFittingSize(h -> secondHandle[0] = h.handle(), 25, 25);
			assertNotEquals(0L, firstHandle[0], "First handle should be non-zero");
			assertEquals(firstHandle[0], secondHandle[0],
					"Consecutive GC.drawImage() calls at different sizes should reuse the same "
					+ "handle when the nearest available zoom is the same (200% in this case)");
		} finally {
			image.dispose();
		}
	}

	/**
	 * Tests that GC.drawImage() handles differ when consecutive calls at different
	 * pixel sizes land in different nearest-available-zoom regions for an image
	 * that provides distinct data at 100% and 200%. A size mapping to 100% and a
	 * size mapping to 200% must not share the same native handle, as they would
	 * represent different pixel content.
	 * <p>
	 * See https://github.com/eclipse-platform/eclipse.platform.swt/issues/3419
	 */
	@Test
	public void testHandlesAreDifferentForTwoZoomImageAtDifferentNearestZooms() {
		PaletteData palette = new PaletteData(0xFF0000, 0xFF00, 0xFF);
		ImageData imageData100 = new ImageData(10, 10, 24, palette);
		ImageData imageData200 = new ImageData(20, 20, 24, palette);
		// Provider has explicit data at 100% and 200%; returns null for anything else
		Image image = new Image(Display.getDefault(), (ImageDataProvider) zoom -> zoom == 100 ? imageData100 : zoom == 200 ? imageData200 : null);
		long[] handle100Zone = {0};
		long[] handle200Zone = {0};
		try {
			// 10x10 pixels → 100% zoom for the 10x10 base image; nearest available is 100%
			image.executeOnImageHandleAtBestFittingSize(h -> handle100Zone[0] = h.handle(), 10, 10);
			// 20x20 pixels → 200% zoom; nearest available is 200% → must differ from the
			// 100% handle since the underlying pixel data is different
			image.executeOnImageHandleAtBestFittingSize(h -> handle200Zone[0] = h.handle(), 20, 20);
			assertNotEquals(0L, handle100Zone[0], "First handle should be non-zero");
			assertNotEquals(handle100Zone[0], handle200Zone[0],
					"GC.drawImage() calls where the nearest available zoom differs must not "
					+ "reuse the same handle (100% data vs 200% data)");
		} finally {
			image.dispose();
		}
	}

	/**
	 * Tests that a GC.drawImage() handle at the exact imageZoom is returned in
	 * preference to a handle at nearestAvailableZoom when both are present in the
	 * image's handle manager.
	 * <p>
	 * Explicitly creating a persistent handle at 200% via
	 * {@link Image#win32_getHandle(Image, int)} places it in the handle manager.
	 * When GC.drawImage() then targets a pixel size that maps to imageZoom=200
	 * (while nearestAvailableZoom stays 100% because the provider only has 100%
	 * data), the 200% handle must be found first via the imageZoom lookup and
	 * returned instead of the 100% one.
	 * <p>
	 * See https://github.com/eclipse-platform/eclipse.platform.swt/issues/3419
	 */
	@Test
	public void testDrawImagePrefersExistingHandleAtExactImageZoom() {
		PaletteData palette = new PaletteData(0xFF0000, 0xFF00, 0xFF);
		ImageData imageData = new ImageData(10, 10, 24, palette);
		Image image = new Image(Display.getDefault(), (ImageDataProvider) zoom -> zoom == 100 ? imageData : null);
		try {
			long handle100 = Image.win32_getHandle(image, 100);
			long handle200 = Image.win32_getHandle(image, 200);
			assertNotEquals(0L, handle100, "100% handle should be non-zero");
			assertNotEquals(0L, handle200, "200% handle should be non-zero");
			assertNotEquals(handle100, handle200, "Handles for different zooms should be distinct native objects");
			long[] drawHandle = {0};
			// 20x20 pixels → imageZoom=200 for a 10x10 base; nearestAvailableZoom=100
			// The 200% handle already in the handle manager must be found and preferred
			image.executeOnImageHandleAtBestFittingSize(h -> drawHandle[0] = h.handle(), 20, 20);
			assertEquals(handle200, drawHandle[0],
					"GC.drawImage() should prefer the existing handle at imageZoom=200 "
					+ "over the nearestAvailableZoom=100 handle");
		} finally {
			image.dispose();
		}
	}

	/**
	 * Tests that when the pixel size requested by GC.drawImage() maps to a zoom
	 * equal to a current monitor's zoom, a persistent handle is created for that
	 * zoom level and is subsequently reusable via
	 * {@link Image#win32_getHandle(Image, int)}.
	 * <p>
	 * A shell is created so that {@code Display.getShells()} is non-empty and
	 * the monitor zoom is visible to the handle-selection logic. Drawing at a
	 * pixel size whose imageZoom equals the shell's zoom triggers the
	 * monitor-zoom persistence path: the handle is stored in the image's handle
	 * manager and can be retrieved without allocating a second native object.
	 * On 100%-DPI machines the new {@code imageZoom == monitorZoom} branch
	 * overlaps with the existing {@code nearestAvailableZoom == 100} fallback;
	 * on HiDPI machines the new branch is exercised in isolation.
	 * <p>
	 * See https://github.com/eclipse-platform/eclipse.platform.swt/issues/3419
	 */
	@Test
	public void testDrawImageCreatesAndReusesPersistentHandleForMonitorZoomImageZoom() {
		PaletteData palette = new PaletteData(0xFF0000, 0xFF00, 0xFF);
		ImageData imageData = new ImageData(10, 10, 24, palette);
		Image image = new Image(Display.getDefault(), (ImageDataProvider) zoom -> zoom == 100 ? imageData : null);
		Shell shell = new Shell(Display.getDefault());
		try {
			int shellZoom = shell.getZoom();
			int pixelSize = 10 * shellZoom / 100;
			long[] drawHandle = {0};
			// Drawing at shellZoom's pixel size → imageZoom == shellZoom == monitorZoom
			// A persistent handle must be created and stored in the handle manager
			image.executeOnImageHandleAtBestFittingSize(h -> drawHandle[0] = h.handle(), pixelSize, pixelSize);
			// If persisted, win32_getHandle returns the same native handle without a new allocation
			long persistedHandle = Image.win32_getHandle(image, shellZoom);
			assertNotEquals(0L, drawHandle[0], "Draw handle should be non-zero");
			assertEquals(persistedHandle, drawHandle[0],
					"GC.drawImage() at the monitor zoom should create a persistent handle "
					+ "so that win32_getHandle returns the same native object");
		} finally {
			image.dispose();
			shell.dispose();
		}
	}

	/**
	 * Tests that a persistent native handle already created via
	 * {@link Image#win32_getHandle(Image, int)} is found and reused by
	 * GC.drawImage() when the nearest available zoom for the requested draw size
	 * maps to the same zoom. This verifies that the
	 * {@code imageHandleManager.get(nearestAvailableZoom)} lookup is effective and
	 * avoids redundant handle allocation.
	 * <p>
	 * See https://github.com/eclipse-platform/eclipse.platform.swt/issues/3419
	 */
	@Test
	public void testDrawImageReusesExistingPersistentHandleForNearestAvailableZoom() {
		PaletteData palette = new PaletteData(0xFF0000, 0xFF00, 0xFF);
		ImageData imageData = new ImageData(10, 10, 24, palette);
		// Provider only has 100% data; every zoom falls back to 100%
		Image image = new Image(Display.getDefault(), (ImageDataProvider) zoom -> zoom == 100 ? imageData : null);
		try {
			// Force creation of a persistent 100% handle (as would happen via GC.drawImage
			// on a 100% zoom canvas or via Image.getImageData())
			long persistentHandle = Image.win32_getHandle(image, 100);
			assertNotEquals(0L, persistentHandle, "Persistent handle should be non-zero");
			long[] drawHandle = {0};
			// 20x20 pixels → 200% zoom equivalent for the 10x10 base image; nearest
			// available is still 100%, so the already-cached persistent handle must be
			// returned without allocating a new one
			image.executeOnImageHandleAtBestFittingSize(h -> drawHandle[0] = h.handle(), 20, 20);
			assertEquals(persistentHandle, drawHandle[0],
					"GC.drawImage() should reuse the existing persistent handle when the "
					+ "nearest available zoom matches the cached handle's zoom (100% here)");
		} finally {
			image.dispose();
		}
	}
}
