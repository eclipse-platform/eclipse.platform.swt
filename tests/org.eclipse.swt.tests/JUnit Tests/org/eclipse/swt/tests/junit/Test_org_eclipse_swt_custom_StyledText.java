/*******************************************************************************
 * Copyright (c) 2000, 2022 IBM Corporation and others.
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


import static org.eclipse.swt.tests.junit.SwtTestUtil.hasPixel;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.junit.Assume.assumeFalse;

import java.time.Instant;
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
import org.eclipse.swt.dnd.HTMLTransfer;
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
import org.eclipse.swt.graphics.GlyphMetrics;
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
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.test.Screenshots;
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
	Display display = Display.getDefault();
	colors.put(RED, new Color (display, RED));
	colors.put(BLUE, new Color (display, BLUE));
	colors.put(GREEN, new Color (display, GREEN));
	colors.put(YELLOW, new Color (display, YELLOW));
	colors.put(CYAN, new Color (display, CYAN));
	colors.put(PURPLE, new Color (display, PURPLE));
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
	ExtendedModifyListener listener = event -> {
		listenerCalled = true;
		assertEquals("ExtendedModify event data invalid", 0, event.start);
		assertEquals("ExtendedModify event data invalid", line.length(), event.length);
		assertEquals("ExtendedModify event data invalid", "", event.replacedText);
	};

	assertThrows(IllegalArgumentException.class, ()->
		text.addExtendedModifyListener(null));

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
	assertEquals(":a:", SWT.NULL, text.getKeyBinding(SWT.DEL));
	text.setKeyBinding(SWT.DEL, ST.LINE_UP);
	assertEquals(":b:", ST.LINE_UP, text.getKeyBinding(SWT.DEL));
	text.setKeyBinding(SWT.DEL | SWT.MOD2, ST.SELECT_PAGE_UP);
	assertEquals(":c:", ST.SELECT_PAGE_UP, text.getKeyBinding(SWT.DEL | SWT.MOD2));
	text.setKeyBinding(SWT.DEL | SWT.MOD2, ST.PAGE_UP);
	assertEquals(":d:", ST.PAGE_UP, text.getKeyBinding(SWT.DEL | SWT.MOD2));
	text.setKeyBinding(SWT.KEYPAD_CR, ST.LINE_END);
	assertEquals(":e:", ST.LINE_END, text.getKeyBinding(SWT.KEYPAD_CR));
	text.setKeyBinding(SWT.KEYPAD_CR | SWT.MOD1, ST.LINE_START);
	assertEquals(":f:",ST.LINE_START, text.getKeyBinding(SWT.KEYPAD_CR | SWT.MOD1));
	text.setKeyBinding(-1, ST.PAGE_UP);
	text.setKeyBinding(-1, -1);
}

@Test
public void test_addBidiSegmentListenerLorg_eclipse_swt_custom_BidiSegmentListener() {
	String line = "Line1";
	BidiSegmentListener listener = event -> listenerCalled = true;

	assertThrows( IllegalArgumentException.class, ()->
		text.addBidiSegmentListener(null));

	listenerCalled = false;
	text.setText(line);
	text.addBidiSegmentListener(listener);
	// cause StyledText to call the BidiSegmentListener.
	text.getLocationAtOffset(0);
	if (SwtTestUtil.isBidi()) {
		assertTrue("Listener not called", listenerCalled);
	} else {
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
	LineBackgroundListener listener = event -> listenerCalled = true;

	assertThrows(IllegalArgumentException.class, ()->
		text.addLineBackgroundListener(null));

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
	LineStyleListener listener = event -> listenerCalled = true;

	assertThrows(IllegalArgumentException.class, () -> text.addLineStyleListener(null));

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
	ModifyListener listener = event -> listenerCalled = true;

	assertThrows(IllegalArgumentException.class, () -> text.addModifyListener(null));

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

	assertThrows(IllegalArgumentException.class, () -> text.addSelectionListener(null));

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
	VerifyKeyListener listener = event -> {};

	assertThrows(IllegalArgumentException.class, () -> text.addVerifyKeyListener(null));

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
	VerifyListener listener = event -> {
		listenerCalled = true;
		assertEquals("Verify event data invalid", 0, event.start);
		assertEquals("Verify event data invalid", 0, event.end);
		assertEquals("Verify event data invalid", line, event.text);
		event.start = 2;
		event.end = 5;
		event.text = newLine;
	};

	assertThrows(IllegalArgumentException.class, () -> text.addVerifyListener(null));

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
	String line = "Line1";

	text.append(line);
	assertEquals("append to empty text", line, text.getText());

	assertThrows("append null string", IllegalArgumentException.class, () -> text.append(null));

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

private static StyleRange getRangeForText(String str, String subStr) {
	int index = str.indexOf(subStr);
	if (index != -1) {
		return new StyleRange(index, subStr.length(), null, null);
	}
	return null;
}

@Test
public void test_clipboardWithHtml() {
	String content = "This is red, background yellow, bold, italic, underscore, big, small, super, sub.";
	text.setText(content);

	StyleRange range;
	range = getRangeForText(content, "red");
	range.foreground = new Color(0xff, 0x00, 0x00);
	text.setStyleRange(range);

	range = getRangeForText(content, "yellow");
	range.background = new Color(0xff, 0xff, 0x00);
	text.setStyleRange(range);

	range = getRangeForText(content, "bold");
	range.fontStyle = SWT.BOLD;
	text.setStyleRange(range);

	range = getRangeForText(content, "italic");
	range.fontStyle = SWT.ITALIC;
	text.setStyleRange(range);

	range = getRangeForText(content, "underscore");
	range.underlineStyle = SWT.UNDERLINE_SINGLE;
	range.underline = true;
	text.setStyleRange(range);

	range = getRangeForText(content, "big");
	Font fontArial16 = new Font(Display.getCurrent(), "Arial", 16, 0);
	range.font = fontArial16;
	text.setStyleRange(range);

	range = getRangeForText(content, "small");
	Font fontArial8 = new Font(Display.getCurrent(), "Arial", 8, SWT.NONE);
	range.font = fontArial8;
	text.setStyleRange(range);

	range = getRangeForText(content, "super");
	range.rise = 12;
	text.setStyleRange(range);
	range = getRangeForText(content, "sub");
	range.rise = -12;
	text.setStyleRange(range);

	text.selectAll();
	text.copy();

	fontArial16.dispose();
	fontArial8.dispose();
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
	assertEquals(":a:", 0, text.getCaretOffset());
	text.setText("Line0\r\n");
	assertEquals(":b:", 0, text.getCaretOffset());
	text.setTopIndex(1);
	assertEquals(":c:", 0, text.getCaretOffset());
	text.replaceTextRange(text.getCharCount(), 0, "Line1");
	assertEquals(":d:", 0, text.getCaretOffset());
	String newText = "Line-1\r\n";
	text.replaceTextRange(0, 0, newText);
	assertEquals(":e:", 0, text.getCaretOffset());

	text.setCaretOffset(1);
	assertEquals(":f:", 1, text.getCaretOffset());
	text.replaceTextRange(2, 0, newText);
	assertEquals(":g:", 1, text.getCaretOffset());
	text.replaceTextRange(0, 0, newText);
	assertEquals(":h:", newText.length() + 1, text.getCaretOffset());
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
	assertEquals(":a:", 0, text.getHorizontalIndex());
	text.setHorizontalIndex(-1);
	assertEquals(":b:", 0, text.getHorizontalIndex());
	text.setHorizontalIndex(1);
	assertEquals(":c:", 0, text.getHorizontalIndex());

	text.setText("Line0");
	assertEquals(":d:", 0, text.getHorizontalIndex());
	text.setHorizontalIndex(-1);
	assertEquals(":e:", 0, text.getHorizontalIndex());
	text.setHorizontalIndex(1);
	assertEquals(":f:", 1, text.getHorizontalIndex());
	text.setHorizontalIndex(500);
	assertTrue(":g:", text.getHorizontalIndex() > 0);
	text.setHorizontalIndex(-1);
	assertEquals(":h:", 0, text.getHorizontalIndex());
	text.setHorizontalIndex(1);
	assertEquals(":i:", 1, text.getHorizontalIndex());
	// make sure the widget can be scrolled
	shell.open();
	text.setSize(10, 50);
	text.setText("Line0");
	text.setHorizontalIndex(1);
	assertEquals(":j:", 1, text.getHorizontalIndex());
}

@Test
public void test_getHorizontalPixel() {
	assertEquals(":a:", 0, text.getHorizontalPixel());
	text.setHorizontalIndex(-1);
	assertEquals(":b:", 0, text.getHorizontalPixel());
	text.setHorizontalIndex(1);
	assertEquals(":c:", 0, text.getHorizontalPixel());

	text.setText("Line0");
	assertEquals(":d:", 0, text.getHorizontalPixel());
	text.setHorizontalIndex(-1);
	assertEquals(":e:", 0, text.getHorizontalPixel());
	text.setHorizontalIndex(1);
	assertTrue(":f:", text.getHorizontalPixel() > 0);
	text.setHorizontalIndex(-1);
	assertEquals(":g:", 0, text.getHorizontalPixel());
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
	assertEquals(":a:", ST.DELETE_NEXT, text.getKeyBinding(SWT.DEL));
	text.setKeyBinding(SWT.DEL, ST.LINE_UP);
	assertEquals(":b:", ST.LINE_UP, text.getKeyBinding(SWT.DEL));
	text.setKeyBinding(SWT.DEL | SWT.MOD2, ST.SELECT_PAGE_UP);
	assertEquals(":c:", ST.SELECT_PAGE_UP, text.getKeyBinding(SWT.DEL | SWT.MOD2));
	text.setKeyBinding(SWT.DEL | SWT.MOD2, ST.PAGE_UP);
	assertEquals(":d:", ST.PAGE_UP, text.getKeyBinding(SWT.DEL | SWT.MOD2));
	assertEquals(":e:", SWT.NULL, text.getKeyBinding(-1));
	assertEquals(":f:", SWT.NULL, text.getKeyBinding(SWT.F2));
}

@Test
public void test_getCharCount() {
	assertEquals(":a:", 0, text.getCharCount());
	text.setText("Line0");
	assertEquals(":b:", 5, text.getCharCount());
	text.setText("");
	assertEquals(":c:", 0, text.getCharCount());
	text.setText("Line0\n");
	assertEquals(":d:", 6, text.getCharCount());
}

@Test
public void test_getLineBackgroundI() {
	String textString = "L1\nL2\nL3\nL4";
	text.setText(textString);
	assertNull(":1:", text.getLineBackground(0));
	text.setLineBackground(1,1,getColor(YELLOW));
	text.setLineBackground(2,1,getColor(BLUE));
	assertEquals(":1:", getColor(YELLOW), text.getLineBackground(1));
	assertEquals(":1:", getColor(BLUE), text.getLineBackground(2));
}

@Test
public void test_getLineCount() {
	String delimiterString = "\r\n";
	assertEquals(":a:", 1, text.getLineCount());
	text.append("dddasd" + delimiterString);
	assertEquals(":b:", 2, text.getLineCount());
	text.append("ddasdasdasdasd" + delimiterString);
	assertEquals(":c:", 3, text.getLineCount());


	text.setText("01234567890");
	text.setSelection(4);
	assertEquals(":a:", 1, text.getLineCount());
	text.insert(delimiterString);
	assertEquals(":b:", 2, text.getLineCount());
}

@Test
public void test_getLineAtOffsetI() {
	assertEquals(":a:", 0, text.getLineAtOffset(0));

	assertThrows(IllegalArgumentException.class, () ->text.getLineAtOffset(-1));
	assertThrows(IllegalArgumentException.class, () ->text.getLineAtOffset(100));

	text.setText("Line0\r\n");
	assertEquals(":d:", 0, text.getLineAtOffset(4));
	assertEquals(":e:", 0, text.getLineAtOffset(5));
	assertEquals(":f:", 0, text.getLineAtOffset(6));
	assertEquals(":g:", 1, text.getLineAtOffset(7));

	assertThrows(IllegalArgumentException.class, () ->text.getLineAtOffset(8));
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

	assertEquals(":a:", new Point(XINSET, 0), text.getLocationAtOffset(0));
	assertThrows("No exception thrown for offset == -1", IllegalArgumentException.class, () ->
		text.getLocationAtOffset(-1));

	assertThrows("No exception thrown for illegal offset argument", IllegalArgumentException.class, () ->
		text.getLocationAtOffset(100));

	text.setText("Line0\r\nLine1");
	assertTrue(":d:", text.getLocationAtOffset(4).x > 0 && text.getLocationAtOffset(4).y == 0);
	assertTrue(":e:", text.getLocationAtOffset(6).x > 0 && text.getLocationAtOffset(6).y == 0);
	// x location will == StyledText x inset on bidi platforms
	assertTrue(":f:", text.getLocationAtOffset(7).x == XINSET && text.getLocationAtOffset(7).y > 0);
	assertThrows("No exception thrown for illegal offset argument", IllegalArgumentException.class, () ->
		text.getLocationAtOffset(13));

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
	assertEquals(":a:", 0, text.getOffsetAtLine(0));
	assertThrows(IllegalArgumentException.class, () -> text.getOffsetAtLine(-1));

	assertThrows(IllegalArgumentException.class, () -> text.getOffsetAtLine(100));

	text.setText("Line0\r\n");
	assertEquals(":d:", 0, text.getOffsetAtLine(0));
	assertEquals(":e:", 7, text.getOffsetAtLine(1));

	assertThrows(IllegalArgumentException.class, () -> text.getOffsetAtLine(2));

	text.setText("");
	assertEquals(":g:", 0, text.getOffsetAtLine(0));
}
@SuppressWarnings("deprecation")
@Test
public void test_getOffsetAtLocationLorg_eclipse_swt_graphics_Point() {
	Point location;
	final int XINSET = isBidiCaret() ? 2 : 0;

	assertEquals(":a:", 0, text.getOffsetAtLocation(new Point(XINSET, 0)));
	assertThrows(IllegalArgumentException.class, () -> text.getOffsetAtLocation(new Point(-1, 0)));

	assertThrows(IllegalArgumentException.class, () -> text.getOffsetAtLocation(new Point(0, -1)));

	text.setText("Line0\r\nLine1");
	location = text.getLocationAtOffset(5);
	assertTrue(":d:", text.getOffsetAtLocation(new Point(10, 0)) > 0);
	assertEquals(":e:", 5, text.getOffsetAtLocation(new Point(location.x - 1, 0)));
	location = text.getLocationAtOffset(7);
	assertEquals(":f:", 7, text.getOffsetAtLocation(location));
	assertThrows(IllegalArgumentException.class, () -> text.getOffsetAtLocation(new Point(100, 0)));

	assertThrows(IllegalArgumentException.class, () -> text.getOffsetAtLocation(new Point(100, 50)));

	text.setTopIndex(1);
	assertEquals(":i:", 0, text.getOffsetAtLocation(new Point(XINSET, -5)));
	assertEquals(":j:", 7, text.getOffsetAtLocation(new Point(XINSET, 0)));

	text.setHorizontalIndex(1);
	assertEquals(":k:", 0, text.getOffsetAtLocation(new Point(XINSET + -5, -5)));
	assertEquals(":l:", 7, text.getOffsetAtLocation(new Point(XINSET + -5, 0)));

	// 1GL4ZVE
	assertEquals(":m:", 2, text.getOffsetAtLocation(text.getLocationAtOffset(2)));
	text.setHorizontalIndex(0);
	assertEquals(":n:", 2, text.getOffsetAtLocation(text.getLocationAtOffset(2)));
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
	assertEquals(":b:", new Point(2, 2), text.getSelection());
	text.setSelection(new Point(2, 3));
	assertEquals(":c:", new Point(2, 3), text.getSelection());
	text.setSelection(new Point(3, 11));
	assertEquals(":d:", new Point(3, 11), text.getSelection());
	text.setText("01234567890");
	text.setSelection(4);
	assertEquals(":a:", new Point(4, 4), text.getSelection());
	text.setSelection(11);
	assertEquals(":b:", new Point(11, 11), text.getSelection());
	text.setSelection(new Point(3, 2));
	assertEquals(":c:", new Point(2, 3), text.getSelection());
}
@Test
public void test_getSelectionBackground() {
	assertNotNull(":1:", text.getSelectionBackground());
	text.setSelectionBackground(getColor(YELLOW));
	assertEquals(":1:", getColor(YELLOW), text.getSelectionBackground());
}
@Test
public void test_getSelectionForeground() {
	assertNotNull(":1:", text.getSelectionForeground());
	text.setSelectionForeground(getColor(RED));
	assertEquals(":1:", getColor(RED), text.getSelectionForeground());
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
public void test_textChangeAfterSelection() {
	// tests https://bugs.eclipse.org/bugs/show_bug.cgi?id=562676#c5
	shell.setLayout(new GridLayout(1, false));
	GridData layoutData = new GridData(SWT.FILL, SWT.FILL,true, true);
	text.setLayoutData(layoutData);
	// requires visible shell to get locations computed
	shell.setVisible(true);
	// requires variable line height
	text.setWordWrap(true);
	text.setText(IntStream.range(1, 50).mapToObj(Integer::toString).collect(Collectors.joining("\n")));
	int startOffset = text.getOffsetAtLine(text.getLineCount() - 1);
	text.setSelection(startOffset, startOffset + 1);
	text.showSelection();
	StyledText other = new StyledText(shell, SWT.NONE);
	other.setText(IntStream.range(1, 100).mapToObj(Integer::toString).collect(Collectors.joining("\n")));
	StyledTextContent otherContent = other.getContent();
	other.dispose();
	// need setContent to reproduce bug to cascade to text.reset()
	text.setContent(otherContent);
}

@Test
public void test_getSelectionCount(){
	text.setText("01234567890");
	assertEquals(":a:", 0, text.getSelectionCount());
	text.setSelection(2, 4);
	assertEquals(":b:", 2, text.getSelectionCount());
	text.setSelection(2, 11);
	assertEquals(":c:", 9, text.getSelectionCount());
	text.setText("0123\n4567890");
	assertEquals(":d:", 0, text.getSelectionCount());
	text.setSelection(2, 4);
	assertEquals(":e:", 2, text.getSelectionCount());
	text.setSelection(2, 12);
	assertEquals(":f:", 10, text.getSelectionCount());
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
	StyleRange style = new StyleRange(styleStart, styleLength, getColor(BLUE), getColor(RED), SWT.BOLD);

	assertThrows("offset out of range no text", IllegalArgumentException.class, () -> text.getStyleRangeAtOffset(0));

	text.setText(line);
	assertThrows("offset out of range negative", IllegalArgumentException.class, () -> text.getStyleRangeAtOffset(-1));
	assertThrows("offset out of range positive", IllegalArgumentException.class,
			() -> text.getStyleRangeAtOffset(line.length()));

	text.setStyleRange(style);
	style.length = 1;
	for (i = styleStart; i < styleStart + styleLength; i++) {
		style.start = i;
		assertEquals(style, text.getStyleRangeAtOffset(i));
	}
	assertNull(text.getStyleRangeAtOffset(i));

	// test offset at line delimiter
	style = new StyleRange(5, 2, null, getColor(BLUE), SWT.NORMAL);
	text.setStyleRange(style);
	style.length = 1;
	assertEquals(style, text.getStyleRangeAtOffset(5));
	style.start = 6;
	assertEquals(style, text.getStyleRangeAtOffset(6));
	assertNull(text.getStyleRangeAtOffset(10));
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
	assertEquals(":1:", "package test;\n\t/*Line 1\n\t * Line 2\n\t */\npublic class SimpleClass {\n}", newText);
	StyleRange[] styles = text.getStyleRanges();
	assertEquals(":1:", 3, styles.length);
	assertEquals(":1:", getStyle(0,7,BLUE,null), styles[0]);
	assertEquals(":1:", getStyle(40,6,BLUE,null), styles[1]);
	assertEquals(":1:", getStyle(47,5,BLUE,null), styles[2]);
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
	assertEquals(":1:", 0, styles.length);
	styles = text.getStyleRanges(0,5);
	assertEquals(":2:", 1, styles.length);
	assertEquals(":2:", getStyle(1,4,BLUE,null), styles[0]);
	styles = text.getStyleRanges(7,3);
	assertEquals(":3:", 1, styles.length);
	assertEquals(":3:", getStyle(7,2,YELLOW,null), styles[0]);
	styles = text.getStyleRanges(0,10);
	assertEquals(":4:", 3, styles.length);
	assertEquals(":4:", getStyle(1,4,BLUE,null), styles[0]);
	assertEquals(":4:", getStyle(5,2,RED,null), styles[1]);
	assertEquals(":4:", getStyle(7,2,YELLOW,null), styles[2]);
	styles = text.getStyleRanges(0,4);
	assertEquals(":5:", 1, styles.length);
	assertEquals(":5:", getStyle(1,3,BLUE,null), styles[0]);
	styles = text.getStyleRanges(2,6);
	assertEquals(":6:", 3, styles.length);
	assertEquals(":6:", getStyle(2,3,BLUE,null), styles[0]);
	assertEquals(":6:", getStyle(5,2,RED,null), styles[1]);
	assertEquals(":6:", getStyle(7,1,YELLOW,null), styles[2]);

	text.setText("0123456789\r\nABCDEFGHIJKL");
	text.setStyleRange(getStyle(4,3,BLUE,null));
	text.setStyleRange(getStyle(7,5,RED,null));
	text.setStyleRange(getStyle(15,1,YELLOW,null));
	styles = text.getStyleRanges(15,1);
	assertEquals(":1a:", 1, styles.length);
	assertEquals(":1a:", getStyle(15,1,YELLOW,null), styles[0]);
	styles = text.getStyleRanges(15,0);
	assertEquals(":2a:", 0, styles.length);
	styles = text.getStyleRanges(0,20);
	assertEquals(":3a:", 3, styles.length);
	assertEquals(":3a:", getStyle(4,3,BLUE,null), styles[0]);
	assertEquals(":3a:", getStyle(7,5,RED,null), styles[1]);
	assertEquals(":3a:", getStyle(15,1,YELLOW,null), styles[2]);
	styles = text.getStyleRanges(8,2);
	assertEquals(":4a:", 1, styles.length);
	assertEquals(":4a:", getStyle(8,2,RED,null), styles[0]);

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
	assertEquals(":a:", 1, text.getTabs());
	text.setTabs(8);
	assertEquals(":b:", 8, text.getTabs());
	text.setText("Line\t1\r\n");
	assertEquals(":c:", 8, text.getTabs());
	text.setTabs(7);
	assertEquals(":d:", 7, text.getTabs());
}
@Test
public void test_getText() {
	String testText = "Line1\r\nLine2";

	assertEquals(":a:", 0, text.getText().length());
	text.setText(testText);
	assertEquals(":b:", testText, text.getText());
	text.setText("");
	assertEquals(":c:", 0, text.getText().length());

	text.setText(testText);
	assertEquals(":a:", testText, text.getText());
	text.setText(testText + "\r\n");
	assertEquals(":b:", testText + "\r\n", text.getText());
	text.setText("");
	assertEquals(":c:", 0, text.getText().length());
}
@Test
public void test_getTextII() {
	String testText = "Line1\r\nLine2";
	int invalidRanges[][] = {{-1, 0}, {0, -1}, {-1, -1}, {100, 1}, {100, -1}, {2, testText.length()}, {5, 2}};
	int ranges[][] = {{0, 1}, {0, 0}, {2, 5}, {7, 11}};

	for (int[] invalidRange : invalidRanges) {
		int start = invalidRange[0];
		int end = invalidRange[1];
		assertThrows(IllegalArgumentException.class, () -> text.getText(start, end));
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

		assertThrows(IllegalArgumentException.class, () -> text.getText(start, end));
	}
	text.setText("testing");
	assertEquals(":d:", "t", text.getText(0,0));
	assertEquals(":d:", "te", text.getText(0,1));
	assertEquals(":d:", "estin", text.getText(1,5));
}
@Test
public void test_getTextRangeII() {
	String testText = "Line1\r\nLine2";
	int invalidRanges[][] = {{-1, 0}, {0, -1}, {-1, -1}, {100, 1}, {100, -1}, {1, testText.length()}, {5, -1}};
	int ranges[][] = {{0, 1}, {0, 0}, {5, 1}, {7, 5}, {12, 0}};

	for (int[] invalidRange : invalidRanges) {
		int start = invalidRange[0];
		int length = invalidRange[1];

		assertThrows(IllegalArgumentException.class, () -> text.getTextRange(start, length));
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

		assertThrows(IllegalArgumentException.class, () -> text.getTextRange(start, length));
	}
	text.setText("testing");
	assertTrue(":d:", text.getTextRange(0,0).isEmpty());
	assertEquals(":d:", "t", text.getTextRange(0, 1));
	assertEquals(":d:", "te", text.getTextRange(0, 2));
	assertEquals(":d:", "estin", text.getTextRange(1, 5));
}
@Test
public void test_getTextLimit() {
	assertTrue(":a:", text.getTextLimit() < 0);
	text.setTextLimit(10);
	assertEquals(";b:", 10, text.getTextLimit());
}

