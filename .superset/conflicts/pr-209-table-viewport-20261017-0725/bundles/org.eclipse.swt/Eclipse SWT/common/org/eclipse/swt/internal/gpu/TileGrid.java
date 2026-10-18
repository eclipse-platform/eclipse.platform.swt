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

/**
 * Retained content-plane tiles of one widget, in content space.
 * <p>
 * Content space is client space plus the accumulated scroll origin, so a scroll
 * is {@link #translate(int, int)}: an origin change, not a raster. Tiles are
 * {@code SIZE x SIZE} pixels addressed by {@code (tx, ty) = (x >> SHIFT, y >> SHIFT)}
 * and stored in dense slots of a {@link RetainedSlots} table, so the slot index
 * doubles as the tile's position in a CPU atlas or GPU texture pool.
 * </p>
 * <p>
 * Per slot the grid keeps one valid rectangle (tile local, half open) and four
 * {@code long[]} bit sets: resident, dirty, fully valid and visible. Widgets
 * only paint inside their client area, so a tile is valid only where it was
 * rasterized while visible; scrolling exposes and rasterizes only the
 * remainder. Validity is conservative: a union that is not a rectangle keeps
 * the larger piece, damage keeps the largest remaining piece.
 * </p>
 * <p>UI-thread confined. All per-frame calls are allocation free.</p>
 */
public final class TileGrid implements RetainedSlots.Evictor {
	public static final int SHIFT = 8, SIZE = 1 << SHIFT;

	/** Receives evicted slots so the backend can drop texture contents. */
	public interface Listener {
		void tileEvicted (int slot);
	}

	final RetainedSlots slots;
	final int [] tx, ty, valid;
	final long [] resident, dirty, full, visible;
	int originX, originY, clientW, clientH;
	Listener listener;

	public TileGrid (int capacity) {
		slots = new RetainedSlots (capacity);
		tx = new int [capacity];
		ty = new int [capacity];
		valid = new int [capacity * 4];
		int words = (capacity + 63) >>> 6;
		resident = new long [words];
		dirty = new long [words];
		full = new long [words];
		visible = new long [words];
	}

	public void setListener (Listener listener) {
		this.listener = listener;
	}

	public int capacity () {
		return slots.capacity ();
	}

	public RetainedSlots slots () {
		return slots;
	}

	/** Tiles needed to cover a client area of {@code w x h} at any scroll phase. */
	public static int tilesFor (int w, int h) {
		return (((w + SIZE - 1) >> SHIFT) + 1) * (((h + SIZE - 1) >> SHIFT) + 1);
	}

	public void setClient (int w, int h) {
		clientW = Math.max (0, w);
		clientH = Math.max (0, h);
	}

	public int clientWidth () { return clientW; }
	public int clientHeight () { return clientH; }
	public int originX () { return originX; }
	public int originY () { return originY; }

	/** Content under the client moved by {@code (dx, dy)} client pixels. */
	public void translate (int dx, int dy) {
		originX -= dx;
		originY -= dy;
	}

	static long tileKey (int x, int y) {
		return ((long) y << 32) | (x & 0xFFFFFFFFL);
	}

	static void set (long [] bits, int i) { bits [i >>> 6] |= 1L << i; }
	static void clear (long [] bits, int i) { bits [i >>> 6] &= ~(1L << i); }
	static boolean test (long [] bits, int i) { return (bits [i >>> 6] & (1L << i)) != 0; }

	public boolean isDirty (int slot) { return test (dirty, slot); }
	public boolean isFull (int slot) { return test (full, slot); }
	public boolean isResident (int slot) { return test (resident, slot); }
	public boolean isVisible (int slot) { return test (visible, slot); }

	/** Iterates set bits: returns the first set slot at or after {@code from}, or -1. */
	public static int nextSet (long [] bits, int from) {
		int w = from >>> 6;
		if (w >= bits.length) return -1;
		long word = bits [w] & (-1L << from);
		while (true) {
			if (word != 0) return (w << 6) + Long.numberOfTrailingZeros (word);
			if (++w == bits.length) return -1;
			word = bits [w];
		}
	}

