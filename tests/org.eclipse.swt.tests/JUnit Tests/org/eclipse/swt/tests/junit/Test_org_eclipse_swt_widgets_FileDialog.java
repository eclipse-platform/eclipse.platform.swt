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

import junit.framework.*;
import junit.textui.*;
import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;

/**
 * Automated Test Suite for class org.eclipse.swt.widgets.FileDialog
 *
 * @see org.eclipse.swt.widgets.FileDialog
 */
public class Test_org_eclipse_swt_widgets_FileDialog extends Test_org_eclipse_swt_widgets_Dialog {

public Test_org_eclipse_swt_widgets_FileDialog(String name) {
	super(name);
}

public static void main(String[] args) {
	TestRunner.run(suite());
}

protected void setUp() {
	super.setUp();
	fileDialog = new FileDialog(shell, SWT.NULL);
	setDialog(fileDialog);
}

protected void tearDown() {
	super.tearDown();
}

public void test_ConstructorLorg_eclipse_swt_widgets_Shell() {
	// Test FileDialog(Shell)
	if (fCheckSwtNullExceptions) {
		FileDialog fd = new FileDialog(shell);
		try {
			fd = new FileDialog(null);
			fail("No exception thrown for parent == null");
		}
		catch (IllegalArgumentException e) {
		}
	}
}

public void test_ConstructorLorg_eclipse_swt_widgets_ShellI() {
	warnUnimpl("Test test_ConstructorLorg_eclipse_swt_widgets_ShellI not written");
}

public void test_getFileName() {
	warnUnimpl("Test test_getFileName not written");
}

public void test_getFileNames() {
	warnUnimpl("Test test_getFileNames not written");
}

public void test_getFilterExtensions() {
	warnUnimpl("Test test_getFilterExtensions not written");
}

public void test_getFilterNames() {
	warnUnimpl("Test test_getFilterNames not written");
}

public void test_getFilterPath() {
	// tested in test_setFilterPathLjava_lang_String
}

public void test_open() {
	if (fTestDialogOpen)
		fileDialog.open();
}

public void test_setFileNameLjava_lang_String() {
	warnUnimpl("Test test_setFileNameLjava_lang_String not written");
}

public void test_setFilterExtensions$Ljava_lang_String() {
	warnUnimpl("Test test_setFilterExtensions$Ljava_lang_String not written");
}

public void test_setFilterNames$Ljava_lang_String() {
	warnUnimpl("Test test_setFilterNames$Ljava_lang_String not written");
}

public void test_setFilterPathLjava_lang_String() {
	assertTrue(":1:", fileDialog.getFilterPath() == "");
	String testStr = "./*";
	fileDialog.setFilterPath(testStr);
	assertTrue(":2:", fileDialog.getFilterPath().equals(testStr));
	fileDialog.setFilterPath("");
	assertTrue(":3:", fileDialog.getFilterPath().equals(""));
	if (fCheckSwtNullExceptions) {
		try {
			fileDialog.setFilterPath(null);
			fail("No exception thrown for filterPath == null");
		}
		catch (IllegalArgumentException e) {
		}
	}
}

public static Test suite() {
	TestSuite suite = new TestSuite();
	java.util.Vector methodNames = methodNames();
	java.util.Enumeration e = methodNames.elements();
	while (e.hasMoreElements()) {
		suite.addTest(new Test_org_eclipse_swt_widgets_FileDialog((String)e.nextElement()));
	}
	return suite;
}
public static java.util.Vector methodNames() {
	java.util.Vector methodNames = new java.util.Vector();
	methodNames.addElement("test_ConstructorLorg_eclipse_swt_widgets_Shell");
	methodNames.addElement("test_ConstructorLorg_eclipse_swt_widgets_ShellI");
	methodNames.addElement("test_getFileName");
	methodNames.addElement("test_getFileNames");
	methodNames.addElement("test_getFilterExtensions");
	methodNames.addElement("test_getFilterNames");
	methodNames.addElement("test_getFilterPath");
	methodNames.addElement("test_open");
	methodNames.addElement("test_setFileNameLjava_lang_String");
	methodNames.addElement("test_setFilterExtensions$Ljava_lang_String");
	methodNames.addElement("test_setFilterNames$Ljava_lang_String");
	methodNames.addElement("test_setFilterPathLjava_lang_String");
	methodNames.addAll(Test_org_eclipse_swt_widgets_Dialog.methodNames()); // add superclass method names
	return methodNames;
}
protected void runTest() throws Throwable {
	if (getName().equals("test_ConstructorLorg_eclipse_swt_widgets_Shell")) test_ConstructorLorg_eclipse_swt_widgets_Shell();
	else if (getName().equals("test_ConstructorLorg_eclipse_swt_widgets_ShellI")) test_ConstructorLorg_eclipse_swt_widgets_ShellI();
	else if (getName().equals("test_getFileName")) test_getFileName();
	else if (getName().equals("test_getFileNames")) test_getFileNames();
	else if (getName().equals("test_getFilterExtensions")) test_getFilterExtensions();
	else if (getName().equals("test_getFilterNames")) test_getFilterNames();
	else if (getName().equals("test_getFilterPath")) test_getFilterPath();
	else if (getName().equals("test_open")) test_open();
	else if (getName().equals("test_setFileNameLjava_lang_String")) test_setFileNameLjava_lang_String();
	else if (getName().equals("test_setFilterExtensions$Ljava_lang_String")) test_setFilterExtensions$Ljava_lang_String();
	else if (getName().equals("test_setFilterNames$Ljava_lang_String")) test_setFilterNames$Ljava_lang_String();
	else if (getName().equals("test_setFilterPathLjava_lang_String")) test_setFilterPathLjava_lang_String();
	else super.runTest();
}

/* custom */
FileDialog fileDialog;
}
