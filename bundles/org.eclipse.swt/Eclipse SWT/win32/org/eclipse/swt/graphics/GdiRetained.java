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
package org.eclipse.swt.graphics;

import org.eclipse.swt.internal.gdip.*;
import org.eclipse.swt.internal.gpu.*;
import org.eclipse.swt.internal.win32.*;

/**
 * Device-wide retained GDI/GDI+ paint objects keyed by packed ints.
 * <p>
 * Stock SWT creates and deletes an HPEN on every foreground/line-state change
 * and an HBRUSH on every background change of every GC. With
 * {@code -Dswt.gpu} set, a GC instead pins a shared object found by its key:
 * pens by {@code (PS_ style | width << 16, COLORREF)}, brushes by COLORREF,
 * GDI+ solid brushes by ARGB. A pinned object is selected in a live DC and is
 * never evicted or deleted; unpinned objects are evicted least recent first
 * when a table reaches its bound, and all are deleted when the device is
 * released.
 * </p>
 * <p>Display-thread confined: GCs created on other threads keep the stock path.</p>
 */
final class GdiRetained {
	static final int PENS = 0, BRUSHES = 1, GDIP_BRUSHES = 2, TABLES = 3;
	static final int CAPACITY = Integer.getInteger ("swt.gpu.gdiObjects", 256);

	final RetainedSlots [] tables = new RetainedSlots [TABLES];
	final long [][] handles = new long [TABLES][];
	private final RetainedSlots.Evictor [] evictors = new RetainedSlots.Evictor [TABLES];

	GdiRetained () {
		for (int t = 0; t < TABLES; t++) {
			tables [t] = new RetainedSlots (CAPACITY);
			handles [t] = new long [CAPACITY];
			int table = t;
			evictors [t] = (slot, key) -> deleteHandle (table, slot);
		}
	}

	static long penKey (int style, int width, int colorref) {
		return PaintKeys.key ((style & 0xFFFF) | (width & 0x7FFF) << 16, colorref);
	}

	/** Pins and returns the slot of a GDI pen, creating it on a miss; -1 means use the stock path. */
	int acquirePen (int style, int width, int colorref) {
		RetainedSlots slots = tables [PENS];
		int slot = slots.acquire (penKey (style, width, colorref), evictors [PENS]);
		if (slot < 0) return -1;
		if (slots.isNew ()) {
			handles [PENS][slot] = createPen (style, width, colorref);
			GpuCounters.inc (GpuCounters.PEN_MISS);
			GpuCounters.inc (GpuCounters.NATIVE_CREATES);
		} else {
			GpuCounters.inc (GpuCounters.PEN_HIT);
		}
		slots.pin (slot);
		return slot;
	}

	static long createPen (int style, int width, int colorref) {
		int lineStyle = style & OS.PS_STYLE_MASK;
		if ((width == 0 && lineStyle != OS.PS_USERSTYLE) || style == 0) {
			return OS.CreatePen (lineStyle, width, colorref);
		}
		LOGBRUSH logBrush = new LOGBRUSH ();
		logBrush.lbStyle = OS.BS_SOLID;
		logBrush.lbColor = colorref;
		return OS.ExtCreatePen (style | OS.PS_GEOMETRIC, Math.max (1, width), logBrush, 0, null);
	}

	/** Pins and returns the slot of a solid GDI brush; -1 means use the stock path. */
	int acquireBrush (int colorref) {
		RetainedSlots slots = tables [BRUSHES];
		int slot = slots.acquire (colorref & 0xFFFFFFFFL, evictors [BRUSHES]);
		if (slot < 0) return -1;
		if (slots.isNew ()) {
			handles [BRUSHES][slot] = OS.CreateSolidBrush (colorref);
			GpuCounters.inc (GpuCounters.BRUSH_MISS);
			GpuCounters.inc (GpuCounters.NATIVE_CREATES);
		} else {
			GpuCounters.inc (GpuCounters.BRUSH_HIT);
		}
		slots.pin (slot);
		return slot;
	}

	/** Pins and returns the slot of a GDI+ solid brush of colour {@code argb}; -1 means use the stock path. */
	int acquireGdipBrush (int argb) {
		RetainedSlots slots = tables [GDIP_BRUSHES];
		int slot = slots.acquire (argb & 0xFFFFFFFFL, evictors [GDIP_BRUSHES]);
		if (slot < 0) return -1;
		if (slots.isNew ()) {
			handles [GDIP_BRUSHES][slot] = Gdip.SolidBrush_new (argb);
			GpuCounters.inc (GpuCounters.GDIP_BRUSH_MISS);
			GpuCounters.inc (GpuCounters.NATIVE_CREATES);
		} else {
			GpuCounters.inc (GpuCounters.GDIP_BRUSH_HIT);
		}
		slots.pin (slot);
		return slot;
	}

	long handle (int table, int slot) {
		return handles [table][slot];
	}

	void release (int table, int slot) {
		if (slot >= 0) tables [table].unpin (slot);
	}

	private void deleteHandle (int table, int slot) {
		long handle = handles [table][slot];
		handles [table][slot] = 0;
		if (handle == 0) return;
		if (table == GDIP_BRUSHES) {
			Gdip.SolidBrush_delete (handle);
		} else {
			OS.DeleteObject (handle);
		}
	}

	/** Deletes every retained object; called from Device.release after all GCs are gone. */
	void dispose () {
		for (int t = 0; t < TABLES; t++) {
			RetainedSlots slots = tables [t];
			for (int slot = slots.first (); slot >= 0; slot = slots.next (slot)) deleteHandle (t, slot);
		}
	}
}
