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


import java.io.*;

import junit.framework.*;
import junit.textui.*;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;

/**
 * Automated Performance Test Suite for class org.eclipse.swt.graphics.ImageLoader
 *
 * @see org.eclipse.swt.graphics.ImageLoader
 */
public class Test_org_eclipse_swt_graphics_ImageLoader extends SwtPerformanceTestCase {
	static int COUNT = 1000;

public Test_org_eclipse_swt_graphics_ImageLoader(String name) {
	super(name);
}

public static void main(String[] args) {
	TestRunner.run(suite());
}

public void test_Constructor() {
	ImageLoader loader = new ImageLoader();
}

public void test_addImageLoaderListenerLorg_eclipse_swt_graphics_ImageLoaderListener() {
	ImageLoader loader = new ImageLoader();
	ImageLoaderListener loaderListener = new ImageLoaderListener() {
		public void imageDataLoaded(ImageLoaderEvent e) {
		};
	};
	
	startMeasuring();
	for (int i = 0; i < COUNT; i++) {
		loader.addImageLoaderListener(loaderListener);
	}
	stopMeasuring();
	
	commitMeasurements();
	assertPerformance();
}

public void test_hasListeners() {
	ImageLoader loader = new ImageLoader();
	
	startMeasuring();
	for (int i = 0; i < COUNT; i++) {
		loader.hasListeners();	// no listeners
	}
	stopMeasuring();
	
	commitMeasurements();
	assertPerformance();
	
	loader.addImageLoaderListener(new ImageLoaderListener() {
		public void imageDataLoaded(ImageLoaderEvent e) {
		};
	});
	
	startMeasuring();
	for (int i = 0; i < COUNT; i++) {
		loader.hasListeners();	// has listener
	}
	stopMeasuring();
	
	commitMeasurements();
	assertPerformance();
}

public void test_loadLjava_io_InputStream() {
	// TODO
}

public void test_loadLjava_lang_String() {
	// j2se and j2me(cdc) can load from a filename but, j2me(cldc) throws an exception
	if (isJ2ME()) return;

	ImageLoader loader = new ImageLoader();
	String fileName = getPath(imageFilenames[0] + "." + imageFormats[0]);
	
	startMeasuring();
	for (int i = 0; i < COUNT; i++) {
		loader.load(fileName);
	}
	stopMeasuring();
	
	commitMeasurements();
	assertPerformance();
}

public void test_notifyListenersLorg_eclipse_swt_graphics_ImageLoaderEvent() {
	ImageLoader loader = new ImageLoader();
	ImageLoaderEvent event = new ImageLoaderEvent(loader, null, 0, true);
	ImageLoaderListener loaderListener = new ImageLoaderListener() {
		public void imageDataLoaded(ImageLoaderEvent e) {
		};
	};
	
	startMeasuring();
	for (int i = 0; i < COUNT; i++) {
		loader.notifyListeners(event);
	}
	stopMeasuring();
	
	commitMeasurements();
	assertPerformance();
}

public void test_removeImageLoaderListenerLorg_eclipse_swt_graphics_ImageLoaderListener() {
	ImageLoader loader = new ImageLoader();
	ImageLoaderListener loaderListener = new ImageLoaderListener() {
		public void imageDataLoaded(ImageLoaderEvent e) {
		};
	};
	
	startMeasuring();
	for (int i = 0; i < COUNT; i++) {
		loader.removeImageLoaderListener(loaderListener);
	}
	stopMeasuring();
	
	commitMeasurements();
	assertPerformance();
}

public void test_saveLjava_io_OutputStreamI() {
	ImageLoader loader = new ImageLoader();
	boolean jpgSupported = false;
	for (int i=0; i<imageFormats.length; i++) {
		if (imageFormats[i].equals("jpg")) {
			jpgSupported = true;
			break;
		}
	}
	if (!jpgSupported) return;
	
	String filename = imageFilenames[0];
	// must use jpg since save is not implemented yet in png format		
	String filetype = "jpg";
	FileInputStream inStream = null;
	try {
		inStream = new FileInputStream(getPath(filename + "." + filetype));
	} catch (FileNotFoundException e1) {
		e1.printStackTrace();
	}	
	loader.load(inStream);
	try {
		inStream.close();
	} catch (IOException e) {
		e.printStackTrace();
	}
	ByteArrayOutputStream outStream = new ByteArrayOutputStream();
	try {
		for (int i = 0; i < imageFormats.length; i++) {
			if (imageFormats[i].equals(filetype)) {

				startMeasuring();
				for (int j = 0; j < COUNT; j++) {
					loader.save(outStream, i);
				}
				stopMeasuring();
				
				commitMeasurements();
				assertPerformance();
				
				break;
			}
		}
	} finally {
		try {
			outStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

public void test_saveLjava_lang_StringI() {
	// j2se and j2me(cdc) can load from a filename but, j2me(cldc) throws an exception
	if (isJ2ME()) return;

	ImageLoader loader = new ImageLoader();
	String name = getPath(imageFilenames[0] + "." + imageFormats[0]);
	InputStream inStream = null;
	try {
		inStream = new FileInputStream(name);
	} catch (FileNotFoundException e) {
		e.printStackTrace();
	}	
	loader.load(inStream);
	try {
		inStream.close();
	} catch (IOException e2) {
		e2.printStackTrace();
	}

	OutputStream outStream = new ByteArrayOutputStream();
	
	try {
		startMeasuring();	// gif
		for (int i = 0; i < COUNT; i++) {
			loader.save(outStream, SWT.IMAGE_GIF);
		}
		stopMeasuring();
		
		commitMeasurements();
		assertPerformance();
	
		startMeasuring();	// ico
		for (int i = 0; i < COUNT; i++) {
			loader.save(outStream, SWT.IMAGE_ICO);
		}
		stopMeasuring();
		
		commitMeasurements();
		assertPerformance();
		
		startMeasuring();	// jpg
		for (int i = 0; i < COUNT; i++) {
			loader.save(outStream, SWT.IMAGE_JPEG);
		}
		stopMeasuring();
		
		commitMeasurements();
		assertPerformance();
	} finally {
		try {
			outStream.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
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
}
