/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tests.junit.performance;

import junit.framework.*;
import junit.textui.*;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.printing.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.test.performance.PerformanceMeter;

/**
 * Automated Performance Test Suite for class org.eclipse.swt.graphics.GC
 *
 * @see org.eclipse.swt.graphics.GC
 */
public class Test_org_eclipse_swt_graphics_GC extends SwtPerformanceTestCase {
	static final int COUNT = 1000;

public Test_org_eclipse_swt_graphics_GC(String name) {
	super(name);
}

public static void main(String[] args) {
	TestRunner.run(suite());
}

protected void setUp() throws Exception {
	super.setUp();
	display = Display.getDefault();
	shell = new Shell(display);
	shell.setBounds(0,30,240,290);
	image = new Image(display, 200, 200);
	gc = new GC(image);
}

protected void tearDown() throws Exception {
	super.tearDown();
	gc.dispose();
	image.dispose();
	shell.dispose();
}
public void test_ConstructorLorg_eclipse_swt_graphics_Drawable() {
	Image[] images = new Image [COUNT];
	for (int i = 0; i < COUNT; i++) {
		images[i] = new Image(display, 10, 10);		
	}
	GC[] gcs = new GC[COUNT];
	
	PerformanceMeter meter = createMeter("image");
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		gcs[i] = new GC(images[i]);
	}
	meter.stop();

	for (int i = 0; i < COUNT; i++) {
		gcs[i].dispose();
		images[i].dispose();
	}
	
	disposeMeter(meter);

	final int count = COUNT % 100;
	
	Class printerClass = null;
	try {
		printerClass = Class.forName("org.eclipse.swt.printing.Printer");
	} catch (ClassNotFoundException e) {
		// Printer class not present (eSWT). Skip test.
		return;
	}
	Object[] printers = new Object[count];
	try {
		for (int i = 0; i < count; i++) {
			printers[i] = printerClass.newInstance();
		}
	} catch (InstantiationException e1) {
		return;
	} catch (IllegalAccessException e1) {
		return;
	}

	meter = createMeter("printer");
	meter.start();
	for (int i = 0; i < count; i++) {
		gcs[i] = new GC((Printer) printers[i]);
	}
	meter.stop();

	for (int i = 0; i < count; i++) {
		gcs[i].dispose();
	}

	disposeMeter(meter);
}

public void test_ConstructorLorg_eclipse_swt_graphics_DrawableI() {
	Image[] images = new Image[COUNT];
	for (int i = 0; i < COUNT; i++) {
		images[i] = new Image(display, 10, 10);
	}
	GC[] gcs = new GC[COUNT];
	
	PerformanceMeter meter = createMeter("image");
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		gcs[i] = new GC(images[i], SWT.RIGHT_TO_LEFT);
	}
	meter.stop();

	for (int i = 0; i < COUNT; i++) {
		gcs[i].dispose();
		images[i].dispose();
	}
	
	disposeMeter(meter);

	final int count = COUNT / 100;
	Class printerClass = null;
	try {
		printerClass = Class.forName("org.eclipse.swt.printing.Printer");
	} catch (ClassNotFoundException e) {
		// Printer class not present (eSWT). Skip test.
		return;
	}
	Object[] printers = new Object[count];
	try {
		for (int i = 0; i < count; i++) {
			printers[i] = printerClass.newInstance();
		}
	} catch (InstantiationException e1) {
		return;
	} catch (IllegalAccessException e1) {
		return;
	}

	meter = createMeter("printer");
	meter.start();
	for (int i = 0; i < count; i++) {
		gcs[i] = new GC((Printer) printers[i], SWT.RIGHT_TO_LEFT);
	}
	meter.stop();

	for (int i = 0; i < count; i++) {
		gcs[i].dispose();
	}

	disposeMeter(meter);
}

public void test_copyAreaIIIIII() {
	gc.setBackground(display.getSystemColor(SWT.COLOR_WHITE));
	gc.setForeground(display.getSystemColor(SWT.COLOR_WHITE));
	gc.fillRectangle(image.getBounds());
	gc.setForeground(display.getSystemColor(SWT.COLOR_BLUE));
	gc.drawLine(5, 5, 10, 10);
	gc.drawLine(5, 10, 10, 5);
	Rectangle imageBounds = image.getBounds();
	
	PerformanceMeter meter = createMeter();
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		int pos = i % imageBounds.width;
		gc.copyArea(pos, pos, 2, 2, pos+2, pos);
	}
	meter.stop();
	
	disposeMeter(meter);
}

