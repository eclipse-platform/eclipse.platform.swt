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

/**
 * The key registry of the look and feel defaults (docs/m3/LOOK_AND_FEEL.md, LF6): every key name, for example
 * <code>Tree.selectionBackground</code>, is interned once to a dense int id with a kind, so that {@link M3UIDefaults}
 * keeps values in typed lanes indexed by id and a delegate reads them with an array access, not a
 * <code>Hashtable</code> lookup of a boxed value (Swing's <code>UIDefaults</code>).
 * <p>
 * A key <code>&lt;Widget&gt;.&lt;property&gt;</code> has a parent, the key <code>Component.&lt;property&gt;</code> of
 * the same kind when it is interned: a theme that leaves <code>Tree.selectionBackground</code> out answers
 * <code>Component.selectionBackground</code>.
 * </p>
 * <p>
 * Interning is synchronized and happens once per key (typically in a delegate's static initializer); reads of the
 * lanes are lock-free. Ids are never reused.
 * </p>
 */
public final class M3UIKeys {
	/** Kinds of values: the LF2 kinds and fonts. */
	public static final byte COLOR = LookAndFeelDefaults.COLOR, INTEGER = LookAndFeelDefaults.INTEGER, BOOLEAN = LookAndFeelDefaults.BOOLEAN,
		INSETS = LookAndFeelDefaults.INSETS, FONT = 5;
	/** The widget prefix of the parent keys. */
	public static final String COMPONENT = "Component";

	private static final Object LOCK = new Object ();
	/* id -> name, kind, parent id (-1: none); published by replacing the arrays (lock-free reads) */
	private static volatile String [] names = new String [128];
	private static volatile byte [] kinds = new byte [128];
	private static volatile int [] parents = new int [128];
	private static volatile int count;
	/* open addressing: slot -> id + 1, 0 empty; capacity a power of two, at most half full */
	private static int [] slots = new int [256];

	private M3UIKeys () {
	}

	/**
	 * The id of a key, interned on first use.
	 *
	 * @param name the key, for example <code>Tree.rowHeight</code>
	 * @param kind {@link #COLOR}, {@link #INTEGER}, {@link #BOOLEAN}, {@link #INSETS} or {@link #FONT}
	 * @return the id
	 * @throws IllegalArgumentException when the key was interned with another kind, or the kind is unknown
	 */
	public static int intern (String name, byte kind) {
		if (kind < COLOR || kind > FONT) throw new IllegalArgumentException ("unknown kind " + kind + " of " + name);
		synchronized (LOCK) {
			int id = find (name);
			if (id < 0) id = add (name, kind);
			if (kinds [id] != kind) throw new IllegalArgumentException (name + " is a " + kindName (kinds [id]) + ", not a " + kindName (kind));
			return id;
		}
	}

	/** The id of <code>widget + "." + property</code>, interned on first use. */
	public static int of (String widget, String property, byte kind) {
		return intern (widget + "." + property, kind);
	}

	/** The id of an interned key, or -1. */
	public static int id (String name) {
		synchronized (LOCK) {
			return find (name);
		}
	}

	/** The number of interned keys; ids are <code>0 .. count () - 1</code>. */
	public static int count () {
		return count;
	}

	/** The name of an id. */
	public static String name (int id) {
		return names [id];
	}

	/** The kind of an id. */
	public static byte kind (int id) {
		return kinds [id];
	}

	/** The id of the <code>Component.&lt;property&gt;</code> parent of an id, or -1. */
	public static int parent (int id) {
		return parents [id];
	}

	/** A kind as a word, for messages. */
	public static String kindName (byte kind) {
		switch (kind) {
			case COLOR: return "colour";
			case INTEGER: return "integer";
			case BOOLEAN: return "boolean";
			case INSETS: return "insets";
			default: return "font";
		}
	}

	private static int find (String name) {
		int mask = slots.length - 1;
		for (int slot = mix (name.hashCode ()) & mask;; slot = (slot + 1) & mask) {
			int entry = slots [slot];
			if (entry == 0) return -1;
			if (names [entry - 1].equals (name)) return entry - 1;
		}
	}

	private static int add (String name, byte kind) {
		int parent = parentOf (name, kind);
		int id = count;
		if (id == names.length) grow ();
		names [id] = name;
		kinds [id] = kind;
		parents [id] = parent;
		if ((id + 1) * 2 > slots.length) rehash (slots.length * 2);
		place (slots, name, id);
		count = id + 1;
		return id;
	}

	/* the Component.<property> key of a <Widget>.<property> key, interned first with the same kind (-1: none) */
	private static int parentOf (String name, byte kind) {
		int dot = name.indexOf ('.');
		if (dot <= 0 || dot == name.length () - 1 || name.startsWith (COMPONENT + ".")) return -1;
		String parent = COMPONENT + name.substring (dot);
		int id = find (parent);
		if (id < 0) return add (parent, kind);
		return kinds [id] == kind ? id : -1;
	}

	private static void grow () {
		int length = names.length * 2;
		String [] newNames = java.util.Arrays.copyOf (names, length);
		byte [] newKinds = java.util.Arrays.copyOf (kinds, length);
		int [] newParents = java.util.Arrays.copyOf (parents, length);
		kinds = newKinds;
		parents = newParents;
		names = newNames;
	}

	private static void rehash (int capacity) {
		int [] newSlots = new int [capacity];
		for (int id = 0; id < count; id++) place (newSlots, names [id], id);
		slots = newSlots;
	}

	private static void place (int [] table, String name, int id) {
		int mask = table.length - 1;
		int slot = mix (name.hashCode ()) & mask;
		while (table [slot] != 0) slot = (slot + 1) & mask;
		table [slot] = id + 1;
	}

	/* spreads the String hash over the low bits (murmur3 finalizer) */
	private static int mix (int hash) {
		hash ^= hash >>> 16;
		hash *= 0x85ebca6b;
		hash ^= hash >>> 13;
		return hash;
	}
}
