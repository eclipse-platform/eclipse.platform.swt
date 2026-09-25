/*******************************************************************************
 * Copyright (c) 2025 SAP SE and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package org.eclipse.swt.internal.skia;

import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Drawable;
import org.eclipse.swt.graphics.Rectangle;

public interface ISkiaCanvasExtension {

	ISkSurface getSurface();

	ISkiaResources getResources();

	ISkSurface createSupportSurface(int width, int height);

	DpiScalerUtil getScaler();

	Drawable getDrawable();

	Rectangle getClientArea();

	Device getDevice();

	Rectangle getBounds();

	void redraw(int srcX, int srcY, int width, int height, boolean b);

}
