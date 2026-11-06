package org.eclipse.swt.tests.doubles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.internal.graphics.SkiaGC;
import org.eclipse.swt.tests.skia.SupportedTestPlatforms;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import io.github.humbleui.skija.BlendMode;
import io.github.humbleui.skija.Font;
import io.github.humbleui.skija.FontMgr;
import io.github.humbleui.skija.FontStyle;
import io.github.humbleui.types.Rect;

public class Test_org_eclipse_swt_skia_drawString {
	@BeforeAll
	static void checkPlatform() {
		assumeTrue(SupportedTestPlatforms.isSupported(), "Test skipped: Platform not supported");
	}

	@Test
	public void testDrawString() {
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

		gc.drawString("World", 10, 20);

		var calls = surface.calls;

		System.out.println(calls);

		// Assert that exactly 4 method calls were made
		assertEquals(4, calls.size(), "Expected 4 method calls");

		// Call 0: clear() - SurfaceDouble-child-0-canvas.clear(0)
		MethodCall call0 = calls.get(0);
		assertEquals("SurfaceDouble-child-0-canvas", call0.className);
		assertEquals("clear", call0.methodName);
		assertEquals(1, call0.params.length);
		assertEquals(0, call0.params[0], "clear() should be called with 0 (background color)");

		// Call 1: drawRect() - draw the background rectangle
		MethodCall call1 = calls.get(1);
		assertEquals("SurfaceDouble-child-0-canvas", call1.className);
		assertEquals("drawRect", call1.methodName);
		assertEquals(2, call1.params.length);
		// First param: Rect with bounds [0, 0, 34, 15]
		Rect rect = (Rect) call1.params[0];
		assertEquals(0.0f, rect.getLeft(), "drawRect() left should be 0");
		assertEquals(0.0f, rect.getTop(), "drawRect() top should be 0");
		assertEquals(34.0f, rect.getRight(), "drawRect() right should be 34");
		assertEquals(15.0f, rect.getBottom(), "drawRect() bottom should be 15");
		// Second param: PaintData for the background rectangle
		PaintData paintCall1 = (PaintData) call1.params[1];
		assertEquals(-16777216, paintCall1.color, "drawRect() background color should be black (-16777216)");
		assertEquals(0.0f, paintCall1.strokeWidth, "drawRect() stroke width should be 0");
		assertEquals(4.0f, paintCall1.strokeMiter, "drawRect() stroke miter should be 4.0");
		assertEquals(0, paintCall1.strokeCap, "drawRect() stroke cap should be 0");
		assertEquals(0, paintCall1.strokeJoin, "drawRect() stroke join should be 0");
		assertEquals(0, paintCall1.style, "drawRect() style should be 0");
		assertEquals(255, paintCall1.alpha, "drawRect() alpha should be 255");
		assertEquals(true, paintCall1.antiAlias, "drawRect() antiAlias should be true");
		assertEquals(false, paintCall1.dither, "drawRect() dither should be false");
		assertEquals(BlendMode.SRC_OVER, paintCall1.blendMode, "drawRect() blendMode should be SRC_OVER");

		// Call 2: drawString() - draw the string "World"
		MethodCall call2 = calls.get(2);
		assertEquals("SurfaceDouble-child-0-canvas", call2.className);
		assertEquals("drawString", call2.methodName);
		assertEquals(5, call2.params.length);
		// Params: String, x, y, FontData, PaintData
		assertEquals("World", call2.params[0], "drawString() should draw 'World'");
		assertEquals(0, call2.params[1], "drawString() x position should be 0");
		assertEquals(11, call2.params[2], "drawString() y position should be 11");
		// FontData validation
		ExtractedFontData fontData = (ExtractedFontData) call2.params[3];
		assertEquals("Arial", fontData.name, "Font name should be Arial");
		assertEquals(12.0f, fontData.size, "Font size should be 12");
		assertEquals(400, fontData.weight, "Font weight should be 400");
		assertEquals(5, fontData.width, "Font width should be 5");
		assertEquals(0, fontData.slant, "Font slant should be 0");
		assertEquals(2, fontData.ediging, "Font ediging should be 2");
		assertEquals(1.0f, fontData.scaleX, "Font scaleX should be 1.0");
		assertEquals(0.0f, fontData.skewX, "Font skewX should be 0.0");
		// PaintData for text
		PaintData paintCall2 = (PaintData) call2.params[4];
		assertEquals(-1, paintCall2.color, "drawString() text color should be white (-1)");
		assertEquals(1.0f, paintCall2.strokeWidth, "drawString() stroke width should be 1.0");
		assertEquals(4.0f, paintCall2.strokeMiter, "drawString() stroke miter should be 4.0");
		assertEquals(0, paintCall2.strokeCap, "drawString() stroke cap should be 0");
		assertEquals(0, paintCall2.strokeJoin, "drawString() stroke join should be 0");
		assertEquals(0, paintCall2.style, "drawString() style should be 0");
		assertEquals(255, paintCall2.alpha, "drawString() alpha should be 255");
		assertEquals(true, paintCall2.antiAlias, "drawString() antiAlias should be true");
		assertEquals(false, paintCall2.dither, "drawString() dither should be false");
		assertEquals(BlendMode.SRC_IN, paintCall2.blendMode, "drawString() blendMode should be SRC_IN");

		// Call 3: drawImage() - draw the cached image snapshot
		MethodCall call3 = calls.get(3);
		assertEquals("SurfaceDouble-canvas", call3.className);
		assertEquals("drawImage", call3.methodName);
		assertEquals(3, call3.params.length);
		// First param: ISkImageDouble
		ISkImageDouble image = (ISkImageDouble) call3.params[0];
		assertEquals("SurfaceDouble-child-0-snapshot-0", image.name, "drawImage() should use the snapshot image");
		assertEquals(10, call3.params[1], "drawImage() x position should be 10");
		assertEquals(20, call3.params[2], "drawImage() y position should be 20");

		surface.close();
	}

