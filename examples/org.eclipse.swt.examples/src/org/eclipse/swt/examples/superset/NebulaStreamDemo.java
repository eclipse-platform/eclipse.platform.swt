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

import java.lang.reflect.Constructor;
import java.util.Calendar;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * Nebula widgets fed by a sequence: Oscilloscope (NOSC: N samples pushed as one
 * packed int[]; retention is the widget's ring buffer, not observable),
 * GanttChart (NGANTT: one GanttEvent per row, required by its API) and
 * paperclips PrintPreview (NPRINT: one TextPrint per line, lazy page layout,
 * scroll = page index).
 */
final class NebulaStreamDemo extends SupersetDemo {

	private static final String PC = "org.eclipse.nebula.paperclips.";
	private static final int LINES_PER_PAGE = 50;
	private Object widget, gantt;
	private Object[] events = new Object[0];

	NebulaStreamDemo(DemoSpec spec, Counters counters) {
		super(spec, counters);
	}

	@Override
	public Control create(Composite parent) {
		widget = Reflect.make(spec.widgetClass(), parent, "NPRINT".equals(spec.kind()) ? SWT.BORDER : SWT.NONE);
		if ("NPRINT".equals(spec.kind())) Reflect.call(widget, "setLazyPageLayout", true);
		if ("NGANTT".equals(spec.kind())) gantt = Reflect.call(widget, "getGanttComposite");
		return control = (Control) widget;
	}

	@Override
	public void setData(int n) {
		super.setData(n);
		switch (spec.kind()) {
			case "NOSC" -> samples(n);
			case "NGANTT" -> ganttEvents(n);
			default -> printJob(n);
		}
	}

	private void samples(int n) {
		int[] values = new int[n];
		for (int i = 0; i < n; i++) values[i] = (RowText.value(i, 0) >> 24);
		Reflect.call(widget, "setValues", 0, values);
		control.redraw();
	}

	private void ganttEvents(int n) {
		Constructor<?> ctor = Reflect.constructor("org.eclipse.nebula.widgets.ganttchart.GanttEvent", widget, "",
				Calendar.getInstance(), Calendar.getInstance(), 0);
		events = new Object[n];
		for (int i = 0; i < n; i++) {
			Calendar start = Calendar.getInstance();
			start.add(Calendar.DATE, i % 365);
			Calendar end = (Calendar) start.clone();
			end.add(Calendar.DATE, 1 + i % 7);
			events[i] = Reflect.create(ctor, widget, text.cell(i, 0), start, end, i % 100);
		}
		counters.add(Counters.MATERIALIZE, n);
		Reflect.call(gantt, "redraw");
	}

	private void printJob(int n) {
		Object grid = Reflect.make(PC + "core.grid.GridPrint", "d:g");
		Constructor<?> line = Reflect.constructor(PC + "core.text.TextPrint", "");
		java.lang.reflect.Method add = Reflect.find(grid.getClass().getName(), "add", Reflect.type(PC + "core.Print"));
		for (int i = 0; i < n; i++) Reflect.exec(add, grid, Reflect.create(line, text.cell(i, 0)));
		Reflect.call(widget, "setPrintJob", Reflect.make(PC + "core.PrintJob", "superset", grid));
		counters.add(Counters.MATERIALIZE, n);
	}

	@Override
	public void updateData() {
		if ("NOSC".equals(spec.kind())) Reflect.call(widget, "setValue", 0, 1);
		else setData(rows + 1);
	}

	@Override
	public void scrollTo(int row) {
		if ("NGANTT".equals(spec.kind()) && events.length > 0) {
			Reflect.call(gantt, "setTopItem", events[row], SWT.LEFT);
		} else if ("NPRINT".equals(spec.kind())) {
			int pages = Math.max(1, (Integer) Reflect.call(widget, "getPageCount"));
			Reflect.call(widget, "setPageIndex", Math.min(pages - 1, row / LINES_PER_PAGE));
		} else {
			control.redraw();
		}
	}

	@Override
	public int materialized() {
		return "NOSC".equals(spec.kind()) ? -1 : super.materialized();
	}

	@Override
	public int visibleRows() {
		return "NPRINT".equals(spec.kind()) ? LINES_PER_PAGE : fit(24);
	}
}
