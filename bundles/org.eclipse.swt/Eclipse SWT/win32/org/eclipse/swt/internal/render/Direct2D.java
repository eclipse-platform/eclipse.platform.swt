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

import java.io.*;
import java.nio.file.*;

import org.eclipse.swt.internal.*;

/**
 * The Direct2D shim of the <code>gpu</code> drawn planes on win32 (library <code>swt-m3-d2d-win32</code>, source
 * <code>Eclipse SWT Render Direct2D/win32/library/swt_m3_d2d.cpp</code>). A frame is a packed int operation stream
 * replayed by one {@link #frame} call into a hardware Direct2D DC render target bound to the back buffer.
 * <p>
 * The library is found, in this order, at <code>-Dswt.render.direct2d.library=&lt;file&gt;</code>, on
 * <code>java.library.path</code>, or as the class path resource <code>/swt-m3-d2d-win32.dll</code> (copied once to a
 * temporary file). {@link #available ()} is false when none loads, and the plane falls back.
 * </p>
 */
public final class Direct2D {
	/** Operation codes and their operands (after the code). */
	public static final int OP_FILL = 1 /* x, y, w, h, rgb */, OP_TEXT = 2 /* x, y, w, h, rgb, font, align, offset, length */,
		OP_BITMAP = 3 /* x, y, w, h, bitmap */, OP_POLYGON = 4 /* rgb, count, count x/y pairs */, OP_FOCUS = 5 /* x, y, w, h */;
	/** Kinds of {@link #free}. */
	public static final int KIND_FONT = 0, KIND_BITMAP = 1;
	static final String LIBRARY = "swt-m3-d2d-win32";
	private static final SwtLog LOG = SwtLog.getLog ("org.eclipse.swt.internal.render.Direct2D");
	private static final boolean LOADED = load ();

	private Direct2D () {
	}

	/** True when the shim is loaded. */
	public static boolean available () {
		return LOADED;
	}

	private static boolean load () {
		try {
			String file = System.getProperty ("swt.render.direct2d.library");
			if (file != null) {
				System.load (file);
				return true;
			}
			try {
				System.loadLibrary (LIBRARY);
				return true;
			} catch (UnsatisfiedLinkError e) {
				return loadResource ();
			}
		} catch (UnsatisfiedLinkError | IOException | SecurityException e) {
			LOG.info (() -> "Direct2D shim not loaded: " + e);
			return false;
		}
	}

	private static boolean loadResource () throws IOException {
		try (InputStream in = Direct2D.class.getResourceAsStream ("/" + LIBRARY + ".dll")) {
			if (in == null) {
				LOG.info (() -> "Direct2D shim not found (" + LIBRARY + ".dll)");
				return false;
			}
			Path directory = Files.createTempDirectory ("swt-m3-d2d");
			Path dll = directory.resolve (LIBRARY + ".dll");
			Files.copy (in, dll);
			dll.toFile ().deleteOnExit ();
			directory.toFile ().deleteOnExit ();
			System.load (dll.toString ());
			return true;
		}
	}

	/** A context (factories and a render target, hardware when <code>hardware</code> and available), or 0. */
	static native long create (int hardware);

	/** A DirectWrite text format for a face, size in pixels, weight and italic; answers its id, or -1. */
	static native int font (long context, char [] face, float size, int weight, int italic);

	/** Keeps premultiplied BGRA pixels as a bitmap; answers its id. */
	static native int bitmap (long context, byte [] bgra, int width, int height);

	/** Frees a font or a bitmap id. */
	static native void free (long context, int kind, int id);

	/** Replays a frame into the rectangle of a device context; answers the HRESULT. */
	static native int frame (long context, long hdc, int x, int y, int width, int height, int [] ops, int length, char [] text);

	/** 1 when the render target is hardware. */
	static native int hardware (long context);

	/** Frees a context. */
	static native void dispose (long context);
}
