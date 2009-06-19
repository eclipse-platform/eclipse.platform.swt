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
 * Automated Test Suite for class org.eclipse.swt.custom.SashForm
 *
 * @see org.eclipse.swt.custom.SashForm
 */
public class Test_org_eclipse_swt_custom_SashForm extends Test_org_eclipse_swt_widgets_Composite {

public Test_org_eclipse_swt_custom_SashForm(String name) {
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

public void test_getMaximizedControl() {
	warnUnimpl("Test test_getMaximizedControl not written");
}

public void test_getOrientation() {
	warnUnimpl("Test test_getOrientation not written");
}

public void test_getWeights() {
	warnUnimpl("Test test_getWeights not written");
}

public void test_layoutZ() {
	warnUnimpl("Test test_layoutZ not written");
}

public void test_setBackgroundLorg_eclipse_swt_graphics_Color() {
	warnUnimpl("Test test_setBackgroundLorg_eclipse_swt_graphics_Color not written");
}

public void test_setForegroundLorg_eclipse_swt_graphics_Color() {
	warnUnimpl("Test test_setForegroundLorg_eclipse_swt_graphics_Color not written");
}

public void test_setLayoutLorg_eclipse_swt_widgets_Layout() {
	warnUnimpl("Test test_setLayoutLorg_eclipse_swt_widgets_Layout not written");
}

public void test_setMaximizedControlLorg_eclipse_swt_widgets_Control() {
	warnUnimpl("Test test_setMaximizedControlLorg_eclipse_swt_widgets_Control not written");
}

public void test_setOrientationI() {
	warnUnimpl("Test test_setOrientationI not written");
}

public void test_setWeights$I() {
	warnUnimpl("Test test_setWeights$I not written");
}

public static Test suite() {
	TestSuite suite = new TestSuite();
	java.util.Vector methodNames = methodNames();
	java.util.Enumeration e = methodNames.elements();
	while (e.hasMoreElements()) {
		suite.addTest(new Test_org_eclipse_swt_custom_SashForm((String)e.nextElement()));
	}
	return suite;
}

public static java.util.Vector methodNames() {
	java.util.Vector methodNames = new java.util.Vector();
	methodNames.addElement("test_ConstructorLorg_eclipse_swt_widgets_CompositeI");
	methodNames.addElement("test_computeSizeIIZ");
	methodNames.addElement("test_getMaximizedControl");
	methodNames.addElement("test_getOrientation");
	methodNames.addElement("test_getWeights");
	methodNames.addElement("test_layoutZ");
	methodNames.addElement("test_setBackgroundLorg_eclipse_swt_graphics_Color");
	methodNames.addElement("test_setForegroundLorg_eclipse_swt_graphics_Color");
	methodNames.addElement("test_setLayoutLorg_eclipse_swt_widgets_Layout");
	methodNames.addElement("test_setMaximizedControlLorg_eclipse_swt_widgets_Control");
	methodNames.addElement("test_setOrientationI");
	methodNames.addElement("test_setWeights$I");
	methodNames.addAll(Test_org_eclipse_swt_widgets_Composite.methodNames()); // add superclass method names
	return methodNames;
}

protected void runTest() throws Throwable {
	if (getName().equals("test_ConstructorLorg_eclipse_swt_widgets_CompositeI")) test_ConstructorLorg_eclipse_swt_widgets_CompositeI();
	else if (getName().equals("test_computeSizeIIZ")) test_computeSizeIIZ();
	else if (getName().equals("test_getMaximizedControl")) test_getMaximizedControl();
	else if (getName().equals("test_getOrientation")) test_getOrientation();
	else if (getName().equals("test_getWeights")) test_getWeights();
	else if (getName().equals("test_layoutZ")) test_layoutZ();
	else if (getName().equals("test_setBackgroundLorg_eclipse_swt_graphics_Color")) test_setBackgroundLorg_eclipse_swt_graphics_Color();
	else if (getName().equals("test_setForegroundLorg_eclipse_swt_graphics_Color")) test_setForegroundLorg_eclipse_swt_graphics_Color();
	else if (getName().equals("test_setLayoutLorg_eclipse_swt_widgets_Layout")) test_setLayoutLorg_eclipse_swt_widgets_Layout();
	else if (getName().equals("test_setMaximizedControlLorg_eclipse_swt_widgets_Control")) test_setMaximizedControlLorg_eclipse_swt_widgets_Control();
	else if (getName().equals("test_setOrientationI")) test_setOrientationI();
	else if (getName().equals("test_setWeights$I")) test_setWeights$I();
	else super.runTest();
}
}
