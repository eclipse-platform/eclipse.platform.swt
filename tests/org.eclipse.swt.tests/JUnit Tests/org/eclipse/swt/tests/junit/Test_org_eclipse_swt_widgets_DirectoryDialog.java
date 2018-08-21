/*******************************************************************************
 * Copyright (c) 2000, 2016 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tests.junit;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.junit.Before;
import org.junit.Test;

/**
 * Automated Test Suite for class org.eclipse.swt.widgets.DirectoryDialog
 *
 * @see org.eclipse.swt.widgets.DirectoryDialog
 */
public class Test_org_eclipse_swt_widgets_DirectoryDialog extends Test_org_eclipse_swt_widgets_Dialog {

@Override
@Before
public void setUp() {
	super.setUp();
	dirDialog = new DirectoryDialog(shell, SWT.NULL);
	setDialog(dirDialog);
}

@Test
public void test_ConstructorLorg_eclipse_swt_widgets_Shell() {
	new DirectoryDialog(shell);
	try {
		new DirectoryDialog(null);
		fail("No exception thrown for null parent");
	}
	catch (IllegalArgumentException e) {
	}
}

@Test
public void test_open() {
	if (SwtTestUtil.fTestDialogOpen)
		dirDialog.open();
}

@Test
public void test_setFilterPathLjava_lang_String() {
	assertTrue(":1:", dirDialog.getFilterPath() == "");
	String testStr = "./*";
	dirDialog.setFilterPath(testStr);
	assertTrue(":2:", dirDialog.getFilterPath().equals(testStr));
	dirDialog.setFilterPath("");
	assertTrue(":3:", dirDialog.getFilterPath().isEmpty());
	dirDialog.setFilterPath(null);
	assertTrue(":4:", dirDialog.getFilterPath() == null);
}

@Test
public void test_setMessageLjava_lang_String() {
	assertTrue(":1:", dirDialog.getMessage() == "");
	String testStr = "test string";
	dirDialog.setMessage(testStr);
	assertTrue(":2:", dirDialog.getMessage().equals(testStr));
	dirDialog.setMessage("");
	assertTrue(":3:", dirDialog.getMessage().isEmpty());
	try {
		dirDialog.setMessage(null);
		fail ("null argument did not throw IllegalArgumentException");
	} catch (IllegalArgumentException e) {
	}
}

/* custom */
DirectoryDialog dirDialog;
}
