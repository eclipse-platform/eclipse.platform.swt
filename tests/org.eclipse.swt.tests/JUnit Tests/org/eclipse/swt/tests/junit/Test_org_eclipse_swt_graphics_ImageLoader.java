/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tests.junit;


import java.io.*;

import junit.framework.*;
import junit.textui.*;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;

/**
 * Automated Test Suite for class org.eclipse.swt.graphics.ImageLoader
 *
 * @see org.eclipse.swt.graphics.ImageLoader
 */
public class Test_org_eclipse_swt_graphics_ImageLoader extends SwtTestCase {

public Test_org_eclipse_swt_graphics_ImageLoader(String name) {
	super(name);
}

public static void main(String[] args) {
	TestRunner.run(suite());
}

public void test_Constructor() {
	new ImageLoader();
}

public void test_addImageLoaderListenerLorg_eclipse_swt_graphics_ImageLoaderListener() {
	ImageLoader loader = new ImageLoader();
	ImageLoaderListener loaderListener = new ImageLoaderListener() {
		public void imageDataLoaded(ImageLoaderEvent e) {
			loaderListenerCalled = true;
		}
	};
	
	try {
		loader.addImageLoaderListener(null);
		fail("No exception thrown for addImageLoaderListener with null argument");
	} catch (IllegalArgumentException e) {
	}

	assertFalse(":a:", loader.hasListeners());
	loader.addImageLoaderListener(loaderListener);
	assertTrue(":b:", loader.hasListeners());

	loaderListenerCalled = false;
	InputStream stream = SwtTestCase.class.getResourceAsStream("interlaced_target.png");	
	loader.load(stream);
	try {
		stream.close();
	} catch (IOException e) {}
	assertTrue(":c:", loaderListenerCalled);

	loaderListenerCalled = false;
	stream = SwtTestCase.class.getResourceAsStream("target.png");	
	loader.load(stream);
	try {
		stream.close();
	} catch (IOException e) {}
	assertFalse(":d:", loaderListenerCalled);

	loaderListenerCalled = false;
	loader.notifyListeners(new ImageLoaderEvent(loader, loader.data[0], 0, true));
	assertTrue(":e:", loaderListenerCalled);

	loader.removeImageLoaderListener(loaderListener);
	assertFalse(":f:", loader.hasListeners());
}

public void test_hasListeners() {
	// tested in addImageLoaderListener method
}

public void test_loadLjava_io_InputStream() {
	ImageLoader loader = new ImageLoader();
	InputStream stream = null;
	try {
		try {
			loader.load(stream);
			fail("No exception thrown for load inputStream == null");
		} catch (IllegalArgumentException e) {
		}
		
		stream = SwtTestCase.class.getResourceAsStream("empty.txt");
		try {
			loader.load(stream);
			try {
				stream.close();
			} catch (IOException e) {}
			fail("No exception thrown for load from invalid inputStream");
		} catch (SWTException e) {
		}
	
		int numFormats = SwtTestCase.imageFormats.length;
		String fileName = SwtTestCase.imageFilenames[0];
		for (int i=0; i<numFormats; i++) {
			String format = SwtTestCase.imageFormats[i];
			stream = SwtTestCase.class.getResourceAsStream(fileName + "." + format);
			loader.load(stream);
			try {
				stream.close();
			} catch (IOException e) {}
		}
	} finally {
		try {
			stream.close();
		} catch (Exception e) {
		}
	}
}

public void test_loadLjava_lang_String() {
	ImageLoader loader = new ImageLoader();
	String filename = null;
	try {
		loader.load(filename);
		fail("No exception thrown for load filename == null");
	} catch (IllegalArgumentException e) {
	}
	// j2se and j2me(cdc) can load from a filename but, j2me(cldc) throws an exception
}

public void test_notifyListenersLorg_eclipse_swt_graphics_ImageLoaderEvent() {
	// tested in addImageLoaderListener method
}

public void test_removeImageLoaderListenerLorg_eclipse_swt_graphics_ImageLoaderListener() {
	// tested in addImageLoaderListener method
}

public void test_saveLjava_io_OutputStreamI() {
	ImageLoader loader = new ImageLoader();
	ByteArrayOutputStream outStream = null;
	InputStream inStream = null;
	try {
		try {
			loader.save(outStream, 0);
			fail("No exception thrown for save outputStream == null");
		} catch (IllegalArgumentException e) {
		}
	
		outStream = new ByteArrayOutputStream();
		try {
			loader.save(outStream, -1);
			fail("No exception thrown for save to invalid outputStream format");
		} catch (SWTException e) {
		}
		boolean jpgSupported = false;
		for (int i=0; i<imageFormats.length; i++) {
			if (imageFormats[i].equals("jpg")) {
				jpgSupported = true;
				break;
			}
		}
		if (jpgSupported) {
			String filename = SwtTestCase.imageFilenames[0];
			// must use jpg since save is not implemented yet in png format		
			String filetype = "jpg";
			inStream = SwtTestCase.class.getResourceAsStream(filename + "." + filetype);	
			loader.load(inStream);
			try {
				inStream.close();
			} catch (IOException e) {}
			for (int i = 0; i < imageFormats.length; i++) {
				if (imageFormats[i].equals(filetype)) {
					// save using the appropriate format
					loader.save(outStream, i);
					break;
				}
			}
		}
	} finally {
		try {
			outStream.close();
		} catch (Exception e) {
		}
	}
}

public void test_saveLjava_lang_StringI() {
	ImageLoader loader = new ImageLoader();
	String filename = null;
	try {
		loader.save(filename, 0);
		fail("No exception thrown for save filename == null");
	} catch (IllegalArgumentException e) {
	}
	// j2se and j2me(cdc) can load from a filename but, j2me(cldc) throws an exception
}

public static Test suite() {
	TestSuite suite = new TestSuite();
	java.util.Vector methodNames = methodNames();
	java.util.Enumeration e = methodNames.elements();
	while (e.hasMoreElements()) {
		suite.addTest(new Test_org_eclipse_swt_graphics_ImageLoader((String)e.nextElement()));
	}
	return suite;
}
public static java.util.Vector methodNames() {
	java.util.Vector methodNames = new java.util.Vector();
	methodNames.addElement("test_Constructor");
	methodNames.addElement("test_addImageLoaderListenerLorg_eclipse_swt_graphics_ImageLoaderListener");
	methodNames.addElement("test_hasListeners");
	methodNames.addElement("test_loadLjava_io_InputStream");
	methodNames.addElement("test_loadLjava_lang_String");
	methodNames.addElement("test_notifyListenersLorg_eclipse_swt_graphics_ImageLoaderEvent");
	methodNames.addElement("test_removeImageLoaderListenerLorg_eclipse_swt_graphics_ImageLoaderListener");
	methodNames.addElement("test_saveLjava_io_OutputStreamI");
	methodNames.addElement("test_saveLjava_lang_StringI");
	return methodNames;
}
protected void runTest() throws Throwable {
	if (getName().equals("test_Constructor")) test_Constructor();
	else if (getName().equals("test_addImageLoaderListenerLorg_eclipse_swt_graphics_ImageLoaderListener")) test_addImageLoaderListenerLorg_eclipse_swt_graphics_ImageLoaderListener();
	else if (getName().equals("test_hasListeners")) test_hasListeners();
	else if (getName().equals("test_loadLjava_io_InputStream")) test_loadLjava_io_InputStream();
	else if (getName().equals("test_loadLjava_lang_String")) test_loadLjava_lang_String();
	else if (getName().equals("test_notifyListenersLorg_eclipse_swt_graphics_ImageLoaderEvent")) test_notifyListenersLorg_eclipse_swt_graphics_ImageLoaderEvent();
	else if (getName().equals("test_removeImageLoaderListenerLorg_eclipse_swt_graphics_ImageLoaderListener")) test_removeImageLoaderListenerLorg_eclipse_swt_graphics_ImageLoaderListener();
	else if (getName().equals("test_saveLjava_io_OutputStreamI")) test_saveLjava_io_OutputStreamI();
	else if (getName().equals("test_saveLjava_lang_StringI")) test_saveLjava_lang_StringI();
}
/* custom */
boolean loaderListenerCalled;
}
