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
 * The {@code RTFWriter} class is used to write styled content as rich text.
 * The implementation complies with the RTF specification version 1.5.
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
class RTFWriter extends StyledTextWriterBase {

	public RTFWriter(int start, int length) {
		super(start, length);
		// TODO Auto-generated constructor stub
	}
//	static final int DEFAULT_FOREGROUND = 0;
//	static final int DEFAULT_BACKGROUND = 1;
//	List<Color> colorTable;
//	List<Font> fontTable;
//
//	public RTFWriter(StyledText styledText, int start, int length) {
//		super(styledText, start, length);
//		colorTable = new ArrayList<>();
//		fontTable = new ArrayList<>();
//		colorTable.add(this.styledText.getForeground());
//		colorTable.add(this.styledText.getBackground());
//		fontTable.add(this.styledText.getFont());
//	}
//
//	@Override
//	public void close() {
//		if (!isClosed()) {
//			writeHeader();
//			write("\n}}\0");
//			super.close();
//		}
//	}
//
//	/**
//	 * Writes the RTF header including font table and color table.
//	 */
//	@Override
//	void writeHeader() {
//		StringBuilder header = new StringBuilder();
//		FontData fontData = styledText.getFont().getFontData()[0];
//		header.append("{\\rtf1\\ansi");
//		// specify code page, necessary for copy to work in bidi
//		// systems that don't support Unicode RTF.
//		String cpg = System.getProperty("file.encoding").toLowerCase();
//		if (cpg.startsWith("cp") || cpg.startsWith("ms")) {
//			cpg = cpg.substring(2, cpg.length());
//			header.append("\\ansicpg");
//			header.append(cpg);
//		}
//		header.append("\\uc1\\deff0{\\fonttbl{\\f0\\fnil ");
//		header.append(fontData.getName());
//		header.append(";");
//		for (int i = 1; i < fontTable.size(); i++) {
//			header.append("\\f");
//			header.append(i);
//			header.append(" ");
//			FontData fd = fontTable.get(i).getFontData()[0];
//			header.append(fd.getName());
//			header.append(";");
//		}
//		header.append("}}\n{\\colortbl");
//		for (Color color : colorTable) {
//			header.append("\\red");
//			header.append(color.getRed());
//			header.append("\\green");
//			header.append(color.getGreen());
//			header.append("\\blue");
//			header.append(color.getBlue());
//			header.append(";");
//		}
//		// some RTF readers ignore the deff0 font tag. Explicitly
//		// set the font for the whole document to work around this.
//		header.append("}\n{\\f0\\fs");
//		// font size is specified in half points
//		header.append(fontData.getHeight() * 2);
//		header.append(" ");
//		write(header.toString(), 0);
//	}
//
//	@Override
//	public void writeLineDelimiter(String lineDelimiter) {
//		if (isClosed()) {
//			SWT.error(SWT.ERROR_IO);
//		}
//		write(lineDelimiter);
//		write("\\par ");
//	}
//
//	@Override
//	String writeLineStart(Color lineBackground, int indent, int verticalIndent, int alignment, boolean justify) {
//		write("\\fi");
//		write(indent);
//		switch (alignment) {
//			case SWT.LEFT: write("\\ql"); break;
//			case SWT.CENTER: write("\\qc"); break;
//			case SWT.RIGHT: write("\\qr"); break;
//		}
//		if (justify) {
//			write("\\qj");
//		}
//		write(" ");
//
//		if (lineBackground != null) {
//			// Background colors are written using the {@code \chshdng0\chcbpat} tag (vs. the {@code \cb} tag).
//			write("{\\chshdng0\\chcbpat");
//			write(getColorIndex(lineBackground, DEFAULT_BACKGROUND));
//			write(" ");
//		}
//
//		// This is what will be used to close the line
//		return lineBackground == null ? "" : "}";
//	}
//
//	@Override
//	void writeEmptyLine() {
//		// Do nothing. RTF does not need special treatment for empty paragraph.
//	}
//
//	@Override
//	String writeSpanStart(StyleRange style) {
//		write("{\\cf");
//		write(getColorIndex(style.foreground, DEFAULT_FOREGROUND));
//		int colorIndex = getColorIndex(style.background, DEFAULT_BACKGROUND);
//		if (colorIndex != DEFAULT_BACKGROUND) {
//			write("\\chshdng0\\chcbpat");
//			write(colorIndex);
//		}
//		int fontStyle = style.fontStyle;
//		Font font = style.font;
//		if (font != null) {
//			int fontIndex = getFontIndex(font);
//			write("\\f");
//			write(fontIndex);
//			FontData fontData = font.getFontData()[0];
//			write("\\fs");
//			write(fontData.getHeight() * 2);
//			fontStyle = fontData.getStyle();
//		}
//		if ((fontStyle & SWT.BOLD) != 0) {
//			write("\\b");
//		}
//		if ((fontStyle & SWT.ITALIC) != 0) {
//			write("\\i");
//		}
//		if (style.underline) {
//			write("\\ul");
//		}
//		if (style.strikeout) {
//			write("\\strike");
//		}
//		write(" ");
//
//		// This is what will be used to close the span
//		StringBuilder toCloseSpan = new StringBuilder();
//		if ((fontStyle & SWT.BOLD) != 0) {
//			toCloseSpan.append("\\b0");
//		}
//		if ((style.fontStyle & SWT.ITALIC) != 0) {
//			toCloseSpan.append("\\i0");
//		}
//		if (style.underline) {
//			toCloseSpan.append("\\ul0");
//		}
//		if (style.strikeout) {
//			toCloseSpan.append("\\strike0");
//		}
//		toCloseSpan.append("}");
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
//			if (ch > 0x7F) {
//				result.append("\\u");
//				result.append(Integer.toString((short) ch));
//				result.append('?'); // ANSI representation (1 byte long, \\uc1)
//			} else if (ch == '}' || ch == '{' || ch == '\\') {
//				result.append('\\');
//				result.append((char) ch);
//			} else {
//				// Fixes bug 21698.
//				result.append((char) ch);
//			}
//		});
//		return result.toString();
//	}
//
//	/**
//	 * Returns the index of the specified color in the RTF color table.
//	 *
//	 * @param color the color
//	 * @param defaultIndex return value if color is null
//	 * @return the index of the specified color in the RTF color table
//	 *  or "defaultIndex" if "color" is null.
//	 */
//	private int getColorIndex(Color color, int defaultIndex) {
//		if (color == null) return defaultIndex;
//		int index = colorTable.indexOf(color);
//		if (index == -1) {
//			index = colorTable.size();
//			colorTable.add(color);
//		}
//		return index;
//	}
//
//	/**
//	 * Returns the index of the specified color in the RTF color table.
//	 *
//	 * @param color the color
//	 * @param defaultIndex return value if color is null
//	 * @return the index of the specified color in the RTF color table
//	 *  or "defaultIndex" if "color" is null.
//	 */
//	private int getFontIndex(Font font) {
//		int index = fontTable.indexOf(font);
//		if (index == -1) {
//			index = fontTable.size();
//			fontTable.add(font);
//		}
//		return index;
//	}
}
