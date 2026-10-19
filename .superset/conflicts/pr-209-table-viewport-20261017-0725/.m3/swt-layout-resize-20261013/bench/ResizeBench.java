/*******************************************************************************
 * Copyright (c) 2026 Contributors to the Eclipse Foundation.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
import java.lang.management.*;
import java.util.*;

import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.examples.controlexample.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

/**
 * Leaf 3 resize bench (public API only, runs on stock and gated classes).
 * <ul>
 * <li>{@code control}: ControlExample in a shell, live-resize sweep of N frames (width +-1 px
 * per frame, then height): Resize events, Move events and Paint events per frame, frame
 * time p50/p95 (setSize + drain).</li>
 * <li>{@code wrap}: ScrolledComposite (V_SCROLL, expandHorizontal) around a wrapping label,
 * min size recomputed in a resize listener (SWT snippet pattern); sweep the width 1 px at a
 * time across the scrollbar threshold: vertical scrollbar visibility toggles per step
 * (more than 1 per step = oscillation), max toggles in one step.</li>
 * <li>{@code stack}: StackLayout with 8 heavy panes, switch topControl N times: Resize events
 * and paints per switch, ms per switch p50/p95.</li>
 * </ul>
 * Final-state parity: prints a hash of every control's bounds and client area.
 * <pre>java ResizeBench &lt;control|wrap|stack&gt; &lt;label&gt; [frames]</pre>
 */
public class ResizeBench {
	static int resizes, moves, paints;

	public static void main (String [] args) throws Exception {
		String scenario = args [0], label = args [1];
		int frames = args.length > 2 ? Integer.parseInt (args [2]) : 200;
		Display display = new Display ();
		display.addFilter (SWT.Resize, e -> resizes++);
		display.addFilter (SWT.Move, e -> moves++);
		display.addFilter (SWT.Paint, e -> paints++);
		Rectangle area = display.getPrimaryMonitor ().getClientArea ();
		Shell shell = new Shell (display, SWT.SHELL_TRIM | SWT.ON_TOP);
		shell.setLayout (new FillLayout ());
		String result;
		switch (scenario) {
			case "wrap" -> result = wrap (display, shell, area, frames);
			case "stack" -> result = stack (display, shell, area, frames);
			default -> result = control (display, shell, area, frames);
		}
		System.out.println (String.join ("\t", scenario, label, Integer.toString (frames), result, Integer.toHexString (treeHash (shell)),
			System.getProperty ("swt.visibility.gate", "default")));
		display.dispose ();
	}

	static String control (Display display, Shell shell, Rectangle area, int frames) throws Exception {
		new ControlExample (shell);
		shell.setBounds (area.x + area.width - 1010, area.y + area.height - 760, 980, 740);
		show (shell);
		settle (display, 500);
		long [] times = new long [frames];
		int [] r = new int [frames], m = new int [frames], p = new int [frames];
		Point base = shell.getSize ();
		for (int f = 0; f < frames; f++) {
			resizes = moves = paints = 0;
			long t0 = System.nanoTime ();
			int delta = (f % 40) - 20;
			if (f < frames / 2) {
				shell.setSize (base.x + delta, base.y);
			} else {
				shell.setSize (base.x, base.y + delta);
			}
			drain (display);
			times [f] = System.nanoTime () - t0;
			r [f] = resizes;
			m [f] = moves;
			p [f] = paints;
		}
		shell.setSize (base);
		settle (display, 300);
		return stats (times, r, m, p);
	}

