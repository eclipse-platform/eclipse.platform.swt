/*******************************************************************************
 * Copyright (c) 2026 Andrey Loskutov <loskutov@gmx.de> and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Andrey Loskutov <loskutov@gmx.de> - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tests.gtk.snippets;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

/**
 * Manual reproducer for the Tree.removeAll() crash on Linux GTK with
 * SWT.VIRTUAL trees.
 *
 * STEPS TO REPRODUCE: 1. Run this snippet. 2. Expand any root item so its
 * children become visible (this creates Java TreeItem objects with GTK iters
 * backed by live GNodes). 3. Click GO.
 *
 * EXPECTED: Tree content refreshes cleanly. ACTUAL: SIGSEGV inside
 * gtk_tree_model_get_path (libgtk-3.so.0).
 *
 * ROOT CAUSE (mirrors the original JFace crash test provided in the ticket):
 *
 * Tree.removeAll() calls gtk_tree_store_clear(). GTK fires cellDataProc
 * synchronously for rows that become visible as the cursor moves during
 * deletion. For an UNCACHED root item (never rendered, so no ID column value in
 * the model), Tree._getItem() calls getId() which calls gtk_tree_store_set() to
 * assign the ID. That gtk_tree_store_set fires a synchronous "row-changed"
 * signal which re-enters cellDataProc a second time for the same row. The inner
 * cellDataProc finds the item still uncached and calls checkData() ->
 * sendEvent(SWT.SetData).
 *
 * At this point gtk_tree_store_clear has already removed (and freed the GNodes
 * of) the earlier root items and their children. The Java TreeItem objects for
 * those freed rows are still alive (release() is called after
 * gtk_tree_store_clear returns), but their handle field contains a GtkTreeIter
 * whose user_data pointer is now dangling.
 *
 * JFace's SetData handler calls viewer.replace() which calls
 * internalFindItems() -> getTreePathFromItem() -> getParentItem() on every
 * existing TreeItem widget. Calling getParentItem() on any item whose GTK row
 * has been freed passes the stale GtkTreeIter to gtk_tree_model_get_path(),
 * which dereferences the freed GNode -> SIGSEGV at si_addr=0x17 (null-like
 * offset into freed memory).
 *
 * This reproducer replicates that by explicitly calling getParentItem() on the
 * previously-collected child items (same as getTreePathFromItem does) inside
 * the SetData listener that fires during gtk_tree_store_clear.
 */
public class Issue3329_TreeCrashBug {

	private static final int ROOT_COUNT  = 40;
	private static final int CHILD_COUNT = 60;

	public static void main(String[] args) {
		var display = new Display();
		var shell   = new Shell(display);
		shell.setLayout(new GridLayout());

		var button = new Button(shell, SWT.PUSH);
		button.setText("GO"); //$NON-NLS-1$

		var tree = new Tree(shell, SWT.VIRTUAL | SWT.BORDER);
		tree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		// Collect every child TreeItem as it is rendered during v1 population.
		// These objects hold GTK iters (GtkTreeIter.user_data -> GNode).
		// After removeAll() frees those GNodes the handles become dangling.
		List<TreeItem> prevChildren = new ArrayList<>();

		tree.addListener(SWT.SetData, e -> {
			TreeItem item   = (TreeItem) e.item;
			TreeItem parent = item.getParentItem();
			if (parent == null) {
				item.setText("v1_" + e.index); //$NON-NLS-1$
				item.setItemCount(CHILD_COUNT);
			} else {
				item.setText(parent.getText() + "_" + e.index); //$NON-NLS-1$
				prevChildren.add(item); // remember this child — its GNode will be freed on removeAll()
			}
		});

		tree.setItemCount(ROOT_COUNT);

		// GO button: replace the tree content with a new version.
		// Crashes if at least one root was expanded (prevChildren is non-empty)
		// AND there are uncached roots below the visible viewport.
		button.addListener(SWT.Selection, e -> {
			for (var l : tree.getListeners(SWT.SetData)) {
				tree.removeListener(SWT.SetData, l);
			}

			// New SetData listener registered BEFORE removeAll().
			// It fires re-entrantly from inside gtk_tree_store_clear (via the
			// cellDataProc -> getId -> gtk_tree_store_set -> row-changed -> cellDataProc
			// -> checkData -> SetData chain described in the root-cause comment above).
			//
			// At the time it fires, the earlier roots and their children have
			// already been removed; prevChildren[i].handle contains a freed GNode.
			// Calling getParentItem() on those items passes the stale GtkTreeIter
			// to gtk_tree_model_get_path() -> SIGSEGV.
			//
			// This mirrors AbstractTreeViewer.getTreePathFromItem() which walks all
			// existing TreeItem widgets via getParentItem() inside internalFindItems().
			tree.addListener(SWT.SetData, ev -> {
				for (TreeItem child : prevChildren) {
					// child is NOT disposed yet (release() happens after gtk_tree_store_clear),
					// but child.handle is a stale GtkTreeIter with a freed GNode pointer.
					if (!child.isDisposed()) {
						child.getParentItem(); // <-- SIGSEGV on production GTK3
												// G_DISABLE_CHECKS)
					}
				}
				TreeItem item   = (TreeItem) ev.item;
				TreeItem parent = item.getParentItem();
				if (parent == null) {
					item.setText("v2_" + ev.index); //$NON-NLS-1$
					item.setItemCount(CHILD_COUNT);
				} else {
					item.setText(parent.getText() + "_" + ev.index); //$NON-NLS-1$
				}
			});

			tree.removeAll(); // triggers the crash
			tree.setItemCount(ROOT_COUNT);
		});

		shell.setSize(300, 250); // small window -> more uncached (out-of-viewport) roots
		shell.open();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();
	}
}