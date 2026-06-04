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
package org.eclipse.swt.internal;

import static org.junit.jupiter.api.Assertions.*;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.win32.*;
import org.eclipse.swt.widgets.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;

@ExtendWith(PlatformSpecificExecutionExtension.class)
public class ImageListTests {

	@Test
	public void testGetHandleReturnsDifferentHandlesForDifferentZooms() {
		ImageList list = new ImageList(SWT.NONE, 16, 16, 100);
		try {
			long handle100 = list.getHandle(100);
			long handle200 = list.getHandle(200);
			assertNotEquals(0, handle100);
			assertNotEquals(0, handle200);
			assertNotEquals(handle100, handle200, "handles for different zooms must differ");
		} finally {
			list.dispose();
		}
	}

	@Test
	public void testGetHandleReturnsCorrectPixelDimensionsPerZoom() {
		int widthPt = 16;
		int heightPt = 16;
		ImageList list = new ImageList(SWT.NONE, widthPt, heightPt, 100);
		try {
			int[] cx = new int[1], cy = new int[1];

			OS.ImageList_GetIconSize(list.getHandle(100), cx, cy);
			assertEquals(DPIUtil.pointToPixel(widthPt, 100), cx[0], "width at 100%");
			assertEquals(DPIUtil.pointToPixel(heightPt, 100), cy[0], "height at 100%");

			OS.ImageList_GetIconSize(list.getHandle(200), cx, cy);
			assertEquals(DPIUtil.pointToPixel(widthPt, 200), cx[0], "width at 200%");
			assertEquals(DPIUtil.pointToPixel(heightPt, 200), cy[0], "height at 200%");

			OS.ImageList_GetIconSize(list.getHandle(150), cx, cy);
			assertEquals(DPIUtil.pointToPixel(widthPt, 150), cx[0], "width at 150%");
			assertEquals(DPIUtil.pointToPixel(heightPt, 150), cy[0], "height at 150%");
		} finally {
			list.dispose();
		}
	}

	@Test
	public void testGetHandleReturnsSameHandleOnRepeatedCall() {
		ImageList list = new ImageList(SWT.NONE, 16, 16, 100);
		try {
			long first = list.getHandle(200);
			long second = list.getHandle(200);
			assertEquals(first, second, "repeated getHandle for same zoom must return same handle");
		} finally {
			list.dispose();
		}
	}

	@Test
	public void testAddFirstImageAdaptsListSize() {
		ImageList list = new ImageList(SWT.NONE, 24, 24, 100);
		Display display = Display.getDefault();
		Image image = new Image(display, 16, 16);
		try {
			list.add(image);
			Point size = list.getImageSize();
			assertEquals(new Point(16, 16), size);
		} finally {
			list.dispose();
			image.dispose();
			display.dispose();
		}
	}

	@Test
	public void testFirstImageSizeAppliedToAllHandles() {
		ImageList list = new ImageList(SWT.NONE, 24, 24, 100);
		Display display = Display.getDefault();
		Image image = new Image(display, 16, 16);
		try {
			long handle = list.getHandle(150);
			list.add(image);
			int[] cx = new int[1];
			int[] cy = new int[1];
			OS.ImageList_GetIconSize(handle, cx, cy);
			assertEquals(new Point(24, 24), new Point(cx[0], cy[0]));
			OS.ImageList_GetIconSize(list.getHandle(200), cx, cy);
			assertEquals(new Point(32, 32), new Point(cx[0], cy[0]));

		} finally {
			list.dispose();
			image.dispose();
			display.dispose();
		}
	}

	@Test
	public void testGetImageSizeReturnsStoredPoints() {
		ImageList list = new ImageList(SWT.NONE, 24, 24, 100);
		try {
			Point size = list.getImageSize();
			assertEquals(new Point(24, 24), size);
		} finally {
			list.dispose();
		}
	}

	@Test
	public void testIsFittingForDistinguishesSize() {
		ImageList list = new ImageList(SWT.NONE, 16, 16, 100);
		try {
			assertTrue(list.isFittingFor(SWT.NONE, 16, 16));
			assertFalse(list.isFittingFor(SWT.NONE, 32, 32));
		} finally {
			list.dispose();
		}
	}

}
