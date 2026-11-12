/*******************************************************************************
 * Copyright (c) 2026 vogella GmbH and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Lars Vogel - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.widgets;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.internal.DPIUtil;
import org.eclipse.swt.internal.PlatformSpecificExecutionExtension;
import org.eclipse.swt.internal.WithMonitorSpecificScalingExtension;
import org.eclipse.swt.internal.win32.OS;
import org.eclipse.swt.internal.win32.RECT;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Checks the pixels a {@link TabFolder} paints once it has custom colors. The
 * shell stays unopened and messages are sent directly, so no window appears and
 * no real input is needed.
 */
@ExtendWith(PlatformSpecificExecutionExtension.class)
class TabFolderWin32Tests {

	private static final int DARK = 0x2F2F2F;
	private static final int LIGHT = 0xDCDCDC;
	/** Cannot result from blending a gray theme color, so only a label can paint it. */
	private static final int SATURATED = 0x0000FF;
	private static final int OTHER_SATURATED = 0x00FF00;
	private static final int ID_UPDOWN = 1;
	private static final int LAST_TAB = 3;
	private static final int IMAGE_SIZE = 16;

	private Display display;
	private Shell shell;

	@BeforeEach
	public void setUp() {
		display = Display.getDefault();
		shell = new Shell(display);
	}

	@AfterEach
	public void tearDown() {
		shell.dispose();
		display.dispose();
	}

	@Test
	public void test_colorsPaintStripPaneAndSelectedTab() {
		TabFolder folder = createFolder(SWT.NONE);
		setColors(folder);
		Pixels pixels = Pixels.ofClient(folder);
		RECT selected = tabRect(folder, 0);
		RECT client = clientRect(folder);

		assertEquals(DARK, pixels.get(client.right - 5, selected.top + 2), "strip");
		assertEquals(DARK, pixels.get(client.right / 2, client.bottom / 2), "pane");
		assertEquals(DARK, pixels.get(selected.left + 2, selected.top + 2), "selected tab");
		assertTrue(pixels.contains(LIGHT, tabRect(folder, 1)), "label of an unselected tab");
		assertTrue(pixels.contains(LIGHT, selected), "label of the selected tab");
	}

	@Test
	public void test_paintMessageDrawsLikePrintClient() {
		TabFolder folder = createFolder(SWT.NONE);
		setColors(folder);
		RECT client = clientRect(folder);

		Pixels painted = Pixels.paint(folder, OS.WM_PAINT, 0, client.right, client.bottom);
		assertArrayEquals(Pixels.ofClient(folder).values, painted.values);
	}

	@Test
	public void test_disabledFolderDimsLabels() {
		TabFolder folder = createFolder(SWT.NONE);
		setColors(folder);
		assertTrue(Pixels.ofClient(folder).contains(LIGHT, tabRect(folder, 1)), "enabled label");

		folder.setEnabled(false);
		assertFalse(Pixels.ofClient(folder).contains(LIGHT, tabRect(folder, 1)), "disabled label");
	}

	@Test
	public void test_withoutColorsPaintingStaysNative() {
		TabFolder folder = createFolder(SWT.NONE);
		int[] nativePixels = Pixels.ofClient(folder).values;

		setColors(folder);
		assertFalse(Arrays.equals(nativePixels, Pixels.ofClient(folder).values), "colors must change the painting");

		folder.setBackground(null);
		folder.setForeground(null);
		assertArrayEquals(nativePixels, Pixels.ofClient(folder).values, "clearing the colors must restore native painting");
	}

	@Test
	public void test_foregroundAloneSwitchesToCustomPainting() {
		TabFolder folder = createFolder(SWT.NONE);
		folder.setForeground(color(SATURATED));

		assertTrue(Pixels.ofClient(folder).contains(SATURATED, tabRect(folder, 1)));
	}

	@Test
	public void test_backgroundAloneKeepsLabelsReadable() {
		TabFolder folder = createFolder(SWT.NONE);

		folder.setBackground(color(DARK));
		assertTrue(Pixels.ofClient(folder).contains(0xFFFFFF, tabRect(folder, 1)), "label on a dark background");

		folder.setBackground(color(LIGHT));
		assertTrue(Pixels.ofClient(folder).contains(0x000000, tabRect(folder, 1)), "label on a light background");
	}

