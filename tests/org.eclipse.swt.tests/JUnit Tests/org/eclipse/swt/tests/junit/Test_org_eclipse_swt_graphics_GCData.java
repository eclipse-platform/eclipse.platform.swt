package org.eclipse.swt.tests.junit;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import org.eclipse.swt.graphics.*;
import junit.framework.*;
import junit.textui.*;

/**
 * Automated Test Suite for class org.eclipse.swt.graphics.GCData
 *
 * @see org.eclipse.swt.graphics.GCData
 */
public class Test_org_eclipse_swt_graphics_GCData extends SwtTestCase {

public Test_org_eclipse_swt_graphics_GCData(String name) {
	super(name);
}

public static void main(String[] args) {
	TestRunner.run(suite());
}

protected void setUp() {
}

protected void tearDown() {
}

public void test_Constructor() {
	warnUnimpl("Test test_Constructor not written");
}

public static Test suite() {
	TestSuite suite = new TestSuite();
	java.util.Vector methodNames = methodNames();
	java.util.Enumeration e = methodNames.elements();
	while (e.hasMoreElements()) {
		suite.addTest(new Test_org_eclipse_swt_graphics_GCData((String)e.nextElement()));
	}
	return suite;
}
public static java.util.Vector methodNames() {
	java.util.Vector methodNames = new java.util.Vector();
	methodNames.addElement("test_Constructor");
	return methodNames;
}
protected void runTest() throws Throwable {
	if (getName().equals("test_Constructor")) test_Constructor();
}
}
