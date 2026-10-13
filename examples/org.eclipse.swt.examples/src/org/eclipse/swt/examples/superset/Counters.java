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
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

/**
 * Packed event counters for one demo: paint, layout, materialize (SetData,
 * items created, lines laid out) and measure calls, plus the damage area of
 * every paint. Damage comes from the paint event's clip rectangle (the OS
 * update region's bounds, i.e. what a VNC server would ship as framebuffer
 * update rectangles) until a DamageRecorder hook exists in SWT.
 */
public final class Counters {

	public static final int PAINT = 0, LAYOUT = 1, MATERIALIZE = 2, MEASURE = 3, DAMAGE_RECTS = 4, FULL_REPAINTS = 5,
			SLOTS = 6;

	private final int[] c = new int[SLOTS];
	private long damagePx;
	private Control root;
	private int ux0 = Integer.MAX_VALUE, uy0 = Integer.MAX_VALUE, ux1 = Integer.MIN_VALUE, uy1 = Integer.MIN_VALUE;
	private final Listener paintCounter = this::paint;

	private void paint(Event e) {
		c[PAINT]++;
		c[DAMAGE_RECTS]++;
		damagePx += (long) e.width * e.height;
		Point size = ((Control) e.widget).getSize();
		if (e.width >= size.x && e.height >= size.y) c[FULL_REPAINTS]++;
		if (root == null || root.isDisposed()) return;
		Rectangle r = e.widget == root ? new Rectangle(e.x, e.y, e.width, e.height)
				: root.getDisplay().map((Control) e.widget, root, e.x, e.y, e.width, e.height);
		ux0 = Math.min(ux0, r.x);
		uy0 = Math.min(uy0, r.y);
		ux1 = Math.max(ux1, r.x + r.width);
		uy1 = Math.max(uy1, r.y + r.height);
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

	/** @return damaged pixels summed over paints since the last reset */
	public long damagePx() {
		return damagePx;
	}

	/** @return bounds of all damage since the last reset, in root coordinates, or null */
	public Rectangle damageUnion() {
		return ux1 < ux0 ? null : new Rectangle(ux0, uy0, ux1 - ux0, uy1 - uy0);
	}

	public void reset() {
		java.util.Arrays.fill(c, 0);
		resetDamage();
	}

	/** Clears only the damage accumulators (rects, full repaints, pixels, union). */
	public void resetDamage() {
		c[DAMAGE_RECTS] = 0;
		c[FULL_REPAINTS] = 0;
		damagePx = 0;
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
