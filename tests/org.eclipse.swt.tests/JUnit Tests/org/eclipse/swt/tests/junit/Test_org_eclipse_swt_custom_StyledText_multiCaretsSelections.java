/*******************************************************************************
 * Copyright (c) 2000, 2021 IBM Corporation and others.
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

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ST;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.test.Screenshots;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestWatcher;

/**
 * Automated Test Suite for class org.eclipse.swt.custom.StyledText with
 * multiple carets/selections
 *
 * @see org.eclipse.swt.custom.StyledText
 */
public class Test_org_eclipse_swt_custom_StyledText_multiCaretsSelections {

	@Rule
	public TestWatcher screenshotRule = Screenshots.onFailure();

	Shell shell;
	StyledText text;
	GC gc;

	@Before
	public void setUp() {
		shell = new Shell();
		text = new StyledText(shell, SWT.NULL);
		gc = new GC(text);
	}

	@After
	public void tearDown() {
		gc.dispose();
		shell.dispose();
	}
	@Test
	public void test_MultiSelectionEdit() {
		text.setText("0123456789abcdef");
		text.setSelectionRanges(new int[] { 1, 1, 5, 1 });
		assertArrayEquals(new int[] { 1, 1, 5, 1 }, text.getSelectionRanges());
		assertEquals(2, text.getCaretOffset());
		Event keyEvent = new Event();
		keyEvent.type = SWT.KeyDown;
		keyEvent.character = 'x';
		keyEvent.widget = text;
		keyEvent.display = text.getDisplay();
		keyEvent.doit = true;
		text.notifyListeners(SWT.KeyDown, keyEvent);
		assertEquals("0x234x6789abcdef", text.getText());
		assertArrayEquals(new int[] { 2, 0, 6, 0 }, text.getSelectionRanges());
		assertEquals(2, text.getCaretOffset());
		keyEvent.character = 'y';
		text.notifyListeners(SWT.KeyDown, keyEvent);
		assertEquals("0xy234xy6789abcdef", text.getText());
		//
		text.setText("0123456789 ABCDEFGHIJKLM NOPQRSTUVWXYZ");
		text.setSelectionRanges(new int[] { 3, 5, 13, 2, 21, 10 });
		text.notifyListeners(SWT.KeyDown, keyEvent);
		assertEquals("012y89 AByEFGHIJyTUVWXYZ", text.getText());
		assertEquals(4, text.getCaretOffset());
		assertArrayEquals(new int[] { 4, 0, 10, 0, 17, 0 }, text.getSelectionRanges());
		keyEvent.character = 'z';
		text.notifyListeners(SWT.KeyDown, keyEvent);
		assertEquals("012yz89 AByzEFGHIJyzTUVWXYZ", text.getText());
		assertEquals(5, text.getCaretOffset());
	}

	@Test
	public void test_MultiSelectionExpandRanges() {
		text.setText("0123456789");
		text.setSelectionRanges(new int[] { 0, 1, 3, 0, 6, 0 });
		text.invokeAction(ST.SELECT_COLUMN_NEXT);
		assertArrayEquals(new int[] { 0, 2, 3, 1, 6, 1 }, text.getSelectionRanges());
		text.notifyListeners(SWT.Paint, paintEvent());
		text.invokeAction(ST.SELECT_COLUMN_NEXT);
		assertArrayEquals(new int[] { 0, 5, 6, 2 }, text.getSelectionRanges());
		text.notifyListeners(SWT.Paint, paintEvent());
		text.invokeAction(ST.SELECT_COLUMN_NEXT);
		assertArrayEquals(new int[] { 0, 9 }, text.getSelectionRanges());
		text.invokeAction(ST.SELECT_COLUMN_NEXT);
		assertArrayEquals(new int[] { 0, 10 }, text.getSelectionRanges());
		text.notifyListeners(SWT.Paint, paintEvent());
		//
		text.setSelectionRanges(new int[] { 3, 0, 5, 2, 10, 0 });
		text.invokeAction(ST.SELECT_COLUMN_PREVIOUS);
		assertArrayEquals(new int[] { 2, 1, 4, 3, 9, 1 }, text.getSelectionRanges());
		text.notifyListeners(SWT.Paint, paintEvent());
		text.invokeAction(ST.SELECT_COLUMN_PREVIOUS);
		assertArrayEquals(new int[] { 1, 6, 8, 2 }, text.getSelectionRanges());
		text.notifyListeners(SWT.Paint, paintEvent());
		text.invokeAction(ST.SELECT_COLUMN_PREVIOUS);
		assertArrayEquals(new int[] { 0, 10 }, text.getSelectionRanges());
		text.notifyListeners(SWT.Paint, paintEvent());
	}

