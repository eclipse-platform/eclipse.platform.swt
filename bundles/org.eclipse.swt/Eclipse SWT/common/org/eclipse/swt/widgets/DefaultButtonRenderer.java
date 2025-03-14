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

public class DefaultButtonRenderer extends ButtonRenderer {

	private static final Color HOVER_COLOR = new Color(224, 238, 254);
	private static final Color TOGGLE_COLOR = new Color(204, 228, 247);
	private static final Color SELECTION_COLOR = new Color(0, 95, 184);
	private static final Color TEXT_COLOR = new Color(0, 0, 0);
	private static final Color DISABLED_COLOR = new Color(160, 160, 160);
	private static final Color BORDER_COLOR = new Color(160, 160, 160);
	private static final Color BORDER_DISABLED_COLOR = new Color(192, 192, 192);
	private static final Color CHECKBOX_GRAYED_COLOR = new Color(192, 192, 192);
	private static final Color PUSH_BACKGROUND_COLOR = new Color(255, 255, 255);

	/**
	 * Left and right margins
	 */
	private static final int LEFT_MARGIN = 2;
	private static final int RIGHT_MARGIN = 2;
	private static final int TOP_MARGIN = 0;
	private static final int BOTTOM_MARGIN = 0;
	private static final int BOX_SIZE = 12;
	private static final int SPACING = 4;

	private static final int DRAW_FLAGS = SWT.DRAW_MNEMONIC | SWT.DRAW_TAB
			| SWT.DRAW_TRANSPARENT | SWT.DRAW_DELIMITER;


	public DefaultButtonRenderer(Button button) {
		super(button);
	}

	public Point computeDefaultSize() {
		final String text = getText();
		final Image image = getImage();

		if (isArrowButton()) {
			int borderWidth = hasBorder() ? 8 : 0;
			int width = 14 + borderWidth;
			int height = 14 + borderWidth;
			return new Point(width, height);
		}

		int textWidth = 0;
		int textHeight = 0;
		int boxSpace = 0;
		if (!isPushOrToggle()) {
			boxSpace = BOX_SIZE + SPACING;
		}
		if (text != null && !text.isEmpty()) {
			Point textExtent = getTextExtent(text, DRAW_FLAGS);
			textWidth = textExtent.x + 1;
			textHeight = textExtent.y;
		}
		int imageSpace = 0;
		int imageHeight = 0;
		if (image != null) {
			Rectangle imgB = image.getBounds();
			imageHeight = imgB.height;
			imageSpace = imgB.width;
			if (text != null && !text.isEmpty()) {
				imageSpace += SPACING;
			}
		}

		int width = LEFT_MARGIN + boxSpace + imageSpace + textWidth + 1
				+ RIGHT_MARGIN;
		int height = TOP_MARGIN
				+ Math.max(boxSpace, Math.max(textHeight, imageHeight))
				+ BOTTOM_MARGIN;

		if (isPushOrToggle()) {
			width += 12;
			height += 10;
		}

		return new Point(width, height);
	}

