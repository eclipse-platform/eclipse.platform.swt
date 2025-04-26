/*******************************************************************************
 * Copyright (c) 2025 Vector Informatik GmbH and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package org.eclipse.swt.widgets;

import org.eclipse.swt.graphics.*;

abstract class NativeBasedCustomScrollable extends Scrollable {

	protected abstract ControlRenderer getRenderer();

	private Color backgroundColor;
	private Color foregroundColor;

	public NativeBasedCustomScrollable(Composite parent, int style) {
		super(parent, style);
		// this is for custom drawn widgets necessary to update the scrolling with thumb
		// and with the arrows in the scrollbar widget. Else the position won't be
		// updated.
		state = state | CANVAS;
	}

	@Override
	public final Color getBackground() {
		return backgroundColor != null ? backgroundColor : getRenderer().getDefaultBackground();
	}

	@Override
	public final void setBackground(Color color) {
		backgroundColor = color;
		super.setBackground(color);
	}

	@Override
	public final Color getForeground() {
		return foregroundColor != null ? foregroundColor : getRenderer().getDefaultForeground();
	}

	@Override
	public final void setForeground(Color color) {
		foregroundColor = color;
		super.setForeground(color);
	}
}
