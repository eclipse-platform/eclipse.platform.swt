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
 * Fence rule: {@link Target#present} and {@link Target#swap} complete the GPU
 * work of the frame (flush and submit with CPU sync) before the CPU reads the
 * DIB or GDI touches the window; tiles are never rewritten while a previous
 * frame that samples them is in flight because frames are synchronous.
 * </p>
 */
public interface PlaneBackend {
	/** Class implementing the Skija backend; present only when Skija is on the class path. */
	String SKIJA = "org.eclipse.swt.internal.gpu.skija.SkijaPlaneBackend";

	/** Per-widget target owning tile textures and a frame surface. */
	interface Target {
		/** Ensures a frame surface of the client size; false on failure. */
		boolean beginFrame (int clientW, int clientH);
		/** Copies a CPU-rasterized rectangle (32-bit BGRX rows at {@code bits}) into tile {@code slot}. */
		boolean uploadTile (int slot, long bits, int stride, int srcX, int srcY, int dstX, int dstY, int w, int h);
		/** Rasterizes {@code ops} on the GPU into tile {@code slot}; ops are in client space, the tile's client origin is given. */
		boolean rasterTile (int slot, int tileClientX, int tileClientY, int localX, int localY, int w, int h, int background, OpStream ops);
		void tileEvicted (int slot);
		/** Draws tile {@code slot} into the frame with its top-left at client {@code (x, y)}. */
		void composite (int slot, int x, int y);
		/** Replays overlay {@code ops} into the frame clipped to the rectangle. */
		boolean replay (OpStream ops, int clipX, int clipY, int clipW, int clipH);
		/** Completes the frame and reads the rectangle back into a top-down 32-bit DIB. */
		boolean present (long bits, int stride, int x, int y, int w, int h);
		/** Completes the frame into the window's own back buffer and swaps; false if unsupported. */
		boolean swap (long hwnd);
		long gpuBytes ();
		void dispose ();
	}

	/** Prepares the shared GPU context; false means the GDI path must be used. */
	boolean init ();

	Target createTarget (int tileCapacity);

	/** Defines font {@code id} (GDI LOGFONT data) for text replay. */
	void defineFont (int id, String face, int heightPx, int weight, int italic);

	boolean isFontDefined (int id);

	/** Blob, font and paint cache counters of the backend, added into {@link GpuCounters}. */
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
