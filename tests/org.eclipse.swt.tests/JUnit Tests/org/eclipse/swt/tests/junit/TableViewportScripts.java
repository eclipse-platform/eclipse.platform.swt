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

import java.lang.invoke.MethodHandle;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Optional;
import java.util.Random;

import org.eclipse.swt.SWT;
import org.eclipse.swt.accessibility.Accessible;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.C;
import org.eclipse.swt.internal.ole.win32.COM;
import org.eclipse.swt.internal.ole.win32.IUnknown;
import org.eclipse.swt.internal.ole.win32.VARIANT;
import org.eclipse.swt.internal.win32.*;
import org.eclipse.swt.tests.junit.Test_org_eclipse_swt_widgets_Table_ViewportDifferential.Scene;
import org.eclipse.swt.widgets.*;

/**
 * Scripts of the Table differential beyond the API calls of {@link Test_org_eclipse_swt_widgets_Table_ViewportDifferential}
 * (docs/m3/TABLE_MEDIATED_VIEWPORT.md, T-Table-7 to T-Table-10). Everything random is drawn before an operation runs.
 * <ul>
 * <li>INPUT: keys and clicks as the window messages Windows delivers, with Shift and Ctrl in the thread's keyboard
 * state (SetKeyboardState through java.lang.foreign, reached by reflection; the JVM runs with --enable-preview on JDK
 * 21). The button-up is posted before the button-down is sent: the ListView detects drags in a modal loop that reads
 * the queue. After a click the ListView runs a timer, so a click settles before the next operation; type-ahead
 * characters settle past the type-ahead timeout. Accessibility: focus, states and names through the Table's
 * Accessible.</li>
 * <li>GEOMETRY: fonts, colours and images per row and cell, getItem (Point), item bounds, label-tip text
 * (LVM_GETITEMTEXT, the request the native label tip makes) and widths, resizes while scrolled, pixels.</li>
 * <li>REENTRANT: one structure change inside the next Selection, DefaultSelection, MeasureItem or SetData event.</li>
 * <li>DPI: WM_DPICHANGED to the shell (runtime rescaling), scrolling and resizes; the zoom returns to 100 % after
 * every seed.</li>
 * </ul>
 */
final class TableViewportScripts {
	static final int WM_KEYDOWN = 0x100, WM_KEYUP = 0x101, WM_CHAR = 0x102, WM_MOUSEMOVE = 0x200, WM_LBUTTONDOWN = 0x201,
			WM_LBUTTONUP = 0x202, WM_LBUTTONDBLCLK = 0x203, MK_LBUTTON = 1, MK_SHIFT = 4, MK_CONTROL = 8, LVM_GETITEMTEXT = 0x1073;
	static final String [] KEYS = {"UP", "DOWN", "PRIOR", "NEXT", "HOME", "END", "SPACE"};
	static final String [] MODIFIERS = {"", "shift", "ctrl", "ctrl shift"};
	/* API operations of the main script that the other scripts interleave; no range removal (the R4_38 keyed-store defect) */
	static final int [] STRUCTURE = {0, 1, 2, 4, 5, 6, 7, 8, 12, 14, 15, 17, 18, 19, 22, 23};

	/** Per-seed state of a scene. */
	static final class State {
		Image [] images = new Image [0];
		Font [] fonts = new Font [0];
		/* the event type that runs the armed action once, the action, and its row as a fraction */
		int armedType, armedAction, armedFraction;
		String armedResult;
		Listener measure;
		int dpi = 96;
	}

	private TableViewportScripts () {
	}

	static ViewportDifferential.Op<Scene> next (int script, Random random, StringBuilder log) {
		int kind = random.nextInt (16), fraction = random.nextInt (1 << 20), pick = random.nextInt (1 << 16), span = random.nextInt (6);
		int delta = (random.nextBoolean () ? 1 : -1) * random.nextInt (40);
		log.append ("script ").append (script).append (" op ").append (kind).append (" at ").append (fraction).append (" pick ").append (pick)
				.append (" span ").append (span).append (" delta ").append (delta).append ('\n');
		return Test_org_eclipse_swt_widgets_Table_ViewportDifferential.guarded (scene -> switch (script) {
			case Test_org_eclipse_swt_widgets_Table_ViewportDifferential.INPUT -> input (scene, kind, fraction, pick, span, delta);
			case Test_org_eclipse_swt_widgets_Table_ViewportDifferential.GEOMETRY -> geometry (scene, kind, fraction, pick, span, delta);
			case Test_org_eclipse_swt_widgets_Table_ViewportDifferential.REENTRANT -> reentrant (scene, kind, fraction, pick, span, delta);
			default -> dpi (scene, kind, fraction, pick, span, delta);
		});
	}

