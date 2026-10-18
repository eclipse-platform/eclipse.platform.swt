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
 * The resolved defaults table of a look and feel: keys (for example <code>Button.background</code>,
 * <code>Tree.rowHeight</code>) to colours, integers, booleans and insets, like Swing's <code>UIDefaults</code>.
 * Every value is resolved once, when the table is loaded ({@link LookAndFeelLoader}), into packed ints: a colour
 * is an ARGB int, a boolean 0 or 1, insets four ints. Painting looks a key up once ({@link #index}) and then reads an
 * int array ({@link #value(int)}), never a map.
 */
public final class LookAndFeelDefaults {
	/** Kinds of values. */
	public static final byte COLOR = 1, INTEGER = 2, BOOLEAN = 3, INSETS = 4;

	private final String name;
	private final String [] keys;
	private final byte [] kinds;
	private final int [] values;
	private final int [] insets;

	LookAndFeelDefaults (String name, String [] keys, byte [] kinds, int [] values, int [] insets) {
		this.name = name;
		this.keys = keys;
		this.kinds = kinds;
		this.values = values;
		this.insets = insets;
	}

	/** The name of the look and feel these defaults belong to. */
	public String name () {
		return name;
	}

	/** The number of keys. */
	public int size () {
		return keys.length;
	}

	/** The keys, sorted. */
	public String [] keys () {
		return keys.clone ();
	}

	/** The index of a key (stable for this table), or -1 when the table has no such key. */
	public int index (String key) {
		int index = Arrays.binarySearch (keys, key);
		return index < 0 ? -1 : index;
	}

	/** The kind of the value at an index ({@link #COLOR}, {@link #INTEGER}, {@link #BOOLEAN}, {@link #INSETS}). */
	public byte kind (int index) {
		return kinds [index];
	}

	/**
	 * The packed value at an index: the ARGB colour, the integer, 0 or 1 for a boolean, or for insets the position of
	 * the four ints in {@link #insets(int)}.
	 */
	public int value (int index) {
		return values [index];
	}

	/** The ARGB colour of a key, or the fallback when the key is missing or not a colour. */
	public int color (String key, int fallback) {
		int index = index (key);
		return index >= 0 && kinds [index] == COLOR ? values [index] : fallback;
	}

	/** The integer of a key, or the fallback when the key is missing or not an integer. */
	public int integer (String key, int fallback) {
		int index = index (key);
		return index >= 0 && kinds [index] == INTEGER ? values [index] : fallback;
	}

	/** The boolean of a key, or the fallback when the key is missing or not a boolean. */
	public boolean bool (String key, boolean fallback) {
		int index = index (key);
		return index >= 0 && kinds [index] == BOOLEAN ? values [index] != 0 : fallback;
	}

	/** The insets (top, left, bottom, right) of a key, or <code>null</code> when the key is missing or not insets. */
	public int [] insets (String key) {
		int index = index (key);
		return index >= 0 && kinds [index] == INSETS ? insets (index) : null;
	}

	/** The insets (top, left, bottom, right) at an index of kind {@link #INSETS}. */
	public int [] insets (int index) {
		int at = values [index];
		return new int [] {insets [at], insets [at + 1], insets [at + 2], insets [at + 3]};
	}

	/** The value of a key as it would be written in a properties file (for logs and tests). */
	public String format (String key) {
		int index = index (key);
		if (index < 0) return null;
		int value = values [index];
		switch (kinds [index]) {
			case COLOR: return String.format ("#%08x", value);
			case BOOLEAN: return value != 0 ? "true" : "false";
			case INSETS: {
				int [] i = insets (index);
				return i [0] + "," + i [1] + "," + i [2] + "," + i [3];
			}
			default: return Integer.toString (value);
		}
	}

	@Override
	public String toString () {
		return "LookAndFeelDefaults {" + name + ", " + keys.length + " keys}";
	}
}
