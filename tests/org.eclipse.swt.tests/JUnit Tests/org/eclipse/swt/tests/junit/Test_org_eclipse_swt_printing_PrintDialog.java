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

public void test_getEndPage() {
	warnUnimpl("Test test_getEndPage not written");
}

public void test_getPrintToFile() {
	warnUnimpl("Test test_getPrintToFile not written");
}

public void test_getScope() {
	warnUnimpl("Test test_getScope not written");
}

public void test_getStartPage() {
	warnUnimpl("Test test_getStartPage not written");
}

public void test_open() {
	warnUnimpl("Test test_open not written");
}

public void test_setEndPageI() {
	warnUnimpl("Test test_setEndPageI not written");
}

public void test_setPrintToFileZ() {
	warnUnimpl("Test test_setPrintToFileZ not written");
}

public void test_setScopeI() {
	warnUnimpl("Test test_setScopeI not written");
}

public void test_setStartPageI() {
	warnUnimpl("Test test_setStartPageI not written");
}

public static Test suite() {
	TestSuite suite = new TestSuite();
	java.util.Vector methodNames = methodNames();
	java.util.Enumeration e = methodNames.elements();
	while (e.hasMoreElements()) {
		suite.addTest(new Test_org_eclipse_swt_printing_PrintDialog((String)e.nextElement()));
	}
	return suite;
}
public static java.util.Vector methodNames() {
	java.util.Vector methodNames = new java.util.Vector();
	methodNames.addElement("test_ConstructorLorg_eclipse_swt_widgets_Shell");
	methodNames.addElement("test_ConstructorLorg_eclipse_swt_widgets_ShellI");
	methodNames.addElement("test_getEndPage");
	methodNames.addElement("test_getPrintToFile");
	methodNames.addElement("test_getScope");
	methodNames.addElement("test_getStartPage");
	methodNames.addElement("test_open");
	methodNames.addElement("test_setEndPageI");
	methodNames.addElement("test_setPrintToFileZ");
	methodNames.addElement("test_setScopeI");
	methodNames.addElement("test_setStartPageI");
	methodNames.addAll(Test_org_eclipse_swt_widgets_Dialog.methodNames()); // add superclass method names
	return methodNames;
}
protected void runTest() throws Throwable {
	if (getName().equals("test_ConstructorLorg_eclipse_swt_widgets_Shell")) test_ConstructorLorg_eclipse_swt_widgets_Shell();
	else if (getName().equals("test_ConstructorLorg_eclipse_swt_widgets_ShellI")) test_ConstructorLorg_eclipse_swt_widgets_ShellI();
	else if (getName().equals("test_getEndPage")) test_getEndPage();
	else if (getName().equals("test_getPrintToFile")) test_getPrintToFile();
	else if (getName().equals("test_getScope")) test_getScope();
	else if (getName().equals("test_getStartPage")) test_getStartPage();
	else if (getName().equals("test_open")) test_open();
	else if (getName().equals("test_setEndPageI")) test_setEndPageI();
	else if (getName().equals("test_setPrintToFileZ")) test_setPrintToFileZ();
	else if (getName().equals("test_setScopeI")) test_setScopeI();
	else if (getName().equals("test_setStartPageI")) test_setStartPageI();
	else super.runTest();
}

/* custom */
	PrintDialog printDialog;
}
