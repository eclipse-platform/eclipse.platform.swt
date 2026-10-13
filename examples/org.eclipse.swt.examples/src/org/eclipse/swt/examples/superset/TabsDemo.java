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
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;

/**
 * TabFolder / CTabFolder with N tab items (kind TABS). Neither has a virtual
 * mode, so every item is created; scrolling selects tab N, which the folder
 * must bring into view. Bound = tabs that fit across the header.
 */
final class TabsDemo extends SupersetDemo {

	private static final int TAB_WIDTH = 64;
	private UiState ui;

	TabsDemo(DemoSpec spec, Counters counters) {
		super(spec, counters);
	}

	@Override
	public Control create(Composite parent) {
		if ("CTabFolder".equals(spec.simpleName())) {
			CTabFolder folder = new CTabFolder(parent, SWT.BORDER | SWT.TOP);
			folder.setMRUVisible(false);
			return control = folder;
		}
		return control = new TabFolder(parent, SWT.TOP);
	}

	@Override
	public void setData(int n) {
		super.setData(n);
		int have = control instanceof CTabFolder cf ? cf.getItemCount() : ((TabFolder) control).getItemCount();
		for (int i = have; i < n; i++) addItem(i);
		counters.add(Counters.MATERIALIZE, n - have);
	}

	private void addItem(int i) {
		String label = "T" + i;
		if (control instanceof CTabFolder cf) new CTabItem(cf, SWT.NONE).setText(label);
		else new TabItem((TabFolder) control, SWT.NONE).setText(label);
	}

	@Override
	public void scrollTo(int row) {
		if (control instanceof CTabFolder cf) cf.setSelection(row);
		else ((TabFolder) control).setSelection(row);
	}

	@Override
	public String applyState() {
		if (rows < 4 || !(control instanceof CTabFolder cf)) return super.applyState();
		ui = new UiState(cf);
		cf.getItem(1).setFont(ui.bold);
		cf.getItem(2).setImage(ui.image);
		cf.getItem(3).setToolTipText("tip 3");
		return "item_font+item_image+item_tooltip+" + super.applyState();
	}

	@Override
	public String captureState() {
		if (ui == null) return super.captureState();
		CTabFolder cf = (CTabFolder) control;
		return "font1=" + ui.font(cf.getItem(1).getFont()) + "|img2=" + (cf.getItem(2).getImage() != null) + "|tip3="
				+ cf.getItem(3).getToolTipText() + "|" + super.captureState();
	}

	@Override
	public int visibleRows() {
		return Math.max(1, control.getSize().x / TAB_WIDTH);
	}
}
