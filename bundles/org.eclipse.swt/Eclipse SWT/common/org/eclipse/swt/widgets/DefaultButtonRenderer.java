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

	private static final Color BACKGROUND_COLOR = new Color(255, 255, 255);
	private static final Color HOVER_COLOR = new Color(224, 238, 254);
	private static final Color TOGGLE_COLOR = new Color(204, 228, 247);
	private static final Color SELECTION_COLOR = new Color(0, 95, 184);
	private static final Color BORDER_COLOR = new Color(160, 160, 160);
	private static final Color BORDER_DISABLED_COLOR = new Color(192, 192, 192);

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
			gc.setForeground(button.isEnabled() ? button.getForeground() : DISABLED_COLOR);
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
		return BACKGROUND_COLOR;
	}

	private void drawPushButton(GC gc, int w, int h) {
		final boolean isToggle = (button.getStyle() & SWT.TOGGLE) != 0;
		if (button.isEnabled()) {
			if (isToggle && button.getSelection()) {
				gc.setBackground(TOGGLE_COLOR);
			} else if (isPressed()) {
				gc.setBackground(TOGGLE_COLOR);
			} else if (isHover()) {
				gc.setBackground(HOVER_COLOR);
			} else {
				gc.setBackground(button.getBackground());
			}
			gc.fillRoundRectangle(0, 0, w, h, 6, 6);

			if (isToggle && button.getSelection() || isHover()) {
				gc.setForeground(SELECTION_COLOR);
			} else {
				gc.setForeground(BORDER_COLOR);
			}
		} else {
			gc.setForeground(BORDER_DISABLED_COLOR);
		}

		// if the button has focus, the border also changes the color
		Color fg = gc.getForeground();
		if (button.hasFocus()) {
			gc.setForeground(SELECTION_COLOR);
		}
		gc.drawRoundRectangle(0, 0, w - 1, h - 1, 6, 6);
		gc.setForeground(fg);
	}
}
