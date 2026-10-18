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

import java.util.ArrayList;
import java.util.logging.*;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.win32.*;
import org.eclipse.swt.widgets.*;
import org.junit.jupiter.api.*;

/**
 * Edge cases of the win32 Tree viewport: hidden headers, redraw off, invisible trees, zero-height
 * bodies, custom draw listeners, a three-level tree, native expand messages, row-data requests for
 * rows outside the client area, listeners that dispose the tree, and the emptiness reset with
 * custom draw. Public API, window messages and key messages only; hidden desktop; the Tree log runs
 * at FINEST so its lazy messages are computed. Skipped when the viewport is off.
 */
@SuppressWarnings("restriction")
public class Test_org_eclipse_swt_widgets_Tree_ViewportEdges {
	static final int WM_KEYDOWN = 0x100, WM_KEYUP = 0x101, WM_CHAR = 0x102, WM_LBUTTONDOWN = 0x201, WM_LBUTTONUP = 0x202;
	static final int VK_UP = 0x26, VK_DOWN = 0x28, VK_RIGHT = 0x27, VK_HOME = 0x24;
	static final int TVM_EXPAND = 0x1102, TVE_COLLAPSE = 1, TVE_EXPAND = 2;

	Display display;
	Shell shell;
	Level treeLevel;
	final java.util.List<Integer> emptiness = new ArrayList<> ();

	@BeforeEach
	public void setUp () {
		display = Display.getDefault ();
		shell = new Shell (display);
		shell.setBounds (20, 20, 340, 460);
		Logger logger = Logger.getLogger ("org.eclipse.swt.widgets.Tree");
		treeLevel = logger.getLevel ();
		logger.setLevel (Level.ALL);
	}

	@AfterEach
	public void tearDown () {
		Logger.getLogger ("org.eclipse.swt.widgets.Tree").setLevel (treeLevel);
		shell.dispose ();
	}

	/* roots with index % 7 == 0 have 20 children; children with index % 5 == 0 have 6 grandchildren */
	private Tree tree (int style, int roots) {
		Tree tree = new Tree (shell, style | SWT.VIRTUAL | SWT.BORDER);
		tree.setBounds (5, 5, 300, 380);
		tree.addListener (SWT.SetData, event -> {
			TreeItem item = (TreeItem) event.item;
			TreeItem parent = item.getParentItem ();
			item.setText ((parent == null ? "root " : parent.getText () + " / ") + event.index);
			if (parent == null && event.index % 7 == 0) item.setItemCount (20);
			if (parent != null && parent.getParentItem () == null && event.index % 5 == 0) item.setItemCount (6);
		});
		tree.addListener (SWT.EmptinessChanged, event -> emptiness.add (event.detail));
		tree.setItemCount (roots);
		Assumptions.assumeTrue (roots <= 1 || tree.getVerticalBar () == null || tree.getVerticalBar ().getMaximum () == roots - 1,
				"Tree viewport mediation is not active in this build");
		return tree;
	}

	private void settle () {
		while (display.readAndDispatch ()) {
			/* drain */
		}
	}

	private static void key (Tree tree, int key) {
		OS.SendMessage (tree.handle, WM_KEYDOWN, key, 0);
		OS.SendMessage (tree.handle, WM_KEYUP, key, 0xC0000001L);
	}

	private static int topIndex (Tree tree) {
		return tree.getVerticalBar ().getSelection ();
	}

	/* the caret item (package-private Tree.getFocusItem) */
	private static TreeItem focusItem (Tree tree) {
		try {
			java.lang.reflect.Method method = Tree.class.getDeclaredMethod ("getFocusItem");
			method.setAccessible (true);
			return (TreeItem) method.invoke (tree);
		} catch (ReflectiveOperationException e) {
			throw new AssertionError (e);
		}
	}

	private static String focusText (Tree tree) {
		TreeItem item = focusItem (tree);
		return item == null ? null : item.getText ();
	}

