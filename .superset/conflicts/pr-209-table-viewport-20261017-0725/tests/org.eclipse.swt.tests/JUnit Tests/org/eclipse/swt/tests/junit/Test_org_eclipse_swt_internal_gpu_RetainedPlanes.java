/*******************************************************************************
 * Copyright (c) 2026 Contributors to the Eclipse Foundation.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package org.eclipse.swt.tests.junit;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.management.*;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.gpu.*;
import org.eclipse.swt.widgets.*;
import org.junit.jupiter.api.*;

/**
 * Retained GPU planes: packed-int structures, zero-allocation steady state,
 * plane facade fallback, and (with -Dswt.gpu set) that focus stays on the
 * owning widget, which owns no child window.
 */
@SuppressWarnings("restriction")
public class Test_org_eclipse_swt_internal_gpu_RetainedPlanes {

	static long allocated () {
		return ((com.sun.management.ThreadMXBean) ManagementFactory.getThreadMXBean ()).getThreadAllocatedBytes (Thread.currentThread ().getId ());
	}

	@Test
	public void test_slotsAdmitHitEvictLeastRecentUnpinned () {
		RetainedSlots slots = new RetainedSlots (3);
		int a = slots.acquire (10L, null), b = slots.acquire (20L, null), c = slots.acquire (30L, null);
		assertTrue (a >= 0 && b >= 0 && c >= 0);
		slots.pin (a);
		assertEquals (b, slots.find (20L));
		long [] evicted = {0};
		int d = slots.acquire (40L, (slot, key) -> evicted [0] = key);
		assertEquals (30L, evicted [0], "least recent unpinned is evicted, pinned 10 survives");
		assertEquals (c, d, "the freed slot is reused");
		assertEquals (a, slots.peek (10L));
		assertEquals (-1, slots.peek (30L));
		slots.pin (b);
		slots.pin (d);
		assertEquals (-1, slots.acquire (50L, null), "all pinned refuses instead of growing");
		assertEquals (1, slots.refusals);
	}

	@Test
	public void test_slotsBackwardShiftDeleteKeepsProbeChains () {
		RetainedSlots slots = new RetainedSlots (64);
		for (long k = 0; k < 64; k++) slots.acquire (k * 1024, null);
		for (long k = 0; k < 64; k += 2) slots.remove (slots.peek (k * 1024));
		for (long k = 1; k < 64; k += 2) assertTrue (slots.peek (k * 1024) >= 0, "key " + k);
		for (long k = 0; k < 64; k += 2) assertEquals (-1, slots.peek (k * 1024));
		assertEquals (32, slots.size ());
	}

	@Test
	public void test_slotsReadjustEvictsUnderPressure () {
		RetainedSlots slots = new RetainedSlots (8);
		for (long k = 0; k < 8; k++) slots.acquire (k, null);
		assertEquals (6, slots.readjust (2, null));
		assertEquals (2, slots.size ());
		assertTrue (slots.peek (6) >= 0 && slots.peek (7) >= 0, "most recent survive");
	}

	@Test
	public void test_steadyStateLookupsDoNotAllocate () {
		RetainedSlots slots = new RetainedSlots (256);
		for (long k = 0; k < 200; k++) slots.acquire (PaintKeys.key (k == 0 ? 1 : (int) k, (int) (k * 31)), null);
		TileGrid grid = new TileGrid (64);
		grid.setClient (1000, 700);
		int [] r = new int [4];
		frame (grid, r);
		long before = allocated ();
		long sum = 0;
		for (int i = 0; i < 10_000; i++) {
			sum += slots.find (PaintKeys.key (1 + i % 199, (1 + i % 199) * 31));
			if ((i & 63) == 0) {
				grid.translate (0, (i & 64) == 0 ? -19 : 19);
				grid.damage (0, 681, 1000, 19);
				frame (grid, r);
			}
		}
		long bytes = allocated () - before;
		assertTrue (sum > 0);
		/*
		 * Interpreted (-Xint) this loop allocates 0 bytes; with C1/C2 a constant few hundred bytes of
		 * compilation artifacts appear once, independent of the 10,000 iterations and 157 frames.
		 */
		assertTrue (bytes < 4096, "packed-int hit path and tile frames do not allocate per call: " + bytes + " bytes");
	}

