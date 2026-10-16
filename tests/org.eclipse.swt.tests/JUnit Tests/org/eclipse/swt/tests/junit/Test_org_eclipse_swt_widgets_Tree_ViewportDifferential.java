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

import java.util.*;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.junit.jupiter.api.*;

/**
 * Recorded differential of the Tree viewport (docs/m3/TREE_MEDIATED_VIEWPORT.md) on the shared harness
 * {@link ViewportDifferential}: seeded random scripts of public API operations run on a SWT.VIRTUAL Tree.
 * Every step records the answer, the scroll bar, the selection, the caret, the events and the geometry a
 * client sees; every seed records the pixels. The reference is stock SWT: a run on the official SWT jar
 * with <code>-Dswt.widget.mode=original</code> records one file per seed under
 * <code>-Dswt.viewport.recording</code>, and the run with the viewport compares against it. On a mismatch
 * the test fails with the operation log, which is the reproduction, and saves the pixels as PNG.
 */
public class Test_org_eclipse_swt_widgets_Tree_ViewportDifferential {
	static final int ROOTS = 300, CHILDREN = 40, SEEDS = 12, STEPS = 150;
	Display display;
	Shell shell;

	/** The Tree under test and its event log. */
	record Subject (Tree tree, StringBuilder events) {
	}

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

	/*
	 * Geometry a client sees: bounds of the top item, its cell, image and text bounds, and two hit tests
	 * (rows 0 and 5). The native TreeView measures a label when it paints the row, so geometry is read
	 * after a paint.
	 */
	static String geometry (Tree tree) {
		tree.update ();
		TreeItem top = tree.getTopItem ();
		int height = tree.getItemHeight ();
		String cell = top == null ? "-" : top.getBounds (0) + " img " + top.getImageBounds (0) + " txt " + top.getTextBounds (0);
		return (top == null ? "-" : top.getBounds ().toString ()) + " cell " + cell + " hit0 " + text (tree.getItem (new Point (30, height / 2)))
			+ " hit5 " + text (tree.getItem (new Point (30, 5 * height + height / 2)));
	}

	static ViewportDifferential.Op<Subject> randomOp (Random random, StringBuilder log) {
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
		return subject -> {
			Tree tree = subject.tree ();
			return switch (kind) {
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
		};
	}

	private ViewportDifferential<Subject> harness () {
		return ViewportDifferential.<Subject>of ("tree-viewport", 0x7EE5EEDL, Test_org_eclipse_swt_widgets_Tree_ViewportDifferential::randomOp)
			.widget ("Tree").seeds (SEEDS).steps (STEPS)
			.beforeSeed (subject -> {
				Tree tree = subject.tree ();
				tree.deselectAll ();
				for (int i = 0; i < ROOTS; i += 7) tree.getItem (i).setExpanded (false);
				tree.setTopItem (tree.getItem (0));
			})
			.beforeStep (subject -> subject.events ().setLength (0))
			.probe ("bar", subject -> ViewportDifferential.bar (subject.tree ().getVerticalBar ()))
			.probe ("sel", subject -> selection (subject.tree ()))
			.probe ("focus", subject -> focus (subject.tree ()))
			.probe ("events", subject -> subject.events ().toString ())
			.probe ("geo", subject -> geometry (subject.tree ()))
			.pixels (subject -> {
				subject.tree ().setFocus ();
				ViewportDifferential.parkCursor (display);
				ViewportDifferential.settle (display, 150);
				return renderTop (subject.tree ());
			});
	}

	/*
	 * The whole control as the user sees it. A viewport Tree is its viewport window (border and scroll bar)
	 * with the TreeView inside, so the top window is printed, with its children.
	 */
	private static ImageData renderTop (Tree tree) {
		try {
			java.lang.reflect.Field field = Tree.class.getDeclaredField ("hwndParent");
			field.setAccessible (true);
			if (field.getLong (tree) == 0) return ViewportDifferential.render (tree);
			long hwnd = field.getLong (tree);
			Point size = tree.getSize ();
			Image image = new Image (tree.getDisplay (), Math.max (1, size.x), Math.max (1, size.y));
			GC gc = new GC (image);
			try {
				java.lang.reflect.Field handle = GC.class.getDeclaredField ("handle");
				handle.setAccessible (true);
				Class<?> os = Class.forName ("org.eclipse.swt.internal.win32.OS");
				os.getMethod ("SendMessage", long.class, int.class, long.class, long.class)
					.invoke (null, hwnd, 0x0317, handle.getLong (gc), (long) (0x2 | 0x4 | 0x8 | 0x10));
			} finally {
				gc.dispose ();
			}
			ImageData data = image.getImageData ();
			image.dispose ();
			return data;
		} catch (NoSuchFieldException e) {
			return ViewportDifferential.render (tree);
		} catch (ReflectiveOperationException e) {
			throw new IllegalStateException (e);
		}
	}

	/* Expand, Collapse, Selection and DefaultSelection, in order, with the item text */
	private Subject subject (Tree tree) {
		StringBuilder events = new StringBuilder ();
		for (int type : new int [] {SWT.Expand, SWT.Collapse, SWT.Selection, SWT.DefaultSelection}) {
			tree.addListener (type, event -> events.append (switch (event.type) {
				case SWT.Expand -> "E:";
				case SWT.Collapse -> "C:";
				case SWT.Selection -> "S:";
				default -> "D:";
			}).append (event.item instanceof TreeItem item ? item.getText () : "-").append (' '));
		}
		return new Subject (tree, events);
	}

	/**
	 * Stock reference: run on the official SWT jar with <code>-Dswt.widget.mode=original</code> to record;
	 * run on this build (viewport, the default) to compare. Skips without <code>-Dswt.viewport.recording</code>.
	 */
	@Test
	public void seededScriptsAnswerLikeStockSwt () throws java.io.IOException {
		boolean record = ViewportDifferential.isReference ("Tree");
		Tree tree = virtualTree ();
		if (!record) {
			Assumptions.assumeTrue (tree.getVerticalBar ().getMaximum () == ROOTS - 1, "Tree viewport mediation is not active in this build");
		}
		Subject subject = subject (tree);
		shell.open ();
		/* the Tree receives focus once: the caret a tree gets on focus is part of the behaviour */
		tree.setFocus ();
		ViewportDifferential.settle (display, 120);
		harness ().compareWithRecording (subject, record, System.getProperty ("swt.viewport.recording"));
	}
}
