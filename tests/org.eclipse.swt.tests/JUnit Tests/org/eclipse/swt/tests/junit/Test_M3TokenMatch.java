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
package org.eclipse.swt.tests.junit;

import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

import org.eclipse.swt.m3.*;
import org.junit.jupiter.api.*;

/**
 * Contract of M3TokenVocabulary, M3TokenMatch and M3TextSegments.tokens (enhanced M3 language
 * API, leaf 4). Headless. Oracles: a HashMap over folded substrings for the vocabulary, a naive
 * search for KMP, and matching over the folded word strings for matching over ids.
 */
public class Test_M3TokenMatch {

	/* ------------------------------------------------------------------ vocabulary */

	@Test
	public void test_vocabularyAssignsDenseStableIds () {
		M3TokenVocabulary vocabulary = new M3TokenVocabulary (100, false);
		String text = "alpha beta alpha gamma";
		assertEquals (1, vocabulary.intern (text, 0, 5));
		assertEquals (2, vocabulary.intern (text, 6, 10));
		assertEquals (1, vocabulary.intern (text, 11, 16));
		assertEquals (3, vocabulary.intern (text, 17, 22));
		assertEquals (3, vocabulary.size ());
		assertEquals (100, vocabulary.capacity ());
		assertFalse (vocabulary.foldsCase ());
		assertEquals (2, vocabulary.lookup ("beta", 0, 4));
		assertEquals (M3TokenVocabulary.UNKNOWN, vocabulary.lookup ("delta", 0, 5));
		assertEquals (M3TokenVocabulary.UNKNOWN, vocabulary.lookup ("Alpha", 0, 5));
		assertEquals (3, vocabulary.size ());
		assertEquals ("alpha", vocabulary.token (1));
		assertEquals ("gamma", vocabulary.token (3));
		assertEquals ("M3TokenVocabulary [3 of 100]", vocabulary.toString ());
	}

	@Test
	public void test_vocabularyFoldsCaseWhenAsked () {
		M3TokenVocabulary vocabulary = new M3TokenVocabulary (10, true);
		assertTrue (vocabulary.foldsCase ());
		int id = vocabulary.intern ("Straße", 0, 6);
		assertEquals (id, vocabulary.lookup ("STRAßE", 0, 6));
		assertEquals (id, vocabulary.intern ("straße", 0, 6));
		int sigma = vocabulary.intern ("ς", 0, 1);
		assertEquals (sigma, vocabulary.lookup ("Σ", 0, 1));
		assertEquals (sigma, vocabulary.lookup ("σ", 0, 1));
		assertEquals ("straße", vocabulary.token (id));
		assertEquals ("M3TokenVocabulary [2 of 10, folded]", vocabulary.toString ());
	}

	@Test
	public void test_vocabularyIsBoundedAndAgreesWithAMapOracle () {
		M3TokenVocabulary vocabulary = new M3TokenVocabulary (500, true);
		Map<String, Integer> oracle = new HashMap<> ();
		Random random = new Random (17);
		StringBuilder text = new StringBuilder ();
		for (int i = 0; i < 4000; i++) {
			int start = text.length ();
			text.append ("w").append (random.nextInt (800)).append (random.nextBoolean () ? "x" : "X");
			String key = text.substring (start).toLowerCase (Locale.ROOT);
			int id = vocabulary.intern (text, start, text.length ());
			Integer known = oracle.get (key);
			if (known != null) {
				assertEquals (known.intValue (), id, key);
			} else if (oracle.size () < 500) {
				assertEquals (oracle.size () + 1, id, key);
				oracle.put (key, id);
			} else {
				assertEquals (M3TokenVocabulary.UNKNOWN, id, key);
			}
		}
		assertEquals (500, vocabulary.size ());
		oracle.forEach ((key, id) -> {
			assertEquals (id.intValue (), vocabulary.lookup (key, 0, key.length ()));
			assertEquals (key, vocabulary.token (id));
		});
	}

	@Test
	public void test_vocabularyResolvesHashCollisions () {
		M3TokenVocabulary vocabulary = new M3TokenVocabulary (10, false);
		/* same 31-polynomial and length ("Aa" and "BB" share String.hashCode) */
		assertEquals ("Aa".hashCode (), "BB".hashCode ());
		assertEquals (1, vocabulary.intern ("Aa", 0, 2));
		assertEquals (2, vocabulary.intern ("BB", 0, 2));
		assertEquals (1, vocabulary.lookup ("Aa", 0, 2));
		assertEquals (2, vocabulary.lookup ("BB", 0, 2));
		/* same 31-polynomial, different length (a leading NUL adds 0) */
		assertEquals ("a".hashCode (), "\u0000a".hashCode ());
		assertEquals (3, vocabulary.intern ("a", 0, 1));
		assertEquals (M3TokenVocabulary.UNKNOWN, vocabulary.lookup ("\u0000a", 0, 2));
		assertEquals (4, vocabulary.intern ("\u0000a", 0, 2));
		assertEquals (3, vocabulary.lookup ("a", 0, 1));
	}

