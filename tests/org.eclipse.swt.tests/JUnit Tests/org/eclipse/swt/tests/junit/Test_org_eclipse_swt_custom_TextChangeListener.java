/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tests.junit;


import org.eclipse.swt.widgets.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.*;
import junit.framework.*;
import junit.textui.*;

/**
 * Automated Test Suite for class org.eclipse.swt.custom.TextChangeListener
 *
 * @see org.eclipse.swt.custom.TextChangeListener
 */
public class Test_org_eclipse_swt_custom_TextChangeListener extends SwtTestCase {
	Shell shell;
	StyledText styledText;
	int verify = -1;

public Test_org_eclipse_swt_custom_TextChangeListener(String name) {
	super(name);
}
public static void main(String[] args) {
	TestRunner.run(suite());
}
protected void setUp() {
	super.setUp();
	shell = new Shell();
	styledText = new StyledText(shell, SWT.NULL);
}

public void test_textChangedLorg_eclipse_swt_custom_TextChangedEvent() {
	StyledTextContent content = styledText.getContent();
	TextChangeListener listener = new TextChangeListener() {
		public void textChanged(TextChangedEvent event) {
			switch (verify) {
				case 1 : {
					assertTrue(":1:", styledText.getText().equals("\ntesting"));
					break;
				}
				case 2 : {
					assertTrue(":2:", styledText.getText().equals("a"));
					break;
				}
				case 3 : {
					assertTrue(":3:", styledText.getText().equals("\n\n"));
					break;
				}
				case 4: {
					assertTrue(":4:", false);
					break;
				}
				case 5 : {
					assertTrue(":5:", styledText.getText().equals("\rLine 1\r\nLine 2"));
					break;
				}
				case 6 : {
					assertTrue(":6:", styledText.getText().equals("This\nis a test\nline 3"));
					break;
				}
				case 7 : {
					assertTrue(":7:", styledText.getText().equals("This\n\r"));
					break;
				}
				case 8 : {
					assertTrue(":8:", styledText.getText().equals("\nL1\r\nL2"));
					break;
				}
				case 9 : {
					assertTrue(":9:", styledText.getText().equals("L1test"));
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
					assertTrue(":12:", styledText.getText().equals("L1\r\n"));
					break;
				}
				case 13: {
					assertTrue(":13:", styledText.getText().equals("L1\r\n"));
					break;
				}
				case 14: {
					assertTrue(":14:", false);
					break;
				}
				case 15: {
					assertTrue(":15:", styledText.getText().equals("L1test\n\n"));
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
					assertTrue(":18:", styledText.getText().equals("L1\r\ntest\r\n"));
					break;
				}
				case 19: {
					assertTrue(":19:", styledText.getText().equals("L1test\r\r\r\n"));
					break;
				}
				case 20: {
					assertTrue(":20:", false);
					break;
				}
			}
		}
		public void textChanging(TextChangingEvent event) {
		}
		public void textSet(TextChangedEvent event) {
		}
	};
	content.addTextChangeListener(listener);
	
	boolean exceptionHandled = false;
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
	try {styledText.replaceTextRange(3, 1, "test\n");}
	catch (IllegalArgumentException ex) {
		exceptionHandled = true;
	}
	assertTrue(":4: exception not thrown", exceptionHandled);
	exceptionHandled = false;

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
	try {styledText.replaceTextRange(3, 1, "");} 
	catch (IllegalArgumentException ex) {
		exceptionHandled = true;
	}
	assertTrue(":10: exception not thrown", exceptionHandled);
	exceptionHandled = false;

	verify = 0;
	styledText.setText("L1\r\nL2\r\nL3\r\nL4\r\n");
	verify = 11;
	try {styledText.replaceTextRange(1, 2, "");}
	catch (IllegalArgumentException ex) {
		exceptionHandled = true;
	}
	assertTrue(":11: exception not thrown", exceptionHandled);
	exceptionHandled = false;

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
	try {styledText.replaceTextRange(3, 0, "test");}
	catch (IllegalArgumentException ex) {
		exceptionHandled = true;
	}
	assertTrue(":14: exception not thrown", exceptionHandled);
	exceptionHandled = false;

	verify = 0;
	styledText.setText("L1\r\n");
	verify = 15;
	styledText.replaceTextRange(2, 2, "test\n\n");

	verify = 0;
	styledText.setText("L1\r\n");
	verify = 16;
	try {styledText.replaceTextRange(3, 1, "test\r\n");}
	catch (IllegalArgumentException ex) {
		exceptionHandled = true;
	}
	assertTrue(":16: exception not thrown", exceptionHandled);
	exceptionHandled = false;

	verify = 0;
	styledText.setText("L1\r\nL2\r\nL3\r\nL4\r\n");
	verify = 17;
	try {styledText.replaceTextRange(1, 2, "test\n\n");}
	catch (IllegalArgumentException ex) {
		exceptionHandled = true;
	}
	assertTrue(":17: exception not thrown", exceptionHandled);
	exceptionHandled = false;

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
	try {styledText.replaceTextRange(3, 1, "test\n");}
	catch (IllegalArgumentException ex) {
		exceptionHandled = true;
	}
	assertTrue(":20: exception not thrown", exceptionHandled);
	exceptionHandled = false;
	content.removeTextChangeListener(listener);
}

public void test_textChangingLorg_eclipse_swt_custom_TextChangingEvent() {
	StyledTextContent content = styledText.getContent();
	TextChangeListener listener = new TextChangeListener() {
		public void textChanging(TextChangingEvent event) {
			switch(verify) {
				case 1 : {
					assertTrue(":1a:", event.replaceLineCount == 0);
					assertTrue(":1b:", event.newLineCount == 1);
					break;
				}
				case 2 : {
					assertTrue(":2a:", event.replaceLineCount == 2);
					assertTrue(":2b:", event.newLineCount == 0);
					break;
				}
				case 3 : {
					assertTrue(":3a:", event.replaceLineCount == 0);
					assertTrue(":3b:", event.newLineCount == 2);
					break;
				}
				case 4: {
					assertTrue(":4:", false);
					break;
				}
				case 5 : {
					assertTrue(":5a:", event.replaceLineCount == 0);
					assertTrue(":5b:", event.newLineCount == 1);
					break;
				}
				case 6 : {
					assertTrue(":6a:", event.replaceLineCount == 1);
					assertTrue(":6b:", event.newLineCount == 0);
					break;
				}
				case 7 : {
					assertTrue(":7a:", event.replaceLineCount == 0);
					assertTrue(":7b:", event.newLineCount == 0);
					break;
				}
				case 8 : {
					assertTrue(":8a:", event.replaceLineCount == 1);
					assertTrue(":8b:", event.newLineCount == 0);
					break;
				}
				case 9 : {
					assertTrue(":9a:", event.replaceLineCount == 1);
					assertTrue(":9b:", event.newLineCount == 0);
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
					assertTrue(":12a:", event.replaceLineCount == 0);
					assertTrue(":12b:", event.newLineCount == 1);
					break;
				}
				case 13: {
					assertTrue(":13a:", event.replaceLineCount == 0);
					assertTrue(":13b:", event.newLineCount == 1);
					break;
				}
				case 14: {
					assertTrue(":14:", false);
					break;
				}
				case 15: {
					assertTrue(":15a:", event.replaceLineCount == 1);
					assertTrue(":15b:", event.newLineCount == 2);
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
					assertTrue(":18a:", event.replaceLineCount == 0);
					assertTrue(":18b:", event.newLineCount == 2);
					break;
				}
				case 19: {
					assertTrue(":19a:", event.replaceLineCount == 0);
					assertTrue(":19b:", event.newLineCount == 3);
					break;
				}
				case 20: {
					assertTrue(":20:", false);
					break;
				}
			}
		}
		public void textChanged(TextChangedEvent event) {
		}
		public void textSet(TextChangedEvent event) {
		}
	};
	content.addTextChangeListener(listener);

	boolean exceptionHandled = false;	
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
	try {styledText.replaceTextRange(3, 1, "test\n");}
	catch (IllegalArgumentException ex) {
		exceptionHandled = true;
	}
	assertTrue(":4: exception not thrown", exceptionHandled);
	exceptionHandled = false;

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
	try {styledText.replaceTextRange(3, 1, "");} 
	catch (IllegalArgumentException ex) {
		exceptionHandled = true;
	}
	assertTrue(":10: exception not thrown", exceptionHandled);
	exceptionHandled = false;

	verify = 11;
	styledText.setText("L1\r\nL2\r\nL3\r\nL4\r\n");
	try {styledText.replaceTextRange(1, 2, "");}
	catch (IllegalArgumentException ex) {
		exceptionHandled = true;
	}
	assertTrue(":11: exception not thrown", exceptionHandled);
	exceptionHandled = false;

	verify = 12;
	styledText.setText("L1\r");
	styledText.replaceTextRange(3, 0, "\n");

	verify = 13;
	styledText.setText("L1\n");
	styledText.replaceTextRange(2, 0, "\r");

	verify = 14;
	styledText.setText("L1\r\n");
	try {styledText.replaceTextRange(3, 0, "test");}
	catch (IllegalArgumentException ex) {
		exceptionHandled = true;
	}
	assertTrue(":14: exception not thrown", exceptionHandled);
	exceptionHandled = false;

	verify = 15;
	styledText.setText("L1\r\n");
	styledText.replaceTextRange(2, 2, "test\n\n");

	verify = 16;
	styledText.setText("L1\r\n");
	try {styledText.replaceTextRange(3, 1, "test\r\n");}
	catch (IllegalArgumentException ex) {
		exceptionHandled = true;
	}
	assertTrue(":16: exception not thrown", exceptionHandled);
	exceptionHandled = false;

	verify = 17;
	styledText.setText("L1\r\nL2\r\nL3\r\nL4\r\n");
	try {styledText.replaceTextRange(1, 2, "test\n\n");}
	catch (IllegalArgumentException ex) {
		exceptionHandled = true;
	}
	assertTrue(":17: exception not thrown", exceptionHandled);
	exceptionHandled = false;

	verify = 18;
 	styledText.setText("L1\r");
	styledText.replaceTextRange(3, 0, "\ntest\r\n");

	verify = 19;
	styledText.setText("L1\n");
	styledText.replaceTextRange(2, 0, "test\r\r\r");
	verify = 20;
	styledText.setText("L1\r\nL2\r\nL3\r\nL4\r\n");
	try {styledText.replaceTextRange(3, 1, "test\n");}
	catch (IllegalArgumentException ex) {
		exceptionHandled = true;
	}
	assertTrue(":20: exception not thrown", exceptionHandled);
	exceptionHandled = false;
	content.removeTextChangeListener(listener);
}

public void test_textSetLorg_eclipse_swt_custom_TextChangedEvent() {
	StyledTextContent content = styledText.getContent();
	TextChangeListener listener = new TextChangeListener() {
		public void textChanging(TextChangingEvent event) {
		}
		public void textChanged(TextChangedEvent event) {
		}
		public void textSet(TextChangedEvent event) {
			switch (verify) {
				case 1 : {
					assertTrue(":1:", styledText.getText().equals("testing"));
					break;
				}
				case 2 : {
					assertTrue(":2:", styledText.getText().equals("\n\n"));
					break;
				}
				case 3 : {
					assertTrue(":3:", styledText.getText().equals("a"));
					break;
				}
				case 4 : {
					assertTrue(":4:", styledText.getText().equals(""));
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
	try {styledText.setText(null);}
	catch (IllegalArgumentException ex) {assertTrue(":4:", true);}	
	content.removeTextChangeListener(listener);
}

public static Test suite() {
	TestSuite suite = new TestSuite();
	java.util.Vector methodNames = methodNames();
	java.util.Enumeration e = methodNames.elements();
	while (e.hasMoreElements()) {
		suite.addTest(new Test_org_eclipse_swt_custom_TextChangeListener((String)e.nextElement()));
	}
	return suite;
}

public static java.util.Vector methodNames() {
	java.util.Vector methodNames = new java.util.Vector();
	methodNames.addElement("test_textChangedLorg_eclipse_swt_custom_TextChangedEvent");
	methodNames.addElement("test_textChangingLorg_eclipse_swt_custom_TextChangingEvent");
	methodNames.addElement("test_textSetLorg_eclipse_swt_custom_TextChangedEvent");
	return methodNames;
}
protected void runTest() throws Throwable {
	if (getName().equals("test_textChangedLorg_eclipse_swt_custom_TextChangedEvent")) test_textChangedLorg_eclipse_swt_custom_TextChangedEvent();
	else if (getName().equals("test_textChangingLorg_eclipse_swt_custom_TextChangingEvent")) test_textChangingLorg_eclipse_swt_custom_TextChangingEvent();
	else if (getName().equals("test_textSetLorg_eclipse_swt_custom_TextChangedEvent")) test_textSetLorg_eclipse_swt_custom_TextChangedEvent();
}
}
