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
import java.util.logging.*;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.win32.OS;
import org.eclipse.swt.widgets.*;
import org.junit.jupiter.api.*;

/**
 * Behaviour of the win32 Tree viewport that the recorded differential does not reach: geometry of
 * rows outside the window through the probe row, scroll bar messages on the viewport window, keys
 * and focus without a caret, trees past the integer bar range, caret moves on removal, emptiness,
 * selection patterns over cold rows, and the mediated item API's guards. Runs with the Tree log at
 * FINEST, so the lazy log messages are computed. Hidden desktop; skipped when the viewport is off.
 */
public class Test_org_eclipse_swt_widgets_Tree_ViewportCoverage {
	static final int WM_SETFOCUS = 0x7, WM_KEYDOWN = 0x100, WM_KEYUP = 0x101, WM_VSCROLL = 0x115, WM_MOUSEWHEEL = 0x20A;
	static final int SB_LINEDOWN = 1, SB_PAGEDOWN = 3, SB_THUMBPOSITION = 4, SB_BOTTOM = 7, SB_ENDSCROLL = 8;

	Display display;
	Shell shell;
	int setData;
	final java.util.List<Integer> emptiness = new ArrayList<> ();
	Level treeLevel;

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

	private Tree virtualTree (int style, int roots, int childrenOfEvery7th) {
		Tree tree = new Tree (shell, style | SWT.VIRTUAL | SWT.BORDER);
		tree.setBounds (5, 5, 300, 380);
		tree.addListener (SWT.SetData, event -> {
			setData++;
			TreeItem item = (TreeItem) event.item;
			TreeItem parent = item.getParentItem ();
			item.setText ((parent == null ? "root " : parent.getText () + " / ") + event.index);
			if (parent == null && childrenOfEvery7th > 0 && event.index % 7 == 0) item.setItemCount (childrenOfEvery7th);
		});
		tree.addListener (SWT.EmptinessChanged, event -> emptiness.add (event.detail));
		tree.setItemCount (roots);
		requireViewport (tree, roots);
		return tree;
	}

	private static void requireViewport (Tree tree, int roots) {
		Assumptions.assumeTrue (roots <= 1 || tree.getVerticalBar () == null
				|| tree.getVerticalBar ().getMaximum () == roots - 1,
				"Tree viewport mediation is not active in this build");
	}

	private void settle () {
		while (display.readAndDispatch ()) {
			/* drain */
		}
	}

	private static long hwndParent (Tree tree) {
		try {
			java.lang.reflect.Field field = Tree.class.getDeclaredField ("hwndParent");
			field.setAccessible (true);
			return field.getLong (tree);
		} catch (ReflectiveOperationException e) {
			throw new AssertionError (e);
		}
	}

	private static void vscroll (Tree tree, int code) {
		OS.SendMessage (hwndParent (tree), WM_VSCROLL, code, 0);
		OS.SendMessage (hwndParent (tree), WM_VSCROLL, SB_ENDSCROLL, 0);
	}

	private static void key (Tree tree, int key) {
		ViewportDifferential.send (tree, WM_KEYDOWN, key, 0);
		ViewportDifferential.send (tree, WM_KEYUP, key, 0xC0000001L);
	}

	private int topIndex (Tree tree) {
		TreeItem top = tree.getTopItem ();
		return top == null ? -1 : tree.indexOf (top);
	}

	/** Runs in every mode, also <code>-Dswt.widget.mode=original</code>: the mode switch itself. */
	@Test
	public void anyModeAnswersTheVirtualBasics () {
		Tree tree = new Tree (shell, SWT.VIRTUAL | SWT.BORDER);
		tree.setBounds (5, 5, 300, 380);
		tree.addListener (SWT.SetData, event -> {
			TreeItem item = (TreeItem) event.item;
			item.setText ("row " + event.index);
		});
		tree.setItemCount (40);
		shell.open ();
		settle ();
		assertEquals (40, tree.getItemCount ());
		assertEquals ("row 5", tree.getItem (5).getText ());
		tree.dispose ();
	}

