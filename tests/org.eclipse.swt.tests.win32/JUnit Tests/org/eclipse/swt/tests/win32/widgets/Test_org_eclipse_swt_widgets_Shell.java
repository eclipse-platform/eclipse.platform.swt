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
package org.eclipse.swt.tests.win32.widgets;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.tests.win32.SwtWin32TestUtil;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class Test_org_eclipse_swt_widgets_Shell {

	private Display display;
	private Shell shell;

	@BeforeEach
	public void setup() {
		display = new Display();
		shell = new Shell(display);
	}

	@AfterEach
	public void tearDown() {
		shell.dispose();
		display.dispose();
	}

	@Test
	public void test_printKeepsChildPositions() throws InterruptedException {
		Composite red = new Composite(shell, SWT.NONE);
		red.setBackground(display.getSystemColor(SWT.COLOR_RED));
		red.setBounds(0, 0, 50, 50);
		Composite blue = new Composite(shell, SWT.NONE);
		blue.setBackground(display.getSystemColor(SWT.COLOR_BLUE));
		blue.setBounds(100, 60, 50, 50);
		shell.setSize(300, 250);
		shell.open();
		SwtWin32TestUtil.processEvents(display, 500, null);

		Rectangle clientArea = shell.getClientArea();
		Image image = new Image(display, clientArea.width, clientArea.height);
		try {
			GC gc = new GC(image);
			try {
				gc.setBackground(display.getSystemColor(SWT.COLOR_WHITE));
				gc.fillRectangle(0, 0, clientArea.width, clientArea.height);
				assertTrue(shell.print(gc));
			} finally {
				gc.dispose();
			}
			ImageData data = image.getImageData();
			assertAll(
					() -> assertEquals(new RGB(255, 0, 0), data.palette.getRGB(data.getPixel(25, 25)), "red child"),
					() -> assertEquals(new RGB(0, 0, 255), data.palette.getRGB(data.getPixel(125, 85)), "blue child"));
		} finally {
			image.dispose();
		}
	}
}
