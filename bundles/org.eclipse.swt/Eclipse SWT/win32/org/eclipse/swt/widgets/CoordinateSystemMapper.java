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

import org.eclipse.swt.graphics.*;

interface CoordinateSystemMapper {

	MonitorAwareRectangle map(Control from, Control to, Rectangle rectangle);

	MonitorAwareRectangle map(Control from, Control to, int x, int y, int width, int height);

	MonitorAwarePoint map(Control from, Control to, Point point);

	MonitorAwarePoint map(Control from, Control to, int x, int y);

	Rectangle mapMonitorBounds(Rectangle rectangle, int zoom);

	Point translateFromDisplayCoordinates(Point point, int zoom);

	Point translateToDisplayCoordinates(MonitorAwarePoint point, int zoom);

	Rectangle translateFromDisplayCoordinates(MonitorAwareRectangle rect, int zoom);

	Rectangle translateToDisplayCoordinates(MonitorAwareRectangle rect, int zoom);

	void setCursorLocation(int x, int y);

	Point getCursorLocation();
}
