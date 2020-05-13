/*******************************************************************************
 * Copyright (c) 2000, 2016 IBM Corporation and others.
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
 *     Red Hat Inc. - Bug 462631
 *******************************************************************************/
package org.eclipse.swt.tests.junit;

import static org.eclipse.swt.tests.junit.SwtTestUtil.assertSWTProblem;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontMetrics;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.internal.DPIUtil;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Automated Test Suite for class org.eclipse.swt.graphics.GC
 *
 * @see org.eclipse.swt.graphics.GC
 */
public class Test_org_eclipse_swt_graphics_GC {

@Before
public void setUp() {
	display = Display.getDefault();
	shell = new Shell(display);
	shell.setBounds(0,30,240,290);
	image = new Image(display, 200, 200);
	gc = new GC(image);
}

@After
public void tearDown() {
	gc.dispose();
	image.dispose();
	shell.dispose();
}

@Test
public void test_ConstructorLorg_eclipse_swt_graphics_Drawable() {
	try {
		GC gc = new GC(null);
		gc.dispose();
		fail("No exception thrown for drawable == null");
	} catch (IllegalArgumentException e) {
		assertSWTProblem("Incorrect exception thrown for drawable == null", SWT.ERROR_NULL_ARGUMENT, e);
	}

	Image image = null;
	GC gc1 = null, gc2 = null;
	try {
		image = new Image(display, 10, 10);
		gc1 = new GC(image);
		gc2 = new GC(image);
		fail("No exception thrown for more than one GC on one image");
	} catch (IllegalArgumentException e) {
		assertSWTProblem("Incorrect exception thrown for more than one GC on one image", SWT.ERROR_INVALID_ARGUMENT, e);
	} finally {
		if (image != null) image.dispose();
		if (gc1 != null) gc1.dispose();
		if (gc2 != null) gc2.dispose();
	}
}

@Test
public void test_ConstructorLorg_eclipse_swt_graphics_DrawableI() {
	try {
		GC gc = new GC(null, SWT.LEFT_TO_RIGHT);
		gc.dispose();
		fail("No exception thrown for drawable == null");
	} catch (IllegalArgumentException e) {
		assertSWTProblem("Incorrect exception thrown for drawable == null", SWT.ERROR_NULL_ARGUMENT, e);
	}

	Image image = null;
	GC gc1 = null, gc2 = null;
	try {
		image = new Image(display, 10, 10);
		gc1 = new GC(image, SWT.RIGHT_TO_LEFT);
		gc2 = new GC(image, SWT.LEFT_TO_RIGHT);
		fail("No exception thrown for more than one GC on one image");
	} catch (IllegalArgumentException e) {
		assertSWTProblem("Incorrect exception thrown for more than one GC on one image", SWT.ERROR_INVALID_ARGUMENT, e);
	} finally {
		if (image != null) image.dispose();
		if (gc1 != null) gc1.dispose();
		if (gc2 != null) gc2.dispose();
	}

	Canvas canvas = new Canvas(shell, SWT.NULL);
	GC testGC = new GC(canvas, SWT.RIGHT_TO_LEFT);
	testGC.dispose();
	testGC = new GC(canvas, SWT.LEFT_TO_RIGHT);
	testGC.dispose();
	canvas.dispose();
}

@Test
public void test_copyAreaIIIIII() {
	Color white = display.getSystemColor(SWT.COLOR_WHITE);
	Color blue = display.getSystemColor(SWT.COLOR_BLUE);
	RGB whiteRGB = getRealRGB(white);
	RGB blueRGB = getRealRGB(blue);
	int width = 20;
	int height = 20;
	int destX = 10;
	int destY = 50;

	gc.setBackground(white);
	gc.fillRectangle(image.getBounds());
	gc.setBackground(blue);
	gc.fillRectangle(5, 0, 6, 1);
	gc.copyArea(0, 0, width, height, destX, destY);

	ImageData imageData = image.getImageData();
	PaletteData palette = imageData.palette;

	if (DPIUtil.getDeviceZoom() != 100) {
		//TODO Fix non integer scaling factors.
		if (SwtTestUtil.verbose) {
			System.out.println("Excluded test_copyAreaIIIIII(org.eclipse.swt.tests.junit.Test_org_eclipse_swt_graphics_GC)");
		}
		return;
	}

	int pixel = imageData.getPixel(destX + 4, destY);
	assertEquals(":a:", whiteRGB, palette.getRGB(pixel));
	pixel = imageData.getPixel(destX + 6 , destY);
	assertEquals(":b:", blueRGB, palette.getRGB(pixel));
	pixel = imageData.getPixel(destX + 10, destY);
	assertEquals(":c:", blueRGB, palette.getRGB(pixel));
	pixel = imageData.getPixel(destX + 12, destY);
	assertEquals(":d:", whiteRGB, palette.getRGB(pixel));
}

@Test
public void test_copyAreaLorg_eclipse_swt_graphics_ImageII() {
	Color white = display.getSystemColor(SWT.COLOR_WHITE);
	Color blue = display.getSystemColor(SWT.COLOR_BLUE);
	RGB whiteRGB = getRealRGB(white);
	RGB blueRGB = getRealRGB(blue);

	gc.setBackground(white);
	gc.fillRectangle(image.getBounds());
	gc.setBackground(blue);
	gc.fillRectangle(5, 0, 6, 1);
	Image image = new Image(display, 12, 12);
	gc.copyArea(image, 0, 0);
	ImageData imageData = image.getImageData();
	PaletteData palette = imageData.palette;

	if (DPIUtil.getDeviceZoom() != 100) {
		//TODO Fix non integer scaling factors.
		if (SwtTestUtil.verbose) {
			System.out.println("Excluded test_copyAreaLorg_eclipse_swt_graphics_ImageII(org.eclipse.swt.tests.junit.Test_org_eclipse_swt_graphics_GC)");
		}
		image.dispose();
		return;
	}

	int pixel = imageData.getPixel(4, 0);
	assertEquals(":a:", whiteRGB, palette.getRGB(pixel));
	pixel = imageData.getPixel(5, 0);
	assertEquals(":b:", blueRGB, palette.getRGB(pixel));
	pixel = imageData.getPixel(10, 0);
	assertEquals(":c:", blueRGB, palette.getRGB(pixel));
	pixel = imageData.getPixel(11, 0);
	assertEquals(":d:", whiteRGB, palette.getRGB(pixel));
	image.dispose();
}

@Test
public void test_dispose() {
	gc.dispose();
}

@Test
public void test_drawArcIIIIII() {
	gc.drawArc(10, 20, 50, 25, 90, 90);
}

@Test
public void test_drawFocusIIII() {
	gc.drawFocus(1, 1, 50, 25);
}

@Test
public void test_drawImageLorg_eclipse_swt_graphics_ImageII() {
	Color c1 = new Color(255, 0, 0);
	Color c2 = new Color(0, 0, 0);
	Color c3 = new Color(255, 255, 0);

	PaletteData paletteData = new PaletteData(c1.getRGB(), c2.getRGB(), c3.getRGB());
	ImageData data = new ImageData(30,30, 8, paletteData);
	for (int y = 0; y < data.height; y++) {
		for (int x = 0; x < data.width; x++) {
			if (x > y) data.setPixel(x, y, paletteData.getPixel(c1.getRGB()));
			else if (x < y) data.setPixel(x, y, paletteData.getPixel(c2.getRGB()));
			else data.setPixel(x, y, paletteData.getPixel(c3.getRGB()));
		}
	}
	Image image = new Image(display, data);
	data = image.getImageData();
	data.transparentPixel = paletteData.getPixel(c1.getRGB());
	Image imageTransparent = new Image(display, data);
	data.transparentPixel = -1;
	for (int y = 0; y < data.height; y++) {
		for (int x = 0; x < data.width; x++) {
			data.setAlpha(x, y, 127);
		}
	}
	Image imageAlpha = new Image(display, data);

	gc.drawImage(image, 100, 100);
	gc.drawImage(imageTransparent, 130, 100);
	gc.drawImage(imageAlpha, 160, 100);
	try {
		gc.drawImage(null, 100, 100);
		fail("No exception thrown");
	}
	catch (IllegalArgumentException e) {
	}
	image.dispose();
	imageTransparent.dispose();
	imageAlpha.dispose();
	c1.dispose();
	c2.dispose();
	c3.dispose();
}

@Test
public void test_drawImageLorg_eclipse_swt_graphics_ImageIIIIIIII() {
	Color c1 = new Color(255, 0, 0);
	Color c2 = new Color(0, 0, 0);
	Color c3 = new Color(255, 255, 0);

	PaletteData paletteData = new PaletteData(c1.getRGB(), c2.getRGB(), c3.getRGB());
	ImageData data = new ImageData(30,30, 8, paletteData);
	for (int y = 0; y < data.height; y++) {
		for (int x = 0; x < data.width; x++) {
			if (x > y) data.setPixel(x, y, paletteData.getPixel(c1.getRGB()));
			else if (x < y) data.setPixel(x, y, paletteData.getPixel(c2.getRGB()));
			else data.setPixel(x, y, paletteData.getPixel(c3.getRGB()));
		}
	}
	Image image = new Image(display, data);
	data = image.getImageData();
	data.transparentPixel = paletteData.getPixel(c1.getRGB());
	Image imageTransparent = new Image(display, data);
	data.transparentPixel = -1;
	for (int y = 0; y < data.height; y++) {
		for (int x = 0; x < data.width; x++) {
			data.setAlpha(x, y, 127);
		}
	}
	Image imageAlpha = new Image(display, data);

	gc.drawImage(image, 10, 5, 20, 15, 100, 120, 50, 60);
	gc.drawImage(imageTransparent, 10, 5, 20, 15, 100, 120, 10, 10);
	gc.drawImage(imageAlpha, 10, 5, 20, 15, 100, 120, 20, 15);
	try {
		gc.drawImage(null, 10, 5, 20, 15, 100, 120, 50, 60);
		fail("No exception thrown"); //should never get here
	}
	catch (IllegalArgumentException e) {
	}
	image.dispose();
	imageAlpha.dispose();
	imageTransparent.dispose();
	c1.dispose();
	c2.dispose();
	c3.dispose();
}

@Test
public void test_drawLineIIII() {
	gc.drawLine(0,0,0,20);
}

@Test
public void test_drawOvalIIII() {
	gc.drawOval(10, 0, 20, 30);
}

@Test
public void test_drawPointII() {
	gc.drawPoint(10, 10);
}

@Test
public void test_drawPolygon$I() {
	gc.drawPolygon(new int[] {0,0, 5,10, 0,20});
	gc.drawPolygon(new int[] {0,0});
}

@Test
public void test_drawPolyline$I() {
	gc.drawPolyline(new int[] {0,0, 5,10, 0,20});
	gc.drawPolyline(new int[] {0,0});
}

@Test
public void test_drawRectangleIIII() {
	gc.drawRectangle(10, 0, 20, 30);
	gc.drawRectangle(0, 0, 0, 0);
}

@Test
public void test_drawRectangleLorg_eclipse_swt_graphics_Rectangle() {
	gc.drawRectangle(new Rectangle(10, 0, 20, 30));
	gc.drawRectangle(new Rectangle(0, 0, 0, 0));
}

@Test
public void test_drawRoundRectangleIIIIII() {
	gc.drawRoundRectangle(10, 0, 20, 30, 3, 3);
	gc.drawRoundRectangle(0, 0, 0, 0, 0, 0);
}

@Test
public void test_drawStringLjava_lang_StringII() {
	gc.drawString("test", 5, 5);
}

@Test
public void test_drawStringLjava_lang_StringIIZ() {
	gc.drawString("test", 5, 5, true);
	gc.drawString("test", 5, 5, false);
}

@Test
public void test_drawTextLjava_lang_StringII() {
	gc.drawText("test", 5, 5);
	gc.drawText("", 0, 0);
}

@Test
public void test_drawTextLjava_lang_StringIII() {
	gc.drawText("abc", 5, 5, 0);
	gc.drawText("abc", 5, 5, SWT.DRAW_TRANSPARENT);
	gc.drawText("abc", 5, 5, SWT.DRAW_DELIMITER);
	gc.drawText("abc", 5, 5, SWT.DRAW_MNEMONIC);
	gc.drawText("abc", 5, 5, SWT.DRAW_TAB);
	gc.drawText("", 0, 0, 0);
	gc.drawText("", 0, 0, SWT.DRAW_TRANSPARENT);
	gc.drawText("", 0, 0, SWT.DRAW_DELIMITER);
	gc.drawText("", 0, 0, SWT.DRAW_MNEMONIC);
	gc.drawText("", 0, 0, SWT.DRAW_TAB);
	gc.drawText("\t\r\na&bc&", 5, 5, 0);
	gc.drawText("\t\r\na&bc&", 5, 5, SWT.DRAW_TRANSPARENT);
	gc.drawText("\t\r\na&bc&", 5, 5, SWT.DRAW_DELIMITER);
	gc.drawText("\t\r\na&bc&", 5, 5, SWT.DRAW_MNEMONIC);
	gc.drawText("\t\r\na&bc&", 5, 5, SWT.DRAW_TAB);
	gc.drawText("\r", 5, 5, SWT.DRAW_DELIMITER);
	gc.drawText("\n", 5, 5, SWT.DRAW_DELIMITER);
	gc.drawText("&", 5, 5, SWT.DRAW_MNEMONIC);
	gc.drawText("\t", 5, 5, SWT.DRAW_TAB);
}

@Test
public void test_drawTextLjava_lang_StringIIZ() {
	gc.drawText("abc", 5, 5, true);
	gc.drawText("abc", 5, 5, false);
	gc.drawText("", 0, 0, true);
	gc.drawText("", 0, 0, false);
}

@Test
public void test_equalsLjava_lang_Object() {
	assertTrue(gc.equals(gc));
	Canvas canvas = new Canvas(shell, SWT.NULL);
	GC testGC = new GC(canvas);
	assertFalse(testGC.equals(gc));
	testGC.dispose();
}

@Test
public void test_fillArcIIIIII() {
	gc.fillArc(10, 20, 50, 25, 90, 90);
	gc.fillArc(10, 20, 50, 25, -10, -10);
}

@Test
public void test_fillGradientRectangleIIIIZ() {
	gc.fillGradientRectangle(10, 0, 20, 30, true);
	gc.fillGradientRectangle(0, 0, 0, 0, true);
	gc.fillGradientRectangle(10, 0, 20, 30, false);
	gc.fillGradientRectangle(0, 0, 0, 0, false);
}

@Test
public void test_fillOvalIIII() {
	gc.fillOval(10, 0, 20, 30);
	gc.fillOval(-1, -1, -1, -1);
}

@Test
public void test_fillPolygon$I() {
	gc.fillPolygon(new int[] {0,0, 5,10, 0,20});
	gc.fillPolygon(new int[] {0,0});
	gc.fillPolygon(new int[] {-1, -1});
}

@Test
public void test_fillRectangleIIII() {
	gc.fillRectangle(new Rectangle(10, 0, 20, 30));
	gc.fillRectangle(new Rectangle(0, 0, 0, 0));
	gc.fillRectangle(new Rectangle(-1, -1, -1, -1));
}

@Test
public void test_fillRectangleLorg_eclipse_swt_graphics_Rectangle() {
	gc.fillRectangle(10, 0, 20, 30);
	gc.fillRectangle(0, 0, 0, 0);
}

@Test
public void test_fillRoundRectangleIIIIII() {
	gc.fillRoundRectangle(10, 0, 20, 30, 3, 3);
	gc.fillRoundRectangle(0, 0, 0, 0, 0, 0);
	gc.fillRoundRectangle(10, 0, 20, 30, -10, -10);
}

@Test
public void test_getAdvanceWidthC() {
	int w = gc.getAdvanceWidth('a');
	assertTrue(w > 0);
}

@Test
public void test_getCharWidthC() {
	int w = gc.getCharWidth('a');
	assertTrue(w > 0);
}

@Test
public void test_getFontMetrics() {
	FontMetrics fm = gc.getFontMetrics();
	assertTrue(fm.getHeight() > 0);
}

@Test
public void test_getStyle() {
	Canvas canvas = new Canvas(shell, SWT.NULL);
	GC testGC = new GC(canvas, SWT.LEFT_TO_RIGHT);
	int style = testGC.getStyle();
	assertTrue((style & SWT.LEFT_TO_RIGHT) != 0);
	testGC.dispose();
	testGC = new GC(canvas);
	style = testGC.getStyle();
	assertTrue((style & SWT.LEFT_TO_RIGHT) != 0);
	testGC.dispose();
	testGC = new GC(canvas, SWT.RIGHT_TO_LEFT);
	style = testGC.getStyle();
	assertTrue((style &  SWT.RIGHT_TO_LEFT) != 0);
	testGC.dispose();
}

@Test
public void test_hashCode() {
	assertTrue(gc.hashCode() == gc.hashCode());
	GC gc2 = new GC(shell);
	assertFalse(gc.hashCode() == gc2.hashCode());
	gc2.dispose();
}

@Test
public void test_isClipped() {
	assertFalse(gc.isClipped());
	gc.setClipping(5,10,15,20);
	assertTrue(gc.isClipped());
}

@Test
public void test_isDisposed() {
	assertFalse(gc.isDisposed());
	gc.dispose();
	assertTrue(gc.isDisposed());
}

@Test
public void test_setBackgroundLorg_eclipse_swt_graphics_Color() {
	Color color = new Color(255, 0, 0);
	gc.setBackground(color);
	assertEquals(color, gc.getBackground());
	try {
		gc.setBackground(null);
		fail("No exception thrown for null color");
	} catch (IllegalArgumentException e) {
	}
	assertEquals(gc.getBackground(),gc.getBackground());
	color.dispose();
	try {
		gc.setBackground(color);
		fail("No exception thrown for color disposed");
	} catch (IllegalArgumentException e) {
	}
}

@Test
public void test_setClippingIIII() {
	// intermittently fails on XP for reasons unknown, comment out the test case
	// until the problem is figured out
//	Canvas canvas = new Canvas(shell, SWT.BORDER);
//	shell.setSize(110,110);
//	canvas.setSize(100,100);
//	shell.open();
//	GC testGc = new GC(canvas);
//	testGc.setClipping(0,5,10,20);
//	Rectangle rect = testGc.getClipping();
//	assertTrue(rect.x == 0);
//	assertTrue(rect.y == 5);
//	assertTrue(rect.width == 10);
//	assertTrue(rect.height == 20);
//	testGc.dispose();
//	canvas.dispose();
}

@Test
public void test_setClippingLorg_eclipse_swt_graphics_Rectangle() {
	// intermittently fails on XP for reasons unknown, comment out the test case
	// until the problem is figured out
//	Canvas canvas = new Canvas(shell, SWT.BORDER);
//	shell.setSize(110,110);
//	canvas.setSize(100,100);
//	shell.open();
//	GC testGc = new GC(canvas);
//	testGc.setClipping(new Rectangle(0,5,10,20));
//	Rectangle rect = testGc.getClipping();
//	assertTrue(rect.x == 0);
//	assertTrue(rect.y == 5);
//	assertTrue(rect.width == 10);
//	assertTrue(rect.height == 20);
//	testGc.dispose();
//	canvas.dispose();
}

@Test
public void test_setFontLorg_eclipse_swt_graphics_Font() {
	gc.setFont(shell.getDisplay().getSystemFont());
	Font font = gc.getFont();
	assertTrue(font.equals(shell.getDisplay().getSystemFont()));
}

@Test
public void test_setForegroundLorg_eclipse_swt_graphics_Color() {
	Color color = new Color(255, 0, 0);
	gc.setForeground(color);
	assertEquals(color, gc.getForeground());
	try {
		gc.setForeground(null);
		fail("No exception thrown for null color");
	} catch (IllegalArgumentException e) {
	}
	assertEquals(gc.getForeground(),gc.getForeground());
	color.dispose();
	try {
		gc.setForeground(color);
		fail("No exception thrown for color disposed");
	} catch (IllegalArgumentException e) {
	}
}

@Test
public void test_setLineStyleI() {
	gc.setLineStyle(SWT.LINE_SOLID);
	assertTrue(gc.getLineStyle() == SWT.LINE_SOLID);
	gc.setLineStyle(SWT.LINE_DASH);
	assertTrue(gc.getLineStyle() == SWT.LINE_DASH);
	gc.setLineStyle(SWT.LINE_DOT);
	assertTrue(gc.getLineStyle() == SWT.LINE_DOT);
	gc.setLineStyle(SWT.LINE_DASHDOT);
	assertTrue(gc.getLineStyle() == SWT.LINE_DASHDOT);
	gc.setLineStyle(SWT.LINE_DASHDOTDOT);
	assertTrue(gc.getLineStyle() == SWT.LINE_DASHDOTDOT);
}

@Test
public void test_setLineWidthI() {
	gc.setLineWidth(10);
	assertTrue(gc.getLineWidth() == 10);
	gc.setLineWidth(0);
	assertTrue(gc.getLineWidth() == 0);
}

@Test
public void test_setXORModeZ() {
	gc.setXORMode(true);
	assertTrue(gc.getXORMode());
	gc.setXORMode(false);
	assertFalse(gc.getXORMode());
}

@Test
public void test_stringExtentLjava_lang_String() {
	Point pt = gc.stringExtent("abc");
	assertTrue(pt.x > 0);
	assertTrue(pt.y > 0);
}

@Test
public void test_textExtentLjava_lang_String() {
	Point pt = gc.textExtent("abc");
	assertTrue(pt.x > 0);
	assertTrue(pt.y > 0);
}

@Test
public void test_textExtentLjava_lang_StringI() {
	Point pt = gc.textExtent("abc", 0);
	assertTrue(pt.x > 0);
	assertTrue(pt.y > 0);
}

@Test
public void test_toString() {
	String s = gc.toString();
	assertNotNull(s);
	assertTrue(s.length() > 0);
}

/* custom */
Display display;
Shell shell;
Image image;
GC gc;

/**
 * Return the actual RGB value used for rendering for the given Color.
 * This may be different from the Color's RGB value on lower-color displays
 * (16bpp or less).
 */
RGB getRealRGB(Color color) {
	Image colorImage = new Image(display, 10, 10);
	GC imageGc = new GC(colorImage);
	ImageData imageData;
	PaletteData palette;
	int pixel;

	imageGc.setBackground(color);
	imageGc.setForeground(color);
	imageGc.fillRectangle(0, 0, 10, 10);
	imageData = colorImage.getImageData();
	palette = imageData.palette;
	imageGc.dispose();
	colorImage.dispose();
	pixel = imageData.getPixel(0, 0);
	return palette.getRGB(pixel);
}
}
