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
