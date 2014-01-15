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

import org.eclipse.swt.*;
import org.eclipse.swt.printing.*;

/**
 * Automated Test Suite for class org.eclipse.swt.printing.PrintDialog
 *
 * @see org.eclipse.swt.printing.PrintDialog
 */
public class Test_org_eclipse_swt_printing_PrintDialog extends Test_org_eclipse_swt_widgets_Dialog {
	
public Test_org_eclipse_swt_printing_PrintDialog(String name) {
	super(name);
}

public static void main(String[] args) {
	TestRunner.run(suite());
}

@Override
protected void setUp() {
	super.setUp();
	printDialog = new PrintDialog(shell, SWT.NONE);
	setDialog(printDialog);
}

public void test_ConstructorLorg_eclipse_swt_widgets_Shell() {
	new PrintDialog(shell);
	
	try {
		new PrintDialog(null);
		fail("No exception thrown for parent == null");
	}
	catch (IllegalArgumentException e) {
	}
}

public void test_ConstructorLorg_eclipse_swt_widgets_ShellI() {
	new PrintDialog(shell, SWT.NONE);
	
	try {
		new PrintDialog(null, SWT.NONE);
		fail("No exception thrown for parent == null");
	}
	catch (IllegalArgumentException e) {
	}
}

public static Test suite() {
	TestSuite suite = new TestSuite();
	java.util.Vector<String> methodNames = methodNames();
	java.util.Enumeration<String> e = methodNames.elements();
	while (e.hasMoreElements()) {
		suite.addTest(new Test_org_eclipse_swt_printing_PrintDialog(e.nextElement()));
	}
	return suite;
}
public static java.util.Vector<String> methodNames() {
	java.util.Vector<String> methodNames = new java.util.Vector<String>();
	methodNames.addElement("test_ConstructorLorg_eclipse_swt_widgets_Shell");
	methodNames.addElement("test_ConstructorLorg_eclipse_swt_widgets_ShellI");
	methodNames.addAll(Test_org_eclipse_swt_widgets_Dialog.methodNames()); // add superclass method names
	return methodNames;
}
@Override
protected void runTest() throws Throwable {
	if (getName().equals("test_ConstructorLorg_eclipse_swt_widgets_Shell")) test_ConstructorLorg_eclipse_swt_widgets_Shell();
	else if (getName().equals("test_ConstructorLorg_eclipse_swt_widgets_ShellI")) test_ConstructorLorg_eclipse_swt_widgets_ShellI();
	else super.runTest();
}

/* custom */
	PrintDialog printDialog;
}
