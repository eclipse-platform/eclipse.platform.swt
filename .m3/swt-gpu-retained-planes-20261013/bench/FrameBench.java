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
import java.lang.management.*;
import java.util.*;

import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.gpu.*;
import org.eclipse.swt.widgets.*;

/**
 * Frame-time benchmark for the retained-plane layer in the current -Dswt.gpu
 * mode. One frame = one state change + Control.update() (a synchronous
 * WM_PAINT that rasterizes, composites and presents).
 * <p>usage: FrameBench &lt;styledtext|table|tree|canvas&gt; &lt;scroll|select|caret&gt; [frames] [header]</p>
 * Prints one TSV row: wall p50/p95/max ms, mean thread CPU ms, mean allocated
 * bytes per frame, mean GPU wait ms, counter deltas and hit rates.
 */
public class FrameBench {
	static final int W = 1200, H = 800, WARMUP = 40;
	static final com.sun.management.ThreadMXBean THREADS = (com.sun.management.ThreadMXBean) ManagementFactory.getThreadMXBean ();

	interface Step {
		void frame (int i);
	}

	public static void main (String [] args) throws Exception {
		String widget = args [0], scenario = args [1];
		int frames = args.length > 2 ? Integer.parseInt (args [2]) : 300;
		Display display = new Display ();
		Shell shell = new Shell (display, SWT.SHELL_TRIM);
		shell.setLayout (new org.eclipse.swt.layout.FillLayout ());
		Control control = build (shell, widget);
		shell.setBounds (20, 20, W, H);
		shell.open ();
		shell.forceActive ();
		control.setFocus ();
		settle (display, control, 30);
		Step step = step (control, widget, scenario);
		for (int i = 0; i < WARMUP; i++) frame (display, control, step, i);
		long [] before = new long [GpuCounters.COUNT], after = new long [GpuCounters.COUNT];
		double [] wall = new double [frames];
		long cpu = 0, alloc = 0, tid = Thread.currentThread ().getId ();
		GpuCounters.snapshot (before);
		for (int i = 0; i < frames; i++) {
			long c0 = THREADS.getCurrentThreadCpuTime (), a0 = THREADS.getThreadAllocatedBytes (tid), t0 = System.nanoTime ();
			frame (display, control, step, WARMUP + i);
			wall [i] = (System.nanoTime () - t0) / 1e6;
			cpu += THREADS.getCurrentThreadCpuTime () - c0;
			alloc += THREADS.getThreadAllocatedBytes (tid) - a0;
		}
		GpuCounters.snapshot (after);
		long [] d = new long [GpuCounters.COUNT];
		for (int i = 0; i < d.length; i++) d [i] = after [i] - before [i];
		boolean focus = display.getFocusControl () == control;
		Arrays.sort (wall);
		if (args.length > 3) {
			System.out.println ("mode\twidget\tscenario\tframes\tp50ms\tp95ms\tmaxms\tcpuMsMean\tallocBytesMean\tgpuWaitMsMean\tpenHit%\tbrushHit%\tfontHit%\tpaintHit%\tblobHit%\ttileReused\ttileRaster\tgpuRasterTiles\tfallbacks\tgpuBytes\tcpuTileBytes\trecordFails\tfailBits\topsRecorded\tnativeCreates\tfocus");
		}
		System.out.println (String.join ("\t", System.getProperty ("swt.gpu", "off"), widget, scenario, "" + frames,
			f (wall [frames / 2]), f (wall [(int) (frames * 0.95)]), f (wall [frames - 1]),
			f (cpu / 1e6 / frames), "" + alloc / frames, f (d [GpuCounters.GPU_WAIT_NANOS] / 1e6 / frames),
			rate (d, GpuCounters.PEN_HIT), rate (d, GpuCounters.BRUSH_HIT), rate (d, GpuCounters.FONT_HIT), rate (d, GpuCounters.PAINT_HIT), rate (d, GpuCounters.BLOB_HIT),
			"" + d [GpuCounters.TILE_REUSED], "" + d [GpuCounters.TILE_RASTER], "" + d [GpuCounters.GPU_RASTER_TILES], "" + after [GpuCounters.FALLBACKS],
			"" + after [GpuCounters.GPU_TILE_BYTES], "" + after [GpuCounters.CPU_TILE_BYTES],
			"" + d [GpuCounters.RECORD_FAILS], "" + after [GpuCounters.RECORD_FAIL_BITS], "" + d [GpuCounters.OPS_RECORDED], "" + d [GpuCounters.NATIVE_CREATES], "" + focus));
		display.dispose ();
	}

	static String f (double v) {
		return String.format (Locale.ROOT, "%.3f", v);
	}

	static String rate (long [] d, int hitId) {
		double r = GpuCounters.hitRate (d, hitId);
		return r < 0 ? "-" : String.format (Locale.ROOT, "%.1f", r);
	}

	static void frame (Display display, Control control, Step step, int i) {
		step.frame (i);
		control.update ();
		while (display.readAndDispatch ()) {
		}
	}

	static void settle (Display display, Control control, int rounds) throws InterruptedException {
		for (int i = 0; i < rounds; i++) {
			control.update ();
			while (display.readAndDispatch ()) {
			}
			Thread.sleep (5);
		}
	}

