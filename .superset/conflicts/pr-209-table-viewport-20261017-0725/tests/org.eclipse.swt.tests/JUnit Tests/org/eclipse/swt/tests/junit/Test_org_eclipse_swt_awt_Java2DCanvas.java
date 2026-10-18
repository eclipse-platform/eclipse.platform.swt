/*******************************************************************************
 * Copyright 2026 Hitesh Soliwal and contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * SPDX-License-Identifier: Apache-2.0
 *******************************************************************************/
package org.eclipse.swt.tests.junit;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.awt.Java2DCanvas;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.graphics.Region;
import org.eclipse.swt.graphics.Transform;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Shell;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** Public API and native image-path checks; screen evidence has no offscreen fallback. */
public class Test_org_eclipse_swt_awt_Java2DCanvas {

	private Display display;
	private Shell shell;

	@BeforeEach
	public void setUp () {
		display = Display.getDefault ();
		shell = new Shell (display, SWT.NO_TRIM);
		shell.setBounds (40, 40, 320, 240);
	}

	@AfterEach
	public void tearDown () {
		if (shell != null && !shell.isDisposed ()) shell.dispose ();
	}

	@Test
	public void test_logicalExtentKeepsRasterVisibleSizedAndComposesOrigin () {
		ScrolledComposite scrolled = new ScrolledComposite (shell, SWT.H_SCROLL | SWT.V_SCROLL);
		scrolled.setBounds (0, 0, 200, 140);
		Java2DCanvas canvas = new Java2DCanvas (scrolled, SWT.NONE);
		scrolled.setContent (canvas);
		scrolled.setLogicalContentSize (1_600, 220_000_000);
		scrolled.layout (false);
		assertEquals (new Point (0, 0), canvas.getRasterSize ());
		Rectangle client = canvas.getClientArea ();
		assertEquals (scrolled.getClientArea ().width, client.width);
		assertEquals (scrolled.getClientArea ().height, client.height);
		scrolled.setOrigin (317, 100_000_003);
		AffineTransform view = AffineTransform.getScaleInstance (1.5, 2);
		canvas.setViewTransform (view);
		AtomicReference<AffineTransform> observed = new AtomicReference<> ();
		canvas.setRenderer (g -> {
			assertSame (display, Display.getCurrent ());
			observed.set (g.getTransform ());
		});
		paintImage (canvas);
		AffineTransform expected = AffineTransform.getTranslateInstance (-317, -100_000_003);
		expected.concatenate (view);
		assertEquals (expected, observed.get ());
		assertEquals (new Point (317, 100_000_003), canvas.getViewportOrigin ());
		assertEquals (new Point (0, 0), canvas.getLocation ());
		assertEquals (new Point (client.width, client.height), canvas.getRasterSize ());
		Point returned = canvas.getViewportOrigin ();
		returned.y = 0;
		assertEquals (100_000_003, canvas.getViewportOrigin ().y);
	}

	@Test
	public void test_logicalExtentRejectsNegativeArgumentsWithoutChangingPriorState () {
		ScrolledComposite scrolled = new ScrolledComposite (shell, SWT.H_SCROLL | SWT.V_SCROLL);
		scrolled.setSize (160, 120);
		scrolled.setContent (new Java2DCanvas (scrolled, SWT.NONE));
		scrolled.setLogicalContentSize (800, 900);
		assertThrows (IllegalArgumentException.class, () -> scrolled.setLogicalContentSize (-1, 20));
		assertThrows (IllegalArgumentException.class, () -> scrolled.setLogicalContentSize (20, -1));
		assertEquals (new Point (800, 900), scrolled.getLogicalContentSize ());
	}