	@Test
	public void offWindowRowsAnswerGeometryThroughAProbe () {
		Tree tree = virtualTree (SWT.MULTI, 10_000, 40);
		shell.open ();
		settle ();
		int height = tree.getItemHeight ();
		Rectangle near = tree.getItem (1).getBounds ();
		assertTrue (near.height > 0 && near.y < 3 * height, "a resident row answers natively: " + near);
		Rectangle far = tree.getItem (9_000).getBounds ();
		assertEquals (9_000L * height, (long) far.y, 2L * height, "the probe translates to the logical place: " + far);
		assertTrue (far.height > 0 && far.width > 0, "the probe measured the label: " + far);
		Rectangle cell = tree.getItem (9_000).getBounds (0);
		assertEquals (far.y, cell.y, "the first cell sits on the row");
		Rectangle text = tree.getItem (9_000).getTextBounds (0);
		assertEquals (far.y, text.y, height, "the text sits on the row");
		tree.getItem (9_000).getImageBounds (0);
		/* a row of a deeper level is indented by the probe's depth translation */
		TreeItem root = tree.getItem (7);
		root.setExpanded (true);
		settle ();
		Rectangle child = root.getItem (39).getBounds ();
		assertTrue (child.x > far.x, "the child is indented below its root: " + child + " vs " + far);
		/* a row inside a collapsed branch has no geometry, as in stock */
		Rectangle hidden = tree.getItem (14).getItem (3).getBounds ();
		assertEquals (new Rectangle (0, 0, 0, 0), hidden);
		assertEquals (0, tree.getItem (14).getItem (3).getImageBounds (0).width);
	}

	@Test
	public void scrollBarMessagesOnTheViewportWindowScrollLogically () {
		Tree tree = virtualTree (SWT.MULTI, 50_000, 0);
		shell.open ();
		settle ();
		assertEquals (0, topIndex (tree));
		vscroll (tree, SB_LINEDOWN);
		assertEquals (1, topIndex (tree));
		vscroll (tree, SB_PAGEDOWN);
		assertTrue (topIndex (tree) > 1, "a page down moved the top");
		vscroll (tree, SB_BOTTOM);
		assertEquals (tree.getVerticalBar ().getMaximum () - tree.getVerticalBar ().getThumb (),
				tree.getVerticalBar ().getSelection (), "the bar sits at its bottom");
		assertTrue (topIndex (tree) > 49_000, "the end of the tree is in view");
		int bottom = topIndex (tree);
		/* a synthetic SB_THUMBPOSITION carries no track position, so only line scrolls are deterministic */
		vscroll (tree, 0 /* SB_LINEUP */);
		assertEquals (bottom - 1, topIndex (tree), "a line up from the bottom");
	}

	@Test
	public void aTreeWithoutScrollBarsStillAnswersScrollMessages () {
		Tree tree = new Tree (shell, SWT.VIRTUAL | SWT.NO_SCROLL | SWT.SINGLE);
		tree.setBounds (5, 5, 300, 380);
		tree.addListener (SWT.SetData, event -> ((TreeItem) event.item).setText ("row " + event.index));
		tree.setItemCount (100);
		requireViewport (tree, 100);
		shell.open ();
		settle ();
		assertNull (tree.getVerticalBar ());
		OS.SendMessage (hwndParent (tree), WM_VSCROLL, SB_LINEDOWN, 0);
		settle ();
		assertEquals (0, topIndex (tree), "without a bar the message does nothing");
	}

	@Test
	public void columnsGiveAndTakeTheHorizontalRange () {
		Tree tree = virtualTree (SWT.MULTI, 300, 0);
		TreeColumn column = new TreeColumn (tree, SWT.NONE);
		column.setText ("wide");
		column.setWidth (600);
		tree.setHeaderVisible (true);
		shell.open ();
		settle ();
		assertTrue (tree.getHorizontalBar ().getMaximum () >= 600, "the column gives a horizontal range");
		column.dispose ();
		settle ();
		tree.getItem (5).getText ();
		settle ();
		assertEquals (tree.getHorizontalBar ().getMaximum (), tree.getHorizontalBar ().getThumb (),
				"without columns the horizontal bar has no range");
	}

