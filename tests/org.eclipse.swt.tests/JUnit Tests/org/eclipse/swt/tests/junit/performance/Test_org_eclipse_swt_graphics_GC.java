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
	static final int COUNT = 10000;

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
	final int count = COUNT / 10;
	Image[] images = new Image [count];
	for (int i = 0; i < count; i++) {
		images[i] = new Image(display, 10, 10);		
	}
	GC[] gcs = new GC[count];
	
	PerformanceMeter meter = createMeter("image");
	meter.start();
	for (int i = 0; i < count; i++) {
		gcs[i] = new GC(images[i]);
	}
	meter.stop();

	for (int i = 0; i < count; i++) {
		gcs[i].dispose();
		images[i].dispose();
	}
	
	disposeMeter(meter);

	final int count2 = COUNT % 100;
	
	Class printerClass = null;
	try {
		printerClass = Class.forName("org.eclipse.swt.printing.Printer");
	} catch (ClassNotFoundException e) {
		// Printer class not present (eSWT). Skip test.
		return;
	}
	Object[] printers = new Object[count2];
	try {
		for (int i = 0; i < count2; i++) {
			printers[i] = printerClass.newInstance();
		}
	} catch (InstantiationException e1) {
		return;
	} catch (IllegalAccessException e1) {
		return;
	}

	meter = createMeter("printer");
	meter.start();
	for (int i = 0; i < count2; i++) {
		gcs[i] = new GC((Printer) printers[i]);
	}
	meter.stop();

	for (int i = 0; i < count2; i++) {
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
	// precompute points
	Rectangle bounds = gc.getClipping();
	final int[][] coords = new int[COUNT][];
	int y = 0;
	for (int i = 0; i < COUNT; i++) {
		int x = i % bounds.width;
		coords[i] = new int[] {x,y};
		if (x == 0) y += 3;
	}

	gc.setBackground(display.getSystemColor(SWT.COLOR_WHITE));
	gc.setForeground(display.getSystemColor(SWT.COLOR_WHITE));
	gc.fillRectangle(image.getBounds());
	gc.setForeground(display.getSystemColor(SWT.COLOR_BLUE));
	gc.drawLine(5, 5, 10, 10);
	gc.drawLine(5, 10, 10, 5);
	
	PerformanceMeter meter = createMeter();
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		gc.copyArea(5, 5, 10, 10, coords[i][0], coords[i][1]);
	}
	meter.stop();
	
	disposeMeter(meter);
}

public void test_copyAreaLorg_eclipse_swt_graphics_ImageII() {
	// precompute points
	Rectangle bounds = gc.getClipping();
	final int[][] coords = new int[COUNT][];
	int y = 0;
	for (int i = 0; i < COUNT; i++) {
		int x = i % bounds.width;
		coords[i] = new int[] {x,y};
		if (x == 0) y += 3;
	}

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
		gc.copyArea(images[i], coords[i][0], coords[i][1]);
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
	// precompute points
	Rectangle bounds = gc.getClipping();
	final int[][] coords = new int[COUNT][];
	int y = 0;
	for (int i = 0; i < COUNT; i++) {
		int x = i % bounds.width;
		coords[i] = new int[] {x,y};
		if (x == 0) y += 3;
	}

	PerformanceMeter meter = createMeter();
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		gc.drawArc(coords[i][0], coords[i][1], 50, 25, 90, 90);
	}
	meter.stop();
	
	disposeMeter(meter);
}

public void test_drawFocusIIII() {
	// precompute points
	Rectangle bounds = gc.getClipping();
	final int[][] coords = new int[COUNT][];
	int y = 0;
	for (int i = 0; i < COUNT; i++) {
		int x = i % bounds.width;
		coords[i] = new int[] {x,y};
		if (x == 0) y += 3;
	}

	PerformanceMeter meter = createMeter();
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		gc.drawFocus(coords[i][0], coords[i][1], 30, 20);
	}
	meter.stop();
	
	disposeMeter(meter);
}

