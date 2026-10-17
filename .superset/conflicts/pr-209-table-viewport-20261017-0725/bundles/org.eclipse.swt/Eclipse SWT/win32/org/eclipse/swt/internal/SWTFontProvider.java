/*******************************************************************************
 * Copyright (c) 2024 Yatta Solutions and others.
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
import java.util.concurrent.*;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

/**
 * This internal class is used to provide and cache fonts scaled for different zoom levels in the win32
 * implementation. Depending on the configuration of the SWT application, either a default behavior or
 * the scaling behavior is used. The default behavior mimics the existing behavior that fonts are scaled
 * to the zoom of the primary monitor and are not updated on runtime. The scaling behavior will always
 * take the provided values for the zoom into consideration and return scaled variant of a font if necessary.
 */
public class SWTFontProvider {
	private static final Map<Device, SWTFontRegistry> fontRegistries = new ConcurrentHashMap<>();

	private static SWTFontRegistry getFontRegistry(Device device) {
		return fontRegistries.computeIfAbsent(device, SWTFontProvider::newFontRegistry);
	}

	/**
	 * Returns the system font for the given device at the specified zoom.
	 *
	 * <b>Note:</b> This operation is not thread-safe. It must thus always be called
	 * from the same thread for the same device, such as the display's UI thread.
	 *
	 * @param device the device to retrieve the font for, must not be {@code null}
	 * @param zoom   the zoom for which the font shall be scaled
	 */
	public static Font getSystemFont(Device device, int zoom) {
		return getFontRegistry(device).getSystemFont(zoom);
	}

	public static long getSystemFontHandle(Device device, int zoom) {
		return Font.win32_getHandle(getSystemFont(device, zoom));
	}

	/**
	 * Returns the font with the given font data for the given device at the
	 * specified zoom.
	 *
	 * <b>Note:</b> This operation is not thread-safe. It must thus always be called
	 * from the same thread for the same device, such as the display's UI thread.
	 *
	 * @param device   the device to retrieve the font for, must not be {@code null}
	 * @param fontData the data for the font to retrieve, must not be {@code null}
	 * @param zoom     the zoom for which the font shall be scaled
	 */
	public static Font getFont(Device device, FontData fontData, int zoom) {
		return getFontRegistry(device).getFont(fontData, zoom);
	}

	public static long getFontHandle(Device device, FontData fontData, int zoom) {
		return Font.win32_getHandle(getFont(device, fontData, zoom));
	}

	public static long getFontHandle(Font font, int zoom) {
		if (font == null) {
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		}
		if (font.isDisposed()) {
			return 0;
		}
		return Font.win32_getHandle(getFont(font.getDevice(), font.getFontData()[0], zoom));
	}

	/**
	 * Returns the font with the given fontHandle for the given device at the
	 * specified zoom.
	 *
	 * <b>Note:</b> This operation is not thread-safe. It must thus always be called
	 * from the same thread for the same device, such as the display's UI thread.
	 *
	 * @param device     the device to retrieve the font for, must not be {@code null}
	 * @param fontHandle the handle to an existing font
	 * @param zoom       the zoom for which the font shall be scaled
	 */
	public static Font getFont(Device device, long fontHandle, int zoom) {
		return getFontRegistry(device).getFont(fontHandle, zoom);
	}

	/**
	 * Disposes the font registry for the given device, if one exists.
	 *
	 * @param device the device to dispose the font registry for, must not be
	 *               {@code null}
	 */
	public static void disposeFontRegistry(Device device) {
		SWTFontRegistry fontRegistry = fontRegistries.remove(device);
		if (fontRegistry != null) {
			fontRegistry.dispose();
		}
	}

	private static final String SWT_FONT_REGISTRY = "swt.fontRegistry";

	@SuppressWarnings("removal")
	private static SWTFontRegistry newFontRegistry(Device device) {
		if ("legacy".equalsIgnoreCase(System.getProperty(SWT_FONT_REGISTRY)) && device instanceof Display display
				&& !display.isRescalingAtRuntime()) {
			return new LegacySWTFontRegistry(device);
		}
		return new ScalingSWTFontRegistry(device);

	}
}
