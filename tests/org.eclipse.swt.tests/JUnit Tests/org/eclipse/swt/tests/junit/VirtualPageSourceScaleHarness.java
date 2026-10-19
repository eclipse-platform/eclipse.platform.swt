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

import java.io.*;
import java.lang.reflect.*;
import java.nio.charset.*;
import java.nio.file.*;
import java.util.*;
import java.util.function.*;

import org.eclipse.swt.custom.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

/**
 * Shared, classic-API-only measurement harness for the lazy-data scale proofs; it compiles and
 * runs against the unmodified SWT classes ("before") and the lazy ones ("after").
 */
final class VirtualPageSourceScaleHarness {
	static final double FRAME_BUDGET_MS = 1000.0 / 60.0;
	static final long BLOCK_LATENCY_MS = 5;

	private VirtualPageSourceScaleHarness () {
	}

	static boolean enabled () {
		return Boolean.getBoolean ("swt.lazy.scale");
	}

	/** Per-frame work times in ms; frames are paced at 60 fps, the idle wait is not counted. */
	static double [] frames (Display display, int frames, IntConsumer step) {
		double [] work = new double [frames];
		long frameNanos = (long) (FRAME_BUDGET_MS * 1e6);
		for (int f = 0; f < frames; f++) {
			long start = System.nanoTime ();
			step.accept (f);
			while (display.readAndDispatch ()) {
				// dispatch the frame's events
			}
			work [f] = (System.nanoTime () - start) / 1e6;
			long until = start + frameNanos;
			while (System.nanoTime () < until) {
				if (!display.readAndDispatch ()) sleep (1);
			}
		}
		return work;
	}

	static double percentile (double [] values, double p) {
		double [] sorted = values.clone ();
		Arrays.sort (sorted);
		int index = (int) Math.ceil (p / 100.0 * sorted.length) - 1;
		return sorted [Math.max (0, Math.min (sorted.length - 1, index))];
	}

	static double max (double [] values) {
		double max = 0;
		for (double v : values) max = Math.max (max, v);
		return max;
	}

	static int over (double [] values, double budget) {
		int n = 0;
		for (double v : values) if (v > budget) n++;
		return n;
	}

	static long heapUsedAfterGc () {
		Runtime runtime = Runtime.getRuntime ();
		for (int i = 0; i < 3; i++) {
			System.gc ();
			sleep (20);
		}
		return runtime.totalMemory () - runtime.freeMemory ();
	}

	static void settle (Display display, long millis) {
		long until = System.currentTimeMillis () + millis;
		while (System.currentTimeMillis () < until) {
			if (!display.readAndDispatch ()) sleep (1);
		}
	}

	static void sleep (long millis) {
		try {
			Thread.sleep (millis);
		} catch (InterruptedException e) {
			Thread.currentThread ().interrupt ();
		}
	}

	/** Number of materialized facades of a virtual Table (VirtualItemStorage size) or -1. */
	static int tableFacades (Table table) {
		try {
			Object storage = field (table, "virtualItems");
			if (storage == null) return -1;
			Method size = storage.getClass ().getDeclaredMethod ("size");
			size.setAccessible (true);
			return (int) size.invoke (storage);
		} catch (ReflectiveOperationException e) {
			return -1;
		}
	}

	/** Number of non-null TreeItem facades of a Tree. */
	static int treeFacades (Tree tree) {
		try {
			Object [] items = (Object []) field (tree, "items");
			int n = 0;
			if (items != null) for (Object item : items) if (item != null) n++;
			return n;
		} catch (ReflectiveOperationException e) {
			return -1;
		}
	}

	/** Native TreeView item count (TVM_GETCOUNT), win32 only. */
	static long nativeTreeCount (Tree tree) {
		try {
			Class<?> os = Class.forName ("org.eclipse.swt.internal.win32.OS");
			Method send = os.getMethod ("SendMessage", long.class, int.class, long.class, long.class);
			return (long) send.invoke (null, (long) field (tree, "handle"), os.getField ("TVM_GETCOUNT").getInt (null), 0L, 0L);
		} catch (ReflectiveOperationException e) {
			return -1;
		}
	}

