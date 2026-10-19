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

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Random;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Recorded differential of the Combo viewport (docs/m3/LIST_MEDIATED_VIEWPORT.md, "Combo") on the
 * shared {@link ViewportDifferential} harness. A reference run (the official SWT jar, or this SWT with
 * <code>-Dswt.widget.mode=original</code>) records seeded scripts into
 * <code>-Dswt.viewport.recording=&lt;dir&gt;</code>; the viewport run replays them and compares every
 * step: the answer, the selection, the text, the item count, whether the list is dropped, the Modify,
 * Selection and DefaultSelection events, the pixels of the combo (<code>WM_PRINT</code>) and, while the
 * list is dropped, the dropped list's vertical bar and pixels.
 * <p>
 * Scripts mix the item and selection API with user input delivered as the window messages Windows
 * sends for it: keys and characters to the window with the focus (the combo when it is read-only,
 * its edit field otherwise), Alt+Down, the wheel, and the dropped list's scroll-bar codes, wheel,
 * hovering and clicks. They run on a hidden desktop.
 * </p>
 */
public class Test_org_eclipse_swt_widgets_Combo_ViewportDifferential {
	static final int ROWS = 300, SEEDS = 10, STEPS = 150;
	static final String [] WORDS = {"apple ", "berry ", "cherry ", "Apple ", "date ", "elder "};
	Display display;
	Shell shell;

	/** The shell, the style and the Combo of the current seed (a new Combo per seed) with its event log. */
	static final class Subject {
		final Shell shell;
		final int style;
		final boolean verify;
		final StringBuilder events = new StringBuilder ();
		Combo combo;

		Subject (Shell shell, int style) {
			this (shell, style, false);
		}

		Subject (Shell shell, int style, boolean verify) {
			this.shell = shell;
			this.style = style;
			this.verify = verify;
		}

		void reset () {
			if (combo != null) combo.dispose ();
			combo = new Combo (shell, style);
			combo.setBounds (5, 5, 300, 30);
			combo.setVisibleItemCount (20);
			for (int i = 0; i < ROWS; i++) combo.add (text (i));
			ViewportDifferential.listen (combo, events, "M", SWT.Modify);
			ViewportDifferential.listen (combo, events, "S", SWT.Selection);
			ViewportDifferential.listen (combo, events, "D", SWT.DefaultSelection);
			if (verify) combo.addListener (SWT.Verify, this::verify);
			shell.open ();
			combo.setFocus ();
			ViewportDifferential.settle (shell.getDisplay (), 60);
			events.setLength (0);
		}

