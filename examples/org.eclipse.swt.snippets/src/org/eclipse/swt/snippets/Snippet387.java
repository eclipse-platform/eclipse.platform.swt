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
package org.eclipse.swt.snippets;

import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

/**
 * Demonstrates different ways of programmatically creating an image with a GC
 * <p>
 * When creating an image programmatically, it is important to use an
 * appropriate way of doing that when at the time of image creation it is not
 * yet known at which zoom the image will be used. This is particularly the case
 * when using monitor-specific scaling on Windows.
 * <p>
 * There are two general options how to programmatically create an image:
 * <ul>
 * <li>{@link ImageGcDrawer}: create an image with an {@link ImageGcDrawer};
 * preferred way as the drawer will be called with the requested zoom when
 * needed
 * <li>{@link GC} on image: create an image with fixed dimensions and create a
 * {@link GC} for the image to drawn on; this is the legacy way of drawing an
 * image and should only be used if the whole drawing logic cannot be wrapped
 * into an {@link ImageGcDrawer} but the {@link GC} may be passed around to
 * collect drawing contributions from different places
 * </ul>
 * When using the second approach creating a {@link GC} on top of an image, it
 * is important to not dispose the GC before using the image, as in that case
 * the Windows implementation of an image can request its regeneration at the
 * desired zoom from the GC. If the GC is disposed before the image is used at
 * desired zoom, it may need to blurry-scale any existing drawing result to the
 * requested zoom instead of sharply rendering it at that zoom.
 * <p>
 * This snippet demonstrates both proper usages of the above mentioned options
 * as well as a usage that disposes the GC before consuming the image at the
 * desired zoom, leading to a blurry result. To see such a bad result, the
 * snippet needs to be executed on Windows and has to be moved between monitors
 * with different monitor zooms.
 * <p>
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 * </p>
 */
public class Snippet387 {

	private static final int PADDING = 5;
	private static final int IMAGE_WIDTH = 270;
	private static final int IMAGE_HEIGHT = 50;

	public static void main(String[] args) {
		activateMonitorSpecificScaling();

		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setText("Programmatic Images");
		shell.setSize(IMAGE_WIDTH + 2 * PADDING + 20, 3 * IMAGE_HEIGHT + 4 * PADDING + 40);

		Image staticImage = createStaticImage(display, "Static Image with disposed GC", true);
		Image staticImageUndisposedGC = createStaticImage(display, "Static Image with non-disposed GC", false);
		Image dynamicImage = createDynamicImage(display, "Dynamic Image");

		shell.addPaintListener(event -> {
			GC gc = event.gc;
			gc.drawImage(staticImage, PADDING, PADDING);
			gc.drawImage(staticImageUndisposedGC, PADDING, 2 * PADDING + IMAGE_HEIGHT);
			gc.drawImage(dynamicImage, PADDING, 3 * PADDING + 2 * IMAGE_HEIGHT);
		});

		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();
	}

	private static void activateMonitorSpecificScaling() {
		System.setProperty("swt.autoScale.updateOnRuntime", "true");
	}

	private static Image createStaticImage(Display display, String text, boolean disposeGC) {
		ImageGcDrawer imageGcDrawer = (gc, imageWidth, imageHeight) -> {
			drawImageContent(gc, text, imageWidth, imageHeight);
		};
		Image staticImage = new Image(display, imageGcDrawer, IMAGE_WIDTH, IMAGE_HEIGHT);
		return staticImage;
	}

	private static Image createDynamicImage(Display display, String text) {
		return new Image(display, (gc, w, h) -> drawImageContent(gc, text, w, h), IMAGE_WIDTH, IMAGE_HEIGHT);
	}

	private static void drawImageContent(GC gc, String text, int width, int height) {
		gc.drawRectangle(0, 0, width - 1, height - 1);
		gc.drawText(text, PADDING, PADDING);
	}

}