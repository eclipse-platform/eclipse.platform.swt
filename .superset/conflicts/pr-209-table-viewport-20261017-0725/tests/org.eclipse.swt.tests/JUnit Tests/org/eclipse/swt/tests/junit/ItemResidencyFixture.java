// Copyright 2026 Hitesh Soliwal and Contributors to the Synexia Project.
// SPDX-License-Identifier: Apache-2.0
package org.eclipse.swt.tests.junit;

import java.lang.reflect.*;
import java.util.*;

/**
 * Reflective fixture for the internal {@code org.eclipse.swt.internal.residency} owners,
 * like the other viewport contract tests. Engine methods have unique (name, arity) pairs.
 */
final class ItemResidencyFixture {
	static final String PACKAGE = "org.eclipse.swt.internal.residency.";
	static final Class<?> ENGINE = type ("ItemResidency");

	private ItemResidencyFixture () {
	}

	static Class<?> type (String simple) {
		try {
			return Class.forName (PACKAGE + simple);
		} catch (ClassNotFoundException e) {
			throw new IllegalStateException (e);
		}
	}

	/** A static int constant of {@code ItemResidency}. */
	static int k (String name) {
		try {
			Field f = ENGINE.getDeclaredField (name);
			f.setAccessible (true);
			return f.getInt (null);
		} catch (ReflectiveOperationException e) {
			throw new IllegalStateException (e);
		}
	}

	private static Method method (Class<?> owner, String name, int arity) {
		for (Class<?> c = owner; c != null; c = c.getSuperclass ()) {
			for (Method m : c.getDeclaredMethods ()) {
				if (m.getName ().equals (name) && m.getParameterCount () == arity && !m.isSynthetic ()) {
					m.setAccessible (true);
					return m;
				}
			}
		}
		throw new IllegalArgumentException (owner.getSimpleName () + "." + name + "/" + arity);
	}

	static Object call (Object target, String name, Object... args) {
		try {
			return method (target.getClass (), name, args.length).invoke (target, args);
		} catch (InvocationTargetException e) {
			Throwable cause = e.getCause ();
			if (cause instanceof RuntimeException) throw (RuntimeException) cause;
			if (cause instanceof Error) throw (Error) cause;
			throw new IllegalStateException (cause);
		} catch (IllegalAccessException e) {
			throw new IllegalStateException (e);
		}
	}

	static Object callStatic (Class<?> owner, String name, Object... args) {
		try {
			return method (owner, name, args.length).invoke (null, args);
		} catch (InvocationTargetException e) {
			Throwable cause = e.getCause ();
			if (cause instanceof RuntimeException) throw (RuntimeException) cause;
			if (cause instanceof Error) throw (Error) cause;
			throw new IllegalStateException (cause);
		} catch (IllegalAccessException e) {
			throw new IllegalStateException (e);
		}
	}

	static int i (Object target, String name, Object... args) {
		return (Integer) call (target, name, args);
	}

	static boolean b (Object target, String name, Object... args) {
		return (Boolean) call (target, name, args);
	}

	static Object proxy (String nestedInterface, InvocationHandler handler) {
		Class<?> iface = type ("ItemResidency$" + nestedInterface);
		return Proxy.newProxyInstance (iface.getClassLoader (), new Class<?> [] {iface}, (p, m, a) -> {
			if (m.isDefault () && !m.getName ().equals ("cancel") && !m.getName ().equals ("urgent")) {
				return InvocationHandler.invokeDefault (p, m, a);
			}
			if (m.getDeclaringClass () == Object.class) {
				return switch (m.getName ()) {
					case "hashCode" -> System.identityHashCode (p);
					case "equals" -> p == a [0];
					default -> "proxy:" + nestedInterface;
				};
			}
			return handler.invoke (p, m, a);
		});
	}

