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

/**
 * The base class encapsulating the logic used to write styled content as
 * a rich format (HTML, RTF, maybe more). It contains the common parts:
 * iterates on lines, then on each line iterates on plain text and {@link StyleRange}(s).
 *
 * <p>{@code toString()} is guaranteed to return a valid formatted string only after
 * {@code close()} has been called.</p>
 *
 * <p>Whole and partial lines and line breaks can be written. Lines will be
 * formatted using the styles queried from the {@link LineStyleListener},
 * if set, or those set directly in the widget. All styles are applied to
 * the stream like they are rendered by the widget. In addition, the
 * widget font name and size is used for the whole text.</p>
 */
// TODO (visjee) SWING
abstract class StyledTextWriterBase extends TextWriter {

	public StyledTextWriterBase(int start, int length) {
		super(start, length);
	}
//	final StyledText styledText;
//
//	/**
//	 * Creates a writer that processed content starting at offset "start"
//	 * in the document. {@code start} and {@code length} can be set to specify
//	 * partial lines.
//	 *
//	 * @param start start offset of content to write, 0 based from beginning of document
//	 * @param length length of content to write
//	 * @param styledText the widget to produce the RTF from
//	 */
//	public StyledTextWriterBase(StyledText styledText, int start, int length) {
//		super(start, length);
//		this.styledText = styledText;
//	}
//
//	/**
//	 * Appends the specified segment of "string" to the output data.
//	 * Copy from {@code start} up to, but excluding, {@code end}.
//	 *
//	 * @param string string to copy a segment from. Must not contain line breaks.
//	 *  Line breaks should be written using {@link #writeLineDelimiter()}
//	 * @param start start offset of segment. 0 based.
//	 * @param end end offset of segment
//	 */
//	void writeEscaped(String string, int start, int end) {
//		String textToWrite = string.substring(start, end);
//		write(escapeText(textToWrite));
//	}
//
//	/**
//	 * Appends the specified line text to the output data. Lines will be formatted
//	 * using the styles queried from the LineStyleListener, if set, or those set
//	 * directly in the widget.
//	 *
//	 * @param line line text to write. Must not contain line breaks
//	 *  Line breaks should be written using {@link #writeLineDelimiter(String)}
//	 * @param lineOffset offset of the line. 0 based from the start of the
//	 *  widget document. Any text occurring before the start offset or after the
//	 *  end offset specified during object creation is ignored.
//	 *
//	 * @throws SWTException {@code ERROR_IO} when the writer is closed.
//	 */
//	@Override
//	public void writeLine(String line, int lineOffset) {
//		if (isClosed()) {
//			SWT.error(SWT.ERROR_IO);
//		}
//
//		int lineIndex = styledText.content.getLineAtOffset(lineOffset);
//		int lineAlignment;
//		int lineIndent;
//		boolean lineJustify;
//		int verticalIndent;
//		int[] ranges;
//		StyleRange[] styles;
//
//		StyledTextEvent event = styledText.getLineStyleData(lineOffset, line);
//		if (event != null) {
//			verticalIndent = event.verticalIndent;
//			lineAlignment = event.alignment;
//			lineIndent = event.indent;
//			lineJustify = event.justify;
//			ranges = event.ranges;
//			styles = event.styles;
//		} else {
//			verticalIndent = styledText.renderer.getLineVerticalIndent(lineIndex);
//			lineAlignment = styledText.renderer.getLineAlignment(lineIndex, styledText.alignment);
//			lineIndent = styledText.renderer.getLineIndent(lineIndex, styledText.indent);
//			lineJustify = styledText.renderer.getLineJustify(lineIndex, styledText.justify);
//			ranges = styledText.renderer.getRanges(lineOffset, line.length());
//			styles = styledText.renderer.getStyleRanges(lineOffset, line.length(), false);
//		}
//
//		if (styles == null) {
//			styles = new StyleRange[0];
//		}
//
//		event = styledText.getLineBackgroundData(lineOffset, line);
//		Color lineBackground = (event != null && event.lineBackground != null)
//				? event.lineBackground
//				: styledText.renderer.getLineBackground(lineIndex, null);
//
//		writeStyledLine(line, lineOffset, ranges, styles, lineBackground, lineIndent, verticalIndent, lineAlignment, lineJustify);
//	}
//
//	/**
//	 * Appends the specified line text to the output data.
//	 *
//	 * <p>Use the colors and font styles specified in {@code styles} and {@code lineBackground}.
//	 * Formatting is written to reflect the text rendering by the text widget.
//	 * Style background colors take precedence over the line background color.</p>
//	 *
//	 * @param line line text to write as RTF. Must not contain line breaks
//	 *  Line breaks should be written using writeLineDelimiter()
//	 * @param lineOffset offset of the line. 0 based from the start of the
//	 *  widget document. Any text occurring before the start offset or after the
//	 *  end offset specified during object creation is ignored.
//	 * @param styles styles to use for formatting. Must not be null.
//	 * @param lineBackground line background color to use for formatting.
//	 *  May be null.
//	 */
//	void writeStyledLine(String line, int lineOffset, int ranges[], StyleRange[] styles,
//			Color lineBackground, int indent, int verticalIndent, int alignment, boolean justify) {
//
//		int lineLength = line.length();
//		int startOffset = getStart();
//		int writeOffset = startOffset - lineOffset;
//		if (writeOffset >= lineLength) {
//			return;
//		}
//		int lineIndex = Math.max(0, writeOffset);
//
//		String atLineEnd = writeLineStart(lineBackground, indent, verticalIndent, alignment, justify);
//
//		int endOffset = startOffset + super.getCharCount();
//
//		// We are already at the end, should not write an empty paragraph
//		if (lineOffset >= endOffset) {
//			return;
//		}
//
//		int lineEndOffset = Math.min(lineLength, endOffset - lineOffset);
//
//		int outTextLen = 0; // collect the length of the text we output (unescaped)
//		for (int i = 0; i < styles.length; i++) {
//			StyleRange style = styles[i];
//			int start, end;
//			if (ranges != null) {
//				start = ranges[i << 1] - lineOffset;
//				end = start + ranges[(i << 1) + 1];
//			} else {
//				start = style.start - lineOffset;
//				end = start + style.length;
//			}
//			// skip over partial first line
//			if (end < writeOffset) {
//				continue;
//			}
//			// style starts beyond line end or write end
//			if (start >= lineEndOffset) {
//				break;
//			}
//			// write any unstyled text
//			if (lineIndex < start) {
//				// copy to start of style
//				// style starting beyond end of write range or end of line
//				// is guarded against above.
//				writeEscaped(line, lineIndex, start);
//				outTextLen += start - lineIndex;
//				lineIndex = start;
//			}
//			// write styled text
//			String atSpanEnd = writeSpanStart(style);
//
//			// copy to end of style or end of write range or end of line
//			int copyEnd = Math.min(end, lineEndOffset);
//			// guard against invalid styles and let style processing continue
//			copyEnd = Math.max(copyEnd, lineIndex);
//			writeEscaped(line, lineIndex, copyEnd);
//			outTextLen += copyEnd - lineIndex;
//
//			writeSpanEnd(atSpanEnd);
//
//			lineIndex = copyEnd;
//		}
//
//		// write the unstyled text at the end of the line
//		if (lineIndex < lineEndOffset) {
//			writeEscaped(line, lineIndex, lineEndOffset);
//			outTextLen += lineEndOffset - lineIndex;
//		}
//
//		if (outTextLen == 0) {
//			writeEmptyLine();
//		}
//
//		writeLineEnd(atLineEnd);
//	}
//
//	/**
//	 * Writes the part that only shows once, at the beginning of the output data.
//	 */
//	abstract void writeHeader();
//
//	/**
//	 * Takes plain text and returns the same text escaped using the rules of the output format.
//	 * @param text the text to escape
//	 * @return the escaped text
//	 */
//	abstract String escapeText(String text);
//
//	/**
//	 * Invoked at the beginning of each line in the original widget.
//	 *
//	 * <p>It should output whatever tags are appropriate to start a new line in the output format.<br>
//	 * It will return the text that has to be output at the end of the line.</p>
//	 *
//	 * @return the text to append at the end of the line
//	 */
//	abstract String writeLineStart(Color lineBackground, int indent, int verticalIndent, int alignment, boolean justify);
//
//	/**
//	 * Invoked at the end of of each line in the original widget.
//	 *
//	 * <p>It receives whatever text {@link #writeLineStart(Color,int,int,int,boolean)} returned
//	 * and it will output it. A class might override it to do more.</p>
//	 *
//	 * @param prepared the leftover text prepared by {@code writeLineStart}
//	 */
//	void writeLineEnd(String prepared) {
//		write(prepared);
//	}
//
//	/**
//	 * Invoked at the end of each line, writeLineEnd, if there was no text output.
//	 *
//	 * <p>This is needed for HTML, which does not render empty paragraphs.</p>
//	 */
//	abstract void writeEmptyLine();
//
//	/**
//	 * Invoked at the beginning of each styled span fragment in the original widget.
//	 *
//	 * <p>It should output whatever tags are appropriate to start a formatted span in the output format.<br>
//	 * It will return the text that has to be output at the end of the span (to close it).</p>
//	 *
//	 * @return the text to append at the end of the styled span
//	 */
//	abstract String writeSpanStart(StyleRange style);
//
//	/**
//	 * Invoked at the end of of each styled span fragment in the original widget.
//	 *
//	 * <p>It receives whatever text {@link #writeSpanStart(StyleRange)} returned
//	 * and it will output it. A class might override it to do more.</p>
//	 *
//	 * @param prepared the leftover text prepared by {@code writeSpanStart}
//	 */
//	void writeSpanEnd(String prepared) {
//		write(prepared);
//	}
}
