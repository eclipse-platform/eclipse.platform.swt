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


import junit.framework.*;
import junit.textui.*;

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

protected void setUp() {
}

protected void tearDown() {
}

public void test_Constructor() {
	warnUnimpl("Test test_Constructor not written");
}

public void test_addImageLoaderListenerLorg_eclipse_swt_graphics_ImageLoaderListener() {
	warnUnimpl("Test test_addImageLoaderListenerLorg_eclipse_swt_graphics_ImageLoaderListener not written");
}

public void test_hasListeners() {
	warnUnimpl("Test test_hasListeners not written");
}

public void test_loadLjava_io_InputStream() {
	warnUnimpl("Test test_loadLjava_io_InputStream not written");
}

public void test_loadLjava_lang_String() {
	warnUnimpl("Test test_loadLjava_lang_String not written");
}

public void test_notifyListenersLorg_eclipse_swt_graphics_ImageLoaderEvent() {
	warnUnimpl("Test test_notifyListenersLorg_eclipse_swt_graphics_ImageLoaderEvent not written");
}

public void test_removeImageLoaderListenerLorg_eclipse_swt_graphics_ImageLoaderListener() {
	warnUnimpl("Test test_removeImageLoaderListenerLorg_eclipse_swt_graphics_ImageLoaderListener not written");
}

public void test_saveLjava_io_OutputStreamI() {
	warnUnimpl("Test test_saveLjava_io_OutputStreamI not written");
}

public void test_saveLjava_lang_StringI() {
	warnUnimpl("Test test_saveLjava_lang_StringI not written");
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
