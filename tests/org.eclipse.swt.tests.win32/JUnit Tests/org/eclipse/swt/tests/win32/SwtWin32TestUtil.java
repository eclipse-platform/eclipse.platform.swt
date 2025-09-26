/*******************************************************************************
 * Copyright (c) 2019 Paul Pazderski and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Paul Pazderski - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tests.win32;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.function.BooleanSupplier;

import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.widgets.Display;

public class SwtWin32TestUtil {

/**
 * Dispatch events until <em>condition</em> is <code>true</code> or timeout reached.
 *
 * @param display display to dispatch events for (not <code>null</code>)
 * @param timeoutMs max time in milliseconds to process events
 * @param condition optional condition. If the condition returns <code>true</code> event processing is stopped.
 */
public static void processEvents(Display display, int timeoutMs, BooleanSupplier condition) throws InterruptedException {
	if (condition == null) {
		condition = () -> false;
	}
	long targetTimestamp = System.currentTimeMillis() + timeoutMs;
	while (!condition.getAsBoolean()) {
		if (!display.readAndDispatch()) {
			if (System.currentTimeMillis() < targetTimestamp) {
				Thread.sleep(50);
			} else {
				return;
			}
		}
	}
}

/**
 * Asserts that both given ImageData are equal, i.e. that:
 * <ul>
 *   <li>depths are equal (considering 24/32 bit as equals since alpha data is stored separately)</li>
 *   <li>width and height are equal</li>
 *   <li>all pixel RGB values are equal</li>
 *   <li>all pixel alpha values in the alphaData are equal</li>
 * </ul>
 * In case any of these properties differ, the test will fail.
 *
 * @param expected the expected ImageData
 * @param actual the actual ImageData
 */
// This method is necessary because ImageData has no custom equals method and the default one isn't sufficient.
public static void assertImageDataEqualsIgnoringAlphaInData(final ImageData expected, final ImageData actual) {
	assertNotNull(expected, "expected data must not be null");
	assertNotNull(actual, "actual data must not be null");
	if (expected == actual) {
		return;
	}
	assertEquals(expected.height, actual.height, "height of expected image is different from actual image");
	// Alpha values are taken from alpha data, so ignore whether data depth is 24 or 32 bits
	int expectedNormalizedDepth = expected.depth == 32 ? 24 : expected.depth;
	int actualNormalizedDepth = expected.depth == 32 ? 24 : expected.depth;
	assertEquals(expectedNormalizedDepth, actualNormalizedDepth, "depth of image data to compare must be equal");
	assertEquals(expected.width, actual.width, "width of expected image is different from actual image");

	for (int y = 0; y < expected.height; y++) {
		for (int x = 0; x < expected.width; x++) {
			// FIXME win32: dragged ALPHA=FF, dropped ALPHA=00, but other transparencyType
			// => alpha stored in ImageData.alphaData
			String expectedPixel = String.format("0x%08X", expected.getPixel(x, y) >> (expected.depth == 32 ? 8 : 0));
			String actualPixel = String.format("0x%08X", actual.getPixel(x, y) >> (actual.depth == 32 ? 8 : 0));
			assertEquals(expectedPixel, actualPixel, "actual pixel at x=" + x + " y=" + y + " is different from expected pixel");
			int expectedAlpha = expected.getAlpha(x, y);
			int actualAlpha = actual.getAlpha(x, y);
			assertEquals(expectedAlpha, actualAlpha, "actual pixel alpha at x=" + x + " y=" + y + " is different from expected pixel");
		}
	}
}
}
