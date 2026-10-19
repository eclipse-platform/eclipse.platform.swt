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
 * Process-wide font id table: a font is an int id keyed by its value (face,
 * pixel height, weight, italic), never by a native handle, so a recycled HFONT
 * can never alias a retained font. Ids are pinned for the process lifetime;
 * when the table is full, new fonts are not replayable and the plane falls back
 * to GDI. Metrics are the platform's (GDI TEXTMETRIC on win32) so a GPU glyph
 * run lands where the platform would have placed it.
 * <p>Confined to the first thread that defines a font (the display thread).</p>
 */
public final class FontTable {
	public static final int CAPACITY = 1024;
	public static final FontTable INSTANCE = new FontTable ();

	final RetainedSlots slots = new RetainedSlots (CAPACITY);
	public final String [] face = new String [CAPACITY];
	public final int [] heightPx = new int [CAPACITY], weight = new int [CAPACITY], italic = new int [CAPACITY];
	public final int [] ascent = new int [CAPACITY], cellHeight = new int [CAPACITY];
	private Thread owner;

	private FontTable () {
	}

	/**
	 * Returns the id for the font, defining it on first use; -1 on another thread,
	 * on a face-hash collision or when full.
	 */
	public int define (char [] faceChars, int faceLength, int lfHeight, int fontWeight, int fontItalic, int pixelHeight, int fontAscent, int fontCellHeight) {
		Thread current = Thread.currentThread ();
		if (owner == null) owner = current;
		if (owner != current) return -1;
		int hash = 0;
		for (int i = 0; i < faceLength; i++) hash = 31 * hash + faceChars [i];
		int style = (fontWeight / 100 & 0xF) | (fontItalic != 0 ? 0x10 : 0) | (lfHeight < 0 ? 0x80 : 0);
		int slot = slots.acquire (PaintKeys.fontKey (hash, Math.abs (lfHeight), style), null);
		if (slot < 0) return -1;
		if (!slots.isNew ()) {
			GpuCounters.inc (GpuCounters.FONT_HIT);
			return face [slot].contentEquals (java.nio.CharBuffer.wrap (faceChars, 0, faceLength)) ? slot : -1;
		}
		slots.pin (slot);
		face [slot] = new String (faceChars, 0, faceLength);
		heightPx [slot] = pixelHeight;
		weight [slot] = fontWeight;
		italic [slot] = fontItalic;
		ascent [slot] = fontAscent;
		cellHeight [slot] = fontCellHeight;
		GpuCounters.inc (GpuCounters.FONT_MISS);
		return slot;
	}

	/* Platform advance widths in 256-char pages, filled lazily by the recorder. */
	final int [][][] advances = new int [CAPACITY][][];

	/** Advance width of {@code c} in font {@code id}, or -1 when its page is not loaded. */
	public int advance (int id, char c) {
		int [][] pages = advances [id];
		int [] page = pages == null ? null : pages [c >>> 8];
		return page == null ? -1 : page [c & 0xFF];
	}

	public boolean hasPage (int id, int page) {
		int [][] pages = advances [id];
		return pages != null && pages [page] != null;
	}

	public void setPage (int id, int page, int [] widths) {
		if (advances [id] == null) advances [id] = new int [256][];
		advances [id][page] = widths;
	}

	public int size () {
		return slots.size ();
	}
}
