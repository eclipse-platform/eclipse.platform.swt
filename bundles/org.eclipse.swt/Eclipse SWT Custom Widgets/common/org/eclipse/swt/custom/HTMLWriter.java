/*******************************************************************************
 * Copyright (c) 2022 Mihai Nita and others
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package org.eclipse.swt.custom;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;

/**
 * The <code>HTMLWriter</code> class is used to write widget content as
 * html. The implementation creates a very-very simple HTML, as compatible
 * as possible: simple styles, no JavaScript.
 *
 * <p>{@code toString()} is guaranteed to return a valid HTML string only after
 * {@code close()} has been called.</p>
 *
 * <p>Whole and partial lines and line breaks can be written. Lines will be
 * formatted using the styles queried from the {@link LineStyleListener},
 * if set, or those set directly in the widget. All styles are applied to
 * the HTML stream like they are rendered by the widget. In addition, the
 * widget font name and size is used for the whole text.</p>
 *
 * <p>How the concepts are mapped to HTML:</p>
 * <ul>
 *   <li>The global {@link StyledText} properties (for example {@link StyledText#getBackground()}
 *       are written in a wrapping {@code <div>}.</p>
 *   <li>The line specific properties (for example {@link StyledText#getLineBackground(int)}
 *       are written the {@code <p>} elements.</li>
 *   <li>The {@link StyleRange} properties inside the line are written the {@code <span>} elements.</li>
 * </ul>
 */
class HTMLWriter extends TextWriter {
	private final StyledText styledText;

	/**
	 * Creates a HTML writer that writes content starting at offset "start"
	 * in the document. {@code start} and {@code length} can be set to specify
	 * partial lines.
	 *
	 * @param start start offset of content to write, 0 based from beginning of document
	 * @param length length of content to write
	 * @param styledText the widget to produce the HTML from
	 */
	public HTMLWriter(StyledText styledText, int start, int length) {
		super(start, length);
		this.styledText = styledText;
		writeHeader();
	}

	/**
	 * Closes the HTML writer. Once closed no more content can be written.
	 *
	 * <p><b>NOTE:</b> {@code toString()} does not return a valid HTML string until
	 * {@code close()} has been called.</p>
	 */
	@Override
	public void close() {
		if (!isClosed()) {
			write("</div>\n");
			write("</div>\n");
			super.close();
		}
	}

	/**
	 * Appends the specified segment of "string" to the HTML data.
	 * Copy from {@code start} up to, but excluding, {@code end}.
	 *
	 * @param string string to copy a segment from. Must not contain line breaks.
	 *  Line breaks should be written using {@link #writeLineDelimiter()}
	 * @param start start offset of segment. 0 based.
	 * @param end end offset of segment
	 */
	private void write(String string, int start, int end, boolean escape) {
		String textToWrite = string.substring(start, end);
		write(escape ? textToHtml(textToWrite) : textToWrite);
	}

	/**
	 * Writes the opening of the two {@code div}s that wrap the content.
	 */
	private void writeHeader() {

		StringBuilder outerDivStyle = new StringBuilder();
		StringBuilder innerDivStyle = new StringBuilder();

		appendStyle(outerDivStyle, "background-color:", styledText.getMarginColor(), ";");
		appendStyle(innerDivStyle, "color:", styledText.getForeground(), ";");
		appendStyle(innerDivStyle, "background-color:", styledText.getBackground(), ";");

		appendStyle(outerDivStyle, "padding-left:", styledText.getLeftMargin(), "px;");
		appendStyle(outerDivStyle, "padding-top:", styledText.getTopMargin(), "px;");
		appendStyle(outerDivStyle, "padding-right:", styledText.getRightMargin(), "px;");
		appendStyle(outerDivStyle, "padding-bottom:", styledText.getBottomMargin(), "px;");

		String language = appendFont(innerDivStyle, styledText.getFont(), 0);

		// Using wrapIndent like this messes up the line background (if set).
//		int wrapIndent = styledText.getWrapIndent();
//		if (wrapIndent != 0) {
//			appendStyle(globalStyle, "padding-left:", wrapIndent, "px;");
//		}
//		int indent = styledText.getIndent() - wrapIndent;
		int indent = styledText.getIndent();
		if (indent != 0) {
			appendStyle(innerDivStyle, "text-indent:", indent, "px;");
		}

		// TODO?
//		int lineSpacing = styledText.getLineSpacing();

		if (!styledText.getWordWrap()) {
			appendStyle(innerDivStyle, "white-space:nowrap;");
		}

		appendAlignAndJustify(innerDivStyle, styledText.getAlignment(), styledText.getJustify());

		if (styledText.getOrientation() == SWT.RIGHT_TO_LEFT || styledText.getTextDirection() == SWT.RIGHT_TO_LEFT) {
			appendStyle(innerDivStyle, "direction:rtl;");
		}

		// Do we have any use for these?
//		Color selectionBackground = styledText.getSelectionBackground();
//		Color selectionForeground = styledText.getSelectionForeground();
//		int tabs = styledText.getTabs();
//		boolean blockSelection = styledText.getBlockSelection();

		write("<div style='" + outerDivStyle + "'>\n");
		if (language == null || language.isEmpty()) {
			write("<div style='" + innerDivStyle + "'>\n");
		} else {
			write("<div lang='" + language + "' style='" + innerDivStyle + "'>\n");
		}
	}

