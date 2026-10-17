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
package org.eclipse.swt.tests.junit;

import static org.junit.jupiter.api.Assertions.*;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;

import org.eclipse.swt.internal.render.*;
import org.eclipse.swt.internal.win32.*;
import org.junit.jupiter.api.*;

/**
 * The win32 pieces of the drawn planes that run without a display: the back buffer (GDI DIB), the image and font
 * conversions, the theme part images and the backend selection. Headless: no shell, no event loop, no desktop
 * interaction.
 */
public class Test_org_eclipse_swt_internal_render_Win32Backends {
	static final char [] TREEVIEW = ("TREEVIEW" + (char) 0).toCharArray ();

	@BeforeAll
	public static void win32Only () {
		Assumptions.assumeTrue ("win32".equals (SWT.getPlatform ()));
	}

	@Test
	public void backBufferKeepsItsDibUntilItGrows () {
		BackBuffer buffer = new BackBuffer ();
		try {
			assertTrue (buffer.ensure (0, 100, 50));
			long dc = buffer.dc, bitmap = buffer.bitmap, bits = buffer.bits;
			assertTrue (dc != 0 && bitmap != 0 && bits != 0);
			assertEquals (100, buffer.width);
			assertEquals (50, buffer.height);
			assertFalse (buffer.ensure (0, 80, 40), "smaller fits");
			assertEquals (dc, buffer.dc);
			assertEquals (bits, buffer.bits);
			assertTrue (buffer.ensure (0, 120, 50), "wider recreates");
			assertTrue (buffer.ensure (0, 1, 60), "taller recreates; width never shrinks below 1");
			assertTrue (buffer.width >= 1 && buffer.height >= 60);
		} finally {
			buffer.release ();
		}
		assertEquals (0, buffer.dc);
		assertEquals (0, buffer.bits);
		assertEquals (0, buffer.width);
		buffer.release ();
		assertEquals (0, buffer.dc, "release is idempotent");
	}

	@Test
	public void backBufferPresentsItsPixels () {
		BackBuffer source = new BackBuffer (), target = new BackBuffer ();
		try {
			source.ensure (0, 8, 8);
			target.ensure (0, 8, 8);
			RECT rect = new RECT ();
			OS.SetRect (rect, 0, 0, 8, 8);
			OS.SetBkColor (source.dc, 0x0000FF); /* COLORREF red */
			OS.ExtTextOut (source.dc, 0, 0, OS.ETO_OPAQUE, rect, null, 0, null);
			OS.SetBkColor (target.dc, 0xFFFFFF);
			OS.ExtTextOut (target.dc, 0, 0, OS.ETO_OPAQUE, rect, null, 0, null);
			source.present (target.dc, 2, 2, 4, 4);
			assertEquals (0x0000FF, OS.GetPixel (target.dc, 3, 3), "copied");
			assertEquals (0xFFFFFF, OS.GetPixel (target.dc, 0, 0), "outside the rectangle untouched");
		} finally {
			source.release ();
			target.release ();
		}
	}

	@Test
	public void bgraConvertsAlphaAndMasks () {
		PaletteData palette = new PaletteData (0xFF0000, 0xFF00, 0xFF);
		ImageData plain = new ImageData (2, 1, 24, palette);
		plain.setPixel (0, 0, 0x112233);
		plain.setPixel (1, 0, 0xFFFFFF);
		byte [] bgra = RowImages.bgra (plain);
		assertEquals (8, bgra.length);
		assertEquals (0x33, bgra [0] & 0xFF);
		assertEquals (0x22, bgra [1] & 0xFF);
		assertEquals (0x11, bgra [2] & 0xFF);
		assertEquals (0xFF, bgra [3] & 0xFF);
		ImageData alpha = new ImageData (1, 1, 24, palette);
		alpha.setPixel (0, 0, 0xFF0000);
		alpha.setAlpha (0, 0, 128);
		byte [] premultiplied = RowImages.bgra (alpha);
		assertEquals (128, premultiplied [3] & 0xFF);
		assertEquals (128 * 0xFF / 255, premultiplied [2] & 0xFF, "red premultiplied");
		assertEquals (0, premultiplied [0] & 0xFF);
		ImageData masked = new ImageData (2, 1, 24, palette);
		masked.transparentPixel = 0x000000;
		masked.setPixel (0, 0, 0x00FF00);
		masked.setPixel (1, 0, 0x000000);
		byte [] withMask = RowImages.bgra (masked);
		assertEquals (0xFF, withMask [3] & 0xFF, "opaque pixel");
		assertEquals (0, withMask [7] & 0xFF, "transparent pixel");
		assertEquals (0, withMask [4] & 0xFF, "transparent pixel premultiplies to black");
	}

	@Test
	public void fontAttributesComeFromTheLogfont () {
		int [] out = new int [3];
		assertEquals ("Segoe UI", RowImages.font (0, out));
		assertEquals (-12, out [0]);
		assertEquals (400, out [1]);
		assertEquals (0, out [2]);
		assertEquals ("Segoe UI", RowImages.font (-1, out));
		long system = OS.GetStockObject (OS.SYSTEM_FONT);
		String face = RowImages.font (system, out);
		assertFalse (face.isEmpty ());
		assertTrue (out [0] != 0, "height");
		assertTrue (out [1] > 0, "weight");
	}

