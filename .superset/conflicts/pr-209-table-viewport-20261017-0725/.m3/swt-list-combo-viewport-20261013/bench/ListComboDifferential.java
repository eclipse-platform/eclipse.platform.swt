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
import java.io.*;
import java.lang.reflect.*;
import java.nio.charset.*;
import java.util.*;

import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.win32.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.widgets.List;

/**
 * Differential trace of List, Combo and CCombo. The same seeded call sequences
 * run against one SWT build per JVM; every call prints its result (or the
 * exception) and a state snapshot. Two traces (unmodified build, viewport
 * build) are compared with diff. Styles carry SWT.VIRTUAL in both runs: the
 * unmodified build ignores the bit, the viewport build switches to the
 * owner-data list box, so equal traces mean equal API results.
 *
 * usage: ListComboDifferential <out.trace> [rebuildThreshold]
 */
public class ListComboDifferential {
	static final String [] WORDS = {"", "a", "A", "apple", "Banana", "berry", "blue", "cherry", "bz", "a\0b", "  pad", "x",
		"אבג", "الع", "long item text that is wider than the others by far", "Zed", "zz", "b"};
	static final int [] LIST_STYLES = {SWT.SINGLE, SWT.MULTI, SWT.MULTI | SWT.SIMPLE, SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL,
		SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER};
	static PrintWriter out;
	static Display display;
	static Shell shell;
	static int events;
	static String scenario;
	static int step;

	public static void main (String [] args) throws Exception {
		out = new PrintWriter (new OutputStreamWriter (new FileOutputStream (args [0]), StandardCharsets.UTF_8));
		if (args.length > 1) setThreshold (Integer.parseInt (args [1]));
		display = new Display ();
		shell = new Shell (display);
		shell.setLayout (null);
		shell.setBounds (40, 40, 420, 360);
		shell.open ();
		flush ();
		Font [] fonts = {null, new Font (display, "Segoe UI", 14, SWT.NORMAL), new Font (display, "Consolas", 8, SWT.BOLD)};
		for (int style : LIST_STYLES) {
			for (int seed = 1; seed <= 12; seed++) {
				randomList (style | SWT.VIRTUAL, seed, 120, fonts);
			}
			scriptedList (style | SWT.VIRTUAL);
			keyboardList (style | SWT.VIRTUAL);
		}
		for (int seed = 1; seed <= 6; seed++) {
			randomCCombo (SWT.VIRTUAL | (seed % 2 == 0 ? SWT.READ_ONLY : SWT.BORDER), seed, 80);
			randomCombo (seed % 2 == 0 ? SWT.READ_ONLY : SWT.DROP_DOWN, seed, 60);
		}
		for (Font font : fonts) if (font != null) font.dispose ();
		shell.dispose ();
		display.dispose ();
		out.close ();
	}

	static void setThreshold (int value) throws Exception {
		Field field = List.class.getDeclaredField ("removeRebuildThreshold");
		field.setAccessible (true);
		field.setInt (null, value);
	}

	static void flush () {
		while (display.readAndDispatch ()) {
			// drain
		}
	}

	interface Op {
		Object run ();
	}

	static void call (String name, Op op) {
		step++;
		String result;
		try {
			result = format (op.run ());
		} catch (RuntimeException | SWTError e) {
			result = "!" + e.getClass ().getSimpleName () + ":" + e.getMessage ();
		}
		out.println (scenario + "\t" + step + "\t" + name + "\t" + result);
		if (step % 64 == 0) out.flush ();
	}

	static String format (Object value) {
		if (value == null) return "-";
		if (value instanceof int []) return Arrays.toString ((int []) value);
		if (value instanceof String []) return digest ((String []) value);
		if (value instanceof String) return "\"" + escape ((String) value) + "\"";
		return String.valueOf (value);
	}

	static String digest (String [] items) {
		StringBuilder builder = new StringBuilder ("n=" + items.length + "[");
		for (int i = 0; i < items.length; i++) {
			if (i == 12) { builder.append ("..h=").append (Arrays.hashCode (items)); break; }
			if (i > 0) builder.append (',');
			builder.append (escape (items [i]));
		}
		return builder.append (']').toString ();
	}

