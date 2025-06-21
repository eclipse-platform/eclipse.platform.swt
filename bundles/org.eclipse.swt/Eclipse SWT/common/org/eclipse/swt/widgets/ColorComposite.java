/*******************************************************************************
 * Copyright (c) 2000, 2019 IBM Corporation and others.
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

import java.util.prefs.*;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;

public class ColorComposite extends Composite {
	private final int rows = 5;
	private final int cols = 9;
	private final int cellSize = 35;
	private final int padding = 8;
	private final int labelHeight = 24;
	private final int customCellSize = 30;

	private final RGB[][] colorGrid;
	private final RGB[] customColors = new RGB[8];

	private Point selectedCell = null;
	Color selectedColor;

	private boolean isHoveringCustomAdd = false;

	private static final String PREF_NODE = "org.eclipse.swt.widgets.ColorComposite";
	private static final String COLOR_KEY_PREFIX = "customColor";
	private static final Preferences prefs = Preferences.userRoot().node(PREF_NODE);

	public ColorComposite(Composite parent, int style) {
		super(parent, style);

		colorGrid = generateColorGrid();
		loadCustomColors();

		Listener listener = event -> {
			switch (event.type) {
			case SWT.Paint -> onPaint(event);
			case SWT.MouseUp -> onMouseUp(event);
			case SWT.Resize -> redraw();
			case SWT.MouseMove -> handleMouseMove(event);

			}
		};

		addListener(SWT.Paint, listener);
		addListener(SWT.MouseUp, listener);
		addListener(SWT.Resize, listener);
		addListener(SWT.MouseMove, listener);

	}

	private void handleMouseMove(Event event) {
		int labelY = padding + rows * (cellSize + padding);
		int customY = labelY + labelHeight;

		int x = padding;
		int y = customY;

		Rectangle plusBox = new Rectangle(x, y, customCellSize, customCellSize);
		boolean hovering = plusBox.contains(event.x, event.y);

		if (hovering != isHoveringCustomAdd) {
			isHoveringCustomAdd = hovering;
			redraw();
		}
	}

	private void onMouseUp(Event event) {
		int startX = padding;
		int startY = padding;

		// Check main grid
		for (int row = 0; row < rows; row++) {
			for (int col = 0; col < cols; col++) {
				int x = startX + col * (cellSize + padding);
				int y = startY + row * (cellSize + padding);
				Rectangle rect = new Rectangle(x, y, cellSize, cellSize);
				if (rect.contains(event.x, event.y)) {
					selectedCell = new Point(col, row);
					// Dispose previous selectedColor to avoid leaks
					if (selectedColor != null && !selectedColor.isDisposed()) {
						selectedColor.dispose();
					}

					RGB rgb = colorGrid[col][row];
					selectedColor = new Color(getDisplay(), rgb);
					redraw();
					return;
				}
			}
		}

		// Check custom color section
		int labelY = padding + rows * (cellSize + padding);
		int customY = labelY + labelHeight;

		for (int i = 0; i < 9; i++) {
			int x = padding + i * (customCellSize + padding);
			int y = customY;
			Rectangle rect = new Rectangle(x, y, customCellSize, customCellSize);

			if (rect.contains(event.x, event.y)) {
				if (i == 0) {
					AdvancedColorDialog acd = new AdvancedColorDialog(getShell());
					Color color = acd.open();
					if (color != null && !color.isDisposed()) {
						selectedColor = color;

						// Add to customColors in queue order
						RGB rgb = color.getRGB();
						System.arraycopy(customColors, 1, customColors, 0, customColors.length - 1); // shift left
						customColors[customColors.length - 1] = rgb;
						saveCustomColors();

						redraw();
					}
				} else {
					RGB rgb = customColors[i - 1];
					if (rgb != null) {
						selectedColor = new Color(getDisplay(), rgb);
						redraw();
					}
				}
				isHoveringCustomAdd = false;
				break;
			}
		}
	}

	private void onPaint(Event event) {
		Drawing.drawWithGC(this, event.gc, this::drawColorGrid);
	}

	private void drawColorGrid(GC gc) {
		Display display = getDisplay();

		// Draw main grid
		for (int row = 0; row < rows; row++) {
			for (int col = 0; col < cols; col++) {
				RGB rgb = colorGrid[col][row];
				Color color = new Color(display, rgb);

				int x = padding + col * (cellSize + padding);
				int y = padding + row * (cellSize + padding);

				gc.setBackground(color);
				gc.fillRectangle(x, y, cellSize, cellSize);

				// Draw border
				gc.setForeground(display.getSystemColor(SWT.COLOR_BLACK));
				gc.drawRectangle(x, y, cellSize, cellSize);

				// Highlight if selected
				if (selectedCell != null && selectedCell.x == col && selectedCell.y == row) {
					gc.setForeground(display.getSystemColor(SWT.COLOR_WHITE));
					gc.setLineWidth(2);
					gc.drawRectangle(x - 1, y - 1, cellSize + 2, cellSize + 2);
					gc.setLineWidth(1);
				}

				color.dispose();
			}
		}

		// "Custom" label
		int labelY = padding + rows * (cellSize + padding);
		String label = "Custom";
		Point textExtent = gc.textExtent(label);
		int labelX = (getSize().x - textExtent.x) / 2;
		gc.setForeground(display.getSystemColor(SWT.COLOR_BLACK));
		gc.drawText(label, labelX, labelY + (padding / 2), true);

		// custom color boxes
		int customY = labelY + labelHeight;

		for (int i = 0; i < 9; i++) {
			int x = padding + i * (customCellSize + padding);
			int y = customY;

			Rectangle rect = new Rectangle(x, y, customCellSize, customCellSize);
			gc.setForeground(display.getSystemColor(SWT.COLOR_BLACK));
			gc.drawRectangle(rect);

			if (i == 0) {
				// Highlight background if hovered
				if (isHoveringCustomAdd) {
					gc.setBackground(display.getSystemColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
					gc.fillRectangle(x + 1, y + 1, customCellSize - 1, customCellSize - 1);
				}

				// Draw plus sign
				int cx = x + customCellSize / 2;
				int cy = y + customCellSize / 2;
				gc.setForeground(display.getSystemColor(SWT.COLOR_BLACK));
				gc.drawLine(cx - 5, cy, cx + 5, cy);
				gc.drawLine(cx, cy - 5, cx, cy + 5);

			} else {
				RGB rgb = customColors[i - 1];
				if (rgb != null) {
					Color color = new Color(display, rgb);
					gc.setBackground(color);
					gc.fillRectangle(x + 1, y + 1, customCellSize - 1, customCellSize - 1);
					color.dispose();
				}
			}
		}
	}

	private RGB[][] generateColorGrid() {
		RGB[] baseColors = { new RGB(0, 0, 255), // Blue
				new RGB(0, 255, 0), // Green
				new RGB(255, 255, 0), // Yellow
				new RGB(255, 165, 0), // Orange
				new RGB(255, 0, 0), // Red
				new RGB(128, 0, 128), // Violet
				new RGB(165, 42, 42), // Brown
				new RGB(255, 255, 255), // White
				new RGB(0, 0, 0) // Black
		};

		RGB[][] grid = new RGB[cols][rows];
		for (int col = 0; col < baseColors.length; col++) {
			RGB base = baseColors[col];
			for (int row = 0; row < rows; row++) {
				// Invert row so brightest is at the bottom
				float factor = 1f - ((rows - 1 - row) * 0.18f);
				grid[col][row] = new RGB(Math.max(0, (int) (base.red * factor)),
						Math.max(0, (int) (base.green * factor)), Math.max(0, (int) (base.blue * factor)));
			}
		}
		return grid;
	}

	private void loadCustomColors() {
		for (int i = 0; i < customColors.length; i++) {
			String value = prefs.get(COLOR_KEY_PREFIX + i, null);
			if (value != null) {
				String[] parts = value.split(",");
				if (parts.length == 3) {
					try {
						int r = Integer.parseInt(parts[0]);
						int g = Integer.parseInt(parts[1]);
						int b = Integer.parseInt(parts[2]);
						customColors[i] = new RGB(r, g, b);
					} catch (NumberFormatException ignored) {
					}
				}
			}
		}
	}

	private void saveCustomColors() {
		for (int i = 0; i < customColors.length; i++) {
			RGB rgb = customColors[i];
			if (rgb != null) {
				String value = rgb.red + "," + rgb.green + "," + rgb.blue;
				prefs.put(COLOR_KEY_PREFIX + i, value);
			} else {
				prefs.remove(COLOR_KEY_PREFIX + i);
			}
		}
	}

	@Override
	public Point computeSize(int wHint, int hHint, boolean changed) {
		int width = padding + cols * (cellSize + padding);
		int height = padding + rows * (cellSize + padding) + labelHeight + customCellSize + padding;
		return new Point(width, height);
	}

	public void setInitialColor(RGB rgb) {
		if (rgb != null) {
			if (selectedColor != null && !selectedColor.isDisposed()) {
				selectedColor.dispose();
			}
			this.selectedColor = new Color(getDisplay(), rgb);
		}
	}

	public void setCustomColors(RGB[] colors) {
		if (colors != null) {
			int count = Math.min(colors.length, customColors.length);
			System.arraycopy(colors, 0, customColors, 0, count);
		}
	}

	public RGB[] getCustomColors() {
		return customColors.clone();
	}

}
