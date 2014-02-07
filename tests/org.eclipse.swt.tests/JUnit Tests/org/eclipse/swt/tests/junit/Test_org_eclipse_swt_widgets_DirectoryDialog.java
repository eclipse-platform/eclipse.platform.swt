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

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.DirectoryDialog;

/**
 * Automated Test Suite for class org.eclipse.swt.widgets.DirectoryDialog
 *
 * @see org.eclipse.swt.widgets.DirectoryDialog
 */
public class Test_org_eclipse_swt_widgets_DirectoryDialog extends Test_org_eclipse_swt_widgets_Dialog {

public Test_org_eclipse_swt_widgets_DirectoryDialog(String name) {
	super(name);
}

@Override
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

public void test_open() {
	if (SwtTestUtil.fTestDialogOpen)
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

/* custom */
DirectoryDialog dirDialog;
}
