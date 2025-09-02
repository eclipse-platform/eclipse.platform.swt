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
 *     Red Hat Inc. - Bug 462631
 *******************************************************************************/
package org.eclipse.swt.tests.junit;


import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.InputStream;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageDataProvider;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;
import org.junit.Before;
import org.junit.Test;

/**
 * Automated Test Suite for class org.eclipse.swt.graphics.Cursor
 *
 * @see org.eclipse.swt.graphics.Cursor
 */
public class Test_org_eclipse_swt_graphics_Cursor {

@Before
public void setUp() {
	display = Display.getDefault();
}

@Test
public void test_ConstructorLorg_eclipse_swt_graphics_DeviceI() {
	// Test new Cursor(Device device, int style)
	// IllegalArgumentException when an unknown style is specified

	Cursor cursor = new Cursor(display, SWT.CURSOR_ARROW);
	cursor.dispose();

	cursor = new Cursor(display, SWT.CURSOR_WAIT);
	cursor.dispose();

	cursor = new Cursor(display, SWT.CURSOR_CROSS);
	cursor.dispose();

	cursor = new Cursor(display, SWT.CURSOR_APPSTARTING);
	cursor.dispose();

	cursor = new Cursor(display, SWT.CURSOR_HELP);
	cursor.dispose();

	cursor = new Cursor(display, SWT.CURSOR_SIZEALL);
	cursor.dispose();

	cursor = new Cursor(display, SWT.CURSOR_SIZENESW);
	cursor.dispose();

	cursor = new Cursor(display, SWT.CURSOR_SIZENS);
	cursor.dispose();

	cursor = new Cursor(display, SWT.CURSOR_SIZENWSE);
	cursor.dispose();

	cursor = new Cursor(display, SWT.CURSOR_SIZEWE);
	cursor.dispose();

	cursor = new Cursor(display, SWT.CURSOR_SIZEN);
	cursor.dispose();

	cursor = new Cursor(display, SWT.CURSOR_SIZES);
	cursor.dispose();

	cursor = new Cursor(display, SWT.CURSOR_SIZEE);
	cursor.dispose();

	cursor = new Cursor(display, SWT.CURSOR_SIZEW);
	cursor.dispose();

	cursor = new Cursor(display, SWT.CURSOR_SIZENE);
	cursor.dispose();

	cursor = new Cursor(display, SWT.CURSOR_SIZESE);
	cursor.dispose();

	cursor = new Cursor(display, SWT.CURSOR_SIZESW);
	cursor.dispose();

	cursor = new Cursor(display, SWT.CURSOR_SIZENW);
	cursor.dispose();

	cursor = new Cursor(display, SWT.CURSOR_UPARROW);
	cursor.dispose();

	cursor = new Cursor(display, SWT.CURSOR_IBEAM);
	cursor.dispose();

	cursor = new Cursor(display, SWT.CURSOR_NO);
	cursor.dispose();

	cursor = new Cursor(display, SWT.CURSOR_HAND);
	cursor.dispose();

	// device == null (valid)
	cursor = new Cursor(null, SWT.CURSOR_ARROW);
	cursor.dispose();

	// illegal argument, style > SWT.CURSOR_HAND (21)
	assertThrows("No exception thrown for style > SWT.CURSOR_HAND (21)",
			IllegalArgumentException.class, () -> new Cursor(display, 100));

	// illegal argument, style < 0
	assertThrows("No exception thrown for style < 0",
			IllegalArgumentException.class, () -> new Cursor(display, -100));
}

@Test
public void test_ConstructorLorg_eclipse_swt_graphics_DeviceLorg_eclipse_swt_graphics_ImageDataLorg_eclipse_swt_graphics_ImageDataII() {
	// Test new Cursor(Device device, ImageData source, ImageData mask, int hotspotX, int hotspotY)
	String fileName = SwtTestUtil.imageFilenames[0];
	for (String format : SwtTestUtil.imageFormats) {
		ImageLoader loader = new ImageLoader();
		try (InputStream stream = SwtTestUtil.class.getResourceAsStream(fileName + "." + format)) {
			ImageData source = loader.load(stream)[0];
			ImageData mask = source.getTransparencyMask();
			if (mask != null && (source.depth == 1)) {
				Cursor cursor = new Cursor(display, source, mask, 0, 0);
				cursor.dispose();
			}
		} catch (IOException e) {
			// continue;
		}
	}
}

@Test
public void test_ConstructorWithImageDataProvider() {
	// Test new Cursor(Device device, ImageData source, ImageData mask, int
	// hotspotX, int hotspotY)
	Image sourceImage = new Image(display, 10, 10);
	Cursor cursor = new Cursor(display, sourceImage::getImageData, 0, 0);
	cursor.dispose();
	cursor = new Cursor(null, sourceImage::getImageData, 0, 0);
	cursor.dispose();
	sourceImage.dispose();

	assertThrows("No exception thrown when ImageDataProvider is null",
			IllegalArgumentException.class, () -> new Cursor(display, (ImageDataProvider) null, 0, 0));
}

@Test
public void test_InvalidArgumentsForAllConstructors() {
	ImageData source = new ImageData(16, 16, 1, new PaletteData(new RGB[] { new RGB(0, 0, 0) }));
	ImageData mask = new ImageData(16, 16, 1, new PaletteData(new RGB[] { new RGB(0, 0, 0) }));

	assertThrows("When wrong style was provided", IllegalArgumentException.class,
			() -> {
				Cursor cursor = new Cursor(Display.getDefault(), -99);
				cursor.dispose();
			});

	assertThrows("When source is null", IllegalArgumentException.class, () -> {
		Cursor cursorFromImageAndMask = new Cursor(Display.getDefault(), null, mask, 0, 0);
		cursorFromImageAndMask.dispose();
	});

	assertThrows("When mask is null and source doesn't heve a mask",
			IllegalArgumentException.class, () -> {
				Cursor cursorFromImageAndMask = new Cursor(Display.getDefault(), source, null, 0, 0);
				cursorFromImageAndMask.dispose();
			});

	assertThrows("When source and the mask are not the same size",
			IllegalArgumentException.class, () -> {
				ImageData source32 = new ImageData(32, 32, 1, new PaletteData(new RGB[] { new RGB(0, 0, 0) }));
				ImageData mask16 = new ImageData(16, 16, 1, new PaletteData(new RGB[] { new RGB(0, 0, 0) }));

				Cursor cursorFromImageAndMask = new Cursor(Display.getDefault(), source32, mask16, 0, 0);
				cursorFromImageAndMask.dispose();
			});

	assertThrows("When hotspot is outside the bounds of the image",
			IllegalArgumentException.class, () -> {
				Cursor cursorFromImageAndMask = new Cursor(Display.getDefault(), source, mask, 18, 18);
				cursorFromImageAndMask.dispose();
			});

	assertThrows("When source image data is null", IllegalArgumentException.class,
			() -> {
				ImageData nullImageData = null;
				Cursor cursorFromSourceOnly = new Cursor(Display.getDefault(), nullImageData, 0, 0);
				cursorFromSourceOnly.dispose();
			});

	assertThrows("When ImageDataProvider is null", IllegalArgumentException.class,
			() -> {
				ImageDataProvider provider = null;
				Cursor cursorFromProvider = new Cursor(Display.getDefault(), provider, 0, 0);
				cursorFromProvider.dispose();
			});

	assertThrows("When source in ImageDataProvider is null",
			IllegalArgumentException.class, () -> {
				ImageData nullSource = null;
				ImageDataProvider provider = zoom -> nullSource;
				Cursor cursorFromProvider = new Cursor(Display.getDefault(), provider, 0, 0);
				cursorFromProvider.dispose();
			});
}

@Test
public void test_equalsLjava_lang_Object() {
	/* Note: Two cursors are only considered equal if their handles are equal.
	 * So since Windows reuses cursor handles, and other platforms do not,
	 * it does not make sense to test whether cursor.equals(sameCursor).
	 */
	Cursor cursor = new Cursor(display, SWT.CURSOR_WAIT);
	Cursor otherCursor = new Cursor(display, SWT.CURSOR_CROSS);
	try {
		// Test Cursor.equals(Object)
		assertTrue("!cursor.equals((Object)null)", !cursor.equals((Object)null));

		// Test Cursor.equals(Cursor)
		assertTrue("!cursor.equals((Cursor)null)", !cursor.equals((Cursor)null));
		assertTrue("cursor.equals(cursor)", cursor.equals(cursor));
		assertTrue("!cursor.equals(otherCursor)", !cursor.equals(otherCursor));
	} finally {
		cursor.dispose();
		otherCursor.dispose();
	}
}

@Test
public void test_isDisposed() {
	// Test Cursor.isDisposed() false
	Cursor cursor = new Cursor(display, SWT.CURSOR_WAIT);
	try {
		assertTrue("Cursor should not be disposed", !cursor.isDisposed());
	} finally {
		// Test Cursor.isDisposed() true
		cursor.dispose();
		assertTrue("Cursor should be disposed", cursor.isDisposed());
	}
}

@Test
public void test_toString() {
	Cursor cursor = new Cursor(display, SWT.CURSOR_WAIT);
	try {
		assertNotNull(cursor.toString());
		assertTrue(cursor.toString().length() > 0);
	} finally {
		cursor.dispose();
	}
}

/* custom */
Display display;
}
