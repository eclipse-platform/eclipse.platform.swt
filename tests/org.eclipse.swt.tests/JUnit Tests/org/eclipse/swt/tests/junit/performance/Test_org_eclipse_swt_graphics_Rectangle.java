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

import junit.framework.*;
import junit.textui.*;

import org.eclipse.swt.graphics.*;
import org.eclipse.test.performance.PerformanceMeter;

/**
 * Automated Performance Test Suite for class org.eclipse.swt.graphics.Rectangle
 *
 * @see org.eclipse.swt.graphics.Rectangle
 */
public class Test_org_eclipse_swt_graphics_Rectangle extends SwtPerformanceTestCase {

public Test_org_eclipse_swt_graphics_Rectangle(String name) {
	super(name);
}

public static void main(String[] args) {
	TestRunner.run(suite());
}

public void test_ConstructorIIII() {
	final int COUNT = 40000000;
	
	PerformanceMeter meter = createMeter("Rectangle constr.");
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		new Rectangle(500000, 700000, 200000, 100000);
	}
	meter.stop();

	disposeMeter(meter);
}

/**
 * Destructively replaces the x, y, width and height values
 * in the receiver with ones which represent the union of the
 * rectangles specified by the receiver and the given rectangle.
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the argument is null</li>
 * </ul>
 */
public void test_addLorg_eclipse_swt_graphics_Rectangle() {
	final int COUNT = 60000000;
	
	Rectangle r1 = new Rectangle(1, 2, 3, 4);
	Rectangle r2 = new Rectangle(3, 3, 2, 2);

	PerformanceMeter meter = createMeter("Rectangle add");
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		r1.add(r2);
	}
	meter.stop();
	
	disposeMeter(meter);
}

/**
 * Returns <code>true</code> if the point specified by the
 * arguments is inside the area specified by the receiver.
 */
public void test_containsII() {
	final int COUNT = 60000000;
	
	Rectangle r = new Rectangle(1, 2, 3, 4);

	PerformanceMeter meter = createMeter("Rectangle contains(II) - yes");
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		r.contains(2, 3);	// does contain
	}
	meter.stop();
	
	disposeMeter(meter);
	
	meter = createMeter("Rectangle contains(II) - no");
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		r.contains(9, 12);	// does not contain
	}
	meter.stop();
	
	disposeMeter(meter);
}

/**
 * Returns <code>true</code> if the given point is inside the
 * area specified by the receiver.
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the argument is null</li>
 * </ul>
 */
public void test_containsLorg_eclipse_swt_graphics_Point() {
	final int COUNT = 60000000;
	
	Rectangle rect = new Rectangle(1, 2, 3, 4);
	Point p1 = new Point(2, 3);
	Point p2 = new Point(9, 10);
	
	PerformanceMeter meter = createMeter("Rectangle contains(Point) - yes");
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		rect.contains(p1);		// does contain
	}
	meter.stop();
	
	disposeMeter(meter);
	
	meter = createMeter("Rectangle contains(Point) - no");
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		rect.contains(p2);		// does not contain
	}
	meter.stop();
	
	disposeMeter(meter);
}

public void test_equalsLjava_lang_Object() {
	final int COUNT = 60000000;
	
	Rectangle r1 = new Rectangle(5, 4, 3, 2);
	Rectangle r2 = new Rectangle(5, 4, 3, 2);
	
	PerformanceMeter meter = createMeter("Rectangle equals - yes");
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		r1.equals(r2);	// equal
	}
	meter.stop();
	
	disposeMeter(meter);
	
	r2 = new Rectangle (2, 3, 4, 5);
	
	meter = createMeter("Rectangle equals - no");
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		r1.equals(r2);	// not equal
	}
	meter.stop();
	
	disposeMeter(meter);
}

public void test_hashCode() {
	final int COUNT = 400000000;
	
	Rectangle rect = new Rectangle(5, 4, 3, 2);
	PerformanceMeter meter = createMeter("Rectangle hashCode");
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		rect.hashCode();
	}
	meter.stop();
	
	disposeMeter(meter);
}

public void test_intersectLorg_eclipse_swt_graphics_Rectangle() {
	final int COUNT = 50000000;
	
	Rectangle r1 = new Rectangle(10, 10, 50, 50);
	Rectangle r2 = new Rectangle(20, 20, 20, 20);
	
	PerformanceMeter meter = createMeter("Rectangle intersect - contained");
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		r1.intersect(r2);	// intersect
	}
	meter.stop();
	
	disposeMeter(meter);
	
	r2 = new Rectangle(0, 0, 5, 5);

	meter = createMeter("Rectangle intersect - disjoint");
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		r1.intersect(r2);	// disjoint
	}
	meter.stop();
	
	disposeMeter(meter);
}

public void test_intersectionLorg_eclipse_swt_graphics_Rectangle() {
	final int COUNT = 25000000;
	
	Rectangle r1 = new Rectangle(10, 10, 50, 50);
	Rectangle r2 = new Rectangle(20, 20, 20, 20);
	
	PerformanceMeter meter = createMeter("Rectangle intersection - contained");
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		r1.intersection(r2);	// intersect
	}
	meter.stop();
	
	disposeMeter(meter);
	
	r2 = new Rectangle(0, 0, 5, 5);

	meter = createMeter("Rectangle intersection - disjoint");
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		r1.intersection(r2);	// disjoint
	}
	meter.stop();
	
	disposeMeter(meter);
}