	/* one API operation of the main script */
	static String structure (Scene scene, int fraction, int pick, int span, int delta) {
		int kind = STRUCTURE [pick % STRUCTURE.length];
		return Test_org_eclipse_swt_widgets_Table_ViewportDifferential.apply (scene, kind, fraction, span, delta, false);
	}

	static int row (Table table, int fraction) {
		int count = table.getItemCount ();
		return count == 0 ? 0 : (int) ((long) fraction * count >>> 20);
	}

	/* ---- INPUT ---- */

	static String input (Scene scene, int kind, int fraction, int pick, int span, int delta) {
		Table table = scene.table;
		String modifiers = MODIFIERS [(pick >> 3) % 4];
		if (kind <= 4) {
			String key = KEYS [pick % KEYS.length];
			key (scene, vk (key), modifiers.contains ("shift"), modifiers.contains ("ctrl"));
			ViewportDifferential.settle (scene.display, 30);
			return "key " + key + " " + modifiers;
		}
		if (kind <= 8) {
			int visible = Math.max (1, table.getClientArea ().height / Math.max (1, table.getItemHeight ()));
			int offset = fraction % (visible + 2), where = (pick >> 5) % 3;
			boolean twice = pick % 13 == 0;
			String answer = click (scene, offset, where, modifiers.contains ("shift"), modifiers.contains ("ctrl"), twice);
			return "click " + offset + " " + new String [] {"box", "text", "cell"} [where] + " " + modifiers + (twice ? " twice" : "") + answer;
		}
		if (kind == 9) {
			if (pick % 4 == 1) return marquee (scene, fraction, pick, modifiers.contains ("ctrl"));
			if (pick % 4 != 0) return accessibility (scene, fraction);
			char character = "rcnax".charAt ((pick >> 2) % 5);
			ViewportDifferential.send (table, WM_CHAR, character, 1);
			/* past the type-ahead timeout, so the next character starts a new search in every run */
			ViewportDifferential.settle (scene.display, 1200);
			return "char " + character;
		}
		if (kind == 10) return accessibility (scene, fraction);
		return structure (scene, fraction, pick, span, delta);
	}

	static int vk (String name) {
		return switch (name) {
			case "UP" -> 0x26;
			case "DOWN" -> 0x28;
			case "PRIOR" -> 0x21;
			case "NEXT" -> 0x22;
			case "HOME" -> 0x24;
			case "END" -> 0x23;
			case "RETURN" -> 0x0D;
			default -> 0x20;
		};
	}

	static void key (Scene scene, int vk, boolean shift, boolean ctrl) {
		Table table = scene.table;
		boolean extended = vk != 0x20 && vk != 0x0D;
		long lParam = 1 | ((long) OS.MapVirtualKey (vk, 0) << 16) | (extended ? 1L << 24 : 0);
		modifiers (shift, ctrl);
		try {
			ViewportDifferential.send (table, WM_KEYDOWN, vk, lParam);
			if (vk == 0x20) ViewportDifferential.send (table, WM_CHAR, ' ', lParam);
			if (vk == 0x0D && !table.isDisposed ()) ViewportDifferential.send (table, WM_CHAR, '\r', lParam);
			if (!table.isDisposed ()) ViewportDifferential.send (table, WM_KEYUP, vk, lParam | 0xC0000000L);
		} finally {
			modifiers (false, false);
		}
	}

