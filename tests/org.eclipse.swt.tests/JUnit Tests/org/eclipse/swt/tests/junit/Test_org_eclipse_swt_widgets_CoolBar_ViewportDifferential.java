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

import java.io.*;
import java.util.*;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.junit.jupiter.api.*;

/**
 * Recorded differential of CoolBar (leaf L11, docs/m3/TOOLBAR_VIEWPORT.md) against a reference run (the official SWT
 * jar, or this SWT with <code>-Dswt.viewport.record=true</code>): seeded scripts add and dispose bands whose controls
 * are tool bars, combos, texts and labels, grow and rename tool bars, set band sizes, preferred and minimum sizes,
 * wrap indices, the item layout and the lock, and resize the bar; every step records the bar and every band (order,
 * wraps, bounds and sizes, the bounds and visibility of every band control), the Resize events of the bar and the
 * Move and Resize events of the band controls, and the Paint regions of the bar; every seed the rendered pixels.
 */
public class Test_org_eclipse_swt_widgets_CoolBar_ViewportDifferential {
	Display display;

	@BeforeEach
	public void setUp () {
		display = Display.getDefault ();
		ViewportDifferential.parkCursor (display);
	}

	final class Scene {
		final Shell shell;
		final CoolBar bar;
		final StringBuilder events = new StringBuilder (), paints = new StringBuilder ();
		final Font [] fonts;
		int created;

		Scene () {
			shell = new Shell (display, SWT.NO_TRIM | SWT.ON_TOP);
			shell.setBounds (40, 40, 520, 260);
			bar = new CoolBar (shell, SWT.FLAT);
			bar.setBounds (0, 0, 500, 40);
			bar.addListener (SWT.Resize, e -> events.append ("R ").append (bar.getSize ()).append (' '));
			bar.addListener (SWT.Paint, e -> paints.append (e.x).append (',').append (e.y).append (' ').append (e.width).append ('x').append (e.height).append (';'));
			fonts = new Font [] {new Font (display, "Segoe UI", 8, SWT.NORMAL), new Font (display, "Segoe UI", 14, SWT.BOLD)};
			shell.open ();
			flush ();
		}

		void flush () {
			ViewportDifferential.drain (display);
			bar.update ();
			ViewportDifferential.drain (display);
		}

		void add (int index, int kind, int tools, boolean dropDown) {
			CoolItem item = new CoolItem (bar, dropDown ? SWT.DROP_DOWN : SWT.NONE, Math.min (index, bar.getItemCount ()));
			int id = created++;
			Control control = switch (kind % 4) {
				case 0, 1 -> {
					ToolBar tools2 = new ToolBar (bar, SWT.FLAT | (kind % 4 == 1 ? SWT.RIGHT : SWT.NONE));
					for (int i = 0; i < tools; i++) {
						ToolItem tool = new ToolItem (tools2, (i % 5) == 4 ? SWT.SEPARATOR : SWT.PUSH);
						if ((tool.getStyle () & SWT.SEPARATOR) == 0) tool.setText ("t" + id + "." + i);
					}
					yield tools2;
				}
				case 2 -> {
					Combo combo = new Combo (bar, SWT.DROP_DOWN);
					combo.setItems ("one " + id, "two", "three");
					combo.select (0);
					yield combo;
				}
				default -> {
					if ((id & 1) == 0) {
						Text text = new Text (bar, SWT.BORDER);
						text.setText ("text " + id);
						yield text;
					}
					Label label = new Label (bar, SWT.NONE);
					label.setText ("label " + id);
					yield label;
				}
			};
			String name = control.getClass ().getSimpleName () + id;
			control.addListener (SWT.Move, e -> events.append ("M:").append (name).append (' '));
			control.addListener (SWT.Resize, e -> events.append ("S:").append (name).append (' '));
			control.pack ();
			item.setControl (control);
			fit (item);
		}

		void fit (CoolItem item) {
			Control control = item.getControl ();
			if (control == null) return;
			Point size = control.computeSize (SWT.DEFAULT, SWT.DEFAULT);
			Point preferred = item.computeSize (size.x, size.y);
			item.setPreferredSize (preferred);
			item.setMinimumSize (Math.min (size.x, 40), size.y);
		}

		void dispose (CoolItem item) {
			Control control = item.getControl ();
			item.dispose ();
			if (control != null) control.dispose ();
		}

		String state () {
			StringBuilder out = new StringBuilder ();
			out.append ("count ").append (bar.getItemCount ()).append (" size ").append (bar.getSize ()).append (" locked ").append (bar.getLocked ())
				.append (" order ").append (Arrays.toString (bar.getItemOrder ())).append (" wraps ").append (Arrays.toString (bar.getWrapIndices ()));
			CoolItem [] items = bar.getItems ();
			for (int i = 0; i < items.length; i++) {
				CoolItem item = items [i];
				out.append (" | ").append (i).append (' ').append (item.getBounds ()).append (" s").append (item.getSize ()).append (" p")
					.append (item.getPreferredSize ()).append (" m").append (item.getMinimumSize ());
				Control control = item.getControl ();
				if (control != null) out.append (" c").append (control.getBounds ()).append (control.getVisible () ? " v" : " h");
			}
			return out.toString ();
		}

		void dispose () {
			shell.dispose ();
			for (Font font : fonts) font.dispose ();
		}
	}