public void test_drawImageLorg_eclipse_swt_graphics_ImageII() {
	// precompute points
	Rectangle bounds = gc.getClipping();
	final int[][] coords = new int[COUNT][];
	int y = 0;
	for (int i = 0; i < COUNT; i++) {
		int x = i % bounds.width;
		coords[i] = new int[] {x,y};
		if (x == 0) y += 3;
	}
	
	Color red = display.getSystemColor(SWT.COLOR_RED);
	Color black = display.getSystemColor(SWT.COLOR_BLACK);
	Color green = display.getSystemColor(SWT.COLOR_GREEN);
	PaletteData paletteData = new PaletteData(new RGB[] {red.getRGB(), black.getRGB(), green.getRGB()});
	ImageData data = new ImageData(30,30, 8, paletteData);
	for (y = 0; y < data.height; y++) {
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
	for (y = 0; y < data.height; y++) {
		for (int x = 0; x < data.width; x++) {
			data.setAlpha(x, y, 127);
		}
	}		
	Image imageAlpha = new Image(display, data);
	gc.drawImage(imageNormal, 100, 100);
	gc.drawImage(imageTransparent, 130, 100);
	gc.drawImage(imageAlpha, 160, 100);
	
	try {
		PerformanceMeter meter = createMeter("normal");
		meter.start();
		for (int i = 0; i < COUNT; i++) {
			gc.drawImage(imageNormal, coords[i][0], coords[i][1]);	// normal image
		}
		meter.stop();
		
		disposeMeter(meter);
	
		meter = createMeter("transparent");
		meter.start();
		for (int i = 0; i < COUNT; i++) {
			gc.drawImage(imageTransparent, coords[i][0], coords[i][1]);	// transparent image
		}
		meter.stop();
		
		disposeMeter(meter);
	
		meter = createMeter("alpha");
		meter.start();
		for (int i = 0; i < COUNT; i++) {
			gc.drawImage(imageAlpha, coords[i][0], coords[i][1]);	// alpha image
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
	// TODO
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
	// precompute points
	Rectangle bounds = gc.getClipping();
	final int[][] coords = new int[COUNT][];
	int y = 0;
	for (int i = 0; i < COUNT; i++) {
		int x = i % bounds.width;
		coords[i] = new int[] {x,y, x+5,y+5};
		if (x == 0) y += 3;
	}

	PerformanceMeter meter = createMeter();
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		gc.drawLine(coords[i][0],coords[i][1], coords[i][2],coords[i][3]);
	}
	meter.stop();
	
	disposeMeter(meter);
}

public void test_drawOvalIIII() {
	// precompute points
	Rectangle bounds = gc.getClipping();
	final int[][] coords = new int[COUNT][];
	int y = 0;
	for (int i = 0; i < COUNT; i++) {
		int x = i % bounds.width;
		coords[i] = new int[] {x,y};
		if (x == 0) y += 3;
	}

	PerformanceMeter meter = createMeter();
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		gc.drawOval(coords[i][0], coords[i][1], 20, 30);				
	}
	meter.stop();
	
	disposeMeter(meter);
}

public void test_drawPointII() {
	// precompute points
	Rectangle bounds = gc.getClipping();
	final int[][] coords = new int[COUNT][];
	int y = 0;
	for (int i = 0; i < COUNT; i++) {
		int x = i % bounds.width;
		coords[i] = new int[] {x,y};
		if (x == 0) y += 3;
	}

	PerformanceMeter meter = createMeter();
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		gc.drawPoint(coords[i][0], coords[i][1]);
	}
	meter.stop();
	
	disposeMeter(meter);
}

public void test_drawPolygon$I() {
	// precompute points
	Rectangle bounds = gc.getClipping();
	final int[][] coords = new int[COUNT][];
	int y = 0;
	for (int i = 0; i < COUNT; i++) {
		int x = i % bounds.width;
		coords[i] = new int[] {x,y, x+5,y+10, x,y+20};
		if (x == 0) y += 3;
	}

	PerformanceMeter meter = createMeter();
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		gc.drawPolygon(coords[i]);				
	}
	meter.stop();
	
	disposeMeter(meter);				
}

public void test_drawPolyline$I() {
	// precompute points
	Rectangle bounds = gc.getClipping();
	final int[][] coords = new int[COUNT][];
	int y = 0;
	for (int i = 0; i < COUNT; i++) {
		int x = i % bounds.width;
		coords[i] = new int[] {x,y, x+5,y+10, x,y+20};
		if (x == 0) y += 3;
	}

	PerformanceMeter meter = createMeter();
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		gc.drawPolyline(coords[i]);				
	}
	meter.stop();
	
	disposeMeter(meter);
}

public void test_drawRectangleIIII() {
	// precompute points
	Rectangle bounds = gc.getClipping();
	final int[][] coords = new int[COUNT][];
	int y = 0;
	for (int i = 0; i < COUNT; i++) {
		int x = i % bounds.width;
		coords[i] = new int[] {x,y};
		if (x == 0) y += 3;
	}
	
	PerformanceMeter meter = createMeter();
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		gc.drawRectangle(coords[i][0], coords[i][1], 20, 30);				
	}
	meter.stop();
	
	disposeMeter(meter);			
}

