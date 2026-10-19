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
package org.eclipse.swt.tests.junit;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Random;

import org.junit.jupiter.api.Test;

/**
 * Headless oracle of the flat native window of a viewport List: a simulated list box (an array
 * of logical row numbers) applies each plan (head deletes, tail deletes, head inserts, tail
 * appends) and logical inserts and removes, and must then hold exactly the planned interval, in
 * order, touching no more rows than the two intervals differ by.
 */
public class Test_VirtualRowWindowPlan {
	final Object plan = construct ();
	/* simulated list box: logical row of each native index */
	final ArrayList<Integer> rows = new ArrayList<> ();

	static Object construct () {
		try {
			Constructor<?> c = Class.forName ("org.eclipse.swt.widgets.VirtualRowWindowPlan").getDeclaredConstructor ();
			c.setAccessible (true);
			return c.newInstance ();
		} catch (ReflectiveOperationException e) {
			throw new AssertionError (e);
		}
	}

	int call (String name, Object... args) {
		Class<?> [] types = new Class<?> [args.length];
		for (int i = 0; i < args.length; i++) types [i] = int.class;
		try {
			Method m = plan.getClass ().getDeclaredMethod (name, types);
			m.setAccessible (true);
			Object result = m.invoke (plan, args);
			return result instanceof Integer i ? i : result instanceof Boolean b ? (b ? 1 : 0) : 0;
		} catch (InvocationTargetException e) {
			if (e.getCause () instanceof RuntimeException r) throw r;
			throw new AssertionError (e.getCause ());
		} catch (ReflectiveOperationException e) {
			throw new AssertionError (e);
		}
	}

	/* applies one move the way ListViewport.rebuild does and returns the native rows touched */
	int move (int first, int size) {
		call ("plan", first, size);
		int touched = call ("touched");
		if (call ("replaced") == 1) {
			rows.clear ();
		} else {
			for (int i = 0; i < call ("retiredHead"); i++) rows.remove (0);
			for (int i = 0; i < call ("retiredTail"); i++) rows.remove (rows.size () - 1);
		}
		int head = call ("insertedHead"), tail = call ("insertedTail");
		for (int i = 0; i < head; i++) rows.add (i, first + i);
		for (int i = 0; i < tail; i++) rows.add (first + size - tail + i);
		call ("commit");
		return touched;
	}

	void assertWindow (int first, int size) {
		assertEquals (first, call ("first"));
		assertEquals (size, call ("size"));
		assertEquals (size, rows.size ());
		for (int i = 0; i < size; i++) {
			assertEquals (first + i, rows.get (i), "native row " + i);
			assertEquals (i, call ("entryOf", first + i));
		}
		assertEquals (-1, call ("entryOf", first - 1));
		assertEquals (-1, call ("entryOf", first + size));
		for (int i = 0; i < size; i++) assertEquals (first + i, call ("rowOf", i));
		assertThrows (IllegalArgumentException.class, () -> call ("rowOf", -1));
		assertThrows (IllegalArgumentException.class, () -> call ("rowOf", size));
	}

	@Test
	public void plansOfImpossibleWindowsAreRejected () {
		assertThrows (IllegalArgumentException.class, () -> call ("plan", -1, 10));
		assertThrows (IllegalArgumentException.class, () -> call ("plan", 10, -1));
		assertThrows (IllegalArgumentException.class, () -> call ("plan", Integer.MAX_VALUE - 5, 10));
		move (Integer.MAX_VALUE - 10, 10);
		assertWindow (Integer.MAX_VALUE - 10, 10);
	}

	@Test
	public void movesTouchOnlyTheRowsThatEnterOrLeave () {
		assertEquals (40, move (100, 40));
		assertWindow (100, 40);
		assertEquals (6, move (103, 40));
		assertWindow (103, 40);
		assertEquals (10, move (98, 40));
		assertWindow (98, 40);
		assertEquals (5, move (98, 45));
		assertWindow (98, 45);
		assertEquals (85, move (500, 40));
		assertWindow (500, 40);
		assertEquals (40, move (0, 0));
		assertWindow (0, 0);
	}

	@Test
	public void randomMovesKeepTheWindowExact () {
		Random random = new Random (7);
		int first = 0, size = 0;
		for (int step = 0; step < 2000; step++) {
			int nextFirst = Math.max (0, first + random.nextInt (81) - 40);
			int nextSize = random.nextInt (60);
			if (random.nextInt (10) == 0) nextFirst = random.nextInt (100_000);
			int touched = move (nextFirst, nextSize);
			int overlap = Math.max (0, Math.min (first + size, nextFirst + nextSize) - Math.max (first, nextFirst));
			int bound = overlap == 0 ? size + nextSize : size + nextSize - 2 * overlap;
			assertEquals (bound, touched, "step " + step);
			first = nextFirst;
			size = nextSize;
			assertWindow (first, size);
		}
	}

	@Test
	public void logicalInsertsAndRemovesKeepTheWindowAligned () {
		move (100, 40);
		/* before the window: the window moves down, no native row changes */
		assertEquals (-1, call ("inserted", 50, 3));
		for (int i = 0; i < rows.size (); i++) rows.set (i, rows.get (i) + 3);
		assertWindow (103, 40);
		/* inside: one native insert at the returned index */
		int entry = call ("inserted", 110, 1);
		assertEquals (7, entry);
		for (int i = entry; i < rows.size (); i++) rows.set (i, rows.get (i) + 1);
		rows.add (entry, 110);
		assertWindow (103, 41);
		/* a removal across the head: rows before the window shift it, resident rows are deleted */
		int resident = call ("removed", 100, 6);
		assertEquals (3, resident);
		int at = call ("removedEntry", 100);
		assertEquals (0, at);
		for (int i = 0; i < resident; i++) rows.remove (at);
		for (int i = 0; i < rows.size (); i++) rows.set (i, rows.get (i) - 6);
		assertWindow (100, 38);
		/* after the window: nothing changes */
		assertEquals (0, call ("removed", 500, 10));
		assertEquals (-1, call ("inserted", 500, 10));
		assertWindow (100, 38);
	}
}
