/*******************************************************************************
 * Copyright (c) 2024 Yatta Solutions
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
package org.eclipse.swt.widgets;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

/**
 * Automated Tests for class org.eclipse.swt.widgets.Control for Windows
 * specific behavior
 *
 * @see org.eclipse.swt.widgets.Control
 */
@ExtendWith(PlatformSpecificExecutionExtension.class)
@ExtendWith(ResetMonitorSpecificScalingExtension.class)
class ControlWin32Tests {

	@Test
	public void testScaleFontCorrectlyInAutoScaleScenario() {
		DPIUtil.setMonitorSpecificScaling(true);
		Display display = Display.getDefault();

		assertTrue("Autoscale property is not set to true", display.isRescalingAtRuntime());
		int scalingFactor = 2;
		FontComparison fontComparison = updateFont(scalingFactor);
		assertEquals("Font height in pixels is not adjusted according to the scale factor",
				fontComparison.originalFontHeight * scalingFactor, fontComparison.currentFontHeight);
	}

	@Test
	public void testSetFontWithMonitorSpecificScalingEnabled() {
		DPIUtil.setMonitorSpecificScaling(true);
		Display display = Display.getDefault();
		Image colorImage = new Image(display, 10, 10);
		GC gc = new GC(colorImage);
		gc.setFont(display.getSystemFont());
		Font font = gc.getFont();
		assertEquals(display.getSystemFont(), font);
	}

	@Test
	public void testScaleFontCorrectlyInNoAutoScaleScenario() {
		DPIUtil.setMonitorSpecificScaling(false);
		Display display = Display.getDefault();

		assertFalse("Autoscale property is not set to false", display.isRescalingAtRuntime());
		int scalingFactor = 2;
		FontComparison fontComparison = updateFont(scalingFactor);
		assertEquals("Font height in pixels is not adjusted according to the scale factor",
				fontComparison.originalFontHeight * scalingFactor, fontComparison.currentFontHeight);
	}

	@Test
	public void testDoNotScaleFontInNoAutoScaleScenarioWithLegacyFontRegistry() {
		DPIUtil.setMonitorSpecificScaling(false);
		String originalValue = System.getProperty("swt.fontRegistry");
		System.setProperty("swt.fontRegistry", "legacy");
		try {
			Display display = Display.getDefault();

			assertFalse("Autoscale property is not set to false", display.isRescalingAtRuntime());
			int scalingFactor = 2;
			FontComparison fontComparison = updateFont(scalingFactor);
			assertEquals("Font height in pixels is different when setting the same font again",
					fontComparison.originalFontHeight, fontComparison.currentFontHeight);
		} finally {
			if (originalValue != null) {
				System.setProperty("swt.fontRegistry", originalValue);
			} else {
				System.clearProperty("swt.fontRegistry");
			}
		}
	}

	@Test
	public void testCorrectScaleUpUsingDifferentSetBoundsMethod() {
		DPIUtil.setMonitorSpecificScaling(true);
		Display display = Display.getDefault();
		Shell shell = new Shell(display);
		Button button = new Button(shell, SWT.PUSH);
		button.setText("Widget Test");
		shell.open();
		DPITestUtil.changeDPIZoom(shell, 175);

		button.setBounds(new Rectangle(0, 47, 200, 47));
		assertEquals("Control::setBounds(Rectangle) doesn't scale up correctly",
				new Rectangle(0, 82, 350, 83), button.getBoundsInPixels());

		button.setBounds(0, 47, 200, 47);
		assertEquals("Control::setBounds(int, int, int, int) doesn't scale up correctly",
				new Rectangle(0, 82, 350, 83), button.getBoundsInPixels());
	}

	@ParameterizedTest
	@CsvSource({ "0.5, 100, true", "1.0, 200, true", "2.0, 200, true", "2.0, quarter, true", "0.5, 100, false",
			"1.0, 200, false", "2.0, 200, false", "2.0, quarter, false", })
	public void testAutoScaleImageData(float scaleFactor, String autoScale, boolean monitorSpecificScaling) {
		DPIUtil.setMonitorSpecificScaling(monitorSpecificScaling);
		DPIUtil.runWithAutoScaleValue(autoScale, () -> {
			Display display = new Display();
			try {
				ImageData imageData = new ImageData(100, 100, 1, new PaletteData(new RGB(0, 0, 0)));
				int width = imageData.width;
				int height = imageData.height;
				int scaledWidth = Math.round(width * scaleFactor);
				int scaledHeight = Math.round(height * scaleFactor);
				ImageData scaledImageData = DPIUtil.autoScaleImageData(display, imageData, scaleFactor);
				assertEquals(scaledWidth, scaledImageData.width);
				assertEquals(scaledHeight, scaledImageData.height);
			} finally {
				display.dispose();
			}
		});
	}

	record FontComparison(int originalFontHeight, int currentFontHeight) {
	}

	private FontComparison updateFont(int scalingFactor) {
		Shell shell = new Shell(Display.getDefault());
		Control control = new Composite(shell, SWT.NONE);
		int zoom = shell.getNativeZoom();
		int newZoom = zoom * scalingFactor;

		Font oldFont = control.getFont();
		DPITestUtil.changeDPIZoom(shell, newZoom);
		control.setFont(oldFont);
		Font newFont = control.getFont();
		FontData fontData = oldFont.getFontData()[0];
		FontData currentFontData = newFont.getFontData()[0];
		int heightInPixels = fontData.data.lfHeight;
		int currentHeightInPixels = currentFontData.data.lfHeight;
		assertEquals("Font height in points is different on different zoom levels", fontData.getHeight(),
				currentFontData.getHeight());

		return new FontComparison(heightInPixels, currentHeightInPixels);
	}

}
