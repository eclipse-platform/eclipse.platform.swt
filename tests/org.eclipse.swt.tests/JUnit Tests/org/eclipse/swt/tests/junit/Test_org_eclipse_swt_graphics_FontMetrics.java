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


import junit.framework.*;
import junit.textui.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.graphics.*;

/**
 * Automated Test Suite for class org.eclipse.swt.graphics.FontMetrics
 *
 * @see org.eclipse.swt.graphics.FontMetrics
 */
public class Test_org_eclipse_swt_graphics_FontMetrics extends SwtTestCase {

public Test_org_eclipse_swt_graphics_FontMetrics(String name) {
	super(name);
}

public static void main(String[] args) {
	TestRunner.run(suite());
}

protected void setUp() {
	super.setUp();
	display = Display.getDefault();
	shell = new Shell(display);
	gc = new GC(shell);
}

protected void tearDown() {
	gc.dispose();
	shell.dispose();
	super.tearDown();
}

public void test_equalsLjava_lang_Object() {
	FontMetrics fm1 = gc.getFontMetrics();
	FontMetrics fm2 = gc.getFontMetrics();
	assertTrue(fm1.equals(fm2));
}

public void test_getAscent() {
	FontMetrics fm = gc.getFontMetrics();
	fm.getAscent();
}

public void test_getAverageCharWidth() {
	FontMetrics fm = gc.getFontMetrics();
	fm.getAverageCharWidth();
}

public void test_getDescent() {
	FontMetrics fm = gc.getFontMetrics();
	fm.getDescent();
}

public void test_getHeight() {
	FontMetrics fm = gc.getFontMetrics();
	fm.getHeight();
}

public void test_getLeading() {
	FontMetrics fm = gc.getFontMetrics();
	fm.getLeading();
}

public void test_hashCode() {
	FontMetrics fm1 = gc.getFontMetrics();
	FontMetrics fm2 = gc.getFontMetrics();
	if (fm1.equals(fm2)) {
		assertEquals(fm1.hashCode(), fm2.hashCode());
	}
}

public void test_win32_newLorg_eclipse_swt_internal_win32_TEXTMETRIC() {
	// do not test - Windows only
}

public static Test suite() {
	TestSuite suite = new TestSuite();
	java.util.Vector methodNames = methodNames();
	java.util.Enumeration e = methodNames.elements();
	while (e.hasMoreElements()) {
		suite.addTest(new Test_org_eclipse_swt_graphics_FontMetrics((String)e.nextElement()));
	}
	return suite;
}
public static java.util.Vector methodNames() {
	java.util.Vector methodNames = new java.util.Vector();
	methodNames.addElement("test_equalsLjava_lang_Object");
	methodNames.addElement("test_getAscent");
	methodNames.addElement("test_getAverageCharWidth");
	methodNames.addElement("test_getDescent");
	methodNames.addElement("test_getHeight");
	methodNames.addElement("test_getLeading");
	methodNames.addElement("test_hashCode");
	methodNames.addElement("test_win32_newLorg_eclipse_swt_internal_win32_TEXTMETRIC");
	return methodNames;
}
protected void runTest() throws Throwable {
	if (getName().equals("test_equalsLjava_lang_Object")) test_equalsLjava_lang_Object();
	else if (getName().equals("test_getAscent")) test_getAscent();
	else if (getName().equals("test_getAverageCharWidth")) test_getAverageCharWidth();
	else if (getName().equals("test_getDescent")) test_getDescent();
	else if (getName().equals("test_getHeight")) test_getHeight();
	else if (getName().equals("test_getLeading")) test_getLeading();
	else if (getName().equals("test_hashCode")) test_hashCode();
	else if (getName().equals("test_win32_newLorg_eclipse_swt_internal_win32_TEXTMETRIC")) test_win32_newLorg_eclipse_swt_internal_win32_TEXTMETRIC();
}

/* custom */
	Display display;
	Shell shell;
	GC gc;
}
