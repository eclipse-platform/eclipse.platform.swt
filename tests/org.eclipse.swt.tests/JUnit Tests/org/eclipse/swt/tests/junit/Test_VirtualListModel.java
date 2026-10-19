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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.ToIntFunction;

import org.junit.jupiter.api.Test;

/**
 * Headless differential contracts for the platform-neutral list model and its
 * widest-item index; no native widget fixtures. The package-private owners are
 * reached by reflection, like the other viewport contract tests.
 */
public class Test_VirtualListModel {
	private static final String PACKAGE = "org.eclipse.swt.widgets.";

	/** Reflective handle on a package-private owner. */
	private static final class Handle {
		final Object target;

		Handle(String type, Object... arguments) {
			try {
				Constructor<?> constructor = Class.forName(PACKAGE + type).getDeclaredConstructor(types(arguments));
				constructor.setAccessible(true);
				target = constructor.newInstance(arguments);
			} catch (ReflectiveOperationException e) {
				throw new IllegalStateException(e);
			}
		}

		Object call(String name, Object... arguments) {
			return callTyped(name, types(arguments), arguments);
		}

		Object callTyped(String name, Class<?>[] types, Object... arguments) {
			try {
				Method method = target.getClass().getDeclaredMethod(name, types);
				method.setAccessible(true);
				return method.invoke(target, arguments);
			} catch (InvocationTargetException e) {
				if (e.getCause() instanceof RuntimeException runtime) throw runtime;
				throw new IllegalStateException(e.getCause());
			} catch (ReflectiveOperationException e) {
				throw new IllegalStateException(e);
			}
		}

		int number(String name, Object... arguments) {
			return (Integer) call(name, arguments);
		}
	}

	private static Class<?>[] types(Object... arguments) {
		Class<?>[] types = new Class<?>[arguments.length];
		for (int i = 0; i < arguments.length; i++) {
			Object argument = arguments[i];
			if (argument instanceof Integer) types[i] = int.class;
			else if (argument instanceof ToIntFunction) types[i] = ToIntFunction.class;
			else types[i] = argument.getClass();
		}
		return types;
	}

	/** Naive oracle: a list of strings and a parallel selection flag. */
	private static final class Oracle {
		final List<String> items = new ArrayList<>();
		final List<Boolean> selected = new ArrayList<>();
		int entered;

		void insert(int index, String s) {
			items.add(index, s);
			selected.add(index, false);
			entered++;
		}

		void remove(int start, int end) {
			for (int i = end; i >= start; i--) {
				items.remove(i);
				selected.remove(i);
			}
		}

		void setAll(String[] all) {
			items.clear();
			selected.clear();
			for (String s : all) {
				items.add(s);
				selected.add(false);
			}
			entered += all.length;
		}

		void mark(int start, int end, boolean value) {
			for (int i = Math.max(0, start); i <= Math.min(end, items.size() - 1); i++) selected.set(i, value);
		}

		int[] selection() {
			return java.util.stream.IntStream.range(0, items.size()).filter(selected::get).toArray();
		}

		int widest() {
			return items.stream().mapToInt(String::length).max().orElse(0);
		}
	}

	private static String text(Random random) {
		return "x".repeat(random.nextInt(9));
	}

	private static void assertSame(Handle model, Oracle oracle, String context) {
		assertEquals(oracle.items.size(), model.number("getItemCount"), context);
		assertArrayEquals(oracle.items.toArray(new String[0]), (String[]) model.call("getItems"), context);
		assertArrayEquals(oracle.selection(), (int[]) model.call("getSelectionIndices"), context);
		assertEquals(oracle.selection().length, model.number("getSelectionCount"), context);
		assertEquals(oracle.widest(), model.number("getWidestWidth"), context);
		int top = model.number("getTopIndex");
		int visible = model.number("getVisibleCount");
		assertTrue(top >= 0 && top <= Math.max(0, oracle.items.size() - visible), context + " top=" + top);
	}

