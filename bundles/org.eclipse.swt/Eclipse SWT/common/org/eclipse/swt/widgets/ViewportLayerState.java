/*******************************************************************************
 * Copyright (c) 2026 Contributors to the Eclipse Foundation.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package org.eclipse.swt.widgets;

import org.eclipse.swt.internal.*;

/**
 * Internal z-plane dirtiness policy for viewport-driven widgets.
 *
 * <p>The canonical model lives in {@link ViewportRuntime}; this package-local
 * adapter preserves existing widget call sites while custom widgets reuse the
 * same runtime directly.</p>
 */
final class ViewportLayerState {
	static final int BODY = ViewportRuntime.BODY;
	static final int FROZEN = ViewportRuntime.FROZEN;
	static final int HEADER = ViewportRuntime.HEADER;
	static final int EDITOR = ViewportRuntime.EDITOR;
	static final int SCROLLBAR = ViewportRuntime.SCROLLBAR;
	static final int FEEDBACK = ViewportRuntime.FEEDBACK;
	static final int ALL = ViewportRuntime.ALL;

	private final ViewportRuntime runtime = new ViewportRuntime ();

	void initialize (double x, double y) {
		runtime.initializeOrigin (x, y);
	}

	int scrollTo (double x, double y) {
		return runtime.scrollTo (x, y);
	}

	double originX () {
		return runtime.originX ();
	}

	double originY () {
		return runtime.originY ();
	}
}
