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

class DefaultButtonRenderer extends ButtonRenderer {

	protected static final String COLOR_BACKGROUND = "button.background"; //$NON-NLS-1$

	/**
	 * Left and right margins
	 */
	private static final int LEFT_MARGIN = 2;
	private static final int RIGHT_MARGIN = 2;
	private static final int TOP_MARGIN = 0;
	private static final int BOTTOM_MARGIN = 0;
	private static final int SPACING = 4;

	private static final int DRAW_FLAGS = SWT.DRAW_MNEMONIC | SWT.DRAW_TAB
			| SWT.DRAW_TRANSPARENT | SWT.DRAW_DELIMITER;


	public DefaultButtonRenderer(Button button) {
		super(button);
	}

	public Point computeDefaultSize() {
		final String text = button.getText();
		final Image image = button.getImage();

		int textWidth = 0;
		int textHeight = 0;
		int boxSpace = 0;
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

		width += 12;
		height += 10;

		return new Point(width, height);
	}

	@Override
	protected void paint(GC gc, int width, int height) {
		final int style = button.getStyle();
		final String text = button.getText();
		final Image image = button.getImage();

		boolean isRightAligned = (style & SWT.RIGHT) != 0;
		boolean isCentered = (style & SWT.CENTER) != 0;
		int initialAntiAlias = gc.getAntialias();

		int boxSpace = 0;
		drawPushButton(gc, width, height);

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
		int imageHeight = 0;
		if (image != null) {
			Rectangle imgB = image.getBounds();
			imageHeight = imgB.height;
			imageSpace = imgB.width;
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

		boolean shiftDownRight = isPressed() || button.getSelection();
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
			gc.setForeground(button.isEnabled() ? button.getForeground() : getColor(COLOR_DISABLED));
			int textTopOffset = (height - 1 - textHeight) / 2;
			int textLeftOffset = contentArea.x + imageSpace;
			if (shiftDownRight) {
				textTopOffset++;
				textLeftOffset++;
			}
			gc.drawText(text, textLeftOffset, textTopOffset, DRAW_FLAGS);
		}
		if (button.hasFocus()) {
			int inset = 2;
			gc.drawFocus(inset, inset, width - 2 * inset - 1, height - 2 * inset - 1);
		}
	}

	@Override
	public Color getDefaultBackground() {
		return getColor(COLOR_BACKGROUND);
	}

	private void drawPushButton(GC gc, int w, int h) {
		final boolean isToggle = (button.getStyle() & SWT.TOGGLE) != 0;
		final String key;
		if (button.isEnabled()) {
			if (isToggle && button.getSelection()) {
				gc.setBackground(getColor(COLOR_TOGGLE));
			} else if (isPressed()) {
				gc.setBackground(getColor(COLOR_TOGGLE));
			} else if (isHover()) {
				gc.setBackground(getColor(COLOR_HOVER));
			} else {
				gc.setBackground(button.getBackground());
			}
			gc.fillRoundRectangle(0, 0, w, h, 6, 6);

			key = isToggle && button.getSelection() || isHover()
					? COLOR_SELECTION
					: COLOR_OUTLINE;
		} else {
			key = COLOR_OUTLINE_DISABLED;
		}

		// if the button has focus, the border also changes the color
		Color fg = getColor(key);
		if (button.hasFocus()) {
			gc.setForeground(getColor(COLOR_SELECTION));
		}
		else {
			gc.setForeground(fg);
		}
		gc.drawRoundRectangle(0, 0, w - 1, h - 1, 6, 6);
		gc.setForeground(fg);
	}
}
