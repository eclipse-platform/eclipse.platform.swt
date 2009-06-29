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

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;

/**
 * Automated Test Suite for class org.eclipse.swt.graphics.Rectangle
 *
 * @see org.eclipse.swt.graphics.Rectangle
 */
public class Test_org_eclipse_swt_graphics_Rectangle extends SwtTestCase {

public Test_org_eclipse_swt_graphics_Rectangle(String name) {
	super(name);
}

public static void main(String[] args) {
	TestRunner.run(suite());
}

public void test_ConstructorIIII() {
	// Test new Rectangle (int x, int y, int width, int height)
	Rectangle r = new Rectangle(3, 4, 5, 6);
	assertEquals(3, r.x);
	assertEquals(4, r.y);
	assertEquals(5, r.width);
	assertEquals(6, r.height);

	r = new Rectangle(-4, -3, -2, -1);
	assertEquals(-4, r.x);
	assertEquals(-3, r.y);
	assertEquals(-2, r.width);
	assertEquals(-1, r.height);

	r = new Rectangle(500000, 700000, 200000, 100000);
	assertEquals(500000, r.x);
	assertEquals(700000, r.y);
	assertEquals(200000, r.width);
	assertEquals(100000, r.height);
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
	Rectangle r1 = new Rectangle(1, 2, 3, 4);
	Rectangle r2 = new Rectangle(3, 3, 2, 2);
	r1.add(r2);
	assertEquals("Rectangle add incorrect", new Rectangle(1, 2, 4, 4), r1);

	r1.add(r2);
	assertEquals("Rectangle add incorrect", new Rectangle(1, 2, 4, 4), r1);
	
	r1 = new Rectangle(1, 2, 3, 4);
	r2 = new Rectangle(3, 3, 0, 0);
	r1.add(r2);
	assertEquals("Rectangle add incorrect", new Rectangle(1, 2, 3, 4), r1);

	r1 = new Rectangle(1, 2, 3, 4);
	r2 = new Rectangle(6, 6, 0, 0);
	r1.add(r2);
	assertEquals("Rectangle add incorrect", new Rectangle(1, 2, 5, 4), r1);

	try {
		r1.add(null);
		fail("No exception thrown for rectangle == null");
	} catch (IllegalArgumentException e) {
		assertEquals("Incorrect exception thrown for rectangle == null", SWT.ERROR_NULL_ARGUMENT, e);
	}
}

/**
 * Returns <code>true</code> if the point specified by the
 * arguments is inside the area specified by the receiver.
 */
public void test_containsII() {
	Rectangle r = new Rectangle(1, 2, 3, 4);
	assertTrue("Rectangle should contain point (1, 2)", r.contains(1, 2));

	assertTrue("Rectangle should contain point (3, 4)", r.contains(3, 4));

	assertTrue("Rectangle should contain point (3, 5)", r.contains(3, 5));

//	assertTrue("Rectangle should contain point (3, 6)", r.contains(3, 6));
//
//	assertTrue("Rectangle should contain point (4, 5)", r.contains(4, 5));
//
//	assertTrue("Rectangle should contain point (4, 6)", r.contains(4, 6));

	assertTrue("Rectangle should not contain point (9, 10)", !r.contains(9, 10));

	assertTrue("Rectangle should not contain point (-1, -1)", !r.contains(-1, -1));
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
	Rectangle r = new Rectangle(1, 2, 3, 4);
	assertTrue("Rectangle should contain Point(1, 2)", r.contains(new Point(1, 2)));

	assertTrue("Rectangle should contain Point(3, 4)", r.contains(new Point(3, 4)));

	assertTrue("Rectangle should contain point (3, 5)", r.contains(new Point(3, 5)));

//	assertTrue("Rectangle should contain point (3, 6)", r.contains(new Point(3, 6)));
//
//	assertTrue("Rectangle should contain point (4, 5)", r.contains(new Point(4, 5)));
//
//	assertTrue("Rectangle should contain Point(4, 6)", r.contains(new Point(4, 6)));

	assertTrue("Rectangle should not contain Point(9, 10)", !r.contains(new Point(9, 10)));

	assertTrue("Rectangle should not contain point (-1, -1)", !r.contains(new Point(-1, -1)));

	try {
		r.contains(null);
		fail("No exception thrown for rectangle == null");
	} catch (IllegalArgumentException e) {
		assertEquals("Incorrect exception thrown for rectangle == null", SWT.ERROR_NULL_ARGUMENT, e);
	}
}

public void test_equalsLjava_lang_Object() {
	Rectangle r1 = new Rectangle(5, 4, 3, 2);
	Rectangle r2 = new Rectangle(5, 4, 3, 2);
	assertTrue("Rectangles should be equal", r1.equals(r2));

	r2 = new Rectangle(3, 4, 5, 6);
	assertTrue("Rectangles should not be equal", !r1.equals(r2));

	r2 = new Rectangle(2, 3, 4, 5);
	assertTrue("Rectangles should not be equal", !r1.equals(r2));

	r2 = new Rectangle(5, 4, 2, 3);
	assertTrue("Rectangles should not be equal", !r1.equals(r2));

	r2 = new Rectangle(4, 5, 3, 2);
	assertTrue("Rectangles should not be equal", !r1.equals(r2));
}

public void test_hashCode() {
	Rectangle r1 = new Rectangle(5, 4, 3, 2);
	Rectangle r2 = new Rectangle(5, 4, 3, 2);
	assertTrue("Rectangles should have the same hashCode", r1.hashCode() == r2.hashCode());
}

public void test_intersectLorg_eclipse_swt_graphics_Rectangle() {
	Rectangle r1 = new Rectangle(1, 2, 3, 4);
	Rectangle r2 = new Rectangle(3, 3, 2, 2);
	
	r1.intersect(r2);
	assertEquals("Rectangle intersect incorrect", new Rectangle(3, 3, 1, 2), r1);

	r1 = new Rectangle(1, 2, 3, 4);
	r2 = new Rectangle(3, 3, 0, 0);
	r1.intersect(r2);
	assertEquals("Rectangle intersect incorrect", new Rectangle(3, 3, 0, 0), r1);

	r1 = new Rectangle(1, 2, 3, 4);
	r2 = new Rectangle(3, 3, -1, -1);
	r1.intersect(r2);
	assertEquals("Rectangle intersect incorrect", new Rectangle(0, 0, 0, 0), r1);

	try {
		r1.intersect(null);
		fail("No exception thrown for rectangle == null");
	} catch (IllegalArgumentException e) {
		assertEquals("Incorrect exception thrown for rectangle == null", SWT.ERROR_NULL_ARGUMENT, e);
	}
}

public void test_intersectionLorg_eclipse_swt_graphics_Rectangle() {
	Rectangle r1 = new Rectangle(1, 2, 3, 4);
	Rectangle r2 = new Rectangle(3, 3, 2, 2);
	assertEquals("Rectangle intersection incorrect", new Rectangle(3, 3, 1, 2), r1.intersection(r2));

	r2 = new Rectangle(3, 3, 0, 0);
	assertEquals("Rectangle intersection incorrect", new Rectangle(3, 3, 0, 0), r1.intersection(r2));

	r2 = new Rectangle(3, 3, -1, -1);
	assertEquals("Rectangle intersection incorrect", new Rectangle(0, 0, 0, 0), r1.intersection(r2));

	try {
		r1.intersection(null);
		fail("No exception thrown for rectangle == null");
	} catch (IllegalArgumentException e) {
		assertEquals("Incorrect exception thrown for rectangle == null", SWT.ERROR_NULL_ARGUMENT, e);
	}
}

public void test_intersectsIIII() {
	Rectangle r1 = new Rectangle(1, 2, 3, 4);
	Rectangle r2 = new Rectangle(2, 3, 7, 8);
	assertTrue("Rectangle(1, 2, 3, 4) should intersect Rectangle(2, 3, 7, 8)", r1.intersects(2, 3, 7, 8));
	assertTrue("Rectangle(2, 3, 7, 8) should intersect Rectangle(1, 2, 3, 4)", r2.intersects(1, 2, 3, 4));

	r2 = new Rectangle(200, 300, 400, 500);
	assertTrue("Rectangle(1, 2, 3, 4) should not intersect Rectangle(200, 300, 400, 500)", !r1.intersects(200, 300, 400, 500));
	assertTrue("Rectangle(200, 300, 400, 500) should not intersect Rectangle(1, 2, 3, 4)", !r2.intersects(1, 2, 3, 4));
	
	r2 = new Rectangle(3, 3, 0, 0);
	assertTrue("Rectangle(1, 2, 3, 4) should intersect Rectangle(3, 3, 0, 0)", r1.intersects(3, 3, 0, 0));
	assertTrue("Rectangle(3, 3, 0, 0) should intersect Rectangle(1, 2, 3, 4)", r2.intersects(1, 2, 3, 4));

	r2 = new Rectangle(3, 3, -1, -1);
	assertTrue("Rectangle(1, 2, 3, 4) should not intersect Rectangle(3, 3, -1, -1)", !r1.intersects(3, 3, -1, -1));
	assertTrue("Rectangle(3, 3, -1, -1) should not intersect Rectangle(1, 2, 3, 4)", !r2.intersects(1, 2, 3, 4));

	try {
		r1.intersects(null);
		fail("No exception thrown for rectangle == null");
	} catch (IllegalArgumentException e) {
		assertEquals("Incorrect exception thrown for rectangle == null", SWT.ERROR_NULL_ARGUMENT, e);
	}
}

public void test_intersectsLorg_eclipse_swt_graphics_Rectangle() {
	Rectangle r1 = new Rectangle(1, 2, 3, 4);
	Rectangle r2 = new Rectangle(2, 3, 7, 8);
	assertTrue("Rectangle(1, 2, 3, 4) should intersect Rectangle(2, 3, 7, 8)", r1.intersects(r2));
	assertTrue("Rectangle(2, 3, 7, 8) should intersect Rectangle(1, 2, 3, 4)", r2.intersects(r1));

	r2 = new Rectangle(200, 300, 400, 500);
	assertTrue("Rectangle(1, 2, 3, 4) should not intersect Rectangle(200, 300, 400, 500)", !r1.intersects(r2));
	assertTrue("Rectangle(200, 300, 400, 500) should not intersect Rectangle(1, 2, 3, 4)", !r2.intersects(r1));
	
	r2 = new Rectangle(3, 3, 0, 0);
	assertTrue("Rectangle(1, 2, 3, 4) should intersect Rectangle(3, 3, 0, 0)", r1.intersects(r2));
	assertTrue("Rectangle(3, 3, 0, 0) should intersect Rectangle(1, 2, 3, 4)", r2.intersects(r1));

	r2 = new Rectangle(3, 3, -1, -1);
	assertTrue("Rectangle(1, 2, 3, 4) should not intersect Rectangle(3, 3, -1, -1)", !r1.intersects(r2));
	assertTrue("Rectangle(3, 3, -1, -1) should not intersect Rectangle(1, 2, 3, 4)", !r2.intersects(r1));

	try {
		r1.intersects(null);
		fail("No exception thrown for rectangle == null");
	} catch (IllegalArgumentException e) {
		assertEquals("Incorrect exception thrown for rectangle == null", SWT.ERROR_NULL_ARGUMENT, e);
	}
}

public void test_isEmpty() {
	Rectangle r = new Rectangle(1, 2, 0, 0);
	assertTrue("Rectangle is empty", r.isEmpty());

	r = new Rectangle(1, 2, -3, -4);
	assertTrue("Rectangle is empty", r.isEmpty());

	r = new Rectangle(1, 2, 3, 4);
	assertTrue("Rectangle is not empty", !r.isEmpty());
}

public void test_toString() {
	Rectangle r = new Rectangle(3, 4, 5, 6);
	assertNotNull(r.toString());
	assertTrue(r.toString().length() > 0);
	assertEquals("Rectangle {3, 4, 5, 6}", r.toString());
}

public void test_unionLorg_eclipse_swt_graphics_Rectangle() {
	Rectangle r1 = new Rectangle(1, 2, 3, 4);
	Rectangle r2 = new Rectangle(3, 3, 2, 2);
	assertEquals("Rectangle union incorrect", new Rectangle(1, 2, 4, 4), r1.union(r2));
	
	r2 = new Rectangle(3, 3, 0, 0);
	assertEquals("Rectangle union incorrect", new Rectangle(1, 2, 3, 4), r1.union(r2));

	r2 = new Rectangle(3, 3, -1, -1);
	assertEquals("Rectangle union incorrect", new Rectangle(1, 2, 3, 4), r1.union(r2));

	try {
		r1.union(null);
		fail("No exception thrown for rectangle == null");
	} catch (IllegalArgumentException e) {
		assertEquals("Incorrect exception thrown for rectangle == null", SWT.ERROR_NULL_ARGUMENT, e);
	}
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
	methodNames.addElement("test_toString");
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
	else if (getName().equals("test_toString")) test_toString();
	else if (getName().equals("test_unionLorg_eclipse_swt_graphics_Rectangle")) test_unionLorg_eclipse_swt_graphics_Rectangle();
}
}
