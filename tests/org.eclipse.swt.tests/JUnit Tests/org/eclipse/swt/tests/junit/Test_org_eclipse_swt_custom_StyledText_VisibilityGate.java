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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

/**
 * Visibility law: a StyledText that is not effectively visible does no
 * measurement, layout, idle pass, timer or redraw work. It records dirty bits
 * and catches up once when it becomes visible; the result is pixel-identical to
 * a widget that was visible throughout.
 * <p>
 * Document size: {@code -Dswt.styledtext.visibilityGate.mib=100} for the 100 MiB
 * proof (about 1.9 million lines); the default 4 MiB (about 76,000 lines, over the
 * 65,536-line viewport threshold) keeps the suite fast. A 2,000-line document covers
 * the non-viewport path, whose idle pass measures every line.
 * {@code -Dswt.styledtext.visibilityGate.report=<file>} appends one TSV row per case.
 */
public class Test_org_eclipse_swt_custom_StyledText_VisibilityGate {
	private static final int MIB = Integer.getInteger("swt.styledtext.visibilityGate.mib", 4).intValue();
	private static final int MEASURED_X = 60, REFERENCE_X = 600;
	private final List<Shell> shells = new ArrayList<>();

	@AfterEach
	public void tearDown() {
		shells.forEach(Shell::dispose);
		shells.clear();
	}

	@ParameterizedTest(name = "wrap={0} mib={1}")
	@CsvSource({ "false, -1", "true, -1", "false, 0", "true, 0" })
	public void test_hiddenTabDoesNoMeasurementThenCatchesUpPixelIdentical(boolean wrap, int mib) {
		String document = document(mib < 0 ? MIB : mib);
		StyledText hidden = tab(false, MEASURED_X);
		StyledText reference = tab(true, REFERENCE_X);
		hidden.setWordWrap(wrap);
		reference.setWordWrap(wrap);
		drain(hidden.getDisplay());
		assertFalse(hidden.isVisible(), "the StyledText tab is not selected");
		assertTrue(reference.isVisible());

		long layouts = StyledTextApiOps.layoutRequests(hidden);
		long t0 = System.nanoTime();
		hidden.setText(document);
		edits(hidden);
		long hiddenNanos = System.nanoTime() - t0;
		drain(hidden.getDisplay());
		assertEquals(layouts, StyledTextApiOps.layoutRequests(hidden), "no line measurement while hidden");
		assertFalse(idleRunning(hidden), "no idle measurement pass queued while hidden");

		long referenceLayouts = StyledTextApiOps.layoutRequests(reference);
		t0 = System.nanoTime();
		reference.setText(document);
		edits(reference);
		long referenceNanos = System.nanoTime() - t0;
		drain(reference.getDisplay());
		referenceLayouts = StyledTextApiOps.layoutRequests(reference) - referenceLayouts;

		CTabFolder folder = (CTabFolder) hidden.getParent();
		folder.setSelection(1);
		drain(hidden.getDisplay());
		assertTrue(hidden.isVisible());
		long caughtUp = StyledTextApiOps.layoutRequests(hidden) - layouts;
		assertTrue(caughtUp > 0 && caughtUp < (hidden.getLineCount() > 65_536 ? 4096 : hidden.getLineCount() * 4L),
				"catch-up measures the visible window (viewport) or the document once (small): " + caughtUp);
		assertState(reference, hidden);
		assertEquals(StyledTextApiOps.pixels(reference), StyledTextApiOps.pixels(hidden), "pixel-identical after catch-up");
		report("hidden-tab", wrap, document, hidden.getLineCount(), caughtUp, referenceLayouts, hiddenNanos, referenceNanos);
	}

	@Test
	public void test_minimizedShellDoesNoMeasurementThenCatchesUp() {
		String document = document(Math.min(MIB, 4));
		StyledText minimized = tab(true, MEASURED_X);
		StyledText reference = tab(true, REFERENCE_X);
		Shell shell = minimized.getShell();
		shell.setMinimized(true);
		drain(shell.getDisplay());
		assertTrue(shell.getMinimized());

		long layouts = StyledTextApiOps.layoutRequests(minimized);
		minimized.setText(document);
		edits(minimized);
		drain(shell.getDisplay());
		assertEquals(layouts, StyledTextApiOps.layoutRequests(minimized), "no line measurement while minimized");

		reference.setText(document);
		edits(reference);
		drain(reference.getDisplay());

		shell.setMinimized(false);
		drain(shell.getDisplay());
		assertState(reference, minimized);
		assertEquals(StyledTextApiOps.pixels(reference), StyledTextApiOps.pixels(minimized), "pixel-identical after restore");
		report("minimized", false, document, minimized.getLineCount(), StyledTextApiOps.layoutRequests(minimized) - layouts, -1, -1, -1);
	}

