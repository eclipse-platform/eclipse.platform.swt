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
 ******************************************************************************/
package org.eclipse.swt.widgets;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;

public class ColorPickerComposite extends Composite {
	Color selectedColor;
	RGB selectedRGB = new RGB(255, 0, 0);
	float currentHue = 0f;
	float currentSaturation = 1f;
	float currentBrightness = 1f;
	int currentAlpha = 255;

	Composite drawArea;

	public ColorPickerComposite(Composite parent, int style) {
		super(parent, style);
		setLayout(new GridLayout(2, false));

		// Drawing Canvas
		drawArea = new Canvas(this, SWT.DOUBLE_BUFFERED);
		GridData canvasGridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		canvasGridData.widthHint = 245;
		canvasGridData.heightHint = 280;
		drawArea.setLayoutData(canvasGridData);
		drawArea.addListener(SWT.Paint, this::onPaint);
		drawArea.addListener(SWT.MouseUp, this::onMouseUp);
		drawArea.addListener(SWT.Resize, e -> drawArea.redraw());


		// Input fields
		Composite inputPanel = new Composite(this, SWT.NONE);
		inputPanel.setLayout(new GridLayout(3, false));
		inputPanel.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false));

		createColorInput(inputPanel, "R:", 0);
		createColorInput(inputPanel, "G:", 1);
		createColorInput(inputPanel, "B:", 2);
		createHSVInput(inputPanel, "H:", 0);
		createHSVInput(inputPanel, "S:", 1);
		createHSVInput(inputPanel, "V:", 2);
	}

	private void createColorInput(Composite parent, String label, int colorIndex) {
		new Label(parent, SWT.NONE).setText(label);
		Scale scale = new Scale(parent, SWT.HORIZONTAL);
		scale.setMinimum(0);
		scale.setMaximum(255);
		scale.setLayoutData(new GridData(100, SWT.DEFAULT));
		Text text = new Text(parent, SWT.BORDER);
		text.setLayoutData(new GridData(40, SWT.DEFAULT));
		text.setText("0");

		scale.addListener(SWT.Selection, e -> {
			int value = scale.getSelection();
			text.setText(String.valueOf(value));
			updateRGB(colorIndex, value);
		});
		text.addListener(SWT.Modify, e -> {
			try {
				int value = Integer.parseInt(text.getText());
				if (value >= 0 && value <= 255) {
					scale.setSelection(value);
					updateRGB(colorIndex, value);
				}
			} catch (NumberFormatException ignored) {
			}
		});
	}

	private void createHSVInput(Composite parent, String label, int index) {
		new Label(parent, SWT.NONE).setText(label);
		Scale scale = new Scale(parent, SWT.HORIZONTAL);
		scale.setMinimum(0);
		scale.setMaximum(index == 0 ? 360 : 100);
		scale.setLayoutData(new GridData(100, SWT.DEFAULT));
		Text text = new Text(parent, SWT.BORDER);
		text.setLayoutData(new GridData(40, SWT.DEFAULT));
		text.setText("0");

		scale.addListener(SWT.Selection, e -> {
			int value = scale.getSelection();
			text.setText(String.valueOf(value));
			updateHSV(index, value);
		});
		text.addListener(SWT.Modify, e -> {
			try {
				int value = Integer.parseInt(text.getText());
				int max = index == 0 ? 360 : 100;
				if (value >= 0 && value <= max) {
					scale.setSelection(value);
					updateHSV(index, value);
				}
			} catch (NumberFormatException ignored) {
			}
		});
	}

	private void updateRGB(int index, int value) {
		if (selectedRGB == null)
			return;
		int r = selectedRGB.red;
		int g = selectedRGB.green;
		int b = selectedRGB.blue;
		if (index == 0)
			r = value;
		if (index == 1)
			g = value;
		if (index == 2)
			b = value;

		selectedRGB = new RGB(r, g, b);
		float[] hsb = java.awt.Color.RGBtoHSB(r, g, b, null);
		currentHue = hsb[0];
		currentSaturation = hsb[1];
		currentBrightness = hsb[2];
		updateSelectedColor();
	}

	private void updateHSV(int index, int value) {
		if (index == 0)
			currentHue = value;
		if (index == 1)
			currentSaturation = value / 100f;
		if (index == 2)
			currentBrightness = value / 100f;

		selectedRGB = new RGB(currentHue, currentSaturation, currentBrightness);
		updateSelectedColor();
	}

	private void updateSelectedColor() {
		if (selectedColor != null && !selectedColor.isDisposed()) {
			selectedColor.dispose();
		}
		selectedColor = new Color(getDisplay(), selectedRGB.red, selectedRGB.green, selectedRGB.blue, currentAlpha);
		drawArea.redraw();
	}

	private void onMouseUp(Event event) {
		int padding = 10;
		int hueBarWidth = 12;
		int sbBoxSize = 200;
		int alphaSliderHeight = 20;
		int sbX = hueBarWidth + padding * 2;
		int sbY = padding;

		int mouseX = event.x - sbX;
		int mouseY = event.y - sbY;

		if (mouseX >= 0 && mouseX < sbBoxSize && mouseY >= 0 && mouseY < sbBoxSize) {
			currentSaturation = mouseX / (float) sbBoxSize;
			currentBrightness = 1f - mouseY / (float) sbBoxSize;
		} else if (event.x >= padding && event.x < padding + hueBarWidth && event.y >= padding
				&& event.y < padding + sbBoxSize) {
			currentHue = 360f * (event.y - padding) / sbBoxSize;
		} else if (event.y >= sbBoxSize + padding * 2 && event.y < sbBoxSize + padding * 2 + alphaSliderHeight) {
			int alphaX = event.x - sbX;
			if (alphaX >= 0 && alphaX <= sbBoxSize) {
				currentAlpha = Math.max(0, Math.min(255, (int) (255f * alphaX / sbBoxSize)));
			}
		}

		selectedRGB = new RGB(currentHue, currentSaturation, currentBrightness);
		if (selectedColor != null && !selectedColor.isDisposed()) {
			selectedColor.dispose();
		}
		selectedColor = new Color(getDisplay(), selectedRGB.red, selectedRGB.green, selectedRGB.blue, currentAlpha);
		redraw();
	}

	private void onPaint(Event event) {
		Drawing.drawWithGC(drawArea, event.gc, this::paint);
	}

	private void paint(GC gc) {
		Display display = getDisplay();

		int padding = 10;
		int hueBarWidth = 12;
		int sbBoxSize = 200;
		int previewHeight = 24;
		int alphaSliderHeight = 20;

		int hueBarX = padding;
		int hueBarY = padding;
		int hueBarHeight = sbBoxSize;

		int sbX = hueBarX + hueBarWidth + padding;
		int sbY = padding;

		int previewX = sbX;
		int previewY = sbY + sbBoxSize + padding * 2 + alphaSliderHeight;

		// Hue Slider
		for (int i = 0; i < hueBarHeight; i++) {
			float hue = 360f * i / hueBarHeight;
			RGB rgb = new RGB(hue, 1f, 1f);
			Color color = new Color(display, rgb);
			gc.setForeground(color);
			gc.drawLine(hueBarX, hueBarY + i, hueBarX + hueBarWidth, hueBarY + i);
			color.dispose();
		}

		// Hue Thumb
		int thumbY = hueBarY + (int) (hueBarHeight * currentHue / 360f);
		gc.setBackground(display.getSystemColor(SWT.COLOR_WHITE));
		gc.setForeground(display.getSystemColor(SWT.COLOR_BLACK));
		gc.fillRoundRectangle(hueBarX - 2, thumbY - 4, hueBarWidth + 5, 6, 6, 6);
		gc.drawRoundRectangle(hueBarX - 2, thumbY - 4, hueBarWidth + 5, 6, 6, 6);

		// Selected Color Preview
		if (selectedColor == null || selectedColor.isDisposed()) {
			selectedColor = new Color(display, selectedRGB.red, selectedRGB.green, selectedRGB.blue, currentAlpha);
		}

		// SB Square
		for (int y = 0; y < sbBoxSize; y++) {
			float brightness = 1f - y / (float) sbBoxSize;
			for (int x = 0; x < sbBoxSize; x++) {
				float saturation = x / (float) sbBoxSize;
				RGB rgb = new RGB(currentHue, saturation, brightness);
				Color color = new Color(display, rgb);
				gc.setBackground(color);
				gc.fillRectangle(sbX + x, sbY + y, 1, 1);
				color.dispose();
			}
		}

		alphaSliderHeight = 17;
		int alphaSliderYOffset = 1;

		// Alpha Slider
		int alphaY = sbY + sbBoxSize + padding + alphaSliderYOffset;
		for (int x = 0; x < sbBoxSize; x++) {
			int alpha = (int) (255f * x / sbBoxSize);
			RGB rgb = new RGB(currentHue, currentSaturation, currentBrightness);
			Color color = new Color(display, rgb.red, rgb.green, rgb.blue, alpha);
			gc.setForeground(color);
			gc.drawLine(sbX + x, alphaY, sbX + x, alphaY + alphaSliderHeight);
			color.dispose();
		}

		// Alpha Thumb
		int thumbX = sbX + (int) (sbBoxSize * currentAlpha / 255f);
		int thumbWidth = 6;
		int thumbHeight = alphaSliderHeight + 2;

		gc.setBackground(display.getSystemColor(SWT.COLOR_WHITE));
		gc.setForeground(display.getSystemColor(SWT.COLOR_BLACK));
		gc.fillRoundRectangle(thumbX - thumbWidth / 2, alphaY - 1, thumbWidth, thumbHeight, 6, 6);
		gc.drawRoundRectangle(thumbX - thumbWidth / 2, alphaY - 1, thumbWidth, thumbHeight, 6, 6);

		// Selected Color Rectangle
		gc.setBackground(selectedColor);
		gc.fillRectangle(previewX, previewY, sbBoxSize, previewHeight);
		gc.setForeground(display.getSystemColor(SWT.COLOR_DARK_GRAY));
		gc.drawRectangle(previewX, previewY, sbBoxSize, previewHeight);

		String hex = String.format("#%02X%02X%02X (%d%%)", selectedRGB.red, selectedRGB.green, selectedRGB.blue,
				(int) (100 * currentAlpha / 255f));
		Point textExtent = gc.textExtent(hex);
		gc.drawText(hex, previewX + (sbBoxSize - textExtent.x) / 2, previewY + (previewHeight - textExtent.y) / 2,
				true);

		// SB Selector Cursor
		int cursorX = sbX + Math.round(currentSaturation * sbBoxSize);
		int cursorY = sbY + Math.round((1f - currentBrightness) * sbBoxSize);

		gc.setForeground(display.getSystemColor(SWT.COLOR_WHITE));
		gc.setLineWidth(2);
		gc.drawOval(cursorX - 4, cursorY - 4, 8, 8);

	}

	public Color getSelectedColor() {
		return selectedColor;
	}

	public RGB getSelectedRGB() {
		return selectedRGB;
	}

	public int getAlpha() {
		return currentAlpha;
	}
}
