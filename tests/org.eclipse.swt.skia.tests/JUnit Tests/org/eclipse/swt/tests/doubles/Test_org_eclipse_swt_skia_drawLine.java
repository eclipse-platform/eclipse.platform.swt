package org.eclipse.swt.tests.doubles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.internal.graphics.SkiaGC;
import org.eclipse.swt.internal.skia.ISkiaResources;
import org.eclipse.swt.tests.skia.SupportedTestPlatforms;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import io.github.humbleui.skija.BlendMode;

public class Test_org_eclipse_swt_skia_drawLine {

	@BeforeAll
	static void checkPlatform() {
		assumeTrue(SupportedTestPlatforms.isSupported(), "Test skipped: Platform not supported");
	}

	@Test
	public void testDrawLine() {
		ISkiaResources resources = new SkiaResourcesDouble();
		Color foregroundColor = new Color(null, 255, 255, 255);
		resources.setForeground(foregroundColor);
		Color backgroundColor = new Color(null, 0, 0, 0);
		resources.setBackground(backgroundColor);

		final SkSurfaceDouble surface = new SkSurfaceDouble(null);
		var canvas = surface.canvas;
		final CanvasExtensionDouble ext = new CanvasExtensionDouble();
		ext.surface = surface;
		ext.resources = resources;
		final SkiaGC gc = new SkiaGC(ext, SWT.NONE);

		gc.drawLine(10, 20, 30, 40);

		var calls = surface.calls;
		assertEquals(1, calls.size(), "Expected 1 method call");

		System.out.println(calls);

		PaintData epd = new PaintData();
		epd.color = -1;
		epd.strokeWidth = 1.0f;
		epd.strokeMiter = 4.0f;
		epd.strokeCap = 0;
		epd.strokeJoin = 0;
		epd.style = 1;
		epd.alpha = 255;
		epd.antiAlias = false;
		epd.dither = false;
		epd.shader = null;
		epd.blendMode = BlendMode.SRC_OVER;
		epd.pathEffect = null;
		epd.imageFilter = null;
		epd.colorFilter = null;
		epd.maskFilter = null;

		// drawLine verifies that SkCanvasDouble recorded the correct method call
		assertEquals(MethodCall.get("surface-canvas", "drawLine", 10.5f, 20.5f, 30.5f, 40.5f, epd),
				surface.calls.get(0));

		surface.close();
	}

	@Test
	public void testDrawLine_Zoom150() {
		ISkiaResources resources = new SkiaResourcesDouble(150);
		Color foregroundColor = new Color(null, 255, 255, 255);
		resources.setForeground(foregroundColor);
		Color backgroundColor = new Color(null, 0, 0, 0);
		resources.setBackground(backgroundColor);

		final SkSurfaceDouble surface = new SkSurfaceDouble(null);
		var canvas = surface.canvas;
		final CanvasExtensionDouble ext = new CanvasExtensionDouble();
		ext.surface = surface;
		ext.resources = resources;
		final SkiaGC gc = new SkiaGC(ext, SWT.NONE);

		gc.drawLine(10, 20, 30, 40);

		var calls = surface.calls;
		assertEquals(1, calls.size(), "Expected 1 method call");

		System.out.println(calls);

		PaintData epd = new PaintData();
		epd.color = -1;
		epd.strokeWidth = 1.5f; // strokeWidth is scaled
		epd.strokeMiter = 4.0f;
		epd.strokeCap = 0;
		epd.strokeJoin = 0;
		epd.style = 1;
		epd.alpha = 255;
		epd.antiAlias = false;
		epd.dither = false;
		epd.shader = null;
		epd.blendMode = BlendMode.SRC_OVER;
		epd.pathEffect = null;
		epd.imageFilter = null;
		epd.colorFilter = null;
		epd.maskFilter = null;

		// At 150% zoom (1.5x), coordinates are scaled: 10*1.5 = 15, 20*1.5 = 30, etc.
		// strokeWidth is scaled to 1.5
		assertEquals(MethodCall.get("surface-canvas", "drawLine", 15.0f, 30.0f, 45.0f, 60.0f, epd),
				surface.calls.get(0));

		surface.close();
	}

	@Test
	public void testDrawLine_Zoom200() {
		ISkiaResources resources = new SkiaResourcesDouble(200);
		Color foregroundColor = new Color(null, 255, 255, 255);
		resources.setForeground(foregroundColor);
		Color backgroundColor = new Color(null, 0, 0, 0);
		resources.setBackground(backgroundColor);

		final SkSurfaceDouble surface = new SkSurfaceDouble(null);
		var canvas = surface.canvas;
		final CanvasExtensionDouble ext = new CanvasExtensionDouble();
		ext.surface = surface;
		ext.resources = resources;
		final SkiaGC gc = new SkiaGC(ext, SWT.NONE);

		gc.drawLine(10, 20, 30, 40);

		var calls = surface.calls;
		assertEquals(1, calls.size(), "Expected 1 method call");

		System.out.println(calls);

		PaintData epd = new PaintData();
		epd.color = -1;
		epd.strokeWidth = 2.0f; // 1.0 * 2.0
		epd.strokeMiter = 4.0f;
		epd.strokeCap = 0;
		epd.strokeJoin = 0;
		epd.style = 1;
		epd.alpha = 255;
		epd.antiAlias = false;
		epd.dither = false;
		epd.shader = null;
		epd.blendMode = BlendMode.SRC_OVER;
		epd.pathEffect = null;
		epd.imageFilter = null;
		epd.colorFilter = null;
		epd.maskFilter = null;

		// At 200% zoom (2.0x), coordinates are scaled: 10*2.0 = 20, 20*2.0 = 40, 30*2.0 = 60, 40*2.0 = 80
		assertEquals(MethodCall.get("surface-canvas", "drawLine", 20.0f, 40.0f, 60.0f, 80.0f, epd),
				surface.calls.get(0));

		surface.close();
	}
}