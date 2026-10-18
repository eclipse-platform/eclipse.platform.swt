/*******************************************************************************
 * Copyright (c) 2000, 2018 IBM Corporation and others.
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
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.eclipse.swt.graphics.FontMetrics;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Automated Test Suite for class org.eclipse.swt.graphics.FontMetrics
 *
 * @see org.eclipse.swt.graphics.FontMetrics
 */
public class Test_org_eclipse_swt_graphics_FontMetrics {

@BeforeEach
public void setUp() {
	display = Display.getDefault();
	shell = new Shell(display);
	gc = new GC(shell);
}

@AfterEach
public void tearDown() {
	gc.dispose();
	shell.dispose();
}

@Test
public void test_equalsLjava_lang_Object() {
	FontMetrics fm1 = gc.getFontMetrics();
	FontMetrics fm2 = gc.getFontMetrics();
	assertTrue(fm1.equals(fm2));
}

@Test
public void test_getAscent() {
	FontMetrics fm = gc.getFontMetrics();
	fm.getAscent();
}

@Test
public void test_getAverageCharacterWidth() {
	FontMetrics fm = gc.getFontMetrics();
	fm.getAverageCharacterWidth();
}

@SuppressWarnings("deprecation")
@Test
public void test_getAverageCharWidth() {
	FontMetrics fm = gc.getFontMetrics();
	fm.getAverageCharWidth();
}

@Test
public void test_getDescent() {
	FontMetrics fm = gc.getFontMetrics();
	fm.getDescent();
}

@Test
public void test_getHeight() {
	FontMetrics fm = gc.getFontMetrics();
	fm.getHeight();
}

@Test
public void test_getLeading() {
	FontMetrics fm = gc.getFontMetrics();
	fm.getLeading();
}

@Test
public void test_hashCode() {
	FontMetrics fm1 = gc.getFontMetrics();
	FontMetrics fm2 = gc.getFontMetrics();
	if (fm1.equals(fm2)) {
		assertEquals(fm1.hashCode(), fm2.hashCode());
	}
}

/* custom */
	Display display;
	Shell shell;
	GC gc;
}
