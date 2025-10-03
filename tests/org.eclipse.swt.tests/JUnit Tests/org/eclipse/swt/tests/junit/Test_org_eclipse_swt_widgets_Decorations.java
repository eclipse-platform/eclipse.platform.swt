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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageGcDrawer;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Decorations;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Widget;
import org.junit.jupiter.api.Test;

/**
 * Automated Test Suite for class org.eclipse.swt.widgets.Decorations
 *
 * @see org.eclipse.swt.widgets.Decorations
 */
public abstract class Test_org_eclipse_swt_widgets_Decorations extends Test_org_eclipse_swt_widgets_Canvas {

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
	assertTrue(rect.height >= 0);
	assertTrue(rect.width >= 0);
}

@Test
public void test_getDefaultButton() {
	Button button = new Button(decorations, SWT.PUSH);
	decorations.setDefaultButton(button);
	assertTrue(decorations.getDefaultButton() == button);
}

@Test
public void test_getImage() {
	ImageGcDrawer noOpGcDrawer = (gc, width, height) -> {};
	Image[] cases = {null, new Image(null, noOpGcDrawer, 100, 100)};
	for (Image image : cases) {
		decorations.setImage(image);
		assertEquals(decorations.getImage(), image);
		if (image!=null)
			image.dispose();
	}
}

@Override
@Test
public void test_getLocation() {
	decorations.setLocation(10,15);
	assertEquals(10, decorations.getLocation().x);
	assertEquals(15, decorations.getLocation().y);
}

@Test
public void test_getMenuBar() {
	assertNull(decorations.getMenuBar());
	Menu bar = new Menu (decorations, SWT.BAR);
	decorations.setMenuBar (bar);
	assertTrue(decorations.getMenuBar() == bar);
}

@Test
public void test_getText() {
	decorations.setText("test");
	assertEquals("test", decorations.getText());
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
	assertTrue(decorations.getDefaultButton() == button);
	if (SwtTestUtil.fCheckBogusTestCases) {
		decorations.setDefaultButton(null);
		assertNull(decorations.getDefaultButton());
	}
}

@Test
public void test_setImageLorg_eclipse_swt_graphics_Image() {
	assertNull(decorations.getImage());
	loadImages();
	decorations.setImage(images.get(0));
	assertTrue(images.get(0) == decorations.getImage());
	assertTrue(images.get(1) != decorations.getImage());
	decorations.setImage(null);
	assertNull(decorations.getImage());
	freeImages();
}

@Test
public void test_setMaximizedZ() {
	decorations.setMaximized(false);
	assertFalse(decorations.getMaximized());
	decorations.setMaximized(true);
	assertTrue(decorations.getMaximized());
	assertFalse(decorations.getMinimized());
}

@Test
public void test_setMenuBarLorg_eclipse_swt_widgets_Menu() {
	assertNull(decorations.getMenu());
	Menu testMenu = new Menu(decorations);
	decorations.setMenu(testMenu);
	assertTrue(decorations.getMenu() == testMenu);
	decorations.setMenu(null);
	assertNull(decorations.getMenu());
}

@Test
public void test_setMinimizedZ() {
	decorations.setMinimized(false);
	assertFalse(decorations.getMinimized());
	decorations.setMinimized(true);
	assertTrue(decorations.getMinimized());
	assertFalse(decorations.getMaximized());
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
	assertEquals(testStr, decorations.getText());
	decorations.setText("");
	assertTrue(decorations.getText().isEmpty());
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
private List<Image> images = new ArrayList<>();

@Override
protected void setWidget(Widget w) {
	if (decorations != null && !decorations.isDisposed())
		decorations.dispose();
	decorations = (Decorations)w;
	super.setWidget(w);
}

// this method must be private or protected so the auto-gen tool keeps it
private void loadImages() {
	for (String format : SwtTestUtil.imageFormats) {
		for (String fileName : SwtTestUtil.imageFilenames) {
			try (InputStream  resource = this.getClass().getResourceAsStream(fileName + "." + format)) {
				images.add(new Image(shell.getDisplay(), resource));
			} catch (IOException e) {
				// continue;
			}
		}
	}
}

// this method must be private or protected so the auto-gen tool keeps it
private void freeImages() {
	for (Image image : images)
		image.dispose();
}
}
