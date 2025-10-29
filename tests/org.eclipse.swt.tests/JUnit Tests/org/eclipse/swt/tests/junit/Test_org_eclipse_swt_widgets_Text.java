/*******************************************************************************
 * Copyright (c) 2000, 2025 IBM Corporation and others.
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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SegmentListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Widget;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * Automated Test Suite for class org.eclipse.swt.widgets.Text
 *
 * @see org.eclipse.swt.widgets.Text
 */
public class Test_org_eclipse_swt_widgets_Text extends Test_org_eclipse_swt_widgets_Scrollable {

@Override
@BeforeEach
public void setUp() {
	super.setUp();
	shell.pack();
	SwtTestUtil.openShell(shell);
	makeCleanEnvironment(false); // use multi-line by default
}

@Override
@Test
public void test_ConstructorLorg_eclipse_swt_widgets_CompositeI() {
	assertThrows(IllegalArgumentException.class, () ->	text = new Text(null, 0));

	int[] cases = {0, SWT.SINGLE, SWT.MULTI, SWT.MULTI | SWT.V_SCROLL, SWT.MULTI | SWT.H_SCROLL, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL,
					SWT.WRAP};
	for (int style : cases)
		text = new Text(shell, style);
}

@Test
public void test_addModifyListenerLorg_eclipse_swt_events_ModifyListener() {
	ModifyListener listener = event -> listenerCalled = true;
	assertThrows(IllegalArgumentException.class, ()-> text.addModifyListener(null));

	// test whether all content modifying API methods send a Modify event
	text.addModifyListener(listener);
	listenerCalled = false;
	text.setText("new text");
	assertTrue(listenerCalled);

	listenerCalled = false;
	text.removeModifyListener(listener);
	// cause to call the listener.
	text.setText("line");
	assertFalse(listenerCalled);
	assertThrows(IllegalArgumentException.class, ()-> text.removeModifyListener(null));
}

@Test
public void test_addSelectionListenerLorg_eclipse_swt_events_SelectionListener() {
	listenerCalled = false;
	SelectionListener listener = new SelectionListener() {
		@Override
		public void widgetSelected(SelectionEvent event) {
			listenerCalled = true;
		}
		@Override
		public void widgetDefaultSelected(SelectionEvent event) {
		}
	};

	assertThrows(IllegalArgumentException.class, () ->text.addSelectionListener(null));

	text.addSelectionListener(listener);
	text.setText("12345");
	text.setSelection(1,3);
	assertFalse(listenerCalled);
	text.removeSelectionListener(listener);

	assertThrows(IllegalArgumentException.class, () ->text.removeSelectionListener(null));
}

@Test
public void test_addSelectionListenerWidgetSelectedAdapterLorg_eclipse_swt_events_SelectionListener() {
	listenerCalled = false;
	SelectionListener listener = SelectionListener.widgetSelectedAdapter(e -> listenerCalled = true);

	text.addSelectionListener(listener);
	text.setText("12345");
	text.setSelection(1,3);
	assertFalse(listenerCalled);
	text.removeSelectionListener(listener);
}

@Test
public void test_addVerifyListenerLorg_eclipse_swt_events_VerifyListener() {
	final String line = "Line1";
	final String newLine = "NewLine1";
	text.setText("");

	// test null listener case
	assertThrows(IllegalArgumentException.class, ()->	text.addVerifyListener(null));

	// test append case
	VerifyListener listener = event -> {
		listenerCalled = true;
		assertEquals(0, event.start);
		assertEquals(0, event.end);
		assertEquals(line, event.text);
		event.text = newLine;
	};
	text.addVerifyListener(listener);
	listenerCalled = false;
	text.append(line);
	assertTrue(listenerCalled);
	assertEquals(newLine, text.getText());
	text.removeVerifyListener(listener);

	// test insert case
	listener = event -> {
		listenerCalled = true;
		assertEquals(8, event.start);
		assertEquals(8, event.end);
		assertEquals(line, event.text);
		event.text = newLine;
	};
	text.addVerifyListener(listener);
	listenerCalled = false;
	text.insert(line);
	assertTrue(listenerCalled);
	assertEquals(newLine + newLine, text.getText());
	text.removeVerifyListener(listener);

	// test setText case
	listener = event -> {
		listenerCalled = true;
		assertEquals(0, event.start);
		assertEquals(16, event.end);
		assertEquals(line, event.text);
		event.text = newLine;
	};
	text.addVerifyListener(listener);
	text.setText(line);
	assertTrue(listenerCalled);
	assertEquals(newLine, text.getText());

	// test remove case
	listenerCalled = false;
	text.removeVerifyListener(listener);
	text.setText(line);
	assertFalse(listenerCalled);
}

@Test
public void test_appendLjava_lang_String() {
	assertThrows(IllegalArgumentException.class, ()->text.append(null));

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

	assertThrows(IllegalArgumentException.class, ()->text.append(null));

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

@Test
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

@Override
@Test
public void test_computeSizeIIZ() {
	// super class test is sufficient
}

@Tag("clipboard")
@Test
public void test_copy() {
	if (SwtTestUtil.isCocoa) {
		// TODO Fix Cocoa failure.
		if (SwtTestUtil.verbose) {
			System.out
					.println("Excluded test_copy(org.eclipse.swt.tests.junit.Test_org_eclipse_swt_widgets_Text).");
		}
		return;
	}
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
	// Spin the event loop to let GTK process the clipboard + entry update
	Display display = text.getDisplay();
	while (display.readAndDispatch()) {
	    // loop until no more events
	}
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
	while (display.readAndDispatch()) {
	    // loop until no more events
	}
	assertEquals("00000", text.getText());
}

@Test
public void test_cut() {
	if (SwtTestUtil.isCocoa) {
		// TODO Fix Cocoa failure.
		if (SwtTestUtil.verbose) {
			System.out
					.println("Excluded test_cut(org.eclipse.swt.tests.junit.Test_org_eclipse_swt_widgets_Text).");
		}
		return;
	}
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

@Test
public void test_getCaretLineNumber() {
	if (SwtTestUtil.isCocoa) {
		//TODO Fix Cocoa failure.
		if (SwtTestUtil.verbose) {
			System.out.println("Excluded test_getCaretLineNumber(org.eclipse.swt.tests.junit.Test_org_eclipse_swt_widgets_Text)");
		}
		return;
	}
	text.setBounds(0, 0, 500, 500);
	assertEquals( 0, text.getCaretLineNumber());
	text.setText("Line0\r\n");
	assertEquals( 0, text.getCaretLineNumber());
	text.setTopIndex(1);
	assertEquals( 0, text.getCaretLineNumber());

	text.append("Line1");
	assertEquals( 1, text.getCaretLineNumber());
	String newText = "Line-1\r\n";
	text.setSelection(0,0);
	text.insert(newText);
	assertEquals(1, text.getCaretLineNumber());

	text.setSelection(0,0);
	assertEquals(0, text.getCaretLineNumber());
	text.setSelection(8,8);
	assertEquals(1, text.getCaretLineNumber());
}

@Test
public void test_getCaretLocation() {
	// account for insets when asserting
	text.setSize(200,50);
	text.setSelection(0,0);
	text.insert("");
	assertTrue(text.getCaretLocation().x >= 0);
	assertTrue(text.getCaretLocation().y >= 0);
	text.setText("Line0\r\nLine1\r\nLine2");
	text.insert("");
	assertTrue(text.getCaretLocation().x >= 0);
	assertTrue(text.getCaretLocation().y >= 0);
	text.setSelection(1,1);
	assertTrue(text.getCaretLocation().x > 0);
	assertTrue(text.getCaretLocation().y >= 0);
}

@Test
public void test_getCaretPosition() {
	text.setText("Line");
	assertEquals(0, text.getCaretPosition());
	text.append("123");
	assertEquals(7, text.getCaretPosition());
	text.setSelection(1,3);
	text.insert("123");
	assertEquals(4, text.getCaretPosition());
}

@Test
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
	if (SwtTestUtil.fCheckBogusTestCases) {
		text.setText("012345"+ delimiterString + "67890");
		assertEquals(11  + delimiterString.length(), text.getCharCount()); //might be bogus on UNIX
	}
	text.setText("");
	assertEquals(0, text.getCharCount());
	text.setText("01234\t567890");
	assertEquals(12, text.getCharCount());
}

@Test
public void test_getDoubleClickEnabled() {
	text.setDoubleClickEnabled(true);
	assertTrue(text.getDoubleClickEnabled());

	text.setDoubleClickEnabled(false);
	assertFalse(text.getDoubleClickEnabled());

	// this method tests a SINGLE line text editor
	makeCleanEnvironment(true);

	text.setDoubleClickEnabled(true);
	assertTrue(text.getDoubleClickEnabled());

	text.setDoubleClickEnabled(false);
	assertFalse(text.getDoubleClickEnabled());
}

@Test
public void test_getEchoChar() {
	// tests a SINGLE line text editor
	makeCleanEnvironment(true);

	text.setEchoChar('a');
	assertEquals('a', text.getEchoChar());
}

@Test
public void test_getEditable() {
	assertTrue(text.getEditable());
	text.setEditable(true);
	assertTrue(text.getEditable());
	text.setEditable(false);
	assertFalse(text.getEditable());
	text.setEditable(false);
	assertFalse(text.getEditable());
	text.setEditable(true);
	assertTrue(text.getEditable());
}

@Test
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

@Test
public void test_getLineDelimiter() {
	String platform = SWT.getPlatform();
	String delimiter = text.getLineDelimiter();
	switch(platform) {
		case "win32" -> assertEquals("\r\n", delimiter);
		case "cocoa" -> assertEquals ("\r", delimiter);
		default-> assertEquals("\n", delimiter);
	}
}

@Test
public void test_getLineHeight() {
	assertTrue(text.getLineHeight() > 0);
}

@Test
public void test_getSelection() {
	text.setText("01234567890");
	text.setSelection(new Point(2, 2));
	assertEquals(new Point(2, 2), text.getSelection());
	text.setSelection(new Point(2, 3));
	assertEquals(new Point(2, 3), text.getSelection());
	text.setSelection(new Point(3, 11));
	assertEquals(new Point(3, 11), text.getSelection());
	text.setText("01234567890");
	text.setSelection(4);
	assertEquals(new Point(4, 4), text.getSelection());
	text.setSelection(11);
	assertEquals(new Point(11, 11), text.getSelection());
	text.setSelection(new Point(3, 2));
	assertEquals(new Point(2, 3), text.getSelection());
}

@Test
public void test_getSelectionCount() {
	text.setText("01234567890");
	assertEquals(0, text.getSelectionCount());
	text.setSelection(2, 4);
	assertEquals(2, text.getSelectionCount());
	text.setSelection(2, 11);
	assertEquals(9, text.getSelectionCount());
	text.setText("0123\n4567890");
	assertEquals(0, text.getSelectionCount());
	text.setSelection(2, 4);
	assertEquals(2, text.getSelectionCount());
	text.setSelection(2, 12);
	assertEquals(10, text.getSelectionCount());
}

@Test
public void test_getSelectionText() {
	text.setText("01234567890");
	assertEquals("", text.getSelectionText());
	text.setSelection(3, 7);
	assertEquals("3456", text.getSelectionText());

	text.selectAll();
	assertEquals("01234567890", text.getSelectionText());
}

@Test
public void test_getTabs() {
	if (SWT.getPlatform().equals("win32") || SWT.getPlatform().equals("gtk")) {
		// API not supported on all platforms
		text.setTabs(1);
		assertEquals(1, text.getTabs());
		text.setTabs(8);
		assertEquals(8, text.getTabs());
		text.setText("Line\t1\r\n");
		assertEquals(8, text.getTabs());
		text.setTabs(7);
		assertEquals(7, text.getTabs());
	}
}

@Test
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

@Test
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

@Test
public void test_getTextLimit() {
	if (SwtTestUtil.isGTK) {
		//TODO Fix GTK failure.
		if (SwtTestUtil.verbose) {
			System.out.println("Excluded test_getTextLimit(org.eclipse.swt.tests.junit.Test_org_eclipse_swt_widgets_Text)");
		}
		return;
	}
	text.setTextLimit(10);
	assertEquals(10, text.getTextLimit());
}

@Test
public void test_getTopIndex() {
	if (SwtTestUtil.isCocoa) {
		//TODO Fix Cocoa failure.
		if (SwtTestUtil.verbose) {
			System.out.println("Excluded test_getTopIndex(org.eclipse.swt.tests.junit.Test_org_eclipse_swt_widgets_Text)");
		}
		return;
	}
	text.setSize(50,text.getLineHeight());
	text.setTopIndex(0);
	assertEquals(0, text.getTopIndex());
	text.append(delimiterString +"0123456789");
	text.setTopIndex(1);
	assertEquals(1, text.getTopIndex());
	text.setTopIndex(17);
	assertEquals(1, text.getTopIndex());
}

@Test
public void test_getTopPixel() {
	if (SwtTestUtil.isGTK || SwtTestUtil.isCocoa) {
		//TODO Fix GTK  and Cocoa failure.
		if (SwtTestUtil.verbose) {
			System.out.println("Excluded test_getTopPixel(org.eclipse.swt.tests.junit.Test_org_eclipse_swt_widgets_Text)");
		}
		return;
	}
	text.setText("Line0\r\nLine0a\r\n");

	assertEquals(0, text.getTopPixel());
	text.setTopIndex(-2);
	assertEquals(0, text.getTopPixel());
	text.setTopIndex(-1);
	assertEquals(0, text.getTopPixel());
	text.setTopIndex(1);
	assertEquals(text.getLineHeight(), text.getTopPixel());
	text.setSize(10, text.getLineHeight());
	text.setTopIndex(2);
	assertEquals(text.getLineHeight() * 2, text.getTopPixel());
	text.setTopIndex(0);
	assertEquals(0, text.getTopPixel());
	text.setTopIndex(3);
	assertEquals(text.getLineHeight() * 2, text.getTopPixel());
}

@Test
public void test_insertLjava_lang_String() {
	text.setBounds(0, 0, 500, 500);
	assertThrows(IllegalArgumentException.class, ()->text.insert(null));

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

	assertThrows(IllegalArgumentException.class, ()->text.insert(null));

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

	assertThrows(IllegalArgumentException.class, ()->text.insert(null));
}

@Override
@Test
public void test_isVisible() {
	// overriding test_isVisible() from Control
	control.setVisible(true);
	assertTrue(control.isVisible());

	control.setVisible(false);
	assertTrue(!control.isVisible());

	control.setVisible(true);
	shell.setVisible(true);
	assertTrue(control.isVisible());
	shell.setVisible(false);
	assertFalse(control.isVisible());
}

@Tag("clipboard")
@Test
public void test_paste() throws InterruptedException {
	if (SwtTestUtil.isCocoa) {
		// TODO Fix Cocoa failure.
		if (SwtTestUtil.verbose) {
			System.out
					.println("Excluded test_paste(org.eclipse.swt.tests.junit.Test_org_eclipse_swt_widgets_Text).");
		}
		return;
	}
	text.setText("01234567890");
	text.setSelection(2, 4);
	assertEquals("01234567890", text.getText());
	text.copy();
	SwtTestUtil.processEvents(100, null);
	text.setSelection(0);
	text.paste();
	SwtTestUtil.processEvents(1000, () -> "2301234567890".equals(text.getText()));
	assertEquals("2301234567890", text.getText());
	text.copy();
	SwtTestUtil.processEvents(100, null);
	text.setSelection(3);
	text.paste();
	SwtTestUtil.processEvents(1000, () -> "230231234567890".equals(text.getText()));
	assertEquals("230231234567890", text.getText());

	text.setText("0" + delimiterString + "1");
	text.selectAll();
	text.copy();
	text.setSelection(0);
	text.paste();
	String expected = "0" + delimiterString + "1" + "0" + delimiterString + "1";
	SwtTestUtil.processEvents(1000, () -> expected.equals(text.getText()));
	assertEquals(expected, text.getText());

	// tests a SINGLE line text editor
	makeCleanEnvironment(true);

	text.setText("01234567890");
	text.setSelection(2, 4);
	assertEquals("01234567890", text.getText());
	text.copy();
	text.setSelection(0);
	text.paste();
	SwtTestUtil.processEvents(1000, () -> "2301234567890".equals(text.getText()));
	assertEquals("2301234567890", text.getText());
	text.copy();
	text.setSelection(3);
	text.paste();
	SwtTestUtil.processEvents(1000, () -> "230231234567890".equals(text.getText()));
	assertEquals("230231234567890", text.getText());

	// tests a SINGLE line text editor
	makeCleanEnvironment(true);

	text.setText("0" + delimiterString + "1");
	text.selectAll();
	text.copy();
	text.setSelection(0);
	text.paste();

	if (SwtTestUtil.fCheckSWTPolicy) {
		String expected2 = "0" + delimiterString + "1" + "0" + delimiterString + "1";
		SwtTestUtil.processEvents(1000, () -> expected2.equals(text.getText()));
		assertEquals("0" + delimiterString + "1" + "0" + delimiterString + "1", text.getText());
	}
}

@Test
public void test_selectAll() {
	if (SwtTestUtil.isCocoa) {
		// TODO Fix Cocoa failure.
		if (SwtTestUtil.verbose) {
			System.out
					.println("Excluded test_selectAll(org.eclipse.swt.tests.junit.Test_org_eclipse_swt_widgets_Text).");
		}
		return;
	}
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
	if (SwtTestUtil.fCheckBogusTestCases) {
		text.setText("01234" + delimiterString+"567890");
		assertEquals("01234" + delimiterString+"567890", text.getText());
		text.selectAll();
		assertEquals("01234" + delimiterString+"567890", text.getSelectionText());
		text.cut();
		assertEquals("", text.getText());
	}
}

@Test
public void test_setDoubleClickEnabledZ() {
	text.setDoubleClickEnabled(true);
	assertTrue(text.getDoubleClickEnabled());

	text.setDoubleClickEnabled(false);
	assertFalse(text.getDoubleClickEnabled());

	// tests a SINGLE line text editor
	makeCleanEnvironment(true);

	text.setDoubleClickEnabled(true);
	assertTrue(text.getDoubleClickEnabled());

	text.setDoubleClickEnabled(false);
	assertFalse(text.getDoubleClickEnabled());
}

@Test
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

@Test
public void test_setEditableZ() {
	text.setEditable(true);
	assertTrue(text.getEditable());
	text.setEditable(false);
	assertFalse(text.getEditable());
	text.setEditable(false);
	assertFalse(text.getEditable());
	text.setEditable(true);
	assertTrue(text.getEditable());
}

@Tag("gtk4-todo")
@Override
@Test
public void test_setFontLorg_eclipse_swt_graphics_Font() {
	FontData fontData = text.getFont().getFontData()[0];
	Font font = new Font(text.getDisplay(), fontData.getName(), 8, fontData.getStyle());
	text.setFont(font);
	int lineHeight = text.getLineHeight();
	text.setFont(null);
	font.dispose();
	font = new Font(text.getDisplay(), fontData.getName(), 12, fontData.getStyle());
	text.setFont(font);
	assertTrue(text.getLineHeight() > lineHeight && font.equals(text.getFont()));
	text.setFont(null);
	font.dispose();
}

@Test
public void test_setForegroundAfterBackground() {
	makeCleanEnvironment(false);
	Color gray = text.getDisplay().getSystemColor(SWT.COLOR_DARK_GRAY);
	Color white = text.getDisplay().getSystemColor(SWT.COLOR_WHITE);
	int systemColor = SwtTestUtil.isCocoa ? SWT.COLOR_LIST_FOREGROUND : SWT.COLOR_WIDGET_FOREGROUND;
	Color defaultForeground = text.getDisplay().getSystemColor(systemColor);

	text.setBackground(gray);
	assertEquals(text.getForeground(), defaultForeground);
	text.setForeground(white);
	assertEquals(text.getForeground(), white);
	assertEquals(text.getBackground(), gray);
}

@Test
public void test_setOrientationI() {
	text.setOrientation(SWT.RIGHT_TO_LEFT);
	if ((text.getStyle() & SWT.MIRRORED) != 0) {
		assertEquals(SWT.RIGHT_TO_LEFT, text.getOrientation());
	}
	text.setOrientation(SWT.LEFT_TO_RIGHT);
	assertEquals(SWT.LEFT_TO_RIGHT, text.getOrientation());
}

@Override
@Test
public void test_setRedrawZ() {
	text.setRedraw(false);
	text.setRedraw(true);
}

@Test
public void test_setSelectionI() {
	text.setText("01234567890");
	assertEquals("", text.getSelectionText());
	text.setSelection(3, 7);
	assertEquals("3456", text.getSelectionText());
	text.setSelection(3, 0);
	assertEquals("012", text.getSelectionText());
}

@Test
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
	if (SwtTestUtil.fCheckBogusTestCases) {
		text.setSelection(2, 100);
		assertEquals(9 +delimiterString.length(), text.getSelectionCount());
	}
}

@Test
public void test_setSelectionLorg_eclipse_swt_graphics_Point() {
	text.setText("dsdsdasdslaasdas");
	assertThrows(IllegalArgumentException.class, ()->text.setSelection((Point) null));

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
	assertThrows(IllegalArgumentException.class, ()->text.setSelection((Point) null));

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

@Test
public void test_setTabsI() {
	for (int i = 0; i < 200; i++) {
		text.setTabs(i);
		assertEquals(i, text.getTabs());
	}
}

@Test
public void test_setTextLimitI() {
	if (SwtTestUtil.isGTK) {
		//TODO Fix GTK failure.
		if (SwtTestUtil.verbose) {
			System.out.println("Excluded test_setTextLimitI(org.eclipse.swt.tests.junit.Test_org_eclipse_swt_widgets_Text)");
		}
		return;
	}
	text.setTextLimit(10);
	assertEquals(10, text.getTextLimit());

	text.setTextLimit(Text.LIMIT);
	assertEquals(Text.LIMIT, text.getTextLimit());

	assertThrows(IllegalArgumentException.class, () -> text.setTextLimit(0));
}

@Test
public void test_setTextLjava_lang_String() {
	assertThrows(IllegalArgumentException.class, ()->text.setText(null));

	text.setText("");

	// tests a SINGLE line text editor
	makeCleanEnvironment(true);

	assertEquals("", text.getText());
	text.setText("01234567890");
	assertEquals("01234567890", text.getText());
	text.setText("");
	assertEquals("", text.getText());
	if (SwtTestUtil.fCheckBogusTestCases) {
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

@Test
public void test_setTopIndexI() {
	if (SwtTestUtil.isGTK || SwtTestUtil.isCocoa) {
		//TODO Fix GTK and cocoa sfailure.
		if (SwtTestUtil.verbose) {
			System.out.println("Excluded test_setTopIndexI(org.eclipse.swt.tests.junit.Test_org_eclipse_swt_widgets_Text)");
		}
		return;
	}
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

@Test
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

@Override
protected void setWidget(Widget w) {
	text = (Text)w;
	super.setWidget(w);
}

@Test
public void test_consistency_EnterSelection () {
	makeCleanEnvironment(true);
	consistencyEvent(13, 10, 0, 0, ConsistencyUtility.KEY_PRESS);
}

@Test
public void test_consistency_Modify() {
	makeCleanEnvironment(true);
	consistencyEvent('a', 0, 0, 0, ConsistencyUtility.KEY_PRESS);
}

@Test
public void test_consistency_MenuDetect () {
	makeCleanEnvironment(true);
	consistencyEvent(10, 10, 3, ConsistencyUtility.ESCAPE_MENU, ConsistencyUtility.MOUSE_CLICK);
}

@Test
public void test_consistency_DragDetect () {
	makeCleanEnvironment(true);
	consistencyEvent(30, 10, 50, 0, ConsistencyUtility.MOUSE_DRAG);
}

@Tag("gtk4-todo")
@Tag("clipboard")
@Test
public void test_consistency_Segments () {
	if (SwtTestUtil.isCocoa) {
		// TODO Fix Cocoa failure.
		if (SwtTestUtil.verbose) {
			System.out
					.println("Excluded test_consistency_Segments(org.eclipse.swt.tests.junit.Test_org_eclipse_swt_widgets_Text).");
		}
		return;
	}
	final SegmentListener sl1 = event -> {
		if ((event.lineText.length() & 1) == 1) {
			event.segments = new int [] {1, event.lineText.length()};
			event.segmentsChars = null;
		} else {
			event.segments = new int [] {0, 0, event.lineText.length()};
			event.segmentsChars = new char [] {':', '<', '>'};
		}
		listenerCalled = true;
	};
	assertThrows(IllegalArgumentException.class, ()->text.addSegmentListener(null));
	boolean[] singleLine = {false, true};
	for (int i = singleLine.length; i-- > 0;) {
		makeCleanEnvironment(singleLine[i]);
		text.addSegmentListener(sl1);
		doSegmentsTest(true);

		text.addSegmentListener(sl1);
		doSegmentsTest(true);

		text.removeSegmentListener(sl1);
		doSegmentsTest(true);

		text.removeSegmentListener(sl1);
		text.setText(text.getText());
		doSegmentsTest(false);
	}
}

private void doSegmentsTest (boolean isListening) {
	String string = "1234";

	// Test setText
	text.setText(string);
	assertEquals(isListening, listenerCalled);
	listenerCalled = false;
	assertEquals(string, text.getText());

	// Test append
	String substr = "56";
	text.append(substr);
	string += substr;
	assertEquals(isListening, listenerCalled);
	listenerCalled = false;
	assertEquals(string, text.getText());

	// Test limit
	if ((text.getStyle() & SWT.SINGLE) != 0) {
		int limit = string.length() - 1;
		text.setTextLimit(limit);
		assertEquals(limit, text.getTextLimit());
		text.setText(string);
		assertEquals(string.substring(0, limit), text.getText());
	}
	text.setTextLimit(Text.LIMIT);
	text.setText(string);
	assertEquals(string, text.getText());

	// Test selection, copy and paste
	listenerCalled = false;
	Point pt = new Point(1, 3);
	text.setSelection(pt);
	assertEquals(pt, text.getSelection());
	assertFalse(listenerCalled);
	text.copy();
	assertEquals(isListening, listenerCalled);
	listenerCalled = false;

	substr = string.substring(pt.x, pt.y);
	pt.x = pt.y = 1;
	text.setSelection(pt);
	text.paste();
	assertEquals(isListening, listenerCalled);
	listenerCalled = false;

	assertEquals(string.substring(0, pt.x) + substr + string.substring(pt.y), text.getText());
	pt.x = pt.y = pt.x + substr.length();
	assertEquals(pt, text.getSelection());

	// Test cut
	pt.x -= 2;
	text.setSelection(pt);
	assertEquals(substr, text.getSelectionText());
	assertEquals(substr, text.getText(pt.x, pt.y - 1));
	text.cut();
	assertEquals(isListening, listenerCalled);
	listenerCalled = false;
	assertEquals(string, text.getText());

	// Test insert
	substr = "12";
	pt.x = 6;
	pt.y = 8;
	text.setSelection(pt.x, pt.y);
	text.cut();
	pt.y = pt.x;
	assertEquals(pt, text.getSelection());
	listenerCalled = false;
	pt.x = pt.y = 0;
	text.setSelection(pt);
	text.insert(substr);
	assertEquals(isListening, listenerCalled);
	listenerCalled = false;
	pt.x = pt.y = pt.x + substr.length();
	assertEquals(pt, text.getSelection());
	assertEquals(substr + string, text.getText());
}

/**
 * Bug 565164 - SWT.BS event no longer working
 */
@Test
public void test_backspaceAndDelete() throws InterruptedException {
	assumeTrue(Boolean.parseBoolean(System.getenv("GITHUB_ACTIONS")),
			"Display.post tests only run successfully on GitHub actions - see https://github.com/eclipse-platform/eclipse.platform.swt/issues/2571");
	shell.open();
	text.setSize(10, 50);
	// The display.post needs to successfully obtain the focused window (at least on GTK3)
	// so we can send events to it. This processEvents gives SWT/GTK time to draw/focus/etc
	// the window so that org.eclipse.swt.widgets.Display.findFocusedWindow()
	// returns non-zero
	SwtTestUtil.processEvents();

	Display display = Display.getDefault();

	Event a = keyEvent('a', SWT.KeyDown, display.getFocusControl());
	Event aUp = keyEvent('a', SWT.KeyUp, display.getFocusControl());
	Event backspace = keyEvent(SWT.BS, SWT.KeyDown, display.getFocusControl());
	Event backspaceUp = keyEvent(SWT.BS, SWT.KeyUp, display.getFocusControl());

	assertTrue(display.post(a));
	assertTrue(display.post(aUp));

	SwtTestUtil.processEvents(10000, () -> text.getText().length() == 1);
	assertEquals(1, text.getText().length());

	assertTrue(display.post(backspace));
	assertTrue(display.post(backspaceUp));

	SwtTestUtil.processEvents(10000, () -> text.getText().length() == 0);
	assertEquals(0, text.getText().length());
}

private Event keyEvent(int key, int type, Widget w) {
	Event e = new Event();
	e.keyCode= key;
	e.character = (char) key;
	e.type = type;
	e.widget = w;
	return e;
}

/**
 * Issue 472 - NPE on macOS when creating multiline Text inside Group
 */
@Test
public void test_issue472() {
	shell.open();

	// On different macOS, different font height is needed to reproduce
	for (int iFontHeight = 10; iFontHeight < 20; iFontHeight++)
	{
		Font font = new Font(shell.getDisplay(), "", iFontHeight, 0);

		Group group = new Group(shell, SWT.NONE);
		group.setText("Group");
		group.setFont(font);

		try {
			Text text = new Text(group, SWT.MULTI | SWT.BORDER | SWT.READ_ONLY | SWT.WRAP);
			text.dispose();
		} catch (NullPointerException ex) {
			throw new RuntimeException("NPE with font=" + iFontHeight, ex);
		} finally {
			font.dispose();
		}
	}
}
}
