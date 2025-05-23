/*******************************************************************************
 * Copyright (c) 2000, 2015 IBM Corporation and others.
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
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.widgets.MessageBox;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Automated Test Suite for class org.eclipse.swt.widgets.MessageBox
 *
 * @see org.eclipse.swt.widgets.MessageBox
 */
public class Test_org_eclipse_swt_widgets_MessageBox extends Test_org_eclipse_swt_widgets_Dialog {


@Override
@BeforeEach
public void setUp() {
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
@Test
public void test_ConstructorLorg_eclipse_swt_widgets_Shell(){
	new MessageBox(shell);
	assertThrows(IllegalArgumentException.class, () -> new MessageBox(null),
			"No exception thrown for parent == null");
}

/**
 * Makes the dialog visible and brings it to the front
 * of the display.
 */
@Test
public void test_open(){
	if (SwtTestUtil.fTestDialogOpen)
		messageBox.open();
}

/**
 * Sets the dialog's message, which is a description of
 * the purpose for which it was opened. This message will be
 * visible on the dialog while it is open.
 */
@Test
public void test_setMessageLjava_lang_String(){
	assertEquals(messageBox.getMessage(), "");
	String testStr = "test string";
	messageBox.setMessage(testStr);
	assertEquals(messageBox.getMessage(), testStr);
	messageBox.setMessage("");
	assertEquals(messageBox.getMessage(), "");
	assertThrows(IllegalArgumentException.class, () -> messageBox.setMessage(null), "No exception thrown");
}

/* custom */
MessageBox messageBox;
}