	@Test
	public void test_vocabularyArgumentsAreChecked () {
		assertThrows (IllegalArgumentException.class, () -> new M3TokenVocabulary (0, false));
		M3TokenVocabulary vocabulary = new M3TokenVocabulary (2, false);
		assertEquals (1, vocabulary.intern ("", 0, 0));
		assertEquals (1, vocabulary.lookup ("abc", 1, 1));
		assertThrows (IllegalArgumentException.class, () -> vocabulary.intern (null, 0, 0));
		assertThrows (IllegalArgumentException.class, () -> vocabulary.lookup ("abc", -1, 2));
		assertThrows (IllegalArgumentException.class, () -> vocabulary.lookup ("abc", 2, 1));
		assertThrows (IllegalArgumentException.class, () -> vocabulary.intern ("abc", 0, 4));
		assertThrows (IllegalArgumentException.class, () -> vocabulary.token (0));
		assertThrows (IllegalArgumentException.class, () -> vocabulary.token (2));
	}

	/* ------------------------------------------------------------------ matching */

	@Test
	public void test_indexOfFindsTheFirstOccurrence () {
		int [] haystack = {4, 1, 2, 1, 2, 3, 1, 2, 3};
		assertEquals (3, M3TokenMatch.indexOf (haystack, new int [] {1, 2, 3}, 0));
		assertEquals (6, M3TokenMatch.indexOf (haystack, new int [] {1, 2, 3}, 4));
		assertEquals (-1, M3TokenMatch.indexOf (haystack, new int [] {1, 2, 3}, 7));
		assertEquals (0, M3TokenMatch.indexOf (haystack, new int [] {4}, -5));
		assertEquals (-1, M3TokenMatch.indexOf (haystack, new int [] {9}, 0));
		assertEquals (-1, M3TokenMatch.indexOf (new int [] {1, 2}, new int [] {1, 2, 3}, 0));
		assertEquals (2, M3TokenMatch.indexOf (haystack, new int [0], 2));
		assertEquals (haystack.length, M3TokenMatch.indexOf (haystack, new int [0], 99));
		assertEquals (0, M3TokenMatch.indexOf (haystack, new int [0], -1));
	}

	@Test
	public void test_unknownMatchesNothing () {
		int [] haystack = {M3TokenVocabulary.UNKNOWN, 1, M3TokenVocabulary.UNKNOWN};
		assertEquals (-1, M3TokenMatch.indexOf (haystack, new int [] {M3TokenVocabulary.UNKNOWN}, 0));
		assertEquals (-1, M3TokenMatch.indexOf (haystack, new int [] {1, M3TokenVocabulary.UNKNOWN}, 0));
		assertEquals (1, M3TokenMatch.indexOf (haystack, new int [] {1}, 0));
	}

	@Test
	public void test_indexOfAgreesWithANaiveSearch () {
		Random random = new Random (42);
		for (int round = 0; round < 2000; round++) {
			int [] haystack = random.ints (random.nextInt (40), 1, 4).toArray ();
			int [] needle = random.ints (1 + random.nextInt (5), 1, 4).toArray ();
			int from = random.nextInt (45) - 2;
			assertEquals (naive (haystack, needle, from), M3TokenMatch.indexOf (haystack, needle, from), Arrays.toString (haystack) + " / " + Arrays.toString (needle) + " from " + from);
		}
		/* periodic needle: the failure table falls back through several borders */
		int [] periodic = {1, 1, 1, 1, 2};
		assertEquals (5, M3TokenMatch.indexOf (new int [] {1, 1, 1, 1, 1, 1, 1, 1, 1, 2}, periodic, 0));
		assertEquals (2, M3TokenMatch.indexOf (new int [] {1, 2, 1, 2, 1, 2, 3}, new int [] {1, 2, 1, 2, 3}, 0));
	}

	private static int naive (int [] haystack, int [] needle, int from) {
		for (int i = Math.max (from, 0); i + needle.length <= haystack.length; i++) {
			if (Arrays.equals (haystack, i, i + needle.length, needle, 0, needle.length)) return i;
		}
		return -1;
	}

