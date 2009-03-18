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


import junit.framework.*;
import junit.textui.*;
import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.internal.BidiUtil;

/**
 * Automated Test Suite for class org.eclipse.swt.custom.StyledTextContent
 *
 * @see org.eclipse.swt.custom.StyledTextContent
 */
public class Test_org_eclipse_swt_custom_StyledTextContent extends SwtTestCase {
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
	
public Test_org_eclipse_swt_custom_StyledTextContent(String name) {
	super(name);
}

public static void main(String[] args) {
	TestRunner.run(suite());
}

protected void setUp() {
	if (isBidi()) XINSET = 2;
	else XINSET = 0;
	shell = new Shell();
	styledText = new StyledText(shell, SWT.NULL);
	styledText.setContent(content);
}

protected void tearDown() {
}

public void test_addTextChangeListenerLorg_eclipse_swt_custom_TextChangeListener() {
	// does not make sense to test, not called by StyledText
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

public void test_removeTextChangeListenerLorg_eclipse_swt_custom_TextChangeListener() {
	// does not make sense to test, not called by StyledText
}

public void test_replaceTextRangeIILjava_lang_String() {
	styledText.replaceTextRange(0,0,"test1");
	assertTrue(":h:", styledText.getText().equals("test1"));
}

public void test_setTextLjava_lang_String() {
	styledText.replaceTextRange(0,0,"test2");
	assertTrue(":i:", styledText.getText().equals("test2"));
}

public static Test suite() {
	TestSuite suite = new TestSuite();
	java.util.Vector methodNames = methodNames();
	java.util.Enumeration e = methodNames.elements();
	while (e.hasMoreElements()) {
		suite.addTest(new Test_org_eclipse_swt_custom_StyledTextContent((String)e.nextElement()));
	}
	return suite;
}

public static java.util.Vector methodNames() {
	java.util.Vector methodNames = new java.util.Vector();
	methodNames.addElement("test_addTextChangeListenerLorg_eclipse_swt_custom_TextChangeListener");
	methodNames.addElement("test_getCharCount");
	methodNames.addElement("test_getLineAtOffsetI");
	methodNames.addElement("test_getLineCount");
	methodNames.addElement("test_getLineDelimiter");
	methodNames.addElement("test_getLineI");
	methodNames.addElement("test_getOffsetAtLineI");
	methodNames.addElement("test_getTextRangeII");
	methodNames.addElement("test_removeTextChangeListenerLorg_eclipse_swt_custom_TextChangeListener");
	methodNames.addElement("test_replaceTextRangeIILjava_lang_String");
	methodNames.addElement("test_setTextLjava_lang_String");
	return methodNames;
}

protected void runTest() throws Throwable {
	if (getName().equals("test_addTextChangeListenerLorg_eclipse_swt_custom_TextChangeListener")) test_addTextChangeListenerLorg_eclipse_swt_custom_TextChangeListener();
	else if (getName().equals("test_getCharCount")) test_getCharCount();
	else if (getName().equals("test_getLineAtOffsetI")) test_getLineAtOffsetI();
	else if (getName().equals("test_getLineCount")) test_getLineCount();
	else if (getName().equals("test_getLineDelimiter")) test_getLineDelimiter();
	else if (getName().equals("test_getLineI")) test_getLineI();
	else if (getName().equals("test_getOffsetAtLineI")) test_getOffsetAtLineI();
	else if (getName().equals("test_getTextRangeII")) test_getTextRangeII();
	else if (getName().equals("test_removeTextChangeListenerLorg_eclipse_swt_custom_TextChangeListener")) test_removeTextChangeListenerLorg_eclipse_swt_custom_TextChangeListener();
	else if (getName().equals("test_replaceTextRangeIILjava_lang_String")) test_replaceTextRangeIILjava_lang_String();
	else if (getName().equals("test_setTextLjava_lang_String")) test_setTextLjava_lang_String();
}
}
