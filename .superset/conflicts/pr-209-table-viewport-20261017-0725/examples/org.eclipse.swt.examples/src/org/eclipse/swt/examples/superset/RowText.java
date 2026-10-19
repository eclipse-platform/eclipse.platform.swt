/*******************************************************************************
 * Copyright (c) 2026 Hitesh Soliwal and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package org.eclipse.swt.examples.superset;

/**
 * Generated dataset with no per-row storage: every cell value is a pure int
 * function of (row, column), so 10M rows cost zero bytes until a widget asks.
 * Text is formatted into one reusable char buffer; a String is allocated only
 * when the widget API requires one (SetData, setItems, getLine).
 */
public final class RowText {

	/** Width of a generated text line, newline excluded. */
	public static final int LINE_CHARS = 63;

	private static final char[] HEX = "0123456789abcdef".toCharArray();
	private final char[] buf = new char[LINE_CHARS + 1];

	/** @return the packed int value of a cell (murmur3 finalizer of row/col) */
	public static int value(int row, int col) {
		int h = row * 0x9E3779B1 + col * 0x85EBCA6B;
		h ^= h >>> 16;
		h *= 0x85EBCA6B;
		h ^= h >>> 13;
		h *= 0xC2B2AE35;
		return h ^ (h >>> 16);
	}

	/** @return "R123456.c 0x1a2b3c4d" for a table/tree/list cell */
	public String cell(int row, int col) {
		int n = put(0, 'R');
		n = putInt(n, row);
		n = put(n, '.');
		n = putInt(n, col);
		n = put(n, ' ');
		n = putHex(n, value(row, col));
		return new String(buf, 0, n);
	}

	/** Fills {@code dst[off..off+LINE_CHARS)} with line {@code row}; no allocation. */
	public void line(int row, char[] dst, int off) {
		int n = putInt(0, row);
		n = put(n, ':');
		int v = value(row, 0);
		for (int i = n; i < LINE_CHARS; i++) {
			buf[i] = ((i & 7) == 7) ? ' ' : HEX[(v >>> ((i & 7) * 4)) & 15];
		}
		System.arraycopy(buf, 0, dst, off, LINE_CHARS);
	}

	private int put(int n, char c) {
		buf[n] = c;
		return n + 1;
	}

	private int putInt(int n, int v) {
		int start = n;
		do {
			buf[n++] = (char) ('0' + v % 10);
			v /= 10;
		} while (v > 0);
		for (int i = start, j = n - 1; i < j; i++, j--) {
			char t = buf[i];
			buf[i] = buf[j];
			buf[j] = t;
		}
		return n;
	}

	private int putHex(int n, int v) {
		for (int s = 28; s >= 0; s -= 4) {
			buf[n++] = HEX[(v >>> s) & 15];
		}
		return n;
	}
}
