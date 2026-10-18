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

import java.text.*;
import java.util.*;

import org.eclipse.swt.*;

/**
 * Locale-aware segmentation facts of a text: words, graphemes (user-perceived characters),
 * line-break opportunities or sentences (enhanced M3 language API, first seed).
 * <p>
 * The segments partition the text: segment <code>i</code> covers
 * <code>[start(i), end(i))</code> in <code>char</code> offsets, <code>start(0) == 0</code>,
 * <code>end(count - 1) == length</code> and <code>end(i) == start(i + 1)</code>. For the
 * {@link #WORD} kind, {@link #isWord(int)} tells word segments (those holding a letter or a
 * digit) from the spaces and punctuation between them.
 * </p>
 * <p>
 * The facts are computed once from the JDK's <code>java.text.BreakIterator</code> for the
 * locale and kept as an <code>int</code> boundary lane and a word bit lane; no substring is
 * created. The surface is independent of the engine, so ICU4J (UAX #29 extended grapheme
 * clusters, rule status) or Hugging Face word lists can answer the same questions later.
 * </p>
 *
 * @since 3.138
 */
public final class M3TextSegments {

	/** Segment kind: words and the spaces and punctuation between them. */
	public static final int WORD = 0;
	/** Segment kind: user-perceived characters (a base character with its combining marks). */
	public static final int GRAPHEME = 1;
	/** Segment kind: the pieces between line-break opportunities. */
	public static final int LINE = 2;
	/** Segment kind: sentences. */
	public static final int SENTENCE = 3;

	private static final int [] NO_BOUNDARIES = {0};

	private final int kind;
	/* boundaries [0] == 0, boundaries [count] == length */
	private final int [] boundaries;
	/* bit i set when segment i holds a letter or a digit; null unless WORD */
	private final long [] wordBits;

	private M3TextSegments (int kind, int [] boundaries, long [] wordBits) {
		this.kind = kind;
		this.boundaries = boundaries;
		this.wordBits = wordBits;
	}

	/**
	 * Segments the text into words and the spaces and punctuation between them.
	 *
	 * @param text the text
	 * @param locale the locale whose rules apply
	 * @return the segments
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_NULL_ARGUMENT - if the text or the locale is null</li>
	 * </ul>
	 */
	public static M3TextSegments words (String text, Locale locale) {
		return of (WORD, text, locale);
	}

	/**
	 * Segments the text into user-perceived characters.
	 *
	 * @param text the text
	 * @param locale the locale whose rules apply
	 * @return the segments
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_NULL_ARGUMENT - if the text or the locale is null</li>
	 * </ul>
	 */
	public static M3TextSegments graphemes (String text, Locale locale) {
		return of (GRAPHEME, text, locale);
	}

	/**
	 * Segments the text at its line-break opportunities.
	 *
	 * @param text the text
	 * @param locale the locale whose rules apply
	 * @return the segments
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_NULL_ARGUMENT - if the text or the locale is null</li>
	 * </ul>
	 */
	public static M3TextSegments lineBreaks (String text, Locale locale) {
		return of (LINE, text, locale);
	}

	/**
	 * Segments the text into sentences.
	 *
	 * @param text the text
	 * @param locale the locale whose rules apply
	 * @return the segments
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_NULL_ARGUMENT - if the text or the locale is null</li>
	 * </ul>
	 */
	public static M3TextSegments sentences (String text, Locale locale) {
		return of (SENTENCE, text, locale);
	}

