/*******************************************************************************
 * Copyright (c) 2024, 2026 Initiative 31 contributors (swt-initiative31/prototype-skija) and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Initiative 31 contributors - TreeItemRenderer.drawArrow, DefaultCheckboxRenderer.drawCheckbox
 *         (swt-initiative31/prototype-skija 146eee2a23faec511de96f2539f7d650ef24300e, blobs ef92445b6730, 4cca2934e769)
 *     Hitesh Soliwal (Synexia) - adapted to the RowCanvas primitives of the drawn planes
 *******************************************************************************/
package org.eclipse.swt.internal.render;

/**
 * The Initiative 31 look of expanders and check boxes, adapted from the custom-drawn widgets of
 * swt-initiative31/prototype-skija (EPL-2.0): a grey filled triangle pointing right (collapsed) or down
 * (expanded), ten pixels wide, centred in the expander slot; a 12 pixel rounded box with a grey outline whose
 * inside is filled with the selection blue when checked (grey when grayed).
 */
final class Initiative31Look {
	/** Half the triangle's side (TreeItemRenderer.drawArrow uses +-5 around the centre). */
	private static final int ARROW = 5;
	/** DefaultCheckboxRenderer.BOX_SIZE and its inner border. */
	private static final int BOX = 12, INNER = 2;

	private Initiative31Look () {
	}

	static void expander (RowCanvas canvas, Theme theme, RowPaint row, boolean open) {
		int cx = row.expanderX + row.expanderWidth / 2, cy = row.y + row.height / 2;
		int a = Math.min (ARROW, Math.max (2, Math.min (row.expanderWidth, row.height) / 2 - 2));
		if (open) {
			set (row.points, cx, cy + a, cx - a, cy - a, cx + a, cy - a);
		} else {
			set (row.points, cx + a, cy, cx - a, cy - a, cx - a, cy + a);
		}
		canvas.polygon (row.points, 3, theme.hash);
	}

	static void checkBox (RowCanvas canvas, Theme theme, RowPaint row, boolean checked, boolean grayed) {
		int size = Math.min (BOX, Math.min (row.checkWidth, row.height) - 2);
		int left = row.checkX + (row.checkWidth - size) / 2, top = row.y + (row.height - size) / 2;
		rounded (canvas, left, top, size, size, theme.border);
		rounded (canvas, left + 1, top + 1, size - 2, size - 2, 0xFFFFFF);
		if (checked) rounded (canvas, left + INNER, top + INNER, size - 2 * INNER, size - 2 * INNER, grayed ? theme.grayFill : theme.checkFill);
	}

	/* a filled rectangle with its four corner pixels left out */
	private static void rounded (RowCanvas canvas, int x, int y, int width, int height, int rgb) {
		if (width < 3 || height < 3) {
			canvas.fill (x, y, width, height, rgb);
			return;
		}
		canvas.fill (x + 1, y, width - 2, height, rgb);
		canvas.fill (x, y + 1, 1, height - 2, rgb);
		canvas.fill (x + width - 1, y + 1, 1, height - 2, rgb);
	}

	private static void set (int [] points, int x0, int y0, int x1, int y1, int x2, int y2) {
		points [0] = x0;
		points [1] = y0;
		points [2] = x1;
		points [3] = y1;
		points [4] = x2;
		points [5] = y2;
	}
}
