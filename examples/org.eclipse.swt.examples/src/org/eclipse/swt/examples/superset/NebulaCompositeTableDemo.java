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

import java.lang.reflect.Method;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

/**
 * Nebula CompositeTable (kind NCOMPTABLE): a header and a row prototype are
 * declared as children; at run time the table clones only the rows that fit
 * and rebinds them through IRowContentProvider.refresh, so scale is the
 * collection size and materialized = rows bound by the first paint.
 */
final class NebulaCompositeTableDemo extends SupersetDemo {

	private static final String PKG = "org.eclipse.nebula.widgets.compositetable.";
	private Composite table;
	private Method setTop;
	private boolean stated;

	/** Header prototype (public: CompositeTable clones children reflectively). */
	public static final class HeaderComposite extends Composite {
		public HeaderComposite(Composite parent, int style) {
			super(parent, style);
			setLayout(new GridLayout(2, true));
			new Label(this, SWT.NONE).setText("Row");
			new Label(this, SWT.NONE).setText("Value");
		}
	}

	/** Row prototype: two labels, rebound per visible row. */
	public static final class RowComposite extends Composite {
		final Label key, value;

		public RowComposite(Composite parent, int style) {
			super(parent, style);
			setLayout(new GridLayout(2, true));
			key = new Label(this, SWT.NONE);
			value = new Label(this, SWT.NONE);
		}
	}

	NebulaCompositeTableDemo(DemoSpec spec, Counters counters) {
		super(spec, counters);
	}

	@Override
	public Control create(Composite parent) {
		table = (Composite) Reflect.make(PKG + "CompositeTable", parent, SWT.BORDER);
		new HeaderComposite(table, SWT.NONE);
		new RowComposite(table, SWT.NONE);
		Object provider = Reflect.proxy(PKG + "IRowContentProvider", (p, m, a) -> {
			if (!"refresh".equals(m.getName())) return m.getName().equals("toString") ? "rows" : null;
			RowComposite row = (RowComposite) a[2];
			int index = (Integer) a[1];
			row.key.setText(Integer.toString(index));
			row.value.setText(text.cell(index, 1));
			counters.inc(Counters.MATERIALIZE);
			return null;
		});
		Reflect.call(table, "addRowContentProvider", provider);
		Reflect.call(table, "setRunTime", true);
		setTop = Reflect.find(PKG + "CompositeTable", "setTopRow", int.class);
		return control = table;
	}

	@Override
	public void setData(int n) {
		super.setData(n);
		Reflect.call(table, "setNumRowsInCollection", n);
	}

	@Override
	public void updateData() {
		rows++;
		Reflect.call(table, "setNumRowsInCollection", rows);
		Reflect.call(table, "refreshAllRows");
	}

	@Override
	public void scrollTo(int row) {
		Reflect.exec(setTop, table, row);
	}

	@Override
	public String applyState() {
		if (rows < 4) return super.applyState();
		Reflect.call(table, "setSelection", 0, 2);
		stated = true;
		return "selection+" + super.applyState();
	}

	@Override
	public String captureState() {
		return stated ? "sel=" + Reflect.call(table, "getSelection") + "|" + super.captureState() : super.captureState();
	}

	@Override
	public int visibleRows() {
		return Math.max(1, (Integer) Reflect.call(table, "getNumRowsVisible"));
	}
}
