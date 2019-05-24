/*******************************************************************************
 * Copyright (c) 2019 Andrey Loskutov and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Andrey Loskutov - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tests.gtk.snippets;
import java.io.File;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

class Bug547529_ControlPrintBroken {
	public static void main(String[] args) {
		String filename = System.getenv("HOME") + "/some_canvas";

		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setLayout(new RowLayout());
		shell.setSize(200, 200);
		shell.setText("Canvas Bug");

		Composite composite = canvas(display, shell);

		shell.open();

		snapshot(display, composite, filename);

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();
	}

	private static Composite canvas(Display display, Shell shell) {
		Composite composite = new Composite(shell, SWT.NONE);
		RowData layoutData = new RowData(100, 100);
		composite.setLayoutData(layoutData);

		composite.setLayout(new RowLayout());
		Canvas canvas = new Canvas(composite, SWT.NONE);
		layoutData = new RowData(10, 10);
		canvas.setLayoutData(layoutData);

		Color white = display.getSystemColor(SWT.COLOR_WHITE);
		Color red = display.getSystemColor(SWT.COLOR_RED);
		Color green = display.getSystemColor(SWT.COLOR_GREEN);
		Color blue = display.getSystemColor(SWT.COLOR_BLUE);

		canvas.addPaintListener(e -> {
			Rectangle clientArea = canvas.getClientArea();
			e.gc.setBackground(red);
			e.gc.fillRectangle(0, 0, clientArea.width / 2, clientArea.height / 2);
			e.gc.setBackground(green);
			e.gc.fillRectangle(clientArea.width / 2, 0, clientArea.width, clientArea.height / 2);
			e.gc.setBackground(blue);
			e.gc.fillRectangle(0, clientArea.height / 2, clientArea.width, clientArea.height);
			e.gc.setBackground(white);
			e.gc.fillRectangle(clientArea.width / 2, clientArea.height / 2, clientArea.width, clientArea.height);
		});
		return canvas;
	}

	private static void snapshot(Display display, Composite composite, String filename) {
		Rectangle bounds = composite.getBounds();
		Image image = new Image(display, bounds);
		GC gc = new GC(image);
		composite.print(gc);
		gc.dispose();

		ImageLoader loader = new ImageLoader();
		loader.data = new ImageData[] { image.getImageData() };
		File file = new File(filename + ".png");
		file.delete();
		loader.save(filename + ".png", SWT.IMAGE_PNG);


		loader = new ImageLoader();
		ImageData[] loaded = loader.load(file.getAbsolutePath());
		Shell shell = display.getShells()[0];
		for (ImageData d : loaded) {
			Label l = new Label(shell, SWT.NONE);
			image = new Image(display, d);
			l.setImage(image);
		}

		loader.save(filename + "_2.png", SWT.IMAGE_PNG);
		shell.pack();
	}
}
