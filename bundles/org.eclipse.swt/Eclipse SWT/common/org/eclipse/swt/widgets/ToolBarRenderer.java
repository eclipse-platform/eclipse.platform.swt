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
public abstract class ToolBarRenderer extends ControlRenderer {

	/**
	 * Computes the size of the rendered ToolBar.
	 *
	 * @return The size as {@link Point}
	 */
	public abstract Point computeSize(int widthHint, int heightHint);

	/**
	 * Returns the row count of the rendered widget.
	 *
	 * @return The row count.
	 */
	public abstract int rowCount();

	protected final ToolBar toolBar;

	protected ToolBarRenderer(ToolBar toolBar) {
		super(toolBar);
		this.toolBar = toolBar;
	}
}
