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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

/**
 * Scale proof: every operation of the shared API catalogue runs on a
 * 1,000,001-line document (fixed line height, then word wrap) and must request
 * at most {@value #LAYOUT_BOUND} line layouts. Lines laid out are counted by the
 * renderer's layout-request counter. Per-operation layouts and wall time are
 * written to <code>-Dswt.styledtext.scaleReport=&lt;file&gt;</code> when set.
 * Test names are the coverage-table ids ("scale:fixed:" / "scale:wrap:" + op id).
 * Opt-in: <code>-Dswt.styledtext.scaleTest=true</code>.
 */
public class Test_org_eclipse_swt_custom_StyledText_ApiScale {
	static final int LINES = Integer.getInteger("swt.styledtext.scaleLines", 1_000_000);
	static final int LAYOUT_BOUND = 4096;
	private static Shell shell;
	private static StyledText text;
	private static final List<String> report = new ArrayList<>();

	@BeforeAll
	public static void setUp() {
		assumeTrue(Boolean.getBoolean("swt.styledtext.scaleTest"), "opt-in: -Dswt.styledtext.scaleTest=true (1M lines, about 1.2 GB heap)");
		shell = new Shell();
		shell.setSize(520, 380);
		text = new StyledText(shell, SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL | SWT.BORDER);
		text.setBounds(0, 0, 480, 320);
		shell.open();
		report.add("mode\top\tcost\tlayouts\tmillis\tlines");
	}

	@AfterAll
	public static void tearDown() throws IOException {
		if (shell == null) {
			return;
		}
		shell.dispose();
		String file = System.getProperty("swt.styledtext.scaleReport");
		if (file != null) {
			Files.write(Path.of(file), report, StandardCharsets.UTF_8);
		}
	}

	static String document(int lines) {
		StringBuilder b = new StringBuilder(lines * 34);
		for (int i = 0; i < lines; i++) {
			b.append("line ").append(i).append(" alpha beta\tgamma delta").append(i % 10 == 3 ? "\r\n" : "\n");
		}
		return b.toString();
	}

	@TestFactory
	Stream<DynamicTest> scale() {
		List<DynamicTest> tests = new ArrayList<>();
		for (String mode : new String[] { "fixed", "wrap" }) {
			tests.add(DynamicTest.dynamicTest("scale:" + mode + ":setText", () -> {
				// a fresh widget per mode, so the paged default content is measured in both modes
				text.dispose();
				text = new StyledText(shell, SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL | SWT.BORDER);
				text.setBounds(0, 0, 480, 320);
				text.setWordWrap(mode.equals("wrap"));
				measure(mode, "setText", "CHARS", t -> t.setText(document(LINES)));
				assertEquals("PagedContent", text.getContent().getClass().getSimpleName());
				assertEquals(LINES + 1, text.getLineCount());
			}));
			for (Test_org_eclipse_swt_custom_StyledText_ApiDifferential.ListenerOp l : Test_org_eclipse_swt_custom_StyledText_ApiDifferential.listenerOps()) {
				tests.add(DynamicTest.dynamicTest("scale:" + mode + ":" + l.id(), () -> measure(mode, l.id(), "VIEW", l.action())));
			}
			for (StyledTextApiOps.Op op : StyledTextApiOps.catalogue()) {
				tests.add(DynamicTest.dynamicTest("scale:" + mode + ":" + op.id(), () -> measure(mode, op.id(), op.cost().name(), op.action())));
			}
			for (Test_org_eclipse_swt_custom_StyledText_ApiDifferential.ListenerOp l : Test_org_eclipse_swt_custom_StyledText_ApiDifferential.removeListenerOps()) {
				tests.add(DynamicTest.dynamicTest("scale:" + mode + ":" + l.id(), () -> measure(mode, l.id(), "VIEW", l.action())));
			}
			tests.add(DynamicTest.dynamicTest("scale:" + mode + ":accessibility", () -> measure(mode, "accessibility", "VIEW", t -> StyledTextApiOps.accessibility(t))));
			tests.add(DynamicTest.dynamicTest("scale:" + mode + ":dropTargetEffect", () -> measure(mode, "dropTargetEffect", "VIEW", t -> StyledTextApiOps.dropEffect(t))));
			tests.add(DynamicTest.dynamicTest("scale:" + mode + ":snapshot-getters", () -> measure(mode, "snapshot-getters", "VIEW", t -> StyledTextApiOps.snapshot(t, false, false))));
		}
		return tests.stream();
	}

	private static void measure(String mode, String id, String cost, java.util.function.Consumer<StyledText> action) {
		long before = StyledTextApiOps.layoutRequests(text);
		long start = System.nanoTime();
		action.accept(text);
		text.update();
		Display display = text.getDisplay();
		long deadline = System.currentTimeMillis() + 3000;
		while (System.currentTimeMillis() < deadline && display.readAndDispatch()) {
			// drain bounded idle work
		}
		long millis = (System.nanoTime() - start) / 1_000_000;
		long layouts = StyledTextApiOps.layoutRequests(text) - before;
		report.add(String.join("\t", mode, id, cost, Long.toString(layouts), Long.toString(millis), Integer.toString(text.getLineCount())));
		assertTrue(text.getLineCount() > LINES / 2, "document stays huge");
		assertTrue(layouts <= LAYOUT_BOUND, String.format(Locale.ROOT, "%s %s laid out %d lines (bound %d)", mode, id, layouts, LAYOUT_BOUND));
	}
}