	static ViewportDifferential.Ops<Scene> ops () {
		return (random, log) -> {
			int kind = random.nextInt (18);
			int a = random.nextInt (1 << 16), b = random.nextInt (1000), count = 1 + random.nextInt (random.nextInt (5) == 0 ? 24 : 4);
			boolean flag = random.nextBoolean ();
			log.append (switch (kind) {
				case 0, 1, 2 -> "add " + count + " bands at " + a + " kind " + b % 4 + " tools " + (1 + b % 9) + (flag ? " drop-down" : "");
				case 3 -> "dispose a band " + a;
				case 4 -> "setWrapIndices " + a;
				case 5 -> "setLocked " + flag;
				case 6 -> "setSize of a band " + a + " " + (20 + b % 300) + "x" + (18 + b % 40);
				case 7 -> "setPreferredSize of a band " + a + " " + (20 + b % 400);
				case 8 -> "setMinimumSize of a band " + a + " " + (b % 200);
				case 9 -> "setItemLayout " + a;
				case 10 -> "resize the bar to " + (80 + a % 440);
				case 11 -> "add tools to a band " + a + " x" + (1 + b % 6);
				case 12 -> "rename a tool of a band " + a;
				case 13 -> "set the font of a band control " + a + " " + b % 3;
				case 14 -> "fit a band " + a;
				case 15 -> "set the control of a band " + a + " to " + (flag ? "none" : "a new label");
				case 16 -> "resize the shell to " + (200 + a % 320) + "x" + (100 + b % 160);
				default -> "dispose all";
			}).append ('\n');
			return scene -> {
				CoolBar bar = scene.bar;
				int items = bar.getItemCount ();
				CoolItem item = items > 0 ? bar.getItem (a % items) : null;
				switch (kind) {
					case 0, 1, 2 -> {
						for (int i = 0; i < count; i++) scene.add (a % (items + 1) + i, b + i, 1 + (b + i) % 9, flag);
					}
					case 3 -> {
						if (item != null) scene.dispose (item);
					}
					case 4 -> {
						int [] wraps = new Random (a).ints (Math.min (items, a % 4), 0, Math.max (1, items)).distinct ().sorted ().toArray ();
						bar.setWrapIndices (wraps);
					}
					case 5 -> bar.setLocked (flag);
					case 6 -> {
						if (item != null) item.setSize (20 + b % 300, 18 + b % 40);
					}
					case 7 -> {
						if (item != null) item.setPreferredSize (20 + b % 400, item.getPreferredSize ().y);
					}
					case 8 -> {
						if (item != null) item.setMinimumSize (b % 200, item.getMinimumSize ().y);
					}
					case 9 -> {
						if (items > 0) {
							java.util.List<Integer> order = new ArrayList<> ();
							for (int i = 0; i < items; i++) order.add (i);
							Collections.shuffle (order, new Random (a));
							int [] wraps = new Random (b).ints (Math.min (items, b % 3), 0, items).distinct ().sorted ().toArray ();
							Point [] sizes = new Point [items];
							CoolItem [] all = bar.getItems ();
							for (int i = 0; i < items; i++) sizes [i] = all [i].getSize ();
							bar.setItemLayout (order.stream ().mapToInt (Integer::intValue).toArray (), wraps, sizes);
						}
					}
					case 10 -> bar.setSize (80 + a % 440, bar.getSize ().y);
					case 11 -> {
						if (item != null && item.getControl () instanceof ToolBar tools) {
							for (int i = 0; i < 1 + b % 6; i++) new ToolItem (tools, SWT.PUSH).setText ("n" + i);
							tools.pack ();
							scene.fit (item);
						}
					}
					case 12 -> {
						if (item != null && item.getControl () instanceof ToolBar tools && tools.getItemCount () > 0) {
							ToolItem tool = tools.getItem (b % tools.getItemCount ());
							if ((tool.getStyle () & SWT.SEPARATOR) == 0) tool.setText (flag ? "long renamed " + a : "r" + b);
						}
					}
					case 13 -> {
						if (item != null && item.getControl () != null) {
							item.getControl ().setFont (b % 3 == 2 ? null : scene.fonts [b % 3]);
							item.getControl ().pack ();
							scene.fit (item);
						}
					}
					case 14 -> {
						if (item != null) scene.fit (item);
					}
					case 15 -> {
						if (item != null) {
							Control old = item.getControl ();
							if (flag) {
								item.setControl (null);
							} else {
								Label label = new Label (bar, SWT.NONE);
								label.setText ("swapped " + a);
								label.pack ();
								item.setControl (label);
							}
							if (old != null) old.dispose ();
						}
					}
					case 16 -> scene.shell.setSize (200 + a % 320, 100 + b % 160);
					default -> {
						for (CoolItem band : bar.getItems ()) scene.dispose (band);
					}
				}
				return "";
			};
		};
	}

	@Test
	public void recordedAgainstReference () throws IOException {
		Assumptions.assumeTrue ("win32".equals (SWT.getPlatform ()), "the rebar notifications measured here are win32");
		Scene scene = new Scene ();
		try {
			ViewportDifferential.of ("CoolBar", 0xC001BL, ops ()).widget ("CoolBar").seeds (8).steps (100)
				.beforeStep (s -> {
					s.events.setLength (0);
					s.paints.setLength (0);
				})
				.afterStep (Scene::flush)
				.probe ("state", Scene::state)
				.probe ("events", s -> s.events.toString ())
				.probe ("paint", s -> s.paints.toString ()) // equal in both modes: no upstream defect is fixed (rule 12)
				.pixels (s -> {
					ViewportDifferential.settle (display, 100);
					return ViewportDifferential.render (s.bar);
				})
				.compareWithRecording (scene, ViewportDifferential.records ("CoolBar"), System.getProperty ("swt.viewport.recording"));
		} finally {
			scene.dispose ();
		}
	}
}
