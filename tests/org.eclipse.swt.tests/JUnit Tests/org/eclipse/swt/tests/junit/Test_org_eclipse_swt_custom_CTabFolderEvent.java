package org.eclipse.swt.tests.junit;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import org.eclipse.swt.custom.*;
import junit.framework.*;
import junit.textui.*;

/**
 * Automated Test Suite for class org.eclipse.swt.custom.CTabFolderEvent
 *
 * @see org.eclipse.swt.custom.CTabFolderEvent
 */
public class Test_org_eclipse_swt_custom_CTabFolderEvent extends Test_org_eclipse_swt_events_TypedEvent {

public Test_org_eclipse_swt_custom_CTabFolderEvent(String name) {
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

public static Test suite() {
	TestSuite suite = new TestSuite();
	java.util.Vector methodNames = methodNames();
	java.util.Enumeration e = methodNames.elements();
	while (e.hasMoreElements()) {
		suite.addTest(new Test_org_eclipse_swt_custom_CTabFolderEvent((String)e.nextElement()));
	}
	return suite;
}
public static java.util.Vector methodNames() {
	java.util.Vector methodNames = new java.util.Vector();
	methodNames.addAll(Test_org_eclipse_swt_events_TypedEvent.methodNames()); // add superclass method names
	return methodNames;
}
protected void runTest() throws Throwable {
	super.runTest();
}
}