	@Test
	public void test_hoverFollowsTabsMovingBelowPointer() {
		TabFolder folder = createFolder(SWT.NONE);
		setColors(folder);
		RECT hovered = tabRect(folder, 2);
		int x = hovered.left + 3;
		int y = hovered.top + 3;
		OS.SendMessage(folder.handle, OS.WM_MOUSEMOVE, 0, OS.MAKELPARAM(x, y));
		int hotPixel = Pixels.ofClient(folder).get(x, y);

		folder.getItem(1).setText("EEEEEEEEEEEEEEEEEEEE");
		RECT moved = tabRect(folder, 2);
		Pixels pixels = Pixels.ofClient(folder);
		assertEquals(hotPixel, pixels.get(x, y), "tab now below the pointer");
		assertNotEquals(hotPixel, pixels.get(moved.left + 3, moved.top + 3), "tab that moved away");
	}

	@Test
	public void test_getItemTakesPointsWhenScaled() {
		DPIUtil.runWithAutoScaleValue("200", () -> {
			TabFolder folder = createFolder(SWT.NONE);
			TabItem item = folder.getItem(2);
			Rectangle bounds = item.getBounds();

			assertEquals(item, folder.getItem(new Point(bounds.x + bounds.width / 2, bounds.y + bounds.height / 2)));
		});
	}

	@Test
	public void test_removingItemClearsHover() {
		TabFolder folder = createFolder(SWT.NONE);
		setColors(folder);
		RECT hovered = tabRect(folder, 1);
		OS.SendMessage(folder.handle, OS.WM_MOUSEMOVE, 0, OS.MAKELPARAM(hovered.left + 3, hovered.top + 3));

		folder.getItem(0).dispose();
		int[] afterRemoval = Pixels.ofClient(folder).values;

		OS.SendMessage(folder.handle, OS.WM_MOUSELEAVE, 0, 0);
		assertArrayEquals(Pixels.ofClient(folder).values, afterRemoval, "the hover must not move to another tab");
	}

	@Test
	public void test_onlySelectedTabCoversPaneFrame() {
		assertOnlySelectedTabCoversPaneFrame(SWT.TOP, 1);
	}

	@Test
	public void test_onlySelectedTabCoversPaneFrameWithTabsAtBottom() {
		assertOnlySelectedTabCoversPaneFrame(SWT.BOTTOM, -1);
	}

	private void assertOnlySelectedTabCoversPaneFrame(int style, int scanDirection) {
		TabFolder folder = createFolder(style);
		setColors(folder);
		Pixels pixels = Pixels.ofClient(folder);
		RECT client = clientRect(folder);
		RECT selected = tabRect(folder, 0);
		RECT unselected = tabRect(folder, 1);

		int freeX = client.right - 5;
		int frameY = scanDirection > 0 ? selected.top : selected.bottom - 1;
		while (pixels.get(freeX, frameY) == DARK) {
			frameY += scanDirection;
		}
		int framePixel = pixels.get(freeX, frameY);

		assertEquals(framePixel, pixels.get(unselected.left + 5, frameY), "frame next to an unselected tab");
		assertEquals(DARK, pixels.get(selected.left + 5, frameY), "selected tab merges with the pane");
	}

	@Test
	public void test_imagesHaveTheSizeOfTheFirstImage() {
		Image small = createImage(16);
		Image large = createImage(32);
		try {
			TabFolder folder = createFolder(SWT.NONE);
			setColors(folder);
			folder.getItem(0).setImage(small);
			folder.getItem(1).setImage(large);
			Pixels pixels = Pixels.ofClient(folder);
			RECT tab = tabRect(folder, 1);

			assertTrue(pixels.contains(SATURATED, tab), "image inside its tab");
			for (int y = 0; y < pixels.height; y++) {
				for (int x = 0; x < pixels.width; x++) {
					boolean inTabs = y < tab.bottom && x < tabRect(folder, 2).left;
					if (!inTabs) {
						assertNotEquals(SATURATED, pixels.get(x, y), "image outside the tabs at " + x + "," + y);
					}
				}
			}
		} finally {
			small.dispose();
			large.dispose();
		}
	}