	static void frame (TileGrid grid, int [] r) {
		grid.beginFrame ();
		for (int ty = grid.visibleTy0 (); ty <= grid.visibleTy1 (); ty++) {
			for (int tx = grid.visibleTx0 (); tx <= grid.visibleTx1 (); tx++) {
				int slot = grid.acquireVisible (tx, ty);
				if (grid.rasterRect (slot, r)) grid.rastered (slot, r [0], r [1], r [2], r [3]);
			}
		}
	}

	@Test
	public void test_tileGridScrollRastersOnlyExposedBand () {
		TileGrid grid = new TileGrid (64);
		grid.setClient (512, 512);
		int [] r = new int [4];
		frame (grid, r);
		grid.translate (0, -20);
		grid.damage (0, 492, 512, 20);
		grid.beginFrame ();
		long area = 0;
		for (int ty = grid.visibleTy0 (); ty <= grid.visibleTy1 (); ty++) {
			for (int tx = grid.visibleTx0 (); tx <= grid.visibleTx1 (); tx++) {
				int slot = grid.acquireVisible (tx, ty);
				if (grid.rasterRect (slot, r)) {
					assertTrue (r [1] >= 492, "only the exposed band rasterizes, got y=" + r [1]);
					area += (long) r [2] * r [3];
					grid.rastered (slot, r [0], r [1], r [2], r [3]);
				}
			}
		}
		assertEquals (512L * 20, area);
	}

	@Test
	public void test_tileGridDirtyBitsIterateWithTrailingZeros () {
		TileGrid grid = new TileGrid (128);
		grid.setClient (2048, 2048);
		int [] r = new int [4];
		frame (grid, r);
		grid.damage (300, 300, 10, 10);
		int count = 0;
		for (int slot = TileGrid.nextSet (grid.dirtyBits (), 0); slot >= 0; slot = TileGrid.nextSet (grid.dirtyBits (), slot + 1)) count++;
		assertEquals (1, count, "one 256 px tile covers (300,300)-(310,310)");
	}

	@Test
	public void test_tileCacheCoversOnlyRasterizedParts () {
		TileGrid grid = new TileGrid (16);
		grid.setClient (300, 300);
		int slot = grid.acquire (0, 0);
		assertTrue (slot >= 0);
		assertEquals (slot, grid.peekSlot (0, 0));
		assertEquals (-1, grid.peekSlot (5, 5), "absent tiles are not admitted by a peek");
		grid.rastered (slot, 0, 0, 256, 100);
		assertTrue (grid.covers (slot, 10, 10, 100, 50), "inside the rasterized part");
		assertFalse (grid.covers (slot, 10, 90, 100, 50), "crosses the rasterized edge");
		grid.translate (0, -40);
		assertTrue (grid.covers (slot, 10, -30, 100, 50), "content space: a scroll moves the client view, not the tile");
		grid.damage (0, -40, 24, 300);
		assertFalse (grid.covers (slot, 0, -40, 24, 10), "a damaged margin column is no longer cached");
		assertTrue (grid.covers (slot, 30, -40, 100, 60), "the rest of the column keeps its validity");
	}

	@Test
	public void test_hiddenSurfaceRastersNothing () {
		if (RetainedPlanes.MODE == RetainedPlanes.MODE_OFF) return;
		Display display = Display.getDefault ();
		Shell shell = new Shell (display);
		try {
			shell.setSize (300, 200);
			Canvas canvas = new Canvas (shell, SWT.NONE);
			canvas.setBounds (0, 0, 280, 160);
			canvas.addListener (SWT.Paint, e -> e.gc.fillRectangle (e.x, e.y, e.width, e.height));
			shell.open ();
			for (int i = 0; i < 10; i++) {
				canvas.update ();
				while (display.readAndDispatch ()) {
				}
			}
			shell.setMinimized (true);
			while (display.readAndDispatch ()) {
			}
			long raster = GpuCounters.get (GpuCounters.RASTER_PIXELS);
			for (int i = 0; i < 5; i++) {
				canvas.redraw ();
				canvas.update ();
				while (display.readAndDispatch ()) {
				}
			}
			assertEquals (raster, GpuCounters.get (GpuCounters.RASTER_PIXELS), "a minimized surface rasterizes 0 pixels and keeps its damage queued");
			shell.setMinimized (false);
			for (int i = 0; i < 10; i++) {
				canvas.update ();
				while (display.readAndDispatch ()) {
				}
			}
			assertTrue (GpuCounters.get (GpuCounters.RASTER_PIXELS) > raster, "queued damage rasterizes once the surface is visible again");
		} finally {
			shell.dispose ();
		}
	}

