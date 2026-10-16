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
import java.lang.reflect.*;
import java.nio.charset.*;
import java.nio.file.*;
import java.nio.file.Path;
import java.util.*;
import java.util.List;
import java.util.function.*;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.junit.jupiter.api.*;

/**
 * Recorded differential harness shared by the viewport tests (Tree, Table, List, Composite, Grid, ...).
 * <p>
 * Seeded random scripts of public API operations run against two subjects that must behave the same:
 * a stock widget and a viewport widget in one JVM ({@link #compare}), or one widget in a reference run
 * and in a viewport run ({@link #compareWithRecording}; directory <code>-Dswt.viewport.recording</code>).
 * There are two references, recorded into separate directories:
 * <ul>
 * <li><b>official</b>: the official SWT jar (for example 3.135.0 from Maven) on the class path instead of
 * this SWT; it has no widget modes. This is the stock behaviour.</li>
 * <li><b>original</b>: this SWT with <code>-Dswt.widget.mode=original</code>: the viewport is off, but
 * earlier changes of this fork are still in.</li>
 * </ul>
 * A recording starts with a <code># reference</code> line naming its reference, and a failure names it.
 * Every step records one
 * line: the operation's answer, then each probe (<code>| label value</code>), for example scroll bars,
 * selection, focus, events and geometry. After each seed the pixels of both subjects are compared.
 * Any difference fails with the operation log of the seed, which is the reproduction; differing pixels
 * are saved as PNG files (directory <code>-Dswt.viewport.evidence</code>, default
 * <code>java.io.tmpdir</code>).
 * </p>
 * <p>
 * Example (the Tree differential): subjects are a plain and a virtual Tree; operations are setTopItem,
 * showItem, expand, select, ...; probes are the scroll bar, the selection, the caret item, the events
 * (Expand, Collapse, Selection) and the geometry (top item bounds and two hit tests after
 * <code>update ()</code>); pixels are {@link #render} of each tree.
 * </p>
 *
 * @param <S> the subject (a widget, or a scene holding one)
 */
public final class ViewportDifferential<S> {
	/** One scripted operation applied identically to every subject; answers what the caller observes. */
	public interface Op<S> {
		String apply (S subject);
	}

	/** Draws the next operation of a script and appends its name to the log. */
	public interface Ops<S> {
		Op<S> next (Random random, StringBuilder log);
	}

	/** Reads one aspect of a subject after a step. */
	public interface Probe<S> {
		String read (S subject);
	}

	/** Captures the pixels of a subject. */
	public interface Capture<S> {
		ImageData capture (S subject);
	}

	private final String name;
	private final long seedBase;
	private final Ops<S> ops;
	private final List<String> labels = new ArrayList<> ();
	private final List<Probe<S>> probes = new ArrayList<> ();
	private int seeds = 8, steps = 120;
	private Capture<S> capture;
	private Consumer<S> beforeSeed = subject -> {}, beforeStep = subject -> {}, afterStep = subject -> {};
	private String widget = "";

	private ViewportDifferential (String name, long seedBase, Ops<S> ops) {
		this.name = name;
		this.seedBase = seedBase;
		this.ops = ops;
	}

	/**
	 * A harness for one family of scripts.
	 *
	 * @param name file name prefix of recordings and evidence
	 * @param seedBase the first seed; seed <code>k</code> is <code>seedBase + k</code>
	 * @param ops the script generator
	 */
	public static <S> ViewportDifferential<S> of (String name, long seedBase, Ops<S> ops) {
		return new ViewportDifferential<> (name, seedBase, ops);
	}

	public ViewportDifferential<S> seeds (int count) {
		seeds = count;
		return this;
	}

	public ViewportDifferential<S> steps (int count) {
		steps = count;
		return this;
	}

	/** Adds a probe; its value is appended to every step line as <code>| label value</code>. */
	public ViewportDifferential<S> probe (String label, Probe<S> probe) {
		labels.add (label);
		probes.add (probe);
		return this;
	}

	/** The widget class whose mode decides the reference of this JVM (<code>swt.widget.mode.&lt;widget&gt;</code>). */
	public ViewportDifferential<S> widget (String widgetClass) {
		widget = widgetClass;
		return this;
	}

	/**
	 * The behaviour this JVM runs for a widget class: <code>official</code> when this SWT has no widget
	 * modes (the official jar), otherwise the requested mode (<code>original</code>, <code>viewport</code>, ...).
	 */
	public static String reference (String widgetClass) {
		try {
			Class<?> mode = Class.forName ("org.eclipse.swt.internal.WidgetMode");
			return mode.getMethod ("requested", String.class).invoke (null, widgetClass).toString ().toLowerCase (Locale.ROOT);
		} catch (ClassNotFoundException e) {
			return "official";
		} catch (ReflectiveOperationException e) {
			throw new AssertionError (e);
		}
	}

