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

import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.win32.*;
import org.junit.jupiter.api.*;

class RegionWin32Tests extends Win32AutoscaleTestBase {

	@Test
	public void testRegionMustBeScaledOnHandleOfScaledZoomLevel() {
		int zoom = DPIUtil.getDeviceZoom();
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

}
