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
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * Nebula visualization figures hosted by a draw2d LightweightSystem on a
 * Canvas: XYGraph with N points (NXYGRAPH; scroll pans a WINDOW-point x
 * range), IntensityGraphFigure with N cells as one packed int[] (NINTENSITY;
 * scroll moves the crop window) and small figures such as gauges (NFIGURE).
 * Charts keep every point by API and points drawn per frame are not observable
 * from outside the figure, so materialized is reported as not observable.
 */
final class NebulaFigureDemo extends SupersetDemo {

	private static final String XY = "org.eclipse.nebula.visualization.xygraph.";
	private static final int WINDOW = 2_000, HEAT_WIDTH = 1_000, HEAT_ROWS = 200;
	private Object figure, provider, xAxis;

	NebulaFigureDemo(DemoSpec spec, Counters counters) {
		super(spec, counters);
	}

	@Override
	public Control create(Composite parent) {
		Canvas canvas = new Canvas(parent, SWT.NONE);
		Object lws = Reflect.make("org.eclipse.draw2d.LightweightSystem", canvas);
		figure = Reflect.make(spec.widgetClass());
		if ("NXYGRAPH".equals(spec.kind())) xyGraph();
		if ("NFIGURE".equals(spec.kind())) Reflect.call(figure, "setValue", 42.0);
		Reflect.call(lws, "setContents", figure);
		return control = canvas;
	}

	private void xyGraph() {
		Reflect.call(figure, "setTitle", "XYGraph");
		provider = Reflect.make(XY + "dataprovider.CircularBufferDataProvider", false);
		xAxis = Reflect.call(figure, "getPrimaryXAxis");
		Object yAxis = Reflect.call(figure, "getPrimaryYAxis");
		Reflect.call(xAxis, "setAutoScale", false);
		Reflect.call(yAxis, "setAutoScale", false);
		Reflect.call(yAxis, "setRange", -1.0, 1.0);
		Object trace = Reflect.make(XY + "figures.Trace", "generated", xAxis, yAxis, provider);
		Reflect.call(figure, "addTrace", trace);
	}

	@Override
	public void setData(int n) {
		super.setData(n);
		if ("NXYGRAPH".equals(spec.kind())) points(n);
		if ("NINTENSITY".equals(spec.kind())) cells(n);
		control.redraw();
	}

	private void points(int n) {
		double[] xs = new double[n], ys = new double[n];
		for (int i = 0; i < n; i++) {
			xs[i] = i;
			ys[i] = RowText.value(i, 0) / (double) Integer.MAX_VALUE;
		}
		Reflect.call(provider, "setBufferSize", n);
		Reflect.call(provider, "setCurrentXDataArray", xs);
		Reflect.call(provider, "setCurrentYDataArray", ys);
		Reflect.call(xAxis, "setRange", 0.0, (double) Math.min(n, WINDOW));
	}

	private void cells(int n) {
		int height = Math.max(1, n / HEAT_WIDTH);
		int[] data = new int[HEAT_WIDTH * height];
		for (int i = 0; i < data.length; i++) data[i] = RowText.value(i, 0) & 0xFFFF;
		Reflect.call(figure, "setMin", 0.0);
		Reflect.call(figure, "setMax", 65535.0);
		Reflect.call(figure, "setDataWidth", HEAT_WIDTH);
		Reflect.call(figure, "setDataHeight", height);
		Reflect.call(figure, "setDataArray", data);
		rows = height;
	}

	@Override
	public void updateData() {
		if (spec.largeContent()) setData(spec.kind().equals("NINTENSITY") ? (rows + 1) * HEAT_WIDTH : rows + 1);
		else Reflect.call(figure, "setValue", 43.0);
	}

	@Override
	public void scrollTo(int row) {
		if ("NXYGRAPH".equals(spec.kind())) {
			Reflect.call(xAxis, "setRange", (double) row, (double) row + WINDOW);
		} else if ("NINTENSITY".equals(spec.kind())) {
			int top = Math.min(row, Math.max(0, rows - HEAT_ROWS));
			Reflect.call(figure, "setCropTop", top);
			Reflect.call(figure, "setCropBottom", Math.max(0, rows - top - HEAT_ROWS));
		}
		control.redraw();
	}

	@Override
	public int materialized() {
		return spec.largeContent() ? -1 : 1;
	}

	@Override
	public int visibleRows() {
		if ("NXYGRAPH".equals(spec.kind())) return WINDOW;
		return "NINTENSITY".equals(spec.kind()) ? HEAT_WIDTH * HEAT_ROWS : 1;
	}

	@Override
	public String notes() {
		return spec.largeContent() ? "model holds every point by API; drawn points not observable" : "";
	}
}
