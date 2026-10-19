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
package org.eclipse.swt.m3;

import java.util.*;

import org.eclipse.swt.*;

/**
 * A bounded vocabulary that maps tokens (regions of text) to dense <code>int</code> ids
 * (enhanced M3 language API).
 * <p>
 * Ids run from 1 to {@link #size()} in interning order and stay stable for the life of the
 * vocabulary; {@link #UNKNOWN} (0) answers a token that is absent or that no longer fits. The
 * ids are the non-negative symbols a token cursor reads (see
 * {@link M3TextSegments#tokens(String, M3TokenVocabulary, boolean)} and {@link M3TokenMatch}).
 * </p>
 * <p>
 * No <code>String</code> is created to intern or look up a token: keys are kept in one
 * character pool with start and end lanes and found by open addressing. With case folding,
 * each UTF-16 unit is compared as <code>Character.toLowerCase(Character.toUpperCase(c))</code>
 * (locale-independent; supplementary letters keep their case).
 * </p>
 *
 * @since 3.138
 */
public final class M3TokenVocabulary {

	/** The id of a token that is absent or did not fit. */
	public static final int UNKNOWN = 0;

	private final int capacity;
	private final boolean foldCase;
	/* folded key characters of all tokens, back to back */
	private char [] pool = new char [64];
	private int poolSize;
	/* per id (1..size): key region in the pool and its hash; index 0 unused */
	private int [] starts = new int [16];
	private int [] ends = new int [16];
	private int [] hashes = new int [16];
	/* open addressing: 0 = empty slot, else an id; length a power of two, load at most 1/2 */
	private int [] table = new int [16];
	private int size;

	/**
	 * Constructs an empty vocabulary.
	 *
	 * @param capacity the maximum number of tokens, at least 1
	 * @param foldCase whether tokens that differ only in case share an id
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_INVALID_ARGUMENT - if the capacity is smaller than 1</li>
	 * </ul>
	 */
	public M3TokenVocabulary (int capacity, boolean foldCase) {
		if (capacity < 1) SWT.error (SWT.ERROR_INVALID_ARGUMENT);
		this.capacity = capacity;
		this.foldCase = foldCase;
	}

	/**
	 * Returns the id of the token <code>text[start, end)</code>, adding it when it is absent
	 * and the vocabulary has room; {@link #UNKNOWN} when it is absent and the vocabulary is full.
	 *
	 * @param text the text holding the token
	 * @param start the first character of the token
	 * @param end the index after the last character of the token
	 * @return the id, or {@link #UNKNOWN}
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_NULL_ARGUMENT - if the text is null</li>
	 *    <li>ERROR_INVALID_RANGE - if the region is not inside the text</li>
	 * </ul>
	 */
	public int intern (CharSequence text, int start, int end) {
		checkRegion (text, start, end);
		int hash = hash (text, start, end);
		int slot = find (text, start, end, hash);
		if (table [slot] != UNKNOWN) return table [slot];
		if (size == capacity) return UNKNOWN;
		return add (text, start, end, hash, slot);
	}

	/**
	 * Returns the id of the token <code>text[start, end)</code>, or {@link #UNKNOWN} when it is
	 * absent; never adds a token.
	 *
	 * @param text the text holding the token
	 * @param start the first character of the token
	 * @param end the index after the last character of the token
	 * @return the id, or {@link #UNKNOWN}
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_NULL_ARGUMENT - if the text is null</li>
	 *    <li>ERROR_INVALID_RANGE - if the region is not inside the text</li>
	 * </ul>
	 */
	public int lookup (CharSequence text, int start, int end) {
		checkRegion (text, start, end);
		return table [find (text, start, end, hash (text, start, end))];
	}

	/**
	 * Returns the number of tokens.
	 *
	 * @return the size
	 */
	public int size () {
		return size;
	}

	/**
	 * Returns the maximum number of tokens.
	 *
	 * @return the capacity
	 */
	public int capacity () {
		return capacity;
	}

	/**
	 * Returns whether tokens that differ only in case share an id.
	 *
	 * @return <code>true</code> when case is folded
	 */
	public boolean foldsCase () {
		return foldCase;
	}

	/**
	 * Returns the text of a token as stored: folded when the vocabulary folds case.
	 *
	 * @param id the id, <code>1 &lt;= id &lt;= size()</code>
	 * @return the token text
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_INVALID_RANGE - if the id is not a token of this vocabulary</li>
	 * </ul>
	 */
	public String token (int id) {
		if (id < 1 || id > size) SWT.error (SWT.ERROR_INVALID_RANGE);
		return new String (pool, starts [id], ends [id] - starts [id]);
	}

	/* ------------------------------------------------------------------ lanes */

	private char fold (char c) {
		return foldCase ? Character.toLowerCase (Character.toUpperCase (c)) : c;
	}

	private int hash (CharSequence text, int start, int end) {
		int h = 0;
		for (int i = start; i < end; i++) h = 31 * h + fold (text.charAt (i));
		h ^= h >>> 16;
		h *= 0x85EBCA6B;
		return h ^ (h >>> 13);
	}

	/* The slot holding the token, or the empty slot where it would go. */
	private int find (CharSequence text, int start, int end, int hash) {
		int mask = table.length - 1;
		int slot = hash & mask;
		while (table [slot] != UNKNOWN && !matches (table [slot], text, start, end, hash)) slot = (slot + 1) & mask;
		return slot;
	}

	private boolean matches (int id, CharSequence text, int start, int end, int hash) {
		if (hashes [id] != hash || ends [id] - starts [id] != end - start) return false;
		int offset = starts [id] - start;
		for (int i = start; i < end; i++) {
			if (pool [offset + i] != fold (text.charAt (i))) return false;
		}
		return true;
	}

	private int add (CharSequence text, int start, int end, int hash, int slot) {
		int id = ++size;
		if (id == starts.length) growLanes ();
		int length = end - start;
		if (poolSize + length > pool.length) pool = Arrays.copyOf (pool, Math.max (pool.length * 2, poolSize + length));
		for (int i = 0; i < length; i++) pool [poolSize + i] = fold (text.charAt (start + i));
		starts [id] = poolSize;
		poolSize += length;
		ends [id] = poolSize;
		hashes [id] = hash;
		table [slot] = id;
		if (size * 2 > table.length) rehash ();
		return id;
	}

	private void growLanes () {
		starts = Arrays.copyOf (starts, starts.length * 2);
		ends = Arrays.copyOf (ends, ends.length * 2);
		hashes = Arrays.copyOf (hashes, hashes.length * 2);
	}

	private void rehash () {
		table = new int [table.length * 2];
		int mask = table.length - 1;
		for (int id = 1; id <= size; id++) {
			int slot = hashes [id] & mask;
			while (table [slot] != UNKNOWN) slot = (slot + 1) & mask;
			table [slot] = id;
		}
	}

	private static void checkRegion (CharSequence text, int start, int end) {
		if (text == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
		if (start < 0 || end < start || end > text.length ()) SWT.error (SWT.ERROR_INVALID_RANGE);
	}

	@Override
	public String toString () {
		return "M3TokenVocabulary [" + size + " of " + capacity + (foldCase ? ", folded]" : "]"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
	}
}
