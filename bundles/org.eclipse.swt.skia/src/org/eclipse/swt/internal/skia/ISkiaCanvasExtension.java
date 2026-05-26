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

import io.github.humbleui.skija.Surface;

public interface ISkiaCanvasExtension {

	Surface getSurface();

	SkiaResources getResources();

	Surface createSupportSurface(int width, int height);

	DpiScalerUtil getScaler();

}
