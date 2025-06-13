/*******************************************************************************
 * Copyright (c) 2025 Syntevo GmbH and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Thomas Singer (Syntevo) - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.widgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;

import java.util.HashMap;
import java.util.Map;

/**
 * @noreference this is still experimental API and might be removed
 */
public final class DefaultColorProvider implements ColorProvider {

	public static DefaultColorProvider createLightInstance() {
		final float baseHue = 208;
		final Color selection = hsb(baseHue, 1, 0.72f);

		final Color black = gray(0);
		final Color gray64 = gray(64);
		final Color gray128 = gray(128);
		final Color gray160 = gray(160);
		final Color gray192 = gray(192);
		final Color gray204 = gray(204);
		final Color gray227 = gray(227);
		final Color gray240 = gray(240);
		final Color white = gray(255);

		final Color backgroundPanel = gray227;
		final Color backgroundInput = white;
		final Color foreground = black;
		final Color selectionBackground = hsb(baseHue, 0.2f, 1);
		final Color selectionForeground = foreground;

		final DefaultColorProvider p = new DefaultColorProvider();
		p.put(ControlRenderer.COLOR_BACKGROUND, backgroundPanel);
		p.put(ControlRenderer.COLOR_FOREGROUND, foreground);
		p.put(ControlRenderer.COLOR_DISABLED, gray160);

		p.put(ButtonRenderer.COLOR_HOVER, hsb(baseHue, 0.1f, 1));
		p.put(ButtonRenderer.COLOR_TOGGLE, hsb(baseHue, 0.2f, 0.96f));
		p.put(ButtonRenderer.COLOR_SELECTION, selection);
		p.put(ButtonRenderer.COLOR_OUTLINE, gray160);
		p.put(ButtonRenderer.COLOR_OUTLINE_DISABLED, gray192);
		p.put(ButtonRenderer.COLOR_BOX, gray128);
		p.put(ButtonRenderer.COLOR_BOX_DISABLED, gray160);
		p.put(ButtonRenderer.COLOR_GRAYED, gray192);

		p.put(DefaultButtonRenderer.COLOR_BACKGROUND, white);

		p.put(LabelRenderer.COLOR_SHADOW_IN1, gray160);
		p.put(LabelRenderer.COLOR_SHADOW_IN2, white);
		p.put(LabelRenderer.COLOR_SHADOW_OUT1, white);
		p.put(LabelRenderer.COLOR_SHADOW_OUT2, gray160);

		p.put(DefaultLinkRenderer.COLOR_LINK, hsb(baseHue, 1, 0.6f));

		p.put(DefaultListRenderer.COLOR_BACKGROUND, backgroundInput);
		p.put(DefaultListRenderer.COLOR_BORDER, gray160);
		p.put(DefaultListRenderer.COLOR_SELECTION_BACKGROUND, selectionBackground);
		p.put(DefaultListRenderer.COLOR_SELECTION_FOREGROUND, selectionForeground);

		p.put(DefaultScaleRenderer.COLOR_HANDLE_IDLE, selection);
		p.put(DefaultScaleRenderer.COLOR_HANDLE_HOVER, black);
		p.put(DefaultScaleRenderer.COLOR_HANDLE_DRAG, gray204);
		p.put(DefaultScaleRenderer.COLOR_HANDLE_OUTLINE, gray160);
		p.put(DefaultScaleRenderer.COLOR_NOTCH, gray160);

		return p;
	}

	public static DefaultColorProvider createDarkInstance() {
		final float baseHue = 208;
		final Color selection = hsb(baseHue, 0.5f, 0.5f);

		final Color black = gray(0);
		final Color gray30 = gray(30);
		final Color gray64 = gray(64);
		final Color gray80 = gray(80);
		final Color gray128 = gray(128);
		final Color gray160 = gray(160);
		final Color gray192 = gray(192);
		final Color gray204 = gray(204);

		final Color backgroundPanel = gray30;
		final Color backgroundInput = gray64;
		// don't use a white as foreground color because we want to keep white as a color for bold text
		// background: white normal text and white bold text look too similar
		final Color foreground = gray192;
		final Color selectionBackground = selection;
		final Color selectionForeground = gray192;

		final DefaultColorProvider p = new DefaultColorProvider();
		p.put(ControlRenderer.COLOR_BACKGROUND, backgroundPanel);
		p.put(ControlRenderer.COLOR_FOREGROUND, foreground);
		p.put(ControlRenderer.COLOR_DISABLED, gray160);

		p.put(ButtonRenderer.COLOR_HOVER, gray80);
		p.put(ButtonRenderer.COLOR_TOGGLE, gray128);
		p.put(ButtonRenderer.COLOR_SELECTION, selection);
		p.put(ButtonRenderer.COLOR_OUTLINE, gray128);
		p.put(ButtonRenderer.COLOR_OUTLINE_DISABLED, gray80);
		p.put(ButtonRenderer.COLOR_BOX, gray204);
		p.put(ButtonRenderer.COLOR_BOX_DISABLED, gray160);
		p.put(ButtonRenderer.COLOR_GRAYED, gray128);

		p.put(DefaultButtonRenderer.COLOR_BACKGROUND, gray64);

		p.put(LabelRenderer.COLOR_SHADOW_IN1, black);
		p.put(LabelRenderer.COLOR_SHADOW_IN2, gray80);
		p.put(LabelRenderer.COLOR_SHADOW_OUT1, gray80);
		p.put(LabelRenderer.COLOR_SHADOW_OUT2, black);

		p.put(DefaultLinkRenderer.COLOR_LINK, hsb(baseHue, 0.3f, 0.8f));

		p.put(DefaultListRenderer.COLOR_BACKGROUND, backgroundInput);
		p.put(DefaultListRenderer.COLOR_BORDER, gray80);
		p.put(DefaultListRenderer.COLOR_SELECTION_BACKGROUND, selectionBackground);
		p.put(DefaultListRenderer.COLOR_SELECTION_FOREGROUND, selectionForeground);

		p.put(DefaultScaleRenderer.COLOR_HANDLE_IDLE, selection);
		p.put(DefaultScaleRenderer.COLOR_HANDLE_HOVER, gray204);
		p.put(DefaultScaleRenderer.COLOR_HANDLE_DRAG, gray128);
		p.put(DefaultScaleRenderer.COLOR_HANDLE_OUTLINE, gray128);
		p.put(DefaultScaleRenderer.COLOR_NOTCH, gray128);

		return p;
	}

	private final Map<String, Color> map = new HashMap<>();

	private DefaultColorProvider() {
	}

	@Override
	public Color getColor(String key) {
		if (key == null) {
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		}
		final Color color = map.get(key);
		if (color == null) {
			SWT.error(SWT.ERROR_UNSPECIFIED);
		}
		return color;
	}

	public void put(String key, Color color) {
		map.put(key, color);
	}

	public static Color gray(int value) {
		return new Color(value, value, value);
	}

	public static Color hsb(float baseHue, float saturation, float brightness) {
		return new Color(new RGB(baseHue, saturation, brightness));
	}

	public static Color blend(Color color1, Color color2, float factor) {
		return new Color(blend(color1.getRed(), color2.getRed(), factor),
				blend(color1.getGreen(), color2.getGreen(), factor),
				blend(color1.getBlue(), color2.getBlue(), factor));
	}

	private static int blend(int value1, int value2, float factor) {
		int value = Math.round(value1 - (value1 - value2) * factor);
		return Math.max(0, Math.min(value, 255));
	}
}
