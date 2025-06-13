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

	public DefaultArrowButtonRenderer(Button button) {
		super(button);
	}

	public Point computeDefaultSize() {
		final int style = button.getStyle();
		int borderWidth = (style & SWT.BORDER) != 0 ? 8 : 0;
		int width = 14 + borderWidth;
		int height = 14 + borderWidth;
		return new Point(width, height);
	}

	@Override
	protected void paint(GC gc, int width, int height) {
		final int style = button.getStyle();
		if (button.hasFocus()) {
			gc.drawFocus(3, 3, width - 7, height - 7);
		}

		gc.setBackground(getColor(button.isEnabled() ? COLOR_FOREGROUND : COLOR_DISABLED));

		int centerHeight = height / 2;
		int centerWidth = width / 2;
		if ((style & SWT.BORDER) != 0) {
			// border ruins center position...
			centerHeight -= 2;
			centerWidth -= 2;
		}

		// TODO: in the next version use a bezier path

		int[] curve;
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
}