	static Object engine (int config, int columns, int hardSlots, Object source, Object spill, Object pressure) {
		try {
			Constructor<?> c = ENGINE.getDeclaredConstructors () [0];
			c.setAccessible (true);
			return c.newInstance (config, columns, hardSlots, source, spill, pressure);
		} catch (InvocationTargetException e) {
			throw new IllegalStateException (e.getCause ());
		} catch (ReflectiveOperationException e) {
			throw new IllegalStateException (e);
		}
	}

	/** Desktop COLD tier: ported TieredSwapCache over the Java FileSwapStore. */
	static Object spill () {
		return callStatic (type ("ItemSwapSpill"), "temporary", 1L << 20);
	}

	static Object field (Object target, String name) {
		for (Class<?> c = target.getClass (); c != null; c = c.getSuperclass ()) {
			try {
				Field f = c.getDeclaredField (name);
				f.setAccessible (true);
				return f.get (target);
			} catch (NoSuchFieldException e) {
				// try the superclass
			} catch (IllegalAccessException e) {
				throw new IllegalStateException (e);
			}
		}
		throw new IllegalArgumentException (name);
	}

	static Object newInstance (String simple, Object... args) {
		try {
			for (Constructor<?> c : type (simple).getDeclaredConstructors ()) {
				if (c.getParameterCount () == args.length) {
					c.setAccessible (true);
					return c.newInstance (args);
				}
			}
		} catch (InvocationTargetException e) {
			Throwable cause = e.getCause ();
			if (cause instanceof RuntimeException) throw (RuntimeException) cause;
			if (cause instanceof Error) throw (Error) cause;
			throw new IllegalStateException (cause);
		} catch (ReflectiveOperationException e) {
			throw new IllegalStateException (e);
		}
		throw new IllegalArgumentException (simple + "/" + args.length);
	}

	/** Deterministic synchronous source: fills text, colours, height and a cell font per item. */
	static final class Model {
		final int [] requests;
		final List<int []> ranges = new ArrayList<> (), cancels = new ArrayList<> ();
		final boolean async;
		Object source;

		Model (int count, boolean async) {
			requests = new int [count];
			this.async = async;
			source = proxy ("DataSource", (p, m, a) -> {
				Object owner = a [0];
				int from = (Integer) a [1], to = (Integer) a [2];
				if (m.getName ().equals ("cancel")) {
					cancels.add (new int [] {from, to});
					return null;
				}
				ranges.add (new int [] {from, to});
				for (int item = from; item < to; item++) {
					requests [item]++;
					if (!async) fill (owner, item);
				}
				return null;
			});
		}

		static void fill (Object e, int item) {
			call (e, "beginData", item);
			call (e, "setText", item, 0, "row " + item);
			call (e, "setText", item, 1, "cell " + (item * 7));
			call (e, "setInt", item, k ("I_FOREGROUND"), 0xFF000000 | ((item * 2654435) & 0xFFFFFF));
			call (e, "setInt", item, k ("I_ROW_HEIGHT"), 18 + item % 5);
			call (e, "setCellInt", item, 1, k ("C_FONT"), 1 + item % 3);
			call (e, "endData", item);
		}
	}

	/** Thin test handle: int[] {item, generation}. */
	static Object handleFactory (List<int []> created) {
		return proxy ("HandleFactory", (p, m, a) -> {
			if (m.getName ().equals ("create")) {
				int [] h = {(Integer) a [0], (Integer) a [1]};
				created.add (h);
				return h;
			}
			int [] h = (int []) a [0];
			h [0] = (Integer) a [1];
			h [1] = (Integer) a [2];
			return null;
		});
	}

	static final String [] USER_LANES = {"LANE_EXPANDED", "LANE_HAS_CHILDREN", "LANE_CHILDREN_KNOWN",
			"LANE_CHILDREN_LOADED", "LANE_LOADING_PLACEHOLDER", "LANE_SELECTED", "LANE_FOCUSED", "LANE_HOVERED",
			"LANE_DISABLED", "LANE_FILTERED_OUT", "LANE_CHECKED", "LANE_GRAYED", "LANE_CHECKABLE",
			"LANE_DIRTY_PAINT", "LANE_DIRTY_MEASURE", "LANE_DIRTY_DATA"};

