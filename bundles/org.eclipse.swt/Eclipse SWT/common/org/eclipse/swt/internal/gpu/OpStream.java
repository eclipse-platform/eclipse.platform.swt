/*******************************************************************************
 * Copyright (c) 2026 Contributors to the Eclipse Foundation.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Hitesh Soliwal - initial API and implementation; contract adapted from
 *         Synexia SwtSkijaViewportCommandReplay (Apache-2.0, hsoliwal/com.synexia@af4494b1432)
 *         and the Dear ImGui (MIT) ImDrawList packing pattern,
 *         see docs/licenses/swt-gpu-retained-planes-NOTICE.txt
 *******************************************************************************/
package org.eclipse.swt.internal.gpu;

/**
 * Reusable packed draw-command stream recorded by a GC and replayed by a
 * backend. One instance per widget; {@link #reset()} keeps the arrays.
 * <p>
 * Each command is a header int {@code op | (length << 8)} followed by
 * {@code length} int arguments. Colours are ARGB ints, stroke state is a
 * {@link PaintKeys} mask, fonts are int ids and strings are indices into a
 * reference array that is cleared (not reallocated) on reset. A command the
 * backend cannot replay sets a {@link #fail} bit instead of being recorded;
 * the frame driver then rasterizes that plane with GDI.
 * </p>
 */
public final class OpStream {
	public static final int CLIP = 1, CLIP_RESET = 2, FILL_RECT = 3, DRAW_RECT = 4, DRAW_LINE = 5, DRAW_POINT = 6;
	public static final int FILL_OVAL = 7, DRAW_OVAL = 8, FILL_GRADIENT = 9, DRAW_TEXT = 10, FILL_POLY = 11, DRAW_POLY = 12;
	public static final int PLANE = 13;
	/** Shaped glyph run: fontId, x, baselineY, argb, count, count glyph ids, count advances (platform shaping). */
	public static final int GLYPH_RUN = 14;
	/** Nested clip: CLIP_PUSH intersects (x, y, w, h) with the current clip; CLIP_POP restores it. */
	public static final int CLIP_PUSH = 15, CLIP_POP = 16;
	/** Flag on DRAW_POLY: the polyline is open (not closed back to its first point). */
	public static final int OPEN = 0x40;

	/** Fail bits: why a stream cannot be replayed on the GPU. */
	public static final int FAIL_UNSUPPORTED = 1, FAIL_OVERFLOW = 2, FAIL_TRANSFORM = 4, FAIL_TEXT_LAYOUT = 8, FAIL_IMAGE = 16;

	private final int maxInts;
	int [] ops;
	int size;
	Object [] refs;
	int refCount;
	/** Bit set of FAIL_* reasons; non-zero means the stream must not be replayed. */
	public int fail;

	public OpStream (int initialInts, int maxInts) {
		this.maxInts = maxInts;
		ops = new int [Math.max (16, initialInts)];
		refs = new Object [16];
	}

	public void reset () {
		size = 0;
		java.util.Arrays.fill (refs, 0, refCount, null);
		refCount = 0;
		fail = 0;
	}

	public int size () { return size; }
	public int [] ops () { return ops; }
	public Object ref (int i) { return refs [i]; }

	/** Starts a command of {@code length} args; returns the write offset or -1 on overflow. */
	public int begin (int op, int length) {
		int need = size + 1 + length;
		if (need > ops.length && !grow (need)) {
			fail |= FAIL_OVERFLOW;
			return -1;
		}
		ops [size] = op | (length << 8);
		int at = size + 1;
		size = need;
		GpuCounters.inc (GpuCounters.OPS_RECORDED);
		return at;
	}

	private boolean grow (int need) {
		if (need > maxInts) return false;
		int capacity = ops.length;
		while (capacity < need) capacity = Math.min (maxInts, capacity * 2);
		ops = java.util.Arrays.copyOf (ops, capacity);
		return true;
	}

	public int addRef (Object value) {
		if (refCount == refs.length) refs = java.util.Arrays.copyOf (refs, refCount * 2);
		refs [refCount] = value;
		return refCount++;
	}

	public void rect (int op, int x, int y, int w, int h, int argb, int mask) {
		int o = begin (op, 6);
		if (o < 0) return;
		ops [o] = x; ops [o + 1] = y; ops [o + 2] = w; ops [o + 3] = h; ops [o + 4] = argb; ops [o + 5] = mask;
	}

	public void line (int x1, int y1, int x2, int y2, int argb, int mask) {
		rect (DRAW_LINE, x1, y1, x2, y2, argb, mask);
	}

	public void clip (int x, int y, int w, int h) {
		int o = begin (CLIP, 4);
		if (o < 0) return;
		ops [o] = x; ops [o + 1] = y; ops [o + 2] = w; ops [o + 3] = h;
	}

	public void clipPush (int x, int y, int w, int h) {
		int o = begin (CLIP_PUSH, 4);
		if (o < 0) return;
		ops [o] = x; ops [o + 1] = y; ops [o + 2] = w; ops [o + 3] = h;
	}

	public void clipPop () {
		begin (CLIP_POP, 0);
	}

	public void clipReset () {
		begin (CLIP_RESET, 0);
	}

	public void gradient (int x, int y, int w, int h, int fromArgb, int toArgb, int vertical) {
		int o = begin (FILL_GRADIENT, 7);
		if (o < 0) return;
		ops [o] = x; ops [o + 1] = y; ops [o + 2] = w; ops [o + 3] = h;
		ops [o + 4] = fromArgb; ops [o + 5] = toArgb; ops [o + 6] = vertical;
	}

	/**
	 * Text run: {@code text[start, start+length)} drawn with its cell's top-left at
	 * {@code (x, y)}. {@code bgArgb} is used only when {@code (flags & 1) != 0}
	 * (opaque background). {@code cellW}/{@code cellH}/{@code ascent} are GDI
	 * metrics so the GPU glyph run lands where GDI would place it.
	 */
	public void text (String text, int start, int length, int fontId, int x, int y, int fgArgb, int bgArgb, int flags, int cellW, int cellH, int ascent) {
		java.util.Objects.requireNonNull (text);
		java.util.Objects.checkFromIndexSize (start, length, text.length ());
		int o = begin (DRAW_TEXT, 12);
		if (o < 0) return;
		ops [o] = addRef (text); ops [o + 1] = start; ops [o + 2] = length; ops [o + 3] = fontId;
		ops [o + 4] = x; ops [o + 5] = y; ops [o + 6] = fgArgb; ops [o + 7] = bgArgb; ops [o + 8] = flags;
		ops [o + 9] = cellW; ops [o + 10] = cellH; ops [o + 11] = ascent;
	}

	/** Polygon or polyline: {@code points} holds x,y pairs (pixels). */
	public void poly (int op, int [] points, int argb, int mask) {
		int n = points.length & ~1;
		int o = begin (op, 3 + n);
		if (o < 0) return;
		ops [o] = n >> 1; ops [o + 1] = argb; ops [o + 2] = mask;
		System.arraycopy (points, 0, ops, o + 3, n);
	}

	public void plane (int planeBit) {
		int o = begin (PLANE, 1);
		if (o >= 0) ops [o] = planeBit;
	}

	public static int opOf (int header) { return header & 0xFF; }
	public static int lengthOf (int header) { return header >>> 8; }
}