	/** True when this JVM is a reference run (official jar, or this SWT in the original mode) for a widget class. */
	public static boolean isReference (String widgetClass) {
		String reference = reference (widgetClass);
		return "official".equals (reference) || "original".equals (reference);
	}

	/** Compares the pixels of the subjects after every seed. */
	public ViewportDifferential<S> pixels (Capture<S> capture) {
		this.capture = capture;
		return this;
	}

	/** Brings a subject to the start state of a seed (for example deselect all and scroll to the top). */
	public ViewportDifferential<S> beforeSeed (Consumer<S> action) {
		beforeSeed = action;
		return this;
	}

	/** Runs before each operation on each subject (for example clear an event log). */
	public ViewportDifferential<S> beforeStep (Consumer<S> action) {
		beforeStep = action;
		return this;
	}

	/** Runs after each operation on each subject, before the probes (for example drain the event loop). */
	public ViewportDifferential<S> afterStep (Consumer<S> action) {
		afterStep = action;
		return this;
	}

	/** The recorded line of one step on one subject. */
	String line (S subject, Op<S> op) {
		beforeStep.accept (subject);
		String answer = op.apply (subject);
		afterStep.accept (subject);
		StringBuilder line = new StringBuilder (answer == null ? "" : answer);
		for (int i = 0; i < probes.size (); i++) {
			line.append (" | ").append (labels.get (i)).append (' ').append (probes.get (i).read (subject));
		}
		return line.toString ();
	}

	/** Runs every seed on both subjects in this JVM; the expected subject is the reference. */
	public void compare (S expected, S actual) {
		for (int seed = 0; seed < seeds; seed++) {
			beforeSeed.accept (expected);
			beforeSeed.accept (actual);
			Random random = new Random (seedBase + seed);
			StringBuilder log = new StringBuilder ("seed " + seed + "\n");
			for (int step = 0; step < steps; step++) {
				Op<S> op = ops.next (random, log);
				String want = line (expected, op), got = line (actual, op);
				if (!want.equals (got)) {
					fail ("step " + step + " differs\n  expected: " + want + "\n  actual:   " + got + "\nlog:\n" + log);
				}
			}
			if (capture != null) {
				ImageData want = stable (expected), got = stable (actual);
				if (!Arrays.equals (want.data, got.data)) {
					fail ("pixels after seed " + seed + " differ (" + save (name + "-seed" + seed + "-expected", want) + ", "
						+ save (name + "-seed" + seed + "-actual", got) + ")\nlog:\n" + log);
				}
			}
		}
	}

	/**
	 * Runs every seed on one subject and records the lines (<code>record</code>, a reference run) or compares
	 * them with the recording of a reference run. The comparison skips (assumption) without a recording.
	 */
	public void compareWithRecording (S subject, boolean record, String directory) throws IOException {
		Assumptions.assumeTrue (directory != null, "-Dswt.viewport.recording=<dir> is not set");
		String header = "# reference " + reference (widget) + ", SWT " + SWT.getVersion () + " " + SWT.getPlatform ();
		for (int seed = 0; seed < seeds; seed++) {
			Path file = Paths.get (directory, name + "-seed" + seed + ".txt");
			List<String> expected = null;
			String against = "reference";
			if (!record) {
				Assumptions.assumeTrue (Files.exists (file), "no reference recording " + file + "; record first with the official jar or -Dswt.widget.mode=original");
				expected = new ArrayList<> ();
				for (String recorded : Files.readAllLines (file, StandardCharsets.UTF_8)) {
					if (recorded.startsWith ("# reference ")) {
						against = recorded.substring (2);
					} else {
						expected.add (recorded);
					}
				}
			}
			beforeSeed.accept (subject);
			Random random = new Random (seedBase + seed);
			StringBuilder log = new StringBuilder ("seed " + seed + "\n");
			List<String> lines = new ArrayList<> ();
			for (int step = 0; step < steps; step++) {
				Op<S> op = ops.next (random, log);
				String got = line (subject, op);
				if (expected != null && !expected.get (step).equals (got)) {
					fail ("step " + step + " differs (" + against + ")\n  recorded: " + expected.get (step) + "\n  this run: " + got + "\nlog:\n" + log);
				}
				lines.add (got);
			}
			if (capture != null) {
				ImageData pixels = stable (subject);
				String hash = "pixels " + hash (pixels);
				String evidence = save (name + "-seed" + seed + "-" + reference (widget), pixels);
				if (expected != null && !expected.get (steps).equals (hash)) {
					fail ("pixels after seed " + seed + " differ from the recording (" + against + "; " + evidence + ")\nlog:\n" + log);
				}
				lines.add (hash);
			}
			if (record) {
				lines.add (0, header);
				Files.createDirectories (file.getParent ());
				Files.write (file, lines, StandardCharsets.UTF_8);
			}
		}
	}

