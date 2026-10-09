/*******************************************************************************
 * Copyright (c) 2026 vogella GmbH and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package org.eclipse.swt.tests.cocoa;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.eclipse.swt.SWT;
import org.eclipse.swt.accessibility.ACC;
import org.eclipse.swt.accessibility.Accessible;
import org.eclipse.swt.accessibility.AccessibleControlAdapter;
import org.eclipse.swt.accessibility.AccessibleControlEvent;
import org.eclipse.swt.accessibility.AccessibleTextAdapter;
import org.eclipse.swt.accessibility.AccessibleTextEvent;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.internal.cocoa.NSAttributedString;
import org.eclipse.swt.internal.cocoa.NSNumber;
import org.eclipse.swt.internal.cocoa.NSRange;
import org.eclipse.swt.internal.cocoa.NSString;
import org.eclipse.swt.internal.cocoa.NSValue;
import org.eclipse.swt.internal.cocoa.OS;
import org.eclipse.swt.internal.cocoa.id;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Queries text attributes with ranges and indexes past the end of the text,
 * which accessibility clients such as VoiceOver send.
 */
public class Test_cocoa_AccessibleTextRanges {

	private Shell shell;

	@BeforeEach
	public void setUp() {
		shell = new Shell(Display.getDefault());
	}

	@AfterEach
	public void tearDown() {
		shell.dispose();
	}

	@Test
	public void test_stringForRange_pastEnd() {
		Accessible accessible = canvasWithValue("short text").getAccessible();
		assertEquals("short text", stringForRange(accessible, 0, 2000));
		assertEquals("text", stringForRange(accessible, 6, 100));
		assertEquals("", stringForRange(accessible, 100, 5));
	}

	@Test
	public void test_selectedText_selectionPastEnd() {
		Accessible accessible = canvasWithValue("abc").getAccessible();
		accessible.addAccessibleTextListener(new AccessibleTextAdapter() {
			@Override
			public void getSelectionRange(AccessibleTextEvent e) {
				e.offset = 2;
				e.length = 100;
			}
		});
		id result = accessible.internal_accessibilityAttributeValue(OS.NSAccessibilitySelectedTextAttribute, ACC.CHILDID_SELF);
		assertEquals("c", new NSString(result).getString());
	}

	@Test
	public void test_lineForIndex_indexPastEnd() {
		Accessible accessible = canvasWithValue("a\nb").getAccessible();
		id result = accessible.internal_accessibilityAttributeValue_forParameter(
				OS.NSAccessibilityLineForIndexParameterizedAttribute, NSNumber.numberWithInt(100), ACC.CHILDID_SELF);
		assertEquals(2, new NSNumber(result).intValue());
	}

	@Test
	public void test_attributedStringForRange_pastEnd() {
		StyledText styledText = new StyledText(shell, SWT.NONE);
		styledText.setText("plain");
		assertEquals("plain", attributedStringForRange(styledText.getAccessible(), 0, 2000));

		styledText.setText("styled text");
		styledText.setStyleRange(new StyleRange(0, 6, null, null, SWT.BOLD));
		assertEquals("styled text", attributedStringForRange(styledText.getAccessible(), 0, 2000));
		assertEquals("text", attributedStringForRange(styledText.getAccessible(), 7, 2000));
	}

	private Canvas canvasWithValue(String value) {
		Canvas canvas = new Canvas(shell, SWT.NONE);
		canvas.getAccessible().addAccessibleControlListener(new AccessibleControlAdapter() {
			@Override
			public void getValue(AccessibleControlEvent e) {
				e.result = value;
			}
		});
		return canvas;
	}

	private static String stringForRange(Accessible accessible, long location, long length) {
		id result = accessible.internal_accessibilityAttributeValue_forParameter(
				OS.NSAccessibilityStringForRangeParameterizedAttribute, range(location, length), ACC.CHILDID_SELF);
		return new NSString(result).getString();
	}

	private static String attributedStringForRange(Accessible accessible, long location, long length) {
		id result = accessible.internal_accessibilityAttributeValue_forParameter(
				OS.NSAccessibilityAttributedStringForRangeParameterizedAttribute, range(location, length), ACC.CHILDID_SELF);
		return new NSAttributedString(result).string().getString();
	}

	private static NSValue range(long location, long length) {
		NSRange range = new NSRange();
		range.location = location;
		range.length = length;
		return NSValue.valueWithRange(range);
	}
}