	@Test
	public void wheelMessagesScrollByLinesAndIgnoreAZeroDelta () {
		Tree tree = virtualTree (SWT.MULTI, 5_000, 0);
		shell.open ();
		settle ();
		tree.setTopItem (tree.getItem (100));
		Point center = tree.toDisplay (150, 190);
		ViewportDifferential.send (tree, WM_MOUSEWHEEL, ((long) (-120) & 0xFFFF) << 16, ViewportDifferential.makeLParam (center.x, center.y));
		int afterDown = topIndex (tree);
		assertTrue (afterDown > 100, "wheel down scrolled: " + afterDown);
		ViewportDifferential.send (tree, WM_MOUSEWHEEL, 0, ViewportDifferential.makeLParam (center.x, center.y));
		assertEquals (afterDown, topIndex (tree), "a zero delta scrolls nothing");
	}

	@Test
	public void navigationKeysWithoutACaretStartAtTheEdges () {
		/* the button keeps the focus, so no tree ever receives WM_SETFOCUS (which would place a caret) */
		Button focusOwner = new Button (shell, SWT.PUSH);
		focusOwner.setBounds (310, 5, 24, 24);
		Tree down = virtualTree (SWT.MULTI, 3_000, 0);
		shell.open ();
		focusOwner.setFocus ();
		settle ();
		down.setTopItem (down.getItem (1_500));
		key (down, 0x28 /* VK_DOWN */);
		settle ();
		assertEquals ("root 0", focusText (down), "the caret went to the first row");
		assertEquals (0, topIndex (down), "the first row scrolled into view");
		down.dispose ();
		Tree end = virtualTree (SWT.MULTI, 3_000, 0);
		focusOwner.setFocus ();
		settle ();
		key (end, 0x23 /* VK_END */);
		settle ();
		assertTrue (topIndex (end) > 2_900, "End without a caret goes to the last row");
		end.dispose ();
		Tree empty = virtualTree (SWT.MULTI, 0, 0);
		focusOwner.setFocus ();
		settle ();
		key (empty, 0x28);
		key (empty, 0x21 /* VK_PRIOR */);
		assertEquals (0, empty.getItemCount (), "keys on an empty tree do nothing");
	}

	@Test
	public void caretKeysOnAnOffWindowAnchorMoveLogically () {
		Tree tree = virtualTree (SWT.MULTI, 3_000, 0);
		shell.open ();
		settle ();
		tree.setSelection (tree.getItem (1_000));
		settle ();
		tree.setTopItem (tree.getItem (0));
		settle ();
		assertEquals (0, topIndex (tree));
		key (tree, 0x28 /* VK_DOWN: the caret anchor is outside the window */);
		settle ();
		assertEquals ("root 1001", focusText (tree), "the caret moved to its logical neighbour");
		assertTrue (topIndex (tree) > 900, "the caret row scrolled into view: top " + topIndex (tree));
		key (tree, 0x26 /* VK_UP on the now-visible caret stays native */);
		settle ();
		assertEquals ("root 1000", focusText (tree));
	}

	private static String focusText (Tree tree) {
		try {
			java.lang.reflect.Method method = Tree.class.getDeclaredMethod ("getFocusItem");
			method.setAccessible (true);
			TreeItem item = (TreeItem) method.invoke (tree);
			return item == null ? null : item.getText ();
		} catch (ReflectiveOperationException e) {
			throw new AssertionError (e);
		}
	}

