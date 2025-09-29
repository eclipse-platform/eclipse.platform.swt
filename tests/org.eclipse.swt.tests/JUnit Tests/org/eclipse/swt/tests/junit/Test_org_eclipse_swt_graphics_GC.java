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
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeFalse;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.lang.ref.WeakReference;
import java.util.concurrent.atomic.AtomicReference;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontMetrics;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageGcDrawer;
import org.eclipse.swt.graphics.LineAttributes;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.graphics.Transform;
import org.eclipse.swt.internal.DPIUtil;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Automated Test Suite for class org.eclipse.swt.graphics.GC
 *
 * @see org.eclipse.swt.graphics.GC
 */
@SuppressWarnings("restriction")
public class Test_org_eclipse_swt_graphics_GC {

@BeforeEach
public void setUp() {
	display = Display.getDefault();
	shell = new Shell(display);
	shell.setBounds(0,30,240,290);
	image = new Image(display, 200, 200);
	gc = new GC(image);
}

@AfterEach
public void tearDown() {
	gc.dispose();
	image.dispose();
	shell.dispose();
}

@Test
public void test_ConstructorLorg_eclipse_swt_graphics_Drawable() {
	IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> new GC(null),
			"No exception thrown for drawable == null");
	assertSWTProblem("Incorrect exception thrown for drawable == null", SWT.ERROR_NULL_ARGUMENT, e);

	IllegalArgumentException e1 = assertThrows(IllegalArgumentException.class, () -> {
		Image image = null;
		GC gc1 = null, gc2 = null;
		try {
			image = new Image(display, 10, 10);
			gc1 = new GC(image);
			gc2 = new GC(image);
		} finally {
			if (image != null)
				image.dispose();
			if (gc1 != null)
				gc1.dispose();
			if (gc2 != null)
				gc2.dispose();
		}
	}, "No exception thrown for more than one GC on one image");
	assertSWTProblem("Incorrect exception thrown for more than one GC on one image", SWT.ERROR_INVALID_ARGUMENT, e1);
}

@Test
public void test_ConstructorLorg_eclipse_swt_graphics_DrawableI() {
	IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> new GC(null, SWT.LEFT_TO_RIGHT),
			"No exception thrown for drawable == null");
	assertSWTProblem("Incorrect exception thrown for drawable == null", SWT.ERROR_NULL_ARGUMENT, e);

	IllegalArgumentException e1 = assertThrows(IllegalArgumentException.class, () -> {
		Image image = null;
		GC gc1 = null, gc2 = null;
		try {
			image = new Image(display, 10, 10);
			new GC(image, SWT.RIGHT_TO_LEFT);
			new GC(image, SWT.LEFT_TO_RIGHT);
		} finally {
			if (image != null)
				image.dispose();
			if (gc1 != null)
				gc1.dispose();
			if (gc2 != null)
				gc2.dispose();
		}
	}, "No exception thrown for more than one GC on one image");
	assertSWTProblem("Incorrect exception thrown for more than one GC on one image", SWT.ERROR_INVALID_ARGUMENT, e1);

	Canvas canvas = new Canvas(shell, SWT.NULL);
	GC testGC = new GC(canvas, SWT.RIGHT_TO_LEFT);
	testGC.dispose();
	testGC = new GC(canvas, SWT.LEFT_TO_RIGHT);
	testGC.dispose();
	canvas.dispose();
}

@Test
public void test_copyAreaIIIIII() {
	// This test verifies pixel-level color values after a copyArea() operation.
	// Such pixel-accurate checks are only reliable at 100% zoom due to fractional scaling.
	assumeTrue(DPIUtil.getDeviceZoom() == 100, "Skipping test due to non-100% zoom");

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

	int pixel = imageData.getPixel(destX + 4, destY);
	assertEquals(whiteRGB, palette.getRGB(pixel));
	pixel = imageData.getPixel(destX + 6 , destY);
	assertEquals(blueRGB, palette.getRGB(pixel));
	pixel = imageData.getPixel(destX + 10, destY);
	assertEquals(blueRGB, palette.getRGB(pixel));
	pixel = imageData.getPixel(destX + 12, destY);
	assertEquals(whiteRGB, palette.getRGB(pixel));
}