	@Test
	public void test_presenceMaskUsesTheCursorFactsRule () {
		assertEquals (0L, M3TokenMatch.presenceMask (new int [0]));
		assertEquals ((1L << 1) | (1L << 5) | 1L, M3TokenMatch.presenceMask (new int [] {1, 5, 64}));
		long mask = M3TokenMatch.presenceMask (new int [] {1, 2, 3});
		assertTrue (M3TokenMatch.mayContain (mask, new int [] {3, 1}));
		assertTrue (M3TokenMatch.mayContain (mask, new int [] {65}));
		assertFalse (M3TokenMatch.mayContain (mask, new int [] {1, 7}));
		assertTrue (M3TokenMatch.mayContain (0L, new int [0]));
		assertThrows (IllegalArgumentException.class, () -> M3TokenMatch.presenceMask (null));
		assertThrows (IllegalArgumentException.class, () -> M3TokenMatch.mayContain (0L, null));
		assertThrows (IllegalArgumentException.class, () -> M3TokenMatch.indexOf (null, new int [0], 0));
		assertThrows (IllegalArgumentException.class, () -> M3TokenMatch.indexOf (new int [0], null, 0));
	}

	/* ------------------------------------------------------------------ tokens of segments */

	@Test
	public void test_tokensOfWordSegmentsMatchLikeTheWords () {
		M3TokenVocabulary vocabulary = new M3TokenVocabulary (1000, true);
		String [] items = {"Open the File menu", "open file", "Save the file as...", "the FILE menu, open"};
		int [][] lanes = new int [items.length][];
		for (int i = 0; i < items.length; i++) {
			lanes [i] = M3TextSegments.words (items [i], Locale.ENGLISH).tokens (items [i], vocabulary, true);
		}
		assertArrayEquals (new int [] {1, 2, 3, 4}, lanes [0]);
		assertArrayEquals (new int [] {1, 3}, lanes [1]);
		String query = "file MENU";
		int [] needle = M3TextSegments.words (query, Locale.ENGLISH).tokens (query, vocabulary, false);
		assertArrayEquals (new int [] {3, 4}, needle);
		for (int i = 0; i < items.length; i++) {
			int expected = Collections.indexOfSubList (folded (items [i]), folded (query));
			assertEquals (expected, M3TokenMatch.indexOf (lanes [i], needle, 0), items [i]);
		}
		String absent = "close file";
		int [] unknown = M3TextSegments.words (absent, Locale.ENGLISH).tokens (absent, vocabulary, false);
		assertEquals (M3TokenVocabulary.UNKNOWN, unknown [0]);
		assertEquals (-1, M3TokenMatch.indexOf (lanes [1], unknown, 0));
		assertEquals (6, vocabulary.size ());
	}

	/* oracle: the folded word strings of a text, by the JDK word iterator */
	private static java.util.List<String> folded (String text) {
		M3TextSegments words = M3TextSegments.words (text, Locale.ENGLISH);
		java.util.List<String> list = new ArrayList<> ();
		for (int i = 0; i < words.count (); i++) {
			if (words.isWord (i)) list.add (text.substring (words.start (i), words.end (i)).toLowerCase (Locale.ROOT));
		}
		return list;
	}

	@Test
	public void test_tokensOfOtherKindsAndErrors () {
		M3TokenVocabulary vocabulary = new M3TokenVocabulary (4, false);
		String text = "one two. three";
		assertEquals (0, M3TextSegments.sentences (text, Locale.ENGLISH).tokens (text, vocabulary, true).length);
		assertEquals (0, M3TextSegments.words ("", Locale.ENGLISH).tokens ("", vocabulary, true).length);
		assertEquals (0, M3TextSegments.words (" , ", Locale.ENGLISH).tokens (" , ", vocabulary, true).length);
		M3TextSegments words = M3TextSegments.words (text, Locale.ENGLISH);
		assertThrows (IllegalArgumentException.class, () -> words.tokens (null, vocabulary, true));
		assertThrows (IllegalArgumentException.class, () -> words.tokens (text, null, true));
		assertThrows (IllegalArgumentException.class, () -> words.tokens ("other", vocabulary, true));
		/* a full vocabulary answers UNKNOWN for new words */
		String many = "a b c d e f";
		int [] ids = M3TextSegments.words (many, Locale.ENGLISH).tokens (many, vocabulary, true);
		assertArrayEquals (new int [] {1, 2, 3, 4, 0, 0}, ids);
	}
}
