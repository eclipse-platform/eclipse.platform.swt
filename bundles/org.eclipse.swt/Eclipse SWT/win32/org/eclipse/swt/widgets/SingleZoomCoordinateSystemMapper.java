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
		point = DPIUtil.scaleUp(point, zoom);
		return DPIUtil.scaleDown(display.mapInPixels(from, to, point), zoom);
	}

	@Override
	public Rectangle map(Control from, Control to, Rectangle rectangle) {
		int zoom = getZoomLevelForMapping(from, to);
		rectangle = DPIUtil.scaleUp(rectangle, zoom);
		return DPIUtil.scaleDown(display.mapInPixels(from, to, rectangle), zoom);
	}

	@Override
	public Point map(Control from, Control to, int x, int y) {
		int zoom = getZoomLevelForMapping(from, to);
		x = DPIUtil.scaleUp(x, zoom);
		y = DPIUtil.scaleUp(y, zoom);
		return DPIUtil.scaleDown(display.mapInPixels(from, to, x, y), zoom);
	}

	@Override
	public Rectangle map(Control from, Control to, int x, int y, int width, int height) {
		int zoom = getZoomLevelForMapping(from, to);
		x = DPIUtil.scaleUp(x, zoom);
		y = DPIUtil.scaleUp(y, zoom);
		width = DPIUtil.scaleUp(width, zoom);
		height = DPIUtil.scaleUp(height, zoom);
		return DPIUtil.scaleDown(display.mapInPixels(from, to, x, y, width, height), zoom);
	}

	@Override
	public Rectangle mapMonitorBounds(Rectangle rect, int zoom) {
		return DPIUtil.scaleDown(rect, DPIUtil.getDeviceZoom());
	}

	@Override
	public Point translateFromDisplayCoordinates(Point point, int zoom) {
		return DPIUtil.scaleDown(point, zoom);
	}

	@Override
	public Point translateToDisplayCoordinates(Point point, int zoom) {
		return DPIUtil.scaleUp(point, zoom);
	}

	@Override
	public Rectangle translateFromDisplayCoordinates(Rectangle rect, int zoom) {
		return DPIUtil.scaleDown(rect, zoom);
	}

	@Override
	public Rectangle translateToDisplayCoordinates(Rectangle rect, int zoom) {
		return DPIUtil.scaleUp(rect, zoom);
	}

	@Override
	public Point getCursorLocation() {
		Point cursorLocationInPixels = display.getCursorLocationInPixels();
		return DPIUtil.scaleDown(cursorLocationInPixels, DPIUtil.getDeviceZoom());
	}

	@Override
	public void setCursorLocation(int x, int y) {
		int zoom = DPIUtil.getDeviceZoom();
		display.setCursorLocationInPixels(DPIUtil.scaleUp(x, zoom), DPIUtil.scaleUp(y, zoom));
	}

	@Override
	public Rectangle getContainingMonitorBoundsInPixels(Point point) {
		int zoom = DPIUtil.getDeviceZoom();
		point = DPIUtil.scaleUp(point, zoom);
		for (Monitor monitor : display.getMonitors()) {
			Rectangle monitorBounds = DPIUtil.scaleUp(monitor.getBounds(), zoom);
			if (monitorBounds.contains(point)) {
				return monitorBounds;
			}
		}
		return null;
	}
}