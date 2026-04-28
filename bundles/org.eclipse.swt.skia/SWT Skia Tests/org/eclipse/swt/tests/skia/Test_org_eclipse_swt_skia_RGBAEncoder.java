package org.eclipse.swt.tests.skia;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.internal.graphics.RGBAEncoder;
import org.junit.jupiter.api.Test;

public class Test_org_eclipse_swt_skia_RGBAEncoder {

	@Test
	public void testIndexed8BitNoTransparency() {
		// Palette with red, green, blue
		final PaletteData palette = new PaletteData(new RGB(255,0,0), new RGB(0,255,0), new RGB(0,0,255));
		final byte[] data = {0, 1, 2, 0}; // 2x2: R G
		//      B R
		final ImageData img = new ImageData(2, 2, 8, palette, 1, data);
		final byte[] rgba = RGBAEncoder.encode(img);
		// Expect RGBA for each pixel
		assertArrayEquals(new byte[] {
				(byte)255,0,0,(byte)255,   0,(byte)255,0,(byte)255,
				0,0,(byte)255,(byte)255,   (byte)255,0,0,(byte)255
		}, rgba);
	}

	@Test
	public void testIndexed8BitTransparentPixel() {
		// Palette with two colors, one transparent
		final PaletteData palette = new PaletteData(new RGB(1,2,3), new RGB(4,5,6));
		final byte[] data = {0, 1, 0, 1};
		final ImageData img = new ImageData(2, 2, 8, palette, 1, data);
		img.transparentPixel = 1;
		final byte[] rgba = RGBAEncoder.encode(img);
		// Transparent pixel should have alpha 0
		assertEquals(0, rgba[7]); // pixel (0,1) transparent
		assertEquals(0, rgba[15]); // pixel (1,1) transparent
		assertEquals((byte)255, rgba[3]); // pixel (0,0) opaque
	}

	@Test
	public void testIndexed8BitAlphaData() {
		// Palette with two colors, alphaData set
		final PaletteData palette = new PaletteData(new RGB(10,20,30), new RGB(40,50,60));
		final byte[] data = {0, 1, 0, 1};
		final ImageData img = new ImageData(2, 2, 8, palette, 1, data);
		img.alphaData = new byte[] {0, (byte)128, (byte)255, (byte)64};
		final byte[] rgba = RGBAEncoder.encode(img);
		// Check alpha values for each pixel
		assertEquals(0, rgba[3]); // pixel (0,0)
		assertEquals((byte)128, rgba[7]); // pixel (1,0)
		assertEquals((byte)255, rgba[11]); // pixel (0,1)
		assertEquals((byte)64, rgba[15]); // pixel (1,1)
	}

	@Test
	public void testIndexed8BitMaskData() {
		// Palette with two colors, maskData set
		final PaletteData palette = new PaletteData(new RGB(1,2,3), new RGB(4,5,6));
		final byte[] data = {0, 1, 0, 1};
		final ImageData img = new ImageData(2, 2, 8, palette, 1, data);
		img.maskData = new byte[] {(byte)0b10100000}; // Only (0,0) and (0,1) visible
		final byte[] rgba = RGBAEncoder.encode(img);
		// Check alpha for mask
		assertEquals((byte)255, rgba[3]); // (0,0) visible
		assertEquals(0, rgba[7]);         // (1,0) masked
		assertEquals((byte)255, rgba[11]);// (0,1) visible
		assertEquals(0, rgba[15]);        // (1,1) masked
	}

	@Test
	public void testDirect24BitNoTransparency() {
		// Direct palette, 24-bit, no transparency
		final PaletteData palette = new PaletteData(0xFF0000, 0x00FF00, 0x0000FF);
		final byte[] data = new byte[4*4];
		final ImageData img = new ImageData(2, 2, 24, palette, 1, data);
		img.setPixel(0,0, 0x112233);
		img.setPixel(1,0, 0x445566);
		img.setPixel(0,1, 0x778899);
		img.setPixel(1,1, 0xAABBCC);
		final byte[] rgba = RGBAEncoder.encode(img);
		// Check RGBA values for each pixel
		assertEquals(0x11, rgba[0] & 0xFF); assertEquals(0x22, rgba[1] & 0xFF); assertEquals(0x33, rgba[2] & 0xFF);
		assertEquals(255, rgba[3] & 0xFF);
		assertEquals(0x44, rgba[4] & 0xFF); assertEquals(0x55, rgba[5] & 0xFF); assertEquals(0x66, rgba[6] & 0xFF);
		assertEquals(255, rgba[7] & 0xFF);
		assertEquals(0x77, rgba[8] & 0xFF); assertEquals(0x88, rgba[9] & 0xFF); assertEquals(0x99, rgba[10] & 0xFF);
		assertEquals(255, rgba[11] & 0xFF);
		assertEquals(0xAA, rgba[12] & 0xFF); assertEquals(0xBB, rgba[13] & 0xFF); assertEquals(0xCC, rgba[14] & 0xFF);
		assertEquals(255, rgba[15] & 0xFF);
	}