	static String wrap (Display display, Shell shell, Rectangle area, int frames) throws Exception {
		ScrolledComposite sc = new ScrolledComposite (shell, SWT.V_SCROLL);
		Label text = new Label (sc, SWT.WRAP);
		StringBuilder sb = new StringBuilder ();
		for (int i = 0; i < 60; i++) {
			sb.append ("wrapping word ").append (i).append (' ');
		}
		text.setText (sb.toString ());
		sc.setContent (text);
		sc.setExpandHorizontal (true);
		sc.setExpandVertical (true);
		sc.addListener (SWT.Resize, e -> {
			Rectangle client = sc.getClientArea ();
			sc.setMinSize (text.computeSize (client.width, SWT.DEFAULT));
		});
		shell.setBounds (area.x + area.width - 520, area.y + area.height - 400, 300, 180);
		show (shell);
		settle (display, 300);
		int toggles = 0, maxPerStep = 0, oscillatingSteps = 0;
		ScrollBar bar = sc.getVerticalBar ();
		long [] times = new long [frames];
		for (int f = 0; f < frames; f++) {
			boolean before = bar.getVisible ();
			int stepToggles = 0;
			long t0 = System.nanoTime ();
			shell.setSize (200 + f, 180);
			for (int k = 0; k < 6; k++) {
				drain (display);
				boolean now = bar.getVisible ();
				if (now != before) {
					stepToggles++;
					before = now;
				}
			}
			times [f] = System.nanoTime () - t0;
			toggles += stepToggles;
			maxPerStep = Math.max (maxPerStep, stepToggles);
			if (stepToggles > 1) {
				oscillatingSteps++;
			}
		}
		Arrays.sort (times);
		return toggles + "\t" + maxPerStep + "\t" + oscillatingSteps + "\t" + times [frames / 2] / 1000 + "\t" + times [frames * 95 / 100] / 1000;
	}

	static String stack (Display display, Shell shell, Rectangle area, int frames) throws Exception {
		StackLayout stack = new StackLayout ();
		shell.setLayout (stack);
		Composite [] panes = new Composite [8];
		for (int i = 0; i < panes.length; i++) {
			panes [i] = new Composite (shell, SWT.NONE);
			panes [i].setLayout (new GridLayout (4, false));
			for (int k = 0; k < 120; k++) {
				new Button (panes [i], SWT.PUSH).setText ("pane " + i + " button " + k);
			}
		}
		stack.topControl = panes [0];
		shell.setBounds (area.x + area.width - 920, area.y + area.height - 700, 900, 680);
		show (shell);
		settle (display, 500);
		long [] times = new long [frames];
		int [] r = new int [frames], m = new int [frames], p = new int [frames];
		for (int f = 0; f < frames; f++) {
			resizes = moves = paints = 0;
			long t0 = System.nanoTime ();
			stack.topControl = panes [(f + 1) % panes.length];
			shell.layout ();
			drain (display);
			times [f] = System.nanoTime () - t0;
			r [f] = resizes;
			m [f] = moves;
			p [f] = paints;
		}
		return stats (times, r, m, p);
	}

	static String stats (long [] times, int [] r, int [] m, int [] p) {
		long [] sorted = times.clone ();
		Arrays.sort (sorted);
		int n = times.length;
		return sum (r) * 100 / n + "\t" + sum (m) * 100 / n + "\t" + sum (p) * 100 / n + "\t" + sorted [n / 2] / 1000 + "\t" + sorted [n * 95 / 100] / 1000;
	}

	static int sum (int [] values) {
		int s = 0;
		for (int v : values) {
			s += v;
		}
		return s;
	}

	/** Bounds and client areas of every control, order-dependent. */
	static int treeHash (Control c) {
		int h = c.getBounds ().hashCode ();
		if (c instanceof Scrollable s) {
			h = h * 31 + s.getClientArea ().hashCode ();
		}
		if (c instanceof Composite composite) {
			for (Control child : composite.getChildren ()) {
				h = h * 31 + treeHash (child);
			}
		}
		return h;
	}

	static void show (Shell shell) {
		long h = shell.handle;
		int ex = org.eclipse.swt.internal.win32.OS.GetWindowLong (h, org.eclipse.swt.internal.win32.OS.GWL_EXSTYLE);
		org.eclipse.swt.internal.win32.OS.SetWindowLong (h, org.eclipse.swt.internal.win32.OS.GWL_EXSTYLE,
			ex | org.eclipse.swt.internal.win32.OS.WS_EX_LAYERED | org.eclipse.swt.internal.win32.OS.WS_EX_TRANSPARENT);
		org.eclipse.swt.internal.win32.OS.SetLayeredWindowAttributes (h, 0, (byte) 0xFF, org.eclipse.swt.internal.win32.OS.LWA_ALPHA);
		shell.setVisible (true);
	}

	static void drain (Display display) {
		while (display.readAndDispatch ()) {
			// drain
		}
	}

	static void settle (Display display, int ms) throws InterruptedException {
		long end = System.currentTimeMillis () + ms;
		while (System.currentTimeMillis () < end) {
			drain (display);
			Thread.sleep (10);
		}
	}
}