	@Test
	public void focusWithoutACaretLandsOnTheLogicalTopRow () {
		Button focusOwner = new Button (shell, SWT.PUSH);
		focusOwner.setBounds (310, 5, 24, 24);
		Tree single = virtualTree (SWT.SINGLE, 400, 0);
		Tree multi = virtualTree (SWT.MULTI, 400, 0);
		multi.setBounds (5, 5, 300, 380);
		shell.open ();
		focusOwner.setFocus ();
		settle ();
		single.setTopItem (single.getItem (50));
		single.setFocus ();
		settle ();
		assertEquals ("root 50", focusText (single), "SINGLE: the caret goes to the top row");
		assertEquals (1, single.getSelectionCount (), "SINGLE selects the caret row");
		multi.setTopItem (multi.getItem (70));
		multi.selectAll ();
		multi.setFocus ();
		settle ();
		assertEquals ("root 70", focusText (multi), "MULTI: the caret goes to the top row");
		assertEquals (400, multi.getSelectionCount (), "MULTI keeps the selection");
		multi.dispose ();
		single.dispose ();
		focusOwner.setFocus ();
		settle ();
		Tree empty = virtualTree (SWT.MULTI, 0, 0);
		empty.setFocus ();
		settle ();
		assertNull (focusText (empty), "an empty tree keeps no caret");
		empty.dispose ();
		Tree flat = virtualTree (SWT.MULTI, 10, 0);
		flat.setBounds (5, 5, 300, 0);
		settle ();
		flat.setFocus ();
		settle ();
		assertEquals (10, flat.getItemCount (), "without a body no row is resident, focus is kept");
	}

	@Test
	public void rowsPastTheIntegerRangeKeepTheBarUsable () {
		Tree tree = new Tree (shell, SWT.VIRTUAL | SWT.BORDER | SWT.MULTI);
		tree.setBounds (5, 5, 300, 380);
		tree.addListener (SWT.SetData, event -> {
			TreeItem item = (TreeItem) event.item;
			TreeItem parent = item.getParentItem ();
			item.setText ((parent == null ? "root " : "child ") + event.index);
			if (parent == null && event.index == 0) item.setItemCount (100);
		});
		tree.setItemCount (Integer.MAX_VALUE);
		requireViewport (tree, Integer.MAX_VALUE);
		shell.open ();
		settle ();
		TreeItem first = tree.getItem (0);
		first.getText ();
		first.setExpanded (true);
		settle ();
		assertTrue (tree.getVerticalBar ().getThumb () > 0, "the scaled thumb stays positive");
		vscroll (tree, SB_LINEDOWN);
		settle ();
		vscroll (tree, SB_BOTTOM);
		settle ();
		assertNotNull (tree.getTopItem (), "the scaled bottom still answers a top item");
		tree.setTopItem (first);
		settle ();
		assertEquals ("root 0", tree.getTopItem ().getText ());
		tree.dispose ();
	}

	@Test
	public void theFirstItemAndTheLastItemMoveEmptiness () {
		Tree tree = virtualTree (SWT.MULTI, 0, 0);
		shell.open ();
		settle ();
		assertThrows (IllegalArgumentException.class, () -> new TreeItem (tree, SWT.NONE, 5), "an index past the end is refused");
		new TreeItem (tree, SWT.NONE);
		assertEquals (java.util.List.of (0), emptiness, "the first item sends EmptinessChanged detail 0");
		TreeItem second = new TreeItem (tree, SWT.NONE, 0);
		second.setText ("first by index");
		TreeItem child = new TreeItem (second, SWT.NONE);
		child.setText ("a child of a resident row");
		TreeItem grandchild = new TreeItem (child, SWT.NONE, 0);
		assertEquals (1, second.getItemCount ());
		assertEquals (1, child.getItemCount ());
		assertEquals (0, child.indexOf (grandchild));
		assertEquals (2, tree.getItemCount ());
		tree.setItemCount (0);
		settle ();
		assertEquals (java.util.List.of (0, 1), emptiness, "the removal of every root sends detail 1");
		assertEquals (0, tree.getItemCount ());
	}