	/* a row-data request (TVN_GETDISPINFO) for one native row, as the TreeView sends when it measures or paints */
	private static void dispInfo (Tree tree, long hItem) {
		NMTVDISPINFO info = new NMTVDISPINFO ();
		info.hwndFrom = tree.handle;
		info.code = OS.TVN_GETDISPINFO;
		info.mask = OS.TVIF_TEXT;
		info.hItem = hItem;
		info.lParam = -1;
		long heap = OS.GetProcessHeap ();
		long memory = OS.HeapAlloc (heap, OS.HEAP_ZERO_MEMORY, NMTVDISPINFO.sizeof);
		try {
			OS.MoveMemory (memory, info, NMTVDISPINFO.sizeof);
			OS.SendMessage (tree.handle, OS.WM_NOTIFY, 0, memory);
		} finally {
			OS.HeapFree (heap, 0, memory);
		}
	}

	@Test
	public void aHiddenHeaderTakesNoBodyHeight () {
		Tree tree = tree (SWT.MULTI, 500);
		TreeColumn column = new TreeColumn (tree, SWT.NONE);
		column.setWidth (200);
		shell.open ();
		settle ();
		assertFalse (tree.getHeaderVisible ());
		int rows = tree.getVerticalBar ().getThumb ();
		tree.setHeaderVisible (true);
		settle ();
		assertTrue (tree.getVerticalBar ().getThumb () <= rows, "a shown header takes rows from the body");
		tree.setHeaderVisible (false);
		settle ();
		assertEquals (rows, tree.getVerticalBar ().getThumb ());
	}

	@Test
	public void geometryWithRedrawOffOrInvisibleStillAnswers () {
		Tree tree = tree (SWT.MULTI, 5_000);
		shell.open ();
		settle ();
		int height = tree.getItemHeight ();
		tree.setRedraw (false);
		tree.setTopItem (tree.getItem (2_000));
		Rectangle far = tree.getItem (4_000).getBounds ();
		tree.setRedraw (true);
		settle ();
		assertEquals ((4_000L - 2_000) * height, (long) far.y, height, "redraw off: " + far);
		tree.setVisible (false);
		Rectangle hidden = tree.getItem (4_500).getBounds ();
		assertEquals ((4_500L - 2_000) * height, (long) hidden.y, height, "invisible: " + hidden);
		tree.setVisible (true);
	}

	@Test
	public void rowsKnownToBeLeavesCarryNoExpander () {
		Tree tree = tree (SWT.MULTI, 3_000);
		shell.open ();
		settle ();
		TreeItem leaf = tree.getItem (7);
		leaf.setItemCount (0);
		leaf.setExpanded (true);
		assertFalse (leaf.getExpanded (), "a known leaf does not expand");
		tree.setTopItem (tree.getItem (2_000));
		settle ();
		tree.setTopItem (tree.getItem (0));
		settle ();
		assertEquals (0, tree.getItem (7).getItemCount (), "the leaf came back without children");
	}

	@Test
	public void rowDataRequestsOutsideTheClientAreaAreDeclined () {
		Tree tree = tree (SWT.MULTI, 5_000);
		shell.open ();
		settle ();
		tree.setTopItem (tree.getItem (3_000));
		settle ();
		int dataBefore = countSetData (tree, () -> {
			dispInfo (tree, 0);
			dispInfo (tree, 1);
			/* overscan rows above and below the client area: resident, never painted, never filled */
			long above = tree.getItem (2_996).handle, below = tree.getItem (3_000 + tree.getVerticalBar ().getThumb () + 3).handle;
			assertNotEquals (0L, above, "the overscan row above is resident");
			assertNotEquals (0L, below, "the overscan row below is resident");
			dispInfo (tree, above);
			dispInfo (tree, below);
		});
		assertEquals (0, dataBefore, "no SetData for rows outside the client area");
	}

	private int countSetData (Tree tree, Runnable action) {
		int [] count = {0};
		Listener listener = event -> count [0]++;
		tree.addListener (SWT.SetData, listener);
		try {
			action.run ();
		} finally {
			tree.removeListener (SWT.SetData, listener);
		}
		return count [0];
	}

