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

	private static final long serialVersionUID = 5041911840525116925L;

	private final Monitor monitor;

	/**
	 * Constructs a new MonitorAwareRectangle
	 *
	 * @param x the x coordinate of the top left corner of the rectangle
	 * @param y the y coordinate of the top left corner of the rectangle
	 * @param width the width of the rectangle
	 * @param height the height of the rectangle
	 * @param monitor the monitor with whose context the rectangle is created
	 */
	public MonitorAwareRectangle(int x, int y, int width, int height, Monitor monitor) {
		super(x, y, width, height);
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
		return new MonitorAwareRectangle(x, y, width, height, monitor);
	}

}