	@Test
	public void test_rectsDifferenceAndSubtract () {
		int [] valid = {0, 0, 256, 100};
		int [] out = new int [4];
		assertTrue (Rects.differenceBounds (0, 0, 256, 256, valid, 0, out));
		assertArrayEquals (new int [] {0, 100, 256, 156}, out);
		int [] parts = new int [16];
		assertEquals (1, Rects.subtract (0, 0, 100, 100, 0, 10, 100, 90, parts));
		assertArrayEquals (new int [] {0, 0, 100, 10}, java.util.Arrays.copyOf (parts, 4));
		int [] u = {0, 0, 256, 100};
		Rects.unionOrLargest (u, 0, 0, 100, 256, 150);
		assertArrayEquals (new int [] {0, 0, 256, 150}, u);
	}

	@Test
	public void test_rectListMergesTransitively () {
		RectList list = new RectList (4);
		list.add (0, 0, 10, 10);
		list.add (20, 0, 30, 10);
		list.add (10, 0, 20, 10);
		assertEquals (1, list.count ());
		assertEquals (30, list.x1 (0));
	}

	@Test
	public void test_paintKeysRoundTripMasks () {
		int mask = PaintKeys.mask (PaintKeys.KIND_STROKE, SWT.LINE_DASH, SWT.CAP_ROUND, SWT.JOIN_BEVEL, SWT.ON, 1, 2.5f, PaintKeys.BACKEND_SKIJA);
		assertEquals (PaintKeys.KIND_STROKE, PaintKeys.kind (mask));
		assertEquals (SWT.LINE_DASH, PaintKeys.lineStyle (mask));
		assertEquals (SWT.CAP_ROUND, PaintKeys.lineCap (mask));
		assertEquals (SWT.JOIN_BEVEL, PaintKeys.lineJoin (mask));
		assertEquals (SWT.ON, PaintKeys.antialias (mask));
		assertEquals (1, PaintKeys.xor (mask));
		assertEquals (2.5f, PaintKeys.lineWidth (mask));
		long key = PaintKeys.key (mask, 0x80112233);
		assertEquals (mask, PaintKeys.maskOf (key));
		assertEquals (0x80112233, PaintKeys.lowOf (key));
		assertEquals (0xFF332211, PaintKeys.argbFromColorref (0x00112233, 0xFF));
	}

	@Test
	public void test_opStreamPacksAbsoluteAffineWithoutReferences () {
		OpStream ops = new OpStream (16, 64);
		ops.transform (2f, .25f, .5f, 3f, 11f, -7f);
		assertEquals (7, ops.size ());
		int [] packed = ops.ops ();
		assertEquals (OpStream.TRANSFORM, OpStream.opOf (packed [0]));
		assertEquals (6, OpStream.lengthOf (packed [0]));
		assertEquals (2f, Float.intBitsToFloat (packed [1]));
		assertEquals (.25f, Float.intBitsToFloat (packed [2]));
		assertEquals (.5f, Float.intBitsToFloat (packed [3]));
		assertEquals (3f, Float.intBitsToFloat (packed [4]));
		assertEquals (11f, Float.intBitsToFloat (packed [5]));
		assertEquals (-7f, Float.intBitsToFloat (packed [6]));

		ops.transformIdentity ();
		assertEquals (14, ops.size ());
		assertEquals (OpStream.TRANSFORM, OpStream.opOf (packed [7]));
		assertEquals (1f, Float.intBitsToFloat (packed [8]));
		assertEquals (0f, Float.intBitsToFloat (packed [9]));
		assertEquals (0f, Float.intBitsToFloat (packed [10]));
		assertEquals (1f, Float.intBitsToFloat (packed [11]));
		assertEquals (0f, Float.intBitsToFloat (packed [12]));
		assertEquals (0f, Float.intBitsToFloat (packed [13]));

		ops.reset ();
		ops.transform (Float.NaN, 0, 0, 1, 0, 0);
		assertEquals (0, ops.size ());
		assertEquals (OpStream.FAIL_TRANSFORM, ops.fail & OpStream.FAIL_TRANSFORM);
	}