	@Test
	public void selectAllSelectsColdRowsThatScrollIn () {
		Tree tree = tree (SWT.MULTI, 4_000);
		shell.open ();
		settle ();
		tree.selectAll ();
		tree.setTopItem (tree.getItem (3_000));
		settle ();
		TreeItem cold = tree.getItem (3_002);
		assertTrue (java.util.Arrays.asList (tree.getSelection ()).contains (cold), "a cold row is selected by the pattern");
		new TreeItem (tree, SWT.NONE).setText ("appended");
		assertEquals (4_000, tree.getSelectionCount (), "a row appended after selectAll is not selected");
		tree.setTopItem (tree.getItem (10));
		settle ();
		assertEquals (4_000, tree.getSelectionCount ());
	}

	@Test
	public void measureItemListenersWidenTheScrollRangeAndProbeOnce () {
		Tree tree = tree (SWT.MULTI, 5_000);
		tree.addListener (SWT.MeasureItem, event -> event.width = 600);
		shell.open ();
		settle ();
		Rectangle probed = tree.getItem (4_000).getBounds ();
		assertTrue (probed.height > 0, "the probe measured the row: " + probed);
		tree.setTopItem (tree.getItem (100));
		settle ();
		assertTrue (tree.getHorizontalBar ().getMaximum () >= 600, "the measured width gives a horizontal range");
		TreeItem hidden = tree.getItem (14).getItem (3);
		tree.showItem (hidden);
		settle ();
		assertEquals ("root 14 / 3", hidden.getText (), "with MeasureItem the shown row is filled at once");
	}

	@Test
	public void showItemWithAHeaderButNoColumnsMeasuresTheRow () {
		Tree tree = tree (SWT.MULTI, 300);
		tree.setHeaderVisible (true);
		shell.open ();
		settle ();
		TreeItem hidden = tree.getItem (28).getItem (19);
		tree.showItem (hidden);
		assertEquals ("root 28 / 19", hidden.getText ());
	}

	@Test
	public void offWindowCellsOfLaterColumnsKeepTheirHeaderPlace () {
		Tree tree = tree (SWT.MULTI, 6_000);
		tree.setHeaderVisible (true);
		for (int c = 0; c < 2; c++) {
			TreeColumn column = new TreeColumn (tree, SWT.NONE);
			column.setWidth (120);
		}
		shell.open ();
		settle ();
		int height = tree.getItemHeight ();
		Rectangle near = tree.getItem (2).getBounds (1);
		Rectangle far = tree.getItem (5_000).getBounds (1);
		assertEquals (near.x, far.x, "the second column keeps its header place");
		assertEquals (near.y + (5_000L - 2) * height, (long) far.y, height, "and moves down to its row: " + far);
	}

	@Test
	public void anInsertMarkOnARetiredRowIsForgotten () {
		Tree tree = tree (SWT.MULTI, 3_000);
		shell.open ();
		settle ();
		tree.setInsertMark (tree.getItem (3), true);
		tree.setTopItem (tree.getItem (2_000));
		settle ();
		tree.setInsertMark (null, true);
		tree.setTopItem (tree.getItem (0));
		settle ();
		assertEquals ("root 3", tree.getItem (3).getText ());
	}

