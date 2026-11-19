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

public class Test_org_eclipse_swt_skia_drawArc {

	@BeforeAll
	static void checkPlatform() {
		assumeTrue(SupportedTestPlatforms.isSupported(), "Test skipped: Platform not supported");
	}

	@Test
	public void testDrawArc() {
		ISkiaResources resources = new SkiaResourcesDouble();
		Color foregroundColor = new Color(null, 255, 255, 255);
		resources.setForeground(foregroundColor);
		Color backgroundColor = new Color(null, 0, 0, 0);
		resources.setBackground(backgroundColor);

		final SkSurfaceDouble surface = new SkSurfaceDouble(100,100, "surface", null);
		final CanvasExtensionDouble ext = new CanvasExtensionDouble();
		ext.surface = surface;
		ext.resources = resources;
		final SkiaGC gc = new SkiaGC(ext, SWT.NONE);

		gc.drawArc(10, 20, 30, 40, 45, 90);

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

		// drawArc verifies that SkCanvasDouble recorded the correct method call
		assertEquals(MethodCall.get("surface-canvas", "drawArc", 10, 20, 40, 60, -45, -90, false, epd),
				surface.calls.get(0));

		surface.close();
	}

	@Test
	public void testDrawArc_Zoom150() {
		ISkiaResources resources = new SkiaResourcesDouble(150);
		Color foregroundColor = new Color(null, 255, 255, 255);
		resources.setForeground(foregroundColor);
		Color backgroundColor = new Color(null, 0, 0, 0);
		resources.setBackground(backgroundColor);

		final SkSurfaceDouble surface = new SkSurfaceDouble(150, 150, "surface", null);
		final CanvasExtensionDouble ext = new CanvasExtensionDouble();
		ext.surface = surface;
		ext.resources = resources;
		final SkiaGC gc = new SkiaGC(ext, SWT.NONE);

		gc.drawArc(10, 20, 30, 40, 45, 90);

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

		// At 150% zoom: x=10*1.5=15, y=20*1.5=30, width=30*1.5=45, height=40*1.5=60
		// x+width=15+45=60, y+height=30+60=90, strokeWidth is scaled to 1.5
		assertEquals(MethodCall.get("surface-canvas", "drawArc", 15, 30, 60, 90, -45, -90, false, epd),
				surface.calls.get(0));

		surface.close();
	}

	@Test
	public void testDrawArc_Zoom200() {
		ISkiaResources resources = new SkiaResourcesDouble(200);
		Color foregroundColor = new Color(null, 255, 255, 255);
		resources.setForeground(foregroundColor);
		Color backgroundColor = new Color(null, 0, 0, 0);
		resources.setBackground(backgroundColor);

		final SkSurfaceDouble surface = new SkSurfaceDouble(200, 200, "surface", null);
		final CanvasExtensionDouble ext = new CanvasExtensionDouble();
		ext.surface = surface;
		ext.resources = resources;
		final SkiaGC gc = new SkiaGC(ext, SWT.NONE);

		gc.drawArc(10, 20, 30, 40, 45, 90);

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

		// At 200% zoom: x=10*2.0=20, y=20*2.0=40, width=30*2.0=60, height=40*2.0=80
		// x+width=20+60=80, y+height=40+80=120
		assertEquals(MethodCall.get("surface-canvas", "drawArc", 20, 40, 80, 120, -45, -90, false, epd),
				surface.calls.get(0));

		surface.close();
	}
}