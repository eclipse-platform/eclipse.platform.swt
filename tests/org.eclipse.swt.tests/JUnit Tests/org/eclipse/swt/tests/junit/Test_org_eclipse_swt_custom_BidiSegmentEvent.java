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


import java.util.Enumeration;
import java.util.Vector;

import junit.framework.*;
import junit.textui.*;

/**
 * Automated Test Suite for class org.eclipse.swt.custom.BidiSegmentEvent
 *
 * @see org.eclipse.swt.custom.BidiSegmentEvent
 */
public class Test_org_eclipse_swt_custom_BidiSegmentEvent extends Test_org_eclipse_swt_events_TypedEvent {

public Test_org_eclipse_swt_custom_BidiSegmentEvent(String name) {
	super(name);
}

public static void main(String[] args) {
	TestRunner.run(suite());
}

public static Test suite() {
	TestSuite suite = new TestSuite();
	Vector<String> methodNames = methodNames();
	Enumeration<String> e = methodNames.elements();
	while (e.hasMoreElements()) {
		suite.addTest(new Test_org_eclipse_swt_custom_BidiSegmentEvent(e.nextElement()));
	}
	return suite;
}
public static java.util.Vector<String> methodNames() {
	java.util.Vector<String> methodNames = new java.util.Vector<String>();
	methodNames.addAll(Test_org_eclipse_swt_events_TypedEvent.methodNames()); // add superclass method names
	return methodNames;
}
@Override
protected void runTest() throws Throwable {
	super.runTest();
}
}