@Test
public void test_getTopIndex() {
	text.setText("Line0\r\nLine0a\r\n");

	assertEquals(":a:", 0, text.getTopIndex());
	text.setTopIndex(-2);
	assertEquals(":b:", 0, text.getTopIndex());
	text.setTopIndex(-1);
	assertEquals(":c:", 0, text.getTopIndex());
	text.setTopIndex(1);
	assertEquals(":d:", 1, text.getTopIndex());
	text.setTopIndex(2);
	assertEquals(":e:", 2, text.getTopIndex());
	text.setTopIndex(0);
	assertEquals(":f:", 0, text.getTopIndex());
	text.setTopIndex(3);
	assertEquals(":g:", 2, text.getTopIndex());
	text.replaceTextRange(text.getCharCount(), 0, "Line1");
	assertEquals(":h:", 2, text.getTopIndex());
	text.setText("");
	assertEquals(":i:", 0, text.getTopIndex());
}

@Test
public void test_getTopPixel() {
	text.setText("Line0\r\nLine0a\r\n");

	assertEquals(":a:", 0, text.getTopPixel());
	text.setTopIndex(-2);
	assertEquals(":b:", 0, text.getTopPixel());
	text.setTopIndex(-1);
	assertEquals(":c:", 0, text.getTopPixel());
	text.setTopIndex(1);
	assertEquals(":d:", text.getLineHeight(), text.getTopPixel());
	text.setTopIndex(2);
	assertEquals(":e:", text.getLineHeight() * 2, text.getTopPixel());
	text.setTopIndex(0);
	assertEquals(":f:", 0, text.getTopPixel());
	text.setTopIndex(3);
	assertEquals(":g:", text.getLineHeight() * 2, text.getTopPixel());
	text.replaceTextRange(text.getCharCount(), 0, "Line1");
	assertEquals(":h:", text.getLineHeight() * 2, text.getTopPixel());
	text.setText("");
	assertEquals(":i:", 0, text.getTopPixel());
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
	assertThrows("No exception thrown for string == null", IllegalArgumentException.class,
		() -> text.insert(null));
	assertTrue(":a:", text.getText().isEmpty());
	text.insert("");
	assertTrue(":b:", text.getText().isEmpty());
	text.insert("fred");
	assertEquals(":c:", "fred", text.getText());
	text.setSelection(2);
	text.insert("helmut");
	assertEquals(":d:", "frhelmuted", text.getText());
	text.setText("01234567890");
	text.setSelection(4);
	assertEquals(":e:", 1, text.getLineCount());
	text.insert(delimiterString);
	assertEquals(":f:", 2, text.getLineCount());
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
	assertEquals(12, text.getCaretOffset());
	text.invokeAction(ST.SELECT_LINE_UP);
	assertEquals("\r\nLineW", text.getSelectionText());
	assertEquals(5, text.getCaretOffset());

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
	assertEquals("LineL", text.getSelectionText());
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
	assertEquals(":a:", 1, text.getCharCount());

	text.setSelectionRange(0, 0);
	text.copy();
	text.paste();
	assertEquals(":b:", 2, text.getCharCount());

	text.setText("0123456789");
	text.setSelectionRange(0, 1);
	text.copy();
	text.setCaretOffset(0);
	text.paste();
	assertEquals(":c:", "00123456789", text.getText());
	text.setSelectionRange(1, 2);
	text.copy();
	text.setText("");
	text.paste();
	assertEquals(":d:", "01", text.getText());
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

	assertThrows("no exception thrown for print(null)", IllegalArgumentException.class, () ->
		text.print((Printer) null));

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
	text.redrawRange(0, 0, true);
	text.redrawRange(0, 0, false);

	IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> text.redrawRange(0, 1, true));
	assertEquals("Index out of bounds", e.getMessage());
	e = assertThrows(IllegalArgumentException.class, () -> text.redrawRange(0, 1, false));
	assertEquals("Index out of bounds", e.getMessage());

	e = assertThrows(IllegalArgumentException.class, () -> text.redrawRange(-1, 2, true));
	assertEquals("Index out of bounds", e.getMessage());

	e = assertThrows(IllegalArgumentException.class, () -> text.redrawRange(-1, 2, false));
	assertEquals("Index out of bounds", e.getMessage());

	text.setText("0123456789");
	text.redrawRange(0, 0, true);
	text.redrawRange(0, 0, false);
	text.redrawRange(0, 1, true);
	text.redrawRange(0, 1, false);
	text.redrawRange(8, 2, true);
	text.redrawRange(8, 2, false);
	text.redrawRange(10, 0, true);
	text.redrawRange(10, 0, false);

	e = assertThrows(IllegalArgumentException.class, () -> text.redrawRange(10, 1, true));
	assertEquals("Index out of bounds", e.getMessage());

	e = assertThrows(IllegalArgumentException.class, () -> text.redrawRange(10, 1, false));
	assertEquals("Index out of bounds", e.getMessage());
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
	assertEquals(":0:", 0, styles.length);
	text.setText(textString);
	styles = text.getStyleRanges();
	assertEquals(":0:", 0, styles.length);
	text.replaceStyleRanges(0, 78, new StyleRange[] {});
	styles = text.getStyleRanges();
	assertEquals(":0:", 0, styles.length);
	text.replaceStyleRanges(0, text.getCharCount(), defaultStyles());
	styles = text.getStyleRanges();
	assertEquals(":0:", 3, styles.length);
	assertEquals(":0:",getStyle(0,48,RED,YELLOW), styles[0]);
	assertEquals(":0:",getStyle(58,10,BLUE,CYAN), styles[1]);
	assertEquals(":0:", getStyle(68,10,GREEN,PURPLE), styles[2]);

	// No overlap with existing styles
	text.replaceStyleRanges(0, text.getCharCount(), defaultStyles());
	text.replaceStyleRanges(48, 5, new StyleRange[] {getStyle(48,5,YELLOW,RED)});
	styles = text.getStyleRanges();
	assertEquals(":1:", 4, styles.length);
	assertEquals(":1:", getStyle(0,48,RED,YELLOW), styles[0]);
	assertEquals(":1:", getStyle(48,5,YELLOW,RED), styles[1]);
	assertEquals(":1:", getStyle(58,10,BLUE,CYAN), styles[2]);
	assertEquals(":1:", getStyle(68,10,GREEN,PURPLE), styles[3]);

	// Overlap middle of one style - partial
	text.replaceStyleRanges(0, text.getCharCount(), defaultStyles());
	text.replaceStyleRanges(10, 10, new StyleRange[] {getStyle(10,10,YELLOW,RED)});
	styles = text.getStyleRanges();
	assertEquals(":2:", 5, styles.length);
	assertEquals(":2:", getStyle(0,10,RED,YELLOW), styles[0]);
	assertEquals(":2:", getStyle(10,10,YELLOW,RED), styles[1]);
	assertEquals(":2:", getStyle(20,28,RED,YELLOW), styles[2]);
	assertEquals(":2:", getStyle(58,10,BLUE,CYAN), styles[3]);
	assertEquals(":2:", getStyle(68,10,GREEN,PURPLE), styles[4]);
	text.replaceStyleRanges(0, text.getCharCount(), new StyleRange[] {});
	styles = text.getStyleRanges();
	assertEquals(":2:", 0, styles.length);

	// Overlap middle of one style - full
	text.replaceStyleRanges(0, text.getCharCount(), defaultStyles());
	text.replaceStyleRanges(58, 10, new StyleRange[] {getStyle(58,10,YELLOW,RED)});
	styles = text.getStyleRanges();
	assertEquals(":3:", 3, styles.length);
	assertEquals(":3:", getStyle(0,48,RED,YELLOW), styles[0]);
	assertEquals(":3:", getStyle(58,10,YELLOW,RED), styles[1]);
	assertEquals(":3:", getStyle(68,10,GREEN,PURPLE), styles[2]);

	// Overlap end of one style
	text.replaceStyleRanges(0, text.getCharCount(), defaultStyles());
	text.replaceStyleRanges(38, 15, new StyleRange[] {getStyle(38,15,YELLOW,RED)});
	styles = text.getStyleRanges();
	assertEquals(":4:", 4, styles.length);
	assertEquals(":4:", getStyle(0,38,RED,YELLOW), styles[0]);
	assertEquals(":4:", getStyle(38,15,YELLOW,RED), styles[1]);
	assertEquals(":4:", getStyle(58,10,BLUE,CYAN), styles[2]);
	assertEquals(":4:", getStyle(68,10,GREEN,PURPLE), styles[3]);

	// Overlap beginning of one style
	text.replaceStyleRanges(0, text.getCharCount(), defaultStyles());
	text.replaceStyleRanges(50, 10, new StyleRange[] {getStyle(50,10,YELLOW,RED)});
	styles = text.getStyleRanges();
	assertEquals(":5:", 4, styles.length);
	assertEquals(":5:", getStyle(0,48,RED,YELLOW), styles[0]);
	assertEquals(":5:", getStyle(50,10,YELLOW,RED), styles[1]);
	assertEquals(":5:", getStyle(60,8,BLUE,CYAN), styles[2]);
	assertEquals(":5:", getStyle(68,10,GREEN,PURPLE), styles[3]);

	// Overlap complete style
	text.replaceStyleRanges(0, text.getCharCount(), defaultStyles());
	text.replaceStyleRanges(48, 20, new StyleRange[] {getStyle(48,20,YELLOW,RED)});
	styles = text.getStyleRanges();
	assertEquals(":6:", 3, styles.length);
	assertEquals(":6:", getStyle(0,48,RED,YELLOW), styles[0]);
	assertEquals(":6:", getStyle(48,20,YELLOW,RED), styles[1]);
	assertEquals(":6:", getStyle(68,10,GREEN,PURPLE), styles[2]);

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
	assertEquals(":7:", 3, styles.length);
	assertEquals(":7:", getStyle(0,48,RED,YELLOW), styles[0]);
	assertEquals(":7", getStyle(48,20,BLUE,CYAN), styles[1]);
	assertEquals(":7:", getStyle(68,10,GREEN,PURPLE), styles[2]);

	text.setText("012345678901234");
	ranges = new StyleRange[2];
	ranges[0] = getStyle(0,5,RED,YELLOW);
	ranges[1] = getStyle(10,5,BLUE,CYAN);
	text.replaceStyleRanges(0, 15, ranges);
	styles = text.getStyleRanges();
	assertEquals(":8:", 2, styles.length);
	assertEquals(":8:", getStyle(0,5,RED,YELLOW), styles[0]);
	assertEquals(":8:", getStyle(10,5,BLUE,CYAN), styles[1]);

	text.setText("redgreenblueyellowcyanpurple");
	ranges = new StyleRange[4];
	ranges[0] = getStyle(0,3,RED,null);
	ranges[1] = getStyle(3,5,GREEN,null);
	ranges[2] = getStyle(8,4,BLUE,null);
	ranges[3] = getStyle(12,6,YELLOW,null);
	text.replaceStyleRanges(0, 18, ranges);
	styles = text.getStyleRanges();
	assertEquals(":9:", 4, styles.length);
	assertEquals(":9:", getStyle(0,3,RED,null), styles[0]);
	assertEquals(":9:", getStyle(3,5,GREEN,null), styles[1]);
	assertEquals(":9:", getStyle(8,4,BLUE, null), styles[2]);
	assertEquals(":9:", getStyle(12,6,YELLOW,null), styles[3]);
	ranges = new StyleRange[2];
	ranges[0] = getStyle(18,4,CYAN,null);
	ranges[1] = getStyle(22,6,PURPLE,null);
	text.replaceStyleRanges(18, 10, ranges);
	styles = text.getStyleRanges();
	assertEquals(":9:", 6, styles.length);
	assertEquals(":9:", getStyle(18,4,CYAN,null), styles[4]);
	assertEquals(":9:", getStyle(22,6,PURPLE,null), styles[5]);

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
	assertEquals(":10:", 3, styles.length);
	assertEquals(":10:", getStyle(0,5,RED,YELLOW), styles[0]);
	assertEquals(":10:", getStyle(5,15,BLUE,CYAN), styles[1]);
	assertEquals(":10:", getStyle(20,15,GREEN,PURPLE), styles[2]);

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
	assertEquals(":11:", 2, styles.length);
	assertEquals(":11:", getStyle(0,8,RED,YELLOW), styles[0]);
	assertEquals(":11:", getStyle(12,8,BLUE,CYAN), styles[1]);

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
	assertEquals(":12:", 1, styles.length);
	assertEquals(":12:", getStyle(0,15,RED,YELLOW), styles[0]);

	text.setText("0123456789012345");
	ranges = new StyleRange[1];
	ranges[0] = getStyle(10,5,GREEN,PURPLE);
	text.replaceStyleRanges(0, 15, ranges);
	ranges = new StyleRange[2];
	ranges[0] = getStyle(0,5,RED,YELLOW);
	ranges[1] = getStyle(5,5,BLUE,CYAN);
	text.replaceStyleRanges(0, 10, ranges);
	styles = text.getStyleRanges();
	assertEquals(":13:", 3, styles.length);
	assertEquals(":13:", getStyle(0,5,RED,YELLOW), styles[0]);
	assertEquals(":13:", getStyle(5,5,BLUE,CYAN), styles[1]);
	assertEquals(":13:", getStyle(10,5,GREEN,PURPLE), styles[2]);

	text.setText("012345678901234");
	ranges = new StyleRange[2];
	ranges[0] = getStyle(0,5,RED,YELLOW);
	ranges[1] = getStyle(10,5,BLUE,CYAN);
	text.replaceStyleRanges(0, 15, ranges);
	ranges = new StyleRange[1];
	ranges[0] = getStyle(5,7,BLUE,CYAN);
	text.replaceStyleRanges(5, 7, ranges);
	styles = text.getStyleRanges();
	assertEquals(":14:", 2, styles.length);
	assertEquals(":14:", getStyle(0,5,RED,YELLOW), styles[0]);
	assertEquals(":14:", getStyle(5,10,BLUE,CYAN), styles[1]);


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
	assertEquals(":1a:", 4, styles.length);
	assertEquals(":1a:", getStyle(0,38,RED,YELLOW), styles[0]);
	assertEquals(":1a:", getStyle(38,25,YELLOW,RED), styles[1]);
	assertEquals(":1a:", getStyle(63,5,BLUE,CYAN), styles[2]);
	assertEquals(":1a:", getStyle(68,10,GREEN,PURPLE), styles[3]);
	text.setStyleRanges(defaultStyles());
	ranges = new StyleRange[1];
	ranges[0] = getStyle(63,10,YELLOW,RED);
	text.replaceStyleRanges(63, 10, ranges);
	styles = text.getStyleRanges();
	assertEquals(":1a:", 4, styles.length);
	assertEquals(":1a:", getStyle(0,48,RED,YELLOW), styles[0]);
	assertEquals(":1a:", getStyle(58,5,BLUE,CYAN), styles[1]);
	assertEquals(":1a:", getStyle(63,10,YELLOW,RED), styles[2]);
	assertEquals(":1a:", getStyle(73,5,GREEN,PURPLE), styles[3]);

	// Complete overlap
	text.setStyleRanges(defaultStyles());
	ranges = new StyleRange[1];
	ranges[0] = getStyle(0,78,YELLOW,RED);
	text.replaceStyleRanges(0, 78, ranges);
	styles = text.getStyleRanges();
	styles = text.getStyleRanges();
	assertEquals(":2a:", 1, styles.length);
	assertEquals(":2a:", getStyle(0,78,YELLOW,RED), styles[0]);

	text.setStyleRanges(defaultStyles());
	ranges = new StyleRange[1];
	ranges[0] = getStyle(0,68,YELLOW,RED);
	text.replaceStyleRanges(0, 68, ranges);
	styles = text.getStyleRanges();
	assertEquals(":2a:", 2, styles.length);
	assertEquals(":2a:", getStyle(0,68,YELLOW,RED), styles[0]);
	assertEquals(":2a:", getStyle(68,10,GREEN,PURPLE), styles[1]);
	text.setStyleRanges(defaultStyles());
	ranges = new StyleRange[1];
	ranges[0] = getStyle(58,20,YELLOW,RED);
	text.replaceStyleRanges(58, 20, ranges);
	styles = text.getStyleRanges();
	assertEquals(":2a:", 2, styles.length);
	assertEquals(":2a:", getStyle(0,48,RED,YELLOW), styles[0]);
	assertEquals(":2a:", getStyle(58,20,YELLOW,RED), styles[1]);

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
	assertEquals(":3a:", 3, styles.length);
	assertEquals(":3a:", getStyle(0,5,RED,RED), styles[0]);
	assertEquals(":3a:", getStyle(5,23,YELLOW,RED), styles[1]);
	assertEquals(":3a:", getStyle(28,2,PURPLE,PURPLE), styles[2]);

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
	assertEquals(":3a:", 5, styles.length);
	assertEquals(":3a:", getStyle(0,5,RED,RED), styles[0]);
	assertEquals(":3a:", getStyle(5,5,YELLOW,YELLOW), styles[1]);
	assertEquals(":3a:", getStyle(10,3,CYAN,CYAN), styles[2]);
	assertEquals(":3a:", getStyle(13,12,YELLOW,RED), styles[3]);
	assertEquals(":3a:", getStyle(25,5,PURPLE,PURPLE), styles[4]);

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
	assertEquals(":1xa:", 1, styles.length);
	assertEquals(":1xa", getStyle(0,5,RED,YELLOW), styles[0]);

	// insert before 1 style
	text.setText("01234567890123456789");
	ranges = new StyleRange[1];
	ranges[0] = getStyle(5,3,RED,YELLOW);
	text.replaceStyleRanges(0, 10, ranges);
	ranges = new StyleRange[1];
	ranges[0] = getStyle(0,3,PURPLE,PURPLE);
	text.replaceStyleRanges(0, 3, ranges);
	styles = text.getStyleRanges();
	assertEquals(":1xb:", 2, styles.length);
	assertEquals(":1xb", getStyle(0,3,PURPLE,PURPLE), styles[0]);
	assertEquals(":1xb", getStyle(5,3,RED,YELLOW), styles[1]);

	// insert after 1 style
	text.setText("01234567890123456789");
	ranges = new StyleRange[1];
	ranges[0] = getStyle(5,3,RED,YELLOW);
	text.replaceStyleRanges(0, 10, ranges);
	ranges = new StyleRange[1];
	ranges[0] = getStyle(8,1,PURPLE,PURPLE);
	text.replaceStyleRanges(8, 1, ranges);
	styles = text.getStyleRanges();
	assertEquals(":1xc", 2, styles.length);
	assertEquals(":1xc:", getStyle(5,3,RED,YELLOW), styles[0]);
	assertEquals(":1xc:", getStyle(8,1,PURPLE,PURPLE), styles[1]);

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
	assertEquals(":1xe:", 3, styles.length);
	assertEquals(":1xe:", getStyle(2,1,PURPLE,PURPLE), styles[0]);
	assertEquals(":1xe:", getStyle(5,2,RED,YELLOW), styles[1]);
	assertEquals(":1xe:", getStyle(10,2,RED,YELLOW), styles[2]);

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
	assertEquals(":1xf:", 3, styles.length);
	assertEquals(":1xf", getStyle(5,2,RED,YELLOW), styles[0]);
	assertEquals(":1xf:", getStyle(10,2,RED,YELLOW), styles[1]);
	assertEquals(":1xf:", getStyle(12,1,PURPLE,PURPLE), styles[2]);

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
	assertEquals(":1xg:", 3, styles.length);
	assertEquals(":1xg:", getStyle(1,2,RED,YELLOW), styles[0]);
	assertEquals(":1xg:", getStyle(5,3,PURPLE,PURPLE), styles[1]);
	assertEquals(":1xg:", getStyle(12,2,RED,YELLOW), styles[2]);

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
	assertEquals(":1xh:", 4, styles.length);
	assertEquals(":1xh:", getStyle(1,3,RED,PURPLE), styles[0]);
	assertEquals(":1xh:", getStyle(4,2,PURPLE,PURPLE), styles[1]);
	assertEquals(":1xh:", getStyle(6,3,PURPLE,YELLOW), styles[2]);
	assertEquals(":1xh:", getStyle(12,3,RED,YELLOW), styles[3]);

	// reset the environment
	text.dispose();
	text = new StyledText(shell, SWT.NULL);
	setWidget(text);

	text.setText("0");
	ranges = new StyleRange[1];
	ranges[0] = getStyle(0,1,PURPLE,PURPLE);
	text.replaceStyleRanges(0, 1, ranges);
	styles = text.getStyleRanges();
	assertEquals(":2xa:", 1, styles.length);

	text.setText("01");
	ranges = new StyleRange[2];
	ranges[0] = getStyle(0,1,PURPLE,PURPLE);
	ranges[1] = getStyle(1,1,RED,RED);
	text.replaceStyleRanges(0, 2, ranges);
	ranges = new StyleRange[1];
	ranges[0] = getStyle(0,1,YELLOW,YELLOW);
	text.replaceStyleRanges(0, 1, ranges);
	styles = text.getStyleRanges();
	assertEquals(":2xb:", 2, styles.length);
	assertEquals(":2xb:", getStyle(0,1,YELLOW,YELLOW), styles[0]);
	assertEquals(":2xb:", getStyle(1,1,RED,RED), styles[1]);

	text.setText("01");
	ranges = new StyleRange[2];
	ranges[0] = getStyle(0,1,PURPLE,PURPLE);
	ranges[1] = getStyle(1,1,RED,RED);
	text.replaceStyleRanges(0, 2, ranges);
	ranges = new StyleRange[1];
	ranges[0] = getStyle(1,1,YELLOW,YELLOW);
	text.replaceStyleRanges(1, 1, ranges);
	styles = text.getStyleRanges();
	assertEquals(":2xc:", 2, styles.length);
	assertEquals(":2xc:", getStyle(0,1,PURPLE,PURPLE), styles[0]);
	assertEquals(":2xc:", getStyle(1,1,YELLOW,YELLOW), styles[1]);

	text.setText("012");
	ranges = new StyleRange[2];
	ranges[0] = getStyle(0,1,PURPLE,PURPLE);
	ranges[1] = getStyle(1,1,RED,RED);
	text.replaceStyleRanges(0, 2, ranges);
	ranges = new StyleRange[1];
	ranges[0] = getStyle(2,1,YELLOW,YELLOW);
	text.replaceStyleRanges(2, 1, ranges);
	styles = text.getStyleRanges();
	assertEquals(":2xd", 3, styles.length);
	assertEquals(":2xd", getStyle(0,1,PURPLE,PURPLE), styles[0]);
	assertEquals(":2xd", getStyle(1,1,RED,RED), styles[1]);
	assertEquals(":2xd:", getStyle(2,1,YELLOW,YELLOW), styles[2]);

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
	assertEquals(":2xe:", 4, styles.length);
	assertEquals(":2xe:", getStyle(4,1,YELLOW,YELLOW), styles[3]);

	text.setText("01234");
	ranges = new StyleRange[1];
	ranges[0] = getStyle(4,1,YELLOW,YELLOW);
	text.replaceStyleRanges(4, 1, ranges);
	styles = text.getStyleRanges();
	assertEquals(":2xf:", 1, styles.length);
	assertEquals(":2xf:", getStyle(4,1,YELLOW,YELLOW), styles[0]);

	text.setText("01234");
	ranges = new StyleRange[1];
	ranges[0] = getStyle(4,1,YELLOW,YELLOW);
	text.replaceStyleRanges(4, 1, ranges);
	ranges = new StyleRange[0];
	text.replaceStyleRanges(4, 1, ranges);
	styles = text.getStyleRanges();
	assertEquals(":2xg:", 0 , styles.length);

}