	@Override
	protected void paint(GC gc, int width, int height) {
		final int style = getStyle();
		final String text = getText();
		final Image image = getImage();

		boolean isRightAligned = (style & SWT.RIGHT) != 0;
		boolean isCentered = (style & SWT.CENTER) != 0;
		int initialAntiAlias = gc.getAntialias();

		int boxSpace = 0;
		// Draw check box / radio box / push button border
		if (isPushOrToggle()) {
			drawPushButton(gc, width - 1, height - 1);
		} else {
			boxSpace = BOX_SIZE + SPACING;
			int boxLeftOffset = LEFT_MARGIN;
			int boxTopOffset = (height - 1 - BOX_SIZE) / 2;
			if ((style & SWT.CHECK) == SWT.CHECK) {
				drawCheckbox(gc, boxLeftOffset, boxTopOffset);
			} else if ((style & SWT.RADIO) == SWT.RADIO) {
				drawRadioButton(gc, boxLeftOffset, boxTopOffset);
			}
		}

		gc.setAntialias(initialAntiAlias);
		gc.setAdvanced(false);

		// Calculate area for button content (image + text)
		int horizontalSpaceForContent = width - RIGHT_MARGIN - LEFT_MARGIN
				- boxSpace;
		int textWidth = 0;
		int textHeight = 0;
		if (text != null && !text.isEmpty()) {
			Point textExtent = gc.textExtent(text, DRAW_FLAGS);
			textWidth = textExtent.x;
			textHeight = textExtent.y;
		}
		int imageSpace = 0;
		int imageWidth = 0;
		int imageHeight = 0;
		if (image != null) {
			Rectangle imgB = image.getBounds();
			imageWidth = imgB.width;
			imageHeight = imgB.height;
			imageSpace = imageWidth;
			if (text != null && !text.isEmpty()) {
				imageSpace += SPACING;
			}
		}
		Rectangle contentArea = new Rectangle(LEFT_MARGIN + boxSpace,
				TOP_MARGIN, imageSpace + textWidth,
				height - TOP_MARGIN - BOTTOM_MARGIN);
		if (isRightAligned) {
			contentArea.x += horizontalSpaceForContent - contentArea.width;
		} else if (isCentered) {
			contentArea.x += (horizontalSpaceForContent - contentArea.width)
					/ 2;
		}

		boolean shiftDownRight = isPushOrToggle() && (isPressed() || isSelected());
		// Draw image
		if (image != null) {
			int imageTopOffset = (height - imageHeight) / 2;
			int imageLeftOffset = contentArea.x;
			if (shiftDownRight) {
				imageTopOffset++;
				imageLeftOffset++;
			}
			drawImage(gc, imageLeftOffset, imageTopOffset);
		}

		// Draw text
		if (text != null && !text.isEmpty()) {
			gc.setForeground(isEnabled() ? TEXT_COLOR : DISABLED_COLOR);
			int textTopOffset = (height - 1 - textHeight) / 2;
			int textLeftOffset = contentArea.x + imageSpace;
			if (shiftDownRight) {
				textTopOffset++;
				textLeftOffset++;
			}
			gc.drawText(text, textLeftOffset, textTopOffset, DRAW_FLAGS);
		}
		if (hasFocus()) {
			if (((style & SWT.RADIO) | (style & SWT.CHECK)) != 0) {
				int textTopOffset = (height - 1 - textHeight) / 2;
				int textLeftOffset = contentArea.x + imageSpace;
				gc.drawFocus(textLeftOffset - 2, textTopOffset, textWidth + 4,
						textHeight);
			} else {
				gc.drawFocus(3, 3, width - 7, height - 7);
			}
		}

		if (isArrowButton()) {
			drawArrowButton(gc, width, height, style);
		}
	}

	private void drawArrowButton(GC gc, int width, int height, int style) {
		Color bg2 = gc.getBackground();

		gc.setBackground(TEXT_COLOR);

		int centerHeight = height / 2;
		int centerWidth = width / 2;
		if (hasBorder()) {
			// border ruins center position...
			centerHeight -= 2;
			centerWidth -= 2;
		}

		// TODO: in the next version use a bezier path

		int[] curve = null;

		if ((style & SWT.DOWN) != 0) {
			curve = new int[]{centerWidth, centerHeight + 5,
					centerWidth - 5, centerHeight - 5, centerWidth + 5,
					centerHeight - 5};

		} else if ((style & SWT.LEFT) != 0) {
			curve = new int[]{centerWidth - 5, centerHeight,
					centerWidth + 5, centerHeight + 5, centerWidth + 5,
					centerHeight - 5};

		} else if ((style & SWT.RIGHT) != 0) {
			curve = new int[]{centerWidth + 5, centerHeight,
					centerWidth - 5, centerHeight - 5, centerWidth - 5,
					centerHeight + 5};

		} else {
			curve = new int[]{centerWidth, centerHeight - 5,
					centerWidth - 5, centerHeight + 5, centerWidth + 5,
					centerHeight + 5};
		}

		gc.fillPolygon(curve);
		gc.setBackground(bg2);
	}

