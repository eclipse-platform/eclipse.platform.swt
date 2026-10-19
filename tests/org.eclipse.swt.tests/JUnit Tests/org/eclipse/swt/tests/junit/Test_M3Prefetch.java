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

import static org.junit.jupiter.api.Assertions.*;

import java.util.BitSet;
import java.util.function.*;

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.junit.jupiter.api.*;

/**
 * Contract of M3Prefetch (enhanced M3 API, leaf 9). The oracle replays the events into a set of
 * announced rows: a prefetch must add only new rows, a cancel must remove only announced rows,
 * and after every step the set must equal the resident window (M3Residency) widened by the
 * margins. Without a viewport (original mode) no event is expected.
 */
public class Test_M3Prefetch {
	static final int ROWS = 100_000;
	Display display;
	Shell shell;

	@BeforeEach
	public void setUp () {
		display = Display.getDefault ();
		shell = new Shell (display);
		shell.setBounds (20, 20, 320, 420);
		shell.open ();
	}

	@AfterEach
	public void tearDown () {
		shell.dispose ();
	}

	private void flush () {
		while (display.readAndDispatch ()) {
			// deliver pending paints and notifications
		}
	}

	/* the announced rows, replayed from the events */
	static final class Announced implements Listener {
		final BitSet rows = new BitSet ();
		int events;

		@Override
		public void handleEvent (Event event) {
			assertEquals (M3Prefetch.EVENT, event.type);
			assertNull (event.item);
			assertTrue (event.start <= event.end, "empty range");
			events++;
			if (event.detail == M3Prefetch.PREFETCH) {
				int already = rows.get (event.start, event.end + 1).cardinality ();
				assertEquals (0, already, "prefetched twice: " + event.start + ".." + event.end);
				rows.set (event.start, event.end + 1);
			} else {
				assertEquals (M3Prefetch.CANCEL, event.detail);
				assertEquals (event.end - event.start + 1, rows.get (event.start, event.end + 1).cardinality (), "cancelled rows that were not announced");
				rows.clear (event.start, event.end + 1);
			}
		}
	}

	private static void assertWindow (Announced announced, M3ItemRange resident, long count, int before, int after, boolean viewport, String what) {
		BitSet expected = new BitSet ();
		if (viewport && !resident.isEmpty ()) {
			int first = Math.max (0, resident.start () - before);
			int last = (int) Math.min (count - 1, (long) resident.end () + after);
			expected.set (first, last + 1);
		}
		assertEquals (expected, announced.rows, what + ": resident " + resident);
	}

	private static boolean viewport (Widget widget) {
		return M3Mode.of (widget) == M3Mode.VIEWPORT;
	}

	/* ------------------------------------------------------------------ Table */

	private Table virtualTable (Shell parent) {
		Table table = new Table (parent, SWT.VIRTUAL | SWT.MULTI | SWT.BORDER);
		table.setBounds (5, 5, 300, 380);
		table.addListener (SWT.SetData, event -> ((TableItem) event.item).setText ("row " + event.index));
		table.setItemCount (ROWS);
		return table;
	}

	private void showTop (Table table, int top) {
		table.setTopIndex (top);
		table.redraw ();
		table.update ();
		flush ();
	}

	@Test
	public void test_tableAnnouncesTheWindowAndOnlyWhatMoved () {
		Table table = virtualTable (shell);
		showTop (table, 0);
		Announced announced = new Announced ();
		M3Prefetch.addListener (table, announced);
		boolean viewport = viewport (table);
		assertWindow (announced, M3Residency.residentRange (table), ROWS, 0, 0, viewport, "after addListener");
		int before = announced.events;
		showTop (table, 1);
		assertWindow (announced, M3Residency.residentRange (table), ROWS, 0, 0, viewport, "after one row");
		if (viewport) assertTrue (announced.events - before <= 4, "one-row scroll announced " + (announced.events - before) + " ranges");
		for (int top : new int [] {500, ROWS / 2, ROWS - 1, 0}) {
			showTop (table, top);
			assertWindow (announced, M3Residency.residentRange (table), ROWS, 0, 0, viewport, "top " + top);
		}
		if (!viewport) assertEquals (0, announced.events);
	}

	@Test
	public void test_marginsWidenTheAnnouncedRange () {
		Table table = virtualTable (shell);
		M3Prefetch.setWindow (table, 50, 200);
		Announced announced = new Announced ();
		M3Prefetch.addListener (table, announced);
		boolean viewport = viewport (table);
		for (int top : new int [] {0, 10, 1000, ROWS - 1}) {
			showTop (table, top);
			assertWindow (announced, M3Residency.residentRange (table), table.getItemCount (), 50, 200, viewport, "top " + top);
		}
		table.removeAll ();
		flush ();
		assertTrue (announced.rows.isEmpty (), "removeAll cancels everything");
	}

