package org.eclipse.swt.widgets;

import org.eclipse.swt.graphics.*;

abstract class CoordinateSystemMapper {

	Display display;

	CoordinateSystemMapper(Display display) {
		super();
		this.display = display;
	}

	abstract Rectangle map(Control from, Control to, Rectangle rectangle);

	abstract Rectangle map(Control from, Control to, int x, int y, int width, int height);

	abstract Point map(Control from, Control to, Point point);

	abstract Point map(Control from, Control to, int x, int y);

	abstract Rectangle mapMonitorBounds(Rectangle rectangle, int zoom);

	abstract Point translateFromDisplayCoordinates(Point point, int zoom);

	abstract Point translateToDisplayCoordinates(Point point, int zoom);

	abstract Rectangle translateFromDisplayCoordinates(Rectangle rect, int zoom);

	abstract Rectangle translateToDisplayCoordinates(Rectangle rect, int zoom);

	abstract void setCursorLocation(int x, int y);

	abstract Point getCursorLocation();
}
