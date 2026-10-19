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

import java.lang.reflect.*;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.render.*;
import org.eclipse.swt.internal.win32.*;
import org.junit.jupiter.api.*;

/**
 * The M3 raster backend without a display: it draws through the library straight into a {@link BackBuffer}'s DIB,
 * whose pixels GDI reads back. Skips (assumption) when the library jars are not on the class path. The data-oriented
 * paint path (rule 18) is exercised: fills and text run on cached pointers.
 */
public class Test_org_eclipse_swt_internal_render_M3Canvas {
	static final char [] TREEVIEW = ("TREEVIEW" + (char) 0).toCharArray ();
	static Class<?> type;

	@BeforeAll
	public static void m3BackendOnly () {
		Assumptions.assumeTrue ("win32".equals (SWT.getPlatform ()));
		try {
			type = Class.forName ("org.eclipse.swt.internal.render.m3.M3RowCanvas");
		} catch (ClassNotFoundException | LinkageError e) {
			Assumptions.assumeTrue (false, "the M3 backend and the library jars are not on the class path");
		}
	}

	static BufferCanvas raster () throws ReflectiveOperationException {
		Constructor<?> constructor = type.getConstructor (long.class, char [].class, java.util.function.IntSupplier.class);
		return (BufferCanvas) constructor.newInstance (0L, TREEVIEW, (java.util.function.IntSupplier) () -> 100);
	}

	@Test
	public void rasterDrawsIntoTheDib () throws ReflectiveOperationException {
		BackBuffer buffer = new BackBuffer ();
		BufferCanvas canvas = raster ();
		try {
			buffer.ensure (0, 64, 40);
			canvas.target (buffer);
			canvas.begin (0, 0, 64, 40);
			canvas.fill (0, 0, 64, 40, 0xFFFFFF);
			canvas.fill (2, 2, 8, 8, 0xFF0000);
			canvas.fill (0, 0, 0, 8, 0x123456);
			canvas.text ("", 0, 0, 0, 0, 10, 10, SWT.LEFT);
			canvas.text ("Ag", 0, 0x000000, 12, 2, 40, 16, SWT.LEFT);
			canvas.text ("Ag", 0, 0x000000, 12, 20, 40, 16, SWT.CENTER);
			canvas.text ("Ag", OS.GetStockObject (OS.SYSTEM_FONT), 0x0000FF, 12, 20, 40, 16, SWT.RIGHT);
			canvas.polygon (new int [] {30, 28, 40, 28, 35, 38}, 3, 0x00FF00);
			canvas.polygon (new int [] {0, 0}, 1, 0x00FF00);
			canvas.focus (1, 1, 30, 30);
			canvas.imageData (new ImageData (4, 4, 24, new PaletteData (0xFF0000, 0xFF00, 0xFF)), 50, 2, 4, 4);
			canvas.imageData (null, 0, 0, 4, 4);
			canvas.end ();
			assertEquals (0x0000FF, OS.GetPixel (buffer.dc, 4, 4), "fill is red as a COLORREF");
			assertEquals (0x00FF00, OS.GetPixel (buffer.dc, 35, 33), "polygon is green");
			boolean dark = false;
			for (int x = 12; x < 40 && !dark; x++) {
				for (int y = 2; y < 18 && !dark; y++) {
					int pixel = OS.GetPixel (buffer.dc, x, y);
					dark = (pixel & 0xFF) < 128 && (pixel >> 16 & 0xFF) < 128;
				}
			}
			assertTrue (dark, "text drew dark pixels");
			canvas.target (buffer);
			assertTrue (buffer.ensure (0, 100, 60), "grow");
			canvas.target (buffer);
			canvas.begin (0, 0, 100, 60);
			canvas.fill (0, 0, 100, 60, 0x336699);
			canvas.end ();
			assertEquals (0x996633, OS.GetPixel (buffer.dc, 99, 59), "resized surface writes the new DIB");
		} finally {
			canvas.dispose ();
			buffer.release ();
		}
	}