	@Test
	public void test_disposedImageDoesNotBreakPainting() {
		TabFolder folder = createFolder(SWT.NONE);
		setColors(folder);
		Image image = createImage(16);
		folder.getItem(1).setImage(image);
		image.dispose();

		assertTrue(Pixels.ofClient(folder).contains(LIGHT, tabRect(folder, 1)), "label of the tab");
	}

	@Test
	public void test_hoverHighlightsOnlyTabBelowPointer() {
		TabFolder folder = createFolder(SWT.NONE);
		setColors(folder);
		RECT hovered = tabRect(folder, 1);
		RECT other = tabRect(folder, 2);
		Pixels normal = Pixels.ofClient(folder);

		OS.SendMessage(folder.handle, OS.WM_MOUSEMOVE, 0, OS.MAKELPARAM(hovered.left + 3, hovered.top + 3));
		Pixels hot = Pixels.ofClient(folder);
		assertNotEquals(normal.get(hovered.left + 3, hovered.top + 3), hot.get(hovered.left + 3, hovered.top + 3), "hovered tab");
		assertEquals(normal.get(other.left + 3, other.top + 3), hot.get(other.left + 3, other.top + 3), "other tab");

		OS.SendMessage(folder.handle, OS.WM_MOUSELEAVE, 0, 0);
		assertArrayEquals(normal.values, Pixels.ofClient(folder).values, "leaving must clear the highlight");
	}

	@Test
	public void test_explicitTextDirectionReordersLabel() {
		assertTextDirectionReordersLabel("EEE!", SWT.RIGHT_TO_LEFT);
	}

	@Test
	public void test_autoTextDirectionReordersLabel() {
		assertTextDirectionReordersLabel("\u05D0\u05D1\u05D2 EEE!", SWT.AUTO_TEXT_DIRECTION);
	}

	private void assertTextDirectionReordersLabel(String label, int textDirection) {
		TabFolder folder = createFolder(SWT.NONE);
		setColors(folder);
		folder.getItem(1).setText(label);
		int[] leftToRight = Pixels.ofClient(folder).values;

		folder.setTextDirection(textDirection);
		assertFalse(Arrays.equals(leftToRight, Pixels.ofClient(folder).values), "the label must follow the text direction");
	}

	@Test
	public void test_childrenBlendIntoPane() {
		TabFolder folder = createFolder(SWT.NONE);
		setColors(folder);
		Control page = folder.getItem(0).getControl();

		assertEquals(DARK, Pixels.ofClient(page).get(1, 1));
	}

	@Test
	public void test_borderUsesColors() {
		TabFolder folder = createFolder(SWT.BORDER);
		setColors(folder);
		Pixels pixels = Pixels.ofWindow(folder);
		int edge = OS.GetSystemMetrics(OS.SM_CXEDGE);
		int middleY = pixels.height / 2;
		int frame = pixels.get(0, middleY);

		assertTrue(isDark(frame), "outer frame line");
		assertEquals(frame, pixels.get(pixels.width - 1, middleY), "right frame line");
		assertEquals(frame, pixels.get(pixels.width / 2, pixels.height - 1), "bottom frame line");
		for (int x = 1; x < edge; x++) {
			assertEquals(DARK, pixels.get(x, middleY), "inside of the frame");
		}
	}

	@Test
	public void test_foregroundAloneUsesInheritedBackground() {
		Composite parent = new Composite(shell, SWT.NONE);
		parent.setBackground(color(DARK));
		parent.setBackgroundMode(SWT.INHERIT_FORCE);
		TabFolder folder = createFolder(parent, SWT.NONE);
		folder.setForeground(color(LIGHT));
		RECT client = clientRect(folder);
		RECT selected = tabRect(folder, 0);

		Pixels pixels = Pixels.ofClient(folder);
		assertEquals(DARK, pixels.get(client.right - 5, selected.top + 2), "strip");
		assertEquals(DARK, pixels.get(client.right / 2, client.bottom / 2), "pane");
		assertEquals(DARK, pixels.get(selected.left + 2, selected.top + 2), "selected tab");

		parent.setBackground(color(LIGHT));
		assertEquals(LIGHT, Pixels.ofClient(folder).get(client.right / 2, client.bottom / 2), "pane follows the parent");
	}

