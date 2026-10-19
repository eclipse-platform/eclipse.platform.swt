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
 * setOrigin, or, when the SWT under test has the fork's logical-content mode
 * (ScrolledComposite#setLogicalContentSize, #106/#108), a viewport-sized
 * canvas painted at the logical origin. All paint only the rows inside the
 * damage rectangle; rows drawn by the first paint are the materialized count.
 */
final class CanvasDemo extends SupersetDemo {

	private static final int ROW = 18;
	private Canvas canvas;
	private ScrolledComposite scrolled;
	private boolean logical;

	CanvasDemo(DemoSpec spec, Counters counters) {
		super(spec, counters);
	}

	@Override
	public Control create(Composite parent) {
		if ("SCROLLED".equals(spec.kind())) {
			scrolled = new ScrolledComposite(parent, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
			canvas = new Canvas(scrolled, SWT.NO_BACKGROUND | SWT.NO_FOCUS);
			scrolled.setContent(canvas);
			logical = Reflect.has(scrolled, "setLogicalContentSize", 0, 0);
			if (logical) {
				scrolled.setExpandHorizontal(true);
				scrolled.setExpandVertical(true);
			}
			canvas.addPaintListener(e -> paint(e, logical ? scrolled.getOrigin().y : 0));
			return control = scrolled;
		}
		canvas = new Canvas(parent, SWT.BORDER | SWT.V_SCROLL | SWT.NO_BACKGROUND | SWT.DOUBLE_BUFFERED);
		canvas.addPaintListener(e -> paint(e, (long) canvas.getVerticalBar().getSelection() * ROW));
		canvas.getVerticalBar().addListener(SWT.Selection, e -> canvas.redraw());
		return control = canvas;
	}

	/** Paints the rows inside the damage rectangle; {@code offset} is the logical y of the canvas top. */
	private void paint(PaintEvent e, long offset) {
		GC gc = e.gc;
		gc.fillRectangle(e.x, e.y, e.width, e.height);
		long first = (offset + e.y) / ROW, last = Math.min(rows, (offset + e.y + e.height + ROW - 1) / ROW);
		for (long r = first; r < last; r++) {
			gc.drawString(text.cell((int) r, 0), 4, (int) (r * ROW - offset), true);
			counters.inc(Counters.MATERIALIZE);
		}
	}

	@Override
	public void setData(int n) {
		super.setData(n);
		if (scrolled != null && logical) {
			Reflect.call(scrolled, "setLogicalContentSize", 400, (int) Math.min(Integer.MAX_VALUE, (long) n * ROW));
			canvas.redraw();
			return;
		}
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
	public boolean paintCountsAsMaterialization() {
		return false;
	}

	@Override
	public int visibleRows() {
		Control viewport = scrolled != null ? scrolled : canvas;
		return Math.max(1, (viewport.getSize().y + ROW - 1) / ROW);
	}

	@Override
	public String notes() {
		if (scrolled == null) return "row_px=" + ROW;
		return logical ? "logical_content_mode=yes (ScrolledComposite#setLogicalContentSize) canvas_px=" + canvas.getSize()
				: "logical_content_mode=absent; content_height_px=" + canvas.getSize().y;
	}
}
