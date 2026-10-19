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

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TreeCursor;
import org.eclipse.swt.custom.TreeEditor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.swt.widgets.Text;

/**
 * SWT Tree in SWT.VIRTUAL mode (kinds VTREE, VTREE_CURSOR). Scale = root
 * nodes; every 10th root declares 3 lazy children (never expanded by the
 * runner), so SetData is the only materialization path.
 */
final class TreeDemo extends SupersetDemo {

	private static final int STATE_ROWS = 10;
	private Tree tree;
	private UiState ui;
	private Text editor;

	TreeDemo(DemoSpec spec, Counters counters) {
		super(spec, counters);
	}

	@Override
	public Control create(Composite parent) {
		tree = new Tree(parent, SWT.VIRTUAL | SWT.CHECK | SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI);
		tree.setHeaderVisible(true);
		for (int c = 0; c < 2; c++) {
			TreeColumn col = new TreeColumn(tree, SWT.NONE);
			col.setText("C" + c);
			col.setWidth(c == 0 ? 220 : 140);
		}
		tree.addListener(SWT.SetData, e -> {
			TreeItem item = (TreeItem) e.item;
			boolean root = item.getParentItem() == null;
			int row = root ? e.index : -1 - e.index;
			item.setText(0, text.cell(row, 0));
			item.setText(1, text.cell(row, 1));
			item.setItemCount(root && e.index % 10 == 0 ? 3 : 0);
			counters.inc(Counters.MATERIALIZE);
		});
		if ("VTREE_CURSOR".equals(spec.kind())) new TreeCursor(tree, SWT.NONE);
		return control = tree;
	}

	@Override
	public void setData(int n) {
		super.setData(n);
		tree.setItemCount(n);
	}

	@Override
	public void updateData() {
		rows++;
		tree.setItemCount(rows);
		tree.clearAll(false);
	}

	@Override
	public void scrollTo(int row) {
		tree.setTopItem(tree.getItem(row));
	}

	@Override
	public int visibleRows() {
		return fit(tree.getItemHeight());
	}

	@Override
	public String applyState() {
		if (tree.getItemCount() < STATE_ROWS) return super.applyState();
		ui = new UiState(tree);
		tree.getItem(0).setExpanded(true);
		tree.setSelection(new TreeItem[] {tree.getItem(1), tree.getItem(3)});
		tree.getItem(2).setChecked(true);
		tree.getItem(4).setFont(ui.bold);
		tree.getItem(5).setForeground(ui.red);
		tree.getItem(6).setBackground(ui.yellow);
		tree.getItem(7).setImage(ui.image);
		editor = new Text(tree, SWT.BORDER);
		editor.setText("cell editor");
		TreeEditor te = new TreeEditor(tree);
		te.grabHorizontal = true;
		te.setEditor(editor, tree.getItem(8), 1);
		return "expansion+selection+check+font+colour+image+editor+" + super.applyState();
	}

	@Override
	public String captureState() {
		if (ui == null) return super.captureState();
		StringBuilder b = new StringBuilder("sel=");
		for (TreeItem s : tree.getSelection()) b.append(tree.indexOf(s)).append(',');
		for (int i = 0; i < STATE_ROWS; i++) {
			TreeItem item = tree.getItem(i);
			b.append('|').append(item.getText(0)).append(',').append(item.getExpanded()).append(',').append(item.getChecked())
					.append(',').append(ui.font(item.getFont())).append(',').append(UiState.rgb(item.getForeground()))
					.append(',').append(UiState.rgb(item.getBackground())).append(',').append(item.getImage() != null);
		}
		return b.append("|editor=").append(editor.getText()).append(editor.getBounds()).append('|').append(super.captureState()).toString();
	}

	@Override
	public boolean select(int i) {
		if (tree.getItemCount() == 0) return false;
		tree.setSelection(tree.getItem(Math.min(tree.getItemCount() - 1, i % Math.max(1, visibleRows() - 4))));
		return true;
	}

	@Override
	public String notes() {
		return "item_height=" + tree.getItemHeight();
	}
}