@Test
public void test_replaceTextRangeIILjava_lang_String(){
	final String defaultText = "line0\n\rline1\n\rline2\n\r";
	final int defaultTextLength = defaultText.length();
	final int selectionStart = 7;
	final int selectionLength = 7;
	final int replaceStart = selectionStart + selectionLength + 1;
	final int replaceLength = 5;
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
	assertEquals(":a:", defaultTextLength + newTextLength, text.getCharCount());
	assertTrue(":b:", text.getSelectionRange().x == selectionStart && text.getSelectionRange().y == selectionLength);

	// before selection
	text.setText(defaultText);
	// select 2nd line including line break
	text.setSelectionRange(selectionStart, selectionLength);
	text.replaceTextRange(0, 0, newText);
	assertEquals(":c:", defaultTextLength + newTextLength, text.getCharCount());
	assertTrue(":d:", text.getSelectionRange().x == selectionStart + newTextLength && text.getSelectionRange().y == selectionLength);
	assertEquals(text.getSelection(), selectionListener.eventSelection);


	// intersecting selection
	text.setText(defaultText);
	// select 2nd line including line break
	text.setSelectionRange(selectionStart, selectionLength);
	text.replaceTextRange(selectionStart + 1, 0, newText);
	assertEquals(":e:", defaultTextLength + newTextLength, text.getCharCount());
	assertTrue(":f:", text.getSelectionRange().x == selectionStart + 1 + newTextLength && text.getSelectionRange().y == 0);
	assertEquals(text.getSelection(), selectionListener.eventSelection);

	// out of range
	text.setText(defaultText);
	// select 2nd line including line break
	text.setSelectionRange(selectionStart, selectionLength);
	assertThrows(IllegalArgumentException.class, () -> text.replaceTextRange(-1, 0, newText));

	assertThrows(IllegalArgumentException.class, () -> text.replaceTextRange(text.getCharCount() + 1, 0, newText));

	assertThrows(IllegalArgumentException.class, () -> text.replaceTextRange(0, 0, null));

	assertTrue(":i:", text.getSelectionRange().x == selectionStart && text.getSelectionRange().y == selectionLength);

	// append text
	// append in empty widget
	text.setText("");
	text.replaceTextRange(text.getCharCount(), 0, newText);
	assertEquals(":j:", newTextLength, text.getCharCount());
	assertTrue(":k:", text.getSelectionRange().x == 0 && text.getSelectionRange().y == 0);

	// append in non-empty widget (selection should always be preserved)
	text.setText(defaultText);
	// select 2nd line including line break
	text.setSelectionRange(selectionStart, selectionLength);
	text.replaceTextRange(text.getCharCount(), 0, newText);
	assertEquals(":l:", defaultTextLength + newTextLength, text.getCharCount());
	assertTrue(":m:", text.getSelectionRange().x == selectionStart && text.getSelectionRange().y == selectionLength);

	// place caret at end of text
	text.setText(defaultText);
	text.setSelectionRange(text.getCharCount(), 0);
	text.replaceTextRange(text.getCharCount(), 0, newText);
	assertEquals(":n:", defaultTextLength + newTextLength, text.getCharCount());
	assertTrue(":o:", text.getSelectionRange().x == text.getCharCount() - newTextLength && text.getSelectionRange().y == 0);

	// replace text
	// within range
	// after selection
	text.setText(defaultText);
	// select 2nd line including line break
	text.setSelectionRange(selectionStart, selectionLength);
	text.replaceTextRange(replaceStart, replaceLength, newText);
	assertEquals(":p:", defaultTextLength + newTextLength - replaceLength, text.getCharCount());
	assertTrue(":q:", text.getSelectionRange().x == selectionStart && text.getSelectionRange().y == selectionLength);

	// before selection
	text.setText(defaultText);
	// select 2nd line including line break
	text.setSelectionRange(selectionStart, selectionLength);
	text.replaceTextRange(0, replaceLength, newText);
	assertEquals(":r:", defaultTextLength + newTextLength - replaceLength, text.getCharCount());
	assertTrue(":s:", text.getSelectionRange().x == selectionStart + newTextLength - replaceLength && text.getSelectionRange().y == selectionLength);
	assertEquals(text.getSelection(), selectionListener.eventSelection);

	// intersecting selection
	text.setText(defaultText);
	// select 2nd line including line break
	text.setSelectionRange(selectionStart, selectionLength);
	text.replaceTextRange(selectionStart + 1, replaceLength, newText);
	assertEquals(":t:", defaultTextLength + newTextLength - replaceLength, text.getCharCount());
	assertTrue(":u:", text.getSelectionRange().x == selectionStart + 1 + newTextLength && text.getSelectionRange().y == 0);
	assertEquals(text.getSelection(), selectionListener.eventSelection);

	// out of range
	text.setText(defaultText);
	// select 2nd line including line break
	text.setSelectionRange(selectionStart, selectionLength);
	assertThrows(IllegalArgumentException.class, () -> text.replaceTextRange(-1, replaceLength, newText));
	assertThrows(IllegalArgumentException.class,
			() -> text.replaceTextRange(text.getCharCount() + 1, replaceLength, newText));
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
	assertEquals(":a:", 0, text.getCaretOffset());
	text.setCaretOffset(1);
	assertEquals(":b:", 0, text.getCaretOffset());
	text.setCaretOffset(0);
	assertEquals(":c:", 0, text.getCaretOffset());

	text.setText("Line0\r\n");
	text.setCaretOffset(-2);
	assertEquals(":d:", 0, text.getCaretOffset());
	text.setCaretOffset(1);
	assertEquals(":e:", 1, text.getCaretOffset());
	text.setCaretOffset(0);
	assertEquals(":f:", 0, text.getCaretOffset());

	text.setCaretOffset(text.getCharCount());
	assertEquals(":g:", text.getCharCount(), text.getCaretOffset());
	text.setCaretOffset(text.getCharCount() + 1);
	assertEquals(":h:", text.getCharCount(), text.getCaretOffset());
	text.setCaretOffset(5);
	assertEquals(":i:", 5, text.getCaretOffset());

	text.setText("");
	text.setCaretOffset(-2);
	assertEquals(":j:", 0, text.getCaretOffset());
	text.setCaretOffset(1);
	assertEquals(":k:", 0, text.getCaretOffset());
	text.setCaretOffset(0);
	assertEquals(":l:", 0, text.getCaretOffset());
}