	private static void report(String name, boolean wrap, String document, int lines, long catchUpLayouts, long referenceLayouts,
			long hiddenNanos, long referenceNanos) {
		String file = System.getProperty("swt.styledtext.visibilityGate.report");
		if (file == null) {
			return;
		}
		String row = String.join("\t", name, Boolean.toString(wrap), Integer.toString(document.length()), Integer.toString(lines), "0",
				Long.toString(catchUpLayouts), Long.toString(referenceLayouts), Long.toString(hiddenNanos / 1_000_000),
				Long.toString(referenceNanos / 1_000_000)) + "\n";
		try {
			java.nio.file.Files.writeString(java.nio.file.Path.of(file), row, java.nio.file.StandardOpenOption.CREATE,
					java.nio.file.StandardOpenOption.APPEND);
		} catch (java.io.IOException e) {
			throw new java.io.UncheckedIOException(e);
		}
	}

	/**
	 * Shell(x,60,520,380) holding a CTabFolder whose second tab is the StyledText. The measured and the
	 * reference shells never overlap and stay on top: a covered shell is occluded, and the gate defers its paint
	 * by design.
	 */
	private StyledText tab(boolean selectText, int x) {
		// ON_TOP: other applications' windows must not occlude the measured shell during a batch run
		Shell shell = new Shell(SWT.SHELL_TRIM | SWT.ON_TOP);
		shells.add(shell);
		shell.setLayout(new FillLayout());
		CTabFolder folder = new CTabFolder(shell, SWT.BORDER);
		CTabItem first = new CTabItem(folder, SWT.NONE);
		first.setText("first");
		Label label = new Label(folder, SWT.NONE);
		label.setText("first tab");
		first.setControl(label);
		CTabItem second = new CTabItem(folder, SWT.NONE);
		second.setText("text");
		StyledText text = new StyledText(folder, SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL);
		second.setControl(text);
		folder.setSelection(selectText ? 1 : 0);
		shell.setBounds(x, 60, 520, 380);
		shell.open();
		drain(shell.getDisplay());
		return text;
	}

	/** Edits at the start, middle and end, a style, a selection, typing and a caret move. */
	private static void edits(StyledText t) {
		t.replaceTextRange(0, 4, "EDIT");
		int middle = t.getOffsetAtLine(t.getLineCount() / 2);
		t.replaceTextRange(middle, 0, "inserted line\n");
		t.append("\nappended tail");
		t.setStyleRange(new StyleRange(0, 4, t.getDisplay().getSystemColor(SWT.COLOR_RED), null, SWT.BOLD));
		t.setSelectionRange(2, 28); // selection without a scroll request (showSelection measures what it reveals)
		t.insert("typed");
		t.setCaretOffset(7);
	}

	private static void assertState(StyledText expected, StyledText actual) {
		assertEquals(expected.getCharCount(), actual.getCharCount());
		assertEquals(expected.getLineCount(), actual.getLineCount());
		assertEquals(expected.getCaretOffset(), actual.getCaretOffset());
		assertEquals(expected.getSelection(), actual.getSelection());
		assertEquals(expected.getTopIndex(), actual.getTopIndex());
		assertEquals(expected.getTopPixel(), actual.getTopPixel());
		assertEquals(expected.getHorizontalPixel(), actual.getHorizontalPixel());
		assertEquals(expected.getCaret().getLocation(), actual.getCaret().getLocation());
		assertEquals(expected.getVerticalBar().getMaximum(), actual.getVerticalBar().getMaximum());
		assertEquals(expected.getVerticalBar().getSelection(), actual.getVerticalBar().getSelection());
		assertEquals(expected.getHorizontalBar().getMaximum(), actual.getHorizontalBar().getMaximum());
	}

	/** A document of {@code mib} MiB, or 2,000 lines when {@code mib} is 0. */
	private static String document(int mib) {
		int chars = mib == 0 ? 2_000 * 55 : mib << 20;
		StringBuilder b = new StringBuilder(chars + 64);
		int i = 0;
		while (mib == 0 ? i < 2_000 : b.length() < chars) {
			b.append("line ").append(i++).append(" the quick brown fox jumps over the lazy dog\n");
		}
		return b.toString();
	}

	private static boolean idleRunning(StyledText t) {
		try {
			Field rendererField = StyledText.class.getDeclaredField("renderer");
			rendererField.setAccessible(true);
			Object renderer = rendererField.get(t);
			Field running = renderer.getClass().getDeclaredField("idleRunning");
			running.setAccessible(true);
			return running.getBoolean(renderer);
		} catch (ReflectiveOperationException e) {
			throw new IllegalStateException(e);
		}
	}

	private static void drain(Display display) {
		long deadline = System.currentTimeMillis() + 1500;
		while (System.currentTimeMillis() < deadline) {
			if (!display.readAndDispatch()) {
				long quiet = System.currentTimeMillis() + 150;
				boolean idle = true;
				while (System.currentTimeMillis() < quiet) {
					if (display.readAndDispatch()) {
						idle = false;
						break;
					}
					Thread.onSpinWait();
				}
				if (idle) {
					return;
				}
			}
		}
	}
}
