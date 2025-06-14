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
					translateRectangleInPointsToPixels(x, y, width, height,
							to.getShell().getMonitor()));
			mappedRectangleInPoints = DPIUtil.scaleDown(mappedRectangleInPixels, to.getZoom());
		} else if (to == null) {
			Rectangle mappedRectangleInPixels = display.mapInPixels(from, to,
					DPIUtil.scaleUp(new Rectangle(x, y, width, height), from.getZoom()));
			mappedRectangleInPoints = translateRectangleInPixelsToPoints(mappedRectangleInPixels.x,
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
		return translateLocationInPixelsToPoints(point.x, point.y);
	}

	@Override
	public Point translateToDisplayCoordinates(Point point, int zoom) {
		Monitor monitor = point instanceof Point.WithMonitor pointWithMonitor ? pointWithMonitor.getMonitor() : null;
		return translateLocationInPointsToPixels(point.x, point.y, monitor);
	}

	@Override
	public Rectangle translateFromDisplayCoordinates(Rectangle rect, int zoom) {
		Monitor monitor = rect instanceof Rectangle.WithMonitor rectWithMonitor ? rectWithMonitor.getMonitor() : null;
		return translateRectangleInPixelsToPoints(rect.x, rect.y, rect.width, rect.height, monitor);
	}

	@Override
	public Rectangle translateToDisplayCoordinates(Rectangle rect, int zoom) {
		Monitor monitor = rect instanceof Rectangle.WithMonitor rectWithMonitor ? rectWithMonitor.getMonitor() : null;
		return translateRectangleInPointsToPixels(rect.x, rect.y, rect.width, rect.height, monitor);
	}

	@Override
	public Point getCursorLocation() {
		Point cursorLocationInPixels = display.getCursorLocationInPixels();
		return translateLocationInPixelsToPoints(cursorLocationInPixels.x, cursorLocationInPixels.y);
	}

	@Override
	public void setCursorLocation (int x, int y) {
		Point cursorLocationInPixels = translateLocationInPointsToPixels(x, y, null);
		display.setCursorLocationInPixels (cursorLocationInPixels.x, cursorLocationInPixels.y);
	}

	private Point translateLocationInPointsToPixels(int x, int y, Monitor monitor) {
		monitor = getValidMonitorIfApplicable(x, y, monitor);
		return getPixelsFromPoint(monitor, x, y);
	}

	private Point translateLocationInPixelsToPoints(int x, int y) {
		Monitor monitor = getContainingMonitorForPixels(x, y);
		return getPointFromPixels(monitor, x, y);
	}

	private Rectangle translateRectangleInPointsToPixels(int x, int y, int width, int height, Monitor monitor) {
		monitor = getValidMonitorIfApplicable(x, y, width, height, monitor);
		Point topLeft = getPixelsFromPoint(monitor, x, y);
		int zoom = getApplicableMonitorZoom(monitor);
		int widthInPixels = DPIUtil.scaleUp(width, zoom);
		int heightInPixels = DPIUtil.scaleUp(height, zoom);
		return new Rectangle(topLeft.x, topLeft.y, widthInPixels, heightInPixels);
	}

	private Rectangle translateRectangleInPixelsToPoints(int x, int y, int widthInPixels, int heightInPixels, Monitor monitor) {
		if (monitor == null)
			monitor = getContainingMonitorForPixels(x, y, widthInPixels, heightInPixels);
		int zoom = getApplicableMonitorZoom(monitor);
		Point topLeft = getPointFromPixels(monitor, x, y);
		int width = DPIUtil.scaleDown(widthInPixels, zoom);
		int height = DPIUtil.scaleDown(heightInPixels, zoom);
		Rectangle.WithMonitor rect = new Rectangle.WithMonitor(topLeft.x, topLeft.y, width, height, monitor);
		return rect;
	}

	private Monitor getValidMonitorIfApplicable(int x, int y, int width, int height, Monitor monitor) {
		if(monitor != null) {
			if (monitor.getClientArea().intersects(x, y, width, height)) {
				return monitor;
			}
			Monitor containingMonitor = getContainingMonitorForPoints(x, y, width, height);
			return containingMonitor != null ? containingMonitor : monitor;
		}
		Monitor containingMonitor = getContainingMonitorForPoints(x, y, width, height);
		return containingMonitor != null ? containingMonitor : monitorSupplier.get()[0];
	}

	private Monitor getValidMonitorIfApplicable(int x, int y, Monitor monitor) {
		if (monitor != null) {
			if (monitor.getClientArea().contains(x, y)) {
				return monitor;
			}
			Monitor containingMonitor = getContainingMonitorForPoints(x, y);
			return containingMonitor != null ? containingMonitor : monitor;
		}
		Monitor containingMonitor = getContainingMonitorForPoints(x, y);
		return containingMonitor != null ? containingMonitor : monitorSupplier.get()[0];
	}

	private Monitor getContainingMonitorForPoints(int x, int y) {
		Monitor[] monitors = monitorSupplier.get();
		for (Monitor currentMonitor : monitors) {
			Rectangle clientArea = currentMonitor.getClientArea();
			if (clientArea.contains(x, y)) {
				return currentMonitor;
			}
		}
		return null;
	}

	private Monitor getContainingMonitorForPoints(int x, int y, int width, int height) {
		if (width <= 0 || height <= 0) {
			return getContainingMonitorForPoints(x, y);
		}
		Monitor[] monitors = monitorSupplier.get();
		Monitor selectedMonitor = null;
		int highestIntersectionRatio = 0;
		for (Monitor currentMonitor : monitors) {
			// Obtain the rectangle in pixels per monitor for absolute comparison
			Point topLeftOfRectangle = getPixelsFromPoint(currentMonitor, x, y);
			int widthInPixels = DPIUtil.scaleUp(width, getApplicableMonitorZoom(currentMonitor));
			int heightInPixels = DPIUtil.scaleUp(height, getApplicableMonitorZoom(currentMonitor));
			Rectangle boundsInPixel = new Rectangle(topLeftOfRectangle.x, topLeftOfRectangle.y, widthInPixels, heightInPixels);
			Rectangle clientArea = getMonitorClientAreaInPixels(currentMonitor);
			Rectangle intersection = clientArea.intersection(boundsInPixel);
			int intersectionArea = intersection.width * intersection.height;
			int boundsArea = boundsInPixel.width * boundsInPixel.height;
			int intersectionRatio = (intersectionArea * 100) / boundsArea;
			if (intersectionRatio > highestIntersectionRatio) {
				selectedMonitor = currentMonitor;
				highestIntersectionRatio = intersectionRatio;
			}
		}
		return selectedMonitor;
	}

	private Monitor getContainingMonitorForPixels(int xInPixels, int yInPixels) {
		Monitor[] monitors = monitorSupplier.get();
		for (Monitor current : monitors) {
			Rectangle clientArea = getMonitorClientAreaInPixels(current);
			if (clientArea.contains(xInPixels, yInPixels)) {
				return current;
			}
		}
		return monitors[0];
	}

	private Monitor getContainingMonitorForPixels(int xInPixels, int yInPixels, int widthInPixels,
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
		return new Point.WithMonitor(mappedX, mappedY, monitor);
	}

	private int getApplicableMonitorZoom(Monitor monitor) {
		return DPIUtil.getZoomForAutoscaleProperty(monitor.zoom);
	}

}