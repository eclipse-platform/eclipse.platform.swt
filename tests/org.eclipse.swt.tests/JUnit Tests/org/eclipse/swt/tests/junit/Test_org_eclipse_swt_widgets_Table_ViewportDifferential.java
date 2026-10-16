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
import java.util.*;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.win32.*;
import org.eclipse.swt.widgets.*;
import org.junit.jupiter.api.*;

/**
 * Recorded differential of the Table viewport (docs/m3/TABLE_MEDIATED_VIEWPORT.md) on the shared harness
 * ({@link ViewportDifferential}, docs/m3/VIEWPORT_DIFFERENTIAL_HARNESS.md).
 * <p>
 * Seeded random scripts of public API operations run on a Table; every step records the answer, the row
 * count and top row, the scroll bar, the selection, the focus row and the events (Selection,
 * DefaultSelection and SetData, in order), and every seed ends with the pixels. A reference run records
 * into <code>-Dswt.viewport.recording=&lt;dir&gt;</code>: the official SWT jar on the class path (stock
 * behaviour) or this SWT with <code>-Dswt.widget.mode.Table=original</code>. The viewport run replays the
 * seeds and compares every line; a difference fails with the operation log of the seed and names the
 * reference. Without a recording the comparison is skipped.
 * </p><p>
 * Two processes see different desktops, so each seed runs in a state both can guarantee: the shell is off
 * the virtual screen (whether the control paints during a step, and so when SetData is sent, depends on
 * how much of it the desktop shows), the pixels are drawn on request with WM_PRINT (client and children),
 * keyboard focus is on a one-pixel button outside the table and focus rectangles are hidden.
 * </p>
 */
public class Test_org_eclipse_swt_widgets_Table_ViewportDifferential {
	static final int ROWS = 300, SEEDS = 12, STEPS = 150;
	/* plain multi with columns, plain single with check boxes, virtual multi */
	static final int [] VARIANTS = {
		SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION,
		SWT.SINGLE | SWT.BORDER | SWT.CHECK,
		SWT.MULTI | SWT.BORDER | SWT.VIRTUAL,
	};

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
	public void seededScriptsAnswerLikeTheReference () throws IOException {
		Display display = Display.getDefault ();
		boolean record = ViewportDifferential.isReference ("Table");
		if (!record) Assumptions.assumeTrue (viewportActive (display), "Table viewport mediation is not active in this build");
		String directory = System.getProperty ("swt.viewport.recording");
		for (int variant = 0; variant < VARIANTS.length; variant++) {
			Scene scene = new Scene (display, VARIANTS [variant]);
			try {
				harness (variant).compareWithRecording (scene, record, directory);
			} finally {
				scene.dispose ();
			}
		}
	}

	static ViewportDifferential<Scene> harness (int variant) {
		return ViewportDifferential.<Scene> of ("table-variant" + variant, 0x7AB1E5EEDL + 31L * variant, Test_org_eclipse_swt_widgets_Table_ViewportDifferential::next)
				.widget ("Table").seeds (SEEDS).steps (STEPS)
				.beforeSeed (Scene::reset)
				.beforeStep (scene -> scene.events.setLength (0))
				.probe ("rows", scene -> scene.table.getItemCount () + " top " + scene.table.getTopIndex ())
				.probe ("bar", scene -> ViewportDifferential.bar (scene.table.getVerticalBar ()))
				.probe ("hbar", scene -> ViewportDifferential.bar (scene.table.getHorizontalBar ()))
				.probe ("sel", scene -> scene.table.getSelectionCount () + Arrays.toString (sorted (scene.table.getSelectionIndices ()))
						+ " index " + scene.table.getSelectionIndex ())
				.probe ("focus", scene -> String.valueOf (focusIndex (scene.table)))
				.probe ("events", scene -> scene.events.toString ())
				.pixels (Scene::pixels);
	}

	/** A Table on an off-screen shell, recreated for every seed, with its event log. */
	static final class Scene {
		final Display display;
		final int style;
		final StringBuilder events = new StringBuilder ();
		Shell shell;
		Table table;
		int step;

		Scene (Display display, int style) {
			this.display = display;
			this.style = style;
		}

