/*******************************************************************************
 * Copyright (c) 2025 Yatta Solutions and others.
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
package org.eclipse.swt.widgets;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.*;

class SingleZoomCoordinateSystemMapper implements CoordinateSystemMapper {

	private final Display display;

	SingleZoomCoordinateSystemMapper(Display display) {
		this.display = display;
	}

	private int getZoomLevelForMapping(Control from, Control to) {
		if (from != null && from.isDisposed()) {
			display.error(SWT.ERROR_INVALID_ARGUMENT);
		}
		if (to != null && to.isDisposed()) {
			display.error(SWT.ERROR_INVALID_ARGUMENT);
		}
		if (to != null) {
			return to.getZoom();
		}
		return from.getZoom();
	}

	@Override
	public Point map(Control from, Control to, Point point) {
		int zoom = getZoomLevelForMapping(from, to);
		point = Win32DPIUtils.scaleUp(point, zoom);
		return Win32DPIUtils.scaleDown(display.mapInPixels(from, to, point), zoom);
	}

	@Override
	public Rectangle map(Control from, Control to, Rectangle rectangle) {
		int zoom = getZoomLevelForMapping(from, to);
		rectangle = Win32DPIUtils.scaleUp(rectangle, zoom);
		return Win32DPIUtils.scaleDown(display.mapInPixels(from, to, rectangle), zoom);
	}

	@Override
	public Point map(Control from, Control to, int x, int y) {
		int zoom = getZoomLevelForMapping(from, to);
		x = Win32DPIUtils.scaleUp(x, zoom);
		y = Win32DPIUtils.scaleUp(y, zoom);
		return Win32DPIUtils.scaleDown(display.mapInPixels(from, to, x, y), zoom);
	}

	@Override
	public Rectangle map(Control from, Control to, int x, int y, int width, int height) {
		int zoom = getZoomLevelForMapping(from, to);
		x = Win32DPIUtils.scaleUp(x, zoom);
		y = Win32DPIUtils.scaleUp(y, zoom);
		width = Win32DPIUtils.scaleUp(width, zoom);
		height = Win32DPIUtils.scaleUp(height, zoom);
		return Win32DPIUtils.scaleDown(display.mapInPixels(from, to, x, y, width, height), zoom);
	}

	@Override
	public Rectangle mapMonitorBounds(Rectangle rect, int zoom) {
		return Win32DPIUtils.scaleDown(rect, zoom);
	}

	@Override
	public Point translateFromDisplayCoordinates(Point point, int zoom) {
		return Win32DPIUtils.scaleDown(point, zoom);
	}

	@Override
	public Point translateToDisplayCoordinates(Point point, int zoom) {
		return Win32DPIUtils.scaleUp(point, zoom);
	}

	@Override
	public Rectangle translateFromDisplayCoordinates(Rectangle rect, int zoom) {
		return Win32DPIUtils.scaleDown(rect, zoom);
	}

	@Override
	public Rectangle translateToDisplayCoordinates(Rectangle rect, int zoom) {
		return Win32DPIUtils.scaleUp(rect, zoom);
	}

	@Override
	public Point getCursorLocation() {
		int zoom = DPIUtil.getDeviceZoom();
		Point cursorLocationInPixels = display.getCursorLocationInPixels();
		return Win32DPIUtils.scaleDown(cursorLocationInPixels, zoom);
	}

	@Override
	public void setCursorLocation(int x, int y) {
		int zoom = DPIUtil.getDeviceZoom();
		display.setCursorLocationInPixels(Win32DPIUtils.scaleUp(x, zoom), Win32DPIUtils.scaleUp(y, zoom));
	}

	@Override
	public Rectangle getContainingMonitorBoundsInPixels(Point point) {
		int zoom = DPIUtil.getDeviceZoom();
		point = Win32DPIUtils.scaleUp(point, zoom);
		for (Monitor monitor : display.getMonitors()) {
			Rectangle monitorBounds = Win32DPIUtils.scaleUp(monitor.getBounds(), zoom);
			if (monitorBounds.contains(point)) {
				return monitorBounds;
			}
		}
		return null;
	}
}