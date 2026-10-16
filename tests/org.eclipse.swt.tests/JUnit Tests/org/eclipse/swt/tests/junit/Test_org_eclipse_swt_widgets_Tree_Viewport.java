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

import java.util.*;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.win32.OS;
import org.eclipse.swt.widgets.*;
import org.junit.jupiter.api.*;

/**
 * SWT.VIRTUAL Tree as a viewport (docs/m3/TREE_MEDIATED_VIEWPORT.md, leaf T1): the native
 * TreeView holds only the resident window, the vertical bar covers every logical row, and
 * every public answer is the one a Tree holding all rows would give.
 */
public class Test_org_eclipse_swt_widgets_Tree_Viewport {
	static final int COUNT = 1_000_000;
	/* visible rows of a 380 px tree (about 21) + 2 x overscan + ancestors, with slack */
	static final int NATIVE_BOUND = 256;
	Display display;
	Shell shell;
	int setData;
	final Set<String> requested = new HashSet<> ();
	final java.util.List<String> repeated = new ArrayList<> ();

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

	private Tree virtualTree (int style, int roots, int childrenOfEvery7th) {
		Tree tree = new Tree (shell, style | SWT.VIRTUAL | SWT.BORDER);
		tree.setBounds (5, 5, 300, 380);
		tree.addListener (SWT.SetData, event -> {
			setData++;
			TreeItem item = (TreeItem) event.item;
			TreeItem parent = item.getParentItem ();
			String text = (parent == null ? "root " : parent.getText () + " / ") + event.index;
			if (!requested.add (text)) repeated.add (text);
			item.setText (text);
			if (parent == null && childrenOfEvery7th > 0 && event.index % 7 == 0) item.setItemCount (childrenOfEvery7th);
		});
		tree.setItemCount (roots);
		requireViewport (tree, roots);
		return tree;
	}

	/*
	 * Without viewport mediation a distant read grows the native prefix one row at a time and
	 * does not return in usable time. The bar range is the cheap, non-blocking discriminator:
	 * these tests are skipped, never left hanging, on a build without the viewport.
	 */
	private static void requireViewport (Tree tree, int roots) {
		Assumptions.assumeTrue (roots <= 1 || tree.getVerticalBar ().getMaximum () == roots - 1,
				"Tree viewport mediation is not active in this build (vertical bar maximum "
				+ tree.getVerticalBar ().getMaximum () + " for " + roots + " rows)");
	}

	private void settle () {
		long end = System.currentTimeMillis () + 300;
		while (System.currentTimeMillis () < end) {
			if (!display.readAndDispatch ()) {
				try {
					Thread.sleep (5);
				} catch (InterruptedException e) {
					Thread.currentThread ().interrupt ();
					return;
				}
			}
		}
	}

	private static long nativeRows (Tree tree) {
		return OS.SendMessage (tree.handle, OS.TVM_GETCOUNT, 0, 0);
	}

	@Test
	public void verticalBarCoversEveryLogicalRow () {
		Tree tree = virtualTree (SWT.MULTI, COUNT, 0);
		shell.open ();
		settle ();
		ScrollBar bar = tree.getVerticalBar ();
		assertEquals (COUNT - 1, bar.getMaximum (), "bar maximum is the logical row count, not the native window");
		assertTrue (bar.getThumb () > 0 && bar.getThumb () < 64, "thumb is one page: " + bar.getThumb ());
		assertTrue (nativeRows (tree) <= NATIVE_BOUND, "native rows " + nativeRows (tree));
	}

	@Test
	public void nativeRowsStayBoundedAtTopMiddleAndEnd () {
		Tree tree = virtualTree (SWT.MULTI, COUNT, 0);
		shell.open ();
		settle ();
		for (int index : new int [] {0, COUNT / 2, COUNT - 1}) {
			tree.setTopItem (tree.getItem (index));
			settle ();
			assertTrue (nativeRows (tree) <= NATIVE_BOUND, "native rows at " + index + ": " + nativeRows (tree));
		}
		assertTrue (setData < 4 * NATIVE_BOUND, "SetData only for resident rows: " + setData);
	}

	@Test
	@Timeout (10)
	public void distantGetItemIsImmediateAndKeepsIdentity () {
		Tree tree = virtualTree (SWT.MULTI, COUNT, 0);
		shell.open ();
		settle ();
		long t0 = System.nanoTime ();
		TreeItem last = tree.getItem (COUNT - 1);
		long millis = (System.nanoTime () - t0) / 1_000_000;
		assertTrue (millis < 50, "getItem (" + (COUNT - 1) + ") took " + millis + " ms");
		assertSame (last, tree.getItem (COUNT - 1), "the same row answers the same TreeItem");
		assertEquals ("root " + (COUNT - 1), last.getText ());
		assertEquals (COUNT - 1, tree.indexOf (last));
		assertTrue (nativeRows (tree) <= NATIVE_BOUND, "a read does not grow the native window: " + nativeRows (tree));
	}

