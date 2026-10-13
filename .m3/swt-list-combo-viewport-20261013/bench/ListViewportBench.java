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
import java.lang.reflect.*;
import java.nio.charset.*;
import java.util.*;

import org.eclipse.swt.*;
import org.eclipse.swt.internal.win32.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.widgets.List;

/**
 * Before/after measurements for one List configuration and one item count.
 * One JVM per row; appends one TSV row to the output file.
 *
 * usage: ListViewportBench <label> <items> <setItems|setData> <out.tsv>
 *
 * Columns: label, items, feed, setItems_ms (or setItemCount_ms), first_paint_ms,
 * scroll_end_ms, drag_p50_ms, drag_p95_ms, drag_max_ms, heap_delta_mb,
 * private_delta_mb, handles_delta, gdi_delta, user_delta, set_data_calls,
 * count_check, selectAll_ms, getSelectionCount_ms, getSelectionIndices_ms.
 */
public class ListViewportBench {
	public static void main (String [] args) throws Exception {
		String label = args [0];
		int n = Integer.parseInt (args [1]);
		boolean setData = "setData".equals (args [2]);
		Display display = new Display ();
		Shell shell = new Shell (display);
		shell.setLayout (null);
		shell.setBounds (30, 30, 360, 420);
		List list = new List (shell, SWT.MULTI | SWT.V_SCROLL | SWT.BORDER | SWT.VIRTUAL);
		list.setBounds (5, 5, 330, 360);
		int [] setDataCalls = {0};
		if (setData) {
			list.addListener (SWT.SetData, e -> {
				setDataCalls [0]++;
				list.setItem (e.index, "item " + e.index);
			});
		}
		shell.open ();
		drain (display);
		String [] items = null;
		if (!setData) {
			items = new String [n];
			for (int i = 0; i < n; i++) items [i] = "item " + i;
		}
		ProcStat before = ProcStat.sample ();
		long t0 = System.nanoTime ();
		if (setData) {
			setItemCount (list, n);
		} else {
			list.setItems (items);
		}
		long t1 = System.nanoTime ();
		list.redraw ();
		list.update ();
		long t2 = System.nanoTime ();
		drain (display);
		list.setTopIndex (n - 1);
		list.update ();
		long t3 = System.nanoTime ();
		drain (display);
		list.setTopIndex (0);
		list.update ();
		drain (display);
		double [] frames = drag (list, 60);
		long s0 = System.nanoTime ();
		list.selectAll ();
		long s1 = System.nanoTime ();
		int selected = list.getSelectionCount ();
		long s2 = System.nanoTime ();
		int indices = list.getSelectionIndices ().length;
		long s3 = System.nanoTime ();
		list.deselectAll ();
		ProcStat after = ProcStat.sample ();
		String check = list.getItemCount () == n && ("item " + (n - 1)).equals (list.getItem (n - 1)) ? "ok" : "MISMATCH";
		Arrays.sort (frames);
		String row = String.join ("\t", label, String.valueOf (n), setData ? "setItemCount+SetData" : "setItems",
			ms (t1 - t0), ms (t2 - t1), ms (t3 - t2),
			f (frames [frames.length / 2]), f (frames [(int) Math.ceil (frames.length * 0.95) - 1]), f (frames [frames.length - 1]),
			mb (after.heapUsed - before.heapUsed), after.privateBytes < 0 || before.privateBytes < 0 ? "NA" : mb (after.privateBytes - before.privateBytes),
			String.valueOf (after.handles - before.handles), String.valueOf (after.gdi - before.gdi), String.valueOf (after.user - before.user),
			String.valueOf (setDataCalls [0]), check + (selected == n && indices == n ? "" : "/SELECTION_MISMATCH"),
			ms (s1 - s0), ms (s2 - s1), ms (s3 - s2));
		try (PrintWriter out = new PrintWriter (new OutputStreamWriter (new FileOutputStream (args [3], true), StandardCharsets.UTF_8))) {
			out.println (row);
		}
		System.out.println (row);
		if (items != null && items.length == 0) System.out.println ();
		shell.dispose ();
		display.dispose ();
	}

	static void setItemCount (List list, int n) throws Exception {
		Method method = List.class.getMethod ("setItemCount", int.class);
		method.invoke (list, n);
	}

	/* Press on row 0, move one row per frame (past the bottom edge, which autoscrolls), release. */
	static double [] drag (List list, int frames) {
		int itemHeight = list.getItemHeight ();
		Display display = list.getDisplay ();
		double [] times = new double [frames];
		OS.SendMessage (list.handle, OS.WM_LBUTTONDOWN, OS.MK_LBUTTON, OS.MAKELPARAM (20, itemHeight / 2));
		list.update ();
		for (int i = 0; i < frames; i++) {
			int y = itemHeight / 2 + (i + 1) * itemHeight;
			long t0 = System.nanoTime ();
			OS.SendMessage (list.handle, OS.WM_MOUSEMOVE, OS.MK_LBUTTON, OS.MAKELPARAM (20, y));
			list.update ();
			while (display.readAndDispatch ()) {
				// one frame = the move, its paint and anything it posted
			}
			times [i] = (System.nanoTime () - t0) / 1e6;
		}
		OS.SendMessage (list.handle, OS.WM_LBUTTONUP, 0, OS.MAKELPARAM (20, itemHeight / 2 + frames * itemHeight));
		return times;
	}

	static void drain (Display display) {
		while (display.readAndDispatch ()) {
			// drain
		}
	}

	static String ms (long nanos) {
		return String.format ("%.1f", nanos / 1e6);
	}

	static String f (double value) {
		return String.format ("%.2f", value);
	}

	static String mb (long bytes) {
		return String.format ("%.1f", bytes / 1048576.0);
	}
}
