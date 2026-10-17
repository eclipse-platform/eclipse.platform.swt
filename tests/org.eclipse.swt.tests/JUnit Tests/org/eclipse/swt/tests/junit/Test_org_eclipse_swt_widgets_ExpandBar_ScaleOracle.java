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

import java.util.*;

import org.eclipse.swt.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.junit.jupiter.api.*;

/**
 * Oracle of leaf L6 (docs/m3/EXPANDBAR_VIEWPORT.md): what an ExpandBar with many items costs per operation (create,
 * paint, scroll a page, expand and collapse the first item, resize, append one more item), every tenth item with a
 * control. Runs against any
 * jar (official or this SWT); prints one line per size, medians in microseconds. Opt-in:
 * <code>-Dswt.expandbar.oracle=true</code>; sizes with <code>-Dswt.expandbar.oracle.counts=1000,4000</code>.
 * Win32 only (scrolling is driven with window messages).
 */
public class Test_org_eclipse_swt_widgets_ExpandBar_ScaleOracle {

	@Test
	public void measure () {
		Assumptions.assumeTrue (Boolean.getBoolean ("swt.expandbar.oracle"), "-Dswt.expandbar.oracle=true is not set");
		Assumptions.assumeTrue ("win32".equals (SWT.getPlatform ()), "scrolling is driven with win32 messages");
		Display display = Display.getDefault ();
		ViewportDifferential.parkCursor (display);
		int [] counts = Arrays.stream (System.getProperty ("swt.expandbar.oracle.counts", "1000,4000").split (",")).mapToInt (Integer::parseInt).toArray ();
		for (int count : counts) {
			Shell shell = new Shell (display, SWT.NO_TRIM | SWT.ON_TOP);
			shell.setBounds (40, 40, 400, 600);
			shell.setLayout (new FillLayout ());
			ExpandBar bar = new ExpandBar (shell, SWT.V_SCROLL);
			shell.open ();
			ViewportDifferential.drain (display);
			long start = System.nanoTime ();
			for (int i = 0; i < count; i++) {
				append (bar, i);
			}
			ViewportDifferential.drain (display);
			long create = (System.nanoTime () - start) / 1000;
			long [] paint = new long [7], scroll = new long [7], expand = new long [7], resize = new long [7];
			for (int k = 0; k < paint.length; k++) {
				start = System.nanoTime ();
				bar.redraw ();
				bar.update ();
				paint [k] = (System.nanoTime () - start) / 1000;
				start = System.nanoTime ();
				ViewportDifferential.send (bar, 0x115, 3, 0); // WM_VSCROLL SB_PAGEDOWN
				bar.update ();
				scroll [k] = (System.nanoTime () - start) / 1000;
				start = System.nanoTime ();
				ExpandItem first = bar.getItem (0);
				first.setExpanded (!first.getExpanded ());
				ViewportDifferential.drain (display);
				bar.update ();
				expand [k] = (System.nanoTime () - start) / 1000;
				start = System.nanoTime ();
				shell.setSize (400 + (k & 1), 600);
				ViewportDifferential.drain (display);
				bar.update ();
				resize [k] = (System.nanoTime () - start) / 1000;
			}
			/* the cost of one more item at this size (O(1) amortized when it does not grow with the count) */
			start = System.nanoTime ();
			for (int i = 0; i < APPEND; i++) {
				append (bar, count + i);
			}
			ViewportDifferential.drain (display);
			long append = (System.nanoTime () - start) / 1000 / APPEND;
			System.out.println ("expandbar-oracle " + ViewportDifferential.reference ("ExpandBar") + " items " + count + " create " + create
				+ " paint " + median (paint) + " scroll " + median (scroll) + " expand " + median (expand) + " resize " + median (resize)
				+ " append " + append);
			shell.dispose ();
		}
	}

	static final int APPEND = 200;

	/* one item as the oracle creates them: text, height 40, every tenth with a Label, every other expanded */
	static void append (ExpandBar bar, int i) {
		ExpandItem item = new ExpandItem (bar, SWT.NONE);
		item.setText ("item " + i);
		item.setHeight (40);
		if (i % 10 == 0) {
			Label label = new Label (bar, SWT.NONE);
			label.setText ("control " + i);
			item.setControl (label);
		}
		item.setExpanded ((i & 1) == 0);
	}

	static long median (long [] values) {
		long [] sorted = values.clone ();
		Arrays.sort (sorted);
		return sorted [sorted.length / 2];
	}
}
