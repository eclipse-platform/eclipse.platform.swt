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
import java.util.regex.Matcher;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.junit.jupiter.api.*;

/**
 * Recorded differential of ExpandBar (leaf L6, docs/m3/EXPANDBAR_VIEWPORT.md) against a reference run (the official
 * SWT jar, or this SWT with <code>-Dswt.viewport.record=true</code>): seeded scripts add and dispose items (some with a
 * control, heights up to 920 pixels, so that the content passes the 16-bit native coordinate range), expand and
 * collapse them by API and by clicks on the headers, change heights, spacing, texts, images and the font, scroll with
 * the bar and the wheel and resize the bar; every step records the answer, the state (scroll bar, client area, item
 * geometry, the bounds of the item controls), whether every control is at its logical position, the Expand and Collapse
 * events, the moves of item controls inside the native range and the Paint regions; every seed the rendered pixels.
 * Stock win32 clamps a child window to the 16-bit range; the viewport mode keeps the logical position, so where the
 * reference is away from it the viewport must be logical ({@link #LOGICAL_WHERE_STOCK_CLAMPS}). Win32 only
 * (scrolling is driven with window messages).
 */
public class Test_org_eclipse_swt_widgets_ExpandBar_ViewportDifferential {
	static final int WM_VSCROLL = 0x115, WM_MOUSEWHEEL = 0x20A, WM_LBUTTONDOWN = 0x201, WM_LBUTTONUP = 0x202;
	/* the native window range: stock win32 places a child window at most here (and reports that position) */
	static final int NATIVE_MIN = -32768, NATIVE_MAX = 32767;
	static final java.util.regex.Pattern BOUNDS = java.util.regex.Pattern.compile ("Rectangle \\{(-?\\d+), (-?\\d+), ");

	/**
	 * Where the reference places an item control away from its logical position (stock clamps a child window to the
	 * native range, and ScrollWindowEx then moves the clamped window), the viewport must place every control logically
	 * and equal the reference in everything but the control y positions and the moves counted inside the native range.
	 * Everywhere else the lines are equal.
	 */
	static final ViewportDifferential.Relation LOGICAL_WHERE_STOCK_CLAMPS = (expected, actual) -> {
		if (expected.equals (actual)) return true;
		if (!stockClamps (expected) || !actual.endsWith ("logical ok")) return false;
		return mask (expected).equals (mask (actual));
	};

	/* the reference line shows a control away from its logical position, or at or beyond the native edge */
	static boolean stockClamps (String line) {
		if (line.contains ("logical off")) return true;
		Matcher matcher = BOUNDS.matcher (line);
		while (matcher.find ()) {
			int y = Integer.parseInt (matcher.group (2));
			if (y <= NATIVE_MIN || y >= NATIVE_MAX) return true;
		}
		return false;
	}

	static String mask (String line) {
		Matcher matcher = BOUNDS.matcher (line);
		StringBuilder out = new StringBuilder ();
		while (matcher.find ()) {
			matcher.appendReplacement (out, Matcher.quoteReplacement ("Rectangle {" + matcher.group (1) + ", *, "));
		}
		matcher.appendTail (out);
		return out.toString ().replaceAll ("moves \\d+", "moves *").replaceAll ("logical .*", "logical");
	}
	Display display;

	@BeforeEach
	public void setUp () {
		Assumptions.assumeTrue ("win32".equals (SWT.getPlatform ()), "scrolling is driven with win32 messages");
		display = Display.getDefault ();
		ViewportDifferential.parkCursor (display);
	}

	final class Scene {
		final Shell shell;
		final ExpandBar bar;
		final StringBuilder events = new StringBuilder (), paints = new StringBuilder ();
		final Font [] fonts;
		final Image [] images;
		int created, moves;

		Scene () {
			shell = new Shell (display, SWT.NO_TRIM | SWT.ON_TOP);
			shell.setBounds (40, 40, 400, 300);
			shell.setLayout (new FillLayout ());
			bar = new ExpandBar (shell, SWT.V_SCROLL);
			ViewportDifferential.listen (bar, events, "E", SWT.Expand);
			ViewportDifferential.listen (bar, events, "C", SWT.Collapse);
			bar.addListener (SWT.Paint, e -> paints.append (e.x).append (',').append (e.y).append (' ').append (e.width).append ('x').append (e.height).append (';'));
			fonts = new Font [] {new Font (display, "Segoe UI", 8, SWT.NORMAL), new Font (display, "Segoe UI", 14, SWT.BOLD)};
			images = new Image [] {image (16), image (40)};
			shell.open ();
			flush ();
		}

