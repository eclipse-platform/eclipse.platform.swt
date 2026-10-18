/*******************************************************************************
 * Copyright (c) 2000, 2022 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.custom;

import org.eclipse.swt.*;

/**
 * The <code>TextWriter</code> class is used to write widget content to
 * a string.  Whole and partial lines and line breaks can be written. To write
 * partial lines, specify the start and length of the desired segment
 * during object creation.
 * <p>
 * <b>NOTE:</b> <code>toString()</code> is guaranteed to return a valid string only after close()
 * has been called.
 * </p>
 */
class TextWriter {
	private StringBuilder buffer;
	private int startOffset;	// offset of first character that will be written
	private int endOffset;		// offset of last character that will be written.
								// 0 based from the beginning of the widget text.
	private boolean isClosed = false;

	/**
	 * Creates a writer that writes content starting at offset "start"
	 * in the document.  <code>start</code> and <code>length</code> can be set to specify partial lines.
	 *
	 * @param start start offset of content to write, 0 based from beginning of document
	 * @param length length of content to write
	 */
	public TextWriter(int start, int length) {
		buffer = new StringBuilder(length);
		startOffset = start;
		endOffset = start + length;
	}
	/**
	 * Closes the writer. Once closed no more content can be written.
	 * <b>NOTE:</b>  <code>toString()</code> is not guaranteed to return a valid string unless
	 * the writer is closed.
	 */
	public void close() {
		if (!isClosed) {
			isClosed = true;
		}
	}
	/**
	 * Returns the number of characters to write.
	 * @return the integer number of characters to write
	 */
	public int getCharCount() {
		return endOffset - startOffset;
	}
	/**
	 * Returns the offset where writing starts. 0 based from the start of
	 * the widget text. Used to write partial lines.
	 * @return the integer offset where writing starts
	 */
	public int getStart() {
		return startOffset;
	}
	/**
	 * Returns whether the writer is closed.
	 * @return a boolean specifying whether or not the writer is closed
	 */
	public boolean isClosed() {
		return isClosed;
	}
	/**
	 * Returns the string.  <code>close()</code> must be called before <code>toString()</code>
	 * is guaranteed to return a valid string.
	 *
	 * @return the string
	 */
	@Override
	public String toString() {
		return buffer.toString();
	}
	/**
	 * Appends the given string to the data.
	 */
	void write(String string) {
		buffer.append(string);
	}
	/**
	 * Inserts the given string to the data at the specified offset.
	 * <p>
	 * Do nothing if "offset" is &lt; 0 or &gt; getCharCount()
	 * </p>
	 *
	 * @param string text to insert
	 * @param offset offset in the existing data to insert "string" at.
	 */
	void write(String string, int offset) {
		if (offset < 0 || offset > buffer.length()) {
			return;
		}
		buffer.insert(offset, string);
	}
	/**
	 * Appends the given int to the data.
	 */
	void write(int i) {
		buffer.append(i);
	}
	/**
	 * Appends the given character to the data.
	 */
	void write(char i) {
		buffer.append(i);
	}
	/**
	 * Appends the specified line text to the data.
	 *
	 * @param line line text to write. Must not contain line breaks
	 * 	Line breaks should be written using writeLineDelimiter()
	 * @param lineOffset offset of the line. 0 based from the start of the
	 * 	widget document. Any text occurring before the start offset or after the
	 *	end offset specified during object creation is ignored.
	 * @exception SWTException <ul>
	 *   <li>ERROR_IO when the writer is closed.</li>
	 * </ul>
	 */
	public void writeLine(String line, int lineOffset) {
		if (isClosed) {
			SWT.error(SWT.ERROR_IO);
		}
		int writeOffset = startOffset - lineOffset;
		int lineLength = line.length();
		int lineIndex;
		if (writeOffset >= lineLength) {
			return;							// whole line is outside write range
		} else if (writeOffset > 0) {
			lineIndex = writeOffset;		// line starts before write start
		} else {
			lineIndex = 0;
		}
		int copyEnd = Math.min(lineLength, endOffset - lineOffset);
		if (lineIndex < copyEnd) {
			write(line.substring(lineIndex, copyEnd));
		}
	}
	/**
	 * Appends the specified line delimiter to the data.
	 *
	 * @param lineDelimiter line delimiter to write
	 * @exception SWTException <ul>
	 *   <li>ERROR_IO when the writer is closed.</li>
	 * </ul>
	 */
	public void writeLineDelimiter(String lineDelimiter) {
		if (isClosed) {
			SWT.error(SWT.ERROR_IO);
		}
		write(lineDelimiter);
	}
}
