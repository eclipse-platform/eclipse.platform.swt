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

import org.eclipse.swt.widgets.*;
import org.junit.jupiter.api.*;

/**
 * Contract of the M3ItemRange value (enhanced M3 API, leaf 1). Headless.
 */
public class Test_M3ItemRange {

	@Test
	public void test_valueAndEmptiness () {
		M3ItemRange range = new M3ItemRange (3, 7);
		assertEquals (3, range.start ());
		assertEquals (7, range.end ());
		assertEquals (5, range.size ());
		assertFalse (range.isEmpty ());
		M3ItemRange single = new M3ItemRange (4, 4);
		assertEquals (1, single.size ());
		M3ItemRange empty = new M3ItemRange (5, 2);
		assertTrue (empty.isEmpty ());
		assertEquals (0, empty.size ());
		assertTrue (M3ItemRange.EMPTY.isEmpty ());
		assertEquals (0, M3ItemRange.EMPTY.start ());
		assertEquals (-1, M3ItemRange.EMPTY.end ());
	}

	@Test
	public void test_ofNormalizesEmpty () {
		assertSame (M3ItemRange.EMPTY, M3ItemRange.of (3, 2));
		assertEquals (new M3ItemRange (2, 9), M3ItemRange.of (2, 9));
		assertThrows (IllegalArgumentException.class, () -> M3ItemRange.of (-1, 2));
		assertThrows (IllegalArgumentException.class, () -> new M3ItemRange (-1, 2));
	}

	@Test
	public void test_containsAndIntersection () {
		M3ItemRange range = new M3ItemRange (10, 20);
		assertTrue (range.contains (10));
		assertTrue (range.contains (20));
		assertFalse (range.contains (9));
		assertFalse (range.contains (21));
		assertFalse (M3ItemRange.EMPTY.contains (0));
		assertEquals (new M3ItemRange (15, 20), range.intersection (new M3ItemRange (15, 30)));
		assertEquals (new M3ItemRange (10, 12), range.intersection (new M3ItemRange (0, 12)));
		assertTrue (range.intersection (new M3ItemRange (21, 25)).isEmpty ());
		assertTrue (range.intersection (M3ItemRange.EMPTY).isEmpty ());
		assertThrows (IllegalArgumentException.class, () -> range.intersection (null));
	}

	@Test
	public void test_coalesceBuildsNormalForm () {
		M3ItemRange [] ranges = M3ItemRange.coalesce (new int [] {5, 3, 4, 9, 1});
		assertArrayEquals (new M3ItemRange [] {new M3ItemRange (1, 1), new M3ItemRange (3, 5), new M3ItemRange (9, 9)}, ranges);
		assertEquals (5, M3ItemRange.countOf (ranges));
		assertArrayEquals (new M3ItemRange [] {new M3ItemRange (2, 3)}, M3ItemRange.coalesce (new int [] {2, 2, 3}));
		assertArrayEquals (new M3ItemRange [] {new M3ItemRange (0, 4)}, M3ItemRange.coalesce (new int [] {0, 1, 2, 3, 4}));
		assertArrayEquals (new M3ItemRange [] {new M3ItemRange (7, 7)}, M3ItemRange.coalesce (new int [] {7}));
		assertEquals (0, M3ItemRange.coalesce (new int [0]).length);
		assertEquals (0, M3ItemRange.countOf (new M3ItemRange [0]));
		assertThrows (IllegalArgumentException.class, () -> M3ItemRange.coalesce (null));
		assertThrows (IllegalArgumentException.class, () -> M3ItemRange.coalesce (new int [] {2, -1}));
		assertThrows (IllegalArgumentException.class, () -> M3ItemRange.countOf (null));
		assertThrows (IllegalArgumentException.class, () -> M3ItemRange.countOf (new M3ItemRange [] {null}));
	}

	@Test
	public void test_coalesceLeavesTheArgumentUntouched () {
		int [] indices = {9, 1, 1, 4};
		M3ItemRange.coalesce (indices);
		assertArrayEquals (new int [] {9, 1, 1, 4}, indices);
	}

	@Test
	public void test_equalsHashCodeToString () {
		M3ItemRange same = new M3ItemRange (1, 2);
		assertTrue (same.equals (same));
		assertFalse (M3ItemRange.EMPTY.equals (same));
		assertFalse (same.equals (M3ItemRange.EMPTY));
		assertEquals (new M3ItemRange (1, 2), new M3ItemRange (1, 2));
		assertEquals (new M3ItemRange (1, 2).hashCode (), new M3ItemRange (1, 2).hashCode ());
		assertNotEquals (new M3ItemRange (1, 2), new M3ItemRange (1, 3));
		assertNotEquals (new M3ItemRange (1, 2), new M3ItemRange (0, 2));
		assertEquals (new M3ItemRange (8, 2), M3ItemRange.EMPTY);
		assertEquals (new M3ItemRange (8, 2).hashCode (), M3ItemRange.EMPTY.hashCode ());
		assertNotEquals (new M3ItemRange (1, 2), "M3ItemRange [1..2]");
		assertNotEquals (new M3ItemRange (1, 2), null);
		assertEquals ("M3ItemRange [1..2]", new M3ItemRange (1, 2).toString ());
		assertEquals ("M3ItemRange []", M3ItemRange.EMPTY.toString ());
	}
}