	@Test
	public void test_ownBackgroundWinsOverInheritedBackground() {
		Composite parent = new Composite(shell, SWT.NONE);
		parent.setBackground(color(LIGHT));
		parent.setBackgroundMode(SWT.INHERIT_FORCE);
		TabFolder folder = createFolder(parent, SWT.NONE);
		setColors(folder);
		RECT client = clientRect(folder);

		assertEquals(DARK, Pixels.ofClient(folder).get(client.right / 2, client.bottom / 2));
	}

	@Test
	public void test_rightToLeftPaintsMirrored() {
		TabFolder folder = createFolder(SWT.RIGHT_TO_LEFT);
		setColors(folder);
		Pixels pixels = Pixels.ofMirroredClient(folder);
		RECT client = clientRect(folder);
		RECT selected = mirrored(tabRect(folder, 0), client);
		RECT unselected = mirrored(tabRect(folder, 1), client);

		assertTrue(selected.left > unselected.left, "first tab on the right");
		assertEquals(DARK, pixels.get(selected.right - 3, selected.top + 2), "selected tab");
		assertEquals(DARK, pixels.get(5, selected.top + 2), "strip left of the tabs");
		assertTrue(pixels.contains(LIGHT, selected), "label of the selected tab");
		assertTrue(pixels.contains(LIGHT, unselected), "label of an unselected tab");
	}

	@Test
	public void test_rightToLeftKeepsImageOrientation() {
		TabFolder folder = createFolder(SWT.RIGHT_TO_LEFT);
		setColors(folder);
		Image image = new Image(display, 16, 16);
		GC gc = new GC(image);
		gc.setBackground(color(SATURATED));
		gc.fillRectangle(0, 0, 8, 16);
		gc.setBackground(color(OTHER_SATURATED));
		gc.fillRectangle(8, 0, 8, 16);
		gc.dispose();
		try {
			folder.getItem(1).setText("");
			folder.getItem(1).setImage(image);
			Pixels pixels = Pixels.ofMirroredClient(folder);
			RECT tab = mirrored(tabRect(folder, 1), clientRect(folder));

			int first = pixels.firstColumnWith(SATURATED, tab);
			int second = pixels.firstColumnWith(OTHER_SATURATED, tab);
			assertTrue(first != -1 && second != -1, "image inside its mirrored tab");
			assertTrue(first < second, "the image must not be flipped");
		} finally {
			image.dispose();
		}
	}

	@Test
	public void test_overflowingFolderPaintsSelectedTabAfterScrolling() {
		TabFolder folder = createOverflowingFolder();
		setColors(folder);
		assertNotEquals(0, OS.GetDlgItem(folder.handle, ID_UPDOWN), "scroll arrows");

		folder.setSelection(LAST_TAB);
		RECT selected = tabRect(folder, LAST_TAB);
		assertTrue(selected.left < clientRect(folder).right, "selected tab scrolled into view");
		assertTrue(tabRect(folder, 0).left < 0, "first tab scrolled out");
		Pixels pixels = Pixels.ofClient(folder);

		assertEquals(DARK, pixels.get(selected.left + 2, selected.top + 2), "selected tab");
		assertTrue(pixels.contains(LIGHT, selected), "label of the selected tab");
	}

	@Test
	public void test_overflowingFolderHoversScrolledTab() {
		TabFolder folder = createOverflowingFolder();
		setColors(folder);
		// The native control scrolls the selected tab to the left edge
		folder.setSelection(1);
		assertTrue(tabRect(folder, 0).left < 0, "first tab scrolled out");
		RECT hovered = tabRect(folder, 2);
		int x = hovered.left + 3;
		int y = hovered.top + 3;
		int normal = Pixels.ofClient(folder).get(x, y);

		OS.SendMessage(folder.handle, OS.WM_MOUSEMOVE, 0, OS.MAKELPARAM(x, y));
		assertNotEquals(normal, Pixels.ofClient(folder).get(x, y), "tab below the pointer after scrolling");
	}

	@Test
	public void test_overflowingFolderKeepsLabelsInStrip() {
		TabFolder folder = createOverflowingFolder();
		setColors(folder);
		folder.setSelection(LAST_TAB);
		RECT client = clientRect(folder);
		RECT pane = new RECT();
		OS.SetRect(pane, client.left, tabRect(folder, LAST_TAB).bottom + 2, client.right, client.bottom);

		assertFalse(Pixels.ofClient(folder).contains(LIGHT, pane), "label below the strip");
	}

