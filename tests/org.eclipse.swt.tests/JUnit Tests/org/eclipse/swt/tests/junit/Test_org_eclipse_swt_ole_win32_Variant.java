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
 * Automated Test Suite for class org.eclipse.swt.ole.win32.Variant
 *
 * @see org.eclipse.swt.ole.win32.Variant
 */
public class Test_org_eclipse_swt_ole_win32_Variant extends SwtTestCase {

public Test_org_eclipse_swt_ole_win32_Variant(String name) {
	super(name);
}

public static void main(String[] args) {
	TestRunner.run(suite());
}

public void test_Constructor() {
	warnUnimpl("Test test_Constructor not written");
}

public void test_ConstructorF() {
	warnUnimpl("Test test_ConstructorF not written");
}

public void test_ConstructorI() {
	warnUnimpl("Test test_ConstructorI not written");
}

public void test_ConstructorIS() {
	warnUnimpl("Test test_ConstructorIS not written");
}

public void test_ConstructorLjava_lang_String() {
	warnUnimpl("Test test_ConstructorLjava_lang_String not written");
}

public void test_ConstructorLorg_eclipse_swt_internal_ole_win32_IDispatch() {
	warnUnimpl("Test test_ConstructorLorg_eclipse_swt_internal_ole_win32_IDispatch not written");
}

public void test_ConstructorLorg_eclipse_swt_internal_ole_win32_IUnknown() {
	warnUnimpl("Test test_ConstructorLorg_eclipse_swt_internal_ole_win32_IUnknown not written");
}

public void test_ConstructorLorg_eclipse_swt_ole_win32_OleAutomation() {
	warnUnimpl("Test test_ConstructorLorg_eclipse_swt_ole_win32_OleAutomation not written");
}

public void test_ConstructorS() {
	warnUnimpl("Test test_ConstructorS not written");
}

public void test_ConstructorZ() {
	warnUnimpl("Test test_ConstructorZ not written");
}

public void test_dispose() {
	warnUnimpl("Test test_dispose not written");
}

public void test_getAutomation() {
	warnUnimpl("Test test_getAutomation not written");
}

public void test_getBoolean() {
	warnUnimpl("Test test_getBoolean not written");
}

public void test_getByRef() {
	warnUnimpl("Test test_getByRef not written");
}

public void test_getDispatch() {
	warnUnimpl("Test test_getDispatch not written");
}

public void test_getFloat() {
	warnUnimpl("Test test_getFloat not written");
}

public void test_getInt() {
	warnUnimpl("Test test_getInt not written");
}

public void test_getShort() {
	warnUnimpl("Test test_getShort not written");
}

public void test_getString() {
	warnUnimpl("Test test_getString not written");
}

public void test_getType() {
	warnUnimpl("Test test_getType not written");
}

public void test_getUnknown() {
	warnUnimpl("Test test_getUnknown not written");
}

public void test_setByRefF() {
	warnUnimpl("Test test_setByRefF not written");
}

public void test_setByRefI() {
	warnUnimpl("Test test_setByRefI not written");
}

public void test_setByRefS() {
	warnUnimpl("Test test_setByRefS not written");
}

public void test_setByRefZ() {
	warnUnimpl("Test test_setByRefZ not written");
}


public static Test suite() {
	TestSuite suite = new TestSuite();
	java.util.Vector methodNames = methodNames();
	java.util.Enumeration e = methodNames.elements();
	while (e.hasMoreElements()) {
		suite.addTest(new Test_org_eclipse_swt_ole_win32_Variant((String)e.nextElement()));
	}
	return suite;
}

public static java.util.Vector methodNames() {
	java.util.Vector methodNames = new java.util.Vector();
	methodNames.addElement("test_Constructor");
	methodNames.addElement("test_ConstructorF");
	methodNames.addElement("test_ConstructorI");
	methodNames.addElement("test_ConstructorIS");
	methodNames.addElement("test_ConstructorLjava_lang_String");
	methodNames.addElement("test_ConstructorLorg_eclipse_swt_internal_ole_win32_IDispatch");
	methodNames.addElement("test_ConstructorLorg_eclipse_swt_internal_ole_win32_IUnknown");
	methodNames.addElement("test_ConstructorLorg_eclipse_swt_ole_win32_OleAutomation");
	methodNames.addElement("test_ConstructorS");
	methodNames.addElement("test_ConstructorZ");
	methodNames.addElement("test_dispose");
	methodNames.addElement("test_getAutomation");
	methodNames.addElement("test_getBoolean");
	methodNames.addElement("test_getByRef");
	methodNames.addElement("test_getDispatch");
	methodNames.addElement("test_getFloat");
	methodNames.addElement("test_getInt");
	methodNames.addElement("test_getShort");
	methodNames.addElement("test_getString");
	methodNames.addElement("test_getType");
	methodNames.addElement("test_getUnknown");
	methodNames.addElement("test_setByRefF");
	methodNames.addElement("test_setByRefI");
	methodNames.addElement("test_setByRefS");
	methodNames.addElement("test_setByRefZ");
	return methodNames;
}

protected void runTest() throws Throwable {
	if (getName().equals("test_Constructor")) test_Constructor();
	else if (getName().equals("test_ConstructorF")) test_ConstructorF();
	else if (getName().equals("test_ConstructorI")) test_ConstructorI();
	else if (getName().equals("test_ConstructorIS")) test_ConstructorIS();
	else if (getName().equals("test_ConstructorLjava_lang_String")) test_ConstructorLjava_lang_String();
	else if (getName().equals("test_ConstructorLorg_eclipse_swt_internal_ole_win32_IDispatch")) test_ConstructorLorg_eclipse_swt_internal_ole_win32_IDispatch();
	else if (getName().equals("test_ConstructorLorg_eclipse_swt_internal_ole_win32_IUnknown")) test_ConstructorLorg_eclipse_swt_internal_ole_win32_IUnknown();
	else if (getName().equals("test_ConstructorLorg_eclipse_swt_ole_win32_OleAutomation")) test_ConstructorLorg_eclipse_swt_ole_win32_OleAutomation();
	else if (getName().equals("test_ConstructorS")) test_ConstructorS();
	else if (getName().equals("test_ConstructorZ")) test_ConstructorZ();
	else if (getName().equals("test_dispose")) test_dispose();
	else if (getName().equals("test_getAutomation")) test_getAutomation();
	else if (getName().equals("test_getBoolean")) test_getBoolean();
	else if (getName().equals("test_getByRef")) test_getByRef();
	else if (getName().equals("test_getDispatch")) test_getDispatch();
	else if (getName().equals("test_getFloat")) test_getFloat();
	else if (getName().equals("test_getInt")) test_getInt();
	else if (getName().equals("test_getShort")) test_getShort();
	else if (getName().equals("test_getString")) test_getString();
	else if (getName().equals("test_getType")) test_getType();
	else if (getName().equals("test_getUnknown")) test_getUnknown();
	else if (getName().equals("test_setByRefF")) test_setByRefF();
	else if (getName().equals("test_setByRefI")) test_setByRefI();
	else if (getName().equals("test_setByRefS")) test_setByRefS();
	else if (getName().equals("test_setByRefZ")) test_setByRefZ();
}
}
