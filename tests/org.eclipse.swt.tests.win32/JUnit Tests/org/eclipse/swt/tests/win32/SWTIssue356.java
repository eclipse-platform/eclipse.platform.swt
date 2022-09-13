/*******************************************************************************
 * Copyright (c) 2022 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tests.win32;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.widgets.Display;
import org.junit.Test;

public class SWTIssue356 {

	private static final int IMG_WIDTH = 320;
	private static final int IMG_HEIGHT = 200;
	private static final int IMG_DEPTH = 4;

	@Test
	public void test_loadOutOfSpecBmp() throws IOException {
		Display display = new Display();
		Image image = null;

		try (InputStream is = SwtWin32TestUtil.class.getResourceAsStream("outofspec.bmp")) {

			image = new Image(display, is);
			ImageData imageData = image.getImageData();

			// sanity checks
			assertEquals(IMG_WIDTH, imageData.width);
			assertEquals(IMG_HEIGHT, imageData.height);
			assertEquals(IMG_DEPTH, imageData.depth);
			assertEquals(IMG_WIDTH * IMG_DEPTH / 8, imageData.bytesPerLine);

			// test whether imageData.data contains any values other than '0'
			assertFalse("ImageData.data is blank", Arrays.equals(new byte[imageData.bytesPerLine * IMG_HEIGHT], imageData.data));

		} finally {

			if (image != null) {
				image.dispose();
			}
			display.dispose();

		}
	}
}
