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

import java.io.*;
import java.nio.charset.*;
import java.nio.file.*;
import java.util.*;
import java.util.List;
import java.nio.file.Path;
import java.util.concurrent.*;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.win32.*;
import org.eclipse.swt.widgets.*;
import org.junit.jupiter.api.*;

/**
 * Recorded differential of the Table viewport (docs/m3/TABLE_MEDIATED_VIEWPORT.md): seeded
 * random scripts of public API operations run against a stock Table and a viewport Table that
 * start with the same rows. The stock run is recorded by a child JVM started with
 * <code>-Dswt.widget.mode.Table=original</code>; this JVM replays the same seeds on the viewport
 * Table. Every step records answer | bar | selection | focus | events, as the Tree differential
 * does, and every seed ends with the printed pixels. Any difference fails with the operation
 * log of the seed, which is the reproduction.
 */
public class Test_org_eclipse_swt_widgets_Table_ViewportDifferential {
	static final int ROWS = 300, SEEDS = 12, STEPS = 150;
	/* plain multi with columns, plain single with check boxes, virtual multi */
	static final int [] VARIANTS = {
		SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION,
		SWT.SINGLE | SWT.BORDER | SWT.CHECK,
		SWT.MULTI | SWT.BORDER | SWT.VIRTUAL,
	};
	static final long CHILD_BUDGET_SECONDS = 300;

	/**
	 * Child JVM entry point: records every variant and seed on the Table of this JVM's mode
	 * (the test starts it with the stock Table) and writes one line per step.
	 */
	public static void main (String [] args) throws IOException {
		Display display = new Display ();
		try {
			List<String> lines = new ArrayList<> ();
			Recorder recorder = new Recorder (display, lines::add);
			recorder.images = Path.of (args [1]);
			for (int variant = 0; variant < VARIANTS.length; variant++) {
				for (int seed = 0; seed < SEEDS; seed++) recorder.run (variant, seed);
			}
			Files.write (Path.of (args [0]), lines, StandardCharsets.UTF_8);
		} finally {
			display.dispose ();
		}
	}

	/** A plain Table realized as owner data is the non-blocking sign that the viewport is active. */
	static boolean viewportActive (Display display) {
		Shell shell = new Shell (display);
		try {
			Table table = new Table (shell, SWT.MULTI);
			return "win32".equals (SWT.getPlatform ()) && (OS.GetWindowLong (table.handle, OS.GWL_STYLE) & OS.LVS_OWNERDATA) != 0;
		} finally {
			shell.dispose ();
		}
	}

	@Test
	public void seededScriptsAnswerLikeAStockTable () throws Exception {
		Display display = Display.getDefault ();
		/* -Dorg.eclipse.swt.tests.tableDifferential.selfCheck=true runs stock against stock: the harness must be deterministic */
		Assumptions.assumeTrue (viewportActive (display) || Boolean.getBoolean ("org.eclipse.swt.tests.tableDifferential.selfCheck"),
				"Table viewport mediation is not active in this build");
		Path images = Files.createTempDirectory ("table-differential");
		List<String> expected = recordStock (images);
		int [] position = {0};
		StringBuilder log = new StringBuilder ();
		Recorder recorder = new Recorder (display, line -> {
			if (line.startsWith ("variant ")) log.setLength (0);
			log.append (line).append ('\n');
			int index = position [0]++;
			String stock = index < expected.size () ? expected.get (index) : "<no stock line>";
			if (!stock.equals (line)) {
				fail ("line " + index + " differs\n  stock:    " + stock + "\n  viewport: " + line + "\nimages: " + images + "\nlog:\n" + log);
			}
		});
		recorder.images = images;
		recorder.suffix = "-viewport";
		for (int variant = 0; variant < VARIANTS.length; variant++) {
			for (int seed = 0; seed < SEEDS; seed++) recorder.run (variant, seed);
		}
		assertEquals (expected.size (), position [0], "the stock recording has more lines");
		/* the printed images are kept only when a difference needs them */
		try (var files = Files.list (images)) {
			for (Path file : files.toList ()) Files.delete (file);
		}
		Files.delete (images);
	}

