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
import java.lang.management.*;
import java.nio.charset.*;
import java.nio.file.*;
import java.util.*;

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;

/**
 * Cross-mode benchmark of the row widgets (docs/m3/RENDER_MODES.md, leaf R2). One JVM measures one operation of
 * one widget in the startup mode of that JVM (<code>-Dswt.widget.mode</code>; the official SWT jar on the class
 * path is the <code>official</code> column) and appends one TSV row: wall time (min, median, max over the measured
 * repetitions, after warm-up runs) and the bytes the UI thread allocated per repetition (median). Every measured
 * repetition ends with <code>update ()</code>, so the paint it causes is part of the time.
 * <p>
 * Usage: <code>RenderModeBenchmark &lt;Tree|Table|List&gt; &lt;operation&gt; &lt;out.tsv&gt;</code>; operations:
 * {@link #OPERATIONS}. It uses public API only (plus reflection for the mode name), so it runs unchanged on the
 * official jar.
 * </p>
 */
public final class RenderModeBenchmark {
	/** Logical rows of every widget. */
	static final int ROWS = 1_000_000;
	/** Children of the Tree's first root (expand and collapse operations). */
	static final int CHILDREN = 10_000;
	/** The far row of setSelection and the far scroll distance. */
	static final int FAR = 900_000, JUMP = 10_000;
	static final String [] OPERATIONS = {"create", "setItemCount", "scrollPage", "scroll10k", "paint", "selectAll", "expand10k", "collapse10k", "selectFar"};
	/* the List's items, built once outside the measurement */
	static final String [] LIST_ITEMS = new String [ROWS];
	static {
		Arrays.setAll (LIST_ITEMS, i -> "row " + i);
	}
	static final String HEADER = "widget\tmode\toperation\tstatus\trepetitions\tmin_ms\tmedian_ms\tmax_ms\talloc_bytes_median\tbar_maximum\tswt\tdate";

	/** A widget under measurement: the public calls each operation needs. */
	interface Rows {
		Control control ();
		void setCount (int count);
		void top (int row);
		int pageRows ();
		void selectAll ();
		void deselectAll ();
		void select (int row);
		void expandFirst (boolean expand);
	}

