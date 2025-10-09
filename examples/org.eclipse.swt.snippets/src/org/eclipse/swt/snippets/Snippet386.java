/*******************************************************************************
 * Copyright (c) 2025 Yatta Solutions
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Yatta Solutions - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.snippets;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

/**
 * Demonstrates various ways to construct and use custom cursors in an SWT
 * application.
 * <p>
 * This example provides a graphical user interface that allows users to select
 * from several different {@link org.eclipse.swt.graphics.Cursor} constructors,
 * showing the resulting cursor in real time on the application shell. It also
 * dynamically updates to reflect changes in system DPI scaling (zoom),
 * displaying the expected cursor size and drawing a set of reference ticks to
 * help visualize scaling.
 * <p>
 * Features of this snippet include:
 * <ul>
 * <li>Combo box to choose between multiple cursor constructors, including
 * system, custom image, and DPI-aware providers.</li>
 * <li>Live update of the shell cursor based on the user's selection.</li>
 * <li>Display of current system zoom level and expected cursor size for visual
 * reference.</li>
 * <li>Painted tick marks and labels to visualize scaling and cursor positioning
 * at different zooms.</li>
 * <li>DPI change listener to update UI when system zoom changes.</li>
 * </ul>
 *
 * This snippet is intended for educational and demonstration purposes to aid
 * understanding of cursor creation and DPI handling in SWT.
 *
 * <p>
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 * </p>
 */
public class Snippet386 {

	private static final int IMAGE_SIZE_IN_POINTS = 16;

	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = createShell(display);

		Combo combo = createConstructorCombo(shell);
		Label zoomLabel = createZoomLabel(shell);

		addZoomChangedListener(shell, zoomLabel);
		Group section = new Group(shell, SWT.NONE);
		section.setText("Scale");
		section.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		section.setLayout(new FillLayout());
		addPaintTicks(section);

		CursorManager cursorManager = new CursorManager(display, shell, combo);
		combo.addListener(SWT.Selection, e -> cursorManager.updateCursor());
		cursorManager.updateCursor();

		shell.setSize(400, 400);
		shell.open();

