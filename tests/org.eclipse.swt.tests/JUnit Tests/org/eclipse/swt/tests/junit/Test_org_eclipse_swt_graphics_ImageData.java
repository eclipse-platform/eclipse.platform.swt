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


import java.io.*;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;

import junit.framework.*;
import junit.textui.*;

/**
 * Automated Test Suite for class org.eclipse.swt.graphics.ImageData
 *
 * @see org.eclipse.swt.graphics.ImageData
 */
public class Test_org_eclipse_swt_graphics_ImageData extends SwtTestCase {

public Test_org_eclipse_swt_graphics_ImageData(String name) {
	super(name);
}

public static void main(String[] args) {
	TestRunner.run(suite());
}

protected void setUp() {
}

protected void tearDown() {
}

public void test_ConstructorIIILorg_eclipse_swt_graphics_PaletteData() {
	try {
		new ImageData(-1, 1, 1, new PaletteData(new RGB[] {new RGB(0, 0, 0)}));
		fail("No exception thrown for width < 0");
	} catch (IllegalArgumentException e) {
	}

	try {
		new ImageData(1, -1, 1, new PaletteData(new RGB[] {new RGB(0, 0, 0)}));
		fail("No exception thrown for height < 0");
	} catch (IllegalArgumentException e) {
	}

	try {
		new ImageData(1, 1, 1, null, 0, new byte[] {});
		fail("No exception thrown for paletteData == null");
	} catch (IllegalArgumentException e) {
	}

	try {
		new ImageData(1, 1, 3, new PaletteData(new RGB[] {new RGB(0, 0, 0)}));
		fail("No exception thrown for unsupported depth");
	} catch (IllegalArgumentException e) {
	}
	
	int[] validDepths = {1, 2, 4, 8, 16, 24, 32};
	for (int i = 0; i < validDepths.length; i++) {
		new ImageData(1, 1, validDepths[i], new PaletteData(new RGB[] {new RGB(0, 0, 0)}));
	};
}

public void test_ConstructorIIILorg_eclipse_swt_graphics_PaletteDataI$B() {
	try {
		new ImageData(-1, 1, 1, new PaletteData(new RGB[] {new RGB(0, 0, 0)}), 1, new byte[] {});
		fail("No exception thrown for width < 0");
	} catch (IllegalArgumentException e) {
	}

	try {
		new ImageData(1, -1, 1, new PaletteData(new RGB[] {new RGB(0, 0, 0)}), 1, new byte[] {});
		fail("No exception thrown for height < 0");
	} catch (IllegalArgumentException e) {
	}

	try {
		new ImageData(1, 1, 1, null, 0, new byte[] {});
		fail("No exception thrown for paletteData == null");
	} catch (IllegalArgumentException e) {
	}

	try {
		new ImageData(1, 1, 1, new PaletteData(new RGB[] {new RGB(0, 0, 0)}), 1, null);
		fail("No exception thrown for data == null");
	} catch (IllegalArgumentException e) {
	}

	try {
		new ImageData(1, 1, 3, new PaletteData(new RGB[] {new RGB(0, 0, 0)}), 1, new byte[] {});
		fail("No exception thrown for unsupported depth");
	} catch (IllegalArgumentException e) {
	}
	
	int[] validDepths = {1, 2, 4, 8, 16, 24, 32};
	for (int i = 0; i < validDepths.length; i++) {
		new ImageData(1, 1, validDepths[i], new PaletteData(new RGB[] {new RGB(0, 0, 0)}), 1, new byte[] {});
	};
	
	// illegal argument, data is null
	try {
		new ImageData(1, 1, 8, new PaletteData(new RGB[] {new RGB(0, 0, 0)}), 4, null);
		fail("No exception thrown for null data");
	} catch (IllegalArgumentException e) {
	}
	
	// divide by zero exception if scanlinePad == 0
	try {
		new ImageData(1, 1, 8, new PaletteData(new RGB[] {new RGB(0, 0, 0)}), 0, new byte[] {});
		fail("No exception thrown for scanlinePad == 0");
	} catch (IllegalArgumentException e) {
	}
}

public void test_ConstructorLjava_io_InputStream() {
	InputStream stream = null;
	try {
		try {
			new ImageData(stream);
			fail("No exception thrown for InputStream == null");
		} catch (IllegalArgumentException e) {
		}
		
		stream = SwtTestCase.class.getResourceAsStream("empty.txt");
		try {
			new ImageData(stream);
			fail("No exception thrown for invalid InputStream");
		} catch (SWTException e) {
		}
	
		int numFormats = SwtTestCase.imageFormats.length;
		String fileName = SwtTestCase.imageFilenames[0];
		for (int i=0; i<numFormats; i++) {
			String format = SwtTestCase.imageFormats[i];
			stream = SwtTestCase.class.getResourceAsStream(fileName + "." + format);
			new ImageData(stream);
		}
	} finally {
		try {
			stream.close();
		} catch (Exception e) {
		}
	}
}

public void test_ConstructorLjava_lang_String() {
	String filename = null;
	try {
		new ImageData(filename);
		fail("No exception thrown for filename == null");
	} catch (IllegalArgumentException e) {
	}
	// j2se and j2me(cdc) can load from a filename but, j2me(cldc) throws an exception
}

public void test_clone() {
	InputStream stream = null;
	try {
		stream = SwtTestCase.class.getResourceAsStream(SwtTestCase.imageFilenames[0] + "." + SwtTestCase.imageFormats[0]);
		ImageLoader loader = new ImageLoader();
		ImageData data1 = loader.load(stream)[0];
		ImageData data2 = (ImageData) data1.clone();
		// imageData does not implement an equals(Object) method
		assertEquals(":a:", data1.alpha, data2.alpha);
		assertEquals(":b:", data1.alphaData, data2.alphaData);
		assertEquals(":c:", data1.bytesPerLine, data2.bytesPerLine);
		assertEquals(":d:", data1.data, data2.data);
		assertEquals(":e:", data1.delayTime, data2.delayTime);
		assertEquals(":f:", data1.depth, data2.depth);
		assertEquals(":g:", data1.disposalMethod, data2.disposalMethod);
		assertEquals(":h:", data1.height, data2.height);
		assertEquals(":i:", data1.maskData, data2.maskData);
		assertEquals(":j:", data1.maskPad, data2.maskPad);
		assertEquals(":k:", data1.palette, data2.palette);
		assertEquals(":l:", data1.scanlinePad, data2.scanlinePad);
		assertEquals(":m:", data1.transparentPixel, data2.transparentPixel);
		assertEquals(":n:", data1.type, data2.type);
		assertEquals(":o:", data1.width, data2.width);
		assertEquals(":p:", data1.x, data2.x);
		assertEquals(":q:", data1.y, data2.y);
	} finally {
		try {
			stream.close();
		} catch (Exception e) {
		}
	}
}

public void test_getAlphaII() {
	warnUnimpl("Test test_getAlphaII not written");
}

public void test_getAlphasIII$BI() {
	warnUnimpl("Test test_getAlphasIII$BI not written");
}

public void test_getPixelII() {
	warnUnimpl("Test test_getPixelII not written");
}

public void test_getPixelsIII$BI() {
	warnUnimpl("Test test_getPixelsIII$BI not written");
}

public void test_getPixelsIII$II() {
	warnUnimpl("Test test_getPixelsIII$II not written");
}

public void test_getRGBs() {
	warnUnimpl("Test test_getRGBs not written");
}

public void test_getTransparencyMask() {
	warnUnimpl("Test test_getTransparencyMask not written");
}

public void test_getTransparencyType() {
	warnUnimpl("Test test_getTransparencyType not written");
}

public void test_internal_newIIILorg_eclipse_swt_graphics_PaletteDataI$BI$B$BIIIIIII() {
	// javadoc states:
	// <b>IMPORTANT:</b> This method is <em>not</em> part of the public
	// API for <code>ImageData</code>. It is marked public only so that it
	// can be shared within the packages provided by SWT. It is subject
	// to change without notice, and should never be called from
	// application code.
}

public void test_scaledToII() {
	warnUnimpl("Test test_scaledToII not written");
}

public void test_setAlphaIII() {
	warnUnimpl("Test test_setAlphaIII not written");
}

public void test_setAlphasIII$BI() {
	warnUnimpl("Test test_setAlphasIII$BI not written");
}

public void test_setPixelIII() {
	warnUnimpl("Test test_setPixelIII not written");
}

public void test_setPixelsIII$BI() {
	warnUnimpl("Test test_setPixelsIII$BI not written");
}

public void test_setPixelsIII$II() {
	warnUnimpl("Test test_setPixelsIII$II not written");
}

public static Test suite() {
	TestSuite suite = new TestSuite();
	java.util.Vector methodNames = methodNames();
	java.util.Enumeration e = methodNames.elements();
	while (e.hasMoreElements()) {
		suite.addTest(new Test_org_eclipse_swt_graphics_ImageData((String)e.nextElement()));
	}
	return suite;
}
public static java.util.Vector methodNames() {
	java.util.Vector methodNames = new java.util.Vector();
	methodNames.addElement("test_ConstructorIIILorg_eclipse_swt_graphics_PaletteData");
	methodNames.addElement("test_ConstructorIIILorg_eclipse_swt_graphics_PaletteDataI$B");
	methodNames.addElement("test_ConstructorLjava_io_InputStream");
	methodNames.addElement("test_ConstructorLjava_lang_String");
	methodNames.addElement("test_clone");
	methodNames.addElement("test_getAlphaII");
	methodNames.addElement("test_getAlphasIII$BI");
	methodNames.addElement("test_getPixelII");
	methodNames.addElement("test_getPixelsIII$BI");
	methodNames.addElement("test_getPixelsIII$II");
	methodNames.addElement("test_getRGBs");
	methodNames.addElement("test_getTransparencyMask");
	methodNames.addElement("test_getTransparencyType");
	methodNames.addElement("test_internal_newIIILorg_eclipse_swt_graphics_PaletteDataI$BI$B$BIIIIIII");
	methodNames.addElement("test_scaledToII");
	methodNames.addElement("test_setAlphaIII");
	methodNames.addElement("test_setAlphasIII$BI");
	methodNames.addElement("test_setPixelIII");
	methodNames.addElement("test_setPixelsIII$BI");
	methodNames.addElement("test_setPixelsIII$II");
	return methodNames;
}
protected void runTest() throws Throwable {
	if (getName().equals("test_ConstructorIIILorg_eclipse_swt_graphics_PaletteData")) test_ConstructorIIILorg_eclipse_swt_graphics_PaletteData();
	else if (getName().equals("test_ConstructorIIILorg_eclipse_swt_graphics_PaletteDataI$B")) test_ConstructorIIILorg_eclipse_swt_graphics_PaletteDataI$B();
	else if (getName().equals("test_ConstructorLjava_io_InputStream")) test_ConstructorLjava_io_InputStream();
	else if (getName().equals("test_ConstructorLjava_lang_String")) test_ConstructorLjava_lang_String();
	else if (getName().equals("test_clone")) test_clone();
	else if (getName().equals("test_getAlphaII")) test_getAlphaII();
	else if (getName().equals("test_getAlphasIII$BI")) test_getAlphasIII$BI();
	else if (getName().equals("test_getPixelII")) test_getPixelII();
	else if (getName().equals("test_getPixelsIII$BI")) test_getPixelsIII$BI();
	else if (getName().equals("test_getPixelsIII$II")) test_getPixelsIII$II();
	else if (getName().equals("test_getRGBs")) test_getRGBs();
	else if (getName().equals("test_getTransparencyMask")) test_getTransparencyMask();
	else if (getName().equals("test_getTransparencyType")) test_getTransparencyType();
	else if (getName().equals("test_internal_newIIILorg_eclipse_swt_graphics_PaletteDataI$BI$B$BIIIIIII")) test_internal_newIIILorg_eclipse_swt_graphics_PaletteDataI$BI$B$BIIIIIII();
	else if (getName().equals("test_scaledToII")) test_scaledToII();
	else if (getName().equals("test_setAlphaIII")) test_setAlphaIII();
	else if (getName().equals("test_setAlphasIII$BI")) test_setAlphasIII$BI();
	else if (getName().equals("test_setPixelIII")) test_setPixelIII();
	else if (getName().equals("test_setPixelsIII$BI")) test_setPixelsIII$BI();
	else if (getName().equals("test_setPixelsIII$II")) test_setPixelsIII$II();
}
/* custom */

void assertEquals(String message, byte expected[], byte actual[]) {
	if (expected == null && actual == null)
		return;
	boolean equal = false;
	if (expected != null && actual != null && expected.length == actual.length) {
		if (expected.length == 0)
			return;
		equal = true;
		for (int i = 0; i < expected.length; i++) {
			if (expected[i] != actual[i]) {
				equal = false;
			}
		}
	}
	if (!equal) {
		String formatted= "";
		if (message != null)
			formatted= message+" ";
		fail(formatted+"expected:<"+expected+"> but was:<"+actual+">");
	}
}

}