public void test_copyAreaLorg_eclipse_swt_graphics_ImageII() {
	gc.setBackground(display.getSystemColor(SWT.COLOR_WHITE));
	gc.setForeground(display.getSystemColor(SWT.COLOR_WHITE));
	gc.fillRectangle(image.getBounds());
	gc.setForeground(display.getSystemColor(SWT.COLOR_BLUE));
	gc.drawLine(5, 5, 10, 10);
	gc.drawLine(5, 10, 10, 5);
	
	Image[] images = new Image[COUNT];
	for (int i = 0; i < COUNT; i++) {
		images[i] = new Image(display, 15, 15);
	}

	PerformanceMeter meter = createMeter();
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		gc.copyArea(images[i], 0, 0);
	}
	meter.stop();

	for (int i = 0; i < COUNT; i++) {
		images[i].dispose();
	}

	disposeMeter(meter);
}

public void test_dispose() {
	Image[] images = new Image [COUNT];
	for (int i = 0; i < COUNT; i++) {
		images[i] = new Image(display, 10, 10); 
	}
	GC[] gcs = new GC [COUNT];
	for (int i = 0; i < COUNT; i++) {
		gcs[i] = new GC(images[i]);
	}

	PerformanceMeter meter = createMeter("not disposed");
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		gcs[i].dispose();	// dispose
	}
	meter.stop();

    for (int i = 0; i < COUNT; i++) {
    	images[i].dispose();
    }

    disposeMeter(meter);

	meter = createMeter("disposed");
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		gcs[i].dispose();	// dispose disposed
	}
	meter.stop();
	
    disposeMeter(meter);
}

public void test_drawArcIIIIII() {
	PerformanceMeter meter = createMeter();
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		gc.drawArc(10, 20, 50, 25, 90, 90);
	}
	meter.stop();
	
	disposeMeter(meter);
}

public void test_drawFocusIIII() {
	PerformanceMeter meter = createMeter();
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		gc.drawFocus(1, 1, 50, 25);
	}
	meter.stop();
	
	disposeMeter(meter);
}

public void test_drawImageLorg_eclipse_swt_graphics_ImageII() {
	Color red = display.getSystemColor(SWT.COLOR_RED);
	Color black = display.getSystemColor(SWT.COLOR_BLACK);
	Color green = display.getSystemColor(SWT.COLOR_GREEN);
	PaletteData paletteData = new PaletteData(new RGB[] {red.getRGB(), black.getRGB(), green.getRGB()});
	ImageData data = new ImageData(30,30, 8, paletteData);
	for (int y = 0; y < data.height; y++) {
		for (int x = 0; x < data.width; x++) {
			if (x > y) data.setPixel(x, y, paletteData.getPixel(red.getRGB()));
			else if (x < y) data.setPixel(x, y, paletteData.getPixel(black.getRGB()));
			else data.setPixel(x, y, paletteData.getPixel(green.getRGB()));
		}
	}
	Image imageNormal = new Image(display, data);
	data = imageNormal.getImageData();
	data.transparentPixel = paletteData.getPixel(red.getRGB());
	Image imageTransparent = new Image(display, data);
	data.transparentPixel = -1;
	for (int y = 0; y < data.height; y++) {
		for (int x = 0; x < data.width; x++) {
			data.setAlpha(x, y, 127);
		}
	}		
	Image imageAlpha = new Image(display, data);
	gc.drawImage(imageNormal, 100, 100);
	gc.drawImage(imageTransparent, 130, 100);
	gc.drawImage(imageAlpha, 160, 100);
	
	try {
		Rectangle bounds = image.getBounds();
		PerformanceMeter meter = createMeter("normal");
		meter.start();
		for (int i = 0; i < COUNT; i++) {
			int pos = i % bounds.width;
			gc.drawImage(imageNormal, pos, pos);	// normal image
		}
		meter.stop();
		
		disposeMeter(meter);
	
		meter = createMeter("transparent");
		meter.start();
		for (int i = 0; i < COUNT; i++) {
			int pos = i % bounds.width;
			gc.drawImage(imageTransparent, pos, pos);	// transparent image
		}
		meter.stop();
		
		disposeMeter(meter);
	
		meter = createMeter("alpha");
		meter.start();
		for (int i = 0; i < COUNT; i++) {
			int pos = i % bounds.width;
			gc.drawImage(imageAlpha, pos, pos);	// alpha image
		}
		meter.stop();
		
		disposeMeter(meter);
	} finally {
		imageNormal.dispose();
		imageTransparent.dispose();
		imageAlpha.dispose();
	}
}

