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
 * Automated Test Suite for class org.eclipse.swt.dnd.RTFTransfer
 *
 * @see org.eclipse.swt.dnd.RTFTransfer
 */
public class Test_org_eclipse_swt_dnd_RTFTransfer extends Test_org_eclipse_swt_dnd_ByteArrayTransfer {

public Test_org_eclipse_swt_dnd_RTFTransfer(String name) {
	super(name);
}

public static void main(String[] args) {
	TestRunner.run(suite());
}

public void test_getInstance() {
	warnUnimpl("Test test_getInstance not written");
}

public void test_javaToNativeLjava_lang_ObjectLorg_eclipse_swt_dnd_TransferData() {
	warnUnimpl("Test test_javaToNativeLjava_lang_ObjectLorg_eclipse_swt_dnd_TransferData not written");
}

public void test_nativeToJavaLorg_eclipse_swt_dnd_TransferData() {
	warnUnimpl("Test test_nativeToJavaLorg_eclipse_swt_dnd_TransferData not written");
}

public static Test suite() {
	TestSuite suite = new TestSuite();
	java.util.Vector methodNames = methodNames();
	java.util.Enumeration e = methodNames.elements();
	while (e.hasMoreElements()) {
		suite.addTest(new Test_org_eclipse_swt_dnd_RTFTransfer((String)e.nextElement()));
	}
	return suite;
}
public static java.util.Vector methodNames() {
	java.util.Vector methodNames = new java.util.Vector();
	methodNames.addElement("test_getInstance");
	methodNames.addElement("test_javaToNativeLjava_lang_ObjectLorg_eclipse_swt_dnd_TransferData");
	methodNames.addElement("test_nativeToJavaLorg_eclipse_swt_dnd_TransferData");
	methodNames.addAll(Test_org_eclipse_swt_dnd_ByteArrayTransfer.methodNames()); // add superclass method names
	return methodNames;
}

protected void runTest() throws Throwable {
	if (getName().equals("test_getInstance")) test_getInstance();
	else if (getName().equals("test_javaToNativeLjava_lang_ObjectLorg_eclipse_swt_dnd_TransferData")) test_javaToNativeLjava_lang_ObjectLorg_eclipse_swt_dnd_TransferData();
	else if (getName().equals("test_nativeToJavaLorg_eclipse_swt_dnd_TransferData")) test_nativeToJavaLorg_eclipse_swt_dnd_TransferData();
	else super.runTest();
}
}
