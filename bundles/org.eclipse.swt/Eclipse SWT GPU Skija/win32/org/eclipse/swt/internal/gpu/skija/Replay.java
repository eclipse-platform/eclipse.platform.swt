/*******************************************************************************
 * Copyright (c) 2026 Contributors to the Eclipse Foundation.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package org.eclipse.swt.internal.gpu.skija;

import org.eclipse.swt.internal.gpu.*;

import io.github.humbleui.skija.*;

/**
 * Replays a packed {@link OpStream} on a Skia canvas pointer with GDI pixel
 * conventions: fills cover {@code [x, x+w)}, cosmetic strokes are aliased
 * hairlines through pixel centres, lines include their end pixel, text cells
 * are positioned by their GDI ascent.
 */
final class Replay {
	static final int INTERSECT = ClipMode.INTERSECT.ordinal ();
	static final int TEXT_MASK = SkijaTarget.TEXT_MASK;

	private Replay () {
	}

	static int fillMask (int mask) {
		return (mask & ~3) | PaintKeys.KIND_FILL;
	}

	static void run (SkijaPlaneBackend backend, long canvas, OpStream ops) {
		int [] a = ops.ops ();
		int n = ops.size (), clipSave = -1;
		for (int i = 0; i < n;) {
			int header = a [i], op = OpStream.opOf (header), o = i + 1;
			i = o + OpStream.lengthOf (header);
			switch (op & ~OpStream.OPEN) {
				case OpStream.CLIP:
					if (clipSave >= 0) Canvas._nRestoreToCount (canvas, clipSave);
					clipSave = Canvas._nSave (canvas);
					Canvas._nClipRect (canvas, a [o], a [o + 1], a [o] + a [o + 2], a [o + 1] + a [o + 3], INTERSECT, false);
					break;
				case OpStream.CLIP_RESET:
					if (clipSave >= 0) Canvas._nRestoreToCount (canvas, clipSave);
					clipSave = -1;
					break;
				case OpStream.FILL_RECT:
					fillRect (backend, canvas, a [o], a [o + 1], a [o + 2], a [o + 3], a [o + 4], a [o + 5]);
					break;
				case OpStream.DRAW_RECT:
					drawRect (backend, canvas, a, o);
					break;
				case OpStream.DRAW_LINE:
					drawLine (backend, canvas, a, o);
					break;
				case OpStream.DRAW_POINT:
					Canvas._nDrawRect (canvas, a [o], a [o + 1], a [o] + 1, a [o + 1] + 1, backend.paint (fillMask (a [o + 5]), a [o + 4]));
					break;
				case OpStream.FILL_OVAL:
					Canvas._nDrawOval (canvas, a [o], a [o + 1], a [o] + a [o + 2], a [o + 1] + a [o + 3], backend.paint (a [o + 5], a [o + 4]));
					break;
				case OpStream.DRAW_OVAL:
					Canvas._nDrawOval (canvas, a [o] + .5f, a [o + 1] + .5f, a [o] + a [o + 2] + .5f, a [o + 1] + a [o + 3] + .5f, backend.paint (a [o + 5], a [o + 4]));
					break;
				case OpStream.FILL_GRADIENT:
					gradient (canvas, a, o);
					break;
				case OpStream.DRAW_TEXT:
					text (backend, canvas, ops, a, o);
					break;
				case OpStream.CLIP_PUSH:
					Canvas._nSave (canvas);
					Canvas._nClipRect (canvas, a [o], a [o + 1], a [o] + a [o + 2], a [o + 1] + a [o + 3], INTERSECT, false);
					break;
				case OpStream.CLIP_POP:
					Canvas._nRestore (canvas);
					break;
				case OpStream.GLYPH_RUN: {
					long blob = backend.glyphRun (a, o);
					if (blob != 0) Canvas._nDrawTextBlob (canvas, blob, a [o + 1], a [o + 2], backend.paint (TEXT_MASK, a [o + 3]));
					break;
				}
				case OpStream.FILL_POLY:
				case OpStream.DRAW_POLY:
					poly (backend, canvas, a, o, op);
					break;
				default:
					break;
			}
		}
		if (clipSave >= 0) Canvas._nRestoreToCount (canvas, clipSave);
	}

