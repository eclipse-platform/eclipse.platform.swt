/*******************************************************************************
 * Copyright (c) 2025 Hannes Wellmann and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Hannes Wellmann - initial API and implementation
 *******************************************************************************/

package org.eclipse.swt.internal.image;

import java.util.*;
import java.util.function.*;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;

public record ExtendedImageDrawer(ImageDrawer original, Consumer<ImageData> postProcessor, int style)
		implements ImageDrawer {

	public ExtendedImageDrawer {
		Objects.requireNonNull(original);
	}

	@Override
	public void drawOn(GC gc, int imageWidth, int imageHeight) {
		original.drawOn(gc, imageWidth, imageHeight);
	}

	public static int getGCStyle(ImageDrawer drawer) {
		if (drawer instanceof ExtendedImageDrawer extendedDrawer) {
			return extendedDrawer.style();
		}
		return SWT.NONE;
	}

	public static void postProcess(ImageDrawer drawer, ImageData data) {
		if (drawer instanceof @SuppressWarnings("removal") ImageGcDrawer gcDrawer) {
			gcDrawer.postProcess(data);
		} else if (drawer instanceof ExtendedImageDrawer extendedDrawer) {
			Consumer<ImageData> postProcessor2 = extendedDrawer.postProcessor();
			if (postProcessor2 != null) {
				postProcessor2.accept(data);
			}
		}
	}

}