	static Control build (Shell shell, String widget) {
		switch (widget) {
			case "styledtext": {
				StyledText text = new StyledText (shell, SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL);
				text.setFont (new Font (shell.getDisplay (), "Consolas", 10, SWT.NORMAL));
				StringBuilder b = new StringBuilder (100_000 * 90);
				for (int i = 0; i < 100_000; i++) b.append ("line ").append (i).append (": the quick brown fox jumps over the lazy dog; retained glyph runs 0123456789\n");
				text.setText (b.toString ());
				return text;
			}
			case "table": {
				Table table = new Table (shell, SWT.VIRTUAL | SWT.FULL_SELECTION | SWT.MULTI | SWT.BORDER);
				table.setHeaderVisible (true);
				for (int c = 0; c < 3; c++) new TableColumn (table, SWT.NONE).setWidth (300);
				table.addListener (SWT.SetData, e -> {
					TableItem item = (TableItem) e.item;
					int row = table.indexOf (item);
					item.setText (new String [] {"row " + row, "value " + (row * 7 % 1000), "detail " + (row % 97)});
				});
				table.setItemCount (1_000_000);
				return table;
			}
			case "tree": {
				Tree tree = new Tree (shell, SWT.VIRTUAL | SWT.FULL_SELECTION | SWT.MULTI | SWT.BORDER);
				tree.addListener (SWT.SetData, e -> {
					TreeItem item = (TreeItem) e.item;
					TreeItem parent = item.getParentItem ();
					int index = parent == null ? tree.indexOf (item) : parent.indexOf (item);
					item.setText ("node " + index);
				});
				tree.setItemCount (1_000_000);
				return tree;
			}
			default: {
				Canvas canvas = new Canvas (shell, SWT.V_SCROLL);
				canvas.setBackground (shell.getDisplay ().getSystemColor (SWT.COLOR_WHITE));
				int [] state = {0, 0};
				canvas.setData ("state", state);
				canvas.addListener (SWT.Paint, e -> paintCanvas (e, state));
				return canvas;
			}
		}
	}

	/** Canvas client: 19 px lines, scrolls with Canvas.scroll; selection and caret in overlay planes. */
	static void paintCanvas (Event e, int [] state) {
		GC gc = e.gc;
		Display d = e.display;
		int first = Math.max (0, (state [0] + e.y) / 19), last = (state [0] + e.y + e.height) / 19;
		/* plane-aware client: skip body work in an overlay-only pass */
		if ((RetainedPlanes.drawnPlanes (gc) & RetainedPlanes.BODY) == 0) last = first - 1;
		for (int line = first; line <= last; line++) {
			int y = line * 19 - state [0];
			gc.setForeground (d.getSystemColor (line % 5 == 0 ? SWT.COLOR_DARK_BLUE : SWT.COLOR_BLACK));
			gc.drawString ("line " + line + ": the quick brown fox jumps over the lazy dog; retained glyph runs", 6, y, true);
		}
		RetainedPlanes.setPlane (gc, RetainedPlanes.SELECTION);
		gc.setBackground (d.getSystemColor (SWT.COLOR_LIST_SELECTION));
		gc.setAlpha (255);
		gc.fillRectangle (6, 40, 40 + state [1] % 600, 19 * 3);
		RetainedPlanes.setPlane (gc, RetainedPlanes.CARET);
		if ((state [1] & 1) == 0) {
			gc.setBackground (d.getSystemColor (SWT.COLOR_BLACK));
			gc.fillRectangle (46 + state [1] % 600, 40, 2, 19);
		}
		RetainedPlanes.setPlane (gc, RetainedPlanes.BODY);
	}

	static Step step (Control control, String widget, String scenario) {
		if (control instanceof StyledText text) {
			int lineHeight = text.getLineHeight ();
			return switch (scenario) {
				case "scroll" -> i -> text.setTopPixel ((i % 4000) * lineHeight);
				case "select" -> i -> text.setSelection (text.getOffsetAtLine (20), text.getOffsetAtLine (20) + 37 * (i % 400));
				default -> i -> text.getCaret ().setVisible ((i & 1) == 0);
			};
		}
		if (control instanceof Table table) {
			return switch (scenario) {
				case "scroll" -> i -> table.setTopIndex (i % 900_000);
				case "select" -> i -> table.setSelection (0, i % 40);
				default -> i -> table.redraw (0, 0, W, table.getItemHeight (), false);
			};
		}
		if (control instanceof Tree tree) {
			return switch (scenario) {
				case "scroll" -> i -> tree.setTopItem (tree.getItem (i % 900_000));
				case "select" -> i -> tree.setSelection (tree.getItem (i % 40));
				default -> i -> tree.redraw (0, 0, W, tree.getItemHeight (), false);
			};
		}
		Canvas canvas = (Canvas) control;
		int [] state = (int []) canvas.getData ("state");
		return switch (scenario) {
			case "scroll" -> i -> {
				int dy = (i / 50) % 2 == 0 ? 19 : -19;
				state [0] += dy;
				Rectangle area = canvas.getClientArea ();
				if (dy > 0) canvas.scroll (0, 0, 0, dy, area.width, area.height - dy, false);
				else canvas.scroll (0, -dy, 0, 0, area.width, area.height + dy, false);
			};
			case "select" -> i -> {
				state [1] = (state [1] + 2) & ~1;
				RetainedPlanes.invalidatePlanes (canvas, RetainedPlanes.SELECTION, 0, 30, W, 19 * 5);
			};
			default -> i -> {
				state [1] ^= 1;
				RetainedPlanes.invalidatePlanes (canvas, RetainedPlanes.CARET, 40, 40, W - 40, 19);
			};
		};
	}
}
