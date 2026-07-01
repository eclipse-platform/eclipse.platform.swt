/*******************************************************************************
 * Copyright (c) 2025 SAP SE and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * Contributors:
 *     SAP SE and others - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.canvasext;

import org.eclipse.swt.widgets.*;

/**
 *
 * Provides utility methods for the canvas extension to scale values according
 * to the current DPI settings of the OS. This is used internally to scale all
 * values that are used for drawing and layout calculations.
 */
public class DpiScaler implements IDpiScaler {

	private Canvas canvas;

	public DpiScaler(Canvas canvas) {
		this.canvas = canvas;
	}

	@Override
	public int getNativeZoom() {
		return canvas.nativeZoom;
	}

}