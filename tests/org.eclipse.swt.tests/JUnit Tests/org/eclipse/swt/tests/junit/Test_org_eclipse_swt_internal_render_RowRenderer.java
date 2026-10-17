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

import java.util.*;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.render.*;
import org.junit.jupiter.api.*;

/**
 * The drawing core of the drawn planes without a display: the row renderer paints through a canvas that records
 * every primitive, under each built-in theme and with user-defined themes (glyph images, row and cell painters).
 */
public class Test_org_eclipse_swt_internal_render_RowRenderer {

	/** Records every primitive as one line. */
	static final class Recorder implements RowCanvas {
		final List<String> calls = new ArrayList<> ();

		@Override public void begin (int x, int y, int w, int h) { calls.add ("begin " + x + "," + y + " " + w + "x" + h); }
		@Override public void end () { calls.add ("end"); }
		@Override public void fill (int x, int y, int w, int h, int rgb) { calls.add ("fill " + x + "," + y + " " + w + "x" + h + " " + Integer.toHexString (rgb)); }
		@Override public void text (String text, long font, int rgb, int x, int y, int w, int h, int align) { calls.add ("text " + text + " font " + font + " " + Integer.toHexString (rgb) + " " + x + "," + y + " " + w + "x" + h + " align " + align); }
		@Override public void image (Image image, int x, int y, int w, int h) { calls.add ("image " + x + "," + y + " " + w + "x" + h); }
		@Override public void imageData (ImageData data, int x, int y, int w, int h) { calls.add ("imageData " + data.width + " " + x + "," + y + " " + w + "x" + h); }
		@Override public void polygon (int [] points, int count, int rgb) { calls.add ("polygon " + count + " " + Arrays.toString (Arrays.copyOf (points, count * 2)) + " " + Integer.toHexString (rgb)); }
		@Override public void part (int part, int state, int x, int y, int w, int h) { calls.add ("part " + part + " state " + state + " " + x + "," + y + " " + w + "x" + h); }
		@Override public void focus (int x, int y, int w, int h) { calls.add ("focus " + x + "," + y + " " + w + "x" + h); }
		@Override public void dispose () { calls.add ("dispose"); }

		long count (String prefix) {
			return calls.stream ().filter (c -> c.startsWith (prefix)).count ();
		}

		boolean has (String line) {
			return calls.contains (line);
		}
	}

	/* a tree row: expander, check box, one cell with text */
	static RowPaint row (int state) {
		RowPaint row = new RowPaint ();
		row.x = 0;
		row.y = 18;
		row.width = 300;
		row.height = 18;
		row.foreground = 0x000000;
		row.background = 0xFFFFFF;
		row.font = 7;
		row.state = state;
		row.expanderX = 0;
		row.expanderWidth = 19;
		row.checkX = 19;
		row.checkWidth = 16;
		row.selectionX = 40;
		row.selectionWidth = 60;
		row.cells (1);
		row.cellX [0] = 40;
		row.cellWidth [0] = 60;
		row.textX [0] = 40;
		row.textWidth [0] = 60;
		row.text [0] = "root 1";
		return row;
	}

	@Test
	public void nativeThemeDrawsPartsInTheFixedOrder () {
		Recorder canvas = new Recorder ();
		new RowRenderer (Theme.NATIVE).paint (canvas, row (RowPaint.SELECTED | RowPaint.FOCUSED | RowPaint.CHILDREN | RowPaint.EXPANDED | RowPaint.CHECKBOX | RowPaint.CHECKED | RowPaint.CARET | RowPaint.FOCUS_CUES | RowPaint.GRID_LINES));
		assertEquals (List.of (
			"fill 0,18 300x18 ffffff",
			"part 1 state 1 40,18 60x18",
			"part 2 state 2 0,18 19x18",
			"part 3 state 2 19,18 16x18",
			"text root 1 font 7 0 42,18 58x18 align 16384",
			"fill 99,18 1x18 e3e3e3",
			"fill 0,35 300x1 e3e3e3",
			"focus 40,18 60x18"), canvas.calls);
	}

