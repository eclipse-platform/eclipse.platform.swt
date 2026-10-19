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

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Random;

import org.eclipse.swt.internal.ViewportRuntime.Selection;
import org.junit.jupiter.api.Test;

/** Target-native tests of the real internal owner; no Display or substitute toolkit. */
public class Test_ViewportSelectionShifts {
	@Test
	public void test_insertRemoveAgainstDenseOracle () {
		for (boolean complement : new boolean [] {false, true}) {
			Random random = new Random (20261014);
			Selection selection = new Selection ();
			boolean [] expected = new boolean [128];
			selection.setLogicalCount (expected.length);
			if (complement) {
				selection.selectAll ();
				Arrays.fill (expected, true);
			}
			for (int step = 0; step < 2000; step++) {
				int index = random.nextInt (expected.length + 1);
				if (step % 3 == 0 && index < expected.length) {
					expected [index] = !expected [index];
					selection.setSelected (index, expected [index]);
				} else if (step % 3 == 1) {
					int count = random.nextInt (5);
					selection.insert (index, count);
					boolean [] next = new boolean [expected.length + count];
					System.arraycopy (expected, 0, next, 0, index);
					System.arraycopy (expected, index, next, index + count, expected.length - index);
					expected = next;
				} else {
					int count = random.nextInt (expected.length - index + 1);
					selection.remove (index, count);
					boolean [] next = new boolean [expected.length - count];
					System.arraycopy (expected, 0, next, 0, index);
					System.arraycopy (expected, index + count, next, index, next.length - index);
					expected = next;
				}
				assertEquals (expected.length, selection.logicalCount ());
				assertEquals (complement, selection.complementMode ());
				int selected = 0;
				for (int i = 0; i < expected.length; i++) {
					assertEquals (expected [i], selection.isSelected (i));
					if (expected [i]) selected++;
				}
				assertEquals (selected, selection.selectedCount ());
			}
		}
	}

	@Test
	public void test_deletedGapMergesAndInsertSplits () {
		Selection selection = new Selection ();
		selection.setLogicalCount (10);
		selection.selectRange (1, 3);
		selection.selectRange (5, 7);
		selection.remove (3, 2);
		assertArrayEquals (new int [] {1, 2, 3, 4}, selection.toArray ());
		assertEquals (1, selection.rangeCount ());
		selection.insert (3, 2);
		assertArrayEquals (new int [] {1, 2, 5, 6}, selection.toArray ());
		assertEquals (2, selection.rangeCount ());
	}

	@Test
	public void test_overflowLeavesStateAndStorageIsReleased () throws Exception {
		Selection selection = new Selection ();
		selection.setLogicalCount (Integer.MAX_VALUE);
		selection.selectAll ();
		assertThrows (ArithmeticException.class, () -> selection.insert (0, 1));
		assertEquals (Integer.MAX_VALUE, selection.selectedCount ());
		selection.clear ();
		selection.setLogicalCount (4096);
		for (int i = 0; i < 4096; i += 2) selection.setSelected (i, true);
		selection.remove (1, 4094);
		Field starts = Selection.class.getDeclaredField ("starts");
		starts.setAccessible (true);
		assertEquals (4, ((int [])starts.get (selection)).length);
		assertArrayEquals (new int [] {0}, selection.toArray ());
	}
}
