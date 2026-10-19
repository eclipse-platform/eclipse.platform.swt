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
import java.lang.management.*;
import java.nio.charset.*;
import java.security.*;
import java.util.*;
import java.util.List;

import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

/**
 * JUnit/SWTBot-free measurement harness for very long text in {@link StyledText}.
 *
 * <p>Usage: {@code StyledTextViewportBench <label> <sizeMiB> <fixed|wrap> [idleTimeoutMs] [header]}.
 * Prints one TSV row (see {@link #HEADER}). Opens a real shell on the current
 * display. Pixels are captured with {@code Control.print} (deterministic) and
 * {@code new GC(display).copyArea} (screen, diagnostic) outside every timed
 * interval. The caller's String stays referenced for the whole run, so heap
 * deltas are what the widget holds in addition to the caller's text.</p>
 *
 * <p>UI-thread split: {@code frame*} is one scroll frame on the UI thread
 * ({@code setTopIndex} + synchronous {@code update}), {@code paint*} its paint
 * part, {@code dispatch*} single event dispatches while the widget is idle
 * after {@code setText} (idle layout slices), {@code page*} the CPU cost per
 * 64 Ki-char page measured by {@code StyledTextViewportProbe}.</p>
 */
public final class StyledTextViewportBench {

	static final String HEADER = String.join("\t", "label", "sizeMiB", "mode", "contentClass", "lines", "chars",
			"setTextMs", "firstPaintMs", "idleDrainMs", "idleTimedOut", "dispatchP95Ms", "dispatchMaxMs",
			"scrollEndMs", "caretEndMs", "scrollTopMs", "uiCpuMs",
			"frameP50Ms", "frameP95Ms", "frameMaxMs", "paintP50Ms", "paintP95Ms", "paintMaxMs",
			"pageCountP50Us", "pageCountP95Us", "pageIndexP50Us", "pageIndexP95Us", "indexBuildMs", "indexBuildWorkerMs",
			"heapRetainedMiB", "heapPeakMiB", "procWorkingSetMiB", "procPrivateMiB", "closeVetoes",
			"topPrintInk", "endPrintInk", "topPrintSha", "endPrintSha", "topScreenSha", "endScreenSha");

	static final int LINE = 80; // bytes per generated line including '\n'

	static int closeVetoes;

	public static void main(String[] args) throws Exception {
		String label = args[0];
		int sizeMiB = Integer.parseInt(args[1]);
		boolean wrap = "wrap".equals(args[2]);
		long idleTimeout = args.length > 3 ? Long.parseLong(args[3]) : 60_000L;
		if (args.length > 4 && "header".equals(args[4])) {
			System.out.println(HEADER);
		}
		String text = generate((long) sizeMiB << 20);
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.addListener(SWT.Close, event -> {
			event.doit = false;
			closeVetoes++;
		});
		shell.setLayout(new FillLayout());
		shell.setSize(1000, 800);
		shell.setLocation(40, 40);
		StyledText styledText = new StyledText(shell, SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL);
		styledText.setFont(new Font(display, "Consolas", 10, SWT.NORMAL));
		styledText.setWordWrap(wrap);
		styledText.setCaret(null);
		shell.open();
		shell.forceActive();
		drain(display, 5_000L, null);

		ThreadMXBean threads = ManagementFactory.getThreadMXBean();
		gc();
		long heapBefore = heapUsed();
		resetPeaks();
		long cpu0 = threads.getCurrentThreadCpuTime();

		long t0 = System.nanoTime();
		styledText.setText(text);
		long t1 = System.nanoTime();
		styledText.redraw();
		styledText.update();
		long t2 = System.nanoTime();
		List<Long> dispatches = new ArrayList<>();
		long[] idle = drain(display, idleTimeout, dispatches);
		long t3 = System.nanoTime();
		styledText.setTopIndex(styledText.getLineCount() - 1);
		styledText.update();
		long t4 = System.nanoTime();
		styledText.setSelection(styledText.getCharCount());
		styledText.showSelection();
		styledText.update();
		long t5 = System.nanoTime();
		long cpu1 = threads.getCurrentThreadCpuTime();
		long heapPeak = heapPeak();
		drain(display, 2_000L, null);
		String[] end = capture(display, styledText);
		long t6 = System.nanoTime();
		styledText.setTopIndex(0);
		styledText.setHorizontalIndex(0);
		styledText.update();
		long t7 = System.nanoTime();
		drain(display, 2_000L, null);
		String[] top = capture(display, styledText);
		long[][] frames = scrollFrames(display, styledText);
		gc();
		long heapAfter = heapUsed();
		long[] proc = processMemory();
		String[] page = StyledTextViewportProbe.pageCosts(text);
		String contentClass = styledText.getContent().getClass().getSimpleName();
		int lines = styledText.getLineCount();
		int chars = styledText.getCharCount();
		long[] dispatch = toArray(dispatches);
		System.out.println(String.join("\t", label, Integer.toString(sizeMiB), wrap ? "wrap" : "fixed", contentClass,
				Integer.toString(lines), Integer.toString(chars), ms(t1 - t0), ms(t2 - t1), ms(idle[0]),
				Boolean.toString(idle[1] != 0), ms(percentile(dispatch, 95)), ms(percentile(dispatch, 100)),
				ms(t4 - t3), ms(t5 - t4), ms(t7 - t6), ms(cpu1 - cpu0),
				ms(percentile(frames[0], 50)), ms(percentile(frames[0], 95)), ms(percentile(frames[0], 100)),
				ms(percentile(frames[1], 50)), ms(percentile(frames[1], 95)), ms(percentile(frames[1], 100)),
				page[0], page[1], page[2], page[3], page[4], page[5],
				mib(heapAfter - heapBefore), mib(heapPeak), mib(proc[0]), mib(proc[1]), Integer.toString(closeVetoes),
				top[0], end[0], top[1], end[1], top[3], end[3]));
		display.dispose();
		if (text.isEmpty()) {
			System.out.println(); // keeps the caller's text reachable until here
		}
	}

