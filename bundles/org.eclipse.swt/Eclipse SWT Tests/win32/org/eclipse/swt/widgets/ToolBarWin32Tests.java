/*******************************************************************************
 * Copyright (c) 2026 Vector Informatik GmbH and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package org.eclipse.swt.widgets;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.*;
import java.util.function.*;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.layout.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;

/**
 * Windows-specific tests for {@link ToolBar}'s image handling across monitor
 * zoom changes.
 */
@ExtendWith(PlatformSpecificExecutionExtension.class)
class ToolBarWin32Tests {

	private static final int TIMEOUT_MILLIS = 5000;

	private record ToolItemWithExpectedColor(ToolItem toolItem, RGB expectedColor) {
	}

	/**
	 * Regression test for a tool bar item rendering the wrong or a blank icon after
	 * a monitor zoom (DPI) change, as reported in <a href=
	 * "https://github.com/eclipse-platform/eclipse.platform.swt/issues/3466">issue
	 * #3466</a>.
	 * <p>
	 * The bug is triggered by clearing one item's image (which leaves the image
	 * list in a state that a later zoom change mishandles); neither disposing an
	 * image nor multiple monitors are required. The test observes the actual
	 * rendered result via public API only: each item gets a distinctly colored
	 * icon, and after the zoom change the dominant color under each item must still
	 * match that item's own icon.
	 */
	@Test
	void testIconsRenderedCorrectlyAfterZoomChangeWithImageListHole() {
		Display display = new Display();
		RGB[] colors = { new RGB(220, 40, 40), new RGB(40, 180, 40), new RGB(40, 40, 220), new RGB(230, 200, 30) };
		Image[] icons = new Image[colors.length];
		for (int i = 0; i < colors.length; i++) {
			icons[i] = solidIcon(display, 16, colors[i]);
		}
		try {
			Shell shell = new Shell(display);
			shell.setLayout(new FillLayout());
			ToolBar bar = new ToolBar(shell, SWT.FLAT);
			ToolItem[] items = new ToolItem[colors.length];
			for (int i = 0; i < colors.length; i++) {
				items[i] = new ToolItem(bar, SWT.PUSH);
				items[i].setImage(icons[i]);
			}
			shell.setSize(500, 90);
			shell.open();

			// Punch a hole below the other items: clear the first item's image.
			items[0].setImage(null);

			int zoom = bar.getAutoscalingZoom();
			DPITestUtil.changeDPIZoom(bar.getShell(), zoom * 2);

			// Only the first item is expected to rendered blank
			Set<ToolItemWithExpectedColor> itemsToCheck = new HashSet<>();
			for (int i = 1; i < items.length; i++) {
				itemsToCheck.add(new ToolItemWithExpectedColor(items[i], colors[i]));
			}
			// Dispatch events until every item renders its own icon color, or fail on
			// timeout. With the bug an item permanently renders another item's icon or
			// a blank one, so the condition is never met and the timeout triggers.
			assertTrue(waitUntilIconsRenderOwnColor(display, () -> iconsRenderOwnColor(bar, itemsToCheck, colors),
					TIMEOUT_MILLIS), "every tool item must render its own icon color after a zoom change");
		} finally {
			for (Image icon : icons)
				icon.dispose();
			display.dispose();
		}
	}

	/**
	 * A tool bar addresses its normal, hot and disabled image list with a single
	 * index per item, and disposing an item frees its slot in those lists for
	 * reuse. The remaining items must keep rendering their own icon.
	 */
	@Test
	void testIconsRenderedCorrectlyAfterDisposingAnotherItem() {
		Display display = new Display();
		RGB[] colors = { new RGB(220, 40, 40), new RGB(40, 180, 40), new RGB(40, 40, 220) };
		Image[] icons = createIcons(display, colors);
		try {
			Shell shell = new Shell(display);
			shell.setLayout(new FillLayout());
			ToolBar bar = new ToolBar(shell, SWT.FLAT);
			ToolItem[] items = createItems(bar, icons);
			shell.setSize(500, 90);
			shell.open();

			items[1].dispose();

			Set<ToolItemWithExpectedColor> itemsToCheck = Set.of(
					new ToolItemWithExpectedColor(items[0], colors[0]),
					new ToolItemWithExpectedColor(items[2], colors[2]));
			assertTrue(waitUntilIconsRenderOwnColor(display, () -> iconsRenderOwnColor(bar, itemsToCheck, colors),
					TIMEOUT_MILLIS), "every tool item must render its own icon after another item is disposed");
		} finally {
			disposeAll(icons, display);
		}
	}

