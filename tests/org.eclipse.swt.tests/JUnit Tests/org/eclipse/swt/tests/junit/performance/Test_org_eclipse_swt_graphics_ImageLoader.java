/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tests.junit.performance;


import java.io.*;

import junit.framework.*;
import junit.textui.*;

import org.eclipse.swt.graphics.*;
import org.eclipse.test.performance.PerformanceMeter;

/**
 * Automated Performance Test Suite for class org.eclipse.swt.graphics.ImageLoader
 *
 * @see org.eclipse.swt.graphics.ImageLoader
 */
public class Test_org_eclipse_swt_graphics_ImageLoader extends SwtPerformanceTestCase {

public Test_org_eclipse_swt_graphics_ImageLoader(String name) {
	super(name);
}

public static void main(String[] args) {
	TestRunner.run(suite());
}

public void test_loadLjava_lang_String() {
	final int COUNT = 4500;
	
	// j2se and j2me(cdc) can load from a filename but, j2me(cldc) throws an exception
	if (isJ2ME()) return;

	ImageLoader loader = new ImageLoader();
	String fileName = getPath(imageFilenames[0] + "." + imageFormats[0]);
	
	PerformanceMeter meter = createMeter("ImageLoader load(String)");
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		loader.load(fileName);
	}
	meter.stop();
	
	disposeMeter(meter);
}

public void test_saveLjava_io_OutputStreamI() {
	final int COUNT = 30000;
	
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

				PerformanceMeter meter = createMeter("ImageLoader save(OutputStream,I) - " + i);
				meter.start();
				for (int j = 0; j < COUNT; j++) {
					loader.save(outStream, i);
				}
				meter.stop();
				
				disposeMeter(meter);
				
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
	methodNames.addElement("test_loadLjava_lang_String");
	methodNames.addElement("test_saveLjava_io_OutputStreamI");
	return methodNames;
}
protected void runTest() throws Throwable {
	if (getName().equals("test_loadLjava_lang_String")) test_loadLjava_lang_String();
	else if (getName().equals("test_saveLjava_io_OutputStreamI")) test_saveLjava_io_OutputStreamI();
}
}
