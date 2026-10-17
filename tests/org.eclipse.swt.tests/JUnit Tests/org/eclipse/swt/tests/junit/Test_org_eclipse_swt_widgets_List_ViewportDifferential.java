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

import java.io.IOException;
import java.lang.invoke.MethodHandle;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Optional;
import java.util.Random;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Recorded differential of the List viewport (docs/m3/LIST_MEDIATED_VIEWPORT.md) on the shared
 * {@link ViewportDifferential} harness. A reference run (the official SWT jar, or this SWT with
 * <code>-Dswt.widget.mode=original</code>) records seeded scripts into
 * <code>-Dswt.viewport.recording=&lt;dir&gt;</code>; the viewport run replays them and compares every
 * step: the answer, both scroll bars, the selection, the caret and the Selection / DefaultSelection
 * events, and the pixels (<code>WM_PRINT</code>) after every seed.
 * <p>
 * Scripts mix the public API with user input delivered as the window messages Windows sends for it
 * (keys, characters, clicks, double clicks, drags with auto-scroll, scroll-bar codes and the wheel),
 * so they run on a hidden desktop. Shift and Ctrl are put into the thread's keyboard state
 * (<code>SetKeyboardState</code> through the foreign function API; the JVM runs with
 * <code>--enable-preview</code> on JDK 21), which is where the list box and SWT read them.
 * </p>
 */
public class Test_org_eclipse_swt_widgets_List_ViewportDifferential {
	static final int ROWS = 300, SEEDS = 10, STEPS = 150;
	Display display;
	Shell shell;

	/** The shell, the style and the List of the current seed (a new List per seed) with its event log. */
	static final class Subject {
		final Shell shell;
		final int style;
		final StringBuilder events = new StringBuilder ();
		List list;

		Subject (Shell shell, int style) {
			this.shell = shell;
			this.style = style;
		}

		void reset () {
			if (list != null) list.dispose ();
			list = new List (shell, style | SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
			list.setBounds (5, 5, 300, 380);
			for (int i = 0; i < ROWS; i++) list.add (text (i, i));
			ViewportDifferential.listen (list, events, "S", SWT.Selection);
			ViewportDifferential.listen (list, events, "D", SWT.DefaultSelection);
			shell.open ();
			list.setFocus ();
			ViewportDifferential.settle (shell.getDisplay (), 60);
			events.setLength (0);
		}
	}

	@BeforeEach
	public void setUp () {
		display = Display.getDefault ();
		shell = new Shell (display);
		shell.setBounds (20, 20, 330, 420);
	}

	@AfterEach
	public void tearDown () {
		shell.dispose ();
	}

	@Test
	public void singleSelectionListAnswersLikeTheReference () throws IOException {
		run ("list-single", SWT.SINGLE);
	}

	@Test
	public void extendedSelectionListAnswersLikeTheReference () throws IOException {
		run ("list-multi", SWT.MULTI);
	}

	@Test
	public void simpleMultiSelectionListAnswersLikeTheReference () throws IOException {
		run ("list-simple", SWT.MULTI | SWT.SIMPLE);
	}

	/** Geometry changes while scrolled: resizes and font changes mixed with scrolling, selection, keys, adds and removes. */
	@Test
	public void resizeAndFontWhileScrolledAnswerLikeTheReference () throws IOException {
		run ("list-resize", SWT.MULTI, Test_org_eclipse_swt_widgets_List_ViewportDifferential::resizeOp, STEPS);
	}

	/**
	 * A DPI change while scrolled (<code>WM_DPICHANGED</code> to the shell; with
	 * <code>-Dswt.autoScale.updateOnRuntime=true</code> SWT rescales fonts and bounds in runnables it posts): each seed
	 * scrolls and selects, changes to 120 or 144 dpi, scrolls, resizes and types, and changes back to 96 dpi, so every
	 * DPI change starts from an idle display. Without runtime rescaling the DPI steps answer "zoom off" in both runs.
	 */
	@Test
	public void zoomWhileScrolledAnswersLikeTheReference () throws IOException {
		run ("list-zoom", SWT.MULTI, Test_org_eclipse_swt_widgets_List_ViewportDifferential::zoomOp, ZOOM_STEPS);
	}

	private void run (String name, int style) throws IOException {
		run (name, style, Test_org_eclipse_swt_widgets_List_ViewportDifferential::randomOp, STEPS);
	}

