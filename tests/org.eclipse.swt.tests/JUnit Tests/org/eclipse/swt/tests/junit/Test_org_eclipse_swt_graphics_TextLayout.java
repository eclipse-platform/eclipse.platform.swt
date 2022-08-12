/*******************************************************************************
 * Copyright (c) 2015, 2022 IBM Corporation and others.
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
 *     Red Hat Inc. - Bug 462631
 *******************************************************************************/
package org.eclipse.swt.tests.junit;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.graphics.TextLayout;
import org.eclipse.swt.graphics.TextStyle;
import org.eclipse.swt.widgets.Display;
import org.junit.Before;
import org.junit.Test;


public class Test_org_eclipse_swt_graphics_TextLayout {
	Display display;

@Before
public void setUp() {
	display = Display.getDefault();
}

/*Test suite start*/
@Test
public void test_getLevel() {
	TextLayout layout = new TextLayout(display);
	String text = "abc55\u05e9\u05e066";
	layout.setText(text);
	assertEquals(0, layout.getLevel(0));
	assertEquals(0, layout.getLevel(2));
//	assertEquals(0, layout.getLevel(4)); //bug in windows (uniscribe)
	assertEquals(1, layout.getLevel(5));
	assertEquals(1, layout.getLevel(6));
	if (!SwtTestUtil.isWindows) assertEquals(2, layout.getLevel(7));  // skipping windows due to fix for bug 565526
	assertEquals(0, layout.getLevel(9));
	assertThrows("invalid range expection expected", IllegalArgumentException.class, () -> layout.getLevel(-1));
	assertThrows("invalid range expection expected", IllegalArgumentException.class,
			() -> layout.getLevel(text.length() + 1));
	layout.dispose();
}

@Test
public void test_getSegments() {
	TextLayout layout = new TextLayout(display);
	layout.setText("AB");
	String[] messages = {"no segments", "segments", "segments (duplicate at 0)", "segments (duplicate at 1)", "segments (duplicate at 2)"};
	int[][] segments = {null, {0,1,2}, {0,0,1,2}, {0,1,1,2}, {0,1,2,2}};
	for (int i = 0; i < segments.length; i++) {
		String m = messages[i];
		layout.setSegments(segments[i]);
		assertEquals(m, 1, layout.getNextOffset(0, SWT.MOVEMENT_CLUSTER));
		assertEquals(m, 2, layout.getNextOffset(1, SWT.MOVEMENT_CLUSTER));
		assertEquals(m, 2, layout.getNextOffset(2, SWT.MOVEMENT_CLUSTER));
		assertEquals(m, 1, layout.getPreviousOffset(2, SWT.MOVEMENT_CLUSTER));
		assertEquals(m, 0, layout.getPreviousOffset(1, SWT.MOVEMENT_CLUSTER));
		assertEquals(m, 0, layout.getPreviousOffset(0, SWT.MOVEMENT_CLUSTER));
	}

	// Bug 295513
	layout.setText("Line");
	layout.setAscent(20);
	layout.setDescent(6);
	layout.setSegments(new int[] {0, layout.getText().length()});
	layout.getBounds();
	layout.dispose();
	layout = new TextLayout(display);

	// Bug 241482 comment 74
	layout.setText("word word word");
	layout.setSegments(new int[] {0, 5, 10});
	int offset = 0;
	offset = layout.getNextOffset(offset, SWT.MOVEMENT_WORD_START);
	assertEquals(5, offset);
	offset = layout.getNextOffset(offset, SWT.MOVEMENT_WORD_START);
	assertEquals(10, offset);
	offset = layout.getNextOffset(offset, SWT.MOVEMENT_WORD_START);
	assertEquals(14, offset);

	layout.setWidth(layout.getBounds().width / 2);
	layout.setAscent(20);
	layout.setDescent(6);
	offset = 0;
	offset = layout.getNextOffset(offset, SWT.MOVEMENT_WORD_START);
	assertEquals(5, offset);
	offset = layout.getNextOffset(offset, SWT.MOVEMENT_WORD_START);
	assertEquals(10, offset);
	offset = layout.getNextOffset(offset, SWT.MOVEMENT_WORD_START);
	assertEquals(14, offset);
	layout.dispose();
	layout = new TextLayout(display);


	//Bug 241482 comment 64
	layout.setText("\nAB");
	layout.setSegments(new int[] {0, 1, 3});
	int[] expected = {0, 1, 3};
	int[] offsets = layout.getLineOffsets();
	assertArrayEquals(expected, offsets);
	layout.dispose();
	layout = new TextLayout(display);

	//Bug 241482 comment  80
	layout.setText("MNMNMN");
	layout.setSegments(new int[] {0,1,6});
	layout.getBounds();
	assertEquals(layout.getText().length() - 1, layout.getOffset(layout.getBounds().width, 0, null));
	layout.setAscent(9);
	assertEquals(layout.getText().length() - 1, layout.getOffset(layout.getBounds().width, 0, null));
	layout.dispose();
	layout = new TextLayout(display);

	//Bug 486600 comment 2
	layout.setText("Abcdef\nGhij\nKl");
	Rectangle bounds1 = layout.getBounds(0, 6 - 1);
	int[] trailing = new int[1];
	offset = layout.getOffset(bounds1.width, bounds1.height + 2, trailing);
	assertEquals(10, offset);
	assertEquals(1, trailing[0]);
	layout.dispose();
	layout = new TextLayout(display);

	layout.setText("AbcdefGhijKl");
	layout.setSegments(new int[] {6, 10});
	layout.setSegmentsChars(new char[] {'\n', '\n'});
	bounds1 = layout.getBounds(0, 6 - 1);
	trailing = new int[1];
	offset = layout.getOffset(bounds1.width, bounds1.height + 2, trailing);
	assertEquals(9, offset);
	assertEquals(1, trailing[0]);
	layout.dispose();
	layout = new TextLayout(display);

	//Lina's bug (bug 241482, comment 37)
	boolean doit = false; //known to be broken
	if (doit) {
		int length = layout.getText().length();
		layout.setSegments(new int[] { length});
		trailing = new int [1];
		int width = layout.getBounds().width + 20;
		assertEquals("hit test to the left", 0, layout.getOffset(0, 0, trailing));
		assertEquals("hit test to the left (trailing)", 0, trailing[0]);
		assertEquals("hit test to the right", length - 1, layout.getOffset(width, 0, trailing));
		assertEquals("hit test to the right (trailing)", 1, trailing[0]);
		layout.setSegmentsChars(new char[] { '*' });
		assertEquals("hit test to the left", 0, layout.getOffset(0, 0, trailing));
		assertEquals("hit test to the left (trailing)", 0, trailing[0]);
		assertEquals("hit test to the right", length - 1, layout.getOffset(width, 0, trailing));
		assertEquals("hit test to the right (trailing)", 1, trailing[0]);
	}

	/* wrong: internal testing */
//	String text = "AB";
//	int textLength = text.length();
//	layout.setText(text);
//	messages = new String [] {
//			"no segments",
//			"segments",
//			"segments (duplicate at 0)",
//			"segments (duplicate at 1)",
//			"segments (duplicate at 2)" };
//	segments = new int [][] {
//			null,
//			{ 0, 1, 2 },
//			{ 0, 0, 1, 2 },
//			{ 0, 1, 1, 2 },
//			{ 0, 1, 2, 2 } };
//	int[][] translatedOffsets = {
//			{ 0, 1, 2 },
//			{ 1, 3, 5 },
//			{ 2, 4, 6 },
//			{ 1, 4, 6 },
//			{ 1, 3, 6 } };
//	int[][] untranslatedOffsets = {
//			{ 0, 1, 2 },
//			{ 0, 0, 1, 1, 2, 2 },
//			{ 0, 0, 0, 1, 1, 2, 2 },
//			{ 0, 0, 1, 1, 1, 2, 2 },
//			{ 0, 0, 1, 1, 2, 2, 2 } };
//	for (int i = 0; i < segments.length; i++) {
//		layout.setSegments(segments[i]);
//		layout.getBounds();
//		for (int j = 0; j <= textLength; j++) {
//			assertEquals(messages[i] + " j = " + j,	translatedOffsets[i][j], layout.translateOffset(j));
//		}
//		for (int j = 0, n = layout.getSegments() == null ? 0 : textLength + layout.getSegments().length; j < n; j++) {
//			assertEquals(messages[i] + " j = " + j,	untranslatedOffsets[i][j], layout.untranslateOffset(j));
//		}
//	}
	layout.dispose();
}

@Test
public void test_getSegmentsChars() {
	if (SwtTestUtil.isCocoa || SwtTestUtil.isWindows) { // skipping windows due to fix for bug 565526
		// TODO Fix Cocoa failure.
		if (SwtTestUtil.verbose) {
			System.out
					.println("Excluded test_getSegmentsChars(org.eclipse.swt.tests.junit.Test_org_eclipse_swt_graphics_TextLayout).");
		}
		return;
	}
	TextLayout layout = new TextLayout(display);
	String text = "ab\u05d0\u05d1.\u05d2cd";
	int textLength = text.length();
	layout.setText(text);

	String[] messages = {
			"no segments",
			"Embedding RTL dir test",
			"Embedding LTR dir test",
			"LRO test",
			"RLO test",
			"Traditional segments",
			"Traditional segments invalid"};
	int[][] segments = {
			null,
			{0, 0, 4, 4, 5, 5, 8, 8},
			{0, 0, 4, 4, 5, 5, 8, 8},
			{0, textLength},
			{0, textLength},
			{0, 4, 8},
			{1}};
	char[][] chars = {
			null,
			{'\u202a', '\u202b', '\u202c', '\u200e', '\u200e', '\u202b', '\u202c', '\u202c'},
			{'\u202b', '\u202a', '\u202c', '\u200f', '\u200f', '\u202a', '\u202c', '\u202c'},
			{0x202d, 0x202c},
			{0x202e, 0x202c},
			null,
			null};
	int[][] levels = {
			{0, 0, 1, 1, 1, 1, 0, 0},
			{4, 4, 3, 3, 2, 3, 4, 4},
			{2, 2, 3, 3, 1, 3, 2, 2},
			{2, 2, 2, 2, 2, 2, 2, 2}, //Fails on cocoa, where it returns {0, 0, 0, 0, 0, 0, 0, 0}
			{1, 1, 1, 1, 1, 1, 1, 1},
			{0, 0, 1, 1, 0, 1, 0, 0},
			{0, 0, 1, 1, 1, 1, 0, 0}};
	int[] offsets = {0, textLength};
	for (int i = 0; i < segments.length; i++) {
		layout.setSegments(segments[i]);
		layout.setSegmentsChars(chars[i]);
		assertArrayEquals("Test line offsets" + ": group: " + i, offsets, layout.getLineOffsets());
		for (int j = 0; j < textLength; j++) {
			assertEquals(messages[i] + ": group: " + i + ", index: " + j, levels[i][j], layout.getLevel(j));
		}
	}
	layout.dispose();
}

@Test
public void test_getLineOffsets() {
	/**
	 * While computing lineCount, Mac doesn't consider the trailing '\n',
	 * causing a different return value than other OS.
	 * Test has been adapted to work on Mac as well.
	 */
	TextLayout layout = new TextLayout(display);
	try {
		String text = "0123456\n890123\n";
		layout.setText(text);
		int[] offsets = layout.getLineOffsets();
		int[] expected = (SwtTestUtil.isCocoa) ? new int [] {0, 8, 15} : new int [] {0, 8, 15, 15} ;
		assertArrayEquals(expected, offsets);
		layout.setText("");
		offsets = layout.getLineOffsets();
		expected = new int [] {0, 0};
		assertArrayEquals(expected, offsets);
		layout.setText("\n");
		offsets = layout.getLineOffsets();
		expected = (SwtTestUtil.isCocoa) ? new int [] {0, 1} : new int [] {0, 1, 1};
		assertArrayEquals(expected, offsets);
		layout.setText("WMWM");
		int width = layout.getBounds().width;
		layout.setWidth(width / 4 + 1);
		offsets = layout.getLineOffsets();
		expected = new int [] {0, 1, 2, 3, 4};
		assertArrayEquals(expected, offsets);
		layout.setWidth(width / 2 + 1);
		offsets = layout.getLineOffsets();
		expected = new int [] {0, 2, 4};
		assertArrayEquals(expected, offsets);
	} finally {
		layout.dispose();
	}
}

@Test
public void test_getLineIndex() {
	if (SwtTestUtil.isCocoa) {
		// TODO Fix Cocoa failure.
		if (SwtTestUtil.verbose) {
			System.out
					.println("Excluded test_getLineIndex(org.eclipse.swt.tests.junit.Test_org_eclipse_swt_graphics_TextLayout).");
		}
		return;
	}
	TextLayout layout = new TextLayout(display);
	String text = "0123456\n890123\n";
	layout.setText(text);
	assertEquals(0, layout.getLineIndex(0));
	assertEquals(0, layout.getLineIndex(5));
	assertEquals(0, layout.getLineIndex(7));
	assertEquals(1, layout.getLineIndex(8));
	assertEquals(1, layout.getLineIndex(12));
	assertEquals(1, layout.getLineIndex(14));
	assertEquals(2, layout.getLineIndex(15));
	assertThrows("invalid range expection expected", IllegalArgumentException.class, ()-> layout.getLineIndex(-1));
	assertThrows("invalid range expection expected", IllegalArgumentException.class, ()->
		layout.getLineIndex(text.length() + 1));
	layout.dispose();
}

@Test
public void test_getLineBounds() {
	if (SwtTestUtil.isCocoa) {
		// TODO Fix Cocoa failure.
		if (SwtTestUtil.verbose) {
			System.out
					.println("Excluded test_getLineBonds(org.eclipse.swt.tests.junit.Test_org_eclipse_swt_graphics_TextLayout).");
		}
		return;
	}
	TextLayout layout = new TextLayout(display);
	String text = "0123456\n890123\n";
	layout.setText(text);
	Rectangle rect0  = layout.getLineBounds(0);
	Rectangle rect1  = layout.getLineBounds(1);
	Rectangle rect2  = layout.getLineBounds(2);

	assertTrue(rect0.width > rect1.width);
	assertEquals(0, rect2.width);
	assertEquals(0, rect0.x);
	assertEquals(0, rect1.x);
	assertEquals(0, rect2.x);
	Rectangle bounds = layout.getBounds();
	assertEquals(bounds.width, rect0.width);
	layout.setWidth(bounds.width);
	layout.setAlignment(SWT.CENTER);
	assertEquals(bounds, layout.getBounds());
	layout.setWidth(bounds.width + 100);
	Rectangle newRect1  = layout.getLineBounds(0);
	assertEquals(50, newRect1.x);
	layout.setAlignment(SWT.RIGHT);
	newRect1  = layout.getLineBounds(0);
	assertEquals(100, newRect1.x);
	assertEquals(bounds.height, rect0.height + rect1.height + rect2.height);

	assertThrows("invalid range expection expected", IllegalArgumentException.class, () -> layout.getLineBounds(-1));
	assertThrows("invalid range expection expected", IllegalArgumentException.class, () -> layout.getLineBounds(3));
	layout.dispose();
}

@Test
public void test_setStyle() {
	TextLayout layout;
	TextStyle s1, s2, s3, s4;
	s1 = new TextStyle (null, null, null);
	s2 = new TextStyle (null, null, null);
	s3 = new TextStyle (null, null, null);
	s4 = new TextStyle (null, null, null);

	layout = new TextLayout (display);
	layout.setText("aabbcc");
	layout.setStyle (s1, 2, 3);
	assertNull(layout.getStyle(0));
	assertNull(layout.getStyle(1));
	assertEquals(s1, layout.getStyle(2));
	assertEquals(s1, layout.getStyle(3));
	assertNull(layout.getStyle(4));
	assertNull(layout.getStyle(5));
	layout.dispose();

	layout = new TextLayout (display);
	layout.setText("aabbcc");
	layout.setStyle (s1, 0, 1);
	layout.setStyle (s2, 2, 3);
	layout.setStyle (s3, 4, 5);
	assertEquals(s1, layout.getStyle(0));
	assertEquals(s1, layout.getStyle(1));
	assertEquals(s2, layout.getStyle(2));
	assertEquals(s2, layout.getStyle(3));
	assertEquals(s3, layout.getStyle(4));
	assertEquals(s3, layout.getStyle(5));
	layout.dispose();

	layout = new TextLayout (display);
	layout.setText("aabbcc");
	layout.setStyle (s1, 0, 1);
	layout.setStyle (s2, 2, 3);
	layout.setStyle (s3, 4, 5);
	layout.setStyle (s4, 1, 2);
	assertEquals(s1, layout.getStyle(0));
	assertEquals(s4, layout.getStyle(1));
	assertEquals(s4, layout.getStyle(2));
	assertEquals(s2, layout.getStyle(3));
	assertEquals(s3, layout.getStyle(4));
	assertEquals(s3, layout.getStyle(5));
	layout.dispose();

	layout = new TextLayout (display);
	layout.setText("aabbcc");
	layout.setStyle (s1, 0, 1);
	layout.setStyle (s2, 2, 3);
	layout.setStyle (s3, 4, 5);
	layout.setStyle (s4, 3, 4);
	assertEquals(s1, layout.getStyle(0));
	assertEquals(s1, layout.getStyle(1));
	assertEquals(s2, layout.getStyle(2));
	assertEquals(s4, layout.getStyle(3));
	assertEquals(s4, layout.getStyle(4));
	assertEquals(s3, layout.getStyle(5));
	layout.dispose();

	layout = new TextLayout (display);
	layout.setText("aabbcc");
	layout.setStyle (s1, 0, 1);
	layout.setStyle (s2, 2, 3);
	layout.setStyle (s3, 4, 5);
	layout.setStyle (s4, 1, 4);
	assertEquals(s1, layout.getStyle(0));
	assertEquals(s4, layout.getStyle(1));
	assertEquals(s4, layout.getStyle(2));
	assertEquals(s4, layout.getStyle(3));
	assertEquals(s4, layout.getStyle(4));
	assertEquals(s3, layout.getStyle(5));
	layout.dispose();

	layout = new TextLayout (display);
	layout.setText("aabbcc");
	layout.setStyle (s1, 0, 0);
	layout.setStyle (s2, 1, 4);
	layout.setStyle (s3, 5, 5);
	layout.setStyle (s4, 2, 3);
	assertEquals(s1, layout.getStyle(0));
	assertEquals(s2, layout.getStyle(1));
	assertEquals(s4, layout.getStyle(2));
	assertEquals(s4, layout.getStyle(3));
	assertEquals(s2, layout.getStyle(4));
	assertEquals(s3, layout.getStyle(5));
	layout.dispose();

	layout = new TextLayout (display);
	layout.setText("aabbcc");
	layout.setStyle (s1, 0, 1);
	layout.setStyle (s2, 2, 3);
	layout.setStyle (s3, 4, 5);
	layout.setStyle (s4, 0, 3);
	assertEquals(s4, layout.getStyle(0));
	assertEquals(s4, layout.getStyle(1));
	assertEquals(s4, layout.getStyle(2));
	assertEquals(s4, layout.getStyle(3));
	assertEquals(s3, layout.getStyle(4));
	assertEquals(s3, layout.getStyle(5));
	layout.dispose();

	layout = new TextLayout (display);
	layout.setText("aabbcc");
	layout.setStyle (s1, 0, 0);
	layout.setStyle (s2, 1, 4);
	layout.setStyle (s3, 5, 5);
	layout.setStyle (s4, 2, 4);
	assertEquals(s1, layout.getStyle(0));
	assertEquals(s2, layout.getStyle(1));
	assertEquals(s4, layout.getStyle(2));
	assertEquals(s4, layout.getStyle(3));
	assertEquals(s4, layout.getStyle(4));
	assertEquals(s3, layout.getStyle(5));
	layout.dispose();

	layout = new TextLayout (display);
	layout.setText("aabbcc");
	layout.setStyle (s1, 0, 0);
	layout.setStyle (s2, 1, 4);
	layout.setStyle (s3, 5, 5);
	layout.setStyle (s4, 1, 3);
	assertEquals(s1, layout.getStyle(0));
	assertEquals(s4, layout.getStyle(1));
	assertEquals(s4, layout.getStyle(2));
	assertEquals(s4, layout.getStyle(3));
	assertEquals(s2, layout.getStyle(4));
	assertEquals(s3, layout.getStyle(5));
	layout.dispose();

	layout = new TextLayout (display);
	layout.setText("aabbcc");
	layout.setStyle (s1, 0, 1);
	layout.setStyle (s2, 2, 3);
	layout.setStyle (s3, 4, 5);
	layout.setStyle (null, 0, 5);
	assertNull(layout.getStyle(0));
	assertNull(layout.getStyle(1));
	assertNull(layout.getStyle(2));
	assertNull(layout.getStyle(3));
	assertNull(layout.getStyle(4));
	assertNull(layout.getStyle(5));
	layout.dispose();

	layout = new TextLayout (display);
	layout.setText("aabbcc");
	layout.setStyle (s1, 0, 1);
	layout.setStyle (s2, 2, 3);
	layout.setStyle (s3, 4, 5);
	layout.setStyle (s4, 0, 5);
	assertEquals(s4, layout.getStyle(0));
	assertEquals(s4, layout.getStyle(1));
	assertEquals(s4, layout.getStyle(2));
	assertEquals(s4, layout.getStyle(3));
	assertEquals(s4, layout.getStyle(4));
	assertEquals(s4, layout.getStyle(5));
	layout.dispose();

	layout = new TextLayout (display);
	layout.setText("aabbcc");
	layout.setStyle (s1, 2, 2);
	layout.setStyle (s2, 2, 3);
	layout.setStyle (null, 3, 3);
	assertNull(layout.getStyle(0));
	assertNull(layout.getStyle(1));
	assertEquals(s2, layout.getStyle(2));
	assertNull(layout.getStyle(3));
	assertNull(layout.getStyle(4));
	assertNull(layout.getStyle(5));
	layout.dispose();
}

@Test
public void test_getAlignment() {
	TextLayout layout = new TextLayout(display);
	layout.setAlignment(SWT.LEFT);
	assertEquals(SWT.LEFT, layout.getAlignment());
	layout.setAlignment(SWT.CENTER);
	assertEquals(SWT.CENTER, layout.getAlignment());
	layout.setAlignment(SWT.RIGHT);
	assertEquals(SWT.RIGHT, layout.getAlignment());

	layout.setAlignment(SWT.PUSH);
	assertEquals(SWT.RIGHT, layout.getAlignment());

	layout.setAlignment(SWT.CENTER| SWT.PUSH);
	assertEquals(SWT.CENTER, layout.getAlignment());

	layout.setAlignment(SWT.CENTER| SWT.LEFT | SWT.RIGHT);
	assertEquals(SWT.LEFT, layout.getAlignment());

	layout.dispose();
}

@Test
public void test_getOrientation() {
	TextLayout layout = new TextLayout(display);
	layout.setOrientation(SWT.LEFT_TO_RIGHT);
	assertEquals(SWT.LEFT_TO_RIGHT, layout.getOrientation());
	layout.setOrientation(SWT.RIGHT_TO_LEFT);
	assertEquals(SWT.RIGHT_TO_LEFT, layout.getOrientation());

	layout.setOrientation(SWT.PUSH);
	assertEquals(SWT.RIGHT_TO_LEFT, layout.getOrientation());

	layout.setOrientation(SWT.LEFT_TO_RIGHT | SWT.PUSH);
	assertEquals(SWT.LEFT_TO_RIGHT, layout.getOrientation());

	layout.setOrientation(SWT.LEFT_TO_RIGHT | SWT.RIGHT_TO_LEFT);
	assertEquals(SWT.LEFT_TO_RIGHT, layout.getOrientation());

	layout.dispose();
}

@Test
public void test_getText() {
	TextLayout layout = new TextLayout(display);
	String text = "Test";
	layout.setText(text);
	assertEquals(text, layout.getText());
	layout.dispose();
}

@Test
public void test_getLocation() {
	if (SwtTestUtil.isCocoa) {
		// TODO Fix Cocoa failure.
		if (SwtTestUtil.verbose) {
			System.out
					.println("Excluded test_getLocation(org.eclipse.swt.tests.junit.Test_org_eclipse_swt_graphics_TextLayout).");
		}
		return;
	}
	TextLayout layout = new TextLayout(display);
	String text = "AB\u05E9\u05E0";
	layout.setText(text);
	assertEquals(0, layout.getLocation(0, false).x);
	assertEquals(layout.getLocation(0, true).x, layout.getLocation(1, false).x);
	assertEquals(layout.getLocation(2, false).x, layout.getLineBounds(0).width);
	assertEquals(layout.getLocation(2, true).x, layout.getLocation(3, false).x);
	assertEquals(layout.getLocation(3, true).x, layout.getLocation(1, true).x);

	assertEquals(layout.getLocation(4, false).x, layout.getLineBounds(0).width);
	assertEquals(layout.getLocation(4, true).x, layout.getLineBounds(0).width);
	layout.dispose();
}

@Test
public void test_getNextOffset() {
	if (SwtTestUtil.isCocoa) {
		// TODO Fix Cocoa failure.
		if (SwtTestUtil.verbose) {
			System.out
					.println("Excluded test_getNextOffset(org.eclipse.swt.tests.junit.Test_org_eclipse_swt_graphics_TextLayout).");
		}
		return;
	}
	TextLayout layout = new TextLayout(display);
	String text;
	int offset;

	text = "word word word";
	layout.setText(text);
	offset = 0;
	offset = layout.getNextOffset(offset, SWT.MOVEMENT_WORD_END);
	assertEquals(4, offset);
	offset = layout.getNextOffset(offset, SWT.MOVEMENT_WORD_END);
	assertEquals(9, offset);
	offset = layout.getNextOffset(offset, SWT.MOVEMENT_WORD_END);
	assertEquals(14, offset);
	offset = layout.getPreviousOffset(offset, SWT.MOVEMENT_WORD_END);
	assertEquals(9, offset);
	offset = layout.getPreviousOffset(offset, SWT.MOVEMENT_WORD_END);
	assertEquals(4, offset);
	offset = layout.getPreviousOffset(offset, SWT.MOVEMENT_WORD_END);
	assertEquals(0, offset);
	assertEquals(4, layout.getNextOffset(2, SWT.MOVEMENT_WORD_END));
	assertEquals(4, layout.getNextOffset(3, SWT.MOVEMENT_WORD_END));
	assertEquals(9, layout.getNextOffset(8, SWT.MOVEMENT_WORD_END));
	assertEquals(14, layout.getNextOffset(10, SWT.MOVEMENT_WORD_END));
	assertEquals(14, layout.getNextOffset(13, SWT.MOVEMENT_WORD_END));
	assertEquals(14, layout.getNextOffset(14, SWT.MOVEMENT_WORD_END));
	assertEquals(0, layout.getPreviousOffset(0, SWT.MOVEMENT_WORD_END));
	assertEquals(0, layout.getPreviousOffset(4, SWT.MOVEMENT_WORD_END));
	assertEquals(4, layout.getPreviousOffset(5, SWT.MOVEMENT_WORD_END));
	assertEquals(9, layout.getPreviousOffset(10, SWT.MOVEMENT_WORD_END));
	assertEquals(9, layout.getPreviousOffset(11, SWT.MOVEMENT_WORD_END));

	offset = 0;
	offset = layout.getNextOffset(offset, SWT.MOVEMENT_WORD_START);
	assertEquals(5, offset);
	offset = layout.getNextOffset(offset, SWT.MOVEMENT_WORD_START);
	assertEquals(10, offset);
	offset = layout.getNextOffset(offset, SWT.MOVEMENT_WORD_START);
	assertEquals(14, offset);
	offset = layout.getPreviousOffset(offset, SWT.MOVEMENT_WORD_START);
	assertEquals(10, offset);
	offset = layout.getPreviousOffset(offset, SWT.MOVEMENT_WORD_START);
	assertEquals(5, offset);
	offset = layout.getPreviousOffset(offset, SWT.MOVEMENT_WORD_START);
	assertEquals(0, offset);
	assertEquals(5, layout.getNextOffset(2, SWT.MOVEMENT_WORD_START));
	assertEquals(5, layout.getNextOffset(4, SWT.MOVEMENT_WORD_START));
	assertEquals(10, layout.getNextOffset(9, SWT.MOVEMENT_WORD_START));
	assertEquals(14, layout.getNextOffset(10, SWT.MOVEMENT_WORD_START));
	assertEquals(14, layout.getNextOffset(13, SWT.MOVEMENT_WORD_START));
	assertEquals(14, layout.getNextOffset(14, SWT.MOVEMENT_WORD_START));
	assertEquals(0, layout.getPreviousOffset(0, SWT.MOVEMENT_WORD_START));
	assertEquals(0, layout.getPreviousOffset(3, SWT.MOVEMENT_WORD_START));
	assertEquals(0, layout.getPreviousOffset(4, SWT.MOVEMENT_WORD_START));
	assertEquals(0, layout.getPreviousOffset(5, SWT.MOVEMENT_WORD_START));
	assertEquals(5, layout.getPreviousOffset(6, SWT.MOVEMENT_WORD_START));



	text = "AB \u05E9\u05E0 CD\nHello";
	layout.setText(text);
	offset = 0;
	offset = layout.getNextOffset(offset, SWT.MOVEMENT_WORD_START);
	assertEquals(3, offset);
	offset = layout.getNextOffset(offset, SWT.MOVEMENT_WORD_START);
	assertEquals(6, offset);
	offset = layout.getNextOffset(offset, SWT.MOVEMENT_WORD_START);
	assertEquals(8, offset);
	offset = layout.getPreviousOffset(offset, SWT.MOVEMENT_WORD_START);
	assertEquals(6, offset);
	offset = layout.getPreviousOffset(offset, SWT.MOVEMENT_WORD_START);
	assertEquals(3, offset);
	offset = layout.getPreviousOffset(offset, SWT.MOVEMENT_WORD_START);
	assertEquals(0, offset);
	offset = 7;
	offset = layout.getNextOffset(offset, SWT.MOVEMENT_WORD_START);
	assertEquals(8, offset);
	offset = layout.getNextOffset(offset, SWT.MOVEMENT_WORD_START);
	assertEquals(9, offset);
	offset = layout.getNextOffset(offset, SWT.MOVEMENT_WORD_START);
	assertEquals(text.length(), offset);
	offset = layout.getPreviousOffset(offset, SWT.MOVEMENT_WORD_START);
	assertEquals(9, offset);
	offset = layout.getPreviousOffset(offset, SWT.MOVEMENT_WORD_START);
	assertEquals(8, offset);
	offset = layout.getNextOffset(offset, SWT.MOVEMENT_CLUSTER);
	assertEquals(9, offset);
	offset = layout.getNextOffset(offset, SWT.MOVEMENT_CLUSTER);
	assertEquals(10, offset);
	offset = layout.getPreviousOffset(offset, SWT.MOVEMENT_CLUSTER);
	assertEquals(9, offset);
	offset = layout.getPreviousOffset(offset, SWT.MOVEMENT_CLUSTER);
	assertEquals(8, offset);
	offset = layout.getPreviousOffset(offset, SWT.MOVEMENT_CLUSTER);
	assertEquals(7, offset);
	for (int i = 0; i < text.length(); i++) {
		assertEquals(i+1, layout.getNextOffset(i, SWT.MOVEMENT_CLUSTER));
	}
	for (int i = text.length(); i > 0; i--) {
		assertEquals(i-1, layout.getPreviousOffset(i, SWT.MOVEMENT_CLUSTER));
	}
	layout.dispose();
}

@Test
public void test_getNextOffset2() {
	if (SwtTestUtil.isGTK||SwtTestUtil.isCocoa) {
		//TODO Fix GTK and Cocoa failure.
		if (SwtTestUtil.verbose) {
			System.out.println("Excluded test_getNextOffset2(org.eclipse.swt.tests.junit.Test_org_eclipse_swt_graphics_TextLayout)");
		}
		return;
	}
	//Text thai cluster separate so it can be excluded
	//for gtk, testing machine (rhel4) is too old to
	//support thai.

	TextLayout layout = new TextLayout(display);
	layout.setText("A\u0E19\u0E49\u0E33B");
	String[] messages = {"no segments", "segments", "segments (duplicate at 0)", "segments (duplicate at 1)", "segments (duplicate at 2)",
						"segments (duplicate at 3)", "segments (duplicate at 4)", "segments (duplicate at 5)"};
//    int[][] segments = {null, {0, 1, 2, 3, 4, 5}, {0, 0, 1, 2, 3, 4, 5}, {0, 1, 1, 2, 3, 4, 5}, {0, 1, 2, 2, 3, 4, 5}, {0, 1, 2, 3, 3, 4, 5},
//            			{0, 1, 2, 3, 4, 4, 5}, {0, 1, 2, 3, 4, 5, 5}};

	int[][] segments = {null};

	for (int i = 0; i < segments.length; i++) {
		String m = messages[i];
		layout.setSegments(segments[i]);
		assertEquals(m, 1, layout.getNextOffset(0, SWT.MOVEMENT_CLUSTER));
		assertEquals(m, 4, layout.getNextOffset(1, SWT.MOVEMENT_CLUSTER));
		assertEquals(m, 5, layout.getNextOffset(4, SWT.MOVEMENT_CLUSTER));
		assertEquals(m, 4, layout.getPreviousOffset(5, SWT.MOVEMENT_CLUSTER));
		assertEquals(m, 1, layout.getPreviousOffset(4, SWT.MOVEMENT_CLUSTER));
		assertEquals(m, 0, layout.getPreviousOffset(1, SWT.MOVEMENT_CLUSTER));
	}
	layout.dispose();
}

@Test
public void test_getLineSpacing() {
	TextLayout layout = new TextLayout(display);
	layout.setSpacing(10);
	assertEquals(10, layout.getSpacing());
	layout.setSpacing(Short.MAX_VALUE);
	assertEquals(Short.MAX_VALUE, layout.getSpacing());
	layout.setSpacing(50);
	assertEquals(50, layout.getSpacing());
	assertThrows("invalid range expection expected", IllegalArgumentException.class, ()->layout.setSpacing(-1));
	assertEquals(50, layout.getSpacing());
	layout.setSpacing(0);
	assertEquals(0, layout.getSpacing());
	layout.dispose();
}

@Test
public void test_getOffset() {
	boolean isCocoa = SwtTestUtil.isCocoa;
	if (isCocoa) {
		// TODO Fix Cocoa failure.
		if (SwtTestUtil.verbose) {
			System.out
					.println("Partially excluded test_getOffset(org.eclipse.swt.tests.junit.Test_org_eclipse_swt_graphics_TextLayout).");
		}
	}
	TextLayout layout = new TextLayout(display);
	String text = "AB \u05E9\u05E0 CD\nHello";
	layout.setText(text);
	int[] trailing = new int[1];

	assertEquals(0, layout.getOffset(0, 0, trailing));
	assertEquals(0, trailing[0]);
	Point point = layout.getLocation(0, true);
	assertEquals(1, layout.getOffset(point.x + 1, 0, trailing));
	assertEquals(0, trailing[0]);
	point = layout.getLocation(1, false);
	assertEquals(0, layout.getOffset(point.x - 1, 0, trailing));
	assertEquals(1, trailing[0]);
	point = layout.getLocation(2, true);
	assertEquals(4, layout.getOffset(point.x + 1, 0, trailing));
	assertEquals(1, trailing[0]);
	point = layout.getLocation(4, true);
	assertEquals(2, layout.getOffset(point.x - 1, 0, trailing));
	assertEquals(1, trailing[0]);
	point = layout.getLocation(4, false);
	assertEquals(3, layout.getOffset(point.x + 1, 0, trailing));
	assertEquals(1, trailing[0]);

	Rectangle bounds = layout.getBounds();
	layout.setWidth(bounds.width + 100);
	layout.setAlignment(SWT.CENTER);

	assertEquals(0, layout.getOffset(0, 0, trailing));
	assertEquals(0, trailing[0]);
	point = layout.getLocation(0, true);
	assertEquals(1, layout.getOffset(point.x + 1, 0, trailing));
	assertEquals(0, trailing[0]);
	point = layout.getLocation(1, false);
	assertEquals(0, layout.getOffset(point.x - 1, 0, trailing));
	assertEquals(1, trailing[0]);
	point = layout.getLocation(2, true);
	assertEquals(4, layout.getOffset(point.x + 1, 0, trailing));
	assertEquals(1, trailing[0]);
	point = layout.getLocation(4, true);
	assertEquals(2, layout.getOffset(point.x - 1, 0, trailing));
	assertEquals(1, trailing[0]);
	point = layout.getLocation(4, false);
	assertEquals(3, layout.getOffset(point.x + 1, 0, trailing));
	assertEquals(1, trailing[0]);

	layout.setAlignment(SWT.RIGHT);

	assertEquals(0, layout.getOffset(0, 0, trailing));
	assertEquals(0, trailing[0]);
	point = layout.getLocation(0, true);
	assertEquals(1, layout.getOffset(point.x + 1, 0, trailing));
	assertEquals(0, trailing[0]);
	point = layout.getLocation(1, false);
	assertEquals(0, layout.getOffset(point.x - 1, 0, trailing));
	assertEquals(1, trailing[0]);
	point = layout.getLocation(2, true);
	assertEquals(4, layout.getOffset(point.x + 1, 0, trailing));
	assertEquals(1, trailing[0]);
	point = layout.getLocation(4, true);
	assertEquals(2, layout.getOffset(point.x - 1, 0, trailing));
	if (!isCocoa) assertEquals(1, trailing[0]);
	point = layout.getLocation(4, false);
	assertEquals(3, layout.getOffset(point.x + 1, 0, trailing));
	assertEquals(1, trailing[0]);

	text = "Text";
	layout.setText(text);
	int width = layout.getBounds().width;
	layout.setAlignment(SWT.LEFT);
	assertEquals(0, layout.getOffset(1, 0, null));
	assertEquals(text.length()-1, layout.getOffset(width-1, 0, null));
	layout.setWidth(width + 100 );
	layout.setAlignment(SWT.CENTER);
	assertEquals(0, layout.getOffset(1+50, 0, null));
	assertEquals(text.length()-1, layout.getOffset(width-1+50, 0, null));
	layout.setAlignment(SWT.RIGHT);
	assertEquals(0, layout.getOffset(1+100, 0, null));
	assertEquals(text.length()-1, layout.getOffset(width-1+100, 0, null));

	layout.dispose();
}

@Test
public void test_getTabs() {
	TextLayout layout = new TextLayout(display);
	assertNull(layout.getTabs());
	int[] tabs = {8, 18, 28, 38, 50, 80};
	layout.setTabs(tabs);
	int[] result = layout.getTabs();
	assertArrayEquals(tabs, result);
	layout.setTabs(null);
	assertNull(layout.getTabs());
	layout.dispose();
}

@Test
public void test_getTextDirection() {
	if (!SwtTestUtil.isWindows) {
		// TODO Fix GTK and Cocoa failure.
		if (SwtTestUtil.verbose) {
			System.out
					.println("Excluded test_getTextDirection(org.eclipse.swt.tests.junit.Test_org_eclipse_swt_graphics_TextLayout).");
		}
		return;
	}
	int[] styles = {SWT.LEFT_TO_RIGHT, SWT.RIGHT_TO_LEFT, SWT.NONE, -1};
	int n = styles.length;
	int prevDirection = SWT.NONE;
	TextLayout layout = new TextLayout(display);
	for (int i = 0; i < n; i++) {
		int orientation = styles[i];
		if (orientation != -1) {
			layout.setOrientation(orientation);
			if (orientation != 0) prevDirection = orientation;
		}
		for (int j = n - 1; j >= 0; j--) {
			int direction = styles[j];
			if (direction != -1) {
				layout.setTextDirection(direction);
			}
			if (direction == SWT.NONE || direction == -1) {
				assertEquals("orientation: " + orientation + ", text direction: " + direction,
						layout.getTextDirection(), prevDirection);
			} else {
				prevDirection = direction;
				assertEquals("orientation: " + orientation + ", text direction: " + direction,
						layout.getTextDirection(), direction);
			}
		}
	}
	layout.dispose();
}

/**
 * Bug 568740 - [Win32] TextLayout renders underscore, strikeout and border only on last line
 */
@Test
public void test_bug568740_multilineTextStyle() {
	Font font = null;
	Image image = null;
	GC gc = null;
	TextLayout layout = null;
	try {
		font = new Font(display, SwtTestUtil.testFontName, 16, SWT.NORMAL);
		image = new Image(display, 200, 100);
		gc = new GC(image);
		gc.setBackground(display.getSystemColor(SWT.COLOR_WHITE));
		gc.fillRectangle(image.getBounds());
		gc.setAntialias(SWT.OFF); // aa can change colors and break the test in worst case

		layout = new TextLayout(display);
		layout.setFont(font);
		layout.setText("first line\nsecond line");

		// The test has one multi-line style containing all the problematic properties
		// in different colors and a second control style with other colors at the
		// end of the second line. Searching for the colors anywhere would even work
		// before the bug was fixed. So we search only in the area of the first line and
		// if we find any of the control colors we know the search area was calculated
		// wrong.

		TextStyle style = new TextStyle();
		style.borderStyle = SWT.BORDER_DOT;
		style.borderColor = display.getSystemColor(SWT.COLOR_BLUE);
		style.underline = true;
		style.underlineColor = display.getSystemColor(SWT.COLOR_GREEN);
		style.strikeout = true;
		style.strikeoutColor = display.getSystemColor(SWT.COLOR_RED);
		layout.setStyle(style, 2, 14);

		TextStyle controlStyle = new TextStyle(style);
		controlStyle.borderColor = display.getSystemColor(SWT.COLOR_DARK_BLUE);
		controlStyle.underlineColor = display.getSystemColor(SWT.COLOR_DARK_GREEN);
		controlStyle.strikeoutColor = display.getSystemColor(SWT.COLOR_DARK_RED);
		layout.setStyle(controlStyle, 15, 23);

		int offset = 10;
		layout.draw(gc, offset, offset);

		Rectangle firstLineBounds = layout.getLineBounds(0);
		Rectangle searchRangeBorder = new Rectangle(0, 0, image.getBounds().width, offset + (int)(firstLineBounds.height * 0.3));
		Rectangle searchRangeStrike = new Rectangle(0, 0, image.getBounds().width, offset + (int)(firstLineBounds.height * 1.0));
		Rectangle searchRangeUnder = new Rectangle(0, 0, image.getBounds().width, offset + (int)(firstLineBounds.height * 1.3));

		assertFalse("Invalid test range for border test. Fix test!", SwtTestUtil.hasPixel(image, display.getSystemColor(SWT.COLOR_DARK_BLUE), searchRangeBorder));
		assertTrue("Found no border style in first line", SwtTestUtil.hasPixel(image, display.getSystemColor(SWT.COLOR_BLUE), searchRangeBorder));

		assertFalse("Invalid test range for strikeout test. Fix test!", SwtTestUtil.hasPixel(image, display.getSystemColor(SWT.COLOR_DARK_RED), searchRangeStrike));
		assertTrue("Found no strikeout style in first line", SwtTestUtil.hasPixel(image, display.getSystemColor(SWT.COLOR_RED), searchRangeStrike));

		assertFalse("Invalid test range for underline test. Fix test!", SwtTestUtil.hasPixel(image, display.getSystemColor(SWT.COLOR_DARK_GREEN), searchRangeUnder));
		assertTrue("Found no underline style in first line", SwtTestUtil.hasPixel(image, display.getSystemColor(SWT.COLOR_GREEN), searchRangeUnder));
	} finally {
		if (layout != null)
			layout.dispose();
		if (gc != null)
			gc.dispose();
		if (image != null)
			image.dispose();
		if (font != null)
			font.dispose();
	}
}

/**
 * Draw the layout on a new image and return it - note the Image needs to be
 * disposed by caller.
 */
private Image draw(TextLayout layout, int antialias) {
	GC gc = null;
	try {
		Image image = new Image(display, layout.getBounds());
		gc = new GC(image);
		gc.setBackground(display.getSystemColor(SWT.COLOR_WHITE));
		gc.fillRectangle(image.getBounds());
		gc.setAntialias(antialias);

		layout.draw(gc, 0, 0);
		return image;
	} finally {
		if (gc != null)
			gc.dispose();
	}
}

/**
 * Draw the input and return a 2d array (indexed [x][y]) of all the pixel
 * values.
 */
private int[][] draw(String input, int antialias) {
	Font font = null;
	Image image = null;
	TextLayout layout = null;
	try {
		// These tests need to use fixed width font to make sure that kerning doesn't
		// make repeated characters overlap.
		font = new Font(display, SwtTestUtil.testFontNameFixedWidth, 16, SWT.NORMAL);

		layout = new TextLayout(display);
		layout.setFont(font);
		layout.setText(input);
		image = draw(layout, antialias);
//		SwtTestUtil.debugDisplayImage(image);
		assertTrue("Found no drawn pixels, nothing at all was drawn!",
		SwtTestUtil.hasPixelNotMatching(image, display.getSystemColor(SWT.COLOR_WHITE), image.getBounds()));
		return SwtTestUtil.getAllPixels(image);

	} finally {
		if (layout != null)
			layout.dispose();
		if (image != null)
			image.dispose();
		if (font != null)
			font.dispose();
	}
}

private void check(String input, int repeat, int antialias) {
	// First draw the short version of the text
	int[][] pixelsOnce = draw(input, antialias);
	// Then draw it repeated however many times needed
	int[][] pixelsRepeated = draw(input.repeat(repeat), antialias);

	// Then compare the drawn short version again and again with each iteration of the repeated version
	for (int i = 0; i < repeat; i++) {
		// Ignore the first and last column in the short version because Windows will tend
		// to draw characters together a little even with fixed width fonts.
		for (int x = 1; x < pixelsOnce.length - 1; x++) {
			try {
				int[] extepectedColumn = pixelsOnce[x];
				int[] actualColumn = pixelsRepeated[x + pixelsOnce.length * i];
				if (Arrays.equals(extepectedColumn, actualColumn)) {
					continue;
				}
				for (int y = 0; y < extepectedColumn.length; y++) {
					SwtTestUtil.assertSimilarBrightness("", extepectedColumn[y], actualColumn[y]);
				}
			} catch (AssertionError e) {
				// When this assertion fails it can be useful to debug it by displaying the two
				// image parts so that the SwtTestUtil.assertSimilarBrightness can be tuned if needed
				// debugCompareTwoImages(pixelsOnce, pixelsRepeated, i);
				throw e;
			}
		}
	}

	// Make sure there are no leftover pixels
	assertEquals(repeat * pixelsOnce.length, pixelsRepeated.length);
}

// Only used when debugging, see catch block above
@SuppressWarnings("unused")
private void debugCompareTwoImages(int[][] pixelsOnce, int[][] pixelsRepeated, int offsetInRepeated) {
	Image expected = SwtTestUtil.createImage(pixelsOnce, 0, pixelsOnce.length);
	try {
		Image actual = SwtTestUtil.createImage(pixelsRepeated, pixelsOnce.length * offsetInRepeated,
				pixelsOnce.length);
		try {
			SwtTestUtil.debugDisplayDifferences(expected, actual);
		} finally {
			actual.dispose();
		}
	} finally {
		expected.dispose();
	}
}

private void check(String input, int repeat) {
	// Run with antialias in multiple modes as it causes different rendering engines
	// to be used (GDI or GDI+)
	check(input, repeat, SWT.DEFAULT);
	check(input, repeat, SWT.ON);
	check(input, repeat, SWT.OFF);
}


/**
 * Bug 23406 - [Win32] TextLayout does not draw lines that are too long
 *
 * This code tests org.eclipse.swt.graphics.TextLayout.splitLongRun(StyleItem) - however
 * to make it easier to debug the test to see what is happening, try reducing
 * org.eclipse.swt.graphics.TextLayout.MAX_RUN_LENGTH and MAX_SEARCH_RUN_BREAK
 * to smaller values so that the splitting happens over shorter runs of text.
 */
@Test
public void test_bug23406_longLines() {
	if (!SwtTestUtil.isWindows) {
		// TODO This test was written for the platform specific parts of TextLayout and
		// on Linux (and possibly mac)
		// the size of images needed to run the tests causes failures.
		if (SwtTestUtil.verbose) {
			System.out.println(
					"Excluded test_bug23406_longLines(org.eclipse.swt.tests.junit.Test_org_eclipse_swt_graphics_TextLayout).");
		}
		return;
	}

	// These first two checks really test the test works fine and
	// should pass with or without the fix to Bug 23406
	check("A", 100);
	check("AV", 100);

	check("A", 100000);
	check("A A", 30000);
	// This is a single code point, if this gets split then the drawing is corrupted
	String crocodile = "\uD83D\uDC0A";
	check(crocodile, 30000);
	// This contains single glyphs requiring multiple characters, if those groups of
	// characters gets split then the drawing is corrupted
	// "This is the best computer program." translated into Thai:
	// "นี่คือโปรแกรมคอมพิวเตอร์ที่ดีที่สุด" then encoded so that java file encoding
	// errors can't corrupt it.
	String bestProgramInThai = "\u0E19\u0E35\u0E48\u0E04\u0E37\u0E2D\u0E42\u0E1B\u0E23\u0E41\u0E01\u0E23\u0E21\u0E04"
			+ "\u0E2D\u0E21\u0E1E\u0E34\u0E27\u0E40\u0E15\u0E2D\u0E23\u0E4C\u0E17\u0E35\u0E48\u0E14\u0E35\u0E17\u0E35\u0E48\u0E2A\u0E38\u0E14";
	check(bestProgramInThai, 1000);
}

@Test
public void test_Bug579335_win32_StyledText_LongLine() {
	if (!SwtTestUtil.isWindows) {
		// TODO This test was written for the platform specific parts of TextLayout and
		// on Linux (and possibly mac)
		// the size of images needed to run the tests causes failures.
		if (SwtTestUtil.verbose) {
			System.out.println(
					"Excluded test_Bug579335_win32_StyledText_LongLine(org.eclipse.swt.tests.junit.Test_org_eclipse_swt_graphics_TextLayout).");
		}
		return;
	}

	Font font = null;
	Image image = null;
	TextLayout layout = null;

	try {
		font = new Font(display, SwtTestUtil.testFontName, 16, SWT.NORMAL);

		layout = new TextLayout(display);
		layout.setFont(font);
		layout.setText("a".repeat(33000));
		Color red = display.getSystemColor(SWT.COLOR_RED);
		Color white = display.getSystemColor(SWT.COLOR_WHITE);
		Color green = display.getSystemColor(SWT.COLOR_GREEN);
		TextStyle redStyle = new TextStyle(font, red, white);
		TextStyle greenStyle = new TextStyle(font, green, white);

		image = draw(layout, SWT.DEFAULT);

		// reset for next test
		image.dispose();
		layout.setStyle(redStyle, 7, 7);
		layout.setStyle(greenStyle, 8, 8 + 32909 - 1);

		image = draw(layout, SWT.DEFAULT);

		// reset for next test
		image.dispose();
		layout.setStyle(null, 0, 33000 - 1);

		layout.setStyle(greenStyle, 2, 2 + 16000 - 1);
		image = draw(layout, SWT.DEFAULT);

		// reset for next test
		image.dispose();
		layout.setStyle(null, 0, 33000 - 1);

		layout.setStyle(redStyle, 0, 0 + 1 - 1);
		layout.setStyle(greenStyle, 1, 1 + 32916 - 1);
		image = draw(layout, SWT.DEFAULT);

//		 SwtTestUtil.debugDisplayImage(image);
	} finally {

		if (layout != null)
			layout.dispose();
		if (image != null)
			image.dispose();
		if (font != null)
			font.dispose();
	}
}
}
