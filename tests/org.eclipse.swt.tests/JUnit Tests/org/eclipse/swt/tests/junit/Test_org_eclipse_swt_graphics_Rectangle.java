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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.junit.jupiter.api.Test;

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
	assertEquals(new Rectangle(1, 2, 4, 4), r1, "Rectangle add incorrect");

	r1.add(r2);
	assertEquals(new Rectangle(1, 2, 4, 4), r1, "Rectangle add incorrect");

	r1 = new Rectangle(1, 2, 3, 4);
	r2 = new Rectangle(3, 3, 0, 0);
	r1.add(r2);
	assertEquals(new Rectangle(1, 2, 3, 4), r1, "Rectangle add incorrect");

	r1 = new Rectangle(1, 2, 3, 4);
	r2 = new Rectangle(6, 6, 0, 0);
	r1.add(r2);
	assertEquals(new Rectangle(1, 2, 5, 4), r1, "Rectangle add incorrect");

	Rectangle r = new Rectangle(1, 2, 3, 4);
	IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> r.add(null),
			"No exception thrown for rectangle == null");
	assertSWTProblem("Incorrect exception thrown for rectangle == null", SWT.ERROR_NULL_ARGUMENT, e);
}

/**
 * Returns <code>true</code> if the point specified by the
 * arguments is inside the area specified by the receiver.
 */
@Test
public void test_containsII() {
	Rectangle r = new Rectangle(1, 2, 3, 4);
	assertTrue(r.contains(1, 2), "Rectangle should contain point (1, 2)");

	assertTrue(r.contains(3, 4), "Rectangle should contain point (3, 4)");

	assertTrue(r.contains(3, 5), "Rectangle should contain point (3, 5)");

//	assertTrue("Rectangle should contain point (3, 6)", r.contains(3, 6));
//
//	assertTrue("Rectangle should contain point (4, 5)", r.contains(4, 5));
//
//	assertTrue("Rectangle should contain point (4, 6)", r.contains(4, 6));

	assertFalse(r.contains(9, 10), "Rectangle should not contain point (9, 10)");

	assertFalse(r.contains(-1, -1), "Rectangle should not contain point (-1, -1)");
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
	assertTrue(r.contains(new Point(1, 2)), "Rectangle should contain Point(1, 2)");

	assertTrue(r.contains(new Point(3, 4)), "Rectangle should contain Point(3, 4)");

	assertTrue(r.contains(new Point(3, 5)), "Rectangle should contain point (3, 5)");

//	assertTrue("Rectangle should contain point (3, 6)", r.contains(new Point(3, 6)));
//
//	assertTrue("Rectangle should contain point (4, 5)", r.contains(new Point(4, 5)));
//
//	assertTrue("Rectangle should contain Point(4, 6)", r.contains(new Point(4, 6)));

	assertFalse(r.contains(new Point(9, 10)), "Rectangle should not contain Point(9, 10)");

	assertFalse(r.contains(new Point(-1, -1)), "Rectangle should not contain point (-1, -1)");

	IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> r.contains(null),
			"No exception thrown for rectangle == null");
	assertSWTProblem("Incorrect exception thrown for rectangle == null", SWT.ERROR_NULL_ARGUMENT, e);
}

@Test
public void test_equalsLjava_lang_Object() {
	Rectangle r1 = new Rectangle(5, 4, 3, 2);
	Rectangle r2 = new Rectangle(5, 4, 3, 2);
	assertTrue(r1.equals(r2), "Rectangles should be equal");

	r2 = new Rectangle(3, 4, 5, 6);
	assertFalse(r1.equals(r2), "Rectangles should not be equal");

	r2 = new Rectangle(2, 3, 4, 5);
	assertFalse(r1.equals(r2), "Rectangles should not be equal");

	r2 = new Rectangle(5, 4, 2, 3);
	assertFalse(r1.equals(r2), "Rectangles should not be equal");

	r2 = new Rectangle(4, 5, 3, 2);
	assertFalse(r1.equals(r2), "Rectangles should not be equal");
}

@Test
public void test_hashCode() {
	Rectangle r1 = new Rectangle(5, 4, 3, 2);
	Rectangle r2 = new Rectangle(5, 4, 3, 2);
	assertEquals(r2.hashCode(), r1.hashCode(), "Rectangles should have the same hashCode");
}

@Test
public void test_intersectLorg_eclipse_swt_graphics_Rectangle() {
	Rectangle r1 = new Rectangle(1, 2, 3, 4);
	Rectangle r2 = new Rectangle(3, 3, 2, 2);

	r1.intersect(r2);
	assertEquals(new Rectangle(3, 3, 1, 2), r1, "Rectangle intersect incorrect");

	r1 = new Rectangle(1, 2, 3, 4);
	r2 = new Rectangle(3, 3, 0, 0);
	r1.intersect(r2);
	assertEquals(new Rectangle(3, 3, 0, 0), r1, "Rectangle intersect incorrect");

	r1 = new Rectangle(1, 2, 3, 4);
	r2 = new Rectangle(3, 3, -1, -1);
	r1.intersect(r2);
	assertEquals(new Rectangle(0, 0, 0, 0), r1, "Rectangle intersect incorrect");

	Rectangle r = new Rectangle(1, 2, 3, 4);
	IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> r.intersect(null),
			"No exception thrown for rectangle == null");
	assertSWTProblem("Incorrect exception thrown for rectangle == null", SWT.ERROR_NULL_ARGUMENT, e);
}

