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
import org.eclipse.swt.widgets.*;
import org.junit.jupiter.api.*;

/**
 * Recorded differential of the Tree viewport (docs/m3/TREE_MEDIATED_VIEWPORT.md): seeded random
 * scripts of public API operations run against a plain Tree and a viewport Tree holding the
 * same rows. Every answer is recorded per step and must be identical; on a mismatch the test
 * fails with the operation log, which is the reproduction.
 */
public class Test_org_eclipse_swt_widgets_Tree_ViewportDifferential {
	static final int ROOTS = 300, CHILDREN = 40, SEEDS = 12, STEPS = 150;
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

	static boolean hasChildren (int root) {
		return root % 7 == 0;
	}

	/*
	 * The reference: a native SWT.VIRTUAL Tree with the same SetData, created while the viewport is
	 * the default (the win32 Tree viewport's test hook). Virtual and non-virtual native Trees differ
	 * (getSelectionCount of a non-virtual MULTI Tree also counts rows under collapsed parents), so
	 * the reference has the same style as the Tree under test.
	 */
	private Tree plainTree () {
		setNativeReference (true);
		try {
			return virtualTree ();
		} finally {
			setNativeReference (false);
		}
	}

	private static void setNativeReference (boolean value) {
		try {
			java.lang.reflect.Field field = Class.forName ("org.eclipse.swt.widgets.TreeViewport").getDeclaredField ("nativeReference");
			field.setAccessible (true);
			field.setBoolean (null, value);
		} catch (ClassNotFoundException e) {
			/* no Tree viewport on this platform: every Tree is native */
		} catch (ReflectiveOperationException e) {
			throw new IllegalStateException (e);
		}
	}

	private Tree viewportTree () {
		Tree tree = virtualTree ();
		Assumptions.assumeTrue (tree.getVerticalBar ().getMaximum () == ROOTS - 1, "Tree viewport mediation is not active in this build");
		return tree;
	}

	private Tree virtualTree () {
		Tree tree = new Tree (shell, SWT.MULTI | SWT.BORDER | SWT.VIRTUAL);
		tree.setBounds (5, 5, 300, 380);
		tree.addListener (SWT.SetData, event -> {
			TreeItem item = (TreeItem) event.item;
			TreeItem parent = item.getParentItem ();
			item.setText ((parent == null ? "root " : parent.getText () + " / ") + event.index);
			if (parent == null && hasChildren (event.index)) item.setItemCount (CHILDREN);
		});
		tree.setItemCount (ROOTS);
		return tree;
	}

	private void settle () {
		long end = System.currentTimeMillis () + 120;
		while (System.currentTimeMillis () < end) {
			if (!display.readAndDispatch ()) {
				try {
					Thread.sleep (2);
				} catch (InterruptedException e) {
					Thread.currentThread ().interrupt ();
					return;
				}
			}
		}
	}

	/** One scripted operation, applied identically to both trees; returns the recorded answer. */
	interface Op {
		String apply (Tree tree);
	}

	static TreeItem itemAt (Tree tree, int root, int child) {
		TreeItem item = tree.getItem (root);
		return child < 0 || item.getItemCount () == 0 ? item : item.getItem (Math.min (child, item.getItemCount () - 1));
	}

	static String text (TreeItem item) {
		return item == null ? "null" : item.getText ();
	}

	static String selection (Tree tree) {
		java.util.List<String> texts = new ArrayList<> ();
		for (TreeItem item : tree.getSelection ()) texts.add (item.getText ());
		return tree.getSelectionCount () + texts.toString ();
	}

	/* the caret item (package-private Tree.getFocusItem), which keyboard and focus behaviour start from */
	static String focus (Tree tree) {
		try {
			java.lang.reflect.Method method = Tree.class.getDeclaredMethod ("getFocusItem");
			method.setAccessible (true);
			return text ((TreeItem) method.invoke (tree));
		} catch (ReflectiveOperationException e) {
			throw new IllegalStateException (e);
		}
	}