		/*
		 * A Verify listener (a read-only combo verifies the item text a CB_SETCURSEL would show): logged, then
		 * vetoed for texts ending in 7 and redirected to the first item for texts ending in 3.
		 */
		void verify (org.eclipse.swt.widgets.Event event) {
			events.append ("V").append (event.start).append (',').append (event.end).append (':').append (event.text).append (' ');
			if (event.text.endsWith ("7")) {
				event.doit = false;
			} else if (event.text.endsWith ("3") && combo.getItemCount () > 0) {
				event.text = combo.getItem (0);
			}
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
	public void readOnlyComboAnswersLikeTheReference () throws IOException {
		run ("combo-readonly", SWT.DROP_DOWN | SWT.READ_ONLY);
	}

	@Test
	public void editableComboAnswersLikeTheReference () throws IOException {
		run ("combo-dropdown", SWT.DROP_DOWN);
	}

	/** A read-only combo with a Verify listener that logs, vetoes and redirects the item texts the combo would show. */
	@Test
	public void readOnlyComboWithVerifyAnswersLikeTheReference () throws IOException {
		run ("combo-verify", SWT.DROP_DOWN | SWT.READ_ONLY, true);
	}

	private void run (String name, int style) throws IOException {
		run (name, style, false);
	}

	private void run (String name, int style, boolean verify) throws IOException {
		Assumptions.assumeTrue ("win32".equals (SWT.getPlatform ()), "the Combo viewport is win32 only");
		boolean reference = ViewportDifferential.isReference ("Combo");
		if (!reference) Assumptions.assumeTrue (viewportActive (shell), "Combo viewport mediation is not active in this build");
		Subject subject = new Subject (shell, style, verify);
		ViewportDifferential.<Subject> of (name, 0xC0B0L, Test_org_eclipse_swt_widgets_Combo_ViewportDifferential::randomOp)
			.widget ("Combo").seeds (SEEDS).steps (STEPS)
			.beforeSeed (Subject::reset)
			.beforeStep (s -> s.events.setLength (0))
			.afterStep (s -> flush (s.shell.getDisplay ()))
			.probe ("state", s -> "sel " + s.combo.getSelectionIndex () + " count " + s.combo.getItemCount () + " text '" + s.combo.getText ()
				+ "' dropped " + s.combo.getListVisible ())
			.probe ("events", s -> s.events.toString ())
			.probe ("face", s -> {
				hideCues (s.combo);
				return ViewportDifferential.hash (ViewportDifferential.render (s.combo));
			})
			.probe ("list", s -> s.combo.getListVisible () ? Native.bar (Native.list (s.combo)) + " " + ViewportDifferential.hash (Native.print (s.combo, Native.list (s.combo))) : "-")
			.pixels (s -> {
				ViewportDifferential.settle (s.shell.getDisplay (), 30);
				hideCues (s.combo);
				return ViewportDifferential.render (s.combo);
			})
			.compareWithRecording (subject, reference, System.getProperty ("swt.viewport.recording"));
	}

	/*
	 * Keyboard cues (the focus rectangle, mnemonic underlines) follow the thread's input history: SWT's traversal
	 * code sends WM_CHANGEUISTATE UIS_INITIALIZE for keyboard input, the scripted keys here are sent messages,
	 * and the state a new combo starts with depends on what ran before it. Both sides are captured with the cues
	 * hidden (as the Tree differential's hideFocusCues does).
	 */
	static void hideCues (Combo combo) {
		ViewportDifferential.send (combo.getShell (), 0x0127 /* WM_CHANGEUISTATE */,
			1 /* UIS_SET */ | ((0x1 /* UISF_HIDEFOCUS */ | 0x2 /* UISF_HIDEACCEL */) << 16), 0);
		ViewportDifferential.settle (combo.getDisplay (), 30);
	}

	static boolean viewportActive (Shell shell) {
		Combo combo = new Combo (shell, SWT.DROP_DOWN);
		try {
			Field field = Combo.class.getDeclaredField ("viewport");
			field.setAccessible (true);
			return field.get (combo) != null;
		} catch (ReflectiveOperationException e) {
			return false;
		} finally {
			combo.dispose ();
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

	static String text (int i) {
		return WORDS [i % WORDS.length] + i;
	}

	static final String [] KEYS = {"UP", "DOWN", "PRIOR", "NEXT", "HOME", "END", "LEFT", "RIGHT", "F4", "ESCAPE", "RETURN"};

	/* one random operation; indices may be out of range on purpose */
	static ViewportDifferential.Op<Subject> randomOp (Random random, StringBuilder log) {
		int i = random.nextInt (ROWS + 3) - 1, j = i + random.nextInt (12), k = random.nextInt (ROWS + 3) - 1;
		int kind = random.nextInt (40);
		String name = switch (kind) {
			case 0 -> "add " + random.nextInt (1000);
			case 1 -> "addAt " + i + " " + random.nextInt (1000);
			case 2 -> "remove " + i;
			case 3 -> "removeRange " + i + " " + j;
			case 4 -> random.nextInt (10) == 0 ? "removeAll" : "removeString " + i;
			case 5 -> random.nextInt (8) == 0 ? "setItems " + random.nextInt (400) : "setItem " + i + " " + random.nextInt (1000);
			case 6, 7 -> "select " + i;
			case 8 -> "deselect " + i;
			case 9 -> random.nextInt (3) == 0 ? "deselectAll" : "selectionIndex";
			case 10 -> "setText " + i + " " + random.nextInt (4);
			case 11 -> "read " + i + " " + k;
			case 12 -> "visible " + (3 + random.nextInt (25));
			case 13 -> "drop " + random.nextBoolean ();
			case 14, 15, 16, 17 -> "key " + KEYS [random.nextInt (KEYS.length)];
			case 18 -> "altdown";
			case 19, 20 -> "type " + "abcdeABz1 \b".charAt (random.nextInt (11));
			case 21 -> "wheel " + new int [] {-120, 120, -240, 360} [random.nextInt (4)];
			case 22, 23 -> "listwheel " + new int [] {-120, 120, -360, 240} [random.nextInt (4)];
			case 24 -> "vscroll " + random.nextInt (6);
			case 25 -> "thumb " + random.nextInt (ROWS + 20);
			case 26, 27 -> "hover " + random.nextInt (20);
			case 28 -> "click " + random.nextInt (20);
			case 29, 30, 31 -> "drop true";
			case 32 -> "addEmpty " + i;
			case 33 -> "indexOfEmpty " + (random.nextInt (8) - 2);
			default -> "selectionIndex";
		};
		log.append (name).append ('\n');
		return subject -> {
			try {
				return acceptListedDefects (name, subject);
			} catch (IllegalArgumentException | SWTException e) {
				return "error " + e.getMessage ();
			}
		};
	}

	/*
	 * The viewport fixes the upstream defects listed in docs/m3/list-viewport/UPSTREAM_DEFECTS.tsv; exactly those
	 * divergences are accepted here, so the stock recording and the viewport run answer alike:
	 * COMBO-1 indexOf ("", negative start) throws in stock, answers -1 in the viewport;
	 * COMBO-2 deselect (index out of range) with no selection sends Modify in stock (and clears an editable combo's
	 * text), nothing in the viewport.
	 */
	static String acceptListedDefects (String op, Subject subject) {
		String [] a = op.split (" ");
		Combo combo = subject.combo;
		if (a [0].equals ("indexOfEmpty") && number (a, 1) < 0) {
			try {
				int index = combo.indexOf ("", number (a, 1));
				if (index != -1) return "indexOf '' " + index;
			} catch (IllegalArgumentException e) {
				/* stock: ERROR_INVALID_RANGE */
			}
			return "defect COMBO-1";
		}
		int count = combo.getItemCount (), before = subject.events.length ();
		String text = combo.getText ();
		String answer = apply (op, combo);
		if (a [0].equals ("deselect") && (number (a, 1) < 0 || number (a, 1) >= count)) {
			if (subject.events.indexOf ("M ", before) == before) subject.events.delete (before, before + 2);
			/* stock also cleared an editable combo's text (CB_SETCURSEL -1): both runs put the text back */
			if ((combo.getStyle () & SWT.READ_ONLY) == 0) combo.setText (text);
		}
		return answer;
	}

	static int number (String [] a, int index) {
		return a.length > index ? Integer.parseInt (a [index]) : 0;
	}

	static String apply (String op, Combo combo) {
		String [] a = op.split (" ");
		switch (a [0]) {
			case "add" -> combo.add ("new " + number (a, 1));
			case "addAt" -> combo.add (text (number (a, 2)) + "+", number (a, 1));
			case "remove" -> combo.remove (number (a, 1));
			case "removeRange" -> combo.remove (number (a, 1), number (a, 2));
			case "removeString" -> {
				int count = combo.getItemCount (), x = number (a, 1);
				combo.remove (x >= 0 && x < count ? combo.getItem (x) : "absent");
			}
			case "removeAll" -> combo.removeAll ();
			case "setItem" -> combo.setItem (number (a, 1), "set " + number (a, 2));
			case "setItems" -> {
				int count = number (a, 1);
				String [] items = new String [count];
				for (int i = 0; i < count; i++) items [i] = text (i + count);
				combo.setItems (items);
			}
			case "select" -> combo.select (number (a, 1));
			case "deselect" -> combo.deselect (number (a, 1));
			case "deselectAll" -> combo.deselectAll ();
			case "setText" -> {
				int count = combo.getItemCount (), x = number (a, 1);
				String item = x >= 0 && x < count ? combo.getItem (x) : "absent";
				/* the whole item, a prefix of it, another case, or text that matches nothing */
				combo.setText (switch (number (a, 2)) {
					case 0 -> item;
					case 1 -> item.substring (0, Math.min (item.length (), 4 + x % 3));
					case 2 -> item.toUpperCase ();
					default -> "zz" + x;
				});
			}
			case "read" -> {
				int count = combo.getItemCount (), x = number (a, 1);
				String probe = x >= 0 && x < count ? combo.getItem (x) : "absent";
				return "item " + probe + " indexOf " + combo.indexOf (probe) + " " + combo.indexOf (probe, number (a, 2)) + " items "
					+ combo.getItems ().length + " height " + combo.getItemHeight ();
			}
			case "selectionIndex" -> {
				return "index " + combo.getSelectionIndex ();
			}
			case "visible" -> combo.setVisibleItemCount (number (a, 1));
			case "addEmpty" -> combo.add ("", Math.max (0, Math.min (number (a, 1), combo.getItemCount ())));
			case "indexOfEmpty" -> {
				return "indexOf '' " + combo.indexOf ("", number (a, 1)) + " " + combo.indexOf ("");
			}
			case "drop" -> combo.setListVisible (Boolean.parseBoolean (a [1]));
			case "key" -> Native.key (combo, a [1]);
			case "altdown" -> Native.altDown (combo);
			case "type" -> Native.character (combo, a.length > 1 ? (a [1].isEmpty () ? ' ' : a [1].charAt (0)) : ' ');
			case "wheel" -> ViewportDifferential.send (combo, 0x20A, (long) (number (a, 1) & 0xFFFF) << 16, 0);
			case "listwheel" -> {
				if (combo.getListVisible ()) Native.send (Native.list (combo), 0x20A, (long) (number (a, 1) & 0xFFFF) << 16, 0);
			}
			case "vscroll" -> {
				if (combo.getListVisible ()) Native.send (Native.list (combo), 0x115, new int [] {1, 0, 3, 2, 6, 7} [number (a, 1)], 0);
			}
			case "thumb" -> {
				if (combo.getListVisible ()) Native.send (Native.list (combo), 0x115, 5 | ((long) number (a, 1) << 16), 0);
			}
			case "hover", "click" -> {
				if (combo.getListVisible ()) Native.mouse (combo, number (a, 1), a [0].equals ("click"));
			}
			default -> {}
		}
		return "ok";
	}

	/** win32 messages and structures reached by reflection (the test bundle does not compile against SWT internals). */
	static final class Native {
		static final int WM_KEYDOWN = 0x100, WM_KEYUP = 0x101, WM_CHAR = 0x102, WM_SYSKEYDOWN = 0x104, WM_SYSKEYUP = 0x105, WM_MOUSEMOVE = 0x200,
				WM_LBUTTONDOWN = 0x201, WM_LBUTTONUP = 0x202, WM_PRINT = 0x317, LB_GETITEMHEIGHT = 0x1a1;

		static Class<?> os () throws ClassNotFoundException {
			return Class.forName ("org.eclipse.swt.internal.win32.OS");
		}

		static Object info (Combo combo) {
			try {
				Class<?> type = Class.forName ("org.eclipse.swt.internal.win32.COMBOBOXINFO");
				Object info = type.getConstructor ().newInstance ();
				type.getField ("cbSize").setInt (info, type.getField ("sizeof").getInt (null));
				os ().getMethod ("GetComboBoxInfo", long.class, type).invoke (null, handle (combo), info);
				return info;
			} catch (ReflectiveOperationException e) {
				throw new AssertionError (e);
			}
		}

		static long handle (Combo combo) {
			try {
				return Combo.class.getField ("handle").getLong (combo);
			} catch (ReflectiveOperationException e) {
				throw new AssertionError (e);
			}
		}

		/* the drop-down list box */
		static long list (Combo combo) {
			try {
				return info (combo).getClass ().getField ("hwndList").getLong (info (combo));
			} catch (ReflectiveOperationException e) {
				throw new AssertionError (e);
			}
		}

		/* the window with the keyboard focus: the combo when it is read-only, its edit field otherwise */
		static long focus (Combo combo) {
			if ((combo.getStyle () & SWT.READ_ONLY) != 0) return handle (combo);
			try {
				return info (combo).getClass ().getField ("hwndItem").getLong (info (combo));
			} catch (ReflectiveOperationException e) {
				throw new AssertionError (e);
			}
		}

		static long send (long hwnd, int message, long wParam, long lParam) {
			try {
				Method sendMessage = os ().getMethod ("SendMessage", long.class, int.class, long.class, long.class);
				return (Long) sendMessage.invoke (null, hwnd, message, wParam, lParam);
			} catch (ReflectiveOperationException e) {
				throw new AssertionError (e);
			}
		}

		static int scanCode (int vk) {
			try {
				return (Integer) os ().getMethod ("MapVirtualKey", int.class, int.class).invoke (null, vk, 0);
			} catch (ReflectiveOperationException e) {
				throw new AssertionError (e);
			}
		}

		static void key (Combo combo, String name) {
			int vk = switch (name) {
				case "UP" -> 0x26;
				case "DOWN" -> 0x28;
				case "PRIOR" -> 0x21;
				case "NEXT" -> 0x22;
				case "HOME" -> 0x24;
				case "END" -> 0x23;
				case "LEFT" -> 0x25;
				case "RIGHT" -> 0x27;
				case "F4" -> 0x73;
				case "ESCAPE" -> 0x1B;
				default -> 0x0D;
			};
			boolean extended = vk >= 0x21 && vk <= 0x28;
			long lParam = 1 | ((long) scanCode (vk) << 16) | (extended ? 1L << 24 : 0);
			long hwnd = focus (combo);
			send (hwnd, WM_KEYDOWN, vk, lParam);
			if (vk == 0x1B || vk == 0x0D) send (hwnd, WM_CHAR, vk, lParam);
			send (hwnd, WM_KEYUP, vk, lParam | 0xC0000000L);
		}

		static void altDown (Combo combo) {
			long lParam = 1 | ((long) scanCode (0x28) << 16) | (1L << 24) | (1L << 29);
			long hwnd = focus (combo);
			send (hwnd, WM_SYSKEYDOWN, 0x28, lParam);
			send (hwnd, WM_SYSKEYUP, 0x28, lParam | 0xC0000000L);
		}

		static void character (Combo combo, char character) {
			send (focus (combo), WM_CHAR, character, 1);
		}

		/* the pointer over a row of the dropped list, then (click) a press and release there */
		static void mouse (Combo combo, int row, boolean click) {
			long list = list (combo);
			int height = (int) send (list, LB_GETITEMHEIGHT, 0, 0);
			long point = ViewportDifferential.makeLParam (40, row * height + height / 2);
			send (list, WM_MOUSEMOVE, 0, point);
			if (click) {
				send (list, WM_LBUTTONDOWN, 1, point);
				send (list, WM_LBUTTONUP, 0, point);
			}
		}

		/* range, page and position of the dropped list's vertical bar */
		static String bar (long hwnd) {
			try {
				Class<?> type = Class.forName ("org.eclipse.swt.internal.win32.SCROLLINFO");
				Object info = type.getConstructor ().newInstance ();
				type.getField ("cbSize").setInt (info, type.getField ("sizeof").getInt (null));
				type.getField ("fMask").setInt (info, 0x17);
				os ().getMethod ("GetScrollInfo", long.class, int.class, type).invoke (null, hwnd, 1, info);
				return type.getField ("nMin").getInt (info) + ".." + type.getField ("nMax").getInt (info) + "/" + type.getField ("nPage").getInt (info) + "@"
					+ type.getField ("nPos").getInt (info);
			} catch (ReflectiveOperationException e) {
				throw new AssertionError (e);
			}
		}

		/* WM_PRINT of a window (the dropped list) with its frame and bar */
		static ImageData print (Combo combo, long hwnd) {
			try {
				Class<?> rectType = Class.forName ("org.eclipse.swt.internal.win32.RECT");
				Object rect = rectType.getConstructor ().newInstance ();
				os ().getMethod ("GetWindowRect", long.class, rectType).invoke (null, hwnd, rect);
				int width = rectType.getField ("right").getInt (rect) - rectType.getField ("left").getInt (rect);
				int height = rectType.getField ("bottom").getInt (rect) - rectType.getField ("top").getInt (rect);
				Image image = new Image (combo.getDisplay (), Math.max (1, width), Math.max (1, height));
				GC gc = new GC (image);
				try {
					send (hwnd, WM_PRINT, GC.class.getField ("handle").getLong (gc), 0x2 | 0x4 | 0x8 | 0x10);
				} finally {
					gc.dispose ();
				}
				ImageData data = image.getImageData ();
				image.dispose ();
				return data;
			} catch (ReflectiveOperationException e) {
				throw new AssertionError (e);
			}
		}
	}
}
