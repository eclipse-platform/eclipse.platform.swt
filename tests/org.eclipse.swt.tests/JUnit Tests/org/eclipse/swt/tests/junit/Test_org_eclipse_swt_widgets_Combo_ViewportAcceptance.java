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

import static org.junit.jupiter.api.Assertions.*;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Acceptance of the Combo viewport at sizes a recorded differential cannot replay against the stock
 * combo: the drop-down list box holds one window of rows whatever the item count, and keys, type-ahead,
 * the edit-text match, scrolling and cancelling a drop reach rows far outside that window with the
 * stock combo's rules (docs/m3/LIST_MEDIATED_VIEWPORT.md, "Combo"). Input is sent as window messages,
 * so it runs on a hidden desktop.
 */
public class Test_org_eclipse_swt_widgets_Combo_ViewportAcceptance {
	static final int MILLION = 1_000_000;
	/* the most native rows a window may hold: the reach plus overscan on both sides */
	static final int WINDOW_LIMIT = 2400;
	Display display;
	Shell shell;

	@BeforeEach
	public void setUp () {
		Assumptions.assumeTrue ("win32".equals (SWT.getPlatform ()), "the Combo viewport is win32 only");
		display = Display.getDefault ();
		shell = new Shell (display);
		shell.setBounds (20, 20, 330, 420);
		shell.open ();
		Assumptions.assumeTrue (Test_org_eclipse_swt_widgets_Combo_ViewportDifferential.viewportActive (shell), "Combo viewport mediation is not active");
	}

	@AfterEach
	public void tearDown () {
		if (shell != null) shell.dispose ();
	}

	private Combo combo (int style, int count) {
		Combo combo = new Combo (shell, style);
		combo.setBounds (5, 5, 300, 30);
		combo.setVisibleItemCount (20);
		String [] items = new String [count];
		for (int i = 0; i < count; i++) items [i] = Test_org_eclipse_swt_widgets_Combo_ViewportDifferential.text (i);
		combo.setItems (items);
		combo.setFocus ();
		settle ();
		return combo;
	}

	private void settle () {
		Test_org_eclipse_swt_widgets_Combo_ViewportDifferential.flush (display);
	}

	private static long nativeCount (Combo combo) {
		return Test_org_eclipse_swt_widgets_Combo_ViewportDifferential.Native.send (Test_org_eclipse_swt_widgets_Combo_ViewportDifferential.Native.list (combo), 0x18b, 0, 0);
	}

	@Test
	public void millionItemsKeepOneWindowNative () {
		Combo combo = combo (SWT.DROP_DOWN | SWT.READ_ONLY, MILLION);
		assertEquals (MILLION, combo.getItemCount ());
		assertTrue (nativeCount (combo) <= WINDOW_LIMIT, "native rows " + nativeCount (combo));
		combo.select (MILLION - 1);
		assertEquals (MILLION - 1, combo.getSelectionIndex ());
		assertEquals (combo.getItem (MILLION - 1), combo.getText ());
		combo.select (MILLION / 2);
		assertEquals (combo.getItem (MILLION / 2), combo.getText ());
		assertEquals (MILLION / 2 + 6, combo.indexOf (combo.getItem (MILLION / 2 + 6)));
		combo.setListVisible (true);
		settle ();
		assertTrue (combo.getListVisible ());
		assertTrue (nativeCount (combo) <= WINDOW_LIMIT, "native rows while dropped " + nativeCount (combo));
		assertEquals ("0.." + (MILLION - 1) + "/20@" + (MILLION / 2), Test_org_eclipse_swt_widgets_Combo_ViewportDifferential.Native.bar (
			Test_org_eclipse_swt_widgets_Combo_ViewportDifferential.Native.list (combo)));
		combo.setListVisible (false);
		combo.remove (0, MILLION / 4);
		assertEquals (MILLION - MILLION / 4 - 1, combo.getItemCount ());
		assertEquals (MILLION / 2 - MILLION / 4 - 1, combo.getSelectionIndex ());
		assertTrue (nativeCount (combo) <= WINDOW_LIMIT);
	}

