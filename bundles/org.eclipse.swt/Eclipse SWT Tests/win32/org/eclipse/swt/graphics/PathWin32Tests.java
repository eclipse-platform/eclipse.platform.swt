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

import static org.junit.Assert.assertTrue;

import java.util.*;

import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.gdip.*;
import org.junit.jupiter.api.*;

class PathWin32Tests extends Win32AutoscaleTestBase {

	int zoom = 100;
	int scaledZoom = 200;

	@Test
	public void testPathMustBeScaledOnZoomLevelChange() {
		Path path = new Path(display);
		path.addArc(0, 0, 10, 10, 0, 90);
		PathData pathData = path.getPathData();
		DPIUtil.setDeviceZoom(scaledZoom);
		Path scaledPath = new Path(display, pathData);
		PathData scaledPathData = scaledPath.getPathData();
		assertTrue("PathData types don't change on zoom level change", Arrays.equals(pathData.types, scaledPathData.types));
		assertTrue("PathData points don't change on zoom level change", Arrays.equals(pathData.points, scaledPathData.points));
	}

	@Test
	public void testHandlesExistForEachZoomLevelInHashMap() {
		DPIUtil.setDeviceZoom(zoom);
		Path path = new Path(display);
		path.addArc(0, 0, 10, 10, 0, 90);
		path.getHandle(scaledZoom);
		assertTrue("zoomLevelToHandle contains scaled handle", path.toString().contains(scaledZoom + "="));
		assertTrue("zoomLevelToHandle contains initial zoom's handle", path.toString().contains(zoom + "="));
	}

	@Test
	public void testBoundsAreScaledWRTZoomLevel() {
		DPIUtil.setDeviceZoom(zoom);
		int scalingFactor = scaledZoom/zoom;
		Path path = new Path(display);
		path.addArc(0, 0, 10, 10, 0, 90);
		RectF bounds = new RectF();
		RectF scaledBounds = new RectF();
		Gdip.GraphicsPath_GetBounds(path.getHandle(zoom), bounds, 0, 0);
		Gdip.GraphicsPath_GetBounds(path.getHandle(scaledZoom), scaledBounds, 0, 0);
		assertTrue("X coordinate is scaled up wrt the scalingFactor", bounds.X * scalingFactor == scaledBounds.X);
		assertTrue("X coordinate is scaled up wrt the scalingFactor", bounds.Y * scalingFactor == scaledBounds.Y);
		assertTrue("Height is scaled up wrt the scalingFactor", bounds.Height * scalingFactor == scaledBounds.Height);
		assertTrue("Height is scaled up wrt the scalingFactor", bounds.Width * scalingFactor == scaledBounds.Width);
	}
}
