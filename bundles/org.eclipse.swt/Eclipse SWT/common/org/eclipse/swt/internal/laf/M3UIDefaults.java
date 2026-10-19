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
package org.eclipse.swt.internal.laf;

import java.util.*;

/**
 * The defaults of one look and feel as a delegate reads them (docs/m3/LOOK_AND_FEEL.md, LF6), Swing's
 * <code>UIDefaults</code> laid out as data: values in typed lanes indexed by the key ids of {@link M3UIKeys} (a colour
 * is an ARGB int, an integer or a font id an int, a boolean 0 or 1, insets an int quad), presence in a bitset. A read
 * is an array access: no hashing, no boxing, no allocation.
 * <p>
 * Two layers answer a key, as in Swing's <code>UIManager</code>: the developer layer ({@link #put}, shared by every
 * look and feel and kept across switches), then the theme. A key without a value answers its
 * <code>Component.&lt;property&gt;</code> parent ({@link M3UIKeys#parent}). Per-widget values come first when the
 * reader passes the widget and the display's {@link M3UIOverrides}.
 * </p>
 */
public final class M3UIDefaults {
	private final String name;
	private final Lanes theme, developer;

	/**
	 * The defaults of a resolved LF2 table, over a developer layer.
	 *
	 * @param table the resolved table
	 * @param developer the developer layer (shared by the look and feels of one manager)
	 * @throws IllegalArgumentException when a key of the table was interned with another kind
	 */
	public M3UIDefaults (LookAndFeelDefaults table, Lanes developer) {
		this.name = table.name ();
		this.developer = developer;
		this.theme = new Lanes ();
		String [] keys = table.keys ();
		for (int index = 0; index < keys.length; index++) {
			int id = M3UIKeys.intern (keys [index], table.kind (index));
			if (table.kind (index) == M3UIKeys.INSETS) {
				int [] insets = table.insets (index);
				theme.setQuad (id, insets [0], insets [1], insets [2], insets [3]);
			} else {
				theme.set (id, table.value (index));
			}
		}
	}

	/** The look and feel these defaults belong to. */
	public String name () {
		return name;
	}

	/** True when a key or its parent has a value of the key's kind. */
	public boolean contains (int key) {
		return resolve (key) >= 0;
	}

	/** The ARGB colour of a {@link M3UIKeys#COLOR} key, or the fallback. */
	public int color (int key, int fallback) {
		return scalar (key, M3UIKeys.COLOR, fallback);
	}

	/** The integer of a {@link M3UIKeys#INTEGER} key, or the fallback. */
	public int integer (int key, int fallback) {
		return scalar (key, M3UIKeys.INTEGER, fallback);
	}

	/** The boolean of a {@link M3UIKeys#BOOLEAN} key, or the fallback. */
	public boolean bool (int key, boolean fallback) {
		return scalar (key, M3UIKeys.BOOLEAN, fallback ? 1 : 0) != 0;
	}

	/** The font id of a {@link M3UIKeys#FONT} key, or the fallback. */
	public int font (int key, int fallback) {
		return scalar (key, M3UIKeys.FONT, fallback);
	}

	/**
	 * The insets of a {@link M3UIKeys#INSETS} key into <code>out</code> (top, left, bottom, right).
	 *
	 * @return false, leaving <code>out</code> unchanged, when the key has no insets
	 */
	public boolean insets (int key, int [] out) {
		int id = M3UIKeys.kind (key) == M3UIKeys.INSETS ? resolve (key) : -1;
		if (id < 0) return false;
		(developer.has (id) ? developer : theme).quad (id, out);
		return true;
	}

	/** The colour of a key for one widget: its override ({@link M3UIOverrides}), else {@link #color(int, int)}. */
	public int color (M3UIOverrides overrides, Object widget, int key, int fallback) {
		long hit = overrides.lookup (widget, key);
		return hit != M3UIOverrides.NONE ? (int) hit : color (key, fallback);
	}

	/** The integer of a key for one widget: its override, else {@link #integer(int, int)}. */
	public int integer (M3UIOverrides overrides, Object widget, int key, int fallback) {
		long hit = overrides.lookup (widget, key);
		return hit != M3UIOverrides.NONE ? (int) hit : integer (key, fallback);
	}

