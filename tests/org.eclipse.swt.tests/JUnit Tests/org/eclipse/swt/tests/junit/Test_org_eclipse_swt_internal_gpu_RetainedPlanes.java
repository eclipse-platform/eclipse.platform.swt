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
	public void slotsAdmitHitEvictLeastRecentUnpinned () {
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
	public void slotsBackwardShiftDeleteKeepsProbeChains () {
		RetainedSlots slots = new RetainedSlots (64);
		for (long k = 0; k < 64; k++) slots.acquire (k * 1024, null);
		for (long k = 0; k < 64; k += 2) slots.remove (slots.peek (k * 1024));
		for (long k = 1; k < 64; k += 2) assertTrue (slots.peek (k * 1024) >= 0, "key " + k);
		for (long k = 0; k < 64; k += 2) assertEquals (-1, slots.peek (k * 1024));
		assertEquals (32, slots.size ());
	}

	@Test
	public void slotsReadjustEvictsUnderPressure () {
		RetainedSlots slots = new RetainedSlots (8);
		for (long k = 0; k < 8; k++) slots.acquire (k, null);
		assertEquals (6, slots.readjust (2, null));
		assertEquals (2, slots.size ());
		assertTrue (slots.peek (6) >= 0 && slots.peek (7) >= 0, "most recent survive");
	}

	@Test
	public void steadyStateLookupsDoNotAllocate () {
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
	public void tileGridScrollRastersOnlyExposedBand () {
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
	public void tileGridDirtyBitsIterateWithTrailingZeros () {
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
	public void rectsDifferenceAndSubtract () {
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
	public void rectListMergesTransitively () {
		RectList list = new RectList (4);
		list.add (0, 0, 10, 10);
		list.add (20, 0, 30, 10);
		list.add (10, 0, 20, 10);
		assertEquals (1, list.count ());
		assertEquals (30, list.x1 (0));
	}

	@Test
	public void paintKeysRoundTripMasks () {
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
	public void facadeWithoutRetainedModeKeepsStockBehaviour () {
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
	public void focusStaysOnTheOwningWidgetWithNoChildWindow () throws ReflectiveOperationException {
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
