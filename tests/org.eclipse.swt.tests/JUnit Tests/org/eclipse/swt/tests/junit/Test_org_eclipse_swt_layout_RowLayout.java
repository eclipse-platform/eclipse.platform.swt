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
 * Automated Test Suite for class org.eclipse.swt.layout.RowLayout
 *
 * @see org.eclipse.swt.layout.RowLayout
 */
public class Test_org_eclipse_swt_layout_RowLayout extends Test_org_eclipse_swt_widgets_Layout {

public Test_org_eclipse_swt_layout_RowLayout(String name) {
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

public void test_Constructor() {
	warnUnimpl("Test test_Constructor not written");
}

public void test_computeSizeLorg_eclipse_swt_widgets_CompositeIIZ() {
	warnUnimpl("Test test_computeSizeLorg_eclipse_swt_widgets_CompositeIIZ not written");
}

public void test_layoutLorg_eclipse_swt_widgets_CompositeZ() {
	warnUnimpl("Test test_layoutLorg_eclipse_swt_widgets_CompositeZ not written");
}

public static Test suite() {
	TestSuite suite = new TestSuite();
	java.util.Vector methodNames = methodNames();
	java.util.Enumeration e = methodNames.elements();
	while (e.hasMoreElements()) {
		suite.addTest(new Test_org_eclipse_swt_layout_RowLayout((String)e.nextElement()));
	}
	return suite;
}
public static java.util.Vector methodNames() {
	java.util.Vector methodNames = new java.util.Vector();
	methodNames.addElement("test_Constructor");
	methodNames.addElement("test_computeSizeLorg_eclipse_swt_widgets_CompositeIIZ");
	methodNames.addElement("test_layoutLorg_eclipse_swt_widgets_CompositeZ");
	methodNames.addAll(Test_org_eclipse_swt_widgets_Layout.methodNames()); // add superclass method names
	return methodNames;
}
protected void runTest() throws Throwable {
	if (getName().equals("test_Constructor")) test_Constructor();
	else if (getName().equals("test_computeSizeLorg_eclipse_swt_widgets_CompositeIIZ")) test_computeSizeLorg_eclipse_swt_widgets_CompositeIIZ();
	else if (getName().equals("test_layoutLorg_eclipse_swt_widgets_CompositeZ")) test_layoutLorg_eclipse_swt_widgets_CompositeZ();
	else super.runTest();
}
}
