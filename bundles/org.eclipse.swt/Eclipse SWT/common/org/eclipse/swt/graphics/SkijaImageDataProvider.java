/*******************************************************************************
 * Copyright (c) 2024 SAP SE and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package org.eclipse.swt.graphics;

import java.io.*;
import java.util.*;

import org.eclipse.swt.*;

import io.github.humbleui.skija.*;
import io.github.humbleui.skija.Image;
import io.github.humbleui.types.*;

public class SkijaImageDataProvider implements ImageDataProvider {

    private Map<Integer, ImageData> convertedImageData = new HashMap<>();
    private ImageData imageData;
	SamplingMode mode = SamplingMode.CATMULL_ROM;
	private Image image;

    /**
     * For scaling imageData up, if necessary.
     * Use this only as a backup, if the image does not support scaling properly.
     * @param imageData
     */
    public SkijaImageDataProvider(ImageData imageData) {
	this.imageData = imageData;
    }

    SkijaImageDataProvider(io.github.humbleui.skija.Image image) {
	this.image = image;
    }

    @Override
    public ImageData getImageData(int zoom) {
	if (convertedImageData.get(zoom) != null)
	    return convertedImageData.get(zoom);

	var id = scaleImage(zoom);
	convertedImageData.put(zoom, id);

	return id;
    }

    ImageData scaleImage(int zoom) {
	Surface surface = null;
	Image image = null;
	try {
	    double scale = (double) (zoom) / 100.0;

	    var originalWidth = imageData.width;
	    var originalHeight = imageData.height;

	    int newWidth = (int) Math.round(originalWidth * scale);
	    int newHeight = (int) Math.round(originalHeight * scale);

		surface = Surface.makeRaster(
				new ImageInfo(new ColorInfo(ColorType.RGBA_8888, ColorAlphaType.UNPREMUL, null), newWidth, newHeight));

	    var sourceRect = Rect.makeWH(imageData.width, imageData.height);
	    var targetRect = Rect.makeWH(newWidth, newHeight);

	    if (imageData != null)
		image = SkijaGC.convertSWTImageToSkijaImage(imageData);
	    else
		image = this.image;
	    surface.getCanvas().drawImageRect(image, sourceRect, targetRect, mode, null, false);
	    ImageData conv = SkijaGC.convertToSkijaImageData(surface.makeImageSnapshot());

	    return conv;
	} finally {
	    if (surface != null)
		surface.close();
	    if (image != null)
		image.close();
	}
    }
}
