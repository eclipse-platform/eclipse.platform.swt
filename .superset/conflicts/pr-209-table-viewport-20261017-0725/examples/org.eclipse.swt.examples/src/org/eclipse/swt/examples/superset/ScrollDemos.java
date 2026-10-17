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
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Slider;

/**
 * Scroll-family demos. Kind SLIDER (after SWT Snippet17/Snippet20): a Slider
 * whose maximum is the scale, scrolled by setSelection; notes carry thumb,
 * maximum and selection read-back (range scaling). Kind SCROLLED_CHILDREN
 * (after SWT Snippet5/Snippet166): a ScrolledComposite over a content
 * composite with one Label per row, the classic non-virtual pattern; children
 * painted while outside the viewport and children laid out are counted.
 */
final class ScrollDemos extends SupersetDemo {

	private static final int ROW = 20;
	private Slider slider;
	private ScrolledComposite scrolled;
	private Composite content;
	private int outsidePaints, readbackErrors, layouts;

	ScrollDemos(DemoSpec spec, Counters counters) {
		super(spec, counters);
	}

	@Override
	public Control create(Composite parent) {
		if ("SLIDER".equals(spec.kind())) return control = slider = new Slider(parent, SWT.HORIZONTAL);
		scrolled = new ScrolledComposite(parent, SWT.V_SCROLL | SWT.H_SCROLL);
		content = new Composite(scrolled, SWT.NO_FOCUS);
		content.setLayout(new GridLayout(1, false));
		scrolled.setContent(content);
		return control = scrolled;
	}

	@Override
	public void setData(int n) {
		super.setData(n);
		if (slider != null) {
			slider.setValues(0, 0, n, Math.max(1, n / 100), 1, Math.max(1, n / 10));
			return;
		}
		for (int i = content.getChildren().length; i < n; i++) {
			Label l = new Label(content, SWT.NO_FOCUS);
			l.setText(text.cell(i, 0));
			l.addListener(SWT.Paint, e -> {
				if (!visible((Control) e.widget)) outsidePaints++;
			});
			counters.inc(Counters.MATERIALIZE);
		}
		Point size = content.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		content.setSize(size);
		layouts++;
	}

	private boolean visible(Control child) {
		Rectangle b = child.getBounds();
		Point origin = content.getLocation();
		Rectangle area = scrolled.getClientArea();
		return b.intersects(-origin.x, -origin.y, area.width, area.height);
	}

	@Override
	public void scrollTo(int row) {
		if (slider != null) {
			slider.setSelection(row);
			if (slider.getSelection() != Math.min(row, slider.getMaximum() - slider.getThumb())) readbackErrors++;
			return;
		}
		Control[] kids = content.getChildren();
		if (kids.length > 0) scrolled.setOrigin(0, kids[Math.min(row, kids.length - 1)].getLocation().y);
	}

	@Override
	public int materialized() {
		return slider != null ? 1 : super.materialized();
	}

	@Override
	public int visibleRows() {
		return slider != null ? 1 : Math.max(1, (scrolled.getClientArea().height + ROW - 1) / ROW);
	}

	@Override
	public boolean select(int i) {
		if (slider == null) return false;
		slider.setSelection(i);
		return true;
	}

	@Override
	public String notes() {
		if (slider != null) {
			return "thumb=" + slider.getThumb() + " maximum=" + slider.getMaximum() + " selection_readback_errors=" + readbackErrors
					+ " slider_px=" + slider.getSize().x;
		}
		return "children=" + content.getChildren().length + " children_painted_outside_viewport=" + outsidePaints
				+ " content_height_px=" + content.getSize().y + " content_size_layouts=" + layouts;
	}
}
