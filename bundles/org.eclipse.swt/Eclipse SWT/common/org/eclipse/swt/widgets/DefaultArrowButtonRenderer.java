/*******************************************************************************
 * Copyright (c) 2024-2025 SAP, Syntevo GmbH and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Denis Ungemach (SAP)
 *     Thomas Singer (Syntevo)
 *******************************************************************************/
package org.eclipse.swt.widgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.*;

public class DefaultArrowButtonRenderer extends ButtonRenderer {

	private static final Color BACKGROUND_COLOR = new Color(240, 240, 240);
	private static final Color FOREGROUND_COLOR = new Color(0, 0, 0);
	private static final Color DISABLED_COLOR = new Color(160, 160, 160);

	public DefaultArrowButtonRenderer(Button button) {
		super(button);
	}

	public Point computeDefaultSize() {
		int borderWidth = hasBorder() ? 8 : 0;
		int width = 14 + borderWidth;
		int height = 14 + borderWidth;
		return new Point(width, height);
	}

	@Override
	protected void paint(GC gc, int width, int height) {
		if (hasFocus()) {
			gc.drawFocus(3, 3, width - 7, height - 7);
		}

		gc.setBackground(isEnabled() ? FOREGROUND_COLOR : DISABLED_COLOR);

		int centerHeight = height / 2;
		int centerWidth = width / 2;
		if (hasBorder()) {
			// border ruins center position...
			centerHeight -= 2;
			centerWidth -= 2;
		}

		// TODO: in the next version use a bezier path

		int[] curve = null;

		final int style = getStyle();
		if ((style & SWT.DOWN) != 0) {
			curve = new int[]{centerWidth, centerHeight + 5,
					centerWidth - 5, centerHeight - 5, centerWidth + 5,
					centerHeight - 5};

		} else if ((style & SWT.LEFT) != 0) {
			curve = new int[]{centerWidth - 5, centerHeight,
					centerWidth + 5, centerHeight + 5, centerWidth + 5,
					centerHeight - 5};

		} else if ((style & SWT.RIGHT) != 0) {
			curve = new int[]{centerWidth + 5, centerHeight,
					centerWidth - 5, centerHeight - 5, centerWidth - 5,
					centerHeight + 5};

		} else {
			curve = new int[]{centerWidth, centerHeight - 5,
					centerWidth - 5, centerHeight + 5, centerWidth + 5,
					centerHeight + 5};
		}

		gc.fillPolygon(curve);
	}

	@Override
	public Color getDefaultBackground() {
		return BACKGROUND_COLOR;
	}

	@Override
	public Color getDefaultForeground() {
		return FOREGROUND_COLOR;
	}
}
