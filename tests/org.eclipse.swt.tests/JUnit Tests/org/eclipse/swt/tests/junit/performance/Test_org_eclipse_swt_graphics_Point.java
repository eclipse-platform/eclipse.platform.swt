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
package org.eclipse.swt.tests.junit.performance;


import org.eclipse.swt.graphics.*;

import junit.framework.*;
import junit.textui.*;

/**
 * Automated Performance Test Suite for class org.eclipse.swt.graphics.Point
 *
 * @see org.eclipse.swt.graphics.Point
 */
public class Test_org_eclipse_swt_graphics_Point extends SwtPerformanceTestCase {
	static final int COUNT = 10000;

public Test_org_eclipse_swt_graphics_Point(String name) {
	super(name);
}

public static void main(String[] args) {
	TestRunner.run(suite());
}

public void test_ConstructorII() {
	startMeasuring();
	for (int i = 0; i < COUNT; i++) {
		new Point(500000, 700000);
	}
	stopMeasuring();
	
	commitMeasurements();
	assertPerformance();
}

public void test_equalsLjava_lang_Object() {
	Point p1 = new Point (5, 3);
	Point p2 = new Point (5, 3);
	Point p3 = new Point (4, 6);
	
	startMeasuring();
	for (int i = 0; i < COUNT; i++) {
		p1.equals(p2);	// equal
	}
	stopMeasuring();
	
	commitMeasurements();
	assertPerformance();
	
	startMeasuring();
	for (int i = 0; i < COUNT; i++) {
		p1.equals(p3);	// not equal
	}
	stopMeasuring();
	
	commitMeasurements();
	assertPerformance();
}

public void test_hashCode() {
	Point point = new Point(5, 5);
	
	startMeasuring();
	for (int i = 0; i < COUNT; i++) {
		point.hashCode();
	}
	stopMeasuring();
	
	commitMeasurements();
	assertPerformance();
}

public static Test suite() {
	TestSuite suite = new TestSuite();
	java.util.Vector methodNames = methodNames();
	java.util.Enumeration e = methodNames.elements();
	while (e.hasMoreElements()) {
		suite.addTest(new Test_org_eclipse_swt_graphics_Point((String)e.nextElement()));
	}
	return suite;
}
public static java.util.Vector methodNames() {
	java.util.Vector methodNames = new java.util.Vector();
	methodNames.addElement("test_ConstructorII");
	methodNames.addElement("test_equalsLjava_lang_Object");
	methodNames.addElement("test_hashCode");
	return methodNames;
}
protected void runTest() throws Throwable {
	if (getName().equals("test_ConstructorII")) test_ConstructorII();
	else if (getName().equals("test_equalsLjava_lang_Object")) test_equalsLjava_lang_Object();
	else if (getName().equals("test_hashCode")) test_hashCode();
}
}
