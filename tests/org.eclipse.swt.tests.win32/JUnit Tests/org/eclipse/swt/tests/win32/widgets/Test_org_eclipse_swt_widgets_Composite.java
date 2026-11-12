/*******************************************************************************
 * Copyright (c) 2026 Vector Informatik GmbH and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Vector Informatik GmbH - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tests.win32.widgets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.internal.DPIUtil;
import org.eclipse.swt.internal.win32.OS;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Automated Tests for class org.eclipse.swt.widgets.Composite
 * for Windows specific behavior
 *
 * @see org.eclipse.swt.widgets.Composite
 */
@SuppressWarnings("restriction")
public class Test_org_eclipse_swt_widgets_Composite {

	private Display display;

	@BeforeEach
	public void setUp() {
		display = Display.getDefault();
	}

	@AfterEach
	public void tearDown() {
		display.dispose();
	}

	/**
	 * A canvas that prints itself must use a font scaled for its own zoom. The
	 * device zoom is not a valid substitute: with monitor-specific scaling it
	 * reflects the zoom of the shell whose monitor scaling has changed most
	 * recently, which is a different one as soon as the shells of an application
	 * are spread over monitors with different scale factors.
	 */
	@Test
	public void test_printClientUsesFontOfControlZoom() {
		Shell shell = new Shell(display);
		int deviceZoom = DPIUtil.getNativeDeviceZoom();
		try {
			Canvas canvas = new Canvas(shell, SWT.NONE);
			canvas.setSize(100, 50);
			FontData[] printedFontData = new FontData[1];
			canvas.addPaintListener(event -> printedFontData[0] = event.gc.getFont().getFontData()[0]);
			FontData expectedFontData = canvas.getFont().getFontData()[0];

			Image image = new Image(display, canvas.getSize().x, canvas.getSize().y);
			GC gc = new GC(image);
			try {
				// let the device zoom deviate from the zoom of the canvas, as it does
				// when another shell resides on a monitor with a different scale factor
				DPIUtil.setDeviceZoom(deviceZoom * 2);
				OS.SendMessage(canvas.handle, OS.WM_PRINT, gc.handle, OS.PRF_CLIENT);
			} finally {
				DPIUtil.setDeviceZoom(deviceZoom);
				gc.dispose();
				image.dispose();
			}

			assertNotNull(printedFontData[0], "canvas has not been painted on WM_PRINT");
			assertEquals(expectedFontData.getHeight(), printedFontData[0].getHeight(),
					"printed canvas must use the font for the zoom of the canvas");
		} finally {
			shell.dispose();
		}
	}

}
