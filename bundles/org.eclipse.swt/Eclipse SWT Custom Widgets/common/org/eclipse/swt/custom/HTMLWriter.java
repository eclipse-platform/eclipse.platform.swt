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

/**
 * The <code>HTMLWriter</code> class is used to write styled content as HTML.
 * The implementation creates a very-very simple HTML, as compatible
 * as possible: simple styles, no JavaScript.
 *
 * <p>How the concepts are mapped to HTML:</p>
 * <ul>
 *   <li>The global {@link StyledText} properties (for example {@link StyledText#getBackground()}
 *       are written in a wrapping {@code <div>} or {@code <span>} (for line fragments).</p>
 *   <li>The line specific properties (for example {@link StyledText#getLineBackground(int)}
 *       are written the {@code <p>} elements - if the copied range contains more then a line fragment.</li>
 *   <li>The {@link StyleRange} properties inside the line are written the {@code <span>} elements.</li>
 * </ul>
 */
// TODO (visjee) SWING
class HTMLWriter extends StyledTextWriterBase {

	public HTMLWriter(int start, int length) {
		super(start, length);
		// TODO Auto-generated constructor stub
	}
//	private String tag;
//	private boolean multiline;
//
//	public HTMLWriter(StyledText styledText, int start, int length, StyledTextContent content) {
//		super(styledText, start, length);
//		String text = content.getTextRange(start, length);
//		multiline = text.contains("\n");
//		tag = multiline ? "div" : "span";
//		writeHeader();
//	}
//
//	@Override
//	public void close() {
//		if (!isClosed()) {
//			write("</"+ tag+">");
//			write("</"+ tag+">");
//			super.close();
//		}
//	}
//
//	@Override
//	void writeHeader() {
//		StringBuilder outerDivStyle = new StringBuilder();
//		StringBuilder innerDivStyle = new StringBuilder();
//
//		appendStyle(outerDivStyle, "background-color:", styledText.getMarginColor(), ";");
//		appendStyle(innerDivStyle, "color:", styledText.getForeground(), ";");
//		appendStyle(innerDivStyle, "background-color:", styledText.getBackground(), ";");
//
//		appendStyle(outerDivStyle, "padding:"
//						+ styledText.getTopMargin() + "px "
//						+ styledText.getRightMargin() + "px "
//						+ styledText.getBottomMargin() + "px "
//						+ styledText.getLeftMargin() + "px;");
//
//		String language = appendFont(innerDivStyle, styledText.getFont(), 0);
//
//		int indent = styledText.getIndent();
//		if (indent != 0) {
//			appendStyle(innerDivStyle, "text-indent:", indent, "px;");
//		}
//
//		if (styledText.getWordWrap()) {
//			// Sequences of white space are preserved.
//			// Lines are broken at newline characters, at <br>, and as necessary to fill line boxes.
//			appendStyle(innerDivStyle, "white-space:pre-wrap;");
//		} else {
//			// Sequences of white space are preserved.
//			// Lines are only broken at newline characters in the source and at <br> elements.
//			appendStyle(innerDivStyle, "white-space:pre;");
//		}
//
//		appendAlignAndJustify(innerDivStyle, styledText.getAlignment(), styledText.getJustify());
//
//		if (styledText.getOrientation() == SWT.RIGHT_TO_LEFT || styledText.getTextDirection() == SWT.RIGHT_TO_LEFT) {
//			appendStyle(innerDivStyle, "direction:rtl;");
//		}
//
//		write("<"+ tag+" style='" + outerDivStyle + "'>");
//		if (language == null || language.isEmpty()) {
//			write("<"+ tag+" style='" + innerDivStyle + "'>");
//		} else {
//			write("<"+ tag+" lang='" + language + "' style='" + innerDivStyle + "'>");
//		}
//	}
//
//	@Override
//	public void writeLineDelimiter(String lineDelimiter) {
//		if (isClosed()) {
//			SWT.error(SWT.ERROR_IO);
//		}
//		// We don't write the newline, otherwise it would be rendered because of the `white-space:pre;`.
//	}
//
//	@Override
//	String writeLineStart(Color lineBackground, int indent, int verticalIndent, int alignment, boolean justify) {
//		StringBuilder paragraphStyle = new StringBuilder();
//
//		appendAlignAndJustify(paragraphStyle, alignment, justify);
//
//		appendStyle(paragraphStyle, "background-color:", lineBackground, ";");
//
//		if (indent != 0) {
//			appendStyle(paragraphStyle, "text-indent:", indent, "px;");
//		}
//
//		if (verticalIndent == 0) {
//			appendStyle(paragraphStyle, "margin:0;");
//		} else {
//			// Set the margin-top to verticalIndent, everything else zero.
//			appendStyle(paragraphStyle, "margin:", verticalIndent, " 0 0 0;");
//		}
//
//		if (!multiline) {
//			return "";
//		}
//		if (paragraphStyle.length() == 0) {
//			write("<p>");
//		} else {
//			write("<p style='" + paragraphStyle + "'>");
//		}
//
//		// This is what will be used to close the line
//		return "</p>";
//	}
//
//	@Override
//	void writeEmptyLine() {
//		write("<br>");
//	}
//
//	@Override
//	String writeSpanStart(StyleRange style) {
//		StringBuilder spanStyle = new StringBuilder();
//
//		appendStyle(spanStyle, "color:", style.foreground, ";");
//		appendStyle(spanStyle, "background-color:", style.background, ";");
//
//		appendFont(spanStyle, style.font, style.fontStyle);
//
//		if (style.rise != 0) {
//			appendStyle(spanStyle, "position:relative;bottom:", style.rise, "pt;");
//		}
//
//		String borderColor = style.borderColor != null ? " " + colorToHex(style.borderColor) : "";
//		switch (style.borderStyle) {
//			case SWT.BORDER: // intentional fall-trough
//				// In Eclipse the default border is solid
//			case SWT.BORDER_SOLID:
//				appendStyle(spanStyle, "border:solid 1pt", borderColor, ";");
//				break;
//			case SWT.BORDER_DASH:
//				appendStyle(spanStyle, "border:dashed 1pt", borderColor, ";");
//				break;
//			case SWT.BORDER_DOT:
//				appendStyle(spanStyle, "border:dotted 1pt", borderColor, ";");
//				break;
//			default:
//				break;
//		}
//
//		if (style.underline) {
//			appendStyle(spanStyle, "text-decoration:underline;");
//			appendStyle(spanStyle, "text-decoration-color:", style.underlineColor, ";");
//			switch (style.underlineStyle) {
//				case SWT.UNDERLINE_SINGLE:
//					appendStyle(spanStyle, "text-decoration-style:solid;");
//					break;
//				case SWT.UNDERLINE_DOUBLE:
//					appendStyle(spanStyle, "text-decoration-style:double;");
//					break;
//				case SWT.UNDERLINE_ERROR:
//					appendStyle(spanStyle, "text-decoration-style:wavy;");
//					break;
//				case SWT.UNDERLINE_SQUIGGLE:
//					appendStyle(spanStyle, "text-decoration-style:wavy;");
//					break;
//				case SWT.UNDERLINE_LINK:
//					appendStyle(spanStyle, "text-decoration-style:solid;");
//					if (style.underlineColor == null) {
//						// If the color of the link underline was not explicitly overridden, then is is blue.
//						appendStyle(spanStyle, "text-decoration-color:#0066cc;");
//					}
//					if (style.foreground == null) {
//						// If the color of the link text was not explicitly overridden, then is is blue.
//						appendStyle(spanStyle, "color:#0066cc;");
//					}
//					break;
//				default:
//					break;
//			}
//		}
//
//		// Both underline and line-through use text-decoration-color.
//		// If we want the underline color to be different than the strikeout color we need two spans.
//		StringBuilder spanStyle2;
//		if (style.strikeout) {
//			spanStyle2 = new StringBuilder();
//			appendStyle(spanStyle2, "text-decoration:line-through;");
//			appendStyle(spanStyle2, "text-decoration-color:", style.strikeoutColor, ";");
//		} else {
//			spanStyle2 = null;
//		}
//
//		if (spanStyle.length() != 0) {
//			write("<span style='" + spanStyle + "'>");
//		}
//		if (spanStyle2 != null) {
//			write("<span style='" + spanStyle2 + "'>");
//		}
//
//		// This is what will be used to close the span
//		StringBuilder toCloseSpan = new StringBuilder();
//		if (spanStyle2 != null) {
//			toCloseSpan.append("</span>");
//		}
//		if (spanStyle.length() != 0) {
//			toCloseSpan.append("</span>");
//		}
//		return toCloseSpan.toString();
//	}
//
//	// ==== Helper methods ====
//
//	@Override
//	String escapeText(String string) {
//		// The resulting string is at least as long as the original.
//		StringBuilder result = new StringBuilder(string.length());
//		string.chars().forEach(ch -> {
//			switch (ch) {
//				case '&':
//					result.append("&amp;");
//					break;
//				case '"':
//					result.append("&quot;");
//					break;
//				case '<':
//					result.append("&lt;");
//					break;
//				case '>':
//					result.append("&gt;");
//					break;
//				default:
//					result.append((char) ch);
//			}
//		});
//		return result.toString();
//	}
//
//	// TODO: do we also want support for alpha?
//	private static String colorToHex(Color color) {
//		if (color == null) {
//			return null;
//		}
//		return String.format("#%02x%02x%02x",
//				color.getRed(),
//				color.getGreen(),
//				color.getBlue());
//	}
//
//	private static void appendStyle(StringBuilder buffer, String value) {
//		buffer.append(value);
//	}
//
//	private static void appendStyle(StringBuilder buffer, String prefix, String value, String postfix) {
//		buffer.append(prefix);
//		buffer.append(value);
//		buffer.append(postfix);
//	}
//
//	private static void appendStyle(StringBuilder buffer, String prefix, int value, String postfix) {
//		buffer.append(prefix);
//		buffer.append(value);
//		buffer.append(postfix);
//	}
//
//	private static void appendStyle(StringBuilder buffer, String prefix, Color value, String postfix) {
//		if (value != null) {
//			buffer.append(prefix);
//			buffer.append(colorToHex(value));
//			buffer.append(postfix);
//		}
//	}
//
//	// Return a locale id, if one was present in the font
//	private static String appendFont(StringBuilder buffer, Font font, int fontStyle) {
//		String language = null;
//
//		if (font != null) {
//			FontData[] fd = font.getFontData();
//			if (fd != null && fd.length > 0) {
//				FontData fdata = fd[0];
//				String name = fdata.getName();
//				if (name != null && !name.isEmpty()) {
//					appendStyle(buffer, "font-family:\"", fdata.getName(), "\";");
//				}
//				appendStyle(buffer, "font-size:", fdata.getHeight(), "pt;");
//				language = fdata.getLocale();
//				// The style in the font wins.
//				// Quote from {@link StyleRange#fontStyle}:
//				// "Note: the font style is not used if the <code>font</code> attribute is set"
//				fontStyle = fdata.getStyle();
//			}
//		}
//
//		if (fontStyle != 0) {
//			if ((fontStyle & SWT.ITALIC) == SWT.ITALIC) {
//				appendStyle(buffer, "font-style:italic;");
//			}
//			if ((fontStyle & SWT.BOLD) == SWT.BOLD) {
//				appendStyle(buffer, "font-weight:bold;");
//			}
//		}
//
//		return language;
//	}
//
//	private static void appendAlignAndJustify(StringBuilder buffer, int alignment, boolean justify) {
//		// `text-align` is used for general paragraph alignment, but when it is used for justify
//		// we need to set `text-align-last` to control the behavior of the last line.
//		String textAlignKey;
//		if (justify) {
//			appendStyle(buffer, "text-align:justify;");
//			textAlignKey = "text-align-last:";
//		} else {
//			textAlignKey = "text-align:";
//		}
//		switch (alignment) {
//		case SWT.LEFT:
//				// This is the default, no need to write anything out.
//				break;
//			case SWT.CENTER:
//				appendStyle(buffer, textAlignKey, "center", ";");
//				break;
//			case SWT.RIGHT:
//				appendStyle(buffer, textAlignKey, "right", ";");
//				break;
//			default:
//				break;
//		}
//	}
}
