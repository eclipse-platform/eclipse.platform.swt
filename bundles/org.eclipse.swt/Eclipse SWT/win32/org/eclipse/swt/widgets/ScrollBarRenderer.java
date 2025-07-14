/*******************************************************************************
 * Copyright (c) 2025 Advantest Europe GmbH and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 * 				Raghunandana Murthappa
 *******************************************************************************/
package org.eclipse.swt.widgets;

import org.eclipse.swt.graphics.*;

public abstract class ScrollBarRenderer extends ControlRenderer {

	protected final ScrollBar scrollbar;

	protected ScrollBarRenderer(ScrollBar scrollbar) {
		super(scrollbar.getParent());
		this.scrollbar = scrollbar;
	}

	public static int minMax(int min, int value, int max) {
		return Math.min(Math.max(min, value), max);
	}

	public abstract Point computeDefaultSize();

	public abstract void setDrawTrack(boolean drawTrack);

	public abstract void setDragging(boolean isDragging);

	public abstract void setThumbHovered(boolean isHovered);

	public abstract boolean getDragging();

	public abstract boolean getHovered();

	public abstract Rectangle getThumbRectangle();

	public abstract Rectangle getTrackRectangle();
}
