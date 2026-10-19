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
package org.eclipse.swt.widgets;

import org.eclipse.swt.internal.*;

/**
 * Time debounce for bursty non-input work (data-change storms, SetData
 * completions, model refreshes, the exact layout after a live resize).
 * Input-driven work (scroll, wheel, drag, typing) is coalesced per frame by its
 * owner and never comes here.
 * <p>
 * Per pending control: one deadline int, one burst-start int and one kind mask
 * in reusable Display arrays; one preallocated timer Runnable drives every
 * control. Each call extends the deadline (adaptive) but never past
 * {@link VisibilityGate#DEBOUNCE_MAX_MS} after the burst's first call. When the
 * deadline passes the control receives one {@link VisibilityGate#DEBOUNCED}
 * event with the ORed kinds in <code>event.detail</code>.
 * </p>
 */
//FLLDCIM: HPCPOP - per call: one array scan + int compares, no allocation
final class VisibilityDebounce {
	private VisibilityDebounce () {
	}

	static int now (Display d) {
		return (int) ((System.nanoTime () - d.debounceEpoch) / 1_000_000L);
	}

	static boolean schedule (Control c, int kind, int delayMs) {
		Display d = c.display;
		int delay = delayMs <= 0 ? VisibilityGate.DEBOUNCE_FRAME_MS : Math.min (delayMs, VisibilityGate.DEBOUNCE_MAX_MS);
		int t = now (d);
		int index = -1;
		for (int i = 0; i < d.debounceCount; i++) {
			if (d.debounceControls [i] == c) {
				index = i;
				break;
			}
		}
		if (index < 0) {
			if (d.debounceCount == d.debounceControls.length) {
				int n = d.debounceCount * 2;
				Control [] controls = new Control [n];
				int [] deadline = new int [n], start = new int [n], kinds = new int [n];
				System.arraycopy (d.debounceControls, 0, controls, 0, d.debounceCount);
				System.arraycopy (d.debounceDeadline, 0, deadline, 0, d.debounceCount);
				System.arraycopy (d.debounceStart, 0, start, 0, d.debounceCount);
				System.arraycopy (d.debounceKinds, 0, kinds, 0, d.debounceCount);
				d.debounceControls = controls;
				d.debounceDeadline = deadline;
				d.debounceStart = start;
				d.debounceKinds = kinds;
			}
			index = d.debounceCount++;
			d.debounceControls [index] = c;
			d.debounceStart [index] = t;
			d.debounceDeadline [index] = t;
			d.debounceKinds [index] = 0;
		}
		int cap = d.debounceStart [index] + VisibilityGate.DEBOUNCE_MAX_MS;
		d.debounceDeadline [index] = Math.min (Math.max (d.debounceDeadline [index], t + delay), cap);
		d.debounceKinds [index] |= kind;
		arm (d, t);
		return true;
	}

	/** Arms the single Display timer for the earliest pending deadline. */
	static void arm (Display d, int t) {
		if (d.debounceCount == 0) {
			return;
		}
		int earliest = d.debounceDeadline [0];
		for (int i = 1; i < d.debounceCount; i++) {
			earliest = Math.min (earliest, d.debounceDeadline [i]);
		}
		if (d.debounceArmed && earliest >= d.debounceArmedFor) {
			return;
		}
		d.debounceArmed = true;
		d.debounceArmedFor = earliest;
		d.timerExec (Math.max (1, earliest - t), d.debounceTick);
	}

	/** Timer: deliver every burst whose deadline passed, then re-arm. */
	static void tick (Display d) {
		d.debounceArmed = false;
		int t = now (d);
		for (int i = 0; i < d.debounceCount; ) {
			if (d.debounceDeadline [i] > t) {
				i++;
				continue;
			}
			Control c = d.debounceControls [i];
			int kinds = d.debounceKinds [i];
			int last = --d.debounceCount;
			d.debounceControls [i] = d.debounceControls [last];
			d.debounceDeadline [i] = d.debounceDeadline [last];
			d.debounceStart [i] = d.debounceStart [last];
			d.debounceKinds [i] = d.debounceKinds [last];
			d.debounceControls [last] = null;
			if (c != null && !c.isDisposed ()) {
				Event event = new Event ();
				event.detail = kinds;
				c.sendEvent (VisibilityGate.DEBOUNCED, event);
			}
		}
		arm (d, now (d));
	}

	/** Display.release. */
	static void release (Display d) {
		for (int i = 0; i < d.debounceCount; i++) {
			d.debounceControls [i] = null;
		}
		d.debounceCount = 0;
		d.debounceArmed = false;
	}
}
