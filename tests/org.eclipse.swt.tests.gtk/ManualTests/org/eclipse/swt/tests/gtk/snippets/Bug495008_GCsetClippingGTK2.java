/*******************************************************************************
 * Copyright (c) 2018 Red Hat and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tests.gtk.snippets;


import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class Bug495008_GCsetClippingGTK2 {
	public static void main(String[] args) {
		final Display display = new Display();

		final PaletteData palette = new PaletteData(0x00FF0000, 0x0000FF00, 0x000000FF);
		int width = 100, height = 100;
		ImageData imageData = new ImageData(width, height, 32, palette);
		Image image = new Image(display, imageData);

		GC gc = new GC(image);
		Rectangle clip = gc.getClipping();
		gc.setClipping(clip);
		gc.setBackground(display.getSystemColor(SWT.COLOR_RED));
		gc.fillRectangle(0, 0, width, height);
		gc.dispose();

		Shell shell = new Shell(display);
		shell.addPaintListener(e -> {
			GC gc1 = e.gc;
			gc1.drawImage(image, 0, 0);
		});

		shell.setSize(350, 550);
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		image.dispose();
		display.dispose();
	}
}