@Test
public void test_setContentLorg_eclipse_swt_custom_StyledTextContent() {
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

	assertThrows(IllegalArgumentException.class, () -> text.setContent(null));
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
	assertEquals(":a:", 0 , text.getHorizontalIndex());
	text.setHorizontalIndex(1);
	assertEquals(":b:", 0 , text.getHorizontalIndex());

	text.setText("Line0");
	text.setHorizontalIndex(-1);
	assertEquals(":c:", 0 , text.getHorizontalIndex());
	text.setHorizontalIndex(1);
	assertEquals(":d:", 1 , text.getHorizontalIndex());
	text.setHorizontalIndex(500);
	assertTrue(":e:", text.getHorizontalIndex() > 0);
	text.setHorizontalIndex(-1);
	assertEquals(":f:", 0 , text.getHorizontalIndex());
	text.setHorizontalIndex(1);
	assertEquals(":g:", 1 , text.getHorizontalIndex());

	text.setText("");
	text.setHorizontalIndex(2);
	assertEquals(":h:", 0 , text.getHorizontalIndex());

	// make sure the widget can be scrolled
	shell.open();
	text.setSize(10, 50);
	text.setText("Line0");
	text.setHorizontalIndex(1);
	assertEquals(":i:", 1 , text.getHorizontalIndex());
}

