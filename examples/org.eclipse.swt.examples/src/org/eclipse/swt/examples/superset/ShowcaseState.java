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

import org.eclipse.swt.widgets.Label;

/**
 * Showcase-wide state: the selected scale, the widget tab in view and the
 * shared metrics strip, which refreshes every 500 ms only while the widget
 * in view is visible and stops otherwise (restarted by the next selection).
 */
final class ShowcaseState {

	private static final int STRIP_MS = 500;
	int scale = DemoSpec.SCALES[0];
	Label strip;
	Label info;
	private DemoPane visible;
	private boolean ticking;

	void show(DemoPane pane) {
		visible = pane;
		if (info != null && !info.isDisposed()) info.setText(pane.info());
		if (!ticking) tick();
	}

	void applyScale() {
		if (visible != null && visible.visible()) visible.load(scale);
	}

	void sweep() {
		if (visible != null && visible.visible()) visible.sweep();
	}

	private void tick() {
		ticking = strip != null && !strip.isDisposed() && visible != null && visible.visible();
		if (!ticking) return;
		strip.setText(visible.strip());
		strip.getDisplay().timerExec(STRIP_MS, this::tick);
	}
}
