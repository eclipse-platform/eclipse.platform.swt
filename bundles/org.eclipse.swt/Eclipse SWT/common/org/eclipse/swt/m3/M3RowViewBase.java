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

/*
 * The shared part of the views: the listener lane, the change object, and the source edits. The
 * first view of a chain (no input) owns the source size and turns every edit into a change of
 * its identity source; the other views forward edits to their input, so an edit reported to any
 * view flows down the whole chain.
 */
abstract class M3RowViewBase implements M3RowView {

	private static final M3RowView.Listener [] NO_LISTENERS = {};

	final M3RowView input;
	final M3RowChange change = new M3RowChange (this);
	private final M3RowChange edit;
	private final M3RowView.Listener inputListener;
	private M3RowView.Listener [] listeners = NO_LISTENERS;
	private int rootSourceSize;

	M3RowViewBase (M3RowView input, int sourceSize) {
		this.input = input;
		if (input == null) {
			M3Checks.argument (sourceSize >= 0);
			rootSourceSize = sourceSize;
			edit = new M3RowChange (null);
			inputListener = null;
		} else {
			edit = null;
			inputListener = this::inputChanged;
			input.addListener (inputListener);
		}
	}

	/* applies a change of the input (or of the identity source) and fires this view's change */
	abstract void inputChanged (M3RowChange change);

	@Override
	public int sourceSize () {
		return input == null ? rootSourceSize : input.sourceSize ();
	}

	@Override
	public void setSourceSize (int sourceSize) {
		if (input != null) {
			input.setSourceSize (sourceSize);
			return;
		}
		M3Checks.argument (sourceSize >= 0);
		rootSourceSize = sourceSize;
		edit.set (M3RowChange.RESET, 0, 0);
		inputChanged (edit);
	}

	@Override
	public void rowsInserted (int start, int count) {
		if (input != null) {
			input.rowsInserted (start, count);
			return;
		}
		M3Checks.span (start, 0, rootSourceSize);
		M3Checks.argument (count >= 0);
		rootSourceSize += count;
		edit.set (M3RowChange.INSERTED, start, count);
		for (int row = start; row < start + count; row++) edit.add (row);
		inputChanged (edit);
	}

	@Override
	public void rowsRemoved (int start, int count) {
		if (input != null) {
			input.rowsRemoved (start, count);
			return;
		}
		M3Checks.span (start, count, rootSourceSize);
		rootSourceSize -= count;
		edit.set (M3RowChange.REMOVED, start, count);
		inputChanged (edit);
	}

	@Override
	public void rowsChanged (int... sourceRows) {
		if (input != null) {
			input.rowsChanged (sourceRows);
			return;
		}
		M3Checks.nonNull (sourceRows);
		int [] rows = sourceRows.clone ();
		Arrays.sort (rows);
		edit.set (M3RowChange.UPDATED, 0, 0);
		for (int i = 0; i < rows.length; i++) {
			M3Checks.range (rows [i], rootSourceSize);
			if (i == 0 || rows [i] != rows [i - 1]) edit.add (rows [i]);
		}
		inputChanged (edit);
	}

	@Override
	public void addListener (M3RowView.Listener listener) {
		M3Checks.nonNull (listener);
		for (M3RowView.Listener l : listeners) {
			if (l == listener) return;
		}
		M3RowView.Listener [] grown = Arrays.copyOf (listeners, listeners.length + 1);
		grown [listeners.length] = listener;
		listeners = grown;
	}

	@Override
	public void removeListener (M3RowView.Listener listener) {
		M3Checks.nonNull (listener);
		for (int i = 0; i < listeners.length; i++) {
			if (listeners [i] == listener) {
				M3RowView.Listener [] shrunk = new M3RowView.Listener [listeners.length - 1];
				System.arraycopy (listeners, 0, shrunk, 0, i);
				System.arraycopy (listeners, i + 1, shrunk, i, shrunk.length - i);
				listeners = shrunk;
				return;
			}
		}
	}

	/**
	 * Detaches this view from its input view, so that it no longer follows the input. A view
	 * without an input is not affected.
	 */
	public void dispose () {
		if (input != null) input.removeListener (inputListener);
	}

	boolean hasListeners () {
		return listeners.length > 0;
	}

	void fire () {
		for (M3RowView.Listener listener : listeners) listener.viewChanged (change);
	}
}
