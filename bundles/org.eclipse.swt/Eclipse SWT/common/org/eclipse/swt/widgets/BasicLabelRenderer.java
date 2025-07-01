/*******************************************************************************
 * Copyright (c) 2024-2025 SAP, Syntevo GmbH and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Denis Ungemach (SAP)
 *     Thomas Singer (Syntevo)
 *******************************************************************************/
package org.eclipse.swt.widgets;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;

class BasicLabelRenderer extends LabelRenderer {

	private static final int DRAW_FLAGS = SWT.DRAW_MNEMONIC | SWT.DRAW_TAB
										  | SWT.DRAW_TRANSPARENT | SWT.DRAW_DELIMITER;

	private static final int IMAGE_TEXT_GAP = 5;

	public BasicLabelRenderer(Label label) {
		super(label);
	}

	@Override
	public Point computeDefaultSize() {
		final String text = label.getText();
		final Image image = label.getImage();

		final int leftMargin = getLeftMargin();
		final int topMargin = getTopMargin();
		final int rightMargin = getRightMargin();
		final int bottomMargin = getBottomMargin();

		int width = 0;
		int height = 0;

		if (!text.isEmpty()) {
			Point textExtent = getTextExtent(text, DRAW_FLAGS);
			width = textExtent.x;
			height = textExtent.y;
		}
		if (image != null) {
			if (width > 0) {
				width += IMAGE_TEXT_GAP;
			}
			Rectangle imgB = image.getBounds();
			width += imgB.width;
			height = Math.max(height, imgB.height);
		}

		return new Point(leftMargin + width + rightMargin,
				topMargin + height + bottomMargin);
	}

	@Override
	protected void paint(GC gc, int width, int height) {
		final String text = label.getText();
		Image image = label.getImage();
		if (text.isEmpty() && image == null) {
			return;
		}

		final int leftMargin = getLeftMargin();
		final int topMargin = getTopMargin();
		final int rightMargin = getRightMargin();
		final int bottomMargin = getBottomMargin();

		int availableWidth = Math.max(0, width - leftMargin - rightMargin);
		String[] lines = null;
		Point extent = getTotalSize(gc, image, text);
		boolean shortenText = false;
		boolean wrap =isWrap();
		TextLayout layout=null;
		try {
		if (!text.isEmpty()) {
			if (wrap) {
				layout = new TextLayout(label.getDisplay());
				layout.setFont(gc.getFont());
				layout.setText(text);
				layout.setWidth(availableWidth);
			} else {
				lines = splitString(text);
			}
		}
		if (!wrap && lines != null && extent.x > availableWidth) {
			if (image != null) {
				image = null;
				extent = getTotalSize(gc, null, text);
			}
			if (extent.x > availableWidth) {
				shortenText = true;
			}
		}

		if (wrap && shortenText) {
			if (layout != null) {
				lines = extractWrappedLines(layout, text);
			}
			extent.x = 0;
			for (int i = 0; i < lines.length; i++) {
				int lineWidth = gc.textExtent(lines[i], DRAW_FLAGS).x;
				if (lineWidth > availableWidth /* && !wrap */) {
					lines[i] = shortenText(gc, lines[i], availableWidth, DRAW_FLAGS);
					lineWidth = getTotalSize(gc, null, lines[i]).x;
				}
				extent.x = Math.max(extent.x, lineWidth);
			}
			if (getToolTipText() == null) {
				setDisplayedToolTip(text);
			}
		} else {
			setDisplayedToolTip(getToolTipText());
		}

		// determine horizontal position
		int x = leftMargin;
		final int align = getAlign();
		if (align == SWT.CENTER) {
			x = (width - extent.x) / 2;
		}
		else if (align == SWT.RIGHT) {
			x = width - rightMargin - extent.x;
		}

		int style = label.getStyle();
		drawBackground(gc, width, height, style);

		// draw border
		if ((style & SWT.SHADOW_IN) != 0 || (style & SWT.SHADOW_OUT) != 0) {
			paintBorder(gc, width, height);
		}

		/*
		 * Compute text height and image height. If image height is more than
		 * the text height, draw image starting from top margin. Else draw text
		 * starting from top margin.
		 */
		Rectangle imageRect = null;
		int lineHeight = 0;
		int textHeight = 0;
		int imageHeight = 0;

		if (image != null) {
			imageRect = image.getBounds();
			imageHeight = imageRect.height;
		}if (wrap && layout != null) {
			lineHeight = layout.getLineBounds(0).height;
			textHeight = layout.getBounds().height;
		} else if (lines != null) {
			lineHeight = gc.getFontMetrics().getHeight();
			textHeight = lines.length * lineHeight;
		}

		// draw the image
		if (image != null) {
			int imageY;
			if (imageHeight > textHeight) {
				imageY = (height - topMargin - bottomMargin - imageHeight) / 2 + topMargin;
			} else {
				int lineY;
				if (topMargin == DEFAULT_MARGIN && bottomMargin == DEFAULT_MARGIN) {
					lineY = (textHeight - imageHeight) / 2;
				} else {
					lineY = topMargin;
				}
				int midPoint = lineY + textHeight / 2;
				imageY = midPoint - imageHeight / 2;
			}

			gc.drawImage(image, 0, 0, imageRect.width, imageHeight, x, imageY,
					imageRect.width, imageHeight);
			x += imageRect.width + IMAGE_TEXT_GAP;
			extent.x -= imageRect.width + IMAGE_TEXT_GAP;
		}

		// draw the text
		if (wrap && layout != null) {
			int lineY = topMargin;
			if (textHeight < imageHeight) {
				lineY = (imageHeight - textHeight) / 2;
			}
			gc.setForeground(label.isEnabled() ? label.getForeground() : getColor(COLOR_DISABLED));
			gc.setAntialias(SWT.ON);
			gc.setTextAntialias(SWT.ON);
			layout.draw(gc, x, lineY);
		} else if (lines != null) {
			// we draw the label at the top.
			int lineY = topMargin;

			if (textHeight < imageHeight) {
				lineY = (imageHeight - textHeight) / 2;
			}

			gc.setForeground(label.isEnabled() ? label.getForeground() : getColor(COLOR_DISABLED));
			for (String line : lines) {
				int lineX = x;
				if (lines.length > 1) {
					if (align == SWT.CENTER) {
						int lineWidth = gc.textExtent(line, DRAW_FLAGS).x;
						lineX = x + Math.max(0, (extent.x - lineWidth) / 2);
					}
					if (align == SWT.RIGHT) {
						int lineWidth = gc.textExtent(line, DRAW_FLAGS).x;
						lineX = Math.max(x, width - rightMargin - lineWidth);
					}
				}
				gc.drawText(line, lineX, lineY, DRAW_FLAGS);
				lineY += lineHeight;
			}
		}
	} finally {
			if (layout != null) {
				layout.dispose();
			}
		}
	}

