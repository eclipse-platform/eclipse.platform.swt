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
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.ScrollBar;

/**
 * Custom-drawn rows on a Canvas. Kind CANVAS: the vertical ScrollBar counts
 * rows (logical scrolling, no pixel extent). Kind SCROLLED: the classic
 * ScrolledComposite pattern, a content Canvas N*ROW pixels tall moved by
 * setOrigin. Both paint only the rows inside the damage rectangle; rows drawn
 * by the first paint are the materialized count.
 */
final class CanvasDemo extends SupersetDemo {

	private static final int ROW = 18;
	private Canvas canvas;
	private ScrolledComposite scrolled;

	CanvasDemo(DemoSpec spec, Counters counters) {
		super(spec, counters);
	}

	@Override
	public Control create(Composite parent) {
		if ("SCROLLED".equals(spec.kind())) {
			scrolled = new ScrolledComposite(parent, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
			canvas = new Canvas(scrolled, SWT.NO_BACKGROUND);
			scrolled.setContent(canvas);
			canvas.addPaintListener(e -> paint(e, e.y / ROW));
			return control = scrolled;
		}
		canvas = new Canvas(parent, SWT.BORDER | SWT.V_SCROLL | SWT.NO_BACKGROUND | SWT.DOUBLE_BUFFERED);
		canvas.addPaintListener(e -> paint(e, canvas.getVerticalBar().getSelection() + e.y / ROW));
		canvas.getVerticalBar().addListener(SWT.Selection, e -> canvas.redraw());
		return control = canvas;
	}

	private void paint(PaintEvent e, int first) {
		GC gc = e.gc;
		gc.fillRectangle(e.x, e.y, e.width, e.height);
		int top = (e.y / ROW) * ROW, last = Math.min(rows, first + (e.height + ROW - 1) / ROW + 1);
		for (int r = first, y = top; r < last; r++, y += ROW) {
			gc.drawString(text.cell(r, 0), 4, y, true);
			counters.inc(Counters.MATERIALIZE);
		}
	}

	@Override
	public void setData(int n) {
		super.setData(n);
		if (scrolled != null) {
			canvas.setSize(400, (int) Math.min(Integer.MAX_VALUE, (long) n * ROW));
			return;
		}
		ScrollBar bar = canvas.getVerticalBar();
		int page = visibleRows();
		bar.setValues(0, 0, n, Math.min(n, page), 1, page);
		canvas.redraw();
	}

	@Override
	public void scrollTo(int row) {
		if (scrolled != null) {
			scrolled.setOrigin(0, row * ROW);
			return;
		}
		canvas.getVerticalBar().setSelection(row);
		canvas.redraw();
	}

	@Override
	public int visibleRows() {
		Control viewport = scrolled != null ? scrolled : canvas;
		return Math.max(1, (viewport.getSize().y + ROW - 1) / ROW);
	}

	@Override
	public String notes() {
		return scrolled != null ? "content_height_px=" + canvas.getSize().y : "row_px=" + ROW;
	}
}
