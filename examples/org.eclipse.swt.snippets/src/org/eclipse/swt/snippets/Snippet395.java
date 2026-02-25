/*******************************************************************************
 * Copyright (c) 2026 Eclipse Foundation and others.
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
 * [Win32] GDI+ vs. legacy GDI text rendering snippet, related to
 * https://github.com/eclipse-platform/eclipse.platform.swt/issues/3091 :
 * GC.drawText() was changed to always use GDI+'s own DrawString/text-layout
 * engine while GC.setAdvanced(true) is active, instead of only falling back
 * to it for characters GDI+ cannot shape itself (and otherwise using GDI to
 * compute glyph positions, then drawing them with GDI+'s DrawDriverString).
 * That change affects more than just underline/strikeout: kerning, tab stop
 * width, mnemonic underlining and bidi/mirroring are all computed by a
 * different engine depending on which path is used.
 *
 * This snippet renders a series of text properties, one row per property, so
 * that the different rendering paths can be visually compared without
 * needing to restart the process:
 * - The "Use GDI+ (advanced) rendering" checkbox calls GC.setAdvanced() at
 *   runtime, switching every row between plain GDI (OS.DrawText, used when
 *   advanced is off) and GDI+ (used when advanced is on). The system
 *   property - and therefore the second checkbox - only ever matters while
 *   this one is checked; it is disabled otherwise.
 * - The "Use legacy GDI text rendering" checkbox toggles the
 *   org.eclipse.swt.internal.win32.useGDITextRenderingWithGDIP system
 *   property at runtime (via System.setProperty) and immediately re-renders
 *   all rows, since GC.useGDIP() re-reads the property on every drawText()
 *   call.
 *
 * How to use this snippet (on Windows):
 * 1. Run it. Both checkboxes start as: advanced = checked, legacy = unchecked,
 *    i.e. the default/new behavior is shown (GDI+'s DrawString).
 * 2. Check "Use legacy GDI text rendering" to switch to the legacy fallback
 *    (GDI+'s DrawDriverString with GDI-computed glyph positions) and watch
 *    which rows change. Uncheck it to switch back. Keep toggling to compare.
 * 3. Uncheck "Use GDI+ (advanced) rendering" to compare against plain GDI
 *    text rendering (OS.DrawText) altogether - this is the code path used
 *    whenever a GC is not advanced, both before and after the #3091 fix, and
 *    is unaffected by the system property (the legacy checkbox is disabled
 *    while unchecked).
 * 4. What to expect while toggling:
 *    - "Underlined"/"Strikeout"/"Bold + underlined" rows are expected to go
 *      blank only when GDI+/advanced is on AND legacy GDI text rendering is
 *      checked (the historical bug from #3091); they should render visibly
 *      with plain GDI (advanced off) and with the default GDI+ path.
 *    - "Mnemonic" should show an underlined "F" in all combinations (it does
 *      not depend on font-level decoration).
 *    - "Kerning pair", "Tab-separated columns" and the "Mirrored / RTL" row
 *      may show differences in spacing/positioning between plain GDI, GDI+
 *      default and GDI+ legacy fallback (expected, since each combination
 *      uses a different layout engine), but should never render blank,
 *      wildly stretched/compressed, or with
 *      overlapping glyphs.
 *    - The script/charset rows (Arabic, Hebrew, CJK, Cyrillic, Greek,
 *      combining diacritics) should render recognisable, visible glyphs
 *      regardless of the checkbox state.
 *
 * On platforms other than Windows the system property has no effect and all
 * rows are expected to render identically regardless of the checkbox state.
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */
public class Snippet395 {

	static final String USE_GDI_TEXT_RENDERING_WITH_GDIP_PROPERTY =
			"org.eclipse.swt.internal.win32.useGDITextRenderingWithGDIP";

	/** One row of the comparison: a label, the text properties to apply, and how to draw it. */
	record TextRow(String label, int fontStyle, boolean underline, boolean strikeout, String text, int drawFlags,
			int gcStyle) {
		TextRow(String label, String text) {
			this(label, SWT.NORMAL, false, false, text, SWT.DRAW_TRANSPARENT, SWT.NONE);
		}
	}

