/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
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
//@todo regression caused by Display/Control change on 19/04/2004
//	suite.addTest(Test_org_eclipse_swt_browser_Browser.suite());
	
//	@todo bug 62760 releng junit tests don't start from eclipse
//  launcher. Disable junit tests until resolved.
//	suite.addTest(Test_org_eclipse_swt_browser_CloseWindowListener.suite());
//	suite.addTest(Test_org_eclipse_swt_browser_LocationAdapter.suite());
//	suite.addTest(Test_org_eclipse_swt_browser_LocationListener.suite());
//	suite.addTest(Test_org_eclipse_swt_browser_OpenWindowListener.suite());
//	suite.addTest(Test_org_eclipse_swt_browser_ProgressAdapter.suite());
//	suite.addTest(Test_org_eclipse_swt_browser_ProgressListener.suite());
//	suite.addTest(Test_org_eclipse_swt_browser_StatusTextListener.suite());
//	suite.addTest(Test_org_eclipse_swt_browser_TitleListener.suite());
//	suite.addTest(Test_org_eclipse_swt_browser_VisibilityWindowAdapter.suite());
//	suite.addTest(Test_org_eclipse_swt_browser_VisibilityWindowListener.suite());
//	suite.addTest(org.eclipse.swt.tests.junit.browser.Test_BrowserSuite.suite());

	return suite;
}
}
