/*******************************************************************************
 * Copyright (c) 2000, 2020 IBM Corporation and others.
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


import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.junit.Assume.assumeFalse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.function.BooleanSupplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.BidiSegmentListener;
import org.eclipse.swt.custom.CaretListener;
import org.eclipse.swt.custom.ExtendedModifyListener;
import org.eclipse.swt.custom.LineBackgroundListener;
import org.eclipse.swt.custom.LineStyleEvent;
import org.eclipse.swt.custom.LineStyleListener;
import org.eclipse.swt.custom.MovementEvent;
import org.eclipse.swt.custom.MovementListener;
import org.eclipse.swt.custom.ST;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.custom.StyledTextContent;
import org.eclipse.swt.custom.TextChangeListener;
import org.eclipse.swt.custom.VerifyKeyListener;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.RTFTransfer;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.GlyphMetrics;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.internal.BidiUtil;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.printing.Printer;
import org.eclipse.swt.widgets.Caret;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.test.Screenshots;
import org.junit.After;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestWatcher;

/**
 * Automated Test Suite for class org.eclipse.swt.custom.StyledText
 *
 * @see org.eclipse.swt.custom.StyledText
 */
public class Test_org_eclipse_swt_custom_StyledText extends Test_org_eclipse_swt_widgets_Canvas {

StyledText text;
final static RGB RED = new RGB(255,0,0);
final static RGB BLUE = new RGB(0,0,255);
final static RGB GREEN = new RGB(0,255,0);
final static RGB YELLOW = new RGB(255,255,0);
final static RGB CYAN = new RGB(0,255,255);
final static RGB PURPLE = new RGB(255,0,255);
final static String PLATFORM_LINE_DELIMITER = System.lineSeparator();
Map<RGB, Color> colors = new HashMap<>();
private boolean listenerCalled;
private boolean listener2Called;

@Rule public TestWatcher screenshotRule = new TestWatcher() {
	@Override
	protected void failed(Throwable e, org.junit.runner.Description description) {
		super.failed(e, description);
		Screenshots.takeScreenshot(description.getTestClass(), description.getMethodName());
	}
};

@Override
@Before
public void setUp() {
	super.setUp();
	initializeColors();
	text = new StyledText(shell, SWT.NULL);
	setWidget(text);
}

@Override
@After
public void tearDown() {
	for (Color color : colors.values()) {
		color.dispose();
	}
	super.tearDown();

}

// this method must not be public so that the auto-gen tool keeps it
private StyleRange[] defaultStyles() {
	return new StyleRange[] {
		getStyle(0,48,RED,YELLOW),
		getStyle(58,10,BLUE,CYAN),
		getStyle(68,10,GREEN,PURPLE)};
}
// this method must not be public so that the auto-gen tool keeps it
protected void getLineBackgrounds() {
	for (int i=0; i<text.getLineCount(); i++) {
		text.getLineBackground(i);
	}
}
// this method must not be public so that the auto-gen tool keeps it
private String textString() {
	return "This is the text component in testing\nNew Line1\nNew Line2\nNew Line3\nNew Line4.";
}
boolean isBidiCaret() {
	return BidiUtil.isBidiPlatform();
}
// this method must not be public so that the auto-gen tool keeps it
private StyleRange getStyle(int start, int length, RGB fg, RGB bg) {
	StyleRange style = new StyleRange();
	style.start = start;
	style.length = length;
	if (fg != null) style.foreground = getColor(fg);
	else style.foreground = null;
	if (bg != null) style.background = getColor(bg);
	else style.background = null;
	return style;
}
// this method must not be public so that the auto-gen tool keeps it
private Color getColor(RGB rgb) {
	return colors.get(rgb);
}
// this method must not be public so that the auto-gen tool keeps it
protected void initializeColors() {
	colors.put(RED, new Color (RED));
	colors.put(BLUE, new Color (BLUE));
	colors.put(GREEN, new Color (GREEN));
	colors.put(YELLOW, new Color (YELLOW));
	colors.put(CYAN, new Color (CYAN));
	colors.put(PURPLE, new Color (PURPLE));
}

@Override
@Test
public void test_ConstructorLorg_eclipse_swt_widgets_CompositeI(){
	StyledText text = new StyledText(shell, SWT.READ_ONLY);

	assertFalse(":a:", text.getEditable());
	text.dispose();

	text = new StyledText(shell, SWT.H_SCROLL | SWT.V_SCROLL | SWT.SINGLE);
	assertNull(":b:", text.getVerticalBar());
	assertNull(":c:", text.getHorizontalBar());
	text.dispose();
}

@Test
public void test_addExtendedModifyListenerLorg_eclipse_swt_custom_ExtendedModifyListener() {
	final String line = "Line1";
	boolean exceptionThrown = false;
	ExtendedModifyListener listener = event -> {
		listenerCalled = true;
		assertEquals("ExtendedModify event data invalid", 0, event.start);
		assertEquals("ExtendedModify event data invalid", line.length(), event.length);
		assertEquals("ExtendedModify event data invalid", "", event.replacedText);
	};

	try {
		text.addExtendedModifyListener(null);
	}
	catch (IllegalArgumentException e) {
		exceptionThrown = true;
	}
	assertTrue("Expected exception not thrown", exceptionThrown);

	// test whether all content modifying API methods send an ExtendedModify event
	text.addExtendedModifyListener(listener);

	listenerCalled = false;
	text.append(line);
	assertTrue("append does not send event", listenerCalled);

	listenerCalled = false;
	text.insert(line);
	assertTrue("replaceTextRange does not send event", listenerCalled);

	listenerCalled = false;
	text.removeExtendedModifyListener(listener);
	listener = event -> {
		listenerCalled = true;
		assertEquals("ExtendedModify event data invalid", 0, event.start);
		assertEquals("ExtendedModify event data invalid", line.length(), event.length);
		assertEquals("ExtendedModify event data invalid", line.substring(0, 1), event.replacedText);
	};
	text.addExtendedModifyListener(listener);
	text.replaceTextRange(0, 1, line);
	assertTrue("replaceTextRange does not send event", listenerCalled);

	listenerCalled = false;
	text.removeExtendedModifyListener(listener);
	listener = event -> {
		listenerCalled = true;
		assertEquals("ExtendedModify event data invalid", 0, event.start);
		assertEquals("ExtendedModify event data invalid", line.length(), event.length);
		assertEquals("ExtendedModify event data invalid", line + line.substring(1, line.length()) + line, event.replacedText);
	};
	text.addExtendedModifyListener(listener);
	text.setText(line);
	assertTrue("setText does not send event", listenerCalled);

	listenerCalled = false;
	text.removeExtendedModifyListener(listener);
	// cause StyledText to call the listener.
	text.setText(line);
	assertFalse("Listener not removed", listenerCalled);
}

@Test
public void test_setKeyBindingII(){
	text.setKeyBinding(SWT.DEL, SWT.NULL);
	assertTrue(":a:", text.getKeyBinding(SWT.DEL) == SWT.NULL);
	text.setKeyBinding(SWT.DEL, ST.LINE_UP);
	assertTrue(":b:", text.getKeyBinding(SWT.DEL) == ST.LINE_UP);
	text.setKeyBinding(SWT.DEL | SWT.MOD2, ST.SELECT_PAGE_UP);
	assertTrue(":c:", text.getKeyBinding(SWT.DEL | SWT.MOD2) == ST.SELECT_PAGE_UP);
	text.setKeyBinding(SWT.DEL | SWT.MOD2, ST.PAGE_UP);
	assertTrue(":d:", text.getKeyBinding(SWT.DEL | SWT.MOD2) == ST.PAGE_UP);
	text.setKeyBinding(SWT.KEYPAD_CR, ST.LINE_END);
	assertTrue(":e:", text.getKeyBinding(SWT.KEYPAD_CR) == ST.LINE_END);
	text.setKeyBinding(SWT.KEYPAD_CR | SWT.MOD1, ST.LINE_START);
	assertTrue(":f:", text.getKeyBinding(SWT.KEYPAD_CR | SWT.MOD1) == ST.LINE_START);
	text.setKeyBinding(-1, ST.PAGE_UP);
	text.setKeyBinding(-1, -1);
}

@Test
public void test_addBidiSegmentListenerLorg_eclipse_swt_custom_BidiSegmentListener() {
	String line = "Line1";
	boolean exceptionThrown = false;
	BidiSegmentListener listener = event -> listenerCalled = true;

	try {
		text.addBidiSegmentListener(null);
	}
	catch (IllegalArgumentException e) {
		exceptionThrown = true;
	}
	assertTrue("Expected exception not thrown", exceptionThrown);

	listenerCalled = false;
	text.setText(line);
	text.addBidiSegmentListener(listener);
	// cause StyledText to call the BidiSegmentListener.
	text.getLocationAtOffset(0);
	if (SwtTestUtil.isBidi()) {
		assertTrue("Listener not called", listenerCalled);
	}
	else {
		assertFalse("Listener called when it shouldn't be", listenerCalled);
	}
	listenerCalled = false;
	text.removeBidiSegmentListener(listener);
	// cause StyledText to call the BidiSegmentListener.
	text.getLocationAtOffset(0);
	assertFalse("Listener not removed", listenerCalled);
}

@Test(expected=IllegalArgumentException.class)
public void test_addCaretListener_passingNullThrowsException() {
	text.addCaretListener(null);
}

@Test
public void test_addCaretListener_CaretListenerCalled() {
	listenerCalled = false;
	CaretListener listener = event -> listenerCalled = true;
	text.setText("Line1");
	text.addCaretListener(listener);
	text.setCaretOffset(1);
	assertTrue("Listener not called", listenerCalled);
}

@Test
public void test_removeCaretListener_CaretListenerNotCalled() {
	listenerCalled = false;
	CaretListener listener = event -> listenerCalled = true;
	text.addCaretListener(listener);
	text.removeCaretListener(listener);
	text.setCaretOffset(1);
	assertFalse("Listener not removed", listenerCalled);
}

@Test
public void test_addLineBackgroundListenerLorg_eclipse_swt_custom_LineBackgroundListener() {
	String line = "Line1";
	boolean exceptionThrown = false;
	LineBackgroundListener listener = event -> listenerCalled = true;

	try {
		text.addLineBackgroundListener(null);
	}
	catch (IllegalArgumentException e) {
		exceptionThrown = true;
	}
	assertTrue("Expected exception not thrown", exceptionThrown);

	listenerCalled = false;
	text.setText(line);
	text.addLineBackgroundListener(listener);
	// cause StyledText to call the listener.
	text.setSelection(0, text.getCharCount());
	text.copy();
	assertTrue("Listener not called", listenerCalled);

	listenerCalled = false;
	text.removeLineBackgroundListener(listener);
	// cause StyledText to call the listener.
	text.setText(line);
	text.setSelection(0, text.getCharCount());
	text.copy();
	assertFalse("Listener not removed", listenerCalled);
}

@Test
public void test_addLineStyleListenerLorg_eclipse_swt_custom_LineStyleListener() {
	String line = "Line1";
	boolean exceptionThrown = false;
	LineStyleListener listener = event -> listenerCalled = true;

	try {
		text.addLineStyleListener(null);
	}
	catch (IllegalArgumentException e) {
		exceptionThrown = true;
	}
	assertTrue("Expected exception not thrown", exceptionThrown);

	listenerCalled = false;
	text.setText(line);
	text.addLineStyleListener(listener);
	// cause StyledText to call the listener.
	text.setSelection(0, text.getCharCount());
	text.copy();
	assertTrue("Listener not called", listenerCalled);

	listenerCalled = false;
	text.removeLineStyleListener(listener);
	// cause StyledText to call the listener.
	text.setText(line);
	text.setSelection(0, text.getCharCount());
	text.copy();
	assertFalse("Listener not removed", listenerCalled);
}

@Test
public void test_addModifyListenerLorg_eclipse_swt_events_ModifyListener() {
	String line = "Line1";
	boolean exceptionThrown = false;
	ModifyListener listener = event -> listenerCalled = true;

	try {
		text.addModifyListener(null);
	}
	catch (IllegalArgumentException e) {
		exceptionThrown = true;
	}
	assertTrue("Expected exception not thrown", exceptionThrown);

	// test whether all content modifying API methods send a Modify event
	text.addModifyListener(listener);

	listenerCalled = false;
	text.append(line);
	assertTrue("append does not send event", listenerCalled);

	listenerCalled = false;
	text.insert(line);
	assertTrue("replaceTextRange does not send event", listenerCalled);

	listenerCalled = false;
	text.replaceTextRange(0, 1, line);
	assertTrue("replaceTextRange does not send event", listenerCalled);

	listenerCalled = false;
	text.setText(line);
	assertTrue("setText does not send event", listenerCalled);

	listenerCalled = false;
	text.removeModifyListener(listener);
	// cause StyledText to call the listener.
	text.setText(line);
	assertFalse("Listener not removed", listenerCalled);
}

@Test
public void test_addSelectionListenerLorg_eclipse_swt_events_SelectionListener() {
	String line = "Line1";
	boolean exceptionThrown = false;
	SelectionListener listener = new SelectionListener() {
		@Override
		public void widgetSelected(SelectionEvent event) {
			listenerCalled = true;
		}
		@Override
		public void widgetDefaultSelected(SelectionEvent event) {
			listener2Called = true;
		}
	};

	try {
		text.addSelectionListener(null);
	}
	catch (IllegalArgumentException e) {
		exceptionThrown = true;
	}
	assertTrue("Expected exception not thrown", exceptionThrown);

	text.setText(line);
	listenerCalled = false;
	listener2Called = false;
	text.addSelectionListener(listener);
	// cause StyledText to call the listener.
	text.invokeAction(ST.SELECT_LINE_END);
	assertTrue("Listener not called", listenerCalled);
	assertFalse("Listener called unexpectedly", listener2Called);

	listenerCalled = false;
	listener2Called = false;
	text.removeSelectionListener(listener);
	// cause StyledText to call the listener.
	text.invokeAction(ST.SELECT_LINE_END);
	assertFalse("Listener not removed", listenerCalled);
	assertFalse("Listener called unexpectedly", listener2Called);
}


@Test
public void test_addSelectionListenerWidgetSelectedAdapterLorg_eclipse_swt_events_SelectionListener() {
	String line = "Line1";
	SelectionListener listener = SelectionListener.widgetSelectedAdapter(e-> listenerCalled = true );

	text.setText(line);
	listenerCalled = false;
	text.addSelectionListener(listener);
	// cause StyledText to call the listener.
	text.invokeAction(ST.SELECT_LINE_END);
	assertTrue("Listener not called", listenerCalled);

	listenerCalled = false;
	text.removeSelectionListener(listener);
	// cause StyledText to call the listener.
	text.invokeAction(ST.SELECT_LINE_END);
	assertFalse("Listener not removed", listenerCalled);
}

@Test
public void test_addSelectionListenerWidgetDefaultSelectedAdapterLorg_eclipse_swt_events_SelectionListener() {
	String line = "Line1";
	SelectionListener listener = SelectionListener.widgetDefaultSelectedAdapter(e-> listener2Called = true );

	text.setText(line);
	listener2Called = false;
	text.addSelectionListener(listener);
	// cause StyledText to call the listener.
	text.invokeAction(ST.SELECT_LINE_END);
	assertFalse("Listener called unexpectedly", listener2Called);

	listener2Called = false;
	text.removeSelectionListener(listener);
	// cause StyledText to call the listener.
	text.invokeAction(ST.SELECT_LINE_END);
	assertFalse("Listener called unexpectedly", listener2Called);
}

@Test
public void test_addVerifyKeyListenerLorg_eclipse_swt_custom_VerifyKeyListener() {
	boolean exceptionThrown = false;
	VerifyKeyListener listener = event -> {
	};

	try {
		text.addVerifyKeyListener(null);
	}
	catch (IllegalArgumentException e) {
		exceptionThrown = true;
	}
	assertTrue("Expected exception not thrown", exceptionThrown);

	// only test whether listener can be added and removed.
	// can't test listener because VerifyKey is user driven.
	text.addVerifyKeyListener(listener);
	text.removeVerifyKeyListener(listener);
}

@Test
public void test_addVerifyListenerLorg_eclipse_swt_events_VerifyListener() {
	final String line = "Line1";
	final String newLine = "NewLine1";
	final int textLength;
	boolean exceptionThrown = false;
	VerifyListener listener = event -> {
		listenerCalled = true;
		assertEquals("Verify event data invalid", 0, event.start);
		assertEquals("Verify event data invalid", 0, event.end);
		assertEquals("Verify event data invalid", line, event.text);
		event.start = 2;
		event.end = 5;
		event.text = newLine;
	};

	try {
		text.addVerifyListener(null);
	}
	catch (IllegalArgumentException e) {
		exceptionThrown = true;
	}
	assertTrue("Expected exception not thrown", exceptionThrown);

	// test whether all content modifying API sends a Verify event
	text.addVerifyListener(listener);

	listenerCalled = false;
	text.append(line);
	assertTrue("append does not send event", listenerCalled);
	assertEquals("Listener failed", newLine, text.getText());

	listenerCalled = false;
	text.insert(line);
	assertTrue("replaceTextRange does not send event", listenerCalled);
	assertEquals("Listener failed", newLine + newLine, text.getText());

	listenerCalled = false;
	text.removeVerifyListener(listener);
	listener = event -> {
		listenerCalled = true;
		assertEquals("Verify event data invalid", 0, event.start);
		assertEquals("Verify event data invalid", 1, event.end);
		assertEquals("Verify event data invalid", line, event.text);
		event.start = 2;
		event.end = 5;
		event.text = newLine;
	};
	text.addVerifyListener(listener);
	textLength = text.getCharCount() - 1 + newLine.length();
	text.replaceTextRange(0, 1, line);
	assertTrue("replaceTextRange does not send event", listenerCalled);
	assertEquals("Listener failed", newLine + newLine.substring(1, newLine.length()) + newLine, text.getText());

	listenerCalled = false;
	text.removeVerifyListener(listener);
	listener = event -> {
		listenerCalled = true;
		assertEquals("Verify event data invalid", 0, event.start);
		assertEquals("Verify event data invalid", textLength, event.end);
		assertEquals("Verify event data invalid", line, event.text);
		event.start = 2;
		event.end = 5;
		event.text = newLine;
	};
	text.addVerifyListener(listener);
	text.setText(line);
	assertTrue("setText does not send event", listenerCalled);
	assertEquals("Listener failed", newLine, text.getText());

	text.removeVerifyListener(listener);

	listenerCalled = false;
	listener = event -> {
		listenerCalled = true;
		assertEquals("Verify event data invalid", 2, event.start);
		assertEquals("Verify event data invalid", newLine.length(), event.end);
		assertEquals("Verify event data invalid", line, event.text);
		event.doit = false;
	};
	text.addVerifyListener(listener);
	// cause StyledText to call the listener.
	text.replaceTextRange(2, text.getCharCount() - 2, line);
	assertTrue("Listener not called", listenerCalled);
	assertEquals("Listener failed", newLine, text.getText());

	listenerCalled = false;
	text.removeVerifyListener(listener);
	// cause StyledText to call the listener.
	text.setText(line);
	assertFalse("Listener not removed", listenerCalled);
}

@Test(expected=IllegalArgumentException.class)
public void test_addWordMovementListener_passingNullThrowsException() {
	text.addWordMovementListener(null);
}

@Test
public void test_addWordMovementListener_invokeActionSelectWordNextCallsGetNextOffset() {
	MovementListener listener = new RecordingMovementListener();
	listenerCalled = false;
	listener2Called = false;
	text.setText("Word0 Word1 Word2");
	text.addWordMovementListener(listener);
	text.invokeAction(ST.SELECT_WORD_NEXT);
	assertFalse("Listener unexpectantly called", listenerCalled);
	assertTrue("Listener not called", listener2Called);
}

@Test
public void test_addWordMovementListener_invokeActionSelectWordPreviousCallsGetPreviousOffset() {
	MovementListener listener = new RecordingMovementListener();
	listenerCalled = false;
	listener2Called = false;
	text.setText("Word0 Word1 Word2");
	text.addWordMovementListener(listener);
	text.invokeAction(ST.SELECT_WORD_PREVIOUS);
	assertTrue("Listener not called", listenerCalled);
	assertFalse("Listener unexpectantly called", listener2Called);
}

@Test
public void test_removeWordMovementListener_invokeActionSelectWordCallsNoMethods() {
	MovementListener listener = new RecordingMovementListener();
	listenerCalled = false;
	listener2Called = false;
	text.setText("Word0 Word1 Word2");
	text.addWordMovementListener(listener);
	text.removeWordMovementListener(listener);
	text.invokeAction(ST.SELECT_WORD_NEXT);
	text.invokeAction(ST.SELECT_WORD_PREVIOUS);
	assertFalse("Listener unexpectantly called", listenerCalled);
	assertFalse("Listener unexpectantly called", listener2Called);
}

private class RecordingMovementListener implements MovementListener {
	@Override
	public void getPreviousOffset(MovementEvent event) {
		listenerCalled = true;
	}

