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
import java.io.*;
import java.nio.file.*;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.gpu.*;
import org.eclipse.swt.widgets.*;

/**
 * Pixel probe: paints a deterministic GC scene on a Canvas in the current
 * -Dswt.gpu mode, captures the presented screen pixels and writes a PPM.
 * usage: GpuPlanesProbe <out.ppm> [scene: shapes|text|scroll|overlay]
 */
public class GpuPlanesProbe {
	static final int W = 640, H = 420;

	public static void main (String [] args) throws Exception {
		String out = args [0], scene = args.length > 1 ? args [1] : "shapes";
		Display display = new Display ();
		Shell shell = new Shell (display, SWT.NO_TRIM | SWT.ON_TOP);
		shell.setLayout (null);
		Canvas canvas = new Canvas (shell, SWT.NONE);
		canvas.setBounds (0, 0, W, H);
		canvas.setBackground (display.getSystemColor (SWT.COLOR_WHITE));
		Font font = new Font (display, "Segoe UI", 10, SWT.NORMAL);
		Font bold = new Font (display, "Consolas", 11, SWT.BOLD);
		int [] offset = {0};
		int [] selection = {40, 120};
		int [] paints = {0};
		canvas.addListener (SWT.Paint, e -> {
			paints [0]++;
			paint (e.gc, scene, font, bold, offset [0], selection);
		});
		shell.setBounds (40, 40, W, H);
		shell.open ();
		shell.forceActive ();
		settle (display, canvas);
		canvas.setFocus ();
		settle (display, canvas);
		String focus = focusReport (display, shell, canvas);
		if (scene.equals ("scroll")) {
			for (int i = 0; i < 6; i++) {
				offset [0] += 37;
				canvas.scroll (0, 0, 0, 37, W, H - 37, false);
				settle (display, canvas);
			}
		} else if (scene.equals ("overlay")) {
			for (int i = 0; i < 5; i++) {
				selection [0] += 17;
				selection [1] += 23;
				RetainedPlanes.invalidatePlanes (canvas, RetainedPlanes.SELECTION, 0, 0, W, H);
				settle (display, canvas);
			}
		}
		Thread.sleep (200);
		settle (display, canvas);
		capture (display, canvas, out);
		long [] counters = new long [GpuCounters.COUNT];
		GpuCounters.snapshot (counters);
		StringBuilder line = new StringBuilder ("mode=" + System.getProperty ("swt.gpu", "off") + " scene=" + scene + " " + focus + " paints=" + paints [0] + " sel=" + selection [0] + "," + selection [1]);
		for (int i = 0; i < GpuCounters.COUNT; i++) if (counters [i] != 0) line.append (' ').append (GpuCounters.NAMES [i]).append ('=').append (counters [i]);
		System.out.println (line);
		font.dispose ();
		bold.dispose ();
		display.dispose ();
	}

	/** Focus, foreground and child-window facts: the GPU path must not add a window or steal focus. */
	static String focusReport (Display display, Shell shell, Control control) {
		long child = org.eclipse.swt.internal.win32.OS.GetWindow (control.handle, org.eclipse.swt.internal.win32.OS.GW_CHILD);
		return "focusControl=" + (display.getFocusControl () == control)
			+ " hwndFocus=" + (org.eclipse.swt.internal.win32.OS.GetFocus () == control.handle)
			+ " foreground=" + (org.eclipse.swt.internal.win32.OS.GetForegroundWindow () == shell.handle)
			+ " childWindows=" + (child != 0 ? 1 : 0);
	}

	static void settle (Display display, Control control) throws InterruptedException {
		for (int i = 0; i < 20; i++) {
			control.update ();
			while (display.readAndDispatch ()) {
			}
			Thread.sleep (5);
		}
	}

