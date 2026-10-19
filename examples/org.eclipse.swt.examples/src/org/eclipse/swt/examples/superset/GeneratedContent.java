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
	/** Line stride in chars (text + newline); shrinks so the char count stays an int at 1B lines. */
	private int stride = STRIDE;

	/** Sets the line count and tells StyledText the whole text changed. */
	void setLines(int n) {
		lines = Math.max(1, n);
		stride = (int) Math.max(2, Math.min(STRIDE, (Integer.MAX_VALUE - 1L) / lines));
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
		return lines * stride - 1;
	}

	@Override
	public String getLine(int lineIndex) {
		text.line(lineIndex, line, 0);
		return new String(line, 0, stride - 1);
	}

	@Override
	public int getLineAtOffset(int offset) {
		return Math.min(offset / stride, lines - 1);
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
		return lineIndex * stride;
	}

	@Override
	public String getTextRange(int start, int length) {
		char[] out = new char[length];
		int pos = start, end = start + length, cached = -1;
		while (pos < end) {
			int ln = pos / stride, col = pos % stride;
			if (ln != cached) {
				text.line(ln, line, 0);
				cached = ln;
			}
			out[pos - start] = col == stride - 1 ? '\n' : line[col];
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
