/*******************************************************************************
 * Copyright (c) 2026 Synexia, Hitesh Soliwal (hsoliwal@gmail.com) and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Apache License, Version 2.0, or (at your option) the Eclipse
 * Public License 2.0, which are available at
 * https://www.apache.org/licenses/LICENSE-2.0 and
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: Apache-2.0 OR EPL-2.0
 *
 * Contributors:
 *     Hitesh Soliwal (Synexia) - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tests.junit;

import static org.eclipse.swt.tests.junit.VirtualPageSourceScaleHarness.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.*;

import java.util.*;
import java.util.concurrent.atomic.*;

import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.junit.jupiter.api.*;

/**
 * The same scale scenarios as {@link Test_org_eclipse_swt_widgets_VirtualPageSource_ScaleClassic} on the lazy layer: VirtualPageSource
 * (5 ms per block on worker threads) and SWT.VIRTUAL tab folders. Enabled with
 * -Dswt.lazy.scale=true.
 */
public class Test_org_eclipse_swt_widgets_VirtualPageSource_ScalePaged {
	private Display display;
	private Shell shell;
	private static final String MODE = "paged";

	@BeforeEach
	public void setUp () {
		assumeTrue (enabled (), "scale run disabled");
		display = Display.getDefault ();
		shell = new Shell (display);
		shell.setLayout (new FillLayout ());
		shell.setBounds (40, 40, 640, 720);
	}

	@AfterEach
	public void tearDown () {
		if (shell != null && !shell.isDisposed ()) shell.dispose ();
		if (display != null) settle (display, 50);
	}

	private void waitIdle (VirtualPageSource source) {
		long until = System.currentTimeMillis () + 10_000;
		while (!source.isIdle () && System.currentTimeMillis () < until) {
			if (!display.readAndDispatch ()) sleep (1);
		}
		while (display.readAndDispatch ()) {
			// flush
		}
	}