	@Override
	public void getNextOffset(MovementEvent event) {
		listener2Called = true;
	}
}

@Test
public void test_appendLjava_lang_String() {
	boolean exceptionThrown;
	String line = "Line1";

	text.append(line);
	assertEquals("append to empty text", line, text.getText());

	exceptionThrown = false;
	try {
		text.append(null);
	}
	catch (IllegalArgumentException exception) {
		exceptionThrown = true;
	}
	assertTrue("append null string", exceptionThrown);

	text.append("");
	assertEquals("append empty string", line, text.getText());

	text.append(line);
	assertEquals("append non-empty string", line + line, text.getText());

	text.setText("");
	String text2 = "line\r";
	text.append(text2);
	assertEquals("append string ending with line delimiter", text2, text.getText());

	String text3 = "line\r\nline3";
	text.append(text3);
	assertEquals("append multi line string", text2 + text3, text.getText());
}

@Override
@Test
public void test_computeSizeIIZ() {
	// inherited test is sufficient
}

@Test
public void test_computeSizeAlignment(){
	shell.setLayout(new GridLayout());
	StyledText singleText = new StyledText(shell, SWT.SINGLE);
	shell.layout(true);
	Point beforeAlignment = singleText.computeSize(100, SWT.DEFAULT);
	//Should not change the computed size
	singleText.setAlignment(SWT.RIGHT);
	Point afterAlignment = singleText.computeSize(100, SWT.DEFAULT);
	assertEquals(beforeAlignment, afterAlignment);
	singleText.dispose();
}
@Test
public void test_marginsCorrect(){
	shell.setLayout(new GridLayout());
	StyledText singleText = new StyledText(shell, SWT.SINGLE);
	int leftMargin = 10;
	singleText.setLeftMargin(leftMargin);
	shell.layout(true);
	singleText.setAlignment(SWT.RIGHT);
	assertEquals(leftMargin, singleText.getLeftMargin());
	singleText.setLeftMargin(leftMargin);
	assertEquals(leftMargin, singleText.getLeftMargin());
	singleText.dispose();
}
@Test
public void test_copy() {
	if (SwtTestUtil.isCocoa) {
		// TODO Fix Cocoa failure.
		if (SwtTestUtil.verbose) {
			System.out
					.println("Excluded test_copy(org.eclipse.swt.tests.junit.Test_org_eclipse_swt_custom_StyledText).");
		}
		return;
	}
	Clipboard clipboard = new Clipboard(text.getDisplay());
	TextTransfer transfer = TextTransfer.getInstance();
	String clipboardText;
	String convertedText;

	String before = (String) clipboard.getContents(transfer);
	text.setSelectionRange(0, 0);
	text.copy();
	clipboardText = (String) clipboard.getContents(transfer);
	assertTrue(":a:", before == null ? clipboardText == null : before.equals(clipboardText));

	before = (String) clipboard.getContents(transfer);
	text.setText("0123456789");
	text.setSelectionRange(0, 0);
	text.copy();
	clipboardText = (String) clipboard.getContents(transfer);
	assertTrue(":c:", before == null ? clipboardText == null : before.equals(clipboardText));

	text.setSelectionRange(0, 1);
	text.copy();
	clipboardText = (String) clipboard.getContents(transfer);
	assertTrue(":d:", clipboardText != null && clipboardText.equals("0"));

	text.setSelectionRange(1, 2);
	text.copy();
	clipboardText = (String) clipboard.getContents(transfer);
	assertTrue(":e:", clipboardText != null && clipboardText.equals("12"));

	// test line delimiter conversion
	text.setText("\rLine1\nLine2\r\nLine3\n\rLine4\n");
	text.setSelectionRange(0, text.getCharCount());
	text.copy();
	clipboardText = (String) clipboard.getContents(transfer);
	if (SwtTestUtil.isWindowsOS) {
		convertedText = "\r\nLine1\r\nLine2\r\nLine3\r\n\r\nLine4\r\n";
	}
	else {
		convertedText = "\nLine1\nLine2\nLine3\n\nLine4\n";
	}
	assertTrue(":f:", clipboardText != null && clipboardText.equals(convertedText));

	// test line delimiter conversion
	text.setText("Line1\r\nLine2");
	text.setSelectionRange(0, text.getCharCount());
	text.copy();
	clipboardText = (String) clipboard.getContents(transfer);
	if (SwtTestUtil.isWindowsOS) {
		convertedText = "Line1\r\nLine2";
	}
	else {
		convertedText = "Line1\nLine2";
	}
	assertTrue(":g:", clipboardText != null && clipboardText.equals(convertedText));

	rtfCopy();
	clipboard.dispose();
}

@Test
public void test_cut() {
	if (SwtTestUtil.isCocoa) {
		// TODO Fix Cocoa failure.
		if (SwtTestUtil.verbose) {
			System.out
					.println("Excluded test_cut(org.eclipse.swt.tests.junit.Test_org_eclipse_swt_custom_StyledText).");
		}
		return;
	}
	Clipboard clipboard = new Clipboard(text.getDisplay());
	TextTransfer transfer = TextTransfer.getInstance();
	String clipboardText;
	String convertedText;

	String before = (String) clipboard.getContents(transfer);
	text.setSelectionRange(0, 0);
	text.cut();
	clipboardText = (String) clipboard.getContents(transfer);
	assertTrue(":a:", before == null ? clipboardText == null : before.equals(clipboardText));

	before = (String) clipboard.getContents(transfer);
	text.setText("0123456789");
	text.setSelectionRange(0, 0);
	text.cut();
	clipboardText = (String) clipboard.getContents(transfer);
	assertTrue(":c:", before == null ? clipboardText == null : before.equals(clipboardText));

	text.setSelectionRange(0, 1);
	text.cut();
	clipboardText = (String) clipboard.getContents(transfer);
	assertTrue(":d:", clipboardText != null && clipboardText.equals("0"));

	text.setSelectionRange(1, 2);
	text.cut();
	clipboardText = (String) clipboard.getContents(transfer);
	assertTrue(":e:", clipboardText != null && clipboardText.equals("23"));

	// test line delimiter conversion
	text.setText("\rLine1\nLine2\r\nLine3\n\rLine4\n");
	text.setSelectionRange(0, text.getCharCount());
	text.cut();
	clipboardText = (String) clipboard.getContents(transfer);
	if (SwtTestUtil.isWindowsOS) {
		convertedText = "\r\nLine1\r\nLine2\r\nLine3\r\n\r\nLine4\r\n";
	}
	else {
		convertedText = "\nLine1\nLine2\nLine3\n\nLine4\n";
	}
	assertTrue(":f:", clipboardText != null && clipboardText.equals(convertedText));

	// test line delimiter conversion
	text.setText("Line1\r\nLine2");
	text.setSelectionRange(0, text.getCharCount());
	text.cut();
	clipboardText = (String) clipboard.getContents(transfer);
	if (SwtTestUtil.isWindowsOS) {
		convertedText = "Line1\r\nLine2";
	}
	else {
		convertedText = "Line1\nLine2";
	}
	assertTrue(":g:", clipboardText != null && clipboardText.equals(convertedText));

	clipboard.dispose();
}

@Test
public void test_getCaretOffset() {
	assertTrue(":a:", text.getCaretOffset() == 0);
	text.setText("Line0\r\n");
	assertTrue(":b:", text.getCaretOffset() == 0);
	text.setTopIndex(1);
	assertTrue(":c:", text.getCaretOffset() == 0);

	text.replaceTextRange(text.getCharCount(), 0, "Line1");
	assertTrue(":d:", text.getCaretOffset() == 0);
	String newText = "Line-1\r\n";
	text.replaceTextRange(0, 0, newText);
	assertTrue(":e:", text.getCaretOffset() == 0);

	text.setCaretOffset(1);
	assertTrue(":f:", text.getCaretOffset() == 1);
	text.replaceTextRange(2, 0, newText);
	assertTrue(":g:", text.getCaretOffset() == 1);
	text.replaceTextRange(0, 0, newText);
	assertTrue(":h:", text.getCaretOffset() == newText.length() + 1);
}

@Test
public void test_getContent() {
	StyledTextContent content = text.getContent();

	assertNotNull(content);
	content = new StyledTextContent() {
		@Override
		public void addTextChangeListener(TextChangeListener listener) {
		}
		@Override
		public int getCharCount() {
			return 0;
		}
		@Override
		public String getLine(int lineIndex) {
			return "";
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
			return "";
		}
		@Override
		public int getOffsetAtLine(int lineIndex) {
			return 0;
		}
		@Override
		public String getTextRange(int start, int length) {
			return "";
		}
		@Override
		public void removeTextChangeListener(TextChangeListener listener) {
		}
		@Override
		public void replaceTextRange(int start, int replaceLength, String text) {
		}
		@Override
		public void setText(String text) {
		}
	};
	text.setContent(content);
	assertEquals(content, text.getContent());
}

@Test
public void test_getDoubleClickEnabled() {
	assertTrue(":a:", text.getDoubleClickEnabled());
	text.setDoubleClickEnabled(true);
	assertTrue(":b:", text.getDoubleClickEnabled());
	text.setDoubleClickEnabled(false);
	assertFalse(":c:", text.getDoubleClickEnabled());
	text.setDoubleClickEnabled(false);
	assertFalse(":d:", text.getDoubleClickEnabled());
	text.setDoubleClickEnabled(true);
	assertTrue(":e:", text.getDoubleClickEnabled());
}

@Test
public void test_getEditable() {
	assertTrue(":a:", text.getEditable());
	text.setEditable(true);
	assertTrue(":b:", text.getEditable());
	text.setEditable(false);
	assertFalse(":c:", text.getEditable());
	text.setEditable(false);
	assertFalse(":d:", text.getEditable());
	text.setEditable(true);
	assertTrue(":e:", text.getEditable());
}

@Test
public void test_getHorizontalIndex() {
	assertTrue(":a:", text.getHorizontalIndex() == 0);
	text.setHorizontalIndex(-1);
	assertTrue(":b:", text.getHorizontalIndex() == 0);
	text.setHorizontalIndex(1);
	assertTrue(":c:", text.getHorizontalIndex() == 0);

	text.setText("Line0");
	assertTrue(":d:", text.getHorizontalIndex() == 0);
	text.setHorizontalIndex(-1);
	assertTrue(":e:", text.getHorizontalIndex() == 0);
	text.setHorizontalIndex(1);
	assertTrue(":f:", text.getHorizontalIndex() == 1);
	text.setHorizontalIndex(500);
	assertTrue(":g:", text.getHorizontalIndex() > 0);
	text.setHorizontalIndex(-1);
	assertTrue(":h:", text.getHorizontalIndex() == 0);
	text.setHorizontalIndex(1);
	assertTrue(":i:", text.getHorizontalIndex() == 1);
	// make sure the widget can be scrolled
	shell.open();
	text.setSize(10, 50);
	text.setText("Line0");
	text.setHorizontalIndex(1);
	assertTrue(":j:", text.getHorizontalIndex() == 1);
}

@Test
public void test_getHorizontalPixel() {
	assertTrue(":a:", text.getHorizontalPixel() == 0);
	text.setHorizontalIndex(-1);
	assertTrue(":b:", text.getHorizontalPixel() == 0);
	text.setHorizontalIndex(1);
	assertTrue(":c:", text.getHorizontalPixel() == 0);

	text.setText("Line0");
	assertTrue(":d:", text.getHorizontalPixel() == 0);
	text.setHorizontalIndex(-1);
	assertTrue(":e:", text.getHorizontalPixel() == 0);
	text.setHorizontalIndex(1);
	assertTrue(":f:", text.getHorizontalPixel() > 0);
	text.setHorizontalIndex(-1);
	assertTrue(":g:", text.getHorizontalPixel() == 0);
	text.setHorizontalIndex(1);
	assertTrue(":h:", text.getHorizontalPixel() > 0);
	// make sure the widget can be scrolled
	shell.open();
	text.setSize(10, 50);
	text.setText("Line0");
	text.setHorizontalIndex(1);
	assertTrue(":i:", text.getHorizontalPixel() > 0);
}

@Test
public void test_getKeyBindingI() {
	assertTrue(":a:", text.getKeyBinding(SWT.DEL) == ST.DELETE_NEXT);
	text.setKeyBinding(SWT.DEL, ST.LINE_UP);
	assertTrue(":b:", text.getKeyBinding(SWT.DEL) == ST.LINE_UP);
	text.setKeyBinding(SWT.DEL | SWT.MOD2, ST.SELECT_PAGE_UP);
	assertTrue(":c:", text.getKeyBinding(SWT.DEL | SWT.MOD2) == ST.SELECT_PAGE_UP);
	text.setKeyBinding(SWT.DEL | SWT.MOD2, ST.PAGE_UP);
	assertTrue(":d:", text.getKeyBinding(SWT.DEL | SWT.MOD2) == ST.PAGE_UP);
	assertTrue(":e:", text.getKeyBinding(-1) == SWT.NULL);
	assertTrue(":f:", text.getKeyBinding(SWT.F2) == SWT.NULL);
}

@Test
public void test_getCharCount() {
	assertTrue(":a:", text.getCharCount() == 0);
	text.setText("Line0");
	assertTrue(":b:", text.getCharCount() == 5);
	text.setText("");
	assertTrue(":c:", text.getCharCount() == 0);
	text.setText("Line0\n");
	assertTrue(":d:", text.getCharCount() == 6);
}

@Test
public void test_getLineBackgroundI() {
	String textString = "L1\nL2\nL3\nL4";
	text.setText(textString);
	assertNull(":1:", text.getLineBackground(0));
	text.setLineBackground(1,1,getColor(YELLOW));
	text.setLineBackground(2,1,getColor(BLUE));
	assertTrue(":1:", text.getLineBackground(1) == getColor(YELLOW));
	assertTrue(":1:", text.getLineBackground(2) == getColor(BLUE));
}

@Test
public void test_getLineCount() {
	String delimiterString = "\r\n";
	assertTrue(":a:", text.getLineCount()==1);
	text.append("dddasd" + delimiterString);
	assertTrue(":b:", text.getLineCount()==2);
	text.append("ddasdasdasdasd" + delimiterString);
	assertTrue(":c:", text.getLineCount()==3);


	text.setText("01234567890");
	text.setSelection(4);
	assertTrue(":a:", text.getLineCount()==1);
	text.insert(delimiterString);
	assertTrue(":b:", text.getLineCount()==2);
}

@Test
public void test_getLineAtOffsetI() {
	boolean exceptionThrown = false;

	assertTrue(":a:", text.getLineAtOffset(0) == 0);
	try {
		text.getLineAtOffset(-1);
	}
	catch (IllegalArgumentException e) {
		exceptionThrown = true;
	}
	assertTrue(":b:", exceptionThrown == true);
	exceptionThrown = false;

	try {
		text.getLineAtOffset(100);
	}
	catch (IllegalArgumentException e) {
		exceptionThrown = true;
	}
	assertTrue(":c:", exceptionThrown == true);
	exceptionThrown = false;

	text.setText("Line0\r\n");
	assertTrue(":d:", text.getLineAtOffset(4) == 0);
	assertTrue(":e:", text.getLineAtOffset(5) == 0);
	assertTrue(":f:", text.getLineAtOffset(6) == 0);
	assertTrue(":g:", text.getLineAtOffset(7) == 1);
	try {
		text.getLineAtOffset(8);
	}
	catch (IllegalArgumentException e) {
		exceptionThrown = true;
	}
	assertTrue(":h:", exceptionThrown == true);
	exceptionThrown = false;
}

@Test
public void test_getLineDelimiter() {
	final String lineDelimiter = "\n";
	StyledTextContent content = text.getContent();

	assertEquals(content.getLineDelimiter(), text.getLineDelimiter());

	content = new StyledTextContent() {
		@Override
		public void addTextChangeListener(TextChangeListener listener) {
		}
		@Override
		public int getCharCount() {
			return 0;
		}
		@Override
		public String getLine(int lineIndex) {
			return "";
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
			return lineDelimiter;
		}
		@Override
		public int getOffsetAtLine(int lineIndex) {
			return 0;
		}
		@Override
		public String getTextRange(int start, int length) {
			return "";
		}
		@Override
		public void removeTextChangeListener(TextChangeListener listener) {
		}
		@Override
		public void replaceTextRange(int start, int replaceLength, String text) {
		}
		@Override
		public void setText(String text) {
		}
	};
	text.setContent(content);
	assertEquals(lineDelimiter, text.getLineDelimiter());
}

@Test
public void test_getLineHeight() {
	assertTrue(":a:", text.getLineHeight() > 0);
}

@Test
public void test_getLineIndex () {
	test_getLineIndex(text);
	StyledText text2 = new StyledText(shell, SWT.WRAP);
	test_getLineIndex(text2);
	text2.dispose();
}

void test_getLineIndex (StyledText text) {
	int lineHeight = text.getLineHeight();

	text.setText("Line0\nLine1\nLine2");
	text.setSize(400, lineHeight * 3);
	assertEquals(0, text.getLineIndex(-100));
	assertEquals(0, text.getLineIndex(-1));
	assertEquals(0, text.getLineIndex(0));
	assertEquals(0, text.getLineIndex(lineHeight / 2));
	assertEquals(0, text.getLineIndex(lineHeight - 1));
	assertEquals(1, text.getLineIndex(lineHeight));
	assertEquals(1, text.getLineIndex(lineHeight + lineHeight / 2));
	assertEquals(1, text.getLineIndex(2 * lineHeight - 1));
	assertEquals(2, text.getLineIndex(2 * lineHeight));
	assertEquals(2, text.getLineIndex(2 * lineHeight + lineHeight / 2));
	assertEquals(2, text.getLineIndex(3 * lineHeight - 1));
	assertEquals(2, text.getLineIndex(3 * lineHeight));
	assertEquals(2, text.getLineIndex(10 * lineHeight));

	text.setSize(400, lineHeight);
	text.setTopIndex(1);
	assertEquals(0, text.getLineIndex(-10 * lineHeight));
	assertEquals(0, text.getLineIndex(-lineHeight));
	assertEquals(0, text.getLineIndex(-lineHeight / 2));
	assertEquals(1, text.getLineIndex(0));
	assertEquals(1, text.getLineIndex(lineHeight - 1));
	assertEquals(2, text.getLineIndex(lineHeight));
	assertEquals(2, text.getLineIndex(2 * lineHeight));
	assertEquals(2, text.getLineIndex(10 * lineHeight));


	text.setTopIndex(2);
	assertEquals(0, text.getLineIndex(-10 * lineHeight));
	assertEquals(0, text.getLineIndex(-2 * lineHeight));
	assertEquals(0, text.getLineIndex(-lineHeight - 1));
	assertEquals(1, text.getLineIndex(-lineHeight));
	assertEquals(1, text.getLineIndex(-1));
	assertEquals(2, text.getLineIndex(0));
	assertEquals(2, text.getLineIndex(lineHeight - 1));
	assertEquals(2, text.getLineIndex(lineHeight));
	assertEquals(2, text.getLineIndex(10 * lineHeight));


	text.setTopIndex(0);
	text.setSize(400, 0);
	assertEquals(0, text.getLineIndex(-100));
	assertEquals(0, text.getLineIndex(-1));
	assertEquals(0, text.getLineIndex(0));
	assertEquals(0, text.getLineIndex(lineHeight / 2));
	assertEquals(0, text.getLineIndex(lineHeight - 1));
	assertEquals(1, text.getLineIndex(lineHeight));
	assertEquals(1, text.getLineIndex(lineHeight + lineHeight / 2));
	assertEquals(1, text.getLineIndex(2 * lineHeight - 1));
	assertEquals(2, text.getLineIndex(2 * lineHeight));
	assertEquals(2, text.getLineIndex(2 * lineHeight + lineHeight / 2));
	assertEquals(2, text.getLineIndex(3 * lineHeight - 1));
	assertEquals(2, text.getLineIndex(3 * lineHeight));
	assertEquals(2, text.getLineIndex(10 * lineHeight));

	text.setTopPixel(3 * lineHeight);
	assertEquals(0, text.getLineIndex(-3 * lineHeight -100));
	assertEquals(0, text.getLineIndex(-3 * lineHeight));
	assertEquals(0, text.getLineIndex(-2 * lineHeight - 1));
	assertEquals(1, text.getLineIndex(-2 * lineHeight));
	assertEquals(1, text.getLineIndex(-lineHeight - 1));
	assertEquals(2, text.getLineIndex(-lineHeight));
	assertEquals(2, text.getLineIndex(0));
	assertEquals(2, text.getLineIndex(100));

	text.setTopPixel(0);
	text.setText("");
	assertEquals(0, text.getLineIndex(-1));
	assertEquals(0, text.getLineIndex(-100));
	assertEquals(0, text.getLineIndex(0));
	assertEquals(0, text.getLineIndex(1));
	assertEquals(0, text.getLineIndex(100));
}

@Test
public void test_getLinePixel () {
	test_getLinePixel(text);
	StyledText text2 = new StyledText(shell, SWT.WRAP);
	test_getLinePixel(text2);
	text2.dispose();
}

void test_getLinePixel(StyledText text) {
	int lineHeight = text.getLineHeight();

	text.setText("Line0\nLine1\nLine2");
	text.setSize(400, lineHeight * 3);
	assertEquals(0, text.getLinePixel(-100));
	assertEquals(0, text.getLinePixel(0));
	assertEquals(lineHeight, text.getLinePixel(1));
	assertEquals(2 * lineHeight, text.getLinePixel(2));
	assertEquals(3 * lineHeight, text.getLinePixel(3));
	assertEquals(3 * lineHeight, text.getLinePixel(100));

	text.setSize(400, 0);
	assertEquals(0, text.getLinePixel(-100));
	assertEquals(0, text.getLinePixel(0));
	assertEquals(lineHeight, text.getLinePixel(1));
	assertEquals(2 * lineHeight, text.getLinePixel(2));
	assertEquals(3 * lineHeight, text.getLinePixel(3));
	assertEquals(3 * lineHeight, text.getLinePixel(100));

	text.setSize(400, lineHeight);
	text.setTopIndex(1);
	assertEquals(-lineHeight, text.getLinePixel(-100));
	assertEquals(-lineHeight, text.getLinePixel(0));
	assertEquals(0, text.getLinePixel(1));
	assertEquals(lineHeight, text.getLinePixel(2));
	assertEquals(2 * lineHeight, text.getLinePixel(3));
	assertEquals(2 * lineHeight, text.getLinePixel(100));

	text.setSize(400, 0);
	text.setTopPixel(3 * lineHeight);
	assertEquals(-3 * lineHeight, text.getLinePixel(-100));
	assertEquals(-3 * lineHeight, text.getLinePixel(0));
	assertEquals(-2 * lineHeight, text.getLinePixel(1));
	assertEquals(-lineHeight, text.getLinePixel(2));
	assertEquals(0, text.getLinePixel(3));
	assertEquals(0, text.getLinePixel(100));

	text.setTopPixel(0);
	text.setText("");
	assertEquals(0, text.getLinePixel(-10));
	assertEquals(0, text.getLinePixel(0));
	assertEquals(lineHeight, text.getLinePixel(1));
	assertEquals(lineHeight, text.getLinePixel(10));
}

@Test
public void test_getLocationAtOffsetI(){
	// copy from StyledText, has to match value used by StyledText
	final int XINSET = isBidiCaret() ? 2 : 0;

	assertTrue(":a:", text.getLocationAtOffset(0).equals(new Point(XINSET, 0)));
	try {
		text.getLocationAtOffset(-1);
		fail("No exception thrown for offset == -1");
	}
	catch (IllegalArgumentException e) {
	}

	try {
		text.getLocationAtOffset(100);
		fail("No exception thrown for illegal offset argument");
	}
	catch (IllegalArgumentException e) {
	}

	text.setText("Line0\r\nLine1");
	assertTrue(":d:", text.getLocationAtOffset(4).x > 0 && text.getLocationAtOffset(4).y == 0);
	assertTrue(":e:", text.getLocationAtOffset(6).x > 0 && text.getLocationAtOffset(6).y == 0);
	// x location will == StyledText x inset on bidi platforms
	assertTrue(":f:", text.getLocationAtOffset(7).x == XINSET && text.getLocationAtOffset(7).y > 0);
	try {
		text.getLocationAtOffset(13);
		fail("No exception thrown for illegal offset argument");
	}
	catch (IllegalArgumentException e) {
	}

	text.setTopIndex(1);
	assertTrue(":h:", text.getLocationAtOffset(4).x > 0 && text.getLocationAtOffset(4).y < 0);
	// x location will == StyledText x inset on bidi platforms
	assertTrue(":i:", text.getLocationAtOffset(7).x == XINSET && text.getLocationAtOffset(7).y == 0);

	text.setHorizontalIndex(1);
	assertTrue(":j:", text.getLocationAtOffset(0).x < 0 && text.getLocationAtOffset(0).y < 0);
	assertTrue(":k:", text.getLocationAtOffset(7).x < 0 && text.getLocationAtOffset(7).y == 0);
}
@Test
public void test_getOffsetAtLineI() {
	boolean exceptionThrown = false;

	assertEquals(":a:", 0, text.getOffsetAtLine(0));
	try {
		text.getOffsetAtLine(-1);
	}
	catch (IllegalArgumentException e) {
		exceptionThrown = true;
	}
	assertTrue(":b:", exceptionThrown);
	exceptionThrown = false;

	try {
		text.getOffsetAtLine(100);
	}
	catch (IllegalArgumentException e) {
		exceptionThrown = true;
	}
	assertTrue(":c:", exceptionThrown);
	exceptionThrown = false;

	text.setText("Line0\r\n");
	assertEquals(":d:", 0, text.getOffsetAtLine(0));
	assertEquals(":e:", 7, text.getOffsetAtLine(1));

	try {
		text.getOffsetAtLine(2);
	}
	catch (IllegalArgumentException e) {
		exceptionThrown = true;
	}
	assertTrue(":f:", exceptionThrown);
	exceptionThrown = false;

	text.setText("");
	assertEquals(":g:", 0, text.getOffsetAtLine(0));
}
@SuppressWarnings("deprecation")
@Test
public void test_getOffsetAtLocationLorg_eclipse_swt_graphics_Point() {
	boolean exceptionThrown = false;
	Point location;
	final int XINSET = isBidiCaret() ? 2 : 0;

	assertTrue(":a:", text.getOffsetAtLocation(new Point(XINSET, 0)) == 0);
	try {
		text.getOffsetAtLocation(new Point(-1, 0));
	}
	catch (IllegalArgumentException e) {
		exceptionThrown = true;
	}
	assertTrue(":b:", exceptionThrown);
	exceptionThrown = false;

	try {
		text.getOffsetAtLocation(new Point(0, -1));
	}
	catch (IllegalArgumentException e) {
		exceptionThrown = true;
	}
	assertTrue(":c:", exceptionThrown);
	exceptionThrown = false;

	text.setText("Line0\r\nLine1");
	location = text.getLocationAtOffset(5);
	assertTrue(":d:", text.getOffsetAtLocation(new Point(10, 0)) > 0);
	assertTrue(":e:", text.getOffsetAtLocation(new Point(location.x - 1, 0)) == 5);
	location = text.getLocationAtOffset(7);
	assertTrue(":f:", text.getOffsetAtLocation(location) == 7);
	try {
		text.getOffsetAtLocation(new Point(100, 0));
	}
	catch (IllegalArgumentException e) {
		exceptionThrown = true;
	}
	assertTrue(":g:", exceptionThrown);
	exceptionThrown = false;

	try {
		text.getOffsetAtLocation(new Point(100, 50));
	}
	catch (IllegalArgumentException e) {
		exceptionThrown = true;
	}
	assertTrue(":h:", exceptionThrown);
	exceptionThrown = false;

	text.setTopIndex(1);
	assertTrue(":i:", text.getOffsetAtLocation(new Point(XINSET, -5)) == 0);
	assertTrue(":j:", text.getOffsetAtLocation(new Point(XINSET, 0)) == 7);

	text.setHorizontalIndex(1);
	assertTrue(":k:", text.getOffsetAtLocation(new Point(XINSET + -5, -5)) == 0);
	assertTrue(":l:", text.getOffsetAtLocation(new Point(XINSET + -5, 0)) == 7);

	// 1GL4ZVE
	assertTrue(":m:", text.getOffsetAtLocation(text.getLocationAtOffset(2)) == 2);
	text.setHorizontalIndex(0);
	assertTrue(":n:", text.getOffsetAtLocation(text.getLocationAtOffset(2)) == 2);
}

@Test
public void test_getOffsetAtPointLorg_eclipse_swt_graphics_Point() {
	Point location;
	final int XINSET = isBidiCaret() ? 2 : 0;

	assertEquals(":a:",  0, text.getOffsetAtPoint(new Point(XINSET, 0)));
	assertEquals(":b:", -1, text.getOffsetAtPoint(new Point(-1, 0)));
	assertEquals(":c:", -1, text.getOffsetAtPoint(new Point(0, -1)));

	text.setText("Line0\r\nLine1");
	location = text.getLocationAtOffset(5);
	assertTrue  (":d:", text.getOffsetAtPoint(new Point(10, 0)) > 0);
	assertEquals(":e:",  5, text.getOffsetAtPoint(new Point(location.x - 1, 0)));
	location = text.getLocationAtOffset(7);
	assertEquals(":f:",  7, text.getOffsetAtPoint(location));
	assertEquals(":g:", -1, text.getOffsetAtPoint(new Point(100, 0)));
	assertEquals(":h:", -1, text.getOffsetAtPoint(new Point(100, 50)));

	text.setTopIndex(1);
	assertEquals(":i:",  0, text.getOffsetAtPoint(new Point(XINSET, -5)));
	assertEquals(":j:",  7, text.getOffsetAtPoint(new Point(XINSET, 0)));

	text.setHorizontalIndex(1);
	assertEquals(":k:",  0, text.getOffsetAtPoint(new Point(XINSET + -5, -5)));
	assertEquals(":l:",  7, text.getOffsetAtPoint(new Point(XINSET + -5, 0)));

	// 1GL4ZVE
	assertEquals(":m:",  2, text.getOffsetAtPoint(text.getLocationAtOffset(2)));
	text.setHorizontalIndex(0);
	assertEquals(":n:",  2, text.getOffsetAtPoint(text.getLocationAtOffset(2)));
}

void testStyles (String msg, int[] resultRanges, int[] expectedRanges, StyleRange[] resultStyles, StyleRange[] expectedStyles) {
	assertNotNull("resultRanges is null on: " + msg, resultRanges);
	assertNotNull("expectedRanges is null on: " + msg, expectedRanges);
	assertNotNull("resultStyles is null on: " + msg, resultStyles);
	assertNotNull("expectedStyles is null on: " + msg, expectedStyles);
	assertEquals("result ranges and styles length don't match on: " + msg, resultRanges.length, resultStyles.length * 2);
	assertEquals("expected ranges and styles length don't match on: " + msg, expectedRanges.length, expectedStyles.length * 2);
	assertArrayEquals("expected and result ranges are differnt on: " + msg, expectedRanges, resultRanges);
	assertArrayEquals("expected and result styles are differnt on: " + msg, expectedStyles, resultStyles);
}

@Test
public void test_getRanges(){
	StyleRange style0 = new StyleRange();
	style0.rise = 10;
	StyleRange style1 = new StyleRange();
	style1.rise = 5;
	StyleRange style2 = new StyleRange();
	style2.rise = 30;
	StyleRange[] expectedStyles;
	int[] expectedRanges;

	// tests using the new API
	text.setText ("");
	text.setText ("0123456789");
	text.setStyleRanges(0, 0, new int[] {0, 10}, new StyleRange[] {style0});
	expectedStyles = new StyleRange[] {style0};
	expectedRanges = new int[] {0, 10};
	testStyles("Test 1", text.getRanges(0,10), expectedRanges, text.getStyleRanges(false), expectedStyles);

	text.setText ("");
	text.setText ("0123456789");
	text.setStyleRanges(0, 0, new int[] {3, 4}, new StyleRange[] {style1});
	expectedStyles = new StyleRange[] {style1};
	expectedRanges = new int[] {3, 4};
	testStyles("Test 2", text.getRanges(0,10), expectedRanges, text.getStyleRanges(false), expectedStyles);

	text.setText ("");
	text.setText ("0123456789");
	text.setStyleRanges(0, 0, new int[] {0, 10}, new StyleRange[] {style0});
	text.setStyleRanges(0, 0, new int[] {3, 4}, new StyleRange[] {style1});
	expectedStyles = new StyleRange[] {style0, style1, style0};
	expectedRanges = new int[] {0, 3, 3, 4, 7, 3};
	testStyles("Test 3", text.getRanges(0,10), expectedRanges, text.getStyleRanges(false), expectedStyles);

	text.setText ("");
	text.setText ("0123456789");
	text.setStyleRanges(0, 0, new int[] {0, 10}, new StyleRange[] {style0});
	text.setStyleRanges(0, 0, new int[] {3, 4}, new StyleRange[] {style1});
	text.setStyleRanges(0, 0, new int[] {1, 4}, new StyleRange[] {style2});
	expectedStyles = new StyleRange[] {style0, style2, style1, style0};
	expectedRanges = new int[] {0, 1, 1, 4, 5, 2, 7, 3};
	testStyles("Test 4", text.getRanges(0,10), expectedRanges, text.getStyleRanges(false), expectedStyles);

	text.setText ("");
	text.setText ("0123456789");
	text.setStyleRanges(0, 0, new int[] {0, 10}, new StyleRange[] {style0});
	text.setStyleRanges(0, 0, new int[] {3, 4}, new StyleRange[] {style1});
	text.setStyleRanges(0, 0, new int[] {1, 8}, new StyleRange[] {style2});
	expectedStyles = new StyleRange[] {style0, style2, style0};
	expectedRanges = new int[] {0, 1, 1, 8, 9, 1};
	testStyles("Test 5", text.getRanges(0,10), expectedRanges, text.getStyleRanges(false), expectedStyles);

	text.setText ("");
	text.setText ("0123456789");
	text.setStyleRanges(0, 0, new int[] {0, 10}, new StyleRange[] {style0});
	text.setStyleRanges(0, 0, new int[] {3, 4}, new StyleRange[] {style1});
	text.setStyleRanges(0, 0, new int[] {0, 5}, new StyleRange[] {style2});
	expectedStyles = new StyleRange[] {style2, style1, style0};
	expectedRanges = new int[] {0, 5, 5, 2, 7, 3};
	testStyles("Test 6", text.getRanges(0,10), expectedRanges, text.getStyleRanges(false), expectedStyles);

	text.setText ("");
	text.setText ("0123456789");
	text.setStyleRanges(0, 0, new int[] {0, 10}, new StyleRange[] {style0});
	text.setStyleRanges(0, 0, new int[] {3, 4}, new StyleRange[] {style1});
	text.setStyleRanges(0, 0, new int[] {1, 6}, new StyleRange[] {style2});
	expectedStyles = new StyleRange[] {style0, style2, style0};
	expectedRanges = new int[] {0, 1, 1, 6, 7, 3};
	testStyles("Test 7", text.getRanges(0,10), expectedRanges, text.getStyleRanges(false), expectedStyles);

	text.setText ("");
	text.setText ("0123456789");
	text.setStyleRanges(0, 0, new int[] {0, 10}, new StyleRange[] {style0});
	text.setStyleRanges(0, 0, new int[] {3, 4}, new StyleRange[] {style1});
	text.setStyleRanges(0, 0, new int[] {3, 4}, new StyleRange[] {style2});
	expectedStyles = new StyleRange[] {style0, style2, style0};
	expectedRanges = new int[] {0, 3, 3, 4, 7, 3};
	testStyles("Test 8", text.getRanges(0,10), expectedRanges, text.getStyleRanges(false), expectedStyles);

	text.setText ("");
	text.setText ("0123456789");
	text.setStyleRanges(0, 0, new int[] {0, 10}, new StyleRange[] {style0});
	text.setStyleRanges(0, 0, new int[] {3, 4}, new StyleRange[] {style1});
	text.setStyleRanges(0, 0, new int[] {0, 3}, new StyleRange[] {style2});
	expectedStyles = new StyleRange[] {style2, style1, style0};
	expectedRanges = new int[] {0, 3, 3, 4, 7, 3};
	testStyles("Test 9", text.getRanges(0,10), expectedRanges, text.getStyleRanges(false), expectedStyles);

	text.setText ("");
	text.setText ("0123456789");
	text.setStyleRanges(0, 0, new int[] {0, 10}, new StyleRange[] {style0});
	text.setStyleRanges(0, 0, new int[] {3, 4}, new StyleRange[] {style1});
	text.setStyleRanges(0, 0, new int[] {7, 3}, new StyleRange[] {style2});
	expectedStyles = new StyleRange[] {style0, style1, style2};
	expectedRanges = new int[] {0, 3, 3, 4, 7, 3};
	testStyles("Test 10", text.getRanges(0,10), expectedRanges, text.getStyleRanges(false), expectedStyles);

	text.setText ("");
	text.setText ("0123456789");
	text.setStyleRanges(0, 0, new int[] {0, 10}, new StyleRange[] {style0});
	text.setStyleRanges(0, 0, new int[] {3, 4}, new StyleRange[] {style1});
	text.setStyleRanges(0, 0, new int[] {4, 2}, new StyleRange[] {style2});
	expectedStyles = new StyleRange[] {style0, style1, style2, style1, style0};
	expectedRanges = new int[] {0, 3, 3, 1, 4, 2, 6, 1, 7, 3};
	testStyles("Test 11", text.getRanges(0,10), expectedRanges, text.getStyleRanges(false), expectedStyles);

	text.setText ("");
	text.setText ("0123456789");
	text.setStyleRanges(0, 0, new int[] {0, 10}, new StyleRange[] {style0});
	text.setStyleRanges(0, 0, new int[] {3, 4}, new StyleRange[] {style1});
	text.setStyleRanges(0, 0, new int[] {2, 6}, new StyleRange[] {style2});
	expectedStyles = new StyleRange[] {style0, style2, style0};
	expectedRanges = new int[] {0, 2, 2, 6, 8, 2};
	testStyles("Test 12", text.getRanges(0,10), expectedRanges, text.getStyleRanges(false), expectedStyles);

	text.setText ("");
	text.setText ("0123456789");
	text.setStyleRanges(0, 0, new int[] {0, 10}, new StyleRange[] {style0});
	text.setStyleRanges(0, 0, new int[] {0, 10}, new StyleRange[] {style1});
	expectedStyles = new StyleRange[] {style1};
	expectedRanges = new int[] {0, 10};
	testStyles("Test 13", text.getRanges(0,10), expectedRanges, text.getStyleRanges(false), expectedStyles);

	text.setText ("");
	text.setText ("0123456789AB");
	text.setStyleRanges(0, 0, new int[] {1, 3}, new StyleRange[] {style0});
	text.setStyleRanges(0, 0, new int[] {5, 1}, new StyleRange[] {style1});
	text.setStyleRanges(0, 0, new int[] {7, 1}, new StyleRange[] {style0});
	text.setStyleRanges(0, 0, new int[] {9, 2}, new StyleRange[] {style1});
	expectedStyles = new StyleRange[] {style0, style1, style0, style1};
	expectedRanges = new int[] {1,3, 5,1, 7,1, 9,2};
	testStyles("Test 14", text.getRanges(0,12), expectedRanges, text.getStyleRanges(false), expectedStyles);

	text.setText ("");
	text.setText ("0123456789AB");
	text.setStyleRanges(0, 0, new int[] {1, 3}, new StyleRange[] {style0});
	text.setStyleRanges(0, 0, new int[] {5, 1}, new StyleRange[] {style1});
	text.setStyleRanges(0, 0, new int[] {7, 1}, new StyleRange[] {style0});
	text.setStyleRanges(0, 0, new int[] {9, 2}, new StyleRange[] {style1});
	text.setStyleRanges(0, 0, new int[] {2, 8}, new StyleRange[] {style2});
	expectedStyles = new StyleRange[] {style0, style2, style1};
	expectedRanges = new int[] {1,1, 2,8, 10,1};
	testStyles("Test 15", text.getRanges(0,12), expectedRanges, text.getStyleRanges(false), expectedStyles);

	//tests mixing the old API and the new API
	text.setText ("");
	text.setText ("0123456789");
	text.setStyleRanges(new StyleRange[]{new StyleRange(3,4,null,null,SWT.BOLD)});
	expectedStyles = new StyleRange[]{new StyleRange(3,4,null,null,SWT.BOLD)};
	expectedRanges = new int[] {3, 4};
	testStyles("Test 16", text.getRanges(0,10), expectedRanges, text.getStyleRanges(false), expectedStyles);


	//test the merging code
	text.setText ("");
	text.setText ("0123456789");
	text.setStyleRanges(new int[] {1, 3, 6, 3}, new StyleRange[] {style0, style0});
	text.setStyleRanges(0, 0, new int[] {4, 2}, new StyleRange[] {style0});
	expectedStyles = new StyleRange[] {style0};
	expectedRanges = new int[] {1, 8};
	testStyles("Test 17", text.getRanges(0,10), expectedRanges, text.getStyleRanges(false), expectedStyles);

	text.setText ("");
	text.setText ("0123456789");
	text.setStyleRanges(new int[] {1, 3, 6, 3}, new StyleRange[] {style0, style0});
	text.setStyleRanges(0, 0, new int[] {2, 6}, new StyleRange[] {style0});
	expectedStyles = new StyleRange[] {style0};
	expectedRanges = new int[] {1, 8};
	testStyles("Test 18", text.getRanges(0,10), expectedRanges, text.getStyleRanges(false), expectedStyles);

	text.setText ("");
	text.setText ("0123456789");
	text.setStyleRanges(new int[] {1, 3, 6, 3}, new StyleRange[] {style0, style0});
	text.setStyleRanges(3, 4, new int[] {0, 1, 9, 1}, new StyleRange[] {style0, style0});
	expectedStyles = new StyleRange[] {style0, style0};
	expectedRanges = new int[] {0, 3, 7, 3};
	testStyles("Test 19", text.getRanges(0,10), expectedRanges, text.getStyleRanges(false), expectedStyles);

	//tests mixing the old API and the new API
	text.setText ("");
	text.setText ("0123456789");
	text.setStyleRanges(new StyleRange[]{new StyleRange(3,4,null,null,SWT.BOLD)});
	expectedStyles = new StyleRange[]{};
	expectedRanges = new int[] {};
	testStyles("Test 20", text.getRanges(3,0), expectedRanges, text.getStyleRanges(3, 0, false), expectedStyles);

	//bug 250859 (getRanges)
	text.setText ("");
	text.setText ("The Eclipse Foundation is currently going through the exercise");
	text.setStyleRanges(new int[] {12, 10, 36, 5}, new StyleRange[] {style0, style1});
	expectedStyles = new StyleRange[] {style0};
	expectedRanges = new int[] {12, 10};
	testStyles("Test 21 (bug 250859)", text.getRanges(12, 10), expectedRanges, text.getStyleRanges(12, 10, false), expectedStyles);

	//bug 250193
	text.setText ("");
	text.setText("The Eclipse Foundation is currently going through the exercise");
	StyleRange sr1 = new StyleRange();
	sr1.underline = true;
	StyleRange sr2 = new StyleRange();
	sr2.strikeout = true;
	sr1.start = 12;
	sr1.length = 10;
	sr2.start = 36;
	sr2.length = 5;
	text.setStyleRange(sr1);
	text.setStyleRange(sr2);
	text.replaceTextRange(12, 10, "");
	expectedStyles = new StyleRange[] {sr2};
	expectedRanges = new int[] {26, 5};
	testStyles("Test 22 (bug 250193)", text.getRanges(26, 5), expectedRanges, text.getStyleRanges(26, 5, false), expectedStyles);

	//bug 212851 (getStyleRanges)
	text.setText("");
	text.setText("line0\nline1\nline2");
	text.setStyleRanges(new int[] {0,2,2,2,4,4,13,3}, new StyleRange[] {style0, style1, style2, style0});
	expectedRanges = new int[] {6, 2};
	expectedStyles = new StyleRange[] {style2};
	testStyles("Test 23 (bug 212851 - getRanges)", text.getRanges(6, 6), expectedRanges, text.getStyleRanges(6, 6, false), expectedStyles);
	StyleRange[] styles = text.getStyleRanges(6, 6, true);
	int[] ranges = new int[styles.length * 2];
	for (int i = 0; i < ranges.length; i+=2) {
		ranges[i] = styles[i/2].start;
		ranges[i+1] = styles[i/2].length;
	}
	testStyles("Test 24 (bug 212851 - getStyleRanges)", ranges, expectedRanges, text.getStyleRanges(6, 6, false), expectedStyles);
	expectedRanges = new int[] {6, 2, 13, 1};
	expectedStyles = new StyleRange[] {style2, style0};
	testStyles("Test 25 ", text.getRanges(6, 8), expectedRanges, text.getStyleRanges(6, 8, false), expectedStyles);
	styles = text.getStyleRanges(6, 8, true);
	ranges = new int[styles.length * 2];
	for (int i = 0; i < ranges.length; i+=2) {
		ranges[i] = styles[i/2].start;
		ranges[i+1] = styles[i/2].length;
	}
	testStyles("Test 26 ", ranges, expectedRanges, text.getStyleRanges(6, 8, false), expectedStyles);
}

@Test
public void test_getSelection(){
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
@Test
public void test_getSelectionBackground() {
	assertNotNull(":1:", text.getSelectionBackground());
	text.setSelectionBackground(getColor(YELLOW));
	assertTrue(":1:", text.getSelectionBackground() ==  getColor(YELLOW));
}
@Test
public void test_getSelectionForeground() {
	assertNotNull(":1:", text.getSelectionForeground());
	text.setSelectionForeground(getColor(RED));
	assertTrue(":1:", text.getSelectionForeground() ==  getColor(RED));
}
@Test
public void test_getSelectionRange() {
	String testText = "Line1\r\nLine2";
	int invalidRanges [][] = {{-1, 0}, {-1, -1}, {100, 1}, {100, -1}, {12, 1}, {11, 2}, {2, -3}, {50, -1}};
	int selectionRanges [][] = {{0, 1}, {0, 0}, {2, 3}, {12, 0}, {2, -2}, {5, -1}};

	for (int[] invalidRange : invalidRanges) {
		int start = invalidRange[0];
		int length = invalidRange[1];

		try {
			text.setSelectionRange(start, length);
		}
		catch (IllegalArgumentException e) {
			fail("should not throw exception for out of range");
		}
	}

	text.setSelectionRange(0, 0);
	assertTrue(":b:", text.getSelectionRange().x == 0 && text.getSelectionRange().y == 0);
	text.setText(testText);
	for (int i = 0; i < selectionRanges.length; i++) {
		int start = selectionRanges[i][0];
		int length = selectionRanges[i][1];
		text.setSelectionRange(start, length);
		if (length < 0) {
			start += length;
			length *= -1;
			assertEquals(":c:a:" + i, start, text.getCaretOffset());
		}
		else {
			assertEquals(":c:a:" + i, start + length, text.getCaretOffset());
		}
		assertTrue(":c:" + i, text.getSelectionRange().x == start && text.getSelectionRange().y == length);
	}

	for (int[] invalidRange : invalidRanges) {
		int start = invalidRange[0];
		int length = invalidRange[1];

		try {
			text.setSelectionRange(start, length);
		}
		catch (IllegalArgumentException e) {
			fail("should not throw exception for out of range");
		}
	}
}

@Test
public void test_getSelectionCount(){
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
@Test
public void test_getSelectionText() {
	String testText = "Line1\r\nLine2";
	int selectionRanges [][] = {{0, 1}, {0, 0}, {2, 3}, {12, 0}};

	text.setSelectionRange(0, 0);
	assertEquals(":b:", "", text.getSelectionText());
	text.setText(testText);
	for (int i = 0; i < selectionRanges.length; i++) {
		int start = selectionRanges[i][0];
		int length = selectionRanges[i][1];
		text.setSelectionRange(start, length);
		assertEquals(":c:" + i, testText.substring(start, start + length), text.getSelectionText());
	}
}
@Test
public void test_getStyleRangeAtOffsetI() {
	String line = "Line1\r\nLine2";
	int styleStart = 0;
	int styleLength = 5;
	int i;
	boolean exceptionThrown = false;
	StyleRange style = new StyleRange(styleStart, styleLength, getColor(BLUE), getColor(RED), SWT.BOLD);

	try {
		text.getStyleRangeAtOffset(0);
	}
	catch (IllegalArgumentException e) {
		exceptionThrown = true;
	}
	assertTrue("offset out of range no text", exceptionThrown);

	text.setText(line);
	exceptionThrown = false;
	try {
		text.getStyleRangeAtOffset(-1);
	}
	catch (IllegalArgumentException e) {
		exceptionThrown = true;
	}
	assertTrue("offset out of range negative", exceptionThrown);

	exceptionThrown = false;
	try {
		text.getStyleRangeAtOffset(line.length());
	}
	catch (IllegalArgumentException e) {
		exceptionThrown = true;
	}
	assertTrue("offset out of range positive", exceptionThrown);

	text.setStyleRange(style);
	style.length = 1;
	for (i = styleStart; i < styleStart + styleLength; i++) {
		style.start = i;
		assertEquals(style, text.getStyleRangeAtOffset(i));
	}
	assertEquals(null, text.getStyleRangeAtOffset(i));

	// test offset at line delimiter
	style = new StyleRange(5, 2, null, getColor(BLUE), SWT.NORMAL);
	text.setStyleRange(style);
	style.length = 1;
	assertEquals(style, text.getStyleRangeAtOffset(5));
	style.start = 6;
	assertEquals(style, text.getStyleRangeAtOffset(6));
	assertEquals(null, text.getStyleRangeAtOffset(10));
}
@Test
public void test_getStyleRanges() {
	text.setText("package test;\n/* Line 1\n * Line 2\n */\npublic class SimpleClass {\n}");
	text.setStyleRange(getStyle(0,7,BLUE,null));
	text.setStyleRange(getStyle(14,23,RED,null));
	text.setStyleRange(getStyle(38,6,BLUE,null));
	text.setStyleRange(getStyle(45,5,BLUE,null));
	text.replaceTextRange(14, 23, "\t/*Line 1\n\t * Line 2\n\t */");
	String newText = text.getTextRange(0, text.getCharCount());
	assertTrue(":1:", newText.equals("package test;\n\t/*Line 1\n\t * Line 2\n\t */\npublic class SimpleClass {\n}"));
	StyleRange[] styles = text.getStyleRanges();
	assertTrue(":1:", styles.length == 3);
	assertTrue(":1:", styles[0].equals(getStyle(0,7,BLUE,null)));
	assertTrue(":1:", styles[1].equals(getStyle(40,6,BLUE,null)));
	assertTrue(":1:", styles[2].equals(getStyle(47,5,BLUE,null)));
}
@Test
public void test_getStyleRangesII() {
	text.setText("0123456789");
//	0
//	 1234
//	 56
//	 78
//	9
	text.setStyleRange(getStyle(1,4,BLUE,null));
	text.setStyleRange(getStyle(5,2,RED,null));
	text.setStyleRange(getStyle(7,2,YELLOW,null));

	StyleRange[] styles = text.getStyleRanges(0,1);
	assertTrue(":1:", styles.length == 0);
	styles = text.getStyleRanges(0,5);
	assertTrue(":2:", styles.length == 1);
	assertTrue(":2:", styles[0].equals(getStyle(1,4,BLUE,null)));
	styles = text.getStyleRanges(7,3);
	assertTrue(":3:", styles.length == 1);
	assertTrue(":3:", styles[0].equals(getStyle(7,2,YELLOW,null)));
	styles = text.getStyleRanges(0,10);
	assertTrue(":4:", styles.length == 3);
	assertTrue(":4:", styles[0].equals(getStyle(1,4,BLUE,null)));
	assertTrue(":4:", styles[1].equals(getStyle(5,2,RED,null)));
	assertTrue(":4:", styles[2].equals(getStyle(7,2,YELLOW,null)));
	styles = text.getStyleRanges(0,4);
	assertTrue(":5:", styles.length == 1);
	assertTrue(":5:", styles[0].equals(getStyle(1,3,BLUE,null)));
	styles = text.getStyleRanges(2,6);
	assertTrue(":6:", styles.length == 3);
	assertTrue(":6:", styles[0].equals(getStyle(2,3,BLUE,null)));
	assertTrue(":6:", styles[1].equals(getStyle(5,2,RED,null)));
	assertTrue(":6:", styles[2].equals(getStyle(7,1,YELLOW,null)));

	text.setText("0123456789\r\nABCDEFGHIJKL");
	text.setStyleRange(getStyle(4,3,BLUE,null));
	text.setStyleRange(getStyle(7,5,RED,null));
	text.setStyleRange(getStyle(15,1,YELLOW,null));
	styles = text.getStyleRanges(15,1);
	assertTrue(":1a:", styles.length == 1);
	assertTrue(":1a:", styles[0].equals(getStyle(15,1,YELLOW,null)));
	styles = text.getStyleRanges(15,0);
	assertTrue(":2a:", styles.length == 0);
	styles = text.getStyleRanges(0,20);
	assertTrue(":3a:", styles.length == 3);
	assertTrue(":3a:", styles[0].equals(getStyle(4,3,BLUE,null)));
	assertTrue(":3a:", styles[1].equals(getStyle(7,5,RED,null)));
	assertTrue(":3a:", styles[2].equals(getStyle(15,1,YELLOW,null)));
	styles = text.getStyleRanges(8,2);
	assertTrue(":4a:", styles.length == 1);
	assertTrue(":4a:", styles[0].equals(getStyle(8,2,RED,null)));

}
@Test
public void test_getStyleRanges_Bug549110() {
	text.setText("abc\tdef\n123\t");
	StyleRange tabStyle = new StyleRange();
	tabStyle.start = 3;
	tabStyle.length = 1;
	tabStyle.metrics = new GlyphMetrics(0, 0, 50);
	text.setStyleRange(tabStyle);
	tabStyle = new StyleRange();
	tabStyle.start = 11;
	tabStyle.length = 1;
	tabStyle.metrics = new GlyphMetrics(0, 0, 100);
	text.setStyleRange(tabStyle);
	text.selectAll();
}
@Test
public void test_getTabs() {
	text.setTabs(1);
	assertTrue(":a:", text.getTabs() == 1);
	text.setTabs(8);
	assertTrue(":b:", text.getTabs() == 8);
	text.setText("Line\t1\r\n");
	assertTrue(":c:", text.getTabs() == 8);
	text.setTabs(7);
	assertTrue(":d:", text.getTabs() == 7);
}
@Test
public void test_getText() {
	String testText = "Line1\r\nLine2";

	assertTrue(":a:", text.getText().length() == 0);
	text.setText(testText);
	assertTrue(":b:", text.getText().equals(testText));
	text.setText("");
	assertTrue(":c:", text.getText().length() == 0);

	text.setText(testText);
	assertTrue(":a:", text.getText().equals(testText));
	text.setText(testText + "\r\n");
	assertTrue(":b:", text.getText().equals(testText + "\r\n"));
	text.setText("");
	assertTrue(":c:", text.getText().length() == 0);
}
@Test
public void test_getTextII() {
	boolean exceptionThrown;
	String testText = "Line1\r\nLine2";
	int invalidRanges[][] = {{-1, 0}, {0, -1}, {-1, -1}, {100, 1}, {100, -1}, {2, testText.length()}, {5, 2}};
	int ranges[][] = {{0, 1}, {0, 0}, {2, 5}, {7, 11}};

	for (int[] invalidRange : invalidRanges) {
		int start = invalidRange[0];
		int end = invalidRange[1];

		exceptionThrown = false;
		try {
			text.getText(start, end);
		}
		catch (IllegalArgumentException e) {
			exceptionThrown = true;
		}
		assertTrue(":a:", exceptionThrown);
	}
	text.setText(testText);
	for (int i = 0; i < ranges.length; i++) {
		int start = ranges[i][0];
		int end = ranges[i][1];
		assertEquals(":b:" + i, testText.substring(start, end + 1), text.getText(start, end));
	}
	for (int[] invalidRange : invalidRanges) {
		int start = invalidRange[0];
		int end = invalidRange[1];

		exceptionThrown = false;
		try {
			text.getText(start, end);
		}
		catch (IllegalArgumentException e) {
			exceptionThrown = true;
		}
		assertTrue(":a:", exceptionThrown);
	}
	text.setText("testing");
	assertTrue(":d:", text.getText(0,0).equals("t"));
	assertTrue(":d:", text.getText(0,1).equals("te"));
	assertTrue(":d:", text.getText(1,5).equals("estin"));
}
@Test
public void test_getTextRangeII() {
	boolean exceptionThrown;
	String testText = "Line1\r\nLine2";
	int invalidRanges[][] = {{-1, 0}, {0, -1}, {-1, -1}, {100, 1}, {100, -1}, {1, testText.length()}, {5, -1}};
	int ranges[][] = {{0, 1}, {0, 0}, {5, 1}, {7, 5}, {12, 0}};

	for (int[] invalidRange : invalidRanges) {
		int start = invalidRange[0];
		int length = invalidRange[1];

		exceptionThrown = false;
		try {
			text.getTextRange(start, length);
		}
		catch (IllegalArgumentException e) {
			exceptionThrown = true;
		}
		assertTrue(":a:", exceptionThrown);
	}
	text.setText(testText);
	for (int i = 0; i < ranges.length; i++) {
		int start = ranges[i][0];
		int length = ranges[i][1];
		assertEquals(":b:" + i, testText.substring(start, start + length), text.getTextRange(start, length));
	}
	for (int[] invalidRange : invalidRanges) {
		int start = invalidRange[0];
		int length = invalidRange[1];

		exceptionThrown = false;
		try {
			text.getTextRange(start, length);
		}
		catch (IllegalArgumentException e) {
			exceptionThrown = true;
		}
		assertTrue(":a:", exceptionThrown);
	}
	text.setText("testing");
	assertTrue(":d:", text.getTextRange(0,0).isEmpty());
	assertTrue(":d:", text.getTextRange(0,1).equals("t"));
	assertTrue(":d:", text.getTextRange(0,2).equals("te"));
	assertTrue(":d:", text.getTextRange(1,5).equals("estin"));
}
@Test
public void test_getTextLimit() {
	assertTrue(":a:", text.getTextLimit() < 0);
	text.setTextLimit(10);
	assertTrue(":b:", text.getTextLimit() == 10);
}

@Test
public void test_getTopIndex() {
	text.setText("Line0\r\nLine0a\r\n");

	assertTrue(":a:", text.getTopIndex() == 0);
	text.setTopIndex(-2);
	assertTrue(":b:", text.getTopIndex() == 0);
	text.setTopIndex(-1);
	assertTrue(":c:", text.getTopIndex() == 0);
	text.setTopIndex(1);
	assertTrue(":d:", text.getTopIndex() == 1);
	text.setTopIndex(2);
	assertTrue(":e:", text.getTopIndex() == 2);
	text.setTopIndex(0);
	assertTrue(":f:", text.getTopIndex() == 0);
	text.setTopIndex(3);
	assertTrue(":g:", text.getTopIndex() == 2);
	text.replaceTextRange(text.getCharCount(), 0, "Line1");
	assertTrue(":h:", text.getTopIndex() == 2);
	text.setText("");
	assertTrue(":i:", text.getTopIndex() == 0);
}

@Test
public void test_getTopPixel() {
	text.setText("Line0\r\nLine0a\r\n");

	assertTrue(":a:", text.getTopPixel() == 0);
	text.setTopIndex(-2);
	assertTrue(":b:", text.getTopPixel() == 0);
	text.setTopIndex(-1);
	assertTrue(":c:", text.getTopPixel() == 0);
	text.setTopIndex(1);
	assertTrue(":d:", text.getTopPixel() == text.getLineHeight());
	text.setTopIndex(2);
	assertTrue(":e:", text.getTopPixel() == text.getLineHeight() * 2);
	text.setTopIndex(0);
	assertTrue(":f:", text.getTopPixel() == 0);
	text.setTopIndex(3);
	assertTrue(":g:", text.getTopPixel() == text.getLineHeight() * 2);
	text.replaceTextRange(text.getCharCount(), 0, "Line1");
	assertTrue(":h:", text.getTopPixel() == text.getLineHeight() * 2);
	text.setText("");
	assertTrue(":i:", text.getTopPixel() == 0);
}

@Test
public void test_getWordWrap() {
	assertFalse(":a:", text.getWordWrap());
	text.setWordWrap(true);
	assertTrue(":b:", text.getWordWrap());
	text.setWordWrap(false);
	assertFalse(":c:", text.getWordWrap());
	text.setWordWrap(false);
	assertFalse(":d:", text.getWordWrap());
	text.setWordWrap(true);
	assertTrue(":e:", text.getWordWrap());
}

@Test
public void test_insertLjava_lang_String(){
	String delimiterString = "\n";
	try {
		text.insert(null);
		fail("No exception thrown for string == null");
	}
	catch (IllegalArgumentException e) {
	}
	assertTrue(":a:", text.getText().isEmpty());
	text.insert("");
	assertTrue(":b:", text.getText().isEmpty());
	text.insert("fred");
	assertTrue(":c:", text.getText().equals("fred"));
	text.setSelection(2);
	text.insert("helmut");
	assertTrue(":d:", text.getText().equals("frhelmuted"));
	text.setText("01234567890");
	text.setSelection(4);
	assertTrue(":e:", text.getLineCount()==1);
	text.insert(delimiterString);
	assertTrue(":f:", text.getLineCount()==2);
}

@Test
public void test_invokeActionI() {
	if (SwtTestUtil.isCocoa) {
		// TODO Fix Cocoa failure.
		if (SwtTestUtil.verbose) {
			System.out
					.println("Excluded test_invokeAction(org.eclipse.swt.tests.junit.Test_org_eclipse_swt_custom_StyledText).");
		}
		return;
	}
	// invoking actions on an empty text should not crash
	text.invokeAction(ST.LINE_DOWN);
	text.invokeAction(ST.LINE_UP);
	text.invokeAction(ST.LINE_END);
	text.invokeAction(ST.LINE_START);
	text.invokeAction(ST.COLUMN_NEXT);
	text.invokeAction(ST.PAGE_DOWN);
	text.invokeAction(ST.PAGE_UP);
	text.invokeAction(ST.WORD_NEXT);
	text.invokeAction(ST.WORD_PREVIOUS);
	text.invokeAction(ST.TEXT_END);
	text.invokeAction(ST.TEXT_START);
	text.invokeAction(ST.WINDOW_END);
	text.invokeAction(ST.WINDOW_START);
	text.invokeAction(ST.SELECT_LINE_DOWN);
	text.invokeAction(ST.SELECT_LINE_UP);
	text.invokeAction(ST.SELECT_LINE_START);
	text.invokeAction(ST.SELECT_LINE_END);
	text.invokeAction(ST.SELECT_COLUMN_PREVIOUS);
	text.invokeAction(ST.SELECT_COLUMN_NEXT);
	text.invokeAction(ST.SELECT_PAGE_UP);
	text.invokeAction(ST.SELECT_PAGE_DOWN);
	text.invokeAction(ST.SELECT_WORD_PREVIOUS);
	text.invokeAction(ST.SELECT_WORD_NEXT);
	text.invokeAction(ST.SELECT_TEXT_END);
	text.invokeAction(ST.SELECT_TEXT_START);
	text.invokeAction(ST.SELECT_WINDOW_START);
	text.invokeAction(ST.SELECT_WINDOW_END);
	text.invokeAction(ST.CUT);
	text.invokeAction(ST.COPY);
	text.invokeAction(ST.PASTE);
	text.invokeAction(ST.DELETE_PREVIOUS);
	text.invokeAction(ST.DELETE_NEXT);
	text.invokeAction(ST.TOGGLE_OVERWRITE);

	//Some platforms consider number a word start, other don't
//	text.setText("Line1\r\nLine2");
	text.setText("LineL\r\nLineW");
	text.invokeAction(ST.LINE_DOWN);
	assertEquals(7, text.getCaretOffset());

	text.invokeAction(ST.LINE_UP);
	assertEquals(0, text.getCaretOffset());

	text.invokeAction(ST.LINE_END);
	assertEquals(5, text.getCaretOffset());

	text.invokeAction(ST.LINE_START);
	assertEquals(0, text.getCaretOffset());

	text.invokeAction(ST.COLUMN_NEXT);
	assertEquals(1, text.getCaretOffset());

	text.invokeAction(ST.PAGE_DOWN);
	assertEquals(8, text.getCaretOffset());

	text.invokeAction(ST.PAGE_UP);
	assertEquals(1, text.getCaretOffset());

	text.invokeAction(ST.TEXT_START);
	text.invokeAction(ST.WORD_NEXT);
	text.invokeAction(ST.WORD_NEXT);
	assertEquals(7, text.getCaretOffset());

	text.invokeAction(ST.WORD_PREVIOUS);
	assertEquals(5, text.getCaretOffset());

	text.invokeAction(ST.TEXT_END);
	assertEquals(text.getCharCount(), text.getCaretOffset());

	text.invokeAction(ST.TEXT_START);
	assertEquals(0, text.getCaretOffset());

	text.invokeAction(ST.WINDOW_END);
	assertEquals(5, text.getCaretOffset());

	text.invokeAction(ST.WINDOW_START);
	assertEquals(0, text.getCaretOffset());

	text.invokeAction(ST.SELECT_LINE_DOWN);
	assertEquals("LineL\r\n", text.getSelectionText());

	text.invokeAction(ST.LINE_END);
	text.invokeAction(ST.SELECT_LINE_UP);
	assertEquals("\r\nLineW", text.getSelectionText());

	text.invokeAction(ST.SELECT_LINE_START);
	assertEquals("LineL\r\nLineW", text.getSelectionText());

	text.invokeAction(ST.LINE_START);
	text.invokeAction(ST.SELECT_LINE_END);
	assertEquals("LineL", text.getSelectionText());

	text.invokeAction(ST.LINE_END);
	text.invokeAction(ST.SELECT_COLUMN_PREVIOUS);
	assertEquals("L", text.getSelectionText());

	text.invokeAction(ST.SELECT_COLUMN_NEXT);
	assertEquals("", text.getSelectionText());

	text.invokeAction(ST.SELECT_PAGE_UP);
	assertEquals("", text.getSelectionText());

	text.invokeAction(ST.SELECT_PAGE_DOWN);
	assertEquals("\r\nLineW", text.getSelectionText());

	text.invokeAction(ST.LINE_END);
	text.invokeAction(ST.SELECT_WORD_PREVIOUS);
	assertEquals("LineW", text.getSelectionText());

	text.invokeAction(ST.LINE_START);
	text.invokeAction(ST.SELECT_WORD_NEXT);
	assertEquals("LineW", text.getSelectionText());

	text.invokeAction(ST.LINE_START);
	text.invokeAction(ST.SELECT_TEXT_END);
	assertEquals("LineW", text.getSelectionText());

	text.invokeAction(ST.SELECT_TEXT_START);
	assertEquals("LineL\r\n", text.getSelectionText());

	text.invokeAction(ST.LINE_START);
	text.invokeAction(ST.SELECT_WINDOW_START);
	assertEquals("", text.getSelectionText());

	text.invokeAction(ST.SELECT_WINDOW_END);
	assertEquals("LineL", text.getSelectionText());

	text.invokeAction(ST.SELECT_LINE_END);
	text.invokeAction(ST.CUT);
	assertEquals("\r\nLineW", text.getText());

	text.invokeAction(ST.SELECT_LINE_DOWN);
	text.invokeAction(ST.COPY);
	assertEquals("\r\nLineW", text.getText());

	text.invokeAction(ST.LINE_END);
	text.invokeAction(ST.PASTE);
	assertEquals("\r\nLineW" + PLATFORM_LINE_DELIMITER, text.getText());

	text.invokeAction(ST.DELETE_PREVIOUS);
	assertEquals("\r\nLineW", text.getText());

	text.invokeAction(ST.TEXT_START);
	text.invokeAction(ST.DELETE_NEXT);
	assertEquals("LineW", text.getText());

	text.invokeAction(ST.TOGGLE_OVERWRITE);
}

@Test
public void test_paste(){
	if (SwtTestUtil.isCocoa) {
		// TODO Fix Cocoa failure.
		if (SwtTestUtil.verbose) {
			System.out
					.println("Excluded test_paste(org.eclipse.swt.tests.junit.Test_org_eclipse_swt_custom_StyledText).");
		}
		return;
	}
	Clipboard clipboard = new Clipboard(text.getDisplay());
	TextTransfer transfer = TextTransfer.getInstance();
	String convertedText;

	clipboard.setContents(new String[]{"x"}, new Transfer[]{transfer});

	text.copy();
	text.paste();
	assertTrue(":a:", text.getCharCount() == 1);

	text.setSelectionRange(0, 0);
	text.copy();
	text.paste();
	assertTrue(":b:", text.getCharCount() == 2);

	text.setText("0123456789");
	text.setSelectionRange(0, 1);
	text.copy();
	text.setCaretOffset(0);
	text.paste();
	assertTrue(":c:", text.getText().equals("00123456789"));
	text.setSelectionRange(1, 2);
	text.copy();
	text.setText("");
	text.paste();
	assertTrue(":d:", text.getText().equals("01"));
	text.setText("");

	// test line delimiter conversion
	clipboard.setContents(new String[]{"\rLine1\nLine2\r\nLine3\n\rLine4\n"}, new Transfer[]{transfer});
	text.paste();
	if (SwtTestUtil.isWindowsOS) {
		convertedText = "\r\nLine1\r\nLine2\r\nLine3\r\n\r\nLine4\r\n";
	}
	else {
		convertedText = "\nLine1\nLine2\nLine3\n\nLine4\n";
	}
	assertTrue(":f:", text.getText() != null && text.getText().equals(convertedText));
	text.setText("");

	// test line delimiter conversion
	clipboard.setContents(new String[]{"Line1\r\nLine2"}, new Transfer[]{transfer});
	text.paste();
	if (SwtTestUtil.isWindowsOS) {
		convertedText = "Line1\r\nLine2";
	}
	else {
		convertedText = "Line1\nLine2";
	}
	assertTrue(":g:", text.getText() != null && text.getText().equals(convertedText));
	text.setText("");

	// test line delimiter conversion
	clipboard.setContents(new String[]{"Line1\rLine2"}, new Transfer[]{transfer});
	text.paste();
	if (SwtTestUtil.isWindowsOS) {
		convertedText = "Line1\r\nLine2";
	}
	else {
		convertedText = "Line1\nLine2";
	}
	assertTrue(":h:", text.getText() != null && text.getText().equals(convertedText));
	text.setText("");


	// test line delimiter conversion
	clipboard.setContents(new String[]{"Line1\nLine2"}, new Transfer[]{transfer});
	text.paste();
	if (SwtTestUtil.isWindowsOS) {
		convertedText = "Line1\r\nLine2";
	}
	else {
		convertedText = "Line1\nLine2";
	}
	assertTrue(":i:", text.getText() != null && text.getText().equals(convertedText));
	text.setText("");

	// test paste with text limit with no selection
	clipboard.setContents(new String[]{"abcde"}, new Transfer[]{transfer});
	text.setTextLimit(3);
	text.copy();
	text.paste();
	assertTrue(":j:", text.getText() != null && text.getText().equals("abc"));
	text.setText("");

	// test paste with text limit with full selection
	clipboard.setContents(new String[]{"abcde"}, new Transfer[]{transfer});
	text.setTextLimit(3);
	text.setText("123");
	text.setSelection(0, 3);
	text.paste();
	assertTrue(":k:", text.getText() != null && text.getText().equals("abc"));
	text.setText("");

	// test paste with text limit with partial selection
	clipboard.setContents(new String[]{"abcde"}, new Transfer[]{transfer});
	text.setTextLimit(3);
	text.setText("123");
	text.setSelection(0, 1);
	text.paste();
	assertTrue(":l:", text.getText() != null && text.getText().equals("a23"));
	text.setText("");

	clipboard.dispose();
}

@Test
public void test_print() {
	// if there aren't any printers, don't do this test
	if (Printer.getDefaultPrinterData() == null) return;

	/* We don't really want to run this test, because it wastes paper.
	 * Almost all of the print() method is tested in print(Printer), below.
	 */
//	text.print();
//	text.setText("Line1");
//	text.print();
}

@Test
public void test_printLorg_eclipse_swt_printing_Printer() {
	// if there aren't any printers, don't do this test
	if (Printer.getDefaultPrinterData() == null) return;

	boolean exceptionThrown = false;
	try {
		text.print((Printer) null);
	} catch (IllegalArgumentException ex) {
		exceptionThrown = true;
	}
	assertTrue("no exception thrown for print(null)", exceptionThrown);

	Printer printer = new Printer();
	text.print(printer); // don't run the runnable, to save paper
	text.setText("Line1");
	text.print(printer); // don't run the runnable, to save paper
	printer.dispose();
}

@Override
@Test
public void test_redraw() {
	// inherited test is sufficient
}

@Override
@Test
public void test_redrawIIIIZ() {
	// inherited test is sufficient
}

@Test
public void test_redrawRangeIIZ() {
	boolean exceptionThrown = false;

	text.redrawRange(0, 0, true);
	text.redrawRange(0, 0, false);

	try {
		text.redrawRange(0, 1, true);
	}
	catch (IllegalArgumentException e) {
		if (e.getMessage().equals("Index out of bounds")) {
			exceptionThrown = true;
		}
	}
	assertTrue(exceptionThrown);

	exceptionThrown = false;
	try {
		text.redrawRange(0, 1, false);
	}
	catch (IllegalArgumentException e) {
		if (e.getMessage().equals("Index out of bounds")) {
			exceptionThrown = true;
		}
	}
	assertTrue(exceptionThrown);

	exceptionThrown = false;
	try {
		text.redrawRange(-1, 2, true);
	}
	catch (IllegalArgumentException e) {
		if (e.getMessage().equals("Index out of bounds")) {
			exceptionThrown = true;
		}
	}
	assertTrue(exceptionThrown);

	exceptionThrown = false;
	try {
		text.redrawRange(-1, 2, false);
	}
	catch (IllegalArgumentException e) {
		if (e.getMessage().equals("Index out of bounds")) {
			exceptionThrown = true;
		}
	}
	assertTrue(exceptionThrown);

	text.setText("0123456789");
	text.redrawRange(0, 0, true);
	text.redrawRange(0, 0, false);
	text.redrawRange(0, 1, true);
	text.redrawRange(0, 1, false);
	text.redrawRange(8, 2, true);
	text.redrawRange(8, 2, false);
	text.redrawRange(10, 0, true);
	text.redrawRange(10, 0, false);

	exceptionThrown = false;
	try {
		text.redrawRange(10, 1, true);
	}
	catch (IllegalArgumentException e) {
		if (e.getMessage().equals("Index out of bounds")) {
			exceptionThrown = true;
		}
	}
	assertTrue(exceptionThrown);

	exceptionThrown = false;
	try {
		text.redrawRange(10, 1, false);
	}
	catch (IllegalArgumentException e) {
		if (e.getMessage().equals("Index out of bounds")) {
			exceptionThrown = true;
		}
	}
	assertTrue(exceptionThrown);
}

@Test
public void test_removeBidiSegmentListenerLorg_eclipse_swt_custom_BidiSegmentListener() {
	// tested in test_addBidiSegmentListenerLorg_eclipse_swt_custom_BidiSegmentListener
}

@Test
public void test_removeExtendedModifyListenerLorg_eclipse_swt_custom_ExtendedModifyListener() {
	// tested in test_addExtendedModifyListenerLorg_eclipse_swt_custom_ExtendedModifyListener
}

@Test
public void test_removeLineBackgroundListenerLorg_eclipse_swt_custom_LineBackgroundListener() {
	// tested in test_addLineBackgroundListenerLorg_eclipse_swt_custom_LineBackgroundListener
}

@Test
public void test_removeLineStyleListenerLorg_eclipse_swt_custom_LineStyleListener() {
	// tested in test_addLineStyleListenerLorg_eclipse_swt_custom_LineStyleListener
}

@Test
public void test_removeModifyListenerLorg_eclipse_swt_events_ModifyListener() {
	// tested in test_addModifyListenerLorg_eclipse_swt_events_ModifyListener
}

@Test
public void test_removeSelectionListenerLorg_eclipse_swt_events_SelectionListener() {
	// tested in test_addSelectionListenerLorg_eclipse_swt_events_SelectionListener
}

@Test
public void test_removeVerifyListenerLorg_eclipse_swt_events_VerifyListener() {
	// tested in test_addVerifyListenerLorg_eclipse_swt_events_VerifyListener
}

@Test
public void test_removeVerifyKeyListenerLorg_eclipse_swt_custom_VerifyKeyListener() {
	// tested in test_addVerifyKeyListenerLorg_eclipse_swt_custom_VerifyKeyListener
}

@Test
public void test_replaceStyleRangesII$Lorg_eclipse_swt_custom_StyleRange() {
	StyleRange[] styles;
	String textString = textString();

	/*
		defaultStyles

			(0,48,RED,YELLOW),
			(58,10,BLUE,CYAN),
			(68,10,GREEN,PURPLE)
	*/


	text.setText(textString);

	text.replaceStyleRanges(0, text.getCharCount(), defaultStyles());
	text.replaceStyleRanges(0, 78, new StyleRange[] {});
	styles = text.getStyleRanges();
	assertTrue(":0:", styles.length == 0);
	text.setText(textString);
	styles = text.getStyleRanges();
	assertTrue(":0:", styles.length == 0);
	text.replaceStyleRanges(0, 78, new StyleRange[] {});
	styles = text.getStyleRanges();
	assertTrue(":0:", styles.length == 0);
	text.replaceStyleRanges(0, text.getCharCount(), defaultStyles());
	styles = text.getStyleRanges();
	assertTrue(":0:", styles.length == 3);
	assertTrue(":0:", styles[0].equals(getStyle(0,48,RED,YELLOW)));
	assertTrue(":0:", styles[1].equals(getStyle(58,10,BLUE,CYAN)));
	assertTrue(":0:", styles[2].equals(getStyle(68,10,GREEN,PURPLE)));

	// No overlap with existing styles
	text.replaceStyleRanges(0, text.getCharCount(), defaultStyles());
	text.replaceStyleRanges(48, 5, new StyleRange[] {getStyle(48,5,YELLOW,RED)});
	styles = text.getStyleRanges();
	assertTrue(":1:", styles.length == 4);
	assertTrue(":1:", styles[0].equals(getStyle(0,48,RED,YELLOW)));
	assertTrue(":1:", styles[1].equals(getStyle(48,5,YELLOW,RED)));
	assertTrue(":1:", styles[2].equals(getStyle(58,10,BLUE,CYAN)));
	assertTrue(":1:", styles[3].equals(getStyle(68,10,GREEN,PURPLE)));

	// Overlap middle of one style - partial
	text.replaceStyleRanges(0, text.getCharCount(), defaultStyles());
	text.replaceStyleRanges(10, 10, new StyleRange[] {getStyle(10,10,YELLOW,RED)});
	styles = text.getStyleRanges();
	assertTrue(":2:", styles.length == 5);
	assertTrue(":2:", styles[0].equals(getStyle(0,10,RED,YELLOW)));
	assertTrue(":2:", styles[1].equals(getStyle(10,10,YELLOW,RED)));
	assertTrue(":2:", styles[2].equals(getStyle(20,28,RED,YELLOW)));
	assertTrue(":2:", styles[3].equals(getStyle(58,10,BLUE,CYAN)));
	assertTrue(":2:", styles[4].equals(getStyle(68,10,GREEN,PURPLE)));
	text.replaceStyleRanges(0, text.getCharCount(), new StyleRange[] {});
	styles = text.getStyleRanges();
	assertTrue(":2:", styles.length == 0);

	// Overlap middle of one style - full
	text.replaceStyleRanges(0, text.getCharCount(), defaultStyles());
	text.replaceStyleRanges(58, 10, new StyleRange[] {getStyle(58,10,YELLOW,RED)});
	styles = text.getStyleRanges();
	assertTrue(":3:", styles.length == 3);
	assertTrue(":3:", styles[0].equals(getStyle(0,48,RED,YELLOW)));
	assertTrue(":3:", styles[1].equals(getStyle(58,10,YELLOW,RED)));
	assertTrue(":3:", styles[2].equals(getStyle(68,10,GREEN,PURPLE)));

	// Overlap end of one style
	text.replaceStyleRanges(0, text.getCharCount(), defaultStyles());
	text.replaceStyleRanges(38, 15, new StyleRange[] {getStyle(38,15,YELLOW,RED)});
	styles = text.getStyleRanges();
	assertTrue(":4:", styles.length == 4);
	assertTrue(":4:", styles[0].equals(getStyle(0,38,RED,YELLOW)));
	assertTrue(":4:", styles[1].equals(getStyle(38,15,YELLOW,RED)));
	assertTrue(":4:", styles[2].equals(getStyle(58,10,BLUE,CYAN)));
	assertTrue(":4:", styles[3].equals(getStyle(68,10,GREEN,PURPLE)));

	// Overlap beginning of one style
	text.replaceStyleRanges(0, text.getCharCount(), defaultStyles());
	text.replaceStyleRanges(50, 10, new StyleRange[] {getStyle(50,10,YELLOW,RED)});
	styles = text.getStyleRanges();
	assertTrue(":5:", styles.length == 4);
	assertTrue(":5:", styles[0].equals(getStyle(0,48,RED,YELLOW)));
	assertTrue(":5:", styles[1].equals(getStyle(50,10,YELLOW,RED)));
	assertTrue(":5:", styles[2].equals(getStyle(60,8,BLUE,CYAN)));
	assertTrue(":5:", styles[3].equals(getStyle(68,10,GREEN,PURPLE)));

	// Overlap complete style
	text.replaceStyleRanges(0, text.getCharCount(), defaultStyles());
	text.replaceStyleRanges(48, 20, new StyleRange[] {getStyle(48,20,YELLOW,RED)});
	styles = text.getStyleRanges();
	assertTrue(":6:", styles.length == 3);
	assertTrue(":6:", styles[0].equals(getStyle(0,48,RED,YELLOW)));
	assertTrue(":6:", styles[1].equals(getStyle(48,20,YELLOW,RED)));
	assertTrue(":6:", styles[2].equals(getStyle(68,10,GREEN,PURPLE)));

	// reset the environment
	text.dispose();
	text = new StyledText(shell, SWT.NULL);
	setWidget(text);

	text.setText(textString);
	StyleRange ranges[] = new StyleRange[3];
	ranges[0] = getStyle(0,48,RED,YELLOW);
	ranges[1] = getStyle(48,20,BLUE,CYAN);
	ranges[2] = getStyle(68,10,GREEN,PURPLE);
	text.replaceStyleRanges(0, 78, ranges);
	styles = text.getStyleRanges();
	assertTrue(":7:", styles.length == 3);
	assertTrue(":7:", styles[0].equals(getStyle(0,48,RED,YELLOW)));
	assertTrue(":7:", styles[1].equals(getStyle(48,20,BLUE,CYAN)));
	assertTrue(":7:", styles[2].equals(getStyle(68,10,GREEN,PURPLE)));

	text.setText("012345678901234");
	ranges = new StyleRange[2];
	ranges[0] = getStyle(0,5,RED,YELLOW);
	ranges[1] = getStyle(10,5,BLUE,CYAN);
	text.replaceStyleRanges(0, 15, ranges);
	styles = text.getStyleRanges();
	assertTrue(":8:", styles.length == 2);
	assertTrue(":8:", styles[0].equals(getStyle(0,5,RED,YELLOW)));
	assertTrue(":8:", styles[1].equals(getStyle(10,5,BLUE,CYAN)));

	text.setText("redgreenblueyellowcyanpurple");
	ranges = new StyleRange[4];
	ranges[0] = getStyle(0,3,RED,null);
	ranges[1] = getStyle(3,5,GREEN,null);
	ranges[2] = getStyle(8,4,BLUE,null);
	ranges[3] = getStyle(12,6,YELLOW,null);
	text.replaceStyleRanges(0, 18, ranges);
	styles = text.getStyleRanges();
	assertTrue(":9:", styles.length == 4);
	assertTrue(":9:", styles[0].equals(getStyle(0,3,RED,null)));
	assertTrue(":9:", styles[1].equals(getStyle(3,5,GREEN,null)));
	assertTrue(":9:", styles[2].equals(getStyle(8,4,BLUE, null)));
	assertTrue(":9:", styles[3].equals(getStyle(12,6,YELLOW,null)));
	ranges = new StyleRange[2];
	ranges[0] = getStyle(18,4,CYAN,null);
	ranges[1] = getStyle(22,6,PURPLE,null);
	text.replaceStyleRanges(18, 10, ranges);
	styles = text.getStyleRanges();
	assertTrue(":9:", styles.length == 6);
	assertTrue(":9:", styles[4].equals(getStyle(18,4,CYAN,null)));
	assertTrue(":9:", styles[5].equals(getStyle(22,6,PURPLE,null)));

	// reset the environment
	text.dispose();
	text = new StyledText(shell, SWT.NULL);
	setWidget(text);

	textString = textString();

	text.setText(textString);
	ranges = new StyleRange[2];
	ranges[0] = getStyle(0,10,RED,YELLOW);
	ranges[1] = getStyle(25,10,GREEN,PURPLE);
	text.replaceStyleRanges(0, 35, ranges);
	ranges = new StyleRange[2];
	ranges[0] = getStyle(5,15,BLUE,CYAN);
	ranges[1] = getStyle(20,10,GREEN,PURPLE);
	text.replaceStyleRanges(5, 25, ranges);
	styles = text.getStyleRanges();
	assertTrue(":10:", styles.length == 3);
	assertTrue(":10:", styles[0].equals(getStyle(0,5,RED,YELLOW)));
	assertTrue(":10:", styles[1].equals(getStyle(5,15,BLUE,CYAN)));
	assertTrue(":10:", styles[2].equals(getStyle(20,15,GREEN,PURPLE)));

	text.setText("01234567890123456789");
	ranges = new StyleRange[2];
	ranges[0] = getStyle(0,10,RED,YELLOW);
	ranges[1] = getStyle(10,10,BLUE,CYAN);
	text.replaceStyleRanges(0, 20, ranges);
	ranges = new StyleRange[2];
	ranges[0] = getStyle(5,3,RED,YELLOW);
	ranges[1] = getStyle(12,5,BLUE,CYAN);
	text.replaceStyleRanges(5, 12, ranges);
	styles = text.getStyleRanges();
	assertTrue(":11:", styles.length == 2);
	assertTrue(":11:", styles[0].equals(getStyle(0,8,RED,YELLOW)));
	assertTrue(":11:", styles[1].equals(getStyle(12,8,BLUE,CYAN)));

	text.setText("0123456789012345");
	ranges = new StyleRange[3];
	ranges[0] = getStyle(0,5,RED,YELLOW);
	ranges[1] = getStyle(5,5,BLUE,CYAN);
	ranges[2] = getStyle(10,5,GREEN,PURPLE);
	text.replaceStyleRanges(0, 15, ranges);
	ranges = new StyleRange[2];
	ranges[0] = getStyle(5,5,RED,YELLOW);
	ranges[1] = getStyle(10,5,RED,YELLOW);
	text.replaceStyleRanges(5, 10, ranges);
	styles = text.getStyleRanges();
	assertTrue(":12:", styles.length == 1);
	assertTrue(":12:", styles[0].equals(getStyle(0,15,RED,YELLOW)));

	text.setText("0123456789012345");
	ranges = new StyleRange[1];
	ranges[0] = getStyle(10,5,GREEN,PURPLE);
	text.replaceStyleRanges(0, 15, ranges);
	ranges = new StyleRange[2];
	ranges[0] = getStyle(0,5,RED,YELLOW);
	ranges[1] = getStyle(5,5,BLUE,CYAN);
	text.replaceStyleRanges(0, 10, ranges);
	styles = text.getStyleRanges();
	assertTrue(":13:", styles.length == 3);
	assertTrue(":13:", styles[0].equals(getStyle(0,5,RED,YELLOW)));
	assertTrue(":13:", styles[1].equals(getStyle(5,5,BLUE,CYAN)));
	assertTrue(":13:", styles[2].equals(getStyle(10,5,GREEN,PURPLE)));

	text.setText("012345678901234");
	ranges = new StyleRange[2];
	ranges[0] = getStyle(0,5,RED,YELLOW);
	ranges[1] = getStyle(10,5,BLUE,CYAN);
	text.replaceStyleRanges(0, 15, ranges);
	ranges = new StyleRange[1];
	ranges[0] = getStyle(5,7,BLUE,CYAN);
	text.replaceStyleRanges(5, 7, ranges);
	styles = text.getStyleRanges();
	assertTrue(":14:", styles.length == 2);
	assertTrue(":14:", styles[0].equals(getStyle(0,5,RED,YELLOW)));
	assertTrue(":14:", styles[1].equals(getStyle(5,10,BLUE,CYAN)));


	// reset the environment
	text.dispose();
	text = new StyledText(shell, SWT.NULL);
	setWidget(text);

	textString = textString();


	/*
		defaultStyles

			(0,48,RED,YELLOW),
			(58,10,BLUE,CYAN),
			(68,10,GREEN,PURPLE)
	*/


	// End/Beginning overlap
	text.setText(textString);
	text.setStyleRanges(defaultStyles());
	ranges = new StyleRange[1];
	ranges[0] = getStyle(38,25,YELLOW,RED);
	text.replaceStyleRanges(38, 25, ranges);
	styles = text.getStyleRanges();
	assertTrue(":1a:", styles.length == 4);
	assertTrue(":1a:", styles[0].equals(getStyle(0,38,RED,YELLOW)));
	assertTrue(":1a:", styles[1].equals(getStyle(38,25,YELLOW,RED)));
	assertTrue(":1a:", styles[2].equals(getStyle(63,5,BLUE,CYAN)));
	assertTrue(":1a:", styles[3].equals(getStyle(68,10,GREEN,PURPLE)));
	text.setStyleRanges(defaultStyles());
	ranges = new StyleRange[1];
	ranges[0] = getStyle(63,10,YELLOW,RED);
	text.replaceStyleRanges(63, 10, ranges);
	styles = text.getStyleRanges();
	assertTrue(":1a:", styles.length == 4);
	assertTrue(":1a:", styles[0].equals(getStyle(0,48,RED,YELLOW)));
	assertTrue(":1a:", styles[1].equals(getStyle(58,5,BLUE,CYAN)));
	assertTrue(":1a:", styles[2].equals(getStyle(63,10,YELLOW,RED)));
	assertTrue(":1a:", styles[3].equals(getStyle(73,5,GREEN,PURPLE)));

	// Complete overlap
	text.setStyleRanges(defaultStyles());
	ranges = new StyleRange[1];
	ranges[0] = getStyle(0,78,YELLOW,RED);
	text.replaceStyleRanges(0, 78, ranges);
	styles = text.getStyleRanges();
	styles = text.getStyleRanges();
	assertTrue(":2a:", styles.length == 1);
	assertTrue(":2a:", styles[0].equals(getStyle(0,78,YELLOW,RED)));

	text.setStyleRanges(defaultStyles());
	ranges = new StyleRange[1];
	ranges[0] = getStyle(0,68,YELLOW,RED);
	text.replaceStyleRanges(0, 68, ranges);
	styles = text.getStyleRanges();
	assertTrue(":2a:", styles.length == 2);
	assertTrue(":2a:", styles[0].equals(getStyle(0,68,YELLOW,RED)));
	assertTrue(":2a:", styles[1].equals(getStyle(68,10,GREEN,PURPLE)));
	text.setStyleRanges(defaultStyles());
	ranges = new StyleRange[1];
	ranges[0] = getStyle(58,20,YELLOW,RED);
	text.replaceStyleRanges(58, 20, ranges);
	styles = text.getStyleRanges();
	assertTrue(":2a:", styles.length == 2);
	assertTrue(":2a:", styles[0].equals(getStyle(0,48,RED,YELLOW)));
	assertTrue(":2a:", styles[1].equals(getStyle(58,20,YELLOW,RED)));

	// 1-N complete, beginning
	text.setText("012345678901234567890123456789");
	text.setStyleRanges(
		new StyleRange[] {getStyle(0,5,RED,RED), getStyle(5,5,YELLOW,YELLOW),
			getStyle(10,5,CYAN,CYAN), getStyle(15,5,BLUE,BLUE),
			getStyle(20,5,GREEN,GREEN), getStyle(25,5,PURPLE,PURPLE)}
	);
	ranges = new StyleRange[1];
	ranges[0] = getStyle(5,23,YELLOW,RED);
	text.replaceStyleRanges(5, 23, ranges);
	styles = text.getStyleRanges();
	assertTrue(":3a:", styles.length == 3);
	assertTrue(":3a:", styles[0].equals(getStyle(0,5,RED,RED)));
	assertTrue(":3a:", styles[1].equals(getStyle(5,23,YELLOW,RED)));
	assertTrue(":3a:", styles[2].equals(getStyle(28,2,PURPLE,PURPLE)));

	// end, 1-N complete, beginning
	text.setStyleRanges(
		new StyleRange[] {getStyle(0,5,RED,RED), getStyle(5,5,YELLOW,YELLOW),
			getStyle(10,5,CYAN,CYAN), getStyle(15,5,BLUE,BLUE),
			getStyle(20,5,GREEN,GREEN), getStyle(25,5,PURPLE,PURPLE)}
	);
	ranges = new StyleRange[1];
	ranges[0] = getStyle(13,12,YELLOW,RED);
	text.replaceStyleRanges(13, 12, ranges);
	styles = text.getStyleRanges();
	assertTrue(":3a:", styles.length == 5);
	assertTrue(":3a:", styles[0].equals(getStyle(0,5,RED,RED)));
	assertTrue(":3a:", styles[1].equals(getStyle(5,5,YELLOW,YELLOW)));
	assertTrue(":3a:", styles[2].equals(getStyle(10,3,CYAN,CYAN)));
	assertTrue(":3a:", styles[3].equals(getStyle(13,12,YELLOW,RED)));
	assertTrue(":3a:", styles[4].equals(getStyle(25,5,PURPLE,PURPLE)));

	// reset the environment
	text.dispose();
	text = new StyledText(shell, SWT.NULL);
	setWidget(text);

	// insert with no styles
	text.setText("01234567890123456789");
	ranges = new StyleRange[1];
	ranges[0] = getStyle(0,5,RED,YELLOW);
	text.replaceStyleRanges(0, 10, ranges);
	styles = text.getStyleRanges();
	assertTrue(":1xa:", styles.length == 1);
	assertTrue(":1xa:", styles[0].equals(getStyle(0,5,RED,YELLOW)));

	// insert before 1 style
	text.setText("01234567890123456789");
	ranges = new StyleRange[1];
	ranges[0] = getStyle(5,3,RED,YELLOW);
	text.replaceStyleRanges(0, 10, ranges);
	ranges = new StyleRange[1];
	ranges[0] = getStyle(0,3,PURPLE,PURPLE);
	text.replaceStyleRanges(0, 3, ranges);
	styles = text.getStyleRanges();
	assertTrue(":1xb:", styles.length == 2);
	assertTrue(":1xb:", styles[0].equals(getStyle(0,3,PURPLE,PURPLE)));
	assertTrue(":1xb:", styles[1].equals(getStyle(5,3,RED,YELLOW)));

	// insert after 1 style
	text.setText("01234567890123456789");
	ranges = new StyleRange[1];
	ranges[0] = getStyle(5,3,RED,YELLOW);
	text.replaceStyleRanges(0, 10, ranges);
	ranges = new StyleRange[1];
	ranges[0] = getStyle(8,1,PURPLE,PURPLE);
	text.replaceStyleRanges(8, 1, ranges);
	styles = text.getStyleRanges();
	assertTrue(":1xc:", styles.length == 2);
	assertTrue(":1xc:", styles[0].equals(getStyle(5,3,RED,YELLOW)));
	assertTrue(":1xc:", styles[1].equals(getStyle(8,1,PURPLE,PURPLE)));

	// insert before 2 styles
	text.setText("01234567890123456789");
	ranges = new StyleRange[2];
	ranges[0] = getStyle(5,2,RED,YELLOW);
	ranges[1] = getStyle(10,2,RED,YELLOW);
	text.replaceStyleRanges(0, 20, ranges);
	ranges = new StyleRange[1];
	ranges[0] = getStyle(2,1,PURPLE,PURPLE);
	text.replaceStyleRanges(2, 1, ranges);
	styles = text.getStyleRanges();
	assertTrue(":1xe:", styles.length == 3);
	assertTrue(":1xe:", styles[0].equals(getStyle(2,1,PURPLE,PURPLE)));
	assertTrue(":1xe:", styles[1].equals(getStyle(5,2,RED,YELLOW)));
	assertTrue(":1xe:", styles[2].equals(getStyle(10,2,RED,YELLOW)));

	// insert after 2 styles
	text.setText("01234567890123456789");
	ranges = new StyleRange[2];
	ranges[0] = getStyle(5,2,RED,YELLOW);
	ranges[1] = getStyle(10,2,RED,YELLOW);
	text.replaceStyleRanges(0, 20, ranges);
	ranges = new StyleRange[1];
	ranges[0] = getStyle(12,1,PURPLE,PURPLE);
	text.replaceStyleRanges(12, 1, ranges);
	styles = text.getStyleRanges();
	assertTrue(":1xf:", styles.length == 3);
	assertTrue(":1xf:", styles[0].equals(getStyle(5,2,RED,YELLOW)));
	assertTrue(":1xf:", styles[1].equals(getStyle(10,2,RED,YELLOW)));
	assertTrue(":1xf:", styles[2].equals(getStyle(12,1,PURPLE,PURPLE)));

	// insert middle 2 styles
	text.setText("01234567890123456789");
	ranges = new StyleRange[2];
	ranges[0] = getStyle(1,2,RED,YELLOW);
	ranges[1] = getStyle(12,2,RED,YELLOW);
	text.replaceStyleRanges(0, 20, ranges);
	ranges = new StyleRange[1];
	ranges[0] = getStyle(5,3,PURPLE,PURPLE);
	text.replaceStyleRanges(5, 3, ranges);
	styles = text.getStyleRanges();
	assertTrue(":1xg:", styles.length == 3);
	assertTrue(":1xg:", styles[0].equals(getStyle(1,2,RED,YELLOW)));
	assertTrue(":1xg:", styles[1].equals(getStyle(5,3,PURPLE,PURPLE)));
	assertTrue(":1xg:", styles[2].equals(getStyle(12,2,RED,YELLOW)));

	// insert middle 3 styles
	text.setText("01234567890123456789");
	ranges = new StyleRange[3];
	ranges[0] = getStyle(1,3,RED,PURPLE);
	ranges[1] = getStyle(6,3,PURPLE,YELLOW);
	ranges[2] = getStyle(12,3,RED,YELLOW);
	text.replaceStyleRanges(0, 20, ranges);
	ranges = new StyleRange[1];
	ranges[0] = getStyle(4,2,PURPLE,PURPLE);
	text.replaceStyleRanges(4, 2, ranges);
	styles = text.getStyleRanges();
	assertTrue(":1xh:", styles.length == 4);
	assertTrue(":1xh:", styles[0].equals(getStyle(1,3,RED,PURPLE)));
	assertTrue(":1xh:", styles[1].equals(getStyle(4,2,PURPLE,PURPLE)));
	assertTrue(":1xh:", styles[2].equals(getStyle(6,3,PURPLE,YELLOW)));
	assertTrue(":1xh:", styles[3].equals(getStyle(12,3,RED,YELLOW)));

	// reset the environment
	text.dispose();
	text = new StyledText(shell, SWT.NULL);
	setWidget(text);

	text.setText("0");
	ranges = new StyleRange[1];
	ranges[0] = getStyle(0,1,PURPLE,PURPLE);
	text.replaceStyleRanges(0, 1, ranges);
	styles = text.getStyleRanges();
	assertTrue(":2xa:", styles.length == 1);

	text.setText("01");
	ranges = new StyleRange[2];
	ranges[0] = getStyle(0,1,PURPLE,PURPLE);
	ranges[1] = getStyle(1,1,RED,RED);
	text.replaceStyleRanges(0, 2, ranges);
	ranges = new StyleRange[1];
	ranges[0] = getStyle(0,1,YELLOW,YELLOW);
	text.replaceStyleRanges(0, 1, ranges);
	styles = text.getStyleRanges();
	assertTrue(":2xb:", styles.length == 2);
	assertTrue(":2xb:", styles[0].equals(getStyle(0,1,YELLOW,YELLOW)));
	assertTrue(":2xb:", styles[1].equals(getStyle(1,1,RED,RED)));

	text.setText("01");
	ranges = new StyleRange[2];
	ranges[0] = getStyle(0,1,PURPLE,PURPLE);
	ranges[1] = getStyle(1,1,RED,RED);
	text.replaceStyleRanges(0, 2, ranges);
	ranges = new StyleRange[1];
	ranges[0] = getStyle(1,1,YELLOW,YELLOW);
	text.replaceStyleRanges(1, 1, ranges);
	styles = text.getStyleRanges();
	assertTrue(":2xc:", styles.length == 2);
	assertTrue(":2xc:", styles[0].equals(getStyle(0,1,PURPLE,PURPLE)));
	assertTrue(":2xc:", styles[1].equals(getStyle(1,1,YELLOW,YELLOW)));

	text.setText("012");
	ranges = new StyleRange[2];
	ranges[0] = getStyle(0,1,PURPLE,PURPLE);
	ranges[1] = getStyle(1,1,RED,RED);
	text.replaceStyleRanges(0, 2, ranges);
	ranges = new StyleRange[1];
	ranges[0] = getStyle(2,1,YELLOW,YELLOW);
	text.replaceStyleRanges(2, 1, ranges);
	styles = text.getStyleRanges();
	assertTrue(":2xd:", styles.length == 3);
	assertTrue(":2xd:", styles[0].equals(getStyle(0,1,PURPLE,PURPLE)));
	assertTrue(":2xd:", styles[1].equals(getStyle(1,1,RED,RED)));
	assertTrue(":2xd:", styles[2].equals(getStyle(2,1,YELLOW,YELLOW)));

	text.setText("01234");
	ranges = new StyleRange[3];
	ranges[0] = getStyle(1,1,PURPLE,PURPLE);
	ranges[1] = getStyle(2,1,RED,RED);
	ranges[2] = getStyle(3,1,PURPLE,PURPLE);
	text.setStyleRanges(ranges);
	ranges = new StyleRange[1];
	ranges[0] = getStyle(4,1,YELLOW,YELLOW);
	text.replaceStyleRanges(4, 1, ranges);
	styles = text.getStyleRanges();
	assertTrue(":2xe:", styles.length == 4);
	assertTrue(":2xe:", styles[3].equals(getStyle(4,1,YELLOW,YELLOW)));

	text.setText("01234");
	ranges = new StyleRange[1];
	ranges[0] = getStyle(4,1,YELLOW,YELLOW);
	text.replaceStyleRanges(4, 1, ranges);
	styles = text.getStyleRanges();
	assertTrue(":2xf:", styles.length == 1);
	assertTrue(":2xf:", styles[0].equals(getStyle(4,1,YELLOW,YELLOW)));

	text.setText("01234");
	ranges = new StyleRange[1];
	ranges[0] = getStyle(4,1,YELLOW,YELLOW);
	text.replaceStyleRanges(4, 1, ranges);
	ranges = new StyleRange[0];
	text.replaceStyleRanges(4, 1, ranges);
	styles = text.getStyleRanges();
	assertTrue(":2xg:", styles.length == 0);

}

@Test
public void test_replaceTextRangeIILjava_lang_String(){
	final String defaultText = "line0\n\rline1\n\rline2\n\r";
	final int defaultTextLength = defaultText.length();
	final int selectionStart = 7;
	final int selectionLength = 7;
	final int replaceStart = selectionStart + selectionLength + 1;
	final int replaceLength = 5;
	boolean exceptionThrown = false;
	final String newText = "newline0\n\rnewline1";
	final int newTextLength = newText.length();
	class TestSelectionListener extends SelectionAdapter {
		public Point eventSelection = new Point(0, 0);
		@Override
		public void widgetSelected(SelectionEvent e) {
			eventSelection.x = e.x;
			eventSelection.y = e.y;
		}
	}
	final TestSelectionListener selectionListener = new TestSelectionListener();
	text.addSelectionListener(selectionListener);

	// insert text
	// within range
	// after selection
	text.setText(defaultText);
	// select 2nd line including line break
	text.setSelectionRange(selectionStart, selectionLength);
	text.replaceTextRange(replaceStart, 0, newText);
	assertTrue(":a:", text.getCharCount() == defaultTextLength + newTextLength);
	assertTrue(":b:", text.getSelectionRange().x == selectionStart && text.getSelectionRange().y == selectionLength);

	// before selection
	text.setText(defaultText);
	// select 2nd line including line break
	text.setSelectionRange(selectionStart, selectionLength);
	text.replaceTextRange(0, 0, newText);
	assertTrue(":c:", text.getCharCount() == defaultTextLength + newTextLength);
	assertTrue(":d:", text.getSelectionRange().x == selectionStart + newTextLength && text.getSelectionRange().y == selectionLength);
	assertEquals(text.getSelection(), selectionListener.eventSelection);


	// intersecting selection
	text.setText(defaultText);
	// select 2nd line including line break
	text.setSelectionRange(selectionStart, selectionLength);
	text.replaceTextRange(selectionStart + 1, 0, newText);
	assertTrue(":e:", text.getCharCount() == defaultTextLength + newTextLength);
	assertTrue(":f:", text.getSelectionRange().x == selectionStart + 1 + newTextLength && text.getSelectionRange().y == 0);
	assertEquals(text.getSelection(), selectionListener.eventSelection);

	// out of range
	text.setText(defaultText);
	// select 2nd line including line break
	text.setSelectionRange(selectionStart, selectionLength);
	try {
		text.replaceTextRange(-1, 0, newText);
	}
	catch (IllegalArgumentException e) {
		exceptionThrown = true;
	}
	assertTrue(":g:", exceptionThrown);
	exceptionThrown = false;
	try {
		text.replaceTextRange(text.getCharCount() + 1, 0, newText);
	}
	catch (IllegalArgumentException e) {
		exceptionThrown = true;
	}

	exceptionThrown = false;
	try {
		text.replaceTextRange(0, 0, null);
	}
	catch (IllegalArgumentException e) {
		exceptionThrown = true;
	}
	assertTrue(exceptionThrown);

	assertTrue(":h:", exceptionThrown);
	assertTrue(":i:", text.getSelectionRange().x == selectionStart && text.getSelectionRange().y == selectionLength);
	exceptionThrown = false;

	// append text
	// append in empty widget
	text.setText("");
	text.replaceTextRange(text.getCharCount(), 0, newText);
	assertTrue(":j:", text.getCharCount() == newTextLength);
	assertTrue(":k:", text.getSelectionRange().x == 0 && text.getSelectionRange().y == 0);

	// append in non-empty widget (selection should always be preserved)
	text.setText(defaultText);
	// select 2nd line including line break
	text.setSelectionRange(selectionStart, selectionLength);
	text.replaceTextRange(text.getCharCount(), 0, newText);
	assertTrue(":l:", text.getCharCount() == defaultTextLength + newTextLength);
	assertTrue(":m:", text.getSelectionRange().x == selectionStart && text.getSelectionRange().y == selectionLength);

	// place caret at end of text
	text.setText(defaultText);
	text.setSelectionRange(text.getCharCount(), 0);
	text.replaceTextRange(text.getCharCount(), 0, newText);
	assertTrue(":n:", text.getCharCount() == defaultTextLength + newTextLength);
	assertTrue(":o:", text.getSelectionRange().x == text.getCharCount() - newTextLength && text.getSelectionRange().y == 0);

	// replace text
	// within range
	// after selection
	text.setText(defaultText);
	// select 2nd line including line break
	text.setSelectionRange(selectionStart, selectionLength);
	text.replaceTextRange(replaceStart, replaceLength, newText);
	assertTrue(":p:", text.getCharCount() == defaultTextLength + newTextLength - replaceLength);
	assertTrue(":q:", text.getSelectionRange().x == selectionStart && text.getSelectionRange().y == selectionLength);

	// before selection
	text.setText(defaultText);
	// select 2nd line including line break
	text.setSelectionRange(selectionStart, selectionLength);
	text.replaceTextRange(0, replaceLength, newText);
	assertTrue(":r:", text.getCharCount() == defaultTextLength + newTextLength - replaceLength);
	assertTrue(":s:", text.getSelectionRange().x == selectionStart + newTextLength - replaceLength && text.getSelectionRange().y == selectionLength);
	assertEquals(text.getSelection(), selectionListener.eventSelection);

	// intersecting selection
	text.setText(defaultText);
	// select 2nd line including line break
	text.setSelectionRange(selectionStart, selectionLength);
	text.replaceTextRange(selectionStart + 1, replaceLength, newText);
	assertTrue(":t:", text.getCharCount() == defaultTextLength + newTextLength - replaceLength);
	assertTrue(":u:", text.getSelectionRange().x == selectionStart + 1 + newTextLength && text.getSelectionRange().y == 0);
	assertEquals(text.getSelection(), selectionListener.eventSelection);

	// out of range
	text.setText(defaultText);
	// select 2nd line including line break
	text.setSelectionRange(selectionStart, selectionLength);
	try {
		text.replaceTextRange(-1, replaceLength, newText);
	}
	catch (IllegalArgumentException e) {
		exceptionThrown = true;
	}
	assertTrue(":v:", exceptionThrown);
	exceptionThrown = false;
	try {
		text.replaceTextRange(text.getCharCount() + 1, replaceLength, newText);
	}
	catch (IllegalArgumentException e) {
		exceptionThrown = true;
	}
	assertTrue(":w:", exceptionThrown);
	assertTrue(":x:", text.getSelectionRange().x == selectionStart && text.getSelectionRange().y == selectionLength);
	text.removeSelectionListener(selectionListener);
}

@Test
public void test_selectAll() {
	String line = "Line1\rLine2";

	text.selectAll();
	assertEquals("", text.getSelectionText());

	text.setText(line);
	text.selectAll();
	assertEquals(line, text.getSelectionText());

	text.setText("");
	text.selectAll();
	assertEquals("", text.getSelectionText());
}

@Override
@Test
public void test_setCaretLorg_eclipse_swt_widgets_Caret() {
	Caret caret = new Caret(text, SWT.NONE);
	final int XINSET = isBidiCaret() ? 2 : 0;

	text.setCaret(caret);
	assertEquals(XINSET, text.getCaret().getLocation().x);
	assertEquals(0, text.getCaret().getLocation().y);

	text.setCaret(null);
	text.setText("\rLine2");
	text.setSelection(2);

	text.setTopIndex(0);
	text.setCaret(caret);
	assertTrue(text.getCaret().getLocation().x > 0);
	assertEquals(text.getLineHeight(), text.getCaret().getLocation().y);
}

@Test
public void test_setCaretOffsetI(){
	text.setCaretOffset(-2);
	assertTrue(":a:", text.getCaretOffset() == 0);
	text.setCaretOffset(1);
	assertTrue(":b:", text.getCaretOffset() == 0);
	text.setCaretOffset(0);
	assertTrue(":c:", text.getCaretOffset() == 0);

	text.setText("Line0\r\n");
	text.setCaretOffset(-2);
	assertTrue(":d:", text.getCaretOffset() == 0);
	text.setCaretOffset(1);
	assertTrue(":e:", text.getCaretOffset() == 1);
	text.setCaretOffset(0);
	assertTrue(":f:", text.getCaretOffset() == 0);

	text.setCaretOffset(text.getCharCount());
	assertTrue(":g:", text.getCaretOffset() == text.getCharCount());
	text.setCaretOffset(text.getCharCount() + 1);
	assertTrue(":h:", text.getCaretOffset() == text.getCharCount());
	text.setCaretOffset(5);
	assertTrue(":i:", text.getCaretOffset() == 5);

	text.setText("");
	text.setCaretOffset(-2);
	assertTrue(":j:", text.getCaretOffset() == 0);
	text.setCaretOffset(1);
	assertTrue(":k:", text.getCaretOffset() == 0);
	text.setCaretOffset(0);
	assertTrue(":l:", text.getCaretOffset() == 0);
}

@Test
public void test_setContentLorg_eclipse_swt_custom_StyledTextContent() {
	boolean exceptionThrown;
	StyledTextContent content = new StyledTextContent() {
		@Override
		public void addTextChangeListener(TextChangeListener listener) {
		}
		@Override
		public int getCharCount() {
			return 0;
		}
		@Override
		public String getLine(int lineIndex) {
			return "";
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
			return "";
		}
		@Override
		public int getOffsetAtLine(int lineIndex) {
			return 0;
		}
		@Override
		public String getTextRange(int start, int length) {
			return "";
		}
		@Override
		public void removeTextChangeListener(TextChangeListener listener) {
		}
		@Override
		public void replaceTextRange(int start, int replaceLength, String text) {
		}
		@Override
		public void setText(String text) {
		}
	};
	text.setContent(content);
	assertEquals(content, text.getContent());

	exceptionThrown = false;
	try {
		text.setContent(null);
	}
	catch (IllegalArgumentException e) {
		exceptionThrown = true;
	}
	assertTrue(exceptionThrown);
}

@Test
public void test_setDoubleClickEnabledZ(){
	text.setDoubleClickEnabled(true);
	assertTrue(":a:", text.getDoubleClickEnabled());
	text.setDoubleClickEnabled(false);
	assertFalse(":b:", text.getDoubleClickEnabled());
	text.setDoubleClickEnabled(false);
	assertFalse(":c:", text.getDoubleClickEnabled());
	text.setDoubleClickEnabled(true);
	assertTrue(":d:", text.getDoubleClickEnabled());
}

@Test
public void test_setEnabled(){
	// Get colors
	Color disabledBg = text.getDisplay().getSystemColor(SWT.COLOR_TEXT_DISABLED_BACKGROUND);
	Color disabledFg = text.getDisplay().getSystemColor(SWT.COLOR_WIDGET_DISABLED_FOREGROUND);
	Color enabledBg = text.getDisplay().getSystemColor(SWT.COLOR_LIST_BACKGROUND);
	Color enabledFg = text.getDisplay().getSystemColor(SWT.COLOR_LIST_FOREGROUND);

	// Test basic enabled/disabled functionality twice
	text.setEnabled(false);
	assertEquals(disabledBg, text.getBackground());
	assertEquals(disabledFg, text.getForeground());
	text.setEnabled(true);
	assertEquals(enabledBg, text.getBackground());
	assertEquals(enabledFg, text.getForeground());
	text.setEnabled(false);
	assertEquals(disabledBg, text.getBackground());
	assertEquals(disabledFg, text.getForeground());
	text.setEnabled(true);
	assertEquals(enabledBg, text.getBackground());
	assertEquals(enabledFg, text.getForeground());

	// Test color preservation
	text.setBackground(getColor(BLUE));
	text.setForeground(getColor(RED));
	text.setEnabled(false);
	assertEquals(getColor(BLUE), text.getBackground());
	assertEquals(getColor(RED), text.getForeground());
	text.setEnabled(true);
	assertEquals(getColor(BLUE), text.getBackground());
	assertEquals(getColor(RED), text.getForeground());

	// Test color reset
	text.setBackground(null);
	text.setForeground(null);
	assertEquals(enabledBg, text.getBackground());
	assertEquals(enabledFg, text.getForeground());
	text.setEnabled(false);
	text.setBackground(null);
	text.setForeground(null);
	assertEquals(disabledBg, text.getBackground());
	assertEquals(disabledFg, text.getForeground());
	text.setBackground(getColor(GREEN));
	text.setForeground(getColor(CYAN));
	assertEquals(getColor(GREEN), text.getBackground());
	assertEquals(getColor(CYAN), text.getForeground());
	text.setBackground(null);
	text.setForeground(null);
	assertEquals(disabledBg, text.getBackground());
	assertEquals(disabledFg, text.getForeground());

	// Dispose colors
	disabledBg.dispose();
	disabledFg.dispose();
	enabledBg.dispose();
	enabledFg.dispose();
}

@Test
public void test_setEditableZ(){
	text.setEditable(true);
	assertTrue(":a:", text.getEditable());
	text.setEditable(false);
	assertFalse(":b:", text.getEditable());
	text.setEditable(false);
	assertFalse(":c:", text.getEditable());
	text.setEditable(true);
	assertTrue(":d:", text.getEditable());
}

@Override
@Test
public void test_setFontLorg_eclipse_swt_graphics_Font(){
	FontData fontData = text.getFont().getFontData()[0];
	int lineHeight;
	Font font;

	font = new Font(text.getDisplay(), fontData.getName(), 20, fontData.getStyle());
	text.setFont(font);
	lineHeight = text.getLineHeight();
	text.setFont(null);
	font.dispose();
	font = new Font(text.getDisplay(), fontData.getName(), 25, fontData.getStyle());
	text.setFont(font);
	assertTrue(":a:", text.getLineHeight() > lineHeight && font.equals(text.getFont()));
	text.setFont(null);
	font.dispose();
}

@Test
public void test_setHorizontalIndexI(){
	text.setHorizontalIndex(-1);
	assertTrue(":a:", text.getHorizontalIndex() == 0);
	text.setHorizontalIndex(1);
	assertTrue(":b:", text.getHorizontalIndex() == 0);

	text.setText("Line0");
	text.setHorizontalIndex(-1);
	assertTrue(":c:", text.getHorizontalIndex() == 0);
	text.setHorizontalIndex(1);
	assertTrue(":d:", text.getHorizontalIndex() == 1);
	text.setHorizontalIndex(500);
	assertTrue(":e:", text.getHorizontalIndex() > 0);
	text.setHorizontalIndex(-1);
	assertTrue(":f:", text.getHorizontalIndex() == 0);
	text.setHorizontalIndex(1);
	assertTrue(":g:", text.getHorizontalIndex() == 1);

	text.setText("");
	text.setHorizontalIndex(2);
	assertTrue(":h:", text.getHorizontalIndex() == 0);

	// make sure the widget can be scrolled
	shell.open();
	text.setSize(10, 50);
	text.setText("Line0");
	text.setHorizontalIndex(1);
	assertTrue(":i:", text.getHorizontalIndex() == 1);
}

@Test
public void test_setHorizontalPixelI(){
	text.setHorizontalPixel(-1);
	assertTrue(":a:", text.getHorizontalPixel() == 0);
	text.setHorizontalPixel(1);
	assertTrue(":b:", text.getHorizontalPixel() == 0);

	text.setText("Line0");
	text.setHorizontalPixel(-1);
	assertTrue(":c:", text.getHorizontalPixel() == 0);
	text.setHorizontalPixel(1);
	assertTrue(":d:", text.getHorizontalPixel() == 1);
	text.setHorizontalPixel(500);
	assertTrue(":e:", text.getHorizontalPixel() > 0);
	text.setHorizontalPixel(-1);
	assertTrue(":f:", text.getHorizontalPixel() == 0);
	text.setHorizontalPixel(25);
	assertTrue(":g:", text.getHorizontalPixel() == 25);

	text.setText("");
	text.setHorizontalPixel(2);
	assertTrue(":h:", text.getHorizontalPixel() == 0);

	// make sure the widget can be scrolled
	shell.open();
	text.setSize(10, 50);
	text.setText("Line0");
	text.setHorizontalPixel(5);
	assertTrue(":i:", text.getHorizontalPixel() == 5);
}

@Test
public void test_setLineBackgroundIILorg_eclipse_swt_graphics_Color(){
	String textString;

	textString = "L1\nL2\nL3\nL4";
	text.setText(textString);
	text.setLineBackground(0,1,getColor(RED));
	text.setLineBackground(1,1,getColor(YELLOW));
	text.setLineBackground(2,1,getColor(BLUE));
	text.setLineBackground(3,1,getColor(GREEN));
	text.replaceTextRange(1,5,"");
	assertTrue(":0a:", text.getLineBackground(0) == getColor(RED));
	assertTrue(":0a:", text.getLineBackground(1) == getColor(GREEN));
	text.setText(textString);
	text.setLineBackground(0,1,getColor(RED));
	text.setLineBackground(1,1,getColor(YELLOW));
	text.setLineBackground(2,1,getColor(BLUE));
	text.setLineBackground(3,1,getColor(GREEN));
	text.replaceTextRange(0,4,"");
	assertTrue(":0b:", text.getLineBackground(0) == getColor(YELLOW));
	assertTrue(":0b:", text.getLineBackground(1) == getColor(BLUE));
	assertTrue(":0b:", text.getLineBackground(2) == getColor(GREEN));
	text.setText(textString);
	text.setLineBackground(0,1,getColor(RED));
	text.setLineBackground(1,1,getColor(YELLOW));
	text.setLineBackground(2,1,getColor(BLUE));
	text.setLineBackground(3,1,getColor(GREEN));
	text.replaceTextRange(1,1,"");
	assertTrue(":0c:", text.getLineBackground(0) == getColor(RED));
	assertTrue(":0c:", text.getLineBackground(1) == getColor(YELLOW));
	assertTrue(":0c:", text.getLineBackground(2) == getColor(BLUE));
	assertTrue(":0c:", text.getLineBackground(3) == getColor(GREEN));
	text.setText(textString);
	text.setLineBackground(0,1,getColor(RED));
	text.setLineBackground(1,1,getColor(YELLOW));
	text.setLineBackground(2,1,getColor(BLUE));
	text.setLineBackground(3,1,getColor(GREEN));
	text.replaceTextRange(0,6,"");
	assertTrue(":0d:", text.getLineBackground(0) == getColor(BLUE));
	assertTrue(":0d:", text.getLineBackground(1) == getColor(GREEN));
	text.setText(textString);
	text.setLineBackground(0,1,getColor(RED));
	text.setLineBackground(1,1,getColor(YELLOW));
	text.setLineBackground(2,1,getColor(BLUE));
	text.setLineBackground(3,1,getColor(GREEN));
	text.replaceTextRange(1,3,"");
	assertTrue(":0e:", text.getLineBackground(0) == getColor(RED));
	assertTrue(":0e:", text.getLineBackground(1) == getColor(BLUE));
	assertTrue(":0e:", text.getLineBackground(2) == getColor(GREEN));

	textString = "L1\nL2";
	text.setText(textString);
	text.setLineBackground(0,1,getColor(RED));
	text.setLineBackground(1,1,getColor(YELLOW));
	text.replaceTextRange(1,4,"");
	assertTrue(":0a1:", text.getLineBackground(0) == getColor(RED));
	text.setText(textString);
	text.setLineBackground(0,1,getColor(RED));
	text.setLineBackground(1,1,getColor(YELLOW));
	text.replaceTextRange(0,4,"");
	assertTrue(":0b1:", text.getLineBackground(0) == getColor(YELLOW));
	text.setText(textString);
	text.setLineBackground(0,1,getColor(RED));
	text.setLineBackground(1,1,getColor(YELLOW));
	text.replaceTextRange(1,1,"");
	assertTrue(":0c1:", text.getLineBackground(0) == getColor(RED));
	assertTrue(":0c1:", text.getLineBackground(1) == getColor(YELLOW));
	text.setText(textString);
	text.setLineBackground(0,1,getColor(RED));
	text.setLineBackground(1,1,getColor(YELLOW));
	text.replaceTextRange(0,5,"");
	assertNull(":0d1:", text.getLineBackground(0));
	text.setText(textString);
	text.setLineBackground(0,1,getColor(RED));
	text.setLineBackground(1,1,getColor(YELLOW));
	text.replaceTextRange(1,3,"");
	assertTrue(":0e1:", text.getLineBackground(0) == getColor(RED));
	assertNull(":0e1:", text.getLineBackground(1));
	textString = "L1\nL2";
	text.setText(textString);
	text.setLineBackground(1,1,getColor(YELLOW));
	text.replaceTextRange(1,4,"");
	assertNull(":0f1:", text.getLineBackground(0));
	text.setText(textString+"\n");
	text.setLineBackground(0,1,getColor(RED));
	text.setLineBackground(1,1,getColor(YELLOW));
	text.replaceTextRange(0,6,"");
	assertNull(":0g1:", text.getLineBackground(0));

	text.setText(textString);
	text.setLineBackground(0,0,getColor(RED));
	assertNull(":1:", text.getLineBackground(0));
	text.setLineBackground(0,1,getColor(RED));
	assertTrue(":1:", text.getLineBackground(0) == getColor(RED));

	textString = "New Line1\nNew Line2\nNew Line3\nNew Line4";
	text.setText(textString);
	text.setLineBackground(0,2,getColor(RED));
	text.setLineBackground(2,2,getColor(YELLOW));
	text.replaceTextRange(0,0,"\n");
	assertNull(":2:", text.getLineBackground(0));
	assertTrue(":2:", text.getLineBackground(1) == getColor(RED));
	assertTrue(":2:", text.getLineBackground(2) == getColor(RED));
	assertTrue(":2:", text.getLineBackground(3) == getColor(YELLOW));
	assertTrue(":2:", text.getLineBackground(4) == getColor(YELLOW));

	textString = "New Line1\nNew Line2\nNew Line3\nNew Line4";
	text.setText(textString);
	text.setLineBackground(0,2,getColor(RED));
	text.setLineBackground(2,2,getColor(YELLOW));
	text.replaceTextRange(0,20,"");
	assertTrue(":3:", text.getLineBackground(0) == getColor(YELLOW));
	assertTrue(":3:", text.getLineBackground(1) == getColor(YELLOW));

	textString = "Line1\nLine2\nLine3\nLine4";
	text.setText(textString);
	text.setLineBackground(0,1,getColor(RED));
	text.setLineBackground(2,1,getColor(YELLOW));
	text.replaceTextRange(0,18,"");
	assertNull(":4:", text.getLineBackground(0));

	textString = "Line1\nLine2\nLine3\nLine4";
	text.setText(textString);
	text.setLineBackground(0,1,getColor(RED));
	text.setLineBackground(2,1,getColor(YELLOW));
	text.replaceTextRange(0,18,"L1\nL2\nL3\n");
	assertNull(":5:", text.getLineBackground(0));
	assertNull(":5:", text.getLineBackground(1));
	assertNull(":5:", text.getLineBackground(2));
	assertNull(":5:", text.getLineBackground(3));

	textString = "Line1\nLine2\nLine3\nLine4";
	text.setText(textString);
	text.setLineBackground(0,1,getColor(RED));
	text.setLineBackground(1,1,getColor(YELLOW));
	text.setLineBackground(2,1,getColor(BLUE));
	text.setLineBackground(3,1,getColor(GREEN));
	text.replaceTextRange(3,9,"L1\nL2\n");
	assertTrue(":6a:", text.getLineBackground(0) == getColor(RED));
	assertNull(":6a:", text.getLineBackground(1));
	textString = "Line1\nLine2\nLine3\nLine4";
	text.setText(textString);
	text.setLineBackground(0,1,getColor(RED));
	text.setLineBackground(1,1,getColor(YELLOW));
	text.setLineBackground(2,1,getColor(BLUE));
	text.setLineBackground(3,1,getColor(GREEN));
	text.replaceTextRange(11,11,"L3\nL4");
	assertNull(":6b:", text.getLineBackground(2));
	assertNull(":6b:", text.getLineBackground(3));

	textString = "Line1\nLine2\nLine3\nLine4";
	text.setText(textString);
	text.setLineBackground(0,1,getColor(RED));
	text.setLineBackground(1,1,getColor(YELLOW));
	text.setLineBackground(2,1,getColor(BLUE));
	text.setLineBackground(3,1,getColor(GREEN));
	text.replaceTextRange(0,18,"L1\n");
	assertNull(":7:", text.getLineBackground(0));
	assertTrue(":7:", text.getLineBackground(1) == getColor(GREEN));
}

@Test
public void test_setSelectionI() {
	int[] invalid = {-1, 100, 12};

	for (int start : invalid) {
		try {
			text.setSelection(start);
		} catch (IllegalArgumentException e) {
			fail("should not throw exception for out of range");
		}
	}
	text.setText("01234567890");
	assertEquals(0, text.getCaretOffset());
	text.setSelection(1);
	assertEquals(1, text.getCaretOffset());
	text.setSelection(11);
	assertEquals(11, text.getCaretOffset());

	for (int start : invalid) {
		try {
			text.setSelection(start);
		}
		catch (IllegalArgumentException e) {
			fail("should not throw exception for out of range");
		}
	}
}

@Test
public void test_setSelectionLorg_eclipse_swt_graphics_Point() {
	Point[] invalidRanges = {new Point(-1, 0), new Point(-1, -1), new Point(100, 1),
		new Point(100, -1), new Point(11, 12), new Point(10, 12)};

	for (Point invalidRange : invalidRanges) {
		try {
			text.setSelection(invalidRange);
		}
		catch (IllegalArgumentException e) {
			fail("should not throw exception for out of range");
		}
	}
	text.setText("01234567890");
	assertEquals("", text.getSelectionText());
	text.setSelection(3, 7);
	assertEquals("3456", text.getSelectionText());

	for (Point invalidRange : invalidRanges) {
		try {
			text.setSelection(invalidRange);
		}
		catch (IllegalArgumentException e) {
			fail("should not throw exception for out of range");
		}
	}
}

@Test
public void test_setSelectionII(){
	int[][] invalidRanges = {{-1, 0}, {-1, -1}, {100, 1}, {100, -1}, {11, 12}, {10, 12}, {2, -3}, {50, -1}};

	for (int[] invalidRange : invalidRanges) {
		int start = invalidRange[0];
		int end = invalidRange[1];

		try {
			text.setSelection(start, end);
		}
		catch (IllegalArgumentException e) {
			fail("should not throw exception for out of range");
		}
	}
	text.setText("01234567890");
	assertEquals("", text.getSelectionText());
	text.setSelection(3, 7);
	assertEquals("3456", text.getSelectionText());
	text.setSelection(3, 0);
	assertEquals("012", text.getSelectionText());
	assertEquals(0, text.getCaretOffset());

	for (int[] invalidRange : invalidRanges) {
		int start = invalidRange[0];
		int end = invalidRange[1];

		try {
			text.setSelection(start, end);
		}
		catch (IllegalArgumentException e) {
			fail("should not throw exception for out of range");
		}
	}
}

@Test
public void test_addSelectionListener() {
	text.setText("0123456789");
	class TestSelectionListener extends SelectionAdapter {
		public int counter;
		public Point eventSelection = new Point(0, 0);
		@Override
		public void widgetSelected(SelectionEvent e) {
			eventSelection.x = e.x;
			eventSelection.y = e.y;
			counter++;
		}
	}
	final TestSelectionListener selectionListener = new TestSelectionListener();
	text.addSelectionListener(selectionListener);

	assertEquals(0, selectionListener.counter);
	assertEquals(new Point(0, 0), selectionListener.eventSelection);

	text.invokeAction(ST.COLUMN_NEXT);
	assertEquals(new Point(1, 1), text.getSelection());

	text.invokeAction(ST.SELECT_COLUMN_NEXT);
	assertEquals(1, selectionListener.counter);
	assertEquals(new Point(1, 2), selectionListener.eventSelection);

	text.invokeAction(ST.SELECT_COLUMN_NEXT);
	assertEquals(2, selectionListener.counter);
	assertEquals(new Point(1, 3), selectionListener.eventSelection);

	// replace text while selection is non-empty:
	text.replaceTextRange(0, 1, "a");
	assertEquals(2, selectionListener.counter);
	assertEquals(new Point(1, 3), selectionListener.eventSelection);

	text.replaceTextRange(9, 1, "z");
	assertEquals(2, selectionListener.counter);
	assertEquals(new Point(1, 3), selectionListener.eventSelection);

	text.replaceTextRange(0, 1, "ab");
	assertEquals(3, selectionListener.counter);
	assertEquals(new Point(2, 4), selectionListener.eventSelection);
	assertEquals(new Point(2, 4), text.getSelection());

	text.invokeAction(ST.COLUMN_NEXT);
	assertEquals(4, selectionListener.counter);
	assertEquals(new Point(4, 4), selectionListener.eventSelection);
	assertEquals(new Point(4, 4), text.getSelection());

	// replace text while selection is empty:
	text.replaceTextRange(0, 2, "a");
	assertEquals(4, selectionListener.counter);
	assertEquals(new Point(3, 3), text.getSelection());

	text.replaceTextRange(9, 1, "9");
	assertEquals(4, selectionListener.counter);
	assertEquals(new Point(3, 3), text.getSelection());

	text.replaceTextRange(0, 1, "0");
	assertEquals(4, selectionListener.counter);
	assertEquals(new Point(3, 3), text.getSelection());

	// replace text that overlaps empty selection:
	text.replaceTextRange(0, 9, "");
	assertEquals(4, selectionListener.counter);
	assertEquals(new Point(0, 0), text.getSelection());
}

@Test
public void test_setSelectionBackgroundLorg_eclipse_swt_graphics_Color(){
	text.setSelectionBackground(getColor(YELLOW));
	assertTrue(":1a:", text.getSelectionBackground() ==  getColor(YELLOW));
	text.setSelectionBackground(null);
	assertTrue(":1b:", text.getSelectionBackground().equals(text.getDisplay().getSystemColor(SWT.COLOR_LIST_SELECTION)));
}

@Test
public void test_setSelectionForegroundLorg_eclipse_swt_graphics_Color(){
	text.setSelectionForeground(getColor(RED));
	assertTrue(":1a:", text.getSelectionForeground() ==  getColor(RED));
	text.setSelectionForeground(null);
	assertTrue(":1b:", text.getSelectionForeground().equals(text.getDisplay().getSystemColor(SWT.COLOR_LIST_SELECTION_TEXT)));
}

@Test
public void test_setSelectionRangeII(){
	// setSelectionRange already tested in test_getSelectionRange
}

@Test
public void test_setStyleRangeLorg_eclipse_swt_custom_StyleRange(){
	StyleRange[] styles;
	String textString = textString();

	/*
		defaultStyles

			(0,48,RED,YELLOW),
			(58,10,BLUE,CYAN),
			(68,10,GREEN,PURPLE)
	*/


	text.setText(textString);

	// No overlap with existing styles
	text.setStyleRanges(defaultStyles());
	text.setStyleRange(getStyle(48,5,YELLOW,RED));
	styles = text.getStyleRanges();
	assertTrue(":1:", styles.length == 4);
	assertTrue(":1:", styles[0].equals(getStyle(0,48,RED,YELLOW)));
	assertTrue(":1:", styles[1].equals(getStyle(48,5,YELLOW,RED)));
	assertTrue(":1:", styles[2].equals(getStyle(58,10,BLUE,CYAN)));
	assertTrue(":1:", styles[3].equals(getStyle(68,10,GREEN,PURPLE)));

	// Overlap middle of one style - partial
	text.setStyleRanges(defaultStyles());
	text.setStyleRange(getStyle(10,10,YELLOW,RED));
	styles = text.getStyleRanges();
	assertTrue(":2:", styles.length == 5);
	assertTrue(":2:", styles[0].equals(getStyle(0,10,RED,YELLOW)));
	assertTrue(":2:", styles[1].equals(getStyle(10,10,YELLOW,RED)));
	assertTrue(":2:", styles[2].equals(getStyle(20,28,RED,YELLOW)));
	assertTrue(":2:", styles[3].equals(getStyle(58,10,BLUE,CYAN)));
	assertTrue(":2:", styles[4].equals(getStyle(68,10,GREEN,PURPLE)));
	text.setStyleRange(null);
	styles = text.getStyleRanges();
	assertTrue(":2:", styles.length == 0);

	// Overlap middle of one style - full
	text.setStyleRanges(defaultStyles());
	text.setStyleRange(getStyle(58,10,YELLOW,RED));
	styles = text.getStyleRanges();
	assertTrue(":3:", styles.length == 3);
	assertTrue(":3:", styles[0].equals(getStyle(0,48,RED,YELLOW)));
	assertTrue(":3:", styles[1].equals(getStyle(58,10,YELLOW,RED)));
	assertTrue(":3:", styles[2].equals(getStyle(68,10,GREEN,PURPLE)));

	// Overlap end of one style
	text.setStyleRanges(defaultStyles());
	text.setStyleRange(getStyle(38,15,YELLOW,RED));
	styles = text.getStyleRanges();
	assertTrue(":4:", styles.length == 4);
	assertTrue(":4:", styles[0].equals(getStyle(0,38,RED,YELLOW)));
	assertTrue(":4:", styles[1].equals(getStyle(38,15,YELLOW,RED)));
	assertTrue(":4:", styles[2].equals(getStyle(58,10,BLUE,CYAN)));
	assertTrue(":4:", styles[3].equals(getStyle(68,10,GREEN,PURPLE)));

	// Overlap beginning of one style
	text.setStyleRanges(defaultStyles());
	text.setStyleRange(getStyle(50,10,YELLOW,RED));
	styles = text.getStyleRanges();
	assertTrue(":5:", styles.length == 4);
	assertTrue(":5:", styles[0].equals(getStyle(0,48,RED,YELLOW)));
	assertTrue(":5:", styles[1].equals(getStyle(50,10,YELLOW,RED)));
	assertTrue(":5:", styles[2].equals(getStyle(60,8,BLUE,CYAN)));
	assertTrue(":5:", styles[3].equals(getStyle(68,10,GREEN,PURPLE)));

	// Overlap complete style
	text.setStyleRanges(defaultStyles());
	text.setStyleRange(getStyle(48,20,YELLOW,RED));
	styles = text.getStyleRanges();
	assertTrue(":6:", styles.length == 3);
	assertTrue(":6:", styles[0].equals(getStyle(0,48,RED,YELLOW)));
	assertTrue(":6:", styles[1].equals(getStyle(48,20,YELLOW,RED)));
	assertTrue(":6:", styles[2].equals(getStyle(68,10,GREEN,PURPLE)));

	// reset the environment
	text.dispose();
	text = new StyledText(shell, SWT.NULL);
	setWidget(text);

	text.setText(textString);
	text.setStyleRange(getStyle(0,48,RED,YELLOW));
	text.setStyleRange(getStyle(48,20,BLUE,CYAN));
	text.setStyleRange(getStyle(68,10,GREEN,PURPLE));
	// should be merged with style before it
	text.setStyleRange(getStyle(48,10,RED,YELLOW));
	styles = text.getStyleRanges();
	assertTrue(":1:", styles.length == 3);
	assertTrue(":1:", styles[0].equals(getStyle(0,58,RED,YELLOW)));
	assertTrue(":1:", styles[1].equals(getStyle(58,10,BLUE,CYAN)));
	assertTrue(":1:", styles[2].equals(getStyle(68,10,GREEN,PURPLE)));

	text.setText(textString);
	text.setStyleRange(getStyle(0,10,RED,YELLOW));
	text.setStyleRange(getStyle(15,10,BLUE,CYAN));
	text.setStyleRange(getStyle(25,10,GREEN,PURPLE));
	// should be merged with style after it
	text.setStyleRange(getStyle(11,4,BLUE,CYAN));
	styles = text.getStyleRanges();
	assertTrue(":2:", styles.length == 3);
	assertTrue(":2:", styles[0].equals(getStyle(0,10,RED,YELLOW)));
	assertTrue(":2:", styles[1].equals(getStyle(11,14,BLUE,CYAN)));
	assertTrue(":2:", styles[2].equals(getStyle(25,10,GREEN,PURPLE)));

	text.setText(textString);
	text.setStyleRange(getStyle(0,10,RED,YELLOW));
	text.setStyleRange(getStyle(15,10,BLUE,CYAN));
	text.setStyleRange(getStyle(25,10,GREEN,PURPLE));
	// should be merged with style after it
	text.setStyleRange(getStyle(5,15,BLUE,CYAN));
	styles = text.getStyleRanges();
	assertTrue(":3:", styles.length == 3);
	assertTrue(":3:", styles[0].equals(getStyle(0,5,RED,YELLOW)));
	assertTrue(":3:", styles[1].equals(getStyle(5,20,BLUE,CYAN)));
	assertTrue(":3:", styles[2].equals(getStyle(25,10,GREEN,PURPLE)));

	text.setText("01234567890123456789");
	text.setStyleRange(getStyle(0,10,RED,YELLOW));
	text.setStyleRange(getStyle(10,10,BLUE,CYAN));
	text.setStyleRange(getStyle(5,3,RED,YELLOW));
	text.setStyleRange(getStyle(12,5,BLUE,CYAN));
	styles = text.getStyleRanges();
	assertTrue(":4:", styles.length == 2);
	assertTrue(":4:", styles[0].equals(getStyle(0,10,RED,YELLOW)));
	assertTrue(":4:", styles[1].equals(getStyle(10,10,BLUE,CYAN)));

	text.setText("0123456789012345");
	text.setStyleRange(getStyle(0,5,RED,YELLOW));
	text.setStyleRange(getStyle(5,5,BLUE,CYAN));
	text.setStyleRange(getStyle(10,5,GREEN,PURPLE));
	text.setStyleRange(getStyle(5,5,RED,YELLOW));
	text.setStyleRange(getStyle(10,5,RED,YELLOW));
	styles = text.getStyleRanges();
	assertTrue(":5:", styles.length == 1);
	assertTrue(":5:", styles[0].equals(getStyle(0,15,RED,YELLOW)));

	text.setText("012345678901234");
	text.setStyleRange(getStyle(0,5,RED,YELLOW));
	text.setStyleRange(getStyle(10,5,BLUE,CYAN));
	// should be merged
	text.setStyleRange(getStyle(5,7,BLUE,CYAN));
	styles = text.getStyleRanges();
	assertTrue(":6:", styles.length == 2);
	assertTrue(":6:", styles[0].equals(getStyle(0,5,RED,YELLOW)));
	assertTrue(":6:", styles[1].equals(getStyle(5,10,BLUE,CYAN)));

	text.setText("123 456 789");
	text.setStyleRange(getStyle(4,3,BLUE,null));
	text.setStyleRange(getStyle(8,3,RED,null));
	text.setStyleRange(getStyle(5,2,BLUE,null));
	styles = text.getStyleRanges();
	assertTrue(":7:", styles.length == 2);
	assertTrue(":7:", styles[0].equals(getStyle(4,3,BLUE,null)));
	assertTrue(":7:", styles[1].equals(getStyle(8,3,RED,null)));

	text.setText("123 456 789");
	text.setStyleRange(getStyle(4,3,BLUE,null));
	text.setStyleRange(getStyle(8,3,RED,null));
	text.setStyleRange(getStyle(7,4,BLUE,null));
	styles = text.getStyleRanges();
	assertTrue(":8:", styles.length == 1);
	assertTrue(":8:", styles[0].equals(getStyle(4,7,BLUE,null)));

	text.setText("123 456 789 ABC DEF");
	text.setStyleRange(getStyle(0,4,BLUE,null));
	text.setStyleRange(getStyle(4,4,RED,null));
	text.setStyleRange(getStyle(8,4,BLUE,null));
	text.setStyleRange(getStyle(12,4,RED,null));
	text.setStyleRange(getStyle(16,3,BLUE,null));
	text.setStyleRange(getStyle(5,14,RED,null));
	styles = text.getStyleRanges();
	assertTrue(":9:", styles.length == 2);
	assertTrue(":9:", styles[0].equals(getStyle(0,4,BLUE,null)));
	assertTrue(":9:", styles[1].equals(getStyle(4,15,RED,null)));

	// reset the environment
	text.dispose();
	text = new StyledText(shell, SWT.NULL);
	setWidget(text);

	text.setText(textString);
	text.setStyleRange(getStyle(0,48,RED,YELLOW));
	text.setStyleRange(getStyle(48,20,BLUE,CYAN));
	text.setStyleRange(getStyle(68,10,GREEN,PURPLE));
	text.setStyleRange(getStyle(38,20,null,null));
	styles = text.getStyleRanges();
	assertTrue(":1:", styles.length == 3);
	assertTrue(":1:", styles[0].equals(getStyle(0,38,RED,YELLOW)));
	assertTrue(":1:", styles[1].equals(getStyle(58,10,BLUE,CYAN)));
	assertTrue(":1:", styles[2].equals(getStyle(68,10,GREEN,PURPLE)));

	text.setText(textString);
	int length = textString.length();
	text.setStyleRange(getStyle(0,48,RED,YELLOW));
	text.setStyleRange(getStyle(48,20,BLUE,CYAN));
	text.setStyleRange(getStyle(68,10,GREEN,PURPLE));
	text.setStyleRange(getStyle(0,length,null,null));
	styles = text.getStyleRanges();
	assertTrue(":2:", styles.length == 0);

	text.setText("01234567890123456789");
	text.setStyleRange(getStyle(0,3,RED,YELLOW));
	text.setStyleRange(getStyle(5,3,BLUE,CYAN));
	text.setStyleRange(getStyle(9,8,GREEN,PURPLE));
	text.setStyleRange(getStyle(0,10,GREEN,PURPLE));
	styles = text.getStyleRanges();
	assertTrue(":3:", styles.length == 1);
	assertTrue(":3:", styles[0].equals(getStyle(0,17,GREEN,PURPLE)));

	text.setText("0123456789012345");
	text.setStyleRange(getStyle(0,5,RED,YELLOW));
	text.setStyleRange(getStyle(5,5,BLUE,CYAN));
	text.setStyleRange(getStyle(10,5,GREEN,PURPLE));
	text.setStyleRange(getStyle(7,9,RED,YELLOW));
	styles = text.getStyleRanges();
	assertTrue(":4:", styles.length == 3);
	assertTrue(":4:", styles[0].equals(getStyle(0,5,RED,YELLOW)));
	assertTrue(":4:", styles[1].equals(getStyle(5,2,BLUE,CYAN)));
	assertTrue(":4:", styles[2].equals(getStyle(7,9,RED,YELLOW)));

	text.setText("012345678901234");
	text.setStyleRange(getStyle(0,5,RED,YELLOW));
	text.setStyleRange(getStyle(10,5,BLUE,CYAN));
	text.setStyleRange(getStyle(3,10,GREEN,PURPLE));
	styles = text.getStyleRanges();
	assertTrue(":5:", styles.length == 3);
	assertTrue(":5:", styles[0].equals(getStyle(0,3,RED,YELLOW)));
	assertTrue(":5:", styles[1].equals(getStyle(3,10,GREEN,PURPLE)));
	assertTrue(":5:", styles[2].equals(getStyle(13,2,BLUE,CYAN)));

	text.setText("redgreenblueyellowcyanpurple");
	text.setStyleRange(getStyle(0,3,RED,null));
	text.setStyleRange(getStyle(3,5,GREEN,null));
	text.setStyleRange(getStyle(8,4,BLUE,null));
	text.setStyleRange(getStyle(12,6,YELLOW,null));
	text.setStyleRange(getStyle(18,4,CYAN,null));
	text.setStyleRange(getStyle(22,6,PURPLE,null));
	text.setStyleRange(getStyle(8,14,null,RED));
	styles = text.getStyleRanges();
	assertTrue(":6:", styles.length == 4);
	assertTrue(":6:", styles[0].equals(getStyle(0,3,RED,null)));
	assertTrue(":6:", styles[1].equals(getStyle(3,5,GREEN,null)));
	assertTrue(":6:", styles[2].equals(getStyle(8,14,null,RED)));
	assertTrue(":6:", styles[3].equals(getStyle(22,6,PURPLE,null)));


	text.setText("redgreenblueyellowcyanpurple");
	text.setStyleRange(getStyle(0,3,RED,null));
	text.setStyleRange(getStyle(3,5,GREEN,null));
	text.setStyleRange(getStyle(8,4,BLUE,null));
	text.setStyleRange(getStyle(12,6,YELLOW,null));
	text.setStyleRange(getStyle(18,4,CYAN,null));
	text.setStyleRange(getStyle(22,6,PURPLE,null));
	text.setStyleRange(getStyle(0,28,null,null));
	styles = text.getStyleRanges();
	assertTrue(":7:", styles.length == 0);


/*
	text.setText("This\r\na\tAnother line.");
	text.setStyleRange(getStyle(3,3,BLUE,null));
	text.setStyleRange(getStyle(7,8,BLUE,null));
	text.setStyleRange(getStyle(6,1,BLUE,null));
	StyledTextEvent event = new StyledTextEvent();
	event.detail = 6;
	event.text = "a\tAnother line.";
	text.notifyListener(ST.LineGetStyle, event);
	assertTrue(":8:", event.styles[0].equals(getStyle(3,4,BLUE,null)));
*/


	text.setText("123 456 789");
	text.setStyleRange(getStyle(4,3,BLUE,null));
	text.setStyleRange(getStyle(8,3,RED,null));
	text.setStyleRange(getStyle(5,5,BLUE,null));
	styles = text.getStyleRanges();
	assertTrue(":9:", styles.length == 2);
	assertTrue(":9:", styles[0].equals(getStyle(4,6,BLUE,null)));
	assertTrue(":9:", styles[1].equals(getStyle(10,1,RED,null)));


	// reset the environment
	text.dispose();
	text = new StyledText(shell, SWT.NULL);
	setWidget(text);

	textString = textString();

	text.setText(textString);
	text.setStyleRange(getStyle(0,48,RED,YELLOW));
	text.setStyleRange(getStyle(48,20,BLUE,CYAN));
	text.setStyleRange(getStyle(68,10,GREEN,PURPLE));
	// should be merged with style before it
	text.setStyleRange(getStyle(48,10,RED,YELLOW));
	styles = text.getStyleRanges();
	assertTrue(":1:", styles.length == 3);
	assertTrue(":1:", styles[0].equals(getStyle(0,58,RED,YELLOW)));
	assertTrue(":1:", styles[1].equals(getStyle(58,10,BLUE,CYAN)));
	assertTrue(":1:", styles[2].equals(getStyle(68,10,GREEN,PURPLE)));


	text.setText(textString);
	text.setStyleRange(getStyle(0,10,RED,YELLOW));
	text.setStyleRange(getStyle(15,10,BLUE,CYAN));
	text.setStyleRange(getStyle(25,10,GREEN,PURPLE));
	// should be merged with style after it
	text.setStyleRange(getStyle(11,4,BLUE,CYAN));
	styles = text.getStyleRanges();
	assertTrue(":2:", styles.length == 3);
	assertTrue(":2:", styles[0].equals(getStyle(0,10,RED,YELLOW)));
	assertTrue(":2:", styles[1].equals(getStyle(11,14,BLUE,CYAN)));
	assertTrue(":2:", styles[2].equals(getStyle(25,10,GREEN,PURPLE)));

	text.setText(textString);
	text.setStyleRange(getStyle(0,10,RED,YELLOW));
	text.setStyleRange(getStyle(15,10,BLUE,CYAN));
	text.setStyleRange(getStyle(25,10,GREEN,PURPLE));
	// should be merged with style after it
	text.setStyleRange(getStyle(5,15,BLUE,CYAN));
	styles = text.getStyleRanges();
	assertTrue(":3:", styles.length == 3);
	assertTrue(":3:", styles[0].equals(getStyle(0,5,RED,YELLOW)));
	assertTrue(":3:", styles[1].equals(getStyle(5,20,BLUE,CYAN)));
	assertTrue(":3:", styles[2].equals(getStyle(25,10,GREEN,PURPLE)));



	text.setText("01234567890123456789");
	text.setStyleRange(getStyle(0,10,RED,YELLOW));
	text.setStyleRange(getStyle(10,10,BLUE,CYAN));
	text.setStyleRange(getStyle(5,3,RED,YELLOW));
	text.setStyleRange(getStyle(12,5,BLUE,CYAN));
	styles = text.getStyleRanges();
	assertTrue(":4:", styles.length == 2);
	assertTrue(":4:", styles[0].equals(getStyle(0,10,RED,YELLOW)));
	assertTrue(":4:", styles[1].equals(getStyle(10,10,BLUE,CYAN)));

	text.setText("0123456789012345");
	text.setStyleRange(getStyle(0,5,RED,YELLOW));
	text.setStyleRange(getStyle(5,5,BLUE,CYAN));
	text.setStyleRange(getStyle(10,5,GREEN,PURPLE));
	text.setStyleRange(getStyle(5,5,RED,YELLOW));
	text.setStyleRange(getStyle(10,5,RED,YELLOW));
	styles = text.getStyleRanges();
	assertTrue(":5:", styles.length == 1);
	assertTrue(":5:", styles[0].equals(getStyle(0,15,RED,YELLOW)));

	text.setText("012345678901234");
	text.setStyleRange(getStyle(0,5,RED,YELLOW));
	text.setStyleRange(getStyle(10,5,BLUE,CYAN));
	// should be merged
	text.setStyleRange(getStyle(5,7,BLUE,CYAN));
	styles = text.getStyleRanges();
	assertTrue(":6:", styles.length == 2);
	assertTrue(":6:", styles[0].equals(getStyle(0,5,RED,YELLOW)));
	assertTrue(":6:", styles[1].equals(getStyle(5,10,BLUE,CYAN)));


	// reset the environment
	text.dispose();
	text = new StyledText(shell, SWT.NULL);
	setWidget(text);

	textString = textString();


	/*
		defaultStyles

			(0,48,RED,YELLOW),
			(58,10,BLUE,CYAN),
			(68,10,GREEN,PURPLE)
	*/


	text.setText(textString);

	// End/Beginning overlap
	text.setStyleRanges(defaultStyles());
	text.setStyleRange(getStyle(38,25,YELLOW,RED));
	styles = text.getStyleRanges();
	assertTrue(":1:", styles.length == 4);
	assertTrue(":1:", styles[0].equals(getStyle(0,38,RED,YELLOW)));
	assertTrue(":1:", styles[1].equals(getStyle(38,25,YELLOW,RED)));
	assertTrue(":1:", styles[2].equals(getStyle(63,5,BLUE,CYAN)));
	assertTrue(":1:", styles[3].equals(getStyle(68,10,GREEN,PURPLE)));
	text.setStyleRanges(defaultStyles());
	text.setStyleRange(getStyle(63,10,YELLOW,RED));
	styles = text.getStyleRanges();
	assertTrue(":1:", styles.length == 4);
	assertTrue(":1:", styles[0].equals(getStyle(0,48,RED,YELLOW)));
	assertTrue(":1:", styles[1].equals(getStyle(58,5,BLUE,CYAN)));
	assertTrue(":1:", styles[2].equals(getStyle(63,10,YELLOW,RED)));
	assertTrue(":1:", styles[3].equals(getStyle(73,5,GREEN,PURPLE)));


	// Complete overlap
	text.setStyleRanges(defaultStyles());
	text.setStyleRange(getStyle(0,78,YELLOW,RED));
	styles = text.getStyleRanges();
	assertTrue(":2:", styles.length == 1);
	assertTrue(":2:", styles[0].equals(getStyle(0,78,YELLOW,RED)));
	text.setStyleRanges(defaultStyles());
	text.setStyleRange(getStyle(0,68,YELLOW,RED));
	styles = text.getStyleRanges();
	assertTrue(":2:", styles.length == 2);
	assertTrue(":2:", styles[0].equals(getStyle(0,68,YELLOW,RED)));
	assertTrue(":2:", styles[1].equals(getStyle(68,10,GREEN,PURPLE)));
	text.setStyleRanges(defaultStyles());
	text.setStyleRange(getStyle(58,20,YELLOW,RED));
	styles = text.getStyleRanges();
	assertTrue(":2:", styles.length == 2);
	assertTrue(":2:", styles[0].equals(getStyle(0,48,RED,YELLOW)));
	assertTrue(":2:", styles[1].equals(getStyle(58,20,YELLOW,RED)));


	// 1-N complete, beginning
	text.setText("012345678901234567890123456789");
	text.setStyleRanges(
		new StyleRange[] {getStyle(0,5,RED,RED), getStyle(5,5,YELLOW,YELLOW),
			getStyle(10,5,CYAN,CYAN), getStyle(15,5,BLUE,BLUE),
			getStyle(20,5,GREEN,GREEN), getStyle(25,5,PURPLE,PURPLE)}
	);
	text.setStyleRange(getStyle(5,23,YELLOW,RED));
	styles = text.getStyleRanges();
	assertTrue(":3:", styles.length == 3);
	assertTrue(":3:", styles[0].equals(getStyle(0,5,RED,RED)));
	assertTrue(":3:", styles[1].equals(getStyle(5,23,YELLOW,RED)));
	assertTrue(":3:", styles[2].equals(getStyle(28,2,PURPLE,PURPLE)));

	// end, 1-N complete, beginning
	text.setStyleRanges(
		new StyleRange[] {getStyle(0,5,RED,RED), getStyle(5,5,YELLOW,YELLOW),
			getStyle(10,5,CYAN,CYAN), getStyle(15,5,BLUE,BLUE),
			getStyle(20,5,GREEN,GREEN), getStyle(25,5,PURPLE,PURPLE)}
	);
	text.setStyleRange(getStyle(13,12,YELLOW,RED));
	styles = text.getStyleRanges();
	assertTrue(":3:", styles.length == 5);
	assertTrue(":3:", styles[0].equals(getStyle(0,5,RED,RED)));
	assertTrue(":3:", styles[1].equals(getStyle(5,5,YELLOW,YELLOW)));
	assertTrue(":3:", styles[2].equals(getStyle(10,3,CYAN,CYAN)));
	assertTrue(":3:", styles[3].equals(getStyle(13,12,YELLOW,RED)));
	assertTrue(":3:", styles[4].equals(getStyle(25,5,PURPLE,PURPLE)));


	text.setText("x/");
	text.setStyleRange(getStyle(0,2,YELLOW,null));
	styles = text.getStyleRanges();
	assertTrue(":4:", styles.length == 1);
	assertTrue(":4:", styles[0].equals(getStyle(0,2,YELLOW,null)));
	text.replaceTextRange(2,0,"/");
	styles = text.getStyleRanges();
	assertTrue(":4:", styles.length == 1);
	assertTrue(":4:", styles[0].equals(getStyle(0,2,YELLOW,null)));
	text.setStyleRange(getStyle(0,1,YELLOW,null));
	assertTrue(":4:", styles.length == 1);
	assertTrue(":4:", styles[0].equals(getStyle(0,2,YELLOW,null)));
	text.setStyleRange(getStyle(1,2,RED,null));
	styles = text.getStyleRanges();
	assertTrue(":4:", styles.length == 2);
	assertTrue(":4:", styles[0].equals(getStyle(0,1,YELLOW,null)));
	assertTrue(":4:", styles[1].equals(getStyle(1,2,RED,null)));


	text.setText("xxx/");
	text.setStyleRange(getStyle(0,2,RED,null));
	text.setStyleRange(getStyle(2,2,YELLOW,null));
	styles = text.getStyleRanges();
	assertTrue(":4a:", styles.length == 2);
	assertTrue(":4a:", styles[0].equals(getStyle(0,2,RED,null)));
	assertTrue(":4a:", styles[1].equals(getStyle(2,2,YELLOW,null)));
	text.replaceTextRange(4,0,"/");
	styles = text.getStyleRanges();
	assertTrue(":4a:", styles.length == 2);
	assertTrue(":4a:", styles[0].equals(getStyle(0,2,RED,null)));
	assertTrue(":4a:", styles[1].equals(getStyle(2,2,YELLOW,null)));
	text.setStyleRange(getStyle(2,1,YELLOW,null));
	assertTrue(":4a:", styles.length == 2);
	assertTrue(":4a:", styles[0].equals(getStyle(0,2,RED,null)));
	assertTrue(":4a:", styles[1].equals(getStyle(2,2,YELLOW,null)));
	text.setStyleRange(getStyle(3,2,RED,null));
	styles = text.getStyleRanges();
	assertTrue(":4a:", styles.length == 3);
	assertTrue(":4a:", styles[0].equals(getStyle(0,2,RED,null)));
	assertTrue(":4a:", styles[1].equals(getStyle(2,1,YELLOW,null)));
	assertTrue(":4a:", styles[2].equals(getStyle(3,2,RED,null)));


	text.setText("xxx/");
	text.setStyleRange(getStyle(0,2,RED,null));
	text.setStyleRange(getStyle(2,2,YELLOW,null));
	text.replaceTextRange(4,0,"/");
	styles = text.getStyleRanges();
	text.setStyleRange(getStyle(2,1,YELLOW,null));
	text.setStyleRange(getStyle(2,3,RED,null));
	styles = text.getStyleRanges();
	assertTrue(":4b:", styles.length == 1);
	assertTrue(":4b:", styles[0].equals(getStyle(0,5,RED,null)));


	text.setText("xxx/");
	text.setStyleRange(getStyle(0,2,RED,null));
	text.setStyleRange(getStyle(2,2,YELLOW,null));
	text.replaceTextRange(4,0,"/");
	styles = text.getStyleRanges();
	text.setStyleRange(getStyle(2,1,YELLOW,null));
	text.setStyleRange(getStyle(1,4,YELLOW,null));
	styles = text.getStyleRanges();
	assertTrue(":4c:", styles.length == 2);
	assertTrue(":4c:", styles[0].equals(getStyle(0,1,RED,null)));
	assertTrue(":4c:", styles[1].equals(getStyle(1,4,YELLOW,null)));


	text.setText("New\r\n");
	StyleRange style = getStyle(0,5,null,null);
	style.fontStyle = SWT.BOLD;
	text.setStyleRange(style);
	// styles (0,5,BOLD)
	text.replaceTextRange(3,0,"a"); // "Newa\r\n"
	// styles (0,3,BOLD), (4,2,BOLD)
	style = text.getStyleRangeAtOffset(4);
	style.start = 3;
	style.length = 1;
	text.setStyleRange(style);
	// styles (0,6,BOLD)
	text.replaceTextRange(0,0,"a"); // "aNewa\r\n"
	// styles (1,6,BOLD)
	style = text.getStyleRangeAtOffset(1);
	style.start = 0;
	style.length = 1;
	text.setStyleRange(style);
	// styles (0,7,BOLD)
	text.replaceTextRange(0,1,""); // "Newa\r\n"
	// styles (0,6,BOLD)
	for (int i=0; i<6; i++) {
		style = text.getStyleRangeAtOffset(i);
		assertTrue(":5:", style.fontStyle == SWT.BOLD);
	}


	text.setText("New L 1\r\nNew L 2\r\n");
	style = getStyle(0,9,null,null);
	style.fontStyle = SWT.BOLD;
	text.setStyleRange(style);
	// styles (0,9,BOLD)
	text.replaceTextRange(7,0,"a");
	// styles (0,7,BOLD), (8,2,BOLD)
	style = text.getStyleRangeAtOffset(8);
	if (style != null) {
		style.start = 7;
		style.length = 1;
		text.setStyleRange(style);
	}
	// styles (0,10,BOLD)
	text.replaceTextRange(4,0,"a");
	// styles (0,4,BOLD), (5,6,BOLD)
	style = text.getStyleRangeAtOffset(5);
	if (style != null) {
		style.start = 4;
		style.length = 1;
		text.setStyleRange(style);
	}
	// styles (0,11,BOLD)
	text.replaceTextRange(2,3,"");
	// styles (0,8,BOLD)
	for (int i=0; i<8; i++) {
		style = text.getStyleRangeAtOffset(i);
		assertTrue(":5a:", style.fontStyle == SWT.BOLD);
	}


	// reset the environment
	text.dispose();
	text = new StyledText(shell, SWT.NULL);
	setWidget(text);


	//					"01234567890123"
	textString = 		"1234 1234 1234";


	text.setText("1234 1234 1234");
	text.setStyleRange(getStyle(5,4,YELLOW,RED));
	text.setStyleRange(getStyle(5,2,YELLOW,RED));
	styles = text.getStyleRanges();
	assertTrue(":1a:", styles.length == 1);
	assertTrue(":1a:", styles[0].equals(getStyle(5,4,YELLOW,RED)));
	text.setText("1234 1234 1234");
	text.setStyleRange(getStyle(5,4,YELLOW,RED));
	text.setStyleRange(getStyle(7,2,YELLOW,RED));
	styles = text.getStyleRanges();
	assertTrue(":2a:", styles.length == 1);
	assertTrue(":2a:", styles[0].equals(getStyle(5,4,YELLOW,RED)));
	text.setText("1234 1234 1234");
	text.setStyleRange(getStyle(5,4,YELLOW,RED));
	text.setStyleRange(getStyle(6,2,YELLOW,RED));
	styles = text.getStyleRanges();
	assertTrue(":3a:", styles.length == 1);
	assertTrue(":3a:", styles[0].equals(getStyle(5,4,YELLOW,RED)));
	text.setText("1234 1234 1234");
	text.setStyleRange(getStyle(5,4,YELLOW,RED));
	text.setStyleRange(getStyle(3,4,YELLOW,RED));
	styles = text.getStyleRanges();
	assertTrue(":4a:", styles.length == 1);
	assertTrue(":4a:", styles[0].equals(getStyle(3,6,YELLOW,RED)));
	text.setText("1234 1234 1234");
	text.setStyleRange(getStyle(5,4,YELLOW,RED));
	text.setStyleRange(getStyle(7,4,YELLOW,RED));
	styles = text.getStyleRanges();
	assertTrue(":5a:", styles.length == 1);
	assertTrue(":5a:", styles[0].equals(getStyle(5,6,YELLOW,RED)));
	text.setText("1234 1234 1234");
	text.setStyleRange(getStyle(5,4,YELLOW,RED));
	text.setStyleRange(getStyle(5,4,YELLOW,RED));
	styles = text.getStyleRanges();
	assertTrue(":6a:", styles.length == 1);
	assertTrue(":6a:", styles[0].equals(getStyle(5,4,YELLOW,RED)));
	text.setText("1234 1234 1234");
	text.setStyleRange(getStyle(5,4,YELLOW,RED));
	text.setStyleRange(getStyle(3,10,YELLOW,RED));
	styles = text.getStyleRanges();
	assertTrue(":7a:", styles.length == 1);
	assertTrue(":7a:", styles[0].equals(getStyle(3,10,YELLOW,RED)));


	text.setText("1234 1234 1234");
	text.setStyleRange(getStyle(5,4,YELLOW,RED));
	text.setStyleRange(getStyle(5,2,BLUE,CYAN));
	styles = text.getStyleRanges();
	assertTrue(":1b:", styles.length == 2);
	assertTrue(":1b:", styles[0].equals(getStyle(5,2,BLUE,CYAN)));
	assertTrue(":1b:", styles[1].equals(getStyle(7,2,YELLOW,RED)));
	text.setText("1234 1234 1234");
	text.setStyleRange(getStyle(5,4,YELLOW,RED));
	text.setStyleRange(getStyle(7,2,BLUE,CYAN));
	styles = text.getStyleRanges();
	assertTrue(":2b:", styles.length == 2);
	assertTrue(":2b:", styles[0].equals(getStyle(5,2,YELLOW,RED)));
	assertTrue(":2b:", styles[1].equals(getStyle(7,2,BLUE,CYAN)));
	text.setText("1234 1234 1234");
	text.setStyleRange(getStyle(5,4,YELLOW,RED));
	text.setStyleRange(getStyle(6,2,BLUE,CYAN));
	styles = text.getStyleRanges();
	assertTrue(":3b:", styles.length == 3);
	assertTrue(":3b:", styles[0].equals(getStyle(5,1,YELLOW,RED)));
	assertTrue(":3b:", styles[1].equals(getStyle(6,2,BLUE,CYAN)));
	assertTrue(":3b:", styles[2].equals(getStyle(8,1,YELLOW,RED)));
	text.setText("1234 1234 1234");
	text.setStyleRange(getStyle(5,4,YELLOW,RED));
	text.setStyleRange(getStyle(3,4,BLUE,CYAN));
	styles = text.getStyleRanges();
	assertTrue(":4b:", styles.length == 2);
	assertTrue(":4b:", styles[0].equals(getStyle(3,4,BLUE,CYAN)));
	assertTrue(":4b:", styles[1].equals(getStyle(7,2,YELLOW,RED)));
	text.setText("1234 1234 1234");
	text.setStyleRange(getStyle(5,4,YELLOW,RED));
	text.setStyleRange(getStyle(7,4,BLUE,CYAN));
	styles = text.getStyleRanges();
	assertTrue(":5b:", styles.length == 2);
	assertTrue(":5b:", styles[0].equals(getStyle(5,2,YELLOW,RED)));
	assertTrue(":5b:", styles[1].equals(getStyle(7,4,BLUE,CYAN)));
	text.setText("1234 1234 1234");
	text.setStyleRange(getStyle(5,4,YELLOW,RED));
	text.setStyleRange(getStyle(5,4,BLUE,CYAN));
	styles = text.getStyleRanges();
	assertTrue(":6b:", styles.length == 1);
	assertTrue(":6b:", styles[0].equals(getStyle(5,4,BLUE,CYAN)));
	text.setText("1234 1234 1234");
	text.setStyleRange(getStyle(5,4,YELLOW,RED));
	text.setStyleRange(getStyle(3,10,BLUE,CYAN));
	styles = text.getStyleRanges();
	assertTrue(":7b:", styles.length == 1);
	assertTrue(":7b:", styles[0].equals(getStyle(3,10,BLUE,CYAN)));


	// reset the environment
	text.dispose();
	text = new StyledText(shell, SWT.NULL);
	setWidget(text);

	//			 		"012345678901234567890123"
	String testString=	"1234 1234 1234 1234 1234";

	text.setText(testString);
	text.setStyleRange(getStyle(10,4,YELLOW,RED));
	text.replaceTextRange(12,2,"");
	styles = text.getStyleRanges();
	assertTrue(":1:", styles.length == 1);
	assertTrue(":1:", styles[0].equals(getStyle(10,2,YELLOW,RED)));
	text.setText(testString);
	text.setStyleRange(getStyle(10,4,YELLOW,RED));
	text.replaceTextRange(10,2,"");
	styles = text.getStyleRanges();
	assertTrue(":2:", styles.length == 1);
	assertTrue(":2:", styles[0].equals(getStyle(10,2,YELLOW,RED)));


	text.setText(testString);
	text.setStyleRange(getStyle(10,4,YELLOW,RED));
	text.replaceTextRange(12,4,"");
	styles = text.getStyleRanges();
	assertTrue(":3:", styles.length == 1);
	assertTrue(":3:", styles[0].equals(getStyle(10,2,YELLOW,RED)));
	text.setText(testString);
	text.setStyleRange(getStyle(10,4,YELLOW,RED));
	text.replaceTextRange(8,4,"");
	styles = text.getStyleRanges();
	assertTrue(":4:", styles.length == 1);
	assertTrue(":4:", styles[0].equals(getStyle(8,2,YELLOW,RED)));


	text.setText(testString);
	text.setStyleRange(getStyle(10,4,YELLOW,RED));
	text.replaceTextRange(8,6,"");
	styles = text.getStyleRanges();
	assertTrue(":5:", styles.length == 0);
	text.setText(testString);
	text.setStyleRange(getStyle(10,4,YELLOW,RED));
	text.replaceTextRange(10,6,"");
	styles = text.getStyleRanges();
	assertTrue(":6:", styles.length == 0);
	text.setText(testString);
	text.setStyleRange(getStyle(10,4,YELLOW,RED));
	text.replaceTextRange(8,12,"");
	styles = text.getStyleRanges();
	assertTrue(":7:", styles.length == 0);

	//			 			"012345678901234567890123"
	//	String testString=	"1234 1234 1234 1234 1234";

	text.setText(testString);
	text.setStyleRange(getStyle(5,4,BLUE,CYAN));
	text.setStyleRange(getStyle(10,4,YELLOW,RED));
	text.replaceTextRange(5,7,"");
	styles = text.getStyleRanges();
	assertTrue(":8:", styles.length == 1);
	assertTrue(":8:", styles[0].equals(getStyle(5,2,YELLOW,RED)));
	text.setText(testString);
	text.setStyleRange(getStyle(5,4,BLUE,CYAN));
	text.setStyleRange(getStyle(10,4,YELLOW,RED));
	text.replaceTextRange(7,7,"");
	styles = text.getStyleRanges();
	assertTrue(":9:", styles.length == 1);
	assertTrue(":9:", styles[0].equals(getStyle(5,2,BLUE,CYAN)));


	text.setText(testString);
	text.setStyleRange(getStyle(5,4,BLUE,CYAN));
	text.setStyleRange(getStyle(10,4,YELLOW,RED));
	text.replaceTextRange(2,10,"");
	styles = text.getStyleRanges();
	assertTrue(":10:", styles.length == 1);
	assertTrue(":10:", styles[0].equals(getStyle(2,2,YELLOW,RED)));
	text.setText(testString);
	text.setStyleRange(getStyle(5,4,BLUE,CYAN));
	text.setStyleRange(getStyle(10,4,YELLOW,RED));
	text.replaceTextRange(7,9,"");
	styles = text.getStyleRanges();
	assertTrue(":11:", styles.length == 1);
	assertTrue(":11:", styles[0].equals(getStyle(5,2,BLUE,CYAN)));


	text.setText(testString);
	text.setStyleRange(getStyle(5,4,BLUE,CYAN));
	text.setStyleRange(getStyle(10,4,YELLOW,RED));
	text.replaceTextRange(5,9,"");
	styles = text.getStyleRanges();
	assertTrue(":12:", styles.length == 0);
	text.setText(testString);
	text.setStyleRange(getStyle(5,4,BLUE,CYAN));
	text.setStyleRange(getStyle(10,4,YELLOW,RED));
	text.replaceTextRange(3,13,"");
	styles = text.getStyleRanges();
	assertTrue(":11:", styles.length == 0);


	//			 			"012345678901234567890123"
	//	String testString=	"1234 1234 1234 1234 1234";

	text.setText(testString);
	text.setStyleRange(getStyle(5,4,BLUE,CYAN));
	text.setStyleRange(getStyle(10,4,YELLOW,RED));
	text.setStyleRange(getStyle(15,4,GREEN,PURPLE));
	text.replaceTextRange(7,12,"");
	styles = text.getStyleRanges();
	assertTrue(":14:", styles.length == 1);
	assertTrue(":14:", styles[0].equals(getStyle(5,2,BLUE,CYAN)));
	text.setText(testString);
	text.setStyleRange(getStyle(5,4,BLUE,CYAN));
	text.setStyleRange(getStyle(10,4,YELLOW,RED));
	text.setStyleRange(getStyle(15,4,GREEN,PURPLE));
	text.replaceTextRange(5,12,"");
	styles = text.getStyleRanges();
	assertTrue(":15:", styles.length == 1);
	assertTrue(":15:", styles[0].equals(getStyle(5,2,GREEN,PURPLE)));


	text.setText(testString);
	text.setStyleRange(getStyle(5,4,BLUE,CYAN));
	text.setStyleRange(getStyle(10,4,YELLOW,RED));
	text.setStyleRange(getStyle(15,4,GREEN,PURPLE));
	text.replaceTextRange(9,10,"");
	styles = text.getStyleRanges();
	assertTrue(":16:", styles.length == 1);
	assertTrue(":16:", styles[0].equals(getStyle(5,4,BLUE,CYAN)));


	// reset the environment
	text.dispose();
	text = new StyledText(shell, SWT.NULL);
	setWidget(text);

	StyleRange style1 = getStyle(0,0,null,null);


	StyleRange style2 = getStyle(0,0,RED,YELLOW);


	assertTrue(":1:", !style1.equals(style2));
	assertTrue(":1:", !style1.similarTo(style2));


	assertTrue(":1:", !style2.equals(style1));


	assertTrue(":1:", !style2.similarTo(style1));



	style1 = getStyle(0,10,RED,YELLOW);
	style2 = getStyle(11,5,RED,YELLOW);


	assertTrue(":2:", !style1.equals(style2));


	assertTrue(":2:", !style2.equals(style1));
	assertTrue(":2:", style1.similarTo(style2));


	assertTrue(":2:", style2.similarTo(style1));


}

@Test
public void test_setStyleRanges$Lorg_eclipse_swt_custom_StyleRange() {
	boolean exceptionThrown = false;
	StyleRange[] ranges = new StyleRange[] {
		new StyleRange(0, 1, getColor(RED), null),
		new StyleRange(2, 1, getColor(RED), null)};

	text.setText("Line0\r\n");
	try {
		text.setStyleRanges(null);
	}
	catch (IllegalArgumentException e) {
		exceptionThrown = true;
	}
	assertTrue(exceptionThrown);

	text.setStyleRanges(ranges);
	StyleRange[] currentRanges = text.getStyleRanges();
	assertEquals(ranges.length, currentRanges.length);
	for (int i = 0; i < currentRanges.length; i++) {
		assertEquals(ranges[i], currentRanges[i]);
	}
	text.setStyleRanges(new StyleRange[] {});
	assertEquals(0, text.getStyleRanges().length);
}

@Test
public void test_setTabsI(){
	text.setTabs(1);
	assertTrue(":a:", text.getTabs() == 1);

	text.setTabs(8);
	assertTrue(":b:", text.getTabs() == 8);
	text.setText("Line\t1\r\n");
	text.setTabs(7);
	assertTrue(":c:", text.getTabs() == 7);
}

@Test
public void test_setTextLjava_lang_String(){
	boolean exceptionThrown = false;

	text.setText("");
	assertEquals("", text.getText());
	text.setText("01234567890");
	assertEquals("01234567890", text.getText());

	try {
		text.setText(null);
	}
	catch (IllegalArgumentException e) {
		exceptionThrown = true;
	}
	assertTrue(exceptionThrown);

	assertEquals("01234567890", text.getText());
	text.setText("");
	assertEquals("", text.getText());
}

@Test
public void test_setTextLimitI(){
	boolean exceptionThrown = false;

	text.setTextLimit(10);
	assertTrue(":a:", text.getTextLimit() == 10);

	text.setTextLimit(-1);
	assertTrue(":b:", text.getTextLimit() == -1);

	try {
		text.setTextLimit(0);
	}
	catch (IllegalArgumentException e) {
		exceptionThrown = true;
	}
	assertTrue(":c:", exceptionThrown == true);
	exceptionThrown = false;
}

@Test
public void test_setTopIndexI(){
	text.setTopIndex(-1);
	assertTrue(":a:", text.getTopIndex() == 0);
	text.setTopIndex(1);
	assertTrue(":b:", text.getTopIndex() == 0);

	text.setText("Line0\r\nLine0a\r\n");

	text.setTopIndex(-2);
	assertTrue(":c:", text.getTopIndex() == 0);
	text.setTopIndex(-1);
	assertTrue(":d:", text.getTopIndex() == 0);
	text.setTopIndex(1);
	assertTrue(":e:", text.getTopIndex() == 1);
	text.setTopIndex(2);
	assertTrue(":f:", text.getTopIndex() == 2);
	text.setTopIndex(0);
	assertTrue(":g:", text.getTopIndex() == 0);
	text.setTopIndex(3);
	assertTrue(":h:", text.getTopIndex() == 2);

	text.setText("");
	text.setTopIndex(2);
	assertTrue(":i:", text.getTopIndex() == 0);
}

@Test
public void test_setTopPixelI(){
	int lineHeight = text.getLineHeight();

	text.setTopPixel(-1);
	assertTrue(":a:", text.getTopPixel() == 0);
	text.setTopPixel(1);
	assertTrue(":b:", text.getTopPixel() == 0);

	text.setText("Line0\r\n");

	text.setTopPixel(-2);
	assertTrue(":c:", text.getTopPixel() == 0);
	text.setTopPixel(-1);
	assertTrue(":d:", text.getTopPixel() == 0);
	text.setTopPixel(1);
	assertTrue(":e:", text.getTopPixel() == 1);
	text.setTopPixel(2 * lineHeight);
	assertTrue(":f:", text.getTopPixel() == 2 * lineHeight);
	text.setTopPixel(0);
	assertTrue(":g:", text.getTopPixel() == 0);
	text.setTopPixel(3 * lineHeight);
	assertTrue(":h:", text.getTopPixel() == 2 * lineHeight);

	text.setText("");
	text.setTopPixel(2 * lineHeight);
	assertTrue(":i:", text.getTopPixel() == 0);
}

@Test
public void test_verticalIndent_changeRelativeBounds() {
	String _5000lines = IntStream.range(1, 5001).mapToObj(n -> Integer.toString(n)).collect(Collectors.joining("\n"));
	text.setText(_5000lines);
	text.setSize(500, 200);
	text.invokeAction(ST.TEXT_END);
	text.setLineVerticalIndent(text.getContent().getLineCount() - 1, 10);
	text.invokeAction(ST.TEXT_START);
	assertEquals(0, text.getTopPixel());
}

@Test
public void test_setWordWrapZ(){
	String testString = "Line1\nLine2";

	text.setWordWrap(true);
	assertTrue(":a:", text.getWordWrap());
	text.setWordWrap(false);
	assertFalse(":b:", text.getWordWrap());
	text.setWordWrap(false);
	assertFalse(":c:", text.getWordWrap());
	text.setWordWrap(true);
	assertTrue(":d:", text.getWordWrap());

	text.setText(testString);
	assertEquals(":e:", testString, text.getText());
	assertEquals(":f:", 2, text.getLineCount());
}

@Test
public void test_showSelection() {
	text.showSelection();
	text.setSelectionRange(0, 0);
	text.showSelection();
	text.setText("Line0\r\n");
	text.showSelection();
	text.setSelectionRange(5, 2);
	text.showSelection();
}

@Test
public void test_isTextSelected() {
	// Empty selection
	text.setText("Sample Text Selection");
	assertFalse(text.isTextSelected());

	// Set selection
	text.setSelection(0, text.getCharCount());
	assertTrue(text.isTextSelected());

	// Set block selection
	StringBuilder buffer = new StringBuilder();
	for (int i = 0; i < 500; i++) {
		buffer.append("Sample Test Selection" + System.lineSeparator());
	}
	text.setText(buffer.toString());
	text.setSize(100, 10000);
	text.setBlockSelection(true);
	text.setBlockSelectionBounds(0, 0, 100, 10000);
	assertTrue(text.isTextSelected());

	// Set block selection on new line
	text.setText(System.lineSeparator());
	text.setSize(100, 100);
	text.setBlockSelection(true);
	text.setBlockSelectionBounds(0, 0, 100, 100);
	assertTrue(text.isTextSelected());
}

@Test
public void test_clickUpdatesCaretPosition() {
	text.setText("1\n2\n3\n");

	Event event = new Event();
	event.button = 1;
	event.count = 1;
	event.x = 0;
	event.y = text.getLinePixel(1);
	text.notifyListeners(SWT.MouseDown, event);

	assertEquals(2, text.getCaretOffset());
}

@Test
public void test_caretSizeAndPositionVariableGlyphMetrics() {
	text.setText("abcd");
	text.setMargins(2, 0, 0, 0); // keep leftMargin as it affects behavior
	text.setLineSpacing(0);
	StyleRange range = new StyleRange(2, 1, null, null);
	range.metrics = new GlyphMetrics(100, 0, 10);
	text.setStyleRange(range);
	text.setCaretOffset(0);
	assertEquals(text.getLineHeight(), text.getCaret().getSize().y);
	// +5: caret takes 5 more pixels
	assertEquals(text.getLineHeight(0) - text.getCaret().getSize().y, text.getCaret().getBounds().y);
	text.setCaretOffset(1);
	assertEquals(text.getLineHeight(), text.getCaret().getSize().y);
	assertEquals(text.getLineHeight(0) - text.getCaret().getSize().y, text.getCaret().getBounds().y);
	text.setCaretOffset(2);
	assertEquals(text.getLineHeight(0), text.getCaret().getSize().y);
	assertEquals(0, text.getCaret().getBounds().y);
	text.setCaretOffset(3);
	assertEquals(text.getLineHeight(), text.getCaret().getSize().y);
	assertEquals(text.getLineHeight(0) - text.getCaret().getSize().y, text.getCaret().getBounds().y);
	text.setCaretOffset(4);
	assertEquals(text.getLineHeight(), text.getCaret().getSize().y);
	assertEquals(text.getLineHeight(0) - text.getCaret().getSize().y, text.getCaret().getBounds().y);
	text.setCaretOffset(3);
	assertEquals(text.getLineHeight(), text.getCaret().getSize().y);
	assertEquals(text.getLineHeight(0) - text.getCaret().getSize().y, text.getCaret().getBounds().y);
	text.setCaretOffset(2);
	assertEquals(text.getLineHeight(0), text.getCaret().getSize().y);
	assertEquals(0, text.getCaret().getBounds().y);
	text.setCaretOffset(1);
	assertEquals(text.getLineHeight(), text.getCaret().getSize().y);
	assertEquals(text.getLineHeight(0) - text.getCaret().getSize().y, text.getCaret().getBounds().y);
	text.setCaretOffset(0);
	assertEquals(text.getLineHeight(), text.getCaret().getSize().y);
	assertEquals(text.getLineHeight(0) - text.getCaret().getSize().y, text.getCaret().getBounds().y);
}

@Test
public void test_doubleClickSelectsWord() {
	text.setText("Test1 Test2");

	Event event = new Event();
	event.button = 1;
	event.count = 2;
	event.x = 0;
	event.y = 0;
	text.notifyListeners(SWT.MouseDown, event);

	assertEquals("Test1", text.getSelectionText());
}

@Test
public void test_doubleClickWithRightMouseButtonDoesNotSelectWord() {
	text.setText("Test1 Test2");

	Event event = new Event();
	event.button = 3;
	event.count = 2;
	event.x = 0;
	event.y = 0;
	text.notifyListeners(SWT.MouseDown, event);

	assertEquals("", text.getSelectionText());
}

@Test
public void test_doubleClickDoesNotSelectIfDoubleClickIsDisabled() {
	text.setText("Test1 Test2");
	text.setDoubleClickEnabled(false);

	Event event = new Event();
	event.button = 1;
	event.count = 2;
	event.x = 0;
	event.y = 0;
	text.notifyListeners(SWT.MouseDown, event);

	assertEquals("", text.getSelectionText());
}

@Test
public void test_trippleClickSelectsSentance() {
	text.setText("Test1 Test2\nTest3 Test4");

	Event event = new Event();
	event.button = 1;
	event.count = 3;
	event.x = 0;
	event.y = 0;
	text.notifyListeners(SWT.MouseDown, event);

	assertEquals("Test1 Test2\n", text.getSelectionText());
}

@Test
public void test_trippleClickOnLastLineSelectsSentance() {
	text.setText("Test1 Test2\nTest3 Test4");

	Event event = new Event();
	event.button = 1;
	event.count = 3;
	event.x = 0;
	event.y = text.getLinePixel(1);
	text.notifyListeners(SWT.MouseDown, event);

	assertEquals("Test3 Test4", text.getSelectionText());
}

@Test
public void test_trippleClickDoesNotSelectIfDoubleClickIsDisabled() {
	text.setText("Test1 Test2\nTest3 Test4");
	text.setDoubleClickEnabled(false);

	Event event = new Event();
	event.button = 1;
	event.count = 3;
	event.x = 0;
	event.y = 0;
	text.notifyListeners(SWT.MouseDown, event);

	assertEquals("", text.getSelectionText());
}

@Test
public void test_isTextSelectedInBlockSelection() {
	text.setText(blockSelectionTestText());
	text.setSize(1000, 1000);
	text.setBlockSelection(true);
	Point lowerRight = text.getLocationAtOffset(blockSelectionTestTextOneLine().length() * 2 + 6);
	text.setBlockSelectionBounds(0, 0, lowerRight.x, lowerRight.y + 1);
	assertTrue(text.isTextSelected());
	assertEquals("Sample" + System.lineSeparator() + "Sample" + System.lineSeparator()
			+ "Sample", text.getSelectionText());
}

@Test
public void test_isTextSelectedInBlockSelectionForLingleEmptyLine() {
	text.setText(System.lineSeparator());
	text.setSize(100, 100);
	text.setBlockSelection(true);
	text.setBlockSelectionBounds(0, 0, 100, 100);
	assertTrue(text.isTextSelected());
}

@Test
public void test_selectionIsClearedOnCaretMoveWhenInBlockSelection() {
	text.setText("Test" + System.lineSeparator());
	text.setSize(100, 100);
	text.setBlockSelection(true);
	text.setBlockSelectionBounds(0, 0, 100, 100);
	assertTrue(text.isTextSelected());

	text.setCaretOffset(1);
	assertFalse(text.isTextSelected());
}

@Test
public void test_setBlockSelectionBounds_updatesNormalSelectionIfNotInBlockMode() {
	text.setText(blockSelectionTestText());
	text.setSize(1000, 1000);
	text.setBlockSelection(false);
	Point lowerRight = text.getLocationAtOffset(blockSelectionTestTextOneLine().length() * 2 + 6);
	text.setBlockSelectionBounds(0, 0, lowerRight.x, lowerRight.y + 1);
	assertTrue(text.isTextSelected());
	assertEquals(blockSelectionTestTextOneLine() + blockSelectionTestTextOneLine() + "Sample", text.getSelectionText());
}

@Test
public void test_selectionIsMaintainedOnDisableOfBlockSelection() {
	text.setText("Test" + System.lineSeparator());
	text.setSize(100, 100);
	text.setBlockSelection(true);
	text.setBlockSelectionBounds(0, 0, 100, 100);
	assertTrue(text.isTextSelected());

	text.setBlockSelection(false);
	assertTrue(text.isTextSelected());
}

@Test
public void test_selectAllInBlockSelectionMode() {
	text.setText("Test" + System.lineSeparator());
	text.setSize(100, 100);
	text.setBlockSelection(true);
	text.selectAll();
	assertTrue(text.isTextSelected());
	assertEquals("Test" + System.lineSeparator(), text.getSelectionText());
}

@Test
public void test_cutTextInBlockSelection() {
	text.setText(blockSelectionTestText());
	text.setSize(1000, 1000);
	text.setBlockSelection(true);
	Point lowerRight = text.getLocationAtOffset(blockSelectionTestTextOneLine().length() * 2 + 6);
	text.setBlockSelectionBounds(0, 0, lowerRight.x, lowerRight.y + 1);
	text.cut();

	assertTrue(text.getText(),
			text.getText()
					.startsWith(" Test Selection" + System.lineSeparator()
							+ " Test Selection" + System.lineSeparator()
							+ " Test Selection" + System.lineSeparator()
							+ "Sample Test Selection" + System.lineSeparator()));
}

@Test
public void test_pasteInsertsTextInBlockSelectionAsBlock() {
	text.setText(blockSelectionTestText());
	text.setSize(1000, 1000);
	text.setBlockSelection(true);
	Point lowerRight = text.getLocationAtOffset(blockSelectionTestTextOneLine().length() * 2 + 6);
	text.setBlockSelectionBounds(0, 0, lowerRight.x, lowerRight.y + 1);
	text.copy();

	text.setCaretOffset(0);
	text.paste();

	assertTrue(text.getText(),
			text.getText()
					.startsWith("Sample" + blockSelectionTestTextOneLine()
							+ "Sample" + blockSelectionTestTextOneLine()
							+ "Sample" + blockSelectionTestTextOneLine()
							+ blockSelectionTestTextOneLine()));
}

@Test
public void test_cutAndPasteInBlockSelection() {
	text.setText(blockSelectionTestText());
	text.setSize(1000, 1000);
	text.setBlockSelection(true);
	Point lowerRight = text.getLocationAtOffset(blockSelectionTestTextOneLine().length() * 2 + 6);
	text.setBlockSelectionBounds(0, 0, lowerRight.x, lowerRight.y + 1);
	text.cut();
	text.paste();

	assertTrue(text.getText(),
			text.getText()
					.startsWith(blockSelectionTestTextOneLine()
							+ blockSelectionTestTextOneLine()
							+ blockSelectionTestTextOneLine()
							+ blockSelectionTestTextOneLine()));
}

@Test
public void test_trippleClickInBlockSelectionSelectsLine() {
	text.setText("  Sample Test Selection  " + System.lineSeparator() + "  Sample Test Selection  "
			+ System.lineSeparator());
	text.setSize(1000, 1000);
	text.setBlockSelection(true);
	Point lowerRight = text.getLocationAtOffset(3);

	Event event = new Event();
	event.button = 1;
	event.count = 3;
	event.x = lowerRight.x;
	event.y = lowerRight.y;
	text.notifyListeners(SWT.MouseDown, event);

	assertEquals("  Sample Test Selection  ", text.getSelectionText());
}

@Test
public void test_getSelectionRangesInBlockSelection() {
	text.setText(blockSelectionTestText());
	text.setSize(1000, 1000);
	text.setBlockSelection(true);
	Point lowerRight = text.getLocationAtOffset(blockSelectionTestTextOneLine().length() * 2 + 6);
	text.setBlockSelectionBounds(0, 0, lowerRight.x, lowerRight.y + 1);
	assertArrayEquals(new int[] { 0, 6, 21 + System.lineSeparator().length(), 6,
			42 + System.lineSeparator().length() * 2, 6 }, text.getSelectionRanges());
}

@Test
public void test_getSelectionCountInBlockSelection() {
	text.setText(blockSelectionTestText());
	text.setSize(1000, 1000);
	text.setBlockSelection(true);
	Point lowerRight = text
			.getLocationAtOffset(blockSelectionTestTextOneLine().length() * 2 + 6);
	text.setBlockSelectionBounds(0, 0, lowerRight.x, lowerRight.y + 1);
	assertEquals(18 + System.lineSeparator().length() * 2, text.getSelectionCount());
}

@Test
public void test_getBlockSelectionlundsInBlockSelection() {
	text.setText(blockSelectionTestText());
	text.setSize(1000, 1000);
	text.setBlockSelection(true);
	Point lowerRight = text
			.getLocationAtOffset(blockSelectionTestTextOneLine().length() * 2 + 6);
	text.setBlockSelectionBounds(0, 0, lowerRight.x, lowerRight.y);
	assertEquals(new Rectangle(0, 0, lowerRight.x, lowerRight.y), text.getBlockSelectionBounds());
}

@Test
public void test_insertInBlockSelection() {
	text.setText(blockSelectionTestText());
	text.setSize(1000, 1000);
	text.setBlockSelection(true);

	text.setSelection(6, 6 + blockSelectionTestTextOneLine().length());
	text.insert("Foo" + System.lineSeparator() + "Foo" + System.lineSeparator());

	assertTrue(text.getText(), text.getText()
			.startsWith("SampleFoo Test Selection" + System.lineSeparator()
					+ "SampleFoo Test Selection" + System.lineSeparator() + "Sample Test Selection"
					+ System.lineSeparator()));
}

@Test
public void test_setStyleRanges_render() throws InterruptedException {
	Assume.assumeFalse("Bug 553090 causes test to fail on Mac", SwtTestUtil.isCocoa);
	shell.setVisible(true);
	text.setText("abc");
	text.setMargins(0, 0, 0, 0);
	text.pack();
	processEvents(1000, () -> hasPixel(text, text.getBackground()) && !hasPixel(text, text.getDisplay().getSystemColor(SWT.COLOR_RED)));
	assertTrue(hasPixel(text, text.getBackground()));
	assertFalse(hasPixel(text, text.getDisplay().getSystemColor(SWT.COLOR_RED)));
	text.setStyleRanges(new StyleRange[] {
			new StyleRange(0, 1, null, text.getDisplay().getSystemColor(SWT.COLOR_RED)),
			new StyleRange(2, 1, null, text.getDisplay().getSystemColor(SWT.COLOR_RED)),
	});
	processEvents(100, () ->
		hasPixel(text, text.getBackground()) &&
		hasPixel(text, text.getDisplay().getSystemColor(SWT.COLOR_RED)));
	assertTrue(hasPixel(text, text.getBackground()));
	assertTrue(hasPixel(text, text.getDisplay().getSystemColor(SWT.COLOR_RED)));
	text.replaceStyleRanges(0, 3, new StyleRange[] {
			new StyleRange(0, 3, null, text.getDisplay().getSystemColor(SWT.COLOR_RED)),
	});
	processEvents(1000, () -> !hasPixel(text, text.getBackground()) && hasPixel(text, text.getDisplay().getSystemColor(SWT.COLOR_RED)));
	assertFalse(hasPixel(text, text.getBackground()));
	assertTrue(hasPixel(text, text.getDisplay().getSystemColor(SWT.COLOR_RED)));
}

/**
 * Test LineStyleListener which provides styles but no ranges.
 */
@Test
public void test_lineStyleListener_styles_render() throws InterruptedException {
	Assume.assumeFalse("Bug 536588 prevents test to work on Mac", SwtTestUtil.isCocoa);
	final ArrayList<StyleRange> styles = new ArrayList<>();
	styles.add(getStyle(0, 2, null, GREEN));
	styles.add(getStyle(4, 1, null, GREEN));
	styles.add(getStyle(7, 7, null, GREEN));
	styles.add(getStyle(17, 13, null, GREEN));
	final LineStyleListener listener = (LineStyleEvent event) -> {
		event.styles = styles.toArray(new StyleRange[0]);
	};
	testLineStyleListener("0123456789\n123456789\n123456789", listener, () -> hasPixel(text, getColor(GREEN)));

	// Bug 549110: test styling from LineStyleListener which include tab character with metrics
	final StyleRange tabStyle = new StyleRange();
	tabStyle.start = 15;
	tabStyle.length = 1;
	tabStyle.metrics = new GlyphMetrics(0, 0, 100);
	styles.add(tabStyle);
	testLineStyleListener("0123456789\n1234\t6789\n123456789", listener, () -> hasPixel(text, getColor(GREEN)));
}

/**
 * Test LineStyleListener which provides styles and ranges.
 */
@Test
public void test_lineStyleListener_stylesAndRanges_render() throws InterruptedException {
	Assume.assumeFalse("Bug 536588 prevents test to work on Mac", SwtTestUtil.isCocoa);
	LineStyleListener listener = (LineStyleEvent event) -> {
		final StyleRange style = getStyle(0, 0, null, GREEN);
		event.styles =  new StyleRange[] { style, style, style, style };
		event.ranges = new int[] {0,2, 4,1, 7,7, 17,13};
	};
	testLineStyleListener("0123456789\n123456789\n123456789", listener, () -> hasPixel(text, getColor(GREEN)));

	// Bug 549110: test styling from LineStyleListener which include tab character with metrics
	listener = (LineStyleEvent event) -> {
		final StyleRange style = getStyle(0, 0, null, GREEN);
		final StyleRange tabStyle = new StyleRange();
		tabStyle.start = 15;
		tabStyle.length = 1;
		tabStyle.metrics = new GlyphMetrics(0, 0, 100);
		event.styles =  new StyleRange[] { style, style, style, tabStyle, style };
		event.ranges = new int[] {0,2, 4,1, 7,7, 15,1, 17,13};
	};
	testLineStyleListener("0123456789\n1234\t6789\n123456789", listener, () -> hasPixel(text, getColor(GREEN)));
}

/**
 * Test LineStyleListener which provides invalid styles with invalid start or length.
 */
@Test
public void test_lineStyleListener_invalidStyles_render() throws InterruptedException {
	Assume.assumeFalse("Bug 536588 prevents test to work on Mac", SwtTestUtil.isCocoa);
	LineStyleListener listener = (LineStyleEvent event) -> {
		event.styles = new StyleRange[] {
			getStyle(-10, 4, null, GREEN),
			getStyle(-5, 10, null, GREEN),
			getStyle(12, 10, null, GREEN),
			getStyle(40, 8, null, GREEN),
		};
	};
	testLineStyleListener("0123456789\n123456789", listener, () -> hasPixel(text, getColor(GREEN)));

	// Bug 549110: test styling from LineStyleListener which include tab character with metrics
	final StyleRange tabStyle = new StyleRange();
	listener = (LineStyleEvent event) -> {
		event.styles = new StyleRange[] { tabStyle };
	};
	// The following tests do not need a condition to wait and test since they should trigger/test
	// for exceptions and the getTextBounds fail due to bug 547532.
	tabStyle.start = 0; tabStyle.length = 1; tabStyle.metrics = new GlyphMetrics(0, 0, 25);
	testLineStyleListener("\t", listener, () -> true /*text.getTextBounds(0, 0).width == 25*/);
	tabStyle.start = -2; tabStyle.length = 3; tabStyle.metrics = new GlyphMetrics(0, 0, 40);
	testLineStyleListener("\t", listener, () -> true /*text.getTextBounds(0, 0).width == 40*/);
	tabStyle.start = 0; tabStyle.length = 3; tabStyle.metrics = new GlyphMetrics(0, 0, 70);
	testLineStyleListener("\t", listener, () -> true /*text.getTextBounds(0, 0).width == 70*/);
	tabStyle.start = -2; tabStyle.length = 10; tabStyle.metrics = new GlyphMetrics(0, 0, 100);
	testLineStyleListener("\t", listener, () -> true /*text.getTextBounds(0, 0).width == 100*/);
}

private void testLineStyleListener(String content, LineStyleListener listener, BooleanSupplier evaluation) throws InterruptedException {
	try {
		text.setText("");
		text.addLineStyleListener(listener);
		shell.setVisible(true);
		text.setText(content);
		text.setMargins(0, 0, 0, 0);
		text.pack();
		processEvents(1000, evaluation);
		assertTrue("Text not styled correctly.", evaluation.getAsBoolean());
	} finally {
		text.removeLineStyleListener(listener);
	}
}

@Test
public void test_variableToFixedLineHeight() throws InterruptedException {
	GridData layoutData = new GridData(SWT.FILL, SWT.FILL,true, true);
	text.setLayoutData(layoutData);
	shell.setVisible(true);
	text.setText("\nabc\n\nd");
	text.setMargins(0, 0, 0, 0);
	text.setSize(100, 100);
	processEvents(1000, () -> Boolean.FALSE);
	StyleRange range = new StyleRange();
	range.start = 1;
	range.length = 1;
	Color colorForVariableHeight = text.getDisplay().getSystemColor(SWT.COLOR_RED);
	range.background = colorForVariableHeight;
	range.metrics = new GlyphMetrics(40, 0, 10);
	text.setStyleRange(range);
	// move to variable height and paint with red to check later whether
	// redraw was done properly
	processEvents(100, null);
	range = new StyleRange();
	range.start = 1;
	range.length = 1;
	range.fontStyle = SWT.BOLD;
	range.metrics = null;
	text.replaceStyleRanges(0, text.getCharCount(), new StyleRange[] {range});
	// changing to fixed line height here should redraw widget until
	// the bottom and clear the area (no more red)
	processEvents(3000, () -> !hasPixel(text, colorForVariableHeight));
	assertFalse(hasPixel(text, colorForVariableHeight));
}

/**
 * Check if StyledText widget contains the given color.
 *
 * @param text widget to check
 * @param expectedColor color to find
 * @return <code>true</code> if the given color was found in current text widget
 *         bounds
 */
private boolean hasPixel(StyledText text, Color expectedColor) {
	return hasPixel(text, expectedColor, null);
}

/**
 * Check if StyledText widget contains the given color in given bounds. The
 * effective search range to find the color is the union of current widget
 * bounds and given rectangle.
 *
 * @param text widget to check
 * @param expectedColor color to find
 * @param rect          the bounds where the color is searched in. Can overlap
 *                      the text widget bounds or <code>null</code> to check the
 *                      widgets full bounds.
 * @return <code>true</code> if the given color was found in search range of
 *         text widget
 */
private boolean hasPixel(StyledText text, Color expectedColor, Rectangle rect) {
	GC gc = new GC(text);
	final Image image = new Image(text.getDisplay(), text.getSize().x, text.getSize().y);
	gc.copyArea(image, 0, 0);
	gc.dispose();
	ImageData imageData = image.getImageData();
	if (rect == null) {
		rect = new Rectangle(0, 0, image.getBounds().width, image.getBounds().height);
	}
	RGB expectedRGB = expectedColor.getRGB();
	int xEnd = rect.x + rect.width;
	int yEnd = rect.y + rect.height;
	for (int x = Math.max(rect.x, 1); x < xEnd && x < image.getBounds().width - 1; x++) { // ignore first and last columns
		for (int y = rect.y; y < yEnd && y < image.getBounds().height; y++) {
			RGB pixelRGB = imageData.palette.getRGB(imageData.getPixel(x, y));
			if (expectedRGB.equals(pixelRGB)) {
				image.dispose();
				return true;
			}
		}
	}
	image.dispose();
	return false;
}

private String blockSelectionTestText() {
	StringBuilder buffer = new StringBuilder();
	for (int i = 0; i < 20; i++) {
		buffer.append(blockSelectionTestTextOneLine());
	}
	return (buffer.toString());
}

private String blockSelectionTestTextOneLine() {
	return "Sample Test Selection" + System.lineSeparator();
}

/**
 * Regression test for bug 19985
 */
protected void rtfCopy() {
	String lines = "Line0\nLine1\nLine2\nLine3\nLine4\nLine5";
	final int[] linesCalled = new int[] {0};
	LineStyleListener listener = event -> {
		Display display = Display.getDefault();
		Color red = display.getSystemColor(SWT.COLOR_RED);
		StyledText styledText = (StyledText) event.widget;
		int lineIndex = styledText.getLineAtOffset(event.lineOffset);
		int lineStart = event.lineOffset;
		int lineEnd = lineStart + event.lineText.length();
		StyleRange goodRange = new StyleRange(0, 1, red, red);

		event.styles = new StyleRange[2];
		switch (lineIndex % 6) {
			case 0:
				event.styles[0] = goodRange;
				event.styles[1] = new StyleRange(lineEnd, 1, red, red);
				linesCalled[0]++;
				break;
			case 1:
				event.styles[0] = goodRange;
				event.styles[1] = new StyleRange(lineEnd, -1, red, red);
				linesCalled[0]++;
				break;
			case 2:
				event.styles[0] = goodRange;
				event.styles[1] = new StyleRange(lineEnd - 1, -1, red, red);
				linesCalled[0]++;
				break;
			case 3:
				event.styles[0] = goodRange;
				event.styles[1] = new StyleRange(lineStart, -1, red, red);
				linesCalled[0]++;
				break;
			case 4:
				event.styles[0] = new StyleRange(lineStart, 1, red, red);
				event.styles[1] = new StyleRange(lineStart, -1, red, red);
				linesCalled[0]++;
				break;
			case 5:
				event.styles[0] = new StyleRange(lineEnd / 2, 1, red, red);
				event.styles[1] = new StyleRange(lineEnd / 2, -1, red, red);
				linesCalled[0]++;
				break;
		}
	};
	text.setText(lines);
	// cause StyledText to call the listener.
	text.setSelection(0, text.getCharCount());
	text.addLineStyleListener(listener);
	linesCalled[0] = 0;
	text.copy();
	assertTrue("not all lines tested for RTF copy", linesCalled[0] == text.getLineCount());

	Clipboard clipboard = new Clipboard(text.getDisplay());
	RTFTransfer rtfTranfer = RTFTransfer.getInstance();
	String clipboardText = (String) clipboard.getContents(rtfTranfer);
	assertTrue("RTF copy failed", clipboardText.length() > 0);

	clipboard.dispose();
	text.removeLineStyleListener(listener);
}

@Test
public void test_consistency_Modify() {
	consistencyEvent('a', 0, 0, 0, ConsistencyUtility.KEY_PRESS);
}

@Override
@Test
public void test_consistency_MenuDetect () {
	consistencyEvent(10, 10, 3, ConsistencyUtility.ESCAPE_MENU, ConsistencyUtility.MOUSE_CLICK);
}

@Override
@Test
public void test_consistency_DragDetect () {
	consistencyEvent(30, 10, 50, 0, ConsistencyUtility.MOUSE_DRAG);
}

/**
 * This tests if a tab character styled with custom metrics affects other styled parts.
 * <p>
 * Such a problem was once caused with bug 547532 and discovered along bug 549110.
 * </p>
 */
@Test
public void test_GlyphMetricsOnTab_Bug549110() throws InterruptedException {
	Assume.assumeFalse("Bug 536588 prevents test to work on Mac", SwtTestUtil.isCocoa);
	shell.setVisible(true);
	text.setText("ab\tcde");
	text.setMargins(0, 0, 0, 0);
	text.pack();
	processEvents(1000,
			() -> hasPixel(text, text.getBackground())
					&& !hasPixel(text, getColor(RED))
					&& !hasPixel(text, getColor(BLUE)));
	assertTrue(hasPixel(text, text.getBackground()));
	assertFalse(hasPixel(text, getColor(RED)));
	assertFalse(hasPixel(text, getColor(BLUE)));

	final StyleRange styleR = getStyle(0, 1, null, RED);
	final StyleRange styleB = getStyle(4, 1, null, BLUE);
	final StyleRange tabStyle = new StyleRange();
	tabStyle.start = 2;
	tabStyle.length = 1;
	tabStyle.metrics = new GlyphMetrics(0, 0, 100);
	text.replaceStyleRanges(0, text.getText().length(), new StyleRange[] { styleR, tabStyle, styleB });
	text.pack();
	processEvents(1000, () -> hasPixel(text, getColor(RED)) && hasPixel(text, getColor(BLUE)));
	assertTrue("Wrong style before tab", hasPixel(text, getColor(RED)));
	assertTrue("Wrong style after tab", hasPixel(text, getColor(BLUE)));
}

@Test
public void test_GlyphMetricsOnTab() {
	text.setTabs(4);
	text.setText("\tabcdefghijkl");
	Rectangle boundsWithoutGlyphMetrics = text.getTextBounds(0, text.getText().length() - 1);
	int tabWidthWithoutGlyphMetrics = text.getTextBounds(0, 0).width;
	StyleRange range = new StyleRange(0, 1, null, null);
	range.metrics = new GlyphMetrics(0, 0, 100);
	text.setStyleRange(range);
	int tabWidthWithGlyphMetrics = text.getTextBounds(0, 0).width;
	assertEquals(range.metrics.width, tabWidthWithGlyphMetrics);
	Rectangle boundsWithGlyphMetrics = text.getTextBounds(0, text.getText().length() - 1);
	assertEquals("Style should change text bounds", boundsWithoutGlyphMetrics.width - tabWidthWithoutGlyphMetrics + tabWidthWithGlyphMetrics, boundsWithGlyphMetrics.width);
}

/**
 * Test for:
 * Bug 550017 - [StyledText] Disabling StyledText moves insertion position to 0
 */
@Test
public void test_InsertWhenDisabled() {
	String str = "some test text";
	text.setText(str);
	String selected = "test";
	int selectionStart = str.indexOf(selected);
	int selectionEnd = selectionStart + selected.length();
	text.setSelection(selectionStart, selectionEnd);
	assertEquals(selectionEnd, text.getCaretOffset());
	assertEquals(selected, text.getSelectionText());

	text.setEnabled(false);

	assertEquals(selected, text.getSelectionText());
	assertEquals(selectionEnd, text.getCaretOffset());

	text.insert("[inserted]");
	String actualText = text.getText();
	String expectedText = "some [inserted] text";
	assertEquals("Wrong insert in disabled StyledText", expectedText, actualText);
}

/**
 * Test for:
 * Bug 551335 - [StyledText] setStyleRanges reset less cache than necessary
 * Bug 551336 - [StyledText] resetting styles does not reset rendering
 */
@Test
public void test_bug551335_lostStyles() throws InterruptedException {
	Assume.assumeFalse("Bug 536588 prevents test to work on Mac", SwtTestUtil.isCocoa);
	shell.setVisible(true);
	text.setText("012345678\n012345678\n0123456789");
	text.setMargins(0, 0, 0, 0);
	text.pack();
	shell.pack();

	StyleRange style = new StyleRange();
	style.background = getColor(BLUE);
	style.length = 3;

	StyleRange[] styles = new StyleRange[3];
	style.start = 3;
	styles[0] = (StyleRange) style.clone();
	style.start = 13;
	styles[1] =  (StyleRange) style.clone();
	style.start = 23;
	styles[2] =  (StyleRange) style.clone();

	int lineHeight = text.getBounds().height / text.getLineCount();
	Rectangle[] testAreas = new Rectangle[text.getLineCount()];
	for (int i = 0; i < testAreas.length; i++) {
		// the test area is a small horizontal line approximate in the middle of the line
		// the concrete width doesn't matter because it is clipped to the actual line width
		testAreas[i] = new Rectangle(0, (int)((i + 0.48) * lineHeight), 1000, 3);
	}
	BooleanSupplier testAllLinesStyled = () -> {
		for (Rectangle testArea : testAreas) {
			if (!hasPixel(text, getColor(BLUE), testArea)) {
				return false;
			}
		}
		return true;
	};
	BooleanSupplier testUnstyledText = () -> {
		for (Rectangle testArea : testAreas) {
			if (hasPixel(text, getColor(BLUE), testArea)) {
				return false;
			}
		}
		return true;
	};

	// 1. apply style one by one
	for (StyleRange s : styles) {
		text.setStyleRange(s);
	}
	processEvents(1000, testAllLinesStyled);
	assertTrue(testAllLinesStyled.getAsBoolean());

	text.setStyleRange(null); // reset style
	processEvents(1000, testUnstyledText);
	assertTrue(testUnstyledText.getAsBoolean());

	// 2. apply styles as array
	text.setStyleRanges(styles);
	processEvents(1000, testAllLinesStyled);
	assertTrue(testAllLinesStyled.getAsBoolean());

	text.setStyleRange(null); // reset style
	processEvents(1000, testUnstyledText);
	assertTrue(testUnstyledText.getAsBoolean());

	// 3. apply styles using ranges
	int[] ranges = new int[styles.length * 2];
	style.start = style.length = 0;
	for (int i = 0; i < styles.length; i++) {
		ranges[i*2] =   styles[i].start;
		ranges[i*2 + 1] = styles[i].length;
		styles[i] = style;
	}
	text.setStyleRanges(ranges, styles);
	processEvents(1000, testAllLinesStyled);
	assertTrue(testAllLinesStyled.getAsBoolean());

	text.setStyleRange(null); // reset style
	processEvents(1000, testUnstyledText);
	assertTrue(testUnstyledText.getAsBoolean());
}

/**
 * Test for: Bug 418714 - [Win32] Content copied to clipboard lost after dispose
 *
 * More a {@link Clipboard} than a StlyedText test. Depending on platform data
 * transfered to clipboard is not actually copied until the data is requested
 * from clipboard. It should be still possible to paste text which was copied
 * from a now disposed/unavailable StyledText.
 */
@Test
public void test_clipboardCarryover() {
	assumeFalse("Disabled on Mac because similar clipboard tests are also disabled.", SwtTestUtil.isCocoa);

	String content = "StyledText-" + Math.abs(new Random().nextInt());
	text.setText(content);
	text.selectAll();
	text.copy();
	text.dispose();

	text = new StyledText(shell, SWT.NONE);
	setWidget(text);
	text.paste();
	assertEquals("Lost clipboard content", content, text.getText());
	assertEquals("Clipboard content got some unexpected styling", 0, text.getStyleRanges().length);

	Clipboard clipboard = new Clipboard(text.getDisplay());
	RTFTransfer rtfTranfer = RTFTransfer.getInstance();
	String clipboardText = (String) clipboard.getContents(rtfTranfer);
	assertTrue("RTF copy failed", clipboardText.length() > 0);
}

/**
 * Bug 563531 - [regression][StyledText] Scrolling with arrow down key does not update caret painting
 */
@Test
public void test_caretLocationOnArrowMove() {
	text.setText(
		  "............................................................\n"
		+ "............................................................\n"
		+ "............................................................\n"
		+ "............................................................\n"
		+ "............................................................\n"
		+ "............................................................\n"
		+ "............................................................\n"
		+ "............................................................\n"
		+ "............................................................\n"
		+ "............................................................\n"
		+ "............................................................\n"
		+ "............................................................");

	shell.open();
	text.setSize(10, 50);

	for (int i = 0; i < 5; i++) {
		text.invokeAction(ST.LINE_DOWN);
	}

	Point caretLocation = text.getCaret().getLocation();
	int caretOffset = text.getCaretOffset();
	text.setCaretOffset(0);
	text.setCaretOffset(caretOffset);
	assertEquals(text.getCaret().getLocation(), caretLocation);
}
}
