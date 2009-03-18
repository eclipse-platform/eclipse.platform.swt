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
 * Automated Test Suite for class org.eclipse.swt.widgets.MessageBox
 *
 * @see org.eclipse.swt.widgets.MessageBox
 */
public class Test_org_eclipse_swt_widgets_MessageBox extends Test_org_eclipse_swt_widgets_Dialog {

public Test_org_eclipse_swt_widgets_MessageBox(String name) {
	super(name);
}

public static void main(String[] args) {
	TestRunner.run(suite());
}

protected void setUp() {
	super.setUp();
	messageBox = new MessageBox(shell, SWT.NULL);
	setDialog(messageBox);
}

/**
 * Possible exceptions:
 * 
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the parent</li>
 *    <li>ERROR_INVALID_SUBCLASS - if this class is not an allowed subclass</li>
 * </ul>
 */
public void test_ConstructorLorg_eclipse_swt_widgets_Shell(){
	new MessageBox(shell);
	try {
		new MessageBox(null);
		fail("No exception thrown for parent == null");
	}
	catch (IllegalArgumentException e) {
	}
}

public void test_ConstructorLorg_eclipse_swt_widgets_ShellI() {
	warnUnimpl("Test test_ConstructorLorg_eclipse_swt_widgets_ShellI not written");
}

public void test_getMessage() {
	warnUnimpl("Test test_getMessage not written");
}

/**
 * Makes the dialog visible and brings it to the front
 * of the display.
 *
 * @return the ID of the button that was selected to dismiss the
 *         message box (e.g. SWT.OK, SWT.CANCEL, etc...)
 */
public void test_open(){
	if (fTestDialogOpen)
		messageBox.open();
}

/**
 * Sets the dialog's message, which is a description of
 * the purpose for which it was opened. This message will be
 * visible on the dialog while it is open.
 */
public void test_setMessageLjava_lang_String(){
	assertEquals(messageBox.getMessage(), "");
	String testStr = "test string";
	messageBox.setMessage(testStr);
	assertEquals(messageBox.getMessage(), testStr);
	messageBox.setMessage("");
	assertEquals(messageBox.getMessage(), "");
	try {
		messageBox.setMessage(null);
		fail("No exception thrown");
	} 
	catch (IllegalArgumentException e) {
	}
}

public static Test suite() {
	TestSuite suite = new TestSuite();
	java.util.Vector methodNames = methodNames();
	java.util.Enumeration e = methodNames.elements();
	while (e.hasMoreElements()) {
		suite.addTest(new Test_org_eclipse_swt_widgets_MessageBox((String)e.nextElement()));
	}
	return suite;
}
public static java.util.Vector methodNames() {
	java.util.Vector methodNames = new java.util.Vector();
	methodNames.addElement("test_ConstructorLorg_eclipse_swt_widgets_Shell");
	methodNames.addElement("test_ConstructorLorg_eclipse_swt_widgets_ShellI");
	methodNames.addElement("test_getMessage");
	methodNames.addElement("test_open");
	methodNames.addElement("test_setMessageLjava_lang_String");
	methodNames.addAll(Test_org_eclipse_swt_widgets_Dialog.methodNames()); // add superclass method names
	return methodNames;
}
protected void runTest() throws Throwable {
	if (getName().equals("test_ConstructorLorg_eclipse_swt_widgets_Shell")) test_ConstructorLorg_eclipse_swt_widgets_Shell();
	else if (getName().equals("test_ConstructorLorg_eclipse_swt_widgets_ShellI")) test_ConstructorLorg_eclipse_swt_widgets_ShellI();
	else if (getName().equals("test_getMessage")) test_getMessage();
	else if (getName().equals("test_open")) test_open();
	else if (getName().equals("test_setMessageLjava_lang_String")) test_setMessageLjava_lang_String();
	else super.runTest();
}

/* custom */
MessageBox messageBox;
}
