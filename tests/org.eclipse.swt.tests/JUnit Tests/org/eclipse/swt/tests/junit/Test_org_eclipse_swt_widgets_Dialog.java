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

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Shell;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Automated Test Suite for class org.eclipse.swt.widgets.Dialog
 *
 * @see org.eclipse.swt.widgets.Dialog
 */
public class Test_org_eclipse_swt_widgets_Dialog {

@Before
public void setUp() {
	shell = new Shell();
}

@After
public void tearDown() {
	shell.dispose();
}

@Test
public void test_getParent() {
	assertTrue(":a:", dialog.getParent() == shell);
}

@Test
public void test_getStyle() {
	// we use this call in a Constructor test so that we can
	// check if the style is the one that was created
	dialog.getStyle();
}

@Test
public void test_setTextLjava_lang_String() {
	assertTrue(":1:", dialog.getText() == "");
	String testStr = "test string";
	dialog.setText(testStr);
	assertTrue(":2:", dialog.getText().equals(testStr));
	dialog.setText("");
	assertTrue(":3:", dialog.getText().isEmpty());
	try {
		dialog.setText(null);
		fail("No exception thrown for string = null");
	}
	catch (IllegalArgumentException e) {
	}
}

/* custom */
public Shell shell;
private Dialog dialog;

protected void setDialog(Dialog newDialog) {
	dialog = newDialog;
}
}
