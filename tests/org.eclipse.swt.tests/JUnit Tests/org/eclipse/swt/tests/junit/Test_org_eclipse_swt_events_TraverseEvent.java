package org.eclipse.swt.tests.junit;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import org.eclipse.swt.events.*;
import junit.framework.*;
import junit.textui.*;

/**
 * Automated Test Suite for class org.eclipse.swt.events.TraverseEvent
 *
 * @see org.eclipse.swt.events.TraverseEvent
 */
public class Test_org_eclipse_swt_events_TraverseEvent extends Test_org_eclipse_swt_events_KeyEvent {

public Test_org_eclipse_swt_events_TraverseEvent(String name) {
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

public void test_ConstructorLorg_eclipse_swt_widgets_Event() {
	warnUnimpl("Test test_ConstructorLorg_eclipse_swt_widgets_Event not written");
}

public static Test suite() {
	TestSuite suite = new TestSuite();
	java.util.Vector methodNames = methodNames();
	java.util.Enumeration e = methodNames.elements();
	while (e.hasMoreElements()) {
		suite.addTest(new Test_org_eclipse_swt_events_TraverseEvent((String)e.nextElement()));
	}
	return suite;
}
public static java.util.Vector methodNames() {
	java.util.Vector methodNames = new java.util.Vector();
	methodNames.addElement("test_ConstructorLorg_eclipse_swt_widgets_Event");
	methodNames.addAll(Test_org_eclipse_swt_events_KeyEvent.methodNames()); // add superclass method names
	return methodNames;
}
protected void runTest() throws Throwable {
	if (getName().equals("test_ConstructorLorg_eclipse_swt_widgets_Event")) test_ConstructorLorg_eclipse_swt_widgets_Event();
	else super.runTest();
}
}
