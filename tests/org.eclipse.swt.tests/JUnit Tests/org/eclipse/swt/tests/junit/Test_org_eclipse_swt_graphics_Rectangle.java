/*******************************************************************************
 * Copyright (c) 2000, 2017 IBM Corporation and others.
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
 *     Red Hat Inc. - Bug 462631
 *******************************************************************************/
package org.eclipse.swt.tests.junit;

import static org.eclipse.swt.tests.junit.SwtTestUtil.assertSWTProblem;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.junit.Test;

/**
 * Automated Test Suite for class org.eclipse.swt.graphics.Rectangle
 *
 * @see org.eclipse.swt.graphics.Rectangle
 */
public class Test_org_eclipse_swt_graphics_Rectangle {

@Test
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
@Test
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
		assertSWTProblem("Incorrect exception thrown for rectangle == null", SWT.ERROR_NULL_ARGUMENT, e);
	}
}

/**
 * Returns <code>true</code> if the point specified by the
 * arguments is inside the area specified by the receiver.
 */
@Test
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
@Test
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

	assertFalse("Rectangle should not contain Point(9, 10)", r.contains(new Point(9, 10)));

	assertFalse("Rectangle should not contain point (-1, -1)", r.contains(new Point(-1, -1)));

	try {
		r.contains(null);
		fail("No exception thrown for rectangle == null");
	} catch (IllegalArgumentException e) {
		assertSWTProblem("Incorrect exception thrown for rectangle == null", SWT.ERROR_NULL_ARGUMENT, e);
	}
}

@Test
public void test_equalsLjava_lang_Object() {
	Rectangle r1 = new Rectangle(5, 4, 3, 2);
	Rectangle r2 = new Rectangle(5, 4, 3, 2);
	assertTrue("Rectangles should be equal", r1.equals(r2));

	r2 = new Rectangle(3, 4, 5, 6);
	assertFalse("Rectangles should not be equal", r1.equals(r2));

	r2 = new Rectangle(2, 3, 4, 5);
	assertFalse("Rectangles should not be equal", r1.equals(r2));

	r2 = new Rectangle(5, 4, 2, 3);
	assertFalse("Rectangles should not be equal", r1.equals(r2));

	r2 = new Rectangle(4, 5, 3, 2);
	assertFalse("Rectangles should not be equal", r1.equals(r2));
}

@Test
public void test_hashCode() {
	Rectangle r1 = new Rectangle(5, 4, 3, 2);
	Rectangle r2 = new Rectangle(5, 4, 3, 2);
	assertTrue("Rectangles should have the same hashCode", r1.hashCode() == r2.hashCode());
}

@Test
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
		assertSWTProblem("Incorrect exception thrown for rectangle == null", SWT.ERROR_NULL_ARGUMENT, e);
	}
}

@Test
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
		assertSWTProblem("Incorrect exception thrown for rectangle == null", SWT.ERROR_NULL_ARGUMENT, e);
	}
}

@Test
public void test_intersectsIIII() {
	Rectangle r1 = new Rectangle(1, 2, 3, 4);
	Rectangle r2 = new Rectangle(2, 3, 7, 8);
	assertTrue("Rectangle(1, 2, 3, 4) should intersect Rectangle(2, 3, 7, 8)", r1.intersects(2, 3, 7, 8));
	assertTrue("Rectangle(2, 3, 7, 8) should intersect Rectangle(1, 2, 3, 4)", r2.intersects(1, 2, 3, 4));

	r2 = new Rectangle(200, 300, 400, 500);
	assertFalse("Rectangle(1, 2, 3, 4) should not intersect Rectangle(200, 300, 400, 500)", r1.intersects(200, 300, 400, 500));
	assertFalse("Rectangle(200, 300, 400, 500) should not intersect Rectangle(1, 2, 3, 4)", r2.intersects(1, 2, 3, 4));

	r2 = new Rectangle(3, 3, 0, 0);
	assertTrue("Rectangle(1, 2, 3, 4) should intersect Rectangle(3, 3, 0, 0)", r1.intersects(3, 3, 0, 0));
	assertTrue("Rectangle(3, 3, 0, 0) should intersect Rectangle(1, 2, 3, 4)", r2.intersects(1, 2, 3, 4));

	r2 = new Rectangle(3, 3, -1, -1);
	assertFalse("Rectangle(1, 2, 3, 4) should not intersect Rectangle(3, 3, -1, -1)", r1.intersects(3, 3, -1, -1));
	assertFalse("Rectangle(3, 3, -1, -1) should not intersect Rectangle(1, 2, 3, 4)", r2.intersects(1, 2, 3, 4));

	try {
		r1.intersects(null);
		fail("No exception thrown for rectangle == null");
	} catch (IllegalArgumentException e) {
		assertSWTProblem("Incorrect exception thrown for rectangle == null", SWT.ERROR_NULL_ARGUMENT, e);
	}
}

@Test
public void test_intersectsLorg_eclipse_swt_graphics_Rectangle() {
	Rectangle r1 = new Rectangle(1, 2, 3, 4);
	Rectangle r2 = new Rectangle(2, 3, 7, 8);
	assertTrue("Rectangle(1, 2, 3, 4) should intersect Rectangle(2, 3, 7, 8)", r1.intersects(r2));
	assertTrue("Rectangle(2, 3, 7, 8) should intersect Rectangle(1, 2, 3, 4)", r2.intersects(r1));

	r2 = new Rectangle(200, 300, 400, 500);
	assertFalse("Rectangle(1, 2, 3, 4) should not intersect Rectangle(200, 300, 400, 500)", r1.intersects(r2));
	assertFalse("Rectangle(200, 300, 400, 500) should not intersect Rectangle(1, 2, 3, 4)", r2.intersects(r1));

	r2 = new Rectangle(3, 3, 0, 0);
	assertTrue("Rectangle(1, 2, 3, 4) should intersect Rectangle(3, 3, 0, 0)", r1.intersects(r2));
	assertTrue("Rectangle(3, 3, 0, 0) should intersect Rectangle(1, 2, 3, 4)", r2.intersects(r1));

	r2 = new Rectangle(3, 3, -1, -1);
	assertFalse("Rectangle(1, 2, 3, 4) should not intersect Rectangle(3, 3, -1, -1)", r1.intersects(r2));
	assertFalse("Rectangle(3, 3, -1, -1) should not intersect Rectangle(1, 2, 3, 4)", r2.intersects(r1));

	try {
		r1.intersects(null);
		fail("No exception thrown for rectangle == null");
	} catch (IllegalArgumentException e) {
		assertSWTProblem("Incorrect exception thrown for rectangle == null", SWT.ERROR_NULL_ARGUMENT, e);
	}
}

@Test
public void test_isEmpty() {
	Rectangle r = new Rectangle(1, 2, 0, 0);
	assertTrue("Rectangle is empty", r.isEmpty());

	r = new Rectangle(1, 2, -3, -4);
	assertTrue("Rectangle is empty", r.isEmpty());

	r = new Rectangle(1, 2, 3, 4);
	assertFalse("Rectangle is not empty", r.isEmpty());
}

@Test
public void test_toString() {
	Rectangle r = new Rectangle(3, 4, 5, 6);
	assertNotNull(r.toString());
	assertTrue(r.toString().length() > 0);
	assertEquals("Rectangle {3, 4, 5, 6}", r.toString());
}

@Test
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
		assertSWTProblem("Incorrect exception thrown for rectangle == null", SWT.ERROR_NULL_ARGUMENT, e);
	}
}

}
