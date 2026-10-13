/*******************************************************************************
 * Copyright (c) 2026 Contributors to the Eclipse Foundation.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
import java.lang.management.*;
import java.security.*;

import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

/**
 * Visibility-gate proof harness. One scenario per JVM; public SWT API only, so
 * the same source runs against stock and gated classes.
 * <pre>
 * java -cp classes;bench VisibilityGateBench &lt;tab|minimized|scrolled|covered&gt; &lt;label&gt; [updates]
 * </pre>
 * Prints one TSV row: update-phase counters (work done while the target is not
 * visible), query-phase counters (public API on the hidden target), catch-up
 * counters, CPU, and parity (print SHA-256 and API values vs a twin that was
 * never hidden).
 */
public class VisibilityGateBench {
	static int paints, layouts, measures, setData;

	static final class CountingLayout extends Layout {
		@Override
		protected Point computeSize (Composite composite, int wHint, int hHint, boolean flushCache) {
			measures++;
			return invoke (composite, wHint, hHint, flushCache);
		}
		@Override
		protected void layout (Composite composite, boolean flushCache) {
			layouts++;
			Rectangle area = composite.getClientArea ();
			Control [] children = composite.getChildren ();
			int y = area.y;
			for (Control child : children) {
				Point size = child.computeSize (area.width, SWT.DEFAULT, flushCache);
				int h = child instanceof Table || child instanceof StyledText ? Math.max (60, (area.height - 80) / 2) : size.y;
				child.setBounds (area.x, y, area.width, h);
				y += h;
			}
		}
		Point invoke (Composite composite, int wHint, int hHint, boolean flush) {
			int w = 0, h = 0;
			for (Control child : composite.getChildren ()) {
				Point size = child.computeSize (wHint, SWT.DEFAULT, flush);
				w = Math.max (w, size.x);
				h += size.y;
			}
			return new Point (wHint == SWT.DEFAULT ? w : wHint, hHint == SWT.DEFAULT ? h : hHint);
		}
	}

	/** The gated payload: Table (virtual, 1M rows), StyledText (large), Canvas, Label. */
	static final class Payload {
		Composite content;
		Table table;
		StyledText text;
		Canvas canvas;
		Label label;

		Payload (Composite parent, boolean count) {
			content = new Composite (parent, SWT.NONE);
			content.setLayout (new CountingLayout ());
			label = new Label (content, SWT.NONE);
			label.setText ("update -1");
			table = new Table (content, SWT.VIRTUAL | SWT.BORDER);
			table.addListener (SWT.SetData, e -> {
				if (count) {
					setData++;
				}
				((TableItem) e.item).setText ("row " + e.index);
			});
			table.setItemCount (1_000_000);
			text = new StyledText (content, SWT.BORDER | SWT.V_SCROLL);
			StringBuilder sb = new StringBuilder (50_000 * 48);
			for (int i = 0; i < 50_000; i++) {
				sb.append ("line ").append (i).append (" the quick brown fox jumps over the dog\n");
			}
			text.setText (sb.toString ());
			canvas = new Canvas (content, SWT.NONE);
			canvas.addListener (SWT.Paint, e -> {
				Rectangle r = canvas.getClientArea ();
				for (int i = 0; i < 40; i++) {
					e.gc.drawLine (r.x, r.y + i, r.x + r.width, r.y + r.height - i);
				}
			});
			if (count) {
				Listener paint = e -> paints++;
				for (Control c : new Control [] {content, table, text, canvas}) {
					c.addListener (SWT.Paint, paint);
				}
			}
		}

		void update (int k) {
			table.clearAll ();
			text.replaceTextRange (0, 1, (k & 1) == 0 ? "a" : "b");
			canvas.redraw ();
			label.setText ("update " + k);
			content.layout (true, true);
			label.requestLayout ();
		}