@Test
public void test_copyAreaIIIIII_overlapingSourceTarget() {
	// This test verifies pixel-level color values after a copyArea() operation.
	// Such pixel-accurate checks are only reliable at 100% zoom due to fractional scaling.
	assumeTrue(DPIUtil.getDeviceZoom() == 100, "Skipping test due to non-100% zoom");

	Color red= display.getSystemColor(SWT.COLOR_RED);
	Color blue = display.getSystemColor(SWT.COLOR_BLUE);
	RGB redRGB = getRealRGB(red);
	RGB blueRGB = getRealRGB(blue);

	gc.setBackground(red);
	gc.fillRectangle(image.getBounds());
	gc.setBackground(blue);
	gc.fillRectangle(0, 100, 200, 100);

	ImageData imageData = image.getImageData();
	PaletteData palette = imageData.palette;

	int pixel = imageData.getPixel(0, 0);
	assertEquals(redRGB, palette.getRGB(pixel));
	pixel = imageData.getPixel(0, 105);
	assertEquals(blueRGB, palette.getRGB(pixel));
	pixel = imageData.getPixel(0, 155);
	assertEquals(blueRGB, palette.getRGB(pixel));

	gc.copyArea(0, 50, 200, 100, 0, 100);

	imageData = image.getImageData();
	palette = imageData.palette;

	pixel = imageData.getPixel(0, 105);
	assertEquals(redRGB, palette.getRGB(pixel));
	pixel = imageData.getPixel(0, 145);
	assertEquals(redRGB, palette.getRGB(pixel));
	pixel = imageData.getPixel(0, 155);
	assertEquals(blueRGB, palette.getRGB(pixel));
	pixel = imageData.getPixel(0, 195);
	assertEquals(blueRGB, palette.getRGB(pixel));
}


@Test
public void test_copyAreaLorg_eclipse_swt_graphics_ImageII() {
	// This test verifies pixel-level color values after a copyArea() operation.
	// Such pixel-accurate checks are only reliable at 100% zoom due to fractional scaling.
	assumeTrue(DPIUtil.getDeviceZoom() == 100, "Skipping test due to non-100% zoom");

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

	int pixel = imageData.getPixel(4, 0);
	assertEquals(whiteRGB, palette.getRGB(pixel));
	pixel = imageData.getPixel(5, 0);
	assertEquals(blueRGB, palette.getRGB(pixel));
	pixel = imageData.getPixel(10, 0);
	assertEquals(blueRGB, palette.getRGB(pixel));
	pixel = imageData.getPixel(11, 0);
	assertEquals(whiteRGB, palette.getRGB(pixel));
	image.dispose();
}

@Test
public void test_dispose() {
	gc.dispose();
}


@Test
public void test_drawImage_nonAutoScalableGC_bug_2504() {
	Shell shell = new Shell(display);
    float targetScale = 2f;
    int srcSize = 50;
    Image image = new Image(display, srcSize, srcSize);
    GC gcSrc = new GC(image);
    gcSrc.setBackground(display.getSystemColor(SWT.COLOR_BLACK));
    gcSrc.fillRectangle(0, 0, srcSize, srcSize);
    gcSrc.setBackground(display.getSystemColor(SWT.COLOR_WHITE));
    gcSrc.fillRectangle(2, 2, srcSize - 4, srcSize - 4);
    gcSrc.dispose();

    Rectangle bounds = image.getBounds();

    Canvas canvas = new Canvas(shell, SWT.NONE) {
        @Override
        public boolean isAutoScalable() {
            return false;
        }
    };

    int canvasWidth = Math.round(bounds.width * targetScale);
    int canvasHeight = Math.round(bounds.height * targetScale);
    canvas.setSize(canvasWidth, canvasHeight);
    canvas.addPaintListener(e -> {
        e.gc.drawImage(image, 0, 0, bounds.width, bounds.height,
                       0, 0, canvasWidth, canvasHeight);
    });

    shell.open();
    while (display.readAndDispatch()) {
    }
    Image target = new Image(display, canvasWidth, canvasHeight);
    GC gcCopy = new GC(canvas);
    gcCopy.copyArea(target, 0, 0);
    gcCopy.dispose();

    ImageData data = target.getImageData();

    int bottomRightX = canvasWidth - 1;
    int bottomRightY = canvasHeight - 1;
    RGB bottomRight = data.palette.getRGB(data.getPixel(bottomRightX, bottomRightY));
    RGB black = new RGB(0, 0, 0);
    assertEquals( black, bottomRight, "Bottom-right pixel is not black! when source GC is not autoScalable");
	shell.dispose();
    target.dispose();
    image.dispose();
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
	assertThrows(IllegalArgumentException.class, () -> gc.drawImage(null, 100, 100));
	image.dispose();
	imageTransparent.dispose();
	imageAlpha.dispose();
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
	assertThrows(IllegalArgumentException.class, () -> gc.drawImage(null, 10, 5, 20, 15, 100, 120, 50, 60));
	image.dispose();
	imageAlpha.dispose();
	imageTransparent.dispose();
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
	assertNotEquals(0, (style & SWT.LEFT_TO_RIGHT));
	testGC.dispose();
	testGC = new GC(canvas);
	style = testGC.getStyle();
	assertNotEquals(0, (style & SWT.LEFT_TO_RIGHT));
	testGC.dispose();
	testGC = new GC(canvas, SWT.RIGHT_TO_LEFT);
	style = testGC.getStyle();
	assertNotEquals(0, (style & SWT.RIGHT_TO_LEFT));
	testGC.dispose();
}

