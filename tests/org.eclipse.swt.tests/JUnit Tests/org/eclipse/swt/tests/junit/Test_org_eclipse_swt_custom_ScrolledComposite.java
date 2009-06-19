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
 * Automated Test Suite for class org.eclipse.swt.custom.ScrolledComposite
 *
 * @see org.eclipse.swt.custom.ScrolledComposite
 */
public class Test_org_eclipse_swt_custom_ScrolledComposite extends Test_org_eclipse_swt_widgets_Composite {

public Test_org_eclipse_swt_custom_ScrolledComposite(String name) {
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

public void test_getAlwaysShowScrollBars() {
	warnUnimpl("Test test_getAlwaysShowScrollBars not written");
}

public void test_getContent() {
	warnUnimpl("Test test_getContent not written");
}

public void test_getOrigin() {
	warnUnimpl("Test test_getOrigin not written");
}

public void test_layoutZ() {
	warnUnimpl("Test test_layoutZ not written");
}

public void test_setAlwaysShowScrollBarsZ() {
	warnUnimpl("Test test_setAlwaysShowScrollBarsZ not written");
}

public void test_setContentLorg_eclipse_swt_widgets_Control() {
	warnUnimpl("Test test_setContentLorg_eclipse_swt_widgets_Control not written");
}

public void test_setExpandHorizontalZ() {
	warnUnimpl("Test test_setExpandHorizontalZ not written");
}

public void test_setExpandVerticalZ() {
	warnUnimpl("Test test_setExpandVerticalZ not written");
}

public void test_setLayoutLorg_eclipse_swt_widgets_Layout() {
	warnUnimpl("Test test_setLayoutLorg_eclipse_swt_widgets_Layout not written");
}

public void test_setMinHeightI() {
	warnUnimpl("Test test_setMinHeightI not written");
}

public void test_setMinSizeII() {
	warnUnimpl("Test test_setMinSizeII not written");
}

public void test_setMinSizeLorg_eclipse_swt_graphics_Point() {
	warnUnimpl("Test test_setMinSizeLorg_eclipse_swt_graphics_Point not written");
}

public void test_setMinWidthI() {
	warnUnimpl("Test test_setMinWidthI not written");
}

public void test_setOriginII() {
	warnUnimpl("Test test_setOriginII not written");
}

public void test_setOriginLorg_eclipse_swt_graphics_Point() {
	warnUnimpl("Test test_setOriginLorg_eclipse_swt_graphics_Point not written");
}


public static Test suite() {
	TestSuite suite = new TestSuite();
	java.util.Vector methodNames = methodNames();
	java.util.Enumeration e = methodNames.elements();
	while (e.hasMoreElements()) {
		suite.addTest(new Test_org_eclipse_swt_custom_ScrolledComposite((String)e.nextElement()));
	}
	return suite;
}
public static java.util.Vector methodNames() {
	java.util.Vector methodNames = new java.util.Vector();
	methodNames.addElement("test_ConstructorLorg_eclipse_swt_widgets_CompositeI");
	methodNames.addElement("test_computeSizeIIZ");
	methodNames.addElement("test_getAlwaysShowScrollBars");
	methodNames.addElement("test_getContent");
	methodNames.addElement("test_getOrigin");
	methodNames.addElement("test_layoutZ");
	methodNames.addElement("test_setAlwaysShowScrollBarsZ");
	methodNames.addElement("test_setContentLorg_eclipse_swt_widgets_Control");
	methodNames.addElement("test_setExpandHorizontalZ");
	methodNames.addElement("test_setExpandVerticalZ");
	methodNames.addElement("test_setLayoutLorg_eclipse_swt_widgets_Layout");
	methodNames.addElement("test_setMinHeightI");
	methodNames.addElement("test_setMinSizeII");
	methodNames.addElement("test_setMinSizeLorg_eclipse_swt_graphics_Point");
	methodNames.addElement("test_setMinWidthI");
	methodNames.addElement("test_setOriginII");
	methodNames.addElement("test_setOriginLorg_eclipse_swt_graphics_Point");
	methodNames.addAll(Test_org_eclipse_swt_widgets_Composite.methodNames()); // add superclass method names
	return methodNames;
}
protected void runTest() throws Throwable {
	if (getName().equals("test_ConstructorLorg_eclipse_swt_widgets_CompositeI")) test_ConstructorLorg_eclipse_swt_widgets_CompositeI();
	else if (getName().equals("test_computeSizeIIZ")) test_computeSizeIIZ();
	else if (getName().equals("test_getAlwaysShowScrollBars")) test_getAlwaysShowScrollBars();
	else if (getName().equals("test_getContent")) test_getContent();
	else if (getName().equals("test_getOrigin")) test_getOrigin();
	else if (getName().equals("test_layoutZ")) test_layoutZ();
	else if (getName().equals("test_setAlwaysShowScrollBarsZ")) test_setAlwaysShowScrollBarsZ();
	else if (getName().equals("test_setContentLorg_eclipse_swt_widgets_Control")) test_setContentLorg_eclipse_swt_widgets_Control();
	else if (getName().equals("test_setExpandHorizontalZ")) test_setExpandHorizontalZ();
	else if (getName().equals("test_setExpandVerticalZ")) test_setExpandVerticalZ();
	else if (getName().equals("test_setLayoutLorg_eclipse_swt_widgets_Layout")) test_setLayoutLorg_eclipse_swt_widgets_Layout();
	else if (getName().equals("test_setMinHeightI")) test_setMinHeightI();
	else if (getName().equals("test_setMinSizeII")) test_setMinSizeII();
	else if (getName().equals("test_setMinSizeLorg_eclipse_swt_graphics_Point")) test_setMinSizeLorg_eclipse_swt_graphics_Point();
	else if (getName().equals("test_setMinWidthI")) test_setMinWidthI();
	else if (getName().equals("test_setOriginII")) test_setOriginII();
	else if (getName().equals("test_setOriginLorg_eclipse_swt_graphics_Point")) test_setOriginLorg_eclipse_swt_graphics_Point();
	else super.runTest();
}
}
