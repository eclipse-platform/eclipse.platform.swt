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

/**
 * Automated Test Suite for class org.eclipse.swt.widgets.Item
 *
 * @see org.eclipse.swt.widgets.Item
 */
public class Test_org_eclipse_swt_widgets_Item extends Test_org_eclipse_swt_widgets_Widget {

Item item;
protected Image[] images = new Image [3];

public Test_org_eclipse_swt_widgets_Item(String name) {
	super(name);
}

protected void setUp() {
	super.setUp();
	loadImages();
}

protected void tearDown() {
	super.tearDown();
	freeImages();
}

// this method must be private or protected so the auto-gen tool keeps it
private void loadImages() {
	java.io.InputStream in1 = this.getClass().getResourceAsStream("folder.bmp");
	java.io.InputStream in2 = this.getClass().getResourceAsStream("folderOpen.bmp");
	java.io.InputStream in3 = this.getClass().getResourceAsStream("target.bmp");
	Display display = shell.getDisplay();
		
	images [0] = new Image (display, in1);
	images [1] = new Image (display, in2);
	images [2] = new Image (display, in3);
	
	try {
		in1.close();
		in2.close();
		in3.close();
	} catch (java.io.IOException e) {
	}
}
protected void setWidget(Widget widget) {
	item = (Item) widget;
	super.setWidget(widget);
}
// this method must be private or protected so the auto-gen tool keeps it
private void freeImages() {
	for (int i=0; i<images.length; i++) {
		if (images[i] != null)
			images[i].dispose();
	}
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
	if (fCheckSwtNullExceptions) {
		try {
			item.setText(null);
			fail("No exception thrown for string == null");
		}
		catch (IllegalArgumentException e) {
		}
	}
}

public static java.util.Vector methodNames() {
	java.util.Vector methodNames = new java.util.Vector();
	methodNames.addElement("test_getImage");
	methodNames.addElement("test_getText");
	methodNames.addElement("test_setImageLorg_eclipse_swt_graphics_Image");
	methodNames.addElement("test_setTextLjava_lang_String");
	methodNames.addAll(Test_org_eclipse_swt_widgets_Widget.methodNames()); // add superclass method names
	return methodNames;
}
protected void runTest() throws Throwable {
	if (getName().equals("test_getImage")) test_getImage();
	else if (getName().equals("test_getText")) test_getText();
	else if (getName().equals("test_setImageLorg_eclipse_swt_graphics_Image")) test_setImageLorg_eclipse_swt_graphics_Image();
	else if (getName().equals("test_setTextLjava_lang_String")) test_setTextLjava_lang_String();
	else super.runTest();
}
}
