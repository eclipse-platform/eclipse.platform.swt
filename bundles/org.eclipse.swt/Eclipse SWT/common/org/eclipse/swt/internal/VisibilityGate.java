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
 *
 * @noreference This class is not intended to be referenced by clients.
 * @noinstantiate This class is not intended to be instantiated by clients.
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

	/**
	 * Platform port. Implemented by the platform <code>widgets</code> package,
	 * which owns the widget state bits.
	 */
	public static abstract class Port {
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
