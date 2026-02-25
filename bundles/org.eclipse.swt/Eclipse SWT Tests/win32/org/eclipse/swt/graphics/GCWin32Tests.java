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
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.concurrent.*;
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
		assertEquals("Drawn elements should scale to the right value", gc.getGCData().lineWidth,
				gc.getLineWidth() * scalingFactor, 0);
	}

	/**
	 * Regression test for https://github.com/eclipse-platform/eclipse.platform.swt/issues/3091
	 */
	@Test
	public void test_drawTextLjava_lang_StringII_advanced_unterlined() {
		Display display = Display.getDefault();
		FontData defaultFontFata = display.getSystemFont().getFontData()[0];
		defaultFontFata.data.lfUnderline = 1;
		Font font = new Font(display, defaultFontFata);
		Image image = new Image(display, 100, 100);
		try {
			GC gc = new GC(image);
			gc.setFont(font);
			gc.setAdvanced(true);
			gc.drawText("Hello World", 10, 50);
			ImageData imageData = image.getImageData();
			boolean anyPixelUnderTextIsNotEmpty = false;
			for (int i = 0; i < imageData.width * imageData.height; i++) {
				if (imageData.getPixel(i % imageData.width, i / imageData.width) != -1) {
					anyPixelUnderTextIsNotEmpty = true;
					break;
				}
			}
			assertTrue(anyPixelUnderTextIsNotEmpty);
		} finally {
			image.dispose();
			font.dispose();
		}
	}

	private static final String USE_GDI_TEXT_RENDERING_WITH_GDIP_PROPERTY =
			"org.eclipse.swt.internal.win32.useGDITextRenderingWithGDIP";

	/**
	 * End-to-end counterpart to
	 * {@link #test_drawTextLjava_lang_StringII_advanced_unterlined()}: it
	 * compares the actual rendered output of the default (fixed) behavior with
	 * the legacy behavior restored via the
	 * {@code org.eclipse.swt.internal.win32.useGDITextRenderingWithGDIP} system
	 * property, for the exact scenario reported in
	 * https://github.com/eclipse-platform/eclipse.platform.swt/issues/3091 (an
	 * underlined font drawn with {@code GC.setAdvanced(true)}).
	 * <p>
	 * This both confirms that the default behavior is fixed (text is visible)
	 * and that the escape-hatch property still reproduces the historical,
	 * broken behavior (no text drawn at all), so that any unintended change to
	 * either path is caught.
	 */
	@Test
	public void drawTextAdvancedUnderlinePropertyTogglesLegacyFallback() {
		Display display = Display.getDefault();
		FontData underlineFontData = display.getSystemFont().getFontData()[0];
		underlineFontData.data.lfUnderline = 1;
		Font underlineFont = new Font(display, underlineFontData);
		Image image = new Image(display, 100, 100);
		try {
			System.clearProperty(USE_GDI_TEXT_RENDERING_WITH_GDIP_PROPERTY);
			int pixelsWithGdipDefault = renderTextAndCountNonWhitePixels(image, underlineFont, "Hello World");

			System.setProperty(USE_GDI_TEXT_RENDERING_WITH_GDIP_PROPERTY, "true");
			int pixelsWithLegacyFallback = renderTextAndCountNonWhitePixels(image, underlineFont, "Hello World");

			assertAll(
				() -> assertTrue(pixelsWithGdipDefault > 0,
					"Default (GDI+) rendering must draw the underlined text (regression for #3091)"),
				() -> assertEquals("Legacy GDI fallback (property=true) historically drew nothing at all for "
						+ "underlined fonts in advanced mode; the property must still reproduce that behavior",
						0, pixelsWithLegacyFallback)
			);
		} finally {
			System.clearProperty(USE_GDI_TEXT_RENDERING_WITH_GDIP_PROPERTY);
			underlineFont.dispose();
			image.dispose();
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
		GC testGC = new GC(target);
		try {
			testGC.setAdvanced(true); // required so font style flags (underline, strikeout) are applied during rendering
			testGC.setBackground(new Color(255, 255, 255));
			testGC.fillRectangle(target.getBounds());
			testGC.setForeground(new Color(0, 0, 0));
			testGC.setFont(font);
			testGC.drawText(text, 5, 5);
		} finally {
			testGC.dispose();
		}
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

	// ------------------------------------------------------------------
	// Broader coverage: the fix for #3091 changes drawText() from computing
	// glyph positions with GDI (GetCharacterPlacement) and drawing them with
	// Gdip.Graphics_DrawDriverString, to fully delegating layout and drawing
	// to Gdip.Graphics_DrawString. Both are still GDI+ calls, but DrawString
	// additionally honours font decorations (underline/strikeout) and uses
	// GDI+'s own text layout engine (kerning, complex script shaping,
	// mnemonic hotkey prefix, bidi/mirroring), whereas DrawDriverString only
	// draws glyphs at explicit, GDI-computed positions. The tests below probe
	// several of those text properties, comparing the default (GDI+
	// DrawString) path with the legacy fallback (GDI DrawDriverString,
	// restored via the escape-hatch system property) to catch regressions in
	// either direction.
	// ------------------------------------------------------------------

	private static Stream<Arguments> styleDecorationCombinations() {
		return Stream.of(
			Arguments.of(SWT.NORMAL, true, false, "normal weight + underline"),
			Arguments.of(SWT.BOLD, true, false, "bold + underline"),
			Arguments.of(SWT.ITALIC, false, true, "italic + strikeout"),
			Arguments.of(SWT.BOLD | SWT.ITALIC, true, true, "bold + italic + underline + strikeout")
		);
	}

	/**
	 * Extends {@link #drawTextAdvancedUnderlinePropertyTogglesLegacyFallback()}
	 * to combinations of weight/style (bold, italic) with underline and/or
	 * strikeout, to ensure the fix is not accidentally narrow to the single
	 * plain+underline scenario reported in
	 * https://github.com/eclipse-platform/eclipse.platform.swt/issues/3091.
	 */
	@ParameterizedTest(name = "{3}")
	@MethodSource("styleDecorationCombinations")
	public void drawTextStyleCombinationsWithDecorationDifferBetweenRenderingPaths(int styleBits, boolean underline,
			boolean strikeout, String description) {
		Display display = Display.getDefault();
		FontData fontData = display.getSystemFont().getFontData()[0];
		fontData.setStyle(styleBits);
		if (underline) fontData.data.lfUnderline = 1;
		if (strikeout) fontData.data.lfStrikeOut = 1;
		Font font = new Font(display, fontData);
		Image image = new Image(display, 150, 100);
		try {
			System.clearProperty(USE_GDI_TEXT_RENDERING_WITH_GDIP_PROPERTY);
			int pixelsWithGdipDefault = renderTextAndCountNonWhitePixels(image, font, "Hello");

			System.setProperty(USE_GDI_TEXT_RENDERING_WITH_GDIP_PROPERTY, "true");
			int pixelsWithLegacyFallback = renderTextAndCountNonWhitePixels(image, font, "Hello");

			assertAll(description,
				() -> assertTrue(pixelsWithGdipDefault > 0,
					"Default (GDI+) rendering must draw text decorated with " + description),
				() -> assertEquals("Legacy GDI fallback must reproduce the historical bug of drawing nothing for "
						+ description, 0, pixelsWithLegacyFallback)
			);
		} finally {
			System.clearProperty(USE_GDI_TEXT_RENDERING_WITH_GDIP_PROPERTY);
			font.dispose();
			image.dispose();
		}
	}

	/**
	 * Verifies that mnemonic (accelerator) underline drawing, which is
	 * implemented completely differently in the two rendering paths (GDI+'s
	 * built-in hotkey-prefix support in the default path vs. a manually drawn
	 * line in the legacy fallback), still produces visibly more ink than the
	 * same text without a mnemonic, on both paths. Unlike font-level
	 * underline/strikeout, mnemonic underlining does not rely on advanced-mode
	 * font decoration and historically worked in both rendering paths, so this
	 * guards against the switch introducing a regression there.
	 */
	@Test
	public void drawTextMnemonicUnderlineIsPreservedAcrossRenderingPaths() {
		Display display = Display.getDefault();
		Font font = display.getSystemFont();
		Image image = new Image(display, 150, 60);
		try {
			for (boolean legacyFallback : new boolean[] { false, true }) {
				if (legacyFallback) {
					System.setProperty(USE_GDI_TEXT_RENDERING_WITH_GDIP_PROPERTY, "true");
				} else {
					System.clearProperty(USE_GDI_TEXT_RENDERING_WITH_GDIP_PROPERTY);
				}
				int pixelsWithMnemonic = renderTextAndCountNonWhitePixels(image, font, "&File",
						SWT.DRAW_MNEMONIC | SWT.DRAW_TRANSPARENT, SWT.NONE);
				int pixelsWithoutMnemonic = renderTextAndCountNonWhitePixels(image, font, "File",
						SWT.DRAW_TRANSPARENT, SWT.NONE);
				String pathDescription = legacyFallback ? "legacy GDI fallback" : "default GDI+";
				assertTrue(pixelsWithMnemonic > pixelsWithoutMnemonic,
						"Mnemonic underline should add visible ink under " + pathDescription + " rendering "
						+ "(with mnemonic: " + pixelsWithMnemonic + ", without: " + pixelsWithoutMnemonic + ")");
			}
		} finally {
			System.clearProperty(USE_GDI_TEXT_RENDERING_WITH_GDIP_PROPERTY);
			image.dispose();
		}
	}

	/**
	 * Verifies that mirrored (right-to-left) rendering, which the two paths
	 * implement differently (manual coordinate/brush transforms for
	 * DrawDriverString vs. {@code StringFormatFlagsDirectionRightToLeft} for
	 * DrawString), still produces a visible, similarly sized block of ink on
	 * both the default and the legacy-fallback rendering path.
	 */
	@Test
	public void drawTextMirroredStyleRendersConsistentInkAreaAcrossRenderingPaths() {
		Display display = Display.getDefault();
		Font font = display.getSystemFont();
		Image image = new Image(display, 200, 60);
		try {
			System.clearProperty(USE_GDI_TEXT_RENDERING_WITH_GDIP_PROPERTY);
			Rectangle gdipInkBounds = renderTextAndGetInkBounds(image, font, "Hello World",
					SWT.DRAW_TRANSPARENT, SWT.RIGHT_TO_LEFT);

			System.setProperty(USE_GDI_TEXT_RENDERING_WITH_GDIP_PROPERTY, "true");
			Rectangle legacyInkBounds = renderTextAndGetInkBounds(image, font, "Hello World",
					SWT.DRAW_TRANSPARENT, SWT.RIGHT_TO_LEFT);

			assertAll(
				() -> assertNotNull(gdipInkBounds, "Default (GDI+) mirrored rendering must draw visible text"),
				() -> assertNotNull(legacyInkBounds, "Legacy fallback mirrored rendering must draw visible text")
			);
			// Widths may legitimately differ a bit (different layout engines), but a
			// gross regression (e.g. text collapsed to a sliver, or drawn far wider
			// because mirroring was applied twice) would fall well outside this range.
			assertWithinTolerance("mirrored text ink width", legacyInkBounds.width, gdipInkBounds.width, 0.4);
		} finally {
			System.clearProperty(USE_GDI_TEXT_RENDERING_WITH_GDIP_PROPERTY);
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
	 * Verifies that plain (non-decorated) text in a variety of scripts and
	 * charsets still renders visible ink on both the default (GDI+
	 * DrawString) and legacy fallback (GDI+ DrawDriverString) rendering
	 * paths. This is a smoke test only: exact glyph shaping/positioning is
	 * expected to differ slightly between the two layout engines, but neither
	 * path should silently drop the text, which would be a much bigger
	 * regression than the underline/strikeout issue this change fixes.
	 */
	@ParameterizedTest(name = "{0}")
	@MethodSource("complexScriptsAndCharsets")
	public void drawTextComplexScriptsAndCharsetsRenderVisibleInkOnBothPaths(String description, String text) {
		Display display = Display.getDefault();
		Font font = display.getSystemFont();
		Image image = new Image(display, 200, 60);
		try {
			System.clearProperty(USE_GDI_TEXT_RENDERING_WITH_GDIP_PROPERTY);
			int pixelsWithGdipDefault = renderTextAndCountNonWhitePixels(image, font, text);

			System.setProperty(USE_GDI_TEXT_RENDERING_WITH_GDIP_PROPERTY, "true");
			int pixelsWithLegacyFallback = renderTextAndCountNonWhitePixels(image, font, text);

			assertAll(description,
				() -> assertTrue(pixelsWithGdipDefault > 0,
					"Default (GDI+) rendering must draw visible ink for " + description),
				() -> assertTrue(pixelsWithLegacyFallback > 0,
					"Legacy fallback rendering must draw visible ink for " + description)
			);
		} finally {
			System.clearProperty(USE_GDI_TEXT_RENDERING_WITH_GDIP_PROPERTY);
			image.dispose();
		}
	}

	/**
	 * Compares the ink width of a kerning-sensitive string between the two
	 * rendering paths. GDI+'s own typographic layout (default path) and GDI's
	 * character-placement-based layout (legacy fallback) are expected to
	 * compute slightly different advances/kerning, but a gross regression
	 * (e.g. glyphs overlapping to near-zero width, or width roughly doubling)
	 * would indicate a real layout bug rather than an expected, minor
	 * kerning difference.
	 */
	@Test
	public void drawTextKerningSensitiveTextWidthRemainsPlausibleBetweenRenderingPaths() {
		Display display = Display.getDefault();
		Font font = display.getSystemFont();
		Image image = new Image(display, 300, 60);
		String kerningSensitiveText = "AVATAR WAVE To Yes";
		try {
			System.clearProperty(USE_GDI_TEXT_RENDERING_WITH_GDIP_PROPERTY);
			Rectangle gdipInkBounds = renderTextAndGetInkBounds(image, font, kerningSensitiveText,
					SWT.DRAW_TRANSPARENT, SWT.NONE);

			System.setProperty(USE_GDI_TEXT_RENDERING_WITH_GDIP_PROPERTY, "true");
			Rectangle legacyInkBounds = renderTextAndGetInkBounds(image, font, kerningSensitiveText,
					SWT.DRAW_TRANSPARENT, SWT.NONE);

			assertAll(
				() -> assertNotNull(gdipInkBounds, "Default (GDI+) rendering must draw visible text"),
				() -> assertNotNull(legacyInkBounds, "Legacy fallback rendering must draw visible text")
			);
			assertWithinTolerance("kerning-sensitive text ink width", legacyInkBounds.width, gdipInkBounds.width, 0.3);
		} finally {
			System.clearProperty(USE_GDI_TEXT_RENDERING_WITH_GDIP_PROPERTY);
			image.dispose();
		}
	}

	/**
	 * Verifies that tab expansion is computed identically by both rendering
	 * paths. Both use {@code lptm.tmAveCharWidth * 8} pixels (the Win32
	 * DrawText()/TabbedTextOut() convention) for the tab stop width: the
	 * legacy fallback via {@code OS.GetTextMetrics}, and the default GDI+
	 * path via the {@code TEXTMETRIC} computed by the caller before dispatch.
	 * (An earlier version of the GDI+ path used 8x the GDI+-measured space
	 * glyph width instead, which produced noticeably narrower/tab stops than
	 * plain GDI and the legacy fallback - see the regression test below.)
	 */
	@Test
	public void drawTextTabStopsExpandToComparablePositionsBetweenRenderingPaths() {
		Display display = Display.getDefault();
		Font font = display.getSystemFont();
		Image image = new Image(display, 300, 60);
		try {
			System.clearProperty(USE_GDI_TEXT_RENDERING_WITH_GDIP_PROPERTY);
			Rectangle gdipInkBounds = renderTextAndGetInkBounds(image, font, "A\tB", SWT.DRAW_TAB, SWT.NONE);

			System.setProperty(USE_GDI_TEXT_RENDERING_WITH_GDIP_PROPERTY, "true");
			Rectangle legacyInkBounds = renderTextAndGetInkBounds(image, font, "A\tB", SWT.DRAW_TAB, SWT.NONE);

			assertAll(
				() -> assertNotNull(gdipInkBounds, "Default (GDI+) rendering must draw text after a tab"),
				() -> assertNotNull(legacyInkBounds, "Legacy fallback rendering must draw text after a tab")
			);
			// The tab should push "B" noticeably to the right of "A" in both paths.
			int minimumExpectedTabAdvance = 20;
			assertAll(
				() -> assertTrue(gdipInkBounds.width > minimumExpectedTabAdvance,
					"Default (GDI+) rendering should expand the tab to a visible column, got width "
					+ gdipInkBounds.width),
				() -> assertTrue(legacyInkBounds.width > minimumExpectedTabAdvance,
					"Legacy fallback rendering should expand the tab to a visible column, got width "
					+ legacyInkBounds.width)
			);
			// Both paths now use the same tab stop metric, so the ink width should
			// be nearly identical; allow a small tolerance for minor per-glyph
			// rendering/rounding differences between DrawString and DrawDriverString.
			assertWithinTolerance("tab-expanded text ink width", legacyInkBounds.width, gdipInkBounds.width, 0.15);
		} finally {
			System.clearProperty(USE_GDI_TEXT_RENDERING_WITH_GDIP_PROPERTY);
			image.dispose();
		}
	}

	/**
	 * Asserts that {@code actual} is within {@code (1 +/- tolerance)} times
	 * {@code expected}, i.e. flags gross regressions (roughly halved/doubled
	 * or worse) while tolerating the minor layout differences that are
	 * expected between GDI+'s own text layout and GDI-computed glyph
	 * placement.
	 */
	private static void assertWithinTolerance(String description, int actual, int expected, double tolerance) {
		int lowerBound = (int) Math.floor(expected * (1 - tolerance));
		int upperBound = (int) Math.ceil(expected * (1 + tolerance));
		assertTrue(actual >= lowerBound && actual <= upperBound,
				"Expected " + description + " (" + actual + ") to be within " + (int) (tolerance * 100)
				+ "% of the reference value (" + expected + "), i.e. in [" + lowerBound + ", " + upperBound + "]");
	}

	private static int renderTextAndCountNonWhitePixels(Image target, Font font, String text, int drawFlags,
			int gcStyle) {
		renderText(target, font, text, drawFlags, gcStyle);
		return countNonWhitePixels(target);
	}

	private static Rectangle renderTextAndGetInkBounds(Image target, Font font, String text, int drawFlags,
			int gcStyle) {
		renderText(target, font, text, drawFlags, gcStyle);
		return inkBounds(target);
	}

	private static void renderText(Image target, Font font, String text, int drawFlags, int gcStyle) {
		GC testGC = new GC(target, gcStyle);
		try {
			testGC.setAdvanced(true);
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