		Image image (int height) {
			Image image = new Image (display, 16, height);
			GC gc = new GC (image);
			gc.setBackground (display.getSystemColor (height > 20 ? SWT.COLOR_DARK_BLUE : SWT.COLOR_DARK_RED));
			gc.fillRectangle (0, 0, 16, height);
			gc.dispose ();
			return image;
		}

		void flush () {
			ViewportDifferential.drain (display);
			bar.update ();
			ViewportDifferential.drain (display);
		}

		void add (int index, int b, boolean expanded) {
			ExpandItem item = new ExpandItem (bar, SWT.NONE, Math.min (index, bar.getItemCount ()));
			item.setText ("item " + created++);
			item.setHeight (20 + (b * 37) % 900);
			if (b % 3 == 0) {
				Label label = new Label (bar, SWT.BORDER);
				label.setText ("control of " + item.getText ());
				label.addListener (SWT.Move, e -> {
					/* moves inside the native range (stock does not move a control along its clamped edge) */
					int y = label.getBounds ().y;
					if (NATIVE_MIN < y && y < NATIVE_MAX) moves++;
				});
				item.setControl (label);
			}
			item.setExpanded (expanded);
		}

		void dispose (ExpandItem item) {
			Control control = item.getControl ();
			item.dispose ();
			if (control != null) control.dispose ();
		}

		String state () {
			StringBuilder out = new StringBuilder ();
			ExpandItem [] items = bar.getItems ();
			out.append ("count ").append (items.length).append (" bar ").append (ViewportDifferential.bar (bar.getVerticalBar ()))
				.append (" client ").append (bar.getClientArea ()).append (" spacing ").append (bar.getSpacing ());
			int expanded = 0, lastControl = -1;
			for (int i = 0; i < items.length; i++) {
				if (items [i].getExpanded ()) expanded++;
				if (items [i].getControl () != null) lastControl = i;
			}
			out.append (" expanded ").append (expanded);
			for (int i : new int [] {0, items.length / 2, lastControl, items.length - 1}) {
				if (i < 0 || i >= items.length) continue;
				ExpandItem item = items [i];
				out.append (" i").append (i).append (' ').append (item.getText ()).append (" h").append (item.getHeaderHeight ()).append ('/')
					.append (item.getHeight ()).append (item.getExpanded () ? " +" : " -");
				Control control = item.getControl ();
				if (control != null) out.append (' ').append (control.getBounds ()).append (control.getVisible () ? " v" : " h");
			}
			return out.toString ();
		}

		/**
		 * Every item control against the layout computed from the public API (spacing, header heights, heights,
		 * expanded states), relative to the first control (the scroll offset of the items is not public):
		 * <code>ok</code>, or how many controls are elsewhere and the first one.
		 */
		String logical () {
			ExpandItem [] items = bar.getItems ();
			int spacing = bar.getSpacing (), y = 0, off = 0;
			Integer base = null;
			String first = "";
			for (int i = 0; i < items.length; i++) {
				ExpandItem item = items [i];
				Control control = item.getControl ();
				if (control != null) {
					int at = control.getBounds ().y - (y + item.getHeaderHeight ());
					if (base == null) {
						base = at;
					} else if (at != base) {
						if (off++ == 0) first = " i" + i + " " + control.getBounds ().y + "/" + (base + y + item.getHeaderHeight ());
					}
				}
				y += item.getHeaderHeight () + spacing + (item.getExpanded () ? item.getHeight () : 0);
			}
			return off == 0 ? "ok" : "off " + off + first;
		}

		void dispose () {
			shell.dispose ();
			for (Font font : fonts) font.dispose ();
			for (Image image : images) image.dispose ();
		}
	}