public void test_drawImageLorg_eclipse_swt_graphics_ImageIIIIIIII() {
//	Color c1 = new Color(display, 255, 0, 0);
//	Color c2 = new Color(display, 0, 0, 0);
//	Color c3 = new Color(display, 255, 255, 0);
//	
//	PaletteData paletteData = new PaletteData(new RGB[] {c1.getRGB(), c2.getRGB(), c3.getRGB()});
//	ImageData data = new ImageData(30,30, 8, paletteData);
//	for (int y = 0; y < data.height; y++) {
//		for (int x = 0; x < data.width; x++) {
//			if (x > y) data.setPixel(x, y, paletteData.getPixel(c1.getRGB()));
//			else if (x < y) data.setPixel(x, y, paletteData.getPixel(c2.getRGB()));
//			else data.setPixel(x, y, paletteData.getPixel(c3.getRGB()));
//		}
//	}
//	Image image = new Image(display, data);
//	data = image.getImageData();
//	data.transparentPixel = paletteData.getPixel(c1.getRGB());
//	Image imageTransparent = new Image(display, data);
//	data.transparentPixel = -1;
//	for (int y = 0; y < data.height; y++) {
//		for (int x = 0; x < data.width; x++) {
//			data.setAlpha(x, y, 127);
//		}
//	}		
//	Image imageAlpha = new Image(display, data);
//							
//	gc.drawImage(image, 10, 5, 20, 15, 100, 120, 50, 60);
//	gc.drawImage(imageTransparent, 10, 5, 20, 15, 100, 120, 10, 10);
//	gc.drawImage(imageAlpha, 10, 5, 20, 15, 100, 120, 20, 15);
//	try {
//		gc.drawImage(null, 10, 5, 20, 15, 100, 120, 50, 60);
//		fail("No exception thrown"); //should never get here
//	}
//	catch (IllegalArgumentException e) {
//	}
//	image.dispose();
//	imageAlpha.dispose();
//	imageTransparent.dispose();
}

public void test_drawLineIIII() {
	gc.drawLine(0,0,0,20);
}

public void test_drawOvalIIII() {
	gc.drawOval(10, 0, 20, 30);				
}

public void test_drawPointII() {
	gc.drawPoint(10, 10);
}

public void test_drawPolygon$I() {
	gc.drawPolygon(new int[] {0,0, 5,10, 0,20});				
	gc.drawPolygon(new int[] {0,0});				
}

public void test_drawPolyline$I() {
	gc.drawPolyline(new int[] {0,0, 5,10, 0,20});				
	gc.drawPolyline(new int[] {0,0});				
}

public void test_drawRectangleIIII() {
	gc.drawRectangle(10, 0, 20, 30);				
	gc.drawRectangle(0, 0, 0, 0);				
}

public void test_drawRectangleLorg_eclipse_swt_graphics_Rectangle() {
	gc.drawRectangle(new Rectangle(10, 0, 20, 30));				
	gc.drawRectangle(new Rectangle(0, 0, 0, 0));				
}

public void test_drawRoundRectangleIIIIII() {
	gc.drawRoundRectangle(10, 0, 20, 30, 3, 3);				
	gc.drawRoundRectangle(0, 0, 0, 0, 0, 0);				
}

public void test_drawStringLjava_lang_StringII() {
	gc.drawString("test", 5, 5);				
}

public void test_drawStringLjava_lang_StringIIZ() {
	gc.drawString("test", 5, 5, true);				
	gc.drawString("test", 5, 5, false);				
}

public void test_drawTextLjava_lang_StringII() {
	gc.drawText("test", 5, 5);				
	gc.drawText("", 0, 0);				
}

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