@Test
public void test_setHorizontalPixelI(){
	text.setHorizontalPixel(-1);
	assertEquals(":a:", 0 , text.getHorizontalPixel());
	text.setHorizontalPixel(1);
	assertEquals(":b:", 0 , text.getHorizontalPixel());

	text.setText("Line0");
	text.setHorizontalPixel(-1);
	assertEquals(":c:", 0 , text.getHorizontalPixel());
	text.setHorizontalPixel(1);
	assertEquals(":d:", 1 , text.getHorizontalPixel());
	text.setHorizontalPixel(500);
	assertTrue(":e:", text.getHorizontalPixel() > 0);
	text.setHorizontalPixel(-1);
	assertEquals(":f:", 0 , text.getHorizontalPixel());
	text.setHorizontalPixel(25);
	assertEquals(":g:", 25 , text.getHorizontalPixel());

	text.setText("");
	text.setHorizontalPixel(2);
	assertEquals(":h:", 0 , text.getHorizontalPixel());

	// make sure the widget can be scrolled
	shell.open();
	text.setSize(10, 50);
	text.setText("Line0");
	text.setHorizontalPixel(5);
	assertEquals(":i:", 5 , text.getHorizontalPixel());
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
	assertEquals(":0a:", getColor(RED), text.getLineBackground(0));
	assertEquals(":0a:", getColor(GREEN), text.getLineBackground(1));
	text.setText(textString);
	text.setLineBackground(0,1,getColor(RED));
	text.setLineBackground(1,1,getColor(YELLOW));
	text.setLineBackground(2,1,getColor(BLUE));
	text.setLineBackground(3,1,getColor(GREEN));
	text.replaceTextRange(0,4,"");
	assertEquals(":0b:", getColor(YELLOW), text.getLineBackground(0));
	assertEquals(":0b:", getColor(BLUE), text.getLineBackground(1));
	assertEquals(":0b:", getColor(GREEN), text.getLineBackground(2));
	text.setText(textString);
	text.setLineBackground(0,1,getColor(RED));
	text.setLineBackground(1,1,getColor(YELLOW));
	text.setLineBackground(2,1,getColor(BLUE));
	text.setLineBackground(3,1,getColor(GREEN));
	text.replaceTextRange(1,1,"");
	assertEquals(":0c:", getColor(RED), text.getLineBackground(0));
	assertEquals(":0c:", getColor(YELLOW), text.getLineBackground(1));
	assertEquals(":0c:", getColor(BLUE), text.getLineBackground(2));
	assertEquals(":0c:", getColor(GREEN), text.getLineBackground(3));
	text.setText(textString);
	text.setLineBackground(0,1,getColor(RED));
	text.setLineBackground(1,1,getColor(YELLOW));
	text.setLineBackground(2,1,getColor(BLUE));
	text.setLineBackground(3,1,getColor(GREEN));
	text.replaceTextRange(0,6,"");
	assertEquals(":0d:", getColor(BLUE), text.getLineBackground(0));
	assertEquals(":0d:", getColor(GREEN), text.getLineBackground(1));
	text.setText(textString);
	text.setLineBackground(0,1,getColor(RED));
	text.setLineBackground(1,1,getColor(YELLOW));
	text.setLineBackground(2,1,getColor(BLUE));
	text.setLineBackground(3,1,getColor(GREEN));
	text.replaceTextRange(1,3,"");
	assertEquals(":0e:", getColor(RED), text.getLineBackground(0));
	assertEquals(":0e:", getColor(BLUE), text.getLineBackground(1));
	assertEquals(":0e:", getColor(GREEN), text.getLineBackground(2));

	textString = "L1\nL2";
	text.setText(textString);
	text.setLineBackground(0,1,getColor(RED));
	text.setLineBackground(1,1,getColor(YELLOW));
	text.replaceTextRange(1,4,"");
	assertEquals(":0a1:", getColor(RED), text.getLineBackground(0));
	text.setText(textString);
	text.setLineBackground(0,1,getColor(RED));
	text.setLineBackground(1,1,getColor(YELLOW));
	text.replaceTextRange(0,4,"");
	assertEquals(":0b1:", getColor(YELLOW), text.getLineBackground(0));
	text.setText(textString);
	text.setLineBackground(0,1,getColor(RED));
	text.setLineBackground(1,1,getColor(YELLOW));
	text.replaceTextRange(1,1,"");
	assertEquals(":0c1:", getColor(RED), text.getLineBackground(0));
	assertEquals(":0c1:", getColor(YELLOW), text.getLineBackground(1));
	text.setText(textString);
	text.setLineBackground(0,1,getColor(RED));
	text.setLineBackground(1,1,getColor(YELLOW));
	text.replaceTextRange(0,5,"");
	assertNull(":0d1:", text.getLineBackground(0));
	text.setText(textString);
	text.setLineBackground(0,1,getColor(RED));
	text.setLineBackground(1,1,getColor(YELLOW));
	text.replaceTextRange(1,3,"");
	assertEquals(":0e1:", getColor(RED), text.getLineBackground(0));
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
	assertEquals(":1:", getColor(RED), text.getLineBackground(0));

	textString = "New Line1\nNew Line2\nNew Line3\nNew Line4";
	text.setText(textString);
	text.setLineBackground(0,2,getColor(RED));
	text.setLineBackground(2,2,getColor(YELLOW));
	text.replaceTextRange(0,0,"\n");
	assertNull(":2:", text.getLineBackground(0));
	assertEquals(":2:", getColor(RED), text.getLineBackground(1));
	assertEquals(":2:", getColor(RED), text.getLineBackground(2));
	assertEquals(":2:", getColor(YELLOW), text.getLineBackground(3));
	assertEquals(":2:", getColor(YELLOW), text.getLineBackground(4));

	textString = "New Line1\nNew Line2\nNew Line3\nNew Line4";
	text.setText(textString);
	text.setLineBackground(0,2,getColor(RED));
	text.setLineBackground(2,2,getColor(YELLOW));
	text.replaceTextRange(0,20,"");
	assertEquals(":3:", getColor(YELLOW), text.getLineBackground(0));
	assertEquals(":3:", getColor(YELLOW), text.getLineBackground(1));

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
	assertEquals(":6a:", getColor(RED), text.getLineBackground(0));
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
	assertEquals(":7:", getColor(GREEN), text.getLineBackground(1));
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
	assertEquals(":1a:", getColor(YELLOW), text.getSelectionBackground());
	text.setSelectionBackground(null);
	assertEquals(":1b:", text.getDisplay().getSystemColor(SWT.COLOR_LIST_SELECTION), text.getSelectionBackground());
}

