/*
 * Copyright 2026 Hitesh Soliwal and contributors
 * SPDX-License-Identifier: EPL-2.0 OR Apache-2.0
 *
 * Dual-licensed by the rights holder's decision of 2026-10-13: you may use this file under the
 * Eclipse Public License 2.0 (https://www.eclipse.org/legal/epl-2.0/) or the Apache License 2.0
 * (https://www.apache.org/licenses/LICENSE-2.0).
 */
package org.eclipse.swt.internal.residency;

import java.util.Arrays;

import org.eclipse.swt.internal.PackedHeapGrowth;

/**
 * Resident payload slots of {@link ItemResidency} plus the spill codec. Arrays are sized by slot
 * capacity, never by item count, and are reused: a released slot is cleared and stacked.
 *
 * <p>Record wire form (big-endian): {@code int stride}, {@code stride} ints, per column a UTF-16
 * string ({@code int length}, -1 = null, then 2 bytes per unit), the blob ({@code int length},
 * -1 = null, then bytes), then the caller-data section written by {@link ItemResidency}.</p>
 */
public final class ItemSlots {
	final int columns, intStride;
	int capacity, highWater, live;
	int [] slotItem, slotRecord, slotInts, refBits, freeSlots;
	String [] slotText;
	byte [][] slotBlob;
	int freeCount;
	byte [] buf = new byte [256];
	int len, pos;
	private char [] chars = new char [64];

	public ItemSlots (int columns, int itemInts, int cellInts) {
		this.columns = columns;
		intStride = itemInts + columns * cellInts;
		resize (16);
	}

	private void resize (int next) {
		slotItem = Arrays.copyOf (slotItem == null ? new int [0] : slotItem, next);
		slotRecord = Arrays.copyOf (slotRecord == null ? new int [0] : slotRecord, next);
		Arrays.fill (slotItem, capacity, next, -1);
		Arrays.fill (slotRecord, capacity, next, -1);
		slotInts = Arrays.copyOf (slotInts == null ? new int [0] : slotInts, next * intStride);
		slotText = Arrays.copyOf (slotText == null ? new String [0] : slotText, next * columns);
		slotBlob = Arrays.copyOf (slotBlob == null ? new byte [0][] : slotBlob, next);
		refBits = Arrays.copyOf (refBits == null ? new int [0] : refBits, (next + 31) >>> 5);
		freeSlots = Arrays.copyOf (freeSlots == null ? new int [0] : freeSlots, next);
		capacity = next;
	}

	public int allocate (int item) {
		int s;
		if (freeCount > 0) {
			s = freeSlots [--freeCount];
		} else {
			if (highWater == capacity) resize (PackedHeapGrowth.capacity (capacity, capacity + 1, 4 * intStride));
			s = highWater++;
		}
		slotItem [s] = item;
		slotRecord [s] = -1;
		live++;
		ref (s, true);
		return s;
	}

	public void release (int s) {
		Arrays.fill (slotInts, s * intStride, (s + 1) * intStride, 0);
		Arrays.fill (slotText, s * columns, (s + 1) * columns, null);
		slotBlob [s] = null;
		slotItem [s] = -1;
		slotRecord [s] = -1;
		ref (s, false);
		freeSlots [freeCount++] = s;
		live--;
	}

	public boolean ref (int s) {
		return (refBits [s >>> 5] & (1 << s)) != 0;
	}

	public void ref (int s, boolean on) {
		int i = s >>> 5;
		refBits [i] = on ? refBits [i] | (1 << s) : refBits [i] & ~(1 << s);
	}

	/** Adds {@code delta} to every resident item at or above {@code from}. */
	public void shiftItems (int from, int delta) {
		for (int s = 0; s < highWater; s++) {
			if (slotItem [s] >= from) slotItem [s] += delta;
		}
	}

	public void encode (int s) {
		len = 0;
		putInt (intStride);
		for (int i = s * intStride, end = i + intStride; i < end; i++) {
			putInt (slotInts [i]);
		}
		for (int c = 0; c < columns; c++) {
			putString (slotText [s * columns + c]);
		}
		putBytes (slotBlob [s]);
	}

	/** Decodes the payload part of {@code buf[0, len)} into slot {@code s}; {@link #pos} ends after it. */
	public void decode (int s) {
		pos = 0;
		if (getInt () != intStride) throw new IllegalStateException ("spill record stride mismatch");
		for (int i = s * intStride, end = i + intStride; i < end; i++) {
			slotInts [i] = getInt ();
		}
		for (int c = 0; c < columns; c++) {
			slotText [s * columns + c] = getString ();
		}
		slotBlob [s] = getBytes ();
	}

	public void ensureBuffer (int n) {
		if (n > buf.length) buf = Arrays.copyOf (buf, PackedHeapGrowth.capacity (buf.length, n, 1));
	}

	public void putInt (int v) {
		ensureBuffer (len + 4);
		buf [len] = (byte) (v >>> 24);
		buf [len + 1] = (byte) (v >>> 16);
		buf [len + 2] = (byte) (v >>> 8);
		buf [len + 3] = (byte) v;
		len += 4;
	}

	public void putString (String s) {
		if (s == null) {
			putInt (-1);
			return;
		}
		int n = s.length ();
		putInt (n);
		ensureBuffer (len + 2 * n);
		for (int i = 0; i < n; i++) {
			char ch = s.charAt (i);
			buf [len++] = (byte) (ch >>> 8);
			buf [len++] = (byte) ch;
		}
	}

	public void putBytes (byte [] b) {
		if (b == null) {
			putInt (-1);
			return;
		}
		putInt (b.length);
		ensureBuffer (len + b.length);
		System.arraycopy (b, 0, buf, len, b.length);
		len += b.length;
	}

	public int getInt () {
		int v = ((buf [pos] & 0xFF) << 24) | ((buf [pos + 1] & 0xFF) << 16)
				| ((buf [pos + 2] & 0xFF) << 8) | (buf [pos + 3] & 0xFF);
		pos += 4;
		return v;
	}

	public String getString () {
		int n = getInt ();
		if (n < 0) return null;
		if (n > chars.length) chars = new char [PackedHeapGrowth.capacity (chars.length, n, 2)];
		for (int i = 0; i < n; i++, pos += 2) {
			chars [i] = (char) (((buf [pos] & 0xFF) << 8) | (buf [pos + 1] & 0xFF));
		}
		return new String (chars, 0, n);
	}

	public byte [] getBytes () {
		int n = getInt ();
		if (n < 0) return null;
		byte [] b = new byte [n];
		System.arraycopy (buf, pos, b, 0, n);
		pos += n;
		return b;
	}

	public int bytes () {
		return capacity * (4 * (4 + intStride) + 4 * (columns + 1)) + refBits.length * 4;
	}
}