	@Test
	public void nativeSelectionStates () {
		assertEquals (1, state (RowPaint.SELECTED | RowPaint.FOCUSED));
		assertEquals (2, state (RowPaint.SELECTED));
		assertEquals (3, state (RowPaint.HOT));
		assertEquals (4, state (RowPaint.SELECTED | RowPaint.HOT));
		assertEquals (2, state (RowPaint.DROP));
		Recorder canvas = new Recorder ();
		RowPaint none = row (0);
		new RowRenderer (Theme.NATIVE).paint (canvas, none);
		assertEquals (0, canvas.count ("part 1"));
		RowPaint empty = row (RowPaint.SELECTED);
		empty.selectionWidth = 0;
		canvas = new Recorder ();
		new RowRenderer (Theme.NATIVE).paint (canvas, empty);
		assertEquals (0, canvas.count ("part 1"));
	}

	private static int state (int bits) {
		Recorder canvas = new Recorder ();
		new RowRenderer (Theme.NATIVE).paint (canvas, row (bits));
		String part = canvas.calls.stream ().filter (c -> c.startsWith ("part 1 ")).findFirst ().orElseThrow ();
		return Integer.parseInt (part.split (" ") [3]);
	}

	@Test
	public void nativeExpanderAndCheckBoxStates () {
		Recorder canvas = new Recorder ();
		new RowRenderer (Theme.NATIVE).paint (canvas, row (RowPaint.CHILDREN | RowPaint.HOT | RowPaint.CHECKBOX | RowPaint.CHECKED | RowPaint.GRAYED));
		assertTrue (canvas.has ("part 2 state " + (RowCanvas.EXPANDER_CLOSED | RowCanvas.HOT) + " 0,18 19x18"), canvas.calls.toString ());
		assertTrue (canvas.has ("part 3 state " + RowCanvas.CHECKBOX_MIXED + " 19,18 16x18"));
		canvas = new Recorder ();
		new RowRenderer (Theme.NATIVE).paint (canvas, row (RowPaint.CHECKBOX));
		assertTrue (canvas.has ("part 3 state " + RowCanvas.CHECKBOX_UNCHECKED + " 19,18 16x18"));
		assertEquals (0, canvas.count ("part 2"), "no children, no expander");
		RowPaint slotless = row (RowPaint.CHILDREN | RowPaint.CHECKBOX);
		slotless.expanderWidth = slotless.checkWidth = 0;
		canvas = new Recorder ();
		new RowRenderer (Theme.NATIVE).paint (canvas, slotless);
		assertEquals (0, canvas.count ("part"));
	}

	@Test
	public void systemSelectionFillsWithHighlightColours () {
		RowPaint row = row (RowPaint.SELECTED | RowPaint.SYSTEM_SELECTION);
		row.systemSelectionBackground = 0x0078D7;
		row.systemSelectionForeground = 0xFFFFFF;
		Recorder canvas = new Recorder ();
		new RowRenderer (Theme.NATIVE).paint (canvas, row);
		assertTrue (canvas.has ("fill 40,18 60x18 78d7"));
		assertTrue (canvas.calls.stream ().anyMatch (c -> c.startsWith ("text root 1 font 7 ffffff")));
		canvas = new Recorder ();
		new RowRenderer (Theme.NATIVE).paint (canvas, row (RowPaint.HOT | RowPaint.SYSTEM_SELECTION));
		assertEquals (1, canvas.count ("fill"), "hot without selection draws nothing in a list box");
	}

	@Test
	public void cellsBackgroundsSortColumnFontsAndAlignment () {
		RowPaint row = row (0);
		row.cells (3);
		for (int i = 0; i < 3; i++) {
			row.cellX [i] = 100 * i;
			row.cellWidth [i] = 100;
			row.textX [i] = 100 * i;
			row.textWidth [i] = 100;
			row.text [i] = "c" + i;
		}
		row.cellBackground [0] = 0x112233;
		row.sortCell = 1;
		row.sortBackground = 0xF7F7F7;
		row.align [1] = SWT.CENTER;
		row.align [2] = SWT.RIGHT;
		row.cellFont [2] = 9;
		row.cellForeground [2] = 0xFF0000;
		Recorder canvas = new Recorder ();
		new RowRenderer (Theme.NATIVE).paint (canvas, row);
		assertTrue (canvas.has ("fill 0,18 100x18 112233"));
		assertTrue (canvas.has ("fill 100,18 100x18 f7f7f7"));
		assertTrue (canvas.has ("text c0 font 7 0 2,18 98x18 align 16384"));
		assertTrue (canvas.has ("text c1 font 7 0 100,18 100x18 align 16777216"));
		assertTrue (canvas.has ("text c2 font 9 ff0000 200,18 100x18 align 131072"));
	}

