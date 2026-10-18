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

import java.util.*;

import org.eclipse.swt.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.junit.jupiter.api.*;

/**
 * Oracle of leaf L11 (docs/m3/TOOLBAR_VIEWPORT.md): what a ToolBar and a CoolBar with many items cost per operation
 * (create, compute size, paint, getItems, indexOf the last item, rename a far item, dispose and re-add the first item,
 * one more item at this size). Runs against any jar; prints one line per size, medians in microseconds. Opt-in:
 * <code>-Dswt.toolbar.oracle=true</code>; sizes with <code>-Dswt.toolbar.oracle.counts=500,2000</code>.
 */
public class Test_org_eclipse_swt_widgets_ToolBar_ScaleOracle {

	@Test
	public void measure () {
		Assumptions.assumeTrue (Boolean.getBoolean ("swt.toolbar.oracle"), "-Dswt.toolbar.oracle=true is not set");
		Display display = Display.getDefault ();
		ViewportDifferential.parkCursor (display);
		int [] counts = Arrays.stream (System.getProperty ("swt.toolbar.oracle.counts", "500,2000").split (",")).mapToInt (Integer::parseInt).toArray ();
		for (int count : counts) {
			for (int wrap : new int [] {SWT.NONE, SWT.WRAP}) {
				Shell shell = new Shell (display, SWT.NO_TRIM | SWT.ON_TOP);
				shell.setBounds (40, 40, 800, 400);
				shell.setLayout (new FillLayout ());
				ToolBar bar = new ToolBar (shell, SWT.FLAT | SWT.RIGHT | wrap);
				shell.open ();
				ViewportDifferential.drain (display);
				long start = System.nanoTime ();
				for (int i = 0; i < count; i++) {
					item (bar, i, -1);
				}
				ViewportDifferential.drain (display);
				long create = (System.nanoTime () - start) / 1000;
				long [] size = new long [7], paint = new long [7], items = new long [7], index = new long [7], rename = new long [7], readd = new long [7];
				for (int k = 0; k < size.length; k++) {
					start = System.nanoTime ();
					bar.computeSize (SWT.DEFAULT, SWT.DEFAULT, true);
					size [k] = (System.nanoTime () - start) / 1000;
					start = System.nanoTime ();
					bar.redraw ();
					bar.update ();
					paint [k] = (System.nanoTime () - start) / 1000;
					start = System.nanoTime ();
					ToolItem [] all = bar.getItems ();
					items [k] = (System.nanoTime () - start) / 1000;
					start = System.nanoTime ();
					bar.indexOf (all [all.length - 1]);
					index [k] = (System.nanoTime () - start) / 1000;
					start = System.nanoTime ();
					all [all.length / 2].setText ("renamed " + k);
					ViewportDifferential.drain (display);
					rename [k] = (System.nanoTime () - start) / 1000;
					start = System.nanoTime ();
					all [0].dispose ();
					item (bar, k, 0);
					ViewportDifferential.drain (display);
					readd [k] = (System.nanoTime () - start) / 1000;
				}
				start = System.nanoTime ();
				for (int i = 0; i < APPEND; i++) {
					item (bar, count + i, -1);
				}
				ViewportDifferential.drain (display);
				long append = (System.nanoTime () - start) / 1000 / APPEND;
				System.out.println ("toolbar-oracle " + ViewportDifferential.reference ("ToolBar") + (wrap == SWT.WRAP ? " wrap" : " single") + " items " + count
					+ " create " + create + " computeSize " + median (size) + " paint " + median (paint) + " getItems " + median (items)
					+ " indexOf " + median (index) + " rename " + median (rename) + " readd " + median (readd) + " append " + append);
				shell.dispose ();
			}
			Shell shell = new Shell (display, SWT.NO_TRIM | SWT.ON_TOP);
			shell.setBounds (40, 40, 800, 400);
			shell.setLayout (new FillLayout ());
			CoolBar cool = new CoolBar (shell, SWT.FLAT);
			shell.open ();
			ViewportDifferential.drain (display);
			int bands = Math.max (1, count / 20);
			long start = System.nanoTime ();
			for (int i = 0; i < bands; i++) {
				CoolItem coolItem = new CoolItem (cool, SWT.DROP_DOWN);
				ToolBar bar = new ToolBar (cool, SWT.FLAT);
				for (int j = 0; j < 20; j++) {
					item (bar, i * 20 + j, -1);
				}
				bar.pack ();
				coolItem.setControl (bar);
				org.eclipse.swt.graphics.Point preferred = bar.getSize ();
				coolItem.setPreferredSize (coolItem.computeSize (preferred.x, preferred.y));
			}
			ViewportDifferential.drain (display);
			long create = (System.nanoTime () - start) / 1000;
			long [] wrapIndices = new long [7], paint = new long [7], layout = new long [7];
			for (int k = 0; k < wrapIndices.length; k++) {
				start = System.nanoTime ();
				cool.setWrapIndices (new int [] {bands / 3, 2 * bands / 3});
				ViewportDifferential.drain (display);
				wrapIndices [k] = (System.nanoTime () - start) / 1000;
				start = System.nanoTime ();
				cool.redraw ();
				cool.update ();
				paint [k] = (System.nanoTime () - start) / 1000;
				start = System.nanoTime ();
				shell.setSize (800 + (k & 1), 400);
				ViewportDifferential.drain (display);
				layout [k] = (System.nanoTime () - start) / 1000;
			}
			System.out.println ("coolbar-oracle " + ViewportDifferential.reference ("CoolBar") + " bands " + bands + " create " + create
				+ " setWrapIndices " + median (wrapIndices) + " paint " + median (paint) + " resize " + median (layout));
			shell.dispose ();
		}
	}

	static final int APPEND = 200;

	static void item (ToolBar bar, int i, int index) {
		ToolItem item = index < 0 ? new ToolItem (bar, (i % 7) == 6 ? SWT.SEPARATOR : SWT.PUSH) : new ToolItem (bar, SWT.PUSH, index);
		if ((item.getStyle () & SWT.SEPARATOR) == 0) {
			item.setText ("tool " + i);
			item.setToolTipText ("tip " + i);
		}
	}

	static long median (long [] values) {
		long [] sorted = values.clone ();
		Arrays.sort (sorted);
		return sorted [sorted.length / 2];
	}
}
