/*******************************************************************************
 * Copyright (c) 2000, 2017 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tests.junit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.InputStream;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Decorations;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Widget;
import org.junit.Test;

/**
 * Automated Test Suite for class org.eclipse.swt.widgets.Decorations
 *
 * @see org.eclipse.swt.widgets.Decorations
 */
public class Test_org_eclipse_swt_widgets_Decorations extends Test_org_eclipse_swt_widgets_Canvas {

@Override
@Test
public void test_ConstructorLorg_eclipse_swt_widgets_CompositeI() {
	// do nothing, even though this is not an abstract class, it was never meant to
	// be instantiated
}

@Override
@Test
public void test_computeTrimIIII() {
	decorations.computeTrim(0,0,0,0);
	decorations.computeTrim(0,0,10,20);
}

@Override
@Test
public void test_getClientArea() {
	Rectangle rect = decorations.getClientArea();
	assertTrue(":a:", rect.height >= 0);
	assertTrue(":b:", rect.width >= 0);
}

@Test
public void test_getDefaultButton() {
	Button button = new Button(decorations, SWT.PUSH);
	decorations.setDefaultButton(button);
	assertTrue(":a:", decorations.getDefaultButton() == button);
}

@Test
public void test_getImage() {
	Image[] cases = {null, new Image(null, 100, 100)};
	for(int i=0; i<cases.length; i++){
		decorations.setImage(cases[i]);
	 	assertEquals(decorations.getImage(), cases[i]);
	 	if (cases[i]!=null)
	  		cases[i].dispose();
	}
}

@Override
@Test
public void test_getLocation() {
	decorations.setLocation(10,15);
	assertTrue(":a:", decorations.getLocation().x == 10);
	assertTrue(":b:", decorations.getLocation().y == 15);
}

@Test
public void test_getMenuBar() {
	assertNull(":a:", decorations.getMenuBar());
	Menu bar = new Menu (decorations, SWT.BAR);
	decorations.setMenuBar (bar);
	assertTrue(":b:", decorations.getMenuBar() == bar);
}

@Test
public void test_getText() {
	decorations.setText("test");
	assertTrue(":a:", decorations.getText().equals("test"));
}

@Override
@Test
public void test_isReparentable() {
	/* Decorations are not reparentable, see win32 implementation of isReparentable() */
	assertFalse(decorations.isReparentable());
}

@Test
public void test_setDefaultButtonLorg_eclipse_swt_widgets_Button() {
	assertNull(decorations.getDefaultButton());
	Button button = new Button(decorations, SWT.NULL);
	decorations.setDefaultButton(button);
	assertTrue("button not default", decorations.getDefaultButton() == button);
	if (SwtTestUtil.fCheckBogusTestCases) {
		decorations.setDefaultButton(null);
		assertNull(decorations.getDefaultButton());
	}
}

@Test
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

@Test
public void test_setMaximizedZ() {
	decorations.setMaximized(false);
	assertFalse(":1:", decorations.getMaximized());
	decorations.setMaximized(true);
	assertTrue(":2:", decorations.getMaximized());
	assertFalse(":3:", decorations.getMinimized());
}

@Test
public void test_setMenuBarLorg_eclipse_swt_widgets_Menu() {
	assertNull(decorations.getMenu());
	Menu testMenu = new Menu(decorations);
	decorations.setMenu(testMenu);
	assertTrue("Incorrect menu", decorations.getMenu() == testMenu);
	decorations.setMenu(null);
	assertNull(decorations.getMenu());
}

@Test
public void test_setMinimizedZ() {
	decorations.setMinimized(false);
	assertFalse(":1:", decorations.getMinimized());
	decorations.setMinimized(true);
	assertTrue(":2:", decorations.getMinimized());
	assertFalse(":3:", decorations.getMaximized());
}

@Test
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
	assertTrue("b", decorations.getText().isEmpty());
	try {
		decorations.setText(null);
		fail("No exception thrown for string == null");
	}
	catch (IllegalArgumentException e) {
	}
}

@Override
@Test
public void test_setVisibleZ() {
	// test in subclasses
}

/* custom */
Decorations decorations;
Image[] images = new Image [SwtTestUtil.imageFormats.length*SwtTestUtil.imageFilenames.length];

@Override
protected void setWidget(Widget w) {
	if (decorations != null && !decorations.isDisposed())
		decorations.dispose();
	decorations = (Decorations)w;
	super.setWidget(w);
}

// this method must be private or protected so the auto-gen tool keeps it
private void loadImages() {
	int numFormats = SwtTestUtil.imageFormats.length;
	int numFiles = SwtTestUtil.imageFilenames.length;
	for (int i=0; i<numFormats; i++) {
		String format = SwtTestUtil.imageFormats[i];
		int index = i*numFiles;
		for (int j=0; j<numFiles; j++){
			String fileName = SwtTestUtil.imageFilenames[j];
			try (InputStream  resource = this.getClass().getResourceAsStream(fileName + "." + format)) {
				images [index+j] = new Image (shell.getDisplay(), resource);
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
