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
 * Automated Test Suite for class org.eclipse.swt.ole.win32.OleAutomation
 *
 * @see org.eclipse.swt.ole.win32.OleAutomation
 */
public class Test_org_eclipse_swt_ole_win32_OleAutomation extends SwtTestCase {

public Test_org_eclipse_swt_ole_win32_OleAutomation(String name) {
	super(name);
}

public static void main(String[] args) {
	TestRunner.run(suite());
}

public void test_ConstructorLorg_eclipse_swt_ole_win32_OleClientSite() {
	warnUnimpl("Test test_ConstructorLorg_eclipse_swt_ole_win32_OleClientSite not written");
}

public void test_dispose() {
	warnUnimpl("Test test_dispose not written");
}

public void test_getDocumentationI() {
	warnUnimpl("Test test_getDocumentationI not written");
}

public void test_getFunctionDescriptionI() {
	warnUnimpl("Test test_getFunctionDescriptionI not written");
}

public void test_getHelpFileI() {
	warnUnimpl("Test test_getHelpFileI not written");
}

public void test_getIDsOfNames$Ljava_lang_String() {
	warnUnimpl("Test test_getIDsOfNames$Ljava_lang_String not written");
}

public void test_getLastError() {
	warnUnimpl("Test test_getLastError not written");
}

public void test_getNameI() {
	warnUnimpl("Test test_getNameI not written");
}

public void test_getNamesII() {
	warnUnimpl("Test test_getNamesII not written");
}

public void test_getPropertyDescriptionI() {
	warnUnimpl("Test test_getPropertyDescriptionI not written");
}

public void test_getPropertyI() {
	warnUnimpl("Test test_getPropertyI not written");
}

public void test_getPropertyI$Lorg_eclipse_swt_ole_win32_Variant() {
	warnUnimpl("Test test_getPropertyI$Lorg_eclipse_swt_ole_win32_Variant not written");
}

public void test_getPropertyI$Lorg_eclipse_swt_ole_win32_Variant$I() {
	warnUnimpl("Test test_getPropertyI$Lorg_eclipse_swt_ole_win32_Variant$I not written");
}

public void test_getTypeInfoAttributes() {
	warnUnimpl("Test test_getTypeInfoAttributes not written");
}

public void test_invokeI() {
	warnUnimpl("Test test_invokeI not written");
}

public void test_invokeI$Lorg_eclipse_swt_ole_win32_Variant() {
	warnUnimpl("Test test_invokeI$Lorg_eclipse_swt_ole_win32_Variant not written");
}

public void test_invokeI$Lorg_eclipse_swt_ole_win32_Variant$I() {
	warnUnimpl("Test test_invokeI$Lorg_eclipse_swt_ole_win32_Variant$I not written");
}

public void test_invokeNoReplyI() {
	warnUnimpl("Test test_invokeNoReplyI not written");
}

public void test_invokeNoReplyI$Lorg_eclipse_swt_ole_win32_Variant() {
	warnUnimpl("Test test_invokeNoReplyI$Lorg_eclipse_swt_ole_win32_Variant not written");
}

public void test_invokeNoReplyI$Lorg_eclipse_swt_ole_win32_Variant$I() {
	warnUnimpl("Test test_invokeNoReplyI$Lorg_eclipse_swt_ole_win32_Variant$I not written");
}

public void test_setPropertyI$Lorg_eclipse_swt_ole_win32_Variant() {
	warnUnimpl("Test test_setPropertyI$Lorg_eclipse_swt_ole_win32_Variant not written");
}

public void test_setPropertyILorg_eclipse_swt_ole_win32_Variant() {
	warnUnimpl("Test test_setPropertyILorg_eclipse_swt_ole_win32_Variant not written");
}


public static Test suite() {
	TestSuite suite = new TestSuite();
	java.util.Vector methodNames = methodNames();
	java.util.Enumeration e = methodNames.elements();
	while (e.hasMoreElements()) {
		suite.addTest(new Test_org_eclipse_swt_ole_win32_OleAutomation((String)e.nextElement()));
	}
	return suite;
}

public static java.util.Vector methodNames() {
	java.util.Vector methodNames = new java.util.Vector();
	methodNames.addElement("test_ConstructorLorg_eclipse_swt_ole_win32_OleClientSite");
	methodNames.addElement("test_dispose");
	methodNames.addElement("test_getDocumentationI");
	methodNames.addElement("test_getFunctionDescriptionI");
	methodNames.addElement("test_getHelpFileI");
	methodNames.addElement("test_getIDsOfNames$Ljava_lang_String");
	methodNames.addElement("test_getLastError");
	methodNames.addElement("test_getNameI");
	methodNames.addElement("test_getNamesII");
	methodNames.addElement("test_getPropertyDescriptionI");
	methodNames.addElement("test_getPropertyI");
	methodNames.addElement("test_getPropertyI$Lorg_eclipse_swt_ole_win32_Variant");
	methodNames.addElement("test_getPropertyI$Lorg_eclipse_swt_ole_win32_Variant$I");
	methodNames.addElement("test_getTypeInfoAttributes");
	methodNames.addElement("test_invokeI");
	methodNames.addElement("test_invokeI$Lorg_eclipse_swt_ole_win32_Variant");
	methodNames.addElement("test_invokeI$Lorg_eclipse_swt_ole_win32_Variant$I");
	methodNames.addElement("test_invokeNoReplyI");
	methodNames.addElement("test_invokeNoReplyI$Lorg_eclipse_swt_ole_win32_Variant");
	methodNames.addElement("test_invokeNoReplyI$Lorg_eclipse_swt_ole_win32_Variant$I");
	methodNames.addElement("test_setPropertyI$Lorg_eclipse_swt_ole_win32_Variant");
	methodNames.addElement("test_setPropertyILorg_eclipse_swt_ole_win32_Variant");
	return methodNames;
}

protected void runTest() throws Throwable {
	if (getName().equals("test_ConstructorLorg_eclipse_swt_ole_win32_OleClientSite")) test_ConstructorLorg_eclipse_swt_ole_win32_OleClientSite();
	else if (getName().equals("test_dispose")) test_dispose();
	else if (getName().equals("test_getDocumentationI")) test_getDocumentationI();
	else if (getName().equals("test_getFunctionDescriptionI")) test_getFunctionDescriptionI();
	else if (getName().equals("test_getHelpFileI")) test_getHelpFileI();
	else if (getName().equals("test_getIDsOfNames$Ljava_lang_String")) test_getIDsOfNames$Ljava_lang_String();
	else if (getName().equals("test_getLastError")) test_getLastError();
	else if (getName().equals("test_getNameI")) test_getNameI();
	else if (getName().equals("test_getNamesII")) test_getNamesII();
	else if (getName().equals("test_getPropertyDescriptionI")) test_getPropertyDescriptionI();
	else if (getName().equals("test_getPropertyI")) test_getPropertyI();
	else if (getName().equals("test_getPropertyI$Lorg_eclipse_swt_ole_win32_Variant")) test_getPropertyI$Lorg_eclipse_swt_ole_win32_Variant();
	else if (getName().equals("test_getPropertyI$Lorg_eclipse_swt_ole_win32_Variant$I")) test_getPropertyI$Lorg_eclipse_swt_ole_win32_Variant$I();
	else if (getName().equals("test_getTypeInfoAttributes")) test_getTypeInfoAttributes();
	else if (getName().equals("test_invokeI")) test_invokeI();
	else if (getName().equals("test_invokeI$Lorg_eclipse_swt_ole_win32_Variant")) test_invokeI$Lorg_eclipse_swt_ole_win32_Variant();
	else if (getName().equals("test_invokeI$Lorg_eclipse_swt_ole_win32_Variant$I")) test_invokeI$Lorg_eclipse_swt_ole_win32_Variant$I();
	else if (getName().equals("test_invokeNoReplyI")) test_invokeNoReplyI();
	else if (getName().equals("test_invokeNoReplyI$Lorg_eclipse_swt_ole_win32_Variant")) test_invokeNoReplyI$Lorg_eclipse_swt_ole_win32_Variant();
	else if (getName().equals("test_invokeNoReplyI$Lorg_eclipse_swt_ole_win32_Variant$I")) test_invokeNoReplyI$Lorg_eclipse_swt_ole_win32_Variant$I();
	else if (getName().equals("test_setPropertyI$Lorg_eclipse_swt_ole_win32_Variant")) test_setPropertyI$Lorg_eclipse_swt_ole_win32_Variant();
	else if (getName().equals("test_setPropertyILorg_eclipse_swt_ole_win32_Variant")) test_setPropertyILorg_eclipse_swt_ole_win32_Variant();
}
}
