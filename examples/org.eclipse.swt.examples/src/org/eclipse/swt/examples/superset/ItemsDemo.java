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
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.List;

/**
 * Item-array widgets with no virtual mode (kinds LIST, COMBO): SWT List,
 * Combo and CCombo. Their API asks for a String[] of every item, so the demo
 * allocates exactly that array (once, reused for the hidden update) and
 * nothing else; materialized = item count. Combo scrolling is selection, so the
 * round-trip test covers font, colour and focus there, plus selection for List.
 */
final class ItemsDemo extends SupersetDemo {

	private String[] items = new String[0];
	private UiState ui;

	ItemsDemo(DemoSpec spec, Counters counters) {
		super(spec, counters);
	}

	@Override
	public Control create(Composite parent) {
		String name = spec.simpleName();
		if ("List".equals(name)) control = new List(parent, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL);
		else if ("CCombo".equals(name)) control = new CCombo(parent, SWT.BORDER | SWT.READ_ONLY);
		else control = new Combo(parent, SWT.DROP_DOWN | SWT.READ_ONLY);
		return control;
	}

	@Override
	public void setData(int n) {
		super.setData(n);
		if (items.length != n) {
			items = new String[n];
			for (int i = 0; i < n; i++) items[i] = text.cell(i, 0);
		}
		apply();
		counters.add(Counters.MATERIALIZE, n);
	}

	private void apply() {
		if (control instanceof List list) list.setItems(items);
		else if (control instanceof CCombo cc) cc.setItems(items);
		else ((Combo) control).setItems(items);
	}

	@Override
	public void updateData() {
		apply();
	}

	@Override
	public void scrollTo(int row) {
		if (control instanceof List list) list.setTopIndex(row);
		else if (control instanceof CCombo cc) cc.select(row);
		else ((Combo) control).select(row);
	}

	@Override
	public String applyState() {
		ui = new UiState(control);
		control.setFont(ui.bold);
		control.setForeground(ui.red);
		if (control instanceof List list && items.length > 3) {
			list.setSelection(new int[] {1, 3});
			return "selection+font+colour+" + super.applyState();
		}
		return "font+colour+" + super.applyState();
	}

	@Override
	public String captureState() {
		if (ui == null) return super.captureState();
		String sel = control instanceof List list ? java.util.Arrays.toString(list.getSelectionIndices()) : "n/a";
		return "sel=" + sel + "|font=" + ui.font(control.getFont()) + "|fg=" + UiState.rgb(control.getForeground()) + "|"
				+ super.captureState();
	}

	@Override
	public int visibleRows() {
		return control instanceof List list ? fit(list.getItemHeight()) : 1;
	}
}
