/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tests.junit;


import junit.framework.TestCase;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.custom.StyledTextContent;
import org.eclipse.swt.custom.TextChangeListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Shell;

/**
 * Automated Test Suite for class org.eclipse.swt.custom.StyledTextContent
 *
 * @see org.eclipse.swt.custom.StyledTextContent
 */
public class Test_org_eclipse_swt_custom_StyledTextContent extends TestCase {
	int XINSET = 0;

	class ContentImplementation implements StyledTextContent {
		String textContent = "";
		
		public void addTextChangeListener(TextChangeListener listener){
		}
		public int getCharCount() {
			return 0;
		}
		public String getLine(int lineIndex) {
			return "getLine";
		}
		public int getLineAtOffset(int offset) {
			return 0;
		}
		public int getLineCount() {
			return 0;
		}
		public String getLineDelimiter() {
			return "getLineDelimiter";
		}
		public int getOffsetAtLine(int lineIndex) {
			return 0;
		}
		public String getTextRange(int start, int length) {
			return textContent;
		}
		public void removeTextChangeListener(TextChangeListener listener) {
		}
		public void replaceTextRange(int start, int replaceLength, String text) {
			textContent = text;
		}
		public void setText(String text) {
			textContent = text;
		}
	}
	StyledTextContent content = new ContentImplementation();
	Shell shell;
	StyledText styledText;
	
@Override
protected void setUp() {
	if (SwtTestUtil.isBidi()) XINSET = 2;
	else XINSET = 0;
	shell = new Shell();
	styledText = new StyledText(shell, SWT.NULL);
	styledText.setContent(content);
}

public void test_getCharCount() {
	assertTrue(":a:", styledText.getCharCount() == 0);
}

public void test_getLineAtOffsetI() {
	assertTrue(":c:", styledText.getLineAtOffset(0) == 0);
}

public void test_getLineCount() {
	assertTrue(":d:", styledText.getLineCount() == 1);
}

public void test_getLineDelimiter() {
	assertTrue(":e:", styledText.getLineDelimiter().equals("getLineDelimiter"));
}

public void test_getLineI() {
	// will indirectly cause getLine to be called
	assertTrue(":b:", styledText.getLocationAtOffset(0).equals(new Point(XINSET,0)));
}

public void test_getOffsetAtLineI() {
	// will indirectly cause getOffsetAtLine to be called
	assertTrue(":f:", styledText.getLocationAtOffset(0).equals(new Point(XINSET,0)));
}

public void test_getTextRangeII() {
	assertTrue(":g:", styledText.getTextRange(0,0).equals(""));
}

public void test_replaceTextRangeIILjava_lang_String() {
	styledText.replaceTextRange(0,0,"test1");
	assertTrue(":h:", styledText.getText().equals("test1"));
}

public void test_setTextLjava_lang_String() {
	styledText.replaceTextRange(0,0,"test2");
	assertTrue(":i:", styledText.getText().equals("test2"));
}

}
