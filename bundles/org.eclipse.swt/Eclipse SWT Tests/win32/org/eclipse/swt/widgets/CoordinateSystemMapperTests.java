package org.eclipse.swt.widgets;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.function.*;

import org.eclipse.swt.graphics.*;
import org.junit.jupiter.api.*;

public class CoordinateSystemMapperTests {

	Supplier<Monitor[]> getMonitorConsumer;
	MultiZoomCoordinateSystemMapper multiZoomCoordinateSystemMapper;

	private Monitor getMonitorLeft200() {
		Monitor monitor = new Monitor();
		monitor.x = 0;
		monitor.y = 0;
		monitor.width = 1000;
		monitor.height = 1000;
		monitor.clientHeight = 1000;
		monitor.clientWidth = 1000;
		monitor.clientX = 0;
		monitor.clientY = 0;
		monitor.zoom = 200;
		monitor.handle = 1;
		return monitor;
	}

	private Monitor getMonitorRight100() {
		Monitor monitor = new Monitor();
		monitor.x = 2000;
		monitor.y = 0;
		monitor.width = 2000;
		monitor.height = 2000;
		monitor.clientHeight = 2000;
		monitor.clientWidth = 2000;
		monitor.clientX = 2000;
		monitor.clientY = 0;
		monitor.zoom = 100;
		monitor.handle = 2;
		return monitor;
	}

	@BeforeEach
	void setup() {
//		Display display = new Display();
		Monitor [] monitors = new Monitor [] {getMonitorLeft200(), getMonitorRight100()};
		getMonitorConsumer = () -> monitors;
		multiZoomCoordinateSystemMapper = new MultiZoomCoordinateSystemMapper(null, getMonitorConsumer);
	}

	@Test
	void translatePointInNoMonitorBackAndForthShouldBeTheSame() {
		Point pt = new Point(5000, -400);
		Point px = multiZoomCoordinateSystemMapper.translateToDisplayCoordinates(pt, 0);
		assertEquals(pt, multiZoomCoordinateSystemMapper.translateFromDisplayCoordinates(px, 0));
	}

	@Test
	void translatePointInGapBackAndForthShouldBeTheSame() {
		Point pt = new Point(1900, 400);
		Point px = multiZoomCoordinateSystemMapper.translateToDisplayCoordinates(pt, 0);
		assertEquals(pt, multiZoomCoordinateSystemMapper.translateFromDisplayCoordinates(px, 0));
	}

	@Test
	void translateRectangleInNoMonitorBackAndForthShouldBeTheSame() {
		Rectangle rectInPts = new Rectangle(5000, -400, 200, 200);
		Rectangle rectInPxs = multiZoomCoordinateSystemMapper.translateToDisplayCoordinates(rectInPts, 0);
		assertEquals(rectInPts, multiZoomCoordinateSystemMapper.translateFromDisplayCoordinates(rectInPxs, 0));
	}

	@Test
	void translateRectangleInGapBackAndForthShouldBeTheSame() {
		Rectangle rectInPts = new Rectangle(1800, 400, 100, 100);
		Rectangle rectInPxs = multiZoomCoordinateSystemMapper.translateToDisplayCoordinates(rectInPts, 0);
		assertEquals(rectInPts, multiZoomCoordinateSystemMapper.translateFromDisplayCoordinates(rectInPxs, 0));
	}

	@Test
	void translateRectangleInGapPartiallyInRightBackAndForthShouldBeTheSame() {
		Rectangle rectInPts = new Rectangle(1950, 400, 100, 100);
		Rectangle rectInPxs = multiZoomCoordinateSystemMapper.translateToDisplayCoordinates(rectInPts, 0);
		assertEquals(rectInPts, multiZoomCoordinateSystemMapper.translateFromDisplayCoordinates(rectInPxs, 0));
	}

	@Test
	void translateRectangleInGapPartiallyInLeftBackAndForthShouldBeTheSame() {
		Rectangle rectInPts = new Rectangle(750, 400, 100, 100);
		Rectangle rectInPxs = multiZoomCoordinateSystemMapper.translateToDisplayCoordinates(rectInPts, 0);
		assertEquals(rectInPts, multiZoomCoordinateSystemMapper.translateFromDisplayCoordinates(rectInPxs, 0));
	}

	@Test
	void translateRectangleInPointsInBothMonitorsPartiallyBackAndForthShouldBeTheSame() {
		Rectangle rectInPts = new Rectangle(950, 400, 1500, 100);
		Rectangle rectInPxs = multiZoomCoordinateSystemMapper.translateToDisplayCoordinates(rectInPts, 0);
		assertEquals(rectInPts, multiZoomCoordinateSystemMapper.translateFromDisplayCoordinates(rectInPxs, 0));
	}
	@Test
	void moveRectangleInPixelsInRightMonitorsPartiallyBackAndForthShouldBeTheSame() {
		Rectangle rectInPxs = new Rectangle(1990, -10, 2000, 2000);
		Rectangle expectedSmallRectInPxs = new Rectangle(0, 0, 0, 0);
		expectedSmallRectInPxs.x = rectInPxs.x + (rectInPxs.width/2) - 200;
		expectedSmallRectInPxs.y = rectInPxs.y + (rectInPxs.height/2) - 200;
		expectedSmallRectInPxs.width = 400;
		expectedSmallRectInPxs.height = 400;
		Rectangle rectInPts = multiZoomCoordinateSystemMapper.translateFromDisplayCoordinates(rectInPxs, 0);
		Rectangle smallRectInPts = new Rectangle(0, 0, 0, 0);
		smallRectInPts.x = rectInPts.x + (rectInPts.width/2) - 200;
		smallRectInPts.y = rectInPts.y + (rectInPts.height/2) - 200;
		smallRectInPts.width = 400;
		smallRectInPts.height = 400;
		Rectangle smallRectInPxs = multiZoomCoordinateSystemMapper.translateToDisplayCoordinates(smallRectInPts, 0);
		assertEquals(expectedSmallRectInPxs, smallRectInPxs);
	}

	@Test
	void translateRectangleInPixelsOutisdeMonitorsBackAndForthShouldBeTheSame() {
		Rectangle rectInPxs = new Rectangle(4400, 400, 1000, 1000);
		Rectangle rectInPts = multiZoomCoordinateSystemMapper.translateFromDisplayCoordinates(rectInPxs, 0);
		assertEquals(rectInPxs, multiZoomCoordinateSystemMapper.translateToDisplayCoordinates(rectInPts, 0));
	}

	@Test
	void translateRectangleInPixelsInGapBackAndForthShouldBeTheSame() {
		Rectangle rectInPxs = new Rectangle(4400, 400, 1000, 1000);
		Rectangle rectInPts = multiZoomCoordinateSystemMapper.translateFromDisplayCoordinates(rectInPxs, 0);
		assertEquals(rectInPxs, multiZoomCoordinateSystemMapper.translateToDisplayCoordinates(rectInPts, 0));
	}

	@Test
	void translateRectangleInPixelsInBothMonitorsBackAndForthShouldBeTheSame() {
		Rectangle rectInPxs = new Rectangle(1500, 400, 502, 500);
		Rectangle rectInPts = multiZoomCoordinateSystemMapper.translateFromDisplayCoordinates(rectInPxs, 0);
		assertEquals(rectInPxs, multiZoomCoordinateSystemMapper.translateToDisplayCoordinates(rectInPts, 0));
	}

}
