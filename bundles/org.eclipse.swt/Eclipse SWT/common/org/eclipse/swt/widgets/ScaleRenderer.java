/*******************************************************************************
 * Copyright (c) 2000, 2025 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.widgets;

import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;

/**
 * Interface for all scale renderer
 */
public interface ScaleRenderer {
	/**
	 * Maps the point to a scale value
	 *
	 * @param point The point containing the pixels relative to the scale
	 * @return The scale value that corresponds to the point
	 */
	int handlePosToValue(Point point);

	/**
	 * Indicates if the given position is within the rendered handle.
	 *
	 * @param position The position to check.
	 * @return True if the position is within the bounds of the handle.
	 */
	boolean isWithinHandle(Point position);

	/**
	 * Indicates if the given position before the rendered handle.
	 *
	 * @param position The position to check.
	 * @return True if the position is before the bounds of the handle.
	 */
	boolean isAfterHandle(Point position);

	/**
	 * Indicates if the given position after the rendered handle.
	 *
	 * @param position The position to check.
	 * @return True if the position is after the bounds of the handle.
	 */
	boolean isBeforeHandle(Point position);

	/**
	 * Renders the handle.
	 *
	 * @param gc
	 * @param size
	 */
	void render(GC gc, Point size);
}