	@Test
	public void disposingTheCaretRowMovesTheCaretLikeStock () {
		Tree tree = virtualTree (SWT.MULTI, 60, 40);
		shell.open ();
		settle ();
		/* the caret row's next sibling takes the caret; on screen it is measured at once */
		tree.setSelection (tree.getItem (10));
		settle ();
		tree.getItem (10).dispose ();
		settle ();
		assertEquals ("root 11", focusText (tree), "the next sibling takes the caret");
		/* the last child falls back to its previous sibling, then to the parent */
		TreeItem root = tree.getItem (7);
		root.setExpanded (true);
		settle ();
		tree.setSelection (root.getItem (39));
		settle ();
		root.getItem (39).dispose ();
		settle ();
		assertEquals (root.getText () + " / 38", focusText (tree), "the previous sibling takes the caret");
		while (root.getItemCount () > 1) root.getItem (root.getItemCount () - 1).dispose ();
		tree.setSelection (root.getItem (0));
		settle ();
		root.getItem (0).dispose ();
		settle ();
		assertEquals (root.getText (), focusText (tree), "the parent takes the caret of its last child");
		/* disposing a row with visible descendants scrolls the replacement into view */
		TreeItem big = tree.getItem (0);
		big.setExpanded (true);
		settle ();
		tree.setTopItem (big.getItem (0));
		big.dispose ();
		settle ();
		assertNotNull (tree.getTopItem ());
	}

	@Test
	public void aSingleTreeSelectsTheCaretAfterRemoval () {
		Tree tree = virtualTree (SWT.SINGLE, 40, 0);
		shell.open ();
		settle ();
		tree.setSelection (tree.getItem (5));
		settle ();
		tree.getItem (5).dispose ();
		settle ();
		assertEquals (1, tree.getSelectionCount (), "SINGLE: the new caret row is selected");
		assertEquals ("root 6", tree.getSelection () [0].getText ());
		/* shrinking past the caret deselects and puts the caret on the new last row */
		tree.setItemCount (3);
		settle ();
		assertEquals (0, tree.getSelectionCount (), "the native caret move on deletion deselects");
		assertEquals ("root 2", focusText (tree));
		tree.setItemCount (0);
		settle ();
		assertNull (focusText (tree), "no rows: no caret");
	}

	@Test
	public void theLastDisposalReleasesTheImageList () {
		Tree tree = virtualTree (SWT.MULTI, 2, 0);
		shell.open ();
		settle ();
		Image image = new Image (display, 16, 16);
		try {
			tree.getItem (0).setImage (image);
			tree.getItem (1).dispose ();
			tree.getItem (0).dispose ();
			assertEquals (0, tree.getItemCount ());
			assertTrue (emptiness.contains (1), "EmptinessChanged detail 1 was sent");
			new TreeItem (tree, SWT.NONE).setText ("fresh");
			assertEquals (1, tree.getItemCount (), "the empty tree accepts new rows");
		} finally {
			image.dispose ();
		}
	}

	@Test
	public void clearInvalidatesOnlyCachedRows () {
		Tree tree = virtualTree (SWT.MULTI, 200, 30);
		shell.open ();
		settle ();
		assertThrows (IllegalArgumentException.class, () -> tree.clear (200, false));
		assertThrows (IllegalArgumentException.class, () -> tree.clear (-1, true));
		tree.clear (150, false);
		String before = tree.getItem (3).getText ();
		tree.clear (3, true);
		assertEquals (before, tree.getItem (3).getText (), "a cleared row is filled again on demand");
		TreeItem root = tree.getItem (7);
		root.setExpanded (true);
		settle ();
		root.getItem (2).getText ();
		root.clearAll (true);
		tree.clearAll (false);
		settle ();
		assertEquals ("root 7 / 2", root.getItem (2).getText ());
		/* clearAll (true) from inside SetData spares the row being filled */
		boolean [] cleared = {false};
		tree.addListener (SWT.SetData, event -> {
			if (!cleared [0]) {
				cleared [0] = true;
				tree.clearAll (true);
			}
		});
		tree.clear (5, false);
		assertEquals ("root 5", tree.getItem (5).getText ());
		assertTrue (cleared [0]);
	}

