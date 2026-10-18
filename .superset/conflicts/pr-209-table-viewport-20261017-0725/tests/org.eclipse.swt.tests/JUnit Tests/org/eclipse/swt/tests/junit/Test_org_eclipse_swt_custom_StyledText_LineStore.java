/*******************************************************************************
 * Copyright (c) 2026 Contributors to the Eclipse Foundation.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package org.eclipse.swt.tests.junit;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Line state law (leaf 3 part B): per-line attributes live in one int mask per
 * line, runs and stride patterns, and parallel int id arrays. A reference model
 * with one record per line (the former LineInfo[] semantics) is driven by the
 * same random operations, text edits included; every line's API values must
 * match after every step. Uniform and striped writes must stay patterns.
 */
public class Test_org_eclipse_swt_custom_StyledText_LineStore {
	private Shell shell;

	@AfterEach
	public void tearDown() {
		if (shell != null) {
			shell.dispose();
		}
	}

	/** One line of the reference model; null fields are "not set". */
	static final class Line {
		Color background;
		Integer alignment, indent, wrapIndent;
		Boolean justify;
		int[] tabStops;

		Line copy() {
			Line l = new Line();
			l.background = background;
			l.alignment = alignment;
			l.indent = indent;
			l.wrapIndent = wrapIndent;
			l.justify = justify;
			l.tabStops = tabStops;
			return l;
		}
	}

	private StyledText create(int lines) {
		shell = new Shell();
		shell.setLayout(new FillLayout());
		StyledText t = new StyledText(shell, SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL);
		shell.setBounds(40, 40, 480, 360);
		shell.open();
		StringBuilder b = new StringBuilder();
		for (int i = 0; i < lines; i++) {
			b.append("line ").append(i).append(i + 1 < lines ? "\n" : "");
		}
		t.setText(b.toString());
		return t;
	}

	@ParameterizedTest(name = "seed={0}")
	@ValueSource(longs = { 1, 2, 3, 4 })
	public void test_randomOperationsMatchPerLineModel(long seed) {
		StyledText t = create(220);
		Display display = t.getDisplay();
		Color[] colors = { display.getSystemColor(SWT.COLOR_RED), display.getSystemColor(SWT.COLOR_GREEN),
				display.getSystemColor(SWT.COLOR_BLUE), display.getSystemColor(SWT.COLOR_YELLOW) };
		List<Line> model = new ArrayList<>();
		for (int i = 0; i < t.getLineCount(); i++) {
			model.add(new Line());
		}
		Random random = new Random(seed);
		for (int step = 0; step < 700; step++) {
			int lineCount = t.getLineCount();
			int start = random.nextInt(lineCount);
			int count = random.nextInt(4) == 0 ? random.nextInt(lineCount - start) + 1 : Math.min(lineCount - start, 1 + random.nextInt(3));
			int op = random.nextInt(12);
			switch (op) {
			case 0, 1 -> {
				Color c = random.nextInt(6) == 0 ? null : colors[random.nextInt(colors.length)];
				t.setLineBackground(start, count, c);
				for (int i = start; i < start + count; i++) {
					model.get(i).background = c;
				}
			}
			case 2 -> { // stride writes: zebra striping of one colour
				Color c = colors[random.nextInt(colors.length)];
				int stride = 1 + random.nextInt(3);
				for (int i = start; i < Math.min(lineCount, start + 12 * stride); i += stride) {
					t.setLineBackground(i, 1, c);
					model.get(i).background = c;
				}
			}
			case 3 -> {
				int a = random.nextBoolean() ? SWT.CENTER : SWT.RIGHT;
				t.setLineAlignment(start, count, a);
				for (int i = start; i < start + count; i++) {
					model.get(i).alignment = a;
				}
			}
			case 4 -> {
				int v = random.nextInt(20);
				t.setLineIndent(start, count, v);
				for (int i = start; i < start + count; i++) {
					model.get(i).indent = v;
				}
			}
			case 5 -> {
				int v = random.nextInt(20);
				t.setLineWrapIndent(start, count, v);
				for (int i = start; i < start + count; i++) {
					model.get(i).wrapIndent = v;
				}
			}
			case 6 -> {
				boolean v = random.nextBoolean();
				t.setLineJustify(start, count, v);
				for (int i = start; i < start + count; i++) {
					model.get(i).justify = v;
				}
			}
			case 7 -> {
				int[] v = random.nextBoolean() ? null : new int[] { 10 + random.nextInt(30), 60 + random.nextInt(30) };
				t.setLineTabStops(start, count, v);
				for (int i = start; i < start + count; i++) {
					model.get(i).tabStops = v;
				}
			}
			case 8 -> { // a run longer than the run threshold
				Color c = colors[random.nextInt(colors.length)];
				int runStart = random.nextInt(Math.max(1, lineCount - 40));
				int runCount = Math.min(lineCount - runStart, 32 + random.nextInt(60));
				t.setLineBackground(runStart, runCount, c);
				for (int i = runStart; i < runStart + runCount; i++) {
					model.get(i).background = c;
				}
			}
			default -> edit(t, model, random);
			}
			if (step % 7 == 0 || step > 690) {
				assertModel(t, model, "seed " + seed + " step " + step + " op " + op);
			}
		}
	}