	/* Runs main () of this class in a child JVM with the stock Table and returns its lines. */
	private static List<String> recordStock (Path images) throws Exception {
		Path out = Files.createTempFile ("table-differential", ".txt"), console = Files.createTempFile ("table-differential", ".log");
		try {
			String java = ProcessHandle.current ().info ().command ().orElse ("java");
			String classPath = String.join (File.pathSeparator, location (Test_org_eclipse_swt_widgets_Table_ViewportDifferential.class),
					location (Display.class), System.getProperty ("java.class.path"));
			Process process = new ProcessBuilder (java, "-Xmx512m", "-Dswt.widget.mode.Table=original", "-cp", classPath,
					Test_org_eclipse_swt_widgets_Table_ViewportDifferential.class.getName (), out.toString (), images.toString ())
					.redirectErrorStream (true).redirectOutput (console.toFile ()).start ();
			if (!process.waitFor (CHILD_BUDGET_SECONDS, TimeUnit.SECONDS)) {
				process.destroyForcibly ().waitFor (10, TimeUnit.SECONDS);
				fail ("the stock recording did not finish in " + CHILD_BUDGET_SECONDS + " s" + System.lineSeparator () + Files.readString (console));
			}
			assertEquals (0, process.exitValue (), "stock recording exit code" + System.lineSeparator () + Files.readString (console));
			return Files.readAllLines (out, StandardCharsets.UTF_8);
		} finally {
			Files.deleteIfExists (out);
			Files.deleteIfExists (console);
		}
	}

	private static String location (Class<?> type) throws Exception {
		return Path.of (type.getProtectionDomain ().getCodeSource ().getLocation ().toURI ()).toString ();
	}

	/** Runs the seeded scripts on the Table of this JVM's mode and hands every recorded line to a sink. */
	static final class Recorder {
		final Display display;
		final java.util.function.Consumer<String> sink;
		final StringBuilder events = new StringBuilder ();
		Path images;
		String suffix = "-stock";
		int variant, seed;
		Shell shell;
		Table table;

		Recorder (Display display, java.util.function.Consumer<String> sink) {
			this.display = display;
			this.sink = sink;
		}

		void run (int variant, int seed) {
			this.variant = variant;
			this.seed = seed;
			int style = VARIANTS [variant];
			shell = new Shell (display);
			/*
			 * Off the virtual screen: whether the native control paints during an operation (and so
			 * when SWT.SetData is sent) depends on how much of the window the desktop shows, which
			 * differs between two processes. Off screen no paint happens during the steps in either
			 * process; the pixels are drawn on request (WM_PRINT).
			 */
			Rectangle screen = display.getBounds ();
			shell.setBounds (screen.x - 400, screen.y + 20, 320, 420);
			try {
				table = create (style);
				shell.open ();
				/* the table receives focus once: the caret it gets on focus is part of the behaviour */
				table.setFocus ();
				settle ();
				events.setLength (0);
				sink.accept ("variant " + variant + " seed " + seed + " | " + state ());
				Random random = new Random (0x7AB1E5EEDL + 31L * variant + seed);
				for (int step = 0; step < STEPS; step++) {
					String [] name = new String [1];
					events.setLength (0);
					String answer = apply (random, step, name);
					sink.accept (step + " " + name [0] + " -> " + answer + " | " + state ());
				}
				settle ();
				sink.accept ("pixels " + pixels ());
			} finally {
				shell.dispose ();
			}
		}

		private Table create (int style) {
			Table table = new Table (shell, style);
			table.setBounds (5, 5, 300, 380);
			if ((style & SWT.FULL_SELECTION) != 0) {
				table.setHeaderVisible (true);
				table.setLinesVisible (true);
				for (int c = 0; c < 2; c++) {
					TableColumn column = new TableColumn (table, SWT.NONE);
					column.setText ("column " + c);
					column.setWidth (120);
				}
			}
			for (int type : new int [] {SWT.Selection, SWT.DefaultSelection, SWT.SetData}) {
				table.addListener (type, event -> {
					switch (event.type) {
						case SWT.Selection -> events.append ("S:").append (text (event.item)).append (event.detail == SWT.CHECK ? "/check" : "");
						case SWT.DefaultSelection -> events.append ("D:").append (text (event.item));
						default -> {
							TableItem item = (TableItem) event.item;
							item.setText (0, "data " + event.index);
							events.append ("SD:").append (event.index);
						}
					}
					events.append (' ');
				});
			}
			if ((style & SWT.VIRTUAL) != 0) {
				table.setItemCount (ROWS);
			} else {
				for (int i = 0; i < ROWS; i++) {
					TableItem item = new TableItem (table, SWT.NONE);
					item.setText (new String [] {"row " + i, "cell " + i});
				}
			}
			return table;
		}

		private static String text (Widget item) {
			return item instanceof TableItem tableItem ? tableItem.getText () : "-";
		}

