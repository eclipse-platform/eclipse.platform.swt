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
import java.nio.file.Path;
import java.util.*;
import java.util.List;

import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.win32.*;
import org.eclipse.swt.widgets.*;
import org.junit.jupiter.api.*;

/**
 * Recorded differentials of scrollable-composite planes (docs/m3/SCROLL_MEDIATED_VIEWPORT.md). Seeded
 * scripts over setOrigin, setMinSize, setExpandHorizontal/Vertical, content size, child layout, scroll
 * bar messages, the wheel and shell resizes; every step records
 * <code>answer | bars | events</code> and every seed the printed pixels.
 * <ul>
 * <li>{@link #recordedAgainstStock}: content of 30000 x 28000 px is a plane in the viewport mode and
 * still inside the native range, so stock SWT is correct for it. Run once with
 * <code>-Dswt.widget.mode=original</code> (records) and once in the default mode (compares); the
 * recording directory is <code>-Dswt.viewport.recording</code>.</li>
 * <li>{@link #shiftedFarContent}: one script runs on an in-range content and on the same content
 * shifted 2^20 px further down (only reachable through planes); answers must be equal after
 * subtracting the shift, pixels identical.</li>
 * </ul>
 * Any difference fails with the operation log, which is the reproduction.
 */
public class Test_org_eclipse_swt_widgets_ScrolledComposite_ViewportDifferential {
	static final int SEEDS = 8, STEPS = 120, SHIFT = 1 << 20, CHILDREN = 10;
	Display display;

	@BeforeEach
	public void setUp () {
		Assumptions.assumeTrue ("win32".equals (SWT.getPlatform ()), "the native coordinate limit is measured on win32");
		display = Display.getDefault ();
		display.setCursorLocation (1500, 900);
	}

	static boolean planesActive () {
		try {
			java.lang.reflect.Field field = Class.forName ("org.eclipse.swt.widgets.ViewportPlane").getDeclaredField ("ENABLED");
			field.setAccessible (true);
			return field.getBoolean (null);
		} catch (ReflectiveOperationException e) {
			return false;
		}
	}

	/** One ScrolledComposite with a painted content and coloured children, shifted down by <code>shift</code> pixels. */
	final class Scene {
		final int shift;
		final Shell shell;
		final ScrolledComposite sc;
		final Canvas content;
		final Control [] children = new Control [CHILDREN];
		final StringBuilder events = new StringBuilder ();

		Scene (int x, int width, int height, int shift) {
			this.shift = shift;
			shell = new Shell (display, SWT.NO_TRIM | SWT.ON_TOP);
			shell.setBounds (x, 40, 360, 300);
			shell.setLayout (new org.eclipse.swt.layout.FillLayout ());
			sc = new ScrolledComposite (shell, SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
			content = new Canvas (sc, SWT.NONE);
			content.setBackground (display.getSystemColor (SWT.COLOR_WHITE));
			content.addListener (SWT.Paint, e -> {
				events.append ("P:").append (e.x).append (',').append (e.y - shift).append (' ').append (e.width).append ('x').append (e.height).append (' ');
				for (int y = (e.y / 64) * 64; y < e.y + e.height + 64; y += 64) {
					e.gc.drawLine (0, y, e.x + e.width, y);
					e.gc.drawString (Integer.toString (y - shift), 4, y + 2, true);
				}
			});
			int [] colours = {SWT.COLOR_RED, SWT.COLOR_BLUE, SWT.COLOR_GREEN, SWT.COLOR_MAGENTA, SWT.COLOR_CYAN};
			for (int i = 0; i < CHILDREN; i++) {
				Composite child = new Composite (content, SWT.NONE);
				child.setBackground (display.getSystemColor (colours [i % colours.length]));
				child.setBounds (10 + 37 * i, childY (i, height) + shift, 30, 24);
				children [i] = child;
			}
			sc.setContent (content);
			content.setSize (width, height + shift);
			content.addListener (SWT.Move, e -> events.append ("M "));
			content.addListener (SWT.Resize, e -> events.append ("R "));
			sc.addListener (SWT.Resize, e -> events.append ("r "));
			sc.getVerticalBar ().addListener (SWT.Selection, e -> events.append ("V:").append (e.detail).append (' '));
			sc.getHorizontalBar ().addListener (SWT.Selection, e -> events.append ("H:").append (e.detail).append (' '));
			shell.open ();
			flush ();
			events.setLength (0);
		}

		int childY (int i, int height) {
			return i == CHILDREN - 1 ? height - 24 : (int) ((long) height * i / CHILDREN) + 7 * i;
		}

		void flush () {
			while (display.readAndDispatch ()) {
				/* drain */
			}
			content.update ();
			sc.update ();
			while (display.readAndDispatch ()) {
				/* drain */
			}
		}

		/* a timed drain before pixels: a resize repaints the non-client scroll bars asynchronously */
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
			shell.update ();
		}

