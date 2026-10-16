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

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.swt.widgets.Shell;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.Test;

/**
 * Recorded differential of the List viewport (docs/m3/LIST_MEDIATED_VIEWPORT.md): seeded random
 * scripts of public API operations run against the original List (in a second JVM started with
 * <code>-Dswt.widget.mode.List=original</code>, which records every answer) and against the
 * viewport List of this JVM. Every step records answer | vertical and horizontal bar | selection |
 * focus | Selection and DefaultSelection events, and every seed ends with a screen copy of the
 * focused list. Any difference fails with the operation log, which is the reproduction.
 */
public class Test_org_eclipse_swt_widgets_List_ViewportDifferential {
	static final int ROWS = 300, SEEDS = 12, STEPS = 150;
	static final String MODE_PROPERTY = "swt.widget.mode.List";

	/** Runs the scripts and writes the record to <code>args[0]</code> (used for the original List). */
	public static void main (String [] args) throws IOException {
		Display display = new Display ();
		try {
			java.util.List<String> record = new Script (display).run ();
			Files.write (Path.of (args [0]), record, StandardCharsets.UTF_8);
		} finally {
			display.dispose ();
		}
	}

	@Test
	public void seededScriptsAnswerLikeTheOriginalList () throws Exception {
		Assumptions.assumeTrue ("win32".equals (SWT.getPlatform ()), "the List viewport is win32 only");
		Display display = Display.getDefault ();
		Assumptions.assumeTrue (viewportActive (display), "List viewport mediation is not active in this build");
		java.util.List<String> expected = originalRecord ();
		java.util.List<String> actual = new Script (display).run ();
		StringBuilder log = new StringBuilder ();
		for (int i = 0; i < Math.max (expected.size (), actual.size ()); i++) {
			String e = i < expected.size () ? expected.get (i) : "<missing>";
			String a = i < actual.size () ? actual.get (i) : "<missing>";
			if (e.startsWith ("seed ")) log.setLength (0);
			log.append (e).append ('\n');
			if (!e.equals (a)) fail ("line " + i + " differs\n  original: " + e + "\n  viewport: " + a + "\nlog:\n" + log);
		}
	}

	static boolean viewportActive (Display display) {
		Shell shell = new Shell (display);
		try {
			List list = new List (shell, SWT.SINGLE);
			java.lang.reflect.Field field = List.class.getDeclaredField ("viewport");
			field.setAccessible (true);
			return field.get (list) != null;
		} catch (ReflectiveOperationException e) {
			return false;
		} finally {
			shell.dispose ();
		}
	}

	/* the same scripts on the original List, in a JVM started with the original mode */
	private java.util.List<String> originalRecord () throws Exception {
		Path out = Files.createTempFile ("list-original-", ".txt");
		Path err = Files.createTempFile ("list-original-", ".log");
		String classpath = location (getClass ()) + File.pathSeparator + location (Display.class);
		String java = Path.of (System.getProperty ("java.home"), "bin", "java").toString ();
		ProcessBuilder builder = new ProcessBuilder (java, "-Xmx512m", "-D" + MODE_PROPERTY + "=original", "-cp", classpath,
				getClass ().getName (), out.toString ());
		builder.redirectErrorStream (true).redirectOutput (err.toFile ());
		Process process = builder.start ();
		if (!process.waitFor (300, TimeUnit.SECONDS)) {
			process.destroyForcibly ();
			fail ("the original-mode recorder did not finish in 300 s");
		}
		assertEquals (0, process.exitValue (), () -> "original-mode recorder failed:\n" + read (err));
		java.util.List<String> record = Files.readAllLines (out, StandardCharsets.UTF_8);
		Files.deleteIfExists (out);
		Files.deleteIfExists (err);
		return record;
	}

	private static String location (Class<?> type) throws Exception {
		return Path.of (type.getProtectionDomain ().getCodeSource ().getLocation ().toURI ()).toString ();
	}

	private static String read (Path file) {
		try {
			return Files.readString (file, StandardCharsets.UTF_8);
		} catch (IOException e) {
			return e.toString ();
		}
	}

	/** The seeded scripts and the recorder; identical in both JVMs. */
	static final class Script {
		final Display display;
		final StringBuilder events = new StringBuilder ();

		Script (Display display) {
			this.display = display;
		}