public void test_drawRectangleLorg_eclipse_swt_graphics_Rectangle() {
	// precompute points
	Rectangle bounds = gc.getClipping();
	final Rectangle[] coords = new Rectangle[COUNT];
	int y = 0;
	for (int i = 0; i < COUNT; i++) {
		int x = i % bounds.width;
		coords[i] = new Rectangle (x, y, 20, 30);
		if (x == 0) y += 3;
	}

	PerformanceMeter meter = createMeter();
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		gc.drawRectangle(coords[i]);
	}
	meter.stop();
	
	disposeMeter(meter);
}

public void test_drawRoundRectangleIIIIII() {
	// precompute points
	Rectangle bounds = gc.getClipping();
	final int[][] coords = new int[COUNT][];
	int y = 0;
	for (int i = 0; i < COUNT; i++) {
		int x = i % bounds.width;
		coords[i] = new int[] {x,y};
		if (x == 0) y += 3;
	}
	
	PerformanceMeter meter = createMeter();
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		gc.drawRoundRectangle(coords[i][0], coords[i][1], 20, 30, 3, 3);				
	}
	meter.stop();
	
	disposeMeter(meter);
}

public void test_drawStringLjava_lang_StringII() {
	// precompute points
	Rectangle bounds = gc.getClipping();
	final int[][] coords = new int[COUNT][];
	int y = 0;
	for (int i = 0; i < COUNT; i++) {
		int x = i % bounds.width;
		coords[i] = new int[] {x,y};
		if (x == 0) y += 3;
	}
	
	PerformanceMeter meter = createMeter();
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		gc.drawString("test string", coords[i][0], coords[i][1]);				
	}
	meter.stop();
	
	disposeMeter(meter);
}

public void test_drawStringLjava_lang_StringIIZ() {
	// precompute points
	Rectangle bounds = gc.getClipping();
	final int[][] coords = new int[COUNT][];
	int y = 0;
	for (int i = 0; i < COUNT; i++) {
		int x = i % bounds.width;
		coords[i] = new int[] {x,y};
		if (x == 0) y += 3;
	}
	
	PerformanceMeter meter = createMeter("transparent");
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		gc.drawString("test string", coords[i][0], coords[i][1], true);				
	}
	meter.stop();
	
	disposeMeter(meter);
	
	meter = createMeter("not transparent");
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		gc.drawString("test string", coords[i][0], coords[i][1], false);				
	}
	meter.stop();
	
	disposeMeter(meter);
}

public void test_drawTextLjava_lang_StringII() {
	// precompute points
	Rectangle bounds = gc.getClipping();
	final int[][] coords = new int[COUNT][];
	int y = 0;
	for (int i = 0; i < COUNT; i++) {
		int x = i % bounds.width;
		coords[i] = new int[] {x,y};
		if (x == 0) y += 3;
	}
	
	PerformanceMeter meter = createMeter();
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		gc.drawText("test string", coords[i][0], coords[i][1], true);				
	}
	meter.stop();
	
	disposeMeter(meter);			
}

public void test_drawTextLjava_lang_StringIII() {
	// precompute points
	Rectangle bounds = gc.getClipping();
	final int[][] coords = new int[COUNT][];
	int y = 0;
	for (int i = 0; i < COUNT; i++) {
		int x = i % bounds.width;
		coords[i] = new int[] {x,y};
		if (x == 0) y += 3;
	}
	
	PerformanceMeter meter = createMeter("transparent");
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		gc.drawText("\t\r\na&bc&", coords[i][0], coords[i][1], SWT.DRAW_TRANSPARENT);				
	}
	meter.stop();
	
	disposeMeter(meter);

	meter = createMeter("delimiter");
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		gc.drawText("\t\r\na&bc&", coords[i][0], coords[i][1], SWT.DRAW_DELIMITER);				
	}
	meter.stop();
	
	disposeMeter(meter);

	meter = createMeter("mnemonic");
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		gc.drawText("\t\r\na&bc&", coords[i][0], coords[i][1], SWT.DRAW_MNEMONIC);				
	}
	meter.stop();
	
	disposeMeter(meter);

	meter = createMeter("tab");
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		gc.drawText("\t\r\na&bc&", coords[i][0], coords[i][1], SWT.DRAW_TAB);				
	}
	meter.stop();
	
	disposeMeter(meter);
	
	meter = createMeter("no flags");
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		gc.drawText("\t\r\na&bc&", coords[i][0], coords[i][1], SWT.NONE);				
	}
	meter.stop();
	
	disposeMeter(meter);
}

