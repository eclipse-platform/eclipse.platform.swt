/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tests.junit;


import org.eclipse.swt.widgets.*;
import org.eclipse.swt.graphics.*;
import junit.framework.*;
import junit.textui.*;

/**
 * Automated Test Suite for class org.eclipse.swt.graphics.Image
 *
 * @see org.eclipse.swt.graphics.Image
 */
public class Test_org_eclipse_swt_graphics_Image extends SwtTestCase {

Display display;

public Test_org_eclipse_swt_graphics_Image(String name) {
	super(name);
}

public static void main(String[] args) {
	TestRunner.run(suite());
}

protected void setUp() {
	display = new Display();
}

protected void tearDown() {
	display.dispose();
}

public void test_ConstructorLorg_eclipse_swt_graphics_DeviceII() {
	warnUnimpl("Test test_ConstructorLorg_eclipse_swt_graphics_DeviceII not written");
}

public void test_ConstructorLorg_eclipse_swt_graphics_DeviceLorg_eclipse_swt_graphics_ImageI() {
	warnUnimpl("Test test_ConstructorLorg_eclipse_swt_graphics_DeviceLorg_eclipse_swt_graphics_ImageI not written");
}

public void test_ConstructorLorg_eclipse_swt_graphics_DeviceLorg_eclipse_swt_graphics_Rectangle() {
	warnUnimpl("Test test_ConstructorLorg_eclipse_swt_graphics_DeviceLorg_eclipse_swt_graphics_Rectangle not written");
}

public void test_ConstructorLorg_eclipse_swt_graphics_DeviceLorg_eclipse_swt_graphics_ImageData() {
	warnUnimpl("Test test_ConstructorLorg_eclipse_swt_graphics_DeviceLorg_eclipse_swt_graphics_ImageData not written");
}

public void test_ConstructorLorg_eclipse_swt_graphics_DeviceLorg_eclipse_swt_graphics_ImageDataLorg_eclipse_swt_graphics_ImageData() {
	warnUnimpl("Test test_ConstructorLorg_eclipse_swt_graphics_DeviceLorg_eclipse_swt_graphics_ImageDataLorg_eclipse_swt_graphics_ImageData not written");
}

public void test_ConstructorLorg_eclipse_swt_graphics_DeviceLjava_io_InputStream() {
	warnUnimpl("Test test_ConstructorLorg_eclipse_swt_graphics_DeviceLjava_io_InputStream not written");
}

public void test_ConstructorLorg_eclipse_swt_graphics_DeviceLjava_lang_String() {
	warnUnimpl("Test test_ConstructorLorg_eclipse_swt_graphics_DeviceLjava_lang_String not written");
}

public void test_dispose() {
	warnUnimpl("Test test_dispose not written");
}

public void test_equalsLjava_lang_Object() {
	warnUnimpl("Test test_equalsLjava_lang_Object not written");
}

public void test_getBackground() {
	warnUnimpl("Test test_getBackground not written");
}

public void test_getBounds() {
	warnUnimpl("Test test_getBounds not written");
}

public void test_getImageData() {	
	getImageData1();
	getImageData2(24, new PaletteData(0xff0000, 0xff00, 0xff));		
	getImageData2(32, new PaletteData(0xff0000, 0xff00, 0xff));
}

public void test_hashCode() {
	warnUnimpl("Test test_hashCode not written");
}

public void test_internal_new_GCLorg_eclipse_swt_graphics_GCData() {
	warnUnimpl("Test test_internal_new_GCLorg_eclipse_swt_graphics_GCData not written");
}

public void test_internal_dispose_GCILorg_eclipse_swt_graphics_GCData() {
	warnUnimpl("Test test_internal_dispose_GCILorg_eclipse_swt_graphics_GCData not written");
}

public void test_isDisposed() {
	warnUnimpl("Test test_isDisposed not written");
}

public void test_setBackgroundLorg_eclipse_swt_graphics_Color() {
	warnUnimpl("Test test_setBackgroundLorg_eclipse_swt_graphics_Color not written");
}

public void test_toString() {
	warnUnimpl("Test test_toString not written");
}

public void test_win32_newLorg_eclipse_swt_graphics_DeviceII() {
	// do not test - Windows only
}

public static Test suite() {
	TestSuite suite = new TestSuite();
	java.util.Vector methodNames = methodNames();
	java.util.Enumeration e = methodNames.elements();
	while (e.hasMoreElements()) {
		suite.addTest(new Test_org_eclipse_swt_graphics_Image((String)e.nextElement()));
	}
	return suite;
}
public static java.util.Vector methodNames() {
	java.util.Vector methodNames = new java.util.Vector();
	methodNames.addElement("test_ConstructorLorg_eclipse_swt_graphics_DeviceII");
	methodNames.addElement("test_ConstructorLorg_eclipse_swt_graphics_DeviceLorg_eclipse_swt_graphics_ImageI");
	methodNames.addElement("test_ConstructorLorg_eclipse_swt_graphics_DeviceLorg_eclipse_swt_graphics_Rectangle");
	methodNames.addElement("test_ConstructorLorg_eclipse_swt_graphics_DeviceLorg_eclipse_swt_graphics_ImageData");
	methodNames.addElement("test_ConstructorLorg_eclipse_swt_graphics_DeviceLorg_eclipse_swt_graphics_ImageDataLorg_eclipse_swt_graphics_ImageData");
	methodNames.addElement("test_ConstructorLorg_eclipse_swt_graphics_DeviceLjava_io_InputStream");
	methodNames.addElement("test_ConstructorLorg_eclipse_swt_graphics_DeviceLjava_lang_String");
	methodNames.addElement("test_dispose");
	methodNames.addElement("test_equalsLjava_lang_Object");
	methodNames.addElement("test_getBackground");
	methodNames.addElement("test_getBounds");
	methodNames.addElement("test_getImageData");
	methodNames.addElement("test_hashCode");
	methodNames.addElement("test_internal_new_GCLorg_eclipse_swt_graphics_GCData");
	methodNames.addElement("test_internal_dispose_GCILorg_eclipse_swt_graphics_GCData");
	methodNames.addElement("test_isDisposed");
	methodNames.addElement("test_setBackgroundLorg_eclipse_swt_graphics_Color");
	methodNames.addElement("test_toString");
	methodNames.addElement("test_win32_newLorg_eclipse_swt_graphics_DeviceII");
	return methodNames;
}
protected void runTest() throws Throwable {
	if (getName().equals("test_ConstructorLorg_eclipse_swt_graphics_DeviceII")) test_ConstructorLorg_eclipse_swt_graphics_DeviceII();
	else if (getName().equals("test_ConstructorLorg_eclipse_swt_graphics_DeviceLorg_eclipse_swt_graphics_ImageI")) test_ConstructorLorg_eclipse_swt_graphics_DeviceLorg_eclipse_swt_graphics_ImageI();
	else if (getName().equals("test_ConstructorLorg_eclipse_swt_graphics_DeviceLorg_eclipse_swt_graphics_Rectangle")) test_ConstructorLorg_eclipse_swt_graphics_DeviceLorg_eclipse_swt_graphics_Rectangle();
	else if (getName().equals("test_ConstructorLorg_eclipse_swt_graphics_DeviceLorg_eclipse_swt_graphics_ImageData")) test_ConstructorLorg_eclipse_swt_graphics_DeviceLorg_eclipse_swt_graphics_ImageData();
	else if (getName().equals("test_ConstructorLorg_eclipse_swt_graphics_DeviceLorg_eclipse_swt_graphics_ImageDataLorg_eclipse_swt_graphics_ImageData")) test_ConstructorLorg_eclipse_swt_graphics_DeviceLorg_eclipse_swt_graphics_ImageDataLorg_eclipse_swt_graphics_ImageData();
	else if (getName().equals("test_ConstructorLorg_eclipse_swt_graphics_DeviceLjava_io_InputStream")) test_ConstructorLorg_eclipse_swt_graphics_DeviceLjava_io_InputStream();
	else if (getName().equals("test_ConstructorLorg_eclipse_swt_graphics_DeviceLjava_lang_String")) test_ConstructorLorg_eclipse_swt_graphics_DeviceLjava_lang_String();
	else if (getName().equals("test_dispose")) test_dispose();
	else if (getName().equals("test_equalsLjava_lang_Object")) test_equalsLjava_lang_Object();
	else if (getName().equals("test_getBackground")) test_getBackground();
	else if (getName().equals("test_getBounds")) test_getBounds();
	else if (getName().equals("test_getImageData")) test_getImageData();
	else if (getName().equals("test_hashCode")) test_hashCode();
	else if (getName().equals("test_internal_new_GCLorg_eclipse_swt_graphics_GCData")) test_internal_new_GCLorg_eclipse_swt_graphics_GCData();
	else if (getName().equals("test_internal_dispose_GCILorg_eclipse_swt_graphics_GCData")) test_internal_dispose_GCILorg_eclipse_swt_graphics_GCData();
	else if (getName().equals("test_isDisposed")) test_isDisposed();
	else if (getName().equals("test_setBackgroundLorg_eclipse_swt_graphics_Color")) test_setBackgroundLorg_eclipse_swt_graphics_Color();
	else if (getName().equals("test_toString")) test_toString();
	else if (getName().equals("test_win32_newLorg_eclipse_swt_graphics_DeviceII")) test_win32_newLorg_eclipse_swt_graphics_DeviceII();
}

/** Test implementation **/

void getImageData1() {
	ImageLoader loader = new ImageLoader();
	ImageData data1 = loader.load(SwtTestCase.class.getResourceAsStream("dot.gif"))[0];

	Image image = new Image(display, data1);
	ImageData data2 = image.getImageData();
	
	assertEquals("Image width should be the same", data1.width, data2.width);
	assertEquals("Image height should be the same", data1.height, data2.height);
}

/*
 * Verify Image.getImageData returns pixels with the same RGB value as the
 * source image. This test only makes sense with depth of 24 and 32 bits.
 */
void getImageData2(int depth, PaletteData palette) {
	int width = 10;
	int height = 10;
	Color color = new Color(display, 0, 0xff, 0);
	RGB colorRGB = color.getRGB();

	ImageData imageData = new ImageData(width, height, depth, palette);
	Image image = new Image(display, imageData);
		
	GC gc = new GC(image);
	gc.setBackground(color);
	gc.setForeground(color);
	gc.fillRectangle(0, 0, 10, 10);
		
	ImageData newData = image.getImageData();
	PaletteData newPalette = newData.palette;
	for (int i = 0; i < width; i++) {
		for (int j = 0; j < height; j++) {
			int pixel = newData.getPixel(i, j);
			RGB rgb = newPalette.getRGB(pixel);
			assertTrue("rgb.equals(colorRGB)", rgb.equals(colorRGB));
		}
	}
	color.dispose();
	gc.dispose();
	image.dispose();
}

}
