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

/**
 * Showcase-wide state: the selected scale and the demo pane currently in
 * view, which receives "load scale" and "sweep" from the toolbar.
 */
final class ShowcaseState {

	int scale = DemoSpec.SCALES[0];
	DemoPane visible;

	void applyScale() {
		if (visible != null && !visible.isDisposed() && visible.isVisible()) visible.load(scale);
	}

	void sweep() {
		if (visible != null && !visible.isDisposed() && visible.isVisible()) visible.sweep();
	}
}