	/* captures until two consecutive captures agree (a resize repaints the non-client area asynchronously) */
	private ImageData stable (S subject) {
		ImageData previous = capture.capture (subject);
		for (int attempt = 0; attempt < 5; attempt++) {
			ImageData current = capture.capture (subject);
			if (Arrays.equals (previous.data, current.data)) return current;
			previous = current;
		}
		return previous;
	}

	/* ---- helpers shared by the viewport tests ---- */

	/** Hash of image pixels, as recorded in a step line. */
	public static String hash (ImageData data) {
		return Integer.toHexString (Arrays.hashCode (data.data));
	}

	/** Saves pixels as <code>&lt;evidence dir&gt;/&lt;fileName&gt;.png</code> and answers the path. */
	public static String save (String fileName, ImageData data) {
		String directory = System.getProperty ("swt.viewport.evidence", System.getProperty ("java.io.tmpdir"));
		File file = new File (directory, fileName + ".png");
		file.getParentFile ().mkdirs ();
		ImageLoader loader = new ImageLoader ();
		loader.data = new ImageData [] {data};
		loader.save (file.getPath (), SWT.IMAGE_PNG);
		return file.getPath ();
	}

	/** Dispatches everything pending without waiting. */
	public static void drain (Display display) {
		while (display.readAndDispatch ()) {
			/* drain */
		}
	}

	/** Dispatches for a fixed time; posted events (for example mouse events) run only after a native message. */
	public static void settle (Display display, int millis) {
		display.wake ();
		long end = System.currentTimeMillis () + millis;
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

	/** Moves the pointer to the bottom right corner of the display, away from test shells (hot tracking changes pixels). */
	public static void parkCursor (Display display) {
		Rectangle bounds = display.getBounds ();
		display.setCursorLocation (bounds.x + bounds.width - 1, bounds.y + bounds.height - 1);
	}

	/**
	 * Pixels of a control rendered synchronously by its window procedures. On win32 this is
	 * <code>WM_PRINT</code> with the children; <code>Control.print</code> captures the composited surface
	 * there and was not stable between two captures. Other platforms use <code>Control.print</code>.
	 */
	public static ImageData render (Control control) {
		Point size = control.getSize ();
		Image image = new Image (control.getDisplay (), Math.max (1, size.x), Math.max (1, size.y));
		GC gc = new GC (image);
		try {
			if ("win32".equals (SWT.getPlatform ())) {
				long hdc = field (GC.class, gc, "handle");
				/* WM_PRINT, PRF_NONCLIENT | PRF_CLIENT | PRF_ERASEBKGND | PRF_CHILDREN */
				send (control, 0x0317, hdc, 0x2 | 0x4 | 0x8 | 0x10);
			} else {
				control.print (gc);
			}
		} finally {
			gc.dispose ();
		}
		ImageData data = image.getImageData ();
		image.dispose ();
		return data;
	}

	/** win32 <code>SendMessage</code> to a control's handle (tests that call it assume win32). */
	public static long send (Control control, int message, long wParam, long lParam) {
		try {
			Class<?> os = Class.forName ("org.eclipse.swt.internal.win32.OS");
			Method sendMessage = os.getMethod ("SendMessage", long.class, int.class, long.class, long.class);
			return (Long) sendMessage.invoke (null, field (Control.class, control, "handle"), message, wParam, lParam);
		} catch (ReflectiveOperationException e) {
			throw new AssertionError ("win32 SendMessage is not available", e);
		}
	}

	/** win32 <code>MAKELPARAM</code>. */
	public static long makeLParam (int low, int high) {
		return (low & 0xFFFFL) | ((high & 0xFFFFL) << 16);
	}

	private static long field (Class<?> type, Object object, String name) {
		try {
			Field field = type.getField (name);
			return field.getLong (object);
		} catch (ReflectiveOperationException e) {
			throw new AssertionError (e);
		}
	}

	/**
	 * Appends <code>code[:detail][:item] </code> to <code>out</code> for every event of the given types, for
	 * example <code>S:16777218</code> for a scroll bar Selection with detail ARROW_DOWN or <code>E:root 7</code>
	 * for an Expand.
	 */
	public static void listen (Widget widget, StringBuilder out, String code, int... types) {
		for (int type : types) {
			widget.addListener (type, event -> {
				out.append (code);
				if (event.detail != 0) out.append (':').append (event.detail);
				if (event.item instanceof Item item) out.append (':').append (item.getText ());
				out.append (' ');
			});
		}
	}

	/** Selection, maximum, thumb, page increment and visibility of a scroll bar (or <code>-</code>). */
	public static String bar (ScrollBar bar) {
		return bar == null ? "-" : bar.getSelection () + "/" + bar.getMaximum () + " t" + bar.getThumb () + " p" + bar.getPageIncrement () + " " + bar.getVisible ();
	}
}