		String api () {
			StringBuilder sb = new StringBuilder ();
			sb.append (content.getSize ()).append (content.getClientArea ());
			for (Control c : content.getChildren ()) {
				sb.append (c.getBounds ());
			}
			sb.append (content.computeSize (SWT.DEFAULT, SWT.DEFAULT));
			sb.append (table.getItemCount ()).append (',').append (table.getTopIndex ());
			sb.append (text.getCharCount ()).append (',').append (text.getLineCount ()).append (',').append (text.getTopIndex ());
			sb.append (label.getText ()).append (label.getBounds ());
			sb.append (label.toDisplay (0, 0).y - content.toDisplay (0, 0).y);
			return sb.toString ();
		}
	}

	static Display display;
	static final ThreadMXBean MX = ManagementFactory.getThreadMXBean ();

	public static void main (String [] args) throws Exception {
		String scenario = args [0], label = args [1];
		int updates = args.length > 2 ? Integer.parseInt (args [2]) : 100;
		display = new Display ();
		/* Off every monitor: no user input can reach the shells and nothing pops over the desktop */
		Rectangle all = display.getMonitors () [0].getBounds ();
		for (Monitor m : display.getMonitors ()) {
			all = all.union (m.getBounds ());
		}
		originX = all.x + all.width + 400;
		Shell twinShell = shell (620, "twin");
		Payload twin;
		Shell shell = shell (40, "gated");
		Payload gated;
		Runnable reveal;
		Composite host;
		switch (scenario) {
			case "tab" -> {
				CTabFolder folder = new CTabFolder (shell, SWT.BORDER);
				CTabItem first = new CTabItem (folder, SWT.NONE);
				first.setText ("visible");
				Label filler = new Label (folder, SWT.NONE);
				filler.setText ("selected tab");
				first.setControl (filler);
				CTabItem second = new CTabItem (folder, SWT.NONE);
				second.setText ("hidden");
				gated = new Payload (folder, true);
				second.setControl (gated.content);
				folder.setSelection (first);
				reveal = () -> folder.setSelection (second);
				CTabFolder twinFolder = new CTabFolder (twinShell, SWT.BORDER);
				CTabItem t1 = new CTabItem (twinFolder, SWT.NONE);
				t1.setText ("visible");
				Label twinFiller = new Label (twinFolder, SWT.NONE);
				twinFiller.setText ("selected tab");
				t1.setControl (twinFiller);
				CTabItem t2 = new CTabItem (twinFolder, SWT.NONE);
				t2.setText ("hidden");
				twin = new Payload (twinFolder, false);
				t2.setControl (twin.content);
				twinFolder.setSelection (t2);
			}
			case "scrolled" -> {
				ScrolledComposite sc = new ScrolledComposite (shell, SWT.V_SCROLL);
				Composite column = new Composite (sc, SWT.NONE);
				column.setLayout (null);
				Label top = new Label (column, SWT.NONE);
				top.setText ("top of a tall column");
				top.setBounds (0, 0, 300, 20);
				gated = new Payload (column, true);
				column.setSize (560, 4000);
				gated.content.setBounds (0, 3000, 560, 520);
				sc.setContent (column);
				reveal = () -> sc.setOrigin (0, 3000);
				twin = new Payload (twinShell, false);
				twinShell.setLayout (null);
				twin.content.setBounds (0, 0, 560, 520);
				twinShell.addListener (SWT.Resize, e -> twin.content.setBounds (0, 0, 560, 520));
			}
			default -> {
				gated = new Payload (shell, true);
				twin = new Payload (twinShell, false);
				reveal = null;
			}
		}
		host = shell;
		shell.open ();
		twinShell.open ();
		settle (300);
		Shell cover = null;
		if ("minimized".equals (scenario)) {
			shell.setMinimized (true);
			settle (500);
		} else if ("covered".equals (scenario)) {
			cover = new Shell (display, SWT.ON_TOP | SWT.TITLE);
			Rectangle b = shell.getBounds ();
			cover.setBounds (b.x - 20, b.y - 20, b.width + 40, b.height + 40);
			cover.setBackground (display.getSystemColor (SWT.COLOR_DARK_GRAY));
			cover.open ();
			settle (800);
		}

		/* Update phase: target not visible */
		reset ();
		long cpu0 = MX.getCurrentThreadCpuTime (), wall0 = System.nanoTime ();
		for (int k = 0; k < updates; k++) {
			gated.update (k);
			pump ();
		}
		settle (200);
		long cpuUpdate = MX.getCurrentThreadCpuTime () - cpu0, wallUpdate = System.nanoTime () - wall0;
		int[] upd = snapshot ();

		/* Twin receives the same updates while visible */
		for (int k = 0; k < updates; k++) {
			twin.update (k);
			pump ();
		}
		settle (200);

		/* Query phase: public API on the hidden target computes on demand */
		reset ();
		String apiHidden = gated.api ();
		String twinApi = twin.api ();
		int[] qry = snapshot ();

		/* Reveal and catch up */
		reset ();
		long cpu1 = MX.getCurrentThreadCpuTime ();
		if ("minimized".equals (scenario)) {
			shell.setMinimized (false);
		} else if ("covered".equals (scenario)) {
			cover.dispose ();
		} else if (reveal != null) {
			reveal.run ();
		}
		settle (700);
		long cpuCatch = MX.getCurrentThreadCpuTime () - cpu1;
		int[] cat = snapshot ();
		String apiShown = gated.api ();
		String h1 = printHash (gated.content), h2 = printHash (twin.content);

		System.out.println (String.join ("\t", scenario, label, Integer.toString (updates),
			Integer.toString (upd [0]), Integer.toString (upd [1]), Integer.toString (upd [2]), Integer.toString (upd [3]),
			Long.toString (cpuUpdate / 1_000_000), Long.toString (wallUpdate / 1_000_000),
			Integer.toString (qry [0]), Integer.toString (qry [1]), Integer.toString (qry [2]), Integer.toString (qry [3]),
			Integer.toString (cat [0]), Integer.toString (cat [1]), Integer.toString (cat [2]), Integer.toString (cat [3]),
			Long.toString (cpuCatch / 1_000_000),
			Boolean.toString (apiHidden.equals (twinApi)), Boolean.toString (apiShown.equals (twinApi)), sha (apiHidden).substring (0, 12),
			Boolean.toString (h1.equals (h2)), h1.substring (0, 16), h2.substring (0, 16),
			System.getProperty ("swt.visibility.gate", "default")));
		if (!apiShown.equals (twinApi) || (!apiHidden.equals (twinApi) && Boolean.getBoolean ("bench.verbose"))) {
			System.err.println ("API hidden: " + apiHidden);
			System.err.println ("API shown : " + apiShown);
			System.err.println ("API twin  : " + twinApi);
		}
		if (host != null) {
			display.dispose ();
		}
	}

