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

import org.eclipse.swt.graphics.*;

/**
 * Interface for all scale renderer
 */
public abstract class ScaleRenderer extends ControlRenderer {

	public abstract Point computeDefaultSize();

	/**
	 * Maps the point to a scale value
	 *
	 * @param point The point containing the pixels relative to the scale
	 * @return The scale value that corresponds to the point
	 */
	public abstract int handlePosToValue(Point point);

	/**
	 * Indicates if the given position is within the rendered handle.
	 *
	 * @param position The position to check.
	 * @return True if the position is within the bounds of the handle.
	 */
	public abstract boolean isWithinHandle(Point position);

	/**
	 * Indicates if the given position before the rendered handle.
	 *
	 * @param position The position to check.
	 * @return True if the position is before the bounds of the handle.
	 */
	public abstract boolean isAfterHandle(Point position);

	/**
	 * Indicates if the given position after the rendered handle.
	 *
	 * @param position The position to check.
	 * @return True if the position is after the bounds of the handle.
	 */
	public abstract boolean isBeforeHandle(Point position);

	private final Scale scale;

	protected ScaleRenderer(Scale scale) {
		super(scale);
		this.scale = scale;
	}

	protected int getSelection() {
		return scale.getSelection();
	}

	protected int getMinimum() {
		return scale.getMinimum();
	}

	protected int getMaximum() {
		return scale.getMaximum();
	}

	protected int getPageIncrement() {
		return scale.getPageIncrement();
	}

	protected boolean isHorizontal() {
		return scale.isHorizontal();
	}

	protected Scale.HandleState getHandleState() {
		return scale.getHandleState();
	}

	protected int getOrientation() {
		return scale.getOrientation();
	}
}
