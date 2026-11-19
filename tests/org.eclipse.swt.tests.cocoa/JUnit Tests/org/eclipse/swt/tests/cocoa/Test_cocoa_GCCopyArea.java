/*******************************************************************************
 * Copyright (c) 2026 vogella GmbH and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package org.eclipse.swt.tests.cocoa;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class Test_cocoa_GCCopyArea {

	// rows of this width need no padding, so the pixel reads do not depend on the row stride
	private static final int SIZE = 16;

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
	public void copyAreaOfBorderedCompositeStartsAtClientArea() {
		RGB blue = new RGB(0, 0, 255);
		RGB red = new RGB(255, 0, 0);
		Composite composite = new Composite(shell, SWT.BORDER);
		composite.setBackground(new Color(blue));
		composite.setBounds(10, 10, 100, 80);
		Composite child = new Composite(composite, SWT.NONE);
		child.setBackground(new Color(red));
		child.setBounds(4, 4, 8, 8);
		openShell();

		ImageData data = copyArea(composite, 0, 0);
		assertColor(blue, data, 0, 0);
		assertColor(red, data, 4, 4);
		assertColor(red, data, 11, 11);
		assertColor(blue, data, 12, 12);
		assertColor(blue, data, SIZE - 1, SIZE - 1);

		data = copyArea(composite, 4, 4);
		assertColor(red, data, 0, 0);
		assertColor(red, data, 7, 7);
		assertColor(blue, data, 8, 8);
	}

	@Test
	public void copyAreaOfTransparentControlIncludesParentBackground() {
		RGB red = new RGB(255, 0, 0);
		shell.setBackground(new Color(red));
		Label label = new Label(shell, SWT.NONE);
		label.setBounds(10, 10, 100, 50);
		openShell();

		ImageData data = copyArea(label, 0, 0);
		assertColor(red, data, 0, 0);
		assertColor(red, data, SIZE - 1, SIZE - 1);
	}

	private void openShell() {
		shell.setSize(200, 200);
		shell.open();
		while (display.readAndDispatch()) {
			// flush pending events
		}
	}

	private ImageData copyArea(Control control, int x, int y) {
		Image image = new Image(display, SIZE, SIZE);
		GC gc = new GC(control);
		try {
			gc.copyArea(image, x, y);
		} finally {
			gc.dispose();
		}
		ImageData data = image.getImageData();
		image.dispose();
		return data;
	}

	// the capture goes through the display color profile, which shifts channels slightly on some machines
	private static void assertColor(RGB expected, ImageData data, int x, int y) {
		RGB actual = data.palette.getRGB(data.getPixel(x, y));
		int tolerance = 16;
		assertTrue(Math.abs(expected.red - actual.red) <= tolerance && Math.abs(expected.green - actual.green) <= tolerance
				&& Math.abs(expected.blue - actual.blue) <= tolerance,
				"expected " + expected + " at " + x + "," + y + " but was " + actual);
	}
}
