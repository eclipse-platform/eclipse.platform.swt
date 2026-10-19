/*******************************************************************************
 * Copyright (c) 2026 Synexia, Hitesh Soliwal (hsoliwal@gmail.com) and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Apache License, Version 2.0, or (at your option) the Eclipse
 * Public License 2.0, which are available at
 * https://www.apache.org/licenses/LICENSE-2.0 and
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: Apache-2.0 OR EPL-2.0
 *
 * Contributors:
 *     Hitesh Soliwal (Synexia) - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tests.junit;

import java.util.*;

import org.eclipse.swt.tests.junit.Test_VirtualTreeOrderStatistic.Api;

/**
 * Micro-benchmark (plain main, not JMH) of virtual Tree row mapping with k
 * materialized siblings under one parent. It uses only package API present
 * before and after the sibling index, so the same class measures both: run it
 * once against classes without {@code VirtualTreeSiblingIndex} (linear walk)
 * and once with it. Fixture: 2k logical root children, k materialized at the
 * odd indices, every fourth of them expanded with 3 cold children.
 *
 * <p>Columns: implementation, k, visible rows, ns per {@code rowAt} (including
 * the Row it returns), ns per {@code visibleIndexOf}, ns per bind plus forget of
 * a sibling at a random cold index, ns per {@code insertCoordinate} plus
 * {@code removeCoordinate}. A last block measures a Fenwick tree over logical
 * child indices, the rejected alternative: ns per row descent, ns per rebuild
 * (needed after every coordinate insert or removal) and its bytes.</p>
 */
public final class VirtualTreeOrderStatisticBenchmark {
	private static final int ROOT = -1;
	private static final long MIN_NANOS = 1_000_000_000L;
	private static int sink;

	private VirtualTreeOrderStatisticBenchmark () {
	}

	/** Arguments: optional sibling counts k; without them k = 10, 1,000, 100,000 plus the Fenwick block. */
	public static void main (String [] args) {
		String implementation = hasSiblingIndex () ? "sibling-index" : "linear-walk";
		int [] counts = args.length == 0 ? new int [] {10, 1_000, 100_000}
				: Arrays.stream (args).mapToInt (Integer::parseInt).toArray ();
		System.out.println ("impl\tk\tvisible_rows\tns_rowAt\tns_visibleIndexOf\tns_bind_forget\tns_insert_remove_coordinate");
		for (int k : counts) {
			measure (implementation, k);
		}
		if (args.length == 0) {
			System.out.println ("fenwick\tk\tlogical_children\tns_descent\tns_rebuild\tbytes");
			for (int k : counts) {
				fenwick (k);
			}
		}
		System.out.println ("sink\t" + sink);
	}

	private static boolean hasSiblingIndex () {
		try {
			Class.forName ("org.eclipse.swt.widgets.VirtualTreeSiblingIndex");
			return true;
		} catch (ClassNotFoundException e) {
			return false;
		}
	}

	private static void measure (String implementation, int k) {
		Object topology = Api.newTopology ();
		Object projection = Api.newProjection (topology);
		Api.setChildCount (topology, ROOT, 2 * k);
		for (int i = k - 1; i >= 0; i--) {
			Api.bind (topology, i, ROOT, 2 * i + 1);
			if (i % 4 == 0) {
				Api.setChildCount (topology, i, 3);
				Api.expand (topology, i, true);
			}
		}
		long total = Api.rowCount (projection);
		Random random = new Random (0x6B7L + k);
		long [] rows = new long [4096];
		int [] ids = new int [4096];
		for (int i = 0; i < rows.length; i++) {
			rows [i] = (long)(random.nextDouble () * total);
			ids [i] = random.nextInt (k);
		}
		double rowAt = time (() -> {
			for (long row : rows) sink += Api.rowChildIndex (Api.rowAtRaw (projection, row));
			return rows.length;
		});
		double indexOf = time (() -> {
			for (int id : ids) sink += (int)Api.indexOf (projection, id);
			return ids.length;
		});
		/* Ids k and k + 1 are reused, so the id capacity stays bounded. */
		double bindForget = time (() -> {
			int operations = 256;
			for (int i = 0; i < operations; i++) {
				Api.bind (topology, k, ROOT, 2 * random.nextInt (k));
				Api.forgetSubtree (topology, k);
			}
			return operations;
		});
		double insertRemove = time (() -> {
			int operations = 64;
			for (int i = 0; i < operations; i++) {
				int index = random.nextInt (2 * k);
				Api.insertCoordinate (topology, ROOT, index, k + 1);
				Api.removeCoordinate (topology, ROOT, index);
			}
			return operations;
		});
		System.out.printf (Locale.ROOT, "%s\t%d\t%d\t%.1f\t%.1f\t%.1f\t%.1f%n", implementation, k, total, rowAt,
				indexOf, bindForget, insertRemove);
	}

	private interface Batch {
		int run ();
	}

	/** Warms up for 3 batches or 0.3 s, then returns mean ns per operation over at least 1 s. */
	private static double time (Batch batch) {
		long warm = System.nanoTime ();
		for (int i = 0; i < 3 || System.nanoTime () - warm < MIN_NANOS / 3; i++) batch.run ();
		long operations = 0, start = System.nanoTime (), elapsed;
		do {
			operations += batch.run ();
			elapsed = System.nanoTime () - start;
		} while (elapsed < MIN_NANOS);
		return (double)elapsed / operations;
	}

	/* Rejected alternative: Fenwick tree over the 2k logical children, weight 1 + extra rows. */
	private static void fenwick (int k) {
		int n = 2 * k;
		long [] weights = new long [n];
		for (int i = 0; i < k; i++) weights [2 * i + 1] = 1 + (i % 4 == 0 ? 3 : 0);
		for (int i = 0; i < n; i += 2) weights [i] = 1;
		long [] tree = new long [n + 1];
		build (tree, weights);
		long total = 0;
		for (long weight : weights) total += weight;
		Random random = new Random (0x6B7L + k);
		long [] rows = new long [4096];
		for (int i = 0; i < rows.length; i++) rows [i] = (long)(random.nextDouble () * total);
		double descent = time (() -> {
			for (long row : rows) sink += descend (tree, row);
			return rows.length;
		});
		double rebuild = time (() -> {
			build (tree, weights);
			return 1;
		});
		System.out.printf (Locale.ROOT, "fenwick\t%d\t%d\t%.1f\t%.1f\t%d%n", k, n, descent, rebuild, 8L * (n + 1));
	}

	private static void build (long [] tree, long [] weights) {
		Arrays.fill (tree, 0);
		for (int i = 1; i < tree.length; i++) {
			tree [i] += weights [i - 1];
			int parent = i + (i & -i);
			if (parent < tree.length) tree [parent] += tree [i];
		}
	}

	/** Index of the logical child whose row range holds {@code row}. */
	private static int descend (long [] tree, long row) {
		int position = 0;
		long remaining = row;
		for (int step = Integer.highestOneBit (tree.length - 1); step > 0; step >>= 1) {
			int next = position + step;
			if (next < tree.length && tree [next] <= remaining) {
				position = next;
				remaining -= tree [next];
			}
		}
		return position;
	}
}
