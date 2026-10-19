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
package org.eclipse.swt.tests.manual;

import org.eclipse.swt.SWT;
import org.eclipse.swt.internal.win32.OS;
import org.eclipse.swt.widgets.*;

/**
 * win32 Tree viewport baseline (docs/m3/TREE_MEDIATED_VIEWPORT.md): native peers, heap,
 * vertical bar range, SetData calls and timings per phase. A watchdog ends the run after
 * the given seconds so a phase that does not return never leaves a busy window behind.
 * <p>
 * Usage: {@code M3TreeViewportBaselineProbe <plain-flat|virtual-flat|virtual-nested> <count> [seconds]}
 * </p>
 */
public class M3TreeViewportBaselineProbe {
	static Display display;
	static int setData;
	static volatile String phase = "start";

	public static void main (String [] args) {
		String mode = args [0];
		int count = Integer.parseInt (args [1]);
		long seconds = args.length > 2 ? Long.parseLong (args [2]) : 60;
		Thread watchdog = new Thread (() -> {
			try {
				Thread.sleep (seconds * 1000);
			} catch (InterruptedException e) {
				return;
			}
			System.out.println ("TIMEOUT after " + seconds + " s in phase: " + phase);
			System.exit (3);
		}, "probe-watchdog");
		watchdog.setDaemon (true);
		watchdog.start ();
		display = new Display ();
		Shell shell = new Shell (display);
		shell.setBounds (20, 20, 320, 420);
		int style = SWT.BORDER | SWT.MULTI | (mode.startsWith ("virtual") ? SWT.VIRTUAL : 0);
		Tree tree = new Tree (shell, style);
		tree.setBounds (5, 5, 300, 380);
		tree.addListener (SWT.SetData, e -> {
			setData++;
			TreeItem item = (TreeItem) e.item;
			TreeItem parent = item.getParentItem ();
			item.setText ((parent == null ? "root " : parent.getText () + " / ") + e.index);
			if (mode.equals ("virtual-nested") && parent == null) item.setItemCount (count);
		});
		phase = "build";
		long t0 = System.nanoTime ();
		switch (mode) {
			case "plain-flat" -> {
				for (int i = 0; i < count; i++) new TreeItem (tree, 0).setText ("root " + i);
			}
			case "virtual-flat" -> tree.setItemCount (count);
			case "virtual-nested" -> tree.setItemCount (1000);
			default -> throw new IllegalArgumentException (mode);
		}
		long build = (System.nanoTime () - t0) / 1_000_000;
		shell.open ();
		drain ();
		report ("built", tree, build);
		t0 = System.nanoTime ();
		TreeItem target;
		if (mode.equals ("virtual-nested")) {
			phase = "expand root 500";
			TreeItem root = tree.getItem (500);
			root.setExpanded (true);
			drain ();
			report ("expanded root 500", tree, (System.nanoTime () - t0) / 1_000_000);
			t0 = System.nanoTime ();
			phase = "getItem (last child)";
			target = root.getItem (count - 1);
		} else {
			phase = "getItem (last)";
			target = tree.getItem (count - 1);
		}
		report ("located last", tree, (System.nanoTime () - t0) / 1_000_000);
		t0 = System.nanoTime ();
		phase = "setTopItem (last)";
		tree.setTopItem (target);
		drain ();
		report ("scrolled to last", tree, (System.nanoTime () - t0) / 1_000_000);
		t0 = System.nanoTime ();
		phase = "setTopItem (first)";
		tree.setTopItem (tree.getItem (0));
		drain ();
		report ("scrolled back to first", tree, (System.nanoTime () - t0) / 1_000_000);
		display.dispose ();
	}

	static void drain () {
		long end = System.currentTimeMillis () + 300;
		while (System.currentTimeMillis () < end) {
			if (!display.readAndDispatch ()) {
				try {
					Thread.sleep (5);
				} catch (InterruptedException e) {
					return;
				}
			}
		}
	}

	static void report (String name, Tree tree, long millis) {
		System.gc ();
		Runtime r = Runtime.getRuntime ();
		long heapMb = (r.totalMemory () - r.freeMemory ()) >> 20;
		long nativeItems = OS.SendMessage (tree.handle, OS.TVM_GETCOUNT, 0, 0);
		ScrollBar bar = tree.getVerticalBar ();
		TreeItem top = tree.getTopItem ();
		System.out.printf ("%-24s ms=%7d nativeItems=%9d heapMB=%5d setData=%8d vbar.max=%9d vbar.sel=%9d thumb=%d top=%s%n",
			name, millis, nativeItems, heapMb, setData, bar == null ? -1 : bar.getMaximum (),
			bar == null ? -1 : bar.getSelection (), bar == null ? -1 : bar.getThumb (), top == null ? "null" : top.getText ());
	}
}
