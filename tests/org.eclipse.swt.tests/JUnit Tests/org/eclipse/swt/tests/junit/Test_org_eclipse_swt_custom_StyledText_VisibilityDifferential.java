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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Stream;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.custom.TextChangeListener;
import org.eclipse.swt.custom.TextChangedEvent;
import org.eclipse.swt.custom.TextChangingEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.swt.widgets.Shell;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

/**
 * Visibility law, API side: every catalogued StyledText operation gives the same
 * exception, events and observable state on a StyledText in a hidden CTabFolder tab
 * as on a visible one (same bounds); after the tab is shown the pixels match too.
 * <p>
 * Not compared, because user input cannot reach a hidden widget: {@code setFocus}
 * (a hidden control takes no focus) and the autoscroll timer bodies (stopped while
 * hidden by design). The scroll bars of a hidden widget are presentation: their
 * extent settles when it is shown, so they are compared after the show only.
 * Not run: {@code setLineSpacingProvider} and its reset. With a spacing provider,
 * SWT gives a line a different height when the idle pass measures it with a
 * TextLayout than when {@code getLineHeight} takes the default-height shortcut, so
 * line pixels depend on the order of measurement, which a hidden widget changes.
 * Not compared: {@code block:invokeAction:SELECT_ALL}. Its block width is the widest
 * measured line, and a measured line width includes the caret width at the time it
 * was measured; the hidden widget measures that line later (742 vs 743 px observed).
 */
public class Test_org_eclipse_swt_custom_StyledText_VisibilityDifferential {
	private static final Set<String> NOT_COMPARED = Set.of("setFocus", "autoscroll-down:run", "autoscroll-up:run",
			"autoscroll-right:run", "autoscroll-left:run", "setLineSpacingProvider", "setLineSpacingProvider-reset",
			"block:invokeAction:SELECT_ALL");
	private static Shell visibleShell;
	private static Shell hiddenShell;
	private static StyledText visible;
	private static StyledText hidden;
	private static final List<String> visibleLog = new ArrayList<>();
	private static final List<String> hiddenLog = new ArrayList<>();

	@BeforeAll
	public static void setUp() {
		visible = tab(true);
		hidden = tab(false);
		visibleShell = visible.getShell();
		hiddenShell = hidden.getShell();
		String text = Test_org_eclipse_swt_custom_StyledText_ApiDifferential.document(300);
		for (StyledText t : new StyledText[] { visible, hidden }) {
			listen(t, t == visible ? visibleLog : hiddenLog);
			t.setText(text);
			Test_org_eclipse_swt_custom_StyledText_ApiDifferential.flush(t);
		}
		assertTrue(visible.isVisible());
		assertFalse(hidden.isVisible());
		assertEquals(visible.getBounds(), hidden.getBounds(), "same client area for both widgets");
	}

	@AfterAll
	public static void tearDown() {
		visibleShell.dispose();
		hiddenShell.dispose();
	}

	private static StyledText tab(boolean show) {
		Shell shell = new Shell();
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
		folder.setSelection(show ? 1 : 0);
		shell.setBounds(60, 60, 520, 380);
		shell.open();
		if (!show) {
			// a hidden tab keeps its control at zero size; give it the visible tab's bounds
			text.setBounds(folder.getClientArea());
		}
		Test_org_eclipse_swt_custom_StyledText_ApiDifferential.flush(text);
		return text;
	}

	private static void listen(StyledText t, List<String> log) {
		t.addModifyListener(e -> log.add("modify"));
		t.addExtendedModifyListener(e -> log.add("extmod " + e.start + "," + e.length + "," + e.replacedText));
		t.addVerifyListener(e -> log.add("verify " + e.start + "," + e.end + "," + e.text));
		t.addSelectionListener(org.eclipse.swt.events.SelectionListener.widgetSelectedAdapter(e -> log.add("selection " + e.x + "," + e.y)));
		t.addCaretListener(e -> log.add("caret " + e.caretOffset));
		t.getContent().addTextChangeListener(new TextChangeListener() {
			@Override
			public void textChanging(TextChangingEvent e) {
				log.add("changing " + e.start + "," + e.replaceCharCount + "," + e.newCharCount);
			}

			@Override
			public void textChanged(TextChangedEvent e) {
				log.add("changed");
			}

			@Override
			public void textSet(TextChangedEvent e) {
				log.add("set");
			}
		});
	}

	@TestFactory
	Stream<DynamicTest> hiddenEqualsVisible() {
		List<DynamicTest> tests = new ArrayList<>();
		for (StyledTextApiOps.Op op : StyledTextApiOps.catalogue()) {
			if (NOT_COMPARED.contains(op.id()) || op.id().equals("setContent-custom")) {
				continue; // setContent-custom replaces the content and its text change listener
			}
			tests.add(DynamicTest.dynamicTest("hidden:" + op.id(), () -> both(op.id(), op.action())));
		}
		tests.add(DynamicTest.dynamicTest("hidden:show-then-pixels", () -> {
			assertFalse(hidden.isVisible());
			((CTabFolder) hidden.getParent()).setSelection(1);
			Test_org_eclipse_swt_custom_StyledText_ApiDifferential.flush(hidden);
			assertTrue(hidden.isVisible());
			assertEquals(state(visible, true), state(hidden, true), "state and scroll bars after show");
			assertEquals(StyledTextApiOps.pixels(visible), StyledTextApiOps.pixels(hidden), "pixels after show");
		}));
		return tests.stream();
	}

	private static void both(String id, Consumer<StyledText> action) {
		visibleLog.clear();
		hiddenLog.clear();
		RuntimeException visibleError = run(visible, action);
		RuntimeException hiddenError = run(hidden, action);
		assertEquals(visibleError == null ? null : visibleError.getClass(), hiddenError == null ? null : hiddenError.getClass(), id + " exception");
		assertEquals(String.join("\n", visibleLog), String.join("\n", hiddenLog), id + " events");
		assertEquals(state(visible, false), state(hidden, false), id + " state");
	}

	private static RuntimeException run(StyledText t, Consumer<StyledText> action) {
		try {
			action.accept(t);
			Test_org_eclipse_swt_custom_StyledText_ApiDifferential.flush(t);
			return null;
		} catch (RuntimeException e) {
			Test_org_eclipse_swt_custom_StyledText_ApiDifferential.flush(t);
			return e;
		}
	}

	/** Public state, geometry queries, the caret's location and, when asked, both scroll bars. */
	private static String state(StyledText t, boolean bars) {
		StringBuilder s = new StringBuilder(StyledTextApiOps.snapshot(t, true, false));
		s.append(" caretLoc=").append(t.getCaret() == null ? null : t.getCaret().getLocation());
		for (ScrollBar bar : bars ? new ScrollBar[] { t.getVerticalBar(), t.getHorizontalBar() } : new ScrollBar[0]) {
			s.append(" bar=").append(bar.getMinimum()).append('/').append(bar.getMaximum()).append('/').append(bar.getSelection())
					.append('/').append(bar.getThumb()).append('/').append(bar.getVisible());
		}
		return s.toString();
	}
}
