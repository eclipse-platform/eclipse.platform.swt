/*******************************************************************************
 * Copyright (c) 2019 Syntevo and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Syntevo - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tests.manual;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;

import java.util.HashMap;
import java.util.Map;

public class Bug548286_DumpSystemColors {
	private static void dumpSystemColors(Display display) {
		// Copied from org.eclipse.swt.examples.controlexample.ColorTab
		HashMap<Integer, String> hmap = new HashMap<>();
		hmap.put(SWT.COLOR_WHITE, "COLOR_WHITE");
		hmap.put(SWT.COLOR_BLACK, "COLOR_BLACK");
		hmap.put(SWT.COLOR_RED, "COLOR_RED");
		hmap.put(SWT.COLOR_DARK_RED, "COLOR_DARK_RED");
		hmap.put(SWT.COLOR_GREEN, "COLOR_GREEN");
		hmap.put(SWT.COLOR_DARK_GREEN, "COLOR_DARK_GREEN");
		hmap.put(SWT.COLOR_YELLOW, "COLOR_YELLOW");
		hmap.put(SWT.COLOR_DARK_YELLOW, "COLOR_DARK_YELLOW");
		hmap.put(SWT.COLOR_WIDGET_DARK_SHADOW, "COLOR_WIDGET_DARK_SHADOW");
		hmap.put(SWT.COLOR_WIDGET_NORMAL_SHADOW, "COLOR_WIDGET_NORMAL_SHADOW");
		hmap.put(SWT.COLOR_WIDGET_LIGHT_SHADOW, "COLOR_WIDGET_LIGHT_SHADOW");
		hmap.put(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW, "COLOR_WIDGET_HIGHLIGHT_SHADOW");
		hmap.put(SWT.COLOR_WIDGET_FOREGROUND, "COLOR_WIDGET_FOREGROUND");
		hmap.put(SWT.COLOR_WIDGET_BACKGROUND, "COLOR_WIDGET_BACKGROUND");
		hmap.put(SWT.COLOR_WIDGET_BORDER, "COLOR_WIDGET_BORDER");
		hmap.put(SWT.COLOR_LIST_FOREGROUND, "COLOR_LIST_FOREGROUND");
		hmap.put(SWT.COLOR_LIST_BACKGROUND, "COLOR_LIST_BACKGROUND");
		hmap.put(SWT.COLOR_LIST_SELECTION, "COLOR_LIST_SELECTION");
		hmap.put(SWT.COLOR_LIST_SELECTION_TEXT, "COLOR_LIST_SELECTION_TEXT");
		hmap.put(SWT.COLOR_INFO_FOREGROUND, "COLOR_INFO_FOREGROUND");
		hmap.put(SWT.COLOR_INFO_BACKGROUND, "COLOR_INFO_BACKGROUND");
		hmap.put(SWT.COLOR_TITLE_FOREGROUND, "COLOR_TITLE_FOREGROUND");
		hmap.put(SWT.COLOR_TITLE_BACKGROUND, "COLOR_TITLE_BACKGROUND");
		hmap.put(SWT.COLOR_TITLE_BACKGROUND_GRADIENT, "COLOR_TITLE_BACKGROUND_GRADIENT");
		hmap.put(SWT.COLOR_TITLE_INACTIVE_FOREGROUND, "COLOR_TITLE_INACTIVE_FOREGROUND");
		hmap.put(SWT.COLOR_TITLE_INACTIVE_BACKGROUND, "COLOR_TITLE_INACTIVE_BACKGROUND");
		hmap.put(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT, "COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT");
		hmap.put(SWT.COLOR_LINK_FOREGROUND, "COLOR_LINK_FOREGROUND");
		hmap.put(SWT.COLOR_WIDGET_DISABLED_FOREGROUND, "COLOR_WIDGET_DISABLED_FOREGROUND");
		hmap.put(SWT.COLOR_TEXT_DISABLED_BACKGROUND, "COLOR_TEXT_DISABLED_BACKGROUND");

		// Printing IDs instead of names gives more compact output
		boolean printIDs = true;

		for (Map.Entry<Integer, String> entry : hmap.entrySet()) {
			Integer id = entry.getKey();
			String name = entry.getValue();
			Color color = display.getSystemColor(id);

			if (printIDs)
				System.out.format("%02d - ARGB:%02X%02X%02X%02X%n", id, color.getAlpha(), color.getRed(), color.getGreen(), color.getBlue());
			else
				System.out.format("%40s - ARGB:%02X%02X%02X%02X%n", name, color.getAlpha(), color.getRed(), color.getGreen(), color.getBlue());
		}
	}

	public static void main(String[] args) {
		long timeBeg = System.nanoTime();
		Display display = new Display();
		long timeEnd = System.nanoTime();
		System.out.format("Display(): %.2f sec%n", (float)(timeEnd - timeBeg) / 1000000000);

		dumpSystemColors(display);
	}
}