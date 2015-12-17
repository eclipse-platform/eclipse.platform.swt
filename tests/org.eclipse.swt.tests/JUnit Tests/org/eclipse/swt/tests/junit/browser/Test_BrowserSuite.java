/*******************************************************************************
 * Copyright (c) 2000, 2009 IBM Corporation and others.
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

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.internal.mozilla.MozillaVersion;
import org.eclipse.swt.tests.junit.SwtTestUtil;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
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
	
public static boolean isRunningOnEclipseOrgHudsonGTK = SwtTestUtil.isGTK && "hudsonbuild".equals(System.getProperty("user.name"));

@Rule public TestName name = new TestName();

private static boolean logXulRunnerVersion() {
	Display display = new Display();
	try {
		Shell shell = new Shell(display);
		Browser browser = new Browser(shell, SWT.NONE);
		String browserType = browser.getBrowserType();
		if ("mozilla".equals(browserType)) {
			int version = MozillaVersion.GetCurrentVersion();
			System.out.println("Test_BrowserSuite: MozillaVersion.GetCurrentVersion(): " + version);
			return version <= MozillaVersion.VERSION_XR1_9_2;
		}
		return false;
	} finally {
		display.dispose();
	}
}

@Test
public void testBrowser1() {
	logXulRunnerVersion();
	if (isRunningOnEclipseOrgHudsonGTK) {
		System.out.println("Test_BrowserSuite.testBrowser1() skipped, see bug 465721");
		return;
	}
	assertTrue(Browser1.test());
}

@Test
public void testBrowser2() {
	assertTrue(Browser2.test());
}

@Test
public void testBrowser3() {
	assertTrue(Browser3.test());
}

@Test
public void testBrowser4() {
	assertTrue(Browser4.test());
}

@Test
public void testBrowser5() {
	if (isRunningOnEclipseOrgHudsonGTK) {
		System.out.println("Test_BrowserSuite.testBrowser1() skipped, see bug 465721");
		return;
	}
	assertTrue(Browser5.test());
}

@Test
public void testBrowser6() {
	if (isRunningOnEclipseOrgHudsonGTK) {
		System.out.println("Test_BrowserSuite.testBrowser1() skipped, see bug 465721");
		return;
	}
	assertTrue(Browser6.test());
}

@Test
public void testBrowser7() {
	assertTrue(Browser7.test());
}

@Test
public void testBrowser8() {
	if (isRunningOnEclipseOrgHudsonGTK) {
		System.out.println("Test_BrowserSuite.testBrowser1() skipped, see bug 465721");
		return;
	}
	assertTrue(Browser8.test());
}

@Test
public void testBrowser9() {
	if (isRunningOnEclipseOrgHudsonGTK) {
		System.out.println("Test_BrowserSuite.testBrowser1() skipped, see bug 465721");
		return;
	}
	assertTrue(Browser9.test());
}

@Before
public void setUp() throws Exception {
	System.out.println("Browser#setUp(): " + name.getMethodName());
	Display display = Display.getCurrent();
	if (display != null) display.dispose();
}

@After
public void tearDown() throws Exception {
	Display display = Display.getCurrent();
	if (display != null) display.dispose();
}
}
