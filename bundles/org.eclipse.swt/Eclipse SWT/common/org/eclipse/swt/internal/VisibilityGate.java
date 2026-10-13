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

import org.eclipse.swt.widgets.*;

/**
 * Internal effective-visibility gate shared by SWT widgets outside the
 * <code>org.eclipse.swt.widgets</code> package (StyledText, custom widgets,
 * Nebula).
 * <p>
 * A control that is not effectively visible should do no paint, measurement,
 * data population, idle or animation work. Callers ask {@link #defer(Control, int)}
 * before such work; when it returns <code>true</code> the work was skipped and
 * recorded as dirty bits on the control. When the control becomes visible the
 * platform sends the internal event {@link #CATCH_UP} once, with
 * <code>event.detail</code> holding the kinds that were skipped.
 * </p>
 * <p>
 * Kinds are small ints so they can be packed into the platform widget state.
 * All methods are allocation-free. When no platform port is installed (GTK and
 * Cocoa today) every query answers "visible", so callers keep stock behaviour.
 * </p>
 * <p>
 * IMPORTANT: This class is <em>not</em> part of the SWT public API. It is marked
 * public only so that it can be shared within the packages provided by SWT.
 * </p>
 */
//FLLDCIM: HPCPOP - bit tests only; port installed once by the platform Display
public final class VisibilityGate {
	/** Skipped painting (redraw, WM_PAINT, repaint-driven data). */
	public static final int PAINT = 1;
	/** Skipped layout. */
	public static final int LAYOUT = 2;
	/** Skipped owner-specific measurement (text extents, row heights). */
	public static final int MEASURE = 4;
	/** Skipped owner-specific data population, idle passes or animation ticks. */
	public static final int DATA = 8;
	/** All kinds. */
	public static final int ALL = PAINT | LAYOUT | MEASURE | DATA;

	/**
	 * Internal event type sent once when a control with deferred
	 * {@link #MEASURE} or {@link #DATA} work becomes effectively visible.
	 * <code>event.detail</code> holds the deferred kinds.
	 */
	public static final int CATCH_UP = 0x5647;

	/** Box-model rings, outside in: border, scrollbar track, inset (padding), margin (content margin). */
	public static final int BORDER = 0, TRACK = 1, INSET = 2, MARGIN = 3;
	/** Sides inside one ring; ring {@code r} side {@code s} is element {@code r * 4 + s} of a box array. */
	public static final int TOP = 0, RIGHT = 1, BOTTOM = 2, LEFT = 3;
	/** Length of a box array: four rings of four sides, device pixels. */
	public static final int BOX_LENGTH = 16;

	/**
	 * Platform port. Implemented by the platform <code>widgets</code> package,
	 * which owns the widget state bits.
	 */
	public static abstract class Port {
		/** Fills {@code out [ring * 4 + side]} in device pixels; false when the platform has no box model. */
		protected boolean boxEdges (Control control, int [] out) {
			return false;
		}

		/** Stores the inset and margin rings (packed with {@link VisibilityGate#pack}). */
		protected void setBoxEdges (Control control, int packedInset, int packedMargin) {
		}

		/** Routes the control's WM_PAINT update regions (and blit copies) to the recorder; null detaches. */
		protected void attachDamageRecorder (Control control, ViewportDamageRecorder recorder) {
		}

		/**
		 * @param control the control, not disposed
		 * @param paint <code>true</code> to include reasons that gate painting
		 *   only (occluded or cloaked shell)
		 * @return <code>true</code> when the control is not effectively visible
		 */
		protected abstract boolean offscreen (Control control, boolean paint);

		/**
		 * @param control the control, not disposed
		 * @param kinds bitwise OR of {@link #PAINT}, {@link #LAYOUT},
		 *   {@link #MEASURE}, {@link #DATA}
		 * @return <code>true</code> when the work was skipped and recorded
		 */
		protected abstract boolean defer (Control control, int kinds);
	}

	private static Port port;

	/**
	 * Packs four edges of one ring into an int, 8 bits per side (0..255 device pixels,
	 * clamped). Bit operations only, so the value is identical in Java and GWT.
	 */
	public static int pack (int top, int right, int bottom, int left) {
		return clampEdge (top) | (clampEdge (right) << 8) | (clampEdge (bottom) << 16) | (clampEdge (left) << 24);
	}

	/** One side of a packed ring. */
	public static int unpack (int packed, int side) {
		return (packed >>> (side << 3)) & 0xFF;
	}

