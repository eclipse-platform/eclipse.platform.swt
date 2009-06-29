/*******************************************************************************
 * Copyright (c) 2000, 2007 IBM Corporation and others.
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
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

/**
 * Automated Test Suite for class org.eclipse.swt.widgets.Text
 *
 * @see org.eclipse.swt.widgets.Text
 */
public class Test_org_eclipse_swt_widgets_Text extends Test_org_eclipse_swt_widgets_Scrollable {

public Test_org_eclipse_swt_widgets_Text(String name) {
	super(name);
}

public static void main(String[] args) {
	TestRunner.run(suite());
}

protected void setUp() {
	super.setUp();
	shell.pack();
	shell.open();
	makeCleanEnvironment(false); // use multi-line by default
}

public void test_ConstructorLorg_eclipse_swt_widgets_CompositeI() {
	try {
		text = new Text(null, 0);
		fail("No exception thrown for parent == null");
	}
	catch (IllegalArgumentException e) {
	}

	int[] cases = {0, SWT.SINGLE, SWT.MULTI, SWT.MULTI | SWT.V_SCROLL, SWT.MULTI | SWT.H_SCROLL, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL, 
					SWT.WRAP};
	for (int i = 0; i < cases.length; i++)
		text = new Text(shell, cases[i]);
}

public void test_addModifyListenerLorg_eclipse_swt_events_ModifyListener() {
	boolean exceptionThrown = false;
	ModifyListener listener = new ModifyListener() {
		public void modifyText(ModifyEvent event) {
			listenerCalled = true;
		}
	};
	try {
		text.addModifyListener(null);
	}
	catch (IllegalArgumentException e) {
		exceptionThrown = true;
	}
	assertTrue("Expected exception not thrown", exceptionThrown);
	exceptionThrown = false;
	
	// test whether all content modifying API methods send a Modify event	
	text.addModifyListener(listener);
	listenerCalled = false;
	text.setText("new text");	
	assertTrue("setText does not send event", listenerCalled);

	listenerCalled = false;	
	text.removeModifyListener(listener);
	// cause to call the listener. 
	text.setText("line");	
	assertTrue("Listener not removed", listenerCalled == false);
	try {
		text.removeModifyListener(null);
	}
	catch (IllegalArgumentException e) {
		exceptionThrown = true;
	}
	assertTrue("Expected exception not thrown", exceptionThrown);
}

public void test_addSelectionListenerLorg_eclipse_swt_events_SelectionListener() {
	boolean exceptionThrown = false;
	listenerCalled = false;
	SelectionListener listener = new SelectionListener() {
		public void widgetSelected(SelectionEvent event) {
			listenerCalled = true;
		}
		public void widgetDefaultSelected(SelectionEvent event) {
		}
	};
	try {
		text.addSelectionListener(null);
	}
	catch (IllegalArgumentException e) {
		exceptionThrown = true;
	}
	assertTrue("Expected exception not thrown", exceptionThrown);
	exceptionThrown = false;
	text.addSelectionListener(listener);
	text.setText("12345");
	text.setSelection(1,3);
	assertTrue(":a:", listenerCalled == false);
	text.removeSelectionListener(listener);
	try {
		text.removeSelectionListener(null);
	}
	catch (IllegalArgumentException e) {
		exceptionThrown = true;
	}
	assertTrue("Expected exception not thrown", exceptionThrown);
}

public void test_addVerifyListenerLorg_eclipse_swt_events_VerifyListener() {
	final String line = "Line1";
	final String newLine = "NewLine1";
	boolean exceptionThrown = false;
	text.setText("");
	
	// test null listener case
	try {
		text.addVerifyListener(null);
	}
	catch (IllegalArgumentException e) {
		exceptionThrown = true;
	}
	assertTrue("Expected exception not thrown", exceptionThrown);
	
	// test append case
	VerifyListener listener = new VerifyListener() {
		public void verifyText(VerifyEvent event) {
			listenerCalled = true;
			assertEquals("Verify event data invalid", 0, event.start);
			assertEquals("Verify event data invalid", 0, event.end);
			assertEquals("Verify event data invalid", line, event.text);
			event.text = newLine;
		}
	};	
	text.addVerifyListener(listener);
	listenerCalled = false;
	text.append(line);	
	assertTrue("append does not send event", listenerCalled);
	assertEquals("Listener failed", newLine, text.getText());
	text.removeVerifyListener(listener);

	// test insert case
	listener = new VerifyListener() {
		public void verifyText(VerifyEvent event) {
			listenerCalled = true;
			assertEquals("Verify event data invalid", 8, event.start);
			assertEquals("Verify event data invalid", 8, event.end);
			assertEquals("Verify event data invalid", line, event.text);
			event.text = newLine;
		}
	};	
	text.addVerifyListener(listener);
	listenerCalled = false;
	text.insert(line);	
	assertTrue("insert does not send event", listenerCalled);
	assertEquals("Listener failed", newLine + newLine, text.getText());
	text.removeVerifyListener(listener);

	// test setText case
	listener = new VerifyListener() {
		public void verifyText(VerifyEvent event) {
			listenerCalled = true;
			assertEquals("Verify event data invalid", 0, event.start);
			assertEquals("Verify event data invalid", 16, event.end);
			assertEquals("Verify event data invalid", line, event.text);
			event.text = newLine;
		}
	};	
	text.addVerifyListener(listener);
	text.setText(line);	
	assertTrue("setText does not send event", listenerCalled);
	assertEquals("Listener failed", newLine, text.getText());

	// test remove case
	listenerCalled = false;	
	text.removeVerifyListener(listener);
	text.setText(line);	
	assertTrue("Listener not removed", listenerCalled == false);
}

public void test_appendLjava_lang_String() {
	try {
		text.append(null);
		fail("No exception thrown for string == null");
	}
	catch (IllegalArgumentException e) {
	}

	text.setText("01");
	text.append("23");
	assertEquals("0123", text.getText());
	text.append("45");
	assertEquals("012345", text.getText());
	text.setSelection(0);
	text.append("67");
	assertEquals("01234567", text.getText());

	text.setText("01");
	text.append("23");
	assertEquals("0123", text.getText());
	text.append("4" + delimiterString+ "5");
	assertEquals("01234" + delimiterString +"5", text.getText());
	text.setSelection(0);
	text.append("67");
	assertEquals("01234" + delimiterString+"567", text.getText());

	// tests a SINGLE line text editor
	makeCleanEnvironment(true);
	
	try {
		text.append(null);
		fail("No exception thrown on string == null");
	}
	catch (IllegalArgumentException e) {
	}

	// tests a SINGLE line text editor
	makeCleanEnvironment(true);

	text.setText("01");
	text.append("23");
	assertEquals("0123", text.getText());
	text.append("45");
	assertEquals("012345", text.getText());
	text.setSelection(0);
	text.append("67");
	assertEquals("01234567", text.getText());

	// tests a SINGLE line text editor
	makeCleanEnvironment(true);

	text.setText("01");
	text.append("23");
	assertEquals("0123", text.getText());
	text.append("4" + delimiterString+"5");
	assertEquals("01234" + delimiterString+"5", text.getText());
	text.setSelection(0);
	text.append("67");
	assertEquals("01234" + delimiterString+"567", text.getText());
}

public void test_clearSelection() {
	text.setText("01234567890");
	assertEquals("", text.getSelectionText());
	text.selectAll();
	assertEquals("01234567890", text.getSelectionText());
	text.clearSelection();
	assertEquals("", text.getSelectionText());

	text.setText("01234567890");
	assertEquals("", text.getSelectionText());
	text.selectAll();
	assertEquals("01234567890", text.getSelectionText());
	text.clearSelection();
	assertEquals("", text.getSelectionText());
	
	// tests a SINGLE line text editor
	makeCleanEnvironment(true);
	
	text.setText("01234567890");
	assertEquals("", text.getSelectionText());
	text.selectAll();
	assertEquals("01234567890", text.getSelectionText());
	text.clearSelection();
	assertEquals("", text.getSelectionText());
}

public void test_computeSizeIIZ() {
	// super class test is sufficient
}

public void test_copy() {
	text.copy();

	text.selectAll();
	text.copy();
	assertEquals("", text.getSelectionText());

	text.setText("00000");
	text.selectAll();
	text.copy();
	text.setSelection(2);
	assertEquals("", text.getSelectionText());

	text.setText("");
	text.paste();
	assertEquals("00000", text.getText());
	
	// tests a SINGLE line text editor
	makeCleanEnvironment(true);
	
	text.copy();

	text.selectAll();
	text.copy();
	assertEquals("", text.getSelectionText());

	text.setText("00000");
	text.selectAll();
	text.copy();
	text.setSelection(2);
	assertEquals("", text.getSelectionText());

	text.setText("");
	text.paste();
	assertEquals("00000", text.getText());
}

public void test_cut() {
	text.cut();
	text.setText("01234567890");
	text.setSelection(2, 5);
	text.cut();
	assertEquals("01567890", text.getText());

	text.selectAll();
	text.cut();
	assertEquals("", text.getText());
	
	// tests a SINGLE line text editor
	makeCleanEnvironment(true);
	
	text.cut();

	text.setText("01234567890");
	text.setSelection(2, 5);
	text.cut();
	assertEquals("01567890", text.getText());

	text.selectAll();
	text.cut();
	assertEquals("", text.getText());
}

public void test_getCaretLineNumber() {
	text.setBounds(0, 0, 500, 500);
	assertTrue(":a:", text.getCaretLineNumber() == 0);
	text.setText("Line0\r\n");
	assertTrue(":b:", text.getCaretLineNumber() == 0);
	text.setTopIndex(1);
	assertTrue(":c:", text.getCaretLineNumber() == 0);

	text.append("Line1");
	assertTrue(":d:", text.getCaretLineNumber() == 1);
	String newText = "Line-1\r\n";
	text.setSelection(0,0);
	text.insert(newText);
	assertTrue(":e:", text.getCaretLineNumber() == 1);

	text.setSelection(0,0);
	assertTrue(":f:", text.getCaretLineNumber() == 0);
	text.setSelection(8,8);
	assertTrue(":g:", text.getCaretLineNumber() == 1);
}

public void test_getCaretLocation() {
	// account for insets when asserting
	text.setSize(200,50);
	text.setSelection(0,0);
	text.insert("");
	assertTrue(":a:", text.getCaretLocation().x >= 0);
	assertTrue(":a:", text.getCaretLocation().y >= 0);
	text.setText("Line0\r\nLine1\r\nLine2");
	text.insert("");
	assertTrue(":b:", text.getCaretLocation().x >= 0);
	assertTrue(":b:", text.getCaretLocation().y >= 0);
	text.setSelection(1,1);
	assertTrue(":c:", text.getCaretLocation().x > 0);
	assertTrue(":c:", text.getCaretLocation().y >= 0);
}

public void test_getCaretPosition() {
	text.setText("Line");
	assertTrue(":a:", text.getCaretPosition() == 0);
	text.append("123");
	assertTrue(":b:", text.getCaretPosition() == 7);
	text.setSelection(1,3);
	text.insert("123");
	assertTrue(":b:", text.getCaretPosition() == 4);
}

public void test_getCharCount() {
	assertEquals(0, text.getCharCount());
	text.setText("");
	assertEquals(0, text.getCharCount());
	text.setText("01234567890");
	assertEquals(11, text.getCharCount());

	text.setText("012345" + delimiterString+"67890");
	assertEquals(11  + delimiterString.length(), text.getCharCount()); //might be bogus on UNIX

	text.setText("");
	assertEquals(0, text.getCharCount());
	
	text.setText("01234\t567890");
	assertEquals(12, text.getCharCount());

	//
	text.setText("");
	assertEquals(0, text.getCharCount());
	text.setText("01234567890");
	assertEquals(11, text.getCharCount());

	text.setText("012345" + delimiterString+"67890");
	assertEquals(11  + delimiterString.length(), text.getCharCount()); //might be bogus on UNIX

	text.setText("");
	assertEquals(0, text.getCharCount());
	
	text.setText("01234\t567890");
	assertEquals(12, text.getCharCount());
	
	// tests a SINGLE line text editor
	makeCleanEnvironment(true);
	
	assertEquals(0, text.getCharCount());
	text.setText("");
	assertEquals(0, text.getCharCount());
	text.setText("01234567890");
	assertEquals(11, text.getCharCount());
	if (fCheckBogusTestCases) {
		text.setText("012345"+ delimiterString + "67890");
		assertEquals(11  + delimiterString.length(), text.getCharCount()); //might be bogus on UNIX
	}
	text.setText("");
	assertEquals(0, text.getCharCount());
	if (!SwtJunit.isAIX) {	
		text.setText("01234\t567890");
		assertEquals(12, text.getCharCount());
	}
}

public void test_getDoubleClickEnabled() {
	text.setDoubleClickEnabled(true);
	assertTrue(text.getDoubleClickEnabled());
	
	text.setDoubleClickEnabled(false);
	assertEquals(false, text.getDoubleClickEnabled());
	
	// this method tests a SINGLE line text editor
	makeCleanEnvironment(true);
	
	text.setDoubleClickEnabled(true);
	assertTrue(text.getDoubleClickEnabled());
	
	text.setDoubleClickEnabled(false);
	assertEquals(false, text.getDoubleClickEnabled());
}

public void test_getEchoChar() {
	// tests a SINGLE line text editor
	makeCleanEnvironment(true);
	
	text.setEchoChar('a');
	assertEquals('a', text.getEchoChar());
}

public void test_getEditable() {
	assertTrue(":a:", text.getEditable() == true);
	text.setEditable(true);
	assertTrue(":b:", text.getEditable() == true);
	text.setEditable(false);
	assertTrue(":c:", text.getEditable() == false);
	text.setEditable(false);
	assertTrue(":d:", text.getEditable() == false);
	text.setEditable(true);
	assertTrue(":e:", text.getEditable() == true);
}

public void test_getLineCount() {
	text.setBounds(0, 0, 500, 500);
	assertEquals(1, text.getLineCount());
	text.append("dddasd" + delimiterString);
	assertEquals(2, text.getLineCount());
	text.append("ddasdasdasdasd" + delimiterString);
	assertEquals(3, text.getLineCount());
	
	// tests a SINGLE line text editor
	makeCleanEnvironment(true);
	
	assertEquals(1, text.getLineCount());
	text.append("dddasd" + delimiterString);
	assertEquals(1, text.getLineCount());
	text.append("ddasdasdasdasd" + delimiterString);
	assertEquals(1, text.getLineCount());
}

public void test_getLineDelimiter() {
	String platform = SWT.getPlatform();
	String delimiter = text.getLineDelimiter();
	if (platform.equals("win32")) {
		assertTrue(":a:", delimiter.equals("\r\n"));
	} else if (platform.equals("motif")) {
		assertTrue(":a:", delimiter.equals("\n"));
	}
}

public void test_getLineHeight() {
	assertTrue(":a:", text.getLineHeight() > 0);
}

public void test_getOrientation() {
	// tested in setOrientation
}

public void test_getSelection() {
	text.setText("01234567890");
	text.setSelection(new Point(2, 2));
	assertTrue(":b:", text.getSelection().equals(new Point(2, 2)));
	text.setSelection(new Point(2, 3));
	assertTrue(":c:", text.getSelection().equals(new Point(2, 3)));
	text.setSelection(new Point(3, 11));
	assertTrue(":d:", text.getSelection().equals(new Point(3, 11)));
	text.setText("01234567890");
	text.setSelection(4);
	assertTrue(":a:", text.getSelection().equals(new Point(4, 4)));
	text.setSelection(11);
	assertTrue(":b:", text.getSelection().equals(new Point(11, 11)));
	text.setSelection(new Point(3, 2));	
	assertTrue(":c:", text.getSelection().equals(new Point(2, 3)));	
}

public void test_getSelectionCount() {
	text.setText("01234567890");
	assertTrue(":a:", text.getSelectionCount()==0);
	text.setSelection(2, 4);
	assertTrue(":b:", text.getSelectionCount()==2);
	text.setSelection(2, 11);
	assertTrue(":c:", text.getSelectionCount()==9);
	text.setText("0123\n4567890");
	assertTrue(":d:", text.getSelectionCount()==0);
	text.setSelection(2, 4);
	assertTrue(":e:", text.getSelectionCount()==2);
	text.setSelection(2, 12);
	assertTrue(":f:", text.getSelectionCount()==10);
}

public void test_getSelectionText() {
	text.setText("01234567890");
	assertEquals("", text.getSelectionText());
	text.setSelection(3, 7);
	assertEquals("3456", text.getSelectionText());

	text.selectAll();
	assertEquals("01234567890", text.getSelectionText());
}

public void test_getTabs() {
	if (SWT.getPlatform().equals("win32")) {
		// API not supported on all platforms (e.g., Motif)
		text.setTabs(1);
		assertTrue(":a:", text.getTabs() == 1);
		text.setTabs(8);
		assertTrue(":b:", text.getTabs() == 8);
		text.setText("Line\t1\r\n");
		assertTrue(":c:", text.getTabs() == 8);
		text.setTabs(7);
		assertTrue(":d:", text.getTabs() == 7);
	}
}

public void test_getText() {
	assertEquals("", text.getText());
	text.setText("01234567890");
	assertEquals("01234567890", text.getText());
	text.setText("");
	assertEquals("", text.getText());
	String string = "012345" + delimiterString + "67890";
	text.setText(string);
	assertEquals(string, text.getText());
	
	// tests a SINGLE line text editor
	makeCleanEnvironment(true);
	assertEquals("", text.getText());
	text.setText("01234567890");
	assertEquals("01234567890", text.getText());
	text.setText("");
	assertEquals("", text.getText());
	
	// tests a SINGLE line text editor with border
	makeCleanEnvironment(true, true);
	assertEquals("", text.getText());
	text.setText("01234567890");
	assertEquals("01234567890", text.getText());
	text.setText("");
	assertEquals("", text.getText());
}

public void test_getTextII() {
	assertEquals("", text.getText());
	assertEquals("", text.getText(-4,-4));
	assertEquals("", text.getText(-4,-2));
	assertEquals("", text.getText(-2,-1));
	assertEquals("", text.getText(-1,-1));
	assertEquals("", text.getText(-1,0));
	assertEquals("", text.getText(0,0));
	assertEquals("", text.getText(0,1));
	assertEquals("", text.getText(10,20));
	
	text.setText("a");
	assertEquals("", text.getText(-4,-4));
	assertEquals("", text.getText(-4,-2));
	assertEquals("", text.getText(-2,-1));
	assertEquals("", text.getText(-1,-1));
	assertEquals("a", text.getText(-1,0));
	assertEquals("a", text.getText(0,0));
	assertEquals("a", text.getText(0,1));
	assertEquals("", text.getText(10,20));
	
	text.setText("01234567890");
	assertEquals("345", text.getText(3, 5));
	assertEquals("012", text.getText(-1, 2));
	assertEquals("34567890", text.getText(3, 100));
	assertEquals("", text.getText(5, 3));
	assertEquals("0", text.getText(10,20));
	
	text.setText("");
	text.setEchoChar('*');
	
	assertEquals("", text.getText());
	assertEquals("", text.getText(-4,-4));
	assertEquals("", text.getText(-4,-2));
	assertEquals("", text.getText(-2,-1));
	assertEquals("", text.getText(-1,-1));
	assertEquals("", text.getText(-1,0));
	assertEquals("", text.getText(0,0));
	assertEquals("", text.getText(0,1));
	assertEquals("", text.getText(10,20));
	
	text.setText("a");
	assertEquals("", text.getText(-4,-4));
	assertEquals("", text.getText(-4,-2));
	assertEquals("", text.getText(-2,-1));
	assertEquals("", text.getText(-1,-1));
	assertEquals("a", text.getText(-1,0));
	assertEquals("a", text.getText(0,0));
	assertEquals("a", text.getText(0,1));
	assertEquals("", text.getText(10,20));
	
	text.setText("01234567890");
	assertEquals("345", text.getText(3, 5));
	assertEquals("012", text.getText(-1, 2));
	assertEquals("34567890", text.getText(3, 100));
	assertEquals("", text.getText(5, 3));
	assertEquals("0", text.getText(10,20));
	
	// tests a SINGLE line text editor
	makeCleanEnvironment(true);
	assertEquals("", text.getText());
	assertEquals("", text.getText(-4,-4));
	assertEquals("", text.getText(-4,-2));
	assertEquals("", text.getText(-2,-1));
	assertEquals("", text.getText(-1,-1));
	assertEquals("", text.getText(-1,0));
	assertEquals("", text.getText(0,0));
	assertEquals("", text.getText(0,1));
	assertEquals("", text.getText(10,20));
	
	text.setText("a");
	assertEquals("", text.getText(-4,-4));
	assertEquals("", text.getText(-4,-2));
	assertEquals("", text.getText(-2,-1));
	assertEquals("", text.getText(-1,-1));
	assertEquals("a", text.getText(-1,0));
	assertEquals("a", text.getText(0,0));
	assertEquals("a", text.getText(0,1));
	assertEquals("", text.getText(10,20));
	
	text.setText("01234567890");
	assertEquals("345", text.getText(3, 5));
	assertEquals("012", text.getText(-1, 2));
	assertEquals("34567890", text.getText(3, 100));
	assertEquals("", text.getText(5, 3));
	assertEquals("0", text.getText(10,20));
	
	text.setText("");
	text.setEchoChar('*');
	
	assertEquals("", text.getText());
	assertEquals("", text.getText(-4,-4));
	assertEquals("", text.getText(-4,-2));
	assertEquals("", text.getText(-2,-1));
	assertEquals("", text.getText(-1,-1));
	assertEquals("", text.getText(-1,0));
	assertEquals("", text.getText(0,0));
	assertEquals("", text.getText(0,1));
	assertEquals("", text.getText(10,20));
	
	text.setText("a");
	assertEquals("", text.getText(-4,-4));
	assertEquals("", text.getText(-4,-2));
	assertEquals("", text.getText(-2,-1));
	assertEquals("", text.getText(-1,-1));
	assertEquals("a", text.getText(-1,0));
	assertEquals("a", text.getText(0,0));
	assertEquals("a", text.getText(0,1));
	assertEquals("", text.getText(10,20));
	
	text.setText("01234567890");
	assertEquals("345", text.getText(3, 5));
	assertEquals("012", text.getText(-1, 2));
	assertEquals("34567890", text.getText(3, 100));
	assertEquals("", text.getText(5, 3));
	assertEquals("0", text.getText(10,20));
	
	// tests a SINGLE line text editor
	makeCleanEnvironment(true, true);
	assertEquals("", text.getText());
	assertEquals("", text.getText(-4,-4));
	assertEquals("", text.getText(-4,-2));
	assertEquals("", text.getText(-2,-1));
	assertEquals("", text.getText(-1,-1));
	assertEquals("", text.getText(-1,0));
	assertEquals("", text.getText(0,0));
	assertEquals("", text.getText(0,1));
	assertEquals("", text.getText(10,20));
	
	text.setText("a");
	assertEquals("", text.getText(-4,-4));
	assertEquals("", text.getText(-4,-2));
	assertEquals("", text.getText(-2,-1));
	assertEquals("", text.getText(-1,-1));
	assertEquals("a", text.getText(-1,0));
	assertEquals("a", text.getText(0,0));
	assertEquals("a", text.getText(0,1));
	assertEquals("", text.getText(10,20));
	
	text.setText("01234567890");
	assertEquals("345", text.getText(3, 5));
	assertEquals("012", text.getText(-1, 2));
	assertEquals("34567890", text.getText(3, 100));
	assertEquals("", text.getText(5, 3));
	assertEquals("0", text.getText(10,20));
	
	text.setText("");
	text.setEchoChar('*');
	
	assertEquals("", text.getText());
	assertEquals("", text.getText(-4,-4));
	assertEquals("", text.getText(-4,-2));
	assertEquals("", text.getText(-2,-1));
	assertEquals("", text.getText(-1,-1));
	assertEquals("", text.getText(-1,0));
	assertEquals("", text.getText(0,0));
	assertEquals("", text.getText(0,1));
	assertEquals("", text.getText(10,20));
	
	text.setText("a");
	assertEquals("", text.getText(-4,-4));
	assertEquals("", text.getText(-4,-2));
	assertEquals("", text.getText(-2,-1));
	assertEquals("", text.getText(-1,-1));
	assertEquals("a", text.getText(-1,0));
	assertEquals("a", text.getText(0,0));
	assertEquals("a", text.getText(0,1));
	assertEquals("", text.getText(10,20));
	
	text.setText("01234567890");
	assertEquals("345", text.getText(3, 5));
	assertEquals("012", text.getText(-1, 2));
	assertEquals("34567890", text.getText(3, 100));
	assertEquals("", text.getText(5, 3));
	assertEquals("0", text.getText(10,20));
}

public void test_getTextLimit() {
	text.setTextLimit(10);
	assertTrue(":a:", text.getTextLimit() == 10);
}

public void test_getTopIndex() {
	text.setSize(50,text.getLineHeight() * 2);
	text.setTopIndex(0);
	assertEquals(0, text.getTopIndex());
	text.append(delimiterString +"0123456789");
	text.setTopIndex(1);
	assertEquals(1, text.getTopIndex());
	text.setTopIndex(17);
	assertEquals(1, text.getTopIndex());
}

public void test_getTopPixel() {
	text.setText("Line0\r\nLine0a\r\n");

	assertTrue(":a:", text.getTopPixel() == 0);
	text.setTopIndex(-2);
	assertTrue(":b:", text.getTopPixel() == 0);
	text.setTopIndex(-1);
	assertTrue(":c:", text.getTopPixel() == 0);
	text.setTopIndex(1);
	assertTrue(":d:", text.getTopPixel() == text.getLineHeight());
	text.setSize(10, text.getLineHeight());
	text.setTopIndex(2);
	assertTrue(":e:", text.getTopPixel() == text.getLineHeight() * 2);
	text.setTopIndex(0);
	assertTrue(":f:", text.getTopPixel() == 0);
	text.setTopIndex(3);
	assertTrue(":g:", text.getTopPixel() == text.getLineHeight() * 2);
}

public void test_insertLjava_lang_String() {
	text.setBounds(0, 0, 500, 500);
	try {
		text.insert(null);
		fail("No exception thrown for string == null");
	}
	catch (IllegalArgumentException e) {
	}

	assertEquals("", text.getText());
	text.insert("");
	assertEquals("", text.getText());
	text.insert("fred");
	assertEquals("fred", text.getText());
	text.setSelection(2);
	text.insert("helmut");
	assertEquals("frhelmuted", text.getText());

	text.setText("01234567890");
	text.setSelection(4);
	assertEquals(1, text.getLineCount());
	text.insert(delimiterString);
	assertEquals(2, text.getLineCount());

	// tests a SINGLE line text editor
	makeCleanEnvironment(true);
	
	try {
		text.insert(null);
		fail("No exception thrown on string == null");
	}
	catch (IllegalArgumentException e) {
	}

	// tests a SINGLE line text editor
	makeCleanEnvironment(true);
	
	assertEquals("", text.getText());
	text.insert("");
	assertEquals("", text.getText());
	text.insert("fred");
	assertEquals("fred", text.getText());
	text.setSelection(2);
	text.insert("helmut");
	assertEquals("frhelmuted", text.getText());

	// tests a SINGLE line text editor
	makeCleanEnvironment(true);
	
	text.setText("01234567890");
	text.setSelection(4);
	assertEquals(1, text.getLineCount());
	text.insert(Text.DELIMITER);
	assertEquals(1, text.getLineCount());

	// tests a SINGLE line text editor
	makeCleanEnvironment(true);
	
	try {
		text.insert(null);
		fail("No exception thrown on string == null");
	}
	catch (IllegalArgumentException e) {
	}
}

public void test_isVisible() {
	// overriding test_isVisible() from Control
	control.setVisible(true);
	assertTrue(control.isVisible());  

	control.setVisible(false);
	assertTrue(!control.isVisible());

	if (!SwtJunit.isAIX) {
		control.setVisible(true);
		shell.setVisible(true);
		assertTrue("Window should be visible", control.isVisible());
		shell.setVisible(false);
		assertTrue("Window should not be visible", !control.isVisible());
	}
}

public void test_paste() {
	text.setText("01234567890");
	text.setSelection(2, 4);
	assertEquals("01234567890", text.getText());
	text.copy();
	text.setSelection(0);
	text.paste();
	assertEquals("2301234567890", text.getText());
	text.copy();
	text.setSelection(3);
	text.paste();
	assertEquals("230231234567890", text.getText());

	text.setText("0" + delimiterString + "1");
	text.selectAll();
	text.copy();
	text.setSelection(0);
	text.paste();
	assertEquals("0" + delimiterString + "1" + "0" + delimiterString + "1", text.getText());
	
	// tests a SINGLE line text editor
	makeCleanEnvironment(true);
	
	text.setText("01234567890");
	text.setSelection(2, 4);
	assertEquals("01234567890", text.getText());
	text.copy();
	text.setSelection(0);
	text.paste();
	assertEquals("2301234567890", text.getText());
	text.copy();
	text.setSelection(3);
	text.paste();
	assertEquals("230231234567890", text.getText());

	// tests a SINGLE line text editor
	makeCleanEnvironment(true);
	
	text.setText("0" + delimiterString + "1");
	text.selectAll();
	text.copy();
	text.setSelection(0);
	text.paste();

	if (fCheckSWTPolicy)
		assertEquals("0" + delimiterString + "1" + "0" + delimiterString + "1", text.getText());
}

public void test_removeModifyListenerLorg_eclipse_swt_events_ModifyListener() {
	// tested in addModifyListener method
}

public void test_removeSelectionListenerLorg_eclipse_swt_events_SelectionListener() {
	// tested in addSelectionListener method
}

public void test_removeVerifyListenerLorg_eclipse_swt_events_VerifyListener() {
	// tested in addVerifyListener method
}

public void test_selectAll() {
	text.setText("01234567890");
	assertEquals("01234567890", text.getText());
	text.selectAll();
	assertEquals("01234567890", text.getSelectionText());
	text.cut();
	assertEquals("", text.getText());

	text.setText("01234" + delimiterString+"567890");
	assertEquals("01234" + delimiterString+"567890", text.getText());
	text.selectAll();
	assertEquals("01234" + delimiterString+"567890", text.getSelectionText());
	text.cut();
	assertEquals("", text.getText());
		
	// tests a SINGLE line text editor
	makeCleanEnvironment(true);

	text.setText("01234567890");
	assertEquals("01234567890", text.getText());
	text.selectAll();
	assertEquals("01234567890", text.getSelectionText());
	text.cut();
	assertEquals("", text.getText());

	// tests a SINGLE line text editor
	if (fCheckBogusTestCases) {
		text.setText("01234" + delimiterString+"567890");
		assertEquals("01234" + delimiterString+"567890", text.getText());
		text.selectAll();
		assertEquals("01234" + delimiterString+"567890", text.getSelectionText());
		text.cut();
		assertEquals("", text.getText());
	}
}

public void test_setDoubleClickEnabledZ() {
	text.setDoubleClickEnabled(true);
	assertTrue(text.getDoubleClickEnabled());
	
	text.setDoubleClickEnabled(false);
	assertEquals(false, text.getDoubleClickEnabled());
	
	// tests a SINGLE line text editor
	makeCleanEnvironment(true);
	
	text.setDoubleClickEnabled(true);
	assertTrue(text.getDoubleClickEnabled());
	
	text.setDoubleClickEnabled(false);
	assertEquals(false, text.getDoubleClickEnabled());
}

public void test_setEchoCharC() {
	// tests a SINGLE line text editor
	makeCleanEnvironment(true);
	
	for (int i=0; i<128; i++){
		text.setEchoChar((char) i);
		assertEquals((char)i, text.getEchoChar());
	}
	
	// tests a SINGLE line text editor
	makeCleanEnvironment(true);
	
	text.setEchoChar('a');
	assertEquals('a', text.getEchoChar());

	text.setEchoChar((char) 0);
	assertEquals((char)0, text.getEchoChar());

	text.setEchoChar('\n');
	assertEquals('\n', text.getEchoChar());

	for (int i=0; i<128; i++){
		text.setEchoChar((char) i);
		assertEquals((char)i, text.getEchoChar());
	}
}

public void test_setEditableZ() {
	text.setEditable(true);
	assertTrue(":a:", text.getEditable() == true);
	text.setEditable(false);
	assertTrue(":b:", text.getEditable() == false);
	text.setEditable(false);
	assertTrue(":c:", text.getEditable() == false);
	text.setEditable(true);
	assertTrue(":d:", text.getEditable() == true);
}

public void test_setFontLorg_eclipse_swt_graphics_Font() {
	FontData fontData = text.getFont().getFontData()[0];
	int lineHeight;
	Font font;
	
	font = new Font(text.getDisplay(), fontData.getName(), 8, fontData.getStyle());
	text.setFont(font);
	lineHeight = text.getLineHeight();
	text.setFont(null);
	font.dispose();
	font = new Font(text.getDisplay(), fontData.getName(), 12, fontData.getStyle());
	text.setFont(font);
	assertTrue(":a:", text.getLineHeight() > lineHeight && font.equals(text.getFont()));
	text.setFont(null);
	font.dispose();
}

public void test_setOrientationI() {
	text.setOrientation(SWT.RIGHT_TO_LEFT);
	if ((text.getStyle() & SWT.MIRRORED) != 0) {
		assertTrue(":a:", text.getOrientation()==SWT.RIGHT_TO_LEFT);
	}
	text.setOrientation(SWT.LEFT_TO_RIGHT);
	assertTrue(":b:", text.getOrientation()==SWT.LEFT_TO_RIGHT);
}

public void test_setRedrawZ() {
	text.setRedraw(false);
	text.setRedraw(true);
}

public void test_setSelectionI() {
	text.setText("01234567890");
	assertEquals("", text.getSelectionText());
	text.setSelection(3, 7);
	assertEquals("3456", text.getSelectionText());
	text.setSelection(3, 0);
	assertEquals("012", text.getSelectionText());
}

public void test_setSelectionII() {
	text.setText("01234567890");
	assertEquals(0, text.getSelectionCount());
	text.setSelection(2, 4);
	assertEquals(2, text.getSelectionCount());
	text.setSelection(2, 100);
	assertEquals(9, text.getSelectionCount());

	text.setText("0123" + delimiterString +"4567890");
	assertEquals(0, text.getSelectionCount());
	text.setSelection(2, 4);
	assertEquals(2, text.getSelectionCount());
	text.setSelection(2, 100);
	assertEquals(9 + delimiterString.length(), text.getSelectionCount());
	
	// tests a SINGLE line text editor
	makeCleanEnvironment(true);
	
	text.setText("01234567890");
	assertEquals(0, text.getSelectionCount());
	text.setSelection(2, 4);
	assertEquals(2, text.getSelectionCount());
	text.setSelection(2, 100);
	assertEquals(9, text.getSelectionCount());

	// tests a SINGLE line text editor
	makeCleanEnvironment(true);
	
	text.setText("0123"+ delimiterString+"4567890");
	assertEquals(0, text.getSelectionCount());
	text.setSelection(2, 4);
	assertEquals(2, text.getSelectionCount());
	if (fCheckBogusTestCases) {
		text.setSelection(2, 100);
		assertEquals(9 +delimiterString.length(), text.getSelectionCount());
	}
}

public void test_setSelectionLorg_eclipse_swt_graphics_Point() {
	text.setText("dsdsdasdslaasdas");
	try {
		text.setSelection((Point) null);
		fail("No exception thrown for selection == null");
	}
	catch (IllegalArgumentException e) {
	}

	text.setText("01234567890");
	text.setSelection(new Point(2, 2));
	assertEquals(new Point(2, 2), text.getSelection());

	text.setSelection(new Point(3, 2));
	assertEquals(new Point(2, 3), text.getSelection());

	text.setSelection(new Point(3, 100));
	assertEquals(new Point(3, 11), text.getSelection());

	text.setText("01234567890");
	text.setSelection(4);
	assertEquals(new Point(4, 4), text.getSelection());

	text.setSelection(100);
	assertEquals(new Point(11, 11), text.getSelection());
	
	// tests a SINGLE line text editor
	makeCleanEnvironment(true);
	
	text.setText("dsdsdasdslaasdas");
	try {
		text.setSelection((Point) null);
		fail("No exception thrown for selection == null");
	}
	catch (IllegalArgumentException e) {
	}

	// tests a SINGLE line text editor
	makeCleanEnvironment(true);
	
	text.setText("01234567890");

	text.setSelection(new Point(2, 2));
	assertEquals(new Point(2, 2), text.getSelection());

	text.setSelection(new Point(3, 2));
	assertEquals(new Point(2, 3), text.getSelection());

	text.setSelection(new Point(3, 100));
	assertEquals(new Point(3, 11), text.getSelection());

	// tests a SINGLE line text editor
	makeCleanEnvironment(true);
	
	text.setText("01234567890");
	text.setSelection(4);
	assertEquals(new Point(4, 4), text.getSelection());

	text.setSelection(100);
	assertEquals(new Point(11, 11), text.getSelection());
}

public void test_setTabsI() {
	if (SwtJunit.isMotif) {
		for (int i = 0; i < 200; i++) {
			text.setTabs(i);
			assertEquals(8, text.getTabs());
		}
	} else {
		for (int i = 0; i < 200; i++) {
			text.setTabs(i);
			assertEquals(i, text.getTabs());
		}
	}
}

public void test_setTextLimitI() {
	boolean exceptionThrown = false;
	
	text.setTextLimit(10);
	assertTrue(":a:", text.getTextLimit() == 10);

	text.setTextLimit(Text.LIMIT);
	assertTrue(":b:", text.getTextLimit() == Text.LIMIT);

	try {
		text.setTextLimit(0);
	}
	catch (IllegalArgumentException e) {
		exceptionThrown = true;
	}
	assertTrue(":c:", exceptionThrown);
}

public void test_setTextLjava_lang_String() {
	try {
		text.setText(null);
		fail("No exception thrown for string == null");
	}
	catch (IllegalArgumentException e) {
	}

	text.setText("");
	
	// tests a SINGLE line text editor
	makeCleanEnvironment(true);
	
	assertEquals("", text.getText());
	text.setText("01234567890");
	assertEquals("01234567890", text.getText());
	text.setText("");
	assertEquals("", text.getText());
	if (fCheckBogusTestCases) {
		text.setText("012345" + delimiterString+ "67890");
		assertEquals("012345" + delimiterString +"67890", text.getText());
	}

	// tests a SINGLE line text editor
	makeCleanEnvironment(true);
	
	assertEquals("", text.getText());

	text.setText("01234567890");
	assertEquals("345", text.getText(3, 5));
	assertEquals("012", text.getText(-1, 2));
	assertEquals("34567890", text.getText(3, 100));
	assertEquals("", text.getText(5, 3));
	
	text.setText("");
	assertEquals("", text.getText(-1, 0));
	assertEquals("", text.getText(0, 10));
	assertEquals("", text.getText(1, 0));
}

public void test_setTopIndexI() {
	int number = 100;
	for (int i = 0; i < number; i++) {
		text.append("01234\n");
	}
	for (int i = 1; i < number; i++) {
		text.setTopIndex(i);
		assertEquals(i, text.getTopIndex());
	}
	
	text.setTopIndex(number+5);
	assertEquals(number, text.getTopIndex());
	
	// tests a SINGLE line text editor
	makeCleanEnvironment(true);
	
	text.setText("01234567890");
	text.append(Text.DELIMITER +"01234567890");
	text.setTopIndex(0);
	assertEquals(0, text.getTopIndex());
	text.setTopIndex(1);
	assertEquals(0, text.getTopIndex());
	text.setTopIndex(17);
	assertEquals(0, text.getTopIndex());

	text.setText("");	
	for (int i = 0; i < number; i++) {
		text.append("01234" + Text.DELIMITER);
	}
	for (int i = 0; i < number; i++) {
		text.setTopIndex(i);
		assertEquals(0, text.getTopIndex());
	}
}

public void test_showSelection() {
	text.showSelection();

	text.selectAll();
	text.showSelection();

	text.setText("00000");
	text.selectAll();
	text.showSelection();
	text.clearSelection();
	text.showSelection();
	
	// this method tests a SINGLE line text editor
	makeCleanEnvironment(true);
	
	text.showSelection();

	text.selectAll();
	text.showSelection();


	text.setText("00000");
	text.selectAll();
	text.showSelection();

	text.clearSelection();
	text.showSelection();
}

public static Test suite() {
	TestSuite suite = new TestSuite();
	java.util.Vector methodNames = methodNames();
	java.util.Enumeration e = methodNames.elements();
	while (e.hasMoreElements()) {
		suite.addTest(new Test_org_eclipse_swt_widgets_Text((String)e.nextElement()));
	}
	return suite;
}
public static java.util.Vector methodNames() {
	java.util.Vector methodNames = new java.util.Vector();
	methodNames.addElement("test_ConstructorLorg_eclipse_swt_widgets_CompositeI");
	methodNames.addElement("test_addModifyListenerLorg_eclipse_swt_events_ModifyListener");
	methodNames.addElement("test_addSelectionListenerLorg_eclipse_swt_events_SelectionListener");
	methodNames.addElement("test_addVerifyListenerLorg_eclipse_swt_events_VerifyListener");
	methodNames.addElement("test_appendLjava_lang_String");
	methodNames.addElement("test_clearSelection");
	methodNames.addElement("test_computeSizeIIZ");
	methodNames.addElement("test_copy");
	methodNames.addElement("test_cut");
	methodNames.addElement("test_getCaretLineNumber");
	methodNames.addElement("test_getCaretLocation");
	methodNames.addElement("test_getCaretPosition");
	methodNames.addElement("test_getCharCount");
	methodNames.addElement("test_getDoubleClickEnabled");
	methodNames.addElement("test_getEchoChar");
	methodNames.addElement("test_getEditable");
	methodNames.addElement("test_getLineCount");
	methodNames.addElement("test_getLineDelimiter");
	methodNames.addElement("test_getLineHeight");
	methodNames.addElement("test_getOrientation");
	methodNames.addElement("test_getSelection");
	methodNames.addElement("test_getSelectionCount");
	methodNames.addElement("test_getSelectionText");
	methodNames.addElement("test_getTabs");
	methodNames.addElement("test_getText");
	methodNames.addElement("test_getTextII");
	methodNames.addElement("test_getTextLimit");
	methodNames.addElement("test_getTopIndex");
	methodNames.addElement("test_getTopPixel");
	methodNames.addElement("test_insertLjava_lang_String");
	methodNames.addElement("test_paste");
	methodNames.addElement("test_removeModifyListenerLorg_eclipse_swt_events_ModifyListener");
	methodNames.addElement("test_removeSelectionListenerLorg_eclipse_swt_events_SelectionListener");
	methodNames.addElement("test_removeVerifyListenerLorg_eclipse_swt_events_VerifyListener");
	methodNames.addElement("test_selectAll");
	methodNames.addElement("test_setDoubleClickEnabledZ");
	methodNames.addElement("test_setEchoCharC");
	methodNames.addElement("test_setEditableZ");
	methodNames.addElement("test_setFontLorg_eclipse_swt_graphics_Font");
	methodNames.addElement("test_setOrientationI");
	methodNames.addElement("test_setRedrawZ");
	methodNames.addElement("test_setSelectionI");
	methodNames.addElement("test_setSelectionII");
	methodNames.addElement("test_setSelectionLorg_eclipse_swt_graphics_Point");
	methodNames.addElement("test_setTabsI");
	methodNames.addElement("test_setTextLimitI");
	methodNames.addElement("test_setTextLjava_lang_String");
	methodNames.addElement("test_setTopIndexI");
	methodNames.addElement("test_showSelection");
	methodNames.addElement("test_consistency_EnterSelection");
	methodNames.addElement("test_consistency_Modify");
	methodNames.addElement("test_consistency_MenuDetect");
	methodNames.addElement("test_consistency_DragDetect");
	methodNames.addAll(Test_org_eclipse_swt_widgets_Scrollable.methodNames()); // add superclass method names
	return methodNames;
}
protected void runTest() throws Throwable {
	if (getName().equals("test_ConstructorLorg_eclipse_swt_widgets_CompositeI")) test_ConstructorLorg_eclipse_swt_widgets_CompositeI();
	else if (getName().equals("test_addModifyListenerLorg_eclipse_swt_events_ModifyListener")) test_addModifyListenerLorg_eclipse_swt_events_ModifyListener();
	else if (getName().equals("test_addSelectionListenerLorg_eclipse_swt_events_SelectionListener")) test_addSelectionListenerLorg_eclipse_swt_events_SelectionListener();
	else if (getName().equals("test_addVerifyListenerLorg_eclipse_swt_events_VerifyListener")) test_addVerifyListenerLorg_eclipse_swt_events_VerifyListener();
	else if (getName().equals("test_appendLjava_lang_String")) test_appendLjava_lang_String();
	else if (getName().equals("test_clearSelection")) test_clearSelection();
	else if (getName().equals("test_computeSizeIIZ")) test_computeSizeIIZ();
	else if (getName().equals("test_copy")) test_copy();
	else if (getName().equals("test_cut")) test_cut();
	else if (getName().equals("test_getCaretLineNumber")) test_getCaretLineNumber();
	else if (getName().equals("test_getCaretLocation")) test_getCaretLocation();
	else if (getName().equals("test_getCaretPosition")) test_getCaretPosition();
	else if (getName().equals("test_getCharCount")) test_getCharCount();
	else if (getName().equals("test_getDoubleClickEnabled")) test_getDoubleClickEnabled();
	else if (getName().equals("test_getEchoChar")) test_getEchoChar();
	else if (getName().equals("test_getEditable")) test_getEditable();
	else if (getName().equals("test_getLineCount")) test_getLineCount();
	else if (getName().equals("test_getLineDelimiter")) test_getLineDelimiter();
	else if (getName().equals("test_getLineHeight")) test_getLineHeight();
	else if (getName().equals("test_getOrientation")) test_getOrientation();
	else if (getName().equals("test_getSelection")) test_getSelection();
	else if (getName().equals("test_getSelectionCount")) test_getSelectionCount();
	else if (getName().equals("test_getSelectionText")) test_getSelectionText();
	else if (getName().equals("test_getTabs")) test_getTabs();
	else if (getName().equals("test_getText")) test_getText();
	else if (getName().equals("test_getTextII")) test_getTextII();
	else if (getName().equals("test_getTextLimit")) test_getTextLimit();
	else if (getName().equals("test_getTopIndex")) test_getTopIndex();
	else if (getName().equals("test_getTopPixel")) test_getTopPixel();
	else if (getName().equals("test_insertLjava_lang_String")) test_insertLjava_lang_String();
	else if (getName().equals("test_paste")) test_paste();
	else if (getName().equals("test_removeModifyListenerLorg_eclipse_swt_events_ModifyListener")) test_removeModifyListenerLorg_eclipse_swt_events_ModifyListener();
	else if (getName().equals("test_removeSelectionListenerLorg_eclipse_swt_events_SelectionListener")) test_removeSelectionListenerLorg_eclipse_swt_events_SelectionListener();
	else if (getName().equals("test_removeVerifyListenerLorg_eclipse_swt_events_VerifyListener")) test_removeVerifyListenerLorg_eclipse_swt_events_VerifyListener();
	else if (getName().equals("test_selectAll")) test_selectAll();
	else if (getName().equals("test_setDoubleClickEnabledZ")) test_setDoubleClickEnabledZ();
	else if (getName().equals("test_setEchoCharC")) test_setEchoCharC();
	else if (getName().equals("test_setEditableZ")) test_setEditableZ();
	else if (getName().equals("test_setFontLorg_eclipse_swt_graphics_Font")) test_setFontLorg_eclipse_swt_graphics_Font();
	else if (getName().equals("test_setOrientationI")) test_setOrientationI();
	else if (getName().equals("test_setRedrawZ")) test_setRedrawZ();
	else if (getName().equals("test_setSelectionI")) test_setSelectionI();
	else if (getName().equals("test_setSelectionII")) test_setSelectionII();
	else if (getName().equals("test_setSelectionLorg_eclipse_swt_graphics_Point")) test_setSelectionLorg_eclipse_swt_graphics_Point();
	else if (getName().equals("test_setTabsI")) test_setTabsI();
	else if (getName().equals("test_setTextLimitI")) test_setTextLimitI();
	else if (getName().equals("test_setTextLjava_lang_String")) test_setTextLjava_lang_String();
	else if (getName().equals("test_setTopIndexI")) test_setTopIndexI();
	else if (getName().equals("test_showSelection")) test_showSelection();
	else if (getName().equals("test_consistency_EnterSelection")) test_consistency_EnterSelection();
	else if (getName().equals("test_consistency_Modify")) test_consistency_Modify();
	else if (getName().equals("test_consistency_MenuDetect")) test_consistency_MenuDetect();
	else if (getName().equals("test_consistency_DragDetect")) test_consistency_DragDetect();
	else super.runTest();
}

/* custom */
Text text;
String delimiterString;

/**
 * Clean up the environment for a new test.
 * 
 * @param single true if the new text widget should be single-line.
 */

private void makeCleanEnvironment(boolean single) {
	makeCleanEnvironment (single, false);
}

private void makeCleanEnvironment(boolean single, boolean border) {
// this method must be private or protected so the auto-gen tool keeps it
	if ( text != null ) text.dispose();

	if ( single == true )
		text = new Text(shell, SWT.SINGLE | (border ? SWT.BORDER : SWT.NULL));	
	else
		text = new Text(shell, SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL | (border ? SWT.BORDER : SWT.NULL));
	setWidget(text);
	delimiterString = Text.DELIMITER;
}

protected void setWidget(Widget w) {
	text = (Text)w;
	super.setWidget(w);
}

public void test_consistency_EnterSelection () {
    makeCleanEnvironment(true);
    consistencyEvent(13, 10, 0, 0, ConsistencyUtility.KEY_PRESS);
}

public void test_consistency_Modify() {
    makeCleanEnvironment(true);
    consistencyEvent('a', 0, 0, 0, ConsistencyUtility.KEY_PRESS);
}

public void test_consistency_MenuDetect () {
    makeCleanEnvironment(true);
    consistencyEvent(10, 10, 3, ConsistencyUtility.ESCAPE_MENU, ConsistencyUtility.MOUSE_CLICK);
}

public void test_consistency_DragDetect () {
    makeCleanEnvironment(true);
    consistencyEvent(30, 10, 50, 0, ConsistencyUtility.MOUSE_DRAG);
}

}