	@Test
	public void test_partialInvalidationReusesPixelsAndExposureDoesNotInvokeRenderer () {
		Java2DCanvas canvas = canvas (64, 48);
		AtomicInteger calls = new AtomicInteger ();
		AtomicInteger color = new AtomicInteger (0xffbe2020);
		canvas.setRenderer (g -> { calls.incrementAndGet (); fill (g, color.get ()); });
		Image image = new Image (display, 64, 48);
		GC gc = new GC (image);
		try {
			paint (canvas, gc, 0, 0, 64, 48);
			paint (canvas, gc, 2, 3, 20, 16);
			assertEquals (1, calls.get ());
			color.set (0xff2050c8);
			canvas.redraw (7, 9, 13, 11, false);
			paint (canvas, gc, 7, 9, 13, 11);
			paint (canvas, gc, 0, 0, 64, 48);
			assertEquals (2, calls.get ());
			int [] actual = pixels (image.getImageData ());
			for (int y = 0; y < 48; y++) for (int x = 0; x < 64; x++) {
				assertEquals (x >= 7 && x < 20 && y >= 9 && y < 20 ? 0xff2050c8 : 0xffbe2020,
						actual [y * 64 + x], "pixel " + x + "," + y);
			}
			assertFalse (gc.isDisposed (), "paint GC remains caller owned");
		} finally { gc.dispose (); image.dispose (); }
	}

	@Test
	public void test_panPartialThenFullExposureMatchesFreshNativeTransfer () {
		ScrolledComposite scrolled = new ScrolledComposite (shell, SWT.H_SCROLL | SWT.V_SCROLL);
		scrolled.setSize (160, 120);
		Java2DCanvas canvas = new Java2DCanvas (scrolled, SWT.NONE);
		scrolled.setContent (canvas);
		scrolled.setLogicalContentSize (1_600, 220_000_000);
		scrolled.layout (false);
		canvas.setRenderer (Test_org_eclipse_swt_awt_Java2DCanvas::tiles);
		ImageData initial = paintImage (canvas);
		scrolled.setOrigin (29, 37);
		Rectangle area = canvas.getClientArea ();
		Image image = new Image (display, area.width, area.height);
		GC gc = new GC (image);
		try {
			paint (canvas, gc, area.width - 23, 0, 23, area.height);
			paint (canvas, gc, 0, 0, area.width, area.height);
			ImageData partialThenExposed = image.getImageData ();
			canvas.redraw ();
			ImageData fresh = paintImage (canvas);
			assertArrayEquals (pixels (fresh), pixels (partialThenExposed));
			assertFalse (Arrays.equals (pixels (initial), pixels (fresh)), "pan must change visible pixels");
			assertTiles (fresh, 29, 37);
		} finally { gc.dispose (); image.dispose (); }
	}

	@Test
	public void test_transformsAreDefensiveFiniteAndCanBeSingular () {
		Java2DCanvas canvas = canvas (48, 32);
		AffineTransform supplied = AffineTransform.getTranslateInstance (3, 7);
		canvas.setViewTransform (supplied);
		supplied.setToScale (9, 9);
		assertEquals (AffineTransform.getTranslateInstance (3, 7), canvas.getViewTransform ());
		AffineTransform returned = canvas.getViewTransform ();
		returned.setToIdentity ();
		assertEquals (AffineTransform.getTranslateInstance (3, 7), canvas.getViewTransform ());
		for (double invalid : new double [] {Double.NaN, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY}) {
			assertThrows (IllegalArgumentException.class,
					() -> canvas.setViewTransform (new AffineTransform (1, 0, 0, 1, invalid, 0)));
		}
		canvas.setViewTransform (AffineTransform.getScaleInstance (0, 1));
		canvas.setRenderer (g -> g.fillRect (0, 0, 20, 20));
		assertDoesNotThrow (() -> paintImage (canvas));
		canvas.setViewTransform (null);
		assertTrue (canvas.getViewTransform ().isIdentity ());
		canvas.setRenderer (null);
		assertNull (canvas.getRenderer ());
		for (int pixel : pixels (paintImage (canvas))) assertEquals (0xffffffff, pixel);
	}

