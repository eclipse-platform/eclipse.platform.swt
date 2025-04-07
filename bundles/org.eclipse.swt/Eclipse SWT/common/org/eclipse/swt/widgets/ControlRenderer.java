/*******************************************************************************
 * Copyright (c) 2025 Syntevo GmbH and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Thomas Singer (Syntevo) - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.widgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.*;

import java.util.function.Function;

public abstract class ControlRenderer {

	protected abstract void paint(GC gc, int width, int height);

	public abstract Color getDefaultBackground();

	public abstract Color getDefaultForeground();

	private final Control control;

	protected ControlRenderer(Control control) {
		this.control = control;
	}

	public final void paint(GC gc) {
		final Point size = control.getSize();
		paint(gc, size.x, size.y);
	}

	protected final Point getSize() {
		return control.getSize();
	}

	protected final boolean isEnabled() {
		return control.isEnabled();
	}

	protected final boolean hasFocus() {
		return control.hasFocus();
	}

	protected final int getStyle() {
		return control.getStyle();
	}

	protected final boolean hasBorder() {
		final int style = getStyle();
		return (style & SWT.BORDER) != 0;
	}

	protected final <T> T measure(Function<GC, T> function) {
		return Drawing.measure(control, function);
	}

	protected final Point getTextExtent(String text, int flags) {
		return Drawing.getTextExtent(control, text, flags);
	}
}
