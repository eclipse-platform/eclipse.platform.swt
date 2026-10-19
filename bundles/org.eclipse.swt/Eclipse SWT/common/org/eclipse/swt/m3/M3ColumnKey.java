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
package org.eclipse.swt.m3;

import java.util.*;
import java.util.function.*;

/**
 * How {@link M3ColumnSort} reads the sort key of a column (enhanced M3 API).
 * <p>
 * A key reads the application's values by source row, typically from its own arrays; widget
 * items are never read. Numeric keys sort through radix keys directly; text and comparator keys
 * are first compressed into dense ranks by one stable merge sort.
 * </p>
 *
 * @since 3.138
 */
public abstract class M3ColumnKey {

	/** Orders two source rows. */
	@FunctionalInterface
	public interface RowComparator {
		/**
		 * Compares two source rows.
		 *
		 * @param sourceA a source row
		 * @param sourceB another source row
		 * @return negative, zero or positive as <code>sourceA</code> sorts before, with or after <code>sourceB</code>
		 */
		int compare (int sourceA, int sourceB);
	}

	M3ColumnKey () {
	}

	/**
	 * Returns a key that sorts by an int value per source row, in <code>Integer.compare</code> order.
	 *
	 * @param value the value of a source row
	 * @return the key
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_NULL_ARGUMENT - if the function is null</li>
	 * </ul>
	 */
	public static M3ColumnKey ofInts (IntUnaryOperator value) {
		M3Checks.nonNull (value);
		return new M3ColumnKey () {
			@Override
			public int compare (int a, int b) {
				return Integer.compare (value.applyAsInt (a), value.applyAsInt (b));
			}

			@Override
			int radixKeys (int [] rows, int n, M3SortLanes lanes) {
				long [] keys = lanes.keys;
				for (int i = 0; i < n; i++) keys [i] = (value.applyAsInt (rows [i]) ^ Integer.MIN_VALUE) & 0xFFFFFFFFL;
				return 4;
			}
		};
	}

	/**
	 * Returns a key that sorts by a long value per source row, in <code>Long.compare</code> order.
	 *
	 * @param value the value of a source row
	 * @return the key
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_NULL_ARGUMENT - if the function is null</li>
	 * </ul>
	 */
	public static M3ColumnKey ofLongs (IntToLongFunction value) {
		M3Checks.nonNull (value);
		return new M3ColumnKey () {
			@Override
			public int compare (int a, int b) {
				return Long.compare (value.applyAsLong (a), value.applyAsLong (b));
			}

			@Override
			int radixKeys (int [] rows, int n, M3SortLanes lanes) {
				long [] keys = lanes.keys;
				for (int i = 0; i < n; i++) keys [i] = value.applyAsLong (rows [i]) ^ Long.MIN_VALUE;
				return 8;
			}
		};
	}

	/**
	 * Returns a key that sorts by a double value per source row, in <code>Double.compare</code>
	 * order (-0.0 before 0.0, NaN last).
	 *
	 * @param value the value of a source row
	 * @return the key
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_NULL_ARGUMENT - if the function is null</li>
	 * </ul>
	 */
	public static M3ColumnKey ofDoubles (IntToDoubleFunction value) {
		M3Checks.nonNull (value);
		return new M3ColumnKey () {
			@Override
			public int compare (int a, int b) {
				return Double.compare (value.applyAsDouble (a), value.applyAsDouble (b));
			}

			@Override
			int radixKeys (int [] rows, int n, M3SortLanes lanes) {
				long [] keys = lanes.keys;
				for (int i = 0; i < n; i++) {
					long bits = Double.doubleToLongBits (value.applyAsDouble (rows [i]));
					keys [i] = bits ^ (bits >> 63 | Long.MIN_VALUE);
				}
				return 8;
			}
		};
	}

	/**
	 * Returns a key that sorts by a text per source row. A <code>null</code> text sorts before
	 * every other text.
	 *
	 * @param text the text of a source row
	 * @param order the order of texts (for example a <code>java.text.Collator</code>), or
	 *        <code>null</code> for <code>String.compareTo</code>
	 * @return the key
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_NULL_ARGUMENT - if the text function is null</li>
	 * </ul>
	 */
	public static M3ColumnKey ofText (IntFunction<String> text, Comparator<? super String> order) {
		M3Checks.nonNull (text);
		Comparator<? super String> base = order == null ? Comparator.<String> naturalOrder () : order;
		Comparator<String> compare = Comparator.nullsFirst (base);
		return new M3ColumnKey () {
			@Override
			public int compare (int a, int b) {
				return compare.compare (text.apply (a), text.apply (b));
			}

			@Override
			int radixKeys (int [] rows, int n, M3SortLanes lanes) {
				String [] texts = lanes.texts (n);
				for (int i = 0; i < n; i++) texts [i] = text.apply (rows [i]);
				long maxRank = lanes.ranks (n, (i, j) -> compare.compare (texts [i], texts [j]));
				lanes.releaseTexts (n);
				return M3SortLanes.bytesFor (maxRank);
			}
		};
	}

	/**
	 * Returns a key that sorts source rows with a comparator, for example
	 * <code>M3SortKeys::compare</code> for collation keys.
	 *
	 * @param comparator the order of source rows
	 * @return the key
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_NULL_ARGUMENT - if the comparator is null</li>
	 * </ul>
	 */
	public static M3ColumnKey ofRows (RowComparator comparator) {
		M3Checks.nonNull (comparator);
		return new M3ColumnKey () {
			@Override
			public int compare (int a, int b) {
				return comparator.compare (a, b);
			}

			@Override
			int radixKeys (int [] rows, int n, M3SortLanes lanes) {
				return M3SortLanes.bytesFor (lanes.ranks (n, (i, j) -> comparator.compare (rows [i], rows [j])));
			}
		};
	}

	/**
	 * Returns a key that sorts by collation keys ({@link M3Collation}): source row <code>s</code>
	 * sorts by key <code>s</code>.
	 *
	 * @param keys the collation keys, one per source row
	 * @return the key
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_NULL_ARGUMENT - if the keys are null</li>
	 * </ul>
	 */
	public static M3ColumnKey ofSortKeys (M3SortKeys keys) {
		M3Checks.nonNull (keys);
		return ofRows (keys::compare);
	}

	/**
	 * Compares two source rows by this key, ascending.
	 *
	 * @param sourceA a source row
	 * @param sourceB another source row
	 * @return negative, zero or positive as <code>sourceA</code> sorts before, with or after <code>sourceB</code>
	 */
	public abstract int compare (int sourceA, int sourceB);

	/* writes the unsigned radix key of rows [i] into lanes.keys [i] for i < n; returns the key width in bytes */
	abstract int radixKeys (int [] rows, int n, M3SortLanes lanes);
}
