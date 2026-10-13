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
package org.eclipse.swt.internal.gpu;

import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.widgets.*;

/**
 * Cross-platform facade for retained paint planes beneath {@link GC}.
 * <p>
 * Widgets keep painting through the normal GC. A widget that wants its
 * selection, caret or transient feedback composited above cached content
 * brackets those GC calls with {@link #setPlane(GC, int)} and invalidates them
 * with {@link #invalidatePlanes(Control, int, int, int, int, int)} instead of
 * {@code redraw}. Without a platform hook (GTK, Cocoa, or the property unset)
 * both calls degrade to today's behaviour: GC calls draw immediately in call
 * order and invalidation is {@code redraw(x, y, w, h, false)}.
 * </p>
 * <p>
 * The retained path is a mask bit, not a class hierarchy: {@code -Dswt.gpu}
 * ({@code gdi} or {@code skija}) is read once and mapped at control creation to
 * a widget state bit; every hot-path check is {@code (state & BIT) != 0}.
 * </p>
 */
public final class RetainedPlanes {
	/** Plane bits. Retained (tile cached) planes reuse {@link ViewportRuntime} z-plane bit values. */
	public static final int BODY = ViewportRuntime.BODY, FROZEN = ViewportRuntime.FROZEN, HEADER = ViewportRuntime.HEADER;
	/** Overlay planes are composited over the tiles every presented frame and never invalidate tiles. */
	public static final int EDITOR = ViewportRuntime.EDITOR, FEEDBACK = ViewportRuntime.FEEDBACK;
	public static final int SELECTION = 1 << 6, CARET = 1 << 7;
	public static final int RETAINED = BODY | FROZEN | HEADER;
	public static final int OVERLAY = EDITOR | FEEDBACK | SELECTION | CARET;
	public static final int ALL_PLANES = RETAINED | OVERLAY;

	/** Mode bits read from {@code -Dswt.gpu}. */
	public static final int MODE_OFF = 0, MODE_RETAINED = 1, MODE_SKIJA = 2;

	/** Platform implementation; null means stock behaviour. */
	public interface Hook {
		void setPlane (GC gc, int plane);
		int drawnPlanes (GC gc);
		void invalidatePlanes (Control control, int planes, int x, int y, int width, int height);
	}

	public static final int MODE = parseMode (System.getProperty ("swt.gpu"));
	static volatile Hook hook;

	private RetainedPlanes () {
	}

	static int parseMode (String value) {
		if (value == null) return MODE_OFF;
		int mode = MODE_OFF;
		for (String part : value.split ("[,+]")) {
			switch (part.trim ()) {
				case "gdi", "retained" -> mode |= MODE_RETAINED;
				case "skija" -> mode |= MODE_RETAINED | MODE_SKIJA;
				default -> { }
			}
		}
		return mode;
	}

	/** Installed by the platform when {@link #MODE} is not {@link #MODE_OFF}. */
	public static void install (Hook platformHook) {
		hook = platformHook;
	}

	/** Subsequent drawing on {@code gc} belongs to {@code plane} (one plane bit). */
	public static void setPlane (GC gc, int plane) {
		Hook h = hook;
		if (h != null) h.setPlane (gc, plane);
	}

	/**
	 * Planes this paint pass executes. Widgets may skip work for planes that are
	 * not drawn (for example layout of content lines during an overlay-only pass).
	 */
	public static int drawnPlanes (GC gc) {
		Hook h = hook;
		return h != null ? h.drawnPlanes (gc) : ALL_PLANES;
	}

	/** Repaints {@code planes} inside the rectangle; retained planes are invalidated only when included. */
	public static void invalidatePlanes (Control control, int planes, int x, int y, int width, int height) {
		Hook h = hook;
		if (h != null) {
			h.invalidatePlanes (control, planes, x, y, width, height);
		} else {
			control.redraw (x, y, width, height, false);
		}
	}
}