	/**
	 * Segments the text by the given kind.
	 *
	 * @param kind one of {@link #WORD}, {@link #GRAPHEME}, {@link #LINE}, {@link #SENTENCE}
	 * @param text the text
	 * @param locale the locale whose rules apply
	 * @return the segments
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_NULL_ARGUMENT - if the text or the locale is null</li>
	 *    <li>ERROR_INVALID_ARGUMENT - if the kind is not one of the four</li>
	 * </ul>
	 */
	public static M3TextSegments of (int kind, String text, Locale locale) {
		if (text == null || locale == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
		if (kind < WORD || kind > SENTENCE) SWT.error (SWT.ERROR_INVALID_ARGUMENT);
		if (text.isEmpty ()) return new M3TextSegments (kind, NO_BOUNDARIES, null);
		int [] boundaries = boundaries (iterator (kind, locale), text);
		return new M3TextSegments (kind, boundaries, kind == WORD ? wordBits (text, boundaries) : null);
	}

	/**
	 * Returns the kind of the segments.
	 *
	 * @return one of {@link #WORD}, {@link #GRAPHEME}, {@link #LINE}, {@link #SENTENCE}
	 */
	public int kind () {
		return kind;
	}

	/**
	 * Returns the number of segments; 0 for an empty text.
	 *
	 * @return the count
	 */
	public int count () {
		return boundaries.length - 1;
	}

	/**
	 * Returns the length of the segmented text in <code>char</code>s.
	 *
	 * @return the length
	 */
	public int length () {
		return boundaries [boundaries.length - 1];
	}

	/**
	 * Returns the offset of the first <code>char</code> of a segment.
	 *
	 * @param index the segment, <code>0 &lt;= index &lt; count()</code>
	 * @return the start offset
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_INVALID_RANGE - if the index is not a segment</li>
	 * </ul>
	 */
	public int start (int index) {
		checkIndex (index);
		return boundaries [index];
	}

	/**
	 * Returns the offset after the last <code>char</code> of a segment.
	 *
	 * @param index the segment, <code>0 &lt;= index &lt; count()</code>
	 * @return the end offset
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_INVALID_RANGE - if the index is not a segment</li>
	 * </ul>
	 */
	public int end (int index) {
		checkIndex (index);
		return boundaries [index + 1];
	}

	/**
	 * Returns whether a segment of the {@link #WORD} kind holds a letter or a digit; always
	 * <code>false</code> for the other kinds.
	 *
	 * @param index the segment, <code>0 &lt;= index &lt; count()</code>
	 * @return <code>true</code> for a word
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_INVALID_RANGE - if the index is not a segment</li>
	 * </ul>
	 */
	public boolean isWord (int index) {
		checkIndex (index);
		return wordBits != null && (wordBits [index >>> 6] & (1L << index)) != 0;
	}

	/**
	 * Returns the segment holding a <code>char</code> offset, or -1 when the offset lies
	 * outside the text.
	 *
	 * @param offset the offset
	 * @return the segment index, or -1
	 */
	public int segmentAt (int offset) {
		if (offset < 0 || offset >= length ()) return -1;
		int found = Arrays.binarySearch (boundaries, 0, boundaries.length - 1, offset);
		return found >= 0 ? found : -found - 2;
	}

	/**
	 * Returns a copy of the boundary offsets: <code>count() + 1</code> ascending offsets from
	 * 0 to the length (<code>{0}</code> for an empty text).
	 *
	 * @return the boundaries
	 */
	public int [] boundaries () {
		return boundaries.clone ();
	}

	/**
	 * Returns the token ids of the word segments in order (see {@link #isWord(int)}): a lane of
	 * non-negative ids that a token cursor reads and {@link M3TokenMatch} searches. With
	 * <code>add</code>, absent words are interned while the vocabulary has room; otherwise, and
	 * when it is full, they are {@link M3TokenVocabulary#UNKNOWN}. Kinds other than
	 * {@link #WORD} have no word segments, so their lane is empty. No substring is created.
	 *
	 * @param text the text these segments were computed from
	 * @param vocabulary the vocabulary that assigns the ids
	 * @param add whether absent words are interned
	 * @return the ids, one per word segment
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_NULL_ARGUMENT - if the text or the vocabulary is null</li>
	 *    <li>ERROR_INVALID_ARGUMENT - if the text length differs from the segmented length</li>
	 * </ul>
	 */
	public int [] tokens (String text, M3TokenVocabulary vocabulary, boolean add) {
		if (text == null || vocabulary == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
		if (text.length () != length ()) SWT.error (SWT.ERROR_INVALID_ARGUMENT);
		int [] ids = new int [wordCount ()];
		for (int i = 0, word = 0; word < ids.length; i++) {
			if (!isWord (i)) continue;
			int start = boundaries [i], end = boundaries [i + 1];
			ids [word++] = add ? vocabulary.intern (text, start, end) : vocabulary.lookup (text, start, end);
		}
		return ids;
	}

	/* Number of word segments: the population count of the word bit lane. */
	private int wordCount () {
		int count = 0;
		if (wordBits != null) {
			for (long bits : wordBits) count += Long.bitCount (bits);
		}
		return count;
	}

	/* ------------------------------------------------------------------ lanes */

	private static BreakIterator iterator (int kind, Locale locale) {
		switch (kind) {
			case GRAPHEME: return BreakIterator.getCharacterInstance (locale);
			case LINE: return BreakIterator.getLineInstance (locale);
			case SENTENCE: return BreakIterator.getSentenceInstance (locale);
			default: return BreakIterator.getWordInstance (locale);
		}
	}

	/* The iterator's boundaries, 0 first and the length last, in one trimmed int lane. */
	private static int [] boundaries (BreakIterator iterator, String text) {
		iterator.setText (text);
		int [] lane = new int [16];
		int count = 1;
		for (int boundary = iterator.next (); boundary != BreakIterator.DONE; boundary = iterator.next ()) {
			if (count == lane.length) lane = Arrays.copyOf (lane, count * 2);
			lane [count++] = boundary;
		}
		return Arrays.copyOf (lane, count);
	}

	/* Bit i set when segment i holds a letter or a digit. */
	private static long [] wordBits (String text, int [] boundaries) {
		int count = boundaries.length - 1;
		long [] bits = new long [(count + 63) >>> 6];
		for (int i = 0; i < count; i++) {
			if (holdsLetterOrDigit (text, boundaries [i], boundaries [i + 1])) bits [i >>> 6] |= 1L << i;
		}
		return bits;
	}

	private static boolean holdsLetterOrDigit (String text, int start, int end) {
		for (int offset = start; offset < end;) {
			int codePoint = text.codePointAt (offset);
			if (Character.isLetterOrDigit (codePoint)) return true;
			offset += Character.charCount (codePoint);
		}
		return false;
	}

	private void checkIndex (int index) {
		if (index < 0 || index >= count ()) SWT.error (SWT.ERROR_INVALID_RANGE);
	}

	@Override
	public String toString () {
		return "M3TextSegments [kind " + kind + ", " + count () + " segments, length " + length () + "]"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
	}
}
