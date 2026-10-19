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

import static org.eclipse.swt.m3.M3TextMatch.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;
import java.util.regex.*;

import org.eclipse.swt.m3.*;
import org.junit.jupiter.api.*;

/** Headless tests of {@link M3TextMatch}: modes, flags, tokens, wildcard and regular-expression semantics. */
@SuppressWarnings ("nls")
public class Test_M3TextMatch {

	static boolean m (String pattern, int mode, String text) {
		return M3TextMatch.compile (pattern, mode).matches (text);
	}

	@Test
	public void test_containsStartsWithExact () {
		assertTrue (m ("b", CONTAINS, "abc"));
		assertFalse (m ("B", CONTAINS, "abc"));
		assertTrue (m ("B", CONTAINS | IGNORE_CASE, "aBc"));
		assertTrue (m ("C", CONTAINS | IGNORE_CASE, "abc"));
		assertFalse (m ("abcd", CONTAINS | IGNORE_CASE, "abc"));
		assertTrue (m ("ab", STARTS_WITH, "abc"));
		assertFalse (m ("bc", STARTS_WITH, "abc"));
		assertTrue (m ("AB", STARTS_WITH | IGNORE_CASE, "abc"));
		assertTrue (m ("abc", EXACT, "abc"));
		assertFalse (m ("ab", EXACT, "abc"));
		assertTrue (m ("ABC", EXACT | IGNORE_CASE, "abc"));
		assertFalse (m ("x", CONTAINS, null));
		assertTrue (m ("x", CONTAINS | NEGATE, null));
	}

	@Test
	public void test_wildcardMatchesTheWholeText () {
		assertTrue (m ("a*c", WILDCARD, "abc"));
		assertTrue (m ("a*c", WILDCARD, "ac"));
		assertFalse (m ("a*c", WILDCARD, "abd"));
		assertFalse (m ("a*c", WILDCARD, "xabc"));
		assertTrue (m ("a?c", WILDCARD, "abc"));
		assertFalse (m ("a?c", WILDCARD, "ac"));
		assertTrue (m ("*", WILDCARD, ""));
		assertTrue (m ("*", WILDCARD, "anything"));
		assertTrue (m ("**a**", WILDCARD, "xxaxx"));
		assertTrue (m ("*a*b", WILDCARD, "xaxaxb"));
		assertFalse (m ("*a*b", WILDCARD, "xaxaxbc"));
		assertTrue (m ("a\\*c", WILDCARD, "a*c"));
		assertFalse (m ("a\\*c", WILDCARD, "abc"));
		assertTrue (m ("a\\?", WILDCARD, "a?"));
		assertFalse (m ("a\\?", WILDCARD, "ab"));
		assertTrue (m ("a\\\\", WILDCARD, "a\\"));
		assertTrue (m ("a\\b", WILDCARD, "a\\b"));
		assertTrue (m ("A*C", WILDCARD | IGNORE_CASE, "abc"));
		assertFalse (m ("A*C", WILDCARD, "abc"));
		assertTrue (m ("\u0130*", WILDCARD | IGNORE_CASE, "i"));
		assertTrue (m ("a\\", WILDCARD, "a\\"));
	}

	@Test
	public void test_regexMatchesTheWholeText () {
		assertFalse (m ("b", REGEX, "abc"));
		assertTrue (m (".*b.*", REGEX, "abc"));
		assertTrue (m ("A.C", REGEX | IGNORE_CASE, "abc"));
		assertFalse (m ("A.C", REGEX, "abc"));
		assertTrue (m ("a{1\\,3}", REGEX, "aaa"));
		assertFalse (m ("a{1\\,3}", REGEX, "aaaa"));
		assertTrue (m ("x,a+", REGEX, "aa"));
		assertFalse (m ("x,a+", REGEX | NEGATE, "aa"));
	}

	@Test
	public void test_tokensAndNegation () {
		assertArrayEquals (new String [] {"a", "b"}, tokens ("a,b"));
		assertArrayEquals (new String [] {"a,b"}, tokens ("a\\,b"));
		assertArrayEquals (new String [] {"a"}, tokens (",,a,"));
		assertArrayEquals (new String [] {"a\\x"}, tokens ("a\\x"));
		assertArrayEquals (new String [] {"a\\"}, tokens ("a\\"));
		assertArrayEquals (new String [0], tokens (""));
		assertTrue (m ("x,b", CONTAINS, "abc"));
		assertFalse (m ("x,b", CONTAINS | NEGATE, "abc"));
		assertTrue (m ("x,y", CONTAINS | NEGATE, "abc"));
		M3TextMatch none = M3TextMatch.compile (",", CONTAINS | NEGATE);
		assertFalse (none.isActive ());
		assertTrue (none.matches ("abc"));
		M3TextMatch match = M3TextMatch.compile ("a,b", WILDCARD | IGNORE_CASE);
		assertEquals ("a,b", match.pattern ());
		assertEquals (WILDCARD | IGNORE_CASE, match.mode ());
		assertTrue (match.isActive ());
		assertTrue (match.toString ().contains ("a,b"));
	}

	@Test
	public void test_checkAndErrors () {
		assertEquals ("", check (".*", REGEX));
		assertEquals ("", check ("(", WILDCARD));
		String problem = check ("ok,(", REGEX);
		assertFalse (problem.isEmpty ());
		try {
			Pattern.compile ("(");
			fail ();
		} catch (PatternSyntaxException e) {
			assertEquals (e.getMessage (), problem);
		}
		IllegalArgumentException e = assertThrows (IllegalArgumentException.class, () -> M3TextMatch.compile ("(", REGEX));
		assertTrue (e.getMessage ().contains ("Unclosed group"), e.getMessage ());
		assertThrows (IllegalArgumentException.class, () -> M3TextMatch.compile (null, CONTAINS));
		assertThrows (IllegalArgumentException.class, () -> M3TextMatch.compile ("x", 5));
		assertThrows (IllegalArgumentException.class, () -> M3TextMatch.compile ("x", 1 << 12));
		assertThrows (IllegalArgumentException.class, () -> check (null, CONTAINS));
		assertThrows (IllegalArgumentException.class, () -> check ("x", 7));
		assertThrows (IllegalArgumentException.class, () -> check ("x", -1));
		assertThrows (IllegalArgumentException.class, () -> tokens (null));
	}

	@Test
	public void test_wildcardAgainstRegexTranslation () {
		Random random = new Random (5);
		char [] alphabet = {'a', 'b', '*', '?'};
		for (int i = 0; i < 3000; i++) {
			StringBuilder pattern = new StringBuilder (), regex = new StringBuilder (), text = new StringBuilder ();
			for (int k = random.nextInt (6); k > 0; k--) {
				char c = alphabet [random.nextInt (4)];
				pattern.append (c);
				regex.append (c == '*' ? ".*" : c == '?' ? "." : String.valueOf (c));
			}
			for (int k = random.nextInt (7); k > 0; k--) text.append (alphabet [random.nextInt (2)]);
			String p = pattern.toString ();
			if (p.isEmpty ()) continue;
			assertEquals (text.toString ().matches (regex.toString ()), m (p, WILDCARD, text.toString ()), p + " / " + text);
		}
	}
}