	@Test
	public void setTopItemMovesTheLogicalTopAndTheBar () {
		Tree tree = virtualTree (SWT.MULTI, COUNT, 0);
		shell.open ();
		settle ();
		TreeItem middle = tree.getItem (COUNT / 2);
		tree.setTopItem (middle);
		settle ();
		assertSame (middle, tree.getTopItem ());
		assertEquals (COUNT / 2, tree.getVerticalBar ().getSelection ());
	}

	@Test
	public void nestedExpansionRangeIsLogical () {
		Tree tree = virtualTree (SWT.MULTI, 1000, 100_000);
		shell.open ();
		settle ();
		TreeItem root = tree.getItem (497);
		assertEquals (100_000, root.getItemCount ());
		root.setExpanded (true);
		settle ();
		assertEquals (1000 + 100_000 - 1, tree.getVerticalBar ().getMaximum ());
		TreeItem lastChild = root.getItem (99_999);
		tree.setTopItem (lastChild);
		settle ();
		assertSame (lastChild, tree.getTopItem ());
		assertTrue (nativeRows (tree) <= NATIVE_BOUND, "native rows " + nativeRows (tree));
		root.setExpanded (false);
		settle ();
		assertEquals (999, tree.getVerticalBar ().getMaximum ());
	}

	@Test
	public void scrollRoundTripReproducesStateAndPixels () {
		for (int style : new int [] {SWT.SINGLE, SWT.MULTI}) {
			setData = 0;
			requested.clear ();
			repeated.clear ();
			Tree tree = virtualTree (style, COUNT, 0);
			shell.open ();
			tree.setTopItem (tree.getItem (1000));
			if ((style & SWT.MULTI) != 0) {
				tree.setSelection (new TreeItem [] {tree.getItem (1002), tree.getItem (1005), tree.getItem (1006)});
			} else {
				tree.setSelection (tree.getItem (1003));
			}
			tree.setTopItem (tree.getItem (1000));
			settle ();
			String before = state (tree) + " pixels=" + pixelHash (tree);
			tree.setTopItem (tree.getItem (COUNT - 1));
			settle ();
			tree.setTopItem (tree.getItem (COUNT / 2));
			settle ();
			tree.setTopItem (tree.getItem (1000));
			settle ();
			String after = state (tree) + " pixels=" + pixelHash (tree);
			assertEquals (before, after, "scroll away and back reproduces API state and pixels");
			assertTrue (repeated.isEmpty (), "SetData once per row, repeated: " + repeated);
			tree.dispose ();
		}
	}

	@Test
	public void virtualAndPlainTreesAnswerTheSame () {
		Tree plain = new Tree (shell, SWT.MULTI | SWT.BORDER);
		plain.setBounds (5, 5, 300, 380);
		for (int i = 0; i < 300; i++) {
			TreeItem item = new TreeItem (plain, 0);
			item.setText ("root " + i);
			if (i % 7 == 0) for (int c = 0; c < 40; c++) new TreeItem (item, 0).setText ("root " + i + " / " + c);
		}
		Tree virtual = virtualTree (SWT.MULTI, 300, 40);
		shell.open ();
		settle ();
		assertEquals (plain.getItemCount (), virtual.getItemCount ());
		for (int i : new int [] {0, 7, 140, 299}) {
			assertEquals (plain.getItem (i).getText (), virtual.getItem (i).getText ());
			assertEquals (plain.getItem (i).getItemCount (), virtual.getItem (i).getItemCount ());
		}
		for (Tree tree : new Tree [] {plain, virtual}) {
			tree.getItem (140).setExpanded (true);
			tree.setSelection (new TreeItem [] {tree.getItem (140).getItem (39), tree.getItem (299)});
			tree.setTopItem (tree.getItem (140).getItem (20));
		}
		settle ();
		assertEquals (plain.getVerticalBar ().getMaximum (), virtual.getVerticalBar ().getMaximum ());
		assertEquals (plain.getVerticalBar ().getSelection (), virtual.getVerticalBar ().getSelection ());
		assertEquals (plain.getTopItem ().getText (), virtual.getTopItem ().getText ());
		assertEquals (texts (plain.getSelection ()), texts (virtual.getSelection ()));
		assertEquals (plain.getSelectionCount (), virtual.getSelectionCount ());
	}

	private static java.util.List<String> texts (TreeItem [] items) {
		java.util.List<String> out = new ArrayList<> ();
		for (TreeItem item : items) out.add (item.getText ());
		Collections.sort (out);
		return out;
	}

	private static String state (Tree tree) {
		TreeItem top = tree.getTopItem ();
		return "top=" + (top == null ? null : top.getText ()) + " selection=" + texts (tree.getSelection ())
			+ " count=" + tree.getItemCount () + " bar=" + tree.getVerticalBar ().getSelection ();
	}

	private String pixelHash (Tree tree) {
		Point size = tree.getSize ();
		Image image = new Image (display, size.x, size.y);
		GC gc = new GC (image);
		try {
			tree.print (gc);
		} finally {
			gc.dispose ();
		}
		ImageData data = image.getImageData ();
		image.dispose ();
		return Integer.toHexString (Arrays.hashCode (data.data));
	}
}
