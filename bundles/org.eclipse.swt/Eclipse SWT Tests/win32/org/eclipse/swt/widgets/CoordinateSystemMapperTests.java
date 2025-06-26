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
 *     Yatta Solutions - initial tests
 *******************************************************************************/
package org.eclipse.swt.widgets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.function.*;
import java.util.stream.*;

import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CoordinateSystemMapperTests {

	Supplier<Monitor[]> getMonitorConsumer;
	Monitor[] monitors;

	private Monitor createMonitor(CoordinateSystemMapper mapper, Rectangle boundsInPixels, int nativeZoom) {
		Monitor monitor = new Monitor();
		Rectangle bounds = mapper.mapMonitorBounds(boundsInPixels, DPIUtil.getZoomForAutoscaleProperty(nativeZoom));
		monitor.setBounds(bounds);
		monitor.setClientArea(bounds);
		monitor.zoom = nativeZoom;
		return monitor;
	}

	private void setupMonitors(CoordinateSystemMapper mapper) {
		Rectangle boundsInPixelsForLeftMonitor = new Rectangle(0, 0, 2000, 2000);
		Rectangle boundsInPixelsForRightMonitor = new Rectangle(2000, 0, 2000, 2000);
		monitors = new Monitor[] { createMonitor(mapper, boundsInPixelsForLeftMonitor, 200),
				createMonitor(mapper, boundsInPixelsForRightMonitor, 100) };
	}

	private Stream<CoordinateSystemMapper> provideCoordinateSystemMappers() {
		return Stream.of(getMultiZoomCoordinateSystemMapper(), getSingleZoomCoordinateSystemMapper());
	}

	private MultiZoomCoordinateSystemMapper getMultiZoomCoordinateSystemMapper() {
		return new MultiZoomCoordinateSystemMapper(null, () -> monitors);
	}

	private SingleZoomCoordinateSystemMapper getSingleZoomCoordinateSystemMapper() {
		return new SingleZoomCoordinateSystemMapper(null);
	}

	@ParameterizedTest
	@MethodSource("provideCoordinateSystemMappers")
	void translatePointInNoMonitorBackAndForthShouldBeTheSame(CoordinateSystemMapper mapper) {
		setupMonitors(mapper);
		Point pt = createExpectedPoint(mapper, 5000, -400, monitors[0]);
		Point px = mapper.translateToDisplayCoordinates(pt, monitors[0].getZoom());
		assertEquals(pt, mapper.translateFromDisplayCoordinates(px, monitors[0].getZoom()));
	}

	@Test
	void translatePointInGapBackAndForthInSingleZoomShouldBeTheSame() {
		SingleZoomCoordinateSystemMapper mapper = getSingleZoomCoordinateSystemMapper();
		setupMonitors(mapper);
		Point pt = new Point(1900, 400);
		Point px = mapper.translateToDisplayCoordinates(pt, monitors[0].getZoom());
		assertEquals(pt, mapper.translateFromDisplayCoordinates(px, monitors[0].getZoom()));
	}

	@Test
	void translatePointInGapBackAndForthInMultiZoomShouldEndInsideTheSameMonitor() {
		MultiZoomCoordinateSystemMapper mapper = getMultiZoomCoordinateSystemMapper();
		setupMonitors(mapper);
		Point pt = new Point(1900, 400);
		Point px = mapper.translateToDisplayCoordinates(pt, monitors[0].getZoom());
		Point translatedPt = mapper.translateFromDisplayCoordinates(px, monitors[0].getZoom());
		Point translatedPx = mapper.translateToDisplayCoordinates(translatedPt, monitors[0].getZoom());
		assertEquals(new Point(translatedPt.x, translatedPt.y), translatedPx);
		assertEquals(translatedPx, px);
	}

	@ParameterizedTest
	@MethodSource("provideCoordinateSystemMappers")
	void translateRectangleInNoMonitorBackAndForthShouldBeTheSame(CoordinateSystemMapper mapper) {
		setupMonitors(mapper);
		Rectangle rectInPts = createExpectedRectangle(mapper, 5000, -400, 200, 200, monitors[0]);
		Rectangle rectInPxs = mapper.translateToDisplayCoordinates(rectInPts, monitors[0].getZoom());
		assertEquals(rectInPts, mapper.translateFromDisplayCoordinates(rectInPxs, monitors[0].getZoom()));
	}

	@Test
	void translateRectangleInGapBackAndForthInSingleZoomShouldBeTheSame() {
		SingleZoomCoordinateSystemMapper mapper = getSingleZoomCoordinateSystemMapper();
		setupMonitors(mapper);
		Rectangle rectInPts = new Rectangle(1800, 400, 100, 100);
		Rectangle rectInPxs = mapper.translateToDisplayCoordinates(rectInPts, monitors[0].getZoom());
		assertEquals(rectInPts, mapper.translateFromDisplayCoordinates(rectInPxs, monitors[0].getZoom()));
	}

	@Test
	void translateRectangleInGapBackAndForthInMultiZoomShouldBeInMonitorBounds() {
		MultiZoomCoordinateSystemMapper mapper = getMultiZoomCoordinateSystemMapper();
		setupMonitors(mapper);
		Rectangle rectInPts = new Rectangle(1800, 400, 100, 100);
		Rectangle rectInPxs = mapper.translateToDisplayCoordinates(rectInPts, monitors[0].getZoom());
		Rectangle rectInPtsTranslated = mapper.translateFromDisplayCoordinates(rectInPxs, monitors[0].getZoom());
		boolean isInsideMonitor = false;
		for (Monitor monitor : monitors) {
			if (monitor.getClientArea().intersects(rectInPtsTranslated)) {
				isInsideMonitor = true;
				break;
			}
		}
		assertTrue(isInsideMonitor, "The translated rectangle in points is inside the monitor bounds in points");
	}

	@Test
	void translateRectangleInGapPartiallyInRightBackAndForthInSingleZoomShouldBeTheSame() {
		SingleZoomCoordinateSystemMapper mapper = getSingleZoomCoordinateSystemMapper();
		setupMonitors(mapper);
		Rectangle rectInPts = new Rectangle(1950, 400, 150, 100);
		Rectangle rectInPxs = mapper.translateToDisplayCoordinates(rectInPts, monitors[0].getZoom());
		assertEquals(rectInPts, mapper.translateFromDisplayCoordinates(rectInPxs, monitors[0].getZoom()));
	}

	@Test
	void translateRectangleInGapPartiallyInRightBackAndForthInMultiZoomShouldBeInside() {
		MultiZoomCoordinateSystemMapper mapper = getMultiZoomCoordinateSystemMapper();
		setupMonitors(mapper);
		Rectangle rectInPts = new Rectangle.WithMonitor(1950, 400, 150, 100, monitors[1]);
		Rectangle rectInPxs = mapper.translateToDisplayCoordinates(rectInPts, monitors[0].getZoom());
		assertEquals(rectInPts, mapper.translateFromDisplayCoordinates(rectInPxs, monitors[0].getZoom()));
	}

	@ParameterizedTest
	@MethodSource("provideCoordinateSystemMappers")
	void translateRectangleInGapPartiallyInLeftBackAndForthShouldBeTheSame(CoordinateSystemMapper mapper) {
		setupMonitors(mapper);
		Rectangle rectInPts = createExpectedRectangle(mapper, 750, 400, 100, 100, monitors[0]);
		Rectangle rectInPxs = mapper.translateToDisplayCoordinates(rectInPts, monitors[0].getZoom());
		assertEquals(rectInPts, mapper.translateFromDisplayCoordinates(rectInPxs, monitors[0].getZoom()));
	}

	@Test
	void translateRectangleInPointsInBothMonitorsPartiallyBackAndForthInSingleZoomShouldBeTheSame() {
		SingleZoomCoordinateSystemMapper mapper = getSingleZoomCoordinateSystemMapper();
		setupMonitors(mapper);
		Rectangle rectInPts = new Rectangle(950, 400, 1500, 100);
		Rectangle rectInPxs = mapper.translateToDisplayCoordinates(rectInPts, monitors[0].getZoom());
		assertEquals(rectInPts, mapper.translateFromDisplayCoordinates(rectInPxs, monitors[0].getZoom()));
	}

	@Test
	void translateRectangleInPointsInBothMonitorsPartiallyBackAndForthInMultiZoomShouldNotEndUpInGap() {
		MultiZoomCoordinateSystemMapper mapper = getMultiZoomCoordinateSystemMapper();
		setupMonitors(mapper);
		Rectangle rectInPts = new Rectangle(950, 400, 1500, 100);
		Rectangle rectInPxs = mapper.translateToDisplayCoordinates(rectInPts, monitors[0].getZoom());
		Rectangle rectInPtsTranslated = mapper.translateFromDisplayCoordinates(rectInPxs, monitors[0].getZoom());
		Rectangle rectInPxsTranslated = mapper.translateToDisplayCoordinates(rectInPtsTranslated,
				monitors[0].getZoom());
		assertEquals(rectInPxs, rectInPxsTranslated);
	}

	@Test
	void moveRectangleInPixelsInRightMonitorsPartiallyBackAndForthShouldBeTheSame() {
		MultiZoomCoordinateSystemMapper mapper = getMultiZoomCoordinateSystemMapper();
		setupMonitors(mapper);
		Rectangle rectInPxs = new Rectangle(1990, -10, 2000, 2000);
		Rectangle expectedSmallRectInPxs = new Rectangle(0, 0, 0, monitors[0].getZoom());
		expectedSmallRectInPxs.x = rectInPxs.x + (rectInPxs.width / 2) - 200;
		expectedSmallRectInPxs.y = rectInPxs.y + (rectInPxs.height / 2) - 200;
		expectedSmallRectInPxs.width = 400;
		expectedSmallRectInPxs.height = 400;
		Rectangle rectInPts = mapper.translateFromDisplayCoordinates(rectInPxs, monitors[0].getZoom());
		Rectangle smallRectInPts = new Rectangle(0, 0, 0, monitors[0].getZoom());
		smallRectInPts.x = rectInPts.x + (rectInPts.width / 2) - 200;
		smallRectInPts.y = rectInPts.y + (rectInPts.height / 2) - 200;
		smallRectInPts.width = 400;
		smallRectInPts.height = 400;
		Rectangle smallRectInPxs = mapper.translateToDisplayCoordinates(smallRectInPts, monitors[0].getZoom());
		assertEquals(expectedSmallRectInPxs, smallRectInPxs);
	}

	@ParameterizedTest
	@MethodSource("provideCoordinateSystemMappers")
	void translateRectangleInPixelsOutisdeMonitorsBackAndForthShouldBeTheSame(CoordinateSystemMapper mapper) {
		setupMonitors(mapper);
		Rectangle rectInPxs = new Rectangle(400, 2400, 1000, 1000);
		Rectangle rectInPts = mapper.translateFromDisplayCoordinates(rectInPxs, monitors[0].getZoom());
		assertEquals(rectInPxs, mapper.translateToDisplayCoordinates(rectInPts, monitors[0].getZoom()));
	}

	@ParameterizedTest
	@MethodSource("provideCoordinateSystemMappers")
	void translateRectangleInPixelsInBothMonitorsBackAndForthShouldBeTheSame(CoordinateSystemMapper mapper) {
		setupMonitors(mapper);
		Rectangle rectInPxs = new Rectangle(1500, 400, 502, 500);
		Rectangle rectInPts = mapper.translateFromDisplayCoordinates(rectInPxs, monitors[0].getZoom());
		assertEquals(rectInPxs, mapper.translateToDisplayCoordinates(rectInPts, monitors[0].getZoom()));
	}

	@ParameterizedTest
	@MethodSource("provideCoordinateSystemMappers")
	void translateRectangleInPixelsForZeroSize(CoordinateSystemMapper mapper) {
		setupMonitors(mapper);
		Rectangle rectInPts = createExpectedRectangle(mapper, 0, 0, 0, 0, monitors[0]);
		Rectangle rectInPxs = mapper.translateToDisplayCoordinates(rectInPts, monitors[0].getZoom());
		assertEquals(rectInPts, mapper.translateFromDisplayCoordinates(rectInPxs, monitors[0].getZoom()));
	}

	private Point createExpectedPoint(CoordinateSystemMapper mapper, int x, int y, Monitor monitor) {
		if (mapper instanceof SingleZoomCoordinateSystemMapper) {
			return new Point(x, y);
		} else {
			return new Point.WithMonitor(x, y, monitor);
		}
	}

	private Rectangle createExpectedRectangle(CoordinateSystemMapper mapper, int x, int y, int width, int height, Monitor monitor) {
		if (mapper instanceof SingleZoomCoordinateSystemMapper) {
			return new Rectangle(x, y, width, height);
		} else {
			return new Rectangle.WithMonitor(x, y, width, height, monitor);
		}
	}

}