		String answer () {
			Point origin = sc.getOrigin ();
			Rectangle bounds = content.getBounds ();
			StringBuilder out = new StringBuilder ();
			out.append ("origin ").append (origin.x).append (',').append (origin.y - shift);
			out.append (" content ").append (bounds.x).append (',').append (bounds.y + shift).append (' ').append (bounds.width).append ('x').append (bounds.height - shift);
			out.append (" client ").append (sc.getClientArea ()).append (" cclient ").append (shiftedHeight (content.getClientArea ()));
			for (Control child : new Control [] {children [0], children [CHILDREN / 2], children [CHILDREN - 1]}) {
				Rectangle c = child.getBounds ();
				out.append (" child ").append (c.x).append (',').append (c.y - shift).append (' ').append (c.width).append ('x').append (c.height);
				out.append (" @sc ").append (display.map (child, sc, 0, 0));
			}
			Point inContent = display.map (sc, content, 5, 5);
			out.append (" sc>content ").append (inContent.x).append (',').append (inContent.y - shift);
			return out.toString ();
		}

		String shiftedHeight (Rectangle r) {
			return r.x + "," + r.y + " " + r.width + "x" + (r.height - shift);
		}

		String bars () {
			ScrollBar v = sc.getVerticalBar (), h = sc.getHorizontalBar ();
			return "v " + (v.getSelection () - shift) + "/" + (v.getMaximum () - shift) + " t" + v.getThumb () + " p" + v.getPageIncrement () + " " + v.getVisible ()
				+ " h " + h.getSelection () + "/" + h.getMaximum () + " t" + h.getThumb () + " " + h.getVisible ();
		}

		/*
		 * The printed pixels of the ScrolledComposite, or of its client area only (scroll bar thumbs differ
		 * when shifted). Printing renders what the windows have painted; it is repeated until two prints agree.
		 */
		String pixels (boolean clientOnly) {
			String previous = null;
			for (int attempt = 0; attempt < 6; attempt++) {
				String current = print (clientOnly);
				if (current.equals (previous)) return current;
				previous = current;
			}
			return previous + " (unstable)";
		}

		String print (boolean clientOnly) {
			display.setCursorLocation (1500, 900);
			settle ();
			ImageData data = render (sc);
			lastPixels = data;
			if (!clientOnly) return Integer.toHexString (Arrays.hashCode (data.data));
			Rectangle client = sc.getClientArea ();
			int border = sc.getBorderWidth ();
			int [] row = new int [client.width];
			int hash = 1;
			for (int y = 0; y < client.height; y++) {
				data.getPixels (border, border + y, client.width, row, 0);
				hash = 31 * hash + Arrays.hashCode (row);
			}
			return Integer.toHexString (hash);
		}

		ImageData lastPixels;

		/* with -Dswt.viewport.evidence=<dir>, the last printed pixels are saved as <name>.png */
		void saveEvidence (String name) {
			String directory = System.getProperty ("swt.viewport.evidence");
			if (directory == null || lastPixels == null) return;
			ImageLoader loader = new ImageLoader ();
			loader.data = new ImageData [] {lastPixels};
			new File (directory).mkdirs ();
			loader.save (new File (directory, name + ".png").getPath (), SWT.IMAGE_PNG);
		}

