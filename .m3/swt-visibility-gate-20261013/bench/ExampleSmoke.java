/*******************************************************************************
 * Copyright (c) 2026 Contributors to the Eclipse Foundation.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
import java.lang.management.*;

import org.eclipse.swt.*;
import org.eclipse.swt.examples.controlexample.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

/**
 * SWT example harness for the visibility gate: builds ControlExample or
 * CustomControlExample, runs an update storm on every tab page (all hidden but
 * one), then tours every tab once. Counts Paint and Resize events through
 * Display filters, UI-thread CPU, and exceptions.
 * <pre>java ExampleSmoke &lt;control|custom&gt; &lt;label&gt; [rounds]</pre>
 */
public class ExampleSmoke {
	static int paints, resizes;

	public static void main (String [] args) throws Exception {
		String which = args [0], label = args [1];
		int rounds = args.length > 2 ? Integer.parseInt (args [2]) : 10;
		Display display = new Display ();
		display.addFilter (SWT.Paint, e -> paints++);
		display.addFilter (SWT.Resize, e -> resizes++);
		Rectangle area = display.getPrimaryMonitor ().getClientArea ();
		Shell shell = new Shell (display, SWT.SHELL_TRIM | SWT.ON_TOP);
		shell.setLayout (new FillLayout ());
		Object example = "custom".equals (which) ? new CustomControlExample (shell) : new ControlExample (shell);
		shell.setBounds (area.x + area.width - 1000, area.y + area.height - 760, 980, 740);
		long h = shell.handle;
		int ex = org.eclipse.swt.internal.win32.OS.GetWindowLong (h, org.eclipse.swt.internal.win32.OS.GWL_EXSTYLE);
		org.eclipse.swt.internal.win32.OS.SetWindowLong (h, org.eclipse.swt.internal.win32.OS.GWL_EXSTYLE,
			ex | org.eclipse.swt.internal.win32.OS.WS_EX_LAYERED | org.eclipse.swt.internal.win32.OS.WS_EX_TRANSPARENT);
		org.eclipse.swt.internal.win32.OS.SetLayeredWindowAttributes (h, 0, (byte) 0xFF, org.eclipse.swt.internal.win32.OS.LWA_ALPHA);
		shell.setVisible (true);
		settle (display, 500);
		java.lang.reflect.Field field = ControlExample.class.getDeclaredField ("tabFolder");
		field.setAccessible (true);
		TabFolder folder = (TabFolder) field.get (example);
		TabItem [] items = folder.getItems ();
		ThreadMXBean mx = ManagementFactory.getThreadMXBean ();
		int errors = 0;

		/* Storm: every page (one visible, the rest hidden) relayouts and redraws */
		paints = resizes = 0;
		long cpu0 = mx.getCurrentThreadCpuTime ();
		for (int r = 0; r < rounds; r++) {
			for (TabItem item : items) {
				Control page = item.getControl ();
				try {
					if (page instanceof Composite composite) {
						composite.layout (true, true);
					}
					page.redraw ();
				} catch (RuntimeException e) {
					errors++;
				}
			}
			while (display.readAndDispatch ()) {
				// drain
			}
		}
		settle (display, 200);
		long stormCpu = mx.getCurrentThreadCpuTime () - cpu0;
		int stormPaints = paints, stormResizes = resizes;

		/* Tour: show every tab once */
		paints = resizes = 0;
		long cpu1 = mx.getCurrentThreadCpuTime ();
		for (int i = 0; i < items.length; i++) {
			try {
				folder.setSelection (i);
				settle (display, 60);
			} catch (RuntimeException e) {
				errors++;
			}
		}
		long tourCpu = mx.getCurrentThreadCpuTime () - cpu1;
		System.out.println (String.join ("\t", which, label, Integer.toString (items.length), Integer.toString (rounds),
			Integer.toString (stormPaints), Integer.toString (stormResizes), Long.toString (stormCpu / 1_000_000),
			Integer.toString (paints), Integer.toString (resizes), Long.toString (tourCpu / 1_000_000),
			Integer.toString (errors), System.getProperty ("swt.visibility.gate", "default")));
		display.dispose ();
	}

	static void settle (Display display, int ms) throws InterruptedException {
		long end = System.currentTimeMillis () + ms;
		while (System.currentTimeMillis () < end) {
			while (display.readAndDispatch ()) {
				// drain
			}
			Thread.sleep (10);
		}
	}
}
