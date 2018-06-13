/*******************************************************************************
 * Copyright (c) 2018 Red Hat and others. All rights reserved.
 * The contents of this file are made available under the terms
 * of the GNU Lesser General Public License (LGPL) Version 2.1 that
 * accompanies this distribution (lgpl-v21.txt).  The LGPL is also
 * available at http://www.gnu.org/licenses/lgpl.html.  If the version
 * of the LGPL at http://www.gnu.org is different to the version of
 * the LGPL accompanying this distribution and there is any conflict
 * between the two license versions, the terms of the LGPL accompanying
 * this distribution shall govern.
 *
 * Contributors:
 *     Red Hat - initial API and implementation
 */
package org.eclipse.swt.tests.gtk;

import static org.junit.Assert.fail;

import java.nio.charset.StandardCharsets;

import org.eclipse.swt.internal.Converter;
import org.junit.Ignore;
import org.junit.Test;

/**
 *  Good source for UTF-8 code points for testing:
 *  https://en.wikipedia.org/wiki/List_of_Unicode_characters
 *
 *  We care about Ascii, UTF-8 (as it's used by glib/gtk much) and UTF-16LE (as it's used by java/intel/amd architecture).
 */
public class Test_GtkConverter {

	static final String emptyStr = "";

	static final String asciiLetterA = "A";	// = 65  // Note, UTF-8 is backwards compatible with Ascii
	static final String dollarSign = "$"; // =36

	static final String asciiLetters = "ABCabc"; // 65(A), 66, 67   97(a), 98, 99

	// Anything above 127 translates to 2 bytes in utf-8.  See: https://en.wikipedia.org/wiki/UTF-8#Description
	static final String codePoint174 = "®";   // U+00AE	Registered sign.
	static final String unicodeCharactersLowCodePoints = "®ÖöėŊ‐"; // bigger than 127, but not many bytes.
	static final String unicodeCharactersHighCodePoints = "▇░▙▚▧▫♂☢⛔";  //2000+ code points.

	@Test
	public void test_HeuristicASCII_letterA() {
		helper_testHeuristic(asciiLetterA.getBytes(StandardCharsets.US_ASCII), asciiLetterA); // A = 65
	}
	@Test
	public void test_HeuristicASCII_dollarSign() {
		helper_testHeuristic(dollarSign.getBytes(StandardCharsets.US_ASCII), dollarSign); // $ = 36
	}

	@Test
	public void test_Heuristic_null() {
		helper_testHeuristic(new byte[] {0}, emptyStr);  // simulate null terminator.
	}

	@Test
	public void test_HeuristicASCII_emptyString() {
		helper_testHeuristic(emptyStr.getBytes(StandardCharsets.US_ASCII), emptyStr); // "" -> [] (empty byte array)
	}

	@Test
	public void test_HeuristicUTF8_null() {
		helper_testHeuristic(emptyStr.getBytes(StandardCharsets.UTF_8), emptyStr);
	}

	@Test
	public void test_HeuristicUTF16LE_null() {
		helper_testHeuristic(emptyStr.getBytes(StandardCharsets.UTF_16LE), emptyStr);
	}

	@Test
	public void test_HeuristicASCII_letters() {
		helper_testHeuristic(asciiLetters.getBytes(StandardCharsets.US_ASCII), asciiLetters);
	}

	@Test
	public void test_HeuristicUTF8_letterUnder127() {
		helper_testHeuristic(asciiLetterA.getBytes(StandardCharsets.UTF_8), asciiLetterA);
	}

	@Test
	public void test_HeuristicUTF8_letterOver127() {
		helper_testHeuristic(codePoint174.getBytes(StandardCharsets.UTF_8), codePoint174);
	}


	@Test
	public void test_HeuristicUTF8_letterSpecial() {
		helper_testHeuristic("Ё".getBytes(StandardCharsets.UTF_8), "Ё");
	}

	@Test
	public void test_HeuristicUTF8_LowCodePoints() {
		helper_testHeuristic(unicodeCharactersLowCodePoints.getBytes(StandardCharsets.UTF_8), unicodeCharactersLowCodePoints);
	}

	@Test
	public void test_HeuristicUTF8_HighCodePoints() {
		byte [] testBytes = unicodeCharactersHighCodePoints.getBytes(StandardCharsets.UTF_8);
		helper_testHeuristic(testBytes, unicodeCharactersHighCodePoints);
	}


	@Test
	public void test_HeuristicUTF16_Asciiletter() {
		helper_testHeuristic(asciiLetterA.getBytes(StandardCharsets.UTF_16LE), asciiLetterA);
	}

	@Test
	public void test_HeuristicUTF16_AsciiLetters() {
		helper_testHeuristic(asciiLetters.getBytes(StandardCharsets.UTF_16LE), asciiLetters);
	}

	@Test
	public void test_HeuristicUTF16_letter() {
		String testValue = "®"; // 174
		byte [] testBytes = testValue.getBytes(StandardCharsets.UTF_16LE);
		helper_testHeuristic(testBytes, testValue);
	}

	@Test
	public void test_HeuristicUTF16_letters() {
		helper_testHeuristic(unicodeCharactersLowCodePoints.getBytes(StandardCharsets.UTF_16LE), unicodeCharactersLowCodePoints);
	}

	@Test
	public void test_HeuristicUTF16_LotsOfLetters() {
		byte [] testBytes = unicodeCharactersHighCodePoints.getBytes(StandardCharsets.UTF_16LE);
		helper_testHeuristic(testBytes, unicodeCharactersHighCodePoints);
	}

	/**
	 * There are a few unicode characters that are ambiguous if they are decoded on their own,
	 * as they can translate to either two valid UTF-8 characters or a single valid UTF-16LE character.
	 *
	 * e.g 'Ё'. (but there are others).
	 *
	 * The heuristic is better is better if there are 2+ characters, e.g HЁLLO WORLD.
	 *
	 * This test is documented, but is currently known to fail.
	 *
	 */
	@Ignore
	@Test
	public void test_Heuristic_specialSingleCases() {
		byte [] testBytes = "Ё".getBytes(StandardCharsets.UTF_16LE);
		helper_testHeuristic(testBytes, "Ё");
	}

	private void helper_testHeuristic(byte[] testBytes, String expected) {
		String result = Converter.byteToStringViaHeuristic(testBytes);
		if (!expected.equals(result)) {
			fail();
		}
	}

}
