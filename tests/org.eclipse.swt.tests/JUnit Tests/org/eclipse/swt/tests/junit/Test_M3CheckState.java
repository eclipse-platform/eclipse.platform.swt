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
 * Contract of M3CheckState (enhanced M3 API, leaf 17). The oracle keeps the checked leaves and
 * derives every internal state from its children. The class passes unchanged in every startup
 * mode.
 */
public class Test_M3CheckState {
	Display display;
	Shell shell;
	Tree tree;
	final java.util.List<TreeItem> all = new ArrayList<> ();

	@BeforeEach
	public void setUp () {
		display = Display.getDefault ();
		shell = new Shell (display);
		shell.setBounds (20, 20, 320, 420);
		tree = new Tree (shell, SWT.CHECK | SWT.MULTI);
		tree.setBounds (5, 5, 300, 380);
		/* 4 roots, each with 3 children, the first two children with 2 grandchildren each */
		for (int r = 0; r < 4; r++) {
			TreeItem root = add (null, "r" + r);
			for (int c = 0; c < 3; c++) {
				TreeItem child = add (root, "r" + r + "." + c);
				if (c < 2) for (int g = 0; g < 2; g++) add (child, "r" + r + "." + c + "." + g);
			}
		}
		shell.open ();
	}

	private TreeItem add (TreeItem parent, String text) {
		TreeItem item = parent == null ? new TreeItem (tree, SWT.NONE) : new TreeItem (parent, SWT.NONE);
		item.setText (text);
		all.add (item);
		return item;
	}

	@AfterEach
	public void tearDown () {
		shell.dispose ();
	}

	/* the oracle state of an item from the checked leaves */
	private static int expected (TreeItem item, Set<TreeItem> checkedLeaves) {
		TreeItem [] children = item.getItems ();
		if (children.length == 0) return checkedLeaves.contains (item) ? M3CheckState.CHECKED : M3CheckState.UNCHECKED;
		boolean any = false, every = true;
		for (TreeItem child : children) {
			int state = expected (child, checkedLeaves);
			any |= state != M3CheckState.UNCHECKED;
			every &= state == M3CheckState.CHECKED;
		}
		return !any ? M3CheckState.UNCHECKED : every ? M3CheckState.CHECKED : M3CheckState.PARTIAL;
	}

	private static void leavesUnder (TreeItem item, java.util.List<TreeItem> out) {
		TreeItem [] children = item.getItems ();
		if (children.length == 0) out.add (item);
		for (TreeItem child : children) leavesUnder (child, out);
	}

	private void assertOracle (Set<TreeItem> checkedLeaves, String what) {
		for (TreeItem item : all) assertEquals (expected (item, checkedLeaves), M3CheckState.state (item), what + ": " + item.getText ());
	}

	@Test
	public void test_randomTogglesAgreeWithTheOracle () {
		Random random = new Random (17);
		Set<TreeItem> checkedLeaves = new HashSet<> ();
		for (int step = 0; step < 200; step++) {
			TreeItem item = all.get (random.nextInt (all.size ()));
			boolean checked = random.nextBoolean ();
			M3CheckState.setChecked (item, checked);
			java.util.List<TreeItem> leaves = new ArrayList<> ();
			leavesUnder (item, leaves);
			if (checked) checkedLeaves.addAll (leaves); else checkedLeaves.removeAll (leaves);
			assertOracle (checkedLeaves, "step " + step + " " + item.getText () + "=" + checked);
		}
	}

	@Test
	public void test_partialAndFullAncestors () {
		TreeItem root = tree.getItem (0), child = root.getItem (0), grandchild = child.getItem (0);
		M3CheckState.setChecked (root, true);
		for (TreeItem item : new TreeItem [] {root, child, grandchild, root.getItem (2)}) assertEquals (M3CheckState.CHECKED, M3CheckState.state (item));
		M3CheckState.setChecked (grandchild, false);
		assertEquals (M3CheckState.UNCHECKED, M3CheckState.state (grandchild));
		assertEquals (M3CheckState.PARTIAL, M3CheckState.state (child));
		assertEquals (M3CheckState.PARTIAL, M3CheckState.state (root));
		assertTrue (child.getChecked () && child.getGrayed (), "partial is checked and grayed");
		M3CheckState.setChecked (grandchild, true);
		assertEquals (M3CheckState.CHECKED, M3CheckState.state (root));
		assertFalse (root.getGrayed ());
		assertEquals (M3CheckState.UNCHECKED, M3CheckState.state (tree.getItem (1)), "other roots untouched");
	}

	@Test
	public void test_digInFollowsUserToggles () {
		TreeItem root = tree.getItem (1), child = root.getItem (1);
		assertFalse (M3CheckState.isDigIn (tree));
		M3CheckState.setDigIn (tree, true);
		M3CheckState.setDigIn (tree, true);
		assertTrue (M3CheckState.isDigIn (tree));
		/* a user toggle: the native control flips the box, then notifies */
		child.setChecked (true);
		Event event = new Event ();
		event.item = child;
		event.detail = SWT.CHECK;
		tree.notifyListeners (SWT.Selection, event);
		assertEquals (M3CheckState.CHECKED, M3CheckState.state (child.getItem (0)));
		assertEquals (M3CheckState.PARTIAL, M3CheckState.state (root));
		/* an application setChecked stays single-item */
		root.getItem (0).setChecked (true);
		assertEquals (M3CheckState.UNCHECKED, M3CheckState.state (root.getItem (0).getItem (0)));
		/* a plain selection is not a toggle */
		Event select = new Event ();
		select.item = root.getItem (2);
		tree.notifyListeners (SWT.Selection, select);
		assertEquals (M3CheckState.UNCHECKED, M3CheckState.state (root.getItem (2)));
		M3CheckState.setDigIn (tree, false);
		assertFalse (M3CheckState.isDigIn (tree));
		TreeItem other = tree.getItem (2);
		other.setChecked (true);
		Event toggle = new Event ();
		toggle.item = other;
		toggle.detail = SWT.CHECK;
		tree.notifyListeners (SWT.Selection, toggle);
		assertEquals (M3CheckState.UNCHECKED, M3CheckState.state (other.getItem (0)), "dig-in off: no propagation");
	}

	@Test
	public void test_treeWithoutCheckIsUnchanged () {
		Tree plain = new Tree (shell, SWT.NONE);
		TreeItem root = new TreeItem (plain, SWT.NONE);
		TreeItem child = new TreeItem (root, SWT.NONE);
		M3CheckState.setChecked (root, true);
		assertEquals (M3CheckState.UNCHECKED, M3CheckState.state (root));
		assertEquals (M3CheckState.UNCHECKED, M3CheckState.state (child));
	}

	@Test
	public void test_argumentsAreChecked () {
		assertThrows (IllegalArgumentException.class, () -> M3CheckState.state (null));
		assertThrows (IllegalArgumentException.class, () -> M3CheckState.setChecked (null, true));
		assertThrows (IllegalArgumentException.class, () -> M3CheckState.setDigIn (null, true));
		assertThrows (IllegalArgumentException.class, () -> M3CheckState.isDigIn (null));
		TreeItem item = tree.getItem (3);
		item.dispose ();
		assertThrows (SWTException.class, () -> M3CheckState.state (item));
		assertThrows (SWTException.class, () -> M3CheckState.setChecked (item, true));
		tree.dispose ();
		assertThrows (SWTException.class, () -> M3CheckState.setDigIn (tree, true));
		assertThrows (SWTException.class, () -> M3CheckState.isDigIn (tree));
	}
}
