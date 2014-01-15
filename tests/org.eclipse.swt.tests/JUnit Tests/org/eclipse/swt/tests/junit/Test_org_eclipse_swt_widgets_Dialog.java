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

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Shell;

/**
 * Automated Test Suite for class org.eclipse.swt.widgets.Dialog
 *
 * @see org.eclipse.swt.widgets.Dialog
 */
public class Test_org_eclipse_swt_widgets_Dialog extends TestCase {

public Test_org_eclipse_swt_widgets_Dialog(String name) {
	super(name);
}

@Override
protected void setUp() {
	shell = new Shell();
}

@Override
protected void tearDown() {
	shell.dispose();
}

public void test_getParent() {
	assertTrue(":a:", dialog.getParent() == shell);
}

public void test_getStyle() {
	// we use this call in a Constructor test so that we can
	// check if the style is the one that was created
	dialog.getStyle();
}


public void test_setTextLjava_lang_String() {
	assertTrue(":1:", dialog.getText() == "");
	String testStr = "test string";
	dialog.setText(testStr);
	assertTrue(":2:", dialog.getText().equals(testStr));
	dialog.setText("");
	assertTrue(":3:", dialog.getText().equals(""));
	try {
		dialog.setText(null);
		fail("No exception thrown for string = null");
	}
	catch (IllegalArgumentException e) {
	}
}

public static Test suite() {
	TestSuite suite = new TestSuite();
	java.util.Vector<String> methodNames = methodNames();
	java.util.Enumeration<String> e = methodNames.elements();
	while (e.hasMoreElements()) {
		suite.addTest(new Test_org_eclipse_swt_widgets_Dialog(e.nextElement()));
	}
	return suite;
}

public static java.util.Vector<String> methodNames() {
	java.util.Vector<String> methodNames = new java.util.Vector<String>();
	methodNames.addElement("test_getParent");
	methodNames.addElement("test_getStyle");
	methodNames.addElement("test_setTextLjava_lang_String");
	return methodNames;
}
@Override
protected void runTest() throws Throwable {
	if (getName().equals("test_getParent")) test_getParent();
	else if (getName().equals("test_getStyle")) test_getStyle();
	else if (getName().equals("test_setTextLjava_lang_String")) test_setTextLjava_lang_String();
}

/* custom */
public Shell shell;
private Dialog dialog;

protected void setDialog(Dialog newDialog) {
	dialog = newDialog;
}
}
