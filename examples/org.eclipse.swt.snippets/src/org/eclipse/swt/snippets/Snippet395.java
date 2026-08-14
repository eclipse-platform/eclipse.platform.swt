/*******************************************************************************
 * Copyright (c) 2026 Vector Informatik GmbH and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package org.eclipse.swt.snippets;

import java.util.*;
import java.util.concurrent.atomic.*;

import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

/*
 * Windows plain GDI vs. GDI+ text rendering snippet.
 *
 * On Windows, GC.drawText() renders text in one of two ways: with plain GDI
 * (OS.DrawText) whenever the GC is not advanced, and with GDI+ once
 * GC.setAdvanced(true) is active. The two engines compute text layout
 * independently, so kerning, tab stop width, mnemonic underlining and
 * bidi/mirroring can all come out differently depending on which one draws.
 *
 * This snippet renders a series of text properties, one row per property, and
 * lets the rendering path be switched at runtime, so that the results can be
 * compared visually without restarting the process:
 * - "Use GDI+ (advanced) rendering" calls GC.setAdvanced() and re-renders
 *   every row, switching between plain GDI and GDI+.
 *
 * The rows labelled "unsupported glyph (U+FFFE)" append U+FFFE, a Unicode
 * non-character that no standard font has a glyph for. Strings containing such
 * a character are always laid out by GDI+ itself rather than having their glyph
 * positions computed by GDI, which is what makes the GDI+ tab stop and
 * decoration handling observable. The trailing box-shaped glyph drawn for
 * U+FFFE itself is expected.
 *
 * What to expect while toggling:
 * - The two tab rows must expand tabs to the same column width in every
 *   combination.
 * - "Mnemonic" shows an underlined "F" in every combination (it does not
 *   depend on font-level decoration).
 * - "Kerning pair", the tab rows and "Mirrored / RTL" may differ slightly in
 *   spacing/positioning between the engines, but should never render blank,
 *   wildly stretched/compressed, or with overlapping glyphs.
 * - The script/charset rows (Arabic, Hebrew, CJK, Cyrillic, Greek, combining
 *   diacritics) should render recognisable, visible glyphs in every
 *   combination.
 * - "Underlined"/"Strikeout"/"Bold + underlined" render their decoration with
 *   plain GDI, but go blank once GDI+/advanced is enabled, unless U+FFFE
 *   forces GDI+'s own text layout. See
 *   https://github.com/eclipse-platform/eclipse.platform.swt/issues/3091 .
 *
 * On platforms other than Windows, GC.setAdvanced() does not select a
 * different text rendering engine, so all rows render identically regardless
 * of the checkbox state.
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */
public class Snippet395 {

	/** One row of the comparison: a label, the text properties to apply, and how to draw it. */
	record TextRow(String label, int fontStyle, boolean underline, boolean strikeout, String text, int drawFlags,
			int gcStyle) {
		TextRow(String label, String text) {
			this(label, SWT.NORMAL, false, false, text, SWT.DRAW_TRANSPARENT, SWT.NONE);
		}
	}

	/**
	 * Renders one fresh sample {@link Image} per row, either with plain GDI
	 * ({@code advanced == false}) or with GDI+ ({@code advanced == true}).
	 * Callers are responsible for disposing the previous set of images
	 * returned by an earlier call.
	 */
	private static Map<TextRow, Image> renderSamples(Display display, java.util.List<TextRow> rows,
			Map<TextRow, Font> fonts, int sampleWidth, int sampleHeight, boolean advanced) {
		Map<TextRow, Image> samples = new HashMap<>();
		for (TextRow row : rows) {
			Image sample = new Image(display, sampleWidth, sampleHeight);
			GC gc = new GC(sample, row.gcStyle());
			try {
				gc.setAdvanced(advanced);
				gc.setBackground(new Color(255, 255, 255));
				gc.fillRectangle(sample.getBounds());
				gc.setForeground(new Color(0, 0, 0));
				gc.setFont(fonts.get(row));
				gc.drawText(row.text(), 5, 5, row.drawFlags());
			} finally {
				gc.dispose();
			}
			samples.put(row, sample);
		}
		return samples;
	}

