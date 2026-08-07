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
package org.eclipse.swt.internal.graphics;

import org.eclipse.swt.graphics.Color;

public class SkiaColorConverter {

	public static int convertSWTColorToSkijaColor(Color swtColor) {
		// extract RGB-components
		final int red = swtColor.getRed();
		final int green = swtColor.getGreen();
		final int blue = swtColor.getBlue();
		final int alpha = swtColor.getAlpha();

		// create ARGB 32-Bit-color
		final int skijaColor = (alpha << 24) | (red << 16) | (green << 8) | blue;

		return skijaColor;
	}

	public static int convertSWTColorToSkijaColor(Color swtColor, int alphaOverride) {
		// extract RGB-components
		final int red = swtColor.getRed();
		final int green = swtColor.getGreen();
		final int blue = swtColor.getBlue();
		final int alpha = alphaOverride;

		// create ARGB 32-Bit-color
		final int skijaColor = (alpha << 24) | (red << 16) | (green << 8) | blue;

		return skijaColor;
	}

	public static int invertSWTColorToInt(Color swtColor) {

		// extract RGB-components
		final int red = swtColor.getRed();
		final int green = swtColor.getGreen();
		final int blue = swtColor.getBlue();
		final int alpha = swtColor.getAlpha();

		// create ARGB 32-Bit-color
		final int skijaColor = (alpha << 24) | ((0xFF - red) << 16) | ((0xFF - green) << 8) | (0xFF - blue);

		return skijaColor;

	}

}