public void test_drawTextLjava_lang_StringIIZ() {
	// precompute points
	Rectangle bounds = gc.getClipping();
	final int[][] coords = new int[COUNT][];
	int y = 0;
	for (int i = 0; i < COUNT; i++) {
		int x = i % bounds.width;
		coords[i] = new int[] {x,y};
		if (x == 0) y += 3;
	}
	
	PerformanceMeter meter = createMeter("transparent");
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		gc.drawText("test string", coords[i][0], coords[i][1], true);				
	}
	meter.stop();
	
	disposeMeter(meter);
	
	meter = createMeter("not transparent");
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		gc.drawText("test string", coords[i][0], coords[i][1], false);				
	}
	meter.stop();
	
	disposeMeter(meter);				
}

public void test_equalsLjava_lang_Object() {
	GC gc1 = new GC(display);
	
	PerformanceMeter meter = createMeter("equal");
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		gc1.equals(gc1);	// equal
	}
	meter.stop();
	
	gc1.dispose();
	
	disposeMeter(meter);
		
	gc1 = new GC(display);
	Color gc2 = new Color(display, 128, 255, 0);
	
	meter = createMeter("not equal");
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		gc1.equals(gc2);	// not equal
	}
	meter.stop();
	
	gc1.dispose();
	gc2.dispose();
	
	disposeMeter(meter);
}

public void test_fillArcIIIIII() {
	// precompute points
	Rectangle bounds = gc.getClipping();
	final int[][] coords = new int[COUNT][];
	int y = 0;
	for (int i = 0; i < COUNT; i++) {
		int x = i % bounds.width;
		coords[i] = new int[] {x,y};
		if (x == 0) y += 10;
	}

	PerformanceMeter meter = createMeter();
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		gc.fillArc(coords[i][0], coords[i][1], 20, 10, 90, 90);
	}
	meter.stop();
	
	disposeMeter(meter);				
}

public void test_fillGradientRectangleIIIIZ() {
	// precompute points
	Rectangle bounds = gc.getClipping();
	final int[][] coords = new int[COUNT][];
	int y = 0;
	for (int i = 0; i < COUNT; i++) {
		int x = i % bounds.width;
		coords[i] = new int[] {x,y};
		if (x == 0) y += 10;
	}
	
	PerformanceMeter meter = createMeter("vertical");
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		gc.fillGradientRectangle(coords[i][0], coords[i][1], 20, 10, true);				
	}
	meter.stop();
	
	disposeMeter(meter);
	
	meter = createMeter("horizontal");
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		gc.fillGradientRectangle(coords[i][0], coords[i][1], 20, 10, false);				
	}
	meter.stop();
	
	disposeMeter(meter);
}

public void test_fillOvalIIII() {
	// precompute points
	Rectangle bounds = gc.getClipping();
	final int[][] coords = new int[COUNT][];
	int y = 0;
	for (int i = 0; i < COUNT; i++) {
		int x = i % bounds.width;
		coords[i] = new int[] {x,y};
		if (x == 0) y += 10;
	}

	PerformanceMeter meter = createMeter();
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		gc.fillOval(coords[i][0], coords[i][1], 20, 10);				
	}
	meter.stop();
	
	disposeMeter(meter);				
}

public void test_fillPolygon$I() {
	// precompute points
	Rectangle bounds = gc.getClipping();
	final int[][] coords = new int[COUNT][];
	int y = 0;
	for (int i = 0; i < COUNT; i++) {
		int x = i % bounds.width;
		coords[i] = new int[] {x,y, x+5,y+5, x,y+10};
		if (x == 0) y += 10;
	}

	PerformanceMeter meter = createMeter();
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		gc.fillPolygon(coords[i]);				
	}
	meter.stop();
	
	disposeMeter(meter);				
}

public void test_fillRectangleIIII() {
	// precompute points
	Rectangle bounds = gc.getClipping();
	final int[][] coords = new int[COUNT][];
	int y = 0;
	for (int i = 0; i < COUNT; i++) {
		int x = i % bounds.width;
		coords[i] = new int[] {x,y};
		if (x == 0) y += 10;
	}
	
	PerformanceMeter meter = createMeter();
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		gc.fillRectangle(coords[i][0], coords[i][1], 20, 10);				
	}
	meter.stop();
	
	disposeMeter(meter);				
}