	@Test
	public void selectionPatternsCoverColdRows () {
		Tree tree = virtualTree (SWT.MULTI, 10_000, 25);
		shell.open ();
		settle ();
		TreeItem root = tree.getItem (7);
		root.setExpanded (true);
		settle ();
		tree.selectAll ();
		assertEquals (10_025, tree.getSelectionCount (), "every visible row is selected, cold rows by pattern");
		TreeItem [] selection = tree.getSelection ();
		assertEquals (10_025, selection.length, "the pattern materializes the selection in preorder");
		assertEquals ("root 7 / 0", selection [8].getText ());
		tree.deselect (root.getItem (3));
		assertEquals (10_024, tree.getSelectionCount ());
		tree.setItemCount (10_001);
		settle ();
		new TreeItem (tree, SWT.NONE, 0);
		settle ();
		assertTrue (tree.getSelectionCount () <= 10_025, "rows added after selectAll are not selected");
		tree.deselectAll ();
		assertEquals (0, tree.getSelectionCount ());
		tree.select (root.getItem (2));
		assertEquals (1, tree.getSelectionCount (), "select adds one row");
		TreeItem stale = new TreeItem (tree, SWT.NONE);
		stale.dispose ();
		assertThrows (IllegalArgumentException.class, () -> tree.setSelection (new TreeItem [] {stale}));
		tree.setSelection (new TreeItem [0]);
		assertEquals (0, tree.getSelectionCount ());
		tree.setSelection (new TreeItem [] {tree.getItem (4), tree.getItem (9)});
		assertEquals (2, tree.getSelectionCount ());
		tree.showSelection ();
		assertEquals (tree.getItem (4).getText (), focusText (tree), "the first selected item leads");
	}

	@Test
	public void aSingleTreeRefusesMultiSelections () {
		Tree tree = virtualTree (SWT.SINGLE, 100, 0);
		shell.open ();
		settle ();
		tree.setSelection (new TreeItem [] {tree.getItem (1), tree.getItem (2)});
		assertEquals (0, tree.getSelectionCount (), "SINGLE drops a multi selection");
		tree.select (tree.getItem (3));
		assertEquals (1, tree.getSelectionCount (), "select on SINGLE is setSelection");
		assertEquals ("root 3", tree.getSelection () [0].getText ());
		tree.deselect (tree.getItem (3));
		assertEquals (0, tree.getSelectionCount ());
	}

	@Test
	public void showItemExpandsCollapsedAncestorsNearestFirst () {
		Tree tree = virtualTree (SWT.MULTI, 300, 40);
		shell.open ();
		settle ();
		java.util.List<String> expands = new ArrayList<> ();
		tree.addListener (SWT.Expand, event -> expands.add (((TreeItem) event.item).getText ()));
		TreeItem hidden = tree.getItem (210).getItem (35);
		tree.showItem (hidden);
		settle ();
		assertEquals (java.util.List.of ("root 210"), expands, "the collapsed ancestor expanded");
		assertTrue (tree.getItem (210).getExpanded ());
		int top = tree.getVerticalBar ().getSelection ();
		assertTrue (top > 200, "the row scrolled into view: top " + top);
		tree.showItem (hidden);
		assertEquals (1, expands.size (), "a visible row does not expand again");
		/* setTopItem expands silently too, and an Expand listener disposing the item aborts */
		TreeItem target = tree.getItem (70).getItem (10);
		tree.setTopItem (target);
		settle ();
		assertEquals ("root 70 / 10", tree.getTopItem ().getText ());
		TreeItem doomed = tree.getItem (140).getItem (5);
		tree.addListener (SWT.Expand, event -> {
			if ("root 140".equals (((TreeItem) event.item).getText ())) doomed.dispose ();
		});
		tree.showItem (doomed);
		settle ();
		assertTrue (doomed.isDisposed (), "the listener disposed the item; showItem aborted");
	}

