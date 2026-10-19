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
 *     Hitesh Soliwal (Synexia) - adapted from TV_5.0 treeviewer IViewerTextFilter,
 *         AbstractRegexFilter and AbstractWildCardFilter (docs/m3/sort-filter/PROVENANCE.tsv)
 *******************************************************************************/
package org.eclipse.swt.m3;

import java.util.*;
import java.util.regex.*;

import org.eclipse.swt.*;

/**
 * A compiled text criterion of {@link M3ColumnFilter} (enhanced M3 API).
 * <p>
 * The pattern text is split at unescaped commas into tokens (<code>\,</code> is a literal comma;
 * empty tokens are ignored). A text matches when any token matches it; with {@link #NEGATE} it
 * matches when no token matches it. Token modes:
 * </p>
 * <ul>
 * <li>{@link #CONTAINS}: the token occurs in the text;</li>
 * <li>{@link #STARTS_WITH}: the text starts with the token;</li>
 * <li>{@link #EXACT}: the text equals the token;</li>
 * <li>{@link #WILDCARD}: the whole text matches the token, where <code>*</code> is any sequence,
 *     <code>?</code> any one character, and <code>\</code> escapes <code>*</code>, <code>?</code> and <code>\</code>;</li>
 * <li>{@link #REGEX}: the whole text matches the token as a <code>java.util.regex</code> pattern.</li>
 * </ul>
 * <p>
 * {@link #IGNORE_CASE} compares without case. A <code>null</code> text is the empty text. A match
 * object reuses its regex matchers, so it is used by one thread at a time.
 * </p>
 *
 * @since 3.138
 */
public final class M3TextMatch {

	/** Mode: the token occurs in the text. */
	public static final int CONTAINS = 0;

	/** Mode: the text starts with the token. */
	public static final int STARTS_WITH = 1;

	/** Mode: the text equals the token. */
	public static final int EXACT = 2;

	/** Mode: the whole text matches the token as a wildcard pattern (<code>*</code>, <code>?</code>). */
	public static final int WILDCARD = 3;

	/** Mode: the whole text matches the token as a regular expression. */
	public static final int REGEX = 4;

	/** Flag: a text matches when no token matches it ("doesn't match"). */
	public static final int NEGATE = 1 << 8;

	/** Flag: compare without case. */
	public static final int IGNORE_CASE = 1 << 9;

	private static final int MODE_MASK = 0xFF;
	private static final int STAR = -1, ANY = -2;

	private final String pattern;
	private final int mode;
	private final String [] tokens;
	private final int [][] wildcards;
	private final Matcher [] matchers;

	private M3TextMatch (String pattern, int mode) {
		this.pattern = pattern;
		this.mode = mode;
		tokens = tokens (pattern);
		wildcards = new int [tokens.length][];
		matchers = new Matcher [tokens.length];
		boolean ignoreCase = (mode & IGNORE_CASE) != 0;
		for (int i = 0; i < tokens.length; i++) {
			if (kind () == WILDCARD) wildcards [i] = wildcard (tokens [i]);
			if (kind () == REGEX) matchers [i] = Pattern.compile (tokens [i], ignoreCase ? Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE : 0).matcher (""); //$NON-NLS-1$
		}
	}

	/**
	 * Compiles a pattern text.
	 *
	 * @param pattern the pattern text
	 * @param mode one of the modes, optionally or-ed with {@link #NEGATE} and {@link #IGNORE_CASE}
	 * @return the match
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_NULL_ARGUMENT - if the pattern is null</li>
	 *    <li>ERROR_INVALID_ARGUMENT - if the mode is not a mode, or a regular expression token is not valid</li>
	 * </ul>
	 */
	public static M3TextMatch compile (String pattern, int mode) {
		String problem = check (pattern, mode);
		if (!problem.isEmpty ()) SWT.error (SWT.ERROR_INVALID_ARGUMENT, null, problem);
		return new M3TextMatch (pattern, mode);
	}

	/**
	 * Checks a pattern text: returns the empty string when {@link #compile(String, int)} accepts
	 * it, or the reason why not.
	 *
	 * @param pattern the pattern text
	 * @param mode one of the modes, optionally or-ed with {@link #NEGATE} and {@link #IGNORE_CASE}
	 * @return the empty string, or a message
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_NULL_ARGUMENT - if the pattern is null</li>
	 *    <li>ERROR_INVALID_ARGUMENT - if the mode is not a mode</li>
	 * </ul>
	 */
	public static String check (String pattern, int mode) {
		M3Checks.nonNull (pattern);
		M3Checks.argument ((mode & MODE_MASK) <= REGEX && (mode & ~(MODE_MASK | NEGATE | IGNORE_CASE)) == 0);
		try {
			new M3TextMatch (pattern, mode);
			return ""; //$NON-NLS-1$
		} catch (PatternSyntaxException e) {
			return e.getMessage ();
		}
	}