public void test_fillRectangleLorg_eclipse_swt_graphics_Rectangle() {
	// precompute points
	Rectangle bounds = gc.getClipping();
	final Rectangle[] coords = new Rectangle[COUNT];
	int y = 0;
	for (int i = 0; i < COUNT; i++) {
		int x = i % bounds.width;
		coords[i] = new Rectangle (x, y, 20, 10);
		if (x == 0) y += 10;
	}

	PerformanceMeter meter = createMeter();
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		gc.fillRectangle(coords[i]);
	}
	meter.stop();
	
	disposeMeter(meter);				
}

public void test_fillRoundRectangleIIIIII() {
	// precompute points
	Rectangle bounds = gc.getClipping();
	final int[][] coords = new int[COUNT][];
	int y = 0;
	for (int i = 0; i < COUNT; i++) {
		int x = i % bounds.width;
		coords[i] = new int[] {x,y};
		if (x == 0) y += 3;
	}
	
	PerformanceMeter meter = createMeter();
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		gc.fillRoundRectangle(coords[i][0], coords[i][1], 20, 30, 3, 3);				
	}
	meter.stop();
	
	disposeMeter(meter);			
}

public void test_getAdvanceWidthC() {
	PerformanceMeter meter = createMeter();
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		gc.getAdvanceWidth('a');
	}
	meter.stop();
	
	disposeMeter(meter);
}

public void test_getBackground() {
	PerformanceMeter meter = createMeter();
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		gc.getBackground();
	}
	meter.stop();
	
	disposeMeter(meter);
}

public void test_getCharWidthC() {
	PerformanceMeter meter = createMeter();
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		gc.getCharWidth('a');
	}
	meter.stop();
	
	disposeMeter(meter);
}

public void test_getClipping() {
	PerformanceMeter meter = createMeter();
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		gc.getClipping();
	}
	meter.stop();
	
	disposeMeter(meter);
}

public void test_getClippingLorg_eclipse_swt_graphics_Region() {
	Region[] regions = new Region[COUNT];
	for (int i = 0; i < COUNT; i++) {
		regions[i] = new Region(display);
	}
	
	PerformanceMeter meter = createMeter();
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		gc.getClipping(regions[i]);
	}
	meter.stop();
	
	for (int i = 0; i < COUNT; i++) {
		regions[i].dispose();
	}
	
	disposeMeter(meter);
}

public void test_getFont() {
	PerformanceMeter meter = createMeter();
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		gc.getFont();
	}
	meter.stop();
	
	disposeMeter(meter);
}

public void test_getFontMetrics() {
	PerformanceMeter meter = createMeter();
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		gc.getFontMetrics();
	}
	meter.stop();
	
	disposeMeter(meter);
}

public void test_getForeground() {
	PerformanceMeter meter = createMeter();
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		gc.getForeground();
	}
	meter.stop();
	
	disposeMeter(meter);
}

public void test_getLineStyle() {
	PerformanceMeter meter = createMeter();
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		gc.getLineStyle();
	}
	meter.stop();
	
	disposeMeter(meter);
}

public void test_getLineWidth() {
	PerformanceMeter meter = createMeter();
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		gc.getLineWidth();
	}
	meter.stop();
	
	disposeMeter(meter);
}

public void test_getStyle() {
	PerformanceMeter meter = createMeter();
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		gc.getStyle();
	}
	meter.stop();
	
	disposeMeter(meter);
}

public void test_getXORMode() {
	PerformanceMeter meter = createMeter();
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		gc.getXORMode();
	}
	meter.stop();
	
	disposeMeter(meter);
}

public void test_hashCode() {
	PerformanceMeter meter = createMeter();
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		gc.hashCode();
	}
	meter.stop();
	
	disposeMeter(meter);
}

public void test_isClipped() {
	PerformanceMeter meter = createMeter();
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		gc.isClipped();
	}
	meter.stop();
	
	disposeMeter(meter);
}

public void test_isDisposed() {
	GC gc = new GC(display);
	
	PerformanceMeter meter = createMeter("not disposed");
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		gc.isDisposed();	// not disposed
	}
	meter.stop();
	
	gc.dispose();
	
	disposeMeter(meter);
	
	meter = createMeter("disposed");
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		gc.isDisposed();	// disposed
	}
	meter.stop();
	
	disposeMeter(meter);
}

