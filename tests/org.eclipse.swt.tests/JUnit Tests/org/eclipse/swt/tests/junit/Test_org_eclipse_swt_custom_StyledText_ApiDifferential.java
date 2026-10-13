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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.BidiSegmentListener;
import org.eclipse.swt.custom.CaretListener;
import org.eclipse.swt.custom.ExtendedModifyListener;
import org.eclipse.swt.custom.LineBackgroundListener;
import org.eclipse.swt.custom.LineStyleListener;
import org.eclipse.swt.custom.MovementListener;
import org.eclipse.swt.custom.PaintObjectListener;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.custom.StyledTextPrintOptions;
import org.eclipse.swt.custom.TextChangeListener;
import org.eclipse.swt.custom.TextChangedEvent;
import org.eclipse.swt.custom.TextChangingEvent;
import org.eclipse.swt.custom.VerifyKeyListener;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.HTMLTransfer;
import org.eclipse.swt.dnd.RTFTransfer;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.printing.Printer;
import org.eclipse.swt.printing.PrinterData;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

/**
 * Differential API proof: every StyledText API operation of the shared catalogue
 * runs on a widget with the default paged content and on a widget with the
 * gap-buffer content (<code>content=gap</code>). After each operation the public
 * state (text, selection, caret, scroll, styles, line attributes, geometry), the
 * listener event transcripts and the rendered pixels (Control.print) must be
 * identical. Test names are the coverage-table ids ("diff:" + op id).
 */
public class Test_org_eclipse_swt_custom_StyledText_ApiDifferential {
	private static final String CONTENT_PROPERTY = "org.eclipse.swt.custom.StyledText.content";
	private static Shell pagedShell;
	private static Shell gapShell;
	private static StyledText paged;
	private static StyledText gap;
	private static final List<String> pagedLog = new ArrayList<>();
	private static final List<String> gapLog = new ArrayList<>();

	@BeforeAll
	public static void setUp() {
		String previous = System.getProperty(CONTENT_PROPERTY);
		try {
			System.clearProperty(CONTENT_PROPERTY);
			pagedShell = new Shell();
			paged = new StyledText(pagedShell, SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL | SWT.BORDER);
			System.setProperty(CONTENT_PROPERTY, "gap");
			gapShell = new Shell();
			gap = new StyledText(gapShell, SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL | SWT.BORDER);
		} finally {
			if (previous == null) {
				System.clearProperty(CONTENT_PROPERTY);
			} else {
				System.setProperty(CONTENT_PROPERTY, previous);
			}
		}
		for (Shell shell : new Shell[] { pagedShell, gapShell }) {
			shell.setBounds(60, 60, 520, 380);
			shell.open();
		}
		paged.setBounds(0, 0, 480, 320);
		gap.setBounds(0, 0, 480, 320);
		String text = document(300);
		paged.setText(text);
		gap.setText(text);
		flush(paged);
		flush(gap);
	}

	@AfterAll
	public static void tearDown() {
		pagedShell.dispose();
		gapShell.dispose();
	}

	static String document(int lines) {
		StringBuilder b = new StringBuilder();
		for (int i = 0; i < lines; i++) {
			b.append(String.format("line %04d alpha beta\tgamma delta epsilon", i));
			if (i % 13 == 5) {
				b.append(" a much longer line that wraps when word wrap is on, with several more words to measure");
			}
			b.append(i % 10 == 3 ? "\r\n" : "\n");
			if (i % 17 == 8) {
				b.append('\n');
			}
		}
		return b.toString();
	}

	static void flush(StyledText t) {
		t.update();
		Display display = t.getDisplay();
		long deadline = System.currentTimeMillis() + 3000;
		while (System.currentTimeMillis() < deadline && display.readAndDispatch()) {
			// drain
		}
	}

	@TestFactory
	Stream<DynamicTest> differential() {
		List<DynamicTest> tests = new ArrayList<>();
		for (ListenerOp l : listenerOps()) {
			tests.add(DynamicTest.dynamicTest("diff:" + l.id, () -> both(l.id, l.action)));
		}
		for (StyledTextApiOps.Op op : StyledTextApiOps.catalogue()) {
			if (op.id().equals("setContent-custom")) {
				tests.add(DynamicTest.dynamicTest("diff:accessibility", () -> compare("accessibility", StyledTextApiOps::accessibility)));
				tests.add(DynamicTest.dynamicTest("diff:dropTargetEffect", () -> compare("dropTargetEffect", StyledTextApiOps::dropEffect)));
				tests.add(DynamicTest.dynamicTest("diff:rtf-html-writers", () -> compare("rtf-html-writers", Test_org_eclipse_swt_custom_StyledText_ApiDifferential::writers)));
				tests.add(DynamicTest.dynamicTest("diff:printOptions", () -> compare("printOptions", Test_org_eclipse_swt_custom_StyledText_ApiDifferential::printing)));
			}
			tests.add(DynamicTest.dynamicTest("diff:" + op.id(), () -> both(op.id(), op.action())));
		}
		for (ListenerOp l : removeListenerOps()) {
			tests.add(DynamicTest.dynamicTest("diff:" + l.id, () -> both(l.id, l.action)));
		}
		return tests.stream();
	}

