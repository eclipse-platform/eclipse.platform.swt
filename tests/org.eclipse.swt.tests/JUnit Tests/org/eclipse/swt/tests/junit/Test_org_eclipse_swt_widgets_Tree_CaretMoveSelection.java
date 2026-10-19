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

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.*;
import org.junit.jupiter.api.*;

/**
 * The selection a MULTI virtual Tree keeps when the caret row is deleted (setItemCount shrinking past it, dispose of
 * the row or of an ancestor). Expected values are the official SWT 3.135.0 answers (oracles ShrinkSelectionOracle and
 * CaretMoveSelectionOracle, docs/m3/tree-viewport/evidence/oracles): on each native caret move the TreeView deselects the
 * most recently selected row other than the old caret, unless it is the new caret. Mode-independent: the viewport and
 * the native Tree must both answer these; the test also passes on the official jar.
 */
public class Test_org_eclipse_swt_widgets_Tree_CaretMoveSelection {
	static final int ROOTS = 300, CHILDREN = 40;
	Display display;
	Shell shell;
	Tree tree;

	@BeforeEach
	public void setUp () {
		Assumptions.assumeTrue ("win32".equals (SWT.getPlatform ()), "native TreeView rule (win32)");
		display = Display.getDefault ();
		shell = new Shell (display);
		shell.setBounds (20, 20, 320, 420);
		tree = new Tree (shell, SWT.MULTI | SWT.BORDER | SWT.VIRTUAL);
		tree.setBounds (5, 5, 300, 380);
		tree.addListener (SWT.SetData, event -> {
			TreeItem item = (TreeItem) event.item;
			TreeItem parent = item.getParentItem ();
			item.setText ((parent == null ? "root " : parent.getText () + " / ") + event.index);
			if (parent == null && event.index % 7 == 0) item.setItemCount (CHILDREN);
		});
		tree.setItemCount (ROOTS);
		shell.open ();
		tree.setFocus ();
		ViewportDifferential.settle (display, 100);
	}

	@AfterEach
	public void tearDown () {
		if (shell != null) shell.dispose ();
	}

	private TreeItem at (String coordinate) {
		String [] parts = coordinate.split ("/");
		TreeItem item = tree.getItem (Integer.parseInt (parts [0]));
		return parts.length == 1 ? item : item.getItem (Integer.parseInt (parts [1]));
	}

	/* the selection as preorder ranges of coordinates, for example "0-7 7/0-7/19 8-289" */
	private String ranges () {
		StringBuilder out = new StringBuilder ();
		String start = null, previous = null;
		int previousParent = -2, previousIndex = -2;
		for (TreeItem item : tree.getSelection ()) {
			TreeItem parent = item.getParentItem ();
			int parentIndex = parent == null ? -1 : tree.indexOf (parent);
			int index = parent == null ? tree.indexOf (item) : parent.indexOf (item);
			String key = parent == null ? String.valueOf (index) : parentIndex + "/" + index;
			if (start != null && parentIndex == previousParent && index == previousIndex + 1) {
				previous = key;
			} else {
				if (start != null) out.append (start.equals (previous) ? start : start + "-" + previous).append (' ');
				start = previous = key;
			}
			previousParent = parentIndex;
			previousIndex = index;
		}
		if (start != null) out.append (start.equals (previous) ? start : start + "-" + previous);
		return out.toString ().trim ();
	}

	/*
	 * caret: the caret row; expand: a row to expand first (or null); selection: applied in order (r, r/c, all, -r,
	 * a-b); action: "shrink N" (roots), "shrinkchild r N" or "dispose r[/c]".
	 */
	private String run (String caret, String expand, String selection, String action) {
		if (expand != null) at (expand).setExpanded (true);
		TreeItem caretItem = at (caret);
		tree.setSelection (caretItem);
		tree.deselectAll ();
		for (String part : selection.split (",")) {
			if (part.equals ("all")) tree.selectAll ();
			else if (part.startsWith ("-")) tree.deselect (at (part.substring (1)));
			else if (part.contains ("-")) {
				String [] range = part.split ("-");
				for (int r = Integer.parseInt (range [0]); r <= Integer.parseInt (range [1]); r++) tree.select (tree.getItem (r));
			} else tree.select (at (part));
		}
		tree.showItem (caretItem);
		tree.update ();
		String [] w = action.split (" ");
		switch (w [0]) {
			case "shrink" -> tree.setItemCount (ROOTS - Integer.parseInt (w [1]));
			case "shrinkchild" -> at (w [1]).setItemCount (Integer.parseInt (w [2]));
			default -> at (w [1]).dispose ();
		}
		tree.update ();
		return ranges ();
	}

	@Test
	public void shrinkPastTheCaretDeselectsTheMostRecentlySelectedRow () {
		assertEquals ("0-296", run ("299", null, "all,-298", "shrink 1"));
	}

	@Test
	public void recencyIsTheSelectionOrderNotThePosition () {
		assertEquals ("297", run ("299", null, "299,297,296", "shrink 1"));
	}

	@Test
	public void theNewCaretKeepsItsSelection () {
		assertEquals ("298", run ("299", null, "298,296", "shrink 1"));
	}

	@Test
	public void selectAllLeavesAlreadySelectedRowsInPlace () {
		assertEquals ("0-295 297", run ("299", null, "297,all,-298", "shrink 1"));
	}

	@Test
	public void disposeOfTheCaretRowDeselectsTheMostRecentlySelectedRow () {
		assertEquals ("5 7", run ("5", null, "6,8,290", "dispose 5"));
	}

	@Test
	public void disposeOfAnAncestorMovesTheCaretTwice () {
		assertEquals ("0-296", run ("7/2", "7", "all", "dispose 7"));
	}

	@Test
	public void childShrinkPastTheCaretMovesItAlongTheRemovedChildren () {
		assertEquals ("0-7 7/0-7/19 8-289", run ("7/30", "7", "all", "shrinkchild 7 20"));
	}

	@Test
	public void rootShrinkWithTheCaretInAnExpandedRow () {
		assertEquals ("0-289", run ("294/39", "294", "all", "shrink 6"));
	}

	@Test
	public void longShrinkConsumesFromBothEnds () {
		assertEquals ("0-64", run ("150", null, "all", "shrink 160"));
	}

	@Test
	public void movesWithoutOtherSelectedRowsLeaveNothingSelected () {
		assertEquals ("", run ("290", null, "290-299", "shrink 20"));
	}
}