public void test_drawTextLjava_lang_StringIIZ() {
	gc.drawText("abc", 5, 5, true);				
	gc.drawText("abc", 5, 5, false);				
	gc.drawText("", 0, 0, true);				
	gc.drawText("", 0, 0, false);				
}

public void test_equalsLjava_lang_Object() {
	assertTrue(gc.equals(gc));
	Canvas canvas = new Canvas(shell, SWT.NULL);
	GC testGC = new GC(canvas);
	assertFalse(testGC.equals(gc));
	testGC.dispose();
}

public void test_fillArcIIIIII() {
	gc.fillArc(10, 20, 50, 25, 90, 90);				
	gc.fillArc(10, 20, 50, 25, -10, -10);				
}

public void test_fillGradientRectangleIIIIZ() {
	gc.fillGradientRectangle(10, 0, 20, 30, true);				
	gc.fillGradientRectangle(0, 0, 0, 0, true);				
	gc.fillGradientRectangle(10, 0, 20, 30, false);				
	gc.fillGradientRectangle(0, 0, 0, 0, false);				
}

public void test_fillOvalIIII() {
	gc.fillOval(10, 0, 20, 30);				
	gc.fillOval(-1, -1, -1, -1);				
}

public void test_fillPolygon$I() {
	gc.fillPolygon(new int[] {0,0, 5,10, 0,20});				
	gc.fillPolygon(new int[] {0,0});				
	gc.fillPolygon(new int[] {-1, -1});				
}

public void test_fillRectangleIIII() {
	gc.fillRectangle(new Rectangle(10, 0, 20, 30));				
	gc.fillRectangle(new Rectangle(0, 0, 0, 0));				
	gc.fillRectangle(new Rectangle(-1, -1, -1, -1));				
}

public void test_fillRectangleLorg_eclipse_swt_graphics_Rectangle() {
	gc.fillRectangle(10, 0, 20, 30);				
	gc.fillRectangle(0, 0, 0, 0);				
}

public void test_fillRoundRectangleIIIIII() {
	gc.fillRoundRectangle(10, 0, 20, 30, 3, 3);				
	gc.fillRoundRectangle(0, 0, 0, 0, 0, 0);				
	gc.fillRoundRectangle(10, 0, 20, 30, -10, -10);				
}

public void test_getAdvanceWidthC() {
	int w = gc.getAdvanceWidth('a');
	assertTrue(w > 0);
}

public void test_getBackground() {
	// tested in setBackground method
}

public void test_getCharWidthC() {
	int w = gc.getCharWidth('a');
	assertTrue(w > 0);
}

public void test_getClipping() {
	// tested in setClipping methods
}

public void test_getClippingLorg_eclipse_swt_graphics_Region() {
	
}

public void test_getFont() {
	// tested in setFont method
}

public void test_getFontMetrics() {
	FontMetrics fm = gc.getFontMetrics();
	assertTrue(fm.getHeight() > 0);
}

public void test_getForeground() {
	// tested in setForeground method
}

public void test_getLineStyle() {
	// tested in setLineStyle method
}

public void test_getLineWidth() {
	// tested in setLineWidth method
}

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

public void test_getXORMode() {
	// tested in setXORMode method
}

public void test_hashCode() {
	assertTrue(gc.hashCode() == gc.hashCode());
	GC gc2 = new GC(shell);
	assertFalse(gc.hashCode() == gc2.hashCode());
	gc2.dispose();
}

public void test_isClipped() {
	assertFalse(gc.isClipped());
	gc.setClipping(5,10,15,20);
	assertTrue(gc.isClipped());
}

public void test_isDisposed() {
	assertFalse(gc.isDisposed());
	gc.dispose();
	assertTrue(gc.isDisposed());
}