@Test
public void test_setSelectionForegroundLorg_eclipse_swt_graphics_Color(){
	text.setSelectionForeground(getColor(RED));
	assertEquals(":1a:", getColor(RED), text.getSelectionForeground());
	text.setSelectionForeground(null);
	assertEquals(":1b:", text.getDisplay().getSystemColor(SWT.COLOR_LIST_SELECTION_TEXT), text.getSelectionForeground());
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
	assertEquals(":1:", 4, styles.length);
	assertEquals(":1:", getStyle(0,48,RED,YELLOW), styles[0]);
	assertEquals(":1:", getStyle(48,5,YELLOW,RED), styles[1]);
	assertEquals(":1:", getStyle(58,10,BLUE,CYAN), styles[2]);
	assertEquals(":1:", getStyle(68,10,GREEN,PURPLE), styles[3]);

	// Overlap middle of one style - partial
	text.setStyleRanges(defaultStyles());
	text.setStyleRange(getStyle(10,10,YELLOW,RED));
	styles = text.getStyleRanges();
	assertEquals(":2:", 5, styles.length);
	assertEquals(":2:", getStyle(0,10,RED,YELLOW), styles[0]);
	assertEquals(":2:", getStyle(10,10,YELLOW,RED), styles[1]);
	assertEquals(":2:", getStyle(20,28,RED,YELLOW), styles[2]);
	assertEquals(":2:", getStyle(58,10,BLUE,CYAN), styles[3]);
	assertEquals(":2:", getStyle(68,10,GREEN,PURPLE), styles[4]);
	text.setStyleRange(null);
	styles = text.getStyleRanges();
	assertEquals(":2:", 0, styles.length);

	// Overlap middle of one style - full
	text.setStyleRanges(defaultStyles());
	text.setStyleRange(getStyle(58,10,YELLOW,RED));
	styles = text.getStyleRanges();
	assertEquals(":3:", 3, styles.length);
	assertEquals(":3:", getStyle(0,48,RED,YELLOW), styles[0]);
	assertEquals(":3:", getStyle(58,10,YELLOW,RED), styles[1]);
	assertEquals(":3:", getStyle(68,10,GREEN,PURPLE), styles[2]);

	// Overlap end of one style
	text.setStyleRanges(defaultStyles());
	text.setStyleRange(getStyle(38,15,YELLOW,RED));
	styles = text.getStyleRanges();

	assertEquals(":4:", 4, styles.length);
	assertEquals(":4:", getStyle(0,38,RED,YELLOW), styles[0]);
	assertEquals(":4:", getStyle(38,15,YELLOW,RED), styles[1]);
	assertEquals(":4:", getStyle(58,10,BLUE,CYAN), styles[2]);
	assertEquals(":4:", getStyle(68,10,GREEN,PURPLE), styles[3]);

	// Overlap beginning of one style
	text.setStyleRanges(defaultStyles());
	text.setStyleRange(getStyle(50,10,YELLOW,RED));
	styles = text.getStyleRanges();
	assertEquals(":5:", 4, styles.length);
	assertEquals(":5:", getStyle(0,48,RED,YELLOW), styles[0]);
	assertEquals(":5:", getStyle(50,10,YELLOW,RED), styles[1]);
	assertEquals(":5:", getStyle(60,8,BLUE,CYAN), styles[2]);
	assertEquals(":5:", getStyle(68,10,GREEN,PURPLE), styles[3]);

	// Overlap complete style
	text.setStyleRanges(defaultStyles());
	text.setStyleRange(getStyle(48,20,YELLOW,RED));
	styles = text.getStyleRanges();
	assertEquals(":6:", 3, styles.length);
	assertEquals(":6:", getStyle(0,48,RED,YELLOW), styles[0]);
	assertEquals(":6:", getStyle(48,20,YELLOW,RED), styles[1]);
	assertEquals(":6:", getStyle(68,10,GREEN,PURPLE), styles[2]);

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
	assertEquals(":1:", 3, styles.length);
	assertEquals(":1:", getStyle(0,58,RED,YELLOW), styles[0]);
	assertEquals(":1:", getStyle(58,10,BLUE,CYAN), styles[1]);
	assertEquals(":1:", getStyle(68,10,GREEN,PURPLE), styles[2]);

	text.setText(textString);
	text.setStyleRange(getStyle(0,10,RED,YELLOW));
	text.setStyleRange(getStyle(15,10,BLUE,CYAN));
	text.setStyleRange(getStyle(25,10,GREEN,PURPLE));
	// should be merged with style after it
	text.setStyleRange(getStyle(11,4,BLUE,CYAN));
	styles = text.getStyleRanges();
	assertEquals(":2:", 3, styles.length);
	assertEquals(":2:", getStyle(0,10,RED,YELLOW), styles[0]);
	assertEquals(":2:", getStyle(11,14,BLUE,CYAN), styles[1]);
	assertEquals(":2:", getStyle(25,10,GREEN,PURPLE), styles[2]);

	text.setText(textString);
	text.setStyleRange(getStyle(0,10,RED,YELLOW));
	text.setStyleRange(getStyle(15,10,BLUE,CYAN));
	text.setStyleRange(getStyle(25,10,GREEN,PURPLE));
	// should be merged with style after it
	text.setStyleRange(getStyle(5,15,BLUE,CYAN));
	styles = text.getStyleRanges();
	assertEquals(":3:", 3, styles.length);
	assertEquals(":3:", getStyle(0,5,RED,YELLOW), styles[0]);
	assertEquals(":3:", getStyle(5,20,BLUE,CYAN), styles[1]);
	assertEquals(":3:", getStyle(25,10,GREEN,PURPLE), styles[2]);

	text.setText("01234567890123456789");
	text.setStyleRange(getStyle(0,10,RED,YELLOW));
	text.setStyleRange(getStyle(10,10,BLUE,CYAN));
	text.setStyleRange(getStyle(5,3,RED,YELLOW));
	text.setStyleRange(getStyle(12,5,BLUE,CYAN));
	styles = text.getStyleRanges();
	assertEquals(":4:", 2 , styles.length);
	assertEquals(":4:", getStyle(0,10,RED,YELLOW), styles[0]);
	assertEquals(":4:", getStyle(10,10,BLUE,CYAN), styles[1]);

	text.setText("0123456789012345");
	text.setStyleRange(getStyle(0,5,RED,YELLOW));
	text.setStyleRange(getStyle(5,5,BLUE,CYAN));
	text.setStyleRange(getStyle(10,5,GREEN,PURPLE));
	text.setStyleRange(getStyle(5,5,RED,YELLOW));
	text.setStyleRange(getStyle(10,5,RED,YELLOW));
	styles = text.getStyleRanges();
	assertEquals(":5:", 1, styles.length);
	assertEquals(":5:", getStyle(0,15,RED,YELLOW), styles[0]);

	text.setText("012345678901234");
	text.setStyleRange(getStyle(0,5,RED,YELLOW));
	text.setStyleRange(getStyle(10,5,BLUE,CYAN));
	// should be merged
	text.setStyleRange(getStyle(5,7,BLUE,CYAN));
	styles = text.getStyleRanges();
	assertEquals(":6:", 2, styles.length);
	assertEquals(":6:", getStyle(0,5,RED,YELLOW), styles[0]);
	assertEquals(":6:", getStyle(5,10,BLUE,CYAN), styles[1]);

	text.setText("123 456 789");
	text.setStyleRange(getStyle(4,3,BLUE,null));
	text.setStyleRange(getStyle(8,3,RED,null));
	text.setStyleRange(getStyle(5,2,BLUE,null));
	styles = text.getStyleRanges();
	assertEquals(":7:", 2, styles.length);
	assertEquals(":7:", getStyle(4,3,BLUE,null), styles[0]);
	assertEquals(":7:", getStyle(8,3,RED,null), styles[1]);

	text.setText("123 456 789");
	text.setStyleRange(getStyle(4,3,BLUE,null));
	text.setStyleRange(getStyle(8,3,RED,null));
	text.setStyleRange(getStyle(7,4,BLUE,null));
	styles = text.getStyleRanges();
	assertEquals(":8:", 1, styles.length);
	assertEquals(":8:", getStyle(4,7,BLUE,null), styles[0]);

	text.setText("123 456 789 ABC DEF");
	text.setStyleRange(getStyle(0,4,BLUE,null));
	text.setStyleRange(getStyle(4,4,RED,null));
	text.setStyleRange(getStyle(8,4,BLUE,null));
	text.setStyleRange(getStyle(12,4,RED,null));
	text.setStyleRange(getStyle(16,3,BLUE,null));
	text.setStyleRange(getStyle(5,14,RED,null));
	styles = text.getStyleRanges();
	assertEquals(":9:", 2, styles.length);
	assertEquals(":9:", getStyle(0,4,BLUE,null), styles[0]);
	assertEquals(":9:", getStyle(4,15,RED,null), styles[1]);

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
	assertEquals(":1:", 3, styles.length);
	assertEquals(":1:", getStyle(0,38,RED,YELLOW), styles[0]);
	assertEquals(":1:", getStyle(58,10,BLUE,CYAN), styles[1]);
	assertEquals(":1:", getStyle(68,10,GREEN,PURPLE), styles[2]);

	text.setText(textString);
	int length = textString.length();
	text.setStyleRange(getStyle(0,48,RED,YELLOW));
	text.setStyleRange(getStyle(48,20,BLUE,CYAN));
	text.setStyleRange(getStyle(68,10,GREEN,PURPLE));
	text.setStyleRange(getStyle(0,length,null,null));
	styles = text.getStyleRanges();
	assertEquals(":2:", 0, styles.length);

	text.setText("01234567890123456789");
	text.setStyleRange(getStyle(0,3,RED,YELLOW));
	text.setStyleRange(getStyle(5,3,BLUE,CYAN));
	text.setStyleRange(getStyle(9,8,GREEN,PURPLE));
	text.setStyleRange(getStyle(0,10,GREEN,PURPLE));
	styles = text.getStyleRanges();
	assertEquals(":3:", 1, styles.length);
	assertEquals(":3:", getStyle(0,17,GREEN,PURPLE), styles[0]);

	text.setText("0123456789012345");
	text.setStyleRange(getStyle(0,5,RED,YELLOW));
	text.setStyleRange(getStyle(5,5,BLUE,CYAN));
	text.setStyleRange(getStyle(10,5,GREEN,PURPLE));
	text.setStyleRange(getStyle(7,9,RED,YELLOW));
	styles = text.getStyleRanges();
	assertEquals(":4:", 3, styles.length);
	assertEquals(":4:", getStyle(0,5,RED,YELLOW), styles[0]);
	assertEquals(":4:", getStyle(5,2,BLUE,CYAN), styles[1]);
	assertEquals(":4:", getStyle(7,9,RED,YELLOW), styles[2]);

	text.setText("012345678901234");
	text.setStyleRange(getStyle(0,5,RED,YELLOW));
	text.setStyleRange(getStyle(10,5,BLUE,CYAN));
	text.setStyleRange(getStyle(3,10,GREEN,PURPLE));
	styles = text.getStyleRanges();
	assertEquals(":5:", 3, styles.length);
	assertEquals(":5:", getStyle(0,3,RED,YELLOW), styles[0]);
	assertEquals(":5:", getStyle(3,10,GREEN,PURPLE), styles[1]);
	assertEquals(":5:", getStyle(13,2,BLUE,CYAN), styles[2]);

	text.setText("redgreenblueyellowcyanpurple");
	text.setStyleRange(getStyle(0,3,RED,null));
	text.setStyleRange(getStyle(3,5,GREEN,null));
	text.setStyleRange(getStyle(8,4,BLUE,null));
	text.setStyleRange(getStyle(12,6,YELLOW,null));
	text.setStyleRange(getStyle(18,4,CYAN,null));
	text.setStyleRange(getStyle(22,6,PURPLE,null));
	text.setStyleRange(getStyle(8,14,null,RED));
	styles = text.getStyleRanges();
	assertEquals(":6:", 4, styles.length);
	assertEquals(":6:", getStyle(0,3,RED,null), styles[0]);
	assertEquals(":6:", getStyle(3,5,GREEN,null), styles[1]);
	assertEquals(":6:", getStyle(8,14,null,RED), styles[2]);
	assertEquals(":6:", getStyle(22,6,PURPLE,null), styles[3]);


	text.setText("redgreenblueyellowcyanpurple");
	text.setStyleRange(getStyle(0,3,RED,null));
	text.setStyleRange(getStyle(3,5,GREEN,null));
	text.setStyleRange(getStyle(8,4,BLUE,null));
	text.setStyleRange(getStyle(12,6,YELLOW,null));
	text.setStyleRange(getStyle(18,4,CYAN,null));
	text.setStyleRange(getStyle(22,6,PURPLE,null));
	text.setStyleRange(getStyle(0,28,null,null));
	styles = text.getStyleRanges();
	assertEquals(":7:", 0, styles.length);


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
	assertEquals(":9:", 2, styles.length);
	assertEquals(":9:", getStyle(4,6,BLUE,null), styles[0]);
	assertEquals(":9:", getStyle(10,1,RED,null), styles[1]);


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
	assertEquals(":1:", 3, styles.length);
	assertEquals(":1:", getStyle(0,58,RED,YELLOW), styles[0]);
	assertEquals(":1:", getStyle(58,10,BLUE,CYAN), styles[1]);
	assertEquals(":1:", getStyle(68,10,GREEN,PURPLE), styles[2]);


	text.setText(textString);
	text.setStyleRange(getStyle(0,10,RED,YELLOW));
	text.setStyleRange(getStyle(15,10,BLUE,CYAN));
	text.setStyleRange(getStyle(25,10,GREEN,PURPLE));
	// should be merged with style after it
	text.setStyleRange(getStyle(11,4,BLUE,CYAN));
	styles = text.getStyleRanges();
	assertEquals(":2:", 3, styles.length);
	assertEquals(":2:", getStyle(0,10,RED,YELLOW), styles[0]);
	assertEquals(":2:", getStyle(11,14,BLUE,CYAN), styles[1]);
	assertEquals(":2:", getStyle(25,10,GREEN,PURPLE), styles[2]);

	text.setText(textString);
	text.setStyleRange(getStyle(0,10,RED,YELLOW));
	text.setStyleRange(getStyle(15,10,BLUE,CYAN));
	text.setStyleRange(getStyle(25,10,GREEN,PURPLE));
	// should be merged with style after it
	text.setStyleRange(getStyle(5,15,BLUE,CYAN));
	styles = text.getStyleRanges();
	assertEquals(":3:", 3, styles.length);
	assertEquals(":3:", getStyle(0,5,RED,YELLOW), styles[0]);
	assertEquals(":3:", getStyle(5,20,BLUE,CYAN), styles[1]);
	assertEquals(":3:", getStyle(25,10,GREEN,PURPLE), styles[2]);



	text.setText("01234567890123456789");
	text.setStyleRange(getStyle(0,10,RED,YELLOW));
	text.setStyleRange(getStyle(10,10,BLUE,CYAN));
	text.setStyleRange(getStyle(5,3,RED,YELLOW));
	text.setStyleRange(getStyle(12,5,BLUE,CYAN));
	styles = text.getStyleRanges();
	assertEquals(":4:", 2, styles.length);
	assertEquals(":4:", getStyle(0,10,RED,YELLOW), styles[0]);
	assertEquals(":4:", getStyle(10,10,BLUE,CYAN), styles[1]);

	text.setText("0123456789012345");
	text.setStyleRange(getStyle(0,5,RED,YELLOW));
	text.setStyleRange(getStyle(5,5,BLUE,CYAN));
	text.setStyleRange(getStyle(10,5,GREEN,PURPLE));
	text.setStyleRange(getStyle(5,5,RED,YELLOW));
	text.setStyleRange(getStyle(10,5,RED,YELLOW));
	styles = text.getStyleRanges();
	assertEquals(":5:", 1, styles.length);
	assertEquals(":5:", getStyle(0,15,RED,YELLOW), styles[0]);

	text.setText("012345678901234");
	text.setStyleRange(getStyle(0,5,RED,YELLOW));
	text.setStyleRange(getStyle(10,5,BLUE,CYAN));
	// should be merged
	text.setStyleRange(getStyle(5,7,BLUE,CYAN));
	styles = text.getStyleRanges();
	assertEquals(":6:", 2, styles.length);
	assertEquals(":6:", getStyle(0,5,RED,YELLOW), styles[0]);
	assertEquals(":6:", getStyle(5,10,BLUE,CYAN), styles[1]);


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
	assertEquals(":1:", 4, styles.length);
	assertEquals(":1:", getStyle(0,38,RED,YELLOW), styles[0]);
	assertEquals(":1:", getStyle(38,25,YELLOW,RED), styles[1]);
	assertEquals(":1:", getStyle(63,5,BLUE,CYAN), styles[2]);
	assertEquals(":1:", getStyle(68,10,GREEN,PURPLE), styles[3]);
	text.setStyleRanges(defaultStyles());
	text.setStyleRange(getStyle(63,10,YELLOW,RED));
	styles = text.getStyleRanges();
	assertEquals(":1:", 4, styles.length);
	assertEquals(":1:", getStyle(0,48,RED,YELLOW), styles[0]);
	assertEquals(":1:", getStyle(58,5,BLUE,CYAN), styles[1]);
	assertEquals(":1:", getStyle(63,10,YELLOW,RED), styles[2]);
	assertEquals(":1:", getStyle(73,5,GREEN,PURPLE), styles[3]);


	// Complete overlap
	text.setStyleRanges(defaultStyles());
	text.setStyleRange(getStyle(0,78,YELLOW,RED));
	styles = text.getStyleRanges();
	assertEquals(":2:", 1, styles.length);
	assertEquals(":2:", getStyle(0,78,YELLOW,RED), styles[0]);
	text.setStyleRanges(defaultStyles());
	text.setStyleRange(getStyle(0,68,YELLOW,RED));
	styles = text.getStyleRanges();
	assertEquals(":2:", 2, styles.length);
	assertEquals(":2:", getStyle(0,68,YELLOW,RED), styles[0]);
	assertEquals(":2:", getStyle(68,10,GREEN,PURPLE), styles[1]);
	text.setStyleRanges(defaultStyles());
	text.setStyleRange(getStyle(58,20,YELLOW,RED));
	styles = text.getStyleRanges();
	assertEquals(":2:", 2, styles.length);
	assertEquals(":2:", getStyle(0,48,RED,YELLOW), styles[0]);
	assertEquals(":2:", getStyle(58,20,YELLOW,RED), styles[1]);


	// 1-N complete, beginning
	text.setText("012345678901234567890123456789");
	text.setStyleRanges(
		new StyleRange[] {getStyle(0,5,RED,RED), getStyle(5,5,YELLOW,YELLOW),
			getStyle(10,5,CYAN,CYAN), getStyle(15,5,BLUE,BLUE),
			getStyle(20,5,GREEN,GREEN), getStyle(25,5,PURPLE,PURPLE)}
	);
	text.setStyleRange(getStyle(5,23,YELLOW,RED));
	styles = text.getStyleRanges();
	assertEquals(":3:", 3, styles.length);
	assertEquals(":3:", getStyle(0,5,RED,RED), styles[0]);
	assertEquals(":3:", getStyle(5,23,YELLOW,RED), styles[1]);
	assertEquals(":3:", getStyle(28,2,PURPLE,PURPLE), styles[2]);

	// end, 1-N complete, beginning
	text.setStyleRanges(
		new StyleRange[] {getStyle(0,5,RED,RED), getStyle(5,5,YELLOW,YELLOW),
			getStyle(10,5,CYAN,CYAN), getStyle(15,5,BLUE,BLUE),
			getStyle(20,5,GREEN,GREEN), getStyle(25,5,PURPLE,PURPLE)}
	);
	text.setStyleRange(getStyle(13,12,YELLOW,RED));
	styles = text.getStyleRanges();
	assertEquals(":3:", 5, styles.length);
	assertEquals(":3:", getStyle(0,5,RED,RED), styles[0]);
	assertEquals(":3:", getStyle(5,5,YELLOW,YELLOW), styles[1]);
	assertEquals(":3:", getStyle(10,3,CYAN,CYAN), styles[2]);
	assertEquals(":3:", getStyle(13,12,YELLOW,RED), styles[3]);
	assertEquals(":3:", getStyle(25,5,PURPLE,PURPLE), styles[4]);


	text.setText("x/");
	text.setStyleRange(getStyle(0,2,YELLOW,null));
	styles = text.getStyleRanges();
	assertEquals(":4:", 1, styles.length);
	assertEquals(":4:", getStyle(0,2,YELLOW,null), styles[0]);
	text.replaceTextRange(2,0,"/");
	styles = text.getStyleRanges();
	assertEquals(":4:", 1, styles.length);
	assertEquals(":4:", getStyle(0,2,YELLOW,null), styles[0]);
	text.setStyleRange(getStyle(0,1,YELLOW,null));
	assertEquals(":4:", 1, styles.length);
	assertEquals(":4:", getStyle(0,2,YELLOW,null), styles[0]);
	text.setStyleRange(getStyle(1,2,RED,null));
	styles = text.getStyleRanges();
	assertEquals(":4:", 2, styles.length);
	assertEquals(":4:", getStyle(0,1,YELLOW,null), styles[0]);
	assertEquals(":4:", getStyle(1,2,RED,null), styles[1]);


	text.setText("xxx/");
	text.setStyleRange(getStyle(0,2,RED,null));
	text.setStyleRange(getStyle(2,2,YELLOW,null));
	styles = text.getStyleRanges();
	assertEquals(":4a:", 2, styles.length);
	assertEquals(":4a:", getStyle(0,2,RED,null), styles[0]);
	assertEquals(":4a:", getStyle(2,2,YELLOW,null), styles[1]);
	text.replaceTextRange(4,0,"/");
	styles = text.getStyleRanges();
	assertEquals(":4a:", 2, styles.length);
	assertEquals(":4a:", getStyle(0,2,RED,null), styles[0]);
	assertEquals(":4a:", getStyle(2,2,YELLOW,null), styles[1]);
	text.setStyleRange(getStyle(2,1,YELLOW,null));
	assertEquals(":4a:", 2, styles.length);
	assertEquals(":4a:", getStyle(0,2,RED,null), styles[0]);
	assertEquals(":4a:", getStyle(2,2,YELLOW,null), styles[1]);
	text.setStyleRange(getStyle(3,2,RED,null));
	styles = text.getStyleRanges();
	assertEquals(":4a:", 3, styles.length);
	assertEquals(":4a:", getStyle(0,2,RED,null), styles[0]);
	assertEquals(":4a:", getStyle(2,1,YELLOW,null), styles[1]);
	assertEquals(":4a:", getStyle(3,2,RED,null), styles[2]);


	text.setText("xxx/");
	text.setStyleRange(getStyle(0,2,RED,null));
	text.setStyleRange(getStyle(2,2,YELLOW,null));
	text.replaceTextRange(4,0,"/");
	styles = text.getStyleRanges();
	text.setStyleRange(getStyle(2,1,YELLOW,null));
	text.setStyleRange(getStyle(2,3,RED,null));
	styles = text.getStyleRanges();
	assertEquals(":4b:", 1, styles.length);
	assertEquals(":4b:", getStyle(0,5,RED,null), styles[0]);


	text.setText("xxx/");
	text.setStyleRange(getStyle(0,2,RED,null));
	text.setStyleRange(getStyle(2,2,YELLOW,null));
	text.replaceTextRange(4,0,"/");
	styles = text.getStyleRanges();
	text.setStyleRange(getStyle(2,1,YELLOW,null));
	text.setStyleRange(getStyle(1,4,YELLOW,null));
	styles = text.getStyleRanges();
	assertEquals(":4c:", 2, styles.length);
	assertEquals(":4c:", getStyle(0,1,RED,null), styles[0]);
	assertEquals(":4c:", getStyle(1,4,YELLOW,null), styles[1]);


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
		assertEquals(":5:", SWT.BOLD, style.fontStyle);
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
		assertEquals(":5a:", SWT.BOLD, style.fontStyle);
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
	assertEquals(":1a:", 1, styles.length);
	assertEquals(":1a:", getStyle(5,4,YELLOW,RED), styles[0]);
	text.setText("1234 1234 1234");
	text.setStyleRange(getStyle(5,4,YELLOW,RED));
	text.setStyleRange(getStyle(7,2,YELLOW,RED));
	styles = text.getStyleRanges();
	assertEquals(":2a:", 1, styles.length);
	assertEquals(":2a:", getStyle(5,4,YELLOW,RED), styles[0]);
	text.setText("1234 1234 1234");
	text.setStyleRange(getStyle(5,4,YELLOW,RED));
	text.setStyleRange(getStyle(6,2,YELLOW,RED));
	styles = text.getStyleRanges();
	assertEquals(":3a:", 1, styles.length);
	assertEquals(":3a:", getStyle(5,4,YELLOW,RED), styles[0]);
	text.setText("1234 1234 1234");
	text.setStyleRange(getStyle(5,4,YELLOW,RED));
	text.setStyleRange(getStyle(3,4,YELLOW,RED));
	styles = text.getStyleRanges();
	assertEquals(":4a:", 1, styles.length);
	assertEquals(":4a:", getStyle(3,6,YELLOW,RED), styles[0]);
	text.setText("1234 1234 1234");
	text.setStyleRange(getStyle(5,4,YELLOW,RED));
	text.setStyleRange(getStyle(7,4,YELLOW,RED));
	styles = text.getStyleRanges();
	assertEquals(":5a:", 1, styles.length);
	assertEquals(":4a:", getStyle(5,6,YELLOW,RED), styles[0]);
	text.setText("1234 1234 1234");
	text.setStyleRange(getStyle(5,4,YELLOW,RED));
	text.setStyleRange(getStyle(5,4,YELLOW,RED));
	styles = text.getStyleRanges();
	assertEquals(":6a:", 1, styles.length);
	assertEquals(":6a:", getStyle(5,4,YELLOW,RED), styles[0]);
	text.setText("1234 1234 1234");
	text.setStyleRange(getStyle(5,4,YELLOW,RED));
	text.setStyleRange(getStyle(3,10,YELLOW,RED));
	styles = text.getStyleRanges();
	assertEquals(":7a:", 1, styles.length);
	assertEquals(":7a:", getStyle(3,10,YELLOW,RED), styles[0]);


	text.setText("1234 1234 1234");
	text.setStyleRange(getStyle(5,4,YELLOW,RED));
	text.setStyleRange(getStyle(5,2,BLUE,CYAN));
	styles = text.getStyleRanges();
	assertEquals(":1b:", 2, styles.length);
	assertEquals(":1b:", getStyle(5,2,BLUE,CYAN), styles[0]);
	assertEquals(":1b:", getStyle(7,2,YELLOW,RED), styles[1]);
	text.setText("1234 1234 1234");
	text.setStyleRange(getStyle(5,4,YELLOW,RED));
	text.setStyleRange(getStyle(7,2,BLUE,CYAN));
	styles = text.getStyleRanges();
	assertEquals(":2b:", 2, styles.length);
	assertEquals(":2b:", getStyle(5,2,YELLOW,RED), styles[0]);
	assertEquals(":2b:", getStyle(7,2,BLUE,CYAN), styles[1]);
	text.setText("1234 1234 1234");
	text.setStyleRange(getStyle(5,4,YELLOW,RED));
	text.setStyleRange(getStyle(6,2,BLUE,CYAN));
	styles = text.getStyleRanges();
	assertEquals(":3b:", 3, styles.length);
	assertEquals(":3b:", getStyle(5,1,YELLOW,RED), styles[0]);
	assertEquals(":3b:", getStyle(6,2,BLUE,CYAN), styles[1]);
	assertEquals(":3b:", getStyle(8,1,YELLOW,RED), styles[2]);
	text.setText("1234 1234 1234");
	text.setStyleRange(getStyle(5,4,YELLOW,RED));
	text.setStyleRange(getStyle(3,4,BLUE,CYAN));
	styles = text.getStyleRanges();
	assertEquals(":4b:", 2, styles.length);
	assertEquals(":4b:", getStyle(3,4,BLUE,CYAN), styles[0]);
	assertEquals(":4b:", getStyle(7,2,YELLOW,RED), styles[1]);
	text.setText("1234 1234 1234");
	text.setStyleRange(getStyle(5,4,YELLOW,RED));
	text.setStyleRange(getStyle(7,4,BLUE,CYAN));
	styles = text.getStyleRanges();
	assertEquals(":5b:", 2, styles.length);
	assertEquals(":5b:", getStyle(5,2,YELLOW,RED), styles[0]);
	assertEquals(":5b:", getStyle(7,4,BLUE,CYAN), styles[1]);
	text.setText("1234 1234 1234");
	text.setStyleRange(getStyle(5,4,YELLOW,RED));
	text.setStyleRange(getStyle(5,4,BLUE,CYAN));
	styles = text.getStyleRanges();
	assertEquals(":6b:", 1, styles.length);
	assertEquals(":6b:", getStyle(5,4,BLUE,CYAN), styles[0]);
	text.setText("1234 1234 1234");
	text.setStyleRange(getStyle(5,4,YELLOW,RED));
	text.setStyleRange(getStyle(3,10,BLUE,CYAN));
	styles = text.getStyleRanges();
	assertEquals(":7b:", 1, styles.length);
	assertEquals(":7b:", getStyle(3,10,BLUE,CYAN), styles[0]);


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
	assertEquals(":1:", 1, styles.length);
	assertEquals(":1:", getStyle(10,2,YELLOW,RED), styles[0]);
	text.setText(testString);
	text.setStyleRange(getStyle(10,4,YELLOW,RED));
	text.replaceTextRange(10,2,"");
	styles = text.getStyleRanges();
	assertEquals(":2:", 1, styles.length);
	assertEquals(":2:", getStyle(10,2,YELLOW,RED), styles[0]);


	text.setText(testString);
	text.setStyleRange(getStyle(10,4,YELLOW,RED));
	text.replaceTextRange(12,4,"");
	styles = text.getStyleRanges();
	assertEquals(":3:", 1, styles.length);
	assertEquals(":3:", getStyle(10,2,YELLOW,RED), styles[0]);
	text.setText(testString);
	text.setStyleRange(getStyle(10,4,YELLOW,RED));
	text.replaceTextRange(8,4,"");
	styles = text.getStyleRanges();
	assertEquals(":4:", 1, styles.length);
	assertEquals(":4:", getStyle(8,2,YELLOW,RED), styles[0]);


	text.setText(testString);
	text.setStyleRange(getStyle(10,4,YELLOW,RED));
	text.replaceTextRange(8,6,"");
	styles = text.getStyleRanges();
	assertEquals(":5:", 0, styles.length);
	text.setText(testString);
	text.setStyleRange(getStyle(10,4,YELLOW,RED));
	text.replaceTextRange(10,6,"");
	styles = text.getStyleRanges();
	assertEquals(":6:", 0, styles.length);
	text.setText(testString);
	text.setStyleRange(getStyle(10,4,YELLOW,RED));
	text.replaceTextRange(8,12,"");
	styles = text.getStyleRanges();
	assertEquals(":7:", 0, styles.length);

	//			 			"012345678901234567890123"
	//	String testString=	"1234 1234 1234 1234 1234";

	text.setText(testString);
	text.setStyleRange(getStyle(5,4,BLUE,CYAN));
	text.setStyleRange(getStyle(10,4,YELLOW,RED));
	text.replaceTextRange(5,7,"");
	styles = text.getStyleRanges();
	assertEquals(":8:", 1, styles.length);
	assertEquals(":8:", getStyle(5,2,YELLOW,RED), styles[0]);
	text.setText(testString);
	text.setStyleRange(getStyle(5,4,BLUE,CYAN));
	text.setStyleRange(getStyle(10,4,YELLOW,RED));
	text.replaceTextRange(7,7,"");
	styles = text.getStyleRanges();
	assertEquals(":9:", 1, styles.length);
	assertEquals(":9:", getStyle(5,2,BLUE,CYAN), styles[0]);


	text.setText(testString);
	text.setStyleRange(getStyle(5,4,BLUE,CYAN));
	text.setStyleRange(getStyle(10,4,YELLOW,RED));
	text.replaceTextRange(2,10,"");
	styles = text.getStyleRanges();
	assertEquals(":10:", 1, styles.length);
	assertEquals(":10:", getStyle(2,2,YELLOW,RED), styles[0]);
	text.setText(testString);
	text.setStyleRange(getStyle(5,4,BLUE,CYAN));
	text.setStyleRange(getStyle(10,4,YELLOW,RED));
	text.replaceTextRange(7,9,"");
	styles = text.getStyleRanges();
	assertEquals(":11:", 1, styles.length);
	assertEquals(":11:", getStyle(5,2,BLUE,CYAN), styles[0]);


	text.setText(testString);
	text.setStyleRange(getStyle(5,4,BLUE,CYAN));
	text.setStyleRange(getStyle(10,4,YELLOW,RED));
	text.replaceTextRange(5,9,"");
	styles = text.getStyleRanges();
	assertEquals(":12:", 0, styles.length);
	text.setText(testString);
	text.setStyleRange(getStyle(5,4,BLUE,CYAN));
	text.setStyleRange(getStyle(10,4,YELLOW,RED));
	text.replaceTextRange(3,13,"");
	styles = text.getStyleRanges();
	assertEquals(":11:", 0, styles.length);


	//			 			"012345678901234567890123"
	//	String testString=	"1234 1234 1234 1234 1234";

	text.setText(testString);
	text.setStyleRange(getStyle(5,4,BLUE,CYAN));
	text.setStyleRange(getStyle(10,4,YELLOW,RED));
	text.setStyleRange(getStyle(15,4,GREEN,PURPLE));
	text.replaceTextRange(7,12,"");
	styles = text.getStyleRanges();
	assertEquals(":14:", 1, styles.length);
	assertEquals(":14:", getStyle(5,2,BLUE,CYAN), styles[0]);
	text.setText(testString);
	text.setStyleRange(getStyle(5,4,BLUE,CYAN));
	text.setStyleRange(getStyle(10,4,YELLOW,RED));
	text.setStyleRange(getStyle(15,4,GREEN,PURPLE));
	text.replaceTextRange(5,12,"");
	styles = text.getStyleRanges();
	assertEquals(":15:", 1, styles.length);
	assertEquals(":15:", getStyle(5,2,GREEN,PURPLE), styles[0]);


	text.setText(testString);
	text.setStyleRange(getStyle(5,4,BLUE,CYAN));
	text.setStyleRange(getStyle(10,4,YELLOW,RED));
	text.setStyleRange(getStyle(15,4,GREEN,PURPLE));
	text.replaceTextRange(9,10,"");
	styles = text.getStyleRanges();
	assertEquals(":16:", 1, styles.length);
	assertEquals(":16:", getStyle(5,4,BLUE,CYAN), styles[0]);


	// reset the environment
	text.dispose();
	text = new StyledText(shell, SWT.NULL);
	setWidget(text);

	StyleRange style1 = getStyle(0,0,null,null);


	StyleRange style2 = getStyle(0,0,RED,YELLOW);


	assertNotEquals(":2:", style2, style1);
	assertFalse(":1:", style1.similarTo(style2));


	assertNotEquals(":2:", style1, style2);


	assertFalse(":1:", style2.similarTo(style1));



	style1 = getStyle(0,10,RED,YELLOW);
	style2 = getStyle(11,5,RED,YELLOW);


	assertNotEquals(":2:", style2, style1);


	assertNotEquals(":2:", style1, style2);
	assertTrue(":2:", style1.similarTo(style2));


	assertTrue(":2:", style2.similarTo(style1));


}

