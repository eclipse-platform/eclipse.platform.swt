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

public void test_ConstructorIII() {
	// Test RGB(int red, int green, int blue)
	new RGB(20,100,200);
	
	new RGB(0,0,0);

	

	try {
		new RGB(-1, 20, 50);
		fail("No exception thrown for red < 0");
	}
	catch (IllegalArgumentException e) {
	}
	
	try {
		new RGB(256, 20, 50);
		fail("No exception thrown for red > 255");
	}
	catch (IllegalArgumentException e) {
	}
	
	try {
		new RGB(20, -1, 50);
		fail("No exception thrown for green < 0");
	}
	catch (IllegalArgumentException e) {
	}
	
	try {
		new RGB(20, 256, 50);
		fail("No exception thrown for green > 255");
	}
	catch (IllegalArgumentException e) {
	}

	try {
		new RGB(20, 50, -1);
		fail("No exception thrown for blue < 0");
	}
	catch (IllegalArgumentException e) {
	}
	
	try {
		new RGB(20, 50, 256);
		fail("No exception thrown for blue > 255");
	}
	catch (IllegalArgumentException e) {
	}
			
}

public void test_ConstructorFFF() {

	new RGB(0f,0f,0f);
	
	new RGB(0f,1f,0f);
	new RGB(0f,0f,1f);
	new RGB(0f,0.6f,0.4f);
	new RGB(1f,0f,1f);
	new RGB(1f,1f,1f);
	new RGB(1f,0f,1f);
	new RGB(1f,1f,0f);
	new RGB(1f,0.6f,0.4f);
	new RGB(59f,0f,1f);
	new RGB(59f,1f,1f);
	new RGB(59f,0f,1f);
	new RGB(59f,1f,0f);
	new RGB(59f,0.6f,0.4f);
	new RGB(60f,0f,1f);
	new RGB(60f,1f,1f);
	new RGB(60f,0f,1f);
	new RGB(60f,1f,0f);
	new RGB(60f,0.6f,0.4f);
	new RGB(61f,0f,1f);
	new RGB(61f,1f,1f);
	new RGB(61f,0f,1f);
	new RGB(61f,1f,0f);
	new RGB(61f,0.6f,0.4f);
	new RGB(119f,0f,1f);
	new RGB(119f,1f,1f);
	new RGB(119f,0f,1f);
	new RGB(119f,1f,0f);
	new RGB(119f,0.6f,0.4f);
	new RGB(120f,0f,1f);
	new RGB(120f,1f,1f);
	new RGB(120f,0f,1f);
	new RGB(120f,1f,0f);
	new RGB(120f,0.6f,0.4f);
	new RGB(121f,0f,1f);
	new RGB(121f,1f,1f);
	new RGB(121f,0f,1f);
	new RGB(121f,1f,0f);
	new RGB(121f,0.6f,0.4f);
	new RGB(179f,0f,1f);
	new RGB(179f,1f,1f);
	new RGB(179f,0f,1f);
	new RGB(179f,1f,0f);
	new RGB(179f,0.6f,0.4f);
	new RGB(180f,0f,1f);
	new RGB(180f,1f,1f);
	new RGB(180f,0f,1f);
	new RGB(180f,1f,0f);
	new RGB(180f,0.6f,0.4f);
	new RGB(181f,0f,1f);
	new RGB(181f,1f,1f);
	new RGB(181f,0f,1f);
	new RGB(181f,1f,0f);
	new RGB(181f,0.6f,0.4f);
	new RGB(239f,0f,1f);
	new RGB(239f,1f,1f);
	new RGB(239f,0f,1f);
	new RGB(239f,1f,0f);
	new RGB(239f,0.6f,0.4f);
	new RGB(240f,0f,1f);
	new RGB(240f,1f,1f);
	new RGB(240f,0f,1f);
	new RGB(240f,1f,0f);
	new RGB(240f,0.6f,0.4f);
	new RGB(241f,0f,1f);
	new RGB(241f,1f,1f);
	new RGB(241f,0f,1f);
	new RGB(241f,1f,0f);
	new RGB(241f,0.6f,0.4f);
	new RGB(299f,0f,1f);
	new RGB(299f,1f,1f);
	new RGB(299f,0f,1f);
	new RGB(299f,1f,0f);
	new RGB(299f,0.6f,0.4f);
	new RGB(300f,0f,1f);
	new RGB(300f,1f,1f);
	new RGB(300f,0f,1f);
	new RGB(300f,1f,0f);
	new RGB(300f,0.6f,0.4f);
	new RGB(301f,0f,1f);
	new RGB(301f,1f,1f);
	new RGB(301f,0f,1f);
	new RGB(301f,1f,0f);
	new RGB(301f,0.6f,0.4f);
	new RGB(359f,0f,1f);
	new RGB(359f,1f,1f);
	new RGB(359f,0f,1f);
	new RGB(359f,1f,0f);
	new RGB(359f,0.6f,0.4f);
	new RGB(360f,0f,1f);
	new RGB(360f,1f,1f);
	new RGB(360f,0f,1f);
	new RGB(360f,1f,0f);
	new RGB(360f,0.6f,0.4f);
	
	try {
		new RGB(400f, 0.5f, 0.5f);
		fail("No exception thrown for hue > 360");
	}
	catch (IllegalArgumentException e) {
	}
	
	try {
		new RGB(-5f, 0.5f, 0.5f);
		fail("No exception thrown for hue < 0");
	}
	catch (IllegalArgumentException e) {
	}
	
	try {
		new RGB(200f, -0.5f, 0.5f);
		fail("No exception thrown for saturation < 0");
	}
	catch (IllegalArgumentException e) {
	}
	
	try {
		new RGB(200f, 300f, 0.5f);
		fail("No exception thrown for saturation > 1");
	}
	catch (IllegalArgumentException e) {
	}
	
	try {
		new RGB(200f, 0.5f, -0.5f);
		fail("No exception thrown for brightness < 0");
	}
	catch (IllegalArgumentException e) {
	}
	
	try {
		new RGB(200f, 0.5f, 400f);
		fail("No exception thrown for brightness > 1");
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

	float hue = 220f, sat = 0.6f, bright = 0.7f;
	rgb1 = new RGB(hue, sat, bright);
	rgb2 = rgb1;
	if (!rgb1.equals(rgb2)) {
		fail("Two references to the same RGB instance not found equal");
	}
	
	rgb2 = new RGB(hue, sat, bright);
	if (!rgb1.equals(rgb2)) {
		fail("References to two different RGB instances with same H S B parameters not found equal");
	}
	
	if (rgb1.equals(new RGB(hue+1, sat, bright)) ||
	    rgb1.equals(new RGB(hue, sat+0.1f, bright)) ||
	    rgb1.equals(new RGB(hue, sat, bright+0.1f)) ||
	    rgb1.equals(new RGB(hue+1, sat+0.1f, bright+0.1f))) {
		fail("Comparing two RGB instances with different combination of H S B parameters found equal");    	
	}
}
public void test_getHSB() {
	float[] hsb = new float[] {
				0f,0f,0f,
				0f,1f,1f,
				0f,1f,0f,
				0f,0f,1f,
				0f,0.6f,0.4f,
				1f,0f,1f,
				1f,1f,1f,
				1f,0f,1f,
				1f,1f,0f,
				1f,0.6f,0.4f,
				59f,0f,1f,
				59f,1f,1f,
				59f,0f,1f,
				59f,1f,0f,
				59f,0.6f,0.4f,
				60f,0f,1f,
				60f,1f,1f,
				60f,0f,1f,
				60f,1f,0f,
				60f,0.6f,0.4f,
				61f,0f,1f,
				61f,1f,1f,
				61f,0f,1f,
				61f,1f,0f,
				61f,0.6f,0.4f,
				119f,0f,1f,
				119f,1f,1f,
				119f,0f,1f,
				119f,1f,0f,
				119f,0.6f,0.4f,
				120f,0f,1f,
				120f,1f,1f,
				120f,0f,1f,
				120f,1f,0f,
				120f,0.6f,0.4f,
				121f,0f,1f,
				121f,1f,1f,
				121f,0f,1f,
				121f,1f,0f,
				121f,0.6f,0.4f,
				179f,0f,1f,
				179f,1f,1f,
				179f,0f,1f,
				179f,1f,0f,
				179f,0.6f,0.4f,
				180f,0f,1f,
				180f,1f,1f,
				180f,0f,1f,
				180f,1f,0f,
				180f,0.6f,0.4f,
				181f,0f,1f,
				181f,1f,1f,
				181f,0f,1f,
				181f,1f,0f,
				181f,0.6f,0.4f,
				239f,0f,1f,
				239f,1f,1f,
				239f,0f,1f,
				239f,1f,0f,
				239f,0.6f,0.4f,
				240f,0f,1f,
				240f,1f,1f,
				240f,0f,1f,
				240f,1f,0f,
				240f,0.6f,0.4f,
				241f,0f,1f,
				241f,1f,1f,
				241f,0f,1f,
				241f,1f,0f,
				241f,0.6f,0.4f,
				299f,0f,1f,
				299f,1f,1f,
				299f,0f,1f,
				299f,1f,0f,
				299f,0.6f,0.4f,
				300f,0f,1f,
				300f,1f,1f,
				300f,0f,1f,
				300f,1f,0f,
				300f,0.6f,0.4f,
				301f,0f,1f,
				301f,1f,1f,
				301f,0f,1f,
				301f,1f,0f,
				301f,0.6f,0.4f,
				359f,0f,1f,
				359f,1f,1f,
				359f,0f,1f,
				359f,1f,0f,
				359f,0.6f,0.4f,
				360f,0f,1f,
				360f,1f,1f,
				360f,0f,1f,
				360f,1f,0f,
				360f,0.6f,0.4f,
				220f,0.6f,0.7f};
	for (int i = 0; i < hsb.length; i+=3) {
		RGB rgb1 = new RGB(hsb[i], hsb[i+1], hsb[i+2]);
		float[] hsb2 = rgb1.getHSB();
		RGB rgb2 = new RGB(hsb2[0], hsb2[1], hsb2[2]);
		if (!rgb1.equals(rgb2)) {
			fail("Two references to the same RGB using getHSB() function not found equal");
		}
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
	methodNames.addElement("test_ConstructorFFF");
	methodNames.addElement("test_equalsLjava_lang_Object");
	methodNames.addElement("test_getHSB");
	methodNames.addElement("test_hashCode");
	methodNames.addElement("test_toString");
	return methodNames;
}
protected void runTest() throws Throwable {
	if (getName().equals("test_ConstructorIII")) test_ConstructorIII();
	else if (getName().equals("test_ConstructorFFF")) test_ConstructorFFF();
	else if (getName().equals("test_equalsLjava_lang_Object")) test_equalsLjava_lang_Object();
	else if (getName().equals("test_getHSB")) test_getHSB();
	else if (getName().equals("test_hashCode")) test_hashCode();
	else if (getName().equals("test_toString")) test_toString();
}
}
