/*******************************************************************************
 * Copyright (c) 2026 Synexia, Hitesh Soliwal (hsoliwal@gmail.com) and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Apache License, Version 2.0, or (at your option) the Eclipse
 * Public License 2.0, which are available at
 * https://www.apache.org/licenses/LICENSE-2.0 and
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: Apache-2.0 OR EPL-2.0
 *
 * Contributors:
 *     Hitesh Soliwal (Synexia) - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tests.junit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.custom.StyledTextContent;
import org.eclipse.swt.custom.TextChangeListener;
import org.eclipse.swt.custom.TextChangedEvent;
import org.eclipse.swt.custom.TextChangingEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Shell;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Differential proof of StyledText's default paged content against the
 * gap-buffer content (<code>-Dorg.eclipse.swt.custom.StyledText.content=gap</code>),
 * plus viewport-mode checks on documents above the renderer's line threshold.
 */
public class Test_org_eclipse_swt_custom_StyledText_PagedContent {
	private static final String CONTENT_PROPERTY = "org.eclipse.swt.custom.StyledText.content";

	private Shell shell;
	private StyledText paged;
	private StyledText gap;

	@BeforeEach
	public void setUp() {
		shell = new Shell();
		shell.setLayout(new FillLayout());
		String previous = System.getProperty(CONTENT_PROPERTY);
		try {
			System.clearProperty(CONTENT_PROPERTY);
			paged = new StyledText(shell, SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL);
			System.setProperty(CONTENT_PROPERTY, "gap");
			gap = new StyledText(shell, SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL);
		} finally {
			if (previous == null) {
				System.clearProperty(CONTENT_PROPERTY);
			} else {
				System.setProperty(CONTENT_PROPERTY, previous);
			}
		}
		shell.setSize(400, 300);
	}

	@AfterEach
	public void tearDown() {
		shell.dispose();
	}

	@Test
	public void test_defaultContentIsPagedAndOptOutIsGap() {
		assertEquals("PagedContent", paged.getContent().getClass().getSimpleName());
		assertEquals("DefaultContent", gap.getContent().getClass().getSimpleName());
	}

	@Test
	public void test_randomEditsMatchGapContent() {
		Random random = new Random(20261013L);
		for (int round = 0; round < 40; round++) {
			String text = randomText(random, random.nextInt(120));
			List<String> pagedEvents = record(paged.getContent());
			List<String> gapEvents = record(gap.getContent());
			paged.getContent().setText(text);
			gap.getContent().setText(text);
			assertSame(paged.getContent(), gap.getContent(), "setText round " + round);
			for (int edit = 0; edit < 60; edit++) {
				int count = gap.getContent().getCharCount();
				int start = random.nextInt(count + 1);
				int length = random.nextInt(Math.min(6, count - start) + 1);
				String insert = randomText(random, random.nextInt(4));
				RuntimeException gapError = apply(gap.getContent(), start, length, insert);
				RuntimeException pagedError = apply(paged.getContent(), start, length, insert);
				String where = "round " + round + " edit " + edit + " replace(" + start + "," + length + ","
						+ insert.replace("\r", "\\r").replace("\n", "\\n") + ")";
				assertEquals(gapError == null ? null : gapError.getClass(), pagedError == null ? null : pagedError.getClass(), where);
				assertSame(paged.getContent(), gap.getContent(), where);
				assertEquals(gapEvents, pagedEvents, where);
				assertEquals(gap.getLineCount(), paged.getLineCount(), where);
			}
		}
	}

	@Test
	public void test_largeDocumentViewportMode() {
		int lines = 200_000;
		StringBuilder builder = new StringBuilder(lines * 32);
		for (int i = 0; i < lines; i++) {
			builder.append(String.format("%010d viewport line text%n", i));
		}
		String text = builder.toString();
		for (boolean wrap : new boolean[] { false, true }) {
			paged.setWordWrap(wrap);
			paged.setText(text);
			assertEquals(lines + 1, paged.getLineCount());
			assertEquals(text.length(), paged.getCharCount());
			int middle = lines / 2;
			int offset = paged.getOffsetAtLine(middle);
			assertEquals(middle, paged.getLineAtOffset(offset));
			assertEquals(String.format("%010d viewport line text", middle), paged.getLine(middle));
			paged.setTopIndex(lines - 1);
			assertTrue(paged.getTopIndex() > middle, "top index after scrolling to the end, wrap=" + wrap);
			paged.setSelection(paged.getCharCount());
			paged.showSelection();
			paged.setTopIndex(0);
			assertEquals(0, paged.getTopIndex());
			paged.replaceTextRange(offset, 0, "edit\n");
			assertEquals(lines + 2, paged.getLineCount());
			assertEquals("edit", paged.getLine(middle));
			paged.replaceTextRange(offset, 5, "");
			assertEquals(text, paged.getText());
		}
	}

