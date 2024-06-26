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

import static org.junit.Assert.*;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.junit.jupiter.api.*;

class ScalingSWTFontRegistryTests {
	private static String TEST_FONT = "Helvetica";
	private SWTFontRegistry fontRegistry;

	@BeforeAll
	public static void assumeIsFittingPlatform() {
		PlatformSpecificExecution.assumeIsFittingPlatform();
	}

	@BeforeEach
	public void setUp() {
		this.fontRegistry = new ScalingSWTFontRegistry(Display.getDefault());
	}

	@AfterEach
	public void tearDown() {
		if (this.fontRegistry != null) {
			this.fontRegistry.dispose();
		}
	}

	@Test
	public void systemFontsAreCached() {
		Font font100_1 = fontRegistry.getSystemFont(100);
		Font font100_2 = fontRegistry.getSystemFont(100);
		assertTrue("System fonts for same zoom factor must be reused", font100_1 == font100_2);
	}

	@Test
	public void systemFontsAreScaled() {
		FontData font100 = fontRegistry.getSystemFont(100).getFontData()[0];
		FontData font200 = fontRegistry.getSystemFont(200).getFontData()[0];
		assertEquals("Point height must be equal for all zoom factors", font100.getHeight(), font200.getHeight());

		int heightFont100 = font100.data.lfHeight;
		int heightFont200 = font200.data.lfHeight;
		assertEquals("Pixel height must be doubled between 100% and 200% zoom factor", heightFont100 * 2, heightFont200);
	}

	@Test
	public void fontsAreCached() {
		FontData fontData = new FontData(TEST_FONT, 10, SWT.NORMAL);
		Font font100_1 = fontRegistry.getFont(fontData, 100);
		FontData fontData2 = new FontData(TEST_FONT, 10, SWT.NORMAL);
		Font font100_2 = fontRegistry.getFont(fontData2, 100);
		assertTrue("Fonts for same font data and zoom factor must be reused", font100_1 == font100_2);
	}

	@Test
	public void fontsAreScaled() {
		FontData fontData = new FontData(TEST_FONT, 10, SWT.NORMAL);
		FontData font100 = fontRegistry.getFont(fontData, 100).getFontData()[0];
		FontData font200 = fontRegistry.getFont(fontData, 200).getFontData()[0];
		assertEquals("Point height must be equal for all zoom factors", font100.getHeight(), font200.getHeight());

		int heightFont100 = font100.data.lfHeight;
		int heightFont200 = font200.data.lfHeight;
		assertEquals("Pixel height must be doubled between 100% and 200% zoom factor", heightFont100 * 2, heightFont200);
	}

	@Test
	public void recreateDisposedFonts() {
		FontData fontData = new FontData(TEST_FONT, 10, SWT.NORMAL);
		Font font200 = fontRegistry.getFont(fontData, 200);
		assertFalse("Font must not be disposed", font200.isDisposed());

		font200.dispose();
		Font font200New = fontRegistry.getFont(fontData, 200);
		assertFalse("Disposed fonts must not be reused in the font registry", font200 == font200New);
		assertFalse("Font must not be disposed", font200New.isDisposed());
	}
}
