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

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Listener;

/**
 * Packed event counters for one demo: paint, layout, materialize (SetData,
 * items created, lines laid out) and measure calls. One int[] slot per kind,
 * reset by the runner between phases.
 */
public final class Counters {

	public static final int PAINT = 0, LAYOUT = 1, MATERIALIZE = 2, MEASURE = 3, SLOTS = 4;

	private final int[] c = new int[SLOTS];
	private final Listener paintCounter = e -> c[PAINT]++;

	public void inc(int slot) {
		c[slot]++;
	}

	public void add(int slot, int n) {
		c[slot] += n;
	}

	public int get(int slot) {
		return c[slot];
	}

	public void reset() {
		java.util.Arrays.fill(c, 0);
	}

	/** Adds the paint counter to {@code control} and every descendant control. */
	public void hookPaint(Control control) {
		control.removeListener(SWT.Paint, paintCounter);
		control.addListener(SWT.Paint, paintCounter);
		if (control instanceof Composite composite) {
			for (Control child : composite.getChildren()) {
				hookPaint(child);
			}
		}
	}
}
