package org.eclipse.swt.tests.junit;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import org.eclipse.swt.custom.*;
import junit.framework.*;
import junit.textui.*;

/**
 * Automated Test Suite for class org.eclipse.swt.custom.TextChangedEvent
 *
 * @see org.eclipse.swt.custom.TextChangedEvent
 */
public class Test_org_eclipse_swt_custom_TextChangedEvent extends Test_org_eclipse_swt_events_TypedEvent {

public Test_org_eclipse_swt_custom_TextChangedEvent(String name) {
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

public void test_ConstructorLorg_eclipse_swt_custom_StyledTextContent() {
	// this method tested by other test classes
}

public static Test suite() {
	TestSuite suite = new TestSuite();
	java.util.Vector methodNames = methodNames();
	java.util.Enumeration e = methodNames.elements();
	while (e.hasMoreElements()) {
		suite.addTest(new Test_org_eclipse_swt_custom_TextChangedEvent((String)e.nextElement()));
	}
	return suite;
}
public static java.util.Vector methodNames() {
	java.util.Vector methodNames = new java.util.Vector();
	methodNames.addElement("test_ConstructorLorg_eclipse_swt_custom_StyledTextContent");
	methodNames.addAll(Test_org_eclipse_swt_events_TypedEvent.methodNames()); // add superclass method names
	return methodNames;
}
protected void runTest() throws Throwable {
	if (getName().equals("test_ConstructorLorg_eclipse_swt_custom_StyledTextContent")) test_ConstructorLorg_eclipse_swt_custom_StyledTextContent();
	else super.runTest();
}
}
