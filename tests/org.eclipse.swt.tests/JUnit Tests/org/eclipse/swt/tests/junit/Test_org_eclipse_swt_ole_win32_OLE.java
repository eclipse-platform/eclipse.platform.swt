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
 * Automated Test Suite for class org.eclipse.swt.ole.win32.OLE
 *
 * @see org.eclipse.swt.ole.win32.OLE
 */
public class Test_org_eclipse_swt_ole_win32_OLE extends Test_org_eclipse_swt_SWT {

public Test_org_eclipse_swt_ole_win32_OLE(String name) {
	super(name);
}

public static void main(String[] args) {
	TestRunner.run(suite());
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

public void test_findProgramIDLjava_lang_String() {
	warnUnimpl("Test test_findProgramIDLjava_lang_String not written");
}

public void test_isOleFileLjava_io_File() {
	warnUnimpl("Test test_isOleFileLjava_io_File not written");
}


public static Test suite() {
	TestSuite suite = new TestSuite();
	java.util.Vector methodNames = methodNames();
	java.util.Enumeration e = methodNames.elements();
	while (e.hasMoreElements()) {
		suite.addTest(new Test_org_eclipse_swt_ole_win32_OLE((String)e.nextElement()));
	}
	return suite;
}

public static java.util.Vector methodNames() {
	java.util.Vector methodNames = new java.util.Vector();
	methodNames.addElement("test_Constructor");
	methodNames.addElement("test_errorI");
	methodNames.addElement("test_errorII");
	methodNames.addElement("test_findProgramIDLjava_lang_String");
	methodNames.addElement("test_isOleFileLjava_io_File");
	methodNames.addAll(Test_org_eclipse_swt_SWT.methodNames()); // add superclass method names
	return methodNames;
}

protected void runTest() throws Throwable {
	if (getName().equals("test_Constructor")) test_Constructor();
	else if (getName().equals("test_errorI")) test_errorI();
	else if (getName().equals("test_errorII")) test_errorII();
	else if (getName().equals("test_findProgramIDLjava_lang_String")) test_findProgramIDLjava_lang_String();
	else if (getName().equals("test_isOleFileLjava_io_File")) test_isOleFileLjava_io_File();
	else super.runTest();
}
}
