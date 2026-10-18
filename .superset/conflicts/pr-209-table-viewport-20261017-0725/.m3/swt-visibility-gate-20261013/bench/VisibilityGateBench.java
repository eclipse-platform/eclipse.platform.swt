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
		java.io.File auto = new java.io.File (System.getProperty ("bench.dumpRoot", "C:/m3-work-c/visgate/dump/auto"), scenario + "-" + label + "-" + System.currentTimeMillis ());
		dumpDir = auto.mkdirs () ? auto.getPath () : null;
		int updates = args.length > 2 ? Integer.parseInt (args [2]) : 100;
		display = new Display ();
		/*
		 * Bottom-right of the primary monitor, ON_TOP, shown without activation:
		 * off-monitor windows receive fewer WM_PAINTs and would understate stock work.
		 */
		Rectangle area = display.getPrimaryMonitor ().getClientArea ();
		originX = area.x + area.width - 1300;
		originY = area.y + area.height - 580;
		Shell twinShell = shell (740, "twin");
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
			case "stack" -> {
				/* StackLayout sizes every pane on each shell resize; only the top pane is visible */
				StackLayout stack = new StackLayout ();
				shell.setLayout (stack);
				Label top = new Label (shell, SWT.NONE);
				top.setText ("top pane");
				gated = new Payload (shell, true);
				stack.topControl = top;
				reveal = () -> {
					stack.topControl = gated.content;
					shell.layout ();
				};
				StackLayout twinStack = new StackLayout ();
				twinShell.setLayout (twinStack);
				twin = new Payload (twinShell, false);
				twinStack.topControl = twin.content;
				resizeStorm = true;
			}
			default -> {
				gated = new Payload (shell, true);
				twin = new Payload (twinShell, false);
				reveal = null;
			}
		}
		host = shell;
		clickThrough (shell);
		clickThrough (twinShell);
		shell.setVisible (true);
		twinShell.setVisible (true);
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
			cover.setVisible (true);
			settle (800);
		}

		/* Update phase: target not visible */
		reset ();
		long cpu0 = MX.getCurrentThreadCpuTime (), wall0 = System.nanoTime ();
		for (int k = 0; k < updates; k++) {
			gated.update (k);
			if (resizeStorm) {
				Point sz = shell.getSize ();
				shell.setSize (sz.x + ((k & 1) == 0 ? 1 : -1), sz.y);
			}
			pump ();
		}
		settle (200);
		long cpuUpdate = MX.getCurrentThreadCpuTime () - cpu0, wallUpdate = System.nanoTime () - wall0;
		int[] upd = snapshot ();
		int occludedShells = gateField ("visOccludedShells");

		/* Twin receives the same updates while visible */
		for (int k = 0; k < updates; k++) {
			twin.update (k);
			if (resizeStorm) {
				Point sz = twinShell.getSize ();
				twinShell.setSize (sz.x + ((k & 1) == 0 ? 1 : -1), sz.y);
			}
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
			/* Restore without activation so no keystroke of the desktop user lands in the bench */
			org.eclipse.swt.internal.win32.OS.ShowWindow (shell.handle, org.eclipse.swt.internal.win32.OS.SW_SHOWNOACTIVATE);
		} else if ("covered".equals (scenario)) {
			cover.dispose ();
		} else if (reveal != null) {
			reveal.run ();
		}
		settle (700);
		long cpuCatch = MX.getCurrentThreadCpuTime () - cpu1;
		if (Boolean.getBoolean ("bench.gateDump")) {
			gateDump ("after-reveal", gated);
		}
		int[] cat = snapshot ();
		String apiShown = gated.api ();
		/* Raw: what the screen holds after the catch-up. Fresh: after a forced full repaint of both. */
		int occludedAtCapture = gateField ("visOccludedShells");
		int foreign = foreignWindowsOver (shell) + foreignWindowsOver (twinShell);
		String raw = printHash (gated.content, "gated-raw");
		for (Control c : new Control [] {gated.content, twin.content}) {
			Point size = c.getSize ();
			c.redraw (0, 0, size.x, size.y, true);
			c.update ();
		}
		settle (100);
		String h1 = printHash (gated.content, "gated"), h2 = printHash (twin.content, "twin");
		/* Same images with the non-client border rings of BORDER children zeroed: the themed border is captured flakily by PrintWindow in stock SWT too */
		String m0 = maskedHash (gated.content, "gated-raw"), m1 = maskedHash (gated.content, "gated"), m2 = maskedHash (twin.content, "twin");

		System.out.println (String.join ("\t", scenario, label, Integer.toString (updates),
			Integer.toString (upd [0]), Integer.toString (upd [1]), Integer.toString (upd [2]), Integer.toString (upd [3]),
			Long.toString (cpuUpdate / 1_000_000), Long.toString (wallUpdate / 1_000_000),
			Integer.toString (qry [0]), Integer.toString (qry [1]), Integer.toString (qry [2]), Integer.toString (qry [3]),
			Integer.toString (cat [0]), Integer.toString (cat [1]), Integer.toString (cat [2]), Integer.toString (cat [3]),
			Long.toString (cpuCatch / 1_000_000),
			Boolean.toString (apiHidden.equals (twinApi)), Boolean.toString (apiShown.equals (twinApi)), sha (apiHidden).substring (0, 12),
			Boolean.toString (h1.equals (h2)), Boolean.toString (raw.equals (h1)), h1.substring (0, 16), h2.substring (0, 16),
			Integer.toString (occludedShells), Integer.toString (upd [4]), Integer.toString (occludedAtCapture),
			Boolean.toString (m1.equals (m2)), Boolean.toString (m0.equals (m1)), Integer.toString (foreign),
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

	static int originX, originY;
	static boolean resizeStorm;
	static String dumpDir;

	static Shell shell (int x, String title) {
		Shell s = new Shell (display, SWT.SHELL_TRIM | SWT.ON_TOP);
		s.setText (title);
		s.setLayout (new FillLayout ());
		s.setBounds (originX + x - 40, originY, 560, 560);
		return s;
	}

	/**
	 * Mouse and wheel input pass through the bench shells (WS_EX_LAYERED at alpha 255
	 * plus WS_EX_TRANSPARENT), so the desktop user cannot scroll or click them.
	 */
	static void clickThrough (Shell s) {
		long h = s.handle;
		int ex = org.eclipse.swt.internal.win32.OS.GetWindowLong (h, org.eclipse.swt.internal.win32.OS.GWL_EXSTYLE);
		org.eclipse.swt.internal.win32.OS.SetWindowLong (h, org.eclipse.swt.internal.win32.OS.GWL_EXSTYLE,
			ex | org.eclipse.swt.internal.win32.OS.WS_EX_LAYERED | org.eclipse.swt.internal.win32.OS.WS_EX_TRANSPARENT);
		org.eclipse.swt.internal.win32.OS.SetLayeredWindowAttributes (h, 0, (byte) 0xFF, org.eclipse.swt.internal.win32.OS.LWA_ALPHA);
	}

	/** Visible top-level windows of other threads above the shell in z-order that intersect it (screen captures are then obstructed). */
	static int foreignWindowsOver (Shell s) {
		org.eclipse.swt.internal.win32.RECT mine = new org.eclipse.swt.internal.win32.RECT (), other = new org.eclipse.swt.internal.win32.RECT ();
		org.eclipse.swt.internal.win32.OS.GetWindowRect (s.handle, mine);
		int [] pid = new int [1];
		int self = org.eclipse.swt.internal.win32.OS.GetWindowThreadProcessId (s.handle, pid);
		int count = 0;
		long h = org.eclipse.swt.internal.win32.OS.GetWindow (org.eclipse.swt.internal.win32.OS.GetDesktopWindow (), org.eclipse.swt.internal.win32.OS.GW_CHILD);
		while (h != 0 && h != s.handle) {
			if (org.eclipse.swt.internal.win32.OS.IsWindowVisible (h) && !org.eclipse.swt.internal.win32.OS.IsIconic (h)
					&& org.eclipse.swt.internal.win32.OS.GetWindowThreadProcessId (h, pid) != self
					&& (org.eclipse.swt.internal.win32.OS.GetWindowLong (h, org.eclipse.swt.internal.win32.OS.GWL_EXSTYLE) & org.eclipse.swt.internal.win32.OS.WS_EX_TRANSPARENT) == 0) {
				org.eclipse.swt.internal.win32.OS.GetWindowRect (h, other);
				if (other.left < mine.right && other.right > mine.left && other.top < mine.bottom && other.bottom > mine.top) {
					count++;
				}
			}
			h = org.eclipse.swt.internal.win32.OS.GetWindow (h, org.eclipse.swt.internal.win32.OS.GW_HWNDNEXT);
		}
		return count;
	}

	static void reset () {
		paints = layouts = measures = setData = 0;
	}

	static int [] snapshot () {
		return new int [] {paints, layouts, measures, setData, gateField ("visDirtyCount")};
	}

	/** Diagnostic: the gate's deferred list and the payload controls' state bits 25..31. */
	static void gateDump (String when, Payload p) {
		try {
			java.lang.reflect.Field list = Display.class.getDeclaredField ("visDirty");
			java.lang.reflect.Field count = Display.class.getDeclaredField ("visDirtyCount");
			java.lang.reflect.Field state = Class.forName ("org.eclipse.swt.widgets.Widget").getDeclaredField ("state");
			list.setAccessible (true);
			count.setAccessible (true);
			state.setAccessible (true);
			Object [] entries = (Object []) list.get (display);
			StringBuilder sb = new StringBuilder (when).append (" dirty=").append (count.getInt (display));
			for (int i = 0; i < count.getInt (display); i++) {
				sb.append (' ').append (entries [i] == null ? "null" : entries [i].getClass ().getSimpleName () + ":" + Integer.toHexString (state.getInt (entries [i]) >>> 25));
			}
			for (Control c : new Control [] {p.content.getShell (), p.content, p.label, p.table, p.text, p.canvas}) {
				sb.append (" | ").append (c.getClass ().getSimpleName ()).append (':').append (Integer.toHexString (state.getInt (c) >>> 25));
			}
			System.err.println (sb);
		} catch (ReflectiveOperationException e) {
			System.err.println (when + " gateDump unavailable: " + e);
		}
	}

	/** Gate state read reflectively (diagnostic only); -1 on stock classes. */
	static int gateField (String name) {
		try {
			java.lang.reflect.Field f = Display.class.getDeclaredField (name);
			f.setAccessible (true);
			return f.getInt (display);
		} catch (ReflectiveOperationException | RuntimeException e) {
			return -1;
		}
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

	static final java.util.Map<String, ImageData> CAPTURED = new java.util.HashMap<> ();

	/** SHA-256 of a captured image with every BORDER child's border ring set to pixel 0. */
	static String maskedHash (Composite content, String name) throws Exception {
		ImageData data = (ImageData) CAPTURED.get (name).clone ();
		for (Control c : content.getChildren ()) {
			int bw = c.getBorderWidth ();
			if (bw <= 0) {
				continue;
			}
			Rectangle b = c.getBounds ();
			for (int y = Math.max (0, b.y); y < Math.min (data.height, b.y + b.height); y++) {
				for (int x = Math.max (0, b.x); x < Math.min (data.width, b.x + b.width); x++) {
					boolean ring = y < b.y + bw || y >= b.y + b.height - bw || x < b.x + bw || x >= b.x + b.width - bw;
					if (ring) {
						data.setPixel (x, y, 0);
					}
				}
			}
		}
		/* Windows 11 rounds the shell's bottom corners over a client-filling child: those pixels show the desktop */
		for (int y = Math.max (0, data.height - 8); y < data.height; y++) {
			for (int x = 0; x < Math.min (8, data.width); x++) {
				data.setPixel (x, y, 0);
				data.setPixel (data.width - 1 - x, y, 0);
			}
		}
		MessageDigest md = MessageDigest.getInstance ("SHA-256");
		md.update (data.data);
		StringBuilder hex = new StringBuilder ();
		for (byte v : md.digest ()) {
			hex.append (String.format ("%02x", v & 0xff));
		}
		return hex.toString ();
	}

	/** Common visible size: the gated control's part inside every ancestor client area (the twin is cropped to it). */
	static Point visible;

	static Point visibleSize (Control control) {
		if (visible != null) {
			return visible;
		}
		Point at = control.toDisplay (0, 0);
		Point size = control.getSize ();
		Rectangle r = new Rectangle (at.x, at.y, size.x, size.y);
		for (Composite p = control.getParent (); p != null; p = p instanceof Shell ? null : p.getParent ()) {
			Rectangle client = p.getClientArea ();
			Point origin = p.toDisplay (client.x, client.y);
			r = r.intersection (new Rectangle (origin.x, origin.y, client.width, client.height));
		}
		visible = new Point (Math.max (1, r.width), Math.max (1, r.height));
		return visible;
	}

	static String sha (String text) throws Exception {
		MessageDigest md = MessageDigest.getInstance ("SHA-256");
		StringBuilder hex = new StringBuilder ();
		for (byte b : md.digest (text.getBytes (java.nio.charset.StandardCharsets.UTF_8))) {
			hex.append (String.format ("%02x", b & 0xff));
		}
		return hex.toString ();
	}

	static String printHash (Control control, String name) throws Exception {
		/*
		 * Screen capture of what the user sees (the shells are ON_TOP on the primary
		 * monitor). Control.print (PrintWindow PW_RENDERFULLCONTENT) returned black
		 * client areas intermittently in stock and gated runs alike.
		 */
		Point size = visibleSize (control);
		Point at = control.toDisplay (0, 0);
		Image image = new Image (display, size.x, size.y);
		GC screen = new GC (display);
		screen.copyArea (image, at.x, at.y);
		screen.dispose ();
		ImageData data = image.getImageData ();
		image.dispose ();
		CAPTURED.put (name, data);
		String dump = System.getProperty ("bench.dump", dumpDir);
		if (dump != null) {
			ImageLoader loader = new ImageLoader ();
			loader.data = new ImageData [] {data};
			loader.save (dump + "/" + name + ".png", SWT.IMAGE_PNG);
		}
		MessageDigest md = MessageDigest.getInstance ("SHA-256");
		md.update (data.data);
		StringBuilder hex = new StringBuilder ();
		for (byte b : md.digest ()) {
			hex.append (String.format ("%02x", b & 0xff));
		}
		return hex.toString ();
	}
}