	@Test
	public void nativeExpandMessagesKeepTheLogicalTop () {
		Tree tree = tree (SWT.MULTI, 300);
		shell.open ();
		settle ();
		TreeItem root = tree.getItem (7);
		root.setExpanded (true);
		tree.setTopItem (root.getItem (2));
		settle ();
		assertEquals ("root 7 / 2", tree.getTopItem ().getText ());
		java.util.List<String> events = new ArrayList<> ();
		tree.addListener (SWT.Collapse, event -> events.add ("collapse " + ((TreeItem) event.item).getText ()));
		tree.addListener (SWT.Expand, event -> events.add ("expand " + ((TreeItem) event.item).getText ()));
		/*
		 * The caret row above the top collapses natively (a key: TVM_EXPAND sends no notification for a row
		 * expanded once). The TreeView resets its own top; the plane puts the logical top back.
		 */
		tree.setSelection (root);
		tree.setTopItem (root.getItem (2));
		settle ();
		key (tree, 0x6D /* VK_SUBTRACT */);
		settle ();
		/* what follows the Collapse event is under review by the TreeViewport owner (COVERAGE_GAPS.tsv, line 1477) */
		assertEquals ("collapse root 7", events.get (0));
		events.clear ();
		/* a native expand of a resident row that is not the caret */
		TreeItem other = tree.getItem (14);
		tree.setTopItem (tree.getItem (10));
		settle ();
		OS.SendMessage (tree.handle, TVM_EXPAND, TVE_EXPAND, other.handle);
		settle ();
		assertTrue (events.contains ("expand root 14"), "the native expand sent its event: " + events);
		assertTrue (other.getExpanded ());
		/* a native expand of a resident overscan row whose data was never set (no expander) */
		TreeItem unfilled = tree.getItem (35);
		OS.SendMessage (tree.handle, TVM_EXPAND, TVE_EXPAND, unfilled.handle);
		settle ();
		assertEquals (20, unfilled.getItemCount ());
	}

	@Test
	public void keysOnEmptyAndZeroHeightTrees () {
		Button focusOwner = new Button (shell, SWT.PUSH);
		focusOwner.setBounds (310, 5, 24, 24);
		Tree empty = tree (SWT.MULTI, 0);
		shell.open ();
		focusOwner.setFocus ();
		settle ();
		key (empty, VK_HOME);
		assertNull (empty.getTopItem (), "an empty tree has no top item");
		empty.dispose ();
		Tree flat = tree (SWT.MULTI, 50);
		flat.setSize (300, 0);
		focusOwner.setFocus ();
		settle ();
		key (flat, VK_DOWN);
		settle ();
		flat.setSelection (flat.getItem (20));
		flat.setTopItem (flat.getItem (5));
		/* the caret anchor is the only native row: its neighbour has no native row to take the caret */
		key (flat, VK_DOWN);
		settle ();
		key (flat, VK_HOME);
		settle ();
		flat.showItem (flat.getItem (14).getItem (3));
		settle ();
		assertTrue (flat.getItem (14).getExpanded (), "showItem expands even without a body");
	}

	@Test
	public void caretKeysAndMessagesAroundTheWindow () {
		Tree tree = tree (SWT.MULTI, 3_000);
		shell.open ();
		settle ();
		tree.setSelection (tree.getItem (10));
		tree.setTopItem (tree.getItem (2_000));
		settle ();
		key (tree, VK_UP /* the caret anchor is above the window */);
		settle ();
		assertEquals ("root 9", focusText (tree));
		assertEquals (9, topIndex (tree), "the caret row scrolled to the top");
		/* RIGHT on an anchor below the view expands it natively and keeps it in view */
		tree.setSelection (tree.getItem (2_002));
		tree.setTopItem (tree.getItem (100));
		settle ();
		key (tree, VK_RIGHT);
		settle ();
		assertTrue (tree.getItem (2_002).getExpanded (), "root 2002 has children (2002 % 7 == 0)");
		assertTrue (topIndex (tree) > 1_900, "the expanded caret row is in view: " + topIndex (tree));
		/* typed characters and clicks go through the native TreeView */
		OS.SendMessage (tree.handle, WM_CHAR, 'r', 0);
		settle ();
		int y = tree.getItemHeight () * 3 + tree.getItemHeight () / 2;
		OS.PostMessage (tree.handle, WM_LBUTTONUP, 0, (y << 16) | 30);
		OS.SendMessage (tree.handle, WM_LBUTTONDOWN, 1, (y << 16) | 30);
		settle ();
		assertNotNull (focusText (tree));
	}

