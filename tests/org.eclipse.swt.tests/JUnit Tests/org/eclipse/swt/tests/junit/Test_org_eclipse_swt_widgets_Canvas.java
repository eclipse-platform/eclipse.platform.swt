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


import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import junit.framework.*;
import junit.textui.*;

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

protected void tearDown() {
	super.tearDown();
}

protected void setWidget(Widget w) {
	if (!canvas.isDisposed())
		canvas.dispose();
	canvas = (Canvas)w;
	super.setWidget(w);
}

public void test_ConstructorLorg_eclipse_swt_widgets_CompositeI() {
	Canvas newCanvas;
	try {
		newCanvas = new Canvas(null, SWT.NONE);
		fail("No exception thrown for parent == null");
	}
	catch (IllegalArgumentException e) {
	}
}

public void test_getCaret() {
	// tested in test_setCaretLorg_eclipse_swt_widgets_Caret
}

public void test_scrollIIIIIIZ() {
	warnUnimpl("Test test_scrollIIIIIIZ not written");
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
	warnUnimpl("Test test_setFontLorg_eclipse_swt_graphics_Font not written");
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
	methodNames.addAll(Test_org_eclipse_swt_widgets_Composite.methodNames()); // add superclass method names
	return methodNames;
}
protected void runTest() throws Throwable {
	if (getName().equals("test_ConstructorLorg_eclipse_swt_widgets_CompositeI")) test_ConstructorLorg_eclipse_swt_widgets_CompositeI();
	else if (getName().equals("test_getCaret")) test_getCaret();
	else if (getName().equals("test_scrollIIIIIIZ")) test_scrollIIIIIIZ();
	else if (getName().equals("test_setCaretLorg_eclipse_swt_widgets_Caret")) test_setCaretLorg_eclipse_swt_widgets_Caret();
	else if (getName().equals("test_setFontLorg_eclipse_swt_graphics_Font")) test_setFontLorg_eclipse_swt_graphics_Font();
	else super.runTest();
}
}
