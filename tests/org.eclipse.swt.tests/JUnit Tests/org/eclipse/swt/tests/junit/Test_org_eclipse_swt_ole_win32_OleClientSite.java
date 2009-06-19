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
 * Automated Test Suite for class org.eclipse.swt.ole.win32.OleClientSite
 *
 * @see org.eclipse.swt.ole.win32.OleClientSite
 */
public class Test_org_eclipse_swt_ole_win32_OleClientSite extends Test_org_eclipse_swt_widgets_Composite {

public Test_org_eclipse_swt_ole_win32_OleClientSite(String name) {
	super(name);
}

public static void main(String[] args) {
	TestRunner.run(suite());
}

public void test_ConstructorLorg_eclipse_swt_widgets_CompositeILjava_io_File() {
	warnUnimpl("Test test_ConstructorLorg_eclipse_swt_widgets_CompositeILjava_io_File not written");
}

public void test_ConstructorLorg_eclipse_swt_widgets_CompositeILjava_lang_String() {
	warnUnimpl("Test test_ConstructorLorg_eclipse_swt_widgets_CompositeILjava_lang_String not written");
}

public void test_ConstructorLorg_eclipse_swt_widgets_CompositeILjava_lang_StringLjava_io_File() {
	warnUnimpl("Test test_ConstructorLorg_eclipse_swt_widgets_CompositeILjava_lang_StringLjava_io_File not written");
}

public void test_deactivateInPlaceClient() {
	warnUnimpl("Test test_deactivateInPlaceClient not written");
}

public void test_doVerbI() {
	warnUnimpl("Test test_doVerbI not written");
}

public void test_execIILorg_eclipse_swt_ole_win32_VariantLorg_eclipse_swt_ole_win32_Variant() {
	warnUnimpl("Test test_execIILorg_eclipse_swt_ole_win32_VariantLorg_eclipse_swt_ole_win32_Variant not written");
}

public void test_getIndent() {
	warnUnimpl("Test test_getIndent not written");
}

public void test_getProgramID() {
	warnUnimpl("Test test_getProgramID not written");
}

public void test_isDirty() {
	warnUnimpl("Test test_isDirty not written");
}

public void test_isFocusControl() {
	warnUnimpl("Test test_isFocusControl not written");
}

public void test_queryStatusI() {
	warnUnimpl("Test test_queryStatusI not written");
}

public void test_saveLjava_io_FileZ() {
	warnUnimpl("Test test_saveLjava_io_FileZ not written");
}

public void test_setIndentLorg_eclipse_swt_graphics_Rectangle() {
	warnUnimpl("Test test_setIndentLorg_eclipse_swt_graphics_Rectangle not written");
}

public void test_showPropertiesLjava_lang_String() {
	warnUnimpl("Test test_showPropertiesLjava_lang_String not written");
}


public static Test suite() {
	TestSuite suite = new TestSuite();
	java.util.Vector methodNames = methodNames();
	java.util.Enumeration e = methodNames.elements();
	while (e.hasMoreElements()) {
		suite.addTest(new Test_org_eclipse_swt_ole_win32_OleClientSite((String)e.nextElement()));
	}
	return suite;
}

public static java.util.Vector methodNames() {
	java.util.Vector methodNames = new java.util.Vector();
	methodNames.addElement("test_ConstructorLorg_eclipse_swt_widgets_CompositeILjava_io_File");
	methodNames.addElement("test_ConstructorLorg_eclipse_swt_widgets_CompositeILjava_lang_String");
	methodNames.addElement("test_ConstructorLorg_eclipse_swt_widgets_CompositeILjava_lang_StringLjava_io_File");
	methodNames.addElement("test_deactivateInPlaceClient");
	methodNames.addElement("test_doVerbI");
	methodNames.addElement("test_execIILorg_eclipse_swt_ole_win32_VariantLorg_eclipse_swt_ole_win32_Variant");
	methodNames.addElement("test_getIndent");
	methodNames.addElement("test_getProgramID");
	methodNames.addElement("test_isDirty");
	methodNames.addElement("test_isFocusControl");
	methodNames.addElement("test_queryStatusI");
	methodNames.addElement("test_saveLjava_io_FileZ");
	methodNames.addElement("test_setIndentLorg_eclipse_swt_graphics_Rectangle");
	methodNames.addElement("test_showPropertiesLjava_lang_String");
	methodNames.addAll(Test_org_eclipse_swt_widgets_Composite.methodNames()); // add superclass method names
	return methodNames;
}

protected void runTest() throws Throwable {
	if (getName().equals("test_ConstructorLorg_eclipse_swt_widgets_CompositeILjava_io_File")) test_ConstructorLorg_eclipse_swt_widgets_CompositeILjava_io_File();
	else if (getName().equals("test_ConstructorLorg_eclipse_swt_widgets_CompositeILjava_lang_String")) test_ConstructorLorg_eclipse_swt_widgets_CompositeILjava_lang_String();
	else if (getName().equals("test_ConstructorLorg_eclipse_swt_widgets_CompositeILjava_lang_StringLjava_io_File")) test_ConstructorLorg_eclipse_swt_widgets_CompositeILjava_lang_StringLjava_io_File();
	else if (getName().equals("test_deactivateInPlaceClient")) test_deactivateInPlaceClient();
	else if (getName().equals("test_doVerbI")) test_doVerbI();
	else if (getName().equals("test_execIILorg_eclipse_swt_ole_win32_VariantLorg_eclipse_swt_ole_win32_Variant")) test_execIILorg_eclipse_swt_ole_win32_VariantLorg_eclipse_swt_ole_win32_Variant();
	else if (getName().equals("test_getIndent")) test_getIndent();
	else if (getName().equals("test_getProgramID")) test_getProgramID();
	else if (getName().equals("test_isDirty")) test_isDirty();
	else if (getName().equals("test_isFocusControl")) test_isFocusControl();
	else if (getName().equals("test_queryStatusI")) test_queryStatusI();
	else if (getName().equals("test_saveLjava_io_FileZ")) test_saveLjava_io_FileZ();
	else if (getName().equals("test_setIndentLorg_eclipse_swt_graphics_Rectangle")) test_setIndentLorg_eclipse_swt_graphics_Rectangle();
	else if (getName().equals("test_showPropertiesLjava_lang_String")) test_showPropertiesLjava_lang_String();
	else super.runTest();
}
}
