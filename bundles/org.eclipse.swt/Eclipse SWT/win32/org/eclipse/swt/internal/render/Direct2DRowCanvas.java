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

import java.util.*;
import java.util.function.*;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.win32.*;

/**
 * The <code>gpu</code> backend on win32: a frame is recorded as packed ints and replayed by the {@link Direct2D}
 * shim in one native call into a hardware Direct2D DC render target bound to the damage of the back buffer.
 * Text is DirectWrite (one text format per native font), images and theme parts are bitmaps kept by the shim
 * (uploaded once, bounded LRU for parts).
 */
public final class Direct2DRowCanvas implements BufferCanvas {
	private static final SwtLog LOG = SwtLog.getLog ("org.eclipse.swt.internal.render.Direct2D");
	private static final int FONTS = 64, PART_IMAGES = 256;
	private final IntSupplier zoom;
	private final ThemeParts parts;
	private long context, hdc;
	private int [] ops = new int [1024];
	private int size;
	private char [] text = new char [1024];
	private int textSize;
	private int frameX, frameY, frameWidth, frameHeight;
	private boolean reported;
	private final int [] fontAttributes = new int [3];
	private final Map<Long, Integer> fonts;
	private final Map<Image, Integer> images = new WeakHashMap<> ();
	private final Map<Long, Integer> partImages;

	/** A Direct2D backend, or null when the shim or a device is not available. */
	public static Direct2DRowCanvas create (long hwnd, char [] itemThemeClass, IntSupplier zoom) {
		if (!Direct2D.available ()) return null;
		long context = Direct2D.create (1);
		if (context == 0) return null;
		return new Direct2DRowCanvas (context, hwnd, itemThemeClass, zoom);
	}

	private Direct2DRowCanvas (long context, long hwnd, char [] itemThemeClass, IntSupplier zoom) {
		this.context = context;
		this.zoom = zoom;
		this.parts = new ThemeParts (hwnd, itemThemeClass, zoom);
		this.fonts = lru (FONTS, Direct2D.KIND_FONT);
		this.partImages = lru (PART_IMAGES, Direct2D.KIND_BITMAP);
	}

	private <K> Map<K, Integer> lru (int capacity, int kind) {
		return new LinkedHashMap<> (16, 0.75f, true) {
			private static final long serialVersionUID = 1L;

			@Override
			protected boolean removeEldestEntry (Map.Entry<K, Integer> eldest) {
				if (size () <= capacity) return false;
				if (context != 0) Direct2D.free (context, kind, eldest.getValue ());
				return true;
			}
		};
	}

	@Override
	public void target (BackBuffer buffer) {
		hdc = buffer.dc;
	}

	@Override
	public void begin (int x, int y, int width, int height) {
		frameX = x;
		frameY = y;
		frameWidth = width;
		frameHeight = height;
		size = textSize = 0;
	}

	@Override
	public void end () {
		if (size == 0 || context == 0) return;
		/* a GDI call that answers flushes the GDI batch of the behaviour pass before Direct2D reads the device context */
		OS.GetPixel (hdc, frameX, frameY);
		int result = Direct2D.frame (context, hdc, frameX, frameY, frameWidth, frameHeight, ops, size, text);
		if (result < 0 && !reported) {
			reported = true;
			LOG.info (() -> "Direct2D frame failed: HRESULT 0x" + Integer.toHexString (result));
		}
	}

	private void ensure (int count) {
		if (size + count > ops.length) ops = Arrays.copyOf (ops, Math.max (ops.length * 2, size + count));
	}

	@Override
	public void fill (int x, int y, int width, int height, int rgb) {
		if (width <= 0 || height <= 0) return;
		ensure (6);
		ops [size++] = Direct2D.OP_FILL;
		ops [size++] = x;
		ops [size++] = y;
		ops [size++] = width;
		ops [size++] = height;
		ops [size++] = rgb;
	}

	@Override
	public void text (String string, long font, int rgb, int x, int y, int width, int height, int align) {
		int length = string.length ();
		if (length == 0 || width <= 0) return;
		int id = font (font);
		if (id < 0) return;
		if (textSize + length > text.length) text = Arrays.copyOf (text, Math.max (text.length * 2, textSize + length));
		string.getChars (0, length, text, textSize);
		ensure (10);
		ops [size++] = Direct2D.OP_TEXT;
		ops [size++] = x;
		ops [size++] = y;
		ops [size++] = width;
		ops [size++] = height;
		ops [size++] = rgb;
		ops [size++] = id;
		ops [size++] = align == SWT.CENTER ? 1 : align == SWT.RIGHT ? 2 : 0;
		ops [size++] = textSize;
		ops [size++] = length;
		textSize += length;
	}

	/* one DirectWrite text format per native font handle: face, pixel height, weight and italic of its LOGFONT */
	private int font (long hFont) {
		Integer id = fonts.get (hFont);
		if (id != null) return id;
		String face = RowImages.font (hFont, fontAttributes);
		int height = fontAttributes [0];
		float size = height < 0 ? -height : height * 0.8f;
		int created = Direct2D.font (context, face.toCharArray (), size, fontAttributes [1], fontAttributes [2]);
		if (created >= 0) fonts.put (hFont, created);
		return created;
	}

	@Override
	public void image (Image image, int x, int y, int width, int height) {
		if (image == null || image.isDisposed () || width <= 0 || height <= 0) return;
		Integer id = images.get (image);
		if (id == null) {
			ImageData data = image.getImageData (zoom.getAsInt ());
			id = Direct2D.bitmap (context, RowImages.bgra (data), data.width, data.height);
			images.put (image, id);
		}
		bitmap (id, x, y, width, height);
	}

	private void bitmap (int id, int x, int y, int width, int height) {
		ensure (6);
		ops [size++] = Direct2D.OP_BITMAP;
		ops [size++] = x;
		ops [size++] = y;
		ops [size++] = width;
		ops [size++] = height;
		ops [size++] = id;
	}

	@Override
	public void polygon (int [] points, int count, int rgb) {
		if (count < 3) return;
		ensure (3 + 2 * count);
		ops [size++] = Direct2D.OP_POLYGON;
		ops [size++] = rgb;
		ops [size++] = count;
		System.arraycopy (points, 0, ops, size, 2 * count);
		size += 2 * count;
	}

	@Override
	public void part (int part, int state, int x, int y, int width, int height) {
		if (width <= 0 || height <= 0) return;
		long key = (long) part << 56 | (long) (state & 0xFFFF) << 40 | (long) (width & 0xFFFFF) << 20 | height & 0xFFFFF;
		Integer id = partImages.get (key);
		if (id == null) {
			id = Direct2D.bitmap (context, parts.image (part, state, width, height), width, height);
			partImages.put (key, id);
		}
		bitmap (id, x, y, width, height);
	}

	@Override
	public void focus (int x, int y, int width, int height) {
		ensure (5);
		ops [size++] = Direct2D.OP_FOCUS;
		ops [size++] = x;
		ops [size++] = y;
		ops [size++] = width;
		ops [size++] = height;
	}

	@Override
	public void dispose () {
		parts.close ();
		if (context != 0) Direct2D.dispose (context);
		context = 0;
		fonts.clear ();
		images.clear ();
		partImages.clear ();
	}
}