		java.util.List<String> run () {
			java.util.List<String> record = new ArrayList<> ();
			for (int seed = 0; seed < SEEDS; seed++) {
				int style = (seed % 2 == 0 ? SWT.SINGLE : SWT.MULTI) | SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL;
				Shell shell = new Shell (display, SWT.NO_TRIM);
				shell.setBounds (60, 60, 330, 420);
				List list = new List (shell, style);
				list.setBounds (5, 5, 300, 380);
				for (int i = 0; i < ROWS; i++) list.add (text (i, i));
				for (int type : new int [] {SWT.Selection, SWT.DefaultSelection}) {
					list.addListener (type, event -> events.append (event.type == SWT.Selection ? "S:" : "D:").append (event.index).append (' '));
				}
				shell.open ();
				settle ();
				Random random = new Random (0x1157L + seed);
				record.add ("seed " + seed + " " + ((style & SWT.SINGLE) != 0 ? "SINGLE" : "MULTI"));
				for (int step = 0; step < STEPS; step++) {
					events.setLength (0);
					String op = op (random, list);
					String answer;
					try {
						answer = apply (op, list);
					} catch (IllegalArgumentException | SWTException e) {
						answer = "error " + e.getMessage ();
					}
					flush ();
					record.add (step + " " + op + " => " + answer + " | " + bars (list) + " | sel " + selection (list) + " | focus "
							+ list.getFocusIndex () + " | events " + events);
				}
				record.add ("pixels " + pixels (shell, list));
				shell.dispose ();
			}
			return record;
		}

		static String text (int seed, int i) {
			return "row " + i + "x".repeat ((i * 7 + seed) % 11);
		}

		/* one random operation; indices may be out of range on purpose */
		static String op (Random random, List list) {
			int count = list.getItemCount ();
			int i = random.nextInt (count + 3) - 1, j = i + random.nextInt (30), k = random.nextInt (count + 3) - 1;
			int kind = random.nextInt (32);
			return switch (kind) {
				case 0 -> "add " + random.nextInt (1000);
				case 1 -> "addAt " + i + " " + random.nextInt (1000);
				case 2, 3 -> "remove " + i;
				case 4 -> "removeRange " + i + " " + j;
				case 5 -> "removeIndices " + i + " " + k + " " + j;
				case 6 -> random.nextInt (8) == 0 ? "removeAll" : "setItem " + i + " " + random.nextInt (1000);
				case 7 -> random.nextInt (6) == 0 ? "setItems " + random.nextInt (400) : "setTopIndex " + k;
				case 8, 9 -> "select " + i;
				case 10 -> "selectRange " + i + " " + j;
				case 11 -> "selectIndices " + i + " " + k;
				case 12 -> "deselect " + i;
				case 13 -> "deselectRange " + i + " " + j;
				case 14 -> "deselectIndices " + i + " " + k;
				case 15 -> random.nextInt (3) == 0 ? "selectAll" : "deselectAll";
				case 16, 17 -> "setSelection " + i;
				case 18 -> "setSelectionRange " + i + " " + j;
				case 19 -> "setSelectionIndices " + k + " " + i;
				case 20 -> "setSelectionStrings " + i + " " + k;
				case 21, 22 -> "setTopIndex " + i;
				case 23 -> "showSelection";
				case 24 -> "read " + i;
				case 25 -> "indexOf " + i + " " + k;
				case 26 -> "selectionIndex";
				case 27 -> "setItemCount " + (count + random.nextInt (5) - 2);
				case 28 -> "clear " + i;
				case 29 -> "addEmpty " + i;
				case 30 -> "addNul " + i;
				default -> "getTop";
			};
		}