		private String state () {
			ScrollBar bar = table.getVerticalBar ();
			return "count " + table.getItemCount () + " top " + table.getTopIndex () + " | bar " + bar.getSelection () + "/"
					+ bar.getMaximum () + " thumb " + bar.getThumb () + " | sel " + table.getSelectionCount ()
					+ Arrays.toString (sorted (table.getSelectionIndices ())) + " index " + table.getSelectionIndex () + " | focus "
					+ focusIndex (table) + " | events " + events;
		}

		private static int [] sorted (int [] indices) {
			Arrays.sort (indices);
			return indices.length > 12 ? Arrays.copyOf (indices, 12) : indices;
		}

		/* the caret row (package-private Table.getFocusIndex), which keyboard and focus behaviour start from */
		static int focusIndex (Table table) {
			try {
				java.lang.reflect.Method method = Table.class.getDeclaredMethod ("getFocusIndex");
				method.setAccessible (true);
				return (Integer) method.invoke (table);
			} catch (ReflectiveOperationException e) {
				throw new IllegalStateException (e);
			}
		}

		/* one public API operation chosen by the seeded random; the operation's answer is returned */
		private String apply (Random random, int step, String [] name) {
			int count = table.getItemCount ();
			int k = count == 0 ? 0 : random.nextInt (count), span = random.nextInt (6);
			int kind = random.nextInt (25);
			boolean check = (table.getStyle () & SWT.CHECK) != 0;
			switch (kind) {
				case 0: {
					name [0] = "insert " + k;
					new TableItem (table, SWT.NONE, k).setText ("new " + step);
					return "index " + table.indexOf (table.getItem (k));
				}
				case 1: {
					name [0] = "append";
					TableItem item = new TableItem (table, SWT.NONE);
					item.setText ("appended " + step);
					return "index " + table.indexOf (item);
				}
				case 2: {
					name [0] = "remove " + k;
					if (count > 0) table.remove (k);
					return "ok";
				}
				case 3: {
					int end = Math.min (count - 1, k + span);
					name [0] = "remove " + k + ".." + end;
					if (count > 0) table.remove (k, end);
					return "ok";
				}
				case 4: {
					int [] indices = {k, Math.max (0, k - span), Math.min (count - 1, k + 2 * span)};
					name [0] = "remove " + Arrays.toString (indices);
					if (count > 0) table.remove (indices);
					return "ok";
				}
				case 5: {
					int target = Math.max (0, count + (random.nextBoolean () ? 1 : -1) * random.nextInt (40));
					name [0] = "setItemCount " + target;
					table.setItemCount (target);
					return "ok";
				}
				case 6:
					name [0] = "setSelection " + k;
					table.setSelection (k);
					return "ok";
				case 7:
					name [0] = "select " + k;
					table.select (k);
					return "ok";
				case 8:
					name [0] = "select " + k + ".." + (k + span);
					table.select (k, k + span);
					return "ok";
				case 9:
					name [0] = "deselect " + k;
					table.deselect (k);
					return "ok";
				case 10:
					name [0] = "deselectAll";
					table.deselectAll ();
					return "ok";
				case 11:
					name [0] = "selectAll";
					table.selectAll ();
					return "ok";
				case 12: {
					int [] indices = {k, Math.max (0, k - span)};
					name [0] = "setSelection " + Arrays.toString (indices);
					table.setSelection (indices);
					return "ok";
				}
				case 13: {
					name [0] = "setSelection item " + k;
					if (count > 0) table.setSelection (table.getItem (k));
					return "ok";
				}
				case 14:
					name [0] = "setTopIndex " + k;
					table.setTopIndex (k);
					return "top " + table.getTopIndex ();
				case 15:
					name [0] = "showItem " + k;
					if (count > 0) table.showItem (table.getItem (k));
					return "top " + table.getTopIndex ();
				case 16:
					name [0] = "showSelection";
					table.showSelection ();
					return "top " + table.getTopIndex ();
				case 17: {
					name [0] = "read " + k;
					if (count == 0) return "empty";
					TableItem item = table.getItem (k);
					return item.getText () + " / " + item.getText (1) + " index " + table.indexOf (item) + " selected " + table.isSelected (k)
							+ " checked " + item.getChecked ();
				}
				case 18:
					name [0] = "setText " + k;
					if (count > 0) table.getItem (k).setText ("text " + step);
					return "ok";
				case 19:
					name [0] = "clear " + k;
					if (count > 0) table.clear (k);
					return "ok";
				case 20: {
					int [] indices = {k, Math.min (count - 1, k + span)};
					name [0] = "clear " + Arrays.toString (indices);
					if (count > 0) table.clear (indices);
					return "ok";
				}
				case 21:
					name [0] = random.nextInt (4) == 0 ? "clearAll" : "getTopIndex";
					if (name [0].equals ("clearAll")) table.clearAll ();
					return "top " + table.getTopIndex ();
				case 22:
					name [0] = "setChecked " + k;
					if (count > 0 && check) table.getItem (k).setChecked (!table.getItem (k).getChecked ());
					return "checked " + (count > 0 && table.getItem (k).getChecked ());
				case 23: {
					name [0] = "dispose item " + k;
					if (count > 0) table.getItem (k).dispose ();
					return "ok";
				}
				default: {
					boolean all = random.nextInt (5) == 0;
					name [0] = all ? "removeAll + refill" : "getItems";
					if (all) {
						table.removeAll ();
						if ((table.getStyle () & SWT.VIRTUAL) != 0) {
							table.setItemCount (ROWS / 3);
						} else {
							for (int i = 0; i < ROWS / 3; i++) new TableItem (table, SWT.NONE).setText ("refill " + i);
						}
					}
					TableItem [] items = table.getItems ();
					return "items " + items.length + (items.length > 0 ? " last " + items [items.length - 1].getText () : "");
				}
			}
		}