	public long [] visibleBits () { return visible; }
	public long [] dirtyBits () { return dirty; }
	public long [] residentBits () { return resident; }

	/** Damage in client coordinates: shrinks validity of resident tiles and marks them dirty. */
	public void damage (int cx, int cy, int cw, int ch) {
		if (cw <= 0 || ch <= 0) return;
		int x0 = cx + originX, y0 = cy + originY, x1 = x0 + cw, y1 = y0 + ch;
		int tx0 = x0 >> SHIFT, ty0 = y0 >> SHIFT, tx1 = (x1 - 1) >> SHIFT, ty1 = (y1 - 1) >> SHIFT;
		if ((long) (tx1 - tx0 + 1) * (ty1 - ty0 + 1) > slots.size ()) {
			damageResident (x0, y0, x1, y1);
			return;
		}
		for (int y = ty0; y <= ty1; y++) {
			for (int x = tx0; x <= tx1; x++) {
				int slot = slots.peek (tileKey (x, y));
				if (slot >= 0) damageSlot (slot, x0, y0, x1, y1);
			}
		}
	}

	private void damageResident (int x0, int y0, int x1, int y1) {
		for (int slot = nextSet (resident, 0); slot >= 0; slot = nextSet (resident, slot + 1)) {
			int bx = tx [slot] << SHIFT, by = ty [slot] << SHIFT;
			if (bx < x1 && x0 < bx + SIZE && by < y1 && y0 < by + SIZE) damageSlot (slot, x0, y0, x1, y1);
		}
	}

	private void damageSlot (int slot, int x0, int y0, int x1, int y1) {
		int bx = tx [slot] << SHIFT, by = ty [slot] << SHIFT;
		Rects.subtractKeepLargest (valid, slot * 4, x0 - bx, y0 - by, x1 - bx, y1 - by);
		clear (full, slot);
		set (dirty, slot);
	}

	/**
	 * Drops validity of resident tiles outside the current client area. Called
	 * after content damage: immediate-mode widgets report damage only for what
	 * is visible, so off-screen retention survives scrolling but not edits.
	 */
	public void damageOffscreen () {
		int cx0 = originX, cy0 = originY, cx1 = originX + clientW, cy1 = originY + clientH;
		for (int slot = nextSet (resident, 0); slot >= 0; slot = nextSet (resident, slot + 1)) {
			int bx = tx [slot] << SHIFT, by = ty [slot] << SHIFT;
			if (bx < cx1 && cx0 < bx + SIZE && by < cy1 && cy0 < by + SIZE) continue;
			Rects.empty (valid, slot * 4);
			clear (full, slot);
			set (dirty, slot);
		}
	}

	/** Damages every resident tile (font change, resize without NO_REDRAW_RESIZE). */
	public void damageAll () {
		for (int slot = nextSet (resident, 0); slot >= 0; slot = nextSet (resident, slot + 1)) {
			Rects.empty (valid, slot * 4);
			clear (full, slot);
			set (dirty, slot);
		}
	}

	/** Starts a frame: unpins the previous visible set. */
	public void beginFrame () {
		for (int slot = nextSet (visible, 0); slot >= 0; slot = nextSet (visible, slot + 1)) slots.unpin (slot);
		java.util.Arrays.fill (visible, 0L);
	}

	public int visibleTx0 () { return originX >> SHIFT; }
	public int visibleTy0 () { return originY >> SHIFT; }
	public int visibleTx1 () { return (originX + Math.max (1, clientW) - 1) >> SHIFT; }
	public int visibleTy1 () { return (originY + Math.max (1, clientH) - 1) >> SHIFT; }

	/** Acquires and pins the slot of visible tile {@code (x, y)}; -1 when the pool is exhausted. */
	public int acquireVisible (int x, int y) {
		int slot = slots.acquire (tileKey (x, y), this);
		if (slot < 0) return -1;
		if (slots.isNew ()) {
			tx [slot] = x;
			ty [slot] = y;
			Rects.empty (valid, slot * 4);
			set (resident, slot);
			clear (full, slot);
			set (dirty, slot);
		} else {
			GpuCounters.inc (GpuCounters.TILE_REUSED);
		}
		if (!test (visible, slot)) {
			slots.pin (slot);
			set (visible, slot);
		}
		return slot;
	}