@Test
public void test_setStyleRanges$Lorg_eclipse_swt_custom_StyleRange() {
	StyleRange[] ranges = new StyleRange[] {
		new StyleRange(0, 1, getColor(RED), null),
		new StyleRange(2, 1, getColor(RED), null)};

	text.setText("Line0\r\n");
	assertThrows(IllegalArgumentException.class, () -> text.setStyleRanges(null));

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
	assertEquals(":a:", 1, text.getTabs());

	text.setTabs(8);
	assertEquals(":b:", 8, text.getTabs());
	text.setText("Line\t1\r\n");
	text.setTabs(7);
	assertEquals(":c:", 7, text.getTabs());
}

@Test
public void test_setTextLjava_lang_String(){

	text.setText("");
	assertEquals("", text.getText());
	text.setText("01234567890");
	assertEquals("01234567890", text.getText());

	assertThrows(IllegalArgumentException.class, () -> text.setText(null));

	assertEquals("01234567890", text.getText());
	text.setText("");
	assertEquals("", text.getText());
}

@Test
public void test_setTextLimitI(){
	text.setTextLimit(10);
	assertEquals(":a:", 10, text.getTextLimit());

	text.setTextLimit(-1);
	assertEquals(":b:", -1, text.getTextLimit());

	assertThrows(IllegalArgumentException.class, () -> text.setTextLimit(0));
}

@Test
public void test_setTopIndexI(){
	text.setTopIndex(-1);
	assertEquals(":a:", 0 , text.getTopIndex());
	text.setTopIndex(1);
	assertEquals(":b:", 0 , text.getTopIndex());

	text.setText("Line0\r\nLine0a\r\n");

	text.setTopIndex(-2);
	assertEquals(":c:", 0, text.getTopIndex());
	text.setTopIndex(-1);
	assertEquals(":d:", 0, text.getTopIndex());
	text.setTopIndex(1);
	assertEquals(":e:", 1, text.getTopIndex());
	text.setTopIndex(2);
	assertEquals(":f:", 2, text.getTopIndex());
	text.setTopIndex(0);
	assertEquals(":g:", 0, text.getTopIndex());
	text.setTopIndex(3);
	assertEquals(":h:", 2, text.getTopIndex());

	text.setText("");
	text.setTopIndex(2);
	assertEquals(":i:", 0 , text.getTopIndex());
}

@Test
public void test_setTopPixelI(){
	int lineHeight = text.getLineHeight();

	text.setTopPixel(-1);
	assertEquals(":a:", 0, text.getTopPixel());
	text.setTopPixel(1);
	assertEquals(":b:", 0, text.getTopPixel());

	text.setText("Line0\r\n");

	text.setTopPixel(-2);
	assertEquals(":c:", 0, text.getTopPixel());
	text.setTopPixel(-1);
	assertEquals(":d:", 0, text.getTopPixel());
	text.setTopPixel(1);
	assertEquals(":e:", 1, text.getTopPixel());
	text.setTopPixel(2 * lineHeight);
	assertEquals(":f:", 2 * lineHeight, text.getTopPixel());
	text.setTopPixel(0);
	assertEquals(":g:", 0, text.getTopPixel());
	text.setTopPixel(3 * lineHeight);
	assertEquals(":h:", 2 * lineHeight, text.getTopPixel());

	text.setText("");
	text.setTopPixel(2 * lineHeight);
	assertEquals(":i:", 0, text.getTopPixel());
}

@Test
public void test_verticalIndent_changeRelativeBounds() {
	String _5000lines = IntStream.range(1, 5001).mapToObj(Integer::toString).collect(Collectors.joining("\n"));
	text.setText(_5000lines);
	text.setSize(500, 200);
	text.invokeAction(ST.TEXT_END);
	text.setLineVerticalIndent(text.getContent().getLineCount() - 1, 10);
	text.invokeAction(ST.TEXT_START);
	assertEquals(0, text.getTopPixel());
}