	@Test
	public void test_wrapThenLargeSetTextMeasuresOnlyTheWindow() throws ReflectiveOperationException {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < 200_000; i++) {
			builder.append(i).append(" wrapped viewport line").append('\n');
		}
		shell.open();
		paged.setWordWrap(true); // queues a whole-document idle pass for the empty document
		paged.setText(builder.toString());
		long deadline = System.currentTimeMillis() + 3000;
		while (System.currentTimeMillis() < deadline && shell.getDisplay().readAndDispatch()) {
			// let idle measurement run
		}
		assertTrue(measuredLines(paged) <= 4096, "measured lines " + measuredLines(paged));
	}

	private static int measuredLines(StyledText styledText) throws ReflectiveOperationException {
		Field rendererField = StyledText.class.getDeclaredField("renderer");
		rendererField.setAccessible(true);
		Object renderer = rendererField.get(styledText);
		Field sizesField = renderer.getClass().getDeclaredField("lineSizes");
		sizesField.setAccessible(true);
		int measured = 0;
		for (Object size : (Object[]) sizesField.get(renderer)) {
			if (size != null) {
				measured++;
			}
		}
		return measured;
	}

	@Test
	public void test_unmergedCrLfStaysSeparateAfterEdits() {
		for (StyledText styledText : new StyledText[] { paged, gap }) {
			styledText.setText("a\rb\nc");
			styledText.replaceTextRange(2, 1, "");
			assertEquals(3, styledText.getLineCount(), styledText.getContent().getClass().getSimpleName());
			styledText.setText(styledText.getText());
			assertEquals(2, styledText.getLineCount(), styledText.getContent().getClass().getSimpleName());
		}
	}

	private static String randomText(Random random, int length) {
		char[] alphabet = { 'a', 'b', '\r', '\n', 'c', '\n' };
		char[] chars = new char[length];
		for (int i = 0; i < length; i++) {
			chars[i] = alphabet[random.nextInt(alphabet.length)];
		}
		return new String(chars);
	}

	private static RuntimeException apply(StyledTextContent content, int start, int length, String text) {
		try {
			content.replaceTextRange(start, length, text);
			return null;
		} catch (RuntimeException e) {
			return e;
		}
	}

	private static List<String> record(StyledTextContent content) {
		List<String> events = new ArrayList<>();
		content.addTextChangeListener(new TextChangeListener() {
			@Override
			public void textChanging(TextChangingEvent event) {
				events.add("changing " + event.start + " " + event.replaceCharCount + " " + event.newCharCount + " "
						+ event.replaceLineCount + " " + event.newLineCount + " " + event.newText);
			}

			@Override
			public void textChanged(TextChangedEvent event) {
				events.add("changed");
			}

			@Override
			public void textSet(TextChangedEvent event) {
				events.add("set");
			}
		});
		return events;
	}

	private static void assertSame(StyledTextContent actual, StyledTextContent expected, String where) {
		assertEquals(expected.getCharCount(), actual.getCharCount(), where);
		assertEquals(expected.getLineCount(), actual.getLineCount(), where);
		assertEquals(expected.getTextRange(0, expected.getCharCount()), actual.getTextRange(0, actual.getCharCount()), where);
		for (int line = 0; line < expected.getLineCount(); line++) {
			assertEquals(expected.getOffsetAtLine(line), actual.getOffsetAtLine(line), where + " offsetAtLine " + line);
			assertEquals(expected.getLine(line), actual.getLine(line), where + " line " + line);
		}
		for (int offset = 0; offset <= expected.getCharCount(); offset++) {
			assertEquals(expected.getLineAtOffset(offset), actual.getLineAtOffset(offset), where + " lineAtOffset " + offset);
		}
	}
}
