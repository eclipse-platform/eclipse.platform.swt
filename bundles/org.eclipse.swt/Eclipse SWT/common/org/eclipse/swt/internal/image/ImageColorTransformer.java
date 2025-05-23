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

	public static final String IMAGE_DISABLEMENT_ALGORITHM_GRAYED = "grayed";
	public static final String IMAGE_DISABLEMENT_ALGORITHM_GTK = "gtk";
	public static final String IMAGE_DISABLEMENT_ALGORITHM_DESATURATED = "desaturated";
	public static final String IMAGE_DISABLEMENT_ALGORITHM = System.getProperty("org.eclipse.swt.image.disablement",
			IMAGE_DISABLEMENT_ALGORITHM_GRAYED).strip();

	public static final ImageColorTransformer DEFAULT_DISABLED_IMAGE_TRANSFORMER = switch (IMAGE_DISABLEMENT_ALGORITHM) {
	case IMAGE_DISABLEMENT_ALGORITHM_GTK -> ImageColorTransformer.forRGB(0.5f, 0.5f, 0.5f, 0.5f);
	case IMAGE_DISABLEMENT_ALGORITHM_DESATURATED -> ImageColorTransformer.forSaturationBrightness(0.2f, 0.9f, 0.5f);
	default -> ImageColorTransformer.forGrayscaledContrastBrightness(0.2f, 2.9f);
	};

	RGBA adaptPixelValue(int red, int green, int blue, int alpha);

	public static ImageColorTransformer forSaturationBrightness(float saturationFactor, float brightnessFactor,
			float alphaFactor) {
		return (red, green, blue, alpha) -> {
			float[] hsba = new RGBA(red, green, blue, alpha).getHSBA();
			float hue = hsba[0];
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

	public static ImageColorTransformer forGrayscaledContrastBrightness(float contrast, float brightness) {
		return (red, green, blue, alpha) -> {
			int grayValue = Math.min((77 * red + 151 * green + 28 * blue) / 255, 255);
			int resultValue = (int) Math.min(Math.max((contrast * (grayValue * brightness - 128) + 128), 0), 255);
			return new RGBA(resultValue, resultValue, resultValue, alpha);
		};
	}

}