	private void drawBackground(GC gc, int width, int height, int style) {
		final Color background = label.getBackground();
		final Image backgroundImage = getBackgroundImage();
		final Color[] gradientColors = getGradientColors();
		final int[] gradientPercents = getGradientPercents();
		// draw a background image behind the text
		try {
			if (backgroundImage != null) {
				drawBackgroundImage(gc, width, height, backgroundImage, background);
			} else if (gradientColors != null) {
				drawBackgroundGradient(gc, width, height, gradientColors, gradientPercents, background);
			} else {
				fillBackground(gc, width, height, background);
			}
		} catch (SWTException e) {
			if ((style & SWT.DOUBLE_BUFFERED) == 0) {
				fillBackground(gc, width, height, background);
			}
		}
	}

	private void drawBackgroundImage(GC gc, int width, int height, Image backgroundImage, Color background) {
		// draw a background image behind the text
		Rectangle imageRect = backgroundImage.getBounds();
		// tile image to fill space
		gc.setBackground(background);
		gc.fillRectangle(0, 0, width, height);
		int xPos = 0;
		while (xPos < width) {
			int yPos = 0;
			while (yPos < height) {
				gc.drawImage(backgroundImage, xPos, yPos);
				yPos += imageRect.height;
			}
			xPos += imageRect.width;
		}
	}

	private void drawBackgroundGradient(GC gc, int width, int height, Color[] gradientColors, int[] gradientPercents, Color background) {
		final Color oldBackground = gc.getBackground();
		if (gradientColors.length == 1) {
			if (gradientColors[0] != null) {
				gc.setBackground(gradientColors[0]);
			}
			gc.fillRectangle(0, 0, width, height);
		} else {
			final boolean gradientVertical = isGradientVertical();
			final Color oldForeground = gc.getForeground();
			Color lastColor = gradientColors[0];
			if (lastColor == null) {
				lastColor = oldBackground;
			}
			int pos = 0;
			for (int i = 0; i < gradientPercents.length; ++i) {
				gc.setForeground(lastColor);
				lastColor = gradientColors[i + 1];
				if (lastColor == null) {
					lastColor = oldBackground;
				}
				gc.setBackground(lastColor);
				if (gradientVertical) {
					final int gradientHeight = (gradientPercents[i]
					                            * height / 100) - pos;
					gc.fillGradientRectangle(0, pos, width,
					                         gradientHeight, true);
					pos += gradientHeight;
				} else {
					final int gradientWidth = (gradientPercents[i]
					                           * width / 100) - pos;
					gc.fillGradientRectangle(pos, 0, gradientWidth,
					                         height, false);
					pos += gradientWidth;
				}
			}
			if (gradientVertical && pos < height) {
				gc.setBackground(background);
				gc.fillRectangle(0, pos, width, height - pos);
			}
			if (!gradientVertical && pos < width) {
				gc.setBackground(background);
				gc.fillRectangle(pos, 0, width - pos, height);
			}
			gc.setForeground(oldForeground);
		}
	}

