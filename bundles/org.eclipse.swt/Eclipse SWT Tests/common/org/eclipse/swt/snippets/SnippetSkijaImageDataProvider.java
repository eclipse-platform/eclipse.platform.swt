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
package org.eclipse.swt.snippets;

import java.io.*;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.*;

import io.github.humbleui.skija.*;

public class SnippetSkijaImageDataProvider {
// if you wish to check the images, set this to false
    private static boolean DELETE_AFTER_EXECUTE = true;

    static int zoom = 800;

    private static final String BEFORE_FILE = "before.png";
    private static final String AFTER_FILE_SWT = "after_swt.png";
    private static final String AFTER_FILE_SKIA = "after_skia.png";
    private static final String CHANGED_FILE = "changed.png";
    private static final String SKIJA_IMAGE_BEFORE = "before_skijaImage.png";
    private static final String SKIJA_IMAGE_AFTER = "after_skijaImage.png";

    private static String[] filenames = new String[] { BEFORE_FILE, AFTER_FILE_SWT, AFTER_FILE_SKIA, CHANGED_FILE,
	    SKIJA_IMAGE_BEFORE, SKIJA_IMAGE_AFTER };

    public static void main(String[] arg) {
	deleteFiles();

	Display display = new Display();

	org.eclipse.swt.graphics.Image image = display.getSystemImage(SWT.ICON_QUESTION);

	ImageLoader saver = new ImageLoader();

	{
	    // just save the png image
	    saver.data = new ImageData[] { image.getImageData() };
	    saver.save(BEFORE_FILE, SWT.IMAGE_PNG);
	}

	{
	    // swt standard zoom
	    saver.data = new ImageData[] { image.getImageData(zoom) };
	    saver.save(AFTER_FILE_SWT, SWT.IMAGE_PNG);
	}
	{

	    // change an image with the ImageUtils.setImageDataProvider
	    SkijaImageDataProvider skiaScale = new SkijaImageDataProvider(image.getImageData());
	    saver.data = new ImageData[] { skiaScale.getImageData(zoom) };
	    saver.save(AFTER_FILE_SKIA, SWT.IMAGE_PNG);
	    image = display.getSystemImage(SWT.ICON_WARNING);
	    ImageUtils.setImageDataProvider(image, skiaScale);
	    saver.data = new ImageData[] { image.getImageData() };
	    saver.save(CHANGED_FILE, SWT.IMAGE_PNG);
	}

	{
	    // create a skijaImage
	    byte[] bytes = SkijaGC.convertToRGBA(image.getImageData());

	    var colType = ColorType.RGBA_8888;
	    ImageInfo imageInfo = new ImageInfo(image.getImageData().width, image.getImageData().height, colType,
		    ColorAlphaType.UNPREMUL);

	    io.github.humbleui.skija.Image skijaImage = io.github.humbleui.skija.Image.makeRasterFromBytes(imageInfo,
		    bytes, image.getImageData().width * 4);

	    // write to file before skijaImageDataProvider

	    SkijaGC.writeFile(SKIJA_IMAGE_BEFORE, skijaImage);

	    // use SkijaImageDataProvider and create an image

	    Image imgFromSkija = ImageUtils.createGenericImage(new SkijaImageDataProvider(skijaImage));

	    // write to file.
	    saver.data = new ImageData[] { imgFromSkija.getImageData(zoom) };
	    saver.save(SKIJA_IMAGE_AFTER, SWT.IMAGE_PNG);

	    imgFromSkija.dispose();
	    skijaImage.close();
	    image.dispose();

	}

	if (DELETE_AFTER_EXECUTE) {
	    deleteFiles();
	}
    }

    private static void deleteFiles() {
	for (var s : filenames) {
	    File f = new File(s);
	    if (f.exists()) {
		f.delete();
	    }
	}
    }
}