public void test_setBackgroundLorg_eclipse_swt_graphics_Color() {
	Color color = new Color(shell.getDisplay(), 255, 0, 0);
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

public void test_setClippingLorg_eclipse_swt_graphics_Region() {
	
}

public void test_setFontLorg_eclipse_swt_graphics_Font() {
	gc.setFont(shell.getDisplay().getSystemFont());
	Font font = gc.getFont();
	assertTrue(font.equals(shell.getDisplay().getSystemFont()));
}

public void test_setForegroundLorg_eclipse_swt_graphics_Color() {
	Color color = new Color(shell.getDisplay(), 255, 0, 0);
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

public void test_setLineWidthI() {
	gc.setLineWidth(10);
	assertTrue(gc.getLineWidth() == 10);
	gc.setLineWidth(0);
	assertTrue(gc.getLineWidth() == 0);
}

public void test_setXORModeZ() {
	gc.setXORMode(true);
	assertTrue(gc.getXORMode());
	gc.setXORMode(false);
	assertFalse(gc.getXORMode());
}

public void test_stringExtentLjava_lang_String() {
	Point pt = gc.stringExtent("abc");
	assertTrue(pt.x > 0);
	assertTrue(pt.y > 0);
}

public void test_textExtentLjava_lang_String() {
	Point pt = gc.textExtent("abc");
	assertTrue(pt.x > 0);
	assertTrue(pt.y > 0);
}

public void test_textExtentLjava_lang_StringI() {
	Point pt = gc.textExtent("abc", 0);
	assertTrue(pt.x > 0);
	assertTrue(pt.y > 0);
}

public void test_toString() {
	String s = gc.toString();
	assertNotNull(s);
	assertTrue(s.length() > 0);
}

public void test_win32_newILorg_eclipse_swt_graphics_GCData() {
	// do not test - Windows only
}

public void test_win32_newLorg_eclipse_swt_graphics_DrawableLorg_eclipse_swt_graphics_GCData() {
	// do not test - Windows only
}

public static Test suite() {
	TestSuite suite = new TestSuite();
	java.util.Vector methodNames = methodNames();
	java.util.Enumeration e = methodNames.elements();
	while (e.hasMoreElements()) {
		suite.addTest(new Test_org_eclipse_swt_graphics_GC((String)e.nextElement()));
	}
	return suite;
}
public static java.util.Vector methodNames() {
	java.util.Vector methodNames = new java.util.Vector();
	methodNames.addElement("test_ConstructorLorg_eclipse_swt_graphics_Drawable");
	methodNames.addElement("test_ConstructorLorg_eclipse_swt_graphics_DrawableI");
	methodNames.addElement("test_copyAreaIIIIII");
	methodNames.addElement("test_copyAreaLorg_eclipse_swt_graphics_ImageII");
	methodNames.addElement("test_dispose");
	methodNames.addElement("test_drawArcIIIIII");
	methodNames.addElement("test_drawFocusIIII");
	methodNames.addElement("test_drawImageLorg_eclipse_swt_graphics_ImageII");
	methodNames.addElement("test_drawImageLorg_eclipse_swt_graphics_ImageIIIIIIII");
	methodNames.addElement("test_drawLineIIII");
	methodNames.addElement("test_drawOvalIIII");
	methodNames.addElement("test_drawPointII");
	methodNames.addElement("test_drawPolygon$I");
	methodNames.addElement("test_drawPolyline$I");
	methodNames.addElement("test_drawRectangleIIII");
	methodNames.addElement("test_drawRectangleLorg_eclipse_swt_graphics_Rectangle");
	methodNames.addElement("test_drawRoundRectangleIIIIII");
	methodNames.addElement("test_drawStringLjava_lang_StringII");
	methodNames.addElement("test_drawStringLjava_lang_StringIIZ");
	methodNames.addElement("test_drawTextLjava_lang_StringII");
	methodNames.addElement("test_drawTextLjava_lang_StringIII");
	methodNames.addElement("test_drawTextLjava_lang_StringIIZ");
	methodNames.addElement("test_equalsLjava_lang_Object");
	methodNames.addElement("test_fillArcIIIIII");
	methodNames.addElement("test_fillGradientRectangleIIIIZ");
	methodNames.addElement("test_fillOvalIIII");
	methodNames.addElement("test_fillPolygon$I");
	methodNames.addElement("test_fillRectangleIIII");
	methodNames.addElement("test_fillRectangleLorg_eclipse_swt_graphics_Rectangle");
	methodNames.addElement("test_fillRoundRectangleIIIIII");
	methodNames.addElement("test_getAdvanceWidthC");
	methodNames.addElement("test_getBackground");
	methodNames.addElement("test_getCharWidthC");
	methodNames.addElement("test_getClipping");
	methodNames.addElement("test_getClippingLorg_eclipse_swt_graphics_Region");
	methodNames.addElement("test_getFont");
	methodNames.addElement("test_getFontMetrics");
	methodNames.addElement("test_getForeground");
	methodNames.addElement("test_getLineStyle");
	methodNames.addElement("test_getLineWidth");
	methodNames.addElement("test_getStyle");
	methodNames.addElement("test_getXORMode");
	methodNames.addElement("test_hashCode");
	methodNames.addElement("test_isClipped");
	methodNames.addElement("test_isDisposed");
	methodNames.addElement("test_setBackgroundLorg_eclipse_swt_graphics_Color");
	methodNames.addElement("test_setClippingIIII");
	methodNames.addElement("test_setClippingLorg_eclipse_swt_graphics_Rectangle");
	methodNames.addElement("test_setClippingLorg_eclipse_swt_graphics_Region");
	methodNames.addElement("test_setFontLorg_eclipse_swt_graphics_Font");
	methodNames.addElement("test_setForegroundLorg_eclipse_swt_graphics_Color");
	methodNames.addElement("test_setLineStyleI");
	methodNames.addElement("test_setLineWidthI");
	methodNames.addElement("test_setXORModeZ");
	methodNames.addElement("test_stringExtentLjava_lang_String");
	methodNames.addElement("test_textExtentLjava_lang_String");
	methodNames.addElement("test_textExtentLjava_lang_StringI");
	methodNames.addElement("test_toString");
	methodNames.addElement("test_win32_newILorg_eclipse_swt_graphics_GCData");
	methodNames.addElement("test_win32_newLorg_eclipse_swt_graphics_DrawableLorg_eclipse_swt_graphics_GCData");
	return methodNames;
}
protected void runTest() throws Throwable {
	if (getName().equals("test_ConstructorLorg_eclipse_swt_graphics_Drawable")) test_ConstructorLorg_eclipse_swt_graphics_Drawable();
	else if (getName().equals("test_ConstructorLorg_eclipse_swt_graphics_DrawableI")) test_ConstructorLorg_eclipse_swt_graphics_DrawableI();
	else if (getName().equals("test_copyAreaIIIIII")) test_copyAreaIIIIII();
	else if (getName().equals("test_copyAreaLorg_eclipse_swt_graphics_ImageII")) test_copyAreaLorg_eclipse_swt_graphics_ImageII();
	else if (getName().equals("test_dispose")) test_dispose();
	else if (getName().equals("test_drawArcIIIIII")) test_drawArcIIIIII();
	else if (getName().equals("test_drawFocusIIII")) test_drawFocusIIII();
	else if (getName().equals("test_drawImageLorg_eclipse_swt_graphics_ImageII")) test_drawImageLorg_eclipse_swt_graphics_ImageII();
	else if (getName().equals("test_drawImageLorg_eclipse_swt_graphics_ImageIIIIIIII")) test_drawImageLorg_eclipse_swt_graphics_ImageIIIIIIII();
	else if (getName().equals("test_drawLineIIII")) test_drawLineIIII();
	else if (getName().equals("test_drawOvalIIII")) test_drawOvalIIII();
	else if (getName().equals("test_drawPointII")) test_drawPointII();
	else if (getName().equals("test_drawPolygon$I")) test_drawPolygon$I();
	else if (getName().equals("test_drawPolyline$I")) test_drawPolyline$I();
	else if (getName().equals("test_drawRectangleIIII")) test_drawRectangleIIII();
	else if (getName().equals("test_drawRectangleLorg_eclipse_swt_graphics_Rectangle")) test_drawRectangleLorg_eclipse_swt_graphics_Rectangle();
	else if (getName().equals("test_drawRoundRectangleIIIIII")) test_drawRoundRectangleIIIIII();
	else if (getName().equals("test_drawStringLjava_lang_StringII")) test_drawStringLjava_lang_StringII();
	else if (getName().equals("test_drawStringLjava_lang_StringIIZ")) test_drawStringLjava_lang_StringIIZ();
	else if (getName().equals("test_drawTextLjava_lang_StringII")) test_drawTextLjava_lang_StringII();
	else if (getName().equals("test_drawTextLjava_lang_StringIII")) test_drawTextLjava_lang_StringIII();
	else if (getName().equals("test_drawTextLjava_lang_StringIIZ")) test_drawTextLjava_lang_StringIIZ();
	else if (getName().equals("test_equalsLjava_lang_Object")) test_equalsLjava_lang_Object();
	else if (getName().equals("test_fillArcIIIIII")) test_fillArcIIIIII();
	else if (getName().equals("test_fillGradientRectangleIIIIZ")) test_fillGradientRectangleIIIIZ();
	else if (getName().equals("test_fillOvalIIII")) test_fillOvalIIII();
	else if (getName().equals("test_fillPolygon$I")) test_fillPolygon$I();
	else if (getName().equals("test_fillRectangleIIII")) test_fillRectangleIIII();
	else if (getName().equals("test_fillRectangleLorg_eclipse_swt_graphics_Rectangle")) test_fillRectangleLorg_eclipse_swt_graphics_Rectangle();
	else if (getName().equals("test_fillRoundRectangleIIIIII")) test_fillRoundRectangleIIIIII();
	else if (getName().equals("test_getAdvanceWidthC")) test_getAdvanceWidthC();
	else if (getName().equals("test_getBackground")) test_getBackground();
	else if (getName().equals("test_getCharWidthC")) test_getCharWidthC();
	else if (getName().equals("test_getClipping")) test_getClipping();
	else if (getName().equals("test_getClippingLorg_eclipse_swt_graphics_Region")) test_getClippingLorg_eclipse_swt_graphics_Region();
	else if (getName().equals("test_getFont")) test_getFont();
	else if (getName().equals("test_getFontMetrics")) test_getFontMetrics();
	else if (getName().equals("test_getForeground")) test_getForeground();
	else if (getName().equals("test_getLineStyle")) test_getLineStyle();
	else if (getName().equals("test_getLineWidth")) test_getLineWidth();
	else if (getName().equals("test_getStyle")) test_getStyle();
	else if (getName().equals("test_getXORMode")) test_getXORMode();
	else if (getName().equals("test_hashCode")) test_hashCode();
	else if (getName().equals("test_isClipped")) test_isClipped();
	else if (getName().equals("test_isDisposed")) test_isDisposed();
	else if (getName().equals("test_setBackgroundLorg_eclipse_swt_graphics_Color")) test_setBackgroundLorg_eclipse_swt_graphics_Color();
	else if (getName().equals("test_setClippingIIII")) test_setClippingIIII();
	else if (getName().equals("test_setClippingLorg_eclipse_swt_graphics_Rectangle")) test_setClippingLorg_eclipse_swt_graphics_Rectangle();
	else if (getName().equals("test_setClippingLorg_eclipse_swt_graphics_Region")) test_setClippingLorg_eclipse_swt_graphics_Region();
	else if (getName().equals("test_setFontLorg_eclipse_swt_graphics_Font")) test_setFontLorg_eclipse_swt_graphics_Font();
	else if (getName().equals("test_setForegroundLorg_eclipse_swt_graphics_Color")) test_setForegroundLorg_eclipse_swt_graphics_Color();
	else if (getName().equals("test_setLineStyleI")) test_setLineStyleI();
	else if (getName().equals("test_setLineWidthI")) test_setLineWidthI();
	else if (getName().equals("test_setXORModeZ")) test_setXORModeZ();
	else if (getName().equals("test_stringExtentLjava_lang_String")) test_stringExtentLjava_lang_String();
	else if (getName().equals("test_textExtentLjava_lang_String")) test_textExtentLjava_lang_String();
	else if (getName().equals("test_textExtentLjava_lang_StringI")) test_textExtentLjava_lang_StringI();
	else if (getName().equals("test_toString")) test_toString();
	else if (getName().equals("test_win32_newILorg_eclipse_swt_graphics_GCData")) test_win32_newILorg_eclipse_swt_graphics_GCData();
	else if (getName().equals("test_win32_newLorg_eclipse_swt_graphics_DrawableLorg_eclipse_swt_graphics_GCData")) test_win32_newLorg_eclipse_swt_graphics_DrawableLorg_eclipse_swt_graphics_GCData();
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
