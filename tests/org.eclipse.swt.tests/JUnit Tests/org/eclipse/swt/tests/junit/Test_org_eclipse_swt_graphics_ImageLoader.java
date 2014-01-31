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


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import junit.framework.TestCase;

import org.eclipse.swt.SWTException;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.graphics.ImageLoaderEvent;
import org.eclipse.swt.graphics.ImageLoaderListener;

/**
 * Automated Test Suite for class org.eclipse.swt.graphics.ImageLoader
 *
 * @see org.eclipse.swt.graphics.ImageLoader
 */
public class Test_org_eclipse_swt_graphics_ImageLoader extends TestCase {

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
	InputStream stream = SwtTestUtil.class.getResourceAsStream("interlaced_target.png");	
	loader.load(stream);
	try {
		stream.close();
	} catch (IOException e) {}
	assertTrue(":c:", loaderListenerCalled);

	loaderListenerCalled = false;
	stream = SwtTestUtil.class.getResourceAsStream("target.png");	
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

public void test_loadLjava_io_InputStream() {
	ImageLoader loader = new ImageLoader();
	InputStream stream = null;
	try {
		try {
			loader.load(stream);
			fail("No exception thrown for load inputStream == null");
		} catch (IllegalArgumentException e) {
		}
		
		stream = SwtTestUtil.class.getResourceAsStream("empty.txt");
		try {
			loader.load(stream);
			try {
				stream.close();
			} catch (IOException e) {}
			fail("No exception thrown for load from invalid inputStream");
		} catch (SWTException e) {
		}
	
		int numFormats = SwtTestUtil.imageFormats.length;
		String fileName = SwtTestUtil.imageFilenames[0];
		for (int i=0; i<numFormats; i++) {
			String format = SwtTestUtil.imageFormats[i];
			stream = SwtTestUtil.class.getResourceAsStream(fileName + "." + format);
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
		for (int i=0; i<SwtTestUtil.imageFormats.length; i++) {
			if (SwtTestUtil.imageFormats[i].equals("jpg")) {
				jpgSupported = true;
				break;
			}
		}
		if (jpgSupported) {
			String filename = SwtTestUtil.imageFilenames[0];
			// must use jpg since save is not implemented yet in png format		
			String filetype = "jpg";
			inStream = SwtTestUtil.class.getResourceAsStream(filename + "." + filetype);	
			loader.load(inStream);
			try {
				inStream.close();
			} catch (IOException e) {}
			for (int i = 0; i < SwtTestUtil.imageFormats.length; i++) {
				if (SwtTestUtil.imageFormats[i].equals(filetype)) {
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

/* custom */
boolean loaderListenerCalled;
}
