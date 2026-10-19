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
 *     Hitesh Soliwal (Synexia) - adapted from TV_5.0 treeviewer RegexFilter, WildCardFilter,
 *         MultiPropertyTextFilter and ElementFilter (docs/m3/sort-filter/PROVENANCE.tsv)
 *******************************************************************************/
package org.eclipse.swt.m3;

import java.util.*;
import java.util.function.*;

/*
 * One criterion of M3ColumnFilter: a text match over one or more columns (a row matches when any
 * of the columns matches; NEGATE inverts the whole), a text value list, or an id value list, plus
 * its logic with the criteria before it and its mask lane (bit = source row passes).
 */
final class M3FilterCriterion {

	int logic = M3ColumnFilter.AND;
	M3TextMatch match;
	int [] columns = {};
	Set<String> values;
	IntUnaryOperator valueId;
	long [] allowed;
	long [] mask = {};

	boolean isActive () {
		return match != null || values != null || valueId != null;
	}

	void clear () {
		match = null;
		values = null;
		valueId = null;
		allowed = null;
		columns = new int [0];
	}

	boolean test (int row, M3CellText text) {
		if (match != null) {
			boolean any = false;
			for (int i = 0; i < columns.length && !any; i++) any = match.any (text.text (row, columns [i]));
			return any != match.negated ();
		}
		if (values != null) {
			String value = text.text (row, columns [0]);
			return values.contains (value == null ? "" : value); //$NON-NLS-1$
		}
		int id = valueId.applyAsInt (row);
		return id >= 0 && id < allowed.length << 6 && M3RowMask.get (allowed, id);
	}

	/* evaluates the rows [start, end) into the mask */
	void evaluate (int start, int end, M3CellText text) {
		for (int row = start; row < end; row++) M3RowMask.set (mask, row, test (row, text));
	}
}