	@Test
	public void test_win32GcRecordsAffineAndFailsClosedForTransformLocalClip () throws Exception {
		if (!"win32".equals (SWT.getPlatform ())) return;
		Display display = Display.getDefault ();
		Image image = new Image (display, 16, 16);
		GC gc = new GC (image);
		Transform transform = new Transform (display, 2f, .25f, .5f, 3f, 0, 0);
		java.lang.reflect.Field dataField = GC.class.getDeclaredField ("data");
		dataField.setAccessible (true);
		Object data = dataField.get (gc);
		java.lang.reflect.Field sinkField = data.getClass ().getField ("planeSink");
		OpStream ops = new OpStream (32, 128);
		try {
			sinkField.set (data, ops);
			gc.setTransform (transform);
			gc.setTransform (null);

			int [] packed = ops.ops ();
			assertEquals (14, ops.size ());
			assertEquals (OpStream.TRANSFORM, OpStream.opOf (packed [0]));
			assertEquals (2f, Float.intBitsToFloat (packed [1]));
			assertEquals (.25f, Float.intBitsToFloat (packed [2]));
			assertEquals (.5f, Float.intBitsToFloat (packed [3]));
			assertEquals (3f, Float.intBitsToFloat (packed [4]));
			assertEquals (OpStream.TRANSFORM, OpStream.opOf (packed [7]));
			assertEquals (1f, Float.intBitsToFloat (packed [8]));
			assertEquals (1f, Float.intBitsToFloat (packed [11]));

			ops.reset ();
			gc.setTransform (transform);
			gc.setClipping (0, 0, 8, 8);
			assertNotEquals (0, ops.fail & OpStream.FAIL_TRANSFORM,
					"transform-local clip semantics must fail closed to the GDI oracle");
		} finally {
			sinkField.set (data, null);
			transform.dispose ();
			gc.dispose ();
			image.dispose ();
		}
	}

	@Test
	public void test_facadeWithoutRetainedModeKeepsStockBehaviour () {
		Display display = Display.getDefault ();
		Image image = new Image (display, 8, 8);
		GC gc = new GC (image);
		try {
			RetainedPlanes.setPlane (gc, RetainedPlanes.SELECTION);
			gc.setBackground (display.getSystemColor (SWT.COLOR_RED));
			gc.fillRectangle (0, 0, 8, 8);
			assertEquals (RetainedPlanes.ALL_PLANES, RetainedPlanes.drawnPlanes (gc), "outside a retained pass every plane draws");
		} finally {
			gc.dispose ();
		}
		ImageData data = image.getImageData ();
		image.dispose ();
		RGB rgb = data.palette.getRGB (data.getPixel (4, 4));
		assertEquals (new RGB (255, 0, 0), rgb, "a plane-tagged draw outside a retained pass is immediate");
	}

	@Test
	public void test_focusStaysOnTheOwningWidgetWithNoChildWindow () throws ReflectiveOperationException {
		Display display = Display.getDefault ();
		Shell shell = new Shell (display);
		try {
			shell.setSize (400, 300);
			Canvas canvas = new Canvas (shell, SWT.NONE);
			canvas.setBounds (0, 0, 380, 260);
			canvas.addListener (SWT.Paint, e -> {
				e.gc.drawString ("retained", 10, 10, true);
				RetainedPlanes.setPlane (e.gc, RetainedPlanes.CARET);
				e.gc.fillRectangle (10, 30, 2, 16);
				RetainedPlanes.setPlane (e.gc, RetainedPlanes.BODY);
			});
			shell.open ();
			canvas.setFocus ();
			for (int i = 0; i < 20; i++) {
				canvas.update ();
				while (display.readAndDispatch ()) {
				}
			}
			RetainedPlanes.invalidatePlanes (canvas, RetainedPlanes.CARET, 10, 30, 2, 16);
			canvas.update ();
			assertSame (canvas, display.getFocusControl (), "focus stays on the widget");
			assertEquals (0, canvas.getChildren ().length, "no SWT child control");
			if ("win32".equals (SWT.getPlatform ())) {
				/* reflective: this common test must compile on every platform */
				Class<?> os = Class.forName ("org.eclipse.swt.internal.win32.OS");
				long hwnd = Control.class.getField ("handle").getLong (canvas);
				long focus = (Long) os.getMethod ("GetFocus").invoke (null);
				int gwChild = os.getField ("GW_CHILD").getInt (null);
				long child = (Long) os.getMethod ("GetWindow", long.class, int.class).invoke (null, hwnd, gwChild);
				assertEquals (hwnd, focus, "native focus is the widget HWND");
				assertEquals (0L, child, "no native child window");
				if (RetainedPlanes.MODE != RetainedPlanes.MODE_OFF) {
					assertTrue (GpuCounters.get (GpuCounters.FRAMES) > 0, "the retained frame driver painted the widget");
				}
			}
		} finally {
			shell.dispose ();
		}
	}
}
