/*******************************************************************************
 * Copyright (c) 2000, 2024 Yatta Solutions and others.
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
package org.eclipse.swt.graphics;

import java.util.*;

import org.eclipse.swt.internal.win32.*;

class DefaultSWTFontRegistry implements SWTFontRegistry {
	private static FontData KEY_SYSTEM_FONTS = new FontData();
	private Map<FontData, Font> fontsMap = new HashMap<>();
	private Device device;

	public DefaultSWTFontRegistry(Device device) {
		this.device = device;
	}

	@Override
	public Font getSystemFont(int zoomFactor) {
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
	public Font getFont(FontData fontData, int zoomFactor) {
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