	static void paint (GC gc, String scene, Font font, Font bold, int offset, int [] selection) {
		Display d = gc.getDevice () instanceof Display display ? display : null;
		if (scene.equals ("scroll")) {
			gc.setFont (font);
			for (int line = 0; line < 60; line++) {
				int y = line * 19 - offset;
				gc.setForeground (d.getSystemColor (line % 3 == 0 ? SWT.COLOR_DARK_BLUE : SWT.COLOR_BLACK));
				gc.drawString ("Line " + line + " of the retained tile scroll probe — the quick brown fox", 8, y, true);
			}
			return;
		}
		gc.setBackground (d.getSystemColor (SWT.COLOR_YELLOW));
		gc.fillRectangle (10, 10, 120, 60);
		gc.setForeground (d.getSystemColor (SWT.COLOR_BLUE));
		gc.drawRectangle (10, 10, 120, 60);
		gc.drawLine (150, 10, 300, 70);
		gc.drawLine (150, 70, 300, 10);
		gc.setBackground (d.getSystemColor (SWT.COLOR_GREEN));
		gc.fillOval (320, 10, 80, 60);
		gc.setForeground (d.getSystemColor (SWT.COLOR_RED));
		gc.drawOval (420, 10, 80, 60);
		gc.setForeground (d.getSystemColor (SWT.COLOR_DARK_GREEN));
		gc.setBackground (d.getSystemColor (SWT.COLOR_CYAN));
		gc.fillGradientRectangle (520, 10, 100, 60, true);
		gc.setBackground (d.getSystemColor (SWT.COLOR_MAGENTA));
		gc.fillPolygon (new int [] {20, 100, 90, 160, 30, 180});
		gc.setForeground (d.getSystemColor (SWT.COLOR_BLACK));
		gc.drawPolyline (new int [] {120, 100, 180, 170, 240, 110, 300, 180});
		gc.setFont (font);
		gc.drawString ("Retained planes: Segoe UI 10, transparent", 320, 100, true);
		gc.setBackground (d.getSystemColor (SWT.COLOR_GRAY));
		gc.drawString ("Opaque background text", 320, 125, false);
		gc.setFont (bold);
		gc.setForeground (d.getSystemColor (SWT.COLOR_DARK_RED));
		gc.drawText ("Consolas 11 bold drawText", 320, 150, SWT.DRAW_TRANSPARENT);
		for (int i = 0; i < 12; i++) {
			gc.setForeground (d.getSystemColor (2 + i % 14));
			gc.drawLine (20, 220 + i * 12, 600, 220 + i * 12);
		}
		if (scene.equals ("overlay")) {
			RetainedPlanes.setPlane (gc, RetainedPlanes.SELECTION);
			gc.setBackground (d.getSystemColor (SWT.COLOR_LIST_SELECTION));
			gc.fillRectangle (selection [0], 200, selection [1], 40);
			gc.setForeground (d.getSystemColor (SWT.COLOR_LIST_SELECTION_TEXT));
			gc.setFont (font);
			gc.drawString ("selection overlay", selection [0] + 4, 210, true);
			RetainedPlanes.setPlane (gc, RetainedPlanes.CARET);
			gc.setBackground (d.getSystemColor (SWT.COLOR_BLACK));
			gc.fillRectangle (selection [0] + selection [1], 200, 2, 40);
			RetainedPlanes.setPlane (gc, RetainedPlanes.BODY);
		}
	}

	static void capture (Display display, Control control, String out) throws IOException {
		Point origin = control.toDisplay (0, 0);
		Image image = new Image (display, W, H);
		GC gc = new GC (display);
		gc.copyArea (image, origin.x, origin.y);
		gc.dispose ();
		ImageData data = image.getImageData ();
		image.dispose ();
		ByteArrayOutputStream bytes = new ByteArrayOutputStream ();
		bytes.write (("P6\n" + W + " " + H + "\n255\n").getBytes ());
		for (int y = 0; y < H; y++) {
			for (int x = 0; x < W; x++) {
				RGB rgb = data.palette.getRGB (data.getPixel (x, y));
				bytes.write (rgb.red);
				bytes.write (rgb.green);
				bytes.write (rgb.blue);
			}
		}
		Files.write (java.nio.file.Path.of (out), bytes.toByteArray ());
	}
}
