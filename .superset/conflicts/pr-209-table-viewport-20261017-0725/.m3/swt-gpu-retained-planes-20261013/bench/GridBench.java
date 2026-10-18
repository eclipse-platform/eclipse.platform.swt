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

import org.eclipse.nebula.widgets.grid.*;
import org.eclipse.swt.*;
import org.eclipse.swt.internal.gpu.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

/**
 * Nebula Grid (fully GC-drawn Canvas) frame times on the retained-plane layer:
 * 1,000,000 virtual rows, 4 columns. Same TSV columns as FrameBench (subset).
 * <p>usage: GridBench &lt;scroll|select|caret&gt; [frames] [header]</p>
 */
public class GridBench {
	public static void main (String [] args) throws Exception {
		String scenario = args [0];
		int frames = args.length > 1 ? Integer.parseInt (args [1]) : 300;
		Display display = new Display ();
		Shell shell = new Shell (display);
		shell.setLayout (new FillLayout ());
		Grid grid = new Grid (shell, SWT.VIRTUAL | SWT.V_SCROLL | SWT.H_SCROLL | SWT.MULTI | SWT.FULL_SELECTION);
		grid.setHeaderVisible (true);
		for (int c = 0; c < 4; c++) {
			GridColumn column = new GridColumn (grid, SWT.NONE);
			column.setText ("Column " + c);
			column.setWidth (260);
		}
		grid.addListener (SWT.SetData, e -> {
			GridItem item = (GridItem) e.item;
			int row = grid.indexOf (item);
			for (int c = 0; c < 4; c++) item.setText (c, "r" + row + " c" + c + " value " + (row * 31 + c) % 9973);
		});
		grid.setItemCount (1_000_000);
		shell.setBounds (20, 20, 1200, 800);
		shell.open ();
		shell.forceActive ();
		grid.setFocus ();
		for (int i = 0; i < 30; i++) {
			grid.update ();
			while (display.readAndDispatch ()) {
			}
			Thread.sleep (5);
		}
		com.sun.management.ThreadMXBean threads = (com.sun.management.ThreadMXBean) ManagementFactory.getThreadMXBean ();
		long tid = Thread.currentThread ().getId ();
		double [] wall = new double [frames];
		long cpu = 0, alloc = 0;
		long [] before = new long [GpuCounters.COUNT], after = new long [GpuCounters.COUNT];
		for (int i = -40; i < frames; i++) {
			if (i == 0) GpuCounters.snapshot (before);
			long c0 = threads.getCurrentThreadCpuTime (), a0 = threads.getThreadAllocatedBytes (tid), t0 = System.nanoTime ();
			switch (scenario) {
				case "scroll" -> grid.setTopIndex ((i + 40) % 900_000);
				case "select" -> grid.setSelection ((i + 40) % 40);
				default -> grid.redraw (0, 0, 1200, grid.getItemHeight (), false);
			}
			grid.update ();
			while (display.readAndDispatch ()) {
			}
			if (i >= 0) {
				wall [i] = (System.nanoTime () - t0) / 1e6;
				cpu += threads.getCurrentThreadCpuTime () - c0;
				alloc += threads.getThreadAllocatedBytes (tid) - a0;
			}
		}
		GpuCounters.snapshot (after);
		Arrays.sort (wall);
		if (args.length > 2) System.out.println ("mode\twidget\tscenario\tframes\tp50ms\tp95ms\tmaxms\tcpuMsMean\tallocBytesMean\tgpuWaitMsMean\tgpuRasterTiles\trecordFails\tfailBits\tblobHit%\tfocus");
		long gpuWait = after [GpuCounters.GPU_WAIT_NANOS] - before [GpuCounters.GPU_WAIT_NANOS];
		long [] d = new long [GpuCounters.COUNT];
		for (int i = 0; i < d.length; i++) d [i] = after [i] - before [i];
		double blob = GpuCounters.hitRate (d, GpuCounters.BLOB_HIT);
		System.out.println (String.join ("\t", System.getProperty ("swt.gpu", "off"), "nebula-grid", scenario, "" + frames,
			f (wall [frames / 2]), f (wall [(int) (frames * 0.95)]), f (wall [frames - 1]), f (cpu / 1e6 / frames), "" + alloc / frames,
			f (gpuWait / 1e6 / frames), "" + d [GpuCounters.GPU_RASTER_TILES], "" + d [GpuCounters.RECORD_FAILS], "" + after [GpuCounters.RECORD_FAIL_BITS],
			blob < 0 ? "-" : f (blob), "" + (display.getFocusControl () == grid)));
		display.dispose ();
	}

	static String f (double v) {
		return String.format (Locale.ROOT, "%.3f", v);
	}
}