	/**
	 * 250 UI-thread frames over the document: 100 page-downs from the top, 50
	 * deterministic far jumps, 100 wheel steps of 3 lines. Returns {frameNs[], paintNs[]}.
	 */
	static long[][] scrollFrames(Display display, StyledText styledText) {
		int lines = styledText.getLineCount();
		int page = Math.max(1, styledText.getClientArea().height / Math.max(1, styledText.getLineHeight()));
		Random random = new Random(20261013L);
		int frames = 250;
		long[] frame = new long[frames];
		long[] paint = new long[frames];
		int top = 0;
		styledText.setTopIndex(0);
		styledText.update();
		for (int i = 0; i < frames; i++) {
			if (i < 100) {
				top = Math.min(lines - 1, top + page);
			} else if (i < 150) {
				top = random.nextInt(lines);
			} else {
				top = Math.min(lines - 1, top + 3);
			}
			long a = System.nanoTime();
			styledText.setTopIndex(top);
			long b = System.nanoTime();
			styledText.update();
			long c = System.nanoTime();
			frame[i] = c - a;
			paint[i] = c - b;
			drain(display, 50L, null);
		}
		return new long[][] { frame, paint };
	}

	/** Deterministic ASCII text, {@value #LINE} bytes per line, '\n' delimited. */
	static String generate(long bytes) {
		byte[] data = new byte[Math.toIntExact(bytes)];
		byte[] filler = "the quick brown fox jumps over the lazy dog 0123456789 viewport-trick ABCDEFGHIJ".getBytes(StandardCharsets.US_ASCII);
		long line = 0;
		int pos = 0;
		while (pos < data.length) {
			String prefix = String.format("%010d ", line++);
			for (int i = 0; i < prefix.length() && pos < data.length; i++) {
				data[pos++] = (byte) prefix.charAt(i);
			}
			int fill = LINE - 1 - prefix.length();
			for (int i = 0; i < fill && pos < data.length; i++) {
				data[pos++] = filler[i % filler.length];
			}
			if (pos < data.length) {
				data[pos++] = '\n';
			}
		}
		return new String(data, StandardCharsets.ISO_8859_1);
	}

	/**
	 * Dispatches events until the queue is empty or the timeout expires; returns
	 * {elapsedNs, timedOut}. Each dispatch duration is added to {@code dispatches}.
	 */
	static long[] drain(Display display, long timeoutMs, List<Long> dispatches) {
		long start = System.nanoTime();
		long deadline = start + timeoutMs * 1_000_000L;
		while (true) {
			long a = System.nanoTime();
			boolean more = display.readAndDispatch();
			long b = System.nanoTime();
			if (!more) {
				return new long[] { b - start, 0 };
			}
			if (dispatches != null) {
				dispatches.add(b - a);
			}
			if (b > deadline) {
				return new long[] { b - start, 1 };
			}
		}
	}