@Test
public void test_hashCode() {
	assertEquals(gc.hashCode(), gc.hashCode());
	GC gc2 = new GC(shell);
	assertNotEquals(gc2.hashCode(), gc.hashCode());
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
	assertThrows(IllegalArgumentException.class, () -> gc.setBackground(null), "No exception thrown for null color");
	assertEquals(gc.getBackground(),gc.getBackground());
	color.dispose();
	assertThrows(IllegalArgumentException.class, () -> gc.setBackground(color),
			"No exception thrown for color disposed");
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
	gc.setFont(null);
	assertEquals(shell.getDisplay().getSystemFont(), gc.getFont());

	gc.setFont(shell.getDisplay().getSystemFont());
	Font font = gc.getFont();
	assertEquals(shell.getDisplay().getSystemFont(), font);
}

@Test
public void test_setForegroundLorg_eclipse_swt_graphics_Color() {
	Color color = new Color(255, 0, 0);
	gc.setForeground(color);
	assertEquals(color, gc.getForeground());
	assertThrows(IllegalArgumentException.class, () -> gc.setForeground(null), "No exception thrown for null color");
	assertEquals(gc.getForeground(), gc.getForeground());
	color.dispose();
	assertThrows(IllegalArgumentException.class, () -> gc.setForeground(color),
			"No exception thrown for color disposed");
}

@Test
public void test_setLineAttributes$I() {
	int width = 1;
	int cap = 3;
	int join = 2;
	int style = 6;
	float[] dashes = new float[] { 1.2f, 3.3f };
	float dashOffset = 3.3f;
	float miterLimit = 2.6f;
	LineAttributes passedLineAttributes = new LineAttributes(width, cap, join, style, dashes, dashOffset, miterLimit);
	gc.setLineAttributes(passedLineAttributes);
	assertEquals(width, gc.getLineWidth(), "unexpected line width");
	assertEquals(cap, gc.getLineCap(), "unexpected line cap");
	assertEquals(join, gc.getLineJoin(), "unexpected line join");
	assertEquals(style, gc.getLineStyle(), "unexpected line style");
	assertEquals(new LineAttributes(width, cap, join, style, dashes, dashOffset, miterLimit), gc.getLineAttributes(), "actual line attributes differ from the ones that have been set");
	assertEquals(width, passedLineAttributes.width, 0.0f, "setter call changed line width");

	gc.setLineAttributes(new LineAttributes(1));
	assertEquals(new LineAttributes(1), gc.getLineAttributes());
}

@Test
public void test_setLineAttributes$I_withDeviceScaling() {
	executeWithNonDefaultDeviceZoom(() -> test_setLineAttributes$I());
}

@Test
public void test_setLineStyleI() {
	gc.setLineStyle(SWT.LINE_SOLID);
	assertEquals(SWT.LINE_SOLID, gc.getLineStyle());
	gc.setLineStyle(SWT.LINE_DASH);
	assertEquals(SWT.LINE_DASH, gc.getLineStyle());
	gc.setLineStyle(SWT.LINE_DOT);
	assertEquals(SWT.LINE_DOT, gc.getLineStyle());
	gc.setLineStyle(SWT.LINE_DASHDOT);
	assertEquals(SWT.LINE_DASHDOT, gc.getLineStyle());
	gc.setLineStyle(SWT.LINE_DASHDOTDOT);
	assertEquals(SWT.LINE_DASHDOTDOT, gc.getLineStyle());
}

@Test
public void test_setLineWidthI() {
	gc.setLineWidth(10);
	assertEquals(10, gc.getLineWidth());
	gc.setLineWidth(0);
	assertEquals(0, gc.getLineWidth());
}

@Test
public void test_setLineWidthI_withDeviceScaling() {
	executeWithNonDefaultDeviceZoom(this::test_setLineWidthI);
}

@Test
public void test_setLineDash$I() {
	int[] dashes = { 5, 1, 3 };
	gc.setLineDash(dashes);
	assertArrayEquals(dashes, gc.getLineDash());
	gc.setLineDash(null);
	assertEquals(null, gc.getLineDash());
}