	static String bar (Tree tree) {
		ScrollBar bar = tree.getVerticalBar ();
		return "bar " + bar.getSelection () + "/" + bar.getMaximum () + " thumb " + bar.getThumb ();
	}

	/*
	 * Geometry a client sees: bounds of the top item and two hit tests (rows 0 and 5). The native
	 * TreeView measures a label when it paints the row, so geometry is read after a paint.
	 */
	static String geometry (Tree tree) {
		tree.update ();
		TreeItem top = tree.getTopItem ();
		int height = tree.getItemHeight ();
		String cell = top == null ? "-" : top.getBounds (0) + " img " + top.getImageBounds (0) + " txt " + top.getTextBounds (0);
		return "geo " + (top == null ? "-" : top.getBounds ().toString ()) + " cell " + cell + " hit0 " + text (tree.getItem (new Point (30, height / 2)))
			+ " hit5 " + text (tree.getItem (new Point (30, 5 * height + height / 2)));
	}

	static Op randomOp (Random random, StringBuilder log) {
		int root = random.nextInt (ROOTS), child = random.nextInt (3) == 0 ? -1 : random.nextInt (CHILDREN);
		int kind = random.nextInt (13);
		String name = switch (kind) {
			case 0 -> "expand " + root;
			case 1 -> "collapse " + root;
			case 2 -> "setTopItem " + root + "/" + child;
			case 3 -> "showItem " + root + "/" + child;
			case 4 -> "setSelection " + root + "/" + child;
			case 5 -> "select+ " + root + "/" + child;
			case 6 -> "deselect " + root + "/" + child;
			case 7 -> "deselectAll";
			case 8 -> "read " + root + "/" + child;
			case 9 -> "showSelection";
			case 10 -> "getTopItem";
			case 12 -> "selectAll";
			default -> "parentOf " + root + "/" + child;
		};
		log.append (name).append ('\n');
		return tree -> switch (kind) {
			case 0 -> { tree.getItem (root).setExpanded (true); yield "expanded=" + tree.getItem (root).getExpanded (); }
			case 1 -> { tree.getItem (root).setExpanded (false); yield "expanded=" + tree.getItem (root).getExpanded (); }
			case 2 -> { TreeItem item = itemAt (tree, root, child); tree.setTopItem (item); yield "top=" + text (tree.getTopItem ()); }
			case 3 -> { tree.showItem (itemAt (tree, root, child)); yield "top=" + text (tree.getTopItem ()); }
			case 4 -> { tree.setSelection (itemAt (tree, root, child)); yield selection (tree); }
			case 5 -> { tree.select (itemAt (tree, root, child)); yield selection (tree); }
			case 6 -> { tree.deselect (itemAt (tree, root, child)); yield selection (tree); }
			case 7 -> { tree.deselectAll (); yield selection (tree); }
			case 8 -> {
				TreeItem item = itemAt (tree, root, child);
				TreeItem parent = item.getParentItem ();
				yield text (item) + " count=" + item.getItemCount () + " index=" + (parent == null ? tree.indexOf (item) : parent.indexOf (item))
					+ " expanded=" + item.getExpanded ();
			}
			case 9 -> { tree.showSelection (); yield "top=" + text (tree.getTopItem ()); }
			case 10 -> "top=" + text (tree.getTopItem ());
			case 12 -> { tree.selectAll (); yield selection (tree); }
			default -> "parent=" + text (itemAt (tree, root, child).getParentItem ());
		};
	}

