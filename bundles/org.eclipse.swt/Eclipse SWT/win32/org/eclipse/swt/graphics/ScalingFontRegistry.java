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
			scaledFonts.put(baseFont.zoomFactor, baseFont);
		}

		private Font getScaledFont(int targetZoomFactor) {
			if (scaledFonts.containsKey(targetZoomFactor)) {
				Font font = scaledFonts.get(targetZoomFactor);
				if (font.isDisposed()) {
					scaledFonts.remove(targetZoomFactor);
					return null;
				}
				return font;
			}
			return null;
		}

		private Font scaleFont(int zoomFactor) {
			FontData fontData = baseFont.getFontData()[0];
			fontData.data.lfHeight = computePixels(zoomFactor, fontData);
			Font scaledFont = Font.win32_new(display, fontData, zoomFactor);
			addScaledFont(zoomFactor, scaledFont);
			return scaledFont;
		}

		private void addScaledFont(int targetZoomFactor, Font scaledFont) {
			scaledFonts.put(targetZoomFactor, scaledFont);
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
	public Font getSystemFont(int zoomFactor) {
		ScaledFontContainer container = getOrCreateBaseSystemFontContainer(display);

		Font systemFont = container.getScaledFont(zoomFactor);
		if (systemFont != null) {
			return systemFont;
		}
		long systemFontHandle = createSystemFont(display, zoomFactor);
		systemFont = Font.win32_new(display, systemFontHandle, zoomFactor);
		container.addScaledFont(zoomFactor, systemFont);
		return systemFont;
	}

	private ScaledFontContainer getOrCreateBaseSystemFontContainer(Display display) {
		ScaledFontContainer systemFontContainer = fontKeyMap.get(KEY_SYSTEM_FONTS);
		if (systemFontContainer == null) {
			int targetZoomFactor = display.getPrimaryMonitor().getZoom();
			long systemFontHandle = createSystemFont(display, targetZoomFactor);
			Font systemFont = Font.win32_new(display, systemFontHandle);
			systemFontContainer = new ScaledFontContainer(systemFont);
			fontHandleMap.put(systemFont.handle, systemFontContainer);
			fontKeyMap.put(KEY_SYSTEM_FONTS, systemFontContainer);
		}
		return systemFontContainer;
	}

	private long createSystemFont(Display display, int targetZoomFactor) {
		long hFont = 0;
		NONCLIENTMETRICS info = new NONCLIENTMETRICS();
		info.cbSize = NONCLIENTMETRICS.sizeof;
		if (fetchSystemParametersInfo(info, targetZoomFactor)) {
			LOGFONT logFont = info.lfMessageFont;
			hFont = OS.CreateFontIndirect(logFont);
		}
		if (hFont == 0)
			hFont = OS.GetStockObject(OS.DEFAULT_GUI_FONT);
		if (hFont == 0)
			hFont = OS.GetStockObject(OS.SYSTEM_FONT);
		return hFont;
	}

	private static boolean fetchSystemParametersInfo(NONCLIENTMETRICS info, int targetZoomFactor) {
		if (OS.WIN32_BUILD >= OS.WIN32_BUILD_WIN10_1607) {
			return OS.SystemParametersInfoForDpi(OS.SPI_GETNONCLIENTMETRICS, NONCLIENTMETRICS.sizeof, info, 0,
					DPIUtil.mapZoomToDPI(targetZoomFactor));
		} else {
			return OS.SystemParametersInfo(OS.SPI_GETNONCLIENTMETRICS, 0, info, 0);
		}
	}

	@Override
	public Font getFont(FontData fontData, int zoomFactor) {
		if (!DPIUtil.autoScaleOnRuntime) {
			return null;
		}
		ScaledFontContainer container;
		if (fontKeyMap.containsKey(fontData)) {
			container = fontKeyMap.get(fontData);
		} else {
			int calculatedZoomFactor = computeZoomFactor(fontData);
			Font newFont = Font.win32_new(display, fontData, calculatedZoomFactor);
			container = new ScaledFontContainer(newFont);
			fontHandleMap.put(newFont.handle, container);
			fontKeyMap.put(fontData, container);
		}
		return getOrCreateFont(container, zoomFactor);
	}

	@Override
	public void dispose() {

	}

	private Font getOrCreateFont(ScaledFontContainer container, int zoomFactor) {
		Font scaledFont = container.getScaledFont(zoomFactor);
		if (scaledFont == null) {
			scaledFont = container.scaleFont(zoomFactor);
			fontHandleMap.put(scaledFont.handle, container);
			fontKeyMap.put(scaledFont.getFontData()[0], container);
		}
		return scaledFont;
	}

	private int computeZoomFactor(FontData fontData) {
		long hDC = display.internal_new_GC(null);
		int pixelsAtPrimaryMonitorZoom = -(int) (0.5f + (fontData.height * OS.GetDeviceCaps(hDC, OS.LOGPIXELSY) / 72f));
		display.internal_dispose_GC(hDC, null);
		int value = display.getPrimaryMonitor().getZoom() * fontData.data.lfHeight / pixelsAtPrimaryMonitorZoom;
		return value;
	}

	private int computePixels(int zoomFactor, FontData fontData) {
		long hDC = display.internal_new_GC(null);
		int adjustedLogFontHeight = -(int) (0.5f + (fontData.height * OS.GetDeviceCaps(hDC, OS.LOGPIXELSY) / 72f));
		display.internal_dispose_GC(hDC, null);

		int primaryZoomFactor = display.getPrimaryMonitor().getZoom();
		if (zoomFactor != primaryZoomFactor) {
			adjustedLogFontHeight *= (1f * zoomFactor / primaryZoomFactor);
		}
		return adjustedLogFontHeight;
	}
}
