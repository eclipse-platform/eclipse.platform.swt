/*******************************************************************************
 * Copyright (c) 2000, 2014 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tests.junit;


import org.junit.runner.JUnitCore;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Suite for testing all of the graphics test cases.
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
	Test_org_eclipse_swt_browser_Browser.class,	
	Test_org_eclipse_swt_browser_CloseWindowListener.class,
	Test_org_eclipse_swt_browser_LocationAdapter.class,
	Test_org_eclipse_swt_browser_LocationListener.class,
	Test_org_eclipse_swt_browser_OpenWindowListener.class,
	Test_org_eclipse_swt_browser_ProgressAdapter.class,
	Test_org_eclipse_swt_browser_ProgressListener.class,
	Test_org_eclipse_swt_browser_StatusTextListener.class,
	Test_org_eclipse_swt_browser_TitleListener.class,
	Test_org_eclipse_swt_browser_VisibilityWindowAdapter.class,
	Test_org_eclipse_swt_browser_VisibilityWindowListener.class,
	org.eclipse.swt.tests.junit.browser.Test_BrowserSuite.class,
})
public class AllBrowserTests {
	
public static void main(String[] args) {
	JUnitCore.main(AllBrowserTests.class.getName());
}
}