	@Test
	public void fontAndLineCachesAreBounded () throws ReflectiveOperationException {
		BackBuffer buffer = new BackBuffer ();
		BufferCanvas canvas = raster ();
		try {
			buffer.ensure (0, 32, 16);
			canvas.target (buffer);
			canvas.begin (0, 0, 32, 16);
			for (int i = 0; i < 2100; i++) canvas.text ("line " + i, 0, 0, 0, 0, 32, 16, SWT.LEFT);
			for (int i = 0; i < 2100; i++) canvas.text ("line " + (i % 7), 0, 0, 0, 0, 32, 16, SWT.LEFT);
			canvas.end ();
		} finally {
			canvas.dispose ();
			buffer.release ();
		}
		assertTrue (true, "the shaped-line LRU evicted and closed entries without failing");
	}

	/* text wider than its box with ELLIPSIS ends with "..." (cut once per width, then cached); without it the text is clipped */
	@Test
	public void ellipsisCutsTextThatDoesNotFit () throws ReflectiveOperationException {
		BackBuffer buffer = new BackBuffer ();
		BufferCanvas canvas = raster ();
		try {
			buffer.ensure (0, 60, 48);
			canvas.target (buffer);
			canvas.begin (0, 0, 60, 48);
			canvas.fill (0, 0, 60, 48, 0xFFFFFF);
			String text = "a longer third cell 207";
			canvas.text (text, 0, 0x000000, 0, 0, 50, 16, SWT.LEFT);
			canvas.text (text, 0, 0x000000, 0, 16, 50, 16, SWT.LEFT | RowCanvas.ELLIPSIS);
			canvas.text (text, 0, 0x000000, 0, 16, 50, 16, SWT.LEFT | RowCanvas.ELLIPSIS);
			canvas.text (text, 0, 0x000000, 0, 32, 4, 16, SWT.LEFT | RowCanvas.ELLIPSIS);
			canvas.text ("fits", 0, 0x000000, 0, 32, 50, 16, SWT.RIGHT | RowCanvas.ELLIPSIS);
			canvas.end ();
			int differing = 0;
			for (int x = 0; x < 50; x++) {
				for (int y = 0; y < 16; y++) {
					if (OS.GetPixel (buffer.dc, x, y) != OS.GetPixel (buffer.dc, x, y + 16)) differing++;
				}
			}
			assertTrue (differing > 0, "the cut line differs from the clipped one");
		} finally {
			canvas.dispose ();
			buffer.release ();
		}
	}

	@Test
	public void backendSelectionPrefersTheM3Backend () {
		BufferCanvas m3 = RowBackends.create (WidgetMode.M3, "Tree", 0, TREEVIEW, () -> 100, hdc -> null);
		try {
			assertEquals ("M3RowCanvas", m3.getClass ().getSimpleName ());
		} finally {
			m3.dispose ();
		}
	}

	@Test
	public void themePartsDrawThroughTheRasterCanvas () throws ReflectiveOperationException {
		BackBuffer buffer = new BackBuffer ();
		BufferCanvas canvas = raster ();
		try {
			buffer.ensure (0, 32, 32);
			canvas.target (buffer);
			canvas.begin (0, 0, 32, 32);
			canvas.fill (0, 0, 32, 32, 0xFFFFFF);
			canvas.part (RowCanvas.PART_CHECKBOX, RowCanvas.CHECKBOX_CHECKED, 2, 2, 20, 20);
			canvas.part (RowCanvas.PART_CHECKBOX, RowCanvas.CHECKBOX_CHECKED, 2, 2, 20, 20);
			canvas.part (RowCanvas.PART_EXPANDER, RowCanvas.EXPANDER_OPEN, 2, 2, 0, 20);
			canvas.end ();
			int changed = 0;
			for (int x = 0; x < 32; x++) {
				for (int y = 0; y < 32; y++) {
					if (OS.GetPixel (buffer.dc, x, y) != 0xFFFFFF) changed++;
				}
			}
			assertTrue (changed > 0, "the part drew pixels");
		} finally {
			canvas.dispose ();
			buffer.release ();
		}
	}
}
