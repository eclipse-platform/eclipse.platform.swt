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
package org.eclipse.swt.internal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;

@ExtendWith(PlatformSpecificExecutionExtension.class)
class LegacySWTFontRegistryTests {
	private static String TEST_FONT = "Helvetica";
	private Display display;
	private SWTFontRegistry fontRegistry;

	@SuppressWarnings("removal")
	@BeforeEach
	public void setUp() {
		this.display = Display.getDefault();
		this.fontRegistry = new LegacySWTFontRegistry(display);
	}

	@AfterEach
	public void tearDown() {
		if (this.fontRegistry != null) {
			this.fontRegistry.dispose();
		}
		display.dispose();
	}

	@Test
	public void systemFontsAreCached() {
		Font font1 = fontRegistry.getSystemFont(100);
		Font font2 = fontRegistry.getSystemFont(100);
		assertTrue(font1 == font2, "System fonts for same zoom factor must be reused");
	}

	@Test
	public void systemFontsAlwaysDependOnPrimaryZoom() {
		int primaryZoom = display.getPrimaryMonitor().getZoom();
		FontData fontPrimary = fontRegistry.getSystemFont(primaryZoom).getFontData()[0];
		FontData font100 = fontRegistry.getSystemFont(100).getFontData()[0];
		assertEquals(fontPrimary.getHeight(), font100.getHeight(), "Point height must be equal for all zoom levels");
		FontData font200 = fontRegistry.getSystemFont(200).getFontData()[0];
		assertEquals(fontPrimary.getHeight(), font200.getHeight(), "Point height must be equal for all zoom levels");

		int heightFontPrimary = fontPrimary.data.lfHeight;
		int heightFont100 = font100.data.lfHeight;
		assertEquals(heightFontPrimary, heightFont100, "Pixel height must not differ between primary monitor and 100% zoom");
		int heightFont200 = font200.data.lfHeight;
		assertEquals(heightFontPrimary, heightFont200, "Pixel height must not differ between primary monitor and 200% zoom");
	}

	@Test
	public void fontsAreCached() {
		int primaryZoom = display.getPrimaryMonitor().getZoom();
		FontData fontData = new FontData(TEST_FONT, 10, SWT.NORMAL);
		Font font1 = fontRegistry.getFont(fontData, primaryZoom);
		FontData fontData2 = new FontData(TEST_FONT, 10, SWT.NORMAL);
		Font font2 = fontRegistry.getFont(fontData2, primaryZoom);
		assertTrue(font1 == font2, "Fonts for same font data and zoom levels must be reused");
	}

	@Test
	public void fontsAlwaysDependOnPrimaryZoom() {
		int primaryZoom = display.getPrimaryMonitor().getZoom();
		FontData fontData = new FontData(TEST_FONT, 10, SWT.NORMAL);
		FontData fontPrimary = fontRegistry.getFont(fontData, primaryZoom).getFontData()[0];
		FontData font100 = fontRegistry.getFont(fontData, 100).getFontData()[0];
		assertEquals(fontPrimary.getHeight(), font100.getHeight(), "Point height must be equal for all zoom levels");
		FontData font200 = fontRegistry.getFont(fontData, 200).getFontData()[0];
		assertEquals(fontPrimary.getHeight(), font200.getHeight(), "Point height must be equal for all zoom levels");

		int heightFontPrimary = fontPrimary.data.lfHeight;
		int heightFont100 = font100.data.lfHeight;
		assertEquals(heightFontPrimary, heightFont100, "Pixel height must not differ between primary monitor and 100% zoom");
		int heightFont200 = font200.data.lfHeight;
		assertEquals(heightFontPrimary, heightFont200, "Pixel height must not differ between primary monitor and 200% zoom");
	}
}
