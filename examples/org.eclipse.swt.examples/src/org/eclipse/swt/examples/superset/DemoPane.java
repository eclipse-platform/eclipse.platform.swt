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

import java.util.Locale;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

/**
 * One widget tab: description line, the widget (or a placeholder / launcher
 * for widgets the runner cannot drive) and the live metrics strip. The strip
 * refreshes every 500 ms only while the pane is visible; a paint of the strip
 * (the pane became visible again) restarts it.
 */
final class DemoPane extends Composite {

	private static final int STRIP_MS = 500;
	private final DemoSpec spec;
	private final Counters counters = new Counters();
	private final Metrics metrics = new Metrics(MatrixRunner.FRAMES);
	private final Label strip;
	private SupersetDemo demo;
	private double firstPaintMs, p50, p95;
	private boolean ticking;

	DemoPane(Composite parent, DemoSpec spec, ConceptInfo concept, int scale) {
		super(parent, SWT.NONE);
		this.spec = spec;
		setLayout(new GridLayout(1, false));
		Label info = new Label(this, SWT.WRAP);
		info.setText(spec.owner() + " | " + spec.widgetClass() + " | kind " + spec.kind() + " | hosts " + spec.hosts()
				+ (spec.largeContent() ? " | large content, design max " + DemoSpec.scaleLabel(spec.maxScale()) : " | small content"));
		info.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
		Composite host = new Composite(this, SWT.NONE);
		host.setLayout(new CountingLayout(null, counters));
		host.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		strip = new Label(this, SWT.NONE);
		strip.setLayoutData(new GridData(SWT.FILL, SWT.BOTTOM, true, false));
		strip.addListener(SWT.Paint, e -> resume());
		build(host, concept);
		if (demo != null) load(scale);
	}

	private void build(Composite host, ConceptInfo concept) {
		demo = spec.runnable() ? Demos.create(spec, counters) : null;
		if (demo == null) {
			launcherOrPlaceholder(host, concept);
			return;
		}
		try {
			demo.create(host);
		} catch (RuntimeException | LinkageError e) {
			demo = null;
			for (var c : host.getChildren()) c.dispose();
			placeholder(host, "Cannot create " + spec.widgetClass() + ": " + e + "\n(Nebula demos need the Nebula jars on the classpath)");
		}
	}

	private void launcherOrPlaceholder(Composite host, ConceptInfo concept) {
		if (Launchers.supports(spec)) {
			Button open = new Button(host, SWT.PUSH);
			open.setText("Open " + spec.simpleName() + " (" + Demos.staticReason(spec) + ")");
			open.addListener(SWT.Selection, e -> {
				try {
					Launchers.open(spec, getShell());
				} catch (RuntimeException | LinkageError err) {
					open.setText("Open " + spec.simpleName() + " failed: " + err);
					layout(true, true);
				}
			});
			return;
		}
		placeholder(host, Demos.staticResult(spec) + " — " + Demos.staticReason(spec) + "\n" + concept.ledgerLine());
	}

	private static void placeholder(Composite host, String text) {
		Label l = new Label(host, SWT.WRAP | SWT.CENTER);
		l.setText(text);
	}

	void load(int scale) {
		if (demo == null) return;
		int n = spec.largeContent() ? Math.min(scale, spec.maxScale()) : 0;
		counters.reset();
		long t0 = System.nanoTime();
		demo.setData(n);
		demo.control().update();
		firstPaintMs = Metrics.ms(System.nanoTime() - t0);
		counters.hookPaint(demo.control());
		getParent().layout(true, true);
	}

	void sweep() {
		if (demo == null) return;
		int last = Math.max(0, demo.rows() - 1), half = MatrixRunner.FRAMES / 2;
		metrics.reset();
		for (int f = 1; f <= MatrixRunner.FRAMES; f++) {
			long t = System.nanoTime();
			demo.scrollTo(f <= half ? (int) ((long) last * f / half) : (int) ((long) last * (MatrixRunner.FRAMES - f) / half));
			demo.control().update();
			while (getDisplay().readAndDispatch()) {
				// drain paints of this frame
			}
			metrics.frame(System.nanoTime() - t);
		}
		double[] p = metrics.percentiles();
		p50 = p[0];
		p95 = p[1];
	}

	/** Restarts the metrics strip after the pane is shown again. */
	void resume() {
		if (!ticking) tick();
	}

	private void tick() {
		ticking = !isDisposed() && isVisible();
		if (!ticking) return;
		{
			Runtime rt = Runtime.getRuntime();
			strip.setText(String.format(Locale.ROOT,
					"rows %s | first paint %.1f ms | frame p50 %.1f p95 %.1f ms | materialized %d / bound %d | heap %d MB | private %.0f MB | paints %d | layouts %d",
					DemoSpec.scaleLabel(demo == null ? 0 : demo.rows()), firstPaintMs, p50, p95, demo == null ? 0 : demo.materialized(),
					demo == null ? 0 : demo.visibleRows() + 2, (rt.totalMemory() - rt.freeMemory()) >> 20, Metrics.privateMb(),
					counters.get(Counters.PAINT), counters.get(Counters.LAYOUT)));
		}
		getDisplay().timerExec(STRIP_MS, this::tick);
	}
}