	@Test
	public void childGuardsAndShrinkingTheCaretsSiblings () {
		Tree tree = tree (SWT.MULTI, 60);
		shell.open ();
		settle ();
		TreeItem root = tree.getItem (7);
		assertThrows (IllegalArgumentException.class, () -> root.getItem (20));
		root.setExpanded (true);
		settle ();
		tree.setSelection (root.getItem (4));
		settle ();
		root.setItemCount (0);
		settle ();
		assertEquals ("root 7", focusText (tree), "the parent takes the caret of its removed children");
		assertEquals (0, tree.getSelectionCount ());
	}

	@Test
	public void clearsOfUnfilledAndUnshownRows () {
		Tree hidden = tree (SWT.MULTI, 100);
		hidden.clearAll (true);
		hidden.getItem (40);
		hidden.clear (40, false);
		shell.open ();
		settle ();
		assertEquals ("root 40", hidden.getItem (40).getText ());
		Tree empty = tree (SWT.MULTI, 0);
		assertNull (empty.getTopItem ());
	}

	@Test
	public void aListenerDisposingTheItemAbortsSetTopItem () {
		Tree tree = tree (SWT.MULTI, 100);
		shell.open ();
		settle ();
		TreeItem doomed = tree.getItem (21).getItem (5);
		tree.addListener (SWT.Expand, event -> doomed.dispose ());
		tree.setTopItem (doomed);
		settle ();
		assertTrue (doomed.isDisposed ());
		assertEquals (0, topIndex (tree), "the aborted setTopItem did not scroll");
	}

	@Test
	public void threeLevelsExpandNearestFirstAndRestoreTheTop () {
		Tree tree = tree (SWT.MULTI, 100);
		shell.open ();
		settle ();
		TreeItem grandchild = tree.getItem (14).getItem (10).getItem (4);
		java.util.List<String> expands = new ArrayList<> ();
		tree.addListener (SWT.Expand, event -> expands.add (((TreeItem) event.item).getText ()));
		tree.showItem (grandchild);
		settle ();
		assertEquals (java.util.List.of ("root 14 / 10", "root 14"), expands, "nearest ancestor first");
		/* the caret two levels down: collapsing the root moves it up two levels */
		tree.setSelection (grandchild);
		settle ();
		tree.getItem (14).setExpanded (false);
		settle ();
		assertEquals ("root 14", focusText (tree));
		/* the top row two levels down: collapsing the root keeps its nearest visible ancestor on top */
		tree.getItem (14).setExpanded (true);
		tree.getItem (14).getItem (10).setExpanded (true);
		tree.setTopItem (grandchild);
		settle ();
		tree.deselectAll ();
		tree.getItem (21).setExpanded (true);
		tree.getItem (14).setExpanded (false);
		settle ();
		assertEquals ("root 14", tree.getTopItem ().getText ());
		/* collapsing the caret row itself keeps the selection */
		tree.setSelection (tree.getItem (21));
		tree.getItem (21).setExpanded (false);
		assertEquals (1, tree.getSelectionCount ());
	}

	@Test
	public void collapsingWithoutAnyCaret () {
		Tree tree = tree (SWT.MULTI, 50);
		tree.getItem (7).getText ();
		tree.getItem (7).setExpanded (true);
		assertTrue (tree.getItem (7).getExpanded ());
		tree.getItem (7).setExpanded (false);
		assertFalse (tree.getItem (7).getExpanded ());
	}

	@Test
	public void aListenerDisposingTheTreeAbortsShowItem () {
		Tree tree = tree (SWT.MULTI, 100);
		shell.open ();
		settle ();
		TreeItem target = tree.getItem (35).getItem (2);
		tree.addListener (SWT.Expand, event -> tree.dispose ());
		tree.showItem (target);
		assertTrue (tree.isDisposed ());
	}