	@ParameterizedTest
	@ValueSource(ints = { 100, 125, 150, 175, 200 })
	@ExtendWith(WithMonitorSpecificScalingExtension.class)
	public void test_imageIsScaledAndCenteredInItsTab(int zoom) {
		Image image = new Image(display, (gc, width, height) -> {
			gc.setBackground(color(SATURATED));
			gc.fillRectangle(0, 0, width, height);
		}, IMAGE_SIZE, IMAGE_SIZE);
		try {
			TabFolder folder = createFolder(SWT.NONE);
			setColors(folder);
			folder.getItem(1).setText("");
			folder.getItem(1).setImage(image);
			DPITestUtil.changeDPIZoom(shell, zoom);

			RECT tab = tabRect(folder, 1);
			Pixels pixels = Pixels.ofClientInPixels(folder);
			RECT painted = pixels.extentOf(SATURATED, tab);
			assertNotNull(painted, "image inside its tab");
			int expectedSize = DPIUtil.pointToPixel(IMAGE_SIZE, zoom);
			assertEquals(expectedSize, painted.right - painted.left, 1, "image width");
			assertEquals(expectedSize, painted.bottom - painted.top, 1, "image height");
			assertEquals(painted.left - tab.left, tab.right - painted.right, 2, "image centered in the tab");
		} finally {
			image.dispose();
		}
	}

	@ParameterizedTest
	@ValueSource(ints = { 100, 125, 150, 175, 200 })
	@ExtendWith(WithMonitorSpecificScalingExtension.class)
	public void test_labelAndPaneFollowTheZoom(int zoom) {
		TabFolder folder = createFolder(SWT.NONE);
		setColors(folder);
		DPITestUtil.changeDPIZoom(shell, 100);
		int heightAt100 = labelHeight(folder);
		DPITestUtil.changeDPIZoom(shell, zoom);

		Pixels pixels = Pixels.ofClientInPixels(folder);
		assertEquals(DARK, pixels.get(pixels.width / 2, pixels.height / 2), "pane");
		// Hinting keeps glyph heights from scaling exactly
		assertEquals(heightAt100 * zoom / 100f, labelHeight(folder), 2, "label height");
	}

	private static int labelHeight(TabFolder folder) {
		RECT label = Pixels.ofClientInPixels(folder).extentOf(LIGHT, tabRect(folder, 1));
		assertNotNull(label, "label inside its tab");
		return label.bottom - label.top;
	}

	private Image createImage(int size) {
		Image image = new Image(display, size, size);
		GC gc = new GC(image);
		gc.setBackground(color(SATURATED));
		gc.fillRectangle(0, 0, size, size);
		gc.dispose();
		return image;
	}

	private TabFolder createFolder(int style) {
		return createFolder(shell, style);
	}

	private TabFolder createFolder(Composite parent, int style) {
		TabFolder folder = new TabFolder(parent, style);
		folder.setBounds(0, 0, 400, 200);
		for (int i = 0; i < 4; i++) {
			TabItem item = new TabItem(folder, SWT.NONE);
			item.setText("EEEEEE");
			item.setControl(new Composite(folder, SWT.NONE));
		}
		return folder;
	}

	private TabFolder createOverflowingFolder() {
		TabFolder folder = createFolder(SWT.NONE);
		folder.setBounds(0, 0, 120, 200);
		return folder;
	}

	private void setColors(TabFolder folder) {
		folder.setBackground(color(DARK));
		folder.setForeground(color(LIGHT));
	}

	private Color color(int pixel) {
		return new Color(pixel & 0xFF, (pixel >> 8) & 0xFF, (pixel >> 16) & 0xFF);
	}

	private static boolean isDark(int pixel) {
		return (pixel & 0xFF) < 128 && ((pixel >> 8) & 0xFF) < 128 && ((pixel >> 16) & 0xFF) < 128;
	}

	private static RECT clientRect(Control control) {
		RECT rect = new RECT();
		OS.GetClientRect(control.handle, rect);
		return rect;
	}

	private static RECT tabRect(TabFolder folder, int index) {
		RECT rect = new RECT();
		OS.SendMessage(folder.handle, OS.TCM_GETITEMRECT, index, rect);
		return rect;
	}

