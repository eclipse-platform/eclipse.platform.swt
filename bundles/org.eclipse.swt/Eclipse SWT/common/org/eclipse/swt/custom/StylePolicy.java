/*******************************************************************************
 * Copyright (c) 2000, 2026 IBM Corporation and others.
 * This program and accompanying materials are made available under the terms
 * of the Eclipse Public License 2.0, https://www.eclipse.org/legal/epl-2.0/
 * SPDX-License-Identifier: EPL-2.0
 * Derived from the exact SWT owners recorded in this recipe's source seals.
 *
 * Contributors:
 *     Hitesh Soliwal (Synexia) - shared style policy
 *******************************************************************************/
package org.eclipse.swt.custom;

import java.util.function.IntUnaryOperator;
import org.eclipse.swt.SWT;

/** Stateless style policies shared by the custom widget entrypoints. */
enum StylePolicy implements IntUnaryOperator {
	NONE {
		@Override public int applyAsInt (int style) {
			return SWT.NONE;
		}
	},
	COMBO {
		@Override public int applyAsInt (int style) {
			int mask = SWT.BORDER | SWT.READ_ONLY | SWT.FLAT | SWT.LEFT_TO_RIGHT | SWT.RIGHT_TO_LEFT | SWT.LEAD | SWT.CENTER | SWT.TRAIL | SWT.VIRTUAL;
			return SWT.NO_FOCUS | (style & mask);
		}
	},
	LABEL {
		@Override public int applyAsInt (int style) {
            if ((style & SWT.BORDER) != 0) {
                style |= SWT.SHADOW_IN;
            }
			int mask = SWT.SHADOW_IN | SWT.SHADOW_OUT | SWT.SHADOW_NONE | SWT.LEFT_TO_RIGHT | SWT.RIGHT_TO_LEFT;
			style = style & mask;
			return style |= SWT.NO_FOCUS | SWT.DOUBLE_BUFFERED;
		}
	},
	DIRECTION {
		@Override public int applyAsInt (int style) {
			int mask = SWT.LEFT_TO_RIGHT | SWT.RIGHT_TO_LEFT;
			return style & mask;
		}
	},
	SASH_FORM {
		@Override public int applyAsInt (int style) {
			int mask = SWT.BORDER | SWT.LEFT_TO_RIGHT | SWT.RIGHT_TO_LEFT;
			return style & mask;
		}
	},
	SCROLLED {
		@Override public int applyAsInt (int style) {
			int mask = SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER | SWT.LEFT_TO_RIGHT | SWT.RIGHT_TO_LEFT;
			return style & mask;
		}
	},
	STYLED_TEXT {
		@Override public int applyAsInt (int style) {
			if ((style & SWT.SINGLE) != 0) {
				style &= ~(SWT.H_SCROLL | SWT.V_SCROLL | SWT.WRAP | SWT.MULTI);
			} else {
				style |= SWT.MULTI;
				if ((style & SWT.WRAP) != 0) {
					style &= ~SWT.H_SCROLL;
				}
			}
			style |= SWT.NO_REDRAW_RESIZE | SWT.DOUBLE_BUFFERED | SWT.NO_BACKGROUND;
			/* Clear SWT.CENTER to avoid the conflict with SWT.EMBEDDED */
			return style & ~SWT.CENTER;
		}
	},
	VIEW_FORM {
		@Override public int applyAsInt (int style) {
			int mask = SWT.FLAT | SWT.LEFT_TO_RIGHT | SWT.RIGHT_TO_LEFT;
			return style & mask | SWT.NO_REDRAW_RESIZE;
		}
	},
;
}
