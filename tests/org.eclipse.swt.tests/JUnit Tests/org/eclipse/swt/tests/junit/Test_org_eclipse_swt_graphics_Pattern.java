/*******************************************************************************
 * Copyright (c) 2026 Vector Informatik GmbH and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package org.eclipse.swt.tests.junit;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Pattern;
import org.eclipse.swt.widgets.Display;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Automated Test Suite for class org.eclipse.swt.graphics.Pattern
 *
 * @see org.eclipse.swt.graphics.Pattern
 */
public class Test_org_eclipse_swt_graphics_Pattern {

	private Display display;

	@BeforeEach
	public void setUp() {
		display = Display.getDefault();
	}

	@AfterEach
	public void tearDown() {
		// display is shared; do not dispose
	}

	// --- Image-based constructor ---

	@Test
	public void test_Constructor_image_valid() {
		Image image = new Image(display, 10, 10);
		try {
			Pattern pattern = new Pattern(display, image);
			assertFalse(pattern.isDisposed(), "Newly constructed Pattern must not be disposed");
			pattern.dispose();
		} finally {
			image.dispose();
		}
	}

	@Test
	public void test_Constructor_image_nullImage() {
		assertThrows(IllegalArgumentException.class, () -> new Pattern(display, (Image) null));
	}

	@Test
	public void test_Constructor_image_disposedImage() {
		Image image = new Image(display, 10, 10);
		image.dispose();
		assertThrows(IllegalArgumentException.class, () -> new Pattern(display, image));
	}

	// --- Gradient constructors ---

	@Test
	public void test_Constructor_gradient_valid() {
		Color red = display.getSystemColor(SWT.COLOR_RED);
		Color blue = display.getSystemColor(SWT.COLOR_BLUE);
		Pattern pattern = new Pattern(display, 0, 0, 100, 100, red, blue);
		assertFalse(pattern.isDisposed(), "Newly constructed Pattern must not be disposed");
		pattern.dispose();
	}

	@Test
	public void test_Constructor_gradientAlpha_valid() {
		Color red = display.getSystemColor(SWT.COLOR_RED);
		Color blue = display.getSystemColor(SWT.COLOR_BLUE);
		Pattern pattern = new Pattern(display, 0, 0, 100, 100, red, 128, blue, 255);
		assertFalse(pattern.isDisposed(), "Newly constructed Pattern must not be disposed");
		pattern.dispose();
	}

	@Test
	public void test_Constructor_gradient_nullColor1() {
		Color blue = display.getSystemColor(SWT.COLOR_BLUE);
		assertThrows(IllegalArgumentException.class,
				() -> new Pattern(display, 0, 0, 100, 100, null, blue));
	}

	@Test
	public void test_Constructor_gradient_nullColor2() {
		Color red = display.getSystemColor(SWT.COLOR_RED);
		assertThrows(IllegalArgumentException.class,
				() -> new Pattern(display, 0, 0, 100, 100, red, null));
	}

	@Test
	public void test_Constructor_gradientAlpha_nullColor1() {
		Color blue = display.getSystemColor(SWT.COLOR_BLUE);
		assertThrows(IllegalArgumentException.class,
				() -> new Pattern(display, 0, 0, 100, 100, null, 255, blue, 255));
	}

	@Test
	public void test_Constructor_gradientAlpha_nullColor2() {
		Color red = display.getSystemColor(SWT.COLOR_RED);
		assertThrows(IllegalArgumentException.class,
				() -> new Pattern(display, 0, 0, 100, 100, red, 255, null, 255));
	}

	@Test
	public void test_Constructor_gradient_disposedColor1() {
		Color red = new Color(display, 255, 0, 0);
		Color blue = display.getSystemColor(SWT.COLOR_BLUE);
		red.dispose();
		assertThrows(IllegalArgumentException.class,
				() -> new Pattern(display, 0, 0, 100, 100, red, blue));
	}

	@Test
	public void test_Constructor_gradient_disposedColor2() {
		Color red = display.getSystemColor(SWT.COLOR_RED);
		Color blue = new Color(display, 0, 0, 255);
		blue.dispose();
		assertThrows(IllegalArgumentException.class,
				() -> new Pattern(display, 0, 0, 100, 100, red, blue));
	}

	@Test
	public void test_Constructor_gradientAlpha_disposedColor1() {
		Color red = new Color(display, 255, 0, 0);
		Color blue = display.getSystemColor(SWT.COLOR_BLUE);
		red.dispose();
		assertThrows(IllegalArgumentException.class,
				() -> new Pattern(display, 0, 0, 100, 100, red, 255, blue, 255));
	}

	@Test
	public void test_Constructor_gradientAlpha_disposedColor2() {
		Color red = display.getSystemColor(SWT.COLOR_RED);
		Color blue = new Color(display, 0, 0, 255);
		blue.dispose();
		assertThrows(IllegalArgumentException.class,
				() -> new Pattern(display, 0, 0, 100, 100, red, 255, blue, 255));
	}

	// --- isDisposed ---

	@Test
	public void test_isDisposed() {
		Color red = display.getSystemColor(SWT.COLOR_RED);
		Color blue = display.getSystemColor(SWT.COLOR_BLUE);
		Pattern pattern = new Pattern(display, 0, 0, 100, 100, red, blue);

		assertFalse(pattern.isDisposed(), "Pattern must not be disposed before dispose() is called");
		pattern.dispose();
		assertTrue(pattern.isDisposed(), "Pattern must be disposed after dispose() is called");
	}

	@Test
	public void test_dispose_twice() {
		Color red = display.getSystemColor(SWT.COLOR_RED);
		Color blue = display.getSystemColor(SWT.COLOR_BLUE);
		Pattern pattern = new Pattern(display, 0, 0, 100, 100, red, blue);

		// Disposing twice must not throw
		pattern.dispose();
		pattern.dispose();
		assertTrue(pattern.isDisposed());
	}

	// --- toString ---

	@Test
	public void test_toString() {
		Color red = display.getSystemColor(SWT.COLOR_RED);
		Color blue = display.getSystemColor(SWT.COLOR_BLUE);
		Pattern pattern = new Pattern(display, 0, 0, 100, 100, red, blue);

		String s = pattern.toString();
		assertNotNull(s, "toString() must not return null");
		assertFalse(s.isEmpty(), "toString() must not return an empty string");

		pattern.dispose();
		s = pattern.toString();
		assertNotNull(s, "toString() on disposed Pattern must not return null");
		assertFalse(s.isEmpty(), "toString() on disposed Pattern must not return an empty string");
	}

}
