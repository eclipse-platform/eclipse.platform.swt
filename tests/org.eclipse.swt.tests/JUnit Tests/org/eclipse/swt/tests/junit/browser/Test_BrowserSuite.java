/*******************************************************************************
 * Copyright (c) 2000, 2016 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tests.junit.browser;

import static org.junit.Assert.assertTrue;

import org.eclipse.swt.tests.junit.SwtTestUtil;
import org.eclipse.swt.widgets.Display;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

/**
 * Automated Test Suite for class org.eclipse.swt.browser.StatusTextListener
 *
 * @see org.eclipse.swt.browser.StatusTextListener
 */
public class Test_BrowserSuite {

public static boolean isRunningOnEclipseOrgHudsonGTK = SwtTestUtil.isGTK
		&& ("hudsonbuild".equalsIgnoreCase(System.getProperty("user.name"))
				|| "genie.platform".equalsIgnoreCase(System.getProperty("user.name")));

@Rule
public TestName name = new TestName();

@Test
public void testBrowser1() {
	if (isRunningOnEclipseOrgHudsonGTK) {
		System.out.println("Test_BrowserSuite." + name.getMethodName() + "() skipped, see bug 499159");
		return;
	}
	assertTrue(Browser1_location_progress_fromURL.test());
}

@Test
public void testBrowser2() {
	assertTrue(Browser2_location_and_progress_advanced.test());
}

@Test
public void testBrowser3() {
	if (isRunningOnEclipseOrgHudsonGTK) {
		System.out.println("Test_BrowserSuite." + name.getMethodName() + "() skipped, see bug 499159");
		return;
	}
	assertTrue(Browser3_window_open_visibility_listeners.test());
}

@Test
public void testBrowser4() {
	assertTrue(Browser4_window_close_listener.test());
}

@Test
public void testBrowser5() {
	assertTrue(Browser5_sizing_and_bounds.test());
}

@Test
public void testBrowser6() {
	if (isRunningOnEclipseOrgHudsonGTK) {
		System.out.println("Test_BrowserSuite." + name.getMethodName() + "() skipped, see bug 499159");
		return;
	}
	assertTrue(Browser6_title_change_listener.test());
}

@Test
public void testBrowser7() {
	assertTrue(Browser7_child_browsers.test());
}

@Test
public void testBrowser8() {
	assertTrue(Browser8_execute_fromMemory.test());
}

@Test
public void testBrowser9() {
	assertTrue(Browser9_execute_fromFile.test());
}

@Before
public void setUp() {
	System.out.println("Browser#setUp(): " + name.getMethodName());
	Display display = Display.getCurrent();
	if (display != null) display.dispose();
}

@After
public void tearDown() {
	Display display = Display.getCurrent();
	if (display != null) display.dispose();
}
}
