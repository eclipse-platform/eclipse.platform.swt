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
package org.eclipse.swt.graphics;

import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

import org.eclipse.swt.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.widgets.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;

@ExtendWith(PlatformSpecificExecutionExtension.class)
@ExtendWith(WithMonitorSpecificScalingExtension.class)
class CursorWin32Tests {

	private Display display;

	@BeforeEach
	void setUp() {
		display = Display.getDefault();
	}

	@Test
	void testDisposedCursorReturnsZeroHandle() {
		Cursor cursor = new Cursor(display, SWT.CURSOR_ARROW);
		cursor.dispose();
		assertEquals(0L, Cursor.win32_getHandle(cursor, 100), "A disposed cursor must return a zero handle");
	}

	@Test
	void testHandleIsCachedForSameZoomLevel() {
		ImageData source = new ImageData(16, 16, 32, new PaletteData(0xFF00, 0xFF0000, 0xFF000000));
		source.alpha = 255;

		Cursor cursor = new Cursor(display, source, 0, 0);
		try {
			long first = Cursor.win32_getHandle(cursor, 100);
			long second = Cursor.win32_getHandle(cursor, 100);
			assertEquals(first, second, "Repeated calls with the same zoom must return the cached handle");
		} finally {
			cursor.dispose();
		}
	}

	@Test
	void testImageDataCursorProducesDifferentHandlesForDifferentZoomLevels() {
		// 32bpp image with uniform alpha — takes the ARGB path in
		// setupCursorFromImageData
		ImageData source = new ImageData(16, 16, 32, new PaletteData(0xFF00, 0xFF0000, 0xFF000000));
		source.alpha = 255;

		Cursor cursor = new Cursor(display, source, 0, 0);
		try {
			long handle100 = Cursor.win32_getHandle(cursor, 100);
			long handle200 = Cursor.win32_getHandle(cursor, 200);

			assertNotEquals(0L, handle100, "Handle at 100% zoom must be non-zero");
			assertNotEquals(0L, handle200, "Handle at 200% zoom must be non-zero");
			assertNotEquals(handle100, handle200,
					"Different zoom levels must produce distinct OS cursor handles (different physical sizes)");
		} finally {
			cursor.dispose();
		}
	}

	@Test
	void testDestroyHandlesExceptPreservesRetainedHandle() {
		// 32bpp ARGB source so we get a unique, non-shared OS handle per zoom level
		ImageData source = new ImageData(16, 16, 32, new PaletteData(0xFF00, 0xFF0000, 0xFF000000));
		source.alpha = 255;

		Cursor cursor = new Cursor(display, source, 0, 0);
		try {
			long handle100 = Cursor.win32_getHandle(cursor, 100);
			Cursor.win32_getHandle(cursor, 200); // populate a second zoom level

			cursor.destroyHandlesExcept(Set.of(DPIUtil.getZoomForAutoscaleProperty(100)));

			// The cursor itself must still be alive and the retained handle unchanged
			assertFalse(cursor.isDisposed(), "Cursor must not be disposed after destroyHandlesExcept");
			assertEquals(handle100, Cursor.win32_getHandle(cursor, 100),
					"The handle for the retained zoom level must be unchanged after destroyHandlesExcept");
		} finally {
			cursor.dispose();
		}
	}
}