	private void run (String name, int style, ViewportDifferential.Ops<Subject> ops, int steps) throws IOException {
		Assumptions.assumeTrue ("win32".equals (SWT.getPlatform ()), "the List viewport is win32 only");
		boolean reference = ViewportDifferential.isReference ("List");
		if (!reference) Assumptions.assumeTrue (viewportActive (shell), "List viewport mediation is not active in this build");
		Subject subject = new Subject (shell, style);
		ViewportDifferential.<Subject> of (name, 0x1157L, ops)
			.widget ("List").seeds (SEEDS).steps (steps)
			.beforeSeed (Subject::reset)
			.beforeStep (s -> s.events.setLength (0))
			.afterStep (s -> flush (s.shell.getDisplay ()))
			.probe ("bars", s -> ViewportDifferential.bar (s.list.getVerticalBar ()) + " " + ViewportDifferential.bar (s.list.getHorizontalBar ()))
			.probe ("sel", s -> selection (s.list))
			.probe ("focus", s -> s.list.getFocusIndex () + (s.list.isFocusControl () ? "*" : ""))
			.probe ("events", s -> s.events.toString ())
			.pixels (s -> {
				ViewportDifferential.settle (s.shell.getDisplay (), 30);
				return ViewportDifferential.render (s.list);
			})
			.compareWithRecording (subject, reference, System.getProperty ("swt.viewport.recording"));
	}

	static boolean viewportActive (Shell shell) {
		List list = new List (shell, SWT.SINGLE);
		try {
			java.lang.reflect.Field field = List.class.getDeclaredField ("viewport");
			field.setAccessible (true);
			return field.get (list) != null;
		} catch (ReflectiveOperationException e) {
			return false;
		} finally {
			list.dispose ();
		}
	}

	/* posted Selection events run after the next dispatched message */
	static void flush (Display display) {
		boolean [] ran = {false};
		display.asyncExec (() -> ran [0] = true);
		long end = System.currentTimeMillis () + 2000;
		while (!ran [0] && System.currentTimeMillis () < end) {
			if (!display.readAndDispatch ()) display.sleep ();
		}
		ViewportDifferential.drain (display);
	}

	static String text (int seed, int i) {
		return "row " + i + "x".repeat ((i * 7 + seed) % 11);
	}

	static String selection (List list) {
		int [] indices = list.getSelectionIndices ();
		String text = indices.length > 16 ? indices.length + " rows " + indices [0] + ".." + indices [indices.length - 1] + " sum "
				+ Arrays.stream (indices).asLongStream ().sum () : Arrays.toString (indices);
		return list.getSelectionCount () + " " + text;
	}

	static final String [] KEYS = {"UP", "DOWN", "PRIOR", "NEXT", "HOME", "END", "SPACE"};
	static final String [] MODIFIERS = {"none", "none", "shift", "ctrl", "ctrlshift"};

	/* one random operation; indices may be out of range on purpose */
	static ViewportDifferential.Op<Subject> randomOp (Random random, StringBuilder log) {
		int i = random.nextInt (ROWS + 3) - 1, j = i + random.nextInt (30), k = random.nextInt (ROWS + 3) - 1;
		int kind = random.nextInt (44);
		String name = switch (kind) {
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
			case 27 -> "getTop";
			case 28 -> "setEmpty " + i;
			case 29 -> "addEmpty " + i;
			case 30 -> "addNul " + i;
			case 32, 33, 42 -> "key " + KEYS [random.nextInt (KEYS.length)] + " " + MODIFIERS [random.nextInt (MODIFIERS.length)];
			case 34 -> "type " + "rnsxR".charAt (random.nextInt (5));
			/* rows 0..24 lie inside the client area; a real click cannot land below it */
			case 35, 36, 37, 43 -> "click " + random.nextInt (25) + " " + MODIFIERS [random.nextInt (MODIFIERS.length)];
			case 38 -> "drag " + random.nextInt (25) + " " + (random.nextBoolean () ? "down" : "up") + " " + (1 + random.nextInt (40));
			case 39 -> "vscroll " + random.nextInt (7);
			/*
			 * whole notches only: the list box keeps one wheel remainder per thread (ListWheelOracle), so a
			 * partial delta would make a seed depend on the seeds before it; partial deltas are an acceptance test
			 */
			case 40 -> "wheel " + new int [] {-120, 120, -240, -360} [random.nextInt (4)];
			case 41 -> "dblclick " + random.nextInt (25);
			default -> "getTop";
		};
		log.append (name).append ('\n');
		return subject -> {
			try {
				return apply (name, subject.list);
			} catch (IllegalArgumentException | SWTException e) {
				return "error " + e.getMessage ();
			}
		};
	}

