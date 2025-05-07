/*******************************************************************************
 * Copyright (c) 2000, 2025 IBM Corporation and others.
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
package org.eclipse.swt.widgets;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;

class DefaultScaleRenderer extends ScaleRenderer {

	private static final int PREFERRED_WIDTH = 170;
	private static final int PREFERRED_HEIGHT = 42;

	static final String COLOR_HANDLE_IDLE = "scale.handle.background"; //$NON-NLS-1$
	static final String COLOR_HANDLE_HOVER = "scale.handle.background.hover"; //$NON-NLS-1$
	static final String COLOR_HANDLE_DRAG = "scale.handle.background.drag"; //$NON-NLS-1$
	static final String COLOR_HANDLE_OUTLINE = "scale.handle.outline"; //$NON-NLS-1$
	static final String COLOR_NOTCH = "scale.notch.foreground"; //$NON-NLS-1$

	/**
	 * Pixel per Unit. Ratio of how many pixels are used to display one unit of the
	 * scale.
	 */
	private double ppu;

	private Rectangle handleBounds;
	private Rectangle bar;

	public DefaultScaleRenderer(Scale scale) {
		super(scale);
	}

	@Override
	public Point computeDefaultSize() {
		int width;
		int height;
		if (isHorizontal()) {
			width = PREFERRED_WIDTH;
			height = PREFERRED_HEIGHT;
		}
		else {
			width = PREFERRED_HEIGHT;
			height = PREFERRED_WIDTH;
		}

		return new Point(width, height);
	}

	@Override
	public void paint(GC gc, int width, int height) {
		int value = scale.getSelection();
		int min = scale.getMinimum();
		int max = scale.getMaximum();
		int units = Math.max(1, max - min);
		int effectiveValue = Math.min(max, Math.max(min, value));

		int firstNotch;
		int lastNotch;

		if (isHorizontal()) {
			bar = new Rectangle(8, 19, width - 16, 3);
			firstNotch = bar.x + 5;
			lastNotch = bar.x + bar.width - 5;
		}
		else {
			bar = new Rectangle(19, 8, 3, height - 16);
			firstNotch = bar.y + 5;
			lastNotch = bar.y + bar.height - 5;
		}

		gc.setForeground(getColor(COLOR_HANDLE_OUTLINE));
		gc.drawRectangle(bar);

		// prepare for line drawing
		gc.setForeground(getColor(COLOR_NOTCH));
		gc.setLineWidth(1);

		// draw first and last notch
		drawNotch(gc, firstNotch, 4);
		drawNotch(gc, lastNotch, 4);

		// draw center notches
		int unitPerPage = scale.getPageIncrement();
		double totalPixel = lastNotch - firstNotch;
		ppu = totalPixel / units;
		drawCenterNotches(gc, firstNotch, lastNotch, units, unitPerPage);

		drawHandle(gc, effectiveValue);
	}

	private void drawCenterNotches(GC gc, int firstNotchPos, int lastNotchPos, int units, int unitPerPage) {
		if (isRTL() && isHorizontal()) {
			for (int i = unitPerPage; i < units; i += unitPerPage) {
				int position = lastNotchPos - (int) (i * ppu);
				drawNotch(gc, position, 3);
			}
		} else { // SWT.LEFT_TO_RIGHT or SWT.VERTICAL
			for (int i = unitPerPage; i < units; i += unitPerPage) {
				int position = firstNotchPos + (int) (i * ppu);
				drawNotch(gc, position, 3);
			}
		}
	}

	private void drawHandle(GC gc, int value) {
		final String colorKey;
		if (scale.isEnabled()) {
			colorKey = switch (getHandleState()) {
				case IDLE -> COLOR_HANDLE_IDLE;
				case HOVER -> COLOR_HANDLE_HOVER;
				case DRAG -> COLOR_HANDLE_DRAG;
			};
		} else {
			colorKey = ControlRenderer.COLOR_DISABLED;
		}
		gc.setBackground(getColor(colorKey));
		handleBounds = calculateHandleBounds(value);
		gc.fillRectangle(handleBounds);
	}

	private Rectangle calculateHandleBounds(int value) {
		int pixelValue = (int) (ppu * value);
		if (isHorizontal()) {
			if (isRTL()) {
				return new Rectangle(bar.x + bar.width - pixelValue - 10, bar.y - 9, 10, 21);
			}
			return new Rectangle(bar.x + pixelValue, bar.y - 9, 10, 21);
		}
		return new Rectangle(bar.x - 9, bar.y + pixelValue, 21, 10);
	}

	@Override
	public int handlePosToValue(Point pos) {
		if (isHorizontal()) {
			return (int) Math.round((pos.x - bar.x) / ppu);
		}
		return (int) Math.round((pos.y - bar.y) / ppu);
	}

	private boolean isRTL() {
		return scale.getOrientation() == SWT.RIGHT_TO_LEFT;
	}

	private void drawNotch(GC gc, int pos, int size) {
		if (isHorizontal()) {
			gc.drawLine(pos, bar.y - 10 - size, pos, bar.y - 10);
			gc.drawLine(pos, bar.y + 14, pos, bar.y + 14 + size);
		}
		else {
			gc.drawLine(bar.x - 10 - size, pos, bar.x - 10, pos);
			gc.drawLine(bar.x + 14, pos, bar.x + 14 + size, pos);
		}
	}

	/*
	 * If in RTL mode, the points of events are relative to the top right corner of
	 * the widget. Since everything else is still relative to the top left, we
	 * mirror vertically.
	 */
	@Override
	public boolean isWithinHandle(Point position) {
		if (isRTL()) {
			position = mirrorVertically(position);
		}
		return handleBounds.contains(position);
	}

	@Override
	public boolean isAfterHandle(Point position) {
		if (isHorizontal()) {
			if (isRTL()) {
				position = mirrorVertically(position);
				return position.x < handleBounds.x;
			}
			return position.x > handleBounds.x + handleBounds.width;
		}
		return position.y > handleBounds.y + handleBounds.height;
	}

	@Override
	public boolean isBeforeHandle(Point position) {
		if (isHorizontal()) {
			if (isRTL()) {
				position = mirrorVertically(position);
				return position.x > handleBounds.x + handleBounds.width;
			}
			return position.x < handleBounds.x;
		}
		return position.y < handleBounds.y;
	}

	private Point mirrorVertically(Point p) {
		return new Point(scale.getSize().x - p.x, p.y);
	}
}
