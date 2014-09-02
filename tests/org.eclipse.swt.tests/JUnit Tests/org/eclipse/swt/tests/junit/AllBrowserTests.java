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
	TestSuite suite = new TestSuite(AllBrowserTests.class.getName());
	suite.addTestSuite(Test_org_eclipse_swt_browser_Browser.class);	
	suite.addTestSuite(Test_org_eclipse_swt_browser_CloseWindowListener.class);
	suite.addTestSuite(Test_org_eclipse_swt_browser_LocationAdapter.class);
	suite.addTestSuite(Test_org_eclipse_swt_browser_LocationListener.class);
	suite.addTestSuite(Test_org_eclipse_swt_browser_OpenWindowListener.class);
	suite.addTestSuite(Test_org_eclipse_swt_browser_ProgressAdapter.class);
	suite.addTestSuite(Test_org_eclipse_swt_browser_ProgressListener.class);
	suite.addTestSuite(Test_org_eclipse_swt_browser_StatusTextListener.class);
	suite.addTestSuite(Test_org_eclipse_swt_browser_TitleListener.class);
	suite.addTestSuite(Test_org_eclipse_swt_browser_VisibilityWindowAdapter.class);
	suite.addTestSuite(Test_org_eclipse_swt_browser_VisibilityWindowListener.class);
	suite.addTestSuite(org.eclipse.swt.tests.junit.browser.Test_BrowserSuite.class);

	return suite;
}
}