	static final int [] FONT_HEIGHTS = {7, 9, 12, 16};
	static final int [] DPIS = {96, 120, 144};

	/* scrolling, resizing, font and DPI changes, some selection and keys; indices may be out of range on purpose */
	static ViewportDifferential.Op<Subject> resizeOp (Random random, StringBuilder log) {
		int i = random.nextInt (ROWS + 3) - 1;
		int kind = random.nextInt (16);
		String name = switch (kind) {
			case 0, 1 -> "setTopIndex " + i;
			case 2 -> "vscroll " + random.nextInt (7);
			case 3 -> "wheel " + new int [] {-120, 120, -360} [random.nextInt (3)];
			case 4, 5 -> "resize " + (40 + random.nextInt (420)) + " " + (120 + random.nextInt (300));
			case 6, 7 -> "font " + FONT_HEIGHTS [random.nextInt (FONT_HEIGHTS.length)];
			case 8, 9 -> "font " + FONT_HEIGHTS [random.nextInt (FONT_HEIGHTS.length)];
			case 10 -> "select " + i;
			case 11 -> "showSelection";
			case 12 -> "key " + KEYS [random.nextInt (KEYS.length)] + " none";
			case 13 -> "add " + random.nextInt (1000);
			case 14 -> "remove " + i;
			default -> "getTop";
		};
		log.append (name).append ('\n');
		return subject -> {
			try {
				return switch (name.split (" ") [0]) {
					case "resize" -> resize (subject, name);
					case "font" -> font (subject, Integer.parseInt (name.split (" ") [1]));
					case "zoom" -> Zoom.apply (subject.shell, Integer.parseInt (name.split (" ") [1]));
					default -> apply (name, subject.list);
				} + " top " + subject.list.getTopIndex () + " client " + subject.list.getClientArea () + " height " + subject.list.getItemHeight ();
			} catch (IllegalArgumentException | SWTException e) {
				return "error " + e.getMessage ();
			}
		};
	}

	/* dispatches until nothing has been pending for 100 ms (at most 5 s) */
	static void idle (Display display) {
		long end = System.currentTimeMillis () + 5000;
		int quiet = 0;
		while (quiet < 10 && System.currentTimeMillis () < end) {
			if (display.readAndDispatch ()) {
				quiet = 0;
			} else {
				quiet++;
				try {
					Thread.sleep (10);
				} catch (InterruptedException e) {
					Thread.currentThread ().interrupt ();
					return;
				}
			}
		}
	}

	static final int ZOOM_STEPS = 24;

	/* steps 0..9 scroll and select, step 10 zooms to 120 or 144 dpi, steps 11..22 scroll, resize, key, step 23 zooms back to 96 */
	static ViewportDifferential.Op<Subject> zoomOp (Random random, StringBuilder log) {
		int step = log.toString ().split ("\n", -1).length - 1;
		if (log.indexOf ("seed") == 0) step--;
		int i = random.nextInt (ROWS + 3) - 1;
		String name;
		if (step == 10) {
			name = "zoom " + (random.nextBoolean () ? 120 : 144);
		} else if (step == ZOOM_STEPS - 1) {
			name = "zoom 96";
		} else {
			name = switch (random.nextInt (step > 10 ? 8 : 5)) {
				case 0 -> "setTopIndex " + i;
				case 1 -> "select " + i;
				case 2 -> "wheel " + new int [] {-120, 120, -360} [random.nextInt (3)];
				case 3 -> "vscroll " + random.nextInt (7);
				case 4 -> "showSelection";
				case 5 -> "resize " + (40 + random.nextInt (420)) + " " + (120 + random.nextInt (300));
				case 6 -> "key " + KEYS [random.nextInt (KEYS.length)] + " none";
				default -> "add " + random.nextInt (1000);
			};
		}
		log.append (name).append ('\n');
		return subject -> {
			try {
				return switch (name.split (" ") [0]) {
					case "resize" -> resize (subject, name);
					case "zoom" -> Zoom.apply (subject.shell, Integer.parseInt (name.split (" ") [1]));
					default -> apply (name, subject.list);
				} + " top " + subject.list.getTopIndex () + " client " + subject.list.getClientArea () + " height " + subject.list.getItemHeight ();
			} catch (IllegalArgumentException | SWTException e) {
				return "error " + e.getMessage ();
			}
		};
	}