		void reset () {
			dispose ();
			step = 0;
			shell = new Shell (display);
			Rectangle screen = display.getBounds ();
			shell.setBounds (screen.x - 400, screen.y + 20, 320, 420);
			table = new Table (shell, style);
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
							((TableItem) event.item).setText (0, "data " + event.index);
							events.append ("SD:").append (event.index);
						}
					}
					events.append (' ');
				});
			}
			if ((style & SWT.VIRTUAL) != 0) {
				table.setItemCount (ROWS);
			} else {
				for (int i = 0; i < ROWS; i++) new TableItem (table, SWT.NONE).setText (new String [] {"row " + i, "cell " + i});
			}
			shell.open ();
			/* the table receives focus once: the caret it gets on focus is part of the behaviour */
			table.setFocus ();
			ViewportDifferential.settle (display, 150);
			events.setLength (0);
		}

		void dispose () {
			if (shell != null && !shell.isDisposed ()) shell.dispose ();
		}

		/* the table unfocused (a one-pixel button outside it takes the focus), focus rectangles hidden */
		ImageData pixels () {
			Button focusSink = new Button (shell, SWT.PUSH);
			focusSink.setBounds (0, 0, 1, 1);
			focusSink.setFocus ();
			long cues = OS.MAKEWPARAM (OS.UIS_SET, OS.UISF_HIDEFOCUS | OS.UISF_HIDEACCEL);
			OS.SendMessage (shell.handle, OS.WM_CHANGEUISTATE, cues, 0);
			OS.SendMessage (table.handle, OS.WM_UPDATEUISTATE, cues, 0);
			table.redraw ();
			ViewportDifferential.settle (display, 150);
			ImageData data = print (display, table);
			focusSink.dispose ();
			return data;
		}
	}

	private static String text (Widget item) {
		return item instanceof TableItem tableItem ? tableItem.getText () : "-";
	}

	private static int [] sorted (int [] indices) {
		Arrays.sort (indices);
		return indices.length > 12 ? Arrays.copyOf (indices, 12) : indices;
	}

	/** The caret row (package-private Table.getFocusIndex), which keyboard and focus behaviour start from. */
	static int focusIndex (Table table) {
		try {
			java.lang.reflect.Method method = Table.class.getDeclaredMethod ("getFocusIndex");
			method.setAccessible (true);
			return (Integer) method.invoke (table);
		} catch (ReflectiveOperationException e) {
			throw new IllegalStateException (e);
		}
	}

	/**
	 * The client area and the children (rows and header) drawn by the control itself (WM_PRINT without
	 * PRF_NONCLIENT), as 24-bit RGB pixel data. Control.print copies the composed screen content, which can
	 * be stale in a process that is not in the foreground; PrintWindow leaves the themed scroll bar
	 * unpainted in some runs (the bar is recorded in every step instead); and the unused byte of 32-bit
	 * pixel data differs between processes.
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
		ImageData rgb = new ImageData (data.width, data.height, 24, new PaletteData (0xFF0000, 0xFF00, 0xFF));
		int [] line = new int [data.width];
		for (int y = 0; y < data.height; y++) {
			data.getPixels (0, y, data.width, line, 0);
			for (int x = 0; x < line.length; x++) {
				RGB color = data.palette.getRGB (line [x]);
				line [x] = (color.red << 16) | (color.green << 8) | color.blue;
			}
			rgb.setPixels (0, y, data.width, line, 0);
		}
		return rgb;
	}

	/*
	 * One public API operation. Everything random is drawn here, before the operation runs, so the
	 * script does not depend on the subject; row positions are fractions of the current row count.
	 */
	static ViewportDifferential.Op<Scene> next (Random random, StringBuilder log) {
		int kind = random.nextInt (25), fraction = random.nextInt (1 << 20), span = random.nextInt (6);
		int delta = (random.nextBoolean () ? 1 : -1) * random.nextInt (40);
		boolean rare = random.nextInt (4) == 0;
		log.append ("op ").append (kind).append (" at ").append (fraction).append (" span ").append (span).append (" delta ").append (delta)
				.append (rare ? " rare" : "").append ('\n');
		return scene -> {
			Table table = scene.table;
			int step = scene.step++;
			int count = table.getItemCount ();
			int k = count == 0 ? 0 : (int) ((long) fraction * count >>> 20);
			boolean check = (table.getStyle () & SWT.CHECK) != 0;
			switch (kind) {
				case 0: {
					new TableItem (table, SWT.NONE, k).setText ("new " + step);
					return "insert " + k + " -> index " + table.indexOf (table.getItem (k));
				}
				case 1: {
					TableItem item = new TableItem (table, SWT.NONE);
					item.setText ("appended " + step);
					return "append -> index " + table.indexOf (item);
				}
				case 2:
					if (count > 0) table.remove (k);
					return "remove " + k;
				case 3: {
					int end = Math.min (count - 1, k + span);
					if (count > 0) table.remove (k, end);
					return "remove " + k + ".." + end;
				}
				case 4: {
					int [] indices = {k, Math.max (0, k - span), Math.min (count - 1, k + 2 * span)};
					if (count > 0) table.remove (indices);
					return "remove " + Arrays.toString (indices);
				}
				case 5: {
					int target = Math.max (0, count + delta);
					table.setItemCount (target);
					return "setItemCount " + target;
				}
				case 6:
					table.setSelection (k);
					return "setSelection " + k;
				case 7:
					table.select (k);
					return "select " + k;
				case 8:
					table.select (k, k + span);
					return "select " + k + ".." + (k + span);
				case 9:
					table.deselect (k);
					return "deselect " + k;
				case 10:
					table.deselectAll ();
					return "deselectAll";
				case 11:
					table.selectAll ();
					return "selectAll";
				case 12: {
					int [] indices = {k, Math.max (0, k - span)};
					table.setSelection (indices);
					return "setSelection " + Arrays.toString (indices);
				}
				case 13:
					if (count > 0) table.setSelection (table.getItem (k));
					return "setSelection item " + k;
				case 14:
					table.setTopIndex (k);
					return "setTopIndex " + k + " -> top " + table.getTopIndex ();
				case 15:
					if (count > 0) table.showItem (table.getItem (k));
					return "showItem " + k + " -> top " + table.getTopIndex ();
				case 16:
					table.showSelection ();
					return "showSelection -> top " + table.getTopIndex ();
				case 17: {
					if (count == 0) return "read: empty";
					TableItem item = table.getItem (k);
					return "read " + k + " -> " + item.getText () + " / " + item.getText (1) + " index " + table.indexOf (item) + " selected "
							+ table.isSelected (k) + " checked " + item.getChecked ();
				}
				case 18:
					if (count > 0) table.getItem (k).setText ("text " + step);
					return "setText " + k;
				case 19:
					if (count > 0) table.clear (k);
					return "clear " + k;
				case 20: {
					int [] indices = {k, Math.min (count - 1, k + span)};
					if (count > 0) table.clear (indices);
					return "clear " + Arrays.toString (indices);
				}
				case 21:
					if (rare) table.clearAll ();
					return (rare ? "clearAll" : "getTopIndex") + " -> top " + table.getTopIndex ();
				case 22:
					if (count > 0 && check) table.getItem (k).setChecked (!table.getItem (k).getChecked ());
					return "setChecked " + k + " -> " + (count > 0 && table.getItem (k).getChecked ());
				case 23:
					if (count > 0) table.getItem (k).dispose ();
					return "dispose item " + k;
				default: {
					/* one in five of the rare cases empties and refills the table */
					boolean all = rare && fraction % 5 == 0;
					if (all) {
						table.removeAll ();
						if ((table.getStyle () & SWT.VIRTUAL) != 0) {
							table.setItemCount (ROWS / 3);
						} else {
							for (int i = 0; i < ROWS / 3; i++) new TableItem (table, SWT.NONE).setText ("refill " + i);
						}
					}
					TableItem [] items = table.getItems ();
					return (all ? "removeAll + refill" : "getItems") + " -> items " + items.length
							+ (items.length > 0 ? " last " + items [items.length - 1].getText () : "");
				}
			}
		};
	}
}