	/**
	 * Tool bars share the image lists for a given icon size, so the items of one
	 * tool bar must not be affected by those of another tool bar using icons of the
	 * same size.
	 */
	@Test
	void testIconsRenderedCorrectlyWithSecondToolBarUsingSameIconSize() {
		Display display = new Display();
		RGB[] colors = { new RGB(220, 40, 40), new RGB(40, 180, 40), new RGB(40, 40, 220), new RGB(230, 200, 30) };
		Image[] icons = createIcons(display, colors);
		try {
			Shell shell = new Shell(display);
			shell.setLayout(new FillLayout(SWT.VERTICAL));
			ToolBar firstBar = new ToolBar(shell, SWT.FLAT);
			ToolItem[] firstItems = createItems(firstBar, icons[0], icons[1]);
			ToolBar secondBar = new ToolBar(shell, SWT.FLAT);
			ToolItem[] secondItems = createItems(secondBar, icons[2], icons[3]);
			shell.setSize(500, 180);
			shell.open();

			Set<ToolItemWithExpectedColor> firstItemsToCheck = Set.of(
					new ToolItemWithExpectedColor(firstItems[0], colors[0]),
					new ToolItemWithExpectedColor(firstItems[1], colors[1]));
			Set<ToolItemWithExpectedColor> secondItemsToCheck = Set.of(
					new ToolItemWithExpectedColor(secondItems[0], colors[2]),
					new ToolItemWithExpectedColor(secondItems[1], colors[3]));
			assertTrue(
					waitUntilIconsRenderOwnColor(display,
							() -> iconsRenderOwnColor(firstBar, firstItemsToCheck, colors)
									&& iconsRenderOwnColor(secondBar, secondItemsToCheck, colors),
							TIMEOUT_MILLIS),
					"every tool item must render its own icon although both tool bars share the image lists");
		} finally {
			disposeAll(icons, display);
		}
	}

	/**
	 * Changing the orientation moves every item's images into image lists created
	 * for the new orientation, which must retain the assignment of items to their
	 * icons.
	 */
	@Test
	void testIconsRenderedCorrectlyAfterOrientationChange() {
		Display display = new Display();
		RGB[] colors = { new RGB(220, 40, 40), new RGB(40, 180, 40), new RGB(40, 40, 220) };
		Image[] icons = createIcons(display, colors);
		try {
			Shell shell = new Shell(display);
			shell.setLayout(new FillLayout());
			ToolBar bar = new ToolBar(shell, SWT.FLAT);
			ToolItem[] items = createItems(bar, icons);
			shell.setSize(500, 90);
			shell.open();

			bar.setOrientation(SWT.RIGHT_TO_LEFT);
			bar.setOrientation(SWT.LEFT_TO_RIGHT);

			Set<ToolItemWithExpectedColor> itemsToCheck = new HashSet<>();
			for (int i = 0; i < items.length; i++) {
				itemsToCheck.add(new ToolItemWithExpectedColor(items[i], colors[i]));
			}
			assertTrue(waitUntilIconsRenderOwnColor(display, () -> iconsRenderOwnColor(bar, itemsToCheck, colors),
					TIMEOUT_MILLIS), "every tool item must render its own icon after an orientation change");
		} finally {
			disposeAll(icons, display);
		}
	}

	private static Image[] createIcons(Display display, RGB[] colors) {
		Image[] icons = new Image[colors.length];
		for (int i = 0; i < colors.length; i++) {
			icons[i] = solidIcon(display, 16, colors[i]);
		}
		return icons;
	}

