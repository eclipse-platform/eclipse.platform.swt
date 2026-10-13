/*******************************************************************************
 * Copyright (c) 2026 Hitesh Soliwal and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package org.eclipse.swt.examples.superset;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 * Process and frame metrics: frame-time percentiles over a reusable long[],
 * retained Java heap, process private memory (committed virtual memory of the
 * JVM, which HotSpot reports as private bytes on Windows) and native handle
 * counts from the optional {@code superset.handleProbe} helper.
 */
public final class Metrics {

	private final long[] frames;
	private final long[] sorted;
	private int count;

	public Metrics(int capacity) {
		frames = new long[capacity];
		sorted = new long[capacity];
	}

	public void reset() {
		count = 0;
	}

	public void frame(long nanos) {
		if (count < frames.length) frames[count++] = nanos;
	}

	/** @return {p50, p95, max} in milliseconds */
	public double[] percentiles() {
		if (count == 0) return new double[] {0, 0, 0};
		System.arraycopy(frames, 0, sorted, 0, count);
		Arrays.sort(sorted, 0, count);
		return new double[] {ms(sorted[(count - 1) / 2]), ms(sorted[(int) Math.ceil(count * 0.95) - 1]), ms(sorted[count - 1])};
	}

	public static double ms(long nanos) {
		return nanos / 1_000_000.0;
	}

	private static final java.lang.reflect.Method THREAD_ALLOCATED = allocatedMethod();

	private static java.lang.reflect.Method allocatedMethod() {
		try {
			return Class.forName("com.sun.management.ThreadMXBean").getMethod("getCurrentThreadAllocatedBytes");
		} catch (ReflectiveOperationException | RuntimeException e) {
			return null;
		}
	}

	/** @return bytes allocated so far by the calling thread (HotSpot TLAB accounting), or 0 when unavailable */
	public static long allocatedBytes() {
		if (THREAD_ALLOCATED == null) return 0;
		try {
			return (Long) THREAD_ALLOCATED.invoke(ManagementFactory.getThreadMXBean());
		} catch (ReflectiveOperationException | RuntimeException e) {
			return 0;
		}
	}

	/** @return used heap in MB after two full collections */
	public static double heapMb() {
		Runtime rt = Runtime.getRuntime();
		System.gc();
		System.gc();
		return (rt.totalMemory() - rt.freeMemory()) / 1048576.0;
	}

	/** @return process private memory in MB, or -1 when unavailable */
	public static double privateMb() {
		try {
			Class<?> type = Class.forName("com.sun.management.OperatingSystemMXBean");
			Method m = type.getMethod("getCommittedVirtualMemorySize");
			return ((Long) m.invoke(ManagementFactory.getOperatingSystemMXBean())) / 1048576.0;
		} catch (ReflectiveOperationException | RuntimeException e) {
			return -1;
		}
	}

	/** @return {kernel, gdi, user} handle counts, or -1s without a probe */
	public static int[] handles() {
		String probe = System.getProperty("superset.handleProbe");
		int[] out = {-1, -1, -1};
		if (probe == null) return out;
		try {
			Process p = new ProcessBuilder(probe, Long.toString(ProcessHandle.current().pid())).redirectErrorStream(true).start();
			try (BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream(), StandardCharsets.US_ASCII))) {
				String[] f = r.readLine().trim().split("\\s+");
				for (int i = 0; i < 3 && i < f.length; i++) out[i] = Integer.parseInt(f[i]);
			}
			p.waitFor(5, TimeUnit.SECONDS);
		} catch (Exception e) {
			// probe unavailable: keep -1
		}
		return out;
	}
}