	/**
	 * Appends the specified line text to the HTML data. Lines will be formatted
	 * using the styles queried from the LineStyleListener, if set, or those set
	 * directly in the widget.
	 *
	 * @param line line text to write as HTML. Must not contain line breaks
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

		int lineAlignment;
		int lineIndent;
		boolean lineJustify;
		int verticalIndent;
		int[] ranges;
		StyleRange[] styles;
		Color lineBackground;

		StyledTextEvent event = styledText.getLineStyleData(lineOffset, line);
		if (event != null) {
			verticalIndent = event.verticalIndent;
			lineAlignment = event.alignment;
			lineIndent = event.indent;
			lineJustify = event.justify;
			ranges = event.ranges;
			styles = event.styles;
			lineBackground = event.lineBackground;
		} else {
			int lineIndex = styledText.content.getLineAtOffset(lineOffset);
			verticalIndent = styledText.renderer.getLineVerticalIndent(lineIndex);
			lineAlignment = styledText.renderer.getLineAlignment(lineIndex, styledText.alignment);
			lineIndent =  styledText.renderer.getLineIndent(lineIndex, styledText.indent);
			lineJustify = styledText.renderer.getLineJustify(lineIndex, styledText.justify);
			ranges = styledText.renderer.getRanges(lineOffset, line.length());
			styles = styledText.renderer.getStyleRanges(lineOffset, line.length(), false);
			lineBackground = styledText.renderer.getLineBackground(lineIndex, null);
		}
		if (styles == null) {
			styles = new StyleRange[0];
		}

		writeStyledLine(line, lineOffset, ranges, styles, lineBackground, lineIndent, verticalIndent, lineAlignment, lineJustify);
	}

	/**
	 * Appends the specified line delimiter to the HTML data.
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
		write(lineDelimiter, 0, lineDelimiter.length(), false);
	}

	/**
	 * Appends the specified line text to the HTML data.
	 *
	 * <p>Use the colors and font styles specified in @{code styles} and {@code lineBackground}.
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
	private void writeStyledLine(String line, int lineOffset, int ranges[], StyleRange[] styles,
			Color lineBackground, int indent, int verticalIndent, int alignment, boolean justify) {

		// Code based on the one in {@link RTFWriter}. There might be an opportunity for reuse.

		int lineLength = line.length();
		int startOffset = getStart();
		int writeOffset = startOffset - lineOffset;
		if (writeOffset >= lineLength) {
			return;
		}
		int lineIndex = Math.max(0, writeOffset);

		StringBuilder paragraphStyle = new StringBuilder();

		appendAlignAndJustify(paragraphStyle, alignment, justify);

		appendStyle(paragraphStyle, "background-color:", lineBackground, ";");

		if (indent != 0) {
			appendStyle(paragraphStyle, "text-indent:", indent, "px;");
		}

		if (verticalIndent != 0) {
			appendStyle(paragraphStyle, "margin-top:", verticalIndent, "px;");
		}

		if (paragraphStyle.length() == 0) {
			write("<p>");
		} else {
			write("<p style='" + paragraphStyle + "'>");
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
			// style starts beyond line end or HTML write end
			if (start >= lineEndOffset) {
				break;
			}
			// write any unstyled text
			if (lineIndex < start) {
				// copy to start of style
				// style starting beyond end of write range or end of line
				// is guarded against above.
				write(line, lineIndex, start, true);
				lineIndex = start;
			}
			// write styled text
			StringBuilder spanStyle = new StringBuilder();

			appendStyle(spanStyle, "color:", style.foreground, ";");
			appendStyle(spanStyle, "background-color:", style.background, ";");

			appendFont(spanStyle, style.font, style.fontStyle);

			if (style.rise != 0) {
				appendStyle(spanStyle, "position:relative;bottom:", style.rise, "pt;");
			}

			String borderColor = style.borderColor != null ? " " + colorToHex(style.borderColor) : "";
			switch (style.borderStyle) {
				case SWT.BORDER: // intentional fall-trough
					// In Eclipse the default border is solid
				case SWT.BORDER_SOLID:
					appendStyle(spanStyle, "border:solid 1pt", borderColor, ";");
					break;
				case SWT.BORDER_DASH:
					appendStyle(spanStyle, "border:dashed 1pt", borderColor, ";");
					break;
				case SWT.BORDER_DOT:
					appendStyle(spanStyle, "border:dotted 1pt", borderColor, ";");
					break;
				default:
					break;
			}

			if (style.underline) {
				appendStyle(spanStyle, "text-decoration:underline;");
				appendStyle(spanStyle, "text-decoration-color:", style.underlineColor, ";");
				switch (style.underlineStyle) {
					case SWT.UNDERLINE_SINGLE:
						appendStyle(spanStyle, "text-decoration-style:solid;");
						break;
					case SWT.UNDERLINE_DOUBLE:
						appendStyle(spanStyle, "text-decoration-style:double;");
						break;
					case SWT.UNDERLINE_ERROR:
						appendStyle(spanStyle, "text-decoration-style:wavy;");
						break;
					case SWT.UNDERLINE_SQUIGGLE:
						appendStyle(spanStyle, "text-decoration-style:wavy;");
						break;
					case SWT.UNDERLINE_LINK:
						appendStyle(spanStyle, "text-decoration-style:solid;");
						if (style.underlineColor == null) {
							// If the color of the link underline was not explicitly overridden, then is is blue.
							appendStyle(spanStyle, "text-decoration-color:#0066cc;");
						}
						if (style.foreground == null) {
							// If the color of the link text was not explicitly overridden, then is is blue.
							appendStyle(spanStyle, "color:#0066cc;");
						}
						break;
					default:
						break;
				}
			}

			// Both underline and line-through use text-decoration-color.
			// If we want the underline color to be different than the strikeout color we need two spans.
			StringBuilder spanStyle2;
			if (style.strikeout) {
				spanStyle2 = new StringBuilder();
				appendStyle(spanStyle2, "text-decoration:line-through;");
				appendStyle(spanStyle2, "text-decoration-color:", style.strikeoutColor, ";");
			} else {
				spanStyle2 = null;
			}

			if (spanStyle.length() != 0) {
				write("<span style='" + spanStyle + "'>");
			}
			if (spanStyle2 != null) {
				write("<span style='" + spanStyle2 + "'>");
			}

			// copy to end of style or end of write range or end of line
			int copyEnd = Math.min(end, lineEndOffset);
			// guard against invalid styles and let style processing continue
			copyEnd = Math.max(copyEnd, lineIndex);
			write(line, lineIndex, copyEnd, true);

			if (spanStyle2 != null) {
				write("</span>");
			}
			if (spanStyle.length() != 0) {
				write("</span>");
			}

			lineIndex = copyEnd;
		}

		// write the unstyled text at the end of the line
		if (lineIndex < lineEndOffset) {
			write(line, lineIndex, lineEndOffset, true);
		}
		write("</p>");
	}

	// TODO: do we also want support for alpha?
	private static String colorToHex(Color color) {
		if (color == null) {
			return null;
		}
		return String.format("#%02x%02x%02x",
				color.getRed(),
				color.getGreen(),
				color.getBlue());
	}

	private static String textToHtml(String text) {
		return text
				.replaceAll("&", "&amp;")
				.replaceAll("\"", "&quot;")
				.replaceAll("<", "&lt;")
				.replaceAll(">", "&gt;");
	}

	private static void appendStyle(StringBuilder buffer, String value) {
		buffer.append(value);
	}

	private static void appendStyle(StringBuilder buffer, String prefix, String value, String postfix) {
		buffer.append(prefix);
		buffer.append(value);
		buffer.append(postfix);
	}

	private static void appendStyle(StringBuilder buffer, String prefix, int value, String postfix) {
		buffer.append(prefix);
		buffer.append(value);
		buffer.append(postfix);
	}

	private static void appendStyle(StringBuilder buffer, String prefix, Color value, String postfix) {
		if (value != null) {
			buffer.append(prefix);
			buffer.append(colorToHex(value));
			buffer.append(postfix);
		}
	}

	// Return a locale id, if one was present in the font
	private static String appendFont(StringBuilder buffer, Font font, int fontStyle) {
		String language = null;

		if (font != null) {
			FontData[] fd = font.getFontData();
			if (fd != null && fd.length > 0) {
				FontData fdata = fd[0];
				String name = fdata.getName();
				if (name != null && !name.isEmpty()) {
					appendStyle(buffer, "font-family:\"", fdata.getName(), "\";");
				}
				appendStyle(buffer, "font-size:", fdata.getHeight(), "pt;");
				language = fdata.getLocale();
				// The style in the font wins.
				// Quote from {@link StyleRange#fontStyle}:
				// "Note: the font style is not used if the <code>font</code> attribute is set"
				fontStyle = fdata.getStyle();
			}
		}

		if (fontStyle != 0) {
			if ((fontStyle & SWT.ITALIC) == SWT.ITALIC) {
				appendStyle(buffer, "font-style:italic;");
			}
			if ((fontStyle & SWT.BOLD) == SWT.BOLD) {
				appendStyle(buffer, "font-weight:bold;");
			}
		}

		return language;
	}

	private static void appendAlignAndJustify(StringBuilder buffer, int alignment, boolean justify) {
		// `text-align` is used for general paragraph alignment, but when it is used for justify
		// we need to set `text-align-last` to control the behavior of the last line.
		String textAlignKey;
		if (justify) {
			appendStyle(buffer, "text-align:justify;");
			textAlignKey = "text-align-last:";
		} else {
			textAlignKey = "text-align:";
		}
		switch (alignment) {
		case SWT.LEFT:
				// This is the default, no need to write anything out.
				break;
			case SWT.CENTER:
				appendStyle(buffer, textAlignKey, "center", ";");
				break;
			case SWT.RIGHT:
				appendStyle(buffer, textAlignKey, "right", ";");
				break;
			default:
				break;
		}
	}
}