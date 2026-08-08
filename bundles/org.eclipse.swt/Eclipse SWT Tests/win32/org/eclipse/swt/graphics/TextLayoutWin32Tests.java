/*******************************************************************************
 * Copyright (c) 2024, 2026 Yatta Solutions and others
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.widgets.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

@ExtendWith(PlatformSpecificExecutionExtension.class)
@ExtendWith(WithMonitorSpecificScalingExtension.class)
class TextLayoutWin32Tests {
	final static String text = "This is a text for testing.";
	private static final String MONOSPACED_FONT = "Courier New";
	private static final int TAB_STOP_TOLERANCE_IN_POINTS = 2;

	@Test
	public void testGetBoundPublicAPIshouldReturnTheSameValueRegardlessOfZoomLevel() {
		Display display = Display.getDefault();

		final TextLayout layout = new TextLayout(display);
		GCData unscaledData = new GCData();
		unscaledData.nativeZoom = DPIUtil.getNativeDeviceZoom();
		GC gc = GC.win32_new(display, unscaledData);
		layout.draw(gc, 10, 10);
		Rectangle unscaledBounds = layout.getBounds();

		int scalingFactor = 2;
		int newZoom = DPIUtil.getNativeDeviceZoom() * scalingFactor;
		GCData scaledData = new GCData();
		scaledData.nativeZoom = newZoom;
		GC scaledGc = GC.win32_new(display, scaledData);
		layout.draw(scaledGc, 10, 10);
		Rectangle scaledBounds = layout.getBounds();

		assertEquals(unscaledBounds, scaledBounds, "The public API for getBounds should give the same result for any zoom level");
	}

	@Test
	public void testCalculateGetBoundsWithVerticalIndent() {
		Display display = Display.getDefault();
		Shell shell = new Shell(display);

		TextLayout layout = new TextLayout(display);
		layout.setVerticalIndent(16);
		layout.setText(text);
		Rectangle unscaledBounds = layout.getBounds();

		int scalingFactor = 2;
		int newZoom = DPIUtil.getNativeDeviceZoom() * scalingFactor;
		DPITestUtil.changeDPIZoom(shell, newZoom);
		TextLayout scaledLayout = new TextLayout(display);
		scaledLayout.setVerticalIndent(16);
		scaledLayout.setText(text);
		Rectangle scaledBounds = scaledLayout.getBounds();

		assertNotEquals(layout.nativeZoom, scaledLayout.nativeZoom, "The native zoom for the TextLayouts must differ");
		assertEquals(unscaledBounds.height, scaledBounds.height, 1, "The public API for getBounds with vertical indent > 0 should give a similar result for any zoom level");
	}

	@ParameterizedTest
	@ValueSource(ints = { 100, 125, 150, 175, 200 })
	public void testTabAfterSpacesReachesTheNextTabStop(int zoom) {
		Display display = Display.getDefault();
		assumeTrue(isFontInstalled(display, MONOSPACED_FONT), MONOSPACED_FONT + " is not installed");

		List<String> violations = new ArrayList<>();
		for (int fontHeight = 8; fontHeight <= 20; fontHeight++) {
			Font font = Font.win32_new(new Font(display, MONOSPACED_FONT, fontHeight, SWT.NORMAL), zoom);
			for (int tabLength : new int[] { 2, 3, 4, 8 }) {
				String spaces = " ".repeat(tabLength);
				// StyledText derives its single tab stop from the width of a run of spaces
				int tabWidth = boundsWidth(display, font, null, spaces);
				int spacesThenTab = boundsWidth(display, font, new int[] { tabWidth }, spaces + "\t");
				int twiceTheSpaces = boundsWidth(display, font, new int[] { tabWidth }, spaces + spaces);
				if (Math.abs(spacesThenTab - twiceTheSpaces) > TAB_STOP_TOLERANCE_IN_POINTS) {
					violations.add(fontHeight + "pt with tab length " + tabLength + ": width " + spacesThenTab
							+ " instead of " + twiceTheSpaces);
				}
			}
		}

		assertTrue(violations.isEmpty(), "A tab placed exactly on a tab stop must advance to the next one, but at zoom "
				+ zoom + "% it did not for " + violations);
	}

	private static int boundsWidth(Display display, Font font, int[] tabs, String content) {
		TextLayout layout = new TextLayout(display);
		try {
			layout.setFont(font);
			layout.setTabs(tabs);
			layout.setText(content);
			return layout.getBounds().width;
		} finally {
			layout.dispose();
		}
	}

	private static boolean isFontInstalled(Display display, String name) {
		for (FontData fontData : display.getFontList(null, true)) {
			if (name.equalsIgnoreCase(fontData.getName())) {
				return true;
			}
		}
		return false;
	}

}