	/** A text edit with the DefaultContent line semantics of the former LineInfo[] arraycopy. */
	private void edit(StyledText t, List<Line> model, Random random) {
		int lineCount = t.getLineCount();
		int line = random.nextInt(lineCount);
		int lineOffset = t.getOffsetAtLine(line);
		int lineLength = t.getLine(line).length();
		int start = lineOffset + (random.nextBoolean() ? 0 : random.nextInt(lineLength + 1));
		int replace = 0;
		if (random.nextBoolean()) {
			int endLine = Math.min(lineCount - 1, line + random.nextInt(4));
			int end = t.getOffsetAtLine(endLine) + (random.nextBoolean() ? 0 : t.getLine(endLine).length());
			replace = Math.max(0, end - start);
		}
		String insert = switch (random.nextInt(4)) {
			case 0 -> "";
			case 1 -> "x";
			case 2 -> "a\nb";
			default -> "p\nq\nr\n";
		};
		if (replace == 0 && insert.isEmpty()) {
			insert = "\n";
		}
		String replaced = t.getTextRange(start, replace);
		int replaceLines = (int) replaced.chars().filter(c -> c == '\n').count();
		int newLines = (int) insert.chars().filter(c -> c == '\n').count();
		int startLine = t.getLineAtOffset(start);
		boolean all = replace == t.getCharCount();
		t.replaceTextRange(start, replace, insert);
		if (all) {
			model.replaceAll(l -> new Line());
		}
		if (replaceLines != 0 || newLines != 0) {
			int s = startLine + (t.getOffsetAtLine(startLine) != start ? 1 : 0);
			List<Line> tail = new ArrayList<>(model.subList(Math.min(model.size(), s + replaceLines), model.size()));
			while (model.size() > s) {
				model.remove(model.size() - 1);
			}
			for (int i = 0; i < newLines; i++) {
				model.add(new Line());
			}
			model.addAll(tail);
		}
		assertEquals(t.getLineCount(), model.size(), "model line count");
	}

