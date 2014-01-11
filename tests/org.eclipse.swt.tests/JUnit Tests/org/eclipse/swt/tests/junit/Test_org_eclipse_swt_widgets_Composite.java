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


import static org.junit.Assert.assertArrayEquals;
import junit.framework.*;
import junit.textui.*;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.*;

/**
 * Automated Test Suite for class org.eclipse.swt.widgets.Composite
 *
 * @see org.eclipse.swt.widgets.Composite
 */
public class Test_org_eclipse_swt_widgets_Composite extends Test_org_eclipse_swt_widgets_Scrollable {

Composite composite;

public Test_org_eclipse_swt_widgets_Composite(String name) {
	super(name);
}

public static void main(String[] args) {
	TestRunner.run(suite());
}

@Override
protected void setUp() {
	super.setUp();
	composite = new Composite(shell, 0);
	super.setWidget(composite);
}

@Override
public void test_ConstructorLorg_eclipse_swt_widgets_CompositeI() {
	try {
		composite = new Composite(null, 0);
		fail("No exception thrown");
	}
	catch (IllegalArgumentException e) {
	}

	int[] cases = {SWT.H_SCROLL, SWT.V_SCROLL, SWT.H_SCROLL | SWT.V_SCROLL};
	for (int i = 0; i < cases.length; i++)
		composite = new Composite(shell, cases[i]);
}

public void test_getChildren() {
	assertArrayEquals(":a:", new Control[]{}, composite.getChildren());
	Composite c1 = new Composite(composite, 0);
	assertArrayEquals(":b:", new Control[]{c1}, composite.getChildren());

	List c2 = new List(composite, 0);
	assertArrayEquals(":c:", new Control[]{c1, c2}, composite.getChildren());

	Button c3 = new Button(composite, 0);
	assertArrayEquals(":d:", new Control[]{c1, c2, c3}, composite.getChildren());

	c2.dispose();
	assertArrayEquals(":e:", new Control[]{c1, c3}, composite.getChildren());
	
	Control[] children = composite.getChildren();
	for (int i = 0; i < children.length; i++)
		children[i].dispose();

	assertArrayEquals(":f:", new Control[]{}, composite.getChildren());
}


public void test_setTabList$Lorg_eclipse_swt_widgets_Control() {
	Button button1 = new Button(composite, SWT.PUSH);
	Button button2 = new Button(composite, SWT.PUSH);
	Control[] tablist = new Control[] {button1, button2};
	composite.setTabList(tablist);
	assertArrayEquals(tablist, composite.getTabList());
	button1.dispose();
	button2.dispose();
}

public static Test suite() {
	TestSuite suite = new TestSuite();
	java.util.Vector<String> methodNames = methodNames();
	java.util.Enumeration<String> e = methodNames.elements();
	while (e.hasMoreElements()) {
		suite.addTest(new Test_org_eclipse_swt_widgets_Composite(e.nextElement()));
	}
	return suite;
}
public static java.util.Vector<String> methodNames() {
	java.util.Vector<String> methodNames = new java.util.Vector<String>();
	methodNames.addElement("test_ConstructorLorg_eclipse_swt_widgets_CompositeI");
	methodNames.addElement("test_getChildren");
	methodNames.addElement("test_setTabList$Lorg_eclipse_swt_widgets_Control");
	methodNames.addAll(Test_org_eclipse_swt_widgets_Scrollable.methodNames()); // add superclass method names
	return methodNames;
}
@Override
protected void runTest() throws Throwable {
	if (getName().equals("test_ConstructorLorg_eclipse_swt_widgets_CompositeI")) test_ConstructorLorg_eclipse_swt_widgets_CompositeI();
	else if (getName().equals("test_getChildren")) test_getChildren();
	else if (getName().equals("test_setTabList$Lorg_eclipse_swt_widgets_Control")) test_setTabList$Lorg_eclipse_swt_widgets_Control();
	else super.runTest();
}

/* custom */
@Override
protected void setWidget(Widget w) {
	if (composite != null)
		composite.dispose();
	composite = (Composite)w;
	super.setWidget(w);
}
}
