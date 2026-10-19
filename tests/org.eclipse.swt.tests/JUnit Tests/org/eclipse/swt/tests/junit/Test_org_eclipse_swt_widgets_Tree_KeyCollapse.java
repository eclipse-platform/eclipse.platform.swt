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
 * A native collapse key (VK_SUBTRACT, VK_LEFT) on an expanded caret row of a virtual Tree. Expected values are the
 * official SWT 3.135.0 answers (VkSubtractCollapseOracle, docs/m3/tree-viewport/evidence/oracles, 8 cases, plain and
 * columns): only Collapse is sent, the row stays collapsed with its children, the top item becomes the caret row when
 * the top was inside or below it, and the bar loses the row's children. Mode-independent.
 */
public class Test_org_eclipse_swt_widgets_Tree_KeyCollapse {
	static final int ROOTS = 300, CHILDREN = 40, VK_LEFT = 0x25, VK_SUBTRACT = 0x6D;
	Display display;
	Shell shell;
	Tree tree;
	final StringBuilder events = new StringBuilder ();

	@BeforeEach
	public void setUp () {
		Assumptions.assumeTrue ("win32".equals (SWT.getPlatform ()), "native TreeView keys (win32)");
		display = Display.getDefault ();
		shell = new Shell (display);
		shell.setBounds (20, 20, 320, 420);
	}

	@AfterEach
	public void tearDown () {
		if (shell != null) shell.dispose ();
	}

	private void createTree (boolean columns) {
		tree = new Tree (shell, SWT.MULTI | SWT.BORDER | SWT.VIRTUAL | (columns ? SWT.CHECK | SWT.FULL_SELECTION : 0));
		tree.setBounds (5, 5, 300, 380);
		if (columns) {
			tree.setHeaderVisible (true);
			tree.setLinesVisible (true);
			for (int c = 0; c < 3; c++) new TreeColumn (tree, SWT.NONE).setWidth (c == 0 ? 160 : 120);
		}
		tree.addListener (SWT.SetData, event -> {
			TreeItem item = (TreeItem) event.item;
			TreeItem parent = item.getParentItem ();
			item.setText ((parent == null ? "root " : parent.getText () + " / ") + event.index);
			if (parent == null && event.index % 7 == 0) item.setItemCount (CHILDREN);
		});
		for (int type : new int [] {SWT.Collapse, SWT.Expand, SWT.Selection}) {
			tree.addListener (type, e -> events.append (e.type == SWT.Collapse ? "C:" : e.type == SWT.Expand ? "E:" : "S:").append (((TreeItem) e.item).getText ()).append (' '));
		}
		tree.setItemCount (ROOTS);
		shell.open ();
		tree.setFocus ();
		ViewportDifferential.settle (display, 100);
	}

	/* caret root 7, expanded; the top at the given row; then the key. Answers the state the oracle prints. */
	private String collapseByKey (boolean columns, String top, int key) {
		createTree (columns);
		TreeItem row = tree.getItem (7);
		row.getItemCount ();
		row.setExpanded (true);
		tree.setSelection (row);
		tree.setTopItem (top.contains ("/") ? row.getItem (Integer.parseInt (top.substring (top.indexOf ('/') + 1))) : tree.getItem (Integer.parseInt (top)));
		tree.update ();
		assertTrue (row.getExpanded (), "expanded before the key");
		assertEquals (CHILDREN + ROOTS - 1, tree.getVerticalBar ().getMaximum (), "bar maximum before the key");
		events.setLength (0);
		ViewportDifferential.send (tree, 0x0100, key, 0);
		ViewportDifferential.send (tree, 0x0101, key, 0xC0000001L);
		tree.update ();
		ViewportDifferential.settle (display, 100);
		TreeItem topItem = tree.getTopItem ();
		return "events " + events.toString ().trim () + " | expanded " + row.getExpanded () + " | count " + row.getItemCount ()
			+ " | top " + (topItem == null ? "null" : topItem.getText ()) + " | sel " + tree.getSelectionCount ()
			+ " | bar " + tree.getVerticalBar ().getSelection () + "/" + tree.getVerticalBar ().getMaximum ();
	}

	static final String STOCK = "events C:root 7 | expanded false | count 40 | top root 7 | sel 1 | bar 7/299";

	@Test
	public void subtractWithTheRowAtTheTop () {
		assertEquals (STOCK, collapseByKey (false, "7", VK_SUBTRACT));
	}

	@Test
	public void subtractWithTheTopInsideTheRow () {
		assertEquals (STOCK, collapseByKey (false, "7/2", VK_SUBTRACT));
	}

	@Test
	public void subtractWithTheTopDeepInsideTheRow () {
		assertEquals (STOCK, collapseByKey (false, "7/30", VK_SUBTRACT));
	}

	@Test
	public void subtractWithTheTopBelowTheRow () {
		assertEquals (STOCK, collapseByKey (false, "100", VK_SUBTRACT));
	}

	@Test
	public void leftWithTheTopInsideTheRow () {
		assertEquals (STOCK, collapseByKey (false, "7/30", VK_LEFT));
	}

	@Test
	public void leftWithTheTopBelowTheRow () {
		assertEquals (STOCK, collapseByKey (false, "100", VK_LEFT));
	}

	@Test
	public void subtractInTheColumnsTree () {
		assertEquals (STOCK, collapseByKey (true, "7/30", VK_SUBTRACT));
	}

	@Test
	public void leftInTheColumnsTree () {
		assertEquals (STOCK, collapseByKey (true, "100", VK_LEFT));
	}
}
