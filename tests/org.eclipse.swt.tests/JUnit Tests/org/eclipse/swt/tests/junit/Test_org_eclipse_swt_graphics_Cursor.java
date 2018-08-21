/*******************************************************************************
 * Copyright (c) 2000, 2016 IBM Corporation and others.
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
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.InputStream;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
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
	try {
		cursor = new Cursor(display, 100);
		cursor.dispose();
		fail("No exception thrown for style > SWT.CURSOR_HAND (21)");
	} catch (IllegalArgumentException e) {
	}

	// illegal argument, style < 0
	try {
		cursor = new Cursor(display, -100);
		cursor.dispose();
		fail("No exception thrown for style < 0");
	} catch (IllegalArgumentException e) {
	}
}

@Test
public void test_ConstructorLorg_eclipse_swt_graphics_DeviceLorg_eclipse_swt_graphics_ImageDataLorg_eclipse_swt_graphics_ImageDataII() {
	// Test new Cursor(Device device, ImageData source, ImageData mask, int hotspotX, int hotspotY)
	int numFormats = SwtTestUtil.imageFormats.length;
	String fileName = SwtTestUtil.imageFilenames[0];
	for (int i=0; i<numFormats; i++) {
		String format = SwtTestUtil.imageFormats[i];
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