	static ViewportDifferential.Ops<Scene> ops () {
		return (random, log) -> {
			int kind = random.nextInt (20);
			int a = random.nextInt (1 << 16), b = random.nextInt (1000), count = 1 + random.nextInt (random.nextInt (4) == 0 ? 60 : 6);
			boolean flag = random.nextBoolean ();
			int code = new int [] {0, 1, 2, 3, 6, 7} [b % 6]; // SB_LINEUP, SB_LINEDOWN, SB_PAGEUP, SB_PAGEDOWN, SB_TOP, SB_BOTTOM
			log.append (switch (kind) {
				case 0, 1, 17, 18 -> "add " + count + " items at " + a;
				case 2 -> "dispose an item " + a;
				case 3, 4 -> "setExpanded " + a + " " + flag;
				case 5 -> "click at y " + (a % 300);
				case 6 -> "setHeight " + a + " " + (b % 420);
				case 7 -> "setSpacing " + (b % 13);
				case 8, 9 -> "WM_VSCROLL " + code;
				case 10 -> "wheel " + (flag ? 120 : -120) * (1 + b % 3);
				case 11 -> "resize " + (150 + a % 400) + "x" + (100 + b % 400);
				case 12 -> "setText " + a;
				case 13 -> "setImage " + a + " " + b % 3;
				case 14 -> "setFont " + b % 3;
				case 15 -> "expand all " + flag;
				case 16 -> "WM_VSCROLL " + code + " x" + (2 + b % 20);
				default -> "dispose all";
			}).append ('\n');
			return scene -> {
				ExpandBar bar = scene.bar;
				int items = bar.getItemCount ();
				switch (kind) {
					case 0, 1, 17, 18 -> {
						for (int i = 0; i < count; i++) scene.add (a % (items + 1) + i, b + i * 11, flag);
					}
					case 2 -> {
						if (items > 0) scene.dispose (bar.getItem (a % items));
					}
					case 3, 4 -> {
						if (items > 0) bar.getItem (a % items).setExpanded (flag);
					}
					case 5 -> {
						long position = ViewportDifferential.makeLParam (12, a % 300);
						ViewportDifferential.send (bar, WM_LBUTTONDOWN, 1, position);
						ViewportDifferential.send (bar, WM_LBUTTONUP, 0, position);
					}
					case 6 -> {
						if (items > 0) bar.getItem (a % items).setHeight (b % 420);
					}
					case 7 -> bar.setSpacing (b % 13);
					case 8, 9 -> ViewportDifferential.send (bar, WM_VSCROLL, code, 0);
					case 10 -> ViewportDifferential.send (bar, WM_MOUSEWHEEL, ((long) ((flag ? 120 : -120) * (1 + b % 3)) & 0xFFFF) << 16, ViewportDifferential.makeLParam (100, 100));
					case 11 -> scene.shell.setSize (150 + a % 400, 100 + b % 400);
					case 12 -> {
						if (items > 0) bar.getItem (a % items).setText ("renamed " + a);
					}
					case 13 -> {
						if (items > 0) bar.getItem (a % items).setImage (b % 3 == 2 ? null : scene.images [b % 3]);
					}
					case 14 -> bar.setFont (b % 3 == 2 ? null : scene.fonts [b % 3]);
					case 15 -> {
						for (ExpandItem item : bar.getItems ()) item.setExpanded (flag);
					}
					case 16 -> {
						for (int i = 0; i < 2 + b % 20; i++) ViewportDifferential.send (bar, WM_VSCROLL, code, 0);
					}
					default -> {
						for (ExpandItem item : bar.getItems ()) scene.dispose (item);
					}
				}
				return "";
			};
		};
	}

	@Test
	public void recordedAgainstReference () throws IOException {
		String reference = ViewportDifferential.reference ("ExpandBar");
		boolean viewport = "viewport".equals (reference);
		Scene scene = new Scene ();
		try {
			ViewportDifferential.of ("ExpandBar", 0xE4BA0L, ops ()).widget ("ExpandBar").seeds (8).steps (120)
				.beforeStep (s -> {
					s.events.setLength (0);
					s.paints.setLength (0);
					s.moves = 0;
				})
				.afterStep (Scene::flush)
				.probe ("state", s -> s.state () + " moves " + s.moves + " logical " + s.logical (), viewport ? LOGICAL_WHERE_STOCK_CLAMPS : String::equals)
				.probe ("events", s -> s.events.toString ())
				.probe ("paint", s -> s.paints.toString (), viewport ? ViewportDifferential.REGIONS_WITHIN : String::equals)
				.pixels (s -> {
					ViewportDifferential.settle (display, 100);
					return ViewportDifferential.render (s.bar);
				})
				.compareWithRecording (scene, ViewportDifferential.records ("ExpandBar"), System.getProperty ("swt.viewport.recording"));
		} finally {
			scene.dispose ();
		}
	}
}
