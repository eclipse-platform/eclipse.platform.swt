/*******************************************************************************
 * Copyright (c) 2026 Synexia, Hitesh Soliwal (hsoliwal@gmail.com) and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Apache License, Version 2.0, or (at your option) the Eclipse
 * Public License 2.0, which are available at
 * https://www.apache.org/licenses/LICENSE-2.0 and
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: Apache-2.0 OR EPL-2.0
 *
 * Contributors:
 *     Hitesh Soliwal (Synexia) - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.examples.superset;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

/**
 * Packed event counters for one demo: paint, layout, materialize (SetData,
 * items created, lines laid out) and measure calls. Every paint's clip
 * rectangle (the OS update region's bounds, i.e. what a VNC server would ship
 * as framebuffer update rectangles) is mapped into the root control's client
 * coordinates and fed to the {@link DamageSink}; its bounding union is kept
 * for the pixel-diff check.
 */
public final class Counters {

	public static final int PAINT = 0, LAYOUT = 1, MATERIALIZE = 2, MEASURE = 3, SLOTS = 4;

	private final int[] c = new int[SLOTS];
	private final DamageSink sink = new DamageSink();
	private Control root;
	private int ux0 = Integer.MAX_VALUE, uy0 = Integer.MAX_VALUE, ux1 = Integer.MIN_VALUE, uy1 = Integer.MIN_VALUE;
	private final Listener paintCounter = this::paint;

	private void paint(Event e) {
		c[PAINT]++;
		if (root == null || root.isDisposed()) return;
		int x = e.x, y = e.y;
		if (e.widget != root) {
			Rectangle r = root.getDisplay().map((Control) e.widget, root, e.x, e.y, e.width, e.height);
			x = r.x;
			y = r.y;
		}
		sink.paint(x, y, e.width, e.height);
		ux0 = Math.min(ux0, x);
		uy0 = Math.min(uy0, y);
		ux1 = Math.max(ux1, x + e.width);
		uy1 = Math.max(uy1, y + e.height);
	}

	public void inc(int slot) {
		c[slot]++;
	}

	public void add(int slot, int n) {
		c[slot] += n;
	}

	public int get(int slot) {
		return c[slot];
	}

	DamageSink sink() {
		return sink;
	}

	/** @return bounds of all damage since the last reset, in root client coordinates, or null */
	public Rectangle damageUnion() {
		return ux1 < ux0 ? null : new Rectangle(ux0, uy0, ux1 - ux0, uy1 - uy0);
	}

	public void reset() {
		java.util.Arrays.fill(c, 0);
		resetUnion();
	}

	/** Clears the damage union (frames of the sink are ended by the runner). */
	public void resetUnion() {
		ux0 = uy0 = Integer.MAX_VALUE;
		ux1 = uy1 = Integer.MIN_VALUE;
	}

	/** Adds the paint/damage counter to {@code control} and every descendant control. */
	public void hookPaint(Control control) {
		if (root == null) root = control;
		control.removeListener(SWT.Paint, paintCounter);
		control.addListener(SWT.Paint, paintCounter);
		if (control instanceof Composite composite) {
			for (Control child : composite.getChildren()) {
				hookPaint(child);
			}
		}
	}
}
