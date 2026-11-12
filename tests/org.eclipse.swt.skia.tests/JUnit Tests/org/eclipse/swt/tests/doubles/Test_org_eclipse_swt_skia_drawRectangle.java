package org.eclipse.swt.tests.doubles;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.internal.graphics.SkiaGC;
import org.eclipse.swt.internal.skia.ISkiaResources;
import org.junit.jupiter.api.Test;

import io.github.humbleui.skija.BlendMode;
import io.github.humbleui.types.Rect;

public class Test_org_eclipse_swt_skia_drawRectangle {

	@Test
	public void testDrawRectangle_Rectangle() {
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

		gc.drawRectangle(new Rectangle(10, 20, 30, 40));
		// drawRectangle verifies that SkCanvasDouble recorded the correct method call
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

		var calls = surface.calls;
		assertEquals(MethodCall.get("surface-canvas", "drawRect", new Rect(10.5f, 20.5f, 40.5f, 60.5f), epd),
				calls.get(0));

		surface.close();

	}

	@Test
	public void testDrawRectangle() {
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

		gc.drawRectangle(10, 20, 30, 40);

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

		assertEquals(MethodCall.get("surface-canvas", "drawRect", new Rect(10.5f, 20.5f, 40.5f, 60.5f), epd),
				calls.get(0));

		surface.close();

	}

	@Test
	public void testDrawRectangle_Zoom150() {
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

		gc.drawRectangle(10, 20, 30, 40);

		var calls = surface.calls;
		assertEquals(1, calls.size(), "Expected 1 method call");

		System.out.println(calls);


		PaintData epd = new PaintData();
		epd.color = -1;
		epd.strokeWidth = 1.5f;
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

		// At 150% zoom (1.5x): x=10*1.5=15, y=20*1.5=30, width=30*1.5=45, height=40*1.5=60
		// Rect coordinates: left=15.0, top=30.0, right=15+45.0=60.0, bottom=30+60.0=90.0
		assertEquals(MethodCall.get("surface-canvas", "drawRect", new Rect(15.0f, 30.0f, 60.0f, 90.0f), epd),
				calls.get(0));

		surface.close();

	}

	@Test
	public void testDrawRectangle_Zoom200() {
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

		gc.drawRectangle(10, 20, 30, 40);

		var calls = surface.calls;
		assertEquals(1, calls.size(), "Expected 1 method call");

		System.out.println(calls);


		PaintData epd = new PaintData();
		epd.color = -1;
		epd.strokeWidth = 2.0f;
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

		// At 200% zoom (2.0x): x=10*2.0=20, y=20*2.0=40, width=30*2.0=60, height=40*2.0=80
		// Rect coordinates: left=20.0, top=40.0, right=20+60.0=80.0, bottom=40+80.0=120.0
		assertEquals(MethodCall.get("surface-canvas", "drawRect", new Rect(20.0f, 40.0f, 80.0f, 120.0f), epd),
				calls.get(0));

		surface.close();

	}

}