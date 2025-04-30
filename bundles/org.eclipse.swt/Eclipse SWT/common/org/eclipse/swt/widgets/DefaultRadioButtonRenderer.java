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

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.*;

public class DefaultRadioButtonRenderer extends ButtonRenderer {

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


	public DefaultRadioButtonRenderer(Button button) {
		super(button);
	}

	public Point computeDefaultSize() {
		final String text = button.getText();
		final Image image = button.getImage();

		int textWidth = 0;
		int textHeight = 0;
		int boxSpace = BOX_SIZE + SPACING;
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

		int boxSpace = BOX_SIZE + SPACING;
		int boxLeftOffset = LEFT_MARGIN;
		int boxTopOffset = (height - 1 - BOX_SIZE) / 2;
		drawRadioButton(gc, boxLeftOffset, boxTopOffset);

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

		// Draw image
		if (image != null) {
			int imageTopOffset = (height - imageHeight) / 2;
			int imageLeftOffset = contentArea.x;
			drawImage(gc, imageLeftOffset, imageTopOffset);
		}

		// Draw text
		if (text != null && !text.isEmpty()) {
			gc.setForeground(button.isEnabled() ? button.getForeground() : getColor(COLOR_DISABLED));
			int textTopOffset = (height - 1 - textHeight) / 2;
			int textLeftOffset = contentArea.x + imageSpace;
			gc.drawText(text, textLeftOffset, textTopOffset, DRAW_FLAGS);
		}
		if (button.hasFocus()) {
			int textTopOffset = (height - 1 - textHeight) / 2;
			int textLeftOffset = contentArea.x + imageSpace;
			gc.drawFocus(textLeftOffset - 2, textTopOffset, textWidth + 4,
					textHeight - 1);
		}
	}

	private void drawRadioButton(GC gc, int x, int y) {
		final boolean enabled = button.isEnabled();
		final boolean selection = button.getSelection();
		if (selection) {
			gc.setBackground(getColor(enabled ? COLOR_SELECTION : COLOR_DISABLED));
			int partialBoxBorder = 2;
			gc.fillOval(x + partialBoxBorder, y + partialBoxBorder,
					BOX_SIZE - 2 * partialBoxBorder + 1, BOX_SIZE - 2 * partialBoxBorder + 1);
		}

		if (enabled) {
			gc.setForeground(getColor(COLOR_BOX));
			if (isHover()) {
				gc.setBackground(getColor(COLOR_HOVER));
				int partialBoxBorder = selection ? 4 : 0;
				gc.fillOval(x + partialBoxBorder, y + partialBoxBorder,
						BOX_SIZE - 2 * partialBoxBorder + 1, BOX_SIZE - 2 * partialBoxBorder + 1);
			}
		} else {
			gc.setForeground(getColor(COLOR_BOX_DISABLED));
		}
		gc.drawOval(x, y, BOX_SIZE, BOX_SIZE);
	}
}