	/*
	 * A click on the visible row at offset (rows past the last one are empty space): on the check box, the text or
	 * the second column. The button-up is posted first, so the ListView's drag-detection loop finds it.
	 */
	static String click (Scene scene, int offset, int where, boolean shift, boolean ctrl, boolean twice) {
		Table table = scene.table;
		int count = table.getItemCount ();
		if (count == 0) return " (empty)";
		int top = table.getTopIndex (), row = Math.min (top + offset, count - 1);
		RECT rect = new RECT ();
		rect.left = OS.LVIR_BOUNDS;
		OS.SendMessage (table.handle, OS.LVM_GETITEMRECT, row, rect);
		int height = rect.bottom - rect.top, y = rect.top + (top + offset - row) * height + height / 2;
		int x = where == 0 ? rect.left + 8 : where == 1 ? 60 : 180;
		long point = OS.MAKELPARAM (x, y);
		int keys = (shift ? MK_SHIFT : 0) | (ctrl ? MK_CONTROL : 0);
		modifiers (shift, ctrl);
		try {
			ViewportDifferential.send (table, WM_MOUSEMOVE, keys, point);
			for (int i = 0; i < (twice ? 2 : 1) && !table.isDisposed (); i++) {
				OS.PostMessage (table.handle, WM_LBUTTONUP, keys, point);
				ViewportDifferential.send (table, i == 0 ? WM_LBUTTONDOWN : WM_LBUTTONDBLCLK, MK_LBUTTON | keys, point);
				ViewportDifferential.drain (scene.display);
			}
		} finally {
			modifiers (false, false);
		}
		/* the ListView's click timer and the posted mouse-leave run before the next operation */
		ViewportDifferential.settle (scene.display, 150);
		return " at " + x + "," + y;
	}

	/*
	 * A marquee: the button goes down in empty space right of the rows' text and moves over a few rows. The moves and
	 * the button-up are posted first, so the ListView's marquee loop reads them from the queue.
	 */
	static String marquee (Scene scene, int fraction, int pick, boolean ctrl) {
		Table table = scene.table;
		if (table.getItemCount () == 0) return "marquee: empty";
		int visible = Math.max (1, table.getClientArea ().height / Math.max (1, table.getItemHeight ()));
		int top = table.getTopIndex (), from = Math.min (fraction % visible, table.getItemCount () - 1 - top), rows = 1 + pick % 4;
		RECT rect = new RECT ();
		rect.left = OS.LVIR_BOUNDS;
		OS.SendMessage (table.handle, OS.LVM_GETITEMRECT, top + Math.max (0, from), rect);
		int height = rect.bottom - rect.top, x = Math.min (table.getClientArea ().width - 6, 290), y = (rect.top + rect.bottom) / 2;
		int keys = ctrl ? MK_CONTROL : 0;
		modifiers (false, ctrl);
		try {
			for (int i = 1; i <= rows; i++) OS.PostMessage (table.handle, WM_MOUSEMOVE, MK_LBUTTON | keys, OS.MAKELPARAM (x - 4 * i, y + i * height));
			OS.PostMessage (table.handle, WM_LBUTTONUP, keys, OS.MAKELPARAM (x - 4 * rows, y + rows * height));
			ViewportDifferential.send (table, WM_LBUTTONDOWN, MK_LBUTTON | keys, OS.MAKELPARAM (x, y));
			ViewportDifferential.drain (scene.display);
		} finally {
			modifiers (false, false);
		}
		ViewportDifferential.settle (scene.display, 150);
		return "marquee " + from + " +" + rows + (ctrl ? " ctrl" : "") + " at " + x;
	}

	private static MethodHandle setKeyboardState;
	private static Object keyState, byteLayout;
	private static Method setByte;

