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

import static org.eclipse.swt.tests.junit.LazyScaleHarness.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.*;

import java.util.*;

import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.junit.jupiter.api.*;

/**
 * Scale baseline with the classic API only (synchronous SWT.SetData, eager tab content), so it
 * runs on the unmodified widgets ("before") and on the lazy branch. Enabled with
 * -Dswt.lazy.scale=true; metrics go to stdout and -Dswt.lazy.report.
 */
public class Test_LazyScaleClassic {
	static final int ROWS = 10_000_000;
	static final int BLOCK = 256;
	static final int MAX_BLOCKS = 64;
	private Display display;
	private Shell shell;
	private final String mode = System.getProperty ("swt.lazy.mode", "classic");

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

	/** Lets the paged scale test reuse the tree runner on its own shell. */
	void useShell (Display display, Shell shell) {
		this.display = display;
		this.shell = shell;
	}

	/** The best synchronous app: an LRU block cache with the same block size, 5 ms per miss. */
	static final class SyncBlocks {
		final LinkedHashMap<Integer, String []> blocks = new LinkedHashMap<> (MAX_BLOCKS, 0.75f, true) {
			private static final long serialVersionUID = 1L;

			@Override
			protected boolean removeEldestEntry (Map.Entry<Integer, String []> eldest) {
				return size () > MAX_BLOCKS;
			}
		};
		int fetched;

		String text (int row) {
			int block = row / BLOCK;
			String [] cells = blocks.get (block);
			if (cells == null) {
				sleep (BLOCK_LATENCY_MS);
				fetched++;
				cells = new String [BLOCK];
				for (int i = 0; i < BLOCK; i++) cells [i] = "row " + (block * BLOCK + i);
				blocks.put (block, cells);
			}
			return cells [row - block * BLOCK];
		}
	}

	@Test
	public void table10MillionRowsSlowSource () {
		Table table = new Table (shell, SWT.VIRTUAL | SWT.BORDER | SWT.FULL_SELECTION);
		table.setHeaderVisible (true);
		for (int c = 0; c < 2; c++) new TableColumn (table, SWT.NONE).setWidth (200);
		SyncBlocks blocks = new SyncBlocks ();
		table.addListener (SWT.SetData, e -> {
			TableItem item = (TableItem) e.item;
			String text = blocks.text (e.index);
			item.setText (0, text);
			item.setText (1, "#" + e.index);
		});
		table.setItemCount (ROWS);
		long heapBefore = heapUsedAfterGc ();
		long t0 = System.nanoTime ();
		shell.open ();
		table.update ();
		while (display.readAndDispatch ()) {
			// first frame
		}
		double firstPaint = (System.nanoTime () - t0) / 1e6;
		int visible = table.getClientArea ().height / table.getItemHeight ();
		int [] maxFacades = {tableFacades (table)};
		double [] page = frames (display, 300, f -> {
			table.setTopIndex (Math.min (ROWS - 1, (f + 1) * visible));
			table.update ();
			maxFacades [0] = Math.max (maxFacades [0], tableFacades (table));
		});
		int [] tops = jumps (300, ROWS, 42);
		double [] jump = frames (display, 300, f -> {
			table.setTopIndex (tops [f]);
			table.update ();
			maxFacades [0] = Math.max (maxFacades [0], tableFacades (table));
		});
		String s = "table_10M";
		report (s, mode, "first_paint_ms", String.format (Locale.ROOT, "%.1f", firstPaint));
		report (s, mode, "first_data_ms", String.format (Locale.ROOT, "%.1f", firstPaint));
		reportFrames (s, mode, "page", page);
		reportFrames (s, mode, "jump", jump);
		report (s, mode, "blocks_fetched", blocks.fetched);
		report (s, mode, "blocks_cancelled", 0);
		report (s, mode, "facades_materialized", tableFacades (table));
		report (s, mode, "facades_max_during_scroll_storm", maxFacades [0]);
		report (s, mode, "heap_delta_mb", String.format (Locale.ROOT, "%.1f", (heapUsedAfterGc () - heapBefore) / 1048576.0));
		assertEquals ("row " + table.getTopIndex (), table.getItem (table.getTopIndex ()).getText ());
	}

	static final int TREE_ROOTS = 1_000;
	static final int TREE_CHILDREN = 1_000;

	@Test
	public void tree1MillionNodes () {
		Tree tree = new Tree (shell, SWT.VIRTUAL | SWT.BORDER);
		int [] setData = new int [1];
		tree.addListener (SWT.SetData, e -> {
			setData [0]++;
			TreeItem item = (TreeItem) e.item;
			TreeItem parent = item.getParentItem ();
			if (parent == null) {
				item.setText ("node " + e.index);
				item.setItemCount (TREE_CHILDREN);
			} else {
				item.setText ("leaf " + e.index);
				item.setItemCount (0);
			}
		});
		tree.setItemCount (TREE_ROOTS);
		runTree (tree, "classic", () -> setData [0], null);
	}

