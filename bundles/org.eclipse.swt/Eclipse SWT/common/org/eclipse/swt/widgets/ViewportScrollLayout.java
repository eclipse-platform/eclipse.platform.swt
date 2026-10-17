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
 * Package-local compatibility adapter for the canonical viewport scrollbar
 * fixed-point solver in {@link ViewportRuntime}.
 */
final class ViewportScrollLayout {
	static final int AUTO = ViewportRuntime.AUTO;
	static final int ALWAYS = ViewportRuntime.ALWAYS;
	static final int NEVER = ViewportRuntime.NEVER;

	record Result (
			boolean horizontalVisible,
			boolean verticalVisible,
			boolean cornerVisible,
			int bodyWidth,
			int bodyHeight,
			int headerWidth,
			int headerHeight,
			int visibleRows,
			long logicalRows,
			long estimatedContentHeight,
			long logicalContentWidth) {
	}

	private ViewportScrollLayout () {
	}

	static Result solve (
			int outerWidth,
			int outerHeight,
			int headerHeight,
			long logicalRows,
			int sampleRowHeight,
			long logicalContentWidth,
			int horizontalBarHeight,
			int verticalBarWidth,
			int horizontalPolicy,
			int verticalPolicy) {
		ViewportRuntime.RowLayout layout = ViewportRuntime.solveRows (
				outerWidth,
				outerHeight,
				headerHeight,
				logicalRows,
				sampleRowHeight,
				logicalContentWidth,
				horizontalBarHeight,
				verticalBarWidth,
				horizontalPolicy,
				verticalPolicy);
		return new Result (
				layout.horizontalVisible (),
				layout.verticalVisible (),
				layout.cornerVisible (),
				layout.bodyWidth (),
				layout.bodyHeight (),
				layout.headerWidth (),
				layout.headerHeight (),
				layout.visibleRows (),
				layout.logicalRows (),
				layout.estimatedContentHeight (),
				layout.logicalContentWidth ());
	}
}
