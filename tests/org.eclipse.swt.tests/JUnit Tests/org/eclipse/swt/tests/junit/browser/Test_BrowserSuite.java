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

import org.eclipse.swt.widgets.Display;

import junit.framework.TestCase;

/**
 * Automated Test Suite for class org.eclipse.swt.browser.StatusTextListener
 *
 * @see org.eclipse.swt.browser.StatusTextListener
 */
public class Test_BrowserSuite extends TestCase {

public void testBrowser1() {
	Browser1.verbose = true;
assertTrue(Browser1.test());
}

public void testBrowser2() {
	Browser2.verbose = true;
assertTrue(Browser2.test());
}

public void testBrowser3() {
	Browser3.verbose = true;
assertTrue(Browser3.test());
}

public void testBrowser4() {
	Browser4.verbose = true;
assertTrue(Browser4.test());
}

public void testBrowser5() {
	Browser5.verbose = true;
assertTrue(Browser5.test());
}

public void testBrowser6() {
	Browser6.verbose = true;
assertTrue(Browser6.test());
}

public void testBrowser7() {
	Browser7.verbose = true;
assertTrue(Browser7.test());
}

public void testBrowser8() {
	Browser8.verbose = true;
assertTrue(Browser8.test());
}

public void testBrowser9() {
	Browser9.verbose = true;
assertTrue(Browser9.test());
}

@Override
protected void setUp() throws Exception {
	System.out.println("Browser#setUp(): " + getName());
	Display display = Display.getCurrent();
	if (display != null) display.dispose();
}

@Override
protected void tearDown() throws Exception {
	Display display = Display.getCurrent();
	if (display != null) display.dispose();
}
}
