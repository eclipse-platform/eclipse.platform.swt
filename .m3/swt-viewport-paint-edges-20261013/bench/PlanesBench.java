/*******************************************************************************
 * Copyright (c) 2026 Contributors to the Eclipse Foundation.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
import java.lang.management.*;
import java.util.*;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

/**
 * Leaf 2 batch-scope bench. A custom-drawn grid (Canvas, V_SCROLL, one text row per 18 px)
 * receives a data operation of N row changes, each followed by redraw of that row and a
 * scrollbar move. Modes:
 * <ul>
 * <li>{@code direct}: no batch, one redraw per change (what small operations should do);</li>
 * <li>{@code setredraw}: stock batch, setRedraw (false) ... setRedraw (true);</li>
 * <li>{@code viewport}: VisibilityGate.beginBatch (PLANE_VIEWPORT) ... endBatch (reflective, gated build only).</li>
 * </ul>
 * Prints: changes, mode, paint events, painted px (sum of Paint event rects), distinct on-screen
 * scrollbar thumb positions observed during the operation (screen capture of the scrollbar
 * column after each change: 1 means the scrollbar was frozen), operation ms, UI CPU ms.
 * <pre>java PlanesBench &lt;changes&gt; &lt;direct|setredraw|viewport&gt; &lt;label&gt;</pre>
 */
public class PlanesBench {
	static int paints;
	static long paintedPx;

	public static void main (String [] args) throws Exception {
		int changes = Integer.parseInt (args [0]);
		String mode = args [1], label = args [2];
		Display display = new Display ();
		Rectangle area = display.getPrimaryMonitor ().getClientArea ();
		Shell shell = new Shell (display, SWT.SHELL_TRIM | SWT.ON_TOP);
		shell.setLayout (new FillLayout ());
		shell.setBounds (area.x + area.width - 640, area.y + area.height - 520, 600, 480);
		String [] rows = new String [100_000];
		for (int i = 0; i < rows.length; i++) {
			rows [i] = "row " + i;
		}
		Canvas grid = new Canvas (shell, SWT.V_SCROLL | SWT.NO_BACKGROUND);
		ScrollBar bar = grid.getVerticalBar ();
		bar.setValues (0, 0, rows.length, 20, 1, 20);
		grid.addListener (SWT.Paint, e -> {
			paints++;
			paintedPx += (long) e.width * e.height;
			Rectangle client = grid.getClientArea ();
			e.gc.setBackground (display.getSystemColor (SWT.COLOR_WHITE));
			e.gc.fillRectangle (e.x, e.y, e.width, e.height);
			int top = bar.getSelection ();
			for (int y = (e.y / 18) * 18; y < e.y + e.height && y < client.height; y += 18) {
				int row = top + y / 18;
				if (row < rows.length) {
					e.gc.drawString (rows [row], 4, y, true);
				}
			}
		});
		long h = shell.handle;
		int ex = org.eclipse.swt.internal.win32.OS.GetWindowLong (h, org.eclipse.swt.internal.win32.OS.GWL_EXSTYLE);
		org.eclipse.swt.internal.win32.OS.SetWindowLong (h, org.eclipse.swt.internal.win32.OS.GWL_EXSTYLE,
			ex | org.eclipse.swt.internal.win32.OS.WS_EX_LAYERED | org.eclipse.swt.internal.win32.OS.WS_EX_TRANSPARENT);
		org.eclipse.swt.internal.win32.OS.SetLayeredWindowAttributes (h, 0, (byte) 0xFF, org.eclipse.swt.internal.win32.OS.LWA_ALPHA);
		shell.setVisible (true);
		settle (display, 300);

		java.lang.reflect.Method begin = null, end = null;
		int viewportPlane = 2;
		if ("viewport".equals (mode)) {
			Class<?> gate = Class.forName ("org.eclipse.swt.internal.VisibilityGate");
			begin = gate.getMethod ("beginBatch", Control.class, int.class);
			end = gate.getMethod ("endBatch", Control.class, int.class);
		}
		Rectangle client = grid.getClientArea ();
		Point column = grid.toDisplay (client.width, 0);
		int barWidth = bar.getSize ().x;
		Set<Integer> thumbFrames = new HashSet<> ();
		paints = 0;
		paintedPx = 0;
		ThreadMXBean mx = ManagementFactory.getThreadMXBean ();
		long cpu0 = mx.getCurrentThreadCpuTime (), t0 = System.nanoTime ();
		if ("setredraw".equals (mode)) {
			grid.setRedraw (false);
		} else if (begin != null) {
			begin.invoke (null, grid, viewportPlane);
		}
		for (int k = 0; k < changes; k++) {
			int row = (k * 7) % 20;
			rows [bar.getSelection () + row] = "row " + (bar.getSelection () + row) + " v" + k;
			grid.redraw (0, row * 18, client.width, 18, false);
			bar.setSelection (k % 2000);
			while (display.readAndDispatch ()) {
				// drain
			}
			if (changes <= 2000 || (k & 15) == 0) {
				thumbFrames.add (columnHash (display, column.x, column.y, barWidth, client.height));
			}
		}
		if ("setredraw".equals (mode)) {
			grid.setRedraw (true);
		} else if (end != null) {
			end.invoke (null, grid, viewportPlane);
		}
		grid.update ();
		long opMs = (System.nanoTime () - t0) / 1_000_000L, cpuMs = (mx.getCurrentThreadCpuTime () - cpu0) / 1_000_000L;
		settle (display, 200);
		System.out.println (String.join ("\t", Integer.toString (changes), mode, label, Integer.toString (paints), Long.toString (paintedPx),
			Integer.toString (thumbFrames.size ()), Long.toString (opMs), Long.toString (cpuMs)));
		display.dispose ();
	}

	static int columnHash (Display display, int x, int y, int w, int h) {
		Image image = new Image (display, w, h);
		GC gc = new GC (display);
		gc.copyArea (image, x, y);
		gc.dispose ();
		ImageData data = image.getImageData ();
		image.dispose ();
		return Arrays.hashCode (data.data);
	}

	static void settle (Display display, int ms) throws InterruptedException {
		long end = System.currentTimeMillis () + ms;
		while (System.currentTimeMillis () < end) {
			while (display.readAndDispatch ()) {
				// drain
			}
			Thread.sleep (10);
		}
	}
}
