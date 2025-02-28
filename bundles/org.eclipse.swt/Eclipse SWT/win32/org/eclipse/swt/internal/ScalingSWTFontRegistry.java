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
 * scaling of fonts in multiple DPI zoom levels.
 *
 * As this class is only intended to be used internally via {@code SWTFontProvider},
 * it should neither be instantiated nor referenced in a client application.
 * The behavior can change any time in a future release.
 */
final class ScalingSWTFontRegistry implements SWTFontRegistry {
	private abstract class ScaledFontContainer {
		protected Map<Integer, Font> scaledFonts = new HashMap<>();

		private Font getScaledFont(int zoom) {
			if (scaledFonts.containsKey(zoom)) {
				Font font = scaledFonts.get(zoom);
				if (font.isDisposed()) {
					scaledFonts.remove(zoom);
					return createAndCacheFont(zoom);
				}
				return font;
			}
			return createAndCacheFont(zoom);
		}

		private Font createAndCacheFont(int zoom) {
			Font newFont = createFont(zoom);
			scaledFonts.put(zoom, newFont);
			return newFont;
		}

		protected abstract Font createFont(int zoom);

		protected abstract void dispose();
	}

	private class ScaledCustomFontContainer extends ScaledFontContainer {
		private final FontData fontData;

		ScaledCustomFontContainer(FontData fontData) {
			this.fontData = fontData;
		}

		@Override
		protected Font createFont(int zoom) {
			return Font.win32_new(device, fontData, zoom);
		}

		@Override
		protected void dispose() {
			for (Font font : scaledFonts.values()) {
				font.dispose();
			}
		}
	}

	private class ScaledSystemFontContainer extends ScaledFontContainer {
		@Override
		protected Font createFont(int zoom) {
			long newHandle = createSystemFontHandle(zoom);
			return Font.win32_new(device, newHandle, zoom);
		}

		private long createSystemFontHandle(int zoom) {
			long hFont = 0;
			NONCLIENTMETRICS info = new NONCLIENTMETRICS();
			info.cbSize = NONCLIENTMETRICS.sizeof;
			if (fetchSystemParametersInfo(info, zoom)) {
				LOGFONT logFont = info.lfMessageFont;
				hFont = OS.CreateFontIndirect(logFont);
			}
			if (hFont == 0)
				hFont = OS.GetStockObject(OS.DEFAULT_GUI_FONT);
			if (hFont == 0)
				hFont = OS.GetStockObject(OS.SYSTEM_FONT);
			return hFont;
		}

		private static boolean fetchSystemParametersInfo(NONCLIENTMETRICS info, int targetZoom) {
			if (OS.WIN32_BUILD >= OS.WIN32_BUILD_WIN10_1607) {
				return OS.SystemParametersInfoForDpi(OS.SPI_GETNONCLIENTMETRICS, NONCLIENTMETRICS.sizeof, info, 0,
						DPIUtil.mapZoomToDPI(targetZoom));
			} else {
				return OS.SystemParametersInfo(OS.SPI_GETNONCLIENTMETRICS, 0, info, 0);
			}
		}

		@Override
		protected void dispose() {
			// do not dispose the system fonts, they are not tied to the device of this registry
		}
	}

	private ScaledFontContainer systemFontContainer;
	private Map<FontData, ScaledFontContainer> customFontsKeyMap = new HashMap<>();
	private Device device;

	ScalingSWTFontRegistry(Device device) {
		this.device = device;
		systemFontContainer = new ScaledSystemFontContainer();
	}

	@Override
	public Font getSystemFont(int zoom) {
		return systemFontContainer.getScaledFont(zoom);
	}

	@Override
	public Font getFont(FontData fontData, int zoom) {
		ScaledFontContainer container;
		if (customFontsKeyMap.containsKey(fontData)) {
			container = customFontsKeyMap.get(fontData);
		} else {
			container = new ScaledCustomFontContainer(fontData);
			customFontsKeyMap.put(fontData, container);
		}
		return container.getScaledFont(zoom);
	}

	@Override
	public void dispose() {
		customFontsKeyMap.values().forEach(ScaledFontContainer::dispose);
		customFontsKeyMap.clear();
	}
}
