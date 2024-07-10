package org.eclipse.swt.tests.manual;
/*******************************************************************************
 * Copyright (c) 2020 Syntevo and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Syntevo - initial API and implementation
 *******************************************************************************/
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageDataProvider;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.internal.DPIUtil;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
public class Bug568641_GC_drawImage_IAE {
	static final int imageSZ = 20;
	static Image createTestImage(Display display) {
		final Image image = new Image(display, 20, 20);
		final GC gc = new GC(image);
		final int halfSZ = imageSZ / 2;
		gc.setBackground(display.getSystemColor(SWT.COLOR_RED));
		gc.fillRectangle(0, 0, halfSZ, halfSZ);
		gc.setBackground(display.getSystemColor(SWT.COLOR_GREEN));
		gc.fillRectangle(halfSZ, 0, halfSZ, halfSZ);
		gc.setBackground(display.getSystemColor(SWT.COLOR_BLUE));
		gc.fillRectangle(0, halfSZ, halfSZ, halfSZ);
		gc.setBackground(display.getSystemColor(SWT.COLOR_MAGENTA));
		gc.fillRectangle(halfSZ, halfSZ, halfSZ, halfSZ);
		return image;
	}
	static Image createImageWithZoom(Image srcImage, int zoom, boolean isProvider) {
		final int oldZoom = DPIUtil.getDeviceZoom();
		DPIUtil.setDeviceZoom(zoom);
		final Image image;
		if (isProvider) {
			ImageDataProvider imageProvider = _zoom -> srcImage.getImageData(_zoom);
			image = new Image(srcImage.getDevice(), imageProvider);
		} else {
			image = new Image(srcImage.getDevice(), srcImage.getImageData());
		}
		DPIUtil.setDeviceZoom(oldZoom);
		return image;
	}
	static void drawImage(GC gc, Image srcImage, int zoom, boolean isProvider, int x, int y, boolean simple) {
		// Image needs to be created each time, because painting it once
		// will update internal state and prevent proper testing
		final Image image = createImageWithZoom(srcImage, zoom, isProvider);
		if (simple)
			gc.drawImage(image, x, y);
		else {
			final Rectangle bounds = image.getBounds();
			gc.drawImage(image, bounds.x, bounds.y, bounds.width, bounds.height, x, y, bounds.width, bounds.height);
		}
	}
	static void verifyBounds(Image srcImage, int zoom, boolean isProvider) {
		final Image image = createImageWithZoom(srcImage, zoom, isProvider);
		{
			final Rectangle bounds = image.getBounds();
			int expected = imageSZ;
			if ((bounds.x != 0) || (bounds.y != 0) || (bounds.width != expected) || (bounds.height != expected)) {
				System.out.println(
					"ERROR: Incorrect Image.getBounds(): zoom=" + zoom +
						" provider=" + (isProvider ? 'Y' : 'N') +
						" expected=" + expected +
						" actual=" + bounds.width);
			}
		}
		{
			final Rectangle bounds = image.getBounds();
			int expected = imageSZ * zoom / 100;
			if ((bounds.x != 0) || (bounds.y != 0) || (bounds.width != expected) || (bounds.height != expected)) {
				System.out.println(
					"ERROR: Incorrect Image.getBoundsInPixels(): zoom=" + zoom +
						" provider=" + (isProvider ? 'Y' : 'N') +
						" expected=" + expected +
						" actual=" + bounds.width);
			}
		}
	}
	public static void main(String[] args) {
		// System.setProperty("swt.autoScale", "false");
		final Display display = new Display();
		final Shell shell = new Shell(display);
		shell.setLayout(new GridLayout(1, true));
		Label hint = new Label(shell, 0);
		hint.setText(
			"1) Start snippet at DPI=100%\n" +
			"2) Check console for errors\n" +
			"3) Snippet will crash with IllegalArgumentException in GC.drawImage()\n" +
			"\n" +
			"For CLabel demo:\n" +
			"1) Go to your OS system settings and change Display DPI to 200%\n" +
			"2) Start this snippet\n" +
			"3) Change Display DPI to 100%\n" +
			"4) Snippet will crash with IllegalArgumentException in GC.drawImage()"
		);
		final Image image = createTestImage(display);
		// A demonstration that bug occurs even without trickery
		ImageDataProvider imageProvider = zoom -> image.getImageData(zoom);
		CLabel label = new CLabel(shell, 0);
		label.setImage(new Image(display, imageProvider));
		// A more detailed test of all combinations
		final int margin = imageSZ;
		Canvas canvas = new Canvas(shell, 0);
		canvas.setLayoutData(new GridData(10*imageSZ, 5*imageSZ));
		canvas.addListener(SWT.Paint, event -> {
			int y = 0;
			for (int iIsProvider = 0; iIsProvider < 2; iIsProvider++)
			{
				boolean isProvider = (iIsProvider == 0) ? false : true;
				int x = 3;
				verifyBounds(image, 100, isProvider);
				drawImage(event.gc, image, 100, isProvider,  x, y, true);
				x += imageSZ + margin;
				drawImage(event.gc, image, 100, isProvider, x, y, false);
				x += imageSZ + margin;
				verifyBounds(image, 200, isProvider);
				drawImage(event.gc, image, 200, isProvider, x, y, true);
				x += imageSZ + margin;
				drawImage(event.gc, image, 200, isProvider, x, y, false);
				x += imageSZ + margin;
				y += imageSZ + margin;
			}
		});
		shell.pack();
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();
	}
}