	static String escape (String s) {
		return s.replace ("\0", "\\0").replace ("\t", "\\t");
	}

	static void snapshotList (List list) {
		call ("state.count", list::getItemCount);
		call ("state.items", list::getItems);
		call ("state.selectionIndices", list::getSelectionIndices);
		call ("state.selectionIndex", list::getSelectionIndex);
		call ("state.selectionCount", list::getSelectionCount);
		call ("state.focus", list::getFocusIndex);
		call ("state.top", list::getTopIndex);
		call ("state.anchor", () -> OS.SendMessage (list.handle, 0x19D, 0, 0));
		call ("state.itemHeight", list::getItemHeight);
		call ("state.computeSize", () -> list.computeSize (SWT.DEFAULT, SWT.DEFAULT));
		/* SWT.Selection from native input depends on keyboard focus, which a background JVM cannot hold;
		   events are proven by bench/NotifyProbe.java with focus, not by this trace */
		call ("state.eventsNonGate", () -> events);
	}

	static String word (Random random) {
		return WORDS [random.nextInt (WORDS.length)];
	}

	static int [] indices (Random random, int count) {
		int [] result = new int [random.nextInt (5)];
		for (int i = 0; i < result.length; i++) result [i] = random.nextInt (count + 4) - 2;
		return result;
	}

	static List newList (int style) {
		List list = new List (shell, style);
		list.setBounds (10, 10, 220, 150);
		list.addListener (SWT.Selection, e -> events++);
		list.addListener (SWT.DefaultSelection, e -> events += 1000);
		return list;
	}

	static void randomList (int style, int seed, int steps, Font [] fonts) {
		scenario = "list.random.s" + Integer.toHexString (style) + ".seed" + seed;
		step = 0;
		events = 0;
		Random random = new Random (seed * 31L + style);
		List list = newList (style);
		call ("getStyle", list::getStyle);
		for (int s = 0; s < steps; s++) {
			int count = list.getItemCount ();
			int a = random.nextInt (count + 4) - 2, b = random.nextInt (count + 4) - 2;
			String w = word (random);
			switch (random.nextInt (34)) {
				case 0: call ("add(String)", () -> { list.add (w); return null; }); break;
				case 1: call ("add(String,int)", () -> { list.add (w, a); return null; }); break;
				case 2: call ("setItem", () -> { list.setItem (a, w); return null; }); break;
				case 3: {
					String [] all = new String [random.nextInt (40)];
					for (int i = 0; i < all.length; i++) all [i] = word (random);
					call ("setItems", () -> { list.setItems (all); return null; });
					break;
				}
				case 4: call ("remove(int)", () -> { list.remove (a); return null; }); break;
				case 5: call ("remove(int,int)", () -> { list.remove (Math.min (a, b), Math.max (a, b)); return null; }); break;
				case 6: { int [] x = indices (random, count); call ("remove(int[])", () -> { list.remove (x); return null; }); break; }
				case 7: call ("remove(String)", () -> { list.remove (w); return null; }); break;
				case 8: if (random.nextInt (4) == 0) call ("removeAll", () -> { list.removeAll (); return null; }); break;
				case 9: call ("select(int)", () -> { list.select (a); return null; }); break;
				case 10: call ("select(int,int)", () -> { list.select (a, b); return null; }); break;
				case 11: { int [] x = indices (random, count); call ("select(int[])", () -> { list.select (x); return null; }); break; }
				case 12: call ("selectAll", () -> { list.selectAll (); return null; }); break;
				case 13: call ("deselect(int)", () -> { list.deselect (a); return null; }); break;
				case 14: call ("deselect(int,int)", () -> { list.deselect (a, b); return null; }); break;
				case 15: { int [] x = indices (random, count); call ("deselect(int[])", () -> { list.deselect (x); return null; }); break; }
				case 16: call ("deselectAll", () -> { list.deselectAll (); return null; }); break;
				case 17: call ("setSelection(int)", () -> { list.setSelection (a); return null; }); break;
				case 18: call ("setSelection(int,int)", () -> { list.setSelection (a, b); return null; }); break;
				case 19: { int [] x = indices (random, count); call ("setSelection(int[])", () -> { list.setSelection (x); return null; }); break; }
				case 20: { String [] x = {w, word (random)}; call ("setSelection(String[])", () -> { list.setSelection (x); return null; }); break; }
				case 21: call ("setTopIndex", () -> { list.setTopIndex (a); return null; }); break;
				case 22: call ("showSelection", () -> { list.showSelection (); return null; }); break;
				case 23: call ("getItem", () -> list.getItem (a)); break;
				case 24: call ("indexOf(String)", () -> list.indexOf (w)); break;
				case 25: call ("indexOf(String,int)", () -> list.indexOf (w, a)); break;
				case 26: call ("isSelected", () -> list.isSelected (a)); break;
				case 27: call ("getSelection", list::getSelection); break;
				case 28: { Font f = fonts [random.nextInt (fonts.length)]; call ("setFont", () -> { list.setFont (f); return null; }); break; }
				case 29: {
					/* AUTO_TEXT_DIRECTION exposes plain-list defects (BASELINE_DEFECTS.tsv B1, B2); it has its own scripted lane */
					int [] dirs = {SWT.LEFT_TO_RIGHT, SWT.RIGHT_TO_LEFT};
					int dir = dirs [random.nextInt (dirs.length)];
					call ("setTextDirection", () -> { list.setTextDirection (dir); return list.getTextDirection (); });
					break;
				}
				case 30: call ("computeSize(hint)", () -> list.computeSize (a * 10, SWT.DEFAULT)); break;
				case 31: call ("setEnabled", () -> { list.setEnabled (b % 2 == 0); return list.isEnabled (); }); break;
				case 32: call ("getItems", list::getItems); break;
				default: {
					String [] all = new String [100 + random.nextInt (200)];
					for (int i = 0; i < all.length; i++) all [i] = word (random) + i;
					call ("setItems(large)", () -> { list.setItems (all); return null; });
					call ("selectAll", () -> { list.selectAll (); return null; });
					int lo = random.nextInt (all.length / 2), hi = lo + random.nextInt (all.length / 2);
					call ("setFocusArea", () -> { list.select (lo); list.setTopIndex (lo); return null; });
					call ("remove(int,int)", () -> { list.remove (lo, hi); return null; });
					break;
				}
			}
			flush ();
			snapshotList (list);
		}
		list.dispose ();
	}

