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

import java.util.function.*;

import org.eclipse.swt.internal.win32.*;

/**
 * The platform theme parts of the native look on win32, opened for one window: row selection
 * (<code>TVP_TREEITEM</code> or <code>LVP_LISTITEM</code>), expander glyphs (<code>TVP_GLYPH</code>,
 * <code>TVP_HOTGLYPH</code>) and check boxes (<code>BP_CHECKBOX</code>). The GC backend draws them straight
 * into its device context; the M3 backends (raster and gpu) draw {@link #image} copies (premultiplied BGRA), which
 * they cache.
 */
public final class ThemeParts {
	private static final int TVP_TREEITEM = 1, TVP_GLYPH = 2, TVP_HOTGLYPH = 4, LVP_LISTITEM = 1;
	private static final char [] BUTTON = "BUTTON\0".toCharArray ();
	private final long hwnd;
	private final char [] itemThemeClass;
	private final boolean listItems;
	private final IntSupplier zoom;
	private long itemTheme, buttonTheme;
	private int themeZoom = -1;
	private final RECT rect = new RECT ();
	private final SIZE size = new SIZE ();

	/**
	 * @param hwnd the window the parts are opened for
	 * @param itemThemeClass the uxtheme class of rows, <code>TREEVIEW</code> or <code>LISTVIEW</code> (NUL-terminated)
	 * @param zoom the widget's current native zoom
	 */
	public ThemeParts (long hwnd, char [] itemThemeClass, IntSupplier zoom) {
		this.hwnd = hwnd;
		this.itemThemeClass = itemThemeClass;
		this.listItems = itemThemeClass.length > 0 && itemThemeClass [0] == 'L';
		this.zoom = zoom;
	}

	/** Draws a part (a {@link RowCanvas} <code>PART_*</code> constant) in a box of a device context. */
	public void draw (long hdc, int part, int state, int x, int y, int width, int height) {
		int z = zoom.getAsInt ();
		if (z != themeZoom) {
			close ();
			themeZoom = z;
		}
		switch (part) {
			case RowCanvas.PART_SELECTION -> {
				OS.SetRect (rect, x, y, x + width, y + height);
				OS.DrawThemeBackground (itemTheme (), hdc, listItems ? LVP_LISTITEM : TVP_TREEITEM, selectionState (state), rect, null);
			}
			case RowCanvas.PART_EXPANDER -> {
				boolean hot = (state & RowCanvas.HOT) != 0;
				int partId = hot ? TVP_HOTGLYPH : TVP_GLYPH, stateId = (state & ~RowCanvas.HOT) == RowCanvas.EXPANDER_OPEN ? 2 : 1;
				centred (hdc, itemTheme (), partId, stateId, x, y, width, height, 0);
			}
			case RowCanvas.PART_CHECKBOX -> centred (hdc, buttonTheme (), OS.BP_CHECKBOX, checkState (state), x, y, width, height, 1);
			default -> {
				/* unknown part: nothing */
			}
		}
	}

	/*
	 * The part at its natural size, centred in the box. DrawThemeBackground stretches a part to the rectangle, so the
	 * rectangle is limited to the part size, as the Tree does for its check box image list (Tree.setCheckboxImageList).
	 */
	private void centred (long hdc, long theme, int partId, int stateId, int x, int y, int width, int height, int dy) {
		OS.GetThemePartSize (theme, hdc, partId, stateId, null, OS.TS_TRUE, size);
		int w = Math.min (width, size.cx), h = Math.min (height, size.cy);
		int left = x + (width - w) / 2, top = y + (height - h) / 2 + dy;
		OS.SetRect (rect, left, top, left + w, top + h);
		OS.DrawThemeBackground (theme, hdc, partId, stateId, rect, null);
	}

	private static int selectionState (int state) {
		return switch (state) {
			case RowCanvas.SELECTION_NO_FOCUS -> OS.TREIS_SELECTEDNOTFOCUS;
			case RowCanvas.SELECTION_HOT -> OS.TREIS_HOT;
			case RowCanvas.SELECTION_HOT_SELECTED -> 6; /* TREIS_HOTSELECTED, LISS_HOTSELECTED */
			default -> OS.TREIS_SELECTED;
		};
	}

	private static int checkState (int state) {
		int hot = (state & RowCanvas.HOT) != 0 ? 1 : 0;
		return switch (state & ~RowCanvas.HOT) {
			case RowCanvas.CHECKBOX_CHECKED -> OS.CBS_CHECKEDNORMAL + hot;
			case RowCanvas.CHECKBOX_MIXED -> OS.CBS_MIXEDNORMAL + hot;
			default -> OS.CBS_UNCHECKEDNORMAL + hot;
		};
	}

	/**
	 * A part drawn into a <code>width x height</code> image: premultiplied BGRA bytes, top-down. The part is drawn
	 * on black and on white; the difference gives the alpha the theme blended with.
	 */
	public byte [] image (int part, int state, int width, int height) {
		width = Math.max (1, width);
		height = Math.max (1, height);
		BackBuffer black = new BackBuffer (), white = new BackBuffer ();
		long screen = OS.GetDC (0);
		try {
			black.ensure (screen, width, height);
			white.ensure (screen, width, height);
			OS.SetRect (rect, 0, 0, width, height);
			OS.SetBkColor (black.dc, 0x000000);
			OS.ExtTextOut (black.dc, 0, 0, OS.ETO_OPAQUE, rect, null, 0, null);
			OS.SetBkColor (white.dc, 0xFFFFFF);
			OS.ExtTextOut (white.dc, 0, 0, OS.ETO_OPAQUE, rect, null, 0, null);
			draw (black.dc, part, state, 0, 0, width, height);
			draw (white.dc, part, state, 0, 0, width, height);
			/* a GDI call that answers flushes the thread's GDI batch before the pixels are read */
			OS.GetPixel (black.dc, 0, 0);
			OS.GetPixel (white.dc, 0, 0);
			int count = width * height * 4;
			byte [] onBlack = new byte [count], onWhite = new byte [count];
			OS.MoveMemory (onBlack, black.bits, count);
			OS.MoveMemory (onWhite, white.bits, count);
			for (int i = 0; i < count; i += 4) {
				int alpha = 255 - ((onWhite [i + 1] & 0xFF) - (onBlack [i + 1] & 0xFF));
				onBlack [i + 3] = (byte) Math.max (0, Math.min (255, alpha));
			}
			return onBlack;
		} finally {
			OS.ReleaseDC (0, screen);
			black.release ();
			white.release ();
		}
	}

	private long itemTheme () {
		if (itemTheme == 0) itemTheme = OS.OpenThemeData (hwnd, itemThemeClass, themeZoom);
		return itemTheme;
	}

	private long buttonTheme () {
		if (buttonTheme == 0) buttonTheme = OS.OpenThemeData (hwnd, BUTTON, themeZoom);
		return buttonTheme;
	}

	/** Closes the opened themes (they are opened again on the next draw). */
	public void close () {
		if (itemTheme != 0) OS.CloseThemeData (itemTheme);
		if (buttonTheme != 0) OS.CloseThemeData (buttonTheme);
		itemTheme = buttonTheme = 0;
	}
}