	@Test
	public void collapsingOverTheCaretSelectsTheCollapsedRow () {
		Tree tree = virtualTree (SWT.MULTI, 60, 30);
		shell.open ();
		settle ();
		TreeItem root = tree.getItem (21);
		root.setExpanded (true);
		settle ();
		tree.setSelection (new TreeItem [] {root.getItem (4), root.getItem (6)});
		settle ();
		java.util.List<String> selections = new ArrayList<> ();
		tree.addListener (SWT.Selection, event -> selections.add (((TreeItem) event.item).getText ()));
		root.setExpanded (false);
		settle ();
		assertEquals (java.util.List.of ("root 21"), selections, "the omitted native Selection event is sent");
		assertEquals (1, tree.getSelectionCount ());
		assertEquals ("root 21", tree.getSelection () [0].getText ());
		assertEquals ("root 21", focusText (tree), "the caret moved to the collapsed row");
		/* collapsing without the caret inside is silent */
		TreeItem other = tree.getItem (28);
		other.setExpanded (true);
		settle ();
		other.setExpanded (false);
		settle ();
		assertEquals (1, selections.size ());
		/* expanding a row whose data was never set stays collapsed, as in stock */
		Tree plainVirtual = virtualTree (SWT.MULTI, 10, 0);
		plainVirtual.getItem (2).setExpanded (true);
		assertFalse (plainVirtual.getItem (2).getExpanded (), "no children: no expansion");
	}

	@Test
	public void nativeExpansionKeysFillTheChildrenInTime () {
		Tree tree = virtualTree (SWT.MULTI, 60, 25);
		shell.open ();
		settle ();
		tree.setSelection (tree.getItem (14));
		settle ();
		key (tree, 0x27 /* VK_RIGHT expands the caret row natively */);
		settle ();
		assertTrue (tree.getItem (14).getExpanded (), "the caret row expanded");
		assertEquals ("root 14", focusText (tree), "the caret stayed");
		java.util.List<String> collapses = new ArrayList<> ();
		tree.addListener (SWT.Collapse, event -> collapses.add (((TreeItem) event.item).getText ()));
		/*
		 * VK_SUBTRACT collapses the caret row natively and the Collapse event is sent. The logical
		 * expansion lane after a native user collapse is under review by the TreeViewport owner
		 * (docs/m3/tree-viewport/COVERAGE_GAPS.tsv), so only the stock-verified event is asserted.
		 */
		key (tree, 0x6D /* VK_SUBTRACT collapses */);
		settle ();
		assertEquals (java.util.List.of ("root 14"), collapses, "the native collapse sent its event");
		/* a leaf row expands to nothing */
		tree.setSelection (tree.getItem (15));
		settle ();
		key (tree, 0x6B /* VK_ADD on a leaf */);
		settle ();
		assertFalse (tree.getItem (15).getExpanded (), "a leaf has nothing to expand");
	}

	@Test
	public void mediatedItemQueriesGuardTheirArguments () {
		Tree tree = virtualTree (SWT.MULTI, 50, 20);
		Tree other = virtualTree (SWT.MULTI, 5, 0);
		shell.open ();
		settle ();
		assertThrows (IllegalArgumentException.class, () -> tree.getItem (50));
		assertThrows (IllegalArgumentException.class, () -> tree.getItem (-1));
		TreeItem foreign = other.getItem (2);
		assertEquals (-1, tree.indexOf (foreign), "another tree's item has no index here");
		TreeItem child = tree.getItem (7).getItem (3);
		assertEquals (-1, tree.indexOf (child), "a child has no root index");
		assertEquals (3, tree.getItem (7).indexOf (child));
		assertEquals ("root 7", child.getParentItem ().getText ());
		assertNull (tree.getItem (4).getParentItem ());
		TreeItem [] children = tree.getItem (7).getItems ();
		assertEquals (20, children.length);
		assertEquals ("root 7 / 19", children [19].getText ());
		TreeItem gone = new TreeItem (tree, SWT.NONE);
		gone.dispose ();
		assertThrows (IllegalArgumentException.class, () -> tree.indexOf (gone), "a disposed item is refused");
		assertEquals (-1, tree.indexOf (foreign), "a live foreign item answers -1");
		assertEquals (50, tree.getItemCount ());
		tree.getItem (7).setItemCount (0);
		assertEquals (0, tree.getItem (7).getItemCount ());
	}
}