	private void drawPushButton(GC gc, int w, int h) {
		final boolean isToggle = isToggle();
		if (isEnabled()) {
			if (isToggle && isSelected()) {
				gc.setBackground(TOGGLE_COLOR);
			} else if (isPressed()) {
				gc.setBackground(TOGGLE_COLOR);
			} else if (isHover()) {
				gc.setBackground(HOVER_COLOR);
			} else {
				gc.setBackground(PUSH_BACKGROUND_COLOR);
			}
			gc.fillRoundRectangle(0, 0, w, h, 6, 6);
		}

		if (isEnabled()) {
			if (isToggle && isSelected() || isHover()) {
				gc.setForeground(SELECTION_COLOR);
			} else {
				gc.setForeground(BORDER_COLOR);
			}
		} else {
			gc.setForeground(BORDER_DISABLED_COLOR);
		}

		// if the button has focus, the border also changes the color
		Color fg = gc.getForeground();
		if (hasFocus()) {
			gc.setForeground(SELECTION_COLOR);
		}
		gc.drawRoundRectangle(0, 0, w - 1, h - 1, 6, 6);
		gc.setForeground(fg);
	}

	private void drawRadioButton(GC gc, int x, int y) {
		if (isSelected()) {
			gc.setBackground(isEnabled() ? SELECTION_COLOR : DISABLED_COLOR);
			int partialBoxBorder = 2;
			gc.fillOval(x + partialBoxBorder, y + partialBoxBorder,
					BOX_SIZE - 2 * partialBoxBorder + 1, BOX_SIZE - 2 * partialBoxBorder + 1);
		}

		if (!isEnabled()) {
			gc.setForeground(BORDER_DISABLED_COLOR);
		} else if (isHover()) {
			gc.setBackground(HOVER_COLOR);
			int partialBoxBorder = isSelected() ? 4 : 0;
			gc.fillOval(x + partialBoxBorder, y + partialBoxBorder,
					BOX_SIZE - 2 * partialBoxBorder + 1, BOX_SIZE - 2 * partialBoxBorder + 1);
		}
		gc.drawOval(x, y, BOX_SIZE, BOX_SIZE);
	}

	private void drawCheckbox(GC gc, int x, int y) {
		if (isSelected()) {
			gc.setBackground(isEnabled()
					? isGrayed() ? CHECKBOX_GRAYED_COLOR : SELECTION_COLOR
					: DISABLED_COLOR);
			int partialBoxBorder = 2;
			gc.fillRoundRectangle(x + partialBoxBorder, y + partialBoxBorder,
					BOX_SIZE - 2 * partialBoxBorder + 1, BOX_SIZE - 2 * partialBoxBorder + 1,
					BOX_SIZE / 4 - partialBoxBorder / 2,
					BOX_SIZE / 4 - partialBoxBorder / 2);
		}

		if (!isEnabled()) {
			gc.setForeground(BORDER_DISABLED_COLOR);
		} else if (isHover()) {
			gc.setBackground(HOVER_COLOR);
			int partialBoxBorder = isSelected() ? 4 : 0;
			gc.fillRoundRectangle(x + partialBoxBorder, y + partialBoxBorder,
					BOX_SIZE - 2 * partialBoxBorder + 1, BOX_SIZE - 2 * partialBoxBorder + 1,
					BOX_SIZE / 4 - partialBoxBorder / 2,
					BOX_SIZE / 4 - partialBoxBorder / 2);
		}
		gc.drawRoundRectangle(x, y, BOX_SIZE, BOX_SIZE, 4, 4);
	}
}
