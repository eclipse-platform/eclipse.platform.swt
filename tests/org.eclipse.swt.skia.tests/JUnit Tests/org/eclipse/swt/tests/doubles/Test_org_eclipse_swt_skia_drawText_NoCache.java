package org.eclipse.swt.tests.doubles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.internal.graphics.SkiaGC;
import org.eclipse.swt.internal.graphics.SkiaTextDrawing;
import org.eclipse.swt.tests.skia.SupportedTestPlatforms;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import io.github.humbleui.skija.BlendMode;
import io.github.humbleui.skija.Font;
import io.github.humbleui.skija.FontMgr;
import io.github.humbleui.skija.FontStyle;
import io.github.humbleui.types.Rect;

public class Test_org_eclipse_swt_skia_drawText_NoCache {
	@BeforeAll
	static void checkPlatform() {
		assumeTrue(SupportedTestPlatforms.isSupported(), "Test skipped: Platform not supported");
	}

	@Test
	public void testDrawText() {
		// Check SkiaTextDrawing there the String:
		System.setProperty(SkiaTextDrawing.NO_TEXT_CACHE, "true");

		SkiaResourcesDouble resources = new SkiaResourcesDouble();
		Color foregroundColor = new Color(null, 255, 255, 255);
		resources.setForeground(foregroundColor);
		Color backgroundColor = new Color(null, 0, 0, 0);
		resources.setBackground(backgroundColor);

		// Use a default Skia font (should be available everywhere)
		final FontMgr fm = FontMgr.getDefault();
		var fontStyle = fm.matchFamilyStyle(fm.getFamilyName(0), FontStyle.NORMAL);
		resources.skiaFont = new Font(fontStyle); // Default constructor uses Skia's default typeface

		FontData data = new FontData();
		data.setName("Arial");
		data.setHeight(12);
		data.setStyle(SWT.NORMAL);
		resources.fontData = data;

		final SkSurfaceDouble surface = new SkSurfaceDouble(100, 100, "SurfaceDouble", null);
		final CanvasExtensionDouble ext = new CanvasExtensionDouble();
		var canvas = surface.canvas;
		ext.surface = surface;
		ext.resources = resources;
		final SkiaGC gc = new SkiaGC(ext, SWT.NONE);

		gc.drawText("World", 10, 20);

		var calls = surface.calls;

		System.out.println(calls);

		// Assert that exactly 2 method calls were made
		assertEquals(2, calls.size(), "Expected 2 method calls");

		// Verify first call: drawRect (background rectangle)
		MethodCall firstCall = calls.get(0);
		assertEquals("drawRect", firstCall.methodName, "First call should be drawRect");

		Rect rectParam = (Rect) firstCall.params[0];
		assertEquals(10.0f, rectParam.getLeft(), 0.1f, "Rectangle left should be 10.0");
		assertEquals(20.0f, rectParam.getTop(), 0.1f, "Rectangle top should be 20.0");
		assertEquals(44.0f, rectParam.getRight(), 0.1f, "Rectangle right should be 44.0");
		assertEquals(35.0f, rectParam.getBottom(), 0.1f, "Rectangle bottom should be 35.0");

		// Verify PaintData for drawRect
		PaintData paintCall1 = (PaintData) firstCall.params[1];
		assertEquals(-16777216, paintCall1.color, "drawRect() background color should be black (-16777216)");
		assertEquals(0.0f, paintCall1.strokeWidth, "drawRect() stroke width should be 0");
		assertEquals(4.0f, paintCall1.strokeMiter, "drawRect() stroke miter should be 4.0");
		assertEquals(0, paintCall1.strokeCap, "drawRect() stroke cap should be 0");
		assertEquals(0, paintCall1.strokeJoin, "drawRect() stroke join should be 0");
		assertEquals(0, paintCall1.style, "drawRect() style should be 0");
		assertEquals(255, paintCall1.alpha, "drawRect() alpha should be 255");
		assertEquals(false, paintCall1.antiAlias, "drawRect() antiAlias should be false");
		assertEquals(false, paintCall1.dither, "drawRect() dither should be false");
		assertEquals(BlendMode.SRC_OVER, paintCall1.blendMode, "drawRect() blendMode should be SRC_OVER");

		// Verify second call: drawString (the text)
		MethodCall secondCall = calls.get(1);
		assertEquals("drawString", secondCall.methodName, "Second call should be drawString");
		assertEquals("World", secondCall.params[0], "Text parameter should be 'World'");
		assertEquals(10, secondCall.params[1], "X position should be 10");
		assertEquals(31, secondCall.params[2], "Y position should be 31");

		// FontData validation
		ExtractedFontData fontData = (ExtractedFontData) secondCall.params[3];
		assertEquals("Arial", fontData.name, "Font name should be Arial");
		assertEquals(12.0f, fontData.size, "Font size should be 12");

		// Verify PaintData for drawString
		PaintData paintCall2 = (PaintData) secondCall.params[4];
		assertEquals(-1, paintCall2.color, "drawString() text color should be white (-1)");
		assertEquals(1.0f, paintCall2.strokeWidth, "drawString() stroke width should be 1.0");
		assertEquals(4.0f, paintCall2.strokeMiter, "drawString() stroke miter should be 4.0");
		assertEquals(0, paintCall2.strokeCap, "drawString() stroke cap should be 0");
		assertEquals(0, paintCall2.strokeJoin, "drawString() stroke join should be 0");
		assertEquals(0, paintCall2.style, "drawString() style should be 0");
		assertEquals(255, paintCall2.alpha, "drawString() alpha should be 255");
		assertEquals(true, paintCall2.antiAlias, "drawString() antiAlias should be true");
		assertEquals(false, paintCall2.dither, "drawString() dither should be false");
		assertEquals(BlendMode.SRC_OVER, paintCall2.blendMode, "drawString() blendMode should be SRC_OVER");

		surface.close();
	}