	static String resize (Subject subject, String op) {
		String [] a = op.split (" ");
		subject.list.setSize (Integer.parseInt (a [1]), Integer.parseInt (a [2]));
		return "resized";
	}

	private static final java.util.Map<Integer, org.eclipse.swt.graphics.Font> FONTS = new java.util.HashMap<> ();

	static String font (Subject subject, int height) {
		Display display = subject.list.getDisplay ();
		org.eclipse.swt.graphics.Font font = FONTS.computeIfAbsent (height, h -> {
			org.eclipse.swt.graphics.FontData data = display.getSystemFont ().getFontData () [0];
			data.setHeight (h);
			return new org.eclipse.swt.graphics.Font (display, data);
		});
		subject.list.setFont (font);
		return "font " + height;
	}

	/** <code>WM_DPICHANGED</code> to the shell with its current window rectangle, as Windows sends it when a monitor's scale changes. */
	static final class Zoom {
		static String apply (Shell shell, int dpi) {
			if (!shell.getDisplay ().isRescalingAtRuntime ()) return "zoom off";
			try {
				Class<?> os = Class.forName ("org.eclipse.swt.internal.win32.OS");
				Class<?> rectType = Class.forName ("org.eclipse.swt.internal.win32.RECT");
				long handle = Shell.class.getField ("handle").getLong (shell);
				Object rect = rectType.getConstructor ().newInstance ();
				os.getMethod ("GetWindowRect", long.class, rectType).invoke (null, handle, rect);
				int size = rectType.getField ("sizeof").getInt (null);
				long heap = (Long) os.getMethod ("GetProcessHeap").invoke (null);
				long memory = (Long) os.getMethod ("HeapAlloc", long.class, int.class, int.class).invoke (null, heap, 8, size);
				try {
					os.getMethod ("MoveMemory", long.class, rectType, int.class).invoke (null, memory, rect, size);
					ViewportDifferential.send (shell, 0x02E0, ((long) dpi << 16) | dpi, memory);
				} finally {
					os.getMethod ("HeapFree", long.class, int.class, long.class).invoke (null, heap, 0, memory);
				}
				/* SWT rescales the shell's children in runnables it posts (some post more): run until the display is idle */
				idle (shell.getDisplay ());
				return "zoom " + dpi;
			} catch (ReflectiveOperationException e) {
				throw new AssertionError (e);
			}
		}
	}

	static int number (String [] a, int index) {
		return a.length > index ? Integer.parseInt (a [index]) : 0;
	}

