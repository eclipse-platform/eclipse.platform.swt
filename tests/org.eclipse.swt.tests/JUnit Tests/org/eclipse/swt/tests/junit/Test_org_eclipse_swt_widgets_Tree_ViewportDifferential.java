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
	static final int ROOTS = 300, CHILDREN = 40, SEEDS = 12, STEPS = 150, COLUMNS = 3;
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
		return virtualTree (false);
	}

	/* columns: SWT.CHECK, FULL_SELECTION, a visible header and three columns wider than the client (horizontal scrolling) */
	private Tree virtualTree (boolean columns) {
		Tree tree = new Tree (shell, SWT.MULTI | SWT.BORDER | SWT.VIRTUAL | (columns ? SWT.CHECK | SWT.FULL_SELECTION : 0));
		tree.setBounds (5, 5, 300, 380);
		if (columns) {
			tree.setHeaderVisible (true);
			tree.setLinesVisible (true);
			for (int c = 0; c < COLUMNS; c++) {
				TreeColumn column = new TreeColumn (tree, SWT.NONE);
				column.setText ("column " + c);
				column.setWidth (c == 0 ? 160 : 120);
			}
		}
		tree.addListener (SWT.SetData, event -> {
			TreeItem item = (TreeItem) event.item;
			TreeItem parent = item.getParentItem ();
			String name = (parent == null ? "root " : parent.getText () + " / ") + event.index;
			if (columns) {
				item.setText (new String [] {name, name + " b", name + " c"});
				item.setChecked (event.index % 3 == 0);
			} else {
				item.setText (name);
			}
			if (parent == null && hasChildren (event.index)) item.setItemCount (CHILDREN);
		});
		tree.setItemCount (ROOTS);
		return tree;
	}

	/* keys the native TreeView handles itself (sent as WM_KEYDOWN/WM_KEYUP: no real input on the test desktop) */
	static final int [] KEYS = {0x26, 0x28, 0x21, 0x22, 0x24, 0x23, 0x25, 0x27, 0x6B, 0x6D, 0x20};
	static final String [] KEY_NAMES = {"UP", "DOWN", "PRIOR", "NEXT", "HOME", "END", "LEFT", "RIGHT", "ADD", "SUBTRACT", "SPACE"};

	static TreeItem itemAt (Tree tree, int root, int child) {
		TreeItem item = tree.getItem (Math.min (root, tree.getItemCount () - 1));
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
		return randomOp (random, log, false);
	}

	static ViewportDifferential.Op<Subject> randomColumnsOp (Random random, StringBuilder log) {
		return randomOp (random, log, true);
	}

	static ViewportDifferential.Op<Subject> randomOp (Random random, StringBuilder log, boolean columns) {
		int root = random.nextInt (ROOTS), child = random.nextInt (3) == 0 ? -1 : random.nextInt (CHILDREN);
		int kind = random.nextInt (columns ? 25 : 19);
		int key = random.nextInt (KEYS.length), delta = random.nextInt (7) - 3;
		int column = columns ? random.nextInt (COLUMNS) : 0, width = columns ? 40 + random.nextInt (160) : 0;
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
			case 13 -> "key " + KEY_NAMES [key];
			case 14 -> "wheel " + (delta < 0 ? "up" : "down");
			case 15 -> "insertRoot " + root;
			case 16 -> "disposeRoot " + root;
			case 17 -> "setItemCount " + delta;
			case 18 -> "clear " + root;
			case 19 -> "check " + root + "/" + child;
			case 20 -> "gray " + root + "/" + child;
			case 21 -> "columnWidth " + column + " " + width;
			case 22 -> "columnOrder";
			case 23 -> "showColumn " + column;
			case 24 -> "sort " + column + " " + delta;
			default -> "parentOf " + root + "/" + child;
		};
		log.append (name).append ('\n');
		return subject -> {
			Tree tree = subject.tree ();
			if (tree.getItemCount () == 0 && kind != 15 && kind != 17 && kind != 13 && kind != 14 && kind != 7 && kind != 12 && kind != 10) return "empty";
			try {
				return kind >= 19 ? applyColumns (tree, kind, root, child, column, width, delta) : apply (tree, kind, root, child, key, delta);
			} catch (IllegalArgumentException | org.eclipse.swt.SWTException e) {
				return "error " + e.getMessage ();
			}
		};
	}

	static String apply (Tree tree, int kind, int root, int child, int key, int delta) {
		int r = Math.min (root, Math.max (0, tree.getItemCount () - 1));
		return switch (kind) {
				case 0 -> { tree.getItem (r).setExpanded (true); yield "expanded=" + tree.getItem (r).getExpanded (); }
				case 1 -> { tree.getItem (r).setExpanded (false); yield "expanded=" + tree.getItem (r).getExpanded (); }
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
				case 13 -> {
					ViewportDifferential.send (tree, 0x0100, KEYS [key], 0);
					ViewportDifferential.send (tree, 0x0101, KEYS [key], 0xC0000001L);
					yield "top=" + text (tree.getTopItem ());
				}
				case 14 -> {
					Point center = tree.toDisplay (tree.getSize ().x / 2, tree.getSize ().y / 2);
					ViewportDifferential.send (tree, 0x020A, ((long) (delta < 0 ? 120 : -120) & 0xFFFF) << 16, ViewportDifferential.makeLParam (center.x, center.y));
					yield "top=" + text (tree.getTopItem ());
				}
				case 15 -> { new TreeItem (tree, SWT.NONE, Math.min (root, tree.getItemCount ())); yield "count=" + tree.getItemCount (); }
				case 16 -> { tree.getItem (r).dispose (); yield "count=" + tree.getItemCount (); }
				case 17 -> { tree.setItemCount (Math.max (0, tree.getItemCount () + delta)); yield "count=" + tree.getItemCount (); }
				case 18 -> { tree.clear (r, false); yield text (tree.getItem (r)); }
				default -> "parent=" + text (itemAt (tree, root, child).getParentItem ());
		};
	}

	static String applyColumns (Tree tree, int kind, int root, int child, int column, int width, int delta) {
		return switch (kind) {
			case 19 -> { TreeItem item = itemAt (tree, root, child); item.setChecked (!item.getChecked ()); yield checks (item); }
			case 20 -> { TreeItem item = itemAt (tree, root, child); item.setGrayed (!item.getGrayed ()); yield checks (item); }
			case 21 -> { tree.getColumn (column).setWidth (width); yield "width=" + tree.getColumn (column).getWidth (); }
			case 22 -> {
				int [] order = tree.getColumnOrder ();
				int [] rotated = new int [order.length];
				for (int i = 0; i < order.length; i++) rotated [i] = order [(i + 1) % order.length];
				tree.setColumnOrder (rotated);
				yield "order=" + Arrays.toString (tree.getColumnOrder ());
			}
			case 23 -> { tree.showColumn (tree.getColumn (column)); yield "hbar " + hbar (tree); }
			default -> {
				tree.setSortColumn (tree.getColumn (column));
				tree.setSortDirection (delta < 0 ? SWT.UP : delta > 0 ? SWT.DOWN : SWT.NONE);
				yield "sort=" + tree.indexOf (tree.getSortColumn ()) + " " + tree.getSortDirection ();
			}
		};
	}

	static String checks (TreeItem item) {
		return item == null ? "-" : (item.getChecked () ? "C" : "c") + (item.getGrayed () ? "G" : "g");
	}

	static String hbar (Tree tree) {
		ScrollBar bar = tree.getHorizontalBar ();
		return bar == null ? "-" : ViewportDifferential.bar (bar);
	}

	/* check state of the rows at the first five hit positions, and cell geometry in columns 1 and 2 */
	static String columnState (Tree tree) {
		tree.update ();
		int height = tree.getItemHeight ();
		StringBuilder out = new StringBuilder ();
		for (int row = 0; row < 5; row++) out.append (checks (tree.getItem (new Point (5, row * height + height / 2)))).append (' ');
		TreeItem top = tree.getTopItem ();
		if (top != null) out.append ("c1 ").append (top.getBounds (1)).append (" c2 ").append (top.getTextBounds (2));
		return out.toString ();
	}

	private ViewportDifferential<Subject> harness () {
		return harness (false);
	}

	private ViewportDifferential<Subject> harness (boolean columns) {
		ViewportDifferential<Subject> harness = ViewportDifferential.<Subject>of (columns ? "tree-viewport-columns" : "tree-viewport", 0x7EE5EEDL,
				columns ? Test_org_eclipse_swt_widgets_Tree_ViewportDifferential::randomColumnsOp : Test_org_eclipse_swt_widgets_Tree_ViewportDifferential::randomOp)
			.widget ("Tree").seeds (SEEDS).steps (STEPS)
			.beforeSeed (subject -> {
				Tree tree = subject.tree ();
				tree.deselectAll ();
				tree.setItemCount (0);
				tree.setItemCount (ROOTS);
				if (columns) {
					tree.setColumnOrder (new int [] {0, 1, 2});
					for (int c = 0; c < COLUMNS; c++) tree.getColumn (c).setWidth (c == 0 ? 160 : 120);
					tree.setSortColumn (null);
					tree.setSortDirection (SWT.NONE);
					tree.showColumn (tree.getColumn (0));
				}
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
		if (columns) harness.probe ("hbar", subject -> hbar (subject.tree ())).probe ("cols", subject -> columnState (subject.tree ()));
		return harness;
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
				case SWT.Selection -> event.detail == SWT.CHECK ? "K:" : "S:";
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
		boolean record = ViewportDifferential.records ("Tree");
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

	/** The same scripts plus check boxes and columns (width, order, showColumn, sort indicator), against stock SWT. */
	@Test
	public void seededScriptsWithCheckAndColumnsAnswerLikeStockSwt () throws java.io.IOException {
		boolean record = ViewportDifferential.records ("Tree");
		Tree tree = virtualTree (true);
		if (!record) {
			Assumptions.assumeTrue (tree.getVerticalBar ().getMaximum () == ROOTS - 1, "Tree viewport mediation is not active in this build");
		}
		Subject subject = subject (tree);
		shell.open ();
		tree.setFocus ();
		ViewportDifferential.settle (display, 120);
		harness (true).compareWithRecording (subject, record, System.getProperty ("swt.viewport.recording"));
	}
}
