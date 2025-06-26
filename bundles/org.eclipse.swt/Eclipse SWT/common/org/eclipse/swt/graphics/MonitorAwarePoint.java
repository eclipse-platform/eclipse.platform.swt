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
public final class MonitorAwarePoint extends Point {

	private static final long serialVersionUID = 7516155847004716654L;

	public float residualX, residualY;

	private final Monitor monitor;

	public MonitorAwarePoint(int x, int y) {
		this(x, y, null);
	}

	public MonitorAwarePoint(float x, float y, Monitor monitor) {
		super(Math.round(x), Math.round(y));
		this.residualX = x - this.x;
		this.residualY = y - this.y;
		this.monitor = monitor;
	}


	/**
	 * Constructs a new MonitorAwarePoint
	 *
	 * @param x       the x coordinate of the point
	 * @param y       the y coordinate of the point
	 * @param monitor the monitor with whose context the point is created
	 */
	public MonitorAwarePoint(int x, int y, Monitor monitor) {
		super(x, y);
		this.monitor = monitor;
	}

	/**
	 * {@return the monitor with whose context the instance is created}
	 */
	public Monitor getMonitor() {
		return monitor;
	}

	@Override
	public boolean equals(Object object) {
		return super.equals(object);
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}
	
	public float getX() {
		return x + residualX;
	}

	public float getY() {
		return y + residualY;
	}

	public void setX(float x) {
		this.x = Math.round(x);
		this.residualX = x - this.x;
	}

	public void setY(float y) {
		this.y = Math.round(y);
		this.residualY = y - this.y;
	}
}