@Test
public void test_setLineDash$I_withDeviceScaling() {
	executeWithNonDefaultDeviceZoom(this::test_setLineDash$I);
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

@Test
public void test_bug493455_drawImageAlpha_srcPos() {
	assumeFalse(SwtTestUtil.isCocoa, "https://github.com/eclipse-platform/eclipse.platform.swt/issues/40 causes test to fail on Mac");
	RGB red = new RGB(255, 0, 0);
	RGB green = new RGB(0, 255, 0);

	ImageData initImageData = new ImageData(200, 200, 32, new PaletteData(0xff, 0xff00, 0xff0000));
	initImageData.alphaData = new byte[200 * 200];
	Image srcImage = new Image(display, initImageData);

	GC srcImageGc = new GC(srcImage);
	srcImageGc.setAdvanced(true);
	srcImageGc.setBackground(new Color(red));
	srcImageGc.fillRectangle(0, 0, 200, 200);
	srcImageGc.setBackground(new Color(green));
	srcImageGc.fillRectangle(100, 50, 100, 150);
	srcImageGc.dispose();

	gc.drawImage(srcImage, 100, 50, 100, 150, 0, 0, 100, 150);

	ImageData srcImageData = srcImage.getImageData();
	srcImage.dispose();

	ImageData testImageData = image.getImageData();

	assertNotNull(srcImageData.alphaData);

	for (int i = 0; i < 200; ++i) {
		for (int j = 0; j < 200; ++j) {
			RGB expected = (i < 100 || j < 50) ? red : green;
			RGB actual = srcImageData.palette.getRGB(srcImageData.getPixel(i, j));
			assertEquals(expected, actual);
		}
	}

	for (int i = 0; i < 100; ++i) {
		for (int j = 0; j < 150; ++j) {
			RGB rgb = testImageData.palette.getRGB(testImageData.getPixel(i, j));
			assertEquals(green, rgb);
		}
	}

}

/**
 * @see <a href="https://github.com/eclipse-platform/eclipse.platform.swt/issues/788">Issue 788</a>
 */
@Test
public void test_drawLine_noSingularitiesIn45DregreeRotation() {
	int imageSize = 3;
	int centerPixel = imageSize / 2;
	Image image = new Image(Display.getDefault(), imageSize, imageSize);
	GC gc = new GC(image);
	Transform rotation = new Transform(gc.getDevice());
	gc.getTransform(rotation);

	try {
		// Rotate 45° around image center
		rotation.translate(centerPixel, centerPixel);
		rotation.rotate(45);
		rotation.translate(- centerPixel, - centerPixel);
		gc.setTransform(rotation);
		gc.drawLine(centerPixel, centerPixel, centerPixel + 1, centerPixel);

		assertThat("line is not drawn with 45 degree rotation",
				image.getImageData().getPixel(centerPixel, centerPixel), not(is(-1)));
	} finally {
		rotation.dispose();
		gc.dispose();
		image.dispose();
	}
}

/**
 * @see <a href="https://github.com/eclipse-platform/eclipse.platform.swt/issues/1288">Issue 1288</a>
 */
@Test
public void test_bug1288_createGCFromImageFromNonDisplayThread() throws InterruptedException {
	AtomicReference<Exception> exceptionReference = new AtomicReference<>();
	Thread thread = new Thread(() -> {
		try {
			Image image = new Image(null, 100, 100);
			GC gc = new GC(image);
			gc.dispose();
			image.dispose();
		} catch(Exception e) {
			exceptionReference.set(e);
		}
	});
	thread.start();
	thread.join();
	assertNull(exceptionReference.get(), "Creating a GC from an Image without a device threw an exception");
}

// Tests that a GC instance is cleaned from memory after it was disposed.
// Primarily supposed to test that the operations (currently only implemented for Windows) do not produce any leaks.
@Test
public void test_noMemoryLeakAfterDispose() {
	GC testGC = new GC(display);
	Image image = new Image(display, 1, 1);
	testGC.setFont(display.getSystemFont());
	testGC.setClipping(new Rectangle(0, 0, 2, 2));
	testGC.drawImage(image, 0, 0);
	testGC.drawText("Test", 0, 0);
	testGC.drawLine(0, 0, 5, 5);
	WeakReference<GC> testGCReference = new WeakReference<>(testGC);
	testGC.dispose();
	testGC = null;
	System.gc();
	assertNull(testGCReference.get());
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
	ImageGcDrawer gcDrawer = (imageGc, width, height) -> {
		imageGc.setBackground(color);
		imageGc.setForeground(color);
		imageGc.fillRectangle(0, 0, width, height);
	};
	Image colorImage = new Image(display, gcDrawer, 10, 10);
	ImageData imageData = colorImage.getImageData();
	PaletteData palette = imageData.palette;
	colorImage.dispose();
	int pixel = imageData.getPixel(0, 0);
	return palette.getRGB(pixel);
}

private void executeWithNonDefaultDeviceZoom(Runnable executable) {
	int previousDeviceZoom = DPIUtil.getDeviceZoom();
	DPIUtil.setDeviceZoom(200);
	gc.dispose();
	gc = new GC(image);
	try {
		executable.run();
	} finally {
		DPIUtil.setDeviceZoom(previousDeviceZoom);
	}
}
}
