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
package org.eclipse.swt.graphics;

import java.util.*;
import java.util.Map.*;

import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.win32.*;
import org.eclipse.swt.widgets.*;

/**
 * This class is used in the win32 implementation only to support
 * scaling of fonts in multiple DPI zoom level.
 * <p>
 * <b>IMPORTANT:</b> This class is <em>not</em> part of the public
 * API for SWT. It is marked public only so that it can be shared
 * within the packages provided by SWT. It is not available on all
 * platforms, and should never be called from application code.
 * </p>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 * @noreference This class is not intended to be referenced by clients
 */
public final class ScalingSWTFontRegistry implements SWTFontRegistry {
	private class ScaledFontContainer {
		// the first (unknown) font to be requested as scaled variant
		// usually it is scaled to the primary monitor zoom, but that is not guaranteed
		private Font baseFont;
		private Map<Integer, Font> scaledFonts = new HashMap<>();

		ScaledFontContainer(Font baseFont) {
			this.baseFont = baseFont;
			scaledFonts.put(baseFont.zoom, baseFont);
		}

		private Font getScaledFont(int targetZoom) {
			if (scaledFonts.containsKey(targetZoom)) {
				Font font = scaledFonts.get(targetZoom);
				if (font.isDisposed()) {
					scaledFonts.remove(targetZoom);
					return null;
				}
				return font;
			}
			return null;
		}

		private Font scaleFont(int zoom) {
			FontData fontData = baseFont.getFontData()[0];
			fontData.data.lfHeight = computePixels(zoom, fontData);
			Font scaledFont = Font.win32_new(display, fontData, zoom);
			addScaledFont(zoom, scaledFont);
			return scaledFont;
		}

		private void addScaledFont(int targetZoom, Font scaledFont) {
			scaledFonts.put(targetZoom, scaledFont);
		}
	}

	private static FontData KEY_SYSTEM_FONTS = new FontData();
	private Map<Long, ScaledFontContainer> fontHandleMap = new HashMap<>();
	private Map<FontData, ScaledFontContainer> fontKeyMap = new HashMap<>();
	private Display display;

	public ScalingSWTFontRegistry(Display display) {
		this.display = display;
	}

	@Override
	public Font getSystemFont(int zoom) {
		ScaledFontContainer container = getOrCreateBaseSystemFontContainer(display);

		Font systemFont = container.getScaledFont(zoom);
		if (systemFont != null) {
			return systemFont;
		}
		long systemFontHandle = createSystemFont(zoom);
		systemFont = Font.win32_new(display, systemFontHandle, zoom);
		container.addScaledFont(zoom, systemFont);
		return systemFont;
	}

	private ScaledFontContainer getOrCreateBaseSystemFontContainer(Display display) {
		ScaledFontContainer systemFontContainer = fontKeyMap.get(KEY_SYSTEM_FONTS);
		if (systemFontContainer == null) {
			int targetZoom = display.getPrimaryMonitor().getZoom();
			long systemFontHandle = createSystemFont(targetZoom);
			Font systemFont = Font.win32_new(display, systemFontHandle);
			systemFontContainer = new ScaledFontContainer(systemFont);
			fontHandleMap.put(systemFont.handle, systemFontContainer);
			fontKeyMap.put(KEY_SYSTEM_FONTS, systemFontContainer);
		}
		return systemFontContainer;
	}

	private long createSystemFont(int targetZoom) {
		long hFont = 0;
		NONCLIENTMETRICS info = new NONCLIENTMETRICS();
		info.cbSize = NONCLIENTMETRICS.sizeof;
		if (fetchSystemParametersInfo(info, targetZoom)) {
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
	public Font getFont(FontData fontData, int zoom) {
		ScaledFontContainer container;
		if (fontKeyMap.containsKey(fontData)) {
			container = fontKeyMap.get(fontData);
		} else {
			int calculatedZoom = computeZoom(fontData);
			Font newFont = Font.win32_new(display, fontData, calculatedZoom);
			container = new ScaledFontContainer(newFont);
			fontHandleMap.put(newFont.handle, container);
			fontKeyMap.put(fontData, container);
		}
		return getOrCreateFont(container, zoom);
	}

	@Override
	public void dispose() {
		for (Entry<FontData, ScaledFontContainer> fontContainerEntry : fontKeyMap.entrySet()) {
			if (KEY_SYSTEM_FONTS.equals(fontContainerEntry.getKey())) {
				// do not dispose the system fonts here, they are not tied to the display of this registry
				continue;
			}
			ScaledFontContainer scaledFontContainer = fontContainerEntry.getValue();
			for (Font font : scaledFontContainer.scaledFonts.values()) {
				font.dispose();
			}
		}
		fontKeyMap.clear();
	}

	private Font getOrCreateFont(ScaledFontContainer container, int zoom) {
		Font scaledFont = container.getScaledFont(zoom);
		if (scaledFont == null) {
			scaledFont = container.scaleFont(zoom);
			fontHandleMap.put(scaledFont.handle, container);
			fontKeyMap.put(scaledFont.getFontData()[0], container);
		}
		return scaledFont;
	}

	private int computeZoom(FontData fontData) {
		int pixelsAtPrimaryMonitorZoom = ((Device) display).computePixels(fontData.height);
		int value = display.getPrimaryMonitor().getZoom() * fontData.data.lfHeight / pixelsAtPrimaryMonitorZoom;
		return value;
	}

	private int computePixels(int zoom, FontData fontData) {
		int adjustedLogFontHeight = ((Device) display).computePixels(fontData.height);
		int primaryZoom = display.getPrimaryMonitor().getZoom();
		if (zoom != primaryZoom) {
			adjustedLogFontHeight *= (1f * zoom / primaryZoom);
		}
		return adjustedLogFontHeight;
	}
}
