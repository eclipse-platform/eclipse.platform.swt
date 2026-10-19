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
package org.eclipse.swt.widgets;

import org.eclipse.swt.internal.*;

/**
 * The one growth rule for the packed lanes of the lazy layer (bit lanes, page-source queues and
 * node registries). It delegates to {@link PackedHeapGrowth} (SWT #122, ported from Synexia
 * <code>PackedHeapGrowth</code>/<code>M3PreGc</code>): on a capacity miss the heap limit and the
 * used heap are sampled once; geometric growth while it fits, the exact required length under
 * pressure. A GWT build replaces PackedHeapGrowth with plain geometric growth.
 */
final class VirtualGrowth {
	private VirtualGrowth () {
	}

	/**
	 * Returns the new capacity for an array of <code>current</code> elements that must hold
	 * <code>required</code>, each element taking about <code>elementBytes</code> bytes.
	 */
	static int capacity (int current, int required, int elementBytes) {
		if (required <= current) return current;
		return PackedHeapGrowth.capacity (current, required, Math.max (1, elementBytes));
	}
}
