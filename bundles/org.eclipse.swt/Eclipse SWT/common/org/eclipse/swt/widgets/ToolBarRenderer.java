/*******************************************************************************
 * Copyright (c) 2000, 2021 IBM Corporation and others.
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
 * Renderer interface for the {@link ToolBar} widget. All renderers have to
 * implement this to work with the ToolBar.
 */
public abstract class ToolBarRenderer {

	/**
	 * Renders the handle.
	 *
	 * @param gc     GC to render with.
	 * @param bounds Bounds of the rendering. x and y are always 0.
	 */
	public abstract void render(GC gc, Rectangle bounds);

	/**
	 * Computes the size of the rendered ToolBar.
	 *
	 * @return The size as {@link Point}
	 */
	public abstract Point computeSize(Point size);

	/**
	 * Returns the row count of the rendered widget.
	 *
	 * @return The row count.
	 */
	public abstract int rowCount();
}
