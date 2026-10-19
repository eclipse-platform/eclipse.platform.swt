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

import org.eclipse.swt.SWT;
import org.eclipse.swt.internal.win32.OS;
import org.eclipse.swt.widgets.*;
import org.junit.jupiter.api.*;

/**
 * Native row handles of the win32 Tree viewport (docs/m3/TREE_MEDIATED_VIEWPORT.md). The TreeView reuses the
 * handles of deleted rows, so a TreeItem whose row left the resident window must never write native state
 * through its old handle: it would change whichever row owns that handle now (check+columns seed 10 of the
 * recorded differential painted a box checked that stock paints unchecked).
 */
public class Test_org_eclipse_swt_widgets_Tree_ViewportHandles {
	static final int ROOTS = 300;
	/* TVM_GETITEMSTATE (TV_FIRST + 39), TVGN_FIRSTVISIBLE, TVGN_NEXTVISIBLE, TVIS_STATEIMAGEMASK */
	static final int TVM_GETITEMSTATE = 0x1127, FIRST_VISIBLE = 5, NEXT_VISIBLE = 6, STATE_IMAGE = 0xF000;
	Display display;
	Shell shell;

	@BeforeEach
	public void setUp () {
		display = Display.getDefault ();
		shell = new Shell (display);
		shell.setBounds (20, 20, 320, 420);
	}

	@AfterEach
	public void tearDown () {
		shell.dispose ();
	}

	/* a flat SWT.CHECK | SWT.VIRTUAL tree: root i is checked when i % 3 == 0 */
	private Tree checkTree () {
		Tree tree = new Tree (shell, SWT.MULTI | SWT.CHECK | SWT.VIRTUAL | SWT.BORDER);
		tree.setBounds (5, 5, 300, 380);
		tree.addListener (SWT.SetData, event -> {
			TreeItem item = (TreeItem) event.item;
			item.setText ("root " + event.index);
			item.setChecked (event.index % 3 == 0);
		});
		tree.setItemCount (ROOTS);
		Assumptions.assumeTrue (tree.getVerticalBar ().getMaximum () == ROOTS - 1, "Tree viewport mediation is not active in this build");
		return tree;
	}

	private void settle () {
		ViewportDifferential.settle (display, 150);
	}

	/* the check-box image of every row on screen, top to bottom: 1 unchecked, 2 checked */
	private static java.util.List<Integer> nativeCheckImages (Tree tree) {
		java.util.List<Integer> images = new ArrayList<> ();
		int rows = tree.getClientArea ().height / tree.getItemHeight ();
		long hItem = OS.SendMessage (tree.handle, OS.TVM_GETNEXTITEM, FIRST_VISIBLE, 0);
		for (int row = 0; row < rows && hItem != 0; row++) {
			images.add ((int) (OS.SendMessage (tree.handle, TVM_GETITEMSTATE, hItem, STATE_IMAGE) & STATE_IMAGE) >> 12);
			hItem = OS.SendMessage (tree.handle, OS.TVM_GETNEXTITEM, NEXT_VISIBLE, hItem);
		}
		return images;
	}

	/**
	 * Items made resident, then scrolled out of the window while the tree has no caret, are checked and
	 * unchecked: the rows now on screen keep the check boxes their own items gave them.
	 */
	@Test
	public void itemsOutsideTheWindowNeverWriteTheCheckBoxOfAReusedRow () {
		Tree tree = checkTree ();
		shell.open ();
		/* no caret: a removed caret row takes the caret with it */
		tree.setItemCount (0);
		tree.setItemCount (ROOTS);
		settle ();
		java.util.List<TreeItem> held = new ArrayList<> ();
		tree.setTopItem (tree.getItem (140));
		tree.update ();
		for (int i = 120; i < 200; i++) held.add (tree.getItem (i));
		tree.setTopItem (tree.getItem (0));
		tree.update ();
		settle ();
		java.util.List<Integer> before = nativeCheckImages (tree);
		for (TreeItem item : held) item.setChecked (!item.getChecked ());
		for (TreeItem item : held) item.setGrayed (true);
		settle ();
		java.util.List<Integer> after = nativeCheckImages (tree);
		assertFalse (before.isEmpty (), "rows on screen");
		for (int row = 0; row < before.size (); row++) {
			assertEquals (row % 3 == 0 ? 2 : 1, before.get (row), "check image of row " + row + " after its SetData");
		}
		assertEquals (before, after, "check images of the rows on screen after items outside the window changed");
		for (TreeItem item : held) {
			int index = tree.indexOf (item);
			assertEquals (index % 3 != 0, item.getChecked (), "lane of " + item.getText ());
			assertTrue (item.getGrayed (), "lane of " + item.getText ());
		}
		/* the changed items show their state when they come back */
		tree.setTopItem (held.get (20));
		tree.update ();
		settle ();
		java.util.List<Integer> back = nativeCheckImages (tree);
		for (int row = 0; row < back.size (); row++) {
			int index = 140 + row;
			assertEquals ((index % 3 != 0 ? 2 : 1) + 2, back.get (row), "check image of root " + index + " back in the window");
		}
	}

	/** clear on an item outside the window resets its lanes only, never the row that reused its old handle. */
	@Test
	public void clearOutsideTheWindowLeavesRowsOnScreenAlone () {
		Tree tree = checkTree ();
		shell.open ();
		tree.setItemCount (0);
		tree.setItemCount (ROOTS);
		settle ();
		tree.setTopItem (tree.getItem (150));
		tree.update ();
		java.util.List<TreeItem> held = new ArrayList<> ();
		for (int i = 130; i < 210; i++) held.add (tree.getItem (i));
		for (TreeItem item : held) item.getChecked ();
		tree.setTopItem (tree.getItem (0));
		tree.update ();
		settle ();
		java.util.List<Integer> before = nativeCheckImages (tree);
		for (int i = 130; i < 210; i++) tree.clear (i, false);
		tree.clearAll (true);
		tree.update ();
		settle ();
		assertEquals (before, nativeCheckImages (tree), "check images of the rows on screen after clearing items outside the window");
	}
}
