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

import java.io.*;

import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.junit.jupiter.api.*;

/**
 * Recorded differentials of scrollable-composite planes (docs/m3/SCROLL_MEDIATED_VIEWPORT.md), on the
 * shared {@link ViewportDifferential} harness. Scripts over setOrigin, setMinSize,
 * setExpandHorizontal/Vertical, content size, child layout, scroll bar messages, the wheel and shell
 * resizes; every step records <code>answer | bars | events</code>, every seed the pixels.
 * <ul>
 * <li>{@link #recordedAgainstStock}: content of 30000 x 28000 px is a plane in the viewport mode and still
 * inside the native range, so stock SWT is correct for it. Record once with the official SWT jar on the class path or with
 * <code>-Dswt.widget.mode=original</code>, then compare in the default mode.</li>
 * <li>{@link #shiftedFarContent}: one script runs on an in-range content and on the same content shifted
 * 2^20 px further down (only reachable through planes); answers must be equal after subtracting the
 * shift, client-area pixels identical.</li>
 * </ul>
 */
public class Test_org_eclipse_swt_widgets_ScrolledComposite_ViewportDifferential {
	static final int SEEDS = 8, STEPS = 120, SHIFT = 1 << 20, CHILDREN = 10;
	/* win32 messages and scroll codes */
	static final int WM_HSCROLL = 0x114, WM_VSCROLL = 0x115, WM_MOUSEWHEEL = 0x20A;
	static final int SB_LINEUP = 0, SB_LINEDOWN = 1, SB_PAGEUP = 2, SB_PAGEDOWN = 3, SB_TOP = 6, SB_BOTTOM = 7;
	Display display;

	@BeforeEach
	public void setUp () {
		Assumptions.assumeTrue ("win32".equals (SWT.getPlatform ()), "the native coordinate limit is measured on win32");
		display = Display.getDefault ();
		ViewportDifferential.parkCursor (display);
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
			shell.setLayout (new FillLayout ());
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
			ViewportDifferential.listen (content, events, "M", SWT.Move);
			ViewportDifferential.listen (content, events, "R", SWT.Resize);
			ViewportDifferential.listen (sc, events, "r", SWT.Resize);
			ViewportDifferential.listen (sc.getVerticalBar (), events, "V", SWT.Selection);
			ViewportDifferential.listen (sc.getHorizontalBar (), events, "H", SWT.Selection);
			shell.open ();
			flush ();
			events.setLength (0);
		}

		int childY (int i, int height) {
			return i == CHILDREN - 1 ? height - 24 : (int) ((long) height * i / CHILDREN) + 7 * i;
		}

		void flush () {
			ViewportDifferential.drain (display);
			content.update ();
			sc.update ();
			ViewportDifferential.drain (display);
		}

		String answer () {
			Point origin = sc.getOrigin ();
			Rectangle bounds = content.getBounds ();
			StringBuilder out = new StringBuilder ();
			out.append ("origin ").append (origin.x).append (',').append (origin.y - shift);
			out.append (" content ").append (bounds.x).append (',').append (bounds.y + shift).append (' ').append (bounds.width).append ('x').append (bounds.height - shift);
			Rectangle client = content.getClientArea ();
			out.append (" client ").append (sc.getClientArea ()).append (" cclient ").append (client.x).append (',').append (client.y).append (' ').append (client.width).append ('x').append (client.height - shift);
			for (Control child : new Control [] {children [0], children [CHILDREN / 2], children [CHILDREN - 1]}) {
				Rectangle c = child.getBounds ();
				out.append (" child ").append (c.x).append (',').append (c.y - shift).append (' ').append (c.width).append ('x').append (c.height);
				out.append (" @sc ").append (display.map (child, sc, 0, 0));
			}
			Point inContent = display.map (sc, content, 5, 5);
			out.append (" sc>content ").append (inContent.x).append (',').append (inContent.y - shift);
			return out.toString ();
		}

		String bars () {
			ScrollBar v = sc.getVerticalBar (), h = sc.getHorizontalBar ();
			return "v " + (v.getSelection () - shift) + "/" + (v.getMaximum () - shift) + " t" + v.getThumb () + " p" + v.getPageIncrement () + " " + v.getVisible ()
				+ " h " + ViewportDifferential.bar (h);
		}

