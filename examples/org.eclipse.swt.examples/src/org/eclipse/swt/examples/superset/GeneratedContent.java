/*******************************************************************************
 * Copyright (c) 2026 Hitesh Soliwal and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package org.eclipse.swt.examples.superset;

import org.eclipse.swt.custom.StyledTextContent;
import org.eclipse.swt.custom.TextChangeListener;
import org.eclipse.swt.custom.TextChangedEvent;

/**
 * Read-only StyledTextContent of {@code n} generated fixed-width lines. Offset
 * to line is a division, so a 10M-line document costs no storage; text is
 * produced on demand by {@link RowText#line}. Strings are allocated only for
 * getLine/getTextRange, which the StyledText API requires.
 */
final class GeneratedContent implements StyledTextContent {

	static final int STRIDE = RowText.LINE_CHARS + 1;
	private final RowText text = new RowText();
	private final char[] line = new char[STRIDE];
	private TextChangeListener[] listeners = new TextChangeListener[0];
	private int lines = 1;

	/** Sets the line count and tells StyledText the whole text changed. */
	void setLines(int n) {
		lines = Math.max(1, n);
		TextChangedEvent event = new TextChangedEvent(this);
		for (TextChangeListener l : listeners) l.textSet(event);
	}

	@Override
	public void addTextChangeListener(TextChangeListener listener) {
		TextChangeListener[] next = java.util.Arrays.copyOf(listeners, listeners.length + 1);
		next[listeners.length] = listener;
		listeners = next;
	}

	@Override
	public void removeTextChangeListener(TextChangeListener listener) {
		listeners = java.util.Arrays.stream(listeners).filter(l -> l != listener).toArray(TextChangeListener[]::new);
	}

	@Override
	public int getCharCount() {
		return lines * STRIDE - 1;
	}

	@Override
	public String getLine(int lineIndex) {
		text.line(lineIndex, line, 0);
		return new String(line, 0, RowText.LINE_CHARS);
	}

	@Override
	public int getLineAtOffset(int offset) {
		return Math.min(offset / STRIDE, lines - 1);
	}

	@Override
	public int getLineCount() {
		return lines;
	}

	@Override
	public String getLineDelimiter() {
		return "\n";
	}

	@Override
	public int getOffsetAtLine(int lineIndex) {
		return lineIndex * STRIDE;
	}

	@Override
	public String getTextRange(int start, int length) {
		char[] out = new char[length];
		int pos = start, end = start + length, cached = -1;
		while (pos < end) {
			int ln = pos / STRIDE, col = pos % STRIDE;
			if (ln != cached) {
				text.line(ln, line, 0);
				line[RowText.LINE_CHARS] = '\n';
				cached = ln;
			}
			out[pos - start] = line[col];
			pos++;
		}
		return new String(out);
	}

	@Override
	public void replaceTextRange(int start, int replaceLength, String text) {
		// read-only generated document
	}

	@Override
	public void setText(String text) {
		// read-only generated document
	}
}
