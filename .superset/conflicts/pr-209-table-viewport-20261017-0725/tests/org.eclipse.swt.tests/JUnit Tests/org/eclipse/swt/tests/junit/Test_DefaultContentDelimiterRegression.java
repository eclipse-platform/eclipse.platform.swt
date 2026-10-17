/*******************************************************************************
 * Copyright (c) 2000, 2025 IBM Corporation and others.
 * Copyright (c) 2026 Hitesh Soliwal and contributors.
 * This program and the accompanying materials are made available under the terms
 * of the Eclipse Public License 2.0 which accompanies this distribution, and is
 * available at https://www.eclipse.org/legal/epl-2.0/
 * SPDX-License-Identifier: EPL-2.0
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Hitesh Soliwal and contributors - headless content regression adaptation
 *******************************************************************************/
package org.eclipse.swt.tests.junit;

import static org.junit.jupiter.api.Assertions.*;
import org.eclipse.swt.custom.*;
import org.junit.jupiter.api.Test;

/** Upstream StyledText delimiter regressions, runnable without Display or natives. */
public class Test_DefaultContentDelimiterRegression {

	private static StyledTextContent content(String text) throws Exception {
		var constructor = Class.forName("org.eclipse.swt.custom.DefaultContent").getDeclaredConstructor();
		constructor.setAccessible(true);
		var content = (StyledTextContent) constructor.newInstance();
		content.setText(text);
		return content;
	}

	private static StyledTextContent adjacentLoneDelimiters() throws Exception {
		var content = content("q\rY\n");
		content.replaceTextRange(2, 1, "");
		assertEquals(3, content.getLineCount());
		return content;
	}

	private static void offsets(StyledTextContent content, String text, int... offsets) {
		assertEquals(text, content.getTextRange(0, content.getCharCount()));
		assertEquals(offsets.length, content.getLineCount());
		for (int i = 0; i < offsets.length; i++) {
			assertEquals(offsets[i], content.getOffsetAtLine(i), "line " + i);
			assertEquals(i, content.getLineAtOffset(offsets[i]), "offset " + offsets[i]);
		}
	}

	@Test public void replacementRetainsDelimiterLineIdentity() throws Exception {
		String full = "abc\n\n\r\rdef\n";
		var content = content(full);
		assertEquals(6, content.getLineCount());
		content.replaceTextRange(7, 3, "");
		assertEquals("abc\n\n\r\r\n", content.getTextRange(0, content.getCharCount()));
		assertEquals(6, content.getLineCount());
		content.replaceTextRange(0, content.getCharCount(), full);
		assertEquals(6, content.getLineCount());
	}

	@Test public void deleteCarriageReturnAtExistingBoundary() throws Exception {
		var content = content("x\rY\n");
		content.replaceTextRange(2, 1, "");
		assertEquals(3, content.getLineCount());
		content.replaceTextRange(1, 1, "");
		offsets(content, "x\n", 0, 2);
		assertEquals("x", content.getLine(0));
		assertEquals("", content.getLine(1));
	}

	@Test public void deleteBeforeAdjacentLoneDelimiters() throws Exception {
		var content = adjacentLoneDelimiters();
		content.replaceTextRange(0, 1, "");
		offsets(content, "\r\n", 0, 1, 2);
	}

	@Test public void deletionEventCountsBothExistingLines() throws Exception {
		var content = adjacentLoneDelimiters();
		content.replaceTextRange(3, 0, "z");
		int[] removed = {-1};
		content.addTextChangeListener(new TextChangeListener() {
			@Override public void textChanging(TextChangingEvent event) { removed[0] = event.replaceLineCount; }
			@Override public void textChanged(TextChangedEvent event) { }
			@Override public void textSet(TextChangedEvent event) { }
		});
		content.replaceTextRange(1, 2, "");
		offsets(content, "qz", 0);
		assertEquals(2, removed[0]);
	}

	@Test public void insertBeforeDelimitersAtEnd() throws Exception {
		var content = adjacentLoneDelimiters();
		content.replaceTextRange(0, 0, "z");
		offsets(content, "zq\r\n", 0, 3, 4);
	}

	@Test public void insertBeforeDelimitersWithTrailingText() throws Exception {
		var content = adjacentLoneDelimiters();
		content.replaceTextRange(3, 0, "w");
		content.replaceTextRange(0, 0, "z");
		offsets(content, "zq\r\nw", 0, 3, 4);
	}

	@Test public void multilineInsertBeforeDelimiters() throws Exception {
		var content = adjacentLoneDelimiters();
		content.replaceTextRange(3, 0, "w");
		content.replaceTextRange(1, 0, "a\nb");
		offsets(content, "qa\nb\r\nw", 0, 3, 5, 6);
	}

	@Test public void insertBetweenDelimiters() throws Exception {
		var content = adjacentLoneDelimiters();
		content.replaceTextRange(3, 0, "w");
		content.replaceTextRange(2, 0, "z");
		offsets(content, "q\rz\nw", 0, 2, 4);
	}
}
