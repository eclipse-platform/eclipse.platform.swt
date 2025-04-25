package org.eclipse.swt.widgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.*;

public class DefaultGroupRenderer extends GroupRenderer {

	private static final int DRAW_FLAGS = SWT.DRAW_MNEMONIC | SWT.DRAW_TAB | SWT.DRAW_TRANSPARENT | SWT.DRAW_DELIMITER;

	private Point textExtentCache;

	public DefaultGroupRenderer(Group group) {
		super(group);
	}

	@Override
	protected void paint(GC gc, int width, int height) {
		final Point textExtent = getTextExtent();
		int titleWidth = textExtent.x;
		int titleHeight = textExtent.y;

		final int style = group.getStyle();
		final int orientation = style & (SWT.LEFT_TO_RIGHT | SWT.RIGHT_TO_LEFT);

		int inset = 2;
		int groupInset = 8;
		int borderRadius = 6;
		int textX = inset + groupInset;
		int textY = titleHeight;
		int groupWidth = width - (inset * 2);
		int groupHeight = height - (inset * 8);
		if (isBorderSet(style)) {
			// Border has to be drawn using Skija once native border is disabled.
			inset = (orientation == SWT.RIGHT_TO_LEFT) ? groupInset / 2 : 0;
			groupWidth -= 1;
			groupHeight -= groupInset / 4;
		}
		// Set shadow color
		gc.setForeground(getShadowColor(style));
		// Draw group border
		gc.drawRoundRectangle(inset, (textY / 2), groupWidth, groupHeight, borderRadius, borderRadius);
		// Draw text background (A fillRectangle to erase the part of the group border
		// where text is drawn) and text
		int textPosX = (orientation == SWT.RIGHT_TO_LEFT) ? groupWidth - groupInset - titleWidth : textX;
		gc.fillRectangle(textPosX, (textY / 2), titleWidth, gc.getLineWidth() + 1);
		gc.setForeground(group.getForeground());
		gc.drawText(group.getText(), textPosX, 0, DRAW_FLAGS);
	}

	@Override
	protected Point getTextExtent() {
		if (textExtentCache == null) {
			textExtentCache = Drawing.getTextExtent(group, group.getText(), DRAW_FLAGS);
		}
		return textExtentCache;
	}

	@Override
	public void clearCache() {
		textExtentCache = null;
	}

	private Color getShadowColor(int style) {
		if ((style & SWT.SHADOW_ETCHED_IN) != 0) {
			return new Color(136, 136, 136);
		}
		if ((style & SWT.SHADOW_ETCHED_OUT) != 0) {
			return new Color(68, 68, 68);
		}
		if ((style & SWT.SHADOW_IN) != 0) {
			return new Color(102, 102, 102);
		}
		if ((style & SWT.SHADOW_OUT) != 0) {
			return new Color(187, 187, 187);
		}
		return group.getForeground();
	}

	private boolean isBorderSet(int style) {
		return (style & SWT.BORDER) != 0;
	}
}
