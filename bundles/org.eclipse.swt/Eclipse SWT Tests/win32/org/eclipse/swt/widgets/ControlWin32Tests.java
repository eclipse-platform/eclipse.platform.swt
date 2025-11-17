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
		Win32DPIUtils.setMonitorSpecificScaling(true);
		Display display = Display.getDefault();

		assertTrue("Autoscale property is not set to true", display.isRescalingAtRuntime());
		int scalingFactor = 2;
		FontComparison fontComparison = updateFont(scalingFactor);
		assertEquals("Font height in pixels is not adjusted according to the scale factor",
				fontComparison.originalFontHeight * scalingFactor, fontComparison.currentFontHeight);
	}

	@Test
	public void testSetFontWithMonitorSpecificScalingEnabled() {
		Win32DPIUtils.setMonitorSpecificScaling(true);
		Display display = Display.getDefault();
		Image colorImage = new Image(display, 10, 10);
		GC gc = new GC(colorImage);
		gc.setFont(display.getSystemFont());
		Font font = gc.getFont();
		assertEquals(display.getSystemFont(), font);
	}

	@Test
	public void testDoNotScaleFontInNoAutoScaleScenario() {
		Win32DPIUtils.setMonitorSpecificScaling(false);
		Display display = Display.getDefault();

		assertFalse("Autoscale property is not set to false", display.isRescalingAtRuntime());
		int scalingFactor = 2;
		FontComparison fontComparison = updateFont(scalingFactor);
		assertEquals("Font height in pixels is different when setting the same font again",
				fontComparison.originalFontHeight, fontComparison.currentFontHeight);
	}

	@Test
	public void testDoNotScaleFontInNoAutoScaleScenarioWithLegacyFontRegistry() {
		Win32DPIUtils.setMonitorSpecificScaling(false);
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
		Win32DPIUtils.setMonitorSpecificScaling(true);
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
	@CsvSource({ "2.0, quarter, true", "0.5, 100, false",
			"1.0, 200, false", "2.0, 200, false", "2.0, quarter, false", })
	public void testAutoScaleImageData(float scaleFactor, String autoScale, boolean monitorSpecificScaling) {
		Win32DPIUtils.setMonitorSpecificScaling(monitorSpecificScaling);
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

	/**
	 * Scenario:
	 * <ul>
	 * <li>parent has bounds with an offset (x != 0) to its parent
	 * <li>child fills the composite, such that both their widths are equal
	 * </ul>
	 * Depending on how the offset of the parent (x value of bounds) is taken
	 * into account when rounding during point-to-pixel conversion, the parent
	 * composite may become one pixel too large or small for the child.
	 */
	@Test
	void testChildFillsCompositeWithOffset() {
		Win32DPIUtils.setMonitorSpecificScaling(true);
		// pixel values at 125%: (2.5, 2.5, 2.5, 2.5) --> when rounding bottom right
		// corner (pixel value (5, 5)) instead of width/height independently, will be
		// rounded to (3, 3, 2, 2) --> too small for child
		Rectangle parentBounds = new Rectangle(2, 2, 2, 2);
		// pixel values at 125%: (0, 0, 2.5, 2.5) --> will be rounded to (0, 0, 3, 3)
		Rectangle childBounds = new Rectangle(0, 0, 2, 2);

		Display display = Display.getDefault();
		Shell shell = new Shell(display);
		Composite parent = new Composite(shell, SWT.NONE);
		DPITestUtil.changeDPIZoom(shell, 125);
		parent.setBounds(parentBounds);
		Button child = new Button(parent, SWT.PUSH);
		child.setBounds(childBounds);

		Rectangle parentBoundsInPixels = parent.getBoundsInPixels();
		Rectangle childBoundsInPixels = child.getBoundsInPixels();
		assertEquals(parentBoundsInPixels.x, 3);
		assertEquals(childBoundsInPixels.x, 0);
		assertEquals(parentBoundsInPixels.width, childBoundsInPixels.width);
		assertEquals(parentBoundsInPixels.height, childBoundsInPixels.height);
		assertEquals(childBounds, child.getBounds());
	}

	/**
	 * Scenario:
	 * <ul>
	 * <li>parent has bounds with an offset (x = 0) to its parent
	 * <li>child has an offset (x != 0) to parent and exactly fills the rest of the
	 * composite, such that child.x+child.width is equal to parent.x
	 * </ul>
	 * Depending on how the offset of the child (x value of bounds) is taken into
	 * account when rounding during point-to-pixel conversion, the child may become
	 * one pixel too large to fit into the parent.
	 */
	@Test
	void testChildWithOffsetFillsComposite() {
		Win32DPIUtils.setMonitorSpecificScaling(true);
		// pixel values at 125%: (0, 0, 5, 5)
		Rectangle parentBounds = new Rectangle(0, 0, 4, 4);
		// pixel values at 125%: (2.5, 2.5, 2.5, 2.5) --> when rounding width/height
		// independently instead of bottom right corner, will be rounded to
		// (3, 3, 3, 3) --> too large for parent
		Rectangle childBounds = new Rectangle(2, 2, 2, 2);

		Display display = Display.getDefault();
		Shell shell = new Shell(display);
		Composite parent = new Composite(shell, SWT.NONE);
		DPITestUtil.changeDPIZoom(shell, 125);
		parent.setBounds(parentBounds);
		Button child = new Button(parent, SWT.PUSH);

		child.setBounds(childBounds);
		assertChildFitsIntoParent(parent, child);
		assertEquals(parentBounds, parent.getBounds());
		assertEquals(childBounds, child.getBounds());

		child.setSize(childBounds.width, childBounds.height);
		assertChildFitsIntoParent(parent, child);
		assertEquals(parentBounds, parent.getBounds());
		assertEquals(childBounds, child.getBounds());
	}

	private void assertChildFitsIntoParent(Control parent, Control child) {
		Rectangle parentBoundsInPixels = parent.getBoundsInPixels();
		Rectangle childBoundsInPixels = child.getBoundsInPixels();
		Rectangle childBounds = child.getBounds();
		assertEquals(parentBoundsInPixels.width, childBoundsInPixels.x + childBoundsInPixels.width);
		assertEquals(parentBoundsInPixels.height, childBoundsInPixels.y + childBoundsInPixels.height);
		assertEquals(parent.getBounds().width, childBounds.x + childBounds.width);
		assertEquals(parent.getBounds().height, childBounds.y + childBounds.height);
	}

	/**
	 * Scenario: Layouting
	 * <p>
	 * Layouts use client area of composites to calculate the sizes of the contained
	 * controls. The rounded values of that client area can lead to child bounds be
	 * calculated larger than the actual available size. This serves as unit test
	 * for that behavior in addition to the integration test
	 * {@link #testChildFillsScrollableWithBadlyRoundedClientArea()}
	 */
	@Test
	void testFitChildIntoParent() {
		Win32DPIUtils.setMonitorSpecificScaling(true);
		// pixel values at 125%: (0, 0, 5, 5)
		Rectangle parentBounds = new Rectangle(0, 0, 4, 4);
		// pixel values at 125%: (2.5, 2.5, 3.75, 3.75) --> rounded to (3, 3, 3, 3)
		Rectangle childBounds = new Rectangle(2, 2, 3, 3);

		Display display = Display.getDefault();
		Shell shell = new Shell(display);
		Composite parent = new Composite(shell, SWT.NONE);
		DPITestUtil.changeDPIZoom(shell, 125);
		parent.setBounds(parentBounds);
		Button child = new Button(parent, SWT.PUSH);

		child.setBounds(childBounds);
		assertChildFitsIntoParent(parent, child);

		child.setSize(childBounds.width, childBounds.height);
		assertChildFitsIntoParent(parent, child);
	}

	// Ensure that the fitting logic does only apply at off-by-one calculation results
	// and not for fitting actually larger childs into parts
	@Test
	void testFitChildIntoParent_limitedSizeDifference() {
		Win32DPIUtils.setMonitorSpecificScaling(true);
		// pixel values at 125%: (0, 0, 5, 5)
		Rectangle parentBounds = new Rectangle(0, 0, 4, 4);
		// pixel values at 125%: (2.5, 2.5, 5, 5) --> rounded to (3, 3, 5, 5)
		Rectangle childBounds = new Rectangle(2, 2, 4, 4);

		Display display = Display.getDefault();
		Shell shell = new Shell(display);
		Composite parent = new Composite(shell, SWT.NONE);
		DPITestUtil.changeDPIZoom(shell, 125);
		parent.setBounds(parentBounds);
		Button child = new Button(parent, SWT.PUSH);

		child.setBounds(childBounds);
		assertChildNotFitsIntoParent(parent, child);

		child.setSize(childBounds.width, childBounds.height);
		assertChildNotFitsIntoParent(parent, child);
	}

	private void assertChildNotFitsIntoParent(Control parent, Control child) {
		Rectangle parentBoundsInPixels = parent.getBoundsInPixels();
		Rectangle childBoundsInPixels = child.getBoundsInPixels();
		Rectangle childBounds = child.getBounds();
		assertNotEquals(parentBoundsInPixels.width, childBoundsInPixels.x + childBoundsInPixels.width);
		assertNotEquals(parentBoundsInPixels.height, childBoundsInPixels.y + childBoundsInPixels.height);
		assertNotEquals(parent.getBounds().width, childBounds.x + childBounds.width);
		assertNotEquals(parent.getBounds().height, childBounds.y + childBounds.height);
	}

	/**
	 * Scenario: Layouting
	 * <p>
	 * Layouts use client area of composites to calculate the sizes of the contained
	 * controls. The rounded values of that client area can lead to child bounds be
	 * calculated larger than the actual available size.
	 */
	@Test
	void testChildFillsScrollableWithBadlyRoundedClientArea() {
		Win32DPIUtils.setMonitorSpecificScaling(true);
		Display display = Display.getDefault();
		Shell shell = new Shell(display);
		Composite parent = new Composite(shell, SWT.H_SCROLL|SWT.V_SCROLL);
		DPITestUtil.changeDPIZoom(shell, 125);
		// Find parent bounds such that client area is rounded to a value that,
		// when converted back to pixels, is one pixel too large
		Rectangle parentBounds = new Rectangle(0, 0, 4, 4);
		Rectangle clientAreaInPixels;
		do {
			do {
				parentBounds.width += 1;
				parentBounds.height += 1;
				parent.setBounds(parentBounds);
				Rectangle clientArea = parent.getClientArea();
				clientAreaInPixels = Win32DPIUtils
						.pointToPixel(new Rectangle(clientArea.x, clientArea.y, clientArea.width, clientArea.height), 125);
			} while (clientAreaInPixels.width <= parent.getClientAreaInPixels().width && clientAreaInPixels.width < 50);
			parentBounds.x += 1;
			parentBounds.y += 1;
			if (parentBounds.x >= 50) {
				fail("No scrolable size with non-invertible point/pixel conversion for its client area could be created");
			}
		} while (clientAreaInPixels.width <= parent.getClientAreaInPixels().width);
		Button child = new Button(parent, SWT.PUSH);
		Rectangle childBounds = new Rectangle(0, 0, parent.getClientArea().width, parent.getClientArea().height);
		child.setBounds(childBounds);

		clientAreaInPixels  = parent.getClientAreaInPixels();
		Rectangle childBoundsInPixels = child.getBoundsInPixels();
		assertTrue(clientAreaInPixels.width <= childBoundsInPixels.x + childBoundsInPixels.width);
		assertTrue(clientAreaInPixels.height <= childBoundsInPixels.y + childBoundsInPixels.height);

		child.setSize(childBounds.width, childBounds.height);

		clientAreaInPixels  = parent.getClientAreaInPixels();
		childBoundsInPixels = child.getBoundsInPixels();
		assertTrue(clientAreaInPixels.width <= childBoundsInPixels.x + childBoundsInPixels.width);
		assertTrue(clientAreaInPixels.height <= childBoundsInPixels.y + childBoundsInPixels.height);
	}

}