	private static void apply(Handle model, Oracle oracle, Random random) {
		int count = oracle.items.size();
		int pick = random.nextInt(100);
		if (pick < 18) {
			String s = text(random);
			model.call("add", s);
			oracle.insert(count, s);
		} else if (pick < 34) {
			String s = text(random);
			int index = random.nextInt(count + 1);
			model.call("add", s, index);
			oracle.insert(index, s);
		} else if (pick < 42 && count > 0) {
			String s = text(random);
			int index = random.nextInt(count);
			model.call("setItem", index, s);
			oracle.items.set(index, s);
			oracle.entered++;
		} else if (pick < 52 && count > 0) {
			int index = random.nextInt(count);
			model.call("remove", index);
			oracle.remove(index, index);
		} else if (pick < 60) {
			int a = random.nextInt(count + 3) - 1;
			int b = random.nextInt(count + 3) - 1;
			if (a > b) {
				model.call("remove", a, b);
			} else if (a < 0 || b >= count) {
				assertThrows(IllegalArgumentException.class, () -> model.call("remove", a, b));
			} else {
				model.call("remove", a, b);
				oracle.remove(a, b);
			}
		} else if (pick < 62) {
			model.call("removeAll");
			oracle.remove(0, count - 1);
		} else if (pick < 65) {
			String[] all = new String[random.nextInt(30)];
			for (int i = 0; i < all.length; i++) all[i] = text(random);
			model.call("setItems", (Object) all);
			oracle.setAll(all);
		} else if (pick < 73) {
			int index = random.nextInt(count + 4) - 2;
			model.call("select", index);
			oracle.mark(index, index, true);
		} else if (pick < 79) {
			int index = random.nextInt(count + 4) - 2;
			model.call("deselect", index);
			oracle.mark(index, index, false);
		} else if (pick < 85) {
			int a = random.nextInt(count + 4) - 2;
			int b = random.nextInt(count + 4) - 2;
			model.call("select", a, b);
			oracle.mark(a, b, true);
		} else if (pick < 90) {
			int a = random.nextInt(count + 4) - 2;
			int b = random.nextInt(count + 4) - 2;
			model.call("deselect", a, b);
			oracle.mark(a, b, false);
		} else if (pick < 92) {
			model.call("selectAll");
			oracle.mark(0, count - 1, true);
		} else if (pick < 94) {
			model.call("deselectAll");
			oracle.mark(0, count - 1, false);
		} else if (pick < 98) {
			model.call("setTopIndex", random.nextInt(count + 40) - 20);
		} else {
			model.call("setGeometry", 1 + random.nextInt(5), random.nextInt(41));
		}
	}

	@Test
	public void randomOperationsAgreeWithNaiveOracle() {
		for (int seed = 1; seed <= 60; seed++) {
			Random random = new Random(seed);
			AtomicInteger measured = new AtomicInteger();
			ToIntFunction<String> measure = s -> {
				measured.incrementAndGet();
				return s.length();
			};
			Handle model = new Handle("VirtualListModel", measure);
			Oracle oracle = new Oracle();
			model.call("setGeometry", 2, 10);
			for (int step = 0; step < 500; step++) {
				apply(model, oracle, random);
				assertSame(model, oracle, "seed=" + seed + " step=" + step);
			}
			assertEquals(oracle.entered, measured.get(), "every entering item is measured exactly once, seed=" + seed);
		}
	}

	@Test
	public void invalidArgumentsAreRejectedWithoutChangingState() {
		Handle model = new Handle("VirtualListModel", (ToIntFunction<String>) String::length);
		model.call("setItems", (Object) new String[] {"a", "bb", "ccc"});
		assertThrows(IllegalArgumentException.class, () -> model.callTyped("add", new Class<?>[] {String.class}, (Object) null));
		assertThrows(IllegalArgumentException.class, () -> model.call("add", "x", 4));
		assertThrows(IllegalArgumentException.class, () -> model.call("add", "x", -1));
		assertThrows(IllegalArgumentException.class, () -> model.call("getItem", 3));
		assertThrows(IllegalArgumentException.class, () -> model.call("setItem", 3, "x"));
		assertThrows(IllegalArgumentException.class, () -> model.call("remove", 3));
		assertThrows(IllegalArgumentException.class, () -> model.call("remove", 3, 4));
		assertThrows(IllegalArgumentException.class, () -> model.call("remove", -1, 1));
		assertThrows(IllegalArgumentException.class, () -> model.call("setItems", (Object) new String[] {"a", null}));
		assertThrows(IllegalArgumentException.class, () -> model.call("setGeometry", 0, 10));
		assertThrows(IllegalArgumentException.class, () -> model.call("setGeometry", 1, -1));
		model.call("remove", 2, 0);
		assertEquals(3, model.number("getItemCount"));
		assertArrayEquals(new String[] {"a", "bb", "ccc"}, (String[]) model.call("getItems"));
		assertEquals(3, model.number("getWidestWidth"));
	}

