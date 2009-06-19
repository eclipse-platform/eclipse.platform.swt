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
 * Automated Test Suite for class org.eclipse.swt.custom.ViewForm
 *
 * @see org.eclipse.swt.custom.ViewForm
 */
public class Test_org_eclipse_swt_custom_ViewForm extends Test_org_eclipse_swt_widgets_Composite {

public Test_org_eclipse_swt_custom_ViewForm(String name) {
	super(name);
}

public static void main(String[] args) {
	TestRunner.run(suite());
}

public void test_ConstructorLorg_eclipse_swt_widgets_CompositeI() {
	warnUnimpl("Test test_ConstructorLorg_eclipse_swt_widgets_CompositeI not written");
}

public void test_computeSizeIIZ() {
	warnUnimpl("Test test_computeSizeIIZ not written");
}

public void test_getClientArea() {
	warnUnimpl("Test test_getClientArea not written");
}

public void test_getContent() {
	warnUnimpl("Test test_getContent not written");
}

public void test_getTopCenter() {
	warnUnimpl("Test test_getTopCenter not written");
}

public void test_getTopLeft() {
	warnUnimpl("Test test_getTopLeft not written");
}

public void test_getTopRight() {
	warnUnimpl("Test test_getTopRight not written");
}

public void test_layoutZ() {
	warnUnimpl("Test test_layoutZ not written");
}

public void test_setBorderVisibleZ() {
	warnUnimpl("Test test_setBorderVisibleZ not written");
}

public void test_setContentLorg_eclipse_swt_widgets_Control() {
	warnUnimpl("Test test_setContentLorg_eclipse_swt_widgets_Control not written");
}

public void test_setFontLorg_eclipse_swt_graphics_Font() {
	warnUnimpl("Test test_setFontLorg_eclipse_swt_graphics_Font not written");
}

public void test_setLayoutLorg_eclipse_swt_widgets_Layout() {
	warnUnimpl("Test test_setLayoutLorg_eclipse_swt_widgets_Layout not written");
}

public void test_setTopCenterLorg_eclipse_swt_widgets_Control() {
	warnUnimpl("Test test_setTopCenterLorg_eclipse_swt_widgets_Control not written");
}

public void test_setTopCenterSeparateZ() {
	warnUnimpl("Test test_setTopCenterSeparateZ not written");
}

public void test_setTopLeftLorg_eclipse_swt_widgets_Control() {
	warnUnimpl("Test test_setTopLeftLorg_eclipse_swt_widgets_Control not written");
}

public void test_setTopRightLorg_eclipse_swt_widgets_Control() {
	warnUnimpl("Test test_setTopRightLorg_eclipse_swt_widgets_Control not written");
}


public static Test suite() {
	TestSuite suite = new TestSuite();
	java.util.Vector methodNames = methodNames();
	java.util.Enumeration e = methodNames.elements();
	while (e.hasMoreElements()) {
		suite.addTest(new Test_org_eclipse_swt_custom_ViewForm((String)e.nextElement()));
	}
	return suite;
}
public static java.util.Vector methodNames() {
	java.util.Vector methodNames = new java.util.Vector();
	methodNames.addElement("test_ConstructorLorg_eclipse_swt_widgets_CompositeI");
	methodNames.addElement("test_computeSizeIIZ");
	methodNames.addElement("test_getClientArea");
	methodNames.addElement("test_getContent");
	methodNames.addElement("test_getTopCenter");
	methodNames.addElement("test_getTopLeft");
	methodNames.addElement("test_getTopRight");
	methodNames.addElement("test_layoutZ");
	methodNames.addElement("test_setBorderVisibleZ");
	methodNames.addElement("test_setContentLorg_eclipse_swt_widgets_Control");
	methodNames.addElement("test_setFontLorg_eclipse_swt_graphics_Font");
	methodNames.addElement("test_setLayoutLorg_eclipse_swt_widgets_Layout");
	methodNames.addElement("test_setTopCenterLorg_eclipse_swt_widgets_Control");
	methodNames.addElement("test_setTopCenterSeparateZ");
	methodNames.addElement("test_setTopLeftLorg_eclipse_swt_widgets_Control");
	methodNames.addElement("test_setTopRightLorg_eclipse_swt_widgets_Control");
	methodNames.addAll(Test_org_eclipse_swt_widgets_Composite.methodNames()); // add superclass method names
	return methodNames;
}
protected void runTest() throws Throwable {
	if (getName().equals("test_ConstructorLorg_eclipse_swt_widgets_CompositeI")) test_ConstructorLorg_eclipse_swt_widgets_CompositeI();
	else if (getName().equals("test_computeSizeIIZ")) test_computeSizeIIZ();
	else if (getName().equals("test_getClientArea")) test_getClientArea();
	else if (getName().equals("test_getContent")) test_getContent();
	else if (getName().equals("test_getTopCenter")) test_getTopCenter();
	else if (getName().equals("test_getTopLeft")) test_getTopLeft();
	else if (getName().equals("test_getTopRight")) test_getTopRight();
	else if (getName().equals("test_layoutZ")) test_layoutZ();
	else if (getName().equals("test_setBorderVisibleZ")) test_setBorderVisibleZ();
	else if (getName().equals("test_setContentLorg_eclipse_swt_widgets_Control")) test_setContentLorg_eclipse_swt_widgets_Control();
	else if (getName().equals("test_setFontLorg_eclipse_swt_graphics_Font")) test_setFontLorg_eclipse_swt_graphics_Font();
	else if (getName().equals("test_setLayoutLorg_eclipse_swt_widgets_Layout")) test_setLayoutLorg_eclipse_swt_widgets_Layout();
	else if (getName().equals("test_setTopCenterLorg_eclipse_swt_widgets_Control")) test_setTopCenterLorg_eclipse_swt_widgets_Control();
	else if (getName().equals("test_setTopCenterSeparateZ")) test_setTopCenterSeparateZ();
	else if (getName().equals("test_setTopLeftLorg_eclipse_swt_widgets_Control")) test_setTopLeftLorg_eclipse_swt_widgets_Control();
	else if (getName().equals("test_setTopRightLorg_eclipse_swt_widgets_Control")) test_setTopRightLorg_eclipse_swt_widgets_Control();
	else super.runTest();
}
}