	@Test
	public void testDirect24BitAlphaData() {
		// Direct palette, 24-bit, with alphaData
		final PaletteData palette = new PaletteData(0xFF0000, 0x00FF00, 0x0000FF);
		final byte[] data = new byte[4*4];
		final ImageData img = new ImageData(2, 2, 24, palette, 1, data);
		img.setPixel(0,0, 0x123456);
		img.setPixel(1,0, 0x654321);
		img.setPixel(0,1, 0xABCDEF);
		img.setPixel(1,1, 0x0F0F0F);
		img.alphaData = new byte[] {0, (byte)128, (byte)255, (byte)64};
		final byte[] rgba = RGBAEncoder.encode(img);
		// Check alpha values
		assertEquals(0, rgba[3]);
		assertEquals((byte)128, rgba[7]);
		assertEquals((byte)255, rgba[11]);
		assertEquals((byte)64, rgba[15]);
	}

	@Test
	public void testDirect24BitMaskData() {
		// Direct palette, 24-bit, with maskData
		final PaletteData palette = new PaletteData(0xFF0000, 0x00FF00, 0x0000FF);
		final byte[] data = new byte[4*4];
		final ImageData img = new ImageData(2, 2, 24, palette, 1, data);
		img.setPixel(0,0, 0x123456);
		img.setPixel(1,0, 0x654321);
		img.setPixel(0,1, 0xABCDEF);
		img.setPixel(1,1, 0x0F0F0F);
		img.maskData = new byte[] {(byte)0b10100000};
		final byte[] rgba = RGBAEncoder.encode(img);
		// Check alpha for mask
		assertEquals((byte)255, rgba[3]); // (0,0) visible
		assertEquals(0, rgba[7]);         // (1,0) masked
		assertEquals((byte)255, rgba[11]);// (0,1) visible
		assertEquals(0, rgba[15]);        // (1,1) masked
	}

	@Test
	public void testDirect24BitGlobalAlpha() {
		// Direct palette, 24-bit, with global alpha
		final PaletteData palette = new PaletteData(0xFF0000, 0x00FF00, 0x0000FF);
		final byte[] data = new byte[4*4];
		final ImageData img = new ImageData(2, 2, 24, palette, 1, data);
		img.setPixel(0,0, 0x123456);
		img.setPixel(1,0, 0x654321);
		img.setPixel(0,1, 0xABCDEF);
		img.setPixel(1,1, 0x0F0F0F);
		img.alpha = 42;
		final byte[] rgba = RGBAEncoder.encode(img);
		// All alpha values should be 42
		for (int i = 3; i < rgba.length; i += 4) {
			assertEquals((byte)42, rgba[i]);
		}
	}

	@Test
	public void testIndexed1BitNoTransparency() {
		// 1-bit indexed palette, no transparency
		final PaletteData palette = new PaletteData(new RGB(0,0,0), new RGB(255,255,255));
		final byte[] data = {(byte)0b00000000, (byte)0b01000000}; // Row 1: 0 0, Row 2: 0 1
		final ImageData img = new ImageData(2, 2, 1, palette, 1, data);
		final byte[] rgba = RGBAEncoder.encode(img);
		// Check RGBA values for each pixel
		assertEquals((byte)0, rgba[0]); assertEquals((byte)0, rgba[1]); assertEquals((byte)0, rgba[2]);
		assertEquals((byte)255, rgba[3]);
		assertEquals((byte)0, rgba[4]); assertEquals((byte)0, rgba[5]); assertEquals((byte)0, rgba[6]);
		assertEquals((byte)255, rgba[7]);
		assertEquals((byte)0, rgba[8]); assertEquals((byte)0, rgba[9]); assertEquals((byte)0, rgba[10]);
		assertEquals((byte)255, rgba[11]);
		assertEquals((byte)255, rgba[12]); assertEquals((byte)255, rgba[13]); assertEquals((byte)255, rgba[14]);
		assertEquals((byte)255, rgba[15]);
	}

	@Test
	public void testIndexed4BitNoTransparency() {
		// 4-bit indexed palette, no transparency
		final PaletteData palette = new PaletteData(
				new RGB(10,20,30), new RGB(40,50,60), new RGB(70,80,90), new RGB(100,110,120),
				new RGB(130,140,150), new RGB(160,170,180), new RGB(190,200,210), new RGB(220,230,240),
				new RGB(250,251,252), new RGB(1,2,3), new RGB(4,5,6), new RGB(7,8,9),
				new RGB(11,12,13), new RGB(14,15,16), new RGB(17,18,19), new RGB(20,21,22)
				);
		final byte[] data = {(byte)0x12, (byte)0x34}; // 2x2: 1 2
		//      3 4
		final ImageData img = new ImageData(2, 2, 4, palette, 1, data);
		img.setPixel(0,0, 1); img.setPixel(1,0, 2); img.setPixel(0,1, 3); img.setPixel(1,1, 4);
		final byte[] rgba = RGBAEncoder.encode(img);
		// Check RGBA values for each pixel
		assertEquals((byte)40, rgba[0]); assertEquals((byte)50, rgba[1]); assertEquals((byte)60, rgba[2]);
		assertEquals((byte)255, rgba[3]);
		assertEquals((byte)70, rgba[4]); assertEquals((byte)80, rgba[5]); assertEquals((byte)90, rgba[6]);
		assertEquals((byte)255, rgba[7]);
		assertEquals((byte)100, rgba[8]); assertEquals((byte)110, rgba[9]); assertEquals((byte)120, rgba[10]);
		assertEquals((byte)255, rgba[11]);
		assertEquals((byte)130, rgba[12]); assertEquals((byte)140, rgba[13]); assertEquals((byte)150, rgba[14]);
		assertEquals((byte)255, rgba[15]);
	}

}