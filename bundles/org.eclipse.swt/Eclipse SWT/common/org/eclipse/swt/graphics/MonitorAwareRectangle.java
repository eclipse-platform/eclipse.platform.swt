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
 * Instances of this class represent {@link org.eclipse.swt.graphics.Rectangle}
 * objects along with the context of the monitor in relation to which they are
 * placed on the display. The monitor awareness makes it easy to scale and
 * translate the rectangles between pixels and points.
 *
 * @since 3.129
 * @noreference This class is not intended to be referenced by clients
 */
public final class MonitorAwareRectangle extends Rectangle {

	private static final long serialVersionUID = -54807918875027527L;

	private float residualX, residualY, residualWidth, residualHeight;

	private final Monitor monitor;

	public MonitorAwareRectangle(int x, int y, int width, int height) {
		this(x, y, width, height, null);
	}

	public MonitorAwareRectangle(float x, float y, float width, float height) {
		this(x, y, width, height, null);
	}

	public MonitorAwareRectangle(int x, int y, int width, int height, Monitor monitor) {
		super(x, y, width, height);
		this.monitor = monitor;
	}

	MonitorAwareRectangle(float x, float y, float width, float height, Monitor monitor) {
		super(Math.round(x), Math.round(y), Math.round(width), Math.round(height));
		this.residualX = x - this.x;
		this.residualY = y - this.y;
		this.residualWidth = width - this.width;
		this.residualHeight = height - this.height;
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
	
	@Override
	public MonitorAwareRectangle clone() {
		return new MonitorAwareRectangle(getX(), getY(), getWidth(), getHeight(), monitor);
	}

	public float getX() {
		return x + residualX;
	}

	public float getY() {
		return y + residualY;
	}

	public float getWidth() {
		return width + residualWidth;
	}

	public float getHeight() {
		return height + residualHeight;
	}

	public void setX(float x) {
		this.x = Math.round(x);
		this.residualX = x - this.x;
	}

	public void setY(float y) {
		this.y = Math.round(y);
		this.residualY = y - this.y;
	}

	public void setWidth(float width) {
		this.width = Math.round(width);
		this.residualWidth = width - this.width;
	}

	public void setHeight(float height) {
		this.height = Math.round(height);
		this.residualHeight = height - this.height;
	}

}