	/** Returns where a rectangle of a mirrored control ends up in device pixels. */
	private static RECT mirrored(RECT rect, RECT client) {
		RECT result = new RECT();
		OS.SetRect(result, client.right - rect.right, rect.top, client.right - rect.left, rect.bottom);
		return result;
	}

	/** The pixels a control paints into a memory device context, in device pixels. */
	private static final class Pixels {
		final int[] values;
		final int width;
		final int height;

		private Pixels(int[] values, int width) {
			this.values = values;
			this.width = width;
			this.height = values.length / width;
		}

		static Pixels ofClient(Control control) {
			RECT rect = clientRect(control);
			return paint(control, OS.WM_PRINTCLIENT, OS.PRF_CLIENT, rect.right, rect.bottom);
		}

		static Pixels ofWindow(Control control) {
			RECT rect = new RECT();
			OS.GetWindowRect(control.handle, rect);
			return paint(control, OS.WM_PRINT, OS.PRF_NONCLIENT | OS.PRF_CLIENT, rect.right - rect.left, rect.bottom - rect.top);
		}

		/** Paints into a device context mirrored like the one of a right-to-left window. */
		static Pixels ofMirroredClient(Control control) {
			return paintInPixels(control, OS.LAYOUT_RTL);
		}

		/** Paints into a bitmap of the client area's size in pixels, whatever the zoom. */
		static Pixels ofClientInPixels(Control control) {
			return paintInPixels(control, 0);
		}

		private static Pixels paintInPixels(Control control, int layout) {
			RECT rect = clientRect(control);
			long screenDC = OS.GetDC(0);
			long memDC = OS.CreateCompatibleDC(screenDC);
			// An Image is sized in points, but the mirroring axis has to match the client area in pixels
			long bitmap = OS.CreateCompatibleBitmap(screenDC, rect.right, rect.bottom);
			long oldBitmap = OS.SelectObject(memDC, bitmap);
			try {
				OS.SetLayout(memDC, layout);
				OS.SendMessage(control.handle, OS.WM_PRINTCLIENT, memDC, OS.PRF_CLIENT);
				OS.SetLayout(memDC, 0);
				return read(memDC, rect.right, rect.bottom);
			} finally {
				OS.SelectObject(memDC, oldBitmap);
				OS.DeleteObject(bitmap);
				OS.DeleteDC(memDC);
				OS.ReleaseDC(0, screenDC);
			}
		}

		private static Pixels paint(Control control, int message, int flags, int width, int height) {
			Image image = new Image(control.getDisplay(), width, height);
			GC gc = new GC(image);
			try {
				OS.SendMessage(control.handle, message, gc.handle, flags);
				return read(gc.handle, width, height);
			} finally {
				gc.dispose();
				image.dispose();
			}
		}

		private static Pixels read(long hDC, int width, int height) {
			int[] values = new int[width * height];
			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					values[y * width + x] = OS.GetPixel(hDC, x, y);
				}
			}
			return new Pixels(values, width);
		}

		int get(int x, int y) {
			return values[y * width + x];
		}

		boolean contains(int pixel, RECT rect) {
			return firstColumnWith(pixel, rect) != -1;
		}

		/** Returns the leftmost column of the visible part of the rectangle that has the pixel, or -1. */
		int firstColumnWith(int pixel, RECT rect) {
			for (int x = Math.max(rect.left, 0); x < Math.min(rect.right, width); x++) {
				for (int y = Math.max(rect.top, 0); y < Math.min(rect.bottom, height); y++) {
					if (get(x, y) == pixel) {
						return x;
					}
				}
			}
			return -1;
		}

		/** Returns the bounds of the pixels of that color inside the rectangle, or null if there are none. */
		RECT extentOf(int pixel, RECT rect) {
			RECT extent = null;
			for (int y = rect.top; y < rect.bottom; y++) {
				for (int x = rect.left; x < rect.right; x++) {
					if (get(x, y) == pixel) {
						if (extent == null) {
							extent = new RECT();
							OS.SetRect(extent, x, y, x + 1, y + 1);
						}
						extent.left = Math.min(extent.left, x);
						extent.right = Math.max(extent.right, x + 1);
						extent.bottom = y + 1;
					}
				}
			}
			return extent;
		}
	}
}