	/**
	 * Captures the control twice outside timed intervals: SWT screen capture
	 * ({@code new GC(display).copyArea}) and control-surface rendering
	 * ({@code Control.print}). Returns {printInk, printSha, screenInk, screenSha}.
	 */
	static String[] capture(Display display, StyledText styledText) throws NoSuchAlgorithmException {
		Rectangle area = styledText.getClientArea();
		Point origin = styledText.toDisplay(0, 0);
		Image screen = new Image(display, area.width, area.height);
		Image printed = new Image(display, styledText.getSize().x, styledText.getSize().y);
		GC screenGC = new GC(display);
		GC printGC = new GC(printed);
		try {
			screenGC.copyArea(screen, origin.x, origin.y);
			styledText.print(printGC);
			String[] p = digest(printed.getImageData(), area);
			String[] s = digest(screen.getImageData(), new Rectangle(0, 0, area.width, area.height));
			return new String[] { p[0], p[1], s[0], s[1] };
		} finally {
			screenGC.dispose();
			printGC.dispose();
			screen.dispose();
			printed.dispose();
		}
	}

	/** {inkPixels, sha256 prefix} over the given area; ink = pixels differing from the area's bottom-right pixel. */
	static String[] digest(ImageData data, Rectangle area) throws NoSuchAlgorithmException {
		int background = data.getPixel(area.x + area.width - 2, area.y + area.height - 2);
		MessageDigest sha = MessageDigest.getInstance("SHA-256");
		long ink = 0;
		byte[] row = new byte[4];
		for (int y = area.y; y < area.y + area.height; y++) {
			for (int x = area.x; x < area.x + area.width; x++) {
				int pixel = data.getPixel(x, y);
				if (pixel != background) {
					ink++;
				}
				row[0] = (byte) (pixel >>> 24); row[1] = (byte) (pixel >>> 16); row[2] = (byte) (pixel >>> 8); row[3] = (byte) pixel;
				sha.update(row);
			}
		}
		return new String[] { Long.toString(ink), HexFormat.of().formatHex(sha.digest(), 0, 8) };
	}

	static long[] toArray(List<Long> values) {
		long[] result = new long[values.size()];
		for (int i = 0; i < result.length; i++) {
			result[i] = values.get(i);
		}
		return result;
	}

	/** Nearest-rank percentile; 100 = max; 0 for an empty sample. */
	static long percentile(long[] values, int p) {
		if (values.length == 0) {
			return 0;
		}
		long[] sorted = values.clone();
		Arrays.sort(sorted);
		int rank = (int) Math.ceil(p / 100.0 * sorted.length);
		return sorted[Math.max(0, Math.min(sorted.length - 1, rank - 1))];
	}

	static void gc() {
		for (int i = 0; i < 3; i++) {
			System.gc();
		}
	}

	static long heapUsed() {
		Runtime runtime = Runtime.getRuntime();
		return runtime.totalMemory() - runtime.freeMemory();
	}

	static void resetPeaks() {
		for (MemoryPoolMXBean pool : ManagementFactory.getMemoryPoolMXBeans()) {
			if (pool.getType() == MemoryType.HEAP) {
				pool.resetPeakUsage();
			}
		}
	}

	static long heapPeak() {
		long peak = 0;
		for (MemoryPoolMXBean pool : ManagementFactory.getMemoryPoolMXBeans()) {
			if (pool.getType() == MemoryType.HEAP) {
				peak += pool.getPeakUsage().getUsed();
			}
		}
		return peak;
	}

	/** {workingSet, privateBytes} of this process via PowerShell, measured outside timed intervals. */
	static long[] processMemory() {
		try {
			long pid = ProcessHandle.current().pid();
			Process process = new ProcessBuilder("powershell", "-NoProfile", "-Command",
					"$p=Get-Process -Id " + pid + "; [string]$p.WorkingSet64 + ' ' + [string]$p.PrivateMemorySize64")
					.redirectErrorStream(true).start();
			String out = new String(process.getInputStream().readAllBytes(), StandardCharsets.US_ASCII).trim();
			process.waitFor();
			String[] parts = out.split("\\s+");
			return new long[] { Long.parseLong(parts[0]), Long.parseLong(parts[1]) };
		} catch (Exception e) {
			return new long[] { -1, -1 };
		}
	}

	static String ms(long nanos) {
		return String.format(Locale.ROOT, "%.2f", nanos / 1e6);
	}

	static String mib(long bytes) {
		return String.format(Locale.ROOT, "%.1f", bytes / 1048576.0);
	}
}
