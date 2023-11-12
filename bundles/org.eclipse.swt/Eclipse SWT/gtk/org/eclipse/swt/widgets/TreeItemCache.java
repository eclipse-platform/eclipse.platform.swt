/*******************************************************************************
 * Copyright (c) 2023, 2023 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Vasili Gulevich - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.widgets;

import java.util.*;

import org.eclipse.swt.internal.gtk.*;

/** Volatile information about a TreeItem, that takes long to compute
 * @since 3.125**/
class TreeItemCache {
	final TreeItem owner;
	private TreeItem[] allChildren;
	private final ArrayList<TreeItem> sparseChildren = new ArrayList<>();
	private int itemCount = -1;

	TreeItemCache(TreeItem owner) {
		this.owner = Objects.requireNonNull(owner);
	}

	int getItemCount() {
		if (itemCount < 0) {
			itemCount = GTK.gtk_tree_model_iter_n_children (owner.parent.modelHandle, owner.handle);
		}
		return itemCount;
	}

	TreeItem getItem(int index) {
		if (sparseChildren.size() > index && sparseChildren.get(index) != null) {
			return sparseChildren.get(index);
		} else {
			int newSize = Math.max(index + 1, itemCount);
			int extraSize = newSize - sparseChildren.size();
			if (extraSize > 0) {
				sparseChildren.addAll(Collections.nCopies(extraSize, null));
			}
			TreeItem result = owner.parent._getItem(owner.handle, index);
			sparseChildren.set(index, result);
			return result;
		}
	}

	TreeItem[] getItems() {
		if (allChildren == null) {
			allChildren = owner.parent.getItems(owner.handle);
			sparseChildren.clear();
			sparseChildren.addAll(Arrays.asList(allChildren));
			itemCount = allChildren.length;
		}
		return allChildren;
	}

	public void reset() {
		itemCount = -1;
		allChildren = null;
		sparseChildren.clear();
	}
}
