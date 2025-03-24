/*******************************************************************************
 * Copyright (c) 2025 Vector Informatik GmbH and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package org.eclipse.swt.internal.image;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;

public interface ImageColorTransformer {

	RGBA adaptPixelValue(int red, int green, int blue, int alpha);

	public static ImageColorTransformer forHSB(float hueFactor, float saturationFactor, float brightnessFactor,
			float alphaFactor) {
		return (red, green, blue, alpha) -> {
			float[] hsba = new RGBA(red, green, blue, alpha).getHSBA();
			float hue = Math.min(hueFactor * hsba[0], 1.0f);
			float saturation = Math.min(saturationFactor * hsba[1], 1.0f);
			float brightness = Math.min(brightnessFactor * hsba[2], 1.0f);
			float alphaResult = Math.min(alphaFactor * hsba[3], 255.0f);
			return new RGBA(hue, saturation, brightness, alphaResult);
		};
	}

	public static ImageColorTransformer forRGB(float redFactor, float greenFactor, float blueFactor,
			float alphaFactor) {
		return (red, green, blue, alpha) -> {
			int redResult = (int) Math.min(redFactor * red, 255.0f);
			int greenResult = (int) Math.min(greenFactor * green, 255.0f);
			int blueResult = (int) Math.min(blueFactor * blue, 255.0f);
			int alphaResult = (int) Math.min(alphaFactor * alpha, 255.0f);
			return new RGBA(redResult, greenResult, blueResult, alphaResult);
		};
	}

	public static ImageColorTransformer forIntensityThreshold(Device device) {
		RGBA lowIntensity = device.getSystemColor(SWT.COLOR_WIDGET_NORMAL_SHADOW).getRGBA();
		RGBA highIntensity = device.getSystemColor(SWT.COLOR_WIDGET_BACKGROUND).getRGBA();
		return (red, green, blue, alpha) -> {
			int intensity = red * red + green * green + blue * blue;
			RGBA usedGraytone = intensity < 98304 ? lowIntensity : highIntensity;
			return new RGBA(usedGraytone.rgb.red, usedGraytone.rgb.green, usedGraytone.rgb.blue, alpha);
		};
	}

}
