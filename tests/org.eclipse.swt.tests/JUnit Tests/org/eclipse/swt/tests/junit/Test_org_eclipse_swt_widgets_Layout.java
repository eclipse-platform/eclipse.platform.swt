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


/**
 * Automated Test Suite for class org.eclipse.swt.widgets.Layout
 *
 * @see org.eclipse.swt.widgets.Layout
 */
public class Test_org_eclipse_swt_widgets_Layout extends SwtTestCase {

public Test_org_eclipse_swt_widgets_Layout(String name) {
	super(name);
}


protected void setUp() {
}

protected void tearDown() {
}

public void test_Constructor() {
	warnUnimpl("Test test_Constructor not written");
}

public void test_computeSizeLorg_eclipse_swt_widgets_CompositeIIZ() {
	warnUnimpl("Test test_computeSizeLorg_eclipse_swt_widgets_CompositeIIZ not written");
}

public void test_layoutLorg_eclipse_swt_widgets_CompositeZ() {
	warnUnimpl("Test test_layoutLorg_eclipse_swt_widgets_CompositeZ not written");
}

public static java.util.Vector methodNames() {
	java.util.Vector methodNames = new java.util.Vector();
	methodNames.addElement("test_Constructor");
	methodNames.addElement("test_computeSizeLorg_eclipse_swt_widgets_CompositeIIZ");
	methodNames.addElement("test_layoutLorg_eclipse_swt_widgets_CompositeZ");
	return methodNames;
}
protected void runTest() throws Throwable {
	if (getName().equals("test_Constructor")) test_Constructor();
	else if (getName().equals("test_computeSizeLorg_eclipse_swt_widgets_CompositeIIZ")) test_computeSizeLorg_eclipse_swt_widgets_CompositeIIZ();
	else if (getName().equals("test_layoutLorg_eclipse_swt_widgets_CompositeZ")) test_layoutLorg_eclipse_swt_widgets_CompositeZ();
}
}
