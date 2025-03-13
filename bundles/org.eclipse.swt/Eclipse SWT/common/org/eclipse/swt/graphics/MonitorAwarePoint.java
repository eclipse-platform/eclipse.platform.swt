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
package org.eclipse.swt.graphics;

import org.eclipse.swt.widgets.*;

/**
 * Instances of this class represent {@link org.eclipse.swt.graphics.Point}
 * objects along with the context of the monitor in relation to which they are
 * placed on the display. The monitor awareness makes it easy to scale and
 * translate the points between pixels and points.
 *
 * @since 3.129
 * @noreference This class is not intended to be referenced by clients
 */
public final class MonitorAwarePoint {
	private final Point point;
	private final Monitor monitor;

	/**
	 * Constructs a new MonitorAwarePoint
	 *
	 * @param x       the x coordinate of the point
	 * @param y       the y coordinate of the point
	 * @param monitor the monitor with whose context the point is created
	 */
	public MonitorAwarePoint(int x, int y, Monitor monitor) {
		this(new Point(x, y), monitor);
	}

	public MonitorAwarePoint(Point point) {
		this(point, null);
	}

	public MonitorAwarePoint(Point point, Monitor monitor) {
		this.point = point;
		this.monitor = monitor;
	}

	/**
	 * {@return the monitor with whose context the instance is created}
	 */
	public Monitor getMonitor() {
		return monitor;
	}

	/**
	 * {@return the monitor with whose context the instance is created}
	 */
	public Point getPoint() {
		return point;
	}
}
