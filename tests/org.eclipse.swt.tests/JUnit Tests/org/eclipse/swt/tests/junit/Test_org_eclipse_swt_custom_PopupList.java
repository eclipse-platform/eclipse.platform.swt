package org.eclipse.swt.tests.junit;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import org.eclipse.swt.custom.*;
import junit.framework.*;
import junit.textui.*;

/**
 * Automated Test Suite for class org.eclipse.swt.custom.PopupList
 *
 * @see org.eclipse.swt.custom.PopupList
 */
public class Test_org_eclipse_swt_custom_PopupList extends SwtTestCase {

public Test_org_eclipse_swt_custom_PopupList(String name) {
	super(name);
}

public static void main(String[] args) {
	TestRunner.run(suite());
}

protected void setUp() {
}

protected void tearDown() {
}

public void test_ConstructorLorg_eclipse_swt_widgets_Shell() {
	warnUnimpl("Test test_ConstructorLorg_eclipse_swt_widgets_Shell not written");
}

public void test_getFont() {
	warnUnimpl("Test test_getFont not written");
}

public void test_getItems() {
	warnUnimpl("Test test_getItems not written");
}

public void test_getMinimumWidth() {
	warnUnimpl("Test test_getMinimumWidth not written");
}

public void test_openLorg_eclipse_swt_graphics_Rectangle() {
	warnUnimpl("Test test_openLorg_eclipse_swt_graphics_Rectangle not written");
}

public void test_selectLjava_lang_String() {
	warnUnimpl("Test test_selectLjava_lang_String not written");
}

public void test_setFontLorg_eclipse_swt_graphics_Font() {
	warnUnimpl("Test test_setFontLorg_eclipse_swt_graphics_Font not written");
}

public void test_setItems$Ljava_lang_String() {
	warnUnimpl("Test test_setItems$Ljava_lang_String not written");
}

public void test_setMinimumWidthI() {
	warnUnimpl("Test test_setMinimumWidthI not written");
}

public static Test suite() {
	TestSuite suite = new TestSuite();
	java.util.Vector methodNames = methodNames();
	java.util.Enumeration e = methodNames.elements();
	while (e.hasMoreElements()) {
		suite.addTest(new Test_org_eclipse_swt_custom_PopupList((String)e.nextElement()));
	}
	return suite;
}
public static java.util.Vector methodNames() {
	java.util.Vector methodNames = new java.util.Vector();
	methodNames.addElement("test_ConstructorLorg_eclipse_swt_widgets_Shell");
	methodNames.addElement("test_getFont");
	methodNames.addElement("test_getItems");
	methodNames.addElement("test_getMinimumWidth");
	methodNames.addElement("test_openLorg_eclipse_swt_graphics_Rectangle");
	methodNames.addElement("test_selectLjava_lang_String");
	methodNames.addElement("test_setFontLorg_eclipse_swt_graphics_Font");
	methodNames.addElement("test_setItems$Ljava_lang_String");
	methodNames.addElement("test_setMinimumWidthI");
	return methodNames;
}
protected void runTest() throws Throwable {
	if (getName().equals("test_ConstructorLorg_eclipse_swt_widgets_Shell")) test_ConstructorLorg_eclipse_swt_widgets_Shell();
	else if (getName().equals("test_getFont")) test_getFont();
	else if (getName().equals("test_getItems")) test_getItems();
	else if (getName().equals("test_getMinimumWidth")) test_getMinimumWidth();
	else if (getName().equals("test_openLorg_eclipse_swt_graphics_Rectangle")) test_openLorg_eclipse_swt_graphics_Rectangle();
	else if (getName().equals("test_selectLjava_lang_String")) test_selectLjava_lang_String();
	else if (getName().equals("test_setFontLorg_eclipse_swt_graphics_Font")) test_setFontLorg_eclipse_swt_graphics_Font();
	else if (getName().equals("test_setItems$Ljava_lang_String")) test_setItems$Ljava_lang_String();
	else if (getName().equals("test_setMinimumWidthI")) test_setMinimumWidthI();
}
}