	@Override
	public void evict (int slot, long key) {
		clear (resident, slot);
		clear (dirty, slot);
		clear (full, slot);
		clear (visible, slot);
		Rects.empty (valid, slot * 4);
		GpuCounters.inc (GpuCounters.TILE_EVICT);
		if (listener != null) listener.tileEvicted (slot);
	}

	/** Client x of the tile's left edge. */
	public int clientX (int slot) { return (tx [slot] << SHIFT) - originX; }
	public int clientY (int slot) { return (ty [slot] << SHIFT) - originY; }
	public int tileX (int slot) { return tx [slot]; }
	public int tileY (int slot) { return ty [slot]; }

	/**
	 * Computes the client-space rectangle of {@code slot} that must be rasterized
	 * this frame (visible but not valid) into {@code out[0..3]} as x, y, w, h.
	 * Returns false when the visible part is already valid.
	 */
	public boolean rasterRect (int slot, int [] out) {
		int bx = tx [slot] << SHIFT, by = ty [slot] << SHIFT;
		int vx0 = Math.max (0, originX - bx), vy0 = Math.max (0, originY - by);
		int vx1 = Math.min (SIZE, originX + clientW - bx), vy1 = Math.min (SIZE, originY + clientH - by);
		if (vx0 >= vx1 || vy0 >= vy1) return false;
		if (!Rects.differenceBounds (vx0, vy0, vx1, vy1, valid, slot * 4, out)) {
			clear (dirty, slot);
			return false;
		}
		out [0] += bx - originX;
		out [1] += by - originY;
		return true;
	}

	/** Records that client rectangle {@code (x, y, w, h)} of {@code slot} now holds current pixels. */
	public void rastered (int slot, int x, int y, int w, int h) {
		int bx = tx [slot] << SHIFT, by = ty [slot] << SHIFT;
		int lx = x + originX - bx, ly = y + originY - by;
		Rects.unionOrLargest (valid, slot * 4, lx, ly, lx + w, ly + h);
		clear (dirty, slot);
		int o = slot * 4;
		if (valid [o] <= 0 && valid [o + 1] <= 0 && valid [o + 2] >= SIZE && valid [o + 3] >= SIZE) set (full, slot);
		GpuCounters.inc (GpuCounters.TILE_RASTER);
	}

	/** Slot of tile {@code (x, y)} admitting it when absent (not pinned); -1 when the pool is exhausted. */
	public int acquire (int x, int y) {
		int slot = slots.acquire (tileKey (x, y), this);
		if (slot >= 0 && slots.isNew ()) {
			tx [slot] = x;
			ty [slot] = y;
			Rects.empty (valid, slot * 4);
			set (resident, slot);
			clear (full, slot);
			clear (dirty, slot);
		}
		return slot;
	}

	/** Slot of resident tile {@code (x, y)}, or -1; does not touch recency. */
	public int peekSlot (int x, int y) {
		return slots.peek (tileKey (x, y));
	}

	/** True when the tile's valid rectangle covers the part of client rectangle {@code (x, y, w, h)} inside the tile. */
	public boolean covers (int slot, int x, int y, int w, int h) {
		int bx = tx [slot] << SHIFT, by = ty [slot] << SHIFT;
		int x0 = Math.max (x + originX, bx) - bx, y0 = Math.max (y + originY, by) - by;
		int x1 = Math.min (x + w + originX, bx + SIZE) - bx, y1 = Math.min (y + h + originY, by + SIZE) - by;
		if (x0 >= x1 || y0 >= y1) return true;
		int o = slot * 4;
		return valid [o] <= x0 && valid [o + 1] <= y0 && valid [o + 2] >= x1 && valid [o + 3] >= y1;
	}

	/** Drops every tile (backend lost, client grew past capacity). */
	public void invalidateAll () {
		for (int slot = nextSet (resident, 0); slot >= 0; slot = nextSet (resident, slot + 1)) {
			slots.unpin (slot);
		}
		slots.readjust (0, this);
		slots.readjust (slots.capacity (), this);
		java.util.Arrays.fill (visible, 0L);
	}
}
