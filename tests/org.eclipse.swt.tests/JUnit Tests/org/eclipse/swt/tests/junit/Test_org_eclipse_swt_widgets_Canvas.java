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


import junit.framework.*;
import junit.textui.*;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

/**
 * Automated Test Suite for class org.eclipse.swt.widgets.Canvas
 *
 * @see org.eclipse.swt.widgets.Canvas
 */
public class Test_org_eclipse_swt_widgets_Canvas extends Test_org_eclipse_swt_widgets_Composite {

Canvas canvas;

public Test_org_eclipse_swt_widgets_Canvas(String name) {
	super(name);
}

public static void main(String[] args) {
	TestRunner.run(suite());
}

protected void setUp() {
	super.setUp();
	canvas = new Canvas(shell, 0);
	super.setWidget(canvas);
}

protected void setWidget(Widget w) {
	if (!canvas.isDisposed())
		canvas.dispose();
	canvas = (Canvas)w;
	super.setWidget(w);
}

public void test_ConstructorLorg_eclipse_swt_widgets_CompositeI() {
	try {
		new Canvas(null, SWT.NONE);
		fail("No exception thrown for parent == null");
	}
	catch (IllegalArgumentException e) {
	}
}

public void test_getCaret() {
	// tested in test_setCaretLorg_eclipse_swt_widgets_Caret
}

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

public void test_setFontLorg_eclipse_swt_graphics_Font() {
	FontData fontData = canvas.getFont().getFontData()[0];
	Font font = new Font(canvas.getDisplay(), fontData.getName(), 8, fontData.getStyle());
	canvas.setFont(font);
	assertTrue(":a:", canvas.getFont().equals(font));
	canvas.setFont(null);
	font.dispose();
}

public static Test suite() {
	TestSuite suite = new TestSuite();
	java.util.Vector methodNames = methodNames();
	java.util.Enumeration e = methodNames.elements();
	while (e.hasMoreElements()) {
		suite.addTest(new Test_org_eclipse_swt_widgets_Canvas((String)e.nextElement()));
	}
	return suite;
}
public static java.util.Vector methodNames() {
	java.util.Vector methodNames = new java.util.Vector();
	methodNames.addElement("test_ConstructorLorg_eclipse_swt_widgets_CompositeI");
	methodNames.addElement("test_getCaret");
	methodNames.addElement("test_scrollIIIIIIZ");
	methodNames.addElement("test_setCaretLorg_eclipse_swt_widgets_Caret");
	methodNames.addElement("test_setFontLorg_eclipse_swt_graphics_Font");
	methodNames.addElement("test_consistency_MenuDetect");
	methodNames.addElement("test_consistency_DragDetect");
	methodNames.addAll(Test_org_eclipse_swt_widgets_Composite.methodNames()); // add superclass method names
	return methodNames;
}
protected void runTest() throws Throwable {
	if (getName().equals("test_ConstructorLorg_eclipse_swt_widgets_CompositeI")) test_ConstructorLorg_eclipse_swt_widgets_CompositeI();
	else if (getName().equals("test_getCaret")) test_getCaret();
	else if (getName().equals("test_scrollIIIIIIZ")) test_scrollIIIIIIZ();
	else if (getName().equals("test_setCaretLorg_eclipse_swt_widgets_Caret")) test_setCaretLorg_eclipse_swt_widgets_Caret();
	else if (getName().equals("test_setFontLorg_eclipse_swt_graphics_Font")) test_setFontLorg_eclipse_swt_graphics_Font();
	else if (getName().equals("test_consistency_MenuDetect")) test_consistency_MenuDetect();
	else if (getName().equals("test_consistency_DragDetect")) test_consistency_DragDetect();
	else super.runTest();
}

/* custom*/

public void test_consistency_MenuDetect() {
    consistencyEvent(10, 10, 3, 0, ConsistencyUtility.MOUSE_CLICK);
}

public void test_consistency_DragDetect() {
    consistencyEvent(10, 10, 20, 20, ConsistencyUtility.MOUSE_DRAG);
}

}