	@Test
	public void testDrawString_Zoom150() {
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
		data.setHeight(12);
		data.setStyle(SWT.NORMAL);
		resources.fontData = data;

		final SkSurfaceDouble surface = new SkSurfaceDouble(150, 150, "SurfaceDouble", null);
		final CanvasExtensionDouble ext = new CanvasExtensionDouble();
		var canvas = surface.canvas;
		ext.surface = surface;
		ext.resources = resources;
		final SkiaGC gc = new SkiaGC(ext, SWT.NONE);

		gc.drawString("World", 10, 20);

		var calls = surface.calls;

		System.out.println(calls);

		// Assert that exactly 4 method calls were made
		assertEquals(4, calls.size(), "Expected 4 method calls");

		// Call 0: clear() - SurfaceDouble-child-0-canvas.clear(0)
		MethodCall call0 = calls.get(0);
		assertEquals("SurfaceDouble-child-0-canvas", call0.className);
		assertEquals("clear", call0.methodName);
		assertEquals(1, call0.params.length);
		assertEquals(0, call0.params[0], "clear() should be called with 0 (background color)");

		// Call 1: drawRect() - draw the background rectangle at 150% zoom
		MethodCall call1 = calls.get(1);
		assertEquals("SurfaceDouble-child-0-canvas", call1.className);
		assertEquals("drawRect", call1.methodName);
		assertEquals(2, call1.params.length);
		// At 150% zoom: bounds stay same [0, 0, 34, 15] - child canvas is not scaled
		Rect rect = (Rect) call1.params[0];
		assertEquals(0.0f, rect.getLeft(), "drawRect() left should be 0");
		assertEquals(0.0f, rect.getTop(), "drawRect() top should be 0");
		assertEquals(34.0f, rect.getRight(), "drawRect() right should be 34");
		assertEquals(15.0f, rect.getBottom(), "drawRect() bottom should be 15");
		PaintData paintCall1 = (PaintData) call1.params[1];
		assertEquals(-16777216, paintCall1.color, "drawRect() background color should be black (-16777216)");
		assertEquals(0.0f, paintCall1.strokeWidth, "drawRect() stroke width should be 0");
		assertEquals(true, paintCall1.antiAlias, "drawRect() antiAlias should be true");

		// Call 2: drawString() - draw the string "World"
		MethodCall call2 = calls.get(2);
		assertEquals("SurfaceDouble-child-0-canvas", call2.className);
		assertEquals("drawString", call2.methodName);
		assertEquals(5, call2.params.length);
		assertEquals("World", call2.params[0], "drawString() should draw 'World'");
		assertEquals(0, call2.params[1], "drawString() x position");
		assertEquals(11, call2.params[2], "drawString() y position");
		// FontData validation at 150% zoom
		ExtractedFontData fontData = (ExtractedFontData) call2.params[3];
		assertEquals("Arial", fontData.name, "Font name should be Arial");
		assertEquals(12.0f, fontData.size, "Font size should be 12");
		// PaintData for text
		PaintData paintCall2 = (PaintData) call2.params[4];
		assertEquals(-1, paintCall2.color, "drawString() text color should be white (-1)");
		assertEquals(1.0f, paintCall2.strokeWidth, "drawString() stroke width should be 1.0");
		assertEquals(true, paintCall2.antiAlias, "drawString() antiAlias should be true");
		assertEquals(BlendMode.SRC_IN, paintCall2.blendMode, "drawString() blendMode should be SRC_IN");

		// Call 3: drawImage() - draw the cached image snapshot
		MethodCall call3 = calls.get(3);
		assertEquals("SurfaceDouble-canvas", call3.className);
		assertEquals("drawImage", call3.methodName);
		assertEquals(3, call3.params.length);
		// Position at 150% zoom: x=10*1.5=15, y=20*1.5=30
		assertEquals(15, call3.params[1], "drawImage() x position should be 15 (10*1.5)");
		assertEquals(30, call3.params[2], "drawImage() y position should be 30 (20*1.5)");

		surface.close();
	}