@Test
public void test_verticalIndent_keepsCurrentCaretAndLinePosition() {
	text.dispose();
	text = new StyledText(shell, SWT.V_SCROLL);
	setWidget(text);
	String _50lines = IntStream.range(1, 50).mapToObj(Integer::toString).collect(Collectors.joining("\n"));
	text.setText(_50lines);
	text.setSize(500, 200);
	final int INDENT = 34;
	int line = 30;
	int offset = text.getOffsetAtLine(line);
	text.setSelection(offset);
	text.showSelection(); // move to somewhere in the middle of the widget
	// then pick some line in the middle to test all cases
	line = text.getLineIndex(text.getClientArea().height / 2);
	offset = text.getOffsetAtLine(line);
	text.setSelection(offset);
	int initialTopPixel = text.getTopPixel();
	Point caretLocation = text.getCaret().getLocation();
	Point offsetLocation = text.getLocationAtOffset(offset);
	ScrollBar scrollbar = text.getVerticalBar();
	int scrollMini = scrollbar.getMinimum();
	int scrollMaxi = scrollbar.getMaximum();
	int scrollOffset = scrollbar.getSelection();

	// on active line
	text.setLineVerticalIndent(line, INDENT);
	assertEquals(caretLocation, text.getCaret().getLocation());
	assertEquals(offsetLocation, text.getLocationAtOffset(offset));
	assertEquals("vertical scroll should have been updated", initialTopPixel + INDENT, text.getTopPixel());
	assertEquals(scrollMini, scrollbar.getMinimum());
	assertEquals(scrollMaxi + INDENT, scrollbar.getMaximum());
	assertEquals(scrollOffset + INDENT, scrollbar.getSelection());
	text.setLineVerticalIndent(line, 0);
	assertEquals(caretLocation, text.getCaret().getLocation());
	assertEquals(offsetLocation, text.getLocationAtOffset(offset));
	assertEquals("vertical scroll should have been restored", initialTopPixel, text.getTopPixel());
	assertEquals(scrollMini, scrollbar.getMinimum());
	assertEquals(scrollMaxi, scrollbar.getMaximum());
	assertEquals(scrollOffset, scrollbar.getSelection());

	// above visible area
	text.setLineVerticalIndent(0, INDENT);
	assertEquals(caretLocation, text.getCaret().getLocation());
	assertEquals(offsetLocation, text.getLocationAtOffset(offset));
	assertEquals("vertical scroll should have been updated", initialTopPixel + INDENT, text.getTopPixel());
	assertEquals(scrollMini, scrollbar.getMinimum());
	assertEquals(scrollMaxi + INDENT, scrollbar.getMaximum());
	assertEquals(scrollOffset + INDENT, scrollbar.getSelection());
	text.setLineVerticalIndent(0, 0);
	assertEquals(caretLocation, text.getCaret().getLocation());
	assertEquals(offsetLocation, text.getLocationAtOffset(offset));
	assertEquals("vertical scroll should have been updated", initialTopPixel, text.getTopPixel());
	assertEquals(scrollMini, scrollbar.getMinimum());
	assertEquals(scrollMaxi, scrollbar.getMaximum());
	assertEquals(scrollOffset, scrollbar.getSelection());

	//below visible area
	int nextInvisibleLine = text.getLineIndex(text.getClientArea().height - 1) + 1;
	text.setLineVerticalIndent(nextInvisibleLine, INDENT);
	assertEquals(caretLocation, text.getCaret().getLocation());
	assertEquals(offsetLocation, text.getLocationAtOffset(offset));
	assertEquals("Vertical scroll shouldn't be modified",initialTopPixel, text.getTopPixel());
	text.setLineVerticalIndent(nextInvisibleLine, 0);
	assertEquals(caretLocation, text.getCaret().getLocation());
	assertEquals(offsetLocation, text.getLocationAtOffset(offset));
	assertEquals("Vertical scroll shouldn't be modified",initialTopPixel, text.getTopPixel());

	// above active line, in visible area
	text.setLineVerticalIndent(line - 2, INDENT);
	assertEquals(caretLocation, text.getCaret().getLocation());
	assertEquals(offsetLocation, text.getLocationAtOffset(offset));
	assertEquals("Vertical scroll should have been updated",initialTopPixel + INDENT, text.getTopPixel());
	text.setLineVerticalIndent(line - 2, 0);
	assertEquals(caretLocation, text.getCaret().getLocation());
	assertEquals(offsetLocation, text.getLocationAtOffset(offset));
	assertEquals("Vertical scroll should have been restored",initialTopPixel, text.getTopPixel());

	// below active line, in visible area
	text.setLineVerticalIndent(line + 2, INDENT);
	assertEquals(caretLocation, text.getCaret().getLocation());
	assertEquals(offsetLocation, text.getLocationAtOffset(offset));
	assertEquals("Vertical scroll shouldn't be modified",initialTopPixel, text.getTopPixel());
	text.setLineVerticalIndent(line + 2, 0);
	assertEquals(caretLocation, text.getCaret().getLocation());
	assertEquals(offsetLocation, text.getLocationAtOffset(offset));
	assertEquals("Vertical scroll shouldn't be modified",initialTopPixel, text.getTopPixel());
}

@Test
public void test_notFixedLineHeightDoesntChangeLinePixelIfUnnecessary() {
	text.dispose();
	text = new StyledText(shell, SWT.V_SCROLL);
	setWidget(text);
	String _50lines = IntStream.range(1, 50).mapToObj(Integer::toString).collect(Collectors.joining("\n"));
	text.setText(_50lines);
	text.setSize(500, 200);
	int line = 30;
	int offset = text.getOffsetAtLine(line);
	text.setSelection(offset);
	text.showSelection();
	int firstLinePixel = text.getLinePixel(line);
	text.setWordWrap(true); // make non fixed line height
	assertEquals(firstLinePixel, text.getLinePixel(line));
	text.replaceTextRange(0, 1, "X");
	assertEquals(0, text.getTopIndex());
	assertEquals(0, text.getLinePixel(0));
	assertEquals(0, text.getTopPixel());
}

@Test
public void test_notFixedLineHeightDoesntChangeLinePixelAfterScroll() {
	text.dispose();
	text = new StyledText(shell, SWT.V_SCROLL);
	setWidget(text);
	String _50lines = ("a".repeat(500) + "\n").repeat(50);
	text.setText(_50lines);
	text.setSize(500, 200);
	text.setTopPixel(20);
	text.setTopPixel(0);
	text.setWordWrap(true); // make non fixed line height
	assertEquals(0, text.getLinePixel(0));
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
	// See https://github.com/eclipse-platform/eclipse.platform.swt/issues/294
	assumeFalse("Test doesn't work on Linux docker image in jenkins PR validation build",
			SwtTestUtil.isLinux && Boolean.parseBoolean(System.getenv("PR_VALIDATION_BUILD")));
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
public void test_doubleClickAtStartOfWordSelectsNextWord() {
	text.setText("Hello world" + System.lineSeparator() + "Bye bye");
	Point onW = text.getLocationAtOffset(6);

	Event event = new Event();
	event.button = 1;
	event.count = 2;
	event.x = onW.x + 1;
	event.y = onW.y;
	text.notifyListeners(SWT.MouseDown, event);

	assertEquals("world", text.getSelectionText());
}

@Test
public void test_doubleClickAtEndOfWordSelectsCurrentWord() {
	text.setText("Hello world" + System.lineSeparator() + "Bye bye");
	Point onWs = text.getLocationAtOffset(5);

	Event event = new Event();
	event.button = 1;
	event.count = 2;
	event.x = onWs.x - 1;
	event.y = onWs.y;
	text.notifyListeners(SWT.MouseDown, event);

	assertEquals("Hello", text.getSelectionText());
}

@Test
public void test_doubleClickAtEndOfWordSelectsCurrentWord2() {
	// Same as above, but at line end
	text.setText("Hello world" + System.lineSeparator() + "Bye bye");
	Point onD = text.getLocationAtOffset(10);

	Event event = new Event();
	event.button = 1;
	event.count = 2;
	event.x = onD.x + 1;
	event.y = onD.y;
	text.notifyListeners(SWT.MouseDown, event);

	assertEquals("world", text.getSelectionText());
}

@Test
public void test_doubleClickBetweenWordsSelectsPrevWord() {
	text.setText("Hello world" + System.lineSeparator() + "Bye bye");
	Point onW = text.getLocationAtOffset(6);
	Point onWs = text.getLocationAtOffset(5);

	Event event = new Event();
	event.button = 1;
	event.count = 2;
	event.x = (onWs.x + onW.x) / 2; // Middle of the blank before 'w'
	event.y = onWs.y;
	text.notifyListeners(SWT.MouseDown, event);

	assertEquals("Hello", text.getSelectionText());
}

@Test
public void test_doubleClickJustBeforeNextWordSelectsPrevWord() {
	text.setText("Hello world" + System.lineSeparator() + "Bye bye");
	Point onW = text.getLocationAtOffset(6);

	Event event = new Event();
	event.button = 1;
	event.count = 2;
	event.x = onW.x;
	event.y = onW.y;
	text.notifyListeners(SWT.MouseDown, event);

	assertEquals("Hello", text.getSelectionText());
}

@Test
public void test_doubleClickBeyondEolSelectsLastWord() {
	text.setText("Hello world" + System.lineSeparator() + "Bye bye");
	text.setSize(1000, 1000);
	Point onD = text.getLocationAtOffset(10);

	Event event = new Event();
	event.button = 1;
	event.count = 2;
	event.x = Math.min(onD.x + 100, 999);
	event.y = onD.y;
	text.notifyListeners(SWT.MouseDown, event);

	assertEquals("world", text.getSelectionText());
}

@Test
public void test_doubleClickBeyondEndOfTextSelectsLastWord() {
	text.setText("Hello world" + System.lineSeparator() + "Bye bye");
	text.setSize(1000, 1000);
	Point onE = text.getLocationAtOffset(text.getOffsetAtLine(1) + 6);

	Event event = new Event();
	event.button = 1;
	event.count = 2;
	event.x = Math.min(onE.x + 100, 999);
	event.y = Math.min(onE.y + 100, 999);
	text.notifyListeners(SWT.MouseDown, event);

	assertEquals("bye", text.getSelectionText());
}

@Test
public void test_doubleClickAtStartOfWordLastLineNoEol() {
	text.setText("Hello world" + System.lineSeparator() + "Bye bye");
	text.setSize(1000, 1000);
	Point onB = text.getLocationAtOffset(text.getOffsetAtLine(1) + 4);

	Event event = new Event();
	event.button = 1;
	event.count = 2;
	event.x = onB.x + 1;
	event.y = onB.y;
	text.notifyListeners(SWT.MouseDown, event);

	assertEquals("bye", text.getSelectionText());
}

@Test
public void test_doubleClickAtEndOfWordLastLineNoEol() {
	text.setText("Hello world" + System.lineSeparator() + "Bye bye");
	text.setSize(1000, 1000);
	Point onB = text.getLocationAtOffset(text.getOffsetAtLine(1) + 6);

	Event event = new Event();
	event.button = 1;
	event.count = 2;
	event.x = onB.x + 1;
	event.y = onB.y;
	text.notifyListeners(SWT.MouseDown, event);

	assertEquals("bye", text.getSelectionText());
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
public void test_tripleClickSelectsSentance() {
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
public void test_tripleClickOnLastLineSelectsSentance() {
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
public void test_tripleClickDoesNotSelectIfDoubleClickIsDisabled() {
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
public void test_tripleClickInBlockSelectionSelectsLine() {
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

	// The listener is invoked twice for each line, once for RTF and once for HTML.
	assertEquals("not all lines tested for RTF & HTML copy", 2 * text.getLineCount(), linesCalled[0]);

	Clipboard clipboard = new Clipboard(text.getDisplay());
	RTFTransfer rtfTranfer = RTFTransfer.getInstance();
	String clipboardText = (String) clipboard.getContents(rtfTranfer);
	assertTrue("RTF copy failed", clipboardText.length() > 0);

	HTMLTransfer htmlTranfer = HTMLTransfer.getInstance();
	clipboardText = (String) clipboard.getContents(htmlTranfer);
	assertTrue("HTML copy failed", clipboardText.length() > 0);

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


/**
 * Bug 576052 - ArrowDown does keep position after new line
 */
@Test
public void test_arrowDownKeepsPositionAfterNewLine() {
	text.setText(
	  "...\n"
	+ "\n"
	+ "............................................................\n");

	shell.open();
	text.setSize(10, 50);
	text.setSelection(2);

	text.invokeAction(ST.LINE_DOWN);
	assertEquals(4, text.getSelection().x);
	text.invokeAction(ST.LINE_DOWN);
	assertEquals(7, text.getSelection().x);
}


/**
 * Bug 565164 - SWT.BS event no longer working
 */
@Test
public void test_backspaceAndDelete() {
	shell.open();
	text.setSize(10, 50);
	final Instant timeOut = Instant.now().plusSeconds(10);

	Display display = Display.getDefault();

	Event a = keyEvent('a', SWT.KeyDown, display.getFocusControl());
	Event aUp = keyEvent('a', SWT.KeyUp, display.getFocusControl());

	display.post(a);
	display.post(aUp);

	while (Instant.now().isBefore(timeOut)) {
		if (text.getText().length() == 1) break;

		if (!shell.isDisposed()) {
			display.readAndDispatch();
		}
	}

	// Simulate the backspace, ensuring that the caret is in the correct position
	text.invokeAction(ST.DELETE_PREVIOUS);

	while (Instant.now().isBefore(timeOut)) {
		if (text.getText().length() == 0) break;

		if (!shell.isDisposed()) {
			display.readAndDispatch();
		}
	}

	assertEquals(0, text.getText().length());
}

/**
 * Bug 568033 - Splitting CRLF shall not be forbidden if already on different lines
 */
@Test
public void test_replaceTextRange_isInsideCRLF() {
	text.setText("0123\r\n6789");
	assertThrows("Exception shall be thrown when splitting CRLF", IllegalArgumentException.class,
			() -> text.replaceTextRange(5, 0, "x"));

	assertThrows("Exception shall be thrown when splitting CRLF", IllegalArgumentException.class,
			() -> text.replaceTextRange(0, 5, "x"));

	assertThrows("Exception shall be thrown when splitting CRLF", IllegalArgumentException.class,
			() -> text.replaceTextRange(5, 5, "x"));

	// Shouldn't throw when CR/LF were already on different lines
	text.setText("0\r2\n4");
	text.replaceTextRange(2, 1, "");
	text.replaceTextRange(2, 0, "2");
}

private Event keyEvent(int key, int type, Widget w) {
	Event e = new Event();
	e.keyCode= key;
	e.character = (char) key;
	e.type = type;
	e.widget = w;
	return e;
}

@Test
public void test_lineUpMovesCaret() {
	shell.setVisible(true);
	shell.setLayout(new GridLayout(1, false));
	text.setText("a\nb\nc");
	text.setCaretOffset(2);
	Point caretLocation = text.getCaret().getLocation();
	text.invokeAction(ST.LINE_UP);
	assertNotEquals(caretLocation, text.getCaret().getLocation());
}


@Test
public void test_rangeSelectionKeepsCaret() {
	shell.setVisible(true);
	shell.setLayout(new GridLayout(1, false));
	text.setText("abcdefghij\nABCDEFGHIJ\n0123456789");
	int initialOffset = 16;
	text.setSelection(initialOffset, initialOffset - 3);
	assertEquals(13, text.getCaretOffset());
	text.invokeAction(ST.SELECT_LINE_DOWN);
	assertEquals("Selection does not start from caret", initialOffset, text.getSelection().x);
	assertNotEquals("Selection is not left-to-right", text.getSelection().x, text.getCaretOffset());
}
}
