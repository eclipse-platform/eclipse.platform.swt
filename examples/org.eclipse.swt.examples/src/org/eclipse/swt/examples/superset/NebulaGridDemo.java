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
package org.eclipse.swt.examples.superset;

import java.lang.reflect.Method;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Item;

/**
 * Nebula Grid in SWT.VIRTUAL mode (kind NGRID: scale = rows) and the column
 * variant (kind NGRIDCOLS: scale = GridColumns over a 1K-row virtual body),
 * following the Grid snippets; bound late because Nebula is a runtime jar.
 */
final class NebulaGridDemo extends SupersetDemo {

	private static final String PKG = "org.eclipse.nebula.widgets.grid.";
	private static final int COLUMNS = 3, COLUMN_BODY_ROWS = 1_000, NARROW = 60;
	private Composite grid;
	private int columns = COLUMNS;
	private static final int STATE_ROWS = 10;
	private Method setTop;
	private UiState ui;

	NebulaGridDemo(DemoSpec spec, Counters counters) {
		super(spec, counters);
	}

	private boolean columnScale() {
		return "NGRIDCOLS".equals(spec.kind());
	}

	@Override
	public Control create(Composite parent) {
		grid = (Composite) Reflect.make(PKG + "Grid", parent, SWT.VIRTUAL | SWT.CHECK | SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL | SWT.MULTI);
		Reflect.call(grid, "setHeaderVisible", true);
		Reflect.call(grid, "setLinesVisible", true);
		Method setText = Reflect.find(PKG + "GridItem", "setText", int.class, String.class);
		setTop = Reflect.find(PKG + "Grid", "setTopIndex", int.class);
		grid.addListener(SWT.SetData, e -> {
			for (int c = 0; c < Math.min(columns, 8); c++) Reflect.exec(setText, e.item, c, text.cell(e.index, c));
			if (!columnScale()) counters.inc(Counters.MATERIALIZE);
		});
		if (!columnScale()) addColumns(COLUMNS);
		return control = grid;
	}

	private void addColumns(int n) {
		int have = (Integer) Reflect.call(grid, "getColumnCount");
		for (int c = have; c < n; c++) {
			Item col = (Item) Reflect.make(PKG + "GridColumn", grid, SWT.NONE);
			col.setText("C" + c);
			Reflect.call(col, "setWidth", columnScale() ? NARROW : 120);
			if (columnScale()) counters.inc(Counters.MATERIALIZE);
		}
	}

	@Override
	public void setData(int n) {
		super.setData(n);
		if (columnScale()) {
			columns = n;
			addColumns(n);
			Reflect.call(grid, "setItemCount", COLUMN_BODY_ROWS);
			return;
		}
		Reflect.call(grid, "setItemCount", n);
	}

	@Override
	public void updateData() {
		if (columnScale()) {
			setData(rows + 1);
			return;
		}
		rows++;
		Reflect.call(grid, "setItemCount", rows);
		Reflect.call(grid, "clearAll", true);
	}

	@Override
	public void scrollTo(int row) {
		if (columnScale()) {
			Reflect.call(grid, "showColumn", Reflect.call(grid, "getColumn", row));
			return;
		}
		Reflect.exec(setTop, grid, row);
	}

	@Override
	public String applyState() {
		if (rows < STATE_ROWS || columnScale()) return super.applyState();
		ui = new UiState(grid);
		Reflect.call(grid, "setSelection", (Object) new int[] {1, 3});
		Reflect.call(item(2), "setChecked", true);
		Reflect.call(item(4), "setFont", ui.bold);
		Reflect.call(item(5), "setForeground", ui.red);
		Reflect.call(item(6), "setBackground", ui.yellow);
		Reflect.call(item(7), "setImage", ui.image);
		return "selection+check+font+colour+image+" + super.applyState();
	}

	@Override
	public boolean select(int i) {
		if (rows == 0 || columnScale()) return false;
		int top = (Integer) Reflect.call(grid, "getTopIndex");
		Reflect.call(grid, "setSelection", top + i % Math.max(1, visibleRows() - 1));
		return true;
	}

	private Object item(int i) {
		return Reflect.call(grid, "getItem", i);
	}

	@Override
	public String captureState() {
		if (ui == null) return super.captureState();
		StringBuilder b = new StringBuilder("sel=").append(java.util.Arrays.toString((int[]) Reflect.call(grid, "getSelectionIndices")));
		for (int i = 0; i < STATE_ROWS; i++) {
			Object it = item(i);
			b.append('|').append(Reflect.call(it, "getText", 0)).append(',').append(Reflect.call(it, "getChecked"))
					.append(',').append(ui.font((org.eclipse.swt.graphics.Font) Reflect.call(it, "getFont")))
					.append(',').append(UiState.rgb((org.eclipse.swt.graphics.Color) Reflect.call(it, "getForeground")))
					.append(',').append(UiState.rgb((org.eclipse.swt.graphics.Color) Reflect.call(it, "getBackground")))
					.append(',').append(Reflect.call(it, "getImage") != null);
		}
		return b.append('|').append(super.captureState()).toString();
	}

	@Override
	public int visibleRows() {
		if (columnScale()) return (grid.getClientArea().width + NARROW - 1) / NARROW;
		return fit((Integer) Reflect.call(grid, "getItemHeight"));
	}
}
