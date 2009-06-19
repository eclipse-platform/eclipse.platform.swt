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
 * Automated Test Suite for class org.eclipse.swt.ole.win32.OleControlSite
 *
 * @see org.eclipse.swt.ole.win32.OleControlSite
 */
public class Test_org_eclipse_swt_ole_win32_OleControlSite extends Test_org_eclipse_swt_ole_win32_OleClientSite {

public Test_org_eclipse_swt_ole_win32_OleControlSite(String name) {
	super(name);
}

public static void main(String[] args) {
	TestRunner.run(suite());
}

public void test_ConstructorLorg_eclipse_swt_widgets_CompositeILjava_lang_String() {
	warnUnimpl("Test test_ConstructorLorg_eclipse_swt_widgets_CompositeILjava_lang_String not written");
}

public void test_addEventListenerILorg_eclipse_swt_ole_win32_OleListener() {
	warnUnimpl("Test test_addEventListenerILorg_eclipse_swt_ole_win32_OleListener not written");
}

public void test_addEventListenerLorg_eclipse_swt_ole_win32_OleAutomationILorg_eclipse_swt_ole_win32_OleListener() {
	warnUnimpl("Test test_addEventListenerLorg_eclipse_swt_ole_win32_OleAutomationILorg_eclipse_swt_ole_win32_OleListener not written");
}

public void test_addPropertyListenerILorg_eclipse_swt_ole_win32_OleListener() {
	warnUnimpl("Test test_addPropertyListenerILorg_eclipse_swt_ole_win32_OleListener not written");
}

public void test_getBackground() {
	warnUnimpl("Test test_getBackground not written");
}

public void test_getFont() {
	warnUnimpl("Test test_getFont not written");
}

public void test_getForeground() {
	warnUnimpl("Test test_getForeground not written");
}

public void test_getSitePropertyI() {
	warnUnimpl("Test test_getSitePropertyI not written");
}

public void test_removeEventListenerILorg_eclipse_swt_ole_win32_OleListener() {
	warnUnimpl("Test test_removeEventListenerILorg_eclipse_swt_ole_win32_OleListener not written");
}

public void test_removeEventListenerLorg_eclipse_swt_ole_win32_OleAutomationILorg_eclipse_swt_ole_win32_OleListener() {
	warnUnimpl("Test test_removeEventListenerLorg_eclipse_swt_ole_win32_OleAutomationILorg_eclipse_swt_ole_win32_OleListener not written");
}

public void test_removeEventListenerLorg_eclipse_swt_ole_win32_OleAutomationLorg_eclipse_swt_internal_ole_win32_GUIDILorg_eclipse_swt_ole_win32_OleListener() {
	warnUnimpl("Test test_removeEventListenerLorg_eclipse_swt_ole_win32_OleAutomationLorg_eclipse_swt_internal_ole_win32_GUIDILorg_eclipse_swt_ole_win32_OleListener not written");
}

public void test_removePropertyListenerILorg_eclipse_swt_ole_win32_OleListener() {
	warnUnimpl("Test test_removePropertyListenerILorg_eclipse_swt_ole_win32_OleListener not written");
}

public void test_setBackgroundLorg_eclipse_swt_graphics_Color() {
	warnUnimpl("Test test_setBackgroundLorg_eclipse_swt_graphics_Color not written");
}

public void test_setFontLorg_eclipse_swt_graphics_Font() {
	warnUnimpl("Test test_setFontLorg_eclipse_swt_graphics_Font not written");
}

public void test_setForegroundLorg_eclipse_swt_graphics_Color() {
	warnUnimpl("Test test_setForegroundLorg_eclipse_swt_graphics_Color not written");
}

public void test_setSitePropertyILorg_eclipse_swt_ole_win32_Variant() {
	warnUnimpl("Test test_setSitePropertyILorg_eclipse_swt_ole_win32_Variant not written");
}


public static Test suite() {
	TestSuite suite = new TestSuite();
	java.util.Vector methodNames = methodNames();
	java.util.Enumeration e = methodNames.elements();
	while (e.hasMoreElements()) {
		suite.addTest(new Test_org_eclipse_swt_ole_win32_OleControlSite((String)e.nextElement()));
	}
	return suite;
}

public static java.util.Vector methodNames() {
	java.util.Vector methodNames = new java.util.Vector();
	methodNames.addElement("test_ConstructorLorg_eclipse_swt_widgets_CompositeILjava_lang_String");
	methodNames.addElement("test_addEventListenerILorg_eclipse_swt_ole_win32_OleListener");
	methodNames.addElement("test_addEventListenerLorg_eclipse_swt_ole_win32_OleAutomationILorg_eclipse_swt_ole_win32_OleListener");
	methodNames.addElement("test_addPropertyListenerILorg_eclipse_swt_ole_win32_OleListener");
	methodNames.addElement("test_getBackground");
	methodNames.addElement("test_getFont");
	methodNames.addElement("test_getForeground");
	methodNames.addElement("test_getSitePropertyI");
	methodNames.addElement("test_removeEventListenerILorg_eclipse_swt_ole_win32_OleListener");
	methodNames.addElement("test_removeEventListenerLorg_eclipse_swt_ole_win32_OleAutomationILorg_eclipse_swt_ole_win32_OleListener");
	methodNames.addElement("test_removeEventListenerLorg_eclipse_swt_ole_win32_OleAutomationLorg_eclipse_swt_internal_ole_win32_GUIDILorg_eclipse_swt_ole_win32_OleListener");
	methodNames.addElement("test_removePropertyListenerILorg_eclipse_swt_ole_win32_OleListener");
	methodNames.addElement("test_setBackgroundLorg_eclipse_swt_graphics_Color");
	methodNames.addElement("test_setFontLorg_eclipse_swt_graphics_Font");
	methodNames.addElement("test_setForegroundLorg_eclipse_swt_graphics_Color");
	methodNames.addElement("test_setSitePropertyILorg_eclipse_swt_ole_win32_Variant");
	methodNames.addAll(Test_org_eclipse_swt_ole_win32_OleClientSite.methodNames()); // add superclass method names
	return methodNames;
}

protected void runTest() throws Throwable {
	if (getName().equals("test_ConstructorLorg_eclipse_swt_widgets_CompositeILjava_lang_String")) test_ConstructorLorg_eclipse_swt_widgets_CompositeILjava_lang_String();
	else if (getName().equals("test_addEventListenerILorg_eclipse_swt_ole_win32_OleListener")) test_addEventListenerILorg_eclipse_swt_ole_win32_OleListener();
	else if (getName().equals("test_addEventListenerLorg_eclipse_swt_ole_win32_OleAutomationILorg_eclipse_swt_ole_win32_OleListener")) test_addEventListenerLorg_eclipse_swt_ole_win32_OleAutomationILorg_eclipse_swt_ole_win32_OleListener();
	else if (getName().equals("test_addPropertyListenerILorg_eclipse_swt_ole_win32_OleListener")) test_addPropertyListenerILorg_eclipse_swt_ole_win32_OleListener();
	else if (getName().equals("test_getBackground")) test_getBackground();
	else if (getName().equals("test_getFont")) test_getFont();
	else if (getName().equals("test_getForeground")) test_getForeground();
	else if (getName().equals("test_getSitePropertyI")) test_getSitePropertyI();
	else if (getName().equals("test_removeEventListenerILorg_eclipse_swt_ole_win32_OleListener")) test_removeEventListenerILorg_eclipse_swt_ole_win32_OleListener();
	else if (getName().equals("test_removeEventListenerLorg_eclipse_swt_ole_win32_OleAutomationILorg_eclipse_swt_ole_win32_OleListener")) test_removeEventListenerLorg_eclipse_swt_ole_win32_OleAutomationILorg_eclipse_swt_ole_win32_OleListener();
	else if (getName().equals("test_removeEventListenerLorg_eclipse_swt_ole_win32_OleAutomationLorg_eclipse_swt_internal_ole_win32_GUIDILorg_eclipse_swt_ole_win32_OleListener")) test_removeEventListenerLorg_eclipse_swt_ole_win32_OleAutomationLorg_eclipse_swt_internal_ole_win32_GUIDILorg_eclipse_swt_ole_win32_OleListener();
	else if (getName().equals("test_removePropertyListenerILorg_eclipse_swt_ole_win32_OleListener")) test_removePropertyListenerILorg_eclipse_swt_ole_win32_OleListener();
	else if (getName().equals("test_setBackgroundLorg_eclipse_swt_graphics_Color")) test_setBackgroundLorg_eclipse_swt_graphics_Color();
	else if (getName().equals("test_setFontLorg_eclipse_swt_graphics_Font")) test_setFontLorg_eclipse_swt_graphics_Font();
	else if (getName().equals("test_setForegroundLorg_eclipse_swt_graphics_Color")) test_setForegroundLorg_eclipse_swt_graphics_Color();
	else if (getName().equals("test_setSitePropertyILorg_eclipse_swt_ole_win32_Variant")) test_setSitePropertyILorg_eclipse_swt_ole_win32_Variant();
	else super.runTest();
}
}
