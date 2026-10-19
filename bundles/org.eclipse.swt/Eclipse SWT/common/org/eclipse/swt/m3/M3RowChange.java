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


/**
 * A change of an {@link M3RowView} (enhanced M3 API).
 * <p>
 * The kind says what happened; the payload is a lane of source rows and, for insertions and
 * removals, a range of source rows. The damaged range names the view rows, after the change,
 * whose source row may differ from before; a widget bound to the view needs to refresh only
 * those rows. Each view owns one change object and reuses it, so listeners must read it during
 * the notification only.
 * </p>
 *
 * @see M3RowView.Listener
 * @since 3.138
 */
public final class M3RowChange {

	/** Everything may have changed: the view was rebuilt. */
	public static final int RESET = 1;

	/** Source rows <code>start .. start + count - 1</code> were inserted; the payload lists those the view shows. */
	public static final int INSERTED = 2;

	/** Source rows <code>start .. start + count - 1</code> were removed (numbered as before the removal). */
	public static final int REMOVED = 3;

	/** The source rows of the payload entered the view. */
	public static final int ENTERED = 4;

	/** The source rows of the payload left the view. */
	public static final int LEFT = 5;

	/** The values of the source rows of the payload changed; the rows stay in the view. */
	public static final int UPDATED = 6;

	/** The order of the view changed; the payload is empty. */
	public static final int ORDER = 7;

	private final M3RowView view;
	int kind, start, count, rowCount, firstDamaged = -1, lastDamaged = -1;
	int [] rows = new int [16];

	M3RowChange (M3RowView view) {
		this.view = view;
	}

	/**
	 * Returns the view that changed.
	 *
	 * @return the view
	 */
	public M3RowView view () {
		return view;
	}

	/**
	 * Returns the kind of change: {@link #RESET}, {@link #INSERTED}, {@link #REMOVED},
	 * {@link #ENTERED}, {@link #LEFT}, {@link #UPDATED} or {@link #ORDER}.
	 *
	 * @return the kind
	 */
	public int kind () {
		return kind;
	}

	/**
	 * Returns the first inserted or removed source row, or 0 for the other kinds.
	 *
	 * @return the start of the source range
	 */
	public int start () {
		return start;
	}

	/**
	 * Returns the number of inserted or removed source rows, or 0 for the other kinds.
	 *
	 * @return the length of the source range
	 */
	public int count () {
		return count;
	}

	/**
	 * Returns the number of source rows in the payload.
	 *
	 * @return the payload length
	 */
	public int rowCount () {
		return rowCount;
	}

	/**
	 * Returns a source row of the payload.
	 *
	 * @param index the index in the payload
	 * @return the source row
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_INVALID_RANGE - if the index is not in the payload</li>
	 * </ul>
	 */
	public int row (int index) {
		M3Checks.range (index, rowCount);
		return rows [index];
	}

	/**
	 * Returns the first damaged view row, or -1 when no row of the view is damaged.
	 *
	 * @return the first damaged view row
	 */
	public int firstDamaged () {
		return firstDamaged;
	}

	/**
	 * Returns the last damaged view row, or -1 when no row of the view is damaged.
	 *
	 * @return the last damaged view row
	 */
	public int lastDamaged () {
		return lastDamaged;
	}

	void set (int kind, int start, int count) {
		this.kind = kind;
		this.start = start;
		this.count = count;
		rowCount = 0;
		firstDamaged = lastDamaged = -1;
	}

	void add (int row) {
		if (rowCount == rows.length) rows = Arrays.copyOf (rows, rowCount * 2);
		rows [rowCount++] = row;
	}

	void damage (int first, int last) {
		if (first > last) return;
		firstDamaged = firstDamaged < 0 ? first : Math.min (firstDamaged, first);
		lastDamaged = Math.max (lastDamaged, last);
	}

	@Override
	public String toString () {
		return "M3RowChange [" + kindName (kind) + ", start " + start + ", count " + count + ", rows " + rowCount //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
			+ ", damaged " + firstDamaged + ".." + lastDamaged + "]"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}

	static String kindName (int kind) {
		switch (kind) {
			case RESET: return "RESET"; //$NON-NLS-1$
			case INSERTED: return "INSERTED"; //$NON-NLS-1$
			case REMOVED: return "REMOVED"; //$NON-NLS-1$
			case ENTERED: return "ENTERED"; //$NON-NLS-1$
			case LEFT: return "LEFT"; //$NON-NLS-1$
			case UPDATED: return "UPDATED"; //$NON-NLS-1$
			default: return "ORDER"; //$NON-NLS-1$
		}
	}
}
