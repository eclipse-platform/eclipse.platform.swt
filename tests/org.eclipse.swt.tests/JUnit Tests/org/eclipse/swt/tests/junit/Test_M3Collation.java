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
 * Contract of M3Collation and M3SortKeys (enhanced M3 language API, leaf 12). Headless. The
 * oracle is <code>java.text.Collator</code> of the same locale and strength.
 */
public class Test_M3Collation {

	private static int index (String [] items, String item) {
		return Arrays.asList (items).indexOf (item);
	}

	@Test
	public void test_signAgreesWithTheCollator () {
		String [] items = {"apple", "Äpfel", "zebra", "Zürich", "école", "ecole", "Ecole", "naïve", "naive", "", "a", "A", "ß", "ss", "é", "é"};
		for (Locale locale : new Locale [] {Locale.ENGLISH, Locale.GERMAN, Locale.forLanguageTag ("sv"), Locale.forLanguageTag ("tr"), Locale.FRENCH}) {
			for (int strength : new int [] {Collator.PRIMARY, Collator.SECONDARY, Collator.TERTIARY, Collator.IDENTICAL}) {
				Collator collator = Collator.getInstance (locale);
				collator.setStrength (strength);
				M3SortKeys keys = M3Collation.keys (items, locale, strength);
				assertEquals (items.length, keys.size ());
				for (int i = 0; i < items.length; i++) {
					for (int j = 0; j < items.length; j++) {
						assertEquals (Integer.signum (collator.compare (items [i], items [j])), Integer.signum (keys.compare (i, j)), locale + "/" + strength + ": " + items [i] + " vs " + items [j]);
					}
				}
			}
		}
	}

	@Test
	public void test_localeDifferencesAreVisible () {
		String [] items = {"ä", "b", "z", "ı", "i", "j"};
		M3SortKeys german = M3Collation.keys (items, Locale.GERMAN);
		assertTrue (german.compare (index (items, "ä"), index (items, "b")) < 0, "de: ä before b");
		M3SortKeys swedish = M3Collation.keys (items, Locale.forLanguageTag ("sv"));
		assertTrue (swedish.compare (index (items, "ä"), index (items, "z")) > 0, "sv: ä after z");
		M3SortKeys turkish = M3Collation.keys (items, Locale.forLanguageTag ("tr"));
		assertTrue (turkish.compare (index (items, "ı"), index (items, "i")) < 0, "tr: dotless i before i");
		assertTrue (turkish.compare (index (items, "i"), index (items, "j")) < 0);
	}

	@Test
	public void test_sortedOrderIsStableAndAscending () {
		Random random = new Random (3);
		String [] alphabet = {"a", "A", "ä", "b", "B", "ss", "ß", "z", "Z", "é", "e"};
		for (int round = 0; round < 50; round++) {
			String [] items = new String [random.nextInt (60)];
			for (int i = 0; i < items.length; i++) items [i] = alphabet [random.nextInt (alphabet.length)] + alphabet [random.nextInt (alphabet.length)];
			Locale locale = random.nextBoolean () ? Locale.GERMAN : Locale.forLanguageTag ("sv");
			int strength = random.nextBoolean () ? Collator.PRIMARY : Collator.TERTIARY;
			M3SortKeys keys = M3Collation.keys (items, locale, strength);
			int [] order = keys.sortedOrder ();
			/* oracle: a stable sort of boxed indices with the collator */
			Collator collator = Collator.getInstance (locale);
			collator.setStrength (strength);
			Integer [] expected = new Integer [items.length];
			for (int i = 0; i < items.length; i++) expected [i] = i;
			Arrays.sort (expected, (a, b) -> collator.compare (items [a], items [b]));
			assertArrayEquals (Arrays.stream (expected).mapToInt (Integer::intValue).toArray (), order, "round " + round);
		}
		assertArrayEquals (new int [0], M3Collation.keys (new String [0], Locale.ROOT).sortedOrder ());
		assertArrayEquals (new int [] {0}, M3Collation.keys (new String [] {"x"}, Locale.ROOT).sortedOrder ());
	}

	@Test
	public void test_keysAreCompactLanes () {
		String [] items = {"alpha", "beta", ""};
		M3SortKeys keys = M3Collation.keys (items, Locale.ENGLISH);
		assertTrue (keys.keyLength (0) > 0);
		assertTrue (keys.keyLength (2) >= 0);
		assertTrue (keys.toString ().startsWith ("M3SortKeys [3 items, "));
	}

	@Test
	public void test_argumentsAreChecked () {
		assertThrows (IllegalArgumentException.class, () -> M3Collation.keys (null, Locale.ROOT));
		assertThrows (IllegalArgumentException.class, () -> M3Collation.keys (new String [] {"a"}, null));
		assertThrows (IllegalArgumentException.class, () -> M3Collation.keys (new String [] {"a", null}, Locale.ROOT));
		assertThrows (IllegalArgumentException.class, () -> M3Collation.keys (new String [] {"a"}, Locale.ROOT, 99));
		M3SortKeys keys = M3Collation.keys (new String [] {"a", "b"}, Locale.ROOT);
		assertThrows (IllegalArgumentException.class, () -> keys.compare (-1, 0));
		assertThrows (IllegalArgumentException.class, () -> keys.compare (0, 2));
		assertThrows (IllegalArgumentException.class, () -> keys.keyLength (2));
	}
}
