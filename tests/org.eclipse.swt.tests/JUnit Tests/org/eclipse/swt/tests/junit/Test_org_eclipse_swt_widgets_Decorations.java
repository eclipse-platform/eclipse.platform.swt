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

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.*;

/**
 * Automated Test Suite for class org.eclipse.swt.widgets.Decorations
 *
 * @see org.eclipse.swt.widgets.Decorations
 */
public class Test_org_eclipse_swt_widgets_Decorations extends Test_org_eclipse_swt_widgets_Canvas {

public Test_org_eclipse_swt_widgets_Decorations(String name) {
	super(name);
}

public static void main(String[] args) {
	TestRunner.run(suite());
}

public void test_ConstructorLorg_eclipse_swt_widgets_CompositeI() {
	// do nothing, even though this is not an abstract class, it was never meant to 
	// be instantiated
}

public void test_computeTrimIIII() {
	decorations.computeTrim(0,0,0,0);
	decorations.computeTrim(0,0,10,20);
}
public void test_dispose() {
	warnUnimpl("Test test_dispose not written");
}

public void test_getBounds() {
	warnUnimpl("Test test_getBounds not written");
}

public void test_getClientArea() {
	Rectangle rect = decorations.getClientArea();
	assertTrue(":a:", rect.height >= 0);
	assertTrue(":b:", rect.width >= 0);
}

public void test_getDefaultButton() {
	Button button = new Button(decorations, SWT.PUSH);
	decorations.setDefaultButton(button);
	assertTrue(":a:", decorations.getDefaultButton() == button);
}

public void test_getImage() {
	Image[] cases = {null, new Image(null, 100, 100)};
	for(int i=0; i<cases.length; i++){
		decorations.setImage(cases[i]);
	 	assertEquals(decorations.getImage(), cases[i]);
	 	if (cases[i]!=null)
	  		cases[i].dispose();
	}
}

public void test_getImages() {
	warnUnimpl("Test test_getImages not written");
}

public void test_getLocation() {
	decorations.setLocation(10,15);
	assertTrue(":a:", decorations.getLocation().x == 10);
	assertTrue(":b:", decorations.getLocation().y == 15);
}

public void test_getMaximized() {
	// tested in setMaximized method
}

public void test_getMenuBar() {
	assertTrue(":a:", decorations.getMenuBar() == null);
	Menu bar = new Menu (decorations, SWT.BAR);
	decorations.setMenuBar (bar);
	assertTrue(":b:", decorations.getMenuBar() == bar);
}

public void test_getMinimized() {
	// tested in setMinimized method
}

public void test_getSize() {
	// super class test sufficient
}

public void test_getText() {
	decorations.setText("test");
	assertTrue(":a:", decorations.getText().equals("test"));
}

public void test_isReparentable() {
	/* Decorations are not reparentable, see win32 implementation of isReparentable() */
	assertFalse(decorations.isReparentable());
}

public void test_setDefaultButtonLorg_eclipse_swt_widgets_Button() {
	assertNull(decorations.getDefaultButton());
	Button button = new Button(decorations, SWT.NULL);
	decorations.setDefaultButton(button);
	assertTrue("button not default", decorations.getDefaultButton() == button);
	if (fCheckBogusTestCases) {
		decorations.setDefaultButton(null);
		assertNull(decorations.getDefaultButton());
	}
}

public void test_setImageLorg_eclipse_swt_graphics_Image() {
	assertNull(":a:", decorations.getImage());
	loadImages();
	decorations.setImage(images[0]);
	assertTrue(":b:", images[0] == decorations.getImage());
	assertTrue(":c:", images[1] != decorations.getImage());
	decorations.setImage(null);
	assertNull(":d:", decorations.getImage());
	freeImages();
}

public void test_setImages$Lorg_eclipse_swt_graphics_Image() {
	warnUnimpl("Test test_setImages$Lorg_eclipse_swt_graphics_Image not written");
}

public void test_setMaximizedZ() {
	decorations.setMaximized(false);
	assertTrue(":1:", decorations.getMaximized() == false);
	decorations.setMaximized(true);
	assertTrue(":2:", decorations.getMaximized() == true);
	assertTrue(":3:", decorations.getMinimized() == false);
}

public void test_setMenuBarLorg_eclipse_swt_widgets_Menu() {
	assertNull(decorations.getMenu());
	Menu testMenu = new Menu(decorations);
	decorations.setMenu(testMenu);
	assertTrue("Incorrect menu", decorations.getMenu() == testMenu);
	decorations.setMenu(null);
	assertNull(decorations.getMenu());
}

public void test_setMinimizedZ() {
	decorations.setMinimized(false);
	assertTrue(":1:", decorations.getMinimized() == false);
	decorations.setMinimized(true);
	assertTrue(":2:", decorations.getMinimized() == true);
	assertTrue(":3:", decorations.getMaximized() == false);
}

public void test_setTextLjava_lang_String() {
	try {
		decorations.setText(null);
		fail("No exception thrown for string == null");
	}
	catch (IllegalArgumentException e) {
	}

	String testStr = "test string";
	decorations.setText(testStr);
	assertTrue("a", decorations.getText().equals(testStr));
	decorations.setText("");
	assertTrue("b", decorations.getText().equals(""));
	try {
		decorations.setText(null);
		fail("No exception thrown for string == null");
	}
	catch (IllegalArgumentException e) {
	}
}

public void test_setVisibleZ() {
	// test in subclasses
}

public static Test suite() {
	TestSuite suite = new TestSuite();
	java.util.Vector methodNames = methodNames();
	java.util.Enumeration e = methodNames.elements();
	while (e.hasMoreElements()) {
		suite.addTest(new Test_org_eclipse_swt_widgets_Decorations((String)e.nextElement()));
	}
	return suite;
}
public static java.util.Vector methodNames() {
	java.util.Vector methodNames = new java.util.Vector();
	methodNames.addElement("test_ConstructorLorg_eclipse_swt_widgets_CompositeI");
	methodNames.addElement("test_computeTrimIIII");
	methodNames.addElement("test_dispose");
	methodNames.addElement("test_getBounds");
	methodNames.addElement("test_getClientArea");
	methodNames.addElement("test_getDefaultButton");
	methodNames.addElement("test_getImage");
	methodNames.addElement("test_getImages");
	methodNames.addElement("test_getLocation");
	methodNames.addElement("test_getMaximized");
	methodNames.addElement("test_getMenuBar");
	methodNames.addElement("test_getMinimized");
	methodNames.addElement("test_getSize");
	methodNames.addElement("test_getText");
	methodNames.addElement("test_isReparentable");
	methodNames.addElement("test_setDefaultButtonLorg_eclipse_swt_widgets_Button");
	methodNames.addElement("test_setImageLorg_eclipse_swt_graphics_Image");
	methodNames.addElement("test_setImages$Lorg_eclipse_swt_graphics_Image");
	methodNames.addElement("test_setMaximizedZ");
	methodNames.addElement("test_setMenuBarLorg_eclipse_swt_widgets_Menu");
	methodNames.addElement("test_setMinimizedZ");
	methodNames.addElement("test_setTextLjava_lang_String");
	methodNames.addElement("test_setVisibleZ");
	methodNames.addAll(Test_org_eclipse_swt_widgets_Canvas.methodNames()); // add superclass method names
	return methodNames;
}
protected void runTest() throws Throwable {
	if (getName().equals("test_ConstructorLorg_eclipse_swt_widgets_CompositeI")) test_ConstructorLorg_eclipse_swt_widgets_CompositeI();
	else if (getName().equals("test_computeTrimIIII")) test_computeTrimIIII();
	else if (getName().equals("test_dispose")) test_dispose();
	else if (getName().equals("test_getBounds")) test_getBounds();
	else if (getName().equals("test_getClientArea")) test_getClientArea();
	else if (getName().equals("test_getDefaultButton")) test_getDefaultButton();
	else if (getName().equals("test_getImage")) test_getImage();
	else if (getName().equals("test_getImages")) test_getImages();
	else if (getName().equals("test_getLocation")) test_getLocation();
	else if (getName().equals("test_getMaximized")) test_getMaximized();
	else if (getName().equals("test_getMenuBar")) test_getMenuBar();
	else if (getName().equals("test_getMinimized")) test_getMinimized();
	else if (getName().equals("test_getSize")) test_getSize();
	else if (getName().equals("test_getText")) test_getText();
	else if (getName().equals("test_isReparentable")) test_isReparentable();
	else if (getName().equals("test_setDefaultButtonLorg_eclipse_swt_widgets_Button")) test_setDefaultButtonLorg_eclipse_swt_widgets_Button();
	else if (getName().equals("test_setImageLorg_eclipse_swt_graphics_Image")) test_setImageLorg_eclipse_swt_graphics_Image();
	else if (getName().equals("test_setImages$Lorg_eclipse_swt_graphics_Image")) test_setImages$Lorg_eclipse_swt_graphics_Image();
	else if (getName().equals("test_setMaximizedZ")) test_setMaximizedZ();
	else if (getName().equals("test_setMenuBarLorg_eclipse_swt_widgets_Menu")) test_setMenuBarLorg_eclipse_swt_widgets_Menu();
	else if (getName().equals("test_setMinimizedZ")) test_setMinimizedZ();
	else if (getName().equals("test_setTextLjava_lang_String")) test_setTextLjava_lang_String();
	else if (getName().equals("test_setVisibleZ")) test_setVisibleZ();
	else super.runTest();
}

/* custom */
Decorations decorations;
Image[] images = new Image [SwtTestCase.imageFormats.length*SwtTestCase.imageFilenames.length];

protected void setWidget(Widget w) {
	if (decorations != null && !decorations.isDisposed())
		decorations.dispose();
	decorations = (Decorations)w;
	super.setWidget(w);
}

// this method must be private or protected so the auto-gen tool keeps it
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

// this method must be private or protected so the auto-gen tool keeps it
private void freeImages() {
	for (int i=0; i<images.length; i++) images[i].dispose();
}
}