		eventLoop(display, shell);
		display.dispose();
	}

	private static Shell createShell(Display display) {
		Shell shell = new Shell(display);
		shell.setText("Snippet 386");
		shell.setLayout(new GridLayout(1, false));
		return shell;
	}

	private static Combo createConstructorCombo(Composite parent) {
		Label label = new Label(parent, SWT.NONE);
		label.setText("Choose Cursor Constructor:");

		Combo combo = new Combo(parent, SWT.READ_ONLY);
		combo.setItems("Cursor(Device, int)", "Cursor(Device, ImageData, ImageData, int, int)",
				"Cursor(Device, ImageData, int, int)", "Cursor(Device, ImageDataProvider, int, int)");
		combo.select(0);
		return combo;
	}

	private static Label createZoomLabel(Shell parent) {
		Label zoomLabel = new Label(parent, SWT.NONE);
		zoomLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		setZoomLabelText(parent, zoomLabel);
		return zoomLabel;
	}

	private static void setZoomLabelText(Shell shell, Label label) {
		int zoom = shell.getMonitor().getZoom();
		int expectedCursorSize = Math.round(IMAGE_SIZE_IN_POINTS * (zoom / 100f));
		label.setText("Current zoom: " + zoom + "% \nExpected Cursor Size = " + expectedCursorSize);
	}

	private static void addZoomChangedListener(Shell shell, Label zoomLabel) {
		shell.addListener(SWT.Resize, event -> {
			setZoomLabelText(shell, zoomLabel);
			shell.layout();
		});
	}

	private static void addPaintTicks(Composite composite) {
		composite.addPaintListener(event -> {
			drawTicks(composite, event.gc);
		});
	}

	private static void drawTicks(Composite shell, GC gc) {
		int deviceZoom = shell.getMonitor().getZoom();
		float devScale = deviceZoom / 100f;
		Point client = shell.getSize();
		int xPos = (int) ((client.x / 2) - (6 * (IMAGE_SIZE_IN_POINTS / devScale)));
		int tickHeight = 10;
		int yPos = 20;

		for (int tickIndex = 0; tickIndex < 6; tickIndex++) {
			xPos += (IMAGE_SIZE_IN_POINTS / devScale);
			int yOffset = (tickIndex % 3 == 1) ? 20 : (tickIndex % 3 == 2) ? 40 : 0;

			gc.drawLine(xPos, yPos, xPos, yPos + tickHeight);
			gc.drawText(Integer.toString(tickIndex * IMAGE_SIZE_IN_POINTS), xPos - 5, yPos + 12 + yOffset);
		}
	}

	private static void eventLoop(Display display, Shell shell) {
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
	}

	public static ImageData createSolidColorImageData(int size, RGB color) {
		PaletteData palette = new PaletteData(0xFF0000, 0x00FF00, 0x0000FF);
		ImageData imageData = new ImageData(size, size, 24, palette);

		int pixel = palette.getPixel(color);
		for (int y = 0; y < size; y++) {
			for (int x = 0; x < size; x++) {
				imageData.setPixel(x, y, pixel);
			}
		}
		return imageData;
	}

	private static class CursorManager {
		private final Display display;
		private final Shell shell;
		private final Combo combo;

		CursorManager(Display display, Shell shell, Combo combo) {
			this.display = display;
			this.shell = shell;
			this.combo = combo;
		}

		void updateCursor() {
			int selection = combo.getSelectionIndex();
			Cursor oldCursor = shell.getCursor();
			if (oldCursor != null && !oldCursor.isDisposed()) {
				oldCursor.dispose();
			}
			Cursor cursor = createCursor(selection);
			if (cursor != null) {
				shell.setCursor(cursor);
			}
		}

		private Cursor createCursor(int selection) {
			switch (selection) {
			case 0:
				return new Cursor(display, SWT.CURSOR_HAND);
			case 1: {
				PaletteData rgbPalette = new PaletteData(0xFF0000, 0x00FF00, 0x0000FF);
				int bluePixel = rgbPalette.getPixel(new RGB(0, 0, 255));
				ImageData source = new ImageData(IMAGE_SIZE_IN_POINTS, IMAGE_SIZE_IN_POINTS, 24,
						new PaletteData(0xFF0000, 0x00FF00, 0x0000FF));

				for (int x = 0; x < IMAGE_SIZE_IN_POINTS; x++) {
					for (int y = 0; y < IMAGE_SIZE_IN_POINTS; y++) {
						source.setPixel(x, y, bluePixel);
					}
				}

				ImageData mask = new ImageData(IMAGE_SIZE_IN_POINTS, IMAGE_SIZE_IN_POINTS, 1,
						new PaletteData(new RGB(0, 0, 0), new RGB(255, 255, 255)));
				for (int x = 0; x < IMAGE_SIZE_IN_POINTS; x++) {
					for (int y = 0; y < IMAGE_SIZE_IN_POINTS; y++) {
						mask.setPixel(x, y, x % 2);
					}
				}
				@SuppressWarnings("deprecation")
				Cursor cursor = new Cursor(display, source, mask, IMAGE_SIZE_IN_POINTS / 2, IMAGE_SIZE_IN_POINTS / 2);
				return cursor;
			}
			case 2:
				RGB red = new RGB(255, 0, 0);
				return new Cursor(display, createSolidColorImageData(IMAGE_SIZE_IN_POINTS, red), 0, 0);

			case 3: {
				RGB green = new RGB(0, 255, 0);
				ImageDataProvider provider = zoom -> {
					return createSolidColorImageData(IMAGE_SIZE_IN_POINTS * zoom / 100, green);
				};
				return new Cursor(display, provider, 0, 0);
			}
			default:
				return null;
			}
		}
	}
}