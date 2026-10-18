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
import java.lang.management.ManagementFactory;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * Frame metrics for StyledText on huge documents (leaf 3 before/after).
 * <p>
 * Usage: {@code StyledTextFrameBench <doc> <frames> <reportTsv> <label>} where doc is
 * {@code mib100} (100 MiB, about 1.86 million lines) or {@code lines10m} (10,000,000 short lines).
 * Scenarios: scroll (3 lines per frame), selection drag (selection end grows), caret (column and
 * line moves), typing (insert one character), resize storm and scroll storm (flicker count).
 * Per scenario: frame p50/p95/max (action plus synchronous paint), layouts requested and built
 * per frame, Java bytes allocated per frame, layout cache hit rate, paint events and painted pixels
 * per frame, and full-client repaints (the flicker count).
 */
public final class StyledTextFrameBench {
	static long paints, paintedPixels, fullPaints;
	static int clientW, clientH;

	public static void main(String[] args) throws Exception {
		String doc = args[0];
		int frames = Integer.parseInt(args[1]);
		Path report = Path.of(args[2]);
		String label = args[3];
		java.util.Set<String> only = args.length > 4 ? java.util.Set.of(args[4].split(",")) : null;
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setLayout(new FillLayout());
		StyledText text = new StyledText(shell, SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL);
		text.addListener(SWT.Paint, e -> {
			paints++;
			paintedPixels += (long) e.width * e.height;
			if (e.width >= clientW && e.height >= clientH) {
				fullPaints++;
			}
		});
		shell.setBounds(40, 40, 900, 700);
		shell.open();
		long used0 = usedHeap();
		String documentText = document(doc);
		long textChars = documentText.length();
		long t0 = System.nanoTime();
		text.setText(documentText);
		long setTextMs = (System.nanoTime() - t0) / 1_000_000;
		documentText = null;
		settle(display, text);
		long heapDelta = usedHeap() - used0;
		System.out.println("heap " + doc + ": " + heapDelta + " bytes retained after setText for " + text.getLineCount() + " lines = "
				+ fmt((double) heapDelta / text.getLineCount()) + " bytes/line (text " + fmt((double) textChars / text.getLineCount())
				+ " chars/line, Latin-1 String bytes)");
		clientW = text.getClientArea().width;
		clientH = text.getClientArea().height;
		int lines = text.getLineCount();
		StringBuilder out = new StringBuilder();
		if (!Files.exists(report)) {
			out.append("label\tdoc\tlines\tscenario\tframes\tp50_us\tp95_us\tmax_us\tlayouts_req_per_frame\tlayouts_built_per_frame\t"
					+ "alloc_bytes_per_frame\tcache_hit_rate\tpaints_per_frame\tpainted_px_per_frame\tfull_repaints\tsetText_ms\n");
		}
		int lineHeight = text.getLineHeight();
		text.setTopIndex(lines / 2);
		settle(display, text);
		int mid = text.getOffsetAtLine(lines / 2 + 5);
		if (only == null || only.contains("scroll")) out.append(run(label, doc, lines, "scroll", frames, display, text, i -> text.setTopPixel(text.getTopPixel() + 3 * lineHeight), setTextMs));
		text.setTopIndex(lines / 2);
		settle(display, text);
		if (only == null || only.contains("selection-drag")) out.append(run(label, doc, lines, "selection-drag", frames, display, text, i -> text.setSelectionRange(mid, 1 + (i * 7) % 1500), setTextMs));
		if (only == null || only.contains("caret")) out.append(run(label, doc, lines, "caret", frames, display, text,
				i -> text.invokeAction((i & 7) == 7 ? (((i >> 3) & 1) == 0 ? org.eclipse.swt.custom.ST.LINE_DOWN : org.eclipse.swt.custom.ST.LINE_UP)
						: org.eclipse.swt.custom.ST.COLUMN_NEXT), setTextMs));
		text.setCaretOffset(mid);
		if (only == null || only.contains("typing")) out.append(run(label, doc, lines, "typing", frames, display, text, i -> text.insert(Character.toString((char) ('a' + i % 26))), setTextMs));
		if (only == null || only.contains("scroll-storm")) out.append(run(label, doc, lines, "scroll-storm", frames, display, text, i -> text.setTopPixel(text.getTopPixel() + ((i & 1) == 0 ? 1 : 2) * lineHeight), setTextMs));
		if (only == null || only.contains("resize-storm")) out.append(run(label, doc, lines, "resize-storm", Math.min(frames, 60), display, text, i -> shell.setSize(900 - (i % 10) * 7, 700 - (i % 10) * 5), setTextMs));
		Files.writeString(report, out.toString(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
		System.out.print(out);
		shell.dispose();
		display.dispose();
	}

	interface Frame {
		void run(int i);
	}

	static String run(String label, String doc, int lines, String scenario, int frames, Display display, StyledText text, Frame frame,
			long setTextMs) throws ReflectiveOperationException {
		com.sun.management.ThreadMXBean threads = (com.sun.management.ThreadMXBean) ManagementFactory.getThreadMXBean();
		long thread = Thread.currentThread().getId();
		for (int i = 0; i < 5; i++) { // warm up
			frame.run(i);
			text.update();
		}
		settle(display, text);
		long[] times = new long[frames];
		long req0 = counter(text, "layoutRequests"), built0 = counter(text, "layoutBuilds");
		long paints0 = paints, px0 = paintedPixels, full0 = fullPaints;
		long alloc0 = threads.getThreadAllocatedBytes(thread);
		for (int i = 0; i < frames; i++) {
			long start = System.nanoTime();
			frame.run(i);
			text.update(); // synchronous paint of the damage
			while (display.readAndDispatch()) {
				// drain the frame's events (paint, idle slices)
			}
			times[i] = System.nanoTime() - start;
		}
		long alloc = threads.getThreadAllocatedBytes(thread) - alloc0;
		long req = counter(text, "layoutRequests") - req0, built = counter(text, "layoutBuilds") - built0;
		Arrays.sort(times);
		return String.join("\t", label, doc, Integer.toString(lines), scenario, Integer.toString(frames),
				Long.toString(times[frames / 2] / 1000), Long.toString(times[(int) (frames * 0.95)] / 1000), Long.toString(times[frames - 1] / 1000),
				fmt((double) req / frames), fmt((double) built / frames), Long.toString(alloc / frames),
				req == 0 ? "n/a" : fmt(1.0 - (double) built / req), fmt((double) (paints - paints0) / frames),
				Long.toString((paintedPixels - px0) / frames), Long.toString(fullPaints - full0), Long.toString(setTextMs)) + "\n";
	}

	static long usedHeap() {
		for (int i = 0; i < 3; i++) {
			System.gc();
		}
		Runtime r = Runtime.getRuntime();
		return r.totalMemory() - r.freeMemory();
	}

	static String fmt(double v) {
		return String.format(java.util.Locale.ROOT, "%.3f", v);
	}

	static long counter(StyledText t, String name) throws ReflectiveOperationException {
		Field rendererField = StyledText.class.getDeclaredField("renderer");
		rendererField.setAccessible(true);
		Object renderer = rendererField.get(t);
		Field f = renderer.getClass().getDeclaredField(name);
		f.setAccessible(true);
		return f.getLong(renderer);
	}

	static void settle(Display display, StyledText text) {
		text.update();
		long deadline = System.currentTimeMillis() + 2000;
		while (System.currentTimeMillis() < deadline) {
			if (!display.readAndDispatch()) {
				break;
			}
		}
	}

	static String document(String doc) {
		StringBuilder b;
		if ("lines10m".equals(doc)) {
			b = new StringBuilder(10_000_000 * 11);
			for (int i = 0; i < 10_000_000; i++) {
				b.append("L").append(i).append(" ab\n");
			}
		} else {
			b = new StringBuilder((100 << 20) + 64);
			int i = 0;
			while (b.length() < (100 << 20)) {
				b.append("line ").append(i++).append(" the quick brown fox jumps over the lazy dog\n");
			}
		}
		return b.toString();
	}
}
