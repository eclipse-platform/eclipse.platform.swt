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

	private static final Color SHADOW_IN_COLOR1 = new Color(160, 160, 160);
	private static final Color SHADOW_IN_COLOR2 = new Color(255, 255, 255);
	private static final Color SHADOW_OUT_COLOR1 = new Color(227, 227, 227);
	private static final Color SHADOW_OUT_COLOR2 = new Color(160, 160, 160);

	private static final int DRAW_FLAGS = SWT.DRAW_MNEMONIC | SWT.DRAW_TAB
										  | SWT.DRAW_TRANSPARENT | SWT.DRAW_DELIMITER;

	/** Gap between icon and text */
	private static final int GAP = 5;

	public BasicLabelRenderer(Label label) {
		super(label);
	}

	@Override
	public Point computeDefaultSize() {
		final String text = label.getText();
		final Image image = label.getImage();

		int lineWidth = 0;
		int lineHeight = 0;

		int leftMargin = getLeftMargin();
		int imageWidth = 0;
		int imageHeight = 0;
		int gap = 0;
		int topMargin = getTopMargin();

		if (text != null && !text.isEmpty()) {
			Point textExtent = getTextExtent(text, DRAW_FLAGS);
			lineWidth = textExtent.x;
			lineHeight = textExtent.y;
			if (image != null) {
				gap = GAP;
			}
		}
		if (image != null) {
			Rectangle imgB = image.getBounds();
			imageWidth = imgB.width;
			imageHeight = imgB.height;
		}

		int width = leftMargin + imageWidth + gap + lineWidth
					+ getRightMargin();
		int height = topMargin + Math.max(lineHeight, imageHeight)
					 + getBottomMargin();

		return new Point(width, height);
	}

	@Override
	protected void paint(GC gc, int width, int height) {
		String text = label.getText();
		Image image = label.getImage();
		if ((text == null || text.isEmpty()) && image == null) {
			return;
		}

		boolean shortenText = false;
		String t = text;
		Image img = image;
		int availableWidth = Math.max(0,
				width - (getLeftMargin() + getRightMargin()));
		Point extent = getTotalSize(gc, img, t);
		if (extent.x > availableWidth) {
			img = null;
			extent = getTotalSize(gc, img, t);
			if (extent.x > availableWidth) {
				shortenText = true;
			}
		}

		String[] lines = text == null ? null : splitString(text);

		// shorten the text
		if (shortenText) {
			extent.x = 0;
			for (int i = 0; i < lines.length; i++) {
				Point e = gc.textExtent(lines[i], DRAW_FLAGS);
				if (e.x > availableWidth) {
					lines[i] = shortenText(gc, lines[i], availableWidth, DRAW_FLAGS);
					extent.x = Math.max(extent.x,
							getTotalSize(gc, null, lines[i]).x);
				} else {
					extent.x = Math.max(extent.x, e.x);
				}
			}
			if (getToolTipText() == null) {
				setDisplayedToolTip(text);
			}
		} else {
			setDisplayedToolTip(getToolTipText());
		}

		// determine horizontal position
		int x = getLeftMargin();
		final int align = getAlign();
		if (align == SWT.CENTER) {
			x = (width - extent.x) / 2;
		}
		else if (align == SWT.RIGHT) {
			x = width - getRightMargin() - extent.x;
		}

		int style = label.getStyle();
		// draw a background image behind the text
		try {
			final Image backgroundImage = getBackgroundImage();
			final Color[] gradientColors = getGradientColors();
			final int[] gradientPercents = getGradientPercents();
			if (backgroundImage != null) {
				// draw a background image behind the text
				Rectangle imageRect = backgroundImage.getBounds();
				// tile image to fill space
				gc.setBackground(getBackground());
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
			} else if (gradientColors != null) {
				// draw a gradient behind the text
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
						gc.setBackground(getBackground());
						gc.fillRectangle(0, pos, width, height - pos);
					}
					if (!gradientVertical && pos < width) {
						gc.setBackground(getBackground());
						gc.fillRectangle(pos, 0, width - pos, height);
					}
					gc.setForeground(oldForeground);
				}
				gc.setBackground(oldBackground);
			} else {
				final Color background = getBackground();
				if (background != null && background.getAlpha() > 0) {
					gc.setBackground(getBackground());
					gc.fillRectangle(0, 0, width, height);
				}
			}
		} catch (SWTException e) {
			if ((style & SWT.DOUBLE_BUFFERED) == 0) {
				gc.setBackground(getBackground());
				gc.fillRectangle(0, 0, width, height);
			}
		}

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
		int lineHeight = 0, textHeight = 0, imageHeight = 0;

		if (img != null) {
			imageRect = img.getBounds();
			imageHeight = imageRect.height;
		}
		if (lines != null) {
			lineHeight = gc.getFontMetrics().getHeight();
			textHeight = lines.length * lineHeight;
		}

		int imageY, midPoint;
		if (imageHeight > textHeight) {
			if (getTopMargin() == DEFAULT_MARGIN && getBottomMargin() == DEFAULT_MARGIN) {
				imageY = 0;
			} else {
				imageY = getTopMargin();
			}
			midPoint = imageY + imageHeight / 2;
		} else {
			int lineY;
			if (getTopMargin() == DEFAULT_MARGIN && getBottomMargin() == DEFAULT_MARGIN) {
				lineY = (textHeight - imageHeight) / 2;
			} else {
				lineY = getTopMargin();
			}
			midPoint = lineY + textHeight / 2;
			imageY = midPoint - imageHeight / 2;
		}

		// draw the image
		if (img != null) {
			gc.drawImage(img, 0, 0, imageRect.width, imageHeight, x, imageY,
					imageRect.width, imageHeight);
			x += imageRect.width + GAP;
			extent.x -= imageRect.width + GAP;
		}

		// draw the text
		// we draw the label at the top.
		int lineY = getTopMargin();

		if (textHeight < imageHeight) {
			lineY = (imageHeight - textHeight) / 2;
		}

		if (lines != null) {
			gc.setForeground(label.isEnabled() ? label.getForeground() : DISABLED_COLOR);
			for (String line : lines) {
				int lineX = x;
				if (lines.length > 1) {
					if (align == SWT.CENTER) {
						int lineWidth = gc.textExtent(line, DRAW_FLAGS).x;
						lineX = x + Math.max(0, (extent.x - lineWidth) / 2);
					}
					if (align == SWT.RIGHT) {
						int lineWidth = gc.textExtent(line, DRAW_FLAGS).x;
						lineX = Math.max(x,
								width - getRightMargin() - lineWidth);
					}
				}
				gc.drawText(line, lineX, lineY, DRAW_FLAGS);
				lineY += lineHeight;
			}
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
			c1 = SHADOW_IN_COLOR1;
			c2 = SHADOW_IN_COLOR2;
		}
		if ((style & SWT.SHADOW_OUT) != 0) {
			c1 = SHADOW_OUT_COLOR1;
			c2 = SHADOW_OUT_COLOR2;
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
				size.x += GAP;
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
}
