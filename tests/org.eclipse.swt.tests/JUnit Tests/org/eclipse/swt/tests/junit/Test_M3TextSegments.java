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

import java.text.*;
import java.util.*;

import org.eclipse.swt.m3.*;
import org.junit.jupiter.api.*;

/**
 * Contract of M3TextSegments (enhanced M3 language API, leaf 3). Headless; the oracle is the
 * JDK <code>BreakIterator</code> of the same kind and locale.
 */
public class Test_M3TextSegments {

	/* oracle: the boundary sequence of a JDK iterator, 0 first */
	private static int [] oracle (BreakIterator iterator, String text) {
		iterator.setText (text);
		java.util.List<Integer> list = new ArrayList<> ();
		list.add (0);
		for (int b = iterator.next (); b != BreakIterator.DONE; b = iterator.next ()) list.add (b);
		return list.stream ().mapToInt (Integer::intValue).toArray ();
	}

	private static void assertPartition (M3TextSegments segments, String text) {
		assertEquals (text.length (), segments.length ());
		if (segments.count () == 0) return;
		assertEquals (0, segments.start (0));
		assertEquals (text.length (), segments.end (segments.count () - 1));
		for (int i = 1; i < segments.count (); i++) {
			assertEquals (segments.end (i - 1), segments.start (i));
			assertTrue (segments.start (i) < segments.end (i));
		}
	}

	@Test
	public void test_wordsMatchTheJdkIteratorAndFlagWords () {
		String text = "Hello, wide world! 42 is 'fine'.";
		M3TextSegments words = M3TextSegments.words (text, Locale.ENGLISH);
		assertEquals (M3TextSegments.WORD, words.kind ());
		assertArrayEquals (oracle (BreakIterator.getWordInstance (Locale.ENGLISH), text), words.boundaries ());
		assertPartition (words, text);
		int flagged = 0;
		for (int i = 0; i < words.count (); i++) {
			String segment = text.substring (words.start (i), words.end (i));
			boolean expected = segment.codePoints ().anyMatch (Character::isLetterOrDigit);
			assertEquals (expected, words.isWord (i), segment);
			if (expected) flagged++;
		}
		assertEquals (6, flagged);
		assertEquals ("Hello", text.substring (words.start (0), words.end (0)));
		assertTrue (words.isWord (0));
		assertFalse (words.isWord (1));
	}

	@Test
	public void test_graphemesKeepCombiningMarksTogether () {
		String text = "éa😀";
		M3TextSegments graphemes = M3TextSegments.graphemes (text, Locale.ROOT);
		assertEquals (M3TextSegments.GRAPHEME, graphemes.kind ());
		assertArrayEquals (oracle (BreakIterator.getCharacterInstance (Locale.ROOT), text), graphemes.boundaries ());
		assertPartition (graphemes, text);
		assertEquals (3, graphemes.count ());
		assertEquals (2, graphemes.end (0));
		assertEquals (3, graphemes.end (1));
		assertEquals (5, graphemes.end (2));
		assertFalse (graphemes.isWord (0));
	}

	@Test
	public void test_lineBreaksAndSentencesMatchTheJdkIterator () {
		String text = "One two-three. Four five? Six!";
		M3TextSegments lines = M3TextSegments.lineBreaks (text, Locale.ENGLISH);
		assertEquals (M3TextSegments.LINE, lines.kind ());
		assertArrayEquals (oracle (BreakIterator.getLineInstance (Locale.ENGLISH), text), lines.boundaries ());
		assertPartition (lines, text);
		M3TextSegments sentences = M3TextSegments.sentences (text, Locale.ENGLISH);
		assertEquals (M3TextSegments.SENTENCE, sentences.kind ());
		assertArrayEquals (oracle (BreakIterator.getSentenceInstance (Locale.ENGLISH), text), sentences.boundaries ());
		assertPartition (sentences, text);
		assertEquals (3, sentences.count ());
		assertEquals ("Four five? ", text.substring (sentences.start (1), sentences.end (1)));
	}

	@Test
	public void test_localeIsPassedToTheIterator () {
		String thai = "สวัสดีครับ ไทย";
		for (Locale locale : new Locale [] {Locale.forLanguageTag ("th"), Locale.ENGLISH, Locale.JAPANESE}) {
			M3TextSegments words = M3TextSegments.words (thai, locale);
			assertArrayEquals (oracle (BreakIterator.getWordInstance (locale), thai), words.boundaries (), locale.toString ());
			assertPartition (words, thai);
		}
	}

	@Test
	public void test_segmentAtMapsOffsetsToSegments () {
		String text = "ab cd";
		M3TextSegments words = M3TextSegments.words (text, Locale.ENGLISH);
		assertEquals (3, words.count ());
		for (int i = 0; i < words.count (); i++) {
			for (int offset = words.start (i); offset < words.end (i); offset++) assertEquals (i, words.segmentAt (offset), "offset " + offset);
		}
		assertEquals (-1, words.segmentAt (-1));
		assertEquals (-1, words.segmentAt (text.length ()));
		assertEquals (-1, words.segmentAt (99));
	}

	@Test
	public void test_emptyText () {
		M3TextSegments none = M3TextSegments.words ("", Locale.ENGLISH);
		assertEquals (0, none.count ());
		assertEquals (0, none.length ());
		assertArrayEquals (new int [] {0}, none.boundaries ());
		assertEquals (-1, none.segmentAt (0));
		assertThrows (IllegalArgumentException.class, () -> none.start (0));
		assertEquals (0, M3TextSegments.graphemes ("", Locale.ROOT).count ());
	}

	@Test
	public void test_boundariesAreACopyAndLanesAreBounded () {
		M3TextSegments words = M3TextSegments.words ("a b", Locale.ENGLISH);
		int [] copy = words.boundaries ();
		copy [0] = 99;
		assertEquals (0, words.start (0));
		/* more than 64 segments exercise the second word-bit word and the growing boundary lane */
		StringBuilder many = new StringBuilder ();
		for (int i = 0; i < 70; i++) many.append ("w").append (i).append (' ');
		M3TextSegments long_ = M3TextSegments.words (many.toString (), Locale.ENGLISH);
		assertEquals (140, long_.count ());
		assertTrue (long_.isWord (138));
		assertFalse (long_.isWord (139));
		assertEquals (139, long_.segmentAt (long_.length () - 1));
		assertTrue (long_.toString ().startsWith ("M3TextSegments [kind 0, 140 segments"));
	}

	@Test
	public void test_argumentsAreChecked () {
		assertThrows (IllegalArgumentException.class, () -> M3TextSegments.words (null, Locale.ENGLISH));
		assertThrows (IllegalArgumentException.class, () -> M3TextSegments.words ("a", null));
		assertThrows (IllegalArgumentException.class, () -> M3TextSegments.of (4, "a", Locale.ENGLISH));
		assertThrows (IllegalArgumentException.class, () -> M3TextSegments.of (-1, "a", Locale.ENGLISH));
		M3TextSegments words = M3TextSegments.words ("a b", Locale.ENGLISH);
		assertThrows (IllegalArgumentException.class, () -> words.start (-1));
		assertThrows (IllegalArgumentException.class, () -> words.end (3));
		assertThrows (IllegalArgumentException.class, () -> words.isWord (3));
		assertEquals (M3TextSegments.SENTENCE, M3TextSegments.of (M3TextSegments.SENTENCE, "a", Locale.ENGLISH).kind ());
	}
}
