/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
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
import org.eclipse.swt.graphics.*;

/**
 * Automated Test Suite for class org.eclipse.swt.graphics.RGB
 *
 * @see org.eclipse.swt.graphics.RGB
 */
public class Test_org_eclipse_swt_graphics_RGB extends SwtTestCase {

public Test_org_eclipse_swt_graphics_RGB(String name) {
	super(name);
}

public static void main(String[] args) {
	TestRunner.run(suite());
}

protected void setUp() {
}

protected void tearDown() {
}

public void test_ConstructorIII() {
	// Test RGB(int red, int green, int blue)
	RGB rgb = new RGB(20,100,200);
	
	rgb = new RGB(0,0,0);

	rgb = new RGB(255,255,255);

	try {
		rgb = new RGB(-1, 20, 50);
		fail("No exception thrown for red < 0");
	}
	catch (IllegalArgumentException e) {
	}
	
	try {
		rgb = new RGB(256, 20, 50);
		fail("No exception thrown for red > 255");
	}
	catch (IllegalArgumentException e) {
	}
	
	try {
		rgb = new RGB(20, -1, 50);
		fail("No exception thrown for green < 0");
	}
	catch (IllegalArgumentException e) {
	}
	
	try {
		rgb = new RGB(20, 256, 50);
		fail("No exception thrown for green > 255");
	}
	catch (IllegalArgumentException e) {
	}

	try {
		rgb = new RGB(20, 50, -1);
		fail("No exception thrown for blue < 0");
	}
	catch (IllegalArgumentException e) {
	}
	
	try {
		rgb = new RGB(20, 50, 256);
		fail("No exception thrown for blue > 255");
	}
	catch (IllegalArgumentException e) {
	}
			
}

public void test_equalsLjava_lang_Object() {
	int r = 0, g = 127, b = 254;
	RGB rgb1 = new RGB(r, g, b);
	RGB rgb2;
	
	rgb2 = rgb1;
	if (!rgb1.equals(rgb2)) {
		fail("Two references to the same RGB instance not found equal");
	}
	
	rgb2 = new RGB(r, g, b);
	if (!rgb1.equals(rgb2)) {
		fail("References to two different RGB instances with same R G B parameters not found equal");
	}
	
	if (rgb1.equals(new RGB(r+1, g, b)) ||
	    rgb1.equals(new RGB(r, g+1, b)) ||
	    rgb1.equals(new RGB(r, g, b+1)) ||
	    rgb1.equals(new RGB(r+1, g+1, b+1))) {
		fail("Comparing two RGB instances with different combination of R G B parameters found equal");    	
	}
}

public void test_hashCode() {
	int r = 255, g = 100, b = 0;
	RGB rgb1 = new RGB(r, g, b);
	RGB rgb2 = new RGB(r, g, b);
	
	int hash1 = rgb1.hashCode();
	int hash2 = rgb2.hashCode();
	
	if (hash1 != hash2) {
		fail("Two RGB instances with same R G B parameters returned different hash codes");
	}
	
	if (rgb1.hashCode() == new RGB(g, b, r).hashCode() ||
		rgb1.hashCode() == new RGB(b, r, g).hashCode()) {
		fail("Two RGB instances with different R G B parameters returned the same hash code");
	}
}

public void test_toString() {
	RGB rgb = new RGB(0, 100, 200);

	String s = rgb.toString();
	
	if (s == null || s.length() == 0) {
		fail("RGB.toString returns a null or empty String");
	}
}

public static Test suite() {
	TestSuite suite = new TestSuite();
	java.util.Vector methodNames = methodNames();
	java.util.Enumeration e = methodNames.elements();
	while (e.hasMoreElements()) {
		suite.addTest(new Test_org_eclipse_swt_graphics_RGB((String)e.nextElement()));
	}
	return suite;
}
public static java.util.Vector methodNames() {
	java.util.Vector methodNames = new java.util.Vector();
	methodNames.addElement("test_ConstructorIII");
	methodNames.addElement("test_equalsLjava_lang_Object");
	methodNames.addElement("test_hashCode");
	methodNames.addElement("test_toString");
	return methodNames;
}
protected void runTest() throws Throwable {
	if (getName().equals("test_ConstructorIII")) test_ConstructorIII();
	else if (getName().equals("test_equalsLjava_lang_Object")) test_equalsLjava_lang_Object();
	else if (getName().equals("test_hashCode")) test_hashCode();
	else if (getName().equals("test_toString")) test_toString();
}
}