	private static ToolItem[] createItems(ToolBar bar, Image... icons) {
		ToolItem[] items = new ToolItem[icons.length];
		for (int i = 0; i < icons.length; i++) {
			items[i] = new ToolItem(bar, SWT.PUSH);
			items[i].setImage(icons[i]);
		}
		return items;
	}

	private static void disposeAll(Image[] icons, Display display) {
		for (Image icon : icons) {
			icon.dispose();
		}
		display.dispose();
	}

	private static boolean waitUntilIconsRenderOwnColor(Display display, BooleanSupplier condition,
			long timeoutMillis) {
		long deadline = System.currentTimeMillis() + timeoutMillis;
		while (System.currentTimeMillis() < deadline) {
			if (condition.getAsBoolean()) {
				return true;
			}
			if (!display.readAndDispatch()) {
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
					break;
				}
			}
		}
		return condition.getAsBoolean();
	}

	private static boolean iconsRenderOwnColor(ToolBar bar, Set<ToolItemWithExpectedColor> items, RGB[] candidateColors) {
		Image snapshot = renderToolBar(bar);
		try {
			for (ToolItemWithExpectedColor item : items) {
				if (!item.expectedColor().equals(dominantIconColor(snapshot, item.toolItem().getBounds(), candidateColors))) {
					return false;
				}
			}
			return true;
		} finally {
			snapshot.dispose();
		}
	}

	private static Image solidIcon(Display display, int size, RGB rgb) {
		Image image = new Image(display, size, size);
		GC gc = new GC(image);
		Color color = new Color(display, rgb);
		gc.setBackground(color);
		gc.fillRectangle(0, 0, size, size);
		gc.dispose();
		return image;
	}

	private static Image renderToolBar(ToolBar bar) {
		Point size = bar.getSize();
		Image snapshot = new Image(bar.getDisplay(), Math.max(1, size.x), Math.max(1, size.y));
		GC gc = new GC(snapshot);
		bar.print(gc);
		gc.dispose();
		return snapshot;
	}

	/**
	 * Returns which of the given candidate icon colors dominates the area of an
	 * item. Each sufficiently saturated pixel is classified to its nearest
	 * candidate color; the candidate matching the most pixels wins. Classifying to
	 * a fixed palette (rather than comparing exact RGB values) makes the result
	 * deterministic despite anti-aliasing, DPI interpolation and theming.
	 */
	private static RGB dominantIconColor(Image snapshot, Rectangle bounds, RGB[] candidates) {
		ImageData data = snapshot.getImageData();
		PaletteData palette = data.palette;
		int[] votes = new int[candidates.length];
		int x0 = Math.max(0, bounds.x), y0 = Math.max(0, bounds.y);
		int x1 = Math.min(data.width, bounds.x + bounds.width);
		int y1 = Math.min(data.height, bounds.y + bounds.height);
		for (int y = y0; y < y1; y++) {
			for (int x = x0; x < x1; x++) {
				RGB rgb = palette.getRGB(data.getPixel(x, y));
				int max = Math.max(rgb.red, Math.max(rgb.green, rgb.blue));
				int min = Math.min(rgb.red, Math.min(rgb.green, rgb.blue));
				if (max - min < 60) {
					continue; // ignore low-saturation background/borders
				}
				int best = -1, bestDist = Integer.MAX_VALUE;
				for (int c = 0; c < candidates.length; c++) {
					int dr = rgb.red - candidates[c].red;
					int dg = rgb.green - candidates[c].green;
					int db = rgb.blue - candidates[c].blue;
					int dist = dr * dr + dg * dg + db * db;
					if (dist < bestDist) {
						bestDist = dist;
						best = c;
					}
				}
				if (best >= 0)
					votes[best]++;
			}
		}
		int winner = -1, most = 0;
		for (int c = 0; c < candidates.length; c++) {
			if (votes[c] > most) {
				most = votes[c];
				winner = c;
			}
		}
		return winner < 0 ? null : candidates[winner];
	}
}
