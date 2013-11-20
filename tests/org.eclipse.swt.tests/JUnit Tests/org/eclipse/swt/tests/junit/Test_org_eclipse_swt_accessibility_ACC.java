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
 * Automated Test Suite for class org.eclipse.swt.accessibility.ACC
 *
 * @see org.eclipse.swt.accessibility.ACC
 */
public class Test_org_eclipse_swt_accessibility_ACC extends SwtTestCase {

public Test_org_eclipse_swt_accessibility_ACC(String name) {
	super(name);
}

public static void main(String[] args) {
	TestRunner.run(suite());
}

public void test_Constructor() {
	warnUnimpl("Test test_Constructor not written");
}


public static Test suite() {
	TestSuite suite = new TestSuite();
	Vector<String> methodNames = methodNames();
	Enumeration<String> e = methodNames.elements();
	while (e.hasMoreElements()) {
		suite.addTest(new Test_org_eclipse_swt_accessibility_ACC(e.nextElement()));
	}
	return suite;
}

public static Vector<String> methodNames() {
	Vector<String> methodNames = new Vector<String>();
	methodNames.addElement("test_Constructor");
	return methodNames;
}

@Override
protected void runTest() throws Throwable {
	if (getName().equals("test_Constructor")) test_Constructor();
}
}
