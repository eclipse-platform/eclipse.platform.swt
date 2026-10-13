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

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

/**
 * Nebula widgets whose API takes one object per row and has no virtual mode:
 * CTree (NCTREE), TableCombo (NTABLECOMBO), CTreeCombo (NCTREECOMBO), DualList
 * (NDUALLIST) and PropertyTable (NPROPTABLE). Every row object is created up
 * front because the API requires it; materialized = rows.
 */
final class NebulaItemsDemo extends SupersetDemo {

	private static final String NEB = "org.eclipse.nebula.widgets.";
	private Object widget;
	private Object[] made = new Object[0];

	NebulaItemsDemo(DemoSpec spec, Counters counters) {
		super(spec, counters);
	}

	@Override
	public Control create(Composite parent) {
		int style = SWT.BORDER | ("NTABLECOMBO".equals(spec.kind()) || "NCTREECOMBO".equals(spec.kind()) ? SWT.READ_ONLY
				: SWT.V_SCROLL | SWT.H_SCROLL | SWT.FULL_SELECTION);
		widget = Reflect.make(spec.widgetClass(), parent, "NDUALLIST".equals(spec.kind()) || "NPROPTABLE".equals(spec.kind()) ? SWT.NONE : style);
		if ("NCTREE".equals(spec.kind())) {
			for (int c = 0; c < 2; c++) {
				Item col = (Item) Reflect.make(NEB + "ctree.CTreeColumn", widget, SWT.NONE);
				col.setText("C" + c);
				Reflect.call(col, "setWidth", 180);
			}
			Reflect.call(widget, "setHeaderVisible", true);
		}
		return control = (Control) widget;
	}

	@Override
	public void setData(int n) {
		super.setData(n);
		switch (spec.kind()) {
			case "NCTREE" -> items(n, NEB + "ctree.CTreeItem");
			case "NCTREECOMBO" -> items(n, NEB + "ctreecombo.CTreeComboItem");
			case "NTABLECOMBO" -> tableItems(n);
			case "NDUALLIST" -> dualList(n);
			case "NPROPTABLE" -> properties(n);
			default -> throw new IllegalStateException(spec.kind());
		}
		counters.add(Counters.MATERIALIZE, n);
	}

	private void items(int n, String itemClass) {
		Constructor<?> ctor = Reflect.constructor(itemClass, widget, SWT.NONE);
		made = new Object[n];
		for (int i = 0; i < n; i++) {
			Item item = (Item) Reflect.create(ctor, widget, SWT.NONE);
			item.setText(text.cell(i, 0));
			made[i] = item;
		}
	}

	private void tableItems(int n) {
		Table table = (Table) Reflect.call(widget, "getTable");
		for (int i = 0; i < n; i++) new TableItem(table, SWT.NONE).setText(text.cell(i, 0));
	}

	private void dualList(int n) {
		Class<?> type = Reflect.type(NEB + "opal.duallist.DLItem");
		Object[] items = (Object[]) Array.newInstance(type, n);
		Constructor<?> ctor = Reflect.constructor(type.getName(), "");
		for (int i = 0; i < n; i++) items[i] = Reflect.create(ctor, text.cell(i, 0));
		Reflect.call(widget, "setItems", items);
	}

	private void properties(int n) {
		Constructor<?> ctor = Reflect.constructor(NEB + "opal.propertytable.PTProperty", "", "", "", (Object) 0);
		Method add = Reflect.find(widget.getClass().getName(), "addProperty", ctor.getDeclaringClass());
		for (int i = 0; i < n; i++) {
			String name = text.cell(i, 0);
			Reflect.exec(add, widget, Reflect.create(ctor, name, name, "generated", (Object) Integer.valueOf(i)));
		}
		Reflect.call(widget, "viewAsFlatList");
	}

	@Override
	public void updateData() {
		if ("NCTREE".equals(spec.kind()) || "NCTREECOMBO".equals(spec.kind())) {
			Item item = (Item) Reflect.make(spec.kind().equals("NCTREE") ? NEB + "ctree.CTreeItem" : NEB + "ctreecombo.CTreeComboItem", widget, SWT.NONE);
			item.setText(text.cell(rows++, 0));
			return;
		}
		if ("NTABLECOMBO".equals(spec.kind())) new TableItem((Table) Reflect.call(widget, "getTable"), SWT.NONE).setText(text.cell(rows++, 0));
	}

	@Override
	public void scrollTo(int row) {
		switch (spec.kind()) {
			case "NCTREE" -> Reflect.call(widget, "setTopItem", made[row]);
			case "NCTREECOMBO" -> Reflect.call(widget, "select", made[row]);
			case "NTABLECOMBO" -> Reflect.call(widget, "select", row);
			default -> super.scrollTo(row);
		}
	}

	@Override
	public String applyState() {
		if (!"NCTREE".equals(spec.kind()) || made.length < 4) return super.applyState();
		Object[] sel = (Object[]) Array.newInstance(made[0].getClass(), 2);
		sel[0] = made[1];
		sel[1] = made[3];
		Reflect.call(widget, "setSelection", (Object) sel);
		return "selection+" + super.applyState();
	}

	@Override
	public boolean select(int i) {
		if (made.length == 0) return false;
		Object item = made[i % Math.min(made.length, Math.max(1, visibleRows() - 1))];
		Reflect.call(widget, "NCTREE".equals(spec.kind()) ? "setSelection" : "select", item);
		return true;
	}

	@Override
	public String captureState() {
		if (!"NCTREE".equals(spec.kind()) || made.length < 4) return super.captureState();
		StringBuilder b = new StringBuilder("sel=");
		for (Object it : (Object[]) Reflect.call(widget, "getSelection")) b.append(((Item) it).getText()).append(',');
		return b.append('|').append(super.captureState()).toString();
	}

	@Override
	public int visibleRows() {
		if ("NCTREE".equals(spec.kind())) return fit((Integer) Reflect.call(widget, "getItemHeight"));
		return "NDUALLIST".equals(spec.kind()) || "NPROPTABLE".equals(spec.kind()) ? fit(20) : 1;
	}
}