public void test_setBackgroundLorg_eclipse_swt_graphics_Color() {
	final Color color = display.getSystemColor(SWT.COLOR_RED);
	PerformanceMeter meter = createMeter();
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		gc.setBackground(color);
	}
	meter.stop();
	
	disposeMeter(meter);
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
	final Color color = display.getSystemColor(SWT.COLOR_RED);
	PerformanceMeter meter = createMeter();
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		gc.setForeground(color);
	}
	meter.stop();
	
	disposeMeter(meter);

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
//	methodNames.addElement("test_ConstructorLorg_eclipse_swt_graphics_DrawableI");
//	methodNames.addElement("test_copyAreaIIIIII");
//	methodNames.addElement("test_copyAreaLorg_eclipse_swt_graphics_ImageII");
//	methodNames.addElement("test_dispose");
//	methodNames.addElement("test_drawArcIIIIII");
//	methodNames.addElement("test_drawFocusIIII");
//	methodNames.addElement("test_drawImageLorg_eclipse_swt_graphics_ImageII");
//	methodNames.addElement("test_drawImageLorg_eclipse_swt_graphics_ImageIIIIIIII");
//	methodNames.addElement("test_drawLineIIII");
//	methodNames.addElement("test_drawOvalIIII");
//	methodNames.addElement("test_drawPointII");
//	methodNames.addElement("test_drawPolygon$I");
//	methodNames.addElement("test_drawPolyline$I");
//	methodNames.addElement("test_drawRectangleIIII");
//	methodNames.addElement("test_drawRectangleLorg_eclipse_swt_graphics_Rectangle");
//	methodNames.addElement("test_drawRoundRectangleIIIIII");
//	methodNames.addElement("test_drawStringLjava_lang_StringII");
//	methodNames.addElement("test_drawStringLjava_lang_StringIIZ");
//	methodNames.addElement("test_drawTextLjava_lang_StringII");
//	methodNames.addElement("test_drawTextLjava_lang_StringIII");
//	methodNames.addElement("test_drawTextLjava_lang_StringIIZ");
//	methodNames.addElement("test_equalsLjava_lang_Object");
//	methodNames.addElement("test_fillArcIIIIII");
//	methodNames.addElement("test_fillGradientRectangleIIIIZ");
//	methodNames.addElement("test_fillOvalIIII");
//	methodNames.addElement("test_fillPolygon$I");
//	methodNames.addElement("test_fillRectangleIIII");
//	methodNames.addElement("test_fillRectangleLorg_eclipse_swt_graphics_Rectangle");
//	methodNames.addElement("test_fillRoundRectangleIIIIII");
//	methodNames.addElement("test_getAdvanceWidthC");
//	methodNames.addElement("test_getBackground");
//	methodNames.addElement("test_getCharWidthC");
//	methodNames.addElement("test_getClipping");
//	methodNames.addElement("test_getClippingLorg_eclipse_swt_graphics_Region");
//	methodNames.addElement("test_getFont");
//	methodNames.addElement("test_getFontMetrics");
//	methodNames.addElement("test_getForeground");
//	methodNames.addElement("test_getLineStyle");
//	methodNames.addElement("test_getLineWidth");
//	methodNames.addElement("test_getStyle");
//	methodNames.addElement("test_getXORMode");
//	methodNames.addElement("test_hashCode");
//	methodNames.addElement("test_isClipped");
//	methodNames.addElement("test_isDisposed");
//	methodNames.addElement("test_setBackgroundLorg_eclipse_swt_graphics_Color");
//	methodNames.addElement("test_setClippingIIII");
//	methodNames.addElement("test_setClippingLorg_eclipse_swt_graphics_Rectangle");
//	methodNames.addElement("test_setClippingLorg_eclipse_swt_graphics_Region");
//	methodNames.addElement("test_setFontLorg_eclipse_swt_graphics_Font");
//	methodNames.addElement("test_setForegroundLorg_eclipse_swt_graphics_Color");
//	methodNames.addElement("test_setLineStyleI");
//	methodNames.addElement("test_setLineWidthI");
//	methodNames.addElement("test_setXORModeZ");
//	methodNames.addElement("test_stringExtentLjava_lang_String");
//	methodNames.addElement("test_textExtentLjava_lang_String");
//	methodNames.addElement("test_textExtentLjava_lang_StringI");
//	methodNames.addElement("test_win32_newILorg_eclipse_swt_graphics_GCData");
//	methodNames.addElement("test_win32_newLorg_eclipse_swt_graphics_DrawableLorg_eclipse_swt_graphics_GCData");
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