	/**
	 * Sets a value in the developer layer (Swing's <code>UIManager.put</code>): it answers before every theme until
	 * removed. Insets go through {@link #putInsets}.
	 */
	public void put (int key, int value) {
		if (M3UIKeys.kind (key) == M3UIKeys.INSETS) throw new IllegalArgumentException (M3UIKeys.name (key) + " is insets");
		developer.set (key, value);
	}

	/** Sets insets in the developer layer. */
	public void putInsets (int key, int top, int left, int bottom, int right) {
		if (M3UIKeys.kind (key) != M3UIKeys.INSETS) throw new IllegalArgumentException (M3UIKeys.name (key) + " is not insets");
		developer.setQuad (key, top, left, bottom, right);
	}

	/** Removes a value from the developer layer. */
	public void remove (int key) {
		developer.remove (key);
	}

	/**
	 * The keys whose answers differ between two defaults (by kind: the value, or the insets), as a bitset over key
	 * ids: what a switch from <code>from</code> to <code>to</code> changes.
	 */
	public static long [] diff (M3UIDefaults from, M3UIDefaults to) {
		int count = M3UIKeys.count ();
		long [] changed = new long [(count + 63) >>> 6];
		int [] a = new int [4], b = new int [4];
		for (int key = 0; key < count; key++) {
			if (differs (from, to, key, a, b)) changed [key >>> 6] |= 1L << key;
		}
		return changed;
	}

	private static boolean differs (M3UIDefaults from, M3UIDefaults to, int key, int [] a, int [] b) {
		int x = from.resolve (key), y = to.resolve (key);
		if (x < 0 || y < 0) return (x < 0) != (y < 0);
		if (M3UIKeys.kind (key) != M3UIKeys.INSETS) return from.value (x) != to.value (y);
		from.insets (key, a);
		to.insets (key, b);
		return !Arrays.equals (a, b);
	}

	private int scalar (int key, byte kind, int fallback) {
		int id = M3UIKeys.kind (key) == kind ? resolve (key) : -1;
		return id < 0 ? fallback : value (id);
	}

	private int value (int id) {
		return developer.has (id) ? developer.value (id) : theme.value (id);
	}

	/* the id that answers a key: the key, else its Component parent; -1 when neither has a value */
	private int resolve (int key) {
		if (developer.has (key) || theme.has (key)) return key;
		int parent = M3UIKeys.parent (key);
		return parent >= 0 && (developer.has (parent) || theme.has (parent)) ? parent : -1;
	}

	@Override
	public String toString () {
		return "M3UIDefaults {" + name + "}";
	}

	/**
	 * Values by key id in typed lanes: an int per key (colour, integer, boolean, font id), an int quad per insets key,
	 * a presence bit per key. Grows with the key registry.
	 */
	public static final class Lanes {
		private int [] values = new int [0], quads = new int [0];
		private long [] present = new long [0];

		/** True when the key has a value here. */
		public boolean has (int key) {
			int word = key >>> 6;
			return word < present.length && (present [word] & (1L << key)) != 0;
		}

		int value (int key) {
			return values [key];
		}

		void quad (int key, int [] out) {
			System.arraycopy (quads, key * 4, out, 0, 4);
		}

		void set (int key, int value) {
			ensure (key);
			values [key] = value;
			present [key >>> 6] |= 1L << key;
		}

		void setQuad (int key, int top, int left, int bottom, int right) {
			ensure (key);
			int at = key * 4;
			quads [at] = top;
			quads [at + 1] = left;
			quads [at + 2] = bottom;
			quads [at + 3] = right;
			present [key >>> 6] |= 1L << key;
		}

		void remove (int key) {
			if (has (key)) present [key >>> 6] &= ~(1L << key);
		}

		private void ensure (int key) {
			if (key < values.length) return;
			int length = Math.max (64, Integer.highestOneBit (key) * 2);
			values = Arrays.copyOf (values, length);
			quads = Arrays.copyOf (quads, length * 4);
			present = Arrays.copyOf (present, (length + 63) >>> 6);
		}
	}
}
