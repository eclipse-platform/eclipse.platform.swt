package org.eclipse.swt.tests.skia;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.internal.graphics.SkijaToSwtImageConverter;
import org.junit.jupiter.api.Test;

import io.github.humbleui.skija.ColorAlphaType;
import io.github.humbleui.skija.ColorType;
import io.github.humbleui.skija.Image;
import io.github.humbleui.skija.ImageInfo;
import io.github.humbleui.skija.Surface;

class Test_org_eclipse_swt_skia_SkijaToSwtImageConvert {

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

}