	@Test
	public void keysAndTypeAheadReachRowsOutsideTheWindow () {
		int count = 100_000;
		Combo combo = combo (SWT.DROP_DOWN | SWT.READ_ONLY, count);
		combo.select (50_000);
		Test_org_eclipse_swt_widgets_Combo_ViewportDifferential.Native.key (combo, "HOME");
		settle ();
		assertEquals (0, combo.getSelectionIndex ());
		Test_org_eclipse_swt_widgets_Combo_ViewportDifferential.Native.key (combo, "END");
		settle ();
		assertEquals (count - 1, combo.getSelectionIndex ());
		assertEquals (combo.getItem (count - 1), combo.getText ());
		/* type-ahead wraps from the last row to the first row starting with 'b' */
		Test_org_eclipse_swt_widgets_Combo_ViewportDifferential.Native.character (combo, 'b');
		settle ();
		assertEquals (1, combo.getSelectionIndex ());
		combo.select (count / 2);
		Test_org_eclipse_swt_widgets_Combo_ViewportDifferential.Native.key (combo, "NEXT");
		settle ();
		int afterNext = combo.getSelectionIndex ();
		assertTrue (afterNext > count / 2 && afterNext < count / 2 + 30, "Next moved to " + afterNext);
		Test_org_eclipse_swt_widgets_Combo_ViewportDifferential.Native.key (combo, "UP");
		settle ();
		assertEquals (afterNext - 1, combo.getSelectionIndex ());
		assertTrue (nativeCount (combo) <= WINDOW_LIMIT);
	}

	@Test
	public void droppedListScrollsAndCancelsOverLogicalRows () {
		int count = 100_000;
		Combo combo = combo (SWT.DROP_DOWN | SWT.READ_ONLY, count);
		combo.select (10);
		combo.setListVisible (true);
		settle ();
		long list = Test_org_eclipse_swt_widgets_Combo_ViewportDifferential.Native.list (combo);
		/* the thumb to the end: the list shows the last page, far outside the window it dropped with */
		Test_org_eclipse_swt_widgets_Combo_ViewportDifferential.Native.send (list, 0x115, 7, 0);
		settle ();
		assertEquals ("0.." + (count - 1) + "/20@" + (count - 20), Test_org_eclipse_swt_widgets_Combo_ViewportDifferential.Native.bar (list));
		assertEquals (10, combo.getSelectionIndex (), "the selection stays while the list scrolls");
		/* hovering selects the row under the pointer, Escape puts the selection of the drop back */
		Test_org_eclipse_swt_widgets_Combo_ViewportDifferential.Native.mouse (combo, 3, false);
		settle ();
		assertEquals (count - 17, combo.getSelectionIndex ());
		Test_org_eclipse_swt_widgets_Combo_ViewportDifferential.Native.key (combo, "ESCAPE");
		settle ();
		assertFalse (combo.getListVisible ());
		assertEquals (10, combo.getSelectionIndex ());
		assertEquals (combo.getItem (10), combo.getText ());
		/* a click far away commits that row with Modify and Selection */
		StringBuilder events = new StringBuilder ();
		ViewportDifferential.listen (combo, events, "M", SWT.Modify);
		ViewportDifferential.listen (combo, events, "S", SWT.Selection);
		combo.setListVisible (true);
		settle ();
		Test_org_eclipse_swt_widgets_Combo_ViewportDifferential.Native.send (list, 0x115, 5 | ((long) 60_000 << 16), 0);
		settle ();
		Test_org_eclipse_swt_widgets_Combo_ViewportDifferential.Native.mouse (combo, 2, true);
		settle ();
		assertFalse (combo.getListVisible ());
		assertEquals (60_002, combo.getSelectionIndex ());
		assertEquals ("M S ", events.toString ());
	}

	@Test
	public void editTextMatchFindsTheFirstLogicalRow () {
		int count = 100_000;
		Combo combo = combo (SWT.DROP_DOWN, count);
		/* "elder 9999" is a prefix of row 99995 only ("elder 9999x" rows are 99995) */
		combo.setText ("ELDER 9999");
		combo.setListVisible (true);
		settle ();
		assertEquals (99_995, combo.getSelectionIndex ());
		assertEquals (combo.getItem (99_995), combo.getText ());
		assertEquals ("0.." + (count - 1) + "/20@" + (count - 20), Test_org_eclipse_swt_widgets_Combo_ViewportDifferential.Native.bar (
			Test_org_eclipse_swt_widgets_Combo_ViewportDifferential.Native.list (combo)), "the last page is on top");
		combo.setListVisible (false);
		settle ();
		combo.setText ("zz");
		combo.setListVisible (true);
		settle ();
		assertEquals (-1, combo.getSelectionIndex ());
		combo.setListVisible (false);
	}
}
