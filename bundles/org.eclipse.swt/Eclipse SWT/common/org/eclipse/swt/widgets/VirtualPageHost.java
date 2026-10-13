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

/**
 * Package-private hooks of a platform <code>Table</code> or <code>Tree</code> that let a
 * {@link VirtualPageSource} keep item facades transient. A platform class registers a factory
 * for them in its class initializer (<code>VirtualPageSource.tableHost</code> /
 * <code>treeHost</code>), so no public method is added to the widget. A platform that registers
 * nothing still works through public API only (the source then uses <code>clear</code> and keeps
 * every facade).
 */
interface VirtualPageHost {
	/** The source now feeds this widget (or <code>null</code> when it detaches). */
	void pageSourceAttached (VirtualPageSource source);

	/**
	 * Re-asks rows <code>[start, end]</code> under <code>parent</code> (<code>null</code> for
	 * top-level rows) without clearing any parent-owned state such as check bits.
	 */
	void pageSourceRefresh (Item parent, int start, int end);

	/**
	 * Releases transient facades of top-level rows outside <code>[keepStart, keepEnd)</code>.
	 * Only facades whose whole state is reproducible from the parent and the source are
	 * released. Returns the number released.
	 */
	int pageSourceRelease (int keepStart, int keepEnd);

	/** Sets or clears the loading-children mask bit of <code>parent</code>. */
	void pageSourceChildrenLoading (Item parent, boolean loading);

	/**
	 * Rows above which a bulk change (a child count arriving for an expanded node) is batched:
	 * drawing is suspended and, at the end, only the accumulated damage is invalidated.
	 */
	default int pageSourceBatchThreshold () {
		return Integer.MAX_VALUE;
	}

	/** Starts (<code>parent</code> null) or ends a bulk batch below <code>parent</code>. */
	default void pageSourceBatch (boolean begin, Item parent) {
	}
}
