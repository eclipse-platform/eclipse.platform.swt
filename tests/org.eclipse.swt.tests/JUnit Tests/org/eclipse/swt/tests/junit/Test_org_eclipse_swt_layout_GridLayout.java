package org.eclipse.swt.tests.junit;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import org.eclipse.swt.layout.*;
import junit.framework.*;
import junit.textui.*;

/**
 * Automated Test Suite for class org.eclipse.swt.layout.GridLayout
 *
 * @see org.eclipse.swt.layout.GridLayout
 */
public class Test_org_eclipse_swt_layout_GridLayout extends Test_org_eclipse_swt_widgets_Layout {

public Test_org_eclipse_swt_layout_GridLayout(String name) {
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
		suite.addTest(new Test_org_eclipse_swt_layout_GridLayout((String)e.nextElement()));
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
