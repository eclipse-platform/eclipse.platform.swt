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
import org.eclipse.swt.custom.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.junit.jupiter.api.*;

/**
 * Oracle of leaf L5 (docs/m3/CTABFOLDER_VIEWPORT.md): what a CTabFolder with many tabs costs per operation (create,
 * layout after a resize, paint, select a far tab, mouse move over the tabs, append one more tab), at 10^3, 10^4 and
 * 5 x 10^4 tabs. Runs
 * against any jar (official or this SWT); prints one line per size, medians in microseconds. Opt-in:
 * <code>-Dswt.ctabfolder.oracle=true</code>; sizes with <code>-Dswt.ctabfolder.oracle.counts=1000,10000</code>.
 */
public class Test_org_eclipse_swt_custom_CTabFolder_ScaleOracle {

	@Test
	public void measure () {
		Assumptions.assumeTrue (Boolean.getBoolean ("swt.ctabfolder.oracle"), "-Dswt.ctabfolder.oracle=true is not set");
		Display display = Display.getDefault ();
		ViewportDifferential.parkCursor (display);
		int [] counts = Arrays.stream (System.getProperty ("swt.ctabfolder.oracle.counts", "1000,10000,50000").split (",")).mapToInt (Integer::parseInt).toArray ();
		for (int count : counts) {
			Shell shell = new Shell (display, SWT.NO_TRIM | SWT.ON_TOP);
			shell.setBounds (40, 40, 800, 300);
			shell.setLayout (new FillLayout ());
			CTabFolder folder = new CTabFolder (shell, SWT.CLOSE | SWT.BORDER);
			shell.open ();
			ViewportDifferential.drain (display);
			long start = System.nanoTime ();
			for (int i = 0; i < count; i++) {
				new CTabItem (folder, SWT.NONE).setText ("tab " + i);
			}
			ViewportDifferential.drain (display);
			long create = (System.nanoTime () - start) / 1000;
			long [] layout = new long [7], paint = new long [7], select = new long [7], move = new long [7];
			Random random = new Random (count);
			for (int k = 0; k < layout.length; k++) {
				start = System.nanoTime ();
				shell.setSize (800 + (k & 1), 300);
				ViewportDifferential.drain (display);
				layout [k] = (System.nanoTime () - start) / 1000;
				start = System.nanoTime ();
				folder.redraw ();
				folder.update ();
				paint [k] = (System.nanoTime () - start) / 1000;
				start = System.nanoTime ();
				folder.setSelection (random.nextInt (count));
				ViewportDifferential.drain (display);
				folder.update ();
				select [k] = (System.nanoTime () - start) / 1000;
				start = System.nanoTime ();
				for (int x = 0; x < 100; x++) {
					Event event = new Event ();
					event.x = 5 + x * 7;
					event.y = 10;
					folder.notifyListeners (SWT.MouseMove, event);
				}
				move [k] = (System.nanoTime () - start) / 1000 / 100;
			}
			/* the cost of one more tab at this size, with a control (O(1) amortized when it does not grow with the count) */
			Label [] controls = new Label [APPEND];
			for (int i = 0; i < APPEND; i++) {
				controls [i] = new Label (folder, SWT.NONE);
			}
			ViewportDifferential.drain (display);
			start = System.nanoTime ();
			for (int i = 0; i < APPEND; i++) {
				CTabItem item = new CTabItem (folder, SWT.NONE);
				item.setText ("tab " + (count + i));
				item.setControl (controls [i]);
			}
			ViewportDifferential.drain (display);
			long append = (System.nanoTime () - start) / 1000 / APPEND;
			System.out.println ("ctabfolder-oracle " + ViewportDifferential.reference ("CTabFolder") + " tabs " + count + " create " + create
				+ " layout " + median (layout) + " paint " + median (paint) + " select " + median (select) + " move " + median (move)
				+ " append " + append);
			shell.dispose ();
		}
	}

	static final int APPEND = 1000;

	static long median (long [] values) {
		long [] sorted = values.clone ();
		Arrays.sort (sorted);
		return sorted [sorted.length / 2];
	}
}
