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
import static org.junit.Assume.assumeFalse;

import org.eclipse.swt.tests.junit.SwtTestUtil;
import org.eclipse.swt.widgets.Display;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

/**
 * Automated Test Suite for class org.eclipse.swt.browser.StatusTextListener
 *
 * @see org.eclipse.swt.browser.StatusTextListener
 */
public class Test_BrowserSuite {

@Rule
public TestName name = new TestName();

String skipMsg() {
	return "Test_BrowserSuite." + name.getMethodName() + "() skipped, see bug 499159, 509411";
}






@Test
public void testBrowser6() {
	assumeFalse(skipMsg(), SwtTestUtil.isRunningOnEclipseOrgHudsonGTK);
	manualSetUp();
	assertTrue(Browser6_title_change_listener.test());
	manualTearDown();
}

@Test
public void testBrowser7() {
	manualSetUp();
	assertTrue(Browser7_child_browsers.test());
	manualTearDown();
}

@Test
public void testBrowser8() {
	manualSetUp();
	assertTrue(Browser8_execute_fromMemory.test());
	manualTearDown();
}

//Bug 509658. Display.dispose can cause webkit1 to crash in some cases.
// Thus we have a situation where the setup causes a crash even in test cases
// that are marked not to be run on hudson. This is very confusing.
//
// For now, to avoid calling display.dispose for problematic test webkit1 test cases
// we call setup/teardown manually.
public void manualSetUp() {
	System.out.println("Test_BrowserSuite#setUp(): " + name.getMethodName());
	Display display = Display.getCurrent();
	if (display != null) display.dispose();
}

public void manualTearDown() {
	Display display = Display.getCurrent();
	if (display != null) display.dispose();
}
}
