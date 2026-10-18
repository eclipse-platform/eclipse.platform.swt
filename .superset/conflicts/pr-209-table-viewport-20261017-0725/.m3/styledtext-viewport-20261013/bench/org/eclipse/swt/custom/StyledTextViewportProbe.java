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
package org.eclipse.swt.custom;

import java.lang.reflect.*;
import java.util.*;

/**
 * Bench-only probe in the custom package. It times the CPU work per 64 Ki-char
 * page of the content index without changing production code: the page break
 * count (worker-side work in PagedContent's parallel build), the page's
 * delimiter-offset materialization (on demand for newly exposed pages) and the
 * whole index build. For the gap-buffer content the whole-document
 * <code>indexLines</code> is the only unit; its per-page cost is total/pages.
 * Reflection keeps the probe compilable against the unmodified SWT jar.
 */
public final class StyledTextViewportProbe {
	private static final int PAGE = 1 << 16;

	private StyledTextViewportProbe() {
	}

	/** {countP50Us, countP95Us, indexP50Us, indexP95Us, buildMs, buildWorkerMs}. */
	public static String[] pageCosts(String text) {
		try {
			Class<?> paged = Class.forName("org.eclipse.swt.custom.PagedContent");
			Method count = paged.getDeclaredMethod("countBreaks", String.class, int.class, int.class);
			Method ends = paged.getDeclaredMethod("delimiterEnds", String.class, int.class, int.class, int.class);
			Method build = paged.getDeclaredMethod("build", String.class, int.class);
			int pages = (text.length() + PAGE - 1) / PAGE;
			long[] countNs = new long[pages];
			long[] indexNs = new long[pages];
			for (int round = 0; round < 2; round++) { // round 0 warms the JIT
				for (int p = 0; p < pages; p++) {
					int from = p * PAGE, to = Math.min(text.length(), from + PAGE);
					long a = System.nanoTime();
					int breaks = (Integer) count.invoke(null, text, from, to);
					long b = System.nanoTime();
					ends.invoke(null, text, from, to, breaks);
					long c = System.nanoTime();
					countNs[p] = b - a;
					indexNs[p] = c - b;
				}
			}
			long a = System.nanoTime();
			build.invoke(null, text, PAGE);
			long b = System.nanoTime();
			long worker = 0;
			for (long ns : countNs) {
				worker += ns;
			}
			return new String[] { us(percentile(countNs, 50)), us(percentile(countNs, 95)), us(percentile(indexNs, 50)),
					us(percentile(indexNs, 95)), ms(b - a), ms(worker) };
		} catch (ClassNotFoundException e) {
			DefaultContent content = new DefaultContent();
			long a = System.nanoTime();
			content.setText(text);
			long b = System.nanoTime();
			int pages = Math.max(1, (text.length() + PAGE - 1) / PAGE);
			String perPage = us((b - a) / pages);
			return new String[] { perPage, "n/a", perPage, "n/a", ms(b - a), "0.00" };
		} catch (ReflectiveOperationException e) {
			throw new IllegalStateException(e);
		}
	}

	private static long percentile(long[] values, int p) {
		long[] sorted = values.clone();
		Arrays.sort(sorted);
		int rank = (int) Math.ceil(p / 100.0 * sorted.length);
		return sorted[Math.max(0, Math.min(sorted.length - 1, rank - 1))];
	}

	private static String us(long nanos) {
		return String.format(Locale.ROOT, "%.1f", nanos / 1e3);
	}

	private static String ms(long nanos) {
		return String.format(Locale.ROOT, "%.2f", nanos / 1e6);
	}
}
