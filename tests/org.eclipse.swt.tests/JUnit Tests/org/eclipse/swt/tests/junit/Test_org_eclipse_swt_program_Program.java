/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tests.junit;


import junit.framework.*;
import junit.textui.*;

/**
 * Automated Test Suite for class org.eclipse.swt.program.Program
 *
 * @see org.eclipse.swt.program.Program
 */
public class Test_org_eclipse_swt_program_Program extends SwtTestCase {

public Test_org_eclipse_swt_program_Program(String name) {
	super(name);
}

public static void main(String[] args) {
	TestRunner.run(suite());
}

protected void setUp() {
}

protected void tearDown() {
}

public void test_findProgramLjava_lang_String() {
	warnUnimpl("Test test_findProgramLjava_lang_String not written");
}

public void test_getExtensions() {
	warnUnimpl("Test test_getExtensions not written");
}

public void test_getPrograms() {
	warnUnimpl("Test test_getPrograms not written");
}

public void test_launchLjava_lang_String() {
	warnUnimpl("Test test_launchLjava_lang_String not written");
}

public void test_executeLjava_lang_String() {
	warnUnimpl("Test test_executeLjava_lang_String not written");
}

public void test_getImageData() {
	warnUnimpl("Test test_getImageData not written");
}

public void test_getName() {
	warnUnimpl("Test test_getName not written");
}

public void test_equalsLjava_lang_Object() {
	warnUnimpl("Test test_equalsLjava_lang_Object not written");
}

public void test_hashCode() {
	warnUnimpl("Test test_hashCode not written");
}

public void test_toString() {
	warnUnimpl("Test test_toString not written");
}

public static Test suite() {
	TestSuite suite = new TestSuite();
	java.util.Vector methodNames = methodNames();
	java.util.Enumeration e = methodNames.elements();
	while (e.hasMoreElements()) {
		suite.addTest(new Test_org_eclipse_swt_program_Program((String)e.nextElement()));
	}
	return suite;
}
public static java.util.Vector methodNames() {
	java.util.Vector methodNames = new java.util.Vector();
	methodNames.addElement("test_findProgramLjava_lang_String");
	methodNames.addElement("test_getExtensions");
	methodNames.addElement("test_getPrograms");
	methodNames.addElement("test_launchLjava_lang_String");
	methodNames.addElement("test_executeLjava_lang_String");
	methodNames.addElement("test_getImageData");
	methodNames.addElement("test_getName");
	methodNames.addElement("test_equalsLjava_lang_Object");
	methodNames.addElement("test_hashCode");
	methodNames.addElement("test_toString");
	return methodNames;
}
protected void runTest() throws Throwable {
	if (getName().equals("test_findProgramLjava_lang_String")) test_findProgramLjava_lang_String();
	else if (getName().equals("test_getExtensions")) test_getExtensions();
	else if (getName().equals("test_getPrograms")) test_getPrograms();
	else if (getName().equals("test_launchLjava_lang_String")) test_launchLjava_lang_String();
	else if (getName().equals("test_executeLjava_lang_String")) test_executeLjava_lang_String();
	else if (getName().equals("test_getImageData")) test_getImageData();
	else if (getName().equals("test_getName")) test_getName();
	else if (getName().equals("test_equalsLjava_lang_Object")) test_equalsLjava_lang_Object();
	else if (getName().equals("test_hashCode")) test_hashCode();
	else if (getName().equals("test_toString")) test_toString();
}
}
