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
package org.eclipse.swt.internal.render;

import org.eclipse.swt.internal.win32.*;

/**
 * The retained frame of a drawn plane on win32: a top-down 32-bit DIB section selected into a memory DC, the
 * size of the plane's client area. It is recreated only when the size grows past it, so a frame allocates
 * nothing. Its pixels (<code>bits</code>, BGRA, <code>stride = width * 4</code>) are what the Skija backend wraps.
 */
public final class BackBuffer {
	/** The memory DC with the DIB selected, or 0 before the first {@link #ensure}. */
	public long dc;
	/** The DIB section and its pixels. */
	public long bitmap, bits;
	/** Allocated size in pixels. */
	public int width, height;
	private long oldBitmap;

	/** Makes the buffer at least <code>width x height</code>; answers true when it was (re)created. */
	public boolean ensure (long referenceDC, int width, int height) {
		width = Math.max (1, width);
		height = Math.max (1, height);
		if (dc != 0 && width <= this.width && height <= this.height) return false;
		release ();
		BITMAPINFOHEADER header = new BITMAPINFOHEADER ();
		header.biSize = BITMAPINFOHEADER.sizeof;
		header.biWidth = width;
		header.biHeight = -height;
		header.biPlanes = 1;
		header.biBitCount = 32;
		header.biCompression = OS.BI_RGB;
		byte [] info = new byte [BITMAPINFOHEADER.sizeof];
		OS.MoveMemory (info, header, BITMAPINFOHEADER.sizeof);
		long [] pixels = new long [1];
		dc = OS.CreateCompatibleDC (referenceDC);
		bitmap = OS.CreateDIBSection (dc, info, OS.DIB_RGB_COLORS, pixels, 0, 0);
		bits = pixels [0];
		oldBitmap = OS.SelectObject (dc, bitmap);
		this.width = width;
		this.height = height;
		return true;
	}

	/** Copies a rectangle of the frame to a device context (same coordinates on both sides). */
	public void present (long targetDC, int x, int y, int width, int height) {
		OS.BitBlt (targetDC, x, y, width, height, dc, x, y, OS.SRCCOPY);
	}

	/** Frees the DC and the DIB. */
	public void release () {
		if (dc != 0) {
			OS.SelectObject (dc, oldBitmap);
			OS.DeleteObject (bitmap);
			OS.DeleteDC (dc);
		}
		dc = bitmap = bits = oldBitmap = 0;
		width = height = 0;
	}
}
