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
import org.eclipse.swt.internal.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

/**
 * Demonstrates various ways to construct and use custom cursors in an SWT application.
 * <p>
 * This example provides a graphical user interface that allows users to select from several
 * different {@link org.eclipse.swt.graphics.Cursor} constructors, showing the resulting cursor
 * in real time on the application shell. It also dynamically updates to reflect changes in
 * system DPI scaling (zoom), displaying the expected cursor size and drawing a set of
 * reference ticks to help visualize scaling.
 * <p>
 * Features of this snippet include:
 * <ul>
 *   <li>Combo box to choose between multiple cursor constructors, including system, custom image, and
 *       DPI-aware providers.</li>
 *   <li>Live update of the shell cursor based on the user's selection.</li>
 *   <li>Display of current system zoom level and expected cursor size for visual reference.</li>
 *   <li>Painted tick marks and labels to visualize scaling and cursor positioning at different zooms.</li>
 *   <li>DPI change listener to update UI when system zoom changes.</li>
 * </ul>
 *
 * This snippet is intended for educational and demonstration purposes to aid understanding of
 * cursor creation and DPI handling in SWT.
 *
 * <p>
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 * </p>
 */
public class Snippet386 {

	private static final int IMAGE_SIZE_IN_POINTS = 16;

	public static void main(String[] args) {
		System.setProperty("swt.autoScale.updateOnRuntime", "true");
		Display display = new Display();
		Shell shell = createShell(display);

		Label zoomLabel = createZoomLabel(shell);
		Combo combo = createConstructorCombo(shell);

		addZoomChangedListener(shell, zoomLabel);
		addPaintTicks(shell);

		CursorManager cursorManager = new CursorManager(display, shell, combo);
		combo.addListener(SWT.Selection, e -> cursorManager.updateCursor());
		cursorManager.updateCursor();

		shell.setSize(300, 600);
		shell.open();

		eventLoop(display, shell);
		display.dispose();
	}

	private static Shell createShell(Display display) {
		Shell shell = new Shell(display);
		shell.setText("Cursor Constructors Example");
		shell.setLayout(new GridLayout(1, false));

		Label label = new Label(shell, SWT.NONE);
		label.setText("Choose Cursor Constructor:");
		return shell;
	}

	private static Combo createConstructorCombo(Composite parent) {
		Combo combo = new Combo(parent, SWT.READ_ONLY);
		combo.setItems("Cursor(Device, int)", "Cursor(Device, ImageData, ImageData, int, int)",
				"Cursor(Device, ImageData, int, int)", "Cursor(Device, ImageDataProvider, int, int)");
		combo.select(0);
		return combo;
	}

	private static Label createZoomLabel(Composite parent) {
		Label zoomLabel = new Label(parent, SWT.NONE);
		zoomLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		setZoomLabelText(zoomLabel);
		return zoomLabel;
	}

	private static void setZoomLabelText(Label label) {
		@SuppressWarnings("restriction")
		int zoom = DPIUtil.getDeviceZoom();
		int expectedCursorSize = Math.round(IMAGE_SIZE_IN_POINTS * (zoom / 100f));
		label.setText("Current zoom: " + zoom + "% Expected Cursor Size = " + expectedCursorSize);
	}

	private static void addZoomChangedListener(Shell shell, Label zoomLabel) {
		shell.addListener(SWT.ZoomChanged, event -> {
			setZoomLabelText(zoomLabel);
			shell.layout();
		});
	}

	private static void addPaintTicks(Shell shell) {
		shell.addPaintListener(event -> {
			drawTicks(event.gc);
		});
	}

	private static void drawTicks(GC gc) {
		@SuppressWarnings("restriction")
		int deviceZoom = DPIUtil.getDeviceZoom();
		float devScale = deviceZoom / 100f;
		int yPos = 300;
		int tickHeight = 10;

		for (int tickIndex = 0; tickIndex < 6; tickIndex++) {
			int xPos = 100 + tickIndex * IMAGE_SIZE_IN_POINTS;
			int yOffset = (tickIndex % 3 == 1) ? 20 : (tickIndex % 3 == 2) ? 40 : 0;
			int xPosUnscaled = (int) (xPos / devScale);
			int yPosUnscaled = (int) (yPos / devScale);

			gc.drawLine(xPosUnscaled, yPosUnscaled, xPosUnscaled, yPosUnscaled + tickHeight);
			gc.drawText(Integer.toString(tickIndex * IMAGE_SIZE_IN_POINTS), xPosUnscaled - 5, yPosUnscaled + 12 + yOffset);
		}
	}

	private static void eventLoop(Display display, Shell shell) {
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
	}

	// --- Utility: creates solid color square ImageData ---
	public static ImageData createSolidColorImageData(int size, RGB color) {
		PaletteData palette = new PaletteData(0xFF0000, 0x00FF00, 0x0000FF);
		ImageData imageData = new ImageData(size, size, 24, palette);

		int pixel = palette.getPixel(color);
		for (int y = 0; y < size; y++)
			for (int x = 0; x < size; x++)
				imageData.setPixel(x, y, pixel);

		return imageData;
	}

	// --- Cursor management ---
	private static class CursorManager {
		private final Display display;
		private final Shell shell;
		private final Combo combo;

		CursorManager(Display display, Shell shell, Combo combo) {
			this.display = display;
			this.shell = shell;
			this.combo = combo;

			// Dispose the final cursor when the Display is disposed
	        display.addListener(SWT.Dispose, e -> {
	            Cursor cursor = shell.getCursor();
	            if (cursor != null && !cursor.isDisposed()) {
	                cursor.dispose();
	            }
	        });
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
				ImageData source = new ImageData(IMAGE_SIZE_IN_POINTS, IMAGE_SIZE_IN_POINTS, 1,
						new PaletteData(new RGB[] { new RGB(0, 255, 0) }));
				ImageData mask = new ImageData(IMAGE_SIZE_IN_POINTS, IMAGE_SIZE_IN_POINTS, 1,
						new PaletteData(new RGB[] { new RGB(0, 255, 0) }));
				return new Cursor(display, source, mask, 0, 0);
			}

			case 2:
				return new Cursor(display, createSolidColorImageData(IMAGE_SIZE_IN_POINTS, new RGB(128, 0, 128)), 0, 0);

			case 3: {
				RGB green = new RGB(0, 255, 0);
				ImageDataProvider provider = zoom -> {
					switch (zoom) {
					case 100:
						return createSolidColorImageData(16, green);
					case 150:
						return createSolidColorImageData(24, green);
					case 200:
						return createSolidColorImageData(32, green);
					case 250:
						return createSolidColorImageData(40, green);
					case 300:
						return createSolidColorImageData(48, green);
					case 350:
						return createSolidColorImageData(56, green);
					default:
						return createSolidColorImageData(64, green);
					}
				};
				return new Cursor(display, provider, 0, 0);
			}
			default:
				return null;
			}
		}
	}
}