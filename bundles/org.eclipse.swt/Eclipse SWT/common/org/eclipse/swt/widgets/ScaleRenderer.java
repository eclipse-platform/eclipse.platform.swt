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
import org.eclipse.swt.widgets.Scale.*;

class ScaleRenderer implements IScaleRenderer {
	private static final Color IDLE_COLOR = new Color(Display.getDefault(), 0, 95, 184);
	private static final Color HOVER_COLOR = new Color(Display.getDefault(), 0, 0, 0);
	private static final Color DRAG_COLOR = new Color(Display.getDefault(), 204, 204, 204);

	private static Color background;

	private final Scale scale;

	/**
	 * Pixel per Unit. Ratio of how many pixels are used to display one unit of the
	 * scale.
	 */
	private double ppu;

	private Rectangle handleBounds;
	private Rectangle bar;

	ScaleRenderer(Scale scale) {
		this.scale = scale;
	}

	@Override
	public void render(GC nativeGc, Rectangle bounds) {
		initBackground(nativeGc, bounds);

		GC sgc = initSkijaGc(nativeGc, bounds);

		renderScale(sgc, 0, 0, bounds.width, bounds.height);

		sgc.commit();
		sgc.dispose();
	}

	private void initBackground(GC originalGC, Rectangle bounds) {
		if (SWT.getPlatform().equals("win32") | SWT.getPlatform().equals("gtk")) {
			// Extract background color on first execution
			if (background == null) {
				extractAndStoreBackgroundColor(bounds, originalGC);
			}
			scale.style |= SWT.NO_BACKGROUND;
		}
	}

	private void extractAndStoreBackgroundColor(Rectangle r, GC originalGC) {
		Image backgroundColorImage = new Image(scale.getDisplay(), r.width, r.height);
		originalGC.copyArea(backgroundColorImage, 0, 0);
		int pixel = backgroundColorImage.getImageData().getPixel(0, 0);
		backgroundColorImage.dispose();
		background = SWT.convertPixelToColor(pixel);
	}

	public GC initSkijaGc(GC originalGC, Rectangle bounds) {
//		IGraphicsContext gc = new SkijaGC(originalGC, background);

		originalGC.setClipping(bounds.x, bounds.y, bounds.width, bounds.height);

		originalGC.setForeground(scale.getForeground());
		originalGC.setBackground(scale.getBackground());
		originalGC.setClipping(new Rectangle(0, 0, bounds.width, bounds.height));
		originalGC.setAntialias(SWT.ON);

		return originalGC;
	}


	private void renderScale(GC gc, int x, int y, int w, int h) {
		int value = scale.getSelection();
		int min = scale.getMinimum();
		int max = scale.getMaximum();
		int units = Math.max(1, max - min);
		int effectiveValue = Math.min(max, Math.max(min, value));

		// draw background
		if (background != null) {
			gc.setBackground(background);
			gc.fillRectangle(x, y, w, h);
		}

		int firstNotch;
		int lastNotch;

		if (isVertical()) {
			bar = new Rectangle(x + 19, y + 8, 3, h - (y + 8) - 8);
			firstNotch = bar.y + 5;
			lastNotch = bar.y + bar.height - 5;
		} else {
			bar = new Rectangle(x + 8, y + 19, w - (x + 8) - 8, 3);
			firstNotch = bar.x + 5;
			lastNotch = bar.x + bar.width - 5;
		}

		gc.fillRectangle(bar);
		gc.setForeground(scale.getDisplay().getSystemColor(SWT.COLOR_WIDGET_NORMAL_SHADOW));
		gc.drawRectangle(bar);

		// prepare for line drawing
		gc.setForeground(scale.getDisplay().getSystemColor(SWT.COLOR_WIDGET_NORMAL_SHADOW));
		gc.setLineWidth(1);
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
		if (isRTL() && !isVertical()) {
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
		// draw handle
		Color handleColor = switch (scale.getHandleState()) {
		case IDLE -> IDLE_COLOR;
		case HOVER -> HOVER_COLOR;
		case DRAG -> DRAG_COLOR;
		};
		gc.setBackground(handleColor);
		handleBounds = calculateHandleBounds(value);
		gc.fillRectangle(handleBounds);
	}

	private Rectangle calculateHandleBounds(int value) {
		int pixelValue = (int) (ppu * value);
		if (isVertical()) {
			return new Rectangle(bar.x - 9, bar.y + pixelValue, 21, 10);
		} else if (isRTL()) {
			return new Rectangle(bar.x + bar.width - pixelValue - 10, bar.y - 9, 10, 21);
		} else {
			return new Rectangle(bar.x + pixelValue, bar.y - 9, 10, 21);
		}
	}

	@Override
	public int handlePosToValue(Point pos) {
		if (isVertical()) {
			return (int) Math.round((pos.y - bar.y) / ppu);
		} else {
			return (int) Math.round((pos.x - bar.x) / ppu);
		}
	}

	private boolean isRTL() {
		return scale.getOrientation() == SWT.RIGHT_TO_LEFT;
	}

	private boolean isVertical() {
		return scale.getAlignement() == SWT.VERTICAL;
	}

	private void drawNotch(GC gc, int pos, int size) {
		if (isVertical()) {
			gc.drawLine(bar.x - 10 - size, pos, bar.x - 10, pos);
			gc.drawLine(bar.x + 14, pos, bar.x + 14 + size, pos);
		} else {
			gc.drawLine(pos, bar.y - 10 - size, pos, bar.y - 10);
			gc.drawLine(pos, bar.y + 14, pos, bar.y + 14 + size);
		}
	}

	/*
	 * If in RTL mode, the points of events are relative to the top right corner of
	 * the widget. Since everything else is still relative to the top left, we
	 * mirror vertically.
	 */
	private Point mirrorVertically(Point p) {
		return new Point(scale.getBounds().width - p.x, p.y);
	}

	@Override
	public boolean isWithinHandle(Point position) {
		if (isRTL()) {
			position = mirrorVertically(position);
		}
		return handleBounds.contains(position);
	}

	@Override
	public boolean isAfterHandle(Point position) {
		if (isVertical()) {
			return position.y > handleBounds.y + handleBounds.height;
		} else if (isRTL()) {
			position = mirrorVertically(position);
			return position.x < handleBounds.x;
		} else {
			return position.x > handleBounds.x + handleBounds.width;
		}
	}

	@Override
	public boolean isBeforeHandle(Point position) {
		if (isVertical()) {
			return position.y < handleBounds.y;
		} else if (isRTL()) {
			position = mirrorVertically(position);
			return position.x > handleBounds.x + handleBounds.width;
		} else {
			return position.x < handleBounds.x;
		}
	}
}
