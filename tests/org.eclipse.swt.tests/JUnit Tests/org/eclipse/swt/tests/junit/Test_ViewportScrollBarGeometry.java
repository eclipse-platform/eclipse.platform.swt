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

import static org.eclipse.swt.tests.junit.Test_org_eclipse_swt_widgets_ScrollBar_GeometryDifferential.call;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/** Arithmetic of the custom scroll bar geometry (package-private <code>ViewportScrollBarGeometry</code>); no display needed. */
public class Test_ViewportScrollBarGeometry {
	static final int MIN = 8;
	static final int NONE = 0, ARROW_DECREASE = 1, PAGE_DECREASE = 2, THUMB = 3, PAGE_INCREASE = 4, ARROW_INCREASE = 5;

	/* native samples of the official SWT 3.135.0 on win32 at 100 % (ScrollBarGeometryOracle, seed 1):
	 * track length, minimum, maximum, thumb, selection, thumb start relative to the track, thumb length */
	static final int [][] NATIVE = {
		{214, 0, 64255, 2100, 21214, 70, 8},
		{167, 23, 87, 61, 25, 5, 159},
		{331, 0, 20333, 7212, 9827, 160, 117},
		{508, 0, 51303, 37547, 2920, 29, 372},
		{355, 0, 70821, 69499, 35, 0, 348},
		{129, 27, 48, 13, 28, 6, 82},
		{53, 0, 55487, 14334, 21820, 21, 14},
		{457, 28, 45, 12, 32, 102, 330},
		{116, 0, 39798, 36309, 162, 0, 106},
		{177, 0, 24696, 9104, 12369, 89, 65},
		{132, 17, 69, 6, 23, 15, 17},
		{446, 0, 7009, 2897, 3197, 204, 184},
		{244, 0, 29, 8, 16, 130, 73},
		{264, 0, 20, 20, 0, -1, 264},
	};

	@Test
	public void matchesNativeSamples () {
		for (int [] s : NATIVE) {
			assertEquals (s [6], call ("thumbLength", s [0], MIN, s [1], s [2], s [3]), "length " + java.util.Arrays.toString (s));
			assertEquals (s [5], call ("thumbStart", s [0], MIN, s [1], s [2], s [3], s [4]), "start " + java.util.Arrays.toString (s));
		}
	}

	@Test
	public void mulDivRoundsHalfAwayFromZero () {
		assertEquals (3, call ("mulDiv", 5, 1, 2));
		assertEquals (-3, call ("mulDiv", -5, 1, 2));
		assertEquals (2, call ("mulDiv", 7, 1, 3));
		assertEquals (0, call ("mulDiv", 7, 1, 0));
		assertEquals (Integer.MAX_VALUE, call ("mulDiv", Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE), "no int overflow");
	}

	@Test
	public void noThumbInATrackShorterThanTheMinimum () {
		assertEquals (0, call ("thumbLength", MIN - 1, MIN, 0, 100, 10));
		assertEquals (MIN, call ("thumbLength", MIN, MIN, 0, 100000, 1));
		assertEquals (NONE, call ("hit", 20, 17, MIN - 1, MIN, 0, 100, 10, 0));
	}

	@Test
	public void draggingInvertsTheThumbStart () {
		/* a track with at least one pixel per selection: every selection has its own start */
		for (int selection = 0; selection <= 90; selection++) {
			int start = call ("thumbStart", 400, MIN, 0, 100, 10, selection);
			assertEquals (selection, call ("selectionAt", start, 400, MIN, 0, 100, 10), "selection " + selection);
		}
		/* a coarse track: the selection never decreases while the thumb moves forward */
		int previous = -1;
		for (int start = -5; start <= 200; start++) {
			int selection = call ("selectionAt", start, 150, MIN, 0, 100000, 5000);
			assertTrue (selection >= previous && selection >= 0 && selection <= 95000, "start " + start);
			previous = selection;
		}
		assertEquals (7, call ("selectionAt", 30, 300, MIN, 7, 20, 20), "a bar that cannot scroll answers its minimum");
	}

	@Test
	public void edgesOfTheArithmetic () {
		assertEquals (-3, call ("mulDiv", 5, 1, -2), "a negative divisor");
		assertEquals (3, call ("mulDiv", -5, 1, -2));
		/* a thumb that rounds to the whole track while the bar can still scroll: no free track, the minimum */
		assertEquals (10, call ("thumbLength", 10, MIN, 0, 100, 99));
		assertEquals (0, call ("selectionAt", 3, 10, MIN, 0, 100, 99));
		assertEquals (NONE, call ("hit", -1, 17, 200, MIN, 0, 100, 20, 40), "before the bar");
	}

	@Test
	public void hitTestFindsEveryPart () {
		int arrow = 17, track = 200;
		int start = call ("thumbStart", track, MIN, 0, 100, 20, 40), length = call ("thumbLength", track, MIN, 0, 100, 20);
		assertEquals (ARROW_DECREASE, call ("hit", 0, arrow, track, MIN, 0, 100, 20, 40));
		assertEquals (PAGE_DECREASE, call ("hit", arrow + start - 1, arrow, track, MIN, 0, 100, 20, 40));
		assertEquals (THUMB, call ("hit", arrow + start, arrow, track, MIN, 0, 100, 20, 40));
		assertEquals (THUMB, call ("hit", arrow + start + length - 1, arrow, track, MIN, 0, 100, 20, 40));
		assertEquals (PAGE_INCREASE, call ("hit", arrow + start + length, arrow, track, MIN, 0, 100, 20, 40));
		assertEquals (ARROW_INCREASE, call ("hit", arrow + track, arrow, track, MIN, 0, 100, 20, 40));
		assertEquals (NONE, call ("hit", 2 * arrow + track, arrow, track, MIN, 0, 100, 20, 40));
	}
}
