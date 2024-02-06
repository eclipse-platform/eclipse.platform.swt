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
public class ScalingFontRegistry implements SWTFontRegistry {
	private class ScaledFontContainer {
		private Font baseFont;
		private Map<Integer, Font> scaledFonts = new HashMap<>();

		ScaledFontContainer(Font baseFont) {
			this.baseFont = baseFont;
			scaledFonts.put(baseFont.zoomLevel, baseFont);
		}

		private Font getScaledFont(int targetDeviceZoom) {
			if (scaledFonts.containsKey(targetDeviceZoom)) {
				Font font = scaledFonts.get(targetDeviceZoom);
				if (font.isDisposed()) {
					scaledFonts.remove(targetDeviceZoom);
					return null;
				}
				return font;
			}
			return null;
		}

		private Font scaleFont(int deviceZoom) {
			FontData fontData = baseFont.getFontData()[0];
			fontData.data.lfHeight = computePixels(deviceZoom, fontData);
			Font scaledFont = Font.win32_new(display, fontData, deviceZoom);
			addScaledFont(deviceZoom, scaledFont);
			return scaledFont;
		}

		private void addScaledFont(int targetDeviceZoom, Font scaledFont) {
			scaledFonts.put(targetDeviceZoom, scaledFont);
		}
	}

	private static FontData KEY_SYSTEM_FONTS = new FontData();
	private Map<Long, ScaledFontContainer> fontHandleMap = new HashMap<>();
	private Map<FontData, ScaledFontContainer> fontKeyMap = new HashMap<>();
	private Display display;

	public ScalingFontRegistry(Display display) {
		this.display = display;
	}

	@Override
	public Font getSystemFont(int deviceZoom) {
		ScaledFontContainer container = getOrCreateBaseSystemFontContainer(display);

		Font systemFont = container.getScaledFont(deviceZoom);
		if (systemFont != null) {
			return systemFont;
		}
		long systemFontHandle = createSystemFont(display, deviceZoom);
		systemFont = Font.win32_new(display, systemFontHandle, deviceZoom);
		container.addScaledFont(deviceZoom, systemFont);
		return systemFont;
	}

	private ScaledFontContainer getOrCreateBaseSystemFontContainer(Display display) {
		ScaledFontContainer systemFontContainer = fontKeyMap.get(KEY_SYSTEM_FONTS);
		if (systemFontContainer == null) {
			int targetDeviceZoom = display.getPrimaryMonitor().getZoom();
			long systemFontHandle = createSystemFont(display, targetDeviceZoom);
			Font systemFont = Font.win32_new(display, systemFontHandle);
			systemFontContainer = new ScaledFontContainer(systemFont);
			fontHandleMap.put(systemFont.handle, systemFontContainer);
			fontKeyMap.put(KEY_SYSTEM_FONTS, systemFontContainer);
		}
		return systemFontContainer;
	}

	private long createSystemFont(Display display, int targetDeviceZoom) {
		long hFont = 0;
		NONCLIENTMETRICS info = new NONCLIENTMETRICS();
		info.cbSize = NONCLIENTMETRICS.sizeof;
		if (fetchSystemParametersInfo(info, targetDeviceZoom)) {
			LOGFONT logFont = info.lfMessageFont;
			hFont = OS.CreateFontIndirect(logFont);
		}
		if (hFont == 0)
			hFont = OS.GetStockObject(OS.DEFAULT_GUI_FONT);
		if (hFont == 0)
			hFont = OS.GetStockObject(OS.SYSTEM_FONT);
		return hFont;
	}

	private static boolean fetchSystemParametersInfo(NONCLIENTMETRICS info, int targetDeviceZoom) {
		if (OS.WIN32_BUILD >= OS.WIN32_BUILD_WIN10_1607) {
			return OS.SystemParametersInfoForDpi(OS.SPI_GETNONCLIENTMETRICS, NONCLIENTMETRICS.sizeof, info, 0,
					DPIUtil.mapZoomToDPI(targetDeviceZoom));
		} else {
			return OS.SystemParametersInfo(OS.SPI_GETNONCLIENTMETRICS, 0, info, 0);
		}
	}

	@Override
	public Font getFont(FontData fontData, int deviceZoom) {
		if (!DPIUtil.autoScaleOnRuntime) {
			return null;
		}
		ScaledFontContainer container;
		if (fontKeyMap.containsKey(fontData)) {
			container = fontKeyMap.get(fontData);
		} else {
			int calculatedZoomLevel = computeZoomLevel(fontData);
			Font newFont = Font.win32_new(display, fontData, calculatedZoomLevel);
			container = new ScaledFontContainer(newFont);
			fontHandleMap.put(newFont.handle, container);
			fontKeyMap.put(fontData, container);
		}
		return getOrCreateFont(container, deviceZoom);
	}

	@Override
	public void dispose() {

	}

	private Font getOrCreateFont(ScaledFontContainer container, int deviceZoom) {
		Font scaledFont = container.getScaledFont(deviceZoom);
		if (scaledFont == null) {
			scaledFont = container.scaleFont(deviceZoom);
			fontHandleMap.put(scaledFont.handle, container);
			fontKeyMap.put(scaledFont.getFontData()[0], container);
		}
		return scaledFont;
	}

	private int computeZoomLevel(FontData fontData) {
		long hDC = display.internal_new_GC(null);
		int pixelsAtPrimaryMonitorZoom = -(int) (0.5f + (fontData.height * OS.GetDeviceCaps(hDC, OS.LOGPIXELSY) / 72f));
		display.internal_dispose_GC(hDC, null);
		int value = display.getPrimaryMonitor().getZoom() * fontData.data.lfHeight / pixelsAtPrimaryMonitorZoom;
		return value;
	}

	private int computePixels(int deviceZoom, FontData fontData) {
		long hDC = display.internal_new_GC(null);
		int adjustedLogFontHeight = -(int) (0.5f + (fontData.height * OS.GetDeviceCaps(hDC, OS.LOGPIXELSY) / 72f));
		display.internal_dispose_GC(hDC, null);

		int primaryZoom = display.getPrimaryMonitor().getZoom();
		if (deviceZoom != primaryZoom) {
			adjustedLogFontHeight *= (1f * deviceZoom / primaryZoom);
		}
		return adjustedLogFontHeight;
	}
}