	static void scriptedList (int style) {
		scenario = "list.script.s" + Integer.toHexString (style);
		step = 0;
		events = 0;
		List list = newList (style);
		String [] items = new String [300];
		for (int i = 0; i < items.length; i++) items [i] = "row " + i;
		call ("setItems", () -> { list.setItems (items); return null; });
		for (int focus : new int [] {0, 5, 40, 41, 150, 299}) {
			for (int [] range : new int [] [] {{40, 60}, {0, 9}, {290, 299}, {41, 41}, {100, 250}}) {
				call ("reset", () -> { list.setItems (items); list.setSelection (new int [] {focus, 45, 46, 120, 298}); list.setTopIndex (focus); return null; });
				call ("remove(int,int)", () -> { list.remove (range [0], range [1]); return null; });
				snapshotList (list);
				call ("reset", () -> { list.setItems (items); list.setSelection (new int [] {focus, 45, 46, 120, 298}); list.setTopIndex (focus); return null; });
				int [] x = new int [range [1] - range [0] + 1];
				for (int i = 0; i < x.length; i++) x [i] = range [0] + i;
				call ("remove(int[])", () -> { list.remove (x); return null; });
				snapshotList (list);
				call ("reset", () -> { list.setItems (items); list.select (focus); return null; });
				call ("add(String,int)", () -> { list.add ("new", range [0]); return null; });
				call ("setItem", () -> { list.setItem (range [1], "changed"); return null; });
				snapshotList (list);
			}
		}
		call ("autoLane.reset", () -> { list.removeAll (); for (int i = 0; i < 40; i++) list.add ((i % 3 == 0 ? "א" : "x") + i); list.setSelection (new int [] {3, 4, 30}); list.setTopIndex (10); return null; });
		snapshotList (list);
		call ("setTextDirection(AUTO)", () -> { list.setTextDirection (SWT.AUTO_TEXT_DIRECTION); return list.getTextDirection (); });
		snapshotList (list);
		call ("add(String,int)", () -> { list.add ("אnew", 2); list.add ("tail"); return null; });
		call ("setItem", () -> { list.setItem (5, "changed"); return null; });
		snapshotList (list);
		call ("setTextDirection(LTR)", () -> { list.setTextDirection (SWT.LEFT_TO_RIGHT); return list.getTextDirection (); });
		snapshotList (list);
		list.dispose ();
		defects (style);
	}

