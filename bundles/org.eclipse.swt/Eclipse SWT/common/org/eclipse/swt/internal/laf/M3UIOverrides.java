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

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;

/**
 * Per-widget look and feel values (docs/m3/LOOK_AND_FEEL.md, LF6, improvement 6), Swing's
 * <code>JComponent.putClientProperty</code> without a map per widget: one sparse lane per display. Owners (widgets,
 * by identity) get a dense slot in an open-addressing table; values live in a second open-addressing table keyed by
 * <code>slot &lt;&lt; 32 | key id &lt;&lt; 2 | insets component</code>. A widget without overrides costs nothing, and
 * a display without any answers every lookup after one comparison.
 * <p>
 * A widget owner is removed when it is disposed (one shared dispose listener). Not thread-safe: like the widgets, a
 * lane belongs to its display's thread.
 * </p>
 */
public final class M3UIOverrides {
	/** The answer of {@link #lookup} when the owner has no value for the key. */
	public static final long NONE = 0;
	private static final Listener DISPOSED = event -> {
		M3UIOverrides lane = (M3UIOverrides) event.display.getData (M3UIOverrides.class.getName ());
		if (lane != null) lane.clear (event.widget);
	};

	/* owners: open addressing by identity hash, slot of each; tombstone = removed owner */
	private static final Object TOMBSTONE = new Object ();
	private Object [] owners = new Object [16];
	private int [] ownerSlots = new int [16];
	private int ownerCount, ownerUsed, nextSlot;
	/* values: open addressing by packed key (0 never a key: slots start at 1) */
	private long [] keys = new long [64];
	private int [] values = new int [64];
	private int valueCount, valueUsed;

	/** The lane of a display, created on first use (kept in the display's data). */
	public static M3UIOverrides of (Display display) {
		String key = M3UIOverrides.class.getName ();
		M3UIOverrides lane = (M3UIOverrides) display.getData (key);
		if (lane == null) display.setData (key, lane = new M3UIOverrides ());
		return lane;
	}

	/** The number of owners with at least one value. */
	public int owners () {
		return ownerCount;
	}

	/** The number of values (an insets value counts four). */
	public int size () {
		return valueCount;
	}

	/**
	 * The value of a key for an owner, packed as <code>1L &lt;&lt; 32 | value &amp; 0xffffffffL</code>, or
	 * {@link #NONE}: <code>(int) lookup (...)</code> is the value.
	 */
	public long lookup (Object owner, int key) {
		if (ownerCount == 0) return NONE;
		int slot = slotOf (owner);
		if (slot == 0) return NONE;
		int at = find (pack (slot, key, 0));
		return at < 0 ? NONE : 1L << 32 | values [at] & 0xffffffffL;
	}

	/** The insets of a key for an owner into <code>out</code>; false when the owner has none. */
	public boolean insets (Object owner, int key, int [] out) {
		int slot = ownerCount == 0 ? 0 : slotOf (owner);
		if (slot == 0 || find (pack (slot, key, 0)) < 0) return false;
		for (int i = 0; i < 4; i++) out [i] = values [find (pack (slot, key, i))];
		return true;
	}

	/** Sets the value of a scalar key for one owner (a widget is cleared when disposed). */
	public void put (Object owner, int key, int value) {
		if (M3UIKeys.kind (key) == M3UIKeys.INSETS) throw new IllegalArgumentException (M3UIKeys.name (key) + " is insets");
		store (pack (slotFor (owner), key, 0), value);
	}

	/** Sets the insets of a key for one owner. */
	public void putInsets (Object owner, int key, int top, int left, int bottom, int right) {
		if (M3UIKeys.kind (key) != M3UIKeys.INSETS) throw new IllegalArgumentException (M3UIKeys.name (key) + " is not insets");
		int slot = slotFor (owner);
		int [] quad = {top, left, bottom, right};
		for (int i = 0; i < 4; i++) store (pack (slot, key, i), quad [i]);
	}

	/** Removes the value of a key for one owner. */
	public void remove (Object owner, int key) {
		int slot = ownerCount == 0 ? 0 : slotOf (owner);
		if (slot == 0) return;
		int parts = M3UIKeys.kind (key) == M3UIKeys.INSETS ? 4 : 1;
		for (int i = 0; i < parts; i++) delete (pack (slot, key, i));
	}

