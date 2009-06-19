/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
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
 * Automated Test Suite for class org.eclipse.swt.accessibility.AccessibleControlAdapter
 *
 * @see org.eclipse.swt.accessibility.AccessibleControlAdapter
 */
public class Test_org_eclipse_swt_accessibility_AccessibleControlAdapter extends SwtTestCase {

public Test_org_eclipse_swt_accessibility_AccessibleControlAdapter(String name) {
	super(name);
}

public static void main(String[] args) {
	TestRunner.run(suite());
}

public void test_Constructor() {
	warnUnimpl("Test test_Constructor not written");
}

public void test_getChildAtPointLorg_eclipse_swt_accessibility_AccessibleControlEvent() {
	warnUnimpl("Test test_getChildAtPointLorg_eclipse_swt_accessibility_AccessibleControlEvent not written");
}

public void test_getChildCountLorg_eclipse_swt_accessibility_AccessibleControlEvent() {
	warnUnimpl("Test test_getChildCountLorg_eclipse_swt_accessibility_AccessibleControlEvent not written");
}

public void test_getChildLorg_eclipse_swt_accessibility_AccessibleControlEvent() {
	warnUnimpl("Test test_getChildLorg_eclipse_swt_accessibility_AccessibleControlEvent not written");
}

public void test_getChildrenLorg_eclipse_swt_accessibility_AccessibleControlEvent() {
	warnUnimpl("Test test_getChildrenLorg_eclipse_swt_accessibility_AccessibleControlEvent not written");
}

public void test_getDefaultActionLorg_eclipse_swt_accessibility_AccessibleControlEvent() {
	warnUnimpl("Test test_getDefaultActionLorg_eclipse_swt_accessibility_AccessibleControlEvent not written");
}

public void test_getFocusLorg_eclipse_swt_accessibility_AccessibleControlEvent() {
	warnUnimpl("Test test_getFocusLorg_eclipse_swt_accessibility_AccessibleControlEvent not written");
}

public void test_getLocationLorg_eclipse_swt_accessibility_AccessibleControlEvent() {
	warnUnimpl("Test test_getLocationLorg_eclipse_swt_accessibility_AccessibleControlEvent not written");
}

public void test_getRoleLorg_eclipse_swt_accessibility_AccessibleControlEvent() {
	warnUnimpl("Test test_getRoleLorg_eclipse_swt_accessibility_AccessibleControlEvent not written");
}

public void test_getSelectionLorg_eclipse_swt_accessibility_AccessibleControlEvent() {
	warnUnimpl("Test test_getSelectionLorg_eclipse_swt_accessibility_AccessibleControlEvent not written");
}

public void test_getStateLorg_eclipse_swt_accessibility_AccessibleControlEvent() {
	warnUnimpl("Test test_getStateLorg_eclipse_swt_accessibility_AccessibleControlEvent not written");
}

public void test_getValueLorg_eclipse_swt_accessibility_AccessibleControlEvent() {
	warnUnimpl("Test test_getValueLorg_eclipse_swt_accessibility_AccessibleControlEvent not written");
}


public static Test suite() {
	TestSuite suite = new TestSuite();
	java.util.Vector methodNames = methodNames();
	java.util.Enumeration e = methodNames.elements();
	while (e.hasMoreElements()) {
		suite.addTest(new Test_org_eclipse_swt_accessibility_AccessibleControlAdapter((String)e.nextElement()));
	}
	return suite;
}

public static java.util.Vector methodNames() {
	java.util.Vector methodNames = new java.util.Vector();
	methodNames.addElement("test_Constructor");
	methodNames.addElement("test_getChildAtPointLorg_eclipse_swt_accessibility_AccessibleControlEvent");
	methodNames.addElement("test_getChildCountLorg_eclipse_swt_accessibility_AccessibleControlEvent");
	methodNames.addElement("test_getChildLorg_eclipse_swt_accessibility_AccessibleControlEvent");
	methodNames.addElement("test_getChildrenLorg_eclipse_swt_accessibility_AccessibleControlEvent");
	methodNames.addElement("test_getDefaultActionLorg_eclipse_swt_accessibility_AccessibleControlEvent");
	methodNames.addElement("test_getFocusLorg_eclipse_swt_accessibility_AccessibleControlEvent");
	methodNames.addElement("test_getLocationLorg_eclipse_swt_accessibility_AccessibleControlEvent");
	methodNames.addElement("test_getRoleLorg_eclipse_swt_accessibility_AccessibleControlEvent");
	methodNames.addElement("test_getSelectionLorg_eclipse_swt_accessibility_AccessibleControlEvent");
	methodNames.addElement("test_getStateLorg_eclipse_swt_accessibility_AccessibleControlEvent");
	methodNames.addElement("test_getValueLorg_eclipse_swt_accessibility_AccessibleControlEvent");
	return methodNames;
}

protected void runTest() throws Throwable {
	if (getName().equals("test_Constructor")) test_Constructor();
	else if (getName().equals("test_getChildAtPointLorg_eclipse_swt_accessibility_AccessibleControlEvent")) test_getChildAtPointLorg_eclipse_swt_accessibility_AccessibleControlEvent();
	else if (getName().equals("test_getChildCountLorg_eclipse_swt_accessibility_AccessibleControlEvent")) test_getChildCountLorg_eclipse_swt_accessibility_AccessibleControlEvent();
	else if (getName().equals("test_getChildLorg_eclipse_swt_accessibility_AccessibleControlEvent")) test_getChildLorg_eclipse_swt_accessibility_AccessibleControlEvent();
	else if (getName().equals("test_getChildrenLorg_eclipse_swt_accessibility_AccessibleControlEvent")) test_getChildrenLorg_eclipse_swt_accessibility_AccessibleControlEvent();
	else if (getName().equals("test_getDefaultActionLorg_eclipse_swt_accessibility_AccessibleControlEvent")) test_getDefaultActionLorg_eclipse_swt_accessibility_AccessibleControlEvent();
	else if (getName().equals("test_getFocusLorg_eclipse_swt_accessibility_AccessibleControlEvent")) test_getFocusLorg_eclipse_swt_accessibility_AccessibleControlEvent();
	else if (getName().equals("test_getLocationLorg_eclipse_swt_accessibility_AccessibleControlEvent")) test_getLocationLorg_eclipse_swt_accessibility_AccessibleControlEvent();
	else if (getName().equals("test_getRoleLorg_eclipse_swt_accessibility_AccessibleControlEvent")) test_getRoleLorg_eclipse_swt_accessibility_AccessibleControlEvent();
	else if (getName().equals("test_getSelectionLorg_eclipse_swt_accessibility_AccessibleControlEvent")) test_getSelectionLorg_eclipse_swt_accessibility_AccessibleControlEvent();
	else if (getName().equals("test_getStateLorg_eclipse_swt_accessibility_AccessibleControlEvent")) test_getStateLorg_eclipse_swt_accessibility_AccessibleControlEvent();
	else if (getName().equals("test_getValueLorg_eclipse_swt_accessibility_AccessibleControlEvent")) test_getValueLorg_eclipse_swt_accessibility_AccessibleControlEvent();
}
}
