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
import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;

/**
 * Automated Test Suite for class org.eclipse.swt.widgets.DirectoryDialog
 *
 * @see org.eclipse.swt.widgets.DirectoryDialog
 */
public class Test_org_eclipse_swt_widgets_DirectoryDialog extends Test_org_eclipse_swt_widgets_Dialog {

public Test_org_eclipse_swt_widgets_DirectoryDialog(String name) {
	super(name);
}

public static void main(String[] args) {
	TestRunner.run(suite());
}

protected void setUp() {
	super.setUp();
	dirDialog = new DirectoryDialog(shell, SWT.NULL);
	setDialog(dirDialog);
}

public void test_ConstructorLorg_eclipse_swt_widgets_Shell() {
	new DirectoryDialog(shell);
	try {
		new DirectoryDialog(null);
		fail("No exception thrown for null parent");
	}
	catch (IllegalArgumentException e) {
	}		
}

public void test_ConstructorLorg_eclipse_swt_widgets_ShellI() {
	warnUnimpl("Test test_ConstructorLorg_eclipse_swt_widgets_ShellI not written");
}

public void test_getFilterPath() {
	// tested in test_setFilterPathLjava_lang_String
}

public void test_getMessage() {
	// tested in test_setMessageLjava_lang_String
}

public void test_open() {
	if (fTestDialogOpen)
		dirDialog.open();
}

public void test_setFilterPathLjava_lang_String() {
	assertTrue(":1:", dirDialog.getFilterPath() == "");
	String testStr = "./*";
	dirDialog.setFilterPath(testStr);
	assertTrue(":2:", dirDialog.getFilterPath().equals(testStr));
	dirDialog.setFilterPath("");
	assertTrue(":3:", dirDialog.getFilterPath().equals(""));
	dirDialog.setFilterPath(null);
	assertTrue(":4:", dirDialog.getFilterPath() == null);
}

public void test_setMessageLjava_lang_String() {
	assertTrue(":1:", dirDialog.getMessage() == "");
	String testStr = "test string";
	dirDialog.setMessage(testStr);
	assertTrue(":2:", dirDialog.getMessage().equals(testStr));
	dirDialog.setMessage("");
	assertTrue(":3:", dirDialog.getMessage().equals(""));
	try {
		dirDialog.setMessage(null);
		fail ("null argument did not throw IllegalArgumentException");
	} catch (IllegalArgumentException e) {
	}
}

public static Test suite() {
	TestSuite suite = new TestSuite();
	java.util.Vector methodNames = methodNames();
	java.util.Enumeration e = methodNames.elements();
	while (e.hasMoreElements()) {
		suite.addTest(new Test_org_eclipse_swt_widgets_DirectoryDialog((String)e.nextElement()));
	}
	return suite;
}
public static java.util.Vector methodNames() {
	java.util.Vector methodNames = new java.util.Vector();
	methodNames.addElement("test_ConstructorLorg_eclipse_swt_widgets_Shell");
	methodNames.addElement("test_ConstructorLorg_eclipse_swt_widgets_ShellI");
	methodNames.addElement("test_getFilterPath");
	methodNames.addElement("test_getMessage");
	methodNames.addElement("test_open");
	methodNames.addElement("test_setFilterPathLjava_lang_String");
	methodNames.addElement("test_setMessageLjava_lang_String");
	methodNames.addAll(Test_org_eclipse_swt_widgets_Dialog.methodNames()); // add superclass method names
	return methodNames;
}
protected void runTest() throws Throwable {
	if (getName().equals("test_ConstructorLorg_eclipse_swt_widgets_Shell")) test_ConstructorLorg_eclipse_swt_widgets_Shell();
	else if (getName().equals("test_ConstructorLorg_eclipse_swt_widgets_ShellI")) test_ConstructorLorg_eclipse_swt_widgets_ShellI();
	else if (getName().equals("test_getFilterPath")) test_getFilterPath();
	else if (getName().equals("test_getMessage")) test_getMessage();
	else if (getName().equals("test_open")) test_open();
	else if (getName().equals("test_setFilterPathLjava_lang_String")) test_setFilterPathLjava_lang_String();
	else if (getName().equals("test_setMessageLjava_lang_String")) test_setMessageLjava_lang_String();
	else super.runTest();
}

/* custom */
DirectoryDialog dirDialog;
}