	@Test
	public void testDrawText_Zoom150() {
		// Check SkiaTextDrawing there the String:
		System.setProperty(SkiaTextDrawing.NO_TEXT_CACHE, "true");

		SkiaResourcesDouble resources = new SkiaResourcesDouble(150);
		Color foregroundColor = new Color(null, 255, 255, 255);
		resources.setForeground(foregroundColor);
		Color backgroundColor = new Color(null, 0, 0, 0);
		resources.setBackground(backgroundColor);

		// Use a default Skia font (should be available everywhere)
		final FontMgr fm = FontMgr.getDefault();
		var fontStyle = fm.matchFamilyStyle(fm.getFamilyName(0), FontStyle.NORMAL);
		resources.skiaFont = new Font(fontStyle);

		FontData data = new FontData();
		data.setName("Arial");
		data.setHeight(18); // 12 * 1.5 = 18
		data.setStyle(SWT.NORMAL);
		resources.fontData = data;

		final SkSurfaceDouble surface = new SkSurfaceDouble(150, 150, "SurfaceDouble", null);
		final CanvasExtensionDouble ext = new CanvasExtensionDouble();
		var canvas = surface.canvas;
		ext.surface = surface;
		ext.resources = resources;
		final SkiaGC gc = new SkiaGC(ext, SWT.NONE);

		gc.drawText("World", 10, 20);

		var calls = surface.calls;

		System.out.println(calls);

		// Assert that exactly 2 method calls were made
		assertEquals(2, calls.size(), "Expected 2 method calls");

		// Verify first call: drawRect (background rectangle) at 150% zoom
		MethodCall firstCall = calls.get(0);
		assertEquals("drawRect", firstCall.methodName, "First call should be drawRect");

		Rect rectParam = (Rect) firstCall.params[0];
		// At 150% zoom: 10*1.5=15, 20*1.5=30, right and bottom are calculated differently
		assertEquals(15.0f, rectParam.getLeft(), 0.1f, "Rectangle left should be 15.0");
		assertEquals(30.0f, rectParam.getTop(), 0.1f, "Rectangle top should be 30.0");
		assertEquals(49.0f, rectParam.getRight(), 0.1f, "Rectangle right should be 49.0");
		assertEquals(45.0f, rectParam.getBottom(), 0.1f, "Rectangle bottom should be 45.0");

		// Verify PaintData for drawRect
		PaintData paintCall1 = (PaintData) firstCall.params[1];
		assertEquals(-16777216, paintCall1.color, "drawRect() background color should be black (-16777216)");

		// Verify second call: drawString (the text)
		MethodCall secondCall = calls.get(1);
		assertEquals("drawString", secondCall.methodName, "Second call should be drawString");
		assertEquals("World", secondCall.params[0], "Text parameter should be 'World'");
		// At 150% zoom: x=10*1.5=15
		assertEquals(15, secondCall.params[1], "X position should be 15 (10*1.5)");

		// FontData validation at 150% zoom
		ExtractedFontData fontData = (ExtractedFontData) secondCall.params[3];
		assertEquals("Arial", fontData.name, "Font name should be Arial");
		assertEquals(12.0f, fontData.size, "Font size should be 18 at 150% zoom");

		// Verify PaintData for drawString
		PaintData paintCall2 = (PaintData) secondCall.params[4];
		assertEquals(-1, paintCall2.color, "drawString() text color should be white (-1)");
		assertEquals(1.0f, paintCall2.strokeWidth, "drawString() stroke width should be 1.5 (1.0*1.5)");
		assertEquals(BlendMode.SRC_OVER, paintCall2.blendMode, "drawString() blendMode should be SRC_OVER");

		surface.close();
	}

