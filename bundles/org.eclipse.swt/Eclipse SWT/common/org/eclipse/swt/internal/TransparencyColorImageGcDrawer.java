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
package org.eclipse.swt.internal;

import org.eclipse.swt.graphics.*;

public abstract class TransparencyColorImageGcDrawer implements ImageGcDrawer {

	private final Color transparencyColor;

	public TransparencyColorImageGcDrawer(Color transparencyColor) {
		this.transparencyColor = transparencyColor;
	}

	@Override
	public void postProcess(ImageData imageData) {
		imageData.transparentPixel = imageData.palette.getPixel(transparencyColor.getRGB());
	}

}
