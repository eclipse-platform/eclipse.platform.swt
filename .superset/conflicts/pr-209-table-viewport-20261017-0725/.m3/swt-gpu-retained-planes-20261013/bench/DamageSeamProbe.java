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
import java.io.*;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.gpu.*;
import org.eclipse.swt.widgets.*;

/**
 * RFB damage and seam probe. A bordered, vertically scrollable Canvas with a
 * fixed left margin plane scrolls its content block with {@link Canvas#scroll}
 * (RFB CopyRect), scrolls back (tile refill), then moves a selection overlay.
 * After every frame the shell is captured from the screen (display
 * {@code GC.copyArea}, shell topmost and active) and the pixels of four regions
 * are appended to a binary file: the border ring, the scrollbar, the margin
 * plane and the tile seam lines (+-1 px around every 256 px device-pixel tile
 * edge). In retained modes the frame driver's damage is fed to
 * {@link ViewportDamageRecorder} (SWT PR #121) with the margin as protected area.
 * <p>usage: DamageSeamProbe &lt;out.bin&gt;; mode from -Dswt.gpu, zoom from -Dswt.autoScale</p>
 */
public class DamageSeamProbe {
	static final int W = 600, H = 420, MARGIN = 24, LINE = 7, STEP = 13;
	static final int [] FRAME = {0};

	public static void main (String [] args) throws Exception {
		Display display = new Display ();
		Shell shell = new Shell (display, SWT.SHELL_TRIM | SWT.ON_TOP);
		shell.setLayout (null);
		Canvas canvas = new Canvas (shell, SWT.BORDER | SWT.V_SCROLL);
		canvas.setBounds (10, 10, W, H);
		int [] state = {0, 0, 0};
		Color [] palette = {display.getSystemColor (SWT.COLOR_RED), display.getSystemColor (SWT.COLOR_DARK_GREEN), display.getSystemColor (SWT.COLOR_BLUE),
			display.getSystemColor (SWT.COLOR_DARK_YELLOW), display.getSystemColor (SWT.COLOR_DARK_CYAN), display.getSystemColor (SWT.COLOR_DARK_MAGENTA)};
		canvas.addListener (SWT.Paint, e -> paint (e, canvas, state, palette));
		shell.setBounds (40, 40, W + 40, H + 60);
		shell.open ();
		shell.forceActive ();
		settle (display, canvas);
		int zoom = DPIUtil.getDeviceZoom ();
		Rectangle client = canvas.getClientArea ();
		int cw = DPIUtil.pointToPixel (client.width, zoom), ch = DPIUtil.pointToPixel (client.height, zoom);
		ViewportDamageRecorder recorder = new ViewportDamageRecorder (cw, ch);
		recorder.protectedArea ().add (0, 0, DPIUtil.pointToPixel (MARGIN, zoom), ch);
		if (RetainedPlanes.MODE != RetainedPlanes.MODE_OFF) {
			RetainedPlanes.setDamageListener (new RetainedPlanes.DamageListener () {
				@Override public void painted (Control c, int x, int y, int w, int h) { if (c == canvas) recorder.paint (x, y, w, h); }
				@Override public void copied (Control c, int sx, int sy, int dx, int dy, int w, int h) { if (c == canvas) recorder.copy (sx, sy, dx, dy, w, h); }
				@Override public void frameEnd (Control c) { if (c == canvas) recorder.endFrame (); }
			});
			canvas.redraw ();
			settle (display, canvas);
		}
		Thread.sleep (300);
		settle (display, canvas);
		int [] originPx = {0};
		try (DataOutputStream out = new DataOutputStream (new BufferedOutputStream (new FileOutputStream (args [0])))) {
			int frames = 0;
			for (int i = 0; i < 70; i++) {
				if (i < 30) scroll (canvas, state, originPx, STEP, client, zoom);
				else if (i < 60) scroll (canvas, state, originPx, -STEP, client, zoom);
				else {
					state [2] = 1;
					state [1] += 9;
					RetainedPlanes.invalidatePlanes (canvas, RetainedPlanes.SELECTION, MARGIN, 40, client.width - MARGIN, 50);
				}
				/* the OS update region after the state change is the area this frame needs (RFB: what changed) */
				org.eclipse.swt.internal.win32.RECT update = new org.eclipse.swt.internal.win32.RECT ();
				org.eclipse.swt.internal.win32.OS.GetUpdateRect (canvas.handle, update, false);
				long tilesBefore = GpuCounters.get (GpuCounters.TILE_RASTER), paintedBefore = GpuCounters.get (GpuCounters.RASTER_PIXELS);
				canvas.update ();
				while (display.readAndDispatch ()) {
				}
				long neededPx = (long) Math.max (0, update.right - update.left) * Math.max (0, update.bottom - update.top);
				int tilesNeeded = update.right <= update.left || update.bottom <= update.top ? 0
					: ((update.right - 1) >> 8) - (update.left >> 8) + 1 == 0 ? 0
					: (((update.right - 1) >> 8) - (update.left >> 8) + 1) * ((((update.bottom - 1 + originPx [0]) >> 8) - ((update.top + originPx [0]) >> 8)) + 1);
				long painted = GpuCounters.get (GpuCounters.RASTER_PIXELS) - paintedBefore, tiles = GpuCounters.get (GpuCounters.TILE_RASTER) - tilesBefore;
				if (painted > neededPx) overpaintPx += painted - neededPx;
				if (tiles > tilesNeeded) overpaintTiles += tiles - tilesNeeded;
				neededTotal += neededPx;
				paintedTotal += painted;
				writeFrame (out, display, shell, canvas, zoom, originPx [0]);
				frames++;
			}
			out.writeInt (-1);
			System.out.println (String.join ("\t", "mode=" + System.getProperty ("swt.gpu", "off"), "zoom=" + zoom, "frames=" + frames,
				"recorderFrames=" + recorder.frameCount (), "totalDamagePx=" + recorder.totalDamagePx (), "totalPaintedPx=" + recorder.totalPaintedPx (),
				"maxPaintedPx=" + recorder.maxPaintedPx (), "fullRepaints=" + recorder.fullRepaints (), "protectedHitFrames=" + recorder.protectedHitFrames (),
				"totalProtectedHitPx=" + recorder.totalProtectedHitPx (), "occludedRetries=" + occludedRetries, "neededPx=" + neededTotal, "rasterPx=" + paintedTotal, "overpaintPx=" + overpaintPx, "overpaintTiles=" + overpaintTiles, "perFrame=" + perFrame (recorder)));
		}
		display.dispose ();
	}

