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
package org.eclipse.swt.internal.canvasext;

import org.eclipse.swt.internal.skia.SkiaGlCanvasExtension;
import org.eclipse.swt.widgets.Canvas;

/**
 * Provides a factory for creating Skia-based canvas extensions. This factory is
 * used by org.eclipse.swt.internal.canvasext.ExternalCanvasHandler with the
 * resource loader to create canvas extensions when the SKIA style is specified.
 */
public class SkiaCanvasFactory implements IExternalCanvasFactory {

	private static boolean skiaFailedWithErrors = false;

	@Override
	public IExternalCanvasHandler createCanvasExtension(Canvas c) {

		if (skiaFailedWithErrors) {
			return null;
		}

		try {
			return new SkiaGlCanvasExtension(c);
		} catch (final Throwable t) {
			// Skia initialization failed; log and disable Skia for all future calls
			Logger.logException(t);
			skiaFailedWithErrors = true;
			return null;
		}
	}
}
