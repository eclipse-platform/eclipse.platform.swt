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
import org.eclipse.swt.widgets.*;

public class SnippetSkijaImageDataProvider {
// if you wish to check the images, set this to false
    private static boolean DELETE_AFTER_EXECUTE = true;

    static int zoom = 800;

    private static final String BEFORE_FILE = "before.png";
    private static final String AFTER_FILE_SWT = "after_swt.png";
    private static final String AFTER_FILE_SKIA = "after_skia.png";
    private static final String CHANGED_FILE = "changed.png";

    private static String[] filenames = new String[] {

	    BEFORE_FILE, AFTER_FILE_SWT, AFTER_FILE_SKIA, CHANGED_FILE

    };

    public static void main(String[] arg) {

	deleteFiles();

	Display display = new Display();

	Image image = display.getSystemImage(SWT.ICON_QUESTION);

	ImageLoader saver = new ImageLoader();

	{
		saver.data = new ImageData[] { image.getImageData() };
	    saver.save(BEFORE_FILE, SWT.IMAGE_PNG);
	}

	{
	    saver.data = new ImageData[] { image.getImageData(zoom) };
	    saver.save(AFTER_FILE_SWT, SWT.IMAGE_PNG);

	}
	{
	    SkijaImageDataProvider skiaScale = new SkijaImageDataProvider(image.getImageData());
		saver.data = new ImageData[] { skiaScale.getImageData(zoom) };
	    saver.save(AFTER_FILE_SKIA, SWT.IMAGE_PNG);
	    image = display.getSystemImage(SWT.ICON_WARNING);
	    image = display.getSystemImage(SWT.ICON_WARNING);
	    ImageUtils.setImageDataProvider(image, skiaScale);
	    saver.data = new ImageData[] { image.getImageData() };
	    saver.save(CHANGED_FILE, SWT.IMAGE_PNG);
	}

	if (DELETE_AFTER_EXECUTE) {
	    deleteFiles();
	}

    }

    private static void deleteFiles() {

	for (var s : filenames) {

	    File f = new File(s);
	    if (f.exists())
		f.delete();

	}

    }

}
