/*******************************************************************************
 * Copyright (c) 2026 Synexia, Hitesh Soliwal (hsoliwal@gmail.com) and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Apache License, Version 2.0, or (at your option) the Eclipse
 * Public License 2.0, which are available at
 * https://www.apache.org/licenses/LICENSE-2.0 and
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: Apache-2.0 OR EPL-2.0
 *
 * Contributors:
 *     Hitesh Soliwal (Synexia) - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.gpu;

/**
 * Bit packing for retained paint, stroke and font state. Every cache key is a
 * {@code long}: the upper 32 bits are a state mask, the lower 32 bits are an
 * ARGB colour or a font id. No objects, no enums, no booleans.
 *
 * <pre>
 * mask bit  0-1   kind        0 fill, 1 stroke, 2 text, 3 pen (GDI)
 *           2-4   line style  SWT.LINE_SOLID(1) .. SWT.LINE_CUSTOM(6)
 *           5-6   line cap    SWT.CAP_FLAT(1), CAP_ROUND(2), CAP_SQUARE(3)
 *           7-8   line join   SWT.JOIN_MITER(1), JOIN_ROUND(2), JOIN_BEVEL(3)
 *           9-10  antialias   0 default, 1 off, 2 on
 *           11    xor mode
 *           12-27 line width  unsigned fixed point 12.4 (0 .. 4095.9375 px)
 *           28-31 backend     0 GDI, 1 GDI+, 2 Skija
 * </pre>
 */
public final class PaintKeys {
	public static final int KIND_FILL = 0, KIND_STROKE = 1, KIND_TEXT = 2, KIND_PEN = 3;
	public static final int BACKEND_GDI = 0, BACKEND_GDIP = 1, BACKEND_SKIJA = 2;

	static final int STYLE_SHIFT = 2, CAP_SHIFT = 5, JOIN_SHIFT = 7, AA_SHIFT = 9, XOR_BIT = 1 << 11;
	static final int WIDTH_SHIFT = 12, BACKEND_SHIFT = 28;

	private PaintKeys () {
	}

	/** Packs stroke/fill state. {@code antialias} uses SWT values (-1 default, 0 off, 1 on). */
	public static int mask (int kind, int lineStyle, int lineCap, int lineJoin, int antialias, int xor, float lineWidth, int backend) {
		int width = (int) (Math.max (0f, Math.min (lineWidth, 4095.9375f)) * 16f + 0.5f);
		return (kind & 3)
			| (lineStyle & 7) << STYLE_SHIFT
			| (lineCap & 3) << CAP_SHIFT
			| (lineJoin & 3) << JOIN_SHIFT
			| ((antialias + 1) & 3) << AA_SHIFT
			| (xor != 0 ? XOR_BIT : 0)
			| (width & 0xFFFF) << WIDTH_SHIFT
			| (backend & 0xF) << BACKEND_SHIFT;
	}

	public static long key (int mask, int argbOrId) {
		return ((long) mask << 32) | (argbOrId & 0xFFFFFFFFL);
	}

	public static int maskOf (long key) {
		return (int) (key >>> 32);
	}

	public static int lowOf (long key) {
		return (int) key;
	}

	public static int kind (int mask) {
		return mask & 3;
	}

	public static int lineStyle (int mask) {
		return (mask >>> STYLE_SHIFT) & 7;
	}

	public static int lineCap (int mask) {
		return (mask >>> CAP_SHIFT) & 3;
	}

	public static int lineJoin (int mask) {
		return (mask >>> JOIN_SHIFT) & 3;
	}

	/** SWT antialias value: -1 default, 0 off, 1 on. */
	public static int antialias (int mask) {
		return ((mask >>> AA_SHIFT) & 3) - 1;
	}

	public static int xor (int mask) {
		return (mask & XOR_BIT) != 0 ? 1 : 0;
	}

	public static float lineWidth (int mask) {
		return ((mask >>> WIDTH_SHIFT) & 0xFFFF) / 16f;
	}

	/** Win32 COLORREF (0x00BBGGRR) plus alpha to ARGB. */
	public static int argbFromColorref (int colorref, int alpha) {
		return (alpha & 0xFF) << 24 | (colorref & 0xFF) << 16 | (colorref & 0xFF00) | (colorref >>> 16) & 0xFF;
	}

	/** ARGB to Win32 COLORREF (alpha dropped). */
	public static int colorrefFromArgb (int argb) {
		return (argb >>> 16) & 0xFF | (argb & 0xFF00) | (argb & 0xFF) << 16;
	}

	/** Font key: face hash in the low word, size in 1/16 px and style bits in the mask. */
	public static long fontKey (int faceHash, float sizePx, int styleBits) {
		int size = (int) (Math.max (0f, Math.min (sizePx, 4095.9375f)) * 16f + 0.5f);
		return key ((size & 0xFFFF) | (styleBits & 0xFFFF) << 16, faceHash);
	}
}