	/* Plain-list defects with SWT.AUTO_TEXT_DIRECTION (BASELINE_DEFECTS.tsv); compared separately, not part of the equality gate. */
	static void defects (int style) {
		scenario = "defect.s" + Integer.toHexString (style);
		step = 0;
		List list = newList (style);
		call ("B1 setItems after AUTO", () -> { list.setTextDirection (SWT.AUTO_TEXT_DIRECTION); list.setItems ("apple", "berry"); return null; });
		call ("B1 getItems", list::getItems);
		call ("B2 empty item then AUTO", () -> { list.setTextDirection (SWT.LEFT_TO_RIGHT); list.setItems ("", "kiwi"); list.setTextDirection (SWT.AUTO_TEXT_DIRECTION); return null; });
		call ("B2 getItem(0)", () -> list.getItem (0));
		list.dispose ();
	}

	static void keyboardList (int style) {
		scenario = "list.keys.s" + Integer.toHexString (style);
		step = 0;
		events = 0;
		List list = newList (style);
		list.setItems ("apple", "Banana", "berry", "blue", "cherry", "bz", "", "a\0b", "Apple pie", "zebra");
		list.setFocus ();
		flush ();
		for (char c : "bbBeazAcz1 ".toCharArray ()) {
			call ("WM_CHAR " + (c == ' ' ? "SPACE" : String.valueOf (c)), () -> { OS.SendMessage (list.handle, OS.WM_CHAR, c, 0); return null; });
			flush ();
			snapshotList (list);
		}
		for (int key : new int [] {OS.VK_DOWN, OS.VK_DOWN, OS.VK_END, OS.VK_HOME, OS.VK_NEXT, OS.VK_PRIOR, OS.VK_UP}) {
			call ("WM_KEYDOWN " + key, () -> { OS.SendMessage (list.handle, OS.WM_KEYDOWN, key, 0); OS.SendMessage (list.handle, OS.WM_KEYUP, key, 0); return null; });
			flush ();
			snapshotList (list);
		}
		int itemHeight = list.getItemHeight ();
		for (int row : new int [] {2, 4, 0}) {
			int y = row * itemHeight + itemHeight / 2;
			call ("click row " + row, () -> {
				long lParam = OS.MAKELPARAM (20, y);
				OS.SendMessage (list.handle, OS.WM_LBUTTONDOWN, OS.MK_LBUTTON, lParam);
				OS.SendMessage (list.handle, OS.WM_LBUTTONUP, 0, lParam);
				return null;
			});
			flush ();
			/* a click depends on mouse capture, which a background JVM cannot hold; NotifyProbe proves clicks with focus */
			String saved = scenario;
			scenario = "nongate." + saved;
			snapshotList (list);
			scenario = saved;
		}
		list.dispose ();
	}

	static void snapshotCCombo (CCombo combo) {
		call ("state.count", combo::getItemCount);
		call ("state.items", combo::getItems);
		call ("state.selectionIndex", combo::getSelectionIndex);
		call ("state.text", combo::getText);
		call ("state.selection", () -> combo.getSelection ().toString ());
		call ("state.itemHeight", combo::getItemHeight);
		call ("state.visibleItemCount", combo::getVisibleItemCount);
		call ("state.computeSize", () -> combo.computeSize (SWT.DEFAULT, SWT.DEFAULT));
		call ("state.listVisible", combo::getListVisible);
		call ("state.events", () -> events);
	}

