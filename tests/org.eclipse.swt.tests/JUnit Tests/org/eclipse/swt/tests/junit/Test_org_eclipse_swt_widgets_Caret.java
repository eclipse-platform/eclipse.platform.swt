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


import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Caret;

/**
 * Automated Test Suite for class org.eclipse.swt.widgets.Caret
 *
 * @see org.eclipse.swt.widgets.Caret
 */
public class Test_org_eclipse_swt_widgets_Caret extends Test_org_eclipse_swt_widgets_Widget {

Canvas canvas;
Caret caret;

public Test_org_eclipse_swt_widgets_Caret(String name) {
	super(name);
}

@Override
protected void setUp() {
	super.setUp();
	canvas = new Canvas(shell, SWT.NULL);
	caret = new Caret(canvas, SWT.NULL);
	setWidget(caret);
}

public void test_ConstructorLorg_eclipse_swt_widgets_CanvasI() {
	try {
		new Caret(null, 0);
		fail("No exception thrown for parent == null");
	}
	catch (IllegalArgumentException e) {
	}
}

public void test_getBounds() {
	Rectangle rect = new Rectangle(0,0,30,30);
	caret.setBounds(rect);
	assertTrue(caret.getBounds().equals(rect));

	rect = new Rectangle(0,0,30,30);
	caret.setBounds(rect);
	assertTrue(!caret.getBounds().equals(new Rectangle (0,0,60,70)));
}

public void test_getParent() {
	assertEquals(canvas, caret.getParent());

	assertTrue(caret.getDisplay()==shell.getDisplay());
}

public void test_isVisible() {
	caret.setVisible(true);
	assertTrue(!caret.isVisible()); //because the shell is not visible

	caret.setVisible(false);
	assertTrue(!caret.isVisible());

	caret.setVisible(true);
	canvas.setVisible(true);
	shell.setVisible(true);
	assertTrue(caret.getVisible() == true);
	canvas.setVisible(false);
	if (SwtTestUtil.fCheckVisibility) {
		assertTrue(!caret.getVisible());
	}

	shell.setVisible(false);
	canvas.setVisible(false);
	caret.setVisible(false);
	assertTrue(!caret.getVisible());
}

public void test_setBoundsIIII() {
	caret.setBounds(0, 0, 30, 30);
}

public void test_setBoundsLorg_eclipse_swt_graphics_Rectangle() {
	caret.setBounds(new Rectangle(0,0,30,30));

	try {
		caret.setBounds(null);
		fail("No exception thrown for bounds == null");
	}
	catch (IllegalArgumentException e) {
	}
}

public void test_setFontLorg_eclipse_swt_graphics_Font() {
	Font font = caret.getFont();
	caret.setFont(font);
	assertEquals(font, caret.getFont());
	caret.setFont(null);
	
	font = new Font(caret.getDisplay(), SwtTestUtil.testFontName, 10, SWT.NORMAL);
	caret.setFont(font);
	assertEquals(font, caret.getFont());

	caret.setFont(null);
	font.dispose();
	try {
		caret.setFont(font);
		caret.setFont(null);
		fail("No exception thrown for disposed font");
	} catch (IllegalArgumentException e) {
	}
}

public void test_setImageLorg_eclipse_swt_graphics_Image() {
	Image image = caret.getImage();
	caret.setImage(image);
	assertEquals(image, caret.getImage());

	caret.setImage(null);
	assertNull(caret.getImage());

	image = new Image(shell.getDisplay(), 10, 10);
	caret.setImage(image);
	assertEquals(image, caret.getImage());

	caret.setImage(null);
	image.dispose();
	try {
		caret.setImage(image);
		caret.setImage(null);
		fail("No exception thrown for disposed image");
	} catch (IllegalArgumentException e) {
	}
}

public void test_setVisibleZ() {
	caret.setVisible(true);
	assertTrue("Caret should be visible", caret.getVisible()==true);

	caret.setVisible(false);
	assertTrue("Caret should not be visible", caret.getVisible()==false);
}
}