		static String apply (String op, List list) {
			String [] a = op.split (" ");
			int x = a.length > 1 ? Integer.parseInt (a [1]) : 0, y = a.length > 2 ? Integer.parseInt (a [2]) : 0;
			int z = a.length > 3 ? Integer.parseInt (a [3]) : 0;
			switch (a [0]) {
				case "add" -> list.add ("new " + x);
				case "addAt" -> list.add ("new " + y + "x".repeat (y % 23), x);
				case "remove" -> list.remove (x);
				case "removeRange" -> list.remove (x, y);
				case "removeIndices" -> list.remove (new int [] {x, y, z, y});
				case "removeAll" -> list.removeAll ();
				case "setItem" -> list.setItem (x, "set " + y);
				case "setItems" -> {
					String [] items = new String [x];
					for (int i = 0; i < x; i++) items [i] = text (x, i);
					list.setItems (items);
				}
				case "select" -> list.select (x);
				case "selectRange" -> list.select (x, y);
				case "selectIndices" -> list.select (new int [] {x, y});
				case "deselect" -> list.deselect (x);
				case "deselectRange" -> list.deselect (x, y);
				case "deselectIndices" -> list.deselect (new int [] {x, y});
				case "selectAll" -> list.selectAll ();
				case "deselectAll" -> list.deselectAll ();
				case "setSelection" -> list.setSelection (x);
				case "setSelectionRange" -> list.setSelection (x, y);
				case "setSelectionIndices" -> list.setSelection (new int [] {x, y});
				case "setSelectionStrings" -> {
					int count = list.getItemCount ();
					String first = x >= 0 && x < count ? list.getItem (x) : "absent";
					String second = y >= 0 && y < count ? list.getItem (y) : "absent";
					list.setSelection (new String [] {first, second});
				}
				case "setTopIndex" -> list.setTopIndex (x);
				case "showSelection" -> list.showSelection ();
				case "read" -> {
					return list.getItem (x) + " height " + list.getItemHeight () + " selected " + list.isSelected (x);
				}
				case "indexOf" -> {
					int count = list.getItemCount ();
					String probe = x >= 0 && x < count ? list.getItem (x) : "absent";
					return "indexOf " + list.indexOf (probe, y) + " " + list.indexOf (probe);
				}
				case "selectionIndex" -> {
					return "index " + list.getSelectionIndex () + " count " + list.getSelectionCount ();
				}
				case "setItemCount" -> list.setItemCount (x);
				case "clear" -> list.clear (x);
				case "addEmpty" -> list.add ("", Math.max (0, Math.min (x, list.getItemCount ())));
				case "addNul" -> list.add ("nul\0tail " + x, Math.max (0, Math.min (x, list.getItemCount ())));
				default -> {}
			}
			return "top " + list.getTopIndex () + " count " + list.getItemCount ();
		}

		static String bars (List list) {
			ScrollBar v = list.getVerticalBar (), h = list.getHorizontalBar ();
			return "v " + v.getSelection () + "/" + v.getMaximum () + " thumb " + v.getThumb () + (v.getVisible () ? " shown" : " hidden")
					+ " h " + h.getSelection () + "/" + h.getMaximum () + " thumb " + h.getThumb () + (h.getVisible () ? " shown" : " hidden");
		}

		static String selection (List list) {
			int [] indices = list.getSelectionIndices ();
			String text = indices.length > 16 ? indices.length + " rows " + indices [0] + ".." + indices [indices.length - 1] + " sum "
					+ Arrays.stream (indices).asLongStream ().sum () : Arrays.toString (indices);
			return list.getSelectionCount () + " " + text;
		}

		/* screen copy of the focused list (a print of a list box is not reliable) */
		String pixels (Shell shell, List list) {
			display.setCursorLocation (shell.getBounds ().x + shell.getBounds ().width + 40, shell.getBounds ().y);
			shell.forceActive ();
			list.setFocus ();
			settle ();
			Rectangle bounds = list.getBounds ();
			Point origin = shell.toDisplay (bounds.x, bounds.y);
			Image image = new Image (display, bounds.width, bounds.height);
			GC gc = new GC (display);
			try {
				gc.copyArea (image, origin.x, origin.y);
			} finally {
				gc.dispose ();
			}
			ImageData data = image.getImageData ();
			image.dispose ();
			return (list.isFocusControl () ? "focused " : "unfocused ") + Integer.toHexString (Arrays.hashCode (data.data));
		}

		/* posted Selection events run after the next dispatched message */
		void flush () {
			boolean [] ran = {false};
			display.asyncExec (() -> ran [0] = true);
			long end = System.currentTimeMillis () + 2000;
			while (!ran [0] && System.currentTimeMillis () < end) {
				if (!display.readAndDispatch ()) display.sleep ();
			}
			while (display.readAndDispatch ()) {
				/* drain */
			}
		}

		void settle () {
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
	}
}