	@Test
	public void test_alphaPresentationClearsPreviousNativePixels () {
		Java2DCanvas canvas = canvas (40, 30);
		canvas.setRenderer (g -> {
			g.setComposite (AlphaComposite.Src);
			g.setColor (new java.awt.Color (200, 20, 80, 128));
			g.fillRect (0, 0, 40, 30);
		});
		Image image = new Image (display, 40, 30);
		GC gc = new GC (image);
		try {
			paint (canvas, gc, 0, 0, 40, 30);
			for (int pixel : pixels (image.getImageData ())) assertRgbNear (0xffe389a7, pixel, 1);
			canvas.setRenderer (g -> {
				g.setComposite (AlphaComposite.Clear);
				g.fillRect (0, 0, 40, 30);
			});
			// Reuse the same native destination: transparent pixels must remove the previous frame.
			paint (canvas, gc, 0, 0, 40, 30);
			for (int pixel : pixels (image.getImageData ())) assertEquals (0xffffffff, pixel);
		} finally { gc.dispose (); image.dispose (); }
	}

	@Test
	public void test_rendererFailureDoesNotPresentPartialPixelsAndCanRecover () {
		Java2DCanvas canvas = canvas (40, 30);
		Image image = new Image (display, 40, 30);
		GC gc = new GC (image);
		try {
			gc.setBackground (display.getSystemColor (SWT.COLOR_DARK_BLUE));
			gc.fillRectangle (0, 0, 40, 30);
			int [] before = pixels (image.getImageData ());
			IllegalStateException failure = new IllegalStateException ("independent renderer failure");
			canvas.setRenderer (g -> { fill (g, 0xffff0000); throw failure; });
			assertSame (failure, assertThrows (IllegalStateException.class, () -> paint (canvas, gc, 0, 0, 40, 30)));
			assertFalse (gc.isDisposed ());
			assertArrayEquals (before, pixels (image.getImageData ()), "failed raster must not be transferred");
			canvas.setRenderer (g -> fill (g, 0xff123456));
			paint (canvas, gc, 0, 0, 40, 30);
			for (int pixel : pixels (image.getImageData ())) assertEquals (0xff123456, pixel);
		} finally { gc.dispose (); image.dispose (); }
	}

	@Test
	public void test_presentationRestoresCallerTransformStateAndExactClip () {
		Java2DCanvas canvas = canvas (80, 60);
		canvas.setRenderer (g -> fill (g, 0xffcd3625));
		Image image = new Image (display, 100, 90);
		GC gc = new GC (image);
		Region expected = new Region (display), actual = new Region (display), difference = new Region (display);
		Transform transform = new Transform (display, 1, 0, 0, 1, 5, 7);
		Transform after = new Transform (display);
		try {
			gc.setBackground (display.getSystemColor (SWT.COLOR_WHITE));
			gc.fillRectangle (0, 0, 100, 90);
			expected.add (new Rectangle (3, 4, 76, 52));
			expected.subtract (new Rectangle (24, 23, 15, 13));
			gc.setClipping (expected);
			gc.setTransform (transform);
			gc.setAlpha (137);
			gc.setInterpolation (SWT.HIGH);
			gc.setXORMode (true);
			paint (canvas, gc, 0, 0, 80, 60);
			gc.getTransform (after);
			float [] elements = new float [6];
			after.getElements (elements);
			assertArrayEquals (new float [] {1, 0, 0, 1, 5, 7}, elements, 0.0001f);
			assertEquals (137, gc.getAlpha ());
			assertEquals (SWT.HIGH, gc.getInterpolation ());
			assertTrue (gc.getXORMode ());
			assertFalse (gc.isDisposed ());
			gc.setTransform (null);
			gc.getClipping (actual);
			difference.add (expected); difference.subtract (actual);
			assertTrue (difference.isEmpty (), "caller clipping lost pixels");
			difference.add (actual); difference.subtract (expected);
			assertTrue (difference.isEmpty (), "caller clipping leaked pixels");
			ImageData data = image.getImageData ();
			for (int y = 0; y < data.height; y++) for (int x = 0; x < data.width; x++) {
				if (!expected.contains (x, y)) assertEquals (0xffffffff, argb (data, x, y));
			}
			assertEquals (0xffcd3625, argb (data, 15, 16), "inside clipped presentation");
		} finally {
			gc.dispose (); image.dispose (); expected.dispose (); actual.dispose (); difference.dispose ();
			transform.dispose (); after.dispose ();
		}
	}