	@Test
	public void table10MillionRowsSlowSource () {
		int rows = Test_org_eclipse_swt_widgets_VirtualPageSource_ScaleClassic.ROWS;
		Table table = new Table (shell, SWT.VIRTUAL | SWT.BORDER | SWT.FULL_SELECTION);
		table.setHeaderVisible (true);
		for (int c = 0; c < 2; c++) new TableColumn (table, SWT.NONE).setWidth (200);
		table.setItemCount (rows);
		AtomicInteger fetcherCalls = new AtomicInteger ();
		VirtualPageSource source = new VirtualPageSource (Test_org_eclipse_swt_widgets_VirtualPageSource_ScaleClassic.BLOCK, Test_org_eclipse_swt_widgets_VirtualPageSource_ScaleClassic.MAX_BLOCKS, 2,
				VirtualPageSource.NONE, request -> {
					fetcherCalls.incrementAndGet ();
					Thread.sleep (BLOCK_LATENCY_MS);
					if (request.isCancelled ()) return;
					for (int i = request.getStart (), end = i + request.getCount (); i < end; i++) {
						request.setText (i, 0, "row " + i);
						request.setText (i, 1, "#" + i);
					}
				});
		source.attach (table);
		long heapBefore = heapUsedAfterGc ();
		long t0 = System.nanoTime ();
		shell.open ();
		table.update ();
		while (display.readAndDispatch ()) {
			// first frame
		}
		double firstPaint = (System.nanoTime () - t0) / 1e6;
		while (!source.isLoaded (VirtualPageSource.ROOT, table.getTopIndex ())) {
			if (!display.readAndDispatch ()) sleep (1);
		}
		table.update ();
		double firstData = (System.nanoTime () - t0) / 1e6;
		int visible = table.getClientArea ().height / table.getItemHeight ();
		int [] maxFacades = {tableFacades (table)};
		int [] maxRequests = {0};
		int [] lastRequested = {source.getCount (VirtualPageSource.REQUESTED)};
		java.util.function.IntConsumer sample = f -> {
			maxFacades [0] = Math.max (maxFacades [0], tableFacades (table));
			int requested = source.getCount (VirtualPageSource.REQUESTED);
			maxRequests [0] = Math.max (maxRequests [0], requested - lastRequested [0]);
			lastRequested [0] = requested;
		};
		double [] page = frames (display, 300, f -> {
			table.setTopIndex (Math.min (rows - 1, (f + 1) * visible));
			table.update ();
			sample.accept (f);
		});
		int [] tops = jumps (300, rows, 42);
		double [] jump = frames (display, 300, f -> {
			table.setTopIndex (tops [f]);
			table.update ();
			sample.accept (f);
		});
		int facadesWhileScrolling = maxFacades [0];
		waitIdle (source);
		String s = "table_10M";
		report (s, MODE, "first_paint_ms", String.format (Locale.ROOT, "%.1f", firstPaint));
		report (s, MODE, "first_data_ms", String.format (Locale.ROOT, "%.1f", firstData));
		reportFrames (s, MODE, "page", page);
		reportFrames (s, MODE, "jump", jump);
		report (s, MODE, "blocks_requested", source.getCount (VirtualPageSource.REQUESTED));
		report (s, MODE, "blocks_fetched", source.getCount (VirtualPageSource.FETCHED));
		report (s, MODE, "blocks_cancelled", source.getCount (VirtualPageSource.CANCELLED));
		report (s, MODE, "fetcher_calls", fetcherCalls.get ());
		report (s, MODE, "blocks_resident_max", Test_org_eclipse_swt_widgets_VirtualPageSource_ScaleClassic.MAX_BLOCKS);
		report (s, MODE, "blocks_resident_end", source.getCount (VirtualPageSource.RESIDENT));
		report (s, MODE, "frames_drained", source.getCount (VirtualPageSource.FRAMES));
		report (s, MODE, "facades_released", source.getCount (VirtualPageSource.RELEASED));
		report (s, MODE, "facades_max_during_scroll_storm", facadesWhileScrolling);
		report (s, MODE, "block_requests_max_per_frame", maxRequests [0]);
		report (s, MODE, "paint_answers_facade_free", intField (table, "pagePaintAnswers"));
		report (s, MODE, "heap_delta_mb", String.format (Locale.ROOT, "%.1f", (heapUsedAfterGc () - heapBefore) / 1048576.0));
		assertEquals ("row " + table.getTopIndex (), table.getItem (table.getTopIndex ()).getText ());
		assertTrue (source.getCount (VirtualPageSource.RESIDENT) <= Test_org_eclipse_swt_widgets_VirtualPageSource_ScaleClassic.MAX_BLOCKS);
		assertEquals (0, facadesWhileScrolling, "paint never materializes a TableItem");
	}

	@Test
	public void tree1MillionNodes () {
		int roots = Test_org_eclipse_swt_widgets_VirtualPageSource_ScaleClassic.TREE_ROOTS, children = Test_org_eclipse_swt_widgets_VirtualPageSource_ScaleClassic.TREE_CHILDREN;
		Tree tree = new Tree (shell, SWT.VIRTUAL | SWT.BORDER);
		AtomicInteger fetcherCalls = new AtomicInteger ();
		VirtualPageSource source = new VirtualPageSource (64, 64, 1, VirtualPageSource.TREE, request -> {
			fetcherCalls.incrementAndGet ();
			Thread.sleep (BLOCK_LATENCY_MS);
			int parent = request.getParentKey ();
			if (parent == VirtualPageSource.ROOT) {
				request.setTotal (roots);
				for (int i = request.getStart (), end = Math.min (roots, i + request.getCount ()); i < end; i++) {
					request.setText (i, 0, "node " + i);
					request.setKey (i, i);
					request.setChildCount (i, VirtualPageSource.UNKNOWN);
				}
			} else {
				request.setTotal (children);
				for (int i = request.getStart (), end = Math.min (children, i + request.getCount ()); i < end; i++) {
					request.setText (i, 0, "leaf " + i);
					request.setKey (i, 1_000_000 + parent * children + i);
					request.setChildCount (i, 0);
				}
			}
		});
		source.attach (tree);
		Test_org_eclipse_swt_widgets_VirtualPageSource_ScaleClassic runner = new Test_org_eclipse_swt_widgets_VirtualPageSource_ScaleClassic ();
		runner.useShell (display, shell);
		runner.runTree (tree, MODE, fetcherCalls::get, () -> waitIdle (source));
		report ("tree_1M", MODE, "blocks_fetched", source.getCount (VirtualPageSource.FETCHED));
		report ("tree_1M", MODE, "blocks_cancelled", source.getCount (VirtualPageSource.CANCELLED));
	}