	static final String [] ITEM_FIELDS = {"I_FOREGROUND", "I_BACKGROUND", "I_FONT", "I_IMAGE", "I_ROW_HEIGHT",
			"I_INDENT", "I_LEVEL", "I_TOOLTIP", "I_ACCESSIBLE", "I_USER"};

	static final String [] CELL_FIELDS = {"C_FOREGROUND", "C_BACKGROUND", "C_FONT", "C_IMAGE"};

	/** Sets every user lane, payload field, cell field, blob, data and keyed data on one item. */
	static void setEveryState (Object e, int item, boolean callerPayload) {
		for (int l = 0; l < USER_LANES.length; l++) {
			call (e, "setBit", k (USER_LANES [l]), item, ((item * 0x9E3779B1) >>> l & 1) != 0);
		}
		if (callerPayload) {
			for (int f = 0; f < ITEM_FIELDS.length; f++) {
				call (e, "setInt", item, k (ITEM_FIELDS [f]), item * 31 + f + 1);
			}
			for (int c = 0; c < 2; c++) {
				for (int f = 0; f < CELL_FIELDS.length; f++) {
					call (e, "setCellInt", item, c, k (CELL_FIELDS [f]), item * 17 + c * 5 + f + 1);
				}
				call (e, "setText", item, c, "caller " + item + "/" + c);
			}
			call (e, "setBlob", item, new byte [] {(byte) item, 7, (byte) (item >>> 8)});
		}
		call (e, "setData", item, item % 2 == 0 ? (Object) ("data-" + item) : new int [] {item});
		call (e, "setKeyedData", item, "k", "keyed-" + item);
		call (e, "setKeyedData", item, "obj", new StringBuilder ("sb-" + item));
	}

	/**
	 * Every public value of one item. Codec-swappable strings compare by value; other data
	 * compares by identity (the identity hash is recorded).
	 */
	static String snapshot (Object e, int item) {
		StringBuilder s = new StringBuilder ();
		s.append ("lanes=").append (Integer.toHexString (i (e, "gather", item) & userMask ()));
		for (String f : ITEM_FIELDS) s.append (' ').append (f).append ('=').append (i (e, "getInt", item, k (f)));
		for (int c = 0; c < 2; c++) {
			s.append (" text").append (c).append ('=').append (call (e, "getText", item, c));
			for (String f : CELL_FIELDS) s.append (' ').append (f).append (c).append ('=').append (i (e, "getCellInt", item, c, k (f)));
		}
		s.append (" blob=").append (Arrays.toString ((byte []) call (e, "getBlob", item)));
		s.append (" data=").append (describe (call (e, "getData", item)));
		s.append (" k=").append (describe (call (e, "getKeyedData", item, "k")));
		s.append (" obj=").append (describe (call (e, "getKeyedData", item, "obj")));
		return s.toString ();
	}

	static String describe (Object v) {
		if (v == null || v instanceof String) return String.valueOf (v);
		return v.getClass ().getSimpleName () + "@" + System.identityHashCode (v);
	}

	static int userMask () {
		int mask = 0;
		for (String l : USER_LANES) mask |= 1 << k (l);
		return mask;
	}

	/** Codec that declares String values immutable and swappable. */
	static Object stringCodec () {
		return proxy ("DataCodec", (p, m, a) -> switch (m.getName ()) {
			case "accepts" -> a [0] instanceof String;
			case "encode" -> ((String) a [0]).getBytes (java.nio.charset.StandardCharsets.UTF_8);
			default -> new String ((byte []) a [0], java.nio.charset.StandardCharsets.UTF_8);
		});
	}

	/** Mutable pressure source; a negative value throws (telemetry failure). */
	static final class Gauge {
		int permille = 1000;
		final Object source = proxy ("Pressure", (p, m, a) -> {
			if (m.getName ().equals ("urgent")) return false;
			if (permille < 0) throw new IllegalStateException ("telemetry down");
			return permille;
		});
	}
}