	@Test
	public void test_wrongThreadDisposedResizeAndReentrantInvalidationContracts () throws Exception {
		Java2DCanvas canvas = canvas (44, 33);
		AtomicReference<Throwable> failure = new AtomicReference<> ();
		Thread worker = new Thread (() -> {
			try { canvas.getViewTransform (); } catch (Throwable thrown) { failure.set (thrown); }
		}, "java2d-contract-worker");
		worker.start (); worker.join (5_000);
		assertFalse (worker.isAlive ());
		assertInstanceOf (SWTException.class, failure.get ());
		assertEquals (SWT.ERROR_THREAD_INVALID_ACCESS, ((SWTException) failure.get ()).code);
		AtomicInteger calls = new AtomicInteger ();
		canvas.setRenderer (g -> {
			fill (g, 0xff234567);
			if (calls.getAndIncrement () == 0) canvas.redraw ();
		});
		paintImage (canvas);
		ImageData ready = paintImage (canvas);
		assertEquals (2, calls.get ());
		for (int pixel : pixels (ready)) assertEquals (0xff234567, pixel);
		canvas.setSize (0, 0);
		assertEquals (new Point (0, 0), canvas.getRasterSize ());
		canvas.setSize (30, 20);
		paintImage (canvas);
		assertEquals (new Point (30, 20), canvas.getRasterSize ());
		canvas.setRenderer (g -> canvas.dispose ());
		assertDoesNotThrow (() -> paintImage (canvas));
		SWTException disposed = assertThrows (SWTException.class, canvas::getRasterSize);
		assertEquals (SWT.ERROR_WIDGET_DISPOSED, disposed.code);
	}

	@Test
	public void test_ownedNativeResourcesReturnToBaselineAndObserverDetectsRetention () {
		boolean tracking = display.isTracking ();
		display.setTracking (true);
		Java2DCanvas canvas = canvas (80, 60);
		canvas.setRenderer (Test_org_eclipse_swt_awt_Java2DCanvas::tiles);
		Image image = new Image (display, 80, 60);
		GC gc = new GC (image);
		try {
			paint (canvas, gc, 0, 0, 80, 60);
			Set<Object> baseline = nativeObjects ();
			Image retained = new Image (display, 3, 3);
			GC retainedGc = new GC (retained);
			try {
				Set<Object> observed = nativeObjects (); observed.removeAll (baseline);
				assertTrue (observed.contains (retained), "observer must detect retained Image control");
				assertTrue (observed.contains (retainedGc), "observer must detect retained GC control");
			} finally { retainedGc.dispose (); retained.dispose (); }
			for (int frame = 0; frame < 120; frame++) {
				canvas.setViewTransform (AffineTransform.getTranslateInstance (-frame, -frame * 3));
				paint (canvas, gc, 0, 0, 80, 60);
			}
			Set<Object> remaining = nativeObjects (); remaining.removeAll (baseline);
			assertTrue (remaining.isEmpty (), "transient Image/GC/Region/Transform must be disposed: " + remaining);
		} finally {
			gc.dispose (); image.dispose (); canvas.dispose (); display.setTracking (tracking);
		}
	}

