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
 * Automated Test Suite for class org.eclipse.swt.awt.SWT_AWT
 *
 * @see org.eclipse.swt.awt.SWT_AWT
 */
public class Test_org_eclipse_swt_awt_SWT_AWT extends SwtTestCase {

public Test_org_eclipse_swt_awt_SWT_AWT(String name) {
	super(name);
}

public static void main(String[] args) {
	TestRunner.run(suite());
}

public void test_Constructor() {
	warnUnimpl("Test test_Constructor not written");
}

public void test_new_FrameLorg_eclipse_swt_widgets_Composite() {
	warnUnimpl("Test test_new_FrameLorg_eclipse_swt_widgets_Composite not written");
}

public void test_new_ShellLorg_eclipse_swt_widgets_DisplayLjava_awt_Canvas() {
	warnUnimpl("Test test_new_ShellLorg_eclipse_swt_widgets_DisplayLjava_awt_Canvas not written");
}


public static Test suite() {
	TestSuite suite = new TestSuite();
	java.util.Vector methodNames = methodNames();
	java.util.Enumeration e = methodNames.elements();
	while (e.hasMoreElements()) {
		suite.addTest(new Test_org_eclipse_swt_awt_SWT_AWT((String)e.nextElement()));
	}
	return suite;
}

public static java.util.Vector methodNames() {
	java.util.Vector methodNames = new java.util.Vector();
	methodNames.addElement("test_Constructor");
	methodNames.addElement("test_new_FrameLorg_eclipse_swt_widgets_Composite");
	methodNames.addElement("test_new_ShellLorg_eclipse_swt_widgets_DisplayLjava_awt_Canvas");
	return methodNames;
}

protected void runTest() throws Throwable {
	if (getName().equals("test_Constructor")) test_Constructor();
	else if (getName().equals("test_new_FrameLorg_eclipse_swt_widgets_Composite")) test_new_FrameLorg_eclipse_swt_widgets_Composite();
	else if (getName().equals("test_new_ShellLorg_eclipse_swt_widgets_DisplayLjava_awt_Canvas")) test_new_ShellLorg_eclipse_swt_widgets_DisplayLjava_awt_Canvas();
}
}