	@SuppressWarnings("restriction")
	public static void main(String[] args) {
		// U+FFFE has no glyph in any standard font. Appending it to a string makes
		// GC.drawText() lay out that whole string with GDI+ instead of computing
		// the glyph positions with GDI, so the rows using it always show what
		// GDI+'s own text layout does with the respective text property.
		String unsupportedGlyph = String.valueOf((char) 0xFFFE);

		java.util.List<TextRow> rows = new ArrayList<>();
		rows.add(new TextRow("Plain text", "Hello World"));
		rows.add(new TextRow("Bold", SWT.BOLD, false, false, "Hello World", SWT.DRAW_TRANSPARENT, SWT.NONE));
		rows.add(new TextRow("Italic", SWT.ITALIC, false, false, "Hello World", SWT.DRAW_TRANSPARENT, SWT.NONE));
		rows.add(new TextRow("Underlined", SWT.NORMAL, true, false, "Hello World", SWT.DRAW_TRANSPARENT, SWT.NONE));
		rows.add(new TextRow("Strikeout", SWT.NORMAL, false, true, "Hello World", SWT.DRAW_TRANSPARENT, SWT.NONE));
		rows.add(new TextRow("Bold + underlined", SWT.BOLD, true, false, "Hello World", SWT.DRAW_TRANSPARENT,
				SWT.NONE));
		rows.add(new TextRow("Underlined + unsupported glyph (U+FFFE)", SWT.NORMAL, true, false,
				"Hi" + unsupportedGlyph, SWT.DRAW_TRANSPARENT, SWT.NONE));
		rows.add(new TextRow("Mnemonic (accelerator underline)", SWT.NORMAL, false, false, "&File",
				SWT.DRAW_MNEMONIC | SWT.DRAW_TRANSPARENT, SWT.NONE));
		rows.add(new TextRow("Tab-separated columns", SWT.NORMAL, false, false, "A\tB\tC", SWT.DRAW_TAB,
				SWT.NONE));
		rows.add(new TextRow("Tab-separated columns + unsupported glyph (U+FFFE)", SWT.NORMAL, false, false,
				"A\tB\tC" + unsupportedGlyph, SWT.DRAW_TAB, SWT.NONE));
		rows.add(new TextRow("Kerning pair", "AVATAR WAVE To Yes"));
		rows.add(new TextRow("Mirrored / RTL", SWT.NORMAL, false, false, "Hello World", SWT.DRAW_TRANSPARENT,
				SWT.RIGHT_TO_LEFT));
		rows.add(new TextRow("Arabic", "\u0645\u0631\u062d\u0628\u0627 \u0628\u0627\u0644\u0639\u0627\u0644\u0645"));
		rows.add(new TextRow("Hebrew", "\u05e9\u05dc\u05d5\u05dd \u05e2\u05d5\u05dc\u05dd"));
		rows.add(new TextRow("CJK (Chinese)", "\u4f60\u597d\u4e16\u754c"));
		rows.add(new TextRow("Cyrillic", "\u041f\u0440\u0438\u0432\u0435\u0442 \u043c\u0438\u0440"));
		rows.add(new TextRow("Greek", "\u0393\u03b5\u03b9\u03ac \u03c3\u03bf\u03c5 \u039a\u03cc\u03c3\u03bc\u03b5"));
		rows.add(new TextRow("Combining diacritics", "e\u0301clat na\u0308\u0131ve"));

		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setLayout(new GridLayout());
		shell.setText("Text rendering comparison");

		Label info = new Label(shell, SWT.WRAP);
		info.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

		Button advancedCheckbox = new Button(shell, SWT.CHECK | SWT.WRAP);
		advancedCheckbox.setText("Use GDI+ (advanced) rendering - GC.setAdvanced(true); "
				+ "uncheck to compare against plain GDI (GC.setAdvanced(false))");
		advancedCheckbox.setSelection(true);
		advancedCheckbox.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

		ScrolledComposite scroller = new ScrolledComposite(shell, SWT.V_SCROLL | SWT.BORDER);
		scroller.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		scroller.setExpandHorizontal(true);
		scroller.setExpandVertical(true);

		int rowHeight = 42;
		int labelWidth = 260;
		int sampleWidth = 320;
		int sampleHeight = rowHeight - 4;

		Canvas canvas = new Canvas(scroller, SWT.NONE);
		canvas.setSize(labelWidth + sampleWidth + 20, rows.size() * rowHeight + 10);
		scroller.setContent(canvas);
		scroller.setMinSize(canvas.getSize());

		Font systemFont = display.getSystemFont();
		Map<TextRow, Font> fonts = new HashMap<>();
		for (TextRow row : rows) {
			FontData fontData = systemFont.getFontData()[0];
			fontData.setStyle(row.fontStyle());
			if (row.underline()) fontData.data.lfUnderline = 1;
			if (row.strikeout()) fontData.data.lfStrikeOut = 1;
			fonts.put(row, new Font(display, fontData));
		}

		// Mutable holder so the checkbox listeners can swap in a freshly rendered
		// set of samples (and dispose the previous ones) whenever a toggle changes.
		// Starts out empty; the initial samples are rendered by the refresh below.
		AtomicReference<Map<TextRow, Image>> samplesHolder = new AtomicReference<>(Map.of());

		Runnable refresh = () -> {
			boolean advanced = advancedCheckbox.getSelection();

			Map<TextRow, Image> old = samplesHolder.getAndSet(
					renderSamples(display, rows, fonts, sampleWidth, sampleHeight, advanced));
			old.values().forEach(Image::dispose);

			shell.setText("Text rendering comparison (advanced = " + advanced + ")");
			info.setText("GC.setAdvanced(" + advanced + ")"
					+ "\nToggle the checkbox below to compare plain GDI vs. GDI+ text rendering."
					+ " See the source comment for what to expect per row.");
			shell.layout(true, true);
			canvas.redraw();
		};

		advancedCheckbox.addListener(SWT.Selection, e -> refresh.run());
		refresh.run();

		canvas.addPaintListener(e -> {
			GC gc = e.gc;
			int y = 5;
			for (TextRow row : rows) {
				gc.drawText(row.label(), 5, y + (rowHeight - 4) / 4, SWT.DRAW_TRANSPARENT);
				gc.drawImage(samplesHolder.get().get(row), labelWidth, y);
				y += rowHeight;
			}
		});

		canvas.addDisposeListener(e -> {
			samplesHolder.get().values().forEach(Image::dispose);
			fonts.values().forEach(Font::dispose);
		});

		shell.setSize(700, 500);
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();
	}
}