	@Test
	public void test_aHiddenTableAnnouncesNothing () {
		Shell hidden = new Shell (display);
		hidden.setBounds (20, 20, 320, 420);
		Table table = virtualTable (hidden);
		Announced announced = new Announced ();
		M3Prefetch.addListener (table, announced);
		table.setTopIndex (5000);
		flush ();
		assertEquals (0, announced.events);
		hidden.dispose ();
	}

	/* ------------------------------------------------------------------ List and Tree */

	@Test
	public void test_listFollowsItsWindow () {
		List list = new List (shell, SWT.MULTI | SWT.BORDER | SWT.V_SCROLL);
		list.setBounds (5, 5, 300, 380);
		String [] strings = new String [ROWS];
		for (int i = 0; i < ROWS; i++) strings [i] = "row " + i;
		list.setItems (strings);
		flush ();
		Announced announced = new Announced ();
		M3Prefetch.setWindow (list, 3, 3);
		M3Prefetch.addListener (list, announced);
		boolean viewport = viewport (list);
		for (int top : new int [] {0, 1, 2, 4000, ROWS - 1}) {
			list.setTopIndex (top);
			list.update ();
			flush ();
			assertWindow (announced, M3Residency.residentRange (list), list.getItemCount (), 3, 3, viewport, "top " + top);
		}
		list.removeAll ();
		flush ();
		assertTrue (announced.rows.isEmpty (), "removeAll cancels everything");
	}

	@Test
	public void test_treeFollowsItsWindow () {
		Tree tree = new Tree (shell, SWT.VIRTUAL | SWT.MULTI | SWT.BORDER);
		tree.setBounds (5, 5, 300, 380);
		tree.addListener (SWT.SetData, event -> ((TreeItem) event.item).setText ("node " + event.index));
		tree.setItemCount (ROWS);
		flush ();
		Announced announced = new Announced ();
		M3Prefetch.addListener (tree, announced);
		boolean viewport = viewport (tree);
		IntConsumer show = index -> {
			tree.setTopItem (tree.getItem (index));
			tree.update ();
			flush ();
		};
		for (int index : new int [] {0, 1, ROWS / 2, ROWS - 1}) {
			show.accept (index);
			assertWindow (announced, M3Residency.residentRange (tree), M3RowMath.visibleRowCount (tree), 0, 0, viewport, "top " + index);
		}
	}

	/* ------------------------------------------------------------------ registration */

	@Test
	public void test_removedListenersHearNothingAndANewOneStartsWhole () {
		Table table = virtualTable (shell);
		showTop (table, 0);
		Announced first = new Announced ();
		M3Prefetch.addListener (table, first);
		M3Prefetch.removeListener (table, first);
		int heard = first.events;
		showTop (table, 3000);
		assertEquals (heard, first.events);
		Announced second = new Announced ();
		M3Prefetch.addListener (table, second);
		assertWindow (second, M3Residency.residentRange (table), ROWS, 0, 0, viewport (table), "new listener");
	}

	@Test
	public void test_argumentsAreChecked () {
		Table table = new Table (shell, SWT.NONE);
		Listener nothing = event -> {};
		assertThrows (IllegalArgumentException.class, () -> M3Prefetch.addListener ((Table) null, nothing));
		assertThrows (IllegalArgumentException.class, () -> M3Prefetch.addListener ((List) null, nothing));
		assertThrows (IllegalArgumentException.class, () -> M3Prefetch.addListener ((Tree) null, nothing));
		assertThrows (IllegalArgumentException.class, () -> M3Prefetch.addListener (table, null));
		assertThrows (IllegalArgumentException.class, () -> M3Prefetch.removeListener ((Table) null, nothing));
		assertThrows (IllegalArgumentException.class, () -> M3Prefetch.removeListener ((List) null, nothing));
		assertThrows (IllegalArgumentException.class, () -> M3Prefetch.removeListener ((Tree) null, nothing));
		assertThrows (IllegalArgumentException.class, () -> M3Prefetch.setWindow ((Table) null, 0, 0));
		assertThrows (IllegalArgumentException.class, () -> M3Prefetch.setWindow ((List) null, 0, 0));
		assertThrows (IllegalArgumentException.class, () -> M3Prefetch.setWindow ((Tree) null, 0, 0));
		assertThrows (IllegalArgumentException.class, () -> M3Prefetch.setWindow (table, -1, 0));
		assertThrows (IllegalArgumentException.class, () -> M3Prefetch.setWindow (table, 0, -1));
		table.dispose ();
		assertThrows (SWTException.class, () -> M3Prefetch.addListener (table, nothing));
		assertThrows (SWTException.class, () -> M3Prefetch.setWindow (table, 1, 1));
	}
}
