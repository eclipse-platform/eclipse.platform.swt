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
package org.eclipse.swt.widgets;

import java.util.*;

import org.eclipse.swt.*;

/**
 * Shared bulk tree projection operations used by the platform Tree
 * implementations.
 *
 * <p>The operation is deliberately owned by SWT rather than by JFace.  JFace
 * should only map model elements/paths to TreeItems and delegate.  SWT owns the
 * redraw transaction, expansion state, selection roots and platform projection.</p>
 */
final class TreeExpansionModel {

	static final int ALL_LEVELS = -1;

	private record Pending(TreeItem item, int level) {
	}

	private TreeExpansionModel() {
	}

	static void expandAll(Tree tree) {
		expandToLevel(tree, ALL_LEVELS);
	}

	static void collapseAll(Tree tree) {
		collapseToLevel(tree, ALL_LEVELS);
	}

	static void expandToLevel(Tree tree, int level) {
		checkLevel(level);
		runLocked(tree, () -> {
            if (level == 0 || level == 1) {
                return;
            }
			int childLevel = level == ALL_LEVELS ? ALL_LEVELS : level - 1;
			TreeItem[] roots = tree.modelChildren(null, true);
			apply(tree, roots, childLevel, true);
		});
	}

	static void collapseToLevel(Tree tree, int level) {
		checkLevel(level);
		runLocked(tree, () -> {
            if (level == 0 || level == 1) {
                return;
            }
			int childLevel = level == ALL_LEVELS ? ALL_LEVELS : level - 1;
			TreeItem[] roots = tree.modelChildren(null, false);
			apply(tree, roots, childLevel, false);
		});
	}

	static void expandToLevel(Tree tree, TreeItem item, int level) {
		checkItem(tree, item);
		checkLevel(level);
		runLocked(tree, () -> apply(tree, new TreeItem[] {item}, level, true));
	}

	static void collapseToLevel(Tree tree, TreeItem item, int level) {
		checkItem(tree, item);
		checkLevel(level);
		runLocked(tree, () -> apply(tree, new TreeItem[] {item}, level, false));
	}

	static void expandSelection(Tree tree, int level) {
		checkLevel(level);
		runLocked(tree, () -> apply(tree, tree.getSelection(), level, true));
	}

	static void collapseSelection(Tree tree, int level) {
		checkLevel(level);
		runLocked(tree, () -> apply(tree, tree.getSelection(), level, false));
	}

	static TreeItem[] expandedItems(Tree tree) {
		ArrayList<TreeItem> result = new ArrayList<>();
		collectExpanded(tree, null, result);
		TreeItem[] items = result.toArray(TreeItem[]::new);
		for (TreeItem item : items) item.pinVirtualFacade();
		return items;
	}

	static void setExpandedItems(Tree tree, TreeItem[] items) {
		if (items == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
		IdentityHashMap<TreeItem, Boolean> wanted = new IdentityHashMap<>();
		for (TreeItem item : items) {
			if (item == null) continue;
			if (item.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
			if (item.getParent() != tree) continue;
			item.pinVirtualFacade();
			wanted.put(item, Boolean.TRUE);
		}
		runLocked(tree, () -> replaceExpanded(tree, null, wanted));
	}

	private static void collectExpanded(Tree tree, TreeItem parent, java.util.List<TreeItem> result) {
		for (TreeItem item : tree.modelChildren(parent, false)) {
			if (item == null || item.isDisposed()) continue;
			if (item.getExpanded()) result.add(item);
			collectExpanded(tree, item, result);
		}
	}

	private static void replaceExpanded(
			Tree tree, TreeItem parent, IdentityHashMap<TreeItem, Boolean> wanted) {
		for (TreeItem item : tree.modelChildren(parent, false)) {
			if (item == null || item.isDisposed()) continue;
			boolean expand = wanted.remove(item) != null;
			if (item.getExpanded() != expand) item.setExpanded(expand);
			replaceExpanded(tree, item, wanted);
		}
		if (parent == null && !wanted.isEmpty()) {
			for (TreeItem item : wanted.keySet()) {
				if (!item.isDisposed()) item.setExpanded(true);
			}
			wanted.clear();
		}
	}

	private static void apply(Tree tree, TreeItem[] roots, int level, boolean expand) {
        if (level == 0 || roots.length == 0) {
            return;
        }
		ArrayDeque<Pending> pending = new ArrayDeque<>();
		for (int index = roots.length - 1; index >= 0; index--) {
			TreeItem item = roots[index];
            if (item != null && !item.isDisposed()) {
                pending.push(new Pending(item, level));
            }
		}
		while (!pending.isEmpty()) {
			Pending current = pending.pop();
			TreeItem item = current.item();
            if (item.isDisposed()) {
                continue;
            }
			int remaining = current.level();
            if (remaining == 0) {
                continue;
            }

			if (expand) {
				/*
				 * getItemCount() is the SWT model boundary for SWT.VIRTUAL.  It may
				 * invoke SetData for this facade, but children are only manufactured
				 * when the requested level requires descending into them.
				 */
				int childCount = item.getItemCount();
                if (childCount == 0) {
                    continue;
                }
				item.setExpanded(true);
                if (remaining == 1) {
                    continue;
                }
				int next = remaining == ALL_LEVELS ? ALL_LEVELS : remaining - 1;
				TreeItem[] children = tree.modelChildren(item, true);
				for (int index = children.length - 1; index >= 0; index--) {
					TreeItem child = children[index];
                    if (child != null && !child.isDisposed()) {
                        pending.push(new Pending(child, next));
                    }
				}
			} else {
				/*
				 * Collapse never needs to manufacture unseen Cocoa viewport facades.
				 * Other platform implementations currently return their existing native
				 * projection here and can be made sparse independently.
				 */
				item.setExpanded(false);
                if (remaining == 1) {
                    continue;
                }
				int next = remaining == ALL_LEVELS ? ALL_LEVELS : remaining - 1;
				TreeItem[] children = tree.modelChildren(item, false);
				for (int index = children.length - 1; index >= 0; index--) {
					TreeItem child = children[index];
                    if (child != null && !child.isDisposed()) {
                        pending.push(new Pending(child, next));
                    }
				}
			}
		}
	}

	private static void runLocked(Tree tree, Runnable operation) {
		tree.setRedraw(false);
		try {
			operation.run();
		} finally {
            if (!tree.isDisposed()) {
                tree.setRedraw(true);
            }
		}
	}

	private static void checkLevel(int level) {
        if (level < ALL_LEVELS) {
            SWT.error(SWT.ERROR_INVALID_ARGUMENT);
        }
	}

	private static void checkItem(Tree tree, TreeItem item) {
        if (item == null) {
            SWT.error(SWT.ERROR_NULL_ARGUMENT);
        }
        if (item.isDisposed()) {
            SWT.error(SWT.ERROR_INVALID_ARGUMENT);
        }
        if (item.getParent() != tree) {
            SWT.error(SWT.ERROR_INVALID_ARGUMENT);
        }
	}
}
