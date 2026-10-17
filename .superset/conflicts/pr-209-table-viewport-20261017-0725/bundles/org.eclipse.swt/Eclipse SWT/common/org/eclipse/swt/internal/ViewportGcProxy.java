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
package org.eclipse.swt.internal;

import org.eclipse.swt.graphics.*;

/**
 * Scoped state guard for viewport-controlled painting on an SWT {@link GC}.
 *
 * <p>The public GC remains the execution surface and is never retained by a
 * paint graph.  A scope snapshots the mutable graphics state that one viewport
 * plane or renderer can leak into the next plane and restores it
 * deterministically on close.  The scope owns only its temporary Region and
 * Transform snapshots; application colors, patterns and fonts remain owned by
 * their original SWT Device.</p>
 *
 * <p>This is intentionally an internal immediate-mode adapter, not a proxy
 * scene graph.  Stable decoration may still be retained by
 * {@link ViewportPaintGraph}; dynamic body and application paint callbacks keep
 * executing immediately against the caller's GC.</p>
 *
 * @noreference This class is not intended to be referenced by clients.
 */
public final class ViewportGcProxy implements AutoCloseable {

	private final GC gc;
	private final Region originalClipping;
	private final Region originalDeviceClipping;
	private final boolean originalAdvanced;
	private final Transform originalTransform;
	private final LineAttributes originalLineAttributes;
	private final int originalAlpha;
	private final int originalAntialias;
	private final int originalTextAntialias;
	private final int originalInterpolation;
	private final int originalFillRule;
	private final boolean originalXorMode;
	private final Color originalForeground;
	private final Color originalBackground;
	private final Pattern originalForegroundPattern;
	private final Pattern originalBackgroundPattern;
	private final Font originalFont;
	private boolean closed;

	private ViewportGcProxy (GC gc) {
		if (gc == null) throw new IllegalArgumentException ("null GC");
		if (gc.isDisposed ()) throw new IllegalArgumentException ("disposed GC");
		this.gc = gc;
		originalAdvanced = gc.getAdvanced ();
		Region clipping = new Region (gc.getDevice ());
		Region deviceClipping = null;
		Transform transform = null;
		try {
			gc.getClipping (clipping);
			transform = new Transform (gc.getDevice ());
			gc.getTransform (transform);
			if (!transform.isIdentity ()) {
				/* Integer user-space Regions lose pixels when an affine is undone/reapplied. */
				deviceClipping = new Region (gc.getDevice ());
				try {
					gc.setTransform (null);
					gc.getClipping (deviceClipping);
				} finally {
					gc.setTransform (transform);
				}
			}
			originalClipping = clipping;
			originalDeviceClipping = deviceClipping;
			originalTransform = transform;
			originalLineAttributes = copy (gc.getLineAttributes ());
			originalAlpha = gc.getAlpha ();
			originalAntialias = gc.getAntialias ();
			originalTextAntialias = gc.getTextAntialias ();
			originalInterpolation = gc.getInterpolation ();
			originalFillRule = gc.getFillRule ();
			originalXorMode = gc.getXORMode ();
			originalForeground = gc.getForeground ();
			originalBackground = gc.getBackground ();
			originalForegroundPattern = gc.getForegroundPattern ();
			originalBackgroundPattern = gc.getBackgroundPattern ();
			originalFont = gc.getFont ();
		} catch (RuntimeException | Error failure) {
			if (deviceClipping != null) deviceClipping.dispose ();
			if (transform != null) transform.dispose ();
			clipping.dispose ();
			throw failure;
		}
	}

	public static ViewportGcProxy wrap (GC gc) {
		return new ViewportGcProxy (gc);
	}

	public GC gc () {
		checkOpen ();
		return gc;
	}

	public ViewportGcProxy clip (Rectangle rectangle) {
		checkOpen ();
		if (rectangle == null) throw new IllegalArgumentException ("null clip");
		Region next = new Region (gc.getDevice ());
		try {
			next.add (rectangle);
			next.intersect (originalClipping);
			gc.setClipping (next);
		} finally {
			next.dispose ();
		}
		return this;
	}

	public ViewportGcProxy transform (ViewportPaintGraph.Affine affine) {
		checkOpen ();
		if (affine == null) throw new IllegalArgumentException ("null affine");
		if (affine.isIdentity ()) return this;
		Transform next = new Transform (gc.getDevice ());
		Transform delta = null;
		try {
			gc.getTransform (next);
			delta = new Transform (
					gc.getDevice (),
					affine.m11 (), affine.m12 (), affine.m21 (),
					affine.m22 (), affine.dx (), affine.dy ());
			next.multiply (delta);
			gc.setTransform (next);
		} finally {
			if (delta != null) delta.dispose ();
			next.dispose ();
		}
		return this;
	}

	public ViewportGcProxy translate (float x, float y) {
		return transform (ViewportPaintGraph.Affine.translation (x, y));
	}

	public ViewportGcProxy lineAttributes (LineAttributes attributes) {
		checkOpen ();
		if (attributes == null) throw new IllegalArgumentException ("null line attributes");
		gc.setLineAttributes (copy (attributes));
		return this;
	}

	public ViewportGcProxy alpha (int alpha) {
		checkOpen ();
		gc.setAlpha (alpha);
		return this;
	}

	@Override
	public void close () {
		if (closed) return;
		closed = true;
		if (gc.isDisposed ()) {
			originalTransform.dispose ();
			originalClipping.dispose ();
			if (originalDeviceClipping != null) originalDeviceClipping.dispose ();
			return;
		}
		try {
			gc.setLineAttributes (originalLineAttributes);
			if (originalAdvanced) {
				gc.setTransform (originalTransform);
				gc.setAlpha (originalAlpha);
				gc.setAntialias (originalAntialias);
				gc.setTextAntialias (originalTextAntialias);
				gc.setInterpolation (originalInterpolation);
				gc.setFillRule (originalFillRule);
			} else {
				/*
				 * Disabling advanced mode resets transform/pattern/alpha/AA state.
				 * Restore basic state and the exact clipping region afterwards.
				 */
				gc.setAdvanced (false);
			}
			gc.setXORMode (originalXorMode);
			gc.setForeground (originalForeground);
			gc.setBackground (originalBackground);
			if (originalAdvanced) {
				gc.setForegroundPattern (originalForegroundPattern);
				gc.setBackgroundPattern (originalBackgroundPattern);
			}
			gc.setFont (originalFont);
			if (originalDeviceClipping == null) {
				gc.setClipping (originalClipping);
			} else {
				try {
					gc.setTransform (null);
					gc.setClipping (originalDeviceClipping);
				} finally {
					gc.setTransform (originalTransform);
				}
			}
		} finally {
			originalTransform.dispose ();
			originalClipping.dispose ();
			if (originalDeviceClipping != null) originalDeviceClipping.dispose ();
		}
	}

	private void checkOpen () {
		if (closed) throw new IllegalStateException ("viewport GC scope closed");
		if (gc.isDisposed ()) throw new IllegalStateException ("GC disposed while scope active");
	}

	private static LineAttributes copy (LineAttributes attributes) {
		float [] dash = attributes.dash == null ? null : attributes.dash.clone ();
		return new LineAttributes (
				attributes.width,
				attributes.cap,
				attributes.join,
				attributes.style,
				dash,
				attributes.dashOffset,
				attributes.miterLimit);
	}
}
