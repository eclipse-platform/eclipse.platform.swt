/*******************************************************************************
 * Copyright (c) 2025 Yatta and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Yatta - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.graphics;

import java.util.function.*;

import org.eclipse.swt.*;
import org.eclipse.swt.internal.image.*;

/**
 * Interface to provide a callback mechanism to draw on different GC instances
 * depending on the zoom the image will be used for. A common use case is when
 * the application is moved from a low DPI monitor to a high DPI monitor. This
 * provides API which will be called by SWT during the image rendering.
 *
 * This interface needs to be implemented by client code to provide logic that
 * draws on the empty GC on demand.
 *
 * @since 3.130
 */
@FunctionalInterface
public interface ImageDrawer {

	/**
	 * Draws an image on a GC for a requested zoom level.
	 *
	 * @param gc          The GC will draw on the underlying Image and is configured
	 *                    for the targeted zoom
	 * @param imageWidth  The width of the image in points to draw on
	 * @param imageHeight The height of the image in points to draw on
	 */
	void drawOn(GC gc, int imageWidth, int imageHeight);

	/**
	 * @since 3.130
	 */
	public static ImageDrawer withGCStyle(int style, ImageDrawer drawer) {
		if (drawer instanceof  ExtendedImageDrawer extendedDrawer) {
			return new ExtendedImageDrawer(extendedDrawer.original(), extendedDrawer.postProcessor(), style);
		}
		return new ExtendedImageDrawer(drawer, null, style);
	}

	/**
	 * @since 3.130
	 */
	public static ImageDrawer create(ImageDrawer drawer, Consumer<ImageData> postProcessor) {
		if (drawer instanceof ExtendedImageDrawer extendedDrawer) {
			return new ExtendedImageDrawer(extendedDrawer.original(), postProcessor, extendedDrawer.style());
		}
		return new ExtendedImageDrawer(drawer, postProcessor, SWT.NONE);
	}

	// TODO: or use some kind of builder pattern?
}
