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

public void test_ConstructorLorg_eclipse_swt_widgets_Shell() {
	// Test FileDialog(Shell)
	new FileDialog(shell);
	try {
		new FileDialog(null);
		fail("No exception thrown for parent == null");
	}
	catch (IllegalArgumentException e) {
	}
}

public void test_ConstructorLorg_eclipse_swt_widgets_ShellI() {
	// Test FileDialog(Shell, int)
	FileDialog fd;
	fd = new FileDialog(shell, SWT.NULL);
	int style = fd.getStyle();
	style &= ~SWT.LEFT_TO_RIGHT;
	style &= ~SWT.RIGHT_TO_LEFT;
	assertTrue(style==SWT.APPLICATION_MODAL);
	
	fd = new FileDialog(shell, SWT.APPLICATION_MODAL);
	style = fd.getStyle();
	style &= ~SWT.LEFT_TO_RIGHT;
	style &= ~SWT.RIGHT_TO_LEFT;
	assertTrue(style==SWT.APPLICATION_MODAL);
	
	fd = new FileDialog(shell, SWT.PRIMARY_MODAL);
	style = fd.getStyle();
	style &= ~SWT.LEFT_TO_RIGHT;
	style &= ~SWT.RIGHT_TO_LEFT;
	assertTrue(style==SWT.PRIMARY_MODAL);
	
	fd = new FileDialog(shell, SWT.SYSTEM_MODAL);
	style = fd.getStyle();
	style &= ~SWT.LEFT_TO_RIGHT;
	style &= ~SWT.RIGHT_TO_LEFT;
	assertTrue(style==SWT.SYSTEM_MODAL);
}


public void test_getFileName() {
	//	tested in test_setFileNameLjava_lang_String()
}

public void test_getFileNames() {
	String[] names = fileDialog.getFileNames();
	assertTrue(names.length==0);
}

public void test_getFilterExtensions() {
	// tested in test_setFilterExtensions$Ljava_lang_String()
}

public void test_getFilterNames() {
	// tested in test_setFilterNames$Ljava_lang_String()
}

public void test_getFilterPath() {
	// tested in test_setFilterPathLjava_lang_String
}

public void test_open() {
	if (fTestDialogOpen)
		fileDialog.open();
}

public void test_setFileNameLjava_lang_String() {
	fileDialog.setFileName("");
	String name = fileDialog.getFileName();
	assertTrue(name.equals(""));
	fileDialog.setFileName(null);
	name = fileDialog.getFileName();
	assertTrue(name==null);
	fileDialog.setFileName("somefile.test");
	name = fileDialog.getFileName();
	assertTrue(name.equals("somefile.test"));
}

public void test_setFilterExtensions$Ljava_lang_String() {
	fileDialog.setFilterExtensions(new String[] {"txt","java"});
	String filters[] = fileDialog.getFilterExtensions();
	assertTrue(filters.length == 2);
	assertTrue(filters[0].equals("txt"));
	assertTrue(filters[1].equals("java"));
	fileDialog.setFilterExtensions(new String[] {""});
	filters = fileDialog.getFilterExtensions();
	assertTrue(filters.length == 1);
	fileDialog.setFilterExtensions(null);
	filters = fileDialog.getFilterExtensions();
	assertTrue(filters==null);
}

public void test_setFilterNames$Ljava_lang_String() {
	fileDialog.setFilterNames(new String[] {"a.txt","b.java"});
	String filters[] = fileDialog.getFilterNames();
	assertTrue(filters.length == 2);
	assertTrue(filters[0].equals("a.txt"));
	assertTrue(filters[1].equals("b.java"));
	fileDialog.setFilterNames(new String[] {""});
	filters = fileDialog.getFilterNames();
	assertTrue(filters.length == 1);
	fileDialog.setFilterNames(null);
	filters = fileDialog.getFilterNames();
	assertTrue(filters==null);
}

public void test_setFilterPathLjava_lang_String() {
	assertTrue(":1:", fileDialog.getFilterPath() == "");
	String testStr = "./*";
	fileDialog.setFilterPath(testStr);
	assertTrue(":2:", fileDialog.getFilterPath().equals(testStr));
	fileDialog.setFilterPath("");
	assertTrue(":3:", fileDialog.getFilterPath().equals(""));
	fileDialog.setFilterPath(null);
	assertTrue(":4:", fileDialog.getFilterPath() == null);
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
