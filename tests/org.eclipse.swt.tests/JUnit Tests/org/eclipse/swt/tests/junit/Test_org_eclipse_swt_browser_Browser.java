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
 * Automated Test Suite for class org.eclipse.swt.browser.Browser
 *
 * @see org.eclipse.swt.browser.Browser
 */
public class Test_org_eclipse_swt_browser_Browser extends Test_org_eclipse_swt_widgets_Composite {

public Test_org_eclipse_swt_browser_Browser(String name) {
	super(name);
}

public static void main(String[] args) {
	TestRunner.run(suite());
}

protected void setUp() {
	super.setUp();
}

protected void tearDown() {
	super.tearDown();
}

public void test_ConstructorLorg_eclipse_swt_widgets_CompositeI() {
	warnUnimpl("Test test_ConstructorLorg_eclipse_swt_widgets_CompositeI not written");
}

public void test_addLocationListenerLorg_eclipse_swt_browser_LocationListener() {
	warnUnimpl("Test test_addLocationListenerLorg_eclipse_swt_browser_LocationListener not written");
}

public void test_addProgressListenerLorg_eclipse_swt_browser_ProgressListener() {
	warnUnimpl("Test test_addProgressListenerLorg_eclipse_swt_browser_ProgressListener not written");
}

public void test_addStatusTextListenerLorg_eclipse_swt_browser_StatusTextListener() {
	warnUnimpl("Test test_addStatusTextListenerLorg_eclipse_swt_browser_StatusTextListener not written");
}

public void test_back() {
	warnUnimpl("Test test_back not written");
}

public void test_forward() {
	warnUnimpl("Test test_forward not written");
}

public void test_getUrl() {
	warnUnimpl("Test test_getUrl not written");
}

public void test_refresh() {
	warnUnimpl("Test test_refresh not written");
}

public void test_removeLocationListenerLorg_eclipse_swt_browser_LocationListener() {
	warnUnimpl("Test test_removeLocationListenerLorg_eclipse_swt_browser_LocationListener not written");
}

public void test_removeProgressListenerLorg_eclipse_swt_browser_ProgressListener() {
	warnUnimpl("Test test_removeProgressListenerLorg_eclipse_swt_browser_ProgressListener not written");
}

public void test_removeStatusTextListenerLorg_eclipse_swt_browser_StatusTextListener() {
	warnUnimpl("Test test_removeStatusTextListenerLorg_eclipse_swt_browser_StatusTextListener not written");
}

public void test_setUrlLjava_lang_String() {
	warnUnimpl("Test test_setUrlLjava_lang_String not written");
}

public void test_stop() {
	warnUnimpl("Test test_stop not written");
}


public static Test suite() {
	TestSuite suite = new TestSuite();
	java.util.Vector methodNames = methodNames();
	java.util.Enumeration e = methodNames.elements();
	while (e.hasMoreElements()) {
		suite.addTest(new Test_org_eclipse_swt_browser_Browser((String)e.nextElement()));
	}
	return suite;
}

public static java.util.Vector methodNames() {
	java.util.Vector methodNames = new java.util.Vector();
	methodNames.addElement("test_ConstructorLorg_eclipse_swt_widgets_CompositeI");
	methodNames.addElement("test_addLocationListenerLorg_eclipse_swt_browser_LocationListener");
	methodNames.addElement("test_addProgressListenerLorg_eclipse_swt_browser_ProgressListener");
	methodNames.addElement("test_addStatusTextListenerLorg_eclipse_swt_browser_StatusTextListener");
	methodNames.addElement("test_back");
	methodNames.addElement("test_forward");
	methodNames.addElement("test_getUrl");
	methodNames.addElement("test_refresh");
	methodNames.addElement("test_removeLocationListenerLorg_eclipse_swt_browser_LocationListener");
	methodNames.addElement("test_removeProgressListenerLorg_eclipse_swt_browser_ProgressListener");
	methodNames.addElement("test_removeStatusTextListenerLorg_eclipse_swt_browser_StatusTextListener");
	methodNames.addElement("test_setUrlLjava_lang_String");
	methodNames.addElement("test_stop");
	methodNames.addAll(Test_org_eclipse_swt_widgets_Composite.methodNames()); // add superclass method names
	return methodNames;
}

protected void runTest() throws Throwable {
	if (getName().equals("test_ConstructorLorg_eclipse_swt_widgets_CompositeI")) test_ConstructorLorg_eclipse_swt_widgets_CompositeI();
	else if (getName().equals("test_addLocationListenerLorg_eclipse_swt_browser_LocationListener")) test_addLocationListenerLorg_eclipse_swt_browser_LocationListener();
	else if (getName().equals("test_addProgressListenerLorg_eclipse_swt_browser_ProgressListener")) test_addProgressListenerLorg_eclipse_swt_browser_ProgressListener();
	else if (getName().equals("test_addStatusTextListenerLorg_eclipse_swt_browser_StatusTextListener")) test_addStatusTextListenerLorg_eclipse_swt_browser_StatusTextListener();
	else if (getName().equals("test_back")) test_back();
	else if (getName().equals("test_forward")) test_forward();
	else if (getName().equals("test_getUrl")) test_getUrl();
	else if (getName().equals("test_refresh")) test_refresh();
	else if (getName().equals("test_removeLocationListenerLorg_eclipse_swt_browser_LocationListener")) test_removeLocationListenerLorg_eclipse_swt_browser_LocationListener();
	else if (getName().equals("test_removeProgressListenerLorg_eclipse_swt_browser_ProgressListener")) test_removeProgressListenerLorg_eclipse_swt_browser_ProgressListener();
	else if (getName().equals("test_removeStatusTextListenerLorg_eclipse_swt_browser_StatusTextListener")) test_removeStatusTextListenerLorg_eclipse_swt_browser_StatusTextListener();
	else if (getName().equals("test_setUrlLjava_lang_String")) test_setUrlLjava_lang_String();
	else if (getName().equals("test_stop")) test_stop();
	else super.runTest();
}
}
