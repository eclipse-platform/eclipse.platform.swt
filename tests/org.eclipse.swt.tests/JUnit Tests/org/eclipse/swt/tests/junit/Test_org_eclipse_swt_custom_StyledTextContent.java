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


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.custom.StyledTextContent;
import org.eclipse.swt.custom.TextChangeListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Shell;
import org.junit.Before;
import org.junit.Test;

/**
 * Automated Test Suite for class org.eclipse.swt.custom.StyledTextContent
 *
 * @see org.eclipse.swt.custom.StyledTextContent
 */
public class Test_org_eclipse_swt_custom_StyledTextContent {
	int XINSET = 0;

	static class ContentImplementation implements StyledTextContent {
		String textContent = "";

		@Override
		public void addTextChangeListener(TextChangeListener listener){
		}
		@Override
		public int getCharCount() {
			return 0;
		}
		@Override
		public String getLine(int lineIndex) {
			return "getLine";
		}
		@Override
		public int getLineAtOffset(int offset) {
			return 0;
		}
		@Override
		public int getLineCount() {
			return 0;
		}
		@Override
		public String getLineDelimiter() {
			return "getLineDelimiter";
		}
		@Override
		public int getOffsetAtLine(int lineIndex) {
			return 0;
		}
		@Override
		public String getTextRange(int start, int length) {
			return textContent;
		}
		@Override
		public void removeTextChangeListener(TextChangeListener listener) {
		}
		@Override
		public void replaceTextRange(int start, int replaceLength, String text) {
			textContent = text;
		}
		@Override
		public void setText(String text) {
			textContent = text;
		}
	}
	StyledTextContent content = new ContentImplementation();
	Shell shell;
	StyledText styledText;

@Before
public void setUp() {
	if (SwtTestUtil.isBidi()) XINSET = 2;
	else XINSET = 0;
	shell = new Shell();
	styledText = new StyledText(shell, SWT.NULL);
	styledText.setContent(content);
}

@Test
public void test_getCharCount() {
	assertEquals(":a:", 0, styledText.getCharCount());
}

@Test
public void test_getLineAtOffsetI() {
	assertEquals(":c:", 0, styledText.getLineAtOffset(0));
}

@Test
public void test_getLineCount() {
	assertEquals(":d:", 1, styledText.getLineCount());
}

@Test
public void test_getLineDelimiter() {
	assertEquals(":e:", "getLineDelimiter", styledText.getLineDelimiter());
}

@Test
public void test_getLineI() {
	// will indirectly cause getLine to be called
	assertEquals(":b:", new Point(XINSET,0), styledText.getLocationAtOffset(0));
}

@Test
public void test_getOffsetAtLineI() {
	// will indirectly cause getOffsetAtLine to be called
	assertEquals(":f:", new Point(XINSET,0), styledText.getLocationAtOffset(0));
}

@Test
public void test_getTextRangeII() {
	assertTrue(":g:", styledText.getTextRange(0,0).isEmpty());
}

@Test
public void test_replaceTextRangeIILjava_lang_String() {
	styledText.replaceTextRange(0,0,"test1");
	assertEquals(":h:", "test1", styledText.getText());
}

@Test
public void test_setTextLjava_lang_String() {
	styledText.replaceTextRange(0,0,"test2");
	assertEquals(":i:", "test2", styledText.getText());
}

}
