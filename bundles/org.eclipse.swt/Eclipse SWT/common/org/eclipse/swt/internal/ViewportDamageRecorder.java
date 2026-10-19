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
package org.eclipse.swt.internal;

import java.util.Arrays;

/**
 * Records what each paint pass damaged, one frame at a time, and keeps the numbers a
 * viewport test asserts on.
 *
 * <p>A frame is the damage between two {@link #endFrame()} calls. Damage can come from a
 * GC hook, from the native update region, or from the rectangle list of a remote
 * framebuffer update (VNC/RFB), which is an independent observer of the same paint: the
 * server reports exactly the area that changed. The recorder is the sink for all three.</p>
 *
 * <p>Two areas are kept per frame. <em>Painted</em> pixels were drawn (RFB Raw, a GC draw);
 * <em>moved</em> pixels were copied (RFB CopyRect, a blit scroll or tile translate) and cost
 * a block move, not a paint. The frame's damage is their union. A scroll by a few rows
 * therefore damages the whole viewport but paints only the exposed strip, and a full
 * repaint is judged on the painted area.</p>
 *
 * <p>Per frame it stores damaged, painted and visible painted pixels, the rectangle
 * count, the CopyRect count and the full-repaint flag. Per-frame lanes are primitive
 * arrays that double up to {@value #MAX_FRAMES} frames; later frames still count in the
 * totals. {@link #endFrame(ViewportDamageRegion)} checks the frame against the area the
 * test expected and counts a violation when damage falls outside it.</p>
 *
 * @noreference This class is not intended to be referenced by clients.
 */
public final class ViewportDamageRecorder {
	/** Largest number of frames with stored per-frame numbers. */
	public static final int MAX_FRAMES = 1 << 16;

	private final ViewportDamageRegion all = new ViewportDamageRegion ();
	private final ViewportDamageRegion painted = new ViewportDamageRegion ();
	private final ViewportDamageRegion protectedArea = new ViewportDamageRegion ();
	private long [] protectedHits = new long [64];
	private long totalProtectedHits;
	private long protectedHitFrames;
	private int viewportWidth;
	private int viewportHeight;
	private int fullPercent = 95;
	private int frames;
	private long droppedFrames;
	private long [] damagePixels = new long [64];
	private long [] paintedPixels = new long [64];
	private long [] visiblePainted = new long [64];
	private int [] rectCounts = new int [64];
	private int [] copyCounts = new int [64];
	private long [] fullBits = new long [1];
	private long totalDamage;
	private long totalPainted;
	private long maxPainted;
	private long fullRepaints;
	private long violations;
	private long frameCount;

	public ViewportDamageRecorder (int viewportWidth, int viewportHeight) {
		setViewport (viewportWidth, viewportHeight);
	}

	public void setViewport (int width, int height) {
		if (width < 0 || height < 0) throw new IllegalArgumentException ("negative viewport");
		viewportWidth = width;
		viewportHeight = height;
	}

	/** Painted visible area of at least this percent of the viewport counts as a full repaint (default 95). */
	public void setFullRepaintPercent (int percent) {
		if (percent < 1 || percent > 100) throw new IllegalArgumentException ("percent out of range");
		fullPercent = percent;
	}

	/** Pixels that were painted (GC draw, update region, RFB Raw). */
	public void paint (int x, int y, int width, int height) {
		painted.add (x, y, width, height);
		all.add (x, y, width, height);
	}

	/** A block move: destination damaged but not painted, source untouched (RFB CopyRect, blit scroll). */
	public void copy (int srcX, int srcY, int dstX, int dstY, int width, int height) {
		all.addCopy (srcX, srcY, dstX, dstY, width, height);
	}

	/** All damage of the in-progress frame (painted and moved), for assertions before {@link #endFrame()}. */
	public ViewportDamageRegion current () {
		return all;
	}

	/** Painted damage of the in-progress frame. */
	public ViewportDamageRegion currentPainted () {
		return painted;
	}

	/**
	 * The protected area: border, margin and scrollbar rectangles that damage must never cover.
	 * Fill it once (and after a resize); damage that lands on it is counted as a protected hit and
	 * does not count towards a full repaint.
	 */
	public ViewportDamageRegion protectedArea () {
		return protectedArea;
	}

	public void endFrame () {
		long damaged = all.area ();
		long paintedArea = painted.area ();
		long inside = painted.areaWithin (0, 0, viewportWidth, viewportHeight);
		long hits = protectedArea.isEmpty () ? 0 : all.intersectionArea (protectedArea);
		long paintedHits = protectedArea.isEmpty () ? 0 : painted.intersectionArea (protectedArea);
		boolean full = isFull (inside, paintedHits);
		if (frames < MAX_FRAMES) {
			store (damaged, paintedArea, inside, full, hits);
		} else {
			droppedFrames++;
		}
		totalProtectedHits += hits;
		if (hits > 0) protectedHitFrames++;
		totalDamage += damaged;
		totalPainted += paintedArea;
		maxPainted = Math.max (maxPainted, paintedArea);
		if (full) fullRepaints++;
		frameCount++;
		all.clear ();
		painted.clear ();
	}

