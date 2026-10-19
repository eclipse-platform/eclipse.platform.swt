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

import java.util.Locale;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

/**
 * Controller of one widget tab. The widget itself is the tab's control (the
 * viewport and the widget are one: no wrapper composite, no ScrolledComposite);
 * placeholders and launchers are a single Label or Button. Metrics are shown
 * by the showcase's shared strip.
 */
final class DemoPane {

	private final DemoSpec spec;
	private final ConceptInfo concept;
	private final Counters counters = new Counters();
	private final Metrics metrics = new Metrics(MatrixRunner.FRAMES);
	private SupersetDemo demo;
	private Control control;
	private double firstPaintMs, p50, p95;

	DemoPane(DemoSpec spec, ConceptInfo concept) {
		this.spec = spec;
		this.concept = concept;
	}

	/** Creates the tab's control directly under {@code parent}. */
	Control create(Composite parent, int scale) {
		demo = spec.runnable() ? Demos.create(spec, counters) : null;
		if (demo != null) {
			try {
				control = demo.create(parent);
				load(scale);
				return control;
			} catch (RuntimeException | LinkageError e) {
				demo = null;
				if (control != null && !control.isDisposed()) control.dispose();
				return control = label(parent, "Cannot create " + spec.widgetClass() + ": " + e
						+ "\n(Nebula demos need the Nebula jars on the classpath)");
			}
		}
		if (Launchers.supports(spec)) return control = launcher(parent);
		return control = label(parent, Demos.staticResult(spec) + " — " + Demos.staticReason(spec) + "\n" + concept.ledgerLine());
	}

	private Control launcher(Composite parent) {
		Button open = new Button(parent, SWT.PUSH | SWT.WRAP);
		open.setText("Open " + spec.simpleName() + " (" + Demos.staticReason(spec) + ")");
		open.addListener(SWT.Selection, e -> {
			try {
				Launchers.open(spec, open.getShell());
			} catch (RuntimeException | LinkageError err) {
				open.setText("Open " + spec.simpleName() + " failed: " + err);
			}
		});
		return open;
	}

	private static Label label(Composite parent, String text) {
		Label l = new Label(parent, SWT.WRAP | SWT.CENTER);
		l.setText(text);
		return l;
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
	}

	void sweep() {
		if (demo == null) return;
		int last = Math.max(0, demo.rows() - 1), half = MatrixRunner.FRAMES / 2;
		metrics.reset();
		for (int f = 1; f <= MatrixRunner.FRAMES; f++) {
			long t = System.nanoTime();
			demo.scrollTo(f <= half ? (int) ((long) last * f / half) : (int) ((long) last * (MatrixRunner.FRAMES - f) / half));
			demo.control().update();
			while (control.getDisplay().readAndDispatch()) {
				// drain paints of this frame
			}
			metrics.frame(System.nanoTime() - t);
		}
		double[] p = metrics.percentiles();
		p50 = p[0];
		p95 = p[1];
	}

	DemoSpec spec() {
		return spec;
	}

	boolean visible() {
		return control != null && !control.isDisposed() && control.isVisible();
	}

	String info() {
		return spec.owner() + " | " + spec.widgetClass() + " | kind " + spec.kind() + " | hosts " + spec.hosts()
				+ (spec.largeContent() ? " | design max " + DemoSpec.scaleLabel(spec.maxScale()) : " | small content");
	}

	String strip() {
		Runtime rt = Runtime.getRuntime();
		return String.format(Locale.ROOT,
				"%s | rows %s | first paint %.1f ms | frame p50 %.1f p95 %.1f ms | materialized %d / bound %d | heap %d MB | private %.0f MB | paints %d | layouts %d",
				spec.simpleName(), DemoSpec.scaleLabel(demo == null ? 0 : demo.rows()), firstPaintMs, p50, p95,
				demo == null ? 0 : demo.materialized(), demo == null ? 0 : demo.visibleRows() + 2,
				(rt.totalMemory() - rt.freeMemory()) >> 20, Metrics.privateMb(), counters.get(Counters.PAINT), counters.get(Counters.LAYOUT));
	}
}