	static String apply (String op, List list) {
		String [] a = op.split (" ");
		switch (a [0]) {
			case "add" -> list.add ("new " + number (a, 1));
			case "addAt" -> list.add ("new " + number (a, 2) + "x".repeat (number (a, 2) % 23), number (a, 1));
			case "remove" -> list.remove (number (a, 1));
			case "removeRange" -> list.remove (number (a, 1), number (a, 2));
			case "removeIndices" -> list.remove (new int [] {number (a, 1), number (a, 2), number (a, 3), number (a, 2)});
			case "removeAll" -> list.removeAll ();
			case "setItem" -> list.setItem (number (a, 1), "set " + number (a, 2));
			case "setItems" -> {
				int count = number (a, 1);
				String [] items = new String [count];
				for (int i = 0; i < count; i++) items [i] = text (count, i);
				list.setItems (items);
			}
			case "select" -> list.select (number (a, 1));
			case "selectRange" -> list.select (number (a, 1), number (a, 2));
			case "selectIndices" -> list.select (new int [] {number (a, 1), number (a, 2)});
			case "deselect" -> list.deselect (number (a, 1));
			case "deselectRange" -> list.deselect (number (a, 1), number (a, 2));
			case "deselectIndices" -> list.deselect (new int [] {number (a, 1), number (a, 2)});
			case "selectAll" -> list.selectAll ();
			case "deselectAll" -> list.deselectAll ();
			case "setSelection" -> list.setSelection (number (a, 1));
			case "setSelectionRange" -> list.setSelection (number (a, 1), number (a, 2));
			case "setSelectionIndices" -> list.setSelection (new int [] {number (a, 1), number (a, 2)});
			case "setSelectionStrings" -> {
				int count = list.getItemCount (), x = number (a, 1), y = number (a, 2);
				String first = x >= 0 && x < count ? list.getItem (x) : "absent";
				String second = y >= 0 && y < count ? list.getItem (y) : "absent";
				list.setSelection (new String [] {first, second});
			}
			case "setTopIndex" -> list.setTopIndex (number (a, 1));
			case "showSelection" -> list.showSelection ();
			case "read" -> {
				int x = number (a, 1);
				return list.getItem (x) + " height " + list.getItemHeight () + " selected " + list.isSelected (x);
			}
			case "indexOf" -> {
				int count = list.getItemCount (), x = number (a, 1);
				String probe = x >= 0 && x < count ? list.getItem (x) : "absent";
				return "indexOf " + list.indexOf (probe, number (a, 2)) + " " + list.indexOf (probe);
			}
			case "selectionIndex" -> {
				return "index " + list.getSelectionIndex () + " count " + list.getSelectionCount ();
			}
			case "setEmpty" -> list.setItem (number (a, 1), "");
			case "addEmpty" -> list.add ("", Math.max (0, Math.min (number (a, 1), list.getItemCount ())));
			case "addNul" -> list.add ("nul\0tail " + number (a, 1), Math.max (0, Math.min (number (a, 1), list.getItemCount ())));
			case "key" -> Input.key (list, a [1], a [2]);
			case "type" -> Input.character (list, a [1].charAt (0));
			case "click" -> Input.click (list, number (a, 1), a [2], false);
			case "dblclick" -> Input.click (list, number (a, 1), "none", true);
			case "drag" -> Input.drag (list, number (a, 1), a [2].equals ("down"), number (a, 3));
			case "vscroll" -> ViewportDifferential.send (list, 0x115, new int [] {1, 0, 3, 2, 6, 7, 8} [number (a, 1)], 0);
			case "wheel" -> ViewportDifferential.send (list, 0x20A, (long) (number (a, 1) & 0xFFFF) << 16, 0);
			default -> {}
		}
		return "top " + list.getTopIndex () + " count " + list.getItemCount ();
	}

	/**
	 * User input as the window messages Windows delivers for it, sent to the list. Shift and Ctrl
	 * are the thread's keyboard state while the messages run.
	 */
	static final class Input {
		static final int WM_KEYDOWN = 0x100, WM_KEYUP = 0x101, WM_CHAR = 0x102, WM_MOUSEMOVE = 0x200, WM_LBUTTONDOWN = 0x201,
				WM_LBUTTONUP = 0x202, WM_LBUTTONDBLCLK = 0x203, MK_LBUTTON = 1, MK_SHIFT = 4, MK_CONTROL = 8;
		static final int VK_SHIFT = 0x10, VK_CONTROL = 0x11, VK_LSHIFT = 0xA0, VK_LCONTROL = 0xA2;

		static void key (List list, String name, String modifiers) {
			int vk = switch (name) {
				case "UP" -> 0x26;
				case "DOWN" -> 0x28;
				case "PRIOR" -> 0x21;
				case "NEXT" -> 0x22;
				case "HOME" -> 0x24;
				case "END" -> 0x23;
				default -> 0x20;
			};
			boolean extended = vk != 0x20;
			long lParam = 1 | ((long) scanCode (vk) << 16) | (extended ? 1L << 24 : 0);
			modifiers (modifiers.contains ("shift"), modifiers.contains ("ctrl"));
			try {
				ViewportDifferential.send (list, WM_KEYDOWN, vk, lParam);
				if (vk == 0x20) ViewportDifferential.send (list, WM_CHAR, ' ', lParam);
				ViewportDifferential.send (list, WM_KEYUP, vk, lParam | 0xC0000000L);
			} finally {
				modifiers (false, false);
			}
		}

		static void character (List list, char character) {
			ViewportDifferential.send (list, WM_CHAR, character, 1);
		}

		/* a click on a row of the client area (rows below the last one are empty space) */
		static void click (List list, int row, String modifiers, boolean twice) {
			boolean shift = modifiers.contains ("shift"), ctrl = modifiers.contains ("ctrl");
			int height = list.getItemHeight ();
			long point = ViewportDifferential.makeLParam (40, row * height + height / 2);
			int keys = (shift ? MK_SHIFT : 0) | (ctrl ? MK_CONTROL : 0);
			modifiers (shift, ctrl);
			try {
				ViewportDifferential.send (list, WM_MOUSEMOVE, keys, point);
				ViewportDifferential.send (list, WM_LBUTTONDOWN, MK_LBUTTON | keys, point);
				ViewportDifferential.send (list, WM_LBUTTONUP, keys, point);
				if (twice) {
					ViewportDifferential.send (list, WM_LBUTTONDBLCLK, MK_LBUTTON | keys, point);
					ViewportDifferential.send (list, WM_LBUTTONUP, keys, point);
				}
			} finally {
				modifiers (false, false);
			}
		}