	@Test
	public void emptyCellsDrawNoText () {
		RowPaint row = row (0);
		row.cells (3);
		row.text [0] = null;
		row.text [1] = "";
		row.text [2] = "x";
		row.textWidth [2] = 0;
		Recorder canvas = new Recorder ();
		new RowRenderer (Theme.NATIVE).paint (canvas, row);
		assertEquals (0, canvas.count ("text"));
	}

	@Test
	public void imagesAreDrawnInTheirBox () {
		RowPaint row = row (0);
		row.image [0] = null;
		Recorder canvas = new Recorder ();
		RowRenderer renderer = new RowRenderer (Theme.NATIVE);
		renderer.paint (canvas, row);
		assertEquals (0, canvas.count ("image "));
		renderer.paintEmpty (canvas, row, 0, 36, 300, 100);
		assertTrue (canvas.has ("fill 0,36 300x100 ffffff"));
	}

	@Test
	public void swingThemeDrawsMetalHandlesBoxesAndFocusLines () {
		Recorder canvas = new Recorder ();
		new RowRenderer (Theme.SWING).paint (canvas, row (RowPaint.SELECTED | RowPaint.FOCUSED | RowPaint.CHILDREN | RowPaint.CHECKBOX | RowPaint.CHECKED | RowPaint.CARET | RowPaint.FOCUS_CUES));
		assertTrue (canvas.has ("fill 0,18 300x18 ffffff"), "theme background");
		assertTrue (canvas.has ("fill 40,18 60x18 b8cfe5"), "selection");
		assertTrue (canvas.has ("fill 5,23 9x9 6382bf"), "handle border");
		assertTrue (canvas.has ("fill 9,25 1x5 333333"), "plus: closed");
		assertTrue (canvas.calls.stream ().anyMatch (c -> c.startsWith ("text root 1 font 7 333333")), "theme text colour");
		assertTrue (canvas.has ("fill 40,18 60x1 a3b8cc"), "focus line");
		assertEquals (0, canvas.count ("part"));
		canvas = new Recorder ();
		new RowRenderer (Theme.SWING).paint (canvas, row (RowPaint.SELECTED | RowPaint.CHILDREN | RowPaint.EXPANDED | RowPaint.CHECKBOX | RowPaint.CHECKED | RowPaint.GRAYED));
		assertTrue (canvas.has ("fill 40,18 60x18 dde6f0"), "unfocused selection");
		assertFalse (canvas.has ("fill 9,25 1x5 333333"), "minus only: open");
		assertTrue (canvas.calls.stream ().anyMatch (c -> c.endsWith (" 808080")), "grayed mark");
		canvas = new Recorder ();
		new RowRenderer (Theme.SWING).paint (canvas, row (RowPaint.HOT | RowPaint.CHECKBOX));
		assertTrue (canvas.has ("fill 40,18 60x18 e6eef6"), "hot");
	}

	@Test
	public void initiative31ThemeDrawsTrianglesRoundedBoxesAndFocusOnSelection () {
		Recorder canvas = new Recorder ();
		new RowRenderer (Theme.INITIATIVE31).paint (canvas, row (RowPaint.SELECTED | RowPaint.FOCUSED | RowPaint.CHILDREN | RowPaint.CHECKBOX | RowPaint.CHECKED));
		assertTrue (canvas.has ("polygon 3 [14, 27, 4, 22, 4, 32] 808080"), canvas.calls.toString ());
		assertTrue (canvas.has ("fill 40,18 60x18 e0eefe"));
		assertTrue (canvas.calls.stream ().anyMatch (c -> c.endsWith (" 62b8")), "checked fill");
		assertTrue (canvas.has ("focus 40,18 60x18"));
		canvas = new Recorder ();
		new RowRenderer (Theme.INITIATIVE31).paint (canvas, row (RowPaint.CHILDREN | RowPaint.EXPANDED | RowPaint.CHECKBOX | RowPaint.CHECKED | RowPaint.GRAYED | RowPaint.HOT));
		assertTrue (canvas.has ("polygon 3 [9, 32, 4, 22, 14, 22] 808080"));
		assertTrue (canvas.calls.stream ().anyMatch (c -> c.endsWith (" c0c0c0")), "grayed fill");
		assertTrue (canvas.has ("fill 40,18 60x18 ffff00"), "yellow hover");
		assertEquals (0, canvas.count ("focus"));
		RowPaint tiny = row (RowPaint.CHECKBOX);
		tiny.checkWidth = 4;
		tiny.height = 5;
		canvas = new Recorder ();
		new RowRenderer (Theme.INITIATIVE31).paint (canvas, tiny);
		assertTrue (canvas.calls.stream ().anyMatch (c -> c.equals ("fill 20,19 2x2 808080")), "a box too small to round is a plain fill");
	}