	private static int clampEdge (int edge) {
		return edge < 0 ? 0 : (edge > 0xFF ? 0xFF : edge);
	}

	/**
	 * The control's box model, outside in: {@link #BORDER}, {@link #TRACK} (native
	 * scrollbar non-client area), {@link #INSET}, {@link #MARGIN}; the content viewport
	 * is what remains. One computation feeds the client area, the scrollbar rectangles,
	 * blit clips and the protected damage set.
	 *
	 * @param control the control
	 * @param out at least {@link #BOX_LENGTH} ints, filled with device pixels
	 * @return false when unsupported (no port, disposed, not a Scrollable)
	 */
	public static boolean boxEdges (Control control, int [] out) {
		Port p = port;
		return p != null && control != null && out != null && out.length >= BOX_LENGTH && !control.isDisposed () && p.boxEdges (control, out);
	}

	/** Sets the inset and margin rings of a Scrollable (packed with {@link #pack}). */
	public static void setBoxEdges (Control control, int packedInset, int packedMargin) {
		Port p = port;
		if (p != null && control != null && !control.isDisposed ()) {
			p.setBoxEdges (control, packedInset, packedMargin);
		}
	}

	/**
	 * Adds the protected rings (border, track, inset, margin) to a region, outside in, in
	 * content-viewport coordinates: the viewport starts at (0, 0) and the rings lie at
	 * negative coordinates and beyond its width and height. Content damage must never
	 * touch this set.
	 *
	 * @return false when the box model is unavailable
	 */
	public static boolean protectBox (Control control, ViewportDamageRegion region, int viewportWidth, int viewportHeight) {
		int [] box = new int [BOX_LENGTH];
		if (region == null || !boxEdges (control, box)) {
			return false;
		}
		int x = 0, y = 0, w = viewportWidth, h = viewportHeight;
		for (int ring = MARGIN; ring >= BORDER; ring--) {
			x -= box [ring * 4 + LEFT];
			y -= box [ring * 4 + TOP];
			w += box [ring * 4 + LEFT] + box [ring * 4 + RIGHT];
			h += box [ring * 4 + TOP] + box [ring * 4 + BOTTOM];
		}
		for (int ring = BORDER; ring <= MARGIN; ring++) {
			int top = box [ring * 4 + TOP], right = box [ring * 4 + RIGHT], bottom = box [ring * 4 + BOTTOM], left = box [ring * 4 + LEFT];
			region.addFrame (x, y, w, h, top, right, bottom, left);
			x += left;
			y += top;
			w -= left + right;
			h -= top + bottom;
		}
		return true;
	}

	/**
	 * Test hook: records every WM_PAINT update region of the control (and of its
	 * descendants, mapped into its content viewport) as painted damage, so tests can
	 * isolate exactly which pixels each frame repainted. Off unless attached.
	 *
	 * @param recorder the recorder, or null to detach
	 */
	public static void attachDamageRecorder (Control control, ViewportDamageRecorder recorder) {
		Port p = port;
		if (p != null && control != null && !control.isDisposed ()) {
			p.attachDamageRecorder (control, recorder);
		}
	}

	private VisibilityGate () {
	}

	/**
	 * Installs the platform port. The first installation wins.
	 *
	 * @param platformPort the port
	 */
	public static synchronized void install (Port platformPort) {
		if (port == null) {
			port = platformPort;
		}
	}

	/**
	 * Returns whether the control is not effectively visible.
	 *
	 * @param control the control
	 * @param paint <code>true</code> to also count shells that are fully covered
	 *   or on another virtual desktop
	 * @return <code>true</code> when the control cannot be seen
	 */
	public static boolean isOffscreen (Control control, boolean paint) {
		Port p = port;
		return p != null && control != null && !control.isDisposed () && p.offscreen (control, paint);
	}

	/**
	 * Skips and records work for a control that is not effectively visible.
	 * {@link #PAINT} and {@link #DATA} are also skipped while the shell is
	 * covered; {@link #LAYOUT} and {@link #MEASURE} only while the control is
	 * hidden, minimized, zero-sized or clipped out by an ancestor.
	 *
	 * @param control the control
	 * @param kinds the kinds of work about to be done
	 * @return <code>true</code> when the caller must skip the work; a
	 *   {@link #CATCH_UP} event follows when the control becomes visible
	 */
	public static boolean defer (Control control, int kinds) {
		Port p = port;
		return p != null && control != null && (kinds & ALL) != 0 && !control.isDisposed () && p.defer (control, kinds & ALL);
	}
}
