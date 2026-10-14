/*******************************************************************************
 * Copyright (c) 2026 Aleksandar Kurtakov and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package org.eclipse.swt.tests.gtk;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class Test_GtkButton {

	private Display display;
	private Shell shell;

	@BeforeEach
	public void setUp() {
		display = Display.getDefault();
		shell = new Shell(display);
	}

	@AfterEach
	public void tearDown() {
		shell.dispose();
	}

	@Test
	public void test_setImage_checkAndRadioDrawImage() {
		shell.setLayout(new GridLayout());
		Color red = display.getSystemColor(SWT.COLOR_RED);
		Image image = new Image(display, (gc, width, height) -> {
			gc.setBackground(red);
			gc.fillRectangle(0, 0, width, height);
		}, 16, 16);
		try {
			Button[] imageButtons = { new Button(shell, SWT.CHECK), new Button(shell, SWT.RADIO) };
			for (Button imageButton : imageButtons) {
				imageButton.setImage(image);
			}
			shell.pack();
			shell.open();
			processEvents();
			for (Button imageButton : imageButtons) {
				Point size = imageButton.getSize();
				Image snapshot = new Image(display, size.x, size.y);
				try {
					GC gc = new GC(snapshot);
					try {
						imageButton.print(gc);
					} finally {
						gc.dispose();
					}
					ImageData data = snapshot.getImageData();
					int redPixels = 0;
					for (int y = 0; y < data.height; y++) {
						for (int x = 0; x < data.width; x++) {
							RGB rgb = data.palette.getRGB(data.getPixel(x, y));
							if (rgb.red > 200 && rgb.green < 60 && rgb.blue < 60) redPixels++;
						}
					}
					assertTrue(redPixels >= 16 * 16 / 2, "Image not drawn in " + imageButton + ", red pixels: " + redPixels);
				} finally {
					snapshot.dispose();
				}
			}
		} finally {
			image.dispose();
		}
	}

	private void processEvents() {
		while (display.readAndDispatch()) {
		}
	}
}
