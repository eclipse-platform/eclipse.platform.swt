/*******************************************************************************
 * Copyright (c) 2024, 2026 Yatta Solutions
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

import java.util.*;
import java.util.stream.*;

import org.eclipse.swt.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.widgets.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

@ExtendWith(PlatformSpecificExecutionExtension.class)
@ExtendWith(WithMonitorSpecificScalingExtension.class)
class ImagesWin32Tests {

	@Test
	public void testImageIconTypeShouldNotChangeAfterCallingGetHandleForDifferentZoom() {
		Image icon = Display.getDefault().getSystemImage(SWT.ICON_ERROR);
		try {
			Image.win32_getHandle(icon, 200);
			assertEquals(SWT.ICON, icon.type, "Image type should stay to SWT.ICON");
		} finally {
			icon.dispose();
		}
	}

	/**
	 * For images with very different width and height (e.g. 500x2), independent
	 * integer division when computing the zoom from each axis's pixel hint can make the
	 * height-derived zoom larger than the width-derived zoom. The correct fix is to
	 * derive the zoom from the axis with the larger pixel hint, which has more pixels
	 * and therefore a proportionally smaller rounding error.
	 * <p>
	 * Verifies that for a wide (500-pixel) image at standard display zoom levels, the
	 * selected handle width matches the expected width at the requested zoom, across a
	 * range of image heights where integer-division rounding divergence is most extreme.
	 */
	@ParameterizedTest
	@MethodSource("provideZoomAndHeightCombinations")
	public void getImageHandle_asymmetricDimensions_selectsCorrectZoom(int displayZoom, int logicalHeight) {
		Display display = Display.getDefault();
		int logicalWidth = 500;
		PaletteData palette = new PaletteData(0xFF0000, 0xFF00, 0xFF);
		ImageDataProvider provider = zoom -> new ImageData(
				Math.max(1, Math.round(logicalWidth * zoom / 100f)),
				Math.max(1, Math.round(logicalHeight * zoom / 100f)),
				32, palette);
		Image image = new Image(display, provider);
		int widthHint = Math.round(logicalWidth * displayZoom / 100f);
		int heightHint = Math.round(logicalHeight * displayZoom / 100f);
		int[] capturedWidth = {-1};
		try {
			image.executeOnImageHandleAtBestFittingSize(h -> capturedWidth[0] = h.width(), widthHint, heightHint);
			assertEquals(widthHint, capturedWidth[0],
					"Handle width should match widthHint for a " + logicalWidth + "x" + logicalHeight
					+ " image at display zoom " + displayZoom + "%: expected " + widthHint
					+ " but got " + capturedWidth[0]);
		} finally {
			image.dispose();
		}
	}

	private static Stream<Arguments> provideZoomAndHeightCombinations() {
		int[] zooms = { 100, 125, 150, 175, 200 };
		int[] heights = IntStream.rangeClosed(1, 20).toArray();
		return Arrays.stream(zooms).boxed()
				.flatMap(zoom -> Arrays.stream(heights).mapToObj(height -> Arguments.of(zoom, height)));
	}
}