	/** Removes every value of one owner. */
	public void clear (Object owner) {
		int at = ownerCount == 0 ? -1 : ownerAt (owner);
		if (at < 0) return;
		long slot = ownerSlots [at];
		for (int i = 0; i < keys.length; i++) {
			if (keys [i] != 0 && keys [i] != -1 && keys [i] >>> 32 == slot) {
				keys [i] = -1;
				valueCount--;
			}
		}
		owners [at] = TOMBSTONE;
		ownerCount--;
	}

	private static long pack (int slot, int key, int component) {
		return (long) slot << 32 | (long) key << 2 | component;
	}

	private int slotOf (Object owner) {
		int at = ownerAt (owner);
		return at < 0 ? 0 : ownerSlots [at];
	}

	private int ownerAt (Object owner) {
		int mask = owners.length - 1;
		for (int at = System.identityHashCode (owner) & mask;; at = (at + 1) & mask) {
			Object o = owners [at];
			if (o == null) return -1;
			if (o == owner) return at;
		}
	}

	private int slotFor (Object owner) {
		int slot = slotOf (owner);
		if (slot != 0) return slot;
		if ((ownerUsed + 1) * 2 > owners.length) rehashOwners ();
		int mask = owners.length - 1, at = System.identityHashCode (owner) & mask;
		while (owners [at] != null) at = (at + 1) & mask;
		owners [at] = owner;
		ownerSlots [at] = slot = ++nextSlot;
		ownerCount++;
		ownerUsed++;
		if (owner instanceof Widget widget) widget.addListener (SWT.Dispose, DISPOSED);
		return slot;
	}

	private void rehashOwners () {
		Object [] oldOwners = owners;
		int [] oldSlots = ownerSlots;
		int capacity = Math.max (16, Integer.highestOneBit (Math.max (1, ownerCount) * 4));
		owners = new Object [capacity];
		ownerSlots = new int [capacity];
		ownerUsed = 0;
		for (int i = 0; i < oldOwners.length; i++) {
			if (oldOwners [i] == null || oldOwners [i] == TOMBSTONE) continue;
			int at = System.identityHashCode (oldOwners [i]) & (capacity - 1);
			while (owners [at] != null) at = (at + 1) & (capacity - 1);
			owners [at] = oldOwners [i];
			ownerSlots [at] = oldSlots [i];
			ownerUsed++;
		}
	}

	/* values: 0 is an empty slot, -1 a deleted one */
	private int find (long key) {
		int mask = keys.length - 1;
		for (int at = mix (key) & mask;; at = (at + 1) & mask) {
			long k = keys [at];
			if (k == 0) return -1;
			if (k == key) return at;
		}
	}

	private void store (long key, int value) {
		int at = find (key);
		if (at < 0) {
			if ((valueUsed + 1) * 2 > keys.length) rehashValues ();
			int mask = keys.length - 1;
			at = mix (key) & mask;
			while (keys [at] != 0 && keys [at] != -1) at = (at + 1) & mask;
			if (keys [at] == 0) valueUsed++;
			keys [at] = key;
			valueCount++;
		}
		values [at] = value;
	}

	private void delete (long key) {
		int at = find (key);
		if (at < 0) return;
		keys [at] = -1;
		valueCount--;
	}

	private void rehashValues () {
		long [] oldKeys = keys;
		int [] oldValues = values;
		int capacity = Math.max (64, Integer.highestOneBit (Math.max (1, valueCount) * 4));
		keys = new long [capacity];
		values = new int [capacity];
		valueUsed = 0;
		for (int i = 0; i < oldKeys.length; i++) {
			if (oldKeys [i] == 0 || oldKeys [i] == -1) continue;
			int at = mix (oldKeys [i]) & (capacity - 1);
			while (keys [at] != 0) at = (at + 1) & (capacity - 1);
			keys [at] = oldKeys [i];
			values [at] = oldValues [i];
			valueUsed++;
		}
	}

	private static int mix (long key) {
		key ^= key >>> 33;
		key *= 0xff51afd7ed558ccdL;
		key ^= key >>> 33;
		return (int) key;
	}

	@Override
	public String toString () {
		return "M3UIOverrides {" + ownerCount + " owners, " + valueCount + " values}";
	}
}