	/**
	 * Ends the frame and checks it: returns true when all damage, painted and moved, lies inside
	 * {@code expected}. A false result is counted in {@link #violations()}.
	 */
	public boolean endFrame (ViewportDamageRegion expected) {
		boolean ok = all.isSubsetOf (expected);
		if (!ok) violations++;
		endFrame ();
		return ok;
	}

	/** Full repaint: the painted unprotected pixels reach fullPercent of the unprotected viewport. */
	private boolean isFull (long inside, long paintedProtected) {
		long area = (long) viewportWidth * viewportHeight - protectedArea.areaWithin (0, 0, viewportWidth, viewportHeight);
		long clean = Math.max (0, inside - paintedProtected);
		return area > 0 && clean * 100 >= area * fullPercent;
	}

	private void store (long damaged, long paintedArea, long inside, boolean full, long hits) {
		if (frames == damagePixels.length) {
			int size = frames * 2;
			protectedHits = Arrays.copyOf (protectedHits, size);
			damagePixels = Arrays.copyOf (damagePixels, size);
			paintedPixels = Arrays.copyOf (paintedPixels, size);
			visiblePainted = Arrays.copyOf (visiblePainted, size);
			rectCounts = Arrays.copyOf (rectCounts, size);
			copyCounts = Arrays.copyOf (copyCounts, size);
			fullBits = Arrays.copyOf (fullBits, (size + 63) >>> 6);
		}
		protectedHits [frames] = hits;
		damagePixels [frames] = damaged;
		paintedPixels [frames] = paintedArea;
		visiblePainted [frames] = inside;
		rectCounts [frames] = all.rectCount ();
		copyCounts [frames] = (int) Math.min (all.copyCount (), Integer.MAX_VALUE);
		if (full) fullBits [frames >>> 6] |= 1L << (frames & 63);
		frames++;
	}

	/** Frames ended so far, including those beyond the per-frame storage cap. */
	public long frameCount () {
		return frameCount;
	}

	public long droppedFrames () {
		return droppedFrames;
	}

	/** Damaged pixels of a stored frame, painted or moved (matrix column damage_px_per_frame). */
	public long damagePx (int frame) {
		checkFrame (frame);
		return damagePixels [frame];
	}

	/** Painted pixels of a stored frame: the repaint cost. */
	public long paintedPx (int frame) {
		checkFrame (frame);
		return paintedPixels [frame];
	}

	/** Painted pixels of a stored frame that lie inside the viewport (the rest is overscan). */
	public long visiblePaintedPx (int frame) {
		checkFrame (frame);
		return visiblePainted [frame];
	}

	/** Rectangles of a stored frame's damage (matrix column damage_rects_per_frame). */
	public int damageRects (int frame) {
		checkFrame (frame);
		return rectCounts [frame];
	}

	/** CopyRect-style moves in a stored frame. */
	public int copyRects (int frame) {
		checkFrame (frame);
		return copyCounts [frame];
	}

	/** Damaged pixels of a stored frame that fell on the protected area (border, margin, scrollbar). */
	public long protectedHitPx (int frame) {
		checkFrame (frame);
		return protectedHits [frame];
	}

	/** Frames in which damage touched the protected area. */
	public long protectedHitFrames () {
		return protectedHitFrames;
	}

	public long totalProtectedHitPx () {
		return totalProtectedHits;
	}

	public boolean isFullRepaint (int frame) {
		checkFrame (frame);
		return (fullBits [frame >>> 6] & (1L << (frame & 63))) != 0;
	}

	/** Frames that repainted (almost) the whole viewport (matrix column full_repaints). */
	public long fullRepaints () {
		return fullRepaints;
	}

	public long totalDamagePx () {
		return totalDamage;
	}

	public long totalPaintedPx () {
		return totalPainted;
	}

	public long maxPaintedPx () {
		return maxPainted;
	}

	/** Frames whose damage fell outside the expected area. */
	public long violations () {
		return violations;
	}

	public void reset () {
		all.clear ();
		painted.clear ();
		frames = 0;
		droppedFrames = 0;
		frameCount = 0;
		totalDamage = totalPainted = maxPainted = fullRepaints = violations = 0;
		totalProtectedHits = 0;
		protectedHitFrames = 0;
		Arrays.fill (fullBits, 0L);
	}

	private void checkFrame (int frame) {
		if (frame < 0 || frame >= frames) throw new IndexOutOfBoundsException ("frame " + frame + " of " + frames);
	}
}