	/** Process working set (RSS) in MB from tasklist, win32; -1 when unavailable. */
	static long rssMb () {
		try {
			Process process = new ProcessBuilder ("tasklist", "/FI", "PID eq " + ProcessHandle.current ().pid (), "/FO", "CSV", "/NH").start ();
			String line = new String (process.getInputStream ().readAllBytes ()).trim ();
			process.waitFor ();
			String [] cells = line.split ("\",\"");
			String kb = cells [cells.length - 1].replaceAll ("[^0-9]", "");
			return Long.parseLong (kb) / 1024;
		} catch (Exception e) {
			return -1;
		}
	}

	@Test
	public void treeBillionNodesFlatHeap () {
		assumeTrue (Boolean.getBoolean ("swt.lazy.billion"), "billion-node run disabled");
		int [][] sizes = {{1_000, 1_000}, {10_000, 10_000}, {31_623, 31_623}};
		for (int [] size : sizes) runBillion (size [0], size [1]);
	}

	private void runBillion (int roots, int children) {
		Shell s = new Shell (display);
		s.setLayout (new FillLayout ());
		s.setBounds (40, 40, 640, 720);
		Tree tree = new Tree (s, SWT.VIRTUAL | SWT.BORDER);
		AtomicInteger fetcherCalls = new AtomicInteger ();
		VirtualPageSource source = new VirtualPageSource (64, 64, 1, VirtualPageSource.TREE, request -> {
			fetcherCalls.incrementAndGet ();
			Thread.sleep (BLOCK_LATENCY_MS);
			int parent = request.getParentKey ();
			boolean root = parent == VirtualPageSource.ROOT;
			int total = root ? roots : children;
			request.setTotal (total);
			for (int i = request.getStart (), end = Math.min (total, i + request.getCount ()); i < end; i++) {
				request.setText (i, 0, root ? "node " + i : "leaf " + parent + "." + i);
				request.setKey (i, root ? i : -2);
				request.setChildCount (i, root ? VirtualPageSource.UNKNOWN : 0);
			}
		});
		source.attach (tree);
		s.open ();
		waitIdle (source);
		nativeExpand (tree, tree.getItem (0));
		waitIdle (source);
		long t = System.nanoTime ();
		tree.setTopItem (tree.getItem (roots / 2));
		tree.update ();
		while (display.readAndDispatch ()) {
			// frame
		}
		double middle = (System.nanoTime () - t) / 1e6;
		waitIdle (source);
		double middleData = (System.nanoTime () - t) / 1e6;
		t = System.nanoTime ();
		tree.setTopItem (tree.getItem (roots - 1));
		tree.update ();
		while (display.readAndDispatch ()) {
			// frame
		}
		double end = (System.nanoTime () - t) / 1e6;
		waitIdle (source);
		double endData = (System.nanoTime () - t) / 1e6;
		t = System.nanoTime ();
		TreeItem first = tree.getItem (0);
		tree.setTopItem (first.getItem (first.getItemCount () - 1));
		tree.update ();
		while (display.readAndDispatch ()) {
			// frame
		}
		waitIdle (source);
		double childEndData = (System.nanoTime () - t) / 1e6;
		String text = tree.getItem (roots - 1).getText ();
		long logical = (long) roots + (long) roots * children;
		String scenario = "tree_billion";
		String mode = "logical_" + logical;
		report (scenario, mode, "heap_used_mb", String.format (Locale.ROOT, "%.1f", heapUsedAfterGc () / 1048576.0));
		report (scenario, mode, "rss_mb", rssMb ());
		report (scenario, mode, "xmx_mb", Runtime.getRuntime ().maxMemory () / 1048576);
		report (scenario, mode, "scroll_to_middle_ms", String.format (Locale.ROOT, "%.1f", middle));
		report (scenario, mode, "scroll_to_middle_data_ms", String.format (Locale.ROOT, "%.1f", middleData));
		report (scenario, mode, "scroll_to_end_ms", String.format (Locale.ROOT, "%.1f", end));
		report (scenario, mode, "scroll_to_end_data_ms", String.format (Locale.ROOT, "%.1f", endData));
		report (scenario, mode, "scroll_to_child_end_data_ms", String.format (Locale.ROOT, "%.1f", childEndData));
		report (scenario, mode, "native_items", nativeTreeCount (tree));
		report (scenario, mode, "facades", treeFacades (tree));
		report (scenario, mode, "fetcher_calls", fetcherCalls.get ());
		assertEquals ("node " + (roots - 1), text);
		assertTrue (nativeTreeCount (tree) <= (long) roots + children + 1, "expand-all never forced");
		s.dispose ();
		while (display.readAndDispatch ()) {
			// flush
		}
	}

