/*******************************************************************************
 * Copyright (c) 2000, 2015 IBM Corporation and others.
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
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Monitor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


/**
 * Automated Test Suite for class org.eclipse.swt.widgets.Monitor
 *
 * @see org.eclipse.swt.widgets.Monitor
 */
public class Test_org_eclipse_swt_widgets_Monitor {

@BeforeEach
public void setUp() {
	display = Display.getDefault();
	monitors = display.getMonitors();
	primary = display.getPrimaryMonitor();
}

@Test
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

@Test
public void test_getBounds() {
	Rectangle bounds = primary.getBounds();
	assertNotNull(bounds);
	for (Monitor monitor : monitors) {
		bounds = monitor.getBounds();
		assertNotNull(bounds);
	}
}

@Test
public void test_getClientArea() {
	Rectangle bounds = primary.getClientArea();
	assertNotNull(bounds);
	for (Monitor monitor : monitors) {
		bounds = monitor.getClientArea();
		assertNotNull(bounds);
	}
}

@Test
public void test_getZoom() {
	int zoom = primary.getZoom();
	assertNotEquals(0, zoom);
	for (Monitor monitor : monitors) {
		zoom = monitor.getZoom();
		assertNotEquals(0, zoom);
	}
}

@Test
public void test_hashCode() {
	for (Monitor monitor : monitors) {
		if (primary.equals(monitor)) {
			assertEquals(monitor.hashCode(), primary.hashCode());
			break;
		}
	}
}

/* custom */
Display display = null;
Monitor[] monitors = null;
Monitor primary = null;
}
