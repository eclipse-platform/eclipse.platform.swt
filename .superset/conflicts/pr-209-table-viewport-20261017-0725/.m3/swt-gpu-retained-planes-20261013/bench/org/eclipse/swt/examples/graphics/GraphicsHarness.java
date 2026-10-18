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
package org.eclipse.swt.examples.graphics;

import java.io.*;
import java.lang.management.*;
import java.util.*;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.gpu.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

/**
 * Drives the fork's GraphicsExample (the whole GC API) as the retained-plane
 * harness. Animation timers are stopped so every capture is deterministic.
 * <p>usage: GraphicsHarness &lt;outDir&gt; [animFrames]</p>
 * Writes one PPM per static tab (canvas pixels as presented on screen) and
 * prints one TSV row per animated tab with frame time p50/p95/max.
 */
public class GraphicsHarness {
	static final String [] STATIC_TABS = {"LineTab", "StarPolyTab", "CurvesSWTTab", "GradientTab", "LineCapTab", "LineJoinTab",
		"LineStyleTab", "PathTab", "CustomFontTab", "AlphaTab", "IntroTab", "FlatTextTab", "ImageFlipTab", "InterpolationTab", "PathClippingTab"};
	static final String [] ANIMATED_TABS = {"BallTab", "SpiralTab", "BlackHoleTab", "AlphaTab", "IntroTab"};

	public static void main (String [] args) throws Exception {
		File out = new File (args [0]);
		int frames = args.length > 1 ? Integer.parseInt (args [1]) : 200;
		out.mkdirs ();
		Display display = new Display ();
		Shell shell = new Shell (display);
		shell.setLayout (new FillLayout ());
		shell.setBounds (10, 10, 1100, 760);
		GraphicsExample example = new GraphicsExample (shell);
		example.animate = false;
		shell.open ();
		shell.forceActive ();
		settle (display, example.canvas);
		String mode = System.getProperty ("swt.gpu", "off");
		Map<String, GraphicsTab> byName = new LinkedHashMap<> ();
		for (GraphicsTab tab : example.tabs) byName.put (tab.getClass ().getSimpleName (), tab);
		for (String name : STATIC_TABS) {
			GraphicsTab tab = byName.get (name);
			if (tab == null) continue;
			example.setTab (tab);
			if (tab instanceof AnimatedGraphicsTab animated) animated.setAnimation (false);
			example.canvas.redraw ();
			settle (display, example.canvas);
			capture (display, example.canvas, new File (out, name + "-" + mode + ".ppm"));
		}
		com.sun.management.ThreadMXBean threads = (com.sun.management.ThreadMXBean) ManagementFactory.getThreadMXBean ();
		long tid = Thread.currentThread ().getId ();
		for (String name : ANIMATED_TABS) {
			if (!(byName.get (name) instanceof AnimatedGraphicsTab tab)) continue;
			example.setTab (tab);
			settle (display, example.canvas);
			Rectangle rect = example.canvas.getClientArea ();
			double [] wall = new double [frames];
			long alloc = 0;
			for (int i = 0; i < frames + 20; i++) {
				long a0 = threads.getThreadAllocatedBytes (tid), t0 = System.nanoTime ();
				tab.next (rect.width, rect.height);
				example.canvas.redraw ();
				example.canvas.update ();
				while (display.readAndDispatch ()) {
				}
				if (i >= 20) {
					wall [i - 20] = (System.nanoTime () - t0) / 1e6;
					alloc += threads.getThreadAllocatedBytes (tid) - a0;
				}
			}
			Arrays.sort (wall);
			System.out.println (String.join ("\t", mode, "GraphicsExample/" + name, "" + frames,
				f (wall [frames / 2]), f (wall [(int) (frames * 0.95)]), f (wall [frames - 1]), "" + alloc / frames,
				"" + GpuCounters.get (GpuCounters.RECORD_FAILS), "" + GpuCounters.get (GpuCounters.RECORD_FAIL_BITS), "" + GpuCounters.get (GpuCounters.FALLBACKS)));
		}
		display.dispose ();
	}

	static String f (double v) {
		return String.format (Locale.ROOT, "%.3f", v);
	}

	static void settle (Display display, Control control) throws InterruptedException {
		for (int i = 0; i < 15; i++) {
			control.update ();
			while (display.readAndDispatch ()) {
			}
			Thread.sleep (5);
		}
	}

	static void capture (Display display, Canvas control, File file) throws IOException {
		Rectangle area = control.getClientArea ();
		Point origin = control.toDisplay (0, 0);
		Image image = new Image (display, area.width, area.height);
		GC gc = new GC (display);
		gc.copyArea (image, origin.x, origin.y);
		gc.dispose ();
		ImageData data = image.getImageData ();
		image.dispose ();
		try (OutputStream stream = new BufferedOutputStream (new FileOutputStream (file))) {
			stream.write (("P6\n" + area.width + " " + area.height + "\n255\n").getBytes ());
			for (int y = 0; y < area.height; y++) {
				for (int x = 0; x < area.width; x++) {
					RGB rgb = data.palette.getRGB (data.getPixel (x, y));
					stream.write (rgb.red);
					stream.write (rgb.green);
					stream.write (rgb.blue);
				}
			}
		}
	}
}