	private static void both(String id, Consumer<StyledText> action) {
		pagedLog.clear();
		gapLog.clear();
		RuntimeException pagedError = run(paged, action);
		RuntimeException gapError = run(gap, action);
		assertEquals(gapError == null ? null : gapError.getClass(), pagedError == null ? null : pagedError.getClass(), id + " exception");
		if (!id.startsWith("autoscroll-")) {
			// autoscroll ticks are timer-driven, so their event count is timing-dependent; state is restored and compared
			assertEquals(String.join("\n", gapLog), String.join("\n", pagedLog), id + " events");
		}
		assertEquals(StyledTextApiOps.snapshot(gap, true, true), StyledTextApiOps.snapshot(paged, true, true), id + " state");
	}

	private static RuntimeException run(StyledText t, Consumer<StyledText> action) {
		try {
			action.accept(t);
			flush(t);
			return null;
		} catch (RuntimeException e) {
			flush(t);
			return e;
		}
	}

	private interface Probe {
		String apply(StyledText t);
	}

	private static void compare(String id, Probe probe) {
		String a = probe.apply(gap);
		flush(gap);
		String b = probe.apply(paged);
		flush(paged);
		assertNotNull(a, id);
		assertEquals(a, b, id);
		assertEquals(StyledTextApiOps.snapshot(gap, true, true), StyledTextApiOps.snapshot(paged, true, true), id + " state");
	}

	/** RTF and HTML clipboard writers (StyledTextWriterBase subclasses) over a styled selection. */
	static String writers(StyledText t) {
		t.setStyleRange(StyledTextApiOps.features(t, StyledTextApiOps.inLine(t, 0.5, 0), 5));
		t.setSelectionRange(StyledTextApiOps.off(t, 0.5), 40);
		t.copy();
		Clipboard clipboard = new Clipboard(t.getDisplay());
		try {
			String rtf = (String) clipboard.getContents(RTFTransfer.getInstance());
			String html = (String) clipboard.getContents(HTMLTransfer.getInstance());
			assertTrue(rtf != null && rtf.startsWith("{\\rtf1"), "rtf");
			assertTrue(html != null && html.contains("<"), "html");
			return rtf.hashCode() + "/" + html.hashCode();
		} finally {
			clipboard.dispose();
		}
	}

	/**
	 * StyledTextPrintOptions and print(Printer, options): the job runs only with
	 * -Dswt.styledtext.printJob=true and a printer (print to file, no dialog).
	 */
	static String printing(StyledText t) {
		StyledTextPrintOptions options = new StyledTextPrintOptions();
		options.header = "head" + StyledTextPrintOptions.SEPARATOR + "<page>";
		options.footer = "foot";
		options.jobName = "styledtext-api";
		options.printTextForeground = true;
		options.printTextBackground = true;
		options.printTextFontStyle = true;
		options.printLineBackground = true;
		options.printLineNumbers = true;
		options.lineLabels = new String[] { "a", "b" };
		if (!Boolean.getBoolean("swt.styledtext.printJob") || Printer.getPrinterList().length == 0) {
			return "options-only";
		}
		PrinterData data = Printer.getDefaultPrinterData();
		data.printToFile = true;
		data.fileName = System.getProperty("java.io.tmpdir") + "/styledtext-api-" + t.getContent().getClass().getSimpleName() + ".prn";
		data.scope = PrinterData.PAGE_RANGE;
		data.startPage = 1;
		data.endPage = 1;
		Printer printer = new Printer(data);
		try {
			t.print(printer, options).run();
			t.print(printer).run();
			return "printed";
		} finally {
			printer.dispose();
		}
	}

	record ListenerOp(String id, Consumer<StyledText> action) {
	}

	private static List<String> log(StyledText t) {
		return t == paged ? pagedLog : gapLog;
	}

	private static final List<Object[]> registered = new ArrayList<>();

	private static <T> void keep(StyledText t, String kind, T listener) {
		registered.add(new Object[] { t, kind, listener });
	}

	@SuppressWarnings("unchecked")
	private static <T> T listener(StyledText t, String kind) {
		for (Object[] r : registered) {
			if (r[0] == t && r[1].equals(kind)) {
				return (T) r[2];
			}
		}
		throw new IllegalStateException(kind);
	}