	@Test
	public void themePartImagesCarryAlpha () {
		ThemeParts parts = new ThemeParts (0, TREEVIEW, () -> 100);
		try {
			byte [] check = parts.image (RowCanvas.PART_CHECKBOX, RowCanvas.CHECKBOX_CHECKED, 16, 16);
			assertEquals (16 * 16 * 4, check.length);
			boolean someOpaque = false, someClear = false;
			for (int i = 3; i < check.length; i += 4) {
				int alpha = check [i] & 0xFF;
				if (alpha > 200) someOpaque = true;
				if (alpha < 55) someClear = true;
			}
			assertTrue (someOpaque, "the check box mark is opaque");
			byte [] tiny = parts.image (RowCanvas.PART_EXPANDER, RowCanvas.EXPANDER_OPEN, 0, 0);
			assertEquals (4, tiny.length, "degenerate sizes clamp to 1 x 1");
			byte [] unknown = parts.image (99, 0, 2, 2);
			assertEquals (16, unknown.length, "an unknown part draws nothing");
			parts.close ();
			parts.close ();
			assertNotNull (parts.image (RowCanvas.PART_SELECTION, RowCanvas.SELECTION_SELECTED, 8, 8), "usable again after close");
		} finally {
			parts.close ();
		}
	}

	@Test
	public void themePartsDrawIntoADeviceContext () {
		ThemeParts parts = new ThemeParts (0, TREEVIEW, () -> 100);
		BackBuffer buffer = new BackBuffer ();
		try {
			buffer.ensure (0, 32, 32);
			RECT rect = new RECT ();
			OS.SetRect (rect, 0, 0, 32, 32);
			OS.SetBkColor (buffer.dc, 0xFFFFFF);
			OS.ExtTextOut (buffer.dc, 0, 0, OS.ETO_OPAQUE, rect, null, 0, null);
			parts.draw (buffer.dc, RowCanvas.PART_SELECTION, RowCanvas.SELECTION_SELECTED, 0, 0, 32, 32);
			parts.draw (buffer.dc, RowCanvas.PART_SELECTION, RowCanvas.SELECTION_NO_FOCUS, 0, 0, 32, 32);
			parts.draw (buffer.dc, RowCanvas.PART_SELECTION, RowCanvas.SELECTION_HOT, 0, 0, 32, 32);
			parts.draw (buffer.dc, RowCanvas.PART_SELECTION, RowCanvas.SELECTION_HOT_SELECTED, 0, 0, 32, 32);
			parts.draw (buffer.dc, RowCanvas.PART_EXPANDER, RowCanvas.EXPANDER_CLOSED, 0, 0, 32, 32);
			parts.draw (buffer.dc, RowCanvas.PART_EXPANDER, RowCanvas.EXPANDER_OPEN | RowCanvas.HOT, 0, 0, 32, 32);
			parts.draw (buffer.dc, RowCanvas.PART_CHECKBOX, RowCanvas.CHECKBOX_UNCHECKED, 0, 0, 32, 32);
			parts.draw (buffer.dc, RowCanvas.PART_CHECKBOX, RowCanvas.CHECKBOX_MIXED | RowCanvas.HOT, 0, 0, 32, 32);
			parts.draw (buffer.dc, 99, 0, 0, 0, 32, 32);
			int changed = 0;
			for (int x = 0; x < 32; x++) {
				for (int y = 0; y < 32; y++) {
					if (OS.GetPixel (buffer.dc, x, y) != 0xFFFFFF) changed++;
				}
			}
			assertTrue (changed > 0, "the parts drew pixels");
		} finally {
			buffer.release ();
			parts.close ();
		}
	}

	@Test
	public void backendSelectionFallsBack () {
		/* no Skija jars on this class path: m3 and gpu fall back to the GC backend; custom is direct */
		assertEquals ("GcRowCanvas", RowBackends.create (org.eclipse.swt.internal.WidgetMode.CUSTOM, "Tree", 0, TREEVIEW, () -> 100, hdc -> null).getClass ().getSimpleName ());
		assertEquals ("GcRowCanvas", RowBackends.create (org.eclipse.swt.internal.WidgetMode.M3, "Tree", 0, TREEVIEW, () -> 100, hdc -> null).getClass ().getSimpleName ());
		assertEquals ("GcRowCanvas", RowBackends.create (org.eclipse.swt.internal.WidgetMode.GPU, "Tree", 0, TREEVIEW, () -> 100, hdc -> null).getClass ().getSimpleName ());
	}

	@Test
	public void gcCanvasFillsTextAndPolygonsHeadless () {
		BackBuffer buffer = new BackBuffer ();
		GcRowCanvas canvas = new GcRowCanvas (0, TREEVIEW, () -> 100, hdc -> null);
		try {
			buffer.ensure (0, 64, 32);
			canvas.target (buffer);
			canvas.begin (0, 0, 64, 32);
			canvas.fill (0, 0, 64, 32, 0xFFFFFF);
			canvas.fill (0, 0, 0, 10, 0x123456);
			canvas.fill (2, 2, 4, 4, 0xFF0000);
			canvas.text ("", 0, 0x000000, 0, 0, 10, 10, SWT.LEFT);
			canvas.text ("Ab", 0, 0x000000, 8, 2, 40, 20, SWT.LEFT);
			canvas.text ("Ab", OS.GetStockObject (OS.SYSTEM_FONT), 0x000000, 8, 2, 40, 20, SWT.CENTER);
			canvas.text ("Ab", -1, 0x000000, 8, 2, 40, 20, SWT.RIGHT);
			canvas.polygon (new int [] {10, 20, 20, 20, 15, 28}, 3, 0x00FF00);
			canvas.polygon (new int [] {0, 0}, 1, 0x00FF00);
			canvas.imageData (null, 0, 0, 4, 4);
			canvas.focus (1, 1, 20, 20);
			canvas.end ();
			assertEquals (0x0000FF, OS.GetPixel (buffer.dc, 3, 3), "fill is red (COLORREF)");
			assertEquals (0x00FF00, OS.GetPixel (buffer.dc, 15, 24), "polygon is green");
		} finally {
			canvas.dispose ();
			buffer.release ();
		}
	}
}