	/**
	 * Splits a pattern text into its tokens: at every comma not preceded by a backslash; a
	 * backslash before a comma is dropped, other backslashes are kept; empty tokens are dropped.
	 *
	 * @param pattern the pattern text
	 * @return the tokens
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_NULL_ARGUMENT - if the pattern is null</li>
	 * </ul>
	 */
	public static String [] tokens (String pattern) {
		M3Checks.nonNull (pattern);
		List<String> tokens = new ArrayList<> ();
		StringBuilder token = new StringBuilder ();
		for (int i = 0; i < pattern.length (); i++) {
			char c = pattern.charAt (i);
			if (c == '\\' && i + 1 < pattern.length () && pattern.charAt (i + 1) == ',') {
				token.append (',');
				i++;
			} else if (c == ',') {
				flush (token, tokens);
			} else {
				token.append (c);
			}
		}
		flush (token, tokens);
		return tokens.toArray (new String [tokens.size ()]);
	}

	private static void flush (StringBuilder token, List<String> tokens) {
		if (token.length () > 0) tokens.add (token.toString ());
		token.setLength (0);
	}

	/**
	 * Returns the pattern text.
	 *
	 * @return the pattern
	 */
	public String pattern () {
		return pattern;
	}

	/**
	 * Returns the mode with its flags.
	 *
	 * @return the mode
	 */
	public int mode () {
		return mode;
	}

	/**
	 * Returns whether the pattern has a token. A match without tokens matches every text and
	 * {@link M3ColumnFilter} treats it as no criterion.
	 *
	 * @return whether there is a token
	 */
	public boolean isActive () {
		return tokens.length > 0;
	}

	/**
	 * Returns whether a text matches.
	 *
	 * @param text the text, <code>null</code> for the empty text
	 * @return whether it matches
	 */
	public boolean matches (String text) {
		return tokens.length == 0 || any (text) != negated ();
	}

	/* whether any token matches the text, ignoring NEGATE */
	boolean any (String text) {
		String value = text == null ? "" : text; //$NON-NLS-1$
		for (int i = 0; i < tokens.length; i++) {
			if (token (i, value)) return true;
		}
		return false;
	}

	boolean negated () {
		return (mode & NEGATE) != 0;
	}

	private int kind () {
		return mode & MODE_MASK;
	}

	private boolean token (int i, String text) {
		boolean ignoreCase = (mode & IGNORE_CASE) != 0;
		String token = tokens [i];
		switch (kind ()) {
			case CONTAINS: return contains (text, token, ignoreCase);
			case STARTS_WITH: return text.regionMatches (ignoreCase, 0, token, 0, token.length ());
			case EXACT: return ignoreCase ? text.equalsIgnoreCase (token) : text.equals (token);
			case WILDCARD: return wildcard (wildcards [i], text, ignoreCase);
			default: return matchers [i].reset (text).matches ();
		}
	}

	private static boolean contains (String text, String token, boolean ignoreCase) {
		if (!ignoreCase) return text.contains (token);
		for (int i = 0, last = text.length () - token.length (); i <= last; i++) {
			if (text.regionMatches (true, i, token, 0, token.length ())) return true;
		}
		return false;
	}

	/* a wildcard token as codes: STAR, ANY, or a literal character */
	private static int [] wildcard (String token) {
		int [] codes = new int [token.length ()];
		int n = 0;
		for (int i = 0; i < token.length (); i++) {
			char c = token.charAt (i);
			if (c == '\\' && i + 1 < token.length () && "*?\\".indexOf (token.charAt (i + 1)) >= 0) { //$NON-NLS-1$
				codes [n++] = token.charAt (++i);
			} else {
				codes [n++] = c == '*' ? STAR : c == '?' ? ANY : c;
			}
		}
		return Arrays.copyOf (codes, n);
	}

	/* greedy two-pointer wildcard match with backtracking to the last star (LeetCode 44 category) */
	private static boolean wildcard (int [] codes, String text, boolean ignoreCase) {
		int t = 0, p = 0, star = -1, resume = 0;
		while (t < text.length ()) {
			if (p < codes.length && codes [p] != STAR && (codes [p] == ANY || same (codes [p], text.charAt (t), ignoreCase))) {
				t++;
				p++;
			} else if (p < codes.length && codes [p] == STAR) {
				star = p++;
				resume = t;
			} else if (star >= 0) {
				p = star + 1;
				t = ++resume;
			} else {
				return false;
			}
		}
		while (p < codes.length && codes [p] == STAR) p++;
		return p == codes.length;
	}

	private static boolean same (int code, char c, boolean ignoreCase) {
		if (code == c) return true;
		if (!ignoreCase) return false;
		char a = Character.toUpperCase ((char) code), b = Character.toUpperCase (c);
		return a == b || Character.toLowerCase (a) == Character.toLowerCase (b);
	}

	@Override
	public String toString () {
		return "M3TextMatch [" + pattern + ", mode " + Integer.toHexString (mode) + "]"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}
}
