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
 * Automated Test Suite for class org.eclipse.swt.dnd.ByteArrayTransfer
 *
 * @see org.eclipse.swt.dnd.ByteArrayTransfer
 */
public class Test_org_eclipse_swt_dnd_ByteArrayTransfer extends Test_org_eclipse_swt_dnd_Transfer {

public Test_org_eclipse_swt_dnd_ByteArrayTransfer(String name) {
	super(name);
}

public static void main(String[] args) {
	TestRunner.run(suite());
}

public void test_Constructor() {
	warnUnimpl("Test test_Constructor not written");
}

public void test_getSupportedTypes() {
	warnUnimpl("Test test_getSupportedTypes not written");
}

public void test_isSupportedTypeLorg_eclipse_swt_dnd_TransferData() {
	warnUnimpl("Test test_isSupportedTypeLorg_eclipse_swt_dnd_TransferData not written");
}


public static Test suite() {
	TestSuite suite = new TestSuite();
	java.util.Vector methodNames = methodNames();
	java.util.Enumeration e = methodNames.elements();
	while (e.hasMoreElements()) {
		suite.addTest(new Test_org_eclipse_swt_dnd_ByteArrayTransfer((String)e.nextElement()));
	}
	return suite;
}

public static java.util.Vector methodNames() {
	java.util.Vector methodNames = new java.util.Vector();
	methodNames.addElement("test_Constructor");
	methodNames.addElement("test_getSupportedTypes");
	methodNames.addElement("test_isSupportedTypeLorg_eclipse_swt_dnd_TransferData");
	methodNames.addAll(Test_org_eclipse_swt_dnd_Transfer.methodNames()); // add superclass method names
	return methodNames;
}
protected void runTest() throws Throwable {
	if (getName().equals("test_Constructor")) test_Constructor();
	else if (getName().equals("test_getSupportedTypes")) test_getSupportedTypes();
	else if (getName().equals("test_isSupportedTypeLorg_eclipse_swt_dnd_TransferData")) test_isSupportedTypeLorg_eclipse_swt_dnd_TransferData();
	else super.runTest();
}
}
