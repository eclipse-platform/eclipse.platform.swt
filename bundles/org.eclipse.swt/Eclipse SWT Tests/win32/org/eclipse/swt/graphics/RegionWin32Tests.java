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
package org.eclipse.swt.graphics;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.win32.*;
import org.eclipse.swt.widgets.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;

@ExtendWith(PlatformSpecificExecutionExtension.class)
@ExtendWith(WithMonitorSpecificScalingExtension.class)
class RegionWin32Tests {

	@Test
	public void testRegionMustBeScaledOnHandleOfScaledZoomLevel() {
		Display display = Display.getDefault();

		int zoom = 100;
		int scalingFactor = 2;

		Region region = new Region(display);
		region.add(0, 0, 5, 10);
		region.subtract(0,0,1,1);
		region.translate(0, 5);
		region.intersect(1,1,1,1);

		long handle = Region.win32_getHandle(region, zoom);
		long scaledRegionHandle = Region.win32_getHandle(region, zoom * scalingFactor);

		RECT rect = new RECT();
		OS.GetRgnBox(handle, rect);
		Rectangle bounds = new Rectangle(rect.left, rect.top, rect.right - rect.left, rect.bottom - rect.top);

		rect = new RECT();
		OS.GetRgnBox(scaledRegionHandle, rect);
		Rectangle scaledBounds = new Rectangle(rect.left, rect.top, rect.right - rect.left, rect.bottom - rect.top);

		assertEquals("scaled region's height should be double of unscaled region", bounds.height * scalingFactor, scaledBounds.height);
		assertEquals("scaled region's width should be double of unscaled region", bounds.width * scalingFactor, scaledBounds.width);
		assertEquals("scaled region's x position should be double of unscaled region", bounds.x * scalingFactor, scaledBounds.x);
		assertEquals("scaled region's y position should be double of unscaled region", bounds.y * scalingFactor, scaledBounds.y);
	}

	@Test
	public void testRegionMustIntersectProperlyOn175Zoom() {
		Display display = Display.getDefault();
		Shell shell = new Shell(display);
		DPITestUtil.changeDPIZoom(shell, 150);

		Region region = new Region(display);
		region.add(0, 0, 100, 51);
		boolean intersectWhenOverlapping = region.intersects(0, 50, 100, 50);
		assertTrue(intersectWhenOverlapping);

		region = new Region(display);
		region.add(0, 0, 100, 50);
		boolean dontIntersectWhenNotOverlapping = region.intersects(0, 50, 100, 50);
		assertFalse(dontIntersectWhenNotOverlapping);

		region = new Region(display);
		 // 58 * 150 equals to 87
		region.add(0, 58, 100, 20);
		// (27 + 31) should equal to 87, but will be 88 if 27 and 31 will
		// be rounded independently
		boolean shouldNotIntersect = region.intersects(0, 27, 100, 31);
		assertFalse(shouldNotIntersect);
		region.dispose();
	}

	@Test
	public void testCreateRegionHandleWithDisposedRegionInvolved() {
		Display display = Display.getDefault();

		Region region = new Region(display);
		region.add(0, 0, 100, 100);

		Region region2 = new Region(display);
		region.add(50, 50, 100, 100);

		region.add(region2);

		region2.dispose();
		Region.win32_getHandle(region, 100);
		Region.win32_getHandle(region, 200);
		region.dispose();
	}
}