	public static void main (String [] args) throws IOException {
		String widget = args [0], operation = args [1];
		Path out = Paths.get (args [2]);
		Display display = new Display ();
		String row;
		try {
			row = measure (display, widget, operation);
		} catch (RuntimeException | Error e) {
			row = widget + "\t" + mode (widget) + "\t" + operation + "\terror " + e.getClass ().getSimpleName () + ": " + String.valueOf (e.getMessage ()).replace ('\t', ' ') + "\t0\t-\t-\t-\t-\t-\t" + SWT.getVersion () + "\t" + new Date ();
		}
		display.dispose ();
		boolean header = !Files.exists (out);
		try (Writer writer = Files.newBufferedWriter (out, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
			if (header) writer.write (HEADER + "\n");
			writer.write (row + "\n");
		}
	}

	static String measure (Display display, String widget, String operation) {
		Shell shell = new Shell (display);
		shell.setBounds (20, 20, 320, 420);
		shell.open ();
		boolean tree = "Tree".equals (widget);
		if ("expand10k".equals (operation) || "collapse10k".equals (operation)) {
			if (!tree) return skipped (widget, operation, "Tree only");
		}
		long [] times, bytes;
		Rows [] subject = new Rows [1];
		if ("create".equals (operation)) {
			times = new long [3];
			bytes = new long [3];
			for (int i = -1; i < times.length; i++) {
				if (subject [0] != null) subject [0].control ().dispose ();
				settle (display);
				long a = allocated (), t = System.nanoTime ();
				subject [0] = create (shell, widget);
				subject [0].control ().update ();
				if (i >= 0) {
					times [i] = System.nanoTime () - t;
					bytes [i] = allocated () - a;
				}
			}
		} else {
			subject [0] = create (shell, widget);
			settle (display);
			Rows rows = subject [0];
			int [] state = {0};
			Runnable prepare = () -> {}, run;
			int repetitions = 20;
			switch (operation) {
				case "setItemCount" -> run = () -> rows.setCount ((state [0]++ & 1) == 0 ? ROWS - 1000 : ROWS);
				case "scrollPage" -> run = () -> rows.top (state [0] += rows.pageRows ());
				case "scroll10k" -> run = () -> rows.top (state [0] += JUMP);
				case "paint" -> run = () -> rows.control ().redraw ();
				case "selectAll" -> {
					prepare = rows::deselectAll;
					run = rows::selectAll;
					repetitions = 5;
				}
				case "expand10k" -> {
					prepare = () -> rows.expandFirst (false);
					run = () -> rows.expandFirst (true);
				}
				case "collapse10k" -> {
					prepare = () -> rows.expandFirst (true);
					run = () -> rows.expandFirst (false);
				}
				case "selectFar" -> {
					prepare = () -> {
						rows.select (10);
						rows.top (0);
					};
					run = () -> rows.select (FAR);
				}
				default -> throw new IllegalArgumentException ("unknown operation " + operation);
			}
			times = new long [repetitions];
			bytes = new long [repetitions];
			for (int i = -2; i < repetitions; i++) {
				prepare.run ();
				rows.control ().update ();
				settle (display);
				long a = allocated (), t = System.nanoTime ();
				run.run ();
				rows.control ().update ();
				if (i >= 0) {
					times [i] = System.nanoTime () - t;
					bytes [i] = allocated () - a;
				}
			}
		}
		ScrollBar bar = subject [0].control () instanceof Scrollable scrollable ? scrollable.getVerticalBar () : null;
		String maximum = bar == null ? "-" : String.valueOf (bar.getMaximum ());
		shell.dispose ();
		Arrays.sort (times);
		Arrays.sort (bytes);
		return widget + "\t" + mode (widget) + "\t" + operation + "\tok\t" + times.length + "\t" + ms (times [0]) + "\t" + ms (times [times.length / 2]) + "\t"
			+ ms (times [times.length - 1]) + "\t" + bytes [bytes.length / 2] + "\t" + maximum + "\t" + SWT.getVersion () + "\t" + new Date ();
	}

	static String skipped (String widget, String operation, String reason) {
		return widget + "\t" + mode (widget) + "\t" + operation + "\tskipped (" + reason + ")\t0\t-\t-\t-\t-\t-\t" + SWT.getVersion () + "\t" + new Date ();
	}

	static String ms (long nanos) {
		return String.format (Locale.ROOT, "%.3f", nanos / 1e6);
	}

	/* the mode this JVM runs for a widget class: the requested mode, or official on the official jar */
	static String mode (String widget) {
		try {
			Class<?> mode = Class.forName ("org.eclipse.swt.internal.WidgetMode");
			return mode.getMethod ("requested", String.class).invoke (null, widget).toString ().toLowerCase (Locale.ROOT);
		} catch (ClassNotFoundException e) {
			return "official";
		} catch (ReflectiveOperationException e) {
			throw new IllegalStateException (e);
		}
	}

	static long allocated () {
		ThreadMXBean bean = ManagementFactory.getThreadMXBean ();
		if (bean instanceof com.sun.management.ThreadMXBean sun) return sun.getThreadAllocatedBytes (Thread.currentThread ().getId ());
		return 0;
	}

	static void settle (Display display) {
		while (display.readAndDispatch ()) {
			/* drain */
		}
	}

	static Rows create (Shell shell, String widget) {
		return switch (widget) {
			case "Tree" -> tree (shell);
			case "Table" -> table (shell);
			case "List" -> list (shell);
			default -> throw new IllegalArgumentException ("unknown widget " + widget);
		};
	}

	static Rows tree (Shell shell) {
		Tree tree = new Tree (shell, SWT.MULTI | SWT.BORDER | SWT.VIRTUAL);
		tree.setBounds (5, 5, 300, 380);
		tree.addListener (SWT.SetData, event -> {
			TreeItem item = (TreeItem) event.item;
			TreeItem parent = item.getParentItem ();
			item.setText (parent == null ? "root " + event.index : "child " + event.index);
			if (parent == null && event.index == 0) item.setItemCount (CHILDREN);
		});
		tree.setItemCount (ROWS);
		return new Rows () {
			@Override public Control control () { return tree; }
			@Override public void setCount (int count) { tree.setItemCount (count); }
			@Override public void top (int row) { tree.setTopItem (tree.getItem (Math.min (row, tree.getItemCount () - 1))); }
			@Override public int pageRows () { return Math.max (1, tree.getClientArea ().height / tree.getItemHeight ()); }
			@Override public void selectAll () { tree.selectAll (); }
			@Override public void deselectAll () { tree.deselectAll (); }
			@Override public void select (int row) { tree.setSelection (tree.getItem (row)); tree.showSelection (); }
			@Override public void expandFirst (boolean expand) { tree.getItem (0).setExpanded (expand); }
		};
	}

	static Rows table (Shell shell) {
		Table table = new Table (shell, SWT.MULTI | SWT.BORDER | SWT.VIRTUAL);
		table.setBounds (5, 5, 300, 380);
		table.addListener (SWT.SetData, event -> ((TableItem) event.item).setText ("row " + event.index));
		table.setItemCount (ROWS);
		return new Rows () {
			@Override public Control control () { return table; }
			@Override public void setCount (int count) { table.setItemCount (count); }
			@Override public void top (int row) { table.setTopIndex (row); }
			@Override public int pageRows () { return Math.max (1, table.getClientArea ().height / table.getItemHeight ()); }
			@Override public void selectAll () { table.selectAll (); }
			@Override public void deselectAll () { table.deselectAll (); }
			@Override public void select (int row) { table.setSelection (row); table.showSelection (); }
			@Override public void expandFirst (boolean expand) { throw new UnsupportedOperationException (); }
		};
	}

	static Rows list (Shell shell) {
		org.eclipse.swt.widgets.List list = new org.eclipse.swt.widgets.List (shell, SWT.MULTI | SWT.BORDER | SWT.V_SCROLL);
		list.setBounds (5, 5, 300, 380);
		list.setItems (LIST_ITEMS);
		return new Rows () {
			@Override public Control control () { return list; }
			@Override public void setCount (int count) {
				int now = list.getItemCount ();
				if (count < now) list.remove (count, now - 1);
				for (int i = now; i < count; i++) list.add ("row " + i);
			}
			@Override public void top (int row) { list.setTopIndex (row); }
			@Override public int pageRows () { return Math.max (1, list.getClientArea ().height / list.getItemHeight ()); }
			@Override public void selectAll () { list.selectAll (); }
			@Override public void deselectAll () { list.deselectAll (); }
			@Override public void select (int row) { list.setSelection (row); list.showSelection (); }
			@Override public void expandFirst (boolean expand) { throw new UnsupportedOperationException (); }
		};
	}
}