	@Test
	public void test_MultiCarets_TypeAndMove() {
		text.setText("0123456789");
		text.setSelectionRanges(new int[] { 1, 0, 3, 0, 6, 0 });
		text.invokeAction(ST.COLUMN_NEXT);
		assertArrayEquals(new int[] { 2, 0, 4, 0, 7, 0 }, text.getSelectionRanges());
		text.invokeAction(ST.COLUMN_PREVIOUS);
		assertArrayEquals(new int[] { 1, 0, 3, 0, 6, 0 }, text.getSelectionRanges());
		//
		Event keyEvent = new Event();
		keyEvent.type = SWT.KeyDown;
		keyEvent.character = 'x';
		keyEvent.widget = text;
		keyEvent.display = text.getDisplay();
		keyEvent.doit = true;
		text.notifyListeners(SWT.KeyDown, keyEvent);
		assertEquals("0x12x345x6789", text.getText());
		assertArrayEquals(new int[] { 2, 0, 5, 0, 9, 0 }, text.getSelectionRanges());
		//
		keyEvent.keyCode = SWT.BS;
		keyEvent.character = 0;
		text.notifyListeners(SWT.KeyDown, keyEvent);
		assertEquals("0123456789", text.getText());
		assertArrayEquals(new int[] { 1, 0, 3, 0, 6, 0 }, text.getSelectionRanges());
		//
		text.setText("12345\n12345\n12345\n12345");
		text.setSelectionRanges(new int[] { 1, 0, 4, 0, 13, 0, 17, 0 });
		text.invokeAction(ST.LINE_DOWN);
		assertArrayEquals(new int[] { 7, 0, 10, 0, 19, 0, 23, 0 }, text.getSelectionRanges());
		text.invokeAction(ST.LINE_UP);
		assertArrayEquals(new int[] { 1, 0, 4, 0, 13, 0, 17, 0 }, text.getSelectionRanges());

		// Test joining selection ranges
		text.setSelectionRanges(new int[] { 8, 0, 15, 0 });
		boolean visible = text.getShell().getVisible();
		Layout layout = text.getShell().getLayout();
		try { // this test painting, so need to make content visible, with a layout and focus
			text.getShell().setVisible(true);
			text.getShell().forceActive();
			text.getShell().setLayout(new FillLayout());
			text.getShell().pack();
			text.forceFocus();
			text.isFocusControl();
			text.invokeAction(ST.SELECT_LINE_UP); // first line up also moves caret to left (selection expands by
													// beginning)
			assertArrayEquals(new int[] { 2, 6, 9, 6 }, text.getSelectionRanges());
			text.invokeAction(ST.SELECT_LINE_UP); // select line above, merging the 2 ranges
			assertArrayEquals(new int[] { 0, 15 }, text.getSelectionRanges());
			text.notifyListeners(SWT.Paint, paintEvent());
			// Assert no exception is sent
		} finally {
			text.getShell().setVisible(visible);
			text.getShell().setLayout(layout);
		}
	}

	private Event paintEvent() {
		Event paintEvent = new Event();
		paintEvent.display = text.getDisplay();
		paintEvent.width = text.getSize().x;
		paintEvent.height = text.getSize().y;
		paintEvent.widget = text;
		paintEvent.type = SWT.Paint;
		paintEvent.gc = gc;
		return paintEvent;
	}

	@Test
	public void test_MultiCarets_DeleteAtOffset0() {
		text.setText("0123456789");
		text.setSelectionRanges(new int[] { 0, 0, 3, 0, 6, 0 });
		text.invokeAction(ST.DELETE_NEXT);
		assertArrayEquals(new int[] { 0, 0, 2, 0, 4, 0 }, text.getSelectionRanges());
	}

	@Test
	public void test_MultiCarets_Delete() {
		text.setText("12345\n12345");
		text.setSelectionRanges(new int[] { 2, 0, 8, 0 });
		text.invokeAction(ST.DELETE_NEXT);
		assertEquals("1245\n1245", text.getText());
		assertArrayEquals(new int[] { 2, 0, 7, 0 }, text.getSelectionRanges());
	}

	@Test
	public void test_MultiCarets_CopyPaste() {
		text.setText("1\n2");
		text.setSelectionRanges(new int[] { 0, 1, 2, 1 });
		text.copy();
		text.setSelectionRanges(new int[] { 1, 0, 3, 0 });
		text.paste();
		assertEquals("11\n22", text.getText());
		assertArrayEquals(new int[] { 2, 0, 5, 0 }, text.getSelectionRanges());
	}
}
