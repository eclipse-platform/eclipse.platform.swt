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

import java.util.function.*;

import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.*;

class MultiZoomCoordinateSystemMapper implements CoordinateSystemMapper {

	private final Display display;

	Supplier<Monitor[]> monitorSupplier;

	private MultiZoomCoordinateSystemMapper(Display display) {
		this.display = display;
	}

	MultiZoomCoordinateSystemMapper(Display display, Supplier<Monitor[]> monitorSupplier) {
		this(display);
		this.monitorSupplier = monitorSupplier;
	}

	@Override
	public Point map(Control from, Control to, Point point) {
		return map(from, to, point.x, point.y);
	}

	@Override
	public Rectangle map(Control from, Control to, Rectangle rectangle) {
		return map(from, to, rectangle.x, rectangle.y, rectangle.width, rectangle.height);
	}

	@Override
	public Point map(Control from, Control to, int x, int y) {
		Point mappedPointInPoints;
		if (from == null) {
			Point mappedPointInpixels = display.mapInPixels(from, to,
					getPixelsFromPoint(to.getShell().getMonitor(), x, y));
			mappedPointInPoints = DPIUtil.scaleDown(mappedPointInpixels, to.getZoom());
		} else if (to == null) {
			Point mappedPointInpixels = display.mapInPixels(from, to, DPIUtil.scaleUp(new Point(x, y), from.getZoom()));
			mappedPointInPoints = getPointFromPixels(from.getShell().getMonitor(), mappedPointInpixels.x,
					mappedPointInpixels.y);
		} else {
			Point mappedPointInpixels = display.mapInPixels(from, to, DPIUtil.scaleUp(new Point(x, y), from.getZoom()));
			mappedPointInPoints = DPIUtil.scaleDown(mappedPointInpixels, to.getZoom());
		}
		return mappedPointInPoints;
	}

	@Override
	public Rectangle map(Control from, Control to, int x, int y, int width, int height) {
		Rectangle mappedRectangleInPoints;
		if (from == null) {
			Rectangle mappedRectangleInPixels = display.mapInPixels(from, to,
					translateRectangleInPixelsInDisplayCoordinateSystem(x, y, width, height,
							to.getShell().getMonitor()));
			mappedRectangleInPoints = DPIUtil.scaleDown(mappedRectangleInPixels, to.getZoom());
		} else if (to == null) {
			Rectangle mappedRectangleInPixels = display.mapInPixels(from, to,
					DPIUtil.scaleUp(new Rectangle(x, y, width, height), from.getZoom()));
			mappedRectangleInPoints = translateRectangleInPointsInDisplayCoordinateSystem(mappedRectangleInPixels.x,
					mappedRectangleInPixels.y, mappedRectangleInPixels.width, mappedRectangleInPixels.height,
					from.getShell().getMonitor());
		} else {
			Rectangle mappedRectangleInPixels = display.mapInPixels(from, to,
					DPIUtil.scaleUp(new Rectangle(x, y, width, height), from.getZoom()));
			mappedRectangleInPoints = DPIUtil.scaleDown(mappedRectangleInPixels, to.getZoom());
		}
		return mappedRectangleInPoints;
	}

	@Override
	public Rectangle mapMonitorBounds(Rectangle rect, int zoom) {
		Rectangle bounds = DPIUtil.scaleDown(rect, zoom);
		bounds.x = rect.x;
		bounds.y = rect.y;
		return bounds;
	}

	@Override
	public Point translateFromDisplayCoordinates(Point point, int zoom) {
		return translateLocationInPixelsFromDisplayCoordinateSystem(point.x, point.y);
	}

	@Override
	public Point translateToDisplayCoordinates(Point point, int zoom) {
		return translateLocationInPointsToDisplayCoordinateSystem(point.x, point.y);
	}

	@Override
	public Rectangle translateFromDisplayCoordinates(Rectangle rect, int zoom) {
		return translateRectangleInPixelsFromDisplayCoordinateSystemByContainment(rect.x, rect.y, rect.width,
				rect.height);
	}

	@Override
	public Rectangle translateToDisplayCoordinates(Rectangle rect, int zoom) {
		return translateRectangleInPointsToDisplayCoordinateSystemByContainment(rect.x, rect.y, rect.width,
				rect.height);
	}

	@Override
	public Point getCursorLocation() {
		Point cursorLocationInPixels = display.getCursorLocationInPixels();
		return translateLocationInPixelsFromDisplayCoordinateSystem(cursorLocationInPixels.x, cursorLocationInPixels.y);
	}

	@Override
	public void setCursorLocation(int x, int y) {
		Point cursorLocationInPixels = translateLocationInPointsToDisplayCoordinateSystem(x, y);
		display.setCursorLocationInPixels(cursorLocationInPixels.x, cursorLocationInPixels.y);
	}

	private Point translateLocationInPointsToDisplayCoordinateSystem(int x, int y) {
		Monitor monitor = getContainingMonitor(x, y);
		return getPixelsFromPoint(monitor, x, y);
	}

	private Point translateLocationInPixelsFromDisplayCoordinateSystem(int x, int y) {
		Monitor monitor = getContainingMonitorInPixelsCoordinate(x, y);
		return getPointFromPixels(monitor, x, y);
	}

	private Rectangle translateRectangleInPointsToDisplayCoordinateSystemByContainment(int x, int y, int width,
			int height) {
		Monitor monitorByLocation = getContainingMonitor(x, y);
		Monitor monitorByContainment = getContainingMonitor(x, y, width, height);
		return translateRectangleInPixelsInDisplayCoordinateSystem(x, y, width, height, monitorByLocation,
				monitorByContainment);
	}

