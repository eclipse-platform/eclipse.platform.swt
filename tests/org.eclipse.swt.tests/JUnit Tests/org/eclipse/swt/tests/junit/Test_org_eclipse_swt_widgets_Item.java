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
package org.eclipse.swt.tests.junit;

import java.io.*;

import junit.framework.*;
import junit.textui.TestRunner;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.*;

/**
 * Automated Test Suite for class org.eclipse.swt.widgets.Item
 *
 * @see org.eclipse.swt.widgets.Item
 */
public class Test_org_eclipse_swt_widgets_Item extends Test_org_eclipse_swt_widgets_Widget {

public Test_org_eclipse_swt_widgets_Item(String name) {
	super(name);
}

public static void main(String[] args) {
	TestRunner.run(suite());
}

protected void setUp() {
	super.setUp();
	loadImages();
}

protected void tearDown() {
	super.tearDown();
	freeImages();
}

public void test_ConstructorLorg_eclipse_swt_widgets_WidgetI() {
	// abstract class
}

public void test_ConstructorLorg_eclipse_swt_widgets_WidgetII() {
	// abstract class
}

public void test_getImage() {
	// tested in test_setImageLorg_eclipse_swt_graphics_Image
}

public void test_getText() {
	// tested in test_setTextLjava_lang_String
}

public void test_setImageLorg_eclipse_swt_graphics_Image() {
	assertNull(item.getImage());
	item.setImage(images[0]);
	assertEquals(images[0], item.getImage());
	assertTrue(item.getImage() != images[1]);
	item.setImage(null);
	assertNull(item.getImage());
}

public void test_setTextLjava_lang_String() {
	String testStr = "test string";
	item.setText(testStr);
	assertTrue("a", item.getText().equals(testStr));
	item.setText("");
	assertTrue("b", item.getText().equals(""));
	try {
		item.setText(null);
		fail("No exception thrown for string == null");
	}
	catch (IllegalArgumentException e) {
	}
}

public static Test suite() {
	TestSuite suite = new TestSuite();
	java.util.Vector methodNames = methodNames();
	java.util.Enumeration e = methodNames.elements();
	while (e.hasMoreElements()) {
		suite.addTest(new Test_org_eclipse_swt_widgets_Item((String)e.nextElement()));
	}
	return suite;
}

public static java.util.Vector methodNames() {
	java.util.Vector methodNames = new java.util.Vector();
	methodNames.addElement("test_ConstructorLorg_eclipse_swt_widgets_WidgetI");
	methodNames.addElement("test_ConstructorLorg_eclipse_swt_widgets_WidgetII");
	methodNames.addElement("test_getImage");
	methodNames.addElement("test_getText");
	methodNames.addElement("test_setImageLorg_eclipse_swt_graphics_Image");
	methodNames.addElement("test_setTextLjava_lang_String");
	methodNames.addAll(Test_org_eclipse_swt_widgets_Widget.methodNames()); // add superclass method names
	return methodNames;
}
protected void runTest() throws Throwable {
	if (getName().equals("test_ConstructorLorg_eclipse_swt_widgets_WidgetI")) test_ConstructorLorg_eclipse_swt_widgets_WidgetI();
	else if (getName().equals("test_ConstructorLorg_eclipse_swt_widgets_WidgetII")) test_ConstructorLorg_eclipse_swt_widgets_WidgetII();
	else if (getName().equals("test_getImage")) test_getImage();
	else if (getName().equals("test_getText")) test_getText();
	else if (getName().equals("test_setImageLorg_eclipse_swt_graphics_Image")) test_setImageLorg_eclipse_swt_graphics_Image();
	else if (getName().equals("test_setTextLjava_lang_String")) test_setTextLjava_lang_String();
	else super.runTest();
}

/* custom */
Item item;
Image[] images = new Image [SwtTestCase.imageFormats.length*SwtTestCase.imageFilenames.length];

private void loadImages() {
	int numFormats = SwtTestCase.imageFormats.length;
	int numFiles = SwtTestCase.imageFilenames.length;
	for (int i=0; i<numFormats; i++) {
		String format = SwtTestCase.imageFormats[i];
		int index = i*numFiles;
		for (int j=0; j<numFiles; j++){
			String fileName = SwtTestCase.imageFilenames[j];
			InputStream  resource = this.getClass().getResourceAsStream(fileName + "." + format);
			images [index+j] = new Image (shell.getDisplay(), resource);
			try {
				resource.close();
			} catch (IOException e) {
				// continue;
			}
		}
	}
}
protected void setWidget(Widget widget) {
	item = (Item) widget;
	super.setWidget(widget);
}

private void freeImages() {
	for (int i=0; i<images.length; i++) {
		if (images[i] != null)
			images[i].dispose();
	}
}
}
