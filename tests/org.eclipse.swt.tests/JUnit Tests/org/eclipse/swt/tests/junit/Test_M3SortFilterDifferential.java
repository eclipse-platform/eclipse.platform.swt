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

import static org.junit.jupiter.api.Assertions.*;

import java.io.*;
import java.nio.charset.*;
import java.nio.file.*;
import java.util.*;

import org.eclipse.swt.*;
import org.eclipse.swt.m3.*;
import org.junit.jupiter.api.*;

/**
 * Differential test of the filter, sort and window chain against a straightforward reference
 * (a List of rows filtered with a predicate and sorted with List.sort and a comparator chain), on
 * seeded data from 10^3 to 10^6 rows, including incremental updates. With
 * <code>-Dm3.sortfilter.timings=&lt;file&gt;</code> the timings are appended to that file as TSV.
 */
@SuppressWarnings ("nls")
public class Test_M3SortFilterDifferential {

	static final class Data {
		final int [] ints;
		final double [] doubles;
		final String [] texts;

		Data (int n, long seed) {
			Random random = new Random (seed);
			ints = new int [n];
			doubles = new double [n];
			texts = new String [n];
			for (int i = 0; i < n; i++) change (i, random);
		}

		void change (int i, Random random) {
			ints [i] = random.nextInt (Math.max (2, ints.length / 10)) - ints.length / 40;
			doubles [i] = random.nextInt (1000) / 8.0;
			texts [i] = random.nextInt (20) == 0 ? null : "w" + random.nextInt (5000);
		}

		String text (int row, int column) {
			return column == 0 ? String.valueOf (ints [row]) : column == 3 ? texts [row] : String.valueOf (doubles [row]);
		}

		boolean passes (int row) {
			String t = texts [row];
			boolean text = t != null && (t.startsWith ("w1") || t.startsWith ("w2"));
			return text && ints [row] >= 0;
		}

		int [] reference (int n) {
			List<Integer> rows = new ArrayList<> ();
			for (int row = 0; row < n; row++) {
				if (passes (row)) rows.add (row);
			}
			Comparator<Integer> chain = Comparator.<Integer, String> comparing (r -> texts [r], Comparator.nullsFirst (Comparator.naturalOrder ()))
				.thenComparing (Comparator.<Integer> comparingInt (r -> ints [r]).reversed ())
				.thenComparingDouble (r -> doubles [r]);
			rows.sort (chain);
			return rows.stream ().mapToInt (Integer::intValue).toArray ();
		}
	}

	static void time (String what, int n, long nanos) {
		String file = System.getProperty ("m3.sortfilter.timings");
		if (file == null) return;
		String line = what + "\t" + n + "\t" + nanos / 1000 + "\n";
		try {
			Files.writeString (Paths.get (file), line, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
		} catch (IOException e) {
			fail (e);
		}
	}

	@Test
	public void test_differentialUpToAMillionRows () {
		for (int n : new int [] {1_000, 10_000, 100_000, 1_000_000}) {
			Data data = new Data (n, n);
			long start = System.nanoTime ();
			int [] expected = data.reference (n);
			time ("reference-filter-sort", n, System.nanoTime () - start);

			start = System.nanoTime ();
			M3ColumnFilter filter = new M3ColumnFilter (4, n, data::text);
			M3ColumnSort sort = new M3ColumnSort (4, filter);
			sort.setKey (0, M3ColumnKey.ofInts (r -> data.ints [r]));
			sort.setKey (2, M3ColumnKey.ofDoubles (r -> data.doubles [r]));
			sort.setKey (3, M3ColumnKey.ofText (r -> data.texts [r], null));
			time ("m3-chain-create", n, System.nanoTime () - start);
			start = System.nanoTime ();
			filter.setText (3, "w1*,w2*", M3TextMatch.WILDCARD);
			filter.setText (0, "-", M3TextMatch.CONTAINS | M3TextMatch.NEGATE);
			time ("m3-filter-two-criteria", n, System.nanoTime () - start);
			start = System.nanoTime ();
			sort.setSortKeys (new int [] {3, 0, 2}, new int [] {SWT.UP, SWT.DOWN, SWT.UP});
			time ("m3-sort-three-keys", n, System.nanoTime () - start);
			assertArrayEquals (expected, Test_M3ColumnSort.order (sort), "n " + n);

			M3RowWindow window = new M3RowWindow (sort);
			window.setWindow (expected.length / 2, 40);
			for (int i = 0; i < window.size (); i++) assertEquals (expected [expected.length / 2 + i], window.map (i));

			Random random = new Random (n + 1);
			int updates = 1000;
			start = System.nanoTime ();
			for (int k = 0; k < updates; k++) {
				int row = random.nextInt (n);
				data.change (row, random);
				window.rowsChanged (row);
			}
			time ("m3-single-row-update-x1000", n, System.nanoTime () - start);
			start = System.nanoTime ();
			int [] batch = new int [Math.max (1, n / 100)];
			for (int k = 0; k < batch.length; k++) {
				batch [k] = random.nextInt (n);
				data.change (batch [k], random);
			}
			sort.rowsChanged (batch);
			time ("m3-batch-update-1pct", n, System.nanoTime () - start);
			start = System.nanoTime ();
			filter.setText (3, "w1*", M3TextMatch.WILDCARD);
			filter.setText (3, "w1*,w2*", M3TextMatch.WILDCARD);
			time ("m3-filter-narrow-widen", n, System.nanoTime () - start);
			start = System.nanoTime ();
			expected = data.reference (n);
			time ("reference-rebuild", n, System.nanoTime () - start);
			assertArrayEquals (expected, Test_M3ColumnSort.order (sort), "n " + n + " after updates");
			Test_M3ColumnSort.assertInverse (sort);
		}
	}

	@Test
	public void test_randomScriptsOnSmallSources () {
		Random random = new Random (77);
		for (int seed = 0; seed < 40; seed++) {
			Test_M3ColumnSort.Rows rows = new Test_M3ColumnSort.Rows (random.nextInt (300), seed, 2 + random.nextInt (9));
			M3CellText text = Test_M3ColumnFilter.text (rows);
			M3ColumnFilter filter = new M3ColumnFilter (4, rows.size (), text);
			M3ColumnSort sort = new M3ColumnSort (4, filter);
			rows.keys (sort);
			int keys = 1 + random.nextInt (3);
			int [] c = new int [keys], d = new int [keys];
			List<Integer> columns = new ArrayList<> (List.of (0, 1, 2, 3));
			Collections.shuffle (columns, random);
			for (int k = 0; k < keys; k++) {
				c [k] = columns.get (k);
				d [k] = random.nextBoolean () ? SWT.UP : SWT.DOWN;
			}
			sort.setSortKeys (c, d);
			for (int step = 0; step < 40; step++) {
				if (random.nextInt (3) == 0) Test_M3ColumnFilter.criterion (filter, random); else Test_M3ColumnSort.edit (rows, sort, random);
				assertArrayEquals (Test_M3ColumnFilter.reference (rows, filter, text, c, d), Test_M3ColumnSort.order (sort), "seed " + seed + " step " + step);
			}
		}
	}
}
