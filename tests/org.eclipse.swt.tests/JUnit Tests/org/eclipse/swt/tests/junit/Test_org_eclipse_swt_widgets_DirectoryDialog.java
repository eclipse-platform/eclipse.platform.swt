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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Automated Test Suite for class org.eclipse.swt.widgets.DirectoryDialog
 *
 * @see org.eclipse.swt.widgets.DirectoryDialog
 */
public class Test_org_eclipse_swt_widgets_DirectoryDialog extends Test_org_eclipse_swt_widgets_Dialog {

@Override
@BeforeEach
public void setUp() {
	super.setUp();
	dirDialog = new DirectoryDialog(shell, SWT.NULL);
	setDialog(dirDialog);
}

@Test
public void test_ConstructorLorg_eclipse_swt_widgets_Shell() {
	new DirectoryDialog(shell);
	assertThrows(IllegalArgumentException.class,()-> new DirectoryDialog(null),"No exception thrown for null parent");
}

@Test
public void test_open() {
	if (SwtTestUtil.fTestDialogOpen)
		dirDialog.open();
}

@Test
public void test_setFilterPathLjava_lang_String() {
	assertTrue(dirDialog.getFilterPath() == "");
	String testStr = "./*";
	dirDialog.setFilterPath(testStr);
	assertEquals(testStr, dirDialog.getFilterPath());
	dirDialog.setFilterPath("");
	assertTrue(dirDialog.getFilterPath().isEmpty());
	dirDialog.setFilterPath(null);
	assertNull(dirDialog.getFilterPath());
}

@Test
public void test_setMessageLjava_lang_String() {
	assertTrue( dirDialog.getMessage() == "");
	String testStr = "test string";
	dirDialog.setMessage(testStr);
	assertEquals(testStr, dirDialog.getMessage());
	dirDialog.setMessage("");
	assertTrue(dirDialog.getMessage().isEmpty());
	assertThrows(IllegalArgumentException.class,()->dirDialog.setMessage(null),"null argument did not throw IllegalArgumentException");
}

/* custom */
DirectoryDialog dirDialog;
}