		private void settle () {
			long end = System.currentTimeMillis () + 150;
			while (System.currentTimeMillis () < end) {
				if (!display.readAndDispatch ()) {
					try {
						Thread.sleep (2);
					} catch (InterruptedException e) {
						Thread.currentThread ().interrupt ();
						return;
					}
				}
			}
		}

		/*
		 * The client area and the children (rows and header) drawn by the control itself into an
		 * image (WM_PRINT without PRF_NONCLIENT). Control.print copies the composed screen content
		 * (PW_RENDERFULLCONTENT), which can be stale in a process that is not in the foreground, and
		 * PrintWindow leaves the themed scroll bar unpainted in some runs; the scroll bar is
		 * recorded in every step instead.
		 */
		static ImageData print (Display display, Control control) {
			RECT rect = new RECT ();
			OS.GetWindowRect (control.handle, rect);
			int zoom = display.getPrimaryMonitor ().getZoom ();
			Image image = new Image (display, (rect.right - rect.left) * 100 / zoom, (rect.bottom - rect.top) * 100 / zoom);
			GC gc = new GC (image);
			try {
				OS.SendMessage (control.handle, OS.WM_PRINT, gc.handle, OS.PRF_CLIENT | OS.PRF_ERASEBKGND | OS.PRF_CHILDREN);
			} finally {
				gc.dispose ();
			}
			ImageData data = image.getImageData ();
			image.dispose ();
			return data;
		}

		/* the colour of every pixel: the raw pixel data (layout, unused bytes) may differ between processes */
		static int rgbHash (ImageData data) {
			int [] line = new int [data.width];
			int hash = 1;
			for (int y = 0; y < data.height; y++) {
				data.getPixels (0, y, data.width, line, 0);
				for (int pixel : line) {
					RGB rgb = data.palette.getRGB (pixel);
					hash = 31 * hash + ((rgb.red << 16) | (rgb.green << 8) | rgb.blue);
				}
			}
			return hash;
		}

		/*
		 * Print the table without keyboard focus (a one-pixel button outside it takes the focus),
		 * with focus rectangles hidden and the pointer away from it (hot tracking). Keyboard focus,
		 * the focus rectangle and the selection colour that follows them depend on which process the
		 * desktop has in the foreground, so both processes print the state they can both guarantee;
		 * the focus row itself is part of every recorded step, and the focused pixels are compared
		 * within one process by Test_org_eclipse_swt_widgets_Table_Viewport.
		 */
		private String pixels () {
			display.setCursorLocation (shell.getBounds ().x + shell.getBounds ().width + 40, shell.getBounds ().y);
			Button focusSink = new Button (shell, SWT.PUSH);
			focusSink.setBounds (0, 0, 1, 1);
			focusSink.setFocus ();
			long cues = OS.MAKEWPARAM (OS.UIS_SET, OS.UISF_HIDEFOCUS | OS.UISF_HIDEACCEL);
			OS.SendMessage (shell.handle, OS.WM_CHANGEUISTATE, cues, 0);
			OS.SendMessage (table.handle, OS.WM_UPDATEUISTATE, cues, 0);
			table.redraw ();
			settle ();
			ImageData data = print (display, table);
			if (images != null) {
				ImageLoader loader = new ImageLoader ();
				loader.data = new ImageData [] {data};
				loader.save (images.resolve ("variant" + variant + "-seed" + seed + suffix + ".png").toString (), SWT.IMAGE_PNG);
			}
			focusSink.dispose ();
			return Integer.toHexString (rgbHash (data));
		}
	}
}
