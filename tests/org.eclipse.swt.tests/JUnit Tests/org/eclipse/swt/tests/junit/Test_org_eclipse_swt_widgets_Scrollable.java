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


import org.eclipse.swt.widgets.*;

/**
 * Automated Test Suite for class org.eclipse.swt.widgets.Scrollable
 *
 * @see org.eclipse.swt.widgets.Scrollable
 */
public class Test_org_eclipse_swt_widgets_Scrollable extends Test_org_eclipse_swt_widgets_Control {

	Scrollable scrollable;
	
public Test_org_eclipse_swt_widgets_Scrollable(String name) {
	super(name);
}

protected void setUp() {
	super.setUp();
}

protected void tearDown() {
	super.tearDown();
}

protected void setWidget(Widget w) {
	scrollable = (Scrollable)w;
	super.setWidget(w);
}

public void test_computeTrimIIII() {
	scrollable.computeTrim(0, 0, 0, 0);
}

public void test_getClientArea() {
	scrollable.getClientArea();
}

public void test_getHorizontalBar() {
	ScrollBar hbar = scrollable.getHorizontalBar();
}

public void test_getVerticalBar() {
	ScrollBar vbar = scrollable.getVerticalBar();
}

public static java.util.Vector methodNames() {
	java.util.Vector methodNames = new java.util.Vector();
	methodNames.addElement("test_computeTrimIIII");
	methodNames.addElement("test_getClientArea");
	methodNames.addElement("test_getHorizontalBar");
	methodNames.addElement("test_getVerticalBar");
	methodNames.addAll(Test_org_eclipse_swt_widgets_Control.methodNames()); // add superclass method names
	return methodNames;
}
protected void runTest() throws Throwable {
	if (getName().equals("test_computeTrimIIII")) test_computeTrimIIII();
	else if (getName().equals("test_getClientArea")) test_getClientArea();
	else if (getName().equals("test_getHorizontalBar")) test_getHorizontalBar();
	else if (getName().equals("test_getVerticalBar")) test_getVerticalBar();
	else super.runTest();
}
}
