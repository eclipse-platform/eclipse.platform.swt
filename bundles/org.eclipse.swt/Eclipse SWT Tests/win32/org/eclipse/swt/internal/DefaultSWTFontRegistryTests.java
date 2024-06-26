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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.junit.jupiter.api.*;

class DefaultSWTFontRegistryTests {
	private static String TEST_FONT = "Helvetica";
	private Display display;
	private SWTFontRegistry fontRegistry;

	@BeforeAll
	public static void assumeIsFittingPlatform() {
		PlatformSpecificExecution.assumeIsFittingPlatform();
	}

	@BeforeEach
	public void setUp() {
		this.display = Display.getDefault();
		this.fontRegistry = new DefaultSWTFontRegistry(display);
	}

	@AfterEach
	public void tearDown() {
		if (this.fontRegistry != null) {
			this.fontRegistry.dispose();
		}
	}

	@Test
	public void systemFontsAreCached() {
		Font font1 = fontRegistry.getSystemFont(100);
		Font font2 = fontRegistry.getSystemFont(100);
		assertTrue("System fonts for same zoom factor must be reused", font1 == font2);
	}

	@Test
	public void systemFontsAlwaysDependOnPrimaryZoom() {
		int primaryZoom = display.getPrimaryMonitor().getZoom();
		FontData fontPrimary = fontRegistry.getSystemFont(primaryZoom).getFontData()[0];
		FontData font100 = fontRegistry.getSystemFont(100).getFontData()[0];
		assertEquals("Point height must be equal for all zoom levels", fontPrimary.getHeight(), font100.getHeight());
		FontData font200 = fontRegistry.getSystemFont(200).getFontData()[0];
		assertEquals("Point height must be equal for all zoom levels", fontPrimary.getHeight(), font200.getHeight());

		int heightFontPrimary = fontPrimary.data.lfHeight;
		int heightFont100 = font100.data.lfHeight;
		assertEquals("Pixel height must not differ between primary monitor and 100% zoom", heightFontPrimary, heightFont100);
		int heightFont200 = font200.data.lfHeight;
		assertEquals("Pixel height must not differ between primary monitor and 200% zoom", heightFontPrimary, heightFont200);
	}

	@Test
	public void fontsAreCached() {
		int primaryZoom = display.getPrimaryMonitor().getZoom();
		FontData fontData = new FontData(TEST_FONT, 10, SWT.NORMAL);
		Font font1 = fontRegistry.getFont(fontData, primaryZoom);
		FontData fontData2 = new FontData(TEST_FONT, 10, SWT.NORMAL);
		Font font2 = fontRegistry.getFont(fontData2, primaryZoom);
		assertTrue("Fonts for same font data and zoom levels must be reused", font1 == font2);
	}

	@Test
	public void fontsAlwaysDependOnPrimaryZoom() {
		int primaryZoom = display.getPrimaryMonitor().getZoom();
		FontData fontData = new FontData(TEST_FONT, 10, SWT.NORMAL);
		FontData fontPrimary = fontRegistry.getFont(fontData, primaryZoom).getFontData()[0];
		FontData font100 = fontRegistry.getFont(fontData, 100).getFontData()[0];
		assertEquals("Point height must be equal for all zoom levels", fontPrimary.getHeight(), font100.getHeight());
		FontData font200 = fontRegistry.getFont(fontData, 200).getFontData()[0];
		assertEquals("Point height must be equal for all zoom levels", fontPrimary.getHeight(), font200.getHeight());

		int heightFontPrimary = fontPrimary.data.lfHeight;
		int heightFont100 = font100.data.lfHeight;
		assertEquals("Pixel height must not differ between primary monitor and 100% zoom", heightFontPrimary, heightFont100);
		int heightFont200 = font200.data.lfHeight;
		assertEquals("Pixel height must not differ between primary monitor and 200% zoom", heightFontPrimary, heightFont200);
	}
}
