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

import java.util.Arrays;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableCursor;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

/**
 * SWT Table in SWT.VIRTUAL mode (kinds VTABLE, VTABLE_CURSOR) and the
 * column-scale variant (kind COLUMNS: scale = number of TableColumns over a
 * fixed 1K-row virtual body). Cells come from {@link RowText} in SetData.
 */
final class TableDemo extends SupersetDemo {

	private static final int COLUMN_WIDTH = 120, COLUMNS = 3, COLUMN_BODY_ROWS = 1_000;
	private Table table;
	private static final int STATE_ROWS = 10;
	private int columns = COLUMNS;
	private UiState ui;
	private Text editor;

	TableDemo(DemoSpec spec, Counters counters) {
		super(spec, counters);
	}

	private boolean columnScale() {
		return "COLUMNS".equals(spec.kind());
	}

	@Override
	public Control create(Composite parent) {
		table = new Table(parent, SWT.VIRTUAL | SWT.CHECK | SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		table.addListener(SWT.SetData, e -> {
			TableItem item = (TableItem) e.item;
			for (int c = 0; c < Math.min(columns, 8); c++) item.setText(c, text.cell(e.index, c));
			if (!columnScale()) counters.inc(Counters.MATERIALIZE);
		});
		if (!columnScale()) addColumns(COLUMNS);
		if ("VTABLE_CURSOR".equals(spec.kind())) new TableCursor(table, SWT.NONE);
		return control = table;
	}

	private void addColumns(int n) {
		for (int c = table.getColumnCount(); c < n; c++) {
			TableColumn col = new TableColumn(table, SWT.NONE);
			col.setText("C" + c);
			col.setWidth(columnScale() ? 60 : COLUMN_WIDTH);
			if (columnScale()) counters.inc(Counters.MATERIALIZE);
		}
	}

	@Override
	public void setData(int n) {
		super.setData(n);
		if (columnScale()) {
			columns = n;
			addColumns(n);
			table.setItemCount(COLUMN_BODY_ROWS);
			return;
		}
		table.setItemCount(n);
	}

	@Override
	public void updateData() {
		if (columnScale()) {
			setData(rows + 1);
			return;
		}
		rows++;
		table.setItemCount(rows);
		table.clearAll();
	}

	@Override
	public void scrollTo(int row) {
		if (columnScale()) {
			table.showColumn(table.getColumn(row));
			return;
		}
		table.setTopIndex(row);
	}

	@Override
	public int visibleRows() {
		if (columnScale()) return (table.getClientArea().width + 59) / 60;
		return fit(table.getItemHeight());
	}

	@Override
	public String applyState() {
		if (table.getItemCount() < STATE_ROWS) return super.applyState();
		ui = new UiState(table);
		table.setSelection(new int[] {1, 3});
		table.getItem(2).setChecked(true);
		table.getItem(4).setFont(ui.bold);
		table.getItem(5).setForeground(ui.red);
		table.getItem(6).setBackground(ui.yellow);
		table.getItem(7).setImage(ui.image);
		editor = new Text(table, SWT.BORDER);
		editor.setText("cell editor");
		TableEditor te = new TableEditor(table);
		te.grabHorizontal = true;
		te.setEditor(editor, table.getItem(8), Math.min(1, table.getColumnCount() - 1));
		return "selection+check+font+colour+image+editor+" + super.applyState();
	}

	@Override
	public String captureState() {
		if (ui == null) return super.captureState();
		StringBuilder b = new StringBuilder("sel=").append(Arrays.toString(table.getSelectionIndices()));
		for (int i = 0; i < STATE_ROWS; i++) {
			TableItem item = table.getItem(i);
			b.append('|').append(item.getText(0)).append(',').append(item.getChecked()).append(',').append(ui.font(item.getFont()))
					.append(',').append(UiState.rgb(item.getForeground())).append(',').append(UiState.rgb(item.getBackground()))
					.append(',').append(item.getImage() != null);
		}
		return b.append("|editor=").append(editor.getText()).append(editor.getBounds()).append('|').append(super.captureState()).toString();
	}

	@Override
	public boolean select(int i) {
		if (table.getItemCount() == 0) return false;
		table.setSelection(table.getTopIndex() + i % Math.max(1, visibleRows() - 1));
		return true;
	}

	@Override
	public String notes() {
		return "columns=" + table.getColumnCount() + " item_height=" + table.getItemHeight();
	}
}
