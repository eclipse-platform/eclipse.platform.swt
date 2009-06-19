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
 * Automated Test Suite for class org.eclipse.swt.ole.win32.OleFrame
 *
 * @see org.eclipse.swt.ole.win32.OleFrame
 */
public class Test_org_eclipse_swt_ole_win32_OleFrame extends Test_org_eclipse_swt_widgets_Composite {

public Test_org_eclipse_swt_ole_win32_OleFrame(String name) {
	super(name);
}

public static void main(String[] args) {
	TestRunner.run(suite());
}

public void test_ConstructorLorg_eclipse_swt_widgets_CompositeI() {
	warnUnimpl("Test test_ConstructorLorg_eclipse_swt_widgets_CompositeI not written");
}

public void test_getContainerMenus() {
	warnUnimpl("Test test_getContainerMenus not written");
}

public void test_getFileMenus() {
	warnUnimpl("Test test_getFileMenus not written");
}

public void test_getWindowMenus() {
	warnUnimpl("Test test_getWindowMenus not written");
}

public void test_setContainerMenus$Lorg_eclipse_swt_widgets_MenuItem() {
	warnUnimpl("Test test_setContainerMenus$Lorg_eclipse_swt_widgets_MenuItem not written");
}

public void test_setFileMenus$Lorg_eclipse_swt_widgets_MenuItem() {
	warnUnimpl("Test test_setFileMenus$Lorg_eclipse_swt_widgets_MenuItem not written");
}

public void test_setWindowMenus$Lorg_eclipse_swt_widgets_MenuItem() {
	warnUnimpl("Test test_setWindowMenus$Lorg_eclipse_swt_widgets_MenuItem not written");
}


public static Test suite() {
	TestSuite suite = new TestSuite();
	java.util.Vector methodNames = methodNames();
	java.util.Enumeration e = methodNames.elements();
	while (e.hasMoreElements()) {
		suite.addTest(new Test_org_eclipse_swt_ole_win32_OleFrame((String)e.nextElement()));
	}
	return suite;
}

public static java.util.Vector methodNames() {
	java.util.Vector methodNames = new java.util.Vector();
	methodNames.addElement("test_ConstructorLorg_eclipse_swt_widgets_CompositeI");
	methodNames.addElement("test_getContainerMenus");
	methodNames.addElement("test_getFileMenus");
	methodNames.addElement("test_getWindowMenus");
	methodNames.addElement("test_setContainerMenus$Lorg_eclipse_swt_widgets_MenuItem");
	methodNames.addElement("test_setFileMenus$Lorg_eclipse_swt_widgets_MenuItem");
	methodNames.addElement("test_setWindowMenus$Lorg_eclipse_swt_widgets_MenuItem");
	methodNames.addAll(Test_org_eclipse_swt_widgets_Composite.methodNames()); // add superclass method names
	return methodNames;
}

protected void runTest() throws Throwable {
	if (getName().equals("test_ConstructorLorg_eclipse_swt_widgets_CompositeI")) test_ConstructorLorg_eclipse_swt_widgets_CompositeI();
	else if (getName().equals("test_getContainerMenus")) test_getContainerMenus();
	else if (getName().equals("test_getFileMenus")) test_getFileMenus();
	else if (getName().equals("test_getWindowMenus")) test_getWindowMenus();
	else if (getName().equals("test_setContainerMenus$Lorg_eclipse_swt_widgets_MenuItem")) test_setContainerMenus$Lorg_eclipse_swt_widgets_MenuItem();
	else if (getName().equals("test_setFileMenus$Lorg_eclipse_swt_widgets_MenuItem")) test_setFileMenus$Lorg_eclipse_swt_widgets_MenuItem();
	else if (getName().equals("test_setWindowMenus$Lorg_eclipse_swt_widgets_MenuItem")) test_setWindowMenus$Lorg_eclipse_swt_widgets_MenuItem();
	else super.runTest();
}
}