	@Test
	public void themeGlyphImagesReplaceTheLooksGlyphs () {
		ImageData open = new ImageData (8, 8, 24, new PaletteData (0xFF0000, 0xFF00, 0xFF)), closed = new ImageData (6, 6, 24, new PaletteData (0xFF0000, 0xFF00, 0xFF));
		ImageData on = new ImageData (10, 10, 24, new PaletteData (0xFF0000, 0xFF00, 0xFF)), off = new ImageData (11, 11, 24, new PaletteData (0xFF0000, 0xFF00, 0xFF)),
			mixed = new ImageData (12, 12, 24, new PaletteData (0xFF0000, 0xFF00, 0xFF));
		Theme theme = Theme.SWING.derive ("glyphs").expanderImages (open, closed).checkImages (on, off, mixed).build ();
		Recorder canvas = new Recorder ();
		new RowRenderer (theme).paint (canvas, row (RowPaint.CHILDREN | RowPaint.EXPANDED | RowPaint.CHECKBOX | RowPaint.CHECKED));
		assertTrue (canvas.has ("imageData 8 5,23 8x8"), canvas.calls.toString ());
		assertTrue (canvas.has ("imageData 10 22,22 10x10"));
		canvas = new Recorder ();
		new RowRenderer (theme).paint (canvas, row (RowPaint.CHILDREN | RowPaint.CHECKBOX));
		assertTrue (canvas.has ("imageData 6 6,24 6x6"));
		assertTrue (canvas.has ("imageData 11 21,21 11x11"));
		canvas = new Recorder ();
		new RowRenderer (theme).paint (canvas, row (RowPaint.CHECKBOX | RowPaint.CHECKED | RowPaint.GRAYED));
		assertTrue (canvas.has ("imageData 12 21,21 12x12"));
	}

	@Test
	public void rowAndCellPaintersReplaceTheDefaults () {
		Theme cells = Theme.builder ("cells").cellPainter ((canvas, row, cell, theme) -> canvas.fill (row.cellX [cell], row.y, 1, 1, 0xABCDEF)).build ();
		Recorder canvas = new Recorder ();
		new RowRenderer (cells).paint (canvas, row (0));
		assertTrue (canvas.has ("fill 40,18 1x1 abcdef"));
		assertEquals (0, canvas.count ("text"));
		Theme rows = Theme.builder ("rows").rowPainter ((c, row, theme, defaults) -> {
			c.fill (0, 0, 1, 1, 0x123456);
			defaults.paintCell (c, row, 0);
		}).selectionForeground (0x00FF00).build ();
		canvas = new Recorder ();
		RowRenderer renderer = new RowRenderer (rows);
		renderer.paint (canvas, row (RowPaint.SELECTED));
		assertEquals (List.of ("fill 0,0 1x1 123456", "text root 1 font 7 ff00 42,18 58x18 align 16384"), canvas.calls);
		assertSame (rows, renderer.theme ());
	}

	@Test
	public void rowPaintGrowsItsLanesAndConvertsColours () {
		RowPaint row = new RowPaint ();
		row.cells (5);
		assertEquals (5, row.cells);
		assertTrue (row.cellX.length >= 5);
		row.text [4] = "x";
		row.cellFont [4] = 3;
		row.cells (2);
		assertNull (row.text [0]);
		assertEquals (-1, row.cellFont [0]);
		row.state = RowPaint.SELECTED;
		assertTrue (row.is (RowPaint.SELECTED));
		assertFalse (row.is (RowPaint.HOT));
		assertEquals (-1, RowPaint.rgbOf (-1));
		assertEquals (0x112233, RowPaint.rgbOf (0x332211));
	}
}
