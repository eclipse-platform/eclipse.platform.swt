/*******************************************************************************
 * Copyright (c) 2024 Yatta Solutions
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Yatta Solutions - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.widgets;

import static org.junit.Assert.*;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.*;
import org.junit.jupiter.api.*;

/**
 * Automated Tests for class org.eclipse.swt.widgets.Control
 * for Windows specific behavior
 *
 * @see org.eclipse.swt.widgets.Control
 */
class ControlWin32Tests extends Win32AutoscaleTestBase {

	@Test
	public void testScaleFontCorrectlyInAutoScaleSzenario() {
		assertTrue("Autoscale property is not set to true", DPITestUtil.isAutoScaleOnRuntimeActive());

		int scalingFactor = 2;
		Control control = new Composite(shell, SWT.NONE);
		int zoom = DPIUtil.getDeviceZoom();
		int newZoom = zoom * scalingFactor;
		try {
			Font oldFont = control.getFont();
			changeDPIZoom(newZoom);
			control.setFont(oldFont);
			Font newFont = control.getFont();
			FontData fontData = oldFont.getFontData()[0];
			FontData currentFontData = newFont.getFontData()[0];
			int heightInPixels = fontData.data.lfHeight;
			int currentHeightInPixels = currentFontData.data.lfHeight;
			assertEquals("Font height in points is different on different zoom levels", fontData.getHeight(), currentFontData.getHeight());
			assertEquals("Font height in pixels is not adjusted according to the scale factor", heightInPixels * scalingFactor, currentHeightInPixels);
		} finally {
			control.dispose();
		}
	}

	@Test
	public void testDoNotScaleFontCorrectlyInNoAutoScaleSzenario() {
		autoScaleOnRuntime(false);
		assertFalse("Autoscale property is not set to false", DPITestUtil.isAutoScaleOnRuntimeActive());

		int scalingFactor = 2;
		Control control = new Composite(shell, SWT.NONE);
		int zoom = DPIUtil.getDeviceZoom();
		int newZoom = zoom * scalingFactor;
		try {
			Font oldFont = control.getFont();
			changeDPIZoom(newZoom);
			control.setFont(oldFont);
			Font newFont = control.getFont();
			FontData fontData = oldFont.getFontData()[0];
			FontData currentFontData = newFont.getFontData()[0];
			int heightInPixels = fontData.data.lfHeight;
			int currentHeightInPixels = currentFontData.data.lfHeight;
			assertEquals("Font height in points is different on different zoom levels", fontData.getHeight(), currentFontData.getHeight());
			assertEquals("Font height in pixels is different when setting the same font again", heightInPixels, currentHeightInPixels);
		} finally {
			control.dispose();
		}
	}
}
