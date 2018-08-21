/*******************************************************************************
 * Copyright (c) 2000, 2015 IBM Corporation and others.
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
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Caret;
import org.eclipse.swt.widgets.Widget;
import org.junit.Before;
import org.junit.Test;

/**
 * Automated Test Suite for class org.eclipse.swt.widgets.Canvas
 *
 * @see org.eclipse.swt.widgets.Canvas
 */
public class Test_org_eclipse_swt_widgets_Canvas extends Test_org_eclipse_swt_widgets_Composite {

Canvas canvas;

@Override
@Before
public void setUp() {
	super.setUp();
	canvas = new Canvas(shell, 0);
	super.setWidget(canvas);
}

@Override
protected void setWidget(Widget w) {
	if (!canvas.isDisposed())
		canvas.dispose();
	canvas = (Canvas)w;
	super.setWidget(w);
}

@Override
@Test
public void test_ConstructorLorg_eclipse_swt_widgets_CompositeI() {
	try {
		new Canvas(null, SWT.NONE);
		fail("No exception thrown for parent == null");
	}
	catch (IllegalArgumentException e) {
	}
}

@Test
public void test_scrollIIIIIIZ() {
	canvas.scroll(100, 100, 0, 0, 50, 50, false);
	canvas.scroll(100, 100, 0, 0, 50, 50, true);

	canvas.scroll(10000, 10000, 100, 100, 500, 500, false);
	canvas.scroll(10000, 10000, 100, 100, 500, 500, true);

	canvas.scroll(-100, -100, 10, 10, 30, 30, false);
	canvas.scroll(-100, -100, 10, 10, 30, 30, true);

	canvas.scroll(10, 10, -200, -200, 100, 100, false);
	canvas.scroll(10, 10, -200, -200, 100, 100, true);

	canvas.scroll(100, 100, 50, 50, -50, -50, false);
	canvas.scroll(100, 100, 50, 50, -50, -50, true);
}

@Test
public void test_setCaretLorg_eclipse_swt_widgets_Caret() {
	int number = 5;
	Caret[] carets = new Caret[number];
	for (int i = 0; i < number; i++) {
		carets[i] = new Caret(canvas, SWT.NONE);
	}
	for (int i = 0; i < number; i++) {
		canvas.setCaret(carets[i]);
		assertEquals("Caret # " + i + "not set properly", canvas.getCaret(), carets[i]);
	}

	canvas.setCaret(null);
	assertNull("Caret should be null" , canvas.getCaret());
}

@Override
@Test
public void test_setFontLorg_eclipse_swt_graphics_Font() {
	FontData fontData = canvas.getFont().getFontData()[0];
	Font font = new Font(canvas.getDisplay(), fontData.getName(), 8, fontData.getStyle());
	canvas.setFont(font);
	assertTrue(":a:", canvas.getFont().equals(font));
	canvas.setFont(null);
	font.dispose();
}

/* custom*/
@Test
public void test_consistency_MenuDetect() {
    consistencyEvent(10, 10, 3, 0, ConsistencyUtility.MOUSE_CLICK);
}

@Test
public void test_consistency_DragDetect() {
    consistencyEvent(10, 10, 20, 20, ConsistencyUtility.MOUSE_DRAG);
}

}
