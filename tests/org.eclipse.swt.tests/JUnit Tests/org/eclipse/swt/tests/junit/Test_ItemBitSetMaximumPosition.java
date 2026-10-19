// Copyright 2026 Hitesh Soliwal and Contributors to the Synexia Project.
// SPDX-License-Identifier: Apache-2.0
package org.eclipse.swt.tests.junit;

import static org.eclipse.swt.tests.junit.ItemResidencyFixture.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

import org.eclipse.swt.internal.PackedHeapGrowth;
import org.junit.jupiter.api.Test;

/** Maximum-position contracts with a small, rebased BitSet oracle; no dense int-domain allocation. */
public class Test_ItemBitSetMaximumPosition {
	private static final int MAX = Integer.MAX_VALUE;

	@Test
	public void runEndClampsBeforeAddingLastChunkBase () {
		Object set = newInstance ("ItemBitSet");
		call (set, "setRange", MAX - 4096, MAX, true);
		call (set, "set", MAX, true);
		assertEquals (MAX, i (set, "runEnd", MAX - 2, MAX));
		assertTrue (b (set, "get", MAX));
		assertTrue (i (set, "bytes") < 4096, "last-chunk RUN stays sparse");
	}

	@Test
	public void bitmapRunEndClampsBeforeAddingLastChunkBase () {
		Object set = newInstance ("ItemBitSet");
		for (int offset = 0; offset < 65536; offset += 2) call (set, "set", MAX - offset, true);
		call (set, "set", MAX - 1, true);
		assertEquals (MAX, i (set, "runEnd", MAX - 2, MAX));
		assertTrue (i (set, "bytes") < 16384, "one BITMAP container");
	}

	@Test
	public void shiftingLastChunkDoesNotLoseSetRows () {
		Object set = newInstance ("ItemBitSet");
		call (set, "setRange", MAX - 4096, MAX, true);
		call (set, "set", MAX, true);
		int [][] scratch = {new int [4]};
		call (set, "shift", MAX - 4096, -1, MAX, scratch);
		assertEquals (4096, i (set, "count", MAX - 4097, MAX));
		assertTrue (b (set, "get", MAX - 4097));
		assertTrue (b (set, "get", MAX - 2));
		assertFalse (b (set, "get", MAX - 1));
		assertTrue (b (set, "get", MAX), "exclusive-limit sentinel is not shifted");
	}

	@Test
	public void smallRebasedOracleCoversLimitsAndContainerBoundaries () {
		for (int base : new int [] {0, 65536 - 4096, MAX - 8192}) {
			Object set = newInstance ("ItemBitSet");
			BitSet ref = new BitSet ();
			call (set, "setRange", base + 100, base + 4096, true);
			ref.set (100, 4096);
			for (int offset = 0; offset < 8192; offset += 3) {
				call (set, "set", base + offset, true);
				ref.set (offset);
			}
			for (int offset = 0; offset < 8192; offset += 17) {
				for (int limit : new int [] {base + offset, base + Math.min (8192, offset + 1), base + 8192}) {
					int expected = ref.get (offset) ? Math.min (ref.nextClearBit (offset), limit - base) : offset;
					assertEquals (base + expected, i (set, "runEnd", base + offset, limit));
				}
			}
		}
	}
}