	@Test
	public void test_screenPanCoverUncoverEqualsFreshFrame () throws Exception {
		Assumptions.assumeTrue (Boolean.getBoolean ("swt.viewport.screenshotRegression"),
				"viewport screenshot lane disabled");
		Assumptions.assumeTrue ("gtk".equals (SWT.getPlatform ()),
				"initial deterministic screenshot lane is GTK");
		assertNotEquals ("1", System.getenv ("SWT_GTK4"), "Screen capture lane requires GTK3");
		assertEquals ("x11", System.getenv ("GDK_BACKEND"), "Screen capture lane requires X11");
		Path evidence = Path.of (System.getProperty ("swt.java2d.evidenceDirectory",
				"target/java2d-native-evidence"));
		Files.createDirectories (evidence);
		ScrolledComposite scrolled = new ScrolledComposite (shell, SWT.H_SCROLL | SWT.V_SCROLL);
		// Fix the fixture geometry: GTK overlay scrollbars would legitimately cover content pixels.
		scrolled.setScrollbarsMode (SWT.NONE);
		scrolled.setBounds (0, 0, 256, 192);
		Java2DCanvas canvas = new Java2DCanvas (scrolled, SWT.NONE);
		scrolled.setContent (canvas);
		scrolled.setLogicalContentSize (1_600, 220_000_000);
		AtomicInteger calls = new AtomicInteger ();
		canvas.setRenderer (g -> { calls.incrementAndGet (); tiles (g); });
		shell.open ();
		shell.forceActive ();
		settle ();
		// Native scrollbar trim is known after realization; apply the existing viewport layout.
		scrolled.layout (false);
		settle ();
		assertTrue (calls.get () > 0, "screen capture follows a native paint");
		Rectangle screen = SwtScreenshotCapture.screenBounds (canvas);
		List<String> captures = new ArrayList<> ();
		ImageData before = capture (canvas, evidence, "before", captures);
		assertTiles (before, 0, 0);
		scrolled.setOrigin (29, 37);
		settle ();
		ImageData panned = capture (canvas, evidence, "panned", captures);
		assertTiles (panned, 29, 37);
		assertFalse (Arrays.equals (pixels (before), pixels (panned)), "stale pan must be rejected");
		Canvas cover = new Canvas (scrolled, SWT.NONE);
		cover.setBounds (31, 27, 73, 51);
		cover.addListener (SWT.Paint, event -> {
			event.gc.setBackground (display.getSystemColor (SWT.COLOR_GREEN));
			event.gc.fillRectangle (cover.getClientArea ());
		});
		cover.moveAbove (canvas);
		settle ();
		ImageData covered = capture (canvas, evidence, "covered", captures);
		assertEquals (0xff00ff00, argb (covered, 45, 40), "capture must include real overlapping control");
		assertEquals (argb (panned, 12, 12), argb (covered, 12, 12), "uncovered pixels retain coordinates");
		cover.dispose ();
		settle ();
		ImageData exposed = capture (canvas, evidence, "uncovered", captures);
		assertTiles (exposed, 29, 37);
		canvas.redraw ();
		settle ();
		ImageData fresh = capture (canvas, evidence, "fresh", captures);
		assertArrayEquals (pixels (fresh), pixels (exposed), "exposure after pan must equal fresh rendering");
		assertArrayEquals (pixels (panned), pixels (fresh));
		Point dpi = display.getDPI ();
		String metadata = "{\n  \"backend\":\"" + SWT.getPlatform () + "\",\n"
				+ "  \"captureApi\":\"GC(Display).copyArea\",\n"
				+ "  \"scrollbarMode\":\"SWT.NONE\",\n"
				+ "  \"screenBounds\":[" + screen.x + "," + screen.y + "," + screen.width + "," + screen.height + "],\n"
				+ "  \"dpi\":[" + dpi.x + "," + dpi.y + "],\n"
				+ "  \"pixelSize\":[" + fresh.width + "," + fresh.height + "],\n"
				+ "  \"origin\":[29,37],\n  \"coverBounds\":[31,27,73,51],\n"
				+ "  \"rendererCalls\":" + calls.get () + ",\n  \"captures\":[\n"
				+ String.join (",\n", captures) + "\n  ]\n}\n";
		Files.writeString (evidence.resolve ("capture-metadata.json"), metadata);
	}

	private Java2DCanvas canvas (int width, int height) {
		Java2DCanvas canvas = new Java2DCanvas (shell, SWT.NONE);
		canvas.setSize (width, height);
		canvas.setBackground (display.getSystemColor (SWT.COLOR_WHITE));
		return canvas;
	}

	private ImageData paintImage (Java2DCanvas canvas) {
		Rectangle area = canvas.getClientArea ();
		Image image = new Image (display, area.width, area.height);
		GC gc = new GC (image);
		try {
			paint (canvas, gc, area.x, area.y, area.width, area.height);
			assertFalse (gc.isDisposed ());
			return image.getImageData ();
		} finally { gc.dispose (); image.dispose (); }
	}