	static void randomCCombo (int style, int seed, int steps) {
		scenario = "ccombo.random.s" + Integer.toHexString (style) + ".seed" + seed;
		step = 0;
		events = 0;
		Random random = new Random (seed * 17L + style);
		CCombo combo = new CCombo (shell, style);
		combo.setBounds (10, 200, 200, 30);
		combo.addListener (SWT.Selection, e -> events++);
		combo.addListener (SWT.Modify, e -> events += 100);
		/* the unmodified CCombo drops SWT.VIRTUAL from its style; the viewport build keeps it (additive) */
		call ("getStyle&~VIRTUAL", () -> combo.getStyle () & ~SWT.VIRTUAL);
		for (int s = 0; s < steps; s++) {
			int count = combo.getItemCount ();
			int a = random.nextInt (count + 4) - 2, b = random.nextInt (count + 4) - 2;
			String w = word (random);
			switch (random.nextInt (16)) {
				case 0: call ("add(String)", () -> { combo.add (w); return null; }); break;
				case 1: call ("add(String,int)", () -> { combo.add (w, a); return null; }); break;
				case 2: call ("setItem", () -> { combo.setItem (a, w); return null; }); break;
				case 3: {
					String [] all = new String [random.nextInt (30)];
					for (int i = 0; i < all.length; i++) all [i] = word (random) + i;
					call ("setItems", () -> { combo.setItems (all); return null; });
					break;
				}
				case 4: call ("remove(int)", () -> { combo.remove (a); return null; }); break;
				case 5: call ("remove(int,int)", () -> { combo.remove (Math.min (a, b), Math.max (a, b)); return null; }); break;
				case 6: call ("remove(String)", () -> { combo.remove (w); return null; }); break;
				case 7: if (random.nextInt (4) == 0) call ("removeAll", () -> { combo.removeAll (); return null; }); break;
				case 8: call ("select", () -> { combo.select (a); return null; }); break;
				case 9: call ("deselect", () -> { combo.deselect (a); return null; }); break;
				case 10: call ("deselectAll", () -> { combo.deselectAll (); return null; }); break;
				case 11: call ("setText", () -> { combo.setText (w); return null; }); break;
				case 12: call ("indexOf(String)", () -> combo.indexOf (w)); break;
				case 13: call ("indexOf(String,int)", () -> combo.indexOf (w, a)); break;
				case 14: call ("getItem", () -> combo.getItem (a)); break;
				default: call ("setVisibleItemCount", () -> { combo.setVisibleItemCount (Math.max (1, a)); return null; }); break;
			}
			flush ();
			snapshotCCombo (combo);
		}
		call ("setListVisible(true)", () -> { combo.setListVisible (true); return combo.getListVisible (); });
		flush ();
		snapshotCCombo (combo);
		call ("setListVisible(false)", () -> { combo.setListVisible (false); return combo.getListVisible (); });
		flush ();
		combo.dispose ();
	}

	static void randomCombo (int style, int seed, int steps) {
		scenario = "combo.random.s" + Integer.toHexString (style) + ".seed" + seed;
		step = 0;
		Random random = new Random (seed * 13L + style);
		Combo combo = new Combo (shell, style);
		combo.setBounds (240, 200, 160, 30);
		for (int s = 0; s < steps; s++) {
			int count = combo.getItemCount ();
			int a = random.nextInt (count + 4) - 2;
			String w = word (random);
			switch (random.nextInt (8)) {
				case 0: call ("add(String)", () -> { combo.add (w); return null; }); break;
				case 1: call ("add(String,int)", () -> { combo.add (w, a); return null; }); break;
				case 2: call ("setItem", () -> { combo.setItem (a, w); return null; }); break;
				case 3: call ("remove(int)", () -> { combo.remove (a); return null; }); break;
				case 4: call ("select", () -> { combo.select (a); return null; }); break;
				case 5: call ("indexOf(String)", () -> combo.indexOf (w)); break;
				case 6: call ("setText", () -> { combo.setText (w); return null; }); break;
				default: call ("getItem", () -> combo.getItem (a)); break;
			}
			call ("state.items", combo::getItems);
			call ("state.selectionIndex", combo::getSelectionIndex);
			call ("state.text", combo::getText);
		}
		combo.dispose ();
	}
}