	private void assertModel(StyledText t, List<Line> model, String where) {
		assertEquals(t.getLineCount(), model.size(), where);
		int[] widgetTabs = t.getTabStops(); // a line without tab stops (or with null) reports the widget's
		for (int i = 0; i < model.size(); i++) {
			Line l = model.get(i);
			String at = where + " line " + i;
			assertSame(l.background, t.getLineBackground(i), at + " background");
			assertEquals(l.alignment == null ? t.getAlignment() : l.alignment.intValue(), t.getLineAlignment(i), at + " alignment");
			assertEquals(l.indent == null ? t.getIndent() : l.indent.intValue(), t.getLineIndent(i), at + " indent");
			assertEquals(l.wrapIndent == null ? t.getWrapIndent() : l.wrapIndent.intValue(), t.getLineWrapIndent(i), at + " wrap indent");
			assertEquals(l.justify == null ? t.getJustify() : l.justify.booleanValue(), t.getLineJustify(i), at + " justify");
			assertArrayEquals(l.tabStops != null ? l.tabStops : widgetTabs, t.getLineTabStops(i), at + " tab stops");
		}
	}

	@Test
	public void test_uniformAndStripedWritesStayPatterns() throws Exception {
		StyledText t = create(200_000);
		Color red = t.getDisplay().getSystemColor(SWT.COLOR_RED);
		Color blue = t.getDisplay().getSystemColor(SWT.COLOR_BLUE);
		Object store = store(t);
		t.setLineBackground(0, t.getLineCount(), red);
		assertNull(field(store, "mask"), "a uniform write allocates no per-line ints");
		assertEquals(1, field(store, "patternCount"));
		for (int i = 1; i < 20_000; i += 2) {
			t.setLineBackground(i, 1, blue); // zebra
		}
		assertNull(field(store, "mask"), "stride writes allocate no per-line ints");
		assertEquals(2, field(store, "patternCount"));
		for (int i = 0; i < 40; i++) {
			assertSame(i % 2 == 1 && i < 20_000 ? blue : red, t.getLineBackground(i), "line " + i);
		}
		assertSame(red, t.getLineBackground(20_001));
		assertSame(red, t.getLineBackground(t.getLineCount() - 1));
		// an edit inside the stripes keeps the stride phase below it
		t.replaceTextRange(t.getOffsetAtLine(10), 0, "x\ny\n");
		assertSame(blue, t.getLineBackground(9));
		assertNull(t.getLineBackground(10));
		assertNull(t.getLineBackground(11));
		assertSame(red, t.getLineBackground(12), "old line 10");
		assertSame(blue, t.getLineBackground(13), "old line 11");
		long bytes = (Long) invoke(store, "footprintBytes");
		assertTrue(bytes < 200_000, "store bytes " + bytes + " for 200,000 lines with patterns");
	}

	@Test
	public void test_patternBoundMaterializesWithoutChangingValues() throws Exception {
		StyledText t = create(6_000);
		Display display = t.getDisplay();
		Color[] colors = { display.getSystemColor(SWT.COLOR_RED), display.getSystemColor(SWT.COLOR_GREEN),
				display.getSystemColor(SWT.COLOR_BLUE) };
		Color[] expected = new Color[t.getLineCount()];
		for (int k = 0; k < 90; k++) { // more runs than the pattern bound, overlapping
			int start = (k * 61) % 5_000;
			Color c = colors[k % colors.length];
			t.setLineBackground(start, 40 + k % 7, c);
			Arrays.fill(expected, start, start + 40 + k % 7, c);
		}
		Object store = store(t);
		assertTrue((Integer) field(store, "patternCount") <= 64);
		for (int i = 0; i < expected.length; i++) {
			assertSame(expected[i], t.getLineBackground(i), "line " + i);
		}
	}

	static Object store(StyledText t) throws Exception {
		Object renderer = field(t, "renderer");
		return field(renderer, "store");
	}

	static Object field(Object owner, String name) throws Exception {
		Class<?> c = owner.getClass();
		while (c != null) {
			try {
				Field f = c.getDeclaredField(name);
				f.setAccessible(true);
				return f.get(owner);
			} catch (NoSuchFieldException e) {
				c = c.getSuperclass();
			}
		}
		throw new NoSuchFieldException(name);
	}

	static Object invoke(Object owner, String name) throws Exception {
		Method m = owner.getClass().getDeclaredMethod(name);
		m.setAccessible(true);
		return m.invoke(owner);
	}
}
