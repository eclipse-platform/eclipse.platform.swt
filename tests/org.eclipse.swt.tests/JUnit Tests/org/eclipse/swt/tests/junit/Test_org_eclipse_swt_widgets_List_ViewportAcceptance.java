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

import java.lang.reflect.*;

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.junit.jupiter.api.*;

/**
 * Acceptance tests of the win32 List viewport (docs/m3/LIST_MEDIATED_VIEWPORT.md): the native
 * list box holds at most one window of rows while the public API answers for every row. The
 * tests skip (never block) when the viewport is not active.
 */
public class Test_org_eclipse_swt_widgets_List_ViewportAcceptance {
	static final int MILLION = 1_000_000;
	Display display;
	Shell shell;

	@BeforeEach
	public void setUp () {
		Assumptions.assumeTrue ("win32".equals (SWT.getPlatform ()), "the List viewport is win32 only");
		display = Display.getDefault ();
		shell = new Shell (display);
		shell.setBounds (20, 20, 330, 420);
	}

	@AfterEach
	public void tearDown () {
		if (shell != null) shell.dispose ();
	}

	private List list (int style) {
		List list = new List (shell, style | SWT.BORDER | SWT.V_SCROLL);
		list.setBounds (5, 5, 300, 380);
		Assumptions.assumeTrue (viewport (list) != null, "List viewport mediation is not active in this build");
		return list;
	}

	static Object viewport (List list) {
		try {
			Field field = List.class.getDeclaredField ("viewport");
			field.setAccessible (true);
			return field.get (list);
		} catch (ReflectiveOperationException e) {
			return null;
		}
	}

	static int residentRows (List list) {
		try {
			Object viewport = viewport (list);
			Method method = viewport.getClass ().getDeclaredMethod ("residentCount");
			method.setAccessible (true);
			return (Integer) method.invoke (viewport);
		} catch (ReflectiveOperationException e) {
			throw new AssertionError (e);
		}
	}

	static String [] rows (int count) {
		String [] rows = new String [count];
		for (int i = 0; i < count; i++) rows [i] = "row " + i;
		return rows;
	}

	@Test
	public void millionItemsKeepOneWindowNative () {
		for (int style : new int [] {SWT.SINGLE, SWT.MULTI}) {
			List list = list (style);
			shell.open ();
			long start = System.nanoTime ();
			list.setItems (rows (MILLION));
			long setItems = System.nanoTime () - start;
			int page = list.getClientArea ().height / list.getItemHeight ();
			int bound = 2 * (page + 2) + 4 * Math.max (16, 2 * page);
			assertEquals (MILLION, list.getItemCount ());
			assertTrue (residentRows (list) <= bound, "native rows " + residentRows (list) + " > " + bound);
			assertEquals (MILLION - 1, list.getVerticalBar ().getMaximum ());
			start = System.nanoTime ();
			list.setTopIndex (MILLION - 1);
			assertEquals (MILLION - page, list.getTopIndex ());
			assertEquals ("row " + (MILLION - 1), list.getItem (MILLION - 1));
			list.setSelection (500_000);
			assertEquals (500_000, list.getSelectionIndex ());
			assertTrue (list.getTopIndex () <= 500_000 && 500_000 < list.getTopIndex () + page);
			list.add ("inserted", 10);
			list.remove (20, 29);
			assertEquals (MILLION - 9, list.getItemCount ());
			assertEquals ("inserted", list.getItem (10));
			assertEquals (500_000 - 9, list.getSelectionIndex ());
			long operations = System.nanoTime () - start;
			assertTrue (residentRows (list) <= bound, "native rows " + residentRows (list) + " > " + bound);
			assertTrue (operations < 2_000_000_000L, "top, selection, insert and remove at 1,000,000 rows took " + operations / 1_000_000 + " ms");
			assertTrue (setItems < 20_000_000_000L, "setItems (1,000,000) took " + setItems / 1_000_000 + " ms");
			if ((style & SWT.MULTI) != 0) {
				list.selectAll ();
				assertEquals (MILLION - 9, list.getSelectionCount ());
				list.deselect (0, 499_999);
				assertEquals (MILLION - 9 - 500_000, list.getSelectionCount ());
			}
			list.dispose ();
		}
	}

	@Test
	public void scrollingMovesOnlyTheChangedRows () {
		List list = list (SWT.MULTI);
		shell.open ();
		list.setItems (rows (100_000));
		list.setSelection (new int [] {5, 50_000, 99_999});
		for (int top = 0; top < 100_000; top += 997) {
			list.setTopIndex (top);
			assertEquals (Math.min (top, 100_000 - list.getClientArea ().height / list.getItemHeight ()), list.getTopIndex ());
		}
		assertArrayEquals (new int [] {5, 50_000, 99_999}, list.getSelectionIndices ());
		list.setTopIndex (50_000 - 3);
		assertTrue (list.isSelected (50_000));
		assertEquals ("row 50000", list.getItem (50_000));
	}

	@Test
	public void emptyAndTinyListsAnswerLikeTheListBox () {
		List list = list (SWT.SINGLE);
		assertEquals (-1, list.getFocusIndex ());
		assertEquals (0, list.getTopIndex ());
		list.setTopIndex (5);
		assertEquals (0, list.getTopIndex ());
		list.add ("a");
		assertEquals (0, list.getFocusIndex ());
		list.removeAll ();
		assertEquals (-1, list.getFocusIndex ());
		assertEquals (0, list.getVerticalBar ().getMaximum ());
	}
}
