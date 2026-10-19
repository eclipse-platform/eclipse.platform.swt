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

import org.eclipse.swt.*;

/**
 * Matching over token-id lanes (enhanced M3 language API).
 * <p>
 * A lane is an <code>int[]</code> of non-negative token ids, as produced by
 * {@link M3TextSegments#tokens(String, M3TokenVocabulary, boolean)}. Lanes have the shape a
 * token cursor reads, and presence masks use the cursor-facts rule: bit <code>id &amp; 63</code>
 * is set when the id may occur. A needle holding {@link M3TokenVocabulary#UNKNOWN} matches
 * nothing, so unknown words never match.
 * </p>
 *
 * @since 3.138
 */
public final class M3TokenMatch {

	private M3TokenMatch () {
	}

	/**
	 * Returns the presence mask of a lane: the OR of <code>1L &lt;&lt; (id &amp; 63)</code> over
	 * its ids.
	 *
	 * @param ids the lane
	 * @return the mask
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_NULL_ARGUMENT - if the lane is null</li>
	 * </ul>
	 */
	public static long presenceMask (int [] ids) {
		if (ids == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
		long mask = 0L;
		for (int id : ids) mask |= 1L << (id & 63);
		return mask;
	}

	/**
	 * Returns <code>false</code> only when the needle cannot occur in a haystack with the given
	 * presence mask (some needle id has its bit clear); never a false negative.
	 *
	 * @param haystackMask the presence mask of the haystack
	 * @param needle the ids to find
	 * @return whether the needle may occur
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_NULL_ARGUMENT - if the needle is null</li>
	 * </ul>
	 */
	public static boolean mayContain (long haystackMask, int [] needle) {
		return (presenceMask (needle) & ~haystackMask) == 0;
	}

	/**
	 * Returns the index of the first occurrence of the needle in the haystack at or after
	 * <code>from</code>, or -1. An empty needle matches at <code>min(max(from, 0), length)</code>,
	 * like <code>String.indexOf("")</code>; a needle holding {@link M3TokenVocabulary#UNKNOWN}
	 * matches nothing. Linear time (Knuth-Morris-Pratt).
	 *
	 * @param haystack the ids to search
	 * @param needle the ids to find
	 * @param from the first index to consider
	 * @return the index, or -1
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_NULL_ARGUMENT - if the haystack or the needle is null</li>
	 * </ul>
	 */
	public static int indexOf (int [] haystack, int [] needle, int from) {
		if (haystack == null || needle == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
		int start = Math.min (Math.max (from, 0), haystack.length);
		for (int id : needle) {
			if (id == M3TokenVocabulary.UNKNOWN) return -1;
		}
		if (needle.length == 0) return start;
		int [] failure = failure (needle);
		int matched = 0;
		for (int i = start; i < haystack.length; i++) {
			while (matched > 0 && haystack [i] != needle [matched]) matched = failure [matched - 1];
			if (haystack [i] == needle [matched]) matched++;
			if (matched == needle.length) return i - matched + 1;
		}
		return -1;
	}

	/* KMP failure table: failure[i] = length of the longest proper border of needle[0..i]. */
	private static int [] failure (int [] needle) {
		int [] failure = new int [needle.length];
		for (int i = 1, border = 0; i < needle.length; i++) {
			while (border > 0 && needle [i] != needle [border]) border = failure [border - 1];
			if (needle [i] == needle [border]) border++;
			failure [i] = border;
		}
		return failure;
	}
}
