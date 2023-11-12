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
	private TreeItem[] children;
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

	TreeItem[] getItems() {
		if (children == null) {
			children = owner.parent.getItems(owner.handle);
			itemCount = children.length;
		}
		return children;
	}

	public void reset() {
		itemCount = -1;
		children = null;
	}
}
