package org.eclipse.swt.tests.junit;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import org.eclipse.swt.dnd.*;
import junit.framework.*;
import junit.textui.*;

/**
 * Automated Test Suite for class org.eclipse.swt.dnd.DND
 *
 * @see org.eclipse.swt.dnd.DND
 */
public class Test_org_eclipse_swt_dnd_DND extends SwtTestCase {

public Test_org_eclipse_swt_dnd_DND(String name) {
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

public void test_errorI() {
	warnUnimpl("Test test_errorI not written");
}

public void test_errorII() {
	warnUnimpl("Test test_errorII not written");
}

public static Test suite() {
	TestSuite suite = new TestSuite();
	java.util.Vector methodNames = methodNames();
	java.util.Enumeration e = methodNames.elements();
	while (e.hasMoreElements()) {
		suite.addTest(new Test_org_eclipse_swt_dnd_DND((String)e.nextElement()));
	}
	return suite;
}
public static java.util.Vector methodNames() {
	java.util.Vector methodNames = new java.util.Vector();
	methodNames.addElement("test_Constructor");
	methodNames.addElement("test_errorI");
	methodNames.addElement("test_errorII");
	return methodNames;
}
protected void runTest() throws Throwable {
	if (getName().equals("test_Constructor")) test_Constructor();
	else if (getName().equals("test_errorI")) test_errorI();
	else if (getName().equals("test_errorII")) test_errorII();
}
}