	@Test
	public void testDrawString_Zoom200() {
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
		data.setHeight(12);
		data.setStyle(SWT.NORMAL);
		resources.fontData = data;

		final SkSurfaceDouble surface = new SkSurfaceDouble(200, 200, "SurfaceDouble", null);
		final CanvasExtensionDouble ext = new CanvasExtensionDouble();
		var canvas = surface.canvas;
		ext.surface = surface;
		ext.resources = resources;
		final SkiaGC gc = new SkiaGC(ext, SWT.NONE);

		gc.drawString("World", 10, 20);

		var calls = surface.calls;

		System.out.println(calls);

		// Assert that exactly 4 method calls were made
		assertEquals(4, calls.size(), "Expected 4 method calls");

		// Call 0: clear() - SurfaceDouble-child-0-canvas.clear(0)
		MethodCall call0 = calls.get(0);
		assertEquals("SurfaceDouble-child-0-canvas", call0.className);
		assertEquals("clear", call0.methodName);
		assertEquals(1, call0.params.length);
		assertEquals(0, call0.params[0], "clear() should be called with 0 (background color)");

		// Call 1: drawRect() - draw the background rectangle at 200% zoom
		MethodCall call1 = calls.get(1);
		assertEquals("SurfaceDouble-child-0-canvas", call1.className);
		assertEquals("drawRect", call1.methodName);
		assertEquals(2, call1.params.length);
		// At 200% zoom: bounds stay same [0, 0, 34, 15] - child canvas is not scaled
		Rect rect = (Rect) call1.params[0];
		assertEquals(0.0f, rect.getLeft(), "drawRect() left should be 0");
		assertEquals(0.0f, rect.getTop(), "drawRect() top should be 0");
		assertEquals(34.0f, rect.getRight(), "drawRect() right should be 34");
		assertEquals(15.0f, rect.getBottom(), "drawRect() bottom should be 15");
		PaintData paintCall1 = (PaintData) call1.params[1];
		assertEquals(-16777216, paintCall1.color, "drawRect() background color should be black (-16777216)");
		assertEquals(0.0f, paintCall1.strokeWidth, "drawRect() stroke width should be 0");
		assertEquals(true, paintCall1.antiAlias, "drawRect() antiAlias should be true");

		// Call 2: drawString() - draw the string "World"
		MethodCall call2 = calls.get(2);
		assertEquals("SurfaceDouble-child-0-canvas", call2.className);
		assertEquals("drawString", call2.methodName);
		assertEquals(5, call2.params.length);
		assertEquals("World", call2.params[0], "drawString() should draw 'World'");
		assertEquals(0, call2.params[1], "drawString() x position");
		assertEquals(11, call2.params[2], "drawString() y position");
		// FontData validation at 200% zoom
		ExtractedFontData fontData = (ExtractedFontData) call2.params[3];
		assertEquals("Arial", fontData.name, "Font name should be Arial");
		assertEquals(12.0f, fontData.size, "Font size should be 12");
		// PaintData for text
		PaintData paintCall2 = (PaintData) call2.params[4];
		assertEquals(-1, paintCall2.color, "drawString() text color should be white (-1)");
		assertEquals(1.0f, paintCall2.strokeWidth, "drawString() stroke width should be 1.0");
		assertEquals(true, paintCall2.antiAlias, "drawString() antiAlias should be true");
		assertEquals(BlendMode.SRC_IN, paintCall2.blendMode, "drawString() blendMode should be SRC_IN");

		// Call 3: drawImage() - draw the cached image snapshot
		MethodCall call3 = calls.get(3);
		assertEquals("SurfaceDouble-canvas", call3.className);
		assertEquals("drawImage", call3.methodName);
		assertEquals(3, call3.params.length);
		// Position at 200% zoom: x=10*2.0=20, y=20*2.0=40
		assertEquals(20, call3.params[1], "drawImage() x position should be 20 (10*2.0)");
		assertEquals(40, call3.params[2], "drawImage() y position should be 40 (20*2.0)");

		surface.close();
	}
}
