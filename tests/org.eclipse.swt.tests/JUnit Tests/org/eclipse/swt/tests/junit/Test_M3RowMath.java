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

import java.util.*;

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.junit.jupiter.api.*;

/**
 * Contract of M3RowMath (enhanced M3 API, leaf 6). The oracle is a recursive walk over the public
 * API (<code>getItems</code>, <code>getExpanded</code>). The class passes unchanged in every
 * startup mode: with <code>-Dswt.widget.mode=viewport</code> the virtual trees are answered by the
 * viewport's lanes, with <code>-Dswt.widget.mode=original</code> by the walk.
 */
public class Test_M3RowMath {
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

	/* ------------------------------------------------------------------ oracle */

	private static java.util.List<TreeItem> visible (Tree tree) {
		java.util.List<TreeItem> rows = new ArrayList<> ();
		collect (tree.getItems (), rows);
		return rows;
	}

	private static void collect (TreeItem [] items, java.util.List<TreeItem> rows) {
		for (TreeItem item : items) {
			rows.add (item);
			if (item.getExpanded ()) collect (item.getItems (), rows);
		}
	}

	private static long subtree (TreeItem item) {
		long rows = 1;
		if (item.getExpanded ()) {
			for (TreeItem child : item.getItems ()) rows += subtree (child);
		}
		return rows;
	}

	private static void all (TreeItem [] items, java.util.List<TreeItem> out) {
		for (TreeItem item : items) {
			out.add (item);
			all (item.getItems (), out);
		}
	}

	/* every row, every item, every subtree against the oracle */
	private static void verify (Tree tree) {
		java.util.List<TreeItem> rows = visible (tree);
		assertEquals (rows.size (), M3RowMath.visibleRowCount (tree));
		for (int row = 0; row < rows.size (); row++) {
			assertSame (rows.get (row), M3RowMath.itemAt (tree, row), "itemAt " + row);
			assertEquals (row, M3RowMath.rowOf (rows.get (row)), "rowOf " + row);
		}
		java.util.List<TreeItem> items = new ArrayList<> ();
		all (tree.getItems (), items);
		Set<TreeItem> shown = Collections.newSetFromMap (new IdentityHashMap<> ());
		shown.addAll (rows);
		for (TreeItem item : items) {
			if (!shown.contains (item)) assertEquals (-1, M3RowMath.rowOf (item), "hidden " + item.getText ());
			assertEquals (subtree (item), M3RowMath.subtreeRowCount (item), "subtree " + item.getText ());
		}
	}

	/* ------------------------------------------------------------------ plain tree */

	private Tree plain () {
		Tree tree = new Tree (shell, SWT.MULTI | SWT.BORDER);
		tree.setBounds (5, 5, 300, 380);
		for (int i = 0; i < 30; i++) {
			TreeItem root = new TreeItem (tree, SWT.NONE);
			root.setText ("r" + i);
			if (i % 3 != 0) continue;
			for (int j = 0; j < 4; j++) {
				TreeItem child = new TreeItem (root, SWT.NONE);
				child.setText ("r" + i + "." + j);
				if (j % 2 == 0) for (int k = 0; k < 3; k++) new TreeItem (child, SWT.NONE).setText ("r" + i + "." + j + "." + k);
			}
		}
		tree.getItem (0).setExpanded (true);
		tree.getItem (0).getItem (0).setExpanded (true);
		tree.getItem (3).getItem (2).setExpanded (true);
		tree.getItem (9).setExpanded (true);
		return tree;
	}

	@Test
	public void test_plainTreeAgreesWithTheWalk () {
		Tree tree = plain ();
		verify (tree);
		/* a collapsed parent hides an expanded child: rowOf -1, subtree still counts its own rows */
		TreeItem hidden = tree.getItem (3).getItem (2);
		assertEquals (-1, M3RowMath.rowOf (hidden));
		assertEquals (4, M3RowMath.subtreeRowCount (hidden));
		tree.getItem (3).setExpanded (true);
		tree.getItem (0).setExpanded (false);
		verify (tree);
	}

	@Test
	public void test_plainTreeFollowsStructuralChanges () {
		Tree tree = plain ();
		new TreeItem (tree.getItem (0), SWT.NONE, 1).setText ("inserted");
		new TreeItem (tree, SWT.NONE, 2).setText ("new root");
		tree.getItem (9).getItem (1).dispose ();
		verify (tree);
		tree.getItem (0).getItem (0).setItemCount (1);
		tree.setItemCount (12);
		verify (tree);
		tree.removeAll ();
		assertEquals (0, M3RowMath.visibleRowCount (tree));
		assertThrows (IllegalArgumentException.class, () -> M3RowMath.itemAt (tree, 0));
	}

	/* ------------------------------------------------------------------ virtual tree */

	private Tree virtualTree (int roots) {
		Tree tree = new Tree (shell, SWT.VIRTUAL | SWT.MULTI | SWT.BORDER);
		tree.setBounds (5, 5, 300, 380);
		tree.addListener (SWT.SetData, event -> {
			TreeItem item = (TreeItem) event.item;
			TreeItem parent = item.getParentItem ();
			item.setText ((parent == null ? "v" : parent.getText () + ".") + event.index);
		});
		tree.setItemCount (roots);
		return tree;
	}

