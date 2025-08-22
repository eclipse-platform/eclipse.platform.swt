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
 *     Red Hat Inc. - Bug 462631
 *******************************************************************************/
package org.eclipse.swt.tests.junit;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.custom.StyledTextContent;
import org.eclipse.swt.custom.TextChangeListener;
import org.eclipse.swt.custom.TextChangedEvent;
import org.eclipse.swt.custom.TextChangingEvent;
import org.eclipse.swt.widgets.Shell;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Automated Test Suite for class org.eclipse.swt.custom.TextChangeListener
 *
 * @see org.eclipse.swt.custom.TextChangeListener
 */
public class Test_org_eclipse_swt_custom_TextChangeListener {
	Shell shell;
	StyledText styledText;
	int verify = -1;

@BeforeEach
public void setUp() {
	shell = new Shell();
	styledText = new StyledText(shell, SWT.NULL);
}

@Test
public void test_textChangedLorg_eclipse_swt_custom_TextChangedEvent() {
	StyledTextContent content = styledText.getContent();
	TextChangeListener listener = new TextChangeListener() {
		@Override
		public void textChanged(TextChangedEvent event) {
			switch (verify) {
				case 1 : {
					assertEquals("\ntesting", styledText.getText());
					break;
				}
				case 2 : {
					assertEquals("a", styledText.getText());
					break;
				}
				case 3 : {
					assertEquals("\n\n", styledText.getText());
					break;
				}
				case 4: {
					fail();
					break;
				}
				case 5 : {
					assertEquals("\rLine 1\r\nLine 2", styledText.getText());
					break;
				}
				case 6 : {
					assertEquals("This\nis a test\nline 3", styledText.getText());
					break;
				}
				case 7 : {
					assertEquals("This\n\r", styledText.getText());
					break;
				}
				case 8 : {
					assertEquals("\nL1\r\nL2", styledText.getText());
					break;
				}
				case 9 : {
					assertEquals("L1test", styledText.getText());
					break;
				}
				case 10:{
					fail();
					break;
				}
				case 11: {
					fail();
					break;
				}
				case 12: {
					assertEquals("L1\r\n", styledText.getText());
					break;
				}
				case 13: {
					assertEquals("L1\r\n", styledText.getText());
					break;
				}
				case 14: {
					fail();
					break;
				}
				case 15: {
					assertEquals("L1test\n\n", styledText.getText());
					break;
				}
				case 16:{
					fail();
					break;
				}
				case 17: {
					fail();
					break;
				}
				case 18: {
					assertEquals("L1\r\ntest\r\n", styledText.getText());
					break;
				}
				case 19: {
					assertEquals("L1test\r\r\r\n", styledText.getText());
					break;
				}
				case 20: {
					fail();
					break;
				}
			}
		}
		@Override
		public void textChanging(TextChangingEvent event) {
		}
		@Override
		public void textSet(TextChangedEvent event) {
		}
	};
	content.addTextChangeListener(listener);

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
	assertThrows(IllegalArgumentException.class, ()->styledText.replaceTextRange(3, 1, "test\n"));

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
	assertThrows(IllegalArgumentException.class, ()->styledText.replaceTextRange(3, 1, ""));

	verify = 0;
	styledText.setText("L1\r\nL2\r\nL3\r\nL4\r\n");
	verify = 11;
	assertThrows(IllegalArgumentException.class, ()->styledText.replaceTextRange(1, 2, ""));

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
	assertThrows(IllegalArgumentException.class, ()->styledText.replaceTextRange(3, 0, "test"));

	verify = 0;
	styledText.setText("L1\r\n");
	verify = 15;
	styledText.replaceTextRange(2, 2, "test\n\n");

	verify = 0;
	styledText.setText("L1\r\n");
	verify = 16;
	assertThrows(IllegalArgumentException.class, ()->styledText.replaceTextRange(3, 1, "test\r\n"));

	verify = 0;
	styledText.setText("L1\r\nL2\r\nL3\r\nL4\r\n");
	verify = 17;
	assertThrows(IllegalArgumentException.class, ()->styledText.replaceTextRange(1, 2, "test\n\n"));

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
	assertThrows(IllegalArgumentException.class, ()->styledText.replaceTextRange(3, 1, "test\n"));
	content.removeTextChangeListener(listener);
}

@Test
public void test_textChangingLorg_eclipse_swt_custom_TextChangingEvent() {
	StyledTextContent content = styledText.getContent();
	TextChangeListener listener = new TextChangeListener() {
		@Override
		public void textChanging(TextChangingEvent event) {
			switch(verify) {
				case 1 : {
					assertEquals(0, event.replaceLineCount);
					assertEquals(1, event.newLineCount);
					break;
				}
				case 2 : {
					assertEquals(2, event.replaceLineCount);
					assertEquals(0, event.newLineCount);
					break;
				}
				case 3 : {
					assertEquals(0, event.replaceLineCount);
					assertEquals(2, event.newLineCount);
					break;
				}
				case 4: {
					fail();
					break;
				}
				case 5 : {
					assertEquals(0, event.replaceLineCount);
					assertEquals(1, event.newLineCount);
					break;
				}
				case 6 : {
					assertEquals(1, event.replaceLineCount);
					assertEquals(0, event.newLineCount);
					break;
				}
				case 7 : {
					assertEquals(0, event.replaceLineCount);
					assertEquals(0, event.newLineCount);
					break;
				}
				case 8 : {
					assertEquals(1, event.replaceLineCount);
					assertEquals(0, event.newLineCount);
					break;
				}
				case 9 : {
					assertEquals(1, event.replaceLineCount);
					assertEquals(0, event.newLineCount);
					break;
				}
				case 10:{
					fail();
					break;
				}
				case 11: {
					fail();
					break;
				}
				case 12: {
					assertEquals(0, event.replaceLineCount);
					assertEquals(1, event.newLineCount);
					break;
				}
				case 13: {
					assertEquals(0, event.replaceLineCount);
					assertEquals(1, event.newLineCount);
					break;
				}
				case 14: {
					fail();
					break;
				}
				case 15: {
					assertEquals(1, event.replaceLineCount);
					assertEquals(2, event.newLineCount);
					break;
				}
				case 16:{
					fail();
					break;
				}
				case 17: {
					fail();
					break;
				}
				case 18: {
					assertEquals(0, event.replaceLineCount);
					assertEquals(2, event.newLineCount);
					break;
				}
				case 19: {
					assertEquals(0, event.replaceLineCount);
					assertEquals(3, event.newLineCount);
					break;
				}
				case 20: {
					fail();
					break;
				}
			}
		}
		@Override
		public void textChanged(TextChangedEvent event) {
		}
		@Override
		public void textSet(TextChangedEvent event) {
		}
	};
	content.addTextChangeListener(listener);

	verify = 1;
	styledText.setText("testing");
	styledText.replaceTextRange(0, 0, "\n");

	verify = 2;
	styledText.setText("\n\n");
	styledText.replaceTextRange(0, 2, "a");

	verify = 3;
	styledText.setText("a");
	styledText.replaceTextRange(0, 1, "\n\n");

	verify = 4;
	styledText.setText("L1\r\nL2\r\nL3\r\nL4\r\n");
	assertThrows(IllegalArgumentException.class, ()->styledText.replaceTextRange(3, 1, "test\n"));

	verify = 5;
	styledText.setText("Line 1\r\nLine 2");
	styledText.replaceTextRange(0, 0, "\r");

	verify = 6;
	styledText.setText("This\nis a test\nline 3\nline 4");
	styledText.replaceTextRange(21, 7, "");

	verify = 7;
	styledText.setText("This\nis a test\r");
	styledText.replaceTextRange(5, 9, "");

	verify = 8;
	styledText.setText("\nL1\r\nL2\r\n");
	styledText.replaceTextRange(7, 2, "");

	verify = 9;
	styledText.setText("L1\r\n");
	styledText.replaceTextRange(2, 2, "test");

	verify = 10;
	styledText.setText("L1\r\n");
	assertThrows(IllegalArgumentException.class, ()->styledText.replaceTextRange(3, 1, ""));

	verify = 11;
	styledText.setText("L1\r\nL2\r\nL3\r\nL4\r\n");
	assertThrows(IllegalArgumentException.class, ()->styledText.replaceTextRange(1, 2, ""));

	verify = 12;
	styledText.setText("L1\r");
	styledText.replaceTextRange(3, 0, "\n");

	verify = 13;
	styledText.setText("L1\n");
	styledText.replaceTextRange(2, 0, "\r");

	verify = 14;
	styledText.setText("L1\r\n");
	assertThrows(IllegalArgumentException.class, ()->styledText.replaceTextRange(3, 0, "test"));

	verify = 15;
	styledText.setText("L1\r\n");
	styledText.replaceTextRange(2, 2, "test\n\n");

	verify = 16;
	styledText.setText("L1\r\n");
	assertThrows(IllegalArgumentException.class, ()->styledText.replaceTextRange(3, 1, "test\r\n"));

	verify = 17;
	styledText.setText("L1\r\nL2\r\nL3\r\nL4\r\n");
	assertThrows(IllegalArgumentException.class, ()->styledText.replaceTextRange(1, 2, "test\n\n"));

	verify = 18;
	styledText.setText("L1\r");
	styledText.replaceTextRange(3, 0, "\ntest\r\n");

	verify = 19;
	styledText.setText("L1\n");
	styledText.replaceTextRange(2, 0, "test\r\r\r");
	verify = 20;
	styledText.setText("L1\r\nL2\r\nL3\r\nL4\r\n");
	assertThrows(IllegalArgumentException.class, ()->styledText.replaceTextRange(3, 1, "test\n"));
	content.removeTextChangeListener(listener);
}

@Test
public void test_textSetLorg_eclipse_swt_custom_TextChangedEvent() {
	StyledTextContent content = styledText.getContent();
	TextChangeListener listener = new TextChangeListener() {
		@Override
		public void textChanging(TextChangingEvent event) {
		}
		@Override
		public void textChanged(TextChangedEvent event) {
		}
		@Override
		public void textSet(TextChangedEvent event) {
			switch (verify) {
				case 1 : {
					assertEquals("testing", styledText.getText());
					break;
				}
				case 2 : {
					assertEquals("\n\n",styledText.getText());
					break;
				}
				case 3 : {
					assertEquals("a", styledText.getText());
					break;
				}
				case 4 : {
					assertTrue(styledText.getText().isEmpty());
					break;
				}
			}
		}
	};
	content.addTextChangeListener(listener);

	verify = 1;
	styledText.setText("testing");

	verify = 2;
	styledText.setText("\n\n");

	verify = 3;
	styledText.setText("a");

	verify = 4;
	assertThrows(IllegalArgumentException.class, ()->styledText.setText(null));
	content.removeTextChangeListener(listener);
}
}
