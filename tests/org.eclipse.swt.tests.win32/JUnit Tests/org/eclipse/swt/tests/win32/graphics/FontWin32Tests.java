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
package org.eclipse.swt.tests.win32.graphics;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.internal.DPIUtil;
import org.eclipse.swt.widgets.Display;
import org.junit.Test;

/**
 * Automated Tests for class org.eclipse.swt.graphics.Font
 * for Windows specific behavior
 *
 * @see org.eclipse.swt.graphics.Font
 */
public class FontWin32Tests {
	private static String TEST_FONT = "Helvetica";
	@Test
	public void fontsAreNotScaledWithoutAutoscale() {
		Display display = Display.getDefault();
		display.dispose();
		DPIUtil.autoScaleOnRuntime = false;
		display = new Display();
		int nativeZoom = DPIUtil.getNativeDeviceZoom();
		Font font = new Font(display, TEST_FONT, 10, SWT.NORMAL);
		try {
			Font scaledFont = font.scaleFor(nativeZoom);
			assertTrue("Fonts are reused for same zoom", scaledFont == font);
			scaledFont = font.scaleFor(nativeZoom*2);

			int fontHeightNative = font.getFontData()[0].data.lfHeight;
			int heightFontNativeDoubled = scaledFont.getFontData()[0].data.lfHeight;
			assertEquals("Fonts are not scaled with auto scale deactivated", fontHeightNative, heightFontNativeDoubled);
		} finally {
			font.dispose();
			display.dispose();
		}
	}

	@Test
	public void fontsAreScaledWithAutoscale() {
		Display display = Display.getDefault();
		display.dispose();
		DPIUtil.autoScaleOnRuntime = true;
		display = new Display();
		int nativeZoom = DPIUtil.getNativeDeviceZoom();
		Font font = new Font(display, TEST_FONT, 10, SWT.NORMAL);
		try {
			Font scaledFont = font.scaleFor(nativeZoom);
			assertTrue("Reuse fonts for same zoom", scaledFont == font);
			scaledFont = font.scaleFor(nativeZoom * 2);

			int heightFontNative = font.getFontData()[0].data.lfHeight;
			int heightFontNativeDoubled = scaledFont.getFontData()[0].data.lfHeight;
			assertFalse("Do not reuse fonts for different zoom", scaledFont == font);
			assertEquals("Pixel height must be doubled between native and doubled native zoom", heightFontNative * 2, heightFontNativeDoubled);
		} finally {
			DPIUtil.autoScaleOnRuntime = false;
			font.dispose();
			display.dispose();
		}
	}
}
