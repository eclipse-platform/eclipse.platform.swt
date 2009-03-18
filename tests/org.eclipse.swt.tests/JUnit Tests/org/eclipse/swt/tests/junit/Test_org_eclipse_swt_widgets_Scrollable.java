/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
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
import org.eclipse.swt.widgets.*;

/**
 * Automated Test Suite for class org.eclipse.swt.widgets.Scrollable
 *
 * @see org.eclipse.swt.widgets.Scrollable
 */
public class Test_org_eclipse_swt_widgets_Scrollable extends Test_org_eclipse_swt_widgets_Control {
	
public Test_org_eclipse_swt_widgets_Scrollable(String name) {
	super(name);
}

public static void main(String[] args) {
	TestRunner.run(suite());
}

public void test_ConstructorLorg_eclipse_swt_widgets_CompositeI() {
	// abstract class
}

public void test_computeTrimIIII() {
	scrollable.computeTrim(0, 0, 0, 0);
}

public void test_getClientArea() {
	scrollable.getClientArea();
}

public void test_getHorizontalBar() {
	scrollable.getHorizontalBar();
}

public void test_getVerticalBar() {
	scrollable.getVerticalBar();
}

public static Test suite() {
	TestSuite suite = new TestSuite();
	java.util.Vector methodNames = methodNames();
	java.util.Enumeration e = methodNames.elements();
	while (e.hasMoreElements()) {
		suite.addTest(new Test_org_eclipse_swt_widgets_Scrollable((String)e.nextElement()));
	}
	return suite;
}

public static java.util.Vector methodNames() {
	java.util.Vector methodNames = new java.util.Vector();
	methodNames.addElement("test_ConstructorLorg_eclipse_swt_widgets_CompositeI");
	methodNames.addElement("test_computeTrimIIII");
	methodNames.addElement("test_getClientArea");
	methodNames.addElement("test_getHorizontalBar");
	methodNames.addElement("test_getVerticalBar");
	methodNames.addAll(Test_org_eclipse_swt_widgets_Control.methodNames()); // add superclass method names
	return methodNames;
}
protected void runTest() throws Throwable {
	if (getName().equals("test_ConstructorLorg_eclipse_swt_widgets_CompositeI")) test_ConstructorLorg_eclipse_swt_widgets_CompositeI();
	else if (getName().equals("test_computeTrimIIII")) test_computeTrimIIII();
	else if (getName().equals("test_getClientArea")) test_getClientArea();
	else if (getName().equals("test_getHorizontalBar")) test_getHorizontalBar();
	else if (getName().equals("test_getVerticalBar")) test_getVerticalBar();
	else super.runTest();
}

/* custom */
	Scrollable scrollable;

protected void setWidget(Widget w) {
	scrollable = (Scrollable)w;
	super.setWidget(w);
}
}