	/** damagePx/paintedPx/protectedHitPx per recorded frame, compact. */
	static String perFrame (ViewportDamageRecorder recorder) {
		StringBuilder b = new StringBuilder ();
		for (int f = 0; f < Math.min (recorder.frameCount (), 512); f++) {
			if (f > 0) b.append (';');
			b.append (recorder.damagePx (f)).append ('/').append (recorder.paintedPx (f)).append ('/').append (recorder.protectedHitPx (f));
		}
		return b.toString ();
	}

	static void scroll (Canvas canvas, int [] state, int [] originPx, int dy, Rectangle client, int zoom) {
		int w = client.width - MARGIN, h = client.height - Math.abs (dy);
		int srcY = dy > 0 ? dy : 0, dstY = dy > 0 ? 0 : -dy;
		originPx [0] += DPIUtil.pointToPixel (srcY, zoom) - DPIUtil.pointToPixel (dstY, zoom);
		state [0] += dy;
		canvas.scroll (MARGIN, dstY, MARGIN, srcY, w, h, false);
	}

	static void paint (Event e, Canvas canvas, int [] state, Color [] palette) {
		GC gc = e.gc;
		Rectangle client = canvas.getClientArea ();
		if ((RetainedPlanes.drawnPlanes (gc) & RetainedPlanes.BODY) != 0) {
			gc.setBackground (e.display.getSystemColor (SWT.COLOR_GRAY));
			gc.fillRectangle (0, e.y, MARGIN, e.height);
			gc.setForeground (e.display.getSystemColor (SWT.COLOR_BLACK));
			gc.drawLine (MARGIN - 1, e.y, MARGIN - 1, e.y + e.height);
			int first = Math.floorDiv (state [0] + e.y, LINE), last = Math.floorDiv (state [0] + e.y + e.height, LINE);
			for (int line = first; line <= last; line++) {
				gc.setBackground (palette [Math.floorMod (line, palette.length)]);
				gc.fillRectangle (MARGIN, line * LINE - state [0], client.width - MARGIN, LINE);
			}
		}
		if (state [2] == 0) return; // the selection overlay appears only after the scroll phase, so every mode shows the same pixels
		RetainedPlanes.setPlane (gc, RetainedPlanes.SELECTION);
		gc.setBackground (e.display.getSystemColor (SWT.COLOR_WHITE));
		gc.fillRectangle (MARGIN + 20 + state [1] % 300, 50, 80, 30);
		RetainedPlanes.setPlane (gc, RetainedPlanes.BODY);
	}

	static void settle (Display display, Control control) throws InterruptedException {
		for (int i = 0; i < 20; i++) {
			control.update ();
			while (display.readAndDispatch ()) {
			}
			Thread.sleep (5);
		}
	}

	static int occludedRetries;
	static long overpaintPx, overpaintTiles, neededTotal, paintedTotal;