	@Test
	public void test_virtualTreeAgreesWithTheWalk () {
		Tree tree = virtualTree (120);
		for (int i : new int [] {0, 5, 50, 119}) {
			TreeItem root = tree.getItem (i);
			root.setItemCount (6);
			root.setExpanded (true);
		}
		TreeItem inner = tree.getItem (5).getItem (2);
		inner.setItemCount (3);
		inner.setExpanded (true);
		verify (tree);
		tree.getItem (5).setExpanded (false);
		tree.getItem (50).getItem (0).dispose ();
		tree.getItem (0).setItemCount (2);
		verify (tree);
		tree.setItemCount (40);
		verify (tree);
	}

	@Test
	public void test_millionRowVirtualTreeAnswersFromTheLanes () {
		Tree tree = virtualTree (1);
		Assumptions.assumeTrue (M3Mode.of (tree) == M3Mode.VIEWPORT, "lanes exist only for the viewport");
		tree.setItemCount (1_000_000);
		TreeItem middle = tree.getItem (500_000);
		middle.setItemCount (1000);
		middle.setExpanded (true);
		assertEquals (1_001_000, M3RowMath.visibleRowCount (tree));
		Random random = new Random (5);
		long start = System.nanoTime ();
		for (int i = 0; i < 2000; i++) {
			long row = random.nextInt (1_001_000);
			TreeItem item = M3RowMath.itemAt (tree, row);
			assertEquals (row, M3RowMath.rowOf (item));
		}
		long millis = (System.nanoTime () - start) / 1_000_000;
		assertTrue (millis < 5000, "2000 round trips took " + millis + " ms");
		assertSame (middle, M3RowMath.itemAt (tree, 500_000));
		assertSame (middle.getItem (999), M3RowMath.itemAt (tree, 501_000));
		assertSame (tree.getItem (500_001), M3RowMath.itemAt (tree, 501_001));
		assertEquals (1001, M3RowMath.subtreeRowCount (middle));
		assertEquals (999_999 + 1000, M3RowMath.rowOf (tree.getItem (999_999)));
	}

	/* ------------------------------------------------------------------ extents */

	@Test
	public void test_extentsAreRowTimesHeightAndMatchTheBounds () {
		Tree tree = plain ();
		int height = tree.getItemHeight ();
		long count = M3RowMath.visibleRowCount (tree);
		for (long row = 0; row < count; row++) {
			long y = M3RowMath.rowExtent (tree, row);
			assertEquals (row * height, y);
			assertEquals (row, M3RowMath.rowAtExtent (tree, y));
			assertEquals (row, M3RowMath.rowAtExtent (tree, y + height - 1));
		}
		assertEquals (count * height, M3RowMath.rowExtent (tree, count));
		assertEquals (-1, M3RowMath.rowAtExtent (tree, count * height));
		assertEquals (-1, M3RowMath.rowAtExtent (tree, -1));
		assertThrows (IllegalArgumentException.class, () -> M3RowMath.rowExtent (tree, count + 1));
		assertThrows (IllegalArgumentException.class, () -> M3RowMath.rowExtent (tree, -1));
		/* rows on screen: bounds relative to the top item */
		tree.setTopItem (tree.getItem (3));
		TreeItem top = tree.getTopItem ();
		long topRow = M3RowMath.rowOf (top);
		int topY = top.getBounds ().y;
		int clientBottom = tree.getClientArea ().height;
		for (long row = topRow; row < count; row++) {
			TreeItem item = M3RowMath.itemAt (tree, row);
			int y = item.getBounds ().y;
			if (y >= clientBottom) break;
			assertEquals (M3RowMath.rowExtent (tree, row) - M3RowMath.rowExtent (tree, topRow), y - topY, item.getText ());
		}
	}

	/* ------------------------------------------------------------------ arguments */

	@Test
	public void test_argumentsAreChecked () {
		Tree tree = plain ();
		assertThrows (IllegalArgumentException.class, () -> M3RowMath.visibleRowCount (null));
		assertThrows (IllegalArgumentException.class, () -> M3RowMath.rowOf (null));
		assertThrows (IllegalArgumentException.class, () -> M3RowMath.itemAt (null, 0));
		assertThrows (IllegalArgumentException.class, () -> M3RowMath.subtreeRowCount (null));
		assertThrows (IllegalArgumentException.class, () -> M3RowMath.rowExtent (null, 0));
		assertThrows (IllegalArgumentException.class, () -> M3RowMath.rowAtExtent (null, 0));
		assertThrows (IllegalArgumentException.class, () -> M3RowMath.itemAt (tree, -1));
		long count = M3RowMath.visibleRowCount (tree);
		assertThrows (IllegalArgumentException.class, () -> M3RowMath.itemAt (tree, count));
		Tree virtual = virtualTree (10);
		assertThrows (IllegalArgumentException.class, () -> M3RowMath.itemAt (virtual, 10));
		assertThrows (IllegalArgumentException.class, () -> M3RowMath.itemAt (virtual, -1));
		TreeItem item = tree.getItem (0);
		item.dispose ();
		assertThrows (SWTException.class, () -> M3RowMath.rowOf (item));
		assertThrows (SWTException.class, () -> M3RowMath.subtreeRowCount (item));
		tree.dispose ();
		assertThrows (SWTException.class, () -> M3RowMath.visibleRowCount (tree));
		assertThrows (SWTException.class, () -> M3RowMath.itemAt (tree, 0));
	}
}