@Test
public void test_intersectionLorg_eclipse_swt_graphics_Rectangle() {
	Rectangle r1 = new Rectangle(1, 2, 3, 4);
	Rectangle r2 = new Rectangle(3, 3, 2, 2);
	assertEquals(new Rectangle(3, 3, 1, 2), r1.intersection(r2), "Rectangle intersection incorrect");

	r2 = new Rectangle(3, 3, 0, 0);
	assertEquals(new Rectangle(3, 3, 0, 0), r1.intersection(r2), "Rectangle intersection incorrect");

	r2 = new Rectangle(3, 3, -1, -1);
	assertEquals(new Rectangle(0, 0, 0, 0), r1.intersection(r2), "Rectangle intersection incorrect");

	IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> r1.intersection(null),
			"No exception thrown for rectangle == null");
	assertSWTProblem("Incorrect exception thrown for rectangle == null", SWT.ERROR_NULL_ARGUMENT, e);
}

@Test
public void test_intersectsIIII() {
	Rectangle r1 = new Rectangle(1, 2, 3, 4);
	Rectangle r2 = new Rectangle(2, 3, 7, 8);
	assertTrue(r1.intersects(2, 3, 7, 8), "Rectangle(1, 2, 3, 4) should intersect Rectangle(2, 3, 7, 8)");
	assertTrue(r2.intersects(1, 2, 3, 4), "Rectangle(2, 3, 7, 8) should intersect Rectangle(1, 2, 3, 4)");

	r2 = new Rectangle(200, 300, 400, 500);
	assertFalse(r1.intersects(200, 300, 400, 500), "Rectangle(1, 2, 3, 4) should not intersect Rectangle(200, 300, 400, 500)");
	assertFalse(r2.intersects(1, 2, 3, 4), "Rectangle(200, 300, 400, 500) should not intersect Rectangle(1, 2, 3, 4)");

	r2 = new Rectangle(3, 3, 0, 0);
	assertTrue(r1.intersects(3, 3, 0, 0), "Rectangle(1, 2, 3, 4) should intersect Rectangle(3, 3, 0, 0)");
	assertTrue(r2.intersects(1, 2, 3, 4), "Rectangle(3, 3, 0, 0) should intersect Rectangle(1, 2, 3, 4)");

	r2 = new Rectangle(3, 3, -1, -1);
	assertFalse(r1.intersects(3, 3, -1, -1), "Rectangle(1, 2, 3, 4) should not intersect Rectangle(3, 3, -1, -1)");
	assertFalse(r2.intersects(1, 2, 3, 4), "Rectangle(3, 3, -1, -1) should not intersect Rectangle(1, 2, 3, 4)");

	IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> r1.intersects(null),
			"No exception thrown for rectangle == null");
	assertSWTProblem("Incorrect exception thrown for rectangle == null", SWT.ERROR_NULL_ARGUMENT, e);
}

@Test
public void test_intersectsLorg_eclipse_swt_graphics_Rectangle() {
	Rectangle r1 = new Rectangle(1, 2, 3, 4);
	Rectangle r2 = new Rectangle(2, 3, 7, 8);
	assertTrue(r1.intersects(r2), "Rectangle(1, 2, 3, 4) should intersect Rectangle(2, 3, 7, 8)");
	assertTrue(r2.intersects(r1), "Rectangle(2, 3, 7, 8) should intersect Rectangle(1, 2, 3, 4)");

	r2 = new Rectangle(200, 300, 400, 500);
	assertFalse(r1.intersects(r2), "Rectangle(1, 2, 3, 4) should not intersect Rectangle(200, 300, 400, 500)");
	assertFalse(r2.intersects(r1), "Rectangle(200, 300, 400, 500) should not intersect Rectangle(1, 2, 3, 4)");

	r2 = new Rectangle(3, 3, 0, 0);
	assertTrue(r1.intersects(r2), "Rectangle(1, 2, 3, 4) should intersect Rectangle(3, 3, 0, 0)");
	assertTrue(r2.intersects(r1), "Rectangle(3, 3, 0, 0) should intersect Rectangle(1, 2, 3, 4)");

	r2 = new Rectangle(3, 3, -1, -1);
	assertFalse(r1.intersects(r2), "Rectangle(1, 2, 3, 4) should not intersect Rectangle(3, 3, -1, -1)");
	assertFalse(r2.intersects(r1), "Rectangle(3, 3, -1, -1) should not intersect Rectangle(1, 2, 3, 4)");

	IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> r1.intersects(null),
			"No exception thrown for rectangle == null");
	assertSWTProblem("Incorrect exception thrown for rectangle == null", SWT.ERROR_NULL_ARGUMENT, e);
}

@Test
public void test_isEmpty() {
	Rectangle r = new Rectangle(1, 2, 0, 0);
	assertTrue(r.isEmpty(), "Rectangle is empty");

	r = new Rectangle(1, 2, -3, -4);
	assertTrue(r.isEmpty(), "Rectangle is empty");

	r = new Rectangle(1, 2, 3, 4);
	assertFalse(r.isEmpty(), "Rectangle is not empty");
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
	assertEquals(new Rectangle(1, 2, 4, 4), r1.union(r2), "Rectangle union incorrect");

	r2 = new Rectangle(3, 3, 0, 0);
	assertEquals(new Rectangle(1, 2, 3, 4), r1.union(r2), "Rectangle union incorrect");

	r2 = new Rectangle(3, 3, -1, -1);
	assertEquals(new Rectangle(1, 2, 3, 4), r1.union(r2), "Rectangle union incorrect");

	IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> r1.union(null),
			"No exception thrown for rectangle == null");
	assertSWTProblem("Incorrect exception thrown for rectangle == null", SWT.ERROR_NULL_ARGUMENT, e);
}

}
