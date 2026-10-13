/*******************************************************************************
 * Copyright (c) 2026 Hitesh Soliwal and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package org.eclipse.swt.examples.superset;

import java.lang.reflect.Method;

/**
 * Per-frame damage sink fed from SWT.Paint clip rectangles. When the SWT jar
 * under test carries {@code org.eclipse.swt.internal.ViewportDamageRecorder}
 * (SWT fork PR #121, owned by peer session com-synexia-d2) every rectangle is
 * forwarded to it and the frame numbers come from it. Otherwise (for example
 * upstream SWT 3.135.0) the interim sums below are used: damaged pixels,
 * rectangle count, full repaint (painted area of at least 95% of the
 * viewport) and pixels painted over the protected band.
 */
final class DamageSink {

	private static final String RECORDER = "org.eclipse.swt.internal.ViewportDamageRecorder";
	private final Object recorder;
	private Method rPaint, rEnd, rFrames, rDamage, rRects, rFull, rHit, rPainted, rVisible, rViewport, rProtected, rAdd, rClear;
	private int width, height;
	private int[] bands = new int[0];
	private long px, hit, visiblePx;
	private int rects;

	DamageSink() {
		Object r = null;
		Class<?> type = Reflect.type(RECORDER);
		if (type != null) {
			try {
				r = type.getConstructor(int.class, int.class).newInstance(1, 1);
				bind(type);
			} catch (ReflectiveOperationException | RuntimeException e) {
				r = null;
			}
		}
		recorder = r;
	}

	private void bind(Class<?> t) throws NoSuchMethodException {
		rPaint = t.getMethod("paint", int.class, int.class, int.class, int.class);
		rEnd = t.getMethod("endFrame");
		rFrames = t.getMethod("frameCount");
		rDamage = t.getMethod("damagePx", int.class);
		rRects = t.getMethod("damageRects", int.class);
		rFull = t.getMethod("isFullRepaint", int.class);
		rHit = t.getMethod("protectedHitPx", int.class);
		rPainted = t.getMethod("paintedPx", int.class);
		rVisible = t.getMethod("visiblePaintedPx", int.class);
		rViewport = t.getMethod("setViewport", int.class, int.class);
		rProtected = t.getMethod("protectedArea");
		Class<?> region = rProtected.getReturnType();
		rAdd = region.getMethod("add", int.class, int.class, int.class, int.class);
		rClear = region.getMethod("clear");
	}

	/** @return "ViewportDamageRecorder" or "interim-clip-rect" */
	String source() {
		return recorder != null ? "ViewportDamageRecorder" : "interim-clip-rect";
	}

	/** Sets the viewport (client area) and the protected band of {@code band} px inside its edges. */
	void viewport(int w, int h, int band) {
		width = w;
		height = h;
		bands = new int[] {0, 0, w, band, 0, h - band, w, band, 0, 0, band, h, w - band, 0, band, h};
		if (recorder == null) return;
		call(rViewport, w, h);
		Object area = call(rProtected);
		call(rClear, area);
		for (int i = 0; i < bands.length; i += 4) Reflect.exec(rAdd, area, bands[i], bands[i + 1], bands[i + 2], bands[i + 3]);
	}

	void paint(int x, int y, int w, int h) {
		if (recorder != null) {
			call(rPaint, x, y, w, h);
			return;
		}
		px += (long) w * h;
		rects++;
		for (int i = 0; i < bands.length; i += 4) hit += overlap(x, y, w, h, bands[i], bands[i + 1], bands[i + 2], bands[i + 3]);
		visiblePx += overlap(x, y, w, h, 0, 0, width, height);
	}

	private static long overlap(int x, int y, int w, int h, int bx, int by, int bw, int bh) {
		long ow = Math.min(x + w, bx + bw) - Math.max(x, bx), oh = Math.min(y + h, by + bh) - Math.max(y, by);
		return ow > 0 && oh > 0 ? ow * oh : 0;
	}

	/** Ends a frame. @return {damagePx, rects, fullRepaint 0/1, protectedHitPx, paintedPx, visiblePaintedPx} */
	long[] endFrame() {
		if (recorder != null) {
			call(rEnd);
			int f = (int) Math.min(Integer.MAX_VALUE, (Long) call(rFrames) - 1);
			return new long[] {(Long) call(rDamage, f), (Integer) call(rRects, f), (Boolean) call(rFull, f) ? 1 : 0, (Long) call(rHit, f),
					(Long) call(rPainted, f), (Long) call(rVisible, f)};
		}
		long viewport = (long) width * height;
		long[] out = {px, rects, viewport > 0 && visiblePx * 100 >= viewport * 95 ? 1 : 0, hit, px, visiblePx};
		px = hit = visiblePx = 0;
		rects = 0;
		return out;
	}

	private Object call(Method m, Object... args) {
		return Reflect.exec(m, m == rClear || m == rAdd ? args[0] : recorder, m == rClear ? new Object[0] : args);
	}
}
