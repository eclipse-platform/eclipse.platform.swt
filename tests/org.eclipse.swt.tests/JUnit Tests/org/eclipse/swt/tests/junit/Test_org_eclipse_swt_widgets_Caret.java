/*******************************************************************************
 * Copyright (c) 2000, 2025 IBM Corporation and others.
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
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageGcDrawer;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Caret;
import org.junit.Before;
import org.junit.Test;

/**
 * Automated Test Suite for class org.eclipse.swt.widgets.Caret
 *
 * @see org.eclipse.swt.widgets.Caret
 */
public class Test_org_eclipse_swt_widgets_Caret extends Test_org_eclipse_swt_widgets_Widget {

Canvas canvas;
Caret caret;

@Override
@Before
public void setUp() {
	super.setUp();
	canvas = new Canvas(shell, SWT.NULL);
	caret = new Caret(canvas, SWT.NULL);
	setWidget(caret);
}

@Test
public void test_ConstructorLorg_eclipse_swt_widgets_CanvasI() {
	assertThrows("No exception thrown for null parent", IllegalArgumentException.class, () -> new Caret(null, 0));
}

@Test
public void test_getBounds() {
	Rectangle rect = new Rectangle(0,0,30,30);
	caret.setBounds(rect);
	assertEquals(rect, caret.getBounds());

	rect = new Rectangle(0,0,30,30);
	caret.setBounds(rect);
	assertNotEquals(new Rectangle (0,0,60,70), caret.getBounds());
}

@Test
public void test_getParent() {
	assertEquals(canvas, caret.getParent());

	assertSame(caret.getDisplay(),shell.getDisplay());
}

@Test
public void test_isVisible() {
	caret.setVisible(true);
	assertFalse(caret.isVisible()); //because the shell is not visible

	caret.setVisible(false);
	assertFalse(caret.isVisible());

	caret.setVisible(true);
	canvas.setVisible(true);
	shell.setVisible(true);
	assertTrue(caret.getVisible());
	canvas.setVisible(false);
	if (SwtTestUtil.fCheckVisibility) {
		assertFalse(caret.getVisible());
	}

	shell.setVisible(false);
	canvas.setVisible(false);
	caret.setVisible(false);
	assertFalse(caret.getVisible());
}

@Test
public void test_setBoundsIIII() {
	caret.setBounds(0, 0, 30, 30);
}

@Test
public void test_setBoundsLorg_eclipse_swt_graphics_Rectangle() {
	caret.setBounds(new Rectangle(0, 0, 30, 30));

	assertThrows("No exception thrown for bounds == null", IllegalArgumentException.class, () -> caret.setBounds(null));
}

@Test
public void test_setFontLorg_eclipse_swt_graphics_Font() {
	Font font1 = caret.getFont();
	caret.setFont(font1);
	assertEquals(font1, caret.getFont());
	caret.setFont(null);

	Font font = new Font(caret.getDisplay(), SwtTestUtil.testFontName, 10, SWT.NORMAL);
	caret.setFont(font);
	assertEquals(font, caret.getFont());

	caret.setFont(null);
	font.dispose();
	assertThrows("No exception thrown for disposed font", IllegalArgumentException.class, () -> caret.setFont(font));
	caret.setFont(null);
}

@Test
public void test_setImageLorg_eclipse_swt_graphics_Image() {
	Image image1 = caret.getImage();
	caret.setImage(image1);
	assertEquals(image1, caret.getImage());

	caret.setImage(null);
	assertNull(caret.getImage());

	ImageGcDrawer noOpGcDrawer = (gc, width, height) -> {};
	Image image = new Image(shell.getDisplay(), noOpGcDrawer, 10, 10);
	caret.setImage(image);
	assertEquals(image, caret.getImage());

	caret.setImage(null);
	image.dispose();
	assertThrows("No exception thrown for disposed image", IllegalArgumentException.class, () -> caret.setImage(image));
	caret.setImage(null);
}

@Test
public void test_setVisibleZ() {
	caret.setVisible(true);
	assertTrue("Caret should be visible", caret.getVisible());

	caret.setVisible(false);
	assertFalse("Caret should not be visible", caret.getVisible());
}
}