	private Rectangle translateRectangleInPixelsInDisplayCoordinateSystem(int x, int y, int width, int height,
			Monitor monitor) {
		return translateRectangleInPixelsInDisplayCoordinateSystem(x, y, width, height, monitor, monitor);
	}

	private Rectangle translateRectangleInPixelsInDisplayCoordinateSystem(int x, int y, int width, int height,
			Monitor monitorOfLocation, Monitor monitorOfArea) {
		Point topLeft = getPixelsFromPoint(monitorOfLocation, x, y);
		int zoom = getApplicableMonitorZoom(monitorOfArea);
		int widthInPixels = DPIUtil.scaleUp(width, zoom);
		int heightInPixels = DPIUtil.scaleUp(height, zoom);
		return new Rectangle(topLeft.x, topLeft.y, widthInPixels, heightInPixels);
	}

	private Rectangle translateRectangleInPixelsFromDisplayCoordinateSystemByContainment(int x, int y,
			int widthInPixels, int heightInPixels) {
		Monitor monitorByLocation = getContainingMonitor(x, y);
		Monitor monitorByContainment = getContainingMonitorInPixelsCoordinate(x, y, widthInPixels, heightInPixels);
		return translateRectangleInPointsInDisplayCoordinateSystem(x, y, widthInPixels, heightInPixels,
				monitorByLocation, monitorByContainment);
	}

	private Rectangle translateRectangleInPointsInDisplayCoordinateSystem(int x, int y, int widthInPixels,
			int heightInPixels, Monitor monitor) {
		return translateRectangleInPointsInDisplayCoordinateSystem(x, y, widthInPixels, heightInPixels, monitor,
				monitor);
	}

	private Rectangle translateRectangleInPointsInDisplayCoordinateSystem(int x, int y, int widthInPixels,
			int heightInPixels, Monitor monitorOfLocation, Monitor monitorOfArea) {
		Point topLeft = getPointFromPixels(monitorOfLocation, x, y);
		int zoom = getApplicableMonitorZoom(monitorOfArea);
		int width = DPIUtil.scaleDown(widthInPixels, zoom);
		int height = DPIUtil.scaleDown(heightInPixels, zoom);
		return new Rectangle(topLeft.x, topLeft.y, width, height);
	}

	private Monitor getContainingMonitor(int x, int y) {
		Monitor[] monitors = monitorSupplier.get();
		for (Monitor currentMonitor : monitors) {
			Rectangle clientArea = currentMonitor.getClientArea();
			if (clientArea.contains(x, y)) {
				return currentMonitor;
			}
		}
		return monitors[0];
	}

	private Monitor getContainingMonitor(int x, int y, int width, int height) {
		Rectangle rectangle = new Rectangle(x, y, width, height);
		Monitor[] monitors = monitorSupplier.get();
		Monitor selectedMonitor = monitors[0];
		int highestArea = 0;
		for (Monitor currentMonitor : monitors) {
			Rectangle clientArea = currentMonitor.getClientArea();
			Rectangle intersection = clientArea.intersection(rectangle);
			int area = intersection.width * intersection.height;
			if (area > highestArea) {
				selectedMonitor = currentMonitor;
				highestArea = area;
			}
		}
		return selectedMonitor;
	}

	private Monitor getContainingMonitorInPixelsCoordinate(int xInPixels, int yInPixels) {
		Monitor[] monitors = monitorSupplier.get();
		for (Monitor current : monitors) {
			Rectangle clientArea = getMonitorClientAreaInPixels(current);
			if (clientArea.contains(xInPixels, yInPixels)) {
				return current;
			}
		}
		return monitors[0];
	}

	private Monitor getContainingMonitorInPixelsCoordinate(int xInPixels, int yInPixels, int widthInPixels,
			int heightInPixels) {
		Rectangle rectangle = new Rectangle(xInPixels, yInPixels, widthInPixels, heightInPixels);
		Monitor[] monitors = monitorSupplier.get();
		Monitor selectedMonitor = monitors[0];
		int highestArea = 0;
		for (Monitor currentMonitor : monitors) {
			Rectangle clientArea = getMonitorClientAreaInPixels(currentMonitor);
			Rectangle intersection = clientArea.intersection(rectangle);
			int area = intersection.width * intersection.height;
			if (area > highestArea) {
				selectedMonitor = currentMonitor;
				highestArea = area;
			}
		}
		return selectedMonitor;
	}

	private Rectangle getMonitorClientAreaInPixels(Monitor monitor) {
		int zoom = getApplicableMonitorZoom(monitor);
		int widthInPixels = DPIUtil.scaleUp(monitor.clientWidth, zoom);
		int heightInPixels = DPIUtil.scaleUp(monitor.clientHeight, zoom);
		return new Rectangle(monitor.clientX, monitor.clientY, widthInPixels, heightInPixels);
	}

	private Point getPixelsFromPoint(Monitor monitor, int x, int y) {
		int zoom = getApplicableMonitorZoom(monitor);
		int mappedX = DPIUtil.scaleUp(x - monitor.clientX, zoom) + monitor.clientX;
		int mappedY = DPIUtil.scaleUp(y - monitor.clientY, zoom) + monitor.clientY;
		return new Point(mappedX, mappedY);
	}

	private Point getPointFromPixels(Monitor monitor, int x, int y) {
		int zoom = getApplicableMonitorZoom(monitor);
		int mappedX = DPIUtil.scaleDown(x - monitor.clientX, zoom) + monitor.clientX;
		int mappedY = DPIUtil.scaleDown(y - monitor.clientY, zoom) + monitor.clientY;
		return new Point(mappedX, mappedY);
	}

	private int getApplicableMonitorZoom(Monitor monitor) {
		return DPIUtil.getZoomForAutoscaleProperty(monitor.zoom);
	}

}