		/* button down on a visible row, then moves above or below the client area (one auto-scrolled row each), then up */
		static void drag (List list, int row, boolean down, int moves) {
			int height = list.getItemHeight ();
			long start = ViewportDifferential.makeLParam (40, row * height + height / 2);
			long outside = ViewportDifferential.makeLParam (40, down ? list.getClientArea ().height + 6 : -6);
			ViewportDifferential.send (list, WM_LBUTTONDOWN, MK_LBUTTON, start);
			for (int i = 0; i < moves; i++) ViewportDifferential.send (list, WM_MOUSEMOVE, MK_LBUTTON, outside);
			ViewportDifferential.send (list, WM_LBUTTONUP, 0, outside);
		}

		static int scanCode (int vk) {
			try {
				return (Integer) Class.forName ("org.eclipse.swt.internal.win32.OS").getMethod ("MapVirtualKey", int.class, int.class).invoke (null, vk, 0);
			} catch (ReflectiveOperationException e) {
				throw new AssertionError (e);
			}
		}

		private static MethodHandle setKeyboardState;
		private static Object keyState, byteLayout;
		private static java.lang.reflect.Method setByte;

		/*
		 * SetKeyboardState (user32) through java.lang.foreign, reached by reflection so that the
		 * test bundle compiles without preview features; the JVM needs --enable-preview on JDK 21.
		 */
		static void modifiers (boolean shift, boolean ctrl) {
			try {
				if (setKeyboardState == null) {
					Class<?> linkerType = Class.forName ("java.lang.foreign.Linker");
					Class<?> arenaType = Class.forName ("java.lang.foreign.Arena");
					Class<?> lookupType = Class.forName ("java.lang.foreign.SymbolLookup");
					Class<?> layoutType = Class.forName ("java.lang.foreign.MemoryLayout");
					Class<?> valueType = Class.forName ("java.lang.foreign.ValueLayout");
					Class<?> descriptorType = Class.forName ("java.lang.foreign.FunctionDescriptor");
					Class<?> segmentType = Class.forName ("java.lang.foreign.MemorySegment");
					Class<?> optionType = Class.forName ("java.lang.foreign.Linker$Option");
					Object linker = linkerType.getMethod ("nativeLinker").invoke (null);
					Object arena = arenaType.getMethod ("global").invoke (null);
					Object user32 = lookupType.getMethod ("libraryLookup", String.class, arenaType).invoke (null, "user32", arena);
					Object symbol = ((Optional<?>) lookupType.getMethod ("find", String.class).invoke (user32, "SetKeyboardState")).orElseThrow ();
					Object arguments = Array.newInstance (layoutType, 1);
					Array.set (arguments, 0, valueType.getField ("ADDRESS").get (null));
					Object descriptor = descriptorType.getMethod ("of", layoutType, arguments.getClass ()).invoke (null, valueType.getField ("JAVA_INT").get (null), arguments);
					Object options = Array.newInstance (optionType, 0);
					setKeyboardState = (MethodHandle) linkerType.getMethod ("downcallHandle", segmentType, descriptorType, options.getClass ())
						.invoke (linker, symbol, descriptor, options);
					keyState = Class.forName ("java.lang.foreign.SegmentAllocator").getMethod ("allocate", long.class).invoke (arena, 256L);
					byteLayout = valueType.getField ("JAVA_BYTE").get (null);
					setByte = segmentType.getMethod ("set", Class.forName ("java.lang.foreign.ValueLayout$OfByte"), long.class, byte.class);
				}
				for (int vk : new int [] {VK_SHIFT, VK_LSHIFT}) setByte.invoke (keyState, byteLayout, (long) vk, shift ? (byte) 0x80 : (byte) 0);
				for (int vk : new int [] {VK_CONTROL, VK_LCONTROL}) setByte.invoke (keyState, byteLayout, (long) vk, ctrl ? (byte) 0x80 : (byte) 0);
				setKeyboardState.invokeWithArguments (keyState);
			} catch (Throwable e) {
				throw new AssertionError ("SetKeyboardState is not reachable (run the JVM with --enable-preview on JDK 21)", e);
			}
		}
	}
}
