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


import junit.framework.TestCase;

import org.eclipse.swt.graphics.FontMetrics;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * Automated Test Suite for class org.eclipse.swt.graphics.FontMetrics
 *
 * @see org.eclipse.swt.graphics.FontMetrics
 */
public class Test_org_eclipse_swt_graphics_FontMetrics extends TestCase {

@Override
protected void setUp() {
	display = Display.getDefault();
	shell = new Shell(display);
	gc = new GC(shell);
}

@Override
protected void tearDown() {
	gc.dispose();
	shell.dispose();
}

public void test_equalsLjava_lang_Object() {
	FontMetrics fm1 = gc.getFontMetrics();
	FontMetrics fm2 = gc.getFontMetrics();
	assertTrue(fm1.equals(fm2));
}

public void test_getAscent() {
	FontMetrics fm = gc.getFontMetrics();
	fm.getAscent();
}

public void test_getAverageCharWidth() {
	FontMetrics fm = gc.getFontMetrics();
	fm.getAverageCharWidth();
}

public void test_getDescent() {
	FontMetrics fm = gc.getFontMetrics();
	fm.getDescent();
}

public void test_getHeight() {
	FontMetrics fm = gc.getFontMetrics();
	fm.getHeight();
}

public void test_getLeading() {
	FontMetrics fm = gc.getFontMetrics();
	fm.getLeading();
}

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
