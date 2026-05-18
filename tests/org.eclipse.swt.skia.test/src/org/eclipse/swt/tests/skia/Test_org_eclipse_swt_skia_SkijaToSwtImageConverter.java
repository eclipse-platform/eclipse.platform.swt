package org.eclipse.swt.tests.skia;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.internal.graphics.SkijaToSwtImageConverter;
import org.junit.jupiter.api.Test;

import io.github.humbleui.skija.ColorAlphaType;
import io.github.humbleui.skija.ColorType;
import io.github.humbleui.skija.Image;
import io.github.humbleui.skija.ImageInfo;
import io.github.humbleui.skija.Surface;

public class Test_org_eclipse_swt_skia_SkijaToSwtImageConverter {

	@Test
	public void testConvertSimpleRGBAImage() {
		// Create a 2x2 Skija image with RGBA_8888
		try (Surface surface = Surface.makeRaster(new ImageInfo(2, 2, ColorType.RGBA_8888, ColorAlphaType.PREMUL))) {
			surface.getCanvas().clear(0xFF0000FF); // Blue
			final Image skijaImage = surface.makeImageSnapshot();
			final ImageData data = SkijaToSwtImageConverter.convertSkijaImageToImageData(skijaImage);
			assertNotNull(data);
			assertEquals(2, data.width);
			assertEquals(2, data.height);
			// Check if the data is blue (0,0,255)
			final int r = data.data[0] & 0xFF;
			final int g = data.data[1] & 0xFF;
			final int b = data.data[2] & 0xFF;
			// Print actual values for debugging
			System.out.printf("Pixel (0,0): R=%d G=%d B=%d\n", r, g, b);
			assertEquals(0, r, "Red channel should be 0");
			assertEquals(0, g, "Green channel should be 0");
			assertEquals(255, b, "Blue channel should be 255");
		}
	}

	@Test
	public void testConvertAlphaHandling() {
		// Create a 1x1 Skija image with alpha
		try (Surface surface = Surface.makeRaster(new ImageInfo(1, 1, ColorType.RGBA_8888, ColorAlphaType.PREMUL))) {
			surface.getCanvas().clear(0x80000000); // Half-transparent black
			final Image skijaImage = surface.makeImageSnapshot();
			final ImageData data = SkijaToSwtImageConverter.convertSkijaImageToImageData(skijaImage);
			assertNotNull(data);
			assertEquals(1, data.width);
			assertEquals(1, data.height);
			// Print actual alpha value for debugging
			final int alpha = data.alphaData[0] & 0xFF;
			System.out.printf("Pixel (0,0) alpha: %d\n", alpha);
			// Check if alpha is between 1 and 254 (exclusive)
			assertTrue(alpha > 0 && alpha < 255, "Alpha should be between 1 and 254, but was: " + alpha);
		}
	}

	@Test
	public void testNullInput() {
		// Should throw AssertionError or NullPointerException on null input
		final Throwable thrown = assertThrows(Throwable.class, () -> {
			SkijaToSwtImageConverter.convertSkijaImageToImageData(null);
		});
		assertTrue(thrown instanceof NullPointerException || thrown instanceof AssertionError,
				"Expected NullPointerException or AssertionError, but got: " + thrown.getClass().getName());
	}
}