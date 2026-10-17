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
 * The <code>custom</code> backend on win32: draws with the platform GC (the GDI device context of the plane's
 * back buffer; images through an SWT {@link GC}). Theme parts are the window's own uxtheme parts
 * ({@link ThemeParts}), so the native theme is drawn with the platform's pixels.
 */
public final class GcRowCanvas implements BufferCanvas {
	private final IntSupplier zoom;
	private final LongFunction<GC> gcFactory;
	private final ThemeParts parts;
	private long hdc;
	private GC gc;
	private int saved;
	private final RECT rect = new RECT ();
	private char [] chars = new char [64];
	private int [] polygon = new int [6];
	/* device copies of theme glyph images, made on first use */
	private final Map<ImageData, Image> glyphs = new WeakHashMap<> ();

	/**
	 * @param hwnd the plane's window (its theme parts are opened for it)
	 * @param itemThemeClass the uxtheme class of rows, <code>TREEVIEW</code> or <code>LISTVIEW</code> (NUL-terminated)
	 * @param zoom the widget's current native zoom
	 * @param gcFactory makes a GC on a device context the way the widget does for its own drawing
	 */
	public GcRowCanvas (long hwnd, char [] itemThemeClass, IntSupplier zoom, LongFunction<GC> gcFactory) {
		this.zoom = zoom;
		this.gcFactory = gcFactory;
		this.parts = new ThemeParts (hwnd, itemThemeClass, zoom);
	}

	@Override
	public void target (BackBuffer buffer) {
		this.hdc = buffer.dc;
	}

	@Override
	public void begin (int x, int y, int width, int height) {
		saved = OS.SaveDC (hdc);
		OS.IntersectClipRect (hdc, x, y, x + width, y + height);
		OS.SetBkMode (hdc, OS.TRANSPARENT);
	}

	@Override
	public void end () {
		if (gc != null) {
			gc.dispose ();
			gc = null;
		}
		OS.RestoreDC (hdc, saved);
	}

	@Override
	public void fill (int x, int y, int width, int height, int rgb) {
		if (width <= 0 || height <= 0) return;
		OS.SetBkColor (hdc, colorRef (rgb));
		OS.SetRect (rect, x, y, x + width, y + height);
		OS.ExtTextOut (hdc, 0, 0, OS.ETO_OPAQUE, rect, null, 0, null);
	}

	@Override
	public void text (String text, long font, int rgb, int x, int y, int width, int height, int align) {
		int length = text.length ();
		if (length == 0 || width <= 0) return;
		if (chars.length < length) chars = new char [Math.max (length, chars.length * 2)];
		text.getChars (0, length, chars, 0);
		long oldFont = font != -1 && font != 0 ? OS.SelectObject (hdc, font) : 0;
		OS.SetTextColor (hdc, colorRef (rgb));
		OS.SetRect (rect, x, y, x + width, y + height);
		int flags = OS.DT_SINGLELINE | OS.DT_VCENTER | OS.DT_NOPREFIX | (align == SWT.CENTER ? OS.DT_CENTER : align == SWT.RIGHT ? OS.DT_RIGHT : OS.DT_LEFT);
		OS.DrawText (hdc, chars, length, rect, flags);
		if (oldFont != 0) OS.SelectObject (hdc, oldFont);
	}

	@Override
	public void image (Image image, int x, int y, int width, int height) {
		if (image == null || image.isDisposed () || width <= 0 || height <= 0) return;
		if (gc == null) gc = gcFactory.apply (hdc);
		int z = zoom.getAsInt ();
		gc.drawImage (image, DPIUtil.pixelToPoint (x, z), DPIUtil.pixelToPoint (y, z), DPIUtil.pixelToPoint (width, z), DPIUtil.pixelToPoint (height, z));
	}

	@Override
	public void imageData (ImageData data, int x, int y, int width, int height) {
		if (data == null || width <= 0 || height <= 0) return;
		if (gc == null) gc = gcFactory.apply (hdc);
		Image image = glyphs.get (data);
		if (image == null) {
			image = new Image (gc.getDevice (), data);
			glyphs.put (data, image);
		}
		image (image, x, y, width, height);
	}

	@Override
	public void polygon (int [] points, int count, int rgb) {
		if (count < 3) return;
		if (polygon.length < count * 2) polygon = new int [count * 2];
		System.arraycopy (points, 0, polygon, 0, count * 2);
		long oldBrush = OS.SelectObject (hdc, OS.GetStockObject (OS.DC_BRUSH));
		long oldPen = OS.SelectObject (hdc, OS.GetStockObject (OS.NULL_PEN));
		OS.SetDCBrushColor (hdc, colorRef (rgb));
		OS.Polygon (hdc, polygon, count);
		OS.SelectObject (hdc, oldPen);
		OS.SelectObject (hdc, oldBrush);
	}

	@Override
	public void part (int part, int state, int x, int y, int width, int height) {
		parts.draw (hdc, part, state, x, y, width, height);
	}

	@Override
	public void focus (int x, int y, int width, int height) {
		OS.SetRect (rect, x, y, x + width, y + height);
		OS.SetTextColor (hdc, 0);
		OS.SetBkColor (hdc, 0xFFFFFF);
		OS.DrawFocusRect (hdc, rect);
	}

	@Override
	public void dispose () {
		parts.close ();
		for (Image image : glyphs.values ()) image.dispose ();
		glyphs.clear ();
	}

	/** <code>0xRRGGBB</code> to a GDI <code>COLORREF</code>. */
	static int colorRef (int rgb) {
		return (rgb & 0xFF) << 16 | rgb & 0xFF00 | rgb >> 16 & 0xFF;
	}
}
