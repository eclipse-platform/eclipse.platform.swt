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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.*;

import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.gdip.*;
import org.eclipse.swt.widgets.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;

@ExtendWith(PlatformSpecificExecutionExtension.class)
@ExtendWith(WithMonitorSpecificScalingExtension.class)
class PathWin32Tests {

	int zoom = 100;
	int scaledZoom = 200;

	@Test
	public void testPathMustBeScaledOnZoomLevelChange() {
		Display display = Display.getDefault();
		Path path = new Path(display);
		path.addArc(0, 0, 10, 10, 0, 90);
		PathData pathData = path.getPathData();
		DPIUtil.setDeviceZoom(scaledZoom);
		Path scaledPath = new Path(display, pathData);
		PathData scaledPathData = scaledPath.getPathData();
		assertTrue(Arrays.equals(pathData.types, scaledPathData.types), "PathData types don't change on zoom level change");
		assertTrue(Arrays.equals(pathData.points, scaledPathData.points), "PathData points don't change on zoom level change");
	}

	@Test
	public void testHandlesExistForEachZoomLevelInHashMap() {
		Display display = Display.getDefault();
		DPIUtil.setDeviceZoom(zoom);
		Path path = new Path(display);
		path.addArc(0, 0, 10, 10, 0, 90);
		path.getHandle(zoom);
		assertTrue(path.toString().contains(zoom + "="), "zoomLevelToHandle should contain initial zoom's handle");
		assertFalse(path.toString().contains(scaledZoom + "="), "zoomLevelToHandle should not contains scaled handle");
		path.getHandle(scaledZoom);
		assertTrue(path.toString().contains(scaledZoom + "="), "zoomLevelToHandle should contain scaled handle");
	}

	@Test
	public void testBoundsAreScaledWRTZoomLevel() {
		Display display = Display.getDefault();
		DPIUtil.setDeviceZoom(zoom);
		int scalingFactor = scaledZoom/zoom;
		Path path = new Path(display);
		path.addArc(0, 0, 10, 10, 0, 90);
		RectF bounds = new RectF();
		RectF scaledBounds = new RectF();
		Gdip.GraphicsPath_GetBounds(path.getHandle(zoom), bounds, 0, 0);
		Gdip.GraphicsPath_GetBounds(path.getHandle(scaledZoom), scaledBounds, 0, 0);
		assertTrue(bounds.X * scalingFactor == scaledBounds.X, "X coordinate is scaled up wrt the scalingFactor");
		assertTrue(bounds.Y * scalingFactor == scaledBounds.Y, "X coordinate is scaled up wrt the scalingFactor");
		assertTrue(bounds.Height * scalingFactor == scaledBounds.Height, "Height is scaled up wrt the scalingFactor");
		assertTrue(bounds.Width * scalingFactor == scaledBounds.Width, "Height is scaled up wrt the scalingFactor");
	}

	@Test
	public void testCreatePathHandleWithDisposedPathInvolved() {
		Display display = Display.getDefault();

		Path path = new Path(display);
		path.addArc(0, 0, 10, 10, 0, 90);

		Path path2 = new Path(display);
		path.addArc(0, 0, 30, 30, 0, 270);

		path.addPath(path2);

		path2.dispose();
		path.getHandle(100);
		path.getHandle(200);
		path.dispose();
	}
}
