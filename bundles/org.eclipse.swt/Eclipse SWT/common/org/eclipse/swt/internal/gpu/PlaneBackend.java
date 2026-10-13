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
 * GPU backend seam for retained planes. Implementations live outside the SWT
 * bundle's compiled sources (the Skija implementation needs Skija on the class
 * path) and are loaded reflectively by {@link #load(String)}; any failure keeps
 * the GDI path. Every method takes ints, longs and the reusable
 * {@link OpStream}; native objects are created only when a draw needs them,
 * cached by int key and released by the implementation's lifetime owner.
 * <p>
 * Threading: all calls happen on the display thread that owns the GL context.
 * Fence rule: {@link Target#present} completes the GPU work of the frame
 * (flush, then a synchronous readback) before the CPU reads the DIB or GDI
 * touches the window; tiles are never rewritten while a previous frame that
 * samples them is in flight because frames are synchronous.
 * </p>
 */
public interface PlaneBackend {
	/** Class implementing the Skija backend; present only when Skija is on the class path. */
	String SKIJA = "org.eclipse.swt.internal.gpu.skija.SkijaPlaneBackend";

	/** Per-widget target owning tile textures and a frame surface. */
	interface Target {
		/** Ensures the content and present surfaces of the client size: 0 failure, 1 ready, 2 ready but content was reallocated. */
		int beginFrame (int clientW, int clientH);
		/** Replays content {@code ops} (client space) once into the content surface, clipped to the rectangle; {@code background} with alpha 0 means no fill. */
		boolean rasterFrame (int x, int y, int w, int h, int background, OpStream ops);
		/** Copies a CPU-rasterized client rectangle (32-bit BGRX rows at {@code bits}, client-sized stride) into the content surface. */
		boolean uploadFrame (long bits, int stride, int x, int y, int w, int h);
		/** RFB CopyRect on the content surface: the block at {@code (srcX, srcY)} moves to {@code (dstX, dstY)}. */
		boolean copyFrame (int srcX, int srcY, int dstX, int dstY, int w, int h);
		/** Copies the content surface at the tile's local {@code (localX, localY, w, h)} into tile {@code slot}. */
		boolean copyFrameToTile (int slot, int tileClientX, int tileClientY, int localX, int localY, int w, int h);
		/** Refills the content surface from tile {@code slot} (a cache hit instead of a raster). */
		boolean copyTileToFrame (int slot, int tileClientX, int tileClientY, int localX, int localY, int w, int h);
		void tileEvicted (int slot);
		/** Present surface = content (rectangle) + overlay {@code ops}; {@code ops} null copies content only. */
		boolean overlay (OpStream ops, int x, int y, int w, int h);
		/** Completes the frame and reads the rectangle (of the present surface when {@code fromPresent} != 0, else of content) back into a top-down 32-bit DIB. */
		boolean present (long bits, int stride, int x, int y, int w, int h, int fromPresent);
		long gpuBytes ();
		void dispose ();
	}

	/** Prepares the shared GPU context; false means the GDI path must be used. */
	boolean init ();

	Target createTarget (int tileCapacity);

	/** One-line description of the backend and its cache sizes, for logs and tests. */
	String describe ();

	void dispose ();

	static PlaneBackend load (String className) {
		try {
			Class<?> type = Class.forName (className, true, PlaneBackend.class.getClassLoader ());
			return (PlaneBackend) type.getDeclaredConstructor ().newInstance ();
		} catch (Throwable notAvailable) {
			GpuCounters.inc (GpuCounters.FALLBACKS);
			return null;
		}
	}
}
