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
import org.junit.jupiter.api.*;

class TextLayoutWin32Tests extends Win32AutoscaleTestBase {
	final static String text = "This is a text for testing.";

	@Test
	public void testGetBoundPublicAPIshouldReturnTheSameValueRegardlessOfZoomLevel() {
		final TextLayout layout = new TextLayout(display);
		GCData unscaledData = new GCData();
		unscaledData.nativeZoom = DPIUtil.getNativeDeviceZoom();
		GC gc = GC.win32_new(display, unscaledData);
		layout.draw(gc, 10, 10);
		Rectangle unscaledBounds = layout.getBounds();

		int scalingFactor = 2;
		int newZoom = DPIUtil.getNativeDeviceZoom() * scalingFactor;
		GCData scaledData = new GCData();
		scaledData.nativeZoom = newZoom;
		GC scaledGc = GC.win32_new(display, scaledData);
		layout.draw(scaledGc, 10, 10);
		Rectangle scaledBounds = layout.getBounds();

		assertEquals("The public API for getBounds should give the same result for any zoom level", scaledBounds, unscaledBounds);
	}

}