	void runTree (Tree tree, String treeMode, java.util.function.IntSupplier work, Runnable waitLoaded) {
		long heapBefore = heapUsedAfterGc ();
		long t0 = System.nanoTime ();
		shell.open ();
		tree.update ();
		while (display.readAndDispatch ()) {
			// first frame
		}
		double firstPaint = (System.nanoTime () - t0) / 1e6;
		if (waitLoaded != null) waitLoaded.run ();
		double firstData = (System.nanoTime () - t0) / 1e6;
		long nativeAfterOpen = nativeTreeCount (tree);
		double [] expand = new double [5];
		for (int k = 0; k < expand.length; k++) {
			TreeItem item = tree.getItem (k * 3);
			long start = System.nanoTime ();
			nativeExpand (tree, item);
			tree.update ();
			while (display.readAndDispatch ()) {
				// expand frame
			}
			expand [k] = (System.nanoTime () - start) / 1e6;
			if (waitLoaded != null) waitLoaded.run ();
		}
		tree.setTopItem (tree.getItem (0));
		int facadesBeforeScroll = treeFacades (tree);
		int [] maxFacades = {facadesBeforeScroll};
		double [] scroll = frames (display, 200, f -> {
			nativeScroll (tree, "SB_PAGEDOWN");
			tree.update ();
			maxFacades [0] = Math.max (maxFacades [0], treeFacades (tree));
		});
		String s = "tree_1M";
		report (s, treeMode, "first_paint_ms", String.format (Locale.ROOT, "%.1f", firstPaint));
		report (s, treeMode, "first_data_ms", String.format (Locale.ROOT, "%.1f", firstData));
		report (s, treeMode, "native_items_after_open", nativeAfterOpen);
		reportFrames (s, treeMode, "expand", expand);
		reportFrames (s, treeMode, "scroll", scroll);
		report (s, treeMode, "native_items_end", nativeTreeCount (tree));
		report (s, treeMode, "facades_materialized", treeFacades (tree));
		report (s, treeMode, "facades_created_by_scroll_storm", maxFacades [0] - facadesBeforeScroll);
		report (s, treeMode, "paint_answers_facade_free", intField (tree, "pagePaintAnswers"));
		report (s, treeMode, "rows_invalidated_precise", intField (tree, "pageRowsInvalidated"));
		report (s, treeMode, "data_requests", work.getAsInt ());
		report (s, treeMode, "heap_delta_mb", String.format (Locale.ROOT, "%.1f", (heapUsedAfterGc () - heapBefore) / 1048576.0));
		long logical = (long) TREE_ROOTS * (TREE_CHILDREN + 1);
		report (s, treeMode, "logical_nodes", logical);
		if (waitLoaded != null) assertTrue (nativeTreeCount (tree) < logical / 100, "expand-all never forced");
	}

	@Test
	public void ctabFolder50HeavyTabsEager () {
		runTabs (true);
	}

	@Test
	public void tabFolder50HeavyTabsEager () {
		runTabs (false);
	}

	void runTabs (boolean custom) {
		int [] controls = new int [1];
		long heapBefore = heapUsedAfterGc ();
		long t0 = System.nanoTime ();
		Composite folder;
		if (custom) {
			CTabFolder c = new CTabFolder (shell, SWT.BORDER);
			for (int i = 0; i < 50; i++) {
				CTabItem item = new CTabItem (c, SWT.NONE);
				item.setText ("Tab " + i);
				item.setControl (heavyContent (c, controls));
			}
			c.setSelection (0);
			folder = c;
		} else {
			TabFolder t = new TabFolder (shell, SWT.NONE);
			for (int i = 0; i < 50; i++) {
				TabItem item = new TabItem (t, SWT.NONE);
				item.setText ("Tab " + i);
				item.setControl (heavyContent (t, controls));
			}
			folder = t;
		}
		shell.open ();
		folder.update ();
		while (display.readAndDispatch ()) {
			// first frame
		}
		double startup = (System.nanoTime () - t0) / 1e6;
		int atStartup = controls [0];
		long heapStartup = heapUsedAfterGc () - heapBefore;
		double [] switches = frames (display, 50, f -> {
			folderSelect (folder, (f * 7) % 50);
			folder.update ();
		});
		String s = custom ? "ctabfolder_50" : "tabfolder_50";
		report (s, mode, "startup_to_first_paint_ms", String.format (Locale.ROOT, "%.1f", startup));
		report (s, mode, "controls_at_startup", atStartup);
		report (s, mode, "heap_at_startup_mb", String.format (Locale.ROOT, "%.1f", heapStartup / 1048576.0));
		reportFrames (s, mode, "switch", switches);
		report (s, mode, "controls_end", controls [0]);
	}
}
