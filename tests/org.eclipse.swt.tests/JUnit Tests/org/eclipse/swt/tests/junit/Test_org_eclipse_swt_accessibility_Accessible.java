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
 * Automated Test Suite for class org.eclipse.swt.accessibility.Accessible
 *
 * @see org.eclipse.swt.accessibility.Accessible
 */
public class Test_org_eclipse_swt_accessibility_Accessible extends SwtTestCase {

public Test_org_eclipse_swt_accessibility_Accessible(String name) {
	super(name);
}

public static void main(String[] args) {
	TestRunner.run(suite());
}

protected void setUp() {
}

protected void tearDown() {
}

public void test_addAccessibleControlListenerLorg_eclipse_swt_accessibility_AccessibleControlListener() {
	warnUnimpl("Test test_addAccessibleControlListenerLorg_eclipse_swt_accessibility_AccessibleControlListener not written");
}

public void test_addAccessibleListenerLorg_eclipse_swt_accessibility_AccessibleListener() {
	warnUnimpl("Test test_addAccessibleListenerLorg_eclipse_swt_accessibility_AccessibleListener not written");
}

public void test_internal_WM_GETOBJECTII() {
	warnUnimpl("Test test_internal_WM_GETOBJECTII not written");
}

public void test_internal_dispose_Accessible() {
	warnUnimpl("Test test_internal_dispose_Accessible not written");
}

public void test_internal_new_AccessibleLorg_eclipse_swt_widgets_Control() {
	warnUnimpl("Test test_internal_new_AccessibleLorg_eclipse_swt_widgets_Control not written");
}

public void test_removeAccessibleControlListenerLorg_eclipse_swt_accessibility_AccessibleControlListener() {
	warnUnimpl("Test test_removeAccessibleControlListenerLorg_eclipse_swt_accessibility_AccessibleControlListener not written");
}

public void test_removeAccessibleListenerLorg_eclipse_swt_accessibility_AccessibleListener() {
	warnUnimpl("Test test_removeAccessibleListenerLorg_eclipse_swt_accessibility_AccessibleListener not written");
}

public void test_setFocusI() {
	warnUnimpl("Test test_setFocusI not written");
}


public static Test suite() {
	TestSuite suite = new TestSuite();
	java.util.Vector methodNames = methodNames();
	java.util.Enumeration e = methodNames.elements();
	while (e.hasMoreElements()) {
		suite.addTest(new Test_org_eclipse_swt_accessibility_Accessible((String)e.nextElement()));
	}
	return suite;
}

public static java.util.Vector methodNames() {
	java.util.Vector methodNames = new java.util.Vector();
	methodNames.addElement("test_addAccessibleControlListenerLorg_eclipse_swt_accessibility_AccessibleControlListener");
	methodNames.addElement("test_addAccessibleListenerLorg_eclipse_swt_accessibility_AccessibleListener");
	methodNames.addElement("test_internal_WM_GETOBJECTII");
	methodNames.addElement("test_internal_dispose_Accessible");
	methodNames.addElement("test_internal_new_AccessibleLorg_eclipse_swt_widgets_Control");
	methodNames.addElement("test_removeAccessibleControlListenerLorg_eclipse_swt_accessibility_AccessibleControlListener");
	methodNames.addElement("test_removeAccessibleListenerLorg_eclipse_swt_accessibility_AccessibleListener");
	methodNames.addElement("test_setFocusI");
	return methodNames;
}

protected void runTest() throws Throwable {
	if (getName().equals("test_addAccessibleControlListenerLorg_eclipse_swt_accessibility_AccessibleControlListener")) test_addAccessibleControlListenerLorg_eclipse_swt_accessibility_AccessibleControlListener();
	else if (getName().equals("test_addAccessibleListenerLorg_eclipse_swt_accessibility_AccessibleListener")) test_addAccessibleListenerLorg_eclipse_swt_accessibility_AccessibleListener();
	else if (getName().equals("test_internal_WM_GETOBJECTII")) test_internal_WM_GETOBJECTII();
	else if (getName().equals("test_internal_dispose_Accessible")) test_internal_dispose_Accessible();
	else if (getName().equals("test_internal_new_AccessibleLorg_eclipse_swt_widgets_Control")) test_internal_new_AccessibleLorg_eclipse_swt_widgets_Control();
	else if (getName().equals("test_removeAccessibleControlListenerLorg_eclipse_swt_accessibility_AccessibleControlListener")) test_removeAccessibleControlListenerLorg_eclipse_swt_accessibility_AccessibleControlListener();
	else if (getName().equals("test_removeAccessibleListenerLorg_eclipse_swt_accessibility_AccessibleListener")) test_removeAccessibleListenerLorg_eclipse_swt_accessibility_AccessibleListener();
	else if (getName().equals("test_setFocusI")) test_setFocusI();
}
}
