/*******************************************************************************
 * Copyright (c) 2000, 2020 IBM Corporation and others.
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


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.graphics.Color;
import org.junit.jupiter.api.Test;

/**
 * Automated Test Suite for class org.eclipse.swt.custom.StyleRange
 *
 * @see org.eclipse.swt.custom.StyleRange
 */
public class Test_org_eclipse_swt_custom_StyleRange {
	final static Color RED = new Color(255,0,0);
	final static Color BLUE = new Color(0,0,255);
	final static Color GREEN = new Color(0,255,0);

@Test
public void test_Constructor() {
	StyleRange styleRange = new StyleRange();
	assertEquals(0, styleRange.start);
	assertEquals(0, styleRange.length);
	assertNull(styleRange.foreground);
	assertNull(styleRange.background);
	assertEquals(SWT.NORMAL, styleRange.fontStyle);
}

@Test
public void test_ConstructorIILorg_eclipse_swt_graphics_ColorLorg_eclipse_swt_graphics_Color() {
	StyleRange styleRange = new StyleRange(5, 10, RED, BLUE);
	assertEquals(5, styleRange.start);
	assertEquals(10, styleRange.length);
	assertEquals(RED, styleRange.foreground);
	assertEquals(BLUE, styleRange.background);
	assertEquals(SWT.NORMAL, styleRange.fontStyle);
}

@Test
public void test_ConstructorIILorg_eclipse_swt_graphics_ColorLorg_eclipse_swt_graphics_ColorI() {
	StyleRange styleRange = new StyleRange(5, 10, RED, BLUE, SWT.BOLD);
	assertEquals(5, styleRange.start);
	assertEquals(10, styleRange.length);
	assertEquals(RED, styleRange.foreground);
	assertEquals(BLUE, styleRange.background);
	assertEquals(SWT.BOLD, styleRange.fontStyle);
}

@Test
public void test_clone() {
	StyleRange styleRangeA = new StyleRange(6, 10, null, BLUE, SWT.NORMAL);
	StyleRange styleRangeB = (StyleRange)styleRangeA.clone();
	assertTrue(styleRangeA.equals(styleRangeB));
}

@Test
public void test_equalsLjava_lang_Object() {
	StyleRange styleRangeA = new StyleRange(5, 10, RED, BLUE, SWT.BOLD);
	StyleRange styleRangeB = new StyleRange(5, 10, RED, BLUE, SWT.BOLD);
	StyleRange styleRangeC = new StyleRange(5, 10, BLUE, BLUE, SWT.BOLD);
	StyleRange styleRangeD = new StyleRange(6, 10, RED, BLUE, SWT.BOLD);
	StyleRange styleRangeE = new StyleRange(5, 11, RED, BLUE, SWT.BOLD);
	StyleRange styleRangeF = new StyleRange(5, 11, RED, RED, SWT.BOLD);
	StyleRange styleRangeG = new StyleRange(5, 11, RED, BLUE, SWT.NORMAL);
	assertTrue(styleRangeA.equals(styleRangeB));
	assertFalse(styleRangeA.equals(styleRangeC));
	assertFalse(styleRangeA.equals(styleRangeD));
	assertFalse(styleRangeA.equals(styleRangeE));
	assertFalse(styleRangeA.equals(styleRangeF));
	assertFalse(styleRangeA.equals(styleRangeG));
}

@Test
public void test_hashCode() {
	Set<StyleRange> set = new HashSet<>();
	Color testColor = new Color(0, 0, 0);

	//regression test for bug 30924
	set.add(new StyleRange());
	set.add(new StyleRange(0, 1, testColor, null));
	set.add(new StyleRange(0, 1, testColor, testColor));
	set.add(new StyleRange(0, 1, testColor, testColor, SWT.BOLD));
}

@Test
public void test_isUnstyled() {
	StyleRange styleRangeA = new StyleRange(5, 10, null, null, SWT.NORMAL);
	StyleRange styleRangeB = new StyleRange(5, 10, RED, null, SWT.NORMAL);
	StyleRange styleRangeC = new StyleRange(5, 10, null, null, SWT.BOLD);
	StyleRange styleRangeD = new StyleRange(6, 10, null, BLUE, SWT.NORMAL);
	StyleRange styleRangeE = new StyleRange(5, 10, null, null);
	assertTrue(styleRangeA.isUnstyled());
	assertFalse(styleRangeB.isUnstyled());
	assertFalse(styleRangeC.isUnstyled());
	assertFalse(styleRangeD.isUnstyled());
	assertTrue(styleRangeE.isUnstyled());
}

@Test
public void test_similarToLorg_eclipse_swt_custom_StyleRange() {
	StyleRange styleRangeA = new StyleRange(6, 10, RED, BLUE, SWT.NORMAL);
	StyleRange styleRangeB = new StyleRange(5, 5, RED, BLUE, SWT.NORMAL);
	StyleRange styleRangeC = new StyleRange(6, 10, RED, BLUE, SWT.NORMAL);
	StyleRange styleRangeD = new StyleRange(6, 10, BLUE, BLUE, SWT.NORMAL);
	StyleRange styleRangeE = new StyleRange(6, 10, RED, RED, SWT.NORMAL);
	StyleRange styleRangeF = new StyleRange(6, 10, RED, BLUE, SWT.BOLD);
	assertTrue(styleRangeA.similarTo(styleRangeB));
	assertTrue(styleRangeA.similarTo(styleRangeC));
	assertFalse(styleRangeA.similarTo(styleRangeD));
	assertFalse(styleRangeA.similarTo(styleRangeE));
	assertFalse(styleRangeA.similarTo(styleRangeF));
}

@Test
public void test_toString() {
	StyleRange styleRange = new StyleRange(6, 10, null, BLUE, SWT.NORMAL);
	styleRange.toString();
}
}