		void dispose () {
			shell.dispose ();
		}
	}

	/*
	 * Pixels of a control rendered synchronously by its window procedures (WM_PRINT with children):
	 * deterministic, unlike Control.print, which captures the composited surface.
	 */
	static ImageData render (Control control) {
		Point size = control.getSize ();
		Image image = new Image (control.getDisplay (), size.x, size.y);
		GC gc = new GC (image);
		try {
			OS.SendMessage (control.handle, OS.WM_PRINT, gc.handle, OS.PRF_NONCLIENT | OS.PRF_CLIENT | OS.PRF_ERASEBKGND | OS.PRF_CHILDREN);
		} finally {
			gc.dispose ();
		}
		ImageData data = image.getImageData ();
		image.dispose ();
		return data;
	}

	interface Op {
		void apply (Scene scene);
	}

	/**
	 * A random operation. With a positive <code>floor</code>, vertical origins stay at or above it, so that
	 * a shifted scene never clamps at a top that the in-range scene clamps at.
	 */
	static Op randomOp (Random random, int height, int floor, StringBuilder log) {
		int kind = random.nextInt (floor > 0 ? 11 : 13);
		int a = random.nextInt (Math.max (1, height)), b = random.nextInt (4000);
		int w = 260 + random.nextInt (200), hh = 220 + random.nextInt (200);
		int child = random.nextInt (CHILDREN), y = floor + random.nextInt (Math.max (1, height - 2 * floor));
		int minHeight = 2000 + random.nextInt (Math.max (1, height - 2000));
		boolean flag = random.nextBoolean ();
		String name = switch (kind) {
			case 0 -> "setOrigin " + b + "," + y;
			case 1 -> "setOrigin y " + y;
			case 2 -> "lineDown";
			case 3 -> "pageDown";
			case 4 -> "end";
			case 5 -> "wheel down";
			case 6 -> "hbar " + (flag ? "page right" : "line right");
			case 7 -> "resize " + w + "x" + hh;
			case 8 -> "move child " + child + " to y " + a;
			case 9 -> floor > 0 ? "content.setSize 3000," + minHeight : "setMinSize 3000," + minHeight + " expand " + flag;
			case 10 -> "lineUp/pageUp " + flag;
			case 11 -> "home";
			default -> "setOrigin " + (b - 2000) + "," + (a - 200);
		};
		log.append (name).append ('\n');
		return scene -> {
			int s = scene.shift;
			switch (kind) {
				case 0 -> scene.sc.setOrigin (b, y + s);
				case 1 -> scene.sc.setOrigin (scene.sc.getOrigin ().x, y + s);
				case 2 -> OS.SendMessage (scene.sc.handle, OS.WM_VSCROLL, OS.SB_LINEDOWN, 0);
				case 3 -> OS.SendMessage (scene.sc.handle, OS.WM_VSCROLL, OS.SB_PAGEDOWN, 0);
				case 4 -> OS.SendMessage (scene.sc.handle, OS.WM_VSCROLL, OS.SB_BOTTOM, 0);
				case 5 -> OS.SendMessage (scene.sc.handle, OS.WM_MOUSEWHEEL, (long) (-120) << 16, 0);
				case 6 -> OS.SendMessage (scene.sc.handle, OS.WM_HSCROLL, flag ? OS.SB_PAGEDOWN : OS.SB_LINEDOWN, 0);
				case 7 -> scene.shell.setSize (w, hh);
				case 8 -> scene.children [child].setLocation (scene.children [child].getLocation ().x, a + s);
				case 9 -> {
					if (floor > 0) {
						/* expand toggles bar visibility, which moves the native bar to the top: not shift-invariant */
						scene.content.setSize (3000, minHeight + s);
					} else {
						scene.sc.setExpandVertical (flag);
						scene.sc.setExpandHorizontal (flag);
						scene.sc.setMinSize (3000, minHeight + s);
					}
				}
				case 10 -> {
					if (scene.sc.getOrigin ().y - s >= floor + 400) OS.SendMessage (scene.sc.handle, OS.WM_VSCROLL, flag ? OS.SB_PAGEUP : OS.SB_LINEUP, 0);
				}
				case 11 -> OS.SendMessage (scene.sc.handle, OS.WM_VSCROLL, OS.SB_TOP, 0);
				default -> scene.sc.setOrigin (b - 2000, a - 200 + s);
			}
		};
	}