	@Test
	public void testDrawText_Zoom200() {
		// Check SkiaTextDrawing there the String:
		System.setProperty(SkiaTextDrawing.NO_TEXT_CACHE, "true");

		SkiaResourcesDouble resources = new SkiaResourcesDouble(200);
		Color foregroundColor = new Color(null, 255, 255, 255);
		resources.setForeground(foregroundColor);
		Color backgroundColor = new Color(null, 0, 0, 0);
		resources.setBackground(backgroundColor);

		// Use a default Skia font (should be available everywhere)
		final FontMgr fm = FontMgr.getDefault();
		var fontStyle = fm.matchFamilyStyle(fm.getFamilyName(0), FontStyle.NORMAL);
		resources.skiaFont = new Font(fontStyle);

		FontData data = new FontData();
		data.setName("Arial");
		data.setHeight(24); // 12 * 2.0 = 24
		data.setStyle(SWT.NORMAL);
		resources.fontData = data;

		final SkSurfaceDouble surface = new SkSurfaceDouble(200, 200, "SurfaceDouble", null);
		final CanvasExtensionDouble ext = new CanvasExtensionDouble();
		var canvas = surface.canvas;
		ext.surface = surface;
		ext.resources = resources;
		final SkiaGC gc = new SkiaGC(ext, SWT.NONE);

		gc.drawText("World", 10, 20);

		var calls = surface.calls;

		System.out.println(calls);

		// Assert that exactly 2 method calls were made
		assertEquals(2, calls.size(), "Expected 2 method calls");

		// Verify first call: drawRect (background rectangle) at 200% zoom
		MethodCall firstCall = calls.get(0);
		assertEquals("drawRect", firstCall.methodName, "First call should be drawRect");

		Rect rectParam = (Rect) firstCall.params[0];
		// At 200% zoom: 10*2.0=20, 20*2.0=40, right=44*2.0=88, bottom=35*2.0=70
		assertEquals(20.0f, rectParam.getLeft(), 0.1f, "Rectangle left should be 20.0");
		assertEquals(40.0f, rectParam.getTop(), 0.1f, "Rectangle top should be 40.0");
		assertEquals(54.0f, rectParam.getRight(), 0.1f, "Rectangle right should be 88.0");
		assertEquals(55.0f, rectParam.getBottom(), 0.1f, "Rectangle bottom should be 70.0");

		// Verify PaintData for drawRect
		PaintData paintCall1 = (PaintData) firstCall.params[1];
		assertEquals(-16777216, paintCall1.color, "drawRect() background color should be black (-16777216)");

		// Verify second call: drawString (the text)
		MethodCall secondCall = calls.get(1);
		assertEquals("drawString", secondCall.methodName, "Second call should be drawString");
		assertEquals("World", secondCall.params[0], "Text parameter should be 'World'");
		// At 200% zoom: x=10*2.0=20
		assertEquals(20, secondCall.params[1], "X position should be 20 (10*2.0)");

		// FontData validation at 200% zoom
		ExtractedFontData fontData = (ExtractedFontData) secondCall.params[3];
		assertEquals("Arial", fontData.name, "Font name should be Arial");
		assertEquals(12.0f, fontData.size, "Font size should be 24 at 200% zoom");

		// Verify PaintData for drawString
		PaintData paintCall2 = (PaintData) secondCall.params[4];
		assertEquals(-1, paintCall2.color, "drawString() text color should be white (-1)");
		assertEquals(1.0f, paintCall2.strokeWidth, "drawString() stroke width should be 2.0 (1.0*2.0)");
		assertEquals(BlendMode.SRC_OVER, paintCall2.blendMode, "drawString() blendMode should be SRC_OVER");

		surface.close();
	}
}
