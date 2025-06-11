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

import java.util.*;

import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.win32.*;

/**
 * <p>
 * Formerly {@code DefaultSWTFontRegistry}, this class is deprecated. Use {@code ScalingSWTFontRegistry} instead.
 * To temporarily fall back to legacy font behavior ({@code LegacySWTFontRegistry})
 * (e.g., if issues arise in existing RCP products), set the system property: {@code
 * -Dswt.fontRegistry=legacy
 * }
 * </p>
 *
 * As this class is only intended to be used internally via {@code SWTFontProvider},
 * it should neither be instantiated nor referenced in a client application.
 * The behavior can change any time in a future release.
 */
@Deprecated(forRemoval= true, since= "2025-09")
final class LegacySWTFontRegistry implements SWTFontRegistry {
	private static FontData KEY_SYSTEM_FONTS = new FontData();
	private Map<FontData, Font> fontsMap = new HashMap<>();
	private Device device;

	LegacySWTFontRegistry(Device device) {
		this.device = device;
	}

	@Override
	public Font getSystemFont(int zoom) {
		if (fontsMap.containsKey(KEY_SYSTEM_FONTS)) {
			return fontsMap.get(KEY_SYSTEM_FONTS);
		}

		long hFont = 0;
		NONCLIENTMETRICS info = new NONCLIENTMETRICS ();
		info.cbSize = NONCLIENTMETRICS.sizeof;
		if (OS.SystemParametersInfo (OS.SPI_GETNONCLIENTMETRICS, 0, info, 0)) {
			hFont = OS.CreateFontIndirect (info.lfMessageFont);
		}
		if (hFont == 0) hFont = OS.GetStockObject (OS.DEFAULT_GUI_FONT);
		if (hFont == 0) hFont = OS.GetStockObject (OS.SYSTEM_FONT);
		Font font = Font.win32_new(device, hFont, zoom);
		registerFont(KEY_SYSTEM_FONTS, font);
		registerFont(font.getFontData()[0], font);
		return font;
	}

	@Override
	public Font getFont(FontData fontData, int zoom) {
		if (fontsMap.containsKey(fontData)) {
			Font font = fontsMap.get(fontData);
			if (font.isDisposed()) {
				// Remove disposed cached fonts
				fontsMap.remove(fontData);
			} else {
				return font;
			}
		}
		Font font = new Font(device, fontData);
		registerFont(fontData, font);
		return font;
	}

	@Override
	public Font getFont(long fontHandle, int zoom) {
		return Font.win32_new(device, fontHandle, zoom);
	}

	private Font registerFont(FontData fontData, Font font) {
		FontData clonedFontData = new FontData(fontData);
		fontsMap.put(clonedFontData, font);
		return font;
	}

	@Override
	public void dispose() {
		for (Font font : fontsMap.values()) {
			if (font != null) {
				font.dispose();
			}
		}
	}
}
