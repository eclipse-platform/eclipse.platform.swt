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

import java.lang.reflect.*;
import java.util.*;
import java.util.function.*;

import org.junit.jupiter.api.Test;

/**
 * Headless differential test of the package-private incremental widest-item index
 * (<code>VirtualListWidth</code>, reached by reflection like the other viewport tests) against an
 * oracle that keeps every width in a plain list: seeded scripts over every operation, dense and
 * lazy, and the error contract.
 */
public class Test_VirtualListWidth {
	private static final int UNMEASURED = -2;
	/** The measure function: the item's length; items are digit strings, so the width is deterministic. */
	private static final ToIntFunction<String> MEASURE = string -> string.length ();

	private final Object index = construct ();

	private static Object construct () {
		try {
			Constructor<?> constructor = Class.forName ("org.eclipse.swt.widgets.VirtualListWidth").getDeclaredConstructor (ToIntFunction.class);
			constructor.setAccessible (true);
			return constructor.newInstance (MEASURE);
		} catch (ReflectiveOperationException failure) {
			throw new AssertionError (failure);
		}
	}

	private Object call (String name, Class<?> [] types, Object... args) {
		try {
			Method method = index.getClass ().getDeclaredMethod (name, types);
			method.setAccessible (true);
			return method.invoke (index, args);
		} catch (InvocationTargetException failure) {
			if (failure.getCause () instanceof RuntimeException runtime) throw runtime;
			throw new AssertionError (failure.getCause ());
		} catch (ReflectiveOperationException failure) {
			throw new AssertionError (failure);
		}
	}

	private int intCall (String name, Class<?> [] types, Object... args) {
		return (Integer) call (name, types, args);
	}

	private static final Class<?> [] INT = {int.class};
	private static final Class<?> [] INT_INT = {int.class, int.class};
	private static final Class<?> [] INT_STRING = {int.class, String.class};
	private static final Class<?> [] NONE = {};

	/** Oracle: the width of every slot in order; null is an unmeasured slot. */
	private final List<Integer> oracle = new ArrayList<> ();

	private void verify () {
		assertEquals (oracle.size (), intCall ("count", NONE), "count");
		int pending = 0, max = 0;
		for (int i = 0; i < oracle.size (); i++) {
			Integer width = oracle.get (i);
			assertEquals (width == null ? UNMEASURED : width, intCall ("widthOf", INT, i), "widthOf " + i);
			if (width == null) pending++;
			else max = Math.max (max, width);
		}
		assertEquals (pending, intCall ("pendingCount", NONE), "pending");
		assertEquals (max, intCall ("maxMeasured", NONE), "maxMeasured");
		if (pending == 0) assertEquals (max, intCall ("maxWidth", NONE), "maxWidth");
		else assertThrows (IllegalStateException.class, () -> intCall ("maxWidth", NONE));
		assertFalse ((Boolean) call ("isStale", NONE), "a queried index is rebuilt");
	}

	private String item (Random random) {
		return "1234567890".substring (0, 1 + random.nextInt (9));
	}

	/**
	 * Seeded scripts over every operation: eager inserts, lazy slots inserted in bulk and measured one
	 * by one or all at once, edits, removals and whole-lane replacements, each answered like the oracle.
	 */
	@Test
	public void seededScriptsAnswerLikeAPlainList () {
		for (int seed = 0; seed < 8; seed++) {
			Random random = new Random (seed);
			call ("clear", NONE);
			oracle.clear ();
			for (int step = 0; step < 400; step++) {
				int count = oracle.size ();
				switch (random.nextInt (count == 0 ? 3 : 12)) {
					case 0 -> { /* insert one measured item */
						int at = random.nextInt (count + 1);
						String string = item (random);
						call ("insert", INT_STRING, at, string);
						oracle.add (at, string.length ());
					}
					case 1 -> { /* insert unmeasured slots */
						int at = random.nextInt (count + 1), inserted = random.nextInt (4);
						call ("insertUnmeasured", INT_INT, at, inserted);
						for (int i = 0; i < inserted; i++) oracle.add (at, null);
					}
					case 2 -> { /* replace the lane with unmeasured slots */
						int next = random.nextInt (6);
						call ("setAllUnmeasured", INT, next);
						oracle.clear ();
						for (int i = 0; i < next; i++) oracle.add (null);
					}
					case 3 -> { /* set (measures) */
						int at = random.nextInt (count);
						String string = item (random);
						call ("set", INT_STRING, at, string);
						oracle.set (at, string.length ());
					}
					case 4 -> { /* forget one slot */
						int at = random.nextInt (count);
						call ("setUnmeasured", INT, at);
						oracle.set (at, null);
					}
					case 5 -> { /* measure one slot if pending */
						int at = random.nextInt (count);
						String string = item (random);
						int width = intCall ("measure", INT_STRING, at, string);
						if (oracle.get (at) == null) oracle.set (at, string.length ());
						assertEquals ((int) oracle.get (at), width, "measure answers the slot's width");
					}
					case 6 -> { /* measure every pending slot */
						call ("measurePending", new Class<?> [] {IntFunction.class}, (IntFunction<String>) row -> "123");
						for (int i = 0; i < count; i++) {
							if (oracle.get (i) == null) oracle.set (i, 3);
						}
					}
					case 7, 8 -> { /* remove a range */
						int start = random.nextInt (count), removed = random.nextInt (count - start + 1);
						call ("remove", INT_INT, start, removed);
						oracle.subList (start, start + removed).clear ();
					}
					case 9 -> { /* replace the lane with measured items */
						String [] items = new String [random.nextInt (5)];
						oracle.clear ();
						for (int i = 0; i < items.length; i++) {
							items [i] = item (random);
							oracle.add (items [i].length ());
						}
						call ("setAll", new Class<?> [] {String [].class}, (Object) items);
					}
					default -> { /* read one slot */
						int at = random.nextInt (count);
						Integer width = oracle.get (at);
						assertEquals (width == null ? UNMEASURED : width, intCall ("widthOf", INT, at));
					}
				}
				verify ();
			}
		}
	}

