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


import junit.framework.*;
import junit.textui.*;

/**
 * Suite for testing all of the graphics test cases.
 */
public class AllBrowserTests {
	
public static void main(String[] args) {
	TestRunner.run (suite());
}
public static Test suite() {
	TestSuite suite = new TestSuite();
	suite.addTest(Test_org_eclipse_swt_browser_Browser.suite());	
	suite.addTest(Test_org_eclipse_swt_browser_CloseWindowListener.suite());
	suite.addTest(Test_org_eclipse_swt_browser_LocationAdapter.suite());
	suite.addTest(Test_org_eclipse_swt_browser_LocationListener.suite());
	suite.addTest(Test_org_eclipse_swt_browser_OpenWindowListener.suite());
	suite.addTest(Test_org_eclipse_swt_browser_ProgressAdapter.suite());
	suite.addTest(Test_org_eclipse_swt_browser_ProgressListener.suite());
	suite.addTest(Test_org_eclipse_swt_browser_StatusTextListener.suite());
	suite.addTest(Test_org_eclipse_swt_browser_TitleListener.suite());
	suite.addTest(Test_org_eclipse_swt_browser_VisibilityWindowAdapter.suite());
	suite.addTest(Test_org_eclipse_swt_browser_VisibilityWindowListener.suite());
	suite.addTest(org.eclipse.swt.tests.junit.browser.Test_BrowserSuite.suite());

	return suite;
}
}