	/* SetKeyboardState (user32) through java.lang.foreign, reached by reflection: the tests compile without preview features */
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
			for (int vk : new int [] {0x10, 0xA0}) setByte.invoke (keyState, byteLayout, (long) vk, shift ? (byte) 0x80 : (byte) 0);
			for (int vk : new int [] {0x11, 0xA2}) setByte.invoke (keyState, byteLayout, (long) vk, ctrl ? (byte) 0x80 : (byte) 0);
			setKeyboardState.invokeWithArguments (keyState);
		} catch (Throwable e) {
			throw new AssertionError ("SetKeyboardState is not reachable (run the JVM with --enable-preview on JDK 21)", e);
		}
	}

	/* ---- accessibility: the Table's Accessible, as a screen reader reaches it ---- */

	private static Method accFocus, accState, accName, accChildCount;

	static String accessibility (Scene scene, int fraction) {
		Table table = scene.table;
		try {
			if (accFocus == null) {
				accFocus = Accessible.class.getDeclaredMethod ("get_accFocus", long.class);
				accState = Accessible.class.getDeclaredMethod ("get_accState", long.class, long.class);
				accName = Accessible.class.getDeclaredMethod ("get_accName", long.class, long.class);
				accChildCount = Accessible.class.getDeclaredMethod ("get_accChildCount", long.class);
				for (Method method : new Method [] {accFocus, accState, accName, accChildCount}) method.setAccessible (true);
			}
			Accessible accessible = table.getAccessible ();
			long heap = OS.GetProcessHeap ();
			long out = OS.HeapAlloc (heap, OS.HEAP_ZERO_MEMORY, VARIANT.sizeof), in = OS.HeapAlloc (heap, OS.HEAP_ZERO_MEMORY, VARIANT.sizeof);
			try {
				StringBuilder answer = new StringBuilder ("acc children ");
				accChildCount.invoke (accessible, out);
				int [] value = new int [1];
				C.memmove (value, out, 4);
				answer.append (value [0]);
				OS.HeapFree (heap, 0, out);
				out = OS.HeapAlloc (heap, OS.HEAP_ZERO_MEMORY, VARIANT.sizeof);
				int code = (Integer) accFocus.invoke (accessible, out);
				answer.append (" focus ").append (code).append (':').append (variant (out));
				int [] rows = {row (table, fraction), Test_org_eclipse_swt_widgets_Table_ViewportDifferential.focusIndex (table), table.getSelectionIndex ()};
				for (int row : rows) {
					if (row < 0 || row >= table.getItemCount ()) continue;
					C.memmove (in, new short [] {COM.VT_I4}, 2);
					C.memmove (in + 8, new int [] {row + 1}, 4);
					OS.HeapFree (heap, 0, out);
					out = OS.HeapAlloc (heap, OS.HEAP_ZERO_MEMORY, VARIANT.sizeof);
					accState.invoke (accessible, in, out);
					C.memmove (value, out + 8, 4);
					answer.append (" | ").append (row).append (" state ").append (Integer.toHexString (value [0]));
					long [] bstr = new long [1];
					long name = OS.HeapAlloc (heap, OS.HEAP_ZERO_MEMORY, C.PTR_SIZEOF);
					accName.invoke (accessible, in, name);
					C.memmove (bstr, name, C.PTR_SIZEOF);
					OS.HeapFree (heap, 0, name);
					if (bstr [0] != 0) {
						char [] chars = new char [COM.SysStringLen (bstr [0])];
						C.memmove (chars, bstr [0], chars.length * 2L);
						COM.SysFreeString (bstr [0]);
						answer.append (" name ").append (chars);
					}
				}
				return answer.toString ();
			} finally {
				OS.HeapFree (heap, 0, out);
				OS.HeapFree (heap, 0, in);
			}
		} catch (ReflectiveOperationException e) {
			throw new IllegalStateException (e);
		}
	}

	/* a VARIANT answer: a child (row) or the control, nothing, or an object (released) */
	static String variant (long pointer) {
		short [] vt = new short [1];
		C.memmove (vt, pointer, 2);
		if (vt [0] == COM.VT_I4) {
			int [] child = new int [1];
			C.memmove (child, pointer + 8, 4);
			return child [0] == COM.CHILDID_SELF ? "self" : "row " + (child [0] - 1);
		}
		if (vt [0] == COM.VT_DISPATCH || vt [0] == COM.VT_UNKNOWN) {
			long [] object = new long [1];
			C.memmove (object, pointer + 8, C.PTR_SIZEOF);
			if (object [0] != 0) new IUnknown (object [0]).Release ();
			return "object";
		}
		return "vt " + vt [0];
	}

	/* ---- GEOMETRY ---- */

	static String geometry (Scene scene, int kind, int fraction, int pick, int span, int delta) {
		Table table = scene.table;
		State state = scene.state;
		int count = table.getItemCount (), k = row (table, fraction), columns = Math.max (1, table.getColumnCount ());
		int column = pick % columns;
		boolean cell = (pick & 0x100) != 0;
		Display display = scene.display;
		switch (kind) {
			case 0, 1: {
				Font font = state.fonts.length == 0 || pick % 3 == 2 ? null : state.fonts [pick % 3];
				if (count == 0) return "font: empty";
				TableItem item = table.getItem (k);
				if (cell) item.setFont (column, font); else item.setFont (font);
				return "font " + k + (cell ? " cell " + column : "") + " " + (font == null ? "default" : pick % 3) + " -> height " + table.getItemHeight ();
			}
			case 2, 3: {
				int [] colors = {SWT.COLOR_RED, SWT.COLOR_BLUE, SWT.COLOR_DARK_GREEN, SWT.COLOR_YELLOW};
				Color color = pick % 5 == 4 ? null : display.getSystemColor (colors [pick % 4]);
				if (count == 0) return "color: empty";
				TableItem item = table.getItem (k);
				if (kind == 2) {
					if (cell) item.setBackground (column, color); else item.setBackground (color);
				} else {
					if (cell) item.setForeground (column, color); else item.setForeground (color);
				}
				return (kind == 2 ? "background " : "foreground ") + k + (cell ? " cell " + column : "") + " " + (color == null ? "default" : pick % 4);
			}
			case 4: {
				Image image = state.images.length == 0 || pick % 3 == 2 ? null : state.images [pick % 3];
				if (count == 0) return "image: empty";
				TableItem item = table.getItem (k);
				if (cell) item.setImage (column, image); else item.setImage (image);
				return "image " + k + (cell ? " cell " + column : "") + " " + (image == null ? "none" : pick % 3) + " -> height " + table.getItemHeight ();
			}
			case 5: {
				int [] xs = {10, 60, 150, 250, 310};
				int x = xs [pick % xs.length], y = fraction % (table.getClientArea ().height + 30) - 10;
				TableItem item = table.getItem (new Point (x, y));
				return "itemAt " + x + "," + y + " -> " + (item == null ? "null" : table.indexOf (item) + " " + item.getText ());
			}
			case 6: {
				if (count == 0) return "bounds: empty";
				TableItem item = table.getItem (k);
				StringBuilder answer = new StringBuilder ("bounds " + k + " " + item.getBounds ());
				int limit = table.getColumnCount () == 0 ? 2 : table.getColumnCount () + 1;
				for (int c = 0; c < limit; c++) {
					answer.append (" | ").append (c).append (' ').append (item.getBounds (c)).append (" text ").append (item.getTextBounds (c))
							.append (" image ").append (item.getImageBounds (c));
				}
				return answer.toString ();
			}
			case 7:
				if (pick % 3 != 0) return "itemHeight " + table.getItemHeight () + " header " + table.getHeaderHeight () + " client " + table.getClientArea ();
				return pixels (scene);
			case 8: {
				int [] widths = {200, 300, 380}, heights = {150, 380, 260};
				table.setTopIndex (k);
				table.setBounds (5, 5, widths [pick % 3], heights [(pick >> 2) % 3]);
				return "scrolled to " + k + ", resized " + widths [pick % 3] + "x" + heights [(pick >> 2) % 3] + " -> top " + table.getTopIndex ();
			}
			case 9: {
				if (table.getColumnCount () == 0) {
					table.setTopIndex (k);
					return "setTopIndex " + k + " -> top " + table.getTopIndex ();
				}
				TableColumn target = table.getColumn (column);
				if (pick % 3 == 0) {
					target.pack ();
					return "pack column " + column + " -> width " + target.getWidth ();
				}
				target.setWidth (30 + pick % 150);
				return "column " + column + " width " + target.getWidth ();
			}
			case 10:
				return labelTip (table, k, column);
			case 11:
				if ((table.getStyle () & SWT.CHECK) != 0 && count > 0) {
					table.getItem (k).setGrayed ((pick & 1) != 0);
					return "setGrayed " + k + " " + ((pick & 1) != 0);
				}
				return structure (scene, fraction, pick, span, delta);
			default:
				return structure (scene, fraction, pick, span, delta);
		}
	}

	/* the text the native label tip asks for (LVM_GETITEMTEXT answers it from LVN_GETDISPINFO) and the widths it compares */
	static String labelTip (Table table, int row, int column) {
		if (table.getItemCount () == 0) return "labeltip: empty";
		long heap = OS.GetProcessHeap (), buffer = OS.HeapAlloc (heap, OS.HEAP_ZERO_MEMORY, 1024);
		try {
			LVITEM item = new LVITEM ();
			item.iSubItem = column;
			item.pszText = buffer;
			item.cchTextMax = 512;
			int length = (int) OS.SendMessage (table.handle, LVM_GETITEMTEXT, row, item);
			char [] chars = new char [Math.max (0, length)];
			C.memmove (chars, buffer, chars.length * 2L);
			String text = new String (chars);
			TCHAR string = new TCHAR (text, true);
			int width = (int) OS.SendMessage (table.handle, OS.LVM_GETSTRINGWIDTH, 0, string);
			int columnWidth = (int) OS.SendMessage (table.handle, OS.LVM_GETCOLUMNWIDTH, column, 0);
			return "labeltip " + row + " " + column + " '" + text + "' width " + width + " of " + columnWidth;
		} finally {
			OS.HeapFree (heap, 0, buffer);
		}
	}

	static String pixels (Scene scene) {
		String hash = ViewportDifferential.hash (scene.pixels ());
		scene.table.setFocus ();
		ViewportDifferential.settle (scene.display, 30);
		return "pixels " + hash;
	}

	/* ---- REENTRANT ---- */

	static final String [] ACTIONS = {"remove", "dispose item", "insert", "shrink count", "setText", "setTopIndex", "setSelection", "clear"};

	static String reentrant (Scene scene, int kind, int fraction, int pick, int span, int delta) {
		Table table = scene.table;
		State state = scene.state;
		boolean virtual = (table.getStyle () & SWT.VIRTUAL) != 0;
		if (kind >= 8) return structure (scene, fraction, pick, span, delta);
		state.armedAction = (pick >> 4) % ACTIONS.length;
		state.armedFraction = fraction;
		state.armedResult = "not run";
		String trigger;
		switch (kind) {
			case 0, 1: {
				state.armedType = SWT.Selection;
				String key = (pick & 1) == 0 ? "DOWN" : "UP";
				key (scene, vk (key), false, false);
				ViewportDifferential.settle (scene.display, 30);
				trigger = "key " + key;
				break;
			}
			case 2, 3: {
				state.armedType = SWT.Selection;
				int visible = Math.max (1, table.getClientArea ().height / Math.max (1, table.getItemHeight ()));
				trigger = "click " + (fraction % visible) + click (scene, fraction % visible, 1, false, false, false);
				break;
			}
			case 4, 5: {
				state.armedType = SWT.MeasureItem;
				state.measure = event -> inside (scene, event);
				table.addListener (SWT.MeasureItem, state.measure);
				try {
					if (table.getColumnCount () > 0 && (pick & 1) == 0) {
						table.getColumn (0).pack ();
						trigger = "pack";
					} else {
						table.redraw ();
						table.update ();
						trigger = "paint";
					}
				} finally {
					if (!table.isDisposed ()) table.removeListener (SWT.MeasureItem, state.measure);
					state.measure = null;
				}
				break;
			}
			case 6: {
				state.armedType = SWT.DefaultSelection;
				key (scene, vk ("RETURN"), false, false);
				ViewportDifferential.settle (scene.display, 30);
				trigger = "key RETURN";
				break;
			}
			default: {
				state.armedType = SWT.SetData;
				int k = row (table, fraction);
				if (virtual && table.getItemCount () > 0) {
					table.getItem (k).getText ();
					trigger = "getText " + k;
				} else {
					table.setTopIndex (k);
					table.update ();
					trigger = "setTopIndex " + k;
				}
				break;
			}
		}
		state.armedType = 0;
		return trigger + " inside " + eventName (kind) + " " + ACTIONS [state.armedAction] + " -> " + state.armedResult;
	}

	static String eventName (int kind) {
		return kind <= 3 ? "Selection" : kind <= 5 ? "MeasureItem" : kind == 6 ? "DefaultSelection" : "SetData";
	}

	/* runs the armed action once, inside the first event of the armed type */
	static void inside (Scene scene, Event event) {
		State state = scene.state;
		if (state.armedType == 0 || event.type != state.armedType) return;
		state.armedType = 0;
		Table table = scene.table;
		try {
			int count = table.getItemCount (), r = row (table, state.armedFraction);
			switch (state.armedAction) {
				case 0 -> {
					if (count > 0) table.remove (r);
				}
				case 1 -> {
					if (event.item != null && !event.item.isDisposed ()) event.item.dispose ();
				}
				case 2 -> new TableItem (table, SWT.NONE, 0).setText ("inner " + scene.step);
				case 3 -> table.setItemCount (Math.max (0, count - 2));
				case 4 -> {
					if (count > 0) table.getItem (r).setText ("inner text " + scene.step);
				}
				case 5 -> table.setTopIndex (r);
				case 6 -> table.setSelection (r);
				default -> {
					if (count > 0) table.clear (r);
				}
			}
			state.armedResult = "ok rows " + table.getItemCount ();
		} catch (RuntimeException e) {
			state.armedResult = "error " + e.getClass ().getName () + " " + e.getMessage ();
		}
	}

	/* ---- DPI ---- */

	static final int [] DPIS = {96, 120, 144, 192};

	static String dpi (Scene scene, int kind, int fraction, int pick, int span, int delta) {
		Table table = scene.table;
		int k = row (table, fraction);
		switch (kind) {
			case 0, 1: {
				int dpi = DPIS [pick % DPIS.length];
				changeDpi (scene, dpi);
				return "dpi " + dpi + " -> itemHeight " + table.getItemHeight () + " header " + table.getHeaderHeight () + " top " + table.getTopIndex ();
			}
			case 2, 3:
				table.setTopIndex (k);
				return "setTopIndex " + k + " -> top " + table.getTopIndex ();
			case 4: {
				int [] widths = {200, 300, 380}, heights = {150, 380, 260};
				table.setBounds (5, 5, widths [pick % 3], heights [(pick >> 2) % 3]);
				return "resized " + widths [pick % 3] + "x" + heights [(pick >> 2) % 3] + " -> top " + table.getTopIndex ();
			}
			case 5: {
				if (table.getItemCount () == 0) return "bounds: empty";
				TableItem item = table.getItem (k);
				return "bounds " + k + " " + item.getBounds () + " " + item.getBounds (1) + " itemHeight " + table.getItemHeight ();
			}
			case 6:
				return pick % 3 == 0 ? pixels (scene) : "client " + table.getClientArea ();
			default:
				return structure (scene, fraction, pick, span, delta);
		}
	}

	/* WM_DPICHANGED as Windows sends it when the shell moves to a monitor with another scale */
	static void changeDpi (Scene scene, int dpi) {
		Shell shell = scene.shell;
		RECT rect = new RECT ();
		OS.GetWindowRect (shell.handle, rect);
		long heap = OS.GetProcessHeap (), pointer = OS.HeapAlloc (heap, OS.HEAP_ZERO_MEMORY, RECT.sizeof);
		try {
			OS.MoveMemory (pointer, rect, RECT.sizeof);
			OS.SendMessage (shell.handle, OS.WM_DPICHANGED, OS.MAKEWPARAM (dpi, dpi), pointer);
		} finally {
			OS.HeapFree (heap, 0, pointer);
		}
		scene.state.dpi = dpi;
		ViewportDifferential.settle (scene.display, 400);
	}

	/* ---- scene life cycle ---- */

	static void setUp (Scene scene) {
		State state = scene.state;
		state.armedType = 0;
		state.dpi = 96;
		if (scene.variant.script () != Test_org_eclipse_swt_widgets_Table_ViewportDifferential.GEOMETRY) return;
		Display display = scene.display;
		state.fonts = new Font [] {new Font (display, "Segoe UI", 12, SWT.BOLD), new Font (display, "Courier New", 8, SWT.ITALIC)};
		state.images = new Image [] {image (display, 16, 16, SWT.COLOR_RED), image (display, 10, 24, SWT.COLOR_BLUE)};
	}

	static Image image (Display display, int width, int height, int color) {
		Image image = new Image (display, width, height);
		GC gc = new GC (image);
		try {
			gc.setBackground (display.getSystemColor (color));
			gc.fillRectangle (0, 0, width, height);
			gc.setForeground (display.getSystemColor (SWT.COLOR_BLACK));
			gc.drawRectangle (0, 0, width - 1, height - 1);
		} finally {
			gc.dispose ();
		}
		return image;
	}

	/* before the shell is disposed: the zoom returns to 100 % */
	static void tearDown (Scene scene) {
		if (scene.state.dpi != 96 && scene.shell != null && !scene.shell.isDisposed ()) changeDpi (scene, 96);
	}

	/* after the shell is disposed */
	static void release (Scene scene) {
		State state = scene.state;
		Arrays.stream (state.images).forEach (Image::dispose);
		Arrays.stream (state.fonts).forEach (Font::dispose);
		state.images = new Image [0];
		state.fonts = new Font [0];
	}
}