	/** Removing the only holder of the maximum marks the index stale; the next query rebuilds without measuring. */
	@Test
	public void removingTheWidestMarksTheIndexStale () {
		call ("insert", INT_STRING, 0, "12");
		call ("insert", INT_STRING, 1, "123456");
		call ("insert", INT_STRING, 2, "123");
		assertEquals (6, intCall ("maxWidth", NONE));
		call ("remove", INT_INT, 1, 1);
		assertTrue ((Boolean) call ("isStale", NONE));
		assertEquals (3, intCall ("maxWidth", NONE));
		assertFalse ((Boolean) call ("isStale", NONE));
		call ("remove", INT_INT, 0, 2);
		assertEquals (0, intCall ("maxWidth", NONE), "an empty lane answers 0");
		assertFalse ((Boolean) call ("isStale", NONE), "an empty lane is never stale");
	}

	/** A lane whose every slot is unmeasured stays a count: measuring one slot keeps the others pending. */
	@Test
	public void aUniformLaneMeasuresOneSlotWithoutMaterializing () {
		call ("setAllUnmeasured", INT, 1000);
		assertEquals (1000, intCall ("pendingCount", NONE));
		assertEquals (4, intCall ("measure", INT_STRING, 500, "1234"));
		assertEquals (4, intCall ("measure", INT_STRING, 500, "momentarily longer"), "a measured slot is not measured again");
		assertEquals (999, intCall ("pendingCount", NONE));
		assertEquals (4, intCall ("maxMeasured", NONE));
		call ("set", INT_STRING, 400, "12345");
		call ("setUnmeasured", INT, 400);
		call ("setUnmeasured", INT, 400);
		call ("insert", INT_STRING, 0, "123");
		call ("remove", INT_INT, 0, 2);
		assertEquals (4, intCall ("widthOf", INT, 499), "the measured slot moved with the removal");
		assertEquals (UNMEASURED, intCall ("widthOf", INT, 400));
	}

	/** The error contract: invalid indexes and ranges, a null item and a negative width are refused unchanged. */
	@Test
	public void invalidArgumentsAreRefused () {
		assertThrows (IllegalArgumentException.class, () -> call ("insert", INT_STRING, 1, "1"));
		assertThrows (IllegalArgumentException.class, () -> call ("insert", INT_STRING, -1, "1"));
		assertThrows (IllegalArgumentException.class, () -> call ("insert", INT_STRING, 0, (String) null));
		call ("insert", INT_STRING, 0, "12");
		assertThrows (IllegalArgumentException.class, () -> call ("set", INT_STRING, 1, "1"));
		assertThrows (IllegalArgumentException.class, () -> call ("widthOf", INT, -1));
		assertThrows (IllegalArgumentException.class, () -> call ("remove", INT_INT, 0, 2));
		assertThrows (IllegalArgumentException.class, () -> call ("remove", INT_INT, -1, 0));
		assertThrows (IllegalArgumentException.class, () -> call ("remove", INT_INT, 0, -1));
		assertThrows (IllegalArgumentException.class, () -> call ("insertUnmeasured", INT_INT, 2, 1));
		assertThrows (IllegalArgumentException.class, () -> call ("insertUnmeasured", INT_INT, -1, 1));
		assertThrows (IllegalArgumentException.class, () -> call ("insertUnmeasured", INT_INT, 0, -1));
		assertThrows (IllegalArgumentException.class, () -> call ("setAllUnmeasured", INT, -1));
		assertThrows (IllegalArgumentException.class, () -> call ("measure", INT_STRING, 1, "1"));
		Object negative = (ToIntFunction<String>) string -> -1;
		try {
			Constructor<?> constructor = Class.forName ("org.eclipse.swt.widgets.VirtualListWidth").getDeclaredConstructor (ToIntFunction.class);
			constructor.setAccessible (true);
			Object bad = constructor.newInstance (negative);
			Method insert = bad.getClass ().getDeclaredMethod ("insert", INT_STRING);
			insert.setAccessible (true);
			InvocationTargetException failure = assertThrows (InvocationTargetException.class, () -> insert.invoke (bad, 0, "1"));
			assertInstanceOf (IllegalArgumentException.class, failure.getCause (), "a negative width is refused");
			assertThrows (NullPointerException.class, () -> {
				try {
					constructor.newInstance ((Object) null);
				} catch (InvocationTargetException wrapped) {
					throw wrapped.getCause ();
				}
			});
		} catch (ReflectiveOperationException failure) {
			throw new AssertionError (failure);
		}
	}
}