public void test_intersectsIIII() {
	final int COUNT = 60000000;
	
	Rectangle rect = new Rectangle(10, 10, 50, 50);
	
	PerformanceMeter meter = createMeter("Rectangle intersects(IIII) - yes");
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		rect.intersects(20, 20, 20, 20);	// intersect
	}
	meter.stop();
	
	disposeMeter(meter);

	meter = createMeter("Rectangle intersects(IIII) - no");
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		rect.intersects(0, 0, 5, 5);	// disjoint
	}
	meter.stop();
	
	disposeMeter(meter);
}

public void test_intersectsLorg_eclipse_swt_graphics_Rectangle() {
	final int COUNT = 40000000;
	
	Rectangle r1 = new Rectangle(10, 10, 50, 50);
	Rectangle r2 = new Rectangle(20, 20, 20, 20);
	
	PerformanceMeter meter = createMeter("Rectangle intersects(Rectangle) - yes");
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		r1.intersects(r2);	// intersect
	}
	meter.stop();
	
	disposeMeter(meter);
	
	r2 = new Rectangle(0, 0, 5, 5);

	meter = createMeter("Rectangle intersects(Rectangle) - no");
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		r1.intersects(r2);	// disjoint
	}
	meter.stop();
	
	disposeMeter(meter);
}

public void test_isEmpty() {
	final int COUNT = 500000000;
	
	Rectangle rect = new Rectangle (10, 10, 0, 0);
	
	PerformanceMeter meter = createMeter("Rectangle isEmpty - yes");
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		rect.isEmpty();		// empty
	}
	meter.stop();
	
	disposeMeter(meter);

	rect = new Rectangle (10, 10, 10, 10);
	
	meter = createMeter("Rectangle isEmpty - no");
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		rect.isEmpty();		// not empty
	}
	meter.stop();
	
	disposeMeter(meter);
}

public void test_unionLorg_eclipse_swt_graphics_Rectangle() {
	final int COUNT = 30000000;
	
	Rectangle r1 = new Rectangle(10, 10, 50, 50);
	Rectangle r2 = new Rectangle(20, 20, 20, 20);
	
	PerformanceMeter meter = createMeter("Rectangle union - contained");
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		r1.union(r2);
	}
	meter.stop();
	
	disposeMeter(meter);
	
	r2 = new Rectangle(0, 0, 5, 5);

	meter = createMeter("Rectangle union - disjoint");
	meter.start();
	for (int i = 0; i < COUNT; i++) {
		r1.union(r2);	// disjoint
	}
	meter.stop();
	
	disposeMeter(meter);
}

public static Test suite() {
	TestSuite suite = new TestSuite();
	java.util.Vector methodNames = methodNames();
	java.util.Enumeration e = methodNames.elements();
	while (e.hasMoreElements()) {
		suite.addTest(new Test_org_eclipse_swt_graphics_Rectangle((String)e.nextElement()));
	}
	return suite;
}
public static java.util.Vector methodNames() {
	java.util.Vector methodNames = new java.util.Vector();
	methodNames.addElement("test_ConstructorIIII");
	methodNames.addElement("test_addLorg_eclipse_swt_graphics_Rectangle");
	methodNames.addElement("test_containsII");
	methodNames.addElement("test_containsLorg_eclipse_swt_graphics_Point");
	methodNames.addElement("test_equalsLjava_lang_Object");
	methodNames.addElement("test_hashCode");
	methodNames.addElement("test_intersectLorg_eclipse_swt_graphics_Rectangle");
	methodNames.addElement("test_intersectionLorg_eclipse_swt_graphics_Rectangle");
	methodNames.addElement("test_intersectsIIII");
	methodNames.addElement("test_intersectsLorg_eclipse_swt_graphics_Rectangle");
	methodNames.addElement("test_isEmpty");
	methodNames.addElement("test_unionLorg_eclipse_swt_graphics_Rectangle");
	return methodNames;
}
protected void runTest() throws Throwable {
	if (getName().equals("test_ConstructorIIII")) test_ConstructorIIII();
	else if (getName().equals("test_addLorg_eclipse_swt_graphics_Rectangle")) test_addLorg_eclipse_swt_graphics_Rectangle();
	else if (getName().equals("test_containsII")) test_containsII();
	else if (getName().equals("test_containsLorg_eclipse_swt_graphics_Point")) test_containsLorg_eclipse_swt_graphics_Point();
	else if (getName().equals("test_equalsLjava_lang_Object")) test_equalsLjava_lang_Object();
	else if (getName().equals("test_hashCode")) test_hashCode();
	else if (getName().equals("test_intersectLorg_eclipse_swt_graphics_Rectangle")) test_intersectLorg_eclipse_swt_graphics_Rectangle();
	else if (getName().equals("test_intersectionLorg_eclipse_swt_graphics_Rectangle")) test_intersectionLorg_eclipse_swt_graphics_Rectangle();
	else if (getName().equals("test_intersectsIIII")) test_intersectsIIII();
	else if (getName().equals("test_intersectsLorg_eclipse_swt_graphics_Rectangle")) test_intersectsLorg_eclipse_swt_graphics_Rectangle();
	else if (getName().equals("test_isEmpty")) test_isEmpty();
	else if (getName().equals("test_unionLorg_eclipse_swt_graphics_Rectangle")) test_unionLorg_eclipse_swt_graphics_Rectangle();
}
}
