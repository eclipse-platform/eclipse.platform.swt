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
 * The one growth rule for the packed lanes of the lazy layer (bit lanes, page-source queues and
 * node registries): on a capacity miss, sample the heap limit and the used heap once; double when
 * the doubled array fits comfortably, otherwise grow to the exact required length.
 *
 * <p>Interim owner until the Xmx-aware growth ported from Synexia <code>PackedHeapGrowth</code> /
 * <code>M3PreGc</code> lands in <code>org.eclipse.swt.internal</code> (SWT #122); callers then
 * switch by changing this one method. A GWT build replaces it with plain doubling (no
 * <code>Runtime</code> there).</p>
 */
final class VirtualGrowth {
	/** Doubling is allowed while used heap plus the new array stays under this share of the limit. */
	private static final double PRESSURE_SHARE = 0.80;

	private VirtualGrowth () {
	}

	/**
	 * Returns the new capacity for an array of <code>current</code> elements that must hold
	 * <code>required</code>, each element taking about <code>elementBytes</code> bytes.
	 */
	static int capacity (int current, int required, int elementBytes) {
		if (required <= current) return current;
		int doubled = current > (Integer.MAX_VALUE >>> 1) ? Integer.MAX_VALUE : Math.max (4, current << 1);
		if (doubled <= required) return required;
		Runtime runtime = Runtime.getRuntime ();
		long limit = runtime.maxMemory ();
		long used = runtime.totalMemory () - runtime.freeMemory ();
		long doubledBytes = (long) doubled * Math.max (1, elementBytes);
		return used + doubledBytes <= (long) (limit * PRESSURE_SHARE) ? doubled : required;
	}
}