	@Test
	public void topIndexStaysInsideTheScrollableRange() {
		Handle model = new Handle("VirtualListModel", (ToIntFunction<String>) String::length);
		model.call("setGeometry", 2, 10);
		String[] items = new String[100];
		Arrays.fill(items, "item");
		model.call("setItems", (Object) items);
		assertEquals(5, model.number("getVisibleCount"));
		model.call("setTopIndex", 1000);
		assertEquals(95, model.number("getTopIndex"));
		model.call("setTopIndex", -5);
		assertEquals(0, model.number("getTopIndex"));
		model.call("setTopIndex", 95);
		model.call("remove", 80, 99);
		assertEquals(75, model.number("getTopIndex"));
		model.call("removeAll");
		assertEquals(0, model.number("getTopIndex"));
	}

	@Test
	public void widestIndexRebuildsOnlyWhenTheLastMaximumLeavesAndNeverRemeasures() {
		AtomicInteger measured = new AtomicInteger();
		Handle width = new Handle("VirtualListWidth", (ToIntFunction<String>) s -> {
			measured.incrementAndGet();
			return s.length();
		});
		for (String s : new String[] {"aaa", "aaaaaaaaa", "aaaaaaaaa", "aa"}) width.call("insert", width.number("count"), s);
		assertEquals(9, width.number("maxWidth"));
		width.call("remove", 1, 1);
		assertFalse((Boolean) width.call("isStale"), "another holder of the maximum remains");
		assertEquals(9, width.number("maxWidth"));
		width.call("remove", 1, 1);
		assertTrue((Boolean) width.call("isStale"), "the last maximum left");
		assertEquals(3, width.number("maxWidth"));
		assertFalse((Boolean) width.call("isStale"));
		assertEquals(4, measured.get(), "rebuild scans the int lane and never measures text again");
		width.call("clear");
		assertEquals(0, width.number("maxWidth"));
	}

	@Test
	public void replacingTheMaximumWithEqualWidthKeepsTheIndexFresh() {
		Handle width = new Handle("VirtualListWidth", (ToIntFunction<String>) String::length);
		width.call("insert", 0, "aaa");
		width.call("insert", 1, "bbbbbbbbb");
		assertEquals(9, width.number("maxWidth"));
		width.call("set", 1, "ccccccccc");
		assertFalse((Boolean) width.call("isStale"), "equal-width replacement does not touch the maximum");
		width.call("set", 1, "c");
		assertTrue((Boolean) width.call("isStale"), "a narrower replacement removes the last maximum");
		assertEquals(3, width.number("maxWidth"));
	}

	@Test
	public void largeListKeepsItemsSelectionAndWidthConsistent() {
		int size = 200_000;
		Handle model = new Handle("VirtualListModel", (ToIntFunction<String>) String::length);
		String[] items = new String[size];
		for (int i = 0; i < size; i++) items[i] = "n" + i;
		model.call("setItems", (Object) items);
		model.call("setGeometry", 1, 40);
		model.call("select", 0, size - 1);
		assertEquals(size, model.number("getSelectionCount"));
		model.call("remove", 0, size / 2 - 1);
		assertEquals(size / 2, model.number("getItemCount"));
		assertEquals(size / 2, model.number("getSelectionCount"));
		assertEquals(("n" + (size - 1)).length(), model.number("getWidestWidth"));
		assertEquals("n" + (size / 2), model.call("getItem", 0));
		model.call("setTopIndex", size);
		assertEquals(size / 2 - 40, model.number("getTopIndex"));
	}
}