	private static void paint (Java2DCanvas canvas, GC gc, int x, int y, int width, int height) {
		Event event = new Event ();
		event.gc = gc; event.x = x; event.y = y; event.width = width; event.height = height;
		canvas.notifyListeners (SWT.Paint, event);
	}

	private Set<Object> nativeObjects () {
		Set<Object> result = Collections.newSetFromMap (new IdentityHashMap<> ());
		for (Object object : display.getDeviceData ().objects) {
			if (object instanceof Image || object instanceof GC || object instanceof Region || object instanceof Transform) {
				result.add (object);
			}
		}
		return result;
	}

	private static void fill (Graphics2D graphics, int argb) {
		graphics.setColor (new java.awt.Color (argb, true));
		java.awt.Rectangle clip = graphics.getClipBounds ();
		if (clip != null) graphics.fill (clip);
	}

	private static void tiles (Graphics2D graphics) {
		java.awt.Rectangle clip = graphics.getClipBounds ();
		if (clip == null || clip.isEmpty ()) return;
		int firstColumn = Math.floorDiv (clip.x, 16), firstRow = Math.floorDiv (clip.y, 16);
		int lastColumn = Math.floorDiv (clip.x + clip.width - 1, 16);
		int lastRow = Math.floorDiv (clip.y + clip.height - 1, 16);
		for (int row = firstRow; row <= lastRow; row++) for (int column = firstColumn; column <= lastColumn; column++) {
			graphics.setColor (new java.awt.Color (tileArgb (column, row), true));
			graphics.fillRect (column * 16, row * 16, 16, 16);
		}
	}

	private static int tileArgb (int column, int row) {
		return 0xff000000 | (40 + Math.floorMod (column * 37 + row * 17, 176)) << 16
				| (35 + Math.floorMod (column * 13 + row * 41, 180)) << 8
				| (45 + Math.floorMod (column * 29 + row * 23, 170));
	}

	private static void assertTiles (ImageData image, int originX, int originY) {
		for (int y = 0; y < image.height; y++) for (int x = 0; x < image.width; x++) {
			assertEquals (tileArgb (Math.floorDiv (x + originX, 16), Math.floorDiv (y + originY, 16)),
					argb (image, x, y), "screen pixel " + x + "," + y);
		}
	}

	private static int argb (ImageData image, int x, int y) {
		RGB rgb = image.palette.getRGB (image.getPixel (x, y));
		return 0xff000000 | rgb.red << 16 | rgb.green << 8 | rgb.blue;
	}

	private static int [] pixels (ImageData image) {
		int [] result = new int [image.width * image.height];
		for (int y = 0; y < image.height; y++) for (int x = 0; x < image.width; x++) {
			result [y * image.width + x] = argb (image, x, y);
		}
		return result;
	}

	private static void assertRgbNear (int expected, int actual, int tolerance) {
		for (int shift : new int [] {0, 8, 16}) {
			assertTrue (Math.abs ((expected >>> shift & 255) - (actual >>> shift & 255)) <= tolerance,
					"alpha channel color: expected " + Integer.toHexString (expected) + " actual " + Integer.toHexString (actual));
		}
	}

	private ImageData capture (Java2DCanvas canvas, Path directory, String name, List<String> captures) throws Exception {
		Path path = directory.resolve (name + ".png");
		SwtScreenshotCapture.Result result = SwtScreenshotCapture.captureScreenControl (canvas, path);
		assertEquals (SwtScreenshotCapture.Method.DISPLAY_COPY_AREA, result.method ());
		captures.add ("    {\"name\":\"" + name + "\",\"sha256\":\"" + result.sha256 () + "\"}");
		return new ImageData (path.toString ());
	}

	private void settle () throws InterruptedException {
		long deadline = System.nanoTime () + 250_000_000L;
		do {
			while (display.readAndDispatch ()) { }
			display.update ();
			Thread.sleep (5);
		} while (System.nanoTime () < deadline);
		while (display.readAndDispatch ()) { }
		display.update ();
	}
}
