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

import junit.framework.TestCase;

import org.eclipse.swt.widgets.Display;

/**
 * Automated Test Suite for class org.eclipse.swt.browser.StatusTextListener
 *
 * @see org.eclipse.swt.browser.StatusTextListener
 */
public class Test_BrowserSuite extends TestCase {

public void testBrowser1() {
	assertTrue(Browser1.test());
}

public void testBrowser2() {
	assertTrue(Browser2.test());
}

public void testBrowser3() {
	assertTrue(Browser3.test());
}

public void testBrowser4() {
	assertTrue(Browser4.test());
}

public void testBrowser5() {
	assertTrue(Browser5.test());
}

public void testBrowser6() {
	assertTrue(Browser6.test());
}

public void testBrowser7() {
	assertTrue(Browser7.test());
}

public void testBrowser8() {
	assertTrue(Browser8.test());
}

public void testBrowser9() {
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
