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
 * Oracle of leaf L10 (docs/m3/TEXT_VIEWPORT.md): what a multi-line Text with many lines costs per operation (setText,
 * getLineCount, getText, append, scroll to the middle, caret line, select the end, paint, one line typed at the end).
 * Measure only: in the viewport mode the native EDIT control keeps its own lines unless this shows a cost that grows
 * with the document on SWT's side. Runs against any jar; prints one line per size, medians in microseconds. Opt-in:
 * <code>-Dswt.text.oracle=true</code>; sizes with <code>-Dswt.text.oracle.counts=10000,100000,1000000</code>.
 */
public class Test_org_eclipse_swt_widgets_Text_ScaleOracle {

	@Test
	public void measure () {
		Assumptions.assumeTrue (Boolean.getBoolean ("swt.text.oracle"), "-Dswt.text.oracle=true is not set");
		Display display = Display.getDefault ();
		ViewportDifferential.parkCursor (display);
		int [] counts = Arrays.stream (System.getProperty ("swt.text.oracle.counts", "10000,100000,1000000").split (",")).mapToInt (Integer::parseInt).toArray ();
		for (int count : counts) {
			Shell shell = new Shell (display, SWT.NO_TRIM | SWT.ON_TOP);
			shell.setBounds (40, 40, 600, 400);
			shell.setLayout (new FillLayout ());
			Text text = new Text (shell, SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL);
			text.setTextLimit (Text.LIMIT);
			shell.open ();
			ViewportDifferential.drain (display);
			StringBuilder content = new StringBuilder (count * 12);
			for (int i = 0; i < count; i++) {
				content.append ("line ").append (i).append ('\n');
			}
			String value = content.toString ();
			long [] set = new long [3], lines = new long [7], get = new long [3], append = new long [7], top = new long [7], caret = new long [7],
				end = new long [7], paint = new long [7], type = new long [7];
			for (int k = 0; k < set.length; k++) {
				long start = System.nanoTime ();
				text.setText (value);
				ViewportDifferential.drain (display);
				set [k] = (System.nanoTime () - start) / 1000;
				start = System.nanoTime ();
				int length = text.getText ().length ();
				get [k] = (System.nanoTime () - start) / 1000;
				Assertions.assertTrue (length >= value.length ());
			}
			for (int k = 0; k < lines.length; k++) {
				long start = System.nanoTime ();
				int lineCount = text.getLineCount ();
				lines [k] = (System.nanoTime () - start) / 1000;
				Assertions.assertTrue (lineCount >= count);
				start = System.nanoTime ();
				text.setTopIndex (count / 2 + k);
				ViewportDifferential.drain (display);
				text.update ();
				top [k] = (System.nanoTime () - start) / 1000;
				start = System.nanoTime ();
				text.getCaretLineNumber ();
				caret [k] = (System.nanoTime () - start) / 1000;
				start = System.nanoTime ();
				text.setSelection (text.getCharCount ());
				end [k] = (System.nanoTime () - start) / 1000;
				start = System.nanoTime ();
				text.redraw ();
				text.update ();
				paint [k] = (System.nanoTime () - start) / 1000;
				start = System.nanoTime ();
				text.append ("appended " + k + "\n");
				ViewportDifferential.drain (display);
				append [k] = (System.nanoTime () - start) / 1000;
				start = System.nanoTime ();
				text.insert ("typed " + k);
				ViewportDifferential.drain (display);
				type [k] = (System.nanoTime () - start) / 1000;
			}
			System.out.println ("text-oracle " + ViewportDifferential.reference ("Text") + " lines " + count + " setText " + median (set)
				+ " getText " + median (get) + " getLineCount " + median (lines) + " setTopIndex " + median (top) + " caretLine " + median (caret)
				+ " selectEnd " + median (end) + " paint " + median (paint) + " append " + median (append) + " insert " + median (type));
			shell.dispose ();
		}
	}

	static long median (long [] values) {
		long [] sorted = values.clone ();
		Arrays.sort (sorted);
		return sorted [sorted.length / 2];
	}
}