	/**
	 * Renders one fresh sample {@link Image} per row, reflecting whatever the
	 * {@link #USE_GDI_TEXT_RENDERING_WITH_GDIP_PROPERTY} system property is set
	 * to right now, and whether GDI+ (advanced) rendering is requested at all.
	 * When {@code advanced} is {@code false}, GC always uses plain GDI
	 * ({@code OS.DrawText}) regardless of the system property - the property
	 * (and the fix/regression it is about) only ever applies to the advanced/
	 * GDI+ code path. Callers are responsible for disposing the previous set
	 * of images returned by an earlier call.
	 */
	private static Map<TextRow, Image> renderSamples(Display display, java.util.List<TextRow> rows,
			Map<TextRow, Font> fonts, int sampleWidth, int sampleHeight, boolean advanced) {
		Map<TextRow, Image> samples = new HashMap<>();
		for (TextRow row : rows) {
			Image sample = new Image(display, sampleWidth, sampleHeight);
			GC gc = new GC(sample, row.gcStyle());
			gc.setAdvanced(advanced);
			gc.setBackground(new Color(255, 255, 255));
			gc.fillRectangle(sample.getBounds());
			gc.setForeground(new Color(0, 0, 0));
			gc.setFont(fonts.get(row));
			gc.drawText(row.text(), 5, 5, row.drawFlags());
			gc.dispose();
			samples.put(row, sample);
		}
		return samples;
	}

	public static void main(String[] args) {
		// U+FFFE has no glyph in any standard font: even in the legacy fallback,
		// SWT must still use GDI+'s DrawString/DrawDriverString for it.
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
		shell.setText("GDI+ text rendering comparison");

		Label info = new Label(shell, SWT.WRAP);
		info.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

		Button advancedCheckbox = new Button(shell, SWT.CHECK);
		advancedCheckbox.setText("Use GDI+ (advanced) rendering - GC.setAdvanced(true); "
				+ "uncheck to compare against plain GDI (GC.setAdvanced(false))");
		advancedCheckbox.setSelection(true);
		advancedCheckbox.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

		Button legacyCheckbox = new Button(shell, SWT.CHECK);
		legacyCheckbox.setText("Use legacy GDI text rendering (org.eclipse.swt.internal.win32."
				+ "useGDITextRenderingWithGDIP = true) - historical, pre-#3091-fix behavior");
		legacyCheckbox.setSelection(Boolean.getBoolean(USE_GDI_TEXT_RENDERING_WITH_GDIP_PROPERTY));
		legacyCheckbox.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		// The property only ever affects the advanced/GDI+ code path, so the
		// checkbox is meaningless (and disabled) while advanced rendering is off.
		legacyCheckbox.setEnabled(advancedCheckbox.getSelection());

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
		// set of samples (and dispose the previous ones) whenever the system
		// property or the advanced/GDI+ toggle changes.
		AtomicReference<Map<TextRow, Image>> samplesHolder = new AtomicReference<>(
				renderSamples(display, rows, fonts, sampleWidth, sampleHeight, advancedCheckbox.getSelection()));

		Runnable refresh = () -> {
			boolean advanced = advancedCheckbox.getSelection();
			legacyCheckbox.setEnabled(advanced);
			boolean legacyFallback = advanced && legacyCheckbox.getSelection();
			System.setProperty(USE_GDI_TEXT_RENDERING_WITH_GDIP_PROPERTY, Boolean.toString(legacyFallback));

			Map<TextRow, Image> old = samplesHolder.getAndSet(
					renderSamples(display, rows, fonts, sampleWidth, sampleHeight, advanced));
			old.values().forEach(Image::dispose);

			shell.setText("GDI+ text rendering comparison (advanced = " + advanced
					+ ", legacy GDI fallback property = " + legacyFallback + ")");
			info.setText("GC.setAdvanced(" + advanced + "); system property "
					+ USE_GDI_TEXT_RENDERING_WITH_GDIP_PROPERTY + " = " + legacyFallback
					+ "\nToggle the checkboxes above to compare plain GDI vs. GDI+ rendering, and - while GDI+ is"
					+ " enabled - the default (GDI+ DrawString) vs. legacy (GDI + DrawDriverString) text"
					+ " rendering path. See the source comment for what to expect per row.");
			shell.layout(true, true);
			canvas.redraw();
		};

		advancedCheckbox.addListener(SWT.Selection, e -> refresh.run());
		legacyCheckbox.addListener(SWT.Selection, e -> refresh.run());
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
