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
 *
 * Dual-licensed by the rights holder's decision of 2026-10-13: you may use this file under the
 * Eclipse Public License 2.0 (https://www.eclipse.org/legal/epl-2.0/) or the Apache License 2.0
 * (https://www.apache.org/licenses/LICENSE-2.0).
 *
 * Ported from hsoliwal/com.synexia 46e296ad537c6ecbd62b9783874e8f1714e278a9:
 * synexia-common/src/main/java/com/synexia/common/collections/PackedHeapGrowth.java (#10871),
 * PackedSupport.growArrayCapacity, and the once-per-miss sampling of
 * synexia-mindex/collections/src/main/java/com/synexia/mindex/collections/M3PreGc.java (#10879),
 * originally Apache-2.0 (Copyright 2026 Synexia <hsoliwal@gmail.com>), copied and specialized here.
 * Notice: docs/m3/item-residency/THIRD_PARTY_NOTICES.md.
 *******************************************************************************/
package org.eclipse.swt.internal;

/**
 * Heap-aware capacity choice for replacement primitive arrays and packed row tables (any positive row width; the Synexia original admits 1, 2, 4 or 8). Call it only on a capacity miss:
 * it samples {@code Runtime.maxMemory()} (Xmx) and used heap once per growth, never per element.
 * Under headroom pressure it returns the exact required length instead of geometric slack; it never
 * rejects a required allocation, changes logical size or forces GC. Callers keep their add/append
 * APIs; nothing is asked of the programmer.
 *
 * <p>Web (GWT/RAP) builds have no {@code Runtime.maxMemory()}; a super-source variant returns the
 * geometric capacity there.</p>
 */
public final class PackedHeapGrowth {
	/** Heap observation source; replaceable so tests can simulate low headroom. */
	public interface Sampler {
		long maxBytes ();

		long usedBytes ();
	}

	static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;
	private static final long MIB = 1024L * 1024L;
	private static final Sampler RUNTIME = new Sampler () {
		@Override
		public long maxBytes () {
			return Runtime.getRuntime ().maxMemory ();
		}

		@Override
		public long usedBytes () {
			Runtime r = Runtime.getRuntime ();
			return r.totalMemory () - r.freeMemory ();
		}
	};
	private static volatile Sampler sampler = RUNTIME;
	private static volatile int samples, exactChoices;

	private PackedHeapGrowth () {
	}

	/** Installs a sampler (null restores the runtime sampler). Test and embedder hook. */
	public static void sampler (Sampler replacement) {
		sampler = replacement == null ? RUNTIME : replacement;
	}

	/** Samples taken and exact (pressure) choices made since start; diagnostics only. */
	public static int samples () {
		return samples;
	}

	public static int exactChoices () {
		return exactChoices;
	}

	/** Capacity for a grown array of {@code elementBytes}-wide elements holding at least {@code minimum}. */
	public static int capacity (int current, int minimum, int elementBytes) {
		Sampler s = sampler;
		samples++;
		int chosen = choose (current, minimum, elementBytes, s.maxBytes (), s.usedBytes ());
		if (chosen == minimum && minimum != grow (current, minimum)) exactChoices++;
		return chosen;
	}

	/** Geometric growth: 8 minimum, then 1.5x, never below {@code minimum}. */
	public static int grow (int current, int minimum) {
		if (minimum < 0 || minimum > MAX_ARRAY_SIZE) throw new OutOfMemoryError ("required array size too large");
		long grown = current < 8 ? 8L : (long) current + (current >>> 1);
		if (grown < minimum) grown = minimum;
		return (int) Math.min (grown, MAX_ARRAY_SIZE);
	}

	/** Pure decision atom; explicit measurements make it deterministically testable. */
	public static int choose (int current, int minimum, int elementBytes, long max, long used) {
		if (elementBytes < 1) throw new IllegalArgumentException ("element width");
		int geometric = grow (current, minimum);
		if (geometric == minimum || max <= 0 || max == Long.MAX_VALUE || used < 0 || used > max) {
			return geometric;
		}
		long available = max - used;
		long reserve = Math.min (64L * MIB, Math.max (MIB, max >>> 3));
		// A replacement is live alongside its source during Arrays.copyOf.
		if (available <= reserve || (long) geometric * elementBytes + 16L > available - reserve) {
			return minimum;
		}
		return geometric;
	}
}