	@Test
	public void seededScriptsAnswerLikeAPlainTree () {
		Tree plain = plainTree ();
		Tree viewport = viewportTree ();
		StringBuilder plainEvents = new StringBuilder (), viewportEvents = new StringBuilder ();
		record (plain, plainEvents);
		record (viewport, viewportEvents);
		shell.open ();
		/* both trees receive focus once: the caret a tree gets on focus is part of the behaviour */
		viewport.setFocus ();
		plain.setFocus ();
		settle ();
		for (int seed = 0; seed < SEEDS; seed++) {
			Random random = new Random (0x7EE5EEDL + seed);
			StringBuilder log = new StringBuilder ("seed " + seed + "\n");
			for (Tree tree : new Tree [] {plain, viewport}) {
				tree.deselectAll ();
				for (int i = 0; i < ROOTS; i += 7) tree.getItem (i).setExpanded (false);
				tree.setTopItem (tree.getItem (0));
			}
			for (int step = 0; step < STEPS; step++) {
				Op op = randomOp (random, log);
				/* the full selection after every step surfaces a divergence next to its cause */
				plainEvents.setLength (0);
				viewportEvents.setLength (0);
				String expected = op.apply (plain) + " | " + bar (plain) + " | sel " + selection (plain) + " | focus " + focus (plain) + " | events " + plainEvents + " | " + geometry (plain);
				String actual = op.apply (viewport) + " | " + bar (viewport) + " | sel " + selection (viewport) + " | focus " + focus (viewport) + " | events " + viewportEvents + " | " + geometry (viewport);
				if (!expected.equals (actual)) {
					fail ("step " + step + " differs\n  plain:    " + expected + "\n  viewport: " + actual + "\nlog:\n" + log);
				}
			}
			settle ();
			ImageData expectedPixels = pixels (plain, viewport, true), actualPixels = pixels (plain, viewport, false);
			if (!Arrays.equals (expectedPixels.data, actualPixels.data)) {
				fail ("printed pixels after seed " + seed + " differ" + saveEvidence (seed, expectedPixels, actualPixels) + "\nlog:\n" + log);
			}
		}
	}

	/* Expand, Collapse, Selection and DefaultSelection, in order, with the item text */
	private static void record (Tree tree, StringBuilder out) {
		for (int type : new int [] {SWT.Expand, SWT.Collapse, SWT.Selection, SWT.DefaultSelection}) {
			tree.addListener (type, event -> out.append (switch (event.type) {
				case SWT.Expand -> "E:";
				case SWT.Collapse -> "C:";
				case SWT.Selection -> "S:";
				default -> "D:";
			}).append (event.item instanceof TreeItem item ? item.getText () : "-").append (' '));
		}
	}

	/* both images as PNG files (directory: system property swt.viewport.evidence, default java.io.tmpdir) */
	private static String saveEvidence (int seed, ImageData expected, ImageData actual) {
		String directory = System.getProperty ("swt.viewport.evidence", System.getProperty ("java.io.tmpdir"));
		StringBuilder paths = new StringBuilder ();
		for (ImageData data : new ImageData [] {expected, actual}) {
			String path = new java.io.File (directory, "tree-viewport-seed" + seed + (data == expected ? "-plain.png" : "-viewport.png")).getPath ();
			ImageLoader loader = new ImageLoader ();
			loader.data = new ImageData [] {data};
			loader.save (path, SWT.IMAGE_PNG);
			paths.append (paths.length () == 0 ? " (" : ", ").append (path);
		}
		return paths.append (')').toString ();
	}

	/* print one tree while the other is hidden and focus is on the printed one */
	private ImageData pixels (Tree plain, Tree viewport, boolean ofPlain) {
		Tree shown = ofPlain ? plain : viewport;
		/* hot tracking highlights the row under the pointer: keep the pointer away from the trees */
		display.setCursorLocation (shell.getBounds ().x + shell.getBounds ().width + 40, shell.getBounds ().y);
		(ofPlain ? viewport : plain).setVisible (false);
		shown.setVisible (true);
		shown.setFocus ();
		settle ();
		Point size = shown.getSize ();
		Image image = new Image (display, size.x, size.y);
		GC gc = new GC (image);
		try {
			shown.print (gc);
		} finally {
			gc.dispose ();
		}
		ImageData data = image.getImageData ();
		image.dispose ();
		plain.setVisible (true);
		viewport.setVisible (true);
		return data;
	}
}
