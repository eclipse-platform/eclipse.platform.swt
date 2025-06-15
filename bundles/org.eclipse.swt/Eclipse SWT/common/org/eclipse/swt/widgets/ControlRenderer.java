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

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;

import java.util.function.Function;

public abstract class ControlRenderer {

	protected static final String COLOR_DISABLED = "disabled"; //$NON-NLS-1$
	public static final String COLOR_BACKGROUND = "background"; //$NON-NLS-1$
	protected static final String COLOR_FOREGROUND = "foreground"; //$NON-NLS-1$

	protected abstract void paint(GC gc, int width, int height);

	private final Control control;

	protected ControlRenderer(Control control) {
		this.control = control;
	}

	public final void paint(GC gc) {
		final Point size = control.getSize();
		paint(gc, size.x, size.y);
	}

	protected final Color getColor(String key) {
		return control.getColorProvider().getColor(key);
	}

	protected final <T> T measure(Function<GC, T> function) {
		final GC gc = new GC(control);
		try {
			return function.apply(gc);
		} finally {
			gc.dispose();
		}
	}

	protected final Point getTextExtent(String text, int flags) {
		return measure(gc -> {
			gc.setFont(control.getFont());
			return gc.textExtent(text, flags);
		});
	}

	public Color getDefaultBackground() {
		return getColor(COLOR_BACKGROUND);
	}

	public Color getDefaultForeground() {
		return getColor(COLOR_FOREGROUND);
	}
}
