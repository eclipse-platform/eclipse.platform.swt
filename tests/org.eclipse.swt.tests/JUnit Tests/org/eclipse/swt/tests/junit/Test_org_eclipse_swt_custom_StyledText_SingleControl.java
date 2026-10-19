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
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.eclipse.swt.SWT;
import org.eclipse.swt.accessibility.ACC;
import org.eclipse.swt.accessibility.AccessibleControlEvent;
import org.eclipse.swt.accessibility.AccessibleControlListener;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * One-widget law: StyledText is the viewport. In viewport mode (a huge document,
 * or the SWT.VIRTUAL style bit) there is no child control or helper surface;
 * focus, keyboard traversal and the accessible role stay on the StyledText.
 */
public class Test_org_eclipse_swt_custom_StyledText_SingleControl {
	private Shell shell;
	private Button before;
	private Button after;

	@BeforeEach
	public void setUp() {
		shell = new Shell();
		shell.setLayout(new FillLayout());
		before = new Button(shell, SWT.PUSH);
		before.setText("before");
	}

	@AfterEach
	public void tearDown() {
		shell.dispose();
	}

	@ParameterizedTest
	@ValueSource(ints = { 0, SWT.VIRTUAL })
	public void test_focusTraversalAndAccessibleRoleStayOnStyledText(int extraStyle) {
		StyledText text = new StyledText(shell, SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL | extraStyle);
		after = new Button(shell, SWT.PUSH);
		after.setText("after");
		shell.setSize(500, 300);
		shell.open();
		StringBuilder b = new StringBuilder();
		int lines = extraStyle == 0 ? 120_000 : 2_000; // over the threshold, or forced by SWT.VIRTUAL
		for (int i = 0; i < lines; i++) {
			b.append("line ").append(i).append('\n');
		}
		text.setText(b.toString());
		text.setWordWrap(true);
		text.setTopIndex(lines / 2);
		assertEquals(extraStyle, text.getStyle() & SWT.VIRTUAL, "style bit is the behaviour switch");
		assertEquals(0, text.getChildren().length, "no child control or helper surface inside StyledText");

		assertTrue(text.forceFocus(), "StyledText takes focus itself");
		drain();
		assertSame(text, shell.getDisplay().getFocusControl(), "focus is on StyledText, never on a child");

		// keyboard traversal leaves StyledText for the next sibling and returns, never entering a child
		text.setEditable(false); // read-only StyledText lets TAB traverse
		assertTrue(text.traverse(SWT.TRAVERSE_TAB_NEXT), "traversal out of StyledText");
		drain();
		Control focus = shell.getDisplay().getFocusControl();
		assertNotEquals(text, focus);
		assertTrue(focus == after || focus == before, "focus moved to a sibling: " + focus);
		assertTrue(focus.traverse(focus == after ? SWT.TRAVERSE_TAB_PREVIOUS : SWT.TRAVERSE_TAB_NEXT));
		drain();
		assertSame(text, shell.getDisplay().getFocusControl(), "traversal returns to StyledText itself");

		AccessibleControlListener control = StyledTextApiOps.accessibleListener(text, "accessibleControlListeners", AccessibleControlListener.class);
		AccessibleControlEvent e = new AccessibleControlEvent(text.getAccessible());
		e.childID = ACC.CHILDID_SELF;
		control.getRole(e);
		assertEquals(ACC.ROLE_TEXT, e.detail, "accessible role is on StyledText");
		e = new AccessibleControlEvent(text.getAccessible());
		e.childID = ACC.CHILDID_SELF;
		control.getChildCount(e);
		assertEquals(0, e.detail, "no accessible children");
	}

	private void drain() {
		Display display = shell.getDisplay();
		long deadline = System.currentTimeMillis() + 1000;
		while (System.currentTimeMillis() < deadline && display.readAndDispatch()) {
			// settle focus events
		}
	}
}
