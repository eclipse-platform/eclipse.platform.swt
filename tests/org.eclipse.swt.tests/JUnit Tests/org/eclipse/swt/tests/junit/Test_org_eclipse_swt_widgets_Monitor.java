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

import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Monitor;

/**
 * Automated Test Suite for class org.eclipse.swt.widgets.Monitor
 *
 * @see org.eclipse.swt.widgets.Monitor
 */
public class Test_org_eclipse_swt_widgets_Monitor extends TestCase {

@Override
protected void setUp() {
	display = Display.getDefault();
	monitors = display.getMonitors();
	primary = display.getPrimaryMonitor();
}

public void test_equalsLjava_lang_Object() {
	int i;
	for (i = 0; i < monitors.length; i++) {
		if (primary.equals(monitors[i])) break;
	}
	if (i == monitors.length) fail();
	for (i = 0; i  < monitors.length; i++) {
		Monitor test = monitors[i];
		for (int j = 0; j < monitors.length; j++) {
			if (test.equals(monitors[j])) {
				if (i != j) fail("Monitors "+i+" and "+j+" should not be equal");
			}
		}
	}
}

public void test_getBounds() {
	Rectangle bounds = primary.getBounds();
	assertNotNull(bounds);
	for (int i = 0; i < monitors.length; i++) {
		bounds = monitors[i].getBounds();
		assertNotNull(bounds);
	}
}

public void test_getClientArea() {
	Rectangle bounds = primary.getClientArea();
	assertNotNull(bounds);
	for (int i = 0; i < monitors.length; i++) {
		bounds = monitors[i].getClientArea();
		assertNotNull(bounds);
	}
}

public void test_hashCode() {
	for (int i = 0; i < monitors.length; i++) {
		if (primary.equals(monitors[i])) {
			assertTrue(primary.hashCode() == monitors[i].hashCode());
			break;
		}
	}
}

/* custom */
Display display = null;
Monitor[] monitors = null;
Monitor primary = null;
}
