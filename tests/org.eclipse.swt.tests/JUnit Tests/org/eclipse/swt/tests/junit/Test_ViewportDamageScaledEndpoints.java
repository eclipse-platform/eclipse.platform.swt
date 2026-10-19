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
package org.eclipse.swt.tests.junit;

import static org.junit.jupiter.api.Assertions.*;

import org.eclipse.swt.internal.ViewportDamageRegion;
import org.junit.jupiter.api.Test;

/** Exact-integer endpoint oracle for bounded zoomed damage; no native resources. */
public class Test_ViewportDamageScaledEndpoints {
	@Test
	public void endpointAdditionWidensBeforeZoomConversion () {
		int [][] cases = {
			{0, 0, 100, 100, 100},
			{0, 0, Integer.MAX_VALUE, 100, 1},
			{-(1 << 28), 0, Integer.MAX_VALUE, 100, 1},
			{1 << 28, 0, Integer.MAX_VALUE, 100, 1},
			{0, 1 << 28, 100, Integer.MAX_VALUE, 1},
			{Integer.MAX_VALUE - 5, 0, 10, 100, 1},
			{0, Integer.MAX_VALUE - 5, 100, 10, 1},
			{Integer.MIN_VALUE, 0, Integer.MAX_VALUE, 100, 1},
			{1 << 28, 1 << 28, Integer.MAX_VALUE, Integer.MAX_VALUE, 1}
		};
		for (int [] input : cases) {
			int x = input [0], y = input [1], width = input [2], height = input [3], zoom = input [4];
			long left = Math.floorDiv ((long) x * zoom, 100);
			long top = Math.floorDiv ((long) y * zoom, 100);
			long right = -Math.floorDiv (-((long) x + width) * zoom, 100);
			long bottom = -Math.floorDiv (-((long) y + height) * zoom, 100);
			ViewportDamageRegion region = new ViewportDamageRegion ();
			region.addPoints (x, y, width, height, zoom);
			assertEquals ((right - left) * (bottom - top), region.area (), "positive scaled damage must survive endpoint addition");
			int [] bounds = new int [4];
			region.boundingBox (bounds);
			assertArrayEquals (new int [] {Math.toIntExact (left), Math.toIntExact (top), Math.toIntExact (right - left), Math.toIntExact (bottom - top)}, bounds);
		}
		ViewportDamageRegion rejected = new ViewportDamageRegion ();
		assertThrows (IllegalArgumentException.class, () -> rejected.addPoints (0, 0, Integer.MAX_VALUE, 100, 100));
		assertThrows (IllegalArgumentException.class, () -> rejected.addPoints (1, 1, 1, 1, 0));
		assertThrows (IllegalArgumentException.class, () -> rejected.addPoints (1, 1, 1, 1, 1601));
		assertTrue (rejected.isEmpty ());
		rejected.addPoints (Integer.MAX_VALUE, Integer.MAX_VALUE, 0, 1, 0);
		assertTrue (rejected.isEmpty ());
	}
}
