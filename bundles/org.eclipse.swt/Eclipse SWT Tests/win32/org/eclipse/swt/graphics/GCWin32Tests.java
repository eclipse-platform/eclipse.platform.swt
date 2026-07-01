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
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.concurrent.*;

import org.eclipse.swt.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.widgets.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;

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
}
