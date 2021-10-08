/*******************************************************************************
 * Copyright (c) 2000, 2021 IBM Corporation and others.
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
 *     Red Hat Inc. - Bug 462631
 *******************************************************************************/
package org.eclipse.swt.tests.junit;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ExtendedModifyListener;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.widgets.Shell;
import org.junit.Before;
import org.junit.Test;

/**
 * Automated Test Suite for class org.eclipse.swt.custom.ExtendedModifyListener
 *
 * @see org.eclipse.swt.custom.ExtendedModifyListener
 */
public class Test_org_eclipse_swt_custom_ExtendedModifyListener {
	Shell shell;
	StyledText styledText;
	int verify = -1;

@Before
public void setUp() {
	shell = new Shell();
	styledText = new StyledText(shell, SWT.NULL);
	shell.open();
}

@Test
public void test_modifyTextLorg_eclipse_swt_custom_ExtendedModifyEvent() {
	ExtendedModifyListener listener = event -> {
		switch(verify) {
			case 1 : {
				assertEquals(":1a:", 0, event.start);
				assertEquals(":1b:", 1, event.length);
				assertTrue(":1c:", event.replacedText.isEmpty());
				break;
			}
			case 2 : {
				assertEquals(":2a:", 0, event.start);
				assertEquals(":2b:", 1, event.length);
				assertEquals(":2c:", "\n\n", event.replacedText);
				break;
			}
			case 3 : {
				assertEquals(":3a:", 0, event.start);
				assertEquals(":3b:", 2, event.length);
				assertEquals(":3c:", "a", event.replacedText);
				break;
			}
			case 4: {
				assertTrue(":4:", false);
				break;
			}
			case 5 : {
				assertEquals(":5a:", 0, event.start);
				assertEquals(":5b:", 1, event.length);
				assertTrue(":5c:", event.replacedText.isEmpty());
				break;
			}
			case 6 : {
				assertEquals(":6a:", 21, event.start);
				assertEquals(":6b:", 0, event.length);
				assertEquals(":6c:", "\nline 4", event.replacedText);
				break;
			}
			case 7 : {
				assertEquals(":7a:", 5, event.start);
				assertEquals(":7b:", 0, event.length);
				assertEquals(":7c:", "is a test", event.replacedText);
				break;
			}
			case 8 : {
				assertEquals(":8a:", 7, event.start);
				assertEquals(":8b:", 0, event.length);
				assertEquals(":8c:", "\r\n", event.replacedText);
				break;
			}
			case 9 : {
				assertEquals(":9a:", 2, event.start);
				assertEquals(":9b:", 4, event.length);
				assertEquals(":9c:", "\r\n", event.replacedText);
				break;
			}
			case 10:{
				assertTrue(":10:", false);
				break;
			}
			case 11: {
				assertTrue(":11:", false);
				break;
			}
			case 12: {
				assertEquals(":12a:", 3, event.start);
				assertEquals(":12b:", 1, event.length);
				assertTrue(":12c:", event.replacedText.isEmpty());
				break;
			}
			case 13: {
				assertEquals(":13a:", 2, event.start);
				assertEquals(":13b:", 1, event.length);
				assertTrue(":13c:", event.replacedText.isEmpty());
				break;
			}
			case 14: {
				assertTrue(":14:", false);
				break;
			}
			case 15: {
				assertEquals(":15a:", 2, event.start);
				assertEquals(":15b:", 6, event.length);
				assertEquals(":15c:", "\r\n", event.replacedText);
				break;
			}
			case 16:{
				assertTrue(":16:", false);
				break;
			}
			case 17: {
				assertTrue(":17:", false);
				break;
			}
			case 18: {
				assertEquals(":18a:", 3, event.start);
				assertEquals(":18b:", 7, event.length);
				assertTrue(":18c:", event.replacedText.isEmpty());
				break;
			}
			case 19: {
				assertEquals(":19a:", 2, event.start);
				assertEquals(":19b:", 7, event.length);
				assertTrue(":19c:", event.replacedText.isEmpty());
				break;
			}
			case 20: {
				assertTrue(":20:", false);
				break;
			}
			case 21: {
				assertEquals(":21a:", 0, event.start);
				assertEquals(":21b:", 16, event.length);
				assertEquals(":21c:", "L1\r\nL2\r\nL3\r\nL4\r\n", event.replacedText);
				break;
			}
		}
	};
	styledText.addExtendedModifyListener(listener);

	verify = 0;
	styledText.setText("testing");
	verify = 1;
	styledText.replaceTextRange(0, 0, "\n");

	verify = 0;
	styledText.setText("\n\n");
	verify = 2;
	styledText.replaceTextRange(0, 2, "a");

	verify = 0;
	styledText.setText("a");
	verify = 3;
	styledText.replaceTextRange(0, 1, "\n\n");

	verify = 0;
	styledText.setText("L1\r\nL2\r\nL3\r\nL4\r\n");
	verify = 4;
	assertThrows(IllegalArgumentException.class, () -> styledText.replaceTextRange(3, 1, "test\n"));

	verify = 0;
	styledText.setText("Line 1\r\nLine 2");
	verify = 5;
	styledText.replaceTextRange(0, 0, "\r");

	verify = 0;
	styledText.setText("This\nis a test\nline 3\nline 4");
	verify = 6;
	styledText.replaceTextRange(21, 7, "");

	verify = 0;
	styledText.setText("This\nis a test\r");
	verify = 7;
	styledText.replaceTextRange(5, 9, "");

	verify = 0;
	styledText.setText("\nL1\r\nL2\r\n");
	verify = 8;
	styledText.replaceTextRange(7, 2, "");

	verify = 0;
	styledText.setText("L1\r\n");
	verify = 9;
	styledText.replaceTextRange(2, 2, "test");

	verify = 0;
	styledText.setText("L1\r\n");
	verify = 10;
	assertThrows(IllegalArgumentException.class, () ->styledText.replaceTextRange(3, 1, ""));

	verify = 0;
	styledText.setText("L1\r\nL2\r\nL3\r\nL4\r\n");
	verify = 11;
	assertThrows(IllegalArgumentException.class, () -> styledText.replaceTextRange(1, 2, ""));

	verify = 0;
	styledText.setText("L1\r");
	verify = 12;
	styledText.replaceTextRange(3, 0, "\n");

	verify = 0;
	styledText.setText("L1\n");
	verify = 13;
	styledText.replaceTextRange(2, 0, "\r");

	verify = 0;
	styledText.setText("L1\r\n");
	verify = 14;
	assertThrows(IllegalArgumentException.class, () ->styledText.replaceTextRange(3, 0, "test"));

	verify = 0;
	styledText.setText("L1\r\n");
	verify = 15;
	styledText.replaceTextRange(2, 2, "test\n\n");

	verify = 0;
	styledText.setText("L1\r\n");
	verify = 16;
	assertThrows(IllegalArgumentException.class, () ->styledText.replaceTextRange(3, 1, "test\r\n"));

	verify = 0;
	styledText.setText("L1\r\nL2\r\nL3\r\nL4\r\n");
	verify = 17;
	assertThrows(IllegalArgumentException.class, () ->styledText.replaceTextRange(1, 2, "test\n\n"));

	verify = 0;
	styledText.setText("L1\r");
	verify = 18;
	styledText.replaceTextRange(3, 0, "\ntest\r\n");

	verify = 0;
	styledText.setText("L1\n");
	verify = 19;
	styledText.replaceTextRange(2, 0, "test\r\r\r");

	verify = 0;
	styledText.setText("L1\r\nL2\r\nL3\r\nL4\r\n");
	verify = 20;
	assertThrows(IllegalArgumentException.class, () -> styledText.replaceTextRange(3, 1, "test\n"));

	verify = 21;
	styledText.setText("L1\r\nL2\r\nL3\r\nL4\r\n");
	styledText.removeExtendedModifyListener(listener);
}
}
