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

public class SnippetSkijaGC {
	// if you wish to check the images, set this to false
	static boolean DELETE_AFTER_EXECUTE = true;

	static String[] FILENAMES = new String[] {
		"testout_before.png", "testout_after.png"
	};

	public static void main(String[] args) {
		deleteFiles();

		Display display = new Display();
		Image image1 = display.getSystemImage(SWT.ICON_QUESTION);

		ImageData img = image1.getImageData();

		byte[] bytes = null;
		var out = ImageUtils.createGenericImage(new SkijaImageDataProvider(img)).getImageData(400);

		ImageLoader saver = new ImageLoader();

		{
			saver.data = new ImageData[] { out };
			saver.save(FILENAMES[0], SWT.IMAGE_PNG);
		}

		bytes = SkijaGC.convertToRGBA(out);
		var colType = ColorType.RGBA_8888;

		ImageInfo imageInfo = new ImageInfo(out.width, out.height, colType, ColorAlphaType.UNPREMUL);

		var i = io.github.humbleui.skija.Image.makeRasterFromBytes(imageInfo, bytes, out.width * 4);

		SkijaGC.writeFile(FILENAMES[1], i);
		if (DELETE_AFTER_EXECUTE) {
			deleteFiles();
		}
	}

	private static void deleteFiles() {
		for (var s : FILENAMES) {
			File f = new File(s);
			if (f.exists()) {
				f.delete();
			}
		}
	}
}
