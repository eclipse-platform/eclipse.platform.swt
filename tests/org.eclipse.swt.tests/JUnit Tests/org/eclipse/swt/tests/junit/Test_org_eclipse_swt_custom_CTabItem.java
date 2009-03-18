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


import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import junit.framework.*;
import junit.textui.*;

/**
 * Automated Test Suite for class org.eclipse.swt.custom.CTabItem
 *
 * @see org.eclipse.swt.custom.CTabItem
 */
public class Test_org_eclipse_swt_custom_CTabItem extends Test_org_eclipse_swt_widgets_Item {

	CTabFolder cTabFolder;
	CTabItem cTabItem;

public Test_org_eclipse_swt_custom_CTabItem(String name) {
	super(name);
}

public static void main(String[] args) {
	TestRunner.run(suite());
}

protected void setUp() {
	super.setUp();
	cTabFolder = new CTabFolder(shell, SWT.NONE);
	cTabItem = new CTabItem(cTabFolder, SWT.NONE);
	setWidget(cTabItem);
}

public void test_ConstructorLorg_eclipse_swt_custom_CTabFolderI() {
	warnUnimpl("Test test_ConstructorLorg_eclipse_swt_custom_CTabFolderI not written");
}

public void test_ConstructorLorg_eclipse_swt_custom_CTabFolderII() {
	warnUnimpl("Test test_ConstructorLorg_eclipse_swt_custom_CTabFolderII not written");
}

public void test_dispose() {
	warnUnimpl("Test test_dispose not written");
}

public void test_getBounds() {
	warnUnimpl("Test test_getBounds not written");
}

public void test_getControl() {
	warnUnimpl("Test test_getControl not written");
}

public void test_getDisabledImage() {
	warnUnimpl("Test test_getDisabledImage not written");
}

public void test_getParent() {
	warnUnimpl("Test test_getParent not written");
}

public void test_getToolTipText() {
	warnUnimpl("Test test_getToolTipText not written");
}

public void test_setControlLorg_eclipse_swt_widgets_Control() {
	warnUnimpl("Test test_setControlLorg_eclipse_swt_widgets_Control not written");
}

public void test_setDisabledImageLorg_eclipse_swt_graphics_Image() {
	warnUnimpl("Test test_setDisabledImageLorg_eclipse_swt_graphics_Image not written");
}

public void test_setImageLorg_eclipse_swt_graphics_Image() {
	warnUnimpl("Test test_setImageLorg_eclipse_swt_graphics_Image not written");
}

public void test_setTextLjava_lang_String() {
	warnUnimpl("Test test_setTextLjava_lang_String not written");
}

public void test_setToolTipTextLjava_lang_String() {
	warnUnimpl("Test test_setToolTipTextLjava_lang_String not written");
}

public static Test suite() {
	TestSuite suite = new TestSuite();
	java.util.Vector methodNames = methodNames();
	java.util.Enumeration e = methodNames.elements();
	while (e.hasMoreElements()) {
		suite.addTest(new Test_org_eclipse_swt_custom_CTabItem((String)e.nextElement()));
	}
	return suite;
}

public static java.util.Vector methodNames() {
	java.util.Vector methodNames = new java.util.Vector();
	methodNames.addElement("test_ConstructorLorg_eclipse_swt_custom_CTabFolderI");
	methodNames.addElement("test_ConstructorLorg_eclipse_swt_custom_CTabFolderII");
	methodNames.addElement("test_dispose");
	methodNames.addElement("test_getBounds");
	methodNames.addElement("test_getControl");
	methodNames.addElement("test_getDisabledImage");
	methodNames.addElement("test_getParent");
	methodNames.addElement("test_getToolTipText");
	methodNames.addElement("test_setControlLorg_eclipse_swt_widgets_Control");
	methodNames.addElement("test_setDisabledImageLorg_eclipse_swt_graphics_Image");
	methodNames.addElement("test_setImageLorg_eclipse_swt_graphics_Image");
	methodNames.addElement("test_setTextLjava_lang_String");
	methodNames.addElement("test_setToolTipTextLjava_lang_String");
	methodNames.addAll(Test_org_eclipse_swt_widgets_Item.methodNames()); // add superclass method names
	return methodNames;
}

protected void runTest() throws Throwable {
	if (getName().equals("test_ConstructorLorg_eclipse_swt_custom_CTabFolderI")) test_ConstructorLorg_eclipse_swt_custom_CTabFolderI();
	else if (getName().equals("test_ConstructorLorg_eclipse_swt_custom_CTabFolderII")) test_ConstructorLorg_eclipse_swt_custom_CTabFolderII();
	else if (getName().equals("test_dispose")) test_dispose();
	else if (getName().equals("test_getBounds")) test_getBounds();
	else if (getName().equals("test_getControl")) test_getControl();
	else if (getName().equals("test_getDisabledImage")) test_getDisabledImage();
	else if (getName().equals("test_getParent")) test_getParent();
	else if (getName().equals("test_getToolTipText")) test_getToolTipText();
	else if (getName().equals("test_setControlLorg_eclipse_swt_widgets_Control")) test_setControlLorg_eclipse_swt_widgets_Control();
	else if (getName().equals("test_setDisabledImageLorg_eclipse_swt_graphics_Image")) test_setDisabledImageLorg_eclipse_swt_graphics_Image();
	else if (getName().equals("test_setImageLorg_eclipse_swt_graphics_Image")) test_setImageLorg_eclipse_swt_graphics_Image();
	else if (getName().equals("test_setTextLjava_lang_String")) test_setTextLjava_lang_String();
	else if (getName().equals("test_setToolTipTextLjava_lang_String")) test_setToolTipTextLjava_lang_String();
	else super.runTest();
}
}
