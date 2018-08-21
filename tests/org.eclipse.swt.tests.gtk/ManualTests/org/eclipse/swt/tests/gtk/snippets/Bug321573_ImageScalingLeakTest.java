/*******************************************************************************
 * Copyright (c) 2018 Red Hat and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Red Hat - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tests.gtk.snippets;


import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;

public class Bug321573_ImageScalingLeakTest {

	public static void main(String[] args) {
		Display display = Display.getDefault();
		Image image = new Image(display, 3200, 2400);
		ImageData imageData = image.getImageData();
		image.dispose();
		long time = System.currentTimeMillis();
		for (int i = 0; i < 10000; i++) {
			image = new Image(display, imageData);
			image = scale(image, 320, 240);
			image.dispose();
			image = null;
			System.gc(); // just make sure we don't see the Java heap growing
			System.out.println(i);
		}
		System.out.println(System.currentTimeMillis() - time);
	}

	public static Image scale(Image image, int width, int height) {
		Rectangle bounds = image.getBounds();
		Image thumbnail = new Image(image.getDevice(), width, height);
		GC gc = null;
		try {
			gc = new GC(thumbnail);
			gc.drawImage(image, 0, 0, bounds.width, bounds.height, 0, 0, width,
					height);
			return thumbnail;
		} finally {
			image.dispose();
			if (gc != null)
				gc.dispose();
		}
	}

}