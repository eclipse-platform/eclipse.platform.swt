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

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.junit.jupiter.api.*;

/**
 * Contract of M3VisibilityObserver (enhanced M3 API, leaf 11). The oracle replays the events into
 * a set of visible rows (an entered row must be new, a left row must be in the set) and compares
 * it with <code>M3ItemSelection.getVisibleRange</code> after every trigger. Resize is a trigger in
 * every mode; a programmatic scroll is announced at once with the viewports and at the next
 * trigger without them.
 */
public class Test_M3VisibilityObserver {
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
			// deliver pending paints, notifications and the coalesced checks
		}
	}

	static final class Seen implements Listener {
		final BitSet rows = new BitSet ();
		int events;

		@Override
		public void handleEvent (Event event) {
			assertEquals (M3VisibilityObserver.EVENT, event.type);
			assertNull (event.item);
			assertTrue (event.start <= event.end);
			events++;
			if (event.detail == M3VisibilityObserver.ENTERED) {
				assertEquals (0, rows.get (event.start, event.end + 1).cardinality (), "entered twice");
				rows.set (event.start, event.end + 1);
			} else {
				assertEquals (M3VisibilityObserver.LEFT, event.detail);
				assertEquals (event.end - event.start + 1, rows.get (event.start, event.end + 1).cardinality (), "left rows that were not visible");
				rows.clear (event.start, event.end + 1);
			}
		}
	}

	private static void assertSeen (Seen seen, M3ItemRange visible, String what) {
		BitSet expected = new BitSet ();
		if (!visible.isEmpty ()) expected.set (visible.start (), visible.end () + 1);
		assertEquals (expected, seen.rows, what + ": visible " + visible);
	}

	private static boolean viewport (Widget widget) {
		return M3Mode.of (widget) == M3Mode.VIEWPORT;
	}

	/* a resize the observer hears in every mode */
	private void resize (Control control, int height) {
		control.setSize (300, height);
		control.update ();
		flush ();
	}

	@Test
	public void test_tableReportsWhatEntersAndLeaves () {
		Table table = new Table (shell, SWT.MULTI | SWT.BORDER);
		table.setBounds (5, 5, 300, 380);
		table.setItemCount (5000);
		flush ();
		Seen seen = new Seen ();
		M3VisibilityObserver.addListener (table, seen);
		assertSeen (seen, M3ItemSelection.getVisibleRange (table), "initial");
		resize (table, 200);
		assertSeen (seen, M3ItemSelection.getVisibleRange (table), "smaller");
		resize (table, 380);
		assertSeen (seen, M3ItemSelection.getVisibleRange (table), "larger");
		for (int top : new int [] {1, 400, 4999, 0}) {
			table.setTopIndex (top);
			table.update ();
			flush ();
			if (!viewport (table)) resize (table, 379 + (top & 1));
			assertSeen (seen, M3ItemSelection.getVisibleRange (table), "top " + top);
		}
		int events = seen.events;
		table.redraw ();
		table.update ();
		resize (table, table.getSize ().y);
		assertEquals (events, seen.events, "no change, no event");
	}

	@Test
	public void test_listReportsWhatEntersAndLeaves () {
		List list = new List (shell, SWT.MULTI | SWT.BORDER | SWT.V_SCROLL);
		list.setBounds (5, 5, 300, 380);
		String [] strings = new String [5000];
		for (int i = 0; i < strings.length; i++) strings [i] = "row " + i;
		list.setItems (strings);
		flush ();
		Seen seen = new Seen ();
		M3VisibilityObserver.addListener (list, seen);
		assertSeen (seen, M3ItemSelection.getVisibleRange (list), "initial");
		for (int top : new int [] {3, 2500, 4999}) {
			list.setTopIndex (top);
			list.update ();
			flush ();
			if (!viewport (list)) resize (list, 379 + (top & 1));
			assertSeen (seen, M3ItemSelection.getVisibleRange (list), "top " + top);
		}
		resize (list, 150);
		assertSeen (seen, M3ItemSelection.getVisibleRange (list), "smaller");
		list.removeAll ();
		resize (list, 151);
		assertTrue (seen.rows.isEmpty (), "an empty list shows nothing");
	}

	@Test
	public void test_treeReportsExpansion () {
		Tree tree = new Tree (shell, SWT.MULTI | SWT.BORDER);
		tree.setBounds (5, 5, 300, 380);
		for (int i = 0; i < 100; i++) {
			TreeItem root = new TreeItem (tree, SWT.NONE);
			root.setText ("root " + i);
			for (int j = 0; j < 4; j++) new TreeItem (root, SWT.NONE).setText ("child " + j);
		}
		flush ();
		Seen seen = new Seen ();
		M3VisibilityObserver.addListener (tree, seen);
		assertSeen (seen, M3ItemSelection.getVisibleRange (tree), "initial");
		tree.getItem (0).setExpanded (true);
		tree.update ();
		flush ();
		resize (tree, 379);
		assertSeen (seen, M3ItemSelection.getVisibleRange (tree), "expanded");
		tree.setTopItem (tree.getItem (50));
		tree.update ();
		flush ();
		resize (tree, 380);
		assertSeen (seen, M3ItemSelection.getVisibleRange (tree), "scrolled");
	}

	@Test
	public void test_removedListenersHearNothing () {
		Table table = new Table (shell, SWT.NONE);
		table.setBounds (5, 5, 300, 380);
		table.setItemCount (1000);
		flush ();
		Seen seen = new Seen ();
		M3VisibilityObserver.addListener (table, seen);
		M3VisibilityObserver.removeListener (table, seen);
		int events = seen.events;
		table.setTopIndex (500);
		resize (table, 200);
		assertEquals (events, seen.events);
	}

	@Test
	public void test_argumentsAreChecked () {
		Table table = new Table (shell, SWT.NONE);
		Listener nothing = event -> {};
		assertThrows (IllegalArgumentException.class, () -> M3VisibilityObserver.addListener ((Table) null, nothing));
		assertThrows (IllegalArgumentException.class, () -> M3VisibilityObserver.addListener ((List) null, nothing));
		assertThrows (IllegalArgumentException.class, () -> M3VisibilityObserver.addListener ((Tree) null, nothing));
		assertThrows (IllegalArgumentException.class, () -> M3VisibilityObserver.addListener (table, null));
		assertThrows (IllegalArgumentException.class, () -> M3VisibilityObserver.removeListener ((Table) null, nothing));
		assertThrows (IllegalArgumentException.class, () -> M3VisibilityObserver.removeListener ((List) null, nothing));
		assertThrows (IllegalArgumentException.class, () -> M3VisibilityObserver.removeListener ((Tree) null, nothing));
		M3VisibilityObserver.removeListener (table, nothing);
		table.dispose ();
		assertThrows (SWTException.class, () -> M3VisibilityObserver.addListener (table, nothing));
	}
}