	@Test
	public void customDrawSurvivesTheTreeBecomingEmpty () {
		Tree lines = tree (SWT.MULTI, 3);
		lines.setLinesVisible (true);
		Tree erase = tree (SWT.MULTI, 3);
		erase.addListener (SWT.EraseItem, event -> { });
		Tree paint = tree (SWT.MULTI, 3);
		paint.addListener (SWT.PaintItem, event -> { });
		shell.open ();
		settle ();
		for (Tree tree : new Tree [] {lines, erase, paint}) {
			for (int i = tree.getItemCount () - 1; i >= 0; i--) tree.getItem (i).dispose ();
			assertEquals (0, tree.getItemCount ());
		}
		assertEquals (java.util.List.of (1, 1, 1), emptiness);
	}

	@Test
	public void removingCaretRowsOffTheScreen () {
		Tree tree = tree (SWT.MULTI, 3_000);
		shell.open ();
		settle ();
		/* a row inside a collapsed branch: nothing visible moves */
		tree.getItem (14).getItem (3).dispose ();
		assertEquals (0, topIndex (tree));
		assertEquals (19, tree.getItem (14).getItemCount ());
		/* the caret row above the window: its next sibling is off screen, it is not measured at once */
		tree.setSelection (tree.getItem (10));
		tree.setTopItem (tree.getItem (500));
		settle ();
		tree.getItem (10).dispose ();
		settle ();
		assertEquals (10, tree.indexOf (focusItem (tree)), "the next sibling took the caret");
		/* the caret row below the window */
		tree.setSelection (tree.getItem (2_500));
		tree.setTopItem (tree.getItem (100));
		settle ();
		tree.getItem (2_500).dispose ();
		settle ();
		assertEquals (2_500, tree.indexOf (focusItem (tree)), "the next sibling took the caret");
	}

	@Test
	public void removingTheOnlyExpandedRootAndARowWithoutACaret () {
		Tree only = tree (SWT.MULTI, 1);
		Tree other = tree (SWT.MULTI, 40);
		other.setBounds (5, 5, 300, 380);
		shell.open ();
		settle ();
		only.getItem (0).getText ();
		only.getItem (0).setExpanded (true);
		settle ();
		assertTrue (only.getItem (0).getExpanded ());
		only.getItem (0).dispose ();
		assertEquals (0, only.getItemCount ());
		/* no native caret was ever placed in this tree */
		other.getItem (3).dispose ();
		assertEquals (39, other.getItemCount ());
	}

	@Test
	public void rowsAppendedAfterSelectAllStayOutOfTheCount () {
		Tree tree = tree (SWT.MULTI, 200);
		shell.open ();
		settle ();
		tree.selectAll ();
		TreeItem appended = new TreeItem (tree, SWT.NONE);
		appended.setText ("appended");
		assertSame (appended, tree.getItem (200));
		assertEquals (200, tree.getSelectionCount (), "the appended row is materialized past the pattern limit");
		tree.select (appended);
		assertEquals (201, tree.getSelectionCount ());
	}

	@Test
	public void aListenerDisposingASelectedItemDuringSetSelection () {
		Tree tree = tree (SWT.MULTI, 100);
		shell.open ();
		settle ();
		TreeItem hidden = tree.getItem (21).getItem (4), other = tree.getItem (30);
		tree.addListener (SWT.Expand, event -> other.dispose ());
		tree.setSelection (new TreeItem [] {hidden, other});
		assertTrue (other.isDisposed ());
		assertEquals (1, tree.getSelectionCount (), "the surviving item stays selected");
	}

	@Test
	public void selectionsWithNullEntries () {
		Tree tree = tree (SWT.MULTI, 40);
		shell.open ();
		settle ();
		tree.setSelection (new TreeItem [] {null});
		assertEquals (0, tree.getSelectionCount ());
		tree.setSelection (new TreeItem [] {null, tree.getItem (3), null});
		assertEquals (1, tree.getSelectionCount ());
		Tree single = tree (SWT.SINGLE, 10);
		single.select (single.getItem (2));
		assertEquals (1, single.getSelectionCount ());
	}
}
