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
 * This class is used in the win32 implementation only to support
 * unscaled fonts in multiple DPI zoom levels.
 *
 * As this class is only intended to be used internally via {@code SWTFontProvider},
 * it should neither be instantiated nor referenced in a client application.
 * The behavior can change any time in a future release.
 *
 * @noreference This class is not intended to be referenced by clients.
 * @noinstantiate This class is not intended to be instantiated by clients.
 */
public final class DefaultSWTFontRegistry implements SWTFontRegistry {
	private static FontData KEY_SYSTEM_FONTS = new FontData();
	private Map<FontData, Font> fontsMap = new HashMap<>();
	private Device device;

	public DefaultSWTFontRegistry(Device device) {
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
		Font font = Font.win32_new(device, hFont);
		registerFont(KEY_SYSTEM_FONTS, font);
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

	private Font registerFont(FontData fontData, Font font) {
		fontsMap.put(fontData, font);
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
