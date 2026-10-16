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
 * Recorded differential of CTabFolder (leaf L5, docs/m3/CTABFOLDER_VIEWPORT.md) against a reference run (the
 * official SWT jar, or this SWT with <code>-Dswt.viewport.record=true</code>): seeded scripts create, dispose,
 * select and show tabs (up to a few hundred, so that most are hidden behind the chevron), toggle the folder's
 * options, set tab and folder fonts, tab images and two-line texts (they change the header height) and resize it; every step records the answer, the folder state (selection, item count, the first
 * visible item, item bounds and visibility, client area, tab height, hit tests), the events and the Paint
 * regions; every seed the rendered pixels.
 */
public class Test_org_eclipse_swt_custom_CTabFolder_ViewportDifferential {
	Display display;

	@BeforeEach
	public void setUp () {
		display = Display.getDefault ();
		ViewportDifferential.parkCursor (display);
	}

	final class Scene {
		final Shell shell;
		final CTabFolder folder;
		final StringBuilder events = new StringBuilder (), paints = new StringBuilder ();
		final Font [] fonts;
		final Image [] images;
		int created;

		Scene () {
			shell = new Shell (display, SWT.NO_TRIM | SWT.ON_TOP);
			shell.setBounds (40, 40, 480, 300);
			shell.setLayout (new FillLayout ());
			folder = new CTabFolder (shell, SWT.CLOSE | SWT.BORDER);
			folder.addListener (SWT.Selection, e -> events.append ("Sel:").append (e.item instanceof CTabItem item ? text (item) : "-").append (' '));
			folder.addListener (SWT.Paint, e -> paints.append (e.x).append (',').append (e.y).append (' ').append (e.width).append ('x').append (e.height).append (';'));
			folder.addListener (SWT.Resize, e -> events.append ("R "));
			fonts = new Font [] {new Font (display, "Segoe UI", 8, SWT.NORMAL), new Font (display, "Segoe UI", 15, SWT.BOLD)};
			images = new Image [] {image (12), image (30)};
			shell.open ();
			flush ();
		}

		Image image (int height) {
			Image image = new Image (display, 16, height);
			GC gc = new GC (image);
			gc.setBackground (display.getSystemColor (height > 20 ? SWT.COLOR_DARK_GREEN : SWT.COLOR_DARK_RED));
			gc.fillRectangle (0, 0, 16, height);
			gc.dispose ();
			return image;
		}

		void flush () {
			ViewportDifferential.drain (display);
			folder.update ();
			ViewportDifferential.drain (display);
		}

		CTabItem add (int index, int length) {
			CTabItem item = new CTabItem (folder, SWT.NONE, Math.min (index, folder.getItemCount ()));
			StringBuilder text = new StringBuilder ("tab ").append (created++);
			for (int k = 0; k < length; k++) text.append ((char) ('a' + k % 26));
			item.setText (text.toString ());
			Label page = new Label (folder, SWT.NONE);
			page.setText ("page of " + item.getText ());
			item.setControl (page);
			return item;
		}

		String state () {
			StringBuilder out = new StringBuilder ();
			CTabItem selection = folder.getSelection ();
			out.append ("count ").append (folder.getItemCount ()).append (" sel ").append (folder.getSelectionIndex ()).append (' ')
				.append (selection == null ? "-" : text (selection)).append (" client ").append (folder.getClientArea ())
				.append (" tabHeight ").append (folder.getTabHeight ());
			CTabItem [] items = folder.getItems ();
			int shown = 0, firstShown = -1;
			for (int i = 0; i < items.length; i++) {
				if (items [i].isShowing ()) {
					shown++;
					if (firstShown < 0) firstShown = i;
				}
			}
			out.append (" shown ").append (shown).append (" first ").append (firstShown);
			for (int i : new int [] {0, firstShown, items.length / 2, items.length - 1}) {
				if (i >= 0 && i < items.length) out.append (" b").append (i).append (' ').append (items [i].getBounds ());
			}
			for (int x : new int [] {10, 120, 300, 470}) {
				CTabItem hit = folder.getItem (new Point (x, 10));
				out.append (" hit").append (x).append (' ').append (hit == null ? "-" : text (hit));
			}
			return out.toString ();
		}

