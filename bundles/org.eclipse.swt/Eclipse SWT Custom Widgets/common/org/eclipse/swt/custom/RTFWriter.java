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

import java.util.*;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;

/**
 * The {@code RTFWriter} class is used to write widget content as
 * rich text. The implementation complies with the RTF specification
 * version 1.5.
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
class RTFWriter extends TextWriter {
	private final StyledText styledText;
	static final int DEFAULT_FOREGROUND = 0;
	static final int DEFAULT_BACKGROUND = 1;
	List<Color> colorTable;
	List<Font> fontTable;

	/**
	 * Creates a RTF writer that writes content starting at offset "start"
	 * in the document. {@code start} and {@code length} can be set to specify
	 * partial lines.
	 *
	 * @param start start offset of content to write, 0 based from beginning of document
	 * @param length length of content to write
	 * @param styledText the widget to produce the RTF from
	 */
	public RTFWriter(StyledText styledText, int start, int length) {
		super(start, length);
		this.styledText = styledText;
		colorTable = new ArrayList<>();
		fontTable = new ArrayList<>();
		colorTable.add(this.styledText.getForeground());
		colorTable.add(this.styledText.getBackground());
		fontTable.add(this.styledText.getFont());
	}

	/**
	 * Closes the writer. Once closed no more content can be written.
	 *
	 * <p><b>NOTE:</b> {@code toString()} does not return a valid formatted string until
	 * {@code close()} has been called.</p>
	 */
	@Override
	public void close() {
		if (!isClosed()) {
			writeHeader();
			write("\n}}\0");
			super.close();
		}
	}

	/**
	 * Appends the specified segment of "string" to the output data.
	 * Copy from {@code start} up to, but excluding, {@code end}.
	 *
	 * @param string string to copy a segment from. Must not contain line breaks.
	 *  Line breaks should be written using {@link #writeLineDelimiter()}
	 * @param start start offset of segment. 0 based.
	 * @param end end offset of segment
	 */
	void writeEscaped(String string, int start, int end) {
		String textToWrite = string.substring(start, end);
		write(escapeText(textToWrite));
	}

	/**
	 * Writes the RTF header including font table and color table.
	 */
	void writeHeader() {
		StringBuilder header = new StringBuilder();
		FontData fontData = styledText.getFont().getFontData()[0];
		header.append("{\\rtf1\\ansi");
		// specify code page, necessary for copy to work in bidi
		// systems that don't support Unicode RTF.
		String cpg = System.getProperty("file.encoding").toLowerCase();
		if (cpg.startsWith("cp") || cpg.startsWith("ms")) {
			cpg = cpg.substring(2, cpg.length());
			header.append("\\ansicpg");
			header.append(cpg);
		}
		header.append("\\uc1\\deff0{\\fonttbl{\\f0\\fnil ");
		header.append(fontData.getName());
		header.append(";");
		for (int i = 1; i < fontTable.size(); i++) {
			header.append("\\f");
			header.append(i);
			header.append(" ");
			FontData fd = fontTable.get(i).getFontData()[0];
			header.append(fd.getName());
			header.append(";");
		}
		header.append("}}\n{\\colortbl");
		for (Color color : colorTable) {
			header.append("\\red");
			header.append(color.getRed());
			header.append("\\green");
			header.append(color.getGreen());
			header.append("\\blue");
			header.append(color.getBlue());
			header.append(";");
		}
		// some RTF readers ignore the deff0 font tag. Explicitly
		// set the font for the whole document to work around this.
		header.append("}\n{\\f0\\fs");
		// font size is specified in half points
		header.append(fontData.getHeight() * 2);
		header.append(" ");
		write(header.toString(), 0);
	}

	/**
	 * Appends the specified line text to the RTF data.  Lines will be formatted
	 * using the styles queried from the LineStyleListener, if set, or those set
	 * directly in the widget.
	 *
	 * @param line line text to write as RTF. Must not contain line breaks
	 *  Line breaks should be written using {@link #writeLineDelimiter(String)}
	 * @param lineOffset offset of the line. 0 based from the start of the
	 *  widget document. Any text occurring before the start offset or after the
	 *  end offset specified during object creation is ignored.
	 *
	 * @throws SWTException {@code ERROR_IO} when the writer is closed.
	 */
	@Override
	public void writeLine(String line, int lineOffset) {
		if (isClosed()) {
			SWT.error(SWT.ERROR_IO);
		}

		int lineIndex = styledText.content.getLineAtOffset(lineOffset);
		int lineAlignment;
		int lineIndent;
		boolean lineJustify;
		int verticalIndent;
		int[] ranges;
		StyleRange[] styles;

		StyledTextEvent event = styledText.getLineStyleData(lineOffset, line);
		if (event != null) {
			verticalIndent = event.verticalIndent;
			lineAlignment = event.alignment;
			lineIndent = event.indent;
			lineJustify = event.justify;
			ranges = event.ranges;
			styles = event.styles;
		} else {
			verticalIndent = styledText.renderer.getLineVerticalIndent(lineIndex);
			lineAlignment = styledText.renderer.getLineAlignment(lineIndex, styledText.alignment);
			lineIndent =  styledText.renderer.getLineIndent(lineIndex, styledText.indent);
			lineJustify = styledText.renderer.getLineJustify(lineIndex, styledText.justify);
			ranges = styledText.renderer.getRanges(lineOffset, line.length());
			styles = styledText.renderer.getStyleRanges(lineOffset, line.length(), false);
		}

		if (styles == null) {
			styles = new StyleRange[0];
		}

		event = styledText.getLineBackgroundData(lineOffset, line);
		Color lineBackground = (event != null && event.lineBackground != null)
				? event.lineBackground
				: styledText.renderer.getLineBackground(lineIndex, null);

		writeStyledLine(line, lineOffset, ranges, styles, lineBackground, lineIndent, verticalIndent, lineAlignment, lineJustify);
	}

	/**
	 * Appends the specified line delimiter to the RTF data.
	 *
	 * @param lineDelimiter line delimiter to write as RTF.
	 *
	 * @throws SWTException {@code ERROR_IO} when the writer is closed.
	 */
	@Override
	public void writeLineDelimiter(String lineDelimiter) {
		if (isClosed()) {
			SWT.error(SWT.ERROR_IO);
		}
		write(lineDelimiter);
		write("\\par ");
	}

	/**
	 * Appends the specified line text to the RTF data.
	 *
	 * <p>Use the colors and font styles specified in {@code styles} and {@code lineBackground}.
	 * Formatting is written to reflect the text rendering by the text widget.
	 * Style background colors take precedence over the line background color.</p>
	 *
	 * @param line line text to write as RTF. Must not contain line breaks
	 *  Line breaks should be written using writeLineDelimiter()
	 * @param lineOffset offset of the line. 0 based from the start of the
	 *  widget document. Any text occurring before the start offset or after the
	 *  end offset specified during object creation is ignored.
	 * @param styles styles to use for formatting. Must not be null.
	 * @param lineBackground line background color to use for formatting.
	 *  May be null.
	 */
	void writeStyledLine(String line, int lineOffset, int ranges[], StyleRange[] styles,
			Color lineBackground, int indent, int verticalIndent, int alignment, boolean justify) {

		int lineLength = line.length();
		int startOffset = getStart();
		int writeOffset = startOffset - lineOffset;
		if (writeOffset >= lineLength) {
			return;
		}
		int lineIndex = Math.max(0, writeOffset);

		write("\\fi");
		write(indent);
		switch (alignment) {
			case SWT.LEFT: write("\\ql"); break;
			case SWT.CENTER: write("\\qc"); break;
			case SWT.RIGHT: write("\\qr"); break;
		}
		if (justify) {
			write("\\qj");
		}
		write(" ");

		if (lineBackground != null) {
			// Background colors are written using the {@code \chshdng0\chcbpat} tag (vs. the {@code \cb} tag).
			write("{\\chshdng0\\chcbpat");
			write(getColorIndex(lineBackground, DEFAULT_BACKGROUND));
			write(" ");
		}

		int endOffset = startOffset + super.getCharCount();
		int lineEndOffset = Math.min(lineLength, endOffset - lineOffset);

		for (int i = 0; i < styles.length; i++) {
			StyleRange style = styles[i];
			int start, end;
			if (ranges != null) {
				start = ranges[i << 1] - lineOffset;
				end = start + ranges[(i << 1) + 1];
			} else {
				start = style.start - lineOffset;
				end = start + style.length;
			}
			// skip over partial first line
			if (end < writeOffset) {
				continue;
			}
			// style starts beyond line end or write end
			if (start >= lineEndOffset) {
				break;
			}
			// write any unstyled text
			if (lineIndex < start) {
				// copy to start of style
				// style starting beyond end of write range or end of line
				// is guarded against above.
				writeEscaped(line, lineIndex, start);
				lineIndex = start;
			}
			// write styled text
			write("{\\cf");
			write(getColorIndex(style.foreground, DEFAULT_FOREGROUND));
			int colorIndex = getColorIndex(style.background, DEFAULT_BACKGROUND);
			if (colorIndex != DEFAULT_BACKGROUND) {
				write("\\chshdng0\\chcbpat");
				write(colorIndex);
			}
			int fontStyle = style.fontStyle;
			Font font = style.font;
			if (font != null) {
				int fontIndex = getFontIndex(font);
				write("\\f");
				write(fontIndex);
				FontData fontData = font.getFontData()[0];
				write("\\fs");
				write(fontData.getHeight() * 2);
				fontStyle = fontData.getStyle();
			}
			if ((fontStyle & SWT.BOLD) != 0) {
				write("\\b");
			}
			if ((fontStyle & SWT.ITALIC) != 0) {
				write("\\i");
			}
			if (style.underline) {
				write("\\ul");
			}
			if (style.strikeout) {
				write("\\strike");
			}
			write(" ");

			// copy to end of style or end of write range or end of line
			int copyEnd = Math.min(end, lineEndOffset);
			// guard against invalid styles and let style processing continue
			copyEnd = Math.max(copyEnd, lineIndex);
			writeEscaped(line, lineIndex, copyEnd);
			if ((fontStyle & SWT.BOLD) != 0) {
				write("\\b0");
			}
			if ((style.fontStyle & SWT.ITALIC) != 0) {
				write("\\i0");
			}
			if (style.underline) {
				write("\\ul0");
			}
			if (style.strikeout) {
				write("\\strike0");
			}
			write("}");
			lineIndex = copyEnd;
		}

		// write the unstyled text at the end of the line
		if (lineIndex < lineEndOffset) {
			writeEscaped(line, lineIndex, lineEndOffset);
		}
		if (lineBackground != null) {
			write("}");
		}
	}

	// ==== Helper methods ====

	static String escapeText(String string) {
		StringBuilder result = new StringBuilder(string.length());
		string.chars().forEach(ch -> {
			if (ch > 0x7F) {
				result.append("\\u");
				result.append(Integer.toString((short) ch));
				result.append('?'); // ANSI representation (1 byte long, \\uc1)
			} else if (ch == '}' || ch == '{' || ch == '\\') {
				result.append('\\');
				result.append((char) ch);
			} else {
				// Fixes bug 21698.
				result.append((char) ch);
			}
        });
        return result.toString();
	}

	/**
	 * Returns the index of the specified color in the RTF color table.
	 *
	 * @param color the color
	 * @param defaultIndex return value if color is null
	 * @return the index of the specified color in the RTF color table
	 *  or "defaultIndex" if "color" is null.
	 */
	int getColorIndex(Color color, int defaultIndex) {
		if (color == null) return defaultIndex;
		int index = colorTable.indexOf(color);
		if (index == -1) {
			index = colorTable.size();
			colorTable.add(color);
		}
		return index;
	}

	/**
	 * Returns the index of the specified color in the RTF color table.
	 *
	 * @param color the color
	 * @param defaultIndex return value if color is null
	 * @return the index of the specified color in the RTF color table
	 *  or "defaultIndex" if "color" is null.
	 */
	int getFontIndex(Font font) {
		int index = fontTable.indexOf(font);
		if (index == -1) {
			index = fontTable.size();
			fontTable.add(font);
		}
		return index;
	}
}
