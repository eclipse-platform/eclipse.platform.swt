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
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Layout;

/**
 * StackLayout panes (kind STACK, after SWT Snippet249): one Composite whose
 * StackLayout shows one of N heavy panes (24 labels each). Scrolling switches
 * the top pane, so the sweep measures pane-switch time; paints and layouts of
 * panes that are not on top are counted as hidden-pane work (target 0).
 */
final class StackDemo extends SupersetDemo {

	private static final int LABELS = 24, COLUMNS = 4;
	private final StackLayout stack = new StackLayout();
	private Composite[] panes = new Composite[0];
	private int hiddenPanePaints, hiddenPaneLayouts, panesPainted;
	private boolean[] painted = new boolean[0];

	/** Grid of labels; counts layouts that happen while its pane is not on top. */
	private final class PaneLayout extends Layout {
		@Override
		protected Point computeSize(Composite c, int wHint, int hHint, boolean flush) {
			return new Point(COLUMNS * 120, (LABELS / COLUMNS) * 22);
		}

		@Override
		protected void layout(Composite c, boolean flush) {
			if (stack.topControl != c) hiddenPaneLayouts++;
			Rectangle a = c.getClientArea();
			Control[] kids = c.getChildren();
			for (int i = 0; i < kids.length; i++) kids[i].setBounds(a.x + (i % COLUMNS) * (a.width / COLUMNS), a.y + (i / COLUMNS) * 22, a.width / COLUMNS, 20);
		}
	}

	StackDemo(DemoSpec spec, Counters counters) {
		super(spec, counters);
	}

	@Override
	public Control create(Composite parent) {
		Composite c = new Composite(parent, SWT.NONE);
		c.setLayout(stack);
		return control = c;
	}

	@Override
	public void setData(int n) {
		super.setData(n);
		Composite parent = (Composite) control;
		panes = new Composite[n];
		painted = new boolean[n];
		for (int p = 0; p < n; p++) panes[p] = pane(parent, p);
		stack.topControl = panes[0];
		parent.layout();
	}

	private Composite pane(Composite parent, int p) {
		Composite pane = new Composite(parent, SWT.NO_FOCUS);
		pane.setLayout(new PaneLayout());
		for (int i = 0; i < LABELS; i++) {
			Label l = new Label(pane, SWT.NO_FOCUS);
			l.setText(text.cell(p * LABELS + i, 0));
			l.addListener(SWT.Paint, e -> onPaint(pane, p));
		}
		pane.addListener(SWT.Paint, e -> onPaint(pane, p));
		return pane;
	}

	private void onPaint(Composite pane, int p) {
		if (stack.topControl != pane) hiddenPanePaints++;
		if (!painted[p]) {
			painted[p] = true;
			panesPainted++;
			counters.inc(Counters.MATERIALIZE);
		}
	}

	@Override
	public void scrollTo(int row) {
		if (panes.length == 0) return;
		stack.topControl = panes[row % panes.length];
		((Composite) control).layout();
	}

	@Override
	public void updateData() {
		for (Composite pane : panes) ((Label) pane.getChildren()[0]).setText(text.cell(rows++, 1));
	}

	@Override
	public String notes() {
		return "panes=" + panes.length + " panes_painted=" + panesPainted + " hidden_pane_paints=" + hiddenPanePaints
				+ " hidden_pane_layouts=" + hiddenPaneLayouts;
	}
}
