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

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.swt.widgets.Scrollable;

/**
 * One widget demo, shared by the interactive showcase and the matrix runner
 * (the role {@code Tab} plays in ControlExample). A demo creates its widget in
 * the large-content configuration, fills generated data on request, and
 * exposes the scroll API the runner drives.
 */
public abstract class SupersetDemo {

	protected final DemoSpec spec;
	protected final Counters counters;
	protected final RowText text = new RowText();
	protected Control control;
	protected int rows;

	protected SupersetDemo(DemoSpec spec, Counters counters) {
		this.spec = spec;
		this.counters = counters;
	}

	/** Creates the widget (no data yet) and returns its top control. */
	public abstract Control create(Composite parent);

	/** Fills {@code n} generated rows/lines/points/nodes; small demos ignore n. */
	public void setData(int n) {
		rows = n;
	}

	/** Changes the data while the demo is hidden (visibility law probe). */
	public void updateData() {
		setData(Math.max(1, rows + 1));
	}

	/** Scrolls so that {@code row} is the top row; small demos redraw. */
	public void scrollTo(int row) {
		if (!scrollByBar(row)) control.redraw();
	}

	/** @return rows that fit in the viewport (the materialization bound) */
	public int visibleRows() {
		return 1;
	}

	/**
	 * @return true when the MATERIALIZE counter means materialization (SetData,
	 *         item objects, line layouts); false when it counts rows a paint
	 *         merely draws (a custom Canvas), which is painting, not materializing
	 */
	public boolean paintCountsAsMaterialization() {
		return true;
	}

	/** @return rows/items/lines created so far */
	public int materialized() {
		return spec.largeContent() ? counters.get(Counters.MATERIALIZE) : 1;
	}

	/**
	 * Round-trip reproduction, step 1: sets a representative UI state mix on
	 * rows near the top (selection, check, expansion, focus, font, colour,
	 * image, open cell editor, as the widget supports).
	 *
	 * @return the state names tested, '+'-joined
	 */
	public String applyState() {
		return control.setFocus() ? "focus" : "focus(denied)";
	}

	/** @return the public-API values of the state set by {@link #applyState()} */
	public String captureState() {
		Control focus = control.getDisplay().getFocusControl();
		while (focus != null && focus != control) focus = focus.getParent();
		return "focus_within=" + (focus != null);
	}

	/**
	 * Damage probe: changes the selection to visible row {@code i}.
	 *
	 * @return false when the widget has no selection to change
	 */
	public boolean select(int i) {
		return false;
	}

	/**
	 * Damage probe: moves the caret (text widgets) or the pointer hover to
	 * visible row {@code i}. Hover is a synthetic SWT.MouseMove, which only
	 * custom-drawn (Canvas) widgets observe, so native widgets return false.
	 */
	public boolean caretOrHover(int i) {
		if (!(control instanceof org.eclipse.swt.widgets.Canvas)) return false;
		Event e = new Event();
		e.x = 24;
		e.y = 6 + (i * 18) % Math.max(1, control.getSize().y - 12);
		control.notifyListeners(SWT.MouseMove, e);
		return true;
	}

	/** @return extra key=value facts for the notes column */
	public String notes() {
		return "";
	}

	public Control control() {
		return control;
	}

	public DemoSpec spec() {
		return spec;
	}

	public int rows() {
		return rows;
	}

	/**
	 * Scrolls a custom-drawn widget through its vertical ScrollBar the way a
	 * user drag does: set the selection, then send SWT.Selection.
	 */
	protected boolean scrollByBar(int row) {
		if (!(control instanceof Scrollable s) || s.getVerticalBar() == null) return false;
		ScrollBar bar = s.getVerticalBar();
		int range = Math.max(0, bar.getMaximum() - bar.getThumb() - bar.getMinimum());
		long denom = Math.max(1, rows - 1);
		bar.setSelection(bar.getMinimum() + (int) (range * (long) row / denom));
		Event e = new Event();
		e.detail = SWT.DRAG;
		bar.notifyListeners(SWT.Selection, e);
		return true;
	}

	/** @return rows of height {@code itemHeight} that fit in the client area */
	protected int fit(int itemHeight) {
		if (!(control instanceof Scrollable s) || itemHeight <= 0) return 1;
		return (s.getClientArea().height + itemHeight - 1) / itemHeight;
	}
}