	@Test
	public void ctabFolder50HeavyTabsLazy () {
		runTabs (true);
	}

	@Test
	public void tabFolder50HeavyTabsLazy () {
		runTabs (false);
	}

	void runTabs (boolean custom) {
		int [] controls = new int [1];
		int [] setData = new int [1];
		long heapBefore = heapUsedAfterGc ();
		long t0 = System.nanoTime ();
		Composite folder;
		if (custom) {
			CTabFolder c = new CTabFolder (shell, SWT.BORDER | SWT.VIRTUAL);
			c.addListener (SWT.SetData, e -> {
				setData [0]++;
				((CTabItem) e.item).setControl (heavyContent (c, controls));
			});
			for (int i = 0; i < 50; i++) new CTabItem (c, SWT.NONE).setText ("Tab " + i);
			c.setSelection (0);
			folder = c;
		} else {
			TabFolder t = new TabFolder (shell, SWT.VIRTUAL);
			t.addListener (SWT.SetData, e -> {
				setData [0]++;
				((TabItem) e.item).setControl (heavyContent (t, controls));
			});
			for (int i = 0; i < 50; i++) new TabItem (t, SWT.NONE).setText ("Tab " + i);
			folder = t;
		}
		shell.open ();
		folder.update ();
		while (display.readAndDispatch ()) {
			// first frame
		}
		double startup = (System.nanoTime () - t0) / 1e6;
		int atStartup = controls [0];
		int tabsAtStartup = setData [0];
		long heapStartup = heapUsedAfterGc () - heapBefore;
		double [] switches = frames (display, 50, f -> {
			folderSelect (folder, (f * 7) % 50);
			folder.update ();
		});
		String s = custom ? "ctabfolder_50" : "tabfolder_50";
		report (s, MODE, "startup_to_first_paint_ms", String.format (Locale.ROOT, "%.1f", startup));
		report (s, MODE, "controls_at_startup", atStartup);
		report (s, MODE, "tabs_filled_at_startup", tabsAtStartup);
		report (s, MODE, "heap_at_startup_mb", String.format (Locale.ROOT, "%.1f", heapStartup / 1048576.0));
		reportFrames (s, MODE, "switch", switches);
		report (s, MODE, "controls_end", controls [0]);
		assertEquals (1, tabsAtStartup, "startup creates only the selected tab");
	}
}
