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
package org.eclipse.swt.graphics;

import org.eclipse.swt.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.gpu.*;
import org.eclipse.swt.internal.win32.*;

/**
 * Plane gate and GPU op recorder beneath win32 {@link GC}.
 * <p>
 * Every drawing method of GC calls one {@code divert*} method once
 * {@code data.planeDraw != -1} (that is, only inside a retained-plane paint
 * pass). A diverted call returns {@code true} when the GC must not touch its
 * HDC: either the current plane is not executed by this pass, or the call was
 * recorded into the pass's {@link OpStream} for GPU replay. A call the GPU
 * cannot replay marks the stream failed; the frame driver then repaints that
 * plane with GDI. State setters (colour, font, clip) still update the HDC so
 * metric queries made by widget paint code stay exact.
 * </p>
 */
final class GCPlanes {
	/** retainedMask bits tracked by GC state setters while a sink is active. */
	static final int TRANSFORMED = 1 << 8, XOR = 1 << 9;
	static final int TEXT_OPAQUE = 1;

	private GCPlanes () {
	}

	private static OpStream sink (GC gc) {
		return (OpStream) gc.data.planeSink;
	}

	/** True when the current plane is skipped or a sink is recording. */
	static boolean skipOrRecord (GC gc) {
		GCData data = gc.data;
		return (data.plane & data.planeDraw) == 0 || data.planeSink != null;
	}

	private static boolean unsupportedState (GCData data) {
		return (data.retainedMask & (TRANSFORMED | XOR)) != 0 || data.foregroundPattern != null || data.backgroundPattern != null
			|| (data.lineStyle == SWT.LINE_CUSTOM && data.lineDashes != null);
	}

	private static int mask (GCData data, int kind) {
		return PaintKeys.mask (kind, data.lineStyle, data.lineCap, data.lineJoin, -1, 0, data.lineWidth, PaintKeys.BACKEND_SKIJA);
	}

	/** Shape ops: FILL_RECT/FILL_OVAL use the background, DRAW_* the foreground. */
	static boolean divert (GC gc, int op, int a, int b, int c, int d) {
		GCData data = gc.data;
		if ((data.plane & data.planeDraw) == 0) return true;
		OpStream sink = sink (gc);
		if (sink == null) return false;
		if (unsupportedState (data)) {
			sink.fail |= OpStream.FAIL_UNSUPPORTED;
			return true;
		}
		boolean fill = op == OpStream.FILL_RECT || op == OpStream.FILL_OVAL;
		int colorref = fill ? data.background : data.foreground;
		sink.rect (op, a, b, c, d, PaintKeys.argbFromColorref (colorref, data.alpha), mask (data, fill ? PaintKeys.KIND_FILL : PaintKeys.KIND_STROKE));
		return true;
	}

	static boolean divertGradient (GC gc, int x, int y, int w, int h, boolean vertical) {
		GCData data = gc.data;
		if ((data.plane & data.planeDraw) == 0) return true;
		OpStream sink = sink (gc);
		if (sink == null) return false;
		if (unsupportedState (data)) {
			sink.fail |= OpStream.FAIL_UNSUPPORTED;
			return true;
		}
		sink.gradient (x, y, w, h, PaintKeys.argbFromColorref (data.foreground, data.alpha),
			PaintKeys.argbFromColorref (data.background, data.alpha), vertical ? 1 : 0);
		return true;
	}

	static boolean divertPoly (GC gc, int op, int [] points) {
		GCData data = gc.data;
		if ((data.plane & data.planeDraw) == 0) return true;
		OpStream sink = sink (gc);
		if (sink == null) return false;
		if (unsupportedState (data)) {
			sink.fail |= OpStream.FAIL_UNSUPPORTED;
			return true;
		}
		boolean fill = op == OpStream.FILL_POLY;
		sink.poly (op, points, PaintKeys.argbFromColorref (fill ? data.background : data.foreground, data.alpha),
			mask (data, fill ? PaintKeys.KIND_FILL : PaintKeys.KIND_STROKE));
		return true;
	}