	static int originX;

	static Shell shell (int x, String title) {
		Shell s = new Shell (display, SWT.SHELL_TRIM | SWT.ON_TOP);
		s.setText (title);
		s.setLayout (new FillLayout ());
		s.setBounds (originX + x, 40, 560, 560);
		return s;
	}

	static void reset () {
		paints = layouts = measures = setData = 0;
	}

	static int [] snapshot () {
		return new int [] {paints, layouts, measures, setData};
	}

	static void pump () {
		while (display.readAndDispatch ()) {
			// drain
		}
	}

	static void settle (int ms) throws InterruptedException {
		long end = System.currentTimeMillis () + ms;
		while (System.currentTimeMillis () < end) {
			pump ();
			Thread.sleep (10);
		}
		pump ();
	}

	static String sha (String text) throws Exception {
		MessageDigest md = MessageDigest.getInstance ("SHA-256");
		StringBuilder hex = new StringBuilder ();
		for (byte b : md.digest (text.getBytes (java.nio.charset.StandardCharsets.UTF_8))) {
			hex.append (String.format ("%02x", b & 0xff));
		}
		return hex.toString ();
	}

	static String printHash (Control control) throws Exception {
		Point size = control.getSize ();
		Image image = new Image (display, size.x, size.y);
		GC gc = new GC (image);
		control.print (gc);
		gc.dispose ();
		ImageData data = image.getImageData ();
		image.dispose ();
		MessageDigest md = MessageDigest.getInstance ("SHA-256");
		md.update (data.data);
		StringBuilder hex = new StringBuilder ();
		for (byte b : md.digest ()) {
			hex.append (String.format ("%02x", b & 0xff));
		}
		return hex.toString ();
	}
}