		/* the rendered ScrolledComposite, or its client area only (scroll bar thumbs differ when shifted) */
		ImageData pixels (boolean clientOnly) {
			ViewportDifferential.parkCursor (display);
			ViewportDifferential.settle (display, 150);
			shell.update ();
			ImageData data = ViewportDifferential.render (sc);
			if (!clientOnly) return data;
			Rectangle client = sc.getClientArea ();
			int border = sc.getBorderWidth ();
			ImageData crop = new ImageData (client.width, client.height, data.depth, data.palette);
			int [] row = new int [client.width];
			for (int y = 0; y < client.height; y++) {
				data.getPixels (border, border + y, client.width, row, 0);
				crop.setPixels (0, y, client.width, row, 0);
			}
			return crop;
		}

		void dispose () {
			shell.dispose ();
		}
	}

	static void send (Scene scene, int message, long wParam) {
		ViewportDifferential.send (scene.sc, message, wParam, 0);
	}

	/**
	 * The script. With a positive <code>floor</code>, vertical origins stay at or above it, so that a shifted
	 * scene never clamps at a top that the in-range scene clamps at.
	 */
	static ViewportDifferential.Ops<Scene> ops (int height, int floor) {
		return (random, log) -> {
			int kind = random.nextInt (floor > 0 ? 11 : 13);
			int a = random.nextInt (Math.max (1, height)), b = random.nextInt (4000);
			int w = 260 + random.nextInt (200), hh = 220 + random.nextInt (200);
			int child = random.nextInt (CHILDREN), y = floor + random.nextInt (Math.max (1, height - 2 * floor));
			int minHeight = 2000 + random.nextInt (Math.max (1, height - 2000));
			boolean flag = random.nextBoolean ();
			log.append (switch (kind) {
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
			}).append ('\n');
			return scene -> {
				int s = scene.shift;
				switch (kind) {
					case 0 -> scene.sc.setOrigin (b, y + s);
					case 1 -> scene.sc.setOrigin (scene.sc.getOrigin ().x, y + s);
					case 2 -> send (scene, WM_VSCROLL, SB_LINEDOWN);
					case 3 -> send (scene, WM_VSCROLL, SB_PAGEDOWN);
					case 4 -> send (scene, WM_VSCROLL, SB_BOTTOM);
					case 5 -> send (scene, WM_MOUSEWHEEL, (long) (-120) << 16);
					case 6 -> send (scene, WM_HSCROLL, flag ? SB_PAGEDOWN : SB_LINEDOWN);
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
						if (scene.sc.getOrigin ().y - s >= floor + 400) send (scene, WM_VSCROLL, flag ? SB_PAGEUP : SB_LINEUP);
					}
					case 11 -> send (scene, WM_VSCROLL, SB_TOP);
					default -> scene.sc.setOrigin (b - 2000, a - 200 + s);
				}
				return "";
			};
		};
	}

	static ViewportDifferential<Scene> harness (String name, long seedBase, int height, int floor, boolean clientPixels) {
		return ViewportDifferential.of (name, seedBase, ops (height, floor)).widget ("Composite").seeds (SEEDS).steps (STEPS)
			.beforeStep (scene -> scene.events.setLength (0))
			.afterStep (Scene::flush)
			.probe ("at", Scene::answer)
			.probe ("bars", Scene::bars)
			.probe ("events", scene -> scene.events.toString ())
			.pixels (scene -> scene.pixels (clientPixels));
	}

	@Test
	public void recordedAgainstStock () throws IOException {
		Scene scene = new Scene (40, 28000, 30000, 0);
		try {
			harness ("ScrolledComposite-plane", 0x5C0DL, 30000, 0, false)
				.compareWithRecording (scene, ViewportDifferential.isReference ("Composite"), System.getProperty ("swt.viewport.recording"));
		} finally {
			scene.dispose ();
		}
	}

	@Test
	public void shiftedFarContent () {
		Assumptions.assumeTrue (planesActive (), "planes are not active (-Dswt.widget.mode.Composite=original)");
		int height = 20000, floor = 1000;
		Scene near = new Scene (40, 3000, height, 0);
		Scene far = new Scene (420, 3000, height, SHIFT);
		try {
			harness ("ScrolledComposite-shift", 0xFA2L, height, floor, true)
				.beforeSeed (scene -> {
					scene.sc.setOrigin (0, floor + scene.shift);
					scene.flush ();
				})
				.compare (near, far);
		} finally {
			near.dispose ();
			far.dispose ();
		}
	}
}