	static void fillRect (SkijaPlaneBackend backend, long canvas, int x, int y, int w, int h, int argb, int mask) {
		if (w < 0) { x += w; w = -w; }
		if (h < 0) { y += h; h = -h; }
		Canvas._nDrawRect (canvas, x, y, x + w, y + h, backend.paint (mask, argb));
	}

	static void drawRect (SkijaPlaneBackend backend, long canvas, int [] a, int o) {
		int x = a [o], y = a [o + 1], w = a [o + 2], h = a [o + 3];
		if (w < 0) { x += w; w = -w; }
		if (h < 0) { y += h; h = -h; }
		Canvas._nDrawRect (canvas, x + .5f, y + .5f, x + w + .5f, y + h + .5f, backend.paint (a [o + 5], a [o + 4]));
	}

	static void drawLine (SkijaPlaneBackend backend, long canvas, int [] a, int o) {
		int mask = a [o + 5], argb = a [o + 4];
		Canvas._nDrawLine (canvas, a [o] + .5f, a [o + 1] + .5f, a [o + 2] + .5f, a [o + 3] + .5f, backend.paint (mask, argb));
		if (PaintKeys.lineWidth (mask) <= 1f) {
			Canvas._nDrawRect (canvas, a [o + 2], a [o + 3], a [o + 2] + 1, a [o + 3] + 1, backend.paint (fillMask (mask), argb));
		}
	}

	static void text (SkijaPlaneBackend backend, long canvas, OpStream ops, int [] a, int o) {
		String string = (String) ops.ref (a [o]);
		int fontId = a [o + 3], x = a [o + 4], y = a [o + 5];
		if ((a [o + 8] & 1) != 0) {
			Canvas._nDrawRect (canvas, x, y, x + a [o + 9], y + a [o + 10], backend.paint (PaintKeys.KIND_FILL, a [o + 7]));
		}
		long blob = backend.blob (string, fontId);
		if (blob != 0) Canvas._nDrawTextBlob (canvas, blob, x, y + a [o + 11], backend.paint (TEXT_MASK, a [o + 6]));
	}

	static void gradient (long canvas, int [] a, int o) {
		int x = a [o], y = a [o + 1], w = a [o + 2], h = a [o + 3];
		if (w < 0) { x += w; w = -w; }
		if (h < 0) { y += h; h = -h; }
		boolean vertical = a [o + 6] != 0;
		try (Shader shader = Shader.makeLinearGradient (x, y, vertical ? x : x + w, vertical ? y + h : y, new int [] {a [o + 4], a [o + 5]});
				Paint paint = new Paint ().setShader (shader)) {
			Canvas._nDrawRect (canvas, x, y, x + w, y + h, paint._ptr);
		}
	}

	static void poly (SkijaPlaneBackend backend, long canvas, int [] a, int o, int op) {
		int count = a [o];
		if (count < 2) return;
		try (PathBuilder builder = new PathBuilder ()) {
			int p = o + 3;
			float offset = (op & ~OpStream.OPEN) == OpStream.DRAW_POLY ? .5f : 0f;
			builder.moveTo (a [p] + offset, a [p + 1] + offset);
			for (int k = 1; k < count; k++) builder.lineTo (a [p + 2 * k] + offset, a [p + 2 * k + 1] + offset);
			if ((op & OpStream.OPEN) == 0) builder.closePath ();
			try (Path path = builder.detach ()) {
				Canvas._nDrawPath (canvas, path._ptr, backend.paint (a [o + 2], a [o + 1]));
			}
		}
	}
}
