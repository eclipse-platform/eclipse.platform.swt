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

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.Bullet;
import org.eclipse.swt.custom.ST;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GlyphMetrics;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Reproduction law: every line and render state of the top lines (styles, link, embedded object,
 * line background, bullet, alignment, indents, justify, tab stops, wrap, multi-selection, caret and
 * IME composition) is reproduced from the widget's stored state plus the content after the lines
 * leave the viewport and their cached layouts are evicted. Scrolling to the end and back must give
 * identical API values and an identical pixel hash, and must have rebuilt the layouts.
 */
public class Test_org_eclipse_swt_custom_StyledText_RoundTrip {
	private Shell shell;

	@AfterEach
	public void tearDown() {
		if (shell != null) {
			shell.dispose();
		}
	}

	@ParameterizedTest(name = "wrap={0}")
	@ValueSource(booleans = { false, true })
	public void test_statesSurviveEvictionAndScrollBack(boolean wrap) {
		shell = new Shell();
		shell.setLayout(new FillLayout());
		StyledText t = new StyledText(shell, SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL);
		shell.setBounds(60, 60, 520, 380);
		shell.open();
		StringBuilder b = new StringBuilder();
		for (int i = 0; i < 100_000; i++) { // over the viewport threshold
			b.append("line ").append(i).append(" alpha\tbeta gamma delta epsilon zeta eta theta").append(i % 5 == 0 ? " and a much longer tail that wraps\n" : "\n");
		}
		t.setText(b.toString());
		t.setWordWrap(wrap);
		Display display = t.getDisplay();
		Color red = display.getSystemColor(SWT.COLOR_RED), yellow = display.getSystemColor(SWT.COLOR_YELLOW);
		// styles: font style, colours, underline, strikeout, border, rise, link, embedded object
		StyleRange features = new StyleRange(t.getOffsetAtLine(1), 9, red, yellow, SWT.BOLD | SWT.ITALIC);
		features.underline = true;
		features.underlineStyle = SWT.UNDERLINE_SQUIGGLE;
		features.strikeout = true;
		features.borderStyle = SWT.BORDER_DOT;
		t.setStyleRange(features);
		StyleRange rise = new StyleRange(t.getOffsetAtLine(2) + 5, 3, null, null);
		rise.rise = 4;
		t.setStyleRange(rise);
		StyleRange link = new StyleRange(t.getOffsetAtLine(3), 5, null, null);
		link.underline = true;
		link.underlineStyle = SWT.UNDERLINE_LINK;
		link.data = "https://example.invalid";
		t.setStyleRange(link);
		StyleRange object = new StyleRange(t.getOffsetAtLine(4) + 6, 1, null, null);
		object.metrics = new GlyphMetrics(10, 3, 14);
		t.setStyleRange(object);
		t.addPaintObjectListener(e -> e.gc.drawRectangle(e.x, e.y - e.ascent, 13, e.ascent + e.descent - 1));
		// line attributes
		t.setLineBackground(5, 2, yellow);
		StyleRange bulletStyle = new StyleRange();
		bulletStyle.metrics = new GlyphMetrics(0, 0, 24);
		t.setLineBullet(7, 3, new Bullet(ST.BULLET_NUMBER | ST.BULLET_TEXT, bulletStyle));
		t.setLineAlignment(10, 1, SWT.CENTER);
		t.setLineIndent(11, 1, 20);
		t.setLineWrapIndent(12, 1, 30);
		t.setLineJustify(13, 1, true);
		t.setLineVerticalIndent(14, 6);
		t.setLineTabStops(15, 1, new int[] { 30, 70 });
		// an IME composition in progress on line 18 (uncommitted), then selection and carets
		org.eclipse.swt.widgets.IME ime = t.getIME();
		if (ime != null) {
			int at = t.getOffsetAtLine(18) + 3;
			t.setCaretOffset(at);
			imeState(ime, at, 2, "ime", 0);
			org.eclipse.swt.widgets.Event e = new org.eclipse.swt.widgets.Event();
			e.detail = SWT.COMPOSITION_CHANGED;
			e.start = at;
			e.end = at;
			e.text = "ime";
			ime.notifyListeners(SWT.ImeComposition, e);
		}
		t.setSelectionRanges(new int[] { t.getOffsetAtLine(16) + 2, 6, t.getOffsetAtLine(17) + 1, 4 });
		flush(t);
		t.setTopIndex(0);
		flush(t);
		String before = StyledTextApiOps.snapshot(t, true, true) + " caretLoc=" + t.getCaret().getLocation();

		long builds = counter(t, "layoutBuilds");
		t.setTopIndex(t.getLineCount() - 1); // the top lines leave the viewport and the layout cache
		flush(t);
		t.setTopIndex(0);
		flush(t);
		String after = StyledTextApiOps.snapshot(t, true, true) + " caretLoc=" + t.getCaret().getLocation();
		assertTrue(counter(t, "layoutBuilds") - builds > 20, "the layouts of the top lines were rebuilt after eviction");
		assertEquals(before, after, "API values and pixels reproduce after eviction");
		if (ime != null) {
			imeState(ime, -1, 0, "", 0);
		}
	}

	private static void imeState(org.eclipse.swt.widgets.IME ime, int start, int caret, String text, int commit) {
		try {
			for (String name : new String[] { "startOffset", "caretOffset", "commitCount" }) {
				Field f = org.eclipse.swt.widgets.IME.class.getDeclaredField(name);
				f.setAccessible(true);
				f.setInt(ime, name.equals("startOffset") ? start : name.equals("caretOffset") ? caret : commit);
			}
			Field f = org.eclipse.swt.widgets.IME.class.getDeclaredField("text");
			f.setAccessible(true);
			f.set(ime, text);
		} catch (ReflectiveOperationException e) {
			throw new IllegalStateException(e);
		}
	}

	private static void flush(StyledText t) {
		t.update();
		Display display = t.getDisplay();
		long deadline = System.currentTimeMillis() + 1500;
		while (System.currentTimeMillis() < deadline && display.readAndDispatch()) {
			// drain paints and idle slices
		}
	}

	private static long counter(StyledText t, String name) {
		try {
			Field rendererField = StyledText.class.getDeclaredField("renderer");
			rendererField.setAccessible(true);
			Object renderer = rendererField.get(t);
			Field f = renderer.getClass().getDeclaredField(name);
			f.setAccessible(true);
			return f.getLong(renderer);
		} catch (ReflectiveOperationException e) {
			throw new IllegalStateException(e);
		}
	}
}