	/**
	 * The capture must show this shell: other agents' windows may pop up on this
	 * shared host. Samples nine points of the capture rectangle with WindowFromPoint
	 * and waits (up to 2 s) until each belongs to this shell; returns the retries.
	 */
	static int waitUnoccluded (Display display, Shell shell, Point origin, Rectangle outer) {
		org.eclipse.swt.internal.win32.POINT pt = new org.eclipse.swt.internal.win32.POINT ();
		for (int attempt = 0; attempt < 40; attempt++) {
			boolean clear = true;
			for (int i = 0; i < 9 && clear; i++) {
				pt.x = DPIUtil.pointToPixel (origin.x + 2 + (outer.width - 4) * (i % 3) / 2, DPIUtil.getDeviceZoom ());
				pt.y = DPIUtil.pointToPixel (origin.y + 2 + (outer.height - 4) * (i / 3) / 2, DPIUtil.getDeviceZoom ());
				long hwnd = org.eclipse.swt.internal.win32.OS.WindowFromPoint (pt);
				clear = org.eclipse.swt.internal.win32.OS.GetAncestor (hwnd, org.eclipse.swt.internal.win32.OS.GA_ROOT) == shell.handle;
			}
			if (clear) return attempt;
			shell.setActive ();
			shell.moveAbove (null);
			try {
				Thread.sleep (50);
			} catch (InterruptedException e) {
				Thread.currentThread ().interrupt ();
			}
			while (display.readAndDispatch ()) {
			}
		}
		return 1000;
	}

	/** Region pixels: border ring, scrollbar, margin, seams (device pixels of the screen capture). */
	static void writeFrame (DataOutputStream out, Display display, Shell shell, Canvas canvas, int zoom, int originPx) throws IOException {
		/*
		 * Capture mode: window-DC BitBlt (GetWindowDC of the control), i.e. the DWM
		 * redirection surface the widget presented into, client and non-client
		 * (border, scrollbar). It is immune to other windows overlapping the shell
		 * and to DWM composition timing on this shared host.
		 */
		Rectangle trim = canvas.computeTrim (0, 0, 0, 0);
		org.eclipse.swt.internal.win32.RECT window = new org.eclipse.swt.internal.win32.RECT ();
		org.eclipse.swt.internal.win32.OS.GetWindowRect (canvas.handle, window);
		org.eclipse.swt.internal.win32.POINT origin = new org.eclipse.swt.internal.win32.POINT ();
		org.eclipse.swt.internal.win32.OS.ClientToScreen (canvas.handle, origin);
		org.eclipse.swt.internal.win32.RECT clientRect = new org.eclipse.swt.internal.win32.RECT ();
		org.eclipse.swt.internal.win32.OS.GetClientRect (canvas.handle, clientRect);
		int ow = window.right - window.left, oh = window.bottom - window.top;
		int cx = origin.x - window.left, cy = origin.y - window.top, cw = clientRect.right, ch = clientRect.bottom;
		Image image = new Image (display, DPIUtil.pixelToPoint (ow, zoom), DPIUtil.pixelToPoint (oh, zoom));
		GC gc = new GC (image);
		long windowDC = org.eclipse.swt.internal.win32.OS.GetWindowDC (canvas.handle);
		org.eclipse.swt.internal.win32.OS.BitBlt (gc.handle, 0, 0, ow, oh, windowDC, 0, 0, org.eclipse.swt.internal.win32.OS.SRCCOPY);
		org.eclipse.swt.internal.win32.OS.ReleaseDC (canvas.handle, windowDC);
		gc.dispose ();
		ImageData data = image.getImageData (zoom);
		image.dispose ();
		String dump = System.getProperty ("seam.dump");
		if (dump != null && (FRAME [0] % 15 == 0 || (FRAME [0] >= 26 && FRAME [0] <= 31))) {
			try (OutputStream f = new BufferedOutputStream (new FileOutputStream (dump + "-" + FRAME [0] + ".ppm"))) {
				f.write (("P6\n" + data.width + " " + data.height + "\n255\n").getBytes ());
				for (int y = 0; y < data.height; y++) for (int x = 0; x < data.width; x++) {
					RGB rgb = data.palette.getRGB (data.getPixel (x, y));
					f.write (rgb.red); f.write (rgb.green); f.write (rgb.blue);
				}
			}
		}
		if (FRAME [0] == 0 && dump != null) System.err.println ("geometry cx=" + cx + " cy=" + cy + " cw=" + cw + " ch=" + ch + " img=" + data.width + "x" + data.height + " margin=" + DPIUtil.pointToPixel (MARGIN, zoom));
		FRAME [0]++;
		out.writeInt (data.width);
		out.writeInt (data.height);
		int margin = DPIUtil.pointToPixel (MARGIN, zoom);
		for (int y = 0; y < data.height; y++) {
			for (int x = 0; x < data.width; x++) {
				int lx = x - cx, ly = y - cy;
				boolean inClient = lx >= 0 && ly >= 0 && lx < cw && ly < ch;
				int region;
				if (!inClient) {
					region = lx >= cw && ly >= 0 && ly < ch ? 2 : 1; // 2 scrollbar column, 1 border ring
				} else if (lx < margin) {
					region = 3;
				} else {
					int mx = lx & 255, my = (ly + originPx) & 255;
					region = mx == 0 || mx == 255 || my == 0 || my == 255 ? 4 : 0;
				}
				if (region == 0) continue;
				RGB rgb = data.palette.getRGB (data.getPixel (x, y));
				out.writeByte (region);
				out.writeByte (rgb.red);
				out.writeByte (rgb.green);
				out.writeByte (rgb.blue);
			}
		}
		out.writeInt (trim.width);
	}
}