		/* a tab text on one recorded line (two-line texts are part of the scripts) */
		String text (CTabItem item) {
			return item.getText ().replace ("\n", "/");
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
			int a = random.nextInt (1 << 16), b = random.nextInt (40), count = 1 + random.nextInt (random.nextInt (8) == 0 ? 200 : 8);
			boolean flag = random.nextBoolean ();
			log.append (switch (kind) {
				case 0, 1 -> "add " + count + " tabs at " + a;
				case 2 -> "dispose a tab " + a;
				case 3, 4 -> "setSelection " + a;
				case 5 -> "showItem " + a;
				case 6 -> "setSimple " + flag;
				case 7 -> "setMRUVisible " + flag;
				case 8 -> "setMaximizeVisible/MinimizeVisible " + flag;
				case 9 -> "setUnselectedCloseVisible " + flag;
				case 10 -> "setTabHeight " + (flag ? -1 : 18 + b % 20);
				case 11 -> "setMinimumCharacters " + (1 + b % 30);
				case 12 -> "resize " + (200 + a % 500) + "x" + (120 + b * 5);
				case 13 -> "setTabPosition " + (flag ? "TOP" : "BOTTOM");
				case 14 -> "setItemText " + a;
				case 15 -> "setItemFont " + a + " " + b % 3;
				case 16 -> "setFolderFont " + b % 3;
				case 17 -> "setItemImage " + a + " " + b % 3;
				case 18 -> "setItemText (two lines) " + a;
				default -> "dispose all";
			}).append ('\n');
			return scene -> {
				CTabFolder folder = scene.folder;
				int items = folder.getItemCount ();
				switch (kind) {
					case 0, 1 -> {
						for (int i = 0; i < count; i++) scene.add (a % (items + 1) + i, b);
					}
					case 2 -> {
						if (items > 0) {
							CTabItem item = folder.getItem (a % items);
							Control page = item.getControl ();
							item.dispose ();
							if (page != null) page.dispose ();
						}
					}
					case 3, 4 -> {
						if (items > 0) folder.setSelection (a % items);
					}
					case 5 -> {
						if (items > 0) folder.showItem (folder.getItem (a % items));
					}
					case 6 -> folder.setSimple (flag);
					case 7 -> folder.setMRUVisible (flag);
					case 8 -> {
						folder.setMaximizeVisible (flag);
						folder.setMinimizeVisible (!flag);
					}
					case 9 -> folder.setUnselectedCloseVisible (flag);
					case 10 -> folder.setTabHeight (flag ? -1 : 18 + b % 20);
					case 11 -> folder.setMinimumCharacters (1 + b % 30);
					case 12 -> scene.shell.setSize (200 + a % 500, 120 + b * 5);
					case 13 -> folder.setTabPosition (flag ? SWT.TOP : SWT.BOTTOM);
					case 14 -> {
						if (items > 0) folder.getItem (a % items).setText ("renamed " + a);
					}
					case 15 -> {
						if (items > 0) folder.getItem (a % items).setFont (b % 3 == 2 ? null : scene.fonts [b % 3]);
					}
					case 16 -> folder.setFont (b % 3 == 2 ? null : scene.fonts [b % 3]);
					case 17 -> {
						if (items > 0) folder.getItem (a % items).setImage (b % 3 == 2 ? null : scene.images [b % 3]);
					}
					case 18 -> {
						if (items > 0) folder.getItem (a % items).setText ("two " + a + "\nlines");
					}
					default -> {
						for (CTabItem item : folder.getItems ()) {
							Control page = item.getControl ();
							item.dispose ();
							if (page != null) page.dispose ();
						}
					}
				}
				return "";
			};
		};
	}

	@Test
	public void recordedAgainstReference () throws IOException {
		String reference = ViewportDifferential.reference ("CTabFolder");
		boolean viewport = "viewport".equals (reference);
		Scene scene = new Scene ();
		try {
			ViewportDifferential.of ("CTabFolder", 0xC7AB0L, ops ()).widget ("CTabFolder").seeds (8).steps (120)
				.beforeStep (s -> {
					s.events.setLength (0);
					s.paints.setLength (0);
				})
				.afterStep (Scene::flush)
				.probe ("state", Scene::state)
				.probe ("events", s -> s.events.toString ())
				.probe ("paint", s -> s.paints.toString (), viewport ? ViewportDifferential.REGIONS_WITHIN : String::equals)
				.pixels (s -> {
					ViewportDifferential.settle (display, 100);
					return ViewportDifferential.render (s.folder);
				})
				.compareWithRecording (scene, ViewportDifferential.records ("CTabFolder"), System.getProperty ("swt.viewport.recording"));
		} finally {
			scene.dispose ();
		}
	}
}