	private void fillBackground(GC gc, int width, int height, Color background) {
		if (background.getAlpha() > 0) {
			gc.setBackground(background);
			gc.fillRectangle(0, 0, width, height);
		}
	}

	/**
	 * Paint the Label's border.
	 */
	private void paintBorder(GC gc, int width, int height) {
		Color c1 = null;
		Color c2 = null;

		int style = label.getStyle();
		if ((style & SWT.SHADOW_IN) != 0) {
			c1 = getColor(COLOR_SHADOW_IN1);
			c2 = getColor(COLOR_SHADOW_IN2);
		}
		if ((style & SWT.SHADOW_OUT) != 0) {
			c1 = getColor(COLOR_SHADOW_OUT1);
			c2 = getColor(COLOR_SHADOW_OUT2);
		}

		if (c1 != null && c2 != null) {
			gc.setLineWidth(1);
			drawBevelRect(gc, 0, 0, width - 1, height - 1, c1, c2);
		}
	}

	/**
	 * Compute the minimum size.
	 */
	private Point getTotalSize(GC gc, Image image, String text) {
		Point size = new Point(0, 0);

		if (image != null) {
			Rectangle r = image.getBounds();
			size.x += r.width;
			size.y += r.height;
		}

		if (text != null && text.length() > 0) {
			Point e = gc.textExtent(text, DRAW_FLAGS);
			size.x += e.x;
			size.y = Math.max(size.y, e.y);
			if (image != null) {
				size.x += IMAGE_TEXT_GAP;
			}
		} else {
			size.y = Math.max(size.y, gc.getFontMetrics().getHeight());
		}

		return size;
	}

	private String[] splitString(String text) {
		String[] lines = new String[1];
		int start = 0, pos;
		do {
			pos = text.indexOf('\n', start);
			if (pos == -1) {
				lines[lines.length - 1] = text.substring(start);
			} else {
				boolean crlf = (pos > 0) && (text.charAt(pos - 1) == '\r');
				lines[lines.length - 1] = text.substring(start,
						pos - (crlf ? 1 : 0));
				start = pos + 1;
				String[] newLines = new String[lines.length + 1];
				System.arraycopy(lines, 0, newLines, 0, lines.length);
				lines = newLines;
			}
		} while (pos != -1);
		return lines;
	}

	/**
	 * Draw a rectangle in the given colors.
	 */
	private void drawBevelRect(GC gc, int x, int y, int w, int h,
			Color topleft, Color bottomright) {
		gc.setForeground(bottomright);
		gc.drawLine(x + w, y, x + w, y + h);
		gc.drawLine(x, y + h, x + w, y + h);

		gc.setForeground(topleft);
		gc.drawLine(x, y, x + w - 1, y);
		gc.drawLine(x, y, x, y + h - 1);
	}

	/**
     * Extracts wrapped lines from a TextLayout for the given text.
     */
    private static String[] extractWrappedLines(TextLayout layout, String text) {
        if (layout == null || text == null){
        	return new String[0];
        }
        int[] lineOffsets = layout.getLineOffsets();
        int lineCount = layout.getLineCount();
        String[] lines = new String[lineCount];
        for (int i = 0; i < lineCount; i++) {
            int start = lineOffsets[i];
            int end = (i + 1 < lineOffsets.length) ? lineOffsets[i + 1] : text.length();
            String line = text.substring(start, end).replaceAll("\n$", "");
            lines[i] = line;
        }
        return lines;
    }

	@Override
	protected String shortenText(GC gc, String t, int width, int drawFlags) {
		if (isWrap()) {
			TextLayout layout = new TextLayout(label.getDisplay());
			layout.setFont(gc.getFont());
			layout.setText(t);
			layout.setWidth(width);
			String[] lines = extractWrappedLines(layout, t);
			StringBuilder wrapped = new StringBuilder();
			for (int i = 0; i < lines.length; i++) {
				if (i > 0) wrapped.append("\n");
				wrapped.append(lines[i]);
			}
			layout.dispose();
			return wrapped.toString();
		} else {
			return super.shortenText(gc, t, width, drawFlags);
		}
	}
}
