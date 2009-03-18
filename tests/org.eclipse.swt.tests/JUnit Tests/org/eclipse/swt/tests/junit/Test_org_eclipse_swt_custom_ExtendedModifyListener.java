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


import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.custom.*;
import junit.framework.*;
import junit.textui.*;

/**
 * Automated Test Suite for class org.eclipse.swt.custom.ExtendedModifyListener
 *
 * @see org.eclipse.swt.custom.ExtendedModifyListener
 */
public class Test_org_eclipse_swt_custom_ExtendedModifyListener extends SwtTestCase {
	Shell shell;
	StyledText styledText;
	int verify = -1;

public Test_org_eclipse_swt_custom_ExtendedModifyListener(String name) {
	super(name);
}

public static void main(String[] args) {
	TestRunner.run(suite());
}
protected void setUp() {
	super.setUp();
	shell = new Shell();
	styledText = new StyledText(shell, SWT.NULL);
	shell.open();
}

public void test_modifyTextLorg_eclipse_swt_custom_ExtendedModifyEvent() {
	ExtendedModifyListener listener = new ExtendedModifyListener() {
		public void modifyText(ExtendedModifyEvent event) {
			switch(verify) {
				case 1 : {
					assertTrue(":1a:", event.start == 0);
					assertTrue(":1b:", event.length == 1);
					assertTrue(":1c:", event.replacedText.equals(""));
					break;
				}
				case 2 : {
					assertTrue(":2a:", event.start == 0);
					assertTrue(":2b:", event.length == 1);
					assertTrue(":2c:", event.replacedText.equals("\n\n"));
					break;
				}
				case 3 : {
					assertTrue(":3a:", event.start == 0);
					assertTrue(":3b:", event.length == 2);
					assertTrue(":3c:", event.replacedText.equals("a"));
					break;
				}
				case 4: {
					assertTrue(":4:", false);
					break;
				}
				case 5 : {
					assertTrue(":5a:", event.start == 0);
					assertTrue(":5b:", event.length == 1);
					assertTrue(":5c:", event.replacedText.equals(""));
					break;
				}
				case 6 : {
					assertTrue(":6a:", event.start == 21);
					assertTrue(":6b:", event.length == 0);
					assertTrue(":6c:", event.replacedText.equals("\nline 4"));
					break;
				}
				case 7 : {
					assertTrue(":7a:", event.start == 5);
					assertTrue(":7b:", event.length == 0);
					assertTrue(":7c:", event.replacedText.equals("is a test"));
					break;
				}
				case 8 : {
					assertTrue(":8a:", event.start == 7);
					assertTrue(":8b:", event.length == 0);
					assertTrue(":8c:", event.replacedText.equals("\r\n"));
					break;
				}
				case 9 : {
					assertTrue(":9a:", event.start == 2);
					assertTrue(":9b:", event.length == 4);
					assertTrue(":9c:", event.replacedText.equals("\r\n"));
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
					assertTrue(":12a:", event.start == 3);
					assertTrue(":12b:", event.length == 1);
					assertTrue(":12c:", event.replacedText.equals(""));
					break;
				}
				case 13: {
					assertTrue(":13a:", event.start == 2);
					assertTrue(":13b:", event.length == 1);
					assertTrue(":13c:", event.replacedText.equals(""));
					break;
				}
				case 14: {
					assertTrue(":14:", false);
					break;
				}
				case 15: {
					assertTrue(":15a:", event.start == 2);
					assertTrue(":15b:", event.length == 6);
					assertTrue(":15c:", event.replacedText.equals("\r\n"));
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
					assertTrue(":18a:", event.start == 3);
					assertTrue(":18b:", event.length == 7);
					assertTrue(":18c:", event.replacedText.equals(""));
					break;
				}
				case 19: {
					assertTrue(":19a:", event.start == 2);
					assertTrue(":19b:", event.length == 7);
					assertTrue(":19c:", event.replacedText.equals(""));
					break;
				}
				case 20: {
					assertTrue(":20:", false);
					break;
				}
				case 21: {
					assertTrue(":21a:", event.start == 0);
					assertTrue(":21b:", event.length == 16);
					assertTrue(":21c:", event.replacedText.equals("L1\r\nL2\r\nL3\r\nL4\r\n"));
					break;
				}
			}
		}
	};
	styledText.addExtendedModifyListener(listener);
	
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

	verify = 21;
	styledText.setText("L1\r\nL2\r\nL3\r\nL4\r\n");
	styledText.removeExtendedModifyListener(listener);
}

public static Test suite() {
	TestSuite suite = new TestSuite();
	java.util.Vector methodNames = methodNames();
	java.util.Enumeration e = methodNames.elements();
	while (e.hasMoreElements()) {
		suite.addTest(new Test_org_eclipse_swt_custom_ExtendedModifyListener((String)e.nextElement()));
	}
	return suite;
}
public static java.util.Vector methodNames() {
	java.util.Vector methodNames = new java.util.Vector();
	methodNames.addElement("test_modifyTextLorg_eclipse_swt_custom_ExtendedModifyEvent");
	return methodNames;
}
protected void runTest() throws Throwable {
	if (getName().equals("test_modifyTextLorg_eclipse_swt_custom_ExtendedModifyEvent")) test_modifyTextLorg_eclipse_swt_custom_ExtendedModifyEvent();
}
}