	static String step (Scene scene, Op op) {
		scene.events.setLength (0);
		op.apply (scene);
		scene.flush ();
		return scene.answer () + " | " + scene.bars () + " | events " + scene.events;
	}

	@Test
	public void recordedAgainstStock () throws IOException {
		String directory = System.getProperty ("swt.viewport.recording");
		Assumptions.assumeTrue (directory != null, "-Dswt.viewport.recording=<dir> is not set");
		boolean recording = !planesActive ();
		int height = 30000;
		for (int seed = 0; seed < SEEDS; seed++) {
			Path file = Paths.get (directory, "ScrolledComposite-plane-seed" + seed + ".txt");
			List<String> expected = null;
			if (!recording) {
				Assumptions.assumeTrue (Files.exists (file), "no stock recording; run first with -Dswt.widget.mode=original");
				expected = Files.readAllLines (file, StandardCharsets.UTF_8);
			}
			Scene scene = new Scene (40, 28000, height, 0);
			List<String> lines = new ArrayList<> ();
			StringBuilder log = new StringBuilder ("seed " + seed + "\n");
			try {
				Random random = new Random (0x5C0DL + seed);
				for (int i = 0; i < STEPS; i++) {
					Op op = randomOp (random, height, 0, log);
					String line = step (scene, op);
					if (expected != null && !expected.get (i).equals (line)) {
						fail ("step " + i + " differs\n  stock:    " + expected.get (i) + "\n  viewport: " + line + "\nlog:\n" + log);
					}
					lines.add (line);
				}
				String pixels = "pixels " + scene.pixels (false);
				scene.saveEvidence ("ScrolledComposite-plane-seed" + seed + (recording ? "-stock" : "-viewport"));
				if (expected != null) assertEquals (expected.get (STEPS), pixels, "printed pixels after seed " + seed + "\nlog:\n" + log);
				lines.add (pixels);
			} finally {
				scene.dispose ();
			}
			if (recording) {
				Files.createDirectories (file.getParent ());
				Files.write (file, lines, StandardCharsets.UTF_8);
			}
		}
	}

	@Test
	public void shiftedFarContent () {
		Assumptions.assumeTrue (planesActive (), "planes are not active (-Dswt.widget.mode.Composite=original)");
		int height = 20000, floor = 1000;
		Scene near = new Scene (40, 3000, height, 0);
		Scene far = new Scene (420, 3000, height, SHIFT);
		try {
			for (int seed = 0; seed < SEEDS; seed++) {
				StringBuilder log = new StringBuilder ("seed " + seed + "\n");
				for (Scene scene : new Scene [] {near, far}) {
					scene.sc.setOrigin (0, floor + scene.shift);
					scene.flush ();
				}
				Random random = new Random (0xFA2L + seed);
				for (int i = 0; i < STEPS; i++) {
					Op op = randomOp (random, height, floor, log);
					String expected = step (near, op);
					String actual = step (far, op);
					if (!expected.equals (actual)) {
						fail ("step " + i + " differs\n  near: " + expected + "\n  far:  " + actual + "\nlog:\n" + log);
					}
				}
				String nearPixels = near.pixels (true), farPixels = far.pixels (true);
				near.saveEvidence ("ScrolledComposite-shift-seed" + seed + "-near");
				far.saveEvidence ("ScrolledComposite-shift-seed" + seed + "-far");
				assertEquals (nearPixels, farPixels, "printed pixels after seed " + seed + "\nlog:\n" + log);
			}
		} finally {
			near.dispose ();
			far.dispose ();
		}
	}
}