	/** Operations with no GPU replay (images, paths, arcs, focus, copyArea, TextLayout). */
	static boolean divertUnsupported (GC gc, int reason) {
		GCData data = gc.data;
		if ((data.plane & data.planeDraw) == 0) return true;
		OpStream sink = sink (gc);
		if (sink == null) return false;
		sink.fail |= reason;
		return true;
	}

	/** Records a clip change; non-rectangular regions make the stream unreplayable. */
	static void recordClip (GC gc, long hRgn) {
		OpStream sink = sink (gc);
		if (hRgn == 0) {
			sink.clipReset ();
			return;
		}
		RECT box = gc.clipBoxScratch ();
		int kind = OS.GetRgnBox (hRgn, box);
		if (kind == 2 /* SIMPLEREGION */) {
			sink.clip (box.left, box.top, box.right - box.left, box.bottom - box.top);
		} else if (kind == OS.NULLREGION) {
			sink.clip (0, 0, 0, 0);
		} else {
			sink.fail |= OpStream.FAIL_UNSUPPORTED;
		}
	}

	/** Text through drawString/drawText; tabs, delimiters and mnemonics are not replayed. */
	static boolean divertText (GC gc, String string, int x, int y, int flags) {
		GCData data = gc.data;
		if ((data.plane & data.planeDraw) == 0) return true;
		OpStream sink = sink (gc);
		if (sink == null) return false;
		if (unsupportedState (data) || (data.style & SWT.MIRRORED) != 0 || !plainText (string, flags)) {
			sink.fail |= OpStream.FAIL_UNSUPPORTED;
			return true;
		}
		int id = fontId (gc);
		if (id < 0) {
			sink.fail |= OpStream.FAIL_UNSUPPORTED;
			return true;
		}
		FontTable fonts = FontTable.INSTANCE;
		boolean opaque = (flags & SWT.DRAW_TRANSPARENT) == 0;
		int width = opaque ? textWidth (gc, string) : 0;
		sink.text (string, 0, string.length (), id, x, y, PaintKeys.argbFromColorref (data.foreground, data.alpha),
			PaintKeys.argbFromColorref (data.background, data.alpha), opaque ? TEXT_OPAQUE : 0, width,
			fonts.cellHeight [id], fonts.ascent [id]);
		return true;
	}

	static boolean plainText (String string, int flags) {
		for (int i = 0, n = string.length (); i < n; i++) {
			char c = string.charAt (i);
			if (c < ' ' || (c == '&' && (flags & SWT.DRAW_MNEMONIC) != 0) || Character.isSurrogate (c)) return false;
		}
		return true;
	}

	private static int textWidth (GC gc, String string) {
		SIZE size = gc.textSizeScratch ();
		char [] chars = gc.textCharsScratch (string);
		OS.GetTextExtentPoint32 (gc.handle, chars, string.length (), size);
		return size.cx;
	}

	/** Int id of the GC's font in {@link FontTable}, or -1 when it cannot be replayed. */
	static int fontId (GC gc) {
		Font font = gc.data.font;
		if (font.gpuFontId != 0) return font.gpuFontId - 1;
		gc.checkGC (GC.FONT);
		LOGFONT logFont = new LOGFONT ();
		OS.GetObject (SWTFontProvider.getFontHandle (font, gc.data.nativeZoom), LOGFONT.sizeof, logFont);
		if (logFont.lfUnderline != 0 || logFont.lfStrikeOut != 0) return -1;
		TEXTMETRIC metric = new TEXTMETRIC ();
		OS.GetTextMetrics (gc.handle, metric);
		char [] face = logFont.lfFaceName;
		int n = 0;
		while (n < face.length && face [n] != 0) n++;
		int pixelHeight = logFont.lfHeight < 0 ? -logFont.lfHeight : metric.tmHeight - metric.tmInternalLeading;
		int id = FontTable.INSTANCE.define (face, n, logFont.lfHeight, logFont.lfWeight, logFont.lfItalic, pixelHeight, metric.tmAscent, metric.tmHeight);
		if (id >= 0) font.gpuFontId = id + 1;
		return id;
	}
}