	static List<ListenerOp> listenerOps() {
		List<ListenerOp> ops = new ArrayList<>();
		ops.add(new ListenerOp("addExtendedModifyListener", t -> { ExtendedModifyListener l = e -> log(t).add("extmod " + e.start + "," + e.length + "," + e.replacedText); keep(t, "extmod", l); t.addExtendedModifyListener(l); }));
		ops.add(new ListenerOp("addModifyListener", t -> { ModifyListener l = e -> log(t).add("modify"); keep(t, "modify", l); t.addModifyListener(l); }));
		ops.add(new ListenerOp("addVerifyListener", t -> { VerifyListener l = e -> log(t).add("verify " + e.start + "," + e.end + "," + e.text); keep(t, "verify", l); t.addVerifyListener(l); }));
		ops.add(new ListenerOp("addVerifyKeyListener", t -> { VerifyKeyListener l = e -> log(t).add("verifykey " + (int) e.character); keep(t, "verifykey", l); t.addVerifyKeyListener(l); }));
		ops.add(new ListenerOp("addSelectionListener", t -> { SelectionListener l = SelectionListener.widgetSelectedAdapter(e -> log(t).add("selection " + e.x + "," + e.y)); keep(t, "selection", l); t.addSelectionListener(l); }));
		ops.add(new ListenerOp("addCaretListener", t -> { CaretListener l = e -> log(t).add("caret " + e.caretOffset); keep(t, "caret", l); t.addCaretListener(l); }));
		ops.add(new ListenerOp("addWordMovementListener", t -> { MovementListener l = new MovementListener() {
			@Override
			public void getNextOffset(org.eclipse.swt.custom.MovementEvent e) {
				log(t).add("next " + e.offset + "," + e.newOffset + "," + e.movement);
			}

			@Override
			public void getPreviousOffset(org.eclipse.swt.custom.MovementEvent e) {
				log(t).add("prev " + e.offset + "," + e.newOffset + "," + e.movement);
			}
		}; keep(t, "movement", l); t.addWordMovementListener(l); }));
		ops.add(new ListenerOp("addTextChangeListener(content)", t -> { TextChangeListener l = new TextChangeListener() {
			@Override
			public void textChanging(TextChangingEvent e) {
				log(t).add("changing " + e.start + "," + e.replaceCharCount + "," + e.newCharCount + "," + e.replaceLineCount + "," + e.newLineCount);
			}

			@Override
			public void textChanged(TextChangedEvent e) {
				log(t).add("changed");
			}

			@Override
			public void textSet(TextChangedEvent e) {
				log(t).add("set");
			}
		}; keep(t, "content", l); t.getContent().addTextChangeListener(l); }));
		ops.add(new ListenerOp("addLineBackgroundListener", t -> { LineBackgroundListener l = e -> { if (e.lineText.contains("5 ")) e.lineBackground = t.getDisplay().getSystemColor(SWT.COLOR_INFO_BACKGROUND); }; keep(t, "linebg", l); t.addLineBackgroundListener(l); }));
		ops.add(new ListenerOp("addBidiSegmentListener", t -> { BidiSegmentListener l = e -> { e.segments = e.lineText.length() > 6 ? new int[] { 0, 5 } : null; }; keep(t, "bidi", l); t.addBidiSegmentListener(l); }));
		ops.add(new ListenerOp("addPaintObjectListener", t -> { PaintObjectListener l = e -> e.gc.drawRectangle(e.x, e.y - e.ascent, 4, e.ascent + e.descent - 1); keep(t, "paintobj", l); t.addPaintObjectListener(l); }));
		ops.add(new ListenerOp("addLineStyleListener", t -> { LineStyleListener l = e -> { if (e.lineText.length() > 4) { StyleRange s = new StyleRange(e.lineOffset, 4, null, null, SWT.BOLD); e.styles = new StyleRange[] { s }; } }; keep(t, "linestyle", l); t.addLineStyleListener(l); }));
		ops.add(new ListenerOp("removeLineStyleListener", t -> t.removeLineStyleListener(listener(t, "linestyle"))));
		return ops;
	}

	static List<ListenerOp> removeListenerOps() {
		List<ListenerOp> ops = new ArrayList<>();
		ops.add(new ListenerOp("removeExtendedModifyListener", t -> t.removeExtendedModifyListener(listener(t, "extmod"))));
		ops.add(new ListenerOp("removeModifyListener", t -> t.removeModifyListener(listener(t, "modify"))));
		ops.add(new ListenerOp("removeVerifyListener", t -> t.removeVerifyListener(listener(t, "verify"))));
		ops.add(new ListenerOp("removeVerifyKeyListener", t -> t.removeVerifyKeyListener(listener(t, "verifykey"))));
		ops.add(new ListenerOp("removeSelectionListener", t -> t.removeSelectionListener(listener(t, "selection"))));
		ops.add(new ListenerOp("removeCaretListener", t -> t.removeCaretListener(listener(t, "caret"))));
		ops.add(new ListenerOp("removeWordMovementListener", t -> t.removeWordMovementListener(listener(t, "movement"))));
		ops.add(new ListenerOp("removeLineBackgroundListener", t -> t.removeLineBackgroundListener(listener(t, "linebg"))));
		ops.add(new ListenerOp("removeBidiSegmentListener", t -> t.removeBidiSegmentListener(listener(t, "bidi"))));
		ops.add(new ListenerOp("removePaintObjectListener", t -> t.removePaintObjectListener(listener(t, "paintobj"))));
		ops.add(new ListenerOp("after-remove:type-keys", t -> { t.setCaretOffset(StyledTextApiOps.off(t, 0.5)); StyledTextApiOps.key(t, 'r', 'r'); }));
		return ops;
	}
}
