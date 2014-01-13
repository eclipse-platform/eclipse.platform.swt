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


import junit.framework.TestCase;

import org.eclipse.swt.graphics.Point;

/**
 * Automated Test Suite for class org.eclipse.swt.graphics.Point
 *
 * @see org.eclipse.swt.graphics.Point
 */
public class Test_org_eclipse_swt_graphics_Point extends TestCase {

public void test_ConstructorII() {
	// Test new Point (int x, int y)
	Point p = new Point(3, 4);
	assertEquals(3, p.x);
	assertEquals(4, p.y);

	p = new Point(-4, -3);
	assertEquals(-4, p.x);
	assertEquals(-3, p.y);

	p = new Point(500000, 700000);
	assertEquals(500000, p.x);
	assertEquals(700000, p.y);
}

public void test_equalsLjava_lang_Object() {
	Point p1 = new Point(5, 5);
	Point p2 = new Point(5, 5);
	assertTrue("Points should be equal", p1.equals(p2));

	p1 = new Point(-5, -5);
	p2 = new Point(-5, -5);
	assertTrue("Points should be equal", p1.equals(p2));

	p2 = new Point(3, 4);
	assertTrue("Points should not be equal", !p1.equals(p2));
}

public void test_hashCode() {
	Point p1 = new Point(5, 5);
	Point p2 = new Point(5, 5);
	assertTrue("Points should have the same hashCode", p1.hashCode() == p2.hashCode());
}

public void test_toString() {
	Point p = new Point(3, 4);
	assertNotNull(p.toString());
	assertTrue(p.toString().length() > 0);
	assertEquals("Point {3, 4}", p.toString());
}
}
