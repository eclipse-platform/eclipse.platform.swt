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

	/**
	 * U+FFFE is a Unicode non-character that no standard font has a glyph for.
	 * Appending it to a string makes an advanced GC lay that string out with
	 * GDI+ instead of letting GDI compute the glyph positions, which is the only
	 * way to exercise the GDI+ tab stop handling.
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
}
