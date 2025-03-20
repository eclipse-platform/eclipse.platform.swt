/*******************************************************************************
 * Copyright (c) 2025 Advantest Europe GmbH and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 * 				Raghunandana Murthappa
 *******************************************************************************/
package org.eclipse.swt.widgets;

import java.util.*;
import java.util.List;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;

public abstract class LinkRenderer extends ControlRenderer {
	public abstract boolean isOverLink(int x, int y);

	protected static final int DRAW_FLAGS = SWT.DRAW_MNEMONIC | SWT.DRAW_TAB | SWT.DRAW_TRANSPARENT
			| SWT.DRAW_DELIMITER;

	protected final List<List<TextSegment>> parsedText = new ArrayList<>();
	protected Link link;

	public static class TextSegment {
		public final String text;
		public final String linkData;
		public Rectangle rect;

		private TextSegment(String text, String linkData) {
			this.text = text;
			this.linkData = linkData;
		}

		public boolean isLink() {
			return linkData != null;
		}
	}

	protected LinkRenderer(Link control) {
		super(control);
		this.link = control;
	}

	/**
	 * Parses the link text into list of TextSegment. It is a split of text into
	 * lines and then each line is processed for Anchor tags. If Anchor tag is
	 * present then corresponding data is extracted for rendering.
	 *
	 * @param text       Raw link text set to Link widget. It contains anchor tags
	 *                   which are drawn as hyperlink on Link widget.
	 */
	public void parseLinkText(String text) {
		parsedText.clear();

	    List<TextSegment> currentLineSegments = new ArrayList<>();
	    StringBuilder buffer = new StringBuilder();
	    StringBuilder linkBuffer = new StringBuilder();
	    String href = null;
	    boolean inAnchor = false;

		for (int i = 0; i < text.length(); i++) {
	        char c = text.charAt(i);

			if (c == '<') {
				if (text.startsWith("<a", i)) {
					if (buffer.length() > 0) {
						currentLineSegments.add(new TextSegment(buffer.toString(), null));
						buffer.setLength(0);
					}
					inAnchor = true;
					href = null;
					int hrefStart = text.indexOf("href=\"", i);
					if (hrefStart != -1 && hrefStart < text.indexOf(">", i)) {
						int hrefEnd = text.indexOf("\"", hrefStart + 6);
						if (hrefEnd != -1) {
							href = text.substring(hrefStart + 6, hrefEnd);
							i = hrefEnd;
						}
					}
					i = text.indexOf(">", i);
					continue;
				}

				if (inAnchor && text.startsWith("</a>", i)) {
					final String linkText = linkBuffer.toString();
					currentLineSegments.add(new TextSegment(linkText, href != null ? href : linkText));
					linkBuffer.setLength(0);
					inAnchor = false;
					i += 3;
					continue;
				}
			}

	        if (c == '\n') {
	            if (buffer.length() > 0) {
	                currentLineSegments.add(new TextSegment(buffer.toString(), null));
	                buffer.setLength(0);
	            }
	            if (linkBuffer.length() > 0) {
	                currentLineSegments.add(new TextSegment(linkBuffer.toString(), href));
	                linkBuffer.setLength(0);
	            }
	            parsedText.add(currentLineSegments);
	            currentLineSegments = new ArrayList<>();
	            continue;
	        }

	        if (inAnchor) {
	            linkBuffer.append(c);
	        } else {
	            buffer.append(c);
	        }
	    }

	    if (buffer.length() > 0) {
	        currentLineSegments.add(new TextSegment(buffer.toString(), null));
	    }
	    if (linkBuffer.length() > 0) {
	        currentLineSegments.add(new TextSegment(linkBuffer.toString(), href));
	    }
	    if (!currentLineSegments.isEmpty()) {
	        parsedText.add(currentLineSegments);
	    }
	}

	/**
	 * Returns the display text that can be displayed on Link widget.
	 *
	 * @return displayText The text that is displayed on Link widget.
	 */
	public String getLinkDisplayText() {
		StringBuilder sb = new StringBuilder();
		boolean firstLine = true;
		for (List<TextSegment> segments : parsedText) {
			if (!firstLine) {
				sb.append("\n");
			}
			firstLine = false;

			for (TextSegment segment : segments) {
				sb.append(segment.text);
			}
		}
		return sb.toString();
	}

	public List<List<TextSegment>> getParsedSegments() {
		return parsedText;
	}

	public abstract Set<TextSegment> getLinks();

	public abstract Point computeDefaultSize();
}
