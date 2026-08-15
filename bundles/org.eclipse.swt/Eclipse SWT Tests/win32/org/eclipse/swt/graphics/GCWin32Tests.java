/*******************************************************************************
 * Copyright (c) 2024, 2026 Yatta Solutions
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Yatta Solutions - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.graphics;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;
import java.util.concurrent.*;
import java.util.function.*;
import java.util.stream.*;

import org.eclipse.swt.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.widgets.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

@ExtendWith(PlatformSpecificExecutionExtension.class)
@ExtendWith(WithMonitorSpecificScalingExtension.class)
class GCWin32Tests {

	@Test
	public void gcZoomLevelMustChangeOnShellZoomChange() throws Exception {
		checkGcZoomLevelOnCanvas(DPIUtil.getNativeDeviceZoom());
		checkGcZoomLevelOnCanvas(DPIUtil.getNativeDeviceZoom()*2);
	}

	private void checkGcZoomLevelOnCanvas(int expectedZoom) throws Exception {
		Display display = Display.getDefault();
		Shell shell = new Shell(display);
		CompletableFuture<Integer> gcNativeZoom = new CompletableFuture<>();

		Canvas canvas = new Canvas(shell, SWT.NONE);
		canvas.setSize(20, 20);
		shell.open ();
		canvas.addPaintListener(event -> {
			gcNativeZoom.complete(event.gc.getGCData().nativeZoom);
		});

		DPITestUtil.changeDPIZoom(shell, expectedZoom);
		canvas.update();
		int returnedZoom = (int) gcNativeZoom.get(10000, TimeUnit.SECONDS);
		assertEquals("GCData must have a zoom level equal to the actual zoom level of the widget/shell", expectedZoom, returnedZoom);
		shell.dispose();
	}

	@Test
	public void drawnElementsShouldScaleUpToTheRightZoomLevel() {
		Shell shell = new Shell(Display.getDefault());
		int zoom = shell.nativeZoom;
		int scalingFactor = 2;
		GC gc = GC.win32_new(shell, new GCData());
		gc.getGCData().nativeZoom = zoom * scalingFactor;
		gc.getGCData().lineWidth = 10;
		assertEquals("Drawn elements should scale to the right value", gc.getGCData().lineWidth, gc.getLineWidth() * scalingFactor, 0);
	}

	/**
	 * Regression test for
	 * https://github.com/eclipse-platform/eclipse.platform.swt/issues/3091: an
	 * advanced GC using a font with an underline (or strikeout) decoration drew
	 * no ink at all, i.e. the text was lost entirely rather than merely losing
	 * its decoration.
	 */
	@Test
	public void drawTextWithUnderlinedFontRendersVisibleInk() {
		Display display = Display.getDefault();
		FontData underlinedFontData = display.getSystemFont().getFontData()[0];
		underlinedFontData.data.lfUnderline = 1;
		Font font = new Font(display, underlinedFontData);
		Image image = new Image(display, 100, 100);
		try {
			assertTrue(renderTextAndCountNonWhitePixels(image, font, "Hello World") > 0,
					"an advanced GC must draw visible ink for text in an underlined font");
		} finally {
			image.dispose();
			font.dispose();
		}
	}

	/**
	 * Verifies that underline and strikeout styles requested via a font's
	 * {@link FontData} are preserved when GDI+ cannot find the font family and
	 * constructs a substitute font from the {@code LOGFONT} fields instead.
	 * <p>
	 * "Courier" (without "New") is used because it is a legacy GDI font whose
	 * family is not available in GDI+, triggering the fallback. The fallback
	 * remaps it to "Courier New", which is available to both GDI and GDI+,
	 * ensuring that glyph rendering remains consistent between the two.
	 * <p>
	 * The test string includes U+FFFE, a Unicode non-character with no glyph in
	 * any standard font. Its absence forces SWT to use the GDI+ rendering path
	 * that honours the font's underline and strikeout decoration; the path used
	 * for plain ASCII text does not render those decorations. Advanced (GDI+)
	 * mode must be enabled on the GC so the fallback font's style flags are
	 * applied at all.
	 *
	 * @see <a href="https://github.com/eclipse-platform/eclipse.platform.swt/issues/2978">Issue 2978</a>
	 */
	@Test
	public void fallbackFontPreservesUnderlineAndStrikeout() {
		Display display = Display.getDefault();
		Font normalFont = new Font(display, "Courier", 24, SWT.NORMAL);
		FontData underlineFD = new FontData("Courier", 24, SWT.NORMAL);
		underlineFD.data.lfUnderline = 1;
		Font underlineFont = new Font(display, underlineFD);
		FontData strikeoutFD = new FontData("Courier", 24, SWT.NORMAL);
		strikeoutFD.data.lfStrikeOut = 1;
		Font strikeoutFont = new Font(display, strikeoutFD);
		Image testImage = new Image(display, 400, 100);
		try {
			// U+FFFE has no glyph in any standard font; forces the rendering path
			// that honours font decoration flags such as underline and strikeout.
			String testString = "Hello" + (char) 0xFFFE;
			int normalPixelCount = renderTextAndCountNonWhitePixels(testImage, normalFont, testString);
			int underlinePixelCount = renderTextAndCountNonWhitePixels(testImage, underlineFont, testString);
			int strikeoutPixelCount = renderTextAndCountNonWhitePixels(testImage, strikeoutFont, testString);
			assertAll(
				() -> assertTrue(underlinePixelCount > normalPixelCount,
					"Underline font via fallback path should produce more pixels than normal font. "
					+ "Normal: " + normalPixelCount + ", Underline: " + underlinePixelCount),
				() -> assertTrue(strikeoutPixelCount > normalPixelCount,
					"Strikeout font via fallback path should produce more pixels than normal font. "
					+ "Normal: " + normalPixelCount + ", Strikeout: " + strikeoutPixelCount)
			);
		} finally {
			normalFont.dispose();
			underlineFont.dispose();
			strikeoutFont.dispose();
			testImage.dispose();
		}
	}

	private static int renderTextAndCountNonWhitePixels(Image target, Font font, String text) {
		// advanced mode is required so that font style flags (underline,
		// strikeout) are applied during rendering
		return renderTextAndCountNonWhitePixels(target, font, text, SWT.DRAW_DELIMITER | SWT.DRAW_TAB, SWT.NONE, true);
	}

	/**
	 * U+FFFE is a Unicode non-character that no standard font has a glyph for.
	 * Appending it to a string makes an advanced GC lay that string out with
	 * GDI+ instead of letting GDI compute the glyph positions.
	 * <p>
	 * Since GDI+ text layout became the default for advanced GCs, this is no
	 * longer strictly required. It is kept deliberately so that the tab stop
	 * tests exercise the GDI+ layout path irrespective of the state of the
	 * {@code useGDITextRenderingWithGDIP} system property, which exists to
	 * switch back to GDI-computed glyph positions.
	 */
	private static final String UNSUPPORTED_GLYPH = String.valueOf((char) 0xFFFE);

	/**
	 * Fonts covering both ends of the space-width/average-character-width ratio:
	 * in proportional fonts a space is roughly half the average character width,
	 * while in monospace fonts the two nearly coincide. A tab stop derived from
	 * the space width therefore only misbehaves noticeably for the proportional
	 * ones, so both kinds have to be covered.
	 */
	private static Stream<String> tabStopTestFonts() {
		return Stream.of("Segoe UI", "Arial", "Times New Roman", "Courier New", "Consolas");
	}

	/**
	 * The extents of the GDI and the GDI+ path are each rounded up to whole
	 * pixels independently, so measurements derived from a difference of two
	 * extents may legitimately be off by one pixel.
	 */
	private static final int ROUNDING_TOLERANCE = 1;

	/**
	 * Verifies that a tab is expanded to eight times the font's average
	 * character width, which is the convention Win32's own {@code DrawText()}
	 * and {@code TabbedTextOut()} follow: {@code TabbedTextOut()} is documented
	 * to expand tabs to "eight times the average character width" by default,
	 * and {@code DRAWTEXTPARAMS.iTabLength} is documented to be measured "in
	 * units equal to the average character width".
	 * <p>
	 * This pins down the constant the GDI+ path is expected to reproduce.
	 */
	@ParameterizedTest
	@MethodSource("tabStopTestFonts")
	public void tabStopWidthEqualsEightAverageCharacterWidths(String fontName) {
		Display display = Display.getDefault();
		Image image = new Image(display, 400, 100);
		Font font = new Font(display, fontName, 12, SWT.NORMAL);
		GC gc = new GC(image);
		try {
			gc.setFont(font);
			int averageCharacterWidth = gc.getFontMetrics().handle.tmAveCharWidth;
			assertWithinRoundingTolerance(8 * averageCharacterWidth, measureTabStopWidth(gc),
					"a tab must be expanded to eight average character widths for font " + fontName);
		} finally {
			gc.dispose();
			font.dispose();
			image.dispose();
		}
	}

	/**
	 * Verifies that tab stops are expanded to the same width no matter whether
	 * text is rendered via plain GDI or via GDI+.
	 * <p>
	 * Both paths must use eight times the font's average character width
	 * ({@code TEXTMETRIC.tmAveCharWidth}). The GDI+ path used to derive its tab
	 * stop width from the width of a single space glyph instead. That is a
	 * different metric, not merely a differently computed one: in proportional
	 * fonts a space is roughly half the average character width, so tab stops
	 * came out about half as wide whenever that path was taken.
	 */
	@ParameterizedTest
	@MethodSource("tabStopTestFonts")
	public void tabStopWidthIsConsistentBetweenGdiAndGdipRendering(String fontName) {
		Display display = Display.getDefault();
		Image image = new Image(display, 400, 100);
		Font font = new Font(display, fontName, 12, SWT.NORMAL);
		try {
			int gdiTabStopWidth = withGC(image, font, false, GCWin32Tests::measureTabStopWidth);
			int gdipTabStopWidth = withGC(image, font, true, GCWin32Tests::measureTabStopWidth);
			assertWithinRoundingTolerance(gdiTabStopWidth, gdipTabStopWidth,
					"GDI+ rendering must expand a tab to the same width as GDI rendering for font " + fontName);
		} finally {
			font.dispose();
			image.dispose();
		}
	}

	/**
	 * Verifies that consecutive tabs advance to consecutive tab stops instead of
	 * collapsing into a single one, i.e. that the tab stop width repeats. GDI
	 * derives the repetition from its own {@code (position / width + 1) * width}
	 * calculation, whereas GDI+ gets a single tab stop distance passed to
	 * {@code StringFormat::SetTabStops} and repeats it internally; this asserts
	 * that both arrive at the same layout.
	 */
	@ParameterizedTest
	@MethodSource("tabStopTestFonts")
	public void consecutiveTabsAdvanceByWholeTabStops(String fontName) {
		Display display = Display.getDefault();
		Image image = new Image(display, 800, 100);
		Font font = new Font(display, fontName, 12, SWT.NORMAL);
		try {
			for (boolean advanced : new boolean[] { false, true }) {
				int twoTabStops = withGC(image, font, advanced,
						gc -> measureTabAdvance(gc, "\t\t") );
				int oneTabStop = withGC(image, font, advanced, GCWin32Tests::measureTabStopWidth);
				assertWithinRoundingTolerance(2 * oneTabStop, twoTabStops,
						"two tabs must advance by two tab stops for font " + fontName
						+ " (advanced=" + advanced + ")");
			}
		} finally {
			font.dispose();
			image.dispose();
		}
	}

	/**
	 * Verifies that a tab advances to the next tab stop rather than adding a
	 * fixed amount of space, so that text following a tab starts at the same
	 * column regardless of what precedes the tab within the same tab stop.
	 */
	@ParameterizedTest
	@MethodSource("tabStopTestFonts")
	public void textAfterTabStartsAtSameTabStopRegardlessOfPrecedingText(String fontName) {
		Display display = Display.getDefault();
		Image image = new Image(display, 400, 100);
		Font font = new Font(display, fontName, 12, SWT.NORMAL);
		try {
			for (boolean advanced : new boolean[] { false, true }) {
				int withoutPrefix = withGC(image, font, advanced, gc -> measureExtent(gc, "\tB"));
				int withPrefix = withGC(image, font, advanced, gc -> measureExtent(gc, "A\tB"));
				assertWithinRoundingTolerance(withoutPrefix, withPrefix,
						"text following a tab must start at the same tab stop no matter what precedes the tab, "
						+ "for font " + fontName + " (advanced=" + advanced + ")");
			}
		} finally {
			font.dispose();
			image.dispose();
		}
	}

	/**
	 * Returns the width of a single tab stop, measured as the advance a leading
	 * tab adds. A leading tab always expands to exactly one tab stop, so unlike
	 * a tab in the middle of a string this measurement is not diluted by the
	 * slightly different glyph advances of the GDI and the GDI+ text layout
	 * engine.
	 */
	private static int measureTabStopWidth(GC gc) {
		return measureTabAdvance(gc, "\t");
	}

	/**
	 * Returns the horizontal advance the given leading tabs add to the extent of
	 * the text that follows them.
	 */
	private static int measureTabAdvance(GC gc, String leadingTabs) {
		return measureExtent(gc, leadingTabs + "B") - measureExtent(gc, "B");
	}

	private static int measureExtent(GC gc, String text) {
		// measure in pixels to keep the comparison free of the point/pixel
		// conversion the public API applies at non-100% zoom levels
		return gc.textExtentInPixels(text + UNSUPPORTED_GLYPH, SWT.DRAW_TAB).x;
	}

	private static void assertWithinRoundingTolerance(int expected, int actual, String message) {
		assertTrue(Math.abs(actual - expected) <= ROUNDING_TOLERANCE,
				message + " (expected " + expected + ", was " + actual + ")");
	}

	private static int withGC(Image target, Font font, boolean advanced, ToIntFunction<GC> measurement) {
		GC gc = new GC(target);
		try {
			gc.setFont(font);
			gc.setAdvanced(advanced);
			return measurement.applyAsInt(gc);
		} finally {
			gc.dispose();
		}
	}

	/**
	 * Regression test for the size calculation in scaling/cropping GC.drawImage()
	 * operations with asymmetric source dimensions (smaller height than width) at
	 * fractional zoom levels.
	 * <p>
	 * At fractional zoom levels the effective X and Y scale factors diverge because
	 * each axis is rounded independently (e.g. at 125%:
	 * scaleFactorX&nbsp;=&nbsp;625/500&nbsp;=&nbsp;1.25 but
	 * scaleFactorY&nbsp;=&nbsp;24/19&nbsp;&asymp;&nbsp;1.263).
	 */
	@ParameterizedTest
	@MethodSource("zoomAndHeightArguments")
	public void drawImage_asymmetricDimensionsAtFractionalZoom(int zoom, int height) {
		Display display = Display.getDefault();

		int logicalWidth = 500;
		int logicalHeight = height;

		PaletteData palette = new PaletteData(0xFF0000, 0xFF00, 0xFF);
		ImageData srcData = new ImageData(logicalWidth, logicalHeight, 32, palette);
		for (int y = 0; y < logicalHeight; y++) {
			for (int x = 0; x < logicalWidth; x++) {
				// left half red, right half blue – makes wrong-rectangle errors visible
				srcData.setPixel(x, y, x < logicalWidth / 2 ? 0xFF0000 : 0x0000FF);
			}
		}
		Image srcImage = new Image(display, srcData);

		int previousZoom = DPIUtil.getDeviceZoom();
		try {
			DPIUtil.setDeviceZoom(zoom);

			Image referenceImage = new Image(display, logicalWidth + 5, logicalHeight + 5);
			GC referenceGC = new GC(referenceImage);
			referenceGC.drawImage(srcImage, 0, 0);
			referenceGC.dispose();

			Image testImageScaled = new Image(display, logicalWidth + 5, logicalHeight + 5);
			GC testGC = new GC(testImageScaled);
			testGC.drawImage(srcImage, 0, 0, logicalWidth, logicalHeight);
			testGC.dispose();
			assertArrayEquals(referenceImage.getImageData(zoom).data, testImageScaled.getImageData(zoom).data);
			testImageScaled.dispose();

			Image testImageScaledCropped = new Image(display, logicalWidth + 5, logicalHeight + 5);
			testGC = new GC(testImageScaledCropped);
			testGC.drawImage(srcImage, 0, 0, logicalWidth, logicalHeight, 0, 0, logicalWidth, logicalHeight);
			testGC.dispose();
			assertArrayEquals(referenceImage.getImageData(zoom).data, testImageScaledCropped.getImageData(zoom).data);
			testImageScaledCropped.dispose();

			referenceImage.dispose();
		} finally {
			DPIUtil.setDeviceZoom(previousZoom);
			srcImage.dispose();
		}
	}

	private static Stream<Arguments> zoomAndHeightArguments() {
		int[] zooms = { 25, 50, 75, 100, 125, 150, 175, 200 };
		int[] heights = IntStream.rangeClosed(4, 20).toArray();
		return Arrays.stream(zooms).boxed()
				.flatMap(zoom -> Arrays.stream(heights).mapToObj(height -> Arguments.of(zoom, height)));
	}

	/**
	 * Verifies that an advanced GC applies a font's underline and strikeout
	 * decoration for every combination of weight and slant, not just for the
	 * plain, non-bold, non-italic case. Decorated text must produce more ink
	 * than the same text in the same weight and slant without decoration.
	 */
	@ParameterizedTest(name = "{3}")
	@MethodSource("styleDecorationCombinations")
	public void drawTextRendersFontDecorationForStyleCombinations(int styleBits, boolean underline,
			boolean strikeout, String description) {
		Display display = Display.getDefault();
		FontData decoratedFontData = display.getSystemFont().getFontData()[0];
		decoratedFontData.setStyle(styleBits);
		if (underline) decoratedFontData.data.lfUnderline = 1;
		if (strikeout) decoratedFontData.data.lfStrikeOut = 1;
		Font decoratedFont = new Font(display, decoratedFontData);
		FontData plainFontData = display.getSystemFont().getFontData()[0];
		plainFontData.setStyle(styleBits);
		Font plainFont = new Font(display, plainFontData);
		Image image = new Image(display, 150, 100);
		try {
			int pixelsWithDecoration = renderTextAndCountNonWhitePixels(image, decoratedFont, "Hello");
			int pixelsWithoutDecoration = renderTextAndCountNonWhitePixels(image, plainFont, "Hello");

			assertTrue(pixelsWithDecoration > pixelsWithoutDecoration,
					"Text decorated with " + description + " must produce more ink than the undecorated text "
					+ "(decorated: " + pixelsWithDecoration + ", undecorated: " + pixelsWithoutDecoration + ")");
		} finally {
			decoratedFont.dispose();
			plainFont.dispose();
			image.dispose();
		}
	}

	private static Stream<Arguments> styleDecorationCombinations() {
		return Stream.of(
			Arguments.of(SWT.NORMAL, true, false, "normal weight + underline"),
			Arguments.of(SWT.BOLD, true, false, "bold + underline"),
			Arguments.of(SWT.ITALIC, false, true, "italic + strikeout"),
			Arguments.of(SWT.BOLD | SWT.ITALIC, true, true, "bold + italic + underline + strikeout")
		);
	}

	/**
	 * Verifies that an advanced GC underlines the mnemonic (accelerator)
	 * character requested via {@link SWT#DRAW_MNEMONIC}, which must produce
	 * visibly more ink than the same text without a mnemonic.
	 */
	@Test
	public void drawTextMnemonicUnderlineAddsVisibleInk() {
		Display display = Display.getDefault();
		Font font = display.getSystemFont();
		Image image = new Image(display, 150, 60);
		try {
			int pixelsWithMnemonic = renderTextAndCountNonWhitePixels(image, font, "&File",
					SWT.DRAW_MNEMONIC | SWT.DRAW_TRANSPARENT, SWT.NONE, true);
			int pixelsWithoutMnemonic = renderTextAndCountNonWhitePixels(image, font, "File",
					SWT.DRAW_TRANSPARENT, SWT.NONE, true);

			assertTrue(pixelsWithMnemonic > pixelsWithoutMnemonic,
					"Mnemonic underline should add visible ink (with mnemonic: " + pixelsWithMnemonic
					+ ", without: " + pixelsWithoutMnemonic + ")");
		} finally {
			image.dispose();
		}
	}

	/**
	 * Verifies that mirrored ({@link SWT#RIGHT_TO_LEFT}) text drawn by an
	 * advanced GC covers about the same area as the same mirrored text drawn by
	 * a plain, non-advanced GC, which serves as the reference.
	 */
	@Test
	public void drawTextMirroredStyleRendersInkAreaComparableToGdi() {
		Display display = Display.getDefault();
		Font font = display.getSystemFont();
		Image image = new Image(display, 200, 60);
		try {
			Rectangle gdipInkBounds = renderTextAndGetInkBounds(image, font, "Hello World",
					SWT.DRAW_TRANSPARENT, SWT.RIGHT_TO_LEFT, true);
			Rectangle gdiInkBounds = renderTextAndGetInkBounds(image, font, "Hello World",
					SWT.DRAW_TRANSPARENT, SWT.RIGHT_TO_LEFT, false);

			assertAll(
				() -> assertNotNull(gdipInkBounds, "GDI+ mirrored rendering must draw visible text"),
				() -> assertNotNull(gdiInkBounds, "GDI mirrored rendering must draw visible text")
			);
			// Widths may legitimately differ a bit (different layout engines), but a
			// gross regression (e.g. text collapsed to a sliver, or drawn far wider
			// because mirroring was applied twice) would fall well outside this range.
			assertWithinTolerance("mirrored text ink width", gdipInkBounds.width, gdiInkBounds.width, 0.4);
		} finally {
			image.dispose();
		}
	}

	private static Stream<Arguments> complexScriptsAndCharsets() {
		return Stream.of(
			Arguments.of("Arabic", "\u0645\u0631\u062d\u0628\u0627"),
			Arguments.of("Hebrew", "\u05e9\u05dc\u05d5\u05dd"),
			Arguments.of("CJK (Chinese)", "\u4f60\u597d\u4e16\u754c"),
			Arguments.of("Cyrillic", "\u041f\u0440\u0438\u0432\u0435\u0442"),
			Arguments.of("Greek", "\u0393\u03b5\u03b9\u03ac \u03c3\u03bf\u03c5"),
			Arguments.of("Combining diacritics", "e\u0301clat")
		);
	}

	/**
	 * Verifies that an advanced GC renders visible ink for text in a variety of
	 * scripts and charsets. This is a smoke test only: exact glyph shaping and
	 * positioning is left to the text layout engine, but text must never be
	 * silently dropped.
	 */
	@ParameterizedTest(name = "{0}")
	@MethodSource("complexScriptsAndCharsets")
	public void drawTextComplexScriptsAndCharsetsRenderVisibleInk(String description, String text) {
		Display display = Display.getDefault();
		Font font = display.getSystemFont();
		Image image = new Image(display, 200, 60);
		try {
			int renderedPixels = renderTextAndCountNonWhitePixels(image, font, text);

			assertTrue(renderedPixels > 0, "GDI+ rendering must draw visible ink for " + description);
		} finally {
			image.dispose();
		}
	}

	/**
	 * Verifies that a kerning-sensitive string drawn by an advanced GC comes
	 * out about as wide as the same string drawn by a plain, non-advanced GC,
	 * which serves as the reference. Minor differences in advances and kerning
	 * are expected, whereas glyphs collapsing onto each other or a roughly
	 * doubled width would indicate a real layout defect.
	 */
	@Test
	public void drawTextKerningSensitiveTextWidthIsComparableToGdi() {
		Display display = Display.getDefault();
		Font font = display.getSystemFont();
		Image image = new Image(display, 300, 60);
		String kerningSensitiveText = "AVATAR WAVE To Yes";
		try {
			Rectangle gdipInkBounds = renderTextAndGetInkBounds(image, font, kerningSensitiveText,
					SWT.DRAW_TRANSPARENT, SWT.NONE, true);
			Rectangle gdiInkBounds = renderTextAndGetInkBounds(image, font, kerningSensitiveText,
					SWT.DRAW_TRANSPARENT, SWT.NONE, false);

			assertAll(
				() -> assertNotNull(gdipInkBounds, "GDI+ rendering must draw visible text"),
				() -> assertNotNull(gdiInkBounds, "GDI rendering must draw visible text")
			);
			assertWithinTolerance("kerning-sensitive text ink width", gdipInkBounds.width, gdiInkBounds.width, 0.3);
		} finally {
			image.dispose();
		}
	}

	/**
	 * Asserts that {@code actual} is within {@code (1 +/- tolerance)} times
	 * {@code expected}, i.e. flags gross deviations (roughly halved/doubled or
	 * worse) while tolerating the minor differences that are expected between
	 * the text layout of an advanced and a non-advanced GC.
	 */
	private static void assertWithinTolerance(String description, int actual, int expected, double tolerance) {
		int lowerBound = (int) Math.floor(expected * (1 - tolerance));
		int upperBound = (int) Math.ceil(expected * (1 + tolerance));
		assertTrue(actual >= lowerBound && actual <= upperBound,
				"Expected " + description + " (" + actual + ") to be within " + (int) (tolerance * 100)
				+ "% of the reference value (" + expected + "), i.e. in [" + lowerBound + ", " + upperBound + "]");
	}

	private static int renderTextAndCountNonWhitePixels(Image target, Font font, String text, int drawFlags,
			int gcStyle, boolean advanced) {
		renderText(target, font, text, drawFlags, gcStyle, advanced);
		return countNonWhitePixels(target);
	}

	private static Rectangle renderTextAndGetInkBounds(Image target, Font font, String text, int drawFlags,
			int gcStyle, boolean advanced) {
		renderText(target, font, text, drawFlags, gcStyle, advanced);
		return inkBounds(target);
	}

	private static void renderText(Image target, Font font, String text, int drawFlags, int gcStyle,
			boolean advanced) {
		GC testGC = new GC(target, gcStyle);
		try {
			testGC.setAdvanced(advanced);
			testGC.setBackground(new Color(255, 255, 255));
			testGC.fillRectangle(target.getBounds());
			testGC.setForeground(new Color(0, 0, 0));
			testGC.setFont(font);
			testGC.drawText(text, 5, 5, drawFlags);
		} finally {
			testGC.dispose();
		}
	}

	private static int countNonWhitePixels(Image target) {
		ImageData imageData = target.getImageData(DPIUtil.getDeviceZoom());
		int count = 0;
		for (int y = 0; y < imageData.height; y++) {
			for (int x = 0; x < imageData.width; x++) {
				RGB rgb = imageData.palette.getRGB(imageData.getPixel(x, y));
				if (rgb.red != 255 || rgb.green != 255 || rgb.blue != 255) {
					count++;
				}
			}
		}
		return count;
	}

	/**
	 * Returns the bounding box of all non-white pixels in the given image, or
	 * {@code null} if the image is entirely white (i.e. nothing was drawn).
	 */
	private static Rectangle inkBounds(Image target) {
		ImageData imageData = target.getImageData(DPIUtil.getDeviceZoom());
		int minX = Integer.MAX_VALUE, minY = Integer.MAX_VALUE, maxX = -1, maxY = -1;
		for (int y = 0; y < imageData.height; y++) {
			for (int x = 0; x < imageData.width; x++) {
				RGB rgb = imageData.palette.getRGB(imageData.getPixel(x, y));
				if (rgb.red != 255 || rgb.green != 255 || rgb.blue != 255) {
					minX = Math.min(minX, x);
					minY = Math.min(minY, y);
					maxX = Math.max(maxX, x);
					maxY = Math.max(maxY, y);
				}
			}
		}
		if (maxX < 0) return null;
		return new Rectangle(minX, minY, maxX - minX + 1, maxY - minY + 1);
	}
}