	/** Expands a tree item the way a user click does (TVM_EXPAND, so SWT.Expand is sent). */
	static void nativeExpand (Tree tree, TreeItem item) {
		try {
			Class<?> os = Class.forName ("org.eclipse.swt.internal.win32.OS");
			Method send = os.getMethod ("SendMessage", long.class, int.class, long.class, long.class);
			send.invoke (null, (long) field (tree, "handle"), os.getField ("TVM_EXPAND").getInt (null),
					(long) os.getField ("TVE_EXPAND").getInt (null), (long) field (item, "handle"));
		} catch (ReflectiveOperationException e) {
			throw new IllegalStateException (e);
		}
	}

	/** Scrolls a native control like the scroll bar does (WM_VSCROLL), win32 only. */
	static void nativeScroll (Control control, String sbCode) {
		try {
			Class<?> os = Class.forName ("org.eclipse.swt.internal.win32.OS");
			Method send = os.getMethod ("SendMessage", long.class, int.class, long.class, long.class);
			send.invoke (null, (long) field (control, "handle"), os.getField ("WM_VSCROLL").getInt (null), (long) os.getField (sbCode).getInt (null), 0L);
		} catch (ReflectiveOperationException e) {
			throw new IllegalStateException (e);
		}
	}

	/** An int field of a widget (proof counters), or -1. */
	static int intField (Object target, String name) {
		try {
			return (int) field (target, name);
		} catch (ReflectiveOperationException e) {
			return -1;
		}
	}

	static Object field (Object target, String name) throws ReflectiveOperationException {
		Class<?> type = target.getClass ();
		while (type != null) {
			try {
				Field field = type.getDeclaredField (name);
				field.setAccessible (true);
				return field.get (target);
			} catch (NoSuchFieldException e) {
				type = type.getSuperclass ();
			}
		}
		throw new NoSuchFieldException (name);
	}

	/** Heavy tab content: a composite with labels, texts and buttons. */
	static Control heavyContent (Composite parent, int[] counter) {
		Composite content = new Composite (parent, 0);
		content.setLayout (new GridLayout (6, false));
		for (int i = 0; i < 60; i++) {
			new Label (content, 0).setText ("label " + i);
			new Text (content, org.eclipse.swt.SWT.BORDER).setText ("text " + i);
			new Button (content, org.eclipse.swt.SWT.CHECK).setText ("check " + i);
		}
		counter [0] += 181;
		return content;
	}

	static void folderSelect (Composite folder, int index) {
		if (folder instanceof CTabFolder c) c.setSelection (index);
		else ((TabFolder) folder).setSelection (index);
	}

	/** Appends one metric row and prints it. */
	static void report (String scenario, String mode, String metric, Object value) {
		String row = scenario + "\t" + mode + "\t" + metric + "\t" + value;
		System.out.println ("LAZY-METRIC\t" + row);
		String path = System.getProperty ("swt.lazy.report");
		if (path == null) return;
		try {
			Files.writeString (Path.of (path), row + System.lineSeparator (), StandardCharsets.UTF_8,
					StandardOpenOption.CREATE, StandardOpenOption.APPEND);
		} catch (IOException e) {
			throw new UncheckedIOException (e);
		}
	}

	static void reportFrames (String scenario, String mode, String phase, double [] work) {
		report (scenario, mode, phase + "_frames", work.length);
		report (scenario, mode, phase + "_p50_ms", String.format (Locale.ROOT, "%.3f", percentile (work, 50)));
		report (scenario, mode, phase + "_p95_ms", String.format (Locale.ROOT, "%.3f", percentile (work, 95)));
		report (scenario, mode, phase + "_max_ms", String.format (Locale.ROOT, "%.3f", max (work)));
		report (scenario, mode, phase + "_over_budget", over (work, FRAME_BUDGET_MS));
	}

	/** Deterministic thumb-jump positions. */
	static int [] jumps (int count, int rows, long seed) {
		Random random = new Random (seed);
		int [] tops = new int [count];
		for (int i = 0; i < count; i++) tops [i] = random.nextInt (rows);
		return tops;
	}
}
